/*    */ package com.kitfox.svg.composite;
/*    */ 
/*    */ import java.awt.CompositeContext;
/*    */ import java.awt.image.Raster;
/*    */ import java.awt.image.WritableRaster;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AdobeCompositeContext
/*    */   implements CompositeContext
/*    */ {
/*    */   final int compositeType;
/*    */   final float extraAlpha;
/* 51 */   float[] rgba_src = new float[4];
/* 52 */   float[] rgba_dstIn = new float[4];
/* 53 */   float[] rgba_dstOut = new float[4];
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AdobeCompositeContext(int compositeType, float extraAlpha) {
/* 61 */     this.compositeType = compositeType;
/* 62 */     this.extraAlpha = extraAlpha;
/*    */     
/* 64 */     this.rgba_dstOut[3] = 1.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public void compose(Raster src, Raster dstIn, WritableRaster dstOut) {
/* 69 */     int width = src.getWidth();
/* 70 */     int height = src.getHeight();
/*    */     
/* 72 */     for (int j = 0; j < height; j++) {
/*    */       
/* 74 */       for (int i = 0; i < width; i++) {
/*    */         
/* 76 */         src.getPixel(i, j, this.rgba_src);
/* 77 */         dstIn.getPixel(i, j, this.rgba_dstIn);
/*    */ 
/*    */         
/* 80 */         if (this.rgba_src[3] != 0.0F) {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */           
/* 86 */           float alpha = this.rgba_src[3];
/*    */           
/* 88 */           switch (this.compositeType) {
/*    */ 
/*    */             
/*    */             default:
/* 92 */               this.rgba_dstOut[0] = this.rgba_src[0] * alpha + this.rgba_dstIn[0] * (1.0F - alpha);
/* 93 */               this.rgba_dstOut[1] = this.rgba_src[1] * alpha + this.rgba_dstIn[1] * (1.0F - alpha);
/* 94 */               this.rgba_dstOut[2] = this.rgba_src[2] * alpha + this.rgba_dstIn[2] * (1.0F - alpha);
/*    */               break;
/*    */             case 1:
/* 97 */               this.rgba_dstOut[0] = this.rgba_src[0] * this.rgba_dstIn[0] * alpha + this.rgba_dstIn[0] * (1.0F - alpha);
/* 98 */               this.rgba_dstOut[1] = this.rgba_src[1] * this.rgba_dstIn[1] * alpha + this.rgba_dstIn[1] * (1.0F - alpha);
/* 99 */               this.rgba_dstOut[2] = this.rgba_src[2] * this.rgba_dstIn[2] * alpha + this.rgba_dstIn[2] * (1.0F - alpha);
/*    */               break;
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void dispose() {}
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\composite\AdobeCompositeContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */