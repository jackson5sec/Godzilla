/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.BitSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ final class SmallCharMatcher
/*     */   extends CharMatcher.NamedFastMatcher
/*     */ {
/*     */   static final int MAX_SIZE = 1023;
/*     */   private final char[] table;
/*     */   private final boolean containsZero;
/*     */   private final long filter;
/*     */   private static final int C1 = -862048943;
/*     */   private static final int C2 = 461845907;
/*     */   private static final double DESIRED_LOAD_FACTOR = 0.5D;
/*     */   
/*     */   private SmallCharMatcher(char[] table, long filter, boolean containsZero, String description) {
/*  36 */     super(description);
/*  37 */     this.table = table;
/*  38 */     this.filter = filter;
/*  39 */     this.containsZero = containsZero;
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
/*     */   static int smear(int hashCode) {
/*  54 */     return 461845907 * Integer.rotateLeft(hashCode * -862048943, 15);
/*     */   }
/*     */   
/*     */   private boolean checkFilter(int c) {
/*  58 */     return (1L == (0x1L & this.filter >> c));
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
/*     */   @VisibleForTesting
/*     */   static int chooseTableSize(int setSize) {
/*  74 */     if (setSize == 1) {
/*  75 */       return 2;
/*     */     }
/*     */ 
/*     */     
/*  79 */     int tableSize = Integer.highestOneBit(setSize - 1) << 1;
/*  80 */     while (tableSize * 0.5D < setSize) {
/*  81 */       tableSize <<= 1;
/*     */     }
/*  83 */     return tableSize;
/*     */   }
/*     */ 
/*     */   
/*     */   static CharMatcher from(BitSet chars, String description) {
/*  88 */     long filter = 0L;
/*  89 */     int size = chars.cardinality();
/*  90 */     boolean containsZero = chars.get(0);
/*     */     
/*  92 */     char[] table = new char[chooseTableSize(size)];
/*  93 */     int mask = table.length - 1; int c;
/*  94 */     for (c = chars.nextSetBit(0); c != -1; ) {
/*     */       
/*  96 */       filter |= 1L << c;
/*  97 */       int index = smear(c) & mask;
/*     */       
/*     */       for (;; c = chars.nextSetBit(c + 1)) {
/* 100 */         if (table[index] == '\000') {
/* 101 */           table[index] = (char)c;
/*     */         }
/*     */         else {
/*     */           
/* 105 */           index = index + 1 & mask; continue;
/*     */         } 
/*     */       } 
/* 108 */     }  return new SmallCharMatcher(table, filter, containsZero, description);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(char c) {
/* 113 */     if (c == '\000') {
/* 114 */       return this.containsZero;
/*     */     }
/* 116 */     if (!checkFilter(c)) {
/* 117 */       return false;
/*     */     }
/* 119 */     int mask = this.table.length - 1;
/* 120 */     int startingIndex = smear(c) & mask;
/* 121 */     int index = startingIndex;
/*     */     while (true) {
/* 123 */       if (this.table[index] == '\000')
/* 124 */         return false; 
/* 125 */       if (this.table[index] == c) {
/* 126 */         return true;
/*     */       }
/* 128 */       index = index + 1 & mask;
/*     */ 
/*     */       
/* 131 */       if (index == startingIndex)
/* 132 */         return false; 
/*     */     } 
/*     */   }
/*     */   
/*     */   void setBits(BitSet table) {
/* 137 */     if (this.containsZero) {
/* 138 */       table.set(0);
/*     */     }
/* 140 */     for (char c : this.table) {
/* 141 */       if (c != '\000')
/* 142 */         table.set(c); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\SmallCharMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */