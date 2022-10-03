/*     */ package javassist.expr;
/*     */ 
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtBehavior;
/*     */ import javassist.CtClass;
/*     */ import javassist.CtMethod;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodCall
/*     */   extends Expr
/*     */ {
/*     */   protected MethodCall(int pos, CodeIterator i, CtClass declaring, MethodInfo m) {
/*  44 */     super(pos, i, declaring, m);
/*     */   }
/*     */   
/*     */   private int getNameAndType(ConstPool cp) {
/*  48 */     int pos = this.currentPos;
/*  49 */     int c = this.iterator.byteAt(pos);
/*  50 */     int index = this.iterator.u16bitAt(pos + 1);
/*     */     
/*  52 */     if (c == 185)
/*  53 */       return cp.getInterfaceMethodrefNameAndType(index); 
/*  54 */     return cp.getMethodrefNameAndType(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtBehavior where() {
/*  62 */     return super.where();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLineNumber() {
/*  72 */     return super.getLineNumber();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFileName() {
/*  82 */     return super.getFileName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CtClass getCtClass() throws NotFoundException {
/*  90 */     return this.thisClass.getClassPool().get(getClassName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getClassName() {
/*     */     String cname;
/* 100 */     ConstPool cp = getConstPool();
/* 101 */     int pos = this.currentPos;
/* 102 */     int c = this.iterator.byteAt(pos);
/* 103 */     int index = this.iterator.u16bitAt(pos + 1);
/*     */     
/* 105 */     if (c == 185) {
/* 106 */       cname = cp.getInterfaceMethodrefClassName(index);
/*     */     } else {
/* 108 */       cname = cp.getMethodrefClassName(index);
/*     */     } 
/* 110 */     if (cname.charAt(0) == '[') {
/* 111 */       cname = Descriptor.toClassName(cname);
/*     */     }
/* 113 */     return cname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodName() {
/* 120 */     ConstPool cp = getConstPool();
/* 121 */     int nt = getNameAndType(cp);
/* 122 */     return cp.getUtf8Info(cp.getNameAndTypeName(nt));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtMethod getMethod() throws NotFoundException {
/* 129 */     return getCtClass().getMethod(getMethodName(), getSignature());
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
/* 143 */     ConstPool cp = getConstPool();
/* 144 */     int nt = getNameAndType(cp);
/* 145 */     return cp.getUtf8Info(cp.getNameAndTypeDescriptor(nt));
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
/* 156 */     return super.mayThrow();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSuper() {
/* 164 */     return (this.iterator.byteAt(this.currentPos) == 183 && 
/* 165 */       !where().getDeclaringClass().getName().equals(getClassName()));
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
/*     */   public void replace(String statement) throws CannotCompileException {
/*     */     String classname, methodname, signature;
/*     */     int opcodeSize;
/* 196 */     this.thisClass.getClassFile();
/* 197 */     ConstPool constPool = getConstPool();
/* 198 */     int pos = this.currentPos;
/* 199 */     int index = this.iterator.u16bitAt(pos + 1);
/*     */ 
/*     */ 
/*     */     
/* 203 */     int c = this.iterator.byteAt(pos);
/* 204 */     if (c == 185) {
/* 205 */       opcodeSize = 5;
/* 206 */       classname = constPool.getInterfaceMethodrefClassName(index);
/* 207 */       methodname = constPool.getInterfaceMethodrefName(index);
/* 208 */       signature = constPool.getInterfaceMethodrefType(index);
/*     */     }
/* 210 */     else if (c == 184 || c == 183 || c == 182) {
/*     */       
/* 212 */       opcodeSize = 3;
/* 213 */       classname = constPool.getMethodrefClassName(index);
/* 214 */       methodname = constPool.getMethodrefName(index);
/* 215 */       signature = constPool.getMethodrefType(index);
/*     */     } else {
/*     */       
/* 218 */       throw new CannotCompileException("not method invocation");
/*     */     } 
/* 220 */     Javac jc = new Javac(this.thisClass);
/* 221 */     ClassPool cp = this.thisClass.getClassPool();
/* 222 */     CodeAttribute ca = this.iterator.get();
/*     */     
/* 224 */     try { CtClass[] params = Descriptor.getParameterTypes(signature, cp);
/* 225 */       CtClass retType = Descriptor.getReturnType(signature, cp);
/* 226 */       int paramVar = ca.getMaxLocals();
/* 227 */       jc.recordParams(classname, params, true, paramVar, 
/* 228 */           withinStatic());
/* 229 */       int retVar = jc.recordReturnType(retType, true);
/* 230 */       if (c == 184) {
/* 231 */         jc.recordStaticProceed(classname, methodname);
/* 232 */       } else if (c == 183) {
/* 233 */         jc.recordSpecialProceed("$0", classname, methodname, signature, index);
/*     */       } else {
/*     */         
/* 236 */         jc.recordProceed("$0", methodname);
/*     */       } 
/*     */ 
/*     */       
/* 240 */       checkResultValue(retType, statement);
/*     */       
/* 242 */       Bytecode bytecode = jc.getBytecode();
/* 243 */       storeStack(params, (c == 184), paramVar, bytecode);
/* 244 */       jc.recordLocalVariables(ca, pos);
/*     */       
/* 246 */       if (retType != CtClass.voidType) {
/* 247 */         bytecode.addConstZero(retType);
/* 248 */         bytecode.addStore(retVar, retType);
/*     */       } 
/*     */       
/* 251 */       jc.compileStmnt(statement);
/* 252 */       if (retType != CtClass.voidType) {
/* 253 */         bytecode.addLoad(retVar, retType);
/*     */       }
/* 255 */       replace0(pos, bytecode, opcodeSize); }
/*     */     catch (CompileError e)
/* 257 */     { throw new CannotCompileException(e); }
/* 258 */     catch (NotFoundException e) { throw new CannotCompileException(e); }
/* 259 */     catch (BadBytecode e)
/* 260 */     { throw new CannotCompileException("broken method"); }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\expr\MethodCall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */