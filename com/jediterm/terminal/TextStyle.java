/*     */ package com.jediterm.terminal;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Objects;
/*     */ import java.util.WeakHashMap;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ public class TextStyle
/*     */ {
/*  12 */   private static final EnumSet<Option> NO_OPTIONS = EnumSet.noneOf(Option.class);
/*     */   
/*  14 */   public static final TextStyle EMPTY = new TextStyle();
/*     */   
/*  16 */   private static final WeakHashMap<TextStyle, WeakReference<TextStyle>> styles = new WeakHashMap<>();
/*     */   
/*     */   private final TerminalColor myForeground;
/*     */   private final TerminalColor myBackground;
/*     */   private final EnumSet<Option> myOptions;
/*     */   
/*     */   public TextStyle() {
/*  23 */     this(null, null, NO_OPTIONS);
/*     */   }
/*     */   
/*     */   public TextStyle(@Nullable TerminalColor foreground, @Nullable TerminalColor background) {
/*  27 */     this(foreground, background, NO_OPTIONS);
/*     */   }
/*     */   
/*     */   public TextStyle(@Nullable TerminalColor foreground, @Nullable TerminalColor background, @NotNull EnumSet<Option> options) {
/*  31 */     this.myForeground = foreground;
/*  32 */     this.myBackground = background;
/*  33 */     this.myOptions = options.clone();
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public static TextStyle getCanonicalStyle(TextStyle currentStyle) {
/*  38 */     if (currentStyle instanceof HyperlinkStyle) {
/*  39 */       if (currentStyle == null) $$$reportNull$$$0(1);  return currentStyle;
/*     */     } 
/*  41 */     WeakReference<TextStyle> canonRef = styles.get(currentStyle);
/*  42 */     if (canonRef != null) {
/*  43 */       TextStyle canonStyle = canonRef.get();
/*  44 */       if (canonStyle != null) {
/*  45 */         if (canonStyle == null) $$$reportNull$$$0(2);  return canonStyle;
/*     */       } 
/*     */     } 
/*  48 */     styles.put(currentStyle, new WeakReference<>(currentStyle));
/*  49 */     if (currentStyle == null) $$$reportNull$$$0(3);  return currentStyle;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public TerminalColor getForeground() {
/*  54 */     return this.myForeground;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public TerminalColor getBackground() {
/*  59 */     return this.myBackground;
/*     */   }
/*     */   
/*     */   public TextStyle createEmptyWithColors() {
/*  63 */     return new TextStyle(this.myForeground, this.myBackground);
/*     */   }
/*     */   
/*     */   public int getId() {
/*  67 */     return hashCode();
/*     */   }
/*     */   
/*     */   public boolean hasOption(Option option) {
/*  71 */     return this.myOptions.contains(option);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  76 */     if (this == o) return true; 
/*  77 */     if (o == null || getClass() != o.getClass()) return false; 
/*  78 */     TextStyle textStyle = (TextStyle)o;
/*  79 */     return (Objects.equals(this.myForeground, textStyle.myForeground) && 
/*  80 */       Objects.equals(this.myBackground, textStyle.myBackground) && this.myOptions
/*  81 */       .equals(textStyle.myOptions));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  86 */     return Objects.hash(new Object[] { this.myForeground, this.myBackground, this.myOptions });
/*     */   }
/*     */   
/*     */   public TerminalColor getBackgroundForRun() {
/*  90 */     return this.myOptions.contains(Option.INVERSE) ? this.myForeground : this.myBackground;
/*     */   }
/*     */   
/*     */   public TerminalColor getForegroundForRun() {
/*  94 */     return this.myOptions.contains(Option.INVERSE) ? this.myBackground : this.myForeground;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public Builder toBuilder() {
/*  99 */     return new Builder(this);
/*     */   }
/*     */   
/*     */   public enum Option {
/* 103 */     BOLD,
/* 104 */     ITALIC,
/* 105 */     BLINK,
/* 106 */     DIM,
/* 107 */     INVERSE,
/* 108 */     UNDERLINED,
/* 109 */     HIDDEN;
/*     */     
/*     */     private void set(@NotNull EnumSet<Option> options, boolean val) {
/* 112 */       if (options == null) $$$reportNull$$$0(0);  if (val) {
/* 113 */         options.add(this);
/*     */       } else {
/*     */         
/* 116 */         options.remove(this);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private TerminalColor myForeground;
/*     */     private TerminalColor myBackground;
/*     */     private EnumSet<TextStyle.Option> myOptions;
/*     */     
/*     */     public Builder(@NotNull TextStyle textStyle) {
/* 127 */       this.myForeground = textStyle.myForeground;
/* 128 */       this.myBackground = textStyle.myBackground;
/* 129 */       this.myOptions = textStyle.myOptions.clone();
/*     */     }
/*     */     
/*     */     public Builder() {
/* 133 */       this.myForeground = null;
/* 134 */       this.myBackground = null;
/* 135 */       this.myOptions = EnumSet.noneOf(TextStyle.Option.class);
/*     */     }
/*     */     
/*     */     @NotNull
/*     */     public Builder setForeground(@Nullable TerminalColor foreground) {
/* 140 */       this.myForeground = foreground;
/* 141 */       if (this == null) $$$reportNull$$$0(1);  return this;
/*     */     }
/*     */     
/*     */     @NotNull
/*     */     public Builder setBackground(@Nullable TerminalColor background) {
/* 146 */       this.myBackground = background;
/* 147 */       if (this == null) $$$reportNull$$$0(2);  return this;
/*     */     }
/*     */     
/*     */     @NotNull
/*     */     public Builder setOption(@NotNull TextStyle.Option option, boolean val) {
/* 152 */       if (option == null) $$$reportNull$$$0(3);  option.set(this.myOptions, val);
/* 153 */       if (this == null) $$$reportNull$$$0(4);  return this;
/*     */     }
/*     */     
/*     */     @NotNull
/*     */     public TextStyle build() {
/* 158 */       return new TextStyle(this.myForeground, this.myBackground, this.myOptions);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\TextStyle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */