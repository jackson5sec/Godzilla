/*    */ package org.fife.rsta.ac.js.ast.type.ecma.v3;
/*    */ 
/*    */ import org.fife.rsta.ac.js.ast.type.TypeDeclaration;
/*    */ import org.fife.rsta.ac.js.ast.type.ecma.TypeDeclarations;
/*    */ 
/*    */ 
/*    */ public class TypeDeclarationsECMAv3
/*    */   extends TypeDeclarations
/*    */ {
/*    */   protected void loadTypes() {
/* 11 */     addTypeDeclaration("JSArray", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3", "JSArray", "Array", false, false));
/*    */     
/* 13 */     addTypeDeclaration("JSBoolean", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3", "JSBoolean", "Boolean", false, false));
/*    */     
/* 15 */     addTypeDeclaration("JSDate", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3", "JSDate", "Date", false, false));
/*    */     
/* 17 */     addTypeDeclaration("JSError", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3", "JSError", "Error", false, false));
/*    */     
/* 19 */     addTypeDeclaration("JSFunction", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3", "JSFunction", "Function", false, false));
/*    */     
/* 21 */     addTypeDeclaration("JSMath", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3", "JSMath", "Math", false, false));
/*    */     
/* 23 */     addTypeDeclaration("JSNumber", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3", "JSNumber", "Number", false, false));
/*    */     
/* 25 */     addTypeDeclaration("JSObject", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3", "JSObject", "Object", false, false));
/*    */     
/* 27 */     addTypeDeclaration("JSRegExp", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3", "JSRegExp", "RegExp", false, false));
/*    */     
/* 29 */     addTypeDeclaration("JSString", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3", "JSString", "String", false, false));
/*    */     
/* 31 */     addTypeDeclaration("JSGlobal", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3", "JSGlobal", "Global", false, false));
/*    */     
/* 33 */     addTypeDeclaration("any", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3", "JSUndefined", "undefined", false, false));
/*    */ 
/*    */     
/* 36 */     addTypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3.functions.JSObjectFunctions", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3.functions", "JSObjectFunctions", "Object", false, false));
/*    */     
/* 38 */     addTypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3.functions.JSArrayFunctions", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3.functions", "JSArrayFunctions", "Array", false, false));
/*    */     
/* 40 */     addTypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3.functions.JSDateFunctions", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3.functions", "JSDateFunctions", "Date", false, false));
/*    */     
/* 42 */     addTypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3.functions.JSFunctionFunctions", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3.functions", "JSFunctionFunctions", "Function", false, false));
/*    */     
/* 44 */     addTypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3.functions.JSNumberFunctions", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3.functions", "JSNumberFunctions", "Number", false, false));
/*    */     
/* 46 */     addTypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3.functions.JSRegExpFunctions", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3.functions", "JSRegExpFunctions", "RegExp", false, false));
/*    */     
/* 48 */     addTypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3.functions.JSStringFunctions", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3.functions", "JSStringFunctions", "String", false, false));
/*    */     
/* 50 */     addTypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3.functions.JSGlobalFunctions", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.ecma3.functions", "JSGlobalFunctions", "Global", false, false));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\type\ecma\v3\TypeDeclarationsECMAv3.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */