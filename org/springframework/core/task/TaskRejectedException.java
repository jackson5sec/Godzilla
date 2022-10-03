/*    */ package org.springframework.core.task;
/*    */ 
/*    */ import java.util.concurrent.RejectedExecutionException;
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
/*    */ public class TaskRejectedException
/*    */   extends RejectedExecutionException
/*    */ {
/*    */   public TaskRejectedException(String msg) {
/* 39 */     super(msg);
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
/*    */   public TaskRejectedException(String msg, Throwable cause) {
/* 51 */     super(msg, cause);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\task\TaskRejectedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */