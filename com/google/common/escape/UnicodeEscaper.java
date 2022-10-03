/*     */ package com.google.common.escape;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
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
/*     */ public abstract class UnicodeEscaper
/*     */   extends Escaper
/*     */ {
/*     */   private static final int DEST_PAD = 32;
/*     */   
/*     */   protected abstract char[] escape(int paramInt);
/*     */   
/*     */   public String escape(String string) {
/* 102 */     Preconditions.checkNotNull(string);
/* 103 */     int end = string.length();
/* 104 */     int index = nextEscapeIndex(string, 0, end);
/* 105 */     return (index == end) ? string : escapeSlow(string, index);
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
/*     */   protected int nextEscapeIndex(CharSequence csq, int start, int end) {
/* 131 */     int index = start;
/* 132 */     while (index < end) {
/* 133 */       int cp = codePointAt(csq, index, end);
/* 134 */       if (cp < 0 || escape(cp) != null) {
/*     */         break;
/*     */       }
/* 137 */       index += Character.isSupplementaryCodePoint(cp) ? 2 : 1;
/*     */     } 
/* 139 */     return index;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String escapeSlow(String s, int index) {
/* 158 */     int end = s.length();
/*     */ 
/*     */     
/* 161 */     char[] dest = Platform.charBufferFromThreadLocal();
/* 162 */     int destIndex = 0;
/* 163 */     int unescapedChunkStart = 0;
/*     */     
/* 165 */     while (index < end) {
/* 166 */       int cp = codePointAt(s, index, end);
/* 167 */       if (cp < 0) {
/* 168 */         throw new IllegalArgumentException("Trailing high surrogate at end of input");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 173 */       char[] escaped = escape(cp);
/* 174 */       int nextIndex = index + (Character.isSupplementaryCodePoint(cp) ? 2 : 1);
/* 175 */       if (escaped != null) {
/* 176 */         int i = index - unescapedChunkStart;
/*     */ 
/*     */ 
/*     */         
/* 180 */         int sizeNeeded = destIndex + i + escaped.length;
/* 181 */         if (dest.length < sizeNeeded) {
/* 182 */           int destLength = sizeNeeded + end - index + 32;
/* 183 */           dest = growBuffer(dest, destIndex, destLength);
/*     */         } 
/*     */         
/* 186 */         if (i > 0) {
/* 187 */           s.getChars(unescapedChunkStart, index, dest, destIndex);
/* 188 */           destIndex += i;
/*     */         } 
/* 190 */         if (escaped.length > 0) {
/* 191 */           System.arraycopy(escaped, 0, dest, destIndex, escaped.length);
/* 192 */           destIndex += escaped.length;
/*     */         } 
/*     */         
/* 195 */         unescapedChunkStart = nextIndex;
/*     */       } 
/* 197 */       index = nextEscapeIndex(s, nextIndex, end);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 202 */     int charsSkipped = end - unescapedChunkStart;
/* 203 */     if (charsSkipped > 0) {
/* 204 */       int endIndex = destIndex + charsSkipped;
/* 205 */       if (dest.length < endIndex) {
/* 206 */         dest = growBuffer(dest, destIndex, endIndex);
/*     */       }
/* 208 */       s.getChars(unescapedChunkStart, end, dest, destIndex);
/* 209 */       destIndex = endIndex;
/*     */     } 
/* 211 */     return new String(dest, 0, destIndex);
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
/*     */   protected static int codePointAt(CharSequence seq, int index, int end) {
/* 246 */     Preconditions.checkNotNull(seq);
/* 247 */     if (index < end) {
/* 248 */       char c1 = seq.charAt(index++);
/* 249 */       if (c1 < '?' || c1 > '?')
/*     */       {
/* 251 */         return c1; } 
/* 252 */       if (c1 <= '?') {
/*     */         
/* 254 */         if (index == end) {
/* 255 */           return -c1;
/*     */         }
/*     */         
/* 258 */         char c2 = seq.charAt(index);
/* 259 */         if (Character.isLowSurrogate(c2)) {
/* 260 */           return Character.toCodePoint(c1, c2);
/*     */         }
/* 262 */         throw new IllegalArgumentException("Expected low surrogate but got char '" + c2 + "' with value " + c2 + " at index " + index + " in '" + seq + "'");
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 273 */       throw new IllegalArgumentException("Unexpected low surrogate character '" + c1 + "' with value " + c1 + " at index " + (index - 1) + " in '" + seq + "'");
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
/* 285 */     throw new IndexOutOfBoundsException("Index exceeds specified range");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static char[] growBuffer(char[] dest, int index, int size) {
/* 293 */     if (size < 0) {
/* 294 */       throw new AssertionError("Cannot increase internal buffer any further");
/*     */     }
/* 296 */     char[] copy = new char[size];
/* 297 */     if (index > 0) {
/* 298 */       System.arraycopy(dest, 0, copy, 0, index);
/*     */     }
/* 300 */     return copy;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\escape\UnicodeEscaper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */