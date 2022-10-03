/*     */ package com.google.common.escape;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ArrayBasedCharEscaper
/*     */   extends CharEscaper
/*     */ {
/*     */   private final char[][] replacements;
/*     */   private final int replacementsLength;
/*     */   private final char safeMin;
/*     */   private final char safeMax;
/*     */   
/*     */   protected ArrayBasedCharEscaper(Map<Character, String> replacementMap, char safeMin, char safeMax) {
/*  69 */     this(ArrayBasedEscaperMap.create(replacementMap), safeMin, safeMax);
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
/*     */   protected ArrayBasedCharEscaper(ArrayBasedEscaperMap escaperMap, char safeMin, char safeMax) {
/*  87 */     Preconditions.checkNotNull(escaperMap);
/*  88 */     this.replacements = escaperMap.getReplacementArray();
/*  89 */     this.replacementsLength = this.replacements.length;
/*  90 */     if (safeMax < safeMin) {
/*     */ 
/*     */       
/*  93 */       safeMax = Character.MIN_VALUE;
/*  94 */       safeMin = Character.MAX_VALUE;
/*     */     } 
/*  96 */     this.safeMin = safeMin;
/*  97 */     this.safeMax = safeMax;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String escape(String s) {
/* 106 */     Preconditions.checkNotNull(s);
/* 107 */     for (int i = 0; i < s.length(); i++) {
/* 108 */       char c = s.charAt(i);
/* 109 */       if ((c < this.replacementsLength && this.replacements[c] != null) || c > this.safeMax || c < this.safeMin) {
/* 110 */         return escapeSlow(s, i);
/*     */       }
/*     */     } 
/* 113 */     return s;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final char[] escape(char c) {
/* 123 */     if (c < this.replacementsLength) {
/* 124 */       char[] chars = this.replacements[c];
/* 125 */       if (chars != null) {
/* 126 */         return chars;
/*     */       }
/*     */     } 
/* 129 */     if (c >= this.safeMin && c <= this.safeMax) {
/* 130 */       return null;
/*     */     }
/* 132 */     return escapeUnsafe(c);
/*     */   }
/*     */   
/*     */   protected abstract char[] escapeUnsafe(char paramChar);
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\escape\ArrayBasedCharEscaper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */