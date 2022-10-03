/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import java.awt.AlphaComposite;
/*     */ import java.awt.Color;
/*     */ import java.awt.Composite;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.SystemColor;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import javax.swing.plaf.TextUI;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.LayeredHighlighter;
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
/*     */ public class ChangeableHighlightPainter
/*     */   extends LayeredHighlighter.LayerPainter
/*     */   implements Serializable
/*     */ {
/*     */   private Paint paint;
/*     */   private boolean roundedEdges;
/*     */   private transient AlphaComposite alphaComposite;
/*     */   private float alpha;
/*     */   private static final int ARCWIDTH = 8;
/*     */   private static final int ARCHEIGHT = 8;
/*     */   
/*     */   public ChangeableHighlightPainter() {
/*  86 */     this(null);
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
/*     */   public ChangeableHighlightPainter(Paint paint) {
/*  99 */     this(paint, false);
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
/*     */   public ChangeableHighlightPainter(Paint paint, boolean rounded) {
/* 112 */     this(paint, rounded, 1.0F);
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
/*     */   public ChangeableHighlightPainter(Paint paint, boolean rounded, float alpha) {
/* 129 */     setPaint(paint);
/* 130 */     setRoundedEdges(rounded);
/* 131 */     setAlpha(alpha);
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
/*     */   public float getAlpha() {
/* 146 */     return this.alpha;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AlphaComposite getAlphaComposite() {
/* 157 */     if (this.alphaComposite == null) {
/* 158 */       this.alphaComposite = AlphaComposite.getInstance(3, this.alpha);
/*     */     }
/*     */     
/* 161 */     return this.alphaComposite;
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
/*     */   public Paint getPaint() {
/* 173 */     return this.paint;
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
/*     */   public boolean getRoundedEdges() {
/* 185 */     return this.roundedEdges;
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
/*     */   public void paint(Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c) {
/* 202 */     Rectangle alloc = bounds.getBounds();
/*     */ 
/*     */     
/* 205 */     Graphics2D g2d = (Graphics2D)g;
/* 206 */     Composite originalComposite = null;
/* 207 */     if (getAlpha() < 1.0F) {
/* 208 */       originalComposite = g2d.getComposite();
/* 209 */       g2d.setComposite(getAlphaComposite());
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 215 */       TextUI mapper = c.getUI();
/* 216 */       Rectangle p0 = mapper.modelToView(c, offs0);
/* 217 */       Rectangle p1 = mapper.modelToView(c, offs1);
/* 218 */       Paint paint = getPaint();
/* 219 */       if (paint == null) {
/* 220 */         g2d.setColor(c.getSelectionColor());
/*     */       } else {
/*     */         
/* 223 */         g2d.setPaint(paint);
/*     */       } 
/*     */ 
/*     */       
/* 227 */       if (p0.y == p1.y)
/*     */       {
/*     */ 
/*     */         
/* 231 */         p1.width = 0;
/* 232 */         Rectangle r = p0.union(p1);
/* 233 */         g2d.fillRect(r.x, r.y, r.width, r.height);
/*     */       
/*     */       }
/*     */       else
/*     */       {
/* 238 */         int p0ToMarginWidth = alloc.x + alloc.width - p0.x;
/* 239 */         g2d.fillRect(p0.x, p0.y, p0ToMarginWidth, p0.height);
/* 240 */         if (p0.y + p0.height != p1.y) {
/* 241 */           g2d.fillRect(alloc.x, p0.y + p0.height, alloc.width, p1.y - p0.y + p0.height);
/*     */         }
/*     */         
/* 244 */         g2d.fillRect(alloc.x, p1.y, p1.x - alloc.x, p1.height);
/*     */       }
/*     */     
/* 247 */     } catch (BadLocationException e) {
/*     */       
/* 249 */       e.printStackTrace();
/*     */     } finally {
/*     */       
/* 252 */       if (getAlpha() < 1.0F) {
/* 253 */         g2d.setComposite(originalComposite);
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
/* 277 */     Graphics2D g2d = (Graphics2D)g;
/* 278 */     Composite originalComposite = null;
/* 279 */     if (getAlpha() < 1.0F) {
/* 280 */       originalComposite = g2d.getComposite();
/* 281 */       g2d.setComposite(getAlphaComposite());
/*     */     } 
/*     */ 
/*     */     
/* 285 */     Paint paint = getPaint();
/* 286 */     if (paint == null) {
/* 287 */       g2d.setColor(c.getSelectionColor());
/*     */     } else {
/*     */       
/* 290 */       g2d.setPaint(paint);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 298 */     if (offs0 == offs1) {
/*     */       try {
/* 300 */         Shape s = view.modelToView(offs0, bounds, Position.Bias.Forward);
/*     */         
/* 302 */         Rectangle r = s.getBounds();
/* 303 */         g.drawLine(r.x, r.y, r.x, r.y + r.height);
/* 304 */         return r;
/* 305 */       } catch (BadLocationException ble) {
/* 306 */         ble.printStackTrace();
/* 307 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 312 */     if (offs0 == view.getStartOffset() && offs1 == view.getEndOffset()) {
/*     */       Rectangle alloc;
/*     */       
/* 315 */       if (bounds instanceof Rectangle) {
/* 316 */         alloc = (Rectangle)bounds;
/*     */       } else {
/*     */         
/* 319 */         alloc = bounds.getBounds();
/*     */       } 
/*     */       
/* 322 */       g2d.fillRect(alloc.x, alloc.y, alloc.width, alloc.height);
/*     */ 
/*     */       
/* 325 */       if (getAlpha() < 1.0F) {
/* 326 */         g2d.setComposite(originalComposite);
/*     */       }
/*     */       
/* 329 */       return alloc;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 338 */       Shape shape = view.modelToView(offs0, Position.Bias.Forward, offs1, Position.Bias.Backward, bounds);
/*     */ 
/*     */ 
/*     */       
/* 342 */       Rectangle r = (shape instanceof Rectangle) ? (Rectangle)shape : shape.getBounds();
/* 343 */       if (this.roundedEdges) {
/* 344 */         g2d.fillRoundRect(r.x, r.y, r.width, r.height, 8, 8);
/*     */       }
/*     */       else {
/*     */         
/* 348 */         g2d.fillRect(r.x, r.y, r.width, r.height);
/*     */       } 
/*     */ 
/*     */       
/* 352 */       if (getAlpha() < 1.0F) {
/* 353 */         g2d.setComposite(originalComposite);
/*     */       }
/*     */       
/* 356 */       return r;
/*     */     }
/* 358 */     catch (BadLocationException ble) {
/* 359 */       ble.printStackTrace();
/*     */     } finally {
/*     */       
/* 362 */       if (getAlpha() < 1.0F) {
/* 363 */         g2d.setComposite(originalComposite);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 370 */     return null;
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
/*     */   private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
/* 384 */     s.defaultReadObject();
/*     */ 
/*     */     
/* 387 */     int rgb = s.readInt();
/* 388 */     this.paint = (rgb == -1) ? null : new Color(rgb);
/* 389 */     this.alphaComposite = null;
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
/*     */   public void setAlpha(float alpha) {
/* 406 */     this.alpha = alpha;
/* 407 */     this.alpha = Math.max(alpha, 0.0F);
/* 408 */     this.alpha = Math.min(1.0F, alpha);
/* 409 */     this.alphaComposite = null;
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
/*     */   public void setPaint(Paint paint) {
/* 421 */     this.paint = paint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRoundedEdges(boolean rounded) {
/* 432 */     this.roundedEdges = rounded;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 443 */     s.defaultWriteObject();
/* 444 */     int rgb = -1;
/* 445 */     if (this.paint != null) {
/*     */ 
/*     */       
/* 448 */       Color c = (this.paint instanceof Color) ? (Color)this.paint : SystemColor.textHighlight;
/*     */       
/* 450 */       rgb = c.getRGB();
/*     */     } 
/* 452 */     s.writeInt(rgb);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\ChangeableHighlightPainter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */