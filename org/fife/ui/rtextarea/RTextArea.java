/*      */ package org.fife.ui.rtextarea;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.HeadlessException;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.print.PageFormat;
/*      */ import java.awt.print.Printable;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Reader;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.ResourceBundle;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.InputMap;
/*      */ import javax.swing.JMenuItem;
/*      */ import javax.swing.JPopupMenu;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.CaretEvent;
/*      */ import javax.swing.plaf.TextUI;
/*      */ import javax.swing.text.AbstractDocument;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import javax.swing.text.Caret;
/*      */ import javax.swing.text.Document;
/*      */ import javax.swing.text.Element;
/*      */ import javax.swing.text.Segment;
/*      */ import javax.swing.undo.CannotRedoException;
/*      */ import javax.swing.undo.CannotUndoException;
/*      */ import org.fife.print.RPrintUtilities;
/*      */ import org.fife.ui.rsyntaxtextarea.DocumentRange;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class RTextArea
/*      */   extends RTextAreaBase
/*      */   implements Printable
/*      */ {
/*      */   public static final int INSERT_MODE = 0;
/*      */   public static final int OVERWRITE_MODE = 1;
/*      */   public static final String MARK_ALL_COLOR_PROPERTY = "RTA.markAllColor";
/*      */   public static final String MARK_ALL_ON_OCCURRENCE_SEARCHES_PROPERTY = "RTA.markAllOnOccurrenceSearches";
/*      */   public static final String MARK_ALL_OCCURRENCES_CHANGED_PROPERTY = "RTA.markAllOccurrencesChanged";
/*      */   private static final int MIN_ACTION_CONSTANT = 0;
/*      */   public static final int COPY_ACTION = 0;
/*      */   public static final int CUT_ACTION = 1;
/*      */   public static final int DELETE_ACTION = 2;
/*      */   public static final int PASTE_ACTION = 3;
/*      */   public static final int REDO_ACTION = 4;
/*      */   public static final int SELECT_ALL_ACTION = 5;
/*      */   public static final int UNDO_ACTION = 6;
/*      */   private static final int MAX_ACTION_CONSTANT = 6;
/*  122 */   private static final Color DEFAULT_MARK_ALL_COLOR = new Color(16762880);
/*      */ 
/*      */   
/*      */   private int textMode;
/*      */ 
/*      */   
/*      */   private static boolean recordingMacro;
/*      */ 
/*      */   
/*      */   private static Macro currentMacro;
/*      */ 
/*      */   
/*      */   private JPopupMenu popupMenu;
/*      */ 
/*      */   
/*      */   private JMenuItem undoMenuItem;
/*      */ 
/*      */   
/*      */   private JMenuItem redoMenuItem;
/*      */ 
/*      */   
/*      */   private JMenuItem cutMenuItem;
/*      */ 
/*      */   
/*      */   private JMenuItem pasteMenuItem;
/*      */ 
/*      */   
/*      */   private JMenuItem deleteMenuItem;
/*      */ 
/*      */   
/*      */   private boolean popupMenuCreated;
/*      */ 
/*      */   
/*      */   private static String selectedOccurrenceText;
/*      */ 
/*      */   
/*      */   private ToolTipSupplier toolTipSupplier;
/*      */ 
/*      */   
/*      */   private static RecordableTextAction cutAction;
/*      */ 
/*      */   
/*      */   private static RecordableTextAction copyAction;
/*      */ 
/*      */   
/*      */   private static RecordableTextAction pasteAction;
/*      */   
/*      */   private static RecordableTextAction deleteAction;
/*      */   
/*      */   private static RecordableTextAction undoAction;
/*      */   
/*      */   private static RecordableTextAction redoAction;
/*      */   
/*      */   private static RecordableTextAction selectAllAction;
/*      */   
/*      */   private static IconGroup iconGroup;
/*      */   
/*      */   private transient RUndoManager undoManager;
/*      */   
/*      */   private transient LineHighlightManager lineHighlightManager;
/*      */   
/*      */   private SmartHighlightPainter markAllHighlightPainter;
/*      */   
/*      */   private boolean markAllOnOccurrenceSearches;
/*      */   
/*      */   private CaretStyle[] carets;
/*      */   
/*      */   private static final String MSG = "org.fife.ui.rtextarea.RTextArea";
/*      */   
/*      */   private static StringBuilder repTabsSB;
/*      */ 
/*      */   
/*      */   public RTextArea() {}
/*      */ 
/*      */   
/*      */   public RTextArea(AbstractDocument doc) {
/*  198 */     super(doc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public RTextArea(String text) {
/*  208 */     super(text);
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
/*      */   public RTextArea(int rows, int cols) {
/*  221 */     super(rows, cols);
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
/*      */   public RTextArea(String text, int rows, int cols) {
/*  235 */     super(text, rows, cols);
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
/*      */   public RTextArea(AbstractDocument doc, String text, int rows, int cols) {
/*  250 */     super(doc, text, rows, cols);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public RTextArea(int textMode) {
/*  261 */     setTextMode(textMode);
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
/*      */   static synchronized void addToCurrentMacro(String id, String actionCommand) {
/*  274 */     currentMacro.addMacroRecord(new Macro.MacroRecord(id, actionCommand));
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
/*      */   public Object addLineHighlight(int line, Color color) throws BadLocationException {
/*  290 */     if (this.lineHighlightManager == null) {
/*  291 */       this.lineHighlightManager = new LineHighlightManager(this);
/*      */     }
/*  293 */     return this.lineHighlightManager.addLineHighlight(line, color);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void beginAtomicEdit() {
/*  317 */     this.undoManager.beginInternalAtomicEdit();
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
/*      */   public static synchronized void beginRecordingMacro() {
/*  331 */     if (isRecordingMacro()) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  336 */     if (currentMacro != null) {
/*  337 */       currentMacro = null;
/*      */     }
/*  339 */     currentMacro = new Macro();
/*  340 */     recordingMacro = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canUndo() {
/*  351 */     return this.undoManager.canUndo();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canRedo() {
/*  362 */     return this.undoManager.canRedo();
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
/*      */   void clearMarkAllHighlights() {
/*  374 */     ((RTextAreaHighlighter)getHighlighter()).clearMarkAllHighlights();
/*      */     
/*  376 */     repaint();
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
/*      */ 
/*      */ 
/*      */   
/*      */   protected void configurePopupMenu(JPopupMenu popupMenu) {
/*  398 */     boolean canType = (isEditable() && isEnabled());
/*      */ 
/*      */ 
/*      */     
/*  402 */     if (this.undoMenuItem != null) {
/*  403 */       this.undoMenuItem.setEnabled((undoAction.isEnabled() && canType));
/*  404 */       this.redoMenuItem.setEnabled((redoAction.isEnabled() && canType));
/*  405 */       this.cutMenuItem.setEnabled((cutAction.isEnabled() && canType));
/*  406 */       this.pasteMenuItem.setEnabled((pasteAction.isEnabled() && canType));
/*  407 */       this.deleteMenuItem.setEnabled((deleteAction.isEnabled() && canType));
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
/*      */   protected Document createDefaultModel() {
/*  422 */     return new RDocument();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected RTextAreaBase.RTAMouseListener createMouseListener() {
/*  432 */     return new RTextAreaMutableCaretEvent(this);
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
/*      */   protected JPopupMenu createPopupMenu() {
/*  446 */     JPopupMenu menu = new JPopupMenu();
/*  447 */     menu.add(this.undoMenuItem = createPopupMenuItem(undoAction));
/*  448 */     menu.add(this.redoMenuItem = createPopupMenuItem(redoAction));
/*  449 */     menu.addSeparator();
/*  450 */     menu.add(this.cutMenuItem = createPopupMenuItem(cutAction));
/*  451 */     menu.add(createPopupMenuItem(copyAction));
/*  452 */     menu.add(this.pasteMenuItem = createPopupMenuItem(pasteAction));
/*  453 */     menu.add(this.deleteMenuItem = createPopupMenuItem(deleteAction));
/*  454 */     menu.addSeparator();
/*  455 */     menu.add(createPopupMenuItem(selectAllAction));
/*  456 */     return menu;
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
/*      */   private static void createPopupMenuActions() {
/*  470 */     int mod = getDefaultModifier();
/*  471 */     ResourceBundle msg = ResourceBundle.getBundle("org.fife.ui.rtextarea.RTextArea");
/*      */     
/*  473 */     cutAction = new RTextAreaEditorKit.CutAction();
/*  474 */     cutAction.setProperties(msg, "Action.Cut");
/*  475 */     cutAction.setAccelerator(KeyStroke.getKeyStroke(88, mod));
/*  476 */     copyAction = new RTextAreaEditorKit.CopyAction();
/*  477 */     copyAction.setProperties(msg, "Action.Copy");
/*  478 */     copyAction.setAccelerator(KeyStroke.getKeyStroke(67, mod));
/*  479 */     pasteAction = new RTextAreaEditorKit.PasteAction();
/*  480 */     pasteAction.setProperties(msg, "Action.Paste");
/*  481 */     pasteAction.setAccelerator(KeyStroke.getKeyStroke(86, mod));
/*  482 */     deleteAction = new RTextAreaEditorKit.DeleteNextCharAction();
/*  483 */     deleteAction.setProperties(msg, "Action.Delete");
/*  484 */     deleteAction.setAccelerator(KeyStroke.getKeyStroke(127, 0));
/*  485 */     undoAction = new RTextAreaEditorKit.UndoAction();
/*  486 */     undoAction.setProperties(msg, "Action.Undo");
/*  487 */     undoAction.setAccelerator(KeyStroke.getKeyStroke(90, mod));
/*  488 */     redoAction = new RTextAreaEditorKit.RedoAction();
/*  489 */     redoAction.setProperties(msg, "Action.Redo");
/*  490 */     redoAction.setAccelerator(KeyStroke.getKeyStroke(89, mod));
/*  491 */     selectAllAction = new RTextAreaEditorKit.SelectAllAction();
/*  492 */     selectAllAction.setProperties(msg, "Action.SelectAll");
/*  493 */     selectAllAction.setAccelerator(KeyStroke.getKeyStroke(65, mod));
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
/*      */   protected JMenuItem createPopupMenuItem(Action a) {
/*  506 */     JMenuItem item = new JMenuItem(a)
/*      */       {
/*      */         public void setToolTipText(String text) {}
/*      */       };
/*      */ 
/*      */ 
/*      */     
/*  513 */     item.setAccelerator(null);
/*  514 */     return item;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected RTextAreaUI createRTextAreaUI() {
/*  525 */     return new RTextAreaUI(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String createSpacer(int size) {
/*  536 */     StringBuilder sb = new StringBuilder();
/*  537 */     for (int i = 0; i < size; i++) {
/*  538 */       sb.append(' ');
/*      */     }
/*  540 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected RUndoManager createUndoManager() {
/*  550 */     return new RUndoManager(this);
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
/*      */   public void discardAllEdits() {
/*  565 */     this.undoManager.discardAllEdits();
/*  566 */     getDocument().removeUndoableEditListener(this.undoManager);
/*  567 */     this.undoManager = createUndoManager();
/*  568 */     getDocument().addUndoableEditListener(this.undoManager);
/*  569 */     this.undoManager.updateActions();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void endAtomicEdit() {
/*  579 */     this.undoManager.endInternalAtomicEdit();
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
/*      */   public static synchronized void endRecordingMacro() {
/*  594 */     if (!isRecordingMacro()) {
/*      */       return;
/*      */     }
/*      */     
/*  598 */     recordingMacro = false;
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
/*      */   protected void fireCaretUpdate(CaretEvent e) {
/*  611 */     possiblyUpdateCurrentLineHighlightLocation();
/*      */ 
/*      */ 
/*      */     
/*  615 */     if (e != null && e.getDot() != e.getMark()) {
/*  616 */       cutAction.setEnabled(true);
/*  617 */       copyAction.setEnabled(true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  624 */     else if (cutAction.isEnabled()) {
/*  625 */       cutAction.setEnabled(false);
/*  626 */       copyAction.setEnabled(false);
/*      */     } 
/*      */     
/*  629 */     super.fireCaretUpdate(e);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fixCtrlH() {
/*  639 */     InputMap inputMap = getInputMap();
/*  640 */     KeyStroke char010 = KeyStroke.getKeyStroke("typed \b");
/*  641 */     InputMap parent = inputMap;
/*  642 */     while (parent != null) {
/*  643 */       parent.remove(char010);
/*  644 */       parent = parent.getParent();
/*      */     } 
/*  646 */     if (inputMap != null) {
/*  647 */       KeyStroke backspace = KeyStroke.getKeyStroke("BACK_SPACE");
/*  648 */       inputMap.put(backspace, "delete-previous");
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static RecordableTextAction getAction(int action) {
/*  670 */     if (action < 0 || action > 6) {
/*  671 */       return null;
/*      */     }
/*  673 */     switch (action) {
/*      */       case 0:
/*  675 */         return copyAction;
/*      */       case 1:
/*  677 */         return cutAction;
/*      */       case 2:
/*  679 */         return deleteAction;
/*      */       case 3:
/*  681 */         return pasteAction;
/*      */       case 4:
/*  683 */         return redoAction;
/*      */       case 5:
/*  685 */         return selectAllAction;
/*      */       case 6:
/*  687 */         return undoAction;
/*      */     } 
/*  689 */     return null;
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
/*      */   public static synchronized Macro getCurrentMacro() {
/*  703 */     return currentMacro;
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
/*      */   public static Color getDefaultMarkAllHighlightColor() {
/*  715 */     return DEFAULT_MARK_ALL_COLOR;
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
/*      */   public static int getDefaultModifier() {
/*  728 */     int modifier = RTextAreaBase.isOSX() ? 4 : 2;
/*      */     
/*      */     try {
/*  731 */       modifier = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
/*  732 */     } catch (HeadlessException headlessException) {}
/*      */ 
/*      */ 
/*      */     
/*  736 */     return modifier;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static IconGroup getIconGroup() {
/*  747 */     return iconGroup;
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
/*      */   public boolean getMarkAllOnOccurrenceSearches() {
/*  760 */     return this.markAllOnOccurrenceSearches;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   LineHighlightManager getLineHighlightManager() {
/*  770 */     return this.lineHighlightManager;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Color getMarkAllHighlightColor() {
/*  781 */     return (Color)this.markAllHighlightPainter.getPaint();
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
/*      */   public int getMaxAscent() {
/*  796 */     return getFontMetrics(getFont()).getAscent();
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
/*      */   public JPopupMenu getPopupMenu() {
/*  809 */     if (!this.popupMenuCreated) {
/*  810 */       this.popupMenu = createPopupMenu();
/*  811 */       if (this.popupMenu != null) {
/*      */         
/*  813 */         ComponentOrientation orientation = ComponentOrientation.getOrientation(Locale.getDefault());
/*  814 */         this.popupMenu.applyComponentOrientation(orientation);
/*      */       } 
/*  816 */       this.popupMenuCreated = true;
/*      */     } 
/*  818 */     return this.popupMenu;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getSelectedOccurrenceText() {
/*  829 */     return selectedOccurrenceText;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getTextMode() {
/*  840 */     return this.textMode;
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
/*      */   public ToolTipSupplier getToolTipSupplier() {
/*  852 */     return this.toolTipSupplier;
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
/*      */   public String getToolTipText(MouseEvent e) {
/*  870 */     String tip = null;
/*  871 */     if (getToolTipSupplier() != null) {
/*  872 */       tip = getToolTipSupplier().getToolTipText(this, e);
/*      */     }
/*  874 */     return (tip != null) ? tip : getToolTipText();
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
/*      */   protected void handleReplaceSelection(String content) {
/*  887 */     super.replaceSelection(content);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void init() {
/*  894 */     super.init();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  902 */     if (cutAction == null) {
/*  903 */       createPopupMenuActions();
/*      */     }
/*      */ 
/*      */     
/*  907 */     this.undoManager = createUndoManager();
/*  908 */     getDocument().addUndoableEditListener(this.undoManager);
/*      */ 
/*      */     
/*  911 */     Color markAllHighlightColor = getDefaultMarkAllHighlightColor();
/*  912 */     this.markAllHighlightPainter = new SmartHighlightPainter(markAllHighlightColor);
/*      */     
/*  914 */     setMarkAllHighlightColor(markAllHighlightColor);
/*  915 */     this.carets = new CaretStyle[2];
/*  916 */     setCaretStyle(0, CaretStyle.THICK_VERTICAL_LINE_STYLE);
/*  917 */     setCaretStyle(1, CaretStyle.BLOCK_STYLE);
/*  918 */     setDragEnabled(true);
/*      */     
/*  920 */     setTextMode(0);
/*  921 */     setMarkAllOnOccurrenceSearches(true);
/*      */ 
/*      */     
/*  924 */     fixCtrlH();
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
/*      */   public static synchronized boolean isRecordingMacro() {
/*  937 */     return recordingMacro;
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
/*      */   public static synchronized void loadMacro(Macro macro) {
/*  949 */     currentMacro = macro;
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
/*      */ 
/*      */   
/*      */   void markAll(List<DocumentRange> ranges) {
/*  970 */     RTextAreaHighlighter h = (RTextAreaHighlighter)getHighlighter();
/*  971 */     if (h != null) {
/*      */ 
/*      */       
/*  974 */       if (ranges != null) {
/*  975 */         for (DocumentRange range : ranges) {
/*      */           try {
/*  977 */             h.addMarkAllHighlight(range
/*  978 */                 .getStartOffset(), range.getEndOffset(), this.markAllHighlightPainter);
/*      */           }
/*  980 */           catch (BadLocationException ble) {
/*  981 */             ble.printStackTrace();
/*      */           } 
/*      */         } 
/*      */       }
/*      */       
/*  986 */       repaint();
/*  987 */       firePropertyChange("RTA.markAllOccurrencesChanged", (Object)null, ranges);
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
/*      */   public void paste() {
/*  999 */     beginAtomicEdit();
/*      */     try {
/* 1001 */       super.paste();
/*      */     } finally {
/* 1003 */       endAtomicEdit();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized void playbackLastMacro() {
/* 1012 */     if (currentMacro != null) {
/* 1013 */       List<Macro.MacroRecord> macroRecords = currentMacro.getMacroRecords();
/* 1014 */       if (!macroRecords.isEmpty()) {
/* 1015 */         Action[] actions = getActions();
/* 1016 */         this.undoManager.beginInternalAtomicEdit();
/*      */         try {
/* 1018 */           for (Macro.MacroRecord record : macroRecords) {
/* 1019 */             for (Action action : actions) {
/* 1020 */               if (action instanceof RecordableTextAction && record.id
/* 1021 */                 .equals(((RecordableTextAction)action)
/* 1022 */                   .getMacroID())) {
/* 1023 */                 action.actionPerformed(new ActionEvent(this, 1001, record.actionCommand));
/*      */ 
/*      */ 
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } finally {
/* 1032 */           this.undoManager.endInternalAtomicEdit();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
/* 1049 */     return RPrintUtilities.printDocumentWordWrap(g, this, getFont(), pageIndex, pageFormat, getTabSize());
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
/*      */   public void read(Reader in, Object desc) throws IOException {
/* 1061 */     RTextAreaEditorKit kit = (RTextAreaEditorKit)getUI().getEditorKit(this);
/* 1062 */     setText((String)null);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1069 */     Document doc = getDocument();
/* 1070 */     setDocument(createDefaultModel());
/*      */     
/* 1072 */     if (desc != null) {
/* 1073 */       doc.putProperty("stream", desc);
/*      */     }
/*      */     
/*      */     try {
/* 1077 */       kit.read(in, doc, 0);
/* 1078 */     } catch (BadLocationException e) {
/* 1079 */       throw new IOException(e.getMessage());
/*      */     } 
/*      */     
/* 1082 */     setDocument(doc);
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
/*      */   private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
/* 1096 */     s.defaultReadObject();
/*      */ 
/*      */ 
/*      */     
/* 1100 */     this.undoManager = createUndoManager();
/* 1101 */     getDocument().addUndoableEditListener(this.undoManager);
/*      */     
/* 1103 */     this.lineHighlightManager = null;
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
/*      */   public void redoLastAction() {
/*      */     try {
/* 1116 */       if (this.undoManager.canRedo()) {
/* 1117 */         this.undoManager.redo();
/*      */       }
/* 1119 */     } catch (CannotRedoException cre) {
/* 1120 */       cre.printStackTrace();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeAllLineHighlights() {
/* 1131 */     if (this.lineHighlightManager != null) {
/* 1132 */       this.lineHighlightManager.removeAllLineHighlights();
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
/*      */   public void removeLineHighlight(Object tag) {
/* 1145 */     if (this.lineHighlightManager != null) {
/* 1146 */       this.lineHighlightManager.removeLineHighlight(tag);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void replaceRange(String str, int start, int end) {
/* 1172 */     if (end < start) {
/* 1173 */       throw new IllegalArgumentException("end before start");
/*      */     }
/* 1175 */     Document doc = getDocument();
/* 1176 */     if (doc != null) {
/*      */       
/*      */       try {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1185 */         this.undoManager.beginInternalAtomicEdit();
/* 1186 */         ((AbstractDocument)doc).replace(start, end - start, str, null);
/*      */       }
/* 1188 */       catch (BadLocationException e) {
/* 1189 */         throw new IllegalArgumentException(e.getMessage());
/*      */       } finally {
/* 1191 */         this.undoManager.endInternalAtomicEdit();
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
/*      */ 
/*      */   
/*      */   public void replaceSelection(String text) {
/* 1208 */     if (text == null) {
/* 1209 */       handleReplaceSelection(text);
/*      */       
/*      */       return;
/*      */     } 
/* 1213 */     if (getTabsEmulated()) {
/* 1214 */       int firstTab = text.indexOf('\t');
/* 1215 */       if (firstTab > -1) {
/* 1216 */         int docOffs = getSelectionStart();
/*      */         try {
/* 1218 */           text = replaceTabsWithSpaces(text, docOffs, firstTab);
/* 1219 */         } catch (BadLocationException ble) {
/* 1220 */           ble.printStackTrace();
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1226 */     if (this.textMode == 1 && !"\n".equals(text)) {
/*      */       
/* 1228 */       Caret caret = getCaret();
/* 1229 */       int caretPos = caret.getDot();
/* 1230 */       Document doc = getDocument();
/* 1231 */       Element map = doc.getDefaultRootElement();
/* 1232 */       int curLine = map.getElementIndex(caretPos);
/* 1233 */       int lastLine = map.getElementCount() - 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 1240 */         int curLineEnd = getLineEndOffset(curLine);
/* 1241 */         if (caretPos == caret.getMark() && caretPos != curLineEnd) {
/* 1242 */           if (curLine == lastLine) {
/* 1243 */             caretPos = Math.min(caretPos + text.length(), curLineEnd);
/*      */           } else {
/*      */             
/* 1246 */             caretPos = Math.min(caretPos + text.length(), curLineEnd - 1);
/*      */           } 
/* 1248 */           caret.moveDot(caretPos);
/*      */         }
/*      */       
/* 1251 */       } catch (BadLocationException ble) {
/* 1252 */         UIManager.getLookAndFeel().provideErrorFeedback(this);
/* 1253 */         ble.printStackTrace();
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1261 */     handleReplaceSelection(text);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1267 */   private static Segment repTabsSeg = new Segment();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String replaceTabsWithSpaces(String text, int docOffs, int firstTab) throws BadLocationException {
/* 1288 */     int tabSize = getTabSize();
/*      */ 
/*      */     
/* 1291 */     Document doc = getDocument();
/* 1292 */     Element root = doc.getDefaultRootElement();
/* 1293 */     int lineIndex = root.getElementIndex(docOffs);
/* 1294 */     Element line = root.getElement(lineIndex);
/* 1295 */     int lineStart = line.getStartOffset();
/* 1296 */     int charCount = docOffs - lineStart;
/*      */ 
/*      */     
/* 1299 */     if (charCount > 0) {
/* 1300 */       doc.getText(lineStart, charCount, repTabsSeg);
/* 1301 */       charCount = 0;
/* 1302 */       for (int i = 0; i < repTabsSeg.count; i++) {
/* 1303 */         char ch = repTabsSeg.array[repTabsSeg.offset + i];
/* 1304 */         if (ch == '\t') {
/* 1305 */           charCount = 0;
/*      */         } else {
/*      */           
/* 1308 */           charCount = (charCount + 1) % tabSize;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1314 */     if (text.length() == 1) {
/* 1315 */       return createSpacer(tabSize - charCount);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1320 */     if (repTabsSB == null) {
/* 1321 */       repTabsSB = new StringBuilder();
/*      */     }
/* 1323 */     repTabsSB.setLength(0);
/* 1324 */     char[] array = text.toCharArray();
/* 1325 */     int lastPos = 0;
/* 1326 */     int offsInLine = charCount;
/* 1327 */     for (int pos = firstTab; pos < array.length; pos++) {
/* 1328 */       int thisTabSize; char ch = array[pos];
/* 1329 */       switch (ch) {
/*      */         case '\t':
/* 1331 */           if (pos > lastPos) {
/* 1332 */             repTabsSB.append(array, lastPos, pos - lastPos);
/*      */           }
/* 1334 */           thisTabSize = tabSize - offsInLine % tabSize;
/* 1335 */           repTabsSB.append(createSpacer(thisTabSize));
/* 1336 */           lastPos = pos + 1;
/* 1337 */           offsInLine = 0;
/*      */           break;
/*      */         case '\n':
/* 1340 */           offsInLine = 0;
/*      */           break;
/*      */         default:
/* 1343 */           offsInLine++;
/*      */           break;
/*      */       } 
/*      */     } 
/* 1347 */     if (lastPos < array.length) {
/* 1348 */       repTabsSB.append(array, lastPos, array.length - lastPos);
/*      */     }
/*      */     
/* 1351 */     return repTabsSB.toString();
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
/*      */   public static void setActionProperties(int action, String name, char mnemonic, KeyStroke accelerator) {
/* 1367 */     setActionProperties(action, name, Integer.valueOf(mnemonic), accelerator);
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
/*      */   public static void setActionProperties(int action, String name, Integer mnemonic, KeyStroke accelerator) {
/*      */     Action tempAction;
/* 1384 */     switch (action) {
/*      */       case 1:
/* 1386 */         tempAction = cutAction;
/*      */         break;
/*      */       case 0:
/* 1389 */         tempAction = copyAction;
/*      */         break;
/*      */       case 3:
/* 1392 */         tempAction = pasteAction;
/*      */         break;
/*      */       case 2:
/* 1395 */         tempAction = deleteAction;
/*      */         break;
/*      */       case 5:
/* 1398 */         tempAction = selectAllAction;
/*      */         break;
/*      */ 
/*      */       
/*      */       default:
/*      */         return;
/*      */     } 
/*      */     
/* 1406 */     tempAction.putValue("Name", name);
/* 1407 */     tempAction.putValue("ShortDescription", name);
/* 1408 */     tempAction.putValue("AcceleratorKey", accelerator);
/* 1409 */     tempAction.putValue("MnemonicKey", mnemonic);
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
/*      */   public void setCaret(Caret caret) {
/* 1425 */     super.setCaret(caret);
/* 1426 */     if (this.carets != null && caret instanceof ConfigurableCaret)
/*      */     {
/* 1428 */       ((ConfigurableCaret)caret).setStyle(this.carets[getTextMode()]);
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
/*      */   public void setCaretStyle(int mode, CaretStyle style) {
/* 1441 */     if (style == null) {
/* 1442 */       style = CaretStyle.THICK_VERTICAL_LINE_STYLE;
/*      */     }
/* 1444 */     this.carets[mode] = style;
/* 1445 */     if (mode == getTextMode() && getCaret() instanceof ConfigurableCaret)
/*      */     {
/* 1447 */       ((ConfigurableCaret)getCaret()).setStyle(style);
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
/*      */   public void setDocument(Document document) {
/* 1461 */     if (!(document instanceof RDocument)) {
/* 1462 */       throw new IllegalArgumentException("RTextArea requires instances of RDocument for its document");
/*      */     }
/*      */     
/* 1465 */     if (this.undoManager != null) {
/* 1466 */       Document old = getDocument();
/* 1467 */       if (old != null) {
/* 1468 */         old.removeUndoableEditListener(this.undoManager);
/*      */       }
/*      */     } 
/* 1471 */     super.setDocument(document);
/* 1472 */     if (this.undoManager != null) {
/* 1473 */       document.addUndoableEditListener(this.undoManager);
/* 1474 */       discardAllEdits();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static synchronized void setIconGroup(IconGroup group) {
/* 1499 */     Icon icon = group.getIcon("cut");
/* 1500 */     cutAction.putValue("SmallIcon", icon);
/* 1501 */     icon = group.getIcon("copy");
/* 1502 */     copyAction.putValue("SmallIcon", icon);
/* 1503 */     icon = group.getIcon("paste");
/* 1504 */     pasteAction.putValue("SmallIcon", icon);
/* 1505 */     icon = group.getIcon("delete");
/* 1506 */     deleteAction.putValue("SmallIcon", icon);
/* 1507 */     icon = group.getIcon("undo");
/* 1508 */     undoAction.putValue("SmallIcon", icon);
/* 1509 */     icon = group.getIcon("redo");
/* 1510 */     redoAction.putValue("SmallIcon", icon);
/* 1511 */     icon = group.getIcon("selectall");
/* 1512 */     selectAllAction.putValue("SmallIcon", icon);
/* 1513 */     iconGroup = group;
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
/*      */   public void setMarkAllHighlightColor(Color color) {
/* 1525 */     Color old = (Color)this.markAllHighlightPainter.getPaint();
/* 1526 */     if (old != null && !old.equals(color)) {
/* 1527 */       this.markAllHighlightPainter.setPaint(color);
/* 1528 */       RTextAreaHighlighter h = (RTextAreaHighlighter)getHighlighter();
/* 1529 */       if (h.getMarkAllHighlightCount() > 0) {
/* 1530 */         repaint();
/*      */       }
/* 1532 */       firePropertyChange("RTA.markAllColor", old, color);
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
/*      */   public void setMarkAllOnOccurrenceSearches(boolean markAll) {
/* 1548 */     if (markAll != this.markAllOnOccurrenceSearches) {
/* 1549 */       this.markAllOnOccurrenceSearches = markAll;
/* 1550 */       firePropertyChange("RTA.markAllOnOccurrenceSearches", !markAll, markAll);
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
/*      */   
/*      */   public void setPopupMenu(JPopupMenu popupMenu) {
/* 1569 */     this.popupMenu = popupMenu;
/* 1570 */     this.popupMenuCreated = true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRoundedSelectionEdges(boolean rounded) {
/* 1576 */     if (getRoundedSelectionEdges() != rounded) {
/* 1577 */       this.markAllHighlightPainter.setRoundedEdges(rounded);
/* 1578 */       super.setRoundedSelectionEdges(rounded);
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
/*      */   
/*      */   public static void setSelectedOccurrenceText(String text) {
/* 1597 */     selectedOccurrenceText = text;
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
/*      */   public void setTextMode(int mode) {
/* 1612 */     if (mode != 0 && mode != 1) {
/* 1613 */       mode = 0;
/*      */     }
/*      */     
/* 1616 */     if (this.textMode != mode) {
/* 1617 */       Caret caret = getCaret();
/* 1618 */       if (caret instanceof ConfigurableCaret) {
/* 1619 */         ((ConfigurableCaret)caret).setStyle(this.carets[mode]);
/*      */       }
/* 1621 */       this.textMode = mode;
/*      */ 
/*      */       
/* 1624 */       caret.setVisible(false);
/* 1625 */       caret.setVisible(true);
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
/*      */   public void setToolTipSupplier(ToolTipSupplier supplier) {
/* 1639 */     this.toolTipSupplier = supplier;
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
/*      */   public final void setUI(TextUI ui) {
/* 1657 */     if (this.popupMenu != null) {
/* 1658 */       SwingUtilities.updateComponentTreeUI(this.popupMenu);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1663 */     RTextAreaUI rtaui = (RTextAreaUI)getUI();
/* 1664 */     if (rtaui != null) {
/* 1665 */       rtaui.installDefaults();
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
/*      */   public void undoLastAction() {
/*      */     try {
/* 1679 */       if (this.undoManager.canUndo()) {
/* 1680 */         this.undoManager.undo();
/*      */       }
/*      */     }
/* 1683 */     catch (CannotUndoException cre) {
/* 1684 */       cre.printStackTrace();
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
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1698 */     getDocument().removeUndoableEditListener(this.undoManager);
/* 1699 */     s.defaultWriteObject();
/* 1700 */     getDocument().addUndoableEditListener(this.undoManager);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected class RTextAreaMutableCaretEvent
/*      */     extends RTextAreaBase.RTAMouseListener
/*      */   {
/*      */     protected RTextAreaMutableCaretEvent(RTextArea textArea) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void focusGained(FocusEvent e) {
/* 1719 */       Caret c = RTextArea.this.getCaret();
/* 1720 */       boolean enabled = (c.getDot() != c.getMark());
/* 1721 */       RTextArea.cutAction.setEnabled(enabled);
/* 1722 */       RTextArea.copyAction.setEnabled(enabled);
/* 1723 */       RTextArea.this.undoManager.updateActions();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void focusLost(FocusEvent e) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void mouseDragged(MouseEvent e) {
/* 1735 */       if ((e.getModifiers() & 0x10) != 0) {
/* 1736 */         Caret caret = RTextArea.this.getCaret();
/* 1737 */         this.dot = caret.getDot();
/* 1738 */         this.mark = caret.getMark();
/* 1739 */         RTextArea.this.fireCaretUpdate(this);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void mousePressed(MouseEvent e) {
/* 1745 */       if (e.isPopupTrigger()) {
/* 1746 */         showPopup(e);
/*      */       }
/* 1748 */       else if ((e.getModifiers() & 0x10) != 0) {
/* 1749 */         Caret caret = RTextArea.this.getCaret();
/* 1750 */         this.dot = caret.getDot();
/* 1751 */         this.mark = caret.getMark();
/* 1752 */         RTextArea.this.fireCaretUpdate(this);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void mouseReleased(MouseEvent e) {
/* 1758 */       if (e.isPopupTrigger()) {
/* 1759 */         showPopup(e);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void showPopup(MouseEvent e) {
/* 1770 */       JPopupMenu popupMenu = RTextArea.this.getPopupMenu();
/* 1771 */       if (popupMenu != null) {
/* 1772 */         RTextArea.this.configurePopupMenu(popupMenu);
/* 1773 */         popupMenu.show(e.getComponent(), e.getX(), e.getY());
/* 1774 */         e.consume();
/*      */       } 
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\RTextArea.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */