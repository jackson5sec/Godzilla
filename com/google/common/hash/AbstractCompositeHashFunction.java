/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.Immutable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ abstract class AbstractCompositeHashFunction
/*     */   extends AbstractHashFunction
/*     */ {
/*     */   final HashFunction[] functions;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   AbstractCompositeHashFunction(HashFunction... functions) {
/*  38 */     for (HashFunction function : functions) {
/*  39 */       Preconditions.checkNotNull(function);
/*     */     }
/*  41 */     this.functions = functions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract HashCode makeHash(Hasher[] paramArrayOfHasher);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Hasher newHasher() {
/*  54 */     Hasher[] hashers = new Hasher[this.functions.length];
/*  55 */     for (int i = 0; i < hashers.length; i++) {
/*  56 */       hashers[i] = this.functions[i].newHasher();
/*     */     }
/*  58 */     return fromHashers(hashers);
/*     */   }
/*     */ 
/*     */   
/*     */   public Hasher newHasher(int expectedInputSize) {
/*  63 */     Preconditions.checkArgument((expectedInputSize >= 0));
/*  64 */     Hasher[] hashers = new Hasher[this.functions.length];
/*  65 */     for (int i = 0; i < hashers.length; i++) {
/*  66 */       hashers[i] = this.functions[i].newHasher(expectedInputSize);
/*     */     }
/*  68 */     return fromHashers(hashers);
/*     */   }
/*     */   
/*     */   private Hasher fromHashers(final Hasher[] hashers) {
/*  72 */     return new Hasher()
/*     */       {
/*     */         public Hasher putByte(byte b) {
/*  75 */           for (Hasher hasher : hashers) {
/*  76 */             hasher.putByte(b);
/*     */           }
/*  78 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Hasher putBytes(byte[] bytes) {
/*  83 */           for (Hasher hasher : hashers) {
/*  84 */             hasher.putBytes(bytes);
/*     */           }
/*  86 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Hasher putBytes(byte[] bytes, int off, int len) {
/*  91 */           for (Hasher hasher : hashers) {
/*  92 */             hasher.putBytes(bytes, off, len);
/*     */           }
/*  94 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Hasher putBytes(ByteBuffer bytes) {
/*  99 */           int pos = bytes.position();
/* 100 */           for (Hasher hasher : hashers) {
/* 101 */             bytes.position(pos);
/* 102 */             hasher.putBytes(bytes);
/*     */           } 
/* 104 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Hasher putShort(short s) {
/* 109 */           for (Hasher hasher : hashers) {
/* 110 */             hasher.putShort(s);
/*     */           }
/* 112 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Hasher putInt(int i) {
/* 117 */           for (Hasher hasher : hashers) {
/* 118 */             hasher.putInt(i);
/*     */           }
/* 120 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Hasher putLong(long l) {
/* 125 */           for (Hasher hasher : hashers) {
/* 126 */             hasher.putLong(l);
/*     */           }
/* 128 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Hasher putFloat(float f) {
/* 133 */           for (Hasher hasher : hashers) {
/* 134 */             hasher.putFloat(f);
/*     */           }
/* 136 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Hasher putDouble(double d) {
/* 141 */           for (Hasher hasher : hashers) {
/* 142 */             hasher.putDouble(d);
/*     */           }
/* 144 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Hasher putBoolean(boolean b) {
/* 149 */           for (Hasher hasher : hashers) {
/* 150 */             hasher.putBoolean(b);
/*     */           }
/* 152 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Hasher putChar(char c) {
/* 157 */           for (Hasher hasher : hashers) {
/* 158 */             hasher.putChar(c);
/*     */           }
/* 160 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Hasher putUnencodedChars(CharSequence chars) {
/* 165 */           for (Hasher hasher : hashers) {
/* 166 */             hasher.putUnencodedChars(chars);
/*     */           }
/* 168 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public Hasher putString(CharSequence chars, Charset charset) {
/* 173 */           for (Hasher hasher : hashers) {
/* 174 */             hasher.putString(chars, charset);
/*     */           }
/* 176 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> Hasher putObject(T instance, Funnel<? super T> funnel) {
/* 181 */           for (Hasher hasher : hashers) {
/* 182 */             hasher.putObject(instance, funnel);
/*     */           }
/* 184 */           return this;
/*     */         }
/*     */ 
/*     */         
/*     */         public HashCode hash() {
/* 189 */           return AbstractCompositeHashFunction.this.makeHash(hashers);
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\hash\AbstractCompositeHashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */