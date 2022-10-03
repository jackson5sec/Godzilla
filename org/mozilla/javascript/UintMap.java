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
/*     */ public class UintMap
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = 4242698212885848444L;
/*     */   private static final int A = -1640531527;
/*     */   private static final int EMPTY = -1;
/*     */   private static final int DELETED = -2;
/*     */   private transient int[] keys;
/*     */   private transient Object[] values;
/*     */   private int power;
/*     */   private int keyCount;
/*     */   private transient int occupiedCount;
/*     */   private transient int ivaluesShift;
/*     */   private static final boolean check = false;
/*     */   
/*     */   public UintMap() {
/*  32 */     this(4);
/*     */   }
/*     */   
/*     */   public UintMap(int initialCapacity) {
/*  36 */     if (initialCapacity < 0) Kit.codeBug();
/*     */     
/*  38 */     int minimalCapacity = initialCapacity * 4 / 3;
/*     */     int i;
/*  40 */     for (i = 2; 1 << i < minimalCapacity; i++);
/*  41 */     this.power = i;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  46 */     return (this.keyCount == 0);
/*     */   }
/*     */   
/*     */   public int size() {
/*  50 */     return this.keyCount;
/*     */   }
/*     */   
/*     */   public boolean has(int key) {
/*  54 */     if (key < 0) Kit.codeBug(); 
/*  55 */     return (0 <= findIndex(key));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getObject(int key) {
/*  63 */     if (key < 0) Kit.codeBug(); 
/*  64 */     if (this.values != null) {
/*  65 */       int index = findIndex(key);
/*  66 */       if (0 <= index) {
/*  67 */         return this.values[index];
/*     */       }
/*     */     } 
/*  70 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getInt(int key, int defaultValue) {
/*  78 */     if (key < 0) Kit.codeBug(); 
/*  79 */     int index = findIndex(key);
/*  80 */     if (0 <= index) {
/*  81 */       if (this.ivaluesShift != 0) {
/*  82 */         return this.keys[this.ivaluesShift + index];
/*     */       }
/*  84 */       return 0;
/*     */     } 
/*  86 */     return defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getExistingInt(int key) {
/*  96 */     if (key < 0) Kit.codeBug(); 
/*  97 */     int index = findIndex(key);
/*  98 */     if (0 <= index) {
/*  99 */       if (this.ivaluesShift != 0) {
/* 100 */         return this.keys[this.ivaluesShift + index];
/*     */       }
/* 102 */       return 0;
/*     */     } 
/*     */     
/* 105 */     Kit.codeBug();
/* 106 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(int key, Object value) {
/* 114 */     if (key < 0) Kit.codeBug(); 
/* 115 */     int index = ensureIndex(key, false);
/* 116 */     if (this.values == null) {
/* 117 */       this.values = new Object[1 << this.power];
/*     */     }
/* 119 */     this.values[index] = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(int key, int value) {
/* 127 */     if (key < 0) Kit.codeBug(); 
/* 128 */     int index = ensureIndex(key, true);
/* 129 */     if (this.ivaluesShift == 0) {
/* 130 */       int N = 1 << this.power;
/*     */       
/* 132 */       if (this.keys.length != N * 2) {
/* 133 */         int[] tmp = new int[N * 2];
/* 134 */         System.arraycopy(this.keys, 0, tmp, 0, N);
/* 135 */         this.keys = tmp;
/*     */       } 
/* 137 */       this.ivaluesShift = N;
/*     */     } 
/* 139 */     this.keys[this.ivaluesShift + index] = value;
/*     */   }
/*     */   
/*     */   public void remove(int key) {
/* 143 */     if (key < 0) Kit.codeBug(); 
/* 144 */     int index = findIndex(key);
/* 145 */     if (0 <= index) {
/* 146 */       this.keys[index] = -2;
/* 147 */       this.keyCount--;
/*     */ 
/*     */       
/* 150 */       if (this.values != null) this.values[index] = null; 
/* 151 */       if (this.ivaluesShift != 0) this.keys[this.ivaluesShift + index] = 0; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void clear() {
/* 156 */     int N = 1 << this.power;
/* 157 */     if (this.keys != null) {
/* 158 */       int i; for (i = 0; i != N; i++) {
/* 159 */         this.keys[i] = -1;
/*     */       }
/* 161 */       if (this.values != null) {
/* 162 */         for (i = 0; i != N; i++) {
/* 163 */           this.values[i] = null;
/*     */         }
/*     */       }
/*     */     } 
/* 167 */     this.ivaluesShift = 0;
/* 168 */     this.keyCount = 0;
/* 169 */     this.occupiedCount = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int[] getKeys() {
/* 174 */     int[] keys = this.keys;
/* 175 */     int n = this.keyCount;
/* 176 */     int[] result = new int[n];
/* 177 */     for (int i = 0; n != 0; i++) {
/* 178 */       int entry = keys[i];
/* 179 */       if (entry != -1 && entry != -2) {
/* 180 */         result[--n] = entry;
/*     */       }
/*     */     } 
/* 183 */     return result;
/*     */   }
/*     */   
/*     */   private static int tableLookupStep(int fraction, int mask, int power) {
/* 187 */     int shift = 32 - 2 * power;
/* 188 */     if (shift >= 0) {
/* 189 */       return fraction >>> shift & mask | 0x1;
/*     */     }
/*     */     
/* 192 */     return fraction & mask >>> -shift | 0x1;
/*     */   }
/*     */ 
/*     */   
/*     */   private int findIndex(int key) {
/* 197 */     int[] keys = this.keys;
/* 198 */     if (keys != null) {
/* 199 */       int fraction = key * -1640531527;
/* 200 */       int index = fraction >>> 32 - this.power;
/* 201 */       int entry = keys[index];
/* 202 */       if (entry == key) return index; 
/* 203 */       if (entry != -1) {
/*     */         
/* 205 */         int mask = (1 << this.power) - 1;
/* 206 */         int step = tableLookupStep(fraction, mask, this.power);
/* 207 */         int n = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         do {
/* 213 */           index = index + step & mask;
/* 214 */           entry = keys[index];
/* 215 */           if (entry == key) return index; 
/* 216 */         } while (entry != -1);
/*     */       } 
/*     */     } 
/* 219 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int insertNewKey(int key) {
/* 227 */     int[] keys = this.keys;
/* 228 */     int fraction = key * -1640531527;
/* 229 */     int index = fraction >>> 32 - this.power;
/* 230 */     if (keys[index] != -1) {
/* 231 */       int mask = (1 << this.power) - 1;
/* 232 */       int step = tableLookupStep(fraction, mask, this.power);
/* 233 */       int firstIndex = index;
/*     */       
/*     */       do {
/* 236 */         index = index + step & mask;
/*     */       }
/* 238 */       while (keys[index] != -1);
/*     */     } 
/* 240 */     keys[index] = key;
/* 241 */     this.occupiedCount++;
/* 242 */     this.keyCount++;
/* 243 */     return index;
/*     */   }
/*     */   
/*     */   private void rehashTable(boolean ensureIntSpace) {
/* 247 */     if (this.keys != null)
/*     */     {
/* 249 */       if (this.keyCount * 2 >= this.occupiedCount)
/*     */       {
/* 251 */         this.power++;
/*     */       }
/*     */     }
/* 254 */     int N = 1 << this.power;
/* 255 */     int[] old = this.keys;
/* 256 */     int oldShift = this.ivaluesShift;
/* 257 */     if (oldShift == 0 && !ensureIntSpace) {
/* 258 */       this.keys = new int[N];
/*     */     } else {
/*     */       
/* 261 */       this.ivaluesShift = N; this.keys = new int[N * 2];
/*     */     } 
/* 263 */     for (int i = 0; i != N; ) { this.keys[i] = -1; i++; }
/*     */     
/* 265 */     Object[] oldValues = this.values;
/* 266 */     if (oldValues != null) this.values = new Object[N];
/*     */     
/* 268 */     int oldCount = this.keyCount;
/* 269 */     this.occupiedCount = 0;
/* 270 */     if (oldCount != 0) {
/* 271 */       this.keyCount = 0;
/* 272 */       for (int j = 0, remaining = oldCount; remaining != 0; j++) {
/* 273 */         int key = old[j];
/* 274 */         if (key != -1 && key != -2) {
/* 275 */           int index = insertNewKey(key);
/* 276 */           if (oldValues != null) {
/* 277 */             this.values[index] = oldValues[j];
/*     */           }
/* 279 */           if (oldShift != 0) {
/* 280 */             this.keys[this.ivaluesShift + index] = old[oldShift + j];
/*     */           }
/* 282 */           remaining--;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int ensureIndex(int key, boolean intType) {
/* 290 */     int index = -1;
/* 291 */     int firstDeleted = -1;
/* 292 */     int[] keys = this.keys;
/* 293 */     if (keys != null) {
/* 294 */       int fraction = key * -1640531527;
/* 295 */       index = fraction >>> 32 - this.power;
/* 296 */       int entry = keys[index];
/* 297 */       if (entry == key) return index; 
/* 298 */       if (entry != -1) {
/* 299 */         if (entry == -2) firstDeleted = index;
/*     */         
/* 301 */         int mask = (1 << this.power) - 1;
/* 302 */         int step = tableLookupStep(fraction, mask, this.power);
/* 303 */         int n = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         do {
/* 309 */           index = index + step & mask;
/* 310 */           entry = keys[index];
/* 311 */           if (entry == key) return index; 
/* 312 */           if (entry != -2 || firstDeleted >= 0)
/* 313 */             continue;  firstDeleted = index;
/*     */         }
/* 315 */         while (entry != -1);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 321 */     if (firstDeleted >= 0) {
/* 322 */       index = firstDeleted;
/*     */     }
/*     */     else {
/*     */       
/* 326 */       if (keys == null || this.occupiedCount * 4 >= (1 << this.power) * 3) {
/*     */         
/* 328 */         rehashTable(intType);
/* 329 */         return insertNewKey(key);
/*     */       } 
/* 331 */       this.occupiedCount++;
/*     */     } 
/* 333 */     keys[index] = key;
/* 334 */     this.keyCount++;
/* 335 */     return index;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 341 */     out.defaultWriteObject();
/*     */     
/* 343 */     int count = this.keyCount;
/* 344 */     if (count != 0) {
/* 345 */       boolean hasIntValues = (this.ivaluesShift != 0);
/* 346 */       boolean hasObjectValues = (this.values != null);
/* 347 */       out.writeBoolean(hasIntValues);
/* 348 */       out.writeBoolean(hasObjectValues);
/*     */       
/* 350 */       for (int i = 0; count != 0; i++) {
/* 351 */         int key = this.keys[i];
/* 352 */         if (key != -1 && key != -2) {
/* 353 */           count--;
/* 354 */           out.writeInt(key);
/* 355 */           if (hasIntValues) {
/* 356 */             out.writeInt(this.keys[this.ivaluesShift + i]);
/*     */           }
/* 358 */           if (hasObjectValues) {
/* 359 */             out.writeObject(this.values[i]);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 369 */     in.defaultReadObject();
/*     */     
/* 371 */     int writtenKeyCount = this.keyCount;
/* 372 */     if (writtenKeyCount != 0) {
/* 373 */       this.keyCount = 0;
/* 374 */       boolean hasIntValues = in.readBoolean();
/* 375 */       boolean hasObjectValues = in.readBoolean();
/*     */       
/* 377 */       int N = 1 << this.power;
/* 378 */       if (hasIntValues) {
/* 379 */         this.keys = new int[2 * N];
/* 380 */         this.ivaluesShift = N;
/*     */       } else {
/* 382 */         this.keys = new int[N];
/*     */       }  int i;
/* 384 */       for (i = 0; i != N; i++) {
/* 385 */         this.keys[i] = -1;
/*     */       }
/* 387 */       if (hasObjectValues) {
/* 388 */         this.values = new Object[N];
/*     */       }
/* 390 */       for (i = 0; i != writtenKeyCount; i++) {
/* 391 */         int key = in.readInt();
/* 392 */         int index = insertNewKey(key);
/* 393 */         if (hasIntValues) {
/* 394 */           int ivalue = in.readInt();
/* 395 */           this.keys[this.ivaluesShift + index] = ivalue;
/*     */         } 
/* 397 */         if (hasObjectValues)
/* 398 */           this.values[index] = in.readObject(); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\UintMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */