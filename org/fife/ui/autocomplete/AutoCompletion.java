/*      */ package org.fife.ui.autocomplete;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.ComponentAdapter;
/*      */ import java.awt.event.ComponentEvent;
/*      */ import java.awt.event.FocusAdapter;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.HierarchyEvent;
/*      */ import java.awt.event.HierarchyListener;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.awt.event.WindowFocusListener;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.util.List;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.ListCellRenderer;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.Timer;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.CaretEvent;
/*      */ import javax.swing.event.CaretListener;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.event.DocumentListener;
/*      */ import javax.swing.event.EventListenerList;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import javax.swing.text.Caret;
/*      */ import javax.swing.text.Document;
/*      */ import javax.swing.text.Element;
/*      */ import javax.swing.text.JTextComponent;
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
/*      */ public class AutoCompletion
/*      */ {
/*      */   private JTextComponent textComponent;
/*      */   private Window parentWindow;
/*      */   private AutoCompletePopupWindow popupWindow;
/*      */   private Dimension preferredChoicesWindowSize;
/*      */   private Dimension preferredDescWindowSize;
/*      */   private Color descWindowColor;
/*      */   private ParameterizedCompletionContext pcc;
/*      */   private CompletionProvider provider;
/*      */   private ListCellRenderer<Object> renderer;
/*      */   private ExternalURLHandler externalURLHandler;
/*      */   private static LinkRedirector linkRedirector;
/*      */   private boolean showDescWindow;
/*      */   private boolean autoCompleteEnabled;
/*      */   private boolean autoActivationEnabled;
/*      */   private boolean autoCompleteSingleChoices;
/*      */   private boolean parameterAssistanceEnabled;
/*      */   private int parameterDescriptionTruncateThreshold;
/*      */   private ListCellRenderer<Object> paramChoicesRenderer;
/*      */   private KeyStroke trigger;
/*      */   private Object oldTriggerKey;
/*      */   private Action oldTriggerAction;
/*      */   private Object oldParenKey;
/*      */   private Action oldParenAction;
/*      */   private ParentWindowListener parentWindowListener;
/*      */   private TextComponentListener textComponentListener;
/*      */   private AutoActivationListener autoActivationListener;
/*      */   private LookAndFeelChangeListener lafListener;
/*      */   private PopupWindowListener popupWindowListener;
/*      */   private EventListenerList listeners;
/*      */   private boolean hideOnNoText;
/*      */   private boolean hideOnCompletionProviderChange;
/*      */   private static final String PARAM_TRIGGER_KEY = "AutoComplete";
/*      */   private static final String PARAM_COMPLETE_KEY = "AutoCompletion.FunctionStart";
/*  243 */   private static final AutoCompletionStyleContext STYLE_CONTEXT = new AutoCompletionStyleContext();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  249 */   private static final boolean DEBUG = initDebug();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public AutoCompletion(CompletionProvider provider) {
/*  259 */     setChoicesWindowSize(350, 200);
/*  260 */     setDescriptionWindowSize(350, 250);
/*      */     
/*  262 */     setCompletionProvider(provider);
/*  263 */     setTriggerKey(getDefaultTriggerKey());
/*  264 */     setAutoCompleteEnabled(true);
/*  265 */     setAutoCompleteSingleChoices(true);
/*  266 */     setAutoActivationEnabled(false);
/*  267 */     setShowDescWindow(false);
/*  268 */     setHideOnCompletionProviderChange(true);
/*  269 */     setHideOnNoText(true);
/*  270 */     setParameterDescriptionTruncateThreshold(300);
/*  271 */     this.parentWindowListener = new ParentWindowListener();
/*  272 */     this.textComponentListener = new TextComponentListener();
/*  273 */     this.autoActivationListener = new AutoActivationListener();
/*  274 */     this.lafListener = new LookAndFeelChangeListener();
/*  275 */     this.popupWindowListener = new PopupWindowListener();
/*  276 */     this.listeners = new EventListenerList();
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
/*      */   public void addAutoCompletionListener(AutoCompletionListener l) {
/*  288 */     this.listeners.add(AutoCompletionListener.class, l);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void doCompletion() {
/*  297 */     refreshPopupWindow();
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
/*      */   protected void fireAutoCompletionEvent(AutoCompletionEvent.Type type) {
/*  309 */     Object[] listeners = this.listeners.getListenerList();
/*  310 */     AutoCompletionEvent e = null;
/*      */ 
/*      */ 
/*      */     
/*  314 */     for (int i = listeners.length - 2; i >= 0; i -= 2) {
/*  315 */       if (listeners[i] == AutoCompletionListener.class) {
/*  316 */         if (e == null) {
/*  317 */           e = new AutoCompletionEvent(this, type);
/*      */         }
/*  319 */         ((AutoCompletionListener)listeners[i + 1]).autoCompleteUpdate(e);
/*      */       } 
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
/*      */   public int getAutoActivationDelay() {
/*  334 */     return this.autoActivationListener.timer.getDelay();
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
/*      */   public boolean getAutoCompleteSingleChoices() {
/*  346 */     return this.autoCompleteSingleChoices;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompletionProvider getCompletionProvider() {
/*  356 */     return this.provider;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean getDebug() {
/*  366 */     return DEBUG;
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
/*      */   public static KeyStroke getDefaultTriggerKey() {
/*  378 */     int mask = 2;
/*  379 */     return KeyStroke.getKeyStroke(32, mask);
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
/*      */   public ExternalURLHandler getExternalURLHandler() {
/*  392 */     return this.externalURLHandler;
/*      */   }
/*      */ 
/*      */   
/*      */   int getLineOfCaret() {
/*  397 */     Document doc = this.textComponent.getDocument();
/*  398 */     Element root = doc.getDefaultRootElement();
/*  399 */     return root.getElementIndex(this.textComponent.getCaretPosition());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static LinkRedirector getLinkRedirector() {
/*  410 */     return linkRedirector;
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
/*      */   public ListCellRenderer getListCellRenderer() {
/*  422 */     return this.renderer;
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
/*      */   public ListCellRenderer<Object> getParamChoicesRenderer() {
/*  437 */     return this.paramChoicesRenderer;
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
/*      */   
/*      */   protected String getReplacementText(Completion c, Document doc, int start, int len) {
/*  456 */     return c.getReplacementText();
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
/*      */   public boolean getShowDescWindow() {
/*  468 */     return this.showDescWindow;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static AutoCompletionStyleContext getStyleContext() {
/*  479 */     return STYLE_CONTEXT;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Color getDescWindowColor() {
/*  489 */     return this.descWindowColor;
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
/*      */   public int getParameterDescriptionTruncateThreshold() {
/*  501 */     return this.parameterDescriptionTruncateThreshold;
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
/*      */   public JTextComponent getTextComponent() {
/*  513 */     return this.textComponent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ComponentOrientation getTextComponentOrientation() {
/*  524 */     return (this.textComponent == null) ? null : this.textComponent
/*  525 */       .getComponentOrientation();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public KeyStroke getTriggerKey() {
/*  536 */     return this.trigger;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hideChildWindows() {
/*  547 */     boolean res = hidePopupWindow();
/*  548 */     res |= hideParameterCompletionPopups();
/*  549 */     return res;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean hideParameterCompletionPopups() {
/*  559 */     if (this.pcc != null) {
/*  560 */       this.pcc.deactivate();
/*  561 */       this.pcc = null;
/*  562 */       return true;
/*      */     } 
/*  564 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean hidePopupWindow() {
/*  574 */     if (this.popupWindow != null && 
/*  575 */       this.popupWindow.isVisible()) {
/*  576 */       setPopupVisible(false);
/*  577 */       return true;
/*      */     } 
/*      */     
/*  580 */     return false;
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
/*      */   private static boolean initDebug() {
/*      */     boolean debug;
/*      */     try {
/*  594 */       debug = Boolean.getBoolean("AutoCompletion.debug");
/*  595 */     } catch (SecurityException se) {
/*  596 */       debug = false;
/*      */     } 
/*  598 */     return debug;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void insertCompletion(Completion c) {
/*  609 */     insertCompletion(c, false);
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
/*      */   protected void insertCompletion(Completion c, boolean typedParamListStartChar) {
/*  624 */     JTextComponent textComp = getTextComponent();
/*  625 */     String alreadyEntered = c.getAlreadyEntered(textComp);
/*  626 */     hidePopupWindow();
/*  627 */     Caret caret = textComp.getCaret();
/*      */     
/*  629 */     int dot = caret.getDot();
/*  630 */     int len = alreadyEntered.length();
/*  631 */     int start = dot - len;
/*  632 */     String replacement = getReplacementText(c, textComp.getDocument(), start, len);
/*      */ 
/*      */     
/*  635 */     caret.setDot(start);
/*  636 */     caret.moveDot(dot);
/*  637 */     textComp.replaceSelection(replacement);
/*      */     
/*  639 */     if (isParameterAssistanceEnabled() && c instanceof ParameterizedCompletion) {
/*      */       
/*  641 */       ParameterizedCompletion pc = (ParameterizedCompletion)c;
/*  642 */       startParameterizedCompletionAssistance(pc, typedParamListStartChar);
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
/*      */ 
/*      */   
/*      */   public void install(JTextComponent c) {
/*  658 */     if (this.textComponent != null) {
/*  659 */       uninstall();
/*      */     }
/*      */     
/*  662 */     this.textComponent = c;
/*  663 */     installTriggerKey(getTriggerKey());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  671 */     char start = this.provider.getParameterListStart();
/*  672 */     if (start != '\000' && start != ' ') {
/*  673 */       InputMap im = c.getInputMap();
/*  674 */       ActionMap am = c.getActionMap();
/*  675 */       KeyStroke ks = KeyStroke.getKeyStroke(start);
/*  676 */       this.oldParenKey = im.get(ks);
/*  677 */       im.put(ks, "AutoCompletion.FunctionStart");
/*  678 */       this.oldParenAction = am.get("AutoCompletion.FunctionStart");
/*  679 */       am.put("AutoCompletion.FunctionStart", new ParameterizedCompletionStartAction(start));
/*      */     } 
/*      */ 
/*      */     
/*  683 */     this.textComponentListener.addTo(this.textComponent);
/*      */     
/*  685 */     this.textComponentListener.hierarchyChanged(null);
/*      */     
/*  687 */     if (isAutoActivationEnabled()) {
/*  688 */       this.autoActivationListener.addTo(this.textComponent);
/*      */     }
/*      */     
/*  691 */     UIManager.addPropertyChangeListener(this.lafListener);
/*  692 */     updateUI();
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
/*      */   private void installTriggerKey(KeyStroke ks) {
/*  704 */     InputMap im = this.textComponent.getInputMap();
/*  705 */     this.oldTriggerKey = im.get(ks);
/*  706 */     im.put(ks, "AutoComplete");
/*  707 */     ActionMap am = this.textComponent.getActionMap();
/*  708 */     this.oldTriggerAction = am.get("AutoComplete");
/*  709 */     am.put("AutoComplete", createAutoCompleteAction());
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
/*      */   protected Action createAutoCompleteAction() {
/*  723 */     return new AutoCompleteAction();
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
/*      */   public boolean isAutoActivationEnabled() {
/*  739 */     return this.autoActivationEnabled;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAutoCompleteEnabled() {
/*  750 */     return this.autoCompleteEnabled;
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
/*      */   protected boolean isHideOnCompletionProviderChange() {
/*  764 */     return this.hideOnCompletionProviderChange;
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
/*      */   protected boolean isHideOnNoText() {
/*  777 */     return this.hideOnNoText;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isParameterAssistanceEnabled() {
/*  788 */     return this.parameterAssistanceEnabled;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPopupVisible() {
/*  798 */     return (this.popupWindow != null && this.popupWindow.isVisible());
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
/*      */   protected int refreshPopupWindow() {
/*  815 */     String text = this.provider.getAlreadyEnteredText(this.textComponent);
/*  816 */     if (text == null && !isPopupVisible()) {
/*  817 */       return getLineOfCaret();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  824 */     int textLen = (text == null) ? 0 : text.length();
/*  825 */     if (textLen == 0 && isHideOnNoText() && 
/*  826 */       isPopupVisible()) {
/*  827 */       hidePopupWindow();
/*  828 */       return getLineOfCaret();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  833 */     List<Completion> completions = this.provider.getCompletions(this.textComponent);
/*  834 */     int count = (completions == null) ? 0 : completions.size();
/*      */     
/*  836 */     if (count > 1 || (count == 1 && (isPopupVisible() || textLen == 0)) || (count == 1 && 
/*  837 */       !getAutoCompleteSingleChoices())) {
/*      */       
/*  839 */       if (this.popupWindow == null) {
/*  840 */         this.popupWindow = new AutoCompletePopupWindow(this.parentWindow, this);
/*  841 */         this.popupWindowListener.install(this.popupWindow);
/*      */ 
/*      */ 
/*      */         
/*  845 */         this.popupWindow
/*  846 */           .applyComponentOrientation(getTextComponentOrientation());
/*  847 */         if (this.renderer != null) {
/*  848 */           this.popupWindow.setListCellRenderer(this.renderer);
/*      */         }
/*  850 */         if (this.preferredChoicesWindowSize != null) {
/*  851 */           this.popupWindow.setSize(this.preferredChoicesWindowSize);
/*      */         }
/*  853 */         if (this.preferredDescWindowSize != null) {
/*  854 */           this.popupWindow
/*  855 */             .setDescriptionWindowSize(this.preferredDescWindowSize);
/*      */         }
/*      */       } 
/*      */       
/*  859 */       this.popupWindow.setCompletions(completions);
/*      */       
/*  861 */       if (!this.popupWindow.isVisible()) {
/*      */         Rectangle r;
/*      */         try {
/*  864 */           r = this.textComponent.modelToView(this.textComponent
/*  865 */               .getCaretPosition());
/*  866 */         } catch (BadLocationException ble) {
/*  867 */           ble.printStackTrace();
/*  868 */           return -1;
/*      */         } 
/*  870 */         Point p = new Point(r.x, r.y);
/*  871 */         SwingUtilities.convertPointToScreen(p, this.textComponent);
/*  872 */         r.x = p.x;
/*  873 */         r.y = p.y;
/*  874 */         this.popupWindow.setLocationRelativeTo(r);
/*  875 */         setPopupVisible(true);
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  880 */     else if (count == 1) {
/*  881 */       SwingUtilities.invokeLater(() -> insertCompletion(paramList.get(0)));
/*      */     }
/*      */     else {
/*      */       
/*  885 */       hidePopupWindow();
/*      */     } 
/*      */     
/*  888 */     return getLineOfCaret();
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
/*      */   public void removeAutoCompletionListener(AutoCompletionListener l) {
/*  900 */     this.listeners.remove(AutoCompletionListener.class, l);
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
/*      */   public void setAutoActivationDelay(int ms) {
/*  912 */     ms = Math.max(0, ms);
/*  913 */     this.autoActivationListener.timer.stop();
/*  914 */     this.autoActivationListener.timer.setInitialDelay(ms);
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
/*      */   public void setAutoActivationEnabled(boolean enabled) {
/*  927 */     if (enabled != this.autoActivationEnabled) {
/*  928 */       this.autoActivationEnabled = enabled;
/*  929 */       if (this.textComponent != null) {
/*  930 */         if (this.autoActivationEnabled) {
/*  931 */           this.autoActivationListener.addTo(this.textComponent);
/*      */         } else {
/*      */           
/*  934 */           this.autoActivationListener.removeFrom(this.textComponent);
/*      */         } 
/*      */       }
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
/*      */   public void setAutoCompleteEnabled(boolean enabled) {
/*  948 */     if (enabled != this.autoCompleteEnabled) {
/*  949 */       this.autoCompleteEnabled = enabled;
/*  950 */       hidePopupWindow();
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
/*      */   public void setAutoCompleteSingleChoices(boolean autoComplete) {
/*  963 */     this.autoCompleteSingleChoices = autoComplete;
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
/*      */   public void setCompletionProvider(CompletionProvider provider) {
/*  976 */     if (provider == null) {
/*  977 */       throw new IllegalArgumentException("provider cannot be null");
/*      */     }
/*  979 */     this.provider = provider;
/*  980 */     if (isHideOnCompletionProviderChange()) {
/*  981 */       hidePopupWindow();
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
/*      */   public void setChoicesWindowSize(int w, int h) {
/*  994 */     this.preferredChoicesWindowSize = new Dimension(w, h);
/*  995 */     if (this.popupWindow != null) {
/*  996 */       this.popupWindow.setSize(this.preferredChoicesWindowSize);
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
/*      */   public void setDescriptionWindowSize(int w, int h) {
/* 1009 */     this.preferredDescWindowSize = new Dimension(w, h);
/* 1010 */     if (this.popupWindow != null) {
/* 1011 */       this.popupWindow.setDescriptionWindowSize(this.preferredDescWindowSize);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDescriptionWindowColor(Color c) {
/* 1021 */     this.descWindowColor = c;
/* 1022 */     if (this.popupWindow != null) {
/* 1023 */       this.popupWindow.setDescriptionWindowColor(this.descWindowColor);
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
/*      */ 
/*      */ 
/*      */   
/*      */   public void setExternalURLHandler(ExternalURLHandler handler) {
/* 1040 */     this.externalURLHandler = handler;
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
/*      */   protected void setHideOnCompletionProviderChange(boolean hideOnCompletionProviderChange) {
/* 1055 */     this.hideOnCompletionProviderChange = hideOnCompletionProviderChange;
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
/*      */   protected void setHideOnNoText(boolean hideOnNoText) {
/* 1068 */     this.hideOnNoText = hideOnNoText;
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
/*      */   public static void setLinkRedirector(LinkRedirector linkRedirector) {
/* 1082 */     AutoCompletion.linkRedirector = linkRedirector;
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
/*      */   public void setListCellRenderer(ListCellRenderer<Object> renderer) {
/* 1095 */     this.renderer = renderer;
/* 1096 */     if (this.popupWindow != null) {
/* 1097 */       this.popupWindow.setListCellRenderer(renderer);
/* 1098 */       hidePopupWindow();
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
/*      */ 
/*      */   
/*      */   public void setParamChoicesRenderer(ListCellRenderer<Object> r) {
/* 1114 */     this.paramChoicesRenderer = r;
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
/*      */   public void setParameterAssistanceEnabled(boolean enabled) {
/* 1129 */     this.parameterAssistanceEnabled = enabled;
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
/*      */   protected void setPopupVisible(boolean visible) {
/* 1141 */     if (visible != this.popupWindow.isVisible()) {
/* 1142 */       this.popupWindow.setVisible(visible);
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
/*      */   public void setShowDescWindow(boolean show) {
/* 1155 */     hidePopupWindow();
/* 1156 */     this.showDescWindow = show;
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
/*      */   public void setTriggerKey(KeyStroke ks) {
/* 1169 */     if (ks == null) {
/* 1170 */       throw new IllegalArgumentException("trigger key cannot be null");
/*      */     }
/* 1172 */     if (!ks.equals(this.trigger)) {
/* 1173 */       if (this.textComponent != null) {
/*      */         
/* 1175 */         uninstallTriggerKey();
/*      */         
/* 1177 */         installTriggerKey(ks);
/*      */       } 
/* 1179 */       this.trigger = ks;
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
/*      */ 
/*      */   
/*      */   private void startParameterizedCompletionAssistance(ParameterizedCompletion pc, boolean typedParamListStartChar) {
/* 1195 */     hideParameterCompletionPopups();
/*      */ 
/*      */ 
/*      */     
/* 1199 */     if (pc.getParamCount() == 0 && !(pc instanceof TemplateCompletion)) {
/* 1200 */       CompletionProvider p = pc.getProvider();
/* 1201 */       char end = p.getParameterListEnd();
/* 1202 */       String text = (end == '\000') ? "" : Character.toString(end);
/* 1203 */       if (typedParamListStartChar) {
/* 1204 */         String template = "${}" + text + "${cursor}";
/* 1205 */         this.textComponent.replaceSelection(Character.toString(p
/* 1206 */               .getParameterListStart()));
/* 1207 */         TemplateCompletion tc = new TemplateCompletion(p, null, null, template);
/*      */         
/* 1209 */         pc = tc;
/*      */       } else {
/*      */         
/* 1212 */         text = p.getParameterListStart() + text;
/* 1213 */         this.textComponent.replaceSelection(text);
/*      */         
/*      */         return;
/*      */       } 
/*      */     } 
/* 1218 */     this.pcc = new ParameterizedCompletionContext(this.parentWindow, this, pc);
/* 1219 */     this.pcc.activate();
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
/*      */   public void uninstall() {
/* 1232 */     if (this.textComponent != null) {
/*      */       
/* 1234 */       hidePopupWindow();
/*      */       
/* 1236 */       uninstallTriggerKey();
/*      */ 
/*      */       
/* 1239 */       char start = this.provider.getParameterListStart();
/* 1240 */       if (start != '\000') {
/* 1241 */         KeyStroke ks = KeyStroke.getKeyStroke(start);
/* 1242 */         InputMap im = this.textComponent.getInputMap();
/* 1243 */         im.put(ks, this.oldParenKey);
/* 1244 */         ActionMap am = this.textComponent.getActionMap();
/* 1245 */         am.put("AutoCompletion.FunctionStart", this.oldParenAction);
/*      */       } 
/*      */       
/* 1248 */       this.textComponentListener.removeFrom(this.textComponent);
/* 1249 */       if (this.parentWindow != null) {
/* 1250 */         this.parentWindowListener.removeFrom(this.parentWindow);
/*      */       }
/*      */       
/* 1253 */       if (isAutoActivationEnabled()) {
/* 1254 */         this.autoActivationListener.removeFrom(this.textComponent);
/*      */       }
/*      */       
/* 1257 */       UIManager.removePropertyChangeListener(this.lafListener);
/*      */       
/* 1259 */       this.textComponent = null;
/* 1260 */       this.popupWindowListener.uninstall(this.popupWindow);
/* 1261 */       this.popupWindow = null;
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
/*      */   private void uninstallTriggerKey() {
/* 1275 */     InputMap im = this.textComponent.getInputMap();
/* 1276 */     im.put(this.trigger, this.oldTriggerKey);
/* 1277 */     ActionMap am = this.textComponent.getActionMap();
/* 1278 */     am.put("AutoComplete", this.oldTriggerAction);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateUI() {
/* 1288 */     if (this.popupWindow != null) {
/* 1289 */       this.popupWindow.updateUI();
/*      */     }
/* 1291 */     if (this.pcc != null) {
/* 1292 */       this.pcc.updateUI();
/*      */     }
/*      */     
/* 1295 */     if (this.paramChoicesRenderer instanceof JComponent) {
/* 1296 */       ((JComponent)this.paramChoicesRenderer).updateUI();
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
/*      */   public void setParameterDescriptionTruncateThreshold(int truncateThreshold) {
/* 1309 */     this.parameterDescriptionTruncateThreshold = truncateThreshold;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class AutoActivationListener
/*      */     extends FocusAdapter
/*      */     implements DocumentListener, CaretListener, ActionListener
/*      */   {
/*      */     private Timer timer;
/*      */     
/*      */     private boolean justInserted;
/*      */ 
/*      */     
/*      */     AutoActivationListener() {
/* 1324 */       this.timer = new Timer(200, this);
/* 1325 */       this.timer.setRepeats(false);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformed(ActionEvent e) {
/* 1330 */       AutoCompletion.this.doCompletion();
/*      */     }
/*      */     
/*      */     public void addTo(JTextComponent tc) {
/* 1334 */       tc.addFocusListener(this);
/* 1335 */       tc.getDocument().addDocumentListener(this);
/* 1336 */       tc.addCaretListener(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void caretUpdate(CaretEvent e) {
/* 1341 */       if (this.justInserted) {
/* 1342 */         this.justInserted = false;
/*      */       } else {
/*      */         
/* 1345 */         this.timer.stop();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void changedUpdate(DocumentEvent e) {}
/*      */ 
/*      */ 
/*      */     
/*      */     public void focusLost(FocusEvent e) {
/* 1356 */       this.timer.stop();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void insertUpdate(DocumentEvent e) {
/* 1362 */       this.justInserted = false;
/* 1363 */       if (AutoCompletion.this.isAutoCompleteEnabled() && AutoCompletion.this.isAutoActivationEnabled() && e
/* 1364 */         .getLength() == 1) {
/* 1365 */         if (AutoCompletion.this.textComponent != null && AutoCompletion.this.provider.isAutoActivateOkay(AutoCompletion.this.textComponent)) {
/* 1366 */           this.timer.restart();
/* 1367 */           this.justInserted = true;
/*      */         } else {
/*      */           
/* 1370 */           this.timer.stop();
/*      */         } 
/*      */       } else {
/*      */         
/* 1374 */         this.timer.stop();
/*      */       } 
/*      */     }
/*      */     
/*      */     public void removeFrom(JTextComponent tc) {
/* 1379 */       tc.removeFocusListener(this);
/* 1380 */       tc.getDocument().removeDocumentListener(this);
/* 1381 */       tc.removeCaretListener(this);
/* 1382 */       this.timer.stop();
/* 1383 */       this.justInserted = false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void removeUpdate(DocumentEvent e) {
/* 1388 */       this.timer.stop();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected class AutoCompleteAction
/*      */     extends AbstractAction
/*      */   {
/*      */     public void actionPerformed(ActionEvent e) {
/* 1401 */       if (AutoCompletion.this.isAutoCompleteEnabled()) {
/* 1402 */         AutoCompletion.this.refreshPopupWindow();
/*      */       }
/* 1404 */       else if (AutoCompletion.this.oldTriggerAction != null) {
/* 1405 */         AutoCompletion.this.oldTriggerAction.actionPerformed(e);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class LookAndFeelChangeListener
/*      */     implements PropertyChangeListener
/*      */   {
/*      */     private LookAndFeelChangeListener() {}
/*      */ 
/*      */     
/*      */     public void propertyChange(PropertyChangeEvent e) {
/* 1419 */       String name = e.getPropertyName();
/* 1420 */       if ("lookAndFeel".equals(name)) {
/* 1421 */         AutoCompletion.this.updateUI();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class ParameterizedCompletionStartAction
/*      */     extends AbstractAction
/*      */   {
/*      */     private String start;
/*      */ 
/*      */     
/*      */     ParameterizedCompletionStartAction(char ch) {
/* 1435 */       this.start = Character.toString(ch);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformed(ActionEvent e) {
/* 1442 */       boolean wasVisible = AutoCompletion.this.hidePopupWindow();
/*      */ 
/*      */       
/* 1445 */       if (!wasVisible || !AutoCompletion.this.isParameterAssistanceEnabled()) {
/* 1446 */         AutoCompletion.this.textComponent.replaceSelection(this.start);
/*      */         
/*      */         return;
/*      */       } 
/* 1450 */       Completion c = AutoCompletion.this.popupWindow.getSelection();
/* 1451 */       if (c instanceof ParameterizedCompletion)
/*      */       {
/* 1453 */         AutoCompletion.this.insertCompletion(c, true);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class ParentWindowListener
/*      */     extends ComponentAdapter
/*      */     implements WindowFocusListener
/*      */   {
/*      */     private ParentWindowListener() {}
/*      */ 
/*      */     
/*      */     public void addTo(Window w) {
/* 1468 */       w.addComponentListener(this);
/* 1469 */       w.addWindowFocusListener(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void componentHidden(ComponentEvent e) {
/* 1474 */       AutoCompletion.this.hideChildWindows();
/*      */     }
/*      */ 
/*      */     
/*      */     public void componentMoved(ComponentEvent e) {
/* 1479 */       AutoCompletion.this.hideChildWindows();
/*      */     }
/*      */ 
/*      */     
/*      */     public void componentResized(ComponentEvent e) {
/* 1484 */       AutoCompletion.this.hideChildWindows();
/*      */     }
/*      */     
/*      */     public void removeFrom(Window w) {
/* 1488 */       w.removeComponentListener(this);
/* 1489 */       w.removeWindowFocusListener(this);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void windowGainedFocus(WindowEvent e) {}
/*      */ 
/*      */     
/*      */     public void windowLostFocus(WindowEvent e) {
/* 1498 */       AutoCompletion.this.hideChildWindows();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class PopupWindowListener
/*      */     extends ComponentAdapter
/*      */   {
/*      */     private PopupWindowListener() {}
/*      */ 
/*      */     
/*      */     public void componentHidden(ComponentEvent e) {
/* 1511 */       AutoCompletion.this.fireAutoCompletionEvent(AutoCompletionEvent.Type.POPUP_HIDDEN);
/*      */     }
/*      */ 
/*      */     
/*      */     public void componentShown(ComponentEvent e) {
/* 1516 */       AutoCompletion.this.fireAutoCompletionEvent(AutoCompletionEvent.Type.POPUP_SHOWN);
/*      */     }
/*      */     
/*      */     public void install(AutoCompletePopupWindow popupWindow) {
/* 1520 */       popupWindow.addComponentListener(this);
/*      */     }
/*      */     
/*      */     public void uninstall(AutoCompletePopupWindow popupWindow) {
/* 1524 */       if (popupWindow != null) {
/* 1525 */         popupWindow.removeComponentListener(this);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private class TextComponentListener
/*      */     extends FocusAdapter
/*      */     implements HierarchyListener
/*      */   {
/*      */     private TextComponentListener() {}
/*      */ 
/*      */     
/*      */     void addTo(JTextComponent tc) {
/* 1539 */       tc.addFocusListener(this);
/* 1540 */       tc.addHierarchyListener(this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void focusLost(FocusEvent e) {
/* 1548 */       AutoCompletion.this.hideChildWindows();
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
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void hierarchyChanged(HierarchyEvent e) {
/* 1564 */       Window oldParentWindow = AutoCompletion.this.parentWindow;
/* 1565 */       AutoCompletion.this.parentWindow = SwingUtilities.getWindowAncestor(AutoCompletion.this.textComponent);
/* 1566 */       if (AutoCompletion.this.parentWindow != oldParentWindow) {
/* 1567 */         if (oldParentWindow != null) {
/* 1568 */           AutoCompletion.this.parentWindowListener.removeFrom(oldParentWindow);
/*      */         }
/* 1570 */         if (AutoCompletion.this.parentWindow != null) {
/* 1571 */           AutoCompletion.this.parentWindowListener.addTo(AutoCompletion.this.parentWindow);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void removeFrom(JTextComponent tc) {
/* 1578 */       tc.removeFocusListener(this);
/* 1579 */       tc.removeHierarchyListener(this);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\AutoCompletion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */