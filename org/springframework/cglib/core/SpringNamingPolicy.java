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
/*    */ 
/*    */ 
/*    */ public class SpringNamingPolicy
/*    */   extends DefaultNamingPolicy
/*    */ {
/* 32 */   public static final SpringNamingPolicy INSTANCE = new SpringNamingPolicy();
/*    */ 
/*    */   
/*    */   protected String getTag() {
/* 36 */     return "BySpringCGLIB";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\core\SpringNamingPolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */