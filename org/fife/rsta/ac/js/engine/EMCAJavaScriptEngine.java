/*    */ package org.fife.rsta.ac.js.engine;
/*    */ 
/*    */ import org.fife.rsta.ac.js.SourceCompletionProvider;
/*    */ import org.fife.rsta.ac.js.ast.TypeDeclarationOptions;
/*    */ import org.fife.rsta.ac.js.ast.jsType.JavaScriptTypesFactory;
/*    */ import org.fife.rsta.ac.js.ast.parser.JavaScriptAstParser;
/*    */ import org.fife.rsta.ac.js.ast.parser.JavaScriptParser;
/*    */ import org.fife.rsta.ac.js.resolver.JavaScriptCompletionResolver;
/*    */ import org.fife.rsta.ac.js.resolver.JavaScriptResolver;
/*    */ 
/*    */ 
/*    */ public class EMCAJavaScriptEngine
/*    */   extends JavaScriptEngine
/*    */ {
/*    */   public static final String EMCA_ENGINE = "EMCA";
/*    */   
/*    */   public JavaScriptResolver getJavaScriptResolver(SourceCompletionProvider provider) {
/* 18 */     return (JavaScriptResolver)new JavaScriptCompletionResolver(provider);
/*    */   }
/*    */ 
/*    */   
/*    */   public JavaScriptTypesFactory getJavaScriptTypesFactory(SourceCompletionProvider provider) {
/* 23 */     if (this.jsFactory == null) {
/* 24 */       this.jsFactory = JavaScriptTypesFactory.getDefaultJavaScriptTypesFactory(provider.getTypesFactory());
/*    */     }
/* 26 */     return this.jsFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public JavaScriptParser getParser(SourceCompletionProvider provider, int dot, TypeDeclarationOptions options) {
/* 32 */     return (JavaScriptParser)new JavaScriptAstParser(provider, dot, options);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\engine\EMCAJavaScriptEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */