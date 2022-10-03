/*     */ package org.fife.rsta.ui.search;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.event.FocusAdapter;
/*     */ import java.awt.event.FocusEvent;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.SpringLayout;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ui.AssistanceIconPanel;
/*     */ import org.fife.rsta.ui.ResizableFrameContentPane;
/*     */ import org.fife.rsta.ui.UIUtil;
/*     */ import org.fife.ui.rtextarea.SearchContext;
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
/*     */ public class FindDialog
/*     */   extends AbstractFindReplaceDialog
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private String lastSearchString;
/*     */   protected SearchListener searchListener;
/*     */   
/*     */   public FindDialog(Dialog owner, SearchListener listener) {
/*  82 */     super(owner);
/*  83 */     init(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FindDialog(Frame owner, SearchListener listener) {
/*  94 */     super(owner);
/*  95 */     init(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(SearchListener listener) {
/* 106 */     this.searchListener = listener;
/*     */ 
/*     */     
/* 109 */     ComponentOrientation orientation = ComponentOrientation.getOrientation(getLocale());
/*     */ 
/*     */     
/* 112 */     JPanel enterTextPane = new JPanel(new SpringLayout());
/* 113 */     enterTextPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
/* 114 */     JTextComponent textField = UIUtil.getTextComponent((JComboBox)this.findTextCombo);
/* 115 */     textField.addFocusListener(new FindFocusAdapter());
/* 116 */     textField.getDocument().addDocumentListener(new FindDocumentListener());
/* 117 */     JPanel temp = new JPanel(new BorderLayout());
/* 118 */     temp.add((Component)this.findTextCombo);
/* 119 */     AssistanceIconPanel aip = new AssistanceIconPanel((JComponent)this.findTextCombo);
/* 120 */     temp.add((Component)aip, "Before");
/* 121 */     if (orientation.isLeftToRight()) {
/* 122 */       enterTextPane.add(this.findFieldLabel);
/* 123 */       enterTextPane.add(temp);
/*     */     } else {
/*     */       
/* 126 */       enterTextPane.add(temp);
/* 127 */       enterTextPane.add(this.findFieldLabel);
/*     */     } 
/*     */     
/* 130 */     UIUtil.makeSpringCompactGrid(enterTextPane, 1, 2, 0, 0, 6, 6);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 136 */     JPanel bottomPanel = new JPanel(new BorderLayout());
/* 137 */     temp = new JPanel(new BorderLayout());
/* 138 */     bottomPanel.setBorder(UIUtil.getEmpty5Border());
/* 139 */     temp.add(this.searchConditionsPanel, "Before");
/* 140 */     JPanel temp2 = new JPanel(new BorderLayout());
/* 141 */     temp2.add(this.dirPanel, "North");
/* 142 */     temp.add(temp2);
/* 143 */     bottomPanel.add(temp, "Before");
/*     */ 
/*     */     
/* 146 */     JPanel leftPanel = new JPanel();
/* 147 */     leftPanel.setLayout(new BoxLayout(leftPanel, 1));
/* 148 */     leftPanel.add(enterTextPane);
/* 149 */     leftPanel.add(bottomPanel);
/*     */ 
/*     */     
/* 152 */     JPanel buttonPanel = new JPanel();
/* 153 */     buttonPanel.setLayout(new GridLayout(2, 1, 5, 5));
/* 154 */     buttonPanel.add(this.findNextButton);
/* 155 */     buttonPanel.add(this.cancelButton);
/* 156 */     JPanel rightPanel = new JPanel();
/* 157 */     rightPanel.setLayout(new BorderLayout());
/* 158 */     rightPanel.add(buttonPanel, "North");
/*     */ 
/*     */     
/* 161 */     JPanel contentPane = new JPanel(new BorderLayout());
/* 162 */     if (orientation.isLeftToRight()) {
/* 163 */       contentPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 5));
/*     */     } else {
/*     */       
/* 166 */       contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
/*     */     } 
/* 168 */     contentPane.add(leftPanel);
/* 169 */     contentPane.add(rightPanel, "After");
/* 170 */     ResizableFrameContentPane resizableFrameContentPane = new ResizableFrameContentPane(new BorderLayout());
/* 171 */     resizableFrameContentPane.add(contentPane, "North");
/* 172 */     setContentPane((Container)resizableFrameContentPane);
/* 173 */     getRootPane().setDefaultButton(this.findNextButton);
/* 174 */     setTitle(getString("FindDialogTitle"));
/* 175 */     setResizable(true);
/* 176 */     pack();
/* 177 */     setLocationRelativeTo(getParent());
/*     */     
/* 179 */     setSearchContext(new SearchContext());
/* 180 */     addSearchListener(listener);
/*     */     
/* 182 */     applyComponentOrientation(orientation);
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
/*     */   public void setVisible(boolean visible) {
/* 196 */     if (visible) {
/*     */ 
/*     */       
/* 199 */       String text = this.searchListener.getSelectedText();
/* 200 */       if (text != null) {
/* 201 */         this.findTextCombo.addItem(text);
/*     */       }
/*     */       
/* 204 */       String selectedItem = this.findTextCombo.getSelectedString();
/* 205 */       boolean nonEmpty = (selectedItem != null && selectedItem.length() > 0);
/* 206 */       this.findNextButton.setEnabled(nonEmpty);
/* 207 */       super.setVisible(true);
/* 208 */       focusFindTextField();
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 213 */       super.setVisible(false);
/*     */     } 
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
/*     */   public void updateUI() {
/* 227 */     SwingUtilities.updateComponentTreeUI((Component)this);
/* 228 */     pack();
/* 229 */     JTextComponent textField = UIUtil.getTextComponent((JComboBox)this.findTextCombo);
/* 230 */     textField.addFocusListener(new FindFocusAdapter());
/* 231 */     textField.getDocument().addDocumentListener(new FindDocumentListener());
/*     */   }
/*     */ 
/*     */   
/*     */   private class FindDocumentListener
/*     */     implements DocumentListener
/*     */   {
/*     */     private FindDocumentListener() {}
/*     */ 
/*     */     
/*     */     public void insertUpdate(DocumentEvent e) {
/* 242 */       FindDialog.this.handleToggleButtons();
/*     */     }
/*     */ 
/*     */     
/*     */     public void removeUpdate(DocumentEvent e) {
/* 247 */       JTextComponent comp = UIUtil.getTextComponent((JComboBox)FindDialog.this.findTextCombo);
/* 248 */       if (comp.getDocument().getLength() == 0) {
/* 249 */         FindDialog.this.findNextButton.setEnabled(false);
/*     */       } else {
/*     */         
/* 252 */         FindDialog.this.handleToggleButtons();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void changedUpdate(DocumentEvent e) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class FindFocusAdapter
/*     */     extends FocusAdapter
/*     */   {
/*     */     private FindFocusAdapter() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void focusGained(FocusEvent e) {
/* 271 */       UIUtil.getTextComponent((JComboBox)FindDialog.this.findTextCombo).selectAll();
/*     */       
/* 273 */       FindDialog.this.lastSearchString = (String)FindDialog.this.findTextCombo.getSelectedItem();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rst\\ui\search\FindDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */