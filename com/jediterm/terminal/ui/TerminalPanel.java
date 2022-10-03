/*      */ package com.jediterm.terminal.ui;
/*      */ import com.jediterm.terminal.CursorShape;
/*      */ import com.jediterm.terminal.HyperlinkStyle;
/*      */ import com.jediterm.terminal.SubstringFinder;
/*      */ import com.jediterm.terminal.TextStyle;
/*      */ import com.jediterm.terminal.emulator.mouse.MouseMode;
/*      */ import com.jediterm.terminal.emulator.mouse.TerminalMouseListener;
/*      */ import com.jediterm.terminal.model.CharBuffer;
/*      */ import com.jediterm.terminal.model.SelectionUtil;
/*      */ import com.jediterm.terminal.model.TerminalLine;
/*      */ import com.jediterm.terminal.model.TerminalLineIntervalHighlighting;
/*      */ import com.jediterm.terminal.model.TerminalSelection;
/*      */ import com.jediterm.terminal.model.TerminalTextBuffer;
/*      */ import com.jediterm.terminal.model.hyperlinks.LinkInfo;
/*      */ import com.jediterm.terminal.util.Pair;
/*      */ import java.awt.Color;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.RenderingHints;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.awt.event.KeyListener;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseWheelEvent;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.text.AttributedCharacterIterator;
/*      */ import java.util.List;
/*      */ import javax.swing.JPopupMenu;
/*      */ import javax.swing.JScrollBar;
/*      */ import javax.swing.Timer;
/*      */ import org.jetbrains.annotations.NotNull;
/*      */ import org.jetbrains.annotations.Nullable;
/*      */ 
/*      */ public class TerminalPanel extends JComponent implements TerminalDisplay, TerminalActionProvider {
/*   40 */   private static final Logger LOG = Logger.getLogger(TerminalPanel.class);
/*      */   
/*      */   private static final long serialVersionUID = -1048763516632093014L;
/*      */   
/*      */   public static final double SCROLL_SPEED = 0.05D;
/*      */   
/*      */   private Font myNormalFont;
/*      */   private Font myItalicFont;
/*      */   private Font myBoldFont;
/*      */   private Font myBoldItalicFont;
/*   50 */   private int myDescent = 0;
/*   51 */   private int mySpaceBetweenLines = 0;
/*   52 */   protected Dimension myCharSize = new Dimension();
/*      */   private boolean myMonospaced;
/*   54 */   protected Dimension myTermSize = new Dimension(80, 24);
/*      */   
/*   56 */   private TerminalStarter myTerminalStarter = null;
/*      */   
/*   58 */   private MouseMode myMouseMode = MouseMode.MOUSE_REPORTING_NONE;
/*   59 */   private Point mySelectionStartPoint = null;
/*   60 */   private TerminalSelection mySelection = null;
/*      */   
/*      */   private final TerminalCopyPasteHandler myCopyPasteHandler;
/*      */   
/*      */   private TerminalPanelListener myTerminalPanelListener;
/*      */   
/*      */   private final SettingsProvider mySettingsProvider;
/*      */   
/*      */   private final TerminalTextBuffer myTerminalTextBuffer;
/*      */   
/*      */   private final StyleState myStyleState;
/*      */   
/*   72 */   private final TerminalCursor myCursor = new TerminalCursor();
/*      */ 
/*      */   
/*   75 */   private final BoundedRangeModel myBoundedRangeModel = new DefaultBoundedRangeModel(0, 80, 0, 80);
/*      */   
/*      */   private boolean myScrollingEnabled = true;
/*      */   protected int myClientScrollOrigin;
/*   79 */   private final List<KeyListener> myCustomKeyListeners = new CopyOnWriteArrayList<>();
/*      */   
/*   81 */   private String myWindowTitle = "Terminal";
/*      */   
/*      */   private TerminalActionProvider myNextActionProvider;
/*      */   
/*      */   private String myInputMethodUncommittedChars;
/*      */   private Timer myRepaintTimer;
/*   87 */   private AtomicInteger scrollDy = new AtomicInteger(0);
/*   88 */   private AtomicBoolean needRepaint = new AtomicBoolean(true);
/*      */   
/*   90 */   private int myMaxFPS = 50;
/*   91 */   private int myBlinkingPeriod = 500;
/*      */   
/*      */   private TerminalCoordinates myCoordsAccessor;
/*      */   
/*      */   private String myCurrentPath;
/*      */   private SubstringFinder.FindResult myFindResult;
/*   97 */   private LinkInfo myHoveredHyperlink = null;
/*      */   
/*   99 */   private int myCursorType = 0;
/*  100 */   private final TerminalKeyHandler myTerminalKeyHandler = new TerminalKeyHandler();
/*      */   private LinkInfo.HoverConsumer myLinkHoverConsumer;
/*      */   
/*      */   public TerminalPanel(@NotNull SettingsProvider settingsProvider, @NotNull TerminalTextBuffer terminalTextBuffer, @NotNull StyleState styleState) {
/*  104 */     this.mySettingsProvider = settingsProvider;
/*  105 */     this.myTerminalTextBuffer = terminalTextBuffer;
/*  106 */     this.myStyleState = styleState;
/*  107 */     this.myTermSize.width = terminalTextBuffer.getWidth();
/*  108 */     this.myTermSize.height = terminalTextBuffer.getHeight();
/*  109 */     this.myMaxFPS = this.mySettingsProvider.maxRefreshRate();
/*  110 */     this.myCopyPasteHandler = createCopyPasteHandler();
/*      */     
/*  112 */     updateScrolling(true);
/*      */     
/*  114 */     enableEvents(2056L);
/*  115 */     enableInputMethods(true);
/*      */     
/*  117 */     terminalTextBuffer.addModelListener(new TerminalModelListener()
/*      */         {
/*      */           public void modelChanged() {
/*  120 */             TerminalPanel.this.repaint();
/*      */           }
/*      */         });
/*      */   }
/*      */   
/*      */   @NotNull
/*      */   protected TerminalCopyPasteHandler createCopyPasteHandler() {
/*  127 */     return (TerminalCopyPasteHandler)new DefaultTerminalCopyPasteHandler();
/*      */   }
/*      */   
/*      */   public TerminalPanelListener getTerminalPanelListener() {
/*  131 */     return this.myTerminalPanelListener;
/*      */   }
/*      */ 
/*      */   
/*      */   public void repaint() {
/*  136 */     this.needRepaint.set(true);
/*      */   }
/*      */   
/*      */   private void doRepaint() {
/*  140 */     super.repaint();
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   protected void reinitFontAndResize() {
/*  145 */     initFont();
/*      */     
/*  147 */     sizeTerminalFromComponent();
/*      */   }
/*      */   
/*      */   protected void initFont() {
/*  151 */     this.myNormalFont = createFont();
/*  152 */     this.myBoldFont = this.myNormalFont.deriveFont(1);
/*  153 */     this.myItalicFont = this.myNormalFont.deriveFont(2);
/*  154 */     this.myBoldItalicFont = this.myNormalFont.deriveFont(3);
/*      */     
/*  156 */     establishFontMetrics();
/*      */   }
/*      */   
/*      */   public void init(@NotNull JScrollBar scrollBar) {
/*  160 */     if (scrollBar == null) $$$reportNull$$$0(3);  initFont();
/*      */     
/*  162 */     setPreferredSize(new Dimension(getPixelWidth(), getPixelHeight()));
/*      */     
/*  164 */     setFocusable(true);
/*  165 */     enableInputMethods(true);
/*  166 */     setDoubleBuffered(true);
/*      */     
/*  168 */     setFocusTraversalKeysEnabled(false);
/*      */     
/*  170 */     addMouseMotionListener(new MouseMotionAdapter()
/*      */         {
/*      */           public void mouseMoved(MouseEvent e) {
/*  173 */             TerminalPanel.this.handleHyperlinks(e.getPoint(), e.isControlDown());
/*      */           }
/*      */ 
/*      */           
/*      */           public void mouseDragged(MouseEvent e) {
/*  178 */             if (!TerminalPanel.this.isLocalMouseAction(e)) {
/*      */               return;
/*      */             }
/*      */             
/*  182 */             Point charCoords = TerminalPanel.this.panelToCharCoords(e.getPoint());
/*      */             
/*  184 */             if (TerminalPanel.this.mySelection == null) {
/*      */               
/*  186 */               if (TerminalPanel.this.mySelectionStartPoint == null) {
/*  187 */                 TerminalPanel.this.mySelectionStartPoint = charCoords;
/*      */               }
/*  189 */               TerminalPanel.this.mySelection = new TerminalSelection(new Point(TerminalPanel.this.mySelectionStartPoint));
/*      */             } 
/*  191 */             TerminalPanel.this.repaint();
/*  192 */             TerminalPanel.this.mySelection.updateEnd(charCoords);
/*  193 */             if (TerminalPanel.this.mySettingsProvider.copyOnSelect()) {
/*  194 */               TerminalPanel.this.handleCopyOnSelect();
/*      */             }
/*      */             
/*  197 */             if ((e.getPoint()).y < 0) {
/*  198 */               TerminalPanel.this.moveScrollBar((int)((e.getPoint()).y * 0.05D));
/*      */             }
/*  200 */             if ((e.getPoint()).y > TerminalPanel.this.getPixelHeight()) {
/*  201 */               TerminalPanel.this.moveScrollBar((int)(((e.getPoint()).y - TerminalPanel.this.getPixelHeight()) * 0.05D));
/*      */             }
/*      */           }
/*      */         });
/*      */     
/*  206 */     addMouseWheelListener(e -> {
/*      */           if (isLocalMouseAction(e)) {
/*      */             handleMouseWheelEvent(e, scrollBar);
/*      */           }
/*      */         });
/*      */     
/*  212 */     addMouseListener(new MouseAdapter()
/*      */         {
/*      */           public void mouseExited(MouseEvent e) {
/*  215 */             if (TerminalPanel.this.myLinkHoverConsumer != null) {
/*  216 */               TerminalPanel.this.myLinkHoverConsumer.onMouseExited();
/*  217 */               TerminalPanel.this.myLinkHoverConsumer = null;
/*      */             } 
/*      */           }
/*      */ 
/*      */           
/*      */           public void mousePressed(MouseEvent e) {
/*  223 */             if (e.getButton() == 1 && 
/*  224 */               e.getClickCount() == 1) {
/*  225 */               TerminalPanel.this.mySelectionStartPoint = TerminalPanel.this.panelToCharCoords(e.getPoint());
/*  226 */               TerminalPanel.this.mySelection = null;
/*  227 */               TerminalPanel.this.repaint();
/*      */             } 
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*      */           public void mouseReleased(MouseEvent e) {
/*  234 */             TerminalPanel.this.requestFocusInWindow();
/*  235 */             TerminalPanel.this.repaint();
/*      */           }
/*      */ 
/*      */           
/*      */           public void mouseClicked(MouseEvent e) {
/*  240 */             TerminalPanel.this.requestFocusInWindow();
/*  241 */             HyperlinkStyle hyperlink = TerminalPanel.this.isFollowLinkEvent(e) ? TerminalPanel.this.findHyperlink(e.getPoint()) : null;
/*  242 */             if (hyperlink != null) {
/*  243 */               hyperlink.getLinkInfo().navigate();
/*  244 */             } else if (e.getButton() == 1 && TerminalPanel.this.isLocalMouseAction(e)) {
/*  245 */               int count = e.getClickCount();
/*  246 */               if (count != 1)
/*      */               {
/*  248 */                 if (count == 2) {
/*      */                   
/*  250 */                   Point charCoords = TerminalPanel.this.panelToCharCoords(e.getPoint());
/*  251 */                   Point start = SelectionUtil.getPreviousSeparator(charCoords, TerminalPanel.this.myTerminalTextBuffer);
/*  252 */                   Point stop = SelectionUtil.getNextSeparator(charCoords, TerminalPanel.this.myTerminalTextBuffer);
/*  253 */                   TerminalPanel.this.mySelection = new TerminalSelection(start);
/*  254 */                   TerminalPanel.this.mySelection.updateEnd(stop);
/*      */                   
/*  256 */                   if (TerminalPanel.this.mySettingsProvider.copyOnSelect()) {
/*  257 */                     TerminalPanel.this.handleCopyOnSelect();
/*      */                   }
/*  259 */                 } else if (count == 3) {
/*      */                   
/*  261 */                   Point charCoords = TerminalPanel.this.panelToCharCoords(e.getPoint());
/*  262 */                   int startLine = charCoords.y;
/*  263 */                   while (startLine > -TerminalPanel.this.getScrollBuffer().getLineCount() && TerminalPanel.this
/*  264 */                     .myTerminalTextBuffer.getLine(startLine - 1).isWrapped()) {
/*  265 */                     startLine--;
/*      */                   }
/*  267 */                   int endLine = charCoords.y;
/*  268 */                   while (endLine < TerminalPanel.this.myTerminalTextBuffer.getHeight() && TerminalPanel.this
/*  269 */                     .myTerminalTextBuffer.getLine(endLine).isWrapped()) {
/*  270 */                     endLine++;
/*      */                   }
/*  272 */                   TerminalPanel.this.mySelection = new TerminalSelection(new Point(0, startLine));
/*  273 */                   TerminalPanel.this.mySelection.updateEnd(new Point(TerminalPanel.this.myTermSize.width, endLine));
/*      */                   
/*  275 */                   if (TerminalPanel.this.mySettingsProvider.copyOnSelect())
/*  276 */                     TerminalPanel.this.handleCopyOnSelect(); 
/*      */                 } 
/*      */               }
/*  279 */             } else if (e.getButton() == 2 && TerminalPanel.this.mySettingsProvider.pasteOnMiddleMouseClick() && TerminalPanel.this.isLocalMouseAction(e)) {
/*  280 */               TerminalPanel.this.handlePasteSelection();
/*  281 */             } else if (e.getButton() == 3) {
/*  282 */               HyperlinkStyle contextHyperlink = TerminalPanel.this.findHyperlink(e.getPoint());
/*  283 */               JPopupMenu popup = TerminalPanel.this.createPopupMenu((contextHyperlink != null) ? contextHyperlink.getLinkInfo() : null, e);
/*  284 */               popup.show(e.getComponent(), e.getX(), e.getY());
/*      */             } 
/*  286 */             TerminalPanel.this.repaint();
/*      */           }
/*      */         });
/*      */     
/*  290 */     addComponentListener(new ComponentAdapter()
/*      */         {
/*      */           public void componentResized(ComponentEvent e) {
/*  293 */             TerminalPanel.this.sizeTerminalFromComponent();
/*      */           }
/*      */         });
/*      */     
/*  297 */     addFocusListener(new FocusAdapter()
/*      */         {
/*      */           public void focusGained(FocusEvent e) {
/*  300 */             TerminalPanel.this.myCursor.cursorChanged();
/*      */           }
/*      */ 
/*      */           
/*      */           public void focusLost(FocusEvent e) {
/*  305 */             TerminalPanel.this.myCursor.cursorChanged();
/*      */             
/*  307 */             TerminalPanel.this.handleHyperlinks(e.getComponent(), false);
/*      */           }
/*      */         });
/*      */     
/*  311 */     this.myBoundedRangeModel.addChangeListener(new ChangeListener()
/*      */         {
/*      */           public void stateChanged(ChangeEvent e)
/*      */           {
/*  315 */             TerminalPanel.this.myClientScrollOrigin = TerminalPanel.this.myBoundedRangeModel.getValue();
/*  316 */             TerminalPanel.this.repaint();
/*      */           }
/*      */         });
/*      */     
/*  320 */     createRepaintTimer();
/*      */   }
/*      */   
/*      */   private boolean isFollowLinkEvent(@NotNull MouseEvent e) {
/*  324 */     if (e == null) $$$reportNull$$$0(4);  return (this.myCursorType == 12 && e.getButton() == 1);
/*      */   }
/*      */   
/*      */   protected void handleMouseWheelEvent(@NotNull MouseWheelEvent e, @NotNull JScrollBar scrollBar) {
/*  328 */     if (e == null) $$$reportNull$$$0(5);  if (scrollBar == null) $$$reportNull$$$0(6);  if (e.isShiftDown() || e.getUnitsToScroll() == 0 || Math.abs(e.getPreciseWheelRotation()) < 0.01D) {
/*      */       return;
/*      */     }
/*  331 */     moveScrollBar(e.getUnitsToScroll());
/*  332 */     e.consume();
/*      */   }
/*      */   
/*      */   private void handleHyperlinks(@NotNull Point panelPoint, boolean isControlDown) {
/*  336 */     if (panelPoint == null) $$$reportNull$$$0(7);  Cell cell = panelPointToCell(panelPoint);
/*  337 */     HyperlinkStyle linkStyle = findHyperlink(cell);
/*  338 */     LinkInfo.HoverConsumer linkHoverConsumer = (linkStyle != null) ? linkStyle.getLinkInfo().getHoverConsumer() : null;
/*  339 */     if (linkHoverConsumer != this.myLinkHoverConsumer) {
/*  340 */       if (this.myLinkHoverConsumer != null) {
/*  341 */         this.myLinkHoverConsumer.onMouseExited();
/*      */       }
/*  343 */       if (linkHoverConsumer != null) {
/*  344 */         LineCellInterval lineCellInterval = findIntervalWithStyle(cell, linkStyle);
/*  345 */         linkHoverConsumer.onMouseEntered(this, getBounds(lineCellInterval));
/*      */       } 
/*      */     } 
/*  348 */     this.myLinkHoverConsumer = linkHoverConsumer;
/*  349 */     if (linkStyle != null && (
/*  350 */       linkStyle.getHighlightMode() == HyperlinkStyle.HighlightMode.ALWAYS || (linkStyle.getHighlightMode() == HyperlinkStyle.HighlightMode.HOVER && isControlDown))) {
/*  351 */       updateCursor(12);
/*  352 */       this.myHoveredHyperlink = linkStyle.getLinkInfo();
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  357 */     this.myHoveredHyperlink = null;
/*  358 */     if (this.myCursorType != 0) {
/*  359 */       updateCursor(0);
/*  360 */       repaint();
/*      */     } 
/*      */   }
/*      */   @NotNull
/*      */   private LineCellInterval findIntervalWithStyle(@NotNull Cell initialCell, @NotNull HyperlinkStyle style) {
/*  365 */     if (initialCell == null) $$$reportNull$$$0(8);  if (style == null) $$$reportNull$$$0(9);  int startColumn = initialCell.getColumn();
/*  366 */     while (startColumn > 0 && style == this.myTerminalTextBuffer.getStyleAt(startColumn - 1, initialCell.getLine())) {
/*  367 */       startColumn--;
/*      */     }
/*  369 */     int endColumn = initialCell.getColumn();
/*  370 */     while (endColumn < this.myTerminalTextBuffer.getWidth() - 1 && style == this.myTerminalTextBuffer.getStyleAt(endColumn + 1, initialCell.getLine())) {
/*  371 */       endColumn++;
/*      */     }
/*  373 */     return new LineCellInterval(initialCell.getLine(), startColumn, endColumn);
/*      */   }
/*      */   
/*      */   private void handleHyperlinks(Component component, boolean controlDown) {
/*  377 */     PointerInfo a = MouseInfo.getPointerInfo();
/*  378 */     if (a != null) {
/*  379 */       Point b = a.getLocation();
/*  380 */       SwingUtilities.convertPointFromScreen(b, component);
/*  381 */       handleHyperlinks(b, controlDown);
/*      */     } 
/*      */   }
/*      */   @Nullable
/*      */   private HyperlinkStyle findHyperlink(@NotNull Point p) {
/*  386 */     if (p == null) $$$reportNull$$$0(10);  return findHyperlink(panelPointToCell(p));
/*      */   }
/*      */   @Nullable
/*      */   private HyperlinkStyle findHyperlink(@Nullable Cell cell) {
/*  390 */     if (cell != null && cell.getColumn() >= 0 && cell.getColumn() < this.myTerminalTextBuffer.getWidth() && cell
/*  391 */       .getLine() >= -this.myTerminalTextBuffer.getHistoryLinesCount() && cell.getLine() <= this.myTerminalTextBuffer.getHeight()) {
/*  392 */       TextStyle style = this.myTerminalTextBuffer.getStyleAt(cell.getColumn(), cell.getLine());
/*  393 */       if (style instanceof HyperlinkStyle) {
/*  394 */         return (HyperlinkStyle)style;
/*      */       }
/*      */     } 
/*  397 */     return null;
/*      */   }
/*      */   
/*      */   private void updateCursor(int cursorType) {
/*  401 */     if (cursorType != this.myCursorType) {
/*  402 */       this.myCursorType = cursorType;
/*      */       
/*  404 */       setCursor(new Cursor(this.myCursorType));
/*      */     } 
/*      */   }
/*      */   
/*      */   private void createRepaintTimer() {
/*  409 */     if (this.myRepaintTimer != null) {
/*  410 */       this.myRepaintTimer.stop();
/*      */     }
/*  412 */     this.myRepaintTimer = new Timer(1000 / this.myMaxFPS, new WeakRedrawTimer(this));
/*  413 */     this.myRepaintTimer.start();
/*      */   }
/*      */   
/*      */   public boolean isLocalMouseAction(MouseEvent e) {
/*  417 */     return (this.mySettingsProvider.forceActionOnMouseReporting() || isMouseReporting() == e.isShiftDown());
/*      */   }
/*      */   
/*      */   public boolean isRemoteMouseAction(MouseEvent e) {
/*  421 */     return (isMouseReporting() && !e.isShiftDown());
/*      */   }
/*      */   
/*      */   protected boolean isRetina() {
/*  425 */     return UIUtil.isRetina();
/*      */   }
/*      */   
/*      */   public void setBlinkingPeriod(int blinkingPeriod) {
/*  429 */     this.myBlinkingPeriod = blinkingPeriod;
/*      */   }
/*      */   
/*      */   public void setCoordAccessor(TerminalCoordinates coordAccessor) {
/*  433 */     this.myCoordsAccessor = coordAccessor;
/*      */   }
/*      */   
/*      */   public void setFindResult(SubstringFinder.FindResult findResult) {
/*  437 */     this.myFindResult = findResult;
/*  438 */     repaint();
/*      */   }
/*      */   
/*      */   public SubstringFinder.FindResult getFindResult() {
/*  442 */     return this.myFindResult;
/*      */   }
/*      */   
/*      */   public SubstringFinder.FindResult.FindItem selectPrevFindResultItem() {
/*  446 */     return selectPrevOrNextFindResultItem(false);
/*      */   }
/*      */   
/*      */   public SubstringFinder.FindResult.FindItem selectNextFindResultItem() {
/*  450 */     return selectPrevOrNextFindResultItem(true);
/*      */   }
/*      */   
/*      */   protected SubstringFinder.FindResult.FindItem selectPrevOrNextFindResultItem(boolean next) {
/*  454 */     if (this.myFindResult != null) {
/*  455 */       SubstringFinder.FindResult.FindItem item = next ? this.myFindResult.nextFindItem() : this.myFindResult.prevFindItem();
/*  456 */       if (item != null) {
/*  457 */         this
/*  458 */           .mySelection = new TerminalSelection(new Point((item.getStart()).x, (item.getStart()).y - this.myTerminalTextBuffer.getHistoryLinesCount()), new Point((item.getEnd()).x, (item.getEnd()).y - this.myTerminalTextBuffer.getHistoryLinesCount()));
/*  459 */         if ((this.mySelection.getStart()).y < getTerminalTextBuffer().getHeight() / 2) {
/*  460 */           this.myBoundedRangeModel.setValue((this.mySelection.getStart()).y - getTerminalTextBuffer().getHeight() / 2);
/*      */         } else {
/*  462 */           this.myBoundedRangeModel.setValue(0);
/*      */         } 
/*  464 */         repaint();
/*  465 */         return item;
/*      */       } 
/*      */     } 
/*  468 */     return null;
/*      */   }
/*      */   
/*      */   static class WeakRedrawTimer
/*      */     implements ActionListener {
/*      */     private WeakReference<TerminalPanel> ref;
/*      */     
/*      */     public WeakRedrawTimer(TerminalPanel terminalPanel) {
/*  476 */       this.ref = new WeakReference<>(terminalPanel);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformed(ActionEvent e) {
/*  481 */       TerminalPanel terminalPanel = this.ref.get();
/*  482 */       if (terminalPanel != null) {
/*  483 */         terminalPanel.myCursor.changeStateIfNeeded();
/*  484 */         terminalPanel.updateScrolling(false);
/*  485 */         if (terminalPanel.needRepaint.getAndSet(false)) {
/*      */           try {
/*  487 */             terminalPanel.doRepaint();
/*  488 */           } catch (Exception ex) {
/*  489 */             TerminalPanel.LOG.error("Error while terminal panel redraw", ex);
/*      */           } 
/*      */         }
/*      */       } else {
/*  493 */         Timer timer = (Timer)e.getSource();
/*  494 */         timer.removeActionListener(this);
/*  495 */         timer.stop();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void terminalMouseModeSet(MouseMode mode) {
/*  502 */     this.myMouseMode = mode;
/*      */   }
/*      */   
/*      */   private boolean isMouseReporting() {
/*  506 */     return (this.myMouseMode != MouseMode.MOUSE_REPORTING_NONE);
/*      */   }
/*      */   
/*      */   private void scrollToBottom() {
/*  510 */     this.myBoundedRangeModel.setValue(this.myTermSize.height);
/*      */   }
/*      */   
/*      */   private void pageUp() {
/*  514 */     moveScrollBar(-this.myTermSize.height);
/*      */   }
/*      */   
/*      */   private void pageDown() {
/*  518 */     moveScrollBar(this.myTermSize.height);
/*      */   }
/*      */   
/*      */   private void scrollUp() {
/*  522 */     moveScrollBar(-1);
/*      */   }
/*      */   
/*      */   private void scrollDown() {
/*  526 */     moveScrollBar(1);
/*      */   }
/*      */   
/*      */   private void moveScrollBar(int k) {
/*  530 */     this.myBoundedRangeModel.setValue(this.myBoundedRangeModel.getValue() + k);
/*      */   }
/*      */   
/*      */   protected Font createFont() {
/*  534 */     return this.mySettingsProvider.getTerminalFont();
/*      */   }
/*      */   @NotNull
/*      */   private Point panelToCharCoords(Point p) {
/*  538 */     Cell cell = panelPointToCell(p);
/*  539 */     return new Point(cell.getColumn(), cell.getLine());
/*      */   }
/*      */   @NotNull
/*      */   private Cell panelPointToCell(@NotNull Point p) {
/*  543 */     if (p == null) $$$reportNull$$$0(11);  int y = Math.min(p.y / this.myCharSize.height, getRowCount() - 1) + this.myClientScrollOrigin;
/*      */     
/*  545 */     TerminalLine buffer = this.myTerminalTextBuffer.getLine(y);
/*      */     
/*  547 */     int bufferLen = buffer.getText().length();
/*  548 */     int insetX = p.x - getInsetX();
/*  549 */     int _x = 0;
/*  550 */     int x = 0;
/*  551 */     for (int i = 0; i < bufferLen && 
/*  552 */       insetX > _x; i++) {
/*  553 */       char c = buffer.charAt(i);
/*  554 */       _x += getGraphics().getFontMetrics(getFontToDisplay(c, TextStyle.EMPTY)).charWidth(c);
/*  555 */       if (insetX > _x) {
/*  556 */         x++;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  563 */     x = Math.min(x, getColumnCount() - 1);
/*  564 */     x = Math.max(0, x);
/*  565 */     return new Cell(y, x);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void copySelection(@Nullable Point selectionStart, @Nullable Point selectionEnd, boolean useSystemSelectionClipboardIfAvailable) {
/*  571 */     if (selectionStart == null || selectionEnd == null) {
/*      */       return;
/*      */     }
/*  574 */     String selectionText = SelectionUtil.getSelectionText(selectionStart, selectionEnd, this.myTerminalTextBuffer);
/*  575 */     if (selectionText.length() != 0) {
/*  576 */       this.myCopyPasteHandler.setContents(selectionText, useSystemSelectionClipboardIfAvailable);
/*      */     }
/*      */   }
/*      */   
/*      */   private void pasteFromClipboard(boolean useSystemSelectionClipboardIfAvailable) {
/*  581 */     String text = this.myCopyPasteHandler.getContents(useSystemSelectionClipboardIfAvailable);
/*      */     
/*  583 */     if (text == null) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  590 */       if (!UIUtil.isWindows)
/*      */       {
/*      */ 
/*      */         
/*  594 */         text = text.replace("\r\n", "\n");
/*      */       }
/*  596 */       text = text.replace('\n', '\r');
/*      */       
/*  598 */       this.myTerminalStarter.sendString(text);
/*  599 */     } catch (RuntimeException e) {
/*  600 */       LOG.info(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private String getClipboardString() {
/*  606 */     return this.myCopyPasteHandler.getContents(false);
/*      */   }
/*      */   
/*      */   protected void drawImage(Graphics2D gfx, BufferedImage image, int x, int y, ImageObserver observer) {
/*  610 */     gfx.drawImage(image, x, y, image
/*  611 */         .getWidth(), image.getHeight(), observer);
/*      */   }
/*      */   
/*      */   protected BufferedImage createBufferedImage(int width, int height) {
/*  615 */     return new BufferedImage(width, height, 1);
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Dimension getTerminalSizeFromComponent() {
/*  621 */     int newWidth = (getWidth() - getInsetX()) / this.myCharSize.width;
/*  622 */     int newHeight = getHeight() / this.myCharSize.height;
/*  623 */     return (newHeight > 0 && newWidth > 0) ? new Dimension(newWidth, newHeight) : null;
/*      */   }
/*      */   
/*      */   private void sizeTerminalFromComponent() {
/*  627 */     if (this.myTerminalStarter != null) {
/*  628 */       Dimension newSize = getTerminalSizeFromComponent();
/*  629 */       if (newSize != null) {
/*  630 */         JediTerminal.ensureTermMinimumSize(newSize);
/*  631 */         if (!this.myTermSize.equals(newSize)) {
/*  632 */           this.myTerminalStarter.postResize(newSize, RequestOrigin.User);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setTerminalStarter(TerminalStarter terminalStarter) {
/*  639 */     this.myTerminalStarter = terminalStarter;
/*  640 */     sizeTerminalFromComponent();
/*      */   }
/*      */   
/*      */   public void addCustomKeyListener(@NotNull KeyListener keyListener) {
/*  644 */     if (keyListener == null) $$$reportNull$$$0(12);  this.myCustomKeyListeners.add(keyListener);
/*      */   }
/*      */   
/*      */   public void removeCustomKeyListener(@NotNull KeyListener keyListener) {
/*  648 */     if (keyListener == null) $$$reportNull$$$0(13);  this.myCustomKeyListeners.remove(keyListener);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void requestResize(@NotNull Dimension newSize, RequestOrigin origin, int cursorX, int cursorY, JediTerminal.ResizeHandler resizeHandler) {
/*  656 */     if (newSize == null) $$$reportNull$$$0(14);  if (!newSize.equals(this.myTermSize)) {
/*  657 */       this.myTerminalTextBuffer.resize(newSize, origin, cursorX, cursorY, resizeHandler, this.mySelection);
/*  658 */       this.myTermSize = (Dimension)newSize.clone();
/*      */       
/*  660 */       Dimension pixelDimension = new Dimension(getPixelWidth(), getPixelHeight());
/*  661 */       setPreferredSize(pixelDimension);
/*  662 */       if (this.myTerminalPanelListener != null) {
/*  663 */         this.myTerminalPanelListener.onPanelResize(origin);
/*      */       }
/*  665 */       SwingUtilities.invokeLater(() -> updateScrolling(true));
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setTerminalPanelListener(TerminalPanelListener terminalPanelListener) {
/*  670 */     this.myTerminalPanelListener = terminalPanelListener;
/*      */   }
/*      */   
/*      */   private void establishFontMetrics() {
/*  674 */     BufferedImage img = createBufferedImage(1, 1);
/*  675 */     Graphics2D graphics = img.createGraphics();
/*  676 */     graphics.setFont(this.myNormalFont);
/*      */     
/*  678 */     float lineSpacing = this.mySettingsProvider.getLineSpacing();
/*  679 */     FontMetrics fo = graphics.getFontMetrics();
/*      */     
/*  681 */     this.myCharSize.width = fo.charWidth('W');
/*  682 */     int fontMetricsHeight = fo.getHeight();
/*  683 */     this.myCharSize.height = (int)Math.ceil((fontMetricsHeight * lineSpacing));
/*  684 */     this.mySpaceBetweenLines = Math.max(0, (this.myCharSize.height - fontMetricsHeight) / 2 * 2);
/*  685 */     this.myDescent = fo.getDescent();
/*  686 */     if (LOG.isDebugEnabled()) {
/*      */ 
/*      */       
/*  689 */       int oldCharHeight = fontMetricsHeight + (int)(lineSpacing * 2.0F) + 2;
/*  690 */       int oldDescent = fo.getDescent() + (int)lineSpacing;
/*  691 */       LOG.debug("charHeight=" + oldCharHeight + "->" + this.myCharSize.height + ", descent=" + oldDescent + "->" + this.myDescent);
/*      */     } 
/*      */ 
/*      */     
/*  695 */     this.myMonospaced = isMonospaced(fo);
/*  696 */     if (!this.myMonospaced) {
/*  697 */       LOG.info("WARNING: Font " + this.myNormalFont.getName() + " is non-monospaced");
/*      */     }
/*      */     
/*  700 */     img.flush();
/*  701 */     graphics.dispose();
/*      */   }
/*      */   
/*      */   private static boolean isMonospaced(FontMetrics fontMetrics) {
/*  705 */     boolean isMonospaced = true;
/*  706 */     int charWidth = -1;
/*  707 */     for (int codePoint = 0; codePoint < 128; codePoint++) {
/*  708 */       if (Character.isValidCodePoint(codePoint)) {
/*  709 */         char character = (char)codePoint;
/*  710 */         if (isWordCharacter(character)) {
/*  711 */           int w = fontMetrics.charWidth(character);
/*  712 */           if (charWidth != -1) {
/*  713 */             if (w != charWidth) {
/*  714 */               isMonospaced = false;
/*      */               break;
/*      */             } 
/*      */           } else {
/*  718 */             charWidth = w;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*  723 */     return isMonospaced;
/*      */   }
/*      */   
/*      */   private static boolean isWordCharacter(char character) {
/*  727 */     return Character.isLetterOrDigit(character);
/*      */   }
/*      */   
/*      */   protected void setupAntialiasing(Graphics graphics) {
/*  731 */     if (graphics instanceof Graphics2D) {
/*  732 */       Graphics2D myGfx = (Graphics2D)graphics;
/*  733 */       Object mode = this.mySettingsProvider.useAntialiasing() ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
/*      */       
/*  735 */       RenderingHints hints = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, mode);
/*      */       
/*  737 */       myGfx.setRenderingHints(hints);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public Color getBackground() {
/*  743 */     return getPalette().getBackground(this.myStyleState.getBackground());
/*      */   }
/*      */ 
/*      */   
/*      */   public Color getForeground() {
/*  748 */     return getPalette().getForeground(this.myStyleState.getForeground());
/*      */   }
/*      */ 
/*      */   
/*      */   public void paintComponent(Graphics g) {
/*  753 */     final Graphics2D gfx = (Graphics2D)g;
/*      */     
/*  755 */     setupAntialiasing(gfx);
/*      */     
/*  757 */     gfx.setColor(getBackground());
/*      */     
/*  759 */     gfx.fillRect(0, 0, getWidth(), getHeight());
/*      */     
/*      */     try {
/*  762 */       this.myTerminalTextBuffer.lock();
/*      */       
/*  764 */       updateScrolling(false);
/*  765 */       this.myTerminalTextBuffer.processHistoryAndScreenLines(this.myClientScrollOrigin, this.myTermSize.height, new StyledTextConsumer() {
/*  766 */             final int columnCount = TerminalPanel.this.getColumnCount();
/*      */ 
/*      */             
/*      */             public void consume(int x, int y, @NotNull TextStyle style, @NotNull CharBuffer characters, int startRow) {
/*  770 */               if (style == null) $$$reportNull$$$0(0);  if (characters == null) $$$reportNull$$$0(1);  int row = y - startRow;
/*  771 */               TerminalPanel.this.drawCharacters(x, row, style, characters, gfx, false);
/*      */               
/*  773 */               if (TerminalPanel.this.myFindResult != null) {
/*  774 */                 List<Pair<Integer, Integer>> ranges = TerminalPanel.this.myFindResult.getRanges(characters);
/*  775 */                 if (ranges != null) {
/*  776 */                   for (Pair<Integer, Integer> range : ranges) {
/*  777 */                     TextStyle foundPatternStyle = TerminalPanel.this.getFoundPattern(style);
/*  778 */                     CharBuffer foundPatternChars = characters.subBuffer(range);
/*      */                     
/*  780 */                     TerminalPanel.this.drawCharacters(x + ((Integer)range.first).intValue(), row, foundPatternStyle, foundPatternChars, gfx);
/*      */                   } 
/*      */                 }
/*      */               } 
/*      */               
/*  785 */               if (TerminalPanel.this.mySelection != null) {
/*  786 */                 Pair<Integer, Integer> interval = TerminalPanel.this.mySelection.intersect(x, row + TerminalPanel.this.myClientScrollOrigin, characters.length());
/*  787 */                 if (interval != null) {
/*  788 */                   TextStyle selectionStyle = TerminalPanel.this.getSelectionStyle(style);
/*  789 */                   CharBuffer selectionChars = characters.subBuffer(((Integer)interval.first).intValue() - x, ((Integer)interval.second).intValue());
/*      */                   
/*  791 */                   TerminalPanel.this.drawCharacters(((Integer)interval.first).intValue(), row, selectionStyle, selectionChars, gfx);
/*      */                 } 
/*      */               } 
/*      */             }
/*      */ 
/*      */             
/*      */             public void consumeNul(int x, int y, int nulIndex, TextStyle style, CharBuffer characters, int startRow) {
/*  798 */               int row = y - startRow;
/*  799 */               if (TerminalPanel.this.mySelection != null) {
/*      */                 
/*  801 */                 Pair<Integer, Integer> interval = TerminalPanel.this.mySelection.intersect(nulIndex, row + TerminalPanel.this.myClientScrollOrigin, this.columnCount - nulIndex);
/*  802 */                 if (interval != null) {
/*  803 */                   TextStyle selectionStyle = TerminalPanel.this.getSelectionStyle(style);
/*  804 */                   TerminalPanel.this.drawCharacters(x, row, selectionStyle, characters, gfx);
/*      */                   return;
/*      */                 } 
/*      */               } 
/*  808 */               TerminalPanel.this.drawCharacters(x, row, style, characters, gfx);
/*      */             }
/*      */ 
/*      */             
/*      */             public void consumeQueue(int x, int y, int nulIndex, int startRow) {
/*  813 */               if (x < this.columnCount) {
/*  814 */                 consumeNul(x, y, nulIndex, TextStyle.EMPTY, new CharBuffer(' ', this.columnCount - x), startRow);
/*      */               }
/*      */             }
/*      */           });
/*      */       
/*  819 */       int cursorY = this.myCursor.getCoordY();
/*  820 */       if (this.myClientScrollOrigin + getRowCount() > cursorY && !hasUncommittedChars()) {
/*  821 */         int cursorX = this.myCursor.getCoordX();
/*  822 */         Pair<Character, TextStyle> sc = this.myTerminalTextBuffer.getStyledCharAt(cursorX, cursorY);
/*  823 */         String cursorChar = "" + sc.first;
/*  824 */         if (Character.isHighSurrogate(((Character)sc.first).charValue())) {
/*  825 */           cursorChar = cursorChar + (this.myTerminalTextBuffer.getStyledCharAt(cursorX + 1, cursorY)).first;
/*      */         }
/*  827 */         TextStyle normalStyle = (sc.second != null) ? (TextStyle)sc.second : this.myStyleState.getCurrent();
/*  828 */         TextStyle selectionStyle = getSelectionStyle(normalStyle);
/*  829 */         boolean inSelection = inSelection(cursorX, cursorY);
/*  830 */         this.myCursor.drawCursor(cursorChar, gfx, inSelection ? selectionStyle : normalStyle);
/*      */       } 
/*      */     } finally {
/*  833 */       this.myTerminalTextBuffer.unlock();
/*      */     } 
/*      */     
/*  836 */     drawInputMethodUncommitedChars(gfx);
/*      */     
/*  838 */     drawMargins(gfx, getWidth(), getHeight());
/*      */   }
/*      */   
/*      */   @NotNull
/*      */   private TextStyle getSelectionStyle(@NotNull TextStyle style) {
/*  843 */     if (style == null) $$$reportNull$$$0(15);  if (this.mySettingsProvider.useInverseSelectionColor()) {
/*  844 */       return getInversedStyle(style);
/*      */     }
/*  846 */     TextStyle.Builder builder = style.toBuilder();
/*  847 */     TextStyle mySelectionStyle = this.mySettingsProvider.getSelectionColor();
/*  848 */     builder.setBackground(mySelectionStyle.getBackground());
/*  849 */     builder.setForeground(mySelectionStyle.getForeground());
/*  850 */     if (builder instanceof HyperlinkStyle.Builder) {
/*  851 */       if (((HyperlinkStyle.Builder)builder).build(true) == null) $$$reportNull$$$0(16);  return (TextStyle)((HyperlinkStyle.Builder)builder).build(true);
/*      */     } 
/*  853 */     if (builder.build() == null) $$$reportNull$$$0(17);  return builder.build();
/*      */   }
/*      */   
/*      */   @NotNull
/*      */   private TextStyle getFoundPattern(@NotNull TextStyle style) {
/*  858 */     if (style == null) $$$reportNull$$$0(18);  TextStyle.Builder builder = style.toBuilder();
/*  859 */     TextStyle foundPattern = this.mySettingsProvider.getFoundPatternColor();
/*  860 */     builder.setBackground(foundPattern.getBackground());
/*  861 */     builder.setForeground(foundPattern.getForeground());
/*  862 */     if (builder.build() == null) $$$reportNull$$$0(19);  return builder.build();
/*      */   }
/*      */   
/*      */   private void drawInputMethodUncommitedChars(Graphics2D gfx) {
/*  866 */     if (hasUncommittedChars()) {
/*  867 */       int xCoord = computexCoord(this.myCursor.getCoordX() + 1, this.myCursor.getCoordY()) + getInsetX();
/*      */       
/*  869 */       int y = this.myCursor.getCoordY() + 1;
/*      */       
/*  871 */       int yCoord = y * this.myCharSize.height - 3;
/*      */       
/*  873 */       int len = computexCoordByCharBuffer(0, this.myInputMethodUncommittedChars.length(), new CharBuffer(this.myInputMethodUncommittedChars));
/*      */       
/*  875 */       gfx.setColor(getBackground());
/*  876 */       gfx.fillRect(xCoord, (y - 1) * this.myCharSize.height - 3, len, this.myCharSize.height);
/*      */       
/*  878 */       gfx.setColor(getForeground());
/*  879 */       gfx.setFont(this.myNormalFont);
/*      */       
/*  881 */       gfx.drawString(this.myInputMethodUncommittedChars, xCoord, yCoord);
/*  882 */       Stroke saved = gfx.getStroke();
/*  883 */       BasicStroke dotted = new BasicStroke(1.0F, 1, 1, 0.0F, new float[] { 0.0F, 2.0F, 0.0F, 2.0F }, 0.0F);
/*  884 */       gfx.setStroke(dotted);
/*      */       
/*  886 */       gfx.drawLine(xCoord, yCoord, xCoord + len, yCoord);
/*  887 */       gfx.setStroke(saved);
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean hasUncommittedChars() {
/*  892 */     return (this.myInputMethodUncommittedChars != null && this.myInputMethodUncommittedChars.length() > 0);
/*      */   }
/*      */   
/*      */   private boolean inSelection(int x, int y) {
/*  896 */     return (this.mySelection != null && this.mySelection.contains(new Point(x, y)));
/*      */   }
/*      */ 
/*      */   
/*      */   public void processKeyEvent(KeyEvent e) {
/*  901 */     handleKeyEvent(e);
/*  902 */     handleHyperlinks(e.getComponent(), e.isControlDown());
/*      */   }
/*      */   
/*      */   private int computexCoord(int coordX, int coordY) {
/*  906 */     int xCoord = 0;
/*  907 */     Graphics gfx = getGraphics();
/*  908 */     char[] chars = this.myTerminalTextBuffer.getLine(this.myClientScrollOrigin + coordY).getText().toCharArray();
/*  909 */     for (int i = 0; i < coordX; i++) {
/*  910 */       char c = (i < chars.length) ? chars[i] : ' ';
/*  911 */       Font font = getFontToDisplay(c, TextStyle.EMPTY);
/*  912 */       xCoord += gfx.getFontMetrics(font).charWidth(c);
/*      */     } 
/*  914 */     return xCoord;
/*      */   }
/*      */   
/*      */   private int computexCoordByCharBuffer(int start, int end, CharBuffer buf) {
/*  918 */     int _width = 0;
/*  919 */     int textLength = end - start;
/*  920 */     Graphics gfx = getGraphics();
/*  921 */     for (int i = 0; i < textLength; i++) {
/*  922 */       char c = buf.charAt(i);
/*  923 */       Font font = getFontToDisplay(c, TextStyle.EMPTY);
/*  924 */       _width += gfx.getFontMetrics(font).charWidth(c);
/*      */     } 
/*  926 */     return _width;
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleKeyEvent(@NotNull KeyEvent e) {
/*  931 */     if (e == null) $$$reportNull$$$0(20);  int id = e.getID();
/*  932 */     if (id == 401) {
/*  933 */       for (KeyListener keyListener : this.myCustomKeyListeners) {
/*  934 */         keyListener.keyPressed(e);
/*      */       }
/*  936 */     } else if (id == 400) {
/*  937 */       for (KeyListener keyListener : this.myCustomKeyListeners) {
/*  938 */         keyListener.keyTyped(e);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public int getPixelWidth() {
/*  944 */     return this.myCharSize.width * this.myTermSize.width + getInsetX();
/*      */   }
/*      */   
/*      */   public int getPixelHeight() {
/*  948 */     return this.myCharSize.height * this.myTermSize.height;
/*      */   }
/*      */   
/*      */   public int getColumnCount() {
/*  952 */     return this.myTermSize.width;
/*      */   }
/*      */   
/*      */   public int getRowCount() {
/*  956 */     return this.myTermSize.height;
/*      */   }
/*      */   
/*      */   public String getWindowTitle() {
/*  960 */     return this.myWindowTitle;
/*      */   }
/*      */   
/*      */   protected int getInsetX() {
/*  964 */     return 4;
/*      */   }
/*      */   
/*      */   public void addTerminalMouseListener(final TerminalMouseListener listener) {
/*  968 */     addMouseListener(new MouseAdapter()
/*      */         {
/*      */           public void mousePressed(MouseEvent e) {
/*  971 */             if (TerminalPanel.this.mySettingsProvider.enableMouseReporting() && TerminalPanel.this.isRemoteMouseAction(e)) {
/*  972 */               Point p = TerminalPanel.this.panelToCharCoords(e.getPoint());
/*  973 */               listener.mousePressed(p.x, p.y, e);
/*      */             } 
/*      */           }
/*      */ 
/*      */           
/*      */           public void mouseReleased(MouseEvent e) {
/*  979 */             if (TerminalPanel.this.mySettingsProvider.enableMouseReporting() && TerminalPanel.this.isRemoteMouseAction(e)) {
/*  980 */               Point p = TerminalPanel.this.panelToCharCoords(e.getPoint());
/*  981 */               listener.mouseReleased(p.x, p.y, e);
/*      */             } 
/*      */           }
/*      */         });
/*      */     
/*  986 */     addMouseWheelListener(new MouseWheelListener()
/*      */         {
/*      */           public void mouseWheelMoved(MouseWheelEvent e) {
/*  989 */             if (TerminalPanel.this.mySettingsProvider.enableMouseReporting() && TerminalPanel.this.isRemoteMouseAction(e)) {
/*  990 */               TerminalPanel.this.mySelection = null;
/*  991 */               Point p = TerminalPanel.this.panelToCharCoords(e.getPoint());
/*  992 */               listener.mouseWheelMoved(p.x, p.y, e);
/*      */             } 
/*      */           }
/*      */         });
/*      */     
/*  997 */     addMouseMotionListener(new MouseMotionAdapter()
/*      */         {
/*      */           public void mouseMoved(MouseEvent e) {
/* 1000 */             if (TerminalPanel.this.mySettingsProvider.enableMouseReporting() && TerminalPanel.this.isRemoteMouseAction(e)) {
/* 1001 */               Point p = TerminalPanel.this.panelToCharCoords(e.getPoint());
/* 1002 */               listener.mouseMoved(p.x, p.y, e);
/*      */             } 
/*      */           }
/*      */ 
/*      */           
/*      */           public void mouseDragged(MouseEvent e) {
/* 1008 */             if (TerminalPanel.this.mySettingsProvider.enableMouseReporting() && TerminalPanel.this.isRemoteMouseAction(e)) {
/* 1009 */               Point p = TerminalPanel.this.panelToCharCoords(e.getPoint());
/* 1010 */               listener.mouseDragged(p.x, p.y, e);
/*      */             } 
/*      */           }
/*      */         });
/*      */   }
/*      */   
/*      */   @NotNull
/*      */   KeyListener getTerminalKeyListener() {
/* 1018 */     if (this.myTerminalKeyHandler == null) $$$reportNull$$$0(21);  return this.myTerminalKeyHandler;
/*      */   }
/*      */   
/*      */   public enum TerminalCursorState {
/* 1022 */     SHOWING, HIDDEN, NO_FOCUS;
/*      */   }
/*      */ 
/*      */   
/*      */   public class TerminalCursor
/*      */   {
/*      */     private boolean myCursorIsShown;
/* 1029 */     protected Point myCursorCoordinates = new Point();
/* 1030 */     private CursorShape myShape = CursorShape.BLINK_BLOCK;
/*      */     
/*      */     private boolean myShouldDrawCursor = true;
/*      */     
/*      */     private boolean myBlinking = true;
/*      */     
/*      */     private long myLastCursorChange;
/*      */     private boolean myCursorHasChanged;
/*      */     
/*      */     public void setX(int x) {
/* 1040 */       this.myCursorCoordinates.x = x;
/* 1041 */       cursorChanged();
/*      */     }
/*      */     
/*      */     public void setY(int y) {
/* 1045 */       this.myCursorCoordinates.y = y;
/* 1046 */       cursorChanged();
/*      */     }
/*      */     
/*      */     public int getCoordX() {
/* 1050 */       return this.myCursorCoordinates.x;
/*      */     }
/*      */     
/*      */     public int getCoordY() {
/* 1054 */       return this.myCursorCoordinates.y - 1 - TerminalPanel.this.myClientScrollOrigin;
/*      */     }
/*      */     
/*      */     public void setShouldDrawCursor(boolean shouldDrawCursor) {
/* 1058 */       this.myShouldDrawCursor = shouldDrawCursor;
/*      */     }
/*      */     
/*      */     public void setBlinking(boolean blinking) {
/* 1062 */       this.myBlinking = blinking;
/*      */     }
/*      */     
/*      */     public boolean isBlinking() {
/* 1066 */       return (this.myBlinking && TerminalPanel.this.getBlinkingPeriod() > 0);
/*      */     }
/*      */     
/*      */     public void cursorChanged() {
/* 1070 */       this.myCursorHasChanged = true;
/* 1071 */       this.myLastCursorChange = System.currentTimeMillis();
/* 1072 */       TerminalPanel.this.repaint();
/*      */     }
/*      */     
/*      */     private boolean cursorShouldChangeBlinkState(long currentTime) {
/* 1076 */       return (currentTime - this.myLastCursorChange > TerminalPanel.this.getBlinkingPeriod());
/*      */     }
/*      */     
/*      */     public void changeStateIfNeeded() {
/* 1080 */       if (!TerminalPanel.this.isFocusOwner()) {
/*      */         return;
/*      */       }
/* 1083 */       long currentTime = System.currentTimeMillis();
/* 1084 */       if (cursorShouldChangeBlinkState(currentTime)) {
/* 1085 */         this.myCursorIsShown = !this.myCursorIsShown;
/* 1086 */         this.myLastCursorChange = currentTime;
/* 1087 */         this.myCursorHasChanged = false;
/* 1088 */         TerminalPanel.this.repaint();
/*      */       } 
/*      */     }
/*      */     
/*      */     private TerminalPanel.TerminalCursorState computeBlinkingState() {
/* 1093 */       if (!isBlinking() || this.myCursorHasChanged || this.myCursorIsShown) {
/* 1094 */         return TerminalPanel.TerminalCursorState.SHOWING;
/*      */       }
/* 1096 */       return TerminalPanel.TerminalCursorState.HIDDEN;
/*      */     }
/*      */     
/*      */     private TerminalPanel.TerminalCursorState computeCursorState() {
/* 1100 */       if (!this.myShouldDrawCursor) {
/* 1101 */         return TerminalPanel.TerminalCursorState.HIDDEN;
/*      */       }
/* 1103 */       if (!TerminalPanel.this.isFocusOwner()) {
/* 1104 */         return TerminalPanel.TerminalCursorState.NO_FOCUS;
/*      */       }
/* 1106 */       return computeBlinkingState();
/*      */     }
/*      */     
/*      */     void drawCursor(String c, Graphics2D gfx, TextStyle style) {
/* 1110 */       TerminalPanel.TerminalCursorState state = computeCursorState();
/*      */ 
/*      */       
/* 1113 */       if (state == TerminalPanel.TerminalCursorState.HIDDEN) {
/*      */         return;
/*      */       }
/*      */       
/* 1117 */       int x = getCoordX();
/* 1118 */       int y = getCoordY();
/*      */       
/* 1120 */       if (y < 0 || y >= TerminalPanel.this.myTermSize.height) {
/*      */         return;
/*      */       }
/*      */       
/* 1124 */       CharBuffer buf = new CharBuffer(c);
/* 1125 */       int xCoord = TerminalPanel.this.computexCoord(x, y) + TerminalPanel.this.getInsetX();
/* 1126 */       int yCoord = y * TerminalPanel.this.myCharSize.height;
/* 1127 */       int textLength = buf.length();
/* 1128 */       int height = Math.min(TerminalPanel.this.myCharSize.height, TerminalPanel.this.getHeight() - yCoord);
/* 1129 */       int width = Math.min(TerminalPanel.this.computexCoordByCharBuffer(0, textLength, buf), TerminalPanel.this.getWidth() - xCoord);
/* 1130 */       int lineStrokeSize = 2;
/*      */       
/* 1132 */       Color fgColor = TerminalPanel.this.getPalette().getForeground(TerminalPanel.this.myStyleState.getForeground(style.getForegroundForRun()));
/* 1133 */       TextStyle inversedStyle = TerminalPanel.this.getInversedStyle(style);
/* 1134 */       Color inverseBg = TerminalPanel.this.getPalette().getBackground(TerminalPanel.this.myStyleState.getBackground(inversedStyle.getBackgroundForRun()));
/*      */       
/* 1136 */       switch (this.myShape) {
/*      */         case BLINK_BLOCK:
/*      */         case STEADY_BLOCK:
/* 1139 */           if (state == TerminalPanel.TerminalCursorState.SHOWING) {
/* 1140 */             gfx.setColor(inverseBg);
/* 1141 */             gfx.fillRect(xCoord, yCoord, width, height);
/* 1142 */             TerminalPanel.this.drawCharacters(x, y, inversedStyle, buf, gfx); break;
/*      */           } 
/* 1144 */           gfx.setColor(fgColor);
/* 1145 */           gfx.drawRect(xCoord, yCoord, width, height);
/*      */           break;
/*      */ 
/*      */         
/*      */         case BLINK_UNDERLINE:
/*      */         case STEADY_UNDERLINE:
/* 1151 */           gfx.setColor(fgColor);
/* 1152 */           gfx.fillRect(xCoord, yCoord + height, width, lineStrokeSize);
/*      */           break;
/*      */         
/*      */         case BLINK_VERTICAL_BAR:
/*      */         case STEADY_VERTICAL_BAR:
/* 1157 */           gfx.setColor(fgColor);
/* 1158 */           gfx.fillRect(xCoord, yCoord, lineStrokeSize, height);
/*      */           break;
/*      */       } 
/*      */     }
/*      */     
/*      */     void setShape(CursorShape shape) {
/* 1164 */       this.myShape = shape;
/*      */     }
/*      */   }
/*      */   
/*      */   private int getBlinkingPeriod() {
/* 1169 */     if (this.myBlinkingPeriod != this.mySettingsProvider.caretBlinkingMs()) {
/* 1170 */       setBlinkingPeriod(this.mySettingsProvider.caretBlinkingMs());
/*      */     }
/* 1172 */     return this.myBlinkingPeriod;
/*      */   }
/*      */   
/*      */   protected void drawImage(Graphics2D g, BufferedImage image, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2) {
/* 1176 */     g.drawImage(image, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
/*      */   }
/*      */   
/*      */   @NotNull
/*      */   private TextStyle getInversedStyle(@NotNull TextStyle style) {
/* 1181 */     if (style == null) $$$reportNull$$$0(22);  TextStyle.Builder builder = new TextStyle.Builder(style);
/* 1182 */     builder.setOption(TextStyle.Option.INVERSE, !style.hasOption(TextStyle.Option.INVERSE));
/* 1183 */     if (style.getForeground() == null) {
/* 1184 */       builder.setForeground(this.myStyleState.getForeground());
/*      */     }
/* 1186 */     if (style.getBackground() == null) {
/* 1187 */       builder.setBackground(this.myStyleState.getBackground());
/*      */     }
/* 1189 */     if (builder.build() == null) $$$reportNull$$$0(23);  return builder.build();
/*      */   }
/*      */   
/*      */   private void drawCharacters(int x, int y, TextStyle style, CharBuffer buf, Graphics2D gfx) {
/* 1193 */     drawCharacters(x, y, style, buf, gfx, true);
/*      */   }
/*      */ 
/*      */   
/*      */   private void drawCharacters(int x, int y, TextStyle style, CharBuffer buf, Graphics2D gfx, boolean includeSpaceBetweenLines) {
/* 1198 */     int xCoord = computexCoord(x, y) + getInsetX();
/* 1199 */     int yCoord = y * this.myCharSize.height + (includeSpaceBetweenLines ? 0 : (this.mySpaceBetweenLines / 2));
/*      */     
/* 1201 */     if (xCoord < 0 || xCoord > getWidth() || yCoord < 0 || yCoord > getHeight()) {
/*      */       return;
/*      */     }
/*      */     
/* 1205 */     int textLength = buf.length();
/* 1206 */     int height = Math.min(this.myCharSize.height - (includeSpaceBetweenLines ? 0 : this.mySpaceBetweenLines), getHeight() - yCoord);
/* 1207 */     int width = Math.min(computexCoordByCharBuffer(0, textLength, buf), getWidth() - xCoord);
/*      */     
/* 1209 */     if (style instanceof HyperlinkStyle) {
/* 1210 */       HyperlinkStyle hyperlinkStyle = (HyperlinkStyle)style;
/*      */       
/* 1212 */       if (hyperlinkStyle.getHighlightMode() == HyperlinkStyle.HighlightMode.ALWAYS || (isHoveredHyperlink(hyperlinkStyle) && hyperlinkStyle.getHighlightMode() == HyperlinkStyle.HighlightMode.HOVER))
/*      */       {
/*      */         
/* 1215 */         style = hyperlinkStyle.getHighlightStyle();
/*      */       }
/*      */     } 
/*      */     
/* 1219 */     Color backgroundColor = getPalette().getBackground(this.myStyleState.getBackground(style.getBackgroundForRun()));
/* 1220 */     gfx.setColor(backgroundColor);
/* 1221 */     gfx.fillRect(xCoord, yCoord, width, height);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1226 */     if (buf.isNul()) {
/*      */       return;
/*      */     }
/*      */     
/* 1230 */     drawChars(x, y, buf, style, gfx);
/*      */     
/* 1232 */     gfx.setColor(getStyleForeground(style));
/*      */ 
/*      */     
/* 1235 */     if (style.hasOption(TextStyle.Option.UNDERLINED)) {
/* 1236 */       int baseLine = (y + 1) * this.myCharSize.height - this.mySpaceBetweenLines / 2 - this.myDescent;
/* 1237 */       int lineY = baseLine + 3;
/* 1238 */       gfx.drawLine(xCoord, lineY, computexCoord(x + textLength, lineY) + getInsetX(), lineY);
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean isHoveredHyperlink(@NotNull HyperlinkStyle link) {
/* 1243 */     if (link == null) $$$reportNull$$$0(24);  return (this.myHoveredHyperlink == link.getLinkInfo());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void drawChars(int x, int y, CharBuffer buf, TextStyle style, Graphics2D gfx) {
/*      */     CharBuffer renderingBuffer;
/* 1251 */     int blockLen = 1;
/* 1252 */     int offset = 0;
/* 1253 */     int drawCharsOffset = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1258 */     if (this.mySettingsProvider.DECCompatibilityMode() && style.hasOption(TextStyle.Option.BOLD)) {
/* 1259 */       renderingBuffer = CharUtils.heavyDecCompatibleBuffer(buf);
/*      */     } else {
/* 1261 */       renderingBuffer = buf;
/*      */     } 
/*      */     
/* 1264 */     while (offset + blockLen <= buf.length()) {
/*      */       
/* 1266 */       Font font = getFontToDisplay(buf.charAt(offset + blockLen - 1), style);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1274 */       gfx.setFont(font);
/*      */       
/* 1276 */       int descent = gfx.getFontMetrics(font).getDescent();
/* 1277 */       int baseLine = (y + 1) * this.myCharSize.height - this.mySpaceBetweenLines / 2 - descent;
/* 1278 */       int xCoord = computexCoord(x + drawCharsOffset, y) + getInsetX();
/*      */       
/* 1280 */       int yCoord = y * this.myCharSize.height + this.mySpaceBetweenLines / 2;
/*      */       
/* 1282 */       gfx.setClip(xCoord, yCoord, 
/*      */           
/* 1284 */           getWidth() - xCoord, 
/* 1285 */           getHeight() - yCoord);
/*      */       
/* 1287 */       gfx.setColor(getStyleForeground(style));
/*      */       
/* 1289 */       gfx.drawChars(renderingBuffer.getBuf(), buf.getStart() + offset, blockLen, xCoord, baseLine);
/*      */       
/* 1291 */       drawCharsOffset += blockLen;
/* 1292 */       offset += blockLen;
/* 1293 */       blockLen = 1;
/*      */     } 
/* 1295 */     gfx.setClip(null);
/*      */   }
/*      */   @NotNull
/*      */   private Color getStyleForeground(@NotNull TextStyle style) {
/* 1299 */     if (style == null) $$$reportNull$$$0(25);  Color foreground = getPalette().getForeground(this.myStyleState.getForeground(style.getForegroundForRun()));
/* 1300 */     if (style.hasOption(TextStyle.Option.DIM)) {
/* 1301 */       Color background = getPalette().getBackground(this.myStyleState.getBackground(style.getBackgroundForRun()));
/*      */ 
/*      */ 
/*      */       
/* 1305 */       foreground = new Color((foreground.getRed() + background.getRed()) / 2, (foreground.getGreen() + background.getGreen()) / 2, (foreground.getBlue() + background.getBlue()) / 2, foreground.getAlpha());
/*      */     } 
/* 1307 */     if (foreground == null) $$$reportNull$$$0(26);  return foreground;
/*      */   }
/*      */   
/*      */   protected Font getFontToDisplay(char c, TextStyle style) {
/* 1311 */     boolean bold = style.hasOption(TextStyle.Option.BOLD);
/* 1312 */     boolean italic = style.hasOption(TextStyle.Option.ITALIC);
/*      */     
/* 1314 */     if (bold && this.mySettingsProvider.DECCompatibilityMode() && CharacterSets.isDecBoxChar(c)) {
/* 1315 */       return this.myNormalFont;
/*      */     }
/* 1317 */     return bold ? (italic ? this.myBoldItalicFont : this.myBoldFont) : (italic ? this.myItalicFont : this.myNormalFont);
/*      */   }
/*      */ 
/*      */   
/*      */   private ColorPalette getPalette() {
/* 1322 */     return this.mySettingsProvider.getTerminalColorPalette();
/*      */   }
/*      */   
/*      */   private void drawMargins(Graphics2D gfx, int width, int height) {
/* 1326 */     gfx.setColor(getBackground());
/* 1327 */     gfx.fillRect(0, height, getWidth(), getHeight() - height);
/* 1328 */     gfx.fillRect(width, 0, getWidth() - width, getHeight());
/*      */   }
/*      */ 
/*      */   
/*      */   public void scrollArea(int scrollRegionTop, int scrollRegionSize, int dy) {
/* 1333 */     this.scrollDy.addAndGet(dy);
/* 1334 */     this.mySelection = null;
/*      */   }
/*      */   
/*      */   private void updateScrolling(boolean forceUpdate) {
/* 1338 */     int dy = this.scrollDy.getAndSet(0);
/* 1339 */     if (dy == 0 && !forceUpdate) {
/*      */       return;
/*      */     }
/* 1342 */     if (this.myScrollingEnabled) {
/* 1343 */       int value = this.myBoundedRangeModel.getValue();
/* 1344 */       int historyLineCount = this.myTerminalTextBuffer.getHistoryLinesCount();
/* 1345 */       if (value == 0) {
/* 1346 */         this.myBoundedRangeModel
/* 1347 */           .setRangeProperties(0, this.myTermSize.height, -historyLineCount, this.myTermSize.height, false);
/*      */       } else {
/*      */         
/* 1350 */         this.myBoundedRangeModel.setRangeProperties(
/* 1351 */             Math.min(Math.max(value + dy, -historyLineCount), this.myTermSize.height), this.myTermSize.height, -historyLineCount, this.myTermSize.height, false);
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1357 */       this.myBoundedRangeModel.setRangeProperties(0, this.myTermSize.height, 0, this.myTermSize.height, false);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setCursor(int x, int y) {
/* 1362 */     this.myCursor.setX(x);
/* 1363 */     this.myCursor.setY(y);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCursorShape(CursorShape shape) {
/* 1368 */     this.myCursor.setShape(shape);
/* 1369 */     switch (shape) {
/*      */       case STEADY_BLOCK:
/*      */       case STEADY_UNDERLINE:
/*      */       case STEADY_VERTICAL_BAR:
/* 1373 */         this.myCursor.myBlinking = false;
/*      */         break;
/*      */       case BLINK_BLOCK:
/*      */       case BLINK_UNDERLINE:
/*      */       case BLINK_VERTICAL_BAR:
/* 1378 */         this.myCursor.myBlinking = true;
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void beep() {
/* 1384 */     if (this.mySettingsProvider.audibleBell())
/* 1385 */       Toolkit.getDefaultToolkit().beep(); 
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public Rectangle getBounds(@NotNull TerminalLineIntervalHighlighting highlighting) {
/* 1390 */     if (highlighting == null) $$$reportNull$$$0(27);  TerminalLine line = highlighting.getLine();
/* 1391 */     int index = this.myTerminalTextBuffer.findScreenLineIndex(line);
/* 1392 */     if (index >= 0 && !highlighting.isDisposed()) {
/* 1393 */       return getBounds(new LineCellInterval(index, highlighting.getStartOffset(), highlighting.getEndOffset() + 1));
/*      */     }
/* 1395 */     return null;
/*      */   }
/*      */   @NotNull
/*      */   private Rectangle getBounds(@NotNull LineCellInterval cellInterval) {
/* 1399 */     if (cellInterval == null) $$$reportNull$$$0(28); 
/* 1400 */     Point topLeft = new Point(cellInterval.getStartColumn() * this.myCharSize.width + getInsetX(), cellInterval.getLine() * this.myCharSize.height);
/* 1401 */     return new Rectangle(topLeft, new Dimension(this.myCharSize.width * cellInterval.getCellCount(), this.myCharSize.height));
/*      */   }
/*      */   
/*      */   public BoundedRangeModel getBoundedRangeModel() {
/* 1405 */     return this.myBoundedRangeModel;
/*      */   }
/*      */   
/*      */   public TerminalTextBuffer getTerminalTextBuffer() {
/* 1409 */     return this.myTerminalTextBuffer;
/*      */   }
/*      */   
/*      */   public TerminalSelection getSelection() {
/* 1413 */     return this.mySelection;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean ambiguousCharsAreDoubleWidth() {
/* 1418 */     return this.mySettingsProvider.ambiguousCharsAreDoubleWidth();
/*      */   }
/*      */   
/*      */   public LinesBuffer getScrollBuffer() {
/* 1422 */     return this.myTerminalTextBuffer.getHistoryBuffer();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCursorVisible(boolean shouldDrawCursor) {
/* 1427 */     this.myCursor.setShouldDrawCursor(shouldDrawCursor);
/*      */   }
/*      */   @NotNull
/*      */   protected JPopupMenu createPopupMenu(@Nullable LinkInfo linkInfo, @NotNull final MouseEvent e) {
/* 1431 */     if (e == null) $$$reportNull$$$0(29);  JPopupMenu popup = new JPopupMenu();
/* 1432 */     final LinkInfo.PopupMenuGroupProvider popupMenuGroupProvider = (linkInfo != null) ? linkInfo.getPopupMenuGroupProvider() : null;
/* 1433 */     if (popupMenuGroupProvider != null) {
/* 1434 */       TerminalAction.addToMenu(popup, new TerminalActionProvider()
/*      */           {
/*      */             public List<TerminalAction> getActions() {
/* 1437 */               return popupMenuGroupProvider.getPopupMenuGroup(e);
/*      */             }
/*      */ 
/*      */             
/*      */             public TerminalActionProvider getNextProvider() {
/* 1442 */               return TerminalPanel.this;
/*      */             }
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             public void setNextProvider(TerminalActionProvider provider) {}
/*      */           });
/*      */     } else {
/* 1451 */       TerminalAction.addToMenu(popup, this);
/*      */     } 
/*      */     
/* 1454 */     if (popup == null) $$$reportNull$$$0(30);  return popup;
/*      */   }
/*      */   
/*      */   public void setScrollingEnabled(boolean scrollingEnabled) {
/* 1458 */     this.myScrollingEnabled = scrollingEnabled;
/*      */     
/* 1460 */     SwingUtilities.invokeLater(() -> updateScrolling(true));
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBlinkingCursor(boolean enabled) {
/* 1465 */     this.myCursor.setBlinking(enabled);
/*      */   }
/*      */   
/*      */   public TerminalCursor getTerminalCursor() {
/* 1469 */     return this.myCursor;
/*      */   }
/*      */   
/*      */   public TerminalOutputStream getTerminalOutputStream() {
/* 1473 */     return (TerminalOutputStream)this.myTerminalStarter;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setWindowTitle(String name) {
/* 1478 */     this.myWindowTitle = name;
/* 1479 */     if (this.myTerminalPanelListener != null) {
/* 1480 */       this.myTerminalPanelListener.onTitleChanged(this.myWindowTitle);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCurrentPath(String path) {
/* 1486 */     this.myCurrentPath = path;
/*      */   }
/*      */ 
/*      */   
/*      */   public List<TerminalAction> getActions() {
/* 1491 */     return Lists.newArrayList((Object[])new TerminalAction[] { (new TerminalAction(this.mySettingsProvider
/* 1492 */             .getOpenUrlActionPresentation(), input -> openSelectionAsURL()))
/*      */           
/* 1494 */           .withEnabledSupplier(this::selectionTextIsUrl), (new TerminalAction(this.mySettingsProvider
/* 1495 */             .getCopyActionPresentation(), this::handleCopy)
/*      */           {
/*      */             public boolean isEnabled(@Nullable KeyEvent e) {
/* 1498 */               return (e != null || TerminalPanel.this.mySelection != null);
/*      */             }
/* 1500 */           }).withMnemonicKey(Integer.valueOf(67)), (new TerminalAction(this.mySettingsProvider
/* 1501 */             .getPasteActionPresentation(), input -> {
/*      */               handlePaste();
/*      */               return true;
/* 1504 */             })).withMnemonicKey(Integer.valueOf(80)).withEnabledSupplier(() -> Boolean.valueOf((getClipboardString() != null))), new TerminalAction(this.mySettingsProvider
/* 1505 */             .getSelectAllActionPresentation(), input -> { selectAll(); return true; }), (new TerminalAction(this.mySettingsProvider
/*      */ 
/*      */ 
/*      */             
/* 1509 */             .getClearBufferActionPresentation(), input -> {
/*      */               clearBuffer();
/*      */               return true;
/* 1512 */             })).withMnemonicKey(Integer.valueOf(75)).withEnabledSupplier(() -> Boolean.valueOf(!this.myTerminalTextBuffer.isUsingAlternateBuffer())).separatorBefore(true), (new TerminalAction(this.mySettingsProvider
/* 1513 */             .getPageUpActionPresentation(), input -> {
/*      */               pageUp();
/*      */               return true;
/* 1516 */             })).withEnabledSupplier(() -> Boolean.valueOf(!this.myTerminalTextBuffer.isUsingAlternateBuffer())).separatorBefore(true), (new TerminalAction(this.mySettingsProvider
/* 1517 */             .getPageDownActionPresentation(), input -> {
/*      */               pageDown();
/*      */               return true;
/* 1520 */             })).withEnabledSupplier(() -> Boolean.valueOf(!this.myTerminalTextBuffer.isUsingAlternateBuffer())), (new TerminalAction(this.mySettingsProvider
/* 1521 */             .getLineUpActionPresentation(), input -> {
/*      */               scrollUp();
/*      */               return true;
/* 1524 */             })).withEnabledSupplier(() -> Boolean.valueOf(!this.myTerminalTextBuffer.isUsingAlternateBuffer())).separatorBefore(true), new TerminalAction(this.mySettingsProvider
/* 1525 */             .getLineDownActionPresentation(), input -> {
/*      */               scrollDown();
/*      */               return true;
/*      */             }) });
/*      */   }
/*      */   
/*      */   public void selectAll() {
/* 1532 */     this
/* 1533 */       .mySelection = new TerminalSelection(new Point(0, -this.myTerminalTextBuffer.getHistoryLinesCount()), new Point(this.myTermSize.width, this.myTerminalTextBuffer.getScreenLinesCount()));
/*      */   }
/*      */   
/*      */   @NotNull
/*      */   private Boolean selectionTextIsUrl() {
/* 1538 */     String selectionText = getSelectionText();
/* 1539 */     if (selectionText != null) {
/*      */       try {
/* 1541 */         URI uri = new URI(selectionText);
/*      */         
/* 1543 */         uri.toURL();
/* 1544 */         if (Boolean.valueOf(true) == null) $$$reportNull$$$0(31);  return Boolean.valueOf(true);
/* 1545 */       } catch (Exception exception) {}
/*      */     }
/*      */ 
/*      */     
/* 1549 */     if (Boolean.valueOf(false) == null) $$$reportNull$$$0(32);  return Boolean.valueOf(false);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private String getSelectionText() {
/* 1554 */     if (this.mySelection != null) {
/* 1555 */       Pair<Point, Point> points = this.mySelection.pointsForRun(this.myTermSize.width);
/*      */       
/* 1557 */       if (points.first != null || points.second != null) {
/* 1558 */         return 
/* 1559 */           SelectionUtil.getSelectionText((Point)points.first, (Point)points.second, this.myTerminalTextBuffer);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1564 */     return null;
/*      */   }
/*      */   
/*      */   protected boolean openSelectionAsURL() {
/* 1568 */     if (Desktop.isDesktopSupported()) {
/*      */       try {
/* 1570 */         String selectionText = getSelectionText();
/*      */         
/* 1572 */         if (selectionText != null) {
/* 1573 */           Desktop.getDesktop().browse(new URI(selectionText));
/*      */         }
/* 1575 */       } catch (Exception exception) {}
/*      */     }
/*      */ 
/*      */     
/* 1579 */     return false;
/*      */   }
/*      */   
/*      */   public void clearBuffer() {
/* 1583 */     clearBuffer(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void clearBuffer(boolean keepLastLine) {
/* 1591 */     if (!this.myTerminalTextBuffer.isUsingAlternateBuffer()) {
/* 1592 */       this.myTerminalTextBuffer.clearHistory();
/*      */       
/* 1594 */       if (this.myCoordsAccessor != null) {
/* 1595 */         if (keepLastLine) {
/* 1596 */           if (this.myCoordsAccessor.getY() > 0) {
/* 1597 */             TerminalLine lastLine = this.myTerminalTextBuffer.getLine(this.myCoordsAccessor.getY() - 1);
/* 1598 */             this.myTerminalTextBuffer.clearAll();
/* 1599 */             this.myCoordsAccessor.setY(0);
/* 1600 */             this.myCursor.setY(1);
/* 1601 */             this.myTerminalTextBuffer.addLine(lastLine);
/*      */           } 
/*      */         } else {
/*      */           
/* 1605 */           this.myTerminalTextBuffer.clearAll();
/* 1606 */           this.myCoordsAccessor.setX(0);
/* 1607 */           this.myCoordsAccessor.setY(1);
/* 1608 */           this.myCursor.setX(0);
/* 1609 */           this.myCursor.setY(1);
/*      */         } 
/*      */       }
/*      */       
/* 1613 */       this.myBoundedRangeModel.setValue(0);
/* 1614 */       updateScrolling(true);
/*      */       
/* 1616 */       this.myClientScrollOrigin = this.myBoundedRangeModel.getValue();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public TerminalActionProvider getNextProvider() {
/* 1622 */     return this.myNextActionProvider;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNextProvider(TerminalActionProvider provider) {
/* 1627 */     this.myNextActionProvider = provider;
/*      */   }
/*      */   
/*      */   private void processTerminalKeyPressed(KeyEvent e) {
/* 1631 */     if (hasUncommittedChars()) {
/*      */       return;
/*      */     }
/*      */     
/*      */     try {
/* 1636 */       int keycode = e.getKeyCode();
/* 1637 */       char keychar = e.getKeyChar();
/*      */ 
/*      */ 
/*      */       
/* 1641 */       if (keycode == 127 && keychar == '.') {
/* 1642 */         this.myTerminalStarter.sendBytes(new byte[] { 46 });
/* 1643 */         e.consume();
/*      */         
/*      */         return;
/*      */       } 
/* 1647 */       if (keychar == ' ' && (e.getModifiers() & 0x2) != 0) {
/* 1648 */         this.myTerminalStarter.sendBytes(new byte[] { 0 });
/* 1649 */         e.consume();
/*      */         
/*      */         return;
/*      */       } 
/* 1653 */       byte[] code = this.myTerminalStarter.getCode(keycode, e.getModifiers());
/* 1654 */       if (code != null) {
/* 1655 */         this.myTerminalStarter.sendBytes(code);
/* 1656 */         e.consume();
/* 1657 */         if (this.mySettingsProvider.scrollToBottomOnTyping() && isCodeThatScrolls(keycode)) {
/* 1658 */           scrollToBottom();
/*      */         }
/*      */       }
/* 1661 */       else if ((e.getModifiersEx() & 0x200) != 0 && Character.isDefined(keychar) && this.mySettingsProvider
/* 1662 */         .altSendsEscape()) {
/*      */ 
/*      */ 
/*      */         
/* 1666 */         this.myTerminalStarter.sendString(new String(new char[] { '\033', (char)e.getKeyCode() }));
/* 1667 */         e.consume();
/*      */       }
/* 1669 */       else if (Character.isISOControl(keychar)) {
/* 1670 */         processCharacter(e);
/*      */       } 
/* 1672 */     } catch (Exception ex) {
/* 1673 */       LOG.error("Error sending pressed key to emulator", ex);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void processCharacter(@NotNull KeyEvent e) {
/* 1678 */     if (e == null) $$$reportNull$$$0(33);  if ((e.getModifiersEx() & 0x200) != 0 && this.mySettingsProvider.altSendsEscape()) {
/*      */       return;
/*      */     }
/* 1681 */     char keyChar = e.getKeyChar();
/* 1682 */     int modifiers = e.getModifiers();
/*      */     
/* 1684 */     char[] obuffer = { keyChar };
/*      */     
/* 1686 */     if (keyChar == '`' && (modifiers & 0x4) != 0) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 1691 */     this.myTerminalStarter.sendString(new String(obuffer));
/* 1692 */     e.consume();
/*      */     
/* 1694 */     if (this.mySettingsProvider.scrollToBottomOnTyping()) {
/* 1695 */       scrollToBottom();
/*      */     }
/*      */   }
/*      */   
/*      */   private static boolean isCodeThatScrolls(int keycode) {
/* 1700 */     return (keycode == 38 || keycode == 40 || keycode == 37 || keycode == 39 || keycode == 8 || keycode == 155 || keycode == 127 || keycode == 10 || keycode == 36 || keycode == 35 || keycode == 33 || keycode == 34);
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
/*      */   private void processTerminalKeyTyped(KeyEvent e) {
/* 1715 */     if (hasUncommittedChars()) {
/*      */       return;
/*      */     }
/*      */     
/* 1719 */     char keychar = e.getKeyChar();
/* 1720 */     if (!Character.isISOControl(keychar)) {
/*      */       try {
/* 1722 */         processCharacter(e);
/* 1723 */       } catch (Exception ex) {
/* 1724 */         LOG.error("Error sending typed key to emulator", ex);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class TerminalKeyHandler
/*      */     extends KeyAdapter
/*      */   {
/*      */     public void keyPressed(KeyEvent e) {
/* 1735 */       if (!TerminalAction.processEvent(TerminalPanel.this, e)) {
/* 1736 */         TerminalPanel.this.processTerminalKeyPressed(e);
/*      */       }
/*      */     }
/*      */     
/*      */     public void keyTyped(KeyEvent e) {
/* 1741 */       TerminalPanel.this.processTerminalKeyTyped(e);
/*      */     }
/*      */   }
/*      */   
/*      */   private void handlePaste() {
/* 1746 */     pasteFromClipboard(false);
/*      */   }
/*      */   
/*      */   private void handlePasteSelection() {
/* 1750 */     pasteFromClipboard(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void handleCopy(boolean unselect, boolean useSystemSelectionClipboardIfAvailable) {
/* 1759 */     if (this.mySelection != null) {
/* 1760 */       Pair<Point, Point> points = this.mySelection.pointsForRun(this.myTermSize.width);
/* 1761 */       copySelection((Point)points.first, (Point)points.second, useSystemSelectionClipboardIfAvailable);
/* 1762 */       if (unselect) {
/* 1763 */         this.mySelection = null;
/* 1764 */         repaint();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean handleCopy(@Nullable KeyEvent e) {
/* 1770 */     boolean ctrlC = (e != null && e.getKeyCode() == 67 && e.getModifiersEx() == 128);
/* 1771 */     boolean sendCtrlC = (ctrlC && this.mySelection == null);
/* 1772 */     handleCopy(ctrlC, false);
/* 1773 */     return !sendCtrlC;
/*      */   }
/*      */   
/*      */   private void handleCopyOnSelect() {
/* 1777 */     handleCopy(false, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void processInputMethodEvent(InputMethodEvent e) {
/* 1786 */     int commitCount = e.getCommittedCharacterCount();
/*      */     
/* 1788 */     if (commitCount > 0) {
/* 1789 */       this.myInputMethodUncommittedChars = null;
/* 1790 */       AttributedCharacterIterator text = e.getText();
/* 1791 */       if (text != null) {
/* 1792 */         StringBuilder sb = new StringBuilder();
/*      */ 
/*      */         
/* 1795 */         for (char c = text.first(); commitCount > 0; c = text.next(), commitCount--) {
/* 1796 */           if (c >= ' ' && c != '') {
/* 1797 */             sb.append(c);
/*      */           }
/*      */         } 
/*      */         
/* 1801 */         if (sb.length() > 0) {
/* 1802 */           this.myTerminalStarter.sendString(sb.toString());
/*      */         }
/*      */       } 
/*      */     } else {
/* 1806 */       this.myInputMethodUncommittedChars = uncommittedChars(e.getText());
/*      */     } 
/*      */   }
/*      */   
/*      */   private static String uncommittedChars(@Nullable AttributedCharacterIterator text) {
/* 1811 */     if (text == null) {
/* 1812 */       return null;
/*      */     }
/*      */     
/* 1815 */     StringBuilder sb = new StringBuilder();
/*      */     
/* 1817 */     for (char c = text.first(); c != Character.MAX_VALUE; c = text.next()) {
/* 1818 */       if (c >= ' ' && c != '') {
/* 1819 */         sb.append(c);
/*      */       }
/*      */     } 
/*      */     
/* 1823 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public InputMethodRequests getInputMethodRequests() {
/* 1828 */     return new MyInputMethodRequests();
/*      */   }
/*      */   
/*      */   private class MyInputMethodRequests implements InputMethodRequests { private MyInputMethodRequests() {}
/*      */     
/*      */     public Rectangle getTextLocation(TextHitInfo offset) {
/* 1834 */       Rectangle r = new Rectangle(TerminalPanel.this.computexCoord(TerminalPanel.this.myCursor.getCoordX(), TerminalPanel.this.myCursor.getCoordY()) + TerminalPanel.this.getInsetX(), (TerminalPanel.this.myCursor.getCoordY() + 1) * TerminalPanel.this.myCharSize.height, 0, 0);
/*      */       
/* 1836 */       Point p = TerminalPanel.this.getLocationOnScreen();
/* 1837 */       r.translate(p.x, p.y);
/* 1838 */       return r;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public TextHitInfo getLocationOffset(int x, int y) {
/* 1844 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getInsertPositionOffset() {
/* 1849 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public AttributedCharacterIterator getCommittedText(int beginIndex, int endIndex, AttributedCharacterIterator.Attribute[] attributes) {
/* 1854 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getCommittedTextLength() {
/* 1859 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public AttributedCharacterIterator cancelLatestCommittedText(AttributedCharacterIterator.Attribute[] attributes) {
/* 1865 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public AttributedCharacterIterator getSelectedText(AttributedCharacterIterator.Attribute[] attributes) {
/* 1871 */       return null;
/*      */     } }
/*      */ 
/*      */ 
/*      */   
/*      */   public void dispose() {
/* 1877 */     this.myRepaintTimer.stop();
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\termina\\ui\TerminalPanel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */