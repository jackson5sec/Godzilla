/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ public class NativeWith
/*     */   implements Scriptable, IdFunctionCall, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   static void init(Scriptable scope, boolean sealed) {
/*  23 */     NativeWith obj = new NativeWith();
/*     */     
/*  25 */     obj.setParentScope(scope);
/*  26 */     obj.setPrototype(ScriptableObject.getObjectPrototype(scope));
/*     */     
/*  28 */     IdFunctionObject ctor = new IdFunctionObject(obj, FTAG, 1, "With", 0, scope);
/*     */     
/*  30 */     ctor.markAsConstructor(obj);
/*  31 */     if (sealed) {
/*  32 */       ctor.sealObject();
/*     */     }
/*  34 */     ctor.exportAsScopeProperty();
/*     */   }
/*     */ 
/*     */   
/*     */   private NativeWith() {}
/*     */   
/*     */   protected NativeWith(Scriptable parent, Scriptable prototype) {
/*  41 */     this.parent = parent;
/*  42 */     this.prototype = prototype;
/*     */   }
/*     */   
/*     */   public String getClassName() {
/*  46 */     return "With";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean has(String id, Scriptable start) {
/*  51 */     return this.prototype.has(id, this.prototype);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean has(int index, Scriptable start) {
/*  56 */     return this.prototype.has(index, this.prototype);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object get(String id, Scriptable start) {
/*  61 */     if (start == this)
/*  62 */       start = this.prototype; 
/*  63 */     return this.prototype.get(id, start);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object get(int index, Scriptable start) {
/*  68 */     if (start == this)
/*  69 */       start = this.prototype; 
/*  70 */     return this.prototype.get(index, start);
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(String id, Scriptable start, Object value) {
/*  75 */     if (start == this)
/*  76 */       start = this.prototype; 
/*  77 */     this.prototype.put(id, start, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void put(int index, Scriptable start, Object value) {
/*  82 */     if (start == this)
/*  83 */       start = this.prototype; 
/*  84 */     this.prototype.put(index, start, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void delete(String id) {
/*  89 */     this.prototype.delete(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public void delete(int index) {
/*  94 */     this.prototype.delete(index);
/*     */   }
/*     */   
/*     */   public Scriptable getPrototype() {
/*  98 */     return this.prototype;
/*     */   }
/*     */   
/*     */   public void setPrototype(Scriptable prototype) {
/* 102 */     this.prototype = prototype;
/*     */   }
/*     */   
/*     */   public Scriptable getParentScope() {
/* 106 */     return this.parent;
/*     */   }
/*     */   
/*     */   public void setParentScope(Scriptable parent) {
/* 110 */     this.parent = parent;
/*     */   }
/*     */   
/*     */   public Object[] getIds() {
/* 114 */     return this.prototype.getIds();
/*     */   }
/*     */   
/*     */   public Object getDefaultValue(Class<?> typeHint) {
/* 118 */     return this.prototype.getDefaultValue(typeHint);
/*     */   }
/*     */   
/*     */   public boolean hasInstance(Scriptable value) {
/* 122 */     return this.prototype.hasInstance(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object updateDotQuery(boolean value) {
/* 131 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 137 */     if (f.hasTag(FTAG) && 
/* 138 */       f.methodId() == 1) {
/* 139 */       throw Context.reportRuntimeError1("msg.cant.call.indirect", "With");
/*     */     }
/*     */     
/* 142 */     throw f.unknown();
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean isWithFunction(Object functionObj) {
/* 147 */     if (functionObj instanceof IdFunctionObject) {
/* 148 */       IdFunctionObject f = (IdFunctionObject)functionObj;
/* 149 */       return (f.hasTag(FTAG) && f.methodId() == 1);
/*     */     } 
/* 151 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   static Object newWithSpecial(Context cx, Scriptable scope, Object[] args) {
/* 156 */     ScriptRuntime.checkDeprecated(cx, "With");
/* 157 */     scope = ScriptableObject.getTopLevelScope(scope);
/* 158 */     NativeWith thisObj = new NativeWith();
/* 159 */     thisObj.setPrototype((args.length == 0) ? ScriptableObject.getObjectPrototype(scope) : ScriptRuntime.toObject(cx, scope, args[0]));
/*     */ 
/*     */     
/* 162 */     thisObj.setParentScope(scope);
/* 163 */     return thisObj;
/*     */   }
/*     */   
/* 166 */   private static final Object FTAG = "With";
/*     */   private static final int Id_constructor = 1;
/*     */   protected Scriptable prototype;
/*     */   protected Scriptable parent;
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\NativeWith.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */