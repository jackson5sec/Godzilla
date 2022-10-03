/*     */ package javassist.expr;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtBehavior;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtConstructor;
/*     */ import javassist.CtPrimitiveType;
/*     */ import javassist.NotFoundException;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.ClassFile;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.ExceptionTable;
/*     */ import javassist.bytecode.ExceptionsAttribute;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ import javassist.bytecode.Opcode;
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
/*     */ public abstract class Expr
/*     */   implements Opcode
/*     */ {
/*     */   int currentPos;
/*     */   CodeIterator iterator;
/*     */   CtClass thisClass;
/*     */   MethodInfo thisMethod;
/*     */   boolean edited;
/*     */   int maxLocals;
/*     */   int maxStack;
/*     */   static final String javaLangObject = "java.lang.Object";
/*     */   
/*     */   protected Expr(int pos, CodeIterator i, CtClass declaring, MethodInfo m) {
/*  59 */     this.currentPos = pos;
/*  60 */     this.iterator = i;
/*  61 */     this.thisClass = declaring;
/*  62 */     this.thisMethod = m;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass getEnclosingClass() {
/*  71 */     return this.thisClass;
/*     */   }
/*     */   protected final ConstPool getConstPool() {
/*  74 */     return this.thisMethod.getConstPool();
/*     */   }
/*     */   
/*     */   protected final boolean edited() {
/*  78 */     return this.edited;
/*     */   }
/*     */   
/*     */   protected final int locals() {
/*  82 */     return this.maxLocals;
/*     */   }
/*     */   
/*     */   protected final int stack() {
/*  86 */     return this.maxStack;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean withinStatic() {
/*  93 */     return ((this.thisMethod.getAccessFlags() & 0x8) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtBehavior where() {
/* 100 */     MethodInfo mi = this.thisMethod;
/* 101 */     CtBehavior[] cb = this.thisClass.getDeclaredBehaviors();
/* 102 */     for (int i = cb.length - 1; i >= 0; i--) {
/* 103 */       if (cb[i].getMethodInfo2() == mi)
/* 104 */         return cb[i]; 
/*     */     } 
/* 106 */     CtConstructor init = this.thisClass.getClassInitializer();
/* 107 */     if (init != null && init.getMethodInfo2() == mi) {
/* 108 */       return (CtBehavior)init;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     for (int j = cb.length - 1; j >= 0; j--) {
/* 116 */       if (this.thisMethod.getName().equals(cb[j].getMethodInfo2().getName()) && this.thisMethod
/* 117 */         .getDescriptor()
/* 118 */         .equals(cb[j].getMethodInfo2().getDescriptor())) {
/* 119 */         return cb[j];
/*     */       }
/*     */     } 
/*     */     
/* 123 */     throw new RuntimeException("fatal: not found");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass[] mayThrow() {
/* 133 */     ClassPool pool = this.thisClass.getClassPool();
/* 134 */     ConstPool cp = this.thisMethod.getConstPool();
/* 135 */     List<CtClass> list = new LinkedList<>();
/*     */     try {
/* 137 */       CodeAttribute ca = this.thisMethod.getCodeAttribute();
/* 138 */       ExceptionTable et = ca.getExceptionTable();
/* 139 */       int pos = this.currentPos;
/* 140 */       int n = et.size();
/* 141 */       for (int i = 0; i < n; i++) {
/* 142 */         if (et.startPc(i) <= pos && pos < et.endPc(i)) {
/* 143 */           int t = et.catchType(i);
/* 144 */           if (t > 0) {
/*     */             try {
/* 146 */               addClass(list, pool.get(cp.getClassInfo(t)));
/*     */             }
/* 148 */             catch (NotFoundException notFoundException) {}
/*     */           }
/*     */         } 
/*     */       } 
/* 152 */     } catch (NullPointerException nullPointerException) {}
/*     */ 
/*     */     
/* 155 */     ExceptionsAttribute ea = this.thisMethod.getExceptionsAttribute();
/* 156 */     if (ea != null) {
/* 157 */       String[] exceptions = ea.getExceptions();
/* 158 */       if (exceptions != null) {
/* 159 */         int n = exceptions.length;
/* 160 */         for (int i = 0; i < n; i++) {
/*     */           try {
/* 162 */             addClass(list, pool.get(exceptions[i]));
/*     */           }
/* 164 */           catch (NotFoundException notFoundException) {}
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 169 */     return list.<CtClass>toArray(new CtClass[list.size()]);
/*     */   }
/*     */   
/*     */   private static void addClass(List<CtClass> list, CtClass c) {
/* 173 */     if (list.contains(c)) {
/*     */       return;
/*     */     }
/* 176 */     list.add(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOfBytecode() {
/* 185 */     return this.currentPos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineNumber() {
/* 194 */     return this.thisMethod.getLineNumber(this.currentPos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/* 203 */     ClassFile cf = this.thisClass.getClassFile2();
/* 204 */     if (cf == null)
/* 205 */       return null; 
/* 206 */     return cf.getSourceFile();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final boolean checkResultValue(CtClass retType, String prog) throws CannotCompileException {
/* 214 */     boolean hasIt = (prog.indexOf("$_") >= 0);
/* 215 */     if (!hasIt && retType != CtClass.voidType) {
/* 216 */       throw new CannotCompileException("the resulting value is not stored in $_");
/*     */     }
/*     */ 
/*     */     
/* 220 */     return hasIt;
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
/*     */   static final void storeStack(CtClass[] params, boolean isStaticCall, int regno, Bytecode bytecode) {
/* 232 */     storeStack0(0, params.length, params, regno + 1, bytecode);
/* 233 */     if (isStaticCall) {
/* 234 */       bytecode.addOpcode(1);
/*     */     }
/* 236 */     bytecode.addAstore(regno);
/*     */   }
/*     */   
/*     */   private static void storeStack0(int i, int n, CtClass[] params, int regno, Bytecode bytecode) {
/*     */     int size;
/* 241 */     if (i >= n)
/*     */       return; 
/* 243 */     CtClass c = params[i];
/*     */     
/* 245 */     if (c instanceof CtPrimitiveType) {
/* 246 */       size = ((CtPrimitiveType)c).getDataSize();
/*     */     } else {
/* 248 */       size = 1;
/*     */     } 
/* 250 */     storeStack0(i + 1, n, params, regno + size, bytecode);
/* 251 */     bytecode.addStore(regno, c);
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
/*     */   public abstract void replace(String paramString) throws CannotCompileException;
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
/*     */   public void replace(String statement, ExprEditor recursive) throws CannotCompileException {
/* 280 */     replace(statement);
/* 281 */     if (recursive != null) {
/* 282 */       runEditor(recursive, this.iterator);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void replace0(int pos, Bytecode bytecode, int size) throws BadBytecode {
/* 287 */     byte[] code = bytecode.get();
/* 288 */     this.edited = true;
/* 289 */     int gap = code.length - size;
/* 290 */     for (int i = 0; i < size; i++) {
/* 291 */       this.iterator.writeByte(0, pos + i);
/*     */     }
/* 293 */     if (gap > 0) {
/* 294 */       pos = (this.iterator.insertGapAt(pos, gap, false)).position;
/*     */     }
/* 296 */     this.iterator.write(code, pos);
/* 297 */     this.iterator.insert(bytecode.getExceptionTable(), pos);
/* 298 */     this.maxLocals = bytecode.getMaxLocals();
/* 299 */     this.maxStack = bytecode.getMaxStack();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void runEditor(ExprEditor ed, CodeIterator oldIterator) throws CannotCompileException {
/* 305 */     CodeAttribute codeAttr = oldIterator.get();
/* 306 */     int orgLocals = codeAttr.getMaxLocals();
/* 307 */     int orgStack = codeAttr.getMaxStack();
/* 308 */     int newLocals = locals();
/* 309 */     codeAttr.setMaxStack(stack());
/* 310 */     codeAttr.setMaxLocals(newLocals);
/* 311 */     ExprEditor.LoopContext context = new ExprEditor.LoopContext(newLocals);
/*     */     
/* 313 */     int size = oldIterator.getCodeLength();
/* 314 */     int endPos = oldIterator.lookAhead();
/* 315 */     oldIterator.move(this.currentPos);
/* 316 */     if (ed.doit(this.thisClass, this.thisMethod, context, oldIterator, endPos)) {
/* 317 */       this.edited = true;
/*     */     }
/* 319 */     oldIterator.move(endPos + oldIterator.getCodeLength() - size);
/* 320 */     codeAttr.setMaxLocals(orgLocals);
/* 321 */     codeAttr.setMaxStack(orgStack);
/* 322 */     this.maxLocals = context.maxLocals;
/* 323 */     this.maxStack += context.maxStack;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\expr\Expr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */