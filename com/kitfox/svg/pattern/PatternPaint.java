/*    */ package com.kitfox.svg.pattern;
/*    */ 
/*    */ import java.awt.Paint;
/*    */ import java.awt.PaintContext;
/*    */ import java.awt.Rectangle;
/*    */ import java.awt.RenderingHints;
/*    */ import java.awt.geom.AffineTransform;
/*    */ import java.awt.geom.Rectangle2D;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.awt.image.ColorModel;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PatternPaint
/*    */   implements Paint
/*    */ {
/*    */   BufferedImage source;
/*    */   AffineTransform xform;
/*    */   
/*    */   public PatternPaint(BufferedImage source, AffineTransform xform) {
/* 55 */     this.source = source;
/* 56 */     this.xform = xform;
/*    */   }
/*    */ 
/*    */   
/*    */   public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform xform, RenderingHints hints) {
/* 61 */     return new PatternPaintContext(this.source, deviceBounds, xform, this.xform);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getTransparency() {
/* 66 */     return this.source.getColorModel().getTransparency();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\pattern\PatternPaint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */