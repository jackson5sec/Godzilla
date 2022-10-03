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
/*    */ public class TimeSum
/*    */   extends TimeBase
/*    */ {
/*    */   TimeBase t1;
/*    */   TimeBase t2;
/*    */   boolean add;
/*    */   
/*    */   public TimeSum(TimeBase t1, TimeBase t2, boolean add) {
/* 55 */     this.t1 = t1;
/* 56 */     this.t2 = t2;
/* 57 */     this.add = add;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public double evalTime() {
/* 63 */     return this.add ? (this.t1.evalTime() + this.t2.evalTime()) : (this.t1.evalTime() - this.t2.evalTime());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setParentElement(AnimationElement ele) {
/* 69 */     this.t1.setParentElement(ele);
/* 70 */     this.t2.setParentElement(ele);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\TimeSum.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */