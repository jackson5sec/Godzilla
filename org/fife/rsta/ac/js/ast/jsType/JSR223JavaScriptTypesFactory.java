/*    */ package org.fife.rsta.ac.js.ast.jsType;
/*    */ 
/*    */ import org.fife.rsta.ac.js.ast.type.TypeDeclaration;
/*    */ import org.fife.rsta.ac.js.ast.type.TypeDeclarationFactory;
/*    */ 
/*    */ 
/*    */ public class JSR223JavaScriptTypesFactory
/*    */   extends JavaScriptTypesFactory
/*    */ {
/*    */   public JSR223JavaScriptTypesFactory(TypeDeclarationFactory typesFactory) {
/* 11 */     super(typesFactory);
/*    */   }
/*    */ 
/*    */   
/*    */   public JavaScriptType makeJavaScriptType(TypeDeclaration type) {
/* 16 */     return new JSR223Type(type);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\jsType\JSR223JavaScriptTypesFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */