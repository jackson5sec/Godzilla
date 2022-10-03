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
/*     */ class SpecialRef
/*     */   extends Ref
/*     */ {
/*     */   static final long serialVersionUID = -7521596632456797847L;
/*     */   private static final int SPECIAL_NONE = 0;
/*     */   private static final int SPECIAL_PROTO = 1;
/*     */   private static final int SPECIAL_PARENT = 2;
/*     */   private Scriptable target;
/*     */   private int type;
/*     */   private String name;
/*     */   
/*     */   private SpecialRef(Scriptable target, int type, String name) {
/*  23 */     this.target = target;
/*  24 */     this.type = type;
/*  25 */     this.name = name;
/*     */   }
/*     */ 
/*     */   
/*     */   static Ref createSpecial(Context cx, Scriptable scope, Object object, String name) {
/*     */     int type;
/*  31 */     Scriptable target = ScriptRuntime.toObjectOrNull(cx, object, scope);
/*  32 */     if (target == null) {
/*  33 */       throw ScriptRuntime.undefReadError(object, name);
/*     */     }
/*     */ 
/*     */     
/*  37 */     if (name.equals("__proto__")) {
/*  38 */       type = 1;
/*  39 */     } else if (name.equals("__parent__")) {
/*  40 */       type = 2;
/*     */     } else {
/*  42 */       throw new IllegalArgumentException(name);
/*     */     } 
/*     */     
/*  45 */     if (!cx.hasFeature(5))
/*     */     {
/*  47 */       type = 0;
/*     */     }
/*     */     
/*  50 */     return new SpecialRef(target, type, name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object get(Context cx) {
/*  56 */     switch (this.type) {
/*     */       case 0:
/*  58 */         return ScriptRuntime.getObjectProp(this.target, this.name, cx);
/*     */       case 1:
/*  60 */         return this.target.getPrototype();
/*     */       case 2:
/*  62 */         return this.target.getParentScope();
/*     */     } 
/*  64 */     throw Kit.codeBug();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Object set(Context cx, Object value) {
/*  71 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object set(Context cx, Scriptable scope, Object value) {
/*     */     Scriptable obj;
/*  77 */     switch (this.type) {
/*     */       case 0:
/*  79 */         return ScriptRuntime.setObjectProp(this.target, this.name, value, cx);
/*     */       
/*     */       case 1:
/*     */       case 2:
/*  83 */         obj = ScriptRuntime.toObjectOrNull(cx, value, scope);
/*  84 */         if (obj != null) {
/*     */ 
/*     */           
/*  87 */           Scriptable search = obj;
/*     */           do {
/*  89 */             if (search == this.target) {
/*  90 */               throw Context.reportRuntimeError1("msg.cyclic.value", this.name);
/*     */             }
/*     */             
/*  93 */             if (this.type == 1) {
/*  94 */               search = search.getPrototype();
/*     */             } else {
/*  96 */               search = search.getParentScope();
/*     */             } 
/*  98 */           } while (search != null);
/*     */         } 
/* 100 */         if (this.type == 1) {
/* 101 */           this.target.setPrototype(obj);
/*     */         } else {
/* 103 */           this.target.setParentScope(obj);
/*     */         } 
/* 105 */         return obj;
/*     */     } 
/*     */     
/* 108 */     throw Kit.codeBug();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean has(Context cx) {
/* 115 */     if (this.type == 0) {
/* 116 */       return ScriptRuntime.hasObjectElem(this.target, this.name, cx);
/*     */     }
/* 118 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean delete(Context cx) {
/* 124 */     if (this.type == 0) {
/* 125 */       return ScriptRuntime.deleteObjectElem(this.target, this.name, cx);
/*     */     }
/* 127 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\SpecialRef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */