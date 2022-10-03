/*    */ package org.springframework.core.codec;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.Map;
/*    */ import org.springframework.core.ResolvableType;
/*    */ import org.springframework.core.io.buffer.DataBuffer;
/*    */ import org.springframework.core.io.buffer.DataBufferUtils;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.MimeType;
/*    */ import org.springframework.util.MimeTypeUtils;
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
/*    */ public class ByteBufferDecoder
/*    */   extends AbstractDataBufferDecoder<ByteBuffer>
/*    */ {
/*    */   public ByteBufferDecoder() {
/* 40 */     super(new MimeType[] { MimeTypeUtils.ALL });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canDecode(ResolvableType elementType, @Nullable MimeType mimeType) {
/* 46 */     return (ByteBuffer.class.isAssignableFrom(elementType.toClass()) && super
/* 47 */       .canDecode(elementType, mimeType));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ByteBuffer decode(DataBuffer dataBuffer, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 54 */     int byteCount = dataBuffer.readableByteCount();
/* 55 */     ByteBuffer copy = ByteBuffer.allocate(byteCount);
/* 56 */     copy.put(dataBuffer.asByteBuffer());
/* 57 */     copy.flip();
/* 58 */     DataBufferUtils.release(dataBuffer);
/* 59 */     if (this.logger.isDebugEnabled()) {
/* 60 */       this.logger.debug(Hints.getLogPrefix(hints) + "Read " + byteCount + " bytes");
/*    */     }
/* 62 */     return copy;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\codec\ByteBufferDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */