/*     */ package javassist;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class CtArray
/*     */   extends CtClass
/*     */ {
/*     */   protected ClassPool pool;
/*     */   private CtClass[] interfaces;
/*     */   
/*     */   public ClassPool getClassPool() {
/*     */     return this.pool;
/*     */   }
/*     */   
/*     */   CtArray(String name, ClassPool cp) {
/*  29 */     super(name);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  45 */     this.interfaces = null;
/*     */     this.pool = cp;
/*     */   }
/*     */   
/*     */   public int getModifiers() {
/*  50 */     int mod = 16;
/*     */     try {
/*  52 */       mod |= getComponentType().getModifiers() & 0x7;
/*     */     
/*     */     }
/*  55 */     catch (NotFoundException notFoundException) {}
/*  56 */     return mod;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass[] getInterfaces() throws NotFoundException {
/*  62 */     if (this.interfaces == null) {
/*  63 */       Class<?>[] intfs = Object[].class.getInterfaces();
/*     */ 
/*     */       
/*  66 */       this.interfaces = new CtClass[intfs.length];
/*  67 */       for (int i = 0; i < intfs.length; i++) {
/*  68 */         this.interfaces[i] = this.pool.get(intfs[i].getName());
/*     */       }
/*     */     } 
/*  71 */     return this.interfaces;
/*     */   }
/*     */   public boolean isArray() {
/*     */     return true;
/*     */   }
/*     */   public boolean subtypeOf(CtClass clazz) throws NotFoundException {
/*  77 */     if (super.subtypeOf(clazz)) {
/*  78 */       return true;
/*     */     }
/*  80 */     String cname = clazz.getName();
/*  81 */     if (cname.equals("java.lang.Object")) {
/*  82 */       return true;
/*     */     }
/*  84 */     CtClass[] intfs = getInterfaces();
/*  85 */     for (int i = 0; i < intfs.length; i++) {
/*  86 */       if (intfs[i].subtypeOf(clazz))
/*  87 */         return true; 
/*     */     } 
/*  89 */     return (clazz.isArray() && 
/*  90 */       getComponentType().subtypeOf(clazz.getComponentType()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass getComponentType() throws NotFoundException {
/*  96 */     String name = getName();
/*  97 */     return this.pool.get(name.substring(0, name.length() - 2));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CtClass getSuperclass() throws NotFoundException {
/* 103 */     return this.pool.get("java.lang.Object");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CtMethod[] getMethods() {
/*     */     try {
/* 110 */       return getSuperclass().getMethods();
/*     */     }
/* 112 */     catch (NotFoundException e) {
/* 113 */       return super.getMethods();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CtMethod getMethod(String name, String desc) throws NotFoundException {
/* 121 */     return getSuperclass().getMethod(name, desc);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CtConstructor[] getConstructors() {
/*     */     try {
/* 128 */       return getSuperclass().getConstructors();
/*     */     }
/* 130 */     catch (NotFoundException e) {
/* 131 */       return super.getConstructors();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\CtArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */