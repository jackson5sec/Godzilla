/*    */ package com.kitfox.svg.animation;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.regex.Pattern;
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
/*    */ public class TimeCompound
/*    */   extends TimeBase
/*    */ {
/* 53 */   static final Pattern patPlus = Pattern.compile("\\+");
/*    */ 
/*    */ 
/*    */   
/*    */   final List<TimeBase> componentTimes;
/*    */ 
/*    */ 
/*    */   
/*    */   private AnimationElement parent;
/*    */ 
/*    */ 
/*    */   
/*    */   public TimeCompound(List<TimeBase> timeBases) {
/* 66 */     this.componentTimes = Collections.unmodifiableList(timeBases);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public double evalTime() {
/* 72 */     double agg = 0.0D;
/*    */     
/* 74 */     for (TimeBase timeEle : this.componentTimes) {
/* 75 */       double time = timeEle.evalTime();
/* 76 */       agg += time;
/*    */     } 
/*    */     
/* 79 */     return agg;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setParentElement(AnimationElement ele) {
/* 85 */     this.parent = ele;
/*    */     
/* 87 */     for (TimeBase timeEle : this.componentTimes)
/* 88 */       timeEle.setParentElement(ele); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\TimeCompound.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */