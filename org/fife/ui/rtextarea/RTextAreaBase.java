/*      */ package org.fife.ui.rtextarea;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Font;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Image;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.ComponentEvent;
/*      */ import java.awt.event.FocusEvent;
/*      */ import java.awt.event.FocusListener;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.event.MouseListener;
/*      */ import java.awt.event.MouseMotionListener;
/*      */ import javax.swing.JTextArea;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.event.CaretEvent;
/*      */ import javax.swing.plaf.ColorUIResource;
/*      */ import javax.swing.plaf.TextUI;
/*      */ import javax.swing.text.AbstractDocument;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import javax.swing.text.Caret;
/*      */ import javax.swing.text.StyleContext;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class RTextAreaBase
/*      */   extends JTextArea
/*      */ {
/*      */   public static final String BACKGROUND_IMAGE_PROPERTY = "background.image";
/*      */   public static final String CURRENT_LINE_HIGHLIGHT_COLOR_PROPERTY = "RTA.currentLineHighlightColor";
/*      */   public static final String CURRENT_LINE_HIGHLIGHT_FADE_PROPERTY = "RTA.currentLineHighlightFade";
/*      */   public static final String HIGHLIGHT_CURRENT_LINE_PROPERTY = "RTA.currentLineHighlight";
/*      */   public static final String ROUNDED_SELECTION_PROPERTY = "RTA.roundedSelection";
/*      */   private boolean tabsEmulatedWithSpaces;
/*      */   private boolean highlightCurrentLine;
/*      */   private Color currentLineColor;
/*      */   private boolean marginLineEnabled;
/*      */   private Color marginLineColor;
/*      */   private int marginLineX;
/*      */   private int marginSizeInChars;
/*      */   private boolean fadeCurrentLineHighlight;
/*      */   private boolean roundedSelectionEdges;
/*      */   private int previousCaretY;
/*      */   int currentCaretY;
/*      */   private BackgroundPainterStrategy backgroundPainter;
/*      */   private RTAMouseListener mouseListener;
/*   71 */   private static final Color DEFAULT_CARET_COLOR = new ColorUIResource(255, 51, 51);
/*   72 */   private static final Color DEFAULT_CURRENT_LINE_HIGHLIGHT_COLOR = new Color(255, 255, 170);
/*   73 */   private static final Color DEFAULT_MARGIN_LINE_COLOR = new Color(255, 224, 224);
/*      */ 
/*      */   
/*      */   private static final int DEFAULT_TAB_SIZE = 4;
/*      */   
/*      */   private static final int DEFAULT_MARGIN_LINE_POSITION = 80;
/*      */ 
/*      */   
/*      */   public RTextAreaBase() {
/*   82 */     init();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public RTextAreaBase(AbstractDocument doc) {
/*   92 */     super(doc);
/*   93 */     init();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public RTextAreaBase(String text) {
/*  104 */     init();
/*  105 */     setText(text);
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
/*      */   public RTextAreaBase(int rows, int cols) {
/*  118 */     super(rows, cols);
/*  119 */     init();
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
/*      */   public RTextAreaBase(String text, int rows, int cols) {
/*  135 */     super(rows, cols);
/*  136 */     init();
/*  137 */     setText(text);
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
/*      */   public RTextAreaBase(AbstractDocument doc, String text, int rows, int cols) {
/*  154 */     super(doc, (String)null, rows, cols);
/*  155 */     init();
/*  156 */     setText(text);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void addCurrentLineHighlightListeners() {
/*  167 */     boolean add = true;
/*  168 */     MouseMotionListener[] mouseMotionListeners = getMouseMotionListeners();
/*  169 */     for (MouseMotionListener mouseMotionListener : mouseMotionListeners) {
/*  170 */       if (mouseMotionListener == this.mouseListener) {
/*  171 */         add = false;
/*      */         break;
/*      */       } 
/*      */     } 
/*  175 */     if (add)
/*      */     {
/*  177 */       addMouseMotionListener(this.mouseListener);
/*      */     }
/*  179 */     MouseListener[] mouseListeners = getMouseListeners();
/*  180 */     for (MouseListener listener : mouseListeners) {
/*  181 */       if (listener == this.mouseListener) {
/*  182 */         add = false;
/*      */         break;
/*      */       } 
/*      */     } 
/*  186 */     if (add)
/*      */     {
/*  188 */       addMouseListener(this.mouseListener);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void addNotify() {
/*  194 */     super.addNotify();
/*      */ 
/*      */ 
/*      */     
/*  198 */     if (getCaretPosition() != 0) {
/*  199 */       SwingUtilities.invokeLater(this::possiblyUpdateCurrentLineHighlightLocation);
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
/*      */   
/*      */   public void convertSpacesToTabs() {
/*  226 */     int caretPosition = getCaretPosition();
/*  227 */     int tabSize = getTabSize();
/*  228 */     StringBuilder stringBuilder = new StringBuilder();
/*  229 */     for (int i = 0; i < tabSize; i++) {
/*  230 */       stringBuilder.append(" ");
/*      */     }
/*  232 */     String text = getText();
/*  233 */     setText(text.replaceAll(stringBuilder.toString(), "\t"));
/*  234 */     int newDocumentLength = getDocument().getLength();
/*      */ 
/*      */     
/*  237 */     if (caretPosition < newDocumentLength) {
/*  238 */       setCaretPosition(caretPosition);
/*      */     } else {
/*      */       
/*  241 */       setCaretPosition(newDocumentLength - 1);
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
/*      */   public void convertTabsToSpaces() {
/*  261 */     int caretPosition = getCaretPosition();
/*  262 */     int tabSize = getTabSize();
/*  263 */     StringBuilder tabInSpaces = new StringBuilder();
/*  264 */     for (int i = 0; i < tabSize; i++) {
/*  265 */       tabInSpaces.append(' ');
/*      */     }
/*  267 */     String text = getText();
/*  268 */     setText(text.replaceAll("\t", tabInSpaces.toString()));
/*      */ 
/*      */     
/*  271 */     setCaretPosition(caretPosition);
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
/*      */   protected abstract RTAMouseListener createMouseListener();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected abstract RTextAreaUI createRTextAreaUI();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void forceCurrentLineHighlightRepaint() {
/*  305 */     if (isShowing()) {
/*      */       
/*  307 */       this.previousCaretY = -1;
/*      */ 
/*      */       
/*  310 */       fireCaretUpdate(this.mouseListener);
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
/*      */   public final Color getBackground() {
/*  325 */     Object bg = getBackgroundObject();
/*  326 */     return (bg instanceof Color) ? (Color)bg : null;
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
/*      */   public final Image getBackgroundImage() {
/*  339 */     Object bg = getBackgroundObject();
/*  340 */     return (bg instanceof Image) ? (Image)bg : null;
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
/*      */   public final Object getBackgroundObject() {
/*  354 */     if (this.backgroundPainter == null) {
/*  355 */       return null;
/*      */     }
/*  357 */     return (this.backgroundPainter instanceof ImageBackgroundPainterStrategy) ? ((ImageBackgroundPainterStrategy)this.backgroundPainter)
/*  358 */       .getMasterImage() : ((ColorBackgroundPainterStrategy)this.backgroundPainter)
/*  359 */       .getColor();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getCaretLineNumber() {
/*      */     try {
/*  370 */       return getLineOfOffset(getCaretPosition());
/*  371 */     } catch (BadLocationException ble) {
/*  372 */       return 0;
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
/*      */   public final int getCaretOffsetFromLineStart() {
/*      */     try {
/*  386 */       int pos = getCaretPosition();
/*  387 */       return pos - getLineStartOffset(getLineOfOffset(pos));
/*  388 */     } catch (BadLocationException ble) {
/*  389 */       return 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int getCurrentCaretY() {
/*  400 */     return this.currentCaretY;
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
/*      */   public Color getCurrentLineHighlightColor() {
/*  415 */     return this.currentLineColor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Color getDefaultCaretColor() {
/*  425 */     return DEFAULT_CARET_COLOR;
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
/*      */   public static Color getDefaultCurrentLineHighlightColor() {
/*  437 */     return DEFAULT_CURRENT_LINE_HIGHLIGHT_COLOR;
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
/*      */   public static Font getDefaultFont() {
/*  450 */     StyleContext sc = StyleContext.getDefaultStyleContext();
/*  451 */     Font font = null;
/*      */     
/*  453 */     if (isOSX()) {
/*      */ 
/*      */       
/*  456 */       font = sc.getFont("Menlo", 0, 12);
/*  457 */       if (!"Menlo".equals(font.getFamily())) {
/*  458 */         font = sc.getFont("Monaco", 0, 12);
/*  459 */         if (!"Monaco".equals(font.getFamily())) {
/*  460 */           font = sc.getFont("Monospaced", 0, 13);
/*      */         }
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  466 */       font = sc.getFont("Consolas", 0, 13);
/*  467 */       if (!"Consolas".equals(font.getFamily())) {
/*  468 */         font = sc.getFont("Monospaced", 0, 13);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  473 */     return font;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Color getDefaultForeground() {
/*  484 */     return Color.BLACK;
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
/*      */   public static Color getDefaultMarginLineColor() {
/*  496 */     return DEFAULT_MARGIN_LINE_COLOR;
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
/*      */   public static int getDefaultMarginLinePosition() {
/*  508 */     return 80;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getDefaultTabSize() {
/*  518 */     return 4;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getFadeCurrentLineHighlight() {
/*  529 */     return this.fadeCurrentLineHighlight;
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
/*      */   public boolean getHighlightCurrentLine() {
/*  542 */     return this.highlightCurrentLine;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getLineEndOffsetOfCurrentLine() {
/*      */     try {
/*  554 */       return getLineEndOffset(getCaretLineNumber());
/*  555 */     } catch (BadLocationException ble) {
/*  556 */       return 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getLineHeight() {
/*  567 */     return getRowHeight();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getLineStartOffsetOfCurrentLine() {
/*      */     try {
/*  579 */       return getLineStartOffset(getCaretLineNumber());
/*  580 */     } catch (BadLocationException ble) {
/*  581 */       return 0;
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
/*      */   public Color getMarginLineColor() {
/*  593 */     return this.marginLineColor;
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
/*      */   public int getMarginLinePixelLocation() {
/*  606 */     return this.marginLineX;
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
/*      */   public int getMarginLinePosition() {
/*  619 */     return this.marginSizeInChars;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getRoundedSelectionEdges() {
/*  630 */     return this.roundedSelectionEdges;
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
/*      */   public boolean getTabsEmulated() {
/*  643 */     return this.tabsEmulatedWithSpaces;
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
/*      */   protected void init() {
/*  656 */     setRTextAreaUI(createRTextAreaUI());
/*      */ 
/*      */     
/*  659 */     enableEvents(9L);
/*      */ 
/*      */     
/*  662 */     setHighlightCurrentLine(true);
/*  663 */     setCurrentLineHighlightColor(getDefaultCurrentLineHighlightColor());
/*  664 */     setMarginLineEnabled(false);
/*  665 */     setMarginLineColor(getDefaultMarginLineColor());
/*  666 */     setMarginLinePosition(getDefaultMarginLinePosition());
/*  667 */     setBackgroundObject(Color.WHITE);
/*  668 */     setWrapStyleWord(true);
/*  669 */     setTabSize(5);
/*  670 */     setForeground(Color.BLACK);
/*  671 */     setTabsEmulated(false);
/*      */ 
/*      */     
/*  674 */     this.previousCaretY = this.currentCaretY = (getInsets()).top;
/*      */ 
/*      */     
/*  677 */     this.mouseListener = createMouseListener();
/*      */ 
/*      */     
/*  680 */     addFocusListener(this.mouseListener);
/*  681 */     addCurrentLineHighlightListeners();
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
/*      */   public boolean isMarginLineEnabled() {
/*  693 */     return this.marginLineEnabled;
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
/*      */   public static boolean isOSX() {
/*  705 */     String osName = System.getProperty("os.name").toLowerCase();
/*  706 */     return osName.startsWith("mac os x");
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
/*      */   protected void paintComponent(Graphics g) {
/*  720 */     this.backgroundPainter.paint(g, getVisibleRect());
/*      */ 
/*      */     
/*  723 */     TextUI ui = getUI();
/*  724 */     if (ui != null) {
/*      */       
/*  726 */       Graphics scratchGraphics = g.create();
/*      */       try {
/*  728 */         ui.update(scratchGraphics, this);
/*      */       } finally {
/*  730 */         scratchGraphics.dispose();
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
/*      */   protected void possiblyUpdateCurrentLineHighlightLocation() {
/*  745 */     int width = getWidth();
/*  746 */     int lineHeight = getLineHeight();
/*  747 */     int dot = getCaretPosition();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  752 */     if (getLineWrap()) {
/*      */       try {
/*  754 */         Rectangle temp = modelToView(dot);
/*  755 */         if (temp != null) {
/*  756 */           this.currentCaretY = temp.y;
/*      */         }
/*  758 */       } catch (BadLocationException ble) {
/*  759 */         ble.printStackTrace();
/*      */       } 
/*      */     } else {
/*      */ 
/*      */       
/*      */       try {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  776 */         Rectangle temp = modelToView(dot);
/*  777 */         if (temp != null) {
/*  778 */           this.currentCaretY = temp.y;
/*      */         }
/*  780 */       } catch (BadLocationException ble) {
/*  781 */         ble.printStackTrace();
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  788 */     repaint(0, this.currentCaretY, width, lineHeight);
/*  789 */     if (this.previousCaretY != this.currentCaretY) {
/*  790 */       repaint(0, this.previousCaretY, width, lineHeight);
/*      */     }
/*      */     
/*  793 */     this.previousCaretY = this.currentCaretY;
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
/*      */   protected void processComponentEvent(ComponentEvent e) {
/*  813 */     if (e.getID() == 101 && 
/*  814 */       getLineWrap() && getHighlightCurrentLine()) {
/*  815 */       this.previousCaretY = -1;
/*  816 */       fireCaretUpdate(this.mouseListener);
/*      */     } 
/*      */     
/*  819 */     super.processComponentEvent(e);
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
/*      */   public void setBackground(Color bg) {
/*  842 */     Object oldBG = getBackgroundObject();
/*  843 */     if (oldBG instanceof Color) {
/*  844 */       ((ColorBackgroundPainterStrategy)this.backgroundPainter)
/*  845 */         .setColor(bg);
/*      */     } else {
/*      */       
/*  848 */       this.backgroundPainter = new ColorBackgroundPainterStrategy(bg);
/*      */     } 
/*  850 */     setOpaque((bg == null || bg.getAlpha() == 255));
/*  851 */     firePropertyChange("background", oldBG, bg);
/*  852 */     repaint();
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
/*      */   public void setBackgroundImage(Image image) {
/*  874 */     Object oldBG = getBackgroundObject();
/*  875 */     if (oldBG instanceof Image) {
/*  876 */       ((ImageBackgroundPainterStrategy)this.backgroundPainter)
/*  877 */         .setImage(image);
/*      */     } else {
/*      */       
/*  880 */       ImageBackgroundPainterStrategy strategy = new BufferedImageBackgroundPainterStrategy(this);
/*      */       
/*  882 */       strategy.setImage(image);
/*  883 */       this.backgroundPainter = strategy;
/*      */     } 
/*  885 */     setOpaque(false);
/*  886 */     firePropertyChange("background.image", oldBG, image);
/*  887 */     repaint();
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
/*      */   public void setBackgroundObject(Object newBackground) {
/*  899 */     if (newBackground instanceof Color) {
/*  900 */       setBackground((Color)newBackground);
/*      */     }
/*  902 */     else if (newBackground instanceof Image) {
/*  903 */       setBackgroundImage((Image)newBackground);
/*      */     } else {
/*      */       
/*  906 */       setBackground(Color.WHITE);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCurrentLineHighlightColor(Color color) {
/*  937 */     if (color == null) {
/*  938 */       throw new NullPointerException();
/*      */     }
/*  940 */     if (!color.equals(this.currentLineColor)) {
/*  941 */       Color old = this.currentLineColor;
/*  942 */       this.currentLineColor = color;
/*  943 */       firePropertyChange("RTA.currentLineHighlightColor", old, color);
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
/*      */   public void setFadeCurrentLineHighlight(boolean fade) {
/*  958 */     if (fade != this.fadeCurrentLineHighlight) {
/*  959 */       this.fadeCurrentLineHighlight = fade;
/*  960 */       if (getHighlightCurrentLine()) {
/*  961 */         forceCurrentLineHighlightRepaint();
/*      */       }
/*  963 */       firePropertyChange("RTA.currentLineHighlightFade", !fade, fade);
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
/*      */   public void setFont(Font font) {
/*  978 */     if (font != null && font.getSize() <= 0) {
/*  979 */       throw new IllegalArgumentException("Font size must be > 0");
/*      */     }
/*  981 */     super.setFont(font);
/*  982 */     if (font != null) {
/*  983 */       updateMarginLineX();
/*  984 */       if (this.highlightCurrentLine) {
/*  985 */         possiblyUpdateCurrentLineHighlightLocation();
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
/*      */   public void setHighlightCurrentLine(boolean highlight) {
/* 1001 */     if (highlight != this.highlightCurrentLine) {
/* 1002 */       this.highlightCurrentLine = highlight;
/* 1003 */       firePropertyChange("RTA.currentLineHighlight", !highlight, highlight);
/*      */       
/* 1005 */       repaint();
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
/*      */   public void setLineWrap(boolean wrap) {
/* 1018 */     super.setLineWrap(wrap);
/* 1019 */     forceCurrentLineHighlightRepaint();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setMargin(Insets insets) {
/* 1030 */     Insets old = getInsets();
/* 1031 */     int oldTop = (old != null) ? old.top : 0;
/* 1032 */     int newTop = (insets != null) ? insets.top : 0;
/* 1033 */     if (oldTop != newTop)
/*      */     {
/*      */       
/* 1036 */       this.previousCaretY = this.currentCaretY = newTop;
/*      */     }
/* 1038 */     super.setMargin(insets);
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
/*      */   public void setMarginLineColor(Color color) {
/* 1050 */     this.marginLineColor = color;
/* 1051 */     if (this.marginLineEnabled) {
/* 1052 */       Rectangle visibleRect = getVisibleRect();
/* 1053 */       repaint(this.marginLineX, visibleRect.y, 1, visibleRect.height);
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
/*      */   public void setMarginLineEnabled(boolean enabled) {
/* 1065 */     if (enabled != this.marginLineEnabled) {
/* 1066 */       this.marginLineEnabled = enabled;
/* 1067 */       if (this.marginLineEnabled) {
/* 1068 */         Rectangle visibleRect = getVisibleRect();
/* 1069 */         repaint(this.marginLineX, visibleRect.y, 1, visibleRect.height);
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
/*      */   public void setMarginLinePosition(int size) {
/* 1083 */     this.marginSizeInChars = size;
/* 1084 */     if (this.marginLineEnabled) {
/* 1085 */       Rectangle visibleRect = getVisibleRect();
/* 1086 */       repaint(this.marginLineX, visibleRect.y, 1, visibleRect.height);
/* 1087 */       updateMarginLineX();
/* 1088 */       repaint(this.marginLineX, visibleRect.y, 1, visibleRect.height);
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
/*      */   public void setRoundedSelectionEdges(boolean rounded) {
/* 1102 */     if (this.roundedSelectionEdges != rounded) {
/* 1103 */       this.roundedSelectionEdges = rounded;
/* 1104 */       Caret c = getCaret();
/* 1105 */       if (c instanceof ConfigurableCaret) {
/* 1106 */         ((ConfigurableCaret)c).setRoundedSelectionEdges(rounded);
/* 1107 */         if (c.getDot() != c.getMark()) {
/* 1108 */           repaint();
/*      */         }
/*      */       } 
/* 1111 */       firePropertyChange("RTA.roundedSelection", !rounded, rounded);
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
/*      */   protected void setRTextAreaUI(RTextAreaUI ui) {
/* 1130 */     setUI(ui);
/*      */ 
/*      */ 
/*      */     
/* 1134 */     setOpaque(getBackgroundObject() instanceof Color);
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
/*      */   public void setTabsEmulated(boolean areEmulated) {
/* 1151 */     this.tabsEmulatedWithSpaces = areEmulated;
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
/*      */   public void setTabSize(int size) {
/* 1167 */     super.setTabSize(size);
/* 1168 */     boolean b = getLineWrap();
/* 1169 */     setLineWrap(!b);
/* 1170 */     setLineWrap(b);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void updateMarginLineX() {
/* 1180 */     Font font = getFont();
/* 1181 */     if (font == null) {
/* 1182 */       this.marginLineX = 0;
/*      */       return;
/*      */     } 
/* 1185 */     this.marginLineX = getFontMetrics(font).charWidth('m') * this.marginSizeInChars;
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
/*      */   public int yForLine(int line) throws BadLocationException {
/* 1201 */     return ((RTextAreaUI)getUI()).yForLine(line);
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
/*      */   public int yForLineContaining(int offs) throws BadLocationException {
/* 1216 */     return ((RTextAreaUI)getUI()).yForLineContaining(offs);
/*      */   }
/*      */   
/*      */   protected static class RTAMouseListener
/*      */     extends CaretEvent
/*      */     implements MouseListener, MouseMotionListener, FocusListener
/*      */   {
/*      */     protected int dot;
/*      */     protected int mark;
/*      */     
/*      */     RTAMouseListener(RTextAreaBase textArea) {
/* 1227 */       super(textArea);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void focusGained(FocusEvent e) {}
/*      */ 
/*      */ 
/*      */     
/*      */     public void focusLost(FocusEvent e) {}
/*      */ 
/*      */ 
/*      */     
/*      */     public void mouseDragged(MouseEvent e) {}
/*      */ 
/*      */ 
/*      */     
/*      */     public void mouseMoved(MouseEvent e) {}
/*      */ 
/*      */ 
/*      */     
/*      */     public void mouseClicked(MouseEvent e) {}
/*      */ 
/*      */ 
/*      */     
/*      */     public void mousePressed(MouseEvent e) {}
/*      */ 
/*      */ 
/*      */     
/*      */     public void mouseReleased(MouseEvent e) {}
/*      */ 
/*      */ 
/*      */     
/*      */     public void mouseEntered(MouseEvent e) {}
/*      */ 
/*      */ 
/*      */     
/*      */     public void mouseExited(MouseEvent e) {}
/*      */ 
/*      */     
/*      */     public int getDot() {
/* 1268 */       return this.dot;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getMark() {
/* 1273 */       return this.mark;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\RTextAreaBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */