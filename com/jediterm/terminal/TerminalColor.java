/*    */ package com.jediterm.terminal;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Supplier;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ import org.jetbrains.annotations.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TerminalColor
/*    */ {
/* 14 */   public static final TerminalColor BLACK = index(0);
/* 15 */   public static final TerminalColor WHITE = index(15);
/*    */   
/*    */   private final int myColorIndex;
/*    */   private final Color myColor;
/*    */   private final Supplier<Color> myColorSupplier;
/*    */   
/*    */   public TerminalColor(int colorIndex) {
/* 22 */     this(colorIndex, (Color)null, (Supplier<Color>)null);
/*    */   }
/*    */   
/*    */   public TerminalColor(int r, int g, int b) {
/* 26 */     this(-1, new Color(r, g, b), (Supplier<Color>)null);
/*    */   }
/*    */   
/*    */   public TerminalColor(@NotNull Supplier<Color> colorSupplier) {
/* 30 */     this(-1, (Color)null, colorSupplier);
/*    */   }
/*    */   
/*    */   private TerminalColor(int colorIndex, @Nullable Color color, @Nullable Supplier<Color> colorSupplier) {
/* 34 */     if (colorIndex != -1) {
/* 35 */       assert color == null;
/* 36 */       assert colorSupplier == null;
/*    */     }
/* 38 */     else if (color != null) {
/* 39 */       assert colorSupplier == null;
/*    */     } else {
/*    */       
/* 42 */       assert colorSupplier != null;
/*    */     } 
/* 44 */     this.myColorIndex = colorIndex;
/* 45 */     this.myColor = color;
/* 46 */     this.myColorSupplier = colorSupplier;
/*    */   }
/*    */   @NotNull
/*    */   public static TerminalColor index(int colorIndex) {
/* 50 */     return new TerminalColor(colorIndex);
/*    */   }
/*    */   
/*    */   public static TerminalColor rgb(int r, int g, int b) {
/* 54 */     return new TerminalColor(r, g, b);
/*    */   }
/*    */   
/*    */   public boolean isIndexed() {
/* 58 */     return (this.myColorIndex != -1);
/*    */   }
/*    */   @NotNull
/*    */   public Color toAwtColor() {
/* 62 */     if (isIndexed()) {
/* 63 */       throw new IllegalArgumentException("Color is indexed color so a palette is needed");
/*    */     }
/*    */     
/* 66 */     if (((this.myColor != null) ? this.myColor : ((Supplier<Color>)Objects.requireNonNull(this.myColorSupplier)).get()) == null) $$$reportNull$$$0(1);  return (this.myColor != null) ? this.myColor : ((Supplier<Color>)Objects.requireNonNull(this.myColorSupplier)).get();
/*    */   }
/*    */   
/*    */   public int getColorIndex() {
/* 70 */     return this.myColorIndex;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 75 */     if (this == o) return true; 
/* 76 */     if (o == null || getClass() != o.getClass()) return false; 
/* 77 */     TerminalColor that = (TerminalColor)o;
/* 78 */     return (this.myColorIndex == that.myColorIndex && Objects.equals(this.myColor, that.myColor));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 83 */     return Objects.hash(new Object[] { Integer.valueOf(this.myColorIndex), this.myColor });
/*    */   }
/*    */   @Nullable
/*    */   public static TerminalColor awt(@Nullable Color color) {
/* 87 */     if (color == null) {
/* 88 */       return null;
/*    */     }
/* 90 */     return rgb(color.getRed(), color.getGreen(), color.getBlue());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\TerminalColor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */