/*      */ package org.mozilla.javascript;
/*      */ 
/*      */ import org.mozilla.javascript.ast.AstRoot;
/*      */ import org.mozilla.javascript.ast.FunctionNode;
/*      */ import org.mozilla.javascript.ast.Jump;
/*      */ import org.mozilla.javascript.ast.ScriptNode;
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
/*      */ class CodeGenerator
/*      */   extends Icode
/*      */ {
/*      */   private static final int MIN_LABEL_TABLE_SIZE = 32;
/*      */   private static final int MIN_FIXUP_TABLE_SIZE = 40;
/*      */   private CompilerEnvirons compilerEnv;
/*      */   private boolean itsInFunctionFlag;
/*      */   private boolean itsInTryFlag;
/*      */   private InterpreterData itsData;
/*      */   private ScriptNode scriptOrFn;
/*      */   private int iCodeTop;
/*      */   private int stackDepth;
/*      */   private int lineNumber;
/*      */   private int doubleTableTop;
/*   35 */   private ObjToIntMap strings = new ObjToIntMap(20);
/*      */   
/*      */   private int localTop;
/*      */   
/*      */   private int[] labelTable;
/*      */   private int labelTableTop;
/*      */   private long[] fixupTable;
/*      */   private int fixupTableTop;
/*   43 */   private ObjArray literalIds = new ObjArray();
/*      */ 
/*      */ 
/*      */   
/*      */   private int exceptionTableTop;
/*      */ 
/*      */   
/*      */   private static final int ECF_TAIL = 1;
/*      */ 
/*      */ 
/*      */   
/*      */   public InterpreterData compile(CompilerEnvirons compilerEnv, ScriptNode tree, String encodedSource, boolean returnFunction) {
/*   55 */     this.compilerEnv = compilerEnv;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   62 */     (new NodeTransformer()).transform(tree);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   69 */     if (returnFunction) {
/*   70 */       this.scriptOrFn = (ScriptNode)tree.getFunctionNode(0);
/*      */     } else {
/*   72 */       this.scriptOrFn = tree;
/*      */     } 
/*   74 */     this.itsData = new InterpreterData(compilerEnv.getLanguageVersion(), this.scriptOrFn.getSourceName(), encodedSource, ((AstRoot)tree).isInStrictMode());
/*      */ 
/*      */ 
/*      */     
/*   78 */     this.itsData.topLevel = true;
/*      */     
/*   80 */     if (returnFunction) {
/*   81 */       generateFunctionICode();
/*      */     } else {
/*   83 */       generateICodeFromTree((Node)this.scriptOrFn);
/*      */     } 
/*   85 */     return this.itsData;
/*      */   }
/*      */ 
/*      */   
/*      */   private void generateFunctionICode() {
/*   90 */     this.itsInFunctionFlag = true;
/*      */     
/*   92 */     FunctionNode theFunction = (FunctionNode)this.scriptOrFn;
/*      */     
/*   94 */     this.itsData.itsFunctionType = theFunction.getFunctionType();
/*   95 */     this.itsData.itsNeedsActivation = theFunction.requiresActivation();
/*   96 */     if (theFunction.getFunctionName() != null) {
/*   97 */       this.itsData.itsName = theFunction.getName();
/*      */     }
/*   99 */     if (theFunction.isGenerator()) {
/*  100 */       addIcode(-62);
/*  101 */       addUint16(theFunction.getBaseLineno() & 0xFFFF);
/*      */     } 
/*      */     
/*  104 */     generateICodeFromTree(theFunction.getLastChild());
/*      */   }
/*      */ 
/*      */   
/*      */   private void generateICodeFromTree(Node tree) {
/*  109 */     generateNestedFunctions();
/*      */     
/*  111 */     generateRegExpLiterals();
/*      */     
/*  113 */     visitStatement(tree, 0);
/*  114 */     fixLabelGotos();
/*      */     
/*  116 */     if (this.itsData.itsFunctionType == 0) {
/*  117 */       addToken(64);
/*      */     }
/*      */     
/*  120 */     if (this.itsData.itsICode.length != this.iCodeTop) {
/*      */ 
/*      */       
/*  123 */       byte[] tmp = new byte[this.iCodeTop];
/*  124 */       System.arraycopy(this.itsData.itsICode, 0, tmp, 0, this.iCodeTop);
/*  125 */       this.itsData.itsICode = tmp;
/*      */     } 
/*  127 */     if (this.strings.size() == 0) {
/*  128 */       this.itsData.itsStringTable = null;
/*      */     } else {
/*  130 */       this.itsData.itsStringTable = new String[this.strings.size()];
/*  131 */       ObjToIntMap.Iterator iter = this.strings.newIterator();
/*  132 */       iter.start(); for (; !iter.done(); iter.next()) {
/*  133 */         String str = (String)iter.getKey();
/*  134 */         int index = iter.getValue();
/*  135 */         if (this.itsData.itsStringTable[index] != null) Kit.codeBug(); 
/*  136 */         this.itsData.itsStringTable[index] = str;
/*      */       } 
/*      */     } 
/*  139 */     if (this.doubleTableTop == 0) {
/*  140 */       this.itsData.itsDoubleTable = null;
/*  141 */     } else if (this.itsData.itsDoubleTable.length != this.doubleTableTop) {
/*  142 */       double[] tmp = new double[this.doubleTableTop];
/*  143 */       System.arraycopy(this.itsData.itsDoubleTable, 0, tmp, 0, this.doubleTableTop);
/*      */       
/*  145 */       this.itsData.itsDoubleTable = tmp;
/*      */     } 
/*  147 */     if (this.exceptionTableTop != 0 && this.itsData.itsExceptionTable.length != this.exceptionTableTop) {
/*      */ 
/*      */       
/*  150 */       int[] tmp = new int[this.exceptionTableTop];
/*  151 */       System.arraycopy(this.itsData.itsExceptionTable, 0, tmp, 0, this.exceptionTableTop);
/*      */       
/*  153 */       this.itsData.itsExceptionTable = tmp;
/*      */     } 
/*      */     
/*  156 */     this.itsData.itsMaxVars = this.scriptOrFn.getParamAndVarCount();
/*      */ 
/*      */     
/*  159 */     this.itsData.itsMaxFrameArray = this.itsData.itsMaxVars + this.itsData.itsMaxLocals + this.itsData.itsMaxStack;
/*      */ 
/*      */ 
/*      */     
/*  163 */     this.itsData.argNames = this.scriptOrFn.getParamAndVarNames();
/*  164 */     this.itsData.argIsConst = this.scriptOrFn.getParamAndVarConst();
/*  165 */     this.itsData.argCount = this.scriptOrFn.getParamCount();
/*      */     
/*  167 */     this.itsData.encodedSourceStart = this.scriptOrFn.getEncodedSourceStart();
/*  168 */     this.itsData.encodedSourceEnd = this.scriptOrFn.getEncodedSourceEnd();
/*      */     
/*  170 */     if (this.literalIds.size() != 0) {
/*  171 */       this.itsData.literalIds = this.literalIds.toArray();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void generateNestedFunctions() {
/*  179 */     int functionCount = this.scriptOrFn.getFunctionCount();
/*  180 */     if (functionCount == 0)
/*      */       return; 
/*  182 */     InterpreterData[] array = new InterpreterData[functionCount];
/*  183 */     for (int i = 0; i != functionCount; i++) {
/*  184 */       FunctionNode fn = this.scriptOrFn.getFunctionNode(i);
/*  185 */       CodeGenerator gen = new CodeGenerator();
/*  186 */       gen.compilerEnv = this.compilerEnv;
/*  187 */       gen.scriptOrFn = (ScriptNode)fn;
/*  188 */       gen.itsData = new InterpreterData(this.itsData);
/*  189 */       gen.generateFunctionICode();
/*  190 */       array[i] = gen.itsData;
/*      */     } 
/*  192 */     this.itsData.itsNestedFunctions = array;
/*      */   }
/*      */ 
/*      */   
/*      */   private void generateRegExpLiterals() {
/*  197 */     int N = this.scriptOrFn.getRegexpCount();
/*  198 */     if (N == 0)
/*      */       return; 
/*  200 */     Context cx = Context.getContext();
/*  201 */     RegExpProxy rep = ScriptRuntime.checkRegExpProxy(cx);
/*  202 */     Object[] array = new Object[N];
/*  203 */     for (int i = 0; i != N; i++) {
/*  204 */       String string = this.scriptOrFn.getRegexpString(i);
/*  205 */       String flags = this.scriptOrFn.getRegexpFlags(i);
/*  206 */       array[i] = rep.compileRegExp(cx, string, flags);
/*      */     } 
/*  208 */     this.itsData.itsRegExpLiterals = array;
/*      */   }
/*      */ 
/*      */   
/*      */   private void updateLineNumber(Node node) {
/*  213 */     int lineno = node.getLineno();
/*  214 */     if (lineno != this.lineNumber && lineno >= 0) {
/*  215 */       if (this.itsData.firstLinePC < 0) {
/*  216 */         this.itsData.firstLinePC = lineno;
/*      */       }
/*  218 */       this.lineNumber = lineno;
/*  219 */       addIcode(-26);
/*  220 */       addUint16(lineno & 0xFFFF);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private RuntimeException badTree(Node node) {
/*  226 */     throw new RuntimeException(node.toString()); } private void visitStatement(Node node, int initialStackDepth) { int fnIndex, local; Jump caseNode; Node target; int finallyRegister; Jump tryNode; int localIndex, fnType, exceptionObjectLocal, scopeIndex, scopeLocal;
/*      */     String name;
/*      */     int tryStart;
/*      */     boolean savedFlag;
/*      */     Node catchTarget, finallyTarget;
/*  231 */     int type = node.getType();
/*  232 */     Node child = node.getFirstChild();
/*  233 */     switch (type) {
/*      */ 
/*      */       
/*      */       case 109:
/*  237 */         fnIndex = node.getExistingIntProp(1);
/*  238 */         fnType = this.scriptOrFn.getFunctionNode(fnIndex).getFunctionType();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  246 */         if (fnType == 3) {
/*  247 */           addIndexOp(-20, fnIndex);
/*      */         }
/*  249 */         else if (fnType != 1) {
/*  250 */           throw Kit.codeBug();
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  258 */         if (!this.itsInFunctionFlag) {
/*  259 */           addIndexOp(-19, fnIndex);
/*  260 */           stackChange(1);
/*  261 */           addIcode(-5);
/*  262 */           stackChange(-1);
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 123:
/*      */       case 128:
/*      */       case 129:
/*      */       case 130:
/*      */       case 132:
/*  272 */         updateLineNumber(node);
/*      */       
/*      */       case 136:
/*  275 */         while (child != null) {
/*  276 */           visitStatement(child, initialStackDepth);
/*  277 */           child = child.getNext();
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 2:
/*  282 */         visitExpression(child, 0);
/*  283 */         addToken(2);
/*  284 */         stackChange(-1);
/*      */         break;
/*      */       
/*      */       case 3:
/*  288 */         addToken(3);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 141:
/*  293 */         local = allocLocal();
/*  294 */         node.putIntProp(2, local);
/*  295 */         updateLineNumber(node);
/*  296 */         while (child != null) {
/*  297 */           visitStatement(child, initialStackDepth);
/*  298 */           child = child.getNext();
/*      */         } 
/*  300 */         addIndexOp(-56, local);
/*  301 */         releaseLocal(local);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 160:
/*  306 */         addIcode(-64);
/*      */         break;
/*      */       
/*      */       case 114:
/*  310 */         updateLineNumber(node);
/*      */ 
/*      */ 
/*      */         
/*  314 */         visitExpression(child, 0);
/*  315 */         caseNode = (Jump)child.getNext();
/*  316 */         for (; caseNode != null; 
/*  317 */           caseNode = (Jump)caseNode.getNext()) {
/*      */           
/*  319 */           if (caseNode.getType() != 115)
/*  320 */             throw badTree(caseNode); 
/*  321 */           Node test = caseNode.getFirstChild();
/*  322 */           addIcode(-1);
/*  323 */           stackChange(1);
/*  324 */           visitExpression(test, 0);
/*  325 */           addToken(46);
/*  326 */           stackChange(-1);
/*      */ 
/*      */           
/*  329 */           addGoto(caseNode.target, -6);
/*  330 */           stackChange(-1);
/*      */         } 
/*  332 */         addIcode(-4);
/*  333 */         stackChange(-1);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 131:
/*  338 */         markTargetLabel(node);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 6:
/*      */       case 7:
/*  344 */         target = ((Jump)node).target;
/*  345 */         visitExpression(child, 0);
/*  346 */         addGoto(target, type);
/*  347 */         stackChange(-1);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 5:
/*  353 */         target = ((Jump)node).target;
/*  354 */         addGoto(target, type);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 135:
/*  360 */         target = ((Jump)node).target;
/*  361 */         addGoto(target, -23);
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 125:
/*  368 */         stackChange(1);
/*  369 */         finallyRegister = getLocalBlockRef(node);
/*  370 */         addIndexOp(-24, finallyRegister);
/*  371 */         stackChange(-1);
/*  372 */         while (child != null) {
/*  373 */           visitStatement(child, initialStackDepth);
/*  374 */           child = child.getNext();
/*      */         } 
/*  376 */         addIndexOp(-25, finallyRegister);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 133:
/*      */       case 134:
/*  382 */         updateLineNumber(node);
/*  383 */         visitExpression(child, 0);
/*  384 */         addIcode((type == 133) ? -4 : -5);
/*  385 */         stackChange(-1);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 81:
/*  390 */         tryNode = (Jump)node;
/*  391 */         exceptionObjectLocal = getLocalBlockRef((Node)tryNode);
/*  392 */         scopeLocal = allocLocal();
/*      */         
/*  394 */         addIndexOp(-13, scopeLocal);
/*      */         
/*  396 */         tryStart = this.iCodeTop;
/*  397 */         savedFlag = this.itsInTryFlag;
/*  398 */         this.itsInTryFlag = true;
/*  399 */         while (child != null) {
/*  400 */           visitStatement(child, initialStackDepth);
/*  401 */           child = child.getNext();
/*      */         } 
/*  403 */         this.itsInTryFlag = savedFlag;
/*      */         
/*  405 */         catchTarget = tryNode.target;
/*  406 */         if (catchTarget != null) {
/*  407 */           int catchStartPC = this.labelTable[getTargetLabel(catchTarget)];
/*      */           
/*  409 */           addExceptionHandler(tryStart, catchStartPC, catchStartPC, false, exceptionObjectLocal, scopeLocal);
/*      */         } 
/*      */ 
/*      */         
/*  413 */         finallyTarget = tryNode.getFinally();
/*  414 */         if (finallyTarget != null) {
/*  415 */           int finallyStartPC = this.labelTable[getTargetLabel(finallyTarget)];
/*      */           
/*  417 */           addExceptionHandler(tryStart, finallyStartPC, finallyStartPC, true, exceptionObjectLocal, scopeLocal);
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  422 */         addIndexOp(-56, scopeLocal);
/*  423 */         releaseLocal(scopeLocal);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 57:
/*  429 */         localIndex = getLocalBlockRef(node);
/*  430 */         scopeIndex = node.getExistingIntProp(14);
/*  431 */         name = child.getString();
/*  432 */         child = child.getNext();
/*  433 */         visitExpression(child, 0);
/*  434 */         addStringPrefix(name);
/*  435 */         addIndexPrefix(localIndex);
/*  436 */         addToken(57);
/*  437 */         addUint8((scopeIndex != 0) ? 1 : 0);
/*  438 */         stackChange(-1);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 50:
/*  443 */         updateLineNumber(node);
/*  444 */         visitExpression(child, 0);
/*  445 */         addToken(50);
/*  446 */         addUint16(this.lineNumber & 0xFFFF);
/*  447 */         stackChange(-1);
/*      */         break;
/*      */       
/*      */       case 51:
/*  451 */         updateLineNumber(node);
/*  452 */         addIndexOp(51, getLocalBlockRef(node));
/*      */         break;
/*      */       
/*      */       case 4:
/*  456 */         updateLineNumber(node);
/*  457 */         if (node.getIntProp(20, 0) != 0) {
/*      */           
/*  459 */           addIcode(-63);
/*  460 */           addUint16(this.lineNumber & 0xFFFF); break;
/*  461 */         }  if (child != null) {
/*  462 */           visitExpression(child, 1);
/*  463 */           addToken(4);
/*  464 */           stackChange(-1); break;
/*      */         } 
/*  466 */         addIcode(-22);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 64:
/*  471 */         updateLineNumber(node);
/*  472 */         addToken(64);
/*      */         break;
/*      */       
/*      */       case 58:
/*      */       case 59:
/*      */       case 60:
/*  478 */         visitExpression(child, 0);
/*  479 */         addIndexOp(type, getLocalBlockRef(node));
/*  480 */         stackChange(-1);
/*      */         break;
/*      */       
/*      */       case -62:
/*      */         break;
/*      */       
/*      */       default:
/*  487 */         throw badTree(node);
/*      */     } 
/*      */     
/*  490 */     if (this.stackDepth != initialStackDepth)
/*  491 */       throw Kit.codeBug();  } private void visitExpression(Node node, int contextFlags) { int fnIndex, localIndex; Node lastChild; int argCount, afterSecondJumpStart; Node ifThen; boolean isName; FunctionNode fn; int callType, jump; Node ifElse; String property, name; int i;
/*      */     double num;
/*      */     int index, memberTypeFlags, queryPC;
/*      */     Node enterWith;
/*      */     int elseJumpStart, childCount;
/*      */     Node with;
/*  497 */     int afterElseJumpStart, inum, type = node.getType();
/*  498 */     Node child = node.getFirstChild();
/*  499 */     int savedStackDepth = this.stackDepth;
/*  500 */     switch (type)
/*      */     
/*      */     { 
/*      */       case 109:
/*  504 */         fnIndex = node.getExistingIntProp(1);
/*  505 */         fn = this.scriptOrFn.getFunctionNode(fnIndex);
/*      */         
/*  507 */         if (fn.getFunctionType() != 2) {
/*  508 */           throw Kit.codeBug();
/*      */         }
/*  510 */         addIndexOp(-19, fnIndex);
/*  511 */         stackChange(1);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 54:
/*  517 */         localIndex = getLocalBlockRef(node);
/*  518 */         addIndexOp(54, localIndex);
/*  519 */         stackChange(1);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 89:
/*  525 */         lastChild = node.getLastChild();
/*  526 */         while (child != lastChild) {
/*  527 */           visitExpression(child, 0);
/*  528 */           addIcode(-4);
/*  529 */           stackChange(-1);
/*  530 */           child = child.getNext();
/*      */         } 
/*      */         
/*  533 */         visitExpression(child, contextFlags & 0x1);
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 138:
/*  540 */         stackChange(1);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 30:
/*      */       case 38:
/*      */       case 70:
/*  547 */         if (type == 30) {
/*  548 */           visitExpression(child, 0);
/*      */         } else {
/*  550 */           generateCallFunAndThis(child);
/*      */         } 
/*  552 */         argCount = 0;
/*  553 */         while ((child = child.getNext()) != null) {
/*  554 */           visitExpression(child, 0);
/*  555 */           argCount++;
/*      */         } 
/*  557 */         callType = node.getIntProp(10, 0);
/*      */         
/*  559 */         if (type != 70 && callType != 0) {
/*      */           
/*  561 */           addIndexOp(-21, argCount);
/*  562 */           addUint8(callType);
/*  563 */           addUint8((type == 30) ? 1 : 0);
/*  564 */           addUint16(this.lineNumber & 0xFFFF);
/*      */         
/*      */         }
/*      */         else {
/*      */           
/*  569 */           if (type == 38 && (contextFlags & 0x1) != 0 && !this.compilerEnv.isGenerateDebugInfo() && !this.itsInTryFlag)
/*      */           {
/*      */             
/*  572 */             type = -55;
/*      */           }
/*  574 */           addIndexOp(type, argCount);
/*      */         } 
/*      */         
/*  577 */         if (type == 30) {
/*      */           
/*  579 */           stackChange(-argCount);
/*      */         }
/*      */         else {
/*      */           
/*  583 */           stackChange(-1 - argCount);
/*      */         } 
/*  585 */         if (argCount > this.itsData.itsMaxCalleeArgs) {
/*  586 */           this.itsData.itsMaxCalleeArgs = argCount;
/*      */         }
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 104:
/*      */       case 105:
/*  594 */         visitExpression(child, 0);
/*  595 */         addIcode(-1);
/*  596 */         stackChange(1);
/*  597 */         afterSecondJumpStart = this.iCodeTop;
/*  598 */         jump = (type == 105) ? 7 : 6;
/*  599 */         addGotoOp(jump);
/*  600 */         stackChange(-1);
/*  601 */         addIcode(-4);
/*  602 */         stackChange(-1);
/*  603 */         child = child.getNext();
/*      */         
/*  605 */         visitExpression(child, contextFlags & 0x1);
/*  606 */         resolveForwardGoto(afterSecondJumpStart);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 102:
/*  612 */         ifThen = child.getNext();
/*  613 */         ifElse = ifThen.getNext();
/*  614 */         visitExpression(child, 0);
/*  615 */         elseJumpStart = this.iCodeTop;
/*  616 */         addGotoOp(7);
/*  617 */         stackChange(-1);
/*      */         
/*  619 */         visitExpression(ifThen, contextFlags & 0x1);
/*  620 */         afterElseJumpStart = this.iCodeTop;
/*  621 */         addGotoOp(5);
/*  622 */         resolveForwardGoto(elseJumpStart);
/*  623 */         this.stackDepth = savedStackDepth;
/*      */         
/*  625 */         visitExpression(ifElse, contextFlags & 0x1);
/*  626 */         resolveForwardGoto(afterElseJumpStart);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 33:
/*      */       case 34:
/*  632 */         visitExpression(child, 0);
/*  633 */         child = child.getNext();
/*  634 */         addStringOp(type, child.getString());
/*      */         break;
/*      */       
/*      */       case 31:
/*  638 */         isName = (child.getType() == 49);
/*  639 */         visitExpression(child, 0);
/*  640 */         child = child.getNext();
/*  641 */         visitExpression(child, 0);
/*  642 */         if (isName) {
/*      */           
/*  644 */           addIcode(0);
/*      */         } else {
/*  646 */           addToken(31);
/*      */         } 
/*  648 */         stackChange(-1);
/*      */         break;
/*      */       
/*      */       case 9:
/*      */       case 10:
/*      */       case 11:
/*      */       case 12:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/*      */       case 19:
/*      */       case 20:
/*      */       case 21:
/*      */       case 22:
/*      */       case 23:
/*      */       case 24:
/*      */       case 25:
/*      */       case 36:
/*      */       case 46:
/*      */       case 47:
/*      */       case 52:
/*      */       case 53:
/*  673 */         visitExpression(child, 0);
/*  674 */         child = child.getNext();
/*  675 */         visitExpression(child, 0);
/*  676 */         addToken(type);
/*  677 */         stackChange(-1);
/*      */         break;
/*      */       
/*      */       case 26:
/*      */       case 27:
/*      */       case 28:
/*      */       case 29:
/*      */       case 32:
/*      */       case 126:
/*  686 */         visitExpression(child, 0);
/*  687 */         if (type == 126) {
/*  688 */           addIcode(-4);
/*  689 */           addIcode(-50); break;
/*      */         } 
/*  691 */         addToken(type);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 67:
/*      */       case 69:
/*  697 */         visitExpression(child, 0);
/*  698 */         addToken(type);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 35:
/*      */       case 139:
/*  704 */         visitExpression(child, 0);
/*  705 */         child = child.getNext();
/*  706 */         property = child.getString();
/*  707 */         child = child.getNext();
/*  708 */         if (type == 139) {
/*  709 */           addIcode(-1);
/*  710 */           stackChange(1);
/*  711 */           addStringOp(33, property);
/*      */           
/*  713 */           stackChange(-1);
/*      */         } 
/*  715 */         visitExpression(child, 0);
/*  716 */         addStringOp(35, property);
/*  717 */         stackChange(-1);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 37:
/*      */       case 140:
/*  723 */         visitExpression(child, 0);
/*  724 */         child = child.getNext();
/*  725 */         visitExpression(child, 0);
/*  726 */         child = child.getNext();
/*  727 */         if (type == 140) {
/*  728 */           addIcode(-2);
/*  729 */           stackChange(2);
/*  730 */           addToken(36);
/*  731 */           stackChange(-1);
/*      */           
/*  733 */           stackChange(-1);
/*      */         } 
/*  735 */         visitExpression(child, 0);
/*  736 */         addToken(37);
/*  737 */         stackChange(-2);
/*      */         break;
/*      */       
/*      */       case 68:
/*      */       case 142:
/*  742 */         visitExpression(child, 0);
/*  743 */         child = child.getNext();
/*  744 */         if (type == 142) {
/*  745 */           addIcode(-1);
/*  746 */           stackChange(1);
/*  747 */           addToken(67);
/*      */           
/*  749 */           stackChange(-1);
/*      */         } 
/*  751 */         visitExpression(child, 0);
/*  752 */         addToken(68);
/*  753 */         stackChange(-1);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 8:
/*      */       case 73:
/*  759 */         name = child.getString();
/*  760 */         visitExpression(child, 0);
/*  761 */         child = child.getNext();
/*  762 */         visitExpression(child, 0);
/*  763 */         addStringOp(type, name);
/*  764 */         stackChange(-1);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 155:
/*  770 */         name = child.getString();
/*  771 */         visitExpression(child, 0);
/*  772 */         child = child.getNext();
/*  773 */         visitExpression(child, 0);
/*  774 */         addStringOp(-59, name);
/*  775 */         stackChange(-1);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 137:
/*  781 */         i = -1;
/*      */ 
/*      */         
/*  784 */         if (this.itsInFunctionFlag && !this.itsData.itsNeedsActivation)
/*  785 */           i = this.scriptOrFn.getIndexForNameNode(node); 
/*  786 */         if (i == -1) {
/*  787 */           addStringOp(-14, node.getString());
/*  788 */           stackChange(1); break;
/*      */         } 
/*  790 */         addVarOp(55, i);
/*  791 */         stackChange(1);
/*  792 */         addToken(32);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 39:
/*      */       case 41:
/*      */       case 49:
/*  800 */         addStringOp(type, node.getString());
/*  801 */         stackChange(1);
/*      */         break;
/*      */       
/*      */       case 106:
/*      */       case 107:
/*  806 */         visitIncDec(node, child);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 40:
/*  811 */         num = node.getDouble();
/*  812 */         inum = (int)num;
/*  813 */         if (inum == num) {
/*  814 */           if (inum == 0) {
/*  815 */             addIcode(-51);
/*      */             
/*  817 */             if (1.0D / num < 0.0D) {
/*  818 */               addToken(29);
/*      */             }
/*  820 */           } else if (inum == 1) {
/*  821 */             addIcode(-52);
/*  822 */           } else if ((short)inum == inum) {
/*  823 */             addIcode(-27);
/*      */             
/*  825 */             addUint16(inum & 0xFFFF);
/*      */           } else {
/*  827 */             addIcode(-28);
/*  828 */             addInt(inum);
/*      */           } 
/*      */         } else {
/*  831 */           int j = getDoubleIndex(num);
/*  832 */           addIndexOp(40, j);
/*      */         } 
/*  834 */         stackChange(1);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 55:
/*  840 */         if (this.itsData.itsNeedsActivation) Kit.codeBug(); 
/*  841 */         index = this.scriptOrFn.getIndexForNameNode(node);
/*  842 */         addVarOp(55, index);
/*  843 */         stackChange(1);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 56:
/*  849 */         if (this.itsData.itsNeedsActivation) Kit.codeBug(); 
/*  850 */         index = this.scriptOrFn.getIndexForNameNode(child);
/*  851 */         child = child.getNext();
/*  852 */         visitExpression(child, 0);
/*  853 */         addVarOp(56, index);
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 156:
/*  859 */         if (this.itsData.itsNeedsActivation) Kit.codeBug(); 
/*  860 */         index = this.scriptOrFn.getIndexForNameNode(child);
/*  861 */         child = child.getNext();
/*  862 */         visitExpression(child, 0);
/*  863 */         addVarOp(156, index);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 42:
/*      */       case 43:
/*      */       case 44:
/*      */       case 45:
/*      */       case 63:
/*  872 */         addToken(type);
/*  873 */         stackChange(1);
/*      */         break;
/*      */       
/*      */       case 61:
/*      */       case 62:
/*  878 */         addIndexOp(type, getLocalBlockRef(node));
/*  879 */         stackChange(1);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 48:
/*  884 */         index = node.getExistingIntProp(4);
/*  885 */         addIndexOp(48, index);
/*  886 */         stackChange(1);
/*      */         break;
/*      */ 
/*      */       
/*      */       case 65:
/*      */       case 66:
/*  892 */         visitLiteral(node, child);
/*      */         break;
/*      */       
/*      */       case 157:
/*  896 */         visitArrayComprehension(node, child, child.getNext());
/*      */         break;
/*      */       
/*      */       case 71:
/*  900 */         visitExpression(child, 0);
/*  901 */         addStringOp(type, (String)node.getProp(17));
/*      */         break;
/*      */ 
/*      */       
/*      */       case 77:
/*      */       case 78:
/*      */       case 79:
/*      */       case 80:
/*  909 */         memberTypeFlags = node.getIntProp(16, 0);
/*      */         
/*  911 */         childCount = 0;
/*      */         while (true)
/*  913 */         { visitExpression(child, 0);
/*  914 */           childCount++;
/*  915 */           child = child.getNext();
/*  916 */           if (child == null)
/*  917 */           { addIndexOp(type, memberTypeFlags);
/*  918 */             stackChange(1 - childCount);
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
/*  967 */             if (savedStackDepth + 1 != this.stackDepth)
/*  968 */               Kit.codeBug();  return; }  } case 146: updateLineNumber(node); visitExpression(child, 0); addIcode(-53); stackChange(-1); queryPC = this.iCodeTop; visitExpression(child.getNext(), 0); addBackwardGoto(-54, queryPC); break;case 74: case 75: case 76: visitExpression(child, 0); addToken(type); break;case 72: if (child != null) { visitExpression(child, 0); } else { addIcode(-50); stackChange(1); }  addToken(72); addUint16(node.getLineno() & 0xFFFF); break;case 159: enterWith = node.getFirstChild(); with = enterWith.getNext(); visitExpression(enterWith.getFirstChild(), 0); addToken(2); stackChange(-1); visitExpression(with.getFirstChild(), 0); addToken(3); break;default: throw badTree(node); }  if (savedStackDepth + 1 != this.stackDepth) Kit.codeBug();
/*      */      }
/*      */ 
/*      */   
/*      */   private void generateCallFunAndThis(Node left) {
/*      */     String name;
/*      */     Node target, id;
/*  975 */     int type = left.getType();
/*  976 */     switch (type) {
/*      */       case 39:
/*  978 */         name = left.getString();
/*      */         
/*  980 */         addStringOp(-15, name);
/*  981 */         stackChange(2);
/*      */         return;
/*      */       
/*      */       case 33:
/*      */       case 36:
/*  986 */         target = left.getFirstChild();
/*  987 */         visitExpression(target, 0);
/*  988 */         id = target.getNext();
/*  989 */         if (type == 33) {
/*  990 */           String property = id.getString();
/*      */           
/*  992 */           addStringOp(-16, property);
/*  993 */           stackChange(1);
/*      */         } else {
/*  995 */           visitExpression(id, 0);
/*      */           
/*  997 */           addIcode(-17);
/*      */         } 
/*      */         return;
/*      */     } 
/*      */ 
/*      */     
/* 1003 */     visitExpression(left, 0);
/*      */     
/* 1005 */     addIcode(-18);
/* 1006 */     stackChange(1);
/*      */   }
/*      */   private void visitIncDec(Node node, Node child) {
/*      */     int i;
/*      */     String name;
/*      */     Node object, ref;
/*      */     String property;
/*      */     Node index;
/* 1014 */     int incrDecrMask = node.getExistingIntProp(13);
/* 1015 */     int childType = child.getType();
/* 1016 */     switch (childType) {
/*      */       case 55:
/* 1018 */         if (this.itsData.itsNeedsActivation) Kit.codeBug(); 
/* 1019 */         i = this.scriptOrFn.getIndexForNameNode(child);
/* 1020 */         addVarOp(-7, i);
/* 1021 */         addUint8(incrDecrMask);
/* 1022 */         stackChange(1);
/*      */         return;
/*      */       
/*      */       case 39:
/* 1026 */         name = child.getString();
/* 1027 */         addStringOp(-8, name);
/* 1028 */         addUint8(incrDecrMask);
/* 1029 */         stackChange(1);
/*      */         return;
/*      */       
/*      */       case 33:
/* 1033 */         object = child.getFirstChild();
/* 1034 */         visitExpression(object, 0);
/* 1035 */         property = object.getNext().getString();
/* 1036 */         addStringOp(-9, property);
/* 1037 */         addUint8(incrDecrMask);
/*      */         return;
/*      */       
/*      */       case 36:
/* 1041 */         object = child.getFirstChild();
/* 1042 */         visitExpression(object, 0);
/* 1043 */         index = object.getNext();
/* 1044 */         visitExpression(index, 0);
/* 1045 */         addIcode(-10);
/* 1046 */         addUint8(incrDecrMask);
/* 1047 */         stackChange(-1);
/*      */         return;
/*      */       
/*      */       case 67:
/* 1051 */         ref = child.getFirstChild();
/* 1052 */         visitExpression(ref, 0);
/* 1053 */         addIcode(-11);
/* 1054 */         addUint8(incrDecrMask);
/*      */         return;
/*      */     } 
/*      */     
/* 1058 */     throw badTree(node);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void visitLiteral(Node node, Node child) {
/* 1065 */     int count, type = node.getType();
/*      */     
/* 1067 */     Object[] propertyIds = null;
/* 1068 */     if (type == 65) {
/* 1069 */       count = 0;
/* 1070 */       for (Node n = child; n != null; n = n.getNext()) {
/* 1071 */         count++;
/*      */       }
/* 1073 */     } else if (type == 66) {
/* 1074 */       propertyIds = (Object[])node.getProp(12);
/* 1075 */       count = propertyIds.length;
/*      */     } else {
/* 1077 */       throw badTree(node);
/*      */     } 
/* 1079 */     addIndexOp(-29, count);
/* 1080 */     stackChange(2);
/* 1081 */     while (child != null) {
/* 1082 */       int childType = child.getType();
/* 1083 */       if (childType == 151) {
/* 1084 */         visitExpression(child.getFirstChild(), 0);
/* 1085 */         addIcode(-57);
/* 1086 */       } else if (childType == 152) {
/* 1087 */         visitExpression(child.getFirstChild(), 0);
/* 1088 */         addIcode(-58);
/*      */       } else {
/* 1090 */         visitExpression(child, 0);
/* 1091 */         addIcode(-30);
/*      */       } 
/* 1093 */       stackChange(-1);
/* 1094 */       child = child.getNext();
/*      */     } 
/* 1096 */     if (type == 65) {
/* 1097 */       int[] skipIndexes = (int[])node.getProp(11);
/* 1098 */       if (skipIndexes == null) {
/* 1099 */         addToken(65);
/*      */       } else {
/* 1101 */         int index = this.literalIds.size();
/* 1102 */         this.literalIds.add(skipIndexes);
/* 1103 */         addIndexOp(-31, index);
/*      */       } 
/*      */     } else {
/* 1106 */       int index = this.literalIds.size();
/* 1107 */       this.literalIds.add(propertyIds);
/* 1108 */       addIndexOp(66, index);
/*      */     } 
/* 1110 */     stackChange(-1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void visitArrayComprehension(Node node, Node initStmt, Node expr) {
/* 1120 */     visitStatement(initStmt, this.stackDepth);
/* 1121 */     visitExpression(expr, 0);
/*      */   }
/*      */ 
/*      */   
/*      */   private int getLocalBlockRef(Node node) {
/* 1126 */     Node localBlock = (Node)node.getProp(3);
/* 1127 */     return localBlock.getExistingIntProp(2);
/*      */   }
/*      */ 
/*      */   
/*      */   private int getTargetLabel(Node target) {
/* 1132 */     int label = target.labelId();
/* 1133 */     if (label != -1) {
/* 1134 */       return label;
/*      */     }
/* 1136 */     label = this.labelTableTop;
/* 1137 */     if (this.labelTable == null || label == this.labelTable.length) {
/* 1138 */       if (this.labelTable == null) {
/* 1139 */         this.labelTable = new int[32];
/*      */       } else {
/* 1141 */         int[] tmp = new int[this.labelTable.length * 2];
/* 1142 */         System.arraycopy(this.labelTable, 0, tmp, 0, label);
/* 1143 */         this.labelTable = tmp;
/*      */       } 
/*      */     }
/* 1146 */     this.labelTableTop = label + 1;
/* 1147 */     this.labelTable[label] = -1;
/*      */     
/* 1149 */     target.labelId(label);
/* 1150 */     return label;
/*      */   }
/*      */ 
/*      */   
/*      */   private void markTargetLabel(Node target) {
/* 1155 */     int label = getTargetLabel(target);
/* 1156 */     if (this.labelTable[label] != -1)
/*      */     {
/* 1158 */       Kit.codeBug();
/*      */     }
/* 1160 */     this.labelTable[label] = this.iCodeTop;
/*      */   }
/*      */ 
/*      */   
/*      */   private void addGoto(Node target, int gotoOp) {
/* 1165 */     int label = getTargetLabel(target);
/* 1166 */     if (label >= this.labelTableTop) Kit.codeBug(); 
/* 1167 */     int targetPC = this.labelTable[label];
/*      */     
/* 1169 */     if (targetPC != -1) {
/* 1170 */       addBackwardGoto(gotoOp, targetPC);
/*      */     } else {
/* 1172 */       int gotoPC = this.iCodeTop;
/* 1173 */       addGotoOp(gotoOp);
/* 1174 */       int top = this.fixupTableTop;
/* 1175 */       if (this.fixupTable == null || top == this.fixupTable.length) {
/* 1176 */         if (this.fixupTable == null) {
/* 1177 */           this.fixupTable = new long[40];
/*      */         } else {
/* 1179 */           long[] tmp = new long[this.fixupTable.length * 2];
/* 1180 */           System.arraycopy(this.fixupTable, 0, tmp, 0, top);
/* 1181 */           this.fixupTable = tmp;
/*      */         } 
/*      */       }
/* 1184 */       this.fixupTableTop = top + 1;
/* 1185 */       this.fixupTable[top] = label << 32L | gotoPC;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void fixLabelGotos() {
/* 1191 */     for (int i = 0; i < this.fixupTableTop; i++) {
/* 1192 */       long fixup = this.fixupTable[i];
/* 1193 */       int label = (int)(fixup >> 32L);
/* 1194 */       int jumpSource = (int)fixup;
/* 1195 */       int pc = this.labelTable[label];
/* 1196 */       if (pc == -1)
/*      */       {
/* 1198 */         throw Kit.codeBug();
/*      */       }
/* 1200 */       resolveGoto(jumpSource, pc);
/*      */     } 
/* 1202 */     this.fixupTableTop = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   private void addBackwardGoto(int gotoOp, int jumpPC) {
/* 1207 */     int fromPC = this.iCodeTop;
/*      */     
/* 1209 */     if (fromPC <= jumpPC) throw Kit.codeBug(); 
/* 1210 */     addGotoOp(gotoOp);
/* 1211 */     resolveGoto(fromPC, jumpPC);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void resolveForwardGoto(int fromPC) {
/* 1217 */     if (this.iCodeTop < fromPC + 3) throw Kit.codeBug(); 
/* 1218 */     resolveGoto(fromPC, this.iCodeTop);
/*      */   }
/*      */ 
/*      */   
/*      */   private void resolveGoto(int fromPC, int jumpPC) {
/* 1223 */     int offset = jumpPC - fromPC;
/*      */     
/* 1225 */     if (0 <= offset && offset <= 2) throw Kit.codeBug(); 
/* 1226 */     int offsetSite = fromPC + 1;
/* 1227 */     if (offset != (short)offset) {
/* 1228 */       if (this.itsData.longJumps == null) {
/* 1229 */         this.itsData.longJumps = new UintMap();
/*      */       }
/* 1231 */       this.itsData.longJumps.put(offsetSite, jumpPC);
/* 1232 */       offset = 0;
/*      */     } 
/* 1234 */     byte[] array = this.itsData.itsICode;
/* 1235 */     array[offsetSite] = (byte)(offset >> 8);
/* 1236 */     array[offsetSite + 1] = (byte)offset;
/*      */   }
/*      */ 
/*      */   
/*      */   private void addToken(int token) {
/* 1241 */     if (!Icode.validTokenCode(token)) throw Kit.codeBug(); 
/* 1242 */     addUint8(token);
/*      */   }
/*      */ 
/*      */   
/*      */   private void addIcode(int icode) {
/* 1247 */     if (!Icode.validIcode(icode)) throw Kit.codeBug();
/*      */     
/* 1249 */     addUint8(icode & 0xFF);
/*      */   }
/*      */ 
/*      */   
/*      */   private void addUint8(int value) {
/* 1254 */     if ((value & 0xFFFFFF00) != 0) throw Kit.codeBug(); 
/* 1255 */     byte[] array = this.itsData.itsICode;
/* 1256 */     int top = this.iCodeTop;
/* 1257 */     if (top == array.length) {
/* 1258 */       array = increaseICodeCapacity(1);
/*      */     }
/* 1260 */     array[top] = (byte)value;
/* 1261 */     this.iCodeTop = top + 1;
/*      */   }
/*      */ 
/*      */   
/*      */   private void addUint16(int value) {
/* 1266 */     if ((value & 0xFFFF0000) != 0) throw Kit.codeBug(); 
/* 1267 */     byte[] array = this.itsData.itsICode;
/* 1268 */     int top = this.iCodeTop;
/* 1269 */     if (top + 2 > array.length) {
/* 1270 */       array = increaseICodeCapacity(2);
/*      */     }
/* 1272 */     array[top] = (byte)(value >>> 8);
/* 1273 */     array[top + 1] = (byte)value;
/* 1274 */     this.iCodeTop = top + 2;
/*      */   }
/*      */ 
/*      */   
/*      */   private void addInt(int i) {
/* 1279 */     byte[] array = this.itsData.itsICode;
/* 1280 */     int top = this.iCodeTop;
/* 1281 */     if (top + 4 > array.length) {
/* 1282 */       array = increaseICodeCapacity(4);
/*      */     }
/* 1284 */     array[top] = (byte)(i >>> 24);
/* 1285 */     array[top + 1] = (byte)(i >>> 16);
/* 1286 */     array[top + 2] = (byte)(i >>> 8);
/* 1287 */     array[top + 3] = (byte)i;
/* 1288 */     this.iCodeTop = top + 4;
/*      */   }
/*      */ 
/*      */   
/*      */   private int getDoubleIndex(double num) {
/* 1293 */     int index = this.doubleTableTop;
/* 1294 */     if (index == 0) {
/* 1295 */       this.itsData.itsDoubleTable = new double[64];
/* 1296 */     } else if (this.itsData.itsDoubleTable.length == index) {
/* 1297 */       double[] na = new double[index * 2];
/* 1298 */       System.arraycopy(this.itsData.itsDoubleTable, 0, na, 0, index);
/* 1299 */       this.itsData.itsDoubleTable = na;
/*      */     } 
/* 1301 */     this.itsData.itsDoubleTable[index] = num;
/* 1302 */     this.doubleTableTop = index + 1;
/* 1303 */     return index;
/*      */   }
/*      */ 
/*      */   
/*      */   private void addGotoOp(int gotoOp) {
/* 1308 */     byte[] array = this.itsData.itsICode;
/* 1309 */     int top = this.iCodeTop;
/* 1310 */     if (top + 3 > array.length) {
/* 1311 */       array = increaseICodeCapacity(3);
/*      */     }
/* 1313 */     array[top] = (byte)gotoOp;
/*      */     
/* 1315 */     this.iCodeTop = top + 1 + 2;
/*      */   }
/*      */ 
/*      */   
/*      */   private void addVarOp(int op, int varIndex) {
/* 1320 */     switch (op) {
/*      */       case 156:
/* 1322 */         if (varIndex < 128) {
/* 1323 */           addIcode(-61);
/* 1324 */           addUint8(varIndex);
/*      */           return;
/*      */         } 
/* 1327 */         addIndexOp(-60, varIndex);
/*      */         return;
/*      */       case 55:
/*      */       case 56:
/* 1331 */         if (varIndex < 128) {
/* 1332 */           addIcode((op == 55) ? -48 : -49);
/* 1333 */           addUint8(varIndex);
/*      */           return;
/*      */         } 
/*      */       
/*      */       case -7:
/* 1338 */         addIndexOp(op, varIndex);
/*      */         return;
/*      */     } 
/* 1341 */     throw Kit.codeBug();
/*      */   }
/*      */ 
/*      */   
/*      */   private void addStringOp(int op, String str) {
/* 1346 */     addStringPrefix(str);
/* 1347 */     if (Icode.validIcode(op)) {
/* 1348 */       addIcode(op);
/*      */     } else {
/* 1350 */       addToken(op);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void addIndexOp(int op, int index) {
/* 1356 */     addIndexPrefix(index);
/* 1357 */     if (Icode.validIcode(op)) {
/* 1358 */       addIcode(op);
/*      */     } else {
/* 1360 */       addToken(op);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void addStringPrefix(String str) {
/* 1366 */     int index = this.strings.get(str, -1);
/* 1367 */     if (index == -1) {
/* 1368 */       index = this.strings.size();
/* 1369 */       this.strings.put(str, index);
/*      */     } 
/* 1371 */     if (index < 4) {
/* 1372 */       addIcode(-41 - index);
/* 1373 */     } else if (index <= 255) {
/* 1374 */       addIcode(-45);
/* 1375 */       addUint8(index);
/* 1376 */     } else if (index <= 65535) {
/* 1377 */       addIcode(-46);
/* 1378 */       addUint16(index);
/*      */     } else {
/* 1380 */       addIcode(-47);
/* 1381 */       addInt(index);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void addIndexPrefix(int index) {
/* 1387 */     if (index < 0) Kit.codeBug(); 
/* 1388 */     if (index < 6) {
/* 1389 */       addIcode(-32 - index);
/* 1390 */     } else if (index <= 255) {
/* 1391 */       addIcode(-38);
/* 1392 */       addUint8(index);
/* 1393 */     } else if (index <= 65535) {
/* 1394 */       addIcode(-39);
/* 1395 */       addUint16(index);
/*      */     } else {
/* 1397 */       addIcode(-40);
/* 1398 */       addInt(index);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addExceptionHandler(int icodeStart, int icodeEnd, int handlerStart, boolean isFinally, int exceptionObjectLocal, int scopeLocal) {
/* 1406 */     int top = this.exceptionTableTop;
/* 1407 */     int[] table = this.itsData.itsExceptionTable;
/* 1408 */     if (table == null) {
/* 1409 */       if (top != 0) Kit.codeBug(); 
/* 1410 */       table = new int[12];
/* 1411 */       this.itsData.itsExceptionTable = table;
/* 1412 */     } else if (table.length == top) {
/* 1413 */       table = new int[table.length * 2];
/* 1414 */       System.arraycopy(this.itsData.itsExceptionTable, 0, table, 0, top);
/* 1415 */       this.itsData.itsExceptionTable = table;
/*      */     } 
/* 1417 */     table[top + 0] = icodeStart;
/* 1418 */     table[top + 1] = icodeEnd;
/* 1419 */     table[top + 2] = handlerStart;
/* 1420 */     table[top + 3] = isFinally ? 1 : 0;
/* 1421 */     table[top + 4] = exceptionObjectLocal;
/* 1422 */     table[top + 5] = scopeLocal;
/*      */     
/* 1424 */     this.exceptionTableTop = top + 6;
/*      */   }
/*      */ 
/*      */   
/*      */   private byte[] increaseICodeCapacity(int extraSize) {
/* 1429 */     int capacity = this.itsData.itsICode.length;
/* 1430 */     int top = this.iCodeTop;
/* 1431 */     if (top + extraSize <= capacity) throw Kit.codeBug(); 
/* 1432 */     capacity *= 2;
/* 1433 */     if (top + extraSize > capacity) {
/* 1434 */       capacity = top + extraSize;
/*      */     }
/* 1436 */     byte[] array = new byte[capacity];
/* 1437 */     System.arraycopy(this.itsData.itsICode, 0, array, 0, top);
/* 1438 */     this.itsData.itsICode = array;
/* 1439 */     return array;
/*      */   }
/*      */ 
/*      */   
/*      */   private void stackChange(int change) {
/* 1444 */     if (change <= 0) {
/* 1445 */       this.stackDepth += change;
/*      */     } else {
/* 1447 */       int newDepth = this.stackDepth + change;
/* 1448 */       if (newDepth > this.itsData.itsMaxStack) {
/* 1449 */         this.itsData.itsMaxStack = newDepth;
/*      */       }
/* 1451 */       this.stackDepth = newDepth;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private int allocLocal() {
/* 1457 */     int localSlot = this.localTop;
/* 1458 */     this.localTop++;
/* 1459 */     if (this.localTop > this.itsData.itsMaxLocals) {
/* 1460 */       this.itsData.itsMaxLocals = this.localTop;
/*      */     }
/* 1462 */     return localSlot;
/*      */   }
/*      */ 
/*      */   
/*      */   private void releaseLocal(int localSlot) {
/* 1467 */     this.localTop--;
/* 1468 */     if (localSlot != this.localTop) Kit.codeBug(); 
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\CodeGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */