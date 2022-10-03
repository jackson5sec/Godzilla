/*    */ package org.fife.rsta.ac.js.ast.type.ecma.client;
/*    */ 
/*    */ import org.fife.rsta.ac.js.ast.type.ECMAAdditions;
/*    */ import org.fife.rsta.ac.js.ast.type.TypeDeclaration;
/*    */ import org.fife.rsta.ac.js.ast.type.ecma.TypeDeclarations;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DOMAddtions
/*    */   implements ECMAAdditions
/*    */ {
/*    */   public void addAdditionalTypes(TypeDeclarations typeDecs) {
/* 13 */     typeDecs.addTypeDeclaration("Attr", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "JSAttr", "Attr", false, false));
/* 14 */     typeDecs.addTypeDeclaration("CDATASection", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "JSCDATASection", "CDATASection", false, false));
/* 15 */     typeDecs.addTypeDeclaration("CharacterData", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "JSCharacterData", "CharacterData", false, false));
/* 16 */     typeDecs.addTypeDeclaration("Comment", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "JSComment", "Comment", false, false));
/* 17 */     typeDecs.addTypeDeclaration("Document", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "JSDocument", "Document", false, false));
/* 18 */     typeDecs.addTypeDeclaration("DocumentFragment", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "JSDocumentFragment", "DocumentFragment", false, false));
/* 19 */     typeDecs.addTypeDeclaration("DocumentType", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "JSDocumentType", "DocumentType", false, false));
/* 20 */     typeDecs.addTypeDeclaration("DOMConfiguration", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "JSDOMConfiguration", "DOMConfiguration", false, false));
/* 21 */     typeDecs.addTypeDeclaration("DOMImplementation", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "JSDOMImplementation", "DOMImplementation", false, false));
/* 22 */     typeDecs.addTypeDeclaration("DOMImplementationList", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "JSDOMImplementationList", "DOMImplementationList", false, false));
/* 23 */     typeDecs.addTypeDeclaration("DOMImplementationSource", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "DOMImplementationSource", "DOMImplementationSource", false, false));
/* 24 */     typeDecs.addTypeDeclaration("DOMLocator", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "JSDOMLocator", "DOMLocator", false, false));
/* 25 */     typeDecs.addTypeDeclaration("DOMStringList", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "JSDOMStringList", "DOMStringList", false, false));
/* 26 */     typeDecs.addTypeDeclaration("Element", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "JSElement", "Element", false, false));
/* 27 */     typeDecs.addTypeDeclaration("Entity", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "JSEntity", "Entity", false, false));
/* 28 */     typeDecs.addTypeDeclaration("EntityReference", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "JSEntityReference", "EntityReference", false, false));
/* 29 */     typeDecs.addTypeDeclaration("NamedNodeMap", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "JSNamedNodeMap", "NamedNodeMap", false, false));
/* 30 */     typeDecs.addTypeDeclaration("NameList", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "JSNameList", "NameList", false, false));
/* 31 */     typeDecs.addTypeDeclaration("Node", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "JSNode", "Node", false, false));
/* 32 */     typeDecs.addTypeDeclaration("NodeList", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "JSNodeList", "NodeList", false, false));
/* 33 */     typeDecs.addTypeDeclaration("Notation", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "JSNotation", "Notation", false, false));
/* 34 */     typeDecs.addTypeDeclaration("ProcessingInstruction", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "JSProcessingInstruction", "ProcessingInstruction", false, false));
/* 35 */     typeDecs.addTypeDeclaration("Text", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "JSText", "Text", false, false));
/* 36 */     typeDecs.addTypeDeclaration("TypeInfo", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "JSTypeInfo", "TypeInfo", false, false));
/* 37 */     typeDecs.addTypeDeclaration("UserDataHandler", new TypeDeclaration("org.fife.rsta.ac.js.ecma.api.dom", "JSUserDataHandler", "UserDataHandler", false, false));
/*    */ 
/*    */ 
/*    */     
/* 41 */     typeDecs.addECMAObject("Attr", true);
/* 42 */     typeDecs.addECMAObject("CDATASection", true);
/* 43 */     typeDecs.addECMAObject("CharacterData", true);
/* 44 */     typeDecs.addECMAObject("Comment", true);
/* 45 */     typeDecs.addECMAObject("Document", true);
/* 46 */     typeDecs.addECMAObject("DocumentFragment", true);
/* 47 */     typeDecs.addECMAObject("DOMConfiguration", true);
/* 48 */     typeDecs.addECMAObject("DOMImplementation", true);
/* 49 */     typeDecs.addECMAObject("DOMImplementationList", true);
/* 50 */     typeDecs.addECMAObject("DOMLocator", true);
/* 51 */     typeDecs.addECMAObject("DOMStringList", true);
/* 52 */     typeDecs.addECMAObject("Element", true);
/* 53 */     typeDecs.addECMAObject("Entity", true);
/* 54 */     typeDecs.addECMAObject("EntityReference", true);
/* 55 */     typeDecs.addECMAObject("NamedNodeMap", true);
/* 56 */     typeDecs.addECMAObject("NameList", true);
/* 57 */     typeDecs.addECMAObject("Node", true);
/* 58 */     typeDecs.addECMAObject("NodeList", true);
/* 59 */     typeDecs.addECMAObject("Notation", true);
/* 60 */     typeDecs.addECMAObject("ProcessingInstruction", true);
/* 61 */     typeDecs.addECMAObject("Text", true);
/* 62 */     typeDecs.addECMAObject("TypeInfo", true);
/* 63 */     typeDecs.addECMAObject("UserDataHandler", true);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\type\ecma\client\DOMAddtions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */