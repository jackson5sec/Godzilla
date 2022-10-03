/*    */ package org.springframework.util;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import java.util.concurrent.atomic.AtomicLong;
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
/*    */ public class SimpleIdGenerator
/*    */   implements IdGenerator
/*    */ {
/* 31 */   private final AtomicLong leastSigBits = new AtomicLong();
/*    */ 
/*    */ 
/*    */   
/*    */   public UUID generateId() {
/* 36 */     return new UUID(0L, this.leastSigBits.incrementAndGet());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\SimpleIdGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */