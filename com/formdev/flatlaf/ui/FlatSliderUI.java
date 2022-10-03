/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.HiDPIUtils;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.geom.Ellipse2D;
/*     */ import java.awt.geom.Path2D;
/*     */ import java.awt.geom.RoundRectangle2D;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JSlider;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicSliderUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatSliderUI
/*     */   extends BasicSliderUI
/*     */ {
/*     */   protected int trackWidth;
/*     */   protected Dimension thumbSize;
/*     */   protected int focusWidth;
/*     */   protected Color trackValueColor;
/*     */   protected Color trackColor;
/*     */   protected Color thumbColor;
/*     */   protected Color thumbBorderColor;
/*     */   protected Color focusBaseColor;
/*     */   protected Color focusedColor;
/*     */   protected Color focusedThumbBorderColor;
/*     */   protected Color hoverThumbColor;
/*     */   protected Color pressedThumbColor;
/*     */   protected Color disabledTrackColor;
/*     */   protected Color disabledThumbColor;
/*     */   protected Color disabledThumbBorderColor;
/*     */   private Color defaultBackground;
/*     */   private Color defaultForeground;
/*     */   protected boolean thumbHover;
/*     */   protected boolean thumbPressed;
/*     */   private Object[] oldRenderingHints;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/* 103 */     return new FlatSliderUI();
/*     */   }
/*     */   
/*     */   public FlatSliderUI() {
/* 107 */     super(null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults(JSlider slider) {
/* 112 */     super.installDefaults(slider);
/*     */     
/* 114 */     LookAndFeel.installProperty(slider, "opaque", Boolean.valueOf(false));
/*     */     
/* 116 */     this.trackWidth = UIManager.getInt("Slider.trackWidth");
/* 117 */     this.thumbSize = UIManager.getDimension("Slider.thumbSize");
/* 118 */     if (this.thumbSize == null) {
/*     */       
/* 120 */       int thumbWidth = UIManager.getInt("Slider.thumbWidth");
/* 121 */       this.thumbSize = new Dimension(thumbWidth, thumbWidth);
/*     */     } 
/* 123 */     this.focusWidth = FlatUIUtils.getUIInt("Slider.focusWidth", 4);
/*     */     
/* 125 */     this.trackValueColor = FlatUIUtils.getUIColor("Slider.trackValueColor", "Slider.thumbColor");
/* 126 */     this.trackColor = UIManager.getColor("Slider.trackColor");
/* 127 */     this.thumbColor = UIManager.getColor("Slider.thumbColor");
/* 128 */     this.thumbBorderColor = UIManager.getColor("Slider.thumbBorderColor");
/* 129 */     this.focusBaseColor = UIManager.getColor("Component.focusColor");
/* 130 */     this.focusedColor = FlatUIUtils.getUIColor("Slider.focusedColor", this.focusBaseColor);
/* 131 */     this.focusedThumbBorderColor = FlatUIUtils.getUIColor("Slider.focusedThumbBorderColor", "Component.focusedBorderColor");
/* 132 */     this.hoverThumbColor = UIManager.getColor("Slider.hoverThumbColor");
/* 133 */     this.pressedThumbColor = UIManager.getColor("Slider.pressedThumbColor");
/* 134 */     this.disabledTrackColor = UIManager.getColor("Slider.disabledTrackColor");
/* 135 */     this.disabledThumbColor = UIManager.getColor("Slider.disabledThumbColor");
/* 136 */     this.disabledThumbBorderColor = FlatUIUtils.getUIColor("Slider.disabledThumbBorderColor", "Component.disabledBorderColor");
/*     */     
/* 138 */     this.defaultBackground = UIManager.getColor("Slider.background");
/* 139 */     this.defaultForeground = UIManager.getColor("Slider.foreground");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallDefaults(JSlider slider) {
/* 144 */     super.uninstallDefaults(slider);
/*     */     
/* 146 */     this.trackValueColor = null;
/* 147 */     this.trackColor = null;
/* 148 */     this.thumbColor = null;
/* 149 */     this.thumbBorderColor = null;
/* 150 */     this.focusBaseColor = null;
/* 151 */     this.focusedColor = null;
/* 152 */     this.focusedThumbBorderColor = null;
/* 153 */     this.hoverThumbColor = null;
/* 154 */     this.pressedThumbColor = null;
/* 155 */     this.disabledTrackColor = null;
/* 156 */     this.disabledThumbColor = null;
/* 157 */     this.disabledThumbBorderColor = null;
/*     */     
/* 159 */     this.defaultBackground = null;
/* 160 */     this.defaultForeground = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BasicSliderUI.TrackListener createTrackListener(JSlider slider) {
/* 165 */     return new FlatTrackListener();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBaseline(JComponent c, int width, int height) {
/* 170 */     if (c == null)
/* 171 */       throw new NullPointerException(); 
/* 172 */     if (width < 0 || height < 0) {
/* 173 */       throw new IllegalArgumentException();
/*     */     }
/*     */     
/* 176 */     if (this.slider.getOrientation() == 1) {
/* 177 */       return -1;
/*     */     }
/*     */     
/* 180 */     FontMetrics fm = this.slider.getFontMetrics(this.slider.getFont());
/* 181 */     return this.trackRect.y + Math.round((this.trackRect.height - fm.getHeight()) / 2.0F) + fm.getAscent() - 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getPreferredHorizontalSize() {
/* 186 */     return UIScale.scale(super.getPreferredHorizontalSize());
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getPreferredVerticalSize() {
/* 191 */     return UIScale.scale(super.getPreferredVerticalSize());
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getMinimumHorizontalSize() {
/* 196 */     return UIScale.scale(super.getMinimumHorizontalSize());
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getMinimumVerticalSize() {
/* 201 */     return UIScale.scale(super.getMinimumVerticalSize());
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getTickLength() {
/* 206 */     return UIScale.scale(super.getTickLength());
/*     */   }
/*     */ 
/*     */   
/*     */   protected Dimension getThumbSize() {
/* 211 */     return calcThumbSize(this.slider, this.thumbSize, this.focusWidth);
/*     */   }
/*     */   
/*     */   public static Dimension calcThumbSize(JSlider slider, Dimension thumbSize, int focusWidth) {
/* 215 */     int fw = UIScale.scale(focusWidth);
/* 216 */     int w = UIScale.scale(thumbSize.width) + fw + fw;
/* 217 */     int h = UIScale.scale(thumbSize.height) + fw + fw;
/* 218 */     return (slider.getOrientation() == 0) ? new Dimension(w, h) : new Dimension(h, w);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void paint(Graphics g, JComponent c) {
/* 225 */     this.oldRenderingHints = FlatUIUtils.setRenderingHints(g);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 244 */     super.paint(g, c);
/*     */     
/* 246 */     FlatUIUtils.resetRenderingHints(g, this.oldRenderingHints);
/* 247 */     this.oldRenderingHints = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void paintLabels(Graphics g) {
/* 252 */     FlatUIUtils.runWithoutRenderingHints(g, this.oldRenderingHints, () -> super.paintLabels(g));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void paintFocus(Graphics g) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void paintTrack(Graphics g) {
/*     */     RoundRectangle2D track;
/* 264 */     boolean enabled = this.slider.isEnabled();
/* 265 */     float tw = UIScale.scale(this.trackWidth);
/* 266 */     float arc = tw;
/*     */     
/* 268 */     RoundRectangle2D coloredTrack = null;
/*     */     
/* 270 */     if (this.slider.getOrientation() == 0)
/* 271 */     { float y = this.trackRect.y + (this.trackRect.height - tw) / 2.0F;
/* 272 */       if (enabled && isRoundThumb()) {
/* 273 */         if (this.slider.getComponentOrientation().isLeftToRight()) {
/* 274 */           int cw = this.thumbRect.x + this.thumbRect.width / 2 - this.trackRect.x;
/* 275 */           coloredTrack = new RoundRectangle2D.Float(this.trackRect.x, y, cw, tw, arc, arc);
/* 276 */           track = new RoundRectangle2D.Float((this.trackRect.x + cw), y, (this.trackRect.width - cw), tw, arc, arc);
/*     */         } else {
/* 278 */           int cw = this.trackRect.x + this.trackRect.width - this.thumbRect.x - this.thumbRect.width / 2;
/* 279 */           coloredTrack = new RoundRectangle2D.Float((this.trackRect.x + this.trackRect.width - cw), y, cw, tw, arc, arc);
/* 280 */           track = new RoundRectangle2D.Float(this.trackRect.x, y, (this.trackRect.width - cw), tw, arc, arc);
/*     */         } 
/*     */       } else {
/* 283 */         track = new RoundRectangle2D.Float(this.trackRect.x, y, this.trackRect.width, tw, arc, arc);
/*     */       }  }
/* 285 */     else { float x = this.trackRect.x + (this.trackRect.width - tw) / 2.0F;
/* 286 */       if (enabled && isRoundThumb()) {
/* 287 */         int ch = this.thumbRect.y + this.thumbRect.height / 2 - this.trackRect.y;
/* 288 */         track = new RoundRectangle2D.Float(x, this.trackRect.y, tw, ch, arc, arc);
/* 289 */         coloredTrack = new RoundRectangle2D.Float(x, (this.trackRect.y + ch), tw, (this.trackRect.height - ch), arc, arc);
/*     */       } else {
/* 291 */         track = new RoundRectangle2D.Float(x, this.trackRect.y, tw, this.trackRect.height, arc, arc);
/*     */       }  }
/*     */     
/* 294 */     if (coloredTrack != null) {
/* 295 */       if (this.slider.getInverted()) {
/* 296 */         RoundRectangle2D temp = track;
/* 297 */         track = coloredTrack;
/* 298 */         coloredTrack = temp;
/*     */       } 
/*     */       
/* 301 */       g.setColor(getTrackValueColor());
/* 302 */       ((Graphics2D)g).fill(coloredTrack);
/*     */     } 
/*     */     
/* 305 */     g.setColor(enabled ? getTrackColor() : this.disabledTrackColor);
/* 306 */     ((Graphics2D)g).fill(track);
/*     */   }
/*     */ 
/*     */   
/*     */   public void paintThumb(Graphics g) {
/* 311 */     Color thumbColor = getThumbColor();
/* 312 */     Color color = stateColor(this.slider, this.thumbHover, this.thumbPressed, thumbColor, this.disabledThumbColor, (Color)null, this.hoverThumbColor, this.pressedThumbColor);
/*     */     
/* 314 */     color = FlatUIUtils.deriveColor(color, thumbColor);
/*     */     
/* 316 */     Color foreground = this.slider.getForeground();
/*     */     
/* 318 */     Color borderColor = (this.thumbBorderColor != null && foreground == this.defaultForeground) ? stateColor(this.slider, false, false, this.thumbBorderColor, this.disabledThumbBorderColor, this.focusedThumbBorderColor, (Color)null, (Color)null) : null;
/*     */ 
/*     */     
/* 321 */     Color focusedColor = FlatUIUtils.deriveColor(this.focusedColor, (foreground != this.defaultForeground) ? foreground : this.focusBaseColor);
/*     */ 
/*     */     
/* 324 */     paintThumb(g, this.slider, this.thumbRect, isRoundThumb(), color, borderColor, focusedColor, this.focusWidth);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void paintThumb(Graphics g, JSlider slider, Rectangle thumbRect, boolean roundThumb, Color thumbColor, Color thumbBorderColor, Color focusedColor, int focusWidth) {
/* 330 */     double systemScaleFactor = UIScale.getSystemScaleFactor((Graphics2D)g);
/* 331 */     if (systemScaleFactor != 1.0D && systemScaleFactor != 2.0D) {
/*     */       
/* 333 */       HiDPIUtils.paintAtScale1x((Graphics2D)g, thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height, (g2d, x2, y2, width2, height2, scaleFactor) -> paintThumbImpl(g, slider, x2, y2, width2, height2, roundThumb, thumbColor, thumbBorderColor, focusedColor, (float)(focusWidth * scaleFactor)));
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 342 */     paintThumbImpl(g, slider, thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height, roundThumb, thumbColor, thumbBorderColor, focusedColor, focusWidth);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void paintThumbImpl(Graphics g, JSlider slider, int x, int y, int width, int height, boolean roundThumb, Color thumbColor, Color thumbBorderColor, Color focusedColor, float focusWidth) {
/* 350 */     int fw = Math.round(UIScale.scale(focusWidth));
/* 351 */     int tx = x + fw;
/* 352 */     int ty = y + fw;
/* 353 */     int tw = width - fw - fw;
/* 354 */     int th = height - fw - fw;
/* 355 */     boolean focused = FlatUIUtils.isPermanentFocusOwner(slider);
/*     */     
/* 357 */     if (roundThumb) {
/*     */       
/* 359 */       if (focused) {
/* 360 */         g.setColor(focusedColor);
/* 361 */         ((Graphics2D)g).fill(createRoundThumbShape(x, y, width, height));
/*     */       } 
/*     */       
/* 364 */       if (thumbBorderColor != null) {
/*     */         
/* 366 */         g.setColor(thumbBorderColor);
/* 367 */         ((Graphics2D)g).fill(createRoundThumbShape(tx, ty, tw, th));
/*     */ 
/*     */         
/* 370 */         float lw = UIScale.scale(1.0F);
/* 371 */         g.setColor(thumbColor);
/* 372 */         ((Graphics2D)g).fill(createRoundThumbShape(tx + lw, ty + lw, tw - lw - lw, th - lw - lw));
/*     */       }
/*     */       else {
/*     */         
/* 376 */         g.setColor(thumbColor);
/* 377 */         ((Graphics2D)g).fill(createRoundThumbShape(tx, ty, tw, th));
/*     */       } 
/*     */     } else {
/* 380 */       Graphics2D g2 = (Graphics2D)g.create();
/*     */       try {
/* 382 */         g2.translate(x, y);
/* 383 */         if (slider.getOrientation() == 1) {
/* 384 */           if (slider.getComponentOrientation().isLeftToRight()) {
/* 385 */             g2.translate(0, height);
/* 386 */             g2.rotate(Math.toRadians(270.0D));
/*     */           } else {
/* 388 */             g2.translate(width, 0);
/* 389 */             g2.rotate(Math.toRadians(90.0D));
/*     */           } 
/*     */ 
/*     */           
/* 393 */           int temp = tw;
/* 394 */           tw = th;
/* 395 */           th = temp;
/*     */         } 
/*     */ 
/*     */         
/* 399 */         if (focused) {
/* 400 */           g2.setColor(focusedColor);
/* 401 */           g2.fill(createDirectionalThumbShape(0.0F, 0.0F, (tw + fw + fw), (th + fw + fw) + fw * 0.4142F, fw));
/*     */         } 
/*     */ 
/*     */         
/* 405 */         if (thumbBorderColor != null) {
/*     */           
/* 407 */           g2.setColor(thumbBorderColor);
/* 408 */           g2.fill(createDirectionalThumbShape(fw, fw, tw, th, 0.0F));
/*     */ 
/*     */           
/* 411 */           float lw = UIScale.scale(1.0F);
/* 412 */           g2.setColor(thumbColor);
/* 413 */           g2.fill(createDirectionalThumbShape(fw + lw, fw + lw, tw - lw - lw, th - lw - lw - lw * 0.4142F, 0.0F));
/*     */         }
/*     */         else {
/*     */           
/* 417 */           g2.setColor(thumbColor);
/* 418 */           g2.fill(createDirectionalThumbShape(fw, fw, tw, th, 0.0F));
/*     */         } 
/*     */       } finally {
/* 421 */         g2.dispose();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Shape createRoundThumbShape(float x, float y, float w, float h) {
/* 427 */     if (w == h) {
/* 428 */       return new Ellipse2D.Float(x, y, w, h);
/*     */     }
/* 430 */     float arc = Math.min(w, h);
/* 431 */     return new RoundRectangle2D.Float(x, y, w, h, arc, arc);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Shape createDirectionalThumbShape(float x, float y, float w, float h, float arc) {
/* 436 */     float wh = w / 2.0F;
/*     */     
/* 438 */     Path2D path = new Path2D.Float();
/* 439 */     path.moveTo((x + wh), (y + h));
/* 440 */     path.lineTo(x, (y + h - wh));
/* 441 */     path.lineTo(x, (y + arc));
/* 442 */     path.quadTo(x, y, (x + arc), y);
/* 443 */     path.lineTo((x + w - arc), y);
/* 444 */     path.quadTo((x + w), y, (x + w), (y + arc));
/* 445 */     path.lineTo((x + w), (y + h - wh));
/* 446 */     path.closePath();
/*     */     
/* 448 */     return path;
/*     */   }
/*     */   
/*     */   protected Color getTrackValueColor() {
/* 452 */     Color foreground = this.slider.getForeground();
/* 453 */     return (foreground != this.defaultForeground) ? foreground : this.trackValueColor;
/*     */   }
/*     */   
/*     */   protected Color getTrackColor() {
/* 457 */     Color backround = this.slider.getBackground();
/* 458 */     return (backround != this.defaultBackground) ? backround : this.trackColor;
/*     */   }
/*     */   
/*     */   protected Color getThumbColor() {
/* 462 */     Color foreground = this.slider.getForeground();
/* 463 */     return (foreground != this.defaultForeground) ? foreground : this.thumbColor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Color stateColor(JSlider slider, boolean hover, boolean pressed, Color enabledColor, Color disabledColor, Color focusedColor, Color hoverColor, Color pressedColor) {
/* 469 */     if (disabledColor != null && !slider.isEnabled())
/* 470 */       return disabledColor; 
/* 471 */     if (pressedColor != null && pressed)
/* 472 */       return pressedColor; 
/* 473 */     if (hoverColor != null && hover)
/* 474 */       return hoverColor; 
/* 475 */     if (focusedColor != null && FlatUIUtils.isPermanentFocusOwner(slider))
/* 476 */       return focusedColor; 
/* 477 */     return enabledColor;
/*     */   }
/*     */   
/*     */   protected boolean isRoundThumb() {
/* 481 */     return (!this.slider.getPaintTicks() && !this.slider.getPaintLabels());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setThumbLocation(int x, int y) {
/* 486 */     if (!isRoundThumb()) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 491 */       Rectangle r = new Rectangle(this.thumbRect);
/* 492 */       this.thumbRect.setLocation(x, y);
/* 493 */       SwingUtilities.computeUnion(this.thumbRect.x, this.thumbRect.y, this.thumbRect.width, this.thumbRect.height, r);
/*     */ 
/*     */       
/* 496 */       int extra = (int)Math.ceil((UIScale.scale(this.focusWidth) * 0.4142F));
/* 497 */       if (this.slider.getOrientation() == 0) {
/* 498 */         r.height += extra;
/*     */       } else {
/* 500 */         r.width += extra;
/* 501 */         if (!this.slider.getComponentOrientation().isLeftToRight()) {
/* 502 */           r.x -= extra;
/*     */         }
/*     */       } 
/* 505 */       this.slider.repaint(r);
/*     */     } else {
/* 507 */       super.setThumbLocation(x, y);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected class FlatTrackListener
/*     */     extends BasicSliderUI.TrackListener
/*     */   {
/*     */     public void mouseEntered(MouseEvent e) {
/* 517 */       setThumbHover(isOverThumb(e));
/* 518 */       super.mouseEntered(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void mouseExited(MouseEvent e) {
/* 523 */       setThumbHover(false);
/* 524 */       super.mouseExited(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void mouseMoved(MouseEvent e) {
/* 529 */       setThumbHover(isOverThumb(e));
/* 530 */       super.mouseMoved(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void mousePressed(MouseEvent e) {
/* 535 */       setThumbPressed(isOverThumb(e));
/*     */       
/* 537 */       if (!FlatSliderUI.this.slider.isEnabled()) {
/*     */         return;
/*     */       }
/*     */       
/* 541 */       if (UIManager.getBoolean("Slider.scrollOnTrackClick")) {
/* 542 */         super.mousePressed(e);
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 548 */       int x = e.getX();
/* 549 */       int y = e.getY();
/*     */ 
/*     */       
/* 552 */       FlatSliderUI.this.calculateGeometry();
/* 553 */       if (FlatSliderUI.this.thumbRect.contains(x, y)) {
/* 554 */         super.mousePressed(e);
/*     */         
/*     */         return;
/*     */       } 
/* 558 */       if (UIManager.getBoolean("Slider.onlyLeftMouseButtonDrag") && 
/* 559 */         !SwingUtilities.isLeftMouseButton(e)) {
/*     */         return;
/*     */       }
/*     */       
/* 563 */       int tx = FlatSliderUI.this.thumbRect.x + FlatSliderUI.this.thumbRect.width / 2 - x;
/* 564 */       int ty = FlatSliderUI.this.thumbRect.y + FlatSliderUI.this.thumbRect.height / 2 - y;
/* 565 */       e.translatePoint(tx, ty);
/*     */ 
/*     */       
/* 568 */       super.mousePressed(e);
/*     */ 
/*     */       
/* 571 */       e.translatePoint(-tx, -ty);
/*     */ 
/*     */       
/* 574 */       mouseDragged(e);
/*     */       
/* 576 */       setThumbPressed(true);
/*     */     }
/*     */ 
/*     */     
/*     */     public void mouseReleased(MouseEvent e) {
/* 581 */       setThumbPressed(false);
/* 582 */       super.mouseReleased(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void mouseDragged(MouseEvent e) {
/* 587 */       super.mouseDragged(e);
/*     */       
/* 589 */       if (FlatSliderUI.this.isDragging() && FlatSliderUI.this
/* 590 */         .slider.getSnapToTicks() && FlatSliderUI.this
/* 591 */         .slider.isEnabled() && 
/* 592 */         !UIManager.getBoolean("Slider.snapToTicksOnReleased")) {
/*     */         
/* 594 */         FlatSliderUI.this.calculateThumbLocation();
/* 595 */         FlatSliderUI.this.slider.repaint();
/*     */       } 
/*     */     }
/*     */     
/*     */     protected void setThumbHover(boolean hover) {
/* 600 */       if (hover != FlatSliderUI.this.thumbHover) {
/* 601 */         FlatSliderUI.this.thumbHover = hover;
/* 602 */         FlatSliderUI.this.slider.repaint(FlatSliderUI.this.thumbRect);
/*     */       } 
/*     */     }
/*     */     
/*     */     protected void setThumbPressed(boolean pressed) {
/* 607 */       if (pressed != FlatSliderUI.this.thumbPressed) {
/* 608 */         FlatSliderUI.this.thumbPressed = pressed;
/* 609 */         FlatSliderUI.this.slider.repaint(FlatSliderUI.this.thumbRect);
/*     */       } 
/*     */     }
/*     */     
/*     */     protected boolean isOverThumb(MouseEvent e) {
/* 614 */       return (e != null && FlatSliderUI.this.slider.isEnabled() && FlatSliderUI.this.thumbRect.contains(e.getX(), e.getY()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatSliderUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */