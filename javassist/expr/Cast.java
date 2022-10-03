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
/*     */ public class Cast
/*     */   extends Expr
/*     */ {
/*     */   protected Cast(int pos, CodeIterator i, CtClass declaring, MethodInfo m) {
/*  46 */     super(pos, i, declaring, m);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtBehavior where() {
/*  54 */     return super.where();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineNumber() {
/*  64 */     return super.getLineNumber();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/*  74 */     return super.getFileName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass getType() throws NotFoundException {
/*  82 */     ConstPool cp = getConstPool();
/*  83 */     int pos = this.currentPos;
/*  84 */     int index = this.iterator.u16bitAt(pos + 1);
/*  85 */     String name = cp.getClassInfo(index);
/*  86 */     return this.thisClass.getClassPool().getCtClass(name);
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
/*  97 */     return super.mayThrow();
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
/* 110 */     this.thisClass.getClassFile();
/*     */     
/* 112 */     ConstPool constPool = getConstPool();
/* 113 */     int pos = this.currentPos;
/* 114 */     int index = this.iterator.u16bitAt(pos + 1);
/*     */     
/* 116 */     Javac jc = new Javac(this.thisClass);
/* 117 */     ClassPool cp = this.thisClass.getClassPool();
/* 118 */     CodeAttribute ca = this.iterator.get();
/*     */ 
/*     */ 
/*     */     
/* 122 */     try { CtClass[] params = { cp.get("java.lang.Object") };
/* 123 */       CtClass retType = getType();
/*     */       
/* 125 */       int paramVar = ca.getMaxLocals();
/* 126 */       jc.recordParams("java.lang.Object", params, true, paramVar, 
/* 127 */           withinStatic());
/* 128 */       int retVar = jc.recordReturnType(retType, true);
/* 129 */       jc.recordProceed(new ProceedForCast(index, retType));
/*     */ 
/*     */ 
/*     */       
/* 133 */       checkResultValue(retType, statement);
/*     */       
/* 135 */       Bytecode bytecode = jc.getBytecode();
/* 136 */       storeStack(params, true, paramVar, bytecode);
/* 137 */       jc.recordLocalVariables(ca, pos);
/*     */       
/* 139 */       bytecode.addConstZero(retType);
/* 140 */       bytecode.addStore(retVar, retType);
/*     */       
/* 142 */       jc.compileStmnt(statement);
/* 143 */       bytecode.addLoad(retVar, retType);
/*     */       
/* 145 */       replace0(pos, bytecode, 3); }
/*     */     catch (CompileError e)
/* 147 */     { throw new CannotCompileException(e); }
/* 148 */     catch (NotFoundException e) { throw new CannotCompileException(e); }
/* 149 */     catch (BadBytecode e)
/* 150 */     { throw new CannotCompileException("broken method"); }
/*     */   
/*     */   }
/*     */   
/*     */   static class ProceedForCast
/*     */     implements ProceedHandler
/*     */   {
/*     */     int index;
/*     */     CtClass retType;
/*     */     
/*     */     ProceedForCast(int i, CtClass t) {
/* 161 */       this.index = i;
/* 162 */       this.retType = t;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void doit(JvstCodeGen gen, Bytecode bytecode, ASTList args) throws CompileError {
/* 169 */       if (gen.getMethodArgsLength(args) != 1) {
/* 170 */         throw new CompileError("$proceed() cannot take more than one parameter for cast");
/*     */       }
/*     */ 
/*     */       
/* 174 */       gen.atMethodArgs(args, new int[1], new int[1], new String[1]);
/* 175 */       bytecode.addOpcode(192);
/* 176 */       bytecode.addIndex(this.index);
/* 177 */       gen.setType(this.retType);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setReturnType(JvstTypeChecker c, ASTList args) throws CompileError {
/* 184 */       c.atMethodArgs(args, new int[1], new int[1], new String[1]);
/* 185 */       c.setType(this.retType);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\expr\Cast.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */