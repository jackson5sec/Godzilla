/*      */ package org.fife.ui.rsyntaxtextarea;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.Font;
/*      */ import java.awt.Point;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Stack;
/*      */ import javax.swing.Action;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.KeyStroke;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import javax.swing.text.Caret;
/*      */ import javax.swing.text.Document;
/*      */ import javax.swing.text.Element;
/*      */ import javax.swing.text.Segment;
/*      */ import javax.swing.text.TextAction;
/*      */ import org.fife.ui.rsyntaxtextarea.folding.Fold;
/*      */ import org.fife.ui.rsyntaxtextarea.folding.FoldCollapser;
/*      */ import org.fife.ui.rsyntaxtextarea.folding.FoldManager;
/*      */ import org.fife.ui.rsyntaxtextarea.templates.CodeTemplate;
/*      */ import org.fife.ui.rtextarea.IconRowHeader;
/*      */ import org.fife.ui.rtextarea.RTextArea;
/*      */ import org.fife.ui.rtextarea.RTextAreaEditorKit;
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
/*      */ public class RSyntaxTextAreaEditorKit
/*      */   extends RTextAreaEditorKit
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*      */   public static final String rstaCloseCurlyBraceAction = "RSTA.CloseCurlyBraceAction";
/*      */   public static final String rstaCloseMarkupTagAction = "RSTA.CloseMarkupTagAction";
/*      */   public static final String rstaCollapseAllFoldsAction = "RSTA.CollapseAllFoldsAction";
/*      */   public static final String rstaCollapseAllCommentFoldsAction = "RSTA.CollapseAllCommentFoldsAction";
/*      */   public static final String rstaCollapseFoldAction = "RSTA.CollapseFoldAction";
/*      */   public static final String rstaCopyAsStyledTextAction = "RSTA.CopyAsStyledTextAction";
/*      */   public static final String rstaDecreaseIndentAction = "RSTA.DecreaseIndentAction";
/*      */   public static final String rstaExpandAllFoldsAction = "RSTA.ExpandAllFoldsAction";
/*      */   public static final String rstaExpandFoldAction = "RSTA.ExpandFoldAction";
/*      */   public static final String rstaGoToMatchingBracketAction = "RSTA.GoToMatchingBracketAction";
/*      */   public static final String rstaPossiblyInsertTemplateAction = "RSTA.TemplateAction";
/*      */   public static final String rstaToggleCommentAction = "RSTA.ToggleCommentAction";
/*      */   public static final String rstaToggleCurrentFoldAction = "RSTA.ToggleCurrentFoldAction";
/*      */   private static final String MSG = "org.fife.ui.rsyntaxtextarea.RSyntaxTextArea";
/*   89 */   private static final ResourceBundle msg = ResourceBundle.getBundle("org.fife.ui.rsyntaxtextarea.RSyntaxTextArea");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   96 */   private static final Action[] defaultActions = new Action[] { (Action)new CloseCurlyBraceAction(), (Action)new CloseMarkupTagAction(), (Action)new BeginWordAction("caret-begin-word", false), (Action)new BeginWordAction("selection-begin-word", true), (Action)new ChangeFoldStateAction("RSTA.CollapseFoldAction", true), (Action)new ChangeFoldStateAction("RSTA.ExpandFoldAction", false), (Action)new CollapseAllFoldsAction(), (Action)new CopyAsStyledTextAction(), (Action)new DecreaseIndentAction(), (Action)new DeletePrevWordAction(), (Action)new DumbCompleteWordAction(), (Action)new RTextAreaEditorKit.EndAction("caret-end", false), (Action)new RTextAreaEditorKit.EndAction("selection-end", true), (Action)new EndWordAction("caret-end-word", false), (Action)new EndWordAction("caret-end-word", true), (Action)new ExpandAllFoldsAction(), (Action)new GoToMatchingBracketAction(), (Action)new InsertBreakAction(), (Action)new InsertTabAction(), (Action)new NextWordAction("caret-next-word", false), (Action)new NextWordAction("selection-next-word", true), (Action)new PossiblyInsertTemplateAction(), (Action)new PreviousWordAction("caret-previous-word", false), (Action)new PreviousWordAction("selection-previous-word", true), (Action)new SelectWordAction(), (Action)new ToggleCommentAction() };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Document createDefaultDocument() {
/*  142 */     return (Document)new RSyntaxDocument("text/plain");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IconRowHeader createIconRowHeader(RTextArea textArea) {
/*  154 */     return new FoldingAwareIconRowHeader((RSyntaxTextArea)textArea);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Action[] getActions() {
/*  167 */     return TextAction.augmentList(super.getActions(), defaultActions);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getString(String key) {
/*  180 */     return msg.getString(key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static class BeginWordAction
/*      */     extends RTextAreaEditorKit.BeginWordAction
/*      */   {
/*      */     private Segment seg;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected BeginWordAction(String name, boolean select) {
/*  195 */       super(name, select);
/*  196 */       this.seg = new Segment();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected int getWordStart(RTextArea textArea, int offs) throws BadLocationException {
/*  203 */       if (offs == 0) {
/*  204 */         return offs;
/*      */       }
/*      */       
/*  207 */       RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
/*  208 */       int line = textArea.getLineOfOffset(offs);
/*  209 */       int start = textArea.getLineStartOffset(line);
/*  210 */       if (offs == start) {
/*  211 */         return start;
/*      */       }
/*  213 */       int end = textArea.getLineEndOffset(line);
/*  214 */       if (line != textArea.getLineCount() - 1) {
/*  215 */         end--;
/*      */       }
/*  217 */       doc.getText(start, end - start, this.seg);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  223 */       int firstIndex = this.seg.getBeginIndex() + offs - start - 1;
/*  224 */       this.seg.setIndex(firstIndex);
/*  225 */       char ch = this.seg.current();
/*  226 */       char nextCh = (offs == end) ? Character.MIN_VALUE : this.seg.array[this.seg.getIndex() + 1];
/*      */ 
/*      */       
/*  229 */       int languageIndex = 0;
/*  230 */       if (doc.isIdentifierChar(languageIndex, ch)) {
/*  231 */         if (offs != end && !doc.isIdentifierChar(languageIndex, nextCh)) {
/*  232 */           return offs;
/*      */         }
/*      */         do {
/*  235 */           ch = this.seg.previous();
/*  236 */         } while (doc.isIdentifierChar(languageIndex, ch) && ch != Character.MAX_VALUE);
/*      */ 
/*      */       
/*      */       }
/*  240 */       else if (Character.isWhitespace(ch)) {
/*  241 */         if (offs != end && !Character.isWhitespace(nextCh)) {
/*  242 */           return offs;
/*      */         }
/*      */         do {
/*  245 */           ch = this.seg.previous();
/*  246 */         } while (Character.isWhitespace(ch));
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  252 */       offs -= firstIndex - this.seg.getIndex() + 1;
/*  253 */       if (ch != Character.MAX_VALUE && nextCh != '\n') {
/*  254 */         offs++;
/*      */       }
/*      */       
/*  257 */       return offs;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class ChangeFoldStateAction
/*      */     extends FoldRelatedAction
/*      */   {
/*      */     private boolean collapse;
/*      */ 
/*      */ 
/*      */     
/*      */     public ChangeFoldStateAction(String name, boolean collapse) {
/*  272 */       super(name);
/*  273 */       this.collapse = collapse;
/*      */     }
/*      */ 
/*      */     
/*      */     public ChangeFoldStateAction(String name, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/*  278 */       super(name, icon, desc, mnemonic, accelerator);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/*  283 */       RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
/*  284 */       if (rsta.isCodeFoldingEnabled()) {
/*  285 */         Fold fold = getClosestFold(rsta);
/*  286 */         if (fold != null) {
/*  287 */           fold.setCollapsed(this.collapse);
/*      */         }
/*  289 */         RSyntaxUtilities.possiblyRepaintGutter(textArea);
/*      */       } else {
/*      */         
/*  292 */         UIManager.getLookAndFeel().provideErrorFeedback((Component)rsta);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/*  298 */       return getName();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class CloseCurlyBraceAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */     
/*      */     private Point bracketInfo;
/*      */     
/*      */     private Segment seg;
/*      */ 
/*      */     
/*      */     public CloseCurlyBraceAction() {
/*  316 */       super("RSTA.CloseCurlyBraceAction");
/*  317 */       this.seg = new Segment();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/*  323 */       RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
/*  324 */       RSyntaxDocument doc = (RSyntaxDocument)rsta.getDocument();
/*      */       
/*  326 */       int languageIndex = 0;
/*  327 */       int dot = textArea.getCaretPosition();
/*  328 */       if (dot > 0) {
/*  329 */         Token t = RSyntaxUtilities.getTokenAtOffset(rsta, dot - 1);
/*  330 */         languageIndex = (t == null) ? 0 : t.getLanguageIndex();
/*      */       } 
/*      */       
/*  333 */       boolean alignCurlyBraces = (rsta.isAutoIndentEnabled() && doc.getCurlyBracesDenoteCodeBlocks(languageIndex));
/*      */       
/*  335 */       if (alignCurlyBraces) {
/*  336 */         textArea.beginAtomicEdit();
/*      */       }
/*      */ 
/*      */       
/*      */       try {
/*  341 */         textArea.replaceSelection("}");
/*      */ 
/*      */         
/*  344 */         if (alignCurlyBraces) {
/*      */           
/*  346 */           Element root = doc.getDefaultRootElement();
/*  347 */           dot = rsta.getCaretPosition() - 1;
/*  348 */           int line = root.getElementIndex(dot);
/*  349 */           Element elem = root.getElement(line);
/*  350 */           int start = elem.getStartOffset();
/*      */ 
/*      */           
/*      */           try {
/*  354 */             doc.getText(start, dot - start, this.seg);
/*  355 */           } catch (BadLocationException ble) {
/*  356 */             ble.printStackTrace();
/*      */ 
/*      */             
/*      */             return;
/*      */           } 
/*      */           
/*  362 */           for (int i = 0; i < this.seg.count; i++) {
/*  363 */             char ch = this.seg.array[this.seg.offset + i];
/*  364 */             if (!Character.isWhitespace(ch)) {
/*      */               return;
/*      */             }
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/*  371 */           this.bracketInfo = RSyntaxUtilities.getMatchingBracketPosition(rsta, this.bracketInfo);
/*      */           
/*  373 */           if (this.bracketInfo.y > -1) {
/*      */             try {
/*  375 */               String ws = RSyntaxUtilities.getLeadingWhitespace((Document)doc, this.bracketInfo.y);
/*      */               
/*  377 */               rsta.replaceRange(ws, start, dot);
/*  378 */             } catch (BadLocationException ble) {
/*  379 */               ble.printStackTrace();
/*      */ 
/*      */               
/*      */               return;
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } finally {
/*  387 */         if (alignCurlyBraces) {
/*  388 */           textArea.endAtomicEdit();
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/*  396 */       return "RSTA.CloseCurlyBraceAction";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class CloseMarkupTagAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */     
/*      */     public CloseMarkupTagAction() {
/*  410 */       super("RSTA.CloseMarkupTagAction");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/*  416 */       if (!textArea.isEditable() || !textArea.isEnabled()) {
/*  417 */         UIManager.getLookAndFeel().provideErrorFeedback((Component)textArea);
/*      */         
/*      */         return;
/*      */       } 
/*  421 */       RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
/*  422 */       RSyntaxDocument doc = (RSyntaxDocument)rsta.getDocument();
/*      */       
/*  424 */       Caret c = rsta.getCaret();
/*  425 */       boolean selection = (c.getDot() != c.getMark());
/*  426 */       rsta.replaceSelection("/");
/*      */ 
/*      */       
/*  429 */       int dot = c.getDot();
/*      */       
/*  431 */       if (doc.getLanguageIsMarkup() && doc
/*  432 */         .getCompleteMarkupCloseTags() && !selection && rsta
/*  433 */         .getCloseMarkupTags() && dot > 1) {
/*      */         
/*      */         try {
/*      */ 
/*      */           
/*  438 */           char ch = doc.charAt(dot - 2);
/*  439 */           if (ch == '<' || ch == '[')
/*      */           {
/*  441 */             Token t = doc.getTokenListForLine(rsta
/*  442 */                 .getCaretLineNumber());
/*  443 */             t = RSyntaxUtilities.getTokenAtOffset(t, dot - 1);
/*  444 */             if (t != null && t.getType() == 25)
/*      */             {
/*  446 */               String tagName = discoverTagName(doc, dot);
/*  447 */               if (tagName != null) {
/*  448 */                 rsta.replaceSelection(tagName + (char)(ch + 2));
/*      */               }
/*      */             }
/*      */           
/*      */           }
/*      */         
/*  454 */         } catch (BadLocationException ble) {
/*  455 */           UIManager.getLookAndFeel().provideErrorFeedback((Component)rsta);
/*  456 */           ble.printStackTrace();
/*      */         } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private String discoverTagName(RSyntaxDocument doc, int dot) {
/*  476 */       Stack<String> stack = new Stack<>();
/*      */       
/*  478 */       Element root = doc.getDefaultRootElement();
/*  479 */       int curLine = root.getElementIndex(dot);
/*      */       
/*  481 */       for (int i = 0; i <= curLine; i++) {
/*      */         
/*  483 */         Token t = doc.getTokenListForLine(i);
/*  484 */         while (t != null && t.isPaintable()) {
/*      */           
/*  486 */           if (t.getType() == 25) {
/*  487 */             if (t.isSingleChar('<') || t.isSingleChar('[')) {
/*  488 */               t = t.getNextToken();
/*  489 */               while (t != null && t.isPaintable()) {
/*  490 */                 if (t.getType() == 26 || t
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/*  495 */                   .getType() == 27) {
/*  496 */                   stack.push(t.getLexeme());
/*      */                   break;
/*      */                 } 
/*  499 */                 t = t.getNextToken();
/*      */               }
/*      */             
/*  502 */             } else if (t.length() == 2 && t.charAt(0) == '/' && (t
/*  503 */               .charAt(1) == '>' || t
/*  504 */               .charAt(1) == ']')) {
/*  505 */               if (!stack.isEmpty()) {
/*  506 */                 stack.pop();
/*      */               }
/*      */             }
/*  509 */             else if (t.length() == 2 && (t
/*  510 */               .charAt(0) == '<' || t.charAt(0) == '[') && t
/*  511 */               .charAt(1) == '/') {
/*  512 */               String tagName = null;
/*  513 */               if (!stack.isEmpty()) {
/*  514 */                 tagName = stack.pop();
/*      */               }
/*  516 */               if (t.getEndOffset() >= dot) {
/*  517 */                 return tagName;
/*      */               }
/*      */             } 
/*      */           }
/*      */           
/*  522 */           t = (t == null) ? null : t.getNextToken();
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  528 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String getMacroID() {
/*  534 */       return getName();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class CollapseAllCommentFoldsAction
/*      */     extends FoldRelatedAction
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */     
/*      */     public CollapseAllCommentFoldsAction() {
/*  548 */       super("RSTA.CollapseAllCommentFoldsAction");
/*  549 */       setProperties(RSyntaxTextAreaEditorKit.msg, "Action.CollapseCommentFolds");
/*      */     }
/*      */ 
/*      */     
/*      */     public CollapseAllCommentFoldsAction(String name, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/*  554 */       super(name, icon, desc, mnemonic, accelerator);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/*  559 */       RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
/*  560 */       if (rsta.isCodeFoldingEnabled()) {
/*  561 */         FoldCollapser collapser = new FoldCollapser();
/*  562 */         collapser.collapseFolds(rsta.getFoldManager());
/*  563 */         RSyntaxUtilities.possiblyRepaintGutter(textArea);
/*      */       } else {
/*      */         
/*  566 */         UIManager.getLookAndFeel().provideErrorFeedback((Component)rsta);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/*  572 */       return "RSTA.CollapseAllCommentFoldsAction";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class CollapseAllFoldsAction
/*      */     extends FoldRelatedAction
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */     
/*      */     public CollapseAllFoldsAction() {
/*  586 */       this(false);
/*      */     }
/*      */     
/*      */     public CollapseAllFoldsAction(boolean localizedName) {
/*  590 */       super("RSTA.CollapseAllFoldsAction");
/*  591 */       if (localizedName) {
/*  592 */         setProperties(RSyntaxTextAreaEditorKit.msg, "Action.CollapseAllFolds");
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public CollapseAllFoldsAction(String name, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/*  598 */       super(name, icon, desc, mnemonic, accelerator);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/*  603 */       RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
/*  604 */       if (rsta.isCodeFoldingEnabled()) {
/*  605 */         FoldCollapser collapser = new FoldCollapser()
/*      */           {
/*      */             public boolean getShouldCollapse(Fold fold) {
/*  608 */               return true;
/*      */             }
/*      */           };
/*  611 */         collapser.collapseFolds(rsta.getFoldManager());
/*  612 */         RSyntaxUtilities.possiblyRepaintGutter(textArea);
/*      */       } else {
/*      */         
/*  615 */         UIManager.getLookAndFeel().provideErrorFeedback((Component)rsta);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/*  621 */       return "RSTA.CollapseAllFoldsAction";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class CopyAsStyledTextAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     private Theme theme;
/*      */ 
/*      */     
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */     
/*      */     public CopyAsStyledTextAction() {
/*  637 */       super("RSTA.CopyAsStyledTextAction");
/*      */     }
/*      */     
/*      */     public CopyAsStyledTextAction(String themeName, Theme theme) {
/*  641 */       super("RSTA.CopyAsStyledTextAction_" + themeName);
/*  642 */       this.theme = theme;
/*      */     }
/*      */ 
/*      */     
/*      */     public CopyAsStyledTextAction(String name, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/*  647 */       super(name, icon, desc, mnemonic, accelerator);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/*  652 */       ((RSyntaxTextArea)textArea).copyAsStyledText(this.theme);
/*  653 */       textArea.requestFocusInWindow();
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/*  658 */       return getName();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class DecreaseFontSizeAction
/*      */     extends RTextAreaEditorKit.DecreaseFontSizeAction
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public DecreaseFontSizeAction() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public DecreaseFontSizeAction(String name, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/*  678 */       super(name, icon, desc, mnemonic, accelerator);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/*  684 */       RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
/*  685 */       SyntaxScheme scheme = rsta.getSyntaxScheme();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  693 */       boolean changed = false;
/*  694 */       int count = scheme.getStyleCount();
/*  695 */       for (int i = 0; i < count; i++) {
/*  696 */         Style ss = scheme.getStyle(i);
/*  697 */         if (ss != null) {
/*  698 */           Font font1 = ss.font;
/*  699 */           if (font1 != null) {
/*  700 */             float f1 = font1.getSize2D();
/*  701 */             float f2 = f1 - this.decreaseAmount;
/*  702 */             if (f2 >= 2.0F) {
/*      */               
/*  704 */               ss.font = font1.deriveFont(f2);
/*  705 */               changed = true;
/*      */             }
/*  707 */             else if (f1 > 2.0F) {
/*      */ 
/*      */               
/*  710 */               ss.font = font1.deriveFont(2.0F);
/*  711 */               changed = true;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  718 */       Font font = rsta.getFont();
/*  719 */       float oldSize = font.getSize2D();
/*  720 */       float newSize = oldSize - this.decreaseAmount;
/*  721 */       if (newSize >= 2.0F) {
/*      */         
/*  723 */         rsta.setFont(font.deriveFont(newSize));
/*  724 */         changed = true;
/*      */       }
/*  726 */       else if (oldSize > 2.0F) {
/*      */ 
/*      */         
/*  729 */         rsta.setFont(font.deriveFont(2.0F));
/*  730 */         changed = true;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  735 */       if (changed) {
/*  736 */         rsta.setSyntaxScheme(scheme);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  745 */         Component parent = rsta.getParent();
/*  746 */         if (parent instanceof javax.swing.JViewport) {
/*  747 */           parent = parent.getParent();
/*  748 */           if (parent instanceof javax.swing.JScrollPane) {
/*  749 */             parent.repaint();
/*      */           }
/*      */         } 
/*      */       } else {
/*      */         
/*  754 */         UIManager.getLookAndFeel().provideErrorFeedback((Component)rsta);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class DecreaseIndentAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */     
/*      */     private Segment s;
/*      */ 
/*      */ 
/*      */     
/*      */     public DecreaseIndentAction() {
/*  773 */       this("RSTA.DecreaseIndentAction");
/*      */     }
/*      */     
/*      */     public DecreaseIndentAction(String name) {
/*  777 */       super(name);
/*  778 */       this.s = new Segment();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/*  784 */       if (!textArea.isEditable() || !textArea.isEnabled()) {
/*  785 */         UIManager.getLookAndFeel().provideErrorFeedback((Component)textArea);
/*      */         
/*      */         return;
/*      */       } 
/*  789 */       Document document = textArea.getDocument();
/*  790 */       Element map = document.getDefaultRootElement();
/*  791 */       Caret c = textArea.getCaret();
/*  792 */       int dot = c.getDot();
/*  793 */       int mark = c.getMark();
/*  794 */       int line1 = map.getElementIndex(dot);
/*  795 */       int tabSize = textArea.getTabSize();
/*      */ 
/*      */ 
/*      */       
/*  799 */       if (dot != mark) {
/*      */ 
/*      */         
/*  802 */         int line2 = map.getElementIndex(mark);
/*  803 */         dot = Math.min(line1, line2);
/*  804 */         mark = Math.max(line1, line2);
/*      */         
/*  806 */         textArea.beginAtomicEdit();
/*      */         try {
/*  808 */           for (line1 = dot; line1 < mark; line1++) {
/*  809 */             Element element = map.getElement(line1);
/*  810 */             handleDecreaseIndent(element, document, tabSize);
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  816 */           Element elem = map.getElement(mark);
/*  817 */           int start = elem.getStartOffset();
/*  818 */           if (Math.max(c.getDot(), c.getMark()) != start) {
/*  819 */             handleDecreaseIndent(elem, document, tabSize);
/*      */           }
/*  821 */         } catch (BadLocationException ble) {
/*  822 */           ble.printStackTrace();
/*  823 */           UIManager.getLookAndFeel()
/*  824 */             .provideErrorFeedback((Component)textArea);
/*      */         } finally {
/*  826 */           textArea.endAtomicEdit();
/*      */         } 
/*      */       } else {
/*      */         
/*  830 */         Element elem = map.getElement(line1);
/*      */         try {
/*  832 */           handleDecreaseIndent(elem, document, tabSize);
/*  833 */         } catch (BadLocationException ble) {
/*  834 */           ble.printStackTrace();
/*  835 */           UIManager.getLookAndFeel()
/*  836 */             .provideErrorFeedback((Component)textArea);
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/*  844 */       return "RSTA.DecreaseIndentAction";
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
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void handleDecreaseIndent(Element elem, Document doc, int tabSize) throws BadLocationException {
/*  864 */       int start = elem.getStartOffset();
/*  865 */       int end = elem.getEndOffset() - 1;
/*  866 */       doc.getText(start, end - start, this.s);
/*  867 */       int i = this.s.offset;
/*  868 */       end = i + this.s.count;
/*  869 */       if (end > i)
/*      */       {
/*  871 */         if (this.s.array[i] == '\t') {
/*  872 */           doc.remove(start, 1);
/*      */ 
/*      */ 
/*      */         
/*      */         }
/*  877 */         else if (this.s.array[i] == ' ') {
/*  878 */           i++;
/*  879 */           int toRemove = 1;
/*  880 */           while (i < end && this.s.array[i] == ' ' && toRemove < tabSize) {
/*  881 */             i++;
/*  882 */             toRemove++;
/*      */           } 
/*  884 */           doc.remove(start, toRemove);
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
/*      */   public static class DeletePrevWordAction
/*      */     extends RTextAreaEditorKit.DeletePrevWordAction
/*      */   {
/*  899 */     private Segment seg = new Segment();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected int getPreviousWordStart(RTextArea textArea, int offs) throws BadLocationException {
/*  905 */       if (offs == 0) {
/*  906 */         return offs;
/*      */       }
/*      */       
/*  909 */       RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
/*  910 */       int line = textArea.getLineOfOffset(offs);
/*  911 */       int start = textArea.getLineStartOffset(line);
/*  912 */       if (offs == start) {
/*  913 */         return start - 1;
/*      */       }
/*  915 */       int end = textArea.getLineEndOffset(line);
/*  916 */       if (line != textArea.getLineCount() - 1) {
/*  917 */         end--;
/*      */       }
/*  919 */       doc.getText(start, end - start, this.seg);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  925 */       int firstIndex = this.seg.getBeginIndex() + offs - start - 1;
/*  926 */       this.seg.setIndex(firstIndex);
/*  927 */       char ch = this.seg.current();
/*      */ 
/*      */       
/*  930 */       if (Character.isWhitespace(ch)) {
/*      */         do {
/*  932 */           ch = this.seg.previous();
/*  933 */         } while (Character.isWhitespace(ch));
/*      */       }
/*      */ 
/*      */       
/*  937 */       int languageIndex = 0;
/*  938 */       if (doc.isIdentifierChar(languageIndex, ch)) {
/*      */         do {
/*  940 */           ch = this.seg.previous();
/*  941 */         } while (doc.isIdentifierChar(languageIndex, ch));
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  946 */         while (!Character.isWhitespace(ch) && 
/*  947 */           !doc.isIdentifierChar(languageIndex, ch) && ch != Character.MAX_VALUE)
/*      */         {
/*  949 */           ch = this.seg.previous();
/*      */         }
/*      */       } 
/*      */       
/*  953 */       if (ch == Character.MAX_VALUE) {
/*  954 */         return start;
/*      */       }
/*  956 */       offs -= firstIndex - this.seg.getIndex();
/*  957 */       return offs;
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
/*      */   public static class DumbCompleteWordAction
/*      */     extends RTextAreaEditorKit.DumbCompleteWordAction
/*      */   {
/*      */     protected int getPreviousWord(RTextArea textArea, int offs) throws BadLocationException {
/*  975 */       RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
/*  976 */       Element root = doc.getDefaultRootElement();
/*  977 */       int line = root.getElementIndex(offs);
/*  978 */       Element elem = root.getElement(line);
/*      */ 
/*      */ 
/*      */       
/*  982 */       int start = elem.getStartOffset();
/*  983 */       if (offs > start) {
/*  984 */         char ch = doc.charAt(offs);
/*  985 */         if (isIdentifierChar(ch)) {
/*  986 */           offs--;
/*      */         }
/*      */       } else {
/*      */         
/*  990 */         if (line == 0) {
/*  991 */           return -1;
/*      */         }
/*  993 */         elem = root.getElement(--line);
/*  994 */         offs = elem.getEndOffset() - 1;
/*      */       } 
/*      */       
/*  997 */       int prevWordStart = getPreviousWordStartInLine(doc, elem, offs);
/*  998 */       while (prevWordStart == -1 && line > 0) {
/*  999 */         line--;
/* 1000 */         elem = root.getElement(line);
/* 1001 */         prevWordStart = getPreviousWordStartInLine(doc, elem, elem
/* 1002 */             .getEndOffset());
/*      */       } 
/*      */       
/* 1005 */       return prevWordStart;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int getPreviousWordStartInLine(RSyntaxDocument doc, Element elem, int offs) throws BadLocationException {
/* 1012 */       int start = elem.getStartOffset();
/* 1013 */       int cur = offs;
/*      */ 
/*      */       
/* 1016 */       while (cur >= start) {
/* 1017 */         char ch = doc.charAt(cur);
/* 1018 */         if (isIdentifierChar(ch)) {
/*      */           break;
/*      */         }
/* 1021 */         cur--;
/*      */       } 
/* 1023 */       if (cur < start)
/*      */       {
/* 1025 */         return -1;
/*      */       }
/*      */       
/* 1028 */       return getWordStartImpl(doc, elem, cur);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected int getWordEnd(RTextArea textArea, int offs) throws BadLocationException {
/* 1036 */       RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
/* 1037 */       Element root = doc.getDefaultRootElement();
/* 1038 */       int line = root.getElementIndex(offs);
/* 1039 */       Element elem = root.getElement(line);
/* 1040 */       int end = elem.getEndOffset() - 1;
/*      */       
/* 1042 */       int wordEnd = offs;
/* 1043 */       while (wordEnd <= end && 
/* 1044 */         isIdentifierChar(doc.charAt(wordEnd)))
/*      */       {
/*      */         
/* 1047 */         wordEnd++;
/*      */       }
/*      */       
/* 1050 */       return wordEnd;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected int getWordStart(RTextArea textArea, int offs) throws BadLocationException {
/* 1057 */       RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
/* 1058 */       Element root = doc.getDefaultRootElement();
/* 1059 */       int line = root.getElementIndex(offs);
/* 1060 */       Element elem = root.getElement(line);
/* 1061 */       return getWordStartImpl(doc, elem, offs);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private static int getWordStartImpl(RSyntaxDocument doc, Element elem, int offs) throws BadLocationException {
/* 1067 */       int start = elem.getStartOffset();
/*      */       
/* 1069 */       int wordStart = offs;
/* 1070 */       while (wordStart >= start) {
/* 1071 */         char ch = doc.charAt(wordStart);
/*      */         
/* 1073 */         if (!isIdentifierChar(ch) && ch != '\n') {
/*      */           break;
/*      */         }
/* 1076 */         wordStart--;
/*      */       } 
/*      */       
/* 1079 */       return (wordStart == offs) ? offs : (wordStart + 1);
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
/*      */     protected boolean isAcceptablePrefix(String prefix) {
/* 1093 */       return (prefix.length() > 0 && 
/* 1094 */         isIdentifierChar(prefix.charAt(prefix.length() - 1)));
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
/*      */     private static boolean isIdentifierChar(char ch) {
/* 1106 */       return (Character.isLetterOrDigit(ch) || ch == '_' || ch == '$');
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static class EndWordAction
/*      */     extends RTextAreaEditorKit.EndWordAction
/*      */   {
/*      */     private Segment seg;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected EndWordAction(String name, boolean select) {
/* 1122 */       super(name, select);
/* 1123 */       this.seg = new Segment();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected int getWordEnd(RTextArea textArea, int offs) throws BadLocationException {
/* 1130 */       RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
/* 1131 */       if (offs == doc.getLength()) {
/* 1132 */         return offs;
/*      */       }
/*      */       
/* 1135 */       int line = textArea.getLineOfOffset(offs);
/* 1136 */       int end = textArea.getLineEndOffset(line);
/* 1137 */       if (line != textArea.getLineCount() - 1) {
/* 1138 */         end--;
/*      */       }
/* 1140 */       if (offs == end) {
/* 1141 */         return end;
/*      */       }
/* 1143 */       doc.getText(offs, end - offs, this.seg);
/*      */ 
/*      */ 
/*      */       
/* 1147 */       char ch = this.seg.first();
/*      */ 
/*      */       
/* 1150 */       int languageIndex = 0;
/* 1151 */       if (doc.isIdentifierChar(languageIndex, ch)) {
/*      */         do {
/* 1153 */           ch = this.seg.next();
/* 1154 */         } while (doc.isIdentifierChar(languageIndex, ch) && ch != Character.MAX_VALUE);
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 1159 */       else if (Character.isWhitespace(ch)) {
/*      */         
/*      */         do {
/* 1162 */           ch = this.seg.next();
/* 1163 */         } while (Character.isWhitespace(ch));
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1169 */       offs += this.seg.getIndex() - this.seg.getBeginIndex();
/* 1170 */       return offs;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class ExpandAllFoldsAction
/*      */     extends FoldRelatedAction
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */     
/*      */     public ExpandAllFoldsAction() {
/* 1185 */       this(false);
/*      */     }
/*      */     
/*      */     public ExpandAllFoldsAction(boolean localizedName) {
/* 1189 */       super("RSTA.ExpandAllFoldsAction");
/* 1190 */       if (localizedName) {
/* 1191 */         setProperties(RSyntaxTextAreaEditorKit.msg, "Action.ExpandAllFolds");
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public ExpandAllFoldsAction(String name, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/* 1197 */       super(name, icon, desc, mnemonic, accelerator);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1202 */       RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
/* 1203 */       if (rsta.isCodeFoldingEnabled()) {
/* 1204 */         FoldManager fm = rsta.getFoldManager();
/* 1205 */         for (int i = 0; i < fm.getFoldCount(); i++) {
/* 1206 */           expand(fm.getFold(i));
/*      */         }
/* 1208 */         RSyntaxUtilities.possiblyRepaintGutter(rsta);
/*      */       } else {
/*      */         
/* 1211 */         UIManager.getLookAndFeel().provideErrorFeedback((Component)rsta);
/*      */       } 
/*      */     }
/*      */     
/*      */     private void expand(Fold fold) {
/* 1216 */       fold.setCollapsed(false);
/* 1217 */       for (int i = 0; i < fold.getChildCount(); i++) {
/* 1218 */         expand(fold.getChild(i));
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 1224 */       return "RSTA.ExpandAllFoldsAction";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class FoldRelatedAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     FoldRelatedAction(String name) {
/* 1236 */       super(name);
/*      */     }
/*      */ 
/*      */     
/*      */     FoldRelatedAction(String name, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/* 1241 */       super(name, icon, desc, mnemonic, accelerator);
/*      */     }
/*      */     
/*      */     protected Fold getClosestFold(RSyntaxTextArea textArea) {
/* 1245 */       int offs = textArea.getCaretPosition();
/* 1246 */       int line = textArea.getCaretLineNumber();
/* 1247 */       FoldManager fm = textArea.getFoldManager();
/* 1248 */       Fold fold = fm.getFoldForLine(line);
/* 1249 */       if (fold == null) {
/* 1250 */         fold = fm.getDeepestOpenFoldContaining(offs);
/*      */       }
/* 1252 */       return fold;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class GoToMatchingBracketAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */     
/*      */     private Point bracketInfo;
/*      */ 
/*      */ 
/*      */     
/*      */     public static class EndAction
/*      */       extends RTextAreaEditorKit.EndAction
/*      */     {
/*      */       public EndAction(String name, boolean select) {
/* 1272 */         super(name, select);
/*      */       }
/*      */ 
/*      */       
/*      */       protected int getVisibleEnd(RTextArea textArea) {
/* 1277 */         RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
/* 1278 */         return rsta.getLastVisibleOffset();
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public GoToMatchingBracketAction() {
/* 1288 */       super("RSTA.GoToMatchingBracketAction");
/*      */     }
/*      */ 
/*      */     
/*      */     public GoToMatchingBracketAction(String name, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/* 1293 */       super(name, icon, desc, mnemonic, accelerator);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1298 */       RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
/* 1299 */       this.bracketInfo = RSyntaxUtilities.getMatchingBracketPosition(rsta, this.bracketInfo);
/*      */       
/* 1301 */       if (this.bracketInfo.y > -1) {
/*      */ 
/*      */         
/* 1304 */         rsta.setCaretPosition(this.bracketInfo.y + 1);
/*      */       } else {
/*      */         
/* 1307 */         UIManager.getLookAndFeel().provideErrorFeedback((Component)rsta);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 1313 */       return "RSTA.GoToMatchingBracketAction";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class IncreaseFontSizeAction
/*      */     extends RTextAreaEditorKit.IncreaseFontSizeAction
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public IncreaseFontSizeAction() {}
/*      */ 
/*      */ 
/*      */     
/*      */     public IncreaseFontSizeAction(String name, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/* 1333 */       super(name, icon, desc, mnemonic, accelerator);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1339 */       RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
/* 1340 */       SyntaxScheme scheme = rsta.getSyntaxScheme();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1348 */       boolean changed = false;
/* 1349 */       int count = scheme.getStyleCount();
/* 1350 */       for (int i = 0; i < count; i++) {
/* 1351 */         Style ss = scheme.getStyle(i);
/* 1352 */         if (ss != null) {
/* 1353 */           Font font1 = ss.font;
/* 1354 */           if (font1 != null) {
/* 1355 */             float f1 = font1.getSize2D();
/* 1356 */             float f2 = f1 + this.increaseAmount;
/* 1357 */             if (f2 <= 40.0F) {
/*      */               
/* 1359 */               ss.font = font1.deriveFont(f2);
/* 1360 */               changed = true;
/*      */             }
/* 1362 */             else if (f1 < 40.0F) {
/*      */ 
/*      */               
/* 1365 */               ss.font = font1.deriveFont(40.0F);
/* 1366 */               changed = true;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1373 */       Font font = rsta.getFont();
/* 1374 */       float oldSize = font.getSize2D();
/* 1375 */       float newSize = oldSize + this.increaseAmount;
/* 1376 */       if (newSize <= 40.0F) {
/*      */         
/* 1378 */         rsta.setFont(font.deriveFont(newSize));
/* 1379 */         changed = true;
/*      */       }
/* 1381 */       else if (oldSize < 40.0F) {
/*      */ 
/*      */         
/* 1384 */         rsta.setFont(font.deriveFont(40.0F));
/* 1385 */         changed = true;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1390 */       if (changed) {
/* 1391 */         rsta.setSyntaxScheme(scheme);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1400 */         Component parent = rsta.getParent();
/* 1401 */         if (parent instanceof javax.swing.JViewport) {
/* 1402 */           parent = parent.getParent();
/* 1403 */           if (parent instanceof javax.swing.JScrollPane) {
/* 1404 */             parent.repaint();
/*      */           }
/*      */         } 
/*      */       } else {
/*      */         
/* 1409 */         UIManager.getLookAndFeel().provideErrorFeedback((Component)rsta);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class InsertBreakAction
/*      */     extends RTextAreaEditorKit.InsertBreakAction
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1429 */       if (!textArea.isEditable() || !textArea.isEnabled()) {
/* 1430 */         UIManager.getLookAndFeel().provideErrorFeedback((Component)textArea);
/*      */         
/*      */         return;
/*      */       } 
/* 1434 */       RSyntaxTextArea sta = (RSyntaxTextArea)textArea;
/* 1435 */       boolean noSelection = (sta.getSelectionStart() == sta.getSelectionEnd());
/*      */ 
/*      */ 
/*      */       
/* 1439 */       boolean handled = false;
/* 1440 */       if (noSelection) {
/* 1441 */         RSyntaxDocument doc = (RSyntaxDocument)sta.getDocument();
/* 1442 */         handled = doc.insertBreakSpecialHandling(e);
/*      */       } 
/*      */ 
/*      */       
/* 1446 */       if (!handled) {
/* 1447 */         handleInsertBreak(sta, noSelection);
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
/*      */     private static int atEndOfLine(int pos, String s, int sLen) {
/* 1459 */       for (int i = pos; i < sLen; i++) {
/* 1460 */         if (!RSyntaxUtilities.isWhitespace(s.charAt(i))) {
/* 1461 */           return i;
/*      */         }
/*      */       } 
/* 1464 */       return -1;
/*      */     }
/*      */ 
/*      */     
/*      */     private static int getOpenBraceCount(RSyntaxDocument doc, int languageIndex) {
/* 1469 */       int openCount = 0;
/* 1470 */       for (Token t : doc) {
/* 1471 */         if (t.getType() == 22 && t.length() == 1 && t
/* 1472 */           .getLanguageIndex() == languageIndex) {
/* 1473 */           char ch = t.charAt(0);
/* 1474 */           if (ch == '{') {
/* 1475 */             openCount++; continue;
/*      */           } 
/* 1477 */           if (ch == '}') {
/* 1478 */             openCount--;
/*      */           }
/*      */         } 
/*      */       } 
/* 1482 */       return openCount;
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
/*      */     protected void handleInsertBreak(RSyntaxTextArea textArea, boolean noSelection) {
/* 1496 */       if (noSelection && textArea.isAutoIndentEnabled()) {
/* 1497 */         insertNewlineWithAutoIndent(textArea);
/*      */       } else {
/*      */         
/* 1500 */         textArea.replaceSelection("\n");
/* 1501 */         if (noSelection) {
/* 1502 */           possiblyCloseCurlyBrace(textArea, null);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void insertNewlineWithAutoIndent(RSyntaxTextArea sta) {
/*      */       try {
/* 1511 */         int caretPos = sta.getCaretPosition();
/* 1512 */         Document doc = sta.getDocument();
/* 1513 */         Element map = doc.getDefaultRootElement();
/* 1514 */         int lineNum = map.getElementIndex(caretPos);
/* 1515 */         Element line = map.getElement(lineNum);
/* 1516 */         int start = line.getStartOffset();
/* 1517 */         int end = line.getEndOffset() - 1;
/* 1518 */         int len = end - start;
/* 1519 */         String s = doc.getText(start, len);
/*      */ 
/*      */ 
/*      */         
/* 1523 */         String leadingWS = RSyntaxUtilities.getLeadingWhitespace(s);
/* 1524 */         StringBuilder sb = new StringBuilder("\n");
/* 1525 */         sb.append(leadingWS);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1530 */         int nonWhitespacePos = atEndOfLine(caretPos - start, s, len);
/* 1531 */         if (nonWhitespacePos == -1) {
/* 1532 */           if (leadingWS.length() == len && sta
/* 1533 */             .isClearWhitespaceLinesEnabled()) {
/*      */ 
/*      */             
/* 1536 */             sta.setSelectionStart(start);
/* 1537 */             sta.setSelectionEnd(end);
/*      */           } 
/* 1539 */           sta.replaceSelection(sb.toString());
/*      */ 
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */ 
/*      */           
/* 1547 */           sb.append(s.substring(nonWhitespacePos));
/* 1548 */           sta.replaceRange(sb.toString(), caretPos, end);
/* 1549 */           sta.setCaretPosition(caretPos + leadingWS.length() + 1);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1555 */         if (sta.getShouldIndentNextLine(lineNum)) {
/* 1556 */           sta.replaceSelection("\t");
/*      */         }
/*      */         
/* 1559 */         possiblyCloseCurlyBrace(sta, leadingWS);
/*      */       }
/* 1561 */       catch (BadLocationException ble) {
/* 1562 */         sta.replaceSelection("\n");
/* 1563 */         ble.printStackTrace();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void possiblyCloseCurlyBrace(RSyntaxTextArea textArea, String leadingWS) {
/* 1571 */       RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
/*      */       
/* 1573 */       if (textArea.getCloseCurlyBraces()) {
/*      */         
/* 1575 */         int line = textArea.getCaretLineNumber();
/* 1576 */         Token t = doc.getTokenListForLine(line - 1);
/* 1577 */         t = t.getLastNonCommentNonWhitespaceToken();
/*      */         
/* 1579 */         if (t != null && t.isLeftCurly()) {
/*      */           
/* 1581 */           int languageIndex = t.getLanguageIndex();
/* 1582 */           if (doc.getCurlyBracesDenoteCodeBlocks(languageIndex) && 
/* 1583 */             getOpenBraceCount(doc, languageIndex) > 0) {
/* 1584 */             StringBuilder sb = new StringBuilder();
/* 1585 */             if (line == textArea.getLineCount() - 1) {
/* 1586 */               sb.append('\n');
/*      */             }
/* 1588 */             if (leadingWS != null) {
/* 1589 */               sb.append(leadingWS);
/*      */             }
/* 1591 */             sb.append("}\n");
/* 1592 */             int dot = textArea.getCaretPosition();
/* 1593 */             int end = textArea.getLineEndOffsetOfCurrentLine();
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1598 */             textArea.insert(sb.toString(), end);
/* 1599 */             textArea.setCaretPosition(dot);
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
/*      */   public static class InsertTabAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public InsertTabAction() {
/* 1620 */       super("insert-tab");
/*      */     }
/*      */     
/*      */     public InsertTabAction(String name) {
/* 1624 */       super(name);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1630 */       if (!textArea.isEditable() || !textArea.isEnabled()) {
/* 1631 */         UIManager.getLookAndFeel().provideErrorFeedback((Component)textArea);
/*      */         
/*      */         return;
/*      */       } 
/* 1635 */       Document document = textArea.getDocument();
/* 1636 */       Element map = document.getDefaultRootElement();
/* 1637 */       Caret c = textArea.getCaret();
/* 1638 */       int dot = c.getDot();
/* 1639 */       int mark = c.getMark();
/*      */ 
/*      */       
/* 1642 */       if (dot != mark) {
/*      */         
/* 1644 */         int dotLine = map.getElementIndex(dot);
/* 1645 */         int markLine = map.getElementIndex(mark);
/* 1646 */         int first = Math.min(dotLine, markLine);
/* 1647 */         int last = Math.max(dotLine, markLine);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1652 */         String replacement = "\t";
/* 1653 */         if (textArea.getTabsEmulated()) {
/* 1654 */           StringBuilder sb = new StringBuilder();
/* 1655 */           int temp = textArea.getTabSize();
/* 1656 */           for (int i = 0; i < temp; i++) {
/* 1657 */             sb.append(' ');
/*      */           }
/* 1659 */           replacement = sb.toString();
/*      */         } 
/*      */         
/* 1662 */         textArea.beginAtomicEdit();
/*      */         try {
/* 1664 */           for (int i = first; i < last; i++) {
/* 1665 */             Element element = map.getElement(i);
/* 1666 */             int j = element.getStartOffset();
/* 1667 */             document.insertString(j, replacement, null);
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1673 */           Element elem = map.getElement(last);
/* 1674 */           int start = elem.getStartOffset();
/* 1675 */           if (Math.max(c.getDot(), c.getMark()) != start) {
/* 1676 */             document.insertString(start, replacement, null);
/*      */           }
/* 1678 */         } catch (BadLocationException ble) {
/* 1679 */           ble.printStackTrace();
/* 1680 */           UIManager.getLookAndFeel()
/* 1681 */             .provideErrorFeedback((Component)textArea);
/*      */         } finally {
/* 1683 */           textArea.endAtomicEdit();
/*      */         } 
/*      */       } else {
/*      */         
/* 1687 */         textArea.replaceSelection("\t");
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 1694 */       return "insert-tab";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class NextWordAction
/*      */     extends RTextAreaEditorKit.NextWordAction
/*      */   {
/*      */     private Segment seg;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NextWordAction(String nm, boolean select) {
/* 1711 */       super(nm, select);
/* 1712 */       this.seg = new Segment();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected int getNextWord(RTextArea textArea, int offs) throws BadLocationException {
/* 1722 */       RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
/* 1723 */       if (offs == doc.getLength()) {
/* 1724 */         return offs;
/*      */       }
/*      */       
/* 1727 */       Element root = doc.getDefaultRootElement();
/* 1728 */       int line = root.getElementIndex(offs);
/* 1729 */       int end = root.getElement(line).getEndOffset() - 1;
/* 1730 */       if (offs == end) {
/* 1731 */         RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
/* 1732 */         if (rsta.isCodeFoldingEnabled()) {
/* 1733 */           FoldManager fm = rsta.getFoldManager();
/* 1734 */           int lineCount = root.getElementCount();
/* 1735 */           while (++line < lineCount && fm.isLineHidden(line));
/* 1736 */           if (line < lineCount) {
/* 1737 */             offs = root.getElement(line).getStartOffset();
/*      */           }
/*      */           
/* 1740 */           return offs;
/*      */         } 
/*      */         
/* 1743 */         return offs + 1;
/*      */       } 
/*      */       
/* 1746 */       doc.getText(offs, end - offs, this.seg);
/*      */ 
/*      */ 
/*      */       
/* 1750 */       char ch = this.seg.first();
/*      */ 
/*      */       
/* 1753 */       int languageIndex = 0;
/* 1754 */       if (doc.isIdentifierChar(languageIndex, ch)) {
/*      */         do {
/* 1756 */           ch = this.seg.next();
/* 1757 */         } while (doc.isIdentifierChar(languageIndex, ch) && ch != Character.MAX_VALUE);
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 1762 */       else if (!Character.isWhitespace(ch)) {
/*      */         do {
/* 1764 */           ch = this.seg.next();
/* 1765 */         } while (ch != Character.MAX_VALUE && 
/* 1766 */           !doc.isIdentifierChar(languageIndex, ch) && 
/* 1767 */           !Character.isWhitespace(ch));
/*      */       } 
/*      */ 
/*      */       
/* 1771 */       while (Character.isWhitespace(ch)) {
/* 1772 */         ch = this.seg.next();
/*      */       }
/*      */       
/* 1775 */       offs += this.seg.getIndex() - this.seg.getBeginIndex();
/* 1776 */       return offs;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class PossiblyInsertTemplateAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public PossiblyInsertTemplateAction() {
/* 1793 */       super("RSTA.TemplateAction");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1799 */       if (!textArea.isEditable() || !textArea.isEnabled()) {
/*      */         return;
/*      */       }
/*      */       
/* 1803 */       RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
/*      */       
/* 1805 */       if (RSyntaxTextArea.getTemplatesEnabled()) {
/*      */         
/* 1807 */         Document doc = textArea.getDocument();
/* 1808 */         if (doc != null) {
/*      */           
/*      */           try {
/*      */ 
/*      */             
/* 1813 */             CodeTemplateManager manager = RSyntaxTextArea.getCodeTemplateManager();
/*      */             
/* 1815 */             CodeTemplate template = (manager == null) ? null : manager.getTemplate(rsta);
/*      */ 
/*      */             
/* 1818 */             if (template != null) {
/* 1819 */               template.invoke(rsta);
/*      */             
/*      */             }
/*      */             else {
/*      */ 
/*      */               
/* 1825 */               doDefaultInsert(rsta);
/*      */             }
/*      */           
/* 1828 */           } catch (BadLocationException ble) {
/* 1829 */             UIManager.getLookAndFeel()
/* 1830 */               .provideErrorFeedback((Component)textArea);
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1840 */         doDefaultInsert(rsta);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void doDefaultInsert(RTextArea textArea) {
/* 1860 */       textArea.replaceSelection(" ");
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 1865 */       return "RSTA.TemplateAction";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class PreviousWordAction
/*      */     extends RTextAreaEditorKit.PreviousWordAction
/*      */   {
/*      */     private Segment seg;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public PreviousWordAction(String nm, boolean select) {
/* 1882 */       super(nm, select);
/* 1883 */       this.seg = new Segment();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected int getPreviousWord(RTextArea textArea, int offs) throws BadLocationException {
/* 1893 */       if (offs == 0) {
/* 1894 */         return offs;
/*      */       }
/*      */       
/* 1897 */       RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
/* 1898 */       Element root = doc.getDefaultRootElement();
/* 1899 */       int line = root.getElementIndex(offs);
/* 1900 */       int start = root.getElement(line).getStartOffset();
/* 1901 */       if (offs == start) {
/* 1902 */         RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
/* 1903 */         if (rsta.isCodeFoldingEnabled()) {
/* 1904 */           FoldManager fm = rsta.getFoldManager();
/* 1905 */           while (--line >= 0 && fm.isLineHidden(line));
/* 1906 */           if (line >= 0) {
/* 1907 */             offs = root.getElement(line).getEndOffset() - 1;
/*      */           }
/*      */           
/* 1910 */           return offs;
/*      */         } 
/*      */         
/* 1913 */         return start - 1;
/*      */       } 
/*      */       
/* 1916 */       doc.getText(start, offs - start, this.seg);
/*      */ 
/*      */ 
/*      */       
/* 1920 */       char ch = this.seg.last();
/*      */ 
/*      */       
/* 1923 */       while (Character.isWhitespace(ch)) {
/* 1924 */         ch = this.seg.previous();
/*      */       }
/*      */ 
/*      */       
/* 1928 */       int languageIndex = 0;
/* 1929 */       if (doc.isIdentifierChar(languageIndex, ch)) {
/*      */         do {
/* 1931 */           ch = this.seg.previous();
/* 1932 */         } while (doc.isIdentifierChar(languageIndex, ch) && ch != Character.MAX_VALUE);
/*      */ 
/*      */ 
/*      */       
/*      */       }
/* 1937 */       else if (!Character.isWhitespace(ch)) {
/*      */         do {
/* 1939 */           ch = this.seg.previous();
/* 1940 */         } while (ch != Character.MAX_VALUE && 
/* 1941 */           !doc.isIdentifierChar(languageIndex, ch) && 
/* 1942 */           !Character.isWhitespace(ch));
/*      */       } 
/*      */       
/* 1945 */       offs -= this.seg.getEndIndex() - this.seg.getIndex();
/* 1946 */       if (ch != Character.MAX_VALUE) {
/* 1947 */         offs++;
/*      */       }
/*      */       
/* 1950 */       return offs;
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
/*      */   public static class SelectWordAction
/*      */     extends RTextAreaEditorKit.SelectWordAction
/*      */   {
/*      */     protected void createActions() {
/* 1966 */       this.start = (Action)new RSyntaxTextAreaEditorKit.BeginWordAction("pigdog", false);
/* 1967 */       this.end = (Action)new RSyntaxTextAreaEditorKit.EndWordAction("pigdog", true);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class ToggleCommentAction
/*      */     extends RecordableTextAction
/*      */   {
/*      */     public ToggleCommentAction() {
/* 1980 */       super("RSTA.ToggleCommentAction");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 1986 */       if (!textArea.isEditable() || !textArea.isEnabled()) {
/* 1987 */         UIManager.getLookAndFeel().provideErrorFeedback((Component)textArea);
/*      */         
/*      */         return;
/*      */       } 
/* 1991 */       RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
/* 1992 */       Element map = doc.getDefaultRootElement();
/* 1993 */       Caret c = textArea.getCaret();
/* 1994 */       int dot = c.getDot();
/* 1995 */       int mark = c.getMark();
/* 1996 */       int line1 = map.getElementIndex(dot);
/* 1997 */       int line2 = map.getElementIndex(mark);
/* 1998 */       int start = Math.min(line1, line2);
/* 1999 */       int end = Math.max(line1, line2);
/*      */       
/* 2001 */       Token t = doc.getTokenListForLine(start);
/* 2002 */       int languageIndex = (t != null) ? t.getLanguageIndex() : 0;
/* 2003 */       String[] startEnd = doc.getLineCommentStartAndEnd(languageIndex);
/*      */       
/* 2005 */       if (startEnd == null) {
/* 2006 */         UIManager.getLookAndFeel().provideErrorFeedback((Component)textArea);
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/* 2012 */       if (start != end) {
/* 2013 */         Element elem = map.getElement(end);
/* 2014 */         if (Math.max(dot, mark) == elem.getStartOffset()) {
/* 2015 */           end--;
/*      */         }
/*      */       } 
/*      */       
/* 2019 */       textArea.beginAtomicEdit();
/*      */       try {
/* 2021 */         boolean add = getDoAdd((Document)doc, map, start, end, startEnd);
/* 2022 */         for (line1 = start; line1 <= end; line1++) {
/* 2023 */           Element elem = map.getElement(line1);
/* 2024 */           handleToggleComment(elem, (Document)doc, startEnd, add);
/*      */         } 
/* 2026 */       } catch (BadLocationException ble) {
/* 2027 */         ble.printStackTrace();
/* 2028 */         UIManager.getLookAndFeel().provideErrorFeedback((Component)textArea);
/*      */       } finally {
/* 2030 */         textArea.endAtomicEdit();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean getDoAdd(Document doc, Element map, int startLine, int endLine, String[] startEnd) throws BadLocationException {
/* 2038 */       boolean doAdd = false;
/* 2039 */       for (int i = startLine; i <= endLine; i++) {
/* 2040 */         Element elem = map.getElement(i);
/* 2041 */         int start = elem.getStartOffset();
/* 2042 */         String t = doc.getText(start, elem.getEndOffset() - start - 1);
/* 2043 */         if (!t.startsWith(startEnd[0]) || (startEnd[1] != null && 
/* 2044 */           !t.endsWith(startEnd[1]))) {
/* 2045 */           doAdd = true;
/*      */           break;
/*      */         } 
/*      */       } 
/* 2049 */       return doAdd;
/*      */     }
/*      */ 
/*      */     
/*      */     private void handleToggleComment(Element elem, Document doc, String[] startEnd, boolean add) throws BadLocationException {
/* 2054 */       int start = elem.getStartOffset();
/* 2055 */       int end = elem.getEndOffset() - 1;
/* 2056 */       if (add) {
/* 2057 */         if (startEnd[1] != null) {
/* 2058 */           doc.insertString(end, startEnd[1], null);
/*      */         }
/* 2060 */         doc.insertString(start, startEnd[0], null);
/*      */       } else {
/*      */         
/* 2063 */         if (startEnd[1] != null) {
/* 2064 */           int temp = startEnd[1].length();
/* 2065 */           doc.remove(end - temp, temp);
/*      */         } 
/* 2067 */         doc.remove(start, startEnd[0].length());
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 2073 */       return "RSTA.ToggleCommentAction";
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class ToggleCurrentFoldAction
/*      */     extends FoldRelatedAction
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */ 
/*      */     
/*      */     public ToggleCurrentFoldAction() {
/* 2087 */       super("RSTA.ToggleCurrentFoldAction");
/* 2088 */       setProperties(RSyntaxTextAreaEditorKit.msg, "Action.ToggleCurrentFold");
/*      */     }
/*      */ 
/*      */     
/*      */     public ToggleCurrentFoldAction(String name, Icon icon, String desc, Integer mnemonic, KeyStroke accelerator) {
/* 2093 */       super(name, icon, desc, mnemonic, accelerator);
/*      */     }
/*      */ 
/*      */     
/*      */     public void actionPerformedImpl(ActionEvent e, RTextArea textArea) {
/* 2098 */       RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
/* 2099 */       if (rsta.isCodeFoldingEnabled()) {
/* 2100 */         Fold fold = getClosestFold(rsta);
/* 2101 */         if (fold != null) {
/* 2102 */           fold.toggleCollapsedState();
/*      */         }
/* 2104 */         RSyntaxUtilities.possiblyRepaintGutter(textArea);
/*      */       } else {
/*      */         
/* 2107 */         UIManager.getLookAndFeel().provideErrorFeedback((Component)rsta);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public final String getMacroID() {
/* 2113 */       return "RSTA.ToggleCurrentFoldAction";
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\RSyntaxTextAreaEditorKit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */