/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
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
/*     */ public class ColorBackgroundPainterStrategy
/*     */   implements BackgroundPainterStrategy
/*     */ {
/*     */   private Color color;
/*     */   
/*     */   public ColorBackgroundPainterStrategy(Color color) {
/*  38 */     setColor(color);
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
/*     */   public boolean equals(Object o2) {
/*  53 */     return (o2 instanceof ColorBackgroundPainterStrategy && this.color
/*  54 */       .equals(((ColorBackgroundPainterStrategy)o2)
/*  55 */         .getColor()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getColor() {
/*  66 */     return this.color;
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
/*     */   public int hashCode() {
/*  79 */     return this.color.hashCode();
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
/*     */   public void paint(Graphics g, Rectangle bounds) {
/*  92 */     Color temp = g.getColor();
/*  93 */     g.setColor(this.color);
/*  94 */     g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
/*  95 */     g.setColor(temp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setColor(Color color) {
/* 106 */     this.color = color;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\ColorBackgroundPainterStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */