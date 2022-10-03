/*    */ package com.kitfox.svg.pathcmd;
/*    */ 
/*    */ import java.awt.geom.Point2D;
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
/*    */ public class BuildHistory
/*    */ {
/* 54 */   Point2D.Float startPoint = new Point2D.Float();
/* 55 */   Point2D.Float lastPoint = new Point2D.Float();
/* 56 */   Point2D.Float lastKnot = new Point2D.Float();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   boolean init;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setStartPoint(float x, float y) {
/* 69 */     this.startPoint.setLocation(x, y);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setLastPoint(float x, float y) {
/* 74 */     this.lastPoint.setLocation(x, y);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setLastKnot(float x, float y) {
/* 79 */     this.lastKnot.setLocation(x, y);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\pathcmd\BuildHistory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */