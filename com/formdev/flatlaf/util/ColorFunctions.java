/*     */ package com.formdev.flatlaf.util;
/*     */ 
/*     */ import java.awt.Color;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ColorFunctions
/*     */ {
/*     */   public static Color applyFunctions(Color color, ColorFunction... functions) {
/*  29 */     float[] hsl = HSLColor.fromRGB(color);
/*  30 */     float alpha = color.getAlpha() / 255.0F;
/*  31 */     float[] hsla = { hsl[0], hsl[1], hsl[2], alpha * 100.0F };
/*     */     
/*  33 */     for (ColorFunction function : functions) {
/*  34 */       function.apply(hsla);
/*     */     }
/*  36 */     return HSLColor.toRGB(hsla[0], hsla[1], hsla[2], hsla[3] / 100.0F);
/*     */   }
/*     */   
/*     */   public static float clamp(float value) {
/*  40 */     return (value < 0.0F) ? 0.0F : ((value > 100.0F) ? 100.0F : value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Color mix(Color color1, Color color2, float weight) {
/*  57 */     if (weight >= 1.0F)
/*  58 */       return color1; 
/*  59 */     if (weight <= 0.0F) {
/*  60 */       return color2;
/*     */     }
/*  62 */     int r1 = color1.getRed();
/*  63 */     int g1 = color1.getGreen();
/*  64 */     int b1 = color1.getBlue();
/*  65 */     int a1 = color1.getAlpha();
/*     */     
/*  67 */     int r2 = color2.getRed();
/*  68 */     int g2 = color2.getGreen();
/*  69 */     int b2 = color2.getBlue();
/*  70 */     int a2 = color2.getAlpha();
/*     */     
/*  72 */     return new Color(
/*  73 */         Math.round(r2 + (r1 - r2) * weight), 
/*  74 */         Math.round(g2 + (g1 - g2) * weight), 
/*  75 */         Math.round(b2 + (b1 - b2) * weight), 
/*  76 */         Math.round(a2 + (a1 - a2) * weight));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface ColorFunction
/*     */   {
/*     */     void apply(float[] param1ArrayOffloat);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class HSLIncreaseDecrease
/*     */     implements ColorFunction
/*     */   {
/*     */     public final int hslIndex;
/*     */     
/*     */     public final boolean increase;
/*     */     
/*     */     public final float amount;
/*     */     
/*     */     public final boolean relative;
/*     */     
/*     */     public final boolean autoInverse;
/*     */ 
/*     */     
/*     */     public HSLIncreaseDecrease(int hslIndex, boolean increase, float amount, boolean relative, boolean autoInverse) {
/* 103 */       this.hslIndex = hslIndex;
/* 104 */       this.increase = increase;
/* 105 */       this.amount = amount;
/* 106 */       this.relative = relative;
/* 107 */       this.autoInverse = autoInverse;
/*     */     }
/*     */ 
/*     */     
/*     */     public void apply(float[] hsla) {
/* 112 */       float amount2 = this.increase ? this.amount : -this.amount;
/*     */       
/* 114 */       if (this.hslIndex == 0) {
/*     */         
/* 116 */         hsla[0] = (hsla[0] + amount2) % 360.0F;
/*     */         
/*     */         return;
/*     */       } 
/* 120 */       amount2 = (this.autoInverse && shouldInverse(hsla)) ? -amount2 : amount2;
/* 121 */       hsla[this.hslIndex] = ColorFunctions.clamp(this.relative ? (hsla[this.hslIndex] * (100.0F + amount2) / 100.0F) : (hsla[this.hslIndex] + amount2));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean shouldInverse(float[] hsla) {
/* 127 */       return this.increase ? ((hsla[this.hslIndex] > 65.0F)) : ((hsla[this.hslIndex] < 35.0F));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Fade
/*     */     implements ColorFunction
/*     */   {
/*     */     public final float amount;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Fade(float amount) {
/* 144 */       this.amount = amount;
/*     */     }
/*     */ 
/*     */     
/*     */     public void apply(float[] hsla) {
/* 149 */       hsla[3] = ColorFunctions.clamp(this.amount);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\util\ColorFunctions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */