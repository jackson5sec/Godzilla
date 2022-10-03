/*      */ package org.fife.ui.autocomplete;
/*      */ 
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.CaretEvent;
/*      */ import javax.swing.event.CaretListener;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.event.DocumentListener;
/*      */ import javax.swing.text.AbstractDocument;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import javax.swing.text.Document;
/*      */ import javax.swing.text.Highlighter;
/*      */ import javax.swing.text.JTextComponent;
/*      */ import javax.swing.text.Position;
/*      */ import org.fife.ui.rsyntaxtextarea.DocumentRange;
/*      */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*      */ import org.fife.ui.rtextarea.ChangeableHighlightPainter;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class ParameterizedCompletionContext
/*      */ {
/*      */   private Window parentWindow;
/*      */   private AutoCompletion ac;
/*      */   private ParameterizedCompletion pc;
/*      */   private boolean active;
/*      */   private ParameterizedCompletionDescriptionToolTip tip;
/*      */   private Highlighter.HighlightPainter p;
/*      */   private Highlighter.HighlightPainter endingP;
/*      */   private Highlighter.HighlightPainter paramCopyP;
/*      */   private List<Object> tags;
/*      */   private List<ParamCopyInfo> paramCopyInfos;
/*      */   private transient boolean ignoringDocumentEvents;
/*      */   private Listener listener;
/*      */   private int minPos;
/*      */   private Position maxPos;
/*      */   private Position defaultEndOffs;
/*      */   private int lastSelectedParam;
/*      */   private ParameterizedCompletionChoicesWindow paramChoicesWindow;
/*      */   private String paramPrefix;
/*      */   private Object oldTabKey;
/*      */   private Action oldTabAction;
/*      */   private Object oldShiftTabKey;
/*      */   private Action oldShiftTabAction;
/*      */   private Object oldUpKey;
/*      */   private Action oldUpAction;
/*      */   private Object oldDownKey;
/*      */   private Action oldDownAction;
/*      */   private Object oldEnterKey;
/*      */   private Action oldEnterAction;
/*      */   private Object oldEscapeKey;
/*      */   private Action oldEscapeAction;
/*      */   private Object oldClosingKey;
/*      */   private Action oldClosingAction;
/*      */   private static final String IM_KEY_TAB = "ParamCompKey.Tab";
/*      */   private static final String IM_KEY_SHIFT_TAB = "ParamCompKey.ShiftTab";
/*      */   private static final String IM_KEY_UP = "ParamCompKey.Up";
/*      */   private static final String IM_KEY_DOWN = "ParamCompKey.Down";
/*      */   private static final String IM_KEY_ESCAPE = "ParamCompKey.Escape";
/*      */   private static final String IM_KEY_ENTER = "ParamCompKey.Enter";
/*      */   private static final String IM_KEY_CLOSING = "ParamCompKey.Closing";
/*      */   
/*      */   ParameterizedCompletionContext(Window owner, AutoCompletion ac, ParameterizedCompletion pc) {
/*  172 */     this.parentWindow = owner;
/*  173 */     this.ac = ac;
/*  174 */     this.pc = pc;
/*  175 */     this.listener = new Listener();
/*      */     
/*  177 */     AutoCompletionStyleContext sc = AutoCompletion.getStyleContext();
/*  178 */     this.p = new OutlineHighlightPainter(sc.getParameterOutlineColor());
/*  179 */     this
/*  180 */       .endingP = new OutlineHighlightPainter(sc.getParameterizedCompletionCursorPositionColor());
/*  181 */     this.paramCopyP = (Highlighter.HighlightPainter)new ChangeableHighlightPainter(sc.getParameterCopyColor());
/*  182 */     this.tags = new ArrayList(1);
/*  183 */     this.paramCopyInfos = new ArrayList<>(1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void activate() {
/*  195 */     if (this.active) {
/*      */       return;
/*      */     }
/*      */     
/*  199 */     this.active = true;
/*  200 */     JTextComponent tc = this.ac.getTextComponent();
/*  201 */     this.lastSelectedParam = -1;
/*      */     
/*  203 */     if (this.pc.getShowParameterToolTip()) {
/*  204 */       this.tip = new ParameterizedCompletionDescriptionToolTip(this.parentWindow, this, this.ac, this.pc);
/*      */       
/*      */       try {
/*  207 */         int dot = tc.getCaretPosition();
/*  208 */         Rectangle r = tc.modelToView(dot);
/*  209 */         Point p = new Point(r.x, r.y);
/*  210 */         SwingUtilities.convertPointToScreen(p, tc);
/*  211 */         r.x = p.x;
/*  212 */         r.y = p.y;
/*  213 */         this.tip.setLocationRelativeTo(r);
/*  214 */         this.tip.setVisible(true);
/*  215 */       } catch (BadLocationException ble) {
/*  216 */         UIManager.getLookAndFeel().provideErrorFeedback(tc);
/*  217 */         ble.printStackTrace();
/*  218 */         this.tip = null;
/*      */       } 
/*      */     } 
/*      */     
/*  222 */     this.listener.install(tc);
/*      */     
/*  224 */     if (this.paramChoicesWindow == null) {
/*  225 */       this.paramChoicesWindow = createParamChoicesWindow();
/*      */     }
/*  227 */     this.lastSelectedParam = getCurrentParameterIndex();
/*  228 */     prepareParamChoicesWindow();
/*  229 */     this.paramChoicesWindow.setVisible(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ParameterizedCompletionChoicesWindow createParamChoicesWindow() {
/*  240 */     ParameterizedCompletionChoicesWindow pcw = new ParameterizedCompletionChoicesWindow(this.parentWindow, this.ac, this);
/*      */ 
/*      */     
/*  243 */     pcw.initialize(this.pc);
/*  244 */     return pcw;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void deactivate() {
/*  255 */     if (!this.active) {
/*      */       return;
/*      */     }
/*  258 */     this.active = false;
/*  259 */     this.listener.uninstall();
/*  260 */     if (this.tip != null) {
/*  261 */       this.tip.setVisible(false);
/*      */     }
/*  263 */     if (this.paramChoicesWindow != null) {
/*  264 */       this.paramChoicesWindow.setVisible(false);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getArgumentText(int offs) {
/*  278 */     List<Highlighter.Highlight> paramHighlights = getParameterHighlights();
/*  279 */     if (paramHighlights == null || paramHighlights.size() == 0) {
/*  280 */       return null;
/*      */     }
/*  282 */     for (Highlighter.Highlight h : paramHighlights) {
/*  283 */       if (offs >= h.getStartOffset() && offs <= h.getEndOffset()) {
/*  284 */         int start = h.getStartOffset() + 1;
/*  285 */         int len = h.getEndOffset() - start;
/*  286 */         JTextComponent tc = this.ac.getTextComponent();
/*  287 */         Document doc = tc.getDocument();
/*      */         try {
/*  289 */           return doc.getText(start, len);
/*  290 */         } catch (BadLocationException ble) {
/*  291 */           UIManager.getLookAndFeel().provideErrorFeedback(tc);
/*  292 */           ble.printStackTrace();
/*  293 */           return null;
/*      */         } 
/*      */       } 
/*      */     } 
/*  297 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Highlighter.Highlight getCurrentParameterHighlight() {
/*  310 */     JTextComponent tc = this.ac.getTextComponent();
/*  311 */     int dot = tc.getCaretPosition();
/*  312 */     if (dot > 0) {
/*  313 */       dot--;
/*      */     }
/*      */     
/*  316 */     List<Highlighter.Highlight> paramHighlights = getParameterHighlights();
/*  317 */     for (Highlighter.Highlight h : paramHighlights) {
/*  318 */       if (dot >= h.getStartOffset() && dot < h.getEndOffset()) {
/*  319 */         return h;
/*      */       }
/*      */     } 
/*      */     
/*  323 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int getCurrentParameterIndex() {
/*  330 */     JTextComponent tc = this.ac.getTextComponent();
/*  331 */     int dot = tc.getCaretPosition();
/*  332 */     if (dot > 0) {
/*  333 */       dot--;
/*      */     }
/*      */     
/*  336 */     List<Highlighter.Highlight> paramHighlights = getParameterHighlights();
/*  337 */     for (int i = 0; i < paramHighlights.size(); i++) {
/*  338 */       Highlighter.Highlight h = paramHighlights.get(i);
/*  339 */       if (dot >= h.getStartOffset() && dot < h.getEndOffset()) {
/*  340 */         return i;
/*      */       }
/*      */     } 
/*      */     
/*  344 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int getCurrentParameterStartOffset() {
/*  357 */     Highlighter.Highlight h = getCurrentParameterHighlight();
/*  358 */     return (h != null) ? (h.getStartOffset() + 1) : -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int getFirstHighlight(List<Highlighter.Highlight> highlights) {
/*  373 */     int first = -1;
/*  374 */     Highlighter.Highlight firstH = null;
/*  375 */     for (int i = 0; i < highlights.size(); i++) {
/*  376 */       Highlighter.Highlight h = highlights.get(i);
/*  377 */       if (firstH == null || h.getStartOffset() < firstH.getStartOffset()) {
/*  378 */         firstH = h;
/*  379 */         first = i;
/*      */       } 
/*      */     } 
/*  382 */     return first;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int getLastHighlight(List<Highlighter.Highlight> highlights) {
/*  397 */     int last = -1;
/*  398 */     Highlighter.Highlight lastH = null;
/*  399 */     for (int i = highlights.size() - 1; i >= 0; i--) {
/*  400 */       Highlighter.Highlight h = highlights.get(i);
/*  401 */       if (lastH == null || h.getStartOffset() > lastH.getStartOffset()) {
/*  402 */         lastH = h;
/*  403 */         last = i;
/*      */       } 
/*      */     } 
/*  406 */     return last;
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Highlighter.Highlight> getParameterHighlights() {
/*  411 */     List<Highlighter.Highlight> paramHighlights = new ArrayList<>(2);
/*  412 */     JTextComponent tc = this.ac.getTextComponent();
/*  413 */     Highlighter.Highlight[] highlights = tc.getHighlighter().getHighlights();
/*  414 */     for (Highlighter.Highlight highlight : highlights) {
/*  415 */       Highlighter.HighlightPainter painter = highlight.getPainter();
/*  416 */       if (painter == this.p || painter == this.endingP) {
/*  417 */         paramHighlights.add(highlight);
/*      */       }
/*      */     } 
/*  420 */     return paramHighlights;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean insertSelectedChoice() {
/*  431 */     if (this.paramChoicesWindow != null && this.paramChoicesWindow.isVisible()) {
/*  432 */       String choice = this.paramChoicesWindow.getSelectedChoice();
/*  433 */       if (choice != null) {
/*  434 */         JTextComponent tc = this.ac.getTextComponent();
/*  435 */         Highlighter.Highlight h = getCurrentParameterHighlight();
/*  436 */         if (h != null) {
/*      */           
/*  438 */           tc.setSelectionStart(h.getStartOffset() + 1);
/*  439 */           tc.setSelectionEnd(h.getEndOffset());
/*  440 */           tc.replaceSelection(choice);
/*  441 */           moveToNextParam();
/*      */         } else {
/*      */           
/*  444 */           UIManager.getLookAndFeel().provideErrorFeedback(tc);
/*      */         } 
/*  446 */         return true;
/*      */       } 
/*      */     } 
/*  449 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void installKeyBindings() {
/*  461 */     if (AutoCompletion.getDebug()) {
/*  462 */       System.out.println("CompletionContext: Installing keybindings");
/*      */     }
/*      */     
/*  465 */     JTextComponent tc = this.ac.getTextComponent();
/*  466 */     InputMap im = tc.getInputMap();
/*  467 */     ActionMap am = tc.getActionMap();
/*      */     
/*  469 */     KeyStroke ks = KeyStroke.getKeyStroke(9, 0);
/*  470 */     this.oldTabKey = im.get(ks);
/*  471 */     im.put(ks, "ParamCompKey.Tab");
/*  472 */     this.oldTabAction = am.get("ParamCompKey.Tab");
/*  473 */     am.put("ParamCompKey.Tab", new NextParamAction());
/*      */     
/*  475 */     ks = KeyStroke.getKeyStroke(9, 1);
/*  476 */     this.oldShiftTabKey = im.get(ks);
/*  477 */     im.put(ks, "ParamCompKey.ShiftTab");
/*  478 */     this.oldShiftTabAction = am.get("ParamCompKey.ShiftTab");
/*  479 */     am.put("ParamCompKey.ShiftTab", new PrevParamAction());
/*      */     
/*  481 */     ks = KeyStroke.getKeyStroke(38, 0);
/*  482 */     this.oldUpKey = im.get(ks);
/*  483 */     im.put(ks, "ParamCompKey.Up");
/*  484 */     this.oldUpAction = am.get("ParamCompKey.Up");
/*  485 */     am.put("ParamCompKey.Up", new NextChoiceAction(-1, this.oldUpAction));
/*      */     
/*  487 */     ks = KeyStroke.getKeyStroke(40, 0);
/*  488 */     this.oldDownKey = im.get(ks);
/*  489 */     im.put(ks, "ParamCompKey.Down");
/*  490 */     this.oldDownAction = am.get("ParamCompKey.Down");
/*  491 */     am.put("ParamCompKey.Down", new NextChoiceAction(1, this.oldDownAction));
/*      */     
/*  493 */     ks = KeyStroke.getKeyStroke(10, 0);
/*  494 */     this.oldEnterKey = im.get(ks);
/*  495 */     im.put(ks, "ParamCompKey.Enter");
/*  496 */     this.oldEnterAction = am.get("ParamCompKey.Enter");
/*  497 */     am.put("ParamCompKey.Enter", new GotoEndAction());
/*      */     
/*  499 */     ks = KeyStroke.getKeyStroke(27, 0);
/*  500 */     this.oldEscapeKey = im.get(ks);
/*  501 */     im.put(ks, "ParamCompKey.Escape");
/*  502 */     this.oldEscapeAction = am.get("ParamCompKey.Escape");
/*  503 */     am.put("ParamCompKey.Escape", new HideAction());
/*      */     
/*  505 */     char end = this.pc.getProvider().getParameterListEnd();
/*  506 */     ks = KeyStroke.getKeyStroke(end);
/*  507 */     this.oldClosingKey = im.get(ks);
/*  508 */     im.put(ks, "ParamCompKey.Closing");
/*  509 */     this.oldClosingAction = am.get("ParamCompKey.Closing");
/*  510 */     am.put("ParamCompKey.Closing", new ClosingAction());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void moveToNextParam() {
/*  522 */     JTextComponent tc = this.ac.getTextComponent();
/*  523 */     int dot = tc.getCaretPosition();
/*  524 */     int tagCount = this.tags.size();
/*  525 */     if (tagCount == 0) {
/*  526 */       tc.setCaretPosition(this.maxPos.getOffset());
/*  527 */       deactivate();
/*      */     } 
/*      */     
/*  530 */     Highlighter.Highlight currentNext = null;
/*  531 */     int pos = -1;
/*  532 */     List<Highlighter.Highlight> highlights = getParameterHighlights();
/*  533 */     for (int i = 0; i < highlights.size(); i++) {
/*  534 */       Highlighter.Highlight hl = highlights.get(i);
/*      */ 
/*      */ 
/*      */       
/*  538 */       if (currentNext == null || currentNext.getStartOffset() < dot || (hl
/*  539 */         .getStartOffset() > dot && hl
/*  540 */         .getStartOffset() <= currentNext.getStartOffset())) {
/*  541 */         currentNext = hl;
/*  542 */         pos = i;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  547 */     if (currentNext.getStartOffset() + 1 <= dot) {
/*  548 */       int nextIndex = getFirstHighlight(highlights);
/*  549 */       currentNext = highlights.get(nextIndex);
/*  550 */       pos = 0;
/*      */     } 
/*      */ 
/*      */     
/*  554 */     tc.setSelectionStart(currentNext.getStartOffset() + 1);
/*  555 */     tc.setSelectionEnd(currentNext.getEndOffset());
/*  556 */     updateToolTipText(pos);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void moveToPreviousParam() {
/*  568 */     JTextComponent tc = this.ac.getTextComponent();
/*      */     
/*  570 */     int tagCount = this.tags.size();
/*  571 */     if (tagCount == 0) {
/*  572 */       tc.setCaretPosition(this.maxPos.getOffset());
/*  573 */       deactivate();
/*      */     } 
/*      */     
/*  576 */     int dot = tc.getCaretPosition();
/*  577 */     int selStart = tc.getSelectionStart() - 1;
/*  578 */     Highlighter.Highlight currentPrev = null;
/*  579 */     int pos = 0;
/*  580 */     List<Highlighter.Highlight> highlights = getParameterHighlights();
/*      */     
/*  582 */     for (int i = 0; i < highlights.size(); i++) {
/*  583 */       Highlighter.Highlight h = highlights.get(i);
/*  584 */       if (currentPrev == null || currentPrev.getStartOffset() >= dot || (h
/*  585 */         .getStartOffset() < selStart && (h
/*  586 */         .getStartOffset() > currentPrev.getStartOffset() || pos == this.lastSelectedParam))) {
/*      */         
/*  588 */         currentPrev = h;
/*  589 */         pos = i;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  594 */     int firstIndex = getFirstHighlight(highlights);
/*      */     
/*  596 */     if (pos == firstIndex && this.lastSelectedParam == firstIndex && highlights.size() > 1) {
/*  597 */       pos = getLastHighlight(highlights);
/*  598 */       currentPrev = highlights.get(pos);
/*      */       
/*  600 */       tc.setSelectionStart(currentPrev.getStartOffset() + 1);
/*  601 */       tc.setSelectionEnd(currentPrev.getEndOffset());
/*  602 */       updateToolTipText(pos);
/*      */     }
/*  604 */     else if (currentPrev != null && dot > currentPrev.getStartOffset()) {
/*      */       
/*  606 */       tc.setSelectionStart(currentPrev.getStartOffset() + 1);
/*  607 */       tc.setSelectionEnd(currentPrev.getEndOffset());
/*  608 */       updateToolTipText(pos);
/*      */     } else {
/*      */       
/*  611 */       tc.setCaretPosition(this.maxPos.getOffset());
/*  612 */       deactivate();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void possiblyUpdateParamCopies(Document doc) {
/*  620 */     int index = getCurrentParameterIndex();
/*      */     
/*  622 */     if (index > -1 && index < this.pc.getParamCount()) {
/*      */ 
/*      */       
/*  625 */       ParameterizedCompletion.Parameter param = this.pc.getParam(index);
/*  626 */       if (param.isEndParam()) {
/*  627 */         deactivate();
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  632 */       List<Highlighter.Highlight> paramHighlights = getParameterHighlights();
/*  633 */       Highlighter.Highlight h = paramHighlights.get(index);
/*  634 */       int start = h.getStartOffset() + 1;
/*  635 */       int len = h.getEndOffset() - start;
/*  636 */       String replacement = null;
/*      */       try {
/*  638 */         replacement = doc.getText(start, len);
/*  639 */       } catch (BadLocationException ble) {
/*  640 */         ble.printStackTrace();
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  645 */       for (ParamCopyInfo pci : this.paramCopyInfos) {
/*  646 */         if (pci.paramName.equals(param.getName())) {
/*  647 */           pci.h = replaceHighlightedText(doc, pci.h, replacement);
/*      */         }
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  654 */       deactivate();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void prepareParamChoicesWindow() {
/*  666 */     if (this.paramChoicesWindow != null) {
/*      */       
/*  668 */       int offs = getCurrentParameterStartOffset();
/*  669 */       if (offs == -1) {
/*  670 */         this.paramChoicesWindow.setVisible(false);
/*      */         
/*      */         return;
/*      */       } 
/*  674 */       JTextComponent tc = this.ac.getTextComponent();
/*      */       try {
/*  676 */         Rectangle r = tc.modelToView(offs);
/*  677 */         Point p = new Point(r.x, r.y);
/*  678 */         SwingUtilities.convertPointToScreen(p, tc);
/*  679 */         r.x = p.x;
/*  680 */         r.y = p.y;
/*  681 */         this.paramChoicesWindow.setLocationRelativeTo(r);
/*  682 */       } catch (BadLocationException ble) {
/*  683 */         UIManager.getLookAndFeel().provideErrorFeedback(tc);
/*  684 */         ble.printStackTrace();
/*      */       } 
/*      */ 
/*      */       
/*  688 */       this.paramChoicesWindow.setParameter(this.lastSelectedParam, this.paramPrefix);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void removeParameterHighlights() {
/*  699 */     JTextComponent tc = this.ac.getTextComponent();
/*  700 */     Highlighter h = tc.getHighlighter();
/*  701 */     for (Object tag : this.tags) {
/*  702 */       h.removeHighlight(tag);
/*      */     }
/*  704 */     this.tags.clear();
/*  705 */     for (ParamCopyInfo pci : this.paramCopyInfos) {
/*  706 */       h.removeHighlight(pci.h);
/*      */     }
/*  708 */     this.paramCopyInfos.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Highlighter.Highlight replaceHighlightedText(Document doc, Highlighter.Highlight h, String replacement) {
/*      */     try {
/*  727 */       int start = h.getStartOffset();
/*  728 */       int len = h.getEndOffset() - start;
/*  729 */       Highlighter highlighter = this.ac.getTextComponent().getHighlighter();
/*  730 */       highlighter.removeHighlight(h);
/*      */       
/*  732 */       if (doc instanceof AbstractDocument) {
/*  733 */         ((AbstractDocument)doc).replace(start, len, replacement, null);
/*      */       } else {
/*      */         
/*  736 */         doc.remove(start, len);
/*  737 */         doc.insertString(start, replacement, null);
/*      */       } 
/*      */       
/*  740 */       int newEnd = start + replacement.length();
/*  741 */       h = (Highlighter.Highlight)highlighter.addHighlight(start, newEnd, this.paramCopyP);
/*  742 */       return h;
/*      */     }
/*  744 */     catch (BadLocationException ble) {
/*  745 */       ble.printStackTrace();
/*      */ 
/*      */       
/*  748 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void uninstallKeyBindings() {
/*  760 */     if (AutoCompletion.getDebug()) {
/*  761 */       System.out.println("CompletionContext Uninstalling keybindings");
/*      */     }
/*      */     
/*  764 */     JTextComponent tc = this.ac.getTextComponent();
/*  765 */     InputMap im = tc.getInputMap();
/*  766 */     ActionMap am = tc.getActionMap();
/*      */     
/*  768 */     KeyStroke ks = KeyStroke.getKeyStroke(9, 0);
/*  769 */     im.put(ks, this.oldTabKey);
/*  770 */     am.put("ParamCompKey.Tab", this.oldTabAction);
/*      */     
/*  772 */     ks = KeyStroke.getKeyStroke(9, 1);
/*  773 */     im.put(ks, this.oldShiftTabKey);
/*  774 */     am.put("ParamCompKey.ShiftTab", this.oldShiftTabAction);
/*      */     
/*  776 */     ks = KeyStroke.getKeyStroke(38, 0);
/*  777 */     im.put(ks, this.oldUpKey);
/*  778 */     am.put("ParamCompKey.Up", this.oldUpAction);
/*      */     
/*  780 */     ks = KeyStroke.getKeyStroke(40, 0);
/*  781 */     im.put(ks, this.oldDownKey);
/*  782 */     am.put("ParamCompKey.Down", this.oldDownAction);
/*      */     
/*  784 */     ks = KeyStroke.getKeyStroke(10, 0);
/*  785 */     im.put(ks, this.oldEnterKey);
/*  786 */     am.put("ParamCompKey.Enter", this.oldEnterAction);
/*      */     
/*  788 */     ks = KeyStroke.getKeyStroke(27, 0);
/*  789 */     im.put(ks, this.oldEscapeKey);
/*  790 */     am.put("ParamCompKey.Escape", this.oldEscapeAction);
/*      */     
/*  792 */     char end = this.pc.getProvider().getParameterListEnd();
/*  793 */     ks = KeyStroke.getKeyStroke(end);
/*  794 */     im.put(ks, this.oldClosingKey);
/*  795 */     am.put("ParamCompKey.Closing", this.oldClosingAction);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String updateToolTipText() {
/*  809 */     JTextComponent tc = this.ac.getTextComponent();
/*  810 */     int dot = tc.getSelectionStart();
/*  811 */     int mark = tc.getSelectionEnd();
/*  812 */     int index = -1;
/*  813 */     String paramPrefix = null;
/*      */     
/*  815 */     List<Highlighter.Highlight> paramHighlights = getParameterHighlights();
/*  816 */     for (int i = 0; i < paramHighlights.size(); i++) {
/*  817 */       Highlighter.Highlight h = paramHighlights.get(i);
/*      */       
/*  819 */       int start = h.getStartOffset() + 1;
/*  820 */       if (dot >= start && dot <= h.getEndOffset()) {
/*      */ 
/*      */         
/*      */         try {
/*  824 */           if (dot != start || mark != h.getEndOffset()) {
/*  825 */             paramPrefix = tc.getText(start, dot - start);
/*      */           }
/*  827 */         } catch (BadLocationException ble) {
/*  828 */           ble.printStackTrace();
/*      */         } 
/*  830 */         index = i;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*  835 */     updateToolTipText(index);
/*  836 */     return paramPrefix;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateToolTipText(int selectedParam) {
/*  842 */     if (selectedParam != this.lastSelectedParam) {
/*  843 */       if (this.tip != null) {
/*  844 */         this.tip.updateText(selectedParam);
/*      */       }
/*  846 */       this.lastSelectedParam = selectedParam;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateUI() {
/*  856 */     if (this.tip != null) {
/*  857 */       this.tip.updateUI();
/*      */     }
/*  859 */     if (this.paramChoicesWindow != null) {
/*  860 */       this.paramChoicesWindow.updateUI();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class GotoEndAction
/*      */     extends AbstractAction
/*      */   {
/*      */     private GotoEndAction() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformed(ActionEvent e) {
/*  875 */       if (ParameterizedCompletionContext.this.paramChoicesWindow != null && ParameterizedCompletionContext.this.paramChoicesWindow.isVisible() && 
/*  876 */         ParameterizedCompletionContext.this.insertSelectedChoice()) {
/*      */         return;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  882 */       ParameterizedCompletionContext.this.deactivate();
/*  883 */       JTextComponent tc = ParameterizedCompletionContext.this.ac.getTextComponent();
/*  884 */       int dot = tc.getCaretPosition();
/*  885 */       if (dot != ParameterizedCompletionContext.this.defaultEndOffs.getOffset()) {
/*  886 */         tc.setCaretPosition(ParameterizedCompletionContext.this.defaultEndOffs.getOffset());
/*      */       }
/*      */       else {
/*      */         
/*  890 */         Action a = getDefaultEnterAction(tc);
/*  891 */         if (a != null) {
/*  892 */           a.actionPerformed(e);
/*      */         } else {
/*      */           
/*  895 */           tc.replaceSelection("\n");
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private Action getDefaultEnterAction(JTextComponent tc) {
/*  902 */       ActionMap am = tc.getActionMap();
/*  903 */       return am.get("insert-break");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class ClosingAction
/*      */     extends AbstractAction
/*      */   {
/*      */     private ClosingAction() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformed(ActionEvent e) {
/*  918 */       JTextComponent tc = ParameterizedCompletionContext.this.ac.getTextComponent();
/*  919 */       int dot = tc.getCaretPosition();
/*  920 */       char end = ParameterizedCompletionContext.this.pc.getProvider().getParameterListEnd();
/*      */ 
/*      */       
/*  923 */       if (dot >= ParameterizedCompletionContext.this.maxPos.getOffset() - 2) {
/*      */ 
/*      */ 
/*      */         
/*  927 */         String text = ParameterizedCompletionContext.this.getArgumentText(dot);
/*  928 */         if (text != null) {
/*  929 */           char start = ParameterizedCompletionContext.this.pc.getProvider().getParameterListStart();
/*  930 */           int startCount = getCount(text, start);
/*  931 */           int endCount = getCount(text, end);
/*  932 */           if (startCount > endCount) {
/*  933 */             tc.replaceSelection(Character.toString(end));
/*      */             
/*      */             return;
/*      */           } 
/*      */         } 
/*  938 */         tc.setCaretPosition(Math.min(tc.getCaretPosition() + 1, tc
/*  939 */               .getDocument().getLength()));
/*      */         
/*  941 */         ParameterizedCompletionContext.this.deactivate();
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/*  947 */         tc.replaceSelection(Character.toString(end));
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int getCount(String text, char ch) {
/*  953 */       int count = 0;
/*  954 */       int old = 0;
/*      */       int pos;
/*  956 */       while ((pos = text.indexOf(ch, old)) > -1) {
/*  957 */         count++;
/*  958 */         old = pos + 1;
/*      */       } 
/*      */       
/*  961 */       return count;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class HideAction
/*      */     extends AbstractAction
/*      */   {
/*      */     private HideAction() {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformed(ActionEvent e) {
/*  978 */       if (ParameterizedCompletionContext.this.paramChoicesWindow != null && ParameterizedCompletionContext.this.paramChoicesWindow.isVisible()) {
/*  979 */         ParameterizedCompletionContext.this.paramChoicesWindow.setVisible(false);
/*  980 */         ParameterizedCompletionContext.this.paramChoicesWindow = null;
/*      */       } else {
/*      */         
/*  983 */         ParameterizedCompletionContext.this.deactivate();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class Listener
/*      */     implements FocusListener, CaretListener, DocumentListener
/*      */   {
/*      */     private boolean markOccurrencesEnabled;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Listener() {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void caretUpdate(CaretEvent e) {
/* 1006 */       if (ParameterizedCompletionContext.this.maxPos == null) {
/* 1007 */         ParameterizedCompletionContext.this.deactivate();
/*      */         return;
/*      */       } 
/* 1010 */       int dot = e.getDot();
/* 1011 */       if (dot < ParameterizedCompletionContext.this.minPos || dot > ParameterizedCompletionContext.this.maxPos.getOffset()) {
/* 1012 */         ParameterizedCompletionContext.this.deactivate();
/*      */         return;
/*      */       } 
/* 1015 */       ParameterizedCompletionContext.this.paramPrefix = ParameterizedCompletionContext.this.updateToolTipText();
/* 1016 */       if (ParameterizedCompletionContext.this.active) {
/* 1017 */         ParameterizedCompletionContext.this.prepareParamChoicesWindow();
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void changedUpdate(DocumentEvent e) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void focusGained(FocusEvent e) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void focusLost(FocusEvent e) {
/* 1045 */       ParameterizedCompletionContext.this.deactivate();
/*      */     }
/*      */ 
/*      */     
/*      */     private void handleDocumentEvent(DocumentEvent e) {
/* 1050 */       if (!ParameterizedCompletionContext.this.ignoringDocumentEvents) {
/* 1051 */         ParameterizedCompletionContext.this.ignoringDocumentEvents = true;
/* 1052 */         SwingUtilities.invokeLater(() -> {
/*      */               ParameterizedCompletionContext.this.possiblyUpdateParamCopies(param1DocumentEvent.getDocument());
/*      */               ParameterizedCompletionContext.this.ignoringDocumentEvents = false;
/*      */             });
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void insertUpdate(DocumentEvent e) {
/* 1062 */       handleDocumentEvent(e);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void install(JTextComponent tc) {
/* 1074 */       boolean replaceTabs = false;
/* 1075 */       if (tc instanceof RSyntaxTextArea) {
/* 1076 */         RSyntaxTextArea textArea = (RSyntaxTextArea)tc;
/* 1077 */         this.markOccurrencesEnabled = textArea.getMarkOccurrences();
/* 1078 */         textArea.setMarkOccurrences(false);
/* 1079 */         replaceTabs = textArea.getTabsEmulated();
/*      */       } 
/*      */       
/* 1082 */       Highlighter h = tc.getHighlighter();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 1088 */         ParameterizedCompletionInsertionInfo info = ParameterizedCompletionContext.this.pc.getInsertionInfo(tc, replaceTabs);
/* 1089 */         tc.replaceSelection(info.getTextToInsert());
/*      */ 
/*      */         
/* 1092 */         int replacementCount = info.getReplacementCount(); int i;
/* 1093 */         for (i = 0; i < replacementCount; i++) {
/* 1094 */           DocumentRange dr = info.getReplacementLocation(i);
/* 1095 */           Highlighter.HighlightPainter painter = (i < replacementCount - 1) ? ParameterizedCompletionContext.this.p : ParameterizedCompletionContext.this.endingP;
/*      */           
/* 1097 */           ParameterizedCompletionContext.this.tags.add(h.addHighlight(dr
/* 1098 */                 .getStartOffset() - 1, dr.getEndOffset(), painter));
/*      */         } 
/* 1100 */         for (i = 0; i < info.getReplacementCopyCount(); i++) {
/* 1101 */           ParameterizedCompletionInsertionInfo.ReplacementCopy rc = info.getReplacementCopy(i);
/* 1102 */           ParameterizedCompletionContext.this.paramCopyInfos.add(new ParameterizedCompletionContext.ParamCopyInfo(rc.getId(), (Highlighter.Highlight)h
/* 1103 */                 .addHighlight(rc.getStart(), rc.getEnd(), ParameterizedCompletionContext.this
/* 1104 */                   .paramCopyP)));
/*      */         } 
/*      */ 
/*      */         
/* 1108 */         tc.setCaretPosition(info.getSelectionStart());
/* 1109 */         if (info.hasSelection()) {
/* 1110 */           tc.moveCaretPosition(info.getSelectionEnd());
/*      */         }
/*      */         
/* 1113 */         ParameterizedCompletionContext.this.minPos = info.getMinOffset();
/* 1114 */         ParameterizedCompletionContext.this.maxPos = info.getMaxOffset();
/*      */         try {
/* 1116 */           Document doc = tc.getDocument();
/* 1117 */           if (ParameterizedCompletionContext.this.maxPos.getOffset() == 0)
/*      */           {
/*      */ 
/*      */             
/* 1121 */             ParameterizedCompletionContext.this.maxPos = doc.createPosition(info
/* 1122 */                 .getTextToInsert().length());
/*      */           }
/* 1124 */           ParameterizedCompletionContext.this.defaultEndOffs = doc.createPosition(info
/* 1125 */               .getDefaultEndOffs());
/* 1126 */         } catch (BadLocationException ble) {
/* 1127 */           ble.printStackTrace();
/*      */         } 
/*      */ 
/*      */         
/* 1131 */         tc.getDocument().addDocumentListener(this);
/*      */       }
/* 1133 */       catch (BadLocationException ble) {
/* 1134 */         ble.printStackTrace();
/*      */       } 
/*      */ 
/*      */       
/* 1138 */       tc.addCaretListener(this);
/* 1139 */       tc.addFocusListener(this);
/* 1140 */       ParameterizedCompletionContext.this.installKeyBindings();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void removeUpdate(DocumentEvent e) {
/* 1147 */       handleDocumentEvent(e);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void uninstall() {
/* 1156 */       JTextComponent tc = ParameterizedCompletionContext.this.ac.getTextComponent();
/* 1157 */       tc.removeCaretListener(this);
/* 1158 */       tc.removeFocusListener(this);
/* 1159 */       tc.getDocument().removeDocumentListener(this);
/* 1160 */       ParameterizedCompletionContext.this.uninstallKeyBindings();
/*      */       
/* 1162 */       if (this.markOccurrencesEnabled) {
/* 1163 */         ((RSyntaxTextArea)tc).setMarkOccurrences(this.markOccurrencesEnabled);
/*      */       }
/*      */ 
/*      */       
/* 1167 */       ParameterizedCompletionContext.this.maxPos = null;
/* 1168 */       ParameterizedCompletionContext.this.minPos = -1;
/* 1169 */       ParameterizedCompletionContext.this.removeParameterHighlights();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class NextChoiceAction
/*      */     extends AbstractAction
/*      */   {
/*      */     private Action oldAction;
/*      */ 
/*      */     
/*      */     private int amount;
/*      */ 
/*      */ 
/*      */     
/*      */     NextChoiceAction(int amount, Action oldAction) {
/* 1187 */       this.amount = amount;
/* 1188 */       this.oldAction = oldAction;
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformed(ActionEvent e) {
/* 1193 */       if (ParameterizedCompletionContext.this.paramChoicesWindow != null && ParameterizedCompletionContext.this.paramChoicesWindow.isVisible()) {
/* 1194 */         ParameterizedCompletionContext.this.paramChoicesWindow.incSelection(this.amount);
/*      */       }
/* 1196 */       else if (this.oldAction != null) {
/* 1197 */         this.oldAction.actionPerformed(e);
/*      */       } else {
/*      */         
/* 1200 */         ParameterizedCompletionContext.this.deactivate();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class NextParamAction
/*      */     extends AbstractAction
/*      */   {
/*      */     private NextParamAction() {}
/*      */ 
/*      */     
/*      */     public void actionPerformed(ActionEvent e) {
/* 1214 */       ParameterizedCompletionContext.this.moveToNextParam();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class ParamCopyInfo
/*      */   {
/*      */     private String paramName;
/*      */ 
/*      */     
/*      */     private Highlighter.Highlight h;
/*      */ 
/*      */     
/*      */     ParamCopyInfo(String paramName, Highlighter.Highlight h) {
/* 1229 */       this.paramName = paramName;
/* 1230 */       this.h = h;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class PrevParamAction
/*      */     extends AbstractAction
/*      */   {
/*      */     private PrevParamAction() {}
/*      */ 
/*      */     
/*      */     public void actionPerformed(ActionEvent e) {
/* 1243 */       ParameterizedCompletionContext.this.moveToPreviousParam();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\ParameterizedCompletionContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */