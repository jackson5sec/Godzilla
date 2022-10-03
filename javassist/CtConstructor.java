/*     */ package javassist;
/*     */ 
/*     */ import javassist.bytecode.BadBytecode;
/*     */ import javassist.bytecode.Bytecode;
/*     */ import javassist.bytecode.ClassFile;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CtConstructor
/*     */   extends CtBehavior
/*     */ {
/*     */   protected CtConstructor(MethodInfo minfo, CtClass declaring) {
/*  46 */     super(declaring, minfo);
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
/*     */   public CtConstructor(CtClass[] parameters, CtClass declaring) {
/*  65 */     this((MethodInfo)null, declaring);
/*  66 */     ConstPool cp = declaring.getClassFile2().getConstPool();
/*  67 */     String desc = Descriptor.ofConstructor(parameters);
/*  68 */     this.methodInfo = new MethodInfo(cp, "<init>", desc);
/*  69 */     setModifiers(1);
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
/*     */   public CtConstructor(CtConstructor src, CtClass declaring, ClassMap map) throws CannotCompileException {
/* 109 */     this((MethodInfo)null, declaring);
/* 110 */     copy(src, true, map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConstructor() {
/* 117 */     return this.methodInfo.isConstructor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClassInitializer() {
/* 124 */     return this.methodInfo.isStaticInitializer();
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
/* 135 */     return getDeclaringClass().getName() + (
/* 136 */       isConstructor() ? Descriptor.toString(getSignature()) : 
/* 137 */       ".<clinit>()");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 148 */     if (this.methodInfo.isStaticInitializer())
/* 149 */       return "<clinit>"; 
/* 150 */     return this.declaringClass.getSimpleName();
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
/*     */   public boolean isEmpty() {
/* 162 */     CodeAttribute ca = getMethodInfo2().getCodeAttribute();
/* 163 */     if (ca == null) {
/* 164 */       return false;
/*     */     }
/*     */     
/* 167 */     ConstPool cp = ca.getConstPool();
/* 168 */     CodeIterator it = ca.iterator();
/*     */     
/*     */     try {
/* 171 */       int op0 = it.byteAt(it.next());
/* 172 */       if (op0 != 177) { int pos; if (op0 == 42 && it
/*     */           
/* 174 */           .byteAt(pos = it.next()) == 183)
/* 175 */         { int desc; if ((desc = cp.isConstructor(getSuperclassName(), it
/* 176 */               .u16bitAt(pos + 1))) != 0 && "()V"
/* 177 */             .equals(cp.getUtf8Info(desc)) && it
/* 178 */             .byteAt(it.next()) == 177 && 
/* 179 */             !it.hasNext()); }  return false; }
/*     */     
/* 181 */     } catch (BadBytecode badBytecode) {
/* 182 */       return false;
/*     */     } 
/*     */   }
/*     */   private String getSuperclassName() {
/* 186 */     ClassFile cf = this.declaringClass.getClassFile2();
/* 187 */     return cf.getSuperclass();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean callsSuper() throws CannotCompileException {
/* 196 */     CodeAttribute codeAttr = this.methodInfo.getCodeAttribute();
/* 197 */     if (codeAttr != null) {
/* 198 */       CodeIterator it = codeAttr.iterator();
/*     */       try {
/* 200 */         int index = it.skipSuperConstructor();
/* 201 */         return (index >= 0);
/*     */       }
/* 203 */       catch (BadBytecode e) {
/* 204 */         throw new CannotCompileException(e);
/*     */       } 
/*     */     } 
/*     */     
/* 208 */     return false;
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
/*     */   public void setBody(String src) throws CannotCompileException {
/* 222 */     if (src == null)
/* 223 */       if (isClassInitializer()) {
/* 224 */         src = ";";
/*     */       } else {
/* 226 */         src = "super();";
/*     */       }  
/* 228 */     super.setBody(src);
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
/*     */   public void setBody(CtConstructor src, ClassMap map) throws CannotCompileException {
/* 246 */     setBody0(src.declaringClass, src.methodInfo, this.declaringClass, this.methodInfo, map);
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
/*     */   public void insertBeforeBody(String src) throws CannotCompileException {
/* 259 */     CtClass cc = this.declaringClass;
/* 260 */     cc.checkModify();
/* 261 */     if (isClassInitializer()) {
/* 262 */       throw new CannotCompileException("class initializer");
/*     */     }
/* 264 */     CodeAttribute ca = this.methodInfo.getCodeAttribute();
/* 265 */     CodeIterator iterator = ca.iterator();
/*     */     
/* 267 */     Bytecode b = new Bytecode(this.methodInfo.getConstPool(), ca.getMaxStack(), ca.getMaxLocals());
/* 268 */     b.setStackDepth(ca.getMaxStack());
/* 269 */     Javac jv = new Javac(b, cc);
/*     */     try {
/* 271 */       jv.recordParams(getParameterTypes(), false);
/* 272 */       jv.compileStmnt(src);
/* 273 */       ca.setMaxStack(b.getMaxStack());
/* 274 */       ca.setMaxLocals(b.getMaxLocals());
/* 275 */       iterator.skipConstructor();
/* 276 */       int pos = iterator.insertEx(b.get());
/* 277 */       iterator.insert(b.getExceptionTable(), pos);
/* 278 */       this.methodInfo.rebuildStackMapIf6(cc.getClassPool(), cc.getClassFile2());
/*     */     }
/* 280 */     catch (NotFoundException e) {
/* 281 */       throw new CannotCompileException(e);
/*     */     }
/* 283 */     catch (CompileError e) {
/* 284 */       throw new CannotCompileException(e);
/*     */     }
/* 286 */     catch (BadBytecode e) {
/* 287 */       throw new CannotCompileException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getStartPosOfBody(CodeAttribute ca) throws CannotCompileException {
/* 296 */     CodeIterator ci = ca.iterator();
/*     */     try {
/* 298 */       ci.skipConstructor();
/* 299 */       return ci.next();
/*     */     }
/* 301 */     catch (BadBytecode e) {
/* 302 */       throw new CannotCompileException(e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtMethod toMethod(String name, CtClass declaring) throws CannotCompileException {
/* 329 */     return toMethod(name, declaring, (ClassMap)null);
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
/*     */   public CtMethod toMethod(String name, CtClass declaring, ClassMap map) throws CannotCompileException {
/* 363 */     CtMethod method = new CtMethod(null, declaring);
/* 364 */     method.copy(this, false, map);
/* 365 */     if (isConstructor()) {
/* 366 */       MethodInfo minfo = method.getMethodInfo2();
/* 367 */       CodeAttribute ca = minfo.getCodeAttribute();
/* 368 */       if (ca != null) {
/* 369 */         removeConsCall(ca);
/*     */         try {
/* 371 */           this.methodInfo.rebuildStackMapIf6(declaring.getClassPool(), declaring
/* 372 */               .getClassFile2());
/*     */         }
/* 374 */         catch (BadBytecode e) {
/* 375 */           throw new CannotCompileException(e);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 380 */     method.setName(name);
/* 381 */     return method;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void removeConsCall(CodeAttribute ca) throws CannotCompileException {
/* 387 */     CodeIterator iterator = ca.iterator();
/*     */     try {
/* 389 */       int pos = iterator.skipConstructor();
/* 390 */       if (pos >= 0) {
/* 391 */         int mref = iterator.u16bitAt(pos + 1);
/* 392 */         String desc = ca.getConstPool().getMethodrefType(mref);
/* 393 */         int num = Descriptor.numOfParameters(desc) + 1;
/* 394 */         if (num > 3) {
/* 395 */           pos = (iterator.insertGapAt(pos, num - 3, false)).position;
/*     */         }
/* 397 */         iterator.writeByte(87, pos++);
/* 398 */         iterator.writeByte(0, pos);
/* 399 */         iterator.writeByte(0, pos + 1);
/* 400 */         Descriptor.Iterator it = new Descriptor.Iterator(desc);
/*     */         while (true) {
/* 402 */           it.next();
/* 403 */           if (it.isParameter()) {
/* 404 */             iterator.writeByte(it.is2byte() ? 88 : 87, pos++);
/*     */             
/*     */             continue;
/*     */           } 
/*     */           break;
/*     */         } 
/*     */       } 
/* 411 */     } catch (BadBytecode e) {
/* 412 */       throw new CannotCompileException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\CtConstructor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */