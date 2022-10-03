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
/*    */ public class LineTo
/*    */   extends PathCommand
/*    */ {
/* 48 */   public float x = 0.0F;
/* 49 */   public float y = 0.0F;
/*    */ 
/*    */   
/*    */   public LineTo() {}
/*    */ 
/*    */   
/*    */   public LineTo(boolean isRelative, float x, float y) {
/* 56 */     super(isRelative);
/* 57 */     this.x = x;
/* 58 */     this.y = y;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void appendPath(GeneralPath path, BuildHistory hist) {
/* 66 */     float offx = this.isRelative ? hist.lastPoint.x : 0.0F;
/* 67 */     float offy = this.isRelative ? hist.lastPoint.y : 0.0F;
/*    */     
/* 69 */     path.lineTo(this.x + offx, this.y + offy);
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
/* 83 */     return "L " + this.x + " " + this.y;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\pathcmd\LineTo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */