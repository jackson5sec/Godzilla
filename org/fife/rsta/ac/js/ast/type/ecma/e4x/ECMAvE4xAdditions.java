/*    */ package org.fife.rsta.ac.js.ast.type.ecma.e4x;
/*    */ 
/*    */ import org.fife.rsta.ac.js.ast.type.ECMAAdditions;
/*    */ import org.fife.rsta.ac.js.ast.type.TypeDeclaration;
/*    */ import org.fife.rsta.ac.js.ast.type.ecma.TypeDeclarations;
/*    */ 
/*    */ 
/*    */ public class ECMAvE4xAdditions
/*    */   implements ECMAAdditions
/*    */ {
/*    */   public void addAdditionalTypes(TypeDeclarations typeDecs) {
/* 12 */     typeDecs.addTypeDeclaration("JSGlobal", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.e4x", "E4XGlobal", "Global", false, false));
/*    */ 
/*    */     
/* 15 */     typeDecs.addTypeDeclaration("E4XNamespace", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.e4x", "E4XNamespace", "Namespace", false, false));
/*    */ 
/*    */     
/* 18 */     typeDecs.addTypeDeclaration("E4XQName", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.e4x", "E4XQName", "QName", false, false));
/*    */ 
/*    */     
/* 21 */     typeDecs.addTypeDeclaration("E4XXML", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.e4x", "E4XXML", "XML", false, false));
/*    */ 
/*    */     
/* 24 */     typeDecs.addTypeDeclaration("E4XXMLList", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.e4x", "E4XXMLList", "XMLList", false, false));
/*    */ 
/*    */     
/* 27 */     typeDecs.addTypeDeclaration("org.fife.rsta.ac.js.ecma.api.e4x.functions.E4XGlobalFunctions", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.e4x.functions", "E4XGlobalFunctions", "Global", false, false));
/*    */ 
/*    */     
/* 30 */     typeDecs.addTypeDeclaration("org.fife.rsta.ac.js.ecma.api.e4x.functions.E4XXMLFunctions", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.e4x.functions", "E4XXMLFunctions", "XML", false, false));
/*    */ 
/*    */     
/* 33 */     typeDecs.addTypeDeclaration("org.fife.rsta.ac.js.ecma.api.e4x.functions.E4XXMLListFunctions", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.e4x.functions", "E4XXMLListFunctions", "XMList", false, false));
/*    */ 
/*    */ 
/*    */     
/* 37 */     typeDecs.addECMAObject("E4XNamespace", true);
/* 38 */     typeDecs.addECMAObject("E4XQName", true);
/* 39 */     typeDecs.addECMAObject("E4XXML", true);
/* 40 */     typeDecs.addECMAObject("E4XXMLList", true);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\type\ecma\e4x\ECMAvE4xAdditions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */