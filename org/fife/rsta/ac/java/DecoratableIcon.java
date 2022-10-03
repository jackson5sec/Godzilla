/*     */ package org.fife.rsta.ac.java;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.Icon;
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
/*     */ public class DecoratableIcon
/*     */   implements Icon
/*     */ {
/*     */   private int width;
/*     */   private Icon mainIcon;
/*     */   private List<Icon> decorations;
/*     */   private boolean deprecated;
/*     */   private static final int DEFAULT_WIDTH = 24;
/*     */   
/*     */   public DecoratableIcon(Icon mainIcon) {
/*  61 */     this(24, mainIcon);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DecoratableIcon(int width, Icon mainIcon) {
/*  72 */     setMainIcon(mainIcon);
/*  73 */     this.width = width;
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
/*     */   public void addDecorationIcon(Icon decoration) {
/*  85 */     if (decoration == null) {
/*  86 */       throw new IllegalArgumentException("decoration cannot be null");
/*     */     }
/*  88 */     if (this.decorations == null) {
/*  89 */       this.decorations = new ArrayList<>(1);
/*     */     }
/*  91 */     this.decorations.add(decoration);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIconHeight() {
/* 100 */     return this.mainIcon.getIconHeight();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIconWidth() {
/* 109 */     return this.width;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void paintIcon(Component c, Graphics g, int x, int y) {
/* 118 */     if (this.deprecated) {
/* 119 */       IconFactory.get().getIcon("deprecatedIcon")
/* 120 */         .paintIcon(c, g, x, y);
/*     */     }
/* 122 */     this.mainIcon.paintIcon(c, g, x, y);
/* 123 */     if (this.decorations != null) {
/* 124 */       x = x + getIconWidth() - 8;
/* 125 */       for (int i = this.decorations.size() - 1; i >= 0; i--) {
/* 126 */         Icon icon = this.decorations.get(i);
/* 127 */         icon.paintIcon(c, g, x, y);
/* 128 */         x -= 8;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDeprecated(boolean deprecated) {
/* 140 */     this.deprecated = deprecated;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMainIcon(Icon icon) {
/* 151 */     if (icon == null) {
/* 152 */       throw new IllegalArgumentException("icon cannot be null");
/*     */     }
/* 154 */     this.mainIcon = icon;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\DecoratableIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */