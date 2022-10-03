/*     */ package org.springframework.core.codec;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.util.Map;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.io.ByteArrayResource;
/*     */ import org.springframework.core.io.InputStreamResource;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.MimeType;
/*     */ import org.springframework.util.MimeTypeUtils;
/*     */ import reactor.core.publisher.Flux;
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
/*     */ public class ResourceDecoder
/*     */   extends AbstractDataBufferDecoder<Resource>
/*     */ {
/*  45 */   public static String FILENAME_HINT = ResourceDecoder.class.getName() + ".filename";
/*     */ 
/*     */   
/*     */   public ResourceDecoder() {
/*  49 */     super(new MimeType[] { MimeTypeUtils.ALL });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canDecode(ResolvableType elementType, @Nullable MimeType mimeType) {
/*  55 */     return (Resource.class.isAssignableFrom(elementType.toClass()) && super
/*  56 */       .canDecode(elementType, mimeType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Flux<Resource> decode(Publisher<DataBuffer> inputStream, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/*  63 */     return Flux.from((Publisher)decodeToMono(inputStream, elementType, mimeType, hints));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Resource decode(DataBuffer dataBuffer, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/*  70 */     final byte[] bytes = new byte[dataBuffer.readableByteCount()];
/*  71 */     dataBuffer.read(bytes);
/*  72 */     DataBufferUtils.release(dataBuffer);
/*     */     
/*  74 */     if (this.logger.isDebugEnabled()) {
/*  75 */       this.logger.debug(Hints.getLogPrefix(hints) + "Read " + bytes.length + " bytes");
/*     */     }
/*     */     
/*  78 */     Class<?> clazz = elementType.toClass();
/*  79 */     final String filename = (hints != null) ? (String)hints.get(FILENAME_HINT) : null;
/*  80 */     if (clazz == InputStreamResource.class) {
/*  81 */       return (Resource)new InputStreamResource(new ByteArrayInputStream(bytes))
/*     */         {
/*     */           public String getFilename() {
/*  84 */             return filename;
/*     */           }
/*     */           
/*     */           public long contentLength() {
/*  88 */             return bytes.length;
/*     */           }
/*     */         };
/*     */     }
/*  92 */     if (Resource.class.isAssignableFrom(clazz)) {
/*  93 */       return (Resource)new ByteArrayResource(bytes)
/*     */         {
/*     */           public String getFilename() {
/*  96 */             return filename;
/*     */           }
/*     */         };
/*     */     }
/*     */     
/* 101 */     throw new IllegalStateException("Unsupported resource class: " + clazz);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\codec\ResourceDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */