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
/*     */ public class NativeUint16Array
/*     */   extends NativeTypedArrayView<Integer>
/*     */ {
/*     */   private static final long serialVersionUID = 7700018949434240321L;
/*     */   private static final String CLASS_NAME = "Uint16Array";
/*     */   private static final int BYTES_PER_ELEMENT = 2;
/*     */   
/*     */   public NativeUint16Array() {}
/*     */   
/*     */   public NativeUint16Array(NativeArrayBuffer ab, int off, int len) {
/*  33 */     super(ab, off, len, len * 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public NativeUint16Array(int len) {
/*  38 */     this(new NativeArrayBuffer(len * 2), 0, len);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  44 */     return "Uint16Array";
/*     */   }
/*     */ 
/*     */   
/*     */   public static void init(Context cx, Scriptable scope, boolean sealed) {
/*  49 */     NativeUint16Array a = new NativeUint16Array();
/*  50 */     a.exportAsJSClass(4, scope, sealed);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected NativeTypedArrayView construct(NativeArrayBuffer ab, int off, int len) {
/*  56 */     return new NativeUint16Array(ab, off, len);
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
/*  68 */     if (!(thisObj instanceof NativeUint16Array)) {
/*  69 */       throw incompatibleCallError(f);
/*     */     }
/*  71 */     return (NativeUint16Array)thisObj;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object js_get(int index) {
/*  77 */     if (checkIndex(index)) {
/*  78 */       return Undefined.instance;
/*     */     }
/*  80 */     return ByteIo.readUint16(this.arrayBuffer.buffer, index * 2 + this.offset, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object js_set(int index, Object c) {
/*  86 */     if (checkIndex(index)) {
/*  87 */       return Undefined.instance;
/*     */     }
/*  89 */     int val = Conversions.toUint16(c);
/*  90 */     ByteIo.writeUint16(this.arrayBuffer.buffer, index * 2 + this.offset, val, false);
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


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\typedarrays\NativeUint16Array.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */