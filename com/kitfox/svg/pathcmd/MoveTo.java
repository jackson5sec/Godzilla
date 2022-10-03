/*    */ package com.kitfox.svg.pathcmd;
/*    */ 
/*    */ import java.awt.geom.GeneralPath;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MoveTo
/*    */   extends PathCommand
/*    */ {
/* 48 */   public float x = 0.0F;
/* 49 */   public float y = 0.0F;
/*    */ 
/*    */   
/*    */   public MoveTo() {}
/*    */ 
/*    */   
/*    */   public MoveTo(boolean isRelative, float x, float y) {
/* 56 */     super(isRelative);
/* 57 */     this.x = x;
/* 58 */     this.y = y;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void appendPath(GeneralPath path, BuildHistory hist) {
/* 65 */     float offx = this.isRelative ? hist.lastPoint.x : 0.0F;
/* 66 */     float offy = this.isRelative ? hist.lastPoint.y : 0.0F;
/*    */     
/* 68 */     path.moveTo(this.x + offx, this.y + offy);
/* 69 */     hist.setStartPoint(this.x + offx, this.y + offy);
/* 70 */     hist.setLastPoint(this.x + offx, this.y + offy);
/* 71 */     hist.setLastKnot(this.x + offx, this.y + offy);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNumKnotsAdded() {
/* 77 */     return 2;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 83 */     return "M " + this.x + " " + this.y;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\pathcmd\MoveTo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */