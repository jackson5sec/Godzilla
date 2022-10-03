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
/*    */ public class Vertical
/*    */   extends PathCommand
/*    */ {
/* 48 */   public float y = 0.0F;
/*    */ 
/*    */ 
/*    */   
/*    */   public Vertical() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return "V " + this.y;
/*    */   }
/*    */   
/*    */   public Vertical(boolean isRelative, float y) {
/* 61 */     super(isRelative);
/* 62 */     this.y = y;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void appendPath(GeneralPath path, BuildHistory hist) {
/* 69 */     float offx = hist.lastPoint.x;
/* 70 */     float offy = this.isRelative ? hist.lastPoint.y : 0.0F;
/*    */     
/* 72 */     path.lineTo(offx, this.y + offy);
/* 73 */     hist.setLastPoint(offx, this.y + offy);
/* 74 */     hist.setLastKnot(offx, this.y + offy);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNumKnotsAdded() {
/* 80 */     return 2;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\pathcmd\Vertical.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */