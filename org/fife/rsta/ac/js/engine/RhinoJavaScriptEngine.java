/*    */ package org.fife.rsta.ac.js.engine;
/*    */ 
/*    */ import org.fife.rsta.ac.js.SourceCompletionProvider;
/*    */ import org.fife.rsta.ac.js.ast.TypeDeclarationOptions;
/*    */ import org.fife.rsta.ac.js.ast.jsType.JavaScriptTypesFactory;
/*    */ import org.fife.rsta.ac.js.ast.jsType.RhinoJavaScriptTypesFactory;
/*    */ import org.fife.rsta.ac.js.ast.parser.JavaScriptParser;
/*    */ import org.fife.rsta.ac.js.ast.parser.RhinoJavaScriptAstParser;
/*    */ import org.fife.rsta.ac.js.resolver.JSR223JavaScriptCompletionResolver;
/*    */ import org.fife.rsta.ac.js.resolver.JavaScriptResolver;
/*    */ 
/*    */ 
/*    */ public class RhinoJavaScriptEngine
/*    */   extends JavaScriptEngine
/*    */ {
/*    */   public static final String RHINO_ENGINE = "RHINO";
/*    */   
/*    */   public JavaScriptResolver getJavaScriptResolver(SourceCompletionProvider provider) {
/* 19 */     return (JavaScriptResolver)new JSR223JavaScriptCompletionResolver(provider);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JavaScriptTypesFactory getJavaScriptTypesFactory(SourceCompletionProvider provider) {
/* 26 */     if (this.jsFactory == null) {
/* 27 */       this.jsFactory = (JavaScriptTypesFactory)new RhinoJavaScriptTypesFactory(provider.getTypesFactory());
/*    */     }
/* 29 */     return this.jsFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public JavaScriptParser getParser(SourceCompletionProvider provider, int dot, TypeDeclarationOptions options) {
/* 35 */     return (JavaScriptParser)new RhinoJavaScriptAstParser(provider, dot, options);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\engine\RhinoJavaScriptEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */