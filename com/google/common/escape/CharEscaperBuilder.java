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
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public final class CharEscaperBuilder
/*     */ {
/*     */   private final Map<Character, String> map;
/*     */   
/*     */   private static class CharArrayDecorator
/*     */     extends CharEscaper
/*     */   {
/*     */     private final char[][] replacements;
/*     */     private final int replaceLength;
/*     */     
/*     */     CharArrayDecorator(char[][] replacements) {
/*  47 */       this.replacements = replacements;
/*  48 */       this.replaceLength = replacements.length;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String escape(String s) {
/*  57 */       int slen = s.length();
/*  58 */       for (int index = 0; index < slen; index++) {
/*  59 */         char c = s.charAt(index);
/*  60 */         if (c < this.replacements.length && this.replacements[c] != null) {
/*  61 */           return escapeSlow(s, index);
/*     */         }
/*     */       } 
/*  64 */       return s;
/*     */     }
/*     */ 
/*     */     
/*     */     protected char[] escape(char c) {
/*  69 */       return (c < this.replaceLength) ? this.replacements[c] : null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   private int max = -1;
/*     */ 
/*     */   
/*     */   public CharEscaperBuilder() {
/*  81 */     this.map = new HashMap<>();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public CharEscaperBuilder addEscape(char c, String r) {
/*  87 */     this.map.put(Character.valueOf(c), Preconditions.checkNotNull(r));
/*  88 */     if (c > this.max) {
/*  89 */       this.max = c;
/*     */     }
/*  91 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public CharEscaperBuilder addEscapes(char[] cs, String r) {
/*  97 */     Preconditions.checkNotNull(r);
/*  98 */     for (char c : cs) {
/*  99 */       addEscape(c, r);
/*     */     }
/* 101 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char[][] toArray() {
/* 112 */     char[][] result = new char[this.max + 1][];
/* 113 */     for (Map.Entry<Character, String> entry : this.map.entrySet()) {
/* 114 */       result[((Character)entry.getKey()).charValue()] = ((String)entry.getValue()).toCharArray();
/*     */     }
/* 116 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Escaper toEscaper() {
/* 126 */     return new CharArrayDecorator(toArray());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\escape\CharEscaperBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */