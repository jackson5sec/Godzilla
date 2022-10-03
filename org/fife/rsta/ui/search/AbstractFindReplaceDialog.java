/*     */ package org.fife.rsta.ui.search;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dialog;
/*     */ import java.awt.Frame;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.ActionMap;
/*     */ import javax.swing.BoxLayout;
/*     */ import javax.swing.ButtonGroup;
/*     */ import javax.swing.InputMap;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JRadioButton;
/*     */ import javax.swing.JRootPane;
/*     */ import javax.swing.KeyStroke;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.EventListenerList;
/*     */ import javax.swing.text.JTextComponent;
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
/*     */ public abstract class AbstractFindReplaceDialog
/*     */   extends AbstractSearchDialog
/*     */ {
/*     */   public static final String SEARCH_DOWNWARD_PROPERTY = "SearchDialog.SearchDownward";
/*     */   protected JRadioButton upButton;
/*     */   protected JRadioButton downButton;
/*     */   protected JPanel dirPanel;
/*     */   private String dirPanelTitle;
/*     */   protected JLabel findFieldLabel;
/*     */   protected JButton findNextButton;
/*     */   protected JCheckBox markAllCheckBox;
/*     */   private EventListenerList listenerList;
/*     */   
/*     */   public AbstractFindReplaceDialog(Dialog owner) {
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
/*     */   
/*     */   public AbstractFindReplaceDialog(Frame owner) {
/*  91 */     super(owner);
/*  92 */     init();
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
/*     */   public void actionPerformed(ActionEvent e) {
/* 104 */     String command = e.getActionCommand();
/*     */     
/* 106 */     if ("UpRadioButtonClicked".equals(command)) {
/* 107 */       this.context.setSearchForward(false);
/*     */     
/*     */     }
/* 110 */     else if ("DownRadioButtonClicked".equals(command)) {
/* 111 */       this.context.setSearchForward(true);
/*     */     
/*     */     }
/* 114 */     else if ("MarkAll".equals(command)) {
/* 115 */       boolean checked = this.markAllCheckBox.isSelected();
/* 116 */       this.context.setMarkAll(checked);
/*     */     
/*     */     }
/* 119 */     else if (SearchEvent.Type.FIND.name().equals(command)) {
/* 120 */       doSearch(this.context.getSearchForward());
/*     */     }
/*     */     else {
/*     */       
/* 124 */       super.actionPerformed(e);
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
/*     */   public void addSearchListener(SearchListener l) {
/* 140 */     this.listenerList.add(SearchListener.class, l);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doSearch(boolean forward) {
/* 147 */     JTextComponent tc = UIUtil.getTextComponent((JComboBox)this.findTextCombo);
/* 148 */     this.findTextCombo.addItem(tc.getText());
/* 149 */     this.context.setSearchFor(getSearchString());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 155 */     SearchContext contextToFire = this.context;
/* 156 */     if (forward != this.context.getSearchForward()) {
/* 157 */       contextToFire = this.context.clone();
/* 158 */       contextToFire.setSearchForward(forward);
/*     */     } 
/*     */ 
/*     */     
/* 162 */     fireSearchEvent(SearchEvent.Type.FIND, contextToFire);
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
/*     */   protected void fireSearchEvent(SearchEvent.Type type, SearchContext context) {
/* 179 */     if (context == null) {
/* 180 */       context = this.context;
/*     */     }
/*     */ 
/*     */     
/* 184 */     Object[] listeners = this.listenerList.getListenerList();
/* 185 */     SearchEvent e = null;
/*     */ 
/*     */     
/* 188 */     for (int i = listeners.length - 2; i >= 0; i -= 2) {
/* 189 */       if (listeners[i] == SearchListener.class) {
/*     */         
/* 191 */         if (e == null) {
/* 192 */           e = new SearchEvent(this, type, context);
/*     */         }
/* 194 */         ((SearchListener)listeners[i + 1]).searchEvent(e);
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
/*     */   public final String getDownRadioButtonText() {
/* 207 */     return this.downButton.getText();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getFindButtonText() {
/* 218 */     return this.findNextButton.getText();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getFindWhatLabelText() {
/* 229 */     return this.findFieldLabel.getText();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getSearchButtonsBorderText() {
/* 240 */     return this.dirPanelTitle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getUpRadioButtonText() {
/* 251 */     return this.upButton.getText();
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
/* 264 */     String prop = e.getPropertyName();
/*     */     
/* 266 */     if ("Search.Forward".equals(prop)) {
/* 267 */       boolean newValue = ((Boolean)e.getNewValue()).booleanValue();
/* 268 */       JRadioButton button = newValue ? this.downButton : this.upButton;
/* 269 */       button.setSelected(true);
/*     */     
/*     */     }
/* 272 */     else if ("Search.MarkAll".equals(prop)) {
/* 273 */       boolean newValue = ((Boolean)e.getNewValue()).booleanValue();
/* 274 */       this.markAllCheckBox.setSelected(newValue);
/*     */     }
/*     */     else {
/*     */       
/* 278 */       super.handleSearchContextPropertyChanged(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected FindReplaceButtonsEnableResult handleToggleButtons() {
/* 287 */     FindReplaceButtonsEnableResult er = super.handleToggleButtons();
/* 288 */     boolean enable = er.getEnable();
/*     */     
/* 290 */     this.findNextButton.setEnabled(enable);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 295 */     JTextComponent tc = UIUtil.getTextComponent((JComboBox)this.findTextCombo);
/* 296 */     tc.setForeground(enable ? UIManager.getColor("TextField.foreground") : 
/* 297 */         UIUtil.getErrorTextForeground());
/*     */     
/* 299 */     String tooltip = SearchUtil.getToolTip(er);
/* 300 */     tc.setToolTipText(tooltip);
/*     */     
/* 302 */     return er;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init() {
/* 309 */     this.listenerList = new EventListenerList();
/*     */ 
/*     */     
/* 312 */     this.dirPanel = new JPanel();
/* 313 */     this.dirPanel.setLayout(new BoxLayout(this.dirPanel, 2));
/* 314 */     setSearchButtonsBorderText(getString("Direction"));
/* 315 */     ButtonGroup bg = new ButtonGroup();
/* 316 */     this.upButton = new JRadioButton(getString("Up"), false);
/* 317 */     this.upButton.setMnemonic(getString("UpMnemonic").charAt(0));
/* 318 */     this.downButton = new JRadioButton(getString("Down"), true);
/* 319 */     this.downButton.setMnemonic(getString("DownMnemonic").charAt(0));
/* 320 */     this.upButton.setActionCommand("UpRadioButtonClicked");
/* 321 */     this.upButton.addActionListener(this);
/* 322 */     this.downButton.setActionCommand("DownRadioButtonClicked");
/* 323 */     this.downButton.addActionListener(this);
/* 324 */     bg.add(this.upButton);
/* 325 */     bg.add(this.downButton);
/* 326 */     this.dirPanel.add(this.upButton);
/* 327 */     this.dirPanel.add(this.downButton);
/*     */ 
/*     */     
/* 330 */     this.markAllCheckBox = new JCheckBox(getString("MarkAll"));
/* 331 */     this.markAllCheckBox.setMnemonic(getString("MarkAllMnemonic").charAt(0));
/* 332 */     this.markAllCheckBox.setActionCommand("MarkAll");
/* 333 */     this.markAllCheckBox.addActionListener(this);
/*     */ 
/*     */     
/* 336 */     this.searchConditionsPanel.removeAll();
/* 337 */     this.searchConditionsPanel.setLayout(new BorderLayout());
/* 338 */     JPanel temp = new JPanel();
/* 339 */     temp.setLayout(new BoxLayout(temp, 3));
/* 340 */     temp.add(this.caseCheckBox);
/* 341 */     temp.add(this.wholeWordCheckBox);
/* 342 */     temp.add(this.wrapCheckBox);
/* 343 */     this.searchConditionsPanel.add(temp, "Before");
/* 344 */     temp = new JPanel();
/* 345 */     temp.setLayout(new BoxLayout(temp, 3));
/* 346 */     temp.add(this.regexCheckBox);
/* 347 */     temp.add(this.markAllCheckBox);
/* 348 */     this.searchConditionsPanel.add(temp, "After");
/*     */ 
/*     */     
/* 351 */     this.findFieldLabel = UIUtil.newLabel(getBundle(), "FindWhat", (Component)this.findTextCombo);
/*     */ 
/*     */     
/* 354 */     this.findNextButton = UIUtil.newButton(getBundle(), "Find");
/* 355 */     this.findNextButton.setActionCommand(SearchEvent.Type.FIND.name());
/* 356 */     this.findNextButton.addActionListener(this);
/* 357 */     this.findNextButton.setDefaultCapable(true);
/* 358 */     this.findNextButton.setEnabled(false);
/*     */     
/* 360 */     installKeyboardActions();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void installKeyboardActions() {
/* 370 */     JRootPane rootPane = getRootPane();
/* 371 */     InputMap im = rootPane.getInputMap(1);
/*     */     
/* 373 */     ActionMap am = rootPane.getActionMap();
/*     */     
/* 375 */     int modifier = getToolkit().getMenuShortcutKeyMask();
/* 376 */     KeyStroke ctrlF = KeyStroke.getKeyStroke(70, modifier);
/* 377 */     im.put(ctrlF, "focusSearchForField");
/* 378 */     am.put("focusSearchForField", new AbstractAction()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 381 */             AbstractFindReplaceDialog.this.requestFocus();
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 386 */     int shift = 1;
/* 387 */     int ctrl = 2;
/* 388 */     if (System.getProperty("os.name").toLowerCase().contains("os x")) {
/* 389 */       ctrl = 4;
/*     */     }
/* 391 */     KeyStroke ks = KeyStroke.getKeyStroke(10, shift);
/* 392 */     im.put(ks, "searchBackward");
/* 393 */     ks = KeyStroke.getKeyStroke(10, ctrl);
/* 394 */     im.put(ks, "searchBackward");
/* 395 */     am.put("searchBackward", new AbstractAction()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/* 398 */             AbstractFindReplaceDialog.this.doSearch(!AbstractFindReplaceDialog.this.context.getSearchForward());
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void refreshUIFromContext() {
/* 409 */     if (this.markAllCheckBox == null) {
/*     */       return;
/*     */     }
/* 412 */     super.refreshUIFromContext();
/* 413 */     this.markAllCheckBox.setSelected(this.context.getMarkAll());
/* 414 */     boolean searchForward = this.context.getSearchForward();
/* 415 */     this.upButton.setSelected(!searchForward);
/* 416 */     this.downButton.setSelected(searchForward);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeSearchListener(SearchListener l) {
/* 427 */     this.listenerList.remove(SearchListener.class, l);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDownRadioButtonText(String text) {
/* 438 */     this.downButton.setText(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setFindButtonText(String text) {
/* 449 */     this.findNextButton.setText(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFindWhatLabelText(String text) {
/* 460 */     this.findFieldLabel.setText(text);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setSearchButtonsBorderText(String text) {
/* 471 */     this.dirPanelTitle = text;
/* 472 */     this.dirPanel.setBorder(createTitledBorder(this.dirPanelTitle));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUpRadioButtonText(String text) {
/* 483 */     this.upButton.setText(text);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rst\\ui\search\AbstractFindReplaceDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */