/*     */ package org.fife.rsta.ac.demo;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.SystemColor;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.File;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.Spring;
/*     */ import javax.swing.SpringLayout;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.AbstractBorder;
/*     */ import javax.swing.border.Border;
/*     */ import org.fife.rsta.ac.java.buildpath.JarLibraryInfo;
/*     */ import org.fife.rsta.ac.java.buildpath.LibraryInfo;
/*     */ import org.fife.rsta.ac.perl.PerlLanguageSupport;
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
/*     */ public class AboutDialog
/*     */   extends JDialog
/*     */ {
/*     */   public AboutDialog(DemoApp parent) {
/*  52 */     super(parent);
/*     */     
/*  54 */     JPanel cp = new JPanel(new BorderLayout());
/*     */     
/*  56 */     Box box = Box.createVerticalBox();
/*     */ 
/*     */ 
/*     */     
/*  60 */     JPanel box2 = new JPanel();
/*  61 */     box2.setLayout(new BoxLayout(box2, 1));
/*  62 */     box2.setOpaque(true);
/*  63 */     box2.setBackground(Color.white);
/*  64 */     box2.setBorder(new TopBorder());
/*     */     
/*  66 */     JLabel label = new JLabel("Language Support Demo");
/*  67 */     label.setOpaque(true);
/*  68 */     label.setBackground(Color.white);
/*  69 */     Font labelFont = label.getFont();
/*  70 */     label.setFont(labelFont.deriveFont(1, 20.0F));
/*  71 */     addLeftAligned(label, box2);
/*  72 */     box2.add(Box.createVerticalStrut(5));
/*     */     
/*  74 */     JTextArea textArea = new JTextArea(6, 60);
/*     */     
/*  76 */     textArea.setFont(labelFont);
/*  77 */     textArea.setText("Version 0.2\n\nDemonstrates basic features of the RSTALanguageSupport library.\nNote that some features for some languages may not work unless your system is set up properly.\nFor example, Java code completion requires a JRE on your PATH, and Perl completion requires the Perl executable to be on your PATH.");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  83 */     textArea.setEditable(false);
/*  84 */     textArea.setBackground(Color.white);
/*  85 */     textArea.setLineWrap(true);
/*  86 */     textArea.setWrapStyleWord(true);
/*  87 */     textArea.setBorder((Border)null);
/*  88 */     box2.add(textArea);
/*     */     
/*  90 */     box.add(box2);
/*  91 */     box.add(Box.createVerticalStrut(5));
/*     */     
/*  93 */     SpringLayout sl = new SpringLayout();
/*  94 */     JPanel temp = new JPanel(sl);
/*  95 */     JLabel perlLabel = new JLabel("Perl install location:");
/*  96 */     File loc = PerlLanguageSupport.getDefaultPerlInstallLocation();
/*  97 */     String text = (loc == null) ? null : loc.getAbsolutePath();
/*  98 */     JTextField perlField = createTextField(text);
/*  99 */     JLabel javaLabel = new JLabel("Java home:");
/* 100 */     String jre = null;
/* 101 */     LibraryInfo info = LibraryInfo.getMainJreJarInfo();
/* 102 */     if (info != null) {
/* 103 */       File jarFile = ((JarLibraryInfo)info).getJarFile();
/* 104 */       jre = jarFile.getParentFile().getParentFile().getAbsolutePath();
/*     */     } 
/* 106 */     JTextField javaField = createTextField(jre);
/*     */     
/* 108 */     if (getComponentOrientation().isLeftToRight()) {
/* 109 */       temp.add(perlLabel); temp.add(perlField);
/* 110 */       temp.add(javaLabel); temp.add(javaField);
/*     */     } else {
/*     */       
/* 113 */       temp.add(perlField); temp.add(perlLabel);
/* 114 */       temp.add(javaField); temp.add(javaLabel);
/*     */     } 
/* 116 */     makeSpringCompactGrid(temp, 2, 2, 5, 5, 15, 5);
/* 117 */     box.add(temp);
/*     */     
/* 119 */     box.add(Box.createVerticalGlue());
/*     */     
/* 121 */     cp.add(box, "North");
/*     */     
/* 123 */     JButton okButton = new JButton("OK");
/* 124 */     okButton.addActionListener(e -> setVisible(false));
/* 125 */     temp = new JPanel(new BorderLayout());
/* 126 */     temp.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
/* 127 */     temp.add(okButton, "After");
/* 128 */     cp.add(temp, "South");
/*     */     
/* 130 */     getRootPane().setDefaultButton(okButton);
/* 131 */     setTitle("About RSTALanguageSupport Demo");
/* 132 */     setContentPane(cp);
/* 133 */     setDefaultCloseOperation(2);
/* 134 */     setModal(true);
/* 135 */     pack();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private JPanel addLeftAligned(Component toAdd, Container addTo) {
/* 141 */     JPanel temp = new JPanel(new BorderLayout());
/* 142 */     temp.setOpaque(false);
/* 143 */     temp.add(toAdd, "Before");
/* 144 */     addTo.add(temp);
/* 145 */     return temp;
/*     */   }
/*     */ 
/*     */   
/*     */   private JTextField createTextField(String text) {
/* 150 */     JTextField field = new JTextField(text);
/* 151 */     field.setEditable(false);
/* 152 */     field.setBorder((Border)null);
/* 153 */     field.setOpaque(false);
/* 154 */     return field;
/*     */   }
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
/*     */   private static SpringLayout.Constraints getConstraintsForCell(int row, int col, Container parent, int cols) {
/* 170 */     SpringLayout layout = (SpringLayout)parent.getLayout();
/* 171 */     Component c = parent.getComponent(row * cols + col);
/* 172 */     return layout.getConstraints(c);
/*     */   }
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
/*     */   private static void makeSpringCompactGrid(Container parent, int rows, int cols, int initialX, int initialY, int xPad, int yPad) {
/*     */     SpringLayout layout;
/*     */     try {
/* 201 */       layout = (SpringLayout)parent.getLayout();
/* 202 */     } catch (ClassCastException cce) {
/* 203 */       System.err.println("The first argument to makeCompactGrid must use SpringLayout.");
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 209 */     Spring x = Spring.constant(initialX);
/* 210 */     for (int c = 0; c < cols; c++) {
/* 211 */       Spring width = Spring.constant(0); int i;
/* 212 */       for (i = 0; i < rows; i++) {
/* 213 */         width = Spring.max(width, 
/* 214 */             getConstraintsForCell(i, c, parent, cols)
/* 215 */             .getWidth());
/*     */       }
/* 217 */       for (i = 0; i < rows; i++) {
/*     */         
/* 219 */         SpringLayout.Constraints constraints = getConstraintsForCell(i, c, parent, cols);
/* 220 */         constraints.setX(x);
/* 221 */         constraints.setWidth(width);
/*     */       } 
/* 223 */       x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
/*     */     } 
/*     */ 
/*     */     
/* 227 */     Spring y = Spring.constant(initialY);
/* 228 */     for (int r = 0; r < rows; r++) {
/* 229 */       Spring height = Spring.constant(0); int i;
/* 230 */       for (i = 0; i < cols; i++) {
/* 231 */         height = Spring.max(height, 
/* 232 */             getConstraintsForCell(r, i, parent, cols).getHeight());
/*     */       }
/* 234 */       for (i = 0; i < cols; i++) {
/*     */         
/* 236 */         SpringLayout.Constraints constraints = getConstraintsForCell(r, i, parent, cols);
/* 237 */         constraints.setY(y);
/* 238 */         constraints.setHeight(height);
/*     */       } 
/* 240 */       y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
/*     */     } 
/*     */ 
/*     */     
/* 244 */     SpringLayout.Constraints pCons = layout.getConstraints(parent);
/* 245 */     pCons.setConstraint("South", y);
/* 246 */     pCons.setConstraint("East", x);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class TopBorder
/*     */     extends AbstractBorder
/*     */   {
/*     */     private TopBorder() {}
/*     */ 
/*     */     
/*     */     public Insets getBorderInsets(Component c) {
/* 258 */       return getBorderInsets(c, new Insets(0, 0, 0, 0));
/*     */     }
/*     */ 
/*     */     
/*     */     public Insets getBorderInsets(Component c, Insets insets) {
/* 263 */       insets.top = insets.left = insets.right = 5;
/* 264 */       insets.bottom = 6;
/* 265 */       return insets;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/* 271 */       Color color = UIManager.getColor("controlShadow");
/* 272 */       if (color == null) {
/* 273 */         color = SystemColor.controlShadow;
/*     */       }
/* 275 */       g.setColor(color);
/* 276 */       g.drawLine(x, y + height - 1, x + width, y + height - 1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\demo\AboutDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */