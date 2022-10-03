/*     */ package com.formdev.flatlaf.util;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Composite;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.Image;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Polygon;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Stroke;
/*     */ import java.awt.font.FontRenderContext;
/*     */ import java.awt.font.GlyphVector;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.BufferedImageOp;
/*     */ import java.awt.image.ImageObserver;
/*     */ import java.awt.image.RenderedImage;
/*     */ import java.awt.image.renderable.RenderableImage;
/*     */ import java.text.AttributedCharacterIterator;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Graphics2DProxy
/*     */   extends Graphics2D
/*     */ {
/*     */   private final Graphics2D delegate;
/*     */   
/*     */   public Graphics2DProxy(Graphics2D delegate) {
/*  56 */     this.delegate = delegate;
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics create() {
/*  61 */     return this.delegate.create();
/*     */   }
/*     */ 
/*     */   
/*     */   public Graphics create(int x, int y, int width, int height) {
/*  66 */     return this.delegate.create(x, y, width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   public Color getColor() {
/*  71 */     return this.delegate.getColor();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setColor(Color c) {
/*  76 */     this.delegate.setColor(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPaintMode() {
/*  81 */     this.delegate.setPaintMode();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setXORMode(Color c1) {
/*  86 */     this.delegate.setXORMode(c1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Font getFont() {
/*  91 */     return this.delegate.getFont();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFont(Font font) {
/*  96 */     this.delegate.setFont(font);
/*     */   }
/*     */ 
/*     */   
/*     */   public FontMetrics getFontMetrics() {
/* 101 */     return this.delegate.getFontMetrics();
/*     */   }
/*     */ 
/*     */   
/*     */   public FontMetrics getFontMetrics(Font f) {
/* 106 */     return this.delegate.getFontMetrics(f);
/*     */   }
/*     */ 
/*     */   
/*     */   public Rectangle getClipBounds() {
/* 111 */     return this.delegate.getClipBounds();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clipRect(int x, int y, int width, int height) {
/* 116 */     this.delegate.clipRect(x, y, width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setClip(int x, int y, int width, int height) {
/* 121 */     this.delegate.setClip(x, y, width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   public Shape getClip() {
/* 126 */     return this.delegate.getClip();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setClip(Shape clip) {
/* 131 */     this.delegate.setClip(clip);
/*     */   }
/*     */ 
/*     */   
/*     */   public void copyArea(int x, int y, int width, int height, int dx, int dy) {
/* 136 */     this.delegate.copyArea(x, y, width, height, dx, dy);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawLine(int x1, int y1, int x2, int y2) {
/* 141 */     this.delegate.drawLine(x1, y1, x2, y2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillRect(int x, int y, int width, int height) {
/* 146 */     this.delegate.fillRect(x, y, width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawRect(int x, int y, int width, int height) {
/* 151 */     this.delegate.drawRect(x, y, width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearRect(int x, int y, int width, int height) {
/* 156 */     this.delegate.clearRect(x, y, width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
/* 161 */     this.delegate.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
/* 166 */     this.delegate.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawOval(int x, int y, int width, int height) {
/* 171 */     this.delegate.drawOval(x, y, width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillOval(int x, int y, int width, int height) {
/* 176 */     this.delegate.fillOval(x, y, width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
/* 181 */     this.delegate.drawArc(x, y, width, height, startAngle, arcAngle);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
/* 186 */     this.delegate.fillArc(x, y, width, height, startAngle, arcAngle);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
/* 191 */     this.delegate.drawPolyline(xPoints, yPoints, nPoints);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
/* 196 */     this.delegate.drawPolygon(xPoints, yPoints, nPoints);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawPolygon(Polygon p) {
/* 201 */     this.delegate.drawPolygon(p);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
/* 206 */     this.delegate.fillPolygon(xPoints, yPoints, nPoints);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillPolygon(Polygon p) {
/* 211 */     this.delegate.fillPolygon(p);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawChars(char[] data, int offset, int length, int x, int y) {
/* 216 */     this.delegate.drawChars(data, offset, length, x, y);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawBytes(byte[] data, int offset, int length, int x, int y) {
/* 221 */     this.delegate.drawBytes(data, offset, length, x, y);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
/* 226 */     return this.delegate.drawImage(img, x, y, observer);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
/* 231 */     return this.delegate.drawImage(img, x, y, width, height, observer);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
/* 236 */     return this.delegate.drawImage(img, x, y, bgcolor, observer);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
/* 241 */     return this.delegate.drawImage(img, x, y, width, height, bgcolor, observer);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
/* 246 */     return this.delegate.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
/* 251 */     return this.delegate.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, bgcolor, observer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 256 */     this.delegate.dispose();
/*     */   }
/*     */ 
/*     */   
/*     */   public void finalize() {
/* 261 */     this.delegate.finalize();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 266 */     return this.delegate.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle getClipRect() {
/* 272 */     return this.delegate.getClipRect();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hitClip(int x, int y, int width, int height) {
/* 277 */     return this.delegate.hitClip(x, y, width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   public Rectangle getClipBounds(Rectangle r) {
/* 282 */     return this.delegate.getClipBounds(r);
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw3DRect(int x, int y, int width, int height, boolean raised) {
/* 287 */     this.delegate.draw3DRect(x, y, width, height, raised);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fill3DRect(int x, int y, int width, int height, boolean raised) {
/* 292 */     this.delegate.fill3DRect(x, y, width, height, raised);
/*     */   }
/*     */ 
/*     */   
/*     */   public void draw(Shape s) {
/* 297 */     this.delegate.draw(s);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
/* 302 */     return this.delegate.drawImage(img, xform, obs);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
/* 307 */     this.delegate.drawImage(img, op, x, y);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
/* 312 */     this.delegate.drawRenderedImage(img, xform);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
/* 317 */     this.delegate.drawRenderableImage(img, xform);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawString(String str, int x, int y) {
/* 322 */     this.delegate.drawString(str, x, y);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawString(String str, float x, float y) {
/* 327 */     this.delegate.drawString(str, x, y);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawString(AttributedCharacterIterator iterator, int x, int y) {
/* 332 */     this.delegate.drawString(iterator, x, y);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawString(AttributedCharacterIterator iterator, float x, float y) {
/* 337 */     this.delegate.drawString(iterator, x, y);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawGlyphVector(GlyphVector g, float x, float y) {
/* 342 */     this.delegate.drawGlyphVector(g, x, y);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fill(Shape s) {
/* 347 */     this.delegate.fill(s);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
/* 352 */     return this.delegate.hit(rect, s, onStroke);
/*     */   }
/*     */ 
/*     */   
/*     */   public GraphicsConfiguration getDeviceConfiguration() {
/* 357 */     return this.delegate.getDeviceConfiguration();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setComposite(Composite comp) {
/* 362 */     this.delegate.setComposite(comp);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPaint(Paint paint) {
/* 367 */     this.delegate.setPaint(paint);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStroke(Stroke s) {
/* 372 */     this.delegate.setStroke(s);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRenderingHint(RenderingHints.Key hintKey, Object hintValue) {
/* 377 */     this.delegate.setRenderingHint(hintKey, hintValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getRenderingHint(RenderingHints.Key hintKey) {
/* 382 */     return this.delegate.getRenderingHint(hintKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRenderingHints(Map<?, ?> hints) {
/* 387 */     this.delegate.setRenderingHints(hints);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addRenderingHints(Map<?, ?> hints) {
/* 392 */     this.delegate.addRenderingHints(hints);
/*     */   }
/*     */ 
/*     */   
/*     */   public RenderingHints getRenderingHints() {
/* 397 */     return this.delegate.getRenderingHints();
/*     */   }
/*     */ 
/*     */   
/*     */   public void translate(int x, int y) {
/* 402 */     this.delegate.translate(x, y);
/*     */   }
/*     */ 
/*     */   
/*     */   public void translate(double tx, double ty) {
/* 407 */     this.delegate.translate(tx, ty);
/*     */   }
/*     */ 
/*     */   
/*     */   public void rotate(double theta) {
/* 412 */     this.delegate.rotate(theta);
/*     */   }
/*     */ 
/*     */   
/*     */   public void rotate(double theta, double x, double y) {
/* 417 */     this.delegate.rotate(theta, x, y);
/*     */   }
/*     */ 
/*     */   
/*     */   public void scale(double sx, double sy) {
/* 422 */     this.delegate.scale(sx, sy);
/*     */   }
/*     */ 
/*     */   
/*     */   public void shear(double shx, double shy) {
/* 427 */     this.delegate.shear(shx, shy);
/*     */   }
/*     */ 
/*     */   
/*     */   public void transform(AffineTransform Tx) {
/* 432 */     this.delegate.transform(Tx);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTransform(AffineTransform Tx) {
/* 437 */     this.delegate.setTransform(Tx);
/*     */   }
/*     */ 
/*     */   
/*     */   public AffineTransform getTransform() {
/* 442 */     return this.delegate.getTransform();
/*     */   }
/*     */ 
/*     */   
/*     */   public Paint getPaint() {
/* 447 */     return this.delegate.getPaint();
/*     */   }
/*     */ 
/*     */   
/*     */   public Composite getComposite() {
/* 452 */     return this.delegate.getComposite();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBackground(Color color) {
/* 457 */     this.delegate.setBackground(color);
/*     */   }
/*     */ 
/*     */   
/*     */   public Color getBackground() {
/* 462 */     return this.delegate.getBackground();
/*     */   }
/*     */ 
/*     */   
/*     */   public Stroke getStroke() {
/* 467 */     return this.delegate.getStroke();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clip(Shape s) {
/* 472 */     this.delegate.clip(s);
/*     */   }
/*     */ 
/*     */   
/*     */   public FontRenderContext getFontRenderContext() {
/* 477 */     return this.delegate.getFontRenderContext();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\util\Graphics2DProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */