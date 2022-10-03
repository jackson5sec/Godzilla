/*     */ package org.fife.rsta.ui.search;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Image;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.awt.event.KeyAdapter;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.Timer;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import org.fife.rsta.ui.AssistanceIconPanel;
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
/*     */ public class FindToolBar
/*     */   extends JPanel
/*     */ {
/*     */   private SearchContext context;
/*     */   protected ToolBarListener listener;
/*     */   protected FindFieldListener findFieldListener;
/*     */   protected SearchComboBox findCombo;
/*     */   protected SearchComboBox replaceCombo;
/*     */   protected JButton findButton;
/*     */   protected JButton findPrevButton;
/*     */   protected JCheckBox matchCaseCheckBox;
/*     */   protected JCheckBox wholeWordCheckBox;
/*     */   protected JCheckBox regexCheckBox;
/*     */   protected JCheckBox markAllCheckBox;
/*     */   protected JCheckBox wrapCheckBox;
/*     */   private JLabel infoLabel;
/*     */   private Timer markAllTimer;
/*     */   private boolean settingFindTextFromEvent;
/*  80 */   protected static final ResourceBundle SEARCH_MSG = ResourceBundle.getBundle("org.fife.rsta.ui.search.Search");
/*     */   
/*  82 */   protected static final ResourceBundle MSG = ResourceBundle.getBundle("org.fife.rsta.ui.search.SearchToolBar");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FindToolBar(SearchListener listener) {
/*  93 */     setFocusCycleRoot(true);
/*     */     
/*  95 */     this.markAllTimer = new Timer(300, new MarkAllEventNotifier());
/*  96 */     this.markAllTimer.setRepeats(false);
/*     */     
/*  98 */     setLayout(new BorderLayout());
/*  99 */     setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
/* 100 */     addSearchListener(listener);
/* 101 */     this.listener = new ToolBarListener();
/*     */ 
/*     */ 
/*     */     
/* 105 */     setSearchContext(new SearchContext());
/*     */ 
/*     */     
/* 108 */     ComponentOrientation orientation = ComponentOrientation.getOrientation(getLocale());
/*     */     
/* 110 */     add(Box.createHorizontalStrut(5));
/*     */     
/* 112 */     add(createFieldPanel());
/*     */     
/* 114 */     Box rest = new Box(2);
/* 115 */     add(rest, "After");
/*     */     
/* 117 */     rest.add(Box.createHorizontalStrut(5));
/* 118 */     rest.add(createButtonPanel());
/* 119 */     rest.add(Box.createHorizontalStrut(15));
/*     */     
/* 121 */     JLabel infoLabel = new JLabel();
/* 122 */     rest.add(infoLabel);
/*     */     
/* 124 */     rest.add(Box.createHorizontalGlue());
/*     */ 
/*     */     
/* 127 */     applyComponentOrientation(orientation);
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
/*     */   public void addSearchListener(SearchListener l) {
/* 139 */     this.listenerList.add(SearchListener.class, l);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Container createButtonPanel() {
/* 145 */     Box panel = new Box(2);
/* 146 */     createFindButtons();
/*     */ 
/*     */ 
/*     */     
/* 150 */     JPanel filler = new JPanel(new BorderLayout());
/* 151 */     filler.setBorder(BorderFactory.createEmptyBorder());
/* 152 */     filler.add(this.findButton);
/* 153 */     panel.add(filler);
/* 154 */     panel.add(Box.createHorizontalStrut(6));
/*     */     
/* 156 */     this.matchCaseCheckBox = createCB("MatchCase");
/* 157 */     panel.add(this.matchCaseCheckBox);
/*     */     
/* 159 */     this.regexCheckBox = createCB("RegEx");
/* 160 */     panel.add(this.regexCheckBox);
/*     */     
/* 162 */     this.wholeWordCheckBox = createCB("WholeWord");
/* 163 */     panel.add(this.wholeWordCheckBox);
/*     */     
/* 165 */     this.markAllCheckBox = createCB("MarkAll");
/* 166 */     panel.add(this.markAllCheckBox);
/*     */     
/* 168 */     this.wrapCheckBox = createCB("Wrap");
/* 169 */     panel.add(this.wrapCheckBox);
/*     */     
/* 171 */     return panel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected JCheckBox createCB(String key) {
/* 177 */     JCheckBox cb = new JCheckBox(SEARCH_MSG.getString(key));
/* 178 */     cb.addActionListener(this.listener);
/* 179 */     cb.addMouseListener(this.listener);
/* 180 */     return cb;
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
/*     */   protected Container createContentAssistablePanel(JComponent comp) {
/* 192 */     JPanel temp = new JPanel(new BorderLayout());
/* 193 */     temp.add(comp);
/* 194 */     AssistanceIconPanel aip = new AssistanceIconPanel(comp);
/* 195 */     temp.add((Component)aip, "Before");
/* 196 */     return temp;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Container createFieldPanel() {
/* 202 */     this.findFieldListener = new FindFieldListener();
/* 203 */     JPanel temp = new JPanel(new BorderLayout());
/*     */     
/* 205 */     this.findCombo = new SearchComboBox(this, false);
/* 206 */     JTextComponent findField = UIUtil.getTextComponent((JComboBox)this.findCombo);
/* 207 */     this.findFieldListener.install(findField);
/* 208 */     temp.add(createContentAssistablePanel((JComponent)this.findCombo));
/*     */     
/* 210 */     return temp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void createFindButtons() {
/* 219 */     this.findPrevButton = new JButton(MSG.getString("FindPrev"));
/* 220 */     makeEnterActivateButton(this.findPrevButton);
/* 221 */     this.findPrevButton.setActionCommand("FindPrevious");
/* 222 */     this.findPrevButton.addActionListener(this.listener);
/* 223 */     this.findPrevButton.setEnabled(false);
/*     */     
/* 225 */     this.findButton = new JButton(SEARCH_MSG.getString("Find"))
/*     */       {
/*     */         public Dimension getPreferredSize() {
/* 228 */           return FindToolBar.this.findPrevButton.getPreferredSize();
/*     */         }
/*     */       };
/* 231 */     makeEnterActivateButton(this.findButton);
/* 232 */     this.findButton.setToolTipText(MSG.getString("Find.ToolTip"));
/* 233 */     this.findButton.setActionCommand("FindNext");
/* 234 */     this.findButton.addActionListener(this.listener);
/* 235 */     this.findButton.setEnabled(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doMarkAll(boolean delay) {
/* 246 */     if (this.context.getMarkAll() && !this.settingFindTextFromEvent) {
/* 247 */       if (delay) {
/* 248 */         this.markAllTimer.restart();
/*     */       } else {
/*     */         
/* 251 */         fireMarkAllEvent();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void doSearch(boolean forward) {
/* 258 */     if (forward) {
/* 259 */       this.findButton.doClick(0);
/*     */     } else {
/*     */       
/* 262 */       this.findPrevButton.doClick(0);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void fireMarkAllEvent() {
/* 271 */     SearchEvent se = new SearchEvent(this, SearchEvent.Type.MARK_ALL, this.context);
/*     */     
/* 273 */     fireSearchEvent(se);
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
/*     */   protected void fireSearchEvent(SearchEvent e) {
/* 289 */     SearchListener[] listeners = this.listenerList.<SearchListener>getListeners(SearchListener.class);
/* 290 */     int count = (listeners == null) ? 0 : listeners.length;
/* 291 */     for (int i = count - 1; i >= 0; i--) {
/* 292 */       listeners[i].searchEvent(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getFindText() {
/* 298 */     return UIUtil.getTextComponent((JComboBox)this.findCombo).getText();
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
/*     */   public int getMarkAllDelay() {
/* 310 */     return this.markAllTimer.getInitialDelay();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getReplaceText() {
/* 315 */     if (this.replaceCombo == null) {
/* 316 */       return null;
/*     */     }
/* 318 */     return UIUtil.getTextComponent((JComboBox)this.replaceCombo).getText();
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
/* 329 */     return this.context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleRegExCheckBoxClicked() {
/* 339 */     handleToggleButtons();
/*     */     
/* 341 */     boolean b = this.regexCheckBox.isSelected();
/* 342 */     this.findCombo.setAutoCompleteEnabled(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleSearchAction(ActionEvent e) {
/* 353 */     SearchEvent.Type type = null;
/* 354 */     boolean forward = true;
/* 355 */     String action = e.getActionCommand();
/*     */     
/* 357 */     int allowedModifiers = 195;
/*     */ 
/*     */ 
/*     */     
/* 361 */     if ("FindNext".equals(action)) {
/* 362 */       type = SearchEvent.Type.FIND;
/* 363 */       int mods = e.getModifiers();
/* 364 */       forward = ((mods & allowedModifiers) == 0);
/*     */       
/* 366 */       JTextComponent tc = UIUtil.getTextComponent((JComboBox)this.findCombo);
/* 367 */       this.findCombo.addItem(tc.getText());
/*     */     }
/* 369 */     else if ("FindPrevious".equals(action)) {
/* 370 */       type = SearchEvent.Type.FIND;
/* 371 */       forward = false;
/*     */       
/* 373 */       JTextComponent tc = UIUtil.getTextComponent((JComboBox)this.findCombo);
/* 374 */       this.findCombo.addItem(tc.getText());
/*     */     }
/* 376 */     else if ("Replace".equals(action)) {
/* 377 */       type = SearchEvent.Type.REPLACE;
/* 378 */       int mods = e.getModifiers();
/* 379 */       forward = ((mods & allowedModifiers) == 0);
/*     */       
/* 381 */       JTextComponent tc = UIUtil.getTextComponent((JComboBox)this.findCombo);
/* 382 */       this.findCombo.addItem(tc.getText());
/* 383 */       tc = UIUtil.getTextComponent((JComboBox)this.replaceCombo);
/* 384 */       this.replaceCombo.addItem(tc.getText());
/*     */     }
/* 386 */     else if ("ReplaceAll".equals(action)) {
/* 387 */       type = SearchEvent.Type.REPLACE_ALL;
/*     */       
/* 389 */       JTextComponent tc = UIUtil.getTextComponent((JComboBox)this.findCombo);
/* 390 */       this.findCombo.addItem(tc.getText());
/* 391 */       tc = UIUtil.getTextComponent((JComboBox)this.replaceCombo);
/* 392 */       this.replaceCombo.addItem(tc.getText());
/*     */     } 
/*     */     
/* 395 */     this.context.setSearchFor(getFindText());
/* 396 */     if (this.replaceCombo != null) {
/* 397 */       this.context.setReplaceWith(this.replaceCombo.getSelectedString());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 404 */     SearchContext contextToFire = this.context;
/* 405 */     if (forward != this.context.getSearchForward()) {
/* 406 */       contextToFire = this.context.clone();
/* 407 */       contextToFire.setSearchForward(forward);
/*     */     } 
/*     */     
/* 410 */     SearchEvent se = new SearchEvent(this, type, contextToFire);
/* 411 */     fireSearchEvent(se);
/* 412 */     handleToggleButtons();
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
/*     */   protected FindReplaceButtonsEnableResult handleToggleButtons() {
/* 427 */     FindReplaceButtonsEnableResult result = new FindReplaceButtonsEnableResult(true, null);
/*     */ 
/*     */     
/* 430 */     String text = getFindText();
/* 431 */     if (text.length() == 0) {
/* 432 */       result = new FindReplaceButtonsEnableResult(false, null);
/*     */     }
/* 434 */     else if (this.regexCheckBox.isSelected()) {
/*     */       try {
/* 436 */         Pattern.compile(text);
/* 437 */       } catch (PatternSyntaxException pse) {
/*     */         
/* 439 */         result = new FindReplaceButtonsEnableResult(false, pse.getMessage());
/*     */       } 
/*     */     } 
/*     */     
/* 443 */     boolean enable = result.getEnable();
/* 444 */     this.findButton.setEnabled(enable);
/* 445 */     this.findPrevButton.setEnabled(enable);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 450 */     JTextComponent tc = UIUtil.getTextComponent((JComboBox)this.findCombo);
/* 451 */     tc.setForeground(enable ? UIManager.getColor("TextField.foreground") : 
/* 452 */         UIUtil.getErrorTextForeground());
/*     */     
/* 454 */     String tooltip = SearchUtil.getToolTip(result);
/* 455 */     tc.setToolTipText(tooltip);
/*     */     
/* 457 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initUIFromContext() {
/* 468 */     if (this.findCombo == null) {
/*     */       return;
/*     */     }
/* 471 */     setFindText(this.context.getSearchFor());
/* 472 */     if (this.replaceCombo != null) {
/* 473 */       setReplaceText(this.context.getReplaceWith());
/*     */     }
/* 475 */     this.matchCaseCheckBox.setSelected(this.context.getMatchCase());
/* 476 */     this.wholeWordCheckBox.setSelected(this.context.getWholeWord());
/* 477 */     this.regexCheckBox.setSelected(this.context.isRegularExpression());
/* 478 */     this.markAllCheckBox.setSelected(this.context.getMarkAll());
/* 479 */     this.wrapCheckBox.setSelected(this.context.getSearchWrap());
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
/*     */   protected void makeEnterActivateButton(JButton button) {
/* 496 */     InputMap im = button.getInputMap();
/*     */ 
/*     */     
/* 499 */     im.put(KeyStroke.getKeyStroke("ENTER"), "pressed");
/* 500 */     im.put(KeyStroke.getKeyStroke("released ENTER"), "released");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 507 */     im.put(KeyStroke.getKeyStroke(10, 1, false), "pressed");
/*     */     
/* 509 */     im.put(KeyStroke.getKeyStroke(10, 1, true), "released");
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
/*     */   public void removeSearchListener(SearchListener l) {
/* 522 */     this.listenerList.remove(SearchListener.class, l);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requestFocusInWindow() {
/* 532 */     JTextComponent findField = UIUtil.getTextComponent((JComboBox)this.findCombo);
/* 533 */     findField.selectAll();
/* 534 */     return findField.requestFocusInWindow();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void searchComboUpdateUICallback(SearchComboBox combo) {
/* 545 */     this.findFieldListener.install(UIUtil.getTextComponent((JComboBox)combo));
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
/* 558 */     this.findCombo.setContentAssistImage(image);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setFindText(String text) {
/* 563 */     UIUtil.getTextComponent((JComboBox)this.findCombo).setText(text);
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
/*     */   public void setMarkAllDelay(int millis) {
/* 576 */     this.markAllTimer.setInitialDelay(millis);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setReplaceText(String text) {
/* 581 */     if (this.replaceCombo != null) {
/* 582 */       UIUtil.getTextComponent((JComboBox)this.replaceCombo).setText(text);
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
/*     */   public void setSearchContext(SearchContext context) {
/* 597 */     if (this.context != null) {
/* 598 */       this.context.removePropertyChangeListener(this.listener);
/*     */     }
/* 600 */     this.context = context;
/* 601 */     this.context.addPropertyChangeListener(this.listener);
/* 602 */     initUIFromContext();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class ToolBarListener
/*     */     extends MouseAdapter
/*     */     implements ActionListener, PropertyChangeListener
/*     */   {
/*     */     private ToolBarListener() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 616 */       Object source = e.getSource();
/*     */       
/* 618 */       if (source == FindToolBar.this.matchCaseCheckBox) {
/* 619 */         FindToolBar.this.context.setMatchCase(FindToolBar.this.matchCaseCheckBox.isSelected());
/* 620 */         if (FindToolBar.this.markAllCheckBox.isSelected()) {
/* 621 */           FindToolBar.this.doMarkAll(false);
/*     */         }
/*     */       }
/* 624 */       else if (source == FindToolBar.this.wholeWordCheckBox) {
/* 625 */         FindToolBar.this.context.setWholeWord(FindToolBar.this.wholeWordCheckBox.isSelected());
/* 626 */         if (FindToolBar.this.markAllCheckBox.isSelected()) {
/* 627 */           FindToolBar.this.doMarkAll(false);
/*     */         }
/*     */       }
/* 630 */       else if (source == FindToolBar.this.regexCheckBox) {
/* 631 */         FindToolBar.this.context.setRegularExpression(FindToolBar.this.regexCheckBox.isSelected());
/* 632 */         if (FindToolBar.this.markAllCheckBox.isSelected()) {
/* 633 */           FindToolBar.this.doMarkAll(false);
/*     */         }
/*     */       }
/* 636 */       else if (source == FindToolBar.this.markAllCheckBox) {
/* 637 */         FindToolBar.this.context.setMarkAll(FindToolBar.this.markAllCheckBox.isSelected());
/* 638 */         FindToolBar.this.fireMarkAllEvent();
/*     */       }
/* 640 */       else if (source == FindToolBar.this.wrapCheckBox) {
/* 641 */         FindToolBar.this.context.setSearchWrap(FindToolBar.this.wrapCheckBox.isSelected());
/*     */       } else {
/*     */         
/* 644 */         FindToolBar.this.handleSearchAction(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void mouseClicked(MouseEvent e) {
/* 651 */       if (e.getSource() instanceof JCheckBox) {
/* 652 */         FindToolBar.this.findFieldListener.selectAll = false;
/* 653 */         FindToolBar.this.findCombo.requestFocusInWindow();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void propertyChange(PropertyChangeEvent e) {
/* 661 */       String prop = e.getPropertyName();
/*     */       
/* 663 */       if ("Search.MatchCase".equals(prop)) {
/* 664 */         boolean newValue = ((Boolean)e.getNewValue()).booleanValue();
/* 665 */         FindToolBar.this.matchCaseCheckBox.setSelected(newValue);
/*     */       }
/* 667 */       else if ("Search.MatchWholeWord".equals(prop)) {
/* 668 */         boolean newValue = ((Boolean)e.getNewValue()).booleanValue();
/* 669 */         FindToolBar.this.wholeWordCheckBox.setSelected(newValue);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 679 */       else if ("Search.UseRegex".equals(prop)) {
/* 680 */         boolean newValue = ((Boolean)e.getNewValue()).booleanValue();
/* 681 */         FindToolBar.this.regexCheckBox.setSelected(newValue);
/* 682 */         FindToolBar.this.handleRegExCheckBoxClicked();
/*     */       }
/* 684 */       else if ("Search.MarkAll".equals(prop)) {
/* 685 */         boolean newValue = ((Boolean)e.getNewValue()).booleanValue();
/* 686 */         FindToolBar.this.markAllCheckBox.setSelected(newValue);
/*     */ 
/*     */       
/*     */       }
/* 690 */       else if ("Search.searchFor".equals(prop)) {
/* 691 */         String newValue = (String)e.getNewValue();
/* 692 */         String oldValue = FindToolBar.this.getFindText();
/*     */         
/* 694 */         if (!newValue.equals(oldValue)) {
/* 695 */           FindToolBar.this.settingFindTextFromEvent = true;
/* 696 */           FindToolBar.this.setFindText(newValue);
/* 697 */           FindToolBar.this.settingFindTextFromEvent = false;
/*     */         }
/*     */       
/* 700 */       } else if ("Search.replaceWith".equals(prop)) {
/* 701 */         String newValue = (String)e.getNewValue();
/* 702 */         String oldValue = FindToolBar.this.getReplaceText();
/*     */         
/* 704 */         if (!newValue.equals(oldValue)) {
/* 705 */           FindToolBar.this.settingFindTextFromEvent = true;
/* 706 */           FindToolBar.this.setReplaceText(newValue);
/* 707 */           FindToolBar.this.settingFindTextFromEvent = false;
/*     */         }
/*     */       
/* 710 */       } else if ("Search.Wrap".equals(prop)) {
/* 711 */         boolean newValue = ((Boolean)e.getNewValue()).booleanValue();
/* 712 */         FindToolBar.this.wrapCheckBox.setSelected(newValue);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class FindFieldListener
/*     */     extends KeyAdapter
/*     */     implements DocumentListener, FocusListener
/*     */   {
/*     */     protected boolean selectAll;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void changedUpdate(DocumentEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void focusGained(FocusEvent e) {
/* 735 */       JTextField field = (JTextField)e.getComponent();
/* 736 */       if (this.selectAll) {
/* 737 */         field.selectAll();
/*     */       }
/* 739 */       this.selectAll = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void focusLost(FocusEvent e) {}
/*     */ 
/*     */     
/*     */     protected void handleDocumentEvent(DocumentEvent e) {
/* 747 */       FindToolBar.this.handleToggleButtons();
/* 748 */       if (!FindToolBar.this.settingFindTextFromEvent) {
/* 749 */         JTextComponent findField = UIUtil.getTextComponent((JComboBox)FindToolBar.this.findCombo);
/* 750 */         if (e.getDocument() == findField.getDocument()) {
/* 751 */           FindToolBar.this.context.setSearchFor(findField.getText());
/* 752 */           if (FindToolBar.this.context.getMarkAll()) {
/* 753 */             FindToolBar.this.doMarkAll(true);
/*     */           }
/*     */         } else {
/*     */           
/* 757 */           JTextComponent replaceField = UIUtil.getTextComponent((JComboBox)FindToolBar.this.replaceCombo);
/*     */           
/* 759 */           FindToolBar.this.context.setReplaceWith(replaceField.getText());
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void insertUpdate(DocumentEvent e) {
/* 767 */       handleDocumentEvent(e);
/*     */     }
/*     */     
/*     */     public void install(JTextComponent field) {
/* 771 */       field.getDocument().addDocumentListener(this);
/* 772 */       field.addKeyListener(this);
/* 773 */       field.addFocusListener(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public void keyTyped(KeyEvent e) {
/* 778 */       if (e.getKeyChar() == '\n') {
/* 779 */         int mod = e.getModifiers();
/* 780 */         int ctrlShift = 3;
/* 781 */         boolean forward = ((mod & ctrlShift) == 0);
/* 782 */         FindToolBar.this.doSearch(forward);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void removeUpdate(DocumentEvent e) {
/* 788 */       handleDocumentEvent(e);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class MarkAllEventNotifier
/*     */     implements ActionListener
/*     */   {
/*     */     private MarkAllEventNotifier() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 803 */       FindToolBar.this.fireMarkAllEvent();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rst\\ui\search\FindToolBar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */