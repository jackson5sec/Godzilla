/*    */ package org.springframework.core.metrics;
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
/*    */ public interface ApplicationStartup
/*    */ {
/* 34 */   public static final ApplicationStartup DEFAULT = new DefaultApplicationStartup();
/*    */   
/*    */   StartupStep start(String paramString);
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\metrics\ApplicationStartup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */