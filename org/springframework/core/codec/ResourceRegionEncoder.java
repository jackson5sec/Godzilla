/*     */ package org.springframework.core.codec;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Map;
/*     */ import java.util.OptionalLong;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.io.InputStreamResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.core.io.buffer.DataBufferUtils;
/*     */ import org.springframework.core.io.support.ResourceRegion;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.MimeType;
/*     */ import org.springframework.util.MimeTypeUtils;
/*     */ import reactor.core.publisher.Flux;
/*     */ import reactor.core.publisher.Mono;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceRegionEncoder
/*     */   extends AbstractEncoder<ResourceRegion>
/*     */ {
/*     */   public static final int DEFAULT_BUFFER_SIZE = 4096;
/*  57 */   public static final String BOUNDARY_STRING_HINT = ResourceRegionEncoder.class.getName() + ".boundaryString";
/*     */   
/*     */   private final int bufferSize;
/*     */ 
/*     */   
/*     */   public ResourceRegionEncoder() {
/*  63 */     this(4096);
/*     */   }
/*     */   
/*     */   public ResourceRegionEncoder(int bufferSize) {
/*  67 */     super(new MimeType[] { MimeTypeUtils.APPLICATION_OCTET_STREAM, MimeTypeUtils.ALL });
/*  68 */     Assert.isTrue((bufferSize > 0), "'bufferSize' must be larger than 0");
/*  69 */     this.bufferSize = bufferSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canEncode(ResolvableType elementType, @Nullable MimeType mimeType) {
/*  74 */     return (super.canEncode(elementType, mimeType) && ResourceRegion.class
/*  75 */       .isAssignableFrom(elementType.toClass()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Flux<DataBuffer> encode(Publisher<? extends ResourceRegion> input, DataBufferFactory bufferFactory, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/*  83 */     Assert.notNull(input, "'inputStream' must not be null");
/*  84 */     Assert.notNull(bufferFactory, "'bufferFactory' must not be null");
/*  85 */     Assert.notNull(elementType, "'elementType' must not be null");
/*     */     
/*  87 */     if (input instanceof Mono) {
/*  88 */       return Mono.from(input)
/*  89 */         .flatMapMany(region -> !region.getResource().isReadable() ? Flux.error((Throwable)new EncodingException("Resource " + region.getResource() + " is not readable")) : writeResourceRegion(region, bufferFactory, hints));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  98 */     String boundaryString = Hints.<String>getRequiredHint(hints, BOUNDARY_STRING_HINT);
/*  99 */     byte[] startBoundary = toAsciiBytes("\r\n--" + boundaryString + "\r\n");
/* 100 */     byte[] contentType = (mimeType != null) ? toAsciiBytes("Content-Type: " + mimeType + "\r\n") : new byte[0];
/*     */     
/* 102 */     return Flux.from(input)
/* 103 */       .concatMap(region -> {
/*     */           if (!region.getResource().isReadable()) {
/*     */             return (Publisher)Flux.error((Throwable)new EncodingException("Resource " + region.getResource() + " is not readable"));
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           Flux<DataBuffer> prefix = Flux.just((Object[])new DataBuffer[] { bufferFactory.wrap(startBoundary), bufferFactory.wrap(contentType), bufferFactory.wrap(getContentRangeHeader(region)) });
/*     */ 
/*     */ 
/*     */           
/*     */           return (Publisher)prefix.concatWith((Publisher)writeResourceRegion(region, bufferFactory, hints));
/* 115 */         }).concatWithValues((Object[])new DataBuffer[] { getRegionSuffix(bufferFactory, boundaryString) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Flux<DataBuffer> writeResourceRegion(ResourceRegion region, DataBufferFactory bufferFactory, @Nullable Map<String, Object> hints) {
/* 123 */     Resource resource = region.getResource();
/* 124 */     long position = region.getPosition();
/* 125 */     long count = region.getCount();
/*     */     
/* 127 */     if (this.logger.isDebugEnabled() && !Hints.isLoggingSuppressed(hints)) {
/* 128 */       this.logger.debug(Hints.getLogPrefix(hints) + "Writing region " + position + "-" + (position + count) + " of [" + resource + "]");
/*     */     }
/*     */ 
/*     */     
/* 132 */     Flux<DataBuffer> in = DataBufferUtils.read(resource, position, bufferFactory, this.bufferSize);
/* 133 */     if (this.logger.isDebugEnabled()) {
/* 134 */       in = in.doOnNext(buffer -> Hints.touchDataBuffer(buffer, hints, this.logger));
/*     */     }
/* 136 */     return DataBufferUtils.takeUntilByteCount((Publisher)in, count);
/*     */   }
/*     */   
/*     */   private DataBuffer getRegionSuffix(DataBufferFactory bufferFactory, String boundaryString) {
/* 140 */     byte[] endBoundary = toAsciiBytes("\r\n--" + boundaryString + "--");
/* 141 */     return bufferFactory.wrap(endBoundary);
/*     */   }
/*     */   
/*     */   private byte[] toAsciiBytes(String in) {
/* 145 */     return in.getBytes(StandardCharsets.US_ASCII);
/*     */   }
/*     */   
/*     */   private byte[] getContentRangeHeader(ResourceRegion region) {
/* 149 */     long start = region.getPosition();
/* 150 */     long end = start + region.getCount() - 1L;
/* 151 */     OptionalLong contentLength = contentLength(region.getResource());
/* 152 */     if (contentLength.isPresent()) {
/* 153 */       long length = contentLength.getAsLong();
/* 154 */       return toAsciiBytes("Content-Range: bytes " + start + '-' + end + '/' + length + "\r\n\r\n");
/*     */     } 
/*     */     
/* 157 */     return toAsciiBytes("Content-Range: bytes " + start + '-' + end + "\r\n\r\n");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private OptionalLong contentLength(Resource resource) {
/* 169 */     if (InputStreamResource.class != resource.getClass()) {
/*     */       try {
/* 171 */         return OptionalLong.of(resource.contentLength());
/*     */       }
/* 173 */       catch (IOException iOException) {}
/*     */     }
/*     */     
/* 176 */     return OptionalLong.empty();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\codec\ResourceRegionEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */