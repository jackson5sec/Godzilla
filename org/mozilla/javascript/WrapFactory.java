/*     */ package org.mozilla.javascript;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WrapFactory
/*     */ {
/*     */   public Object wrap(Context cx, Scriptable scope, Object obj, Class<?> staticType) {
/*  47 */     if (obj == null || obj == Undefined.instance || obj instanceof Scriptable)
/*     */     {
/*     */       
/*  50 */       return obj;
/*     */     }
/*  52 */     if (staticType != null && staticType.isPrimitive()) {
/*  53 */       if (staticType == void.class)
/*  54 */         return Undefined.instance; 
/*  55 */       if (staticType == char.class)
/*  56 */         return Integer.valueOf(((Character)obj).charValue()); 
/*  57 */       return obj;
/*     */     } 
/*  59 */     if (!isJavaPrimitiveWrap()) {
/*  60 */       if (obj instanceof String || obj instanceof Number || obj instanceof Boolean)
/*     */       {
/*     */         
/*  63 */         return obj; } 
/*  64 */       if (obj instanceof Character) {
/*  65 */         return String.valueOf(((Character)obj).charValue());
/*     */       }
/*     */     } 
/*  68 */     Class<?> cls = obj.getClass();
/*  69 */     if (cls.isArray()) {
/*  70 */       return NativeJavaArray.wrap(scope, obj);
/*     */     }
/*  72 */     return wrapAsJavaObject(cx, scope, obj, staticType);
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
/*     */   public Scriptable wrapNewObject(Context cx, Scriptable scope, Object obj) {
/*  84 */     if (obj instanceof Scriptable) {
/*  85 */       return (Scriptable)obj;
/*     */     }
/*  87 */     Class<?> cls = obj.getClass();
/*  88 */     if (cls.isArray()) {
/*  89 */       return NativeJavaArray.wrap(scope, obj);
/*     */     }
/*  91 */     return wrapAsJavaObject(cx, scope, obj, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Scriptable wrapAsJavaObject(Context cx, Scriptable scope, Object javaObject, Class<?> staticType) {
/* 115 */     return new NativeJavaObject(scope, javaObject, staticType);
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
/*     */   public Scriptable wrapJavaClass(Context cx, Scriptable scope, Class<?> javaClass) {
/* 134 */     return new NativeJavaClass(scope, javaClass);
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
/*     */   public final boolean isJavaPrimitiveWrap() {
/* 150 */     return this.javaPrimitiveWrap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setJavaPrimitiveWrap(boolean value) {
/* 158 */     Context cx = Context.getCurrentContext();
/* 159 */     if (cx != null && cx.isSealed()) {
/* 160 */       Context.onSealedMutation();
/*     */     }
/* 162 */     this.javaPrimitiveWrap = value;
/*     */   }
/*     */   
/*     */   private boolean javaPrimitiveWrap = true;
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\WrapFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */