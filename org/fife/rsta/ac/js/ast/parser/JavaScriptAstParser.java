/*     */ package org.fife.rsta.ac.js.ast.parser;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.fife.rsta.ac.js.JavaScriptHelper;
/*     */ import org.fife.rsta.ac.js.Logger;
/*     */ import org.fife.rsta.ac.js.SourceCompletionProvider;
/*     */ import org.fife.rsta.ac.js.ast.CodeBlock;
/*     */ import org.fife.rsta.ac.js.ast.JavaScriptFunctionDeclaration;
/*     */ import org.fife.rsta.ac.js.ast.JavaScriptVariableDeclaration;
/*     */ import org.fife.rsta.ac.js.ast.TypeDeclarationOptions;
/*     */ import org.fife.rsta.ac.js.ast.type.ArrayTypeDeclaration;
/*     */ import org.fife.rsta.ac.js.ast.type.TypeDeclaration;
/*     */ import org.fife.rsta.ac.js.completion.JavaScriptInScriptFunctionCompletion;
/*     */ import org.fife.rsta.ac.js.resolver.JavaScriptResolver;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.autocomplete.CompletionProvider;
/*     */ import org.fife.ui.autocomplete.ParameterizedCompletion;
/*     */ import org.mozilla.javascript.Node;
/*     */ import org.mozilla.javascript.ast.Assignment;
/*     */ import org.mozilla.javascript.ast.AstNode;
/*     */ import org.mozilla.javascript.ast.AstRoot;
/*     */ import org.mozilla.javascript.ast.CatchClause;
/*     */ import org.mozilla.javascript.ast.DoLoop;
/*     */ import org.mozilla.javascript.ast.ExpressionStatement;
/*     */ import org.mozilla.javascript.ast.ForInLoop;
/*     */ import org.mozilla.javascript.ast.ForLoop;
/*     */ import org.mozilla.javascript.ast.FunctionNode;
/*     */ import org.mozilla.javascript.ast.IfStatement;
/*     */ import org.mozilla.javascript.ast.InfixExpression;
/*     */ import org.mozilla.javascript.ast.Name;
/*     */ import org.mozilla.javascript.ast.NodeVisitor;
/*     */ import org.mozilla.javascript.ast.ReturnStatement;
/*     */ import org.mozilla.javascript.ast.SwitchCase;
/*     */ import org.mozilla.javascript.ast.SwitchStatement;
/*     */ import org.mozilla.javascript.ast.TryStatement;
/*     */ import org.mozilla.javascript.ast.VariableDeclaration;
/*     */ import org.mozilla.javascript.ast.VariableInitializer;
/*     */ import org.mozilla.javascript.ast.WhileLoop;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavaScriptAstParser
/*     */   extends JavaScriptParser
/*     */ {
/*     */   private ArrayList<ProcessFunctionType> functions;
/*     */   
/*     */   public JavaScriptAstParser(SourceCompletionProvider provider, int dot, TypeDeclarationOptions options) {
/*  51 */     super(provider, dot, options);
/*  52 */     this.functions = new ArrayList<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CodeBlock convertAstNodeToCodeBlock(AstRoot root, Set<Completion> set, String entered) {
/*  59 */     this.functions.clear();
/*  60 */     CodeBlock block = new CodeBlock(0);
/*  61 */     addCodeBlock((Node)root, set, entered, block, 2147483647);
/*  62 */     setFunctionValues();
/*  63 */     this.provider.getLanguageSupport().getJavaScriptParser()
/*  64 */       .setVariablesAndFunctions(this.provider.getVariableResolver());
/*  65 */     return block;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void setFunctionValues() {
/*  71 */     for (ProcessFunctionType type : this.functions) {
/*  72 */       type.dec.setTypeDeclaration(type.typeNode);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addCodeBlock(Node parent, Set<Completion> set, String entered, CodeBlock codeBlock, int offset) {
/*  90 */     if (parent == null) {
/*     */       return;
/*     */     }
/*  93 */     Node child = parent.getFirstChild();
/*     */     
/*  95 */     while (child != null) {
/*  96 */       CodeBlock childBlock = codeBlock;
/*  97 */       if (child instanceof AstNode) {
/*  98 */         AstNode node = (AstNode)child;
/*  99 */         int start = node.getAbsolutePosition();
/* 100 */         childBlock = codeBlock.addChildCodeBlock(start);
/* 101 */         childBlock.setEndOffset(offset);
/*     */       } 
/* 103 */       iterateNode((AstNode)child, set, entered, childBlock, offset);
/*     */       
/* 105 */       child = child.getNext();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void iterateNode(AstNode child, Set<Completion> set, String entered, CodeBlock block, int offset) {
/* 113 */     if (child == null) {
/*     */       return;
/*     */     }
/* 116 */     Logger.log(JavaScriptHelper.convertNodeToSource(child));
/* 117 */     Logger.log(child.shortName());
/*     */     
/* 119 */     if (JavaScriptHelper.isInfixOnly(child))
/*     */     {
/* 121 */       processInfix((Node)child, block, set, entered, offset);
/*     */     }
/*     */     
/* 124 */     switch (child.getType()) {
/*     */       case 109:
/* 126 */         processFunctionNode((Node)child, block, set, entered, offset);
/*     */       
/*     */       case 122:
/* 129 */         processVariableNode((Node)child, block, set, entered, offset);
/*     */       
/*     */       case 119:
/* 132 */         processForNode((Node)child, block, set, entered, offset);
/*     */ 
/*     */       
/*     */       case 117:
/* 136 */         processWhileNode((Node)child, block, set, entered, offset);
/*     */ 
/*     */       
/*     */       case 129:
/* 140 */         addCodeBlock((Node)child, set, entered, block, offset);
/*     */ 
/*     */       
/*     */       case 90:
/* 144 */         reassignVariable(child, block, offset);
/*     */ 
/*     */       
/*     */       case 133:
/* 148 */         processExpressionNode((Node)child, block, set, entered, offset);
/*     */ 
/*     */       
/*     */       case 112:
/* 152 */         processIfThenElse((Node)child, block, set, entered, offset);
/*     */ 
/*     */       
/*     */       case 81:
/* 156 */         processTryCatchNode((Node)child, block, set, entered, offset);
/*     */ 
/*     */       
/*     */       case 118:
/* 160 */         processDoNode((Node)child, block, set, entered, offset);
/*     */ 
/*     */       
/*     */       case 114:
/* 164 */         processSwitchNode((Node)child, block, set, entered, offset);
/*     */ 
/*     */       
/*     */       case 115:
/* 168 */         processCaseNode((Node)child, block, set, entered, offset);
/*     */ 
/*     */       
/*     */       case -1:
/*     */       case 4:
/*     */       case 30:
/*     */       case 33:
/*     */       case 38:
/*     */       case 39:
/*     */       case 74:
/*     */       case 120:
/*     */       case 121:
/*     */       case 124:
/*     */       case 128:
/*     */       case 147:
/*     */         return;
/*     */ 
/*     */       
/*     */       case 134:
/* 187 */         processExpressionStatement((Node)child, block, set, entered, offset);
/*     */     } 
/*     */ 
/*     */     
/* 191 */     Logger.log("Unhandled: " + child.getClass() + " (\"" + child
/* 192 */         .toString() + "\":" + child.getLineno());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processExpressionStatement(Node child, CodeBlock block, Set<Completion> set, String entered, int offset) {
/* 202 */     ExpressionStatement exp = (ExpressionStatement)child;
/*     */     
/* 204 */     AstNode expNode = exp.getExpression();
/* 205 */     iterateNode(expNode, set, entered, block, offset);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void reassignVariable(AstNode assign, CodeBlock block, int locationOffSet) {
/* 211 */     Assignment assignNode = (Assignment)assign;
/*     */     
/* 213 */     AstNode leftNode = assignNode.getLeft();
/*     */     
/* 215 */     AstNode rightNode = assignNode.getRight();
/*     */ 
/*     */     
/* 218 */     String name = (leftNode.getType() == 39) ? ((Name)leftNode).getIdentifier() : null;
/* 219 */     if (name != null) {
/* 220 */       int start = assignNode.getAbsolutePosition();
/* 221 */       int offset = start + assignNode.getLength();
/*     */ 
/*     */       
/* 224 */       if (offset <= this.dot) {
/*     */         
/* 226 */         JavaScriptVariableDeclaration dec = this.provider.getVariableResolver().findDeclaration(name, this.dot);
/* 227 */         if (dec != null && (dec
/* 228 */           .getCodeBlock() == null || dec.getCodeBlock()
/* 229 */           .contains(this.dot))) {
/*     */           
/* 231 */           dec.setTypeDeclaration(rightNode, isPreProcessing());
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 236 */           addVariableToResolver(leftNode, rightNode, block, locationOffSet);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addVariableToResolver(AstNode name, AstNode target, CodeBlock block, int offset) {
/* 246 */     JavaScriptVariableDeclaration dec = extractVariableFromNode(name, block, offset, target);
/*     */     
/* 248 */     if (dec != null && target != null && 
/* 249 */       JavaScriptHelper.canResolveVariable(name, target)) {
/* 250 */       dec.setTypeDeclaration(target);
/*     */     
/*     */     }
/* 253 */     else if (dec != null) {
/* 254 */       dec.setTypeDeclaration(this.provider.getTypesFactory().getDefaultTypeDeclaration());
/*     */     } 
/*     */     
/* 257 */     if (dec != null && 
/* 258 */       canAddVariable(block))
/*     */     {
/*     */       
/* 261 */       if (isPreProcessing()) {
/* 262 */         block.setStartOffSet(0);
/*     */         
/* 264 */         dec.setTypeDeclarationOptions(this.options);
/* 265 */         this.provider.getVariableResolver().addPreProcessingVariable(dec);
/*     */       } else {
/*     */         
/* 268 */         this.provider.getVariableResolver().addLocalVariable(dec);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processCaseNode(Node child, CodeBlock block, Set<Completion> set, String entered, int offset) {
/* 277 */     SwitchCase switchCase = (SwitchCase)child;
/* 278 */     List<AstNode> statements = switchCase.getStatements();
/* 279 */     int start = switchCase.getAbsolutePosition();
/* 280 */     offset = start + switchCase.getLength();
/* 281 */     if (canProcessNode((AstNode)switchCase)) {
/* 282 */       block = block.addChildCodeBlock(start);
/* 283 */       block.setEndOffset(offset);
/* 284 */       if (statements != null) {
/* 285 */         for (AstNode node : statements) {
/* 286 */           iterateNode(node, set, entered, block, offset);
/*     */         }
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processSwitchNode(Node child, CodeBlock block, Set<Completion> set, String entered, int offset) {
/* 296 */     SwitchStatement switchStatement = (SwitchStatement)child;
/* 297 */     if (canProcessNode((AstNode)switchStatement)) {
/* 298 */       List<SwitchCase> cases = switchStatement.getCases();
/* 299 */       if (cases != null) {
/* 300 */         for (SwitchCase switchCase : cases) {
/* 301 */           iterateNode((AstNode)switchCase, set, entered, block, offset);
/*     */         }
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processTryCatchNode(Node child, CodeBlock block, Set<Completion> set, String entered, int offset) {
/* 313 */     TryStatement tryStatement = (TryStatement)child;
/* 314 */     if (canProcessNode((AstNode)tryStatement)) {
/*     */       
/* 316 */       offset = tryStatement.getTryBlock().getAbsolutePosition() + tryStatement.getTryBlock().getLength();
/* 317 */       addCodeBlock((Node)tryStatement.getTryBlock(), set, entered, block, offset);
/*     */ 
/*     */       
/* 320 */       for (int i = 0; i < tryStatement.getCatchClauses().size(); i++) {
/*     */         
/* 322 */         CatchClause clause = tryStatement.getCatchClauses().get(i);
/* 323 */         if (canProcessNode((AstNode)clause)) {
/* 324 */           offset = clause.getAbsolutePosition() + clause.getLength();
/* 325 */           CodeBlock catchBlock = block.getParent().addChildCodeBlock(clause
/* 326 */               .getAbsolutePosition());
/* 327 */           catchBlock.setEndOffset(offset);
/* 328 */           Name name = clause.getVarName();
/*     */           
/* 330 */           JavaScriptVariableDeclaration dec = extractVariableFromNode((AstNode)name, catchBlock, offset);
/*     */           
/* 332 */           if (dec != null) {
/* 333 */             dec.setTypeDeclaration((AstNode)clause);
/*     */           }
/*     */           
/* 336 */           addCodeBlock((Node)clause.getBody(), set, entered, catchBlock, offset);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 341 */       if (tryStatement.getFinallyBlock() != null) {
/* 342 */         AstNode finallyNode = tryStatement.getFinallyBlock();
/* 343 */         if (canProcessNode(finallyNode)) {
/*     */           
/* 345 */           offset = finallyNode.getAbsolutePosition() + finallyNode.getLength();
/*     */           
/* 347 */           CodeBlock finallyBlock = block.getParent().addChildCodeBlock(tryStatement
/* 348 */               .getFinallyBlock()
/* 349 */               .getAbsolutePosition());
/* 350 */           addCodeBlock((Node)finallyNode, set, entered, finallyBlock, offset);
/*     */           
/* 352 */           finallyBlock.setEndOffset(offset);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processIfThenElse(Node child, CodeBlock block, Set<Completion> set, String entered, int offset) {
/* 364 */     IfStatement ifStatement = (IfStatement)child;
/* 365 */     if (canProcessNode((AstNode)ifStatement)) {
/*     */       
/* 367 */       offset = ifStatement.getAbsolutePosition() + ifStatement.getLength();
/* 368 */       addCodeBlock((Node)ifStatement.getThenPart(), set, entered, block, offset);
/* 369 */       AstNode elseNode = ifStatement.getElsePart();
/* 370 */       if (elseNode != null) {
/* 371 */         int start = elseNode.getAbsolutePosition();
/* 372 */         CodeBlock childBlock = block.addChildCodeBlock(start);
/* 373 */         offset = start + elseNode.getLength();
/* 374 */         iterateNode(elseNode, set, entered, childBlock, offset);
/* 375 */         childBlock.setEndOffset(offset);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processExpressionNode(Node child, CodeBlock block, Set<Completion> set, String entered, int offset) {
/* 387 */     if (child instanceof ExpressionStatement) {
/* 388 */       ExpressionStatement expr = (ExpressionStatement)child;
/* 389 */       iterateNode(expr.getExpression(), set, entered, block, offset);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processWhileNode(Node child, CodeBlock block, Set<Completion> set, String entered, int offset) {
/* 399 */     WhileLoop loop = (WhileLoop)child;
/* 400 */     if (canProcessNode((AstNode)loop)) {
/* 401 */       offset = loop.getAbsolutePosition() + loop.getLength();
/* 402 */       addCodeBlock((Node)loop.getBody(), set, entered, block, offset);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processDoNode(Node child, CodeBlock block, Set<Completion> set, String entered, int offset) {
/* 412 */     DoLoop loop = (DoLoop)child;
/* 413 */     if (canProcessNode((AstNode)loop)) {
/* 414 */       offset = loop.getAbsolutePosition() + loop.getLength();
/* 415 */       addCodeBlock((Node)loop.getBody(), set, entered, block, offset);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processInfix(Node child, CodeBlock block, Set<Completion> set, String entered, int offset) {
/* 425 */     InfixExpression epre = (InfixExpression)child;
/* 426 */     AstNode target = epre.getLeft();
/* 427 */     if (canProcessNode(target)) {
/* 428 */       extractVariableFromNode(target, block, offset);
/* 429 */       addCodeBlock((Node)epre, set, entered, block, offset);
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
/*     */   private void processFunctionNode(Node child, CodeBlock block, Set<Completion> set, String entered, int offset) {
/* 441 */     FunctionNode fn = (FunctionNode)child;
/* 442 */     String jsdoc = fn.getJsDoc();
/* 443 */     TypeDeclaration returnType = getFunctionType(fn);
/*     */     
/* 445 */     JavaScriptInScriptFunctionCompletion fc = new JavaScriptInScriptFunctionCompletion((CompletionProvider)this.provider, fn.getName(), returnType);
/* 446 */     fc.setShortDescription(jsdoc);
/* 447 */     offset = fn.getAbsolutePosition() + fn.getLength();
/*     */     
/* 449 */     if (fn.getParamCount() > 0) {
/* 450 */       List<AstNode> fnParams = fn.getParams();
/* 451 */       List<ParameterizedCompletion.Parameter> params = new ArrayList<>();
/* 452 */       for (int i = 0; i < fn.getParamCount(); i++) {
/* 453 */         String paramName = null;
/* 454 */         AstNode node = fnParams.get(i);
/* 455 */         switch (node.getType()) {
/*     */           case 39:
/* 457 */             paramName = ((Name)node).getIdentifier();
/*     */             break;
/*     */         } 
/*     */ 
/*     */         
/* 462 */         ParameterizedCompletion.Parameter param = new ParameterizedCompletion.Parameter(null, paramName);
/* 463 */         params.add(param);
/*     */         
/* 465 */         if (!isPreProcessing() && canProcessNode((AstNode)fn)) {
/* 466 */           JavaScriptVariableDeclaration dec = extractVariableFromNode(node, block, offset);
/* 467 */           this.provider.getVariableResolver().addLocalVariable(dec);
/*     */         } 
/*     */       } 
/* 470 */       fc.setParams(params);
/*     */     } 
/*     */     
/* 473 */     if (isPreProcessing()) {
/* 474 */       block.setStartOffSet(0);
/*     */     }
/*     */     
/* 477 */     if (isPreProcessing()) {
/* 478 */       JavaScriptFunctionDeclaration function = createJavaScriptFunction(fc.getLookupName(), offset, block, returnType, fn);
/* 479 */       function.setTypeDeclarationOptions(this.options);
/* 480 */       this.provider.getVariableResolver().addPreProcessingFunction(function);
/*     */     } else {
/*     */       
/* 483 */       this.provider.getVariableResolver().addLocalFunction(createJavaScriptFunction(fc.getLookupName(), offset, block, returnType, fn));
/*     */     } 
/*     */ 
/*     */     
/* 487 */     addCodeBlock((Node)fn.getBody(), set, entered, block, offset);
/*     */     
/* 489 */     if (entered.indexOf('.') == -1) {
/* 490 */       set.add(fc);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private JavaScriptFunctionDeclaration createJavaScriptFunction(String lookupName, int offset, CodeBlock block, TypeDeclaration returnType, FunctionNode fn) {
/* 496 */     Name name = fn.getFunctionName();
/* 497 */     JavaScriptFunctionDeclaration function = new JavaScriptFunctionDeclaration(lookupName, offset, block, returnType);
/* 498 */     if (name != null) {
/* 499 */       int start = name.getAbsolutePosition();
/* 500 */       int end = start + name.getLength();
/* 501 */       function.setStartOffset(start);
/* 502 */       function.setEndOffset(end);
/* 503 */       function.setFunctionName(fn.getName());
/*     */     } 
/*     */     
/* 506 */     return function;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean canProcessNode(AstNode node) {
/* 511 */     int start = node.getAbsolutePosition();
/* 512 */     int offset = start + node.getLength();
/* 513 */     return (this.dot >= start && this.dot < offset);
/*     */   }
/*     */ 
/*     */   
/*     */   private TypeDeclaration getFunctionType(FunctionNode fn) {
/* 518 */     FunctionReturnVisitor visitor = new FunctionReturnVisitor();
/* 519 */     fn.visit(visitor);
/* 520 */     return visitor.getCommonReturnType();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processVariableNode(Node child, CodeBlock block, Set<Completion> set, String entered, int offset) {
/* 530 */     if (block.contains(this.dot) || isPreProcessing()) {
/*     */       
/* 532 */       VariableDeclaration varDec = (VariableDeclaration)child;
/* 533 */       List<VariableInitializer> vars = varDec.getVariables();
/* 534 */       for (VariableInitializer var : vars) {
/* 535 */         extractVariableFromNode(var, block, offset);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processForNode(Node child, CodeBlock block, Set<Completion> set, String entered, int offset) {
/* 547 */     if (child instanceof ForLoop) {
/* 548 */       ForLoop loop = (ForLoop)child;
/* 549 */       offset = loop.getAbsolutePosition() + loop.getLength();
/* 550 */       if (canProcessNode((AstNode)loop)) {
/* 551 */         iterateNode(loop.getInitializer(), set, entered, block, offset);
/* 552 */         addCodeBlock((Node)loop.getBody(), set, entered, block, offset);
/*     */       }
/*     */     
/* 555 */     } else if (child instanceof ForInLoop) {
/* 556 */       ForInLoop loop = (ForInLoop)child;
/* 557 */       offset = loop.getAbsolutePosition() + loop.getLength();
/* 558 */       if (canProcessNode((AstNode)loop)) {
/* 559 */         AstNode iteratedObject = loop.getIteratedObject();
/* 560 */         AstNode iterator = loop.getIterator();
/* 561 */         if (iterator != null && 
/* 562 */           iterator.getType() == 122) {
/*     */           
/* 564 */           VariableDeclaration vd = (VariableDeclaration)iterator;
/* 565 */           List<VariableInitializer> variables = vd.getVariables();
/* 566 */           if (variables.size() == 1) {
/*     */             
/* 568 */             VariableInitializer vi = variables.get(0);
/* 569 */             if (loop.isForEach()) {
/* 570 */               extractVariableForForEach(vi, block, offset, iteratedObject);
/*     */             }
/*     */             else {
/*     */               
/* 574 */               extractVariableForForIn(vi, block, offset, iteratedObject);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 580 */         addCodeBlock((Node)loop.getBody(), set, entered, block, offset);
/*     */       } 
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
/*     */ 
/*     */   
/*     */   private void extractVariableFromNode(VariableInitializer initializer, CodeBlock block, int offset) {
/* 595 */     AstNode target = initializer.getTarget();
/*     */     
/* 597 */     if (target != null) {
/* 598 */       addVariableToResolver(target, initializer.getInitializer(), block, offset);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void extractVariableForForEach(VariableInitializer initializer, CodeBlock block, int offset, AstNode iteratedObject) {
/* 616 */     AstNode target = initializer.getTarget();
/* 617 */     if (target != null) {
/* 618 */       JavaScriptVariableDeclaration dec = extractVariableFromNode(target, block, offset);
/*     */       
/* 620 */       if (dec != null && iteratedObject != null && 
/*     */         
/* 622 */         JavaScriptHelper.canResolveVariable(target, iteratedObject)) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 627 */         JavaScriptResolver resolver = this.provider.getJavaScriptEngine().getJavaScriptResolver(this.provider);
/* 628 */         if (resolver != null) {
/*     */           
/* 630 */           TypeDeclaration iteratorDec = resolver.resolveNode(iteratedObject);
/* 631 */           if (iteratorDec instanceof ArrayTypeDeclaration) {
/*     */             
/* 633 */             dec
/* 634 */               .setTypeDeclaration(((ArrayTypeDeclaration)iteratorDec)
/* 635 */                 .getArrayType());
/*     */           } else {
/*     */             
/* 638 */             dec.setTypeDeclaration(iteratorDec);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 643 */         if (canAddVariable(block)) {
/* 644 */           this.provider.getVariableResolver().addLocalVariable(dec);
/*     */         }
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void extractVariableForForIn(VariableInitializer initializer, CodeBlock block, int offset, AstNode iteratedObject) {
/* 662 */     AstNode target = initializer.getTarget();
/* 663 */     if (target != null) {
/* 664 */       JavaScriptVariableDeclaration dec = extractVariableFromNode(target, block, offset);
/*     */       
/* 666 */       if (dec != null && iteratedObject != null && 
/*     */         
/* 668 */         JavaScriptHelper.canResolveVariable(target, iteratedObject)) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 673 */         JavaScriptResolver resolver = this.provider.getJavaScriptEngine().getJavaScriptResolver(this.provider);
/* 674 */         if (resolver != null) {
/*     */           
/* 676 */           TypeDeclaration iteratorDec = resolver.resolveNode(iteratedObject);
/* 677 */           if (iteratorDec instanceof ArrayTypeDeclaration) {
/*     */             
/* 679 */             dec.setTypeDeclaration(this.provider.getTypesFactory().getTypeDeclaration("JSNumber"));
/*     */           }
/*     */           else {
/*     */             
/* 683 */             dec.setTypeDeclaration(this.provider.getTypesFactory()
/* 684 */                 .getDefaultTypeDeclaration());
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 689 */         if (canAddVariable(block)) {
/* 690 */           this.provider.getVariableResolver().addLocalVariable(dec);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean canAddVariable(CodeBlock block) {
/* 698 */     if (!isPreProcessing()) {
/* 699 */       return true;
/*     */     }
/* 701 */     CodeBlock parent = block.getParent();
/* 702 */     return (parent != null && parent.getStartOffset() == 0);
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
/*     */   private JavaScriptVariableDeclaration extractVariableFromNode(AstNode node, CodeBlock block, int offset) {
/* 715 */     return extractVariableFromNode(node, block, offset, (AstNode)null);
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
/*     */   private JavaScriptVariableDeclaration extractVariableFromNode(AstNode node, CodeBlock block, int offset, AstNode initializer) {
/* 729 */     JavaScriptVariableDeclaration dec = null;
/*     */ 
/*     */     
/* 732 */     if (node != null)
/*     */     { Name name;
/* 734 */       switch (node.getType())
/*     */       { case 39:
/* 736 */           name = (Name)node;
/*     */           
/* 738 */           dec = new JavaScriptVariableDeclaration(name.getIdentifier(), offset, this.provider, block);
/* 739 */           dec.setStartOffset(name.getAbsolutePosition());
/* 740 */           dec.setEndOffset(name.getAbsolutePosition() + name.getLength());
/* 741 */           if (initializer != null && initializer
/* 742 */             .getType() == 38) {
/*     */ 
/*     */ 
/*     */             
/* 746 */             ProcessFunctionType func = new ProcessFunctionType();
/* 747 */             func.dec = dec;
/* 748 */             func.typeNode = initializer;
/* 749 */             this.functions.add(func);
/*     */           } 
/* 751 */           if (initializer == null || 
/* 752 */             JavaScriptHelper.canResolveVariable((AstNode)name, initializer))
/*     */           {
/* 754 */             block.addVariable(dec);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 764 */           return dec; }  Logger.log("... Unknown var target type: " + node.getClass()); }  return dec;
/*     */   }
/*     */ 
/*     */   
/*     */   public SourceCompletionProvider getProvider() {
/* 769 */     return this.provider;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDot() {
/* 774 */     return this.dot;
/*     */   }
/*     */   
/*     */   private class FunctionReturnVisitor
/*     */     implements NodeVisitor
/*     */   {
/* 780 */     private ArrayList<ReturnStatement> returnStatements = new ArrayList<>();
/*     */ 
/*     */     
/*     */     public boolean visit(AstNode node) {
/* 784 */       switch (node.getType()) {
/*     */         case 4:
/* 786 */           this.returnStatements.add((ReturnStatement)node);
/*     */           break;
/*     */       } 
/* 789 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public TypeDeclaration getCommonReturnType() {
/* 800 */       TypeDeclaration commonType = null;
/* 801 */       for (ReturnStatement rs : this.returnStatements) {
/* 802 */         AstNode returnValue = rs.getReturnValue();
/*     */ 
/*     */         
/* 805 */         TypeDeclaration type = JavaScriptAstParser.this.provider.getJavaScriptEngine().getJavaScriptResolver(JavaScriptAstParser.this.provider).resolveNode(returnValue);
/*     */         
/* 807 */         if (commonType == null) {
/* 808 */           commonType = type;
/*     */           continue;
/*     */         } 
/* 811 */         if (!commonType.equals(type)) {
/*     */           
/* 813 */           commonType = JavaScriptAstParser.this.provider.getTypesFactory().getDefaultTypeDeclaration();
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */       
/* 819 */       return commonType;
/*     */     }
/*     */     
/*     */     private FunctionReturnVisitor() {}
/*     */   }
/*     */   
/*     */   static class ProcessFunctionType {
/*     */     AstNode typeNode;
/*     */     JavaScriptVariableDeclaration dec;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\parser\JavaScriptAstParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */