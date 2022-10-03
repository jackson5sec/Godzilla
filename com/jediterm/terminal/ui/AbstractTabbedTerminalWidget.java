/*     */ package com.jediterm.terminal.ui;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.jediterm.terminal.TtyConnector;
/*     */ import com.jediterm.terminal.ui.settings.TabbedSettingsProvider;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.FocusAdapter;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.JTextField;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ public abstract class AbstractTabbedTerminalWidget<T extends JediTermWidget> extends JPanel implements TerminalWidget, TerminalActionProvider {
/*  29 */   private final Object myLock = new Object();
/*     */   
/*  31 */   private TerminalPanelListener myTerminalPanelListener = null;
/*     */   
/*  33 */   private T myTermWidget = null;
/*     */   
/*     */   private AbstractTabs<T> myTabs;
/*     */   
/*     */   private TabbedSettingsProvider mySettingsProvider;
/*     */   
/*  39 */   private List<TabListener> myTabListeners = Lists.newArrayList();
/*  40 */   private List<TerminalWidgetListener> myWidgetListeners = new CopyOnWriteArrayList<>();
/*     */   
/*     */   private TerminalActionProvider myNextActionProvider;
/*     */   
/*     */   private final Function<AbstractTabbedTerminalWidget<T>, T> myCreateNewSessionAction;
/*     */   private JPanel myPanel;
/*     */   
/*     */   public AbstractTabbedTerminalWidget(@NotNull TabbedSettingsProvider settingsProvider, @NotNull Function<AbstractTabbedTerminalWidget<T>, T> createNewSessionAction) {
/*  48 */     super(new BorderLayout());
/*  49 */     this.mySettingsProvider = settingsProvider;
/*  50 */     this.myCreateNewSessionAction = createNewSessionAction;
/*     */     
/*  52 */     setFocusTraversalPolicy(new DefaultFocusTraversalPolicy());
/*     */     
/*  54 */     this.myPanel = new JPanel(new BorderLayout());
/*  55 */     this.myPanel.add(this, "Center");
/*     */   }
/*     */ 
/*     */   
/*     */   public T createTerminalSession(TtyConnector ttyConnector) {
/*  60 */     T terminal = createNewTabWidget();
/*     */     
/*  62 */     initSession(ttyConnector, terminal);
/*     */     
/*  64 */     return terminal;
/*     */   }
/*     */   
/*     */   public void initSession(TtyConnector ttyConnector, T terminal) {
/*  68 */     terminal.createTerminalSession(ttyConnector);
/*  69 */     if (this.myTabs != null) {
/*  70 */       int index = this.myTabs.indexOfComponent((Component)terminal);
/*  71 */       if (index != -1) {
/*  72 */         this.myTabs.setTitleAt(index, generateUniqueName(terminal, this.myTabs));
/*     */       }
/*     */     } 
/*  75 */     setupTtyConnectorWaitFor(ttyConnector, terminal);
/*     */   }
/*     */   
/*     */   public T createNewTabWidget() {
/*  79 */     T terminal = createInnerTerminalWidget();
/*     */     
/*  81 */     terminal.setNextProvider(this);
/*     */     
/*  83 */     if (this.myTerminalPanelListener != null) {
/*  84 */       terminal.setTerminalPanelListener(this.myTerminalPanelListener);
/*     */     }
/*     */     
/*  87 */     if (this.myTermWidget == null && this.myTabs == null) {
/*  88 */       this.myTermWidget = terminal;
/*  89 */       Dimension size = terminal.getComponent().getSize();
/*     */       
/*  91 */       add(this.myTermWidget.getComponent(), "Center");
/*  92 */       setSize(size);
/*     */       
/*  94 */       if (this.myTerminalPanelListener != null) {
/*  95 */         this.myTerminalPanelListener.onPanelResize(RequestOrigin.User);
/*     */       }
/*     */       
/*  98 */       onSessionChanged();
/*     */     } else {
/*     */       
/* 101 */       if (this.myTabs == null) {
/* 102 */         this.myTabs = setupTabs();
/*     */       }
/*     */       
/* 105 */       addTab(terminal, this.myTabs);
/*     */     } 
/* 107 */     return terminal;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setupTtyConnectorWaitFor(TtyConnector ttyConnector, T widget) {
/* 113 */     (new TtyConnectorWaitFor(ttyConnector, Executors.newSingleThreadExecutor())).setTerminationCallback(integer -> {
/*     */           if (this.mySettingsProvider.shouldCloseTabOnLogout(ttyConnector)) {
/*     */             closeTab((T)widget);
/*     */             if (this.myTabs.getTabCount() == 0) {
/*     */               for (TerminalWidgetListener widgetListener : this.myWidgetListeners) {
/*     */                 widgetListener.allSessionsClosed(widget);
/*     */               }
/*     */             }
/*     */           } 
/*     */           return true;
/*     */         });
/*     */   }
/*     */   
/*     */   private void addTab(T terminal, AbstractTabs<T> tabs) {
/* 127 */     String name = generateUniqueName(terminal, tabs);
/*     */     
/* 129 */     addTab(terminal, tabs, name);
/*     */   }
/*     */   
/*     */   private String generateUniqueName(T terminal, AbstractTabs<T> tabs) {
/* 133 */     return generateUniqueName(this.mySettingsProvider.tabName(terminal.getTtyConnector(), terminal.getSessionName()), tabs);
/*     */   }
/*     */   
/*     */   private void addTab(T terminal, AbstractTabs<T> tabs, String name) {
/* 137 */     tabs.addTab(name, terminal);
/*     */ 
/*     */     
/* 140 */     tabs.setTabComponentAt(tabs.getTabCount() - 1, createTabComponent(tabs, terminal));
/* 141 */     tabs.setSelectedComponent(terminal);
/*     */   }
/*     */   
/*     */   public void addTab(String name, T terminal) {
/* 145 */     if (this.myTabs == null) {
/* 146 */       this.myTabs = setupTabs();
/*     */     }
/*     */     
/* 149 */     addTab(terminal, this.myTabs, name);
/*     */   }
/*     */   
/*     */   private String generateUniqueName(String suggestedName, AbstractTabs<T> tabs) {
/* 153 */     Set<String> names = Sets.newHashSet();
/* 154 */     for (int i = 0; i < tabs.getTabCount(); i++) {
/* 155 */       names.add(tabs.getTitleAt(i));
/*     */     }
/* 157 */     String newSdkName = suggestedName;
/* 158 */     int j = 0;
/* 159 */     while (names.contains(newSdkName)) {
/* 160 */       newSdkName = suggestedName + " (" + ++j + ")";
/*     */     }
/* 162 */     return newSdkName;
/*     */   }
/*     */   
/*     */   private AbstractTabs<T> setupTabs() {
/* 166 */     AbstractTabs<T> tabs = createTabbedPane();
/*     */     
/* 168 */     tabs.addChangeListener(new AbstractTabs.TabChangeListener()
/*     */         {
/*     */           public void tabRemoved() {
/* 171 */             if (AbstractTabbedTerminalWidget.this.myTabs.getTabCount() == 1) {
/* 172 */               AbstractTabbedTerminalWidget.this.removeTabbedPane();
/*     */             }
/*     */           }
/*     */ 
/*     */           
/*     */           public void selectionChanged() {
/* 178 */             AbstractTabbedTerminalWidget.this.onSessionChanged();
/*     */           }
/*     */         });
/*     */     
/* 182 */     remove((Component)this.myTermWidget);
/*     */     
/* 184 */     addTab(this.myTermWidget, tabs);
/*     */     
/* 186 */     this.myTermWidget = null;
/*     */     
/* 188 */     add(tabs.getComponent(), "Center");
/*     */     
/* 190 */     return tabs;
/*     */   }
/*     */   
/*     */   public boolean isNoActiveSessions() {
/* 194 */     return (this.myTabs == null && this.myTermWidget == null);
/*     */   }
/*     */   
/*     */   private void onSessionChanged() {
/* 198 */     T session = getCurrentSession();
/* 199 */     if (session != null) {
/* 200 */       if (this.myTerminalPanelListener != null) {
/* 201 */         this.myTerminalPanelListener.onSessionChanged((TerminalSession)session);
/*     */       }
/* 203 */       session.getTerminalPanel().requestFocusInWindow();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Component createTabComponent(AbstractTabs<T> tabs, T terminal) {
/* 210 */     return new TabComponent(tabs, (JediTermWidget)terminal);
/*     */   }
/*     */   
/*     */   public void closeTab(final T terminal) {
/* 214 */     if (terminal != null) {
/* 215 */       if (this.myTabs != null && this.myTabs.indexOfComponent((Component)terminal) != -1) {
/* 216 */         SwingUtilities.invokeLater(new Runnable()
/*     */             {
/*     */               public void run() {
/* 219 */                 AbstractTabbedTerminalWidget.this.removeTab(terminal);
/*     */               }
/*     */             });
/* 222 */         fireTabClosed(terminal);
/* 223 */       } else if (this.myTermWidget == terminal) {
/* 224 */         this.myTermWidget = null;
/* 225 */         fireTabClosed(terminal);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public void closeCurrentSession() {
/* 231 */     T session = getCurrentSession();
/* 232 */     if (session != null) {
/* 233 */       session.close();
/* 234 */       closeTab(session);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void dispose() {
/* 239 */     for (TerminalSession s : getAllTerminalSessions()) {
/* 240 */       if (s != null) s.close(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private List<T> getAllTerminalSessions() {
/* 245 */     List<T> session = Lists.newArrayList();
/* 246 */     if (this.myTabs != null) {
/* 247 */       for (int i = 0; i < this.myTabs.getTabCount(); i++) {
/* 248 */         session.add(getTerminalPanel(i));
/*     */       
/*     */       }
/*     */     }
/* 252 */     else if (this.myTermWidget != null) {
/* 253 */       session.add(this.myTermWidget);
/*     */     } 
/*     */     
/* 256 */     return session;
/*     */   }
/*     */   
/*     */   public void removeTab(T terminal) {
/* 260 */     synchronized (this.myLock) {
/* 261 */       if (this.myTabs != null) {
/* 262 */         this.myTabs.remove(terminal);
/*     */       }
/* 264 */       onSessionChanged();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void removeTabbedPane() {
/* 269 */     this.myTermWidget = getTerminalPanel(0);
/* 270 */     this.myTabs.removeAll();
/* 271 */     remove(this.myTabs.getComponent());
/* 272 */     this.myTabs = null;
/* 273 */     add(this.myTermWidget.getComponent(), "Center");
/*     */   }
/*     */ 
/*     */   
/*     */   public List<TerminalAction> getActions() {
/* 278 */     return Lists.newArrayList((Object[])new TerminalAction[] { (new TerminalAction(this.mySettingsProvider
/* 279 */             .getNewSessionActionPresentation(), (Predicate<KeyEvent>)new Predicate<KeyEvent>()
/*     */             {
/*     */               public boolean apply(KeyEvent input) {
/* 282 */                 AbstractTabbedTerminalWidget.this.handleNewSession();
/* 283 */                 return true;
/*     */               }
/* 285 */             })).withMnemonicKey(Integer.valueOf(78)), (new TerminalAction(this.mySettingsProvider
/* 286 */             .getCloseSessionActionPresentation(), (Predicate<KeyEvent>)new Predicate<KeyEvent>()
/*     */             {
/*     */               public boolean apply(KeyEvent input) {
/* 289 */                 AbstractTabbedTerminalWidget.this.closeCurrentSession();
/* 290 */                 return true;
/*     */               }
/* 292 */             })).withMnemonicKey(Integer.valueOf(83)), (new TerminalAction(this.mySettingsProvider
/* 293 */             .getNextTabActionPresentation(), (Predicate<KeyEvent>)new Predicate<KeyEvent>()
/*     */             {
/*     */               public boolean apply(KeyEvent input) {
/* 296 */                 AbstractTabbedTerminalWidget.this.selectNextTab();
/* 297 */                 return true;
/*     */               }
/* 299 */             })).withEnabledSupplier((Supplier<Boolean>)new Supplier<Boolean>()
/*     */             {
/*     */               public Boolean get() {
/* 302 */                 return Boolean.valueOf((AbstractTabbedTerminalWidget.this.myTabs != null && AbstractTabbedTerminalWidget.this.myTabs.getSelectedIndex() < AbstractTabbedTerminalWidget.this.myTabs.getTabCount() - 1));
/*     */               }
/* 305 */             }), (new TerminalAction(this.mySettingsProvider.getPreviousTabActionPresentation(), (Predicate<KeyEvent>)new Predicate<KeyEvent>()
/*     */             {
/*     */               public boolean apply(KeyEvent input) {
/* 308 */                 AbstractTabbedTerminalWidget.this.selectPreviousTab();
/* 309 */                 return true;
/*     */               }
/* 311 */             })).withEnabledSupplier((Supplier<Boolean>)new Supplier<Boolean>()
/*     */             {
/*     */               public Boolean get() {
/* 314 */                 return Boolean.valueOf((AbstractTabbedTerminalWidget.this.myTabs != null && AbstractTabbedTerminalWidget.this.myTabs.getSelectedIndex() > 0));
/*     */               }
/*     */             }) });
/*     */   }
/*     */ 
/*     */   
/*     */   private void selectPreviousTab() {
/* 321 */     this.myTabs.setSelectedIndex(this.myTabs.getSelectedIndex() - 1);
/*     */   }
/*     */   
/*     */   private void selectNextTab() {
/* 325 */     this.myTabs.setSelectedIndex(this.myTabs.getSelectedIndex() + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public TerminalActionProvider getNextProvider() {
/* 330 */     return this.myNextActionProvider;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNextProvider(TerminalActionProvider provider) {
/* 335 */     this.myNextActionProvider = provider;
/*     */   }
/*     */   
/*     */   private void handleNewSession() {
/* 339 */     this.myCreateNewSessionAction.apply(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class TabRenamer
/*     */   {
/*     */     public void install(final int selectedIndex, String text, final Component label, final RenameCallBack callBack) {
/* 352 */       final JTextField textField = createTextField();
/*     */       
/* 354 */       textField.setOpaque(false);
/*     */       
/* 356 */       textField.setDocument((Document)new JTextFieldLimit(50));
/* 357 */       textField.setText(text);
/*     */       
/* 359 */       final FocusAdapter focusAdapter = new FocusAdapter()
/*     */         {
/*     */           public void focusLost(FocusEvent focusEvent) {
/* 362 */             AbstractTabbedTerminalWidget.TabRenamer.finishRename(selectedIndex, label, textField.getText(), callBack);
/*     */           }
/*     */         };
/* 365 */       textField.addFocusListener(focusAdapter);
/* 366 */       textField.addKeyListener(new KeyAdapter()
/*     */           {
/*     */             public void keyPressed(KeyEvent keyEvent) {
/* 369 */               if (keyEvent.getKeyCode() == 27) {
/* 370 */                 textField.removeFocusListener(focusAdapter);
/* 371 */                 AbstractTabbedTerminalWidget.TabRenamer.finishRename(selectedIndex, label, null, callBack);
/*     */               }
/* 373 */               else if (keyEvent.getKeyCode() == 10) {
/* 374 */                 textField.removeFocusListener(focusAdapter);
/* 375 */                 AbstractTabbedTerminalWidget.TabRenamer.finishRename(selectedIndex, label, textField.getText(), callBack);
/*     */               } else {
/*     */                 
/* 378 */                 super.keyPressed(keyEvent);
/*     */               } 
/*     */             }
/*     */           });
/*     */       
/* 383 */       callBack.setComponent(textField);
/*     */ 
/*     */       
/* 386 */       textField.requestFocus();
/* 387 */       textField.selectAll();
/*     */     }
/*     */     
/*     */     protected JTextField createTextField() {
/* 391 */       return new JTextField();
/*     */     }
/*     */     
/*     */     private static void finishRename(int index, Component label, String newName, RenameCallBack callBack) {
/* 395 */       if (newName != null) {
/* 396 */         callBack.setNewName(index, newName);
/*     */       }
/* 398 */       callBack.setComponent(label);
/*     */     }
/*     */     public static interface RenameCallBack { void setComponent(Component param2Component);
/*     */       
/*     */       void setNewName(int param2Int, String param2String); }
/*     */   }
/*     */   
/*     */   private class TabComponent extends JPanel implements FocusListener { private T myTerminal;
/* 406 */     private MyLabelHolder myLabelHolder = new MyLabelHolder();
/*     */     
/*     */     private class MyLabelHolder extends JPanel { private MyLabelHolder() {}
/*     */       
/*     */       public void set(Component c) {
/* 411 */         AbstractTabbedTerminalWidget.TabComponent.this.myLabelHolder.removeAll();
/* 412 */         AbstractTabbedTerminalWidget.TabComponent.this.myLabelHolder.add(c);
/* 413 */         AbstractTabbedTerminalWidget.TabComponent.this.myLabelHolder.validate();
/* 414 */         AbstractTabbedTerminalWidget.TabComponent.this.myLabelHolder.repaint();
/*     */       } }
/*     */ 
/*     */     
/*     */     class TabComponentLabel extends JLabel {
/*     */       AbstractTabbedTerminalWidget<T>.TabComponent getTabComponent() {
/* 420 */         return AbstractTabbedTerminalWidget.TabComponent.this;
/*     */       }
/*     */       
/*     */       public String getText() {
/* 424 */         if (AbstractTabbedTerminalWidget.this.myTabs != null) {
/* 425 */           int i = AbstractTabbedTerminalWidget.this.myTabs.indexOfTabComponent(AbstractTabbedTerminalWidget.TabComponent.this);
/* 426 */           if (i != -1) {
/* 427 */             return AbstractTabbedTerminalWidget.this.myTabs.getTitleAt(i);
/*     */           }
/*     */         } 
/* 430 */         return null;
/*     */       }
/*     */     }
/*     */     
/*     */     private TabComponent(final AbstractTabs<T> tabs, final T terminal) {
/* 435 */       super(new FlowLayout(0, 0, 0));
/* 436 */       this.myTerminal = terminal;
/* 437 */       setOpaque(false);
/*     */       
/* 439 */       setFocusable(false);
/*     */       
/* 441 */       addFocusListener(this);
/*     */ 
/*     */       
/* 444 */       JLabel label = new TabComponentLabel();
/*     */       
/* 446 */       label.addFocusListener(this);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 451 */       label.addMouseListener(new MouseAdapter()
/*     */           {
/*     */             public void mouseReleased(MouseEvent event)
/*     */             {
/* 455 */               AbstractTabbedTerminalWidget.TabComponent.this.handleMouse(event);
/*     */             }
/*     */ 
/*     */             
/*     */             public void mousePressed(MouseEvent event) {
/* 460 */               tabs.setSelectedComponent(terminal);
/* 461 */               AbstractTabbedTerminalWidget.TabComponent.this.handleMouse(event);
/*     */             }
/*     */           });
/*     */       
/* 465 */       this.myLabelHolder.set(label);
/* 466 */       add(this.myLabelHolder);
/*     */     }
/*     */     
/*     */     protected void handleMouse(MouseEvent event) {
/* 470 */       if (event.isPopupTrigger()) {
/* 471 */         JPopupMenu menu = createPopup();
/* 472 */         menu.show(event.getComponent(), event.getX(), event.getY());
/*     */       
/*     */       }
/* 475 */       else if (event.getClickCount() == 2 && !event.isConsumed()) {
/* 476 */         event.consume();
/* 477 */         renameTab();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected JPopupMenu createPopup() {
/* 483 */       JPopupMenu popupMenu = new JPopupMenu();
/*     */       
/* 485 */       TerminalAction.addToMenu(popupMenu, AbstractTabbedTerminalWidget.this);
/*     */       
/* 487 */       JMenuItem rename = new JMenuItem("Rename Tab");
/*     */       
/* 489 */       rename.addActionListener(new ActionListener()
/*     */           {
/*     */             public void actionPerformed(ActionEvent actionEvent) {
/* 492 */               AbstractTabbedTerminalWidget.TabComponent.this.renameTab();
/*     */             }
/*     */           });
/*     */       
/* 496 */       popupMenu.add(rename);
/*     */       
/* 498 */       return popupMenu;
/*     */     }
/*     */     
/*     */     private void renameTab() {
/* 502 */       int selectedIndex = AbstractTabbedTerminalWidget.this.myTabs.getSelectedIndex();
/* 503 */       JLabel label = (JLabel)this.myLabelHolder.getComponent(0);
/*     */       
/* 505 */       (new AbstractTabbedTerminalWidget.TabRenamer()).install(selectedIndex, label.getText(), label, new AbstractTabbedTerminalWidget.TabRenamer.RenameCallBack()
/*     */           {
/*     */             public void setComponent(Component c) {
/* 508 */               AbstractTabbedTerminalWidget.TabComponent.this.myLabelHolder.set(c);
/*     */             }
/*     */ 
/*     */             
/*     */             public void setNewName(int index, String name) {
/* 513 */               if (AbstractTabbedTerminalWidget.this.myTabs != null) {
/* 514 */                 AbstractTabbedTerminalWidget.this.myTabs.setTitleAt(index, name);
/*     */               }
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*     */     public void focusGained(FocusEvent e) {
/* 522 */       this.myTerminal.getComponent().requestFocusInWindow();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void focusLost(FocusEvent e) {} }
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractTabs<T> getTerminalTabs() {
/* 532 */     return this.myTabs;
/*     */   }
/*     */ 
/*     */   
/*     */   public JComponent getComponent() {
/* 537 */     return this.myPanel;
/*     */   }
/*     */   
/*     */   public JComponent getFocusableComponent() {
/* 541 */     return (this.myTabs != null) ? this.myTabs.getComponent() : ((this.myTermWidget != null) ? (JComponent)this.myTermWidget : this);
/*     */   }
/*     */ 
/*     */   
/*     */   public JComponent getPreferredFocusableComponent() {
/* 546 */     return getFocusableComponent();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canOpenSession() {
/* 551 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTerminalPanelListener(TerminalPanelListener terminalPanelListener) {
/* 556 */     if (this.myTabs != null) {
/* 557 */       for (int i = 0; i < this.myTabs.getTabCount(); i++) {
/* 558 */         getTerminalPanel(i).setTerminalPanelListener(terminalPanelListener);
/*     */       }
/* 560 */     } else if (this.myTermWidget != null) {
/* 561 */       this.myTermWidget.setTerminalPanelListener(terminalPanelListener);
/*     */     } 
/* 563 */     this.myTerminalPanelListener = terminalPanelListener;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public T getCurrentSession() {
/* 569 */     if (this.myTabs != null) {
/* 570 */       return getTerminalPanel(this.myTabs.getSelectedIndex());
/*     */     }
/*     */     
/* 573 */     return this.myTermWidget;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TerminalDisplay getTerminalDisplay() {
/* 579 */     return getCurrentSession().getTerminalDisplay();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private T getTerminalPanel(int index) {
/* 584 */     if (index < this.myTabs.getTabCount() && index >= 0) {
/* 585 */       return this.myTabs.getComponentAt(index);
/*     */     }
/*     */     
/* 588 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addTabListener(TabListener listener) {
/* 593 */     this.myTabListeners.add(listener);
/*     */   }
/*     */   
/*     */   public void removeTabListener(TabListener listener) {
/* 597 */     this.myTabListeners.remove(listener);
/*     */   }
/*     */   
/*     */   private void fireTabClosed(T terminal) {
/* 601 */     for (TabListener<T> l : this.myTabListeners) {
/* 602 */       l.tabClosed(terminal);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addListener(TerminalWidgetListener listener) {
/* 612 */     this.myWidgetListeners.add(listener);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeListener(TerminalWidgetListener listener) {
/* 617 */     this.myWidgetListeners.remove(listener);
/*     */   }
/*     */   
/*     */   public TabbedSettingsProvider getSettingsProvider() {
/* 621 */     return this.mySettingsProvider;
/*     */   }
/*     */   
/*     */   public abstract T createInnerTerminalWidget();
/*     */   
/*     */   protected abstract AbstractTabs<T> createTabbedPane();
/*     */   
/*     */   public static interface TabListener<T extends JediTermWidget> {
/*     */     void tabClosed(T param1T);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\termina\\ui\AbstractTabbedTerminalWidget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */