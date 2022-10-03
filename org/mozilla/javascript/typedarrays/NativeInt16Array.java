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
/*     */ public class NativeInt16Array
/*     */   extends NativeTypedArrayView<Short>
/*     */ {
/*     */   private static final long serialVersionUID = -8592870435287581398L;
/*     */   private static final String CLASS_NAME = "Int16Array";
/*     */   private static final int BYTES_PER_ELEMENT = 2;
/*     */   
/*     */   public NativeInt16Array() {}
/*     */   
/*     */   public NativeInt16Array(NativeArrayBuffer ab, int off, int len) {
/*  33 */     super(ab, off, len, len * 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public NativeInt16Array(int len) {
/*  38 */     this(new NativeArrayBuffer(len * 2), 0, len);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  44 */     return "Int16Array";
/*     */   }
/*     */ 
/*     */   
/*     */   public static void init(Context cx, Scriptable scope, boolean sealed) {
/*  49 */     NativeInt16Array a = new NativeInt16Array();
/*  50 */     a.exportAsJSClass(4, scope, sealed);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected NativeTypedArrayView construct(NativeArrayBuffer ab, int off, int len) {
/*  56 */     return new NativeInt16Array(ab, off, len);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBytesPerElement() {
/*  62 */     return 2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected NativeTypedArrayView realThis(Scriptable thisObj, IdFunctionObject f) {
/*  68 */     if (!(thisObj instanceof NativeInt16Array)) {
/*  69 */       throw incompatibleCallError(f);
/*     */     }
/*  71 */     return (NativeInt16Array)thisObj;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object js_get(int index) {
/*  77 */     if (checkIndex(index)) {
/*  78 */       return Undefined.instance;
/*     */     }
/*  80 */     return ByteIo.readInt16(this.arrayBuffer.buffer, index * 2 + this.offset, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object js_set(int index, Object c) {
/*  86 */     if (checkIndex(index)) {
/*  87 */       return Undefined.instance;
/*     */     }
/*  89 */     int val = Conversions.toInt16(c);
/*  90 */     ByteIo.writeInt16(this.arrayBuffer.buffer, index * 2 + this.offset, val, false);
/*  91 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Short get(int i) {
/*  97 */     if (checkIndex(i)) {
/*  98 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 100 */     return (Short)js_get(i);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Short set(int i, Short aByte) {
/* 106 */     if (checkIndex(i)) {
/* 107 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 109 */     return (Short)js_set(i, aByte);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\typedarrays\NativeInt16Array.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */