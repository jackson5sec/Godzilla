/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import javax.swing.text.BadLocationException;
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
/*     */ public class SmartHighlightPainter
/*     */   extends ChangeableHighlightPainter
/*     */ {
/*     */   private Color borderColor;
/*     */   private boolean paintBorder;
/*     */   
/*     */   public SmartHighlightPainter() {
/*  50 */     super(Color.BLUE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SmartHighlightPainter(Paint paint) {
/*  60 */     super(paint);
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
/*     */   public boolean getPaintBorder() {
/*  72 */     return this.paintBorder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Shape paintLayer(Graphics g, int p0, int p1, Shape viewBounds, JTextComponent c, View view) {
/*  80 */     g.setColor((Color)getPaint());
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
/* 108 */       g.fillRect(alloc.x, alloc.y, alloc.width, alloc.height);
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
/* 119 */       g.fillRect(r.x, r.y, r.width, r.height);
/* 120 */       if (this.paintBorder) {
/* 121 */         g.setColor(this.borderColor);
/* 122 */         g.drawRect(r.x, r.y, r.width - 1, r.height - 1);
/*     */       } 
/* 124 */       return r;
/* 125 */     } catch (BadLocationException e) {
/* 126 */       e.printStackTrace();
/* 127 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPaint(Paint paint) {
/* 135 */     super.setPaint(paint);
/* 136 */     if (paint instanceof Color) {
/* 137 */       this.borderColor = ((Color)paint).darker();
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
/*     */   public void setPaintBorder(boolean paint) {
/* 150 */     this.paintBorder = paint;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\SmartHighlightPainter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */