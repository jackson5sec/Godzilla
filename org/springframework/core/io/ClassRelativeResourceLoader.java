/*    */ package org.springframework.core.io;
/*    */ 
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClassRelativeResourceLoader
/*    */   extends DefaultResourceLoader
/*    */ {
/*    */   private final Class<?> clazz;
/*    */   
/*    */   public ClassRelativeResourceLoader(Class<?> clazz) {
/* 41 */     Assert.notNull(clazz, "Class must not be null");
/* 42 */     this.clazz = clazz;
/* 43 */     setClassLoader(clazz.getClassLoader());
/*    */   }
/*    */ 
/*    */   
/*    */   protected Resource getResourceByPath(String path) {
/* 48 */     return new ClassRelativeContextResource(path, this.clazz);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static class ClassRelativeContextResource
/*    */     extends ClassPathResource
/*    */     implements ContextResource
/*    */   {
/*    */     private final Class<?> clazz;
/*    */ 
/*    */     
/*    */     public ClassRelativeContextResource(String path, Class<?> clazz) {
/* 61 */       super(path, clazz);
/* 62 */       this.clazz = clazz;
/*    */     }
/*    */ 
/*    */     
/*    */     public String getPathWithinContext() {
/* 67 */       return getPath();
/*    */     }
/*    */ 
/*    */     
/*    */     public Resource createRelative(String relativePath) {
/* 72 */       String pathToUse = StringUtils.applyRelativePath(getPath(), relativePath);
/* 73 */       return new ClassRelativeContextResource(pathToUse, this.clazz);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\ClassRelativeResourceLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */