/*     */ package org.mozilla.javascript.typedarrays;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.RandomAccess;
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.ExternalArrayData;
/*     */ import org.mozilla.javascript.IdFunctionObject;
/*     */ import org.mozilla.javascript.NativeArray;
/*     */ import org.mozilla.javascript.ScriptRuntime;
/*     */ import org.mozilla.javascript.Scriptable;
/*     */ import org.mozilla.javascript.Undefined;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class NativeTypedArrayView<T>
/*     */   extends NativeArrayBufferView
/*     */   implements List<T>, RandomAccess, ExternalArrayData
/*     */ {
/*     */   protected final int length;
/*     */   private static final int Id_constructor = 1;
/*     */   private static final int Id_get = 2;
/*     */   private static final int Id_set = 3;
/*     */   private static final int Id_subarray = 4;
/*     */   protected static final int MAX_PROTOTYPE_ID = 4;
/*     */   private static final int Id_length = 10;
/*     */   private static final int Id_BYTES_PER_ELEMENT = 11;
/*     */   private static final int MAX_INSTANCE_ID = 11;
/*     */   
/*     */   protected NativeTypedArrayView() {
/*  39 */     this.length = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected NativeTypedArrayView(NativeArrayBuffer ab, int off, int len, int byteLen) {
/*  44 */     super(ab, off, byteLen);
/*  45 */     this.length = len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object get(int index, Scriptable start) {
/*  53 */     return js_get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean has(int index, Scriptable start) {
/*  59 */     return (index > 0 && index < this.length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(int index, Scriptable start, Object val) {
/*  65 */     js_set(index, val);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete(int index) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getIds() {
/*  76 */     Object[] ret = new Object[this.length];
/*  77 */     for (int i = 0; i < this.length; i++) {
/*  78 */       ret[i] = Integer.valueOf(i);
/*     */     }
/*  80 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkIndex(int index) {
/*  87 */     return (index < 0 || index >= this.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract int getBytesPerElement();
/*     */ 
/*     */   
/*     */   protected abstract NativeTypedArrayView construct(NativeArrayBuffer paramNativeArrayBuffer, int paramInt1, int paramInt2);
/*     */   
/*     */   protected abstract Object js_get(int paramInt);
/*     */   
/*     */   protected abstract Object js_set(int paramInt, Object paramObject);
/*     */   
/*     */   protected abstract NativeTypedArrayView realThis(Scriptable paramScriptable, IdFunctionObject paramIdFunctionObject);
/*     */   
/*     */   private NativeArrayBuffer makeArrayBuffer(Context cx, Scriptable scope, int length) {
/* 103 */     return (NativeArrayBuffer)cx.newObject(scope, "ArrayBuffer", new Object[] { Integer.valueOf(length) });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private NativeTypedArrayView js_constructor(Context cx, Scriptable scope, Object[] args) {
/* 109 */     if (!isArg(args, 0)) {
/* 110 */       return construct(NativeArrayBuffer.EMPTY_BUFFER, 0, 0);
/*     */     }
/* 112 */     if (args[0] instanceof Number || args[0] instanceof String) {
/*     */       
/* 114 */       int length = ScriptRuntime.toInt32(args[0]);
/* 115 */       NativeArrayBuffer buffer = makeArrayBuffer(cx, scope, length * getBytesPerElement());
/* 116 */       return construct(buffer, 0, length);
/*     */     } 
/* 118 */     if (args[0] instanceof NativeTypedArrayView) {
/*     */       
/* 120 */       NativeTypedArrayView src = (NativeTypedArrayView)args[0];
/* 121 */       NativeArrayBuffer na = makeArrayBuffer(cx, scope, src.length * getBytesPerElement());
/* 122 */       NativeTypedArrayView v = construct(na, 0, src.length);
/*     */       
/* 124 */       for (int i = 0; i < src.length; i++) {
/* 125 */         v.js_set(i, src.js_get(i));
/*     */       }
/* 127 */       return v;
/*     */     } 
/* 129 */     if (args[0] instanceof NativeArrayBuffer) {
/*     */       int byteLen;
/* 131 */       NativeArrayBuffer na = (NativeArrayBuffer)args[0];
/* 132 */       int byteOff = isArg(args, 1) ? ScriptRuntime.toInt32(args[1]) : 0;
/*     */ 
/*     */       
/* 135 */       if (isArg(args, 2)) {
/* 136 */         byteLen = ScriptRuntime.toInt32(args[2]) * getBytesPerElement();
/*     */       } else {
/* 138 */         byteLen = na.getLength() - byteOff;
/*     */       } 
/*     */       
/* 141 */       if (byteOff < 0 || byteOff > na.buffer.length) {
/* 142 */         throw ScriptRuntime.constructError("RangeError", "offset out of range");
/*     */       }
/* 144 */       if (byteLen < 0 || byteOff + byteLen > na.buffer.length) {
/* 145 */         throw ScriptRuntime.constructError("RangeError", "length out of range");
/*     */       }
/* 147 */       if (byteOff % getBytesPerElement() != 0) {
/* 148 */         throw ScriptRuntime.constructError("RangeError", "offset must be a multiple of the byte size");
/*     */       }
/* 150 */       if (byteLen % getBytesPerElement() != 0) {
/* 151 */         throw ScriptRuntime.constructError("RangeError", "offset and buffer must be a multiple of the byte size");
/*     */       }
/*     */       
/* 154 */       return construct(na, byteOff, byteLen / getBytesPerElement());
/*     */     } 
/* 156 */     if (args[0] instanceof NativeArray) {
/*     */       
/* 158 */       List l = (List)args[0];
/* 159 */       NativeArrayBuffer na = makeArrayBuffer(cx, scope, l.size() * getBytesPerElement());
/* 160 */       NativeTypedArrayView v = construct(na, 0, l.size());
/* 161 */       int p = 0;
/* 162 */       for (Object o : l) {
/* 163 */         v.js_set(p, o);
/* 164 */         p++;
/*     */       } 
/* 166 */       return v;
/*     */     } 
/*     */     
/* 169 */     throw ScriptRuntime.constructError("Error", "invalid argument");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void setRange(NativeTypedArrayView v, int off) {
/* 175 */     if (off >= this.length) {
/* 176 */       throw ScriptRuntime.constructError("RangeError", "offset out of range");
/*     */     }
/*     */     
/* 179 */     if (v.length > this.length - off) {
/* 180 */       throw ScriptRuntime.constructError("RangeError", "source array too long");
/*     */     }
/*     */     
/* 183 */     if (v.arrayBuffer == this.arrayBuffer) {
/*     */       
/* 185 */       Object[] tmp = new Object[v.length]; int i;
/* 186 */       for (i = 0; i < v.length; i++) {
/* 187 */         tmp[i] = v.js_get(i);
/*     */       }
/* 189 */       for (i = 0; i < v.length; i++) {
/* 190 */         js_set(i + off, tmp[i]);
/*     */       }
/*     */     } else {
/* 193 */       for (int i = 0; i < v.length; i++) {
/* 194 */         js_set(i + off, v.js_get(i));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void setRange(NativeArray a, int off) {
/* 201 */     if (off > this.length) {
/* 202 */       throw ScriptRuntime.constructError("RangeError", "offset out of range");
/*     */     }
/* 204 */     if (off + a.size() > this.length) {
/* 205 */       throw ScriptRuntime.constructError("RangeError", "offset + length out of range");
/*     */     }
/*     */     
/* 208 */     int pos = off;
/* 209 */     for (Object val : a) {
/* 210 */       js_set(pos, val);
/* 211 */       pos++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private Object js_subarray(Context cx, Scriptable scope, int s, int e) {
/* 217 */     int start = (s < 0) ? (this.length + s) : s;
/* 218 */     int end = (e < 0) ? (this.length + e) : e;
/*     */ 
/*     */     
/* 221 */     start = Math.max(0, start);
/* 222 */     end = Math.min(this.length, end);
/* 223 */     int len = Math.max(0, end - start);
/* 224 */     int byteOff = Math.min(start * getBytesPerElement(), this.arrayBuffer.getLength());
/*     */     
/* 226 */     return cx.newObject(scope, getClassName(), new Object[] { this.arrayBuffer, Integer.valueOf(byteOff), Integer.valueOf(len) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 237 */     if (!f.hasTag(getClassName())) {
/* 238 */       return super.execIdCall(f, cx, scope, thisObj, args);
/*     */     }
/* 240 */     int id = f.methodId();
/* 241 */     switch (id) {
/*     */       case 1:
/* 243 */         return js_constructor(cx, scope, args);
/*     */       
/*     */       case 2:
/* 246 */         if (args.length > 0) {
/* 247 */           return realThis(thisObj, f).js_get(ScriptRuntime.toInt32(args[0]));
/*     */         }
/* 249 */         throw ScriptRuntime.constructError("Error", "invalid arguments");
/*     */ 
/*     */       
/*     */       case 3:
/* 253 */         if (args.length > 0) {
/* 254 */           NativeTypedArrayView self = realThis(thisObj, f);
/* 255 */           if (args[0] instanceof NativeTypedArrayView) {
/* 256 */             int offset = isArg(args, 1) ? ScriptRuntime.toInt32(args[1]) : 0;
/* 257 */             self.setRange((NativeTypedArrayView)args[0], offset);
/* 258 */             return Undefined.instance;
/*     */           } 
/* 260 */           if (args[0] instanceof NativeArray) {
/* 261 */             int offset = isArg(args, 1) ? ScriptRuntime.toInt32(args[1]) : 0;
/* 262 */             self.setRange((NativeArray)args[0], offset);
/* 263 */             return Undefined.instance;
/*     */           } 
/* 265 */           if (args[0] instanceof Scriptable)
/*     */           {
/* 267 */             return Undefined.instance;
/*     */           }
/* 269 */           if (isArg(args, 2)) {
/* 270 */             return self.js_set(ScriptRuntime.toInt32(args[0]), args[1]);
/*     */           }
/*     */         } 
/* 273 */         throw ScriptRuntime.constructError("Error", "invalid arguments");
/*     */       
/*     */       case 4:
/* 276 */         if (args.length > 0) {
/* 277 */           NativeTypedArrayView self = realThis(thisObj, f);
/* 278 */           int start = ScriptRuntime.toInt32(args[0]);
/* 279 */           int end = isArg(args, 1) ? ScriptRuntime.toInt32(args[1]) : self.length;
/* 280 */           return self.js_subarray(cx, scope, start, end);
/*     */         } 
/* 282 */         throw ScriptRuntime.constructError("Error", "invalid arguments");
/*     */     } 
/*     */     
/* 285 */     throw new IllegalArgumentException(String.valueOf(id));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initPrototypeId(int id) {
/*     */     String s;
/*     */     int arity;
/* 293 */     switch (id) { case 1:
/* 294 */         arity = 1; s = "constructor"; break;
/* 295 */       case 2: arity = 1; s = "get"; break;
/* 296 */       case 3: arity = 2; s = "set"; break;
/* 297 */       case 4: arity = 2; s = "subarray"; break;
/* 298 */       default: throw new IllegalArgumentException(String.valueOf(id)); }
/*     */     
/* 300 */     initPrototypeMethod(getClassName(), id, s, arity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int findPrototypeId(String s) {
/* 310 */     int id = 0; String X = null;
/* 311 */     int s_length = s.length();
/* 312 */     if (s_length == 3)
/* 313 */     { int c = s.charAt(0);
/* 314 */       if (c == 103) { if (s.charAt(2) == 't' && s.charAt(1) == 'e') { id = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 323 */           return id; }  } else if (c == 115 && s.charAt(2) == 't' && s.charAt(1) == 'e') { id = 3; return id; }  } else if (s_length == 8) { X = "subarray"; id = 4; } else if (s_length == 11) { X = "constructor"; id = 1; }  if (X != null && X != s && !X.equals(s)) id = 0;  return id;
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
/*     */   protected void fillConstructorProperties(IdFunctionObject ctor) {
/* 343 */     ctor.put("BYTES_PER_ELEMENT", (Scriptable)ctor, ScriptRuntime.wrapInt(getBytesPerElement()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getMaxInstanceId() {
/* 351 */     return 11;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getInstanceIdName(int id) {
/* 357 */     switch (id) { case 10:
/* 358 */         return "length";
/* 359 */       case 11: return "BYTES_PER_ELEMENT"; }
/* 360 */      return super.getInstanceIdName(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object getInstanceIdValue(int id) {
/* 367 */     switch (id) {
/*     */       case 10:
/* 369 */         return ScriptRuntime.wrapInt(this.length);
/*     */       case 11:
/* 371 */         return ScriptRuntime.wrapInt(getBytesPerElement());
/*     */     } 
/* 373 */     return super.getInstanceIdValue(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int findInstanceIdInfo(String s) {
/* 384 */     int id = 0; String X = null;
/* 385 */     int s_length = s.length();
/* 386 */     if (s_length == 6) { X = "length"; id = 10; }
/* 387 */     else if (s_length == 17) { X = "BYTES_PER_ELEMENT"; id = 11; }
/* 388 */      if (X != null && X != s && !X.equals(s)) id = 0;
/*     */ 
/*     */ 
/*     */     
/* 392 */     if (id == 0) {
/* 393 */       return super.findInstanceIdInfo(s);
/*     */     }
/* 395 */     return instanceIdInfo(5, id);
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
/*     */   public Object getArrayElement(int index) {
/* 413 */     return js_get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArrayElement(int index, Object value) {
/* 419 */     js_set(index, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getArrayLength() {
/* 424 */     return this.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 432 */     return this.length;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 438 */     return (this.length == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Object o) {
/* 444 */     return (indexOf(o) >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> objects) {
/* 450 */     for (Object o : objects) {
/* 451 */       if (!contains(o)) {
/* 452 */         return false;
/*     */       }
/*     */     } 
/* 455 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOf(Object o) {
/* 461 */     for (int i = 0; i < this.length; i++) {
/* 462 */       if (o.equals(js_get(i))) {
/* 463 */         return i;
/*     */       }
/*     */     } 
/* 466 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int lastIndexOf(Object o) {
/* 472 */     for (int i = this.length - 1; i >= 0; i--) {
/* 473 */       if (o.equals(js_get(i))) {
/* 474 */         return i;
/*     */       }
/*     */     } 
/* 477 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 483 */     Object[] a = new Object[this.length];
/* 484 */     for (int i = 0; i < this.length; i++) {
/* 485 */       a[i] = js_get(i);
/*     */     }
/* 487 */     return a;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <U> U[] toArray(U[] ts) {
/*     */     U[] a;
/* 495 */     if (ts.length >= this.length) {
/* 496 */       a = ts;
/*     */     } else {
/* 498 */       a = (U[])Array.newInstance(ts.getClass().getComponentType(), this.length);
/*     */     } 
/*     */     
/* 501 */     for (int i = 0; i < this.length; i++) {
/*     */       try {
/* 503 */         a[i] = (U)js_get(i);
/* 504 */       } catch (ClassCastException cce) {
/* 505 */         throw new ArrayStoreException();
/*     */       } 
/*     */     } 
/* 508 */     return a;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*     */     try {
/* 515 */       NativeTypedArrayView<T> v = (NativeTypedArrayView<T>)o;
/* 516 */       if (this.length != v.length) {
/* 517 */         return false;
/*     */       }
/* 519 */       for (int i = 0; i < this.length; i++) {
/* 520 */         if (!js_get(i).equals(v.js_get(i))) {
/* 521 */           return false;
/*     */         }
/*     */       } 
/* 524 */       return true;
/* 525 */     } catch (ClassCastException cce) {
/* 526 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 533 */     int hc = 0;
/* 534 */     for (int i = 0; i < this.length; i++) {
/* 535 */       hc += js_get(i).hashCode();
/*     */     }
/* 537 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<T> iterator() {
/* 543 */     return new NativeTypedArrayIterator<T>(this, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ListIterator<T> listIterator() {
/* 549 */     return new NativeTypedArrayIterator<T>(this, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ListIterator<T> listIterator(int start) {
/* 555 */     if (checkIndex(start)) {
/* 556 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 558 */     return new NativeTypedArrayIterator<T>(this, start);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<T> subList(int i, int i2) {
/* 564 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(T aByte) {
/* 570 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(int i, T aByte) {
/* 576 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends T> bytes) {
/* 582 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAll(int i, Collection<? extends T> bytes) {
/* 588 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 594 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public T remove(int i) {
/* 600 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove(Object o) {
/* 606 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> objects) {
/* 612 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> objects) {
/* 618 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\typedarrays\NativeTypedArrayView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */