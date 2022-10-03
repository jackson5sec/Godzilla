/*     */ package com.jediterm.terminal.util;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.BitSet;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Util
/*     */ {
/*     */   public static <T> T[] copyOf(T[] original, int newLength) {
/*  13 */     Class<T> type = (Class)original.getClass().getComponentType();
/*  14 */     T[] newArr = (T[])Array.newInstance(type, newLength);
/*     */     
/*  16 */     System.arraycopy(original, 0, newArr, 0, Math.min(original.length, newLength));
/*     */     
/*  18 */     return newArr;
/*     */   }
/*     */   
/*     */   public static int[] copyOf(int[] original, int newLength) {
/*  22 */     int[] newArr = new int[newLength];
/*     */     
/*  24 */     System.arraycopy(original, 0, newArr, 0, Math.min(original.length, newLength));
/*     */     
/*  26 */     return newArr;
/*     */   }
/*     */   
/*     */   public static char[] copyOf(char[] original, int newLength) {
/*  30 */     char[] newArr = new char[newLength];
/*     */     
/*  32 */     System.arraycopy(original, 0, newArr, 0, Math.min(original.length, newLength));
/*     */     
/*  34 */     return newArr;
/*     */   }
/*     */   
/*     */   public static void bitsetCopy(BitSet src, int srcOffset, BitSet dest, int destOffset, int length) {
/*  38 */     for (int i = 0; i < length; i++) {
/*  39 */       dest.set(destOffset + i, src.get(srcOffset + i));
/*     */     }
/*     */   }
/*     */   
/*     */   public static String trimTrailing(String string) {
/*  44 */     int index = string.length() - 1;
/*  45 */     for (; index >= 0 && Character.isWhitespace(string.charAt(index)); index--);
/*  46 */     return string.substring(0, index + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean containsIgnoreCase(@NotNull String where, @NotNull String what) {
/*  51 */     if (where == null) $$$reportNull$$$0(0);  if (what == null) $$$reportNull$$$0(1);  return (indexOfIgnoreCase(where, what, 0) >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int indexOfIgnoreCase(@NotNull String where, @NotNull String what, int fromIndex) {
/*  58 */     if (where == null) $$$reportNull$$$0(2);  if (what == null) $$$reportNull$$$0(3);  int targetCount = what.length();
/*  59 */     int sourceCount = where.length();
/*     */     
/*  61 */     if (fromIndex >= sourceCount) {
/*  62 */       return (targetCount == 0) ? sourceCount : -1;
/*     */     }
/*     */     
/*  65 */     if (fromIndex < 0) {
/*  66 */       fromIndex = 0;
/*     */     }
/*     */     
/*  69 */     if (targetCount == 0) {
/*  70 */       return fromIndex;
/*     */     }
/*     */     
/*  73 */     char first = what.charAt(0);
/*  74 */     int max = sourceCount - targetCount;
/*     */     
/*  76 */     for (int i = fromIndex; i <= max; i++) {
/*     */       
/*  78 */       if (!charsEqualIgnoreCase(where.charAt(i), first)) {
/*  79 */         while (++i <= max && !charsEqualIgnoreCase(where.charAt(i), first));
/*     */       }
/*     */ 
/*     */       
/*  83 */       if (i <= max) {
/*  84 */         int j = i + 1;
/*  85 */         int end = j + targetCount - 1;
/*  86 */         for (int k = 1; j < end && charsEqualIgnoreCase(where.charAt(j), what.charAt(k)); ) { j++; k++; }
/*     */         
/*  88 */         if (j == end)
/*     */         {
/*  90 */           return i;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  95 */     return -1;
/*     */   }
/*     */   
/*     */   public static boolean charsEqualIgnoreCase(char a, char b) {
/*  99 */     return (a == b || toUpperCase(a) == toUpperCase(b) || toLowerCase(a) == toLowerCase(b));
/*     */   }
/*     */   
/*     */   private static char toLowerCase(char b) {
/* 103 */     return Character.toLowerCase(b);
/*     */   }
/*     */   
/*     */   private static char toUpperCase(char a) {
/* 107 */     return Character.toUpperCase(a);
/*     */   }
/*     */   
/*     */   public static int compareVersionNumbers(@Nullable String v1, @Nullable String v2) {
/* 111 */     if (v1 == null && v2 == null) {
/* 112 */       return 0;
/*     */     }
/* 114 */     if (v1 == null) {
/* 115 */       return -1;
/*     */     }
/* 117 */     if (v2 == null) {
/* 118 */       return 1;
/*     */     }
/*     */     
/* 121 */     String[] part1 = v1.split("[\\.\\_\\-]");
/* 122 */     String[] part2 = v2.split("[\\.\\_\\-]");
/*     */     
/* 124 */     int idx = 0;
/* 125 */     for (; idx < part1.length && idx < part2.length; idx++) {
/* 126 */       int cmp; String p1 = part1[idx];
/* 127 */       String p2 = part2[idx];
/*     */ 
/*     */       
/* 130 */       if (p1.matches("\\d+") && p2.matches("\\d+")) {
/* 131 */         cmp = (new Integer(p1)).compareTo(new Integer(p2));
/*     */       } else {
/*     */         
/* 134 */         cmp = part1[idx].compareTo(part2[idx]);
/*     */       } 
/* 136 */       if (cmp != 0) return cmp;
/*     */     
/*     */     } 
/* 139 */     if (part1.length == part2.length) {
/* 140 */       return 0;
/*     */     }
/* 142 */     if (part1.length > idx) {
/* 143 */       return 1;
/*     */     }
/*     */     
/* 146 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\termina\\util\Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */