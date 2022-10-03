/*    */ package org.springframework.core.codec;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.reactivestreams.Publisher;
/*    */ import org.springframework.core.ResolvableType;
/*    */ import org.springframework.core.io.buffer.DataBuffer;
/*    */ import org.springframework.core.io.buffer.DataBufferFactory;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public class DataBufferEncoder
/*    */   extends AbstractEncoder<DataBuffer>
/*    */ {
/*    */   public DataBufferEncoder() {
/* 40 */     super(new MimeType[] { MimeTypeUtils.ALL });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canEncode(ResolvableType elementType, @Nullable MimeType mimeType) {
/* 46 */     Class<?> clazz = elementType.toClass();
/* 47 */     return (super.canEncode(elementType, mimeType) && DataBuffer.class.isAssignableFrom(clazz));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Flux<DataBuffer> encode(Publisher<? extends DataBuffer> inputStream, DataBufferFactory bufferFactory, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 55 */     Flux<DataBuffer> flux = Flux.from(inputStream);
/* 56 */     if (this.logger.isDebugEnabled() && !Hints.isLoggingSuppressed(hints)) {
/* 57 */       flux = flux.doOnNext(buffer -> logValue(buffer, hints));
/*    */     }
/* 59 */     return flux;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DataBuffer encodeValue(DataBuffer buffer, DataBufferFactory bufferFactory, ResolvableType valueType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 66 */     if (this.logger.isDebugEnabled() && !Hints.isLoggingSuppressed(hints)) {
/* 67 */       logValue(buffer, hints);
/*    */     }
/* 69 */     return buffer;
/*    */   }
/*    */   
/*    */   private void logValue(DataBuffer buffer, @Nullable Map<String, Object> hints) {
/* 73 */     String logPrefix = Hints.getLogPrefix(hints);
/* 74 */     this.logger.debug(logPrefix + "Writing " + buffer.readableByteCount() + " bytes");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\codec\DataBufferEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */