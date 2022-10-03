/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ObjToIntMap
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = -1542220580748809402L;
/*     */   private static final int A = -1640531527;
/*     */   
/*     */   public static class Iterator
/*     */   {
/*     */     ObjToIntMap master;
/*     */     private int cursor;
/*     */     private int remaining;
/*     */     private Object[] keys;
/*     */     private int[] values;
/*     */     
/*     */     Iterator(ObjToIntMap master) {
/*  36 */       this.master = master;
/*     */     }
/*     */     
/*     */     final void init(Object[] keys, int[] values, int keyCount) {
/*  40 */       this.keys = keys;
/*  41 */       this.values = values;
/*  42 */       this.cursor = -1;
/*  43 */       this.remaining = keyCount;
/*     */     }
/*     */     
/*     */     public void start() {
/*  47 */       this.master.initIterator(this);
/*  48 */       next();
/*     */     }
/*     */     
/*     */     public boolean done() {
/*  52 */       return (this.remaining < 0);
/*     */     }
/*     */     
/*     */     public void next() {
/*  56 */       if (this.remaining == -1) Kit.codeBug(); 
/*  57 */       if (this.remaining == 0) {
/*  58 */         this.remaining = -1;
/*  59 */         this.cursor = -1;
/*     */       } else {
/*  61 */         this.cursor++; for (;; this.cursor++) {
/*  62 */           Object key = this.keys[this.cursor];
/*  63 */           if (key != null && key != ObjToIntMap.DELETED) {
/*  64 */             this.remaining--;
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public Object getKey() {
/*  72 */       Object key = this.keys[this.cursor];
/*  73 */       if (key == UniqueTag.NULL_VALUE) key = null; 
/*  74 */       return key;
/*     */     }
/*     */     
/*     */     public int getValue() {
/*  78 */       return this.values[this.cursor];
/*     */     }
/*     */     
/*     */     public void setValue(int value) {
/*  82 */       this.values[this.cursor] = value;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjToIntMap() {
/*  93 */     this(4);
/*     */   }
/*     */   
/*     */   public ObjToIntMap(int keyCountHint) {
/*  97 */     if (keyCountHint < 0) Kit.codeBug();
/*     */     
/*  99 */     int minimalCapacity = keyCountHint * 4 / 3;
/*     */     int i;
/* 101 */     for (i = 2; 1 << i < minimalCapacity; i++);
/* 102 */     this.power = i;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 107 */     return (this.keyCount == 0);
/*     */   }
/*     */   
/*     */   public int size() {
/* 111 */     return this.keyCount;
/*     */   }
/*     */   
/*     */   public boolean has(Object key) {
/* 115 */     if (key == null) key = UniqueTag.NULL_VALUE; 
/* 116 */     return (0 <= findIndex(key));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int get(Object key, int defaultValue) {
/* 124 */     if (key == null) key = UniqueTag.NULL_VALUE; 
/* 125 */     int index = findIndex(key);
/* 126 */     if (0 <= index) {
/* 127 */       return this.values[index];
/*     */     }
/* 129 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getExisting(Object key) {
/* 138 */     if (key == null) key = UniqueTag.NULL_VALUE; 
/* 139 */     int index = findIndex(key);
/* 140 */     if (0 <= index) {
/* 141 */       return this.values[index];
/*     */     }
/*     */     
/* 144 */     Kit.codeBug();
/* 145 */     return 0;
/*     */   }
/*     */   
/*     */   public void put(Object key, int value) {
/* 149 */     if (key == null) key = UniqueTag.NULL_VALUE; 
/* 150 */     int index = ensureIndex(key);
/* 151 */     this.values[index] = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object intern(Object keyArg) {
/* 160 */     boolean nullKey = false;
/* 161 */     if (keyArg == null) {
/* 162 */       nullKey = true;
/* 163 */       keyArg = UniqueTag.NULL_VALUE;
/*     */     } 
/* 165 */     int index = ensureIndex(keyArg);
/* 166 */     this.values[index] = 0;
/* 167 */     return nullKey ? null : this.keys[index];
/*     */   }
/*     */   
/*     */   public void remove(Object key) {
/* 171 */     if (key == null) key = UniqueTag.NULL_VALUE; 
/* 172 */     int index = findIndex(key);
/* 173 */     if (0 <= index) {
/* 174 */       this.keys[index] = DELETED;
/* 175 */       this.keyCount--;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void clear() {
/* 180 */     int i = this.keys.length;
/* 181 */     while (i != 0) {
/* 182 */       this.keys[--i] = null;
/*     */     }
/* 184 */     this.keyCount = 0;
/* 185 */     this.occupiedCount = 0;
/*     */   }
/*     */   
/*     */   public Iterator newIterator() {
/* 189 */     return new Iterator(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final void initIterator(Iterator i) {
/* 196 */     i.init(this.keys, this.values, this.keyCount);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] getKeys() {
/* 201 */     Object[] array = new Object[this.keyCount];
/* 202 */     getKeys(array, 0);
/* 203 */     return array;
/*     */   }
/*     */   
/*     */   public void getKeys(Object[] array, int offset) {
/* 207 */     int count = this.keyCount;
/* 208 */     for (int i = 0; count != 0; i++) {
/* 209 */       Object key = this.keys[i];
/* 210 */       if (key != null && key != DELETED) {
/* 211 */         if (key == UniqueTag.NULL_VALUE) key = null; 
/* 212 */         array[offset] = key;
/* 213 */         offset++;
/* 214 */         count--;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int tableLookupStep(int fraction, int mask, int power) {
/* 220 */     int shift = 32 - 2 * power;
/* 221 */     if (shift >= 0) {
/* 222 */       return fraction >>> shift & mask | 0x1;
/*     */     }
/*     */     
/* 225 */     return fraction & mask >>> -shift | 0x1;
/*     */   }
/*     */ 
/*     */   
/*     */   private int findIndex(Object key) {
/* 230 */     if (this.keys != null) {
/* 231 */       int hash = key.hashCode();
/* 232 */       int fraction = hash * -1640531527;
/* 233 */       int index = fraction >>> 32 - this.power;
/* 234 */       Object test = this.keys[index];
/* 235 */       if (test != null) {
/* 236 */         int N = 1 << this.power;
/* 237 */         if (test == key || (this.values[N + index] == hash && test.equals(key)))
/*     */         {
/*     */           
/* 240 */           return index;
/*     */         }
/*     */         
/* 243 */         int mask = N - 1;
/* 244 */         int step = tableLookupStep(fraction, mask, this.power);
/* 245 */         int n = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         while (true) {
/* 251 */           index = index + step & mask;
/* 252 */           test = this.keys[index];
/* 253 */           if (test == null) {
/*     */             break;
/*     */           }
/* 256 */           if (test == key || (this.values[N + index] == hash && test.equals(key)))
/*     */           {
/*     */             
/* 259 */             return index;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 264 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int insertNewKey(Object key, int hash) {
/* 272 */     int fraction = hash * -1640531527;
/* 273 */     int index = fraction >>> 32 - this.power;
/* 274 */     int N = 1 << this.power;
/* 275 */     if (this.keys[index] != null) {
/* 276 */       int mask = N - 1;
/* 277 */       int step = tableLookupStep(fraction, mask, this.power);
/* 278 */       int firstIndex = index;
/*     */       
/*     */       do {
/* 281 */         index = index + step & mask;
/*     */       }
/* 283 */       while (this.keys[index] != null);
/*     */     } 
/* 285 */     this.keys[index] = key;
/* 286 */     this.values[N + index] = hash;
/* 287 */     this.occupiedCount++;
/* 288 */     this.keyCount++;
/*     */     
/* 290 */     return index;
/*     */   }
/*     */   
/*     */   private void rehashTable() {
/* 294 */     if (this.keys == null) {
/*     */ 
/*     */       
/* 297 */       int N = 1 << this.power;
/* 298 */       this.keys = new Object[N];
/* 299 */       this.values = new int[2 * N];
/*     */     }
/*     */     else {
/*     */       
/* 303 */       if (this.keyCount * 2 >= this.occupiedCount)
/*     */       {
/* 305 */         this.power++;
/*     */       }
/* 307 */       int N = 1 << this.power;
/* 308 */       Object[] oldKeys = this.keys;
/* 309 */       int[] oldValues = this.values;
/* 310 */       int oldN = oldKeys.length;
/* 311 */       this.keys = new Object[N];
/* 312 */       this.values = new int[2 * N];
/*     */       
/* 314 */       int remaining = this.keyCount;
/* 315 */       this.occupiedCount = this.keyCount = 0;
/* 316 */       for (int i = 0; remaining != 0; i++) {
/* 317 */         Object key = oldKeys[i];
/* 318 */         if (key != null && key != DELETED) {
/* 319 */           int keyHash = oldValues[oldN + i];
/* 320 */           int index = insertNewKey(key, keyHash);
/* 321 */           this.values[index] = oldValues[i];
/* 322 */           remaining--;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int ensureIndex(Object key) {
/* 330 */     int hash = key.hashCode();
/* 331 */     int index = -1;
/* 332 */     int firstDeleted = -1;
/* 333 */     if (this.keys != null) {
/* 334 */       int fraction = hash * -1640531527;
/* 335 */       index = fraction >>> 32 - this.power;
/* 336 */       Object test = this.keys[index];
/* 337 */       if (test != null) {
/* 338 */         int N = 1 << this.power;
/* 339 */         if (test == key || (this.values[N + index] == hash && test.equals(key)))
/*     */         {
/*     */           
/* 342 */           return index;
/*     */         }
/* 344 */         if (test == DELETED) {
/* 345 */           firstDeleted = index;
/*     */         }
/*     */ 
/*     */         
/* 349 */         int mask = N - 1;
/* 350 */         int step = tableLookupStep(fraction, mask, this.power);
/* 351 */         int n = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         while (true) {
/* 357 */           index = index + step & mask;
/* 358 */           test = this.keys[index];
/* 359 */           if (test == null) {
/*     */             break;
/*     */           }
/* 362 */           if (test == key || (this.values[N + index] == hash && test.equals(key)))
/*     */           {
/*     */             
/* 365 */             return index;
/*     */           }
/* 367 */           if (test == DELETED && firstDeleted < 0) {
/* 368 */             firstDeleted = index;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 376 */     if (firstDeleted >= 0) {
/* 377 */       index = firstDeleted;
/*     */     }
/*     */     else {
/*     */       
/* 381 */       if (this.keys == null || this.occupiedCount * 4 >= (1 << this.power) * 3) {
/*     */         
/* 383 */         rehashTable();
/* 384 */         return insertNewKey(key, hash);
/*     */       } 
/* 386 */       this.occupiedCount++;
/*     */     } 
/* 388 */     this.keys[index] = key;
/* 389 */     this.values[(1 << this.power) + index] = hash;
/* 390 */     this.keyCount++;
/* 391 */     return index;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 397 */     out.defaultWriteObject();
/*     */     
/* 399 */     int count = this.keyCount;
/* 400 */     for (int i = 0; count != 0; i++) {
/* 401 */       Object key = this.keys[i];
/* 402 */       if (key != null && key != DELETED) {
/* 403 */         count--;
/* 404 */         out.writeObject(key);
/* 405 */         out.writeInt(this.values[i]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 413 */     in.defaultReadObject();
/*     */     
/* 415 */     int writtenKeyCount = this.keyCount;
/* 416 */     if (writtenKeyCount != 0) {
/* 417 */       this.keyCount = 0;
/* 418 */       int N = 1 << this.power;
/* 419 */       this.keys = new Object[N];
/* 420 */       this.values = new int[2 * N];
/* 421 */       for (int i = 0; i != writtenKeyCount; i++) {
/* 422 */         Object key = in.readObject();
/* 423 */         int hash = key.hashCode();
/* 424 */         int index = insertNewKey(key, hash);
/* 425 */         this.values[index] = in.readInt();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 434 */   private static final Object DELETED = new Object();
/*     */   private transient Object[] keys;
/*     */   private transient int[] values;
/*     */   private int power;
/*     */   private int keyCount;
/*     */   private transient int occupiedCount;
/*     */   private static final boolean check = false;
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ObjToIntMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */