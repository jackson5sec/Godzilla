/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DecoratingClassLoader
/*     */   extends ClassLoader
/*     */ {
/*     */   static {
/*  38 */     ClassLoader.registerAsParallelCapable();
/*     */   }
/*     */ 
/*     */   
/*  42 */   private final Set<String> excludedPackages = Collections.newSetFromMap(new ConcurrentHashMap<>(8));
/*     */   
/*  44 */   private final Set<String> excludedClasses = Collections.newSetFromMap(new ConcurrentHashMap<>(8));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DecoratingClassLoader(@Nullable ClassLoader parent) {
/*  58 */     super(parent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void excludePackage(String packageName) {
/*  69 */     Assert.notNull(packageName, "Package name must not be null");
/*  70 */     this.excludedPackages.add(packageName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void excludeClass(String className) {
/*  80 */     Assert.notNull(className, "Class name must not be null");
/*  81 */     this.excludedClasses.add(className);
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
/*     */   protected boolean isExcluded(String className) {
/*  94 */     if (this.excludedClasses.contains(className)) {
/*  95 */       return true;
/*     */     }
/*  97 */     for (String packageName : this.excludedPackages) {
/*  98 */       if (className.startsWith(packageName)) {
/*  99 */         return true;
/*     */       }
/*     */     } 
/* 102 */     return false;
/*     */   }
/*     */   
/*     */   public DecoratingClassLoader() {}
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\DecoratingClassLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */