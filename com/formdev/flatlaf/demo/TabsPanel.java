/*      */ package com.formdev.flatlaf.demo;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JTabbedPane;
/*      */ import javax.swing.JToggleButton;
/*      */ import javax.swing.JToolBar;
/*      */ 
/*      */ class TabsPanel extends JPanel {
/*      */   private JToolBar tabPlacementToolBar;
/*      */   private JToggleButton topPlacementButton;
/*      */   private JToggleButton bottomPlacementButton;
/*      */   private JToggleButton leftPlacementButton;
/*      */   private JToggleButton rightPlacementButton;
/*      */   private JToggleButton scrollButton;
/*      */   private JToggleButton borderButton;
/*      */   private JTabbedPane tabPlacementTabbedPane;
/*      */   private JToolBar tabLayoutToolBar;
/*      */   private JToggleButton scrollTabLayoutButton;
/*      */   private JToggleButton wrapTabLayoutButton;
/*      */   private JLabel scrollLayoutNoteLabel;
/*      */   private JLabel wrapLayoutNoteLabel;
/*      */   private JTabbedPane scrollLayoutTabbedPane;
/*      */   private JTabbedPane wrapLayoutTabbedPane;
/*      */   private JToolBar closableTabsToolBar;
/*      */   private JToggleButton squareCloseButton;
/*      */   private JToggleButton circleCloseButton;
/*      */   private JToggleButton redCrossCloseButton;
/*      */   private JTabbedPane closableTabsTabbedPane;
/*      */   private JToolBar tabAreaComponentsToolBar;
/*      */   private JToggleButton leadingComponentButton;
/*      */   private JToggleButton trailingComponentButton;
/*      */   private JTabbedPane customComponentsTabbedPane;
/*      */   private JTabbedPane iconTopTabbedPane;
/*      */   private JTabbedPane iconBottomTabbedPane;
/*      */   private JTabbedPane iconLeadingTabbedPane;
/*      */   private JTabbedPane iconTrailingTabbedPane;
/*      */   
/*      */   TabsPanel() {
/*   39 */     initComponents();
/*      */     
/*   41 */     initTabPlacementTabs(this.tabPlacementTabbedPane);
/*      */     
/*   43 */     initScrollLayoutTabs(this.scrollLayoutTabbedPane);
/*   44 */     initWrapLayoutTabs(this.wrapLayoutTabbedPane);
/*      */     
/*   46 */     initClosableTabs(this.closableTabsTabbedPane);
/*      */     
/*   48 */     initCustomComponentsTabs(this.customComponentsTabbedPane);
/*      */     
/*   50 */     initMinimumTabWidth(this.minimumTabWidthTabbedPane);
/*   51 */     initMaximumTabWidth(this.maximumTabWidthTabbedPane);
/*      */     
/*   53 */     initTabIconPlacement(this.iconTopTabbedPane, 1);
/*   54 */     initTabIconPlacement(this.iconBottomTabbedPane, 3);
/*   55 */     initTabIconPlacement(this.iconLeadingTabbedPane, 10);
/*   56 */     initTabIconPlacement(this.iconTrailingTabbedPane, 11);
/*      */     
/*   58 */     initTabAreaAlignment(this.alignLeadingTabbedPane, "leading");
/*   59 */     initTabAreaAlignment(this.alignCenterTabbedPane, "center");
/*   60 */     initTabAreaAlignment(this.alignTrailingTabbedPane, "trailing");
/*   61 */     initTabAreaAlignment(this.alignFillTabbedPane, "fill");
/*      */     
/*   63 */     initTabAlignment(this.tabAlignLeadingTabbedPane, 10);
/*   64 */     initTabAlignment(this.tabAlignCenterTabbedPane, 0);
/*   65 */     initTabAlignment(this.tabAlignTrailingTabbedPane, 11);
/*   66 */     initTabAlignment(this.tabAlignVerticalTabbedPane, 11);
/*      */     
/*   68 */     initTabWidthMode(this.widthPreferredTabbedPane, "preferred");
/*   69 */     initTabWidthMode(this.widthEqualTabbedPane, "equal");
/*   70 */     initTabWidthMode(this.widthCompactTabbedPane, "compact");
/*      */   }
/*      */   private JTabbedPane alignLeadingTabbedPane; private JTabbedPane alignCenterTabbedPane; private JTabbedPane alignTrailingTabbedPane; private JTabbedPane alignFillTabbedPane; private JTabbedPane widthPreferredTabbedPane; private JTabbedPane widthEqualTabbedPane; private JTabbedPane widthCompactTabbedPane; private JTabbedPane minimumTabWidthTabbedPane; private JTabbedPane maximumTabWidthTabbedPane; private JPanel panel5; private JTabbedPane tabAlignLeadingTabbedPane; private JTabbedPane tabAlignVerticalTabbedPane; private JTabbedPane tabAlignCenterTabbedPane; private JTabbedPane tabAlignTrailingTabbedPane; private JSeparator separator2; private JLabel scrollButtonsPolicyLabel; private JToolBar scrollButtonsPolicyToolBar; private JToggleButton scrollAsNeededSingleButton; private JToggleButton scrollAsNeededButton; private JToggleButton scrollNeverButton; private JLabel scrollButtonsPlacementLabel; private JToolBar scrollButtonsPlacementToolBar; private JToggleButton scrollBothButton; private JToggleButton scrollTrailingButton; private JLabel tabsPopupPolicyLabel; private JToolBar tabsPopupPolicyToolBar; private JToggleButton popupAsNeededButton; private JToggleButton popupNeverButton; private JCheckBox showTabSeparatorsCheckBox;
/*      */   private void initTabPlacementTabs(JTabbedPane tabbedPane) {
/*   74 */     addTab(tabbedPane, "Tab 1", "tab content 1");
/*      */     
/*   76 */     JComponent tab2 = createTab("tab content 2");
/*   77 */     tab2.setBorder(new LineBorder(Color.magenta));
/*   78 */     tabbedPane.addTab("Second Tab", tab2);
/*      */     
/*   80 */     addTab(tabbedPane, "Disabled", "tab content 3");
/*   81 */     tabbedPane.setEnabledAt(2, false);
/*      */   }
/*      */   
/*      */   private void addTab(JTabbedPane tabbedPane, String title, String text) {
/*   85 */     tabbedPane.addTab(title, createTab(text));
/*      */   }
/*      */   
/*      */   private JComponent createTab(String text) {
/*   89 */     JLabel label = new JLabel(text);
/*   90 */     label.setHorizontalAlignment(0);
/*      */     
/*   92 */     JPanel tab = new JPanel(new BorderLayout());
/*   93 */     tab.add(label, "Center");
/*   94 */     return tab;
/*      */   }
/*      */   
/*      */   private void tabPlacementChanged() {
/*   98 */     int tabPlacement = 1;
/*   99 */     if (this.bottomPlacementButton.isSelected()) {
/*  100 */       tabPlacement = 3;
/*  101 */     } else if (this.leftPlacementButton.isSelected()) {
/*  102 */       tabPlacement = 2;
/*  103 */     } else if (this.rightPlacementButton.isSelected()) {
/*  104 */       tabPlacement = 4;
/*      */     } 
/*  106 */     this.tabPlacementTabbedPane.setTabPlacement(tabPlacement);
/*      */   }
/*      */   
/*      */   private void scrollChanged() {
/*  110 */     boolean scroll = this.scrollButton.isSelected();
/*  111 */     this.tabPlacementTabbedPane.setTabLayoutPolicy(scroll ? 1 : 0);
/*      */     
/*  113 */     int extraTabCount = 7;
/*  114 */     if (scroll) {
/*  115 */       int tabCount = this.tabPlacementTabbedPane.getTabCount();
/*  116 */       for (int i = tabCount + 1; i <= tabCount + extraTabCount; i++)
/*  117 */         addTab(this.tabPlacementTabbedPane, "Tab " + i, "tab content " + i); 
/*      */     } else {
/*  119 */       for (int i = 0; i < extraTabCount; i++)
/*  120 */         this.tabPlacementTabbedPane.removeTabAt(this.tabPlacementTabbedPane.getTabCount() - 1); 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void borderChanged() {
/*  125 */     Boolean hasFullBorder = this.borderButton.isSelected() ? Boolean.valueOf(true) : null;
/*  126 */     this.tabPlacementTabbedPane.putClientProperty("JTabbedPane.hasFullBorder", hasFullBorder);
/*      */   }
/*      */   
/*      */   private void initScrollLayoutTabs(JTabbedPane tabbedPane) {
/*  130 */     tabbedPane.setTabLayoutPolicy(1);
/*  131 */     addDefaultTabsNoContent(tabbedPane, 9);
/*      */   }
/*      */   
/*      */   private void initWrapLayoutTabs(JTabbedPane tabbedPane) {
/*  135 */     tabbedPane.setTabLayoutPolicy(0);
/*  136 */     addDefaultTabsNoContent(tabbedPane, 9);
/*      */     
/*  138 */     this.wrapLayoutTabbedPane.setVisible(false);
/*  139 */     this.wrapLayoutNoteLabel.setVisible(false);
/*      */   }
/*      */   
/*      */   private void tabLayoutChanged() {
/*  143 */     boolean scroll = this.scrollTabLayoutButton.isSelected();
/*      */     
/*  145 */     this.scrollLayoutTabbedPane.setVisible(scroll);
/*  146 */     this.scrollLayoutNoteLabel.setVisible(scroll);
/*  147 */     this.wrapLayoutTabbedPane.setVisible(!scroll);
/*  148 */     this.wrapLayoutNoteLabel.setVisible(!scroll);
/*      */   }
/*      */   
/*      */   private void initClosableTabs(JTabbedPane tabbedPane) {
/*  152 */     tabbedPane.putClientProperty("JTabbedPane.tabClosable", Boolean.valueOf(true));
/*  153 */     tabbedPane.putClientProperty("JTabbedPane.tabCloseToolTipText", "Close");
/*  154 */     tabbedPane.putClientProperty("JTabbedPane.tabCloseCallback", (tabPane, tabIndex) -> {
/*      */           AWTEvent e = EventQueue.getCurrentEvent();
/*      */ 
/*      */           
/*      */           int modifiers = (e instanceof MouseEvent) ? ((MouseEvent)e).getModifiers() : 0;
/*      */           
/*      */           JOptionPane.showMessageDialog(this, "Closed tab '" + tabPane.getTitleAt(tabIndex.intValue()) + "'.\n\n(modifiers: " + MouseEvent.getMouseModifiersText(modifiers) + ")", "Tab Closed", -1);
/*      */         });
/*      */     
/*  163 */     addDefaultTabsNoContent(tabbedPane, 3);
/*      */   }
/*      */   
/*      */   private void initCustomComponentsTabs(JTabbedPane tabbedPane) {
/*  167 */     addDefaultTabsNoContent(tabbedPane, 2);
/*  168 */     customComponentsChanged();
/*      */   }
/*      */   
/*      */   private void customComponentsChanged() {
/*  172 */     JToolBar leading = null;
/*  173 */     JToolBar trailing = null;
/*  174 */     if (this.leadingComponentButton.isSelected()) {
/*  175 */       leading = new JToolBar();
/*  176 */       leading.setFloatable(false);
/*  177 */       leading.setBorder((Border)null);
/*  178 */       leading.add(new JButton((Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/project.svg")));
/*      */     } 
/*  180 */     if (this.trailingComponentButton.isSelected()) {
/*  181 */       trailing = new JToolBar();
/*  182 */       trailing.setFloatable(false);
/*  183 */       trailing.setBorder((Border)null);
/*  184 */       trailing.add(new JButton((Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/buildLoadChanges.svg")));
/*  185 */       trailing.add(Box.createHorizontalGlue());
/*  186 */       trailing.add(new JButton((Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/commit.svg")));
/*  187 */       trailing.add(new JButton((Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/diff.svg")));
/*  188 */       trailing.add(new JButton((Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/listFiles.svg")));
/*      */     } 
/*  190 */     this.customComponentsTabbedPane.putClientProperty("JTabbedPane.leadingComponent", leading);
/*  191 */     this.customComponentsTabbedPane.putClientProperty("JTabbedPane.trailingComponent", trailing);
/*      */   }
/*      */   
/*      */   private void addDefaultTabsNoContent(JTabbedPane tabbedPane, int count) {
/*  195 */     tabbedPane.addTab("Tab 1", (Component)null);
/*  196 */     tabbedPane.addTab("Second Tab", (Component)null);
/*  197 */     if (count >= 3) {
/*  198 */       tabbedPane.addTab("3rd Tab", (Component)null);
/*      */     }
/*  200 */     for (int i = 4; i <= count; i++) {
/*  201 */       tabbedPane.addTab("Tab " + i, (Component)null);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void closeButtonStyleChanged() {
/*  208 */     if (this.circleCloseButton.isSelected()) {
/*  209 */       UIManager.put("TabbedPane.closeArc", Integer.valueOf(999));
/*  210 */       UIManager.put("TabbedPane.closeCrossFilledSize", Float.valueOf(5.5F));
/*  211 */       UIManager.put("TabbedPane.closeIcon", new FlatTabbedPaneCloseIcon());
/*  212 */       this.closableTabsTabbedPane.updateUI();
/*  213 */       UIManager.put("TabbedPane.closeArc", null);
/*  214 */       UIManager.put("TabbedPane.closeCrossFilledSize", null);
/*  215 */       UIManager.put("TabbedPane.closeIcon", null);
/*  216 */     } else if (this.redCrossCloseButton.isSelected()) {
/*  217 */       UIManager.put("TabbedPane.closeHoverForeground", Color.red);
/*  218 */       UIManager.put("TabbedPane.closePressedForeground", Color.red);
/*  219 */       UIManager.put("TabbedPane.closeHoverBackground", new Color(0, true));
/*  220 */       UIManager.put("TabbedPane.closeIcon", new FlatTabbedPaneCloseIcon());
/*  221 */       this.closableTabsTabbedPane.updateUI();
/*  222 */       UIManager.put("TabbedPane.closeHoverForeground", null);
/*  223 */       UIManager.put("TabbedPane.closePressedForeground", null);
/*  224 */       UIManager.put("TabbedPane.closeHoverBackground", null);
/*  225 */       UIManager.put("TabbedPane.closeIcon", null);
/*      */     } else {
/*  227 */       this.closableTabsTabbedPane.updateUI();
/*      */     } 
/*      */   }
/*      */   private void initTabIconPlacement(JTabbedPane tabbedPane, int iconPlacement) {
/*  231 */     boolean topOrBottom = (iconPlacement == 1 || iconPlacement == 3);
/*  232 */     int iconSize = topOrBottom ? 24 : 16;
/*  233 */     tabbedPane.putClientProperty("JTabbedPane.tabIconPlacement", Integer.valueOf(iconPlacement));
/*  234 */     if (topOrBottom) {
/*  235 */       tabbedPane.putClientProperty("JTabbedPane.tabAreaAlignment", "fill");
/*  236 */       tabbedPane.putClientProperty("JTabbedPane.tabWidthMode", "equal");
/*      */     } 
/*  238 */     tabbedPane.addTab("Search", (Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/search.svg", iconSize, iconSize), (Component)null);
/*  239 */     tabbedPane.addTab("Recents", (Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/RecentlyUsed.svg", iconSize, iconSize), (Component)null);
/*  240 */     if (topOrBottom)
/*  241 */       tabbedPane.addTab("Favorites", (Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/favorite.svg", iconSize, iconSize), (Component)null); 
/*      */   }
/*      */   
/*      */   private void initTabAreaAlignment(JTabbedPane tabbedPane, String tabAreaAlignment) {
/*  245 */     tabbedPane.putClientProperty("JTabbedPane.tabAreaAlignment", tabAreaAlignment);
/*  246 */     tabbedPane.addTab("Search", (Component)null);
/*  247 */     tabbedPane.addTab("Recents", (Component)null);
/*      */   }
/*      */   
/*      */   private void initTabAlignment(JTabbedPane tabbedPane, int tabAlignment) {
/*  251 */     boolean vertical = (tabbedPane.getTabPlacement() == 2 || tabbedPane.getTabPlacement() == 4);
/*  252 */     tabbedPane.putClientProperty("JTabbedPane.tabAlignment", Integer.valueOf(tabAlignment));
/*  253 */     if (!vertical)
/*  254 */       tabbedPane.putClientProperty("JTabbedPane.minimumTabWidth", Integer.valueOf(80)); 
/*  255 */     tabbedPane.addTab("A", (Component)null);
/*  256 */     if (vertical) {
/*  257 */       tabbedPane.addTab("Search", (Component)null);
/*  258 */       tabbedPane.addTab("Recents", (Component)null);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void initTabWidthMode(JTabbedPane tabbedPane, String tabWidthMode) {
/*  263 */     tabbedPane.putClientProperty("JTabbedPane.tabWidthMode", tabWidthMode);
/*  264 */     if (tabWidthMode.equals("compact")) {
/*  265 */       tabbedPane.addTab("Search", (Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/search.svg", 16, 16), (Component)null);
/*  266 */       tabbedPane.addTab("Recents", (Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/RecentlyUsed.svg", 16, 16), (Component)null);
/*  267 */       tabbedPane.addTab("Favorites", (Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/favorite.svg", 16, 16), (Component)null);
/*      */     } else {
/*  269 */       tabbedPane.addTab("Short", (Component)null);
/*  270 */       tabbedPane.addTab("Longer Title", (Component)null);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void initMinimumTabWidth(JTabbedPane tabbedPane) {
/*  275 */     tabbedPane.putClientProperty("JTabbedPane.minimumTabWidth", Integer.valueOf(80));
/*  276 */     tabbedPane.addTab("A", (Component)null);
/*  277 */     tabbedPane.addTab("Very long title", (Component)null);
/*      */   }
/*      */   
/*      */   private void initMaximumTabWidth(JTabbedPane tabbedPane) {
/*  281 */     tabbedPane.putClientProperty("JTabbedPane.maximumTabWidth", Integer.valueOf(80));
/*  282 */     tabbedPane.addTab("Very long title", (Component)null);
/*  283 */     tabbedPane.addTab("B", (Component)null);
/*  284 */     tabbedPane.addTab("C", (Component)null);
/*      */   }
/*      */   
/*      */   private void tabsPopupPolicyChanged() {
/*  288 */     String tabsPopupPolicy = this.popupNeverButton.isSelected() ? "never" : null;
/*  289 */     putTabbedPanesClientProperty("JTabbedPane.tabsPopupPolicy", tabsPopupPolicy);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void scrollButtonsPolicyChanged() {
/*  295 */     String scrollButtonsPolicy = this.scrollAsNeededButton.isSelected() ? "asNeeded" : (this.scrollNeverButton.isSelected() ? "never" : null);
/*      */ 
/*      */     
/*  298 */     putTabbedPanesClientProperty("JTabbedPane.scrollButtonsPolicy", scrollButtonsPolicy);
/*      */   }
/*      */   
/*      */   private void scrollButtonsPlacementChanged() {
/*  302 */     String scrollButtonsPlacement = this.scrollTrailingButton.isSelected() ? "trailing" : null;
/*  303 */     putTabbedPanesClientProperty("JTabbedPane.scrollButtonsPlacement", scrollButtonsPlacement);
/*      */   }
/*      */   
/*      */   private void showTabSeparatorsChanged() {
/*  307 */     Boolean showTabSeparators = this.showTabSeparatorsCheckBox.isSelected() ? Boolean.valueOf(true) : null;
/*  308 */     putTabbedPanesClientProperty("JTabbedPane.showTabSeparators", showTabSeparators);
/*      */   }
/*      */   
/*      */   private void putTabbedPanesClientProperty(String key, Object value) {
/*  312 */     updateTabbedPanesRecur(this, tabbedPane -> tabbedPane.putClientProperty(key, value));
/*      */   }
/*      */   
/*      */   private void updateTabbedPanesRecur(Container container, Consumer<JTabbedPane> action) {
/*  316 */     for (Component c : container.getComponents()) {
/*  317 */       if (c instanceof JTabbedPane) {
/*  318 */         JTabbedPane tabPane = (JTabbedPane)c;
/*  319 */         action.accept(tabPane);
/*      */       } 
/*      */       
/*  322 */       if (c instanceof Container) {
/*  323 */         updateTabbedPanesRecur((Container)c, action);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void initComponents() {
/*  329 */     JPanel panel1 = new JPanel();
/*  330 */     JLabel tabPlacementLabel = new JLabel();
/*  331 */     this.tabPlacementToolBar = new JToolBar();
/*  332 */     this.topPlacementButton = new JToggleButton();
/*  333 */     this.bottomPlacementButton = new JToggleButton();
/*  334 */     this.leftPlacementButton = new JToggleButton();
/*  335 */     this.rightPlacementButton = new JToggleButton();
/*  336 */     this.scrollButton = new JToggleButton();
/*  337 */     this.borderButton = new JToggleButton();
/*  338 */     this.tabPlacementTabbedPane = new JTabbedPane();
/*  339 */     JLabel tabLayoutLabel = new JLabel();
/*  340 */     this.tabLayoutToolBar = new JToolBar();
/*  341 */     this.scrollTabLayoutButton = new JToggleButton();
/*  342 */     this.wrapTabLayoutButton = new JToggleButton();
/*  343 */     this.scrollLayoutNoteLabel = new JLabel();
/*  344 */     this.wrapLayoutNoteLabel = new JLabel();
/*  345 */     this.scrollLayoutTabbedPane = new JTabbedPane();
/*  346 */     this.wrapLayoutTabbedPane = new JTabbedPane();
/*  347 */     JLabel closableTabsLabel = new JLabel();
/*  348 */     this.closableTabsToolBar = new JToolBar();
/*  349 */     this.squareCloseButton = new JToggleButton();
/*  350 */     this.circleCloseButton = new JToggleButton();
/*  351 */     this.redCrossCloseButton = new JToggleButton();
/*  352 */     this.closableTabsTabbedPane = new JTabbedPane();
/*  353 */     JLabel tabAreaComponentsLabel = new JLabel();
/*  354 */     this.tabAreaComponentsToolBar = new JToolBar();
/*  355 */     this.leadingComponentButton = new JToggleButton();
/*  356 */     this.trailingComponentButton = new JToggleButton();
/*  357 */     this.customComponentsTabbedPane = new JTabbedPane();
/*  358 */     JPanel panel2 = new JPanel();
/*  359 */     JLabel tabIconPlacementLabel = new JLabel();
/*  360 */     JLabel tabIconPlacementNodeLabel = new JLabel();
/*  361 */     this.iconTopTabbedPane = new JTabbedPane();
/*  362 */     this.iconBottomTabbedPane = new JTabbedPane();
/*  363 */     this.iconLeadingTabbedPane = new JTabbedPane();
/*  364 */     this.iconTrailingTabbedPane = new JTabbedPane();
/*  365 */     JLabel tabAreaAlignmentLabel = new JLabel();
/*  366 */     JLabel tabAreaAlignmentNoteLabel = new JLabel();
/*  367 */     this.alignLeadingTabbedPane = new JTabbedPane();
/*  368 */     this.alignCenterTabbedPane = new JTabbedPane();
/*  369 */     this.alignTrailingTabbedPane = new JTabbedPane();
/*  370 */     this.alignFillTabbedPane = new JTabbedPane();
/*  371 */     JPanel panel3 = new JPanel();
/*  372 */     JLabel tabWidthModeLabel = new JLabel();
/*  373 */     JLabel tabWidthModeNoteLabel = new JLabel();
/*  374 */     this.widthPreferredTabbedPane = new JTabbedPane();
/*  375 */     this.widthEqualTabbedPane = new JTabbedPane();
/*  376 */     this.widthCompactTabbedPane = new JTabbedPane();
/*  377 */     JLabel minMaxTabWidthLabel = new JLabel();
/*  378 */     this.minimumTabWidthTabbedPane = new JTabbedPane();
/*  379 */     this.maximumTabWidthTabbedPane = new JTabbedPane();
/*  380 */     JLabel tabAlignmentLabel = new JLabel();
/*  381 */     this.panel5 = new JPanel();
/*  382 */     JLabel tabAlignmentNoteLabel = new JLabel();
/*  383 */     JLabel tabAlignmentNoteLabel2 = new JLabel();
/*  384 */     this.tabAlignLeadingTabbedPane = new JTabbedPane();
/*  385 */     this.tabAlignVerticalTabbedPane = new JTabbedPane();
/*  386 */     this.tabAlignCenterTabbedPane = new JTabbedPane();
/*  387 */     this.tabAlignTrailingTabbedPane = new JTabbedPane();
/*  388 */     this.separator2 = new JSeparator();
/*  389 */     JPanel panel4 = new JPanel();
/*  390 */     this.scrollButtonsPolicyLabel = new JLabel();
/*  391 */     this.scrollButtonsPolicyToolBar = new JToolBar();
/*  392 */     this.scrollAsNeededSingleButton = new JToggleButton();
/*  393 */     this.scrollAsNeededButton = new JToggleButton();
/*  394 */     this.scrollNeverButton = new JToggleButton();
/*  395 */     this.scrollButtonsPlacementLabel = new JLabel();
/*  396 */     this.scrollButtonsPlacementToolBar = new JToolBar();
/*  397 */     this.scrollBothButton = new JToggleButton();
/*  398 */     this.scrollTrailingButton = new JToggleButton();
/*  399 */     this.tabsPopupPolicyLabel = new JLabel();
/*  400 */     this.tabsPopupPolicyToolBar = new JToolBar();
/*  401 */     this.popupAsNeededButton = new JToggleButton();
/*  402 */     this.popupNeverButton = new JToggleButton();
/*  403 */     this.showTabSeparatorsCheckBox = new JCheckBox();
/*      */ 
/*      */     
/*  406 */     setName("this");
/*  407 */     setLayout((LayoutManager)new MigLayout("insets dialog,hidemode 3", "[grow,fill]para[fill]para[fill]", "[grow,fill]para[][]"));
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
/*  420 */     panel1.setName("panel1");
/*  421 */     panel1.setLayout((LayoutManager)new MigLayout("insets 0,hidemode 3", "[grow,fill]", "[][fill]para[]0[][]para[][]para[][]"));
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
/*  437 */     tabPlacementLabel.setText("Tab placement");
/*  438 */     tabPlacementLabel.setFont(tabPlacementLabel.getFont().deriveFont(tabPlacementLabel.getFont().getSize() + 4.0F));
/*  439 */     tabPlacementLabel.setName("tabPlacementLabel");
/*  440 */     panel1.add(tabPlacementLabel, "cell 0 0");
/*      */ 
/*      */ 
/*      */     
/*  444 */     this.tabPlacementToolBar.setFloatable(false);
/*  445 */     this.tabPlacementToolBar.setBorder(BorderFactory.createEmptyBorder());
/*  446 */     this.tabPlacementToolBar.setName("tabPlacementToolBar");
/*      */ 
/*      */     
/*  449 */     this.topPlacementButton.setText("top");
/*  450 */     this.topPlacementButton.setSelected(true);
/*  451 */     this.topPlacementButton.setFont(this.topPlacementButton.getFont().deriveFont(this.topPlacementButton.getFont().getSize() - 2.0F));
/*  452 */     this.topPlacementButton.setName("topPlacementButton");
/*  453 */     this.topPlacementButton.addActionListener(e -> tabPlacementChanged());
/*  454 */     this.tabPlacementToolBar.add(this.topPlacementButton);
/*      */ 
/*      */     
/*  457 */     this.bottomPlacementButton.setText("bottom");
/*  458 */     this.bottomPlacementButton.setFont(this.bottomPlacementButton.getFont().deriveFont(this.bottomPlacementButton.getFont().getSize() - 2.0F));
/*  459 */     this.bottomPlacementButton.setName("bottomPlacementButton");
/*  460 */     this.bottomPlacementButton.addActionListener(e -> tabPlacementChanged());
/*  461 */     this.tabPlacementToolBar.add(this.bottomPlacementButton);
/*      */ 
/*      */     
/*  464 */     this.leftPlacementButton.setText("left");
/*  465 */     this.leftPlacementButton.setFont(this.leftPlacementButton.getFont().deriveFont(this.leftPlacementButton.getFont().getSize() - 2.0F));
/*  466 */     this.leftPlacementButton.setName("leftPlacementButton");
/*  467 */     this.leftPlacementButton.addActionListener(e -> tabPlacementChanged());
/*  468 */     this.tabPlacementToolBar.add(this.leftPlacementButton);
/*      */ 
/*      */     
/*  471 */     this.rightPlacementButton.setText("right");
/*  472 */     this.rightPlacementButton.setFont(this.rightPlacementButton.getFont().deriveFont(this.rightPlacementButton.getFont().getSize() - 2.0F));
/*  473 */     this.rightPlacementButton.setName("rightPlacementButton");
/*  474 */     this.rightPlacementButton.addActionListener(e -> tabPlacementChanged());
/*  475 */     this.tabPlacementToolBar.add(this.rightPlacementButton);
/*  476 */     this.tabPlacementToolBar.addSeparator();
/*      */ 
/*      */     
/*  479 */     this.scrollButton.setText("scroll");
/*  480 */     this.scrollButton.setFont(this.scrollButton.getFont().deriveFont(this.scrollButton.getFont().getSize() - 2.0F));
/*  481 */     this.scrollButton.setName("scrollButton");
/*  482 */     this.scrollButton.addActionListener(e -> scrollChanged());
/*  483 */     this.tabPlacementToolBar.add(this.scrollButton);
/*      */ 
/*      */     
/*  486 */     this.borderButton.setText("border");
/*  487 */     this.borderButton.setFont(this.borderButton.getFont().deriveFont(this.borderButton.getFont().getSize() - 2.0F));
/*  488 */     this.borderButton.setName("borderButton");
/*  489 */     this.borderButton.addActionListener(e -> borderChanged());
/*  490 */     this.tabPlacementToolBar.add(this.borderButton);
/*      */     
/*  492 */     panel1.add(this.tabPlacementToolBar, "cell 0 0,alignx right,growx 0");
/*      */ 
/*      */ 
/*      */     
/*  496 */     this.tabPlacementTabbedPane.setName("tabPlacementTabbedPane");
/*      */     
/*  498 */     panel1.add(this.tabPlacementTabbedPane, "cell 0 1,width 300:300,height 100:100");
/*      */ 
/*      */     
/*  501 */     tabLayoutLabel.setText("Tab layout");
/*  502 */     tabLayoutLabel.setFont(tabLayoutLabel.getFont().deriveFont(tabLayoutLabel.getFont().getSize() + 4.0F));
/*  503 */     tabLayoutLabel.setName("tabLayoutLabel");
/*  504 */     panel1.add(tabLayoutLabel, "cell 0 2");
/*      */ 
/*      */ 
/*      */     
/*  508 */     this.tabLayoutToolBar.setFloatable(false);
/*  509 */     this.tabLayoutToolBar.setBorder(BorderFactory.createEmptyBorder());
/*  510 */     this.tabLayoutToolBar.setName("tabLayoutToolBar");
/*      */ 
/*      */     
/*  513 */     this.scrollTabLayoutButton.setText("scroll");
/*  514 */     this.scrollTabLayoutButton.setFont(this.scrollTabLayoutButton.getFont().deriveFont(this.scrollTabLayoutButton.getFont().getSize() - 2.0F));
/*  515 */     this.scrollTabLayoutButton.setSelected(true);
/*  516 */     this.scrollTabLayoutButton.setName("scrollTabLayoutButton");
/*  517 */     this.scrollTabLayoutButton.addActionListener(e -> tabLayoutChanged());
/*  518 */     this.tabLayoutToolBar.add(this.scrollTabLayoutButton);
/*      */ 
/*      */     
/*  521 */     this.wrapTabLayoutButton.setText("wrap");
/*  522 */     this.wrapTabLayoutButton.setFont(this.wrapTabLayoutButton.getFont().deriveFont(this.wrapTabLayoutButton.getFont().getSize() - 2.0F));
/*  523 */     this.wrapTabLayoutButton.setName("wrapTabLayoutButton");
/*  524 */     this.wrapTabLayoutButton.addActionListener(e -> tabLayoutChanged());
/*  525 */     this.tabLayoutToolBar.add(this.wrapTabLayoutButton);
/*      */     
/*  527 */     panel1.add(this.tabLayoutToolBar, "cell 0 2,alignx right,growx 0");
/*      */ 
/*      */     
/*  530 */     this.scrollLayoutNoteLabel.setText("(use mouse wheel to scroll; arrow button shows hidden tabs)");
/*  531 */     this.scrollLayoutNoteLabel.setEnabled(false);
/*  532 */     this.scrollLayoutNoteLabel.setFont(this.scrollLayoutNoteLabel.getFont().deriveFont(this.scrollLayoutNoteLabel.getFont().getSize() - 2.0F));
/*  533 */     this.scrollLayoutNoteLabel.setName("scrollLayoutNoteLabel");
/*  534 */     panel1.add(this.scrollLayoutNoteLabel, "cell 0 3");
/*      */ 
/*      */     
/*  537 */     this.wrapLayoutNoteLabel.setText("(probably better to use scroll layout?)");
/*  538 */     this.wrapLayoutNoteLabel.setEnabled(false);
/*  539 */     this.wrapLayoutNoteLabel.setFont(this.wrapLayoutNoteLabel.getFont().deriveFont(this.wrapLayoutNoteLabel.getFont().getSize() - 2.0F));
/*  540 */     this.wrapLayoutNoteLabel.setName("wrapLayoutNoteLabel");
/*  541 */     panel1.add(this.wrapLayoutNoteLabel, "cell 0 3");
/*      */ 
/*      */ 
/*      */     
/*  545 */     this.scrollLayoutTabbedPane.setName("scrollLayoutTabbedPane");
/*      */     
/*  547 */     panel1.add(this.scrollLayoutTabbedPane, "cell 0 4");
/*      */ 
/*      */ 
/*      */     
/*  551 */     this.wrapLayoutTabbedPane.setName("wrapLayoutTabbedPane");
/*      */     
/*  553 */     panel1.add(this.wrapLayoutTabbedPane, "cell 0 4,width 100:100,height pref*2px");
/*      */ 
/*      */     
/*  556 */     closableTabsLabel.setText("Closable tabs");
/*  557 */     closableTabsLabel.setFont(closableTabsLabel.getFont().deriveFont(closableTabsLabel.getFont().getSize() + 4.0F));
/*  558 */     closableTabsLabel.setName("closableTabsLabel");
/*  559 */     panel1.add(closableTabsLabel, "cell 0 5");
/*      */ 
/*      */ 
/*      */     
/*  563 */     this.closableTabsToolBar.setFloatable(false);
/*  564 */     this.closableTabsToolBar.setBorder(BorderFactory.createEmptyBorder());
/*  565 */     this.closableTabsToolBar.setName("closableTabsToolBar");
/*      */ 
/*      */     
/*  568 */     this.squareCloseButton.setText("square");
/*  569 */     this.squareCloseButton.setFont(this.squareCloseButton.getFont().deriveFont(this.squareCloseButton.getFont().getSize() - 2.0F));
/*  570 */     this.squareCloseButton.setSelected(true);
/*  571 */     this.squareCloseButton.setName("squareCloseButton");
/*  572 */     this.squareCloseButton.addActionListener(e -> closeButtonStyleChanged());
/*  573 */     this.closableTabsToolBar.add(this.squareCloseButton);
/*      */ 
/*      */     
/*  576 */     this.circleCloseButton.setText("circle");
/*  577 */     this.circleCloseButton.setFont(this.circleCloseButton.getFont().deriveFont(this.circleCloseButton.getFont().getSize() - 2.0F));
/*  578 */     this.circleCloseButton.setName("circleCloseButton");
/*  579 */     this.circleCloseButton.addActionListener(e -> closeButtonStyleChanged());
/*  580 */     this.closableTabsToolBar.add(this.circleCloseButton);
/*      */ 
/*      */     
/*  583 */     this.redCrossCloseButton.setText("red cross");
/*  584 */     this.redCrossCloseButton.setFont(this.redCrossCloseButton.getFont().deriveFont(this.redCrossCloseButton.getFont().getSize() - 2.0F));
/*  585 */     this.redCrossCloseButton.setName("redCrossCloseButton");
/*  586 */     this.redCrossCloseButton.addActionListener(e -> closeButtonStyleChanged());
/*  587 */     this.closableTabsToolBar.add(this.redCrossCloseButton);
/*      */     
/*  589 */     panel1.add(this.closableTabsToolBar, "cell 0 5,alignx right,growx 0");
/*      */ 
/*      */ 
/*      */     
/*  593 */     this.closableTabsTabbedPane.setName("closableTabsTabbedPane");
/*      */     
/*  595 */     panel1.add(this.closableTabsTabbedPane, "cell 0 6");
/*      */ 
/*      */     
/*  598 */     tabAreaComponentsLabel.setText("Custom tab area components");
/*  599 */     tabAreaComponentsLabel.setFont(tabAreaComponentsLabel.getFont().deriveFont(tabAreaComponentsLabel.getFont().getSize() + 4.0F));
/*  600 */     tabAreaComponentsLabel.setName("tabAreaComponentsLabel");
/*  601 */     panel1.add(tabAreaComponentsLabel, "cell 0 7");
/*      */ 
/*      */ 
/*      */     
/*  605 */     this.tabAreaComponentsToolBar.setFloatable(false);
/*  606 */     this.tabAreaComponentsToolBar.setBorder(BorderFactory.createEmptyBorder());
/*  607 */     this.tabAreaComponentsToolBar.setName("tabAreaComponentsToolBar");
/*      */ 
/*      */     
/*  610 */     this.leadingComponentButton.setText("leading");
/*  611 */     this.leadingComponentButton.setFont(this.leadingComponentButton.getFont().deriveFont(this.leadingComponentButton.getFont().getSize() - 2.0F));
/*  612 */     this.leadingComponentButton.setSelected(true);
/*  613 */     this.leadingComponentButton.setName("leadingComponentButton");
/*  614 */     this.leadingComponentButton.addActionListener(e -> customComponentsChanged());
/*  615 */     this.tabAreaComponentsToolBar.add(this.leadingComponentButton);
/*      */ 
/*      */     
/*  618 */     this.trailingComponentButton.setText("trailing");
/*  619 */     this.trailingComponentButton.setFont(this.trailingComponentButton.getFont().deriveFont(this.trailingComponentButton.getFont().getSize() - 2.0F));
/*  620 */     this.trailingComponentButton.setSelected(true);
/*  621 */     this.trailingComponentButton.setName("trailingComponentButton");
/*  622 */     this.trailingComponentButton.addActionListener(e -> customComponentsChanged());
/*  623 */     this.tabAreaComponentsToolBar.add(this.trailingComponentButton);
/*      */     
/*  625 */     panel1.add(this.tabAreaComponentsToolBar, "cell 0 7,alignx right,growx 0");
/*      */ 
/*      */ 
/*      */     
/*  629 */     this.customComponentsTabbedPane.setName("customComponentsTabbedPane");
/*      */     
/*  631 */     panel1.add(this.customComponentsTabbedPane, "cell 0 8");
/*      */     
/*  633 */     add(panel1, "cell 0 0");
/*      */ 
/*      */ 
/*      */     
/*  637 */     panel2.setName("panel2");
/*  638 */     panel2.setLayout((LayoutManager)new MigLayout("insets 0,hidemode 3", "[grow,fill]", "[]0[][fill][center][center][center]para[center]0[][center][center][center][]"));
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
/*  657 */     tabIconPlacementLabel.setText("Tab icon placement");
/*  658 */     tabIconPlacementLabel.setFont(tabIconPlacementLabel.getFont().deriveFont(tabIconPlacementLabel.getFont().getSize() + 4.0F));
/*  659 */     tabIconPlacementLabel.setName("tabIconPlacementLabel");
/*  660 */     panel2.add(tabIconPlacementLabel, "cell 0 0");
/*      */ 
/*      */     
/*  663 */     tabIconPlacementNodeLabel.setText("(top/bottom/leading/trailing)");
/*  664 */     tabIconPlacementNodeLabel.setEnabled(false);
/*  665 */     tabIconPlacementNodeLabel.setFont(tabIconPlacementNodeLabel.getFont().deriveFont(tabIconPlacementNodeLabel.getFont().getSize() - 2.0F));
/*  666 */     tabIconPlacementNodeLabel.setName("tabIconPlacementNodeLabel");
/*  667 */     panel2.add(tabIconPlacementNodeLabel, "cell 0 1");
/*      */ 
/*      */ 
/*      */     
/*  671 */     this.iconTopTabbedPane.setName("iconTopTabbedPane");
/*      */     
/*  673 */     panel2.add(this.iconTopTabbedPane, "cell 0 2");
/*      */ 
/*      */ 
/*      */     
/*  677 */     this.iconBottomTabbedPane.setName("iconBottomTabbedPane");
/*      */     
/*  679 */     panel2.add(this.iconBottomTabbedPane, "cell 0 3");
/*      */ 
/*      */ 
/*      */     
/*  683 */     this.iconLeadingTabbedPane.setName("iconLeadingTabbedPane");
/*      */     
/*  685 */     panel2.add(this.iconLeadingTabbedPane, "cell 0 4");
/*      */ 
/*      */ 
/*      */     
/*  689 */     this.iconTrailingTabbedPane.setName("iconTrailingTabbedPane");
/*      */     
/*  691 */     panel2.add(this.iconTrailingTabbedPane, "cell 0 5");
/*      */ 
/*      */     
/*  694 */     tabAreaAlignmentLabel.setText("Tab area alignment");
/*  695 */     tabAreaAlignmentLabel.setFont(tabAreaAlignmentLabel.getFont().deriveFont(tabAreaAlignmentLabel.getFont().getSize() + 4.0F));
/*  696 */     tabAreaAlignmentLabel.setName("tabAreaAlignmentLabel");
/*  697 */     panel2.add(tabAreaAlignmentLabel, "cell 0 6");
/*      */ 
/*      */     
/*  700 */     tabAreaAlignmentNoteLabel.setText("(leading/center/trailing/fill)");
/*  701 */     tabAreaAlignmentNoteLabel.setEnabled(false);
/*  702 */     tabAreaAlignmentNoteLabel.setFont(tabAreaAlignmentNoteLabel.getFont().deriveFont(tabAreaAlignmentNoteLabel.getFont().getSize() - 2.0F));
/*  703 */     tabAreaAlignmentNoteLabel.setName("tabAreaAlignmentNoteLabel");
/*  704 */     panel2.add(tabAreaAlignmentNoteLabel, "cell 0 7");
/*      */ 
/*      */ 
/*      */     
/*  708 */     this.alignLeadingTabbedPane.setName("alignLeadingTabbedPane");
/*      */     
/*  710 */     panel2.add(this.alignLeadingTabbedPane, "cell 0 8");
/*      */ 
/*      */ 
/*      */     
/*  714 */     this.alignCenterTabbedPane.setName("alignCenterTabbedPane");
/*      */     
/*  716 */     panel2.add(this.alignCenterTabbedPane, "cell 0 9");
/*      */ 
/*      */ 
/*      */     
/*  720 */     this.alignTrailingTabbedPane.setName("alignTrailingTabbedPane");
/*      */     
/*  722 */     panel2.add(this.alignTrailingTabbedPane, "cell 0 10");
/*      */ 
/*      */ 
/*      */     
/*  726 */     this.alignFillTabbedPane.setName("alignFillTabbedPane");
/*      */     
/*  728 */     panel2.add(this.alignFillTabbedPane, "cell 0 11");
/*      */     
/*  730 */     add(panel2, "cell 1 0,growy");
/*      */ 
/*      */ 
/*      */     
/*  734 */     panel3.setName("panel3");
/*  735 */     panel3.setLayout((LayoutManager)new MigLayout("insets 0,hidemode 3", "[grow,fill]", "[]0[][][][]para[][][]para[]0[]"));
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
/*  752 */     tabWidthModeLabel.setText("Tab width mode");
/*  753 */     tabWidthModeLabel.setFont(tabWidthModeLabel.getFont().deriveFont(tabWidthModeLabel.getFont().getSize() + 4.0F));
/*  754 */     tabWidthModeLabel.setName("tabWidthModeLabel");
/*  755 */     panel3.add(tabWidthModeLabel, "cell 0 0");
/*      */ 
/*      */     
/*  758 */     tabWidthModeNoteLabel.setText("(preferred/equal/compact)");
/*  759 */     tabWidthModeNoteLabel.setFont(tabWidthModeNoteLabel.getFont().deriveFont(tabWidthModeNoteLabel.getFont().getSize() - 2.0F));
/*  760 */     tabWidthModeNoteLabel.setEnabled(false);
/*  761 */     tabWidthModeNoteLabel.setName("tabWidthModeNoteLabel");
/*  762 */     panel3.add(tabWidthModeNoteLabel, "cell 0 1");
/*      */ 
/*      */ 
/*      */     
/*  766 */     this.widthPreferredTabbedPane.setName("widthPreferredTabbedPane");
/*      */     
/*  768 */     panel3.add(this.widthPreferredTabbedPane, "cell 0 2");
/*      */ 
/*      */ 
/*      */     
/*  772 */     this.widthEqualTabbedPane.setName("widthEqualTabbedPane");
/*      */     
/*  774 */     panel3.add(this.widthEqualTabbedPane, "cell 0 3");
/*      */ 
/*      */ 
/*      */     
/*  778 */     this.widthCompactTabbedPane.setName("widthCompactTabbedPane");
/*      */     
/*  780 */     panel3.add(this.widthCompactTabbedPane, "cell 0 4");
/*      */ 
/*      */     
/*  783 */     minMaxTabWidthLabel.setText("Minimum/maximum tab width");
/*  784 */     minMaxTabWidthLabel.setFont(minMaxTabWidthLabel.getFont().deriveFont(minMaxTabWidthLabel.getFont().getSize() + 4.0F));
/*  785 */     minMaxTabWidthLabel.setName("minMaxTabWidthLabel");
/*  786 */     panel3.add(minMaxTabWidthLabel, "cell 0 5");
/*      */ 
/*      */ 
/*      */     
/*  790 */     this.minimumTabWidthTabbedPane.setName("minimumTabWidthTabbedPane");
/*      */     
/*  792 */     panel3.add(this.minimumTabWidthTabbedPane, "cell 0 6");
/*      */ 
/*      */ 
/*      */     
/*  796 */     this.maximumTabWidthTabbedPane.setName("maximumTabWidthTabbedPane");
/*      */     
/*  798 */     panel3.add(this.maximumTabWidthTabbedPane, "cell 0 7");
/*      */ 
/*      */     
/*  801 */     tabAlignmentLabel.setText("Tab title alignment");
/*  802 */     tabAlignmentLabel.setFont(tabAlignmentLabel.getFont().deriveFont(tabAlignmentLabel.getFont().getSize() + 4.0F));
/*  803 */     tabAlignmentLabel.setName("tabAlignmentLabel");
/*  804 */     panel3.add(tabAlignmentLabel, "cell 0 8");
/*      */ 
/*      */ 
/*      */     
/*  808 */     this.panel5.setName("panel5");
/*  809 */     this.panel5.setLayout((LayoutManager)new MigLayout("insets 0,hidemode 3", "[grow,fill]para[fill]", "[][][][]"));
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
/*  821 */     tabAlignmentNoteLabel.setText("(leading/center/trailing)");
/*  822 */     tabAlignmentNoteLabel.setEnabled(false);
/*  823 */     tabAlignmentNoteLabel.setFont(tabAlignmentNoteLabel.getFont().deriveFont(tabAlignmentNoteLabel.getFont().getSize() - 2.0F));
/*  824 */     tabAlignmentNoteLabel.setName("tabAlignmentNoteLabel");
/*  825 */     this.panel5.add(tabAlignmentNoteLabel, "cell 0 0");
/*      */ 
/*      */     
/*  828 */     tabAlignmentNoteLabel2.setText("(trailing)");
/*  829 */     tabAlignmentNoteLabel2.setEnabled(false);
/*  830 */     tabAlignmentNoteLabel2.setFont(tabAlignmentNoteLabel2.getFont().deriveFont(tabAlignmentNoteLabel2.getFont().getSize() - 2.0F));
/*  831 */     tabAlignmentNoteLabel2.setName("tabAlignmentNoteLabel2");
/*  832 */     this.panel5.add(tabAlignmentNoteLabel2, "cell 1 0,alignx right,growx 0");
/*      */ 
/*      */ 
/*      */     
/*  836 */     this.tabAlignLeadingTabbedPane.setName("tabAlignLeadingTabbedPane");
/*      */     
/*  838 */     this.panel5.add(this.tabAlignLeadingTabbedPane, "cell 0 1");
/*      */ 
/*      */ 
/*      */     
/*  842 */     this.tabAlignVerticalTabbedPane.setTabPlacement(2);
/*  843 */     this.tabAlignVerticalTabbedPane.setName("tabAlignVerticalTabbedPane");
/*      */     
/*  845 */     this.panel5.add(this.tabAlignVerticalTabbedPane, "cell 1 1 1 3,growy");
/*      */ 
/*      */ 
/*      */     
/*  849 */     this.tabAlignCenterTabbedPane.setName("tabAlignCenterTabbedPane");
/*      */     
/*  851 */     this.panel5.add(this.tabAlignCenterTabbedPane, "cell 0 2");
/*      */ 
/*      */ 
/*      */     
/*  855 */     this.tabAlignTrailingTabbedPane.setName("tabAlignTrailingTabbedPane");
/*      */     
/*  857 */     this.panel5.add(this.tabAlignTrailingTabbedPane, "cell 0 3");
/*      */     
/*  859 */     panel3.add(this.panel5, "cell 0 9");
/*      */     
/*  861 */     add(panel3, "cell 2 0");
/*      */ 
/*      */     
/*  864 */     this.separator2.setName("separator2");
/*  865 */     add(this.separator2, "cell 0 1 3 1");
/*      */ 
/*      */ 
/*      */     
/*  869 */     panel4.setName("panel4");
/*  870 */     panel4.setLayout((LayoutManager)new MigLayout("insets 0,hidemode 3", "[][fill]para[fill][fill]para", "[][center]"));
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
/*  882 */     this.scrollButtonsPolicyLabel.setText("Scroll buttons policy:");
/*  883 */     this.scrollButtonsPolicyLabel.setName("scrollButtonsPolicyLabel");
/*  884 */     panel4.add(this.scrollButtonsPolicyLabel, "cell 0 0");
/*      */ 
/*      */ 
/*      */     
/*  888 */     this.scrollButtonsPolicyToolBar.setFloatable(false);
/*  889 */     this.scrollButtonsPolicyToolBar.setBorder(BorderFactory.createEmptyBorder());
/*  890 */     this.scrollButtonsPolicyToolBar.setName("scrollButtonsPolicyToolBar");
/*      */ 
/*      */     
/*  893 */     this.scrollAsNeededSingleButton.setText("asNeededSingle");
/*  894 */     this.scrollAsNeededSingleButton.setFont(this.scrollAsNeededSingleButton.getFont().deriveFont(this.scrollAsNeededSingleButton.getFont().getSize() - 2.0F));
/*  895 */     this.scrollAsNeededSingleButton.setSelected(true);
/*  896 */     this.scrollAsNeededSingleButton.setName("scrollAsNeededSingleButton");
/*  897 */     this.scrollAsNeededSingleButton.addActionListener(e -> scrollButtonsPolicyChanged());
/*  898 */     this.scrollButtonsPolicyToolBar.add(this.scrollAsNeededSingleButton);
/*      */ 
/*      */     
/*  901 */     this.scrollAsNeededButton.setText("asNeeded");
/*  902 */     this.scrollAsNeededButton.setFont(this.scrollAsNeededButton.getFont().deriveFont(this.scrollAsNeededButton.getFont().getSize() - 2.0F));
/*  903 */     this.scrollAsNeededButton.setName("scrollAsNeededButton");
/*  904 */     this.scrollAsNeededButton.addActionListener(e -> scrollButtonsPolicyChanged());
/*  905 */     this.scrollButtonsPolicyToolBar.add(this.scrollAsNeededButton);
/*      */ 
/*      */     
/*  908 */     this.scrollNeverButton.setText("never");
/*  909 */     this.scrollNeverButton.setFont(this.scrollNeverButton.getFont().deriveFont(this.scrollNeverButton.getFont().getSize() - 2.0F));
/*  910 */     this.scrollNeverButton.setName("scrollNeverButton");
/*  911 */     this.scrollNeverButton.addActionListener(e -> scrollButtonsPolicyChanged());
/*  912 */     this.scrollButtonsPolicyToolBar.add(this.scrollNeverButton);
/*      */     
/*  914 */     panel4.add(this.scrollButtonsPolicyToolBar, "cell 1 0");
/*      */ 
/*      */     
/*  917 */     this.scrollButtonsPlacementLabel.setText("Scroll buttons placement:");
/*  918 */     this.scrollButtonsPlacementLabel.setName("scrollButtonsPlacementLabel");
/*  919 */     panel4.add(this.scrollButtonsPlacementLabel, "cell 2 0");
/*      */ 
/*      */ 
/*      */     
/*  923 */     this.scrollButtonsPlacementToolBar.setFloatable(false);
/*  924 */     this.scrollButtonsPlacementToolBar.setBorder(BorderFactory.createEmptyBorder());
/*  925 */     this.scrollButtonsPlacementToolBar.setName("scrollButtonsPlacementToolBar");
/*      */ 
/*      */     
/*  928 */     this.scrollBothButton.setText("both");
/*  929 */     this.scrollBothButton.setFont(this.scrollBothButton.getFont().deriveFont(this.scrollBothButton.getFont().getSize() - 2.0F));
/*  930 */     this.scrollBothButton.setSelected(true);
/*  931 */     this.scrollBothButton.setName("scrollBothButton");
/*  932 */     this.scrollBothButton.addActionListener(e -> scrollButtonsPlacementChanged());
/*  933 */     this.scrollButtonsPlacementToolBar.add(this.scrollBothButton);
/*      */ 
/*      */     
/*  936 */     this.scrollTrailingButton.setText("trailing");
/*  937 */     this.scrollTrailingButton.setFont(this.scrollTrailingButton.getFont().deriveFont(this.scrollTrailingButton.getFont().getSize() - 2.0F));
/*  938 */     this.scrollTrailingButton.setName("scrollTrailingButton");
/*  939 */     this.scrollTrailingButton.addActionListener(e -> scrollButtonsPlacementChanged());
/*  940 */     this.scrollButtonsPlacementToolBar.add(this.scrollTrailingButton);
/*      */     
/*  942 */     panel4.add(this.scrollButtonsPlacementToolBar, "cell 3 0");
/*      */ 
/*      */     
/*  945 */     this.tabsPopupPolicyLabel.setText("Tabs popup policy:");
/*  946 */     this.tabsPopupPolicyLabel.setName("tabsPopupPolicyLabel");
/*  947 */     panel4.add(this.tabsPopupPolicyLabel, "cell 0 1");
/*      */ 
/*      */ 
/*      */     
/*  951 */     this.tabsPopupPolicyToolBar.setFloatable(false);
/*  952 */     this.tabsPopupPolicyToolBar.setBorder(BorderFactory.createEmptyBorder());
/*  953 */     this.tabsPopupPolicyToolBar.setName("tabsPopupPolicyToolBar");
/*      */ 
/*      */     
/*  956 */     this.popupAsNeededButton.setText("asNeeded");
/*  957 */     this.popupAsNeededButton.setFont(this.popupAsNeededButton.getFont().deriveFont(this.popupAsNeededButton.getFont().getSize() - 2.0F));
/*  958 */     this.popupAsNeededButton.setSelected(true);
/*  959 */     this.popupAsNeededButton.setName("popupAsNeededButton");
/*  960 */     this.popupAsNeededButton.addActionListener(e -> tabsPopupPolicyChanged());
/*  961 */     this.tabsPopupPolicyToolBar.add(this.popupAsNeededButton);
/*      */ 
/*      */     
/*  964 */     this.popupNeverButton.setText("never");
/*  965 */     this.popupNeverButton.setFont(this.popupNeverButton.getFont().deriveFont(this.popupNeverButton.getFont().getSize() - 2.0F));
/*  966 */     this.popupNeverButton.setName("popupNeverButton");
/*  967 */     this.popupNeverButton.addActionListener(e -> tabsPopupPolicyChanged());
/*  968 */     this.tabsPopupPolicyToolBar.add(this.popupNeverButton);
/*      */     
/*  970 */     panel4.add(this.tabsPopupPolicyToolBar, "cell 1 1");
/*      */ 
/*      */     
/*  973 */     this.showTabSeparatorsCheckBox.setText("Show tab separators");
/*  974 */     this.showTabSeparatorsCheckBox.setName("showTabSeparatorsCheckBox");
/*  975 */     this.showTabSeparatorsCheckBox.addActionListener(e -> showTabSeparatorsChanged());
/*  976 */     panel4.add(this.showTabSeparatorsCheckBox, "cell 2 1 2 1");
/*      */     
/*  978 */     add(panel4, "cell 0 2 3 1");
/*      */ 
/*      */     
/*  981 */     ButtonGroup tabPlacementButtonGroup = new ButtonGroup();
/*  982 */     tabPlacementButtonGroup.add(this.topPlacementButton);
/*  983 */     tabPlacementButtonGroup.add(this.bottomPlacementButton);
/*  984 */     tabPlacementButtonGroup.add(this.leftPlacementButton);
/*  985 */     tabPlacementButtonGroup.add(this.rightPlacementButton);
/*      */ 
/*      */     
/*  988 */     ButtonGroup tabLayoutButtonGroup = new ButtonGroup();
/*  989 */     tabLayoutButtonGroup.add(this.scrollTabLayoutButton);
/*  990 */     tabLayoutButtonGroup.add(this.wrapTabLayoutButton);
/*      */ 
/*      */     
/*  993 */     ButtonGroup closableTabsButtonGroup = new ButtonGroup();
/*  994 */     closableTabsButtonGroup.add(this.squareCloseButton);
/*  995 */     closableTabsButtonGroup.add(this.circleCloseButton);
/*  996 */     closableTabsButtonGroup.add(this.redCrossCloseButton);
/*      */ 
/*      */     
/*  999 */     ButtonGroup scrollButtonsPolicyButtonGroup = new ButtonGroup();
/* 1000 */     scrollButtonsPolicyButtonGroup.add(this.scrollAsNeededSingleButton);
/* 1001 */     scrollButtonsPolicyButtonGroup.add(this.scrollAsNeededButton);
/* 1002 */     scrollButtonsPolicyButtonGroup.add(this.scrollNeverButton);
/*      */ 
/*      */     
/* 1005 */     ButtonGroup scrollButtonsPlacementButtonGroup = new ButtonGroup();
/* 1006 */     scrollButtonsPlacementButtonGroup.add(this.scrollBothButton);
/* 1007 */     scrollButtonsPlacementButtonGroup.add(this.scrollTrailingButton);
/*      */ 
/*      */     
/* 1010 */     ButtonGroup tabsPopupPolicyButtonGroup = new ButtonGroup();
/* 1011 */     tabsPopupPolicyButtonGroup.add(this.popupAsNeededButton);
/* 1012 */     tabsPopupPolicyButtonGroup.add(this.popupNeverButton);
/*      */ 
/*      */     
/* 1015 */     if (FlatLafDemo.screenshotsMode) {
/* 1016 */       Component[] components = { tabPlacementLabel, this.tabPlacementToolBar, this.tabPlacementTabbedPane, this.iconBottomTabbedPane, this.iconTrailingTabbedPane, this.alignLeadingTabbedPane, this.alignTrailingTabbedPane, this.alignFillTabbedPane, panel3, this.separator2, panel4 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1023 */       for (Component c : components) {
/* 1024 */         c.setVisible(false);
/*      */       }
/*      */       
/* 1027 */       MigLayout layout1 = (MigLayout)panel1.getLayout();
/* 1028 */       AC rowSpecs1 = ConstraintParser.parseRowConstraints((String)layout1.getRowConstraints());
/* 1029 */       rowSpecs1.gap("0!", new int[] { 0, 1 });
/* 1030 */       layout1.setRowConstraints(rowSpecs1);
/*      */       
/* 1032 */       MigLayout layout2 = (MigLayout)panel2.getLayout();
/* 1033 */       AC rowSpecs2 = ConstraintParser.parseRowConstraints((String)layout2.getRowConstraints());
/* 1034 */       rowSpecs2.gap("0!", new int[] { 2, 4, 8 });
/* 1035 */       layout2.setRowConstraints(rowSpecs2);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\demo\TabsPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */