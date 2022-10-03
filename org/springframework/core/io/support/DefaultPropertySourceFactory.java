/*    */ package org.springframework.core.io.support;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.springframework.core.env.PropertySource;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public class DefaultPropertySourceFactory
/*    */   implements PropertySourceFactory
/*    */ {
/*    */   public PropertySource<?> createPropertySource(@Nullable String name, EncodedResource resource) throws IOException {
/* 37 */     return (name != null) ? (PropertySource<?>)new ResourcePropertySource(name, resource) : (PropertySource<?>)new ResourcePropertySource(resource);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\support\DefaultPropertySourceFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */