/*     */ package javassist;
/*     */ 
/*     */ import java.util.Map;
/*     */ import javassist.bytecode.AccessFlag;
/*     */ import javassist.bytecode.AttributeInfo;
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.ClassFile;
/*     */ import javassist.bytecode.MethodInfo;
/*     */ import javassist.bytecode.SyntheticAttribute;
/*     */ import javassist.compiler.JvstCodeGen;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CtNewWrappedMethod
/*     */ {
/*     */   private static final String addedWrappedMethod = "_added_m$";
/*     */   
/*     */   public static CtMethod wrapped(CtClass returnType, String mname, CtClass[] parameterTypes, CtClass[] exceptionTypes, CtMethod body, CtMethod.ConstParameter constParam, CtClass declaring) throws CannotCompileException {
/*  41 */     CtMethod mt = new CtMethod(returnType, mname, parameterTypes, declaring);
/*     */     
/*  43 */     mt.setModifiers(body.getModifiers());
/*     */     try {
/*  45 */       mt.setExceptionTypes(exceptionTypes);
/*     */     }
/*  47 */     catch (NotFoundException e) {
/*  48 */       throw new CannotCompileException(e);
/*     */     } 
/*     */     
/*  51 */     Bytecode code = makeBody(declaring, declaring.getClassFile2(), body, parameterTypes, returnType, constParam);
/*     */     
/*  53 */     MethodInfo minfo = mt.getMethodInfo2();
/*  54 */     minfo.setCodeAttribute(code.toCodeAttribute());
/*     */     
/*  56 */     return mt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Bytecode makeBody(CtClass clazz, ClassFile classfile, CtMethod wrappedBody, CtClass[] parameters, CtClass returnType, CtMethod.ConstParameter cparam) throws CannotCompileException {
/*  66 */     boolean isStatic = Modifier.isStatic(wrappedBody.getModifiers());
/*  67 */     Bytecode code = new Bytecode(classfile.getConstPool(), 0, 0);
/*  68 */     int stacksize = makeBody0(clazz, classfile, wrappedBody, isStatic, parameters, returnType, cparam, code);
/*     */     
/*  70 */     code.setMaxStack(stacksize);
/*  71 */     code.setMaxLocals(isStatic, parameters, 0);
/*  72 */     return code;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static int makeBody0(CtClass clazz, ClassFile classfile, CtMethod wrappedBody, boolean isStatic, CtClass[] parameters, CtClass returnType, CtMethod.ConstParameter cparam, Bytecode code) throws CannotCompileException {
/*     */     int stacksize2;
/*     */     String desc, bodyname;
/*  85 */     if (!(clazz instanceof CtClassType)) {
/*  86 */       throw new CannotCompileException("bad declaring class" + clazz
/*  87 */           .getName());
/*     */     }
/*  89 */     if (!isStatic) {
/*  90 */       code.addAload(0);
/*     */     }
/*  92 */     int stacksize = compileParameterList(code, parameters, 
/*  93 */         isStatic ? 0 : 1);
/*     */ 
/*     */     
/*  96 */     if (cparam == null) {
/*  97 */       stacksize2 = 0;
/*  98 */       desc = CtMethod.ConstParameter.defaultDescriptor();
/*     */     } else {
/*     */       
/* 101 */       stacksize2 = cparam.compile(code);
/* 102 */       desc = cparam.descriptor();
/*     */     } 
/*     */     
/* 105 */     checkSignature(wrappedBody, desc);
/*     */ 
/*     */     
/*     */     try {
/* 109 */       bodyname = addBodyMethod((CtClassType)clazz, classfile, wrappedBody);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 115 */     catch (BadBytecode e) {
/* 116 */       throw new CannotCompileException(e);
/*     */     } 
/*     */     
/* 119 */     if (isStatic) {
/* 120 */       code.addInvokestatic(Bytecode.THIS, bodyname, desc);
/*     */     } else {
/* 122 */       code.addInvokespecial(Bytecode.THIS, bodyname, desc);
/*     */     } 
/* 124 */     compileReturn(code, returnType);
/*     */     
/* 126 */     if (stacksize < stacksize2 + 2) {
/* 127 */       stacksize = stacksize2 + 2;
/*     */     }
/* 129 */     return stacksize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void checkSignature(CtMethod wrappedBody, String descriptor) throws CannotCompileException {
/* 136 */     if (!descriptor.equals(wrappedBody.getMethodInfo2().getDescriptor())) {
/* 137 */       throw new CannotCompileException("wrapped method with a bad signature: " + wrappedBody
/*     */           
/* 139 */           .getDeclaringClass().getName() + '.' + wrappedBody
/* 140 */           .getName());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String addBodyMethod(CtClassType clazz, ClassFile classfile, CtMethod src) throws BadBytecode, CannotCompileException {
/* 148 */     Map<CtMethod, String> bodies = clazz.getHiddenMethods();
/* 149 */     String bodyname = bodies.get(src);
/* 150 */     if (bodyname == null)
/*     */       while (true) {
/* 152 */         bodyname = "_added_m$" + clazz.getUniqueNumber();
/* 153 */         if (classfile.getMethod(bodyname) == null) {
/* 154 */           ClassMap map = new ClassMap();
/* 155 */           map.put(src.getDeclaringClass().getName(), clazz.getName());
/*     */           
/* 157 */           MethodInfo body = new MethodInfo(classfile.getConstPool(), bodyname, src.getMethodInfo2(), map);
/*     */           
/* 159 */           int acc = body.getAccessFlags();
/* 160 */           body.setAccessFlags(AccessFlag.setPrivate(acc));
/* 161 */           body.addAttribute((AttributeInfo)new SyntheticAttribute(classfile.getConstPool()));
/*     */           
/* 163 */           classfile.addMethod(body);
/* 164 */           bodies.put(src, bodyname);
/* 165 */           CtMember.Cache cache = clazz.hasMemberCache();
/* 166 */           if (cache != null)
/* 167 */             cache.addMethod(new CtMethod(body, clazz));  break;
/*     */         } 
/*     */       }  
/* 170 */     return bodyname;
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
/*     */   static int compileParameterList(Bytecode code, CtClass[] params, int regno) {
/* 182 */     return JvstCodeGen.compileParameterList(code, params, regno);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void compileReturn(Bytecode code, CtClass type) {
/* 189 */     if (type.isPrimitive()) {
/* 190 */       CtPrimitiveType pt = (CtPrimitiveType)type;
/* 191 */       if (pt != CtClass.voidType) {
/* 192 */         String wrapper = pt.getWrapperName();
/* 193 */         code.addCheckcast(wrapper);
/* 194 */         code.addInvokevirtual(wrapper, pt.getGetMethodName(), pt
/* 195 */             .getGetMethodDescriptor());
/*     */       } 
/*     */       
/* 198 */       code.addOpcode(pt.getReturnOp());
/*     */     } else {
/*     */       
/* 201 */       code.addCheckcast(type);
/* 202 */       code.addOpcode(176);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\CtNewWrappedMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */