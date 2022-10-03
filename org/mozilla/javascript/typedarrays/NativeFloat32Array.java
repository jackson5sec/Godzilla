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
/*     */ public class NativeFloat32Array
/*     */   extends NativeTypedArrayView<Float>
/*     */ {
/*     */   private static final long serialVersionUID = -8963461831950499340L;
/*     */   private static final String CLASS_NAME = "Float32Array";
/*     */   private static final int BYTES_PER_ELEMENT = 4;
/*     */   
/*     */   public NativeFloat32Array() {}
/*     */   
/*     */   public NativeFloat32Array(NativeArrayBuffer ab, int off, int len) {
/*  34 */     super(ab, off, len, len * 4);
/*     */   }
/*     */ 
/*     */   
/*     */   public NativeFloat32Array(int len) {
/*  39 */     this(new NativeArrayBuffer(len * 4), 0, len);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  45 */     return "Float32Array";
/*     */   }
/*     */ 
/*     */   
/*     */   public static void init(Context cx, Scriptable scope, boolean sealed) {
/*  50 */     NativeFloat32Array a = new NativeFloat32Array();
/*  51 */     a.exportAsJSClass(4, scope, sealed);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected NativeTypedArrayView construct(NativeArrayBuffer ab, int off, int len) {
/*  57 */     return new NativeFloat32Array(ab, off, len);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBytesPerElement() {
/*  63 */     return 4;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected NativeTypedArrayView realThis(Scriptable thisObj, IdFunctionObject f) {
/*  69 */     if (!(thisObj instanceof NativeFloat32Array)) {
/*  70 */       throw incompatibleCallError(f);
/*     */     }
/*  72 */     return (NativeFloat32Array)thisObj;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object js_get(int index) {
/*  78 */     if (checkIndex(index)) {
/*  79 */       return Undefined.instance;
/*     */     }
/*  81 */     return ByteIo.readFloat32(this.arrayBuffer.buffer, index * 4 + this.offset, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object js_set(int index, Object c) {
/*  87 */     if (checkIndex(index)) {
/*  88 */       return Undefined.instance;
/*     */     }
/*  90 */     double val = ScriptRuntime.toNumber(c);
/*  91 */     ByteIo.writeFloat32(this.arrayBuffer.buffer, index * 4 + this.offset, val, false);
/*  92 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Float get(int i) {
/*  98 */     if (checkIndex(i)) {
/*  99 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 101 */     return (Float)js_get(i);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Float set(int i, Float aByte) {
/* 107 */     if (checkIndex(i)) {
/* 108 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 110 */     return (Float)js_set(i, aByte);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\typedarrays\NativeFloat32Array.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */