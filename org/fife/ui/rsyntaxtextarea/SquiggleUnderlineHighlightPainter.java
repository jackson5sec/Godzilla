/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.Position;
/*     */ import javax.swing.text.View;
/*     */ import org.fife.ui.rtextarea.ChangeableHighlightPainter;
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
/*     */ public class SquiggleUnderlineHighlightPainter
/*     */   extends ChangeableHighlightPainter
/*     */ {
/*     */   private static final int AMT = 2;
/*     */   
/*     */   public SquiggleUnderlineHighlightPainter(Color color) {
/*  47 */     super(color);
/*  48 */     setPaint(color);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public Shape paintLayer(Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c, View view) {
/*  68 */     g.setColor((Color)getPaint());
/*     */     
/*  70 */     if (offs0 == view.getStartOffset() && offs1 == view.getEndOffset()) {
/*     */       Rectangle alloc;
/*     */       
/*  73 */       if (bounds instanceof Rectangle) {
/*  74 */         alloc = (Rectangle)bounds;
/*     */       } else {
/*     */         
/*  77 */         alloc = bounds.getBounds();
/*     */       } 
/*  79 */       paintSquiggle(g, alloc);
/*  80 */       return alloc;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  86 */       Shape shape = view.modelToView(offs0, Position.Bias.Forward, offs1, Position.Bias.Backward, bounds);
/*     */ 
/*     */ 
/*     */       
/*  90 */       Rectangle r = (shape instanceof Rectangle) ? (Rectangle)shape : shape.getBounds();
/*  91 */       paintSquiggle(g, r);
/*  92 */       return r;
/*  93 */     } catch (BadLocationException e) {
/*  94 */       e.printStackTrace();
/*     */ 
/*     */ 
/*     */       
/*  98 */       return null;
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
/*     */   protected void paintSquiggle(Graphics g, Rectangle r) {
/* 110 */     int x = r.x;
/* 111 */     int y = r.y + r.height - 2;
/* 112 */     int delta = -2;
/* 113 */     while (x < r.x + r.width) {
/* 114 */       g.drawLine(x, y, x + 2, y + delta);
/* 115 */       y += delta;
/* 116 */       delta = -delta;
/* 117 */       x += 2;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\SquiggleUnderlineHighlightPainter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */