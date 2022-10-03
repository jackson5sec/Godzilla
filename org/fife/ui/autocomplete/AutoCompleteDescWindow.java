/*     */ package org.fife.ui.autocomplete;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Window;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.swing.AbstractAction;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JEditorPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JToolBar;
/*     */ import javax.swing.JWindow;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.Timer;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.AbstractBorder;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.event.HyperlinkEvent;
/*     */ import javax.swing.event.HyperlinkListener;
/*     */ import org.fife.ui.rsyntaxtextarea.PopupWindowDecorator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class AutoCompleteDescWindow
/*     */   extends JWindow
/*     */   implements HyperlinkListener, DescWindowCallback
/*     */ {
/*     */   private AutoCompletion ac;
/*     */   private JEditorPane descArea;
/*     */   private JScrollPane scrollPane;
/*     */   private JToolBar descWindowNavBar;
/*     */   private Action backAction;
/*     */   private Action forwardAction;
/*     */   private List<HistoryEntry> history;
/*     */   private int historyPos;
/*     */   private Timer timer;
/*     */   private TimerAction timerAction;
/*     */   private ResourceBundle bundle;
/*     */   private static final int INITIAL_TIMER_DELAY = 120;
/*     */   private static final String MSG = "org.fife.ui.autocomplete.AutoCompleteDescWindow";
/*     */   
/*     */   AutoCompleteDescWindow(Window owner, AutoCompletion ac) {
/* 138 */     super(owner);
/* 139 */     this.ac = ac;
/*     */     
/* 141 */     ComponentOrientation o = ac.getTextComponentOrientation();
/*     */     
/* 143 */     JPanel cp = new JPanel(new BorderLayout());
/* 144 */     cp.setBorder(TipUtil.getToolTipBorder());
/*     */     
/* 146 */     this.descArea = new JEditorPane("text/html", null);
/* 147 */     TipUtil.tweakTipEditorPane(this.descArea);
/* 148 */     this.descArea.addHyperlinkListener(this);
/* 149 */     this.scrollPane = new JScrollPane(this.descArea);
/* 150 */     Border b = BorderFactory.createEmptyBorder();
/* 151 */     this.descArea.setBackground(ac.getDescWindowColor());
/* 152 */     this.scrollPane.setBorder(b);
/* 153 */     this.scrollPane.setViewportBorder(b);
/* 154 */     this.scrollPane.setBackground(this.descArea.getBackground());
/* 155 */     this.scrollPane.getViewport().setBackground(this.descArea.getBackground());
/* 156 */     cp.add(this.scrollPane);
/*     */     
/* 158 */     this.descWindowNavBar = new JToolBar();
/* 159 */     this.backAction = new ToolBarBackAction(o.isLeftToRight());
/* 160 */     this.forwardAction = new ToolBarForwardAction(o.isLeftToRight());
/* 161 */     this.descWindowNavBar.setFloatable(false);
/* 162 */     this.descWindowNavBar.setBackground(ac.getDescWindowColor());
/* 163 */     this.descWindowNavBar.add(new JButton(this.backAction));
/* 164 */     this.descWindowNavBar.add(new JButton(this.forwardAction));
/*     */     
/* 166 */     JPanel bottomPanel = new JPanel(new BorderLayout());
/* 167 */     b = new AbstractBorder()
/*     */       {
/*     */         public Insets getBorderInsets(Component c) {
/* 170 */           return new Insets(1, 0, 0, 0);
/*     */         }
/*     */         
/*     */         public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
/* 174 */           g.setColor(UIManager.getColor("controlDkShadow"));
/* 175 */           g.drawLine(x, y, x + w - 1, y);
/*     */         }
/*     */       };
/* 178 */     bottomPanel.setBorder(b);
/* 179 */     bottomPanel.setBackground(ac.getDescWindowColor());
/* 180 */     SizeGrip rp = new SizeGrip();
/* 181 */     bottomPanel.add(this.descWindowNavBar, "Before");
/* 182 */     bottomPanel.add(rp, "After");
/* 183 */     rp.setBackground(ac.getDescWindowColor());
/* 184 */     cp.add(bottomPanel, "South");
/* 185 */     setContentPane(cp);
/*     */     
/* 187 */     applyComponentOrientation(o);
/* 188 */     setFocusableWindowState(false);
/*     */ 
/*     */     
/* 191 */     if (Util.getShouldAllowDecoratingMainAutoCompleteWindows()) {
/* 192 */       PopupWindowDecorator decorator = PopupWindowDecorator.get();
/* 193 */       if (decorator != null) {
/* 194 */         decorator.decorate(this);
/*     */       }
/*     */     } 
/*     */     
/* 198 */     this.history = new ArrayList<>(1);
/* 199 */     this.historyPos = -1;
/*     */     
/* 201 */     this.timerAction = new TimerAction();
/* 202 */     this.timer = new Timer(120, this.timerAction);
/* 203 */     this.timer.setRepeats(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToHistory(HistoryEntry historyItem) {
/* 214 */     this.history.add(++this.historyPos, historyItem);
/* 215 */     clearHistoryAfterCurrentPos();
/* 216 */     setActionStates();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clearHistory() {
/* 224 */     this.history.clear();
/* 225 */     this.historyPos = -1;
/* 226 */     if (this.descWindowNavBar != null) {
/* 227 */       setActionStates();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clearHistoryAfterCurrentPos() {
/* 236 */     for (int i = this.history.size() - 1; i > this.historyPos; i--) {
/* 237 */       this.history.remove(i);
/*     */     }
/* 239 */     setActionStates();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean copy() {
/* 250 */     if (isVisible() && this.descArea
/* 251 */       .getSelectionStart() != this.descArea.getSelectionEnd()) {
/* 252 */       this.descArea.copy();
/* 253 */       return true;
/*     */     } 
/* 255 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getString(String key) {
/* 266 */     if (this.bundle == null) {
/* 267 */       this.bundle = ResourceBundle.getBundle("org.fife.ui.autocomplete.AutoCompleteDescWindow");
/*     */     }
/* 269 */     return this.bundle.getString(key);
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
/*     */   public void hyperlinkUpdate(HyperlinkEvent e) {
/* 281 */     HyperlinkEvent.EventType type = e.getEventType();
/* 282 */     if (!type.equals(HyperlinkEvent.EventType.ACTIVATED)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 287 */     URL url = e.getURL();
/* 288 */     if (url != null) {
/* 289 */       LinkRedirector redirector = AutoCompletion.getLinkRedirector();
/* 290 */       if (redirector != null) {
/* 291 */         URL newUrl = redirector.possiblyRedirect(url);
/* 292 */         if (newUrl != null && newUrl != url) {
/* 293 */           url = newUrl;
/*     */           
/* 295 */           e = new HyperlinkEvent(e.getSource(), e.getEventType(), newUrl, e.getDescription(), e.getSourceElement());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 301 */     ExternalURLHandler handler = this.ac.getExternalURLHandler();
/* 302 */     if (handler != null) {
/* 303 */       HistoryEntry current = this.history.get(this.historyPos);
/* 304 */       handler.urlClicked(e, current.completion, this);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 309 */     if (url != null) {
/*     */       
/*     */       try {
/* 312 */         Util.browse(new URI(url.toString()));
/* 313 */       } catch (URISyntaxException ioe) {
/* 314 */         UIManager.getLookAndFeel().provideErrorFeedback(this.descArea);
/* 315 */         ioe.printStackTrace();
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 322 */       AutoCompletePopupWindow parent = (AutoCompletePopupWindow)getParent();
/* 323 */       CompletionProvider p = parent.getSelection().getProvider();
/* 324 */       if (p instanceof AbstractCompletionProvider) {
/* 325 */         String name = e.getDescription();
/*     */         
/* 327 */         List<Completion> l = ((AbstractCompletionProvider)p).getCompletionByInputText(name);
/* 328 */         if (l != null && !l.isEmpty()) {
/*     */           
/* 330 */           Completion c = l.get(0);
/* 331 */           setDescriptionFor(c, true);
/*     */         } else {
/*     */           
/* 334 */           UIManager.getLookAndFeel().provideErrorFeedback(this.descArea);
/*     */         } 
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
/*     */   private void setActionStates() {
/* 347 */     String desc = null;
/* 348 */     if (this.historyPos > 0) {
/* 349 */       this.backAction.setEnabled(true);
/* 350 */       desc = "Back to " + this.history.get(this.historyPos - 1);
/*     */     } else {
/*     */       
/* 353 */       this.backAction.setEnabled(false);
/*     */     } 
/* 355 */     this.backAction.putValue("ShortDescription", desc);
/* 356 */     if (this.historyPos > -1 && this.historyPos < this.history.size() - 1) {
/* 357 */       this.forwardAction.setEnabled(true);
/* 358 */       desc = "Forward to " + this.history.get(this.historyPos + 1);
/*     */     } else {
/*     */       
/* 361 */       this.forwardAction.setEnabled(false);
/* 362 */       desc = null;
/*     */     } 
/* 364 */     this.forwardAction.putValue("ShortDescription", desc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDescriptionFor(Completion item) {
/* 375 */     setDescriptionFor(item, false);
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
/*     */   protected void setDescriptionFor(Completion item, boolean addToHistory) {
/* 387 */     setDescriptionFor(item, (String)null, addToHistory);
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
/*     */   protected void setDescriptionFor(Completion item, String anchor, boolean addToHistory) {
/* 401 */     this.timer.stop();
/* 402 */     this.timerAction.setCompletion(item, anchor, addToHistory);
/* 403 */     this.timer.start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setDisplayedDesc(Completion completion, String anchor, boolean addToHistory) {
/* 410 */     String desc = (completion == null) ? null : completion.getSummary();
/* 411 */     if (desc == null) {
/* 412 */       desc = "<html><em>" + getString("NoDescAvailable") + "</em>";
/*     */     }
/* 414 */     this.descArea.setText(desc);
/* 415 */     if (anchor != null) {
/* 416 */       SwingUtilities.invokeLater(() -> this.descArea.scrollToReference(paramString));
/*     */     } else {
/*     */       
/* 419 */       this.descArea.setCaretPosition(0);
/*     */     } 
/*     */     
/* 422 */     if (!addToHistory)
/*     */     {
/*     */       
/* 425 */       clearHistory();
/*     */     }
/* 427 */     addToHistory(new HistoryEntry(completion, desc, null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVisible(boolean visible) {
/* 437 */     if (!visible) {
/* 438 */       clearHistory();
/*     */     }
/* 440 */     super.setVisible(visible);
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
/*     */   public void showSummaryFor(Completion completion, String anchor) {
/* 453 */     setDescriptionFor(completion, anchor, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateUI() {
/* 461 */     SwingUtilities.updateComponentTreeUI(this);
/*     */     
/* 463 */     TipUtil.tweakTipEditorPane(this.descArea);
/* 464 */     this.scrollPane.setBackground(this.descArea.getBackground());
/* 465 */     this.scrollPane.getViewport().setBackground(this.descArea.getBackground());
/* 466 */     ((JPanel)getContentPane()).setBorder(TipUtil.getToolTipBorder());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class HistoryEntry
/*     */   {
/*     */     private Completion completion;
/*     */     
/*     */     private String summary;
/*     */     
/*     */     private String anchor;
/*     */ 
/*     */     
/*     */     HistoryEntry(Completion completion, String summary, String anchor) {
/* 481 */       this.completion = completion;
/* 482 */       this.summary = summary;
/* 483 */       this.anchor = anchor;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 494 */       return this.completion.getInputText();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class TimerAction
/*     */     extends AbstractAction
/*     */   {
/*     */     private Completion completion;
/*     */     
/*     */     private String anchor;
/*     */     
/*     */     private boolean addToHistory;
/*     */ 
/*     */     
/*     */     private TimerAction() {}
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 514 */       AutoCompleteDescWindow.this.setDisplayedDesc(this.completion, this.anchor, this.addToHistory);
/*     */     }
/*     */ 
/*     */     
/*     */     void setCompletion(Completion c, String anchor, boolean addToHistory) {
/* 519 */       this.completion = c;
/* 520 */       this.anchor = anchor;
/* 521 */       this.addToHistory = addToHistory;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class ToolBarBackAction
/*     */     extends AbstractAction
/*     */   {
/*     */     ToolBarBackAction(boolean ltr) {
/* 533 */       String img = "org/fife/ui/autocomplete/arrow_" + (ltr ? "left.png" : "right.png");
/*     */       
/* 535 */       ClassLoader cl = getClass().getClassLoader();
/* 536 */       Icon icon = new ImageIcon(cl.getResource(img));
/* 537 */       putValue("SmallIcon", icon);
/*     */     }
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 542 */       if (AutoCompleteDescWindow.this.historyPos > 0) {
/* 543 */         AutoCompleteDescWindow.HistoryEntry pair = AutoCompleteDescWindow.this.history.get(--AutoCompleteDescWindow.this.historyPos);
/* 544 */         AutoCompleteDescWindow.this.descArea.setText(pair.summary);
/* 545 */         if (pair.anchor != null) {
/*     */           
/* 547 */           AutoCompleteDescWindow.this.descArea.scrollToReference(pair.anchor);
/*     */         } else {
/*     */           
/* 550 */           AutoCompleteDescWindow.this.descArea.setCaretPosition(0);
/*     */         } 
/* 552 */         AutoCompleteDescWindow.this.setActionStates();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class ToolBarForwardAction
/*     */     extends AbstractAction
/*     */   {
/*     */     ToolBarForwardAction(boolean ltr) {
/* 565 */       String img = "org/fife/ui/autocomplete/arrow_" + (ltr ? "right.png" : "left.png");
/*     */       
/* 567 */       ClassLoader cl = getClass().getClassLoader();
/* 568 */       Icon icon = new ImageIcon(cl.getResource(img));
/* 569 */       putValue("SmallIcon", icon);
/*     */     }
/*     */ 
/*     */     
/*     */     public void actionPerformed(ActionEvent e) {
/* 574 */       if (AutoCompleteDescWindow.this.history != null && AutoCompleteDescWindow.this.historyPos < AutoCompleteDescWindow.this.history.size() - 1) {
/* 575 */         AutoCompleteDescWindow.HistoryEntry pair = AutoCompleteDescWindow.this.history.get(++AutoCompleteDescWindow.this.historyPos);
/* 576 */         AutoCompleteDescWindow.this.descArea.setText(pair.summary);
/* 577 */         if (pair.anchor != null) {
/*     */           
/* 579 */           AutoCompleteDescWindow.this.descArea.scrollToReference(pair.anchor);
/*     */         } else {
/*     */           
/* 582 */           AutoCompleteDescWindow.this.descArea.setCaretPosition(0);
/*     */         } 
/* 584 */         AutoCompleteDescWindow.this.setActionStates();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\AutoCompleteDescWindow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */