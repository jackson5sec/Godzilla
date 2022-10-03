/*    */ package org.springframework.core.env;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Properties;
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
/*    */ public class PropertiesPropertySource
/*    */   extends MapPropertySource
/*    */ {
/*    */   public PropertiesPropertySource(String name, Properties source) {
/* 40 */     super(name, source);
/*    */   }
/*    */   
/*    */   protected PropertiesPropertySource(String name, Map<String, Object> source) {
/* 44 */     super(name, source);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String[] getPropertyNames() {
/* 50 */     synchronized (this.source) {
/* 51 */       return super.getPropertyNames();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\env\PropertiesPropertySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */