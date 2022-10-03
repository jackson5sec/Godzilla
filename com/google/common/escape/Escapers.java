/*     */ package com.google.common.escape;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public final class Escapers
/*     */ {
/*     */   public static Escaper nullEscaper() {
/*  42 */     return NULL_ESCAPER;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  47 */   private static final Escaper NULL_ESCAPER = new CharEscaper()
/*     */     {
/*     */       public String escape(String string)
/*     */       {
/*  51 */         return (String)Preconditions.checkNotNull(string);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       protected char[] escape(char c) {
/*  57 */         return null;
/*     */       }
/*     */     };
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
/*     */   public static Builder builder() {
/*  79 */     return new Builder();
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
/*     */   @Beta
/*     */   public static final class Builder
/*     */   {
/*  95 */     private final Map<Character, String> replacementMap = new HashMap<>();
/*  96 */     private char safeMin = Character.MIN_VALUE;
/*  97 */     private char safeMax = Character.MAX_VALUE;
/*  98 */     private String unsafeReplacement = null;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder setSafeRange(char safeMin, char safeMax) {
/* 114 */       this.safeMin = safeMin;
/* 115 */       this.safeMax = safeMax;
/* 116 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder setUnsafeReplacement(String unsafeReplacement) {
/* 129 */       this.unsafeReplacement = unsafeReplacement;
/* 130 */       return this;
/*     */     }
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder addEscape(char c, String replacement) {
/* 145 */       Preconditions.checkNotNull(replacement);
/*     */       
/* 147 */       this.replacementMap.put(Character.valueOf(c), replacement);
/* 148 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Escaper build() {
/* 153 */       return new ArrayBasedCharEscaper(this.replacementMap, this.safeMin, this.safeMax)
/*     */         {
/* 155 */           private final char[] replacementChars = (Escapers.Builder.this.unsafeReplacement != null) ? Escapers.Builder.this.unsafeReplacement.toCharArray() : null;
/*     */ 
/*     */           
/*     */           protected char[] escapeUnsafe(char c) {
/* 159 */             return this.replacementChars;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Builder() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static UnicodeEscaper asUnicodeEscaper(Escaper escaper) {
/* 180 */     Preconditions.checkNotNull(escaper);
/* 181 */     if (escaper instanceof UnicodeEscaper)
/* 182 */       return (UnicodeEscaper)escaper; 
/* 183 */     if (escaper instanceof CharEscaper) {
/* 184 */       return wrap((CharEscaper)escaper);
/*     */     }
/*     */ 
/*     */     
/* 188 */     throw new IllegalArgumentException("Cannot create a UnicodeEscaper from: " + escaper
/* 189 */         .getClass().getName());
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
/*     */   public static String computeReplacement(CharEscaper escaper, char c) {
/* 202 */     return stringOrNull(escaper.escape(c));
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
/*     */   public static String computeReplacement(UnicodeEscaper escaper, int cp) {
/* 215 */     return stringOrNull(escaper.escape(cp));
/*     */   }
/*     */   
/*     */   private static String stringOrNull(char[] in) {
/* 219 */     return (in == null) ? null : new String(in);
/*     */   }
/*     */ 
/*     */   
/*     */   private static UnicodeEscaper wrap(final CharEscaper escaper) {
/* 224 */     return new UnicodeEscaper()
/*     */       {
/*     */         protected char[] escape(int cp)
/*     */         {
/* 228 */           if (cp < 65536) {
/* 229 */             return escaper.escape((char)cp);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 235 */           char[] surrogateChars = new char[2];
/* 236 */           Character.toChars(cp, surrogateChars, 0);
/* 237 */           char[] hiChars = escaper.escape(surrogateChars[0]);
/* 238 */           char[] loChars = escaper.escape(surrogateChars[1]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 244 */           if (hiChars == null && loChars == null)
/*     */           {
/* 246 */             return null;
/*     */           }
/*     */           
/* 249 */           int hiCount = (hiChars != null) ? hiChars.length : 1;
/* 250 */           int loCount = (loChars != null) ? loChars.length : 1;
/* 251 */           char[] output = new char[hiCount + loCount];
/* 252 */           if (hiChars != null) {
/*     */             
/* 254 */             for (int n = 0; n < hiChars.length; n++) {
/* 255 */               output[n] = hiChars[n];
/*     */             }
/*     */           } else {
/* 258 */             output[0] = surrogateChars[0];
/*     */           } 
/* 260 */           if (loChars != null) {
/* 261 */             for (int n = 0; n < loChars.length; n++) {
/* 262 */               output[hiCount + n] = loChars[n];
/*     */             }
/*     */           } else {
/* 265 */             output[hiCount] = surrogateChars[1];
/*     */           } 
/* 267 */           return output;
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\escape\Escapers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */