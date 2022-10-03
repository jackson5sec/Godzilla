/*     */ package com.jediterm.terminal.ui;
/*     */ import com.jediterm.terminal.SubstringFinder;
/*     */ import com.jediterm.terminal.Terminal;
/*     */ import com.jediterm.terminal.TerminalStarter;
/*     */ import com.jediterm.terminal.TtyConnector;
/*     */ import com.jediterm.terminal.model.JediTerminal;
/*     */ import com.jediterm.terminal.model.StyleState;
/*     */ import com.jediterm.terminal.model.TerminalTextBuffer;
/*     */ import com.jediterm.terminal.model.hyperlinks.HyperlinkFilter;
/*     */ import com.jediterm.terminal.ui.settings.SettingsProvider;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.ItemListener;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.util.List;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JLayeredPane;
/*     */ import javax.swing.JScrollBar;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ public class JediTermWidget extends JPanel implements TerminalSession, TerminalWidget, TerminalActionProvider {
/*  35 */   private static final Logger LOG = Logger.getLogger(JediTermWidget.class);
/*     */   
/*     */   protected final TerminalPanel myTerminalPanel;
/*     */   protected final JScrollBar myScrollBar;
/*     */   protected final JediTerminal myTerminal;
/*  40 */   protected final AtomicBoolean mySessionRunning = new AtomicBoolean();
/*     */   private SearchComponent myFindComponent;
/*     */   private final PreConnectHandler myPreConnectHandler;
/*     */   private TtyConnector myTtyConnector;
/*     */   private TerminalStarter myTerminalStarter;
/*     */   private Thread myEmuThread;
/*     */   protected final SettingsProvider mySettingsProvider;
/*     */   private TerminalActionProvider myNextActionProvider;
/*     */   private JLayeredPane myInnerPanel;
/*     */   private final TextProcessing myTextProcessing;
/*  50 */   private final List<TerminalWidgetListener> myListeners = new CopyOnWriteArrayList<>();
/*     */   
/*     */   public JediTermWidget(@NotNull SettingsProvider settingsProvider) {
/*  53 */     this(80, 24, settingsProvider);
/*     */   }
/*     */   
/*     */   public JediTermWidget(Dimension dimension, SettingsProvider settingsProvider) {
/*  57 */     this(dimension.width, dimension.height, settingsProvider);
/*     */   }
/*     */   
/*     */   public JediTermWidget(int columns, int lines, SettingsProvider settingsProvider) {
/*  61 */     super(new BorderLayout());
/*     */     
/*  63 */     this.mySettingsProvider = settingsProvider;
/*     */     
/*  65 */     StyleState styleState = createDefaultStyle();
/*     */     
/*  67 */     this
/*  68 */       .myTextProcessing = new TextProcessing(settingsProvider.getHyperlinkColor(), settingsProvider.getHyperlinkHighlightingMode());
/*     */     
/*  70 */     TerminalTextBuffer terminalTextBuffer = new TerminalTextBuffer(columns, lines, styleState, settingsProvider.getBufferMaxLinesCount(), this.myTextProcessing);
/*  71 */     this.myTextProcessing.setTerminalTextBuffer(terminalTextBuffer);
/*     */     
/*  73 */     this.myTerminalPanel = createTerminalPanel(this.mySettingsProvider, styleState, terminalTextBuffer);
/*  74 */     this.myTerminal = new JediTerminal(this.myTerminalPanel, terminalTextBuffer, styleState);
/*     */     
/*  76 */     this.myTerminal.setModeEnabled(TerminalMode.AltSendsEscape, this.mySettingsProvider.altSendsEscape());
/*     */     
/*  78 */     this.myTerminalPanel.addTerminalMouseListener((TerminalMouseListener)this.myTerminal);
/*  79 */     this.myTerminalPanel.setNextProvider(this);
/*  80 */     this.myTerminalPanel.setCoordAccessor((TerminalCoordinates)this.myTerminal);
/*     */     
/*  82 */     this.myPreConnectHandler = createPreConnectHandler(this.myTerminal);
/*  83 */     this.myTerminalPanel.addCustomKeyListener(this.myPreConnectHandler);
/*  84 */     this.myScrollBar = createScrollBar();
/*     */     
/*  86 */     this.myInnerPanel = new JLayeredPane();
/*  87 */     this.myInnerPanel.setFocusable(false);
/*  88 */     setFocusable(false);
/*     */     
/*  90 */     this.myInnerPanel.setLayout(new TerminalLayout());
/*  91 */     this.myInnerPanel.add(this.myTerminalPanel, "TERMINAL");
/*  92 */     this.myInnerPanel.add(this.myScrollBar, "SCROLL");
/*     */     
/*  94 */     add(this.myInnerPanel, "Center");
/*     */     
/*  96 */     this.myScrollBar.setModel(this.myTerminalPanel.getBoundedRangeModel());
/*  97 */     this.mySessionRunning.set(false);
/*     */     
/*  99 */     this.myTerminalPanel.init(this.myScrollBar);
/*     */     
/* 101 */     this.myTerminalPanel.setVisible(true);
/*     */   }
/*     */   
/*     */   protected JScrollBar createScrollBar() {
/* 105 */     JScrollBar scrollBar = new JScrollBar();
/* 106 */     scrollBar.setUI(new FindResultScrollBarUI());
/* 107 */     return scrollBar;
/*     */   }
/*     */   
/*     */   protected StyleState createDefaultStyle() {
/* 111 */     StyleState styleState = new StyleState();
/* 112 */     styleState.setDefaultStyle(this.mySettingsProvider.getDefaultStyle());
/* 113 */     return styleState;
/*     */   }
/*     */   
/*     */   protected TerminalPanel createTerminalPanel(@NotNull SettingsProvider settingsProvider, @NotNull StyleState styleState, @NotNull TerminalTextBuffer terminalTextBuffer) {
/* 117 */     if (settingsProvider == null) $$$reportNull$$$0(1);  if (styleState == null) $$$reportNull$$$0(2);  if (terminalTextBuffer == null) $$$reportNull$$$0(3);  return new TerminalPanel(settingsProvider, terminalTextBuffer, styleState);
/*     */   }
/*     */   
/*     */   protected PreConnectHandler createPreConnectHandler(JediTerminal terminal) {
/* 121 */     return new PreConnectHandler((Terminal)terminal);
/*     */   }
/*     */   
/*     */   public TerminalDisplay getTerminalDisplay() {
/* 125 */     return getTerminalPanel();
/*     */   }
/*     */   
/*     */   public TerminalPanel getTerminalPanel() {
/* 129 */     return this.myTerminalPanel;
/*     */   }
/*     */   
/*     */   public void setTtyConnector(@NotNull TtyConnector ttyConnector) {
/* 133 */     if (ttyConnector == null) $$$reportNull$$$0(4);  this.myTtyConnector = ttyConnector;
/*     */     
/* 135 */     this.myTerminalStarter = createTerminalStarter(this.myTerminal, this.myTtyConnector);
/* 136 */     this.myTerminalPanel.setTerminalStarter(this.myTerminalStarter);
/*     */   }
/*     */   
/*     */   protected TerminalStarter createTerminalStarter(JediTerminal terminal, TtyConnector connector) {
/* 140 */     return new TerminalStarter((Terminal)terminal, connector, (TerminalDataStream)new TtyBasedArrayDataStream(connector));
/*     */   }
/*     */ 
/*     */   
/*     */   public TtyConnector getTtyConnector() {
/* 145 */     return this.myTtyConnector;
/*     */   }
/*     */ 
/*     */   
/*     */   public Terminal getTerminal() {
/* 150 */     return (Terminal)this.myTerminal;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSessionName() {
/* 155 */     if (this.myTtyConnector != null) {
/* 156 */       return this.myTtyConnector.getName();
/*     */     }
/* 158 */     return "Session";
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 163 */     if (!this.mySessionRunning.get()) {
/* 164 */       this.myEmuThread = new Thread(new EmulatorTask());
/* 165 */       this.myEmuThread.start();
/*     */     } else {
/* 167 */       LOG.error("Should not try to start session again at this point... ");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stop() {
/* 172 */     if (this.mySessionRunning.get() && this.myEmuThread != null) {
/* 173 */       this.myEmuThread.interrupt();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isSessionRunning() {
/* 178 */     return this.mySessionRunning.get();
/*     */   }
/*     */   
/*     */   public String getBufferText(DebugBufferType type) {
/* 182 */     return type.getValue(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public TerminalTextBuffer getTerminalTextBuffer() {
/* 187 */     return this.myTerminalPanel.getTerminalTextBuffer();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requestFocusInWindow() {
/* 192 */     SwingUtilities.invokeLater(new Runnable() {
/*     */           public void run() {
/* 194 */             JediTermWidget.this.myTerminalPanel.requestFocusInWindow();
/*     */           }
/*     */         });
/* 197 */     return super.requestFocusInWindow();
/*     */   }
/*     */ 
/*     */   
/*     */   public void requestFocus() {
/* 202 */     this.myTerminalPanel.requestFocus();
/*     */   }
/*     */   
/*     */   public boolean canOpenSession() {
/* 206 */     return !isSessionRunning();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTerminalPanelListener(TerminalPanelListener terminalPanelListener) {
/* 211 */     this.myTerminalPanel.setTerminalPanelListener(terminalPanelListener);
/*     */   }
/*     */ 
/*     */   
/*     */   public TerminalSession getCurrentSession() {
/* 216 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public JediTermWidget createTerminalSession(TtyConnector ttyConnector) {
/* 221 */     setTtyConnector(ttyConnector);
/* 222 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public JComponent getComponent() {
/* 227 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 232 */     stop();
/* 233 */     if (this.myTerminalStarter != null) {
/* 234 */       this.myTerminalStarter.close();
/*     */     }
/* 236 */     this.myTerminalPanel.dispose();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<TerminalAction> getActions() {
/* 241 */     return Lists.newArrayList((Object[])new TerminalAction[] { (new TerminalAction(this.mySettingsProvider.getFindActionPresentation(), (Predicate<KeyEvent>)new Predicate<KeyEvent>()
/*     */             {
/*     */               public boolean apply(KeyEvent input)
/*     */               {
/* 245 */                 JediTermWidget.this.showFindText();
/* 246 */                 return true;
/*     */               }
/* 248 */             })).withMnemonicKey(Integer.valueOf(70)) });
/*     */   }
/*     */   
/*     */   private void showFindText() {
/* 252 */     if (this.myFindComponent == null) {
/* 253 */       this.myFindComponent = createSearchComponent();
/*     */       
/* 255 */       final JComponent component = this.myFindComponent.getComponent();
/* 256 */       this.myInnerPanel.add(component, "FIND");
/* 257 */       this.myInnerPanel.moveToFront(component);
/* 258 */       this.myInnerPanel.revalidate();
/* 259 */       this.myInnerPanel.repaint();
/* 260 */       component.requestFocus();
/*     */       
/* 262 */       this.myFindComponent.addDocumentChangeListener(new DocumentListener()
/*     */           {
/*     */             public void insertUpdate(DocumentEvent e) {
/* 265 */               textUpdated();
/*     */             }
/*     */ 
/*     */             
/*     */             public void removeUpdate(DocumentEvent e) {
/* 270 */               textUpdated();
/*     */             }
/*     */ 
/*     */             
/*     */             public void changedUpdate(DocumentEvent e) {
/* 275 */               textUpdated();
/*     */             }
/*     */             
/*     */             private void textUpdated() {
/* 279 */               JediTermWidget.this.findText(JediTermWidget.this.myFindComponent.getText(), JediTermWidget.this.myFindComponent.ignoreCase());
/*     */             }
/*     */           });
/*     */       
/* 283 */       this.myFindComponent.addIgnoreCaseListener(new ItemListener()
/*     */           {
/*     */             public void itemStateChanged(ItemEvent e) {
/* 286 */               JediTermWidget.this.findText(JediTermWidget.this.myFindComponent.getText(), JediTermWidget.this.myFindComponent.ignoreCase());
/*     */             }
/*     */           });
/*     */       
/* 290 */       this.myFindComponent.addKeyListener(new KeyAdapter()
/*     */           {
/*     */             public void keyPressed(KeyEvent keyEvent) {
/* 293 */               if (keyEvent.getKeyCode() == 27) {
/* 294 */                 JediTermWidget.this.myInnerPanel.remove(component);
/* 295 */                 JediTermWidget.this.myInnerPanel.revalidate();
/* 296 */                 JediTermWidget.this.myInnerPanel.repaint();
/* 297 */                 JediTermWidget.this.myFindComponent = null;
/* 298 */                 JediTermWidget.this.myTerminalPanel.setFindResult((SubstringFinder.FindResult)null);
/* 299 */                 JediTermWidget.this.myTerminalPanel.requestFocusInWindow();
/* 300 */               } else if (keyEvent.getKeyCode() == 10 || keyEvent.getKeyCode() == 38) {
/* 301 */                 JediTermWidget.this.myFindComponent.nextFindResultItem(JediTermWidget.this.myTerminalPanel.selectNextFindResultItem());
/* 302 */               } else if (keyEvent.getKeyCode() == 40) {
/* 303 */                 JediTermWidget.this.myFindComponent.prevFindResultItem(JediTermWidget.this.myTerminalPanel.selectPrevFindResultItem());
/*     */               } else {
/* 305 */                 super.keyPressed(keyEvent);
/*     */               } 
/*     */             }
/*     */           });
/*     */     } else {
/* 310 */       this.myFindComponent.getComponent().requestFocusInWindow();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected SearchComponent createSearchComponent() {
/* 315 */     return new SearchPanel();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void findText(String text, boolean ignoreCase) {
/* 339 */     SubstringFinder.FindResult results = this.myTerminal.searchInTerminalTextBuffer(text, ignoreCase);
/* 340 */     this.myTerminalPanel.setFindResult(results);
/* 341 */     this.myFindComponent.onResultUpdated(results);
/* 342 */     this.myScrollBar.repaint();
/*     */   }
/*     */ 
/*     */   
/*     */   public TerminalActionProvider getNextProvider() {
/* 347 */     return this.myNextActionProvider;
/*     */   }
/*     */   
/*     */   public void setNextProvider(TerminalActionProvider actionProvider) {
/* 351 */     this.myNextActionProvider = actionProvider;
/*     */   } protected static interface SearchComponent {
/*     */     String getText(); boolean ignoreCase(); JComponent getComponent(); void addDocumentChangeListener(DocumentListener param1DocumentListener); void addKeyListener(KeyListener param1KeyListener); void addIgnoreCaseListener(ItemListener param1ItemListener); void onResultUpdated(SubstringFinder.FindResult param1FindResult); void nextFindResultItem(SubstringFinder.FindResult.FindItem param1FindItem);
/*     */     void prevFindResultItem(SubstringFinder.FindResult.FindItem param1FindItem); }
/*     */   class EmulatorTask implements Runnable { public void run() {
/*     */       try {
/* 357 */         JediTermWidget.this.mySessionRunning.set(true);
/* 358 */         Thread.currentThread().setName("Connector-" + JediTermWidget.this.myTtyConnector.getName());
/* 359 */         if (JediTermWidget.this.myTtyConnector.init(JediTermWidget.this.myPreConnectHandler)) {
/* 360 */           JediTermWidget.this.myTerminalPanel.addCustomKeyListener(JediTermWidget.this.myTerminalPanel.getTerminalKeyListener());
/* 361 */           JediTermWidget.this.myTerminalPanel.removeCustomKeyListener(JediTermWidget.this.myPreConnectHandler);
/* 362 */           JediTermWidget.this.myTerminalStarter.start();
/*     */         } 
/* 364 */       } catch (Exception e) {
/* 365 */         JediTermWidget.LOG.error("Exception running terminal", e);
/*     */       } finally {
/*     */         try {
/* 368 */           JediTermWidget.this.myTtyConnector.close();
/* 369 */         } catch (Exception exception) {}
/*     */         
/* 371 */         JediTermWidget.this.mySessionRunning.set(false);
/* 372 */         TerminalPanelListener terminalPanelListener = JediTermWidget.this.myTerminalPanel.getTerminalPanelListener();
/* 373 */         if (terminalPanelListener != null)
/* 374 */           terminalPanelListener.onSessionChanged(JediTermWidget.this.getCurrentSession()); 
/* 375 */         for (TerminalWidgetListener listener : JediTermWidget.this.myListeners) {
/* 376 */           listener.allSessionsClosed(JediTermWidget.this);
/*     */         }
/* 378 */         JediTermWidget.this.myTerminalPanel.addCustomKeyListener(JediTermWidget.this.myPreConnectHandler);
/* 379 */         JediTermWidget.this.myTerminalPanel.removeCustomKeyListener(JediTermWidget.this.myTerminalPanel.getTerminalKeyListener());
/*     */       } 
/*     */     } }
/*     */ 
/*     */   
/*     */   public TerminalStarter getTerminalStarter() {
/* 385 */     return this.myTerminalStarter;
/*     */   }
/*     */   
/*     */   public class SearchPanel
/*     */     extends JPanel implements SearchComponent {
/* 390 */     private final JTextField myTextField = new JTextField();
/* 391 */     private final JLabel label = new JLabel();
/*     */     private final JButton prev;
/*     */     private final JButton next;
/* 394 */     private final JCheckBox ignoreCaseCheckBox = new JCheckBox("Ignore Case", true);
/*     */     
/*     */     public SearchPanel() {
/* 397 */       this.next = createNextButton();
/* 398 */       this.next.addActionListener(new ActionListener()
/*     */           {
/*     */             public void actionPerformed(ActionEvent e) {
/* 401 */               JediTermWidget.SearchPanel.this.nextFindResultItem(JediTermWidget.this.myTerminalPanel.selectNextFindResultItem());
/*     */             }
/*     */           });
/*     */       
/* 405 */       this.prev = createPrevButton();
/* 406 */       this.prev.addActionListener(new ActionListener()
/*     */           {
/*     */             public void actionPerformed(ActionEvent e) {
/* 409 */               JediTermWidget.SearchPanel.this.prevFindResultItem(JediTermWidget.this.myTerminalPanel.selectPrevFindResultItem());
/*     */             }
/*     */           });
/*     */       
/* 413 */       this.myTextField.setPreferredSize(new Dimension(JediTermWidget.this.myTerminalPanel.myCharSize.width * 30, JediTermWidget.this.myTerminalPanel.myCharSize.height + 3));
/*     */ 
/*     */       
/* 416 */       this.myTextField.setEditable(true);
/*     */       
/* 418 */       updateLabel((SubstringFinder.FindResult.FindItem)null);
/*     */       
/* 420 */       add(this.myTextField);
/* 421 */       add(this.ignoreCaseCheckBox);
/* 422 */       add(this.label);
/* 423 */       add(this.next);
/* 424 */       add(this.prev);
/*     */       
/* 426 */       setOpaque(true);
/*     */     }
/*     */     
/*     */     protected JButton createNextButton() {
/* 430 */       return new BasicArrowButton(1);
/*     */     }
/*     */     
/*     */     protected JButton createPrevButton() {
/* 434 */       return new BasicArrowButton(5);
/*     */     }
/*     */ 
/*     */     
/*     */     public void nextFindResultItem(SubstringFinder.FindResult.FindItem selectedItem) {
/* 439 */       updateLabel(selectedItem);
/*     */     }
/*     */ 
/*     */     
/*     */     public void prevFindResultItem(SubstringFinder.FindResult.FindItem selectedItem) {
/* 444 */       updateLabel(selectedItem);
/*     */     }
/*     */     
/*     */     private void updateLabel(SubstringFinder.FindResult.FindItem selectedItem) {
/* 448 */       SubstringFinder.FindResult result = JediTermWidget.this.myTerminalPanel.getFindResult();
/* 449 */       this.label.setText(((selectedItem != null) ? selectedItem.getIndex() : 0) + " of " + ((result != null) ? result
/* 450 */           .getItems().size() : 0));
/*     */     }
/*     */ 
/*     */     
/*     */     public void onResultUpdated(SubstringFinder.FindResult results) {
/* 455 */       updateLabel((SubstringFinder.FindResult.FindItem)null);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getText() {
/* 460 */       return this.myTextField.getText();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean ignoreCase() {
/* 465 */       return this.ignoreCaseCheckBox.isSelected();
/*     */     }
/*     */ 
/*     */     
/*     */     public JComponent getComponent() {
/* 470 */       return this;
/*     */     }
/*     */     
/*     */     public void requestFocus() {
/* 474 */       this.myTextField.requestFocus();
/*     */     }
/*     */ 
/*     */     
/*     */     public void addDocumentChangeListener(DocumentListener listener) {
/* 479 */       this.myTextField.getDocument().addDocumentListener(listener);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addKeyListener(KeyListener listener) {
/* 484 */       this.myTextField.addKeyListener(listener);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addIgnoreCaseListener(ItemListener listener) {
/* 489 */       this.ignoreCaseCheckBox.addItemListener(listener);
/*     */     }
/*     */   }
/*     */   
/*     */   private class FindResultScrollBarUI extends BasicScrollBarUI {
/*     */     private FindResultScrollBarUI() {}
/*     */     
/*     */     protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
/* 497 */       super.paintTrack(g, c, trackBounds);
/*     */       
/* 499 */       SubstringFinder.FindResult result = JediTermWidget.this.myTerminalPanel.getFindResult();
/* 500 */       if (result != null) {
/* 501 */         int modelHeight = this.scrollbar.getModel().getMaximum() - this.scrollbar.getModel().getMinimum();
/* 502 */         int anchorHeight = Math.max(2, trackBounds.height / modelHeight);
/*     */ 
/*     */         
/* 505 */         Color color = JediTermWidget.this.mySettingsProvider.getTerminalColorPalette().getBackground(Objects.<TerminalColor>requireNonNull(JediTermWidget.this.mySettingsProvider.getFoundPatternColor().getBackground()));
/* 506 */         g.setColor(color);
/* 507 */         for (SubstringFinder.FindResult.FindItem r : result.getItems()) {
/* 508 */           int where = trackBounds.height * (r.getStart()).y / modelHeight;
/* 509 */           g.fillRect(trackBounds.x, trackBounds.y + where, trackBounds.width, anchorHeight);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TerminalLayout
/*     */     implements LayoutManager {
/*     */     public static final String TERMINAL = "TERMINAL";
/*     */     public static final String SCROLL = "SCROLL";
/*     */     public static final String FIND = "FIND";
/*     */     private Component terminal;
/*     */     private Component scroll;
/*     */     private Component find;
/*     */     
/*     */     private TerminalLayout() {}
/*     */     
/*     */     public void addLayoutComponent(String name, Component comp) {
/* 527 */       if ("TERMINAL".equals(name))
/* 528 */       { this.terminal = comp; }
/* 529 */       else if ("FIND".equals(name))
/* 530 */       { this.find = comp; }
/* 531 */       else if ("SCROLL".equals(name))
/* 532 */       { this.scroll = comp; }
/* 533 */       else { throw new IllegalArgumentException("unknown component name " + name); }
/*     */     
/*     */     }
/*     */     
/*     */     public void removeLayoutComponent(Component comp) {
/* 538 */       if (comp == this.terminal) {
/* 539 */         this.terminal = null;
/*     */       }
/* 541 */       if (comp == this.scroll) {
/* 542 */         this.scroll = null;
/*     */       }
/* 544 */       if (comp == this.find) {
/* 545 */         this.find = comp;
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public Dimension preferredLayoutSize(Container target) {
/* 551 */       synchronized (target.getTreeLock()) {
/* 552 */         Dimension dim = new Dimension(0, 0);
/*     */         
/* 554 */         if (this.terminal != null) {
/* 555 */           Dimension d = this.terminal.getPreferredSize();
/* 556 */           dim.width = Math.max(d.width, dim.width);
/* 557 */           dim.height = Math.max(d.height, dim.height);
/*     */         } 
/*     */         
/* 560 */         if (this.scroll != null) {
/* 561 */           Dimension d = this.scroll.getPreferredSize();
/* 562 */           dim.width += d.width;
/* 563 */           dim.height = Math.max(d.height, dim.height);
/*     */         } 
/*     */         
/* 566 */         if (this.find != null) {
/* 567 */           Dimension d = this.find.getPreferredSize();
/* 568 */           dim.width = Math.max(d.width, dim.width);
/* 569 */           dim.height = Math.max(d.height, dim.height);
/*     */         } 
/*     */         
/* 572 */         Insets insets = target.getInsets();
/* 573 */         dim.width += insets.left + insets.right;
/* 574 */         dim.height += insets.top + insets.bottom;
/*     */         
/* 576 */         return dim;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Dimension minimumLayoutSize(Container target) {
/* 582 */       synchronized (target.getTreeLock()) {
/* 583 */         Dimension dim = new Dimension(0, 0);
/*     */         
/* 585 */         if (this.terminal != null) {
/* 586 */           Dimension d = this.terminal.getMinimumSize();
/* 587 */           dim.width = Math.max(d.width, dim.width);
/* 588 */           dim.height = Math.max(d.height, dim.height);
/*     */         } 
/*     */         
/* 591 */         if (this.scroll != null) {
/* 592 */           Dimension d = this.scroll.getPreferredSize();
/* 593 */           dim.width += d.width;
/* 594 */           dim.height = Math.max(d.height, dim.height);
/*     */         } 
/*     */         
/* 597 */         if (this.find != null) {
/* 598 */           Dimension d = this.find.getMinimumSize();
/* 599 */           dim.width = Math.max(d.width, dim.width);
/* 600 */           dim.height = Math.max(d.height, dim.height);
/*     */         } 
/*     */         
/* 603 */         Insets insets = target.getInsets();
/* 604 */         dim.width += insets.left + insets.right;
/* 605 */         dim.height += insets.top + insets.bottom;
/*     */         
/* 607 */         return dim;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void layoutContainer(Container target) {
/* 613 */       synchronized (target.getTreeLock()) {
/* 614 */         Insets insets = target.getInsets();
/* 615 */         int top = insets.top;
/* 616 */         int bottom = target.getHeight() - insets.bottom;
/* 617 */         int left = insets.left;
/* 618 */         int right = target.getWidth() - insets.right;
/*     */         
/* 620 */         Dimension scrollDim = new Dimension(0, 0);
/* 621 */         if (this.scroll != null) {
/* 622 */           scrollDim = this.scroll.getPreferredSize();
/* 623 */           this.scroll.setBounds(right - scrollDim.width, top, scrollDim.width, bottom - top);
/*     */         } 
/*     */         
/* 626 */         if (this.terminal != null) {
/* 627 */           this.terminal.setBounds(left, top, right - left - scrollDim.width, bottom - top);
/*     */         }
/*     */         
/* 630 */         if (this.find != null) {
/* 631 */           Dimension d = this.find.getPreferredSize();
/* 632 */           this.find.setBounds(right - d.width - scrollDim.width, top, d.width, d.height);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addHyperlinkFilter(HyperlinkFilter filter) {
/* 640 */     this.myTextProcessing.addHyperlinkFilter(filter);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addListener(TerminalWidgetListener listener) {
/* 645 */     this.myListeners.add(listener);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeListener(TerminalWidgetListener listener) {
/* 650 */     this.myListeners.remove(listener);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\termina\\ui\JediTermWidget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */