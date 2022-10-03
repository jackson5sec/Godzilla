/*    */ package org.springframework.core.codec;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.reactivestreams.Publisher;
/*    */ import org.springframework.core.ResolvableType;
/*    */ import org.springframework.core.io.buffer.DataBuffer;
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
/*    */ public class DataBufferDecoder
/*    */   extends AbstractDataBufferDecoder<DataBuffer>
/*    */ {
/*    */   public DataBufferDecoder() {
/* 49 */     super(new MimeType[] { MimeTypeUtils.ALL });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canDecode(ResolvableType elementType, @Nullable MimeType mimeType) {
/* 55 */     return (DataBuffer.class.isAssignableFrom(elementType.toClass()) && super
/* 56 */       .canDecode(elementType, mimeType));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Flux<DataBuffer> decode(Publisher<DataBuffer> input, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 63 */     return Flux.from(input);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DataBuffer decode(DataBuffer buffer, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 70 */     if (this.logger.isDebugEnabled()) {
/* 71 */       this.logger.debug(Hints.getLogPrefix(hints) + "Read " + buffer.readableByteCount() + " bytes");
/*    */     }
/* 73 */     return buffer;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\codec\DataBufferDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */