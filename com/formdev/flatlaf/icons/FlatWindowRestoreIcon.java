/*    */ package com.formdev.flatlaf.icons;
/*    */ 
/*    */ import com.formdev.flatlaf.ui.FlatUIUtils;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.geom.Area;
/*    */ import java.awt.geom.Path2D;
/*    */ import java.awt.geom.Rectangle2D;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FlatWindowRestoreIcon
/*    */   extends FlatWindowAbstractIcon
/*    */ {
/*    */   protected void paintIconAt1x(Graphics2D g, int x, int y, int width, int height, double scaleFactor) {
/* 38 */     int iwh = (int)(10.0D * scaleFactor);
/* 39 */     int ix = x + (width - iwh) / 2;
/* 40 */     int iy = y + (height - iwh) / 2;
/* 41 */     int thickness = (int)scaleFactor;
/*    */     
/* 43 */     int rwh = (int)(8.0D * scaleFactor);
/* 44 */     int ro2 = iwh - rwh;
/*    */     
/* 46 */     Path2D r1 = FlatUIUtils.createRectangle((ix + ro2), iy, rwh, rwh, thickness);
/* 47 */     Path2D r2 = FlatUIUtils.createRectangle(ix, (iy + ro2), rwh, rwh, thickness);
/*    */     
/* 49 */     Area area = new Area(r1);
/* 50 */     area.subtract(new Area(new Rectangle2D.Float(ix, (iy + ro2), rwh, rwh)));
/* 51 */     g.fill(area);
/*    */     
/* 53 */     g.fill(r2);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\icons\FlatWindowRestoreIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */