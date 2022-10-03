/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.Objects;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JScrollBar;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicScrollBarUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatScrollBarUI
/*     */   extends BasicScrollBarUI
/*     */ {
/*     */   protected Insets trackInsets;
/*     */   protected Insets thumbInsets;
/*     */   protected int trackArc;
/*     */   protected int thumbArc;
/*     */   protected Color hoverTrackColor;
/*     */   protected Color hoverThumbColor;
/*     */   protected boolean hoverThumbWithTrack;
/*     */   protected Color pressedTrackColor;
/*     */   protected Color pressedThumbColor;
/*     */   protected boolean pressedThumbWithTrack;
/*     */   protected boolean showButtons;
/*     */   protected String arrowType;
/*     */   protected Color buttonArrowColor;
/*     */   protected Color buttonDisabledArrowColor;
/*     */   protected Color hoverButtonBackground;
/*     */   protected Color pressedButtonBackground;
/*     */   private MouseAdapter hoverListener;
/*     */   protected boolean hoverTrack;
/*     */   protected boolean hoverThumb;
/*     */   private static boolean isPressed;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/* 102 */     return new FlatScrollBarUI();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installListeners() {
/* 107 */     super.installListeners();
/*     */     
/* 109 */     this.hoverListener = new ScrollBarHoverListener();
/* 110 */     this.scrollbar.addMouseListener(this.hoverListener);
/* 111 */     this.scrollbar.addMouseMotionListener(this.hoverListener);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallListeners() {
/* 116 */     super.uninstallListeners();
/*     */     
/* 118 */     this.scrollbar.removeMouseListener(this.hoverListener);
/* 119 */     this.scrollbar.removeMouseMotionListener(this.hoverListener);
/* 120 */     this.hoverListener = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults() {
/* 125 */     super.installDefaults();
/*     */     
/* 127 */     this.trackInsets = UIManager.getInsets("ScrollBar.trackInsets");
/* 128 */     this.thumbInsets = UIManager.getInsets("ScrollBar.thumbInsets");
/* 129 */     this.trackArc = UIManager.getInt("ScrollBar.trackArc");
/* 130 */     this.thumbArc = UIManager.getInt("ScrollBar.thumbArc");
/* 131 */     this.hoverTrackColor = UIManager.getColor("ScrollBar.hoverTrackColor");
/* 132 */     this.hoverThumbColor = UIManager.getColor("ScrollBar.hoverThumbColor");
/* 133 */     this.hoverThumbWithTrack = UIManager.getBoolean("ScrollBar.hoverThumbWithTrack");
/* 134 */     this.pressedTrackColor = UIManager.getColor("ScrollBar.pressedTrackColor");
/* 135 */     this.pressedThumbColor = UIManager.getColor("ScrollBar.pressedThumbColor");
/* 136 */     this.pressedThumbWithTrack = UIManager.getBoolean("ScrollBar.pressedThumbWithTrack");
/*     */     
/* 138 */     this.showButtons = UIManager.getBoolean("ScrollBar.showButtons");
/* 139 */     this.arrowType = UIManager.getString("Component.arrowType");
/* 140 */     this.buttonArrowColor = UIManager.getColor("ScrollBar.buttonArrowColor");
/* 141 */     this.buttonDisabledArrowColor = UIManager.getColor("ScrollBar.buttonDisabledArrowColor");
/* 142 */     this.hoverButtonBackground = UIManager.getColor("ScrollBar.hoverButtonBackground");
/* 143 */     this.pressedButtonBackground = UIManager.getColor("ScrollBar.pressedButtonBackground");
/*     */ 
/*     */     
/* 146 */     if (this.trackInsets == null)
/* 147 */       this.trackInsets = new Insets(0, 0, 0, 0); 
/* 148 */     if (this.thumbInsets == null) {
/* 149 */       this.thumbInsets = new Insets(0, 0, 0, 0);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void uninstallDefaults() {
/* 154 */     super.uninstallDefaults();
/*     */     
/* 156 */     this.trackInsets = null;
/* 157 */     this.thumbInsets = null;
/* 158 */     this.hoverTrackColor = null;
/* 159 */     this.hoverThumbColor = null;
/* 160 */     this.pressedTrackColor = null;
/* 161 */     this.pressedThumbColor = null;
/*     */     
/* 163 */     this.buttonArrowColor = null;
/* 164 */     this.buttonDisabledArrowColor = null;
/* 165 */     this.hoverButtonBackground = null;
/* 166 */     this.pressedButtonBackground = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected PropertyChangeListener createPropertyChangeListener() {
/* 171 */     return new BasicScrollBarUI.PropertyChangeHandler() {
/*     */         public void propertyChange(PropertyChangeEvent e) {
/*     */           InputMap inputMap;
/* 174 */           super.propertyChange(e);
/*     */           
/* 176 */           switch (e.getPropertyName()) {
/*     */             case "JScrollBar.showButtons":
/* 178 */               FlatScrollBarUI.this.scrollbar.revalidate();
/* 179 */               FlatScrollBarUI.this.scrollbar.repaint();
/*     */               break;
/*     */ 
/*     */             
/*     */             case "componentOrientation":
/* 184 */               inputMap = (InputMap)UIManager.get("ScrollBar.ancestorInputMap");
/* 185 */               if (!FlatScrollBarUI.this.scrollbar.getComponentOrientation().isLeftToRight()) {
/* 186 */                 InputMap rtlInputMap = (InputMap)UIManager.get("ScrollBar.ancestorInputMap.RightToLeft");
/* 187 */                 if (rtlInputMap != null) {
/* 188 */                   rtlInputMap.setParent(inputMap);
/* 189 */                   inputMap = rtlInputMap;
/*     */                 } 
/*     */               } 
/* 192 */               SwingUtilities.replaceUIInputMap(FlatScrollBarUI.this.scrollbar, 1, inputMap);
/*     */               break;
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Dimension getPreferredSize(JComponent c) {
/* 201 */     return UIScale.scale(super.getPreferredSize(c));
/*     */   }
/*     */ 
/*     */   
/*     */   protected JButton createDecreaseButton(int orientation) {
/* 206 */     return new FlatScrollBarButton(orientation);
/*     */   }
/*     */ 
/*     */   
/*     */   protected JButton createIncreaseButton(int orientation) {
/* 211 */     return new FlatScrollBarButton(orientation);
/*     */   }
/*     */   
/*     */   protected boolean isShowButtons() {
/* 215 */     Object showButtons = this.scrollbar.getClientProperty("JScrollBar.showButtons");
/* 216 */     if (showButtons == null && this.scrollbar.getParent() instanceof JScrollPane)
/* 217 */       showButtons = ((JScrollPane)this.scrollbar.getParent()).getClientProperty("JScrollBar.showButtons"); 
/* 218 */     return (showButtons != null) ? Objects.equals(showButtons, Boolean.valueOf(true)) : this.showButtons;
/*     */   }
/*     */ 
/*     */   
/*     */   public void paint(Graphics g, JComponent c) {
/* 223 */     Object[] oldRenderingHints = FlatUIUtils.setRenderingHints(g);
/* 224 */     super.paint(g, c);
/* 225 */     FlatUIUtils.resetRenderingHints(g, oldRenderingHints);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
/* 230 */     g.setColor(getTrackColor(c, this.hoverTrack, (isPressed && this.hoverTrack && !this.hoverThumb)));
/* 231 */     paintTrackOrThumb(g, c, trackBounds, this.trackInsets, this.trackArc);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
/* 236 */     if (thumbBounds.isEmpty() || !this.scrollbar.isEnabled()) {
/*     */       return;
/*     */     }
/* 239 */     g.setColor(getThumbColor(c, (this.hoverThumb || (this.hoverThumbWithTrack && this.hoverTrack)), (isPressed && (this.hoverThumb || (this.pressedThumbWithTrack && this.hoverTrack)))));
/*     */     
/* 241 */     paintTrackOrThumb(g, c, thumbBounds, this.thumbInsets, this.thumbArc);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void paintTrackOrThumb(Graphics g, JComponent c, Rectangle bounds, Insets insets, int arc) {
/* 246 */     if (this.scrollbar.getOrientation() == 0) {
/* 247 */       insets = new Insets(insets.right, insets.top, insets.left, insets.bottom);
/*     */     }
/*     */     
/* 250 */     bounds = FlatUIUtils.subtractInsets(bounds, UIScale.scale(insets));
/*     */     
/* 252 */     if (arc <= 0) {
/*     */       
/* 254 */       g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
/*     */     } else {
/*     */       
/* 257 */       arc = Math.min(UIScale.scale(arc), Math.min(bounds.width, bounds.height));
/* 258 */       g.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, arc, arc);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintDecreaseHighlight(Graphics g) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintIncreaseHighlight(Graphics g) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected Color getTrackColor(JComponent c, boolean hover, boolean pressed) {
/* 273 */     Color trackColor = FlatUIUtils.deriveColor(this.trackColor, c.getBackground());
/* 274 */     return (pressed && this.pressedTrackColor != null) ? 
/* 275 */       FlatUIUtils.deriveColor(this.pressedTrackColor, trackColor) : ((hover && this.hoverTrackColor != null) ? 
/*     */       
/* 277 */       FlatUIUtils.deriveColor(this.hoverTrackColor, trackColor) : trackColor);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Color getThumbColor(JComponent c, boolean hover, boolean pressed) {
/* 282 */     Color trackColor = FlatUIUtils.deriveColor(this.trackColor, c.getBackground());
/* 283 */     Color thumbColor = FlatUIUtils.deriveColor(this.thumbColor, trackColor);
/* 284 */     return (pressed && this.pressedThumbColor != null) ? 
/* 285 */       FlatUIUtils.deriveColor(this.pressedThumbColor, thumbColor) : ((hover && this.hoverThumbColor != null) ? 
/*     */       
/* 287 */       FlatUIUtils.deriveColor(this.hoverThumbColor, thumbColor) : thumbColor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Dimension getMinimumThumbSize() {
/* 293 */     return UIScale.scale(FlatUIUtils.addInsets(super.getMinimumThumbSize(), this.thumbInsets));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Dimension getMaximumThumbSize() {
/* 298 */     return UIScale.scale(FlatUIUtils.addInsets(super.getMaximumThumbSize(), this.thumbInsets));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class ScrollBarHoverListener
/*     */     extends MouseAdapter
/*     */   {
/*     */     private ScrollBarHoverListener() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void mouseExited(MouseEvent e) {
/* 311 */       if (!FlatScrollBarUI.isPressed) {
/* 312 */         FlatScrollBarUI.this.hoverTrack = FlatScrollBarUI.this.hoverThumb = false;
/* 313 */         repaint();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void mouseMoved(MouseEvent e) {
/* 319 */       if (!FlatScrollBarUI.isPressed) {
/* 320 */         update(e.getX(), e.getY());
/*     */       }
/*     */     }
/*     */     
/*     */     public void mousePressed(MouseEvent e) {
/* 325 */       FlatScrollBarUI.isPressed = true;
/* 326 */       repaint();
/*     */     }
/*     */ 
/*     */     
/*     */     public void mouseReleased(MouseEvent e) {
/* 331 */       FlatScrollBarUI.isPressed = false;
/* 332 */       repaint();
/*     */       
/* 334 */       update(e.getX(), e.getY());
/*     */     }
/*     */     
/*     */     private void update(int x, int y) {
/* 338 */       boolean inTrack = FlatScrollBarUI.this.getTrackBounds().contains(x, y);
/* 339 */       boolean inThumb = FlatScrollBarUI.this.getThumbBounds().contains(x, y);
/* 340 */       if (inTrack != FlatScrollBarUI.this.hoverTrack || inThumb != FlatScrollBarUI.this.hoverThumb) {
/* 341 */         FlatScrollBarUI.this.hoverTrack = inTrack;
/* 342 */         FlatScrollBarUI.this.hoverThumb = inThumb;
/* 343 */         repaint();
/*     */       } 
/*     */     }
/*     */     
/*     */     private void repaint() {
/* 348 */       if (FlatScrollBarUI.this.scrollbar.isEnabled()) {
/* 349 */         FlatScrollBarUI.this.scrollbar.repaint();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected class FlatScrollBarButton
/*     */     extends FlatArrowButton
/*     */   {
/*     */     protected FlatScrollBarButton(int direction) {
/* 359 */       this(direction, FlatScrollBarUI.this.arrowType, FlatScrollBarUI.this.buttonArrowColor, FlatScrollBarUI.this.buttonDisabledArrowColor, (Color)null, FlatScrollBarUI.this.hoverButtonBackground, (Color)null, FlatScrollBarUI.this.pressedButtonBackground);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected FlatScrollBarButton(int direction, String type, Color foreground, Color disabledForeground, Color hoverForeground, Color hoverBackground, Color pressedForeground, Color pressedBackground) {
/* 366 */       super(direction, type, foreground, disabledForeground, hoverForeground, hoverBackground, pressedForeground, pressedBackground);
/*     */ 
/*     */       
/* 369 */       setArrowWidth(6);
/* 370 */       setFocusable(false);
/* 371 */       setRequestFocusEnabled(false);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Color deriveBackground(Color background) {
/* 376 */       return FlatUIUtils.deriveColor(background, FlatScrollBarUI.this.scrollbar.getBackground());
/*     */     }
/*     */ 
/*     */     
/*     */     public Dimension getPreferredSize() {
/* 381 */       if (FlatScrollBarUI.this.isShowButtons()) {
/* 382 */         int w = UIScale.scale(FlatScrollBarUI.this.scrollBarWidth);
/* 383 */         return new Dimension(w, w);
/*     */       } 
/* 385 */       return new Dimension();
/*     */     }
/*     */ 
/*     */     
/*     */     public Dimension getMinimumSize() {
/* 390 */       return FlatScrollBarUI.this.isShowButtons() ? super.getMinimumSize() : new Dimension();
/*     */     }
/*     */ 
/*     */     
/*     */     public Dimension getMaximumSize() {
/* 395 */       return FlatScrollBarUI.this.isShowButtons() ? super.getMaximumSize() : new Dimension();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatScrollBarUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */