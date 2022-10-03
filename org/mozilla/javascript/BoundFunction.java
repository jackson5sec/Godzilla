/*    */ package org.mozilla.javascript;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BoundFunction
/*    */   extends BaseFunction
/*    */ {
/*    */   static final long serialVersionUID = 2118137342826470729L;
/*    */   private final Callable targetFunction;
/*    */   private final Scriptable boundThis;
/*    */   private final Object[] boundArgs;
/*    */   private final int length;
/*    */   
/*    */   public BoundFunction(Context cx, Scriptable scope, Callable targetFunction, Scriptable boundThis, Object[] boundArgs) {
/* 26 */     this.targetFunction = targetFunction;
/* 27 */     this.boundThis = boundThis;
/* 28 */     this.boundArgs = boundArgs;
/* 29 */     if (targetFunction instanceof BaseFunction) {
/* 30 */       this.length = Math.max(0, ((BaseFunction)targetFunction).getLength() - boundArgs.length);
/*    */     } else {
/* 32 */       this.length = 0;
/*    */     } 
/*    */     
/* 35 */     ScriptRuntime.setFunctionProtoAndParent(this, scope);
/*    */     
/* 37 */     Function thrower = ScriptRuntime.typeErrorThrower(cx);
/* 38 */     NativeObject throwing = new NativeObject();
/* 39 */     throwing.put("get", throwing, thrower);
/* 40 */     throwing.put("set", throwing, thrower);
/* 41 */     throwing.put("enumerable", throwing, Boolean.valueOf(false));
/* 42 */     throwing.put("configurable", throwing, Boolean.valueOf(false));
/* 43 */     throwing.preventExtensions();
/*    */     
/* 45 */     defineOwnProperty(cx, "caller", throwing, false);
/* 46 */     defineOwnProperty(cx, "arguments", throwing, false);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] extraArgs) {
/* 52 */     Scriptable callThis = (this.boundThis != null) ? this.boundThis : ScriptRuntime.getTopCallScope(cx);
/* 53 */     return this.targetFunction.call(cx, scope, callThis, concat(this.boundArgs, extraArgs));
/*    */   }
/*    */ 
/*    */   
/*    */   public Scriptable construct(Context cx, Scriptable scope, Object[] extraArgs) {
/* 58 */     if (this.targetFunction instanceof Function) {
/* 59 */       return ((Function)this.targetFunction).construct(cx, scope, concat(this.boundArgs, extraArgs));
/*    */     }
/* 61 */     throw ScriptRuntime.typeError0("msg.not.ctor");
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasInstance(Scriptable instance) {
/* 66 */     if (this.targetFunction instanceof Function) {
/* 67 */       return ((Function)this.targetFunction).hasInstance(instance);
/*    */     }
/* 69 */     throw ScriptRuntime.typeError0("msg.not.ctor");
/*    */   }
/*    */ 
/*    */   
/*    */   public int getLength() {
/* 74 */     return this.length;
/*    */   }
/*    */   
/*    */   private Object[] concat(Object[] first, Object[] second) {
/* 78 */     Object[] args = new Object[first.length + second.length];
/* 79 */     System.arraycopy(first, 0, args, 0, first.length);
/* 80 */     System.arraycopy(second, 0, args, first.length, second.length);
/* 81 */     return args;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\BoundFunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */