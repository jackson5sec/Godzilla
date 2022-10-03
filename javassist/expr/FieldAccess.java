/*     */ package javassist.expr;
/*     */ 
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.CtBehavior;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtField;
/*     */ import javassist.CtPrimitiveType;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.Descriptor;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ import javassist.compiler.CompileError;
/*     */ import javassist.compiler.Javac;
/*     */ import javassist.compiler.JvstCodeGen;
/*     */ import javassist.compiler.JvstTypeChecker;
/*     */ import javassist.compiler.ProceedHandler;
/*     */ import javassist.compiler.ast.ASTList;
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
/*     */ public class FieldAccess
/*     */   extends Expr
/*     */ {
/*     */   int opcode;
/*     */   
/*     */   protected FieldAccess(int pos, CodeIterator i, CtClass declaring, MethodInfo m, int op) {
/*  49 */     super(pos, i, declaring, m);
/*  50 */     this.opcode = op;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtBehavior where() {
/*  58 */     return super.where();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineNumber() {
/*  68 */     return super.getLineNumber();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/*  78 */     return super.getFileName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStatic() {
/*  85 */     return isStatic(this.opcode);
/*     */   }
/*     */   
/*     */   static boolean isStatic(int c) {
/*  89 */     return (c == 178 || c == 179);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReader() {
/*  96 */     return (this.opcode == 180 || this.opcode == 178);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWriter() {
/* 103 */     return (this.opcode == 181 || this.opcode == 179);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CtClass getCtClass() throws NotFoundException {
/* 110 */     return this.thisClass.getClassPool().get(getClassName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/* 117 */     int index = this.iterator.u16bitAt(this.currentPos + 1);
/* 118 */     return getConstPool().getFieldrefClassName(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFieldName() {
/* 125 */     int index = this.iterator.u16bitAt(this.currentPos + 1);
/* 126 */     return getConstPool().getFieldrefName(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtField getField() throws NotFoundException {
/* 133 */     CtClass cc = getCtClass();
/* 134 */     int index = this.iterator.u16bitAt(this.currentPos + 1);
/* 135 */     ConstPool cp = getConstPool();
/* 136 */     return cc.getField(cp.getFieldrefName(index), cp.getFieldrefType(index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass[] mayThrow() {
/* 147 */     return super.mayThrow();
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
/*     */   public String getSignature() {
/* 159 */     int index = this.iterator.u16bitAt(this.currentPos + 1);
/* 160 */     return getConstPool().getFieldrefType(index);
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
/*     */   public void replace(String statement) throws CannotCompileException {
/* 175 */     this.thisClass.getClassFile();
/* 176 */     ConstPool constPool = getConstPool();
/* 177 */     int pos = this.currentPos;
/* 178 */     int index = this.iterator.u16bitAt(pos + 1);
/*     */     
/* 180 */     Javac jc = new Javac(this.thisClass);
/* 181 */     CodeAttribute ca = this.iterator.get();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 186 */     try { CtClass params[], retType, fieldType = Descriptor.toCtClass(constPool.getFieldrefType(index), this.thisClass
/* 187 */           .getClassPool());
/* 188 */       boolean read = isReader();
/* 189 */       if (read) {
/* 190 */         params = new CtClass[0];
/* 191 */         retType = fieldType;
/*     */       } else {
/*     */         
/* 194 */         params = new CtClass[1];
/* 195 */         params[0] = fieldType;
/* 196 */         retType = CtClass.voidType;
/*     */       } 
/*     */       
/* 199 */       int paramVar = ca.getMaxLocals();
/* 200 */       jc.recordParams(constPool.getFieldrefClassName(index), params, true, paramVar, 
/* 201 */           withinStatic());
/*     */ 
/*     */ 
/*     */       
/* 205 */       boolean included = checkResultValue(retType, statement);
/* 206 */       if (read) {
/* 207 */         included = true;
/*     */       }
/* 209 */       int retVar = jc.recordReturnType(retType, included);
/* 210 */       if (read) {
/* 211 */         jc.recordProceed(new ProceedForRead(retType, this.opcode, index, paramVar));
/*     */       }
/*     */       else {
/*     */         
/* 215 */         jc.recordType(fieldType);
/* 216 */         jc.recordProceed(new ProceedForWrite(params[0], this.opcode, index, paramVar));
/*     */       } 
/*     */ 
/*     */       
/* 220 */       Bytecode bytecode = jc.getBytecode();
/* 221 */       storeStack(params, isStatic(), paramVar, bytecode);
/* 222 */       jc.recordLocalVariables(ca, pos);
/*     */       
/* 224 */       if (included) {
/* 225 */         if (retType == CtClass.voidType) {
/* 226 */           bytecode.addOpcode(1);
/* 227 */           bytecode.addAstore(retVar);
/*     */         } else {
/*     */           
/* 230 */           bytecode.addConstZero(retType);
/* 231 */           bytecode.addStore(retVar, retType);
/*     */         } 
/*     */       }
/* 234 */       jc.compileStmnt(statement);
/* 235 */       if (read) {
/* 236 */         bytecode.addLoad(retVar, retType);
/*     */       }
/* 238 */       replace0(pos, bytecode, 3); }
/*     */     catch (CompileError e)
/* 240 */     { throw new CannotCompileException(e); }
/* 241 */     catch (NotFoundException e) { throw new CannotCompileException(e); }
/* 242 */     catch (BadBytecode e)
/* 243 */     { throw new CannotCompileException("broken method"); }
/*     */   
/*     */   }
/*     */   
/*     */   static class ProceedForRead
/*     */     implements ProceedHandler {
/*     */     CtClass fieldType;
/*     */     int opcode;
/*     */     int targetVar;
/*     */     int index;
/*     */     
/*     */     ProceedForRead(CtClass type, int op, int i, int var) {
/* 255 */       this.fieldType = type;
/* 256 */       this.targetVar = var;
/* 257 */       this.opcode = op;
/* 258 */       this.index = i;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void doit(JvstCodeGen gen, Bytecode bytecode, ASTList args) throws CompileError {
/*     */       int stack;
/* 265 */       if (args != null && !gen.isParamListName(args)) {
/* 266 */         throw new CompileError("$proceed() cannot take a parameter for field reading");
/*     */       }
/*     */ 
/*     */       
/* 270 */       if (FieldAccess.isStatic(this.opcode)) {
/* 271 */         stack = 0;
/*     */       } else {
/* 273 */         stack = -1;
/* 274 */         bytecode.addAload(this.targetVar);
/*     */       } 
/*     */       
/* 277 */       if (this.fieldType instanceof CtPrimitiveType) {
/* 278 */         stack += ((CtPrimitiveType)this.fieldType).getDataSize();
/*     */       } else {
/* 280 */         stack++;
/*     */       } 
/* 282 */       bytecode.add(this.opcode);
/* 283 */       bytecode.addIndex(this.index);
/* 284 */       bytecode.growStack(stack);
/* 285 */       gen.setType(this.fieldType);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setReturnType(JvstTypeChecker c, ASTList args) throws CompileError {
/* 292 */       c.setType(this.fieldType);
/*     */     }
/*     */   }
/*     */   
/*     */   static class ProceedForWrite
/*     */     implements ProceedHandler
/*     */   {
/*     */     CtClass fieldType;
/*     */     int opcode;
/*     */     int targetVar;
/*     */     int index;
/*     */     
/*     */     ProceedForWrite(CtClass type, int op, int i, int var) {
/* 305 */       this.fieldType = type;
/* 306 */       this.targetVar = var;
/* 307 */       this.opcode = op;
/* 308 */       this.index = i;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void doit(JvstCodeGen gen, Bytecode bytecode, ASTList args) throws CompileError {
/*     */       int stack;
/* 315 */       if (gen.getMethodArgsLength(args) != 1) {
/* 316 */         throw new CompileError("$proceed() cannot take more than one parameter for field writing");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 321 */       if (FieldAccess.isStatic(this.opcode)) {
/* 322 */         stack = 0;
/*     */       } else {
/* 324 */         stack = -1;
/* 325 */         bytecode.addAload(this.targetVar);
/*     */       } 
/*     */       
/* 328 */       gen.atMethodArgs(args, new int[1], new int[1], new String[1]);
/* 329 */       gen.doNumCast(this.fieldType);
/* 330 */       if (this.fieldType instanceof CtPrimitiveType) {
/* 331 */         stack -= ((CtPrimitiveType)this.fieldType).getDataSize();
/*     */       } else {
/* 333 */         stack--;
/*     */       } 
/* 335 */       bytecode.add(this.opcode);
/* 336 */       bytecode.addIndex(this.index);
/* 337 */       bytecode.growStack(stack);
/* 338 */       gen.setType(CtClass.voidType);
/* 339 */       gen.addNullIfVoid();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setReturnType(JvstTypeChecker c, ASTList args) throws CompileError {
/* 346 */       c.atMethodArgs(args, new int[1], new int[1], new String[1]);
/* 347 */       c.setType(CtClass.voidType);
/* 348 */       c.addNullIfVoid();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\expr\FieldAccess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */