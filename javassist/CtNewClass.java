/*     */ package javassist;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import javassist.bytecode.ClassFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CtNewClass
/*     */   extends CtClassType
/*     */ {
/*     */   protected boolean hasConstructor;
/*     */   
/*     */   CtNewClass(String name, ClassPool cp, boolean isInterface, CtClass superclass) {
/*  31 */     super(name, cp); String superName;
/*  32 */     this.wasChanged = true;
/*     */     
/*  34 */     if (isInterface || superclass == null) {
/*  35 */       superName = null;
/*     */     } else {
/*  37 */       superName = superclass.getName();
/*     */     } 
/*  39 */     this.classfile = new ClassFile(isInterface, name, superName);
/*  40 */     if (isInterface && superclass != null) {
/*  41 */       this.classfile.setInterfaces(new String[] { superclass.getName() });
/*     */     }
/*  43 */     setModifiers(Modifier.setPublic(getModifiers()));
/*  44 */     this.hasConstructor = isInterface;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void extendToString(StringBuffer buffer) {
/*  49 */     if (this.hasConstructor) {
/*  50 */       buffer.append("hasConstructor ");
/*     */     }
/*  52 */     super.extendToString(buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConstructor(CtConstructor c) throws CannotCompileException {
/*  59 */     this.hasConstructor = true;
/*  60 */     super.addConstructor(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void toBytecode(DataOutputStream out) throws CannotCompileException, IOException {
/*  67 */     if (!this.hasConstructor) {
/*     */       try {
/*  69 */         inheritAllConstructors();
/*  70 */         this.hasConstructor = true;
/*     */       }
/*  72 */       catch (NotFoundException e) {
/*  73 */         throw new CannotCompileException(e);
/*     */       } 
/*     */     }
/*  76 */     super.toBytecode(out);
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
/*     */   public void inheritAllConstructors() throws CannotCompileException, NotFoundException {
/*  92 */     CtClass superclazz = getSuperclass();
/*  93 */     CtConstructor[] cs = superclazz.getDeclaredConstructors();
/*     */     
/*  95 */     int n = 0;
/*  96 */     for (int i = 0; i < cs.length; i++) {
/*  97 */       CtConstructor c = cs[i];
/*  98 */       int mod = c.getModifiers();
/*  99 */       if (isInheritable(mod, superclazz)) {
/*     */         
/* 101 */         CtConstructor cons = CtNewConstructor.make(c.getParameterTypes(), c
/* 102 */             .getExceptionTypes(), this);
/* 103 */         cons.setModifiers(mod & 0x7);
/* 104 */         addConstructor(cons);
/* 105 */         n++;
/*     */       } 
/*     */     } 
/*     */     
/* 109 */     if (n < 1) {
/* 110 */       throw new CannotCompileException("no inheritable constructor in " + superclazz
/* 111 */           .getName());
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isInheritable(int mod, CtClass superclazz) {
/* 116 */     if (Modifier.isPrivate(mod)) {
/* 117 */       return false;
/*     */     }
/* 119 */     if (Modifier.isPackage(mod)) {
/* 120 */       String pname = getPackageName();
/* 121 */       String pname2 = superclazz.getPackageName();
/* 122 */       if (pname == null)
/* 123 */         return (pname2 == null); 
/* 124 */       return pname.equals(pname2);
/*     */     } 
/*     */     
/* 127 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\CtNewClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */