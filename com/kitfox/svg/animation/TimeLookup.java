/*    */ package com.kitfox.svg.animation;
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
/*    */ public class TimeLookup
/*    */   extends TimeBase
/*    */ {
/*    */   private AnimationElement parent;
/*    */   String node;
/*    */   String event;
/*    */   String paramList;
/*    */   
/*    */   public TimeLookup(AnimationElement parent, String node, String event, String paramList) {
/* 70 */     this.parent = parent;
/* 71 */     this.node = node;
/* 72 */     this.event = event;
/* 73 */     this.paramList = paramList;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public double evalTime() {
/* 79 */     return 0.0D;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setParentElement(AnimationElement ele) {
/* 85 */     this.parent = ele;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\TimeLookup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */