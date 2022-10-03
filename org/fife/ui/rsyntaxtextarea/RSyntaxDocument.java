/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.util.Iterator;
/*     */ import javax.swing.Action;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.text.AbstractDocument;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.Segment;
/*     */ import org.fife.ui.rsyntaxtextarea.modes.AbstractMarkupTokenMaker;
/*     */ import org.fife.ui.rtextarea.RDocument;
/*     */ import org.fife.util.DynamicIntArray;
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
/*     */ public class RSyntaxDocument
/*     */   extends RDocument
/*     */   implements Iterable<Token>, SyntaxConstants
/*     */ {
/*     */   private transient TokenMakerFactory tokenMakerFactory;
/*     */   private transient TokenMaker tokenMaker;
/*     */   private String syntaxStyle;
/*     */   protected transient DynamicIntArray lastTokensOnLines;
/*  79 */   private transient int lastLine = -1;
/*     */   private transient Token cachedTokenList;
/*  81 */   private transient int useCacheCount = 0;
/*  82 */   private transient int tokenRetrievalCount = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private transient Segment s;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final boolean DEBUG_TOKEN_CACHING = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RSyntaxDocument(String syntaxStyle) {
/* 100 */     this((TokenMakerFactory)null, syntaxStyle);
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
/*     */   public RSyntaxDocument(TokenMakerFactory tmf, String syntaxStyle) {
/* 113 */     putProperty("tabSize", Integer.valueOf(5));
/* 114 */     this.lastTokensOnLines = new DynamicIntArray(400);
/* 115 */     this.lastTokensOnLines.add(0);
/* 116 */     this.s = new Segment();
/* 117 */     setTokenMakerFactory(tmf);
/* 118 */     setSyntaxStyle(syntaxStyle);
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
/*     */   protected void fireInsertUpdate(DocumentEvent e) {
/* 134 */     this.cachedTokenList = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     Element lineMap = getDefaultRootElement();
/* 143 */     DocumentEvent.ElementChange change = e.getChange(lineMap);
/* 144 */     Element[] added = (change == null) ? null : change.getChildrenAdded();
/*     */     
/* 146 */     int numLines = lineMap.getElementCount();
/* 147 */     int line = lineMap.getElementIndex(e.getOffset());
/* 148 */     int previousLine = line - 1;
/*     */     
/* 150 */     int previousTokenType = (previousLine > -1) ? this.lastTokensOnLines.get(previousLine) : 0;
/*     */ 
/*     */     
/* 153 */     if (added != null && added.length > 0) {
/*     */       
/* 155 */       Element[] removed = change.getChildrenRemoved();
/* 156 */       int numRemoved = (removed != null) ? removed.length : 0;
/*     */       
/* 158 */       int endBefore = line + added.length - numRemoved;
/*     */ 
/*     */       
/* 161 */       for (int i = line; i < endBefore; i++) {
/*     */         
/* 163 */         setSharedSegment(i);
/*     */         
/* 165 */         int tokenType = this.tokenMaker.getLastTokenTypeOnLine(this.s, previousTokenType);
/* 166 */         this.lastTokensOnLines.add(i, tokenType);
/*     */ 
/*     */         
/* 169 */         previousTokenType = tokenType;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 174 */       updateLastTokensBelow(endBefore, numLines, previousTokenType);
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */       
/* 182 */       updateLastTokensBelow(line, numLines, previousTokenType);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 187 */     super.fireInsertUpdate(e);
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
/*     */   protected void fireRemoveUpdate(DocumentEvent chng) {
/* 209 */     this.cachedTokenList = null;
/* 210 */     Element lineMap = getDefaultRootElement();
/* 211 */     int numLines = lineMap.getElementCount();
/*     */     
/* 213 */     DocumentEvent.ElementChange change = chng.getChange(lineMap);
/* 214 */     Element[] removed = (change == null) ? null : change.getChildrenRemoved();
/*     */ 
/*     */     
/* 217 */     if (removed != null && removed.length > 0) {
/*     */       
/* 219 */       int line = change.getIndex();
/* 220 */       int previousLine = line - 1;
/*     */       
/* 222 */       int previousTokenType = (previousLine > -1) ? this.lastTokensOnLines.get(previousLine) : 0;
/*     */       
/* 224 */       Element[] added = change.getChildrenAdded();
/* 225 */       int numAdded = (added == null) ? 0 : added.length;
/*     */ 
/*     */       
/* 228 */       int endBefore = line + removed.length - numAdded;
/*     */ 
/*     */ 
/*     */       
/* 232 */       this.lastTokensOnLines.removeRange(line, endBefore);
/*     */ 
/*     */ 
/*     */       
/* 236 */       updateLastTokensBelow(line, numLines, previousTokenType);
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/* 243 */       int line = lineMap.getElementIndex(chng.getOffset());
/* 244 */       if (line >= this.lastTokensOnLines.getSize()) {
/*     */         return;
/*     */       }
/*     */       
/* 248 */       int previousLine = line - 1;
/*     */       
/* 250 */       int previousTokenType = (previousLine > -1) ? this.lastTokensOnLines.get(previousLine) : 0;
/*     */ 
/*     */       
/* 253 */       updateLastTokensBelow(line, numLines, previousTokenType);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 258 */     super.fireRemoveUpdate(chng);
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
/*     */   public int getClosestStandardTokenTypeForInternalType(int type) {
/* 272 */     return this.tokenMaker.getClosestStandardTokenTypeForInternalType(type);
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
/*     */   public boolean getCompleteMarkupCloseTags() {
/* 286 */     return (getLanguageIsMarkup() && ((AbstractMarkupTokenMaker)this.tokenMaker)
/* 287 */       .getCompleteCloseTags());
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
/*     */   public boolean getCurlyBracesDenoteCodeBlocks(int languageIndex) {
/* 302 */     return this.tokenMaker.getCurlyBracesDenoteCodeBlocks(languageIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getLanguageIsMarkup() {
/* 313 */     return this.tokenMaker.isMarkupLanguage();
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
/*     */   public int getLastTokenTypeOnLine(int line) {
/* 325 */     return this.lastTokensOnLines.get(line);
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
/*     */   public String[] getLineCommentStartAndEnd(int languageIndex) {
/* 340 */     return this.tokenMaker.getLineCommentStartAndEnd(languageIndex);
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
/*     */   boolean getMarkOccurrencesOfTokenType(int type) {
/* 353 */     return this.tokenMaker.getMarkOccurrencesOfTokenType(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   OccurrenceMarker getOccurrenceMarker() {
/* 363 */     return this.tokenMaker.getOccurrenceMarker();
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
/*     */   public boolean getShouldIndentNextLine(int line) {
/* 375 */     Token t = getTokenListForLine(line);
/* 376 */     t = t.getLastNonCommentNonWhitespaceToken();
/* 377 */     return this.tokenMaker.getShouldIndentNextLineAfter(t);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSyntaxStyle() {
/* 388 */     return this.syntaxStyle;
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
/*     */   public final Token getTokenListForLine(int line) {
/* 404 */     this.tokenRetrievalCount++;
/* 405 */     if (line == this.lastLine && this.cachedTokenList != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 411 */       return this.cachedTokenList;
/*     */     }
/* 413 */     this.lastLine = line;
/*     */     
/* 415 */     Element map = getDefaultRootElement();
/* 416 */     Element elem = map.getElement(line);
/* 417 */     int startOffset = elem.getStartOffset();
/*     */ 
/*     */     
/* 420 */     int endOffset = elem.getEndOffset() - 1;
/*     */     try {
/* 422 */       getText(startOffset, endOffset - startOffset, this.s);
/* 423 */     } catch (BadLocationException ble) {
/* 424 */       ble.printStackTrace();
/* 425 */       return null;
/*     */     } 
/*     */     
/* 428 */     int initialTokenType = (line == 0) ? 0 : getLastTokenTypeOnLine(line - 1);
/*     */ 
/*     */     
/* 431 */     this.cachedTokenList = this.tokenMaker.getTokenList(this.s, initialTokenType, startOffset);
/* 432 */     return this.cachedTokenList;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean insertBreakSpecialHandling(ActionEvent e) {
/* 438 */     Action a = this.tokenMaker.getInsertBreakAction();
/* 439 */     if (a != null) {
/* 440 */       a.actionPerformed(e);
/* 441 */       return true;
/*     */     } 
/* 443 */     return false;
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
/*     */   public boolean isIdentifierChar(int languageIndex, char ch) {
/* 457 */     return this.tokenMaker.isIdentifierChar(languageIndex, ch);
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
/*     */   public Iterator<Token> iterator() {
/* 473 */     return new TokenIterator(this);
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
/*     */   private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
/* 487 */     in.defaultReadObject();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 492 */     setTokenMakerFactory((TokenMakerFactory)null);
/*     */ 
/*     */     
/* 495 */     this.s = new Segment();
/* 496 */     int lineCount = getDefaultRootElement().getElementCount();
/* 497 */     this.lastTokensOnLines = new DynamicIntArray(lineCount);
/* 498 */     setSyntaxStyle(this.syntaxStyle);
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
/*     */   private void setSharedSegment(int line) {
/* 512 */     Element map = getDefaultRootElement();
/*     */ 
/*     */     
/* 515 */     Element element = map.getElement(line);
/* 516 */     if (element == null) {
/* 517 */       throw new InternalError("Invalid line number: " + line);
/*     */     }
/* 519 */     int startOffset = element.getStartOffset();
/*     */ 
/*     */     
/* 522 */     int endOffset = element.getEndOffset() - 1;
/*     */     try {
/* 524 */       getText(startOffset, endOffset - startOffset, this.s);
/* 525 */     } catch (BadLocationException ble) {
/* 526 */       throw new InternalError("Text range not in document: " + startOffset + "-" + endOffset);
/*     */     } 
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
/*     */   public void setSyntaxStyle(String styleKey) {
/* 547 */     this.tokenMaker = this.tokenMakerFactory.getTokenMaker(styleKey);
/* 548 */     updateSyntaxHighlightingInformation();
/* 549 */     this.syntaxStyle = styleKey;
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
/*     */   public void setSyntaxStyle(TokenMaker tokenMaker) {
/* 563 */     this.tokenMaker = tokenMaker;
/* 564 */     updateSyntaxHighlightingInformation();
/* 565 */     this.syntaxStyle = "text/unknown";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTokenMakerFactory(TokenMakerFactory tmf) {
/* 576 */     this
/* 577 */       .tokenMakerFactory = (tmf != null) ? tmf : TokenMakerFactory.getDefaultInstance();
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
/*     */   private int updateLastTokensBelow(int line, int numLines, int previousTokenType) {
/* 595 */     int firstLine = line;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 601 */     int end = numLines;
/*     */     
/* 603 */     while (line < end) {
/*     */       
/* 605 */       setSharedSegment(line);
/*     */       
/* 607 */       int oldTokenType = this.lastTokensOnLines.get(line);
/* 608 */       int newTokenType = this.tokenMaker.getLastTokenTypeOnLine(this.s, previousTokenType);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 616 */       if (oldTokenType == newTokenType) {
/*     */         
/* 618 */         fireChangedUpdate(new AbstractDocument.DefaultDocumentEvent((AbstractDocument)this, firstLine, line, DocumentEvent.EventType.CHANGE));
/* 619 */         return line;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 626 */       this.lastTokensOnLines.setUnsafe(line, newTokenType);
/* 627 */       previousTokenType = newTokenType;
/* 628 */       line++;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 638 */     if (line > firstLine)
/*     */     {
/* 640 */       fireChangedUpdate(new AbstractDocument.DefaultDocumentEvent((AbstractDocument)this, firstLine, line, DocumentEvent.EventType.CHANGE));
/*     */     }
/*     */ 
/*     */     
/* 644 */     return line;
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
/*     */   private void updateSyntaxHighlightingInformation() {
/* 661 */     Element map = getDefaultRootElement();
/* 662 */     int numLines = map.getElementCount();
/* 663 */     int lastTokenType = 0;
/* 664 */     for (int i = 0; i < numLines; i++) {
/* 665 */       setSharedSegment(i);
/* 666 */       lastTokenType = this.tokenMaker.getLastTokenTypeOnLine(this.s, lastTokenType);
/* 667 */       this.lastTokensOnLines.set(i, lastTokenType);
/*     */     } 
/*     */ 
/*     */     
/* 671 */     this.lastLine = -1;
/* 672 */     this.cachedTokenList = null;
/*     */ 
/*     */     
/* 675 */     fireChangedUpdate(new AbstractDocument.DefaultDocumentEvent((AbstractDocument)this, 0, numLines - 1, DocumentEvent.EventType.CHANGE));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\RSyntaxDocument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */