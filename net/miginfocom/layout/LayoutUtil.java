/*     */ package net.miginfocom.layout;
/*     */ 
/*     */ import java.beans.Beans;
/*     */ import java.beans.ExceptionListener;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PersistenceDelegate;
/*     */ import java.beans.XMLDecoder;
/*     */ import java.beans.XMLEncoder;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.OutputStream;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.TreeSet;
/*     */ import java.util.WeakHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class LayoutUtil
/*     */ {
/*     */   public static final int INF = 2097051;
/*     */   static final int NOT_SET = -2147471302;
/*     */   public static final int MIN = 0;
/*     */   public static final int PREF = 1;
/*     */   public static final int MAX = 2;
/*     */   public static final int HORIZONTAL = 0;
/*     */   public static final int VERTICAL = 1;
/*  64 */   private static volatile WeakHashMap<Object, String> CR_MAP = null;
/*  65 */   private static volatile WeakHashMap<Object, Boolean> DT_MAP = null;
/*  66 */   private static int eSz = 0;
/*  67 */   private static int globalDebugMillis = 0;
/*  68 */   public static final boolean HAS_BEANS = hasBeans();
/*     */ 
/*     */   
/*     */   private static boolean hasBeans() {
/*     */     try {
/*  73 */       LayoutUtil.class.getClassLoader().loadClass("java.beans.Beans");
/*  74 */       return true;
/*  75 */     } catch (Throwable e) {
/*  76 */       return false;
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
/*     */   public static String getVersion() {
/*  89 */     return "5.0";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getGlobalDebugMillis() {
/*  99 */     return globalDebugMillis;
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
/*     */   public static void setGlobalDebugMillis(int millis) {
/* 113 */     globalDebugMillis = millis;
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
/*     */   public static void setDesignTime(ContainerWrapper cw, boolean b) {
/* 128 */     if (DT_MAP == null) {
/* 129 */       DT_MAP = new WeakHashMap<>();
/*     */     }
/* 131 */     DT_MAP.put((cw != null) ? cw.getComponent() : null, Boolean.valueOf(b));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isDesignTime(ContainerWrapper cw) {
/* 142 */     if (DT_MAP == null) {
/* 143 */       return (HAS_BEANS && Beans.isDesignTime());
/*     */     }
/*     */ 
/*     */     
/* 147 */     if (cw == null && DT_MAP != null && !DT_MAP.isEmpty()) {
/* 148 */       return true;
/*     */     }
/* 150 */     if (cw != null && !DT_MAP.containsKey(cw.getComponent())) {
/* 151 */       cw = null;
/*     */     }
/* 153 */     Boolean b = DT_MAP.get((cw != null) ? cw.getComponent() : null);
/* 154 */     return (b != null && b.booleanValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getDesignTimeEmptySize() {
/* 162 */     return eSz;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setDesignTimeEmptySize(int pixels) {
/* 172 */     eSz = pixels;
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
/*     */   static void putCCString(Object con, String s) {
/* 185 */     if (s != null && con != null && isDesignTime(null)) {
/* 186 */       if (CR_MAP == null) {
/* 187 */         CR_MAP = new WeakHashMap<>(64);
/*     */       }
/* 189 */       CR_MAP.put(con, s);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static synchronized void setDelegate(Class<?> c, PersistenceDelegate del) {
/*     */     try {
/* 200 */       Introspector.getBeanInfo(c, 3).getBeanDescriptor().setValue("persistenceDelegate", del);
/* 201 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String getCCString(Object con) {
/* 212 */     return (CR_MAP != null) ? CR_MAP.get(con) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   static void throwCC() {
/* 217 */     throw new IllegalStateException("setStoreConstraintData(true) must be set for strings to be saved.");
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
/*     */   static int[] calculateSerial(int[][] sizes, ResizeConstraint[] resConstr, Float[] defPushWeights, int startSizeType, int bounds) {
/* 234 */     float[] lengths = new float[sizes.length];
/* 235 */     float usedLength = 0.0F;
/*     */ 
/*     */     
/* 238 */     for (int i = 0; i < sizes.length; i++) {
/* 239 */       if (sizes[i] != null) {
/* 240 */         float len = (sizes[i][startSizeType] != -2147471302) ? sizes[i][startSizeType] : 0.0F;
/* 241 */         int newSizeBounded = getBrokenBoundary(len, sizes[i][0], sizes[i][2]);
/* 242 */         if (newSizeBounded != -2147471302) {
/* 243 */           len = newSizeBounded;
/*     */         }
/* 245 */         usedLength += len;
/* 246 */         lengths[i] = len;
/*     */       } 
/*     */     } 
/*     */     
/* 250 */     int useLengthI = Math.round(usedLength);
/* 251 */     if (useLengthI != bounds && resConstr != null) {
/* 252 */       boolean isGrow = (useLengthI < bounds);
/*     */ 
/*     */       
/* 255 */       TreeSet<Integer> prioList = new TreeSet<>();
/* 256 */       for (int j = 0; j < sizes.length; j++) {
/* 257 */         ResizeConstraint resC = (ResizeConstraint)getIndexSafe((Object[])resConstr, j);
/* 258 */         if (resC != null)
/* 259 */           prioList.add(Integer.valueOf(isGrow ? resC.growPrio : resC.shrinkPrio)); 
/*     */       } 
/* 261 */       Integer[] prioIntegers = (Integer[])prioList.toArray((Object[])new Integer[prioList.size()]);
/*     */       
/* 263 */       for (int force = 0; force <= ((isGrow && defPushWeights != null) ? 1 : 0); force++) {
/* 264 */         for (int pr = prioIntegers.length - 1; pr >= 0; pr--) {
/* 265 */           int curPrio = prioIntegers[pr].intValue();
/*     */           
/* 267 */           float totWeight = 0.0F;
/* 268 */           Float[] resizeWeight = new Float[sizes.length];
/* 269 */           for (int k = 0; k < sizes.length; k++) {
/* 270 */             if (sizes[k] != null) {
/*     */ 
/*     */               
/* 273 */               ResizeConstraint resC = (ResizeConstraint)getIndexSafe((Object[])resConstr, k);
/* 274 */               if (resC != null) {
/* 275 */                 int prio = isGrow ? resC.growPrio : resC.shrinkPrio;
/*     */                 
/* 277 */                 if (curPrio == prio) {
/* 278 */                   if (isGrow) {
/* 279 */                     resizeWeight[k] = (force == 0 || resC.grow != null) ? resC.grow : defPushWeights[(k < defPushWeights.length) ? k : (defPushWeights.length - 1)];
/*     */                   } else {
/* 281 */                     resizeWeight[k] = resC.shrink;
/*     */                   } 
/* 283 */                   if (resizeWeight[k] != null)
/* 284 */                     totWeight += resizeWeight[k].floatValue(); 
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/* 289 */           if (totWeight > 0.0F) {
/*     */             boolean hit;
/*     */             do {
/* 292 */               float toChange = bounds - usedLength;
/* 293 */               hit = false;
/* 294 */               float changedWeight = 0.0F;
/* 295 */               for (int m = 0; m < sizes.length && totWeight > 1.0E-4F; m++) {
/*     */                 
/* 297 */                 Float weight = resizeWeight[m];
/* 298 */                 if (weight != null) {
/* 299 */                   float sizeDelta = toChange * weight.floatValue() / totWeight;
/* 300 */                   float newSize = lengths[m] + sizeDelta;
/*     */                   
/* 302 */                   if (sizes[m] != null) {
/* 303 */                     int newSizeBounded = getBrokenBoundary(newSize, sizes[m][0], sizes[m][2]);
/* 304 */                     if (newSizeBounded != -2147471302) {
/* 305 */                       resizeWeight[m] = null;
/* 306 */                       hit = true;
/* 307 */                       changedWeight += weight.floatValue();
/* 308 */                       newSize = newSizeBounded;
/* 309 */                       sizeDelta = newSize - lengths[m];
/*     */                     } 
/*     */                   } 
/*     */                   
/* 313 */                   lengths[m] = newSize;
/* 314 */                   usedLength += sizeDelta;
/*     */                 } 
/*     */               } 
/* 317 */               totWeight -= changedWeight;
/* 318 */             } while (hit);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 323 */     return roundSizes(lengths);
/*     */   }
/*     */ 
/*     */   
/*     */   static Object getIndexSafe(Object[] arr, int ix) {
/* 328 */     return (arr != null) ? arr[(ix < arr.length) ? ix : (arr.length - 1)] : null;
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
/*     */   private static int getBrokenBoundary(float sz, int lower, int upper) {
/* 341 */     if (lower != -2147471302) {
/* 342 */       if (sz < lower)
/* 343 */         return lower; 
/* 344 */     } else if (sz < 0.0F) {
/* 345 */       return 0;
/*     */     } 
/*     */     
/* 348 */     if (upper != -2147471302 && sz > upper) {
/* 349 */       return upper;
/*     */     }
/* 351 */     return -2147471302;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static int sum(int[] terms, int start, int len) {
/* 357 */     int s = 0;
/* 358 */     for (int i = start, iSz = start + len; i < iSz; i++)
/* 359 */       s += terms[i]; 
/* 360 */     return s;
/*     */   }
/*     */ 
/*     */   
/*     */   static int sum(int[] terms) {
/* 365 */     return sum(terms, 0, terms.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static float clamp(float f, float min, float max) {
/* 376 */     return Math.max(min, Math.min(f, max));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int clamp(int i, int min, int max) {
/* 387 */     return Math.max(min, Math.min(i, max));
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getSizeSafe(int[] sizes, int sizeType) {
/* 392 */     if (sizes == null || sizes[sizeType] == -2147471302)
/* 393 */       return (sizeType == 2) ? 2097051 : 0; 
/* 394 */     return sizes[sizeType];
/*     */   }
/*     */ 
/*     */   
/*     */   static BoundSize derive(BoundSize bs, UnitValue min, UnitValue pref, UnitValue max) {
/* 399 */     if (bs == null || bs.isUnset()) {
/* 400 */       return new BoundSize(min, pref, max, null);
/*     */     }
/* 402 */     return new BoundSize((min != null) ? min : bs
/* 403 */         .getMin(), (pref != null) ? pref : bs
/* 404 */         .getPreferred(), (max != null) ? max : bs
/* 405 */         .getMax(), bs
/* 406 */         .getGapPush(), null);
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
/*     */   public static boolean isLeftToRight(LC lc, ContainerWrapper container) {
/* 418 */     if (lc != null && lc.getLeftToRight() != null) {
/* 419 */       return lc.getLeftToRight().booleanValue();
/*     */     }
/* 421 */     return (container == null || container.isLeftToRight());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int[] roundSizes(float[] sizes) {
/* 430 */     int[] retInts = new int[sizes.length];
/* 431 */     float posD = 0.0F;
/*     */     
/* 433 */     for (int i = 0; i < retInts.length; i++) {
/* 434 */       int posI = (int)(posD + 0.5F);
/*     */       
/* 436 */       posD += sizes[i];
/*     */       
/* 438 */       retInts[i] = (int)(posD + 0.5F) - posI;
/*     */     } 
/*     */     
/* 441 */     return retInts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean equals(Object o1, Object o2) {
/* 451 */     return (o1 == o2 || (o1 != null && o2 != null && o1.equals(o2)));
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
/*     */   static UnitValue getInsets(LC lc, int side, boolean getDefault) {
/* 486 */     UnitValue[] i = lc.getInsets();
/* 487 */     return (i != null && i[side] != null) ? i[side] : (getDefault ? PlatformDefaults.getPanelInsets(side) : UnitValue.ZERO);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void writeXMLObject(OutputStream os, Object o, ExceptionListener listener) {
/* 497 */     ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
/* 498 */     Thread.currentThread().setContextClassLoader(LayoutUtil.class.getClassLoader());
/*     */     
/* 500 */     XMLEncoder encoder = new XMLEncoder(os);
/*     */     
/* 502 */     if (listener != null) {
/* 503 */       encoder.setExceptionListener(listener);
/*     */     }
/* 505 */     encoder.writeObject(o);
/* 506 */     encoder.close();
/*     */     
/* 508 */     Thread.currentThread().setContextClassLoader(oldClassLoader);
/*     */   }
/*     */   
/* 511 */   private static ByteArrayOutputStream writeOutputStream = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized void writeAsXML(ObjectOutput out, Object o) throws IOException {
/* 518 */     if (writeOutputStream == null) {
/* 519 */       writeOutputStream = new ByteArrayOutputStream(16384);
/*     */     }
/* 521 */     writeOutputStream.reset();
/*     */     
/* 523 */     writeXMLObject(writeOutputStream, o, new ExceptionListener()
/*     */         {
/*     */           public void exceptionThrown(Exception e) {
/* 526 */             e.printStackTrace();
/*     */           }
/*     */         });
/* 529 */     byte[] buf = writeOutputStream.toByteArray();
/*     */     
/* 531 */     out.writeInt(buf.length);
/* 532 */     out.write(buf);
/*     */   }
/*     */   
/* 535 */   private static byte[] readBuf = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized Object readAsXML(ObjectInput in) throws IOException {
/* 543 */     if (readBuf == null) {
/* 544 */       readBuf = new byte[16384];
/*     */     }
/* 546 */     Thread cThread = Thread.currentThread();
/* 547 */     ClassLoader oldCL = null;
/*     */     
/*     */     try {
/* 550 */       oldCL = cThread.getContextClassLoader();
/* 551 */       cThread.setContextClassLoader(LayoutUtil.class.getClassLoader());
/* 552 */     } catch (SecurityException securityException) {}
/*     */ 
/*     */     
/* 555 */     Object o = null;
/*     */     try {
/* 557 */       int length = in.readInt();
/* 558 */       if (length > readBuf.length) {
/* 559 */         readBuf = new byte[length];
/*     */       }
/* 561 */       in.readFully(readBuf, 0, length);
/*     */       
/* 563 */       o = (new XMLDecoder(new ByteArrayInputStream(readBuf, 0, length))).readObject();
/*     */     }
/* 565 */     catch (EOFException eOFException) {}
/*     */ 
/*     */     
/* 568 */     if (oldCL != null) {
/* 569 */       cThread.setContextClassLoader(oldCL);
/*     */     }
/* 571 */     return o;
/*     */   }
/*     */   
/* 574 */   private static final IdentityHashMap<Object, Object> SER_MAP = new IdentityHashMap<>(2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setSerializedObject(Object caller, Object o) {
/* 582 */     synchronized (SER_MAP) {
/* 583 */       SER_MAP.put(caller, o);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object getSerializedObject(Object caller) {
/* 593 */     synchronized (SER_MAP) {
/* 594 */       return SER_MAP.remove(caller);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\net\miginfocom\layout\LayoutUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */