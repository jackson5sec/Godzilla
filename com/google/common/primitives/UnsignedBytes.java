/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.reflect.Field;
/*     */ import java.nio.ByteOrder;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class UnsignedBytes
/*     */ {
/*     */   public static final byte MAX_POWER_OF_TWO = -128;
/*     */   public static final byte MAX_VALUE = -1;
/*     */   private static final int UNSIGNED_MASK = 255;
/*     */   
/*     */   public static int toInt(byte value) {
/*  74 */     return value & 0xFF;
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
/*     */   @CanIgnoreReturnValue
/*     */   public static byte checkedCast(long value) {
/*  87 */     Preconditions.checkArgument((value >> 8L == 0L), "out of range: %s", value);
/*  88 */     return (byte)(int)value;
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
/*     */   public static byte saturatedCast(long value) {
/* 100 */     if (value > toInt((byte)-1)) {
/* 101 */       return -1;
/*     */     }
/* 103 */     if (value < 0L) {
/* 104 */       return 0;
/*     */     }
/* 106 */     return (byte)(int)value;
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
/*     */   public static int compare(byte a, byte b) {
/* 120 */     return toInt(a) - toInt(b);
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
/* 132 */     Preconditions.checkArgument((array.length > 0));
/* 133 */     int min = toInt(array[0]);
/* 134 */     for (int i = 1; i < array.length; i++) {
/* 135 */       int next = toInt(array[i]);
/* 136 */       if (next < min) {
/* 137 */         min = next;
/*     */       }
/*     */     } 
/* 140 */     return (byte)min;
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
/* 152 */     Preconditions.checkArgument((array.length > 0));
/* 153 */     int max = toInt(array[0]);
/* 154 */     for (int i = 1; i < array.length; i++) {
/* 155 */       int next = toInt(array[i]);
/* 156 */       if (next > max) {
/* 157 */         max = next;
/*     */       }
/*     */     } 
/* 160 */     return (byte)max;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static String toString(byte x) {
/* 170 */     return toString(x, 10);
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
/*     */   @Beta
/*     */   public static String toString(byte x, int radix) {
/* 185 */     Preconditions.checkArgument((radix >= 2 && radix <= 36), "radix (%s) must be between Character.MIN_RADIX and Character.MAX_RADIX", radix);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     return Integer.toString(toInt(x), radix);
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
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   public static byte parseUnsignedByte(String string) {
/* 205 */     return parseUnsignedByte(string, 10);
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
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   public static byte parseUnsignedByte(String string, int radix) {
/* 223 */     int parse = Integer.parseInt((String)Preconditions.checkNotNull(string), radix);
/*     */     
/* 225 */     if (parse >> 8 == 0) {
/* 226 */       return (byte)parse;
/*     */     }
/* 228 */     throw new NumberFormatException("out of range: " + parse);
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
/*     */   public static String join(String separator, byte... array) {
/* 242 */     Preconditions.checkNotNull(separator);
/* 243 */     if (array.length == 0) {
/* 244 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 248 */     StringBuilder builder = new StringBuilder(array.length * (3 + separator.length()));
/* 249 */     builder.append(toInt(array[0]));
/* 250 */     for (int i = 1; i < array.length; i++) {
/* 251 */       builder.append(separator).append(toString(array[i]));
/*     */     }
/* 253 */     return builder.toString();
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
/* 271 */     return LexicographicalComparatorHolder.BEST_COMPARATOR;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static Comparator<byte[]> lexicographicalComparatorJavaImpl() {
/* 276 */     return LexicographicalComparatorHolder.PureJavaComparator.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static class LexicographicalComparatorHolder
/*     */   {
/* 288 */     static final String UNSAFE_COMPARATOR_NAME = LexicographicalComparatorHolder.class
/* 289 */       .getName() + "$UnsafeComparator";
/*     */     
/* 291 */     static final Comparator<byte[]> BEST_COMPARATOR = getBestComparator();
/*     */     
/*     */     @VisibleForTesting
/*     */     enum UnsafeComparator implements Comparator<byte[]> {
/* 295 */       INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       static final int BYTE_ARRAY_BASE_OFFSET;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 315 */       static final Unsafe theUnsafe = getUnsafe(); static final boolean BIG_ENDIAN = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);
/*     */       
/*     */       static {
/* 318 */         BYTE_ARRAY_BASE_OFFSET = theUnsafe.arrayBaseOffset(byte[].class);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 323 */         if (!"64".equals(System.getProperty("sun.arch.data.model")) || BYTE_ARRAY_BASE_OFFSET % 8 != 0 || theUnsafe
/*     */ 
/*     */           
/* 326 */           .arrayIndexScale(byte[].class) != 1) {
/* 327 */           throw new Error();
/*     */         }
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       private static Unsafe getUnsafe() {
/*     */         try {
/* 339 */           return Unsafe.getUnsafe();
/* 340 */         } catch (SecurityException securityException) {
/*     */ 
/*     */           
/*     */           try {
/* 344 */             return AccessController.<Unsafe>doPrivileged(new PrivilegedExceptionAction<Unsafe>()
/*     */                 {
/*     */                   public Unsafe run() throws Exception
/*     */                   {
/* 348 */                     Class<Unsafe> k = Unsafe.class;
/* 349 */                     for (Field f : k.getDeclaredFields()) {
/* 350 */                       f.setAccessible(true);
/* 351 */                       Object x = f.get((Object)null);
/* 352 */                       if (k.isInstance(x)) {
/* 353 */                         return k.cast(x);
/*     */                       }
/*     */                     } 
/* 356 */                     throw new NoSuchFieldError("the Unsafe");
/*     */                   }
/*     */                 });
/* 359 */           } catch (PrivilegedActionException e) {
/* 360 */             throw new RuntimeException("Could not initialize intrinsics", e.getCause());
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/*     */       public int compare(byte[] left, byte[] right) {
/* 366 */         int stride = 8;
/* 367 */         int minLength = Math.min(left.length, right.length);
/* 368 */         int strideLimit = minLength & 0xFFFFFFF8;
/*     */ 
/*     */ 
/*     */         
/*     */         int i;
/*     */ 
/*     */         
/* 375 */         for (i = 0; i < strideLimit; i += 8) {
/* 376 */           long lw = theUnsafe.getLong(left, BYTE_ARRAY_BASE_OFFSET + i);
/* 377 */           long rw = theUnsafe.getLong(right, BYTE_ARRAY_BASE_OFFSET + i);
/* 378 */           if (lw != rw) {
/* 379 */             if (BIG_ENDIAN) {
/* 380 */               return UnsignedLongs.compare(lw, rw);
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 390 */             int n = Long.numberOfTrailingZeros(lw ^ rw) & 0xFFFFFFF8;
/* 391 */             return (int)(lw >>> n & 0xFFL) - (int)(rw >>> n & 0xFFL);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 396 */         for (; i < minLength; i++) {
/* 397 */           int result = UnsignedBytes.compare(left[i], right[i]);
/* 398 */           if (result != 0) {
/* 399 */             return result;
/*     */           }
/*     */         } 
/* 402 */         return left.length - right.length;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 407 */         return "UnsignedBytes.lexicographicalComparator() (sun.misc.Unsafe version)";
/*     */       }
/*     */     }
/*     */     
/*     */     enum PureJavaComparator implements Comparator<byte[]> {
/* 412 */       INSTANCE;
/*     */ 
/*     */       
/*     */       public int compare(byte[] left, byte[] right) {
/* 416 */         int minLength = Math.min(left.length, right.length);
/* 417 */         for (int i = 0; i < minLength; i++) {
/* 418 */           int result = UnsignedBytes.compare(left[i], right[i]);
/* 419 */           if (result != 0) {
/* 420 */             return result;
/*     */           }
/*     */         } 
/* 423 */         return left.length - right.length;
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 428 */         return "UnsignedBytes.lexicographicalComparator() (pure Java version)";
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static Comparator<byte[]> getBestComparator() {
/*     */       try {
/* 438 */         Class<?> theClass = Class.forName(UNSAFE_COMPARATOR_NAME);
/*     */ 
/*     */ 
/*     */         
/* 442 */         Comparator<byte[]> comparator = (Comparator<byte[]>)theClass.getEnumConstants()[0];
/* 443 */         return comparator;
/* 444 */       } catch (Throwable t) {
/* 445 */         return UnsignedBytes.lexicographicalComparatorJavaImpl();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static byte flip(byte b) {
/* 451 */     return (byte)(b ^ 0x80);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sort(byte[] array) {
/* 460 */     Preconditions.checkNotNull(array);
/* 461 */     sort(array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sort(byte[] array, int fromIndex, int toIndex) {
/* 471 */     Preconditions.checkNotNull(array);
/* 472 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length); int i;
/* 473 */     for (i = fromIndex; i < toIndex; i++) {
/* 474 */       array[i] = flip(array[i]);
/*     */     }
/* 476 */     Arrays.sort(array, fromIndex, toIndex);
/* 477 */     for (i = fromIndex; i < toIndex; i++) {
/* 478 */       array[i] = flip(array[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void sortDescending(byte[] array) {
/* 489 */     Preconditions.checkNotNull(array);
/* 490 */     sortDescending(array, 0, array.length);
/*     */   }
/*     */   enum PureJavaComparator implements Comparator<byte[]> {
/*     */     INSTANCE; public int compare(byte[] left, byte[] right) { int minLength = Math.min(left.length, right.length);
/*     */       for (int i = 0; i < minLength; i++) {
/*     */         int result = UnsignedBytes.compare(left[i], right[i]);
/*     */         if (result != 0)
/*     */           return result; 
/*     */       } 
/*     */       return left.length - right.length; } public String toString() { return "UnsignedBytes.lexicographicalComparator() (pure Java version)"; } } public static void sortDescending(byte[] array, int fromIndex, int toIndex) {
/* 500 */     Preconditions.checkNotNull(array);
/* 501 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length); int i;
/* 502 */     for (i = fromIndex; i < toIndex; i++) {
/* 503 */       array[i] = (byte)(array[i] ^ Byte.MAX_VALUE);
/*     */     }
/* 505 */     Arrays.sort(array, fromIndex, toIndex);
/* 506 */     for (i = fromIndex; i < toIndex; i++)
/* 507 */       array[i] = (byte)(array[i] ^ Byte.MAX_VALUE); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\primitives\UnsignedBytes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */