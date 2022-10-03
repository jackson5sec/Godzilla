/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
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
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public final class UnsignedInts
/*     */ {
/*     */   static final long INT_MASK = 4294967295L;
/*     */   
/*     */   static int flip(int value) {
/*  56 */     return value ^ Integer.MIN_VALUE;
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
/*     */   public static int compare(int a, int b) {
/*  71 */     return Ints.compare(flip(a), flip(b));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long toLong(int value) {
/*  80 */     return value & 0xFFFFFFFFL;
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
/*     */   public static int checkedCast(long value) {
/*  94 */     Preconditions.checkArgument((value >> 32L == 0L), "out of range: %s", value);
/*  95 */     return (int)value;
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
/*     */   public static int saturatedCast(long value) {
/* 108 */     if (value <= 0L)
/* 109 */       return 0; 
/* 110 */     if (value >= 4294967296L) {
/* 111 */       return -1;
/*     */     }
/* 113 */     return (int)value;
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
/*     */   public static int min(int... array) {
/* 126 */     Preconditions.checkArgument((array.length > 0));
/* 127 */     int min = flip(array[0]);
/* 128 */     for (int i = 1; i < array.length; i++) {
/* 129 */       int next = flip(array[i]);
/* 130 */       if (next < min) {
/* 131 */         min = next;
/*     */       }
/*     */     } 
/* 134 */     return flip(min);
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
/*     */   public static int max(int... array) {
/* 146 */     Preconditions.checkArgument((array.length > 0));
/* 147 */     int max = flip(array[0]);
/* 148 */     for (int i = 1; i < array.length; i++) {
/* 149 */       int next = flip(array[i]);
/* 150 */       if (next > max) {
/* 151 */         max = next;
/*     */       }
/*     */     } 
/* 154 */     return flip(max);
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
/*     */   public static String join(String separator, int... array) {
/* 166 */     Preconditions.checkNotNull(separator);
/* 167 */     if (array.length == 0) {
/* 168 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 172 */     StringBuilder builder = new StringBuilder(array.length * 5);
/* 173 */     builder.append(toString(array[0]));
/* 174 */     for (int i = 1; i < array.length; i++) {
/* 175 */       builder.append(separator).append(toString(array[i]));
/*     */     }
/* 177 */     return builder.toString();
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
/*     */   public static Comparator<int[]> lexicographicalComparator() {
/* 191 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   enum LexicographicalComparator implements Comparator<int[]> {
/* 195 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(int[] left, int[] right) {
/* 199 */       int minLength = Math.min(left.length, right.length);
/* 200 */       for (int i = 0; i < minLength; i++) {
/* 201 */         if (left[i] != right[i]) {
/* 202 */           return UnsignedInts.compare(left[i], right[i]);
/*     */         }
/*     */       } 
/* 205 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 210 */       return "UnsignedInts.lexicographicalComparator()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sort(int[] array) {
/* 220 */     Preconditions.checkNotNull(array);
/* 221 */     sort(array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sort(int[] array, int fromIndex, int toIndex) {
/* 231 */     Preconditions.checkNotNull(array);
/* 232 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length); int i;
/* 233 */     for (i = fromIndex; i < toIndex; i++) {
/* 234 */       array[i] = flip(array[i]);
/*     */     }
/* 236 */     Arrays.sort(array, fromIndex, toIndex);
/* 237 */     for (i = fromIndex; i < toIndex; i++) {
/* 238 */       array[i] = flip(array[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(int[] array) {
/* 249 */     Preconditions.checkNotNull(array);
/* 250 */     sortDescending(array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(int[] array, int fromIndex, int toIndex) {
/* 260 */     Preconditions.checkNotNull(array);
/* 261 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length); int i;
/* 262 */     for (i = fromIndex; i < toIndex; i++) {
/* 263 */       array[i] = array[i] ^ Integer.MAX_VALUE;
/*     */     }
/* 265 */     Arrays.sort(array, fromIndex, toIndex);
/* 266 */     for (i = fromIndex; i < toIndex; i++) {
/* 267 */       array[i] = array[i] ^ Integer.MAX_VALUE;
/*     */     }
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
/*     */   public static int divide(int dividend, int divisor) {
/* 282 */     return (int)(toLong(dividend) / toLong(divisor));
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
/*     */   public static int remainder(int dividend, int divisor) {
/* 296 */     return (int)(toLong(dividend) % toLong(divisor));
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
/*     */   @CanIgnoreReturnValue
/*     */   public static int decode(String stringValue) {
/* 316 */     ParseRequest request = ParseRequest.fromString(stringValue);
/*     */     
/*     */     try {
/* 319 */       return parseUnsignedInt(request.rawValue, request.radix);
/* 320 */     } catch (NumberFormatException e) {
/* 321 */       NumberFormatException decodeException = new NumberFormatException("Error parsing value: " + stringValue);
/*     */       
/* 323 */       decodeException.initCause(e);
/* 324 */       throw decodeException;
/*     */     } 
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
/*     */   @CanIgnoreReturnValue
/*     */   public static int parseUnsignedInt(String s) {
/* 339 */     return parseUnsignedInt(s, 10);
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
/*     */   @CanIgnoreReturnValue
/*     */   public static int parseUnsignedInt(String string, int radix) {
/* 357 */     Preconditions.checkNotNull(string);
/* 358 */     long result = Long.parseLong(string, radix);
/* 359 */     if ((result & 0xFFFFFFFFL) != result) {
/* 360 */       throw new NumberFormatException("Input " + string + " in base " + radix + " is not in the range of an unsigned integer");
/*     */     }
/*     */     
/* 363 */     return (int)result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(int x) {
/* 372 */     return toString(x, 10);
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
/*     */   public static String toString(int x, int radix) {
/* 387 */     long asLong = x & 0xFFFFFFFFL;
/* 388 */     return Long.toString(asLong, radix);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\primitives\UnsignedInts.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */