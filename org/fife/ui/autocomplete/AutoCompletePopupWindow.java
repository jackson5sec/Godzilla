/*      */ package org.fife.ui.autocomplete;
/*      */ 
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Color;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.Window;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseListener;
/*      */ import java.util.List;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.ActionMap;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JList;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JScrollPane;
/*      */ import javax.swing.JWindow;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.ListCellRenderer;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.event.CaretEvent;
/*      */ import javax.swing.event.CaretListener;
/*      */ import javax.swing.event.ListSelectionEvent;
/*      */ import javax.swing.event.ListSelectionListener;
/*      */ import javax.swing.plaf.ListUI;
/*      */ import javax.swing.text.Caret;
/*      */ import javax.swing.text.JTextComponent;
/*      */ import org.fife.ui.rsyntaxtextarea.PopupWindowDecorator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class AutoCompletePopupWindow
/*      */   extends JWindow
/*      */   implements CaretListener, ListSelectionListener, MouseListener
/*      */ {
/*      */   private AutoCompletion ac;
/*      */   private PopupList list;
/*      */   private CompletionListModel model;
/*      */   private Completion lastSelection;
/*      */   private AutoCompleteDescWindow descWindow;
/*      */   private Dimension preferredDescWindowSize;
/*      */   private boolean aboveCaret;
/*      */   private Color descWindowColor;
/*      */   private int lastLine;
/*      */   private boolean keyBindingsInstalled;
/*      */   private KeyActionPair escapeKap;
/*      */   private KeyActionPair upKap;
/*      */   private KeyActionPair downKap;
/*      */   private KeyActionPair leftKap;
/*      */   private KeyActionPair rightKap;
/*      */   private KeyActionPair enterKap;
/*      */   private KeyActionPair tabKap;
/*      */   private KeyActionPair homeKap;
/*      */   private KeyActionPair endKap;
/*      */   private KeyActionPair pageUpKap;
/*      */   private KeyActionPair pageDownKap;
/*      */   private KeyActionPair ctrlCKap;
/*      */   private KeyActionPair oldEscape;
/*      */   private KeyActionPair oldUp;
/*      */   private KeyActionPair oldDown;
/*      */   private KeyActionPair oldLeft;
/*      */   private KeyActionPair oldRight;
/*      */   private KeyActionPair oldEnter;
/*      */   private KeyActionPair oldTab;
/*      */   private KeyActionPair oldHome;
/*      */   private KeyActionPair oldEnd;
/*      */   private KeyActionPair oldPageUp;
/*      */   private KeyActionPair oldPageDown;
/*      */   private KeyActionPair oldCtrlC;
/*      */   private static final int VERTICAL_SPACE = 1;
/*      */   private static final String SUBSTANCE_LIST_UI = "org.pushingpixels.substance.internal.ui.SubstanceListUI";
/*      */   
/*      */   AutoCompletePopupWindow(Window parent, AutoCompletion ac) {
/*  142 */     super(parent);
/*  143 */     ComponentOrientation o = ac.getTextComponentOrientation();
/*      */     
/*  145 */     this.ac = ac;
/*  146 */     this.model = new CompletionListModel();
/*  147 */     this.list = new PopupList(this.model);
/*      */     
/*  149 */     this.list.setCellRenderer(new DelegatingCellRenderer());
/*  150 */     this.list.addListSelectionListener(this);
/*  151 */     this.list.addMouseListener(this);
/*      */     
/*  153 */     JPanel contentPane = new JPanel(new BorderLayout());
/*  154 */     JScrollPane sp = new JScrollPane(this.list, 22, 32);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  162 */     JPanel corner = new SizeGrip();
/*      */     
/*  164 */     boolean isLeftToRight = o.isLeftToRight();
/*  165 */     String str = isLeftToRight ? "LOWER_RIGHT_CORNER" : "LOWER_LEFT_CORNER";
/*      */     
/*  167 */     sp.setCorner(str, corner);
/*      */     
/*  169 */     contentPane.add(sp);
/*  170 */     setContentPane(contentPane);
/*  171 */     applyComponentOrientation(o);
/*      */ 
/*      */     
/*  174 */     if (Util.getShouldAllowDecoratingMainAutoCompleteWindows()) {
/*  175 */       PopupWindowDecorator decorator = PopupWindowDecorator.get();
/*  176 */       if (decorator != null) {
/*  177 */         decorator.decorate(this);
/*      */       }
/*      */     } 
/*      */     
/*  181 */     pack();
/*      */     
/*  183 */     setFocusableWindowState(false);
/*      */     
/*  185 */     this.lastLine = -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void caretUpdate(CaretEvent e) {
/*  192 */     if (isVisible()) {
/*  193 */       int line = this.ac.getLineOfCaret();
/*  194 */       if (line != this.lastLine) {
/*  195 */         this.lastLine = -1;
/*  196 */         setVisible(false);
/*      */       } else {
/*      */         
/*  199 */         doAutocomplete();
/*      */       }
/*      */     
/*  202 */     } else if (AutoCompletion.getDebug()) {
/*  203 */       Thread.dumpStack();
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
/*      */   private AutoCompleteDescWindow createDescriptionWindow() {
/*  215 */     AutoCompleteDescWindow dw = new AutoCompleteDescWindow(this, this.ac);
/*  216 */     dw.applyComponentOrientation(this.ac.getTextComponentOrientation());
/*      */     
/*  218 */     Dimension size = this.preferredDescWindowSize;
/*  219 */     if (size == null) {
/*  220 */       size = getSize();
/*      */     }
/*  222 */     dw.setSize(size);
/*      */     
/*  224 */     if (this.descWindowColor != null) {
/*  225 */       dw.setBackground(this.descWindowColor);
/*      */     } else {
/*      */       
/*  228 */       this.descWindowColor = dw.getBackground();
/*      */     } 
/*      */     
/*  231 */     return dw;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void createKeyActionPairs() {
/*  242 */     EnterAction enterAction = new EnterAction();
/*  243 */     this.escapeKap = new KeyActionPair("Escape", new EscapeAction());
/*  244 */     this.upKap = new KeyActionPair("Up", new UpAction());
/*  245 */     this.downKap = new KeyActionPair("Down", new DownAction());
/*  246 */     this.leftKap = new KeyActionPair("Left", new LeftAction());
/*  247 */     this.rightKap = new KeyActionPair("Right", new RightAction());
/*  248 */     this.enterKap = new KeyActionPair("Enter", enterAction);
/*  249 */     this.tabKap = new KeyActionPair("Tab", enterAction);
/*  250 */     this.homeKap = new KeyActionPair("Home", new HomeAction());
/*  251 */     this.endKap = new KeyActionPair("End", new EndAction());
/*  252 */     this.pageUpKap = new KeyActionPair("PageUp", new PageUpAction());
/*  253 */     this.pageDownKap = new KeyActionPair("PageDown", new PageDownAction());
/*  254 */     this.ctrlCKap = new KeyActionPair("CtrlC", new CopyAction());
/*      */ 
/*      */     
/*  257 */     this.oldEscape = new KeyActionPair();
/*  258 */     this.oldUp = new KeyActionPair();
/*  259 */     this.oldDown = new KeyActionPair();
/*  260 */     this.oldLeft = new KeyActionPair();
/*  261 */     this.oldRight = new KeyActionPair();
/*  262 */     this.oldEnter = new KeyActionPair();
/*  263 */     this.oldTab = new KeyActionPair();
/*  264 */     this.oldHome = new KeyActionPair();
/*  265 */     this.oldEnd = new KeyActionPair();
/*  266 */     this.oldPageUp = new KeyActionPair();
/*  267 */     this.oldPageDown = new KeyActionPair();
/*  268 */     this.oldCtrlC = new KeyActionPair();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void doAutocomplete() {
/*  274 */     this.lastLine = this.ac.refreshPopupWindow();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static KeyStroke getCopyKeyStroke() {
/*  284 */     int key = 67;
/*  285 */     int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
/*  286 */     return KeyStroke.getKeyStroke(key, mask);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Color getDescriptionWindowColor() {
/*  296 */     if (this.descWindow != null) {
/*  297 */       return this.descWindow.getBackground();
/*      */     }
/*  299 */     return this.descWindowColor;
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
/*      */   public ListCellRenderer getListCellRenderer() {
/*  312 */     DelegatingCellRenderer dcr = (DelegatingCellRenderer)this.list.getCellRenderer();
/*  313 */     return dcr.getFallbackCellRenderer();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Completion getSelection() {
/*  323 */     return isShowing() ? this.list.getSelectedValue() : this.lastSelection;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void insertSelectedCompletion() {
/*  333 */     Completion comp = getSelection();
/*  334 */     this.ac.insertCompletion(comp);
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
/*  346 */     if (AutoCompletion.getDebug()) {
/*  347 */       System.out.println("PopupWindow: Installing keybindings");
/*      */     }
/*  349 */     if (this.keyBindingsInstalled) {
/*  350 */       System.err.println("Error: key bindings were already installed");
/*  351 */       Thread.dumpStack();
/*      */       
/*      */       return;
/*      */     } 
/*  355 */     if (this.escapeKap == null) {
/*  356 */       createKeyActionPairs();
/*      */     }
/*      */     
/*  359 */     JTextComponent comp = this.ac.getTextComponent();
/*  360 */     InputMap im = comp.getInputMap();
/*  361 */     ActionMap am = comp.getActionMap();
/*      */     
/*  363 */     replaceAction(im, am, 27, this.escapeKap, this.oldEscape);
/*  364 */     if (AutoCompletion.getDebug() && this.oldEscape.action == this.escapeKap.action) {
/*  365 */       Thread.dumpStack();
/*      */     }
/*  367 */     replaceAction(im, am, 38, this.upKap, this.oldUp);
/*  368 */     replaceAction(im, am, 37, this.leftKap, this.oldLeft);
/*  369 */     replaceAction(im, am, 40, this.downKap, this.oldDown);
/*  370 */     replaceAction(im, am, 39, this.rightKap, this.oldRight);
/*  371 */     replaceAction(im, am, 10, this.enterKap, this.oldEnter);
/*  372 */     replaceAction(im, am, 9, this.tabKap, this.oldTab);
/*  373 */     replaceAction(im, am, 36, this.homeKap, this.oldHome);
/*  374 */     replaceAction(im, am, 35, this.endKap, this.oldEnd);
/*  375 */     replaceAction(im, am, 33, this.pageUpKap, this.oldPageUp);
/*  376 */     replaceAction(im, am, 34, this.pageDownKap, this.oldPageDown);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  381 */     KeyStroke ks = getCopyKeyStroke();
/*  382 */     this.oldCtrlC.key = im.get(ks);
/*  383 */     im.put(ks, this.ctrlCKap.key);
/*  384 */     this.oldCtrlC.action = am.get(this.ctrlCKap.key);
/*  385 */     am.put(this.ctrlCKap.key, this.ctrlCKap.action);
/*      */     
/*  387 */     comp.addCaretListener(this);
/*      */     
/*  389 */     this.keyBindingsInstalled = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void mouseClicked(MouseEvent e) {
/*  396 */     if (e.getClickCount() == 2) {
/*  397 */       insertSelectedCompletion();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void mouseEntered(MouseEvent e) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void mouseExited(MouseEvent e) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void mousePressed(MouseEvent e) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void mouseReleased(MouseEvent e) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void positionDescWindow() {
/*      */     int x;
/*  429 */     boolean showDescWindow = (this.descWindow != null && this.ac.getShowDescWindow());
/*  430 */     if (!showDescWindow) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  437 */     Point p = getLocation();
/*  438 */     Rectangle screenBounds = Util.getScreenBoundsForPoint(p.x, p.y);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  444 */     if (this.ac.getTextComponentOrientation().isLeftToRight()) {
/*  445 */       x = getX() + getWidth() + 5;
/*  446 */       if (x + this.descWindow.getWidth() > screenBounds.x + screenBounds.width) {
/*  447 */         x = getX() - 5 - this.descWindow.getWidth();
/*      */       }
/*      */     } else {
/*      */       
/*  451 */       x = getX() - 5 - this.descWindow.getWidth();
/*  452 */       if (x < screenBounds.x) {
/*  453 */         x = getX() + getWidth() + 5;
/*      */       }
/*      */     } 
/*      */     
/*  457 */     int y = getY();
/*  458 */     if (this.aboveCaret) {
/*  459 */       y = y + getHeight() - this.descWindow.getHeight();
/*      */     }
/*      */     
/*  462 */     if (x != this.descWindow.getX() || y != this.descWindow.getY()) {
/*  463 */       this.descWindow.setLocation(x, y);
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
/*      */   
/*      */   private void putBackAction(InputMap im, ActionMap am, int key, KeyActionPair kap) {
/*  481 */     KeyStroke ks = KeyStroke.getKeyStroke(key, 0);
/*  482 */     am.put(im.get(ks), kap.action);
/*  483 */     im.put(ks, kap.key);
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
/*      */   private void replaceAction(InputMap im, ActionMap am, int key, KeyActionPair kap, KeyActionPair old) {
/*  500 */     KeyStroke ks = KeyStroke.getKeyStroke(key, 0);
/*  501 */     old.key = im.get(ks);
/*  502 */     im.put(ks, kap.key);
/*  503 */     old.action = am.get(kap.key);
/*  504 */     am.put(kap.key, kap.action);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void selectFirstItem() {
/*  514 */     if (this.model.getSize() > 0) {
/*  515 */       this.list.setSelectedIndex(0);
/*  516 */       this.list.ensureIndexIsVisible(0);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void selectLastItem() {
/*  527 */     int index = this.model.getSize() - 1;
/*  528 */     if (index > -1) {
/*  529 */       this.list.setSelectedIndex(index);
/*  530 */       this.list.ensureIndexIsVisible(index);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void selectNextItem() {
/*  541 */     int index = this.list.getSelectedIndex();
/*  542 */     if (index > -1) {
/*  543 */       index = (index + 1) % this.model.getSize();
/*  544 */       this.list.setSelectedIndex(index);
/*  545 */       this.list.ensureIndexIsVisible(index);
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
/*      */   private void selectPageDownItem() {
/*  557 */     int visibleRowCount = this.list.getVisibleRowCount();
/*  558 */     int i = Math.min(this.list.getModel().getSize() - 1, this.list
/*  559 */         .getSelectedIndex() + visibleRowCount);
/*  560 */     this.list.setSelectedIndex(i);
/*  561 */     this.list.ensureIndexIsVisible(i);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void selectPageUpItem() {
/*  572 */     int visibleRowCount = this.list.getVisibleRowCount();
/*  573 */     int i = Math.max(0, this.list.getSelectedIndex() - visibleRowCount);
/*  574 */     this.list.setSelectedIndex(i);
/*  575 */     this.list.ensureIndexIsVisible(i);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void selectPreviousItem() {
/*  585 */     int index = this.list.getSelectedIndex();
/*  586 */     switch (index) {
/*      */       case 0:
/*  588 */         index = this.list.getModel().getSize() - 1;
/*      */         break;
/*      */       case -1:
/*  591 */         index = this.list.getModel().getSize() - 1;
/*  592 */         if (index == -1) {
/*      */           return;
/*      */         }
/*      */         break;
/*      */       default:
/*  597 */         index--;
/*      */         break;
/*      */     } 
/*  600 */     this.list.setSelectedIndex(index);
/*  601 */     this.list.ensureIndexIsVisible(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCompletions(List<Completion> completions) {
/*  612 */     this.model.setContents(completions);
/*  613 */     selectFirstItem();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDescriptionWindowSize(Dimension size) {
/*  623 */     if (this.descWindow != null) {
/*  624 */       this.descWindow.setSize(size);
/*      */     } else {
/*      */       
/*  627 */       this.preferredDescWindowSize = size;
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
/*      */   public void setDescriptionWindowColor(Color color) {
/*  639 */     if (this.descWindow != null) {
/*  640 */       this.descWindow.setBackground(color);
/*      */     } else {
/*      */       
/*  643 */       this.descWindowColor = color;
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
/*      */   public void setListCellRenderer(ListCellRenderer<Object> renderer) {
/*  658 */     DelegatingCellRenderer dcr = (DelegatingCellRenderer)this.list.getCellRenderer();
/*  659 */     dcr.setFallbackCellRenderer(renderer);
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
/*      */   public void setLocationRelativeTo(Rectangle r) {
/*  677 */     Rectangle screenBounds = Util.getScreenBoundsForPoint(r.x, r.y);
/*      */ 
/*      */     
/*  680 */     boolean showDescWindow = (this.descWindow != null && this.ac.getShowDescWindow());
/*  681 */     int totalH = getHeight();
/*  682 */     if (showDescWindow) {
/*  683 */       totalH = Math.max(totalH, this.descWindow.getHeight());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  688 */     this.aboveCaret = false;
/*  689 */     int y = r.y + r.height + 1;
/*  690 */     if (y + totalH > screenBounds.height) {
/*  691 */       y = r.y - 1 - getHeight();
/*  692 */       this.aboveCaret = true;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  697 */     int x = r.x;
/*  698 */     if (!this.ac.getTextComponentOrientation().isLeftToRight()) {
/*  699 */       x -= getWidth();
/*      */     }
/*  701 */     if (x < screenBounds.x) {
/*  702 */       x = screenBounds.x;
/*      */     }
/*  704 */     else if (x + getWidth() > screenBounds.x + screenBounds.width) {
/*  705 */       x = screenBounds.x + screenBounds.width - getWidth();
/*      */     } 
/*      */     
/*  708 */     setLocation(x, y);
/*      */ 
/*      */     
/*  711 */     if (showDescWindow) {
/*  712 */       positionDescWindow();
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
/*      */   public void setVisible(boolean visible) {
/*  726 */     if (visible != isVisible()) {
/*      */       
/*  728 */       if (visible) {
/*  729 */         installKeyBindings();
/*  730 */         this.lastLine = this.ac.getLineOfCaret();
/*  731 */         selectFirstItem();
/*  732 */         if (this.descWindow == null && this.ac.getShowDescWindow()) {
/*  733 */           this.descWindow = createDescriptionWindow();
/*  734 */           positionDescWindow();
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  739 */         if (this.descWindow != null) {
/*  740 */           Completion c = this.list.getSelectedValue();
/*  741 */           if (c != null) {
/*  742 */             this.descWindow.setDescriptionFor(c);
/*      */           }
/*      */         } 
/*      */       } else {
/*      */         
/*  747 */         uninstallKeyBindings();
/*      */       } 
/*      */       
/*  750 */       super.setVisible(visible);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  762 */       if (!visible) {
/*  763 */         this.lastSelection = this.list.getSelectedValue();
/*  764 */         this.model.clear();
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  770 */       if (this.descWindow != null) {
/*  771 */         this.descWindow.setVisible((visible && this.ac.getShowDescWindow()));
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
/*      */   private void uninstallKeyBindings() {
/*  786 */     if (AutoCompletion.getDebug()) {
/*  787 */       System.out.println("PopupWindow: Removing keybindings");
/*      */     }
/*  789 */     if (!this.keyBindingsInstalled) {
/*      */       return;
/*      */     }
/*      */     
/*  793 */     JTextComponent comp = this.ac.getTextComponent();
/*  794 */     InputMap im = comp.getInputMap();
/*  795 */     ActionMap am = comp.getActionMap();
/*      */     
/*  797 */     putBackAction(im, am, 27, this.oldEscape);
/*  798 */     putBackAction(im, am, 38, this.oldUp);
/*  799 */     putBackAction(im, am, 40, this.oldDown);
/*  800 */     putBackAction(im, am, 37, this.oldLeft);
/*  801 */     putBackAction(im, am, 39, this.oldRight);
/*  802 */     putBackAction(im, am, 10, this.oldEnter);
/*  803 */     putBackAction(im, am, 9, this.oldTab);
/*  804 */     putBackAction(im, am, 36, this.oldHome);
/*  805 */     putBackAction(im, am, 35, this.oldEnd);
/*  806 */     putBackAction(im, am, 33, this.oldPageUp);
/*  807 */     putBackAction(im, am, 34, this.oldPageDown);
/*      */ 
/*      */     
/*  810 */     KeyStroke ks = getCopyKeyStroke();
/*  811 */     am.put(im.get(ks), this.oldCtrlC.action);
/*  812 */     im.put(ks, this.oldCtrlC.key);
/*      */     
/*  814 */     comp.removeCaretListener(this);
/*      */     
/*  816 */     this.keyBindingsInstalled = false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateUI() {
/*  826 */     SwingUtilities.updateComponentTreeUI(this);
/*  827 */     if (this.descWindow != null) {
/*  828 */       this.descWindow.updateUI();
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
/*      */   public void valueChanged(ListSelectionEvent e) {
/*  840 */     if (!e.getValueIsAdjusting()) {
/*  841 */       Completion value = this.list.getSelectedValue();
/*  842 */       if (value != null && this.descWindow != null) {
/*  843 */         this.descWindow.setDescriptionFor(value);
/*  844 */         positionDescWindow();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class CopyAction
/*      */     extends AbstractAction
/*      */   {
/*      */     public void actionPerformed(ActionEvent e) {
/*  857 */       boolean doNormalCopy = false;
/*  858 */       if (AutoCompletePopupWindow.this.descWindow != null && AutoCompletePopupWindow.this.descWindow.isVisible()) {
/*  859 */         doNormalCopy = !AutoCompletePopupWindow.this.descWindow.copy();
/*      */       }
/*  861 */       if (doNormalCopy) {
/*  862 */         AutoCompletePopupWindow.this.ac.getTextComponent().copy();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class DownAction
/*      */     extends AbstractAction
/*      */   {
/*      */     public void actionPerformed(ActionEvent e) {
/*  876 */       if (AutoCompletePopupWindow.this.isVisible()) {
/*  877 */         AutoCompletePopupWindow.this.selectNextItem();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class EndAction
/*      */     extends AbstractAction
/*      */   {
/*      */     public void actionPerformed(ActionEvent e) {
/*  891 */       if (AutoCompletePopupWindow.this.isVisible()) {
/*  892 */         AutoCompletePopupWindow.this.selectLastItem();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class EnterAction
/*      */     extends AbstractAction
/*      */   {
/*      */     public void actionPerformed(ActionEvent e) {
/*  906 */       if (AutoCompletePopupWindow.this.isVisible()) {
/*  907 */         AutoCompletePopupWindow.this.insertSelectedCompletion();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class EscapeAction
/*      */     extends AbstractAction
/*      */   {
/*      */     public void actionPerformed(ActionEvent e) {
/*  921 */       if (AutoCompletePopupWindow.this.isVisible()) {
/*  922 */         AutoCompletePopupWindow.this.setVisible(false);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class HomeAction
/*      */     extends AbstractAction
/*      */   {
/*      */     public void actionPerformed(ActionEvent e) {
/*  936 */       if (AutoCompletePopupWindow.this.isVisible()) {
/*  937 */         AutoCompletePopupWindow.this.selectFirstItem();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class KeyActionPair
/*      */   {
/*      */     private Object key;
/*      */ 
/*      */     
/*      */     private Action action;
/*      */ 
/*      */     
/*      */     KeyActionPair() {}
/*      */ 
/*      */     
/*      */     KeyActionPair(Object key, Action a) {
/*  956 */       this.key = key;
/*  957 */       this.action = a;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class LeftAction
/*      */     extends AbstractAction
/*      */   {
/*      */     public void actionPerformed(ActionEvent e) {
/*  970 */       if (AutoCompletePopupWindow.this.isVisible()) {
/*  971 */         JTextComponent comp = AutoCompletePopupWindow.this.ac.getTextComponent();
/*  972 */         Caret c = comp.getCaret();
/*  973 */         int dot = c.getDot();
/*  974 */         if (dot > 0) {
/*  975 */           c.setDot(--dot);
/*      */ 
/*      */           
/*  978 */           if (comp.isVisible() && 
/*  979 */             AutoCompletePopupWindow.this.lastLine != -1) {
/*  980 */             AutoCompletePopupWindow.this.doAutocomplete();
/*      */           }
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
/*      */   class PageDownAction
/*      */     extends AbstractAction
/*      */   {
/*      */     public void actionPerformed(ActionEvent e) {
/*  997 */       if (AutoCompletePopupWindow.this.isVisible()) {
/*  998 */         AutoCompletePopupWindow.this.selectPageDownItem();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class PageUpAction
/*      */     extends AbstractAction
/*      */   {
/*      */     public void actionPerformed(ActionEvent e) {
/* 1012 */       if (AutoCompletePopupWindow.this.isVisible()) {
/* 1013 */         AutoCompletePopupWindow.this.selectPageUpItem();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class PopupList
/*      */     extends JList<Completion>
/*      */   {
/*      */     PopupList(CompletionListModel model) {
/* 1026 */       super(model);
/*      */     }
/*      */ 
/*      */     
/*      */     public void setUI(ListUI ui) {
/* 1031 */       if (Util.getUseSubstanceRenderers() && "org.pushingpixels.substance.internal.ui.SubstanceListUI"
/* 1032 */         .equals(ui.getClass().getName())) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1039 */         CompletionProvider p = AutoCompletePopupWindow.this.ac.getCompletionProvider();
/* 1040 */         BasicCompletion bc = new BasicCompletion(p, "Hello world");
/* 1041 */         setPrototypeCellValue(bc);
/*      */       }
/*      */       else {
/*      */         
/* 1045 */         ui = new FastListUI();
/* 1046 */         setPrototypeCellValue((Completion)null);
/*      */       } 
/* 1048 */       super.setUI(ui);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class RightAction
/*      */     extends AbstractAction
/*      */   {
/*      */     public void actionPerformed(ActionEvent e) {
/* 1061 */       if (AutoCompletePopupWindow.this.isVisible()) {
/* 1062 */         JTextComponent comp = AutoCompletePopupWindow.this.ac.getTextComponent();
/* 1063 */         Caret c = comp.getCaret();
/* 1064 */         int dot = c.getDot();
/* 1065 */         if (dot < comp.getDocument().getLength()) {
/* 1066 */           c.setDot(++dot);
/*      */ 
/*      */           
/* 1069 */           if (comp.isVisible() && 
/* 1070 */             AutoCompletePopupWindow.this.lastLine != -1) {
/* 1071 */             AutoCompletePopupWindow.this.doAutocomplete();
/*      */           }
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
/*      */   class UpAction
/*      */     extends AbstractAction
/*      */   {
/*      */     public void actionPerformed(ActionEvent e) {
/* 1088 */       if (AutoCompletePopupWindow.this.isVisible())
/* 1089 */         AutoCompletePopupWindow.this.selectPreviousItem(); 
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\autocomplete\AutoCompletePopupWindow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */