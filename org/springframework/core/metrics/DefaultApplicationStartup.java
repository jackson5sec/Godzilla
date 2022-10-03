/*    */ package org.springframework.core.metrics;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.function.Supplier;
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
/*    */ class DefaultApplicationStartup
/*    */   implements ApplicationStartup
/*    */ {
/* 32 */   private static final DefaultStartupStep DEFAULT_STARTUP_STEP = new DefaultStartupStep();
/*    */ 
/*    */   
/*    */   public DefaultStartupStep start(String name) {
/* 36 */     return DEFAULT_STARTUP_STEP;
/*    */   }
/*    */   
/*    */   static class DefaultStartupStep
/*    */     implements StartupStep
/*    */   {
/* 42 */     private final DefaultTags TAGS = new DefaultTags();
/*    */ 
/*    */     
/*    */     public String getName() {
/* 46 */       return "default";
/*    */     }
/*    */ 
/*    */     
/*    */     public long getId() {
/* 51 */       return 0L;
/*    */     }
/*    */ 
/*    */     
/*    */     public Long getParentId() {
/* 56 */       return null;
/*    */     }
/*    */ 
/*    */     
/*    */     public StartupStep.Tags getTags() {
/* 61 */       return this.TAGS;
/*    */     }
/*    */ 
/*    */     
/*    */     public StartupStep tag(String key, String value) {
/* 66 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public StartupStep tag(String key, Supplier<String> value) {
/* 71 */       return this;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public void end() {}
/*    */ 
/*    */ 
/*    */     
/*    */     static class DefaultTags
/*    */       implements StartupStep.Tags
/*    */     {
/*    */       public Iterator<StartupStep.Tag> iterator() {
/* 84 */         return Collections.emptyIterator();
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\metrics\DefaultApplicationStartup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */