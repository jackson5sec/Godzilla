/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Shape;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.geom.Path2D;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicArrowButton;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatArrowButton
/*     */   extends BasicArrowButton
/*     */   implements UIResource
/*     */ {
/*     */   public static final int DEFAULT_ARROW_WIDTH = 8;
/*     */   protected final boolean chevron;
/*     */   protected final Color foreground;
/*     */   protected final Color disabledForeground;
/*     */   protected final Color hoverForeground;
/*     */   protected final Color hoverBackground;
/*     */   protected final Color pressedForeground;
/*     */   protected final Color pressedBackground;
/*  53 */   private int arrowWidth = 8;
/*  54 */   private int xOffset = 0;
/*  55 */   private int yOffset = 0;
/*     */   
/*     */   private boolean hover;
/*     */   
/*     */   private boolean pressed;
/*     */ 
/*     */   
/*     */   public FlatArrowButton(int direction, String type, Color foreground, Color disabledForeground, Color hoverForeground, Color hoverBackground, Color pressedForeground, Color pressedBackground) {
/*  63 */     super(direction, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
/*     */     
/*  65 */     this.chevron = FlatUIUtils.isChevron(type);
/*  66 */     this.foreground = foreground;
/*  67 */     this.disabledForeground = disabledForeground;
/*  68 */     this.hoverForeground = hoverForeground;
/*  69 */     this.hoverBackground = hoverBackground;
/*  70 */     this.pressedForeground = pressedForeground;
/*  71 */     this.pressedBackground = pressedBackground;
/*     */     
/*  73 */     setOpaque(false);
/*  74 */     setBorder((Border)null);
/*     */     
/*  76 */     if (hoverForeground != null || hoverBackground != null || pressedForeground != null || pressedBackground != null)
/*     */     {
/*     */       
/*  79 */       addMouseListener(new MouseAdapter()
/*     */           {
/*     */             public void mouseEntered(MouseEvent e) {
/*  82 */               FlatArrowButton.this.hover = true;
/*  83 */               FlatArrowButton.this.repaint();
/*     */             }
/*     */ 
/*     */             
/*     */             public void mouseExited(MouseEvent e) {
/*  88 */               FlatArrowButton.this.hover = false;
/*  89 */               FlatArrowButton.this.repaint();
/*     */             }
/*     */ 
/*     */             
/*     */             public void mousePressed(MouseEvent e) {
/*  94 */               FlatArrowButton.this.pressed = true;
/*  95 */               FlatArrowButton.this.repaint();
/*     */             }
/*     */ 
/*     */             
/*     */             public void mouseReleased(MouseEvent e) {
/* 100 */               FlatArrowButton.this.pressed = false;
/* 101 */               FlatArrowButton.this.repaint();
/*     */             }
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   public int getArrowWidth() {
/* 108 */     return this.arrowWidth;
/*     */   }
/*     */   
/*     */   public void setArrowWidth(int arrowWidth) {
/* 112 */     this.arrowWidth = arrowWidth;
/*     */   }
/*     */   
/*     */   protected boolean isHover() {
/* 116 */     return this.hover;
/*     */   }
/*     */   
/*     */   protected boolean isPressed() {
/* 120 */     return this.pressed;
/*     */   }
/*     */   
/*     */   public int getXOffset() {
/* 124 */     return this.xOffset;
/*     */   }
/*     */   
/*     */   public void setXOffset(int xOffset) {
/* 128 */     this.xOffset = xOffset;
/*     */   }
/*     */   
/*     */   public int getYOffset() {
/* 132 */     return this.yOffset;
/*     */   }
/*     */   
/*     */   public void setYOffset(int yOffset) {
/* 136 */     this.yOffset = yOffset;
/*     */   }
/*     */   
/*     */   protected Color deriveBackground(Color background) {
/* 140 */     return background;
/*     */   }
/*     */   
/*     */   protected Color deriveForeground(Color foreground) {
/* 144 */     return FlatUIUtils.deriveColor(foreground, this.foreground);
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getPreferredSize() {
/* 149 */     return UIScale.scale(super.getPreferredSize());
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getMinimumSize() {
/* 154 */     return UIScale.scale(super.getMinimumSize());
/*     */   }
/*     */ 
/*     */   
/*     */   public void paint(Graphics g) {
/* 159 */     Object[] oldRenderingHints = FlatUIUtils.setRenderingHints(g);
/*     */ 
/*     */     
/* 162 */     if (isEnabled()) {
/*     */ 
/*     */       
/* 165 */       Color background = (this.pressedBackground != null && isPressed()) ? this.pressedBackground : ((this.hoverBackground != null && isHover()) ? this.hoverBackground : null);
/*     */ 
/*     */ 
/*     */       
/* 169 */       if (background != null) {
/* 170 */         g.setColor(deriveBackground(background));
/* 171 */         paintBackground((Graphics2D)g);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 176 */     g.setColor(deriveForeground(isEnabled() ? ((this.pressedForeground != null && 
/* 177 */           isPressed()) ? this.pressedForeground : ((this.hoverForeground != null && 
/*     */           
/* 179 */           isHover()) ? this.hoverForeground : this.foreground)) : this.disabledForeground));
/*     */ 
/*     */ 
/*     */     
/* 183 */     paintArrow((Graphics2D)g);
/*     */     
/* 185 */     FlatUIUtils.resetRenderingHints(g, oldRenderingHints);
/*     */   }
/*     */   
/*     */   protected void paintBackground(Graphics2D g) {
/* 189 */     g.fillRect(0, 0, getWidth(), getHeight());
/*     */   }
/*     */   
/*     */   protected void paintArrow(Graphics2D g) {
/* 193 */     int direction = getDirection();
/* 194 */     boolean vert = (direction == 1 || direction == 5);
/*     */ 
/*     */     
/* 197 */     int w = UIScale.scale(this.arrowWidth + (this.chevron ? 0 : 1));
/* 198 */     int h = UIScale.scale(this.arrowWidth / 2 + (this.chevron ? 0 : 1));
/*     */ 
/*     */     
/* 201 */     int rw = vert ? w : h;
/* 202 */     int rh = vert ? h : w;
/*     */ 
/*     */     
/* 205 */     if (this.chevron) {
/*     */       
/* 207 */       rw++;
/* 208 */       rh++;
/*     */     } 
/*     */     
/* 211 */     int x = Math.round((getWidth() - rw) / 2.0F + UIScale.scale(this.xOffset));
/* 212 */     int y = Math.round((getHeight() - rh) / 2.0F + UIScale.scale(this.yOffset));
/*     */ 
/*     */     
/* 215 */     Container parent = getParent();
/* 216 */     if (vert && parent instanceof JComponent && FlatUIUtils.hasRoundBorder((JComponent)parent)) {
/* 217 */       x -= UIScale.scale(parent.getComponentOrientation().isLeftToRight() ? 1 : -1);
/*     */     }
/*     */     
/* 220 */     g.translate(x, y);
/*     */ 
/*     */ 
/*     */     
/* 224 */     Shape arrowShape = createArrowShape(direction, this.chevron, w, h);
/* 225 */     if (this.chevron) {
/* 226 */       g.setStroke(new BasicStroke(UIScale.scale(1.0F)));
/* 227 */       g.draw(arrowShape);
/*     */     } else {
/*     */       
/* 230 */       g.fill(arrowShape);
/*     */     } 
/* 232 */     g.translate(-x, -y);
/*     */   }
/*     */   
/*     */   public static Shape createArrowShape(int direction, boolean chevron, float w, float h) {
/* 236 */     switch (direction) { case 1:
/* 237 */         return FlatUIUtils.createPath(!chevron, new double[] { 0.0D, h, (w / 2.0F), 0.0D, w, h });
/* 238 */       case 5: return FlatUIUtils.createPath(!chevron, new double[] { 0.0D, 0.0D, (w / 2.0F), h, w, 0.0D });
/* 239 */       case 7: return FlatUIUtils.createPath(!chevron, new double[] { h, 0.0D, 0.0D, (w / 2.0F), h, w });
/* 240 */       case 3: return FlatUIUtils.createPath(!chevron, new double[] { 0.0D, 0.0D, h, (w / 2.0F), 0.0D, w }); }
/* 241 */      return new Path2D.Float();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatArrowButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */