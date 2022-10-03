/*    */ package com.jediterm.terminal.model;
/*    */ 
/*    */ import com.jediterm.terminal.TerminalColor;
/*    */ import com.jediterm.terminal.TextStyle;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public class StyleState {
/*  8 */   private TextStyle myCurrentStyle = TextStyle.EMPTY;
/*  9 */   private TextStyle myDefaultStyle = TextStyle.EMPTY;
/*    */   
/* 11 */   private TextStyle myMergedStyle = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TextStyle getCurrent() {
/* 17 */     return TextStyle.getCanonicalStyle(getMergedStyle());
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   private static TextStyle merge(@NotNull TextStyle style, @NotNull TextStyle defaultStyle) {
/* 22 */     if (style == null) $$$reportNull$$$0(0);  if (defaultStyle == null) $$$reportNull$$$0(1);  TextStyle.Builder builder = style.toBuilder();
/* 23 */     if (style.getBackground() == null && defaultStyle.getBackground() != null) {
/* 24 */       builder.setBackground(defaultStyle.getBackground());
/*    */     }
/* 26 */     if (style.getForeground() == null && defaultStyle.getForeground() != null) {
/* 27 */       builder.setForeground(defaultStyle.getForeground());
/*    */     }
/* 29 */     if (builder.build() == null) $$$reportNull$$$0(2);  return builder.build();
/*    */   }
/*    */   
/*    */   public void reset() {
/* 33 */     this.myCurrentStyle = this.myDefaultStyle;
/* 34 */     this.myMergedStyle = null;
/*    */   }
/*    */   
/*    */   public void set(StyleState styleState) {
/* 38 */     setCurrent(styleState.getCurrent());
/*    */   }
/*    */   
/*    */   public void setDefaultStyle(TextStyle defaultStyle) {
/* 42 */     this.myDefaultStyle = defaultStyle;
/* 43 */     this.myMergedStyle = null;
/*    */   }
/*    */   
/*    */   public TerminalColor getBackground() {
/* 47 */     return getBackground(null);
/*    */   }
/*    */   
/*    */   public TerminalColor getBackground(TerminalColor color) {
/* 51 */     return (color != null) ? color : this.myDefaultStyle.getBackground();
/*    */   }
/*    */   
/*    */   public TerminalColor getForeground() {
/* 55 */     return getForeground(null);
/*    */   }
/*    */   
/*    */   public TerminalColor getForeground(TerminalColor color) {
/* 59 */     return (color != null) ? color : this.myDefaultStyle.getForeground();
/*    */   }
/*    */   
/*    */   public void setCurrent(TextStyle current) {
/* 63 */     this.myCurrentStyle = current;
/* 64 */     this.myMergedStyle = null;
/*    */   }
/*    */   
/*    */   private TextStyle getMergedStyle() {
/* 68 */     if (this.myMergedStyle == null) {
/* 69 */       this.myMergedStyle = merge(this.myCurrentStyle, this.myDefaultStyle);
/*    */     }
/* 71 */     return this.myMergedStyle;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\model\StyleState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */