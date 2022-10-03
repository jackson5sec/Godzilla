/*     */ package javassist.compiler;
/*     */ 
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtPrimitiveType;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.compiler.ast.ASTList;
/*     */ import javassist.compiler.ast.ASTree;
/*     */ import javassist.compiler.ast.CallExpr;
/*     */ import javassist.compiler.ast.CastExpr;
/*     */ import javassist.compiler.ast.Expr;
/*     */ import javassist.compiler.ast.Member;
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
/*     */ public class JvstTypeChecker
/*     */   extends TypeChecker
/*     */ {
/*     */   private JvstCodeGen codeGen;
/*     */   
/*     */   public JvstTypeChecker(CtClass cc, ClassPool cp, JvstCodeGen gen) {
/*  38 */     super(cc, cp);
/*  39 */     this.codeGen = gen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNullIfVoid() {
/*  46 */     if (this.exprType == 344) {
/*  47 */       this.exprType = 307;
/*  48 */       this.arrayDim = 0;
/*  49 */       this.className = "java/lang/Object";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void atMember(Member mem) throws CompileError {
/*  58 */     String name = mem.get();
/*  59 */     if (name.equals(this.codeGen.paramArrayName)) {
/*  60 */       this.exprType = 307;
/*  61 */       this.arrayDim = 1;
/*  62 */       this.className = "java/lang/Object";
/*     */     }
/*  64 */     else if (name.equals("$sig")) {
/*  65 */       this.exprType = 307;
/*  66 */       this.arrayDim = 1;
/*  67 */       this.className = "java/lang/Class";
/*     */     }
/*  69 */     else if (name.equals("$type") || name
/*  70 */       .equals("$class")) {
/*  71 */       this.exprType = 307;
/*  72 */       this.arrayDim = 0;
/*  73 */       this.className = "java/lang/Class";
/*     */     } else {
/*     */       
/*  76 */       super.atMember(mem);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void atFieldAssign(Expr expr, int op, ASTree left, ASTree right) throws CompileError {
/*  83 */     if (left instanceof Member && ((Member)left)
/*  84 */       .get().equals(this.codeGen.paramArrayName)) {
/*  85 */       right.accept(this);
/*  86 */       CtClass[] params = this.codeGen.paramTypeList;
/*  87 */       if (params == null) {
/*     */         return;
/*     */       }
/*  90 */       int n = params.length;
/*  91 */       for (int i = 0; i < n; i++) {
/*  92 */         compileUnwrapValue(params[i]);
/*     */       }
/*     */     } else {
/*  95 */       super.atFieldAssign(expr, op, left, right);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void atCastExpr(CastExpr expr) throws CompileError {
/* 100 */     ASTList classname = expr.getClassName();
/* 101 */     if (classname != null && expr.getArrayDim() == 0) {
/* 102 */       ASTree p = classname.head();
/* 103 */       if (p instanceof Symbol && classname.tail() == null) {
/* 104 */         String typename = ((Symbol)p).get();
/* 105 */         if (typename.equals(this.codeGen.returnCastName)) {
/* 106 */           atCastToRtype(expr);
/*     */           return;
/*     */         } 
/* 109 */         if (typename.equals("$w")) {
/* 110 */           atCastToWrapper(expr);
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/* 116 */     super.atCastExpr(expr);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void atCastToRtype(CastExpr expr) throws CompileError {
/* 124 */     CtClass returnType = this.codeGen.returnType;
/* 125 */     expr.getOprand().accept(this);
/* 126 */     if (this.exprType == 344 || CodeGen.isRefType(this.exprType) || this.arrayDim > 0) {
/* 127 */       compileUnwrapValue(returnType);
/* 128 */     } else if (returnType instanceof CtPrimitiveType) {
/* 129 */       CtPrimitiveType pt = (CtPrimitiveType)returnType;
/* 130 */       int destType = MemberResolver.descToType(pt.getDescriptor());
/* 131 */       this.exprType = destType;
/* 132 */       this.arrayDim = 0;
/* 133 */       this.className = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void atCastToWrapper(CastExpr expr) throws CompileError {
/* 138 */     expr.getOprand().accept(this);
/* 139 */     if (CodeGen.isRefType(this.exprType) || this.arrayDim > 0) {
/*     */       return;
/*     */     }
/* 142 */     CtClass clazz = this.resolver.lookupClass(this.exprType, this.arrayDim, this.className);
/* 143 */     if (clazz instanceof CtPrimitiveType) {
/* 144 */       this.exprType = 307;
/* 145 */       this.arrayDim = 0;
/* 146 */       this.className = "java/lang/Object";
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void atCallExpr(CallExpr expr) throws CompileError {
/* 155 */     ASTree method = expr.oprand1();
/* 156 */     if (method instanceof Member) {
/* 157 */       String name = ((Member)method).get();
/* 158 */       if (this.codeGen.procHandler != null && name
/* 159 */         .equals(this.codeGen.proceedName)) {
/* 160 */         this.codeGen.procHandler.setReturnType(this, (ASTList)expr
/* 161 */             .oprand2());
/*     */         return;
/*     */       } 
/* 164 */       if (name.equals("$cflow")) {
/* 165 */         atCflow((ASTList)expr.oprand2());
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 170 */     super.atCallExpr(expr);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void atCflow(ASTList cname) throws CompileError {
/* 176 */     this.exprType = 324;
/* 177 */     this.arrayDim = 0;
/* 178 */     this.className = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isParamListName(ASTList args) {
/* 185 */     if (this.codeGen.paramTypeList != null && args != null && args
/* 186 */       .tail() == null) {
/* 187 */       ASTree left = args.head();
/* 188 */       return (left instanceof Member && ((Member)left)
/* 189 */         .get().equals(this.codeGen.paramListName));
/*     */     } 
/* 191 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMethodArgsLength(ASTList args) {
/* 196 */     String pname = this.codeGen.paramListName;
/* 197 */     int n = 0;
/* 198 */     while (args != null) {
/* 199 */       ASTree a = args.head();
/* 200 */       if (a instanceof Member && ((Member)a).get().equals(pname)) {
/* 201 */         if (this.codeGen.paramTypeList != null) {
/* 202 */           n += this.codeGen.paramTypeList.length;
/*     */         }
/*     */       } else {
/* 205 */         n++;
/*     */       } 
/* 207 */       args = args.tail();
/*     */     } 
/*     */     
/* 210 */     return n;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void atMethodArgs(ASTList args, int[] types, int[] dims, String[] cnames) throws CompileError {
/* 216 */     CtClass[] params = this.codeGen.paramTypeList;
/* 217 */     String pname = this.codeGen.paramListName;
/* 218 */     int i = 0;
/* 219 */     while (args != null) {
/* 220 */       ASTree a = args.head();
/* 221 */       if (a instanceof Member && ((Member)a).get().equals(pname)) {
/* 222 */         if (params != null) {
/* 223 */           int n = params.length;
/* 224 */           for (int k = 0; k < n; k++) {
/* 225 */             CtClass p = params[k];
/* 226 */             setType(p);
/* 227 */             types[i] = this.exprType;
/* 228 */             dims[i] = this.arrayDim;
/* 229 */             cnames[i] = this.className;
/* 230 */             i++;
/*     */           } 
/*     */         } 
/*     */       } else {
/*     */         
/* 235 */         a.accept(this);
/* 236 */         types[i] = this.exprType;
/* 237 */         dims[i] = this.arrayDim;
/* 238 */         cnames[i] = this.className;
/* 239 */         i++;
/*     */       } 
/*     */       
/* 242 */       args = args.tail();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void compileInvokeSpecial(ASTree target, String classname, String methodname, String descriptor, ASTList args) throws CompileError {
/* 253 */     target.accept(this);
/* 254 */     int nargs = getMethodArgsLength(args);
/* 255 */     atMethodArgs(args, new int[nargs], new int[nargs], new String[nargs]);
/*     */     
/* 257 */     setReturnType(descriptor);
/* 258 */     addNullIfVoid();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void compileUnwrapValue(CtClass type) throws CompileError {
/* 263 */     if (type == CtClass.voidType) {
/* 264 */       addNullIfVoid();
/*     */     } else {
/* 266 */       setType(type);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(CtClass type) throws CompileError {
/* 273 */     setType(type, 0);
/*     */   }
/*     */   
/*     */   private void setType(CtClass type, int dim) throws CompileError {
/* 277 */     if (type.isPrimitive()) {
/* 278 */       CtPrimitiveType pt = (CtPrimitiveType)type;
/* 279 */       this.exprType = MemberResolver.descToType(pt.getDescriptor());
/* 280 */       this.arrayDim = dim;
/* 281 */       this.className = null;
/*     */     }
/* 283 */     else if (type.isArray()) {
/*     */       try {
/* 285 */         setType(type.getComponentType(), dim + 1);
/*     */       }
/* 287 */       catch (NotFoundException e) {
/* 288 */         throw new CompileError("undefined type: " + type.getName());
/*     */       } 
/*     */     } else {
/* 291 */       this.exprType = 307;
/* 292 */       this.arrayDim = dim;
/* 293 */       this.className = MemberResolver.javaToJvmName(type.getName());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\JvstTypeChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */