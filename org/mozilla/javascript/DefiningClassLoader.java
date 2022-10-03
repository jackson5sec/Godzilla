/*    */ package org.mozilla.javascript;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefiningClassLoader
/*    */   extends ClassLoader
/*    */   implements GeneratedClassLoader
/*    */ {
/*    */   private final ClassLoader parentLoader;
/*    */   
/*    */   public DefiningClassLoader() {
/* 16 */     this.parentLoader = getClass().getClassLoader();
/*    */   }
/*    */   
/*    */   public DefiningClassLoader(ClassLoader parentLoader) {
/* 20 */     this.parentLoader = parentLoader;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Class<?> defineClass(String name, byte[] data) {
/* 27 */     return defineClass(name, data, 0, data.length, SecurityUtilities.getProtectionDomain(getClass()));
/*    */   }
/*    */ 
/*    */   
/*    */   public void linkClass(Class<?> cl) {
/* 32 */     resolveClass(cl);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
/* 39 */     Class<?> cl = findLoadedClass(name);
/* 40 */     if (cl == null) {
/* 41 */       if (this.parentLoader != null) {
/* 42 */         cl = this.parentLoader.loadClass(name);
/*    */       } else {
/* 44 */         cl = findSystemClass(name);
/*    */       } 
/*    */     }
/* 47 */     if (resolve) {
/* 48 */       resolveClass(cl);
/*    */     }
/* 50 */     return cl;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\DefiningClassLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */