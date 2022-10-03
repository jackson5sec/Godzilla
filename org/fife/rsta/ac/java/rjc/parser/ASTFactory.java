/*      */ package org.fife.rsta.ac.java.rjc.parser;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import org.fife.rsta.ac.java.rjc.ast.CodeBlock;
/*      */ import org.fife.rsta.ac.java.rjc.ast.CompilationUnit;
/*      */ import org.fife.rsta.ac.java.rjc.ast.EnumBody;
/*      */ import org.fife.rsta.ac.java.rjc.ast.EnumDeclaration;
/*      */ import org.fife.rsta.ac.java.rjc.ast.Field;
/*      */ import org.fife.rsta.ac.java.rjc.ast.FormalParameter;
/*      */ import org.fife.rsta.ac.java.rjc.ast.ImportDeclaration;
/*      */ import org.fife.rsta.ac.java.rjc.ast.LocalVariable;
/*      */ import org.fife.rsta.ac.java.rjc.ast.Member;
/*      */ import org.fife.rsta.ac.java.rjc.ast.Method;
/*      */ import org.fife.rsta.ac.java.rjc.ast.NormalClassDeclaration;
/*      */ import org.fife.rsta.ac.java.rjc.ast.NormalInterfaceDeclaration;
/*      */ import org.fife.rsta.ac.java.rjc.ast.Package;
/*      */ import org.fife.rsta.ac.java.rjc.ast.TypeDeclaration;
/*      */ import org.fife.rsta.ac.java.rjc.ast.TypeDeclarationContainer;
/*      */ import org.fife.rsta.ac.java.rjc.lang.Annotation;
/*      */ import org.fife.rsta.ac.java.rjc.lang.Modifiers;
/*      */ import org.fife.rsta.ac.java.rjc.lang.Type;
/*      */ import org.fife.rsta.ac.java.rjc.lang.TypeArgument;
/*      */ import org.fife.rsta.ac.java.rjc.lang.TypeParameter;
/*      */ import org.fife.rsta.ac.java.rjc.lexer.Scanner;
/*      */ import org.fife.rsta.ac.java.rjc.lexer.Token;
/*      */ import org.fife.rsta.ac.java.rjc.lexer.TokenTypes;
/*      */ import org.fife.rsta.ac.java.rjc.notices.ParserNotice;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ASTFactory
/*      */   implements TokenTypes
/*      */ {
/*      */   private static final boolean DEBUG = false;
/*      */   private boolean nextMemberDeprecated;
/*      */   
/*      */   private boolean checkDeprecated() {
/*   65 */     boolean deprecated = this.nextMemberDeprecated;
/*   66 */     this.nextMemberDeprecated = false;
/*   67 */     return deprecated;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void checkForDuplicateLocalVarNames(CompilationUnit cu, Token lVar, CodeBlock block, Method m) {
/*   86 */     String name = lVar.getLexeme();
/*   87 */     boolean found = false;
/*      */     
/*      */     int i;
/*      */     
/*   91 */     for (i = 0; i < block.getLocalVarCount(); i++) {
/*   92 */       LocalVariable otherLocal = block.getLocalVar(i);
/*   93 */       if (name.equals(otherLocal.getName())) {
/*   94 */         cu.addParserNotice(lVar, "Duplicate local variable: " + name);
/*   95 */         found = true;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     
/*  101 */     if (!found)
/*      */     {
/*      */ 
/*      */       
/*  105 */       if (block.getParent() != null) {
/*  106 */         checkForDuplicateLocalVarNames(cu, lVar, block.getParent(), m);
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*  111 */       else if (m != null) {
/*  112 */         for (i = 0; i < m.getParameterCount(); i++) {
/*  113 */           FormalParameter param = m.getParameter(i);
/*  114 */           if (name.equals(param.getName())) {
/*  115 */             cu.addParserNotice(lVar, "Duplicate local variable: " + name);
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Annotation _getAnnotation(CompilationUnit cu, Scanner s) throws IOException {
/*  137 */     s.yylexNonNull(67108864, "Annotation expected");
/*  138 */     Type type = _getType(cu, s);
/*      */     
/*  140 */     if ("Deprecated".equals(type.toString())) {
/*  141 */       this.nextMemberDeprecated = true;
/*      */     }
/*      */     
/*  144 */     if (s.yyPeekCheckType() == 8388609) {
/*  145 */       s.yylex();
/*      */       
/*  147 */       s.eatThroughNextSkippingBlocks(8388610);
/*      */     } 
/*      */     
/*  150 */     Annotation a = new Annotation(type);
/*  151 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private CodeBlock _getBlock(CompilationUnit cu, CodeBlock parent, Method m, Scanner s, boolean isStatic) throws IOException {
/*  158 */     return _getBlock(cu, parent, m, s, isStatic, 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private CodeBlock _getBlock(CompilationUnit cu, CodeBlock parent, Method m, Scanner s, boolean isStatic, int depth) throws IOException {
/*  177 */     log("Entering _getBlock() (" + depth + ")");
/*      */ 
/*      */ 
/*      */     
/*  181 */     Token t = s.yylexNonNull(8388611, "'{' expected");
/*  182 */     CodeBlock block = new CodeBlock(isStatic, s.createOffset(t.getOffset()));
/*  183 */     block.setParent(parent);
/*  184 */     boolean atStatementStart = true;
/*      */ 
/*      */     
/*      */     while (true) {
/*      */       CodeBlock child, tryBlock;
/*      */       
/*      */       int nextType;
/*      */       
/*  192 */       if ((t = s.yylex()) == null) {
/*  193 */         log("Exiting _getBlock() - eos (" + depth + ")");
/*  194 */         block.setDeclarationEndOffset(s.createOffset(s.getOffset()));
/*  195 */         return block;
/*      */       } 
/*      */       
/*  198 */       int type = t.getType();
/*  199 */       boolean isFinal = false;
/*      */       
/*  201 */       switch (type) {
/*      */         
/*      */         case 8388611:
/*  204 */           s.yyPushback(t);
/*  205 */           child = _getBlock(cu, block, m, s, isStatic, depth + 1);
/*  206 */           block.add(child);
/*  207 */           atStatementStart = true;
/*      */           continue;
/*      */         
/*      */         case 8388612:
/*  211 */           block.setDeclarationEndOffset(s.createOffset(t.getOffset()));
/*      */           break;
/*      */         
/*      */         case 65583:
/*  215 */           t = s.yyPeekNonNull(8388611, 8388609, "'{' or '(' expected");
/*  216 */           if (t.getType() == 8388609)
/*      */           {
/*  218 */             s.eatParenPairs();
/*      */           }
/*  220 */           s.yyPeekNonNull(8388611, "'{' expected");
/*  221 */           tryBlock = _getBlock(cu, block, m, s, isStatic, depth + 1);
/*  222 */           block.add(tryBlock);
/*  223 */           while (s.yyPeekCheckType() == 65543 && s
/*  224 */             .yyPeekCheckType(2) == 8388609) {
/*  225 */             Type exType; Token var; CodeBlock catchBlock; int offs; s.yylex();
/*  226 */             s.yylex();
/*      */ 
/*      */             
/*  229 */             boolean multiCatch = false;
/*      */             while (true) {
/*  231 */               isFinal = false;
/*  232 */               Token temp = s.yyPeekNonNull(262144, 65554, "Throwable type expected");
/*  233 */               if (temp.isType(65554)) {
/*  234 */                 isFinal = true;
/*  235 */                 s.yylex();
/*      */               } 
/*  237 */               s.yyPeekNonNull(262144, "Variable declarator expected");
/*  238 */               exType = _getType(cu, s);
/*  239 */               var = s.yylexNonNull(262144, 16777237, "Variable declarator expected");
/*  240 */               multiCatch |= var.isType(16777237);
/*  241 */               if (!var.isType(16777237)) {
/*  242 */                 s.yylexNonNull(8388610, "')' expected");
/*  243 */                 s.yyPeekNonNull(8388611, "'{' expected");
/*  244 */                 catchBlock = _getBlock(cu, block, m, s, false, depth);
/*  245 */                 offs = var.getOffset();
/*  246 */                 if (multiCatch)
/*      */                 
/*      */                 { 
/*      */                   
/*  250 */                   exType = new Type("java");
/*  251 */                   exType.addIdentifier("lang", null);
/*  252 */                   exType.addIdentifier("Throwable", null); }  break;
/*      */               } 
/*  254 */             }  LocalVariable localVar = new LocalVariable(s, isFinal, exType, offs, var.getLexeme());
/*  255 */             checkForDuplicateLocalVarNames(cu, var, block, m);
/*  256 */             catchBlock.addLocalVariable(localVar);
/*  257 */             block.add(catchBlock);
/*      */           } 
/*      */           continue;
/*      */ 
/*      */ 
/*      */         
/*      */         case 65557:
/*      */         case 65586:
/*  265 */           nextType = s.yyPeekCheckType();
/*  266 */           while (nextType != -1 && nextType != 8388609) {
/*  267 */             t = s.yylex();
/*  268 */             if (t != null) {
/*  269 */               ParserNotice pn = new ParserNotice(t, "Unexpected token");
/*  270 */               cu.addParserNotice(pn);
/*      */             } 
/*  272 */             nextType = s.yyPeekCheckType();
/*      */           } 
/*  274 */           if (nextType == 8388609) {
/*  275 */             s.eatParenPairs();
/*      */           }
/*  277 */           nextType = s.yyPeekCheckType();
/*  278 */           if (nextType == 8388611) {
/*  279 */             child = _getBlock(cu, block, m, s, isStatic, depth + 1);
/*  280 */             block.add(child);
/*  281 */             atStatementStart = true;
/*      */           } 
/*      */           continue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 65554:
/*  297 */           isFinal = true;
/*  298 */           t = s.yylexNonNull("Unexpected end of file");
/*      */           break;
/*      */       } 
/*      */       
/*  302 */       if (t.isType(8388615)) {
/*  303 */         atStatementStart = true;
/*      */         continue;
/*      */       } 
/*  306 */       if (atStatementStart && (t.isBasicType() || t.isIdentifier())) {
/*  307 */         Type varType; s.yyPushback(t);
/*      */ 
/*      */         
/*      */         try {
/*  311 */           varType = _getType(cu, s, true);
/*  312 */         } catch (IOException ioe) {
/*  313 */           s.eatUntilNext(8388615, 8388611, 8388612);
/*      */           
/*  315 */           atStatementStart = true;
/*      */           continue;
/*      */         } 
/*  318 */         if (s.yyPeekCheckType() == 262144) {
/*  319 */           while ((t = s.yylexNonNull(262144, "Variable name expected (type==" + varType.toString() + ")")) != null) {
/*  320 */             int arrayDepth = s.skipBracketPairs();
/*  321 */             varType.incrementBracketPairCount(arrayDepth);
/*  322 */             String varDec = varType.toString() + " " + t.getLexeme();
/*  323 */             log(">>> Variable -- " + varDec + " (line " + t.getLine() + ")");
/*  324 */             int offs = t.getOffset();
/*  325 */             String name = t.getLexeme();
/*  326 */             LocalVariable lVar = new LocalVariable(s, isFinal, varType, offs, name);
/*  327 */             checkForDuplicateLocalVarNames(cu, t, block, m);
/*  328 */             block.addLocalVariable(lVar);
/*  329 */             nextType = s.yyPeekCheckType();
/*      */ 
/*      */             
/*  332 */             if (nextType == 33554433) {
/*  333 */               Token temp = s.eatThroughNextSkippingBlocksAndStuffInParens(8388616, 8388615);
/*  334 */               if (temp != null) {
/*  335 */                 s.yyPushback(temp);
/*      */               }
/*  337 */               nextType = s.yyPeekCheckType();
/*      */             } 
/*      */ 
/*      */ 
/*      */             
/*  342 */             if (nextType != 8388616) {
/*  343 */               s.eatThroughNextSkippingBlocks(8388615);
/*      */               break;
/*      */             } 
/*  346 */             s.yylex();
/*      */           } 
/*      */         }
/*      */         continue;
/*      */       } 
/*  351 */       atStatementStart = false;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  359 */     log("Exiting _getBlock() (" + depth + ")");
/*  360 */     return block;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void _getClassBody(CompilationUnit cu, Scanner s, NormalClassDeclaration classDec) throws IOException {
/*  368 */     log("Entering _getClassBody");
/*      */     
/*  370 */     Token t = s.yylexNonNull(8388611, "'{' expected");
/*  371 */     classDec.setBodyStartOffset(s.createOffset(t.getOffset()));
/*      */     
/*  373 */     t = s.yylexNonNull("ClassBody expected");
/*      */     
/*  375 */     while (t.getType() != 8388612) {
/*      */       Token t2; Modifiers modList; CodeBlock block; Modifiers modifiers1;
/*  377 */       switch (t.getType()) {
/*      */         case 8388615:
/*      */           break;
/*      */ 
/*      */         
/*      */         case 65574:
/*  383 */           t2 = s.yyPeekNonNull("'{' or modifier expected");
/*  384 */           if (t2.isType(8388611)) {
/*  385 */             CodeBlock codeBlock = _getBlock(cu, null, null, s, true);
/*  386 */             classDec.addMember((Member)codeBlock);
/*      */             
/*      */             break;
/*      */           } 
/*  390 */           s.yyPushback(t);
/*  391 */           modList = _getModifierList(cu, s);
/*  392 */           _getMemberDecl(cu, s, classDec, modList);
/*      */           break;
/*      */ 
/*      */         
/*      */         case 8388611:
/*  397 */           s.yyPushback(t);
/*  398 */           block = _getBlock(cu, null, null, s, false);
/*  399 */           classDec.addMember((Member)block);
/*      */           break;
/*      */         
/*      */         default:
/*  403 */           s.yyPushback(t);
/*  404 */           modifiers1 = _getModifierList(cu, s);
/*  405 */           _getMemberDecl(cu, s, classDec, modifiers1);
/*      */           break;
/*      */       } 
/*      */ 
/*      */       
/*      */       try {
/*  411 */         t = s.yylexNonNull("'}' expected (one)");
/*  412 */         classDec.setBodyEndOffset(s.createOffset(t.getOffset()));
/*  413 */       } catch (IOException ioe) {
/*  414 */         classDec.setBodyEndOffset(s.createOffset(s.getOffset()));
/*  415 */         int line = s.getLine();
/*  416 */         int col = s.getColumn();
/*  417 */         ParserNotice pn = new ParserNotice(line, col, 1, "'}' expected (two)");
/*  418 */         cu.addParserNotice(pn);
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     
/*  424 */     log("Exiting _getClassBody");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private TypeDeclaration _getClassOrInterfaceDeclaration(CompilationUnit cu, Scanner s, TypeDeclarationContainer addTo, Modifiers modList) throws IOException {
/*      */     NormalClassDeclaration normalClassDeclaration2;
/*      */     EnumDeclaration enumDeclaration;
/*      */     NormalInterfaceDeclaration normalInterfaceDeclaration;
/*  433 */     log("Entering _getClassOrInterfaceDeclaration");
/*  434 */     Token t = s.yyPeekNonNull("class, enum, interface or @interface expected");
/*      */ 
/*      */     
/*  437 */     if (modList == null) {
/*  438 */       modList = _getModifierList(cu, s);
/*      */     }
/*  440 */     t = s.yylexNonNull("class, enum, interface or @interface expected");
/*      */ 
/*      */ 
/*      */     
/*  444 */     switch (t.getType())
/*      */     
/*      */     { case 65545:
/*  447 */         normalClassDeclaration2 = _getNormalClassDeclaration(cu, s, addTo);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  474 */         normalClassDeclaration2.setModifiers(modList);
/*  475 */         normalClassDeclaration2.setDeprecated(checkDeprecated());
/*      */         
/*  477 */         log("Exiting _getClassOrInterfaceDeclaration");
/*  478 */         return (TypeDeclaration)normalClassDeclaration2;case 65552: enumDeclaration = _getEnumDeclaration(cu, s, addTo); enumDeclaration.setModifiers(modList); enumDeclaration.setDeprecated(checkDeprecated()); log("Exiting _getClassOrInterfaceDeclaration"); return (TypeDeclaration)enumDeclaration;case 65564: normalInterfaceDeclaration = _getNormalInterfaceDeclaration(cu, s, addTo); normalInterfaceDeclaration.setModifiers(modList); normalInterfaceDeclaration.setDeprecated(checkDeprecated()); log("Exiting _getClassOrInterfaceDeclaration"); return (TypeDeclaration)normalInterfaceDeclaration;case 67108864: throw new IOException("AnnotationTypeDeclaration not implemented"); }  ParserNotice notice = new ParserNotice(t, "class, interface or enum expected"); cu.addParserNotice(notice); NormalClassDeclaration normalClassDeclaration1 = _getNormalClassDeclaration(cu, s, addTo); normalClassDeclaration1.setModifiers(modList); normalClassDeclaration1.setDeprecated(checkDeprecated()); log("Exiting _getClassOrInterfaceDeclaration"); return (TypeDeclaration)normalClassDeclaration1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompilationUnit getCompilationUnit(String name, Scanner scanner) {
/*  492 */     CompilationUnit cu = new CompilationUnit(name);
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  497 */       List<Annotation> initialAnnotations = null;
/*  498 */       while (scanner.yyPeekCheckType() == 67108864) {
/*  499 */         if (initialAnnotations == null) {
/*  500 */           initialAnnotations = new ArrayList<>(1);
/*      */         }
/*  502 */         initialAnnotations.add(_getAnnotation(cu, scanner));
/*      */       } 
/*      */ 
/*      */       
/*  506 */       Token t = scanner.yylex();
/*  507 */       if (t == null) {
/*  508 */         return cu;
/*      */       }
/*  510 */       if (t.isType(65568)) {
/*  511 */         t = scanner.yyPeekNonNull("Identifier expected");
/*  512 */         int offs = t.getOffset();
/*  513 */         String qualifiedID = getQualifiedIdentifier(scanner);
/*  514 */         Package pkg = new Package(scanner, offs, qualifiedID);
/*  515 */         if (initialAnnotations != null)
/*      */         {
/*  517 */           initialAnnotations = null;
/*      */         }
/*  519 */         cu.setPackage(pkg);
/*  520 */         scanner.yylexNonNull(8388615, "Semicolon expected");
/*  521 */         t = scanner.yylex();
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  526 */       while (t != null && t.isType(65561)) {
/*      */         
/*  528 */         boolean isStatic = false;
/*  529 */         StringBuilder buf = new StringBuilder();
/*  530 */         t = scanner.yylexNonNull("Incomplete import statement");
/*  531 */         Token temp = null;
/*  532 */         int offs = 0;
/*      */         
/*  534 */         if (t.isType(65574)) {
/*  535 */           isStatic = true;
/*  536 */           t = scanner.yylexNonNull("Incomplete import statement");
/*      */         } 
/*      */         
/*  539 */         if (!t.isIdentifier()) {
/*  540 */           cu.addParserNotice(t, "Expected identifier, found: \"" + t
/*  541 */               .getLexeme() + "\"");
/*  542 */           scanner.eatThroughNextSkippingBlocks(8388615);
/*      */           
/*  544 */           t = scanner.getMostRecentToken();
/*      */         } else {
/*      */           
/*  547 */           offs = t.getOffset();
/*  548 */           buf.append(t.getLexeme());
/*  549 */           temp = scanner.yylexNonNull(8388617, 8388615, "'.' or ';' expected");
/*      */           
/*  551 */           while (temp.isType(8388617)) {
/*  552 */             temp = scanner.yylexNonNull(262144, 16777234, "Identifier or '*' expected");
/*      */             
/*  554 */             if (temp.isIdentifier()) {
/*  555 */               buf.append('.').append(temp.getLexeme());
/*      */             } else {
/*      */               
/*  558 */               buf.append(".*");
/*  559 */               temp = scanner.yylex();
/*      */               break;
/*      */             } 
/*  562 */             temp = scanner.yylexNonNull(65561, 8388617, 8388615, "'.' or ';' expected");
/*      */             
/*  564 */             if (temp.isType(65561)) {
/*  565 */               cu.addParserNotice(temp, "';' expected");
/*  566 */               t = temp;
/*      */             } 
/*      */           } 
/*      */           
/*  570 */           t = temp;
/*      */         } 
/*      */         
/*  573 */         if (temp == null || !t.isType(8388615)) {
/*  574 */           throw new IOException("Semicolon expected, found " + t);
/*      */         }
/*      */ 
/*      */         
/*  578 */         ImportDeclaration id = new ImportDeclaration(scanner, offs, buf.toString(), isStatic);
/*  579 */         cu.addImportDeclaration(id);
/*  580 */         t = scanner.yylex();
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  585 */       if (t == null) {
/*  586 */         return cu;
/*      */       }
/*      */       
/*  589 */       scanner.yyPushback(t);
/*      */       
/*  591 */       while (_getTypeDeclaration(cu, scanner) != null) {
/*  592 */         if (initialAnnotations != null)
/*      */         {
/*  594 */           initialAnnotations = null;
/*      */         
/*      */         }
/*      */       }
/*      */     
/*      */     }
/*  600 */     catch (IOException ioe) {
/*  601 */       ParserNotice notice; if (isDebug() && !(ioe instanceof java.io.EOFException)) {
/*  602 */         ioe.printStackTrace();
/*      */       }
/*      */       
/*  605 */       Token lastTokenLexed = scanner.getMostRecentToken();
/*  606 */       if (lastTokenLexed == null) {
/*  607 */         notice = new ParserNotice(0, 0, 5, ioe.getMessage());
/*      */       } else {
/*      */         
/*  610 */         notice = new ParserNotice(lastTokenLexed, ioe.getMessage());
/*      */       } 
/*  612 */       cu.addParserNotice(notice);
/*      */     } 
/*      */ 
/*      */     
/*  616 */     return cu;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private EnumBody _getEnumBody(CompilationUnit cu, Scanner s, EnumDeclaration enumDec) throws IOException {
/*  624 */     CodeBlock block = _getBlock(cu, null, null, s, false);
/*  625 */     enumDec.setBodyEndOffset(s.createOffset(block.getNameEndOffset()));
/*  626 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private EnumDeclaration _getEnumDeclaration(CompilationUnit cu, Scanner s, TypeDeclarationContainer addTo) throws IOException {
/*  633 */     Token t = s.yylexNonNull(262144, "Identifier expected");
/*  634 */     String enumName = t.getLexeme();
/*  635 */     EnumDeclaration enumDec = new EnumDeclaration(s, t.getOffset(), enumName);
/*  636 */     enumDec.setPackage(cu.getPackage());
/*  637 */     addTo.addTypeDeclaration((TypeDeclaration)enumDec);
/*      */     
/*  639 */     t = s.yylexNonNull("implements or '{' expected");
/*      */     
/*  641 */     if (t.isType(65560)) {
/*  642 */       List<Type> implemented = new ArrayList<>(1);
/*      */       do {
/*  644 */         implemented.add(_getType(cu, s));
/*  645 */         t = s.yylex();
/*  646 */       } while (t != null && t.isType(8388616));
/*      */       
/*  648 */       if (t != null) {
/*  649 */         s.yyPushback(t);
/*      */       }
/*      */     }
/*  652 */     else if (t.isType(8388611)) {
/*  653 */       s.yyPushback(t);
/*      */     } 
/*      */     
/*  656 */     _getEnumBody(cu, s, enumDec);
/*      */ 
/*      */ 
/*      */     
/*  660 */     return enumDec;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<FormalParameter> _getFormalParameters(CompilationUnit cu, List<Token> tokenList) throws IOException {
/*  668 */     List<FormalParameter> list = new ArrayList<>(0);
/*      */     
/*  670 */     Scanner s = new Scanner(tokenList);
/*  671 */     Token t = s.yylex();
/*  672 */     if (t == null) {
/*  673 */       return list;
/*      */     }
/*      */ 
/*      */     
/*      */     while (true) {
/*  678 */       boolean isFinal = false;
/*  679 */       if (t.isType(65554)) {
/*  680 */         isFinal = true;
/*  681 */         t = s.yylexNonNull("Type expected");
/*      */       } 
/*      */       
/*  684 */       List<Annotation> annotations = null;
/*  685 */       while (t.getType() == 67108864) {
/*  686 */         s.yyPushback(t);
/*  687 */         if (annotations == null) {
/*  688 */           annotations = new ArrayList<>(1);
/*      */         }
/*  690 */         annotations.add(_getAnnotation(cu, s));
/*  691 */         t = s.yylexNonNull("Type expected");
/*      */       } 
/*      */       
/*  694 */       s.yyPushback(t);
/*  695 */       Type type = _getType(cu, s);
/*  696 */       Token temp = s.yylexNonNull("Argument name expected");
/*  697 */       boolean elipsis = false;
/*  698 */       if (temp.isType(134217728)) {
/*  699 */         elipsis = true;
/*  700 */         temp = s.yylexNonNull(262144, "Argument name expected");
/*      */       } 
/*  702 */       type.incrementBracketPairCount(s.skipBracketPairs());
/*  703 */       int offs = temp.getOffset();
/*  704 */       String name = temp.getLexeme();
/*  705 */       FormalParameter param = new FormalParameter(s, isFinal, type, offs, name, annotations);
/*      */       
/*  707 */       list.add(param);
/*  708 */       if (elipsis) {
/*      */         break;
/*      */       }
/*  711 */       t = s.yylex();
/*  712 */       if (t == null) {
/*      */         break;
/*      */       }
/*  715 */       if (t.getType() != 8388616) {
/*  716 */         throw new IOException("Comma expected");
/*      */       }
/*  718 */       t = s.yylexNonNull("Parameter or ')' expected");
/*      */     } 
/*      */     
/*  721 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void _getInterfaceBody(CompilationUnit cu, Scanner s, NormalInterfaceDeclaration iDec) throws IOException {
/*  729 */     log("Entering _getInterfaceBody");
/*      */     
/*  731 */     Token t = s.yylexNonNull(8388611, "'{' expected");
/*  732 */     iDec.setBodyStartOffset(s.createOffset(t.getOffset()));
/*      */     
/*  734 */     t = s.yylexNonNull("InterfaceBody expected");
/*      */     
/*  736 */     while (t.getType() != 8388612) {
/*      */       Modifiers modList;
/*  738 */       switch (t.getType()) {
/*      */         case 8388615:
/*      */           break;
/*      */ 
/*      */         
/*      */         case 8388611:
/*  744 */           s.yyPushback(t);
/*      */           
/*  746 */           _getBlock(cu, null, null, s, false);
/*      */           break;
/*      */         
/*      */         default:
/*  750 */           s.yyPushback(t);
/*  751 */           modList = _getModifierList(cu, s);
/*  752 */           _getInterfaceMemberDecl(cu, s, iDec, modList);
/*      */           break;
/*      */       } 
/*      */ 
/*      */       
/*      */       try {
/*  758 */         t = s.yylexNonNull("'}' expected (one)");
/*  759 */         iDec.setBodyEndOffset(s.createOffset(t.getOffset()));
/*  760 */       } catch (IOException ioe) {
/*  761 */         iDec.setBodyEndOffset(s.createOffset(s.getOffset()));
/*  762 */         int line = s.getLine();
/*  763 */         int col = s.getColumn();
/*  764 */         ParserNotice pn = new ParserNotice(line, col, 1, "'}' expected (two)");
/*  765 */         cu.addParserNotice(pn);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  770 */     log("Exiting _getInterfaceBody");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void _getInterfaceMemberDecl(CompilationUnit cu, Scanner s, NormalInterfaceDeclaration iDec, Modifiers modList) throws IOException {
/*  787 */     log("Entering _getInterfaceMemberDecl");
/*      */     
/*  789 */     List<Token> tokenList = new ArrayList<>(1);
/*  790 */     List<Token> methodNameAndTypeTokenList = null;
/*  791 */     List<Token> methodParamsList = null;
/*      */     
/*  793 */     boolean methodDecl = false;
/*  794 */     boolean blockDecl = false;
/*  795 */     boolean varDecl = false;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/*  801 */       Token t = s.yylexNonNull("Unexpected end of input");
/*      */       
/*  803 */       switch (t.getType()) {
/*      */         case 8388609:
/*  805 */           methodNameAndTypeTokenList = tokenList;
/*  806 */           methodParamsList = new ArrayList<>(1);
/*  807 */           methodDecl = true;
/*      */           break;
/*      */         case 8388611:
/*  810 */           blockDecl = true;
/*      */           break;
/*      */         case 33554433:
/*  813 */           varDecl = true;
/*      */           
/*  815 */           s.eatThroughNextSkippingBlocks(8388615);
/*      */           break;
/*      */         case 8388615:
/*  818 */           varDecl = true;
/*      */           break;
/*      */       } 
/*  821 */       tokenList.add(t);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  827 */     if (varDecl) {
/*  828 */       log("*** Variable declaration:");
/*  829 */       Scanner tempScanner = new Scanner(tokenList);
/*  830 */       Type type = _getType(cu, tempScanner);
/*  831 */       Token fieldNameToken = tempScanner.yylexNonNull(262144, "Identifier (field name) expected");
/*  832 */       int bracketPairCount = tempScanner.skipBracketPairs();
/*  833 */       type.incrementBracketPairCount(bracketPairCount);
/*  834 */       Field field = new Field(s, modList, type, fieldNameToken);
/*  835 */       field.setDeprecated(checkDeprecated());
/*  836 */       field.setDocComment(s.getLastDocComment());
/*  837 */       log(field.toString());
/*  838 */       iDec.addMember((Member)field);
/*      */     } else {
/*      */       Token token;
/*  841 */       if (methodDecl) {
/*  842 */         log("*** Method declaration:");
/*  843 */         Scanner tempScanner = new Scanner(methodNameAndTypeTokenList);
/*  844 */         Type type = null;
/*  845 */         if (methodNameAndTypeTokenList.size() > 1) {
/*  846 */           if (tempScanner.yyPeekCheckType() == 16777219) {
/*  847 */             _getTypeParameters(cu, tempScanner);
/*  848 */             type = _getType(cu, tempScanner);
/*      */           } else {
/*      */             
/*  851 */             type = _getType(cu, tempScanner);
/*      */           } 
/*      */         }
/*  854 */         Token methodNameToken = tempScanner.yylexNonNull(262144, "Identifier (method name) expected");
/*      */         while (true) {
/*  856 */           Token token1 = s.yylexNonNull("Unexpected end of input");
/*  857 */           if (token1.isType(8388610)) {
/*      */             break;
/*      */           }
/*  860 */           methodParamsList.add(token1);
/*      */         } 
/*  862 */         List<FormalParameter> formalParams = _getFormalParameters(cu, methodParamsList);
/*  863 */         if (s.yyPeekCheckType() == 8388613) {
/*  864 */           if (type == null) {
/*  865 */             throw new IOException("Constructors cannot return array types");
/*      */           }
/*  867 */           type.incrementBracketPairCount(s.skipBracketPairs());
/*      */         } 
/*  869 */         List<String> thrownTypeNames = getThrownTypeNames(cu, s);
/*  870 */         token = s.yylexNonNull("'{' or ';' expected");
/*  871 */         if (token.getType() != 8388615) {
/*  872 */           throw new IOException("';' expected");
/*      */         }
/*  874 */         Method m = new Method(s, modList, type, methodNameToken, formalParams, thrownTypeNames);
/*      */         
/*  876 */         m.setDeprecated(checkDeprecated());
/*  877 */         m.setDocComment(s.getLastDocComment());
/*  878 */         iDec.addMember((Member)m);
/*      */       }
/*  880 */       else if (blockDecl) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  886 */         if (tokenList.size() < 2) {
/*  887 */           for (int i = tokenList.size() - 1; i >= 0; i--) {
/*  888 */             s.yyPushback(tokenList.get(i));
/*      */           }
/*  890 */           CodeBlock block = _getBlock(cu, null, null, s, false);
/*  891 */           iDec.addMember((Member)block);
/*      */         } else {
/*      */           
/*  894 */           s.yyPushback(token);
/*  895 */           for (int i = tokenList.size() - 1; i >= 0; i--) {
/*  896 */             s.yyPushback(tokenList.get(i));
/*      */           }
/*  898 */           _getClassOrInterfaceDeclaration(cu, s, (TypeDeclarationContainer)iDec, modList);
/*      */         } 
/*      */       } 
/*      */     } 
/*  902 */     log("Exiting _getInterfaceMemberDecl");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void _getMemberDecl(CompilationUnit cu, Scanner s, NormalClassDeclaration classDec, Modifiers modList) throws IOException {
/*  920 */     log("Entering _getMemberDecl");
/*      */     
/*  922 */     List<Token> tokenList = new ArrayList<>(1);
/*  923 */     List<Token> methodNameAndTypeTokenList = null;
/*  924 */     List<Token> methodParamsList = null;
/*      */     
/*  926 */     boolean methodDecl = false;
/*  927 */     boolean blockDecl = false;
/*  928 */     boolean varDecl = false;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/*  934 */       Token t = s.yylexNonNull("Unexpected end of input");
/*      */       
/*  936 */       switch (t.getType()) {
/*      */         case 8388609:
/*  938 */           methodNameAndTypeTokenList = tokenList;
/*  939 */           methodParamsList = new ArrayList<>(1);
/*  940 */           methodDecl = true;
/*      */           break;
/*      */         case 8388611:
/*  943 */           blockDecl = true;
/*      */           break;
/*      */         case 33554433:
/*  946 */           varDecl = true;
/*      */           
/*  948 */           s.eatThroughNextSkippingBlocks(8388615);
/*      */           break;
/*      */         case 8388615:
/*  951 */           varDecl = true;
/*      */           break;
/*      */       } 
/*  954 */       tokenList.add(t);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  960 */     if (varDecl) {
/*  961 */       log("*** Variable declaration:");
/*  962 */       Scanner tempScanner = new Scanner(tokenList);
/*  963 */       Type type = _getType(cu, tempScanner);
/*  964 */       Token fieldNameToken = tempScanner.yylexNonNull(262144, "Identifier (field name) expected");
/*  965 */       int bracketPairCount = tempScanner.skipBracketPairs();
/*  966 */       type.incrementBracketPairCount(bracketPairCount);
/*  967 */       Field field = new Field(s, modList, type, fieldNameToken);
/*  968 */       field.setDeprecated(checkDeprecated());
/*  969 */       field.setDocComment(s.getLastDocComment());
/*  970 */       log(field.toString());
/*  971 */       classDec.addMember((Member)field);
/*      */     } else {
/*  973 */       Token token; if (methodDecl) {
/*  974 */         log("*** Method declaration:");
/*  975 */         CodeBlock block = null;
/*  976 */         Scanner tempScanner = new Scanner(methodNameAndTypeTokenList);
/*  977 */         Type type = null;
/*  978 */         if (methodNameAndTypeTokenList.size() > 1) {
/*  979 */           if (tempScanner.yyPeekCheckType() == 16777219) {
/*  980 */             _getTypeParameters(cu, tempScanner);
/*  981 */             if (tempScanner.yyPeekCheckType(2) != -1)
/*      */             {
/*      */ 
/*      */               
/*  985 */               type = _getType(cu, tempScanner);
/*      */             }
/*      */           } else {
/*      */             
/*  989 */             type = _getType(cu, tempScanner);
/*      */           } 
/*      */         }
/*  992 */         Token methodNameToken = tempScanner.yylexNonNull(262144, "Identifier (method name) expected");
/*      */         
/*      */         label82: while (true) {
/*  995 */           Token token1 = s.yylexNonNull("Unexpected end of input");
/*  996 */           if (token1.isType(67108864)) {
/*      */ 
/*      */             
/*  999 */             methodParamsList.add(token1);
/*      */             
/* 1001 */             token1 = s.yylexNonNull("Unexpected end of input");
/* 1002 */             methodParamsList.add(token1);
/* 1003 */             token1 = s.yylexNonNull("Unexpected end of input");
/* 1004 */             if (token1.isType(8388609)) {
/*      */               
/* 1006 */               methodParamsList.add(token1);
/*      */               
/*      */               while (true) {
/* 1009 */                 token1 = s.yylexNonNull("Unexpected end of input");
/* 1010 */                 methodParamsList.add(token1);
/* 1011 */                 if (token1.isType(8388610)) {
/*      */                   continue label82;
/*      */                 }
/*      */               } 
/*      */             } 
/*      */ 
/*      */             
/* 1018 */             if (token1.isType(67108864)) {
/* 1019 */               s.yyPushback(token1); continue;
/*      */             } 
/* 1021 */             if (!token1.isType(8388610)) {
/*      */               
/* 1023 */               methodParamsList.add(token1);
/*      */               
/*      */               continue;
/*      */             } 
/*      */             
/*      */             break;
/*      */           } 
/*      */           
/* 1031 */           if (token1.isType(8388610)) {
/*      */             break;
/*      */           }
/*      */           
/* 1035 */           methodParamsList.add(token1);
/*      */         } 
/*      */         
/* 1038 */         List<FormalParameter> formalParams = _getFormalParameters(cu, methodParamsList);
/* 1039 */         if (s.yyPeekCheckType() == 8388613) {
/* 1040 */           if (type == null) {
/* 1041 */             throw new IOException("Constructors cannot return array types");
/*      */           }
/* 1043 */           type.incrementBracketPairCount(s.skipBracketPairs());
/*      */         } 
/* 1045 */         List<String> thrownTypeNames = getThrownTypeNames(cu, s);
/* 1046 */         Method m = new Method(s, modList, type, methodNameToken, formalParams, thrownTypeNames);
/*      */         
/* 1048 */         m.setDeprecated(checkDeprecated());
/* 1049 */         m.setDocComment(s.getLastDocComment());
/* 1050 */         classDec.addMember((Member)m);
/* 1051 */         token = s.yylexNonNull("'{' or ';' expected");
/* 1052 */         if (!token.isType(8388615))
/*      */         {
/*      */           
/* 1055 */           if (token.isType(8388611)) {
/* 1056 */             s.yyPushback(token);
/* 1057 */             block = _getBlock(cu, null, m, s, false);
/*      */           } else {
/*      */             
/* 1060 */             throw new IOException("'{' or ';' expected");
/*      */           }  } 
/* 1062 */         m.setBody(block);
/*      */       }
/* 1064 */       else if (blockDecl) {
/*      */ 
/*      */         
/* 1067 */         this.nextMemberDeprecated = false;
/* 1068 */         if (tokenList.size() < 2) {
/* 1069 */           for (int i = tokenList.size() - 1; i >= 0; i--) {
/* 1070 */             s.yyPushback(tokenList.get(i));
/*      */           }
/* 1072 */           CodeBlock block = _getBlock(cu, null, null, s, false);
/* 1073 */           classDec.addMember((Member)block);
/*      */         } else {
/*      */           
/* 1076 */           s.yyPushback(token);
/* 1077 */           for (int i = tokenList.size() - 1; i >= 0; i--) {
/* 1078 */             s.yyPushback(tokenList.get(i));
/*      */           }
/* 1080 */           _getClassOrInterfaceDeclaration(cu, s, (TypeDeclarationContainer)classDec, modList);
/*      */         } 
/*      */       } 
/*      */     } 
/* 1084 */     log("Exiting _getMemberDecl (next== " + s.yyPeek() + ")");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Modifiers _getModifierList(CompilationUnit cu, Scanner s) throws IOException {
/* 1092 */     Modifiers modList = null;
/* 1093 */     Token t = s.yylexNonNull("Unexpected end of input");
/*      */ 
/*      */     
/*      */     while (true) {
/* 1097 */       int modifier = isModifier(t);
/* 1098 */       if (modifier != -1) {
/* 1099 */         if (modList == null) {
/* 1100 */           modList = new Modifiers();
/*      */         }
/* 1102 */         if (!modList.addModifier(modifier)) {
/* 1103 */           cu.addParserNotice(t, "Duplicate modifier");
/*      */         }
/*      */       }
/* 1106 */       else if (t.isType(67108864)) {
/* 1107 */         Token next = s.yyPeekNonNull("Annotation expected");
/* 1108 */         s.yyPushback(t);
/*      */         
/* 1110 */         if (next.isType(65564)) {
/* 1111 */           return modList;
/*      */         }
/* 1113 */         if (modList == null) {
/* 1114 */           modList = new Modifiers();
/*      */         }
/* 1116 */         modList.addAnnotation(_getAnnotation(cu, s));
/*      */       } else {
/*      */         
/* 1119 */         s.yyPushback(t);
/* 1120 */         return modList;
/*      */       } 
/*      */       
/* 1123 */       t = s.yylexNonNull("Unexpected end of input");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private NormalClassDeclaration _getNormalClassDeclaration(CompilationUnit cu, Scanner s, TypeDeclarationContainer addTo) throws IOException {
/*      */     String className;
/* 1134 */     log("Entering _getNormalClassDeclaration");
/*      */ 
/*      */     
/* 1137 */     Token t = s.yylexNonNull("Identifier expected");
/* 1138 */     if (t.isType(262144)) {
/* 1139 */       className = t.getLexeme();
/*      */     } else {
/*      */       
/* 1142 */       className = "Unknown";
/* 1143 */       cu.addParserNotice(new ParserNotice(t, "Class name expected"));
/* 1144 */       s.eatUntilNext(65553, 65560, 8388611);
/*      */     } 
/*      */ 
/*      */     
/* 1148 */     NormalClassDeclaration classDec = new NormalClassDeclaration(s, t.getOffset(), className);
/* 1149 */     classDec.setPackage(cu.getPackage());
/* 1150 */     addTo.addTypeDeclaration((TypeDeclaration)classDec);
/*      */     
/* 1152 */     t = s.yylexNonNull("TypeParameters, extends, implements or '{' expected");
/* 1153 */     if (t.isType(16777219)) {
/* 1154 */       s.yyPushback(t);
/* 1155 */       List<TypeParameter> typeParams = _getTypeParameters(cu, s);
/* 1156 */       classDec.setTypeParameters(typeParams);
/* 1157 */       t = s.yylexNonNull("extends, implements or '{' expected");
/*      */     } 
/*      */     
/* 1160 */     if (t.isType(65553)) {
/* 1161 */       classDec.setExtendedType(_getType(cu, s));
/* 1162 */       t = s.yylexNonNull("implements or '{' expected");
/*      */     } 
/*      */     
/* 1165 */     if (t.isType(65560)) {
/*      */       do {
/* 1167 */         classDec.addImplemented(_getType(cu, s));
/* 1168 */         t = s.yylex();
/* 1169 */       } while (t != null && t.isType(8388616));
/* 1170 */       if (t != null) {
/* 1171 */         s.yyPushback(t);
/*      */       }
/*      */     }
/* 1174 */     else if (t.isType(8388611)) {
/* 1175 */       s.yyPushback(t);
/*      */     } 
/*      */     
/* 1178 */     _getClassBody(cu, s, classDec);
/*      */     
/* 1180 */     log("Exiting _getNormalClassDeclaration");
/* 1181 */     return classDec;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private NormalInterfaceDeclaration _getNormalInterfaceDeclaration(CompilationUnit cu, Scanner s, TypeDeclarationContainer addTo) throws IOException {
/*      */     String iName;
/* 1192 */     Token t = s.yylexNonNull("Identifier expected");
/* 1193 */     if (t.isType(262144)) {
/* 1194 */       iName = t.getLexeme();
/*      */     } else {
/*      */       
/* 1197 */       iName = "Unknown";
/* 1198 */       cu.addParserNotice(new ParserNotice(t, "Interface name expected"));
/* 1199 */       s.eatUntilNext(65553, 8388611);
/*      */     } 
/*      */ 
/*      */     
/* 1203 */     NormalInterfaceDeclaration iDec = new NormalInterfaceDeclaration(s, t.getOffset(), iName);
/* 1204 */     iDec.setPackage(cu.getPackage());
/* 1205 */     addTo.addTypeDeclaration((TypeDeclaration)iDec);
/*      */     
/* 1207 */     t = s.yylexNonNull("TypeParameters, extends or '{' expected");
/* 1208 */     if (t.isType(16777219)) {
/* 1209 */       s.yyPushback(t);
/* 1210 */       _getTypeParameters(cu, s);
/* 1211 */       t = s.yylexNonNull("Interface body expected");
/*      */     } 
/*      */     
/* 1214 */     if (t.isType(65553)) {
/*      */       do {
/* 1216 */         iDec.addExtended(_getType(cu, s));
/* 1217 */         t = s.yylex();
/* 1218 */       } while (t != null && t.isType(8388616));
/* 1219 */       if (t != null) {
/* 1220 */         s.yyPushback(t);
/*      */       }
/*      */     }
/* 1223 */     else if (t.isType(8388611)) {
/* 1224 */       s.yyPushback(t);
/*      */     } 
/*      */     
/* 1227 */     _getInterfaceBody(cu, s, iDec);
/*      */     
/* 1229 */     return iDec;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String getQualifiedIdentifier(Scanner scanner) throws IOException {
/* 1238 */     StringBuilder sb = new StringBuilder();
/*      */     Token t;
/* 1240 */     while ((t = scanner.yylex()).isIdentifier()) {
/* 1241 */       sb.append(t.getLexeme());
/* 1242 */       t = scanner.yylex();
/* 1243 */       if (t.isType(8388617)) {
/* 1244 */         sb.append('.');
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1252 */     scanner.yyPushback(t);
/*      */     
/* 1254 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<String> getThrownTypeNames(CompilationUnit cu, Scanner s) throws IOException {
/* 1262 */     if (s.yyPeekCheckType() != 65581) {
/* 1263 */       return null;
/*      */     }
/* 1265 */     s.yylex();
/*      */     
/* 1267 */     List<String> list = new ArrayList<>(1);
/*      */     
/* 1269 */     list.add(getQualifiedIdentifier(s));
/* 1270 */     while (s.yyPeekCheckType() == 8388616) {
/* 1271 */       s.yylex();
/* 1272 */       list.add(getQualifiedIdentifier(s));
/*      */     } 
/*      */     
/* 1275 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Type _getType(CompilationUnit cu, Scanner s) throws IOException {
/* 1283 */     return _getType(cu, s, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Type _getType(CompilationUnit cu, Scanner s, boolean pushbackOnUnexpected) throws IOException {
/* 1290 */     log("Entering _getType()");
/* 1291 */     Type type = new Type();
/*      */     
/* 1293 */     Token t = s.yylexNonNull("Type expected");
/*      */ 
/*      */     
/* 1296 */     if (t.isType(65584)) {
/* 1297 */       type.addIdentifier(t.getLexeme(), null);
/* 1298 */       log("Exiting _getType(): " + type.toString());
/* 1299 */       return type;
/*      */     } 
/* 1301 */     if (t.isBasicType()) {
/* 1302 */       int arrayDepth = s.skipBracketPairs();
/* 1303 */       type.addIdentifier(t.getLexeme(), null);
/* 1304 */       type.setBracketPairCount(arrayDepth);
/* 1305 */       log("Exiting _getType(): " + type.toString());
/* 1306 */       return type;
/*      */     } 
/*      */     
/*      */     while (true) {
/*      */       List<TypeArgument> typeArgs;
/* 1311 */       switch (t.getType()) {
/*      */         case 262144:
/* 1313 */           typeArgs = null;
/* 1314 */           if (s.yyPeekCheckType() == 16777219) {
/* 1315 */             typeArgs = _getTypeArguments(cu, s);
/*      */           }
/* 1317 */           type.addIdentifier(t.getLexeme(), typeArgs);
/* 1318 */           t = s.yylexNonNull("Unexpected end of input");
/* 1319 */           if (t.isType(8388617)) {
/* 1320 */             t = s.yylexNonNull("Unexpected end of input");
/*      */             continue;
/*      */           } 
/* 1323 */           if (t.isType(8388613)) {
/* 1324 */             s.yyPushback(t);
/* 1325 */             type.setBracketPairCount(s.skipBracketPairs());
/*      */           }
/*      */           else {
/*      */             
/* 1329 */             s.yyPushback(t);
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1340 */           log("Exiting _getType(): " + type.toString());
/* 1341 */           return type;
/*      */       } 
/*      */       break;
/*      */     } 
/*      */     if (pushbackOnUnexpected)
/*      */       s.yyPushback(t); 
/*      */     throw new IOException("Expected identifier, found: " + t); } private TypeArgument _getTypeArgument(CompilationUnit cu, Scanner s) throws IOException {
/*      */     TypeArgument typeArg;
/* 1349 */     log("Entering _getTypeArgument()");
/*      */ 
/*      */ 
/*      */     
/* 1353 */     Token t = s.yyPeekNonNull("Type or '?' expected");
/*      */     
/* 1355 */     if (t.isType(16777222))
/* 1356 */     { s.yylex();
/* 1357 */       t = s.yyPeek();
/* 1358 */       if (t.getType() != 16777218)
/* 1359 */       { t = s.yylexNonNull(8388616, 65553, 65576, "',', super or extends expected");
/*      */ 
/*      */         
/* 1362 */         switch (t.getType())
/*      */         { case 8388616:
/* 1364 */             typeArg = new TypeArgument(null, 0, null);
/* 1365 */             s.yyPushback(t);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1386 */             log("Exiting _getTypeArgument() : " + typeArg);
/* 1387 */             return typeArg;case 65553: otherType = _getType(cu, s); typeArg = new TypeArgument(null, 1, otherType); log("Exiting _getTypeArgument() : " + typeArg); return typeArg; }  Type otherType = _getType(cu, s); typeArg = new TypeArgument(null, 2, otherType); } else { typeArg = new TypeArgument(null, 0, null); }  } else { Type type = _getType(cu, s); typeArg = new TypeArgument(type); }  log("Exiting _getTypeArgument() : " + typeArg); return typeArg;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<TypeArgument> _getTypeArguments(CompilationUnit cu, Scanner s) throws IOException {
/*      */     Token t;
/* 1395 */     s.increaseTypeArgumentsLevel();
/* 1396 */     log("Entering _getTypeArguments() (" + s.getTypeArgumentsLevel() + ")");
/*      */     
/* 1398 */     s.markResetPosition();
/* 1399 */     s.yylexNonNull(16777219, "'<' expected");
/*      */     
/* 1401 */     List<TypeArgument> typeArgs = new ArrayList<>(1);
/*      */ 
/*      */     
/*      */     do {
/* 1405 */       typeArgs.add(_getTypeArgument(cu, s));
/* 1406 */       t = s.yylexNonNull("',' or '>' expected");
/* 1407 */       if (t.getType() != 8388616 && t.getType() != 16777218) {
/*      */ 
/*      */         
/* 1410 */         s.resetToLastMarkedPosition();
/* 1411 */         log("Exiting _getTypeArguments() (" + s.getTypeArgumentsLevel() + ") - NOT TYPE ARGUMENTS (" + t.getLexeme() + ")");
/* 1412 */         s.decreaseTypeArgumentsLevel();
/* 1413 */         return null;
/*      */       } 
/* 1415 */     } while (t.isType(8388616));
/*      */     
/* 1417 */     log("Exiting _getTypeArguments() (" + s.getTypeArgumentsLevel() + ")");
/* 1418 */     s.decreaseTypeArgumentsLevel();
/*      */     
/* 1420 */     s.clearResetPosition();
/* 1421 */     return typeArgs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private TypeDeclaration _getTypeDeclaration(CompilationUnit cu, Scanner s) throws IOException {
/* 1435 */     Token t = s.yylex();
/* 1436 */     if (t == null) {
/* 1437 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 1441 */     while (t.isType(8388615)) {
/* 1442 */       t = s.yylex();
/* 1443 */       if (t == null) {
/* 1444 */         return null;
/*      */       }
/*      */     } 
/*      */     
/* 1448 */     s.yyPushback(t);
/*      */     
/* 1450 */     String docComment = s.getLastDocComment();
/* 1451 */     TypeDeclaration td = _getClassOrInterfaceDeclaration(cu, s, (TypeDeclarationContainer)cu, null);
/* 1452 */     td.setDocComment(docComment);
/* 1453 */     return td;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private TypeParameter _getTypeParameter(CompilationUnit cu, Scanner s) throws IOException {
/* 1461 */     log("Entering _getTypeParameter()");
/*      */     
/* 1463 */     Token identifier = s.yylexNonNull(262144, "Identifier expected");
/* 1464 */     TypeParameter typeParam = new TypeParameter(identifier);
/*      */     
/* 1466 */     if (s.yyPeekCheckType() == 65553) {
/*      */       do {
/* 1468 */         s.yylex();
/* 1469 */         typeParam.addBound(_getType(cu, s));
/* 1470 */       } while (s.yyPeekCheckType() == 16777236);
/*      */     }
/*      */     
/* 1473 */     log("Exiting _getTypeParameter(): " + typeParam.getName());
/* 1474 */     return typeParam;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<TypeParameter> _getTypeParameters(CompilationUnit cu, Scanner s) throws IOException {
/* 1482 */     s.increaseTypeArgumentsLevel();
/* 1483 */     log("Entering _getTypeParameters() (" + s.getTypeArgumentsLevel() + ")");
/*      */     
/* 1485 */     s.markResetPosition();
/* 1486 */     Token t = s.yylexNonNull(16777219, "TypeParameters expected");
/*      */     
/* 1488 */     List<TypeParameter> typeParams = new ArrayList<>(1);
/*      */     
/*      */     do {
/* 1491 */       TypeParameter typeParam = _getTypeParameter(cu, s);
/* 1492 */       typeParams.add(typeParam);
/* 1493 */       t = s.yylexNonNull(8388616, 16777218, "',' or '>' expected");
/* 1494 */     } while (t.isType(8388616));
/*      */     
/* 1496 */     log("Exiting _getTypeParameters() (" + s.getTypeArgumentsLevel() + ")");
/* 1497 */     s.decreaseTypeArgumentsLevel();
/*      */     
/* 1499 */     return typeParams;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isDebug() {
/* 1505 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private int isModifier(Token t) {
/* 1510 */     switch (t.getType()) {
/*      */       case 65537:
/*      */       case 65554:
/*      */       case 65566:
/*      */       case 65569:
/*      */       case 65570:
/*      */       case 65571:
/*      */       case 65574:
/*      */       case 65575:
/*      */       case 65578:
/*      */       case 65582:
/*      */       case 65585:
/* 1522 */         return t.getType();
/*      */     } 
/* 1524 */     return -1;
/*      */   }
/*      */   
/*      */   private static void log(String msg) {}
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\parser\ASTFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */