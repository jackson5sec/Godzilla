/*    */ package org.springframework.core.task;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SyncTaskExecutor
/*    */   implements TaskExecutor, Serializable
/*    */ {
/*    */   public void execute(Runnable task) {
/* 49 */     Assert.notNull(task, "Runnable must not be null");
/* 50 */     task.run();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\task\SyncTaskExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */