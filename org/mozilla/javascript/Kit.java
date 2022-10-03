/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.lang.reflect.Method;
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
/*     */ public class Kit
/*     */ {
/*  25 */   private static Method Throwable_initCause = null;
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/*  30 */       Class<?> ThrowableClass = classOrNull("java.lang.Throwable");
/*  31 */       Class<?>[] signature = new Class[] { ThrowableClass };
/*  32 */       Throwable_initCause = ThrowableClass.getMethod("initCause", signature);
/*     */     }
/*  34 */     catch (Exception ex) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> classOrNull(String className) {
/*     */     
/*  42 */     try { return Class.forName(className); }
/*  43 */     catch (ClassNotFoundException ex) {  }
/*  44 */     catch (SecurityException ex) {  }
/*  45 */     catch (LinkageError ex) {  }
/*  46 */     catch (IllegalArgumentException e) {}
/*     */ 
/*     */ 
/*     */     
/*  50 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Class<?> classOrNull(ClassLoader loader, String className) {
/*     */     
/*  60 */     try { return loader.loadClass(className); }
/*  61 */     catch (ClassNotFoundException ex) {  }
/*  62 */     catch (SecurityException ex) {  }
/*  63 */     catch (LinkageError ex) {  }
/*  64 */     catch (IllegalArgumentException e) {}
/*     */ 
/*     */ 
/*     */     
/*  68 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   static Object newInstanceOrNull(Class<?> cl) {
/*     */     
/*  74 */     try { return cl.newInstance(); }
/*  75 */     catch (SecurityException x) {  }
/*  76 */     catch (LinkageError ex) {  }
/*  77 */     catch (InstantiationException x) {  }
/*  78 */     catch (IllegalAccessException x) {}
/*     */     
/*  80 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean testIfCanLoadRhinoClasses(ClassLoader loader) {
/*  88 */     Class<?> testClass = ScriptRuntime.ContextFactoryClass;
/*  89 */     Class<?> x = classOrNull(loader, testClass.getName());
/*  90 */     if (x != testClass)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/*  95 */       return false;
/*     */     }
/*  97 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static RuntimeException initCause(RuntimeException ex, Throwable cause) {
/* 108 */     if (Throwable_initCause != null) {
/* 109 */       Object[] args = { cause };
/*     */       try {
/* 111 */         Throwable_initCause.invoke(ex, args);
/* 112 */       } catch (Exception e) {}
/*     */     } 
/*     */ 
/*     */     
/* 116 */     return ex;
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
/*     */   public static int xDigitToInt(int c, int accumulator) {
/* 128 */     if (c <= 57)
/* 129 */     { c -= 48;
/* 130 */       if (0 <= c)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 144 */         return accumulator << 4 | c; }  } else { if (c <= 70) { if (65 <= c) { c -= 55; } else { return -1; }  } else if (c <= 102 && 97 <= c) { c -= 87; } else { return -1; }  return accumulator << 4 | c; }
/*     */     
/*     */     return -1;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object addListener(Object bag, Object listener) {
/* 197 */     if (listener == null) throw new IllegalArgumentException(); 
/* 198 */     if (listener instanceof Object[]) throw new IllegalArgumentException();
/*     */     
/* 200 */     if (bag == null) {
/* 201 */       bag = listener;
/* 202 */     } else if (!(bag instanceof Object[])) {
/* 203 */       bag = new Object[] { bag, listener };
/*     */     } else {
/* 205 */       Object[] array = (Object[])bag;
/* 206 */       int L = array.length;
/*     */       
/* 208 */       if (L < 2) throw new IllegalArgumentException(); 
/* 209 */       Object[] tmp = new Object[L + 1];
/* 210 */       System.arraycopy(array, 0, tmp, 0, L);
/* 211 */       tmp[L] = listener;
/* 212 */       bag = tmp;
/*     */     } 
/*     */     
/* 215 */     return bag;
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
/*     */   
/*     */   public static Object removeListener(Object bag, Object listener) {
/* 236 */     if (listener == null) throw new IllegalArgumentException(); 
/* 237 */     if (listener instanceof Object[]) throw new IllegalArgumentException();
/*     */     
/* 239 */     if (bag == listener) {
/* 240 */       bag = null;
/* 241 */     } else if (bag instanceof Object[]) {
/* 242 */       Object[] array = (Object[])bag;
/* 243 */       int L = array.length;
/*     */       
/* 245 */       if (L < 2) throw new IllegalArgumentException(); 
/* 246 */       if (L == 2) {
/* 247 */         if (array[1] == listener) {
/* 248 */           bag = array[0];
/* 249 */         } else if (array[0] == listener) {
/* 250 */           bag = array[1];
/*     */         } 
/*     */       } else {
/* 253 */         int i = L;
/*     */         do {
/* 255 */           i--;
/* 256 */           if (array[i] == listener) {
/* 257 */             Object[] tmp = new Object[L - 1];
/* 258 */             System.arraycopy(array, 0, tmp, 0, i);
/* 259 */             System.arraycopy(array, i + 1, tmp, i, L - i + 1);
/* 260 */             bag = tmp;
/*     */             break;
/*     */           } 
/* 263 */         } while (i != 0);
/*     */       } 
/*     */     } 
/*     */     
/* 267 */     return bag;
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
/*     */   public static Object getListener(Object bag, int index) {
/* 284 */     if (index == 0) {
/* 285 */       if (bag == null)
/* 286 */         return null; 
/* 287 */       if (!(bag instanceof Object[]))
/* 288 */         return bag; 
/* 289 */       Object[] arrayOfObject = (Object[])bag;
/*     */       
/* 291 */       if (arrayOfObject.length < 2) throw new IllegalArgumentException(); 
/* 292 */       return arrayOfObject[0];
/* 293 */     }  if (index == 1) {
/* 294 */       if (!(bag instanceof Object[])) {
/* 295 */         if (bag == null) throw new IllegalArgumentException(); 
/* 296 */         return null;
/*     */       } 
/* 298 */       Object[] arrayOfObject = (Object[])bag;
/*     */       
/* 300 */       return arrayOfObject[1];
/*     */     } 
/*     */     
/* 303 */     Object[] array = (Object[])bag;
/* 304 */     int L = array.length;
/* 305 */     if (L < 2) throw new IllegalArgumentException(); 
/* 306 */     if (index == L)
/* 307 */       return null; 
/* 308 */     return array[index];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static Object initHash(Map<Object, Object> h, Object key, Object initialValue) {
/* 314 */     synchronized (h) {
/* 315 */       Object current = h.get(key);
/* 316 */       if (current == null) {
/* 317 */         h.put(key, initialValue);
/*     */       } else {
/* 319 */         initialValue = current;
/*     */       } 
/*     */     } 
/* 322 */     return initialValue;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class ComplexKey
/*     */   {
/*     */     private Object key1;
/*     */     private Object key2;
/*     */     private int hash;
/*     */     
/*     */     ComplexKey(Object key1, Object key2) {
/* 333 */       this.key1 = key1;
/* 334 */       this.key2 = key2;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object anotherObj) {
/* 340 */       if (!(anotherObj instanceof ComplexKey))
/* 341 */         return false; 
/* 342 */       ComplexKey another = (ComplexKey)anotherObj;
/* 343 */       return (this.key1.equals(another.key1) && this.key2.equals(another.key2));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 349 */       if (this.hash == 0) {
/* 350 */         this.hash = this.key1.hashCode() ^ this.key2.hashCode();
/*     */       }
/* 352 */       return this.hash;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static Object makeHashKeyFromPair(Object key1, Object key2) {
/* 358 */     if (key1 == null) throw new IllegalArgumentException(); 
/* 359 */     if (key2 == null) throw new IllegalArgumentException(); 
/* 360 */     return new ComplexKey(key1, key2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String readReader(Reader r) throws IOException {
/* 366 */     char[] buffer = new char[512];
/* 367 */     int cursor = 0;
/*     */     while (true) {
/* 369 */       int n = r.read(buffer, cursor, buffer.length - cursor);
/* 370 */       if (n < 0)
/* 371 */         break;  cursor += n;
/* 372 */       if (cursor == buffer.length) {
/* 373 */         char[] tmp = new char[buffer.length * 2];
/* 374 */         System.arraycopy(buffer, 0, tmp, 0, cursor);
/* 375 */         buffer = tmp;
/*     */       } 
/*     */     } 
/* 378 */     return new String(buffer, 0, cursor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] readStream(InputStream is, int initialBufferCapacity) throws IOException {
/* 384 */     if (initialBufferCapacity <= 0) {
/* 385 */       throw new IllegalArgumentException("Bad initialBufferCapacity: " + initialBufferCapacity);
/*     */     }
/*     */     
/* 388 */     byte[] buffer = new byte[initialBufferCapacity];
/* 389 */     int cursor = 0;
/*     */     while (true) {
/* 391 */       int n = is.read(buffer, cursor, buffer.length - cursor);
/* 392 */       if (n < 0)
/* 393 */         break;  cursor += n;
/* 394 */       if (cursor == buffer.length) {
/* 395 */         byte[] tmp = new byte[buffer.length * 2];
/* 396 */         System.arraycopy(buffer, 0, tmp, 0, cursor);
/* 397 */         buffer = tmp;
/*     */       } 
/*     */     } 
/* 400 */     if (cursor != buffer.length) {
/* 401 */       byte[] tmp = new byte[cursor];
/* 402 */       System.arraycopy(buffer, 0, tmp, 0, cursor);
/* 403 */       buffer = tmp;
/*     */     } 
/* 405 */     return buffer;
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
/*     */   public static RuntimeException codeBug() throws RuntimeException {
/* 417 */     RuntimeException ex = new IllegalStateException("FAILED ASSERTION");
/*     */     
/* 419 */     ex.printStackTrace(System.err);
/* 420 */     throw ex;
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
/*     */   public static RuntimeException codeBug(String msg) throws RuntimeException {
/* 432 */     msg = "FAILED ASSERTION: " + msg;
/* 433 */     RuntimeException ex = new IllegalStateException(msg);
/*     */     
/* 435 */     ex.printStackTrace(System.err);
/* 436 */     throw ex;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\Kit.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */