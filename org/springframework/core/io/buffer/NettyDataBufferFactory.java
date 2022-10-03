/*     */ package org.springframework.core.io.buffer;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.CompositeByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NettyDataBufferFactory
/*     */   implements DataBufferFactory
/*     */ {
/*     */   private final ByteBufAllocator byteBufAllocator;
/*     */   
/*     */   public NettyDataBufferFactory(ByteBufAllocator byteBufAllocator) {
/*  51 */     Assert.notNull(byteBufAllocator, "ByteBufAllocator must not be null");
/*  52 */     this.byteBufAllocator = byteBufAllocator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBufAllocator getByteBufAllocator() {
/*  60 */     return this.byteBufAllocator;
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer allocateBuffer() {
/*  65 */     ByteBuf byteBuf = this.byteBufAllocator.buffer();
/*  66 */     return new NettyDataBuffer(byteBuf, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer allocateBuffer(int initialCapacity) {
/*  71 */     ByteBuf byteBuf = this.byteBufAllocator.buffer(initialCapacity);
/*  72 */     return new NettyDataBuffer(byteBuf, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer wrap(ByteBuffer byteBuffer) {
/*  77 */     ByteBuf byteBuf = Unpooled.wrappedBuffer(byteBuffer);
/*  78 */     return new NettyDataBuffer(byteBuf, this);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataBuffer wrap(byte[] bytes) {
/*  83 */     ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
/*  84 */     return new NettyDataBuffer(byteBuf, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NettyDataBuffer wrap(ByteBuf byteBuf) {
/*  93 */     byteBuf.touch();
/*  94 */     return new NettyDataBuffer(byteBuf, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DataBuffer join(List<? extends DataBuffer> dataBuffers) {
/* 103 */     Assert.notEmpty(dataBuffers, "DataBuffer List must not be empty");
/* 104 */     int bufferCount = dataBuffers.size();
/* 105 */     if (bufferCount == 1) {
/* 106 */       return dataBuffers.get(0);
/*     */     }
/* 108 */     CompositeByteBuf composite = this.byteBufAllocator.compositeBuffer(bufferCount);
/* 109 */     for (DataBuffer dataBuffer : dataBuffers) {
/* 110 */       Assert.isInstanceOf(NettyDataBuffer.class, dataBuffer);
/* 111 */       composite.addComponent(true, ((NettyDataBuffer)dataBuffer).getNativeBuffer());
/*     */     } 
/* 113 */     return new NettyDataBuffer((ByteBuf)composite, this);
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
/*     */   public static ByteBuf toByteBuf(DataBuffer buffer) {
/* 125 */     if (buffer instanceof NettyDataBuffer) {
/* 126 */       return ((NettyDataBuffer)buffer).getNativeBuffer();
/*     */     }
/*     */     
/* 129 */     return Unpooled.wrappedBuffer(buffer.asByteBuffer());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 136 */     return "NettyDataBufferFactory (" + this.byteBufAllocator + ")";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\buffer\NettyDataBufferFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */