/*     */ package com.formdev.flatlaf.demo;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPanel;
/*     */ 
/*     */ class NewDialog extends JDialog {
/*     */   private JPanel dialogPane;
/*     */   private JPanel contentPanel;
/*     */   private JLabel label1;
/*     */   private JTextField textField1;
/*     */   private JLabel label3;
/*     */   private JComboBox<String> comboBox2;
/*     */   private JLabel label2;
/*     */   private JComboBox<String> comboBox1;
/*     */   private JPanel buttonBar;
/*     */   private JButton okButton;
/*     */   private JButton cancelButton;
/*     */   private JMenuBar menuBar1;
/*     */   private JMenu menu1;
/*     */   private JMenuItem menuItem8;
/*     */   private JMenuItem menuItem7;
/*     */   private JMenuItem menuItem6;
/*     */   private JMenuItem menuItem5;
/*     */   private JMenuItem menuItem4;
/*     */   private JMenuItem menuItem3;
/*     */   private JMenuItem menuItem2;
/*     */   private JMenuItem menuItem1;
/*     */   private JMenu menu2;
/*     */   
/*     */   NewDialog(Window owner) {
/*  31 */     super(owner);
/*  32 */     initComponents();
/*     */ 
/*     */     
/*  35 */     this.menuBar1.setVisible(false);
/*     */     
/*  37 */     getRootPane().setDefaultButton(this.okButton);
/*     */ 
/*     */     
/*  40 */     ((JComponent)getContentPane()).registerKeyboardAction(e -> dispose(), 
/*     */         
/*  42 */         KeyStroke.getKeyStroke(27, 0, false), 1);
/*     */   }
/*     */   private JMenuItem menuItem18; private JMenuItem menuItem17; private JMenuItem menuItem16; private JMenuItem menuItem15; private JMenuItem menuItem14; private JMenuItem menuItem13; private JMenuItem menuItem12; private JMenuItem menuItem11; private JMenuItem menuItem10; private JMenuItem menuItem9; private JMenu menu3; private JMenuItem menuItem25; private JMenuItem menuItem26; private JMenuItem menuItem24; private JMenuItem menuItem23; private JMenuItem menuItem22; private JMenuItem menuItem21; private JMenuItem menuItem20; private JMenuItem menuItem19; private JPopupMenu popupMenu1; private JMenuItem cutMenuItem; private JMenuItem copyMenuItem; private JMenuItem pasteMenuItem;
/*     */   
/*     */   private void okActionPerformed() {
/*  47 */     dispose();
/*     */   }
/*     */   
/*     */   private void cancelActionPerformed() {
/*  51 */     dispose();
/*     */   }
/*     */ 
/*     */   
/*     */   private void initComponents() {
/*  56 */     this.dialogPane = new JPanel();
/*  57 */     this.contentPanel = new JPanel();
/*  58 */     this.label1 = new JLabel();
/*  59 */     this.textField1 = new JTextField();
/*  60 */     this.label3 = new JLabel();
/*  61 */     this.comboBox2 = new JComboBox<>();
/*  62 */     this.label2 = new JLabel();
/*  63 */     this.comboBox1 = new JComboBox<>();
/*  64 */     this.buttonBar = new JPanel();
/*  65 */     this.okButton = new JButton();
/*  66 */     this.cancelButton = new JButton();
/*  67 */     this.menuBar1 = new JMenuBar();
/*  68 */     this.menu1 = new JMenu();
/*  69 */     this.menuItem8 = new JMenuItem();
/*  70 */     this.menuItem7 = new JMenuItem();
/*  71 */     this.menuItem6 = new JMenuItem();
/*  72 */     this.menuItem5 = new JMenuItem();
/*  73 */     this.menuItem4 = new JMenuItem();
/*  74 */     this.menuItem3 = new JMenuItem();
/*  75 */     this.menuItem2 = new JMenuItem();
/*  76 */     this.menuItem1 = new JMenuItem();
/*  77 */     this.menu2 = new JMenu();
/*  78 */     this.menuItem18 = new JMenuItem();
/*  79 */     this.menuItem17 = new JMenuItem();
/*  80 */     this.menuItem16 = new JMenuItem();
/*  81 */     this.menuItem15 = new JMenuItem();
/*  82 */     this.menuItem14 = new JMenuItem();
/*  83 */     this.menuItem13 = new JMenuItem();
/*  84 */     this.menuItem12 = new JMenuItem();
/*  85 */     this.menuItem11 = new JMenuItem();
/*  86 */     this.menuItem10 = new JMenuItem();
/*  87 */     this.menuItem9 = new JMenuItem();
/*  88 */     this.menu3 = new JMenu();
/*  89 */     this.menuItem25 = new JMenuItem();
/*  90 */     this.menuItem26 = new JMenuItem();
/*  91 */     this.menuItem24 = new JMenuItem();
/*  92 */     this.menuItem23 = new JMenuItem();
/*  93 */     this.menuItem22 = new JMenuItem();
/*  94 */     this.menuItem21 = new JMenuItem();
/*  95 */     this.menuItem20 = new JMenuItem();
/*  96 */     this.menuItem19 = new JMenuItem();
/*  97 */     this.popupMenu1 = new JPopupMenu();
/*  98 */     this.cutMenuItem = new JMenuItem();
/*  99 */     this.copyMenuItem = new JMenuItem();
/* 100 */     this.pasteMenuItem = new JMenuItem();
/*     */ 
/*     */     
/* 103 */     setTitle("New");
/* 104 */     setDefaultCloseOperation(2);
/* 105 */     setModal(true);
/* 106 */     Container contentPane = getContentPane();
/* 107 */     contentPane.setLayout(new BorderLayout());
/*     */ 
/*     */ 
/*     */     
/* 111 */     this.dialogPane.setLayout(new BorderLayout());
/*     */ 
/*     */ 
/*     */     
/* 115 */     this.contentPanel.setLayout((LayoutManager)new MigLayout("insets dialog,hidemode 3", "[fill][grow,fill]", "[][][]"));
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
/* 126 */     this.label1.setText("Name:");
/* 127 */     this.contentPanel.add(this.label1, "cell 0 0");
/*     */ 
/*     */     
/* 130 */     this.textField1.setComponentPopupMenu(this.popupMenu1);
/* 131 */     this.contentPanel.add(this.textField1, "cell 1 0");
/*     */ 
/*     */     
/* 134 */     this.label3.setText("Package:");
/* 135 */     this.contentPanel.add(this.label3, "cell 0 1");
/*     */ 
/*     */     
/* 138 */     this.comboBox2.setEditable(true);
/* 139 */     this.comboBox2.setModel(new DefaultComboBoxModel<>(new String[] { "com.myapp", "com.myapp.core", "com.myapp.ui", "com.myapp.util", "com.myapp.extras", "com.myapp.components", "com.myapp.dialogs", "com.myapp.windows" }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 149 */     this.contentPanel.add(this.comboBox2, "cell 1 1");
/*     */ 
/*     */     
/* 152 */     this.label2.setText("Type:");
/* 153 */     this.contentPanel.add(this.label2, "cell 0 2");
/*     */ 
/*     */     
/* 156 */     this.comboBox1.setModel(new DefaultComboBoxModel<>(new String[] { "Class", "Interface", "Package", "Annotation", "Enum", "Record", "Java Project", "Project", "Folder", "File" }));
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
/* 168 */     this.contentPanel.add(this.comboBox1, "cell 1 2");
/*     */     
/* 170 */     this.dialogPane.add(this.contentPanel, "Center");
/*     */ 
/*     */ 
/*     */     
/* 174 */     this.buttonBar.setLayout((LayoutManager)new MigLayout("insets dialog,alignx right", "[button,fill][button,fill]", null));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 183 */     this.okButton.setText("OK");
/* 184 */     this.okButton.addActionListener(e -> okActionPerformed());
/* 185 */     this.buttonBar.add(this.okButton, "cell 0 0");
/*     */ 
/*     */     
/* 188 */     this.cancelButton.setText("Cancel");
/* 189 */     this.cancelButton.addActionListener(e -> cancelActionPerformed());
/* 190 */     this.buttonBar.add(this.cancelButton, "cell 1 0");
/*     */     
/* 192 */     this.dialogPane.add(this.buttonBar, "South");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 199 */     this.menu1.setText("text");
/*     */ 
/*     */     
/* 202 */     this.menuItem8.setText("text");
/* 203 */     this.menu1.add(this.menuItem8);
/*     */ 
/*     */     
/* 206 */     this.menuItem7.setText("text");
/* 207 */     this.menu1.add(this.menuItem7);
/*     */ 
/*     */     
/* 210 */     this.menuItem6.setText("text");
/* 211 */     this.menu1.add(this.menuItem6);
/*     */ 
/*     */     
/* 214 */     this.menuItem5.setText("text");
/* 215 */     this.menu1.add(this.menuItem5);
/*     */ 
/*     */     
/* 218 */     this.menuItem4.setText("text");
/* 219 */     this.menu1.add(this.menuItem4);
/*     */ 
/*     */     
/* 222 */     this.menuItem3.setText("text");
/* 223 */     this.menu1.add(this.menuItem3);
/*     */ 
/*     */     
/* 226 */     this.menuItem2.setText("text");
/* 227 */     this.menu1.add(this.menuItem2);
/*     */ 
/*     */     
/* 230 */     this.menuItem1.setText("text");
/* 231 */     this.menu1.add(this.menuItem1);
/*     */     
/* 233 */     this.menuBar1.add(this.menu1);
/*     */ 
/*     */ 
/*     */     
/* 237 */     this.menu2.setText("text");
/*     */ 
/*     */     
/* 240 */     this.menuItem18.setText("text");
/* 241 */     this.menu2.add(this.menuItem18);
/*     */ 
/*     */     
/* 244 */     this.menuItem17.setText("text");
/* 245 */     this.menu2.add(this.menuItem17);
/*     */ 
/*     */     
/* 248 */     this.menuItem16.setText("text");
/* 249 */     this.menu2.add(this.menuItem16);
/*     */ 
/*     */     
/* 252 */     this.menuItem15.setText("text");
/* 253 */     this.menu2.add(this.menuItem15);
/*     */ 
/*     */     
/* 256 */     this.menuItem14.setText("text");
/* 257 */     this.menu2.add(this.menuItem14);
/*     */ 
/*     */     
/* 260 */     this.menuItem13.setText("text");
/* 261 */     this.menu2.add(this.menuItem13);
/*     */ 
/*     */     
/* 264 */     this.menuItem12.setText("text");
/* 265 */     this.menu2.add(this.menuItem12);
/*     */ 
/*     */     
/* 268 */     this.menuItem11.setText("text");
/* 269 */     this.menu2.add(this.menuItem11);
/*     */ 
/*     */     
/* 272 */     this.menuItem10.setText("text");
/* 273 */     this.menu2.add(this.menuItem10);
/*     */ 
/*     */     
/* 276 */     this.menuItem9.setText("text");
/* 277 */     this.menu2.add(this.menuItem9);
/*     */     
/* 279 */     this.menuBar1.add(this.menu2);
/*     */ 
/*     */ 
/*     */     
/* 283 */     this.menu3.setText("text");
/*     */ 
/*     */     
/* 286 */     this.menuItem25.setText("text");
/* 287 */     this.menu3.add(this.menuItem25);
/*     */ 
/*     */     
/* 290 */     this.menuItem26.setText("text");
/* 291 */     this.menu3.add(this.menuItem26);
/*     */ 
/*     */     
/* 294 */     this.menuItem24.setText("text");
/* 295 */     this.menu3.add(this.menuItem24);
/*     */ 
/*     */     
/* 298 */     this.menuItem23.setText("text");
/* 299 */     this.menu3.add(this.menuItem23);
/*     */ 
/*     */     
/* 302 */     this.menuItem22.setText("text");
/* 303 */     this.menu3.add(this.menuItem22);
/*     */ 
/*     */     
/* 306 */     this.menuItem21.setText("text");
/* 307 */     this.menu3.add(this.menuItem21);
/*     */ 
/*     */     
/* 310 */     this.menuItem20.setText("text");
/* 311 */     this.menu3.add(this.menuItem20);
/*     */ 
/*     */     
/* 314 */     this.menuItem19.setText("text");
/* 315 */     this.menu3.add(this.menuItem19);
/*     */     
/* 317 */     this.menuBar1.add(this.menu3);
/*     */     
/* 319 */     this.dialogPane.add(this.menuBar1, "North");
/*     */     
/* 321 */     contentPane.add(this.dialogPane, "Center");
/* 322 */     pack();
/* 323 */     setLocationRelativeTo(getOwner());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 329 */     this.cutMenuItem.setText("Cut");
/* 330 */     this.cutMenuItem.setMnemonic('C');
/* 331 */     this.popupMenu1.add(this.cutMenuItem);
/*     */ 
/*     */     
/* 334 */     this.copyMenuItem.setText("Copy");
/* 335 */     this.copyMenuItem.setMnemonic('O');
/* 336 */     this.popupMenu1.add(this.copyMenuItem);
/*     */ 
/*     */     
/* 339 */     this.pasteMenuItem.setText("Paste");
/* 340 */     this.pasteMenuItem.setMnemonic('P');
/* 341 */     this.popupMenu1.add(this.pasteMenuItem);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\demo\NewDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */