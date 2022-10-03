/*    */ package org.mozilla.javascript.commonjs.module;
/*    */ 
/*    */ import java.net.URI;
/*    */ import org.mozilla.javascript.Scriptable;
/*    */ import org.mozilla.javascript.TopLevel;
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
/*    */ public class ModuleScope
/*    */   extends TopLevel
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final URI uri;
/*    */   private final URI base;
/*    */   
/*    */   public ModuleScope(Scriptable prototype, URI uri, URI base) {
/* 25 */     this.uri = uri;
/* 26 */     this.base = base;
/* 27 */     setPrototype(prototype);
/* 28 */     cacheBuiltins();
/*    */   }
/*    */   
/*    */   public URI getUri() {
/* 32 */     return this.uri;
/*    */   }
/*    */   
/*    */   public URI getBase() {
/* 36 */     return this.base;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\commonjs\module\ModuleScope.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */