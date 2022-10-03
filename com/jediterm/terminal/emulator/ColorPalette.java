/*    */ package com.jediterm.terminal.emulator;
/*    */ 
/*    */ import com.jediterm.terminal.TerminalColor;
/*    */ import java.awt.Color;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ import org.jetbrains.annotations.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ColorPalette
/*    */ {
/*    */   @NotNull
/*    */   public Color getForeground(@NotNull TerminalColor color) {
/* 15 */     if (color == null) $$$reportNull$$$0(0);  if (color.isIndexed()) {
/* 16 */       int colorIndex = color.getColorIndex();
/* 17 */       assertColorIndexIsLessThan16(colorIndex);
/* 18 */       if (getForegroundByColorIndex(colorIndex) == null) $$$reportNull$$$0(1);  return getForegroundByColorIndex(colorIndex);
/*    */     } 
/* 20 */     if (color.toAwtColor() == null) $$$reportNull$$$0(2);  return color.toAwtColor();
/*    */   }
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public Color getBackground(@NotNull TerminalColor color) {
/* 26 */     if (color == null) $$$reportNull$$$0(3);  if (color.isIndexed()) {
/* 27 */       int colorIndex = color.getColorIndex();
/* 28 */       assertColorIndexIsLessThan16(colorIndex);
/* 29 */       if (getBackgroundByColorIndex(colorIndex) == null) $$$reportNull$$$0(4);  return getBackgroundByColorIndex(colorIndex);
/*    */     } 
/* 31 */     if (color.toAwtColor() == null) $$$reportNull$$$0(5);  return color.toAwtColor();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private void assertColorIndexIsLessThan16(int colorIndex) {
/* 37 */     if (colorIndex < 0 || colorIndex >= 16)
/* 38 */       throw new AssertionError("Color index is out of bounds [0,15]: " + colorIndex); 
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public static TerminalColor getIndexedTerminalColor(int colorIndex) {
/* 43 */     return (colorIndex < 16) ? TerminalColor.index(colorIndex) : getXTerm256(colorIndex);
/*    */   }
/*    */   @Nullable
/*    */   private static TerminalColor getXTerm256(int colorIndex) {
/* 47 */     return (colorIndex < 256) ? COL_RES_256[colorIndex - 16] : null;
/*    */   }
/*    */ 
/*    */   
/* 51 */   private static final TerminalColor[] COL_RES_256 = new TerminalColor[240];
/*    */ 
/*    */   
/*    */   static {
/* 55 */     for (int red = 0; red < 6; red++) {
/* 56 */       for (int green = 0; green < 6; green++) {
/* 57 */         for (int blue = 0; blue < 6; blue++) {
/* 58 */           COL_RES_256[36 * red + 6 * green + blue] = new TerminalColor(getCubeColorValue(red), 
/* 59 */               getCubeColorValue(green), 
/* 60 */               getCubeColorValue(blue));
/*    */         }
/*    */       } 
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 67 */     for (int gray = 0; gray < 24; gray++) {
/* 68 */       int level = 10 * gray + 8;
/* 69 */       COL_RES_256[216 + gray] = new TerminalColor(level, level, level);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static int getCubeColorValue(int value) {
/* 74 */     return (value == 0) ? 0 : (40 * value + 55);
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   protected abstract Color getForegroundByColorIndex(int paramInt);
/*    */   
/*    */   @NotNull
/*    */   protected abstract Color getBackgroundByColorIndex(int paramInt);
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\emulator\ColorPalette.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */