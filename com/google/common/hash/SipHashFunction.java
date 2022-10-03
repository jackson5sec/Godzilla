/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
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
/*     */ @Immutable
/*     */ final class SipHashFunction
/*     */   extends AbstractHashFunction
/*     */   implements Serializable
/*     */ {
/*  38 */   static final HashFunction SIP_HASH_24 = new SipHashFunction(2, 4, 506097522914230528L, 1084818905618843912L);
/*     */ 
/*     */   
/*     */   private final int c;
/*     */ 
/*     */   
/*     */   private final int d;
/*     */ 
/*     */   
/*     */   private final long k0;
/*     */ 
/*     */   
/*     */   private final long k1;
/*     */   
/*     */   private static final long serialVersionUID = 0L;
/*     */ 
/*     */   
/*     */   SipHashFunction(int c, int d, long k0, long k1) {
/*  56 */     Preconditions.checkArgument((c > 0), "The number of SipRound iterations (c=%s) during Compression must be positive.", c);
/*     */     
/*  58 */     Preconditions.checkArgument((d > 0), "The number of SipRound iterations (d=%s) during Finalization must be positive.", d);
/*     */     
/*  60 */     this.c = c;
/*  61 */     this.d = d;
/*  62 */     this.k0 = k0;
/*  63 */     this.k1 = k1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int bits() {
/*  68 */     return 64;
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher newHasher() {
/*  73 */     return new SipHasher(this.c, this.d, this.k0, this.k1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  80 */     return "Hashing.sipHash" + this.c + "" + this.d + "(" + this.k0 + ", " + this.k1 + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/*  85 */     if (object instanceof SipHashFunction) {
/*  86 */       SipHashFunction other = (SipHashFunction)object;
/*  87 */       return (this.c == other.c && this.d == other.d && this.k0 == other.k0 && this.k1 == other.k1);
/*     */     } 
/*  89 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  94 */     return (int)((getClass().hashCode() ^ this.c ^ this.d) ^ this.k0 ^ this.k1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class SipHasher
/*     */     extends AbstractStreamingHasher
/*     */   {
/*     */     private static final int CHUNK_SIZE = 8;
/*     */ 
/*     */     
/*     */     private final int c;
/*     */     
/*     */     private final int d;
/*     */     
/* 109 */     private long v0 = 8317987319222330741L;
/* 110 */     private long v1 = 7237128888997146477L;
/* 111 */     private long v2 = 7816392313619706465L;
/* 112 */     private long v3 = 8387220255154660723L;
/*     */ 
/*     */     
/* 115 */     private long b = 0L;
/*     */ 
/*     */ 
/*     */     
/* 119 */     private long finalM = 0L;
/*     */     
/*     */     SipHasher(int c, int d, long k0, long k1) {
/* 122 */       super(8);
/* 123 */       this.c = c;
/* 124 */       this.d = d;
/* 125 */       this.v0 ^= k0;
/* 126 */       this.v1 ^= k1;
/* 127 */       this.v2 ^= k0;
/* 128 */       this.v3 ^= k1;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void process(ByteBuffer buffer) {
/* 133 */       this.b += 8L;
/* 134 */       processM(buffer.getLong());
/*     */     }
/*     */ 
/*     */     
/*     */     protected void processRemaining(ByteBuffer buffer) {
/* 139 */       this.b += buffer.remaining();
/* 140 */       for (int i = 0; buffer.hasRemaining(); i += 8) {
/* 141 */         this.finalM ^= (buffer.get() & 0xFFL) << i;
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public HashCode makeHash() {
/* 148 */       this.finalM ^= this.b << 56L;
/* 149 */       processM(this.finalM);
/*     */ 
/*     */       
/* 152 */       this.v2 ^= 0xFFL;
/* 153 */       sipRound(this.d);
/* 154 */       return HashCode.fromLong(this.v0 ^ this.v1 ^ this.v2 ^ this.v3);
/*     */     }
/*     */     
/*     */     private void processM(long m) {
/* 158 */       this.v3 ^= m;
/* 159 */       sipRound(this.c);
/* 160 */       this.v0 ^= m;
/*     */     }
/*     */     
/*     */     private void sipRound(int iterations) {
/* 164 */       for (int i = 0; i < iterations; i++) {
/* 165 */         this.v0 += this.v1;
/* 166 */         this.v2 += this.v3;
/* 167 */         this.v1 = Long.rotateLeft(this.v1, 13);
/* 168 */         this.v3 = Long.rotateLeft(this.v3, 16);
/* 169 */         this.v1 ^= this.v0;
/* 170 */         this.v3 ^= this.v2;
/* 171 */         this.v0 = Long.rotateLeft(this.v0, 32);
/* 172 */         this.v2 += this.v1;
/* 173 */         this.v0 += this.v3;
/* 174 */         this.v1 = Long.rotateLeft(this.v1, 17);
/* 175 */         this.v3 = Long.rotateLeft(this.v3, 21);
/* 176 */         this.v1 ^= this.v2;
/* 177 */         this.v3 ^= this.v0;
/* 178 */         this.v2 = Long.rotateLeft(this.v2, 32);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\hash\SipHashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */