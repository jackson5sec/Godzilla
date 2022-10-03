/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Container;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.ComponentListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.event.WindowStateListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.function.Supplier;
/*     */ import javax.swing.DesktopManager;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JLayeredPane;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.UIManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class FlatWindowResizer
/*     */   implements PropertyChangeListener, ComponentListener
/*     */ {
/*  59 */   protected static final Integer WINDOW_RESIZER_LAYER = Integer.valueOf(JLayeredPane.DRAG_LAYER.intValue() + 1);
/*     */   
/*     */   protected final JComponent resizeComp;
/*     */   
/*  63 */   protected final int borderDragThickness = FlatUIUtils.getUIInt("RootPane.borderDragThickness", 5);
/*  64 */   protected final int cornerDragWidth = FlatUIUtils.getUIInt("RootPane.cornerDragWidth", 16);
/*  65 */   protected final boolean honorFrameMinimumSizeOnResize = UIManager.getBoolean("RootPane.honorFrameMinimumSizeOnResize");
/*  66 */   protected final boolean honorDialogMinimumSizeOnResize = UIManager.getBoolean("RootPane.honorDialogMinimumSizeOnResize");
/*     */   
/*     */   protected final DragBorderComponent topDragComp;
/*     */   protected final DragBorderComponent bottomDragComp;
/*     */   protected final DragBorderComponent leftDragComp;
/*     */   protected final DragBorderComponent rightDragComp;
/*     */   
/*     */   protected FlatWindowResizer(JComponent resizeComp) {
/*  74 */     this.resizeComp = resizeComp;
/*     */     
/*  76 */     this.topDragComp = createDragBorderComponent(6, 8, 7);
/*  77 */     this.bottomDragComp = createDragBorderComponent(4, 9, 5);
/*  78 */     this.leftDragComp = createDragBorderComponent(6, 10, 4);
/*  79 */     this.rightDragComp = createDragBorderComponent(7, 11, 5);
/*     */     
/*  81 */     Container cont = (resizeComp instanceof JRootPane) ? ((JRootPane)resizeComp).getLayeredPane() : resizeComp;
/*  82 */     Object cons = (cont instanceof JLayeredPane) ? WINDOW_RESIZER_LAYER : null;
/*  83 */     cont.add(this.topDragComp, cons, 0);
/*  84 */     cont.add(this.bottomDragComp, cons, 1);
/*  85 */     cont.add(this.leftDragComp, cons, 2);
/*  86 */     cont.add(this.rightDragComp, cons, 3);
/*     */     
/*  88 */     resizeComp.addComponentListener(this);
/*  89 */     resizeComp.addPropertyChangeListener("ancestor", this);
/*     */     
/*  91 */     if (resizeComp.isDisplayable())
/*  92 */       addNotify(); 
/*     */   }
/*     */   
/*     */   protected DragBorderComponent createDragBorderComponent(int leadingResizeDir, int centerResizeDir, int trailingResizeDir) {
/*  96 */     return new DragBorderComponent(leadingResizeDir, centerResizeDir, trailingResizeDir);
/*     */   }
/*     */   
/*     */   public void uninstall() {
/* 100 */     removeNotify();
/*     */     
/* 102 */     this.resizeComp.removeComponentListener(this);
/* 103 */     this.resizeComp.removePropertyChangeListener("ancestor", this);
/*     */     
/* 105 */     Container cont = this.topDragComp.getParent();
/* 106 */     cont.remove(this.topDragComp);
/* 107 */     cont.remove(this.bottomDragComp);
/* 108 */     cont.remove(this.leftDragComp);
/* 109 */     cont.remove(this.rightDragComp);
/*     */   }
/*     */   
/*     */   public void doLayout() {
/* 113 */     if (!this.topDragComp.isVisible()) {
/*     */       return;
/*     */     }
/* 116 */     int x = 0;
/* 117 */     int y = 0;
/* 118 */     int width = this.resizeComp.getWidth();
/* 119 */     int height = this.resizeComp.getHeight();
/* 120 */     if (width == 0 || height == 0) {
/*     */       return;
/*     */     }
/* 123 */     Insets resizeInsets = getResizeInsets();
/* 124 */     int thickness = UIScale.scale(this.borderDragThickness);
/* 125 */     int topThickness = Math.max(resizeInsets.top, thickness);
/* 126 */     int bottomThickness = Math.max(resizeInsets.bottom, thickness);
/* 127 */     int leftThickness = Math.max(resizeInsets.left, thickness);
/* 128 */     int rightThickness = Math.max(resizeInsets.right, thickness);
/* 129 */     int y2 = y + topThickness;
/* 130 */     int height2 = height - topThickness - bottomThickness;
/*     */ 
/*     */     
/* 133 */     this.topDragComp.setBounds(x, y, width, topThickness);
/* 134 */     this.bottomDragComp.setBounds(x, y + height - bottomThickness, width, bottomThickness);
/* 135 */     this.leftDragComp.setBounds(x, y2, leftThickness, height2);
/* 136 */     this.rightDragComp.setBounds(x + width - rightThickness, y2, rightThickness, height2);
/*     */ 
/*     */     
/* 139 */     int cornerDelta = UIScale.scale(this.cornerDragWidth - this.borderDragThickness);
/* 140 */     this.topDragComp.setCornerDragWidths(leftThickness + cornerDelta, rightThickness + cornerDelta);
/* 141 */     this.bottomDragComp.setCornerDragWidths(leftThickness + cornerDelta, rightThickness + cornerDelta);
/* 142 */     this.leftDragComp.setCornerDragWidths(cornerDelta, cornerDelta);
/* 143 */     this.rightDragComp.setCornerDragWidths(cornerDelta, cornerDelta);
/*     */   }
/*     */   
/*     */   protected Insets getResizeInsets() {
/* 147 */     return new Insets(0, 0, 0, 0);
/*     */   }
/*     */   
/*     */   protected void addNotify() {
/* 151 */     updateVisibility();
/*     */   }
/*     */   
/*     */   protected void removeNotify() {
/* 155 */     updateVisibility();
/*     */   }
/*     */   
/*     */   protected void updateVisibility() {
/* 159 */     boolean visible = isWindowResizable();
/* 160 */     if (visible == this.topDragComp.isVisible()) {
/*     */       return;
/*     */     }
/* 163 */     this.topDragComp.setVisible(visible);
/* 164 */     this.bottomDragComp.setVisible(visible);
/* 165 */     this.leftDragComp.setVisible(visible);
/*     */ 
/*     */ 
/*     */     
/* 169 */     this.rightDragComp.setEnabled(visible);
/* 170 */     if (visible) {
/* 171 */       this.rightDragComp.setVisible(true);
/* 172 */       doLayout();
/*     */     } else {
/* 174 */       this.rightDragComp.setBounds(0, 0, 1, 1);
/*     */     } 
/*     */   }
/*     */   boolean isDialog() {
/* 178 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean isWindowResizable();
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Rectangle getWindowBounds();
/*     */ 
/*     */   
/*     */   protected abstract void setWindowBounds(Rectangle paramRectangle);
/*     */ 
/*     */   
/*     */   public void propertyChange(PropertyChangeEvent e) {
/* 194 */     switch (e.getPropertyName()) {
/*     */       case "ancestor":
/* 196 */         if (e.getNewValue() != null) {
/* 197 */           addNotify(); break;
/*     */         } 
/* 199 */         removeNotify();
/*     */         break;
/*     */       
/*     */       case "resizable":
/* 203 */         updateVisibility();
/*     */         break;
/*     */     } 
/*     */   }
/*     */   protected abstract boolean honorMinimumSizeOnResize();
/*     */   protected abstract Dimension getWindowMinimumSize();
/*     */   protected void beginResizing(int direction) {}
/*     */   protected void endResizing() {}
/*     */   public void componentResized(ComponentEvent e) {
/* 212 */     doLayout();
/*     */   }
/*     */ 
/*     */   
/*     */   public void componentMoved(ComponentEvent e) {}
/*     */ 
/*     */   
/*     */   public void componentShown(ComponentEvent e) {}
/*     */ 
/*     */   
/*     */   public void componentHidden(ComponentEvent e) {}
/*     */   
/*     */   public static class WindowResizer
/*     */     extends FlatWindowResizer
/*     */     implements WindowStateListener
/*     */   {
/*     */     protected Window window;
/*     */     
/*     */     public WindowResizer(JRootPane rootPane) {
/* 231 */       super(rootPane);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addNotify() {
/* 236 */       Container parent = this.resizeComp.getParent();
/* 237 */       this.window = (parent instanceof Window) ? (Window)parent : null;
/* 238 */       if (this.window instanceof Frame) {
/* 239 */         this.window.addPropertyChangeListener("resizable", this);
/* 240 */         this.window.addWindowStateListener(this);
/*     */       } 
/*     */       
/* 243 */       super.addNotify();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void removeNotify() {
/* 248 */       if (this.window instanceof Frame) {
/* 249 */         this.window.removePropertyChangeListener("resizable", this);
/* 250 */         this.window.removeWindowStateListener(this);
/*     */       } 
/* 252 */       this.window = null;
/*     */       
/* 254 */       super.removeNotify();
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isWindowResizable() {
/* 259 */       if (FlatUIUtils.isFullScreen(this.resizeComp))
/* 260 */         return false; 
/* 261 */       if (this.window instanceof Frame)
/* 262 */         return (((Frame)this.window).isResizable() && (((Frame)this.window).getExtendedState() & 0x6) == 0); 
/* 263 */       if (this.window instanceof Dialog)
/* 264 */         return ((Dialog)this.window).isResizable(); 
/* 265 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Rectangle getWindowBounds() {
/* 270 */       return this.window.getBounds();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void setWindowBounds(Rectangle r) {
/* 275 */       this.window.setBounds(r);
/*     */ 
/*     */       
/* 278 */       doLayout();
/*     */       
/* 280 */       if (Toolkit.getDefaultToolkit().isDynamicLayoutActive()) {
/* 281 */         this.window.validate();
/* 282 */         this.resizeComp.repaint();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean honorMinimumSizeOnResize() {
/* 288 */       return ((this.honorFrameMinimumSizeOnResize && this.window instanceof Frame) || (this.honorDialogMinimumSizeOnResize && this.window instanceof Dialog));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Dimension getWindowMinimumSize() {
/* 295 */       return this.window.getMinimumSize();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isDialog() {
/* 300 */       return this.window instanceof Dialog;
/*     */     }
/*     */ 
/*     */     
/*     */     public void windowStateChanged(WindowEvent e) {
/* 305 */       updateVisibility();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class InternalFrameResizer
/*     */     extends FlatWindowResizer
/*     */   {
/*     */     protected final Supplier<DesktopManager> desktopManager;
/*     */ 
/*     */ 
/*     */     
/*     */     public InternalFrameResizer(JInternalFrame frame, Supplier<DesktopManager> desktopManager) {
/* 320 */       super(frame);
/* 321 */       this.desktopManager = desktopManager;
/*     */       
/* 323 */       frame.addPropertyChangeListener("resizable", this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void uninstall() {
/* 328 */       getFrame().removePropertyChangeListener("resizable", this);
/*     */       
/* 330 */       super.uninstall();
/*     */     }
/*     */     
/*     */     private JInternalFrame getFrame() {
/* 334 */       return (JInternalFrame)this.resizeComp;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Insets getResizeInsets() {
/* 339 */       return getFrame().getInsets();
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isWindowResizable() {
/* 344 */       return getFrame().isResizable();
/*     */     }
/*     */ 
/*     */     
/*     */     protected Rectangle getWindowBounds() {
/* 349 */       return getFrame().getBounds();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void setWindowBounds(Rectangle r) {
/* 354 */       ((DesktopManager)this.desktopManager.get()).resizeFrame(getFrame(), r.x, r.y, r.width, r.height);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean honorMinimumSizeOnResize() {
/* 359 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Dimension getWindowMinimumSize() {
/* 364 */       return getFrame().getMinimumSize();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void beginResizing(int direction) {
/* 369 */       ((DesktopManager)this.desktopManager.get()).beginResizingFrame(getFrame(), direction);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void endResizing() {
/* 374 */       ((DesktopManager)this.desktopManager.get()).endResizingFrame(getFrame());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected class DragBorderComponent
/*     */     extends JComponent
/*     */     implements MouseListener, MouseMotionListener
/*     */   {
/*     */     private final int leadingResizeDir;
/*     */     
/*     */     private final int centerResizeDir;
/*     */     
/*     */     private final int trailingResizeDir;
/* 388 */     private int resizeDir = -1;
/*     */     
/*     */     private int leadingCornerDragWidth;
/*     */     
/*     */     private int trailingCornerDragWidth;
/*     */     
/*     */     private int dragLeftOffset;
/*     */     private int dragRightOffset;
/*     */     private int dragTopOffset;
/*     */     private int dragBottomOffset;
/*     */     
/*     */     protected DragBorderComponent(int leadingResizeDir, int centerResizeDir, int trailingResizeDir) {
/* 400 */       this.leadingResizeDir = leadingResizeDir;
/* 401 */       this.centerResizeDir = centerResizeDir;
/* 402 */       this.trailingResizeDir = trailingResizeDir;
/*     */       
/* 404 */       setResizeDir(centerResizeDir);
/* 405 */       setVisible(false);
/*     */       
/* 407 */       addMouseListener(this);
/* 408 */       addMouseMotionListener(this);
/*     */     }
/*     */     
/*     */     void setCornerDragWidths(int leading, int trailing) {
/* 412 */       this.leadingCornerDragWidth = leading;
/* 413 */       this.trailingCornerDragWidth = trailing;
/*     */     }
/*     */     
/*     */     protected void setResizeDir(int resizeDir) {
/* 417 */       if (this.resizeDir == resizeDir)
/*     */         return; 
/* 419 */       this.resizeDir = resizeDir;
/*     */       
/* 421 */       setCursor(Cursor.getPredefinedCursor(resizeDir));
/*     */     }
/*     */ 
/*     */     
/*     */     public Dimension getPreferredSize() {
/* 426 */       int thickness = UIScale.scale(FlatWindowResizer.this.borderDragThickness);
/* 427 */       return new Dimension(thickness, thickness);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void paintComponent(Graphics g) {
/* 432 */       paintChildren(g);
/*     */ 
/*     */ 
/*     */       
/* 436 */       FlatWindowResizer.this.updateVisibility();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void mouseClicked(MouseEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void mousePressed(MouseEvent e) {
/* 463 */       if (!FlatWindowResizer.this.isWindowResizable()) {
/*     */         return;
/*     */       }
/* 466 */       int xOnScreen = e.getXOnScreen();
/* 467 */       int yOnScreen = e.getYOnScreen();
/* 468 */       Rectangle windowBounds = FlatWindowResizer.this.getWindowBounds();
/*     */ 
/*     */       
/* 471 */       this.dragLeftOffset = xOnScreen - windowBounds.x;
/* 472 */       this.dragTopOffset = yOnScreen - windowBounds.y;
/* 473 */       this.dragRightOffset = windowBounds.x + windowBounds.width - xOnScreen;
/* 474 */       this.dragBottomOffset = windowBounds.y + windowBounds.height - yOnScreen;
/*     */       
/* 476 */       int direction = 0;
/* 477 */       switch (this.resizeDir) { case 8:
/* 478 */           direction = 1; break;
/* 479 */         case 9: direction = 5; break;
/* 480 */         case 10: direction = 7; break;
/* 481 */         case 11: direction = 3; break;
/* 482 */         case 6: direction = 8; break;
/* 483 */         case 7: direction = 2; break;
/* 484 */         case 4: direction = 6; break;
/* 485 */         case 5: direction = 4; break; }
/*     */       
/* 487 */       FlatWindowResizer.this.beginResizing(direction);
/*     */     }
/*     */ 
/*     */     
/*     */     public void mouseReleased(MouseEvent e) {
/* 492 */       if (!FlatWindowResizer.this.isWindowResizable()) {
/*     */         return;
/*     */       }
/* 495 */       this.dragLeftOffset = this.dragRightOffset = this.dragTopOffset = this.dragBottomOffset = 0;
/*     */       
/* 497 */       FlatWindowResizer.this.endResizing();
/*     */     }
/*     */     
/*     */     public void mouseEntered(MouseEvent e) {}
/*     */     
/*     */     public void mouseExited(MouseEvent e) {}
/*     */     
/*     */     public void mouseMoved(MouseEvent e) {
/* 505 */       boolean topOrBottom = (this.centerResizeDir == 8 || this.centerResizeDir == 9);
/* 506 */       int xy = topOrBottom ? e.getX() : e.getY();
/* 507 */       int wh = topOrBottom ? getWidth() : getHeight();
/*     */       
/* 509 */       setResizeDir((xy <= this.leadingCornerDragWidth) ? this.leadingResizeDir : ((xy >= wh - this.trailingCornerDragWidth) ? this.trailingResizeDir : this.centerResizeDir));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void mouseDragged(MouseEvent e) {
/* 518 */       if (!FlatWindowResizer.this.isWindowResizable()) {
/*     */         return;
/*     */       }
/* 521 */       int xOnScreen = e.getXOnScreen();
/* 522 */       int yOnScreen = e.getYOnScreen();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 530 */       Rectangle oldBounds = FlatWindowResizer.this.getWindowBounds();
/* 531 */       Rectangle newBounds = new Rectangle(oldBounds);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 536 */       if (this.resizeDir == 8 || this.resizeDir == 6 || this.resizeDir == 7) {
/* 537 */         newBounds.y = yOnScreen - this.dragTopOffset;
/* 538 */         newBounds.height += oldBounds.y - newBounds.y;
/*     */       } 
/*     */ 
/*     */       
/* 542 */       if (this.resizeDir == 9 || this.resizeDir == 4 || this.resizeDir == 5) {
/* 543 */         newBounds.height = yOnScreen + this.dragBottomOffset - newBounds.y;
/*     */       }
/*     */       
/* 546 */       if (this.resizeDir == 10 || this.resizeDir == 6 || this.resizeDir == 4) {
/* 547 */         newBounds.x = xOnScreen - this.dragLeftOffset;
/* 548 */         newBounds.width += oldBounds.x - newBounds.x;
/*     */       } 
/*     */ 
/*     */       
/* 552 */       if (this.resizeDir == 11 || this.resizeDir == 7 || this.resizeDir == 5) {
/* 553 */         newBounds.width = xOnScreen + this.dragRightOffset - newBounds.x;
/*     */       }
/*     */       
/* 556 */       Dimension minimumSize = FlatWindowResizer.this.honorMinimumSizeOnResize() ? FlatWindowResizer.this.getWindowMinimumSize() : null;
/* 557 */       if (minimumSize == null)
/* 558 */         minimumSize = UIScale.scale(new Dimension(150, 50)); 
/* 559 */       if (newBounds.width < minimumSize.width) {
/* 560 */         if (newBounds.x != oldBounds.x)
/* 561 */           newBounds.x -= minimumSize.width - newBounds.width; 
/* 562 */         newBounds.width = minimumSize.width;
/*     */       } 
/* 564 */       if (newBounds.height < minimumSize.height) {
/* 565 */         if (newBounds.y != oldBounds.y)
/* 566 */           newBounds.y -= minimumSize.height - newBounds.height; 
/* 567 */         newBounds.height = minimumSize.height;
/*     */       } 
/*     */ 
/*     */       
/* 571 */       if (!newBounds.equals(oldBounds))
/* 572 */         FlatWindowResizer.this.setWindowBounds(newBounds); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatWindowResizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */