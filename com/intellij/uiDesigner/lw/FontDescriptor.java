/*    */ package com.intellij.uiDesigner.lw;
/*    */ 
/*    */ import java.awt.Font;
/*    */ import javax.swing.UIManager;
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
/*    */ public class FontDescriptor
/*    */ {
/*    */   private String myFontName;
/*    */   private int myFontSize;
/*    */   private int myFontStyle;
/*    */   private String mySwingFont;
/*    */   
/*    */   private FontDescriptor() {}
/*    */   
/*    */   public FontDescriptor(String fontName, int fontStyle, int fontSize) {
/* 34 */     this.myFontName = fontName;
/* 35 */     this.myFontSize = fontSize;
/* 36 */     this.myFontStyle = fontStyle;
/*    */   }
/*    */   
/*    */   public boolean isFixedFont() {
/* 40 */     return (this.mySwingFont == null);
/*    */   }
/*    */   
/*    */   public boolean isFullyDefinedFont() {
/* 44 */     return (this.myFontName != null && this.myFontSize >= 0 && this.myFontStyle >= 0);
/*    */   }
/*    */   
/*    */   public static FontDescriptor fromSwingFont(String swingFont) {
/* 48 */     FontDescriptor result = new FontDescriptor();
/* 49 */     result.mySwingFont = swingFont;
/* 50 */     return result;
/*    */   }
/*    */   
/*    */   public String getFontName() {
/* 54 */     return this.myFontName;
/*    */   }
/*    */   
/*    */   public int getFontSize() {
/* 58 */     return this.myFontSize;
/*    */   }
/*    */   
/*    */   public int getFontStyle() {
/* 62 */     return this.myFontStyle;
/*    */   }
/*    */   
/*    */   public Font getFont(Font defaultFont) {
/* 66 */     return new Font((this.myFontName != null) ? this.myFontName : defaultFont.getFontName(), (this.myFontStyle >= 0) ? this.myFontStyle : defaultFont.getStyle(), (this.myFontSize >= 0) ? this.myFontSize : defaultFont.getSize());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSwingFont() {
/* 72 */     return this.mySwingFont;
/*    */   }
/*    */   
/*    */   public Font getResolvedFont(Font defaultFont) {
/* 76 */     if (this.mySwingFont != null) {
/* 77 */       return UIManager.getFont(this.mySwingFont);
/*    */     }
/* 79 */     return getFont(defaultFont);
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj) {
/* 83 */     if (obj == null || !(obj instanceof FontDescriptor)) {
/* 84 */       return false;
/*    */     }
/* 86 */     FontDescriptor rhs = (FontDescriptor)obj;
/* 87 */     if (this.mySwingFont != null) {
/* 88 */       return this.mySwingFont.equals(rhs.mySwingFont);
/*    */     }
/*    */     
/* 91 */     if (this.myFontName == null && rhs.myFontName != null) return false; 
/* 92 */     if (this.myFontName != null && rhs.myFontName == null) return false; 
/* 93 */     if (this.myFontName != null && !this.myFontName.equals(rhs.myFontName)) return false; 
/* 94 */     return (this.myFontSize == rhs.myFontSize && this.myFontStyle == rhs.myFontStyle);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\intelli\\uiDesigner\lw\FontDescriptor.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       1.1.3
 */