/*     */ package org.fife.rsta.ui.search;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.GridLayout;
/*     */ import java.awt.Image;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.SpringLayout;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ui.UIUtil;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReplaceToolBar
/*     */   extends FindToolBar
/*     */ {
/*     */   private JButton replaceButton;
/*     */   private JButton replaceAllButton;
/*     */   protected SearchListener searchListener;
/*     */   
/*     */   public ReplaceToolBar(SearchListener listener) {
/*  55 */     super(listener);
/*  56 */     this.searchListener = listener;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNotify() {
/*  62 */     super.addNotify();
/*  63 */     handleToggleButtons();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Container createButtonPanel() {
/*  70 */     Box panel = new Box(2);
/*     */     
/*  72 */     JPanel bp = new JPanel(new GridLayout(2, 2, 5, 5));
/*  73 */     panel.add(bp);
/*     */     
/*  75 */     createFindButtons();
/*     */     
/*  77 */     Component filler = Box.createRigidArea(new Dimension(5, 5));
/*     */     
/*  79 */     bp.add(this.findButton); bp.add(this.replaceButton);
/*  80 */     bp.add(this.replaceAllButton); bp.add(filler);
/*  81 */     panel.add(bp);
/*     */     
/*  83 */     JPanel optionPanel = new JPanel(new SpringLayout());
/*  84 */     this.matchCaseCheckBox = createCB("MatchCase");
/*  85 */     this.regexCheckBox = createCB("RegEx");
/*  86 */     this.wholeWordCheckBox = createCB("WholeWord");
/*  87 */     this.markAllCheckBox = createCB("MarkAll");
/*  88 */     this.wrapCheckBox = createCB("Wrap");
/*     */ 
/*     */ 
/*     */     
/*  92 */     Dimension spacing = new Dimension(1, 5);
/*  93 */     Component space1 = Box.createRigidArea(spacing);
/*  94 */     Component space2 = Box.createRigidArea(spacing);
/*  95 */     Component space3 = Box.createRigidArea(spacing);
/*  96 */     Component space4 = Box.createRigidArea(spacing);
/*     */ 
/*     */     
/*  99 */     ComponentOrientation orientation = ComponentOrientation.getOrientation(getLocale());
/*     */     
/* 101 */     if (orientation.isLeftToRight()) {
/* 102 */       optionPanel.add(this.matchCaseCheckBox); optionPanel.add(this.wholeWordCheckBox); optionPanel.add(this.wrapCheckBox);
/* 103 */       optionPanel.add(space1); optionPanel.add(space2); optionPanel.add(space3);
/* 104 */       optionPanel.add(this.regexCheckBox); optionPanel.add(this.markAllCheckBox); optionPanel.add(space4);
/*     */     } else {
/*     */       
/* 107 */       optionPanel.add(this.wrapCheckBox); optionPanel.add(this.wholeWordCheckBox); optionPanel.add(this.matchCaseCheckBox);
/* 108 */       optionPanel.add(space3); optionPanel.add(space2); optionPanel.add(space1);
/* 109 */       optionPanel.add(space4); optionPanel.add(this.markAllCheckBox); optionPanel.add(this.regexCheckBox);
/*     */     } 
/* 111 */     UIUtil.makeSpringCompactGrid(optionPanel, 3, 3, 0, 0, 0, 0);
/* 112 */     panel.add(optionPanel);
/*     */     
/* 114 */     return panel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Container createFieldPanel() {
/* 122 */     this.findFieldListener = new ReplaceFindFieldListener();
/*     */     
/* 124 */     JPanel temp = new JPanel(new SpringLayout());
/*     */     
/* 126 */     JLabel findLabel = new JLabel(MSG.getString("FindWhat"));
/* 127 */     JLabel replaceLabel = new JLabel(MSG.getString("ReplaceWith"));
/*     */     
/* 129 */     this.findCombo = new SearchComboBox(this, false);
/* 130 */     JTextComponent findField = UIUtil.getTextComponent((JComboBox)this.findCombo);
/* 131 */     this.findFieldListener.install(findField);
/* 132 */     Container fcp = createContentAssistablePanel((JComponent)this.findCombo);
/*     */     
/* 134 */     this.replaceCombo = new SearchComboBox(this, true);
/* 135 */     JTextComponent replaceField = UIUtil.getTextComponent((JComboBox)this.replaceCombo);
/* 136 */     this.findFieldListener.install(replaceField);
/* 137 */     Container rcp = createContentAssistablePanel((JComponent)this.replaceCombo);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     Dimension spacing = new Dimension(1, 5);
/* 143 */     Component space1 = Box.createRigidArea(spacing);
/* 144 */     Component space2 = Box.createRigidArea(spacing);
/*     */     
/* 146 */     if (getComponentOrientation().isLeftToRight()) {
/* 147 */       temp.add(findLabel); temp.add(fcp);
/* 148 */       temp.add(space1); temp.add(space2);
/* 149 */       temp.add(replaceLabel); temp.add(rcp);
/*     */     } else {
/*     */       
/* 152 */       temp.add(fcp); temp.add(findLabel);
/* 153 */       temp.add(space2); temp.add(space1);
/* 154 */       temp.add(rcp); temp.add(replaceLabel);
/*     */     } 
/* 156 */     UIUtil.makeSpringCompactGrid(temp, 3, 2, 0, 0, 0, 0);
/*     */     
/* 158 */     return temp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void createFindButtons() {
/* 165 */     super.createFindButtons();
/*     */     
/* 167 */     this.replaceButton = new JButton(SEARCH_MSG.getString("Replace"));
/* 168 */     makeEnterActivateButton(this.replaceButton);
/* 169 */     this.replaceButton.setToolTipText(MSG.getString("Replace.ToolTip"));
/* 170 */     this.replaceButton.setActionCommand("Replace");
/* 171 */     this.replaceButton.addActionListener(this.listener);
/* 172 */     this.replaceButton.setEnabled(false);
/*     */     
/* 174 */     this.replaceAllButton = new JButton(SEARCH_MSG.getString("ReplaceAll"));
/* 175 */     makeEnterActivateButton(this.replaceAllButton);
/* 176 */     this.replaceAllButton.setActionCommand("ReplaceAll");
/* 177 */     this.replaceAllButton.addActionListener(this.listener);
/* 178 */     this.replaceAllButton.setEnabled(false);
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
/*     */   protected void handleRegExCheckBoxClicked() {
/* 190 */     super.handleRegExCheckBoxClicked();
/*     */     
/* 192 */     boolean b = this.regexCheckBox.isSelected();
/* 193 */     this.replaceCombo.setAutoCompleteEnabled(b);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleSearchAction(ActionEvent e) {
/* 199 */     String command = e.getActionCommand();
/* 200 */     super.handleSearchAction(e);
/* 201 */     if ("FindNext".equals(command) || "FindPrevious".equals(command)) {
/* 202 */       handleToggleButtons();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FindReplaceButtonsEnableResult handleToggleButtons() {
/* 210 */     FindReplaceButtonsEnableResult er = super.handleToggleButtons();
/* 211 */     boolean shouldReplace = er.getEnable();
/* 212 */     this.replaceAllButton.setEnabled(shouldReplace);
/*     */ 
/*     */ 
/*     */     
/* 216 */     if (shouldReplace) {
/* 217 */       String text = this.searchListener.getSelectedText();
/* 218 */       shouldReplace = matchesSearchFor(text);
/*     */     } 
/* 220 */     this.replaceButton.setEnabled(shouldReplace);
/*     */     
/* 222 */     return er;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean matchesSearchFor(String text) {
/* 227 */     if (text == null || text.length() == 0) {
/* 228 */       return false;
/*     */     }
/* 230 */     String searchFor = this.findCombo.getSelectedString();
/* 231 */     if (searchFor != null && searchFor.length() > 0) {
/* 232 */       boolean matchCase = this.matchCaseCheckBox.isSelected();
/* 233 */       if (this.regexCheckBox.isSelected()) {
/* 234 */         Pattern pattern; int flags = 8;
/* 235 */         flags = RSyntaxUtilities.getPatternFlags(matchCase, flags);
/*     */         
/*     */         try {
/* 238 */           pattern = Pattern.compile(searchFor, flags);
/* 239 */         } catch (PatternSyntaxException pse) {
/* 240 */           pse.printStackTrace();
/* 241 */           return false;
/*     */         } 
/* 243 */         return pattern.matcher(text).matches();
/*     */       } 
/*     */       
/* 246 */       if (matchCase) {
/* 247 */         return searchFor.equals(text);
/*     */       }
/* 249 */       return searchFor.equalsIgnoreCase(text);
/*     */     } 
/*     */     
/* 252 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requestFocusInWindow() {
/* 261 */     boolean result = super.requestFocusInWindow();
/* 262 */     handleToggleButtons();
/* 263 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContentAssistImage(Image image) {
/* 268 */     super.setContentAssistImage(image);
/* 269 */     this.replaceCombo.setContentAssistImage(image);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class ReplaceFindFieldListener
/*     */     extends FindToolBar.FindFieldListener
/*     */   {
/*     */     protected void handleDocumentEvent(DocumentEvent e) {
/* 280 */       super.handleDocumentEvent(e);
/* 281 */       JTextComponent findField = UIUtil.getTextComponent((JComboBox)ReplaceToolBar.this.findCombo);
/* 282 */       JTextComponent replaceField = UIUtil.getTextComponent((JComboBox)ReplaceToolBar.this.replaceCombo);
/* 283 */       if (e.getDocument().equals(findField.getDocument())) {
/* 284 */         ReplaceToolBar.this.handleToggleButtons();
/*     */       }
/* 286 */       if (e.getDocument() == replaceField.getDocument())
/* 287 */         ReplaceToolBar.this.getSearchContext().setReplaceWith(replaceField.getText()); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rst\\ui\search\ReplaceToolBar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */