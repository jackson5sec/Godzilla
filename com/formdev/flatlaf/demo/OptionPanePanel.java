/*     */ package com.formdev.flatlaf.demo;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.LineBorder;
/*     */ import net.miginfocom.swing.MigLayout;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class OptionPanePanel
/*     */   extends JPanel
/*     */ {
/*     */   private ShowDialogLinkLabel plainShowDialogLabel;
/*     */   private ShowDialogLinkLabel errorShowDialogLabel;
/*     */   private ShowDialogLinkLabel informationShowDialogLabel;
/*     */   private JOptionPane customOptionPane;
/*     */   
/*     */   OptionPanePanel() {
/*  33 */     initComponents();
/*     */     
/*  35 */     this.customOptionPane.setMessage(new Object[] { "string", "multi-\nline string", new JCheckBox("check box"), new JTextField("text field"), "more text" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  42 */     this.customOptionPane.setOptions(new Object[] { new JCheckBox("check me"), "OK", "Cancel" });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initComponents() {
/*  51 */     JScrollPane scrollPane1 = new JScrollPane();
/*  52 */     ScrollablePanel panel9 = new ScrollablePanel();
/*  53 */     JLabel plainLabel = new JLabel();
/*  54 */     JPanel panel1 = new JPanel();
/*  55 */     JOptionPane plainOptionPane = new JOptionPane();
/*  56 */     this.plainShowDialogLabel = new ShowDialogLinkLabel();
/*  57 */     JLabel errorLabel = new JLabel();
/*  58 */     JPanel panel2 = new JPanel();
/*  59 */     JOptionPane errorOptionPane = new JOptionPane();
/*  60 */     this.errorShowDialogLabel = new ShowDialogLinkLabel();
/*  61 */     JLabel informationLabel = new JLabel();
/*  62 */     JPanel panel3 = new JPanel();
/*  63 */     JOptionPane informationOptionPane = new JOptionPane();
/*  64 */     this.informationShowDialogLabel = new ShowDialogLinkLabel();
/*  65 */     JLabel questionLabel = new JLabel();
/*  66 */     JPanel panel4 = new JPanel();
/*  67 */     JOptionPane questionOptionPane = new JOptionPane();
/*  68 */     ShowDialogLinkLabel questionShowDialogLabel = new ShowDialogLinkLabel();
/*  69 */     JLabel warningLabel = new JLabel();
/*  70 */     JPanel panel5 = new JPanel();
/*  71 */     JOptionPane warningOptionPane = new JOptionPane();
/*  72 */     ShowDialogLinkLabel warningShowDialogLabel = new ShowDialogLinkLabel();
/*  73 */     JLabel inputLabel = new JLabel();
/*  74 */     JPanel panel7 = new JPanel();
/*  75 */     JOptionPane inputOptionPane = new JOptionPane();
/*  76 */     ShowDialogLinkLabel inputShowDialogLabel = new ShowDialogLinkLabel();
/*  77 */     JLabel inputIconLabel = new JLabel();
/*  78 */     JPanel panel8 = new JPanel();
/*  79 */     JOptionPane inputIconOptionPane = new JOptionPane();
/*  80 */     ShowDialogLinkLabel inputIconShowDialogLabel = new ShowDialogLinkLabel();
/*  81 */     JLabel customLabel = new JLabel();
/*  82 */     JPanel panel6 = new JPanel();
/*  83 */     this.customOptionPane = new JOptionPane();
/*  84 */     ShowDialogLinkLabel customShowDialogLabel = new ShowDialogLinkLabel();
/*     */ 
/*     */     
/*  87 */     setLayout(new BorderLayout());
/*     */ 
/*     */ 
/*     */     
/*  91 */     scrollPane1.setBorder(BorderFactory.createEmptyBorder());
/*     */ 
/*     */ 
/*     */     
/*  95 */     panel9.setLayout((LayoutManager)new MigLayout("flowy,insets dialog,hidemode 3", "[][][fill]", "[top][top][top][top][top][top][top][top]"));
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
/* 112 */     plainLabel.setText("Plain");
/* 113 */     panel9.add(plainLabel, "cell 0 0");
/*     */ 
/*     */ 
/*     */     
/* 117 */     panel1.setBorder(LineBorder.createGrayLineBorder());
/* 118 */     panel1.setLayout(new BorderLayout());
/*     */ 
/*     */     
/* 121 */     plainOptionPane.setMessage("Hello world.");
/* 122 */     panel1.add(plainOptionPane, "Center");
/*     */     
/* 124 */     panel9.add(panel1, "cell 1 0");
/*     */ 
/*     */     
/* 127 */     this.plainShowDialogLabel.setOptionPane(plainOptionPane);
/* 128 */     this.plainShowDialogLabel.setTitleLabel(plainLabel);
/* 129 */     panel9.add(this.plainShowDialogLabel, "cell 2 0");
/*     */ 
/*     */     
/* 132 */     errorLabel.setText("Error");
/* 133 */     panel9.add(errorLabel, "cell 0 1");
/*     */ 
/*     */ 
/*     */     
/* 137 */     panel2.setBorder(LineBorder.createGrayLineBorder());
/* 138 */     panel2.setLayout(new BorderLayout());
/*     */ 
/*     */     
/* 141 */     errorOptionPane.setMessageType(0);
/* 142 */     errorOptionPane.setOptionType(2);
/* 143 */     errorOptionPane.setMessage("Your PC ran into a problem. Buy a new one.");
/* 144 */     panel2.add(errorOptionPane, "Center");
/*     */     
/* 146 */     panel9.add(panel2, "cell 1 1");
/*     */ 
/*     */     
/* 149 */     this.errorShowDialogLabel.setTitleLabel(errorLabel);
/* 150 */     this.errorShowDialogLabel.setOptionPane(errorOptionPane);
/* 151 */     panel9.add(this.errorShowDialogLabel, "cell 2 1");
/*     */ 
/*     */     
/* 154 */     informationLabel.setText("Information");
/* 155 */     panel9.add(informationLabel, "cell 0 2");
/*     */ 
/*     */ 
/*     */     
/* 159 */     panel3.setBorder(LineBorder.createGrayLineBorder());
/* 160 */     panel3.setLayout(new BorderLayout());
/*     */ 
/*     */     
/* 163 */     informationOptionPane.setMessageType(1);
/* 164 */     informationOptionPane.setOptionType(0);
/* 165 */     informationOptionPane.setMessage("Text with\nmultiple lines\n(use \\n to separate lines)");
/* 166 */     panel3.add(informationOptionPane, "Center");
/*     */     
/* 168 */     panel9.add(panel3, "cell 1 2");
/*     */ 
/*     */     
/* 171 */     this.informationShowDialogLabel.setOptionPane(informationOptionPane);
/* 172 */     this.informationShowDialogLabel.setTitleLabel(informationLabel);
/* 173 */     panel9.add(this.informationShowDialogLabel, "cell 2 2");
/*     */ 
/*     */     
/* 176 */     questionLabel.setText("Question");
/* 177 */     panel9.add(questionLabel, "cell 0 3");
/*     */ 
/*     */ 
/*     */     
/* 181 */     panel4.setBorder(LineBorder.createGrayLineBorder());
/* 182 */     panel4.setLayout(new BorderLayout());
/*     */ 
/*     */     
/* 185 */     questionOptionPane.setMessageType(3);
/* 186 */     questionOptionPane.setOptionType(1);
/* 187 */     questionOptionPane.setMessage("Answer the question. What question? Don't know. Just writing useless text to make this longer than 80 characters.");
/* 188 */     panel4.add(questionOptionPane, "Center");
/*     */     
/* 190 */     panel9.add(panel4, "cell 1 3");
/*     */ 
/*     */     
/* 193 */     questionShowDialogLabel.setOptionPane(questionOptionPane);
/* 194 */     questionShowDialogLabel.setTitleLabel(questionLabel);
/* 195 */     panel9.add(questionShowDialogLabel, "cell 2 3");
/*     */ 
/*     */     
/* 198 */     warningLabel.setText("Warning");
/* 199 */     panel9.add(warningLabel, "cell 0 4");
/*     */ 
/*     */ 
/*     */     
/* 203 */     panel5.setBorder(LineBorder.createGrayLineBorder());
/* 204 */     panel5.setLayout(new BorderLayout());
/*     */ 
/*     */     
/* 207 */     warningOptionPane.setMessageType(2);
/* 208 */     warningOptionPane.setOptionType(2);
/* 209 */     warningOptionPane.setMessage("<html>I like <b>bold</b>,<br> and I like <i>italic</i>,<br> and I like to have<br> many lines.<br> Lots of lines.");
/* 210 */     panel5.add(warningOptionPane, "Center");
/*     */     
/* 212 */     panel9.add(panel5, "cell 1 4");
/*     */ 
/*     */     
/* 215 */     warningShowDialogLabel.setOptionPane(warningOptionPane);
/* 216 */     warningShowDialogLabel.setTitleLabel(warningLabel);
/* 217 */     panel9.add(warningShowDialogLabel, "cell 2 4");
/*     */ 
/*     */     
/* 220 */     inputLabel.setText("Input");
/* 221 */     panel9.add(inputLabel, "cell 0 5");
/*     */ 
/*     */ 
/*     */     
/* 225 */     panel7.setBorder(LineBorder.createGrayLineBorder());
/* 226 */     panel7.setLayout(new BorderLayout());
/*     */ 
/*     */     
/* 229 */     inputOptionPane.setWantsInput(true);
/* 230 */     inputOptionPane.setOptionType(2);
/* 231 */     inputOptionPane.setMessage("Enter whatever you want:");
/* 232 */     panel7.add(inputOptionPane, "Center");
/*     */     
/* 234 */     panel9.add(panel7, "cell 1 5");
/*     */ 
/*     */     
/* 237 */     inputShowDialogLabel.setOptionPane(inputOptionPane);
/* 238 */     inputShowDialogLabel.setTitleLabel(inputLabel);
/* 239 */     panel9.add(inputShowDialogLabel, "cell 2 5");
/*     */ 
/*     */     
/* 242 */     inputIconLabel.setText("Input + icon");
/* 243 */     panel9.add(inputIconLabel, "cell 0 6");
/*     */ 
/*     */ 
/*     */     
/* 247 */     panel8.setBorder(LineBorder.createGrayLineBorder());
/* 248 */     panel8.setLayout(new BorderLayout());
/*     */ 
/*     */     
/* 251 */     inputIconOptionPane.setMessageType(1);
/* 252 */     inputIconOptionPane.setWantsInput(true);
/* 253 */     inputIconOptionPane.setOptionType(2);
/* 254 */     inputIconOptionPane.setMessage("Enter something:");
/* 255 */     panel8.add(inputIconOptionPane, "Center");
/*     */     
/* 257 */     panel9.add(panel8, "cell 1 6");
/*     */ 
/*     */     
/* 260 */     inputIconShowDialogLabel.setTitleLabel(inputIconLabel);
/* 261 */     inputIconShowDialogLabel.setOptionPane(inputIconOptionPane);
/* 262 */     panel9.add(inputIconShowDialogLabel, "cell 2 6");
/*     */ 
/*     */     
/* 265 */     customLabel.setText("Custom");
/* 266 */     panel9.add(customLabel, "cell 0 7");
/*     */ 
/*     */ 
/*     */     
/* 270 */     panel6.setBorder(LineBorder.createGrayLineBorder());
/* 271 */     panel6.setLayout(new BorderLayout());
/*     */ 
/*     */     
/* 274 */     this.customOptionPane.setIcon(UIManager.getIcon("Tree.leafIcon"));
/* 275 */     panel6.add(this.customOptionPane, "Center");
/*     */     
/* 277 */     panel9.add(panel6, "cell 1 7");
/*     */ 
/*     */     
/* 280 */     customShowDialogLabel.setOptionPane(this.customOptionPane);
/* 281 */     customShowDialogLabel.setTitleLabel(customLabel);
/* 282 */     panel9.add(customShowDialogLabel, "cell 2 7");
/*     */     
/* 284 */     scrollPane1.setViewportView(panel9);
/*     */     
/* 286 */     add(scrollPane1, "Center");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ShowDialogLinkLabel
/*     */     extends JLabel
/*     */   {
/*     */     private JLabel titleLabel;
/*     */ 
/*     */ 
/*     */     
/*     */     private JOptionPane optionPane;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ShowDialogLinkLabel() {
/* 306 */       setText("<html><a href=\"#\">Show dialog</a></html>");
/*     */       
/* 308 */       addMouseListener(new MouseAdapter()
/*     */           {
/*     */             public void mouseClicked(MouseEvent e) {
/* 311 */               OptionPanePanel.ShowDialogLinkLabel.this.showDialog();
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     private void showDialog() {
/* 317 */       Window window = SwingUtilities.windowForComponent(this);
/*     */       
/* 319 */       if (this.optionPane.getWantsInput()) {
/* 320 */         JOptionPane.showInputDialog(window, this.optionPane
/*     */             
/* 322 */             .getMessage(), this.titleLabel
/* 323 */             .getText() + " Title", this.optionPane
/* 324 */             .getMessageType(), this.optionPane
/* 325 */             .getIcon(), null, null);
/*     */       }
/*     */       else {
/*     */         
/* 329 */         JOptionPane.showOptionDialog(window, this.optionPane
/*     */             
/* 331 */             .getMessage(), this.titleLabel
/* 332 */             .getText() + " Title", this.optionPane
/* 333 */             .getOptionType(), this.optionPane
/* 334 */             .getMessageType(), this.optionPane
/* 335 */             .getIcon(), this.optionPane
/* 336 */             .getOptions(), this.optionPane
/* 337 */             .getInitialValue());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public JLabel getTitleLabel() {
/* 343 */       return this.titleLabel;
/*     */     }
/*     */     
/*     */     public void setTitleLabel(JLabel titleLabel) {
/* 347 */       this.titleLabel = titleLabel;
/*     */     }
/*     */ 
/*     */     
/*     */     public JOptionPane getOptionPane() {
/* 352 */       return this.optionPane;
/*     */     }
/*     */     
/*     */     public void setOptionPane(JOptionPane optionPane) {
/* 356 */       this.optionPane = optionPane;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatlaf\demo\OptionPanePanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */