/*     */ package org.springframework.core.io.buffer;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufInputStream;
/*     */ import io.netty.buffer.ByteBufOutputStream;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.function.IntPredicate;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public class NettyDataBuffer
/*     */   implements PooledDataBuffer
/*     */ {
/*     */   private final ByteBuf byteBuf;
/*     */   private final NettyDataBufferFactory dataBufferFactory;
/*     */   
/*     */   NettyDataBuffer(ByteBuf byteBuf, NettyDataBufferFactory dataBufferFactory) {
/*  55 */     Assert.notNull(byteBuf, "ByteBuf must not be null");
/*  56 */     Assert.notNull(dataBufferFactory, "NettyDataBufferFactory must not be null");
/*  57 */     this.byteBuf = byteBuf;
/*  58 */     this.dataBufferFactory = dataBufferFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuf getNativeBuffer() {
/*  67 */     return this.byteBuf;
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBufferFactory factory() {
/*  72 */     return this.dataBufferFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(IntPredicate predicate, int fromIndex) {
/*  77 */     Assert.notNull(predicate, "IntPredicate must not be null");
/*  78 */     if (fromIndex < 0) {
/*  79 */       fromIndex = 0;
/*     */     }
/*  81 */     else if (fromIndex >= this.byteBuf.writerIndex()) {
/*  82 */       return -1;
/*     */     } 
/*  84 */     int length = this.byteBuf.writerIndex() - fromIndex;
/*  85 */     return this.byteBuf.forEachByte(fromIndex, length, predicate.negate()::test);
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(IntPredicate predicate, int fromIndex) {
/*  90 */     Assert.notNull(predicate, "IntPredicate must not be null");
/*  91 */     if (fromIndex < 0) {
/*  92 */       return -1;
/*     */     }
/*  94 */     fromIndex = Math.min(fromIndex, this.byteBuf.writerIndex() - 1);
/*  95 */     return this.byteBuf.forEachByteDesc(0, fromIndex + 1, predicate.negate()::test);
/*     */   }
/*     */ 
/*     */   
/*     */   public int readableByteCount() {
/* 100 */     return this.byteBuf.readableBytes();
/*     */   }
/*     */ 
/*     */   
/*     */   public int writableByteCount() {
/* 105 */     return this.byteBuf.writableBytes();
/*     */   }
/*     */ 
/*     */   
/*     */   public int readPosition() {
/* 110 */     return this.byteBuf.readerIndex();
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer readPosition(int readPosition) {
/* 115 */     this.byteBuf.readerIndex(readPosition);
/* 116 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int writePosition() {
/* 121 */     return this.byteBuf.writerIndex();
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer writePosition(int writePosition) {
/* 126 */     this.byteBuf.writerIndex(writePosition);
/* 127 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getByte(int index) {
/* 132 */     return this.byteBuf.getByte(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 137 */     return this.byteBuf.capacity();
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer capacity(int capacity) {
/* 142 */     this.byteBuf.capacity(capacity);
/* 143 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataBuffer ensureCapacity(int capacity) {
/* 148 */     this.byteBuf.ensureWritable(capacity);
/* 149 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte read() {
/* 154 */     return this.byteBuf.readByte();
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer read(byte[] destination) {
/* 159 */     this.byteBuf.readBytes(destination);
/* 160 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer read(byte[] destination, int offset, int length) {
/* 165 */     this.byteBuf.readBytes(destination, offset, length);
/* 166 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer write(byte b) {
/* 171 */     this.byteBuf.writeByte(b);
/* 172 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer write(byte[] source) {
/* 177 */     this.byteBuf.writeBytes(source);
/* 178 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer write(byte[] source, int offset, int length) {
/* 183 */     this.byteBuf.writeBytes(source, offset, length);
/* 184 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer write(DataBuffer... buffers) {
/* 189 */     if (!ObjectUtils.isEmpty((Object[])buffers)) {
/* 190 */       if (hasNettyDataBuffers(buffers)) {
/* 191 */         ByteBuf[] nativeBuffers = new ByteBuf[buffers.length];
/* 192 */         for (int i = 0; i < buffers.length; i++) {
/* 193 */           nativeBuffers[i] = ((NettyDataBuffer)buffers[i]).getNativeBuffer();
/*     */         }
/* 195 */         write(nativeBuffers);
/*     */       } else {
/*     */         
/* 198 */         ByteBuffer[] byteBuffers = new ByteBuffer[buffers.length];
/* 199 */         for (int i = 0; i < buffers.length; i++) {
/* 200 */           byteBuffers[i] = buffers[i].asByteBuffer();
/*     */         }
/*     */         
/* 203 */         write(byteBuffers);
/*     */       } 
/*     */     }
/* 206 */     return this;
/*     */   }
/*     */   
/*     */   private static boolean hasNettyDataBuffers(DataBuffer[] buffers) {
/* 210 */     for (DataBuffer buffer : buffers) {
/* 211 */       if (!(buffer instanceof NettyDataBuffer)) {
/* 212 */         return false;
/*     */       }
/*     */     } 
/* 215 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer write(ByteBuffer... buffers) {
/* 220 */     if (!ObjectUtils.isEmpty((Object[])buffers)) {
/* 221 */       for (ByteBuffer buffer : buffers) {
/* 222 */         this.byteBuf.writeBytes(buffer);
/*     */       }
/*     */     }
/* 225 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NettyDataBuffer write(ByteBuf... byteBufs) {
/* 235 */     if (!ObjectUtils.isEmpty((Object[])byteBufs)) {
/* 236 */       for (ByteBuf byteBuf : byteBufs) {
/* 237 */         this.byteBuf.writeBytes(byteBuf);
/*     */       }
/*     */     }
/* 240 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataBuffer write(CharSequence charSequence, Charset charset) {
/* 245 */     Assert.notNull(charSequence, "CharSequence must not be null");
/* 246 */     Assert.notNull(charset, "Charset must not be null");
/* 247 */     if (StandardCharsets.UTF_8.equals(charset)) {
/* 248 */       ByteBufUtil.writeUtf8(this.byteBuf, charSequence);
/*     */     }
/* 250 */     else if (StandardCharsets.US_ASCII.equals(charset)) {
/* 251 */       ByteBufUtil.writeAscii(this.byteBuf, charSequence);
/*     */     } else {
/*     */       
/* 254 */       return super.write(charSequence, charset);
/*     */     } 
/* 256 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer slice(int index, int length) {
/* 261 */     ByteBuf slice = this.byteBuf.slice(index, length);
/* 262 */     return new NettyDataBuffer(slice, this.dataBufferFactory);
/*     */   }
/*     */ 
/*     */   
/*     */   public NettyDataBuffer retainedSlice(int index, int length) {
/* 267 */     ByteBuf slice = this.byteBuf.retainedSlice(index, length);
/* 268 */     return new NettyDataBuffer(slice, this.dataBufferFactory);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer asByteBuffer() {
/* 273 */     return this.byteBuf.nioBuffer();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer asByteBuffer(int index, int length) {
/* 278 */     return this.byteBuf.nioBuffer(index, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream asInputStream() {
/* 283 */     return (InputStream)new ByteBufInputStream(this.byteBuf);
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream asInputStream(boolean releaseOnClose) {
/* 288 */     return (InputStream)new ByteBufInputStream(this.byteBuf, releaseOnClose);
/*     */   }
/*     */ 
/*     */   
/*     */   public OutputStream asOutputStream() {
/* 293 */     return (OutputStream)new ByteBufOutputStream(this.byteBuf);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString(Charset charset) {
/* 298 */     Assert.notNull(charset, "Charset must not be null");
/* 299 */     return this.byteBuf.toString(charset);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString(int index, int length, Charset charset) {
/* 304 */     Assert.notNull(charset, "Charset must not be null");
/* 305 */     return this.byteBuf.toString(index, length, charset);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAllocated() {
/* 310 */     return (this.byteBuf.refCnt() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public PooledDataBuffer retain() {
/* 315 */     return new NettyDataBuffer(this.byteBuf.retain(), this.dataBufferFactory);
/*     */   }
/*     */ 
/*     */   
/*     */   public PooledDataBuffer touch(Object hint) {
/* 320 */     this.byteBuf.touch(hint);
/* 321 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean release() {
/* 326 */     return this.byteBuf.release();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 332 */     return (this == other || (other instanceof NettyDataBuffer && this.byteBuf
/* 333 */       .equals(((NettyDataBuffer)other).byteBuf)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 338 */     return this.byteBuf.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 343 */     return this.byteBuf.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\buffer\NettyDataBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */