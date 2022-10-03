/*      */ package com.formdev.flatlaf.ui;
/*      */ 
/*      */ import com.formdev.flatlaf.FlatClientProperties;
/*      */ import com.formdev.flatlaf.FlatLaf;
/*      */ import com.formdev.flatlaf.util.Animator;
/*      */ import com.formdev.flatlaf.util.CubicBezierEasing;
/*      */ import com.formdev.flatlaf.util.JavaCompatibility;
/*      */ import com.formdev.flatlaf.util.StringUtils;
/*      */ import com.formdev.flatlaf.util.UIScale;
/*      */ import java.awt.AWTKeyStroke;
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.EventQueue;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.ComponentEvent;
/*      */ import java.awt.event.ComponentListener;
/*      */ import java.awt.event.ContainerEvent;
/*      */ import java.awt.event.ContainerListener;
/*      */ import java.awt.event.MouseAdapter;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseListener;
/*      */ import java.awt.event.MouseMotionListener;
/*      */ import java.awt.event.MouseWheelEvent;
/*      */ import java.awt.geom.Path2D;
/*      */ import java.awt.geom.Rectangle2D;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.util.Collections;
/*      */ import java.util.Locale;
/*      */ import java.util.Set;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.IntConsumer;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.swing.ButtonModel;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JMenuItem;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JPopupMenu;
/*      */ import javax.swing.JTabbedPane;
/*      */ import javax.swing.JViewport;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.Timer;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.event.PopupMenuEvent;
/*      */ import javax.swing.event.PopupMenuListener;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import javax.swing.plaf.basic.BasicTabbedPaneUI;
/*      */ import javax.swing.text.JTextComponent;
/*      */ import javax.swing.text.View;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class FlatTabbedPaneUI
/*      */   extends BasicTabbedPaneUI
/*      */ {
/*      */   protected static final int NEVER = 0;
/*      */   protected static final int AS_NEEDED = 2;
/*      */   protected static final int AS_NEEDED_SINGLE = 3;
/*      */   protected static final int BOTH = 100;
/*      */   protected static final int FILL = 100;
/*      */   protected static final int WIDTH_MODE_PREFERRED = 0;
/*      */   protected static final int WIDTH_MODE_EQUAL = 1;
/*      */   protected static final int WIDTH_MODE_COMPACT = 2;
/*      */   private static Set<KeyStroke> focusForwardTraversalKeys;
/*      */   private static Set<KeyStroke> focusBackwardTraversalKeys;
/*      */   protected Color foreground;
/*      */   protected Color disabledForeground;
/*      */   protected Color selectedBackground;
/*      */   protected Color selectedForeground;
/*      */   protected Color underlineColor;
/*      */   protected Color disabledUnderlineColor;
/*      */   protected Color hoverColor;
/*      */   protected Color focusColor;
/*      */   protected Color tabSeparatorColor;
/*      */   protected Color contentAreaColor;
/*      */   private int textIconGapUnscaled;
/*      */   protected int minimumTabWidth;
/*      */   protected int maximumTabWidth;
/*      */   protected int tabHeight;
/*      */   protected int tabSelectionHeight;
/*      */   protected int contentSeparatorHeight;
/*      */   protected boolean showTabSeparators;
/*      */   protected boolean tabSeparatorsFullHeight;
/*      */   protected boolean hasFullBorder;
/*      */   protected boolean tabsOpaque = true;
/*      */   private int tabsPopupPolicy;
/*      */   private int scrollButtonsPolicy;
/*      */   private int scrollButtonsPlacement;
/*      */   private int tabAreaAlignment;
/*      */   private int tabAlignment;
/*      */   private int tabWidthMode;
/*      */   protected Icon closeIcon;
/*      */   protected String arrowType;
/*      */   protected Insets buttonInsets;
/*      */   protected int buttonArc;
/*      */   protected Color buttonHoverBackground;
/*      */   protected Color buttonPressedBackground;
/*      */   protected String moreTabsButtonToolTipText;
/*      */   protected JViewport tabViewport;
/*      */   protected FlatWheelTabScroller wheelTabScroller;
/*      */   private JButton tabCloseButton;
/*      */   private JButton moreTabsButton;
/*      */   private Container leadingComponent;
/*      */   private Container trailingComponent;
/*      */   private Dimension scrollBackwardButtonPrefSize;
/*      */   private Handler handler;
/*      */   private boolean blockRollover;
/*      */   private boolean rolloverTabClose;
/*      */   private boolean pressedTabClose;
/*      */   private Object[] oldRenderingHints;
/*      */   private boolean inCalculateEqual;
/*      */   
/*      */   public static ComponentUI createUI(JComponent c) {
/*  234 */     return new FlatTabbedPaneUI();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void installUI(JComponent c) {
/*  240 */     String tabLayoutPolicyStr = UIManager.getString("TabbedPane.tabLayoutPolicy");
/*  241 */     if (tabLayoutPolicyStr != null) {
/*      */       int tabLayoutPolicy;
/*  243 */       switch (tabLayoutPolicyStr) {
/*      */         default:
/*  245 */           tabLayoutPolicy = 0; break;
/*  246 */         case "scroll": tabLayoutPolicy = 1; break;
/*      */       } 
/*  248 */       ((JTabbedPane)c).setTabLayoutPolicy(tabLayoutPolicy);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  253 */     this.arrowType = UIManager.getString("TabbedPane.arrowType");
/*  254 */     this.foreground = UIManager.getColor("TabbedPane.foreground");
/*  255 */     this.disabledForeground = UIManager.getColor("TabbedPane.disabledForeground");
/*  256 */     this.buttonHoverBackground = UIManager.getColor("TabbedPane.buttonHoverBackground");
/*  257 */     this.buttonPressedBackground = UIManager.getColor("TabbedPane.buttonPressedBackground");
/*      */     
/*  259 */     super.installUI(c);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void installDefaults() {
/*  264 */     if (UIManager.getBoolean("TabbedPane.tabsOverlapBorder")) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  280 */       Object oldValue = UIManager.put("TabbedPane.tabsOverlapBorder", Boolean.valueOf(false));
/*  281 */       super.installDefaults();
/*  282 */       UIManager.put("TabbedPane.tabsOverlapBorder", oldValue);
/*      */     } else {
/*  284 */       super.installDefaults();
/*      */     } 
/*  286 */     this.selectedBackground = UIManager.getColor("TabbedPane.selectedBackground");
/*  287 */     this.selectedForeground = UIManager.getColor("TabbedPane.selectedForeground");
/*  288 */     this.underlineColor = UIManager.getColor("TabbedPane.underlineColor");
/*  289 */     this.disabledUnderlineColor = UIManager.getColor("TabbedPane.disabledUnderlineColor");
/*  290 */     this.hoverColor = UIManager.getColor("TabbedPane.hoverColor");
/*  291 */     this.focusColor = UIManager.getColor("TabbedPane.focusColor");
/*  292 */     this.tabSeparatorColor = UIManager.getColor("TabbedPane.tabSeparatorColor");
/*  293 */     this.contentAreaColor = UIManager.getColor("TabbedPane.contentAreaColor");
/*      */     
/*  295 */     this.textIconGapUnscaled = UIManager.getInt("TabbedPane.textIconGap");
/*  296 */     this.minimumTabWidth = UIManager.getInt("TabbedPane.minimumTabWidth");
/*  297 */     this.maximumTabWidth = UIManager.getInt("TabbedPane.maximumTabWidth");
/*  298 */     this.tabHeight = UIManager.getInt("TabbedPane.tabHeight");
/*  299 */     this.tabSelectionHeight = UIManager.getInt("TabbedPane.tabSelectionHeight");
/*  300 */     this.contentSeparatorHeight = UIManager.getInt("TabbedPane.contentSeparatorHeight");
/*  301 */     this.showTabSeparators = UIManager.getBoolean("TabbedPane.showTabSeparators");
/*  302 */     this.tabSeparatorsFullHeight = UIManager.getBoolean("TabbedPane.tabSeparatorsFullHeight");
/*  303 */     this.hasFullBorder = UIManager.getBoolean("TabbedPane.hasFullBorder");
/*  304 */     this.tabsOpaque = UIManager.getBoolean("TabbedPane.tabsOpaque");
/*      */     
/*  306 */     this.tabsPopupPolicy = parseTabsPopupPolicy(UIManager.getString("TabbedPane.tabsPopupPolicy"));
/*  307 */     this.scrollButtonsPolicy = parseScrollButtonsPolicy(UIManager.getString("TabbedPane.scrollButtonsPolicy"));
/*  308 */     this.scrollButtonsPlacement = parseScrollButtonsPlacement(UIManager.getString("TabbedPane.scrollButtonsPlacement"));
/*      */     
/*  310 */     this.tabAreaAlignment = parseAlignment(UIManager.getString("TabbedPane.tabAreaAlignment"), 10);
/*  311 */     this.tabAlignment = parseAlignment(UIManager.getString("TabbedPane.tabAlignment"), 0);
/*  312 */     this.tabWidthMode = parseTabWidthMode(UIManager.getString("TabbedPane.tabWidthMode"));
/*  313 */     this.closeIcon = UIManager.getIcon("TabbedPane.closeIcon");
/*      */     
/*  315 */     this.buttonInsets = UIManager.getInsets("TabbedPane.buttonInsets");
/*  316 */     this.buttonArc = UIManager.getInt("TabbedPane.buttonArc");
/*      */     
/*  318 */     Locale l = this.tabPane.getLocale();
/*  319 */     this.moreTabsButtonToolTipText = UIManager.getString("TabbedPane.moreTabsButtonToolTipText", l);
/*      */ 
/*      */     
/*  322 */     this.textIconGap = UIScale.scale(this.textIconGapUnscaled);
/*      */ 
/*      */ 
/*      */     
/*  326 */     if (focusForwardTraversalKeys == null) {
/*  327 */       focusForwardTraversalKeys = Collections.singleton(KeyStroke.getKeyStroke(9, 0));
/*  328 */       focusBackwardTraversalKeys = Collections.singleton(KeyStroke.getKeyStroke(9, 1));
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  333 */     this.tabPane.setFocusTraversalKeys(0, (Set)focusForwardTraversalKeys);
/*  334 */     this.tabPane.setFocusTraversalKeys(1, (Set)focusBackwardTraversalKeys);
/*      */     
/*  336 */     MigLayoutVisualPadding.install(this.tabPane, null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void uninstallDefaults() {
/*  342 */     this.tabPane.setFocusTraversalKeys(0, (Set<? extends AWTKeyStroke>)null);
/*  343 */     this.tabPane.setFocusTraversalKeys(1, (Set<? extends AWTKeyStroke>)null);
/*      */     
/*  345 */     super.uninstallDefaults();
/*      */     
/*  347 */     this.foreground = null;
/*  348 */     this.disabledForeground = null;
/*  349 */     this.selectedBackground = null;
/*  350 */     this.selectedForeground = null;
/*  351 */     this.underlineColor = null;
/*  352 */     this.disabledUnderlineColor = null;
/*  353 */     this.hoverColor = null;
/*  354 */     this.focusColor = null;
/*  355 */     this.tabSeparatorColor = null;
/*  356 */     this.contentAreaColor = null;
/*  357 */     this.closeIcon = null;
/*      */     
/*  359 */     this.buttonHoverBackground = null;
/*  360 */     this.buttonPressedBackground = null;
/*      */     
/*  362 */     MigLayoutVisualPadding.uninstall(this.tabPane);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void installComponents() {
/*  367 */     super.installComponents();
/*      */ 
/*      */     
/*  370 */     this.tabViewport = null;
/*  371 */     if (isScrollTabLayout()) {
/*  372 */       for (Component c : this.tabPane.getComponents()) {
/*  373 */         if (c instanceof JViewport && c.getClass().getName().equals("javax.swing.plaf.basic.BasicTabbedPaneUI$ScrollableTabViewport")) {
/*  374 */           this.tabViewport = (JViewport)c;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     }
/*  380 */     installHiddenTabsNavigation();
/*  381 */     installLeadingComponent();
/*  382 */     installTrailingComponent();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void uninstallComponents() {
/*  389 */     uninstallHiddenTabsNavigation();
/*  390 */     uninstallLeadingComponent();
/*  391 */     uninstallTrailingComponent();
/*      */     
/*  393 */     super.uninstallComponents();
/*      */     
/*  395 */     this.tabCloseButton = null;
/*  396 */     this.tabViewport = null;
/*      */   }
/*      */   
/*      */   protected void installHiddenTabsNavigation() {
/*  400 */     if (!isScrollTabLayout() || this.tabViewport == null) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  409 */     this.tabPane.setLayout(createScrollLayoutManager((BasicTabbedPaneUI.TabbedPaneLayout)this.tabPane.getLayout()));
/*      */ 
/*      */     
/*  412 */     this.moreTabsButton = createMoreTabsButton();
/*  413 */     this.tabPane.add(this.moreTabsButton);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void uninstallHiddenTabsNavigation() {
/*  419 */     if (this.tabPane.getLayout() instanceof FlatTabbedPaneScrollLayout) {
/*  420 */       this.tabPane.setLayout(((FlatTabbedPaneScrollLayout)this.tabPane.getLayout()).delegate);
/*      */     }
/*  422 */     if (this.moreTabsButton != null) {
/*  423 */       this.tabPane.remove(this.moreTabsButton);
/*  424 */       this.moreTabsButton = null;
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void installLeadingComponent() {
/*  429 */     Object c = this.tabPane.getClientProperty("JTabbedPane.leadingComponent");
/*  430 */     if (c instanceof Component) {
/*  431 */       this.leadingComponent = new ContainerUIResource((Component)c);
/*  432 */       this.tabPane.add(this.leadingComponent);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void uninstallLeadingComponent() {
/*  437 */     if (this.leadingComponent != null) {
/*  438 */       this.tabPane.remove(this.leadingComponent);
/*  439 */       this.leadingComponent = null;
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void installTrailingComponent() {
/*  444 */     Object c = this.tabPane.getClientProperty("JTabbedPane.trailingComponent");
/*  445 */     if (c instanceof Component) {
/*  446 */       this.trailingComponent = new ContainerUIResource((Component)c);
/*  447 */       this.tabPane.add(this.trailingComponent);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void uninstallTrailingComponent() {
/*  452 */     if (this.trailingComponent != null) {
/*  453 */       this.tabPane.remove(this.trailingComponent);
/*  454 */       this.trailingComponent = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void installListeners() {
/*  460 */     super.installListeners();
/*      */     
/*  462 */     getHandler().installListeners();
/*      */     
/*  464 */     if (this.tabViewport != null && (this.wheelTabScroller = createWheelTabScroller()) != null) {
/*      */ 
/*      */ 
/*      */       
/*  468 */       this.tabPane.addMouseWheelListener(this.wheelTabScroller);
/*  469 */       this.tabPane.addMouseMotionListener(this.wheelTabScroller);
/*  470 */       this.tabPane.addMouseListener(this.wheelTabScroller);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void uninstallListeners() {
/*  476 */     super.uninstallListeners();
/*      */     
/*  478 */     if (this.handler != null) {
/*  479 */       this.handler.uninstallListeners();
/*  480 */       this.handler = null;
/*      */     } 
/*      */     
/*  483 */     if (this.wheelTabScroller != null) {
/*  484 */       this.wheelTabScroller.uninstall();
/*      */       
/*  486 */       this.tabPane.removeMouseWheelListener(this.wheelTabScroller);
/*  487 */       this.tabPane.removeMouseMotionListener(this.wheelTabScroller);
/*  488 */       this.tabPane.removeMouseListener(this.wheelTabScroller);
/*  489 */       this.wheelTabScroller = null;
/*      */     } 
/*      */   }
/*      */   
/*      */   private Handler getHandler() {
/*  494 */     if (this.handler == null)
/*  495 */       this.handler = new Handler(); 
/*  496 */     return this.handler;
/*      */   }
/*      */   
/*      */   protected FlatWheelTabScroller createWheelTabScroller() {
/*  500 */     return new FlatWheelTabScroller();
/*      */   }
/*      */ 
/*      */   
/*      */   protected MouseListener createMouseListener() {
/*  505 */     Handler handler = getHandler();
/*  506 */     handler.mouseDelegate = super.createMouseListener();
/*  507 */     return handler;
/*      */   }
/*      */ 
/*      */   
/*      */   protected PropertyChangeListener createPropertyChangeListener() {
/*  512 */     Handler handler = getHandler();
/*  513 */     handler.propertyChangeDelegate = super.createPropertyChangeListener();
/*  514 */     return handler;
/*      */   }
/*      */ 
/*      */   
/*      */   protected ChangeListener createChangeListener() {
/*  519 */     Handler handler = getHandler();
/*  520 */     handler.changeDelegate = super.createChangeListener();
/*  521 */     return handler;
/*      */   }
/*      */ 
/*      */   
/*      */   protected LayoutManager createLayoutManager() {
/*  526 */     if (this.tabPane.getTabLayoutPolicy() == 0) {
/*  527 */       return new FlatTabbedPaneLayout();
/*      */     }
/*  529 */     return super.createLayoutManager();
/*      */   }
/*      */   
/*      */   protected LayoutManager createScrollLayoutManager(BasicTabbedPaneUI.TabbedPaneLayout delegate) {
/*  533 */     return new FlatTabbedPaneScrollLayout(delegate);
/*      */   }
/*      */   
/*      */   protected JButton createMoreTabsButton() {
/*  537 */     return new FlatMoreTabsButton();
/*      */   }
/*      */ 
/*      */   
/*      */   protected JButton createScrollButton(int direction) {
/*  542 */     return new FlatScrollableTabButton(direction);
/*      */   }
/*      */   
/*      */   protected void setRolloverTab(int x, int y) {
/*  546 */     setRolloverTab(tabForCoordinate(this.tabPane, x, y));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void setRolloverTab(int index) {
/*  551 */     if (this.blockRollover) {
/*      */       return;
/*      */     }
/*  554 */     int oldIndex = getRolloverTab();
/*  555 */     super.setRolloverTab(index);
/*      */     
/*  557 */     if (index == oldIndex) {
/*      */       return;
/*      */     }
/*      */     
/*  561 */     repaintTab(oldIndex);
/*  562 */     repaintTab(index);
/*      */   }
/*      */   
/*      */   protected boolean isRolloverTabClose() {
/*  566 */     return this.rolloverTabClose;
/*      */   }
/*      */   
/*      */   protected void setRolloverTabClose(boolean rollover) {
/*  570 */     if (this.rolloverTabClose == rollover) {
/*      */       return;
/*      */     }
/*  573 */     this.rolloverTabClose = rollover;
/*  574 */     repaintTab(getRolloverTab());
/*      */   }
/*      */   
/*      */   protected boolean isPressedTabClose() {
/*  578 */     return this.pressedTabClose;
/*      */   }
/*      */   
/*      */   protected void setPressedTabClose(boolean pressed) {
/*  582 */     if (this.pressedTabClose == pressed) {
/*      */       return;
/*      */     }
/*  585 */     this.pressedTabClose = pressed;
/*  586 */     repaintTab(getRolloverTab());
/*      */   }
/*      */   
/*      */   private void repaintTab(int tabIndex) {
/*  590 */     if (tabIndex < 0 || tabIndex >= this.tabPane.getTabCount()) {
/*      */       return;
/*      */     }
/*  593 */     Rectangle r = getTabBounds(this.tabPane, tabIndex);
/*  594 */     if (r != null) {
/*  595 */       this.tabPane.repaint(r);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
/*  602 */     int tabWidth, tabWidthMode = getTabWidthMode();
/*  603 */     if (tabWidthMode == 1 && isHorizontalTabPlacement() && !this.inCalculateEqual) {
/*  604 */       this.inCalculateEqual = true;
/*      */       try {
/*  606 */         tabWidth = calculateMaxTabWidth(tabPlacement); return tabWidth;
/*      */       } finally {
/*  608 */         this.inCalculateEqual = false;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  613 */     this.textIconGap = UIScale.scale(this.textIconGapUnscaled);
/*      */     
/*      */     Icon icon;
/*      */     
/*  617 */     if (tabWidthMode == 2 && tabIndex != this.tabPane
/*  618 */       .getSelectedIndex() && 
/*  619 */       isHorizontalTabPlacement() && this.tabPane
/*  620 */       .getTabComponentAt(tabIndex) == null && (
/*  621 */       icon = getIconForTab(tabIndex)) != null) {
/*      */       
/*  623 */       Insets tabInsets = getTabInsets(tabPlacement, tabIndex);
/*  624 */       tabWidth = icon.getIconWidth() + tabInsets.left + tabInsets.right;
/*      */     } else {
/*  626 */       int iconPlacement = FlatClientProperties.clientPropertyInt(this.tabPane, "JTabbedPane.tabIconPlacement", 10);
/*  627 */       if ((iconPlacement == 1 || iconPlacement == 3) && this.tabPane
/*  628 */         .getTabComponentAt(tabIndex) == null && (
/*  629 */         icon = getIconForTab(tabIndex)) != null) {
/*      */ 
/*      */         
/*  632 */         tabWidth = icon.getIconWidth();
/*      */         
/*  634 */         View view = getTextViewForTab(tabIndex);
/*  635 */         if (view != null) {
/*  636 */           tabWidth = Math.max(tabWidth, (int)view.getPreferredSpan(0));
/*      */         } else {
/*  638 */           String title = this.tabPane.getTitleAt(tabIndex);
/*  639 */           if (title != null) {
/*  640 */             tabWidth = Math.max(tabWidth, metrics.stringWidth(title));
/*      */           }
/*      */         } 
/*  643 */         Insets tabInsets = getTabInsets(tabPlacement, tabIndex);
/*  644 */         tabWidth += tabInsets.left + tabInsets.right;
/*      */       } else {
/*  646 */         tabWidth = super.calculateTabWidth(tabPlacement, tabIndex, metrics) - 3;
/*      */       } 
/*      */     } 
/*      */     
/*  650 */     if (isTabClosable(tabIndex)) {
/*  651 */       tabWidth += this.closeIcon.getIconWidth();
/*      */     }
/*      */     
/*  654 */     int min = getTabClientPropertyInt(tabIndex, "JTabbedPane.minimumTabWidth", this.minimumTabWidth);
/*  655 */     int max = getTabClientPropertyInt(tabIndex, "JTabbedPane.maximumTabWidth", this.maximumTabWidth);
/*  656 */     if (min > 0)
/*  657 */       tabWidth = Math.max(tabWidth, UIScale.scale(min)); 
/*  658 */     if (max > 0 && this.tabPane.getTabComponentAt(tabIndex) == null) {
/*  659 */       tabWidth = Math.min(tabWidth, UIScale.scale(max));
/*      */     }
/*  661 */     return tabWidth;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
/*  669 */     int tabHeight, iconPlacement = FlatClientProperties.clientPropertyInt(this.tabPane, "JTabbedPane.tabIconPlacement", 10); Icon icon;
/*  670 */     if ((iconPlacement == 1 || iconPlacement == 3) && this.tabPane
/*  671 */       .getTabComponentAt(tabIndex) == null && (
/*  672 */       icon = getIconForTab(tabIndex)) != null) {
/*      */ 
/*      */       
/*  675 */       tabHeight = icon.getIconHeight();
/*      */       
/*  677 */       View view = getTextViewForTab(tabIndex);
/*  678 */       if (view != null) {
/*  679 */         tabHeight += (int)view.getPreferredSpan(1) + UIScale.scale(this.textIconGapUnscaled);
/*  680 */       } else if (this.tabPane.getTitleAt(tabIndex) != null) {
/*  681 */         tabHeight += fontHeight + UIScale.scale(this.textIconGapUnscaled);
/*      */       } 
/*  683 */       Insets tabInsets = getTabInsets(tabPlacement, tabIndex);
/*  684 */       tabHeight += tabInsets.top + tabInsets.bottom;
/*      */     } else {
/*  686 */       tabHeight = super.calculateTabHeight(tabPlacement, tabIndex, fontHeight) - 2;
/*      */     } 
/*  688 */     return Math.max(tabHeight, UIScale.scale(FlatClientProperties.clientPropertyInt(this.tabPane, "JTabbedPane.tabHeight", this.tabHeight)));
/*      */   }
/*      */ 
/*      */   
/*      */   protected int calculateMaxTabWidth(int tabPlacement) {
/*  693 */     return hideTabArea() ? 0 : super.calculateMaxTabWidth(tabPlacement);
/*      */   }
/*      */ 
/*      */   
/*      */   protected int calculateMaxTabHeight(int tabPlacement) {
/*  698 */     return hideTabArea() ? 0 : super.calculateMaxTabHeight(tabPlacement);
/*      */   }
/*      */ 
/*      */   
/*      */   protected int calculateTabAreaWidth(int tabPlacement, int vertRunCount, int maxTabWidth) {
/*  703 */     return hideTabArea() ? 0 : super.calculateTabAreaWidth(tabPlacement, vertRunCount, maxTabWidth);
/*      */   }
/*      */ 
/*      */   
/*      */   protected int calculateTabAreaHeight(int tabPlacement, int horizRunCount, int maxTabHeight) {
/*  708 */     return hideTabArea() ? 0 : super.calculateTabAreaHeight(tabPlacement, horizRunCount, maxTabHeight);
/*      */   }
/*      */ 
/*      */   
/*      */   protected Insets getTabInsets(int tabPlacement, int tabIndex) {
/*  713 */     Object value = getTabClientProperty(tabIndex, "JTabbedPane.tabInsets");
/*  714 */     return UIScale.scale((value instanceof Insets) ? (Insets)value : super
/*      */         
/*  716 */         .getTabInsets(tabPlacement, tabIndex));
/*      */   }
/*      */ 
/*      */   
/*      */   protected Insets getSelectedTabPadInsets(int tabPlacement) {
/*  721 */     return new Insets(0, 0, 0, 0);
/*      */   }
/*      */   
/*      */   protected Insets getRealTabAreaInsets(int tabPlacement) {
/*  725 */     Insets currentTabAreaInsets = super.getTabAreaInsets(tabPlacement);
/*  726 */     Insets insets = (Insets)currentTabAreaInsets.clone();
/*      */     
/*  728 */     Object value = this.tabPane.getClientProperty("JTabbedPane.tabAreaInsets");
/*  729 */     if (value instanceof Insets) {
/*  730 */       rotateInsets((Insets)value, insets, tabPlacement);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  737 */     currentTabAreaInsets.top = currentTabAreaInsets.left = -10000;
/*      */ 
/*      */     
/*  740 */     insets = UIScale.scale(insets);
/*      */     
/*  742 */     return insets;
/*      */   }
/*      */ 
/*      */   
/*      */   protected Insets getTabAreaInsets(int tabPlacement) {
/*  747 */     Insets insets = getRealTabAreaInsets(tabPlacement);
/*      */ 
/*      */     
/*  750 */     if (this.tabPane.getTabLayoutPolicy() == 0) {
/*  751 */       if (isHorizontalTabPlacement()) {
/*  752 */         insets.left += getLeadingPreferredWidth();
/*  753 */         insets.right += getTrailingPreferredWidth();
/*      */       } else {
/*  755 */         insets.top += getLeadingPreferredHeight();
/*  756 */         insets.bottom += getTrailingPreferredHeight();
/*      */       } 
/*      */     }
/*      */     
/*  760 */     return insets;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Insets getContentBorderInsets(int tabPlacement) {
/*  770 */     if (hideTabArea() || this.contentSeparatorHeight == 0 || !FlatClientProperties.clientPropertyBoolean(this.tabPane, "JTabbedPane.showContentSeparator", true)) {
/*  771 */       return new Insets(0, 0, 0, 0);
/*      */     }
/*  773 */     boolean hasFullBorder = FlatClientProperties.clientPropertyBoolean(this.tabPane, "JTabbedPane.hasFullBorder", this.hasFullBorder);
/*  774 */     int sh = UIScale.scale(this.contentSeparatorHeight);
/*  775 */     Insets insets = hasFullBorder ? new Insets(sh, sh, sh, sh) : new Insets(sh, 0, 0, 0);
/*      */     
/*  777 */     Insets contentBorderInsets = new Insets(0, 0, 0, 0);
/*  778 */     rotateInsets(insets, contentBorderInsets, tabPlacement);
/*  779 */     return contentBorderInsets;
/*      */   }
/*      */ 
/*      */   
/*      */   protected int getTabLabelShiftX(int tabPlacement, int tabIndex, boolean isSelected) {
/*  784 */     if (isTabClosable(tabIndex)) {
/*  785 */       int shift = this.closeIcon.getIconWidth() / 2;
/*  786 */       return isLeftToRight() ? -shift : shift;
/*      */     } 
/*  788 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected) {
/*  793 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public void update(Graphics g, JComponent c) {
/*  798 */     this.oldRenderingHints = FlatUIUtils.setRenderingHints(g);
/*      */     
/*  800 */     super.update(g, c);
/*      */     
/*  802 */     FlatUIUtils.resetRenderingHints(g, this.oldRenderingHints);
/*  803 */     this.oldRenderingHints = null;
/*      */   }
/*      */ 
/*      */   
/*      */   public void paint(Graphics g, JComponent c) {
/*  808 */     if (hideTabArea()) {
/*      */       return;
/*      */     }
/*  811 */     ensureCurrentLayout();
/*      */     
/*  813 */     int tabPlacement = this.tabPane.getTabPlacement();
/*  814 */     int selectedIndex = this.tabPane.getSelectedIndex();
/*      */     
/*  816 */     paintContentBorder(g, tabPlacement, selectedIndex);
/*      */     
/*  818 */     if (!isScrollTabLayout()) {
/*  819 */       paintTabArea(g, tabPlacement, selectedIndex);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect) {
/*  826 */     Rectangle tabRect = rects[tabIndex];
/*  827 */     int x = tabRect.x;
/*  828 */     int y = tabRect.y;
/*  829 */     int w = tabRect.width;
/*  830 */     int h = tabRect.height;
/*  831 */     boolean isSelected = (tabIndex == this.tabPane.getSelectedIndex());
/*      */ 
/*      */     
/*  834 */     if (this.tabsOpaque || this.tabPane.isOpaque()) {
/*  835 */       paintTabBackground(g, tabPlacement, tabIndex, x, y, w, h, isSelected);
/*      */     }
/*      */     
/*  838 */     paintTabBorder(g, tabPlacement, tabIndex, x, y, w, h, isSelected);
/*      */ 
/*      */     
/*  841 */     if (isTabClosable(tabIndex)) {
/*  842 */       paintTabCloseButton(g, tabIndex, x, y, w, h);
/*      */     }
/*      */     
/*  845 */     if (isSelected) {
/*  846 */       paintTabSelection(g, tabPlacement, x, y, w, h);
/*      */     }
/*  848 */     if (this.tabPane.getTabComponentAt(tabIndex) != null) {
/*      */       return;
/*      */     }
/*      */     
/*  852 */     String title = this.tabPane.getTitleAt(tabIndex);
/*  853 */     Icon icon = getIconForTab(tabIndex);
/*  854 */     Font font = this.tabPane.getFont();
/*  855 */     FontMetrics metrics = this.tabPane.getFontMetrics(font);
/*  856 */     boolean isCompact = (icon != null && !isSelected && getTabWidthMode() == 2 && isHorizontalTabPlacement());
/*  857 */     if (isCompact)
/*  858 */       title = null; 
/*  859 */     String clippedTitle = layoutAndClipLabel(tabPlacement, metrics, tabIndex, title, icon, tabRect, iconRect, textRect, isSelected);
/*      */ 
/*      */     
/*  862 */     if (this.tabViewport != null && (tabPlacement == 1 || tabPlacement == 3)) {
/*  863 */       Rectangle viewRect = this.tabViewport.getViewRect();
/*  864 */       viewRect.width -= 4;
/*  865 */       if (!viewRect.contains(textRect)) {
/*  866 */         Rectangle r = viewRect.intersection(textRect);
/*  867 */         if (r.x > viewRect.x) {
/*  868 */           clippedTitle = JavaCompatibility.getClippedString(null, metrics, title, r.width);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  873 */     if (!isCompact)
/*  874 */       paintText(g, tabPlacement, font, metrics, tabIndex, clippedTitle, textRect, isSelected); 
/*  875 */     paintIcon(g, tabPlacement, tabIndex, icon, iconRect, isSelected);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
/*  882 */     g.setFont(font);
/*      */     
/*  884 */     FlatUIUtils.runWithoutRenderingHints(g, this.oldRenderingHints, () -> {
/*      */           Color color;
/*      */           View view = getTextViewForTab(tabIndex);
/*      */           if (view != null) {
/*      */             view.paint(g, textRect);
/*      */             return;
/*      */           } 
/*      */           if (this.tabPane.isEnabled() && this.tabPane.isEnabledAt(tabIndex)) {
/*      */             color = this.tabPane.getForegroundAt(tabIndex);
/*      */             if (isSelected && color instanceof UIResource && this.selectedForeground != null) {
/*      */               color = this.selectedForeground;
/*      */             }
/*      */           } else {
/*      */             color = this.disabledForeground;
/*      */           } 
/*      */           int mnemIndex = FlatLaf.isShowMnemonics() ? this.tabPane.getDisplayedMnemonicIndexAt(tabIndex) : -1;
/*      */           g.setColor(color);
/*      */           FlatUIUtils.drawStringUnderlineCharAt(this.tabPane, g, title, mnemIndex, textRect.x, textRect.y + metrics.getAscent());
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
/*  914 */     boolean enabled = this.tabPane.isEnabled();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  921 */     Color background = (enabled && this.tabPane.isEnabledAt(tabIndex) && getRolloverTab() == tabIndex) ? this.hoverColor : ((enabled && isSelected && FlatUIUtils.isPermanentFocusOwner(this.tabPane)) ? this.focusColor : ((this.selectedBackground != null && enabled && isSelected) ? this.selectedBackground : this.tabPane.getBackgroundAt(tabIndex)));
/*  922 */     g.setColor(FlatUIUtils.deriveColor(background, this.tabPane.getBackground()));
/*  923 */     g.fillRect(x, y, w, h);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
/*  931 */     if (FlatClientProperties.clientPropertyBoolean(this.tabPane, "JTabbedPane.showTabSeparators", this.showTabSeparators) && 
/*  932 */       !isLastInRun(tabIndex)) {
/*  933 */       paintTabSeparator(g, tabPlacement, x, y, w, h);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void paintTabCloseButton(Graphics g, int tabIndex, int x, int y, int w, int h) {
/*  938 */     if (this.tabCloseButton == null) {
/*  939 */       this.tabCloseButton = new TabCloseButton();
/*  940 */       this.tabCloseButton.setVisible(false);
/*      */     } 
/*      */ 
/*      */     
/*  944 */     boolean rollover = (tabIndex == getRolloverTab());
/*  945 */     ButtonModel bm = this.tabCloseButton.getModel();
/*  946 */     bm.setRollover((rollover && isRolloverTabClose()));
/*  947 */     bm.setPressed((rollover && isPressedTabClose()));
/*      */ 
/*      */     
/*  950 */     this.tabCloseButton.setBackground(this.tabPane.getBackground());
/*  951 */     this.tabCloseButton.setForeground(this.tabPane.getForeground());
/*      */ 
/*      */     
/*  954 */     Rectangle tabCloseRect = getTabCloseBounds(tabIndex, x, y, w, h, this.calcRect);
/*  955 */     this.closeIcon.paintIcon(this.tabCloseButton, g, tabCloseRect.x, tabCloseRect.y);
/*      */   }
/*      */   
/*      */   protected void paintTabSeparator(Graphics g, int tabPlacement, int x, int y, int w, int h) {
/*  959 */     float sepWidth = UIScale.scale(1.0F);
/*  960 */     float offset = this.tabSeparatorsFullHeight ? 0.0F : UIScale.scale(5.0F);
/*      */     
/*  962 */     g.setColor((this.tabSeparatorColor != null) ? this.tabSeparatorColor : this.contentAreaColor);
/*  963 */     if (tabPlacement == 2 || tabPlacement == 4) {
/*      */       
/*  965 */       ((Graphics2D)g).fill(new Rectangle2D.Float(x + offset, (y + h) - sepWidth, w - offset * 2.0F, sepWidth));
/*  966 */     } else if (isLeftToRight()) {
/*      */       
/*  968 */       ((Graphics2D)g).fill(new Rectangle2D.Float((x + w) - sepWidth, y + offset, sepWidth, h - offset * 2.0F));
/*      */     } else {
/*      */       
/*  971 */       ((Graphics2D)g).fill(new Rectangle2D.Float(x, y + offset, sepWidth, h - offset * 2.0F));
/*      */     } 
/*      */   }
/*      */   protected void paintTabSelection(Graphics g, int tabPlacement, int x, int y, int w, int h) {
/*      */     int sy, sx;
/*  976 */     g.setColor(this.tabPane.isEnabled() ? this.underlineColor : this.disabledUnderlineColor);
/*      */ 
/*      */     
/*  979 */     Insets contentInsets = getContentBorderInsets(tabPlacement);
/*  980 */     int tabSelectionHeight = UIScale.scale(this.tabSelectionHeight);
/*  981 */     switch (tabPlacement) {
/*      */       
/*      */       default:
/*  984 */         sy = y + h + contentInsets.top - tabSelectionHeight;
/*  985 */         g.fillRect(x, sy, w, tabSelectionHeight);
/*      */         return;
/*      */       
/*      */       case 3:
/*  989 */         g.fillRect(x, y - contentInsets.bottom, w, tabSelectionHeight);
/*      */         return;
/*      */       
/*      */       case 2:
/*  993 */         sx = x + w + contentInsets.left - tabSelectionHeight;
/*  994 */         g.fillRect(sx, y, tabSelectionHeight, h); return;
/*      */       case 4:
/*      */         break;
/*      */     } 
/*  998 */     g.fillRect(x - contentInsets.right, y, tabSelectionHeight, h);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
/* 1013 */     if (this.tabPane.getTabCount() <= 0 || this.contentSeparatorHeight == 0 || 
/*      */       
/* 1015 */       !FlatClientProperties.clientPropertyBoolean(this.tabPane, "JTabbedPane.showContentSeparator", true)) {
/*      */       return;
/*      */     }
/* 1018 */     Insets insets = this.tabPane.getInsets();
/* 1019 */     Insets tabAreaInsets = getTabAreaInsets(tabPlacement);
/*      */     
/* 1021 */     int x = insets.left;
/* 1022 */     int y = insets.top;
/* 1023 */     int w = this.tabPane.getWidth() - insets.right - insets.left;
/* 1024 */     int h = this.tabPane.getHeight() - insets.top - insets.bottom;
/*      */ 
/*      */     
/* 1027 */     switch (tabPlacement) {
/*      */       
/*      */       default:
/* 1030 */         y += calculateTabAreaHeight(tabPlacement, this.runCount, this.maxTabHeight);
/* 1031 */         y -= tabAreaInsets.bottom;
/* 1032 */         h -= y - insets.top;
/*      */         break;
/*      */       
/*      */       case 3:
/* 1036 */         h -= calculateTabAreaHeight(tabPlacement, this.runCount, this.maxTabHeight);
/* 1037 */         h += tabAreaInsets.top;
/*      */         break;
/*      */       
/*      */       case 2:
/* 1041 */         x += calculateTabAreaWidth(tabPlacement, this.runCount, this.maxTabWidth);
/* 1042 */         x -= tabAreaInsets.right;
/* 1043 */         w -= x - insets.left;
/*      */         break;
/*      */       
/*      */       case 4:
/* 1047 */         w -= calculateTabAreaWidth(tabPlacement, this.runCount, this.maxTabWidth);
/* 1048 */         w += tabAreaInsets.left;
/*      */         break;
/*      */     } 
/*      */ 
/*      */     
/* 1053 */     boolean hasFullBorder = FlatClientProperties.clientPropertyBoolean(this.tabPane, "JTabbedPane.hasFullBorder", this.hasFullBorder);
/* 1054 */     int sh = UIScale.scale(this.contentSeparatorHeight * 100);
/* 1055 */     Insets ci = new Insets(0, 0, 0, 0);
/* 1056 */     rotateInsets(hasFullBorder ? new Insets(sh, sh, sh, sh) : new Insets(sh, 0, 0, 0), ci, tabPlacement);
/*      */ 
/*      */     
/* 1059 */     g.setColor(this.contentAreaColor);
/* 1060 */     Path2D path = new Path2D.Float(0);
/* 1061 */     path.append(new Rectangle2D.Float(x, y, w, h), false);
/* 1062 */     path.append(new Rectangle2D.Float(x + ci.left / 100.0F, y + ci.top / 100.0F, w - ci.left / 100.0F - ci.right / 100.0F, h - ci.top / 100.0F - ci.bottom / 100.0F), false);
/*      */     
/* 1064 */     ((Graphics2D)g).fill(path);
/*      */ 
/*      */ 
/*      */     
/* 1068 */     if (isScrollTabLayout() && selectedIndex >= 0 && this.tabViewport != null) {
/* 1069 */       Rectangle tabRect = getTabBounds(this.tabPane, selectedIndex);
/*      */ 
/*      */ 
/*      */       
/* 1073 */       Shape oldClip = g.getClip();
/* 1074 */       Rectangle vr = this.tabViewport.getBounds();
/* 1075 */       if (isHorizontalTabPlacement()) {
/* 1076 */         g.clipRect(vr.x, 0, vr.width, this.tabPane.getHeight());
/*      */       } else {
/* 1078 */         g.clipRect(0, vr.y, this.tabPane.getWidth(), vr.height);
/*      */       } 
/* 1080 */       paintTabSelection(g, tabPlacement, tabRect.x, tabRect.y, tabRect.width, tabRect.height);
/* 1081 */       g.setClip(oldClip);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String layoutAndClipLabel(int tabPlacement, FontMetrics metrics, int tabIndex, String title, Icon icon, Rectangle tabRect, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
/*      */     int verticalTextPosition, horizontalTextPosition;
/* 1096 */     tabRect = FlatUIUtils.subtractInsets(tabRect, getTabInsets(tabPlacement, tabIndex));
/* 1097 */     if (isTabClosable(tabIndex)) {
/* 1098 */       tabRect.width -= this.closeIcon.getIconWidth();
/* 1099 */       if (!isLeftToRight()) {
/* 1100 */         tabRect.x += this.closeIcon.getIconWidth();
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1106 */     switch (FlatClientProperties.clientPropertyInt(this.tabPane, "JTabbedPane.tabIconPlacement", 10)) {
/*      */       default:
/* 1108 */         verticalTextPosition = 0; horizontalTextPosition = 11; break;
/* 1109 */       case 11: verticalTextPosition = 0; horizontalTextPosition = 10; break;
/* 1110 */       case 1: verticalTextPosition = 3; horizontalTextPosition = 0; break;
/* 1111 */       case 3: verticalTextPosition = 1; horizontalTextPosition = 0;
/*      */         break;
/*      */     } 
/*      */     
/* 1115 */     textRect.setBounds(0, 0, 0, 0);
/* 1116 */     iconRect.setBounds(0, 0, 0, 0);
/*      */ 
/*      */     
/* 1119 */     View view = getTextViewForTab(tabIndex);
/* 1120 */     if (view != null) {
/* 1121 */       this.tabPane.putClientProperty("html", view);
/*      */     }
/*      */     
/* 1124 */     String clippedTitle = SwingUtilities.layoutCompoundLabel(this.tabPane, metrics, title, icon, 0, 
/* 1125 */         getTabAlignment(tabIndex), verticalTextPosition, horizontalTextPosition, tabRect, iconRect, textRect, 
/* 1126 */         UIScale.scale(this.textIconGapUnscaled));
/*      */ 
/*      */     
/* 1129 */     this.tabPane.putClientProperty("html", (Object)null);
/*      */     
/* 1131 */     return clippedTitle;
/*      */   }
/*      */ 
/*      */   
/*      */   public int tabForCoordinate(JTabbedPane pane, int x, int y) {
/* 1136 */     if (this.moreTabsButton != null) {
/*      */       
/* 1138 */       Point viewPosition = this.tabViewport.getViewPosition();
/* 1139 */       x = x - this.tabViewport.getX() + viewPosition.x;
/* 1140 */       y = y - this.tabViewport.getY() + viewPosition.y;
/*      */ 
/*      */       
/* 1143 */       if (!this.tabViewport.getViewRect().contains(x, y)) {
/* 1144 */         return -1;
/*      */       }
/*      */     } 
/* 1147 */     return super.tabForCoordinate(pane, x, y);
/*      */   }
/*      */ 
/*      */   
/*      */   protected Rectangle getTabBounds(int tabIndex, Rectangle dest) {
/* 1152 */     if (this.moreTabsButton != null) {
/*      */       
/* 1154 */       dest.setBounds(this.rects[tabIndex]);
/*      */ 
/*      */       
/* 1157 */       Point viewPosition = this.tabViewport.getViewPosition();
/* 1158 */       dest.x = dest.x + this.tabViewport.getX() - viewPosition.x;
/* 1159 */       dest.y = dest.y + this.tabViewport.getY() - viewPosition.y;
/* 1160 */       return dest;
/*      */     } 
/* 1162 */     return super.getTabBounds(tabIndex, dest);
/*      */   }
/*      */   
/*      */   protected Rectangle getTabCloseBounds(int tabIndex, int x, int y, int w, int h, Rectangle dest) {
/* 1166 */     int iconWidth = this.closeIcon.getIconWidth();
/* 1167 */     int iconHeight = this.closeIcon.getIconHeight();
/* 1168 */     Insets tabInsets = getTabInsets(this.tabPane.getTabPlacement(), tabIndex);
/*      */ 
/*      */     
/* 1171 */     dest.x = isLeftToRight() ? (x + w - tabInsets.right / 3 * 2 - iconWidth) : (x + tabInsets.left / 3 * 2);
/*      */ 
/*      */     
/* 1174 */     dest.y = y + (h - iconHeight) / 2;
/* 1175 */     dest.width = iconWidth;
/* 1176 */     dest.height = iconHeight;
/* 1177 */     return dest;
/*      */   }
/*      */   
/*      */   protected Rectangle getTabCloseHitArea(int tabIndex) {
/* 1181 */     Rectangle tabRect = getTabBounds(this.tabPane, tabIndex);
/* 1182 */     Rectangle tabCloseRect = getTabCloseBounds(tabIndex, tabRect.x, tabRect.y, tabRect.width, tabRect.height, this.calcRect);
/* 1183 */     return new Rectangle(tabCloseRect.x, tabRect.y, tabCloseRect.width, tabRect.height);
/*      */   }
/*      */   
/*      */   protected boolean isTabClosable(int tabIndex) {
/* 1187 */     Object value = getTabClientProperty(tabIndex, "JTabbedPane.tabClosable");
/* 1188 */     return (value instanceof Boolean) ? ((Boolean)value).booleanValue() : false;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void closeTab(int tabIndex) {
/* 1193 */     Object callback = getTabClientProperty(tabIndex, "JTabbedPane.tabCloseCallback");
/* 1194 */     if (callback instanceof IntConsumer) {
/* 1195 */       ((IntConsumer)callback).accept(tabIndex);
/* 1196 */     } else if (callback instanceof BiConsumer) {
/* 1197 */       ((BiConsumer<JTabbedPane, Integer>)callback).accept(this.tabPane, Integer.valueOf(tabIndex));
/*      */     } else {
/* 1199 */       throw new RuntimeException("Missing tab close callback. Set client property 'JTabbedPane.tabCloseCallback' to a 'java.util.function.IntConsumer' or 'java.util.function.BiConsumer<JTabbedPane, Integer>'");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object getTabClientProperty(int tabIndex, String key) {
/* 1207 */     if (tabIndex < 0) {
/* 1208 */       return null;
/*      */     }
/* 1210 */     Component c = this.tabPane.getComponentAt(tabIndex);
/* 1211 */     if (c instanceof JComponent) {
/* 1212 */       Object value = ((JComponent)c).getClientProperty(key);
/* 1213 */       if (value != null)
/* 1214 */         return value; 
/*      */     } 
/* 1216 */     return this.tabPane.getClientProperty(key);
/*      */   }
/*      */   
/*      */   protected int getTabClientPropertyInt(int tabIndex, String key, int defaultValue) {
/* 1220 */     Object value = getTabClientProperty(tabIndex, key);
/* 1221 */     return (value instanceof Integer) ? ((Integer)value).intValue() : defaultValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void ensureCurrentLayout() {
/* 1227 */     getTabRunCount(this.tabPane);
/*      */   }
/*      */   
/*      */   private boolean isLastInRun(int tabIndex) {
/* 1231 */     int run = getRunForTab(this.tabPane.getTabCount(), tabIndex);
/* 1232 */     return (lastTabInRun(this.tabPane.getTabCount(), run) == tabIndex);
/*      */   }
/*      */   
/*      */   private boolean isScrollTabLayout() {
/* 1236 */     return (this.tabPane.getTabLayoutPolicy() == 1);
/*      */   }
/*      */   
/*      */   private boolean isLeftToRight() {
/* 1240 */     return this.tabPane.getComponentOrientation().isLeftToRight();
/*      */   }
/*      */   
/*      */   protected boolean isHorizontalTabPlacement() {
/* 1244 */     int tabPlacement = this.tabPane.getTabPlacement();
/* 1245 */     return (tabPlacement == 1 || tabPlacement == 3);
/*      */   }
/*      */   
/*      */   protected boolean isSmoothScrollingEnabled() {
/* 1249 */     if (!Animator.useAnimation()) {
/* 1250 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1255 */     return UIManager.getBoolean("ScrollPane.smoothScrolling");
/*      */   }
/*      */   
/*      */   protected boolean hideTabArea() {
/* 1259 */     return (this.tabPane.getTabCount() == 1 && this.leadingComponent == null && this.trailingComponent == null && 
/*      */ 
/*      */       
/* 1262 */       FlatClientProperties.clientPropertyBoolean(this.tabPane, "JTabbedPane.hideTabAreaWithOneTab", false));
/*      */   }
/*      */   
/*      */   protected int getTabsPopupPolicy() {
/* 1266 */     Object value = this.tabPane.getClientProperty("JTabbedPane.tabsPopupPolicy");
/*      */     
/* 1268 */     return (value instanceof String) ? 
/* 1269 */       parseTabsPopupPolicy((String)value) : this.tabsPopupPolicy;
/*      */   }
/*      */ 
/*      */   
/*      */   protected int getScrollButtonsPolicy() {
/* 1274 */     Object value = this.tabPane.getClientProperty("JTabbedPane.scrollButtonsPolicy");
/*      */     
/* 1276 */     return (value instanceof String) ? 
/* 1277 */       parseScrollButtonsPolicy((String)value) : this.scrollButtonsPolicy;
/*      */   }
/*      */ 
/*      */   
/*      */   protected int getScrollButtonsPlacement() {
/* 1282 */     Object value = this.tabPane.getClientProperty("JTabbedPane.scrollButtonsPlacement");
/*      */     
/* 1284 */     return (value instanceof String) ? 
/* 1285 */       parseScrollButtonsPlacement((String)value) : this.scrollButtonsPlacement;
/*      */   }
/*      */ 
/*      */   
/*      */   protected int getTabAreaAlignment() {
/* 1290 */     Object value = this.tabPane.getClientProperty("JTabbedPane.tabAreaAlignment");
/* 1291 */     if (value instanceof Integer) {
/* 1292 */       return ((Integer)value).intValue();
/*      */     }
/* 1294 */     return (value instanceof String) ? 
/* 1295 */       parseAlignment((String)value, 10) : this.tabAreaAlignment;
/*      */   }
/*      */ 
/*      */   
/*      */   protected int getTabAlignment(int tabIndex) {
/* 1300 */     Object value = getTabClientProperty(tabIndex, "JTabbedPane.tabAlignment");
/* 1301 */     if (value instanceof Integer) {
/* 1302 */       return ((Integer)value).intValue();
/*      */     }
/* 1304 */     return (value instanceof String) ? 
/* 1305 */       parseAlignment((String)value, 0) : this.tabAlignment;
/*      */   }
/*      */ 
/*      */   
/*      */   protected int getTabWidthMode() {
/* 1310 */     Object value = this.tabPane.getClientProperty("JTabbedPane.tabWidthMode");
/*      */     
/* 1312 */     return (value instanceof String) ? 
/* 1313 */       parseTabWidthMode((String)value) : this.tabWidthMode;
/*      */   }
/*      */ 
/*      */   
/*      */   protected static int parseTabsPopupPolicy(String str) {
/* 1318 */     if (str == null) {
/* 1319 */       return 2;
/*      */     }
/* 1321 */     switch (str)
/*      */     { default:
/* 1323 */         return 2;
/* 1324 */       case "never": break; }  return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   protected static int parseScrollButtonsPolicy(String str) {
/* 1329 */     if (str == null) {
/* 1330 */       return 3;
/*      */     }
/* 1332 */     switch (str)
/*      */     { default:
/* 1334 */         return 3;
/* 1335 */       case "asNeeded": return 2;
/* 1336 */       case "never": break; }  return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   protected static int parseScrollButtonsPlacement(String str) {
/* 1341 */     if (str == null) {
/* 1342 */       return 100;
/*      */     }
/* 1344 */     switch (str)
/*      */     { default:
/* 1346 */         return 100;
/* 1347 */       case "trailing": break; }  return 11;
/*      */   }
/*      */ 
/*      */   
/*      */   protected static int parseAlignment(String str, int defaultValue) {
/* 1352 */     if (str == null) {
/* 1353 */       return defaultValue;
/*      */     }
/* 1355 */     switch (str) { case "leading":
/* 1356 */         return 10;
/* 1357 */       case "trailing": return 11;
/* 1358 */       case "center": return 0;
/* 1359 */       case "fill": return 100; }
/* 1360 */      return defaultValue;
/*      */   }
/*      */ 
/*      */   
/*      */   protected static int parseTabWidthMode(String str) {
/* 1365 */     if (str == null) {
/* 1366 */       return 0;
/*      */     }
/* 1368 */     switch (str)
/*      */     { default:
/* 1370 */         return 0;
/* 1371 */       case "equal": return 1;
/* 1372 */       case "compact": break; }  return 2;
/*      */   }
/*      */ 
/*      */   
/*      */   private void runWithOriginalLayoutManager(Runnable runnable) {
/* 1377 */     LayoutManager layout = this.tabPane.getLayout();
/* 1378 */     if (layout instanceof FlatTabbedPaneScrollLayout) {
/*      */ 
/*      */       
/* 1381 */       this.tabPane.setLayout(((FlatTabbedPaneScrollLayout)layout).delegate);
/* 1382 */       runnable.run();
/* 1383 */       this.tabPane.setLayout(layout);
/*      */     } else {
/* 1385 */       runnable.run();
/*      */     } 
/*      */   }
/*      */   protected void ensureSelectedTabIsVisibleLater() {
/* 1389 */     EventQueue.invokeLater(() -> ensureSelectedTabIsVisible());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void ensureSelectedTabIsVisible() {
/* 1395 */     if (this.tabPane == null || this.tabViewport == null) {
/*      */       return;
/*      */     }
/* 1398 */     ensureCurrentLayout();
/*      */     
/* 1400 */     int selectedIndex = this.tabPane.getSelectedIndex();
/* 1401 */     if (selectedIndex < 0 || selectedIndex >= this.rects.length) {
/*      */       return;
/*      */     }
/* 1404 */     ((JComponent)this.tabViewport.getView()).scrollRectToVisible((Rectangle)this.rects[selectedIndex].clone());
/*      */   }
/*      */   
/*      */   private int getLeadingPreferredWidth() {
/* 1408 */     return (this.leadingComponent != null) ? (this.leadingComponent.getPreferredSize()).width : 0;
/*      */   }
/*      */   
/*      */   private int getLeadingPreferredHeight() {
/* 1412 */     return (this.leadingComponent != null) ? (this.leadingComponent.getPreferredSize()).height : 0;
/*      */   }
/*      */   
/*      */   private int getTrailingPreferredWidth() {
/* 1416 */     return (this.trailingComponent != null) ? (this.trailingComponent.getPreferredSize()).width : 0;
/*      */   }
/*      */   
/*      */   private int getTrailingPreferredHeight() {
/* 1420 */     return (this.trailingComponent != null) ? (this.trailingComponent.getPreferredSize()).height : 0;
/*      */   }
/*      */   
/*      */   private void shiftTabs(int sx, int sy) {
/* 1424 */     if (sx == 0 && sy == 0) {
/*      */       return;
/*      */     }
/* 1427 */     for (int i = 0; i < this.rects.length; i++) {
/*      */       
/* 1429 */       (this.rects[i]).x += sx;
/* 1430 */       (this.rects[i]).y += sy;
/*      */ 
/*      */       
/* 1433 */       Component c = this.tabPane.getTabComponentAt(i);
/* 1434 */       if (c != null)
/* 1435 */         c.setLocation(c.getX() + sx, c.getY() + sy); 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void stretchTabsWidth(int sw, boolean leftToRight) {
/* 1440 */     int rsw = sw / this.rects.length;
/* 1441 */     int x = (this.rects[0]).x - (leftToRight ? 0 : rsw);
/* 1442 */     for (int i = 0; i < this.rects.length; i++) {
/*      */       
/* 1444 */       Component c = this.tabPane.getTabComponentAt(i);
/* 1445 */       if (c != null) {
/* 1446 */         c.setLocation(x + c.getX() - (this.rects[i]).x + rsw / 2, c.getY());
/*      */       }
/*      */       
/* 1449 */       (this.rects[i]).x = x;
/* 1450 */       (this.rects[i]).width += rsw;
/*      */       
/* 1452 */       if (leftToRight) {
/* 1453 */         x += (this.rects[i]).width;
/* 1454 */       } else if (i + 1 < this.rects.length) {
/* 1455 */         x = (this.rects[i]).x - (this.rects[i + 1]).width - rsw;
/*      */       } 
/*      */     } 
/*      */     
/* 1459 */     int diff = sw - rsw * this.rects.length;
/* 1460 */     (this.rects[this.rects.length - 1]).width += diff;
/* 1461 */     if (!leftToRight)
/* 1462 */       (this.rects[this.rects.length - 1]).x -= diff; 
/*      */   }
/*      */   
/*      */   private void stretchTabsHeight(int sh) {
/* 1466 */     int rsh = sh / this.rects.length;
/* 1467 */     int y = (this.rects[0]).y;
/* 1468 */     for (int i = 0; i < this.rects.length; i++) {
/*      */       
/* 1470 */       Component c = this.tabPane.getTabComponentAt(i);
/* 1471 */       if (c != null) {
/* 1472 */         c.setLocation(c.getX(), y + c.getY() - (this.rects[i]).y + rsh / 2);
/*      */       }
/*      */       
/* 1475 */       (this.rects[i]).y = y;
/* 1476 */       (this.rects[i]).height += rsh;
/*      */       
/* 1478 */       y += (this.rects[i]).height;
/*      */     } 
/*      */ 
/*      */     
/* 1482 */     (this.rects[this.rects.length - 1]).height += sh - rsh * this.rects.length;
/*      */   }
/*      */   
/*      */   private int rectsTotalWidth(boolean leftToRight) {
/* 1486 */     int last = this.rects.length - 1;
/* 1487 */     return leftToRight ? ((this.rects[last]).x + (this.rects[last]).width - (this.rects[0]).x) : ((this.rects[0]).x + (this.rects[0]).width - (this.rects[last]).x);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private int rectsTotalHeight() {
/* 1493 */     int last = this.rects.length - 1;
/* 1494 */     return (this.rects[last]).y + (this.rects[last]).height - (this.rects[0]).y;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class TabCloseButton
/*      */     extends JButton
/*      */     implements UIResource
/*      */   {
/*      */     private TabCloseButton() {}
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class ContainerUIResource
/*      */     extends JPanel
/*      */     implements UIResource
/*      */   {
/*      */     private ContainerUIResource(Component c) {
/* 1514 */       super(new BorderLayout());
/* 1515 */       add(c);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected class FlatTabAreaButton
/*      */     extends FlatArrowButton
/*      */   {
/*      */     public FlatTabAreaButton(int direction) {
/* 1525 */       super(direction, FlatTabbedPaneUI.this.arrowType, FlatTabbedPaneUI.this.foreground, FlatTabbedPaneUI.this.disabledForeground, (Color)null, FlatTabbedPaneUI.this.buttonHoverBackground, (Color)null, FlatTabbedPaneUI.this.buttonPressedBackground);
/*      */ 
/*      */       
/* 1528 */       setArrowWidth(10);
/*      */     }
/*      */ 
/*      */     
/*      */     protected Color deriveBackground(Color background) {
/* 1533 */       return FlatUIUtils.deriveColor(background, FlatTabbedPaneUI.this.tabPane.getBackground());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void paint(Graphics g) {
/* 1539 */       if (FlatTabbedPaneUI.this.tabsOpaque || FlatTabbedPaneUI.this.tabPane.isOpaque()) {
/* 1540 */         g.setColor(FlatTabbedPaneUI.this.tabPane.getBackground());
/* 1541 */         g.fillRect(0, 0, getWidth(), getHeight());
/*      */       } 
/*      */       
/* 1544 */       super.paint(g);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     protected void paintBackground(Graphics2D g) {
/* 1550 */       Insets insets = new Insets(0, 0, 0, 0);
/* 1551 */       FlatTabbedPaneUI.rotateInsets(FlatTabbedPaneUI.this.buttonInsets, insets, FlatTabbedPaneUI.this.tabPane.getTabPlacement());
/*      */ 
/*      */       
/* 1554 */       int top = UIScale.scale2(insets.top);
/* 1555 */       int left = UIScale.scale2(insets.left);
/* 1556 */       int bottom = UIScale.scale2(insets.bottom);
/* 1557 */       int right = UIScale.scale2(insets.right);
/*      */       
/* 1559 */       FlatUIUtils.paintComponentBackground(g, left, top, 
/* 1560 */           getWidth() - left - right, 
/* 1561 */           getHeight() - top - bottom, 0.0F, 
/* 1562 */           UIScale.scale(FlatTabbedPaneUI.this.buttonArc));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected class FlatMoreTabsButton
/*      */     extends FlatTabAreaButton
/*      */     implements ActionListener, PopupMenuListener
/*      */   {
/*      */     private boolean popupVisible;
/*      */ 
/*      */     
/*      */     public FlatMoreTabsButton() {
/* 1575 */       super(5);
/*      */       
/* 1577 */       updateDirection();
/* 1578 */       setToolTipText(FlatTabbedPaneUI.this.moreTabsButtonToolTipText);
/* 1579 */       addActionListener(this);
/*      */     }
/*      */     
/*      */     protected void updateDirection() {
/*      */       int direction;
/* 1584 */       switch (FlatTabbedPaneUI.this.tabPane.getTabPlacement()) {
/*      */         default:
/* 1586 */           direction = 5; break;
/* 1587 */         case 3: direction = 1; break;
/* 1588 */         case 2: direction = 3; break;
/* 1589 */         case 4: direction = 7;
/*      */           break;
/*      */       } 
/* 1592 */       setDirection(direction);
/*      */     }
/*      */ 
/*      */     
/*      */     public Dimension getPreferredSize() {
/* 1597 */       Dimension size = super.getPreferredSize();
/* 1598 */       boolean horizontal = (this.direction == 5 || this.direction == 1);
/* 1599 */       int margin = UIScale.scale(8);
/* 1600 */       return new Dimension(size.width + (horizontal ? margin : 0), size.height + (horizontal ? 0 : margin));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void paint(Graphics g) {
/* 1608 */       if (this.direction == 3 || this.direction == 7) {
/* 1609 */         int xoffset = Math.max(UIScale.unscale((getWidth() - getHeight()) / 2) - 4, 0);
/* 1610 */         setXOffset((this.direction == 3) ? xoffset : -xoffset);
/*      */       } else {
/* 1612 */         setXOffset(0);
/*      */       } 
/* 1614 */       super.paint(g);
/*      */     }
/*      */ 
/*      */     
/*      */     protected boolean isHover() {
/* 1619 */       return (super.isHover() || this.popupVisible);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformed(ActionEvent e) {
/* 1624 */       if (FlatTabbedPaneUI.this.tabViewport == null) {
/*      */         return;
/*      */       }
/*      */       
/* 1628 */       JPopupMenu popupMenu = new JPopupMenu();
/* 1629 */       popupMenu.addPopupMenuListener(this);
/* 1630 */       Rectangle viewRect = FlatTabbedPaneUI.this.tabViewport.getViewRect();
/* 1631 */       int lastIndex = -1;
/* 1632 */       for (int i = 0; i < FlatTabbedPaneUI.this.rects.length; i++) {
/* 1633 */         if (!viewRect.contains(FlatTabbedPaneUI.this.rects[i])) {
/*      */           
/* 1635 */           if (lastIndex >= 0 && lastIndex + 1 != i)
/* 1636 */             popupMenu.addSeparator(); 
/* 1637 */           lastIndex = i;
/*      */ 
/*      */           
/* 1640 */           popupMenu.add(createTabMenuItem(i));
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1645 */       int buttonWidth = getWidth();
/* 1646 */       int buttonHeight = getHeight();
/* 1647 */       Dimension popupSize = popupMenu.getPreferredSize();
/*      */       
/* 1649 */       int x = FlatTabbedPaneUI.this.isLeftToRight() ? (buttonWidth - popupSize.width) : 0;
/* 1650 */       int y = buttonHeight - popupSize.height;
/* 1651 */       switch (FlatTabbedPaneUI.this.tabPane.getTabPlacement()) {
/*      */         default:
/* 1653 */           y = buttonHeight; break;
/* 1654 */         case 3: y = -popupSize.height; break;
/* 1655 */         case 2: x = buttonWidth; break;
/* 1656 */         case 4: x = -popupSize.width;
/*      */           break;
/*      */       } 
/*      */       
/* 1660 */       popupMenu.show(this, x, y);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected JMenuItem createTabMenuItem(int tabIndex) {
/* 1670 */       String title = FlatTabbedPaneUI.this.tabPane.getTitleAt(tabIndex);
/* 1671 */       if (StringUtils.isEmpty(title)) {
/* 1672 */         Component tabComp = FlatTabbedPaneUI.this.tabPane.getTabComponentAt(tabIndex);
/* 1673 */         if (tabComp != null)
/* 1674 */           title = findTabTitle(tabComp); 
/* 1675 */         if (StringUtils.isEmpty(title))
/* 1676 */           title = FlatTabbedPaneUI.this.tabPane.getAccessibleContext().getAccessibleChild(tabIndex).getAccessibleContext().getAccessibleName(); 
/* 1677 */         if (StringUtils.isEmpty(title) && tabComp instanceof Accessible)
/* 1678 */           title = findTabTitleInAccessible((Accessible)tabComp); 
/* 1679 */         if (StringUtils.isEmpty(title)) {
/* 1680 */           title = (tabIndex + 1) + ". Tab";
/*      */         }
/*      */       } 
/* 1683 */       JMenuItem menuItem = new JMenuItem(title, FlatTabbedPaneUI.this.tabPane.getIconAt(tabIndex));
/* 1684 */       menuItem.setDisabledIcon(FlatTabbedPaneUI.this.tabPane.getDisabledIconAt(tabIndex));
/* 1685 */       menuItem.setToolTipText(FlatTabbedPaneUI.this.tabPane.getToolTipTextAt(tabIndex));
/*      */       
/* 1687 */       Color foregroundAt = FlatTabbedPaneUI.this.tabPane.getForegroundAt(tabIndex);
/* 1688 */       if (foregroundAt != FlatTabbedPaneUI.this.tabPane.getForeground()) {
/* 1689 */         menuItem.setForeground(foregroundAt);
/*      */       }
/* 1691 */       Color backgroundAt = FlatTabbedPaneUI.this.tabPane.getBackgroundAt(tabIndex);
/* 1692 */       if (backgroundAt != FlatTabbedPaneUI.this.tabPane.getBackground()) {
/* 1693 */         menuItem.setBackground(backgroundAt);
/* 1694 */         menuItem.setOpaque(true);
/*      */       } 
/*      */       
/* 1697 */       if (!FlatTabbedPaneUI.this.tabPane.isEnabledAt(tabIndex)) {
/* 1698 */         menuItem.setEnabled(false);
/*      */       }
/* 1700 */       menuItem.addActionListener(e -> selectTab(tabIndex));
/* 1701 */       return menuItem;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private String findTabTitle(Component c) {
/* 1708 */       String title = null;
/* 1709 */       if (c instanceof JLabel) {
/* 1710 */         title = ((JLabel)c).getText();
/* 1711 */       } else if (c instanceof JTextComponent) {
/* 1712 */         title = ((JTextComponent)c).getText();
/*      */       } 
/* 1714 */       if (!StringUtils.isEmpty(title)) {
/* 1715 */         return title;
/*      */       }
/* 1717 */       if (c instanceof Container) {
/* 1718 */         for (Component child : ((Container)c).getComponents()) {
/* 1719 */           title = findTabTitle(child);
/* 1720 */           if (title != null) {
/* 1721 */             return title;
/*      */           }
/*      */         } 
/*      */       }
/* 1725 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private String findTabTitleInAccessible(Accessible accessible) {
/* 1732 */       AccessibleContext context = accessible.getAccessibleContext();
/* 1733 */       if (context == null) {
/* 1734 */         return null;
/*      */       }
/* 1736 */       String title = context.getAccessibleName();
/* 1737 */       if (!StringUtils.isEmpty(title)) {
/* 1738 */         return title;
/*      */       }
/* 1740 */       int childrenCount = context.getAccessibleChildrenCount();
/* 1741 */       for (int i = 0; i < childrenCount; i++) {
/* 1742 */         title = findTabTitleInAccessible(context.getAccessibleChild(i));
/* 1743 */         if (title != null) {
/* 1744 */           return title;
/*      */         }
/*      */       } 
/* 1747 */       return null;
/*      */     }
/*      */     
/*      */     protected void selectTab(int tabIndex) {
/* 1751 */       FlatTabbedPaneUI.this.tabPane.setSelectedIndex(tabIndex);
/* 1752 */       FlatTabbedPaneUI.this.ensureSelectedTabIsVisible();
/*      */     }
/*      */ 
/*      */     
/*      */     public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
/* 1757 */       this.popupVisible = true;
/* 1758 */       repaint();
/*      */     }
/*      */ 
/*      */     
/*      */     public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
/* 1763 */       this.popupVisible = false;
/* 1764 */       repaint();
/*      */     }
/*      */ 
/*      */     
/*      */     public void popupMenuCanceled(PopupMenuEvent e) {
/* 1769 */       this.popupVisible = false;
/* 1770 */       repaint();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected class FlatScrollableTabButton
/*      */     extends FlatTabAreaButton
/*      */     implements MouseListener
/*      */   {
/*      */     private Timer autoRepeatTimer;
/*      */ 
/*      */     
/*      */     protected FlatScrollableTabButton(int direction) {
/* 1783 */       super(direction);
/*      */       
/* 1785 */       addMouseListener(this);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void fireActionPerformed(ActionEvent event) {
/* 1790 */       FlatTabbedPaneUI.this.runWithOriginalLayoutManager(() -> super.fireActionPerformed(event));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void mousePressed(MouseEvent e) {
/* 1797 */       if (SwingUtilities.isLeftMouseButton(e) && isEnabled()) {
/* 1798 */         if (this.autoRepeatTimer == null) {
/*      */           
/* 1800 */           this.autoRepeatTimer = new Timer(60, e2 -> {
/*      */                 if (isEnabled())
/*      */                   doClick(); 
/*      */               });
/* 1804 */           this.autoRepeatTimer.setInitialDelay(300);
/*      */         } 
/*      */         
/* 1807 */         this.autoRepeatTimer.start();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void mouseReleased(MouseEvent e) {
/* 1813 */       if (this.autoRepeatTimer != null) {
/* 1814 */         this.autoRepeatTimer.stop();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public void mouseClicked(MouseEvent e) {}
/*      */ 
/*      */     
/*      */     public void mouseEntered(MouseEvent e) {
/* 1823 */       if (this.autoRepeatTimer != null && isPressed()) {
/* 1824 */         this.autoRepeatTimer.start();
/*      */       }
/*      */     }
/*      */     
/*      */     public void mouseExited(MouseEvent e) {
/* 1829 */       if (this.autoRepeatTimer != null) {
/* 1830 */         this.autoRepeatTimer.stop();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected class FlatWheelTabScroller
/*      */     extends MouseAdapter
/*      */   {
/*      */     private int lastMouseX;
/*      */     
/*      */     private int lastMouseY;
/*      */     
/*      */     private boolean inViewport;
/*      */     private boolean scrolled;
/*      */     private Timer rolloverTimer;
/*      */     private Timer exitedTimer;
/*      */     private Animator animator;
/*      */     private Point startViewPosition;
/*      */     private Point targetViewPosition;
/*      */     
/*      */     protected void uninstall() {
/* 1852 */       if (this.rolloverTimer != null)
/* 1853 */         this.rolloverTimer.stop(); 
/* 1854 */       if (this.exitedTimer != null)
/* 1855 */         this.exitedTimer.stop(); 
/* 1856 */       if (this.animator != null) {
/* 1857 */         this.animator.cancel();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public void mouseWheelMoved(MouseWheelEvent e) {
/* 1863 */       if ((FlatTabbedPaneUI.this.tabPane.getMouseWheelListeners()).length > 1) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/* 1868 */       if (!isInViewport(e.getX(), e.getY())) {
/*      */         return;
/*      */       }
/* 1871 */       this.lastMouseX = e.getX();
/* 1872 */       this.lastMouseY = e.getY();
/*      */       
/* 1874 */       double preciseWheelRotation = e.getPreciseWheelRotation();
/* 1875 */       boolean isPreciseWheel = (preciseWheelRotation != 0.0D && preciseWheelRotation != e.getWheelRotation());
/* 1876 */       int amount = (int)(FlatTabbedPaneUI.this.maxTabHeight * preciseWheelRotation);
/*      */ 
/*      */       
/* 1879 */       if (amount == 0) {
/* 1880 */         if (preciseWheelRotation > 0.0D) {
/* 1881 */           amount = 1;
/* 1882 */         } else if (preciseWheelRotation < 0.0D) {
/* 1883 */           amount = -1;
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1889 */       Point viewPosition = (this.targetViewPosition != null) ? this.targetViewPosition : FlatTabbedPaneUI.this.tabViewport.getViewPosition();
/* 1890 */       Dimension viewSize = FlatTabbedPaneUI.this.tabViewport.getViewSize();
/* 1891 */       boolean horizontal = FlatTabbedPaneUI.this.isHorizontalTabPlacement();
/* 1892 */       int x = viewPosition.x;
/* 1893 */       int y = viewPosition.y;
/* 1894 */       if (horizontal) {
/* 1895 */         x += FlatTabbedPaneUI.this.isLeftToRight() ? amount : -amount;
/*      */       } else {
/* 1897 */         y += amount;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1905 */       if ((isPreciseWheel && FlatTabbedPaneUI.this
/* 1906 */         .getScrollButtonsPlacement() == 100 && FlatTabbedPaneUI.this
/* 1907 */         .getScrollButtonsPolicy() == 3 && (FlatTabbedPaneUI.this
/* 1908 */         .isLeftToRight() || !horizontal)) || FlatTabbedPaneUI.this
/* 1909 */         .scrollBackwardButtonPrefSize != null)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1920 */         if (horizontal) {
/*      */           
/* 1922 */           if (viewPosition.x == 0 && x > 0) {
/* 1923 */             x += FlatTabbedPaneUI.this.scrollBackwardButtonPrefSize.width;
/* 1924 */           } else if (amount < 0 && x <= FlatTabbedPaneUI.this.scrollBackwardButtonPrefSize.width) {
/* 1925 */             x = 0;
/*      */           } 
/* 1927 */         } else if (viewPosition.y == 0 && y > 0) {
/* 1928 */           y += FlatTabbedPaneUI.this.scrollBackwardButtonPrefSize.height;
/* 1929 */         } else if (amount < 0 && y <= FlatTabbedPaneUI.this.scrollBackwardButtonPrefSize.height) {
/* 1930 */           y = 0;
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/* 1935 */       if (horizontal) {
/* 1936 */         x = Math.min(Math.max(x, 0), viewSize.width - FlatTabbedPaneUI.this.tabViewport.getWidth());
/*      */       } else {
/* 1938 */         y = Math.min(Math.max(y, 0), viewSize.height - FlatTabbedPaneUI.this.tabViewport.getHeight());
/*      */       } 
/*      */       
/* 1941 */       Point newViewPosition = new Point(x, y);
/* 1942 */       if (newViewPosition.equals(viewPosition)) {
/*      */         return;
/*      */       }
/*      */       
/* 1946 */       if (isPreciseWheel) {
/*      */ 
/*      */ 
/*      */         
/* 1950 */         if (this.animator != null) {
/* 1951 */           this.animator.stop();
/*      */         }
/* 1953 */         FlatTabbedPaneUI.this.tabViewport.setViewPosition(newViewPosition);
/* 1954 */         updateRolloverDelayed();
/*      */       } else {
/* 1956 */         setViewPositionAnimated(newViewPosition);
/*      */       } 
/* 1958 */       this.scrolled = true;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void setViewPositionAnimated(Point viewPosition) {
/* 1963 */       if (viewPosition.equals(FlatTabbedPaneUI.this.tabViewport.getViewPosition())) {
/*      */         return;
/*      */       }
/*      */       
/* 1967 */       if (!FlatTabbedPaneUI.this.isSmoothScrollingEnabled()) {
/* 1968 */         FlatTabbedPaneUI.this.tabViewport.setViewPosition(viewPosition);
/* 1969 */         updateRolloverDelayed();
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/* 1974 */       this.startViewPosition = FlatTabbedPaneUI.this.tabViewport.getViewPosition();
/* 1975 */       this.targetViewPosition = viewPosition;
/*      */ 
/*      */       
/* 1978 */       if (this.animator == null) {
/*      */         
/* 1980 */         int duration = 200;
/* 1981 */         int resolution = 10;
/*      */         
/* 1983 */         this.animator = new Animator(duration, fraction -> {
/*      */               if (FlatTabbedPaneUI.this.tabViewport == null || !FlatTabbedPaneUI.this.tabViewport.isShowing()) {
/*      */                 this.animator.stop();
/*      */                 
/*      */                 return;
/*      */               } 
/*      */               
/*      */               int x = this.startViewPosition.x + Math.round((this.targetViewPosition.x - this.startViewPosition.x) * fraction);
/*      */               
/*      */               int y = this.startViewPosition.y + Math.round((this.targetViewPosition.y - this.startViewPosition.y) * fraction);
/*      */               FlatTabbedPaneUI.this.tabViewport.setViewPosition(new Point(x, y));
/*      */             }() -> {
/*      */               this.startViewPosition = this.targetViewPosition = null;
/*      */               if (FlatTabbedPaneUI.this.tabPane != null) {
/*      */                 FlatTabbedPaneUI.this.setRolloverTab(this.lastMouseX, this.lastMouseY);
/*      */               }
/*      */             });
/* 2000 */         this.animator.setResolution(resolution);
/* 2001 */         this.animator.setInterpolator((Animator.Interpolator)new CubicBezierEasing(0.5F, 0.5F, 0.5F, 1.0F));
/*      */       } 
/*      */ 
/*      */       
/* 2005 */       this.animator.restart();
/*      */     }
/*      */     
/*      */     protected void updateRolloverDelayed() {
/* 2009 */       FlatTabbedPaneUI.this.blockRollover = true;
/*      */ 
/*      */       
/* 2012 */       int oldIndex = FlatTabbedPaneUI.this.getRolloverTab();
/* 2013 */       if (oldIndex >= 0) {
/* 2014 */         int index = FlatTabbedPaneUI.this.tabForCoordinate(FlatTabbedPaneUI.this.tabPane, this.lastMouseX, this.lastMouseY);
/* 2015 */         if (index >= 0 && index != oldIndex) {
/*      */           
/* 2017 */           FlatTabbedPaneUI.this.blockRollover = false;
/* 2018 */           FlatTabbedPaneUI.this.setRolloverTab(-1);
/* 2019 */           FlatTabbedPaneUI.this.blockRollover = true;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 2024 */       if (this.rolloverTimer == null) {
/* 2025 */         this.rolloverTimer = new Timer(150, e -> {
/*      */               FlatTabbedPaneUI.this.blockRollover = false;
/*      */               
/*      */               if (FlatTabbedPaneUI.this.tabPane != null) {
/*      */                 FlatTabbedPaneUI.this.setRolloverTab(this.lastMouseX, this.lastMouseY);
/*      */               }
/*      */             });
/* 2032 */         this.rolloverTimer.setRepeats(false);
/*      */       } 
/*      */ 
/*      */       
/* 2036 */       this.rolloverTimer.restart();
/*      */     }
/*      */ 
/*      */     
/*      */     public void mouseMoved(MouseEvent e) {
/* 2041 */       checkViewportExited(e.getX(), e.getY());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void mouseExited(MouseEvent e) {
/* 2048 */       checkViewportExited(e.getX(), e.getY());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void mousePressed(MouseEvent e) {
/* 2054 */       FlatTabbedPaneUI.this.setRolloverTab(e.getX(), e.getY());
/*      */     }
/*      */     
/*      */     protected boolean isInViewport(int x, int y) {
/* 2058 */       return (FlatTabbedPaneUI.this.tabViewport != null && FlatTabbedPaneUI.this.tabViewport.getBounds().contains(x, y));
/*      */     }
/*      */     
/*      */     protected void checkViewportExited(int x, int y) {
/* 2062 */       this.lastMouseX = x;
/* 2063 */       this.lastMouseY = y;
/*      */       
/* 2065 */       boolean wasInViewport = this.inViewport;
/* 2066 */       this.inViewport = isInViewport(x, y);
/*      */       
/* 2068 */       if (this.inViewport != wasInViewport)
/* 2069 */         if (!this.inViewport) {
/* 2070 */           viewportExited();
/* 2071 */         } else if (this.exitedTimer != null) {
/* 2072 */           this.exitedTimer.stop();
/*      */         }  
/*      */     }
/*      */     
/*      */     protected void viewportExited() {
/* 2077 */       if (!this.scrolled) {
/*      */         return;
/*      */       }
/* 2080 */       if (this.exitedTimer == null) {
/* 2081 */         this.exitedTimer = new Timer(500, e -> ensureSelectedTabVisible());
/* 2082 */         this.exitedTimer.setRepeats(false);
/*      */       } 
/*      */       
/* 2085 */       this.exitedTimer.start();
/*      */     }
/*      */ 
/*      */     
/*      */     protected void ensureSelectedTabVisible() {
/* 2090 */       if (FlatTabbedPaneUI.this.tabPane == null || FlatTabbedPaneUI.this.tabViewport == null) {
/*      */         return;
/*      */       }
/* 2093 */       if (!this.scrolled || FlatTabbedPaneUI.this.tabViewport == null)
/*      */         return; 
/* 2095 */       this.scrolled = false;
/*      */ 
/*      */       
/* 2098 */       FlatTabbedPaneUI.this.ensureSelectedTabIsVisible();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private class Handler
/*      */     implements MouseListener, MouseMotionListener, PropertyChangeListener, ChangeListener, ComponentListener, ContainerListener
/*      */   {
/*      */     MouseListener mouseDelegate;
/*      */     
/*      */     PropertyChangeListener propertyChangeDelegate;
/*      */     
/*      */     ChangeListener changeDelegate;
/*      */     
/* 2112 */     private final PropertyChangeListener contentListener = this::contentPropertyChange;
/*      */     
/* 2114 */     private int pressedTabIndex = -1;
/* 2115 */     private int lastTipTabIndex = -1;
/*      */     private String lastTip;
/*      */     
/*      */     void installListeners() {
/* 2119 */       FlatTabbedPaneUI.this.tabPane.addMouseMotionListener(this);
/* 2120 */       FlatTabbedPaneUI.this.tabPane.addComponentListener(this);
/* 2121 */       FlatTabbedPaneUI.this.tabPane.addContainerListener(this);
/*      */       
/* 2123 */       for (Component c : FlatTabbedPaneUI.this.tabPane.getComponents()) {
/* 2124 */         if (!(c instanceof UIResource))
/* 2125 */           c.addPropertyChangeListener(this.contentListener); 
/*      */       } 
/*      */     }
/*      */     
/*      */     void uninstallListeners() {
/* 2130 */       FlatTabbedPaneUI.this.tabPane.removeMouseMotionListener(this);
/* 2131 */       FlatTabbedPaneUI.this.tabPane.removeComponentListener(this);
/* 2132 */       FlatTabbedPaneUI.this.tabPane.removeContainerListener(this);
/*      */       
/* 2134 */       for (Component c : FlatTabbedPaneUI.this.tabPane.getComponents()) {
/* 2135 */         if (!(c instanceof UIResource)) {
/* 2136 */           c.removePropertyChangeListener(this.contentListener);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void mouseClicked(MouseEvent e) {
/* 2144 */       this.mouseDelegate.mouseClicked(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public void mousePressed(MouseEvent e) {
/* 2149 */       updateRollover(e);
/*      */       
/* 2151 */       if (!FlatTabbedPaneUI.this.isPressedTabClose()) {
/* 2152 */         this.mouseDelegate.mousePressed(e);
/*      */       }
/*      */     }
/*      */     
/*      */     public void mouseReleased(MouseEvent e) {
/* 2157 */       if (FlatTabbedPaneUI.this.isPressedTabClose()) {
/* 2158 */         updateRollover(e);
/* 2159 */         if (this.pressedTabIndex >= 0 && this.pressedTabIndex == FlatTabbedPaneUI.this.getRolloverTab()) {
/* 2160 */           restoreTabToolTip();
/* 2161 */           FlatTabbedPaneUI.this.closeTab(this.pressedTabIndex);
/*      */         } 
/*      */       } else {
/* 2164 */         this.mouseDelegate.mouseReleased(e);
/*      */       } 
/* 2166 */       this.pressedTabIndex = -1;
/* 2167 */       updateRollover(e);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void mouseEntered(MouseEvent e) {
/* 2173 */       updateRollover(e);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void mouseExited(MouseEvent e) {
/* 2181 */       updateRollover(e);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void mouseDragged(MouseEvent e) {
/* 2188 */       updateRollover(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public void mouseMoved(MouseEvent e) {
/* 2193 */       updateRollover(e);
/*      */     }
/*      */     
/*      */     private void updateRollover(MouseEvent e) {
/* 2197 */       int x = e.getX();
/* 2198 */       int y = e.getY();
/*      */       
/* 2200 */       int tabIndex = FlatTabbedPaneUI.this.tabForCoordinate(FlatTabbedPaneUI.this.tabPane, x, y);
/*      */       
/* 2202 */       FlatTabbedPaneUI.this.setRolloverTab(tabIndex);
/*      */ 
/*      */ 
/*      */       
/* 2206 */       boolean hitClose = FlatTabbedPaneUI.this.isTabClosable(tabIndex) ? FlatTabbedPaneUI.this.getTabCloseHitArea(tabIndex).contains(x, y) : false;
/*      */       
/* 2208 */       if (e.getID() == 501)
/* 2209 */         this.pressedTabIndex = hitClose ? tabIndex : -1; 
/* 2210 */       FlatTabbedPaneUI.this.setRolloverTabClose(hitClose);
/* 2211 */       FlatTabbedPaneUI.this.setPressedTabClose((hitClose && tabIndex == this.pressedTabIndex));
/*      */ 
/*      */       
/* 2214 */       if (tabIndex >= 0 && hitClose)
/* 2215 */       { Object closeTip = FlatTabbedPaneUI.this.getTabClientProperty(tabIndex, "JTabbedPane.tabCloseToolTipText");
/* 2216 */         if (closeTip instanceof String) {
/* 2217 */           setCloseToolTip(tabIndex, (String)closeTip);
/*      */         } else {
/* 2219 */           restoreTabToolTip();
/*      */         }  }
/* 2221 */       else { restoreTabToolTip(); }
/*      */     
/*      */     }
/*      */     private void setCloseToolTip(int tabIndex, String closeTip) {
/* 2225 */       if (tabIndex == this.lastTipTabIndex) {
/*      */         return;
/*      */       }
/* 2228 */       if (tabIndex != this.lastTipTabIndex) {
/* 2229 */         restoreTabToolTip();
/*      */       }
/* 2231 */       this.lastTipTabIndex = tabIndex;
/* 2232 */       this.lastTip = FlatTabbedPaneUI.this.tabPane.getToolTipTextAt(this.lastTipTabIndex);
/* 2233 */       FlatTabbedPaneUI.this.tabPane.setToolTipTextAt(this.lastTipTabIndex, closeTip);
/*      */     }
/*      */     
/*      */     private void restoreTabToolTip() {
/* 2237 */       if (this.lastTipTabIndex < 0) {
/*      */         return;
/*      */       }
/* 2240 */       if (this.lastTipTabIndex < FlatTabbedPaneUI.this.tabPane.getTabCount())
/* 2241 */         FlatTabbedPaneUI.this.tabPane.setToolTipTextAt(this.lastTipTabIndex, this.lastTip); 
/* 2242 */       this.lastTip = null;
/* 2243 */       this.lastTipTabIndex = -1;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void propertyChange(PropertyChangeEvent e) {
/* 2251 */       switch (e.getPropertyName()) {
/*      */         case "tabPlacement":
/*      */         case "opaque":
/*      */         case "background":
/*      */         case "indexForTabComponent":
/* 2256 */           FlatTabbedPaneUI.this.runWithOriginalLayoutManager(() -> this.propertyChangeDelegate.propertyChange(e));
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         default:
/* 2262 */           this.propertyChangeDelegate.propertyChange(e);
/*      */           break;
/*      */       } 
/*      */ 
/*      */       
/* 2267 */       switch (e.getPropertyName()) {
/*      */         case "tabPlacement":
/* 2269 */           if (FlatTabbedPaneUI.this.moreTabsButton instanceof FlatTabbedPaneUI.FlatMoreTabsButton) {
/* 2270 */             ((FlatTabbedPaneUI.FlatMoreTabsButton)FlatTabbedPaneUI.this.moreTabsButton).updateDirection();
/*      */           }
/*      */           break;
/*      */         case "componentOrientation":
/* 2274 */           FlatTabbedPaneUI.this.ensureSelectedTabIsVisibleLater();
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case "JTabbedPane.showTabSeparators":
/*      */         case "JTabbedPane.showContentSeparator":
/*      */         case "JTabbedPane.hasFullBorder":
/*      */         case "JTabbedPane.hideTabAreaWithOneTab":
/*      */         case "JTabbedPane.minimumTabWidth":
/*      */         case "JTabbedPane.maximumTabWidth":
/*      */         case "JTabbedPane.tabHeight":
/*      */         case "JTabbedPane.tabInsets":
/*      */         case "JTabbedPane.tabAreaInsets":
/*      */         case "JTabbedPane.tabsPopupPolicy":
/*      */         case "JTabbedPane.scrollButtonsPolicy":
/*      */         case "JTabbedPane.scrollButtonsPlacement":
/*      */         case "JTabbedPane.tabAreaAlignment":
/*      */         case "JTabbedPane.tabAlignment":
/*      */         case "JTabbedPane.tabWidthMode":
/*      */         case "JTabbedPane.tabIconPlacement":
/*      */         case "JTabbedPane.tabClosable":
/* 2296 */           FlatTabbedPaneUI.this.tabPane.revalidate();
/* 2297 */           FlatTabbedPaneUI.this.tabPane.repaint();
/*      */           break;
/*      */         
/*      */         case "JTabbedPane.leadingComponent":
/* 2301 */           FlatTabbedPaneUI.this.uninstallLeadingComponent();
/* 2302 */           FlatTabbedPaneUI.this.installLeadingComponent();
/* 2303 */           FlatTabbedPaneUI.this.tabPane.revalidate();
/* 2304 */           FlatTabbedPaneUI.this.tabPane.repaint();
/* 2305 */           FlatTabbedPaneUI.this.ensureSelectedTabIsVisibleLater();
/*      */           break;
/*      */         
/*      */         case "JTabbedPane.trailingComponent":
/* 2309 */           FlatTabbedPaneUI.this.uninstallTrailingComponent();
/* 2310 */           FlatTabbedPaneUI.this.installTrailingComponent();
/* 2311 */           FlatTabbedPaneUI.this.tabPane.revalidate();
/* 2312 */           FlatTabbedPaneUI.this.tabPane.repaint();
/* 2313 */           FlatTabbedPaneUI.this.ensureSelectedTabIsVisibleLater();
/*      */           break;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void stateChanged(ChangeEvent e) {
/* 2322 */       this.changeDelegate.stateChanged(e);
/*      */ 
/*      */       
/* 2325 */       if (FlatTabbedPaneUI.this.moreTabsButton != null)
/* 2326 */         FlatTabbedPaneUI.this.ensureSelectedTabIsVisible(); 
/*      */     }
/*      */     
/*      */     protected void contentPropertyChange(PropertyChangeEvent e) {
/* 2330 */       switch (e.getPropertyName()) {
/*      */         case "JTabbedPane.minimumTabWidth":
/*      */         case "JTabbedPane.maximumTabWidth":
/*      */         case "JTabbedPane.tabInsets":
/*      */         case "JTabbedPane.tabAlignment":
/*      */         case "JTabbedPane.tabClosable":
/* 2336 */           FlatTabbedPaneUI.this.tabPane.revalidate();
/* 2337 */           FlatTabbedPaneUI.this.tabPane.repaint();
/*      */           break;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void componentResized(ComponentEvent e) {
/* 2347 */       FlatTabbedPaneUI.this.ensureSelectedTabIsVisibleLater();
/*      */     }
/*      */ 
/*      */     
/*      */     public void componentMoved(ComponentEvent e) {}
/*      */     
/*      */     public void componentShown(ComponentEvent e) {}
/*      */     
/*      */     public void componentHidden(ComponentEvent e) {}
/*      */     
/*      */     public void componentAdded(ContainerEvent e) {
/* 2358 */       Component c = e.getChild();
/* 2359 */       if (!(c instanceof UIResource)) {
/* 2360 */         c.addPropertyChangeListener(this.contentListener);
/*      */       }
/*      */     }
/*      */     
/*      */     public void componentRemoved(ContainerEvent e) {
/* 2365 */       Component c = e.getChild();
/* 2366 */       if (!(c instanceof UIResource)) {
/* 2367 */         c.removePropertyChangeListener(this.contentListener);
/*      */       }
/*      */     }
/*      */     
/*      */     private Handler() {}
/*      */   }
/*      */   
/*      */   protected class FlatTabbedPaneLayout
/*      */     extends BasicTabbedPaneUI.TabbedPaneLayout
/*      */   {
/*      */     protected Dimension calculateSize(boolean minimum) {
/* 2378 */       if (isContentEmpty()) {
/* 2379 */         return calculateTabAreaSize();
/*      */       }
/* 2381 */       return super.calculateSize(minimum);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected boolean isContentEmpty() {
/* 2391 */       int tabCount = FlatTabbedPaneUI.this.tabPane.getTabCount();
/* 2392 */       if (tabCount == 0) {
/* 2393 */         return false;
/*      */       }
/* 2395 */       for (int i = 0; i < tabCount; i++) {
/* 2396 */         Component c = FlatTabbedPaneUI.this.tabPane.getComponentAt(i);
/* 2397 */         if (c != null) {
/* 2398 */           Dimension cs = c.getPreferredSize();
/* 2399 */           if (cs.width != 0 || cs.height != 0) {
/* 2400 */             return false;
/*      */           }
/*      */         } 
/*      */       } 
/* 2404 */       return true;
/*      */     }
/*      */     
/*      */     protected Dimension calculateTabAreaSize() {
/* 2408 */       boolean horizontal = FlatTabbedPaneUI.this.isHorizontalTabPlacement();
/* 2409 */       int tabPlacement = FlatTabbedPaneUI.this.tabPane.getTabPlacement();
/* 2410 */       FontMetrics metrics = FlatTabbedPaneUI.this.getFontMetrics();
/* 2411 */       int fontHeight = metrics.getHeight();
/*      */ 
/*      */       
/* 2414 */       int width = 0;
/* 2415 */       int height = 0;
/* 2416 */       int tabCount = FlatTabbedPaneUI.this.tabPane.getTabCount();
/* 2417 */       for (int i = 0; i < tabCount; i++) {
/* 2418 */         if (horizontal) {
/* 2419 */           width += FlatTabbedPaneUI.this.calculateTabWidth(tabPlacement, i, metrics);
/* 2420 */           height = Math.max(height, FlatTabbedPaneUI.this.calculateTabHeight(tabPlacement, i, fontHeight));
/*      */         } else {
/* 2422 */           width = Math.max(width, FlatTabbedPaneUI.this.calculateTabWidth(tabPlacement, i, metrics));
/* 2423 */           height += FlatTabbedPaneUI.this.calculateTabHeight(tabPlacement, i, fontHeight);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 2428 */       if (horizontal) {
/* 2429 */         height += UIScale.scale(FlatTabbedPaneUI.this.contentSeparatorHeight);
/*      */       } else {
/* 2431 */         width += UIScale.scale(FlatTabbedPaneUI.this.contentSeparatorHeight);
/*      */       } 
/*      */       
/* 2434 */       Insets insets = FlatTabbedPaneUI.this.tabPane.getInsets();
/* 2435 */       Insets tabAreaInsets = FlatTabbedPaneUI.this.getTabAreaInsets(tabPlacement);
/* 2436 */       return new Dimension(width + insets.left + insets.right + tabAreaInsets.left + tabAreaInsets.right, height + insets.bottom + insets.top + tabAreaInsets.top + tabAreaInsets.bottom);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void layoutContainer(Container parent) {
/* 2443 */       super.layoutContainer(parent);
/*      */       
/* 2445 */       Rectangle bounds = FlatTabbedPaneUI.this.tabPane.getBounds();
/* 2446 */       Insets insets = FlatTabbedPaneUI.this.tabPane.getInsets();
/* 2447 */       int tabPlacement = FlatTabbedPaneUI.this.tabPane.getTabPlacement();
/* 2448 */       int tabAreaAlignment = FlatTabbedPaneUI.this.getTabAreaAlignment();
/* 2449 */       Insets tabAreaInsets = FlatTabbedPaneUI.this.getRealTabAreaInsets(tabPlacement);
/* 2450 */       boolean leftToRight = FlatTabbedPaneUI.this.isLeftToRight();
/*      */ 
/*      */       
/* 2453 */       if (tabPlacement == 1 || tabPlacement == 3) {
/*      */         
/* 2455 */         if (!leftToRight) {
/* 2456 */           FlatTabbedPaneUI.this.shiftTabs(insets.left + tabAreaInsets.right + FlatTabbedPaneUI.this.getTrailingPreferredWidth(), 0);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 2461 */         int tabAreaHeight = (FlatTabbedPaneUI.this.maxTabHeight > 0) ? FlatTabbedPaneUI.this.maxTabHeight : Math.max(
/* 2462 */             Math.max(FlatTabbedPaneUI.this.getLeadingPreferredHeight(), FlatTabbedPaneUI.this.getTrailingPreferredHeight()), 
/* 2463 */             UIScale.scale(FlatClientProperties.clientPropertyInt(FlatTabbedPaneUI.this.tabPane, "JTabbedPane.tabHeight", FlatTabbedPaneUI.this.tabHeight)));
/*      */ 
/*      */         
/* 2466 */         int tx = insets.left;
/* 2467 */         int ty = (tabPlacement == 1) ? (insets.top + tabAreaInsets.top) : (bounds.height - insets.bottom - tabAreaInsets.bottom - tabAreaHeight);
/*      */ 
/*      */         
/* 2470 */         int tw = bounds.width - insets.left - insets.right;
/* 2471 */         int th = tabAreaHeight;
/*      */         
/* 2473 */         int leadingWidth = FlatTabbedPaneUI.this.getLeadingPreferredWidth();
/* 2474 */         int trailingWidth = FlatTabbedPaneUI.this.getTrailingPreferredWidth();
/*      */ 
/*      */         
/* 2477 */         if (FlatTabbedPaneUI.this.runCount == 1 && FlatTabbedPaneUI.this.rects.length > 0) {
/* 2478 */           int availWidth = tw - leadingWidth - trailingWidth - tabAreaInsets.left - tabAreaInsets.right;
/* 2479 */           int totalTabWidth = FlatTabbedPaneUI.this.rectsTotalWidth(leftToRight);
/* 2480 */           int diff = availWidth - totalTabWidth;
/*      */           
/* 2482 */           switch (tabAreaAlignment) {
/*      */             case 10:
/* 2484 */               trailingWidth += diff;
/*      */               break;
/*      */             
/*      */             case 11:
/* 2488 */               FlatTabbedPaneUI.this.shiftTabs(leftToRight ? diff : -diff, 0);
/* 2489 */               leadingWidth += diff;
/*      */               break;
/*      */             
/*      */             case 0:
/* 2493 */               FlatTabbedPaneUI.this.shiftTabs((leftToRight ? diff : -diff) / 2, 0);
/* 2494 */               leadingWidth += diff / 2;
/* 2495 */               trailingWidth += diff - diff / 2;
/*      */               break;
/*      */             
/*      */             case 100:
/* 2499 */               FlatTabbedPaneUI.this.stretchTabsWidth(diff, leftToRight);
/*      */               break;
/*      */           } 
/* 2502 */         } else if (FlatTabbedPaneUI.this.rects.length == 0) {
/* 2503 */           trailingWidth = tw - leadingWidth;
/*      */         } 
/*      */         
/* 2506 */         Container leftComponent = leftToRight ? FlatTabbedPaneUI.this.leadingComponent : FlatTabbedPaneUI.this.trailingComponent;
/* 2507 */         if (leftComponent != null) {
/* 2508 */           int leftWidth = leftToRight ? leadingWidth : trailingWidth;
/* 2509 */           leftComponent.setBounds(tx, ty, leftWidth, th);
/*      */         } 
/*      */ 
/*      */         
/* 2513 */         Container rightComponent = leftToRight ? FlatTabbedPaneUI.this.trailingComponent : FlatTabbedPaneUI.this.leadingComponent;
/* 2514 */         if (rightComponent != null) {
/* 2515 */           int rightWidth = leftToRight ? trailingWidth : leadingWidth;
/* 2516 */           rightComponent.setBounds(tx + tw - rightWidth, ty, rightWidth, th);
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 2522 */         int tabAreaWidth = (FlatTabbedPaneUI.this.maxTabWidth > 0) ? FlatTabbedPaneUI.this.maxTabWidth : Math.max(FlatTabbedPaneUI.this.getLeadingPreferredWidth(), FlatTabbedPaneUI.this.getTrailingPreferredWidth());
/*      */ 
/*      */         
/* 2525 */         int tx = (tabPlacement == 2) ? (insets.left + tabAreaInsets.left) : (bounds.width - insets.right - tabAreaInsets.right - tabAreaWidth);
/*      */ 
/*      */         
/* 2528 */         int ty = insets.top;
/* 2529 */         int tw = tabAreaWidth;
/* 2530 */         int th = bounds.height - insets.top - insets.bottom;
/*      */         
/* 2532 */         int topHeight = FlatTabbedPaneUI.this.getLeadingPreferredHeight();
/* 2533 */         int bottomHeight = FlatTabbedPaneUI.this.getTrailingPreferredHeight();
/*      */ 
/*      */         
/* 2536 */         if (FlatTabbedPaneUI.this.runCount == 1 && FlatTabbedPaneUI.this.rects.length > 0) {
/* 2537 */           int availHeight = th - topHeight - bottomHeight - tabAreaInsets.top - tabAreaInsets.bottom;
/* 2538 */           int totalTabHeight = FlatTabbedPaneUI.this.rectsTotalHeight();
/* 2539 */           int diff = availHeight - totalTabHeight;
/*      */           
/* 2541 */           switch (tabAreaAlignment) {
/*      */             case 10:
/* 2543 */               bottomHeight += diff;
/*      */               break;
/*      */             
/*      */             case 11:
/* 2547 */               FlatTabbedPaneUI.this.shiftTabs(0, diff);
/* 2548 */               topHeight += diff;
/*      */               break;
/*      */             
/*      */             case 0:
/* 2552 */               FlatTabbedPaneUI.this.shiftTabs(0, diff / 2);
/* 2553 */               topHeight += diff / 2;
/* 2554 */               bottomHeight += diff - diff / 2;
/*      */               break;
/*      */             
/*      */             case 100:
/* 2558 */               FlatTabbedPaneUI.this.stretchTabsHeight(diff);
/*      */               break;
/*      */           } 
/* 2561 */         } else if (FlatTabbedPaneUI.this.rects.length == 0) {
/* 2562 */           bottomHeight = th - topHeight;
/*      */         } 
/*      */         
/* 2565 */         if (FlatTabbedPaneUI.this.leadingComponent != null) {
/* 2566 */           FlatTabbedPaneUI.this.leadingComponent.setBounds(tx, ty, tw, topHeight);
/*      */         }
/*      */         
/* 2569 */         if (FlatTabbedPaneUI.this.trailingComponent != null) {
/* 2570 */           FlatTabbedPaneUI.this.trailingComponent.setBounds(tx, ty + th - bottomHeight, tw, bottomHeight);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected class FlatTabbedPaneScrollLayout
/*      */     extends FlatTabbedPaneLayout
/*      */     implements LayoutManager
/*      */   {
/*      */     private final BasicTabbedPaneUI.TabbedPaneLayout delegate;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected FlatTabbedPaneScrollLayout(BasicTabbedPaneUI.TabbedPaneLayout delegate) {
/* 2593 */       this.delegate = delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     public void calculateLayoutInfo() {
/* 2598 */       this.delegate.calculateLayoutInfo();
/*      */     }
/*      */ 
/*      */     
/*      */     protected Dimension calculateTabAreaSize() {
/* 2603 */       Dimension size = super.calculateTabAreaSize();
/*      */ 
/*      */       
/* 2606 */       if (FlatTabbedPaneUI.this.isHorizontalTabPlacement()) {
/* 2607 */         size.width = Math.min(size.width, UIScale.scale(100));
/*      */       } else {
/* 2609 */         size.height = Math.min(size.height, UIScale.scale(100));
/* 2610 */       }  return size;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Dimension preferredLayoutSize(Container parent) {
/* 2617 */       if (isContentEmpty()) {
/* 2618 */         return calculateTabAreaSize();
/*      */       }
/* 2620 */       return this.delegate.preferredLayoutSize(parent);
/*      */     }
/*      */ 
/*      */     
/*      */     public Dimension minimumLayoutSize(Container parent) {
/* 2625 */       if (isContentEmpty()) {
/* 2626 */         return calculateTabAreaSize();
/*      */       }
/* 2628 */       return this.delegate.minimumLayoutSize(parent);
/*      */     }
/*      */ 
/*      */     
/*      */     public void addLayoutComponent(String name, Component comp) {
/* 2633 */       this.delegate.addLayoutComponent(name, comp);
/*      */     }
/*      */ 
/*      */     
/*      */     public void removeLayoutComponent(Component comp) {
/* 2638 */       this.delegate.removeLayoutComponent(comp);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void layoutContainer(Container parent) {
/* 2647 */       FlatTabbedPaneUI.this.runWithOriginalLayoutManager(() -> this.delegate.layoutContainer(parent));
/*      */ 
/*      */ 
/*      */       
/* 2651 */       int tabsPopupPolicy = FlatTabbedPaneUI.this.getTabsPopupPolicy();
/* 2652 */       int scrollButtonsPolicy = FlatTabbedPaneUI.this.getScrollButtonsPolicy();
/* 2653 */       int scrollButtonsPlacement = FlatTabbedPaneUI.this.getScrollButtonsPlacement();
/*      */       
/* 2655 */       boolean useMoreTabsButton = (tabsPopupPolicy == 2);
/* 2656 */       boolean useScrollButtons = (scrollButtonsPolicy == 2 || scrollButtonsPolicy == 3);
/* 2657 */       boolean hideDisabledScrollButtons = (scrollButtonsPolicy == 3 && scrollButtonsPlacement == 100);
/* 2658 */       boolean trailingScrollButtons = (scrollButtonsPlacement == 11);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2663 */       boolean leftToRight = FlatTabbedPaneUI.this.isLeftToRight();
/* 2664 */       if (!leftToRight && FlatTabbedPaneUI.this.isHorizontalTabPlacement()) {
/* 2665 */         useMoreTabsButton = true;
/* 2666 */         useScrollButtons = false;
/*      */       } 
/*      */ 
/*      */       
/* 2670 */       JButton backwardButton = null;
/* 2671 */       JButton forwardButton = null;
/* 2672 */       for (Component c : FlatTabbedPaneUI.this.tabPane.getComponents()) {
/* 2673 */         if (c instanceof FlatTabbedPaneUI.FlatScrollableTabButton) {
/* 2674 */           int direction = ((FlatTabbedPaneUI.FlatScrollableTabButton)c).getDirection();
/* 2675 */           if (direction == 7 || direction == 1) {
/* 2676 */             backwardButton = (JButton)c;
/* 2677 */           } else if (direction == 3 || direction == 5) {
/* 2678 */             forwardButton = (JButton)c;
/*      */           } 
/*      */         } 
/*      */       } 
/* 2682 */       if (backwardButton == null || forwardButton == null) {
/*      */         return;
/*      */       }
/* 2685 */       Rectangle bounds = FlatTabbedPaneUI.this.tabPane.getBounds();
/* 2686 */       Insets insets = FlatTabbedPaneUI.this.tabPane.getInsets();
/* 2687 */       int tabPlacement = FlatTabbedPaneUI.this.tabPane.getTabPlacement();
/* 2688 */       int tabAreaAlignment = FlatTabbedPaneUI.this.getTabAreaAlignment();
/* 2689 */       Insets tabAreaInsets = FlatTabbedPaneUI.this.getRealTabAreaInsets(tabPlacement);
/* 2690 */       boolean moreTabsButtonVisible = false;
/* 2691 */       boolean backwardButtonVisible = false;
/* 2692 */       boolean forwardButtonVisible = false;
/*      */ 
/*      */ 
/*      */       
/* 2696 */       if (tabAreaInsets.left != 0 || tabAreaInsets.top != 0) {
/*      */         
/* 2698 */         FlatTabbedPaneUI.this.shiftTabs(-tabAreaInsets.left, -tabAreaInsets.top);
/*      */ 
/*      */         
/* 2701 */         Component view = FlatTabbedPaneUI.this.tabViewport.getView();
/* 2702 */         Dimension viewSize = view.getPreferredSize();
/* 2703 */         boolean horizontal = (tabPlacement == 1 || tabPlacement == 3);
/* 2704 */         view.setPreferredSize(new Dimension(viewSize.width - (horizontal ? tabAreaInsets.left : 0), viewSize.height - (horizontal ? 0 : tabAreaInsets.top)));
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2710 */       if (tabPlacement == 1 || tabPlacement == 3) {
/*      */         
/* 2712 */         if (useScrollButtons && hideDisabledScrollButtons) {
/* 2713 */           Point viewPosition = FlatTabbedPaneUI.this.tabViewport.getViewPosition();
/* 2714 */           if (viewPosition.x <= (backwardButton.getPreferredSize()).width) {
/* 2715 */             FlatTabbedPaneUI.this.tabViewport.setViewPosition(new Point(0, viewPosition.y));
/*      */           }
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 2721 */         int tabAreaHeight = (FlatTabbedPaneUI.this.maxTabHeight > 0) ? FlatTabbedPaneUI.this.maxTabHeight : Math.max(
/* 2722 */             Math.max(FlatTabbedPaneUI.this.getLeadingPreferredHeight(), FlatTabbedPaneUI.this.getTrailingPreferredHeight()), 
/* 2723 */             UIScale.scale(FlatClientProperties.clientPropertyInt(FlatTabbedPaneUI.this.tabPane, "JTabbedPane.tabHeight", FlatTabbedPaneUI.this.tabHeight)));
/*      */ 
/*      */         
/* 2726 */         int tx = insets.left;
/* 2727 */         int ty = (tabPlacement == 1) ? (insets.top + tabAreaInsets.top) : (bounds.height - insets.bottom - tabAreaInsets.bottom - tabAreaHeight);
/*      */ 
/*      */         
/* 2730 */         int tw = bounds.width - insets.left - insets.right;
/* 2731 */         int th = tabAreaHeight;
/*      */         
/* 2733 */         int leadingWidth = FlatTabbedPaneUI.this.getLeadingPreferredWidth();
/* 2734 */         int trailingWidth = FlatTabbedPaneUI.this.getTrailingPreferredWidth();
/* 2735 */         int availWidth = tw - leadingWidth - trailingWidth - tabAreaInsets.left - tabAreaInsets.right;
/* 2736 */         int totalTabWidth = (FlatTabbedPaneUI.this.rects.length > 0) ? FlatTabbedPaneUI.this.rectsTotalWidth(leftToRight) : 0;
/*      */ 
/*      */         
/* 2739 */         if (totalTabWidth < availWidth && FlatTabbedPaneUI.this.rects.length > 0) {
/* 2740 */           int diff = availWidth - totalTabWidth;
/* 2741 */           switch (tabAreaAlignment) {
/*      */             case 10:
/* 2743 */               trailingWidth += diff;
/*      */               break;
/*      */             
/*      */             case 11:
/* 2747 */               leadingWidth += diff;
/*      */               break;
/*      */             
/*      */             case 0:
/* 2751 */               leadingWidth += diff / 2;
/* 2752 */               trailingWidth += diff - diff / 2;
/*      */               break;
/*      */             
/*      */             case 100:
/* 2756 */               FlatTabbedPaneUI.this.stretchTabsWidth(diff, leftToRight);
/* 2757 */               totalTabWidth = FlatTabbedPaneUI.this.rectsTotalWidth(leftToRight);
/*      */               break;
/*      */           } 
/* 2760 */         } else if (FlatTabbedPaneUI.this.rects.length == 0) {
/* 2761 */           trailingWidth = tw - leadingWidth;
/*      */         } 
/*      */         
/* 2764 */         Container leftComponent = leftToRight ? FlatTabbedPaneUI.this.leadingComponent : FlatTabbedPaneUI.this.trailingComponent;
/* 2765 */         int leftWidth = leftToRight ? leadingWidth : trailingWidth;
/* 2766 */         if (leftComponent != null) {
/* 2767 */           leftComponent.setBounds(tx, ty, leftWidth, th);
/*      */         }
/*      */         
/* 2770 */         Container rightComponent = leftToRight ? FlatTabbedPaneUI.this.trailingComponent : FlatTabbedPaneUI.this.leadingComponent;
/* 2771 */         int rightWidth = leftToRight ? trailingWidth : leadingWidth;
/* 2772 */         if (rightComponent != null) {
/* 2773 */           rightComponent.setBounds(tx + tw - rightWidth, ty, rightWidth, th);
/*      */         }
/*      */         
/* 2776 */         if (FlatTabbedPaneUI.this.rects.length > 0) {
/* 2777 */           int txi = tx + leftWidth + (leftToRight ? tabAreaInsets.left : tabAreaInsets.right);
/* 2778 */           int twi = tw - leftWidth - rightWidth - tabAreaInsets.left - tabAreaInsets.right;
/*      */ 
/*      */           
/* 2781 */           int x = txi;
/* 2782 */           int w = twi;
/*      */           
/* 2784 */           if (w < totalTabWidth) {
/*      */ 
/*      */ 
/*      */             
/* 2788 */             if (useMoreTabsButton) {
/* 2789 */               int buttonWidth = (FlatTabbedPaneUI.this.moreTabsButton.getPreferredSize()).width;
/* 2790 */               FlatTabbedPaneUI.this.moreTabsButton.setBounds(leftToRight ? (x + w - buttonWidth) : x, ty, buttonWidth, th);
/* 2791 */               x += leftToRight ? 0 : buttonWidth;
/* 2792 */               w -= buttonWidth;
/* 2793 */               moreTabsButtonVisible = true;
/*      */             } 
/* 2795 */             if (useScrollButtons) {
/*      */               
/* 2797 */               if (!hideDisabledScrollButtons || forwardButton.isEnabled()) {
/* 2798 */                 int buttonWidth = (forwardButton.getPreferredSize()).width;
/* 2799 */                 forwardButton.setBounds(leftToRight ? (x + w - buttonWidth) : x, ty, buttonWidth, th);
/* 2800 */                 x += leftToRight ? 0 : buttonWidth;
/* 2801 */                 w -= buttonWidth;
/* 2802 */                 forwardButtonVisible = true;
/*      */               } 
/*      */ 
/*      */               
/* 2806 */               if (!hideDisabledScrollButtons || backwardButton.isEnabled()) {
/* 2807 */                 int buttonWidth = (backwardButton.getPreferredSize()).width;
/* 2808 */                 if (trailingScrollButtons) {
/*      */                   
/* 2810 */                   backwardButton.setBounds(leftToRight ? (x + w - buttonWidth) : x, ty, buttonWidth, th);
/* 2811 */                   x += leftToRight ? 0 : buttonWidth;
/*      */                 } else {
/*      */                   
/* 2814 */                   backwardButton.setBounds(leftToRight ? x : (x + w - buttonWidth), ty, buttonWidth, th);
/* 2815 */                   x += leftToRight ? buttonWidth : 0;
/*      */                 } 
/* 2817 */                 w -= buttonWidth;
/* 2818 */                 backwardButtonVisible = true;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           
/* 2823 */           FlatTabbedPaneUI.this.tabViewport.setBounds(x, ty, w, th);
/*      */           
/* 2825 */           if (!leftToRight) {
/*      */             
/* 2827 */             FlatTabbedPaneUI.this.tabViewport.doLayout();
/*      */ 
/*      */             
/* 2830 */             FlatTabbedPaneUI.this.shiftTabs(FlatTabbedPaneUI.this.tabViewport.getView().getWidth() - (FlatTabbedPaneUI.this.rects[0]).x + (FlatTabbedPaneUI.this.rects[0]).width, 0);
/*      */           } 
/*      */         } 
/*      */       } else {
/*      */         
/* 2835 */         if (useScrollButtons && hideDisabledScrollButtons) {
/* 2836 */           Point viewPosition = FlatTabbedPaneUI.this.tabViewport.getViewPosition();
/* 2837 */           if (viewPosition.y <= (backwardButton.getPreferredSize()).height) {
/* 2838 */             FlatTabbedPaneUI.this.tabViewport.setViewPosition(new Point(viewPosition.x, 0));
/*      */           }
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 2844 */         int tabAreaWidth = (FlatTabbedPaneUI.this.maxTabWidth > 0) ? FlatTabbedPaneUI.this.maxTabWidth : Math.max(FlatTabbedPaneUI.this.getLeadingPreferredWidth(), FlatTabbedPaneUI.this.getTrailingPreferredWidth());
/*      */ 
/*      */         
/* 2847 */         int tx = (tabPlacement == 2) ? (insets.left + tabAreaInsets.left) : (bounds.width - insets.right - tabAreaInsets.right - tabAreaWidth);
/*      */ 
/*      */         
/* 2850 */         int ty = insets.top;
/* 2851 */         int tw = tabAreaWidth;
/* 2852 */         int th = bounds.height - insets.top - insets.bottom;
/*      */         
/* 2854 */         int topHeight = FlatTabbedPaneUI.this.getLeadingPreferredHeight();
/* 2855 */         int bottomHeight = FlatTabbedPaneUI.this.getTrailingPreferredHeight();
/* 2856 */         int availHeight = th - topHeight - bottomHeight - tabAreaInsets.top - tabAreaInsets.bottom;
/* 2857 */         int totalTabHeight = (FlatTabbedPaneUI.this.rects.length > 0) ? FlatTabbedPaneUI.this.rectsTotalHeight() : 0;
/*      */ 
/*      */         
/* 2860 */         if (totalTabHeight < availHeight && FlatTabbedPaneUI.this.rects.length > 0) {
/* 2861 */           int diff = availHeight - totalTabHeight;
/* 2862 */           switch (tabAreaAlignment) {
/*      */             case 10:
/* 2864 */               bottomHeight += diff;
/*      */               break;
/*      */             
/*      */             case 11:
/* 2868 */               topHeight += diff;
/*      */               break;
/*      */             
/*      */             case 0:
/* 2872 */               topHeight += diff / 2;
/* 2873 */               bottomHeight += diff - diff / 2;
/*      */               break;
/*      */             
/*      */             case 100:
/* 2877 */               FlatTabbedPaneUI.this.stretchTabsHeight(diff);
/* 2878 */               totalTabHeight = FlatTabbedPaneUI.this.rectsTotalHeight();
/*      */               break;
/*      */           } 
/* 2881 */         } else if (FlatTabbedPaneUI.this.rects.length == 0) {
/* 2882 */           bottomHeight = th - topHeight;
/*      */         } 
/*      */         
/* 2885 */         if (FlatTabbedPaneUI.this.leadingComponent != null) {
/* 2886 */           FlatTabbedPaneUI.this.leadingComponent.setBounds(tx, ty, tw, topHeight);
/*      */         }
/*      */         
/* 2889 */         if (FlatTabbedPaneUI.this.trailingComponent != null) {
/* 2890 */           FlatTabbedPaneUI.this.trailingComponent.setBounds(tx, ty + th - bottomHeight, tw, bottomHeight);
/*      */         }
/*      */         
/* 2893 */         if (FlatTabbedPaneUI.this.rects.length > 0) {
/* 2894 */           int tyi = ty + topHeight + tabAreaInsets.top;
/* 2895 */           int thi = th - topHeight - bottomHeight - tabAreaInsets.top - tabAreaInsets.bottom;
/*      */ 
/*      */           
/* 2898 */           int y = tyi;
/* 2899 */           int h = thi;
/*      */           
/* 2901 */           if (h < totalTabHeight) {
/*      */ 
/*      */ 
/*      */             
/* 2905 */             if (useMoreTabsButton) {
/* 2906 */               int buttonHeight = (FlatTabbedPaneUI.this.moreTabsButton.getPreferredSize()).height;
/* 2907 */               FlatTabbedPaneUI.this.moreTabsButton.setBounds(tx, y + h - buttonHeight, tw, buttonHeight);
/* 2908 */               h -= buttonHeight;
/* 2909 */               moreTabsButtonVisible = true;
/*      */             } 
/* 2911 */             if (useScrollButtons) {
/*      */               
/* 2913 */               if (!hideDisabledScrollButtons || forwardButton.isEnabled()) {
/* 2914 */                 int buttonHeight = (forwardButton.getPreferredSize()).height;
/* 2915 */                 forwardButton.setBounds(tx, y + h - buttonHeight, tw, buttonHeight);
/* 2916 */                 h -= buttonHeight;
/* 2917 */                 forwardButtonVisible = true;
/*      */               } 
/*      */ 
/*      */               
/* 2921 */               if (!hideDisabledScrollButtons || backwardButton.isEnabled()) {
/* 2922 */                 int buttonHeight = (backwardButton.getPreferredSize()).height;
/* 2923 */                 if (trailingScrollButtons) {
/*      */                   
/* 2925 */                   backwardButton.setBounds(tx, y + h - buttonHeight, tw, buttonHeight);
/*      */                 } else {
/*      */                   
/* 2928 */                   backwardButton.setBounds(tx, y, tw, buttonHeight);
/* 2929 */                   y += buttonHeight;
/*      */                 } 
/* 2931 */                 h -= buttonHeight;
/* 2932 */                 backwardButtonVisible = true;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */           
/* 2937 */           FlatTabbedPaneUI.this.tabViewport.setBounds(tx, y, tw, h);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 2942 */       FlatTabbedPaneUI.this.tabViewport.setVisible((FlatTabbedPaneUI.this.rects.length > 0));
/* 2943 */       FlatTabbedPaneUI.this.moreTabsButton.setVisible(moreTabsButtonVisible);
/* 2944 */       backwardButton.setVisible(backwardButtonVisible);
/* 2945 */       forwardButton.setVisible(forwardButtonVisible);
/*      */       
/* 2947 */       FlatTabbedPaneUI.this.scrollBackwardButtonPrefSize = backwardButton.getPreferredSize();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\ui\FlatTabbedPaneUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */