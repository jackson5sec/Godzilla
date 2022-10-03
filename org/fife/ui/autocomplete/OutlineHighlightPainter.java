/*     */ package org.fife.ui.autocomplete;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.DefaultHighlighter;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.Position;
/*     */ import javax.swing.text.View;
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
/*     */ class OutlineHighlightPainter
/*     */   extends DefaultHighlighter.DefaultHighlightPainter
/*     */ {
/*     */   private Color color;
/*     */   
/*     */   OutlineHighlightPainter(Color color) {
/*  55 */     super(color);
/*  56 */     setColor(color);
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
/*     */   public Color getColor() {
/*  68 */     return this.color;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Shape paintLayer(Graphics g, int p0, int p1, Shape viewBounds, JTextComponent c, View view) {
/*  79 */     g.setColor(getColor());
/*  80 */     p0++;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  87 */     if (p0 == p1) {
/*     */       try {
/*  89 */         Shape s = view.modelToView(p0, viewBounds, Position.Bias.Forward);
/*     */         
/*  91 */         Rectangle r = s.getBounds();
/*  92 */         g.drawLine(r.x, r.y, r.x, r.y + r.height);
/*  93 */         return r;
/*  94 */       } catch (BadLocationException ble) {
/*  95 */         ble.printStackTrace();
/*  96 */         return null;
/*     */       } 
/*     */     }
/*     */     
/* 100 */     if (p0 == view.getStartOffset() && p1 == view.getEndOffset()) {
/*     */       Rectangle alloc;
/*     */       
/* 103 */       if (viewBounds instanceof Rectangle) {
/* 104 */         alloc = (Rectangle)viewBounds;
/*     */       } else {
/* 106 */         alloc = viewBounds.getBounds();
/*     */       } 
/* 108 */       g.drawRect(alloc.x, alloc.y, alloc.width - 1, alloc.height - 1);
/* 109 */       return alloc;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 115 */       Shape shape = view.modelToView(p0, Position.Bias.Forward, p1, Position.Bias.Backward, viewBounds);
/*     */ 
/*     */       
/* 118 */       Rectangle r = (shape instanceof Rectangle) ? (Rectangle)shape : shape.getBounds();
/* 119 */       g.drawRect(r.x, r.y, r.width - 1, r.height - 1);
/* 120 */       return r;
/* 121 */     } catch (BadLocationException e) {
/* 122 */       e.printStackTrace();
/* 123 */       return null;
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
/*     */   public void setColor(Color color) {
/* 136 */     if (color == null) {
/* 137 */       throw new IllegalArgumentException("color cannot be null");
/*     */     }
/* 139 */     this.color = color;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\OutlineHighlightPainter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */