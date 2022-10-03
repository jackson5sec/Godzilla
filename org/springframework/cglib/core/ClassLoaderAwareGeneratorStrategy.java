/*    */ package org.springframework.cglib.core;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClassLoaderAwareGeneratorStrategy
/*    */   extends DefaultGeneratorStrategy
/*    */ {
/*    */   private final ClassLoader classLoader;
/*    */   
/*    */   public ClassLoaderAwareGeneratorStrategy(ClassLoader classLoader) {
/* 33 */     this.classLoader = classLoader;
/*    */   }
/*    */   
/*    */   public byte[] generate(ClassGenerator cg) throws Exception {
/*    */     ClassLoader threadContextClassLoader;
/* 38 */     if (this.classLoader == null) {
/* 39 */       return super.generate(cg);
/*    */     }
/*    */     
/* 42 */     Thread currentThread = Thread.currentThread();
/*    */     
/*    */     try {
/* 45 */       threadContextClassLoader = currentThread.getContextClassLoader();
/*    */     }
/* 47 */     catch (Throwable ex) {
/*    */       
/* 49 */       return super.generate(cg);
/*    */     } 
/*    */     
/* 52 */     boolean overrideClassLoader = !this.classLoader.equals(threadContextClassLoader);
/* 53 */     if (overrideClassLoader) {
/* 54 */       currentThread.setContextClassLoader(this.classLoader);
/*    */     }
/*    */     try {
/* 57 */       return super.generate(cg);
/*    */     } finally {
/*    */       
/* 60 */       if (overrideClassLoader)
/*    */       {
/* 62 */         currentThread.setContextClassLoader(threadContextClassLoader);
/*    */       }
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\core\ClassLoaderAwareGeneratorStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */