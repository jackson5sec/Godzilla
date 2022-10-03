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
/*     */ public class NativeInt32Array
/*     */   extends NativeTypedArrayView<Integer>
/*     */ {
/*     */   private static final long serialVersionUID = -8963461831950499340L;
/*     */   private static final String CLASS_NAME = "Int32Array";
/*     */   private static final int BYTES_PER_ELEMENT = 4;
/*     */   
/*     */   public NativeInt32Array() {}
/*     */   
/*     */   public NativeInt32Array(NativeArrayBuffer ab, int off, int len) {
/*  34 */     super(ab, off, len, len * 4);
/*     */   }
/*     */ 
/*     */   
/*     */   public NativeInt32Array(int len) {
/*  39 */     this(new NativeArrayBuffer(len * 4), 0, len);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  45 */     return "Int32Array";
/*     */   }
/*     */ 
/*     */   
/*     */   public static void init(Context cx, Scriptable scope, boolean sealed) {
/*  50 */     NativeInt32Array a = new NativeInt32Array();
/*  51 */     a.exportAsJSClass(4, scope, sealed);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected NativeTypedArrayView construct(NativeArrayBuffer ab, int off, int len) {
/*  57 */     return new NativeInt32Array(ab, off, len);
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
/*  69 */     if (!(thisObj instanceof NativeInt32Array)) {
/*  70 */       throw incompatibleCallError(f);
/*     */     }
/*  72 */     return (NativeInt32Array)thisObj;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object js_get(int index) {
/*  78 */     if (checkIndex(index)) {
/*  79 */       return Undefined.instance;
/*     */     }
/*  81 */     return ByteIo.readInt32(this.arrayBuffer.buffer, index * 4 + this.offset, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object js_set(int index, Object c) {
/*  87 */     if (checkIndex(index)) {
/*  88 */       return Undefined.instance;
/*     */     }
/*  90 */     int val = ScriptRuntime.toInt32(c);
/*  91 */     ByteIo.writeInt32(this.arrayBuffer.buffer, index * 4 + this.offset, val, false);
/*  92 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer get(int i) {
/*  98 */     if (checkIndex(i)) {
/*  99 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 101 */     return (Integer)js_get(i);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer set(int i, Integer aByte) {
/* 107 */     if (checkIndex(i)) {
/* 108 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 110 */     return (Integer)js_set(i, aByte);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\typedarrays\NativeInt32Array.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */