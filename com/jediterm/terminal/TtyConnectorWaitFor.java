/*    */ package com.jediterm.terminal;
/*    */ 
/*    */ import com.google.common.base.Predicate;
/*    */ import java.util.concurrent.ArrayBlockingQueue;
/*    */ import java.util.concurrent.BlockingQueue;
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.Future;
/*    */ import org.apache.log4j.Logger;
/*    */ 
/*    */ public class TtyConnectorWaitFor
/*    */ {
/* 12 */   private static final Logger LOG = Logger.getLogger(TtyConnectorWaitFor.class);
/*    */   
/*    */   private final Future<?> myWaitForThreadFuture;
/* 15 */   private final BlockingQueue<Predicate<Integer>> myTerminationCallback = new ArrayBlockingQueue<>(1);
/*    */   
/*    */   public void detach() {
/* 18 */     this.myWaitForThreadFuture.cancel(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public TtyConnectorWaitFor(final TtyConnector ttyConnector, ExecutorService executor) {
/* 23 */     this.myWaitForThreadFuture = executor.submit(new Runnable()
/*    */         {
/*    */           public void run() {
/* 26 */             int exitCode = 0;
/*    */             try {
/*    */               while (true) {
/*    */                 try {
/* 30 */                   exitCode = ttyConnector.waitFor();
/*    */                   
/*    */                   break;
/* 33 */                 } catch (InterruptedException e) {
/* 34 */                   TtyConnectorWaitFor.LOG.debug(e);
/*    */                 } 
/*    */               } 
/*    */             } finally {
/*    */               
/*    */               try {
/* 40 */                 if (!TtyConnectorWaitFor.this.myWaitForThreadFuture.isCancelled()) {
/* 41 */                   ((Predicate)TtyConnectorWaitFor.this.myTerminationCallback.take()).apply(Integer.valueOf(exitCode));
/*    */                 }
/*    */               }
/* 44 */               catch (InterruptedException e) {
/* 45 */                 TtyConnectorWaitFor.LOG.info(e);
/*    */               } 
/*    */             } 
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   public void setTerminationCallback(Predicate<Integer> r) {
/* 53 */     this.myTerminationCallback.offer(r);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\TtyConnectorWaitFor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */