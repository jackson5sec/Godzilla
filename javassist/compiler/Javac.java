/*     */ package javassist.compiler;
/*     */ 
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.CtBehavior;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtConstructor;
/*     */ import javassist.CtField;
/*     */ import javassist.CtMember;
/*     */ import javassist.CtMethod;
/*     */ import javassist.CtPrimitiveType;
/*     */ import javassist.Modifier;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.LocalVariableAttribute;
/*     */ import javassist.compiler.ast.ASTList;
/*     */ import javassist.compiler.ast.ASTree;
/*     */ import javassist.compiler.ast.CallExpr;
/*     */ import javassist.compiler.ast.Declarator;
/*     */ import javassist.compiler.ast.Expr;
/*     */ import javassist.compiler.ast.FieldDecl;
/*     */ import javassist.compiler.ast.Member;
/*     */ import javassist.compiler.ast.MethodDecl;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Javac
/*     */ {
/*     */   JvstCodeGen gen;
/*     */   SymbolTable stable;
/*     */   private Bytecode bytecode;
/*     */   public static final String param0Name = "$0";
/*     */   public static final String resultVarName = "$_";
/*     */   public static final String proceedName = "$proceed";
/*     */   
/*     */   public Javac(CtClass thisClass) {
/*  61 */     this(new Bytecode(thisClass.getClassFile2().getConstPool(), 0, 0), thisClass);
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
/*     */   public Javac(Bytecode b, CtClass thisClass) {
/*  74 */     this.gen = new JvstCodeGen(b, thisClass, thisClass.getClassPool());
/*  75 */     this.stable = new SymbolTable();
/*  76 */     this.bytecode = b;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Bytecode getBytecode() {
/*  82 */     return this.bytecode;
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
/*     */   public CtMember compile(String src) throws CompileError {
/*  97 */     Parser p = new Parser(new Lex(src));
/*  98 */     ASTList mem = p.parseMember1(this.stable);
/*     */     try {
/* 100 */       if (mem instanceof FieldDecl)
/* 101 */         return (CtMember)compileField((FieldDecl)mem); 
/* 102 */       CtBehavior cb = compileMethod(p, (MethodDecl)mem);
/* 103 */       CtClass decl = cb.getDeclaringClass();
/* 104 */       cb.getMethodInfo2()
/* 105 */         .rebuildStackMapIf6(decl.getClassPool(), decl
/* 106 */           .getClassFile2());
/* 107 */       return (CtMember)cb;
/*     */     }
/* 109 */     catch (BadBytecode bb) {
/* 110 */       throw new CompileError(bb.getMessage());
/*     */     }
/* 112 */     catch (CannotCompileException e) {
/* 113 */       throw new CompileError(e.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   public static class CtFieldWithInit
/*     */     extends CtField
/*     */   {
/*     */     private ASTree init;
/*     */     
/*     */     CtFieldWithInit(CtClass type, String name, CtClass declaring) throws CannotCompileException {
/* 123 */       super(type, name, declaring);
/* 124 */       this.init = null;
/*     */     }
/*     */     protected void setInit(ASTree i) {
/* 127 */       this.init = i;
/*     */     }
/*     */     
/*     */     protected ASTree getInitAST() {
/* 131 */       return this.init;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CtField compileField(FieldDecl fd) throws CompileError, CannotCompileException {
/* 139 */     Declarator d = fd.getDeclarator();
/*     */     
/* 141 */     CtFieldWithInit f = new CtFieldWithInit(this.gen.resolver.lookupClass(d), d.getVariable().get(), this.gen.getThisClass());
/* 142 */     f.setModifiers(MemberResolver.getModifiers(fd.getModifiers()));
/* 143 */     if (fd.getInit() != null) {
/* 144 */       f.setInit(fd.getInit());
/*     */     }
/* 146 */     return f;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private CtBehavior compileMethod(Parser p, MethodDecl md) throws CompileError {
/* 152 */     int mod = MemberResolver.getModifiers(md.getModifiers());
/* 153 */     CtClass[] plist = this.gen.makeParamList(md);
/* 154 */     CtClass[] tlist = this.gen.makeThrowsList(md);
/* 155 */     recordParams(plist, Modifier.isStatic(mod));
/* 156 */     md = p.parseMethod2(this.stable, md);
/*     */     try {
/* 158 */       if (md.isConstructor()) {
/*     */         
/* 160 */         CtConstructor cons = new CtConstructor(plist, this.gen.getThisClass());
/* 161 */         cons.setModifiers(mod);
/* 162 */         md.accept(this.gen);
/* 163 */         cons.getMethodInfo().setCodeAttribute(this.bytecode
/* 164 */             .toCodeAttribute());
/* 165 */         cons.setExceptionTypes(tlist);
/* 166 */         return (CtBehavior)cons;
/*     */       } 
/* 168 */       Declarator r = md.getReturn();
/* 169 */       CtClass rtype = this.gen.resolver.lookupClass(r);
/* 170 */       recordReturnType(rtype, false);
/*     */       
/* 172 */       CtMethod method = new CtMethod(rtype, r.getVariable().get(), plist, this.gen.getThisClass());
/* 173 */       method.setModifiers(mod);
/* 174 */       this.gen.setThisMethod(method);
/* 175 */       md.accept(this.gen);
/* 176 */       if (md.getBody() != null) {
/* 177 */         method.getMethodInfo().setCodeAttribute(this.bytecode
/* 178 */             .toCodeAttribute());
/*     */       } else {
/* 180 */         method.setModifiers(mod | 0x400);
/*     */       } 
/* 182 */       method.setExceptionTypes(tlist);
/* 183 */       return (CtBehavior)method;
/*     */     }
/* 185 */     catch (NotFoundException e) {
/* 186 */       throw new CompileError(e.toString());
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
/*     */   public Bytecode compileBody(CtBehavior method, String src) throws CompileError {
/*     */     try {
/*     */       CtClass rtype;
/* 200 */       int mod = method.getModifiers();
/* 201 */       recordParams(method.getParameterTypes(), Modifier.isStatic(mod));
/*     */ 
/*     */       
/* 204 */       if (method instanceof CtMethod) {
/* 205 */         this.gen.setThisMethod((CtMethod)method);
/* 206 */         rtype = ((CtMethod)method).getReturnType();
/*     */       } else {
/*     */         
/* 209 */         rtype = CtClass.voidType;
/*     */       } 
/* 211 */       recordReturnType(rtype, false);
/* 212 */       boolean isVoid = (rtype == CtClass.voidType);
/*     */       
/* 214 */       if (src == null) {
/* 215 */         makeDefaultBody(this.bytecode, rtype);
/*     */       } else {
/* 217 */         Parser p = new Parser(new Lex(src));
/* 218 */         SymbolTable stb = new SymbolTable(this.stable);
/* 219 */         Stmnt s = p.parseStatement(stb);
/* 220 */         if (p.hasMore()) {
/* 221 */           throw new CompileError("the method/constructor body must be surrounded by {}");
/*     */         }
/*     */         
/* 224 */         boolean callSuper = false;
/* 225 */         if (method instanceof CtConstructor) {
/* 226 */           callSuper = !((CtConstructor)method).isClassInitializer();
/*     */         }
/* 228 */         this.gen.atMethodBody(s, callSuper, isVoid);
/*     */       } 
/*     */       
/* 231 */       return this.bytecode;
/*     */     }
/* 233 */     catch (NotFoundException e) {
/* 234 */       throw new CompileError(e.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void makeDefaultBody(Bytecode b, CtClass type) {
/*     */     int op, value;
/* 241 */     if (type instanceof CtPrimitiveType) {
/* 242 */       CtPrimitiveType pt = (CtPrimitiveType)type;
/* 243 */       op = pt.getReturnOp();
/* 244 */       if (op == 175) {
/* 245 */         value = 14;
/* 246 */       } else if (op == 174) {
/* 247 */         value = 11;
/* 248 */       } else if (op == 173) {
/* 249 */         value = 9;
/* 250 */       } else if (op == 177) {
/* 251 */         value = 0;
/*     */       } else {
/* 253 */         value = 3;
/*     */       } 
/*     */     } else {
/* 256 */       op = 176;
/* 257 */       value = 1;
/*     */     } 
/*     */     
/* 260 */     if (value != 0) {
/* 261 */       b.addOpcode(value);
/*     */     }
/* 263 */     b.addOpcode(op);
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
/*     */   public boolean recordLocalVariables(CodeAttribute ca, int pc) throws CompileError {
/* 280 */     LocalVariableAttribute va = (LocalVariableAttribute)ca.getAttribute("LocalVariableTable");
/* 281 */     if (va == null) {
/* 282 */       return false;
/*     */     }
/* 284 */     int n = va.tableLength();
/* 285 */     for (int i = 0; i < n; i++) {
/* 286 */       int start = va.startPc(i);
/* 287 */       int len = va.codeLength(i);
/* 288 */       if (start <= pc && pc < start + len) {
/* 289 */         this.gen.recordVariable(va.descriptor(i), va.variableName(i), va
/* 290 */             .index(i), this.stable);
/*     */       }
/*     */     } 
/* 293 */     return true;
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
/*     */   public boolean recordParamNames(CodeAttribute ca, int numOfLocalVars) throws CompileError {
/* 310 */     LocalVariableAttribute va = (LocalVariableAttribute)ca.getAttribute("LocalVariableTable");
/* 311 */     if (va == null) {
/* 312 */       return false;
/*     */     }
/* 314 */     int n = va.tableLength();
/* 315 */     for (int i = 0; i < n; i++) {
/* 316 */       int index = va.index(i);
/* 317 */       if (index < numOfLocalVars) {
/* 318 */         this.gen.recordVariable(va.descriptor(i), va.variableName(i), index, this.stable);
/*     */       }
/*     */     } 
/*     */     
/* 322 */     return true;
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
/*     */   public int recordParams(CtClass[] params, boolean isStatic) throws CompileError {
/* 339 */     return this.gen.recordParams(params, isStatic, "$", "$args", "$$", this.stable);
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
/*     */   public int recordParams(String target, CtClass[] params, boolean use0, int varNo, boolean isStatic) throws CompileError {
/* 367 */     return this.gen.recordParams(params, isStatic, "$", "$args", "$$", use0, varNo, target, this.stable);
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
/*     */   public void setMaxLocals(int max) {
/* 381 */     this.gen.setMaxLocals(max);
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
/*     */   public int recordReturnType(CtClass type, boolean useResultVar) throws CompileError {
/* 401 */     this.gen.recordType(type);
/* 402 */     return this.gen.recordReturnType(type, "$r", 
/* 403 */         useResultVar ? "$_" : null, this.stable);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void recordType(CtClass t) {
/* 413 */     this.gen.recordType(t);
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
/*     */   public int recordVariable(CtClass type, String name) throws CompileError {
/* 425 */     return this.gen.recordVariable(type, name, this.stable);
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
/*     */   public void recordProceed(String target, String method) throws CompileError {
/* 440 */     Parser p = new Parser(new Lex(target));
/* 441 */     final ASTree texpr = p.parseExpression(this.stable);
/* 442 */     final String m = method;
/*     */     
/* 444 */     ProceedHandler h = new ProceedHandler()
/*     */       {
/*     */         public void doit(JvstCodeGen gen, Bytecode b, ASTList args) throws CompileError
/*     */         {
/*     */           Expr expr;
/* 449 */           Member member = new Member(m);
/* 450 */           if (texpr != null) {
/* 451 */             expr = Expr.make(46, texpr, (ASTree)member);
/*     */           }
/* 453 */           CallExpr callExpr = CallExpr.makeCall((ASTree)expr, (ASTree)args);
/* 454 */           gen.compileExpr((ASTree)callExpr);
/* 455 */           gen.addNullIfVoid();
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public void setReturnType(JvstTypeChecker check, ASTList args) throws CompileError {
/*     */           Expr expr;
/* 462 */           Member member = new Member(m);
/* 463 */           if (texpr != null) {
/* 464 */             expr = Expr.make(46, texpr, (ASTree)member);
/*     */           }
/* 466 */           CallExpr callExpr = CallExpr.makeCall((ASTree)expr, (ASTree)args);
/* 467 */           callExpr.accept(check);
/* 468 */           check.addNullIfVoid();
/*     */         }
/*     */       };
/*     */     
/* 472 */     this.gen.setProceedHandler(h, "$proceed");
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
/*     */   public void recordStaticProceed(String targetClass, String method) throws CompileError {
/* 487 */     final String c = targetClass;
/* 488 */     final String m = method;
/*     */     
/* 490 */     ProceedHandler h = new ProceedHandler()
/*     */       {
/*     */         
/*     */         public void doit(JvstCodeGen gen, Bytecode b, ASTList args) throws CompileError
/*     */         {
/* 495 */           Expr expr = Expr.make(35, (ASTree)new Symbol(c), (ASTree)new Member(m));
/*     */           
/* 497 */           CallExpr callExpr = CallExpr.makeCall((ASTree)expr, (ASTree)args);
/* 498 */           gen.compileExpr((ASTree)callExpr);
/* 499 */           gen.addNullIfVoid();
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void setReturnType(JvstTypeChecker check, ASTList args) throws CompileError {
/* 506 */           Expr expr = Expr.make(35, (ASTree)new Symbol(c), (ASTree)new Member(m));
/*     */           
/* 508 */           CallExpr callExpr = CallExpr.makeCall((ASTree)expr, (ASTree)args);
/* 509 */           callExpr.accept(check);
/* 510 */           check.addNullIfVoid();
/*     */         }
/*     */       };
/*     */     
/* 514 */     this.gen.setProceedHandler(h, "$proceed");
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
/*     */   public void recordSpecialProceed(String target, final String classname, final String methodname, final String descriptor, final int methodIndex) throws CompileError {
/* 533 */     Parser p = new Parser(new Lex(target));
/* 534 */     final ASTree texpr = p.parseExpression(this.stable);
/*     */     
/* 536 */     ProceedHandler h = new ProceedHandler()
/*     */       {
/*     */         
/*     */         public void doit(JvstCodeGen gen, Bytecode b, ASTList args) throws CompileError
/*     */         {
/* 541 */           gen.compileInvokeSpecial(texpr, methodIndex, descriptor, args);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void setReturnType(JvstTypeChecker c, ASTList args) throws CompileError {
/* 548 */           c.compileInvokeSpecial(texpr, classname, methodname, descriptor, args);
/*     */         }
/*     */       };
/*     */ 
/*     */     
/* 553 */     this.gen.setProceedHandler(h, "$proceed");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void recordProceed(ProceedHandler h) {
/* 560 */     this.gen.setProceedHandler(h, "$proceed");
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
/*     */   public void compileStmnt(String src) throws CompileError {
/* 573 */     Parser p = new Parser(new Lex(src));
/* 574 */     SymbolTable stb = new SymbolTable(this.stable);
/* 575 */     while (p.hasMore()) {
/* 576 */       Stmnt s = p.parseStatement(stb);
/* 577 */       if (s != null) {
/* 578 */         s.accept(this.gen);
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
/*     */   public void compileExpr(String src) throws CompileError {
/* 592 */     ASTree e = parseExpr(src, this.stable);
/* 593 */     compileExpr(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ASTree parseExpr(String src, SymbolTable st) throws CompileError {
/* 602 */     Parser p = new Parser(new Lex(src));
/* 603 */     return p.parseExpression(st);
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
/*     */   public void compileExpr(ASTree e) throws CompileError {
/* 616 */     if (e != null)
/* 617 */       this.gen.compileExpr(e); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\Javac.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */