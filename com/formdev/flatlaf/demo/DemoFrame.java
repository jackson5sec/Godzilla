/*     */ package com.formdev.flatlaf.demo;
/*     */ import com.formdev.flatlaf.FlatLaf;
/*     */ import com.formdev.flatlaf.demo.extras.ExtrasPanel;
/*     */ import com.formdev.flatlaf.demo.intellijthemes.IJThemesPanel;
/*     */ import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
/*     */ import com.formdev.flatlaf.extras.FlatSVGIcon;
/*     */ import com.formdev.flatlaf.extras.FlatSVGUtils;
/*     */ import com.formdev.flatlaf.extras.FlatUIDefaultsInspector;
/*     */ import com.formdev.flatlaf.ui.JBRCustomDecorations;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Font;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.prefs.Preferences;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBoxMenuItem;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JRadioButtonMenuItem;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.JToggleButton;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.text.DefaultEditorKit;
/*     */ import javax.swing.text.StyleContext;
/*     */ import net.miginfocom.layout.LC;
/*     */ import net.miginfocom.layout.UnitValue;
/*     */ import net.miginfocom.swing.MigLayout;
/*     */ 
/*     */ class DemoFrame extends JFrame {
/*     */   private final String[] availableFontFamilyNames;
/*  49 */   private int initialFontMenuItemCount = -1; private JMenu fontMenu; private JMenu optionsMenu;
/*     */   
/*     */   DemoFrame() {
/*  52 */     int tabIndex = DemoPrefs.getState().getInt("tab", 0);
/*     */     
/*  54 */     this
/*  55 */       .availableFontFamilyNames = (String[])GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames().clone();
/*  56 */     Arrays.sort((Object[])this.availableFontFamilyNames);
/*     */     
/*  58 */     initComponents();
/*  59 */     updateFontMenuItems();
/*  60 */     this.controlBar.initialize(this, this.tabbedPane);
/*     */     
/*  62 */     setIconImages(FlatSVGUtils.createWindowIconImages("/com/formdev/flatlaf/demo/FlatLaf.svg"));
/*     */     
/*  64 */     if (tabIndex >= 0 && tabIndex < this.tabbedPane.getTabCount() && tabIndex != this.tabbedPane.getSelectedIndex()) {
/*  65 */       this.tabbedPane.setSelectedIndex(tabIndex);
/*     */     }
/*  67 */     SwingUtilities.invokeLater(() -> showHints());
/*     */   }
/*     */   private JCheckBoxMenuItem windowDecorationsCheckBoxMenuItem; private JCheckBoxMenuItem menuBarEmbeddedCheckBoxMenuItem; private JCheckBoxMenuItem underlineMenuSelectionMenuItem; private JCheckBoxMenuItem alwaysShowMnemonicsMenuItem; private JCheckBoxMenuItem animatedLafChangeMenuItem; private JTabbedPane tabbedPane;
/*     */   private ControlBar controlBar;
/*     */   IJThemesPanel themesPanel;
/*     */   
/*     */   public void dispose() {
/*  74 */     super.dispose();
/*     */     
/*  76 */     FlatUIDefaultsInspector.hide();
/*     */   }
/*     */   
/*     */   private void showHints() {
/*  80 */     HintManager.Hint fontMenuHint = new HintManager.Hint("Use 'Font' menu to increase/decrease font size or try different fonts.", this.fontMenu, 3, "hint.fontMenu", null);
/*     */ 
/*     */ 
/*     */     
/*  84 */     HintManager.Hint optionsMenuHint = new HintManager.Hint("Use 'Options' menu to try out various FlatLaf options.", this.optionsMenu, 3, "hint.optionsMenu", fontMenuHint);
/*     */ 
/*     */ 
/*     */     
/*  88 */     HintManager.Hint themesHint = new HintManager.Hint("Use 'Themes' list to try out various themes.", (Component)this.themesPanel, 2, "hint.themesPanel", optionsMenuHint);
/*     */ 
/*     */ 
/*     */     
/*  92 */     HintManager.showHint(themesHint);
/*     */   }
/*     */   
/*     */   private void clearHints() {
/*  96 */     HintManager.hideAllHints();
/*     */     
/*  98 */     Preferences state = DemoPrefs.getState();
/*  99 */     state.remove("hint.fontMenu");
/* 100 */     state.remove("hint.optionsMenu");
/* 101 */     state.remove("hint.themesPanel");
/*     */   }
/*     */   
/*     */   private void showUIDefaultsInspector() {
/* 105 */     FlatUIDefaultsInspector.show();
/*     */   }
/*     */   
/*     */   private void newActionPerformed() {
/* 109 */     NewDialog newDialog = new NewDialog(this);
/* 110 */     newDialog.setVisible(true);
/*     */   }
/*     */   
/*     */   private void openActionPerformed() {
/* 114 */     JFileChooser chooser = new JFileChooser();
/* 115 */     chooser.showOpenDialog(this);
/*     */   }
/*     */   
/*     */   private void saveAsActionPerformed() {
/* 119 */     JFileChooser chooser = new JFileChooser();
/* 120 */     chooser.showSaveDialog(this);
/*     */   }
/*     */   
/*     */   private void exitActionPerformed() {
/* 124 */     dispose();
/*     */   }
/*     */   
/*     */   private void aboutActionPerformed() {
/* 128 */     JOptionPane.showMessageDialog(this, "FlatLaf Demo", "About", -1);
/*     */   }
/*     */   
/*     */   private void selectedTabChanged() {
/* 132 */     DemoPrefs.getState().putInt("tab", this.tabbedPane.getSelectedIndex());
/*     */   }
/*     */   
/*     */   private void menuItemActionPerformed(ActionEvent e) {
/* 136 */     SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, e.getActionCommand(), "Menu Item", -1));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void windowDecorationsChanged() {
/* 142 */     boolean windowDecorations = this.windowDecorationsCheckBoxMenuItem.isSelected();
/*     */ 
/*     */     
/* 145 */     dispose();
/* 146 */     setUndecorated(windowDecorations);
/* 147 */     getRootPane().setWindowDecorationStyle(windowDecorations ? 1 : 0);
/* 148 */     this.menuBarEmbeddedCheckBoxMenuItem.setEnabled(windowDecorations);
/* 149 */     setVisible(true);
/*     */ 
/*     */     
/* 152 */     JFrame.setDefaultLookAndFeelDecorated(windowDecorations);
/* 153 */     JDialog.setDefaultLookAndFeelDecorated(windowDecorations);
/*     */   }
/*     */   
/*     */   private void menuBarEmbeddedChanged() {
/* 157 */     getRootPane().putClientProperty("JRootPane.menuBarEmbedded", 
/* 158 */         this.menuBarEmbeddedCheckBoxMenuItem.isSelected() ? null : Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void underlineMenuSelection() {
/* 167 */     UIManager.put("MenuItem.selectionType", this.underlineMenuSelectionMenuItem.isSelected() ? "underline" : null);
/*     */   }
/*     */   
/*     */   private void alwaysShowMnemonics() {
/* 171 */     UIManager.put("Component.hideMnemonics", Boolean.valueOf(!this.alwaysShowMnemonicsMenuItem.isSelected()));
/* 172 */     repaint();
/*     */   }
/*     */   
/*     */   private void animatedLafChangeChanged() {
/* 176 */     System.setProperty("flatlaf.animatedLafChange", String.valueOf(this.animatedLafChangeMenuItem.isSelected()));
/*     */   }
/*     */   
/*     */   private void showHintsChanged() {
/* 180 */     clearHints();
/* 181 */     showHints();
/*     */   }
/*     */   
/*     */   private void fontFamilyChanged(ActionEvent e) {
/* 185 */     String fontFamily = e.getActionCommand();
/*     */     
/* 187 */     FlatAnimatedLafChange.showSnapshot();
/*     */     
/* 189 */     Font font = UIManager.getFont("defaultFont");
/* 190 */     Font newFont = StyleContext.getDefaultStyleContext().getFont(fontFamily, font.getStyle(), font.getSize());
/* 191 */     UIManager.put("defaultFont", newFont);
/*     */     
/* 193 */     FlatLaf.updateUI();
/* 194 */     FlatAnimatedLafChange.hideSnapshotWithAnimation();
/*     */   }
/*     */   
/*     */   private void fontSizeChanged(ActionEvent e) {
/* 198 */     String fontSizeStr = e.getActionCommand();
/*     */     
/* 200 */     Font font = UIManager.getFont("defaultFont");
/* 201 */     Font newFont = font.deriveFont(Integer.parseInt(fontSizeStr));
/* 202 */     UIManager.put("defaultFont", newFont);
/*     */     
/* 204 */     FlatLaf.updateUI();
/*     */   }
/*     */   
/*     */   private void restoreFont() {
/* 208 */     UIManager.put("defaultFont", null);
/* 209 */     updateFontMenuItems();
/* 210 */     FlatLaf.updateUI();
/*     */   }
/*     */   
/*     */   private void incrFont() {
/* 214 */     Font font = UIManager.getFont("defaultFont");
/* 215 */     Font newFont = font.deriveFont((font.getSize() + 1));
/* 216 */     UIManager.put("defaultFont", newFont);
/*     */     
/* 218 */     updateFontMenuItems();
/* 219 */     FlatLaf.updateUI();
/*     */   }
/*     */   
/*     */   private void decrFont() {
/* 223 */     Font font = UIManager.getFont("defaultFont");
/* 224 */     Font newFont = font.deriveFont(Math.max(font.getSize() - 1, 10));
/* 225 */     UIManager.put("defaultFont", newFont);
/*     */     
/* 227 */     updateFontMenuItems();
/* 228 */     FlatLaf.updateUI();
/*     */   }
/*     */   
/*     */   void updateFontMenuItems() {
/* 232 */     if (this.initialFontMenuItemCount < 0) {
/* 233 */       this.initialFontMenuItemCount = this.fontMenu.getItemCount();
/*     */     } else {
/*     */       
/* 236 */       for (int i = this.fontMenu.getItemCount() - 1; i >= this.initialFontMenuItemCount; i--) {
/* 237 */         this.fontMenu.remove(i);
/*     */       }
/*     */     } 
/*     */     
/* 241 */     Font currentFont = UIManager.getFont("Label.font");
/* 242 */     String currentFamily = currentFont.getFamily();
/* 243 */     String currentSize = Integer.toString(currentFont.getSize());
/*     */ 
/*     */     
/* 246 */     this.fontMenu.addSeparator();
/* 247 */     ArrayList<String> families = new ArrayList<>(Arrays.asList(new String[] { "Arial", "Cantarell", "Comic Sans MS", "Courier New", "DejaVu Sans", "Dialog", "Liberation Sans", "Monospaced", "Noto Sans", "Roboto", "SansSerif", "Segoe UI", "Serif", "Tahoma", "Ubuntu", "Verdana" }));
/*     */ 
/*     */ 
/*     */     
/* 251 */     if (!families.contains(currentFamily))
/* 252 */       families.add(currentFamily); 
/* 253 */     families.sort(String.CASE_INSENSITIVE_ORDER);
/*     */     
/* 255 */     ButtonGroup familiesGroup = new ButtonGroup();
/* 256 */     for (String family : families) {
/* 257 */       if (Arrays.binarySearch((Object[])this.availableFontFamilyNames, family) < 0) {
/*     */         continue;
/*     */       }
/* 260 */       JCheckBoxMenuItem item = new JCheckBoxMenuItem(family);
/* 261 */       item.setSelected(family.equals(currentFamily));
/* 262 */       item.addActionListener(this::fontFamilyChanged);
/* 263 */       this.fontMenu.add(item);
/*     */       
/* 265 */       familiesGroup.add(item);
/*     */     } 
/*     */ 
/*     */     
/* 269 */     this.fontMenu.addSeparator();
/* 270 */     ArrayList<String> sizes = new ArrayList<>(Arrays.asList(new String[] { "10", "12", "14", "16", "18", "20", "24", "28" }));
/*     */     
/* 272 */     if (!sizes.contains(currentSize))
/* 273 */       sizes.add(currentSize); 
/* 274 */     sizes.sort(String.CASE_INSENSITIVE_ORDER);
/*     */     
/* 276 */     ButtonGroup sizesGroup = new ButtonGroup();
/* 277 */     for (String size : sizes) {
/* 278 */       JCheckBoxMenuItem item = new JCheckBoxMenuItem(size);
/* 279 */       item.setSelected(size.equals(currentSize));
/* 280 */       item.addActionListener(this::fontSizeChanged);
/* 281 */       this.fontMenu.add(item);
/*     */       
/* 283 */       sizesGroup.add(item);
/*     */     } 
/*     */ 
/*     */     
/* 287 */     boolean enabled = UIManager.getLookAndFeel() instanceof FlatLaf;
/* 288 */     for (Component item : this.fontMenu.getMenuComponents()) {
/* 289 */       item.setEnabled(enabled);
/*     */     }
/*     */   }
/*     */   
/*     */   private void initComponents() {
/* 294 */     JMenuBar menuBar1 = new JMenuBar();
/* 295 */     JMenu fileMenu = new JMenu();
/* 296 */     JMenuItem newMenuItem = new JMenuItem();
/* 297 */     JMenuItem openMenuItem = new JMenuItem();
/* 298 */     JMenuItem saveAsMenuItem = new JMenuItem();
/* 299 */     JMenuItem closeMenuItem = new JMenuItem();
/* 300 */     JMenuItem exitMenuItem = new JMenuItem();
/* 301 */     JMenu editMenu = new JMenu();
/* 302 */     JMenuItem undoMenuItem = new JMenuItem();
/* 303 */     JMenuItem redoMenuItem = new JMenuItem();
/* 304 */     JMenuItem cutMenuItem = new JMenuItem();
/* 305 */     JMenuItem copyMenuItem = new JMenuItem();
/* 306 */     JMenuItem pasteMenuItem = new JMenuItem();
/* 307 */     JMenuItem deleteMenuItem = new JMenuItem();
/* 308 */     JMenu viewMenu = new JMenu();
/* 309 */     JCheckBoxMenuItem checkBoxMenuItem1 = new JCheckBoxMenuItem();
/* 310 */     JMenu menu1 = new JMenu();
/* 311 */     JMenu subViewsMenu = new JMenu();
/* 312 */     JMenu subSubViewsMenu = new JMenu();
/* 313 */     JMenuItem errorLogViewMenuItem = new JMenuItem();
/* 314 */     JMenuItem searchViewMenuItem = new JMenuItem();
/* 315 */     JMenuItem projectViewMenuItem = new JMenuItem();
/* 316 */     JMenuItem structureViewMenuItem = new JMenuItem();
/* 317 */     JMenuItem propertiesViewMenuItem = new JMenuItem();
/* 318 */     JMenuItem menuItem2 = new JMenuItem();
/* 319 */     JMenuItem menuItem1 = new JMenuItem();
/* 320 */     JRadioButtonMenuItem radioButtonMenuItem1 = new JRadioButtonMenuItem();
/* 321 */     JRadioButtonMenuItem radioButtonMenuItem2 = new JRadioButtonMenuItem();
/* 322 */     JRadioButtonMenuItem radioButtonMenuItem3 = new JRadioButtonMenuItem();
/* 323 */     this.fontMenu = new JMenu();
/* 324 */     JMenuItem restoreFontMenuItem = new JMenuItem();
/* 325 */     JMenuItem incrFontMenuItem = new JMenuItem();
/* 326 */     JMenuItem decrFontMenuItem = new JMenuItem();
/* 327 */     this.optionsMenu = new JMenu();
/* 328 */     this.windowDecorationsCheckBoxMenuItem = new JCheckBoxMenuItem();
/* 329 */     this.menuBarEmbeddedCheckBoxMenuItem = new JCheckBoxMenuItem();
/* 330 */     this.underlineMenuSelectionMenuItem = new JCheckBoxMenuItem();
/* 331 */     this.alwaysShowMnemonicsMenuItem = new JCheckBoxMenuItem();
/* 332 */     this.animatedLafChangeMenuItem = new JCheckBoxMenuItem();
/* 333 */     JMenuItem showHintsMenuItem = new JMenuItem();
/* 334 */     JMenuItem showUIDefaultsInspectorMenuItem = new JMenuItem();
/* 335 */     JMenu helpMenu = new JMenu();
/* 336 */     JMenuItem aboutMenuItem = new JMenuItem();
/* 337 */     JToolBar toolBar1 = new JToolBar();
/* 338 */     JButton backButton = new JButton();
/* 339 */     JButton forwardButton = new JButton();
/* 340 */     JButton cutButton = new JButton();
/* 341 */     JButton copyButton = new JButton();
/* 342 */     JButton pasteButton = new JButton();
/* 343 */     JButton refreshButton = new JButton();
/* 344 */     JToggleButton showToggleButton = new JToggleButton();
/* 345 */     JPanel contentPanel = new JPanel();
/* 346 */     this.tabbedPane = new JTabbedPane();
/* 347 */     BasicComponentsPanel basicComponentsPanel = new BasicComponentsPanel();
/* 348 */     MoreComponentsPanel moreComponentsPanel = new MoreComponentsPanel();
/* 349 */     DataComponentsPanel dataComponentsPanel = new DataComponentsPanel();
/* 350 */     TabsPanel tabsPanel = new TabsPanel();
/* 351 */     OptionPanePanel optionPanePanel = new OptionPanePanel();
/* 352 */     ExtrasPanel extrasPanel1 = new ExtrasPanel();
/* 353 */     this.controlBar = new ControlBar();
/* 354 */     this.themesPanel = new IJThemesPanel();
/*     */ 
/*     */     
/* 357 */     setTitle("FlatLaf Demo");
/* 358 */     setDefaultCloseOperation(2);
/* 359 */     Container contentPane = getContentPane();
/* 360 */     contentPane.setLayout(new BorderLayout());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 367 */     fileMenu.setText("File");
/* 368 */     fileMenu.setMnemonic('F');
/*     */ 
/*     */     
/* 371 */     newMenuItem.setText("New");
/* 372 */     newMenuItem.setAccelerator(KeyStroke.getKeyStroke(78, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
/* 373 */     newMenuItem.setMnemonic('N');
/* 374 */     newMenuItem.addActionListener(e -> newActionPerformed());
/* 375 */     fileMenu.add(newMenuItem);
/*     */ 
/*     */     
/* 378 */     openMenuItem.setText("Open...");
/* 379 */     openMenuItem.setAccelerator(KeyStroke.getKeyStroke(79, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
/* 380 */     openMenuItem.setMnemonic('O');
/* 381 */     openMenuItem.addActionListener(e -> openActionPerformed());
/* 382 */     fileMenu.add(openMenuItem);
/*     */ 
/*     */     
/* 385 */     saveAsMenuItem.setText("Save As...");
/* 386 */     saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(83, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
/* 387 */     saveAsMenuItem.setMnemonic('S');
/* 388 */     saveAsMenuItem.addActionListener(e -> saveAsActionPerformed());
/* 389 */     fileMenu.add(saveAsMenuItem);
/* 390 */     fileMenu.addSeparator();
/*     */ 
/*     */     
/* 393 */     closeMenuItem.setText("Close");
/* 394 */     closeMenuItem.setAccelerator(KeyStroke.getKeyStroke(87, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
/* 395 */     closeMenuItem.setMnemonic('C');
/* 396 */     closeMenuItem.addActionListener(e -> menuItemActionPerformed(e));
/* 397 */     fileMenu.add(closeMenuItem);
/* 398 */     fileMenu.addSeparator();
/*     */ 
/*     */     
/* 401 */     exitMenuItem.setText("Exit");
/* 402 */     exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(81, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
/* 403 */     exitMenuItem.setMnemonic('X');
/* 404 */     exitMenuItem.addActionListener(e -> exitActionPerformed());
/* 405 */     fileMenu.add(exitMenuItem);
/*     */     
/* 407 */     menuBar1.add(fileMenu);
/*     */ 
/*     */ 
/*     */     
/* 411 */     editMenu.setText("Edit");
/* 412 */     editMenu.setMnemonic('E');
/*     */ 
/*     */     
/* 415 */     undoMenuItem.setText("Undo");
/* 416 */     undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(90, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
/* 417 */     undoMenuItem.setMnemonic('U');
/* 418 */     undoMenuItem.addActionListener(e -> menuItemActionPerformed(e));
/* 419 */     editMenu.add(undoMenuItem);
/*     */ 
/*     */     
/* 422 */     redoMenuItem.setText("Redo");
/* 423 */     redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(89, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
/* 424 */     redoMenuItem.setMnemonic('R');
/* 425 */     redoMenuItem.addActionListener(e -> menuItemActionPerformed(e));
/* 426 */     editMenu.add(redoMenuItem);
/* 427 */     editMenu.addSeparator();
/*     */ 
/*     */     
/* 430 */     cutMenuItem.setText("Cut");
/* 431 */     cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(88, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
/* 432 */     cutMenuItem.setMnemonic('C');
/* 433 */     editMenu.add(cutMenuItem);
/*     */ 
/*     */     
/* 436 */     copyMenuItem.setText("Copy");
/* 437 */     copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(67, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
/* 438 */     copyMenuItem.setMnemonic('O');
/* 439 */     editMenu.add(copyMenuItem);
/*     */ 
/*     */     
/* 442 */     pasteMenuItem.setText("Paste");
/* 443 */     pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(86, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
/* 444 */     pasteMenuItem.setMnemonic('P');
/* 445 */     editMenu.add(pasteMenuItem);
/* 446 */     editMenu.addSeparator();
/*     */ 
/*     */     
/* 449 */     deleteMenuItem.setText("Delete");
/* 450 */     deleteMenuItem.setAccelerator(KeyStroke.getKeyStroke(127, 0));
/* 451 */     deleteMenuItem.setMnemonic('D');
/* 452 */     deleteMenuItem.addActionListener(e -> menuItemActionPerformed(e));
/* 453 */     editMenu.add(deleteMenuItem);
/*     */     
/* 455 */     menuBar1.add(editMenu);
/*     */ 
/*     */ 
/*     */     
/* 459 */     viewMenu.setText("View");
/* 460 */     viewMenu.setMnemonic('V');
/*     */ 
/*     */     
/* 463 */     checkBoxMenuItem1.setText("Show Toolbar");
/* 464 */     checkBoxMenuItem1.setSelected(true);
/* 465 */     checkBoxMenuItem1.setMnemonic('T');
/* 466 */     checkBoxMenuItem1.addActionListener(e -> menuItemActionPerformed(e));
/* 467 */     viewMenu.add(checkBoxMenuItem1);
/*     */ 
/*     */ 
/*     */     
/* 471 */     menu1.setText("Show View");
/* 472 */     menu1.setMnemonic('V');
/*     */ 
/*     */ 
/*     */     
/* 476 */     subViewsMenu.setText("Sub Views");
/* 477 */     subViewsMenu.setMnemonic('S');
/*     */ 
/*     */ 
/*     */     
/* 481 */     subSubViewsMenu.setText("Sub sub Views");
/* 482 */     subSubViewsMenu.setMnemonic('U');
/*     */ 
/*     */     
/* 485 */     errorLogViewMenuItem.setText("Error Log");
/* 486 */     errorLogViewMenuItem.setMnemonic('E');
/* 487 */     errorLogViewMenuItem.addActionListener(e -> menuItemActionPerformed(e));
/* 488 */     subSubViewsMenu.add(errorLogViewMenuItem);
/*     */     
/* 490 */     subViewsMenu.add(subSubViewsMenu);
/*     */ 
/*     */     
/* 493 */     searchViewMenuItem.setText("Search");
/* 494 */     searchViewMenuItem.setMnemonic('S');
/* 495 */     searchViewMenuItem.addActionListener(e -> menuItemActionPerformed(e));
/* 496 */     subViewsMenu.add(searchViewMenuItem);
/*     */     
/* 498 */     menu1.add(subViewsMenu);
/*     */ 
/*     */     
/* 501 */     projectViewMenuItem.setText("Project");
/* 502 */     projectViewMenuItem.setMnemonic('P');
/* 503 */     projectViewMenuItem.addActionListener(e -> menuItemActionPerformed(e));
/* 504 */     menu1.add(projectViewMenuItem);
/*     */ 
/*     */     
/* 507 */     structureViewMenuItem.setText("Structure");
/* 508 */     structureViewMenuItem.setMnemonic('T');
/* 509 */     structureViewMenuItem.addActionListener(e -> menuItemActionPerformed(e));
/* 510 */     menu1.add(structureViewMenuItem);
/*     */ 
/*     */     
/* 513 */     propertiesViewMenuItem.setText("Properties");
/* 514 */     propertiesViewMenuItem.setMnemonic('O');
/* 515 */     propertiesViewMenuItem.addActionListener(e -> menuItemActionPerformed(e));
/* 516 */     menu1.add(propertiesViewMenuItem);
/*     */     
/* 518 */     viewMenu.add(menu1);
/*     */ 
/*     */     
/* 521 */     menuItem2.setText("Disabled Item");
/* 522 */     menuItem2.setEnabled(false);
/* 523 */     viewMenu.add(menuItem2);
/*     */ 
/*     */     
/* 526 */     menuItem1.setText("<html>some <b color=\"red\">HTML</b> <i color=\"blue\">text</i></html>");
/* 527 */     viewMenu.add(menuItem1);
/* 528 */     viewMenu.addSeparator();
/*     */ 
/*     */     
/* 531 */     radioButtonMenuItem1.setText("Details");
/* 532 */     radioButtonMenuItem1.setSelected(true);
/* 533 */     radioButtonMenuItem1.setMnemonic('D');
/* 534 */     radioButtonMenuItem1.addActionListener(e -> menuItemActionPerformed(e));
/* 535 */     viewMenu.add(radioButtonMenuItem1);
/*     */ 
/*     */     
/* 538 */     radioButtonMenuItem2.setText("Small Icons");
/* 539 */     radioButtonMenuItem2.setMnemonic('S');
/* 540 */     radioButtonMenuItem2.addActionListener(e -> menuItemActionPerformed(e));
/* 541 */     viewMenu.add(radioButtonMenuItem2);
/*     */ 
/*     */     
/* 544 */     radioButtonMenuItem3.setText("Large Icons");
/* 545 */     radioButtonMenuItem3.setMnemonic('L');
/* 546 */     radioButtonMenuItem3.addActionListener(e -> menuItemActionPerformed(e));
/* 547 */     viewMenu.add(radioButtonMenuItem3);
/*     */     
/* 549 */     menuBar1.add(viewMenu);
/*     */ 
/*     */ 
/*     */     
/* 553 */     this.fontMenu.setText("Font");
/*     */ 
/*     */     
/* 556 */     restoreFontMenuItem.setText("Restore Font");
/* 557 */     restoreFontMenuItem.setAccelerator(KeyStroke.getKeyStroke(48, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
/* 558 */     restoreFontMenuItem.addActionListener(e -> restoreFont());
/* 559 */     this.fontMenu.add(restoreFontMenuItem);
/*     */ 
/*     */     
/* 562 */     incrFontMenuItem.setText("Increase Font Size");
/* 563 */     incrFontMenuItem.setAccelerator(KeyStroke.getKeyStroke(521, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
/* 564 */     incrFontMenuItem.addActionListener(e -> incrFont());
/* 565 */     this.fontMenu.add(incrFontMenuItem);
/*     */ 
/*     */     
/* 568 */     decrFontMenuItem.setText("Decrease Font Size");
/* 569 */     decrFontMenuItem.setAccelerator(KeyStroke.getKeyStroke(45, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
/* 570 */     decrFontMenuItem.addActionListener(e -> decrFont());
/* 571 */     this.fontMenu.add(decrFontMenuItem);
/*     */     
/* 573 */     menuBar1.add(this.fontMenu);
/*     */ 
/*     */ 
/*     */     
/* 577 */     this.optionsMenu.setText("Options");
/*     */ 
/*     */     
/* 580 */     this.windowDecorationsCheckBoxMenuItem.setText("Window decorations");
/* 581 */     this.windowDecorationsCheckBoxMenuItem.setSelected(true);
/* 582 */     this.windowDecorationsCheckBoxMenuItem.addActionListener(e -> windowDecorationsChanged());
/* 583 */     this.optionsMenu.add(this.windowDecorationsCheckBoxMenuItem);
/*     */ 
/*     */     
/* 586 */     this.menuBarEmbeddedCheckBoxMenuItem.setText("Embedded menu bar");
/* 587 */     this.menuBarEmbeddedCheckBoxMenuItem.setSelected(true);
/* 588 */     this.menuBarEmbeddedCheckBoxMenuItem.addActionListener(e -> menuBarEmbeddedChanged());
/* 589 */     this.optionsMenu.add(this.menuBarEmbeddedCheckBoxMenuItem);
/*     */ 
/*     */     
/* 592 */     this.underlineMenuSelectionMenuItem.setText("Use underline menu selection");
/* 593 */     this.underlineMenuSelectionMenuItem.addActionListener(e -> underlineMenuSelection());
/* 594 */     this.optionsMenu.add(this.underlineMenuSelectionMenuItem);
/*     */ 
/*     */     
/* 597 */     this.alwaysShowMnemonicsMenuItem.setText("Always show mnemonics");
/* 598 */     this.alwaysShowMnemonicsMenuItem.addActionListener(e -> alwaysShowMnemonics());
/* 599 */     this.optionsMenu.add(this.alwaysShowMnemonicsMenuItem);
/*     */ 
/*     */     
/* 602 */     this.animatedLafChangeMenuItem.setText("Animated Laf Change");
/* 603 */     this.animatedLafChangeMenuItem.setSelected(true);
/* 604 */     this.animatedLafChangeMenuItem.addActionListener(e -> animatedLafChangeChanged());
/* 605 */     this.optionsMenu.add(this.animatedLafChangeMenuItem);
/*     */ 
/*     */     
/* 608 */     showHintsMenuItem.setText("Show hints");
/* 609 */     showHintsMenuItem.addActionListener(e -> showHintsChanged());
/* 610 */     this.optionsMenu.add(showHintsMenuItem);
/*     */ 
/*     */     
/* 613 */     showUIDefaultsInspectorMenuItem.setText("Show UI Defaults Inspector");
/* 614 */     showUIDefaultsInspectorMenuItem.addActionListener(e -> showUIDefaultsInspector());
/* 615 */     this.optionsMenu.add(showUIDefaultsInspectorMenuItem);
/*     */     
/* 617 */     menuBar1.add(this.optionsMenu);
/*     */ 
/*     */ 
/*     */     
/* 621 */     helpMenu.setText("Help");
/* 622 */     helpMenu.setMnemonic('H');
/*     */ 
/*     */     
/* 625 */     aboutMenuItem.setText("About");
/* 626 */     aboutMenuItem.setMnemonic('A');
/* 627 */     aboutMenuItem.addActionListener(e -> aboutActionPerformed());
/* 628 */     helpMenu.add(aboutMenuItem);
/*     */     
/* 630 */     menuBar1.add(helpMenu);
/*     */     
/* 632 */     setJMenuBar(menuBar1);
/*     */ 
/*     */ 
/*     */     
/* 636 */     toolBar1.setMargin(new Insets(3, 3, 3, 3));
/*     */ 
/*     */     
/* 639 */     backButton.setToolTipText("Back");
/* 640 */     toolBar1.add(backButton);
/*     */ 
/*     */     
/* 643 */     forwardButton.setToolTipText("Forward");
/* 644 */     toolBar1.add(forwardButton);
/* 645 */     toolBar1.addSeparator();
/*     */ 
/*     */     
/* 648 */     cutButton.setToolTipText("Cut");
/* 649 */     toolBar1.add(cutButton);
/*     */ 
/*     */     
/* 652 */     copyButton.setToolTipText("Copy");
/* 653 */     toolBar1.add(copyButton);
/*     */ 
/*     */     
/* 656 */     pasteButton.setToolTipText("Paste");
/* 657 */     toolBar1.add(pasteButton);
/* 658 */     toolBar1.addSeparator();
/*     */ 
/*     */     
/* 661 */     refreshButton.setToolTipText("Refresh");
/* 662 */     toolBar1.add(refreshButton);
/* 663 */     toolBar1.addSeparator();
/*     */ 
/*     */     
/* 666 */     showToggleButton.setSelected(true);
/* 667 */     showToggleButton.setToolTipText("Show Details");
/* 668 */     toolBar1.add(showToggleButton);
/*     */     
/* 670 */     contentPane.add(toolBar1, "North");
/*     */ 
/*     */ 
/*     */     
/* 674 */     contentPanel.setLayout((LayoutManager)new MigLayout("insets dialog,hidemode 3", "[grow,fill]", "[grow,fill]"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 683 */     this.tabbedPane.setTabLayoutPolicy(1);
/* 684 */     this.tabbedPane.addChangeListener(e -> selectedTabChanged());
/* 685 */     this.tabbedPane.addTab("Basic Components", basicComponentsPanel);
/* 686 */     this.tabbedPane.addTab("More Components", moreComponentsPanel);
/* 687 */     this.tabbedPane.addTab("Data Components", dataComponentsPanel);
/* 688 */     this.tabbedPane.addTab("Tabs", tabsPanel);
/* 689 */     this.tabbedPane.addTab("Option Pane", optionPanePanel);
/* 690 */     this.tabbedPane.addTab("Extras", (Component)extrasPanel1);
/*     */     
/* 692 */     contentPanel.add(this.tabbedPane, "cell 0 0");
/*     */     
/* 694 */     contentPane.add(contentPanel, "Center");
/* 695 */     contentPane.add(this.controlBar, "South");
/* 696 */     contentPane.add((Component)this.themesPanel, "East");
/*     */ 
/*     */     
/* 699 */     ButtonGroup buttonGroup1 = new ButtonGroup();
/* 700 */     buttonGroup1.add(radioButtonMenuItem1);
/* 701 */     buttonGroup1.add(radioButtonMenuItem2);
/* 702 */     buttonGroup1.add(radioButtonMenuItem3);
/*     */ 
/*     */     
/* 705 */     undoMenuItem.setIcon((Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/undo.svg"));
/* 706 */     redoMenuItem.setIcon((Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/redo.svg"));
/*     */     
/* 708 */     cutMenuItem.setIcon((Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/menu-cut.svg"));
/* 709 */     copyMenuItem.setIcon((Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/copy.svg"));
/* 710 */     pasteMenuItem.setIcon((Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/menu-paste.svg"));
/*     */     
/* 712 */     backButton.setIcon((Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/back.svg"));
/* 713 */     forwardButton.setIcon((Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/forward.svg"));
/* 714 */     cutButton.setIcon((Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/menu-cut.svg"));
/* 715 */     copyButton.setIcon((Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/copy.svg"));
/* 716 */     pasteButton.setIcon((Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/menu-paste.svg"));
/* 717 */     refreshButton.setIcon((Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/refresh.svg"));
/* 718 */     showToggleButton.setIcon((Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/show.svg"));
/*     */     
/* 720 */     cutMenuItem.addActionListener(new DefaultEditorKit.CutAction());
/* 721 */     copyMenuItem.addActionListener(new DefaultEditorKit.CopyAction());
/* 722 */     pasteMenuItem.addActionListener(new DefaultEditorKit.PasteAction());
/*     */ 
/*     */     
/* 725 */     boolean supportsWindowDecorations = (UIManager.getLookAndFeel().getSupportsWindowDecorations() || JBRCustomDecorations.isSupported());
/* 726 */     this.windowDecorationsCheckBoxMenuItem.setEnabled((supportsWindowDecorations && !JBRCustomDecorations.isSupported()));
/* 727 */     this.menuBarEmbeddedCheckBoxMenuItem.setEnabled(supportsWindowDecorations);
/*     */ 
/*     */     
/* 730 */     MigLayout layout = (MigLayout)contentPanel.getLayout();
/* 731 */     LC lc = ConstraintParser.parseLayoutConstraint((String)layout.getLayoutConstraints());
/* 732 */     UnitValue[] insets = lc.getInsets();
/* 733 */     lc.setInsets(new UnitValue[] { insets[0], insets[1], new UnitValue(0.0F, 0, null), insets[3] });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 739 */     layout.setLayoutConstraints(lc);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\demo\DemoFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */