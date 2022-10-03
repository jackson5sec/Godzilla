/*     */ package com.formdev.flatlaf.demo;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.LayoutManager;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JEditorPane;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JProgressBar;
/*     */ import javax.swing.JScrollBar;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSeparator;
/*     */ import javax.swing.JSlider;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.JTextPane;
/*     */ import javax.swing.JToggleButton;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.JToolTip;
/*     */ import javax.swing.UIManager;
/*     */ import net.miginfocom.swing.MigLayout;
/*     */ 
/*     */ class MoreComponentsPanel extends JPanel {
/*     */   private JProgressBar progressBar3;
/*     */   private JProgressBar progressBar4;
/*     */   private JSlider slider3;
/*     */   
/*     */   MoreComponentsPanel() {
/*  31 */     initComponents();
/*     */   }
/*     */   private JProgressBar progressBar1; private JProgressBar progressBar2; private JCheckBox indeterminateCheckBox;
/*     */   private void changeProgress() {
/*  35 */     int value = this.slider3.getValue();
/*  36 */     this.progressBar1.setValue(value);
/*  37 */     this.progressBar2.setValue(value);
/*  38 */     this.progressBar3.setValue(value);
/*  39 */     this.progressBar4.setValue(value);
/*     */   }
/*     */   
/*     */   private void indeterminateCheckBoxActionPerformed() {
/*  43 */     boolean indeterminate = this.indeterminateCheckBox.isSelected();
/*  44 */     this.progressBar1.setIndeterminate(indeterminate);
/*  45 */     this.progressBar2.setIndeterminate(indeterminate);
/*  46 */     this.progressBar3.setIndeterminate(indeterminate);
/*  47 */     this.progressBar4.setIndeterminate(indeterminate);
/*     */   }
/*     */ 
/*     */   
/*     */   private void initComponents() {
/*  52 */     JLabel scrollPaneLabel = new JLabel();
/*  53 */     JScrollPane scrollPane13 = new JScrollPane();
/*  54 */     JPanel panel1 = new JPanel();
/*  55 */     JScrollBar scrollBar2 = new JScrollBar();
/*  56 */     JScrollBar scrollBar3 = new JScrollBar();
/*  57 */     JScrollBar scrollBar7 = new JScrollBar();
/*  58 */     JScrollBar scrollBar8 = new JScrollBar();
/*  59 */     JSeparator separator2 = new JSeparator();
/*  60 */     JSlider slider2 = new JSlider();
/*  61 */     JSlider slider4 = new JSlider();
/*  62 */     this.progressBar3 = new JProgressBar();
/*  63 */     this.progressBar4 = new JProgressBar();
/*  64 */     JToolBar toolBar2 = new JToolBar();
/*  65 */     JButton button9 = new JButton();
/*  66 */     JButton button10 = new JButton();
/*  67 */     JButton button11 = new JButton();
/*  68 */     JToggleButton toggleButton7 = new JToggleButton();
/*  69 */     JPanel panel2 = new JPanel();
/*  70 */     JLabel scrollBarLabel = new JLabel();
/*  71 */     JScrollBar scrollBar1 = new JScrollBar();
/*  72 */     JScrollBar scrollBar4 = new JScrollBar();
/*  73 */     JPanel panel3 = new JPanel();
/*  74 */     JLabel label4 = new JLabel();
/*  75 */     JLabel label3 = new JLabel();
/*  76 */     JScrollPane scrollPane15 = new JScrollPane();
/*  77 */     JEditorPane editorPane6 = new JEditorPane();
/*  78 */     JScrollPane scrollPane16 = new JScrollPane();
/*  79 */     JTextPane textPane6 = new JTextPane();
/*  80 */     JScrollBar scrollBar5 = new JScrollBar();
/*  81 */     JScrollBar scrollBar6 = new JScrollBar();
/*  82 */     JLabel separatorLabel = new JLabel();
/*  83 */     JSeparator separator1 = new JSeparator();
/*  84 */     JLabel sliderLabel = new JLabel();
/*  85 */     JSlider slider1 = new JSlider();
/*  86 */     JSlider slider6 = new JSlider();
/*  87 */     this.slider3 = new JSlider();
/*  88 */     JSlider slider5 = new JSlider();
/*  89 */     JLabel progressBarLabel = new JLabel();
/*  90 */     this.progressBar1 = new JProgressBar();
/*  91 */     this.progressBar2 = new JProgressBar();
/*  92 */     this.indeterminateCheckBox = new JCheckBox();
/*  93 */     JLabel toolTipLabel = new JLabel();
/*  94 */     JToolTip toolTip1 = new JToolTip();
/*  95 */     JToolTip toolTip2 = new JToolTip();
/*  96 */     JLabel toolBarLabel = new JLabel();
/*  97 */     JToolBar toolBar1 = new JToolBar();
/*  98 */     JButton button4 = new JButton();
/*  99 */     JButton button6 = new JButton();
/* 100 */     JButton button7 = new JButton();
/* 101 */     JButton button8 = new JButton();
/* 102 */     JToggleButton toggleButton6 = new JToggleButton();
/* 103 */     JButton button1 = new JButton();
/* 104 */     JLabel splitPaneLabel = new JLabel();
/* 105 */     JSplitPane splitPane3 = new JSplitPane();
/* 106 */     JSplitPane splitPane1 = new JSplitPane();
/* 107 */     JPanel panel10 = new JPanel();
/* 108 */     JLabel label1 = new JLabel();
/* 109 */     JPanel panel11 = new JPanel();
/* 110 */     JLabel label2 = new JLabel();
/* 111 */     JSplitPane splitPane2 = new JSplitPane();
/* 112 */     JPanel panel12 = new JPanel();
/* 113 */     JLabel label5 = new JLabel();
/* 114 */     JPanel panel13 = new JPanel();
/* 115 */     JLabel label6 = new JLabel();
/*     */ 
/*     */     
/* 118 */     setLayout((LayoutManager)new MigLayout("insets dialog,hidemode 3", "[][][][][]", "[][][][][][][][][][][][100,top]"));
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
/* 141 */     scrollPaneLabel.setText("JScrollPane:");
/* 142 */     add(scrollPaneLabel, "cell 0 0");
/*     */ 
/*     */ 
/*     */     
/* 146 */     scrollPane13.setHorizontalScrollBarPolicy(32);
/* 147 */     scrollPane13.setVerticalScrollBarPolicy(22);
/*     */ 
/*     */ 
/*     */     
/* 151 */     panel1.setPreferredSize(new Dimension(200, 200));
/* 152 */     panel1.setLayout(new BorderLayout());
/*     */     
/* 154 */     scrollPane13.setViewportView(panel1);
/*     */     
/* 156 */     add(scrollPane13, "cell 1 0,grow,width 70,height 40");
/* 157 */     add(scrollBar2, "cell 2 0 1 6,growy");
/*     */ 
/*     */     
/* 160 */     scrollBar3.setEnabled(false);
/* 161 */     add(scrollBar3, "cell 2 0 1 6,growy");
/*     */ 
/*     */     
/* 164 */     scrollBar7.putClientProperty("JScrollBar.showButtons", Boolean.valueOf(true));
/* 165 */     add(scrollBar7, "cell 2 0 1 6,growy");
/*     */ 
/*     */     
/* 168 */     scrollBar8.setEnabled(false);
/* 169 */     scrollBar8.putClientProperty("JScrollBar.showButtons", Boolean.valueOf(true));
/* 170 */     add(scrollBar8, "cell 2 0 1 6,growy");
/*     */ 
/*     */     
/* 173 */     separator2.setOrientation(1);
/* 174 */     add(separator2, "cell 2 0 1 6,growy");
/*     */ 
/*     */     
/* 177 */     slider2.setOrientation(1);
/* 178 */     slider2.setValue(30);
/* 179 */     add(slider2, "cell 2 0 1 6,growy,height 100");
/*     */ 
/*     */     
/* 182 */     slider4.setMinorTickSpacing(10);
/* 183 */     slider4.setPaintTicks(true);
/* 184 */     slider4.setMajorTickSpacing(50);
/* 185 */     slider4.setPaintLabels(true);
/* 186 */     slider4.setOrientation(1);
/* 187 */     slider4.setValue(30);
/* 188 */     add(slider4, "cell 2 0 1 6,growy,height 100");
/*     */ 
/*     */     
/* 191 */     this.progressBar3.setOrientation(1);
/* 192 */     this.progressBar3.setValue(60);
/* 193 */     add(this.progressBar3, "cell 2 0 1 6,growy");
/*     */ 
/*     */     
/* 196 */     this.progressBar4.setOrientation(1);
/* 197 */     this.progressBar4.setValue(60);
/* 198 */     this.progressBar4.setStringPainted(true);
/* 199 */     add(this.progressBar4, "cell 2 0 1 6,growy");
/*     */ 
/*     */ 
/*     */     
/* 203 */     toolBar2.setOrientation(1);
/*     */ 
/*     */     
/* 206 */     button9.setIcon(UIManager.getIcon("Tree.closedIcon"));
/* 207 */     toolBar2.add(button9);
/*     */ 
/*     */     
/* 210 */     button10.setIcon(UIManager.getIcon("Tree.openIcon"));
/* 211 */     toolBar2.add(button10);
/* 212 */     toolBar2.addSeparator();
/*     */ 
/*     */     
/* 215 */     button11.setIcon(UIManager.getIcon("Tree.leafIcon"));
/* 216 */     toolBar2.add(button11);
/*     */ 
/*     */     
/* 219 */     toggleButton7.setIcon(UIManager.getIcon("Tree.closedIcon"));
/* 220 */     toolBar2.add(toggleButton7);
/*     */     
/* 222 */     add(toolBar2, "cell 2 0 1 6,growy");
/*     */ 
/*     */ 
/*     */     
/* 226 */     panel2.setBorder(new TitledBorder("TitledBorder"));
/* 227 */     panel2.setLayout(new FlowLayout());
/*     */     
/* 229 */     add(panel2, "cell 3 0 1 6,grow");
/*     */ 
/*     */     
/* 232 */     scrollBarLabel.setText("JScrollBar:");
/* 233 */     add(scrollBarLabel, "cell 0 1");
/*     */ 
/*     */     
/* 236 */     scrollBar1.setOrientation(0);
/* 237 */     add(scrollBar1, "cell 1 1,growx");
/*     */ 
/*     */     
/* 240 */     scrollBar4.setOrientation(0);
/* 241 */     scrollBar4.setEnabled(false);
/* 242 */     add(scrollBar4, "cell 1 2,growx");
/*     */ 
/*     */ 
/*     */     
/* 246 */     panel3.setOpaque(false);
/* 247 */     panel3.setLayout((LayoutManager)new MigLayout("ltr,insets 0,hidemode 3", "[]", "[][][][]"));
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
/* 258 */     label4.setText("HTML:");
/* 259 */     panel3.add(label4, "cell 0 0");
/*     */ 
/*     */     
/* 262 */     label3.setText("<html>JLabel HTML<br>Sample <b>content</b><br> <u>text</u> with <a href=\"#\">link</a></html>");
/* 263 */     panel3.add(label3, "cell 0 1");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 269 */     editorPane6.setContentType("text/html");
/* 270 */     editorPane6.setText("JEditorPane HTML<br>Sample <b>content</b><br> <u>text</u> with <a href=\"#\">link</a>");
/* 271 */     scrollPane15.setViewportView(editorPane6);
/*     */     
/* 273 */     panel3.add(scrollPane15, "cell 0 2,grow");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 279 */     textPane6.setContentType("text/html");
/* 280 */     textPane6.setText("JTextPane HTML<br>Sample <b>content</b><br> <u>text</u> with <a href=\"#\">link</a>");
/* 281 */     scrollPane16.setViewportView(textPane6);
/*     */     
/* 283 */     panel3.add(scrollPane16, "cell 0 3,grow");
/*     */     
/* 285 */     add(panel3, "cell 4 0 1 8,aligny top,growy 0");
/*     */ 
/*     */     
/* 288 */     scrollBar5.setOrientation(0);
/* 289 */     scrollBar5.putClientProperty("JScrollBar.showButtons", Boolean.valueOf(true));
/* 290 */     add(scrollBar5, "cell 1 3,growx");
/*     */ 
/*     */     
/* 293 */     scrollBar6.setOrientation(0);
/* 294 */     scrollBar6.setEnabled(false);
/* 295 */     scrollBar6.putClientProperty("JScrollBar.showButtons", Boolean.valueOf(true));
/* 296 */     add(scrollBar6, "cell 1 4,growx");
/*     */ 
/*     */     
/* 299 */     separatorLabel.setText("JSeparator:");
/* 300 */     add(separatorLabel, "cell 0 5");
/* 301 */     add(separator1, "cell 1 5,growx");
/*     */ 
/*     */     
/* 304 */     sliderLabel.setText("JSlider:");
/* 305 */     add(sliderLabel, "cell 0 6");
/*     */ 
/*     */     
/* 308 */     slider1.setValue(30);
/* 309 */     add(slider1, "cell 1 6 3 1,aligny top,grow 100 0");
/*     */ 
/*     */     
/* 312 */     slider6.setEnabled(false);
/* 313 */     slider6.setValue(30);
/* 314 */     add(slider6, "cell 1 6 3 1,aligny top,growy 0");
/*     */ 
/*     */     
/* 317 */     this.slider3.setMinorTickSpacing(10);
/* 318 */     this.slider3.setPaintTicks(true);
/* 319 */     this.slider3.setMajorTickSpacing(50);
/* 320 */     this.slider3.setPaintLabels(true);
/* 321 */     this.slider3.setValue(30);
/* 322 */     this.slider3.addChangeListener(e -> changeProgress());
/* 323 */     add(this.slider3, "cell 1 7 3 1,aligny top,grow 100 0");
/*     */ 
/*     */     
/* 326 */     slider5.setMinorTickSpacing(10);
/* 327 */     slider5.setPaintTicks(true);
/* 328 */     slider5.setMajorTickSpacing(50);
/* 329 */     slider5.setPaintLabels(true);
/* 330 */     slider5.setEnabled(false);
/* 331 */     slider5.setValue(30);
/* 332 */     add(slider5, "cell 1 7 3 1,aligny top,growy 0");
/*     */ 
/*     */     
/* 335 */     progressBarLabel.setText("JProgressBar:");
/* 336 */     add(progressBarLabel, "cell 0 8");
/*     */ 
/*     */     
/* 339 */     this.progressBar1.setValue(60);
/* 340 */     add(this.progressBar1, "cell 1 8 3 1,growx");
/*     */ 
/*     */     
/* 343 */     this.progressBar2.setStringPainted(true);
/* 344 */     this.progressBar2.setValue(60);
/* 345 */     add(this.progressBar2, "cell 1 8 3 1,growx");
/*     */ 
/*     */     
/* 348 */     this.indeterminateCheckBox.setText("indeterminate");
/* 349 */     this.indeterminateCheckBox.addActionListener(e -> indeterminateCheckBoxActionPerformed());
/* 350 */     add(this.indeterminateCheckBox, "cell 4 8");
/*     */ 
/*     */     
/* 353 */     toolTipLabel.setText("JToolTip:");
/* 354 */     add(toolTipLabel, "cell 0 9");
/*     */ 
/*     */     
/* 357 */     toolTip1.setTipText("Some text in tool tip.");
/* 358 */     add(toolTip1, "cell 1 9 3 1");
/*     */ 
/*     */     
/* 361 */     toolTip2.setTipText("Tool tip with\nmultiple\nlines.");
/* 362 */     add(toolTip2, "cell 1 9 3 1");
/*     */ 
/*     */     
/* 365 */     toolBarLabel.setText("JToolBar:");
/* 366 */     add(toolBarLabel, "cell 0 10");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 372 */     button4.setIcon(UIManager.getIcon("Tree.closedIcon"));
/* 373 */     toolBar1.add(button4);
/*     */ 
/*     */     
/* 376 */     button6.setIcon(UIManager.getIcon("Tree.openIcon"));
/* 377 */     toolBar1.add(button6);
/* 378 */     toolBar1.addSeparator();
/*     */ 
/*     */     
/* 381 */     button7.setIcon(UIManager.getIcon("Tree.leafIcon"));
/* 382 */     toolBar1.add(button7);
/* 383 */     toolBar1.addSeparator();
/*     */ 
/*     */     
/* 386 */     button8.setText("Text");
/* 387 */     button8.setIcon(UIManager.getIcon("Tree.expandedIcon"));
/* 388 */     toolBar1.add(button8);
/*     */ 
/*     */     
/* 391 */     toggleButton6.setText("Toggle");
/* 392 */     toggleButton6.setIcon(UIManager.getIcon("Tree.leafIcon"));
/* 393 */     toggleButton6.setSelected(true);
/* 394 */     toolBar1.add(toggleButton6);
/*     */ 
/*     */     
/* 397 */     button1.setIcon(new ImageIcon(getClass().getResource("/com/formdev/flatlaf/demo/icons/intellij-showWriteAccess.png")));
/* 398 */     button1.setEnabled(false);
/* 399 */     toolBar1.add(button1);
/*     */     
/* 401 */     add(toolBar1, "cell 1 10 3 1,growx");
/*     */ 
/*     */     
/* 404 */     splitPaneLabel.setText("JSplitPane:");
/* 405 */     add(splitPaneLabel, "cell 0 11");
/*     */ 
/*     */ 
/*     */     
/* 409 */     splitPane3.setResizeWeight(0.5D);
/*     */ 
/*     */ 
/*     */     
/* 413 */     splitPane1.setResizeWeight(0.5D);
/*     */ 
/*     */ 
/*     */     
/* 417 */     panel10.setBackground(new Color(217, 163, 67));
/* 418 */     panel10.setLayout(new BorderLayout());
/*     */ 
/*     */     
/* 421 */     label1.setText("LEFT");
/* 422 */     label1.setHorizontalAlignment(0);
/* 423 */     label1.setForeground(Color.white);
/* 424 */     panel10.add(label1, "Center");
/*     */     
/* 426 */     splitPane1.setLeftComponent(panel10);
/*     */ 
/*     */ 
/*     */     
/* 430 */     panel11.setBackground(new Color(98, 181, 67));
/* 431 */     panel11.setLayout(new BorderLayout());
/*     */ 
/*     */     
/* 434 */     label2.setText("RIGHT");
/* 435 */     label2.setHorizontalAlignment(0);
/* 436 */     label2.setForeground(Color.white);
/* 437 */     panel11.add(label2, "Center");
/*     */     
/* 439 */     splitPane1.setRightComponent(panel11);
/*     */     
/* 441 */     splitPane3.setLeftComponent(splitPane1);
/*     */ 
/*     */ 
/*     */     
/* 445 */     splitPane2.setOrientation(0);
/* 446 */     splitPane2.setResizeWeight(0.5D);
/*     */ 
/*     */ 
/*     */     
/* 450 */     panel12.setBackground(new Color(242, 101, 34));
/* 451 */     panel12.setLayout(new BorderLayout());
/*     */ 
/*     */     
/* 454 */     label5.setText("TOP");
/* 455 */     label5.setHorizontalAlignment(0);
/* 456 */     label5.setForeground(Color.white);
/* 457 */     panel12.add(label5, "Center");
/*     */     
/* 459 */     splitPane2.setTopComponent(panel12);
/*     */ 
/*     */ 
/*     */     
/* 463 */     panel13.setBackground(new Color(64, 182, 224));
/* 464 */     panel13.setLayout(new BorderLayout());
/*     */ 
/*     */     
/* 467 */     label6.setText("BOTTOM");
/* 468 */     label6.setHorizontalAlignment(0);
/* 469 */     label6.setForeground(Color.white);
/* 470 */     panel13.add(label6, "Center");
/*     */     
/* 472 */     splitPane2.setBottomComponent(panel13);
/*     */     
/* 474 */     splitPane3.setRightComponent(splitPane2);
/*     */     
/* 476 */     add(splitPane3, "cell 1 11 4 1,grow");
/*     */ 
/*     */     
/* 479 */     if (FlatLafDemo.screenshotsMode) {
/* 480 */       Component[] components = { this.indeterminateCheckBox, toolTipLabel, toolTip1, toolTip2, toolBarLabel, toolBar1, toolBar2, splitPaneLabel, splitPane3 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 487 */       for (Component c : components)
/* 488 */         c.setVisible(false); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\demo\MoreComponentsPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */