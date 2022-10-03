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
/*     */ public class ObjArray
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = 4174889037736658296L;
/*     */   private int size;
/*     */   private boolean sealed;
/*     */   private static final int FIELDS_STORE_SIZE = 5;
/*     */   private transient Object f0;
/*     */   private transient Object f1;
/*     */   private transient Object f2;
/*     */   private transient Object f3;
/*     */   private transient Object f4;
/*     */   private transient Object[] data;
/*     */   
/*     */   public final boolean isSealed() {
/*  26 */     return this.sealed;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void seal() {
/*  31 */     this.sealed = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isEmpty() {
/*  36 */     return (this.size == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int size() {
/*  41 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void setSize(int newSize) {
/*  46 */     if (newSize < 0) throw new IllegalArgumentException(); 
/*  47 */     if (this.sealed) throw onSeledMutation(); 
/*  48 */     int N = this.size;
/*  49 */     if (newSize < N) {
/*  50 */       for (int i = newSize; i != N; i++) {
/*  51 */         setImpl(i, null);
/*     */       }
/*  53 */     } else if (newSize > N && 
/*  54 */       newSize > 5) {
/*  55 */       ensureCapacity(newSize);
/*     */     } 
/*     */     
/*  58 */     this.size = newSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Object get(int index) {
/*  63 */     if (0 > index || index >= this.size) throw onInvalidIndex(index, this.size); 
/*  64 */     return getImpl(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void set(int index, Object value) {
/*  69 */     if (0 > index || index >= this.size) throw onInvalidIndex(index, this.size); 
/*  70 */     if (this.sealed) throw onSeledMutation(); 
/*  71 */     setImpl(index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   private Object getImpl(int index) {
/*  76 */     switch (index) { case 0:
/*  77 */         return this.f0;
/*  78 */       case 1: return this.f1;
/*  79 */       case 2: return this.f2;
/*  80 */       case 3: return this.f3;
/*  81 */       case 4: return this.f4; }
/*     */     
/*  83 */     return this.data[index - 5];
/*     */   }
/*     */ 
/*     */   
/*     */   private void setImpl(int index, Object value) {
/*  88 */     switch (index) { case 0:
/*  89 */         this.f0 = value; return;
/*  90 */       case 1: this.f1 = value; return;
/*  91 */       case 2: this.f2 = value; return;
/*  92 */       case 3: this.f3 = value; return;
/*  93 */       case 4: this.f4 = value; return; }
/*  94 */      this.data[index - 5] = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOf(Object obj) {
/* 101 */     int N = this.size;
/* 102 */     for (int i = 0; i != N; i++) {
/* 103 */       Object current = getImpl(i);
/* 104 */       if (current == obj || (current != null && current.equals(obj))) {
/* 105 */         return i;
/*     */       }
/*     */     } 
/* 108 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(Object obj) {
/* 113 */     for (int i = this.size; i != 0; ) {
/* 114 */       i--;
/* 115 */       Object current = getImpl(i);
/* 116 */       if (current == obj || (current != null && current.equals(obj))) {
/* 117 */         return i;
/*     */       }
/*     */     } 
/* 120 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Object peek() {
/* 125 */     int N = this.size;
/* 126 */     if (N == 0) throw onEmptyStackTopRead(); 
/* 127 */     return getImpl(N - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Object pop() {
/* 132 */     if (this.sealed) throw onSeledMutation(); 
/* 133 */     int N = this.size;
/* 134 */     N--;
/*     */     
/* 136 */     switch (N) { case -1:
/* 137 */         throw onEmptyStackTopRead();
/* 138 */       case 0: top = this.f0; this.f0 = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 147 */         this.size = N;
/* 148 */         return top;case 1: top = this.f1; this.f1 = null; this.size = N; return top;case 2: top = this.f2; this.f2 = null; this.size = N; return top;case 3: top = this.f3; this.f3 = null; this.size = N; return top;case 4: top = this.f4; this.f4 = null; this.size = N; return top; }  Object top = this.data[N - 5]; this.data[N - 5] = null; this.size = N; return top;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void push(Object value) {
/* 153 */     add(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void add(Object value) {
/* 158 */     if (this.sealed) throw onSeledMutation(); 
/* 159 */     int N = this.size;
/* 160 */     if (N >= 5) {
/* 161 */       ensureCapacity(N + 1);
/*     */     }
/* 163 */     this.size = N + 1;
/* 164 */     setImpl(N, value);
/*     */   }
/*     */   
/*     */   public final void add(int index, Object value) {
/*     */     Object tmp;
/* 169 */     int N = this.size;
/* 170 */     if (0 > index || index > N) throw onInvalidIndex(index, N + 1); 
/* 171 */     if (this.sealed) throw onSeledMutation();
/*     */     
/* 173 */     switch (index) {
/*     */       case 0:
/* 175 */         if (N == 0) { this.f0 = value; break; }
/* 176 */          tmp = this.f0; this.f0 = value; value = tmp;
/*     */       case 1:
/* 178 */         if (N == 1) { this.f1 = value; break; }
/* 179 */          tmp = this.f1; this.f1 = value; value = tmp;
/*     */       case 2:
/* 181 */         if (N == 2) { this.f2 = value; break; }
/* 182 */          tmp = this.f2; this.f2 = value; value = tmp;
/*     */       case 3:
/* 184 */         if (N == 3) { this.f3 = value; break; }
/* 185 */          tmp = this.f3; this.f3 = value; value = tmp;
/*     */       case 4:
/* 187 */         if (N == 4) { this.f4 = value; break; }
/* 188 */          tmp = this.f4; this.f4 = value; value = tmp;
/*     */         
/* 190 */         index = 5;
/*     */       default:
/* 192 */         ensureCapacity(N + 1);
/* 193 */         if (index != N) {
/* 194 */           System.arraycopy(this.data, index - 5, this.data, index - 5 + 1, N - index);
/*     */         }
/*     */ 
/*     */         
/* 198 */         this.data[index - 5] = value; break;
/*     */     } 
/* 200 */     this.size = N + 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void remove(int index) {
/* 205 */     int N = this.size;
/* 206 */     if (0 > index || index >= N) throw onInvalidIndex(index, N); 
/* 207 */     if (this.sealed) throw onSeledMutation(); 
/* 208 */     N--;
/* 209 */     switch (index) {
/*     */       case 0:
/* 211 */         if (N == 0) { this.f0 = null; break; }
/* 212 */          this.f0 = this.f1;
/*     */       case 1:
/* 214 */         if (N == 1) { this.f1 = null; break; }
/* 215 */          this.f1 = this.f2;
/*     */       case 2:
/* 217 */         if (N == 2) { this.f2 = null; break; }
/* 218 */          this.f2 = this.f3;
/*     */       case 3:
/* 220 */         if (N == 3) { this.f3 = null; break; }
/* 221 */          this.f3 = this.f4;
/*     */       case 4:
/* 223 */         if (N == 4) { this.f4 = null; break; }
/* 224 */          this.f4 = this.data[0];
/*     */         
/* 226 */         index = 5;
/*     */       default:
/* 228 */         if (index != N) {
/* 229 */           System.arraycopy(this.data, index - 5 + 1, this.data, index - 5, N - index);
/*     */         }
/*     */ 
/*     */         
/* 233 */         this.data[N - 5] = null; break;
/*     */     } 
/* 235 */     this.size = N;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void clear() {
/* 240 */     if (this.sealed) throw onSeledMutation(); 
/* 241 */     int N = this.size;
/* 242 */     for (int i = 0; i != N; i++) {
/* 243 */       setImpl(i, null);
/*     */     }
/* 245 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Object[] toArray() {
/* 250 */     Object[] array = new Object[this.size];
/* 251 */     toArray(array, 0);
/* 252 */     return array;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void toArray(Object[] array) {
/* 257 */     toArray(array, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void toArray(Object[] array, int offset) {
/* 262 */     int N = this.size;
/* 263 */     switch (N) {
/*     */       default:
/* 265 */         System.arraycopy(this.data, 0, array, offset + 5, N - 5);
/*     */       case 5:
/* 267 */         array[offset + 4] = this.f4;
/* 268 */       case 4: array[offset + 3] = this.f3;
/* 269 */       case 3: array[offset + 2] = this.f2;
/* 270 */       case 2: array[offset + 1] = this.f1;
/* 271 */       case 1: array[offset + 0] = this.f0;
/*     */         break;
/*     */       case 0:
/*     */         break;
/*     */     } 
/*     */   }
/*     */   private void ensureCapacity(int minimalCapacity) {
/* 278 */     int required = minimalCapacity - 5;
/* 279 */     if (required <= 0) throw new IllegalArgumentException(); 
/* 280 */     if (this.data == null) {
/* 281 */       int alloc = 10;
/* 282 */       if (alloc < required) {
/* 283 */         alloc = required;
/*     */       }
/* 285 */       this.data = new Object[alloc];
/*     */     } else {
/* 287 */       int alloc = this.data.length;
/* 288 */       if (alloc < required) {
/* 289 */         if (alloc <= 5) {
/* 290 */           alloc = 10;
/*     */         } else {
/* 292 */           alloc *= 2;
/*     */         } 
/* 294 */         if (alloc < required) {
/* 295 */           alloc = required;
/*     */         }
/* 297 */         Object[] tmp = new Object[alloc];
/* 298 */         if (this.size > 5) {
/* 299 */           System.arraycopy(this.data, 0, tmp, 0, this.size - 5);
/*     */         }
/*     */         
/* 302 */         this.data = tmp;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static RuntimeException onInvalidIndex(int index, int upperBound) {
/* 310 */     String msg = index + " ∉ [0, " + upperBound + ')';
/* 311 */     throw new IndexOutOfBoundsException(msg);
/*     */   }
/*     */ 
/*     */   
/*     */   private static RuntimeException onEmptyStackTopRead() {
/* 316 */     throw new RuntimeException("Empty stack");
/*     */   }
/*     */ 
/*     */   
/*     */   private static RuntimeException onSeledMutation() {
/* 321 */     throw new IllegalStateException("Attempt to modify sealed array");
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream os) throws IOException {
/* 326 */     os.defaultWriteObject();
/* 327 */     int N = this.size;
/* 328 */     for (int i = 0; i != N; i++) {
/* 329 */       Object obj = getImpl(i);
/* 330 */       os.writeObject(obj);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream is) throws IOException, ClassNotFoundException {
/* 337 */     is.defaultReadObject();
/* 338 */     int N = this.size;
/* 339 */     if (N > 5) {
/* 340 */       this.data = new Object[N - 5];
/*     */     }
/* 342 */     for (int i = 0; i != N; i++) {
/* 343 */       Object obj = is.readObject();
/* 344 */       setImpl(i, obj);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ObjArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */