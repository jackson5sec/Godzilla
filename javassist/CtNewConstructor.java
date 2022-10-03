/*     */ package javassist;
/*     */ 
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.ConstPool;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CtNewConstructor
/*     */ {
/*     */   public static final int PASS_NONE = 0;
/*     */   public static final int PASS_ARRAY = 1;
/*     */   public static final int PASS_PARAMS = 2;
/*     */   
/*     */   public static CtConstructor make(String src, CtClass declaring) throws CannotCompileException {
/*  69 */     Javac compiler = new Javac(declaring);
/*     */     try {
/*  71 */       CtMember obj = compiler.compile(src);
/*  72 */       if (obj instanceof CtConstructor)
/*     */       {
/*  74 */         return (CtConstructor)obj;
/*     */       }
/*     */     }
/*  77 */     catch (CompileError e) {
/*  78 */       throw new CannotCompileException(e);
/*     */     } 
/*     */     
/*  81 */     throw new CannotCompileException("not a constructor");
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
/*     */   public static CtConstructor make(CtClass[] parameters, CtClass[] exceptions, String body, CtClass declaring) throws CannotCompileException {
/*     */     try {
/* 102 */       CtConstructor cc = new CtConstructor(parameters, declaring);
/* 103 */       cc.setExceptionTypes(exceptions);
/* 104 */       cc.setBody(body);
/* 105 */       return cc;
/*     */     }
/* 107 */     catch (NotFoundException e) {
/* 108 */       throw new CannotCompileException(e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CtConstructor copy(CtConstructor c, CtClass declaring, ClassMap map) throws CannotCompileException {
/* 128 */     return new CtConstructor(c, declaring, map);
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
/*     */   public static CtConstructor defaultConstructor(CtClass declaring) throws CannotCompileException {
/* 140 */     CtConstructor cons = new CtConstructor((CtClass[])null, declaring);
/*     */     
/* 142 */     ConstPool cp = declaring.getClassFile2().getConstPool();
/* 143 */     Bytecode code = new Bytecode(cp, 1, 1);
/* 144 */     code.addAload(0);
/*     */     try {
/* 146 */       code.addInvokespecial(declaring.getSuperclass(), "<init>", "()V");
/*     */     
/*     */     }
/* 149 */     catch (NotFoundException e) {
/* 150 */       throw new CannotCompileException(e);
/*     */     } 
/*     */     
/* 153 */     code.add(177);
/*     */ 
/*     */     
/* 156 */     cons.getMethodInfo2().setCodeAttribute(code.toCodeAttribute());
/* 157 */     return cons;
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
/*     */   public static CtConstructor skeleton(CtClass[] parameters, CtClass[] exceptions, CtClass declaring) throws CannotCompileException {
/* 182 */     return make(parameters, exceptions, 0, null, null, declaring);
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
/*     */   public static CtConstructor make(CtClass[] parameters, CtClass[] exceptions, CtClass declaring) throws CannotCompileException {
/* 201 */     return make(parameters, exceptions, 2, null, null, declaring);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CtConstructor make(CtClass[] parameters, CtClass[] exceptions, int howto, CtMethod body, CtMethod.ConstParameter cparam, CtClass declaring) throws CannotCompileException {
/* 317 */     return CtNewWrappedConstructor.wrapped(parameters, exceptions, howto, body, cparam, declaring);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\CtNewConstructor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */