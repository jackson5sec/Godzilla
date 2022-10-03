/*      */ package javassist.compiler;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import javassist.bytecode.Bytecode;
/*      */ import javassist.bytecode.Opcode;
/*      */ import javassist.compiler.ast.ASTList;
/*      */ import javassist.compiler.ast.ASTree;
/*      */ import javassist.compiler.ast.ArrayInit;
/*      */ import javassist.compiler.ast.AssignExpr;
/*      */ import javassist.compiler.ast.BinExpr;
/*      */ import javassist.compiler.ast.CallExpr;
/*      */ import javassist.compiler.ast.CastExpr;
/*      */ import javassist.compiler.ast.CondExpr;
/*      */ import javassist.compiler.ast.Declarator;
/*      */ import javassist.compiler.ast.DoubleConst;
/*      */ import javassist.compiler.ast.Expr;
/*      */ import javassist.compiler.ast.FieldDecl;
/*      */ import javassist.compiler.ast.InstanceOfExpr;
/*      */ import javassist.compiler.ast.IntConst;
/*      */ import javassist.compiler.ast.Keyword;
/*      */ import javassist.compiler.ast.Member;
/*      */ import javassist.compiler.ast.MethodDecl;
/*      */ import javassist.compiler.ast.NewExpr;
/*      */ import javassist.compiler.ast.Pair;
/*      */ import javassist.compiler.ast.Stmnt;
/*      */ import javassist.compiler.ast.StringL;
/*      */ import javassist.compiler.ast.Symbol;
/*      */ import javassist.compiler.ast.Variable;
/*      */ import javassist.compiler.ast.Visitor;
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
/*      */ public abstract class CodeGen
/*      */   extends Visitor
/*      */   implements Opcode, TokenId
/*      */ {
/*      */   static final String javaLangObject = "java.lang.Object";
/*      */   static final String jvmJavaLangObject = "java/lang/Object";
/*      */   static final String javaLangString = "java.lang.String";
/*      */   static final String jvmJavaLangString = "java/lang/String";
/*      */   protected Bytecode bytecode;
/*      */   private int tempVar;
/*      */   TypeChecker typeChecker;
/*      */   protected boolean hasReturned;
/*      */   public boolean inStaticMethod;
/*      */   protected List<Integer> breakList;
/*      */   protected List<Integer> continueList;
/*      */   protected ReturnHook returnHooks;
/*      */   protected int exprType;
/*      */   protected int arrayDim;
/*      */   protected String className;
/*      */   
/*      */   protected static abstract class ReturnHook
/*      */   {
/*      */     ReturnHook next;
/*      */     
/*      */     protected abstract boolean doit(Bytecode param1Bytecode, int param1Int);
/*      */     
/*      */     protected ReturnHook(CodeGen gen) {
/*   91 */       this.next = gen.returnHooks;
/*   92 */       gen.returnHooks = this;
/*      */     }
/*      */     
/*      */     protected void remove(CodeGen gen) {
/*   96 */       gen.returnHooks = this.next;
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
/*      */   public CodeGen(Bytecode b) {
/*  110 */     this.bytecode = b;
/*  111 */     this.tempVar = -1;
/*  112 */     this.typeChecker = null;
/*  113 */     this.hasReturned = false;
/*  114 */     this.inStaticMethod = false;
/*  115 */     this.breakList = null;
/*  116 */     this.continueList = null;
/*  117 */     this.returnHooks = null;
/*      */   }
/*      */   
/*      */   public void setTypeChecker(TypeChecker checker) {
/*  121 */     this.typeChecker = checker;
/*      */   }
/*      */   
/*      */   protected static void fatal() throws CompileError {
/*  125 */     throw new CompileError("fatal");
/*      */   }
/*      */   
/*      */   public static boolean is2word(int type, int dim) {
/*  129 */     return (dim == 0 && (type == 312 || type == 326));
/*      */   }
/*      */   public int getMaxLocals() {
/*  132 */     return this.bytecode.getMaxLocals();
/*      */   }
/*      */   public void setMaxLocals(int n) {
/*  135 */     this.bytecode.setMaxLocals(n);
/*      */   }
/*      */   
/*      */   protected void incMaxLocals(int size) {
/*  139 */     this.bytecode.incMaxLocals(size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int getTempVar() {
/*  147 */     if (this.tempVar < 0) {
/*  148 */       this.tempVar = getMaxLocals();
/*  149 */       incMaxLocals(2);
/*      */     } 
/*      */     
/*  152 */     return this.tempVar;
/*      */   }
/*      */   
/*      */   protected int getLocalVar(Declarator d) {
/*  156 */     int v = d.getLocalVar();
/*  157 */     if (v < 0) {
/*  158 */       v = getMaxLocals();
/*  159 */       d.setLocalVar(v);
/*  160 */       incMaxLocals(1);
/*      */     } 
/*      */     
/*  163 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract String getThisName();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract String getSuperName() throws CompileError;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract String resolveClassName(ASTList paramASTList) throws CompileError;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract String resolveClassName(String paramString) throws CompileError;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static String toJvmArrayName(String name, int dim) {
/*  195 */     if (name == null) {
/*  196 */       return null;
/*      */     }
/*  198 */     if (dim == 0)
/*  199 */       return name; 
/*  200 */     StringBuffer sbuf = new StringBuffer();
/*  201 */     int d = dim;
/*  202 */     while (d-- > 0) {
/*  203 */       sbuf.append('[');
/*      */     }
/*  205 */     sbuf.append('L');
/*  206 */     sbuf.append(name);
/*  207 */     sbuf.append(';');
/*      */     
/*  209 */     return sbuf.toString();
/*      */   }
/*      */   
/*      */   protected static String toJvmTypeName(int type, int dim) {
/*  213 */     char c = 'I';
/*  214 */     switch (type) {
/*      */       case 301:
/*  216 */         c = 'Z';
/*      */         break;
/*      */       case 303:
/*  219 */         c = 'B';
/*      */         break;
/*      */       case 306:
/*  222 */         c = 'C';
/*      */         break;
/*      */       case 334:
/*  225 */         c = 'S';
/*      */         break;
/*      */       case 324:
/*  228 */         c = 'I';
/*      */         break;
/*      */       case 326:
/*  231 */         c = 'J';
/*      */         break;
/*      */       case 317:
/*  234 */         c = 'F';
/*      */         break;
/*      */       case 312:
/*  237 */         c = 'D';
/*      */         break;
/*      */       case 344:
/*  240 */         c = 'V';
/*      */         break;
/*      */     } 
/*      */     
/*  244 */     StringBuffer sbuf = new StringBuffer();
/*  245 */     while (dim-- > 0) {
/*  246 */       sbuf.append('[');
/*      */     }
/*  248 */     sbuf.append(c);
/*  249 */     return sbuf.toString();
/*      */   }
/*      */   
/*      */   public void compileExpr(ASTree expr) throws CompileError {
/*  253 */     doTypeCheck(expr);
/*  254 */     expr.accept(this);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean compileBooleanExpr(boolean branchIf, ASTree expr) throws CompileError {
/*  260 */     doTypeCheck(expr);
/*  261 */     return booleanExpr(branchIf, expr);
/*      */   }
/*      */   
/*      */   public void doTypeCheck(ASTree expr) throws CompileError {
/*  265 */     if (this.typeChecker != null)
/*  266 */       expr.accept(this.typeChecker); 
/*      */   }
/*      */   
/*      */   public void atASTList(ASTList n) throws CompileError {
/*  270 */     fatal();
/*      */   }
/*      */   public void atPair(Pair n) throws CompileError {
/*  273 */     fatal();
/*      */   }
/*      */   public void atSymbol(Symbol n) throws CompileError {
/*  276 */     fatal();
/*      */   }
/*      */   
/*      */   public void atFieldDecl(FieldDecl field) throws CompileError {
/*  280 */     field.getInit().accept(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public void atMethodDecl(MethodDecl method) throws CompileError {
/*  285 */     ASTList mods = method.getModifiers();
/*  286 */     setMaxLocals(1);
/*  287 */     while (mods != null) {
/*  288 */       Keyword k = (Keyword)mods.head();
/*  289 */       mods = mods.tail();
/*  290 */       if (k.get() == 335) {
/*  291 */         setMaxLocals(0);
/*  292 */         this.inStaticMethod = true;
/*      */       } 
/*      */     } 
/*      */     
/*  296 */     ASTList params = method.getParams();
/*  297 */     while (params != null) {
/*  298 */       atDeclarator((Declarator)params.head());
/*  299 */       params = params.tail();
/*      */     } 
/*      */     
/*  302 */     Stmnt s = method.getBody();
/*  303 */     atMethodBody(s, method.isConstructor(), 
/*  304 */         (method.getReturn().getType() == 344));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void atMethodBody(Stmnt s, boolean isCons, boolean isVoid) throws CompileError {
/*  314 */     if (s == null) {
/*      */       return;
/*      */     }
/*  317 */     if (isCons && needsSuperCall(s)) {
/*  318 */       insertDefaultSuperCall();
/*      */     }
/*  320 */     this.hasReturned = false;
/*  321 */     s.accept(this);
/*  322 */     if (!this.hasReturned)
/*  323 */       if (isVoid) {
/*  324 */         this.bytecode.addOpcode(177);
/*  325 */         this.hasReturned = true;
/*      */       } else {
/*      */         
/*  328 */         throw new CompileError("no return statement");
/*      */       }  
/*      */   }
/*      */   private boolean needsSuperCall(Stmnt body) throws CompileError {
/*  332 */     if (body.getOperator() == 66) {
/*  333 */       body = (Stmnt)body.head();
/*      */     }
/*  335 */     if (body != null && body.getOperator() == 69) {
/*  336 */       ASTree expr = body.head();
/*  337 */       if (expr != null && expr instanceof Expr && ((Expr)expr)
/*  338 */         .getOperator() == 67) {
/*  339 */         ASTree target = ((Expr)expr).head();
/*  340 */         if (target instanceof Keyword) {
/*  341 */           int token = ((Keyword)target).get();
/*  342 */           return (token != 339 && token != 336);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  347 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   protected abstract void insertDefaultSuperCall() throws CompileError;
/*      */   
/*      */   public void atStmnt(Stmnt st) throws CompileError {
/*  354 */     if (st == null) {
/*      */       return;
/*      */     }
/*  357 */     int op = st.getOperator();
/*  358 */     if (op == 69) {
/*  359 */       ASTree expr = st.getLeft();
/*  360 */       doTypeCheck(expr);
/*  361 */       if (expr instanceof AssignExpr) {
/*  362 */         atAssignExpr((AssignExpr)expr, false);
/*  363 */       } else if (isPlusPlusExpr(expr)) {
/*  364 */         Expr e = (Expr)expr;
/*  365 */         atPlusPlus(e.getOperator(), e.oprand1(), e, false);
/*      */       } else {
/*      */         
/*  368 */         expr.accept(this);
/*  369 */         if (is2word(this.exprType, this.arrayDim)) {
/*  370 */           this.bytecode.addOpcode(88);
/*  371 */         } else if (this.exprType != 344) {
/*  372 */           this.bytecode.addOpcode(87);
/*      */         } 
/*      */       } 
/*  375 */     } else if (op == 68 || op == 66) {
/*  376 */       Stmnt stmnt = st;
/*  377 */       while (stmnt != null) {
/*  378 */         ASTree h = stmnt.head();
/*  379 */         ASTList aSTList = stmnt.tail();
/*  380 */         if (h != null) {
/*  381 */           h.accept(this);
/*      */         }
/*      */       } 
/*  384 */     } else if (op == 320) {
/*  385 */       atIfStmnt(st);
/*  386 */     } else if (op == 346 || op == 311) {
/*  387 */       atWhileStmnt(st, (op == 346));
/*  388 */     } else if (op == 318) {
/*  389 */       atForStmnt(st);
/*  390 */     } else if (op == 302 || op == 309) {
/*  391 */       atBreakStmnt(st, (op == 302));
/*  392 */     } else if (op == 333) {
/*  393 */       atReturnStmnt(st);
/*  394 */     } else if (op == 340) {
/*  395 */       atThrowStmnt(st);
/*  396 */     } else if (op == 343) {
/*  397 */       atTryStmnt(st);
/*  398 */     } else if (op == 337) {
/*  399 */       atSwitchStmnt(st);
/*  400 */     } else if (op == 338) {
/*  401 */       atSyncStmnt(st);
/*      */     } else {
/*      */       
/*  404 */       this.hasReturned = false;
/*  405 */       throw new CompileError("sorry, not supported statement: TokenId " + op);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void atIfStmnt(Stmnt st) throws CompileError {
/*  411 */     ASTree expr = st.head();
/*  412 */     Stmnt thenp = (Stmnt)st.tail().head();
/*  413 */     Stmnt elsep = (Stmnt)st.tail().tail().head();
/*  414 */     if (compileBooleanExpr(false, expr)) {
/*  415 */       this.hasReturned = false;
/*  416 */       if (elsep != null) {
/*  417 */         elsep.accept(this);
/*      */       }
/*      */       
/*      */       return;
/*      */     } 
/*  422 */     int pc = this.bytecode.currentPc();
/*  423 */     int pc2 = 0;
/*  424 */     this.bytecode.addIndex(0);
/*      */     
/*  426 */     this.hasReturned = false;
/*  427 */     if (thenp != null) {
/*  428 */       thenp.accept(this);
/*      */     }
/*  430 */     boolean thenHasReturned = this.hasReturned;
/*  431 */     this.hasReturned = false;
/*      */     
/*  433 */     if (elsep != null && !thenHasReturned) {
/*  434 */       this.bytecode.addOpcode(167);
/*  435 */       pc2 = this.bytecode.currentPc();
/*  436 */       this.bytecode.addIndex(0);
/*      */     } 
/*      */     
/*  439 */     this.bytecode.write16bit(pc, this.bytecode.currentPc() - pc + 1);
/*  440 */     if (elsep != null) {
/*  441 */       elsep.accept(this);
/*  442 */       if (!thenHasReturned) {
/*  443 */         this.bytecode.write16bit(pc2, this.bytecode.currentPc() - pc2 + 1);
/*      */       }
/*  445 */       this.hasReturned = (thenHasReturned && this.hasReturned);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void atWhileStmnt(Stmnt st, boolean notDo) throws CompileError {
/*  450 */     List<Integer> prevBreakList = this.breakList;
/*  451 */     List<Integer> prevContList = this.continueList;
/*  452 */     this.breakList = new ArrayList<>();
/*  453 */     this.continueList = new ArrayList<>();
/*      */     
/*  455 */     ASTree expr = st.head();
/*  456 */     Stmnt body = (Stmnt)st.tail();
/*      */     
/*  458 */     int pc = 0;
/*  459 */     if (notDo) {
/*  460 */       this.bytecode.addOpcode(167);
/*  461 */       pc = this.bytecode.currentPc();
/*  462 */       this.bytecode.addIndex(0);
/*      */     } 
/*      */     
/*  465 */     int pc2 = this.bytecode.currentPc();
/*  466 */     if (body != null) {
/*  467 */       body.accept(this);
/*      */     }
/*  469 */     int pc3 = this.bytecode.currentPc();
/*  470 */     if (notDo) {
/*  471 */       this.bytecode.write16bit(pc, pc3 - pc + 1);
/*      */     }
/*  473 */     boolean alwaysBranch = compileBooleanExpr(true, expr);
/*  474 */     if (alwaysBranch) {
/*  475 */       this.bytecode.addOpcode(167);
/*  476 */       alwaysBranch = (this.breakList.size() == 0);
/*      */     } 
/*      */     
/*  479 */     this.bytecode.addIndex(pc2 - this.bytecode.currentPc() + 1);
/*  480 */     patchGoto(this.breakList, this.bytecode.currentPc());
/*  481 */     patchGoto(this.continueList, pc3);
/*  482 */     this.continueList = prevContList;
/*  483 */     this.breakList = prevBreakList;
/*  484 */     this.hasReturned = alwaysBranch;
/*      */   }
/*      */   
/*      */   protected void patchGoto(List<Integer> list, int targetPc) {
/*  488 */     for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext(); ) { int pc = ((Integer)iterator.next()).intValue();
/*  489 */       this.bytecode.write16bit(pc, targetPc - pc + 1); }
/*      */   
/*      */   }
/*      */   private void atForStmnt(Stmnt st) throws CompileError {
/*  493 */     List<Integer> prevBreakList = this.breakList;
/*  494 */     List<Integer> prevContList = this.continueList;
/*  495 */     this.breakList = new ArrayList<>();
/*  496 */     this.continueList = new ArrayList<>();
/*      */     
/*  498 */     Stmnt init = (Stmnt)st.head();
/*  499 */     ASTList p = st.tail();
/*  500 */     ASTree expr = p.head();
/*  501 */     p = p.tail();
/*  502 */     Stmnt update = (Stmnt)p.head();
/*  503 */     Stmnt body = (Stmnt)p.tail();
/*      */     
/*  505 */     if (init != null) {
/*  506 */       init.accept(this);
/*      */     }
/*  508 */     int pc = this.bytecode.currentPc();
/*  509 */     int pc2 = 0;
/*  510 */     if (expr != null) {
/*  511 */       if (compileBooleanExpr(false, expr)) {
/*      */         
/*  513 */         this.continueList = prevContList;
/*  514 */         this.breakList = prevBreakList;
/*  515 */         this.hasReturned = false;
/*      */         
/*      */         return;
/*      */       } 
/*  519 */       pc2 = this.bytecode.currentPc();
/*  520 */       this.bytecode.addIndex(0);
/*      */     } 
/*      */     
/*  523 */     if (body != null) {
/*  524 */       body.accept(this);
/*      */     }
/*  526 */     int pc3 = this.bytecode.currentPc();
/*  527 */     if (update != null) {
/*  528 */       update.accept(this);
/*      */     }
/*  530 */     this.bytecode.addOpcode(167);
/*  531 */     this.bytecode.addIndex(pc - this.bytecode.currentPc() + 1);
/*      */     
/*  533 */     int pc4 = this.bytecode.currentPc();
/*  534 */     if (expr != null) {
/*  535 */       this.bytecode.write16bit(pc2, pc4 - pc2 + 1);
/*      */     }
/*  537 */     patchGoto(this.breakList, pc4);
/*  538 */     patchGoto(this.continueList, pc3);
/*  539 */     this.continueList = prevContList;
/*  540 */     this.breakList = prevBreakList;
/*  541 */     this.hasReturned = false;
/*      */   }
/*      */   
/*      */   private void atSwitchStmnt(Stmnt st) throws CompileError {
/*  545 */     boolean isString = false;
/*  546 */     if (this.typeChecker != null) {
/*  547 */       doTypeCheck(st.head());
/*      */ 
/*      */       
/*  550 */       isString = (this.typeChecker.exprType == 307 && this.typeChecker.arrayDim == 0 && "java/lang/String".equals(this.typeChecker.className));
/*      */     } 
/*      */     
/*  553 */     compileExpr(st.head());
/*  554 */     int tmpVar = -1;
/*  555 */     if (isString) {
/*  556 */       tmpVar = getMaxLocals();
/*  557 */       incMaxLocals(1);
/*  558 */       this.bytecode.addAstore(tmpVar);
/*  559 */       this.bytecode.addAload(tmpVar);
/*  560 */       this.bytecode.addInvokevirtual("java/lang/String", "hashCode", "()I");
/*      */     } 
/*      */     
/*  563 */     List<Integer> prevBreakList = this.breakList;
/*  564 */     this.breakList = new ArrayList<>();
/*  565 */     int opcodePc = this.bytecode.currentPc();
/*  566 */     this.bytecode.addOpcode(171);
/*  567 */     int npads = 3 - (opcodePc & 0x3);
/*  568 */     while (npads-- > 0) {
/*  569 */       this.bytecode.add(0);
/*      */     }
/*  571 */     Stmnt body = (Stmnt)st.tail();
/*  572 */     int npairs = 0;
/*  573 */     for (Stmnt stmnt1 = body; stmnt1 != null; aSTList = stmnt1.tail()) {
/*  574 */       ASTList aSTList; if (((Stmnt)stmnt1.head()).getOperator() == 304) {
/*  575 */         npairs++;
/*      */       }
/*      */     } 
/*  578 */     int opcodePc2 = this.bytecode.currentPc();
/*  579 */     this.bytecode.addGap(4);
/*  580 */     this.bytecode.add32bit(npairs);
/*  581 */     this.bytecode.addGap(npairs * 8);
/*      */     
/*  583 */     long[] pairs = new long[npairs];
/*  584 */     ArrayList<Integer> gotoDefaults = new ArrayList<>();
/*  585 */     int ipairs = 0;
/*  586 */     int defaultPc = -1;
/*  587 */     for (Stmnt stmnt2 = body; stmnt2 != null; aSTList = stmnt2.tail()) {
/*  588 */       ASTList aSTList; Stmnt label = (Stmnt)stmnt2.head();
/*  589 */       int op = label.getOperator();
/*  590 */       if (op == 310) {
/*  591 */         defaultPc = this.bytecode.currentPc();
/*  592 */       } else if (op != 304) {
/*  593 */         fatal();
/*      */       } else {
/*  595 */         long caseLabel; int curPos = this.bytecode.currentPc();
/*      */         
/*  597 */         if (isString) {
/*      */           
/*  599 */           caseLabel = computeStringLabel(label.head(), tmpVar, gotoDefaults);
/*      */         } else {
/*      */           
/*  602 */           caseLabel = computeLabel(label.head());
/*      */         } 
/*  604 */         pairs[ipairs++] = (caseLabel << 32L) + ((curPos - opcodePc) & 0xFFFFFFFFFFFFFFFFL);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  609 */       this.hasReturned = false;
/*  610 */       ((Stmnt)label.tail()).accept(this);
/*      */     } 
/*      */     
/*  613 */     Arrays.sort(pairs);
/*  614 */     int pc = opcodePc2 + 8;
/*  615 */     for (int i = 0; i < npairs; i++) {
/*  616 */       this.bytecode.write32bit(pc, (int)(pairs[i] >>> 32L));
/*  617 */       this.bytecode.write32bit(pc + 4, (int)pairs[i]);
/*  618 */       pc += 8;
/*      */     } 
/*      */     
/*  621 */     if (defaultPc < 0 || this.breakList.size() > 0) {
/*  622 */       this.hasReturned = false;
/*      */     }
/*  624 */     int endPc = this.bytecode.currentPc();
/*  625 */     if (defaultPc < 0) {
/*  626 */       defaultPc = endPc;
/*      */     }
/*  628 */     this.bytecode.write32bit(opcodePc2, defaultPc - opcodePc);
/*  629 */     for (Iterator<Integer> iterator = gotoDefaults.iterator(); iterator.hasNext(); ) { int addr = ((Integer)iterator.next()).intValue();
/*  630 */       this.bytecode.write16bit(addr, defaultPc - addr + 1); }
/*      */     
/*  632 */     patchGoto(this.breakList, endPc);
/*  633 */     this.breakList = prevBreakList;
/*      */   }
/*      */   
/*      */   private int computeLabel(ASTree expr) throws CompileError {
/*  637 */     doTypeCheck(expr);
/*  638 */     expr = TypeChecker.stripPlusExpr(expr);
/*  639 */     if (expr instanceof IntConst)
/*  640 */       return (int)((IntConst)expr).get(); 
/*  641 */     throw new CompileError("bad case label");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private int computeStringLabel(ASTree expr, int tmpVar, List<Integer> gotoDefaults) throws CompileError {
/*  647 */     doTypeCheck(expr);
/*  648 */     expr = TypeChecker.stripPlusExpr(expr);
/*  649 */     if (expr instanceof StringL) {
/*  650 */       String label = ((StringL)expr).get();
/*  651 */       this.bytecode.addAload(tmpVar);
/*  652 */       this.bytecode.addLdc(label);
/*  653 */       this.bytecode.addInvokevirtual("java/lang/String", "equals", "(Ljava/lang/Object;)Z");
/*      */       
/*  655 */       this.bytecode.addOpcode(153);
/*  656 */       Integer pc = Integer.valueOf(this.bytecode.currentPc());
/*  657 */       this.bytecode.addIndex(0);
/*  658 */       gotoDefaults.add(pc);
/*  659 */       return label.hashCode();
/*      */     } 
/*  661 */     throw new CompileError("bad case label");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void atBreakStmnt(Stmnt st, boolean notCont) throws CompileError {
/*  667 */     if (st.head() != null) {
/*  668 */       throw new CompileError("sorry, not support labeled break or continue");
/*      */     }
/*      */     
/*  671 */     this.bytecode.addOpcode(167);
/*  672 */     Integer pc = Integer.valueOf(this.bytecode.currentPc());
/*  673 */     this.bytecode.addIndex(0);
/*  674 */     if (notCont) {
/*  675 */       this.breakList.add(pc);
/*      */     } else {
/*  677 */       this.continueList.add(pc);
/*      */     } 
/*      */   }
/*      */   protected void atReturnStmnt(Stmnt st) throws CompileError {
/*  681 */     atReturnStmnt2(st.getLeft());
/*      */   }
/*      */   
/*      */   protected final void atReturnStmnt2(ASTree result) throws CompileError {
/*      */     int op;
/*  686 */     if (result == null) {
/*  687 */       op = 177;
/*      */     } else {
/*  689 */       compileExpr(result);
/*  690 */       if (this.arrayDim > 0) {
/*  691 */         op = 176;
/*      */       } else {
/*  693 */         int type = this.exprType;
/*  694 */         if (type == 312) {
/*  695 */           op = 175;
/*  696 */         } else if (type == 317) {
/*  697 */           op = 174;
/*  698 */         } else if (type == 326) {
/*  699 */           op = 173;
/*  700 */         } else if (isRefType(type)) {
/*  701 */           op = 176;
/*      */         } else {
/*  703 */           op = 172;
/*      */         } 
/*      */       } 
/*      */     } 
/*  707 */     for (ReturnHook har = this.returnHooks; har != null; har = har.next) {
/*  708 */       if (har.doit(this.bytecode, op)) {
/*  709 */         this.hasReturned = true;
/*      */         return;
/*      */       } 
/*      */     } 
/*  713 */     this.bytecode.addOpcode(op);
/*  714 */     this.hasReturned = true;
/*      */   }
/*      */   
/*      */   private void atThrowStmnt(Stmnt st) throws CompileError {
/*  718 */     ASTree e = st.getLeft();
/*  719 */     compileExpr(e);
/*  720 */     if (this.exprType != 307 || this.arrayDim > 0) {
/*  721 */       throw new CompileError("bad throw statement");
/*      */     }
/*  723 */     this.bytecode.addOpcode(191);
/*  724 */     this.hasReturned = true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void atTryStmnt(Stmnt st) throws CompileError {
/*  730 */     this.hasReturned = false;
/*      */   }
/*      */   
/*      */   private void atSyncStmnt(Stmnt st) throws CompileError {
/*  734 */     int nbreaks = getListSize(this.breakList);
/*  735 */     int ncontinues = getListSize(this.continueList);
/*      */     
/*  737 */     compileExpr(st.head());
/*  738 */     if (this.exprType != 307 && this.arrayDim == 0) {
/*  739 */       throw new CompileError("bad type expr for synchronized block");
/*      */     }
/*  741 */     Bytecode bc = this.bytecode;
/*  742 */     final int var = bc.getMaxLocals();
/*  743 */     bc.incMaxLocals(1);
/*  744 */     bc.addOpcode(89);
/*  745 */     bc.addAstore(var);
/*  746 */     bc.addOpcode(194);
/*      */     
/*  748 */     ReturnHook rh = new ReturnHook(this)
/*      */       {
/*      */         protected boolean doit(Bytecode b, int opcode) {
/*  751 */           b.addAload(var);
/*  752 */           b.addOpcode(195);
/*  753 */           return false;
/*      */         }
/*      */       };
/*      */     
/*  757 */     int pc = bc.currentPc();
/*  758 */     Stmnt body = (Stmnt)st.tail();
/*  759 */     if (body != null) {
/*  760 */       body.accept(this);
/*      */     }
/*  762 */     int pc2 = bc.currentPc();
/*  763 */     int pc3 = 0;
/*  764 */     if (!this.hasReturned) {
/*  765 */       rh.doit(bc, 0);
/*  766 */       bc.addOpcode(167);
/*  767 */       pc3 = bc.currentPc();
/*  768 */       bc.addIndex(0);
/*      */     } 
/*      */     
/*  771 */     if (pc < pc2) {
/*  772 */       int pc4 = bc.currentPc();
/*  773 */       rh.doit(bc, 0);
/*  774 */       bc.addOpcode(191);
/*  775 */       bc.addExceptionHandler(pc, pc2, pc4, 0);
/*      */     } 
/*      */     
/*  778 */     if (!this.hasReturned) {
/*  779 */       bc.write16bit(pc3, bc.currentPc() - pc3 + 1);
/*      */     }
/*  781 */     rh.remove(this);
/*      */     
/*  783 */     if (getListSize(this.breakList) != nbreaks || 
/*  784 */       getListSize(this.continueList) != ncontinues) {
/*  785 */       throw new CompileError("sorry, cannot break/continue in synchronized block");
/*      */     }
/*      */   }
/*      */   
/*      */   private static int getListSize(List<Integer> list) {
/*  790 */     return (list == null) ? 0 : list.size();
/*      */   }
/*      */   
/*      */   private static boolean isPlusPlusExpr(ASTree expr) {
/*  794 */     if (expr instanceof Expr) {
/*  795 */       int op = ((Expr)expr).getOperator();
/*  796 */       return (op == 362 || op == 363);
/*      */     } 
/*      */     
/*  799 */     return false;
/*      */   }
/*      */   
/*      */   public void atDeclarator(Declarator d) throws CompileError {
/*      */     int size;
/*  804 */     d.setLocalVar(getMaxLocals());
/*  805 */     d.setClassName(resolveClassName(d.getClassName()));
/*      */ 
/*      */     
/*  808 */     if (is2word(d.getType(), d.getArrayDim())) {
/*  809 */       size = 2;
/*      */     } else {
/*  811 */       size = 1;
/*      */     } 
/*  813 */     incMaxLocals(size);
/*      */ 
/*      */ 
/*      */     
/*  817 */     ASTree init = d.getInitializer();
/*  818 */     if (init != null) {
/*  819 */       doTypeCheck(init);
/*  820 */       atVariableAssign(null, 61, null, d, init, false);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public abstract void atNewExpr(NewExpr paramNewExpr) throws CompileError;
/*      */ 
/*      */   
/*      */   public abstract void atArrayInit(ArrayInit paramArrayInit) throws CompileError;
/*      */ 
/*      */   
/*      */   public void atAssignExpr(AssignExpr expr) throws CompileError {
/*  832 */     atAssignExpr(expr, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void atAssignExpr(AssignExpr expr, boolean doDup) throws CompileError {
/*  839 */     int op = expr.getOperator();
/*  840 */     ASTree left = expr.oprand1();
/*  841 */     ASTree right = expr.oprand2();
/*  842 */     if (left instanceof Variable) {
/*  843 */       atVariableAssign((Expr)expr, op, (Variable)left, ((Variable)left)
/*  844 */           .getDeclarator(), right, doDup);
/*      */     } else {
/*      */       
/*  847 */       if (left instanceof Expr) {
/*  848 */         Expr e = (Expr)left;
/*  849 */         if (e.getOperator() == 65) {
/*  850 */           atArrayAssign((Expr)expr, op, (Expr)left, right, doDup);
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*  855 */       atFieldAssign((Expr)expr, op, left, right, doDup);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected static void badAssign(Expr expr) throws CompileError {
/*      */     String msg;
/*  861 */     if (expr == null) {
/*  862 */       msg = "incompatible type for assignment";
/*      */     } else {
/*  864 */       msg = "incompatible type for " + expr.getName();
/*      */     } 
/*  866 */     throw new CompileError(msg);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void atVariableAssign(Expr expr, int op, Variable var, Declarator d, ASTree right, boolean doDup) throws CompileError {
/*  877 */     int varType = d.getType();
/*  878 */     int varArray = d.getArrayDim();
/*  879 */     String varClass = d.getClassName();
/*  880 */     int varNo = getLocalVar(d);
/*      */     
/*  882 */     if (op != 61) {
/*  883 */       atVariable(var);
/*      */     }
/*      */     
/*  886 */     if (expr == null && right instanceof ArrayInit) {
/*  887 */       atArrayVariableAssign((ArrayInit)right, varType, varArray, varClass);
/*      */     } else {
/*  889 */       atAssignCore(expr, op, right, varType, varArray, varClass);
/*      */     } 
/*  891 */     if (doDup)
/*  892 */       if (is2word(varType, varArray)) {
/*  893 */         this.bytecode.addOpcode(92);
/*      */       } else {
/*  895 */         this.bytecode.addOpcode(89);
/*      */       }  
/*  897 */     if (varArray > 0) {
/*  898 */       this.bytecode.addAstore(varNo);
/*  899 */     } else if (varType == 312) {
/*  900 */       this.bytecode.addDstore(varNo);
/*  901 */     } else if (varType == 317) {
/*  902 */       this.bytecode.addFstore(varNo);
/*  903 */     } else if (varType == 326) {
/*  904 */       this.bytecode.addLstore(varNo);
/*  905 */     } else if (isRefType(varType)) {
/*  906 */       this.bytecode.addAstore(varNo);
/*      */     } else {
/*  908 */       this.bytecode.addIstore(varNo);
/*      */     } 
/*  910 */     this.exprType = varType;
/*  911 */     this.arrayDim = varArray;
/*  912 */     this.className = varClass;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract void atArrayVariableAssign(ArrayInit paramArrayInit, int paramInt1, int paramInt2, String paramString) throws CompileError;
/*      */ 
/*      */   
/*      */   private void atArrayAssign(Expr expr, int op, Expr array, ASTree right, boolean doDup) throws CompileError {
/*  921 */     arrayAccess(array.oprand1(), array.oprand2());
/*      */     
/*  923 */     if (op != 61) {
/*  924 */       this.bytecode.addOpcode(92);
/*  925 */       this.bytecode.addOpcode(getArrayReadOp(this.exprType, this.arrayDim));
/*      */     } 
/*      */     
/*  928 */     int aType = this.exprType;
/*  929 */     int aDim = this.arrayDim;
/*  930 */     String cname = this.className;
/*      */     
/*  932 */     atAssignCore(expr, op, right, aType, aDim, cname);
/*      */     
/*  934 */     if (doDup)
/*  935 */       if (is2word(aType, aDim)) {
/*  936 */         this.bytecode.addOpcode(94);
/*      */       } else {
/*  938 */         this.bytecode.addOpcode(91);
/*      */       }  
/*  940 */     this.bytecode.addOpcode(getArrayWriteOp(aType, aDim));
/*  941 */     this.exprType = aType;
/*  942 */     this.arrayDim = aDim;
/*  943 */     this.className = cname;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract void atFieldAssign(Expr paramExpr, int paramInt, ASTree paramASTree1, ASTree paramASTree2, boolean paramBoolean) throws CompileError;
/*      */ 
/*      */ 
/*      */   
/*      */   protected void atAssignCore(Expr expr, int op, ASTree right, int type, int dim, String cname) throws CompileError {
/*  953 */     if (op == 354 && dim == 0 && type == 307) {
/*  954 */       atStringPlusEq(expr, type, dim, cname, right);
/*      */     } else {
/*  956 */       right.accept(this);
/*  957 */       if (invalidDim(this.exprType, this.arrayDim, this.className, type, dim, cname, false) || (op != 61 && dim > 0))
/*      */       {
/*  959 */         badAssign(expr);
/*      */       }
/*  961 */       if (op != 61) {
/*  962 */         int token = assignOps[op - 351];
/*  963 */         int k = lookupBinOp(token);
/*  964 */         if (k < 0) {
/*  965 */           fatal();
/*      */         }
/*  967 */         atArithBinExpr(expr, token, k, type);
/*      */       } 
/*      */     } 
/*      */     
/*  971 */     if (op != 61 || (dim == 0 && !isRefType(type))) {
/*  972 */       atNumCastExpr(this.exprType, type);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void atStringPlusEq(Expr expr, int type, int dim, String cname, ASTree right) throws CompileError {
/*  981 */     if (!"java/lang/String".equals(cname)) {
/*  982 */       badAssign(expr);
/*      */     }
/*  984 */     convToString(type, dim);
/*  985 */     right.accept(this);
/*  986 */     convToString(this.exprType, this.arrayDim);
/*  987 */     this.bytecode.addInvokevirtual("java.lang.String", "concat", "(Ljava/lang/String;)Ljava/lang/String;");
/*      */     
/*  989 */     this.exprType = 307;
/*  990 */     this.arrayDim = 0;
/*  991 */     this.className = "java/lang/String";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean invalidDim(int srcType, int srcDim, String srcClass, int destType, int destDim, String destClass, boolean isCast) {
/*  998 */     if (srcDim != destDim) {
/*  999 */       if (srcType == 412)
/* 1000 */         return false; 
/* 1001 */       if (destDim == 0 && destType == 307 && "java/lang/Object"
/* 1002 */         .equals(destClass))
/* 1003 */         return false; 
/* 1004 */       if (isCast && srcDim == 0 && srcType == 307 && "java/lang/Object"
/* 1005 */         .equals(srcClass)) {
/* 1006 */         return false;
/*      */       }
/* 1008 */       return true;
/*      */     } 
/* 1010 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void atCondExpr(CondExpr expr) throws CompileError {
/* 1015 */     if (booleanExpr(false, expr.condExpr())) {
/* 1016 */       expr.elseExpr().accept(this);
/*      */     } else {
/* 1018 */       int pc = this.bytecode.currentPc();
/* 1019 */       this.bytecode.addIndex(0);
/* 1020 */       expr.thenExpr().accept(this);
/* 1021 */       int dim1 = this.arrayDim;
/* 1022 */       this.bytecode.addOpcode(167);
/* 1023 */       int pc2 = this.bytecode.currentPc();
/* 1024 */       this.bytecode.addIndex(0);
/* 1025 */       this.bytecode.write16bit(pc, this.bytecode.currentPc() - pc + 1);
/* 1026 */       expr.elseExpr().accept(this);
/* 1027 */       if (dim1 != this.arrayDim) {
/* 1028 */         throw new CompileError("type mismatch in ?:");
/*      */       }
/* 1030 */       this.bytecode.write16bit(pc2, this.bytecode.currentPc() - pc2 + 1);
/*      */     } 
/*      */   }
/*      */   
/* 1034 */   static final int[] binOp = new int[] { 43, 99, 98, 97, 96, 45, 103, 102, 101, 100, 42, 107, 106, 105, 104, 47, 111, 110, 109, 108, 37, 115, 114, 113, 112, 124, 0, 0, 129, 128, 94, 0, 0, 131, 130, 38, 0, 0, 127, 126, 364, 0, 0, 121, 120, 366, 0, 0, 123, 122, 370, 0, 0, 125, 124 };
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
/*      */   static int lookupBinOp(int token) {
/* 1048 */     int[] code = binOp;
/* 1049 */     int s = code.length;
/* 1050 */     for (int k = 0; k < s; k += 5) {
/* 1051 */       if (code[k] == token)
/* 1052 */         return k; 
/*      */     } 
/* 1054 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   public void atBinExpr(BinExpr expr) throws CompileError {
/* 1059 */     int token = expr.getOperator();
/*      */ 
/*      */ 
/*      */     
/* 1063 */     int k = lookupBinOp(token);
/* 1064 */     if (k >= 0) {
/* 1065 */       expr.oprand1().accept(this);
/* 1066 */       ASTree right = expr.oprand2();
/* 1067 */       if (right == null) {
/*      */         return;
/*      */       }
/* 1070 */       int type1 = this.exprType;
/* 1071 */       int dim1 = this.arrayDim;
/* 1072 */       String cname1 = this.className;
/* 1073 */       right.accept(this);
/* 1074 */       if (dim1 != this.arrayDim) {
/* 1075 */         throw new CompileError("incompatible array types");
/*      */       }
/* 1077 */       if (token == 43 && dim1 == 0 && (type1 == 307 || this.exprType == 307)) {
/*      */         
/* 1079 */         atStringConcatExpr((Expr)expr, type1, dim1, cname1);
/*      */       } else {
/* 1081 */         atArithBinExpr((Expr)expr, token, k, type1);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 1086 */       if (!booleanExpr(true, (ASTree)expr)) {
/* 1087 */         this.bytecode.addIndex(7);
/* 1088 */         this.bytecode.addIconst(0);
/* 1089 */         this.bytecode.addOpcode(167);
/* 1090 */         this.bytecode.addIndex(4);
/*      */       } 
/*      */       
/* 1093 */       this.bytecode.addIconst(1);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void atArithBinExpr(Expr expr, int token, int index, int type1) throws CompileError {
/* 1104 */     if (this.arrayDim != 0) {
/* 1105 */       badTypes(expr);
/*      */     }
/* 1107 */     int type2 = this.exprType;
/* 1108 */     if (token == 364 || token == 366 || token == 370)
/* 1109 */     { if (type2 == 324 || type2 == 334 || type2 == 306 || type2 == 303) {
/*      */         
/* 1111 */         this.exprType = type1;
/*      */       } else {
/* 1113 */         badTypes(expr);
/*      */       }  }
/* 1115 */     else { convertOprandTypes(type1, type2, expr); }
/*      */     
/* 1117 */     int p = typePrecedence(this.exprType);
/* 1118 */     if (p >= 0) {
/* 1119 */       int op = binOp[index + p + 1];
/* 1120 */       if (op != 0) {
/* 1121 */         if (p == 3 && this.exprType != 301) {
/* 1122 */           this.exprType = 324;
/*      */         }
/* 1124 */         this.bytecode.addOpcode(op);
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/* 1129 */     badTypes(expr);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void atStringConcatExpr(Expr expr, int type1, int dim1, String cname1) throws CompileError {
/* 1135 */     int type2 = this.exprType;
/* 1136 */     int dim2 = this.arrayDim;
/* 1137 */     boolean type2Is2 = is2word(type2, dim2);
/*      */     
/* 1139 */     boolean type2IsString = (type2 == 307 && "java/lang/String".equals(this.className));
/*      */     
/* 1141 */     if (type2Is2) {
/* 1142 */       convToString(type2, dim2);
/*      */     }
/* 1144 */     if (is2word(type1, dim1)) {
/* 1145 */       this.bytecode.addOpcode(91);
/* 1146 */       this.bytecode.addOpcode(87);
/*      */     } else {
/*      */       
/* 1149 */       this.bytecode.addOpcode(95);
/*      */     } 
/*      */     
/* 1152 */     convToString(type1, dim1);
/* 1153 */     this.bytecode.addOpcode(95);
/*      */     
/* 1155 */     if (!type2Is2 && !type2IsString) {
/* 1156 */       convToString(type2, dim2);
/*      */     }
/* 1158 */     this.bytecode.addInvokevirtual("java.lang.String", "concat", "(Ljava/lang/String;)Ljava/lang/String;");
/*      */     
/* 1160 */     this.exprType = 307;
/* 1161 */     this.arrayDim = 0;
/* 1162 */     this.className = "java/lang/String";
/*      */   }
/*      */   
/*      */   private void convToString(int type, int dim) throws CompileError {
/* 1166 */     String method = "valueOf";
/*      */     
/* 1168 */     if (isRefType(type) || dim > 0) {
/* 1169 */       this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;");
/*      */     }
/* 1171 */     else if (type == 312) {
/* 1172 */       this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(D)Ljava/lang/String;");
/*      */     }
/* 1174 */     else if (type == 317) {
/* 1175 */       this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(F)Ljava/lang/String;");
/*      */     }
/* 1177 */     else if (type == 326) {
/* 1178 */       this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(J)Ljava/lang/String;");
/*      */     }
/* 1180 */     else if (type == 301) {
/* 1181 */       this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(Z)Ljava/lang/String;");
/*      */     }
/* 1183 */     else if (type == 306) {
/* 1184 */       this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(C)Ljava/lang/String;");
/*      */     } else {
/* 1186 */       if (type == 344) {
/* 1187 */         throw new CompileError("void type expression");
/*      */       }
/* 1189 */       this.bytecode.addInvokestatic("java.lang.String", "valueOf", "(I)Ljava/lang/String;");
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
/*      */   private boolean booleanExpr(boolean branchIf, ASTree expr) throws CompileError {
/* 1203 */     int op = getCompOperator(expr);
/* 1204 */     if (op == 358) {
/* 1205 */       BinExpr bexpr = (BinExpr)expr;
/* 1206 */       int type1 = compileOprands(bexpr);
/*      */ 
/*      */       
/* 1209 */       compareExpr(branchIf, bexpr.getOperator(), type1, bexpr);
/*      */     } else {
/* 1211 */       if (op == 33)
/* 1212 */         return booleanExpr(!branchIf, ((Expr)expr).oprand1());  boolean isAndAnd;
/* 1213 */       if ((isAndAnd = (op == 369)) || op == 368) {
/* 1214 */         BinExpr bexpr = (BinExpr)expr;
/* 1215 */         if (booleanExpr(!isAndAnd, bexpr.oprand1())) {
/* 1216 */           this.exprType = 301;
/* 1217 */           this.arrayDim = 0;
/* 1218 */           return true;
/*      */         } 
/* 1220 */         int pc = this.bytecode.currentPc();
/* 1221 */         this.bytecode.addIndex(0);
/* 1222 */         if (booleanExpr(isAndAnd, bexpr.oprand2())) {
/* 1223 */           this.bytecode.addOpcode(167);
/*      */         }
/* 1225 */         this.bytecode.write16bit(pc, this.bytecode.currentPc() - pc + 3);
/* 1226 */         if (branchIf != isAndAnd) {
/* 1227 */           this.bytecode.addIndex(6);
/* 1228 */           this.bytecode.addOpcode(167);
/*      */         } 
/*      */       } else {
/* 1231 */         if (isAlwaysBranch(expr, branchIf)) {
/*      */           
/* 1233 */           this.exprType = 301;
/* 1234 */           this.arrayDim = 0;
/* 1235 */           return true;
/*      */         } 
/*      */         
/* 1238 */         expr.accept(this);
/* 1239 */         if (this.exprType != 301 || this.arrayDim != 0) {
/* 1240 */           throw new CompileError("boolean expr is required");
/*      */         }
/* 1242 */         this.bytecode.addOpcode(branchIf ? 154 : 153);
/*      */       } 
/*      */     } 
/* 1245 */     this.exprType = 301;
/* 1246 */     this.arrayDim = 0;
/* 1247 */     return false;
/*      */   }
/*      */   
/*      */   private static boolean isAlwaysBranch(ASTree expr, boolean branchIf) {
/* 1251 */     if (expr instanceof Keyword) {
/* 1252 */       int t = ((Keyword)expr).get();
/* 1253 */       return branchIf ? ((t == 410)) : ((t == 411));
/*      */     } 
/*      */     
/* 1256 */     return false;
/*      */   }
/*      */   
/*      */   static int getCompOperator(ASTree expr) throws CompileError {
/* 1260 */     if (expr instanceof Expr) {
/* 1261 */       Expr bexpr = (Expr)expr;
/* 1262 */       int token = bexpr.getOperator();
/* 1263 */       if (token == 33)
/* 1264 */         return 33; 
/* 1265 */       if (bexpr instanceof BinExpr && token != 368 && token != 369 && token != 38 && token != 124)
/*      */       {
/*      */         
/* 1268 */         return 358;
/*      */       }
/* 1270 */       return token;
/*      */     } 
/*      */     
/* 1273 */     return 32;
/*      */   }
/*      */   
/*      */   private int compileOprands(BinExpr expr) throws CompileError {
/* 1277 */     expr.oprand1().accept(this);
/* 1278 */     int type1 = this.exprType;
/* 1279 */     int dim1 = this.arrayDim;
/* 1280 */     expr.oprand2().accept(this);
/* 1281 */     if (dim1 != this.arrayDim) {
/* 1282 */       if (type1 != 412 && this.exprType != 412)
/* 1283 */         throw new CompileError("incompatible array types"); 
/* 1284 */       if (this.exprType == 412)
/* 1285 */         this.arrayDim = dim1; 
/*      */     } 
/* 1287 */     if (type1 == 412)
/* 1288 */       return this.exprType; 
/* 1289 */     return type1;
/*      */   }
/*      */   
/* 1292 */   private static final int[] ifOp = new int[] { 358, 159, 160, 350, 160, 159, 357, 164, 163, 359, 162, 161, 60, 161, 162, 62, 163, 164 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1299 */   private static final int[] ifOp2 = new int[] { 358, 153, 154, 350, 154, 153, 357, 158, 157, 359, 156, 155, 60, 155, 156, 62, 157, 158 };
/*      */ 
/*      */   
/*      */   private static final int P_DOUBLE = 0;
/*      */ 
/*      */   
/*      */   private static final int P_FLOAT = 1;
/*      */   
/*      */   private static final int P_LONG = 2;
/*      */   
/*      */   private static final int P_INT = 3;
/*      */   
/*      */   private static final int P_OTHER = -1;
/*      */ 
/*      */   
/*      */   private void compareExpr(boolean branchIf, int token, int type1, BinExpr expr) throws CompileError {
/* 1315 */     if (this.arrayDim == 0) {
/* 1316 */       convertOprandTypes(type1, this.exprType, (Expr)expr);
/*      */     }
/* 1318 */     int p = typePrecedence(this.exprType);
/* 1319 */     if (p == -1 || this.arrayDim > 0) {
/* 1320 */       if (token == 358) {
/* 1321 */         this.bytecode.addOpcode(branchIf ? 165 : 166);
/* 1322 */       } else if (token == 350) {
/* 1323 */         this.bytecode.addOpcode(branchIf ? 166 : 165);
/*      */       } else {
/* 1325 */         badTypes((Expr)expr);
/*      */       } 
/* 1327 */     } else if (p == 3) {
/* 1328 */       int[] op = ifOp;
/* 1329 */       for (int i = 0; i < op.length; i += 3) {
/* 1330 */         if (op[i] == token) {
/* 1331 */           this.bytecode.addOpcode(op[i + (branchIf ? 1 : 2)]);
/*      */           return;
/*      */         } 
/*      */       } 
/* 1335 */       badTypes((Expr)expr);
/*      */     } else {
/*      */       
/* 1338 */       if (p == 0) {
/* 1339 */         if (token == 60 || token == 357)
/* 1340 */         { this.bytecode.addOpcode(152); }
/*      */         else
/* 1342 */         { this.bytecode.addOpcode(151); } 
/* 1343 */       } else if (p == 1) {
/* 1344 */         if (token == 60 || token == 357)
/* 1345 */         { this.bytecode.addOpcode(150); }
/*      */         else
/* 1347 */         { this.bytecode.addOpcode(149); } 
/* 1348 */       } else if (p == 2) {
/* 1349 */         this.bytecode.addOpcode(148);
/*      */       } else {
/* 1351 */         fatal();
/*      */       } 
/* 1353 */       int[] op = ifOp2;
/* 1354 */       for (int i = 0; i < op.length; i += 3) {
/* 1355 */         if (op[i] == token) {
/* 1356 */           this.bytecode.addOpcode(op[i + (branchIf ? 1 : 2)]);
/*      */           return;
/*      */         } 
/*      */       } 
/* 1360 */       badTypes((Expr)expr);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected static void badTypes(Expr expr) throws CompileError {
/* 1365 */     throw new CompileError("invalid types for " + expr.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static boolean isRefType(int type) {
/* 1375 */     return (type == 307 || type == 412);
/*      */   }
/*      */   
/*      */   private static int typePrecedence(int type) {
/* 1379 */     if (type == 312)
/* 1380 */       return 0; 
/* 1381 */     if (type == 317)
/* 1382 */       return 1; 
/* 1383 */     if (type == 326)
/* 1384 */       return 2; 
/* 1385 */     if (isRefType(type))
/* 1386 */       return -1; 
/* 1387 */     if (type == 344) {
/* 1388 */       return -1;
/*      */     }
/* 1390 */     return 3;
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean isP_INT(int type) {
/* 1395 */     return (typePrecedence(type) == 3);
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean rightIsStrong(int type1, int type2) {
/* 1400 */     int type1_p = typePrecedence(type1);
/* 1401 */     int type2_p = typePrecedence(type2);
/* 1402 */     return (type1_p >= 0 && type2_p >= 0 && type1_p > type2_p);
/*      */   }
/*      */   
/* 1405 */   private static final int[] castOp = new int[] { 0, 144, 143, 142, 141, 0, 140, 139, 138, 137, 0, 136, 135, 134, 133, 0 };
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
/*      */   private void convertOprandTypes(int type1, int type2, Expr expr) throws CompileError {
/*      */     boolean rightStrong;
/* 1419 */     int op, result_type, type1_p = typePrecedence(type1);
/* 1420 */     int type2_p = typePrecedence(type2);
/*      */     
/* 1422 */     if (type2_p < 0 && type1_p < 0) {
/*      */       return;
/*      */     }
/* 1425 */     if (type2_p < 0 || type1_p < 0) {
/* 1426 */       badTypes(expr);
/*      */     }
/*      */     
/* 1429 */     if (type1_p <= type2_p) {
/* 1430 */       rightStrong = false;
/* 1431 */       this.exprType = type1;
/* 1432 */       op = castOp[type2_p * 4 + type1_p];
/* 1433 */       result_type = type1_p;
/*      */     } else {
/*      */       
/* 1436 */       rightStrong = true;
/* 1437 */       op = castOp[type1_p * 4 + type2_p];
/* 1438 */       result_type = type2_p;
/*      */     } 
/*      */     
/* 1441 */     if (rightStrong) {
/* 1442 */       if (result_type == 0 || result_type == 2) {
/* 1443 */         if (type1_p == 0 || type1_p == 2) {
/* 1444 */           this.bytecode.addOpcode(94);
/*      */         } else {
/* 1446 */           this.bytecode.addOpcode(93);
/*      */         } 
/* 1448 */         this.bytecode.addOpcode(88);
/* 1449 */         this.bytecode.addOpcode(op);
/* 1450 */         this.bytecode.addOpcode(94);
/* 1451 */         this.bytecode.addOpcode(88);
/*      */       }
/* 1453 */       else if (result_type == 1) {
/* 1454 */         if (type1_p == 2) {
/* 1455 */           this.bytecode.addOpcode(91);
/* 1456 */           this.bytecode.addOpcode(87);
/*      */         } else {
/*      */           
/* 1459 */           this.bytecode.addOpcode(95);
/*      */         } 
/* 1461 */         this.bytecode.addOpcode(op);
/* 1462 */         this.bytecode.addOpcode(95);
/*      */       } else {
/*      */         
/* 1465 */         fatal();
/*      */       } 
/* 1467 */     } else if (op != 0) {
/* 1468 */       this.bytecode.addOpcode(op);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void atCastExpr(CastExpr expr) throws CompileError {
/* 1473 */     String cname = resolveClassName(expr.getClassName());
/* 1474 */     String toClass = checkCastExpr(expr, cname);
/* 1475 */     int srcType = this.exprType;
/* 1476 */     this.exprType = expr.getType();
/* 1477 */     this.arrayDim = expr.getArrayDim();
/* 1478 */     this.className = cname;
/* 1479 */     if (toClass == null) {
/* 1480 */       atNumCastExpr(srcType, this.exprType);
/*      */     } else {
/* 1482 */       this.bytecode.addCheckcast(toClass);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void atInstanceOfExpr(InstanceOfExpr expr) throws CompileError {
/* 1487 */     String cname = resolveClassName(expr.getClassName());
/* 1488 */     String toClass = checkCastExpr((CastExpr)expr, cname);
/* 1489 */     this.bytecode.addInstanceof(toClass);
/* 1490 */     this.exprType = 301;
/* 1491 */     this.arrayDim = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String checkCastExpr(CastExpr expr, String name) throws CompileError {
/* 1497 */     String msg = "invalid cast";
/* 1498 */     ASTree oprand = expr.getOprand();
/* 1499 */     int dim = expr.getArrayDim();
/* 1500 */     int type = expr.getType();
/* 1501 */     oprand.accept(this);
/* 1502 */     int srcType = this.exprType;
/* 1503 */     int srcDim = this.arrayDim;
/* 1504 */     if (invalidDim(srcType, this.arrayDim, this.className, type, dim, name, true) || srcType == 344 || type == 344)
/*      */     {
/* 1506 */       throw new CompileError("invalid cast");
/*      */     }
/* 1508 */     if (type == 307) {
/* 1509 */       if (!isRefType(srcType) && srcDim == 0) {
/* 1510 */         throw new CompileError("invalid cast");
/*      */       }
/* 1512 */       return toJvmArrayName(name, dim);
/*      */     } 
/*      */     
/* 1515 */     if (dim > 0) {
/* 1516 */       return toJvmTypeName(type, dim);
/*      */     }
/* 1518 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   void atNumCastExpr(int srcType, int destType) throws CompileError {
/*      */     int op, op2;
/* 1524 */     if (srcType == destType) {
/*      */       return;
/*      */     }
/*      */     
/* 1528 */     int stype = typePrecedence(srcType);
/* 1529 */     int dtype = typePrecedence(destType);
/* 1530 */     if (0 <= stype && stype < 3) {
/* 1531 */       op = castOp[stype * 4 + dtype];
/*      */     } else {
/* 1533 */       op = 0;
/*      */     } 
/* 1535 */     if (destType == 312) {
/* 1536 */       op2 = 135;
/* 1537 */     } else if (destType == 317) {
/* 1538 */       op2 = 134;
/* 1539 */     } else if (destType == 326) {
/* 1540 */       op2 = 133;
/* 1541 */     } else if (destType == 334) {
/* 1542 */       op2 = 147;
/* 1543 */     } else if (destType == 306) {
/* 1544 */       op2 = 146;
/* 1545 */     } else if (destType == 303) {
/* 1546 */       op2 = 145;
/*      */     } else {
/* 1548 */       op2 = 0;
/*      */     } 
/* 1550 */     if (op != 0) {
/* 1551 */       this.bytecode.addOpcode(op);
/*      */     }
/* 1553 */     if ((op == 0 || op == 136 || op == 139 || op == 142) && 
/* 1554 */       op2 != 0) {
/* 1555 */       this.bytecode.addOpcode(op2);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void atExpr(Expr expr) throws CompileError {
/* 1563 */     int token = expr.getOperator();
/* 1564 */     ASTree oprand = expr.oprand1();
/* 1565 */     if (token == 46) {
/* 1566 */       String member = ((Symbol)expr.oprand2()).get();
/* 1567 */       if (member.equals("class")) {
/* 1568 */         atClassObject(expr);
/*      */       } else {
/* 1570 */         atFieldRead((ASTree)expr);
/*      */       } 
/* 1572 */     } else if (token == 35) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1577 */       atFieldRead((ASTree)expr);
/*      */     }
/* 1579 */     else if (token == 65) {
/* 1580 */       atArrayRead(oprand, expr.oprand2());
/* 1581 */     } else if (token == 362 || token == 363) {
/* 1582 */       atPlusPlus(token, oprand, expr, true);
/* 1583 */     } else if (token == 33) {
/* 1584 */       if (!booleanExpr(false, (ASTree)expr)) {
/* 1585 */         this.bytecode.addIndex(7);
/* 1586 */         this.bytecode.addIconst(1);
/* 1587 */         this.bytecode.addOpcode(167);
/* 1588 */         this.bytecode.addIndex(4);
/*      */       } 
/*      */       
/* 1591 */       this.bytecode.addIconst(0);
/*      */     }
/* 1593 */     else if (token == 67) {
/* 1594 */       fatal();
/*      */     } else {
/* 1596 */       expr.oprand1().accept(this);
/* 1597 */       int type = typePrecedence(this.exprType);
/* 1598 */       if (this.arrayDim > 0) {
/* 1599 */         badType(expr);
/*      */       }
/* 1601 */       if (token == 45) {
/* 1602 */         if (type == 0) {
/* 1603 */           this.bytecode.addOpcode(119);
/* 1604 */         } else if (type == 1) {
/* 1605 */           this.bytecode.addOpcode(118);
/* 1606 */         } else if (type == 2) {
/* 1607 */           this.bytecode.addOpcode(117);
/* 1608 */         } else if (type == 3) {
/* 1609 */           this.bytecode.addOpcode(116);
/* 1610 */           this.exprType = 324;
/*      */         } else {
/*      */           
/* 1613 */           badType(expr);
/*      */         } 
/* 1615 */       } else if (token == 126) {
/* 1616 */         if (type == 3) {
/* 1617 */           this.bytecode.addIconst(-1);
/* 1618 */           this.bytecode.addOpcode(130);
/* 1619 */           this.exprType = 324;
/*      */         }
/* 1621 */         else if (type == 2) {
/* 1622 */           this.bytecode.addLconst(-1L);
/* 1623 */           this.bytecode.addOpcode(131);
/*      */         } else {
/*      */           
/* 1626 */           badType(expr);
/*      */         }
/*      */       
/* 1629 */       } else if (token == 43) {
/* 1630 */         if (type == -1) {
/* 1631 */           badType(expr);
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/* 1636 */         fatal();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   protected static void badType(Expr expr) throws CompileError {
/* 1641 */     throw new CompileError("invalid type for " + expr.getName());
/*      */   }
/*      */ 
/*      */   
/*      */   public abstract void atCallExpr(CallExpr paramCallExpr) throws CompileError;
/*      */   
/*      */   protected abstract void atFieldRead(ASTree paramASTree) throws CompileError;
/*      */   
/*      */   public void atClassObject(Expr expr) throws CompileError {
/* 1650 */     ASTree op1 = expr.oprand1();
/* 1651 */     if (!(op1 instanceof Symbol)) {
/* 1652 */       throw new CompileError("fatal error: badly parsed .class expr");
/*      */     }
/* 1654 */     String cname = ((Symbol)op1).get();
/* 1655 */     if (cname.startsWith("[")) {
/* 1656 */       int i = cname.indexOf("[L");
/* 1657 */       if (i >= 0) {
/* 1658 */         String name = cname.substring(i + 2, cname.length() - 1);
/* 1659 */         String name2 = resolveClassName(name);
/* 1660 */         if (!name.equals(name2)) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1665 */           name2 = MemberResolver.jvmToJavaName(name2);
/* 1666 */           StringBuffer sbuf = new StringBuffer();
/* 1667 */           while (i-- >= 0) {
/* 1668 */             sbuf.append('[');
/*      */           }
/* 1670 */           sbuf.append('L').append(name2).append(';');
/* 1671 */           cname = sbuf.toString();
/*      */         } 
/*      */       } 
/*      */     } else {
/*      */       
/* 1676 */       cname = resolveClassName(MemberResolver.javaToJvmName(cname));
/* 1677 */       cname = MemberResolver.jvmToJavaName(cname);
/*      */     } 
/*      */     
/* 1680 */     atClassObject2(cname);
/* 1681 */     this.exprType = 307;
/* 1682 */     this.arrayDim = 0;
/* 1683 */     this.className = "java/lang/Class";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void atClassObject2(String cname) throws CompileError {
/* 1689 */     int start = this.bytecode.currentPc();
/* 1690 */     this.bytecode.addLdc(cname);
/* 1691 */     this.bytecode.addInvokestatic("java.lang.Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;");
/*      */     
/* 1693 */     int end = this.bytecode.currentPc();
/* 1694 */     this.bytecode.addOpcode(167);
/* 1695 */     int pc = this.bytecode.currentPc();
/* 1696 */     this.bytecode.addIndex(0);
/*      */     
/* 1698 */     this.bytecode.addExceptionHandler(start, end, this.bytecode.currentPc(), "java.lang.ClassNotFoundException");
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
/* 1717 */     this.bytecode.growStack(1);
/* 1718 */     this.bytecode.addInvokestatic("javassist.runtime.DotClass", "fail", "(Ljava/lang/ClassNotFoundException;)Ljava/lang/NoClassDefFoundError;");
/*      */ 
/*      */     
/* 1721 */     this.bytecode.addOpcode(191);
/* 1722 */     this.bytecode.write16bit(pc, this.bytecode.currentPc() - pc + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void atArrayRead(ASTree array, ASTree index) throws CompileError {
/* 1728 */     arrayAccess(array, index);
/* 1729 */     this.bytecode.addOpcode(getArrayReadOp(this.exprType, this.arrayDim));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void arrayAccess(ASTree array, ASTree index) throws CompileError {
/* 1735 */     array.accept(this);
/* 1736 */     int type = this.exprType;
/* 1737 */     int dim = this.arrayDim;
/* 1738 */     if (dim == 0) {
/* 1739 */       throw new CompileError("bad array access");
/*      */     }
/* 1741 */     String cname = this.className;
/*      */     
/* 1743 */     index.accept(this);
/* 1744 */     if (typePrecedence(this.exprType) != 3 || this.arrayDim > 0) {
/* 1745 */       throw new CompileError("bad array index");
/*      */     }
/* 1747 */     this.exprType = type;
/* 1748 */     this.arrayDim = dim - 1;
/* 1749 */     this.className = cname;
/*      */   }
/*      */   
/*      */   protected static int getArrayReadOp(int type, int dim) {
/* 1753 */     if (dim > 0) {
/* 1754 */       return 50;
/*      */     }
/* 1756 */     switch (type) {
/*      */       case 312:
/* 1758 */         return 49;
/*      */       case 317:
/* 1760 */         return 48;
/*      */       case 326:
/* 1762 */         return 47;
/*      */       case 324:
/* 1764 */         return 46;
/*      */       case 334:
/* 1766 */         return 53;
/*      */       case 306:
/* 1768 */         return 52;
/*      */       case 301:
/*      */       case 303:
/* 1771 */         return 51;
/*      */     } 
/* 1773 */     return 50;
/*      */   }
/*      */ 
/*      */   
/*      */   protected static int getArrayWriteOp(int type, int dim) {
/* 1778 */     if (dim > 0) {
/* 1779 */       return 83;
/*      */     }
/* 1781 */     switch (type) {
/*      */       case 312:
/* 1783 */         return 82;
/*      */       case 317:
/* 1785 */         return 81;
/*      */       case 326:
/* 1787 */         return 80;
/*      */       case 324:
/* 1789 */         return 79;
/*      */       case 334:
/* 1791 */         return 86;
/*      */       case 306:
/* 1793 */         return 85;
/*      */       case 301:
/*      */       case 303:
/* 1796 */         return 84;
/*      */     } 
/* 1798 */     return 83;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void atPlusPlus(int token, ASTree oprand, Expr expr, boolean doDup) throws CompileError {
/* 1805 */     boolean isPost = (oprand == null);
/* 1806 */     if (isPost) {
/* 1807 */       oprand = expr.oprand2();
/*      */     }
/* 1809 */     if (oprand instanceof Variable) {
/* 1810 */       Declarator d = ((Variable)oprand).getDeclarator();
/* 1811 */       int t = this.exprType = d.getType();
/* 1812 */       this.arrayDim = d.getArrayDim();
/* 1813 */       int var = getLocalVar(d);
/* 1814 */       if (this.arrayDim > 0) {
/* 1815 */         badType(expr);
/*      */       }
/* 1817 */       if (t == 312) {
/* 1818 */         this.bytecode.addDload(var);
/* 1819 */         if (doDup && isPost) {
/* 1820 */           this.bytecode.addOpcode(92);
/*      */         }
/* 1822 */         this.bytecode.addDconst(1.0D);
/* 1823 */         this.bytecode.addOpcode((token == 362) ? 99 : 103);
/* 1824 */         if (doDup && !isPost) {
/* 1825 */           this.bytecode.addOpcode(92);
/*      */         }
/* 1827 */         this.bytecode.addDstore(var);
/*      */       }
/* 1829 */       else if (t == 326) {
/* 1830 */         this.bytecode.addLload(var);
/* 1831 */         if (doDup && isPost) {
/* 1832 */           this.bytecode.addOpcode(92);
/*      */         }
/* 1834 */         this.bytecode.addLconst(1L);
/* 1835 */         this.bytecode.addOpcode((token == 362) ? 97 : 101);
/* 1836 */         if (doDup && !isPost) {
/* 1837 */           this.bytecode.addOpcode(92);
/*      */         }
/* 1839 */         this.bytecode.addLstore(var);
/*      */       }
/* 1841 */       else if (t == 317) {
/* 1842 */         this.bytecode.addFload(var);
/* 1843 */         if (doDup && isPost) {
/* 1844 */           this.bytecode.addOpcode(89);
/*      */         }
/* 1846 */         this.bytecode.addFconst(1.0F);
/* 1847 */         this.bytecode.addOpcode((token == 362) ? 98 : 102);
/* 1848 */         if (doDup && !isPost) {
/* 1849 */           this.bytecode.addOpcode(89);
/*      */         }
/* 1851 */         this.bytecode.addFstore(var);
/*      */       }
/* 1853 */       else if (t == 303 || t == 306 || t == 334 || t == 324) {
/* 1854 */         if (doDup && isPost) {
/* 1855 */           this.bytecode.addIload(var);
/*      */         }
/* 1857 */         int delta = (token == 362) ? 1 : -1;
/* 1858 */         if (var > 255) {
/* 1859 */           this.bytecode.addOpcode(196);
/* 1860 */           this.bytecode.addOpcode(132);
/* 1861 */           this.bytecode.addIndex(var);
/* 1862 */           this.bytecode.addIndex(delta);
/*      */         } else {
/*      */           
/* 1865 */           this.bytecode.addOpcode(132);
/* 1866 */           this.bytecode.add(var);
/* 1867 */           this.bytecode.add(delta);
/*      */         } 
/*      */         
/* 1870 */         if (doDup && !isPost) {
/* 1871 */           this.bytecode.addIload(var);
/*      */         }
/*      */       } else {
/* 1874 */         badType(expr);
/*      */       } 
/*      */     } else {
/* 1877 */       if (oprand instanceof Expr) {
/* 1878 */         Expr e = (Expr)oprand;
/* 1879 */         if (e.getOperator() == 65) {
/* 1880 */           atArrayPlusPlus(token, isPost, e, doDup);
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/* 1885 */       atFieldPlusPlus(token, isPost, oprand, expr, doDup);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void atArrayPlusPlus(int token, boolean isPost, Expr expr, boolean doDup) throws CompileError {
/* 1892 */     arrayAccess(expr.oprand1(), expr.oprand2());
/* 1893 */     int t = this.exprType;
/* 1894 */     int dim = this.arrayDim;
/* 1895 */     if (dim > 0) {
/* 1896 */       badType(expr);
/*      */     }
/* 1898 */     this.bytecode.addOpcode(92);
/* 1899 */     this.bytecode.addOpcode(getArrayReadOp(t, this.arrayDim));
/* 1900 */     int dup_code = is2word(t, dim) ? 94 : 91;
/* 1901 */     atPlusPlusCore(dup_code, doDup, token, isPost, expr);
/* 1902 */     this.bytecode.addOpcode(getArrayWriteOp(t, dim));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void atPlusPlusCore(int dup_code, boolean doDup, int token, boolean isPost, Expr expr) throws CompileError {
/* 1909 */     int t = this.exprType;
/*      */     
/* 1911 */     if (doDup && isPost) {
/* 1912 */       this.bytecode.addOpcode(dup_code);
/*      */     }
/* 1914 */     if (t == 324 || t == 303 || t == 306 || t == 334) {
/* 1915 */       this.bytecode.addIconst(1);
/* 1916 */       this.bytecode.addOpcode((token == 362) ? 96 : 100);
/* 1917 */       this.exprType = 324;
/*      */     }
/* 1919 */     else if (t == 326) {
/* 1920 */       this.bytecode.addLconst(1L);
/* 1921 */       this.bytecode.addOpcode((token == 362) ? 97 : 101);
/*      */     }
/* 1923 */     else if (t == 317) {
/* 1924 */       this.bytecode.addFconst(1.0F);
/* 1925 */       this.bytecode.addOpcode((token == 362) ? 98 : 102);
/*      */     }
/* 1927 */     else if (t == 312) {
/* 1928 */       this.bytecode.addDconst(1.0D);
/* 1929 */       this.bytecode.addOpcode((token == 362) ? 99 : 103);
/*      */     } else {
/*      */       
/* 1932 */       badType(expr);
/*      */     } 
/* 1934 */     if (doDup && !isPost) {
/* 1935 */       this.bytecode.addOpcode(dup_code);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected abstract void atFieldPlusPlus(int paramInt, boolean paramBoolean1, ASTree paramASTree, Expr paramExpr, boolean paramBoolean2) throws CompileError;
/*      */ 
/*      */   
/*      */   public abstract void atMember(Member paramMember) throws CompileError;
/*      */   
/*      */   public void atVariable(Variable v) throws CompileError {
/* 1946 */     Declarator d = v.getDeclarator();
/* 1947 */     this.exprType = d.getType();
/* 1948 */     this.arrayDim = d.getArrayDim();
/* 1949 */     this.className = d.getClassName();
/* 1950 */     int var = getLocalVar(d);
/*      */     
/* 1952 */     if (this.arrayDim > 0) {
/* 1953 */       this.bytecode.addAload(var);
/*      */     } else {
/* 1955 */       switch (this.exprType) {
/*      */         case 307:
/* 1957 */           this.bytecode.addAload(var);
/*      */           return;
/*      */         case 326:
/* 1960 */           this.bytecode.addLload(var);
/*      */           return;
/*      */         case 317:
/* 1963 */           this.bytecode.addFload(var);
/*      */           return;
/*      */         case 312:
/* 1966 */           this.bytecode.addDload(var);
/*      */           return;
/*      */       } 
/* 1969 */       this.bytecode.addIload(var);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void atKeyword(Keyword k) throws CompileError {
/* 1976 */     this.arrayDim = 0;
/* 1977 */     int token = k.get();
/* 1978 */     switch (token) {
/*      */       case 410:
/* 1980 */         this.bytecode.addIconst(1);
/* 1981 */         this.exprType = 301;
/*      */         return;
/*      */       case 411:
/* 1984 */         this.bytecode.addIconst(0);
/* 1985 */         this.exprType = 301;
/*      */         return;
/*      */       case 412:
/* 1988 */         this.bytecode.addOpcode(1);
/* 1989 */         this.exprType = 412;
/*      */         return;
/*      */       case 336:
/*      */       case 339:
/* 1993 */         if (this.inStaticMethod) {
/* 1994 */           throw new CompileError("not-available: " + (
/* 1995 */               (token == 339) ? "this" : "super"));
/*      */         }
/* 1997 */         this.bytecode.addAload(0);
/* 1998 */         this.exprType = 307;
/* 1999 */         if (token == 339) {
/* 2000 */           this.className = getThisName();
/*      */         } else {
/* 2002 */           this.className = getSuperName();
/*      */         }  return;
/*      */     } 
/* 2005 */     fatal();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void atStringL(StringL s) throws CompileError {
/* 2011 */     this.exprType = 307;
/* 2012 */     this.arrayDim = 0;
/* 2013 */     this.className = "java/lang/String";
/* 2014 */     this.bytecode.addLdc(s.get());
/*      */   }
/*      */ 
/*      */   
/*      */   public void atIntConst(IntConst i) throws CompileError {
/* 2019 */     this.arrayDim = 0;
/* 2020 */     long value = i.get();
/* 2021 */     int type = i.getType();
/* 2022 */     if (type == 402 || type == 401) {
/* 2023 */       this.exprType = (type == 402) ? 324 : 306;
/* 2024 */       this.bytecode.addIconst((int)value);
/*      */     } else {
/*      */       
/* 2027 */       this.exprType = 326;
/* 2028 */       this.bytecode.addLconst(value);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void atDoubleConst(DoubleConst d) throws CompileError {
/* 2034 */     this.arrayDim = 0;
/* 2035 */     if (d.getType() == 405) {
/* 2036 */       this.exprType = 312;
/* 2037 */       this.bytecode.addDconst(d.get());
/*      */     } else {
/*      */       
/* 2040 */       this.exprType = 317;
/* 2041 */       this.bytecode.addFconst((float)d.get());
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\CodeGen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */