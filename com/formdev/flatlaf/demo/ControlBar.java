/*     */ package com.formdev.flatlaf.demo;
/*     */ 
/*     */ import com.formdev.flatlaf.FlatDarculaLaf;
/*     */ import com.formdev.flatlaf.FlatDarkLaf;
/*     */ import com.formdev.flatlaf.FlatIntelliJLaf;
/*     */ import com.formdev.flatlaf.FlatLaf;
/*     */ import com.formdev.flatlaf.FlatLightLaf;
/*     */ import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
/*     */ import com.formdev.flatlaf.util.SystemInfo;
/*     */ import com.formdev.flatlaf.util.UIScale;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Font;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ComponentAdapter;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSeparator;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.metal.MetalLookAndFeel;
/*     */ import javax.swing.plaf.nimbus.NimbusLookAndFeel;
/*     */ import net.miginfocom.layout.ConstraintParser;
/*     */ import net.miginfocom.layout.LC;
/*     */ import net.miginfocom.layout.UnitValue;
/*     */ import net.miginfocom.swing.MigLayout;
/*     */ 
/*     */ class ControlBar extends JPanel {
/*     */   private DemoFrame frame;
/*     */   private JTabbedPane tabbedPane;
/*     */   
/*     */   ControlBar() {
/*  48 */     initComponents();
/*     */ 
/*     */     
/*  51 */     MigLayout layout = (MigLayout)getLayout();
/*  52 */     LC lc = ConstraintParser.parseLayoutConstraint((String)layout.getLayoutConstraints());
/*  53 */     UnitValue[] insets = lc.getInsets();
/*  54 */     lc.setInsets(new UnitValue[] { new UnitValue(0.0F, 0, null), insets[1], insets[2], insets[3] });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  60 */     layout.setLayoutConstraints(lc);
/*     */ 
/*     */     
/*  63 */     DefaultComboBoxModel<UIManager.LookAndFeelInfo> lafModel = new DefaultComboBoxModel<>();
/*  64 */     lafModel.addElement(new UIManager.LookAndFeelInfo("Flat Light (F1)", FlatLightLaf.class.getName()));
/*  65 */     lafModel.addElement(new UIManager.LookAndFeelInfo("Flat Dark (F2)", FlatDarkLaf.class.getName()));
/*  66 */     lafModel.addElement(new UIManager.LookAndFeelInfo("Flat IntelliJ (F3)", FlatIntelliJLaf.class.getName()));
/*  67 */     lafModel.addElement(new UIManager.LookAndFeelInfo("Flat Darcula (F4)", FlatDarculaLaf.class.getName()));
/*     */     
/*  69 */     UIManager.LookAndFeelInfo[] lookAndFeels = UIManager.getInstalledLookAndFeels();
/*  70 */     for (UIManager.LookAndFeelInfo lookAndFeel : lookAndFeels) {
/*  71 */       String name = lookAndFeel.getName();
/*  72 */       String className = lookAndFeel.getClassName();
/*  73 */       if (!className.equals("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel") && 
/*  74 */         !className.equals("com.sun.java.swing.plaf.motif.MotifLookAndFeel")) {
/*     */ 
/*     */         
/*  77 */         if ((SystemInfo.isWindows && className.equals("com.sun.java.swing.plaf.windows.WindowsLookAndFeel")) || (SystemInfo.isMacOS && className
/*  78 */           .equals("com.apple.laf.AquaLookAndFeel")) || (SystemInfo.isLinux && className
/*  79 */           .equals("com.sun.java.swing.plaf.gtk.GTKLookAndFeel"))) {
/*  80 */           name = name + " (F9)";
/*  81 */         } else if (className.equals(MetalLookAndFeel.class.getName())) {
/*  82 */           name = name + " (F12)";
/*  83 */         } else if (className.equals(NimbusLookAndFeel.class.getName())) {
/*  84 */           name = name + " (F11)";
/*     */         } 
/*  86 */         lafModel.addElement(new UIManager.LookAndFeelInfo(name, className));
/*     */       } 
/*     */     } 
/*  89 */     this.lookAndFeelComboBox.setModel(lafModel);
/*     */     
/*  91 */     UIManager.addPropertyChangeListener(e -> {
/*     */           if ("lookAndFeel".equals(e.getPropertyName())) {
/*     */             EventQueue.invokeLater(());
/*     */           }
/*     */         });
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
/* 106 */     UIScale.addPropertyChangeListener(e -> updateInfoLabel());
/*     */   }
/*     */   private JSeparator separator1; private LookAndFeelsComboBox lookAndFeelComboBox; private JCheckBox rightToLeftCheckBox;
/*     */   private JCheckBox enabledCheckBox;
/*     */   private JLabel infoLabel;
/*     */   private JButton closeButton;
/*     */   
/*     */   public void updateUI() {
/* 114 */     super.updateUI();
/*     */     
/* 116 */     if (this.infoLabel != null)
/* 117 */       updateInfoLabel(); 
/*     */   }
/*     */   
/*     */   void initialize(DemoFrame frame, JTabbedPane tabbedPane) {
/* 121 */     this.frame = frame;
/* 122 */     this.tabbedPane = tabbedPane;
/*     */ 
/*     */     
/* 125 */     registerSwitchToLookAndFeel(112, FlatLightLaf.class.getName());
/* 126 */     registerSwitchToLookAndFeel(113, FlatDarkLaf.class.getName());
/* 127 */     registerSwitchToLookAndFeel(114, FlatIntelliJLaf.class.getName());
/* 128 */     registerSwitchToLookAndFeel(115, FlatDarculaLaf.class.getName());
/*     */     
/* 130 */     if (SystemInfo.isWindows) {
/* 131 */       registerSwitchToLookAndFeel(120, "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
/* 132 */     } else if (SystemInfo.isMacOS) {
/* 133 */       registerSwitchToLookAndFeel(120, "com.apple.laf.AquaLookAndFeel");
/* 134 */     } else if (SystemInfo.isLinux) {
/* 135 */       registerSwitchToLookAndFeel(120, "com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
/* 136 */     }  registerSwitchToLookAndFeel(123, MetalLookAndFeel.class.getName());
/* 137 */     registerSwitchToLookAndFeel(122, NimbusLookAndFeel.class.getName());
/*     */ 
/*     */     
/* 140 */     ((JComponent)frame.getContentPane()).registerKeyboardAction(e -> frame.themesPanel.selectPreviousTheme(), 
/*     */         
/* 142 */         KeyStroke.getKeyStroke(38, 512), 1);
/*     */     
/* 144 */     ((JComponent)frame.getContentPane()).registerKeyboardAction(e -> frame.themesPanel.selectNextTheme(), 
/*     */         
/* 146 */         KeyStroke.getKeyStroke(40, 512), 1);
/*     */ 
/*     */ 
/*     */     
/* 150 */     ((JComponent)frame.getContentPane()).registerKeyboardAction(e -> frame.dispose(), 
/*     */ 
/*     */ 
/*     */         
/* 154 */         KeyStroke.getKeyStroke(27, 0, false), 1);
/*     */ 
/*     */ 
/*     */     
/* 158 */     frame.getRootPane().setDefaultButton(this.closeButton);
/*     */ 
/*     */     
/* 161 */     frame.addWindowListener(new WindowAdapter()
/*     */         {
/*     */           public void windowOpened(WindowEvent e) {
/* 164 */             ControlBar.this.updateInfoLabel();
/* 165 */             ControlBar.this.closeButton.requestFocusInWindow();
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 170 */     frame.addComponentListener(new ComponentAdapter()
/*     */         {
/*     */           public void componentMoved(ComponentEvent e) {
/* 173 */             ControlBar.this.updateInfoLabel();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void updateInfoLabel() {
/* 179 */     String javaVendor = System.getProperty("java.vendor");
/* 180 */     if ("Oracle Corporation".equals(javaVendor))
/* 181 */       javaVendor = null; 
/* 182 */     double systemScaleFactor = UIScale.getSystemScaleFactor(getGraphicsConfiguration());
/* 183 */     float userScaleFactor = UIScale.getUserScaleFactor();
/* 184 */     Font font = UIManager.getFont("Label.font");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 192 */     String newInfo = "(Java " + System.getProperty("java.version") + ((javaVendor != null) ? ("; " + javaVendor) : "") + ((systemScaleFactor != 1.0D) ? (";  system scale factor " + systemScaleFactor) : "") + ((userScaleFactor != 1.0F) ? (";  user scale factor " + userScaleFactor) : "") + ((systemScaleFactor == 1.0D && userScaleFactor == 1.0F) ? "; no scaling" : "") + "; " + font.getFamily() + " " + font.getSize() + (font.isBold() ? " BOLD" : "") + (font.isItalic() ? " ITALIC" : "") + ")";
/*     */ 
/*     */     
/* 195 */     if (!newInfo.equals(this.infoLabel.getText()))
/* 196 */       this.infoLabel.setText(newInfo); 
/*     */   }
/*     */   
/*     */   private void registerSwitchToLookAndFeel(int keyCode, String lafClassName) {
/* 200 */     ((JComponent)this.frame.getContentPane()).registerKeyboardAction(e -> selectLookAndFeel(lafClassName), 
/*     */ 
/*     */ 
/*     */         
/* 204 */         KeyStroke.getKeyStroke(keyCode, 0, false), 1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void selectLookAndFeel(String lafClassName) {
/* 209 */     this.lookAndFeelComboBox.setSelectedLookAndFeel(lafClassName);
/*     */   }
/*     */   
/*     */   private void lookAndFeelChanged() {
/* 213 */     String lafClassName = this.lookAndFeelComboBox.getSelectedLookAndFeel();
/* 214 */     if (lafClassName == null) {
/*     */       return;
/*     */     }
/* 217 */     if (lafClassName.equals(UIManager.getLookAndFeel().getClass().getName())) {
/*     */       return;
/*     */     }
/* 220 */     EventQueue.invokeLater(() -> {
/*     */           try {
/*     */             FlatAnimatedLafChange.showSnapshot();
/*     */             
/*     */             UIManager.setLookAndFeel(lafClassName);
/*     */             
/*     */             if (!(UIManager.getLookAndFeel() instanceof FlatLaf)) {
/*     */               UIManager.put("defaultFont", null);
/*     */             }
/*     */             
/*     */             FlatLaf.updateUI();
/*     */             
/*     */             FlatAnimatedLafChange.hideSnapshotWithAnimation();
/*     */             
/*     */             int width = this.frame.getWidth();
/*     */             
/*     */             int height = this.frame.getHeight();
/*     */             
/*     */             Dimension prefSize = this.frame.getPreferredSize();
/*     */             if (prefSize.width > width || prefSize.height > height) {
/*     */               this.frame.setSize(Math.max(prefSize.width, width), Math.max(prefSize.height, height));
/*     */             }
/* 242 */           } catch (Exception ex) {
/*     */             ex.printStackTrace();
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private void rightToLeftChanged() {
/* 249 */     boolean rightToLeft = this.rightToLeftCheckBox.isSelected();
/* 250 */     rightToLeftChanged(this.frame, rightToLeft);
/*     */   }
/*     */   
/*     */   private void rightToLeftChanged(Container c, boolean rightToLeft) {
/* 254 */     c.applyComponentOrientation(rightToLeft ? ComponentOrientation.RIGHT_TO_LEFT : ComponentOrientation.LEFT_TO_RIGHT);
/*     */ 
/*     */     
/* 257 */     c.revalidate();
/* 258 */     c.repaint();
/*     */   }
/*     */   
/*     */   private void enabledChanged() {
/* 262 */     enabledDisable(this.tabbedPane, this.enabledCheckBox.isSelected());
/*     */ 
/*     */     
/* 265 */     this.tabbedPane.repaint();
/*     */   }
/*     */   
/*     */   private void enabledDisable(Container container, boolean enabled) {
/* 269 */     for (Component c : container.getComponents()) {
/* 270 */       if (c instanceof JPanel) {
/* 271 */         enabledDisable((JPanel)c, enabled);
/*     */       }
/*     */       else {
/*     */         
/* 275 */         c.setEnabled(enabled);
/*     */         
/* 277 */         if (c instanceof JScrollPane) {
/* 278 */           Component view = ((JScrollPane)c).getViewport().getView();
/* 279 */           if (view != null)
/* 280 */             view.setEnabled(enabled); 
/* 281 */         } else if (c instanceof JTabbedPane) {
/* 282 */           JTabbedPane tabPane = (JTabbedPane)c;
/* 283 */           int tabCount = tabPane.getTabCount();
/* 284 */           for (int i = 0; i < tabCount; i++) {
/* 285 */             Component tab = tabPane.getComponentAt(i);
/* 286 */             if (tab != null) {
/* 287 */               tab.setEnabled(enabled);
/*     */             }
/*     */           } 
/*     */         } 
/* 291 */         if (c instanceof JToolBar)
/* 292 */           enabledDisable((JToolBar)c, enabled); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void closePerformed() {
/* 297 */     this.frame.dispose();
/*     */   }
/*     */ 
/*     */   
/*     */   private void initComponents() {
/* 302 */     this.separator1 = new JSeparator();
/* 303 */     this.lookAndFeelComboBox = new LookAndFeelsComboBox();
/* 304 */     this.rightToLeftCheckBox = new JCheckBox();
/* 305 */     this.enabledCheckBox = new JCheckBox();
/* 306 */     this.infoLabel = new JLabel();
/* 307 */     this.closeButton = new JButton();
/*     */ 
/*     */     
/* 310 */     setLayout((LayoutManager)new MigLayout("insets dialog", "[fill][fill][fill][grow,fill][button,fill]", "[bottom][]"));
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
/* 321 */     add(this.separator1, "cell 0 0 5 1");
/*     */ 
/*     */     
/* 324 */     this.lookAndFeelComboBox.addActionListener(e -> lookAndFeelChanged());
/* 325 */     add(this.lookAndFeelComboBox, "cell 0 1");
/*     */ 
/*     */     
/* 328 */     this.rightToLeftCheckBox.setText("right-to-left");
/* 329 */     this.rightToLeftCheckBox.setMnemonic('R');
/* 330 */     this.rightToLeftCheckBox.addActionListener(e -> rightToLeftChanged());
/* 331 */     add(this.rightToLeftCheckBox, "cell 1 1");
/*     */ 
/*     */     
/* 334 */     this.enabledCheckBox.setText("enabled");
/* 335 */     this.enabledCheckBox.setMnemonic('E');
/* 336 */     this.enabledCheckBox.setSelected(true);
/* 337 */     this.enabledCheckBox.addActionListener(e -> enabledChanged());
/* 338 */     add(this.enabledCheckBox, "cell 2 1");
/*     */ 
/*     */     
/* 341 */     this.infoLabel.setText("text");
/* 342 */     add(this.infoLabel, "cell 3 1,alignx center,growx 0");
/*     */ 
/*     */     
/* 345 */     this.closeButton.setText("Close");
/* 346 */     this.closeButton.addActionListener(e -> closePerformed());
/* 347 */     add(this.closeButton, "cell 4 1");
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\demo\ControlBar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */