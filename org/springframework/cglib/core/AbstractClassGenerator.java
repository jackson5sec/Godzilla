/*     */ package org.springframework.cglib.core;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.WeakHashMap;
/*     */ import org.springframework.asm.ClassReader;
/*     */ import org.springframework.cglib.core.internal.Function;
/*     */ import org.springframework.cglib.core.internal.LoadingCache;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractClassGenerator<T>
/*     */   implements ClassGenerator
/*     */ {
/*  39 */   private static final ThreadLocal CURRENT = new ThreadLocal();
/*     */   
/*  41 */   private static volatile Map<ClassLoader, ClassLoaderData> CACHE = new WeakHashMap<>();
/*     */ 
/*     */   
/*  44 */   private static final boolean DEFAULT_USE_CACHE = Boolean.parseBoolean(System.getProperty("cglib.useCache", "true"));
/*     */ 
/*     */   
/*  47 */   private GeneratorStrategy strategy = DefaultGeneratorStrategy.INSTANCE;
/*     */   
/*  49 */   private NamingPolicy namingPolicy = DefaultNamingPolicy.INSTANCE;
/*     */   
/*     */   private Source source;
/*     */   
/*     */   private ClassLoader classLoader;
/*     */   
/*     */   private Class contextClass;
/*     */   
/*     */   private String namePrefix;
/*     */   
/*     */   private Object key;
/*     */   
/*  61 */   private boolean useCache = DEFAULT_USE_CACHE;
/*     */   
/*     */   private String className;
/*     */   
/*     */   private boolean attemptLoad;
/*     */ 
/*     */   
/*     */   protected static class ClassLoaderData
/*     */   {
/*  70 */     private final Set<String> reservedClassNames = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final LoadingCache<AbstractClassGenerator, Object, Object> generatedClasses;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final WeakReference<ClassLoader> classLoader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  90 */     private final Predicate uniqueNamePredicate = new Predicate() {
/*     */         public boolean evaluate(Object name) {
/*  92 */           return AbstractClassGenerator.ClassLoaderData.this.reservedClassNames.contains(name);
/*     */         }
/*     */       };
/*     */     
/*  96 */     private static final Function<AbstractClassGenerator, Object> GET_KEY = new Function<AbstractClassGenerator, Object>() {
/*     */         public Object apply(AbstractClassGenerator gen) {
/*  98 */           return gen.key;
/*     */         }
/*     */       };
/*     */     
/*     */     public ClassLoaderData(ClassLoader classLoader) {
/* 103 */       if (classLoader == null) {
/* 104 */         throw new IllegalArgumentException("classLoader == null is not yet supported");
/*     */       }
/* 106 */       this.classLoader = new WeakReference<>(classLoader);
/* 107 */       Function<AbstractClassGenerator, Object> load = new Function<AbstractClassGenerator, Object>()
/*     */         {
/*     */           public Object apply(AbstractClassGenerator gen) {
/* 110 */             Class klass = gen.generate(AbstractClassGenerator.ClassLoaderData.this);
/* 111 */             return gen.wrapCachedClass(klass);
/*     */           }
/*     */         };
/* 114 */       this.generatedClasses = new LoadingCache(GET_KEY, load);
/*     */     }
/*     */     
/*     */     public ClassLoader getClassLoader() {
/* 118 */       return this.classLoader.get();
/*     */     }
/*     */     
/*     */     public void reserveName(String name) {
/* 122 */       this.reservedClassNames.add(name);
/*     */     }
/*     */     
/*     */     public Predicate getUniqueNamePredicate() {
/* 126 */       return this.uniqueNamePredicate;
/*     */     }
/*     */     
/*     */     public Object get(AbstractClassGenerator<Object> gen, boolean useCache) {
/* 130 */       if (!useCache) {
/* 131 */         return gen.generate(this);
/*     */       }
/*     */       
/* 134 */       Object cachedValue = this.generatedClasses.get(gen);
/* 135 */       return gen.unwrapCachedValue(cachedValue);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected T wrapCachedClass(Class<?> klass) {
/* 142 */     return (T)new WeakReference<>(klass);
/*     */   }
/*     */   
/*     */   protected Object unwrapCachedValue(T cached) {
/* 146 */     return ((WeakReference)cached).get();
/*     */   }
/*     */ 
/*     */   
/*     */   protected static class Source
/*     */   {
/*     */     String name;
/*     */     
/*     */     public Source(String name) {
/* 155 */       this.name = name;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected AbstractClassGenerator(Source source) {
/* 161 */     this.source = source;
/*     */   }
/*     */   
/*     */   protected void setNamePrefix(String namePrefix) {
/* 165 */     this.namePrefix = namePrefix;
/*     */   }
/*     */   
/*     */   protected final String getClassName() {
/* 169 */     return this.className;
/*     */   }
/*     */   
/*     */   private void setClassName(String className) {
/* 173 */     this.className = className;
/*     */   }
/*     */   
/*     */   private String generateClassName(Predicate nameTestPredicate) {
/* 177 */     return this.namingPolicy.getClassName(this.namePrefix, this.source.name, this.key, nameTestPredicate);
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
/*     */   public void setClassLoader(ClassLoader classLoader) {
/* 190 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContextClass(Class contextClass) {
/* 195 */     this.contextClass = contextClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNamingPolicy(NamingPolicy namingPolicy) {
/* 205 */     if (namingPolicy == null)
/* 206 */       namingPolicy = DefaultNamingPolicy.INSTANCE; 
/* 207 */     this.namingPolicy = namingPolicy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NamingPolicy getNamingPolicy() {
/* 214 */     return this.namingPolicy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUseCache(boolean useCache) {
/* 222 */     this.useCache = useCache;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getUseCache() {
/* 229 */     return this.useCache;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAttemptLoad(boolean attemptLoad) {
/* 238 */     this.attemptLoad = attemptLoad;
/*     */   }
/*     */   
/*     */   public boolean getAttemptLoad() {
/* 242 */     return this.attemptLoad;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setStrategy(GeneratorStrategy strategy) {
/* 250 */     if (strategy == null)
/* 251 */       strategy = DefaultGeneratorStrategy.INSTANCE; 
/* 252 */     this.strategy = strategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GeneratorStrategy getStrategy() {
/* 259 */     return this.strategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static AbstractClassGenerator getCurrent() {
/* 267 */     return CURRENT.get();
/*     */   }
/*     */   
/*     */   public ClassLoader getClassLoader() {
/* 271 */     ClassLoader t = this.classLoader;
/* 272 */     if (t == null) {
/* 273 */       t = getDefaultClassLoader();
/*     */     }
/* 275 */     if (t == null) {
/* 276 */       t = getClass().getClassLoader();
/*     */     }
/* 278 */     if (t == null) {
/* 279 */       t = Thread.currentThread().getContextClassLoader();
/*     */     }
/* 281 */     if (t == null) {
/* 282 */       throw new IllegalStateException("Cannot determine classloader");
/*     */     }
/* 284 */     return t;
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
/*     */   protected ProtectionDomain getProtectionDomain() {
/* 298 */     return null;
/*     */   }
/*     */   
/*     */   protected Object create(Object key) {
/*     */     try {
/* 303 */       ClassLoader loader = getClassLoader();
/* 304 */       Map<ClassLoader, ClassLoaderData> cache = CACHE;
/* 305 */       ClassLoaderData data = cache.get(loader);
/* 306 */       if (data == null) {
/* 307 */         synchronized (AbstractClassGenerator.class) {
/* 308 */           cache = CACHE;
/* 309 */           data = cache.get(loader);
/* 310 */           if (data == null) {
/* 311 */             Map<ClassLoader, ClassLoaderData> newCache = new WeakHashMap<>(cache);
/* 312 */             data = new ClassLoaderData(loader);
/* 313 */             newCache.put(loader, data);
/* 314 */             CACHE = newCache;
/*     */           } 
/*     */         } 
/*     */       }
/* 318 */       this.key = key;
/* 319 */       Object obj = data.get(this, getUseCache());
/* 320 */       if (obj instanceof Class) {
/* 321 */         return firstInstance((Class)obj);
/*     */       }
/* 323 */       return nextInstance(obj);
/*     */     }
/* 325 */     catch (RuntimeException|Error ex) {
/* 326 */       throw ex;
/*     */     }
/* 328 */     catch (Exception ex) {
/* 329 */       throw new CodeGenerationException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Class generate(ClassLoaderData data) {
/* 335 */     Object save = CURRENT.get();
/* 336 */     CURRENT.set(this); try {
/*     */       Class gen;
/* 338 */       ClassLoader classLoader = data.getClassLoader();
/* 339 */       if (classLoader == null) {
/* 340 */         throw new IllegalStateException("ClassLoader is null while trying to define class " + 
/* 341 */             getClassName() + ". It seems that the loader has been expired from a weak reference somehow. Please file an issue at cglib's issue tracker.");
/*     */       }
/*     */       
/* 344 */       synchronized (classLoader) {
/* 345 */         String name = generateClassName(data.getUniqueNamePredicate());
/* 346 */         data.reserveName(name);
/* 347 */         setClassName(name);
/*     */       } 
/* 349 */       if (this.attemptLoad) {
/*     */         try {
/* 351 */           gen = classLoader.loadClass(getClassName());
/* 352 */           return gen;
/*     */         }
/* 354 */         catch (ClassNotFoundException classNotFoundException) {}
/*     */       }
/*     */ 
/*     */       
/* 358 */       byte[] b = this.strategy.generate(this);
/* 359 */       String className = ClassNameReader.getClassName(new ClassReader(b));
/* 360 */       ProtectionDomain protectionDomain = getProtectionDomain();
/* 361 */       synchronized (classLoader) {
/*     */         
/* 363 */         gen = ReflectUtils.defineClass(className, b, classLoader, protectionDomain, this.contextClass);
/*     */       } 
/*     */       
/* 366 */       return gen;
/*     */     }
/* 368 */     catch (RuntimeException|Error ex) {
/* 369 */       throw ex;
/*     */     }
/* 371 */     catch (Exception ex) {
/* 372 */       throw new CodeGenerationException(ex);
/*     */     } finally {
/*     */       
/* 375 */       CURRENT.set(save);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract ClassLoader getDefaultClassLoader();
/*     */   
/*     */   protected abstract Object firstInstance(Class paramClass) throws Exception;
/*     */   
/*     */   protected abstract Object nextInstance(Object paramObject) throws Exception;
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\core\AbstractClassGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */