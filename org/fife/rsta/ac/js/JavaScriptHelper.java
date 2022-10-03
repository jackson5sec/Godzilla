/*     */ package org.fife.rsta.ac.js;
/*     */ 
/*     */ import java.io.StringReader;
/*     */ import org.fife.rsta.ac.js.ast.type.ArrayTypeDeclaration;
/*     */ import org.fife.rsta.ac.js.ast.type.TypeDeclaration;
/*     */ import org.fife.rsta.ac.js.resolver.JavaScriptCompletionResolver;
/*     */ import org.fife.rsta.ac.js.resolver.JavaScriptResolver;
/*     */ import org.mozilla.javascript.CompilerEnvirons;
/*     */ import org.mozilla.javascript.ErrorReporter;
/*     */ import org.mozilla.javascript.EvaluatorException;
/*     */ import org.mozilla.javascript.Parser;
/*     */ import org.mozilla.javascript.ast.ArrayLiteral;
/*     */ import org.mozilla.javascript.ast.AstNode;
/*     */ import org.mozilla.javascript.ast.AstRoot;
/*     */ import org.mozilla.javascript.ast.ElementGet;
/*     */ import org.mozilla.javascript.ast.ExpressionStatement;
/*     */ import org.mozilla.javascript.ast.FunctionCall;
/*     */ import org.mozilla.javascript.ast.InfixExpression;
/*     */ import org.mozilla.javascript.ast.Name;
/*     */ import org.mozilla.javascript.ast.NewExpression;
/*     */ import org.mozilla.javascript.ast.NodeVisitor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavaScriptHelper
/*     */ {
/*  38 */   private static final String INFIX = InfixExpression.class
/*  39 */     .getName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean canResolveVariable(AstNode target, AstNode initialiser) {
/*  51 */     String varName = target.toSource();
/*     */     try {
/*  53 */       String init = initialiser.toSource();
/*  54 */       String[] splitInit = init.split("\\.");
/*  55 */       if (splitInit.length > 0) {
/*  56 */         return !varName.equals(splitInit[0]);
/*     */       }
/*     */     }
/*  59 */     catch (Exception exception) {}
/*     */ 
/*     */     
/*  62 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final ParseText parseEnteredText(String text) {
/*  74 */     CompilerEnvirons env = new CompilerEnvirons();
/*  75 */     env.setIdeMode(true);
/*  76 */     env.setErrorReporter(new ErrorReporter()
/*     */         {
/*     */           public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
/*  88 */             return null;
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {}
/*     */         });
/*  98 */     env.setRecoverFromErrors(true);
/*  99 */     Parser parser = new Parser(env);
/* 100 */     StringReader r = new StringReader(text);
/* 101 */     ParseText pt = new ParseText();
/*     */     try {
/* 103 */       AstRoot root = parser.parse(r, null, 0);
/* 104 */       ParseTextVisitor visitor = new ParseTextVisitor(text);
/* 105 */       root.visitAll(visitor);
/* 106 */       pt.isNew = visitor.isNew();
/* 107 */       pt.text = visitor.getLastNodeSource();
/*     */     }
/* 109 */     catch (Exception e) {
/* 110 */       e.printStackTrace();
/*     */     } 
/* 112 */     return pt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getFunctionNameLookup(AstNode node, SourceCompletionProvider provider) {
/* 122 */     FunctionCall call = findFunctionCallFromNode(node);
/* 123 */     return provider.getJavaScriptEngine().getJavaScriptResolver(provider).getFunctionNameLookup(call, provider);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FunctionCall findFunctionCallFromNode(AstNode node) {
/* 141 */     AstNode parent = node;
/* 142 */     for (int i = 0; i < 3; i++) {
/*     */       
/* 144 */       if (parent == null || parent instanceof AstRoot)
/*     */         break; 
/* 146 */       if (parent instanceof FunctionCall) {
/* 147 */         return (FunctionCall)parent;
/*     */       }
/* 149 */       parent = parent.getParent();
/*     */     } 
/* 151 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final TypeDeclaration tokenToNativeTypeDeclaration(AstNode typeNode, SourceCompletionProvider provider) {
/* 165 */     if (typeNode != null) {
/* 166 */       AstNode expr; TypeDeclaration dec; String self; switch (typeNode.getType()) {
/*     */         case 134:
/* 168 */           expr = ((ExpressionStatement)typeNode).getExpression();
/* 169 */           if (expr.getType() == 39) {
/* 170 */             return provider.resolveTypeDeclation(((Name)expr)
/* 171 */                 .getIdentifier());
/*     */           }
/*     */           break;
/*     */         case 124:
/* 175 */           return getTypeDeclaration("JSError", provider);
/*     */         case 39:
/* 177 */           return provider.resolveTypeDeclation(((Name)typeNode)
/* 178 */               .getIdentifier());
/*     */         case 30:
/* 180 */           return processNewNode(typeNode, provider);
/*     */         case 40:
/* 182 */           return getTypeDeclaration("JSNumber", provider);
/*     */         case 66:
/* 184 */           return getTypeDeclaration("JSObject", provider);
/*     */         case 41:
/* 186 */           return getTypeDeclaration("JSString", provider);
/*     */         case 44:
/*     */         case 45:
/* 189 */           return getTypeDeclaration("JSBoolean", provider);
/*     */         case 65:
/* 191 */           return createArrayType(typeNode, provider);
/*     */         case 36:
/* 193 */           dec = findGetElementType(typeNode, provider);
/* 194 */           if (dec != null) {
/* 195 */             return dec;
/*     */           }
/*     */           break;
/*     */ 
/*     */         
/*     */         case 43:
/* 201 */           self = provider.getSelf();
/* 202 */           if (self == null) {
/* 203 */             self = "JSGlobal";
/*     */           }
/* 205 */           return getTypeDeclaration(self, provider);
/*     */ 
/*     */         
/*     */         case 145:
/* 209 */           if (provider.isXMLSupported())
/*     */           {
/* 211 */             return getTypeDeclaration("E4XXML", provider);
/*     */           }
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/* 217 */       if (isInfixOnly(typeNode)) {
/* 218 */         TypeDeclaration typeDeclaration = getTypeFromInFixExpression(typeNode, provider);
/* 219 */         if (typeDeclaration != null) {
/* 220 */           return typeDeclaration;
/*     */         }
/*     */       } 
/*     */     } 
/* 224 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static TypeDeclaration findGetElementType(AstNode node, SourceCompletionProvider provider) {
/* 239 */     ElementGet getElement = (ElementGet)node;
/*     */     
/* 241 */     AstNode target = getElement.getTarget();
/* 242 */     if (target != null) {
/* 243 */       JavaScriptCompletionResolver resolver = new JavaScriptCompletionResolver(provider);
/* 244 */       TypeDeclaration typeDec = resolver.resolveNode(target);
/* 245 */       if (typeDec != null && 
/* 246 */         typeDec instanceof ArrayTypeDeclaration) {
/* 247 */         return ((ArrayTypeDeclaration)typeDec).getArrayType();
/*     */       }
/*     */     } 
/*     */     
/* 251 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static TypeDeclaration createArrayType(AstNode typeNode, SourceCompletionProvider provider) {
/* 262 */     TypeDeclaration array = getTypeDeclaration("JSArray", provider);
/* 263 */     if (array != null) {
/*     */       
/* 265 */       ArrayTypeDeclaration arrayDec = new ArrayTypeDeclaration(array.getPackageName(), array.getAPITypeName(), array.getJSName());
/* 266 */       ArrayLiteral arrayLit = (ArrayLiteral)typeNode;
/*     */       
/* 268 */       arrayDec.setArrayType(findArrayType(arrayLit, provider));
/* 269 */       return (TypeDeclaration)arrayDec;
/*     */     } 
/*     */     
/* 272 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static TypeDeclaration findArrayType(ArrayLiteral arrayLit, SourceCompletionProvider provider) {
/* 283 */     TypeDeclaration dec = null;
/* 284 */     boolean first = true;
/* 285 */     JavaScriptResolver resolver = provider.getJavaScriptEngine().getJavaScriptResolver(provider);
/* 286 */     for (AstNode element : arrayLit.getElements()) {
/* 287 */       TypeDeclaration elementType = resolver.resolveNode(element);
/* 288 */       if (first) {
/* 289 */         dec = elementType;
/* 290 */         first = false;
/*     */         continue;
/*     */       } 
/* 293 */       if (elementType != null && !elementType.equals(dec)) {
/* 294 */         dec = provider.getTypesFactory().getDefaultTypeDeclaration();
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 299 */     return (dec != null) ? dec : provider.getTypesFactory().getDefaultTypeDeclaration();
/*     */   }
/*     */ 
/*     */   
/*     */   private static TypeDeclaration processNewNode(AstNode typeNode, SourceCompletionProvider provider) {
/* 304 */     String newName = findNewExpressionString(typeNode);
/* 305 */     if (newName != null) {
/*     */       
/* 307 */       TypeDeclaration newType = createNewTypeDeclaration(newName);
/* 308 */       if (newType.isQualified()) {
/* 309 */         return newType;
/*     */       }
/* 311 */       return findOrMakeTypeDeclaration(newName, provider);
/*     */     } 
/*     */     
/* 314 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static TypeDeclaration findOrMakeTypeDeclaration(String name, SourceCompletionProvider provider) {
/* 320 */     TypeDeclaration newType = getTypeDeclaration(name, provider);
/* 321 */     if (newType == null) {
/* 322 */       newType = createNewTypeDeclaration(name);
/*     */     }
/* 324 */     return newType;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static TypeDeclaration createNewTypeDeclaration(String newName) {
/* 330 */     String pName = (newName.indexOf('.') > 0) ? newName.substring(0, newName
/* 331 */         .lastIndexOf('.')) : "";
/* 332 */     String cName = (newName.indexOf('.') > 0) ? newName.substring(newName
/* 333 */         .lastIndexOf('.') + 1, newName.length()) : newName;
/*     */     
/* 335 */     return new TypeDeclaration(pName, cName, newName);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isInfixOnly(AstNode typeNode) {
/* 340 */     return (typeNode instanceof InfixExpression && typeNode
/* 341 */       .getClass().getName().equals(INFIX));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class InfixVisitor
/*     */     implements NodeVisitor
/*     */   {
/* 355 */     private String type = null;
/*     */     private SourceCompletionProvider provider;
/*     */     
/*     */     private InfixVisitor(SourceCompletionProvider provider) {
/* 359 */       this.provider = provider;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean visit(AstNode node) {
/* 365 */       if (!(node instanceof InfixExpression)) {
/*     */         
/* 367 */         JavaScriptResolver resolver = this.provider.getJavaScriptEngine().getJavaScriptResolver(this.provider);
/* 368 */         TypeDeclaration dec = resolver.resolveNode(node);
/*     */         
/* 370 */         boolean isNumber = ("JSNumber".equals(dec.getAPITypeName()) || "JSBoolean".equals(dec.getAPITypeName()));
/* 371 */         if (isNumber && (this.type == null || (isNumber && "JSNumber".equals(this.type)))) {
/* 372 */           this.type = "JSNumber";
/*     */         } else {
/*     */           
/* 375 */           this.type = "JSString";
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 380 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static TypeDeclaration getTypeFromInFixExpression(AstNode node, SourceCompletionProvider provider) {
/*     */     InfixVisitor visitor;
/* 393 */     InfixExpression infix = (InfixExpression)node;
/* 394 */     switch (infix.getType()) {
/*     */ 
/*     */       
/*     */       case 21:
/*     */       case 22:
/*     */       case 23:
/*     */       case 24:
/*     */       case 25:
/* 402 */         visitor = new InfixVisitor(provider);
/* 403 */         infix.visit(visitor);
/* 404 */         return getTypeDeclaration(visitor.type, provider);
/*     */     } 
/*     */ 
/*     */     
/* 408 */     AstNode rightExp = infix.getRight();
/* 409 */     JavaScriptResolver resolver = provider.getJavaScriptEngine().getJavaScriptResolver(provider);
/* 410 */     TypeDeclaration dec = resolver.resolveNode(rightExp);
/* 411 */     return dec;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String convertNodeToSource(AstNode node) {
/*     */     try {
/* 419 */       return node.toSource();
/*     */     }
/* 421 */     catch (Exception e) {
/*     */       
/* 423 */       Logger.log(e.getMessage());
/*     */       
/* 425 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int findIndexOfFirstOpeningBracket(String text) {
/* 437 */     int index = 0;
/* 438 */     if (text != null && text.length() > 0) {
/* 439 */       char[] chars = text.toCharArray();
/* 440 */       for (int i = chars.length - 1; i >= 0; i--) {
/* 441 */         switch (chars[i]) {
/*     */           case '(':
/* 443 */             index--;
/*     */             break;
/*     */           case ')':
/* 446 */             index++;
/*     */             break;
/*     */         } 
/* 449 */         if (index == -1) {
/* 450 */           return i + 1;
/*     */         }
/*     */       } 
/*     */     } else {
/* 454 */       return 0;
/*     */     } 
/* 456 */     return 0;
/*     */   }
/*     */   
/*     */   public static int findIndexOfFirstOpeningSquareBracket(String text) {
/* 460 */     int index = 0;
/* 461 */     if (text != null && text.length() > 0) {
/* 462 */       char[] chars = text.toCharArray();
/* 463 */       for (int i = chars.length - 1; i >= 0; i--) {
/* 464 */         switch (chars[i]) {
/*     */           case '[':
/* 466 */             index--;
/*     */             break;
/*     */           case ']':
/* 469 */             index++;
/*     */             break;
/*     */         } 
/* 472 */         if (index == -1) {
/* 473 */           return i + 1;
/*     */         }
/*     */       } 
/*     */     } else {
/* 477 */       return 0;
/*     */     } 
/* 479 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String findNewExpressionString(AstNode node) {
/* 491 */     NewExpression newEx = (NewExpression)node;
/* 492 */     AstNode target = newEx.getTarget();
/* 493 */     String source = target.toSource();
/* 494 */     int index = source.indexOf('(');
/* 495 */     if (index != -1) {
/* 496 */       source = source.substring(0, index);
/*     */     }
/* 498 */     return source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TypeDeclaration getTypeDeclaration(String name, SourceCompletionProvider provider) {
/* 510 */     return provider.getTypesFactory().getTypeDeclaration(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int findLastIndexOfJavaScriptIdentifier(String input) {
/* 515 */     int index = -1;
/* 516 */     if (input != null) {
/* 517 */       char[] c = input.toCharArray();
/* 518 */       for (int i = 0; i < c.length; i++) {
/* 519 */         if (!Character.isJavaIdentifierPart(c[i])) {
/* 520 */           index = i;
/*     */         }
/*     */       } 
/*     */     } 
/* 524 */     return index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String removeLastDotFromText(String text) {
/* 533 */     int trim = text.length();
/* 534 */     if (text.lastIndexOf('.') != -1) {
/* 535 */       trim = text.lastIndexOf('.');
/*     */     }
/*     */     
/* 538 */     String parseText = text.substring(0, trim);
/*     */     
/* 540 */     return parseText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String trimFromLastParam(String text) {
/* 556 */     int trim = 0;
/* 557 */     if (text.lastIndexOf(',') != -1) {
/* 558 */       int i1 = 0;
/* 559 */       int i2 = 0;
/* 560 */       char[] chars = text.toCharArray();
/* 561 */       for (int i = chars.length - 1; i >= 0; i--) {
/* 562 */         switch (chars[i]) {
/*     */           case '(':
/* 564 */             i1--;
/*     */             break;
/*     */           case '[':
/* 567 */             i2--;
/*     */             break;
/*     */           case ')':
/* 570 */             i1++;
/*     */             break;
/*     */           case ']':
/* 573 */             i2++;
/*     */             break;
/*     */           case ',':
/* 576 */             if (i1 == 0 && i2 == 0) {
/* 577 */               return text.substring(i + 1, text.length()).trim();
/*     */             }
/*     */             break;
/*     */         } 
/*     */ 
/*     */       
/*     */       } 
/* 584 */       trim = text.lastIndexOf(',') + 1;
/*     */     } 
/*     */     
/* 587 */     String parseText = text.substring(trim, text.length());
/*     */     
/* 589 */     return parseText.trim();
/*     */   }
/*     */   
/*     */   private static class ParseTextVisitor
/*     */     implements NodeVisitor
/*     */   {
/*     */     private AstNode lastNode;
/*     */     private String text;
/*     */     private boolean isNew;
/*     */     
/*     */     private ParseTextVisitor(String text) {
/* 600 */       this.text = text;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean visit(AstNode node) {
/* 606 */       switch (node.getType()) {
/*     */         case 39:
/*     */         case 40:
/*     */         case 41:
/*     */         case 44:
/*     */         case 45:
/*     */         case 65:
/*     */         case 66:
/* 614 */           this.lastNode = node;
/*     */           break;
/*     */         case 30:
/* 617 */           this.isNew = true;
/*     */           break;
/*     */       } 
/* 620 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getLastNodeSource() {
/* 625 */       return (this.lastNode != null) ? this.lastNode.toSource() : (this.isNew ? "" : this.text);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isNew() {
/* 630 */       return this.isNew;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ParseText
/*     */   {
/* 637 */     public String text = "";
/*     */     public boolean isNew;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\JavaScriptHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */