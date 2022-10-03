/*    */ package com.formdev.flatlaf.ui;
/*    */ 
/*    */ import com.formdev.flatlaf.util.MultiResolutionImageSupport;
/*    */ import com.formdev.flatlaf.util.ScaledImageIcon;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Image;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.swing.Icon;
/*    */ import javax.swing.ImageIcon;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FlatTitlePaneIcon
/*    */   extends ScaledImageIcon
/*    */ {
/*    */   private final List<Image> images;
/*    */   
/*    */   public static Icon create(List<Image> images, Dimension size) {
/* 36 */     List<Image> allImages = new ArrayList<>();
/* 37 */     for (Image image : images) {
/* 38 */       if (MultiResolutionImageSupport.isMultiResolutionImage(image)) {
/* 39 */         allImages.addAll(MultiResolutionImageSupport.getResolutionVariants(image)); continue;
/*    */       } 
/* 41 */       allImages.add(image);
/*    */     } 
/*    */ 
/*    */     
/* 45 */     allImages.sort((image1, image2) -> image1.getWidth(null) - image2.getWidth(null));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 50 */     return (Icon)new FlatTitlePaneIcon(allImages, size);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private FlatTitlePaneIcon(List<Image> images, Dimension size) {
/* 56 */     super(new ImageIcon(images.get(0)), size.width, size.height);
/* 57 */     this.images = images;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Image getResolutionVariant(int destImageWidth, int destImageHeight) {
/* 62 */     for (Image image : this.images) {
/* 63 */       if (destImageWidth <= image.getWidth(null) && destImageHeight <= image
/* 64 */         .getHeight(null)) {
/* 65 */         return image;
/*    */       }
/*    */     } 
/* 68 */     return this.images.get(this.images.size() - 1);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatTitlePaneIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */