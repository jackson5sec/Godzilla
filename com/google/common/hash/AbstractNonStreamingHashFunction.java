/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Arrays;
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
/*     */ @Immutable
/*     */ abstract class AbstractNonStreamingHashFunction
/*     */   extends AbstractHashFunction
/*     */ {
/*     */   public Hasher newHasher() {
/*  36 */     return newHasher(32);
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher newHasher(int expectedInputSize) {
/*  41 */     Preconditions.checkArgument((expectedInputSize >= 0));
/*  42 */     return new BufferingHasher(expectedInputSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashInt(int input) {
/*  47 */     return hashBytes(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(input).array());
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashLong(long input) {
/*  52 */     return hashBytes(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(input).array());
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashUnencodedChars(CharSequence input) {
/*  57 */     int len = input.length();
/*  58 */     ByteBuffer buffer = ByteBuffer.allocate(len * 2).order(ByteOrder.LITTLE_ENDIAN);
/*  59 */     for (int i = 0; i < len; i++) {
/*  60 */       buffer.putChar(input.charAt(i));
/*     */     }
/*  62 */     return hashBytes(buffer.array());
/*     */   }
/*     */ 
/*     */   
/*     */   public HashCode hashString(CharSequence input, Charset charset) {
/*  67 */     return hashBytes(input.toString().getBytes(charset));
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract HashCode hashBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
/*     */ 
/*     */   
/*     */   public HashCode hashBytes(ByteBuffer input) {
/*  75 */     return newHasher(input.remaining()).putBytes(input).hash();
/*     */   }
/*     */   
/*     */   private final class BufferingHasher
/*     */     extends AbstractHasher {
/*     */     final AbstractNonStreamingHashFunction.ExposedByteArrayOutputStream stream;
/*     */     
/*     */     BufferingHasher(int expectedInputSize) {
/*  83 */       this.stream = new AbstractNonStreamingHashFunction.ExposedByteArrayOutputStream(expectedInputSize);
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putByte(byte b) {
/*  88 */       this.stream.write(b);
/*  89 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putBytes(byte[] bytes, int off, int len) {
/*  94 */       this.stream.write(bytes, off, len);
/*  95 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Hasher putBytes(ByteBuffer bytes) {
/* 100 */       this.stream.write(bytes);
/* 101 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public HashCode hash() {
/* 106 */       return AbstractNonStreamingHashFunction.this.hashBytes(this.stream.byteArray(), 0, this.stream.length());
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ExposedByteArrayOutputStream
/*     */     extends ByteArrayOutputStream {
/*     */     ExposedByteArrayOutputStream(int expectedInputSize) {
/* 113 */       super(expectedInputSize);
/*     */     }
/*     */     
/*     */     void write(ByteBuffer input) {
/* 117 */       int remaining = input.remaining();
/* 118 */       if (this.count + remaining > this.buf.length) {
/* 119 */         this.buf = Arrays.copyOf(this.buf, this.count + remaining);
/*     */       }
/* 121 */       input.get(this.buf, this.count, remaining);
/* 122 */       this.count += remaining;
/*     */     }
/*     */     
/*     */     byte[] byteArray() {
/* 126 */       return this.buf;
/*     */     }
/*     */     
/*     */     int length() {
/* 130 */       return this.count;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\hash\AbstractNonStreamingHashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */