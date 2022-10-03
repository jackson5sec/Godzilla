/*    */ package org.springframework.core.codec;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import java.util.Map;
/*    */ import org.springframework.core.ResolvableType;
/*    */ import org.springframework.core.io.buffer.DataBuffer;
/*    */ import org.springframework.core.io.buffer.DataBufferUtils;
/*    */ import org.springframework.core.io.buffer.NettyDataBuffer;
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
/*    */ public class NettyByteBufDecoder
/*    */   extends AbstractDataBufferDecoder<ByteBuf>
/*    */ {
/*    */   public NettyByteBufDecoder() {
/* 41 */     super(new MimeType[] { MimeTypeUtils.ALL });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canDecode(ResolvableType elementType, @Nullable MimeType mimeType) {
/* 47 */     return (ByteBuf.class.isAssignableFrom(elementType.toClass()) && super
/* 48 */       .canDecode(elementType, mimeType));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ByteBuf decode(DataBuffer dataBuffer, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 55 */     if (this.logger.isDebugEnabled()) {
/* 56 */       this.logger.debug(Hints.getLogPrefix(hints) + "Read " + dataBuffer.readableByteCount() + " bytes");
/*    */     }
/* 58 */     if (dataBuffer instanceof NettyDataBuffer) {
/* 59 */       return ((NettyDataBuffer)dataBuffer).getNativeBuffer();
/*    */     }
/*    */     
/* 62 */     byte[] bytes = new byte[dataBuffer.readableByteCount()];
/* 63 */     dataBuffer.read(bytes);
/* 64 */     ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
/* 65 */     DataBufferUtils.release(dataBuffer);
/* 66 */     return byteBuf;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\codec\NettyByteBufDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */