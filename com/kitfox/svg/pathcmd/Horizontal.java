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
/*    */ public class Horizontal
/*    */   extends PathCommand
/*    */ {
/* 48 */   public float x = 0.0F;
/*    */ 
/*    */ 
/*    */   
/*    */   public Horizontal() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return "H " + this.x;
/*    */   }
/*    */   
/*    */   public Horizontal(boolean isRelative, float x) {
/* 61 */     super(isRelative);
/* 62 */     this.x = x;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void appendPath(GeneralPath path, BuildHistory hist) {
/* 70 */     float offx = this.isRelative ? hist.lastPoint.x : 0.0F;
/* 71 */     float offy = hist.lastPoint.y;
/*    */     
/* 73 */     path.lineTo(this.x + offx, offy);
/* 74 */     hist.setLastPoint(this.x + offx, offy);
/* 75 */     hist.setLastKnot(this.x + offx, offy);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNumKnotsAdded() {
/* 81 */     return 2;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\pathcmd\Horizontal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */