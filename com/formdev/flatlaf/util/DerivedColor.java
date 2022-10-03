/*    */ package com.formdev.flatlaf.util;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import javax.swing.plaf.ColorUIResource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DerivedColor
/*    */   extends ColorUIResource
/*    */ {
/*    */   private final ColorFunctions.ColorFunction[] functions;
/*    */   private boolean hasBaseOfDefaultColor;
/*    */   private int baseOfDefaultColorRGB;
/*    */   
/*    */   public DerivedColor(Color defaultColor, ColorFunctions.ColorFunction... functions) {
/* 39 */     super((defaultColor != null) ? defaultColor : Color.red);
/* 40 */     this.functions = functions;
/*    */   }
/*    */   
/*    */   public Color derive(Color baseColor) {
/* 44 */     if ((this.hasBaseOfDefaultColor && this.baseOfDefaultColorRGB == baseColor.getRGB()) || baseColor == this) {
/* 45 */       return this;
/*    */     }
/* 47 */     Color result = ColorFunctions.applyFunctions(baseColor, this.functions);
/*    */ 
/*    */ 
/*    */     
/* 51 */     if (!this.hasBaseOfDefaultColor && result.getRGB() == getRGB()) {
/* 52 */       this.hasBaseOfDefaultColor = true;
/* 53 */       this.baseOfDefaultColorRGB = baseColor.getRGB();
/*    */     } 
/*    */     
/* 56 */     return result;
/*    */   }
/*    */   
/*    */   public ColorFunctions.ColorFunction[] getFunctions() {
/* 60 */     return this.functions;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\util\DerivedColor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */