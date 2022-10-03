/*     */ package com.formdev.flatlaf.ui;
/*     */ 
/*     */ import com.formdev.flatlaf.FlatLaf;
/*     */ import com.formdev.flatlaf.util.HiDPIUtils;
/*     */ import com.formdev.flatlaf.util.SystemInfo;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.LayoutManager2;
/*     */ import java.awt.Window;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.util.function.Function;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLayeredPane;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.BorderUIResource;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicRootPaneUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FlatRootPaneUI
/*     */   extends BasicRootPaneUI
/*     */ {
/*  74 */   static final boolean canUseJBRCustomDecorations = (SystemInfo.isJetBrainsJVM_11_orLater && SystemInfo.isWindows_10_orLater);
/*     */ 
/*     */   
/*  77 */   protected final Color borderColor = UIManager.getColor("TitlePane.borderColor");
/*     */   
/*     */   protected JRootPane rootPane;
/*     */   
/*     */   protected FlatTitlePane titlePane;
/*     */   protected FlatWindowResizer windowResizer;
/*     */   private LayoutManager oldLayout;
/*     */   
/*     */   public static ComponentUI createUI(JComponent c) {
/*  86 */     return new FlatRootPaneUI();
/*     */   }
/*     */ 
/*     */   
/*     */   public void installUI(JComponent c) {
/*  91 */     super.installUI(c);
/*     */     
/*  93 */     this.rootPane = (JRootPane)c;
/*     */     
/*  95 */     if (this.rootPane.getWindowDecorationStyle() != 0) {
/*  96 */       installClientDecorations();
/*     */     } else {
/*  98 */       installBorder();
/*     */     } 
/* 100 */     if (canUseJBRCustomDecorations)
/* 101 */       JBRCustomDecorations.install(this.rootPane); 
/*     */   }
/*     */   
/*     */   protected void installBorder() {
/* 105 */     if (this.borderColor != null) {
/* 106 */       Border b = this.rootPane.getBorder();
/* 107 */       if (b == null || b instanceof javax.swing.plaf.UIResource) {
/* 108 */         this.rootPane.setBorder(new FlatWindowTitleBorder(this.borderColor));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void uninstallUI(JComponent c) {
/* 114 */     super.uninstallUI(c);
/*     */     
/* 116 */     uninstallClientDecorations();
/* 117 */     this.rootPane = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void installDefaults(JRootPane c) {
/* 122 */     super.installDefaults(c);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 129 */     Container parent = c.getParent();
/* 130 */     if (parent instanceof JFrame || parent instanceof javax.swing.JDialog) {
/* 131 */       Color background = parent.getBackground();
/* 132 */       if (background == null || background instanceof javax.swing.plaf.UIResource) {
/* 133 */         parent.setBackground(UIManager.getColor("control"));
/*     */       }
/*     */     } 
/*     */     
/* 137 */     if (SystemInfo.isJetBrainsJVM && SystemInfo.isMacOS_10_14_Mojave_orLater)
/* 138 */       c.putClientProperty("jetbrains.awt.windowDarkAppearance", Boolean.valueOf(FlatLaf.isLafDark())); 
/*     */   }
/*     */   
/*     */   protected void installClientDecorations() {
/* 142 */     boolean isJBRSupported = (canUseJBRCustomDecorations && JBRCustomDecorations.isSupported());
/*     */ 
/*     */     
/* 145 */     if (this.rootPane.getWindowDecorationStyle() != 0 && !isJBRSupported) {
/* 146 */       LookAndFeel.installBorder(this.rootPane, "RootPane.border");
/*     */     } else {
/* 148 */       LookAndFeel.uninstallBorder(this.rootPane);
/*     */     } 
/*     */     
/* 151 */     setTitlePane(createTitlePane());
/*     */ 
/*     */     
/* 154 */     this.oldLayout = this.rootPane.getLayout();
/* 155 */     this.rootPane.setLayout(createRootLayout());
/*     */ 
/*     */     
/* 158 */     if (!isJBRSupported)
/* 159 */       this.windowResizer = createWindowResizer(); 
/*     */   }
/*     */   
/*     */   protected void uninstallClientDecorations() {
/* 163 */     LookAndFeel.uninstallBorder(this.rootPane);
/* 164 */     setTitlePane(null);
/*     */     
/* 166 */     if (this.windowResizer != null) {
/* 167 */       this.windowResizer.uninstall();
/* 168 */       this.windowResizer = null;
/*     */     } 
/*     */     
/* 171 */     if (this.oldLayout != null) {
/* 172 */       this.rootPane.setLayout(this.oldLayout);
/* 173 */       this.oldLayout = null;
/*     */     } 
/*     */     
/* 176 */     if (this.rootPane.getWindowDecorationStyle() == 0) {
/* 177 */       this.rootPane.revalidate();
/* 178 */       this.rootPane.repaint();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected FlatRootLayout createRootLayout() {
/* 183 */     return new FlatRootLayout();
/*     */   }
/*     */   
/*     */   protected FlatWindowResizer createWindowResizer() {
/* 187 */     return new FlatWindowResizer.WindowResizer(this.rootPane);
/*     */   }
/*     */   
/*     */   protected FlatTitlePane createTitlePane() {
/* 191 */     return new FlatTitlePane(this.rootPane);
/*     */   }
/*     */ 
/*     */   
/* 195 */   protected static final Integer TITLE_PANE_LAYER = Integer.valueOf(JLayeredPane.FRAME_CONTENT_LAYER.intValue() - 1);
/*     */   
/*     */   protected void setTitlePane(FlatTitlePane newTitlePane) {
/* 198 */     JLayeredPane layeredPane = this.rootPane.getLayeredPane();
/*     */     
/* 200 */     if (this.titlePane != null) {
/* 201 */       layeredPane.remove(this.titlePane);
/*     */     }
/* 203 */     if (newTitlePane != null) {
/* 204 */       layeredPane.add(newTitlePane, TITLE_PANE_LAYER);
/*     */     }
/* 206 */     this.titlePane = newTitlePane;
/*     */   }
/*     */ 
/*     */   
/*     */   public void propertyChange(PropertyChangeEvent e) {
/* 211 */     super.propertyChange(e);
/*     */     
/* 213 */     switch (e.getPropertyName()) {
/*     */       case "windowDecorationStyle":
/* 215 */         uninstallClientDecorations();
/* 216 */         if (this.rootPane.getWindowDecorationStyle() != 0) {
/* 217 */           installClientDecorations(); break;
/*     */         } 
/* 219 */         installBorder();
/*     */         break;
/*     */       
/*     */       case "JRootPane.menuBarEmbedded":
/* 223 */         if (this.titlePane != null) {
/* 224 */           this.titlePane.menuBarChanged();
/* 225 */           this.rootPane.revalidate();
/* 226 */           this.rootPane.repaint();
/*     */         } 
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected class FlatRootLayout
/*     */     implements LayoutManager2
/*     */   {
/*     */     public void addLayoutComponent(String name, Component comp) {}
/*     */     
/*     */     public void addLayoutComponent(Component comp, Object constraints) {}
/*     */     
/*     */     public void removeLayoutComponent(Component comp) {}
/*     */     
/*     */     public Dimension preferredLayoutSize(Container parent) {
/* 243 */       return computeLayoutSize(parent, c -> c.getPreferredSize());
/*     */     }
/*     */ 
/*     */     
/*     */     public Dimension minimumLayoutSize(Container parent) {
/* 248 */       return computeLayoutSize(parent, c -> c.getMinimumSize());
/*     */     }
/*     */ 
/*     */     
/*     */     public Dimension maximumLayoutSize(Container parent) {
/* 253 */       return new Dimension(2147483647, 2147483647);
/*     */     }
/*     */     
/*     */     private Dimension computeLayoutSize(Container parent, Function<Component, Dimension> getSizeFunc) {
/* 257 */       JRootPane rootPane = (JRootPane)parent;
/*     */ 
/*     */       
/* 260 */       Dimension titlePaneSize = (FlatRootPaneUI.this.titlePane != null) ? getSizeFunc.apply(FlatRootPaneUI.this.titlePane) : new Dimension();
/*     */ 
/*     */ 
/*     */       
/* 264 */       Dimension contentSize = (rootPane.getContentPane() != null) ? getSizeFunc.apply(rootPane.getContentPane()) : rootPane.getSize();
/*     */       
/* 266 */       int width = Math.max(titlePaneSize.width, contentSize.width);
/* 267 */       int height = titlePaneSize.height + contentSize.height;
/* 268 */       if (FlatRootPaneUI.this.titlePane == null || !FlatRootPaneUI.this.titlePane.isMenuBarEmbedded()) {
/* 269 */         JMenuBar menuBar = rootPane.getJMenuBar();
/*     */         
/* 271 */         Dimension menuBarSize = (menuBar != null && menuBar.isVisible()) ? getSizeFunc.apply(menuBar) : new Dimension();
/*     */ 
/*     */         
/* 274 */         width = Math.max(width, menuBarSize.width);
/* 275 */         height += menuBarSize.height;
/*     */       } 
/*     */       
/* 278 */       Insets insets = rootPane.getInsets();
/*     */       
/* 280 */       return new Dimension(width + insets.left + insets.right, height + insets.top + insets.bottom);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void layoutContainer(Container parent) {
/* 287 */       JRootPane rootPane = (JRootPane)parent;
/* 288 */       boolean isFullScreen = FlatUIUtils.isFullScreen(rootPane);
/*     */       
/* 290 */       Insets insets = rootPane.getInsets();
/* 291 */       int x = insets.left;
/* 292 */       int y = insets.top;
/* 293 */       int width = rootPane.getWidth() - insets.left - insets.right;
/* 294 */       int height = rootPane.getHeight() - insets.top - insets.bottom;
/*     */       
/* 296 */       if (rootPane.getLayeredPane() != null)
/* 297 */         rootPane.getLayeredPane().setBounds(x, y, width, height); 
/* 298 */       if (rootPane.getGlassPane() != null) {
/* 299 */         rootPane.getGlassPane().setBounds(x, y, width, height);
/*     */       }
/* 301 */       int nextY = 0;
/* 302 */       if (!isFullScreen && FlatRootPaneUI.this.titlePane != null) {
/* 303 */         Dimension prefSize = FlatRootPaneUI.this.titlePane.getPreferredSize();
/* 304 */         FlatRootPaneUI.this.titlePane.setBounds(0, 0, width, prefSize.height);
/* 305 */         nextY += prefSize.height;
/*     */       } 
/*     */       
/* 308 */       JMenuBar menuBar = rootPane.getJMenuBar();
/* 309 */       if (menuBar != null && menuBar.isVisible()) {
/* 310 */         if (!isFullScreen && FlatRootPaneUI.this.titlePane != null && FlatRootPaneUI.this.titlePane.isMenuBarEmbedded()) {
/* 311 */           FlatRootPaneUI.this.titlePane.validate();
/* 312 */           menuBar.setBounds(FlatRootPaneUI.this.titlePane.getMenuBarBounds());
/*     */         } else {
/* 314 */           Dimension prefSize = menuBar.getPreferredSize();
/* 315 */           menuBar.setBounds(0, nextY, width, prefSize.height);
/* 316 */           nextY += prefSize.height;
/*     */         } 
/*     */       }
/*     */       
/* 320 */       Container contentPane = rootPane.getContentPane();
/* 321 */       if (contentPane != null) {
/* 322 */         contentPane.setBounds(0, nextY, width, Math.max(height - nextY, 0));
/*     */       }
/* 324 */       if (FlatRootPaneUI.this.titlePane != null) {
/* 325 */         FlatRootPaneUI.this.titlePane.menuBarLayouted();
/*     */       }
/*     */     }
/*     */     
/*     */     public void invalidateLayout(Container parent) {
/* 330 */       if (FlatRootPaneUI.this.titlePane != null) {
/* 331 */         FlatRootPaneUI.this.titlePane.menuBarChanged();
/*     */       }
/*     */     }
/*     */     
/*     */     public float getLayoutAlignmentX(Container target) {
/* 336 */       return 0.0F;
/*     */     }
/*     */ 
/*     */     
/*     */     public float getLayoutAlignmentY(Container target) {
/* 341 */       return 0.0F;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class FlatWindowBorder
/*     */     extends BorderUIResource.EmptyBorderUIResource
/*     */   {
/* 350 */     protected final Color activeBorderColor = UIManager.getColor("RootPane.activeBorderColor");
/* 351 */     protected final Color inactiveBorderColor = UIManager.getColor("RootPane.inactiveBorderColor");
/* 352 */     protected final Color baseBorderColor = UIManager.getColor("Panel.background");
/*     */     
/*     */     public FlatWindowBorder() {
/* 355 */       super(1, 1, 1, 1);
/*     */     }
/*     */ 
/*     */     
/*     */     public Insets getBorderInsets(Component c, Insets insets) {
/* 360 */       if (isWindowMaximized(c) || FlatUIUtils.isFullScreen(c)) {
/*     */         
/* 362 */         insets.top = insets.left = insets.bottom = insets.right = 0;
/* 363 */         return insets;
/*     */       } 
/* 365 */       return super.getBorderInsets(c, insets);
/*     */     }
/*     */ 
/*     */     
/*     */     public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/* 370 */       if (isWindowMaximized(c) || FlatUIUtils.isFullScreen(c)) {
/*     */         return;
/*     */       }
/* 373 */       Container parent = c.getParent();
/* 374 */       boolean active = (parent instanceof Window) ? ((Window)parent).isActive() : false;
/*     */       
/* 376 */       g.setColor(FlatUIUtils.deriveColor(active ? this.activeBorderColor : this.inactiveBorderColor, this.baseBorderColor));
/* 377 */       HiDPIUtils.paintAtScale1x((Graphics2D)g, x, y, width, height, this::paintImpl);
/*     */     }
/*     */     
/*     */     private void paintImpl(Graphics2D g, int x, int y, int width, int height, double scaleFactor) {
/* 381 */       g.drawRect(x, y, width - 1, height - 1);
/*     */     }
/*     */     
/*     */     protected boolean isWindowMaximized(Component c) {
/* 385 */       Container parent = c.getParent();
/* 386 */       return (parent instanceof Frame) ? (
/* 387 */         ((((Frame)parent).getExtendedState() & 0x6) != 0)) : false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FlatWindowTitleBorder
/*     */     extends BorderUIResource.EmptyBorderUIResource
/*     */   {
/*     */     private final Color borderColor;
/*     */ 
/*     */     
/*     */     FlatWindowTitleBorder(Color borderColor) {
/* 400 */       super(0, 0, 0, 0);
/* 401 */       this.borderColor = borderColor;
/*     */     }
/*     */ 
/*     */     
/*     */     public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/* 406 */       if (showBorder(c)) {
/* 407 */         float lineHeight = UIScale.scale(1.0F);
/* 408 */         FlatUIUtils.paintFilledRectangle(g, this.borderColor, x, y, width, lineHeight);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Insets getBorderInsets(Component c, Insets insets) {
/* 414 */       insets.set(showBorder(c) ? 1 : 0, 0, 0, 0);
/* 415 */       return insets;
/*     */     }
/*     */     
/*     */     private boolean showBorder(Component c) {
/* 419 */       Container parent = c.getParent();
/* 420 */       return ((parent instanceof JFrame && (((JFrame)parent)
/*     */         
/* 422 */         .getJMenuBar() == null || 
/* 423 */         !((JFrame)parent).getJMenuBar().isVisible())) || parent instanceof javax.swing.JDialog);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatRootPaneUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */