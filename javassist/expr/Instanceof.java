/*     */ package javassist.expr;
/*     */ 
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtBehavior;
/*     */ import javassist.CtClass;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ConstPool;
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
/*     */ public class Instanceof
/*     */   extends Expr
/*     */ {
/*     */   protected Instanceof(int pos, CodeIterator i, CtClass declaring, MethodInfo m) {
/*  47 */     super(pos, i, declaring, m);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtBehavior where() {
/*  55 */     return super.where();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineNumber() {
/*  65 */     return super.getLineNumber();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/*  76 */     return super.getFileName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass getType() throws NotFoundException {
/*  85 */     ConstPool cp = getConstPool();
/*  86 */     int pos = this.currentPos;
/*  87 */     int index = this.iterator.u16bitAt(pos + 1);
/*  88 */     String name = cp.getClassInfo(index);
/*  89 */     return this.thisClass.getClassPool().getCtClass(name);
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
/* 100 */     return super.mayThrow();
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
/*     */   public void replace(String statement) throws CannotCompileException {
/* 113 */     this.thisClass.getClassFile();
/*     */     
/* 115 */     ConstPool constPool = getConstPool();
/* 116 */     int pos = this.currentPos;
/* 117 */     int index = this.iterator.u16bitAt(pos + 1);
/*     */     
/* 119 */     Javac jc = new Javac(this.thisClass);
/* 120 */     ClassPool cp = this.thisClass.getClassPool();
/* 121 */     CodeAttribute ca = this.iterator.get();
/*     */ 
/*     */ 
/*     */     
/* 125 */     try { CtClass[] params = { cp.get("java.lang.Object") };
/* 126 */       CtClass retType = CtClass.booleanType;
/*     */       
/* 128 */       int paramVar = ca.getMaxLocals();
/* 129 */       jc.recordParams("java.lang.Object", params, true, paramVar, 
/* 130 */           withinStatic());
/* 131 */       int retVar = jc.recordReturnType(retType, true);
/* 132 */       jc.recordProceed(new ProceedForInstanceof(index));
/*     */ 
/*     */       
/* 135 */       jc.recordType(getType());
/*     */ 
/*     */ 
/*     */       
/* 139 */       checkResultValue(retType, statement);
/*     */       
/* 141 */       Bytecode bytecode = jc.getBytecode();
/* 142 */       storeStack(params, true, paramVar, bytecode);
/* 143 */       jc.recordLocalVariables(ca, pos);
/*     */       
/* 145 */       bytecode.addConstZero(retType);
/* 146 */       bytecode.addStore(retVar, retType);
/*     */       
/* 148 */       jc.compileStmnt(statement);
/* 149 */       bytecode.addLoad(retVar, retType);
/*     */       
/* 151 */       replace0(pos, bytecode, 3); }
/*     */     catch (CompileError e)
/* 153 */     { throw new CannotCompileException(e); }
/* 154 */     catch (NotFoundException e) { throw new CannotCompileException(e); }
/* 155 */     catch (BadBytecode e)
/* 156 */     { throw new CannotCompileException("broken method"); }
/*     */   
/*     */   }
/*     */   
/*     */   static class ProceedForInstanceof
/*     */     implements ProceedHandler
/*     */   {
/*     */     int index;
/*     */     
/*     */     ProceedForInstanceof(int i) {
/* 166 */       this.index = i;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void doit(JvstCodeGen gen, Bytecode bytecode, ASTList args) throws CompileError {
/* 173 */       if (gen.getMethodArgsLength(args) != 1) {
/* 174 */         throw new CompileError("$proceed() cannot take more than one parameter for instanceof");
/*     */       }
/*     */ 
/*     */       
/* 178 */       gen.atMethodArgs(args, new int[1], new int[1], new String[1]);
/* 179 */       bytecode.addOpcode(193);
/* 180 */       bytecode.addIndex(this.index);
/* 181 */       gen.setType(CtClass.booleanType);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setReturnType(JvstTypeChecker c, ASTList args) throws CompileError {
/* 188 */       c.atMethodArgs(args, new int[1], new int[1], new String[1]);
/* 189 */       c.setType(CtClass.booleanType);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\expr\Instanceof.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */