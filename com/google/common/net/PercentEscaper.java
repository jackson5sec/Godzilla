/*     */ package com.google.common.net;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.escape.UnicodeEscaper;
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
/*     */ public final class PercentEscaper
/*     */   extends UnicodeEscaper
/*     */ {
/*  57 */   private static final char[] PLUS_SIGN = new char[] { '+' };
/*     */ 
/*     */   
/*  60 */   private static final char[] UPPER_HEX_DIGITS = "0123456789ABCDEF".toCharArray();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean plusForSpace;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean[] safeOctets;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PercentEscaper(String safeChars, boolean plusForSpace) {
/*  88 */     Preconditions.checkNotNull(safeChars);
/*     */     
/*  90 */     if (safeChars.matches(".*[0-9A-Za-z].*")) {
/*  91 */       throw new IllegalArgumentException("Alphanumeric characters are always 'safe' and should not be explicitly specified");
/*     */     }
/*     */     
/*  94 */     safeChars = safeChars + "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
/*     */ 
/*     */     
/*  97 */     if (plusForSpace && safeChars.contains(" ")) {
/*  98 */       throw new IllegalArgumentException("plusForSpace cannot be specified when space is a 'safe' character");
/*     */     }
/*     */     
/* 101 */     this.plusForSpace = plusForSpace;
/* 102 */     this.safeOctets = createSafeOctets(safeChars);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean[] createSafeOctets(String safeChars) {
/* 111 */     int maxChar = -1;
/* 112 */     char[] safeCharArray = safeChars.toCharArray();
/* 113 */     for (char c : safeCharArray) {
/* 114 */       maxChar = Math.max(c, maxChar);
/*     */     }
/* 116 */     boolean[] octets = new boolean[maxChar + 1];
/* 117 */     for (char c : safeCharArray) {
/* 118 */       octets[c] = true;
/*     */     }
/* 120 */     return octets;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int nextEscapeIndex(CharSequence csq, int index, int end) {
/* 129 */     Preconditions.checkNotNull(csq);
/* 130 */     for (; index < end; index++) {
/* 131 */       char c = csq.charAt(index);
/* 132 */       if (c >= this.safeOctets.length || !this.safeOctets[c]) {
/*     */         break;
/*     */       }
/*     */     } 
/* 136 */     return index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String escape(String s) {
/* 145 */     Preconditions.checkNotNull(s);
/* 146 */     int slen = s.length();
/* 147 */     for (int index = 0; index < slen; index++) {
/* 148 */       char c = s.charAt(index);
/* 149 */       if (c >= this.safeOctets.length || !this.safeOctets[c]) {
/* 150 */         return escapeSlow(s, index);
/*     */       }
/*     */     } 
/* 153 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected char[] escape(int cp) {
/* 161 */     if (cp < this.safeOctets.length && this.safeOctets[cp])
/* 162 */       return null; 
/* 163 */     if (cp == 32 && this.plusForSpace)
/* 164 */       return PLUS_SIGN; 
/* 165 */     if (cp <= 127) {
/*     */ 
/*     */       
/* 168 */       char[] dest = new char[3];
/* 169 */       dest[0] = '%';
/* 170 */       dest[2] = UPPER_HEX_DIGITS[cp & 0xF];
/* 171 */       dest[1] = UPPER_HEX_DIGITS[cp >>> 4];
/* 172 */       return dest;
/* 173 */     }  if (cp <= 2047) {
/*     */ 
/*     */       
/* 176 */       char[] dest = new char[6];
/* 177 */       dest[0] = '%';
/* 178 */       dest[3] = '%';
/* 179 */       dest[5] = UPPER_HEX_DIGITS[cp & 0xF];
/* 180 */       cp >>>= 4;
/* 181 */       dest[4] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 182 */       cp >>>= 2;
/* 183 */       dest[2] = UPPER_HEX_DIGITS[cp & 0xF];
/* 184 */       cp >>>= 4;
/* 185 */       dest[1] = UPPER_HEX_DIGITS[0xC | cp];
/* 186 */       return dest;
/* 187 */     }  if (cp <= 65535) {
/*     */ 
/*     */       
/* 190 */       char[] dest = new char[9];
/* 191 */       dest[0] = '%';
/* 192 */       dest[1] = 'E';
/* 193 */       dest[3] = '%';
/* 194 */       dest[6] = '%';
/* 195 */       dest[8] = UPPER_HEX_DIGITS[cp & 0xF];
/* 196 */       cp >>>= 4;
/* 197 */       dest[7] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 198 */       cp >>>= 2;
/* 199 */       dest[5] = UPPER_HEX_DIGITS[cp & 0xF];
/* 200 */       cp >>>= 4;
/* 201 */       dest[4] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 202 */       cp >>>= 2;
/* 203 */       dest[2] = UPPER_HEX_DIGITS[cp];
/* 204 */       return dest;
/* 205 */     }  if (cp <= 1114111) {
/* 206 */       char[] dest = new char[12];
/*     */ 
/*     */       
/* 209 */       dest[0] = '%';
/* 210 */       dest[1] = 'F';
/* 211 */       dest[3] = '%';
/* 212 */       dest[6] = '%';
/* 213 */       dest[9] = '%';
/* 214 */       dest[11] = UPPER_HEX_DIGITS[cp & 0xF];
/* 215 */       cp >>>= 4;
/* 216 */       dest[10] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 217 */       cp >>>= 2;
/* 218 */       dest[8] = UPPER_HEX_DIGITS[cp & 0xF];
/* 219 */       cp >>>= 4;
/* 220 */       dest[7] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 221 */       cp >>>= 2;
/* 222 */       dest[5] = UPPER_HEX_DIGITS[cp & 0xF];
/* 223 */       cp >>>= 4;
/* 224 */       dest[4] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
/* 225 */       cp >>>= 2;
/* 226 */       dest[2] = UPPER_HEX_DIGITS[cp & 0x7];
/* 227 */       return dest;
/*     */     } 
/*     */     
/* 230 */     throw new IllegalArgumentException("Invalid unicode character value " + cp);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\net\PercentEscaper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */