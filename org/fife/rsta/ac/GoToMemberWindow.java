/*     */ package org.fife.rsta.ac;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.ComponentListener;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.event.WindowFocusListener;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.JWindow;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.EmptyBorder;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
/*     */ import javax.swing.plaf.basic.BasicTextFieldUI;
/*     */ import javax.swing.tree.DefaultTreeCellRenderer;
/*     */ import org.fife.ui.rsyntaxtextarea.PopupWindowDecorator;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rsyntaxtextarea.focusabletip.TipUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GoToMemberWindow
/*     */   extends JWindow
/*     */ {
/*     */   private RSyntaxTextArea textArea;
/*     */   private JTextField field;
/*     */   private AbstractSourceTree tree;
/*     */   private Listener listener;
/*     */   
/*     */   public GoToMemberWindow(Window parent, RSyntaxTextArea textArea, AbstractSourceTree tree) {
/*  80 */     super(parent);
/*  81 */     this.textArea = textArea;
/*  82 */     ComponentOrientation o = parent.getComponentOrientation();
/*  83 */     JPanel contentPane = new JPanel(new BorderLayout());
/*  84 */     contentPane.setBorder(TipUtil.getToolTipBorder());
/*     */     
/*  86 */     this.listener = new Listener();
/*  87 */     addWindowFocusListener(this.listener);
/*  88 */     parent.addComponentListener(this.listener);
/*     */     
/*  90 */     this.field = createTextField();
/*  91 */     contentPane.add(this.field, "North");
/*     */     
/*  93 */     this.tree = tree;
/*  94 */     tweakTreeBorder(this.tree);
/*  95 */     tree.setSorted(true);
/*  96 */     tree.setShowMajorElementsOnly(true);
/*  97 */     tree.setGotoSelectedElementOnClick(false);
/*  98 */     tree.setFocusable(false);
/*  99 */     tree.listenTo(textArea);
/* 100 */     tree.addMouseListener(this.listener);
/* 101 */     JScrollPane sp = new JScrollPane(tree);
/* 102 */     sp.setBorder((Border)null);
/* 103 */     sp.setViewportBorder(BorderFactory.createEmptyBorder());
/* 104 */     contentPane.add(sp);
/*     */     
/* 106 */     Color bg = TipUtil.getToolTipBackground();
/* 107 */     setBackground(bg);
/* 108 */     this.field.setBackground(bg);
/* 109 */     tree.setBackground(bg);
/* 110 */     ((DefaultTreeCellRenderer)tree.getCellRenderer()).setBackgroundNonSelectionColor(bg);
/*     */ 
/*     */     
/* 113 */     setContentPane(contentPane);
/* 114 */     PopupWindowDecorator decorator = PopupWindowDecorator.get();
/*     */     
/* 116 */     if (decorator != null) {
/* 117 */       decorator.decorate(this);
/*     */     }
/*     */     
/* 120 */     applyComponentOrientation(o);
/* 121 */     pack();
/* 122 */     JRootPane pane = getRootPane();
/* 123 */     InputMap im = pane.getInputMap(1);
/* 124 */     im.put(KeyStroke.getKeyStroke(27, 0), "EscapePressed");
/* 125 */     ActionMap am = pane.getActionMap();
/* 126 */     am.put("EscapePressed", new AbstractAction()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 129 */             GoToMemberWindow.this.dispose();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JTextField createTextField() {
/* 142 */     JTextField field = new JTextField(30);
/* 143 */     field.setUI(new BasicTextFieldUI());
/* 144 */     field.setBorder(new TextFieldBorder());
/* 145 */     field.addActionListener(this.listener);
/* 146 */     field.addKeyListener(this.listener);
/* 147 */     field.getDocument().addDocumentListener(this.listener);
/* 148 */     return field;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 154 */     this.listener.uninstall();
/* 155 */     super.dispose();
/*     */ 
/*     */ 
/*     */     
/* 159 */     this.textArea.requestFocusInWindow();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void tweakTreeBorder(AbstractSourceTree tree) {
/* 170 */     Border treeBorder = tree.getBorder();
/* 171 */     if (treeBorder == null) {
/* 172 */       treeBorder = BorderFactory.createEmptyBorder(2, 0, 0, 0);
/* 173 */       tree.setBorder(treeBorder);
/*     */     }
/* 175 */     else if (treeBorder instanceof EmptyBorder && 
/* 176 */       (((EmptyBorder)treeBorder).getBorderInsets()).top == 0) {
/* 177 */       treeBorder = BorderFactory.createCompoundBorder(
/* 178 */           BorderFactory.createEmptyBorder(2, 0, 0, 0), treeBorder);
/* 179 */       tree.setBorder(treeBorder);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private class Listener
/*     */     extends MouseAdapter
/*     */     implements WindowFocusListener, ComponentListener, DocumentListener, ActionListener, KeyListener
/*     */   {
/*     */     private Listener() {}
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 192 */       if (GoToMemberWindow.this.tree.gotoSelectedElement()) {
/* 193 */         GoToMemberWindow.this.dispose();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void changedUpdate(DocumentEvent e) {
/* 199 */       handleDocumentEvent(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void componentHidden(ComponentEvent e) {
/* 204 */       GoToMemberWindow.this.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public void componentMoved(ComponentEvent e) {
/* 209 */       GoToMemberWindow.this.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public void componentResized(ComponentEvent e) {
/* 214 */       GoToMemberWindow.this.dispose();
/*     */     }
/*     */ 
/*     */     
/*     */     public void componentShown(ComponentEvent e) {}
/*     */ 
/*     */     
/*     */     private void handleDocumentEvent(DocumentEvent e) {
/* 222 */       GoToMemberWindow.this.tree.filter(GoToMemberWindow.this.field.getText());
/* 223 */       GoToMemberWindow.this.tree.selectFirstNodeMatchingFilter();
/*     */     }
/*     */ 
/*     */     
/*     */     public void insertUpdate(DocumentEvent e) {
/* 228 */       handleDocumentEvent(e);
/*     */     }
/*     */ 
/*     */     
/*     */     public void keyPressed(KeyEvent e) {
/* 233 */       switch (e.getKeyCode()) {
/*     */         case 40:
/* 235 */           GoToMemberWindow.this.tree.selectNextVisibleRow();
/*     */           break;
/*     */         case 38:
/* 238 */           GoToMemberWindow.this.tree.selectPreviousVisibleRow();
/*     */           break;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void keyReleased(KeyEvent e) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void keyTyped(KeyEvent e) {}
/*     */ 
/*     */     
/*     */     public void mouseClicked(MouseEvent e) {
/* 253 */       if (e.getClickCount() == 2) {
/* 254 */         GoToMemberWindow.this.tree.gotoSelectedElement();
/* 255 */         GoToMemberWindow.this.dispose();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void removeUpdate(DocumentEvent e) {
/* 261 */       handleDocumentEvent(e);
/*     */     }
/*     */     
/*     */     public void uninstall() {
/* 265 */       GoToMemberWindow.this.field.removeActionListener(this);
/* 266 */       GoToMemberWindow.this.field.getDocument().removeDocumentListener(this);
/* 267 */       GoToMemberWindow.this.tree.removeMouseListener(this);
/* 268 */       GoToMemberWindow.this.removeWindowFocusListener(this);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void windowGainedFocus(WindowEvent e) {}
/*     */ 
/*     */     
/*     */     public void windowLostFocus(WindowEvent e) {
/* 277 */       GoToMemberWindow.this.dispose();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class TextFieldBorder
/*     */     implements Border
/*     */   {
/*     */     private TextFieldBorder() {}
/*     */ 
/*     */     
/*     */     public Insets getBorderInsets(Component c) {
/* 290 */       return new Insets(2, 5, 3, 5);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isBorderOpaque() {
/* 295 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
/* 301 */       g.setColor(UIManager.getColor("controlDkShadow"));
/* 302 */       g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\GoToMemberWindow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */