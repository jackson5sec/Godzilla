/*    */ package org.fife.rsta.ac.js.engine;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.fife.rsta.ac.js.SourceCompletionProvider;
/*    */ import org.fife.rsta.ac.js.ast.TypeDeclarationOptions;
/*    */ import org.fife.rsta.ac.js.ast.jsType.JavaScriptTypesFactory;
/*    */ import org.fife.rsta.ac.js.ast.parser.JavaScriptParser;
/*    */ import org.fife.rsta.ac.js.ast.type.TypeDeclarationFactory;
/*    */ import org.fife.rsta.ac.js.resolver.JavaScriptResolver;
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
/*    */ public abstract class JavaScriptEngine
/*    */ {
/* 24 */   private TypeDeclarationFactory typesFactory = new TypeDeclarationFactory();
/*    */ 
/*    */   
/*    */   protected JavaScriptTypesFactory jsFactory;
/*    */ 
/*    */   
/*    */   public List<String> setTypeDeclarationVersion(String ecmaVersion, boolean xmlSupported, boolean client) {
/* 31 */     return this.typesFactory.setTypeDeclarationVersion(ecmaVersion, xmlSupported, client);
/*    */   }
/*    */   
/*    */   public TypeDeclarationFactory getTypesFactory() {
/* 35 */     return this.typesFactory;
/*    */   }
/*    */   
/*    */   public abstract JavaScriptResolver getJavaScriptResolver(SourceCompletionProvider paramSourceCompletionProvider);
/*    */   
/*    */   public abstract JavaScriptTypesFactory getJavaScriptTypesFactory(SourceCompletionProvider paramSourceCompletionProvider);
/*    */   
/*    */   public abstract JavaScriptParser getParser(SourceCompletionProvider paramSourceCompletionProvider, int paramInt, TypeDeclarationOptions paramTypeDeclarationOptions);
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\engine\JavaScriptEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */