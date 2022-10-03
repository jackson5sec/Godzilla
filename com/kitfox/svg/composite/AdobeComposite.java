/*    */ package com.kitfox.svg.composite;
/*    */ 
/*    */ import java.awt.Composite;
/*    */ import java.awt.CompositeContext;
/*    */ import java.awt.RenderingHints;
/*    */ import java.awt.image.ColorModel;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AdobeComposite
/*    */   implements Composite
/*    */ {
/*    */   public static final int CT_NORMAL = 0;
/*    */   public static final int CT_MULTIPLY = 1;
/*    */   public static final int CT_LAST = 2;
/*    */   final int compositeType;
/*    */   final float extraAlpha;
/*    */   
/*    */   public AdobeComposite(int compositeType, float extraAlpha) {
/* 65 */     this.compositeType = compositeType;
/* 66 */     this.extraAlpha = extraAlpha;
/*    */     
/* 68 */     if (compositeType < 0 || compositeType >= 2)
/*    */     {
/* 70 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Invalid composite type");
/*    */     }
/*    */     
/* 73 */     if (extraAlpha < 0.0F || extraAlpha > 1.0F)
/*    */     {
/* 75 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Invalid alpha"); } 
/*    */   }
/*    */   
/*    */   public int getCompositeType() {
/* 79 */     return this.compositeType;
/*    */   }
/*    */   
/*    */   public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
/* 83 */     return new AdobeCompositeContext(this.compositeType, this.extraAlpha);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\composite\AdobeComposite.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */