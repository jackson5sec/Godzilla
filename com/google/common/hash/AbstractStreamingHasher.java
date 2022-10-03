/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
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
/*     */ @CanIgnoreReturnValue
/*     */ abstract class AbstractStreamingHasher
/*     */   extends AbstractHasher
/*     */ {
/*     */   private final ByteBuffer buffer;
/*     */   private final int bufferSize;
/*     */   private final int chunkSize;
/*     */   
/*     */   protected AbstractStreamingHasher(int chunkSize) {
/*  50 */     this(chunkSize, chunkSize);
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
/*     */ 
/*     */   
/*     */   protected AbstractStreamingHasher(int chunkSize, int bufferSize) {
/*  64 */     Preconditions.checkArgument((bufferSize % chunkSize == 0));
/*     */ 
/*     */ 
/*     */     
/*  68 */     this.buffer = ByteBuffer.allocate(bufferSize + 7).order(ByteOrder.LITTLE_ENDIAN);
/*  69 */     this.bufferSize = bufferSize;
/*  70 */     this.chunkSize = chunkSize;
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
/*     */   
/*     */   protected void processRemaining(ByteBuffer bb) {
/*  83 */     bb.position(bb.limit());
/*  84 */     bb.limit(this.chunkSize + 7);
/*  85 */     while (bb.position() < this.chunkSize) {
/*  86 */       bb.putLong(0L);
/*     */     }
/*  88 */     bb.limit(this.chunkSize);
/*  89 */     bb.flip();
/*  90 */     process(bb);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Hasher putBytes(byte[] bytes, int off, int len) {
/*  95 */     return putBytesInternal(ByteBuffer.wrap(bytes, off, len).order(ByteOrder.LITTLE_ENDIAN));
/*     */   }
/*     */ 
/*     */   
/*     */   public final Hasher putBytes(ByteBuffer readBuffer) {
/* 100 */     ByteOrder order = readBuffer.order();
/*     */     try {
/* 102 */       readBuffer.order(ByteOrder.LITTLE_ENDIAN);
/* 103 */       return putBytesInternal(readBuffer);
/*     */     } finally {
/* 105 */       readBuffer.order(order);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Hasher putBytesInternal(ByteBuffer readBuffer) {
/* 111 */     if (readBuffer.remaining() <= this.buffer.remaining()) {
/* 112 */       this.buffer.put(readBuffer);
/* 113 */       munchIfFull();
/* 114 */       return this;
/*     */     } 
/*     */ 
/*     */     
/* 118 */     int bytesToCopy = this.bufferSize - this.buffer.position();
/* 119 */     for (int i = 0; i < bytesToCopy; i++) {
/* 120 */       this.buffer.put(readBuffer.get());
/*     */     }
/* 122 */     munch();
/*     */ 
/*     */     
/* 125 */     while (readBuffer.remaining() >= this.chunkSize) {
/* 126 */       process(readBuffer);
/*     */     }
/*     */ 
/*     */     
/* 130 */     this.buffer.put(readBuffer);
/* 131 */     return this;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public final Hasher putByte(byte b) {
/* 146 */     this.buffer.put(b);
/* 147 */     munchIfFull();
/* 148 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Hasher putShort(short s) {
/* 153 */     this.buffer.putShort(s);
/* 154 */     munchIfFull();
/* 155 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Hasher putChar(char c) {
/* 160 */     this.buffer.putChar(c);
/* 161 */     munchIfFull();
/* 162 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Hasher putInt(int i) {
/* 167 */     this.buffer.putInt(i);
/* 168 */     munchIfFull();
/* 169 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Hasher putLong(long l) {
/* 174 */     this.buffer.putLong(l);
/* 175 */     munchIfFull();
/* 176 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final HashCode hash() {
/* 181 */     munch();
/* 182 */     this.buffer.flip();
/* 183 */     if (this.buffer.remaining() > 0) {
/* 184 */       processRemaining(this.buffer);
/* 185 */       this.buffer.position(this.buffer.limit());
/*     */     } 
/* 187 */     return makeHash();
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
/*     */   private void munchIfFull() {
/* 199 */     if (this.buffer.remaining() < 8)
/*     */     {
/* 201 */       munch();
/*     */     }
/*     */   }
/*     */   
/*     */   private void munch() {
/* 206 */     this.buffer.flip();
/* 207 */     while (this.buffer.remaining() >= this.chunkSize)
/*     */     {
/*     */       
/* 210 */       process(this.buffer);
/*     */     }
/* 212 */     this.buffer.compact();
/*     */   }
/*     */   
/*     */   protected abstract void process(ByteBuffer paramByteBuffer);
/*     */   
/*     */   protected abstract HashCode makeHash();
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\hash\AbstractStreamingHasher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */