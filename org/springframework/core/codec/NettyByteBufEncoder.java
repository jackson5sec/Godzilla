/*    */ package org.springframework.core.codec;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Map;
/*    */ import org.reactivestreams.Publisher;
/*    */ import org.springframework.core.ResolvableType;
/*    */ import org.springframework.core.io.buffer.DataBuffer;
/*    */ import org.springframework.core.io.buffer.DataBufferFactory;
/*    */ import org.springframework.core.io.buffer.NettyDataBufferFactory;
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
/*    */ public class NettyByteBufEncoder
/*    */   extends AbstractEncoder<ByteBuf>
/*    */ {
/*    */   public NettyByteBufEncoder() {
/* 42 */     super(new MimeType[] { MimeTypeUtils.ALL });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canEncode(ResolvableType type, @Nullable MimeType mimeType) {
/* 48 */     Class<?> clazz = type.toClass();
/* 49 */     return (super.canEncode(type, mimeType) && ByteBuf.class.isAssignableFrom(clazz));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Flux<DataBuffer> encode(Publisher<? extends ByteBuf> inputStream, DataBufferFactory bufferFactory, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 57 */     return Flux.from(inputStream).map(byteBuffer -> encodeValue(byteBuffer, bufferFactory, elementType, mimeType, hints));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DataBuffer encodeValue(ByteBuf byteBuf, DataBufferFactory bufferFactory, ResolvableType valueType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 65 */     if (this.logger.isDebugEnabled() && !Hints.isLoggingSuppressed(hints)) {
/* 66 */       String logPrefix = Hints.getLogPrefix(hints);
/* 67 */       this.logger.debug(logPrefix + "Writing " + byteBuf.readableBytes() + " bytes");
/*    */     } 
/* 69 */     if (bufferFactory instanceof NettyDataBufferFactory) {
/* 70 */       return (DataBuffer)((NettyDataBufferFactory)bufferFactory).wrap(byteBuf);
/*    */     }
/* 72 */     byte[] bytes = new byte[byteBuf.readableBytes()];
/* 73 */     byteBuf.readBytes(bytes);
/* 74 */     byteBuf.release();
/* 75 */     return bufferFactory.wrap(bytes);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\codec\NettyByteBufEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */