/*     */ package org.fife.rsta.ui.search;
/*     */ 
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Frame;
/*     */ import java.awt.Image;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ui.EscapableDialog;
/*     */ import org.fife.rsta.ui.UIUtil;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
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
/*     */ public class AbstractSearchDialog
/*     */   extends EscapableDialog
/*     */   implements ActionListener
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected SearchContext context;
/*     */   private SearchContextListener contextListener;
/*     */   protected JCheckBox caseCheckBox;
/*     */   protected JCheckBox wholeWordCheckBox;
/*     */   protected JCheckBox regexCheckBox;
/*     */   protected JCheckBox wrapCheckBox;
/*     */   protected JPanel searchConditionsPanel;
/*     */   private static Image contentAssistImage;
/*     */   protected SearchComboBox findTextCombo;
/*     */   protected JButton cancelButton;
/*  79 */   private static final ResourceBundle MSG = ResourceBundle.getBundle("org.fife.rsta.ui.search.Search");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractSearchDialog(Dialog owner) {
/*  89 */     super(owner);
/*  90 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractSearchDialog(Frame owner) {
/* 101 */     super(owner);
/* 102 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent e) {
/*     */     boolean matchCase, wholeWord, useRegEx, wrap;
/* 112 */     String command = e.getActionCommand();
/*     */     
/* 114 */     switch (command) {
/*     */ 
/*     */       
/*     */       case "FlipMatchCase":
/* 118 */         matchCase = this.caseCheckBox.isSelected();
/* 119 */         this.context.setMatchCase(matchCase);
/*     */         return;
/*     */ 
/*     */       
/*     */       case "FlipWholeWord":
/* 124 */         wholeWord = this.wholeWordCheckBox.isSelected();
/* 125 */         this.context.setWholeWord(wholeWord);
/*     */         return;
/*     */ 
/*     */       
/*     */       case "FlipRegEx":
/* 130 */         useRegEx = this.regexCheckBox.isSelected();
/* 131 */         this.context.setRegularExpression(useRegEx);
/*     */         return;
/*     */ 
/*     */       
/*     */       case "FlipWrap":
/* 136 */         wrap = this.wrapCheckBox.isSelected();
/* 137 */         this.context.setSearchWrap(wrap);
/*     */         return;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 143 */     setVisible(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JCheckBox createCheckBox(ResourceBundle msg, String keyRoot) {
/* 150 */     JCheckBox cb = new JCheckBox(msg.getString(keyRoot));
/* 151 */     cb.setMnemonic(msg.getString(keyRoot + "Mnemonic").charAt(0));
/* 152 */     cb.setActionCommand("Flip" + keyRoot);
/* 153 */     cb.addActionListener(this);
/* 154 */     return cb;
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
/*     */   protected SearchContext createDefaultSearchContext() {
/* 166 */     return new SearchContext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Border createTitledBorder(String title) {
/* 177 */     if (title != null && title.charAt(title.length() - 1) != ':') {
/* 178 */       title = title + ":";
/*     */     }
/* 180 */     return BorderFactory.createTitledBorder(title);
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
/*     */   protected void escapePressed() {
/* 196 */     if (this.findTextCombo.hideAutoCompletePopups()) {
/*     */       return;
/*     */     }
/* 199 */     super.escapePressed();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void focusFindTextField() {
/* 207 */     JTextComponent textField = UIUtil.getTextComponent((JComboBox)this.findTextCombo);
/* 208 */     textField.requestFocusInWindow();
/* 209 */     textField.selectAll();
/*     */   }
/*     */ 
/*     */   
/*     */   protected ResourceBundle getBundle() {
/* 214 */     return MSG;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getCancelButtonText() {
/* 225 */     return this.cancelButton.getText();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Image getContentAssistImage() {
/* 236 */     if (contentAssistImage == null) {
/* 237 */       URL url = AbstractSearchDialog.class.getResource("lightbulb.png");
/*     */       try {
/* 239 */         contentAssistImage = ImageIO.read(url);
/* 240 */       } catch (IOException ioe) {
/* 241 */         ioe.printStackTrace();
/*     */       } 
/*     */     } 
/* 244 */     return contentAssistImage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getMatchCaseCheckboxText() {
/* 255 */     return this.caseCheckBox.getText();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getRegularExpressionCheckboxText() {
/* 266 */     return this.regexCheckBox.getText();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SearchContext getSearchContext() {
/* 277 */     return this.context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSearchString() {
/* 287 */     return this.findTextCombo.getSelectedString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getString(String key) {
/* 292 */     return MSG.getString(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getWholeWordCheckboxText() {
/* 303 */     return this.wholeWordCheckBox.getText();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getWrapCheckboxText() {
/* 313 */     return this.wrapCheckBox.getText();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleRegExCheckBoxClicked() {
/* 323 */     handleToggleButtons();
/*     */     
/* 325 */     boolean b = this.regexCheckBox.isSelected();
/* 326 */     this.findTextCombo.setAutoCompleteEnabled(b);
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
/*     */   protected void handleSearchContextPropertyChanged(PropertyChangeEvent e) {
/* 339 */     String prop = e.getPropertyName();
/*     */     
/* 341 */     if ("Search.MatchCase".equals(prop)) {
/* 342 */       boolean newValue = ((Boolean)e.getNewValue()).booleanValue();
/* 343 */       this.caseCheckBox.setSelected(newValue);
/*     */     }
/* 345 */     else if ("Search.MatchWholeWord".equals(prop)) {
/* 346 */       boolean newValue = ((Boolean)e.getNewValue()).booleanValue();
/* 347 */       this.wholeWordCheckBox.setSelected(newValue);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 357 */     else if ("Search.UseRegex".equals(prop)) {
/* 358 */       boolean newValue = ((Boolean)e.getNewValue()).booleanValue();
/* 359 */       this.regexCheckBox.setSelected(newValue);
/* 360 */       handleRegExCheckBoxClicked();
/*     */     }
/* 362 */     else if ("Search.searchFor".equals(prop)) {
/* 363 */       String newValue = (String)e.getNewValue();
/* 364 */       String oldValue = getSearchString();
/*     */       
/* 366 */       if (!newValue.equals(oldValue)) {
/* 367 */         setSearchString(newValue);
/*     */       }
/*     */     }
/* 370 */     else if ("Search.Wrap".equals(prop)) {
/* 371 */       boolean newValue = ((Boolean)e.getNewValue()).booleanValue();
/* 372 */       this.wrapCheckBox.setSelected(newValue);
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
/*     */   protected FindReplaceButtonsEnableResult handleToggleButtons() {
/* 389 */     JTextComponent tc = UIUtil.getTextComponent((JComboBox)this.findTextCombo);
/* 390 */     String text = tc.getText();
/* 391 */     if (text.length() == 0) {
/* 392 */       return new FindReplaceButtonsEnableResult(false, null);
/*     */     }
/* 394 */     if (this.regexCheckBox.isSelected()) {
/*     */       try {
/* 396 */         Pattern.compile(text);
/* 397 */       } catch (PatternSyntaxException pse) {
/* 398 */         return new FindReplaceButtonsEnableResult(false, pse.getMessage());
/*     */       } 
/*     */     }
/* 401 */     return new FindReplaceButtonsEnableResult(true, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init() {
/* 409 */     this.contextListener = new SearchContextListener();
/* 410 */     setSearchContext(createDefaultSearchContext());
/*     */ 
/*     */     
/* 413 */     this.searchConditionsPanel = new JPanel();
/* 414 */     this.searchConditionsPanel.setLayout(new BoxLayout(this.searchConditionsPanel, 1));
/*     */     
/* 416 */     this.caseCheckBox = createCheckBox(MSG, "MatchCase");
/* 417 */     this.searchConditionsPanel.add(this.caseCheckBox);
/* 418 */     this.wholeWordCheckBox = createCheckBox(MSG, "WholeWord");
/* 419 */     this.searchConditionsPanel.add(this.wholeWordCheckBox);
/* 420 */     this.regexCheckBox = createCheckBox(MSG, "RegEx");
/* 421 */     this.searchConditionsPanel.add(this.regexCheckBox);
/*     */     
/* 423 */     this.wrapCheckBox = createCheckBox(MSG, "Wrap");
/* 424 */     this.searchConditionsPanel.add(this.wrapCheckBox);
/*     */ 
/*     */     
/* 427 */     this.findTextCombo = new SearchComboBox(null, false);
/*     */ 
/*     */     
/* 430 */     this.cancelButton = new JButton(getString("Cancel"));
/*     */     
/* 432 */     this.cancelButton.setActionCommand("Cancel");
/* 433 */     this.cancelButton.addActionListener(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean matchesSearchFor(String text) {
/* 439 */     if (text == null || text.length() == 0) {
/* 440 */       return false;
/*     */     }
/* 442 */     String searchFor = this.findTextCombo.getSelectedString();
/* 443 */     if (searchFor != null && searchFor.length() > 0) {
/* 444 */       boolean matchCase = this.caseCheckBox.isSelected();
/* 445 */       if (this.regexCheckBox.isSelected()) {
/* 446 */         Pattern pattern; int flags = 8;
/* 447 */         flags = RSyntaxUtilities.getPatternFlags(matchCase, flags);
/*     */         
/*     */         try {
/* 450 */           pattern = Pattern.compile(searchFor, flags);
/* 451 */         } catch (PatternSyntaxException pse) {
/* 452 */           pse.printStackTrace();
/* 453 */           return false;
/*     */         } 
/* 455 */         return pattern.matcher(text).matches();
/*     */       } 
/*     */       
/* 458 */       if (matchCase) {
/* 459 */         return searchFor.equals(text);
/*     */       }
/* 461 */       return searchFor.equalsIgnoreCase(text);
/*     */     } 
/*     */     
/* 464 */     return false;
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
/*     */   @Deprecated
/*     */   protected static boolean isPreJava6JRE() {
/* 485 */     String version = System.getProperty("java.specification.version");
/* 486 */     return (version.startsWith("1.5") || version.startsWith("1.4"));
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
/*     */   public static boolean isWholeWord(CharSequence searchIn, int offset, int len) {
/*     */     boolean wsBefore;
/*     */     boolean wsAfter;
/*     */     try {
/* 507 */       wsBefore = Character.isWhitespace(searchIn.charAt(offset - 1));
/* 508 */     } catch (IndexOutOfBoundsException e) {
/* 509 */       wsBefore = true;
/*     */     } 
/*     */     try {
/* 512 */       wsAfter = Character.isWhitespace(searchIn.charAt(offset + len));
/* 513 */     } catch (IndexOutOfBoundsException e) {
/* 514 */       wsAfter = true;
/*     */     } 
/*     */     
/* 517 */     return (wsBefore && wsAfter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void refreshUIFromContext() {
/* 528 */     if (this.caseCheckBox == null) {
/*     */       return;
/*     */     }
/* 531 */     this.caseCheckBox.setSelected(this.context.getMatchCase());
/* 532 */     this.regexCheckBox.setSelected(this.context.isRegularExpression());
/* 533 */     this.wholeWordCheckBox.setSelected(this.context.getWholeWord());
/* 534 */     this.wrapCheckBox.setSelected(this.context.getSearchWrap());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void requestFocus() {
/* 543 */     super.requestFocus();
/* 544 */     focusFindTextField();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setCancelButtonText(String text) {
/* 555 */     this.cancelButton.setText(text);
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
/*     */   public void setContentAssistImage(Image image) {
/* 568 */     this.findTextCombo.setContentAssistImage(image);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setMatchCaseCheckboxText(String text) {
/* 579 */     this.caseCheckBox.setText(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setRegularExpressionCheckboxText(String text) {
/* 590 */     this.regexCheckBox.setText(text);
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
/*     */   public void setSearchContext(SearchContext context) {
/* 604 */     if (this.context != null) {
/* 605 */       this.context.removePropertyChangeListener(this.contextListener);
/*     */     }
/* 607 */     this.context = context;
/* 608 */     this.context.addPropertyChangeListener(this.contextListener);
/* 609 */     refreshUIFromContext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSearchString(String newSearchString) {
/* 620 */     this.findTextCombo.addItem(newSearchString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setWholeWordCheckboxText(String text) {
/* 631 */     this.wholeWordCheckBox.setText(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setWrapCheckboxText(String text) {
/* 641 */     this.wrapCheckBox.setText(text);
/*     */   }
/*     */ 
/*     */   
/*     */   private class SearchContextListener
/*     */     implements PropertyChangeListener
/*     */   {
/*     */     private SearchContextListener() {}
/*     */     
/*     */     public void propertyChange(PropertyChangeEvent e) {
/* 651 */       AbstractSearchDialog.this.handleSearchContextPropertyChanged(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rst\\ui\search\AbstractSearchDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */