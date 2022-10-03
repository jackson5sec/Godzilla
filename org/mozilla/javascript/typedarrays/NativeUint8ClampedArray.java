/*     */ package org.mozilla.javascript.typedarrays;
/*     */ 
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.IdFunctionObject;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NativeUint8ClampedArray
/*     */   extends NativeTypedArrayView<Integer>
/*     */ {
/*     */   private static final long serialVersionUID = -3349419704390398895L;
/*     */   private static final String CLASS_NAME = "Uint8ClampedArray";
/*     */   
/*     */   public NativeUint8ClampedArray() {}
/*     */   
/*     */   public NativeUint8ClampedArray(NativeArrayBuffer ab, int off, int len) {
/*  33 */     super(ab, off, len, len);
/*     */   }
/*     */ 
/*     */   
/*     */   public NativeUint8ClampedArray(int len) {
/*  38 */     this(new NativeArrayBuffer(len), 0, len);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  44 */     return "Uint8ClampedArray";
/*     */   }
/*     */ 
/*     */   
/*     */   public static void init(Context cx, Scriptable scope, boolean sealed) {
/*  49 */     NativeUint8ClampedArray a = new NativeUint8ClampedArray();
/*  50 */     a.exportAsJSClass(4, scope, sealed);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected NativeTypedArrayView construct(NativeArrayBuffer ab, int off, int len) {
/*  56 */     return new NativeUint8ClampedArray(ab, off, len);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBytesPerElement() {
/*  62 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected NativeTypedArrayView realThis(Scriptable thisObj, IdFunctionObject f) {
/*  68 */     if (!(thisObj instanceof NativeUint8ClampedArray)) {
/*  69 */       throw incompatibleCallError(f);
/*     */     }
/*  71 */     return (NativeUint8ClampedArray)thisObj;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object js_get(int index) {
/*  77 */     if (checkIndex(index)) {
/*  78 */       return Undefined.instance;
/*     */     }
/*  80 */     return ByteIo.readUint8(this.arrayBuffer.buffer, index + this.offset);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object js_set(int index, Object c) {
/*  86 */     if (checkIndex(index)) {
/*  87 */       return Undefined.instance;
/*     */     }
/*  89 */     int val = Conversions.toUint8Clamp(c);
/*  90 */     ByteIo.writeUint8(this.arrayBuffer.buffer, index + this.offset, val);
/*  91 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer get(int i) {
/*  97 */     if (checkIndex(i)) {
/*  98 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 100 */     return (Integer)js_get(i);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer set(int i, Integer aByte) {
/* 106 */     if (checkIndex(i)) {
/* 107 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 109 */     return (Integer)js_set(i, aByte);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\typedarrays\NativeUint8ClampedArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */