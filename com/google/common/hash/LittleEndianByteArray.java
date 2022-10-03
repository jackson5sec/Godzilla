/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.primitives.Longs;
/*     */ import java.lang.reflect.Field;
/*     */ import java.nio.ByteOrder;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
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
/*     */ final class LittleEndianByteArray
/*     */ {
/*     */   private static final LittleEndianBytes byteArray;
/*     */   
/*     */   static long load64(byte[] input, int offset) {
/*  42 */     assert input.length >= offset + 8;
/*     */     
/*  44 */     return byteArray.getLongLittleEndian(input, offset);
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
/*     */   static long load64Safely(byte[] input, int offset, int length) {
/*  58 */     long result = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  63 */     int limit = Math.min(length, 8);
/*  64 */     for (int i = 0; i < limit; i++)
/*     */     {
/*  66 */       result |= (input[offset + i] & 0xFFL) << i * 8;
/*     */     }
/*  68 */     return result;
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
/*     */   static void store64(byte[] sink, int offset, long value) {
/*  80 */     assert offset >= 0 && offset + 8 <= sink.length;
/*     */     
/*  82 */     byteArray.putLongLittleEndian(sink, offset, value);
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
/*     */   static int load32(byte[] source, int offset) {
/*  94 */     return source[offset] & 0xFF | (source[offset + 1] & 0xFF) << 8 | (source[offset + 2] & 0xFF) << 16 | (source[offset + 3] & 0xFF) << 24;
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
/*     */   static boolean usingUnsafe() {
/* 106 */     return byteArray instanceof UnsafeByteArray;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static interface LittleEndianBytes
/*     */   {
/*     */     long getLongLittleEndian(byte[] param1ArrayOfbyte, int param1Int);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void putLongLittleEndian(byte[] param1ArrayOfbyte, int param1Int, long param1Long);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private enum UnsafeByteArray
/*     */     implements LittleEndianBytes
/*     */   {
/* 128 */     UNSAFE_LITTLE_ENDIAN
/*     */     {
/*     */       public long getLongLittleEndian(byte[] array, int offset) {
/* 131 */         return UnsafeByteArray.theUnsafe.getLong(array, offset + UnsafeByteArray.BYTE_ARRAY_BASE_OFFSET);
/*     */       }
/*     */ 
/*     */       
/*     */       public void putLongLittleEndian(byte[] array, int offset, long value) {
/* 136 */         UnsafeByteArray.theUnsafe.putLong(array, offset + UnsafeByteArray.BYTE_ARRAY_BASE_OFFSET, value);
/*     */       }
/*     */     },
/* 139 */     UNSAFE_BIG_ENDIAN
/*     */     {
/*     */       public long getLongLittleEndian(byte[] array, int offset) {
/* 142 */         long bigEndian = UnsafeByteArray.theUnsafe.getLong(array, offset + UnsafeByteArray.BYTE_ARRAY_BASE_OFFSET);
/*     */         
/* 144 */         return Long.reverseBytes(bigEndian);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void putLongLittleEndian(byte[] array, int offset, long value) {
/* 150 */         long littleEndianValue = Long.reverseBytes(value);
/* 151 */         UnsafeByteArray.theUnsafe.putLong(array, offset + UnsafeByteArray.BYTE_ARRAY_BASE_OFFSET, littleEndianValue);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 195 */     private static final Unsafe theUnsafe = getUnsafe();
/* 196 */     private static final int BYTE_ARRAY_BASE_OFFSET = theUnsafe.arrayBaseOffset(byte[].class); private static Unsafe getUnsafe() { try { return Unsafe.getUnsafe(); } catch (SecurityException securityException) { try { return AccessController.<Unsafe>doPrivileged(new PrivilegedExceptionAction<Unsafe>() {
/*     */                 public Unsafe run() throws Exception { Class<Unsafe> k = Unsafe.class; for (Field f : k.getDeclaredFields()) { f.setAccessible(true); Object x = f.get((Object)null); if (k.isInstance(x))
/*     */                       return k.cast(x);  }  throw new NoSuchFieldError("the Unsafe"); }
/* 199 */               }); } catch (PrivilegedActionException e) { throw new RuntimeException("Could not initialize intrinsics", e.getCause()); }  }  } static { if (theUnsafe.arrayIndexScale(byte[].class) != 1)
/* 200 */         throw new AssertionError();  }
/*     */   
/*     */   }
/*     */   
/*     */   private enum JavaLittleEndianBytes
/*     */     implements LittleEndianBytes
/*     */   {
/* 207 */     INSTANCE
/*     */     {
/*     */       public long getLongLittleEndian(byte[] source, int offset) {
/* 210 */         return Longs.fromBytes(source[offset + 7], source[offset + 6], source[offset + 5], source[offset + 4], source[offset + 3], source[offset + 2], source[offset + 1], source[offset]);
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
/*     */       
/*     */       public void putLongLittleEndian(byte[] sink, int offset, long value) {
/* 223 */         long mask = 255L;
/* 224 */         for (int i = 0; i < 8; mask <<= 8L, i++) {
/* 225 */           sink[offset + i] = (byte)(int)((value & mask) >> i * 8);
/*     */         }
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   static {
/* 232 */     LittleEndianBytes theGetter = JavaLittleEndianBytes.INSTANCE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 245 */       String arch = System.getProperty("os.arch");
/* 246 */       if ("amd64".equals(arch))
/*     */       {
/* 248 */         theGetter = ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN) ? UnsafeByteArray.UNSAFE_LITTLE_ENDIAN : UnsafeByteArray.UNSAFE_BIG_ENDIAN;
/*     */       
/*     */       }
/*     */     }
/* 252 */     catch (Throwable throwable) {}
/*     */ 
/*     */     
/* 255 */     byteArray = theGetter;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\hash\LittleEndianByteArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */