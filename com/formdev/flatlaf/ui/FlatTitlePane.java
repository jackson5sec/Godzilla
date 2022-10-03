/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.FlatClientProperties;
/*     */ import com.formdev.flatlaf.FlatSystemProperties;
/*     */ import com.formdev.flatlaf.util.ScaledImageIcon;
/*     */ import com.formdev.flatlaf.util.SystemInfo;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GraphicsConfiguration;
/*     */ import java.awt.Image;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.ComponentListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.AbstractBorder;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatTitlePane
/*     */   extends JComponent
/*     */ {
/*  96 */   protected final Color activeBackground = UIManager.getColor("TitlePane.background");
/*  97 */   protected final Color inactiveBackground = UIManager.getColor("TitlePane.inactiveBackground");
/*  98 */   protected final Color activeForeground = UIManager.getColor("TitlePane.foreground");
/*  99 */   protected final Color inactiveForeground = UIManager.getColor("TitlePane.inactiveForeground");
/* 100 */   protected final Color embeddedForeground = UIManager.getColor("TitlePane.embeddedForeground");
/* 101 */   protected final Color borderColor = UIManager.getColor("TitlePane.borderColor");
/*     */   
/* 103 */   protected final Insets menuBarMargins = UIManager.getInsets("TitlePane.menuBarMargins");
/* 104 */   protected final Dimension iconSize = UIManager.getDimension("TitlePane.iconSize");
/* 105 */   protected final int buttonMaximizedHeight = UIManager.getInt("TitlePane.buttonMaximizedHeight");
/*     */   
/*     */   protected final JRootPane rootPane;
/*     */   
/*     */   protected JPanel leftPanel;
/*     */   
/*     */   protected JLabel iconLabel;
/*     */   
/*     */   protected JComponent menuBarPlaceholder;
/*     */   protected JLabel titleLabel;
/*     */   protected JPanel buttonPanel;
/*     */   protected JButton iconifyButton;
/*     */   protected JButton maximizeButton;
/*     */   protected JButton restoreButton;
/*     */   protected JButton closeButton;
/*     */   protected Window window;
/*     */   private final Handler handler;
/*     */   
/*     */   public FlatTitlePane(JRootPane rootPane) {
/* 124 */     this.rootPane = rootPane;
/*     */     
/* 126 */     this.handler = createHandler();
/* 127 */     setBorder(createTitlePaneBorder());
/*     */     
/* 129 */     addSubComponents();
/* 130 */     activeChanged(true);
/*     */     
/* 132 */     addMouseListener(this.handler);
/* 133 */     addMouseMotionListener(this.handler);
/*     */ 
/*     */     
/* 136 */     this.iconLabel.addMouseListener(this.handler);
/*     */   }
/*     */   
/*     */   protected FlatTitlePaneBorder createTitlePaneBorder() {
/* 140 */     return new FlatTitlePaneBorder();
/*     */   }
/*     */   
/*     */   protected Handler createHandler() {
/* 144 */     return new Handler();
/*     */   }
/*     */   
/*     */   protected void addSubComponents() {
/* 148 */     this.leftPanel = new JPanel();
/* 149 */     this.iconLabel = new JLabel();
/* 150 */     this.titleLabel = new JLabel();
/* 151 */     this.iconLabel.setBorder(new FlatEmptyBorder(UIManager.getInsets("TitlePane.iconMargins")));
/* 152 */     this.titleLabel.setBorder(new FlatEmptyBorder(UIManager.getInsets("TitlePane.titleMargins")));
/*     */     
/* 154 */     this.leftPanel.setLayout(new BoxLayout(this.leftPanel, 2));
/* 155 */     this.leftPanel.setOpaque(false);
/* 156 */     this.leftPanel.add(this.iconLabel);
/*     */     
/* 158 */     this.menuBarPlaceholder = new JComponent()
/*     */       {
/*     */         public Dimension getPreferredSize() {
/* 161 */           JMenuBar menuBar = FlatTitlePane.this.rootPane.getJMenuBar();
/* 162 */           return (menuBar != null && menuBar.isVisible() && FlatTitlePane.this.isMenuBarEmbedded()) ? 
/* 163 */             FlatUIUtils.addInsets(menuBar.getPreferredSize(), UIScale.scale(FlatTitlePane.this.menuBarMargins)) : new Dimension();
/*     */         }
/*     */       };
/*     */     
/* 167 */     this.leftPanel.add(this.menuBarPlaceholder);
/*     */     
/* 169 */     createButtons();
/*     */     
/* 171 */     setLayout(new BorderLayout()
/*     */         {
/*     */           public void layoutContainer(Container target) {
/* 174 */             super.layoutContainer(target);
/*     */ 
/*     */ 
/*     */             
/* 178 */             Insets insets = target.getInsets();
/* 179 */             int width = target.getWidth() - insets.left - insets.right;
/* 180 */             if (FlatTitlePane.this.leftPanel.getWidth() + FlatTitlePane.this.buttonPanel.getWidth() > width) {
/* 181 */               int oldWidth = FlatTitlePane.this.leftPanel.getWidth();
/* 182 */               int newWidth = Math.max(width - FlatTitlePane.this.buttonPanel.getWidth(), 0);
/* 183 */               FlatTitlePane.this.leftPanel.setSize(newWidth, FlatTitlePane.this.leftPanel.getHeight());
/* 184 */               if (!FlatTitlePane.this.getComponentOrientation().isLeftToRight()) {
/* 185 */                 FlatTitlePane.this.leftPanel.setLocation(FlatTitlePane.this.leftPanel.getX() + oldWidth - newWidth, FlatTitlePane.this.leftPanel.getY());
/*     */               }
/*     */             } 
/*     */           }
/*     */         });
/* 190 */     add(this.leftPanel, "Before");
/* 191 */     add(this.titleLabel, "Center");
/* 192 */     add(this.buttonPanel, "After");
/*     */   }
/*     */   
/*     */   protected void createButtons() {
/* 196 */     this.iconifyButton = createButton("TitlePane.iconifyIcon", "Iconify", e -> iconify());
/* 197 */     this.maximizeButton = createButton("TitlePane.maximizeIcon", "Maximize", e -> maximize());
/* 198 */     this.restoreButton = createButton("TitlePane.restoreIcon", "Restore", e -> restore());
/* 199 */     this.closeButton = createButton("TitlePane.closeIcon", "Close", e -> close());
/*     */     
/* 201 */     this.buttonPanel = new JPanel()
/*     */       {
/*     */         public Dimension getPreferredSize() {
/* 204 */           Dimension size = super.getPreferredSize();
/* 205 */           if (FlatTitlePane.this.buttonMaximizedHeight > 0 && FlatTitlePane.this.window instanceof Frame && (((Frame)FlatTitlePane.this.window)
/*     */             
/* 207 */             .getExtendedState() & 0x6) != 0)
/*     */           {
/*     */             
/* 210 */             size = new Dimension(size.width, Math.min(size.height, UIScale.scale(FlatTitlePane.this.buttonMaximizedHeight)));
/*     */           }
/* 212 */           return size;
/*     */         }
/*     */       };
/* 215 */     this.buttonPanel.setOpaque(false);
/* 216 */     this.buttonPanel.setLayout(new BoxLayout(this.buttonPanel, 2));
/* 217 */     if (this.rootPane.getWindowDecorationStyle() == 1) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 223 */       this.restoreButton.setVisible(false);
/*     */       
/* 225 */       this.buttonPanel.add(this.iconifyButton);
/* 226 */       this.buttonPanel.add(this.maximizeButton);
/* 227 */       this.buttonPanel.add(this.restoreButton);
/*     */     } 
/* 229 */     this.buttonPanel.add(this.closeButton);
/*     */   }
/*     */   
/*     */   protected JButton createButton(String iconKey, String accessibleName, ActionListener action) {
/* 233 */     JButton button = new JButton(UIManager.getIcon(iconKey));
/* 234 */     button.setFocusable(false);
/* 235 */     button.setContentAreaFilled(false);
/* 236 */     button.setBorder(BorderFactory.createEmptyBorder());
/* 237 */     button.putClientProperty("AccessibleName", accessibleName);
/* 238 */     button.addActionListener(action);
/* 239 */     return button;
/*     */   }
/*     */   
/*     */   protected void activeChanged(boolean active) {
/* 243 */     boolean hasEmbeddedMenuBar = (this.rootPane.getJMenuBar() != null && this.rootPane.getJMenuBar().isVisible() && isMenuBarEmbedded());
/* 244 */     Color background = FlatUIUtils.nonUIResource(active ? this.activeBackground : this.inactiveBackground);
/* 245 */     Color foreground = FlatUIUtils.nonUIResource(active ? this.activeForeground : this.inactiveForeground);
/* 246 */     Color titleForeground = (hasEmbeddedMenuBar && active) ? FlatUIUtils.nonUIResource(this.embeddedForeground) : foreground;
/*     */     
/* 248 */     setBackground(background);
/* 249 */     this.titleLabel.setForeground(titleForeground);
/* 250 */     this.iconifyButton.setForeground(foreground);
/* 251 */     this.maximizeButton.setForeground(foreground);
/* 252 */     this.restoreButton.setForeground(foreground);
/* 253 */     this.closeButton.setForeground(foreground);
/*     */     
/* 255 */     this.titleLabel.setHorizontalAlignment(hasEmbeddedMenuBar ? 0 : 10);
/*     */ 
/*     */     
/* 258 */     this.iconifyButton.setBackground(background);
/* 259 */     this.maximizeButton.setBackground(background);
/* 260 */     this.restoreButton.setBackground(background);
/* 261 */     this.closeButton.setBackground(background);
/*     */   }
/*     */   
/*     */   protected void frameStateChanged() {
/* 265 */     if (this.window == null || this.rootPane.getWindowDecorationStyle() != 1) {
/*     */       return;
/*     */     }
/* 268 */     if (this.window instanceof Frame) {
/* 269 */       Frame frame = (Frame)this.window;
/* 270 */       boolean resizable = frame.isResizable();
/* 271 */       boolean maximized = ((frame.getExtendedState() & 0x6) != 0);
/*     */       
/* 273 */       this.iconifyButton.setVisible(true);
/* 274 */       this.maximizeButton.setVisible((resizable && !maximized));
/* 275 */       this.restoreButton.setVisible((resizable && maximized));
/*     */       
/* 277 */       if (maximized && this.rootPane
/* 278 */         .getClientProperty("_flatlaf.maximizedBoundsUpToDate") == null) {
/*     */         
/* 280 */         this.rootPane.putClientProperty("_flatlaf.maximizedBoundsUpToDate", (Object)null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 287 */         Rectangle oldMaximizedBounds = frame.getMaximizedBounds();
/* 288 */         updateMaximizedBounds();
/* 289 */         Rectangle newMaximizedBounds = frame.getMaximizedBounds();
/* 290 */         if (newMaximizedBounds != null && !newMaximizedBounds.equals(oldMaximizedBounds)) {
/* 291 */           int oldExtendedState = frame.getExtendedState();
/* 292 */           frame.setExtendedState(oldExtendedState & 0xFFFFFFF9);
/* 293 */           frame.setExtendedState(oldExtendedState);
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 298 */       this.iconifyButton.setVisible(false);
/* 299 */       this.maximizeButton.setVisible(false);
/* 300 */       this.restoreButton.setVisible(false);
/*     */       
/* 302 */       revalidate();
/* 303 */       repaint();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateIcon() {
/* 309 */     List<Image> images = this.window.getIconImages();
/* 310 */     if (images.isEmpty())
/*     */     {
/* 312 */       for (Window owner = this.window.getOwner(); owner != null; owner = owner.getOwner()) {
/* 313 */         images = owner.getIconImages();
/* 314 */         if (!images.isEmpty()) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     }
/* 319 */     boolean hasIcon = true;
/*     */ 
/*     */     
/* 322 */     if (!images.isEmpty()) {
/* 323 */       this.iconLabel.setIcon(FlatTitlePaneIcon.create(images, this.iconSize));
/*     */     } else {
/*     */       
/* 326 */       Icon defaultIcon = UIManager.getIcon("InternalFrame.icon");
/* 327 */       if (defaultIcon != null && (defaultIcon.getIconWidth() == 0 || defaultIcon.getIconHeight() == 0))
/* 328 */         defaultIcon = null; 
/* 329 */       if (defaultIcon != null) {
/* 330 */         ScaledImageIcon scaledImageIcon; if (defaultIcon instanceof ImageIcon)
/* 331 */           scaledImageIcon = new ScaledImageIcon((ImageIcon)defaultIcon, this.iconSize.width, this.iconSize.height); 
/* 332 */         this.iconLabel.setIcon((Icon)scaledImageIcon);
/*     */       } else {
/* 334 */         hasIcon = false;
/*     */       } 
/*     */     } 
/*     */     
/* 338 */     this.iconLabel.setVisible(hasIcon);
/*     */     
/* 340 */     updateJBRHitTestSpotsAndTitleBarHeightLater();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addNotify() {
/* 345 */     super.addNotify();
/*     */     
/* 347 */     uninstallWindowListeners();
/*     */     
/* 349 */     this.window = SwingUtilities.getWindowAncestor(this);
/* 350 */     if (this.window != null) {
/* 351 */       frameStateChanged();
/* 352 */       activeChanged(this.window.isActive());
/* 353 */       updateIcon();
/* 354 */       this.titleLabel.setText(getWindowTitle());
/* 355 */       installWindowListeners();
/*     */     } 
/*     */     
/* 358 */     updateJBRHitTestSpotsAndTitleBarHeightLater();
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeNotify() {
/* 363 */     super.removeNotify();
/*     */     
/* 365 */     uninstallWindowListeners();
/* 366 */     this.window = null;
/*     */   }
/*     */   
/*     */   protected String getWindowTitle() {
/* 370 */     if (this.window instanceof Frame)
/* 371 */       return ((Frame)this.window).getTitle(); 
/* 372 */     if (this.window instanceof Dialog)
/* 373 */       return ((Dialog)this.window).getTitle(); 
/* 374 */     return null;
/*     */   }
/*     */   
/*     */   protected void installWindowListeners() {
/* 378 */     if (this.window == null) {
/*     */       return;
/*     */     }
/* 381 */     this.window.addPropertyChangeListener(this.handler);
/* 382 */     this.window.addWindowListener(this.handler);
/* 383 */     this.window.addWindowStateListener(this.handler);
/* 384 */     this.window.addComponentListener(this.handler);
/*     */   }
/*     */   
/*     */   protected void uninstallWindowListeners() {
/* 388 */     if (this.window == null) {
/*     */       return;
/*     */     }
/* 391 */     this.window.removePropertyChangeListener(this.handler);
/* 392 */     this.window.removeWindowListener(this.handler);
/* 393 */     this.window.removeWindowStateListener(this.handler);
/* 394 */     this.window.removeComponentListener(this.handler);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isMenuBarEmbedded() {
/* 399 */     return (UIManager.getBoolean("TitlePane.menuBarEmbedded") && 
/* 400 */       FlatClientProperties.clientPropertyBoolean(this.rootPane, "JRootPane.menuBarEmbedded", true) && 
/* 401 */       FlatSystemProperties.getBoolean("flatlaf.menuBarEmbedded", true));
/*     */   }
/*     */   
/*     */   protected Rectangle getMenuBarBounds() {
/* 405 */     Insets insets = this.rootPane.getInsets();
/*     */ 
/*     */     
/* 408 */     Rectangle bounds = new Rectangle(SwingUtilities.convertPoint(this.menuBarPlaceholder, -insets.left, -insets.top, this.rootPane), this.menuBarPlaceholder.getSize());
/*     */ 
/*     */ 
/*     */     
/* 412 */     Insets borderInsets = getBorder().getBorderInsets(this);
/* 413 */     bounds.height += borderInsets.bottom;
/*     */     
/* 415 */     return FlatUIUtils.subtractInsets(bounds, UIScale.scale(getMenuBarMargins()));
/*     */   }
/*     */   
/*     */   protected Insets getMenuBarMargins() {
/* 419 */     return getComponentOrientation().isLeftToRight() ? this.menuBarMargins : new Insets(this.menuBarMargins.top, this.menuBarMargins.right, this.menuBarMargins.bottom, this.menuBarMargins.left);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void menuBarChanged() {
/* 425 */     this.menuBarPlaceholder.invalidate();
/*     */ 
/*     */ 
/*     */     
/* 429 */     repaint();
/*     */ 
/*     */     
/* 432 */     EventQueue.invokeLater(() -> activeChanged(
/* 433 */           (this.window == null || this.window.isActive())));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void menuBarLayouted() {
/* 438 */     updateJBRHitTestSpotsAndTitleBarHeightLater();
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
/*     */   protected void paintComponent(Graphics g) {
/* 460 */     g.setColor(getBackground());
/* 461 */     g.fillRect(0, 0, getWidth(), getHeight());
/*     */   }
/*     */   
/*     */   protected void repaintWindowBorder() {
/* 465 */     int width = this.rootPane.getWidth();
/* 466 */     int height = this.rootPane.getHeight();
/* 467 */     Insets insets = this.rootPane.getInsets();
/* 468 */     this.rootPane.repaint(0, 0, width, insets.top);
/* 469 */     this.rootPane.repaint(0, 0, insets.left, height);
/* 470 */     this.rootPane.repaint(0, height - insets.bottom, width, insets.bottom);
/* 471 */     this.rootPane.repaint(width - insets.right, 0, insets.right, height);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void iconify() {
/* 478 */     if (this.window instanceof Frame) {
/* 479 */       Frame frame = (Frame)this.window;
/* 480 */       frame.setExtendedState(frame.getExtendedState() | 0x1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void maximize() {
/* 488 */     if (!(this.window instanceof Frame)) {
/*     */       return;
/*     */     }
/* 491 */     Frame frame = (Frame)this.window;
/*     */     
/* 493 */     updateMaximizedBounds();
/*     */ 
/*     */     
/* 496 */     this.rootPane.putClientProperty("_flatlaf.maximizedBoundsUpToDate", Boolean.valueOf(true));
/*     */ 
/*     */     
/* 499 */     frame.setExtendedState(frame.getExtendedState() | 0x6);
/*     */   }
/*     */   
/*     */   protected void updateMaximizedBounds() {
/* 503 */     Frame frame = (Frame)this.window;
/*     */ 
/*     */ 
/*     */     
/* 507 */     Rectangle oldMaximizedBounds = frame.getMaximizedBounds();
/* 508 */     if (!hasJBRCustomDecoration() && (oldMaximizedBounds == null || 
/*     */       
/* 510 */       Objects.equals(oldMaximizedBounds, this.rootPane.getClientProperty("_flatlaf.maximizedBounds")))) {
/*     */       
/* 512 */       GraphicsConfiguration gc = this.window.getGraphicsConfiguration();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 524 */       Rectangle screenBounds = gc.getBounds();
/*     */       
/* 526 */       int maximizedX = screenBounds.x;
/* 527 */       int maximizedY = screenBounds.y;
/* 528 */       int maximizedWidth = screenBounds.width;
/* 529 */       int maximizedHeight = screenBounds.height;
/*     */       
/* 531 */       if (!isMaximizedBoundsFixed()) {
/*     */         
/* 533 */         maximizedX = 0;
/* 534 */         maximizedY = 0;
/*     */ 
/*     */         
/* 537 */         AffineTransform defaultTransform = gc.getDefaultTransform();
/* 538 */         maximizedWidth = (int)(maximizedWidth * defaultTransform.getScaleX());
/* 539 */         maximizedHeight = (int)(maximizedHeight * defaultTransform.getScaleY());
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 545 */       Insets screenInsets = this.window.getToolkit().getScreenInsets(gc);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 551 */       Rectangle newMaximizedBounds = new Rectangle(maximizedX + screenInsets.left, maximizedY + screenInsets.top, maximizedWidth - screenInsets.left - screenInsets.right, maximizedHeight - screenInsets.top - screenInsets.bottom);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 557 */       if (!Objects.equals(oldMaximizedBounds, newMaximizedBounds)) {
/*     */         
/* 559 */         frame.setMaximizedBounds(newMaximizedBounds);
/*     */ 
/*     */ 
/*     */         
/* 563 */         this.rootPane.putClientProperty("_flatlaf.maximizedBounds", newMaximizedBounds);
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
/*     */   private boolean isMaximizedBoundsFixed() {
/* 576 */     return (SystemInfo.isJava_15_orLater || (SystemInfo.javaVersion >= 
/* 577 */       SystemInfo.toVersion(11, 0, 8, 0) && SystemInfo.javaVersion < 
/* 578 */       SystemInfo.toVersion(12, 0, 0, 0)) || (SystemInfo.javaVersion >= 
/* 579 */       SystemInfo.toVersion(13, 0, 4, 0) && SystemInfo.javaVersion < 
/* 580 */       SystemInfo.toVersion(14, 0, 0, 0)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void restore() {
/* 587 */     if (this.window instanceof Frame) {
/* 588 */       Frame frame = (Frame)this.window;
/* 589 */       int state = frame.getExtendedState();
/* 590 */       frame.setExtendedState(((state & 0x1) != 0) ? (state & 0xFFFFFFFE) : (state & 0xFFFFFFF9));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void close() {
/* 600 */     if (this.window != null)
/* 601 */       this.window.dispatchEvent(new WindowEvent(this.window, 201)); 
/*     */   }
/*     */   
/*     */   protected boolean hasJBRCustomDecoration() {
/* 605 */     return (FlatRootPaneUI.canUseJBRCustomDecorations && this.window != null && 
/*     */       
/* 607 */       JBRCustomDecorations.hasCustomDecoration(this.window));
/*     */   }
/*     */   
/*     */   protected void updateJBRHitTestSpotsAndTitleBarHeightLater() {
/* 611 */     EventQueue.invokeLater(() -> updateJBRHitTestSpotsAndTitleBarHeight());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateJBRHitTestSpotsAndTitleBarHeight() {
/* 617 */     if (!isDisplayable()) {
/*     */       return;
/*     */     }
/* 620 */     if (!hasJBRCustomDecoration()) {
/*     */       return;
/*     */     }
/* 623 */     List<Rectangle> hitTestSpots = new ArrayList<>();
/* 624 */     if (this.iconLabel.isVisible())
/* 625 */       addJBRHitTestSpot(this.iconLabel, false, hitTestSpots); 
/* 626 */     addJBRHitTestSpot(this.buttonPanel, false, hitTestSpots);
/* 627 */     addJBRHitTestSpot(this.menuBarPlaceholder, true, hitTestSpots);
/*     */     
/* 629 */     int titleBarHeight = getHeight();
/*     */     
/* 631 */     if (titleBarHeight > 0) {
/* 632 */       titleBarHeight--;
/*     */     }
/* 634 */     JBRCustomDecorations.setHitTestSpotsAndTitleBarHeight(this.window, hitTestSpots, titleBarHeight);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addJBRHitTestSpot(JComponent c, boolean subtractMenuBarMargins, List<Rectangle> hitTestSpots) {
/* 644 */     Dimension size = c.getSize();
/* 645 */     if (size.width <= 0 || size.height <= 0) {
/*     */       return;
/*     */     }
/* 648 */     Point location = SwingUtilities.convertPoint(c, 0, 0, this.window);
/* 649 */     Rectangle r = new Rectangle(location, size);
/* 650 */     if (subtractMenuBarMargins) {
/* 651 */       r = FlatUIUtils.subtractInsets(r, UIScale.scale(getMenuBarMargins()));
/*     */     }
/* 653 */     r.grow(2, 2);
/* 654 */     hitTestSpots.add(r);
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
/*     */   protected class FlatTitlePaneBorder
/*     */     extends AbstractBorder
/*     */   {
/*     */     public Insets getBorderInsets(Component c, Insets insets) {
/* 669 */       super.getBorderInsets(c, insets);
/*     */       
/* 671 */       Border menuBarBorder = getMenuBarBorder();
/* 672 */       if (menuBarBorder != null) {
/*     */         
/* 674 */         Insets menuBarInsets = menuBarBorder.getBorderInsets(c);
/* 675 */         insets.bottom += menuBarInsets.bottom;
/* 676 */       } else if (FlatTitlePane.this.borderColor != null && (FlatTitlePane.this.rootPane.getJMenuBar() == null || !FlatTitlePane.this.rootPane.getJMenuBar().isVisible())) {
/* 677 */         insets.bottom += UIScale.scale(1);
/*     */       } 
/* 679 */       if (FlatTitlePane.this.hasJBRCustomDecoration()) {
/* 680 */         insets = FlatUIUtils.addInsets(insets, JBRCustomDecorations.JBRWindowTopBorder.getInstance().getBorderInsets());
/*     */       }
/* 682 */       return insets;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/* 688 */       Border menuBarBorder = getMenuBarBorder();
/* 689 */       if (menuBarBorder != null) {
/*     */         
/* 691 */         menuBarBorder.paintBorder(c, g, x, y, width, height);
/* 692 */       } else if (FlatTitlePane.this.borderColor != null && (FlatTitlePane.this.rootPane.getJMenuBar() == null || !FlatTitlePane.this.rootPane.getJMenuBar().isVisible())) {
/*     */         
/* 694 */         float lineHeight = UIScale.scale(1.0F);
/* 695 */         FlatUIUtils.paintFilledRectangle(g, FlatTitlePane.this.borderColor, x, (y + height) - lineHeight, width, lineHeight);
/*     */       } 
/*     */       
/* 698 */       if (FlatTitlePane.this.hasJBRCustomDecoration())
/* 699 */         JBRCustomDecorations.JBRWindowTopBorder.getInstance().paintBorder(c, g, x, y, width, height); 
/*     */     }
/*     */     
/*     */     protected Border getMenuBarBorder() {
/* 703 */       JMenuBar menuBar = FlatTitlePane.this.rootPane.getJMenuBar();
/* 704 */       return (menuBar != null && menuBar.isVisible() && FlatTitlePane.this.isMenuBarEmbedded()) ? menuBar.getBorder() : null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected class Handler
/*     */     extends WindowAdapter
/*     */     implements PropertyChangeListener, MouseListener, MouseMotionListener, ComponentListener
/*     */   {
/*     */     private Point dragOffset;
/*     */ 
/*     */     
/*     */     public void propertyChange(PropertyChangeEvent e) {
/* 718 */       switch (e.getPropertyName()) {
/*     */         case "title":
/* 720 */           FlatTitlePane.this.titleLabel.setText(FlatTitlePane.this.getWindowTitle());
/*     */           break;
/*     */         
/*     */         case "resizable":
/* 724 */           if (FlatTitlePane.this.window instanceof Frame) {
/* 725 */             FlatTitlePane.this.frameStateChanged();
/*     */           }
/*     */           break;
/*     */         case "iconImage":
/* 729 */           FlatTitlePane.this.updateIcon();
/*     */           break;
/*     */         
/*     */         case "componentOrientation":
/* 733 */           FlatTitlePane.this.updateJBRHitTestSpotsAndTitleBarHeightLater();
/*     */           break;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void windowActivated(WindowEvent e) {
/* 742 */       FlatTitlePane.this.activeChanged(true);
/* 743 */       FlatTitlePane.this.updateJBRHitTestSpotsAndTitleBarHeight();
/*     */       
/* 745 */       if (FlatTitlePane.this.hasJBRCustomDecoration()) {
/* 746 */         JBRCustomDecorations.JBRWindowTopBorder.getInstance().repaintBorder(FlatTitlePane.this);
/*     */       }
/* 748 */       FlatTitlePane.this.repaintWindowBorder();
/*     */     }
/*     */ 
/*     */     
/*     */     public void windowDeactivated(WindowEvent e) {
/* 753 */       FlatTitlePane.this.activeChanged(false);
/* 754 */       FlatTitlePane.this.updateJBRHitTestSpotsAndTitleBarHeight();
/*     */       
/* 756 */       if (FlatTitlePane.this.hasJBRCustomDecoration()) {
/* 757 */         JBRCustomDecorations.JBRWindowTopBorder.getInstance().repaintBorder(FlatTitlePane.this);
/*     */       }
/* 759 */       FlatTitlePane.this.repaintWindowBorder();
/*     */     }
/*     */ 
/*     */     
/*     */     public void windowStateChanged(WindowEvent e) {
/* 764 */       FlatTitlePane.this.frameStateChanged();
/* 765 */       FlatTitlePane.this.updateJBRHitTestSpotsAndTitleBarHeight();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void mouseClicked(MouseEvent e) {
/* 774 */       if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
/* 775 */         if (e.getSource() == FlatTitlePane.this.iconLabel) {
/*     */           
/* 777 */           FlatTitlePane.this.close();
/* 778 */         } else if (!FlatTitlePane.this.hasJBRCustomDecoration() && FlatTitlePane.this.window instanceof Frame && ((Frame)FlatTitlePane.this.window)
/*     */           
/* 780 */           .isResizable()) {
/*     */ 
/*     */           
/* 783 */           Frame frame = (Frame)FlatTitlePane.this.window;
/* 784 */           if ((frame.getExtendedState() & 0x6) != 0) {
/* 785 */             FlatTitlePane.this.restore();
/*     */           } else {
/* 787 */             FlatTitlePane.this.maximize();
/*     */           } 
/*     */         } 
/*     */       }
/*     */     }
/*     */     
/*     */     public void mousePressed(MouseEvent e) {
/* 794 */       if (FlatTitlePane.this.window == null) {
/*     */         return;
/*     */       }
/* 797 */       this.dragOffset = SwingUtilities.convertPoint(FlatTitlePane.this, e.getPoint(), FlatTitlePane.this.window);
/*     */     }
/*     */ 
/*     */     
/*     */     public void mouseReleased(MouseEvent e) {}
/*     */     
/*     */     public void mouseEntered(MouseEvent e) {}
/*     */     
/*     */     public void mouseExited(MouseEvent e) {}
/*     */     
/*     */     public void mouseDragged(MouseEvent e) {
/* 808 */       if (FlatTitlePane.this.window == null) {
/*     */         return;
/*     */       }
/* 811 */       if (FlatTitlePane.this.hasJBRCustomDecoration()) {
/*     */         return;
/*     */       }
/*     */       
/* 815 */       if (FlatTitlePane.this.window instanceof Frame) {
/* 816 */         Frame frame = (Frame)FlatTitlePane.this.window;
/* 817 */         int state = frame.getExtendedState();
/* 818 */         if ((state & 0x6) != 0) {
/* 819 */           int maximizedWidth = FlatTitlePane.this.window.getWidth();
/*     */ 
/*     */           
/* 822 */           frame.setExtendedState(state & 0xFFFFFFF9);
/*     */ 
/*     */ 
/*     */           
/* 826 */           int restoredWidth = FlatTitlePane.this.window.getWidth();
/* 827 */           int center = restoredWidth / 2;
/* 828 */           if (this.dragOffset.x > center)
/*     */           {
/* 830 */             if (this.dragOffset.x > maximizedWidth - center) {
/* 831 */               this.dragOffset.x = restoredWidth - maximizedWidth - this.dragOffset.x;
/*     */             } else {
/* 833 */               this.dragOffset.x = center;
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 839 */       int newX = e.getXOnScreen() - this.dragOffset.x;
/* 840 */       int newY = e.getYOnScreen() - this.dragOffset.y;
/*     */       
/* 842 */       if (newX == FlatTitlePane.this.window.getX() && newY == FlatTitlePane.this.window.getY()) {
/*     */         return;
/*     */       }
/*     */       
/* 846 */       FlatTitlePane.this.window.setLocation(newX, newY);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void mouseMoved(MouseEvent e) {}
/*     */ 
/*     */     
/*     */     public void componentResized(ComponentEvent e) {
/* 855 */       FlatTitlePane.this.updateJBRHitTestSpotsAndTitleBarHeightLater();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void componentShown(ComponentEvent e) {
/* 861 */       FlatTitlePane.this.frameStateChanged();
/*     */     }
/*     */     
/*     */     public void componentMoved(ComponentEvent e) {}
/*     */     
/*     */     public void componentHidden(ComponentEvent e) {}
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatTitlePane.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */