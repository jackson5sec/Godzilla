/*    */ package org.fife.rsta.ac.js.ast.type.ecma.v5;
/*    */ 
/*    */ import org.fife.rsta.ac.js.ast.type.TypeDeclaration;
/*    */ import org.fife.rsta.ac.js.ast.type.ecma.v3.TypeDeclarationsECMAv3;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TypeDeclarationsECMAv5
/*    */   extends TypeDeclarationsECMAv3
/*    */ {
/*    */   protected void loadTypes() {
/* 12 */     super.loadTypes();
/*    */     
/* 14 */     addTypeDeclaration("JSArray", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma5", "JS5Array", "Array", false, false));
/*    */     
/* 16 */     addTypeDeclaration("JSDate", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma5", "JS5Date", "Date", false, false));
/*    */     
/* 18 */     addTypeDeclaration("JSFunction", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma5", "JS5Function", "Function", false, false));
/*    */     
/* 20 */     addTypeDeclaration("JSObject", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma5", "JS5Object", "Object", false, false));
/*    */     
/* 22 */     addTypeDeclaration("JSString", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma5", "JS5String", "String", false, false));
/*    */     
/* 24 */     addTypeDeclaration("JSJSON", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma5", "JS5JSON", "JSON", false, false));
/*    */ 
/*    */     
/* 27 */     addTypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma5.functions.JS5ArrayFunctions", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma5.functions", "JS5ArrayFunctions", "Array", false, false));
/*    */     
/* 29 */     addTypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma5.functions.JS5DateFunctions", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma5.functions", "JS5DateFunctions", "Date", false, false));
/*    */     
/* 31 */     addTypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma5.functions.JS5FunctionFunctions", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma5.functions", "JS5FunctionFunctions", "Function", false, false));
/*    */     
/* 33 */     addTypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma5.functions.JS5ObjectFunctions", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma5.functions", "JS5ObjectFunctions", "Object", false, false));
/*    */     
/* 35 */     addTypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma5.functions.JS5StringFunctions", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma5.functions", "JS5StringFunctions", "String", false, false));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\type\ecma\v5\TypeDeclarationsECMAv5.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */