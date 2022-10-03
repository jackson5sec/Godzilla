/*    */ package org.springframework.core.env;
/*    */ 
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.Set;
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
/*    */ public class MissingRequiredPropertiesException
/*    */   extends IllegalStateException
/*    */ {
/* 34 */   private final Set<String> missingRequiredProperties = new LinkedHashSet<>();
/*    */ 
/*    */   
/*    */   void addMissingRequiredProperty(String key) {
/* 38 */     this.missingRequiredProperties.add(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 43 */     return "The following properties were declared as required but could not be resolved: " + 
/* 44 */       getMissingRequiredProperties();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<String> getMissingRequiredProperties() {
/* 54 */     return this.missingRequiredProperties;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\env\MissingRequiredPropertiesException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */