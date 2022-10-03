/*      */ package org.mozilla.javascript;
/*      */ import java.util.List;
/*      */ import org.mozilla.javascript.ast.ArrayComprehension;
/*      */ import org.mozilla.javascript.ast.ArrayComprehensionLoop;
/*      */ import org.mozilla.javascript.ast.ArrayLiteral;
/*      */ import org.mozilla.javascript.ast.AstNode;
/*      */ import org.mozilla.javascript.ast.Block;
/*      */ import org.mozilla.javascript.ast.ElementGet;
/*      */ import org.mozilla.javascript.ast.ForInLoop;
/*      */ import org.mozilla.javascript.ast.ForLoop;
/*      */ import org.mozilla.javascript.ast.FunctionNode;
/*      */ import org.mozilla.javascript.ast.GeneratorExpression;
/*      */ import org.mozilla.javascript.ast.GeneratorExpressionLoop;
/*      */ import org.mozilla.javascript.ast.Jump;
/*      */ import org.mozilla.javascript.ast.Label;
/*      */ import org.mozilla.javascript.ast.LetNode;
/*      */ import org.mozilla.javascript.ast.Name;
/*      */ import org.mozilla.javascript.ast.ObjectLiteral;
/*      */ import org.mozilla.javascript.ast.ObjectProperty;
/*      */ import org.mozilla.javascript.ast.PropertyGet;
/*      */ import org.mozilla.javascript.ast.Scope;
/*      */ import org.mozilla.javascript.ast.ScriptNode;
/*      */ import org.mozilla.javascript.ast.UnaryExpression;
/*      */ import org.mozilla.javascript.ast.VariableDeclaration;
/*      */ import org.mozilla.javascript.ast.VariableInitializer;
/*      */ import org.mozilla.javascript.ast.XmlRef;
/*      */ 
/*      */ public final class IRFactory extends Parser {
/*      */   private static final int LOOP_DO_WHILE = 0;
/*   30 */   private Decompiler decompiler = new Decompiler(); private static final int LOOP_WHILE = 1; private static final int LOOP_FOR = 2;
/*      */   private static final int ALWAYS_TRUE_BOOLEAN = 1;
/*      */   private static final int ALWAYS_FALSE_BOOLEAN = -1;
/*      */   
/*      */   public IRFactory() {}
/*      */   
/*      */   public IRFactory(CompilerEnvirons env) {
/*   37 */     this(env, env.getErrorReporter());
/*      */   }
/*      */   
/*      */   public IRFactory(CompilerEnvirons env, ErrorReporter errorReporter) {
/*   41 */     super(env, errorReporter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ScriptNode transformTree(AstRoot root) {
/*   49 */     this.currentScriptOrFn = (ScriptNode)root;
/*   50 */     this.inUseStrictDirective = root.isInStrictMode();
/*   51 */     int sourceStartOffset = this.decompiler.getCurrentOffset();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   57 */     ScriptNode script = (ScriptNode)transform((AstNode)root);
/*      */     
/*   59 */     int sourceEndOffset = this.decompiler.getCurrentOffset();
/*   60 */     script.setEncodedSourceBounds(sourceStartOffset, sourceEndOffset);
/*      */ 
/*      */     
/*   63 */     if (this.compilerEnv.isGeneratingSource()) {
/*   64 */       script.setEncodedSource(this.decompiler.getEncodedSource());
/*      */     }
/*      */     
/*   67 */     this.decompiler = null;
/*   68 */     return script;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Node transform(AstNode node) {
/*   76 */     switch (node.getType()) {
/*      */       case 157:
/*   78 */         return transformArrayComp((ArrayComprehension)node);
/*      */       case 65:
/*   80 */         return transformArrayLiteral((ArrayLiteral)node);
/*      */       case 129:
/*   82 */         return transformBlock(node);
/*      */       case 120:
/*   84 */         return transformBreak((BreakStatement)node);
/*      */       case 38:
/*   86 */         return transformFunctionCall((FunctionCall)node);
/*      */       case 121:
/*   88 */         return transformContinue((ContinueStatement)node);
/*      */       case 118:
/*   90 */         return transformDoLoop((DoLoop)node);
/*      */       case 128:
/*   92 */         return (Node)node;
/*      */       case 119:
/*   94 */         if (node instanceof ForInLoop) {
/*   95 */           return transformForInLoop((ForInLoop)node);
/*      */         }
/*   97 */         return transformForLoop((ForLoop)node);
/*      */       
/*      */       case 109:
/*  100 */         return transformFunction((FunctionNode)node);
/*      */       case 162:
/*  102 */         return transformGenExpr((GeneratorExpression)node);
/*      */       case 36:
/*  104 */         return transformElementGet((ElementGet)node);
/*      */       case 33:
/*  106 */         return transformPropertyGet((PropertyGet)node);
/*      */       case 102:
/*  108 */         return transformCondExpr((ConditionalExpression)node);
/*      */       case 112:
/*  110 */         return transformIf((IfStatement)node);
/*      */       
/*      */       case 42:
/*      */       case 43:
/*      */       case 44:
/*      */       case 45:
/*      */       case 160:
/*  117 */         return transformLiteral(node);
/*      */       
/*      */       case 39:
/*  120 */         return transformName((Name)node);
/*      */       case 40:
/*  122 */         return transformNumber((NumberLiteral)node);
/*      */       case 30:
/*  124 */         return transformNewExpr((NewExpression)node);
/*      */       case 66:
/*  126 */         return transformObjectLiteral((ObjectLiteral)node);
/*      */       case 48:
/*  128 */         return transformRegExp((RegExpLiteral)node);
/*      */       case 4:
/*  130 */         return transformReturn((ReturnStatement)node);
/*      */       case 136:
/*  132 */         return transformScript((ScriptNode)node);
/*      */       case 41:
/*  134 */         return transformString((StringLiteral)node);
/*      */       case 114:
/*  136 */         return transformSwitch((SwitchStatement)node);
/*      */       case 50:
/*  138 */         return transformThrow((ThrowStatement)node);
/*      */       case 81:
/*  140 */         return transformTry((TryStatement)node);
/*      */       case 117:
/*  142 */         return transformWhileLoop((WhileLoop)node);
/*      */       case 123:
/*  144 */         return transformWith((WithStatement)node);
/*      */       case 72:
/*  146 */         return transformYield((Yield)node);
/*      */     } 
/*  148 */     if (node instanceof ExpressionStatement) {
/*  149 */       return transformExprStmt((ExpressionStatement)node);
/*      */     }
/*  151 */     if (node instanceof Assignment) {
/*  152 */       return transformAssignment((Assignment)node);
/*      */     }
/*  154 */     if (node instanceof UnaryExpression) {
/*  155 */       return transformUnary((UnaryExpression)node);
/*      */     }
/*  157 */     if (node instanceof XmlMemberGet) {
/*  158 */       return transformXmlMemberGet((XmlMemberGet)node);
/*      */     }
/*  160 */     if (node instanceof InfixExpression) {
/*  161 */       return transformInfix((InfixExpression)node);
/*      */     }
/*  163 */     if (node instanceof VariableDeclaration) {
/*  164 */       return transformVariables((VariableDeclaration)node);
/*      */     }
/*  166 */     if (node instanceof ParenthesizedExpression) {
/*  167 */       return transformParenExpr((ParenthesizedExpression)node);
/*      */     }
/*  169 */     if (node instanceof LabeledStatement) {
/*  170 */       return transformLabeledStatement((LabeledStatement)node);
/*      */     }
/*  172 */     if (node instanceof LetNode) {
/*  173 */       return transformLetNode((LetNode)node);
/*      */     }
/*  175 */     if (node instanceof XmlRef) {
/*  176 */       return transformXmlRef((XmlRef)node);
/*      */     }
/*  178 */     if (node instanceof XmlLiteral) {
/*  179 */       return transformXmlLiteral((XmlLiteral)node);
/*      */     }
/*  181 */     throw new IllegalArgumentException("Can't transform: " + node);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Node transformArrayComp(ArrayComprehension node) {
/*  206 */     int lineno = node.getLineno();
/*  207 */     Scope scopeNode = createScopeNode(157, lineno);
/*  208 */     String arrayName = this.currentScriptOrFn.getNextTempName();
/*  209 */     pushScope(scopeNode);
/*      */     try {
/*  211 */       defineSymbol(153, arrayName, false);
/*  212 */       Node block = new Node(129, lineno);
/*  213 */       Node newArray = createCallOrNew(30, createName("Array"));
/*  214 */       Node init = new Node(133, createAssignment(90, createName(arrayName), newArray), lineno);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  219 */       block.addChildToBack(init);
/*  220 */       block.addChildToBack(arrayCompTransformHelper(node, arrayName));
/*  221 */       scopeNode.addChildToBack(block);
/*  222 */       scopeNode.addChildToBack(createName(arrayName));
/*  223 */       return (Node)scopeNode;
/*      */     } finally {
/*  225 */       popScope();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private Node arrayCompTransformHelper(ArrayComprehension node, String arrayName) {
/*  231 */     this.decompiler.addToken(83);
/*  232 */     int lineno = node.getLineno();
/*  233 */     Node expr = transform(node.getResult());
/*      */     
/*  235 */     List<ArrayComprehensionLoop> loops = node.getLoops();
/*  236 */     int numLoops = loops.size();
/*      */ 
/*      */     
/*  239 */     Node[] iterators = new Node[numLoops];
/*  240 */     Node[] iteratedObjs = new Node[numLoops];
/*      */     
/*  242 */     for (int i = 0; i < numLoops; i++) {
/*  243 */       ArrayComprehensionLoop acl = loops.get(i);
/*  244 */       this.decompiler.addName(" ");
/*  245 */       this.decompiler.addToken(119);
/*  246 */       if (acl.isForEach()) {
/*  247 */         this.decompiler.addName("each ");
/*      */       }
/*  249 */       this.decompiler.addToken(87);
/*      */       
/*  251 */       AstNode iter = acl.getIterator();
/*  252 */       String name = null;
/*  253 */       if (iter.getType() == 39) {
/*  254 */         name = iter.getString();
/*  255 */         this.decompiler.addName(name);
/*      */       } else {
/*      */         
/*  258 */         decompile(iter);
/*  259 */         name = this.currentScriptOrFn.getNextTempName();
/*  260 */         defineSymbol(87, name, false);
/*  261 */         expr = createBinary(89, createAssignment(90, (Node)iter, createName(name)), expr);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  267 */       Node init = createName(name);
/*      */ 
/*      */       
/*  270 */       defineSymbol(153, name, false);
/*  271 */       iterators[i] = init;
/*      */       
/*  273 */       this.decompiler.addToken(52);
/*  274 */       iteratedObjs[i] = transform(acl.getIteratedObject());
/*  275 */       this.decompiler.addToken(88);
/*      */     } 
/*      */ 
/*      */     
/*  279 */     Node call = createCallOrNew(38, createPropertyGet(createName(arrayName), (String)null, "push", 0));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  284 */     Node body = new Node(133, call, lineno);
/*      */     
/*  286 */     if (node.getFilter() != null) {
/*  287 */       this.decompiler.addName(" ");
/*  288 */       this.decompiler.addToken(112);
/*  289 */       this.decompiler.addToken(87);
/*  290 */       body = createIf(transform(node.getFilter()), body, (Node)null, lineno);
/*  291 */       this.decompiler.addToken(88);
/*      */     } 
/*      */ 
/*      */     
/*  295 */     int pushed = 0; try {
/*      */       int j;
/*  297 */       for (j = numLoops - 1; j >= 0; j--) {
/*  298 */         ArrayComprehensionLoop acl = loops.get(j);
/*  299 */         Scope loop = createLoopNode((Node)null, acl.getLineno());
/*      */         
/*  301 */         pushScope(loop);
/*  302 */         pushed++;
/*  303 */         body = createForIn(153, (Node)loop, iterators[j], iteratedObjs[j], body, acl.isForEach());
/*      */       
/*      */       }
/*      */ 
/*      */     
/*      */     }
/*      */     finally {
/*      */       
/*  311 */       for (int j = 0; j < pushed; j++) {
/*  312 */         popScope();
/*      */       }
/*      */     } 
/*      */     
/*  316 */     this.decompiler.addToken(84);
/*      */ 
/*      */ 
/*      */     
/*  320 */     call.addChildToBack(expr);
/*  321 */     return body;
/*      */   }
/*      */   
/*      */   private Node transformArrayLiteral(ArrayLiteral node) {
/*  325 */     if (node.isDestructuring()) {
/*  326 */       return (Node)node;
/*      */     }
/*  328 */     this.decompiler.addToken(83);
/*  329 */     List<AstNode> elems = node.getElements();
/*  330 */     Node array = new Node(65);
/*  331 */     List<Integer> skipIndexes = null;
/*  332 */     for (int i = 0; i < elems.size(); i++) {
/*  333 */       AstNode elem = elems.get(i);
/*  334 */       if (elem.getType() != 128) {
/*  335 */         array.addChildToBack(transform(elem));
/*      */       } else {
/*  337 */         if (skipIndexes == null) {
/*  338 */           skipIndexes = new ArrayList<Integer>();
/*      */         }
/*  340 */         skipIndexes.add(Integer.valueOf(i));
/*      */       } 
/*  342 */       if (i < elems.size() - 1)
/*  343 */         this.decompiler.addToken(89); 
/*      */     } 
/*  345 */     this.decompiler.addToken(84);
/*  346 */     array.putIntProp(21, node.getDestructuringLength());
/*      */     
/*  348 */     if (skipIndexes != null) {
/*  349 */       int[] skips = new int[skipIndexes.size()];
/*  350 */       for (int j = 0; j < skipIndexes.size(); j++)
/*  351 */         skips[j] = ((Integer)skipIndexes.get(j)).intValue(); 
/*  352 */       array.putProp(11, skips);
/*      */     } 
/*  354 */     return array;
/*      */   }
/*      */   
/*      */   private Node transformAssignment(Assignment node) {
/*  358 */     AstNode left = removeParens(node.getLeft());
/*  359 */     Node target = null;
/*  360 */     if (isDestructuring((Node)left)) {
/*  361 */       decompile(left);
/*  362 */       AstNode astNode = left;
/*      */     } else {
/*  364 */       target = transform(left);
/*      */     } 
/*  366 */     this.decompiler.addToken(node.getType());
/*  367 */     return createAssignment(node.getType(), target, transform(node.getRight()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Node transformBlock(AstNode node) {
/*  373 */     if (node instanceof Scope) {
/*  374 */       pushScope((Scope)node);
/*      */     }
/*      */     try {
/*  377 */       List<Node> kids = new ArrayList<Node>();
/*  378 */       for (Node kid : node) {
/*  379 */         kids.add(transform((AstNode)kid));
/*      */       }
/*  381 */       node.removeChildren();
/*  382 */       for (Node kid : kids) {
/*  383 */         node.addChildToBack(kid);
/*      */       }
/*  385 */       return (Node)node;
/*      */     } finally {
/*  387 */       if (node instanceof Scope) {
/*  388 */         popScope();
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private Node transformBreak(BreakStatement node) {
/*  394 */     this.decompiler.addToken(120);
/*  395 */     if (node.getBreakLabel() != null) {
/*  396 */       this.decompiler.addName(node.getBreakLabel().getIdentifier());
/*      */     }
/*  398 */     this.decompiler.addEOL(82);
/*  399 */     return (Node)node;
/*      */   }
/*      */   
/*      */   private Node transformCondExpr(ConditionalExpression node) {
/*  403 */     Node test = transform(node.getTestExpression());
/*  404 */     this.decompiler.addToken(102);
/*  405 */     Node ifTrue = transform(node.getTrueExpression());
/*  406 */     this.decompiler.addToken(103);
/*  407 */     Node ifFalse = transform(node.getFalseExpression());
/*  408 */     return createCondExpr(test, ifTrue, ifFalse);
/*      */   }
/*      */   
/*      */   private Node transformContinue(ContinueStatement node) {
/*  412 */     this.decompiler.addToken(121);
/*  413 */     if (node.getLabel() != null) {
/*  414 */       this.decompiler.addName(node.getLabel().getIdentifier());
/*      */     }
/*  416 */     this.decompiler.addEOL(82);
/*  417 */     return (Node)node;
/*      */   }
/*      */   
/*      */   private Node transformDoLoop(DoLoop loop) {
/*  421 */     loop.setType(132);
/*  422 */     pushScope((Scope)loop);
/*      */     try {
/*  424 */       this.decompiler.addToken(118);
/*  425 */       this.decompiler.addEOL(85);
/*  426 */       Node body = transform(loop.getBody());
/*  427 */       this.decompiler.addToken(86);
/*  428 */       this.decompiler.addToken(117);
/*  429 */       this.decompiler.addToken(87);
/*  430 */       Node cond = transform(loop.getCondition());
/*  431 */       this.decompiler.addToken(88);
/*  432 */       this.decompiler.addEOL(82);
/*  433 */       return createLoop((Jump)loop, 0, body, cond, (Node)null, (Node)null);
/*      */     } finally {
/*      */       
/*  436 */       popScope();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Node transformElementGet(ElementGet node) {
/*  443 */     Node target = transform(node.getTarget());
/*  444 */     this.decompiler.addToken(83);
/*  445 */     Node element = transform(node.getElement());
/*  446 */     this.decompiler.addToken(84);
/*  447 */     return new Node(36, target, element);
/*      */   }
/*      */   
/*      */   private Node transformExprStmt(ExpressionStatement node) {
/*  451 */     Node expr = transform(node.getExpression());
/*  452 */     this.decompiler.addEOL(82);
/*  453 */     return new Node(node.getType(), expr, node.getLineno());
/*      */   }
/*      */   
/*      */   private Node transformForInLoop(ForInLoop loop) {
/*  457 */     this.decompiler.addToken(119);
/*  458 */     if (loop.isForEach())
/*  459 */       this.decompiler.addName("each "); 
/*  460 */     this.decompiler.addToken(87);
/*      */     
/*  462 */     loop.setType(132);
/*  463 */     pushScope((Scope)loop);
/*      */     try {
/*  465 */       int declType = -1;
/*  466 */       AstNode iter = loop.getIterator();
/*  467 */       if (iter instanceof VariableDeclaration) {
/*  468 */         declType = ((VariableDeclaration)iter).getType();
/*      */       }
/*  470 */       Node lhs = transform(iter);
/*  471 */       this.decompiler.addToken(52);
/*  472 */       Node obj = transform(loop.getIteratedObject());
/*  473 */       this.decompiler.addToken(88);
/*  474 */       this.decompiler.addEOL(85);
/*  475 */       Node body = transform(loop.getBody());
/*  476 */       this.decompiler.addEOL(86);
/*  477 */       return createForIn(declType, (Node)loop, lhs, obj, body, loop.isForEach());
/*      */     } finally {
/*      */       
/*  480 */       popScope();
/*      */     } 
/*      */   }
/*      */   
/*      */   private Node transformForLoop(ForLoop loop) {
/*  485 */     this.decompiler.addToken(119);
/*  486 */     this.decompiler.addToken(87);
/*  487 */     loop.setType(132);
/*      */ 
/*      */     
/*  490 */     Scope savedScope = this.currentScope;
/*  491 */     this.currentScope = (Scope)loop;
/*      */     try {
/*  493 */       Node init = transform(loop.getInitializer());
/*  494 */       this.decompiler.addToken(82);
/*  495 */       Node test = transform(loop.getCondition());
/*  496 */       this.decompiler.addToken(82);
/*  497 */       Node incr = transform(loop.getIncrement());
/*  498 */       this.decompiler.addToken(88);
/*  499 */       this.decompiler.addEOL(85);
/*  500 */       Node body = transform(loop.getBody());
/*  501 */       this.decompiler.addEOL(86);
/*  502 */       return createFor((Scope)loop, init, test, incr, body);
/*      */     } finally {
/*  504 */       this.currentScope = savedScope;
/*      */     } 
/*      */   }
/*      */   
/*      */   private Node transformFunction(FunctionNode fn) {
/*  509 */     int functionType = fn.getFunctionType();
/*  510 */     int start = this.decompiler.markFunctionStart(functionType);
/*  511 */     Node mexpr = decompileFunctionHeader(fn);
/*  512 */     int index = this.currentScriptOrFn.addFunction(fn);
/*      */     
/*  514 */     Parser.PerFunctionVariables savedVars = new Parser.PerFunctionVariables(this, fn);
/*      */ 
/*      */     
/*      */     try {
/*  518 */       Node destructuring = (Node)fn.getProp(23);
/*  519 */       fn.removeProp(23);
/*      */       
/*  521 */       int lineno = fn.getBody().getLineno();
/*  522 */       this.nestingOfFunction++;
/*  523 */       Node body = transform(fn.getBody());
/*      */       
/*  525 */       if (!fn.isExpressionClosure()) {
/*  526 */         this.decompiler.addToken(86);
/*      */       }
/*  528 */       fn.setEncodedSourceBounds(start, this.decompiler.markFunctionEnd(start));
/*      */       
/*  530 */       if (functionType != 2 && !fn.isExpressionClosure())
/*      */       {
/*      */         
/*  533 */         this.decompiler.addToken(1);
/*      */       }
/*      */       
/*  536 */       if (destructuring != null) {
/*  537 */         body.addChildToFront(new Node(133, destructuring, lineno));
/*      */       }
/*      */ 
/*      */       
/*  541 */       int syntheticType = fn.getFunctionType();
/*  542 */       Node pn = initFunction(fn, index, body, syntheticType);
/*  543 */       if (mexpr != null) {
/*  544 */         pn = createAssignment(90, mexpr, pn);
/*  545 */         if (syntheticType != 2) {
/*  546 */           pn = createExprStatementNoReturn(pn, fn.getLineno());
/*      */         }
/*      */       } 
/*  549 */       return pn;
/*      */     } finally {
/*      */       
/*  552 */       this.nestingOfFunction--;
/*  553 */       savedVars.restore();
/*      */     } 
/*      */   }
/*      */   
/*      */   private Node transformFunctionCall(FunctionCall node) {
/*  558 */     Node call = createCallOrNew(38, transform(node.getTarget()));
/*  559 */     call.setLineno(node.getLineno());
/*  560 */     this.decompiler.addToken(87);
/*  561 */     List<AstNode> args = node.getArguments();
/*  562 */     for (int i = 0; i < args.size(); i++) {
/*  563 */       AstNode arg = args.get(i);
/*  564 */       call.addChildToBack(transform(arg));
/*  565 */       if (i < args.size() - 1) {
/*  566 */         this.decompiler.addToken(89);
/*      */       }
/*      */     } 
/*  569 */     this.decompiler.addToken(88);
/*  570 */     return call;
/*      */   }
/*      */ 
/*      */   
/*      */   private Node transformGenExpr(GeneratorExpression node) {
/*      */     Node pn;
/*  576 */     FunctionNode fn = new FunctionNode();
/*  577 */     fn.setSourceName(this.currentScriptOrFn.getNextTempName());
/*  578 */     fn.setIsGenerator();
/*  579 */     fn.setFunctionType(2);
/*  580 */     fn.setRequiresActivation();
/*      */     
/*  582 */     int functionType = fn.getFunctionType();
/*  583 */     int start = this.decompiler.markFunctionStart(functionType);
/*  584 */     Node mexpr = decompileFunctionHeader(fn);
/*  585 */     int index = this.currentScriptOrFn.addFunction(fn);
/*      */     
/*  587 */     Parser.PerFunctionVariables savedVars = new Parser.PerFunctionVariables(this, fn);
/*      */ 
/*      */     
/*      */     try {
/*  591 */       Node destructuring = (Node)fn.getProp(23);
/*  592 */       fn.removeProp(23);
/*      */       
/*  594 */       int lineno = node.lineno;
/*  595 */       this.nestingOfFunction++;
/*  596 */       Node body = genExprTransformHelper(node);
/*      */       
/*  598 */       if (!fn.isExpressionClosure()) {
/*  599 */         this.decompiler.addToken(86);
/*      */       }
/*  601 */       fn.setEncodedSourceBounds(start, this.decompiler.markFunctionEnd(start));
/*      */       
/*  603 */       if (functionType != 2 && !fn.isExpressionClosure())
/*      */       {
/*      */         
/*  606 */         this.decompiler.addToken(1);
/*      */       }
/*      */       
/*  609 */       if (destructuring != null) {
/*  610 */         body.addChildToFront(new Node(133, destructuring, lineno));
/*      */       }
/*      */ 
/*      */       
/*  614 */       int syntheticType = fn.getFunctionType();
/*  615 */       pn = initFunction(fn, index, body, syntheticType);
/*  616 */       if (mexpr != null) {
/*  617 */         pn = createAssignment(90, mexpr, pn);
/*  618 */         if (syntheticType != 2) {
/*  619 */           pn = createExprStatementNoReturn(pn, fn.getLineno());
/*      */         }
/*      */       } 
/*      */     } finally {
/*  623 */       this.nestingOfFunction--;
/*  624 */       savedVars.restore();
/*      */     } 
/*      */     
/*  627 */     Node call = createCallOrNew(38, pn);
/*  628 */     call.setLineno(node.getLineno());
/*  629 */     this.decompiler.addToken(87);
/*  630 */     this.decompiler.addToken(88);
/*  631 */     return call;
/*      */   }
/*      */   
/*      */   private Node genExprTransformHelper(GeneratorExpression node) {
/*  635 */     this.decompiler.addToken(87);
/*  636 */     int lineno = node.getLineno();
/*  637 */     Node expr = transform(node.getResult());
/*      */     
/*  639 */     List<GeneratorExpressionLoop> loops = node.getLoops();
/*  640 */     int numLoops = loops.size();
/*      */ 
/*      */     
/*  643 */     Node[] iterators = new Node[numLoops];
/*  644 */     Node[] iteratedObjs = new Node[numLoops];
/*      */     
/*  646 */     for (int i = 0; i < numLoops; i++) {
/*  647 */       GeneratorExpressionLoop acl = loops.get(i);
/*  648 */       this.decompiler.addName(" ");
/*  649 */       this.decompiler.addToken(119);
/*  650 */       this.decompiler.addToken(87);
/*      */       
/*  652 */       AstNode iter = acl.getIterator();
/*  653 */       String name = null;
/*  654 */       if (iter.getType() == 39) {
/*  655 */         name = iter.getString();
/*  656 */         this.decompiler.addName(name);
/*      */       } else {
/*      */         
/*  659 */         decompile(iter);
/*  660 */         name = this.currentScriptOrFn.getNextTempName();
/*  661 */         defineSymbol(87, name, false);
/*  662 */         expr = createBinary(89, createAssignment(90, (Node)iter, createName(name)), expr);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  668 */       Node init = createName(name);
/*      */ 
/*      */       
/*  671 */       defineSymbol(153, name, false);
/*  672 */       iterators[i] = init;
/*      */       
/*  674 */       this.decompiler.addToken(52);
/*  675 */       iteratedObjs[i] = transform(acl.getIteratedObject());
/*  676 */       this.decompiler.addToken(88);
/*      */     } 
/*      */ 
/*      */     
/*  680 */     Node yield = new Node(72, expr, node.getLineno());
/*      */     
/*  682 */     Node body = new Node(133, yield, lineno);
/*      */     
/*  684 */     if (node.getFilter() != null) {
/*  685 */       this.decompiler.addName(" ");
/*  686 */       this.decompiler.addToken(112);
/*  687 */       this.decompiler.addToken(87);
/*  688 */       body = createIf(transform(node.getFilter()), body, (Node)null, lineno);
/*  689 */       this.decompiler.addToken(88);
/*      */     } 
/*      */ 
/*      */     
/*  693 */     int pushed = 0; try {
/*      */       int j;
/*  695 */       for (j = numLoops - 1; j >= 0; j--) {
/*  696 */         GeneratorExpressionLoop acl = loops.get(j);
/*  697 */         Scope loop = createLoopNode((Node)null, acl.getLineno());
/*      */         
/*  699 */         pushScope(loop);
/*  700 */         pushed++;
/*  701 */         body = createForIn(153, (Node)loop, iterators[j], iteratedObjs[j], body, acl.isForEach());
/*      */       
/*      */       }
/*      */ 
/*      */     
/*      */     }
/*      */     finally {
/*      */       
/*  709 */       for (int j = 0; j < pushed; j++) {
/*  710 */         popScope();
/*      */       }
/*      */     } 
/*      */     
/*  714 */     this.decompiler.addToken(88);
/*      */     
/*  716 */     return body;
/*      */   }
/*      */   
/*      */   private Node transformIf(IfStatement n) {
/*  720 */     this.decompiler.addToken(112);
/*  721 */     this.decompiler.addToken(87);
/*  722 */     Node cond = transform(n.getCondition());
/*  723 */     this.decompiler.addToken(88);
/*  724 */     this.decompiler.addEOL(85);
/*  725 */     Node ifTrue = transform(n.getThenPart());
/*  726 */     Node ifFalse = null;
/*  727 */     if (n.getElsePart() != null) {
/*  728 */       this.decompiler.addToken(86);
/*  729 */       this.decompiler.addToken(113);
/*  730 */       this.decompiler.addEOL(85);
/*  731 */       ifFalse = transform(n.getElsePart());
/*      */     } 
/*  733 */     this.decompiler.addEOL(86);
/*  734 */     return createIf(cond, ifTrue, ifFalse, n.getLineno());
/*      */   }
/*      */   
/*      */   private Node transformInfix(InfixExpression node) {
/*  738 */     Node left = transform(node.getLeft());
/*  739 */     this.decompiler.addToken(node.getType());
/*  740 */     Node right = transform(node.getRight());
/*  741 */     if (node instanceof org.mozilla.javascript.ast.XmlDotQuery) {
/*  742 */       this.decompiler.addToken(88);
/*      */     }
/*  744 */     return createBinary(node.getType(), left, right);
/*      */   }
/*      */   
/*      */   private Node transformLabeledStatement(LabeledStatement ls) {
/*  748 */     Label label = ls.getFirstLabel();
/*  749 */     List<Label> labels = ls.getLabels();
/*  750 */     this.decompiler.addName(label.getName());
/*  751 */     if (labels.size() > 1)
/*      */     {
/*  753 */       for (Label lb : labels.subList(1, labels.size())) {
/*  754 */         this.decompiler.addEOL(103);
/*  755 */         this.decompiler.addName(lb.getName());
/*      */       } 
/*      */     }
/*  758 */     if (ls.getStatement().getType() == 129) {
/*      */       
/*  760 */       this.decompiler.addToken(66);
/*  761 */       this.decompiler.addEOL(85);
/*      */     } else {
/*  763 */       this.decompiler.addEOL(103);
/*      */     } 
/*  765 */     Node statement = transform(ls.getStatement());
/*  766 */     if (ls.getStatement().getType() == 129) {
/*  767 */       this.decompiler.addEOL(86);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  772 */     Node breakTarget = Node.newTarget();
/*  773 */     Node block = new Node(129, (Node)label, statement, breakTarget);
/*  774 */     label.target = breakTarget;
/*      */     
/*  776 */     return block;
/*      */   }
/*      */   
/*      */   private Node transformLetNode(LetNode node) {
/*  780 */     pushScope((Scope)node);
/*      */     try {
/*  782 */       this.decompiler.addToken(153);
/*  783 */       this.decompiler.addToken(87);
/*  784 */       Node vars = transformVariableInitializers(node.getVariables());
/*  785 */       this.decompiler.addToken(88);
/*  786 */       node.addChildToBack(vars);
/*  787 */       boolean letExpr = (node.getType() == 158);
/*  788 */       if (node.getBody() != null) {
/*  789 */         if (letExpr) {
/*  790 */           this.decompiler.addName(" ");
/*      */         } else {
/*  792 */           this.decompiler.addEOL(85);
/*      */         } 
/*  794 */         node.addChildToBack(transform(node.getBody()));
/*  795 */         if (!letExpr) {
/*  796 */           this.decompiler.addEOL(86);
/*      */         }
/*      */       } 
/*  799 */       return (Node)node;
/*      */     } finally {
/*  801 */       popScope();
/*      */     } 
/*      */   }
/*      */   
/*      */   private Node transformLiteral(AstNode node) {
/*  806 */     this.decompiler.addToken(node.getType());
/*  807 */     return (Node)node;
/*      */   }
/*      */   
/*      */   private Node transformName(Name node) {
/*  811 */     this.decompiler.addName(node.getIdentifier());
/*  812 */     return (Node)node;
/*      */   }
/*      */   
/*      */   private Node transformNewExpr(NewExpression node) {
/*  816 */     this.decompiler.addToken(30);
/*  817 */     Node nx = createCallOrNew(30, transform(node.getTarget()));
/*  818 */     nx.setLineno(node.getLineno());
/*  819 */     List<AstNode> args = node.getArguments();
/*  820 */     this.decompiler.addToken(87);
/*  821 */     for (int i = 0; i < args.size(); i++) {
/*  822 */       AstNode arg = args.get(i);
/*  823 */       nx.addChildToBack(transform(arg));
/*  824 */       if (i < args.size() - 1) {
/*  825 */         this.decompiler.addToken(89);
/*      */       }
/*      */     } 
/*  828 */     this.decompiler.addToken(88);
/*  829 */     if (node.getInitializer() != null) {
/*  830 */       nx.addChildToBack(transformObjectLiteral(node.getInitializer()));
/*      */     }
/*  832 */     return nx;
/*      */   }
/*      */   
/*      */   private Node transformNumber(NumberLiteral node) {
/*  836 */     this.decompiler.addNumber(node.getNumber());
/*  837 */     return (Node)node;
/*      */   }
/*      */   private Node transformObjectLiteral(ObjectLiteral node) {
/*      */     Object[] properties;
/*  841 */     if (node.isDestructuring()) {
/*  842 */       return (Node)node;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  847 */     this.decompiler.addToken(85);
/*  848 */     List<ObjectProperty> elems = node.getElements();
/*  849 */     Node object = new Node(66);
/*      */     
/*  851 */     if (elems.isEmpty()) {
/*  852 */       properties = ScriptRuntime.emptyArgs;
/*      */     } else {
/*  854 */       int size = elems.size(), i = 0;
/*  855 */       properties = new Object[size];
/*  856 */       for (ObjectProperty prop : elems) {
/*  857 */         if (prop.isGetter()) {
/*  858 */           this.decompiler.addToken(151);
/*  859 */         } else if (prop.isSetter()) {
/*  860 */           this.decompiler.addToken(152);
/*      */         } 
/*      */         
/*  863 */         properties[i++] = getPropKey((Node)prop.getLeft());
/*      */ 
/*      */ 
/*      */         
/*  867 */         if (!prop.isGetter() && !prop.isSetter()) {
/*  868 */           this.decompiler.addToken(66);
/*      */         }
/*      */         
/*  871 */         Node right = transform(prop.getRight());
/*  872 */         if (prop.isGetter()) {
/*  873 */           right = createUnary(151, right);
/*  874 */         } else if (prop.isSetter()) {
/*  875 */           right = createUnary(152, right);
/*      */         } 
/*  877 */         object.addChildToBack(right);
/*      */         
/*  879 */         if (i < size) {
/*  880 */           this.decompiler.addToken(89);
/*      */         }
/*      */       } 
/*      */     } 
/*  884 */     this.decompiler.addToken(86);
/*  885 */     object.putProp(12, properties);
/*  886 */     return object;
/*      */   }
/*      */   
/*      */   private Object getPropKey(Node id) {
/*      */     Object key;
/*  891 */     if (id instanceof Name) {
/*  892 */       String s = ((Name)id).getIdentifier();
/*  893 */       this.decompiler.addName(s);
/*  894 */       key = ScriptRuntime.getIndexObject(s);
/*  895 */     } else if (id instanceof StringLiteral) {
/*  896 */       String s = ((StringLiteral)id).getValue();
/*  897 */       this.decompiler.addString(s);
/*  898 */       key = ScriptRuntime.getIndexObject(s);
/*  899 */     } else if (id instanceof NumberLiteral) {
/*  900 */       double n = ((NumberLiteral)id).getNumber();
/*  901 */       this.decompiler.addNumber(n);
/*  902 */       key = ScriptRuntime.getIndexObject(n);
/*      */     } else {
/*  904 */       throw Kit.codeBug();
/*      */     } 
/*  906 */     return key;
/*      */   }
/*      */   
/*      */   private Node transformParenExpr(ParenthesizedExpression node) {
/*  910 */     AstNode expr = node.getExpression();
/*  911 */     this.decompiler.addToken(87);
/*  912 */     int count = 1;
/*  913 */     while (expr instanceof ParenthesizedExpression) {
/*  914 */       this.decompiler.addToken(87);
/*  915 */       count++;
/*  916 */       expr = ((ParenthesizedExpression)expr).getExpression();
/*      */     } 
/*  918 */     Node result = transform(expr);
/*  919 */     for (int i = 0; i < count; i++) {
/*  920 */       this.decompiler.addToken(88);
/*      */     }
/*  922 */     result.putProp(19, Boolean.TRUE);
/*  923 */     return result;
/*      */   }
/*      */   
/*      */   private Node transformPropertyGet(PropertyGet node) {
/*  927 */     Node target = transform(node.getTarget());
/*  928 */     String name = node.getProperty().getIdentifier();
/*  929 */     this.decompiler.addToken(108);
/*  930 */     this.decompiler.addName(name);
/*  931 */     return createPropertyGet(target, (String)null, name, 0);
/*      */   }
/*      */   
/*      */   private Node transformRegExp(RegExpLiteral node) {
/*  935 */     this.decompiler.addRegexp(node.getValue(), node.getFlags());
/*  936 */     this.currentScriptOrFn.addRegExp(node);
/*  937 */     return (Node)node;
/*      */   }
/*      */   
/*      */   private Node transformReturn(ReturnStatement node) {
/*  941 */     boolean expClosure = Boolean.TRUE.equals(node.getProp(25));
/*  942 */     if (expClosure) {
/*  943 */       this.decompiler.addName(" ");
/*      */     } else {
/*  945 */       this.decompiler.addToken(4);
/*      */     } 
/*  947 */     AstNode rv = node.getReturnValue();
/*  948 */     Node value = (rv == null) ? null : transform(rv);
/*  949 */     if (!expClosure) this.decompiler.addEOL(82); 
/*  950 */     return (rv == null) ? new Node(4, node.getLineno()) : new Node(4, value, node.getLineno());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Node transformScript(ScriptNode node) {
/*  956 */     this.decompiler.addToken(136);
/*  957 */     if (this.currentScope != null) Kit.codeBug(); 
/*  958 */     this.currentScope = (Scope)node;
/*  959 */     Node body = new Node(129);
/*  960 */     for (Node kid : node) {
/*  961 */       body.addChildToBack(transform((AstNode)kid));
/*      */     }
/*  963 */     node.removeChildren();
/*  964 */     Node children = body.getFirstChild();
/*  965 */     if (children != null) {
/*  966 */       node.addChildrenToBack(children);
/*      */     }
/*  968 */     return (Node)node;
/*      */   }
/*      */   
/*      */   private Node transformString(StringLiteral node) {
/*  972 */     this.decompiler.addString(node.getValue());
/*  973 */     return Node.newString(node.getValue());
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
/*      */   private Node transformSwitch(SwitchStatement node) {
/* 1016 */     this.decompiler.addToken(114);
/* 1017 */     this.decompiler.addToken(87);
/* 1018 */     Node switchExpr = transform(node.getExpression());
/* 1019 */     this.decompiler.addToken(88);
/* 1020 */     node.addChildToBack(switchExpr);
/*      */     
/* 1022 */     Node block = new Node(129, (Node)node, node.getLineno());
/* 1023 */     this.decompiler.addEOL(85);
/*      */     
/* 1025 */     for (SwitchCase sc : node.getCases()) {
/* 1026 */       AstNode expr = sc.getExpression();
/* 1027 */       Node caseExpr = null;
/*      */       
/* 1029 */       if (expr != null) {
/* 1030 */         this.decompiler.addToken(115);
/* 1031 */         caseExpr = transform(expr);
/*      */       } else {
/* 1033 */         this.decompiler.addToken(116);
/*      */       } 
/* 1035 */       this.decompiler.addEOL(103);
/*      */       
/* 1037 */       List<AstNode> stmts = sc.getStatements();
/* 1038 */       Block block1 = new Block();
/* 1039 */       if (stmts != null) {
/* 1040 */         for (AstNode kid : stmts) {
/* 1041 */           block1.addChildToBack(transform(kid));
/*      */         }
/*      */       }
/* 1044 */       addSwitchCase(block, caseExpr, (Node)block1);
/*      */     } 
/* 1046 */     this.decompiler.addEOL(86);
/* 1047 */     closeSwitch(block);
/* 1048 */     return block;
/*      */   }
/*      */   
/*      */   private Node transformThrow(ThrowStatement node) {
/* 1052 */     this.decompiler.addToken(50);
/* 1053 */     Node value = transform(node.getExpression());
/* 1054 */     this.decompiler.addEOL(82);
/* 1055 */     return new Node(50, value, node.getLineno());
/*      */   }
/*      */   
/*      */   private Node transformTry(TryStatement node) {
/* 1059 */     this.decompiler.addToken(81);
/* 1060 */     this.decompiler.addEOL(85);
/* 1061 */     Node tryBlock = transform(node.getTryBlock());
/* 1062 */     this.decompiler.addEOL(86);
/*      */     
/* 1064 */     Block block = new Block();
/* 1065 */     for (CatchClause cc : node.getCatchClauses()) {
/* 1066 */       EmptyExpression emptyExpression; this.decompiler.addToken(124);
/* 1067 */       this.decompiler.addToken(87);
/*      */       
/* 1069 */       String varName = cc.getVarName().getIdentifier();
/* 1070 */       this.decompiler.addName(varName);
/*      */       
/* 1072 */       Node catchCond = null;
/* 1073 */       AstNode ccc = cc.getCatchCondition();
/* 1074 */       if (ccc != null) {
/* 1075 */         this.decompiler.addName(" ");
/* 1076 */         this.decompiler.addToken(112);
/* 1077 */         catchCond = transform(ccc);
/*      */       } else {
/* 1079 */         emptyExpression = new EmptyExpression();
/*      */       } 
/* 1081 */       this.decompiler.addToken(88);
/* 1082 */       this.decompiler.addEOL(85);
/*      */       
/* 1084 */       Node body = transform((AstNode)cc.getBody());
/* 1085 */       this.decompiler.addEOL(86);
/*      */       
/* 1087 */       block.addChildToBack(createCatch(varName, (Node)emptyExpression, body, cc.getLineno()));
/*      */     } 
/*      */     
/* 1090 */     Node finallyBlock = null;
/* 1091 */     if (node.getFinallyBlock() != null) {
/* 1092 */       this.decompiler.addToken(125);
/* 1093 */       this.decompiler.addEOL(85);
/* 1094 */       finallyBlock = transform(node.getFinallyBlock());
/* 1095 */       this.decompiler.addEOL(86);
/*      */     } 
/* 1097 */     return createTryCatchFinally(tryBlock, (Node)block, finallyBlock, node.getLineno());
/*      */   }
/*      */ 
/*      */   
/*      */   private Node transformUnary(UnaryExpression node) {
/* 1102 */     int type = node.getType();
/* 1103 */     if (type == 74) {
/* 1104 */       return transformDefaultXmlNamepace(node);
/*      */     }
/* 1106 */     if (node.isPrefix()) {
/* 1107 */       this.decompiler.addToken(type);
/*      */     }
/* 1109 */     Node child = transform(node.getOperand());
/* 1110 */     if (node.isPostfix()) {
/* 1111 */       this.decompiler.addToken(type);
/*      */     }
/* 1113 */     if (type == 106 || type == 107) {
/* 1114 */       return createIncDec(type, node.isPostfix(), child);
/*      */     }
/* 1116 */     return createUnary(type, child);
/*      */   }
/*      */   
/*      */   private Node transformVariables(VariableDeclaration node) {
/* 1120 */     this.decompiler.addToken(node.getType());
/* 1121 */     transformVariableInitializers(node);
/*      */ 
/*      */ 
/*      */     
/* 1125 */     AstNode parent = node.getParent();
/* 1126 */     if (!(parent instanceof org.mozilla.javascript.ast.Loop) && !(parent instanceof LetNode))
/*      */     {
/* 1128 */       this.decompiler.addEOL(82);
/*      */     }
/* 1130 */     return (Node)node;
/*      */   }
/*      */   
/*      */   private Node transformVariableInitializers(VariableDeclaration node) {
/* 1134 */     List<VariableInitializer> vars = node.getVariables();
/* 1135 */     int size = vars.size(), i = 0;
/* 1136 */     for (VariableInitializer var : vars) {
/* 1137 */       AstNode target = var.getTarget();
/* 1138 */       AstNode init = var.getInitializer();
/*      */       
/* 1140 */       Node left = null;
/* 1141 */       if (var.isDestructuring()) {
/* 1142 */         decompile(target);
/* 1143 */         AstNode astNode = target;
/*      */       } else {
/* 1145 */         left = transform(target);
/*      */       } 
/*      */       
/* 1148 */       Node right = null;
/* 1149 */       if (init != null) {
/* 1150 */         this.decompiler.addToken(90);
/* 1151 */         right = transform(init);
/*      */       } 
/*      */       
/* 1154 */       if (var.isDestructuring()) {
/* 1155 */         if (right == null) {
/* 1156 */           node.addChildToBack(left);
/*      */         } else {
/* 1158 */           Node d = createDestructuringAssignment(node.getType(), left, right);
/*      */           
/* 1160 */           node.addChildToBack(d);
/*      */         } 
/*      */       } else {
/* 1163 */         if (right != null) {
/* 1164 */           left.addChildToBack(right);
/*      */         }
/* 1166 */         node.addChildToBack(left);
/*      */       } 
/* 1168 */       if (i++ < size - 1) {
/* 1169 */         this.decompiler.addToken(89);
/*      */       }
/*      */     } 
/* 1172 */     return (Node)node;
/*      */   }
/*      */   
/*      */   private Node transformWhileLoop(WhileLoop loop) {
/* 1176 */     this.decompiler.addToken(117);
/* 1177 */     loop.setType(132);
/* 1178 */     pushScope((Scope)loop);
/*      */     try {
/* 1180 */       this.decompiler.addToken(87);
/* 1181 */       Node cond = transform(loop.getCondition());
/* 1182 */       this.decompiler.addToken(88);
/* 1183 */       this.decompiler.addEOL(85);
/* 1184 */       Node body = transform(loop.getBody());
/* 1185 */       this.decompiler.addEOL(86);
/* 1186 */       return createLoop((Jump)loop, 1, body, cond, (Node)null, (Node)null);
/*      */     } finally {
/* 1188 */       popScope();
/*      */     } 
/*      */   }
/*      */   
/*      */   private Node transformWith(WithStatement node) {
/* 1193 */     this.decompiler.addToken(123);
/* 1194 */     this.decompiler.addToken(87);
/* 1195 */     Node expr = transform(node.getExpression());
/* 1196 */     this.decompiler.addToken(88);
/* 1197 */     this.decompiler.addEOL(85);
/* 1198 */     Node stmt = transform(node.getStatement());
/* 1199 */     this.decompiler.addEOL(86);
/* 1200 */     return createWith(expr, stmt, node.getLineno());
/*      */   }
/*      */   
/*      */   private Node transformYield(Yield node) {
/* 1204 */     this.decompiler.addToken(72);
/* 1205 */     Node kid = (node.getValue() == null) ? null : transform(node.getValue());
/* 1206 */     if (kid != null) {
/* 1207 */       return new Node(72, kid, node.getLineno());
/*      */     }
/* 1209 */     return new Node(72, node.getLineno());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Node transformXmlLiteral(XmlLiteral node) {
/* 1216 */     Node pnXML = new Node(30, node.getLineno());
/* 1217 */     List<XmlFragment> frags = node.getFragments();
/*      */     
/* 1219 */     XmlString first = (XmlString)frags.get(0);
/* 1220 */     boolean anon = first.getXml().trim().startsWith("<>");
/* 1221 */     pnXML.addChildToBack(createName(anon ? "XMLList" : "XML"));
/*      */     
/* 1223 */     Node pn = null;
/* 1224 */     for (XmlFragment frag : frags) {
/* 1225 */       Node expr; if (frag instanceof XmlString) {
/* 1226 */         String xml = ((XmlString)frag).getXml();
/* 1227 */         this.decompiler.addName(xml);
/* 1228 */         if (pn == null) {
/* 1229 */           pn = createString(xml); continue;
/*      */         } 
/* 1231 */         pn = createBinary(21, pn, createString(xml));
/*      */         continue;
/*      */       } 
/* 1234 */       XmlExpression xexpr = (XmlExpression)frag;
/* 1235 */       boolean isXmlAttr = xexpr.isXmlAttribute();
/*      */       
/* 1237 */       this.decompiler.addToken(85);
/* 1238 */       if (xexpr.getExpression() instanceof EmptyExpression) {
/* 1239 */         expr = createString("");
/*      */       } else {
/* 1241 */         expr = transform(xexpr.getExpression());
/*      */       } 
/* 1243 */       this.decompiler.addToken(86);
/* 1244 */       if (isXmlAttr) {
/*      */         
/* 1246 */         expr = createUnary(75, expr);
/* 1247 */         Node prepend = createBinary(21, createString("\""), expr);
/*      */ 
/*      */         
/* 1250 */         expr = createBinary(21, prepend, createString("\""));
/*      */       }
/*      */       else {
/*      */         
/* 1254 */         expr = createUnary(76, expr);
/*      */       } 
/* 1256 */       pn = createBinary(21, pn, expr);
/*      */     } 
/*      */ 
/*      */     
/* 1260 */     pnXML.addChildToBack(pn);
/* 1261 */     return pnXML;
/*      */   }
/*      */   
/*      */   private Node transformXmlMemberGet(XmlMemberGet node) {
/* 1265 */     XmlRef ref = node.getMemberRef();
/* 1266 */     Node pn = transform(node.getLeft());
/* 1267 */     int flags = ref.isAttributeAccess() ? 2 : 0;
/* 1268 */     if (node.getType() == 143) {
/* 1269 */       flags |= 0x4;
/* 1270 */       this.decompiler.addToken(143);
/*      */     } else {
/* 1272 */       this.decompiler.addToken(108);
/*      */     } 
/* 1274 */     return transformXmlRef(pn, ref, flags);
/*      */   }
/*      */ 
/*      */   
/*      */   private Node transformXmlRef(XmlRef node) {
/* 1279 */     int memberTypeFlags = node.isAttributeAccess() ? 2 : 0;
/*      */     
/* 1281 */     return transformXmlRef((Node)null, node, memberTypeFlags);
/*      */   }
/*      */   
/*      */   private Node transformXmlRef(Node pn, XmlRef node, int memberTypeFlags) {
/* 1285 */     if ((memberTypeFlags & 0x2) != 0)
/* 1286 */       this.decompiler.addToken(147); 
/* 1287 */     Name namespace = node.getNamespace();
/* 1288 */     String ns = (namespace != null) ? namespace.getIdentifier() : null;
/* 1289 */     if (ns != null) {
/* 1290 */       this.decompiler.addName(ns);
/* 1291 */       this.decompiler.addToken(144);
/*      */     } 
/* 1293 */     if (node instanceof XmlPropRef) {
/* 1294 */       String name = ((XmlPropRef)node).getPropName().getIdentifier();
/* 1295 */       this.decompiler.addName(name);
/* 1296 */       return createPropertyGet(pn, ns, name, memberTypeFlags);
/*      */     } 
/* 1298 */     this.decompiler.addToken(83);
/* 1299 */     Node expr = transform(((XmlElemRef)node).getExpression());
/* 1300 */     this.decompiler.addToken(84);
/* 1301 */     return createElementGet(pn, ns, expr, memberTypeFlags);
/*      */   }
/*      */ 
/*      */   
/*      */   private Node transformDefaultXmlNamepace(UnaryExpression node) {
/* 1306 */     this.decompiler.addToken(116);
/* 1307 */     this.decompiler.addName(" xml");
/* 1308 */     this.decompiler.addName(" namespace");
/* 1309 */     this.decompiler.addToken(90);
/* 1310 */     Node child = transform(node.getOperand());
/* 1311 */     return createUnary(74, child);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addSwitchCase(Node switchBlock, Node caseExpression, Node statements) {
/* 1320 */     if (switchBlock.getType() != 129) throw Kit.codeBug(); 
/* 1321 */     Jump switchNode = (Jump)switchBlock.getFirstChild();
/* 1322 */     if (switchNode.getType() != 114) throw Kit.codeBug();
/*      */     
/* 1324 */     Node gotoTarget = Node.newTarget();
/* 1325 */     if (caseExpression != null) {
/* 1326 */       Jump caseNode = new Jump(115, caseExpression);
/* 1327 */       caseNode.target = gotoTarget;
/* 1328 */       switchNode.addChildToBack((Node)caseNode);
/*      */     } else {
/* 1330 */       switchNode.setDefault(gotoTarget);
/*      */     } 
/* 1332 */     switchBlock.addChildToBack(gotoTarget);
/* 1333 */     switchBlock.addChildToBack(statements);
/*      */   }
/*      */ 
/*      */   
/*      */   private void closeSwitch(Node switchBlock) {
/* 1338 */     if (switchBlock.getType() != 129) throw Kit.codeBug(); 
/* 1339 */     Jump switchNode = (Jump)switchBlock.getFirstChild();
/* 1340 */     if (switchNode.getType() != 114) throw Kit.codeBug();
/*      */     
/* 1342 */     Node switchBreakTarget = Node.newTarget();
/*      */ 
/*      */     
/* 1345 */     switchNode.target = switchBreakTarget;
/*      */     
/* 1347 */     Node defaultTarget = switchNode.getDefault();
/* 1348 */     if (defaultTarget == null) {
/* 1349 */       defaultTarget = switchBreakTarget;
/*      */     }
/*      */     
/* 1352 */     switchBlock.addChildAfter((Node)makeJump(5, defaultTarget), (Node)switchNode);
/*      */     
/* 1354 */     switchBlock.addChildToBack(switchBreakTarget);
/*      */   }
/*      */   
/*      */   private Node createExprStatementNoReturn(Node expr, int lineno) {
/* 1358 */     return new Node(133, expr, lineno);
/*      */   }
/*      */   
/*      */   private Node createString(String string) {
/* 1362 */     return Node.newString(string);
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
/*      */   private Node createCatch(String varName, Node catchCond, Node stmts, int lineno) {
/* 1375 */     if (catchCond == null) {
/* 1376 */       catchCond = new Node(128);
/*      */     }
/* 1378 */     return new Node(124, createName(varName), catchCond, stmts, lineno);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Node initFunction(FunctionNode fnNode, int functionIndex, Node statements, int functionType) {
/* 1384 */     fnNode.setFunctionType(functionType);
/* 1385 */     fnNode.addChildToBack(statements);
/*      */     
/* 1387 */     int functionCount = fnNode.getFunctionCount();
/* 1388 */     if (functionCount != 0)
/*      */     {
/* 1390 */       fnNode.setRequiresActivation();
/*      */     }
/*      */     
/* 1393 */     if (functionType == 2) {
/* 1394 */       Name name = fnNode.getFunctionName();
/* 1395 */       if (name != null && name.length() != 0 && fnNode.getSymbol(name.getIdentifier()) == null) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1404 */         fnNode.putSymbol(new Symbol(109, name.getIdentifier()));
/* 1405 */         Node setFn = new Node(133, new Node(8, Node.newString(49, name.getIdentifier()), new Node(63)));
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1410 */         statements.addChildrenToFront(setFn);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1415 */     Node lastStmt = statements.getLastChild();
/* 1416 */     if (lastStmt == null || lastStmt.getType() != 4) {
/* 1417 */       statements.addChildToBack(new Node(4));
/*      */     }
/*      */     
/* 1420 */     Node result = Node.newString(109, fnNode.getName());
/* 1421 */     result.putIntProp(1, functionIndex);
/* 1422 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Scope createLoopNode(Node loopLabel, int lineno) {
/* 1431 */     Scope result = createScopeNode(132, lineno);
/* 1432 */     if (loopLabel != null) {
/* 1433 */       ((Jump)loopLabel).setLoop((Jump)result);
/*      */     }
/* 1435 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   private Node createFor(Scope loop, Node init, Node test, Node incr, Node body) {
/* 1440 */     if (init.getType() == 153) {
/*      */ 
/*      */ 
/*      */       
/* 1444 */       Scope let = Scope.splitScope(loop);
/* 1445 */       let.setType(153);
/* 1446 */       let.addChildrenToBack(init);
/* 1447 */       let.addChildToBack(createLoop((Jump)loop, 2, body, test, new Node(128), incr));
/*      */       
/* 1449 */       return (Node)let;
/*      */     } 
/* 1451 */     return createLoop((Jump)loop, 2, body, test, init, incr);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Node createLoop(Jump loop, int loopType, Node body, Node cond, Node init, Node incr) {
/* 1457 */     Node bodyTarget = Node.newTarget();
/* 1458 */     Node condTarget = Node.newTarget();
/* 1459 */     if (loopType == 2 && cond.getType() == 128) {
/* 1460 */       cond = new Node(45);
/*      */     }
/* 1462 */     Jump IFEQ = new Jump(6, cond);
/* 1463 */     IFEQ.target = bodyTarget;
/* 1464 */     Node breakTarget = Node.newTarget();
/*      */     
/* 1466 */     loop.addChildToBack(bodyTarget);
/* 1467 */     loop.addChildrenToBack(body);
/* 1468 */     if (loopType == 1 || loopType == 2)
/*      */     {
/* 1470 */       loop.addChildrenToBack(new Node(128, loop.getLineno()));
/*      */     }
/* 1472 */     loop.addChildToBack(condTarget);
/* 1473 */     loop.addChildToBack((Node)IFEQ);
/* 1474 */     loop.addChildToBack(breakTarget);
/*      */     
/* 1476 */     loop.target = breakTarget;
/* 1477 */     Node continueTarget = condTarget;
/*      */     
/* 1479 */     if (loopType == 1 || loopType == 2) {
/*      */       
/* 1481 */       loop.addChildToFront((Node)makeJump(5, condTarget));
/*      */       
/* 1483 */       if (loopType == 2) {
/* 1484 */         int initType = init.getType();
/* 1485 */         if (initType != 128) {
/* 1486 */           if (initType != 122 && initType != 153) {
/* 1487 */             init = new Node(133, init);
/*      */           }
/* 1489 */           loop.addChildToFront(init);
/*      */         } 
/* 1491 */         Node incrTarget = Node.newTarget();
/* 1492 */         loop.addChildAfter(incrTarget, body);
/* 1493 */         if (incr.getType() != 128) {
/* 1494 */           incr = new Node(133, incr);
/* 1495 */           loop.addChildAfter(incr, incrTarget);
/*      */         } 
/* 1497 */         continueTarget = incrTarget;
/*      */       } 
/*      */     } 
/*      */     
/* 1501 */     loop.setContinue(continueTarget);
/* 1502 */     return (Node)loop;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Node createForIn(int declType, Node loop, Node lhs, Node obj, Node body, boolean isForEach) {
/*      */     Node lvalue, assign;
/* 1511 */     int destructuring = -1;
/* 1512 */     int destructuringLen = 0;
/*      */     
/* 1514 */     int type = lhs.getType();
/* 1515 */     if (type == 122 || type == 153) {
/* 1516 */       Node kid = lhs.getLastChild();
/* 1517 */       int kidType = kid.getType();
/* 1518 */       if (kidType == 65 || kidType == 66) {
/*      */         
/* 1520 */         type = destructuring = kidType;
/* 1521 */         lvalue = kid;
/* 1522 */         destructuringLen = 0;
/* 1523 */         if (kid instanceof ArrayLiteral)
/* 1524 */           destructuringLen = ((ArrayLiteral)kid).getDestructuringLength(); 
/* 1525 */       } else if (kidType == 39) {
/* 1526 */         lvalue = Node.newString(39, kid.getString());
/*      */       } else {
/* 1528 */         reportError("msg.bad.for.in.lhs");
/* 1529 */         return null;
/*      */       } 
/* 1531 */     } else if (type == 65 || type == 66) {
/* 1532 */       destructuring = type;
/* 1533 */       lvalue = lhs;
/* 1534 */       destructuringLen = 0;
/* 1535 */       if (lhs instanceof ArrayLiteral)
/* 1536 */         destructuringLen = ((ArrayLiteral)lhs).getDestructuringLength(); 
/*      */     } else {
/* 1538 */       lvalue = makeReference(lhs);
/* 1539 */       if (lvalue == null) {
/* 1540 */         reportError("msg.bad.for.in.lhs");
/* 1541 */         return null;
/*      */       } 
/*      */     } 
/*      */     
/* 1545 */     Node localBlock = new Node(141);
/* 1546 */     int initType = isForEach ? 59 : ((destructuring != -1) ? 60 : 58);
/*      */ 
/*      */ 
/*      */     
/* 1550 */     Node init = new Node(initType, obj);
/* 1551 */     init.putProp(3, localBlock);
/* 1552 */     Node cond = new Node(61);
/* 1553 */     cond.putProp(3, localBlock);
/* 1554 */     Node id = new Node(62);
/* 1555 */     id.putProp(3, localBlock);
/*      */     
/* 1557 */     Node newBody = new Node(129);
/*      */     
/* 1559 */     if (destructuring != -1) {
/* 1560 */       assign = createDestructuringAssignment(declType, lvalue, id);
/* 1561 */       if (!isForEach && (destructuring == 66 || destructuringLen != 2))
/*      */       {
/*      */ 
/*      */ 
/*      */         
/* 1566 */         reportError("msg.bad.for.in.destruct");
/*      */       }
/*      */     } else {
/* 1569 */       assign = simpleAssignment(lvalue, id);
/*      */     } 
/* 1571 */     newBody.addChildToBack(new Node(133, assign));
/* 1572 */     newBody.addChildToBack(body);
/*      */     
/* 1574 */     loop = createLoop((Jump)loop, 1, newBody, cond, (Node)null, (Node)null);
/* 1575 */     loop.addChildToFront(init);
/* 1576 */     if (type == 122 || type == 153)
/* 1577 */       loop.addChildToFront(lhs); 
/* 1578 */     localBlock.addChildToBack(loop);
/*      */     
/* 1580 */     return localBlock;
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
/*      */ 
/*      */   
/*      */   private Node createTryCatchFinally(Node tryBlock, Node catchBlocks, Node finallyBlock, int lineno) {
/* 1601 */     boolean hasFinally = (finallyBlock != null && (finallyBlock.getType() != 129 || finallyBlock.hasChildren()));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1606 */     if (tryBlock.getType() == 129 && !tryBlock.hasChildren() && !hasFinally)
/*      */     {
/*      */       
/* 1609 */       return tryBlock;
/*      */     }
/*      */     
/* 1612 */     boolean hasCatch = catchBlocks.hasChildren();
/*      */ 
/*      */     
/* 1615 */     if (!hasFinally && !hasCatch)
/*      */     {
/* 1617 */       return tryBlock;
/*      */     }
/*      */     
/* 1620 */     Node handlerBlock = new Node(141);
/* 1621 */     Jump pn = new Jump(81, tryBlock, lineno);
/* 1622 */     pn.putProp(3, handlerBlock);
/*      */     
/* 1624 */     if (hasCatch) {
/*      */       
/* 1626 */       Node endCatch = Node.newTarget();
/* 1627 */       pn.addChildToBack((Node)makeJump(5, endCatch));
/*      */ 
/*      */       
/* 1630 */       Node catchTarget = Node.newTarget();
/* 1631 */       pn.target = catchTarget;
/*      */       
/* 1633 */       pn.addChildToBack(catchTarget);
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
/* 1683 */       Node catchScopeBlock = new Node(141);
/*      */ 
/*      */       
/* 1686 */       Node cb = catchBlocks.getFirstChild();
/* 1687 */       boolean hasDefault = false;
/* 1688 */       int scopeIndex = 0;
/* 1689 */       while (cb != null) {
/* 1690 */         Node condStmt; int catchLineNo = cb.getLineno();
/*      */         
/* 1692 */         Node name = cb.getFirstChild();
/* 1693 */         Node cond = name.getNext();
/* 1694 */         Node catchStatement = cond.getNext();
/* 1695 */         cb.removeChild(name);
/* 1696 */         cb.removeChild(cond);
/* 1697 */         cb.removeChild(catchStatement);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1703 */         catchStatement.addChildToBack(new Node(3));
/* 1704 */         catchStatement.addChildToBack((Node)makeJump(5, endCatch));
/*      */ 
/*      */ 
/*      */         
/* 1708 */         if (cond.getType() == 128) {
/* 1709 */           condStmt = catchStatement;
/* 1710 */           hasDefault = true;
/*      */         } else {
/* 1712 */           condStmt = createIf(cond, catchStatement, (Node)null, catchLineNo);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1718 */         Node catchScope = new Node(57, name, createUseLocal(handlerBlock));
/*      */         
/* 1720 */         catchScope.putProp(3, catchScopeBlock);
/* 1721 */         catchScope.putIntProp(14, scopeIndex);
/* 1722 */         catchScopeBlock.addChildToBack(catchScope);
/*      */ 
/*      */         
/* 1725 */         catchScopeBlock.addChildToBack(createWith(createUseLocal(catchScopeBlock), condStmt, catchLineNo));
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1730 */         cb = cb.getNext();
/* 1731 */         scopeIndex++;
/*      */       } 
/* 1733 */       pn.addChildToBack(catchScopeBlock);
/* 1734 */       if (!hasDefault) {
/*      */         
/* 1736 */         Node rethrow = new Node(51);
/* 1737 */         rethrow.putProp(3, handlerBlock);
/* 1738 */         pn.addChildToBack(rethrow);
/*      */       } 
/*      */       
/* 1741 */       pn.addChildToBack(endCatch);
/*      */     } 
/*      */     
/* 1744 */     if (hasFinally) {
/* 1745 */       Node finallyTarget = Node.newTarget();
/* 1746 */       pn.setFinally(finallyTarget);
/*      */ 
/*      */       
/* 1749 */       pn.addChildToBack((Node)makeJump(135, finallyTarget));
/*      */ 
/*      */       
/* 1752 */       Node finallyEnd = Node.newTarget();
/* 1753 */       pn.addChildToBack((Node)makeJump(5, finallyEnd));
/*      */       
/* 1755 */       pn.addChildToBack(finallyTarget);
/* 1756 */       Node fBlock = new Node(125, finallyBlock);
/* 1757 */       fBlock.putProp(3, handlerBlock);
/* 1758 */       pn.addChildToBack(fBlock);
/*      */       
/* 1760 */       pn.addChildToBack(finallyEnd);
/*      */     } 
/* 1762 */     handlerBlock.addChildToBack((Node)pn);
/* 1763 */     return handlerBlock;
/*      */   }
/*      */   
/*      */   private Node createWith(Node obj, Node body, int lineno) {
/* 1767 */     setRequiresActivation();
/* 1768 */     Node result = new Node(129, lineno);
/* 1769 */     result.addChildToBack(new Node(2, obj));
/* 1770 */     Node bodyNode = new Node(123, body, lineno);
/* 1771 */     result.addChildrenToBack(bodyNode);
/* 1772 */     result.addChildToBack(new Node(3));
/* 1773 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   private Node createIf(Node cond, Node ifTrue, Node ifFalse, int lineno) {
/* 1778 */     int condStatus = isAlwaysDefinedBoolean(cond);
/* 1779 */     if (condStatus == 1)
/* 1780 */       return ifTrue; 
/* 1781 */     if (condStatus == -1) {
/* 1782 */       if (ifFalse != null) {
/* 1783 */         return ifFalse;
/*      */       }
/*      */       
/* 1786 */       return new Node(129, lineno);
/*      */     } 
/*      */     
/* 1789 */     Node result = new Node(129, lineno);
/* 1790 */     Node ifNotTarget = Node.newTarget();
/* 1791 */     Jump IFNE = new Jump(7, cond);
/* 1792 */     IFNE.target = ifNotTarget;
/*      */     
/* 1794 */     result.addChildToBack((Node)IFNE);
/* 1795 */     result.addChildrenToBack(ifTrue);
/*      */     
/* 1797 */     if (ifFalse != null) {
/* 1798 */       Node endTarget = Node.newTarget();
/* 1799 */       result.addChildToBack((Node)makeJump(5, endTarget));
/* 1800 */       result.addChildToBack(ifNotTarget);
/* 1801 */       result.addChildrenToBack(ifFalse);
/* 1802 */       result.addChildToBack(endTarget);
/*      */     } else {
/* 1804 */       result.addChildToBack(ifNotTarget);
/*      */     } 
/*      */     
/* 1807 */     return result;
/*      */   }
/*      */   
/*      */   private Node createCondExpr(Node cond, Node ifTrue, Node ifFalse) {
/* 1811 */     int condStatus = isAlwaysDefinedBoolean(cond);
/* 1812 */     if (condStatus == 1)
/* 1813 */       return ifTrue; 
/* 1814 */     if (condStatus == -1) {
/* 1815 */       return ifFalse;
/*      */     }
/* 1817 */     return new Node(102, cond, ifTrue, ifFalse);
/*      */   }
/*      */   
/*      */   private Node createUnary(int nodeType, Node child) {
/*      */     Node n;
/* 1822 */     int status, childType = child.getType();
/* 1823 */     switch (nodeType) {
/*      */       
/*      */       case 31:
/* 1826 */         if (childType == 39) {
/*      */ 
/*      */           
/* 1829 */           child.setType(49);
/* 1830 */           Node left = child;
/* 1831 */           Node right = Node.newString(child.getString());
/* 1832 */           n = new Node(nodeType, left, right);
/* 1833 */         } else if (childType == 33 || childType == 36) {
/*      */ 
/*      */           
/* 1836 */           Node left = child.getFirstChild();
/* 1837 */           Node right = child.getLastChild();
/* 1838 */           child.removeChild(left);
/* 1839 */           child.removeChild(right);
/* 1840 */           n = new Node(nodeType, left, right);
/* 1841 */         } else if (childType == 67) {
/* 1842 */           Node ref = child.getFirstChild();
/* 1843 */           child.removeChild(ref);
/* 1844 */           n = new Node(69, ref);
/*      */         } else {
/*      */           
/* 1847 */           n = new Node(nodeType, new Node(45), child);
/*      */         } 
/* 1849 */         return n;
/*      */       
/*      */       case 32:
/* 1852 */         if (childType == 39) {
/* 1853 */           child.setType(137);
/* 1854 */           return child;
/*      */         } 
/*      */         break;
/*      */       case 27:
/* 1858 */         if (childType == 40) {
/* 1859 */           int value = ScriptRuntime.toInt32(child.getDouble());
/* 1860 */           child.setDouble((value ^ 0xFFFFFFFF));
/* 1861 */           return child;
/*      */         } 
/*      */         break;
/*      */       case 29:
/* 1865 */         if (childType == 40) {
/* 1866 */           child.setDouble(-child.getDouble());
/* 1867 */           return child;
/*      */         } 
/*      */         break;
/*      */       case 26:
/* 1871 */         status = isAlwaysDefinedBoolean(child);
/* 1872 */         if (status != 0) {
/*      */           int type;
/* 1874 */           if (status == 1) {
/* 1875 */             type = 44;
/*      */           } else {
/* 1877 */             type = 45;
/*      */           } 
/* 1879 */           if (childType == 45 || childType == 44) {
/* 1880 */             child.setType(type);
/* 1881 */             return child;
/*      */           } 
/* 1883 */           return new Node(type);
/*      */         } 
/*      */         break;
/*      */     } 
/*      */     
/* 1888 */     return new Node(nodeType, child);
/*      */   }
/*      */   
/*      */   private Node createCallOrNew(int nodeType, Node child) {
/* 1892 */     int type = 0;
/* 1893 */     if (child.getType() == 39) {
/* 1894 */       String name = child.getString();
/* 1895 */       if (name.equals("eval")) {
/* 1896 */         type = 1;
/* 1897 */       } else if (name.equals("With")) {
/* 1898 */         type = 2;
/*      */       } 
/* 1900 */     } else if (child.getType() == 33) {
/* 1901 */       String name = child.getLastChild().getString();
/* 1902 */       if (name.equals("eval")) {
/* 1903 */         type = 1;
/*      */       }
/*      */     } 
/* 1906 */     Node node = new Node(nodeType, child);
/* 1907 */     if (type != 0) {
/*      */       
/* 1909 */       setRequiresActivation();
/* 1910 */       node.putIntProp(10, type);
/*      */     } 
/* 1912 */     return node;
/*      */   }
/*      */   private Node createIncDec(int nodeType, boolean post, Node child) {
/*      */     Node n;
/*      */     int incrDecrMask;
/* 1917 */     child = makeReference(child);
/* 1918 */     int childType = child.getType();
/*      */     
/* 1920 */     switch (childType) {
/*      */       case 33:
/*      */       case 36:
/*      */       case 39:
/*      */       case 67:
/* 1925 */         n = new Node(nodeType, child);
/* 1926 */         incrDecrMask = 0;
/* 1927 */         if (nodeType == 107) {
/* 1928 */           incrDecrMask |= 0x1;
/*      */         }
/* 1930 */         if (post) {
/* 1931 */           incrDecrMask |= 0x2;
/*      */         }
/* 1933 */         n.putIntProp(13, incrDecrMask);
/* 1934 */         return n;
/*      */     } 
/*      */     
/* 1937 */     throw Kit.codeBug();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Node createPropertyGet(Node target, String namespace, String name, int memberTypeFlags) {
/* 1943 */     if (namespace == null && memberTypeFlags == 0) {
/* 1944 */       if (target == null) {
/* 1945 */         return createName(name);
/*      */       }
/* 1947 */       checkActivationName(name, 33);
/* 1948 */       if (ScriptRuntime.isSpecialProperty(name)) {
/* 1949 */         Node ref = new Node(71, target);
/* 1950 */         ref.putProp(17, name);
/* 1951 */         return new Node(67, ref);
/*      */       } 
/* 1953 */       return new Node(33, target, Node.newString(name));
/*      */     } 
/* 1955 */     Node elem = Node.newString(name);
/* 1956 */     memberTypeFlags |= 0x1;
/* 1957 */     return createMemberRefGet(target, namespace, elem, memberTypeFlags);
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
/*      */   private Node createElementGet(Node target, String namespace, Node elem, int memberTypeFlags) {
/* 1971 */     if (namespace == null && memberTypeFlags == 0) {
/*      */ 
/*      */       
/* 1974 */       if (target == null) throw Kit.codeBug(); 
/* 1975 */       return new Node(36, target, elem);
/*      */     } 
/* 1977 */     return createMemberRefGet(target, namespace, elem, memberTypeFlags);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Node createMemberRefGet(Node target, String namespace, Node elem, int memberTypeFlags) {
/* 1983 */     Node ref, nsNode = null;
/* 1984 */     if (namespace != null)
/*      */     {
/* 1986 */       if (namespace.equals("*")) {
/* 1987 */         nsNode = new Node(42);
/*      */       } else {
/* 1989 */         nsNode = createName(namespace);
/*      */       } 
/*      */     }
/*      */     
/* 1993 */     if (target == null) {
/* 1994 */       if (namespace == null) {
/* 1995 */         ref = new Node(79, elem);
/*      */       } else {
/* 1997 */         ref = new Node(80, nsNode, elem);
/*      */       }
/*      */     
/* 2000 */     } else if (namespace == null) {
/* 2001 */       ref = new Node(77, target, elem);
/*      */     } else {
/* 2003 */       ref = new Node(78, target, nsNode, elem);
/*      */     } 
/*      */     
/* 2006 */     if (memberTypeFlags != 0) {
/* 2007 */       ref.putIntProp(16, memberTypeFlags);
/*      */     }
/* 2009 */     return new Node(67, ref);
/*      */   }
/*      */   private Node createBinary(int nodeType, Node left, Node right) {
/*      */     int leftStatus;
/* 2013 */     switch (nodeType) {
/*      */ 
/*      */       
/*      */       case 21:
/* 2017 */         if (left.type == 41) {
/*      */           String s2;
/* 2019 */           if (right.type == 41)
/* 2020 */           { s2 = right.getString(); }
/* 2021 */           else { if (right.type == 40)
/* 2022 */             { s2 = ScriptRuntime.numberToString(right.getDouble(), 10);
/*      */ 
/*      */ 
/*      */               
/* 2026 */               String s1 = left.getString();
/* 2027 */               left.setString(s1.concat(s2));
/* 2028 */               return left; }  break; }  String str1 = left.getString(); left.setString(str1.concat(s2)); return left;
/* 2029 */         }  if (left.type == 40) {
/* 2030 */           if (right.type == 40) {
/* 2031 */             left.setDouble(left.getDouble() + right.getDouble());
/* 2032 */             return left;
/* 2033 */           }  if (right.type == 41) {
/*      */             
/* 2035 */             String s1 = ScriptRuntime.numberToString(left.getDouble(), 10);
/* 2036 */             String s2 = right.getString();
/* 2037 */             right.setString(s1.concat(s2));
/* 2038 */             return right;
/*      */           } 
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 22:
/* 2048 */         if (left.type == 40) {
/* 2049 */           double ld = left.getDouble();
/* 2050 */           if (right.type == 40) {
/*      */             
/* 2052 */             left.setDouble(ld - right.getDouble());
/* 2053 */             return left;
/* 2054 */           }  if (ld == 0.0D)
/*      */           {
/* 2056 */             return new Node(29, right); }  break;
/*      */         } 
/* 2058 */         if (right.type == 40 && 
/* 2059 */           right.getDouble() == 0.0D)
/*      */         {
/*      */           
/* 2062 */           return new Node(28, left);
/*      */         }
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 23:
/* 2069 */         if (left.type == 40) {
/* 2070 */           double ld = left.getDouble();
/* 2071 */           if (right.type == 40) {
/*      */             
/* 2073 */             left.setDouble(ld * right.getDouble());
/* 2074 */             return left;
/* 2075 */           }  if (ld == 1.0D)
/*      */           {
/* 2077 */             return new Node(28, right); }  break;
/*      */         } 
/* 2079 */         if (right.type == 40 && 
/* 2080 */           right.getDouble() == 1.0D)
/*      */         {
/*      */           
/* 2083 */           return new Node(28, left);
/*      */         }
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 24:
/* 2091 */         if (right.type == 40) {
/* 2092 */           double rd = right.getDouble();
/* 2093 */           if (left.type == 40) {
/*      */             
/* 2095 */             left.setDouble(left.getDouble() / rd);
/* 2096 */             return left;
/* 2097 */           }  if (rd == 1.0D)
/*      */           {
/*      */             
/* 2100 */             return new Node(28, left);
/*      */           }
/*      */         } 
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 105:
/* 2110 */         leftStatus = isAlwaysDefinedBoolean(left);
/* 2111 */         if (leftStatus == -1)
/*      */         {
/* 2113 */           return left; } 
/* 2114 */         if (leftStatus == 1)
/*      */         {
/* 2116 */           return right;
/*      */         }
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 104:
/* 2126 */         leftStatus = isAlwaysDefinedBoolean(left);
/* 2127 */         if (leftStatus == 1)
/*      */         {
/* 2129 */           return left; } 
/* 2130 */         if (leftStatus == -1)
/*      */         {
/* 2132 */           return right;
/*      */         }
/*      */         break;
/*      */     } 
/*      */ 
/*      */     
/* 2138 */     return new Node(nodeType, left, right);
/*      */   } private Node createAssignment(int assignType, Node left, Node right) {
/*      */     int assignOp;
/*      */     Node op, obj, opLeft, lvalueLeft, id, node1;
/*      */     int type;
/* 2143 */     Node node2, node3, ref = makeReference(left);
/* 2144 */     if (ref == null) {
/* 2145 */       if (left.getType() == 65 || left.getType() == 66) {
/*      */ 
/*      */         
/* 2148 */         if (assignType != 90) {
/* 2149 */           reportError("msg.bad.destruct.op");
/* 2150 */           return right;
/*      */         } 
/* 2152 */         return createDestructuringAssignment(-1, left, right);
/*      */       } 
/* 2154 */       reportError("msg.bad.assign.left");
/* 2155 */       return right;
/*      */     } 
/* 2157 */     left = ref;
/*      */ 
/*      */     
/* 2160 */     switch (assignType) {
/*      */       case 90:
/* 2162 */         return simpleAssignment(left, right);
/* 2163 */       case 91: assignOp = 9; break;
/* 2164 */       case 92: assignOp = 10; break;
/* 2165 */       case 93: assignOp = 11; break;
/* 2166 */       case 94: assignOp = 18; break;
/* 2167 */       case 95: assignOp = 19; break;
/* 2168 */       case 96: assignOp = 20; break;
/* 2169 */       case 97: assignOp = 21; break;
/* 2170 */       case 98: assignOp = 22; break;
/* 2171 */       case 99: assignOp = 23; break;
/* 2172 */       case 100: assignOp = 24; break;
/* 2173 */       case 101: assignOp = 25; break;
/* 2174 */       default: throw Kit.codeBug();
/*      */     } 
/*      */     
/* 2177 */     int nodeType = left.getType();
/* 2178 */     switch (nodeType) {
/*      */       case 39:
/* 2180 */         op = new Node(assignOp, left, right);
/* 2181 */         lvalueLeft = Node.newString(49, left.getString());
/* 2182 */         return new Node(8, lvalueLeft, op);
/*      */       
/*      */       case 33:
/*      */       case 36:
/* 2186 */         obj = left.getFirstChild();
/* 2187 */         id = left.getLastChild();
/*      */         
/* 2189 */         type = (nodeType == 33) ? 139 : 140;
/*      */ 
/*      */ 
/*      */         
/* 2193 */         node2 = new Node(138);
/* 2194 */         node3 = new Node(assignOp, node2, right);
/* 2195 */         return new Node(type, obj, id, node3);
/*      */       
/*      */       case 67:
/* 2198 */         ref = left.getFirstChild();
/* 2199 */         checkMutableReference(ref);
/* 2200 */         opLeft = new Node(138);
/* 2201 */         node1 = new Node(assignOp, opLeft, right);
/* 2202 */         return new Node(142, ref, node1);
/*      */     } 
/*      */ 
/*      */     
/* 2206 */     throw Kit.codeBug();
/*      */   }
/*      */   
/*      */   private Node createUseLocal(Node localBlock) {
/* 2210 */     if (141 != localBlock.getType()) throw Kit.codeBug(); 
/* 2211 */     Node result = new Node(54);
/* 2212 */     result.putProp(3, localBlock);
/* 2213 */     return result;
/*      */   }
/*      */   
/*      */   private Jump makeJump(int type, Node target) {
/* 2217 */     Jump n = new Jump(type);
/* 2218 */     n.target = target;
/* 2219 */     return n;
/*      */   }
/*      */   
/*      */   private Node makeReference(Node node) {
/* 2223 */     int type = node.getType();
/* 2224 */     switch (type) {
/*      */       case 33:
/*      */       case 36:
/*      */       case 39:
/*      */       case 67:
/* 2229 */         return node;
/*      */       case 38:
/* 2231 */         node.setType(70);
/* 2232 */         return new Node(67, node);
/*      */     } 
/*      */     
/* 2235 */     return null;
/*      */   }
/*      */   
/*      */   private static int isAlwaysDefinedBoolean(Node node) {
/*      */     double num;
/* 2240 */     switch (node.getType()) {
/*      */       case 42:
/*      */       case 44:
/* 2243 */         return -1;
/*      */       case 45:
/* 2245 */         return 1;
/*      */       case 40:
/* 2247 */         num = node.getDouble();
/* 2248 */         if (num == num && num != 0.0D) {
/* 2249 */           return 1;
/*      */         }
/* 2251 */         return -1;
/*      */     } 
/*      */ 
/*      */     
/* 2255 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   boolean isDestructuring(Node n) {
/* 2260 */     return (n instanceof DestructuringForm && ((DestructuringForm)n).isDestructuring());
/*      */   }
/*      */ 
/*      */   
/*      */   Node decompileFunctionHeader(FunctionNode fn) {
/* 2265 */     Node mexpr = null;
/* 2266 */     if (fn.getFunctionName() != null) {
/* 2267 */       this.decompiler.addName(fn.getName());
/* 2268 */     } else if (fn.getMemberExprNode() != null) {
/* 2269 */       mexpr = transform(fn.getMemberExprNode());
/*      */     } 
/* 2271 */     this.decompiler.addToken(87);
/* 2272 */     List<AstNode> params = fn.getParams();
/* 2273 */     for (int i = 0; i < params.size(); i++) {
/* 2274 */       decompile(params.get(i));
/* 2275 */       if (i < params.size() - 1) {
/* 2276 */         this.decompiler.addToken(89);
/*      */       }
/*      */     } 
/* 2279 */     this.decompiler.addToken(88);
/* 2280 */     if (!fn.isExpressionClosure()) {
/* 2281 */       this.decompiler.addEOL(85);
/*      */     }
/* 2283 */     return mexpr;
/*      */   }
/*      */   
/*      */   void decompile(AstNode node) {
/* 2287 */     switch (node.getType()) {
/*      */       case 65:
/* 2289 */         decompileArrayLiteral((ArrayLiteral)node);
/*      */       
/*      */       case 66:
/* 2292 */         decompileObjectLiteral((ObjectLiteral)node);
/*      */       
/*      */       case 41:
/* 2295 */         this.decompiler.addString(((StringLiteral)node).getValue());
/*      */       
/*      */       case 39:
/* 2298 */         this.decompiler.addName(((Name)node).getIdentifier());
/*      */       
/*      */       case 40:
/* 2301 */         this.decompiler.addNumber(((NumberLiteral)node).getNumber());
/*      */       
/*      */       case 33:
/* 2304 */         decompilePropertyGet((PropertyGet)node);
/*      */       
/*      */       case 128:
/*      */         return;
/*      */       case 36:
/* 2309 */         decompileElementGet((ElementGet)node);
/*      */       
/*      */       case 43:
/* 2312 */         this.decompiler.addToken(node.getType());
/*      */     } 
/*      */     
/* 2315 */     Kit.codeBug("unexpected token: " + Token.typeToName(node.getType()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void decompileArrayLiteral(ArrayLiteral node) {
/* 2322 */     this.decompiler.addToken(83);
/* 2323 */     List<AstNode> elems = node.getElements();
/* 2324 */     int size = elems.size();
/* 2325 */     for (int i = 0; i < size; i++) {
/* 2326 */       AstNode elem = elems.get(i);
/* 2327 */       decompile(elem);
/* 2328 */       if (i < size - 1) {
/* 2329 */         this.decompiler.addToken(89);
/*      */       }
/*      */     } 
/* 2332 */     this.decompiler.addToken(84);
/*      */   }
/*      */ 
/*      */   
/*      */   void decompileObjectLiteral(ObjectLiteral node) {
/* 2337 */     this.decompiler.addToken(85);
/* 2338 */     List<ObjectProperty> props = node.getElements();
/* 2339 */     int size = props.size();
/* 2340 */     for (int i = 0; i < size; i++) {
/* 2341 */       ObjectProperty prop = props.get(i);
/* 2342 */       boolean destructuringShorthand = Boolean.TRUE.equals(prop.getProp(26));
/*      */       
/* 2344 */       decompile(prop.getLeft());
/* 2345 */       if (!destructuringShorthand) {
/* 2346 */         this.decompiler.addToken(103);
/* 2347 */         decompile(prop.getRight());
/*      */       } 
/* 2349 */       if (i < size - 1) {
/* 2350 */         this.decompiler.addToken(89);
/*      */       }
/*      */     } 
/* 2353 */     this.decompiler.addToken(86);
/*      */   }
/*      */ 
/*      */   
/*      */   void decompilePropertyGet(PropertyGet node) {
/* 2358 */     decompile(node.getTarget());
/* 2359 */     this.decompiler.addToken(108);
/* 2360 */     decompile((AstNode)node.getProperty());
/*      */   }
/*      */ 
/*      */   
/*      */   void decompileElementGet(ElementGet node) {
/* 2365 */     decompile(node.getTarget());
/* 2366 */     this.decompiler.addToken(83);
/* 2367 */     decompile(node.getElement());
/* 2368 */     this.decompiler.addToken(84);
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\IRFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */