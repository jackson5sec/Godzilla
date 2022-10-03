/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.math.BigInteger;
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
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public final class UnsignedLongs
/*     */ {
/*     */   public static final long MAX_VALUE = -1L;
/*     */   
/*     */   private static long flip(long a) {
/*  64 */     return a ^ Long.MIN_VALUE;
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
/*     */   public static int compare(long a, long b) {
/*  79 */     return Longs.compare(flip(a), flip(b));
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
/*     */   public static long min(long... array) {
/*  91 */     Preconditions.checkArgument((array.length > 0));
/*  92 */     long min = flip(array[0]);
/*  93 */     for (int i = 1; i < array.length; i++) {
/*  94 */       long next = flip(array[i]);
/*  95 */       if (next < min) {
/*  96 */         min = next;
/*     */       }
/*     */     } 
/*  99 */     return flip(min);
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
/*     */   public static long max(long... array) {
/* 111 */     Preconditions.checkArgument((array.length > 0));
/* 112 */     long max = flip(array[0]);
/* 113 */     for (int i = 1; i < array.length; i++) {
/* 114 */       long next = flip(array[i]);
/* 115 */       if (next > max) {
/* 116 */         max = next;
/*     */       }
/*     */     } 
/* 119 */     return flip(max);
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
/*     */   public static String join(String separator, long... array) {
/* 131 */     Preconditions.checkNotNull(separator);
/* 132 */     if (array.length == 0) {
/* 133 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 137 */     StringBuilder builder = new StringBuilder(array.length * 5);
/* 138 */     builder.append(toString(array[0]));
/* 139 */     for (int i = 1; i < array.length; i++) {
/* 140 */       builder.append(separator).append(toString(array[i]));
/*     */     }
/* 142 */     return builder.toString();
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
/*     */   public static Comparator<long[]> lexicographicalComparator() {
/* 157 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   enum LexicographicalComparator implements Comparator<long[]> {
/* 161 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(long[] left, long[] right) {
/* 165 */       int minLength = Math.min(left.length, right.length);
/* 166 */       for (int i = 0; i < minLength; i++) {
/* 167 */         if (left[i] != right[i]) {
/* 168 */           return UnsignedLongs.compare(left[i], right[i]);
/*     */         }
/*     */       } 
/* 171 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 176 */       return "UnsignedLongs.lexicographicalComparator()";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sort(long[] array) {
/* 186 */     Preconditions.checkNotNull(array);
/* 187 */     sort(array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sort(long[] array, int fromIndex, int toIndex) {
/* 197 */     Preconditions.checkNotNull(array);
/* 198 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length); int i;
/* 199 */     for (i = fromIndex; i < toIndex; i++) {
/* 200 */       array[i] = flip(array[i]);
/*     */     }
/* 202 */     Arrays.sort(array, fromIndex, toIndex);
/* 203 */     for (i = fromIndex; i < toIndex; i++) {
/* 204 */       array[i] = flip(array[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(long[] array) {
/* 215 */     Preconditions.checkNotNull(array);
/* 216 */     sortDescending(array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(long[] array, int fromIndex, int toIndex) {
/* 226 */     Preconditions.checkNotNull(array);
/* 227 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length); int i;
/* 228 */     for (i = fromIndex; i < toIndex; i++) {
/* 229 */       array[i] = array[i] ^ Long.MAX_VALUE;
/*     */     }
/* 231 */     Arrays.sort(array, fromIndex, toIndex);
/* 232 */     for (i = fromIndex; i < toIndex; i++) {
/* 233 */       array[i] = array[i] ^ Long.MAX_VALUE;
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
/*     */   public static long divide(long dividend, long divisor) {
/* 248 */     if (divisor < 0L) {
/* 249 */       if (compare(dividend, divisor) < 0) {
/* 250 */         return 0L;
/*     */       }
/* 252 */       return 1L;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 257 */     if (dividend >= 0L) {
/* 258 */       return dividend / divisor;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 267 */     long quotient = (dividend >>> 1L) / divisor << 1L;
/* 268 */     long rem = dividend - quotient * divisor;
/* 269 */     return quotient + ((compare(rem, divisor) >= 0) ? 1L : 0L);
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
/*     */   public static long remainder(long dividend, long divisor) {
/* 284 */     if (divisor < 0L) {
/* 285 */       if (compare(dividend, divisor) < 0) {
/* 286 */         return dividend;
/*     */       }
/* 288 */       return dividend - divisor;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 293 */     if (dividend >= 0L) {
/* 294 */       return dividend % divisor;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 303 */     long quotient = (dividend >>> 1L) / divisor << 1L;
/* 304 */     long rem = dividend - quotient * divisor;
/* 305 */     return rem - ((compare(rem, divisor) >= 0) ? divisor : 0L);
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
/*     */   @CanIgnoreReturnValue
/*     */   public static long parseUnsignedLong(String string) {
/* 320 */     return parseUnsignedLong(string, 10);
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
/*     */   public static long parseUnsignedLong(String string, int radix) {
/* 338 */     Preconditions.checkNotNull(string);
/* 339 */     if (string.length() == 0) {
/* 340 */       throw new NumberFormatException("empty string");
/*     */     }
/* 342 */     if (radix < 2 || radix > 36) {
/* 343 */       throw new NumberFormatException("illegal radix: " + radix);
/*     */     }
/*     */     
/* 346 */     int maxSafePos = ParseOverflowDetection.maxSafeDigits[radix] - 1;
/* 347 */     long value = 0L;
/* 348 */     for (int pos = 0; pos < string.length(); pos++) {
/* 349 */       int digit = Character.digit(string.charAt(pos), radix);
/* 350 */       if (digit == -1) {
/* 351 */         throw new NumberFormatException(string);
/*     */       }
/* 353 */       if (pos > maxSafePos && ParseOverflowDetection.overflowInParse(value, digit, radix)) {
/* 354 */         throw new NumberFormatException("Too large for unsigned long: " + string);
/*     */       }
/* 356 */       value = value * radix + digit;
/*     */     } 
/*     */     
/* 359 */     return value;
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
/*     */   @CanIgnoreReturnValue
/*     */   public static long decode(String stringValue) {
/* 380 */     ParseRequest request = ParseRequest.fromString(stringValue);
/*     */     
/*     */     try {
/* 383 */       return parseUnsignedLong(request.rawValue, request.radix);
/* 384 */     } catch (NumberFormatException e) {
/* 385 */       NumberFormatException decodeException = new NumberFormatException("Error parsing value: " + stringValue);
/*     */       
/* 387 */       decodeException.initCause(e);
/* 388 */       throw decodeException;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ParseOverflowDetection
/*     */   {
/* 400 */     static final long[] maxValueDivs = new long[37];
/* 401 */     static final int[] maxValueMods = new int[37];
/* 402 */     static final int[] maxSafeDigits = new int[37];
/*     */     
/*     */     static {
/* 405 */       BigInteger overflow = new BigInteger("10000000000000000", 16);
/* 406 */       for (int i = 2; i <= 36; i++) {
/* 407 */         maxValueDivs[i] = UnsignedLongs.divide(-1L, i);
/* 408 */         maxValueMods[i] = (int)UnsignedLongs.remainder(-1L, i);
/* 409 */         maxSafeDigits[i] = overflow.toString(i).length() - 1;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static boolean overflowInParse(long current, int digit, int radix) {
/* 420 */       if (current >= 0L) {
/* 421 */         if (current < maxValueDivs[radix]) {
/* 422 */           return false;
/*     */         }
/* 424 */         if (current > maxValueDivs[radix]) {
/* 425 */           return true;
/*     */         }
/*     */         
/* 428 */         return (digit > maxValueMods[radix]);
/*     */       } 
/*     */ 
/*     */       
/* 432 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(long x) {
/* 442 */     return toString(x, 10);
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
/*     */   public static String toString(long x, int radix) {
/* 457 */     Preconditions.checkArgument((radix >= 2 && radix <= 36), "radix (%s) must be between Character.MIN_RADIX and Character.MAX_RADIX", radix);
/*     */ 
/*     */ 
/*     */     
/* 461 */     if (x == 0L)
/*     */     {
/* 463 */       return "0"; } 
/* 464 */     if (x > 0L) {
/* 465 */       return Long.toString(x, radix);
/*     */     }
/* 467 */     char[] buf = new char[64];
/* 468 */     int i = buf.length;
/* 469 */     if ((radix & radix - 1) == 0) {
/*     */       
/* 471 */       int shift = Integer.numberOfTrailingZeros(radix);
/* 472 */       int mask = radix - 1;
/*     */       do {
/* 474 */         buf[--i] = Character.forDigit((int)x & mask, radix);
/* 475 */         x >>>= shift;
/* 476 */       } while (x != 0L);
/*     */     } else {
/*     */       long quotient;
/*     */ 
/*     */       
/* 481 */       if ((radix & 0x1) == 0) {
/*     */         
/* 483 */         quotient = (x >>> 1L) / (radix >>> 1);
/*     */       } else {
/* 485 */         quotient = divide(x, radix);
/*     */       } 
/* 487 */       long rem = x - quotient * radix;
/* 488 */       buf[--i] = Character.forDigit((int)rem, radix);
/* 489 */       x = quotient;
/*     */       
/* 491 */       while (x > 0L) {
/* 492 */         buf[--i] = Character.forDigit((int)(x % radix), radix);
/* 493 */         x /= radix;
/*     */       } 
/*     */     } 
/*     */     
/* 497 */     return new String(buf, i, buf.length - i);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\primitives\UnsignedLongs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */