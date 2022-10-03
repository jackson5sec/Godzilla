/*    */ package org.springframework.core.codec;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.springframework.core.ResolvableType;
/*    */ import org.springframework.core.io.Resource;
/*    */ import org.springframework.core.io.buffer.DataBuffer;
/*    */ import org.springframework.core.io.buffer.DataBufferFactory;
/*    */ import org.springframework.core.io.buffer.DataBufferUtils;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.MimeType;
/*    */ import org.springframework.util.MimeTypeUtils;
/*    */ import reactor.core.publisher.Flux;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ResourceEncoder
/*    */   extends AbstractSingleValueEncoder<Resource>
/*    */ {
/*    */   public static final int DEFAULT_BUFFER_SIZE = 4096;
/*    */   private final int bufferSize;
/*    */   
/*    */   public ResourceEncoder() {
/* 51 */     this(4096);
/*    */   }
/*    */   
/*    */   public ResourceEncoder(int bufferSize) {
/* 55 */     super(new MimeType[] { MimeTypeUtils.APPLICATION_OCTET_STREAM, MimeTypeUtils.ALL });
/* 56 */     Assert.isTrue((bufferSize > 0), "'bufferSize' must be larger than 0");
/* 57 */     this.bufferSize = bufferSize;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canEncode(ResolvableType elementType, @Nullable MimeType mimeType) {
/* 63 */     Class<?> clazz = elementType.toClass();
/* 64 */     return (super.canEncode(elementType, mimeType) && Resource.class.isAssignableFrom(clazz));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Flux<DataBuffer> encode(Resource resource, DataBufferFactory bufferFactory, ResolvableType type, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 71 */     if (this.logger.isDebugEnabled() && !Hints.isLoggingSuppressed(hints)) {
/* 72 */       String logPrefix = Hints.getLogPrefix(hints);
/* 73 */       this.logger.debug(logPrefix + "Writing [" + resource + "]");
/*    */     } 
/* 75 */     return DataBufferUtils.read(resource, bufferFactory, this.bufferSize);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\codec\ResourceEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */