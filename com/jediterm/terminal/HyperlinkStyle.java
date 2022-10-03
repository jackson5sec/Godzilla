/*     */ package com.jediterm.terminal;
/*     */ 
/*     */ import com.jediterm.terminal.model.hyperlinks.LinkInfo;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HyperlinkStyle
/*     */   extends TextStyle
/*     */ {
/*     */   @NotNull
/*     */   private final LinkInfo myLinkInfo;
/*     */   @NotNull
/*     */   private final TextStyle myHighlightStyle;
/*     */   @Nullable
/*     */   private final TextStyle myPrevTextStyle;
/*     */   @NotNull
/*     */   private final HighlightMode myHighlightMode;
/*     */   
/*     */   public HyperlinkStyle(@NotNull TextStyle prevTextStyle, @NotNull LinkInfo hyperlinkInfo) {
/*  24 */     this(prevTextStyle.getForeground(), prevTextStyle.getBackground(), hyperlinkInfo, HighlightMode.HOVER, prevTextStyle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HyperlinkStyle(@Nullable TerminalColor foreground, @Nullable TerminalColor background, @NotNull LinkInfo hyperlinkInfo, @NotNull HighlightMode mode, @Nullable TextStyle prevTextStyle) {
/*  32 */     this(false, foreground, background, hyperlinkInfo, mode, prevTextStyle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private HyperlinkStyle(boolean keepColors, @Nullable TerminalColor foreground, @Nullable TerminalColor background, @NotNull LinkInfo hyperlinkInfo, @NotNull HighlightMode mode, @Nullable TextStyle prevTextStyle) {
/*  41 */     super(keepColors ? foreground : null, keepColors ? background : null);
/*  42 */     this
/*     */ 
/*     */ 
/*     */       
/*  46 */       .myHighlightStyle = (new TextStyle.Builder()).setBackground(background).setForeground(foreground).setOption(TextStyle.Option.UNDERLINED, true).build();
/*  47 */     this.myLinkInfo = hyperlinkInfo;
/*  48 */     this.myHighlightMode = mode;
/*  49 */     this.myPrevTextStyle = prevTextStyle;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public TextStyle getPrevTextStyle() {
/*  54 */     return this.myPrevTextStyle;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public TextStyle getHighlightStyle() {
/*  59 */     if (this.myHighlightStyle == null) $$$reportNull$$$0(6);  return this.myHighlightStyle;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public LinkInfo getLinkInfo() {
/*  64 */     if (this.myLinkInfo == null) $$$reportNull$$$0(7);  return this.myLinkInfo;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public HighlightMode getHighlightMode() {
/*  69 */     if (this.myHighlightMode == null) $$$reportNull$$$0(8);  return this.myHighlightMode;
/*     */   }
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public Builder toBuilder() {
/*  75 */     return new Builder(this);
/*     */   }
/*     */   
/*     */   public enum HighlightMode {
/*  79 */     ALWAYS, NEVER, HOVER;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */     extends TextStyle.Builder
/*     */   {
/*     */     @NotNull
/*     */     private LinkInfo myLinkInfo;
/*     */     
/*     */     @NotNull
/*     */     private TextStyle myHighlightStyle;
/*     */     @Nullable
/*     */     private TextStyle myPrevTextStyle;
/*     */     @NotNull
/*     */     private HyperlinkStyle.HighlightMode myHighlightMode;
/*     */     
/*     */     private Builder(@NotNull HyperlinkStyle style) {
/*  97 */       this.myLinkInfo = style.myLinkInfo;
/*  98 */       this.myHighlightStyle = style.myHighlightStyle;
/*  99 */       this.myPrevTextStyle = style.myPrevTextStyle;
/* 100 */       this.myHighlightMode = style.myHighlightMode;
/*     */     }
/*     */     
/*     */     @NotNull
/*     */     public HyperlinkStyle build() {
/* 105 */       if (build(false) == null) $$$reportNull$$$0(1);  return build(false);
/*     */     }
/*     */     
/*     */     @NotNull
/*     */     public HyperlinkStyle build(boolean keepColors) {
/* 110 */       TerminalColor foreground = this.myHighlightStyle.getForeground();
/* 111 */       TerminalColor background = this.myHighlightStyle.getBackground();
/* 112 */       if (keepColors) {
/* 113 */         TextStyle style = super.build();
/* 114 */         foreground = (style.getForeground() != null) ? style.getForeground() : this.myHighlightStyle.getForeground();
/* 115 */         background = (style.getBackground() != null) ? style.getBackground() : this.myHighlightStyle.getBackground();
/*     */       } 
/* 117 */       return new HyperlinkStyle(keepColors, foreground, background, this.myLinkInfo, this.myHighlightMode, this.myPrevTextStyle);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\HyperlinkStyle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */