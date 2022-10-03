/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import javax.swing.JPanel;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Style
/*     */   implements Cloneable
/*     */ {
/*  37 */   public static final Color DEFAULT_FOREGROUND = Color.BLACK;
/*  38 */   public static final Color DEFAULT_BACKGROUND = null;
/*  39 */   public static final Font DEFAULT_FONT = null;
/*     */ 
/*     */   
/*     */   public Color foreground;
/*     */   
/*     */   public Color background;
/*     */   
/*     */   public boolean underline;
/*     */   
/*     */   public Font font;
/*     */   
/*     */   public FontMetrics fontMetrics;
/*     */ 
/*     */   
/*     */   public Style() {
/*  54 */     this(DEFAULT_FOREGROUND);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Style(Color fg) {
/*  64 */     this(fg, DEFAULT_BACKGROUND);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Style(Color fg, Color bg) {
/*  75 */     this(fg, bg, DEFAULT_FONT);
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
/*     */   public Style(Color fg, Color bg, Font font) {
/*  87 */     this(fg, bg, font, false);
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
/*     */   public Style(Color fg, Color bg, Font font, boolean underline) {
/* 100 */     this.foreground = fg;
/* 101 */     this.background = bg;
/* 102 */     this.font = font;
/* 103 */     this.underline = underline;
/* 104 */     this
/* 105 */       .fontMetrics = (font == null) ? null : (new JPanel()).getFontMetrics(font);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean areEqual(Object o1, Object o2) {
/* 114 */     return ((o1 == null && o2 == null) || (o1 != null && o1.equals(o2)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/*     */     Style clone;
/*     */     try {
/* 127 */       clone = (Style)super.clone();
/* 128 */     } catch (CloneNotSupportedException cnse) {
/* 129 */       cnse.printStackTrace();
/* 130 */       return null;
/*     */     } 
/* 132 */     clone.foreground = this.foreground;
/* 133 */     clone.background = this.background;
/* 134 */     clone.font = this.font;
/* 135 */     clone.underline = this.underline;
/* 136 */     clone.fontMetrics = this.fontMetrics;
/* 137 */     return clone;
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
/*     */   public boolean equals(Object o2) {
/* 150 */     if (o2 instanceof Style) {
/* 151 */       Style ss2 = (Style)o2;
/* 152 */       if (this.underline == ss2.underline && 
/* 153 */         areEqual(this.foreground, ss2.foreground) && 
/* 154 */         areEqual(this.background, ss2.background) && 
/* 155 */         areEqual(this.font, ss2.font) && 
/* 156 */         areEqual(this.fontMetrics, ss2.fontMetrics)) {
/* 157 */         return true;
/*     */       }
/*     */     } 
/* 160 */     return false;
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
/*     */   public int hashCode() {
/* 175 */     int hashCode = this.underline ? 1 : 0;
/* 176 */     if (this.foreground != null) {
/* 177 */       hashCode ^= this.foreground.hashCode();
/*     */     }
/* 179 */     if (this.background != null) {
/* 180 */       hashCode ^= this.background.hashCode();
/*     */     }
/* 182 */     return hashCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 193 */     return "[Style: foreground: " + this.foreground + ", background: " + this.background + ", underline: " + this.underline + ", font: " + this.font + "]";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\Style.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */