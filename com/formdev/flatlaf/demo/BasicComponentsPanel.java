/*     */ package com.formdev.flatlaf.demo;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.LayoutManager;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JEditorPane;
/*     */ import javax.swing.JFormattedTextField;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JPasswordField;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.JRadioButton;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSpinner;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.JTextPane;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.text.DefaultEditorKit;
/*     */ import net.miginfocom.swing.MigLayout;
/*     */ 
/*     */ 
/*     */ class BasicComponentsPanel
/*     */   extends JPanel
/*     */ {
/*     */   BasicComponentsPanel() {
/*  31 */     initComponents();
/*     */   }
/*     */ 
/*     */   
/*     */   private void initComponents() {
/*  36 */     JLabel labelLabel = new JLabel();
/*  37 */     JLabel label1 = new JLabel();
/*  38 */     JLabel label2 = new JLabel();
/*  39 */     JLabel buttonLabel = new JLabel();
/*  40 */     JButton button1 = new JButton();
/*  41 */     JButton button2 = new JButton();
/*  42 */     JButton button5 = new JButton();
/*  43 */     JButton button6 = new JButton();
/*  44 */     JButton button3 = new JButton();
/*  45 */     JButton button4 = new JButton();
/*  46 */     JButton button13 = new JButton();
/*  47 */     JButton button14 = new JButton();
/*  48 */     JButton button15 = new JButton();
/*  49 */     JButton button16 = new JButton();
/*  50 */     JLabel checkBoxLabel = new JLabel();
/*  51 */     JCheckBox checkBox1 = new JCheckBox();
/*  52 */     JCheckBox checkBox2 = new JCheckBox();
/*  53 */     JCheckBox checkBox3 = new JCheckBox();
/*  54 */     JCheckBox checkBox4 = new JCheckBox();
/*  55 */     JLabel radioButtonLabel = new JLabel();
/*  56 */     JRadioButton radioButton1 = new JRadioButton();
/*  57 */     JRadioButton radioButton2 = new JRadioButton();
/*  58 */     JRadioButton radioButton3 = new JRadioButton();
/*  59 */     JRadioButton radioButton4 = new JRadioButton();
/*  60 */     JLabel comboBoxLabel = new JLabel();
/*  61 */     JComboBox<String> comboBox1 = new JComboBox<>();
/*  62 */     JComboBox<String> comboBox2 = new JComboBox<>();
/*  63 */     JComboBox<String> comboBox3 = new JComboBox<>();
/*  64 */     JComboBox<String> comboBox4 = new JComboBox<>();
/*  65 */     JComboBox<String> comboBox5 = new JComboBox<>();
/*  66 */     JLabel spinnerLabel = new JLabel();
/*  67 */     JSpinner spinner1 = new JSpinner();
/*  68 */     JSpinner spinner2 = new JSpinner();
/*  69 */     JComboBox<String> comboBox6 = new JComboBox<>();
/*  70 */     JLabel textFieldLabel = new JLabel();
/*  71 */     JTextField textField1 = new JTextField();
/*  72 */     JTextField textField2 = new JTextField();
/*  73 */     JTextField textField3 = new JTextField();
/*  74 */     JTextField textField4 = new JTextField();
/*  75 */     JTextField textField6 = new JTextField();
/*  76 */     JLabel formattedTextFieldLabel = new JLabel();
/*  77 */     JFormattedTextField formattedTextField1 = new JFormattedTextField();
/*  78 */     JFormattedTextField formattedTextField2 = new JFormattedTextField();
/*  79 */     JFormattedTextField formattedTextField3 = new JFormattedTextField();
/*  80 */     JFormattedTextField formattedTextField4 = new JFormattedTextField();
/*  81 */     JFormattedTextField formattedTextField5 = new JFormattedTextField();
/*  82 */     JLabel passwordFieldLabel = new JLabel();
/*  83 */     JPasswordField passwordField1 = new JPasswordField();
/*  84 */     JPasswordField passwordField2 = new JPasswordField();
/*  85 */     JPasswordField passwordField3 = new JPasswordField();
/*  86 */     JPasswordField passwordField4 = new JPasswordField();
/*  87 */     JPasswordField passwordField5 = new JPasswordField();
/*  88 */     JLabel textAreaLabel = new JLabel();
/*  89 */     JScrollPane scrollPane1 = new JScrollPane();
/*  90 */     JTextArea textArea1 = new JTextArea();
/*  91 */     JScrollPane scrollPane2 = new JScrollPane();
/*  92 */     JTextArea textArea2 = new JTextArea();
/*  93 */     JScrollPane scrollPane3 = new JScrollPane();
/*  94 */     JTextArea textArea3 = new JTextArea();
/*  95 */     JScrollPane scrollPane4 = new JScrollPane();
/*  96 */     JTextArea textArea4 = new JTextArea();
/*  97 */     JTextArea textArea5 = new JTextArea();
/*  98 */     JLabel editorPaneLabel = new JLabel();
/*  99 */     JScrollPane scrollPane5 = new JScrollPane();
/* 100 */     JEditorPane editorPane1 = new JEditorPane();
/* 101 */     JScrollPane scrollPane6 = new JScrollPane();
/* 102 */     JEditorPane editorPane2 = new JEditorPane();
/* 103 */     JScrollPane scrollPane7 = new JScrollPane();
/* 104 */     JEditorPane editorPane3 = new JEditorPane();
/* 105 */     JScrollPane scrollPane8 = new JScrollPane();
/* 106 */     JEditorPane editorPane4 = new JEditorPane();
/* 107 */     JEditorPane editorPane5 = new JEditorPane();
/* 108 */     JLabel textPaneLabel = new JLabel();
/* 109 */     JScrollPane scrollPane9 = new JScrollPane();
/* 110 */     JTextPane textPane1 = new JTextPane();
/* 111 */     JScrollPane scrollPane10 = new JScrollPane();
/* 112 */     JTextPane textPane2 = new JTextPane();
/* 113 */     JScrollPane scrollPane11 = new JScrollPane();
/* 114 */     JTextPane textPane3 = new JTextPane();
/* 115 */     JScrollPane scrollPane12 = new JScrollPane();
/* 116 */     JTextPane textPane4 = new JTextPane();
/* 117 */     JTextPane textPane5 = new JTextPane();
/* 118 */     JLabel errorHintsLabel = new JLabel();
/* 119 */     JTextField errorHintsTextField = new JTextField();
/* 120 */     JComboBox<String> errorHintsComboBox = new JComboBox<>();
/* 121 */     JSpinner errorHintsSpinner = new JSpinner();
/* 122 */     JLabel warningHintsLabel = new JLabel();
/* 123 */     JTextField warningHintsTextField = new JTextField();
/* 124 */     JComboBox<String> warningHintsComboBox = new JComboBox<>();
/* 125 */     JSpinner warningHintsSpinner = new JSpinner();
/* 126 */     JPopupMenu popupMenu1 = new JPopupMenu();
/* 127 */     JMenuItem cutMenuItem = new JMenuItem();
/* 128 */     JMenuItem copyMenuItem = new JMenuItem();
/* 129 */     JMenuItem pasteMenuItem = new JMenuItem();
/*     */ 
/*     */     
/* 132 */     setLayout((LayoutManager)new MigLayout("insets dialog,hidemode 3", "[sizegroup 1][sizegroup 1][sizegroup 1][sizegroup 1][][]", "[][][][][][][][][][][][]para[][]"));
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
/* 158 */     labelLabel.setText("JLabel:");
/* 159 */     add(labelLabel, "cell 0 0");
/*     */ 
/*     */     
/* 162 */     label1.setText("Enabled");
/* 163 */     label1.setDisplayedMnemonic('E');
/* 164 */     add(label1, "cell 1 0");
/*     */ 
/*     */     
/* 167 */     label2.setText("Disabled");
/* 168 */     label2.setDisplayedMnemonic('D');
/* 169 */     label2.setEnabled(false);
/* 170 */     add(label2, "cell 2 0");
/*     */ 
/*     */     
/* 173 */     buttonLabel.setText("JButton:");
/* 174 */     add(buttonLabel, "cell 0 1");
/*     */ 
/*     */     
/* 177 */     button1.setText("Enabled");
/* 178 */     button1.setDisplayedMnemonicIndex(0);
/* 179 */     add(button1, "cell 1 1");
/*     */ 
/*     */     
/* 182 */     button2.setText("Disabled");
/* 183 */     button2.setDisplayedMnemonicIndex(0);
/* 184 */     button2.setEnabled(false);
/* 185 */     add(button2, "cell 2 1");
/*     */ 
/*     */     
/* 188 */     button5.setText("Square");
/* 189 */     button5.putClientProperty("JButton.buttonType", "square");
/* 190 */     add(button5, "cell 3 1");
/*     */ 
/*     */     
/* 193 */     button6.setText("Round");
/* 194 */     button6.putClientProperty("JButton.buttonType", "roundRect");
/* 195 */     add(button6, "cell 4 1");
/*     */ 
/*     */     
/* 198 */     button3.setText("Help");
/* 199 */     button3.putClientProperty("JButton.buttonType", "help");
/* 200 */     add(button3, "cell 4 1");
/*     */ 
/*     */     
/* 203 */     button4.setText("Help");
/* 204 */     button4.putClientProperty("JButton.buttonType", "help");
/* 205 */     button4.setEnabled(false);
/* 206 */     add(button4, "cell 4 1");
/*     */ 
/*     */     
/* 209 */     button13.setIcon(UIManager.getIcon("Tree.closedIcon"));
/* 210 */     add(button13, "cell 5 1");
/*     */ 
/*     */     
/* 213 */     button14.setText("...");
/* 214 */     add(button14, "cell 5 1");
/*     */ 
/*     */     
/* 217 */     button15.setText("…");
/* 218 */     add(button15, "cell 5 1");
/*     */ 
/*     */     
/* 221 */     button16.setText("#");
/* 222 */     add(button16, "cell 5 1");
/*     */ 
/*     */     
/* 225 */     checkBoxLabel.setText("JCheckBox");
/* 226 */     add(checkBoxLabel, "cell 0 2");
/*     */ 
/*     */     
/* 229 */     checkBox1.setText("Enabled");
/* 230 */     checkBox1.setMnemonic('A');
/* 231 */     add(checkBox1, "cell 1 2");
/*     */ 
/*     */     
/* 234 */     checkBox2.setText("Disabled");
/* 235 */     checkBox2.setEnabled(false);
/* 236 */     checkBox2.setMnemonic('D');
/* 237 */     add(checkBox2, "cell 2 2");
/*     */ 
/*     */     
/* 240 */     checkBox3.setText("Selected");
/* 241 */     checkBox3.setSelected(true);
/* 242 */     add(checkBox3, "cell 3 2");
/*     */ 
/*     */     
/* 245 */     checkBox4.setText("Selected disabled");
/* 246 */     checkBox4.setSelected(true);
/* 247 */     checkBox4.setEnabled(false);
/* 248 */     add(checkBox4, "cell 4 2");
/*     */ 
/*     */     
/* 251 */     radioButtonLabel.setText("JRadioButton:");
/* 252 */     add(radioButtonLabel, "cell 0 3");
/*     */ 
/*     */     
/* 255 */     radioButton1.setText("Enabled");
/* 256 */     radioButton1.setMnemonic('N');
/* 257 */     add(radioButton1, "cell 1 3");
/*     */ 
/*     */     
/* 260 */     radioButton2.setText("Disabled");
/* 261 */     radioButton2.setEnabled(false);
/* 262 */     radioButton2.setMnemonic('S');
/* 263 */     add(radioButton2, "cell 2 3");
/*     */ 
/*     */     
/* 266 */     radioButton3.setText("Selected");
/* 267 */     radioButton3.setSelected(true);
/* 268 */     add(radioButton3, "cell 3 3");
/*     */ 
/*     */     
/* 271 */     radioButton4.setText("Selected disabled");
/* 272 */     radioButton4.setSelected(true);
/* 273 */     radioButton4.setEnabled(false);
/* 274 */     add(radioButton4, "cell 4 3");
/*     */ 
/*     */     
/* 277 */     comboBoxLabel.setText("JComboBox:");
/* 278 */     comboBoxLabel.setDisplayedMnemonic('C');
/* 279 */     comboBoxLabel.setLabelFor(comboBox1);
/* 280 */     add(comboBoxLabel, "cell 0 4");
/*     */ 
/*     */     
/* 283 */     comboBox1.setEditable(true);
/* 284 */     comboBox1.setModel(new DefaultComboBoxModel<>(new String[] { "Editable", "a", "bb", "ccc" }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 290 */     add(comboBox1, "cell 1 4,growx");
/*     */ 
/*     */     
/* 293 */     comboBox2.setEditable(true);
/* 294 */     comboBox2.setEnabled(false);
/* 295 */     comboBox2.setModel(new DefaultComboBoxModel<>(new String[] { "Disabled", "a", "bb", "ccc" }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 301 */     add(comboBox2, "cell 2 4,growx");
/*     */ 
/*     */     
/* 304 */     comboBox3.setModel(new DefaultComboBoxModel<>(new String[] { "Not editable", "a", "bb", "ccc" }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 310 */     add(comboBox3, "cell 3 4,growx");
/*     */ 
/*     */     
/* 313 */     comboBox4.setModel(new DefaultComboBoxModel<>(new String[] { "Not editable disabled", "a", "bb", "ccc" }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 319 */     comboBox4.setEnabled(false);
/* 320 */     add(comboBox4, "cell 4 4,growx");
/*     */ 
/*     */     
/* 323 */     comboBox5.setModel(new DefaultComboBoxModel<>(new String[] { "Wide popup if text is longer", "aa", "bbb", "cccc" }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 329 */     add(comboBox5, "cell 5 4,growx,wmax 100");
/*     */ 
/*     */     
/* 332 */     spinnerLabel.setText("JSpinner:");
/* 333 */     spinnerLabel.setLabelFor(spinner1);
/* 334 */     spinnerLabel.setDisplayedMnemonic('S');
/* 335 */     add(spinnerLabel, "cell 0 5");
/* 336 */     add(spinner1, "cell 1 5,growx");
/*     */ 
/*     */     
/* 339 */     spinner2.setEnabled(false);
/* 340 */     add(spinner2, "cell 2 5,growx");
/*     */ 
/*     */     
/* 343 */     comboBox6.setEditable(true);
/* 344 */     comboBox6.putClientProperty("JTextField.placeholderText", "Placeholder");
/* 345 */     add(comboBox6, "cell 5 5,growx");
/*     */ 
/*     */     
/* 348 */     textFieldLabel.setText("JTextField:");
/* 349 */     textFieldLabel.setDisplayedMnemonic('T');
/* 350 */     textFieldLabel.setLabelFor(textField1);
/* 351 */     add(textFieldLabel, "cell 0 6");
/*     */ 
/*     */     
/* 354 */     textField1.setText("Editable");
/* 355 */     textField1.setComponentPopupMenu(popupMenu1);
/* 356 */     add(textField1, "cell 1 6,growx");
/*     */ 
/*     */     
/* 359 */     textField2.setText("Disabled");
/* 360 */     textField2.setEnabled(false);
/* 361 */     add(textField2, "cell 2 6,growx");
/*     */ 
/*     */     
/* 364 */     textField3.setText("Not editable");
/* 365 */     textField3.setEditable(false);
/* 366 */     add(textField3, "cell 3 6,growx");
/*     */ 
/*     */     
/* 369 */     textField4.setText("Not editable disabled");
/* 370 */     textField4.setEnabled(false);
/* 371 */     textField4.setEditable(false);
/* 372 */     add(textField4, "cell 4 6,growx");
/*     */ 
/*     */     
/* 375 */     textField6.putClientProperty("JTextField.placeholderText", "Placeholder");
/* 376 */     add(textField6, "cell 5 6,growx");
/*     */ 
/*     */     
/* 379 */     formattedTextFieldLabel.setText("JFormattedTextField:");
/* 380 */     formattedTextFieldLabel.setLabelFor(formattedTextField1);
/* 381 */     formattedTextFieldLabel.setDisplayedMnemonic('O');
/* 382 */     add(formattedTextFieldLabel, "cell 0 7");
/*     */ 
/*     */     
/* 385 */     formattedTextField1.setText("Editable");
/* 386 */     formattedTextField1.setComponentPopupMenu(popupMenu1);
/* 387 */     add(formattedTextField1, "cell 1 7,growx");
/*     */ 
/*     */     
/* 390 */     formattedTextField2.setText("Disabled");
/* 391 */     formattedTextField2.setEnabled(false);
/* 392 */     add(formattedTextField2, "cell 2 7,growx");
/*     */ 
/*     */     
/* 395 */     formattedTextField3.setText("Not editable");
/* 396 */     formattedTextField3.setEditable(false);
/* 397 */     add(formattedTextField3, "cell 3 7,growx");
/*     */ 
/*     */     
/* 400 */     formattedTextField4.setText("Not editable disabled");
/* 401 */     formattedTextField4.setEnabled(false);
/* 402 */     formattedTextField4.setEditable(false);
/* 403 */     add(formattedTextField4, "cell 4 7,growx");
/*     */ 
/*     */     
/* 406 */     formattedTextField5.putClientProperty("JTextField.placeholderText", "Placeholder");
/* 407 */     add(formattedTextField5, "cell 5 7,growx");
/*     */ 
/*     */     
/* 410 */     passwordFieldLabel.setText("JPasswordField:");
/* 411 */     add(passwordFieldLabel, "cell 0 8");
/*     */ 
/*     */     
/* 414 */     passwordField1.setText("Editable");
/* 415 */     add(passwordField1, "cell 1 8,growx");
/*     */ 
/*     */     
/* 418 */     passwordField2.setText("Disabled");
/* 419 */     passwordField2.setEnabled(false);
/* 420 */     add(passwordField2, "cell 2 8,growx");
/*     */ 
/*     */     
/* 423 */     passwordField3.setText("Not editable");
/* 424 */     passwordField3.setEditable(false);
/* 425 */     add(passwordField3, "cell 3 8,growx");
/*     */ 
/*     */     
/* 428 */     passwordField4.setText("Not editable disabled");
/* 429 */     passwordField4.setEnabled(false);
/* 430 */     passwordField4.setEditable(false);
/* 431 */     add(passwordField4, "cell 4 8,growx");
/*     */ 
/*     */     
/* 434 */     passwordField5.putClientProperty("JTextField.placeholderText", "Placeholder");
/* 435 */     add(passwordField5, "cell 5 8,growx");
/*     */ 
/*     */     
/* 438 */     textAreaLabel.setText("JTextArea:");
/* 439 */     add(textAreaLabel, "cell 0 9");
/*     */ 
/*     */ 
/*     */     
/* 443 */     scrollPane1.setVerticalScrollBarPolicy(21);
/* 444 */     scrollPane1.setHorizontalScrollBarPolicy(31);
/*     */ 
/*     */     
/* 447 */     textArea1.setText("Editable");
/* 448 */     textArea1.setRows(2);
/* 449 */     scrollPane1.setViewportView(textArea1);
/*     */     
/* 451 */     add(scrollPane1, "cell 1 9,growx");
/*     */ 
/*     */ 
/*     */     
/* 455 */     scrollPane2.setVerticalScrollBarPolicy(21);
/* 456 */     scrollPane2.setHorizontalScrollBarPolicy(31);
/*     */ 
/*     */     
/* 459 */     textArea2.setText("Disabled");
/* 460 */     textArea2.setRows(2);
/* 461 */     textArea2.setEnabled(false);
/* 462 */     scrollPane2.setViewportView(textArea2);
/*     */     
/* 464 */     add(scrollPane2, "cell 2 9,growx");
/*     */ 
/*     */ 
/*     */     
/* 468 */     scrollPane3.setVerticalScrollBarPolicy(21);
/* 469 */     scrollPane3.setHorizontalScrollBarPolicy(31);
/*     */ 
/*     */     
/* 472 */     textArea3.setText("Not editable");
/* 473 */     textArea3.setRows(2);
/* 474 */     textArea3.setEditable(false);
/* 475 */     scrollPane3.setViewportView(textArea3);
/*     */     
/* 477 */     add(scrollPane3, "cell 3 9,growx");
/*     */ 
/*     */ 
/*     */     
/* 481 */     scrollPane4.setVerticalScrollBarPolicy(21);
/* 482 */     scrollPane4.setHorizontalScrollBarPolicy(31);
/*     */ 
/*     */     
/* 485 */     textArea4.setText("Not editable disabled");
/* 486 */     textArea4.setRows(2);
/* 487 */     textArea4.setEditable(false);
/* 488 */     textArea4.setEnabled(false);
/* 489 */     scrollPane4.setViewportView(textArea4);
/*     */     
/* 491 */     add(scrollPane4, "cell 4 9,growx");
/*     */ 
/*     */     
/* 494 */     textArea5.setRows(2);
/* 495 */     textArea5.setText("No scroll pane");
/* 496 */     add(textArea5, "cell 5 9,growx");
/*     */ 
/*     */     
/* 499 */     editorPaneLabel.setText("JEditorPane");
/* 500 */     add(editorPaneLabel, "cell 0 10");
/*     */ 
/*     */ 
/*     */     
/* 504 */     scrollPane5.setVerticalScrollBarPolicy(21);
/* 505 */     scrollPane5.setHorizontalScrollBarPolicy(31);
/*     */ 
/*     */     
/* 508 */     editorPane1.setText("Editable");
/* 509 */     scrollPane5.setViewportView(editorPane1);
/*     */     
/* 511 */     add(scrollPane5, "cell 1 10,growx");
/*     */ 
/*     */ 
/*     */     
/* 515 */     scrollPane6.setVerticalScrollBarPolicy(21);
/* 516 */     scrollPane6.setHorizontalScrollBarPolicy(31);
/*     */ 
/*     */     
/* 519 */     editorPane2.setText("Disabled");
/* 520 */     editorPane2.setEnabled(false);
/* 521 */     scrollPane6.setViewportView(editorPane2);
/*     */     
/* 523 */     add(scrollPane6, "cell 2 10,growx");
/*     */ 
/*     */ 
/*     */     
/* 527 */     scrollPane7.setVerticalScrollBarPolicy(21);
/* 528 */     scrollPane7.setHorizontalScrollBarPolicy(31);
/*     */ 
/*     */     
/* 531 */     editorPane3.setText("Not editable");
/* 532 */     editorPane3.setEditable(false);
/* 533 */     scrollPane7.setViewportView(editorPane3);
/*     */     
/* 535 */     add(scrollPane7, "cell 3 10,growx");
/*     */ 
/*     */ 
/*     */     
/* 539 */     scrollPane8.setVerticalScrollBarPolicy(21);
/* 540 */     scrollPane8.setHorizontalScrollBarPolicy(31);
/*     */ 
/*     */     
/* 543 */     editorPane4.setText("Not editable disabled");
/* 544 */     editorPane4.setEditable(false);
/* 545 */     editorPane4.setEnabled(false);
/* 546 */     scrollPane8.setViewportView(editorPane4);
/*     */     
/* 548 */     add(scrollPane8, "cell 4 10,growx");
/*     */ 
/*     */     
/* 551 */     editorPane5.setText("No scroll pane");
/* 552 */     add(editorPane5, "cell 5 10,growx");
/*     */ 
/*     */     
/* 555 */     textPaneLabel.setText("JTextPane:");
/* 556 */     add(textPaneLabel, "cell 0 11");
/*     */ 
/*     */ 
/*     */     
/* 560 */     scrollPane9.setVerticalScrollBarPolicy(21);
/* 561 */     scrollPane9.setHorizontalScrollBarPolicy(31);
/*     */ 
/*     */     
/* 564 */     textPane1.setText("Editable");
/* 565 */     scrollPane9.setViewportView(textPane1);
/*     */     
/* 567 */     add(scrollPane9, "cell 1 11,growx");
/*     */ 
/*     */ 
/*     */     
/* 571 */     scrollPane10.setVerticalScrollBarPolicy(21);
/* 572 */     scrollPane10.setHorizontalScrollBarPolicy(31);
/*     */ 
/*     */     
/* 575 */     textPane2.setText("Disabled");
/* 576 */     textPane2.setEnabled(false);
/* 577 */     scrollPane10.setViewportView(textPane2);
/*     */     
/* 579 */     add(scrollPane10, "cell 2 11,growx");
/*     */ 
/*     */ 
/*     */     
/* 583 */     scrollPane11.setVerticalScrollBarPolicy(21);
/* 584 */     scrollPane11.setHorizontalScrollBarPolicy(31);
/*     */ 
/*     */     
/* 587 */     textPane3.setText("Not editable");
/* 588 */     textPane3.setEditable(false);
/* 589 */     scrollPane11.setViewportView(textPane3);
/*     */     
/* 591 */     add(scrollPane11, "cell 3 11,growx");
/*     */ 
/*     */ 
/*     */     
/* 595 */     scrollPane12.setVerticalScrollBarPolicy(21);
/* 596 */     scrollPane12.setHorizontalScrollBarPolicy(31);
/*     */ 
/*     */     
/* 599 */     textPane4.setText("Not editable disabled");
/* 600 */     textPane4.setEditable(false);
/* 601 */     textPane4.setEnabled(false);
/* 602 */     scrollPane12.setViewportView(textPane4);
/*     */     
/* 604 */     add(scrollPane12, "cell 4 11,growx");
/*     */ 
/*     */     
/* 607 */     textPane5.setText("No scroll pane");
/* 608 */     add(textPane5, "cell 5 11,growx");
/*     */ 
/*     */     
/* 611 */     errorHintsLabel.setText("Error hints:");
/* 612 */     add(errorHintsLabel, "cell 0 12");
/*     */ 
/*     */     
/* 615 */     errorHintsTextField.putClientProperty("JComponent.outline", "error");
/* 616 */     add(errorHintsTextField, "cell 1 12,growx");
/*     */ 
/*     */     
/* 619 */     errorHintsComboBox.putClientProperty("JComponent.outline", "error");
/* 620 */     errorHintsComboBox.setModel(new DefaultComboBoxModel<>(new String[] { "Editable" }));
/*     */ 
/*     */     
/* 623 */     errorHintsComboBox.setEditable(true);
/* 624 */     add(errorHintsComboBox, "cell 2 12,growx");
/*     */ 
/*     */     
/* 627 */     errorHintsSpinner.putClientProperty("JComponent.outline", "error");
/* 628 */     add(errorHintsSpinner, "cell 3 12,growx");
/*     */ 
/*     */     
/* 631 */     warningHintsLabel.setText("Warning hints:");
/* 632 */     add(warningHintsLabel, "cell 0 13");
/*     */ 
/*     */     
/* 635 */     warningHintsTextField.putClientProperty("JComponent.outline", "warning");
/* 636 */     add(warningHintsTextField, "cell 1 13,growx");
/*     */ 
/*     */     
/* 639 */     warningHintsComboBox.putClientProperty("JComponent.outline", "warning");
/* 640 */     warningHintsComboBox.setModel(new DefaultComboBoxModel<>(new String[] { "Not editable" }));
/*     */ 
/*     */     
/* 643 */     add(warningHintsComboBox, "cell 2 13,growx");
/*     */ 
/*     */     
/* 646 */     warningHintsSpinner.putClientProperty("JComponent.outline", "warning");
/* 647 */     add(warningHintsSpinner, "cell 3 13,growx");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 653 */     cutMenuItem.setText("Cut");
/* 654 */     cutMenuItem.setMnemonic('C');
/* 655 */     popupMenu1.add(cutMenuItem);
/*     */ 
/*     */     
/* 658 */     copyMenuItem.setText("Copy");
/* 659 */     copyMenuItem.setMnemonic('O');
/* 660 */     popupMenu1.add(copyMenuItem);
/*     */ 
/*     */     
/* 663 */     pasteMenuItem.setText("Paste");
/* 664 */     pasteMenuItem.setMnemonic('P');
/* 665 */     popupMenu1.add(pasteMenuItem);
/*     */ 
/*     */ 
/*     */     
/* 669 */     cutMenuItem.addActionListener(new DefaultEditorKit.CutAction());
/* 670 */     copyMenuItem.addActionListener(new DefaultEditorKit.CopyAction());
/* 671 */     pasteMenuItem.addActionListener(new DefaultEditorKit.PasteAction());
/*     */     
/* 673 */     if (FlatLafDemo.screenshotsMode) {
/* 674 */       Component[] components = { button13, button14, button15, button16, comboBox5, comboBox6, textField6, passwordField5, formattedTextFieldLabel, formattedTextField1, formattedTextField2, formattedTextField3, formattedTextField4, formattedTextField5, textAreaLabel, scrollPane1, scrollPane2, scrollPane3, scrollPane4, textArea5, editorPaneLabel, scrollPane5, scrollPane6, scrollPane7, scrollPane8, editorPane5, textPaneLabel, scrollPane9, scrollPane10, scrollPane11, scrollPane12, textPane5, errorHintsLabel, errorHintsTextField, errorHintsComboBox, errorHintsSpinner, warningHintsLabel, warningHintsTextField, warningHintsComboBox, warningHintsSpinner };
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
/* 687 */       for (Component c : components) {
/* 688 */         c.setVisible(false);
/*     */       }
/*     */       
/* 691 */       Component[] formattedTextFields = { formattedTextFieldLabel, formattedTextField1, formattedTextField2, formattedTextField3, formattedTextField4 };
/* 692 */       Component[] passwordFields = { passwordFieldLabel, passwordField1, passwordField2, passwordField3, passwordField4 };
/* 693 */       MigLayout layout = (MigLayout)getLayout();
/* 694 */       for (int i = 0; i < passwordFields.length; i++) {
/* 695 */         Object cons = layout.getComponentConstraints(formattedTextFields[i]);
/* 696 */         layout.setComponentConstraints(passwordFields[i], cons);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\demo\BasicComponentsPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */