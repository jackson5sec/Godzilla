/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.util.EnumMap;
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
/*     */ public class TopLevel
/*     */   extends IdScriptableObject
/*     */ {
/*     */   static final long serialVersionUID = -4648046356662472260L;
/*     */   private EnumMap<Builtins, BaseFunction> ctors;
/*     */   private EnumMap<NativeErrors, BaseFunction> errors;
/*     */   
/*     */   public enum Builtins
/*     */   {
/*  47 */     Object,
/*     */     
/*  49 */     Array,
/*     */     
/*  51 */     Function,
/*     */     
/*  53 */     String,
/*     */     
/*  55 */     Number,
/*     */     
/*  57 */     Boolean,
/*     */     
/*  59 */     RegExp,
/*     */     
/*  61 */     Error;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   enum NativeErrors
/*     */   {
/*  69 */     Error,
/*     */     
/*  71 */     EvalError,
/*     */     
/*  73 */     RangeError,
/*     */     
/*  75 */     ReferenceError,
/*     */     
/*  77 */     SyntaxError,
/*     */     
/*  79 */     TypeError,
/*     */     
/*  81 */     URIError,
/*     */     
/*  83 */     InternalError,
/*     */     
/*  85 */     JavaException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  93 */     return "global";
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
/*     */   public void cacheBuiltins() {
/* 105 */     this.ctors = new EnumMap<Builtins, BaseFunction>(Builtins.class);
/* 106 */     for (Builtins builtin : Builtins.values()) {
/* 107 */       Object value = ScriptableObject.getProperty(this, builtin.name());
/* 108 */       if (value instanceof BaseFunction) {
/* 109 */         this.ctors.put(builtin, (BaseFunction)value);
/*     */       }
/*     */     } 
/* 112 */     this.errors = new EnumMap<NativeErrors, BaseFunction>(NativeErrors.class);
/* 113 */     for (NativeErrors error : NativeErrors.values()) {
/* 114 */       Object value = ScriptableObject.getProperty(this, error.name());
/* 115 */       if (value instanceof BaseFunction) {
/* 116 */         this.errors.put(error, (BaseFunction)value);
/*     */       }
/*     */     } 
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
/*     */   public static Function getBuiltinCtor(Context cx, Scriptable scope, Builtins type) {
/* 136 */     assert scope.getParentScope() == null;
/* 137 */     if (scope instanceof TopLevel) {
/* 138 */       Function result = ((TopLevel)scope).getBuiltinCtor(type);
/* 139 */       if (result != null) {
/* 140 */         return result;
/*     */       }
/*     */     } 
/*     */     
/* 144 */     return ScriptRuntime.getExistingCtor(cx, scope, type.name());
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
/*     */   static Function getNativeErrorCtor(Context cx, Scriptable scope, NativeErrors type) {
/* 161 */     assert scope.getParentScope() == null;
/* 162 */     if (scope instanceof TopLevel) {
/* 163 */       Function result = ((TopLevel)scope).getNativeErrorCtor(type);
/* 164 */       if (result != null) {
/* 165 */         return result;
/*     */       }
/*     */     } 
/*     */     
/* 169 */     return ScriptRuntime.getExistingCtor(cx, scope, type.name());
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
/*     */   public static Scriptable getBuiltinPrototype(Scriptable scope, Builtins type) {
/* 185 */     assert scope.getParentScope() == null;
/* 186 */     if (scope instanceof TopLevel) {
/* 187 */       Scriptable result = ((TopLevel)scope).getBuiltinPrototype(type);
/*     */       
/* 189 */       if (result != null) {
/* 190 */         return result;
/*     */       }
/*     */     } 
/*     */     
/* 194 */     return ScriptableObject.getClassPrototype(scope, type.name());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseFunction getBuiltinCtor(Builtins type) {
/* 205 */     return (this.ctors != null) ? this.ctors.get(type) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   BaseFunction getNativeErrorCtor(NativeErrors type) {
/* 216 */     return (this.errors != null) ? this.errors.get(type) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Scriptable getBuiltinPrototype(Builtins type) {
/* 227 */     BaseFunction func = getBuiltinCtor(type);
/* 228 */     Object proto = (func != null) ? func.getPrototypeProperty() : null;
/* 229 */     return (proto instanceof Scriptable) ? (Scriptable)proto : null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\TopLevel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */