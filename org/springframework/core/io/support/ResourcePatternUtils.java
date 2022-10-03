/*    */ package org.springframework.core.io.support;
/*    */ 
/*    */ import org.springframework.core.io.ResourceLoader;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.ResourceUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ResourcePatternUtils
/*    */ {
/*    */   public static boolean isUrl(@Nullable String resourceLocation) {
/* 46 */     return (resourceLocation != null && (resourceLocation
/* 47 */       .startsWith("classpath*:") || 
/* 48 */       ResourceUtils.isUrl(resourceLocation)));
/*    */   }
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
/*    */   public static ResourcePatternResolver getResourcePatternResolver(@Nullable ResourceLoader resourceLoader) {
/* 62 */     if (resourceLoader instanceof ResourcePatternResolver) {
/* 63 */       return (ResourcePatternResolver)resourceLoader;
/*    */     }
/* 65 */     if (resourceLoader != null) {
/* 66 */       return new PathMatchingResourcePatternResolver(resourceLoader);
/*    */     }
/*    */     
/* 69 */     return new PathMatchingResourcePatternResolver();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\support\ResourcePatternUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */