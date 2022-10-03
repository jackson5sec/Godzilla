/*     */ package org.fife.rsta.ui;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
/*     */ import javax.swing.text.AbstractDocument;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.DocumentFilter;
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
/*     */ 
/*     */ 
/*     */ public class GoToDialog
/*     */   extends EscapableDialog
/*     */ {
/*     */   private JButton okButton;
/*     */   private JButton cancelButton;
/*     */   private JTextField lineNumberField;
/*     */   private int maxLineNumberAllowed;
/*     */   private int lineNumber;
/*     */   private String errorDialogTitle;
/*  70 */   private static final ResourceBundle MSG = ResourceBundle.getBundle("org.fife.rsta.ui.GoToDialog");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GoToDialog(Dialog owner) {
/*  79 */     super(owner);
/*  80 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GoToDialog(Frame owner) {
/*  90 */     super(owner);
/*  91 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init() {
/*  98 */     ComponentOrientation orientation = ComponentOrientation.getOrientation(getLocale());
/*     */     
/* 100 */     this.lineNumber = -1;
/* 101 */     this.maxLineNumberAllowed = 1;
/* 102 */     Listener l = new Listener();
/*     */ 
/*     */     
/* 105 */     JPanel contentPane = new ResizableFrameContentPane(new BorderLayout());
/* 106 */     contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
/* 107 */     setContentPane(contentPane);
/*     */ 
/*     */     
/* 110 */     Box enterLineNumberPane = new Box(2);
/* 111 */     enterLineNumberPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
/*     */     
/* 113 */     this.lineNumberField = new JTextField(16);
/* 114 */     this.lineNumberField.setText("1");
/* 115 */     AbstractDocument doc = (AbstractDocument)this.lineNumberField.getDocument();
/* 116 */     doc.addDocumentListener(l);
/* 117 */     doc.setDocumentFilter(new NumberDocumentFilter());
/* 118 */     JLabel label = UIUtil.newLabel(MSG, "LineNumber", this.lineNumberField);
/* 119 */     enterLineNumberPane.add(label);
/* 120 */     enterLineNumberPane.add(Box.createHorizontalStrut(15));
/* 121 */     enterLineNumberPane.add(this.lineNumberField);
/*     */ 
/*     */     
/* 124 */     this.okButton = UIUtil.newButton(MSG, "OK");
/* 125 */     this.okButton.addActionListener(l);
/* 126 */     this.cancelButton = UIUtil.newButton(MSG, "Cancel");
/* 127 */     this.cancelButton.addActionListener(l);
/* 128 */     Container bottomPanel = createButtonPanel(this.okButton, this.cancelButton);
/*     */ 
/*     */     
/* 131 */     contentPane.add(enterLineNumberPane, "North");
/* 132 */     contentPane.add(bottomPanel, "South");
/* 133 */     JRootPane rootPane = getRootPane();
/* 134 */     rootPane.setDefaultButton(this.okButton);
/* 135 */     setTitle(MSG.getString("GotoDialogTitle"));
/* 136 */     setModal(true);
/* 137 */     applyComponentOrientation(orientation);
/* 138 */     pack();
/* 139 */     setLocationRelativeTo(getParent());
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
/*     */   private boolean attemptToGetGoToLine() {
/*     */     try {
/* 156 */       this.lineNumber = Integer.parseInt(this.lineNumberField.getText());
/*     */       
/* 158 */       if (this.lineNumber < 1 || this.lineNumber > this.maxLineNumberAllowed) {
/* 159 */         this.lineNumber = -1;
/* 160 */         throw new NumberFormatException();
/*     */       }
/*     */     
/* 163 */     } catch (NumberFormatException nfe) {
/* 164 */       displayInvalidLineNumberMessage();
/* 165 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 169 */     setVisible(false);
/* 170 */     return true;
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
/*     */   protected Container createButtonPanel(JButton ok, JButton cancel) {
/* 186 */     JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
/* 187 */     buttonPanel.add(ok);
/* 188 */     buttonPanel.add(cancel);
/* 189 */     JPanel bottomPanel = new JPanel(new BorderLayout());
/* 190 */     bottomPanel.add(buttonPanel, "After");
/* 191 */     return bottomPanel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void displayInvalidLineNumberMessage() {
/* 202 */     JOptionPane.showMessageDialog(this, MSG
/* 203 */         .getString("LineNumberRange") + this.maxLineNumberAllowed + ".", 
/* 204 */         getErrorDialogTitle(), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void escapePressed() {
/* 215 */     this.lineNumber = -1;
/* 216 */     super.escapePressed();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getErrorDialogTitle() {
/* 227 */     String title = this.errorDialogTitle;
/* 228 */     if (title == null) {
/* 229 */       title = MSG.getString("ErrorDialog.Title");
/*     */     }
/* 231 */     return title;
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
/*     */   public int getLineNumber() {
/* 243 */     return this.lineNumber;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxLineNumberAllowed() {
/* 254 */     return this.maxLineNumberAllowed;
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
/*     */   public void setErrorDialogTitle(String title) {
/* 266 */     this.errorDialogTitle = title;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxLineNumberAllowed(int max) {
/* 277 */     this.maxLineNumberAllowed = max;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVisible(boolean visible) {
/* 287 */     if (visible) {
/* 288 */       this.lineNumber = -1;
/* 289 */       this.okButton.setEnabled((this.lineNumberField.getDocument().getLength() > 0));
/* 290 */       SwingUtilities.invokeLater(() -> {
/*     */             this.lineNumberField.requestFocusInWindow();
/*     */             this.lineNumberField.selectAll();
/*     */           });
/*     */     } 
/* 295 */     super.setVisible(visible);
/*     */   }
/*     */ 
/*     */   
/*     */   private class Listener
/*     */     implements ActionListener, DocumentListener
/*     */   {
/*     */     private Listener() {}
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 306 */       Object source = e.getSource();
/* 307 */       if (GoToDialog.this.okButton == source) {
/* 308 */         GoToDialog.this.attemptToGetGoToLine();
/*     */       }
/* 310 */       else if (GoToDialog.this.cancelButton == source) {
/* 311 */         GoToDialog.this.escapePressed();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void changedUpdate(DocumentEvent e) {}
/*     */ 
/*     */     
/*     */     public void insertUpdate(DocumentEvent e) {
/* 321 */       GoToDialog.this.okButton.setEnabled((GoToDialog.this.lineNumberField.getDocument().getLength() > 0));
/*     */     }
/*     */ 
/*     */     
/*     */     public void removeUpdate(DocumentEvent e) {
/* 326 */       GoToDialog.this.okButton.setEnabled((GoToDialog.this.lineNumberField.getDocument().getLength() > 0));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class NumberDocumentFilter
/*     */     extends DocumentFilter
/*     */   {
/*     */     private NumberDocumentFilter() {}
/*     */ 
/*     */     
/*     */     private String fix(String str) {
/* 338 */       if (str != null) {
/* 339 */         int origLength = str.length();
/* 340 */         for (int i = 0; i < str.length(); i++) {
/* 341 */           if (!Character.isDigit(str.charAt(i))) {
/* 342 */             str = str.substring(0, i) + str.substring(i + 1);
/* 343 */             i--;
/*     */           } 
/*     */         } 
/* 346 */         if (origLength != str.length()) {
/* 347 */           UIManager.getLookAndFeel().provideErrorFeedback(GoToDialog.this);
/*     */         }
/*     */       } 
/*     */       
/* 351 */       return str;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
/* 357 */       fb.insertString(offset, fix(string), attr);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attr) throws BadLocationException {
/* 364 */       fb.replace(offset, length, fix(text), attr);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rst\\ui\GoToDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */