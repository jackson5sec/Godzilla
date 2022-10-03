/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class NativeIterator
/*     */   extends IdScriptableObject
/*     */ {
/*     */   private static final long serialVersionUID = -4136968203581667681L;
/*     */   private static final String STOP_ITERATION = "StopIteration";
/*     */   public static final String ITERATOR_PROPERTY_NAME = "__iterator__";
/*  19 */   private static final Object ITERATOR_TAG = "Iterator";
/*     */   private static final int Id_constructor = 1;
/*     */   
/*     */   static void init(ScriptableObject scope, boolean sealed) {
/*  23 */     NativeIterator iterator = new NativeIterator();
/*  24 */     iterator.exportAsJSClass(3, scope, sealed);
/*     */ 
/*     */     
/*  27 */     NativeGenerator.init(scope, sealed);
/*     */ 
/*     */     
/*  30 */     NativeObject obj = new StopIteration();
/*  31 */     obj.setPrototype(getObjectPrototype(scope));
/*  32 */     obj.setParentScope(scope);
/*  33 */     if (sealed) obj.sealObject(); 
/*  34 */     ScriptableObject.defineProperty(scope, "StopIteration", obj, 2);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  39 */     scope.associateValue(ITERATOR_TAG, obj);
/*     */   }
/*     */   private static final int Id_next = 2;
/*     */   private static final int Id___iterator__ = 3;
/*     */   private static final int MAX_PROTOTYPE_ID = 3;
/*     */   private Object objectIterator;
/*     */   
/*     */   private NativeIterator() {}
/*     */   
/*     */   private NativeIterator(Object objectIterator) {
/*  49 */     this.objectIterator = objectIterator;
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
/*     */   public static Object getStopIterationObject(Scriptable scope) {
/*  61 */     Scriptable top = ScriptableObject.getTopLevelScope(scope);
/*  62 */     return ScriptableObject.getTopScopeValue(top, ITERATOR_TAG);
/*     */   }
/*     */ 
/*     */   
/*     */   static class StopIteration
/*     */     extends NativeObject
/*     */   {
/*     */     private static final long serialVersionUID = 2485151085722377663L;
/*     */ 
/*     */     
/*     */     public String getClassName() {
/*  73 */       return "StopIteration";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasInstance(Scriptable instance) {
/*  81 */       return instance instanceof StopIteration;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  87 */     return "Iterator";
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initPrototypeId(int id) {
/*     */     String s;
/*     */     int arity;
/*  94 */     switch (id) { case 1:
/*  95 */         arity = 2; s = "constructor"; break;
/*  96 */       case 2: arity = 0; s = "next"; break;
/*  97 */       case 3: arity = 1; s = "__iterator__"; break;
/*  98 */       default: throw new IllegalArgumentException(String.valueOf(id)); }
/*     */     
/* 100 */     initPrototypeMethod(ITERATOR_TAG, id, s, arity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 107 */     if (!f.hasTag(ITERATOR_TAG)) {
/* 108 */       return super.execIdCall(f, cx, scope, thisObj, args);
/*     */     }
/* 110 */     int id = f.methodId();
/*     */     
/* 112 */     if (id == 1) {
/* 113 */       return jsConstructor(cx, scope, thisObj, args);
/*     */     }
/*     */     
/* 116 */     if (!(thisObj instanceof NativeIterator)) {
/* 117 */       throw incompatibleCallError(f);
/*     */     }
/* 119 */     NativeIterator iterator = (NativeIterator)thisObj;
/*     */     
/* 121 */     switch (id) {
/*     */       
/*     */       case 2:
/* 124 */         return iterator.next(cx, scope);
/*     */ 
/*     */       
/*     */       case 3:
/* 128 */         return thisObj;
/*     */     } 
/*     */     
/* 131 */     throw new IllegalArgumentException(String.valueOf(id));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object jsConstructor(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 139 */     if (args.length == 0 || args[0] == null || args[0] == Undefined.instance) {
/*     */ 
/*     */       
/* 142 */       Object argument = (args.length == 0) ? Undefined.instance : args[0];
/* 143 */       throw ScriptRuntime.typeError1("msg.no.properties", ScriptRuntime.toString(argument));
/*     */     } 
/*     */     
/* 146 */     Scriptable obj = ScriptRuntime.toObject(cx, scope, args[0]);
/* 147 */     boolean keyOnly = (args.length > 1 && ScriptRuntime.toBoolean(args[1]));
/* 148 */     if (thisObj != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 154 */       Iterator<?> iterator = VMBridge.instance.getJavaIterator(cx, scope, obj);
/*     */       
/* 156 */       if (iterator != null) {
/* 157 */         scope = ScriptableObject.getTopLevelScope(scope);
/* 158 */         return cx.getWrapFactory().wrap(cx, scope, new WrappedJavaIterator(iterator, scope), WrappedJavaIterator.class);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 164 */       Scriptable jsIterator = ScriptRuntime.toIterator(cx, scope, obj, keyOnly);
/*     */       
/* 166 */       if (jsIterator != null) {
/* 167 */         return jsIterator;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 173 */     Object objectIterator = ScriptRuntime.enumInit(obj, cx, scope, keyOnly ? 3 : 5);
/*     */ 
/*     */     
/* 176 */     ScriptRuntime.setEnumNumbers(objectIterator, true);
/* 177 */     NativeIterator result = new NativeIterator(objectIterator);
/* 178 */     result.setPrototype(ScriptableObject.getClassPrototype(scope, result.getClassName()));
/*     */     
/* 180 */     result.setParentScope(scope);
/* 181 */     return result;
/*     */   }
/*     */   
/*     */   private Object next(Context cx, Scriptable scope) {
/* 185 */     Boolean b = ScriptRuntime.enumNext(this.objectIterator);
/* 186 */     if (!b.booleanValue())
/*     */     {
/* 188 */       throw new JavaScriptException(getStopIterationObject(scope), null, 0);
/*     */     }
/*     */     
/* 191 */     return ScriptRuntime.enumId(this.objectIterator, cx);
/*     */   }
/*     */   public static class WrappedJavaIterator { private Iterator<?> iterator;
/*     */     private Scriptable scope;
/*     */     
/*     */     WrappedJavaIterator(Iterator<?> iterator, Scriptable scope) {
/* 197 */       this.iterator = iterator;
/* 198 */       this.scope = scope;
/*     */     }
/*     */     
/*     */     public Object next() {
/* 202 */       if (!this.iterator.hasNext())
/*     */       {
/* 204 */         throw new JavaScriptException(NativeIterator.getStopIterationObject(this.scope), null, 0);
/*     */       }
/*     */       
/* 207 */       return this.iterator.next();
/*     */     }
/*     */     
/*     */     public Object __iterator__(boolean b) {
/* 211 */       return this;
/*     */     } }
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
/*     */   protected int findPrototypeId(String s) {
/* 224 */     int id = 0; String X = null;
/* 225 */     int s_length = s.length();
/* 226 */     if (s_length == 4) { X = "next"; id = 2; }
/* 227 */     else if (s_length == 11) { X = "constructor"; id = 1; }
/* 228 */     else if (s_length == 12) { X = "__iterator__"; id = 3; }
/* 229 */      if (X != null && X != s && !X.equals(s)) id = 0;
/*     */ 
/*     */ 
/*     */     
/* 233 */     return id;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\NativeIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */