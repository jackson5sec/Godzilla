/*     */ package javassist.compiler;
/*     */ 
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtPrimitiveType;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.Descriptor;
/*     */ import javassist.compiler.ast.ASTList;
/*     */ import javassist.compiler.ast.ASTree;
/*     */ import javassist.compiler.ast.CallExpr;
/*     */ import javassist.compiler.ast.CastExpr;
/*     */ import javassist.compiler.ast.Declarator;
/*     */ import javassist.compiler.ast.Expr;
/*     */ import javassist.compiler.ast.Member;
/*     */ import javassist.compiler.ast.Stmnt;
/*     */ import javassist.compiler.ast.Symbol;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JvstCodeGen
/*     */   extends MemberCodeGen
/*     */ {
/*  39 */   String paramArrayName = null;
/*  40 */   String paramListName = null;
/*  41 */   CtClass[] paramTypeList = null;
/*  42 */   private int paramVarBase = 0;
/*     */   private boolean useParam0 = false;
/*  44 */   private String param0Type = null;
/*     */   public static final String sigName = "$sig";
/*     */   public static final String dollarTypeName = "$type";
/*     */   public static final String clazzName = "$class";
/*  48 */   private CtClass dollarType = null;
/*  49 */   CtClass returnType = null;
/*  50 */   String returnCastName = null;
/*  51 */   private String returnVarName = null;
/*     */   
/*     */   public static final String wrapperCastName = "$w";
/*  54 */   String proceedName = null;
/*     */   public static final String cflowName = "$cflow";
/*  56 */   ProceedHandler procHandler = null;
/*     */   
/*     */   public JvstCodeGen(Bytecode b, CtClass cc, ClassPool cp) {
/*  59 */     super(b, cc, cp);
/*  60 */     setTypeChecker(new JvstTypeChecker(cc, cp, this));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int indexOfParam1() {
/*  66 */     return this.paramVarBase + (this.useParam0 ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProceedHandler(ProceedHandler h, String name) {
/*  75 */     this.proceedName = name;
/*  76 */     this.procHandler = h;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNullIfVoid() {
/*  83 */     if (this.exprType == 344) {
/*  84 */       this.bytecode.addOpcode(1);
/*  85 */       this.exprType = 307;
/*  86 */       this.arrayDim = 0;
/*  87 */       this.className = "java/lang/Object";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void atMember(Member mem) throws CompileError {
/*  96 */     String name = mem.get();
/*  97 */     if (name.equals(this.paramArrayName)) {
/*  98 */       compileParameterList(this.bytecode, this.paramTypeList, indexOfParam1());
/*  99 */       this.exprType = 307;
/* 100 */       this.arrayDim = 1;
/* 101 */       this.className = "java/lang/Object";
/*     */     }
/* 103 */     else if (name.equals("$sig")) {
/* 104 */       this.bytecode.addLdc(Descriptor.ofMethod(this.returnType, this.paramTypeList));
/* 105 */       this.bytecode.addInvokestatic("javassist/runtime/Desc", "getParams", "(Ljava/lang/String;)[Ljava/lang/Class;");
/*     */       
/* 107 */       this.exprType = 307;
/* 108 */       this.arrayDim = 1;
/* 109 */       this.className = "java/lang/Class";
/*     */     }
/* 111 */     else if (name.equals("$type")) {
/* 112 */       if (this.dollarType == null) {
/* 113 */         throw new CompileError("$type is not available");
/*     */       }
/* 115 */       this.bytecode.addLdc(Descriptor.of(this.dollarType));
/* 116 */       callGetType("getType");
/*     */     }
/* 118 */     else if (name.equals("$class")) {
/* 119 */       if (this.param0Type == null) {
/* 120 */         throw new CompileError("$class is not available");
/*     */       }
/* 122 */       this.bytecode.addLdc(this.param0Type);
/* 123 */       callGetType("getClazz");
/*     */     } else {
/*     */       
/* 126 */       super.atMember(mem);
/*     */     } 
/*     */   }
/*     */   private void callGetType(String method) {
/* 130 */     this.bytecode.addInvokestatic("javassist/runtime/Desc", method, "(Ljava/lang/String;)Ljava/lang/Class;");
/*     */     
/* 132 */     this.exprType = 307;
/* 133 */     this.arrayDim = 0;
/* 134 */     this.className = "java/lang/Class";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void atFieldAssign(Expr expr, int op, ASTree left, ASTree right, boolean doDup) throws CompileError {
/* 141 */     if (left instanceof Member && ((Member)left)
/* 142 */       .get().equals(this.paramArrayName)) {
/* 143 */       if (op != 61) {
/* 144 */         throw new CompileError("bad operator for " + this.paramArrayName);
/*     */       }
/* 146 */       right.accept(this);
/* 147 */       if (this.arrayDim != 1 || this.exprType != 307) {
/* 148 */         throw new CompileError("invalid type for " + this.paramArrayName);
/*     */       }
/* 150 */       atAssignParamList(this.paramTypeList, this.bytecode);
/* 151 */       if (!doDup) {
/* 152 */         this.bytecode.addOpcode(87);
/*     */       }
/*     */     } else {
/* 155 */       super.atFieldAssign(expr, op, left, right, doDup);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void atAssignParamList(CtClass[] params, Bytecode code) throws CompileError {
/* 161 */     if (params == null) {
/*     */       return;
/*     */     }
/* 164 */     int varNo = indexOfParam1();
/* 165 */     int n = params.length;
/* 166 */     for (int i = 0; i < n; i++) {
/* 167 */       code.addOpcode(89);
/* 168 */       code.addIconst(i);
/* 169 */       code.addOpcode(50);
/* 170 */       compileUnwrapValue(params[i], code);
/* 171 */       code.addStore(varNo, params[i]);
/* 172 */       varNo += is2word(this.exprType, this.arrayDim) ? 2 : 1;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void atCastExpr(CastExpr expr) throws CompileError {
/* 178 */     ASTList classname = expr.getClassName();
/* 179 */     if (classname != null && expr.getArrayDim() == 0) {
/* 180 */       ASTree p = classname.head();
/* 181 */       if (p instanceof Symbol && classname.tail() == null) {
/* 182 */         String typename = ((Symbol)p).get();
/* 183 */         if (typename.equals(this.returnCastName)) {
/* 184 */           atCastToRtype(expr);
/*     */           return;
/*     */         } 
/* 187 */         if (typename.equals("$w")) {
/* 188 */           atCastToWrapper(expr);
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/* 194 */     super.atCastExpr(expr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void atCastToRtype(CastExpr expr) throws CompileError {
/* 202 */     expr.getOprand().accept(this);
/* 203 */     if (this.exprType == 344 || isRefType(this.exprType) || this.arrayDim > 0) {
/* 204 */       compileUnwrapValue(this.returnType, this.bytecode);
/* 205 */     } else if (this.returnType instanceof CtPrimitiveType) {
/* 206 */       CtPrimitiveType pt = (CtPrimitiveType)this.returnType;
/* 207 */       int destType = MemberResolver.descToType(pt.getDescriptor());
/* 208 */       atNumCastExpr(this.exprType, destType);
/* 209 */       this.exprType = destType;
/* 210 */       this.arrayDim = 0;
/* 211 */       this.className = null;
/*     */     } else {
/*     */       
/* 214 */       throw new CompileError("invalid cast");
/*     */     } 
/*     */   }
/*     */   protected void atCastToWrapper(CastExpr expr) throws CompileError {
/* 218 */     expr.getOprand().accept(this);
/* 219 */     if (isRefType(this.exprType) || this.arrayDim > 0) {
/*     */       return;
/*     */     }
/* 222 */     CtClass clazz = this.resolver.lookupClass(this.exprType, this.arrayDim, this.className);
/* 223 */     if (clazz instanceof CtPrimitiveType) {
/* 224 */       CtPrimitiveType pt = (CtPrimitiveType)clazz;
/* 225 */       String wrapper = pt.getWrapperName();
/* 226 */       this.bytecode.addNew(wrapper);
/* 227 */       this.bytecode.addOpcode(89);
/* 228 */       if (pt.getDataSize() > 1) {
/* 229 */         this.bytecode.addOpcode(94);
/*     */       } else {
/* 231 */         this.bytecode.addOpcode(93);
/*     */       } 
/* 233 */       this.bytecode.addOpcode(88);
/* 234 */       this.bytecode.addInvokespecial(wrapper, "<init>", "(" + pt
/* 235 */           .getDescriptor() + ")V");
/*     */       
/* 237 */       this.exprType = 307;
/* 238 */       this.arrayDim = 0;
/* 239 */       this.className = "java/lang/Object";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void atCallExpr(CallExpr expr) throws CompileError {
/* 248 */     ASTree method = expr.oprand1();
/* 249 */     if (method instanceof Member) {
/* 250 */       String name = ((Member)method).get();
/* 251 */       if (this.procHandler != null && name.equals(this.proceedName)) {
/* 252 */         this.procHandler.doit(this, this.bytecode, (ASTList)expr.oprand2());
/*     */         return;
/*     */       } 
/* 255 */       if (name.equals("$cflow")) {
/* 256 */         atCflow((ASTList)expr.oprand2());
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 261 */     super.atCallExpr(expr);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void atCflow(ASTList cname) throws CompileError {
/* 267 */     StringBuffer sbuf = new StringBuffer();
/* 268 */     if (cname == null || cname.tail() != null) {
/* 269 */       throw new CompileError("bad $cflow");
/*     */     }
/* 271 */     makeCflowName(sbuf, cname.head());
/* 272 */     String name = sbuf.toString();
/* 273 */     Object[] names = this.resolver.getClassPool().lookupCflow(name);
/* 274 */     if (names == null) {
/* 275 */       throw new CompileError("no such $cflow: " + name);
/*     */     }
/* 277 */     this.bytecode.addGetstatic((String)names[0], (String)names[1], "Ljavassist/runtime/Cflow;");
/*     */     
/* 279 */     this.bytecode.addInvokevirtual("javassist.runtime.Cflow", "value", "()I");
/*     */     
/* 281 */     this.exprType = 324;
/* 282 */     this.arrayDim = 0;
/* 283 */     this.className = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void makeCflowName(StringBuffer sbuf, ASTree name) throws CompileError {
/* 294 */     if (name instanceof Symbol) {
/* 295 */       sbuf.append(((Symbol)name).get());
/*     */       return;
/*     */     } 
/* 298 */     if (name instanceof Expr) {
/* 299 */       Expr expr = (Expr)name;
/* 300 */       if (expr.getOperator() == 46) {
/* 301 */         makeCflowName(sbuf, expr.oprand1());
/* 302 */         sbuf.append('.');
/* 303 */         makeCflowName(sbuf, expr.oprand2());
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 308 */     throw new CompileError("bad $cflow");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isParamListName(ASTList args) {
/* 315 */     if (this.paramTypeList != null && args != null && args
/* 316 */       .tail() == null) {
/* 317 */       ASTree left = args.head();
/* 318 */       return (left instanceof Member && ((Member)left)
/* 319 */         .get().equals(this.paramListName));
/*     */     } 
/* 321 */     return false;
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
/*     */   public int getMethodArgsLength(ASTList args) {
/* 335 */     String pname = this.paramListName;
/* 336 */     int n = 0;
/* 337 */     while (args != null) {
/* 338 */       ASTree a = args.head();
/* 339 */       if (a instanceof Member && ((Member)a).get().equals(pname)) {
/* 340 */         if (this.paramTypeList != null) {
/* 341 */           n += this.paramTypeList.length;
/*     */         }
/*     */       } else {
/* 344 */         n++;
/*     */       } 
/* 346 */       args = args.tail();
/*     */     } 
/*     */     
/* 349 */     return n;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void atMethodArgs(ASTList args, int[] types, int[] dims, String[] cnames) throws CompileError {
/* 355 */     CtClass[] params = this.paramTypeList;
/* 356 */     String pname = this.paramListName;
/* 357 */     int i = 0;
/* 358 */     while (args != null) {
/* 359 */       ASTree a = args.head();
/* 360 */       if (a instanceof Member && ((Member)a).get().equals(pname)) {
/* 361 */         if (params != null) {
/* 362 */           int n = params.length;
/* 363 */           int regno = indexOfParam1();
/* 364 */           for (int k = 0; k < n; k++) {
/* 365 */             CtClass p = params[k];
/* 366 */             regno += this.bytecode.addLoad(regno, p);
/* 367 */             setType(p);
/* 368 */             types[i] = this.exprType;
/* 369 */             dims[i] = this.arrayDim;
/* 370 */             cnames[i] = this.className;
/* 371 */             i++;
/*     */           } 
/*     */         } 
/*     */       } else {
/*     */         
/* 376 */         a.accept(this);
/* 377 */         types[i] = this.exprType;
/* 378 */         dims[i] = this.arrayDim;
/* 379 */         cnames[i] = this.className;
/* 380 */         i++;
/*     */       } 
/*     */       
/* 383 */       args = args.tail();
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
/*     */ 
/*     */   
/*     */   void compileInvokeSpecial(ASTree target, int methodIndex, String descriptor, ASTList args) throws CompileError {
/* 418 */     target.accept(this);
/* 419 */     int nargs = getMethodArgsLength(args);
/* 420 */     atMethodArgs(args, new int[nargs], new int[nargs], new String[nargs]);
/*     */     
/* 422 */     this.bytecode.addInvokespecial(methodIndex, descriptor);
/* 423 */     setReturnType(descriptor, false, false);
/* 424 */     addNullIfVoid();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void atReturnStmnt(Stmnt st) throws CompileError {
/* 432 */     ASTree result = st.getLeft();
/* 433 */     if (result != null && this.returnType == CtClass.voidType) {
/* 434 */       compileExpr(result);
/* 435 */       if (is2word(this.exprType, this.arrayDim)) {
/* 436 */         this.bytecode.addOpcode(88);
/* 437 */       } else if (this.exprType != 344) {
/* 438 */         this.bytecode.addOpcode(87);
/*     */       } 
/* 440 */       result = null;
/*     */     } 
/*     */     
/* 443 */     atReturnStmnt2(result);
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
/*     */   public int recordReturnType(CtClass type, String castName, String resultName, SymbolTable tbl) throws CompileError {
/* 459 */     this.returnType = type;
/* 460 */     this.returnCastName = castName;
/* 461 */     this.returnVarName = resultName;
/* 462 */     if (resultName == null)
/* 463 */       return -1; 
/* 464 */     int varNo = getMaxLocals();
/* 465 */     int locals = varNo + recordVar(type, resultName, varNo, tbl);
/* 466 */     setMaxLocals(locals);
/* 467 */     return varNo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void recordType(CtClass t) {
/* 474 */     this.dollarType = t;
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
/*     */   public int recordParams(CtClass[] params, boolean isStatic, String prefix, String paramVarName, String paramsName, SymbolTable tbl) throws CompileError {
/* 487 */     return recordParams(params, isStatic, prefix, paramVarName, paramsName, !isStatic, 0, 
/* 488 */         getThisName(), tbl);
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
/*     */   public int recordParams(CtClass[] params, boolean isStatic, String prefix, String paramVarName, String paramsName, boolean use0, int paramBase, String target, SymbolTable tbl) throws CompileError {
/* 518 */     this.paramTypeList = params;
/* 519 */     this.paramArrayName = paramVarName;
/* 520 */     this.paramListName = paramsName;
/* 521 */     this.paramVarBase = paramBase;
/* 522 */     this.useParam0 = use0;
/*     */     
/* 524 */     if (target != null) {
/* 525 */       this.param0Type = MemberResolver.jvmToJavaName(target);
/*     */     }
/* 527 */     this.inStaticMethod = isStatic;
/* 528 */     int varNo = paramBase;
/* 529 */     if (use0) {
/* 530 */       String varName = prefix + "0";
/*     */       
/* 532 */       Declarator decl = new Declarator(307, MemberResolver.javaToJvmName(target), 0, varNo++, new Symbol(varName));
/*     */       
/* 534 */       tbl.append(varName, decl);
/*     */     } 
/*     */     
/* 537 */     for (int i = 0; i < params.length; i++) {
/* 538 */       varNo += recordVar(params[i], prefix + (i + 1), varNo, tbl);
/*     */     }
/* 540 */     if (getMaxLocals() < varNo) {
/* 541 */       setMaxLocals(varNo);
/*     */     }
/* 543 */     return varNo;
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
/*     */   public int recordVariable(CtClass type, String varName, SymbolTable tbl) throws CompileError {
/* 555 */     if (varName == null)
/* 556 */       return -1; 
/* 557 */     int varNo = getMaxLocals();
/* 558 */     int locals = varNo + recordVar(type, varName, varNo, tbl);
/* 559 */     setMaxLocals(locals);
/* 560 */     return varNo;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int recordVar(CtClass cc, String varName, int varNo, SymbolTable tbl) throws CompileError {
/* 566 */     if (cc == CtClass.voidType) {
/* 567 */       this.exprType = 307;
/* 568 */       this.arrayDim = 0;
/* 569 */       this.className = "java/lang/Object";
/*     */     } else {
/*     */       
/* 572 */       setType(cc);
/*     */     } 
/* 574 */     Declarator decl = new Declarator(this.exprType, this.className, this.arrayDim, varNo, new Symbol(varName));
/*     */ 
/*     */     
/* 577 */     tbl.append(varName, decl);
/* 578 */     return is2word(this.exprType, this.arrayDim) ? 2 : 1;
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
/*     */   public void recordVariable(String typeDesc, String varName, int varNo, SymbolTable tbl) throws CompileError {
/* 592 */     int dim = 0; char c;
/* 593 */     while ((c = typeDesc.charAt(dim)) == '[') {
/* 594 */       dim++;
/*     */     }
/* 596 */     int type = MemberResolver.descToType(c);
/* 597 */     String cname = null;
/* 598 */     if (type == 307) {
/* 599 */       if (dim == 0) {
/* 600 */         cname = typeDesc.substring(1, typeDesc.length() - 1);
/*     */       } else {
/* 602 */         cname = typeDesc.substring(dim + 1, typeDesc.length() - 1);
/*     */       } 
/*     */     }
/* 605 */     Declarator decl = new Declarator(type, cname, dim, varNo, new Symbol(varName));
/*     */     
/* 607 */     tbl.append(varName, decl);
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
/*     */   public static int compileParameterList(Bytecode code, CtClass[] params, int regno) {
/* 621 */     if (params == null) {
/* 622 */       code.addIconst(0);
/* 623 */       code.addAnewarray("java.lang.Object");
/* 624 */       return 1;
/*     */     } 
/* 626 */     CtClass[] args = new CtClass[1];
/* 627 */     int n = params.length;
/* 628 */     code.addIconst(n);
/* 629 */     code.addAnewarray("java.lang.Object");
/* 630 */     for (int i = 0; i < n; i++) {
/* 631 */       code.addOpcode(89);
/* 632 */       code.addIconst(i);
/* 633 */       if (params[i].isPrimitive()) {
/* 634 */         CtPrimitiveType pt = (CtPrimitiveType)params[i];
/* 635 */         String wrapper = pt.getWrapperName();
/* 636 */         code.addNew(wrapper);
/* 637 */         code.addOpcode(89);
/* 638 */         int s = code.addLoad(regno, (CtClass)pt);
/* 639 */         regno += s;
/* 640 */         args[0] = (CtClass)pt;
/* 641 */         code.addInvokespecial(wrapper, "<init>", 
/* 642 */             Descriptor.ofMethod(CtClass.voidType, args));
/*     */       }
/*     */       else {
/*     */         
/* 646 */         code.addAload(regno);
/* 647 */         regno++;
/*     */       } 
/*     */       
/* 650 */       code.addOpcode(83);
/*     */     } 
/*     */     
/* 653 */     return 8;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void compileUnwrapValue(CtClass type, Bytecode code) throws CompileError {
/* 659 */     if (type == CtClass.voidType) {
/* 660 */       addNullIfVoid();
/*     */       
/*     */       return;
/*     */     } 
/* 664 */     if (this.exprType == 344) {
/* 665 */       throw new CompileError("invalid type for " + this.returnCastName);
/*     */     }
/* 667 */     if (type instanceof CtPrimitiveType) {
/* 668 */       CtPrimitiveType pt = (CtPrimitiveType)type;
/*     */       
/* 670 */       String wrapper = pt.getWrapperName();
/* 671 */       code.addCheckcast(wrapper);
/* 672 */       code.addInvokevirtual(wrapper, pt.getGetMethodName(), pt
/* 673 */           .getGetMethodDescriptor());
/* 674 */       setType(type);
/*     */     } else {
/*     */       
/* 677 */       code.addCheckcast(type);
/* 678 */       setType(type);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(CtClass type) throws CompileError {
/* 686 */     setType(type, 0);
/*     */   }
/*     */   
/*     */   private void setType(CtClass type, int dim) throws CompileError {
/* 690 */     if (type.isPrimitive()) {
/* 691 */       CtPrimitiveType pt = (CtPrimitiveType)type;
/* 692 */       this.exprType = MemberResolver.descToType(pt.getDescriptor());
/* 693 */       this.arrayDim = dim;
/* 694 */       this.className = null;
/*     */     }
/* 696 */     else if (type.isArray()) {
/*     */       try {
/* 698 */         setType(type.getComponentType(), dim + 1);
/*     */       }
/* 700 */       catch (NotFoundException e) {
/* 701 */         throw new CompileError("undefined type: " + type.getName());
/*     */       } 
/*     */     } else {
/* 704 */       this.exprType = 307;
/* 705 */       this.arrayDim = dim;
/* 706 */       this.className = MemberResolver.javaToJvmName(type.getName());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void doNumCast(CtClass type) throws CompileError {
/* 713 */     if (this.arrayDim == 0 && !isRefType(this.exprType))
/* 714 */       if (type instanceof CtPrimitiveType) {
/* 715 */         CtPrimitiveType pt = (CtPrimitiveType)type;
/* 716 */         atNumCastExpr(this.exprType, 
/* 717 */             MemberResolver.descToType(pt.getDescriptor()));
/*     */       } else {
/*     */         
/* 720 */         throw new CompileError("type mismatch");
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\JvstCodeGen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */