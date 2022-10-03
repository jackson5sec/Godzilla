/*    */ package org.springframework.core.codec;
/*    */ 
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
/*    */ public class ByteArrayDecoder
/*    */   extends AbstractDataBufferDecoder<byte[]>
/*    */ {
/*    */   public ByteArrayDecoder() {
/* 38 */     super(new MimeType[] { MimeTypeUtils.ALL });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canDecode(ResolvableType elementType, @Nullable MimeType mimeType) {
/* 44 */     return (elementType.resolve() == byte[].class && super.canDecode(elementType, mimeType));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] decode(DataBuffer dataBuffer, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 51 */     byte[] result = new byte[dataBuffer.readableByteCount()];
/* 52 */     dataBuffer.read(result);
/* 53 */     DataBufferUtils.release(dataBuffer);
/* 54 */     if (this.logger.isDebugEnabled()) {
/* 55 */       this.logger.debug(Hints.getLogPrefix(hints) + "Read " + result.length + " bytes");
/*    */     }
/* 57 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\codec\ByteArrayDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */