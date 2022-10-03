/*      */ package javassist.compiler;
/*      */ 
/*      */ import javassist.ClassPool;
/*      */ import javassist.CtClass;
/*      */ import javassist.CtField;
/*      */ import javassist.Modifier;
/*      */ import javassist.NotFoundException;
/*      */ import javassist.bytecode.FieldInfo;
/*      */ import javassist.bytecode.MethodInfo;
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
/*      */ import javassist.compiler.ast.InstanceOfExpr;
/*      */ import javassist.compiler.ast.IntConst;
/*      */ import javassist.compiler.ast.Keyword;
/*      */ import javassist.compiler.ast.Member;
/*      */ import javassist.compiler.ast.NewExpr;
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
/*      */ public class TypeChecker
/*      */   extends Visitor
/*      */   implements Opcode, TokenId
/*      */ {
/*      */   static final String javaLangObject = "java.lang.Object";
/*      */   static final String jvmJavaLangObject = "java/lang/Object";
/*      */   static final String jvmJavaLangString = "java/lang/String";
/*      */   static final String jvmJavaLangClass = "java/lang/Class";
/*      */   protected int exprType;
/*      */   protected int arrayDim;
/*      */   protected String className;
/*      */   protected MemberResolver resolver;
/*      */   protected CtClass thisClass;
/*      */   protected MethodInfo thisMethod;
/*      */   
/*      */   public TypeChecker(CtClass cc, ClassPool cp) {
/*   66 */     this.resolver = new MemberResolver(cp);
/*   67 */     this.thisClass = cc;
/*   68 */     this.thisMethod = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static String argTypesToString(int[] types, int[] dims, String[] cnames) {
/*   77 */     StringBuffer sbuf = new StringBuffer();
/*   78 */     sbuf.append('(');
/*   79 */     int n = types.length;
/*   80 */     if (n > 0) {
/*   81 */       int i = 0;
/*      */       while (true) {
/*   83 */         typeToString(sbuf, types[i], dims[i], cnames[i]);
/*   84 */         if (++i < n) {
/*   85 */           sbuf.append(',');
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/*      */     } 
/*   91 */     sbuf.append(')');
/*   92 */     return sbuf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static StringBuffer typeToString(StringBuffer sbuf, int type, int dim, String cname) {
/*      */     String s;
/*  102 */     if (type == 307) {
/*  103 */       s = MemberResolver.jvmToJavaName(cname);
/*  104 */     } else if (type == 412) {
/*  105 */       s = "Object";
/*      */     } else {
/*      */       try {
/*  108 */         s = MemberResolver.getTypeName(type);
/*      */       }
/*  110 */       catch (CompileError e) {
/*  111 */         s = "?";
/*      */       } 
/*      */     } 
/*  114 */     sbuf.append(s);
/*  115 */     while (dim-- > 0) {
/*  116 */       sbuf.append("[]");
/*      */     }
/*  118 */     return sbuf;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setThisMethod(MethodInfo m) {
/*  125 */     this.thisMethod = m;
/*      */   }
/*      */   
/*      */   protected static void fatal() throws CompileError {
/*  129 */     throw new CompileError("fatal");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getThisName() {
/*  136 */     return MemberResolver.javaToJvmName(this.thisClass.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getSuperName() throws CompileError {
/*  143 */     return MemberResolver.javaToJvmName(
/*  144 */         MemberResolver.getSuperclass(this.thisClass).getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String resolveClassName(ASTList name) throws CompileError {
/*  153 */     return this.resolver.resolveClassName(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String resolveClassName(String jvmName) throws CompileError {
/*  160 */     return this.resolver.resolveJvmClassName(jvmName);
/*      */   }
/*      */ 
/*      */   
/*      */   public void atNewExpr(NewExpr expr) throws CompileError {
/*  165 */     if (expr.isArray()) {
/*  166 */       atNewArrayExpr(expr);
/*      */     } else {
/*  168 */       CtClass clazz = this.resolver.lookupClassByName(expr.getClassName());
/*  169 */       String cname = clazz.getName();
/*  170 */       ASTList args = expr.getArguments();
/*  171 */       atMethodCallCore(clazz, "<init>", args);
/*  172 */       this.exprType = 307;
/*  173 */       this.arrayDim = 0;
/*  174 */       this.className = MemberResolver.javaToJvmName(cname);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void atNewArrayExpr(NewExpr expr) throws CompileError {
/*  179 */     int type = expr.getArrayType();
/*  180 */     ASTList size = expr.getArraySize();
/*  181 */     ASTList classname = expr.getClassName();
/*  182 */     ArrayInit arrayInit = expr.getInitializer();
/*  183 */     if (arrayInit != null) {
/*  184 */       arrayInit.accept(this);
/*      */     }
/*  186 */     if (size.length() > 1) {
/*  187 */       atMultiNewArray(type, classname, size);
/*      */     } else {
/*  189 */       ASTree sizeExpr = size.head();
/*  190 */       if (sizeExpr != null) {
/*  191 */         sizeExpr.accept(this);
/*      */       }
/*  193 */       this.exprType = type;
/*  194 */       this.arrayDim = 1;
/*  195 */       if (type == 307) {
/*  196 */         this.className = resolveClassName(classname);
/*      */       } else {
/*  198 */         this.className = null;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void atArrayInit(ArrayInit init) throws CompileError {
/*  204 */     ArrayInit arrayInit = init;
/*  205 */     while (arrayInit != null) {
/*  206 */       ASTree h = arrayInit.head();
/*  207 */       ASTList aSTList = arrayInit.tail();
/*  208 */       if (h != null) {
/*  209 */         h.accept(this);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void atMultiNewArray(int type, ASTList classname, ASTList size) throws CompileError {
/*  218 */     int dim = size.length();
/*  219 */     for (int count = 0; size != null; size = size.tail()) {
/*  220 */       ASTree s = size.head();
/*  221 */       if (s == null) {
/*      */         break;
/*      */       }
/*  224 */       count++;
/*  225 */       s.accept(this);
/*      */     } 
/*      */     
/*  228 */     this.exprType = type;
/*  229 */     this.arrayDim = dim;
/*  230 */     if (type == 307) {
/*  231 */       this.className = resolveClassName(classname);
/*      */     } else {
/*  233 */       this.className = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void atAssignExpr(AssignExpr expr) throws CompileError {
/*  239 */     int op = expr.getOperator();
/*  240 */     ASTree left = expr.oprand1();
/*  241 */     ASTree right = expr.oprand2();
/*  242 */     if (left instanceof Variable) {
/*  243 */       atVariableAssign((Expr)expr, op, (Variable)left, ((Variable)left)
/*  244 */           .getDeclarator(), right);
/*      */     } else {
/*      */       
/*  247 */       if (left instanceof Expr) {
/*  248 */         Expr e = (Expr)left;
/*  249 */         if (e.getOperator() == 65) {
/*  250 */           atArrayAssign((Expr)expr, op, (Expr)left, right);
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*  255 */       atFieldAssign((Expr)expr, op, left, right);
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
/*      */   private void atVariableAssign(Expr expr, int op, Variable var, Declarator d, ASTree right) throws CompileError {
/*  267 */     int varType = d.getType();
/*  268 */     int varArray = d.getArrayDim();
/*  269 */     String varClass = d.getClassName();
/*      */     
/*  271 */     if (op != 61) {
/*  272 */       atVariable(var);
/*      */     }
/*  274 */     right.accept(this);
/*  275 */     this.exprType = varType;
/*  276 */     this.arrayDim = varArray;
/*  277 */     this.className = varClass;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void atArrayAssign(Expr expr, int op, Expr array, ASTree right) throws CompileError {
/*  283 */     atArrayRead(array.oprand1(), array.oprand2());
/*  284 */     int aType = this.exprType;
/*  285 */     int aDim = this.arrayDim;
/*  286 */     String cname = this.className;
/*  287 */     right.accept(this);
/*  288 */     this.exprType = aType;
/*  289 */     this.arrayDim = aDim;
/*  290 */     this.className = cname;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void atFieldAssign(Expr expr, int op, ASTree left, ASTree right) throws CompileError {
/*  296 */     CtField f = fieldAccess(left);
/*  297 */     atFieldRead(f);
/*  298 */     int fType = this.exprType;
/*  299 */     int fDim = this.arrayDim;
/*  300 */     String cname = this.className;
/*  301 */     right.accept(this);
/*  302 */     this.exprType = fType;
/*  303 */     this.arrayDim = fDim;
/*  304 */     this.className = cname;
/*      */   }
/*      */ 
/*      */   
/*      */   public void atCondExpr(CondExpr expr) throws CompileError {
/*  309 */     booleanExpr(expr.condExpr());
/*  310 */     expr.thenExpr().accept(this);
/*  311 */     int type1 = this.exprType;
/*  312 */     int dim1 = this.arrayDim;
/*      */     
/*  314 */     String cname1 = this.className;
/*  315 */     expr.elseExpr().accept(this);
/*      */     
/*  317 */     if (dim1 == 0 && dim1 == this.arrayDim) {
/*  318 */       if (CodeGen.rightIsStrong(type1, this.exprType)) {
/*  319 */         expr.setThen((ASTree)new CastExpr(this.exprType, 0, expr.thenExpr()));
/*  320 */       } else if (CodeGen.rightIsStrong(this.exprType, type1)) {
/*  321 */         expr.setElse((ASTree)new CastExpr(type1, 0, expr.elseExpr()));
/*  322 */         this.exprType = type1;
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
/*      */   public void atBinExpr(BinExpr expr) throws CompileError {
/*  334 */     int token = expr.getOperator();
/*  335 */     int k = CodeGen.lookupBinOp(token);
/*  336 */     if (k >= 0) {
/*      */ 
/*      */       
/*  339 */       if (token == 43) {
/*  340 */         Expr e = atPlusExpr(expr);
/*  341 */         if (e != null) {
/*      */ 
/*      */ 
/*      */           
/*  345 */           CallExpr callExpr = CallExpr.makeCall((ASTree)Expr.make(46, (ASTree)e, (ASTree)new Member("toString")), null);
/*      */           
/*  347 */           expr.setOprand1((ASTree)callExpr);
/*  348 */           expr.setOprand2(null);
/*  349 */           this.className = "java/lang/String";
/*      */         } 
/*      */       } else {
/*      */         
/*  353 */         ASTree left = expr.oprand1();
/*  354 */         ASTree right = expr.oprand2();
/*  355 */         left.accept(this);
/*  356 */         int type1 = this.exprType;
/*  357 */         right.accept(this);
/*  358 */         if (!isConstant(expr, token, left, right)) {
/*  359 */           computeBinExprType(expr, token, type1);
/*      */         }
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  365 */       booleanExpr((ASTree)expr);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Expr atPlusExpr(BinExpr expr) throws CompileError {
/*  374 */     ASTree left = expr.oprand1();
/*  375 */     ASTree right = expr.oprand2();
/*  376 */     if (right == null) {
/*      */ 
/*      */       
/*  379 */       left.accept(this);
/*  380 */       return null;
/*      */     } 
/*      */     
/*  383 */     if (isPlusExpr(left)) {
/*  384 */       Expr newExpr = atPlusExpr((BinExpr)left);
/*  385 */       if (newExpr != null) {
/*  386 */         right.accept(this);
/*  387 */         this.exprType = 307;
/*  388 */         this.arrayDim = 0;
/*  389 */         this.className = "java/lang/StringBuffer";
/*  390 */         return makeAppendCall((ASTree)newExpr, right);
/*      */       } 
/*      */     } else {
/*      */       
/*  394 */       left.accept(this);
/*      */     } 
/*  396 */     int type1 = this.exprType;
/*  397 */     int dim1 = this.arrayDim;
/*  398 */     String cname = this.className;
/*  399 */     right.accept(this);
/*      */     
/*  401 */     if (isConstant(expr, 43, left, right)) {
/*  402 */       return null;
/*      */     }
/*  404 */     if ((type1 == 307 && dim1 == 0 && "java/lang/String".equals(cname)) || (this.exprType == 307 && this.arrayDim == 0 && "java/lang/String"
/*      */       
/*  406 */       .equals(this.className))) {
/*  407 */       ASTList sbufClass = ASTList.make((ASTree)new Symbol("java"), (ASTree)new Symbol("lang"), (ASTree)new Symbol("StringBuffer"));
/*      */       
/*  409 */       NewExpr newExpr = new NewExpr(sbufClass, null);
/*  410 */       this.exprType = 307;
/*  411 */       this.arrayDim = 0;
/*  412 */       this.className = "java/lang/StringBuffer";
/*  413 */       return makeAppendCall((ASTree)makeAppendCall((ASTree)newExpr, left), right);
/*      */     } 
/*  415 */     computeBinExprType(expr, 43, type1);
/*  416 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isConstant(BinExpr expr, int op, ASTree left, ASTree right) throws CompileError {
/*  422 */     left = stripPlusExpr(left);
/*  423 */     right = stripPlusExpr(right);
/*  424 */     ASTree newExpr = null;
/*  425 */     if (left instanceof StringL && right instanceof StringL && op == 43) {
/*      */       
/*  427 */       StringL stringL = new StringL(((StringL)left).get() + ((StringL)right).get());
/*  428 */     } else if (left instanceof IntConst) {
/*  429 */       newExpr = ((IntConst)left).compute(op, right);
/*  430 */     } else if (left instanceof DoubleConst) {
/*  431 */       newExpr = ((DoubleConst)left).compute(op, right);
/*      */     } 
/*  433 */     if (newExpr == null)
/*  434 */       return false; 
/*  435 */     expr.setOperator(43);
/*  436 */     expr.setOprand1(newExpr);
/*  437 */     expr.setOprand2(null);
/*  438 */     newExpr.accept(this);
/*  439 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static ASTree stripPlusExpr(ASTree expr) {
/*  445 */     if (expr instanceof BinExpr) {
/*  446 */       BinExpr e = (BinExpr)expr;
/*  447 */       if (e.getOperator() == 43 && e.oprand2() == null) {
/*  448 */         return e.getLeft();
/*      */       }
/*  450 */     } else if (expr instanceof Expr) {
/*  451 */       Expr e = (Expr)expr;
/*  452 */       int op = e.getOperator();
/*  453 */       if (op == 35) {
/*  454 */         ASTree cexpr = getConstantFieldValue((Member)e.oprand2());
/*  455 */         if (cexpr != null) {
/*  456 */           return cexpr;
/*      */         }
/*  458 */       } else if (op == 43 && e.getRight() == null) {
/*  459 */         return e.getLeft();
/*      */       } 
/*  461 */     } else if (expr instanceof Member) {
/*  462 */       ASTree cexpr = getConstantFieldValue((Member)expr);
/*  463 */       if (cexpr != null) {
/*  464 */         return cexpr;
/*      */       }
/*      */     } 
/*  467 */     return expr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static ASTree getConstantFieldValue(Member mem) {
/*  475 */     return getConstantFieldValue(mem.getField());
/*      */   }
/*      */   
/*      */   public static ASTree getConstantFieldValue(CtField f) {
/*  479 */     if (f == null) {
/*  480 */       return null;
/*      */     }
/*  482 */     Object value = f.getConstantValue();
/*  483 */     if (value == null) {
/*  484 */       return null;
/*      */     }
/*  486 */     if (value instanceof String)
/*  487 */       return (ASTree)new StringL((String)value); 
/*  488 */     if (value instanceof Double || value instanceof Float) {
/*      */       
/*  490 */       int token = (value instanceof Double) ? 405 : 404;
/*  491 */       return (ASTree)new DoubleConst(((Number)value).doubleValue(), token);
/*      */     } 
/*  493 */     if (value instanceof Number) {
/*  494 */       int token = (value instanceof Long) ? 403 : 402;
/*  495 */       return (ASTree)new IntConst(((Number)value).longValue(), token);
/*      */     } 
/*  497 */     if (value instanceof Boolean) {
/*  498 */       return (ASTree)new Keyword(((Boolean)value).booleanValue() ? 
/*  499 */           410 : 411);
/*      */     }
/*  501 */     return null;
/*      */   }
/*      */   
/*      */   private static boolean isPlusExpr(ASTree expr) {
/*  505 */     if (expr instanceof BinExpr) {
/*  506 */       BinExpr bexpr = (BinExpr)expr;
/*  507 */       int token = bexpr.getOperator();
/*  508 */       return (token == 43);
/*      */     } 
/*      */     
/*  511 */     return false;
/*      */   }
/*      */   
/*      */   private static Expr makeAppendCall(ASTree target, ASTree arg) {
/*  515 */     return (Expr)CallExpr.makeCall((ASTree)Expr.make(46, target, (ASTree)new Member("append")), (ASTree)new ASTList(arg));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void computeBinExprType(BinExpr expr, int token, int type1) throws CompileError {
/*  523 */     int type2 = this.exprType;
/*  524 */     if (token == 364 || token == 366 || token == 370) {
/*  525 */       this.exprType = type1;
/*      */     } else {
/*  527 */       insertCast(expr, type1, type2);
/*      */     } 
/*  529 */     if (CodeGen.isP_INT(this.exprType) && this.exprType != 301) {
/*  530 */       this.exprType = 324;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void booleanExpr(ASTree expr) throws CompileError {
/*  536 */     int op = CodeGen.getCompOperator(expr);
/*  537 */     if (op == 358) {
/*  538 */       BinExpr bexpr = (BinExpr)expr;
/*  539 */       bexpr.oprand1().accept(this);
/*  540 */       int type1 = this.exprType;
/*  541 */       int dim1 = this.arrayDim;
/*  542 */       bexpr.oprand2().accept(this);
/*  543 */       if (dim1 == 0 && this.arrayDim == 0) {
/*  544 */         insertCast(bexpr, type1, this.exprType);
/*      */       }
/*  546 */     } else if (op == 33) {
/*  547 */       ((Expr)expr).oprand1().accept(this);
/*  548 */     } else if (op == 369 || op == 368) {
/*  549 */       BinExpr bexpr = (BinExpr)expr;
/*  550 */       bexpr.oprand1().accept(this);
/*  551 */       bexpr.oprand2().accept(this);
/*      */     } else {
/*      */       
/*  554 */       expr.accept(this);
/*      */     } 
/*  556 */     this.exprType = 301;
/*  557 */     this.arrayDim = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void insertCast(BinExpr expr, int type1, int type2) throws CompileError {
/*  563 */     if (CodeGen.rightIsStrong(type1, type2)) {
/*  564 */       expr.setLeft((ASTree)new CastExpr(type2, 0, expr.oprand1()));
/*      */     } else {
/*  566 */       this.exprType = type1;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void atCastExpr(CastExpr expr) throws CompileError {
/*  571 */     String cname = resolveClassName(expr.getClassName());
/*  572 */     expr.getOprand().accept(this);
/*  573 */     this.exprType = expr.getType();
/*  574 */     this.arrayDim = expr.getArrayDim();
/*  575 */     this.className = cname;
/*      */   }
/*      */ 
/*      */   
/*      */   public void atInstanceOfExpr(InstanceOfExpr expr) throws CompileError {
/*  580 */     expr.getOprand().accept(this);
/*  581 */     this.exprType = 301;
/*  582 */     this.arrayDim = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void atExpr(Expr expr) throws CompileError {
/*  590 */     int token = expr.getOperator();
/*  591 */     ASTree oprand = expr.oprand1();
/*  592 */     if (token == 46) {
/*  593 */       String member = ((Symbol)expr.oprand2()).get();
/*  594 */       if (member.equals("length")) {
/*      */         try {
/*  596 */           atArrayLength(expr);
/*      */         }
/*  598 */         catch (NoFieldException nfe) {
/*      */           
/*  600 */           atFieldRead((ASTree)expr);
/*      */         } 
/*  602 */       } else if (member.equals("class")) {
/*  603 */         atClassObject(expr);
/*      */       } else {
/*  605 */         atFieldRead((ASTree)expr);
/*      */       } 
/*  607 */     } else if (token == 35) {
/*  608 */       String member = ((Symbol)expr.oprand2()).get();
/*  609 */       if (member.equals("class")) {
/*  610 */         atClassObject(expr);
/*      */       } else {
/*  612 */         atFieldRead((ASTree)expr);
/*      */       } 
/*  614 */     } else if (token == 65) {
/*  615 */       atArrayRead(oprand, expr.oprand2());
/*  616 */     } else if (token == 362 || token == 363) {
/*  617 */       atPlusPlus(token, oprand, expr);
/*  618 */     } else if (token == 33) {
/*  619 */       booleanExpr((ASTree)expr);
/*  620 */     } else if (token == 67) {
/*  621 */       fatal();
/*      */     } else {
/*  623 */       oprand.accept(this);
/*  624 */       if (!isConstant(expr, token, oprand) && (
/*  625 */         token == 45 || token == 126) && 
/*  626 */         CodeGen.isP_INT(this.exprType))
/*  627 */         this.exprType = 324; 
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean isConstant(Expr expr, int op, ASTree oprand) {
/*  632 */     oprand = stripPlusExpr(oprand);
/*  633 */     if (oprand instanceof IntConst) {
/*  634 */       IntConst c = (IntConst)oprand;
/*  635 */       long v = c.get();
/*  636 */       if (op == 45) {
/*  637 */         v = -v;
/*  638 */       } else if (op == 126) {
/*  639 */         v ^= 0xFFFFFFFFFFFFFFFFL;
/*      */       } else {
/*  641 */         return false;
/*      */       } 
/*  643 */       c.set(v);
/*      */     }
/*  645 */     else if (oprand instanceof DoubleConst) {
/*  646 */       DoubleConst c = (DoubleConst)oprand;
/*  647 */       if (op == 45) {
/*  648 */         c.set(-c.get());
/*      */       } else {
/*  650 */         return false;
/*      */       } 
/*      */     } else {
/*  653 */       return false;
/*      */     } 
/*  655 */     expr.setOperator(43);
/*  656 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void atCallExpr(CallExpr expr) throws CompileError {
/*  661 */     String mname = null;
/*  662 */     CtClass targetClass = null;
/*  663 */     ASTree method = expr.oprand1();
/*  664 */     ASTList args = (ASTList)expr.oprand2();
/*      */     
/*  666 */     if (method instanceof Member) {
/*  667 */       mname = ((Member)method).get();
/*  668 */       targetClass = this.thisClass;
/*      */     }
/*  670 */     else if (method instanceof Keyword) {
/*  671 */       mname = "<init>";
/*  672 */       if (((Keyword)method).get() == 336) {
/*  673 */         targetClass = MemberResolver.getSuperclass(this.thisClass);
/*      */       } else {
/*  675 */         targetClass = this.thisClass;
/*      */       } 
/*  677 */     } else if (method instanceof Expr) {
/*  678 */       Expr e = (Expr)method;
/*  679 */       mname = ((Symbol)e.oprand2()).get();
/*  680 */       int op = e.getOperator();
/*  681 */       if (op == 35) {
/*      */         
/*  683 */         targetClass = this.resolver.lookupClass(((Symbol)e.oprand1()).get(), false);
/*      */       }
/*  685 */       else if (op == 46) {
/*  686 */         ASTree target = e.oprand1();
/*  687 */         String classFollowedByDotSuper = isDotSuper(target);
/*  688 */         if (classFollowedByDotSuper != null) {
/*  689 */           targetClass = MemberResolver.getSuperInterface(this.thisClass, classFollowedByDotSuper);
/*      */         } else {
/*      */           
/*      */           try {
/*  693 */             target.accept(this);
/*      */           }
/*  695 */           catch (NoFieldException nfe) {
/*  696 */             if (nfe.getExpr() != target) {
/*  697 */               throw nfe;
/*      */             }
/*      */             
/*  700 */             this.exprType = 307;
/*  701 */             this.arrayDim = 0;
/*  702 */             this.className = nfe.getField();
/*  703 */             e.setOperator(35);
/*  704 */             e.setOprand1((ASTree)new Symbol(MemberResolver.jvmToJavaName(this.className)));
/*      */           } 
/*      */ 
/*      */           
/*  708 */           if (this.arrayDim > 0) {
/*  709 */             targetClass = this.resolver.lookupClass("java.lang.Object", true);
/*  710 */           } else if (this.exprType == 307) {
/*  711 */             targetClass = this.resolver.lookupClassByJvmName(this.className);
/*      */           } else {
/*  713 */             badMethod();
/*      */           } 
/*      */         } 
/*      */       } else {
/*  717 */         badMethod();
/*      */       } 
/*      */     } else {
/*  720 */       fatal();
/*      */     } 
/*      */     
/*  723 */     MemberResolver.Method minfo = atMethodCallCore(targetClass, mname, args);
/*  724 */     expr.setMethod(minfo);
/*      */   }
/*      */   
/*      */   private static void badMethod() throws CompileError {
/*  728 */     throw new CompileError("bad method");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String isDotSuper(ASTree target) {
/*  739 */     if (target instanceof Expr) {
/*  740 */       Expr e = (Expr)target;
/*  741 */       if (e.getOperator() == 46) {
/*  742 */         ASTree right = e.oprand2();
/*  743 */         if (right instanceof Keyword && ((Keyword)right).get() == 336) {
/*  744 */           return ((Symbol)e.oprand1()).get();
/*      */         }
/*      */       } 
/*      */     } 
/*  748 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MemberResolver.Method atMethodCallCore(CtClass targetClass, String mname, ASTList args) throws CompileError {
/*  759 */     int nargs = getMethodArgsLength(args);
/*  760 */     int[] types = new int[nargs];
/*  761 */     int[] dims = new int[nargs];
/*  762 */     String[] cnames = new String[nargs];
/*  763 */     atMethodArgs(args, types, dims, cnames);
/*      */ 
/*      */     
/*  766 */     MemberResolver.Method found = this.resolver.lookupMethod(targetClass, this.thisClass, this.thisMethod, mname, types, dims, cnames);
/*      */     
/*  768 */     if (found == null) {
/*  769 */       String msg, clazz = targetClass.getName();
/*  770 */       String signature = argTypesToString(types, dims, cnames);
/*      */       
/*  772 */       if (mname.equals("<init>")) {
/*  773 */         msg = "cannot find constructor " + clazz + signature;
/*      */       } else {
/*  775 */         msg = mname + signature + " not found in " + clazz;
/*      */       } 
/*  777 */       throw new CompileError(msg);
/*      */     } 
/*      */     
/*  780 */     String desc = found.info.getDescriptor();
/*  781 */     setReturnType(desc);
/*  782 */     return found;
/*      */   }
/*      */   
/*      */   public int getMethodArgsLength(ASTList args) {
/*  786 */     return ASTList.length(args);
/*      */   }
/*      */ 
/*      */   
/*      */   public void atMethodArgs(ASTList args, int[] types, int[] dims, String[] cnames) throws CompileError {
/*  791 */     int i = 0;
/*  792 */     while (args != null) {
/*  793 */       ASTree a = args.head();
/*  794 */       a.accept(this);
/*  795 */       types[i] = this.exprType;
/*  796 */       dims[i] = this.arrayDim;
/*  797 */       cnames[i] = this.className;
/*  798 */       i++;
/*  799 */       args = args.tail();
/*      */     } 
/*      */   }
/*      */   
/*      */   void setReturnType(String desc) throws CompileError {
/*  804 */     int i = desc.indexOf(')');
/*  805 */     if (i < 0) {
/*  806 */       badMethod();
/*      */     }
/*  808 */     char c = desc.charAt(++i);
/*  809 */     int dim = 0;
/*  810 */     while (c == '[') {
/*  811 */       dim++;
/*  812 */       c = desc.charAt(++i);
/*      */     } 
/*      */     
/*  815 */     this.arrayDim = dim;
/*  816 */     if (c == 'L') {
/*  817 */       int j = desc.indexOf(';', i + 1);
/*  818 */       if (j < 0) {
/*  819 */         badMethod();
/*      */       }
/*  821 */       this.exprType = 307;
/*  822 */       this.className = desc.substring(i + 1, j);
/*      */     } else {
/*      */       
/*  825 */       this.exprType = MemberResolver.descToType(c);
/*  826 */       this.className = null;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void atFieldRead(ASTree expr) throws CompileError {
/*  831 */     atFieldRead(fieldAccess(expr));
/*      */   }
/*      */   
/*      */   private void atFieldRead(CtField f) throws CompileError {
/*  835 */     FieldInfo finfo = f.getFieldInfo2();
/*  836 */     String type = finfo.getDescriptor();
/*      */     
/*  838 */     int i = 0;
/*  839 */     int dim = 0;
/*  840 */     char c = type.charAt(i);
/*  841 */     while (c == '[') {
/*  842 */       dim++;
/*  843 */       c = type.charAt(++i);
/*      */     } 
/*      */     
/*  846 */     this.arrayDim = dim;
/*  847 */     this.exprType = MemberResolver.descToType(c);
/*      */     
/*  849 */     if (c == 'L') {
/*  850 */       this.className = type.substring(i + 1, type.indexOf(';', i + 1));
/*      */     } else {
/*  852 */       this.className = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected CtField fieldAccess(ASTree expr) throws CompileError {
/*  861 */     if (expr instanceof Member) {
/*  862 */       Member mem = (Member)expr;
/*  863 */       String name = mem.get();
/*      */       try {
/*  865 */         CtField f = this.thisClass.getField(name);
/*  866 */         if (Modifier.isStatic(f.getModifiers())) {
/*  867 */           mem.setField(f);
/*      */         }
/*  869 */         return f;
/*      */       }
/*  871 */       catch (NotFoundException e) {
/*      */         
/*  873 */         throw new NoFieldException(name, expr);
/*      */       } 
/*      */     } 
/*  876 */     if (expr instanceof Expr) {
/*  877 */       Expr e = (Expr)expr;
/*  878 */       int op = e.getOperator();
/*  879 */       if (op == 35) {
/*  880 */         Member mem = (Member)e.oprand2();
/*      */         
/*  882 */         CtField f = this.resolver.lookupField(((Symbol)e.oprand1()).get(), (Symbol)mem);
/*  883 */         mem.setField(f);
/*  884 */         return f;
/*      */       } 
/*  886 */       if (op == 46) {
/*      */         try {
/*  888 */           e.oprand1().accept(this);
/*      */         }
/*  890 */         catch (NoFieldException nfe) {
/*  891 */           if (nfe.getExpr() != e.oprand1()) {
/*  892 */             throw nfe;
/*      */           }
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  898 */           return fieldAccess2(e, nfe.getField());
/*      */         } 
/*      */         
/*  901 */         CompileError err = null;
/*      */         try {
/*  903 */           if (this.exprType == 307 && this.arrayDim == 0) {
/*  904 */             return this.resolver.lookupFieldByJvmName(this.className, (Symbol)e
/*  905 */                 .oprand2());
/*      */           }
/*  907 */         } catch (CompileError ce) {
/*  908 */           err = ce;
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  927 */         ASTree oprnd1 = e.oprand1();
/*  928 */         if (oprnd1 instanceof Symbol) {
/*  929 */           return fieldAccess2(e, ((Symbol)oprnd1).get());
/*      */         }
/*  931 */         if (err != null) {
/*  932 */           throw err;
/*      */         }
/*      */       } 
/*      */     } 
/*  936 */     throw new CompileError("bad field access");
/*      */   }
/*      */   
/*      */   private CtField fieldAccess2(Expr e, String jvmClassName) throws CompileError {
/*  940 */     Member fname = (Member)e.oprand2();
/*  941 */     CtField f = this.resolver.lookupFieldByJvmName2(jvmClassName, (Symbol)fname, (ASTree)e);
/*  942 */     e.setOperator(35);
/*  943 */     e.setOprand1((ASTree)new Symbol(MemberResolver.jvmToJavaName(jvmClassName)));
/*  944 */     fname.setField(f);
/*  945 */     return f;
/*      */   }
/*      */   
/*      */   public void atClassObject(Expr expr) throws CompileError {
/*  949 */     this.exprType = 307;
/*  950 */     this.arrayDim = 0;
/*  951 */     this.className = "java/lang/Class";
/*      */   }
/*      */   
/*      */   public void atArrayLength(Expr expr) throws CompileError {
/*  955 */     expr.oprand1().accept(this);
/*  956 */     if (this.arrayDim == 0) {
/*  957 */       throw new NoFieldException("length", expr);
/*      */     }
/*  959 */     this.exprType = 324;
/*  960 */     this.arrayDim = 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void atArrayRead(ASTree array, ASTree index) throws CompileError {
/*  966 */     array.accept(this);
/*  967 */     int type = this.exprType;
/*  968 */     int dim = this.arrayDim;
/*  969 */     String cname = this.className;
/*  970 */     index.accept(this);
/*  971 */     this.exprType = type;
/*  972 */     this.arrayDim = dim - 1;
/*  973 */     this.className = cname;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void atPlusPlus(int token, ASTree oprand, Expr expr) throws CompileError {
/*  979 */     boolean isPost = (oprand == null);
/*  980 */     if (isPost) {
/*  981 */       oprand = expr.oprand2();
/*      */     }
/*  983 */     if (oprand instanceof Variable) {
/*  984 */       Declarator d = ((Variable)oprand).getDeclarator();
/*  985 */       this.exprType = d.getType();
/*  986 */       this.arrayDim = d.getArrayDim();
/*      */     } else {
/*      */       
/*  989 */       if (oprand instanceof Expr) {
/*  990 */         Expr e = (Expr)oprand;
/*  991 */         if (e.getOperator() == 65) {
/*  992 */           atArrayRead(e.oprand1(), e.oprand2());
/*      */           
/*  994 */           int t = this.exprType;
/*  995 */           if (t == 324 || t == 303 || t == 306 || t == 334) {
/*  996 */             this.exprType = 324;
/*      */           }
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/* 1002 */       atFieldPlusPlus(oprand);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void atFieldPlusPlus(ASTree oprand) throws CompileError {
/* 1008 */     CtField f = fieldAccess(oprand);
/* 1009 */     atFieldRead(f);
/* 1010 */     int t = this.exprType;
/* 1011 */     if (t == 324 || t == 303 || t == 306 || t == 334) {
/* 1012 */       this.exprType = 324;
/*      */     }
/*      */   }
/*      */   
/*      */   public void atMember(Member mem) throws CompileError {
/* 1017 */     atFieldRead((ASTree)mem);
/*      */   }
/*      */ 
/*      */   
/*      */   public void atVariable(Variable v) throws CompileError {
/* 1022 */     Declarator d = v.getDeclarator();
/* 1023 */     this.exprType = d.getType();
/* 1024 */     this.arrayDim = d.getArrayDim();
/* 1025 */     this.className = d.getClassName();
/*      */   }
/*      */ 
/*      */   
/*      */   public void atKeyword(Keyword k) throws CompileError {
/* 1030 */     this.arrayDim = 0;
/* 1031 */     int token = k.get();
/* 1032 */     switch (token) {
/*      */       case 410:
/*      */       case 411:
/* 1035 */         this.exprType = 301;
/*      */         return;
/*      */       case 412:
/* 1038 */         this.exprType = 412;
/*      */         return;
/*      */       case 336:
/*      */       case 339:
/* 1042 */         this.exprType = 307;
/* 1043 */         if (token == 339) {
/* 1044 */           this.className = getThisName();
/*      */         } else {
/* 1046 */           this.className = getSuperName();
/*      */         }  return;
/*      */     } 
/* 1049 */     fatal();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void atStringL(StringL s) throws CompileError {
/* 1055 */     this.exprType = 307;
/* 1056 */     this.arrayDim = 0;
/* 1057 */     this.className = "java/lang/String";
/*      */   }
/*      */ 
/*      */   
/*      */   public void atIntConst(IntConst i) throws CompileError {
/* 1062 */     this.arrayDim = 0;
/* 1063 */     int type = i.getType();
/* 1064 */     if (type == 402 || type == 401) {
/* 1065 */       this.exprType = (type == 402) ? 324 : 306;
/*      */     } else {
/* 1067 */       this.exprType = 326;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void atDoubleConst(DoubleConst d) throws CompileError {
/* 1072 */     this.arrayDim = 0;
/* 1073 */     if (d.getType() == 405) {
/* 1074 */       this.exprType = 312;
/*      */     } else {
/* 1076 */       this.exprType = 317;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\TypeChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */