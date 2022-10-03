/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.FileCopyUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OverridingClassLoader
/*     */   extends DecoratingClassLoader
/*     */ {
/*  40 */   public static final String[] DEFAULT_EXCLUDED_PACKAGES = new String[] { "java.", "javax.", "sun.", "oracle.", "javassist.", "org.aspectj.", "net.sf.cglib." };
/*     */   private static final String CLASS_FILE_SUFFIX = ".class";
/*     */   @Nullable
/*     */   private final ClassLoader overrideDelegate;
/*     */   
/*     */   static {
/*  46 */     ClassLoader.registerAsParallelCapable();
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
/*     */   public OverridingClassLoader(@Nullable ClassLoader parent) {
/*  59 */     this(parent, (ClassLoader)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OverridingClassLoader(@Nullable ClassLoader parent, @Nullable ClassLoader overrideDelegate) {
/*  69 */     super(parent);
/*  70 */     this.overrideDelegate = overrideDelegate;
/*  71 */     for (String packageName : DEFAULT_EXCLUDED_PACKAGES) {
/*  72 */       excludePackage(packageName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> loadClass(String name) throws ClassNotFoundException {
/*  79 */     if (this.overrideDelegate != null && isEligibleForOverriding(name)) {
/*  80 */       return this.overrideDelegate.loadClass(name);
/*     */     }
/*  82 */     return super.loadClass(name);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
/*  87 */     if (isEligibleForOverriding(name)) {
/*  88 */       Class<?> result = loadClassForOverriding(name);
/*  89 */       if (result != null) {
/*  90 */         if (resolve) {
/*  91 */           resolveClass(result);
/*     */         }
/*  93 */         return result;
/*     */       } 
/*     */     } 
/*  96 */     return super.loadClass(name, resolve);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isEligibleForOverriding(String className) {
/* 107 */     return !isExcluded(className);
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
/*     */   @Nullable
/*     */   protected Class<?> loadClassForOverriding(String name) throws ClassNotFoundException {
/* 120 */     Class<?> result = findLoadedClass(name);
/* 121 */     if (result == null) {
/* 122 */       byte[] bytes = loadBytesForClass(name);
/* 123 */       if (bytes != null) {
/* 124 */         result = defineClass(name, bytes, 0, bytes.length);
/*     */       }
/*     */     } 
/* 127 */     return result;
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
/*     */   @Nullable
/*     */   protected byte[] loadBytesForClass(String name) throws ClassNotFoundException {
/* 142 */     InputStream is = openStreamForClass(name);
/* 143 */     if (is == null) {
/* 144 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 148 */       byte[] bytes = FileCopyUtils.copyToByteArray(is);
/*     */       
/* 150 */       return transformIfNecessary(name, bytes);
/*     */     }
/* 152 */     catch (IOException ex) {
/* 153 */       throw new ClassNotFoundException("Cannot load resource for class [" + name + "]", ex);
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
/*     */   @Nullable
/*     */   protected InputStream openStreamForClass(String name) {
/* 166 */     String internalName = name.replace('.', '/') + ".class";
/* 167 */     return getParent().getResourceAsStream(internalName);
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
/*     */   protected byte[] transformIfNecessary(String name, byte[] bytes) {
/* 180 */     return bytes;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\OverridingClassLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */