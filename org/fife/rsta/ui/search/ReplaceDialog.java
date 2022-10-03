/*     */ package org.fife.rsta.ui.search;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Frame;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.Image;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.FocusAdapter;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
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
/*     */ 
/*     */ public class ReplaceDialog
/*     */   extends AbstractFindReplaceDialog
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private JButton replaceButton;
/*     */   private JButton replaceAllButton;
/*     */   private JLabel replaceFieldLabel;
/*     */   private SearchComboBox replaceWithCombo;
/*     */   private String lastSearchString;
/*     */   private String lastReplaceString;
/*     */   private Component superMainComponent;
/*     */   protected SearchListener searchListener;
/*     */   
/*     */   public ReplaceDialog(Dialog owner, SearchListener listener) {
/*  96 */     super(owner);
/*  97 */     this.superMainComponent = owner;
/*  98 */     init(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReplaceDialog(Frame owner, SearchListener listener) {
/* 109 */     super(owner);
/* 110 */     this.superMainComponent = owner;
/* 111 */     init(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/* 118 */     String command = e.getActionCommand();
/*     */     
/* 120 */     if (SearchEvent.Type.REPLACE.name().equals(command) || SearchEvent.Type.REPLACE_ALL
/* 121 */       .name().equals(command)) {
/*     */       
/* 123 */       this.context.setSearchFor(getSearchString());
/* 124 */       this.context.setReplaceWith(this.replaceWithCombo.getSelectedString());
/*     */       
/* 126 */       JTextComponent tc = UIUtil.getTextComponent((JComboBox)this.findTextCombo);
/* 127 */       this.findTextCombo.addItem(tc.getText());
/*     */       
/* 129 */       tc = UIUtil.getTextComponent((JComboBox)this.replaceWithCombo);
/* 130 */       String replaceText = tc.getText();
/* 131 */       if (replaceText.length() != 0) {
/* 132 */         this.replaceWithCombo.addItem(replaceText);
/*     */       }
/*     */ 
/*     */       
/* 136 */       fireSearchEvent(SearchEvent.Type.valueOf(command), (SearchContext)null);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 141 */       super.actionPerformed(e);
/* 142 */       if (SearchEvent.Type.FIND.name().equals(command)) {
/* 143 */         handleToggleButtons();
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void escapePressed() {
/* 162 */     if (this.replaceWithCombo.hideAutoCompletePopups()) {
/*     */       return;
/*     */     }
/* 165 */     super.escapePressed();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getReplaceButtonText() {
/* 176 */     return this.replaceButton.getText();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getReplaceAllButtonText() {
/* 187 */     return this.replaceAllButton.getText();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getReplaceString() {
/* 198 */     String text = this.replaceWithCombo.getSelectedString();
/* 199 */     if (text == null) {
/* 200 */       text = "";
/*     */     }
/* 202 */     return text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getReplaceWithLabelText() {
/* 213 */     return this.replaceFieldLabel.getText();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleRegExCheckBoxClicked() {
/* 223 */     super.handleRegExCheckBoxClicked();
/*     */     
/* 225 */     boolean b = this.regexCheckBox.isSelected();
/*     */     
/* 227 */     this.replaceWithCombo.setAutoCompleteEnabled(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleSearchContextPropertyChanged(PropertyChangeEvent e) {
/* 234 */     String prop = e.getPropertyName();
/*     */     
/* 236 */     if ("Search.replaceWith".equals(prop)) {
/* 237 */       String newValue = (String)e.getNewValue();
/* 238 */       if (newValue == null) {
/* 239 */         newValue = "";
/*     */       }
/* 241 */       String oldValue = getReplaceString();
/*     */       
/* 243 */       if (!newValue.equals(oldValue)) {
/* 244 */         setReplaceString(newValue);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 249 */       super.handleSearchContextPropertyChanged(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FindReplaceButtonsEnableResult handleToggleButtons() {
/* 258 */     FindReplaceButtonsEnableResult er = super.handleToggleButtons();
/* 259 */     boolean shouldReplace = er.getEnable();
/* 260 */     this.replaceAllButton.setEnabled(shouldReplace);
/*     */ 
/*     */ 
/*     */     
/* 264 */     if (shouldReplace) {
/* 265 */       String text = this.searchListener.getSelectedText();
/* 266 */       shouldReplace = matchesSearchFor(text);
/*     */     } 
/* 268 */     this.replaceButton.setEnabled(shouldReplace);
/*     */     
/* 270 */     return er;
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
/*     */   private void init(SearchListener listener) {
/* 282 */     this.searchListener = listener;
/*     */ 
/*     */     
/* 285 */     ComponentOrientation orientation = ComponentOrientation.getOrientation(getLocale());
/*     */ 
/*     */     
/* 288 */     JPanel searchPanel = new JPanel(new SpringLayout());
/*     */ 
/*     */     
/* 291 */     ReplaceFocusAdapter replaceFocusAdapter = new ReplaceFocusAdapter();
/* 292 */     ReplaceDocumentListener replaceDocumentListener = new ReplaceDocumentListener();
/*     */ 
/*     */     
/* 295 */     JTextComponent textField = UIUtil.getTextComponent((JComboBox)this.findTextCombo);
/* 296 */     textField.addFocusListener(replaceFocusAdapter);
/* 297 */     textField.getDocument().addDocumentListener(replaceDocumentListener);
/*     */ 
/*     */     
/* 300 */     this.replaceWithCombo = new SearchComboBox(null, true);
/* 301 */     textField = UIUtil.getTextComponent((JComboBox)this.replaceWithCombo);
/* 302 */     textField.addFocusListener(replaceFocusAdapter);
/* 303 */     textField.getDocument().addDocumentListener(replaceDocumentListener);
/*     */ 
/*     */     
/* 306 */     this.replaceFieldLabel = UIUtil.newLabel(getBundle(), "ReplaceWith", (Component)this.replaceWithCombo);
/*     */ 
/*     */     
/* 309 */     JPanel temp = new JPanel(new BorderLayout());
/* 310 */     temp.add((Component)this.findTextCombo);
/* 311 */     AssistanceIconPanel aip = new AssistanceIconPanel((JComponent)this.findTextCombo);
/* 312 */     temp.add((Component)aip, "Before");
/* 313 */     JPanel temp2 = new JPanel(new BorderLayout());
/* 314 */     temp2.add((Component)this.replaceWithCombo);
/* 315 */     AssistanceIconPanel aip2 = new AssistanceIconPanel((JComponent)this.replaceWithCombo);
/* 316 */     temp2.add((Component)aip2, "Before");
/*     */ 
/*     */     
/* 319 */     if (orientation.isLeftToRight()) {
/* 320 */       searchPanel.add(this.findFieldLabel);
/* 321 */       searchPanel.add(temp);
/* 322 */       searchPanel.add(this.replaceFieldLabel);
/* 323 */       searchPanel.add(temp2);
/*     */     } else {
/*     */       
/* 326 */       searchPanel.add(temp);
/* 327 */       searchPanel.add(this.findFieldLabel);
/* 328 */       searchPanel.add(temp2);
/* 329 */       searchPanel.add(this.replaceFieldLabel);
/*     */     } 
/*     */     
/* 332 */     UIUtil.makeSpringCompactGrid(searchPanel, 2, 2, 0, 0, 6, 6);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 338 */     JPanel bottomPanel = new JPanel(new BorderLayout());
/* 339 */     temp = new JPanel(new BorderLayout());
/* 340 */     bottomPanel.setBorder(UIUtil.getEmpty5Border());
/* 341 */     temp.add(this.searchConditionsPanel, "Before");
/* 342 */     temp.add(this.searchConditionsPanel, "Before");
/* 343 */     temp2 = new JPanel(new BorderLayout());
/* 344 */     temp2.add(this.dirPanel, "North");
/* 345 */     temp.add(temp2);
/* 346 */     bottomPanel.add(temp, "Before");
/*     */ 
/*     */     
/* 349 */     JPanel leftPanel = new JPanel();
/* 350 */     leftPanel.setLayout(new BoxLayout(leftPanel, 1));
/* 351 */     leftPanel.add(searchPanel);
/* 352 */     leftPanel.add(bottomPanel);
/*     */ 
/*     */     
/* 355 */     JPanel buttonPanel = new JPanel();
/* 356 */     buttonPanel.setLayout(new GridLayout(4, 1, 5, 5));
/* 357 */     ResourceBundle msg = getBundle();
/* 358 */     this.replaceButton = UIUtil.newButton(msg, "Replace");
/* 359 */     this.replaceButton.setActionCommand(SearchEvent.Type.REPLACE.name());
/* 360 */     this.replaceButton.addActionListener(this);
/* 361 */     this.replaceButton.setEnabled(false);
/* 362 */     this.replaceButton.setIcon((Icon)null);
/* 363 */     this.replaceButton.setToolTipText((String)null);
/* 364 */     this.replaceAllButton = UIUtil.newButton(msg, "ReplaceAll");
/* 365 */     this.replaceAllButton.setActionCommand(SearchEvent.Type.REPLACE_ALL.name());
/* 366 */     this.replaceAllButton.addActionListener(this);
/* 367 */     this.replaceAllButton.setEnabled(false);
/* 368 */     this.replaceAllButton.setIcon((Icon)null);
/* 369 */     this.replaceAllButton.setToolTipText((String)null);
/* 370 */     buttonPanel.add(this.findNextButton);
/* 371 */     buttonPanel.add(this.replaceButton);
/* 372 */     buttonPanel.add(this.replaceAllButton);
/* 373 */     buttonPanel.add(this.cancelButton);
/* 374 */     JPanel rightPanel = new JPanel(new BorderLayout());
/* 375 */     rightPanel.add(buttonPanel, "North");
/*     */ 
/*     */     
/* 378 */     JPanel contentPane = new JPanel(new BorderLayout());
/* 379 */     contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
/* 380 */     contentPane.add(leftPanel);
/* 381 */     contentPane.add(rightPanel, "After");
/* 382 */     ResizableFrameContentPane resizableFrameContentPane = new ResizableFrameContentPane(new BorderLayout());
/* 383 */     resizableFrameContentPane.add(contentPane, "North");
/* 384 */     setContentPane((Container)resizableFrameContentPane);
/* 385 */     getRootPane().setDefaultButton(this.findNextButton);
/* 386 */     setTitle(getString("ReplaceDialogTitle"));
/* 387 */     setResizable(true);
/* 388 */     pack();
/* 389 */     setLocationRelativeTo(getParent());
/*     */     
/* 391 */     setSearchContext(new SearchContext());
/* 392 */     addSearchListener(listener);
/*     */     
/* 394 */     applyComponentOrientation(orientation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContentAssistImage(Image image) {
/* 401 */     super.setContentAssistImage(image);
/* 402 */     this.replaceWithCombo.setContentAssistImage(image);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setReplaceButtonText(String text) {
/* 413 */     this.replaceButton.setText(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setReplaceAllButtonText(String text) {
/* 424 */     this.replaceAllButton.setText(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setReplaceWithLabelText(String text) {
/* 435 */     this.replaceFieldLabel.setText(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReplaceString(String newReplaceString) {
/* 446 */     this.replaceWithCombo.addItem(newReplaceString);
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
/*     */   public void setVisible(boolean visible) {
/* 459 */     setLocationRelativeTo(this.superMainComponent);
/*     */     
/* 461 */     if (visible) {
/*     */ 
/*     */       
/* 464 */       String text = this.searchListener.getSelectedText();
/* 465 */       if (text != null) {
/* 466 */         this.findTextCombo.addItem(text);
/*     */       }
/*     */       
/* 469 */       String selectedItem = this.findTextCombo.getSelectedString();
/* 470 */       if (selectedItem == null || selectedItem.length() == 0) {
/* 471 */         this.findNextButton.setEnabled(false);
/* 472 */         this.replaceButton.setEnabled(false);
/* 473 */         this.replaceAllButton.setEnabled(false);
/*     */       } else {
/*     */         
/* 476 */         handleToggleButtons();
/*     */       } 
/*     */       
/* 479 */       super.setVisible(true);
/* 480 */       focusFindTextField();
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 485 */       super.setVisible(false);
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
/*     */ 
/*     */   
/*     */   public void updateUI() {
/* 501 */     SwingUtilities.updateComponentTreeUI((Component)this);
/* 502 */     pack();
/*     */ 
/*     */     
/* 505 */     ReplaceFocusAdapter replaceFocusAdapter = new ReplaceFocusAdapter();
/* 506 */     ReplaceDocumentListener replaceDocumentListener = new ReplaceDocumentListener();
/*     */ 
/*     */     
/* 509 */     JTextComponent textField = UIUtil.getTextComponent((JComboBox)this.findTextCombo);
/* 510 */     textField.addFocusListener(replaceFocusAdapter);
/* 511 */     textField.getDocument().addDocumentListener(replaceDocumentListener);
/*     */ 
/*     */     
/* 514 */     textField = UIUtil.getTextComponent((JComboBox)this.replaceWithCombo);
/* 515 */     textField.addFocusListener(replaceFocusAdapter);
/* 516 */     textField.getDocument().addDocumentListener(replaceDocumentListener);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class ReplaceDocumentListener
/*     */     implements DocumentListener
/*     */   {
/*     */     private ReplaceDocumentListener() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void insertUpdate(DocumentEvent e) {
/* 529 */       JTextComponent findWhatTextField = UIUtil.getTextComponent((JComboBox)ReplaceDialog.this.findTextCombo);
/* 530 */       if (e.getDocument().equals(findWhatTextField.getDocument())) {
/* 531 */         ReplaceDialog.this.handleToggleButtons();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void removeUpdate(DocumentEvent e) {
/* 538 */       JTextComponent findWhatTextField = UIUtil.getTextComponent((JComboBox)ReplaceDialog.this.findTextCombo);
/* 539 */       if (e.getDocument().equals(findWhatTextField.getDocument()) && e
/* 540 */         .getDocument().getLength() == 0) {
/* 541 */         ReplaceDialog.this.findNextButton.setEnabled(false);
/* 542 */         ReplaceDialog.this.replaceButton.setEnabled(false);
/* 543 */         ReplaceDialog.this.replaceAllButton.setEnabled(false);
/*     */       } else {
/*     */         
/* 546 */         ReplaceDialog.this.handleToggleButtons();
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
/*     */   private class ReplaceFocusAdapter
/*     */     extends FocusAdapter
/*     */   {
/*     */     private ReplaceFocusAdapter() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void focusGained(FocusEvent e) {
/* 565 */       JTextComponent textField = (JTextComponent)e.getSource();
/* 566 */       textField.selectAll();
/*     */       
/* 568 */       if (textField == UIUtil.getTextComponent((JComboBox)ReplaceDialog.this.findTextCombo)) {
/*     */         
/* 570 */         ReplaceDialog.this.lastSearchString = ReplaceDialog.this.findTextCombo.getSelectedString();
/*     */       }
/*     */       else {
/*     */         
/* 574 */         ReplaceDialog.this.lastReplaceString = ReplaceDialog.this.replaceWithCombo.getSelectedString();
/*     */       } 
/*     */ 
/*     */       
/* 578 */       ReplaceDialog.this.handleToggleButtons();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rst\\ui\search\ReplaceDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */