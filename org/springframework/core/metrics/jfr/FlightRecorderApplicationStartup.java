/*    */ package org.springframework.core.metrics.jfr;
/*    */ 
/*    */ import java.util.Deque;
/*    */ import java.util.concurrent.ConcurrentLinkedDeque;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ import org.springframework.core.metrics.ApplicationStartup;
/*    */ import org.springframework.core.metrics.StartupStep;
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
/*    */ public class FlightRecorderApplicationStartup
/*    */   implements ApplicationStartup
/*    */ {
/* 41 */   private final AtomicLong currentSequenceId = new AtomicLong(0L);
/*    */   
/*    */   private final Deque<Long> currentSteps;
/*    */ 
/*    */   
/*    */   public FlightRecorderApplicationStartup() {
/* 47 */     this.currentSteps = new ConcurrentLinkedDeque<>();
/* 48 */     this.currentSteps.offerFirst(Long.valueOf(this.currentSequenceId.get()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public StartupStep start(String name) {
/* 54 */     long sequenceId = this.currentSequenceId.incrementAndGet();
/* 55 */     this.currentSteps.offerFirst(Long.valueOf(sequenceId));
/* 56 */     return new FlightRecorderStartupStep(sequenceId, name, ((Long)this.currentSteps
/* 57 */         .getFirst()).longValue(), committedStep -> this.currentSteps.removeFirstOccurrence(Long.valueOf(sequenceId)));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\metrics\jfr\FlightRecorderApplicationStartup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */