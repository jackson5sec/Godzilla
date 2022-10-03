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
/*     */ public class NativeUint8Array
/*     */   extends NativeTypedArrayView<Integer>
/*     */ {
/*     */   private static final long serialVersionUID = -3349419704390398895L;
/*     */   private static final String CLASS_NAME = "Uint8Array";
/*     */   
/*     */   public NativeUint8Array() {}
/*     */   
/*     */   public NativeUint8Array(NativeArrayBuffer ab, int off, int len) {
/*  32 */     super(ab, off, len, len);
/*     */   }
/*     */ 
/*     */   
/*     */   public NativeUint8Array(int len) {
/*  37 */     this(new NativeArrayBuffer(len), 0, len);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  43 */     return "Uint8Array";
/*     */   }
/*     */ 
/*     */   
/*     */   public static void init(Context cx, Scriptable scope, boolean sealed) {
/*  48 */     NativeUint8Array a = new NativeUint8Array();
/*  49 */     a.exportAsJSClass(4, scope, sealed);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected NativeTypedArrayView construct(NativeArrayBuffer ab, int off, int len) {
/*  55 */     return new NativeUint8Array(ab, off, len);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBytesPerElement() {
/*  61 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected NativeTypedArrayView realThis(Scriptable thisObj, IdFunctionObject f) {
/*  67 */     if (!(thisObj instanceof NativeUint8Array)) {
/*  68 */       throw incompatibleCallError(f);
/*     */     }
/*  70 */     return (NativeUint8Array)thisObj;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object js_get(int index) {
/*  76 */     if (checkIndex(index)) {
/*  77 */       return Undefined.instance;
/*     */     }
/*  79 */     return ByteIo.readUint8(this.arrayBuffer.buffer, index + this.offset);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object js_set(int index, Object c) {
/*  85 */     if (checkIndex(index)) {
/*  86 */       return Undefined.instance;
/*     */     }
/*  88 */     int val = Conversions.toUint8(c);
/*  89 */     ByteIo.writeUint8(this.arrayBuffer.buffer, index + this.offset, val);
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer get(int i) {
/*  96 */     if (checkIndex(i)) {
/*  97 */       throw new IndexOutOfBoundsException();
/*     */     }
/*  99 */     return (Integer)js_get(i);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer set(int i, Integer aByte) {
/* 105 */     if (checkIndex(i)) {
/* 106 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 108 */     return (Integer)js_set(i, aByte);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\typedarrays\NativeUint8Array.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */