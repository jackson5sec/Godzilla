/*     */ package core.ui.component.dialog;
/*     */ 
/*     */ import com.formdev.flatlaf.demo.intellijthemes.IJThemeInfo;
/*     */ import com.formdev.flatlaf.demo.intellijthemes.IJThemesPanel;
/*     */ import core.ApplicationContext;
/*     */ import core.Db;
/*     */ import core.EasyI18N;
/*     */ import core.ui.MainActivity;
/*     */ import core.ui.component.GBC;
/*     */ import core.ui.component.RTextArea;
/*     */ import core.ui.component.SimplePanel;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.JTextField;
/*     */ import util.Log;
/*     */ import util.OpenC;
/*     */ import util.UiFunction;
/*     */ import util.automaticBindClick;
/*     */ import util.functions;
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
/*     */ public class AppSeting
/*     */   extends JDialog
/*     */ {
/*     */   private final JTabbedPane tabbedPane;
/*     */   private JPanel globallHttpHeaderPanel;
/*     */   private JPanel setFontPanel;
/*     */   private JPanel coreConfigPanel;
/*     */   private SimplePanel globalProxyPanel;
/*     */   private SimplePanel httpsCertConfigPanel;
/*     */   private JComboBox<String> fontNameComboBox;
/*     */   private JComboBox<String> fontTypeComboBox;
/*     */   private JComboBox<String> fontSizeComboBox;
/*     */   private JLabel testFontLabel;
/*     */   private JLabel currentFontLabel;
/*     */   private JButton updateFontButton;
/*     */   private JButton resetFontButton;
/*     */   private JLabel fontNameLabel;
/*     */   private JLabel fontTypeLabel;
/*     */   private JLabel fontSizeLabel;
/*     */   private JLabel currentFontLLabel;
/*     */   private RTextArea headerTextArea;
/*     */   private JButton updateHeaderButton;
/*     */   private JLabel godModeLabel;
/*     */   private JCheckBox godModeCheckBox;
/*     */   private JLabel execCommandModeLabel;
/*     */   private JComboBox<String> execCommandModeComboBox;
/*     */   private JLabel languageLabel;
/*     */   private JComboBox<String> languageComboBox;
/*     */   private JCheckBox isOpenCacheCheckBox;
/*     */   private JLabel isSuperLogLabel;
/*     */   private JLabel isOpenCacheLabel;
/*     */   private JCheckBox isSuperLogCheckBox;
/*     */   private JLabel superRequestLabel;
/*     */   private JButton superRequestButton;
/*     */   private JLabel isAutoCloseShellLabel;
/*     */   private JCheckBox isAutoCloseShellCheckBox;
/*  87 */   private int currentCoreConfigPanelComponent = 0;
/*     */   
/*     */   private JLabel globalProxyTypeLabel;
/*     */   
/*     */   private JLabel globalProxyHostLabel;
/*     */   
/*     */   private JLabel globalProxyPortLabel;
/*     */   
/*     */   private JTextField globalProxyHostTextField;
/*     */   
/*     */   private JTextField globalProxyPortTextField;
/*     */   private JComboBox<String> globalProxyTypeComboBox;
/*     */   private JButton updateGlobalProxyButton;
/*     */   private JButton httpsCertConfigExportButton;
/*     */   private JButton httpsCertConfigResetButton;
/*     */   private SimplePanel bigFilePanel;
/*     */   private JLabel bigFileErrorRetryNumLabel;
/*     */   private JLabel oneceBigFileUploadByteNumLabel;
/*     */   private JLabel oneceBigFileDownloadByteNumLabel;
/*     */   private JLabel bigFileSendRequestSleepLabel;
/*     */   private JTextField oneceBigFileUploadByteNumTextField;
/*     */   private JTextField bigFileErrorRetryNumTextField;
/*     */   private JTextField bigFileSendRequestSleepTextField;
/*     */   private JTextField oneceBigFileDownloadByteNumTextField;
/*     */   private JButton bigFileConfigSaveButton;
/*     */   private JSplitPane themesSplitPane;
/*     */   private IJThemesPanel themesPanel;
/*     */   private JButton updateThemesButton;
/* 115 */   private static final HashMap<String, Class<?>> pluginSeting = new HashMap<>();
/*     */   
/*     */   public AppSeting() {
/* 118 */     super((Frame)MainActivity.getFrame(), "AppSeting", true);
/*     */     
/* 120 */     this.tabbedPane = new JTabbedPane();
/*     */     
/* 122 */     initSetFontPanel();
/* 123 */     initGloballHttpHeader();
/* 124 */     initCoreConfigPanel();
/* 125 */     initGlobalProxy();
/* 126 */     initHttpsCertConfig();
/* 127 */     initBigFilePanel();
/* 128 */     initThemesPanel();
/*     */     
/* 130 */     this.tabbedPane.addTab("???????????????", this.globallHttpHeaderPanel);
/* 131 */     this.tabbedPane.addTab("????????????", (Component)this.globalProxyPanel);
/* 132 */     this.tabbedPane.addTab("??????????????????", (Component)this.httpsCertConfigPanel);
/* 133 */     this.tabbedPane.addTab("????????????", this.setFontPanel);
/* 134 */     this.tabbedPane.addTab("????????????", this.coreConfigPanel);
/* 135 */     this.tabbedPane.addTab("???????????????", (Component)this.bigFilePanel);
/* 136 */     this.tabbedPane.addTab("UI??????", this.themesSplitPane);
/*     */ 
/*     */     
/* 139 */     pluginSeting.keySet().forEach(k -> {
/*     */           try {
/*     */             JPanel panel = ((Class<JPanel>)pluginSeting.get(k)).newInstance();
/*     */             EasyI18N.installObject(panel);
/*     */             this.tabbedPane.addTab(k, panel);
/* 144 */           } catch (InstantiationException e) {
/*     */             e.printStackTrace();
/* 146 */           } catch (IllegalAccessException e) {
/*     */             e.printStackTrace();
/*     */           } 
/*     */         });
/*     */ 
/*     */     
/* 152 */     add(this.tabbedPane);
/*     */     
/* 154 */     automaticBindClick.bindJButtonClick(this, this);
/* 155 */     functions.setWindowSize(this, 1200, 500);
/* 156 */     setLocationRelativeTo((Component)MainActivity.getFrame());
/* 157 */     EasyI18N.installObject(this);
/* 158 */     setVisible(true);
/*     */   }
/*     */   
/*     */   void initGlobalProxy() {
/* 162 */     this.globalProxyPanel = new SimplePanel();
/*     */     
/* 164 */     this.updateGlobalProxyButton = new JButton("????????????");
/*     */     
/* 166 */     this.globalProxyHostLabel = new JLabel("????????????");
/* 167 */     this.globalProxyPortLabel = new JLabel("????????????");
/* 168 */     this.globalProxyTypeLabel = new JLabel("????????????");
/*     */     
/* 170 */     this.globalProxyHostTextField = new JTextField(Db.tryGetSetingValue("globalProxyHost", "127.0.0.1"), 10);
/* 171 */     this.globalProxyPortTextField = new JTextField(Db.tryGetSetingValue("globalProxyPort", "8888"), 7);
/* 172 */     this.globalProxyTypeComboBox = new JComboBox<>(ApplicationContext.getAllProxy());
/* 173 */     this.globalProxyTypeComboBox.setSelectedItem(Db.tryGetSetingValue("globalProxyType", "NO_PROXY"));
/*     */ 
/*     */     
/* 176 */     this.globalProxyTypeComboBox.removeItem("GLOBAL_PROXY");
/*     */     
/* 178 */     this.globalProxyPanel.setSetup(-270);
/*     */     
/* 180 */     this.globalProxyPanel.addX(new Component[] { this.globalProxyTypeLabel, this.globalProxyTypeComboBox });
/* 181 */     this.globalProxyPanel.addX(new Component[] { this.globalProxyHostLabel, this.globalProxyHostTextField });
/* 182 */     this.globalProxyPanel.addX(new Component[] { this.globalProxyPortLabel, this.globalProxyPortTextField });
/*     */ 
/*     */     
/* 185 */     this.globalProxyPanel.addX(new Component[] { this.updateGlobalProxyButton });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void initSetFontPanel() {
/* 191 */     Font currentFont = ApplicationContext.getFont();
/*     */     
/* 193 */     this.setFontPanel = new JPanel(new GridBagLayout());
/* 194 */     this.fontNameComboBox = new JComboBox<>(UiFunction.getAllFontName());
/* 195 */     this.fontTypeComboBox = new JComboBox<>(UiFunction.getAllFontType());
/* 196 */     this.fontSizeComboBox = new JComboBox<>(UiFunction.getAllFontSize());
/* 197 */     this.testFontLabel = new JLabel("??????\tHello");
/* 198 */     this.currentFontLabel = new JLabel(functions.toString(currentFont));
/* 199 */     this.currentFontLLabel = new JLabel("???????????? : ");
/* 200 */     this.updateFontButton = new JButton("??????");
/* 201 */     this.resetFontButton = new JButton("??????");
/*     */     
/* 203 */     this.fontNameLabel = new JLabel("??????:    ");
/* 204 */     this.fontTypeLabel = new JLabel("???????????? : ");
/* 205 */     this.fontSizeLabel = new JLabel("???????????? : ");
/*     */     
/* 207 */     GBC gbcLFontName = (new GBC(0, 0)).setInsets(5, -40, 0, 0);
/* 208 */     GBC gbcFontName = (new GBC(1, 0, 3, 1)).setInsets(5, 20, 0, 0);
/* 209 */     GBC gbcLFontType = (new GBC(0, 1)).setInsets(5, -40, 0, 0);
/* 210 */     GBC gbcFontType = (new GBC(1, 1, 3, 1)).setInsets(5, 20, 0, 0);
/* 211 */     GBC gbcLFontSize = (new GBC(0, 2)).setInsets(5, -40, 0, 0);
/* 212 */     GBC gbcFontSize = (new GBC(1, 2, 3, 1)).setInsets(5, 20, 0, 0);
/* 213 */     GBC gbcLCurrentFont = (new GBC(0, 3)).setInsets(5, -40, 0, 0);
/* 214 */     GBC gbcCurrentFont = (new GBC(1, 3, 3, 1)).setInsets(5, 20, 0, 0);
/* 215 */     GBC gbcTestFont = new GBC(0, 4);
/* 216 */     GBC gbcUpdateFont = (new GBC(2, 5)).setInsets(5, -40, 0, 0);
/* 217 */     GBC gbcResetFont = (new GBC(1, 5, 3, 1)).setInsets(5, 20, 0, 0);
/*     */     
/* 219 */     this.setFontPanel.add(this.fontNameLabel, gbcLFontName);
/* 220 */     this.setFontPanel.add(this.fontNameComboBox, gbcFontName);
/* 221 */     this.setFontPanel.add(this.fontTypeLabel, gbcLFontType);
/* 222 */     this.setFontPanel.add(this.fontTypeComboBox, gbcFontType);
/* 223 */     this.setFontPanel.add(this.fontSizeLabel, gbcLFontSize);
/* 224 */     this.setFontPanel.add(this.fontSizeComboBox, gbcFontSize);
/* 225 */     this.setFontPanel.add(this.currentFontLLabel, gbcLCurrentFont);
/* 226 */     this.setFontPanel.add(this.currentFontLabel, gbcCurrentFont);
/* 227 */     this.setFontPanel.add(this.testFontLabel, gbcTestFont);
/* 228 */     this.setFontPanel.add(this.updateFontButton, gbcUpdateFont);
/* 229 */     this.setFontPanel.add(this.resetFontButton, gbcResetFont);
/*     */     
/* 231 */     this.fontNameComboBox.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent paramActionEvent) {
/* 234 */             AppSeting.this.testFontLabel.setFont(AppSeting.this.getSelectFont());
/*     */           }
/*     */         });
/* 237 */     this.fontTypeComboBox.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent paramActionEvent)
/*     */           {
/* 241 */             AppSeting.this.testFontLabel.setFont(AppSeting.this.getSelectFont());
/*     */           }
/*     */         });
/*     */     
/* 245 */     this.fontSizeComboBox.addActionListener(new ActionListener()
/*     */         {
/*     */           
/*     */           public void actionPerformed(ActionEvent paramActionEvent)
/*     */           {
/* 250 */             AppSeting.this.testFontLabel.setFont(AppSeting.this.getSelectFont());
/*     */           }
/*     */         });
/*     */     
/* 254 */     if (currentFont != null) {
/* 255 */       this.fontNameComboBox.setSelectedItem(currentFont.getName());
/* 256 */       this.fontTypeComboBox.setSelectedItem(UiFunction.getFontType(currentFont));
/* 257 */       this.fontSizeComboBox.setSelectedItem(Integer.toString(currentFont.getSize()));
/* 258 */       this.testFontLabel.setFont(currentFont);
/*     */     } 
/*     */   }
/*     */   
/*     */   void initHttpsCertConfig() {
/* 263 */     this.httpsCertConfigPanel = new SimplePanel();
/* 264 */     this.httpsCertConfigPanel.setSetup(200);
/* 265 */     this.httpsCertConfigExportButton = new JButton("????????????");
/* 266 */     this.httpsCertConfigResetButton = new JButton("????????????");
/* 267 */     this.httpsCertConfigPanel.addX(new Component[] { this.httpsCertConfigExportButton, this.httpsCertConfigResetButton });
/*     */   }
/*     */   void initGloballHttpHeader() {
/* 270 */     this.globallHttpHeaderPanel = new JPanel(new BorderLayout(1, 1));
/* 271 */     this.headerTextArea = new RTextArea();
/* 272 */     this.updateHeaderButton = new JButton("??????");
/* 273 */     this.headerTextArea.setText(ApplicationContext.getGloballHttpHeader());
/* 274 */     Dimension dimension = new Dimension();
/* 275 */     dimension.height = 30;
/* 276 */     JSplitPane splitPane = new JSplitPane();
/* 277 */     splitPane.setOrientation(0);
/* 278 */     JPanel bottomPanel = new JPanel();
/* 279 */     splitPane.setTopComponent(new JScrollPane((Component)this.headerTextArea));
/* 280 */     bottomPanel.add(this.updateHeaderButton);
/* 281 */     bottomPanel.setMaximumSize(dimension);
/* 282 */     bottomPanel.setMinimumSize(dimension);
/* 283 */     splitPane.setBottomComponent(bottomPanel);
/*     */     
/* 285 */     splitPane.setResizeWeight(0.9D);
/*     */     
/* 287 */     this.globallHttpHeaderPanel.add(splitPane);
/*     */   }
/*     */ 
/*     */   
/*     */   public void initThemesPanel() {
/* 292 */     this.themesPanel = new IJThemesPanel();
/* 293 */     this.updateThemesButton = new JButton("??????");
/* 294 */     this.themesSplitPane = new JSplitPane(0);
/* 295 */     this.themesSplitPane.setBottomComponent(this.updateThemesButton);
/* 296 */     this.themesSplitPane.setTopComponent((Component)this.themesPanel);
/* 297 */     this.themesSplitPane.setResizeWeight(0.99D);
/*     */   }
/*     */   
/*     */   void addCoreConfigPanelComponent(JLabel label, Component component) {
/* 301 */     GBC gbcl = (new GBC(0, this.currentCoreConfigPanelComponent)).setInsets(5, -40, 0, 0);
/* 302 */     GBC gbc = (new GBC(1, this.currentCoreConfigPanelComponent, 3, 1)).setInsets(5, 20, 0, 0);
/* 303 */     this.coreConfigPanel.add(label, gbcl);
/* 304 */     this.coreConfigPanel.add(component, gbc);
/* 305 */     this.currentCoreConfigPanelComponent++;
/*     */   }
/*     */   void initCoreConfigPanel() {
/* 308 */     this.coreConfigPanel = new JPanel(new GridBagLayout());
/* 309 */     this.godModeLabel = new JLabel("????????????: ");
/* 310 */     this.godModeCheckBox = new JCheckBox("????????????", ApplicationContext.isGodMode());
/*     */     
/* 312 */     this.execCommandModeLabel = new JLabel("??????????????????: ");
/* 313 */     this.execCommandModeComboBox = new JComboBox<>(new String[] { "EASY", "KNIFE", "NO_MODE" });
/*     */     
/* 315 */     this.languageLabel = new JLabel("??????");
/* 316 */     this.languageComboBox = new JComboBox<>(new String[] { "en", "zh" });
/*     */     
/* 318 */     this.isOpenCacheLabel = new JLabel("????????????");
/* 319 */     this.isOpenCacheCheckBox = new JCheckBox("??????", ApplicationContext.isOpenCache());
/*     */     
/* 321 */     this.isSuperLogLabel = new JLabel("????????????: ");
/* 322 */     this.isSuperLogCheckBox = new JCheckBox("??????", ApplicationContext.isOpenC("isSuperLog"));
/*     */     
/* 324 */     this.isAutoCloseShellLabel = new JLabel("????????????Shell");
/* 325 */     this.isAutoCloseShellCheckBox = new JCheckBox("??????", ApplicationContext.isOpenC("isAutoCloseShell"));
/*     */     
/* 327 */     this.superRequestLabel = new JLabel("??????????????????: ");
/* 328 */     this.superRequestButton = new JButton("config");
/*     */     
/* 330 */     addCoreConfigPanelComponent(this.godModeLabel, this.godModeCheckBox);
/* 331 */     addCoreConfigPanelComponent(this.execCommandModeLabel, this.execCommandModeComboBox);
/* 332 */     addCoreConfigPanelComponent(this.languageLabel, this.languageComboBox);
/* 333 */     addCoreConfigPanelComponent(this.isSuperLogLabel, this.isSuperLogCheckBox);
/* 334 */     addCoreConfigPanelComponent(this.isOpenCacheLabel, this.isOpenCacheCheckBox);
/* 335 */     addCoreConfigPanelComponent(this.isAutoCloseShellLabel, this.isAutoCloseShellCheckBox);
/* 336 */     addCoreConfigPanelComponent(this.superRequestLabel, this.superRequestButton);
/*     */     
/* 338 */     this.isSuperLogCheckBox.addActionListener((ActionListener)new OpenC("isSuperLog", this.isSuperLogCheckBox));
/* 339 */     this.isAutoCloseShellCheckBox.addActionListener((ActionListener)new OpenC("isAutoCloseShell", this.isAutoCloseShellCheckBox));
/* 340 */     this.execCommandModeComboBox.setSelectedItem(Db.getSetingValue("EXEC_COMMAND_MODE", "EASY"));
/* 341 */     this.languageComboBox.setSelectedItem(Db.getSetingValue("language", "zh".equalsIgnoreCase(Locale.getDefault().getLanguage()) ? "zh" : "en"));
/*     */     
/* 343 */     this.godModeCheckBox.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e)
/*     */           {
/* 347 */             if (ApplicationContext.setGodMode(AppSeting.this.godModeCheckBox.isSelected())) {
/* 348 */               GOptionPane.showMessageDialog(null, "????????????!", "??????", 1);
/*     */             } else {
/*     */               
/* 351 */               GOptionPane.showMessageDialog(null, "????????????!", "??????", 2);
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/* 356 */     this.execCommandModeComboBox.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 359 */             if (Db.updateSetingKV("EXEC_COMMAND_MODE", AppSeting.this.execCommandModeComboBox.getSelectedItem().toString())) {
/* 360 */               GOptionPane.showMessageDialog(null, "????????????!", "??????", 1);
/*     */             } else {
/*     */               
/* 363 */               GOptionPane.showMessageDialog(null, "????????????!", "??????", 2);
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/* 368 */     this.languageComboBox.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 371 */             if (Db.updateSetingKV("language", AppSeting.this.languageComboBox.getSelectedItem().toString())) {
/* 372 */               GOptionPane.showMessageDialog(null, "????????????!", "??????", 1);
/*     */             } else {
/*     */               
/* 375 */               GOptionPane.showMessageDialog(null, "????????????!", "??????", 2);
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/* 380 */     this.isOpenCacheCheckBox.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 383 */             if (ApplicationContext.setOpenCache(AppSeting.this.isOpenCacheCheckBox.isSelected())) {
/* 384 */               GOptionPane.showMessageDialog(null, "????????????!", "??????", 1);
/*     */             } else {
/*     */               
/* 387 */               GOptionPane.showMessageDialog(null, "????????????!", "??????", 2);
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   void initBigFilePanel() {
/* 395 */     this.bigFilePanel = new SimplePanel();
/*     */     
/* 397 */     this.bigFileErrorRetryNumLabel = new JLabel("????????????????????????: ");
/* 398 */     this.bigFileSendRequestSleepLabel = new JLabel("??????????????????(ms)");
/* 399 */     this.oneceBigFileDownloadByteNumLabel = new JLabel("????????????????????????: ");
/* 400 */     this.oneceBigFileUploadByteNumLabel = new JLabel("????????????????????????: ");
/*     */     
/* 402 */     this.oneceBigFileDownloadByteNumTextField = new JTextField(String.valueOf(Db.getSetingIntValue("oneceBigFileDownloadByteNum", 1048576)), 10);
/* 403 */     this.oneceBigFileUploadByteNumTextField = new JTextField(String.valueOf(Db.getSetingIntValue("oneceBigFileUploadByteNum", 1048576)), 10);
/*     */ 
/*     */     
/* 406 */     this.bigFileErrorRetryNumTextField = new JTextField(String.valueOf(Db.getSetingIntValue("bigFileErrorRetryNum", 10)));
/* 407 */     this.bigFileSendRequestSleepTextField = new JTextField(String.valueOf(Db.getSetingIntValue("bigFileSendRequestSleep", 521)));
/*     */ 
/*     */     
/* 410 */     this.bigFileConfigSaveButton = new JButton("????????????");
/*     */ 
/*     */     
/* 413 */     this.bigFilePanel.setSetup(-270);
/*     */     
/* 415 */     this.bigFilePanel.addX(new Component[] { this.bigFileErrorRetryNumLabel, this.bigFileErrorRetryNumTextField });
/* 416 */     this.bigFilePanel.addX(new Component[] { this.bigFileSendRequestSleepLabel, this.bigFileSendRequestSleepTextField });
/* 417 */     this.bigFilePanel.addX(new Component[] { this.oneceBigFileDownloadByteNumLabel, this.oneceBigFileDownloadByteNumTextField });
/* 418 */     this.bigFilePanel.addX(new Component[] { this.oneceBigFileUploadByteNumLabel, this.oneceBigFileUploadByteNumTextField });
/* 419 */     this.bigFilePanel.addX(new Component[] { this.bigFileConfigSaveButton });
/*     */   }
/*     */   
/*     */   public Font getSelectFont() {
/*     */     try {
/* 424 */       String fontName = (String)this.fontNameComboBox.getSelectedItem();
/* 425 */       String fontType = (String)this.fontTypeComboBox.getSelectedItem();
/* 426 */       int fontSize = Integer.parseInt((String)this.fontSizeComboBox.getSelectedItem());
/* 427 */       Font font = new Font(fontName, UiFunction.getFontType(fontType), fontSize);
/* 428 */       return font;
/* 429 */     } catch (Exception e) {
/* 430 */       Log.error(e);
/* 431 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateFontButtonClick(ActionEvent actionEvent) {
/* 438 */     ApplicationContext.setFont(getSelectFont());
/* 439 */     GOptionPane.showMessageDialog(this, "????????????! ??????????????????!", "??????", 1);
/*     */   }
/*     */   
/*     */   private void resetFontButtonClick(ActionEvent actionEvent) {
/* 443 */     ApplicationContext.resetFont();
/* 444 */     GOptionPane.showMessageDialog(this, "????????????! ??????????????????!", "??????", 1);
/*     */   }
/*     */   
/*     */   private void updateHeaderButtonClick(ActionEvent actionEvent) {
/* 448 */     String header = this.headerTextArea.getText();
/* 449 */     if (ApplicationContext.updateGloballHttpHeader(header)) {
/* 450 */       GOptionPane.showMessageDialog(this, "????????????!", "??????", 1);
/*     */     } else {
/* 452 */       GOptionPane.showMessageDialog(this, "????????????!", "??????", 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void superRequestButtonClick(ActionEvent actionEvent) {
/* 457 */     ShellSuperRequest shellSuperRequest = new ShellSuperRequest();
/*     */   }
/*     */   
/*     */   private void updateGlobalProxyButtonClick(ActionEvent actionEvent) {
/*     */     try {
/* 462 */       String globalProxyHostString = this.globalProxyHostTextField.getText().trim();
/* 463 */       String globalProxyPortString = this.globalProxyPortTextField.getText().trim();
/* 464 */       String globalProxyTypeString = this.globalProxyTypeComboBox.getSelectedItem().toString().trim();
/*     */       
/* 466 */       Db.updateSetingKV("globalProxyType", globalProxyTypeString);
/* 467 */       Db.updateSetingKV("globalProxyHost", globalProxyHostString);
/* 468 */       Db.updateSetingKV("globalProxyPort", globalProxyPortString);
/* 469 */       GOptionPane.showMessageDialog(this, "????????????!", "??????", 1);
/* 470 */     } catch (Exception e) {
/* 471 */       GOptionPane.showMessageDialog(this, "????????????!", "??????", 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void httpsCertConfigExportButtonClick(ActionEvent actionEvent) throws Exception {
/* 476 */     byte[] cert = ApplicationContext.getHttpsCert().getEncoded();
/* 477 */     GFileChooser chooser = new GFileChooser();
/* 478 */     chooser.setFileSelectionMode(0);
/* 479 */     boolean flag = (0 == chooser.showDialog(new JLabel(), "??????"));
/* 480 */     File selectdFile = chooser.getSelectedFile();
/* 481 */     if (flag && selectdFile != null) {
/* 482 */       if (!selectdFile.getName().endsWith(".crt")) {
/* 483 */         selectdFile = new File(selectdFile.getCanonicalPath() + ".crt");
/*     */       }
/* 485 */       FileOutputStream fileOutputStream = new FileOutputStream(selectdFile);
/* 486 */       fileOutputStream.write(cert);
/* 487 */       fileOutputStream.flush();
/* 488 */       fileOutputStream.close();
/* 489 */       GOptionPane.showMessageDialog(this, String.format("Succes! cert >> %s", new Object[] { selectdFile.getCanonicalPath() }), "??????", 1);
/*     */     } else {
/*     */       
/* 492 */       GOptionPane.showMessageDialog(this, "?????????????????????", "??????", 2);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void httpsCertConfigResetButtonClick(ActionEvent actionEvent) throws Exception {
/*     */     try {
/* 498 */       ApplicationContext.genHttpsConfig();
/* 499 */       GOptionPane.showMessageDialog(this, "Succes!", "??????", 1);
/* 500 */     } catch (Exception e) {
/* 501 */       GOptionPane.showMessageDialog(this, e.getMessage(), "??????", 2);
/*     */     } 
/*     */   }
/*     */   private void bigFileConfigSaveButtonClick(ActionEvent actionEvent) throws Exception {
/* 505 */     Db.updateSetingKV("oneceBigFileDownloadByteNum", this.oneceBigFileDownloadByteNumTextField.getText().trim());
/* 506 */     Db.updateSetingKV("oneceBigFileUploadByteNum", this.oneceBigFileUploadByteNumTextField.getText().trim());
/* 507 */     Db.updateSetingKV("bigFileErrorRetryNum", String.valueOf(this.bigFileErrorRetryNumTextField.getText().trim()));
/* 508 */     Db.updateSetingKV("bigFileSendRequestSleep", String.valueOf(this.bigFileSendRequestSleepTextField.getText().trim()));
/* 509 */     GOptionPane.showMessageDialog(this, "Succes!", "??????", 1);
/*     */   }
/*     */   private void updateThemesButtonClick(ActionEvent actionEvent) {
/* 512 */     IJThemeInfo ijThemeInfo = this.themesPanel.getSelect();
/* 513 */     if (ijThemeInfo != null && ApplicationContext.saveUi(ijThemeInfo)) {
/* 514 */       GOptionPane.showMessageDialog(this, "????????????!", "??????", 1);
/*     */     } else {
/* 516 */       GOptionPane.showMessageDialog(this, "????????????!", "??????", 2);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void registerPluginSeting(String tabName, Class<?> panelClass) {
/* 525 */     pluginSeting.put(tabName, panelClass);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\cor\\ui\component\dialog\AppSeting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */