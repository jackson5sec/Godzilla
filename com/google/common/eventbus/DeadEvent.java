/*    */ package com.google.common.eventbus;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.base.MoreObjects;
/*    */ import com.google.common.base.Preconditions;
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
/*    */ @Beta
/*    */ public class DeadEvent
/*    */ {
/*    */   private final Object source;
/*    */   private final Object event;
/*    */   
/*    */   public DeadEvent(Object source, Object event) {
/* 44 */     this.source = Preconditions.checkNotNull(source);
/* 45 */     this.event = Preconditions.checkNotNull(event);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getSource() {
/* 55 */     return this.source;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getEvent() {
/* 65 */     return this.event;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 70 */     return MoreObjects.toStringHelper(this).add("source", this.source).add("event", this.event).toString();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\eventbus\DeadEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */