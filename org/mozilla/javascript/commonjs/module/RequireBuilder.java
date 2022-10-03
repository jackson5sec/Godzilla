/*    */ package org.mozilla.javascript.commonjs.module;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.mozilla.javascript.Context;
/*    */ import org.mozilla.javascript.Script;
/*    */ import org.mozilla.javascript.Scriptable;
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
/*    */ public class RequireBuilder
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private boolean sandboxed = true;
/*    */   private ModuleScriptProvider moduleScriptProvider;
/*    */   private Script preExec;
/*    */   private Script postExec;
/*    */   
/*    */   public RequireBuilder setModuleScriptProvider(ModuleScriptProvider moduleScriptProvider) {
/* 42 */     this.moduleScriptProvider = moduleScriptProvider;
/* 43 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RequireBuilder setPostExec(Script postExec) {
/* 53 */     this.postExec = postExec;
/* 54 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RequireBuilder setPreExec(Script preExec) {
/* 64 */     this.preExec = preExec;
/* 65 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RequireBuilder setSandboxed(boolean sandboxed) {
/* 77 */     this.sandboxed = sandboxed;
/* 78 */     return this;
/*    */   }
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
/*    */   public Require createRequire(Context cx, Scriptable globalScope) {
/* 91 */     return new Require(cx, globalScope, this.moduleScriptProvider, this.preExec, this.postExec, this.sandboxed);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\commonjs\module\RequireBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */