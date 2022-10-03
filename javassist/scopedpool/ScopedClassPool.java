/*     */ package javassist.scopedpool;
/*     */ 
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.Map;
/*     */ import javassist.CannotCompileException;
/*     */ import javassist.ClassPath;
/*     */ import javassist.ClassPool;
/*     */ import javassist.CtClass;
/*     */ import javassist.LoaderClassPath;
/*     */ import javassist.NotFoundException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScopedClassPool
/*     */   extends ClassPool
/*     */ {
/*     */   protected ScopedClassPoolRepository repository;
/*     */   protected Reference<ClassLoader> classLoader;
/*     */   protected LoaderClassPath classPath;
/*  44 */   protected Map<String, CtClass> softcache = new SoftValueHashMap<>();
/*     */   
/*     */   boolean isBootstrapCl = true;
/*     */   
/*     */   static {
/*  49 */     ClassPool.doPruning = false;
/*  50 */     ClassPool.releaseUnmodifiedClassFile = false;
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
/*     */   protected ScopedClassPool(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository) {
/*  66 */     this(cl, src, repository, false);
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
/*     */   protected ScopedClassPool(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository, boolean isTemp) {
/*  84 */     super(src);
/*  85 */     this.repository = repository;
/*  86 */     this.classLoader = new WeakReference<>(cl);
/*  87 */     if (cl != null) {
/*  88 */       this.classPath = new LoaderClassPath(cl);
/*  89 */       insertClassPath((ClassPath)this.classPath);
/*     */     } 
/*  91 */     this.childFirstLookup = true;
/*  92 */     if (!isTemp && cl == null)
/*     */     {
/*  94 */       this.isBootstrapCl = true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassLoader getClassLoader() {
/* 104 */     ClassLoader cl = getClassLoader0();
/* 105 */     if (cl == null && !this.isBootstrapCl)
/*     */     {
/* 107 */       throw new IllegalStateException("ClassLoader has been garbage collected");
/*     */     }
/*     */     
/* 110 */     return cl;
/*     */   }
/*     */   
/*     */   protected ClassLoader getClassLoader0() {
/* 114 */     return this.classLoader.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 121 */     removeClassPath((ClassPath)this.classPath);
/* 122 */     this.classes.clear();
/* 123 */     this.softcache.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void flushClass(String classname) {
/* 133 */     this.classes.remove(classname);
/* 134 */     this.softcache.remove(classname);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void soften(CtClass clazz) {
/* 144 */     if (this.repository.isPrune())
/* 145 */       clazz.prune(); 
/* 146 */     this.classes.remove(clazz.getName());
/* 147 */     this.softcache.put(clazz.getName(), clazz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUnloadedClassLoader() {
/* 156 */     return false;
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
/*     */   protected CtClass getCached(String classname) {
/* 168 */     CtClass clazz = getCachedLocally(classname);
/* 169 */     if (clazz == null) {
/* 170 */       boolean isLocal = false;
/*     */       
/* 172 */       ClassLoader dcl = getClassLoader0();
/* 173 */       if (dcl != null) {
/* 174 */         int lastIndex = classname.lastIndexOf('$');
/* 175 */         String classResourceName = null;
/* 176 */         if (lastIndex < 0) {
/* 177 */           classResourceName = classname.replaceAll("[\\.]", "/") + ".class";
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */           
/* 183 */           classResourceName = classname.substring(0, lastIndex).replaceAll("[\\.]", "/") + classname.substring(lastIndex) + ".class";
/*     */         } 
/*     */         
/* 186 */         isLocal = (dcl.getResource(classResourceName) != null);
/*     */       } 
/*     */       
/* 189 */       if (!isLocal) {
/* 190 */         Map<ClassLoader, ScopedClassPool> registeredCLs = this.repository.getRegisteredCLs();
/* 191 */         synchronized (registeredCLs) {
/* 192 */           for (ScopedClassPool pool : registeredCLs.values()) {
/* 193 */             if (pool.isUnloadedClassLoader()) {
/* 194 */               this.repository.unregisterClassLoader(pool
/* 195 */                   .getClassLoader());
/*     */               
/*     */               continue;
/*     */             } 
/* 199 */             clazz = pool.getCachedLocally(classname);
/* 200 */             if (clazz != null) {
/* 201 */               return clazz;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 208 */     return clazz;
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
/*     */   protected void cacheCtClass(String classname, CtClass c, boolean dynamic) {
/* 222 */     if (dynamic) {
/* 223 */       super.cacheCtClass(classname, c, dynamic);
/*     */     } else {
/*     */       
/* 226 */       if (this.repository.isPrune())
/* 227 */         c.prune(); 
/* 228 */       this.softcache.put(classname, c);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void lockInCache(CtClass c) {
/* 239 */     super.cacheCtClass(c.getName(), c, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CtClass getCachedLocally(String classname) {
/* 250 */     CtClass cached = (CtClass)this.classes.get(classname);
/* 251 */     if (cached != null)
/* 252 */       return cached; 
/* 253 */     synchronized (this.softcache) {
/* 254 */       return this.softcache.get(classname);
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
/*     */   public synchronized CtClass getLocally(String classname) throws NotFoundException {
/* 269 */     this.softcache.remove(classname);
/* 270 */     CtClass clazz = (CtClass)this.classes.get(classname);
/* 271 */     if (clazz == null) {
/* 272 */       clazz = createCtClass(classname, true);
/* 273 */       if (clazz == null)
/* 274 */         throw new NotFoundException(classname); 
/* 275 */       super.cacheCtClass(classname, clazz, false);
/*     */     } 
/*     */     
/* 278 */     return clazz;
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
/*     */   public Class<?> toClass(CtClass ct, ClassLoader loader, ProtectionDomain domain) throws CannotCompileException {
/* 305 */     lockInCache(ct);
/* 306 */     return super.toClass(ct, getClassLoader0(), domain);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\scopedpool\ScopedClassPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */