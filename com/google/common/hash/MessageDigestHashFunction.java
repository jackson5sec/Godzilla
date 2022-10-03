/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ final class MessageDigestHashFunction
/*     */   extends AbstractHashFunction
/*     */   implements Serializable
/*     */ {
/*     */   private final MessageDigest prototype;
/*     */   private final int bytes;
/*     */   private final boolean supportsClone;
/*     */   private final String toString;
/*     */   
/*     */   MessageDigestHashFunction(String algorithmName, String toString) {
/*  45 */     this.prototype = getMessageDigest(algorithmName);
/*  46 */     this.bytes = this.prototype.getDigestLength();
/*  47 */     this.toString = (String)Preconditions.checkNotNull(toString);
/*  48 */     this.supportsClone = supportsClone(this.prototype);
/*     */   }
/*     */   
/*     */   MessageDigestHashFunction(String algorithmName, int bytes, String toString) {
/*  52 */     this.toString = (String)Preconditions.checkNotNull(toString);
/*  53 */     this.prototype = getMessageDigest(algorithmName);
/*  54 */     int maxLength = this.prototype.getDigestLength();
/*  55 */     Preconditions.checkArgument((bytes >= 4 && bytes <= maxLength), "bytes (%s) must be >= 4 and < %s", bytes, maxLength);
/*     */     
/*  57 */     this.bytes = bytes;
/*  58 */     this.supportsClone = supportsClone(this.prototype);
/*     */   }
/*     */   
/*     */   private static boolean supportsClone(MessageDigest digest) {
/*     */     try {
/*  63 */       digest.clone();
/*  64 */       return true;
/*  65 */     } catch (CloneNotSupportedException e) {
/*  66 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int bits() {
/*  72 */     return this.bytes * 8;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  77 */     return this.toString;
/*     */   }
/*     */   
/*     */   private static MessageDigest getMessageDigest(String algorithmName) {
/*     */     try {
/*  82 */       return MessageDigest.getInstance(algorithmName);
/*  83 */     } catch (NoSuchAlgorithmException e) {
/*  84 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher newHasher() {
/*  90 */     if (this.supportsClone) {
/*     */       try {
/*  92 */         return new MessageDigestHasher((MessageDigest)this.prototype.clone(), this.bytes);
/*  93 */       } catch (CloneNotSupportedException cloneNotSupportedException) {}
/*     */     }
/*     */ 
/*     */     
/*  97 */     return new MessageDigestHasher(getMessageDigest(this.prototype.getAlgorithm()), this.bytes);
/*     */   }
/*     */   
/*     */   private static final class SerializedForm
/*     */     implements Serializable {
/*     */     private final String algorithmName;
/*     */     private final int bytes;
/*     */     
/*     */     private SerializedForm(String algorithmName, int bytes, String toString) {
/* 106 */       this.algorithmName = algorithmName;
/* 107 */       this.bytes = bytes;
/* 108 */       this.toString = toString;
/*     */     }
/*     */     private final String toString; private static final long serialVersionUID = 0L;
/*     */     private Object readResolve() {
/* 112 */       return new MessageDigestHashFunction(this.algorithmName, this.bytes, this.toString);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 119 */     return new SerializedForm(this.prototype.getAlgorithm(), this.bytes, this.toString);
/*     */   }
/*     */   
/*     */   private static final class MessageDigestHasher
/*     */     extends AbstractByteHasher {
/*     */     private final MessageDigest digest;
/*     */     private final int bytes;
/*     */     private boolean done;
/*     */     
/*     */     private MessageDigestHasher(MessageDigest digest, int bytes) {
/* 129 */       this.digest = digest;
/* 130 */       this.bytes = bytes;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void update(byte b) {
/* 135 */       checkNotDone();
/* 136 */       this.digest.update(b);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void update(byte[] b, int off, int len) {
/* 141 */       checkNotDone();
/* 142 */       this.digest.update(b, off, len);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void update(ByteBuffer bytes) {
/* 147 */       checkNotDone();
/* 148 */       this.digest.update(bytes);
/*     */     }
/*     */     
/*     */     private void checkNotDone() {
/* 152 */       Preconditions.checkState(!this.done, "Cannot re-use a Hasher after calling hash() on it");
/*     */     }
/*     */ 
/*     */     
/*     */     public HashCode hash() {
/* 157 */       checkNotDone();
/* 158 */       this.done = true;
/* 159 */       return (this.bytes == this.digest.getDigestLength()) ? 
/* 160 */         HashCode.fromBytesNoCopy(this.digest.digest()) : 
/* 161 */         HashCode.fromBytesNoCopy(Arrays.copyOf(this.digest.digest(), this.bytes));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\hash\MessageDigestHashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */