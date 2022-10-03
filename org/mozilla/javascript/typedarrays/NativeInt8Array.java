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
/*     */ public class NativeInt8Array
/*     */   extends NativeTypedArrayView<Byte>
/*     */ {
/*     */   private static final long serialVersionUID = -3349419704390398895L;
/*     */   private static final String CLASS_NAME = "Int8Array";
/*     */   
/*     */   public NativeInt8Array() {}
/*     */   
/*     */   public NativeInt8Array(NativeArrayBuffer ab, int off, int len) {
/*  32 */     super(ab, off, len, len);
/*     */   }
/*     */ 
/*     */   
/*     */   public NativeInt8Array(int len) {
/*  37 */     this(new NativeArrayBuffer(len), 0, len);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  43 */     return "Int8Array";
/*     */   }
/*     */ 
/*     */   
/*     */   public static void init(Context cx, Scriptable scope, boolean sealed) {
/*  48 */     NativeInt8Array a = new NativeInt8Array();
/*  49 */     a.exportAsJSClass(4, scope, sealed);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected NativeTypedArrayView construct(NativeArrayBuffer ab, int off, int len) {
/*  55 */     return new NativeInt8Array(ab, off, len);
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
/*  67 */     if (!(thisObj instanceof NativeInt8Array)) {
/*  68 */       throw incompatibleCallError(f);
/*     */     }
/*  70 */     return (NativeInt8Array)thisObj;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object js_get(int index) {
/*  76 */     if (checkIndex(index)) {
/*  77 */       return Undefined.instance;
/*     */     }
/*  79 */     return ByteIo.readInt8(this.arrayBuffer.buffer, index + this.offset);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object js_set(int index, Object c) {
/*  85 */     if (checkIndex(index)) {
/*  86 */       return Undefined.instance;
/*     */     }
/*  88 */     int val = Conversions.toInt8(c);
/*  89 */     ByteIo.writeInt8(this.arrayBuffer.buffer, index + this.offset, val);
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Byte get(int i) {
/*  98 */     if (checkIndex(i)) {
/*  99 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 101 */     return (Byte)js_get(i);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Byte set(int i, Byte aByte) {
/* 107 */     if (checkIndex(i)) {
/* 108 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 110 */     return (Byte)js_set(i, aByte);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\typedarrays\NativeInt8Array.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */