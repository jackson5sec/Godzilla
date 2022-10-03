/*    */ package org.mozilla.javascript;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class NativeContinuation
/*    */   extends IdScriptableObject
/*    */   implements Function
/*    */ {
/*    */   static final long serialVersionUID = 1794167133757605367L;
/* 14 */   private static final Object FTAG = "Continuation";
/*    */   private Object implementation;
/*    */   private static final int Id_constructor = 1;
/*    */   private static final int MAX_PROTOTYPE_ID = 1;
/*    */   
/*    */   public static void init(Context cx, Scriptable scope, boolean sealed) {
/* 20 */     NativeContinuation obj = new NativeContinuation();
/* 21 */     obj.exportAsJSClass(1, scope, sealed);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getImplementation() {
/* 26 */     return this.implementation;
/*    */   }
/*    */ 
/*    */   
/*    */   public void initImplementation(Object implementation) {
/* 31 */     this.implementation = implementation;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 37 */     return "Continuation";
/*    */   }
/*    */ 
/*    */   
/*    */   public Scriptable construct(Context cx, Scriptable scope, Object[] args) {
/* 42 */     throw Context.reportRuntimeError("Direct call is not supported");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 48 */     return Interpreter.restartContinuation(this, cx, scope, args);
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean isContinuationConstructor(IdFunctionObject f) {
/* 53 */     if (f.hasTag(FTAG) && f.methodId() == 1) {
/* 54 */       return true;
/*    */     }
/* 56 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void initPrototypeId(int id) {
/*    */     String s;
/*    */     int arity;
/* 64 */     switch (id) { case 1:
/* 65 */         arity = 0; s = "constructor"; break;
/* 66 */       default: throw new IllegalArgumentException(String.valueOf(id)); }
/*    */     
/* 68 */     initPrototypeMethod(FTAG, id, s, arity);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object execIdCall(IdFunctionObject f, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 75 */     if (!f.hasTag(FTAG)) {
/* 76 */       return super.execIdCall(f, cx, scope, thisObj, args);
/*    */     }
/* 78 */     int id = f.methodId();
/* 79 */     switch (id) {
/*    */       case 1:
/* 81 */         throw Context.reportRuntimeError("Direct call is not supported");
/*    */     } 
/* 83 */     throw new IllegalArgumentException(String.valueOf(id));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int findPrototypeId(String s) {
/* 93 */     int id = 0; String X = null;
/* 94 */     if (s.length() == 11) { X = "constructor"; id = 1; }
/* 95 */      if (X != null && X != s && !X.equals(s)) id = 0;
/*    */ 
/*    */ 
/*    */     
/* 99 */     return id;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\NativeContinuation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */