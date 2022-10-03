/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.Key;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import javax.crypto.Mac;
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
/*     */ @Immutable
/*     */ final class MacHashFunction
/*     */   extends AbstractHashFunction
/*     */ {
/*     */   private final Mac prototype;
/*     */   private final Key key;
/*     */   private final String toString;
/*     */   private final int bits;
/*     */   private final boolean supportsClone;
/*     */   
/*     */   MacHashFunction(String algorithmName, Key key, String toString) {
/*  46 */     this.prototype = getMac(algorithmName, key);
/*  47 */     this.key = (Key)Preconditions.checkNotNull(key);
/*  48 */     this.toString = (String)Preconditions.checkNotNull(toString);
/*  49 */     this.bits = this.prototype.getMacLength() * 8;
/*  50 */     this.supportsClone = supportsClone(this.prototype);
/*     */   }
/*     */ 
/*     */   
/*     */   public int bits() {
/*  55 */     return this.bits;
/*     */   }
/*     */   
/*     */   private static boolean supportsClone(Mac mac) {
/*     */     try {
/*  60 */       mac.clone();
/*  61 */       return true;
/*  62 */     } catch (CloneNotSupportedException e) {
/*  63 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Mac getMac(String algorithmName, Key key) {
/*     */     try {
/*  69 */       Mac mac = Mac.getInstance(algorithmName);
/*  70 */       mac.init(key);
/*  71 */       return mac;
/*  72 */     } catch (NoSuchAlgorithmException e) {
/*  73 */       throw new IllegalStateException(e);
/*  74 */     } catch (InvalidKeyException e) {
/*  75 */       throw new IllegalArgumentException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher newHasher() {
/*  81 */     if (this.supportsClone) {
/*     */       try {
/*  83 */         return new MacHasher((Mac)this.prototype.clone());
/*  84 */       } catch (CloneNotSupportedException cloneNotSupportedException) {}
/*     */     }
/*     */ 
/*     */     
/*  88 */     return new MacHasher(getMac(this.prototype.getAlgorithm(), this.key));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  93 */     return this.toString;
/*     */   }
/*     */   
/*     */   private static final class MacHasher
/*     */     extends AbstractByteHasher {
/*     */     private final Mac mac;
/*     */     private boolean done;
/*     */     
/*     */     private MacHasher(Mac mac) {
/* 102 */       this.mac = mac;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void update(byte b) {
/* 107 */       checkNotDone();
/* 108 */       this.mac.update(b);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void update(byte[] b) {
/* 113 */       checkNotDone();
/* 114 */       this.mac.update(b);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void update(byte[] b, int off, int len) {
/* 119 */       checkNotDone();
/* 120 */       this.mac.update(b, off, len);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void update(ByteBuffer bytes) {
/* 125 */       checkNotDone();
/* 126 */       Preconditions.checkNotNull(bytes);
/* 127 */       this.mac.update(bytes);
/*     */     }
/*     */     
/*     */     private void checkNotDone() {
/* 131 */       Preconditions.checkState(!this.done, "Cannot re-use a Hasher after calling hash() on it");
/*     */     }
/*     */ 
/*     */     
/*     */     public HashCode hash() {
/* 136 */       checkNotDone();
/* 137 */       this.done = true;
/* 138 */       return HashCode.fromBytesNoCopy(this.mac.doFinal());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\hash\MacHashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */