/*     */ package javassist.expr;
/*     */ 
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtBehavior;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtConstructor;
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
/*     */ public class NewExpr
/*     */   extends Expr
/*     */ {
/*     */   String newTypeName;
/*     */   int newPos;
/*     */   
/*     */   protected NewExpr(int pos, CodeIterator i, CtClass declaring, MethodInfo m, String type, int np) {
/*  52 */     super(pos, i, declaring, m);
/*  53 */     this.newTypeName = type;
/*  54 */     this.newPos = np;
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
/*     */   public CtBehavior where() {
/*  76 */     return super.where();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineNumber() {
/*  86 */     return super.getLineNumber();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/*  96 */     return super.getFileName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CtClass getCtClass() throws NotFoundException {
/* 103 */     return this.thisClass.getClassPool().get(this.newTypeName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/* 110 */     return this.newTypeName;
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
/*     */   public String getSignature() {
/* 124 */     ConstPool constPool = getConstPool();
/* 125 */     int methodIndex = this.iterator.u16bitAt(this.currentPos + 1);
/* 126 */     return constPool.getMethodrefType(methodIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtConstructor getConstructor() throws NotFoundException {
/* 133 */     ConstPool cp = getConstPool();
/* 134 */     int index = this.iterator.u16bitAt(this.currentPos + 1);
/* 135 */     String desc = cp.getMethodrefType(index);
/* 136 */     return getCtClass().getConstructor(desc);
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
/*     */ 
/*     */ 
/*     */   
/*     */   private int canReplace() throws CannotCompileException {
/* 162 */     int op = this.iterator.byteAt(this.newPos + 3);
/* 163 */     if (op == 89)
/* 164 */       return (this.iterator.byteAt(this.newPos + 4) == 94 && this.iterator
/* 165 */         .byteAt(this.newPos + 5) == 88) ? 6 : 4; 
/* 166 */     if (op == 90 && this.iterator
/* 167 */       .byteAt(this.newPos + 4) == 95) {
/* 168 */       return 5;
/*     */     }
/* 170 */     return 3;
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
/* 185 */     this.thisClass.getClassFile();
/*     */     
/* 187 */     int bytecodeSize = 3;
/* 188 */     int pos = this.newPos;
/*     */     
/* 190 */     int newIndex = this.iterator.u16bitAt(pos + 1);
/*     */ 
/*     */ 
/*     */     
/* 194 */     int codeSize = canReplace();
/* 195 */     int end = pos + codeSize;
/* 196 */     for (int i = pos; i < end; i++) {
/* 197 */       this.iterator.writeByte(0, i);
/*     */     }
/* 199 */     ConstPool constPool = getConstPool();
/* 200 */     pos = this.currentPos;
/* 201 */     int methodIndex = this.iterator.u16bitAt(pos + 1);
/*     */     
/* 203 */     String signature = constPool.getMethodrefType(methodIndex);
/*     */     
/* 205 */     Javac jc = new Javac(this.thisClass);
/* 206 */     ClassPool cp = this.thisClass.getClassPool();
/* 207 */     CodeAttribute ca = this.iterator.get();
/*     */     
/* 209 */     try { CtClass[] params = Descriptor.getParameterTypes(signature, cp);
/* 210 */       CtClass newType = cp.get(this.newTypeName);
/* 211 */       int paramVar = ca.getMaxLocals();
/* 212 */       jc.recordParams(this.newTypeName, params, true, paramVar, 
/* 213 */           withinStatic());
/* 214 */       int retVar = jc.recordReturnType(newType, true);
/* 215 */       jc.recordProceed(new ProceedForNew(newType, newIndex, methodIndex));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 220 */       checkResultValue(newType, statement);
/*     */       
/* 222 */       Bytecode bytecode = jc.getBytecode();
/* 223 */       storeStack(params, true, paramVar, bytecode);
/* 224 */       jc.recordLocalVariables(ca, pos);
/*     */       
/* 226 */       bytecode.addConstZero(newType);
/* 227 */       bytecode.addStore(retVar, newType);
/*     */       
/* 229 */       jc.compileStmnt(statement);
/* 230 */       if (codeSize > 3) {
/* 231 */         bytecode.addAload(retVar);
/*     */       }
/* 233 */       replace0(pos, bytecode, 3); }
/*     */     catch (CompileError e)
/* 235 */     { throw new CannotCompileException(e); }
/* 236 */     catch (NotFoundException e) { throw new CannotCompileException(e); }
/* 237 */     catch (BadBytecode e)
/* 238 */     { throw new CannotCompileException("broken method"); }
/*     */   
/*     */   }
/*     */   
/*     */   static class ProceedForNew implements ProceedHandler { CtClass newType;
/*     */     int newIndex;
/*     */     int methodIndex;
/*     */     
/*     */     ProceedForNew(CtClass nt, int ni, int mi) {
/* 247 */       this.newType = nt;
/* 248 */       this.newIndex = ni;
/* 249 */       this.methodIndex = mi;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void doit(JvstCodeGen gen, Bytecode bytecode, ASTList args) throws CompileError {
/* 256 */       bytecode.addOpcode(187);
/* 257 */       bytecode.addIndex(this.newIndex);
/* 258 */       bytecode.addOpcode(89);
/* 259 */       gen.atMethodCallCore(this.newType, "<init>", args, false, true, -1, null);
/*     */       
/* 261 */       gen.setType(this.newType);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setReturnType(JvstTypeChecker c, ASTList args) throws CompileError {
/* 268 */       c.atMethodCallCore(this.newType, "<init>", args);
/* 269 */       c.setType(this.newType);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\expr\NewExpr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */