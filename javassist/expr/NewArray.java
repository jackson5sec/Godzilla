/*     */ package javassist.expr;
/*     */ 
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.CtBehavior;
/*     */ import javassist.CtClass;
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
/*     */ 
/*     */ 
/*     */ public class NewArray
/*     */   extends Expr
/*     */ {
/*     */   int opcode;
/*     */   
/*     */   protected NewArray(int pos, CodeIterator i, CtClass declaring, MethodInfo m, int op) {
/*  50 */     super(pos, i, declaring, m);
/*  51 */     this.opcode = op;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtBehavior where() {
/*  59 */     return super.where();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineNumber() {
/*  69 */     return super.getLineNumber();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/*  79 */     return super.getFileName();
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
/*  90 */     return super.mayThrow();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass getComponentType() throws NotFoundException {
/* 100 */     if (this.opcode == 188) {
/* 101 */       int atype = this.iterator.byteAt(this.currentPos + 1);
/* 102 */       return getPrimitiveType(atype);
/*     */     } 
/* 104 */     if (this.opcode == 189 || this.opcode == 197) {
/*     */       
/* 106 */       int index = this.iterator.u16bitAt(this.currentPos + 1);
/* 107 */       String desc = getConstPool().getClassInfo(index);
/* 108 */       int dim = Descriptor.arrayDimension(desc);
/* 109 */       desc = Descriptor.toArrayComponent(desc, dim);
/* 110 */       return Descriptor.toCtClass(desc, this.thisClass.getClassPool());
/*     */     } 
/*     */     
/* 113 */     throw new RuntimeException("bad opcode: " + this.opcode);
/*     */   }
/*     */   
/*     */   CtClass getPrimitiveType(int atype) {
/* 117 */     switch (atype) {
/*     */       case 4:
/* 119 */         return CtClass.booleanType;
/*     */       case 5:
/* 121 */         return CtClass.charType;
/*     */       case 6:
/* 123 */         return CtClass.floatType;
/*     */       case 7:
/* 125 */         return CtClass.doubleType;
/*     */       case 8:
/* 127 */         return CtClass.byteType;
/*     */       case 9:
/* 129 */         return CtClass.shortType;
/*     */       case 10:
/* 131 */         return CtClass.intType;
/*     */       case 11:
/* 133 */         return CtClass.longType;
/*     */     } 
/* 135 */     throw new RuntimeException("bad atype: " + atype);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDimension() {
/* 143 */     if (this.opcode == 188)
/* 144 */       return 1; 
/* 145 */     if (this.opcode == 189 || this.opcode == 197) {
/*     */       
/* 147 */       int index = this.iterator.u16bitAt(this.currentPos + 1);
/* 148 */       String desc = getConstPool().getClassInfo(index);
/* 149 */       return Descriptor.arrayDimension(desc) + (
/* 150 */         (this.opcode == 189) ? 1 : 0);
/*     */     } 
/*     */     
/* 153 */     throw new RuntimeException("bad opcode: " + this.opcode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCreatedDimensions() {
/* 162 */     if (this.opcode == 197)
/* 163 */       return this.iterator.byteAt(this.currentPos + 3); 
/* 164 */     return 1;
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
/*     */     
/* 180 */     try { replace2(statement); }
/*     */     catch (CompileError e)
/* 182 */     { throw new CannotCompileException(e); }
/* 183 */     catch (NotFoundException e) { throw new CannotCompileException(e); }
/* 184 */     catch (BadBytecode e)
/* 185 */     { throw new CannotCompileException("broken method"); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   private void replace2(String statement) throws CompileError, NotFoundException, BadBytecode, CannotCompileException {
/*     */     int codeLength;
/*     */     String desc;
/* 193 */     this.thisClass.getClassFile();
/* 194 */     ConstPool constPool = getConstPool();
/* 195 */     int pos = this.currentPos;
/*     */ 
/*     */     
/* 198 */     int index = 0;
/* 199 */     int dim = 1;
/*     */     
/* 201 */     if (this.opcode == 188) {
/* 202 */       index = this.iterator.byteAt(this.currentPos + 1);
/* 203 */       CtPrimitiveType cpt = (CtPrimitiveType)getPrimitiveType(index);
/* 204 */       desc = "[" + cpt.getDescriptor();
/* 205 */       codeLength = 2;
/*     */     }
/* 207 */     else if (this.opcode == 189) {
/* 208 */       index = this.iterator.u16bitAt(pos + 1);
/* 209 */       desc = constPool.getClassInfo(index);
/* 210 */       if (desc.startsWith("[")) {
/* 211 */         desc = "[" + desc;
/*     */       } else {
/* 213 */         desc = "[L" + desc + ";";
/*     */       } 
/* 215 */       codeLength = 3;
/*     */     }
/* 217 */     else if (this.opcode == 197) {
/* 218 */       index = this.iterator.u16bitAt(this.currentPos + 1);
/* 219 */       desc = constPool.getClassInfo(index);
/* 220 */       dim = this.iterator.byteAt(this.currentPos + 3);
/* 221 */       codeLength = 4;
/*     */     } else {
/*     */       
/* 224 */       throw new RuntimeException("bad opcode: " + this.opcode);
/*     */     } 
/* 226 */     CtClass retType = Descriptor.toCtClass(desc, this.thisClass.getClassPool());
/*     */     
/* 228 */     Javac jc = new Javac(this.thisClass);
/* 229 */     CodeAttribute ca = this.iterator.get();
/*     */     
/* 231 */     CtClass[] params = new CtClass[dim];
/* 232 */     for (int i = 0; i < dim; i++) {
/* 233 */       params[i] = CtClass.intType;
/*     */     }
/* 235 */     int paramVar = ca.getMaxLocals();
/* 236 */     jc.recordParams("java.lang.Object", params, true, paramVar, 
/* 237 */         withinStatic());
/*     */ 
/*     */ 
/*     */     
/* 241 */     checkResultValue(retType, statement);
/* 242 */     int retVar = jc.recordReturnType(retType, true);
/* 243 */     jc.recordProceed(new ProceedForArray(retType, this.opcode, index, dim));
/*     */     
/* 245 */     Bytecode bytecode = jc.getBytecode();
/* 246 */     storeStack(params, true, paramVar, bytecode);
/* 247 */     jc.recordLocalVariables(ca, pos);
/*     */     
/* 249 */     bytecode.addOpcode(1);
/* 250 */     bytecode.addAstore(retVar);
/*     */     
/* 252 */     jc.compileStmnt(statement);
/* 253 */     bytecode.addAload(retVar);
/*     */     
/* 255 */     replace0(pos, bytecode, codeLength);
/*     */   }
/*     */   
/*     */   static class ProceedForArray
/*     */     implements ProceedHandler {
/*     */     CtClass arrayType;
/*     */     int opcode;
/*     */     int index;
/*     */     int dimension;
/*     */     
/*     */     ProceedForArray(CtClass type, int op, int i, int dim) {
/* 266 */       this.arrayType = type;
/* 267 */       this.opcode = op;
/* 268 */       this.index = i;
/* 269 */       this.dimension = dim;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void doit(JvstCodeGen gen, Bytecode bytecode, ASTList args) throws CompileError {
/* 276 */       int num = gen.getMethodArgsLength(args);
/* 277 */       if (num != this.dimension) {
/* 278 */         throw new CompileError("$proceed() with a wrong number of parameters");
/*     */       }
/*     */       
/* 281 */       gen.atMethodArgs(args, new int[num], new int[num], new String[num]);
/*     */       
/* 283 */       bytecode.addOpcode(this.opcode);
/* 284 */       if (this.opcode == 189) {
/* 285 */         bytecode.addIndex(this.index);
/* 286 */       } else if (this.opcode == 188) {
/* 287 */         bytecode.add(this.index);
/*     */       } else {
/* 289 */         bytecode.addIndex(this.index);
/* 290 */         bytecode.add(this.dimension);
/* 291 */         bytecode.growStack(1 - this.dimension);
/*     */       } 
/*     */       
/* 294 */       gen.setType(this.arrayType);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setReturnType(JvstTypeChecker c, ASTList args) throws CompileError {
/* 301 */       c.setType(this.arrayType);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\expr\NewArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */