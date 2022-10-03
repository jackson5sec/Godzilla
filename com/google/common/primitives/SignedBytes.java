/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public final class SignedBytes
/*     */ {
/*     */   public static final byte MAX_POWER_OF_TWO = 64;
/*     */   
/*     */   public static byte checkedCast(long value) {
/*  58 */     byte result = (byte)(int)value;
/*  59 */     Preconditions.checkArgument((result == value), "Out of range: %s", value);
/*  60 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte saturatedCast(long value) {
/*  71 */     if (value > 127L) {
/*  72 */       return Byte.MAX_VALUE;
/*     */     }
/*  74 */     if (value < -128L) {
/*  75 */       return Byte.MIN_VALUE;
/*     */     }
/*  77 */     return (byte)(int)value;
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
/*     */   public static int compare(byte a, byte b) {
/*  94 */     return a - b;
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
/*     */   public static byte min(byte... array) {
/* 106 */     Preconditions.checkArgument((array.length > 0));
/* 107 */     byte min = array[0];
/* 108 */     for (int i = 1; i < array.length; i++) {
/* 109 */       if (array[i] < min) {
/* 110 */         min = array[i];
/*     */       }
/*     */     } 
/* 113 */     return min;
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
/*     */   public static byte max(byte... array) {
/* 125 */     Preconditions.checkArgument((array.length > 0));
/* 126 */     byte max = array[0];
/* 127 */     for (int i = 1; i < array.length; i++) {
/* 128 */       if (array[i] > max) {
/* 129 */         max = array[i];
/*     */       }
/*     */     } 
/* 132 */     return max;
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
/*     */   public static String join(String separator, byte... array) {
/* 144 */     Preconditions.checkNotNull(separator);
/* 145 */     if (array.length == 0) {
/* 146 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 150 */     StringBuilder builder = new StringBuilder(array.length * 5);
/* 151 */     builder.append(array[0]);
/* 152 */     for (int i = 1; i < array.length; i++) {
/* 153 */       builder.append(separator).append(array[i]);
/*     */     }
/* 155 */     return builder.toString();
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
/*     */   public static Comparator<byte[]> lexicographicalComparator() {
/* 173 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<byte[]> {
/* 177 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(byte[] left, byte[] right) {
/* 181 */       int minLength = Math.min(left.length, right.length);
/* 182 */       for (int i = 0; i < minLength; i++) {
/* 183 */         int result = SignedBytes.compare(left[i], right[i]);
/* 184 */         if (result != 0) {
/* 185 */           return result;
/*     */         }
/*     */       } 
/* 188 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 193 */       return "SignedBytes.lexicographicalComparator()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(byte[] array) {
/* 203 */     Preconditions.checkNotNull(array);
/* 204 */     sortDescending(array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(byte[] array, int fromIndex, int toIndex) {
/* 214 */     Preconditions.checkNotNull(array);
/* 215 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
/* 216 */     Arrays.sort(array, fromIndex, toIndex);
/* 217 */     Bytes.reverse(array, fromIndex, toIndex);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\primitives\SignedBytes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */