/*     */ package org.fife.rsta.ac.java;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.RenderingHints;
/*     */ import java.util.Map;
/*     */ import javax.swing.DefaultListCellRenderer;
/*     */ import javax.swing.JList;
/*     */ import org.fife.ui.autocomplete.Completion;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
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
/*     */ public class JavaCellRenderer
/*     */   extends DefaultListCellRenderer
/*     */ {
/*     */   private JList<?> list;
/*     */   private boolean selected;
/*     */   private boolean evenRow;
/*     */   private JavaSourceCompletion jsc;
/*     */   private static Color altBG;
/*     */   private Completion nonJavaCompletion;
/*     */   private boolean simpleText;
/*     */   
/*     */   public static Color getAlternateBackground() {
/*  72 */     return altBG;
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
/*     */   public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean selected, boolean hasFocus) {
/*  89 */     super.getListCellRendererComponent(list, value, index, selected, hasFocus);
/*     */     
/*  91 */     setText("Foobar");
/*  92 */     this.list = list;
/*  93 */     this.selected = selected;
/*     */     
/*  95 */     if (value instanceof JavaSourceCompletion) {
/*  96 */       this.jsc = (JavaSourceCompletion)value;
/*  97 */       this.nonJavaCompletion = null;
/*  98 */       setIcon(this.jsc.getIcon());
/*     */     } else {
/*     */       
/* 101 */       this.jsc = null;
/* 102 */       this.nonJavaCompletion = (Completion)value;
/* 103 */       setIcon(this.nonJavaCompletion.getIcon());
/*     */     } 
/*     */     
/* 106 */     this.evenRow = ((index & 0x1) == 0);
/* 107 */     if (altBG != null && this.evenRow && !selected) {
/* 108 */       setBackground(altBG);
/*     */     }
/*     */     
/* 111 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintComponent(Graphics g) {
/*     */     Object old;
/* 120 */     Graphics2D g2d = (Graphics2D)g;
/*     */ 
/*     */ 
/*     */     
/* 124 */     Map<?, ?> hints = RSyntaxUtilities.getDesktopAntiAliasHints();
/* 125 */     if (hints != null) {
/* 126 */       old = g2d.getRenderingHints();
/* 127 */       g2d.addRenderingHints(hints);
/*     */     }
/*     */     else {
/*     */       
/* 131 */       old = g2d.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
/* 132 */       g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 141 */     int iconW = 18;
/* 142 */     int h = getHeight();
/* 143 */     if (!this.selected) {
/* 144 */       g.setColor(getBackground());
/* 145 */       g.fillRect(0, 0, getWidth(), h);
/*     */     } else {
/*     */       
/* 148 */       g.setColor((altBG != null && this.evenRow) ? altBG : this.list.getBackground());
/* 149 */       g.fillRect(0, 0, 18, h);
/* 150 */       g.setColor(getBackground());
/* 151 */       g.fillRect(18, 0, getWidth() - 18, h);
/*     */     } 
/* 153 */     if (getIcon() != null) {
/* 154 */       int y = (h - getIcon().getIconHeight()) / 2;
/* 155 */       getIcon().paintIcon(this, g, 0, y);
/*     */     } 
/*     */     
/* 158 */     int x = getX() + 18 + 2;
/* 159 */     g.setColor(this.selected ? this.list.getSelectionForeground() : this.list
/* 160 */         .getForeground());
/* 161 */     if (this.jsc != null && !this.simpleText) {
/* 162 */       this.jsc.rendererText(g, x, g.getFontMetrics().getHeight(), this.selected);
/*     */     } else {
/*     */       
/* 165 */       Completion c = (this.jsc != null) ? this.jsc : this.nonJavaCompletion;
/* 166 */       if (c != null) {
/* 167 */         g.drawString(c.toString(), x, g.getFontMetrics().getHeight());
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 172 */     if (hints != null) {
/* 173 */       if (old instanceof Map) {
/* 174 */         g2d.addRenderingHints((Map<?, ?>)old);
/*     */       } else {
/*     */         
/* 177 */         g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, old);
/*     */       } 
/*     */     } else {
/*     */       
/* 181 */       g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, old);
/*     */     } 
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
/*     */   public static void setAlternateBackground(Color altBG) {
/* 196 */     JavaCellRenderer.altBG = altBG;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSimpleText(boolean simple) {
/* 207 */     this.simpleText = simple;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\JavaCellRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */