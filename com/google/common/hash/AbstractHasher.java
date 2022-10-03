/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
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
/*     */ abstract class AbstractHasher
/*     */   implements Hasher
/*     */ {
/*     */   public final Hasher putBoolean(boolean b) {
/*  32 */     return putByte(b ? 1 : 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Hasher putDouble(double d) {
/*  37 */     return putLong(Double.doubleToRawLongBits(d));
/*     */   }
/*     */ 
/*     */   
/*     */   public final Hasher putFloat(float f) {
/*  42 */     return putInt(Float.floatToRawIntBits(f));
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putUnencodedChars(CharSequence charSequence) {
/*  47 */     for (int i = 0, len = charSequence.length(); i < len; i++) {
/*  48 */       putChar(charSequence.charAt(i));
/*     */     }
/*  50 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putString(CharSequence charSequence, Charset charset) {
/*  55 */     return putBytes(charSequence.toString().getBytes(charset));
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putBytes(byte[] bytes) {
/*  60 */     return putBytes(bytes, 0, bytes.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putBytes(byte[] bytes, int off, int len) {
/*  65 */     Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*  66 */     for (int i = 0; i < len; i++) {
/*  67 */       putByte(bytes[off + i]);
/*     */     }
/*  69 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putBytes(ByteBuffer b) {
/*  74 */     if (b.hasArray()) {
/*  75 */       putBytes(b.array(), b.arrayOffset() + b.position(), b.remaining());
/*  76 */       b.position(b.limit());
/*     */     } else {
/*  78 */       for (int remaining = b.remaining(); remaining > 0; remaining--) {
/*  79 */         putByte(b.get());
/*     */       }
/*     */     } 
/*  82 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putShort(short s) {
/*  87 */     putByte((byte)s);
/*  88 */     putByte((byte)(s >>> 8));
/*  89 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putInt(int i) {
/*  94 */     putByte((byte)i);
/*  95 */     putByte((byte)(i >>> 8));
/*  96 */     putByte((byte)(i >>> 16));
/*  97 */     putByte((byte)(i >>> 24));
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putLong(long l) {
/* 103 */     for (int i = 0; i < 64; i += 8) {
/* 104 */       putByte((byte)(int)(l >>> i));
/*     */     }
/* 106 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher putChar(char c) {
/* 111 */     putByte((byte)c);
/* 112 */     putByte((byte)(c >>> 8));
/* 113 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Hasher putObject(T instance, Funnel<? super T> funnel) {
/* 118 */     funnel.funnel(instance, this);
/* 119 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\hash\AbstractHasher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */