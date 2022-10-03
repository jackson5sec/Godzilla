/*     */ package com.formdev.flatlaf.demo.intellijthemes;
/*     */ 
/*     */ import com.formdev.flatlaf.FlatDarculaLaf;
/*     */ import com.formdev.flatlaf.FlatDarkLaf;
/*     */ import com.formdev.flatlaf.FlatIntelliJLaf;
/*     */ import com.formdev.flatlaf.FlatLaf;
/*     */ import com.formdev.flatlaf.FlatLightLaf;
/*     */ import com.formdev.flatlaf.FlatPropertiesLaf;
/*     */ import com.formdev.flatlaf.IntelliJTheme;
/*     */ import com.formdev.flatlaf.demo.DemoPrefs;
/*     */ import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
/*     */ import com.formdev.flatlaf.extras.FlatSVGIcon;
/*     */ import java.awt.Component;
/*     */ import java.awt.Desktop;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.event.WindowListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Predicate;
/*     */ import javax.swing.AbstractListModel;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.DefaultListCellRenderer;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.CompoundBorder;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import net.miginfocom.swing.MigLayout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IJThemesPanel
/*     */   extends JPanel
/*     */ {
/*     */   public static final String THEMES_PACKAGE = "/com/formdev/flatlaf/intellijthemes/themes/";
/*  66 */   private final IJThemesManager themesManager = new IJThemesManager();
/*  67 */   private final List<IJThemeInfo> themes = new ArrayList<>();
/*  68 */   private final HashMap<Integer, String> categories = new HashMap<>();
/*  69 */   private final PropertyChangeListener lafListener = this::lafChanged;
/*  70 */   private final WindowListener windowListener = new WindowAdapter()
/*     */     {
/*     */       public void windowActivated(WindowEvent e) {
/*  73 */         IJThemesPanel.this.windowActivated();
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   private Window window;
/*     */   private File lastDirectory;
/*     */   private boolean isAdjustingThemesList;
/*     */   private JToolBar toolBar;
/*     */   
/*     */   public static void main(String[] args) {
/*  84 */     JFrame frame = new JFrame();
/*     */     
/*  86 */     IJThemesPanel ijThemesPanel = new IJThemesPanel();
/*     */     
/*  88 */     frame.setContentPane(ijThemesPanel);
/*     */     
/*  90 */     frame.setVisible(true);
/*  91 */     frame.setDefaultCloseOperation(2);
/*     */   }
/*     */   private JButton saveButton; private JButton sourceCodeButton; private JComboBox<String> filterComboBox; private JScrollPane themesScrollPane; private JList<IJThemeInfo> themesList;
/*     */   public IJThemesPanel() {
/*  95 */     initComponents();
/*     */     
/*  97 */     this.saveButton.setEnabled(false);
/*  98 */     this.sourceCodeButton.setEnabled(false);
/*  99 */     this.saveButton.setIcon((Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/download.svg"));
/* 100 */     this.sourceCodeButton.setIcon((Icon)new FlatSVGIcon("com/formdev/flatlaf/demo/icons/github.svg"));
/*     */ 
/*     */     
/* 103 */     this.themesList.setCellRenderer(new DefaultListCellRenderer()
/*     */         {
/*     */           
/*     */           public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
/*     */           {
/* 108 */             String title = (String)IJThemesPanel.this.categories.get(Integer.valueOf(index));
/* 109 */             String name = ((IJThemeInfo)value).name;
/* 110 */             int sep = name.indexOf('/');
/* 111 */             if (sep >= 0) {
/* 112 */               name = name.substring(sep + 1).trim();
/*     */             }
/* 114 */             JComponent c = (JComponent)super.getListCellRendererComponent(list, name, index, isSelected, cellHasFocus);
/* 115 */             c.setToolTipText(buildToolTip((IJThemeInfo)value));
/* 116 */             if (title != null)
/* 117 */               c.setBorder(new CompoundBorder(new ListCellTitledBorder(IJThemesPanel.this.themesList, title), c.getBorder())); 
/* 118 */             return c;
/*     */           }
/*     */           
/*     */           private String buildToolTip(IJThemeInfo ti) {
/* 122 */             if (ti.themeFile != null)
/* 123 */               return ti.themeFile.getPath(); 
/* 124 */             if (ti.resourceName == null) {
/* 125 */               return ti.name;
/*     */             }
/* 127 */             return "Name: " + ti.name + "\nLicense: " + ti.license + "\nSource Code: " + ti.sourceCodeUrl;
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 133 */     updateThemesList();
/*     */   }
/*     */   
/*     */   private void updateThemesList() {
/* 137 */     int filterLightDark = this.filterComboBox.getSelectedIndex();
/* 138 */     boolean showLight = (filterLightDark != 2);
/* 139 */     boolean showDark = (filterLightDark != 1);
/*     */ 
/*     */     
/* 142 */     this.themesManager.loadBundledThemes();
/* 143 */     this.themesManager.loadThemesFromDirectory();
/*     */ 
/*     */     
/* 146 */     Comparator<? super IJThemeInfo> comparator = (t1, t2) -> t1.name.compareToIgnoreCase(t2.name);
/* 147 */     this.themesManager.bundledThemes.sort(comparator);
/* 148 */     this.themesManager.moreThemes.sort(comparator);
/*     */ 
/*     */     
/* 151 */     IJThemeInfo oldSel = this.themesList.getSelectedValue();
/*     */     
/* 153 */     this.themes.clear();
/* 154 */     this.categories.clear();
/*     */ 
/*     */     
/* 157 */     this.categories.put(Integer.valueOf(this.themes.size()), "Core Themes");
/* 158 */     if (showLight)
/* 159 */       this.themes.add(new IJThemeInfo("Flat Light", null, false, null, null, null, null, null, FlatLightLaf.class.getName())); 
/* 160 */     if (showDark)
/* 161 */       this.themes.add(new IJThemeInfo("Flat Dark", null, true, null, null, null, null, null, FlatDarkLaf.class.getName())); 
/* 162 */     if (showLight)
/* 163 */       this.themes.add(new IJThemeInfo("Flat IntelliJ", null, false, null, null, null, null, null, FlatIntelliJLaf.class.getName())); 
/* 164 */     if (showDark) {
/* 165 */       this.themes.add(new IJThemeInfo("Flat Darcula", null, true, null, null, null, null, null, FlatDarculaLaf.class.getName()));
/*     */     }
/*     */     
/* 168 */     this.categories.put(Integer.valueOf(this.themes.size()), "Current Directory");
/* 169 */     this.themes.addAll(this.themesManager.moreThemes);
/*     */ 
/*     */     
/* 172 */     this.categories.put(Integer.valueOf(this.themes.size()), "IntelliJ Themes");
/* 173 */     for (IJThemeInfo ti : this.themesManager.bundledThemes) {
/* 174 */       boolean show = ((showLight && !ti.dark) || (showDark && ti.dark));
/* 175 */       if (show && !ti.name.contains("/")) {
/* 176 */         this.themes.add(ti);
/*     */       }
/*     */     } 
/*     */     
/* 180 */     String lastCategory = null;
/* 181 */     for (IJThemeInfo ti : this.themesManager.bundledThemes) {
/* 182 */       boolean show = ((showLight && !ti.dark) || (showDark && ti.dark));
/* 183 */       int sep = ti.name.indexOf('/');
/* 184 */       if (!show || sep < 0) {
/*     */         continue;
/*     */       }
/* 187 */       String category = ti.name.substring(0, sep).trim();
/* 188 */       if (!Objects.equals(lastCategory, category)) {
/* 189 */         lastCategory = category;
/* 190 */         this.categories.put(Integer.valueOf(this.themes.size()), category);
/*     */       } 
/*     */       
/* 193 */       this.themes.add(ti);
/*     */     } 
/*     */ 
/*     */     
/* 197 */     this.themesList.setModel(new AbstractListModel<IJThemeInfo>()
/*     */         {
/*     */           public int getSize() {
/* 200 */             return IJThemesPanel.this.themes.size();
/*     */           }
/*     */           
/*     */           public IJThemeInfo getElementAt(int index) {
/* 204 */             return IJThemesPanel.this.themes.get(index);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 209 */     if (oldSel != null) {
/* 210 */       for (int i = 0; i < this.themes.size(); i++) {
/* 211 */         IJThemeInfo theme = this.themes.get(i);
/* 212 */         if (oldSel.name.equals(theme.name) && 
/* 213 */           Objects.equals(oldSel.resourceName, theme.resourceName) && 
/* 214 */           Objects.equals(oldSel.themeFile, theme.themeFile) && 
/* 215 */           Objects.equals(oldSel.lafClassName, theme.lafClassName)) {
/*     */           
/* 217 */           this.themesList.setSelectedIndex(i);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */       
/* 223 */       if (this.themesList.getSelectedIndex() < 0) {
/* 224 */         this.themesList.setSelectedIndex(0);
/*     */       }
/*     */     } 
/*     */     
/* 228 */     int sel = this.themesList.getSelectedIndex();
/* 229 */     if (sel >= 0) {
/* 230 */       Rectangle bounds = this.themesList.getCellBounds(sel, sel);
/* 231 */       if (bounds != null)
/* 232 */         this.themesList.scrollRectToVisible(bounds); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void selectPreviousTheme() {
/* 237 */     int sel = this.themesList.getSelectedIndex();
/* 238 */     if (sel > 0)
/* 239 */       this.themesList.setSelectedIndex(sel - 1); 
/*     */   }
/*     */   
/*     */   public void selectNextTheme() {
/* 243 */     int sel = this.themesList.getSelectedIndex();
/* 244 */     this.themesList.setSelectedIndex(sel + 1);
/*     */   }
/*     */   
/*     */   private void themesListValueChanged(ListSelectionEvent e) {
/* 248 */     IJThemeInfo themeInfo = this.themesList.getSelectedValue();
/* 249 */     boolean bundledTheme = (themeInfo != null && themeInfo.resourceName != null);
/* 250 */     this.saveButton.setEnabled(bundledTheme);
/* 251 */     this.sourceCodeButton.setEnabled(bundledTheme);
/*     */     
/* 253 */     if (e.getValueIsAdjusting() || this.isAdjustingThemesList) {
/*     */       return;
/*     */     }
/* 256 */     EventQueue.invokeLater(() -> setTheme(themeInfo));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setTheme(IJThemeInfo themeInfo) {
/* 262 */     if (themeInfo == null) {
/*     */       return;
/*     */     }
/* 265 */     if (themeInfo.lafClassName != null) {
/* 266 */       if (themeInfo.lafClassName.equals(UIManager.getLookAndFeel().getClass().getName())) {
/*     */         return;
/*     */       }
/* 269 */       FlatAnimatedLafChange.showSnapshot();
/*     */       
/*     */       try {
/* 272 */         UIManager.setLookAndFeel(themeInfo.lafClassName);
/* 273 */       } catch (Exception ex) {
/* 274 */         ex.printStackTrace();
/*     */       } 
/* 276 */     } else if (themeInfo.themeFile != null) {
/* 277 */       FlatAnimatedLafChange.showSnapshot();
/*     */       
/*     */       try {
/* 280 */         if (themeInfo.themeFile.getName().endsWith(".properties")) {
/* 281 */           FlatLaf.install((LookAndFeel)new FlatPropertiesLaf(themeInfo.name, themeInfo.themeFile));
/*     */         } else {
/* 283 */           FlatLaf.install((LookAndFeel)IntelliJTheme.createLaf(new FileInputStream(themeInfo.themeFile)));
/*     */         } 
/* 285 */         DemoPrefs.getState().put("lafTheme", "file:" + themeInfo.themeFile);
/* 286 */       } catch (Exception ex) {
/* 287 */         ex.printStackTrace();
/*     */       } 
/*     */     } else {
/* 290 */       FlatAnimatedLafChange.showSnapshot();
/*     */       
/* 292 */       IntelliJTheme.install(IJThemesPanel.class.getResourceAsStream("/com/formdev/flatlaf/intellijthemes/themes/" + themeInfo.resourceName));
/*     */     } 
/*     */ 
/*     */     
/* 296 */     FlatLaf.updateUI();
/* 297 */     FlatAnimatedLafChange.hideSnapshotWithAnimation();
/*     */   }
/*     */   
/*     */   public IJThemeInfo getSelect() {
/* 301 */     return this.themesList.getSelectedValue();
/*     */   }
/*     */ 
/*     */   
/*     */   private void saveTheme() {}
/*     */ 
/*     */   
/*     */   private void browseSourceCode() {
/* 309 */     IJThemeInfo themeInfo = this.themesList.getSelectedValue();
/* 310 */     if (themeInfo == null || themeInfo.resourceName == null) {
/*     */       return;
/*     */     }
/* 313 */     String themeUrl = (themeInfo.sourceCodeUrl + '/' + themeInfo.sourceCodePath).replace(" ", "%20");
/*     */     try {
/* 315 */       Desktop.getDesktop().browse(new URI(themeUrl));
/* 316 */     } catch (IOException|java.net.URISyntaxException ex) {
/* 317 */       showInformationDialog("Failed to browse '" + themeUrl + "'.", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void showInformationDialog(String message, Exception ex) {
/* 322 */     JOptionPane.showMessageDialog(SwingUtilities.windowForComponent(this), message + "\n\n" + ex
/* 323 */         .getMessage(), "FlatLaf", 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNotify() {
/* 329 */     super.addNotify();
/*     */     
/* 331 */     selectedCurrentLookAndFeel();
/* 332 */     UIManager.addPropertyChangeListener(this.lafListener);
/*     */     
/* 334 */     this.window = SwingUtilities.windowForComponent(this);
/* 335 */     if (this.window != null) {
/* 336 */       this.window.addWindowListener(this.windowListener);
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeNotify() {
/* 341 */     super.removeNotify();
/*     */     
/* 343 */     UIManager.removePropertyChangeListener(this.lafListener);
/*     */     
/* 345 */     if (this.window != null) {
/* 346 */       this.window.removeWindowListener(this.windowListener);
/* 347 */       this.window = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void lafChanged(PropertyChangeEvent e) {
/* 352 */     if ("lookAndFeel".equals(e.getPropertyName())) {
/* 353 */       selectedCurrentLookAndFeel();
/*     */     }
/*     */   }
/*     */   
/*     */   private void windowActivated() {
/* 358 */     if (this.themesManager.hasThemesFromDirectoryChanged())
/* 359 */       updateThemesList(); 
/*     */   }
/*     */   private void selectedCurrentLookAndFeel() {
/*     */     Predicate<IJThemeInfo> test;
/* 363 */     LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
/* 364 */     String theme = UIManager.getLookAndFeelDefaults().getString("__FlatLaf.demo.theme");
/*     */     
/* 366 */     if (theme == null && (lookAndFeel instanceof IntelliJTheme.ThemeLaf || lookAndFeel instanceof FlatPropertiesLaf)) {
/*     */       return;
/*     */     }
/*     */     
/* 370 */     if (theme != null && theme.startsWith("res:")) {
/* 371 */       String resourceName = theme.substring("res:".length());
/* 372 */       test = (ti -> Objects.equals(ti.resourceName, resourceName));
/* 373 */     } else if (theme != null && theme.startsWith("file:")) {
/* 374 */       File themeFile = new File(theme.substring("file:".length()));
/* 375 */       test = (ti -> Objects.equals(ti.themeFile, themeFile));
/*     */     } else {
/* 377 */       String lafClassName = lookAndFeel.getClass().getName();
/* 378 */       test = (ti -> Objects.equals(ti.lafClassName, lafClassName));
/*     */     } 
/*     */     
/* 381 */     int newSel = -1;
/* 382 */     for (int i = 0; i < this.themes.size(); i++) {
/* 383 */       if (test.test(this.themes.get(i))) {
/* 384 */         newSel = i;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 389 */     this.isAdjustingThemesList = true;
/* 390 */     if (newSel >= 0) {
/* 391 */       if (newSel != this.themesList.getSelectedIndex())
/* 392 */         this.themesList.setSelectedIndex(newSel); 
/*     */     } else {
/* 394 */       this.themesList.clearSelection();
/* 395 */     }  this.isAdjustingThemesList = false;
/*     */   }
/*     */   
/*     */   private void filterChanged() {
/* 399 */     updateThemesList();
/*     */   }
/*     */ 
/*     */   
/*     */   private void initComponents() {
/* 404 */     JLabel themesLabel = new JLabel();
/* 405 */     this.toolBar = new JToolBar();
/* 406 */     this.saveButton = new JButton();
/* 407 */     this.sourceCodeButton = new JButton();
/* 408 */     this.filterComboBox = new JComboBox<>();
/* 409 */     this.themesScrollPane = new JScrollPane();
/* 410 */     this.themesList = new JList<>();
/*     */ 
/*     */     
/* 413 */     setLayout((LayoutManager)new MigLayout("insets dialog,hidemode 3", "[grow,fill]", "[]3[grow,fill]"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 422 */     themesLabel.setText("Themes:");
/* 423 */     add(themesLabel, "cell 0 0");
/*     */ 
/*     */ 
/*     */     
/* 427 */     this.toolBar.setFloatable(false);
/*     */ 
/*     */     
/* 430 */     this.saveButton.setToolTipText("Save .theme.json of selected IntelliJ theme to file.");
/* 431 */     this.saveButton.addActionListener(e -> saveTheme());
/* 432 */     this.toolBar.add(this.saveButton);
/*     */ 
/*     */     
/* 435 */     this.sourceCodeButton.setToolTipText("Opens the source code repository of selected IntelliJ theme in the browser.");
/* 436 */     this.sourceCodeButton.addActionListener(e -> browseSourceCode());
/* 437 */     this.toolBar.add(this.sourceCodeButton);
/*     */     
/* 439 */     add(this.toolBar, "cell 0 0,alignx right,growx 0");
/*     */ 
/*     */     
/* 442 */     this.filterComboBox.setModel(new DefaultComboBoxModel<>(new String[] { "all", "light", "dark" }));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 447 */     this.filterComboBox.putClientProperty("JComponent.minimumWidth", Integer.valueOf(0));
/* 448 */     this.filterComboBox.setFocusable(false);
/* 449 */     this.filterComboBox.addActionListener(e -> filterChanged());
/* 450 */     add(this.filterComboBox, "cell 0 0,alignx right,growx 0");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 456 */     this.themesList.setSelectionMode(0);
/* 457 */     this.themesList.addListSelectionListener(e -> themesListValueChanged(e));
/* 458 */     this.themesScrollPane.setViewportView(this.themesList);
/*     */     
/* 460 */     add(this.themesScrollPane, "cell 0 1");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\demo\intellijthemes\IJThemesPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */