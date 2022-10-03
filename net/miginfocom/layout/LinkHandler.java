/*     */ package net.miginfocom.layout;
/*     */ 
/*     */ import java.util.HashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class LinkHandler
/*     */ {
/*     */   public static final int X = 0;
/*     */   public static final int Y = 1;
/*     */   public static final int WIDTH = 2;
/*     */   public static final int HEIGHT = 3;
/*     */   public static final int X2 = 4;
/*     */   public static final int Y2 = 5;
/*     */   private static final int VALUES = 0;
/*     */   private static final int VALUES_TEMP = 1;
/*  54 */   private static final WeakHashMap<Object, HashMap<String, int[]>[]> LAYOUTS = (WeakHashMap)new WeakHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized Integer getValue(Object layout, String key, int type) {
/*  62 */     Integer ret = null;
/*     */     
/*  64 */     HashMap[] arrayOfHashMap = (HashMap[])LAYOUTS.get(layout);
/*  65 */     if (arrayOfHashMap != null) {
/*  66 */       int[] rect = arrayOfHashMap[1].get(key);
/*  67 */       if (rect != null && rect[type] != -2147471302) {
/*  68 */         ret = Integer.valueOf(rect[type]);
/*     */       } else {
/*  70 */         rect = arrayOfHashMap[0].get(key);
/*  71 */         ret = (rect != null && rect[type] != -2147471302) ? Integer.valueOf(rect[type]) : null;
/*     */       } 
/*     */     } 
/*  74 */     return ret;
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
/*     */   public static synchronized boolean setBounds(Object layout, String key, int x, int y, int width, int height) {
/*  88 */     return setBounds(layout, key, x, y, width, height, false, false);
/*     */   }
/*     */ 
/*     */   
/*     */   static synchronized boolean setBounds(Object layout, String key, int x, int y, int width, int height, boolean temporary, boolean incCur) {
/*  93 */     HashMap[] arrayOfHashMap = (HashMap[])LAYOUTS.get(layout);
/*  94 */     if (arrayOfHashMap != null) {
/*  95 */       HashMap<String, int[]> map = arrayOfHashMap[temporary ? 1 : 0];
/*  96 */       int[] old = map.get(key);
/*     */       
/*  98 */       if (old == null || old[0] != x || old[1] != y || old[2] != width || old[3] != height) {
/*  99 */         if (old == null || !incCur) {
/* 100 */           map.put(key, new int[] { x, y, width, height, x + width, y + height });
/* 101 */           return true;
/*     */         } 
/* 103 */         boolean changed = false;
/*     */         
/* 105 */         if (x != -2147471302) {
/* 106 */           if (old[0] == -2147471302 || x < old[0]) {
/* 107 */             old[0] = x;
/* 108 */             old[2] = old[4] - x;
/* 109 */             changed = true;
/*     */           } 
/*     */           
/* 112 */           if (width != -2147471302) {
/* 113 */             int x2 = x + width;
/* 114 */             if (old[4] == -2147471302 || x2 > old[4]) {
/* 115 */               old[4] = x2;
/* 116 */               old[2] = x2 - old[0];
/* 117 */               changed = true;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 122 */         if (y != -2147471302) {
/* 123 */           if (old[1] == -2147471302 || y < old[1]) {
/* 124 */             old[1] = y;
/* 125 */             old[3] = old[5] - y;
/* 126 */             changed = true;
/*     */           } 
/*     */           
/* 129 */           if (height != -2147471302) {
/* 130 */             int y2 = y + height;
/* 131 */             if (old[5] == -2147471302 || y2 > old[5]) {
/* 132 */               old[5] = y2;
/* 133 */               old[3] = y2 - old[1];
/* 134 */               changed = true;
/*     */             } 
/*     */           } 
/*     */         } 
/* 138 */         return changed;
/*     */       } 
/*     */       
/* 141 */       return false;
/*     */     } 
/*     */     
/* 144 */     int[] bounds = { x, y, width, height, x + width, y + height };
/*     */     
/* 146 */     HashMap<String, int[]> values_temp = (HashMap)new HashMap<>(4);
/* 147 */     if (temporary) {
/* 148 */       values_temp.put(key, bounds);
/*     */     }
/* 150 */     HashMap<String, int[]> values = (HashMap)new HashMap<>(4);
/* 151 */     if (!temporary) {
/* 152 */       values.put(key, bounds);
/*     */     }
/* 154 */     LAYOUTS.put(layout, new HashMap[] { values, values_temp });
/*     */     
/* 156 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized void clearWeakReferencesNow() {
/* 165 */     LAYOUTS.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public static synchronized boolean clearBounds(Object layout, String key) {
/* 170 */     HashMap[] arrayOfHashMap = (HashMap[])LAYOUTS.get(layout);
/* 171 */     if (arrayOfHashMap != null)
/* 172 */       return (arrayOfHashMap[0].remove(key) != null); 
/* 173 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   static synchronized void clearTemporaryBounds(Object layout) {
/* 178 */     HashMap[] arrayOfHashMap = (HashMap[])LAYOUTS.get(layout);
/* 179 */     if (arrayOfHashMap != null)
/* 180 */       arrayOfHashMap[1].clear(); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\net\miginfocom\layout\LinkHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */