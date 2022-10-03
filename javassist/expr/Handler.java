/*     */ package javassist.expr;
/*     */ 
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.CtBehavior;
/*     */ import javassist.CtClass;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.ExceptionTable;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ import javassist.compiler.CompileError;
/*     */ import javassist.compiler.Javac;
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
/*     */ public class Handler
/*     */   extends Expr
/*     */ {
/*  37 */   private static String EXCEPTION_NAME = "$1";
/*     */ 
/*     */   
/*     */   private ExceptionTable etable;
/*     */   
/*     */   private int index;
/*     */ 
/*     */   
/*     */   protected Handler(ExceptionTable et, int nth, CodeIterator it, CtClass declaring, MethodInfo m) {
/*  46 */     super(et.handlerPc(nth), it, declaring, m);
/*  47 */     this.etable = et;
/*  48 */     this.index = nth;
/*     */   }
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
/*     */   public CtClass[] mayThrow() {
/*  82 */     return super.mayThrow();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass getType() throws NotFoundException {
/*  90 */     int type = this.etable.catchType(this.index);
/*  91 */     if (type == 0)
/*  92 */       return null; 
/*  93 */     ConstPool cp = getConstPool();
/*  94 */     String name = cp.getClassInfo(type);
/*  95 */     return this.thisClass.getClassPool().getCtClass(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFinally() {
/* 102 */     return (this.etable.catchType(this.index) == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void replace(String statement) throws CannotCompileException {
/* 112 */     throw new RuntimeException("not implemented yet");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void insertBefore(String src) throws CannotCompileException {
/* 123 */     this.edited = true;
/*     */ 
/*     */     
/* 126 */     ConstPool cp = getConstPool();
/* 127 */     CodeAttribute ca = this.iterator.get();
/* 128 */     Javac jv = new Javac(this.thisClass);
/* 129 */     Bytecode b = jv.getBytecode();
/* 130 */     b.setStackDepth(1);
/* 131 */     b.setMaxLocals(ca.getMaxLocals());
/*     */     
/*     */     try {
/* 134 */       CtClass type = getType();
/* 135 */       int var = jv.recordVariable(type, EXCEPTION_NAME);
/* 136 */       jv.recordReturnType(type, false);
/* 137 */       b.addAstore(var);
/* 138 */       jv.compileStmnt(src);
/* 139 */       b.addAload(var);
/*     */       
/* 141 */       int oldHandler = this.etable.handlerPc(this.index);
/* 142 */       b.addOpcode(167);
/* 143 */       b.addIndex(oldHandler - this.iterator.getCodeLength() - b
/* 144 */           .currentPc() + 1);
/*     */       
/* 146 */       this.maxStack = b.getMaxStack();
/* 147 */       this.maxLocals = b.getMaxLocals();
/*     */       
/* 149 */       int pos = this.iterator.append(b.get());
/* 150 */       this.iterator.append(b.getExceptionTable(), pos);
/* 151 */       this.etable.setHandlerPc(this.index, pos);
/*     */     }
/* 153 */     catch (NotFoundException e) {
/* 154 */       throw new CannotCompileException(e);
/*     */     }
/* 156 */     catch (CompileError e) {
/* 157 */       throw new CannotCompileException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\expr\Handler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */