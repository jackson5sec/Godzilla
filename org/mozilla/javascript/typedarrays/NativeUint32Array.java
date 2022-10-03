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
/*     */ public class NativeUint32Array
/*     */   extends NativeTypedArrayView<Long>
/*     */ {
/*     */   private static final long serialVersionUID = -7987831421954144244L;
/*     */   private static final String CLASS_NAME = "Uint32Array";
/*     */   private static final int BYTES_PER_ELEMENT = 4;
/*     */   
/*     */   public NativeUint32Array() {}
/*     */   
/*     */   public NativeUint32Array(NativeArrayBuffer ab, int off, int len) {
/*  33 */     super(ab, off, len, len * 4);
/*     */   }
/*     */ 
/*     */   
/*     */   public NativeUint32Array(int len) {
/*  38 */     this(new NativeArrayBuffer(len * 4), 0, len);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  44 */     return "Uint32Array";
/*     */   }
/*     */ 
/*     */   
/*     */   public static void init(Context cx, Scriptable scope, boolean sealed) {
/*  49 */     NativeUint32Array a = new NativeUint32Array();
/*  50 */     a.exportAsJSClass(4, scope, sealed);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected NativeTypedArrayView construct(NativeArrayBuffer ab, int off, int len) {
/*  56 */     return new NativeUint32Array(ab, off, len);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBytesPerElement() {
/*  62 */     return 4;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected NativeTypedArrayView realThis(Scriptable thisObj, IdFunctionObject f) {
/*  68 */     if (!(thisObj instanceof NativeUint32Array)) {
/*  69 */       throw incompatibleCallError(f);
/*     */     }
/*  71 */     return (NativeUint32Array)thisObj;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object js_get(int index) {
/*  77 */     if (checkIndex(index)) {
/*  78 */       return Undefined.instance;
/*     */     }
/*  80 */     return ByteIo.readUint32(this.arrayBuffer.buffer, index * 4 + this.offset, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object js_set(int index, Object c) {
/*  86 */     if (checkIndex(index)) {
/*  87 */       return Undefined.instance;
/*     */     }
/*  89 */     long val = Conversions.toUint32(c);
/*  90 */     ByteIo.writeUint32(this.arrayBuffer.buffer, index * 4 + this.offset, val, false);
/*  91 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Long get(int i) {
/*  97 */     if (checkIndex(i)) {
/*  98 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 100 */     return (Long)js_get(i);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Long set(int i, Long aByte) {
/* 106 */     if (checkIndex(i)) {
/* 107 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 109 */     return (Long)js_set(i, aByte);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\typedarrays\NativeUint32Array.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */