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
/*     */ @CanIgnoreReturnValue
/*     */ abstract class AbstractByteHasher
/*     */   extends AbstractHasher
/*     */ {
/*  36 */   private final ByteBuffer scratch = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void update(byte[] b) {
/*  43 */     update(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void update(byte[] b, int off, int len) {
/*  48 */     for (int i = off; i < off + len; i++) {
/*  49 */       update(b[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void update(ByteBuffer b) {
/*  55 */     if (b.hasArray()) {
/*  56 */       update(b.array(), b.arrayOffset() + b.position(), b.remaining());
/*  57 */       b.position(b.limit());
/*     */     } else {
/*  59 */       for (int remaining = b.remaining(); remaining > 0; remaining--) {
/*  60 */         update(b.get());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Hasher update(int bytes) {
/*     */     try {
/*  68 */       update(this.scratch.array(), 0, bytes);
/*     */     } finally {
/*  70 */       this.scratch.clear();
/*     */     } 
/*  72 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putByte(byte b) {
/*  77 */     update(b);
/*  78 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putBytes(byte[] bytes) {
/*  83 */     Preconditions.checkNotNull(bytes);
/*  84 */     update(bytes);
/*  85 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putBytes(byte[] bytes, int off, int len) {
/*  90 */     Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*  91 */     update(bytes, off, len);
/*  92 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putBytes(ByteBuffer bytes) {
/*  97 */     update(bytes);
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putShort(short s) {
/* 103 */     this.scratch.putShort(s);
/* 104 */     return update(2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putInt(int i) {
/* 109 */     this.scratch.putInt(i);
/* 110 */     return update(4);
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putLong(long l) {
/* 115 */     this.scratch.putLong(l);
/* 116 */     return update(8);
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putChar(char c) {
/* 121 */     this.scratch.putChar(c);
/* 122 */     return update(2);
/*     */   }
/*     */   
/*     */   protected abstract void update(byte paramByte);
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\hash\AbstractByteHasher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */