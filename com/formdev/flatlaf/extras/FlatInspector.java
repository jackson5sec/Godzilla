/*     */ package com.formdev.flatlaf.extras;
/*     */ 
/*     */ import com.formdev.flatlaf.ui.FlatToolTipUI;
/*     */ import com.formdev.flatlaf.ui.FlatUIUtils;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.AWTEvent;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.KeyboardFocusManager;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.MouseInfo;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.AWTEventListener;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseMotionAdapter;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.lang.reflect.Field;
/*     */ import javax.swing.AbstractButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.JToolTip;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.RootPaneContainer;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import javax.swing.border.LineBorder;
/*     */ import javax.swing.text.JTextComponent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatInspector
/*     */ {
/*  85 */   private static final Integer HIGHLIGHT_LAYER = Integer.valueOf(401);
/*  86 */   private static final Integer TOOLTIP_LAYER = Integer.valueOf(402);
/*     */   
/*     */   private static final int KEY_MODIFIERS_MASK = 960;
/*     */   
/*     */   private final JRootPane rootPane;
/*     */   private final MouseMotionListener mouseMotionListener;
/*     */   private final AWTEventListener keyListener;
/*  93 */   private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
/*     */   
/*     */   private boolean enabled;
/*     */   
/*     */   private Component lastComponent;
/*     */   
/*     */   private int lastX;
/*     */   
/*     */   private int lastY;
/*     */   
/*     */   private int inspectParentLevel;
/*     */   
/*     */   private boolean wasCtrlOrShiftKeyPressed;
/*     */   
/*     */   private JComponent highlightFigure;
/*     */   
/*     */   private JToolTip tip;
/*     */   
/*     */   public static void install(String activationKeys) {
/* 112 */     KeyStroke keyStroke = KeyStroke.getKeyStroke(activationKeys);
/* 113 */     Toolkit.getDefaultToolkit().addAWTEventListener(e -> { if (e.getID() == 402 && ((KeyEvent)e).getKeyCode() == keyStroke.getKeyCode() && (((KeyEvent)e).getModifiersEx() & 0x3C0) == (keyStroke.getModifiers() & 0x3C0)) { Window activeWindow = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow(); if (activeWindow instanceof RootPaneContainer) { JRootPane rootPane = ((RootPaneContainer)activeWindow).getRootPane(); FlatInspector inspector = (FlatInspector)rootPane.getClientProperty(FlatInspector.class); if (inspector == null) { inspector = new FlatInspector(rootPane); rootPane.putClientProperty(FlatInspector.class, inspector); inspector.setEnabled(true); } else { inspector.uninstall(); rootPane.putClientProperty(FlatInspector.class, null); }  }  }  }8L);
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
/*     */   
/*     */   public FlatInspector(JRootPane rootPane) {
/* 136 */     this.rootPane = rootPane;
/*     */     
/* 138 */     this.mouseMotionListener = new MouseMotionAdapter()
/*     */       {
/*     */         public void mouseMoved(MouseEvent e) {
/* 141 */           FlatInspector.this.lastX = e.getX();
/* 142 */           FlatInspector.this.lastY = e.getY();
/* 143 */           FlatInspector.this.inspect(FlatInspector.this.lastX, FlatInspector.this.lastY);
/*     */         }
/*     */       };
/*     */     
/* 147 */     rootPane.getGlassPane().addMouseMotionListener(this.mouseMotionListener);
/*     */     
/* 149 */     this.keyListener = (e -> {
/*     */         KeyEvent keyEvent = (KeyEvent)e;
/*     */         int keyCode = keyEvent.getKeyCode();
/*     */         int id = e.getID();
/*     */         if (id == 401) {
/*     */           if (keyCode == 17 || keyCode == 16) {
/*     */             this.wasCtrlOrShiftKeyPressed = true;
/*     */           }
/*     */         } else if (id == 402 && this.wasCtrlOrShiftKeyPressed) {
/*     */           if (keyCode == 17) {
/*     */             this.inspectParentLevel++;
/*     */             int parentLevel = inspect(this.lastX, this.lastY);
/*     */             if (this.inspectParentLevel > parentLevel) {
/*     */               this.inspectParentLevel = parentLevel;
/*     */             }
/*     */           } else if (keyCode == 16 && this.inspectParentLevel > 0) {
/*     */             this.inspectParentLevel--;
/*     */             int parentLevel = inspect(this.lastX, this.lastY);
/*     */             if (this.inspectParentLevel > parentLevel) {
/*     */               this.inspectParentLevel = Math.max(parentLevel - 1, 0);
/*     */               inspect(this.lastX, this.lastY);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         if (keyCode == 27) {
/*     */           keyEvent.consume();
/*     */           if (id == 401) {
/*     */             FlatInspector inspector = (FlatInspector)rootPane.getClientProperty(FlatInspector.class);
/*     */             if (inspector == this) {
/*     */               uninstall();
/*     */               rootPane.putClientProperty(FlatInspector.class, null);
/*     */             } else {
/*     */               setEnabled(false);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void uninstall() {
/* 196 */     setEnabled(false);
/* 197 */     this.rootPane.getGlassPane().setVisible(false);
/* 198 */     this.rootPane.getGlassPane().removeMouseMotionListener(this.mouseMotionListener);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPropertyChangeListener(PropertyChangeListener l) {
/* 203 */     this.propertyChangeSupport.addPropertyChangeListener(l);
/*     */   }
/*     */   
/*     */   public void removePropertyChangeListener(PropertyChangeListener l) {
/* 207 */     this.propertyChangeSupport.removePropertyChangeListener(l);
/*     */   }
/*     */   
/*     */   public boolean isEnabled() {
/* 211 */     return this.enabled;
/*     */   }
/*     */   
/*     */   public void setEnabled(boolean enabled) {
/* 215 */     if (this.enabled == enabled) {
/*     */       return;
/*     */     }
/* 218 */     this.enabled = enabled;
/*     */ 
/*     */     
/* 221 */     ((JComponent)this.rootPane.getGlassPane()).setOpaque(false);
/*     */     
/* 223 */     this.rootPane.getGlassPane().setVisible(enabled);
/*     */     
/* 225 */     Toolkit toolkit = Toolkit.getDefaultToolkit();
/* 226 */     if (enabled) {
/* 227 */       toolkit.addAWTEventListener(this.keyListener, 8L);
/*     */     } else {
/* 229 */       toolkit.removeAWTEventListener(this.keyListener);
/*     */     } 
/* 231 */     if (enabled) {
/* 232 */       Point pt = new Point(MouseInfo.getPointerInfo().getLocation());
/* 233 */       SwingUtilities.convertPointFromScreen(pt, this.rootPane);
/*     */       
/* 235 */       this.lastX = pt.x;
/* 236 */       this.lastY = pt.y;
/* 237 */       inspect(this.lastX, this.lastY);
/*     */     } else {
/* 239 */       this.lastComponent = null;
/* 240 */       this.inspectParentLevel = 0;
/*     */       
/* 242 */       if (this.highlightFigure != null)
/* 243 */         this.highlightFigure.getParent().remove(this.highlightFigure); 
/* 244 */       this.highlightFigure = null;
/*     */       
/* 246 */       if (this.tip != null)
/* 247 */         this.tip.getParent().remove(this.tip); 
/* 248 */       this.tip = null;
/*     */     } 
/*     */     
/* 251 */     this.propertyChangeSupport.firePropertyChange("enabled", !enabled, enabled);
/*     */   }
/*     */   
/*     */   public void update() {
/* 255 */     if (!this.rootPane.getGlassPane().isVisible()) {
/*     */       return;
/*     */     }
/* 258 */     EventQueue.invokeLater(() -> {
/*     */           setEnabled(false);
/*     */           setEnabled(true);
/*     */           inspect(this.lastX, this.lastY);
/*     */         });
/*     */   }
/*     */   
/*     */   private int inspect(int x, int y) {
/* 266 */     Point pt = SwingUtilities.convertPoint(this.rootPane.getGlassPane(), x, y, this.rootPane);
/* 267 */     Component c = getDeepestComponentAt(this.rootPane, pt.x, pt.y);
/* 268 */     int parentLevel = 0;
/* 269 */     for (int i = 0; i < this.inspectParentLevel && c != null; i++) {
/* 270 */       Container parent = c.getParent();
/* 271 */       if (parent == null) {
/*     */         break;
/*     */       }
/* 274 */       c = parent;
/* 275 */       parentLevel++;
/*     */     } 
/*     */     
/* 278 */     if (c == this.lastComponent) {
/* 279 */       return parentLevel;
/*     */     }
/* 281 */     this.lastComponent = c;
/*     */     
/* 283 */     highlight(c);
/* 284 */     showToolTip(c, x, y, parentLevel);
/*     */     
/* 286 */     return parentLevel;
/*     */   }
/*     */   
/*     */   private Component getDeepestComponentAt(Component parent, int x, int y) {
/* 290 */     if (!parent.contains(x, y)) {
/* 291 */       return null;
/*     */     }
/* 293 */     if (parent instanceof Container)
/* 294 */       for (Component child : ((Container)parent).getComponents()) {
/* 295 */         if (child != null && child.isVisible()) {
/*     */ 
/*     */           
/* 298 */           int cx = x - child.getX();
/* 299 */           int cy = y - child.getY();
/*     */ 
/*     */           
/* 302 */           Component c = (child instanceof Container) ? getDeepestComponentAt(child, cx, cy) : child.getComponentAt(cx, cy);
/* 303 */           if (c != null && c.isVisible())
/*     */           {
/*     */ 
/*     */             
/* 307 */             if (c != this.highlightFigure && c != this.tip)
/*     */             {
/*     */ 
/*     */               
/* 311 */               if (!(c.getParent() instanceof JRootPane) || c != ((JRootPane)c.getParent()).getGlassPane())
/*     */               {
/*     */                 
/* 314 */                 if (!"com.formdev.flatlaf.ui.FlatWindowResizer".equals(c.getClass().getName()))
/*     */                 {
/*     */                   
/* 317 */                   return c; }  }  } 
/*     */           }
/*     */         } 
/*     */       }  
/* 321 */     return parent;
/*     */   }
/*     */   
/*     */   private void highlight(Component c) {
/* 325 */     if (this.highlightFigure == null) {
/* 326 */       this.highlightFigure = createHighlightFigure();
/* 327 */       this.rootPane.getLayeredPane().add(this.highlightFigure, HIGHLIGHT_LAYER);
/*     */     } 
/*     */     
/* 330 */     this.highlightFigure.setVisible((c != null));
/*     */     
/* 332 */     if (c != null) {
/* 333 */       Insets insets = this.rootPane.getInsets();
/* 334 */       this.highlightFigure.setBounds(new Rectangle(
/* 335 */             SwingUtilities.convertPoint(c, -insets.left, -insets.top, this.rootPane), c
/* 336 */             .getSize()));
/*     */     } 
/*     */   }
/*     */   
/*     */   private JComponent createHighlightFigure() {
/* 341 */     JComponent c = new JComponent()
/*     */       {
/*     */         protected void paintComponent(Graphics g) {
/* 344 */           g.setColor(getBackground());
/* 345 */           g.fillRect(0, 0, getWidth(), getHeight());
/*     */         }
/*     */ 
/*     */         
/*     */         protected void paintBorder(Graphics g) {
/* 350 */           Object[] oldRenderingHints = FlatUIUtils.setRenderingHints(g);
/* 351 */           super.paintBorder(g);
/* 352 */           FlatUIUtils.resetRenderingHints(g, oldRenderingHints);
/*     */         }
/*     */       };
/* 355 */     c.setBackground(new Color(255, 0, 0, 32));
/* 356 */     c.setBorder(new LineBorder(Color.red));
/* 357 */     return c;
/*     */   }
/*     */   
/*     */   private void showToolTip(Component c, int x, int y, int parentLevel) {
/* 361 */     if (c == null) {
/* 362 */       if (this.tip != null) {
/* 363 */         this.tip.setVisible(false);
/*     */       }
/*     */       return;
/*     */     } 
/* 367 */     if (this.tip == null) {
/* 368 */       this.tip = new JToolTip()
/*     */         {
/*     */           public void updateUI() {
/* 371 */             setUI(FlatToolTipUI.createUI(this));
/*     */           }
/*     */         };
/* 374 */       this.rootPane.getLayeredPane().add(this.tip, TOOLTIP_LAYER);
/*     */     } else {
/* 376 */       this.tip.setVisible(true);
/*     */     } 
/* 378 */     this.tip.setTipText(buildToolTipText(c, parentLevel));
/*     */     
/* 380 */     int tx = x + UIScale.scale(8);
/* 381 */     int ty = y + UIScale.scale(16);
/* 382 */     Dimension size = this.tip.getPreferredSize();
/*     */ 
/*     */     
/* 385 */     Rectangle visibleRect = this.rootPane.getVisibleRect();
/* 386 */     if (tx + size.width > visibleRect.x + visibleRect.width)
/* 387 */       tx -= size.width + UIScale.scale(16); 
/* 388 */     if (ty + size.height > visibleRect.y + visibleRect.height)
/* 389 */       ty -= size.height + UIScale.scale(32); 
/* 390 */     if (tx < visibleRect.x)
/* 391 */       tx = visibleRect.x; 
/* 392 */     if (ty < visibleRect.y) {
/* 393 */       ty = visibleRect.y;
/*     */     }
/* 395 */     this.tip.setBounds(tx, ty, size.width, size.height);
/* 396 */     this.tip.repaint();
/*     */   }
/*     */   
/*     */   private static String buildToolTipText(Component c, int parentLevel) {
/* 400 */     String name = c.getClass().getName();
/* 401 */     name = name.substring(name.lastIndexOf('.') + 1);
/*     */ 
/*     */ 
/*     */     
/* 405 */     String text = "Class: " + name + " (" + c.getClass().getPackage().getName() + ")\nSize: " + c.getWidth() + ',' + c.getHeight() + "  @ " + c.getX() + ',' + c.getY() + '\n';
/*     */     
/* 407 */     if (c instanceof Container) {
/* 408 */       text = text + "Insets: " + toString(((Container)c).getInsets()) + '\n';
/*     */     }
/* 410 */     Insets margin = null;
/* 411 */     if (c instanceof AbstractButton) {
/* 412 */       margin = ((AbstractButton)c).getMargin();
/* 413 */     } else if (c instanceof JTextComponent) {
/* 414 */       margin = ((JTextComponent)c).getMargin();
/* 415 */     } else if (c instanceof JMenuBar) {
/* 416 */       margin = ((JMenuBar)c).getMargin();
/* 417 */     } else if (c instanceof JToolBar) {
/* 418 */       margin = ((JToolBar)c).getMargin();
/*     */     } 
/* 420 */     if (margin != null) {
/* 421 */       text = text + "Margin: " + toString(margin) + '\n';
/*     */     }
/* 423 */     Dimension prefSize = c.getPreferredSize();
/* 424 */     Dimension minSize = c.getMinimumSize();
/* 425 */     Dimension maxSize = c.getMaximumSize();
/* 426 */     text = text + "Pref size: " + prefSize.width + ',' + prefSize.height + '\n' + "Min size: " + minSize.width + ',' + minSize.height + '\n' + "Max size: " + maxSize.width + ',' + maxSize.height + '\n';
/*     */ 
/*     */ 
/*     */     
/* 430 */     if (c instanceof JComponent) {
/* 431 */       text = text + "Border: " + toString(((JComponent)c).getBorder()) + '\n';
/*     */     }
/*     */ 
/*     */     
/* 435 */     text = text + "Background: " + toString(c.getBackground()) + '\n' + "Foreground: " + toString(c.getForeground()) + '\n' + "Font: " + toString(c.getFont()) + '\n';
/*     */     
/* 437 */     if (c instanceof JComponent) {
/*     */       try {
/* 439 */         Field f = JComponent.class.getDeclaredField("ui");
/* 440 */         f.setAccessible(true);
/* 441 */         Object ui = f.get(c);
/* 442 */         text = text + "UI: " + ((ui != null) ? ui.getClass().getName() : "null") + '\n';
/* 443 */       } catch (NoSuchFieldException|SecurityException|IllegalArgumentException|IllegalAccessException noSuchFieldException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 448 */     if (c instanceof Container) {
/* 449 */       LayoutManager layout = ((Container)c).getLayout();
/* 450 */       if (layout != null) {
/* 451 */         text = text + "Layout: " + layout.getClass().getName() + '\n';
/*     */       }
/*     */     } 
/* 454 */     text = text + "Enabled: " + c.isEnabled() + '\n';
/*     */     
/* 456 */     text = text + "Opaque: " + c.isOpaque() + ((c instanceof JComponent && FlatUIUtils.hasOpaqueBeenExplicitlySet((JComponent)c)) ? " EXPLICIT" : "") + '\n';
/* 457 */     if (c instanceof AbstractButton)
/* 458 */       text = text + "ContentAreaFilled: " + ((AbstractButton)c).isContentAreaFilled() + '\n'; 
/* 459 */     text = text + "Focusable: " + c.isFocusable() + '\n';
/* 460 */     text = text + "Left-to-right: " + c.getComponentOrientation().isLeftToRight() + '\n';
/* 461 */     text = text + "Parent: " + ((c.getParent() != null) ? c.getParent().getClass().getName() : "null");
/*     */     
/* 463 */     if (parentLevel > 0) {
/* 464 */       text = text + "\n\nParent level: " + parentLevel;
/*     */     }
/* 466 */     if (parentLevel > 0) {
/* 467 */       text = text + "\n(press Ctrl/Shift to increase/decrease level)";
/*     */     } else {
/* 469 */       text = text + "\n\n(press Ctrl key to inspect parent)";
/*     */     } 
/* 471 */     return text;
/*     */   }
/*     */   
/*     */   private static String toString(Insets insets) {
/* 475 */     if (insets == null) {
/* 476 */       return "null";
/*     */     }
/* 478 */     return insets.top + "," + insets.left + ',' + insets.bottom + ',' + insets.right + ((insets instanceof javax.swing.plaf.UIResource) ? " UI" : "");
/*     */   }
/*     */ 
/*     */   
/*     */   private static String toString(Color c) {
/* 483 */     if (c == null) {
/* 484 */       return "null";
/*     */     }
/* 486 */     String s = Long.toString(c.getRGB() & 0xFFFFFFFFL, 16);
/* 487 */     if (c instanceof javax.swing.plaf.UIResource)
/* 488 */       s = s + " UI"; 
/* 489 */     return s;
/*     */   }
/*     */   
/*     */   private static String toString(Font f) {
/* 493 */     if (f == null) {
/* 494 */       return "null";
/*     */     }
/* 496 */     return f.getFamily() + " " + f.getSize() + " " + f.getStyle() + ((f instanceof javax.swing.plaf.UIResource) ? " UI" : "");
/*     */   }
/*     */ 
/*     */   
/*     */   private static String toString(Border b) {
/* 501 */     if (b == null) {
/* 502 */       return "null";
/*     */     }
/* 504 */     String s = b.getClass().getName();
/*     */     
/* 506 */     if (b instanceof EmptyBorder) {
/* 507 */       s = s + '(' + toString(((EmptyBorder)b).getBorderInsets()) + ')';
/*     */     }
/* 509 */     if (b instanceof javax.swing.plaf.UIResource) {
/* 510 */       s = s + " UI";
/*     */     }
/* 512 */     return s;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\extras\FlatInspector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */