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
/*     */ public class Delegator
/*     */   implements Function
/*     */ {
/*  28 */   protected Scriptable obj = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Delegator() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Delegator(Scriptable obj) {
/*  49 */     this.obj = obj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Delegator newInstance() {
/*     */     try {
/*  61 */       return (Delegator)getClass().newInstance();
/*  62 */     } catch (Exception ex) {
/*  63 */       throw Context.throwAsScriptRuntimeEx(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Scriptable getDelegee() {
/*  73 */     return this.obj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDelegee(Scriptable obj) {
/*  82 */     this.obj = obj;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*  88 */     return this.obj.getClassName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object get(String name, Scriptable start) {
/*  94 */     return this.obj.get(name, start);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object get(int index, Scriptable start) {
/* 100 */     return this.obj.get(index, start);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean has(String name, Scriptable start) {
/* 106 */     return this.obj.has(name, start);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean has(int index, Scriptable start) {
/* 112 */     return this.obj.has(index, start);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(String name, Scriptable start, Object value) {
/* 118 */     this.obj.put(name, start, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(int index, Scriptable start, Object value) {
/* 124 */     this.obj.put(index, start, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete(String name) {
/* 130 */     this.obj.delete(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void delete(int index) {
/* 136 */     this.obj.delete(index);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Scriptable getPrototype() {
/* 142 */     return this.obj.getPrototype();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPrototype(Scriptable prototype) {
/* 148 */     this.obj.setPrototype(prototype);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Scriptable getParentScope() {
/* 154 */     return this.obj.getParentScope();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParentScope(Scriptable parent) {
/* 160 */     this.obj.setParentScope(parent);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] getIds() {
/* 166 */     return this.obj.getIds();
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
/*     */   public Object getDefaultValue(Class<?> hint) {
/* 181 */     return (hint == null || hint == ScriptRuntime.ScriptableClass || hint == ScriptRuntime.FunctionClass) ? this : this.obj.getDefaultValue(hint);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasInstance(Scriptable instance) {
/* 190 */     return this.obj.hasInstance(instance);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 198 */     return ((Function)this.obj).call(cx, scope, thisObj, args);
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
/*     */   public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
/* 218 */     if (this.obj == null) {
/*     */       Scriptable delegee;
/*     */       
/* 221 */       Delegator n = newInstance();
/*     */       
/* 223 */       if (args.length == 0) {
/* 224 */         delegee = new NativeObject();
/*     */       } else {
/* 226 */         delegee = ScriptRuntime.toObject(cx, scope, args[0]);
/*     */       } 
/* 228 */       n.setDelegee(delegee);
/* 229 */       return n;
/*     */     } 
/*     */     
/* 232 */     return ((Function)this.obj).construct(cx, scope, args);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\Delegator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */