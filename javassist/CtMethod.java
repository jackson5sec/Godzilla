/*     */ package javassist;
/*     */ 
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.CodeAttribute;
/*     */ import javassist.bytecode.CodeIterator;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.Descriptor;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CtMethod
/*     */   extends CtBehavior
/*     */ {
/*     */   protected String cachedStringRep;
/*     */   
/*     */   CtMethod(MethodInfo minfo, CtClass declaring) {
/*  46 */     super(declaring, minfo);
/*  47 */     this.cachedStringRep = null;
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
/*     */   public CtMethod(CtClass returnType, String mname, CtClass[] parameters, CtClass declaring) {
/*  63 */     this((MethodInfo)null, declaring);
/*  64 */     ConstPool cp = declaring.getClassFile2().getConstPool();
/*  65 */     String desc = Descriptor.ofMethod(returnType, parameters);
/*  66 */     this.methodInfo = new MethodInfo(cp, mname, desc);
/*  67 */     setModifiers(1025);
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
/*     */   public CtMethod(CtMethod src, CtClass declaring, ClassMap map) throws CannotCompileException {
/* 123 */     this((MethodInfo)null, declaring);
/* 124 */     copy(src, false, map);
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
/*     */   public static CtMethod make(String src, CtClass declaring) throws CannotCompileException {
/* 140 */     return CtNewMethod.make(src, declaring);
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
/*     */   public static CtMethod make(MethodInfo minfo, CtClass declaring) throws CannotCompileException {
/* 155 */     if (declaring.getClassFile2().getConstPool() != minfo.getConstPool()) {
/* 156 */       throw new CannotCompileException("bad declaring class");
/*     */     }
/* 158 */     return new CtMethod(minfo, declaring);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 168 */     return getStringRep().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void nameReplaced() {
/* 177 */     this.cachedStringRep = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final String getStringRep() {
/* 183 */     if (this.cachedStringRep == null) {
/* 184 */       this
/* 185 */         .cachedStringRep = this.methodInfo.getName() + Descriptor.getParamDescriptor(this.methodInfo.getDescriptor());
/*     */     }
/* 187 */     return this.cachedStringRep;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 196 */     return (obj != null && obj instanceof CtMethod && ((CtMethod)obj)
/* 197 */       .getStringRep().equals(getStringRep()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLongName() {
/* 208 */     return getDeclaringClass().getName() + "." + 
/* 209 */       getName() + Descriptor.toString(getSignature());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 217 */     return this.methodInfo.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String newname) {
/* 224 */     this.declaringClass.checkModify();
/* 225 */     this.methodInfo.setName(newname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass getReturnType() throws NotFoundException {
/* 232 */     return getReturnType0();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 241 */     CodeAttribute ca = getMethodInfo2().getCodeAttribute();
/* 242 */     if (ca == null) {
/* 243 */       return ((getModifiers() & 0x400) != 0);
/*     */     }
/* 245 */     CodeIterator it = ca.iterator();
/*     */     try {
/* 247 */       return (it.hasNext() && it.byteAt(it.next()) == 177 && 
/* 248 */         !it.hasNext());
/*     */     }
/* 250 */     catch (BadBytecode badBytecode) {
/* 251 */       return false;
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
/*     */   public void setBody(CtMethod src, ClassMap map) throws CannotCompileException {
/* 271 */     setBody0(src.declaringClass, src.methodInfo, this.declaringClass, this.methodInfo, map);
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
/*     */   public void setWrappedBody(CtMethod mbody, ConstParameter constParam) throws CannotCompileException {
/*     */     CtClass params[], retType;
/* 289 */     this.declaringClass.checkModify();
/*     */     
/* 291 */     CtClass clazz = getDeclaringClass();
/*     */ 
/*     */     
/*     */     try {
/* 295 */       params = getParameterTypes();
/* 296 */       retType = getReturnType();
/*     */     }
/* 298 */     catch (NotFoundException e) {
/* 299 */       throw new CannotCompileException(e);
/*     */     } 
/*     */     
/* 302 */     Bytecode code = CtNewWrappedMethod.makeBody(clazz, clazz
/* 303 */         .getClassFile2(), mbody, params, retType, constParam);
/*     */ 
/*     */ 
/*     */     
/* 307 */     CodeAttribute cattr = code.toCodeAttribute();
/* 308 */     this.methodInfo.setCodeAttribute(cattr);
/* 309 */     this.methodInfo.setAccessFlags(this.methodInfo.getAccessFlags() & 0xFFFFFBFF);
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
/*     */   public static class ConstParameter
/*     */   {
/*     */     public static ConstParameter integer(int i) {
/* 332 */       return new CtMethod.IntConstParameter(i);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static ConstParameter integer(long i) {
/* 341 */       return new CtMethod.LongConstParameter(i);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static ConstParameter string(String s) {
/* 350 */       return new CtMethod.StringConstParameter(s);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int compile(Bytecode code) throws CannotCompileException {
/* 359 */       return 0;
/*     */     }
/*     */     
/*     */     String descriptor() {
/* 363 */       return defaultDescriptor();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static String defaultDescriptor() {
/* 370 */       return "([Ljava/lang/Object;)Ljava/lang/Object;";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     String constDescriptor() {
/* 379 */       return defaultConstDescriptor();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static String defaultConstDescriptor() {
/* 386 */       return "([Ljava/lang/Object;)V";
/*     */     }
/*     */   }
/*     */   
/*     */   static class IntConstParameter extends ConstParameter {
/*     */     int param;
/*     */     
/*     */     IntConstParameter(int i) {
/* 394 */       this.param = i;
/*     */     }
/*     */ 
/*     */     
/*     */     int compile(Bytecode code) throws CannotCompileException {
/* 399 */       code.addIconst(this.param);
/* 400 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*     */     String descriptor() {
/* 405 */       return "([Ljava/lang/Object;I)Ljava/lang/Object;";
/*     */     }
/*     */ 
/*     */     
/*     */     String constDescriptor() {
/* 410 */       return "([Ljava/lang/Object;I)V";
/*     */     }
/*     */   }
/*     */   
/*     */   static class LongConstParameter extends ConstParameter {
/*     */     long param;
/*     */     
/*     */     LongConstParameter(long l) {
/* 418 */       this.param = l;
/*     */     }
/*     */ 
/*     */     
/*     */     int compile(Bytecode code) throws CannotCompileException {
/* 423 */       code.addLconst(this.param);
/* 424 */       return 2;
/*     */     }
/*     */ 
/*     */     
/*     */     String descriptor() {
/* 429 */       return "([Ljava/lang/Object;J)Ljava/lang/Object;";
/*     */     }
/*     */ 
/*     */     
/*     */     String constDescriptor() {
/* 434 */       return "([Ljava/lang/Object;J)V";
/*     */     }
/*     */   }
/*     */   
/*     */   static class StringConstParameter extends ConstParameter {
/*     */     String param;
/*     */     
/*     */     StringConstParameter(String s) {
/* 442 */       this.param = s;
/*     */     }
/*     */ 
/*     */     
/*     */     int compile(Bytecode code) throws CannotCompileException {
/* 447 */       code.addLdc(this.param);
/* 448 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*     */     String descriptor() {
/* 453 */       return "([Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;";
/*     */     }
/*     */ 
/*     */     
/*     */     String constDescriptor() {
/* 458 */       return "([Ljava/lang/Object;Ljava/lang/String;)V";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\CtMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */