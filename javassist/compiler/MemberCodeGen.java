/*      */ package javassist.compiler;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import javassist.ClassPool;
/*      */ import javassist.CtClass;
/*      */ import javassist.CtField;
/*      */ import javassist.CtMethod;
/*      */ import javassist.Modifier;
/*      */ import javassist.NotFoundException;
/*      */ import javassist.bytecode.AccessFlag;
/*      */ import javassist.bytecode.Bytecode;
/*      */ import javassist.bytecode.ClassFile;
/*      */ import javassist.bytecode.ConstPool;
/*      */ import javassist.bytecode.Descriptor;
/*      */ import javassist.bytecode.FieldInfo;
/*      */ import javassist.bytecode.MethodInfo;
/*      */ import javassist.compiler.ast.ASTList;
/*      */ import javassist.compiler.ast.ASTree;
/*      */ import javassist.compiler.ast.ArrayInit;
/*      */ import javassist.compiler.ast.CallExpr;
/*      */ import javassist.compiler.ast.Declarator;
/*      */ import javassist.compiler.ast.Expr;
/*      */ import javassist.compiler.ast.Keyword;
/*      */ import javassist.compiler.ast.Member;
/*      */ import javassist.compiler.ast.MethodDecl;
/*      */ import javassist.compiler.ast.NewExpr;
/*      */ import javassist.compiler.ast.Pair;
/*      */ import javassist.compiler.ast.Stmnt;
/*      */ import javassist.compiler.ast.Symbol;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class MemberCodeGen
/*      */   extends CodeGen
/*      */ {
/*      */   protected MemberResolver resolver;
/*      */   protected CtClass thisClass;
/*      */   protected MethodInfo thisMethod;
/*      */   protected boolean resultStatic;
/*      */   
/*      */   public MemberCodeGen(Bytecode b, CtClass cc, ClassPool cp) {
/*   60 */     super(b);
/*   61 */     this.resolver = new MemberResolver(cp);
/*   62 */     this.thisClass = cc;
/*   63 */     this.thisMethod = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMajorVersion() {
/*   71 */     ClassFile cf = this.thisClass.getClassFile2();
/*   72 */     if (cf == null)
/*   73 */       return ClassFile.MAJOR_VERSION; 
/*   74 */     return cf.getMajorVersion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setThisMethod(CtMethod m) {
/*   81 */     this.thisMethod = m.getMethodInfo2();
/*   82 */     if (this.typeChecker != null)
/*   83 */       this.typeChecker.setThisMethod(this.thisMethod); 
/*      */   }
/*      */   public CtClass getThisClass() {
/*   86 */     return this.thisClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getThisName() {
/*   93 */     return MemberResolver.javaToJvmName(this.thisClass.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getSuperName() throws CompileError {
/*  101 */     return MemberResolver.javaToJvmName(
/*  102 */         MemberResolver.getSuperclass(this.thisClass).getName());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void insertDefaultSuperCall() throws CompileError {
/*  107 */     this.bytecode.addAload(0);
/*  108 */     this.bytecode.addInvokespecial(MemberResolver.getSuperclass(this.thisClass), "<init>", "()V");
/*      */   }
/*      */   
/*      */   static class JsrHook
/*      */     extends CodeGen.ReturnHook {
/*      */     List<int[]> jsrList;
/*      */     CodeGen cgen;
/*      */     int var;
/*      */     
/*      */     JsrHook(CodeGen gen) {
/*  118 */       super(gen);
/*  119 */       this.jsrList = (List)new ArrayList<>();
/*  120 */       this.cgen = gen;
/*  121 */       this.var = -1;
/*      */     }
/*      */     
/*      */     private int getVar(int size) {
/*  125 */       if (this.var < 0) {
/*  126 */         this.var = this.cgen.getMaxLocals();
/*  127 */         this.cgen.incMaxLocals(size);
/*      */       } 
/*      */       
/*  130 */       return this.var;
/*      */     }
/*      */     
/*      */     private void jsrJmp(Bytecode b) {
/*  134 */       b.addOpcode(167);
/*  135 */       this.jsrList.add(new int[] { b.currentPc(), this.var });
/*  136 */       b.addIndex(0);
/*      */     }
/*      */ 
/*      */     
/*      */     protected boolean doit(Bytecode b, int opcode) {
/*  141 */       switch (opcode) {
/*      */         case 177:
/*  143 */           jsrJmp(b);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  174 */           return false;case 176: b.addAstore(getVar(1)); jsrJmp(b); b.addAload(this.var); return false;case 172: b.addIstore(getVar(1)); jsrJmp(b); b.addIload(this.var); return false;case 173: b.addLstore(getVar(2)); jsrJmp(b); b.addLload(this.var); return false;case 175: b.addDstore(getVar(2)); jsrJmp(b); b.addDload(this.var); return false;case 174: b.addFstore(getVar(1)); jsrJmp(b); b.addFload(this.var); return false;
/*      */       } 
/*      */       throw new RuntimeException("fatal");
/*      */     } }
/*      */   
/*      */   static class JsrHook2 extends CodeGen.ReturnHook { int var;
/*      */     int target;
/*      */     
/*      */     JsrHook2(CodeGen gen, int[] retTarget) {
/*  183 */       super(gen);
/*  184 */       this.target = retTarget[0];
/*  185 */       this.var = retTarget[1];
/*      */     }
/*      */ 
/*      */     
/*      */     protected boolean doit(Bytecode b, int opcode) {
/*  190 */       switch (opcode)
/*      */       
/*      */       { 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 177:
/*  212 */           b.addOpcode(167);
/*  213 */           b.addIndex(this.target - b.currentPc() + 3);
/*  214 */           return true;
/*      */         case 176: b.addAstore(this.var);
/*      */         case 172: b.addIstore(this.var);
/*      */         case 173: b.addLstore(this.var);
/*      */         case 175: b.addDstore(this.var);
/*      */         case 174:
/*  220 */           b.addFstore(this.var); }  throw new RuntimeException("fatal"); } } protected void atTryStmnt(Stmnt st) throws CompileError { Bytecode bc = this.bytecode;
/*  221 */     Stmnt body = (Stmnt)st.getLeft();
/*  222 */     if (body == null) {
/*      */       return;
/*      */     }
/*  225 */     ASTList catchList = (ASTList)st.getRight().getLeft();
/*  226 */     Stmnt finallyBlock = (Stmnt)st.getRight().getRight().getLeft();
/*  227 */     List<Integer> gotoList = new ArrayList<>();
/*      */     
/*  229 */     JsrHook jsrHook = null;
/*  230 */     if (finallyBlock != null) {
/*  231 */       jsrHook = new JsrHook(this);
/*      */     }
/*  233 */     int start = bc.currentPc();
/*  234 */     body.accept(this);
/*  235 */     int end = bc.currentPc();
/*  236 */     if (start == end) {
/*  237 */       throw new CompileError("empty try block");
/*      */     }
/*  239 */     boolean tryNotReturn = !this.hasReturned;
/*  240 */     if (tryNotReturn) {
/*  241 */       bc.addOpcode(167);
/*  242 */       gotoList.add(Integer.valueOf(bc.currentPc()));
/*  243 */       bc.addIndex(0);
/*      */     } 
/*      */     
/*  246 */     int var = getMaxLocals();
/*  247 */     incMaxLocals(1);
/*  248 */     while (catchList != null) {
/*      */       
/*  250 */       Pair p = (Pair)catchList.head();
/*  251 */       catchList = catchList.tail();
/*  252 */       Declarator decl = (Declarator)p.getLeft();
/*  253 */       Stmnt block = (Stmnt)p.getRight();
/*      */       
/*  255 */       decl.setLocalVar(var);
/*      */       
/*  257 */       CtClass type = this.resolver.lookupClassByJvmName(decl.getClassName());
/*  258 */       decl.setClassName(MemberResolver.javaToJvmName(type.getName()));
/*  259 */       bc.addExceptionHandler(start, end, bc.currentPc(), type);
/*  260 */       bc.growStack(1);
/*  261 */       bc.addAstore(var);
/*  262 */       this.hasReturned = false;
/*  263 */       if (block != null) {
/*  264 */         block.accept(this);
/*      */       }
/*  266 */       if (!this.hasReturned) {
/*  267 */         bc.addOpcode(167);
/*  268 */         gotoList.add(Integer.valueOf(bc.currentPc()));
/*  269 */         bc.addIndex(0);
/*  270 */         tryNotReturn = true;
/*      */       } 
/*      */     } 
/*      */     
/*  274 */     if (finallyBlock != null) {
/*  275 */       jsrHook.remove(this);
/*      */       
/*  277 */       int pcAnyCatch = bc.currentPc();
/*  278 */       bc.addExceptionHandler(start, pcAnyCatch, pcAnyCatch, 0);
/*  279 */       bc.growStack(1);
/*  280 */       bc.addAstore(var);
/*  281 */       this.hasReturned = false;
/*  282 */       finallyBlock.accept(this);
/*  283 */       if (!this.hasReturned) {
/*  284 */         bc.addAload(var);
/*  285 */         bc.addOpcode(191);
/*      */       } 
/*      */       
/*  288 */       addFinally(jsrHook.jsrList, finallyBlock);
/*      */     } 
/*      */     
/*  291 */     int pcEnd = bc.currentPc();
/*  292 */     patchGoto(gotoList, pcEnd);
/*  293 */     this.hasReturned = !tryNotReturn;
/*  294 */     if (finallyBlock != null && 
/*  295 */       tryNotReturn) {
/*  296 */       finallyBlock.accept(this);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addFinally(List<int[]> returnList, Stmnt finallyBlock) throws CompileError {
/*  306 */     Bytecode bc = this.bytecode;
/*  307 */     for (int[] ret : returnList) {
/*  308 */       int pc = ret[0];
/*  309 */       bc.write16bit(pc, bc.currentPc() - pc + 1);
/*  310 */       CodeGen.ReturnHook hook = new JsrHook2(this, ret);
/*  311 */       finallyBlock.accept(this);
/*  312 */       hook.remove(this);
/*  313 */       if (!this.hasReturned) {
/*  314 */         bc.addOpcode(167);
/*  315 */         bc.addIndex(pc + 3 - bc.currentPc());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void atNewExpr(NewExpr expr) throws CompileError {
/*  322 */     if (expr.isArray()) {
/*  323 */       atNewArrayExpr(expr);
/*      */     } else {
/*  325 */       CtClass clazz = this.resolver.lookupClassByName(expr.getClassName());
/*  326 */       String cname = clazz.getName();
/*  327 */       ASTList args = expr.getArguments();
/*  328 */       this.bytecode.addNew(cname);
/*  329 */       this.bytecode.addOpcode(89);
/*      */       
/*  331 */       atMethodCallCore(clazz, "<init>", args, false, true, -1, (MemberResolver.Method)null);
/*      */ 
/*      */       
/*  334 */       this.exprType = 307;
/*  335 */       this.arrayDim = 0;
/*  336 */       this.className = MemberResolver.javaToJvmName(cname);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void atNewArrayExpr(NewExpr expr) throws CompileError {
/*  341 */     int type = expr.getArrayType();
/*  342 */     ASTList size = expr.getArraySize();
/*  343 */     ASTList classname = expr.getClassName();
/*  344 */     ArrayInit init = expr.getInitializer();
/*  345 */     if (size.length() > 1) {
/*  346 */       if (init != null) {
/*  347 */         throw new CompileError("sorry, multi-dimensional array initializer for new is not supported");
/*      */       }
/*      */ 
/*      */       
/*  351 */       atMultiNewArray(type, classname, size);
/*      */       
/*      */       return;
/*      */     } 
/*  355 */     ASTree sizeExpr = size.head();
/*  356 */     atNewArrayExpr2(type, sizeExpr, Declarator.astToClassName(classname, '/'), init);
/*      */   }
/*      */   
/*      */   private void atNewArrayExpr2(int type, ASTree sizeExpr, String jvmClassname, ArrayInit init) throws CompileError {
/*      */     String elementClass;
/*  361 */     if (init == null) {
/*  362 */       if (sizeExpr == null) {
/*  363 */         throw new CompileError("no array size");
/*      */       }
/*  365 */       sizeExpr.accept(this);
/*      */     }
/*  367 */     else if (sizeExpr == null) {
/*  368 */       int s = init.size();
/*  369 */       this.bytecode.addIconst(s);
/*      */     } else {
/*      */       
/*  372 */       throw new CompileError("unnecessary array size specified for new");
/*      */     } 
/*      */     
/*  375 */     if (type == 307) {
/*  376 */       elementClass = resolveClassName(jvmClassname);
/*  377 */       this.bytecode.addAnewarray(MemberResolver.jvmToJavaName(elementClass));
/*      */     } else {
/*      */       
/*  380 */       elementClass = null;
/*  381 */       int atype = 0;
/*  382 */       switch (type) {
/*      */         case 301:
/*  384 */           atype = 4;
/*      */           break;
/*      */         case 306:
/*  387 */           atype = 5;
/*      */           break;
/*      */         case 317:
/*  390 */           atype = 6;
/*      */           break;
/*      */         case 312:
/*  393 */           atype = 7;
/*      */           break;
/*      */         case 303:
/*  396 */           atype = 8;
/*      */           break;
/*      */         case 334:
/*  399 */           atype = 9;
/*      */           break;
/*      */         case 324:
/*  402 */           atype = 10;
/*      */           break;
/*      */         case 326:
/*  405 */           atype = 11;
/*      */           break;
/*      */         default:
/*  408 */           badNewExpr();
/*      */           break;
/*      */       } 
/*      */       
/*  412 */       this.bytecode.addOpcode(188);
/*  413 */       this.bytecode.add(atype);
/*      */     } 
/*      */     
/*  416 */     if (init != null) {
/*  417 */       int s = init.size();
/*  418 */       ArrayInit arrayInit = init;
/*  419 */       for (int i = 0; i < s; i++) {
/*  420 */         this.bytecode.addOpcode(89);
/*  421 */         this.bytecode.addIconst(i);
/*  422 */         arrayInit.head().accept(this);
/*  423 */         if (!isRefType(type)) {
/*  424 */           atNumCastExpr(this.exprType, type);
/*      */         }
/*  426 */         this.bytecode.addOpcode(getArrayWriteOp(type, 0));
/*  427 */         ASTList aSTList = arrayInit.tail();
/*      */       } 
/*      */     } 
/*      */     
/*  431 */     this.exprType = type;
/*  432 */     this.arrayDim = 1;
/*  433 */     this.className = elementClass;
/*      */   }
/*      */   
/*      */   private static void badNewExpr() throws CompileError {
/*  437 */     throw new CompileError("bad new expression");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void atArrayVariableAssign(ArrayInit init, int varType, int varArray, String varClass) throws CompileError {
/*  443 */     atNewArrayExpr2(varType, (ASTree)null, varClass, init);
/*      */   }
/*      */ 
/*      */   
/*      */   public void atArrayInit(ArrayInit init) throws CompileError {
/*  448 */     throw new CompileError("array initializer is not supported");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void atMultiNewArray(int type, ASTList classname, ASTList size) throws CompileError {
/*      */     String desc;
/*  455 */     int dim = size.length(); int count;
/*  456 */     for (count = 0; size != null; size = size.tail()) {
/*  457 */       ASTree s = size.head();
/*  458 */       if (s == null) {
/*      */         break;
/*      */       }
/*  461 */       count++;
/*  462 */       s.accept(this);
/*  463 */       if (this.exprType != 324) {
/*  464 */         throw new CompileError("bad type for array size");
/*      */       }
/*      */     } 
/*      */     
/*  468 */     this.exprType = type;
/*  469 */     this.arrayDim = dim;
/*  470 */     if (type == 307) {
/*  471 */       this.className = resolveClassName(classname);
/*  472 */       desc = toJvmArrayName(this.className, dim);
/*      */     } else {
/*      */       
/*  475 */       desc = toJvmTypeName(type, dim);
/*      */     } 
/*  477 */     this.bytecode.addMultiNewarray(desc, count);
/*      */   }
/*      */ 
/*      */   
/*      */   public void atCallExpr(CallExpr expr) throws CompileError {
/*  482 */     String mname = null;
/*  483 */     CtClass targetClass = null;
/*  484 */     ASTree method = expr.oprand1();
/*  485 */     ASTList args = (ASTList)expr.oprand2();
/*  486 */     boolean isStatic = false;
/*  487 */     boolean isSpecial = false;
/*  488 */     int aload0pos = -1;
/*      */     
/*  490 */     MemberResolver.Method cached = expr.getMethod();
/*  491 */     if (method instanceof Member) {
/*  492 */       mname = ((Member)method).get();
/*  493 */       targetClass = this.thisClass;
/*  494 */       if (this.inStaticMethod || (cached != null && cached.isStatic())) {
/*  495 */         isStatic = true;
/*      */       } else {
/*  497 */         aload0pos = this.bytecode.currentPc();
/*  498 */         this.bytecode.addAload(0);
/*      */       }
/*      */     
/*  501 */     } else if (method instanceof Keyword) {
/*  502 */       isSpecial = true;
/*  503 */       mname = "<init>";
/*  504 */       targetClass = this.thisClass;
/*  505 */       if (this.inStaticMethod)
/*  506 */         throw new CompileError("a constructor cannot be static"); 
/*  507 */       this.bytecode.addAload(0);
/*      */       
/*  509 */       if (((Keyword)method).get() == 336) {
/*  510 */         targetClass = MemberResolver.getSuperclass(targetClass);
/*      */       }
/*  512 */     } else if (method instanceof Expr) {
/*  513 */       Expr e = (Expr)method;
/*  514 */       mname = ((Symbol)e.oprand2()).get();
/*  515 */       int op = e.getOperator();
/*  516 */       if (op == 35) {
/*      */         
/*  518 */         targetClass = this.resolver.lookupClass(((Symbol)e.oprand1()).get(), false);
/*  519 */         isStatic = true;
/*      */       }
/*  521 */       else if (op == 46) {
/*  522 */         ASTree target = e.oprand1();
/*  523 */         String classFollowedByDotSuper = TypeChecker.isDotSuper(target);
/*  524 */         if (classFollowedByDotSuper != null) {
/*  525 */           isSpecial = true;
/*  526 */           targetClass = MemberResolver.getSuperInterface(this.thisClass, classFollowedByDotSuper);
/*      */           
/*  528 */           if (this.inStaticMethod || (cached != null && cached.isStatic())) {
/*  529 */             isStatic = true;
/*      */           } else {
/*  531 */             aload0pos = this.bytecode.currentPc();
/*  532 */             this.bytecode.addAload(0);
/*      */           } 
/*      */         } else {
/*      */           
/*  536 */           if (target instanceof Keyword && (
/*  537 */             (Keyword)target).get() == 336) {
/*  538 */             isSpecial = true;
/*      */           }
/*      */           try {
/*  541 */             target.accept(this);
/*      */           }
/*  543 */           catch (NoFieldException nfe) {
/*  544 */             if (nfe.getExpr() != target) {
/*  545 */               throw nfe;
/*      */             }
/*      */             
/*  548 */             this.exprType = 307;
/*  549 */             this.arrayDim = 0;
/*  550 */             this.className = nfe.getField();
/*  551 */             isStatic = true;
/*      */           } 
/*      */           
/*  554 */           if (this.arrayDim > 0) {
/*  555 */             targetClass = this.resolver.lookupClass("java.lang.Object", true);
/*  556 */           } else if (this.exprType == 307) {
/*  557 */             targetClass = this.resolver.lookupClassByJvmName(this.className);
/*      */           } else {
/*  559 */             badMethod();
/*      */           } 
/*      */         } 
/*      */       } else {
/*  563 */         badMethod();
/*      */       } 
/*      */     } else {
/*  566 */       fatal();
/*      */     } 
/*  568 */     atMethodCallCore(targetClass, mname, args, isStatic, isSpecial, aload0pos, cached);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void badMethod() throws CompileError {
/*  573 */     throw new CompileError("bad method");
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
/*      */   public void atMethodCallCore(CtClass targetClass, String mname, ASTList args, boolean isStatic, boolean isSpecial, int aload0pos, MemberResolver.Method found) throws CompileError {
/*  587 */     int nargs = getMethodArgsLength(args);
/*  588 */     int[] types = new int[nargs];
/*  589 */     int[] dims = new int[nargs];
/*  590 */     String[] cnames = new String[nargs];
/*      */     
/*  592 */     if (!isStatic && found != null && found.isStatic()) {
/*  593 */       this.bytecode.addOpcode(87);
/*  594 */       isStatic = true;
/*      */     } 
/*      */ 
/*      */     
/*  598 */     int stack = this.bytecode.getStackDepth();
/*      */ 
/*      */     
/*  601 */     atMethodArgs(args, types, dims, cnames);
/*      */     
/*  603 */     if (found == null) {
/*  604 */       found = this.resolver.lookupMethod(targetClass, this.thisClass, this.thisMethod, mname, types, dims, cnames);
/*      */     }
/*      */     
/*  607 */     if (found == null) {
/*      */       String msg;
/*  609 */       if (mname.equals("<init>")) {
/*  610 */         msg = "constructor not found";
/*      */       } else {
/*      */         
/*  613 */         msg = "Method " + mname + " not found in " + targetClass.getName();
/*      */       } 
/*  615 */       throw new CompileError(msg);
/*      */     } 
/*      */     
/*  618 */     atMethodCallCore2(targetClass, mname, isStatic, isSpecial, aload0pos, found);
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isFromSameDeclaringClass(CtClass outer, CtClass inner) {
/*      */     try {
/*  624 */       while (outer != null) {
/*  625 */         if (isEnclosing(outer, inner))
/*  626 */           return true; 
/*  627 */         outer = outer.getDeclaringClass();
/*      */       }
/*      */     
/*  630 */     } catch (NotFoundException notFoundException) {}
/*  631 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void atMethodCallCore2(CtClass targetClass, String mname, boolean isStatic, boolean isSpecial, int aload0pos, MemberResolver.Method found) throws CompileError {
/*  640 */     CtClass declClass = found.declaring;
/*  641 */     MethodInfo minfo = found.info;
/*  642 */     String desc = minfo.getDescriptor();
/*  643 */     int acc = minfo.getAccessFlags();
/*      */     
/*  645 */     if (mname.equals("<init>")) {
/*  646 */       isSpecial = true;
/*  647 */       if (declClass != targetClass) {
/*  648 */         throw new CompileError("no such constructor: " + targetClass.getName());
/*      */       }
/*  650 */       if (declClass != this.thisClass && AccessFlag.isPrivate(acc) && (
/*  651 */         declClass.getClassFile().getMajorVersion() < 55 || 
/*  652 */         !isFromSameDeclaringClass(declClass, this.thisClass))) {
/*  653 */         desc = getAccessibleConstructor(desc, declClass, minfo);
/*  654 */         this.bytecode.addOpcode(1);
/*      */       }
/*      */     
/*      */     }
/*  658 */     else if (AccessFlag.isPrivate(acc)) {
/*  659 */       if (declClass == this.thisClass) {
/*  660 */         isSpecial = true;
/*      */       } else {
/*  662 */         isSpecial = false;
/*  663 */         isStatic = true;
/*  664 */         String origDesc = desc;
/*  665 */         if ((acc & 0x8) == 0) {
/*  666 */           desc = Descriptor.insertParameter(declClass.getName(), origDesc);
/*      */         }
/*      */         
/*  669 */         acc = AccessFlag.setPackage(acc) | 0x8;
/*  670 */         mname = getAccessiblePrivate(mname, origDesc, desc, minfo, declClass);
/*      */       } 
/*      */     } 
/*      */     
/*  674 */     boolean popTarget = false;
/*  675 */     if ((acc & 0x8) != 0) {
/*  676 */       if (!isStatic) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  682 */         isStatic = true;
/*  683 */         if (aload0pos >= 0) {
/*  684 */           this.bytecode.write(aload0pos, 0);
/*      */         } else {
/*  686 */           popTarget = true;
/*      */         } 
/*      */       } 
/*  689 */       this.bytecode.addInvokestatic(declClass, mname, desc);
/*      */     }
/*  691 */     else if (isSpecial) {
/*  692 */       this.bytecode.addInvokespecial(targetClass, mname, desc);
/*      */     } else {
/*  694 */       if (!Modifier.isPublic(declClass.getModifiers()) || declClass
/*  695 */         .isInterface() != targetClass.isInterface()) {
/*  696 */         declClass = targetClass;
/*      */       }
/*  698 */       if (declClass.isInterface()) {
/*  699 */         int nargs = Descriptor.paramSize(desc) + 1;
/*  700 */         this.bytecode.addInvokeinterface(declClass, mname, desc, nargs);
/*      */       } else {
/*      */         
/*  703 */         if (isStatic) {
/*  704 */           throw new CompileError(mname + " is not static");
/*      */         }
/*  706 */         this.bytecode.addInvokevirtual(declClass, mname, desc);
/*      */       } 
/*      */     } 
/*  709 */     setReturnType(desc, isStatic, popTarget);
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
/*      */   protected String getAccessiblePrivate(String methodName, String desc, String newDesc, MethodInfo minfo, CtClass declClass) throws CompileError {
/*  724 */     if (isEnclosing(declClass, this.thisClass)) {
/*  725 */       AccessorMaker maker = declClass.getAccessorMaker();
/*  726 */       if (maker != null) {
/*  727 */         return maker.getMethodAccessor(methodName, desc, newDesc, minfo);
/*      */       }
/*      */     } 
/*      */     
/*  731 */     throw new CompileError("Method " + methodName + " is private");
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
/*      */   protected String getAccessibleConstructor(String desc, CtClass declClass, MethodInfo minfo) throws CompileError {
/*  748 */     if (isEnclosing(declClass, this.thisClass)) {
/*  749 */       AccessorMaker maker = declClass.getAccessorMaker();
/*  750 */       if (maker != null) {
/*  751 */         return maker.getConstructor(declClass, desc, minfo);
/*      */       }
/*      */     } 
/*  754 */     throw new CompileError("the called constructor is private in " + declClass
/*  755 */         .getName());
/*      */   }
/*      */   
/*      */   private boolean isEnclosing(CtClass outer, CtClass inner) {
/*      */     try {
/*  760 */       while (inner != null) {
/*  761 */         inner = inner.getDeclaringClass();
/*  762 */         if (inner == outer) {
/*  763 */           return true;
/*      */         }
/*      */       } 
/*  766 */     } catch (NotFoundException notFoundException) {}
/*  767 */     return false;
/*      */   }
/*      */   
/*      */   public int getMethodArgsLength(ASTList args) {
/*  771 */     return ASTList.length(args);
/*      */   }
/*      */ 
/*      */   
/*      */   public void atMethodArgs(ASTList args, int[] types, int[] dims, String[] cnames) throws CompileError {
/*  776 */     int i = 0;
/*  777 */     while (args != null) {
/*  778 */       ASTree a = args.head();
/*  779 */       a.accept(this);
/*  780 */       types[i] = this.exprType;
/*  781 */       dims[i] = this.arrayDim;
/*  782 */       cnames[i] = this.className;
/*  783 */       i++;
/*  784 */       args = args.tail();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void setReturnType(String desc, boolean isStatic, boolean popTarget) throws CompileError {
/*  791 */     int i = desc.indexOf(')');
/*  792 */     if (i < 0) {
/*  793 */       badMethod();
/*      */     }
/*  795 */     char c = desc.charAt(++i);
/*  796 */     int dim = 0;
/*  797 */     while (c == '[') {
/*  798 */       dim++;
/*  799 */       c = desc.charAt(++i);
/*      */     } 
/*      */     
/*  802 */     this.arrayDim = dim;
/*  803 */     if (c == 'L') {
/*  804 */       int j = desc.indexOf(';', i + 1);
/*  805 */       if (j < 0) {
/*  806 */         badMethod();
/*      */       }
/*  808 */       this.exprType = 307;
/*  809 */       this.className = desc.substring(i + 1, j);
/*      */     } else {
/*      */       
/*  812 */       this.exprType = MemberResolver.descToType(c);
/*  813 */       this.className = null;
/*      */     } 
/*      */     
/*  816 */     int etype = this.exprType;
/*  817 */     if (isStatic && 
/*  818 */       popTarget) {
/*  819 */       if (is2word(etype, dim)) {
/*  820 */         this.bytecode.addOpcode(93);
/*  821 */         this.bytecode.addOpcode(88);
/*  822 */         this.bytecode.addOpcode(87);
/*      */       }
/*  824 */       else if (etype == 344) {
/*  825 */         this.bytecode.addOpcode(87);
/*      */       } else {
/*  827 */         this.bytecode.addOpcode(95);
/*  828 */         this.bytecode.addOpcode(87);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void atFieldAssign(Expr expr, int op, ASTree left, ASTree right, boolean doDup) throws CompileError {
/*      */     int fi;
/*  838 */     CtField f = fieldAccess(left, false);
/*  839 */     boolean is_static = this.resultStatic;
/*  840 */     if (op != 61 && !is_static) {
/*  841 */       this.bytecode.addOpcode(89);
/*      */     }
/*      */     
/*  844 */     if (op == 61) {
/*  845 */       FieldInfo finfo = f.getFieldInfo2();
/*  846 */       setFieldType(finfo);
/*  847 */       AccessorMaker maker = isAccessibleField(f, finfo);
/*  848 */       if (maker == null) {
/*  849 */         fi = addFieldrefInfo(f, finfo);
/*      */       } else {
/*  851 */         fi = 0;
/*      */       } 
/*      */     } else {
/*  854 */       fi = atFieldRead(f, is_static);
/*      */     } 
/*  856 */     int fType = this.exprType;
/*  857 */     int fDim = this.arrayDim;
/*  858 */     String cname = this.className;
/*      */     
/*  860 */     atAssignCore(expr, op, right, fType, fDim, cname);
/*      */     
/*  862 */     boolean is2w = is2word(fType, fDim);
/*  863 */     if (doDup) {
/*      */       int dup_code;
/*  865 */       if (is_static) {
/*  866 */         dup_code = is2w ? 92 : 89;
/*      */       } else {
/*  868 */         dup_code = is2w ? 93 : 90;
/*      */       } 
/*  870 */       this.bytecode.addOpcode(dup_code);
/*      */     } 
/*      */     
/*  873 */     atFieldAssignCore(f, is_static, fi, is2w);
/*      */     
/*  875 */     this.exprType = fType;
/*  876 */     this.arrayDim = fDim;
/*  877 */     this.className = cname;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void atFieldAssignCore(CtField f, boolean is_static, int fi, boolean is2byte) throws CompileError {
/*  884 */     if (fi != 0) {
/*  885 */       if (is_static) {
/*  886 */         this.bytecode.add(179);
/*  887 */         this.bytecode.growStack(is2byte ? -2 : -1);
/*      */       } else {
/*      */         
/*  890 */         this.bytecode.add(181);
/*  891 */         this.bytecode.growStack(is2byte ? -3 : -2);
/*      */       } 
/*      */       
/*  894 */       this.bytecode.addIndex(fi);
/*      */     } else {
/*      */       
/*  897 */       CtClass declClass = f.getDeclaringClass();
/*  898 */       AccessorMaker maker = declClass.getAccessorMaker();
/*      */       
/*  900 */       FieldInfo finfo = f.getFieldInfo2();
/*  901 */       MethodInfo minfo = maker.getFieldSetter(finfo, is_static);
/*  902 */       this.bytecode.addInvokestatic(declClass, minfo.getName(), minfo
/*  903 */           .getDescriptor());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void atMember(Member mem) throws CompileError {
/*  911 */     atFieldRead((ASTree)mem);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void atFieldRead(ASTree expr) throws CompileError {
/*  917 */     CtField f = fieldAccess(expr, true);
/*  918 */     if (f == null) {
/*  919 */       atArrayLength(expr);
/*      */       
/*      */       return;
/*      */     } 
/*  923 */     boolean is_static = this.resultStatic;
/*  924 */     ASTree cexpr = TypeChecker.getConstantFieldValue(f);
/*  925 */     if (cexpr == null) {
/*  926 */       atFieldRead(f, is_static);
/*      */     } else {
/*  928 */       cexpr.accept(this);
/*  929 */       setFieldType(f.getFieldInfo2());
/*      */     } 
/*      */   }
/*      */   
/*      */   private void atArrayLength(ASTree expr) throws CompileError {
/*  934 */     if (this.arrayDim == 0) {
/*  935 */       throw new CompileError(".length applied to a non array");
/*      */     }
/*  937 */     this.bytecode.addOpcode(190);
/*  938 */     this.exprType = 324;
/*  939 */     this.arrayDim = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int atFieldRead(CtField f, boolean isStatic) throws CompileError {
/*  948 */     FieldInfo finfo = f.getFieldInfo2();
/*  949 */     boolean is2byte = setFieldType(finfo);
/*  950 */     AccessorMaker maker = isAccessibleField(f, finfo);
/*  951 */     if (maker != null) {
/*  952 */       MethodInfo minfo = maker.getFieldGetter(finfo, isStatic);
/*  953 */       this.bytecode.addInvokestatic(f.getDeclaringClass(), minfo.getName(), minfo
/*  954 */           .getDescriptor());
/*  955 */       return 0;
/*      */     } 
/*  957 */     int fi = addFieldrefInfo(f, finfo);
/*  958 */     if (isStatic) {
/*  959 */       this.bytecode.add(178);
/*  960 */       this.bytecode.growStack(is2byte ? 2 : 1);
/*      */     } else {
/*      */       
/*  963 */       this.bytecode.add(180);
/*  964 */       this.bytecode.growStack(is2byte ? 1 : 0);
/*      */     } 
/*      */     
/*  967 */     this.bytecode.addIndex(fi);
/*  968 */     return fi;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private AccessorMaker isAccessibleField(CtField f, FieldInfo finfo) throws CompileError {
/*  979 */     if (AccessFlag.isPrivate(finfo.getAccessFlags()) && f
/*  980 */       .getDeclaringClass() != this.thisClass) {
/*  981 */       CtClass declClass = f.getDeclaringClass();
/*  982 */       if (isEnclosing(declClass, this.thisClass)) {
/*  983 */         AccessorMaker maker = declClass.getAccessorMaker();
/*  984 */         if (maker != null)
/*  985 */           return maker; 
/*      */       } 
/*  987 */       throw new CompileError("Field " + f.getName() + " in " + declClass
/*  988 */           .getName() + " is private.");
/*      */     } 
/*      */     
/*  991 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean setFieldType(FieldInfo finfo) throws CompileError {
/* 1000 */     String type = finfo.getDescriptor();
/*      */     
/* 1002 */     int i = 0;
/* 1003 */     int dim = 0;
/* 1004 */     char c = type.charAt(i);
/* 1005 */     while (c == '[') {
/* 1006 */       dim++;
/* 1007 */       c = type.charAt(++i);
/*      */     } 
/*      */     
/* 1010 */     this.arrayDim = dim;
/* 1011 */     this.exprType = MemberResolver.descToType(c);
/*      */     
/* 1013 */     if (c == 'L') {
/* 1014 */       this.className = type.substring(i + 1, type.indexOf(';', i + 1));
/*      */     } else {
/* 1016 */       this.className = null;
/*      */     } 
/* 1018 */     boolean is2byte = (dim == 0 && (c == 'J' || c == 'D'));
/* 1019 */     return is2byte;
/*      */   }
/*      */   
/*      */   private int addFieldrefInfo(CtField f, FieldInfo finfo) {
/* 1023 */     ConstPool cp = this.bytecode.getConstPool();
/* 1024 */     String cname = f.getDeclaringClass().getName();
/* 1025 */     int ci = cp.addClassInfo(cname);
/* 1026 */     String name = finfo.getName();
/* 1027 */     String type = finfo.getDescriptor();
/* 1028 */     return cp.addFieldrefInfo(ci, name, type);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void atClassObject2(String cname) throws CompileError {
/* 1033 */     if (getMajorVersion() < 49) {
/* 1034 */       super.atClassObject2(cname);
/*      */     } else {
/* 1036 */       this.bytecode.addLdc(this.bytecode.getConstPool().addClassInfo(cname));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void atFieldPlusPlus(int token, boolean isPost, ASTree oprand, Expr expr, boolean doDup) throws CompileError {
/*      */     int dup_code;
/* 1044 */     CtField f = fieldAccess(oprand, false);
/* 1045 */     boolean is_static = this.resultStatic;
/* 1046 */     if (!is_static) {
/* 1047 */       this.bytecode.addOpcode(89);
/*      */     }
/* 1049 */     int fi = atFieldRead(f, is_static);
/* 1050 */     int t = this.exprType;
/* 1051 */     boolean is2w = is2word(t, this.arrayDim);
/*      */ 
/*      */     
/* 1054 */     if (is_static) {
/* 1055 */       dup_code = is2w ? 92 : 89;
/*      */     } else {
/* 1057 */       dup_code = is2w ? 93 : 90;
/*      */     } 
/* 1059 */     atPlusPlusCore(dup_code, doDup, token, isPost, expr);
/* 1060 */     atFieldAssignCore(f, is_static, fi, is2w);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CtField fieldAccess(ASTree expr, boolean acceptLength) throws CompileError {
/* 1070 */     if (expr instanceof Member) {
/* 1071 */       String name = ((Member)expr).get();
/* 1072 */       CtField f = null;
/*      */       try {
/* 1074 */         f = this.thisClass.getField(name);
/*      */       }
/* 1076 */       catch (NotFoundException e) {
/*      */         
/* 1078 */         throw new NoFieldException(name, expr);
/*      */       } 
/*      */       
/* 1081 */       boolean is_static = Modifier.isStatic(f.getModifiers());
/* 1082 */       if (!is_static) {
/* 1083 */         if (this.inStaticMethod) {
/* 1084 */           throw new CompileError("not available in a static method: " + name);
/*      */         }
/*      */         
/* 1087 */         this.bytecode.addAload(0);
/*      */       } 
/* 1089 */       this.resultStatic = is_static;
/* 1090 */       return f;
/*      */     } 
/* 1092 */     if (expr instanceof Expr) {
/* 1093 */       Expr e = (Expr)expr;
/* 1094 */       int op = e.getOperator();
/* 1095 */       if (op == 35) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1100 */         CtField f = this.resolver.lookupField(((Symbol)e.oprand1()).get(), (Symbol)e
/* 1101 */             .oprand2());
/* 1102 */         this.resultStatic = true;
/* 1103 */         return f;
/*      */       } 
/* 1105 */       if (op == 46) {
/* 1106 */         CtField f = null;
/*      */         try {
/* 1108 */           e.oprand1().accept(this);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1113 */           if (this.exprType == 307 && this.arrayDim == 0)
/* 1114 */           { f = this.resolver.lookupFieldByJvmName(this.className, (Symbol)e
/* 1115 */                 .oprand2()); }
/* 1116 */           else { if (acceptLength && this.arrayDim > 0 && ((Symbol)e
/* 1117 */               .oprand2()).get().equals("length")) {
/* 1118 */               return null;
/*      */             }
/* 1120 */             badLvalue(); }
/*      */           
/* 1122 */           boolean is_static = Modifier.isStatic(f.getModifiers());
/* 1123 */           if (is_static) {
/* 1124 */             this.bytecode.addOpcode(87);
/*      */           }
/* 1126 */           this.resultStatic = is_static;
/* 1127 */           return f;
/*      */         }
/* 1129 */         catch (NoFieldException nfe) {
/* 1130 */           if (nfe.getExpr() != e.oprand1()) {
/* 1131 */             throw nfe;
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1137 */           Symbol fname = (Symbol)e.oprand2();
/* 1138 */           String cname = nfe.getField();
/* 1139 */           f = this.resolver.lookupFieldByJvmName2(cname, fname, expr);
/* 1140 */           this.resultStatic = true;
/* 1141 */           return f;
/*      */         } 
/*      */       } 
/*      */       
/* 1145 */       badLvalue();
/*      */     } else {
/*      */       
/* 1148 */       badLvalue();
/*      */     } 
/* 1150 */     this.resultStatic = false;
/* 1151 */     return null;
/*      */   }
/*      */   
/*      */   private static void badLvalue() throws CompileError {
/* 1155 */     throw new CompileError("bad l-value");
/*      */   }
/*      */   
/*      */   public CtClass[] makeParamList(MethodDecl md) throws CompileError {
/*      */     CtClass[] params;
/* 1160 */     ASTList plist = md.getParams();
/* 1161 */     if (plist == null) {
/* 1162 */       params = new CtClass[0];
/*      */     } else {
/* 1164 */       int i = 0;
/* 1165 */       params = new CtClass[plist.length()];
/* 1166 */       while (plist != null) {
/* 1167 */         params[i++] = this.resolver.lookupClass((Declarator)plist.head());
/* 1168 */         plist = plist.tail();
/*      */       } 
/*      */     } 
/*      */     
/* 1172 */     return params;
/*      */   }
/*      */ 
/*      */   
/*      */   public CtClass[] makeThrowsList(MethodDecl md) throws CompileError {
/* 1177 */     ASTList list = md.getThrows();
/* 1178 */     if (list == null)
/* 1179 */       return null; 
/* 1180 */     int i = 0;
/* 1181 */     CtClass[] clist = new CtClass[list.length()];
/* 1182 */     while (list != null) {
/* 1183 */       clist[i++] = this.resolver.lookupClassByName((ASTList)list.head());
/* 1184 */       list = list.tail();
/*      */     } 
/*      */     
/* 1187 */     return clist;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String resolveClassName(ASTList name) throws CompileError {
/* 1197 */     return this.resolver.resolveClassName(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String resolveClassName(String jvmName) throws CompileError {
/* 1205 */     return this.resolver.resolveJvmClassName(jvmName);
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\MemberCodeGen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */