/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
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
/*     */ @Beta
/*     */ public final class HashingInputStream
/*     */   extends FilterInputStream
/*     */ {
/*     */   private final Hasher hasher;
/*     */   
/*     */   public HashingInputStream(HashFunction hashFunction, InputStream in) {
/*  42 */     super((InputStream)Preconditions.checkNotNull(in));
/*  43 */     this.hasher = (Hasher)Preconditions.checkNotNull(hashFunction.newHasher());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int read() throws IOException {
/*  53 */     int b = this.in.read();
/*  54 */     if (b != -1) {
/*  55 */       this.hasher.putByte((byte)b);
/*     */     }
/*  57 */     return b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int read(byte[] bytes, int off, int len) throws IOException {
/*  67 */     int numOfBytesRead = this.in.read(bytes, off, len);
/*  68 */     if (numOfBytesRead != -1) {
/*  69 */       this.hasher.putBytes(bytes, off, numOfBytesRead);
/*     */     }
/*  71 */     return numOfBytesRead;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/*  81 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mark(int readlimit) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() throws IOException {
/*  95 */     throw new IOException("reset not supported");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HashCode hash() {
/* 103 */     return this.hasher.hash();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\hash\HashingInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */