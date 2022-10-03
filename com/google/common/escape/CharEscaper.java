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
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public abstract class CharEscaper
/*     */   extends Escaper
/*     */ {
/*     */   private static final int DEST_PAD_MULTIPLIER = 2;
/*     */   
/*     */   public String escape(String string) {
/*  57 */     Preconditions.checkNotNull(string);
/*     */     
/*  59 */     int length = string.length();
/*  60 */     for (int index = 0; index < length; index++) {
/*  61 */       if (escape(string.charAt(index)) != null) {
/*  62 */         return escapeSlow(string, index);
/*     */       }
/*     */     } 
/*  65 */     return string;
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
/*     */   protected abstract char[] escape(char paramChar);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*  97 */     int slen = s.length();
/*     */ 
/*     */     
/* 100 */     char[] dest = Platform.charBufferFromThreadLocal();
/* 101 */     int destSize = dest.length;
/* 102 */     int destIndex = 0;
/* 103 */     int lastEscape = 0;
/*     */ 
/*     */ 
/*     */     
/* 107 */     for (; index < slen; index++) {
/*     */ 
/*     */       
/* 110 */       char[] r = escape(s.charAt(index));
/*     */ 
/*     */       
/* 113 */       if (r != null) {
/*     */ 
/*     */ 
/*     */         
/* 117 */         int rlen = r.length;
/* 118 */         int charsSkipped = index - lastEscape;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 123 */         int sizeNeeded = destIndex + charsSkipped + rlen;
/* 124 */         if (destSize < sizeNeeded) {
/* 125 */           destSize = sizeNeeded + 2 * (slen - index);
/* 126 */           dest = growBuffer(dest, destIndex, destSize);
/*     */         } 
/*     */ 
/*     */         
/* 130 */         if (charsSkipped > 0) {
/* 131 */           s.getChars(lastEscape, index, dest, destIndex);
/* 132 */           destIndex += charsSkipped;
/*     */         } 
/*     */ 
/*     */         
/* 136 */         if (rlen > 0) {
/* 137 */           System.arraycopy(r, 0, dest, destIndex, rlen);
/* 138 */           destIndex += rlen;
/*     */         } 
/* 140 */         lastEscape = index + 1;
/*     */       } 
/*     */     } 
/*     */     
/* 144 */     int charsLeft = slen - lastEscape;
/* 145 */     if (charsLeft > 0) {
/* 146 */       int sizeNeeded = destIndex + charsLeft;
/* 147 */       if (destSize < sizeNeeded)
/*     */       {
/*     */         
/* 150 */         dest = growBuffer(dest, destIndex, sizeNeeded);
/*     */       }
/* 152 */       s.getChars(lastEscape, slen, dest, destIndex);
/* 153 */       destIndex = sizeNeeded;
/*     */     } 
/* 155 */     return new String(dest, 0, destIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static char[] growBuffer(char[] dest, int index, int size) {
/* 163 */     if (size < 0) {
/* 164 */       throw new AssertionError("Cannot increase internal buffer any further");
/*     */     }
/* 166 */     char[] copy = new char[size];
/* 167 */     if (index > 0) {
/* 168 */       System.arraycopy(dest, 0, copy, 0, index);
/*     */     }
/* 170 */     return copy;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\escape\CharEscaper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */