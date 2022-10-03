/*    */ package com.formdev.flatlaf.icons;
/*    */ 
/*    */ import com.formdev.flatlaf.ui.FlatUIUtils;
/*    */ import java.awt.Graphics2D;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FlatWindowMaximizeIcon
/*    */   extends FlatWindowAbstractIcon
/*    */ {
/*    */   protected void paintIconAt1x(Graphics2D g, int x, int y, int width, int height, double scaleFactor) {
/* 35 */     int iwh = (int)(10.0D * scaleFactor);
/* 36 */     int ix = x + (width - iwh) / 2;
/* 37 */     int iy = y + (height - iwh) / 2;
/* 38 */     int thickness = (int)scaleFactor;
/*    */     
/* 40 */     g.fill(FlatUIUtils.createRectangle(ix, iy, iwh, iwh, thickness));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\icons\FlatWindowMaximizeIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */