/*    */ package org.fife.rsta.ac.js.ast.type.ecma.client;
/*    */ 
/*    */ import org.fife.rsta.ac.js.ast.type.ECMAAdditions;
/*    */ import org.fife.rsta.ac.js.ast.type.TypeDeclaration;
/*    */ import org.fife.rsta.ac.js.ast.type.ecma.TypeDeclarations;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClientBrowserAdditions
/*    */   implements ECMAAdditions
/*    */ {
/*    */   public void addAdditionalTypes(TypeDeclarations typeDecs) {
/* 15 */     typeDecs.addTypeDeclaration("Window", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.client", "Window", "Window", false, false));
/* 16 */     typeDecs.addTypeDeclaration("History", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.client", "History", "History", false, false));
/* 17 */     typeDecs.addTypeDeclaration("Location", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.client", "Location", "Location", false, false));
/* 18 */     typeDecs.addTypeDeclaration("Navigator", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.client", "Navigator", "Navigator", false, false));
/* 19 */     typeDecs.addTypeDeclaration("Screen", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.client", "Screen", "Screen", false, false));
/* 20 */     typeDecs.addTypeDeclaration("BarProp", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.client", "BarProp", "BarProp", false, false));
/*    */     
/* 22 */     typeDecs.addTypeDeclaration("org.fife.rsta.ac.js.ecma.api.client.funtions.HistoryFunctions", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.client.funtions", "HistoryFunctions", "History", false, false));
/*    */     
/* 24 */     typeDecs.addTypeDeclaration("org.fife.rsta.ac.js.ecma.api.client.funtions.LocationFunctions", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.client.funtions", "LocationFunctions", "Location", false, false));
/*    */     
/* 26 */     typeDecs.addTypeDeclaration("org.fife.rsta.ac.js.ecma.api.client.funtions.NavigatorFunctions", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.client.funtions", "NavigatorFunctions", "Navigator", false, false));
/*    */     
/* 28 */     typeDecs.addTypeDeclaration("org.fife.rsta.ac.js.ecma.api.client.funtions.WindowFunctions", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.client.funtions", "WindowFunctions", "Window", false, false));
/*    */ 
/*    */     
/* 31 */     typeDecs.addECMAObject("Window", true);
/* 32 */     typeDecs.addECMAObject("History", true);
/* 33 */     typeDecs.addECMAObject("Location", true);
/* 34 */     typeDecs.addECMAObject("Navigator", true);
/* 35 */     typeDecs.addECMAObject("Screen", true);
/* 36 */     typeDecs.addECMAObject("BarProp", true);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\type\ecma\client\ClientBrowserAdditions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */