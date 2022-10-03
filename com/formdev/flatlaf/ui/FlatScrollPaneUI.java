/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ContainerEvent;
/*     */ import java.awt.event.ContainerListener;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.awt.event.MouseWheelEvent;
/*     */ import java.awt.event.MouseWheelListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JScrollBar;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JViewport;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.Scrollable;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicScrollPaneUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatScrollPaneUI
/*     */   extends BasicScrollPaneUI
/*     */ {
/*     */   private Handler handler;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/*  70 */     return new FlatScrollPaneUI();
/*     */   }
/*     */ 
/*     */   
/*     */   public void installUI(JComponent c) {
/*  75 */     super.installUI(c);
/*     */     
/*  77 */     int focusWidth = UIManager.getInt("Component.focusWidth");
/*  78 */     LookAndFeel.installProperty(c, "opaque", Boolean.valueOf((focusWidth == 0)));
/*     */     
/*  80 */     MigLayoutVisualPadding.install(this.scrollpane);
/*     */   }
/*     */ 
/*     */   
/*     */   public void uninstallUI(JComponent c) {
/*  85 */     MigLayoutVisualPadding.uninstall(this.scrollpane);
/*     */     
/*  87 */     super.uninstallUI(c);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installListeners(JScrollPane c) {
/*  92 */     super.installListeners(c);
/*     */     
/*  94 */     addViewportListeners(this.scrollpane.getViewport());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void uninstallListeners(JComponent c) {
/*  99 */     super.uninstallListeners(c);
/*     */     
/* 101 */     removeViewportListeners(this.scrollpane.getViewport());
/*     */     
/* 103 */     this.handler = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected MouseWheelListener createMouseWheelListener() {
/* 108 */     return new BasicScrollPaneUI.MouseWheelHandler()
/*     */       {
/*     */         public void mouseWheelMoved(MouseWheelEvent e) {
/* 111 */           if (FlatScrollPaneUI.this.isSmoothScrollingEnabled() && FlatScrollPaneUI.this
/* 112 */             .scrollpane.isWheelScrollingEnabled() && e
/* 113 */             .getScrollType() == 0 && e
/* 114 */             .getPreciseWheelRotation() != 0.0D && e
/* 115 */             .getPreciseWheelRotation() != e.getWheelRotation()) {
/*     */             
/* 117 */             FlatScrollPaneUI.this.mouseWheelMovedSmooth(e);
/*     */           } else {
/* 119 */             super.mouseWheelMoved(e);
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */   protected boolean isSmoothScrollingEnabled() {
/* 125 */     Object smoothScrolling = this.scrollpane.getClientProperty("JScrollPane.smoothScrolling");
/* 126 */     if (smoothScrolling instanceof Boolean) {
/* 127 */       return ((Boolean)smoothScrolling).booleanValue();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 132 */     return UIManager.getBoolean("ScrollPane.smoothScrolling");
/*     */   }
/*     */   
/*     */   private void mouseWheelMovedSmooth(MouseWheelEvent e) {
/*     */     int unitIncrement;
/* 137 */     JViewport viewport = this.scrollpane.getViewport();
/* 138 */     if (viewport == null) {
/*     */       return;
/*     */     }
/*     */     
/* 142 */     JScrollBar scrollbar = this.scrollpane.getVerticalScrollBar();
/* 143 */     if (scrollbar == null || !scrollbar.isVisible() || e.isShiftDown()) {
/* 144 */       scrollbar = this.scrollpane.getHorizontalScrollBar();
/* 145 */       if (scrollbar == null || !scrollbar.isVisible()) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */     
/* 150 */     e.consume();
/*     */ 
/*     */     
/* 153 */     double rotation = e.getPreciseWheelRotation();
/*     */ 
/*     */ 
/*     */     
/* 157 */     int orientation = scrollbar.getOrientation();
/* 158 */     Component view = viewport.getView();
/* 159 */     if (view instanceof Scrollable) {
/* 160 */       Scrollable scrollable = (Scrollable)view;
/*     */ 
/*     */ 
/*     */       
/* 164 */       Rectangle visibleRect = new Rectangle(viewport.getViewSize());
/* 165 */       unitIncrement = scrollable.getScrollableUnitIncrement(visibleRect, orientation, 1);
/*     */       
/* 167 */       if (unitIncrement > 0) {
/*     */ 
/*     */ 
/*     */         
/* 171 */         if (orientation == 1) {
/* 172 */           visibleRect.y += unitIncrement;
/* 173 */           visibleRect.height -= unitIncrement;
/*     */         } else {
/* 175 */           visibleRect.x += unitIncrement;
/* 176 */           visibleRect.width -= unitIncrement;
/*     */         } 
/* 178 */         int unitIncrement2 = scrollable.getScrollableUnitIncrement(visibleRect, orientation, 1);
/* 179 */         if (unitIncrement2 > 0)
/* 180 */           unitIncrement = Math.min(unitIncrement, unitIncrement2); 
/*     */       } 
/*     */     } else {
/* 183 */       int direction = (rotation < 0.0D) ? -1 : 1;
/* 184 */       unitIncrement = scrollbar.getUnitIncrement(direction);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 190 */     int viewportWH = (orientation == 1) ? viewport.getHeight() : viewport.getWidth();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 195 */     int scrollIncrement = Math.min(unitIncrement * e.getScrollAmount(), viewportWH);
/*     */ 
/*     */     
/* 198 */     double delta = rotation * scrollIncrement;
/* 199 */     int idelta = (int)Math.round(delta);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 205 */     if (idelta == 0) {
/* 206 */       if (rotation > 0.0D) {
/* 207 */         idelta = 1;
/* 208 */       } else if (rotation < 0.0D) {
/* 209 */         idelta = -1;
/*     */       } 
/*     */     }
/*     */     
/* 213 */     int value = scrollbar.getValue();
/* 214 */     int minValue = scrollbar.getMinimum();
/* 215 */     int maxValue = scrollbar.getMaximum() - scrollbar.getModel().getExtent();
/* 216 */     int newValue = Math.max(minValue, Math.min(value + idelta, maxValue));
/*     */ 
/*     */     
/* 219 */     if (newValue != value) {
/* 220 */       scrollbar.setValue(newValue);
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
/*     */   protected PropertyChangeListener createPropertyChangeListener() {
/* 242 */     return new BasicScrollPaneUI.PropertyChangeHandler() { public void propertyChange(PropertyChangeEvent e) {
/*     */           JScrollBar vsb, hsb;
/*     */           Object corner;
/* 245 */           super.propertyChange(e);
/*     */           
/* 247 */           switch (e.getPropertyName()) {
/*     */             case "JScrollBar.showButtons":
/* 249 */               vsb = FlatScrollPaneUI.this.scrollpane.getVerticalScrollBar();
/* 250 */               hsb = FlatScrollPaneUI.this.scrollpane.getHorizontalScrollBar();
/* 251 */               if (vsb != null) {
/* 252 */                 vsb.revalidate();
/* 253 */                 vsb.repaint();
/*     */               } 
/* 255 */               if (hsb != null) {
/* 256 */                 hsb.revalidate();
/* 257 */                 hsb.repaint();
/*     */               } 
/*     */               break;
/*     */ 
/*     */             
/*     */             case "LOWER_LEFT_CORNER":
/*     */             case "LOWER_RIGHT_CORNER":
/*     */             case "UPPER_LEFT_CORNER":
/*     */             case "UPPER_RIGHT_CORNER":
/* 266 */               corner = e.getNewValue();
/* 267 */               if (corner instanceof JButton && ((JButton)corner)
/* 268 */                 .getBorder() instanceof FlatButtonBorder && FlatScrollPaneUI.this
/* 269 */                 .scrollpane.getViewport() != null && FlatScrollPaneUI.this
/* 270 */                 .scrollpane.getViewport().getView() instanceof javax.swing.JTable) {
/*     */                 
/* 272 */                 ((JButton)corner).setBorder(BorderFactory.createEmptyBorder());
/* 273 */                 ((JButton)corner).setFocusable(false);
/*     */               } 
/*     */               break;
/*     */           } 
/*     */         } }
/*     */       ;
/*     */   }
/*     */   
/*     */   private Handler getHandler() {
/* 282 */     if (this.handler == null)
/* 283 */       this.handler = new Handler(); 
/* 284 */     return this.handler;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateViewport(PropertyChangeEvent e) {
/* 289 */     super.updateViewport(e);
/*     */     
/* 291 */     JViewport oldViewport = (JViewport)e.getOldValue();
/* 292 */     JViewport newViewport = (JViewport)e.getNewValue();
/*     */     
/* 294 */     removeViewportListeners(oldViewport);
/* 295 */     addViewportListeners(newViewport);
/*     */   }
/*     */   
/*     */   private void addViewportListeners(JViewport viewport) {
/* 299 */     if (viewport == null) {
/*     */       return;
/*     */     }
/* 302 */     viewport.addContainerListener(getHandler());
/*     */     
/* 304 */     Component view = viewport.getView();
/* 305 */     if (view != null)
/* 306 */       view.addFocusListener(getHandler()); 
/*     */   }
/*     */   
/*     */   private void removeViewportListeners(JViewport viewport) {
/* 310 */     if (viewport == null) {
/*     */       return;
/*     */     }
/* 313 */     viewport.removeContainerListener(getHandler());
/*     */     
/* 315 */     Component view = viewport.getView();
/* 316 */     if (view != null) {
/* 317 */       view.removeFocusListener(getHandler());
/*     */     }
/*     */   }
/*     */   
/*     */   public void update(Graphics g, JComponent c) {
/* 322 */     if (c.isOpaque()) {
/* 323 */       FlatUIUtils.paintParentBackground(g, c);
/*     */ 
/*     */       
/* 326 */       Insets insets = c.getInsets();
/* 327 */       g.setColor(c.getBackground());
/* 328 */       g.fillRect(insets.left, insets.top, c
/* 329 */           .getWidth() - insets.left - insets.right, c
/* 330 */           .getHeight() - insets.top - insets.bottom);
/*     */     } 
/*     */     
/* 333 */     paint(g, c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class Handler
/*     */     implements ContainerListener, FocusListener
/*     */   {
/*     */     private Handler() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void componentAdded(ContainerEvent e) {
/* 347 */       e.getChild().addFocusListener(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void componentRemoved(ContainerEvent e) {
/* 352 */       e.getChild().removeFocusListener(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void focusGained(FocusEvent e) {
/* 357 */       FlatScrollPaneUI.this.scrollpane.repaint();
/*     */     }
/*     */ 
/*     */     
/*     */     public void focusLost(FocusEvent e) {
/* 362 */       FlatScrollPaneUI.this.scrollpane.repaint();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatScrollPaneUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */