/*     */ package org.mozilla.javascript;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IdFunctionObject
/*     */   extends BaseFunction
/*     */ {
/*     */   static final long serialVersionUID = -5332312783643935019L;
/*     */   private final IdFunctionCall idcall;
/*     */   private final Object tag;
/*     */   private final int methodId;
/*     */   private int arity;
/*     */   private boolean useCallAsConstructor;
/*     */   private String functionName;
/*     */   
/*     */   public IdFunctionObject(IdFunctionCall idcall, Object tag, int id, int arity) {
/*  18 */     if (arity < 0) {
/*  19 */       throw new IllegalArgumentException();
/*     */     }
/*  21 */     this.idcall = idcall;
/*  22 */     this.tag = tag;
/*  23 */     this.methodId = id;
/*  24 */     this.arity = arity;
/*  25 */     if (arity < 0) throw new IllegalArgumentException();
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public IdFunctionObject(IdFunctionCall idcall, Object tag, int id, String name, int arity, Scriptable scope) {
/*  31 */     super(scope, (Scriptable)null);
/*     */     
/*  33 */     if (arity < 0)
/*  34 */       throw new IllegalArgumentException(); 
/*  35 */     if (name == null) {
/*  36 */       throw new IllegalArgumentException();
/*     */     }
/*  38 */     this.idcall = idcall;
/*  39 */     this.tag = tag;
/*  40 */     this.methodId = id;
/*  41 */     this.arity = arity;
/*  42 */     this.functionName = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initFunction(String name, Scriptable scope) {
/*  47 */     if (name == null) throw new IllegalArgumentException(); 
/*  48 */     if (scope == null) throw new IllegalArgumentException(); 
/*  49 */     this.functionName = name;
/*  50 */     setParentScope(scope);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasTag(Object tag) {
/*  55 */     return (tag == null) ? ((this.tag == null)) : tag.equals(this.tag);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int methodId() {
/*  60 */     return this.methodId;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void markAsConstructor(Scriptable prototypeProperty) {
/*  65 */     this.useCallAsConstructor = true;
/*  66 */     setImmunePrototypeProperty(prototypeProperty);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void addAsProperty(Scriptable target) {
/*  71 */     ScriptableObject.defineProperty(target, this.functionName, this, 2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void exportAsScopeProperty() {
/*  77 */     addAsProperty(getParentScope());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Scriptable getPrototype() {
/*  85 */     Scriptable proto = super.getPrototype();
/*  86 */     if (proto == null) {
/*  87 */       proto = getFunctionPrototype(getParentScope());
/*  88 */       setPrototype(proto);
/*     */     } 
/*  90 */     return proto;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/*  97 */     return this.idcall.execIdCall(this, cx, scope, thisObj, args);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Scriptable createObject(Context cx, Scriptable scope) {
/* 103 */     if (this.useCallAsConstructor) {
/* 104 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     throw ScriptRuntime.typeError1("msg.not.ctor", this.functionName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   String decompile(int indent, int flags) {
/* 116 */     StringBuilder sb = new StringBuilder();
/* 117 */     boolean justbody = (0 != (flags & 0x1));
/* 118 */     if (!justbody) {
/* 119 */       sb.append("function ");
/* 120 */       sb.append(getFunctionName());
/* 121 */       sb.append("() { ");
/*     */     } 
/* 123 */     sb.append("[native code for ");
/* 124 */     if (this.idcall instanceof Scriptable) {
/* 125 */       Scriptable sobj = (Scriptable)this.idcall;
/* 126 */       sb.append(sobj.getClassName());
/* 127 */       sb.append('.');
/*     */     } 
/* 129 */     sb.append(getFunctionName());
/* 130 */     sb.append(", arity=");
/* 131 */     sb.append(getArity());
/* 132 */     sb.append(justbody ? "]\n" : "] }\n");
/* 133 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getArity() {
/* 139 */     return this.arity;
/*     */   }
/*     */   
/*     */   public int getLength() {
/* 143 */     return getArity();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFunctionName() {
/* 148 */     return (this.functionName == null) ? "" : this.functionName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final RuntimeException unknown() {
/* 154 */     return new IllegalArgumentException("BAD FUNCTION ID=" + this.methodId + " MASTER=" + this.idcall);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\IdFunctionObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */