/*     */ package org.mozilla.javascript.typedarrays;
/*     */ 
/*     */ import org.mozilla.javascript.Context;
/*     */ import org.mozilla.javascript.IdFunctionObject;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NativeFloat64Array
/*     */   extends NativeTypedArrayView<Double>
/*     */ {
/*     */   private static final long serialVersionUID = -1255405650050639335L;
/*     */   private static final String CLASS_NAME = "Float64Array";
/*     */   private static final int BYTES_PER_ELEMENT = 8;
/*     */   
/*     */   public NativeFloat64Array() {}
/*     */   
/*     */   public NativeFloat64Array(NativeArrayBuffer ab, int off, int len) {
/*  34 */     super(ab, off, len, len * 8);
/*     */   }
/*     */ 
/*     */   
/*     */   public NativeFloat64Array(int len) {
/*  39 */     this(new NativeArrayBuffer(len * 8), 0, len);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  45 */     return "Float64Array";
/*     */   }
/*     */ 
/*     */   
/*     */   public static void init(Context cx, Scriptable scope, boolean sealed) {
/*  50 */     NativeFloat64Array a = new NativeFloat64Array();
/*  51 */     a.exportAsJSClass(4, scope, sealed);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected NativeTypedArrayView construct(NativeArrayBuffer ab, int off, int len) {
/*  57 */     return new NativeFloat64Array(ab, off, len);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBytesPerElement() {
/*  63 */     return 8;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected NativeTypedArrayView realThis(Scriptable thisObj, IdFunctionObject f) {
/*  69 */     if (!(thisObj instanceof NativeFloat64Array)) {
/*  70 */       throw incompatibleCallError(f);
/*     */     }
/*  72 */     return (NativeFloat64Array)thisObj;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object js_get(int index) {
/*  78 */     if (checkIndex(index)) {
/*  79 */       return Undefined.instance;
/*     */     }
/*  81 */     long base = ByteIo.readUint64Primitive(this.arrayBuffer.buffer, index * 8 + this.offset, false);
/*  82 */     return Double.valueOf(Double.longBitsToDouble(base));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object js_set(int index, Object c) {
/*  88 */     if (checkIndex(index)) {
/*  89 */       return Undefined.instance;
/*     */     }
/*  91 */     double val = ScriptRuntime.toNumber(c);
/*  92 */     long base = Double.doubleToLongBits(val);
/*  93 */     ByteIo.writeUint64(this.arrayBuffer.buffer, index * 8 + this.offset, base, false);
/*  94 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Double get(int i) {
/* 100 */     if (checkIndex(i)) {
/* 101 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 103 */     return (Double)js_get(i);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Double set(int i, Double aByte) {
/* 109 */     if (checkIndex(i)) {
/* 110 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 112 */     return (Double)js_set(i, aByte);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\typedarrays\NativeFloat64Array.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */