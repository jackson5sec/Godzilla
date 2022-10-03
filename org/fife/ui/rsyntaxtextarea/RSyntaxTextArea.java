/*      */ package org.fife.ui.rsyntaxtextarea;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Cursor;
/*      */ import java.awt.Dialog;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Frame;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.RenderingHints;
/*      */ import java.awt.Window;
/*      */ import java.awt.datatransfer.Clipboard;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.MouseEvent;
/*      */ import java.awt.font.FontRenderContext;
/*      */ import java.io.File;
/*      */ import java.lang.reflect.Method;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.ResourceBundle;
/*      */ import javax.swing.AbstractAction;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JMenu;
/*      */ import javax.swing.JPopupMenu;
/*      */ import javax.swing.JTextArea;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.Timer;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.event.CaretEvent;
/*      */ import javax.swing.event.CaretListener;
/*      */ import javax.swing.event.HyperlinkEvent;
/*      */ import javax.swing.event.HyperlinkListener;
/*      */ import javax.swing.text.AbstractDocument;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import javax.swing.text.Document;
/*      */ import javax.swing.text.Element;
/*      */ import javax.swing.text.Highlighter;
/*      */ import javax.swing.text.JTextComponent;
/*      */ import org.fife.rsta.ui.GoToDialog;
/*      */ import org.fife.rsta.ui.search.ReplaceDialog;
/*      */ import org.fife.rsta.ui.search.SearchListener;
/*      */ import org.fife.rsta.ui.search.SearchListenerImpl;
/*      */ import org.fife.ui.rsyntaxtextarea.focusabletip.FocusableTip;
/*      */ import org.fife.ui.rsyntaxtextarea.folding.DefaultFoldManager;
/*      */ import org.fife.ui.rsyntaxtextarea.folding.Fold;
/*      */ import org.fife.ui.rsyntaxtextarea.folding.FoldManager;
/*      */ import org.fife.ui.rsyntaxtextarea.parser.Parser;
/*      */ import org.fife.ui.rsyntaxtextarea.parser.ParserNotice;
/*      */ import org.fife.ui.rsyntaxtextarea.parser.ToolTipInfo;
/*      */ import org.fife.ui.rtextarea.RTextArea;
/*      */ import org.fife.ui.rtextarea.RTextAreaBase;
/*      */ import org.fife.ui.rtextarea.RTextAreaUI;
/*      */ import org.fife.ui.rtextarea.RecordableTextAction;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class RSyntaxTextArea
/*      */   extends RTextArea
/*      */   implements SyntaxConstants
/*      */ {
/*      */   public static final String ANIMATE_BRACKET_MATCHING_PROPERTY = "RSTA.animateBracketMatching";
/*      */   public static final String ANTIALIAS_PROPERTY = "RSTA.antiAlias";
/*      */   public static final String AUTO_INDENT_PROPERTY = "RSTA.autoIndent";
/*      */   public static final String BRACKET_MATCHING_PROPERTY = "RSTA.bracketMatching";
/*      */   public static final String CLEAR_WHITESPACE_LINES_PROPERTY = "RSTA.clearWhitespaceLines";
/*      */   public static final String CLOSE_CURLY_BRACES_PROPERTY = "RSTA.closeCurlyBraces";
/*      */   public static final String CLOSE_MARKUP_TAGS_PROPERTY = "RSTA.closeMarkupTags";
/*      */   public static final String CODE_FOLDING_PROPERTY = "RSTA.codeFolding";
/*      */   public static final String EOL_VISIBLE_PROPERTY = "RSTA.eolMarkersVisible";
/*      */   public static final String FOCUSABLE_TIPS_PROPERTY = "RSTA.focusableTips";
/*      */   public static final String FRACTIONAL_FONTMETRICS_PROPERTY = "RSTA.fractionalFontMetrics";
/*      */   public static final String HIGHLIGHT_SECONDARY_LANGUAGES_PROPERTY = "RSTA.highlightSecondaryLanguages";
/*      */   public static final String HYPERLINKS_ENABLED_PROPERTY = "RSTA.hyperlinksEnabled";
/*      */   public static final String MARK_OCCURRENCES_PROPERTY = "RSTA.markOccurrences";
/*      */   public static final String MARKED_OCCURRENCES_CHANGED_PROPERTY = "RSTA.markedOccurrencesChanged";
/*      */   public static final String PAINT_MATCHED_BRACKET_PAIR_PROPERTY = "RSTA.paintMatchedBracketPair";
/*      */   public static final String PARSER_NOTICES_PROPERTY = "RSTA.parserNotices";
/*      */   public static final String SYNTAX_SCHEME_PROPERTY = "RSTA.syntaxScheme";
/*      */   public static final String SYNTAX_STYLE_PROPERTY = "RSTA.syntaxStyle";
/*      */   public static final String TAB_LINE_COLOR_PROPERTY = "RSTA.tabLineColor";
/*      */   public static final String TAB_LINES_PROPERTY = "RSTA.tabLines";
/*      */   public static final String USE_SELECTED_TEXT_COLOR_PROPERTY = "RSTA.useSelectedTextColor";
/*      */   public static final String VISIBLE_WHITESPACE_PROPERTY = "RSTA.visibleWhitespace";
/*  151 */   private static final Color DEFAULT_BRACKET_MATCH_BG_COLOR = new Color(234, 234, 255);
/*  152 */   private static final Color DEFAULT_BRACKET_MATCH_BORDER_COLOR = new Color(0, 0, 128);
/*  153 */   private static final Color DEFAULT_SELECTION_COLOR = new Color(200, 200, 255);
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String MSG = "org.fife.ui.rsyntaxtextarea.RSyntaxTextArea";
/*      */ 
/*      */ 
/*      */   
/*      */   private JMenu foldingMenu;
/*      */ 
/*      */ 
/*      */   
/*      */   private static RecordableTextAction toggleCurrentFoldAction;
/*      */ 
/*      */ 
/*      */   
/*      */   private static RecordableTextAction collapseAllCommentFoldsAction;
/*      */ 
/*      */ 
/*      */   
/*      */   private static RecordableTextAction collapseAllFoldsAction;
/*      */ 
/*      */   
/*      */   private static RecordableTextAction expandAllFoldsAction;
/*      */ 
/*      */   
/*      */   private String syntaxStyleKey;
/*      */ 
/*      */   
/*      */   private SyntaxScheme syntaxScheme;
/*      */ 
/*      */   
/*      */   private static CodeTemplateManager codeTemplateManager;
/*      */ 
/*      */   
/*      */   private static boolean templatesEnabled;
/*      */ 
/*      */   
/*      */   private Rectangle match;
/*      */ 
/*      */   
/*      */   private Rectangle dotRect;
/*      */ 
/*      */   
/*      */   private Point bracketInfo;
/*      */ 
/*      */   
/*      */   private Color matchedBracketBGColor;
/*      */ 
/*      */   
/*      */   private Color matchedBracketBorderColor;
/*      */ 
/*      */   
/*      */   private int lastBracketMatchPos;
/*      */ 
/*      */   
/*      */   private boolean bracketMatchingEnabled;
/*      */ 
/*      */   
/*      */   private boolean animateBracketMatching;
/*      */ 
/*      */   
/*      */   private boolean paintMatchedBracketPair;
/*      */ 
/*      */   
/*      */   private BracketMatchingTimer bracketRepaintTimer;
/*      */ 
/*      */   
/*      */   private MatchedBracketPopupTimer matchedBracketPopupTimer;
/*      */ 
/*      */   
/*      */   private boolean metricsNeverRefreshed;
/*      */ 
/*      */   
/*      */   private boolean autoIndentEnabled;
/*      */ 
/*      */   
/*      */   private boolean closeCurlyBraces;
/*      */ 
/*      */   
/*      */   private boolean closeMarkupTags;
/*      */ 
/*      */   
/*      */   private boolean clearWhitespaceLines;
/*      */ 
/*      */   
/*      */   private boolean whitespaceVisible;
/*      */ 
/*      */   
/*      */   private boolean eolMarkersVisible;
/*      */ 
/*      */   
/*      */   private boolean paintTabLines;
/*      */ 
/*      */   
/*      */   private Color tabLineColor;
/*      */ 
/*      */   
/*      */   private boolean hyperlinksEnabled;
/*      */ 
/*      */   
/*      */   private Color hyperlinkFG;
/*      */ 
/*      */   
/*      */   private int linkScanningMask;
/*      */ 
/*      */   
/*      */   private boolean highlightSecondaryLanguages;
/*      */ 
/*      */   
/*      */   private boolean useSelectedTextColor;
/*      */ 
/*      */   
/*      */   private MarkOccurrencesSupport markOccurrencesSupport;
/*      */ 
/*      */   
/*      */   private Color markOccurrencesColor;
/*      */ 
/*      */   
/*      */   private int markOccurrencesDelay;
/*      */ 
/*      */   
/*      */   private boolean paintMarkOccurrencesBorder;
/*      */ 
/*      */   
/*      */   private FontMetrics defaultFontMetrics;
/*      */ 
/*      */   
/*      */   private ParserManager parserManager;
/*      */ 
/*      */   
/*      */   private String cachedTip;
/*      */ 
/*      */   
/*      */   private Point cachedTipLoc;
/*      */ 
/*      */   
/*      */   private boolean isScanningForLinks;
/*      */ 
/*      */   
/*      */   private int hoveredOverLinkOffset;
/*      */ 
/*      */   
/*      */   private LinkGenerator linkGenerator;
/*      */ 
/*      */   
/*      */   private LinkGeneratorResult linkGeneratorResult;
/*      */ 
/*      */   
/*      */   private int rhsCorrection;
/*      */ 
/*      */   
/*      */   private FoldManager foldManager;
/*      */ 
/*      */   
/*      */   private boolean useFocusableTips;
/*      */ 
/*      */   
/*      */   private FocusableTip focusableTip;
/*      */ 
/*      */   
/*      */   private Map<?, ?> aaHints;
/*      */ 
/*      */   
/*      */   private TokenPainter tokenPainter;
/*      */ 
/*      */   
/*      */   private boolean showMatchedBracketPopup;
/*      */ 
/*      */   
/*      */   private int lineHeight;
/*      */ 
/*      */   
/*      */   private int maxAscent;
/*      */ 
/*      */   
/*      */   private boolean fractionalFontMetricsEnabled;
/*      */ 
/*      */   
/*      */   private Color[] secondaryLanguageBackgrounds;
/*      */ 
/*      */ 
/*      */   
/*      */   public RSyntaxTextArea() {
/*  337 */     registerReplaceDialog();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public RSyntaxTextArea(RSyntaxDocument doc) {
/*  347 */     super((AbstractDocument)doc);
/*  348 */     setSyntaxEditingStyle(doc.getSyntaxStyle());
/*  349 */     registerReplaceDialog();
/*  350 */     registerGoToDialog();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public RSyntaxTextArea(String text) {
/*  359 */     super(text);
/*  360 */     registerReplaceDialog();
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
/*      */   public RSyntaxTextArea(int rows, int cols) {
/*  373 */     super(rows, cols);
/*  374 */     registerReplaceDialog();
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
/*      */   public RSyntaxTextArea(String text, int rows, int cols) {
/*  388 */     super(text, rows, cols);
/*  389 */     registerReplaceDialog();
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
/*      */   public RSyntaxTextArea(RSyntaxDocument doc, String text, int rows, int cols) {
/*  404 */     super((AbstractDocument)doc, text, rows, cols);
/*  405 */     registerReplaceDialog();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public RSyntaxTextArea(int textMode) {
/*  416 */     super(textMode);
/*  417 */     registerReplaceDialog();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addActiveLineRangeListener(ActiveLineRangeListener l) {
/*  428 */     this.listenerList.add(ActiveLineRangeListener.class, l);
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
/*      */   public void addHyperlinkListener(HyperlinkListener l) {
/*  444 */     this.listenerList.add(HyperlinkListener.class, l);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addNotify() {
/*  454 */     super.addNotify();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  460 */     if (this.metricsNeverRefreshed) {
/*  461 */       Window parent = SwingUtilities.getWindowAncestor((Component)this);
/*  462 */       if (parent != null && parent.getWidth() > 0 && parent.getHeight() > 0) {
/*  463 */         refreshFontMetrics(getGraphics2D(getGraphics()));
/*  464 */         this.metricsNeverRefreshed = false;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  470 */     if (this.parserManager != null) {
/*  471 */       this.parserManager.restartParsing();
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
/*      */   public void addParser(Parser parser) {
/*  489 */     if (this.parserManager == null) {
/*  490 */       this.parserManager = new ParserManager(this);
/*      */     }
/*  492 */     this.parserManager.addParser(parser);
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
/*      */   protected void appendFoldingMenu(JPopupMenu popup) {
/*  504 */     popup.addSeparator();
/*  505 */     ResourceBundle bundle = ResourceBundle.getBundle("org.fife.ui.rsyntaxtextarea.RSyntaxTextArea");
/*  506 */     this.foldingMenu = new JMenu(bundle.getString("ContextMenu.Folding"));
/*  507 */     this.foldingMenu.add(createPopupMenuItem((Action)toggleCurrentFoldAction));
/*  508 */     this.foldingMenu.add(createPopupMenuItem((Action)collapseAllCommentFoldsAction));
/*  509 */     this.foldingMenu.add(createPopupMenuItem((Action)collapseAllFoldsAction));
/*  510 */     this.foldingMenu.add(createPopupMenuItem((Action)expandAllFoldsAction));
/*  511 */     popup.add(this.foldingMenu);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void calculateLineHeight() {
/*  522 */     this.lineHeight = this.maxAscent = 0;
/*      */ 
/*      */     
/*  525 */     for (int i = 0; i < this.syntaxScheme.getStyleCount(); i++) {
/*  526 */       Style ss = this.syntaxScheme.getStyle(i);
/*  527 */       if (ss != null && ss.font != null) {
/*  528 */         FontMetrics fontMetrics = getFontMetrics(ss.font);
/*  529 */         int j = fontMetrics.getHeight();
/*  530 */         if (j > this.lineHeight) {
/*  531 */           this.lineHeight = j;
/*      */         }
/*  533 */         int k = fontMetrics.getMaxAscent();
/*  534 */         if (k > this.maxAscent) {
/*  535 */           this.maxAscent = k;
/*      */         }
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  541 */     Font temp = getFont();
/*  542 */     FontMetrics fm = getFontMetrics(temp);
/*  543 */     int height = fm.getHeight();
/*  544 */     if (height > this.lineHeight) {
/*  545 */       this.lineHeight = height;
/*      */     }
/*  547 */     int ascent = fm.getMaxAscent();
/*  548 */     if (ascent > this.maxAscent) {
/*  549 */       this.maxAscent = ascent;
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
/*      */   public void clearParsers() {
/*  561 */     if (this.parserManager != null) {
/*  562 */       this.parserManager.clearParsers();
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
/*      */   private TokenImpl cloneTokenList(Token t) {
/*  577 */     if (t == null) {
/*  578 */       return null;
/*      */     }
/*      */     
/*  581 */     TokenImpl clone = new TokenImpl(t);
/*  582 */     TokenImpl cloneEnd = clone;
/*      */     
/*  584 */     while ((t = t.getNextToken()) != null) {
/*  585 */       TokenImpl temp = new TokenImpl(t);
/*  586 */       cloneEnd.setNextToken(temp);
/*  587 */       cloneEnd = temp;
/*      */     } 
/*      */     
/*  590 */     return clone;
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
/*      */   protected void configurePopupMenu(JPopupMenu popupMenu) {
/*  610 */     super.configurePopupMenu(popupMenu);
/*      */ 
/*      */     
/*  613 */     if (popupMenu != null && popupMenu.getComponentCount() > 0 && this.foldingMenu != null)
/*      */     {
/*  615 */       this.foldingMenu.setEnabled(this.foldManager
/*  616 */           .isCodeFoldingSupportedAndEnabled());
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
/*      */   public void copyAsStyledText(Theme theme) {
/*  634 */     if (theme == null) {
/*  635 */       copyAsStyledText();
/*      */       
/*      */       return;
/*      */     } 
/*  639 */     Theme origTheme = new Theme(this);
/*      */     
/*  641 */     theme.apply(this);
/*      */     try {
/*  643 */       copyAsStyledText();
/*      */     } finally {
/*  645 */       origTheme.apply(this);
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
/*      */   public void copyAsStyledText() {
/*  658 */     int selStart = getSelectionStart();
/*  659 */     int selEnd = getSelectionEnd();
/*  660 */     if (selStart == selEnd) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  665 */     String html = HtmlUtil.getTextAsHtml(this, selStart, selEnd);
/*      */ 
/*      */     
/*  668 */     byte[] rtfBytes = getTextAsRtf(selStart, selEnd);
/*      */ 
/*      */     
/*  671 */     StyledTextTransferable contents = new StyledTextTransferable(html, rtfBytes);
/*      */     
/*  673 */     Clipboard cb = getToolkit().getSystemClipboard();
/*      */     try {
/*  675 */       cb.setContents(contents, null);
/*  676 */     } catch (IllegalStateException ise) {
/*  677 */       UIManager.getLookAndFeel().provideErrorFeedback(null);
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
/*      */   protected Document createDefaultModel() {
/*  689 */     return (Document)new RSyntaxDocument("text/plain");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private HyperlinkEvent createHyperlinkEvent(HyperlinkEvent.EventType type) {
/*  697 */     if (type == HyperlinkEvent.EventType.EXITED) {
/*  698 */       return new HyperlinkEvent(this, type, null);
/*      */     }
/*      */     
/*  701 */     HyperlinkEvent he = null;
/*      */     
/*  703 */     if (this.linkGeneratorResult != null) {
/*  704 */       he = this.linkGeneratorResult.execute();
/*  705 */       this.linkGeneratorResult = null;
/*      */     } else {
/*      */       
/*  708 */       Token t = modelToToken(this.hoveredOverLinkOffset);
/*  709 */       if (t != null) {
/*  710 */         URL url = null;
/*  711 */         String desc = null;
/*      */         try {
/*  713 */           String temp = t.getLexeme();
/*      */           
/*  715 */           if (temp.startsWith("www.")) {
/*  716 */             temp = "http://" + temp;
/*      */           }
/*  718 */           url = new URL(temp);
/*  719 */         } catch (MalformedURLException mue) {
/*  720 */           desc = mue.getMessage();
/*      */         } 
/*  722 */         he = new HyperlinkEvent(this, type, url, desc);
/*      */       } 
/*      */     } 
/*      */     
/*  726 */     return he;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected RTextAreaBase.RTAMouseListener createMouseListener() {
/*  737 */     return (RTextAreaBase.RTAMouseListener)new RSyntaxTextAreaMutableCaretEvent(this);
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
/*      */   protected JPopupMenu createPopupMenu() {
/*  749 */     JPopupMenu popup = super.createPopupMenu();
/*  750 */     appendFoldingMenu(popup);
/*  751 */     return popup;
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
/*      */   private static void createRstaPopupMenuActions() {
/*  763 */     ResourceBundle msg = ResourceBundle.getBundle("org.fife.ui.rsyntaxtextarea.RSyntaxTextArea");
/*      */     
/*  765 */     toggleCurrentFoldAction = new RSyntaxTextAreaEditorKit.ToggleCurrentFoldAction();
/*      */     
/*  767 */     toggleCurrentFoldAction.setProperties(msg, "Action.ToggleCurrentFold");
/*      */     
/*  769 */     collapseAllCommentFoldsAction = new RSyntaxTextAreaEditorKit.CollapseAllCommentFoldsAction();
/*      */     
/*  771 */     collapseAllCommentFoldsAction.setProperties(msg, "Action.CollapseCommentFolds");
/*      */     
/*  773 */     collapseAllFoldsAction = new RSyntaxTextAreaEditorKit.CollapseAllFoldsAction(true);
/*  774 */     expandAllFoldsAction = new RSyntaxTextAreaEditorKit.ExpandAllFoldsAction(true);
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
/*      */   protected RTextAreaUI createRTextAreaUI() {
/*  786 */     return new RSyntaxTextAreaUI((JComponent)this);
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
/*      */   protected final void doBracketMatching() {
/*  798 */     if (this.match != null) {
/*  799 */       repaint(this.match);
/*  800 */       if (this.dotRect != null) {
/*  801 */         repaint(this.dotRect);
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  806 */     int lastCaretBracketPos = (this.bracketInfo == null) ? -1 : this.bracketInfo.x;
/*  807 */     this.bracketInfo = RSyntaxUtilities.getMatchingBracketPosition(this, this.bracketInfo);
/*      */     
/*  809 */     if (this.bracketInfo.y > -1 && (this.bracketInfo.y != this.lastBracketMatchPos || this.bracketInfo.x != lastCaretBracketPos)) {
/*      */ 
/*      */       
/*      */       try {
/*  813 */         this.match = modelToView(this.bracketInfo.y);
/*  814 */         if (this.match != null) {
/*  815 */           if (getPaintMatchedBracketPair()) {
/*  816 */             this.dotRect = modelToView(this.bracketInfo.x);
/*      */           } else {
/*      */             
/*  819 */             this.dotRect = null;
/*      */           } 
/*  821 */           if (getAnimateBracketMatching()) {
/*  822 */             this.bracketRepaintTimer.restart();
/*      */           }
/*  824 */           repaint(this.match);
/*  825 */           if (this.dotRect != null) {
/*  826 */             repaint(this.dotRect);
/*      */           }
/*      */           
/*  829 */           if (getShowMatchedBracketPopup()) {
/*  830 */             Container parent = getParent();
/*  831 */             if (parent instanceof javax.swing.JViewport) {
/*  832 */               Rectangle visibleRect = getVisibleRect();
/*  833 */               if ((this.match.y + this.match.height) < visibleRect.getY()) {
/*  834 */                 if (this.matchedBracketPopupTimer == null) {
/*  835 */                   this.matchedBracketPopupTimer = new MatchedBracketPopupTimer();
/*      */                 }
/*      */                 
/*  838 */                 this.matchedBracketPopupTimer.restart(this.bracketInfo.y);
/*      */               }
/*      */             
/*      */             } 
/*      */           } 
/*      */         } 
/*  844 */       } catch (BadLocationException ble) {
/*  845 */         ble.printStackTrace();
/*      */       }
/*      */     
/*  848 */     } else if (this.bracketInfo.y == -1) {
/*      */       
/*  850 */       this.match = null;
/*  851 */       this.dotRect = null;
/*  852 */       this.bracketRepaintTimer.stop();
/*      */     } 
/*  854 */     this.lastBracketMatchPos = this.bracketInfo.y;
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
/*      */   protected void fireCaretUpdate(CaretEvent e) {
/*  866 */     super.fireCaretUpdate(e);
/*  867 */     if (isBracketMatchingEnabled()) {
/*  868 */       doBracketMatching();
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
/*      */   private void fireActiveLineRangeEvent(int min, int max) {
/*  880 */     ActiveLineRangeEvent e = null;
/*      */     
/*  882 */     Object[] listeners = this.listenerList.getListenerList();
/*      */ 
/*      */     
/*  885 */     for (int i = listeners.length - 2; i >= 0; i -= 2) {
/*  886 */       if (listeners[i] == ActiveLineRangeListener.class) {
/*  887 */         if (e == null) {
/*  888 */           e = new ActiveLineRangeEvent(this, min, max);
/*      */         }
/*  890 */         ((ActiveLineRangeListener)listeners[i + 1]).activeLineRangeChanged(e);
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
/*      */   private void fireHyperlinkUpdate(HyperlinkEvent.EventType type) {
/*  904 */     HyperlinkEvent e = null;
/*      */ 
/*      */     
/*  907 */     Object[] listeners = this.listenerList.getListenerList();
/*      */ 
/*      */ 
/*      */     
/*  911 */     for (int i = listeners.length - 2; i >= 0; i -= 2) {
/*      */       
/*  913 */       if (listeners[i] == HyperlinkListener.class) {
/*      */         
/*  915 */         if (e == null) {
/*  916 */           e = createHyperlinkEvent(type);
/*  917 */           if (e == null) {
/*      */             return;
/*      */           }
/*      */         } 
/*  921 */         ((HyperlinkListener)listeners[i + 1]).hyperlinkUpdate(e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void fireMarkedOccurrencesChanged() {
/*  932 */     firePropertyChange("RSTA.markedOccurrencesChanged", null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void fireParserNoticesChange() {
/*  942 */     firePropertyChange("RSTA.parserNotices", null, null);
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
/*      */   public void foldToggled(Fold fold) {
/*  954 */     this.match = null;
/*  955 */     this.dotRect = null;
/*  956 */     if (getLineWrap()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  962 */       SwingUtilities.invokeLater(this::possiblyUpdateCurrentLineHighlightLocation);
/*      */     } else {
/*      */       
/*  965 */       possiblyUpdateCurrentLineHighlightLocation();
/*      */     } 
/*  967 */     revalidate();
/*  968 */     repaint();
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
/*      */   public void forceReparsing(int parser) {
/*  986 */     this.parserManager.forceReparsing(parser);
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
/*      */   public boolean forceReparsing(Parser parser) {
/* 1001 */     for (int i = 0; i < getParserCount(); i++) {
/* 1002 */       if (getParser(i) == parser) {
/* 1003 */         forceReparsing(i);
/* 1004 */         return true;
/*      */       } 
/*      */     } 
/* 1007 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getAnimateBracketMatching() {
/* 1018 */     return this.animateBracketMatching;
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
/*      */   public boolean getAntiAliasingEnabled() {
/* 1030 */     return (this.aaHints != null);
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
/*      */   public Color getBackgroundForToken(Token token) {
/* 1044 */     Color c = null;
/* 1045 */     if (getHighlightSecondaryLanguages()) {
/*      */       
/* 1047 */       int languageIndex = token.getLanguageIndex() - 1;
/* 1048 */       if (languageIndex >= 0 && languageIndex < this.secondaryLanguageBackgrounds.length)
/*      */       {
/* 1050 */         c = this.secondaryLanguageBackgrounds[languageIndex];
/*      */       }
/*      */     } 
/* 1053 */     if (c == null) {
/* 1054 */       c = (this.syntaxScheme.getStyle(token.getType())).background;
/*      */     }
/*      */ 
/*      */     
/* 1058 */     return c;
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
/*      */   public boolean getCloseCurlyBraces() {
/* 1072 */     return this.closeCurlyBraces;
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
/*      */   public boolean getCloseMarkupTags() {
/* 1085 */     return this.closeMarkupTags;
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
/*      */   public static synchronized CodeTemplateManager getCodeTemplateManager() {
/* 1097 */     if (codeTemplateManager == null) {
/* 1098 */       codeTemplateManager = new CodeTemplateManager();
/*      */     }
/* 1100 */     return codeTemplateManager;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Color getDefaultBracketMatchBGColor() {
/* 1111 */     return DEFAULT_BRACKET_MATCH_BG_COLOR;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Color getDefaultBracketMatchBorderColor() {
/* 1122 */     return DEFAULT_BRACKET_MATCH_BORDER_COLOR;
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
/*      */   public static Color getDefaultSelectionColor() {
/* 1135 */     return DEFAULT_SELECTION_COLOR;
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
/*      */   public SyntaxScheme getDefaultSyntaxScheme() {
/* 1149 */     return new SyntaxScheme(getFont());
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
/*      */   public boolean getEOLMarkersVisible() {
/* 1161 */     return this.eolMarkersVisible;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FoldManager getFoldManager() {
/* 1171 */     return this.foldManager;
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
/*      */   public Font getFontForTokenType(int type) {
/* 1183 */     Font f = (this.syntaxScheme.getStyle(type)).font;
/* 1184 */     return (f != null) ? f : getFont();
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
/*      */   public FontMetrics getFontMetricsForTokenType(int type) {
/* 1196 */     FontMetrics fm = (this.syntaxScheme.getStyle(type)).fontMetrics;
/* 1197 */     return (fm != null) ? fm : this.defaultFontMetrics;
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
/*      */   public Color getForegroundForToken(Token t) {
/* 1210 */     if (getHyperlinksEnabled() && this.hoveredOverLinkOffset == t.getOffset() && (t
/* 1211 */       .isHyperlink() || this.linkGeneratorResult != null)) {
/* 1212 */       return this.hyperlinkFG;
/*      */     }
/* 1214 */     return getForegroundForTokenType(t.getType());
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
/*      */   public Color getForegroundForTokenType(int type) {
/* 1228 */     Color fg = (this.syntaxScheme.getStyle(type)).foreground;
/* 1229 */     return (fg != null) ? fg : getForeground();
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
/*      */   public boolean getFractionalFontMetricsEnabled() {
/* 1241 */     return this.fractionalFontMetricsEnabled;
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
/*      */   private Graphics2D getGraphics2D(Graphics g) {
/* 1254 */     Graphics2D g2d = (Graphics2D)g;
/* 1255 */     if (this.aaHints != null) {
/* 1256 */       g2d.addRenderingHints(this.aaHints);
/*      */     }
/* 1258 */     if (this.fractionalFontMetricsEnabled) {
/* 1259 */       g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
/*      */     }
/*      */     
/* 1262 */     return g2d;
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
/*      */   public boolean getHighlightSecondaryLanguages() {
/* 1279 */     return this.highlightSecondaryLanguages;
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
/*      */   public Color getHyperlinkForeground() {
/* 1291 */     return this.hyperlinkFG;
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
/*      */   public boolean getHyperlinksEnabled() {
/* 1303 */     return this.hyperlinksEnabled;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getLastVisibleOffset() {
/* 1314 */     if (isCodeFoldingEnabled()) {
/* 1315 */       int lastVisibleLine = this.foldManager.getLastVisibleLine();
/* 1316 */       if (lastVisibleLine < getLineCount() - 1) {
/*      */         try {
/* 1318 */           return getLineEndOffset(lastVisibleLine) - 1;
/* 1319 */         } catch (BadLocationException ble) {
/* 1320 */           ble.printStackTrace();
/*      */         } 
/*      */       }
/*      */     } 
/* 1324 */     return getDocument().getLength();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getLineHeight() {
/* 1335 */     return this.lineHeight;
/*      */   }
/*      */ 
/*      */   
/*      */   public LinkGenerator getLinkGenerator() {
/* 1340 */     return this.linkGenerator;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<DocumentRange> getMarkAllHighlightRanges() {
/* 1351 */     return ((RSyntaxTextAreaHighlighter)getHighlighter())
/* 1352 */       .getMarkAllHighlightRanges();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<DocumentRange> getMarkedOccurrences() {
/* 1363 */     return ((RSyntaxTextAreaHighlighter)getHighlighter())
/* 1364 */       .getMarkedOccurrences();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getMarkOccurrences() {
/* 1375 */     return (this.markOccurrencesSupport != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Color getMarkOccurrencesColor() {
/* 1386 */     return this.markOccurrencesColor;
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
/*      */   public int getMarkOccurrencesDelay() {
/* 1398 */     return this.markOccurrencesDelay;
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
/*      */   boolean getMarkOccurrencesOfTokenType(int type) {
/* 1411 */     RSyntaxDocument doc = (RSyntaxDocument)getDocument();
/* 1412 */     return doc.getMarkOccurrencesOfTokenType(type);
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
/*      */   public Color getMatchedBracketBGColor() {
/* 1425 */     return this.matchedBracketBGColor;
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
/*      */   public Color getMatchedBracketBorderColor() {
/* 1437 */     return this.matchedBracketBorderColor;
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
/*      */   Rectangle getDotRectangle() {
/* 1451 */     return this.dotRect;
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
/*      */   Rectangle getMatchRectangle() {
/* 1464 */     return this.match;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxAscent() {
/* 1475 */     return this.maxAscent;
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
/*      */   public boolean getPaintMatchedBracketPair() {
/* 1492 */     return this.paintMatchedBracketPair;
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
/*      */   public boolean getPaintTabLines() {
/* 1504 */     return this.paintTabLines;
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
/*      */   boolean getPaintTokenBackgrounds(int line, float y) {
/* 1520 */     int iy = (int)y;
/* 1521 */     int curCaretY = getCurrentCaretY();
/* 1522 */     return (iy < curCaretY || iy >= curCaretY + getLineHeight() || 
/* 1523 */       !getHighlightCurrentLine());
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
/*      */   public Parser getParser(int index) {
/* 1536 */     return this.parserManager.getParser(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getParserCount() {
/* 1547 */     return (this.parserManager == null) ? 0 : this.parserManager.getParserCount();
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
/*      */   public int getParserDelay() {
/* 1559 */     return this.parserManager.getDelay();
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
/*      */   public List<ParserNotice> getParserNotices() {
/* 1572 */     if (this.parserManager == null) {
/* 1573 */       return Collections.emptyList();
/*      */     }
/* 1575 */     return this.parserManager.getParserNotices();
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
/*      */   public int getRightHandSideCorrection() {
/* 1587 */     return this.rhsCorrection;
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
/*      */   public boolean getShouldIndentNextLine(int line) {
/* 1610 */     if (isAutoIndentEnabled()) {
/* 1611 */       RSyntaxDocument doc = (RSyntaxDocument)getDocument();
/* 1612 */       return doc.getShouldIndentNextLine(line);
/*      */     } 
/* 1614 */     return false;
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
/*      */   public boolean getShowMatchedBracketPopup() {
/* 1627 */     return this.showMatchedBracketPopup;
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
/*      */   public String getSyntaxEditingStyle() {
/* 1640 */     return this.syntaxStyleKey;
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
/*      */   public SyntaxScheme getSyntaxScheme() {
/* 1653 */     return this.syntaxScheme;
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
/*      */   public Color getTabLineColor() {
/* 1666 */     return this.tabLineColor;
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
/*      */   public boolean getPaintMarkOccurrencesBorder() {
/* 1679 */     return this.paintMarkOccurrencesBorder;
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
/*      */   public Color getSecondaryLanguageBackground(int index) {
/* 1695 */     return this.secondaryLanguageBackgrounds[index - 1];
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
/*      */   public int getSecondaryLanguageCount() {
/* 1708 */     return this.secondaryLanguageBackgrounds.length;
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
/*      */   public static synchronized boolean getTemplatesEnabled() {
/* 1728 */     return templatesEnabled;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private byte[] getTextAsRtf(int start, int end) {
/* 1734 */     RtfGenerator gen = new RtfGenerator(getBackground());
/* 1735 */     Token tokenList = getTokenListFor(start, end);
/* 1736 */     for (Token t = tokenList; t != null; t = t.getNextToken()) {
/* 1737 */       if (t.isPaintable()) {
/* 1738 */         if (t.length() == 1 && t.charAt(0) == '\n') {
/* 1739 */           gen.appendNewline();
/*      */         } else {
/* 1741 */           Font font = getFontForTokenType(t.getType());
/* 1742 */           Color bg = getBackgroundForToken(t);
/* 1743 */           boolean underline = getUnderlineForToken(t);
/*      */ 
/*      */           
/* 1746 */           if (t.isWhitespace()) {
/* 1747 */             gen.appendToDocNoFG(t.getLexeme(), font, bg, underline);
/*      */           } else {
/* 1749 */             Color fg = getForegroundForToken(t);
/* 1750 */             gen.appendToDoc(t.getLexeme(), font, fg, bg, underline);
/*      */           } 
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1757 */     return gen.getRtf().getBytes(StandardCharsets.UTF_8);
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
/*      */   public Token getTokenListFor(int startOffs, int endOffs) {
/* 1770 */     TokenImpl tokenList = null;
/* 1771 */     TokenImpl lastToken = null;
/*      */     
/* 1773 */     Element map = getDocument().getDefaultRootElement();
/* 1774 */     int startLine = map.getElementIndex(startOffs);
/* 1775 */     int endLine = map.getElementIndex(endOffs);
/*      */     
/* 1777 */     for (int line = startLine; line <= endLine; line++) {
/* 1778 */       TokenImpl t = (TokenImpl)getTokenListForLine(line);
/* 1779 */       t = cloneTokenList(t);
/* 1780 */       if (tokenList == null) {
/* 1781 */         tokenList = t;
/* 1782 */         lastToken = tokenList;
/*      */       } else {
/*      */         
/* 1785 */         lastToken.setNextToken(t);
/*      */       } 
/* 1787 */       while (lastToken.getNextToken() != null && lastToken
/* 1788 */         .getNextToken().isPaintable()) {
/* 1789 */         lastToken = (TokenImpl)lastToken.getNextToken();
/*      */       }
/* 1791 */       if (line < endLine) {
/*      */ 
/*      */         
/* 1794 */         int docOffs = map.getElement(line).getEndOffset() - 1;
/* 1795 */         t = new TokenImpl(new char[] { '\n' }, 0, 0, docOffs, 21, 0);
/*      */         
/* 1797 */         lastToken.setNextToken(t);
/* 1798 */         lastToken = t;
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
/*      */ 
/*      */     
/* 1811 */     if (startOffs >= tokenList.getOffset()) {
/* 1812 */       while (!tokenList.containsPosition(startOffs)) {
/* 1813 */         tokenList = (TokenImpl)tokenList.getNextToken();
/*      */       }
/* 1815 */       tokenList.makeStartAt(startOffs);
/*      */     } 
/*      */     
/* 1818 */     TokenImpl temp = tokenList;
/*      */ 
/*      */     
/* 1821 */     while (temp != null && !temp.containsPosition(endOffs)) {
/* 1822 */       temp = (TokenImpl)temp.getNextToken();
/*      */     }
/* 1824 */     if (temp != null) {
/* 1825 */       temp.textCount = endOffs - temp.getOffset();
/* 1826 */       temp.setNextToken(null);
/*      */     } 
/*      */     
/* 1829 */     return tokenList;
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
/*      */   public Token getTokenListForLine(int line) {
/* 1841 */     return ((RSyntaxDocument)getDocument()).getTokenListForLine(line);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   TokenPainter getTokenPainter() {
/* 1851 */     return this.tokenPainter;
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
/*      */   public String getToolTipText(MouseEvent e) {
/* 1870 */     if (RSyntaxUtilities.getOS() == 2) {
/* 1871 */       Point newLoc = e.getPoint();
/* 1872 */       if (newLoc != null && newLoc.equals(this.cachedTipLoc)) {
/* 1873 */         return this.cachedTip;
/*      */       }
/* 1875 */       this.cachedTipLoc = newLoc;
/*      */     } 
/*      */     
/* 1878 */     return this.cachedTip = getToolTipTextImpl(e);
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
/*      */   protected String getToolTipTextImpl(MouseEvent e) {
/* 1892 */     String text = null;
/* 1893 */     URL imageBase = null;
/* 1894 */     if (this.parserManager != null) {
/* 1895 */       ToolTipInfo info = this.parserManager.getToolTipText(e);
/* 1896 */       if (info != null) {
/* 1897 */         text = info.getToolTipText();
/* 1898 */         imageBase = info.getImageBase();
/*      */       } 
/*      */     } 
/* 1901 */     if (text == null) {
/* 1902 */       text = super.getToolTipText(e);
/*      */     }
/*      */ 
/*      */     
/* 1906 */     if (getUseFocusableTips()) {
/* 1907 */       if (text != null) {
/* 1908 */         if (this.focusableTip == null) {
/* 1909 */           this.focusableTip = new FocusableTip((JTextArea)this, this.parserManager);
/*      */         }
/* 1911 */         this.focusableTip.setImageBase(imageBase);
/* 1912 */         this.focusableTip.toolTipRequested(e, text);
/*      */ 
/*      */       
/*      */       }
/* 1916 */       else if (this.focusableTip != null) {
/* 1917 */         this.focusableTip.possiblyDisposeOfTipWindow();
/*      */       } 
/* 1919 */       return null;
/*      */     } 
/*      */     
/* 1922 */     return text;
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
/*      */   public boolean getUnderlineForToken(Token t) {
/* 1936 */     return ((getHyperlinksEnabled() && (t
/* 1937 */       .isHyperlink() || (this.linkGeneratorResult != null && this.linkGeneratorResult
/*      */       
/* 1939 */       .getSourceOffset() == t.getOffset()))) || 
/* 1940 */       (this.syntaxScheme.getStyle(t.getType())).underline);
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
/*      */   public boolean getUseFocusableTips() {
/* 1954 */     return this.useFocusableTips;
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
/*      */   public boolean getUseSelectedTextColor() {
/* 1970 */     return this.useSelectedTextColor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void init() {
/* 1981 */     super.init();
/* 1982 */     this.metricsNeverRefreshed = true;
/*      */     
/* 1984 */     this.tokenPainter = new DefaultTokenPainter();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1992 */     if (toggleCurrentFoldAction == null) {
/* 1993 */       createRstaPopupMenuActions();
/*      */     }
/*      */ 
/*      */     
/* 1997 */     this.syntaxStyleKey = "text/plain";
/* 1998 */     setMatchedBracketBGColor(getDefaultBracketMatchBGColor());
/* 1999 */     setMatchedBracketBorderColor(getDefaultBracketMatchBorderColor());
/* 2000 */     setBracketMatchingEnabled(true);
/* 2001 */     setAnimateBracketMatching(true);
/* 2002 */     this.lastBracketMatchPos = -1;
/* 2003 */     setSelectionColor(getDefaultSelectionColor());
/* 2004 */     setTabLineColor((Color)null);
/* 2005 */     setMarkOccurrencesColor(MarkOccurrencesSupport.DEFAULT_COLOR);
/* 2006 */     setMarkOccurrencesDelay(1000);
/*      */     
/* 2008 */     this.foldManager = (FoldManager)new DefaultFoldManager(this);
/*      */ 
/*      */     
/* 2011 */     setAutoIndentEnabled(true);
/* 2012 */     setCloseCurlyBraces(true);
/* 2013 */     setCloseMarkupTags(true);
/* 2014 */     setClearWhitespaceLinesEnabled(true);
/*      */     
/* 2016 */     setHyperlinksEnabled(true);
/* 2017 */     setLinkScanningMask(128);
/* 2018 */     setHyperlinkForeground(Color.BLUE);
/* 2019 */     this.isScanningForLinks = false;
/* 2020 */     setUseFocusableTips(true);
/*      */ 
/*      */     
/* 2023 */     setDefaultAntiAliasingState();
/* 2024 */     restoreDefaultSyntaxScheme();
/*      */     
/* 2026 */     setHighlightSecondaryLanguages(true);
/* 2027 */     this.secondaryLanguageBackgrounds = new Color[3];
/* 2028 */     this.secondaryLanguageBackgrounds[0] = new Color(16773324);
/* 2029 */     this.secondaryLanguageBackgrounds[1] = new Color(14352090);
/* 2030 */     this.secondaryLanguageBackgrounds[2] = new Color(16769264);
/*      */     
/* 2032 */     setRightHandSideCorrection(0);
/* 2033 */     setShowMatchedBracketPopup(true);
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
/*      */   public boolean isAutoIndentEnabled() {
/* 2045 */     return this.autoIndentEnabled;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isBracketMatchingEnabled() {
/* 2056 */     return this.bracketMatchingEnabled;
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
/*      */   public boolean isClearWhitespaceLinesEnabled() {
/* 2069 */     return this.clearWhitespaceLines;
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
/*      */   public boolean isCodeFoldingEnabled() {
/* 2082 */     return this.foldManager.isCodeFoldingEnabled();
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
/*      */   public boolean isWhitespaceVisible() {
/* 2094 */     return this.whitespaceVisible;
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
/*      */   public Token modelToToken(int offs) {
/* 2107 */     if (offs >= 0) {
/*      */       try {
/* 2109 */         int line = getLineOfOffset(offs);
/* 2110 */         Token t = getTokenListForLine(line);
/* 2111 */         return RSyntaxUtilities.getTokenAtOffset(t, offs);
/* 2112 */       } catch (BadLocationException ble) {
/* 2113 */         ble.printStackTrace();
/*      */       } 
/*      */     }
/* 2116 */     return null;
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
/*      */   protected void paintComponent(Graphics g) {
/* 2133 */     if (this.metricsNeverRefreshed) {
/* 2134 */       refreshFontMetrics(getGraphics2D(getGraphics()));
/* 2135 */       this.metricsNeverRefreshed = false;
/*      */     } 
/*      */     
/* 2138 */     super.paintComponent(getGraphics2D(g));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void refreshFontMetrics(Graphics2D g2d) {
/* 2144 */     this.defaultFontMetrics = g2d.getFontMetrics(getFont());
/* 2145 */     this.syntaxScheme.refreshFontMetrics(g2d);
/* 2146 */     if (!getLineWrap()) {
/*      */ 
/*      */       
/* 2149 */       SyntaxView sv = (SyntaxView)getUI().getRootView((JTextComponent)this).getView(0);
/* 2150 */       sv.calculateLongestLine();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void redoLastAction() {
/* 2157 */     super.redoLastAction();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2165 */     ((RSyntaxTextAreaHighlighter)getHighlighter())
/* 2166 */       .clearMarkOccurrencesHighlights();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeActiveLineRangeListener(ActiveLineRangeListener l) {
/* 2177 */     this.listenerList.remove(ActiveLineRangeListener.class, l);
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
/*      */   public void removeHyperlinkListener(HyperlinkListener l) {
/* 2190 */     this.listenerList.remove(HyperlinkListener.class, l);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeNotify() {
/* 2199 */     if (this.parserManager != null) {
/* 2200 */       this.parserManager.stopParsing();
/*      */     }
/* 2202 */     super.removeNotify();
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
/*      */   public boolean removeParser(Parser parser) {
/* 2216 */     boolean removed = false;
/* 2217 */     if (this.parserManager != null) {
/* 2218 */       removed = this.parserManager.removeParser(parser);
/*      */     }
/* 2220 */     return removed;
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
/*      */   public void restoreDefaultSyntaxScheme() {
/* 2232 */     setSyntaxScheme(getDefaultSyntaxScheme());
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
/*      */   public static synchronized boolean saveTemplates() {
/* 2252 */     if (!getTemplatesEnabled()) {
/* 2253 */       return false;
/*      */     }
/* 2255 */     return getCodeTemplateManager().saveTemplates();
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
/*      */   public void setActiveLineRange(int min, int max) {
/* 2279 */     if (min == -1) {
/* 2280 */       max = -1;
/*      */     }
/* 2282 */     fireActiveLineRangeEvent(min, max);
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
/*      */   public void setAnimateBracketMatching(boolean animate) {
/* 2294 */     if (animate != this.animateBracketMatching) {
/* 2295 */       this.animateBracketMatching = animate;
/* 2296 */       if (animate && this.bracketRepaintTimer == null) {
/* 2297 */         this.bracketRepaintTimer = new BracketMatchingTimer();
/*      */       }
/* 2299 */       firePropertyChange("RSTA.animateBracketMatching", !animate, animate);
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
/*      */   public void setAntiAliasingEnabled(boolean enabled) {
/* 2314 */     boolean currentlyEnabled = (this.aaHints != null);
/*      */     
/* 2316 */     if (enabled != currentlyEnabled) {
/*      */       
/* 2318 */       if (enabled) {
/* 2319 */         this.aaHints = RSyntaxUtilities.getDesktopAntiAliasHints();
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2324 */         if (this.aaHints == null) {
/* 2325 */           Map<RenderingHints.Key, Object> temp = new HashMap<>();
/*      */           
/* 2327 */           temp.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/*      */           
/* 2329 */           this.aaHints = temp;
/*      */         } 
/*      */       } else {
/*      */         
/* 2333 */         this.aaHints = null;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2338 */       if (isDisplayable()) {
/* 2339 */         refreshFontMetrics(getGraphics2D(getGraphics()));
/*      */       }
/* 2341 */       firePropertyChange("RSTA.antiAlias", !enabled, enabled);
/* 2342 */       repaint();
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
/*      */   public void setAutoIndentEnabled(boolean enabled) {
/* 2357 */     if (this.autoIndentEnabled != enabled) {
/* 2358 */       this.autoIndentEnabled = enabled;
/* 2359 */       firePropertyChange("RSTA.autoIndent", !enabled, enabled);
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
/*      */   public void setBracketMatchingEnabled(boolean enabled) {
/* 2372 */     if (enabled != this.bracketMatchingEnabled) {
/* 2373 */       this.bracketMatchingEnabled = enabled;
/* 2374 */       repaint();
/* 2375 */       firePropertyChange("RSTA.bracketMatching", !enabled, enabled);
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
/*      */   public void setClearWhitespaceLinesEnabled(boolean enabled) {
/* 2390 */     if (enabled != this.clearWhitespaceLines) {
/* 2391 */       this.clearWhitespaceLines = enabled;
/* 2392 */       firePropertyChange("RSTA.clearWhitespaceLines", !enabled, enabled);
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
/*      */   public void setCloseCurlyBraces(boolean close) {
/* 2411 */     if (close != this.closeCurlyBraces) {
/* 2412 */       this.closeCurlyBraces = close;
/* 2413 */       firePropertyChange("RSTA.closeCurlyBraces", !close, close);
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
/*      */   public void setCloseMarkupTags(boolean close) {
/* 2431 */     if (close != this.closeMarkupTags) {
/* 2432 */       this.closeMarkupTags = close;
/* 2433 */       firePropertyChange("RSTA.closeMarkupTags", !close, close);
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
/*      */   public void setCodeFoldingEnabled(boolean enabled) {
/* 2449 */     if (enabled != this.foldManager.isCodeFoldingEnabled()) {
/* 2450 */       this.foldManager.setCodeFoldingEnabled(enabled);
/* 2451 */       firePropertyChange("RSTA.codeFolding", !enabled, enabled);
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
/*      */   private void setDefaultAntiAliasingState() {
/* 2464 */     this.aaHints = RSyntaxUtilities.getDesktopAntiAliasHints();
/* 2465 */     if (this.aaHints == null) {
/*      */       
/* 2467 */       Map<RenderingHints.Key, Object> temp = new HashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2472 */       JLabel label = new JLabel();
/* 2473 */       FontMetrics fm = label.getFontMetrics(label.getFont());
/* 2474 */       Object hint = null;
/*      */ 
/*      */       
/*      */       try {
/* 2478 */         Method m = FontMetrics.class.getMethod("getFontRenderContext", new Class[0]);
/* 2479 */         FontRenderContext frc = (FontRenderContext)m.invoke(fm, new Object[0]);
/* 2480 */         m = FontRenderContext.class.getMethod("getAntiAliasingHint", new Class[0]);
/* 2481 */         hint = m.invoke(frc, new Object[0]);
/* 2482 */       } catch (RuntimeException re) {
/* 2483 */         throw re;
/* 2484 */       } catch (Exception exception) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2493 */       if (hint == null) {
/* 2494 */         String os = System.getProperty("os.name").toLowerCase();
/* 2495 */         if (os.contains("windows")) {
/* 2496 */           hint = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
/*      */         } else {
/*      */           
/* 2499 */           hint = RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT;
/*      */         } 
/*      */       } 
/* 2502 */       temp.put(RenderingHints.KEY_TEXT_ANTIALIASING, hint);
/*      */       
/* 2504 */       this.aaHints = temp;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2510 */     if (isDisplayable()) {
/* 2511 */       refreshFontMetrics(getGraphics2D(getGraphics()));
/*      */     }
/* 2513 */     repaint();
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
/*      */   public void setDocument(Document document) {
/* 2529 */     if (!(document instanceof RSyntaxDocument)) {
/* 2530 */       throw new IllegalArgumentException("Documents for RSyntaxTextArea must be instances of RSyntaxDocument!");
/*      */     }
/*      */ 
/*      */     
/* 2534 */     if (this.markOccurrencesSupport != null) {
/* 2535 */       this.markOccurrencesSupport.clear();
/*      */     }
/* 2537 */     super.setDocument(document);
/* 2538 */     setSyntaxEditingStyle(((RSyntaxDocument)document).getSyntaxStyle());
/* 2539 */     if (this.markOccurrencesSupport != null) {
/* 2540 */       this.markOccurrencesSupport.doMarkOccurrences();
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
/*      */   public void setEOLMarkersVisible(boolean visible) {
/* 2554 */     if (visible != this.eolMarkersVisible) {
/* 2555 */       this.eolMarkersVisible = visible;
/* 2556 */       repaint();
/* 2557 */       firePropertyChange("RSTA.eolMarkersVisible", !visible, visible);
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
/*      */   public void setFont(Font font) {
/* 2574 */     Font old = getFont();
/* 2575 */     super.setFont(font);
/*      */ 
/*      */ 
/*      */     
/* 2579 */     SyntaxScheme scheme = getSyntaxScheme();
/* 2580 */     if (scheme != null && old != null) {
/* 2581 */       scheme.changeBaseFont(old, font);
/* 2582 */       calculateLineHeight();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 2587 */     if (isDisplayable()) {
/* 2588 */       refreshFontMetrics(getGraphics2D(getGraphics()));
/*      */       
/* 2590 */       updateMarginLineX();
/*      */ 
/*      */       
/* 2593 */       forceCurrentLineHighlightRepaint();
/*      */ 
/*      */       
/* 2596 */       firePropertyChange("font", old, font);
/*      */       
/* 2598 */       revalidate();
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
/*      */   public void setFractionalFontMetricsEnabled(boolean enabled) {
/* 2612 */     if (this.fractionalFontMetricsEnabled != enabled) {
/* 2613 */       this.fractionalFontMetricsEnabled = enabled;
/*      */ 
/*      */       
/* 2616 */       if (isDisplayable()) {
/* 2617 */         refreshFontMetrics(getGraphics2D(getGraphics()));
/*      */       }
/* 2619 */       firePropertyChange("RSTA.fractionalFontMetrics", !enabled, enabled);
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
/*      */   public void setHighlighter(Highlighter h) {
/*      */     RSyntaxTextAreaHighlighter rSyntaxTextAreaHighlighter;
/* 2638 */     if (h == null) {
/* 2639 */       rSyntaxTextAreaHighlighter = new RSyntaxTextAreaHighlighter();
/*      */     }
/*      */     
/* 2642 */     if (!(rSyntaxTextAreaHighlighter instanceof RSyntaxTextAreaHighlighter)) {
/* 2643 */       throw new IllegalArgumentException("RSyntaxTextArea requires an RSyntaxTextAreaHighlighter for its Highlighter");
/*      */     }
/*      */     
/* 2646 */     super.setHighlighter((Highlighter)rSyntaxTextAreaHighlighter);
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
/*      */   public void setHighlightSecondaryLanguages(boolean highlight) {
/* 2661 */     if (this.highlightSecondaryLanguages != highlight) {
/* 2662 */       this.highlightSecondaryLanguages = highlight;
/* 2663 */       repaint();
/* 2664 */       firePropertyChange("RSTA.highlightSecondaryLanguages", !highlight, highlight);
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
/*      */   public void setHyperlinkForeground(Color fg) {
/* 2679 */     if (fg == null) {
/* 2680 */       throw new NullPointerException("fg cannot be null");
/*      */     }
/* 2682 */     this.hyperlinkFG = fg;
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
/*      */   public void setHyperlinksEnabled(boolean enabled) {
/* 2697 */     if (this.hyperlinksEnabled != enabled) {
/* 2698 */       this.hyperlinksEnabled = enabled;
/* 2699 */       repaint();
/* 2700 */       firePropertyChange("RSTA.hyperlinksEnabled", !enabled, enabled);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setLinkGenerator(LinkGenerator generator) {
/* 2706 */     this.linkGenerator = generator;
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
/*      */   public void setLinkScanningMask(int mask) {
/* 2729 */     mask &= 0x3C0;
/*      */     
/* 2731 */     if (mask == 0) {
/* 2732 */       throw new IllegalArgumentException("mask argument should be some combination of InputEvent.*_DOWN_MASK fields");
/*      */     }
/*      */     
/* 2735 */     this.linkScanningMask = mask;
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
/*      */   public void setMarkOccurrences(boolean markOccurrences) {
/* 2748 */     if (markOccurrences) {
/* 2749 */       if (this.markOccurrencesSupport == null) {
/* 2750 */         this.markOccurrencesSupport = new MarkOccurrencesSupport();
/* 2751 */         this.markOccurrencesSupport.install(this);
/* 2752 */         firePropertyChange("RSTA.markOccurrences", false, true);
/*      */       }
/*      */     
/*      */     }
/* 2756 */     else if (this.markOccurrencesSupport != null) {
/* 2757 */       this.markOccurrencesSupport.uninstall();
/* 2758 */       this.markOccurrencesSupport = null;
/* 2759 */       firePropertyChange("RSTA.markOccurrences", true, false);
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
/*      */   public void setMarkOccurrencesColor(Color color) {
/* 2773 */     this.markOccurrencesColor = color;
/* 2774 */     if (this.markOccurrencesSupport != null) {
/* 2775 */       this.markOccurrencesSupport.setColor(color);
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
/*      */   public void setMarkOccurrencesDelay(int delay) {
/* 2789 */     if (delay <= 0) {
/* 2790 */       throw new IllegalArgumentException("Delay must be > 0");
/*      */     }
/* 2792 */     if (delay != this.markOccurrencesDelay) {
/* 2793 */       this.markOccurrencesDelay = delay;
/* 2794 */       if (this.markOccurrencesSupport != null) {
/* 2795 */         this.markOccurrencesSupport.setDelay(delay);
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
/*      */   public void setMatchedBracketBGColor(Color color) {
/* 2811 */     this.matchedBracketBGColor = color;
/* 2812 */     if (this.match != null) {
/* 2813 */       repaint();
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
/*      */   public void setMatchedBracketBorderColor(Color color) {
/* 2826 */     this.matchedBracketBorderColor = color;
/* 2827 */     if (this.match != null) {
/* 2828 */       repaint();
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
/*      */   public void setPaintMarkOccurrencesBorder(boolean paintBorder) {
/* 2842 */     this.paintMarkOccurrencesBorder = paintBorder;
/* 2843 */     if (this.markOccurrencesSupport != null) {
/* 2844 */       this.markOccurrencesSupport.setPaintBorder(paintBorder);
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
/*      */   public void setPaintMatchedBracketPair(boolean paintPair) {
/* 2864 */     if (paintPair != this.paintMatchedBracketPair) {
/* 2865 */       this.paintMatchedBracketPair = paintPair;
/* 2866 */       doBracketMatching();
/* 2867 */       repaint();
/* 2868 */       firePropertyChange("RSTA.paintMatchedBracketPair", !this.paintMatchedBracketPair, this.paintMatchedBracketPair);
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
/*      */   public void setPaintTabLines(boolean paint) {
/* 2883 */     if (paint != this.paintTabLines) {
/* 2884 */       this.paintTabLines = paint;
/* 2885 */       repaint();
/* 2886 */       firePropertyChange("RSTA.tabLines", !paint, paint);
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
/*      */   public void setParserDelay(int millis) {
/* 2900 */     if (this.parserManager == null) {
/* 2901 */       this.parserManager = new ParserManager(this);
/*      */     }
/* 2903 */     this.parserManager.setDelay(millis);
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
/*      */   public void setRightHandSideCorrection(int rhsCorrection) {
/* 2918 */     if (rhsCorrection < 0) {
/* 2919 */       throw new IllegalArgumentException("correction should be > 0");
/*      */     }
/* 2921 */     if (rhsCorrection != this.rhsCorrection) {
/* 2922 */       this.rhsCorrection = rhsCorrection;
/* 2923 */       revalidate();
/* 2924 */       repaint();
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
/*      */   public void setSecondaryLanguageBackground(int index, Color color) {
/* 2940 */     index--;
/* 2941 */     Color old = this.secondaryLanguageBackgrounds[index];
/* 2942 */     if ((color == null && old != null) || (color != null && !color.equals(old))) {
/* 2943 */       this.secondaryLanguageBackgrounds[index] = color;
/* 2944 */       if (getHighlightSecondaryLanguages()) {
/* 2945 */         repaint();
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
/*      */   public void setShowMatchedBracketPopup(boolean show) {
/* 2960 */     this.showMatchedBracketPopup = show;
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
/*      */   public void setSyntaxEditingStyle(String styleKey) {
/* 2975 */     if (styleKey == null) {
/* 2976 */       styleKey = "text/plain";
/*      */     }
/* 2978 */     if (!styleKey.equals(this.syntaxStyleKey)) {
/* 2979 */       String oldStyle = this.syntaxStyleKey;
/* 2980 */       this.syntaxStyleKey = styleKey;
/* 2981 */       ((RSyntaxDocument)getDocument()).setSyntaxStyle(styleKey);
/* 2982 */       firePropertyChange("RSTA.syntaxStyle", oldStyle, styleKey);
/* 2983 */       setActiveLineRange(-1, -1);
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
/*      */   public void setSyntaxScheme(SyntaxScheme scheme) {
/* 3008 */     SyntaxScheme old = this.syntaxScheme;
/* 3009 */     this.syntaxScheme = scheme;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3014 */     calculateLineHeight();
/*      */     
/* 3016 */     if (isDisplayable()) {
/* 3017 */       refreshFontMetrics(getGraphics2D(getGraphics()));
/*      */     }
/*      */ 
/*      */     
/* 3021 */     updateMarginLineX();
/* 3022 */     this.lastBracketMatchPos = -1;
/* 3023 */     doBracketMatching();
/*      */ 
/*      */ 
/*      */     
/* 3027 */     forceCurrentLineHighlightRepaint();
/*      */ 
/*      */     
/* 3030 */     revalidate();
/*      */     
/* 3032 */     firePropertyChange("RSTA.syntaxScheme", old, this.syntaxScheme);
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
/*      */   public static synchronized boolean setTemplateDirectory(String dir) {
/* 3053 */     if (getTemplatesEnabled() && dir != null) {
/* 3054 */       File directory = new File(dir);
/* 3055 */       if (directory.isDirectory()) {
/* 3056 */         return 
/* 3057 */           (getCodeTemplateManager().setTemplateDirectory(directory) > -1);
/*      */       }
/* 3059 */       boolean created = directory.mkdir();
/* 3060 */       if (created) {
/* 3061 */         return 
/* 3062 */           (getCodeTemplateManager().setTemplateDirectory(directory) > -1);
/*      */       }
/*      */     } 
/* 3065 */     return false;
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static synchronized void setTemplatesEnabled(boolean enabled) {
/* 3099 */     templatesEnabled = enabled;
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
/*      */   public void setTabLineColor(Color c) {
/* 3115 */     if (c == null) {
/* 3116 */       c = Color.gray;
/*      */     }
/*      */     
/* 3119 */     if (!c.equals(this.tabLineColor)) {
/* 3120 */       Color old = this.tabLineColor;
/* 3121 */       this.tabLineColor = c;
/* 3122 */       if (getPaintTabLines()) {
/* 3123 */         repaint();
/*      */       }
/* 3125 */       firePropertyChange("RSTA.tabLineColor", old, this.tabLineColor);
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
/*      */   public void setUseFocusableTips(boolean use) {
/* 3142 */     if (use != this.useFocusableTips) {
/* 3143 */       this.useFocusableTips = use;
/* 3144 */       firePropertyChange("RSTA.focusableTips", !use, use);
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
/*      */   public void setUseSelectedTextColor(boolean use) {
/* 3162 */     if (use != this.useSelectedTextColor) {
/* 3163 */       this.useSelectedTextColor = use;
/* 3164 */       firePropertyChange("RSTA.useSelectedTextColor", !use, use);
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
/*      */   public void setWhitespaceVisible(boolean visible) {
/* 3177 */     if (this.whitespaceVisible != visible) {
/* 3178 */       this.whitespaceVisible = visible;
/* 3179 */       this.tokenPainter = visible ? new VisibleWhitespaceTokenPainter() : new DefaultTokenPainter();
/*      */       
/* 3181 */       repaint();
/* 3182 */       firePropertyChange("RSTA.visibleWhitespace", !visible, visible);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void stopScanningForLinks() {
/* 3193 */     if (this.isScanningForLinks) {
/*      */       
/* 3195 */       this.isScanningForLinks = false;
/* 3196 */       this.linkGeneratorResult = null;
/* 3197 */       this.hoveredOverLinkOffset = -1;
/*      */       
/* 3199 */       Cursor c = getCursor();
/* 3200 */       if (c != null && c.getType() == 12) {
/* 3201 */         fireHyperlinkUpdate(HyperlinkEvent.EventType.EXITED);
/* 3202 */         setCursor(Cursor.getPredefinedCursor(2));
/* 3203 */         repaint();
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void undoLastAction() {
/* 3211 */     super.undoLastAction();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3219 */     ((RSyntaxTextAreaHighlighter)getHighlighter())
/* 3220 */       .clearMarkOccurrencesHighlights();
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
/*      */   public Token viewToToken(Point p) {
/* 3239 */     return modelToToken(viewToModel(p));
/*      */   }
/*      */ 
/*      */   
/*      */   private final class MatchedBracketPopupTimer
/*      */     extends Timer
/*      */     implements ActionListener, CaretListener
/*      */   {
/*      */     private MatchedBracketPopup popup;
/*      */     
/*      */     private int origDot;
/*      */     
/*      */     private int matchedBracketOffs;
/*      */     
/*      */     private MatchedBracketPopupTimer() {
/* 3254 */       super(350, null);
/* 3255 */       addActionListener(this);
/* 3256 */       setRepeats(false);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformed(ActionEvent e) {
/* 3262 */       if (this.popup != null) {
/* 3263 */         this.popup.dispose();
/*      */       }
/*      */       
/* 3266 */       Window window = SwingUtilities.getWindowAncestor((Component)RSyntaxTextArea.this);
/* 3267 */       this.popup = new MatchedBracketPopup(window, RSyntaxTextArea.this, this.matchedBracketOffs);
/* 3268 */       this.popup.pack();
/* 3269 */       this.popup.setVisible(true);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void caretUpdate(CaretEvent e) {
/* 3275 */       int dot = e.getDot();
/* 3276 */       if (dot != this.origDot) {
/* 3277 */         stop();
/* 3278 */         RSyntaxTextArea.this.removeCaretListener(this);
/* 3279 */         if (this.popup != null) {
/* 3280 */           this.popup.dispose();
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void restart(int matchedBracketOffs) {
/* 3291 */       this.origDot = RSyntaxTextArea.this.getCaretPosition();
/* 3292 */       this.matchedBracketOffs = matchedBracketOffs;
/* 3293 */       restart();
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 3298 */       super.start();
/* 3299 */       RSyntaxTextArea.this.addCaretListener(this);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class BracketMatchingTimer
/*      */     extends Timer
/*      */     implements ActionListener
/*      */   {
/*      */     private int pulseCount;
/*      */ 
/*      */     
/*      */     BracketMatchingTimer() {
/* 3313 */       super(20, null);
/* 3314 */       addActionListener(this);
/* 3315 */       setCoalesce(false);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformed(ActionEvent e) {
/* 3320 */       if (RSyntaxTextArea.this.isBracketMatchingEnabled()) {
/* 3321 */         if (RSyntaxTextArea.this.match != null) {
/* 3322 */           updateAndInvalidate(RSyntaxTextArea.this.match);
/*      */         }
/* 3324 */         if (RSyntaxTextArea.this.dotRect != null && RSyntaxTextArea.this.getPaintMatchedBracketPair()) {
/* 3325 */           updateAndInvalidate(RSyntaxTextArea.this.dotRect);
/*      */         }
/* 3327 */         if (++this.pulseCount == 8) {
/* 3328 */           this.pulseCount = 0;
/* 3329 */           stop();
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     private void init(Rectangle r) {
/* 3335 */       r.x += 3;
/* 3336 */       r.y += 3;
/* 3337 */       r.width -= 6;
/* 3338 */       r.height -= 6;
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 3343 */       init(RSyntaxTextArea.this.match);
/* 3344 */       if (RSyntaxTextArea.this.dotRect != null && RSyntaxTextArea.this.getPaintMatchedBracketPair()) {
/* 3345 */         init(RSyntaxTextArea.this.dotRect);
/*      */       }
/* 3347 */       this.pulseCount = 0;
/* 3348 */       super.start();
/*      */     }
/*      */     
/*      */     private void updateAndInvalidate(Rectangle r) {
/* 3352 */       if (this.pulseCount < 5) {
/* 3353 */         r.x--;
/* 3354 */         r.y--;
/* 3355 */         r.width += 2;
/* 3356 */         r.height += 2;
/* 3357 */         RSyntaxTextArea.this.repaint(r.x, r.y, r.width, r.height);
/*      */       }
/* 3359 */       else if (this.pulseCount < 7) {
/* 3360 */         r.x++;
/* 3361 */         r.y++;
/* 3362 */         r.width -= 2;
/* 3363 */         r.height -= 2;
/* 3364 */         RSyntaxTextArea.this.repaint(r.x - 2, r.y - 2, r.width + 5, r.height + 5);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class RSyntaxTextAreaMutableCaretEvent
/*      */     extends RTextArea.RTextAreaMutableCaretEvent
/*      */   {
/*      */     private Insets insets;
/*      */ 
/*      */ 
/*      */     
/*      */     protected RSyntaxTextAreaMutableCaretEvent(RTextArea textArea) {
/* 3380 */       super(RSyntaxTextArea.this, textArea);
/* 3381 */       this.insets = new Insets(0, 0, 0, 0);
/*      */     }
/*      */ 
/*      */     
/*      */     private boolean equal(LinkGeneratorResult e1, LinkGeneratorResult e2) {
/* 3386 */       return (e1.getSourceOffset() == e2.getSourceOffset());
/*      */     }
/*      */ 
/*      */     
/*      */     public void mouseClicked(MouseEvent e) {
/* 3391 */       if (RSyntaxTextArea.this.getHyperlinksEnabled() && RSyntaxTextArea.this.isScanningForLinks && RSyntaxTextArea.this
/* 3392 */         .hoveredOverLinkOffset > -1) {
/* 3393 */         RSyntaxTextArea.this.fireHyperlinkUpdate(HyperlinkEvent.EventType.ACTIVATED);
/* 3394 */         RSyntaxTextArea.this.stopScanningForLinks();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void mouseMoved(MouseEvent e) {
/* 3401 */       super.mouseMoved(e);
/*      */       
/* 3403 */       if (!RSyntaxTextArea.this.getHyperlinksEnabled()) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/* 3408 */       if ((e.getModifiersEx() & RSyntaxTextArea.this.linkScanningMask) == RSyntaxTextArea.this.linkScanningMask) {
/*      */         Cursor c2;
/*      */ 
/*      */         
/* 3412 */         this.insets = RSyntaxTextArea.this.getInsets(this.insets);
/* 3413 */         if (this.insets != null) {
/* 3414 */           int x = e.getX();
/* 3415 */           int y = e.getY();
/* 3416 */           if (x <= this.insets.left || y < this.insets.top) {
/* 3417 */             if (RSyntaxTextArea.this.isScanningForLinks) {
/* 3418 */               RSyntaxTextArea.this.stopScanningForLinks();
/*      */             }
/*      */             
/*      */             return;
/*      */           } 
/*      */         } 
/* 3424 */         RSyntaxTextArea.this.isScanningForLinks = true;
/* 3425 */         Token t = RSyntaxTextArea.this.viewToToken(e.getPoint());
/* 3426 */         if (t != null)
/*      */         {
/* 3428 */           t = new TokenImpl(t);
/*      */         }
/*      */         
/* 3431 */         if (t != null && t.isHyperlink()) {
/* 3432 */           if (RSyntaxTextArea.this.hoveredOverLinkOffset == -1 || RSyntaxTextArea.this
/* 3433 */             .hoveredOverLinkOffset != t.getOffset()) {
/* 3434 */             RSyntaxTextArea.this.hoveredOverLinkOffset = t.getOffset();
/* 3435 */             RSyntaxTextArea.this.repaint();
/*      */           } 
/* 3437 */           c2 = Cursor.getPredefinedCursor(12);
/*      */         }
/* 3439 */         else if (t != null && RSyntaxTextArea.this.linkGenerator != null) {
/* 3440 */           int offs = RSyntaxTextArea.this.viewToModel(e.getPoint());
/*      */           
/* 3442 */           LinkGeneratorResult newResult = RSyntaxTextArea.this.linkGenerator.isLinkAtOffset(RSyntaxTextArea.this, offs);
/* 3443 */           if (newResult != null) {
/*      */             
/* 3445 */             if (RSyntaxTextArea.this.linkGeneratorResult == null || 
/* 3446 */               !equal(newResult, RSyntaxTextArea.this.linkGeneratorResult)) {
/* 3447 */               RSyntaxTextArea.this.repaint();
/*      */             }
/* 3449 */             RSyntaxTextArea.this.linkGeneratorResult = newResult;
/* 3450 */             RSyntaxTextArea.this.hoveredOverLinkOffset = t.getOffset();
/* 3451 */             c2 = Cursor.getPredefinedCursor(12);
/*      */           }
/*      */           else {
/*      */             
/* 3455 */             if (RSyntaxTextArea.this.linkGeneratorResult != null) {
/* 3456 */               RSyntaxTextArea.this.repaint();
/*      */             }
/* 3458 */             c2 = Cursor.getPredefinedCursor(2);
/* 3459 */             RSyntaxTextArea.this.hoveredOverLinkOffset = -1;
/* 3460 */             RSyntaxTextArea.this.linkGeneratorResult = null;
/*      */           } 
/*      */         } else {
/*      */           
/* 3464 */           c2 = Cursor.getPredefinedCursor(2);
/* 3465 */           RSyntaxTextArea.this.hoveredOverLinkOffset = -1;
/* 3466 */           if (RSyntaxTextArea.this.linkGeneratorResult != null) {
/* 3467 */             RSyntaxTextArea.this.linkGeneratorResult = null;
/*      */           }
/*      */         } 
/* 3470 */         if (RSyntaxTextArea.this.getCursor() != c2)
/*      */         {
/* 3472 */           RSyntaxTextArea.this.setCursor(c2);
/*      */           
/* 3474 */           RSyntaxTextArea.this.repaint();
/*      */ 
/*      */ 
/*      */           
/* 3478 */           RSyntaxTextArea.this.fireHyperlinkUpdate((c2 == Cursor.getPredefinedCursor(12)) ? HyperlinkEvent.EventType.ENTERED : HyperlinkEvent.EventType.EXITED);
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 3483 */       else if (RSyntaxTextArea.this.isScanningForLinks) {
/* 3484 */         RSyntaxTextArea.this.stopScanningForLinks();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setActionForKeyStroke(KeyStroke keyStroke, Action action) {
/* 3493 */     getActionMap().put(keyStroke.toString(), action);
/* 3494 */     getInputMap().put(keyStroke, keyStroke.toString());
/*      */   }
/*      */   public void setActionForKey(String keyString, Action action) {
/* 3497 */     setActionForKeyStroke(KeyStroke.getKeyStroke(keyString), action);
/*      */   }
/*      */   public void registerReplaceDialog() {
/* 3500 */     final RSyntaxTextArea textArea = this;
/* 3501 */     setActionForKey("ctrl pressed F", new AbstractAction() {
/* 3502 */           ReplaceDialog replaceDialog = createReplaceDialog();
/*      */           
/*      */           public void actionPerformed(ActionEvent e) {
/* 3505 */             this.replaceDialog.setVisible(true);
/*      */           }
/*      */           public ReplaceDialog createReplaceDialog() {
/* 3508 */             Frame frame = RSyntaxTextArea.this.getParentFrame();
/* 3509 */             SearchListenerImpl searchListenerImpl = new SearchListenerImpl(textArea);
/* 3510 */             if (frame != null) {
/* 3511 */               return new ReplaceDialog(frame, (SearchListener)searchListenerImpl);
/*      */             }
/* 3513 */             Dialog dialog = RSyntaxTextArea.this.getParentDialog();
/* 3514 */             if (dialog != null) {
/* 3515 */               return new ReplaceDialog(dialog, (SearchListener)searchListenerImpl);
/*      */             }
/* 3517 */             return new ReplaceDialog(frame, (SearchListener)searchListenerImpl);
/*      */           }
/*      */         });
/*      */   }
/*      */   public void registerGoToDialog() {
/* 3522 */     setActionForKey("ctrl pressed G", new AbstractAction() {
/* 3523 */           RSyntaxTextArea textArea = RSyntaxTextArea.this;
/*      */           
/*      */           public void actionPerformed(ActionEvent e) {
/* 3526 */             GoToDialog dialog = new GoToDialog(RSyntaxTextArea.this.getParentFrame());
/* 3527 */             dialog.setMaxLineNumberAllowed(this.textArea.getLineCount());
/* 3528 */             dialog.setVisible(true);
/* 3529 */             int line = dialog.getLineNumber();
/* 3530 */             if (line > 0)
/*      */               try {
/* 3532 */                 this.textArea.setCaretPosition(this.textArea.getLineStartOffset(line - 1));
/* 3533 */               } catch (BadLocationException ble) {
/* 3534 */                 UIManager.getLookAndFeel().provideErrorFeedback((Component)this.textArea);
/* 3535 */                 ble.printStackTrace();
/*      */               }  
/*      */           }
/*      */         });
/*      */   }
/*      */   
/*      */   public Frame getParentFrame() {
/* 3542 */     RSyntaxTextArea rSyntaxTextArea = this; Container container;
/* 3543 */     while ((container = rSyntaxTextArea.getParent()) != null) {
/* 3544 */       if (Frame.class.isAssignableFrom(container.getClass())) {
/* 3545 */         return (Frame)container;
/*      */       }
/*      */     } 
/* 3548 */     return null;
/*      */   }
/*      */   public Dialog getParentDialog() {
/* 3551 */     RSyntaxTextArea rSyntaxTextArea = this; Container container;
/* 3552 */     while ((container = rSyntaxTextArea.getParent()) != null) {
/* 3553 */       if (Dialog.class.isAssignableFrom(container.getClass())) {
/* 3554 */         return (Dialog)container;
/*      */       }
/*      */     } 
/* 3557 */     return null;
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\RSyntaxTextArea.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */