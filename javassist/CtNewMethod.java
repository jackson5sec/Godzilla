/*     */ package javassist;
/*     */ 
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.ConstPool;
/*     */ import javassist.bytecode.ExceptionsAttribute;
/*     */ import javassist.bytecode.FieldInfo;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CtNewMethod
/*     */ {
/*     */   public static CtMethod make(String src, CtClass declaring) throws CannotCompileException {
/*  50 */     return make(src, declaring, null, null);
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
/*     */   public static CtMethod make(String src, CtClass declaring, String delegateObj, String delegateMethod) throws CannotCompileException {
/*  74 */     Javac compiler = new Javac(declaring);
/*     */     try {
/*  76 */       if (delegateMethod != null) {
/*  77 */         compiler.recordProceed(delegateObj, delegateMethod);
/*     */       }
/*  79 */       CtMember obj = compiler.compile(src);
/*  80 */       if (obj instanceof CtMethod) {
/*  81 */         return (CtMethod)obj;
/*     */       }
/*  83 */     } catch (CompileError e) {
/*  84 */       throw new CannotCompileException(e);
/*     */     } 
/*     */     
/*  87 */     throw new CannotCompileException("not a method");
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
/*     */   public static CtMethod make(CtClass returnType, String mname, CtClass[] parameters, CtClass[] exceptions, String body, CtClass declaring) throws CannotCompileException {
/* 111 */     return make(1, returnType, mname, parameters, exceptions, body, declaring);
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
/*     */   public static CtMethod make(int modifiers, CtClass returnType, String mname, CtClass[] parameters, CtClass[] exceptions, String body, CtClass declaring) throws CannotCompileException {
/*     */     try {
/* 139 */       CtMethod cm = new CtMethod(returnType, mname, parameters, declaring);
/*     */       
/* 141 */       cm.setModifiers(modifiers);
/* 142 */       cm.setExceptionTypes(exceptions);
/* 143 */       cm.setBody(body);
/* 144 */       return cm;
/*     */     }
/* 146 */     catch (NotFoundException e) {
/* 147 */       throw new CannotCompileException(e);
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
/*     */   
/*     */   public static CtMethod copy(CtMethod src, CtClass declaring, ClassMap map) throws CannotCompileException {
/* 168 */     return new CtMethod(src, declaring, map);
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
/*     */   public static CtMethod copy(CtMethod src, String name, CtClass declaring, ClassMap map) throws CannotCompileException {
/* 190 */     CtMethod cm = new CtMethod(src, declaring, map);
/* 191 */     cm.setName(name);
/* 192 */     return cm;
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
/*     */   public static CtMethod abstractMethod(CtClass returnType, String mname, CtClass[] parameters, CtClass[] exceptions, CtClass declaring) throws NotFoundException {
/* 213 */     CtMethod cm = new CtMethod(returnType, mname, parameters, declaring);
/* 214 */     cm.setExceptionTypes(exceptions);
/* 215 */     return cm;
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
/*     */   public static CtMethod getter(String methodName, CtField field) throws CannotCompileException {
/* 230 */     FieldInfo finfo = field.getFieldInfo2();
/* 231 */     String fieldType = finfo.getDescriptor();
/* 232 */     String desc = "()" + fieldType;
/* 233 */     ConstPool cp = finfo.getConstPool();
/* 234 */     MethodInfo minfo = new MethodInfo(cp, methodName, desc);
/* 235 */     minfo.setAccessFlags(1);
/*     */     
/* 237 */     Bytecode code = new Bytecode(cp, 2, 1);
/*     */     try {
/* 239 */       String fieldName = finfo.getName();
/* 240 */       if ((finfo.getAccessFlags() & 0x8) == 0) {
/* 241 */         code.addAload(0);
/* 242 */         code.addGetfield(Bytecode.THIS, fieldName, fieldType);
/*     */       } else {
/*     */         
/* 245 */         code.addGetstatic(Bytecode.THIS, fieldName, fieldType);
/*     */       } 
/* 247 */       code.addReturn(field.getType());
/*     */     }
/* 249 */     catch (NotFoundException e) {
/* 250 */       throw new CannotCompileException(e);
/*     */     } 
/*     */     
/* 253 */     minfo.setCodeAttribute(code.toCodeAttribute());
/* 254 */     CtClass cc = field.getDeclaringClass();
/*     */     
/* 256 */     return new CtMethod(minfo, cc);
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
/*     */   public static CtMethod setter(String methodName, CtField field) throws CannotCompileException {
/* 273 */     FieldInfo finfo = field.getFieldInfo2();
/* 274 */     String fieldType = finfo.getDescriptor();
/* 275 */     String desc = "(" + fieldType + ")V";
/* 276 */     ConstPool cp = finfo.getConstPool();
/* 277 */     MethodInfo minfo = new MethodInfo(cp, methodName, desc);
/* 278 */     minfo.setAccessFlags(1);
/*     */     
/* 280 */     Bytecode code = new Bytecode(cp, 3, 3);
/*     */     try {
/* 282 */       String fieldName = finfo.getName();
/* 283 */       if ((finfo.getAccessFlags() & 0x8) == 0) {
/* 284 */         code.addAload(0);
/* 285 */         code.addLoad(1, field.getType());
/* 286 */         code.addPutfield(Bytecode.THIS, fieldName, fieldType);
/*     */       } else {
/*     */         
/* 289 */         code.addLoad(1, field.getType());
/* 290 */         code.addPutstatic(Bytecode.THIS, fieldName, fieldType);
/*     */       } 
/*     */       
/* 293 */       code.addReturn(null);
/*     */     }
/* 295 */     catch (NotFoundException e) {
/* 296 */       throw new CannotCompileException(e);
/*     */     } 
/*     */     
/* 299 */     minfo.setCodeAttribute(code.toCodeAttribute());
/* 300 */     CtClass cc = field.getDeclaringClass();
/*     */     
/* 302 */     return new CtMethod(minfo, cc);
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
/*     */   public static CtMethod delegator(CtMethod delegate, CtClass declaring) throws CannotCompileException {
/*     */     try {
/* 331 */       return delegator0(delegate, declaring);
/*     */     }
/* 333 */     catch (NotFoundException e) {
/* 334 */       throw new CannotCompileException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static CtMethod delegator0(CtMethod delegate, CtClass declaring) throws CannotCompileException, NotFoundException {
/*     */     int s;
/* 341 */     MethodInfo deleInfo = delegate.getMethodInfo2();
/* 342 */     String methodName = deleInfo.getName();
/* 343 */     String desc = deleInfo.getDescriptor();
/* 344 */     ConstPool cp = declaring.getClassFile2().getConstPool();
/* 345 */     MethodInfo minfo = new MethodInfo(cp, methodName, desc);
/* 346 */     minfo.setAccessFlags(deleInfo.getAccessFlags());
/*     */     
/* 348 */     ExceptionsAttribute eattr = deleInfo.getExceptionsAttribute();
/* 349 */     if (eattr != null) {
/* 350 */       minfo.setExceptionsAttribute((ExceptionsAttribute)eattr
/* 351 */           .copy(cp, null));
/*     */     }
/* 353 */     Bytecode code = new Bytecode(cp, 0, 0);
/* 354 */     boolean isStatic = Modifier.isStatic(delegate.getModifiers());
/* 355 */     CtClass deleClass = delegate.getDeclaringClass();
/* 356 */     CtClass[] params = delegate.getParameterTypes();
/*     */     
/* 358 */     if (isStatic) {
/* 359 */       s = code.addLoadParameters(params, 0);
/* 360 */       code.addInvokestatic(deleClass, methodName, desc);
/*     */     } else {
/*     */       
/* 363 */       code.addLoad(0, deleClass);
/* 364 */       s = code.addLoadParameters(params, 1);
/* 365 */       code.addInvokespecial(deleClass, methodName, desc);
/*     */     } 
/*     */     
/* 368 */     code.addReturn(delegate.getReturnType());
/* 369 */     code.setMaxLocals(++s);
/* 370 */     code.setMaxStack((s < 2) ? 2 : s);
/* 371 */     minfo.setCodeAttribute(code.toCodeAttribute());
/*     */     
/* 373 */     return new CtMethod(minfo, declaring);
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
/*     */   public static CtMethod wrapped(CtClass returnType, String mname, CtClass[] parameterTypes, CtClass[] exceptionTypes, CtMethod body, CtMethod.ConstParameter constParam, CtClass declaring) throws CannotCompileException {
/* 480 */     return CtNewWrappedMethod.wrapped(returnType, mname, parameterTypes, exceptionTypes, body, constParam, declaring);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\CtNewMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */