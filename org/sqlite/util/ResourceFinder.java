/*     */ package org.sqlite.util;
/*     */ 
/*     */ import java.net.URL;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceFinder
/*     */ {
/*     */   public static URL find(Class<?> referenceClass, String resourceFileName) {
/*  50 */     return find(referenceClass.getClassLoader(), referenceClass.getPackage(), resourceFileName);
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
/*     */   public static URL find(ClassLoader classLoader, Package basePackage, String resourceFileName) {
/*  64 */     return find(classLoader, basePackage.getName(), resourceFileName);
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
/*     */   public static URL find(ClassLoader classLoader, String packageName, String resourceFileName) {
/*  78 */     String packagePath = packagePath(packageName);
/*  79 */     String resourcePath = packagePath + resourceFileName;
/*  80 */     if (!resourcePath.startsWith("/")) {
/*  81 */       resourcePath = "/" + resourcePath;
/*     */     }
/*  83 */     return classLoader.getResource(resourcePath);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String packagePath(Class<?> referenceClass) {
/*  89 */     return packagePath(referenceClass.getPackage());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String packagePath(Package basePackage) {
/*  98 */     return packagePath(basePackage.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String packagePath(String packageName) {
/* 107 */     String packageAsPath = packageName.replaceAll("\\.", "/");
/* 108 */     return packageAsPath.endsWith("/") ? packageAsPath : (packageAsPath + "/");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlit\\util\ResourceFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */