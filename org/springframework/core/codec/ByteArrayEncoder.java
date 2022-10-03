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
/*    */ public class ByteArrayEncoder
/*    */   extends AbstractEncoder<byte[]>
/*    */ {
/*    */   public ByteArrayEncoder() {
/* 40 */     super(new MimeType[] { MimeTypeUtils.ALL });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canEncode(ResolvableType elementType, @Nullable MimeType mimeType) {
/* 46 */     Class<?> clazz = elementType.toClass();
/* 47 */     return (super.canEncode(elementType, mimeType) && byte[].class.isAssignableFrom(clazz));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Flux<DataBuffer> encode(Publisher<? extends byte[]> inputStream, DataBufferFactory bufferFactory, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 56 */     return Flux.from(inputStream).map(bytes -> encodeValue(bytes, bufferFactory, elementType, mimeType, hints));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DataBuffer encodeValue(byte[] bytes, DataBufferFactory bufferFactory, ResolvableType valueType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 64 */     DataBuffer dataBuffer = bufferFactory.wrap(bytes);
/* 65 */     if (this.logger.isDebugEnabled() && !Hints.isLoggingSuppressed(hints)) {
/* 66 */       String logPrefix = Hints.getLogPrefix(hints);
/* 67 */       this.logger.debug(logPrefix + "Writing " + dataBuffer.readableByteCount() + " bytes");
/*    */     } 
/* 69 */     return dataBuffer;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\codec\ByteArrayEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */