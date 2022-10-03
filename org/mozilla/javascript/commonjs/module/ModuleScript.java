/*    */ package org.mozilla.javascript.commonjs.module;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.net.URI;
/*    */ import org.mozilla.javascript.Script;
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
/*    */ public class ModuleScript
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final Script script;
/*    */   private final URI uri;
/*    */   private final URI base;
/*    */   
/*    */   public ModuleScript(Script script, URI uri, URI base) {
/* 34 */     this.script = script;
/* 35 */     this.uri = uri;
/* 36 */     this.base = base;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Script getScript() {
/* 44 */     return this.script;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public URI getUri() {
/* 52 */     return this.uri;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public URI getBase() {
/* 61 */     return this.base;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSandboxed() {
/* 70 */     return (this.base != null && this.uri != null && !this.base.relativize(this.uri).isAbsolute());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\commonjs\module\ModuleScript.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */