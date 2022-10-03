/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.FlatClientProperties;
/*     */ import com.formdev.flatlaf.util.SystemInfo;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.awt.MouseInfo;
/*     */ import java.awt.Panel;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.ComponentListener;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLayeredPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JWindow;
/*     */ import javax.swing.Popup;
/*     */ import javax.swing.PopupFactory;
/*     */ import javax.swing.RootPaneContainer;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatPopupFactory
/*     */   extends PopupFactory
/*     */ {
/*     */   private Method java8getPopupMethod;
/*     */   private Method java9getPopupMethod;
/*     */   
/*     */   public Popup getPopup(Component owner, Component contents, int x, int y) throws IllegalArgumentException {
/*  65 */     Point pt = fixToolTipLocation(owner, contents, x, y);
/*  66 */     if (pt != null) {
/*  67 */       x = pt.x;
/*  68 */       y = pt.y;
/*     */     } 
/*     */     
/*  71 */     boolean forceHeavyWeight = isOptionEnabled(owner, contents, "Popup.forceHeavyWeight", "Popup.forceHeavyWeight");
/*     */     
/*  73 */     if (!isOptionEnabled(owner, contents, "Popup.dropShadowPainted", "Popup.dropShadowPainted")) {
/*  74 */       return new NonFlashingPopup(getPopupForScreenOfOwner(owner, contents, x, y, forceHeavyWeight), contents);
/*     */     }
/*     */     
/*  77 */     if (SystemInfo.isMacOS || SystemInfo.isLinux) {
/*  78 */       return new NonFlashingPopup(getPopupForScreenOfOwner(owner, contents, x, y, true), contents);
/*     */     }
/*     */     
/*  81 */     return new DropShadowPopup(getPopupForScreenOfOwner(owner, contents, x, y, forceHeavyWeight), owner, contents);
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
/*     */   private Popup getPopupForScreenOfOwner(Component owner, Component contents, int x, int y, boolean forceHeavyWeight) throws IllegalArgumentException {
/* 101 */     int count = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 107 */       Popup popup = forceHeavyWeight ? getHeavyWeightPopup(owner, contents, x, y) : super.getPopup(owner, contents, x, y);
/*     */ 
/*     */       
/* 110 */       Window popupWindow = SwingUtilities.windowForComponent(contents);
/*     */ 
/*     */       
/* 113 */       if (popupWindow == null || popupWindow
/* 114 */         .getGraphicsConfiguration() == owner.getGraphicsConfiguration()) {
/* 115 */         return popup;
/*     */       }
/*     */       
/* 118 */       if (popupWindow instanceof JWindow) {
/* 119 */         ((JWindow)popupWindow).getContentPane().removeAll();
/*     */       }
/*     */ 
/*     */       
/* 123 */       popupWindow.dispose();
/*     */ 
/*     */       
/* 126 */       if (++count > 10) {
/* 127 */         return popup;
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
/*     */   private static void showPopupAndFixLocation(Popup popup, Window popupWindow) {
/* 141 */     if (popupWindow != null) {
/*     */       
/* 143 */       int x = popupWindow.getX();
/* 144 */       int y = popupWindow.getY();
/*     */       
/* 146 */       popup.show();
/*     */ 
/*     */ 
/*     */       
/* 150 */       if (popupWindow.getX() != x || popupWindow.getY() != y)
/* 151 */         popupWindow.setLocation(x, y); 
/*     */     } else {
/* 153 */       popup.show();
/*     */     } 
/*     */   }
/*     */   private boolean isOptionEnabled(Component owner, Component contents, String clientKey, String uiKey) {
/* 157 */     if (owner instanceof JComponent) {
/* 158 */       Boolean b = FlatClientProperties.clientPropertyBooleanStrict((JComponent)owner, clientKey, null);
/* 159 */       if (b != null) {
/* 160 */         return b.booleanValue();
/*     */       }
/*     */     } 
/* 163 */     if (contents instanceof JComponent) {
/* 164 */       Boolean b = FlatClientProperties.clientPropertyBooleanStrict((JComponent)contents, clientKey, null);
/* 165 */       if (b != null) {
/* 166 */         return b.booleanValue();
/*     */       }
/*     */     } 
/* 169 */     return UIManager.getBoolean(uiKey);
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
/*     */   private Popup getHeavyWeightPopup(Component owner, Component contents, int x, int y) throws IllegalArgumentException {
/*     */     try {
/* 184 */       if (SystemInfo.isJava_9_orLater) {
/* 185 */         if (this.java9getPopupMethod == null) {
/* 186 */           this.java9getPopupMethod = PopupFactory.class.getDeclaredMethod("getPopup", new Class[] { Component.class, Component.class, int.class, int.class, boolean.class });
/*     */         }
/*     */         
/* 189 */         return (Popup)this.java9getPopupMethod.invoke(this, new Object[] { owner, contents, Integer.valueOf(x), Integer.valueOf(y), Boolean.valueOf(true) });
/*     */       } 
/*     */       
/* 192 */       if (this.java8getPopupMethod == null) {
/* 193 */         this.java8getPopupMethod = PopupFactory.class.getDeclaredMethod("getPopup", new Class[] { Component.class, Component.class, int.class, int.class, int.class });
/*     */         
/* 195 */         this.java8getPopupMethod.setAccessible(true);
/*     */       } 
/* 197 */       return (Popup)this.java8getPopupMethod.invoke(this, new Object[] { owner, contents, Integer.valueOf(x), Integer.valueOf(y), Integer.valueOf(2) });
/*     */     }
/* 199 */     catch (NoSuchMethodException|SecurityException|IllegalAccessException|java.lang.reflect.InvocationTargetException ex) {
/*     */       
/* 201 */       return null;
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
/*     */   private Point fixToolTipLocation(Component owner, Component contents, int x, int y) {
/* 215 */     if (!(contents instanceof javax.swing.JToolTip) || !wasInvokedFromToolTipManager()) {
/* 216 */       return null;
/*     */     }
/* 218 */     Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
/* 219 */     Dimension tipSize = contents.getPreferredSize();
/*     */ 
/*     */     
/* 222 */     Rectangle tipBounds = new Rectangle(x, y, tipSize.width, tipSize.height);
/* 223 */     if (!tipBounds.contains(mouseLocation)) {
/* 224 */       return null;
/*     */     }
/*     */     
/* 227 */     return new Point(x, mouseLocation.y - tipSize.height - UIScale.scale(20));
/*     */   }
/*     */   
/*     */   private boolean wasInvokedFromToolTipManager() {
/* 231 */     StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
/* 232 */     for (StackTraceElement stackTraceElement : stackTrace) {
/* 233 */       if ("javax.swing.ToolTipManager".equals(stackTraceElement.getClassName()) && "showTipWindow"
/* 234 */         .equals(stackTraceElement.getMethodName()))
/* 235 */         return true; 
/*     */     } 
/* 237 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private class NonFlashingPopup
/*     */     extends Popup
/*     */   {
/*     */     private Popup delegate;
/*     */     
/*     */     private Component contents;
/*     */     
/*     */     protected Window popupWindow;
/*     */     
/*     */     private Color oldPopupWindowBackground;
/*     */     
/*     */     NonFlashingPopup(Popup delegate, Component contents) {
/* 253 */       this.delegate = delegate;
/* 254 */       this.contents = contents;
/*     */       
/* 256 */       this.popupWindow = SwingUtilities.windowForComponent(contents);
/* 257 */       if (this.popupWindow != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 262 */         this.oldPopupWindowBackground = this.popupWindow.getBackground();
/* 263 */         this.popupWindow.setBackground(contents.getBackground());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void show() {
/* 269 */       if (this.delegate != null) {
/* 270 */         FlatPopupFactory.showPopupAndFixLocation(this.delegate, this.popupWindow);
/*     */ 
/*     */ 
/*     */         
/* 274 */         if (this.contents instanceof javax.swing.JToolTip && this.popupWindow == null) {
/* 275 */           Container parent = this.contents.getParent();
/* 276 */           if (parent instanceof JPanel) {
/* 277 */             Dimension prefSize = parent.getPreferredSize();
/* 278 */             if (!prefSize.equals(parent.getSize())) {
/* 279 */               Container mediumWeightPanel = SwingUtilities.getAncestorOfClass(Panel.class, parent);
/* 280 */               Container c = (mediumWeightPanel != null) ? mediumWeightPanel : parent;
/*     */ 
/*     */               
/* 283 */               c.setSize(prefSize);
/* 284 */               c.validate();
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void hide() {
/* 293 */       if (this.delegate != null) {
/* 294 */         this.delegate.hide();
/* 295 */         this.delegate = null;
/* 296 */         this.contents = null;
/*     */       } 
/*     */       
/* 299 */       if (this.popupWindow != null) {
/*     */ 
/*     */         
/* 302 */         this.popupWindow.setBackground(this.oldPopupWindowBackground);
/* 303 */         this.popupWindow = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class DropShadowPopup
/*     */     extends NonFlashingPopup
/*     */   {
/*     */     private final Component owner;
/*     */     
/*     */     private JComponent lightComp;
/*     */     
/*     */     private Border oldBorder;
/*     */     
/*     */     private boolean oldOpaque;
/*     */     
/*     */     private boolean mediumWeightShown;
/*     */     
/*     */     private Panel mediumWeightPanel;
/*     */     
/*     */     private JPanel dropShadowPanel;
/*     */     
/*     */     private ComponentListener mediumPanelListener;
/*     */     private Popup dropShadowDelegate;
/*     */     private Window dropShadowWindow;
/*     */     private Color oldDropShadowWindowBackground;
/*     */     
/*     */     DropShadowPopup(Popup delegate, Component owner, Component contents) {
/* 332 */       super(delegate, contents);
/* 333 */       this.owner = owner;
/*     */       
/* 335 */       Dimension size = contents.getPreferredSize();
/* 336 */       if (size.width <= 0 || size.height <= 0) {
/*     */         return;
/*     */       }
/* 339 */       if (this.popupWindow != null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 349 */         JPanel dropShadowPanel = new JPanel();
/* 350 */         dropShadowPanel.setBorder(createDropShadowBorder());
/* 351 */         dropShadowPanel.setOpaque(false);
/*     */ 
/*     */         
/* 354 */         Dimension prefSize = this.popupWindow.getPreferredSize();
/* 355 */         Insets insets = dropShadowPanel.getInsets();
/* 356 */         dropShadowPanel.setPreferredSize(new Dimension(prefSize.width + insets.left + insets.right, prefSize.height + insets.top + insets.bottom));
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 361 */         int x = this.popupWindow.getX() - insets.left;
/* 362 */         int y = this.popupWindow.getY() - insets.top;
/* 363 */         this.dropShadowDelegate = FlatPopupFactory.this.getPopupForScreenOfOwner(owner, dropShadowPanel, x, y, true);
/*     */ 
/*     */         
/* 366 */         this.dropShadowWindow = SwingUtilities.windowForComponent(dropShadowPanel);
/* 367 */         if (this.dropShadowWindow != null) {
/* 368 */           this.oldDropShadowWindowBackground = this.dropShadowWindow.getBackground();
/* 369 */           this.dropShadowWindow.setBackground(new Color(0, true));
/*     */         } 
/*     */       } else {
/* 372 */         this.mediumWeightPanel = (Panel)SwingUtilities.getAncestorOfClass(Panel.class, contents);
/* 373 */         if (this.mediumWeightPanel != null) {
/*     */           
/* 375 */           this.dropShadowPanel = new JPanel();
/* 376 */           this.dropShadowPanel.setBorder(createDropShadowBorder());
/* 377 */           this.dropShadowPanel.setOpaque(false);
/* 378 */           this.dropShadowPanel.setSize(FlatUIUtils.addInsets(this.mediumWeightPanel.getSize(), this.dropShadowPanel.getInsets()));
/*     */         } else {
/*     */           
/* 381 */           Container p = contents.getParent();
/* 382 */           if (!(p instanceof JComponent)) {
/*     */             return;
/*     */           }
/* 385 */           this.lightComp = (JComponent)p;
/* 386 */           this.oldBorder = this.lightComp.getBorder();
/* 387 */           this.oldOpaque = this.lightComp.isOpaque();
/* 388 */           this.lightComp.setBorder(createDropShadowBorder());
/* 389 */           this.lightComp.setOpaque(false);
/* 390 */           this.lightComp.setSize(this.lightComp.getPreferredSize());
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private Border createDropShadowBorder() {
/* 396 */       return new FlatDropShadowBorder(
/* 397 */           UIManager.getColor("Popup.dropShadowColor"), 
/* 398 */           UIManager.getInsets("Popup.dropShadowInsets"), 
/* 399 */           FlatUIUtils.getUIFloat("Popup.dropShadowOpacity", 0.5F));
/*     */     }
/*     */ 
/*     */     
/*     */     public void show() {
/* 404 */       if (this.dropShadowDelegate != null) {
/* 405 */         FlatPopupFactory.showPopupAndFixLocation(this.dropShadowDelegate, this.dropShadowWindow);
/*     */       }
/* 407 */       if (this.mediumWeightPanel != null) {
/* 408 */         showMediumWeightDropShadow();
/*     */       }
/* 410 */       super.show();
/*     */ 
/*     */       
/* 413 */       if (this.lightComp != null) {
/* 414 */         Insets insets = this.lightComp.getInsets();
/* 415 */         if (insets.left != 0 || insets.top != 0) {
/* 416 */           this.lightComp.setLocation(this.lightComp.getX() - insets.left, this.lightComp.getY() - insets.top);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     public void hide() {
/* 422 */       if (this.dropShadowDelegate != null) {
/* 423 */         this.dropShadowDelegate.hide();
/* 424 */         this.dropShadowDelegate = null;
/*     */       } 
/*     */       
/* 427 */       if (this.mediumWeightPanel != null) {
/* 428 */         hideMediumWeightDropShadow();
/* 429 */         this.dropShadowPanel = null;
/* 430 */         this.mediumWeightPanel = null;
/*     */       } 
/*     */       
/* 433 */       super.hide();
/*     */       
/* 435 */       if (this.dropShadowWindow != null) {
/* 436 */         this.dropShadowWindow.setBackground(this.oldDropShadowWindowBackground);
/* 437 */         this.dropShadowWindow = null;
/*     */       } 
/*     */       
/* 440 */       if (this.lightComp != null) {
/* 441 */         this.lightComp.setBorder(this.oldBorder);
/* 442 */         this.lightComp.setOpaque(this.oldOpaque);
/* 443 */         this.lightComp = null;
/*     */       } 
/*     */     }
/*     */     
/*     */     private void showMediumWeightDropShadow() {
/* 448 */       if (this.mediumWeightShown) {
/*     */         return;
/*     */       }
/* 451 */       this.mediumWeightShown = true;
/*     */       
/* 453 */       Window window = SwingUtilities.windowForComponent(this.owner);
/* 454 */       if (window == null) {
/*     */         return;
/*     */       }
/* 457 */       if (!(window instanceof RootPaneContainer)) {
/*     */         return;
/*     */       }
/* 460 */       this.dropShadowPanel.setVisible(false);
/*     */       
/* 462 */       JLayeredPane layeredPane = ((RootPaneContainer)window).getLayeredPane();
/* 463 */       layeredPane.add(this.dropShadowPanel, JLayeredPane.POPUP_LAYER, 0);
/*     */       
/* 465 */       this.mediumPanelListener = new ComponentListener()
/*     */         {
/*     */           public void componentShown(ComponentEvent e) {
/* 468 */             if (FlatPopupFactory.DropShadowPopup.this.dropShadowPanel != null) {
/* 469 */               FlatPopupFactory.DropShadowPopup.this.dropShadowPanel.setVisible(true);
/*     */             }
/*     */           }
/*     */           
/*     */           public void componentHidden(ComponentEvent e) {
/* 474 */             if (FlatPopupFactory.DropShadowPopup.this.dropShadowPanel != null) {
/* 475 */               FlatPopupFactory.DropShadowPopup.this.dropShadowPanel.setVisible(false);
/*     */             }
/*     */           }
/*     */           
/*     */           public void componentMoved(ComponentEvent e) {
/* 480 */             if (FlatPopupFactory.DropShadowPopup.this.dropShadowPanel != null && FlatPopupFactory.DropShadowPopup.this.mediumWeightPanel != null) {
/* 481 */               Point location = FlatPopupFactory.DropShadowPopup.this.mediumWeightPanel.getLocation();
/* 482 */               Insets insets = FlatPopupFactory.DropShadowPopup.this.dropShadowPanel.getInsets();
/* 483 */               FlatPopupFactory.DropShadowPopup.this.dropShadowPanel.setLocation(location.x - insets.left, location.y - insets.top);
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public void componentResized(ComponentEvent e) {
/* 489 */             if (FlatPopupFactory.DropShadowPopup.this.dropShadowPanel != null)
/* 490 */               FlatPopupFactory.DropShadowPopup.this.dropShadowPanel.setSize(FlatUIUtils.addInsets(FlatPopupFactory.DropShadowPopup.this.mediumWeightPanel.getSize(), FlatPopupFactory.DropShadowPopup.this.dropShadowPanel.getInsets())); 
/*     */           }
/*     */         };
/* 493 */       this.mediumWeightPanel.addComponentListener(this.mediumPanelListener);
/*     */     }
/*     */     
/*     */     private void hideMediumWeightDropShadow() {
/* 497 */       this.mediumWeightPanel.removeComponentListener(this.mediumPanelListener);
/*     */       
/* 499 */       Container parent = this.dropShadowPanel.getParent();
/* 500 */       if (parent != null) {
/* 501 */         Rectangle bounds = this.dropShadowPanel.getBounds();
/* 502 */         parent.remove(this.dropShadowPanel);
/* 503 */         parent.repaint(bounds.x, bounds.y, bounds.width, bounds.height);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatPopupFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */