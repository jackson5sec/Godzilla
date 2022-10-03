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
/*     */ final class NativeBoolean
/*     */   extends IdScriptableObject
/*     */ {
/*     */   static final long serialVersionUID = -3716996899943880933L;
/*  18 */   private static final Object BOOLEAN_TAG = "Boolean"; private static final int Id_constructor = 1; private static final int Id_toString = 2;
/*     */   private static final int Id_toSource = 3;
/*     */   
/*     */   static void init(Scriptable scope, boolean sealed) {
/*  22 */     NativeBoolean obj = new NativeBoolean(false);
/*  23 */     obj.exportAsJSClass(4, scope, sealed);
/*     */   }
/*     */   private static final int Id_valueOf = 4; private static final int MAX_PROTOTYPE_ID = 4; private boolean booleanValue;
/*     */   
/*     */   NativeBoolean(boolean b) {
/*  28 */     this.booleanValue = b;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  34 */     return "Boolean";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getDefaultValue(Class<?> typeHint) {
/*  41 */     if (typeHint == ScriptRuntime.BooleanClass)
/*  42 */       return ScriptRuntime.wrapBoolean(this.booleanValue); 
/*  43 */     return super.getDefaultValue(typeHint);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initPrototypeId(int id) {
/*     */     String s;
/*     */     int arity;
/*  51 */     switch (id) { case 1:
/*  52 */         arity = 1; s = "constructor"; break;
/*  53 */       case 2: arity = 0; s = "toString"; break;
/*  54 */       case 3: arity = 0; s = "toSource"; break;
/*  55 */       case 4: arity = 0; s = "valueOf"; break;
/*  56 */       default: throw new IllegalArgumentException(String.valueOf(id)); }
/*     */     
/*  58 */     initPrototypeMethod(BOOLEAN_TAG, id, s, arity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/*  65 */     if (!f.hasTag(BOOLEAN_TAG)) {
/*  66 */       return super.execIdCall(f, cx, scope, thisObj, args);
/*     */     }
/*  68 */     int id = f.methodId();
/*     */     
/*  70 */     if (id == 1) {
/*     */       boolean b;
/*  72 */       if (args.length == 0) {
/*  73 */         b = false;
/*     */       } else {
/*  75 */         b = (args[0] instanceof ScriptableObject && ((ScriptableObject)args[0]).avoidObjectDetection()) ? true : ScriptRuntime.toBoolean(args[0]);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/*  80 */       if (thisObj == null)
/*     */       {
/*  82 */         return new NativeBoolean(b);
/*     */       }
/*     */       
/*  85 */       return ScriptRuntime.wrapBoolean(b);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  90 */     if (!(thisObj instanceof NativeBoolean))
/*  91 */       throw incompatibleCallError(f); 
/*  92 */     boolean value = ((NativeBoolean)thisObj).booleanValue;
/*     */     
/*  94 */     switch (id) {
/*     */       
/*     */       case 2:
/*  97 */         return value ? "true" : "false";
/*     */       
/*     */       case 3:
/* 100 */         return value ? "(new Boolean(true))" : "(new Boolean(false))";
/*     */       
/*     */       case 4:
/* 103 */         return ScriptRuntime.wrapBoolean(value);
/*     */     } 
/* 105 */     throw new IllegalArgumentException(String.valueOf(id));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int findPrototypeId(String s) {
/* 115 */     int id = 0; String X = null;
/* 116 */     int s_length = s.length();
/* 117 */     if (s_length == 7) { X = "valueOf"; id = 4; }
/* 118 */     else if (s_length == 8)
/* 119 */     { int c = s.charAt(3);
/* 120 */       if (c == 111) { X = "toSource"; id = 3; }
/* 121 */       else if (c == 116) { X = "toString"; id = 2; }
/*     */        }
/* 123 */     else if (s_length == 11) { X = "constructor"; id = 1; }
/* 124 */      if (X != null && X != s && !X.equals(s)) id = 0;
/*     */ 
/*     */ 
/*     */     
/* 128 */     return id;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\NativeBoolean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */