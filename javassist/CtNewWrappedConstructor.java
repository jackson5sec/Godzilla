/*     */ package javassist;
/*     */ 
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.ClassFile;
/*     */ import javassist.bytecode.Descriptor;
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
/*     */ class CtNewWrappedConstructor
/*     */   extends CtNewWrappedMethod
/*     */ {
/*     */   private static final int PASS_NONE = 0;
/*     */   private static final int PASS_PARAMS = 2;
/*     */   
/*     */   public static CtConstructor wrapped(CtClass[] parameterTypes, CtClass[] exceptionTypes, int howToCallSuper, CtMethod body, CtMethod.ConstParameter constParam, CtClass declaring) throws CannotCompileException {
/*     */     try {
/*  38 */       CtConstructor cons = new CtConstructor(parameterTypes, declaring);
/*  39 */       cons.setExceptionTypes(exceptionTypes);
/*  40 */       Bytecode code = makeBody(declaring, declaring.getClassFile2(), howToCallSuper, body, parameterTypes, constParam);
/*     */ 
/*     */       
/*  43 */       cons.getMethodInfo2().setCodeAttribute(code.toCodeAttribute());
/*     */       
/*  45 */       return cons;
/*     */     }
/*  47 */     catch (NotFoundException e) {
/*  48 */       throw new CannotCompileException(e);
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
/*     */   protected static Bytecode makeBody(CtClass declaring, ClassFile classfile, int howToCallSuper, CtMethod wrappedBody, CtClass[] parameters, CtMethod.ConstParameter cparam) throws CannotCompileException {
/*  61 */     int stacksize, superclazz = classfile.getSuperclassId();
/*  62 */     Bytecode code = new Bytecode(classfile.getConstPool(), 0, 0);
/*  63 */     code.setMaxLocals(false, parameters, 0);
/*  64 */     code.addAload(0);
/*  65 */     if (howToCallSuper == 0) {
/*  66 */       stacksize = 1;
/*  67 */       code.addInvokespecial(superclazz, "<init>", "()V");
/*     */     }
/*  69 */     else if (howToCallSuper == 2) {
/*  70 */       stacksize = code.addLoadParameters(parameters, 1) + 1;
/*  71 */       code.addInvokespecial(superclazz, "<init>", 
/*  72 */           Descriptor.ofConstructor(parameters));
/*     */     } else {
/*     */       int stacksize2; String desc;
/*  75 */       stacksize = compileParameterList(code, parameters, 1);
/*     */       
/*  77 */       if (cparam == null) {
/*  78 */         stacksize2 = 2;
/*  79 */         desc = CtMethod.ConstParameter.defaultConstDescriptor();
/*     */       } else {
/*     */         
/*  82 */         stacksize2 = cparam.compile(code) + 2;
/*  83 */         desc = cparam.constDescriptor();
/*     */       } 
/*     */       
/*  86 */       if (stacksize < stacksize2) {
/*  87 */         stacksize = stacksize2;
/*     */       }
/*  89 */       code.addInvokespecial(superclazz, "<init>", desc);
/*     */     } 
/*     */     
/*  92 */     if (wrappedBody == null) {
/*  93 */       code.add(177);
/*     */     } else {
/*  95 */       int stacksize2 = makeBody0(declaring, classfile, wrappedBody, false, parameters, CtClass.voidType, cparam, code);
/*     */ 
/*     */       
/*  98 */       if (stacksize < stacksize2) {
/*  99 */         stacksize = stacksize2;
/*     */       }
/*     */     } 
/* 102 */     code.setMaxStack(stacksize);
/* 103 */     return code;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\CtNewWrappedConstructor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */