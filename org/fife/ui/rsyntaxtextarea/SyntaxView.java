/*      */ package org.fife.ui.rsyntaxtextarea;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import javax.swing.text.Element;
/*      */ import javax.swing.text.JTextComponent;
/*      */ import javax.swing.text.Position;
/*      */ import javax.swing.text.TabExpander;
/*      */ import javax.swing.text.View;
/*      */ import javax.swing.text.ViewFactory;
/*      */ import org.fife.ui.rsyntaxtextarea.folding.Fold;
/*      */ import org.fife.ui.rsyntaxtextarea.folding.FoldManager;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class SyntaxView
/*      */   extends View
/*      */   implements TabExpander, TokenOrientedView, RSTAView
/*      */ {
/*      */   private Font font;
/*      */   private FontMetrics metrics;
/*      */   private Element longLine;
/*      */   private float longLineWidth;
/*      */   private int tabSize;
/*      */   private int tabBase;
/*      */   private RSyntaxTextArea host;
/*   69 */   private int lineHeight = 0;
/*      */ 
/*      */ 
/*      */   
/*      */   private int ascent;
/*      */ 
/*      */ 
/*      */   
/*      */   private int clipStart;
/*      */ 
/*      */   
/*      */   private int clipEnd;
/*      */ 
/*      */   
/*      */   private TokenImpl tempToken;
/*      */ 
/*      */ 
/*      */   
/*      */   public SyntaxView(Element elem) {
/*   88 */     super(elem);
/*   89 */     this.tempToken = new TokenImpl();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void calculateLongestLine() {
/*  102 */     Component c = getContainer();
/*  103 */     this.font = c.getFont();
/*  104 */     this.metrics = c.getFontMetrics(this.font);
/*  105 */     this.tabSize = getTabSize() * this.metrics.charWidth(' ');
/*  106 */     Element lines = getElement();
/*  107 */     int n = lines.getElementCount();
/*  108 */     for (int i = 0; i < n; i++) {
/*  109 */       Element line = lines.getElement(i);
/*  110 */       float w = getLineWidth(i);
/*  111 */       if (w > this.longLineWidth) {
/*  112 */         this.longLineWidth = w;
/*  113 */         this.longLine = line;
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
/*      */   public void changedUpdate(DocumentEvent changes, Shape a, ViewFactory f) {
/*  130 */     updateDamage(changes, a, f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void damageLineRange(int line0, int line1, Shape a, Component host) {
/*  146 */     if (a != null) {
/*  147 */       Rectangle area0 = lineToRect(a, line0);
/*  148 */       Rectangle area1 = lineToRect(a, line1);
/*  149 */       if (area0 != null && area1 != null) {
/*  150 */         Rectangle dmg = area0.union(area1);
/*  151 */         host.repaint(dmg.x, dmg.y, dmg.width, dmg.height);
/*      */       } else {
/*      */         
/*  154 */         host.repaint();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private float drawLine(TokenPainter painter, Token token, Graphics2D g, float x, float y, int line) {
/*  176 */     float nextX = x;
/*  177 */     boolean paintBG = this.host.getPaintTokenBackgrounds(line, y);
/*      */     
/*  179 */     while (token != null && token.isPaintable() && nextX < this.clipEnd) {
/*  180 */       nextX = painter.paint(token, g, nextX, y, this.host, this, this.clipStart, paintBG);
/*      */       
/*  182 */       token = token.getNextToken();
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  187 */     if (this.host.getEOLMarkersVisible()) {
/*  188 */       g.setColor(this.host.getForegroundForTokenType(21));
/*  189 */       g.setFont(this.host.getFontForTokenType(21));
/*  190 */       g.drawString("¶", nextX, y);
/*      */     } 
/*      */ 
/*      */     
/*  194 */     return nextX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private float drawLineWithSelection(TokenPainter painter, Token token, Graphics2D g, float x, float y, int selStart, int selEnd) {
/*  216 */     float nextX = x;
/*  217 */     boolean useSTC = this.host.getUseSelectedTextColor();
/*      */     
/*  219 */     while (token != null && token.isPaintable() && nextX < this.clipEnd) {
/*      */ 
/*      */       
/*  222 */       if (token.containsPosition(selStart)) {
/*      */         
/*  224 */         if (selStart > token.getOffset()) {
/*  225 */           this.tempToken.copyFrom(token);
/*  226 */           this.tempToken.textCount = selStart - this.tempToken.getOffset();
/*  227 */           nextX = painter.paint(this.tempToken, g, nextX, y, this.host, this, this.clipStart);
/*  228 */           this.tempToken.textCount = token.length();
/*  229 */           this.tempToken.makeStartAt(selStart);
/*      */ 
/*      */           
/*  232 */           token = new TokenImpl(this.tempToken);
/*      */         } 
/*      */         
/*  235 */         int tokenLen = token.length();
/*  236 */         int selCount = Math.min(tokenLen, selEnd - token.getOffset());
/*  237 */         if (selCount == tokenLen) {
/*  238 */           nextX = painter.paintSelected(token, g, nextX, y, this.host, this, this.clipStart, useSTC);
/*      */         }
/*      */         else {
/*      */           
/*  242 */           this.tempToken.copyFrom(token);
/*  243 */           this.tempToken.textCount = selCount;
/*  244 */           nextX = painter.paintSelected(this.tempToken, g, nextX, y, this.host, this, this.clipStart, useSTC);
/*      */           
/*  246 */           this.tempToken.textCount = token.length();
/*  247 */           this.tempToken.makeStartAt(token.getOffset() + selCount);
/*  248 */           token = this.tempToken;
/*  249 */           nextX = painter.paint(token, g, nextX, y, this.host, this, this.clipStart);
/*      */ 
/*      */         
/*      */         }
/*      */ 
/*      */       
/*      */       }
/*  256 */       else if (token.containsPosition(selEnd)) {
/*  257 */         this.tempToken.copyFrom(token);
/*  258 */         this.tempToken.textCount = selEnd - this.tempToken.getOffset();
/*  259 */         nextX = painter.paintSelected(this.tempToken, g, nextX, y, this.host, this, this.clipStart, useSTC);
/*      */         
/*  261 */         this.tempToken.textCount = token.length();
/*  262 */         this.tempToken.makeStartAt(selEnd);
/*  263 */         token = this.tempToken;
/*  264 */         nextX = painter.paint(token, g, nextX, y, this.host, this, this.clipStart);
/*      */ 
/*      */       
/*      */       }
/*  268 */       else if (token.getOffset() >= selStart && token
/*  269 */         .getEndOffset() <= selEnd) {
/*  270 */         nextX = painter.paintSelected(token, g, nextX, y, this.host, this, this.clipStart, useSTC);
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/*  276 */         nextX = painter.paint(token, g, nextX, y, this.host, this, this.clipStart);
/*      */       } 
/*      */       
/*  279 */       token = token.getNextToken();
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  285 */     if (this.host.getEOLMarkersVisible()) {
/*  286 */       g.setColor(this.host.getForegroundForTokenType(21));
/*  287 */       g.setFont(this.host.getFontForTokenType(21));
/*  288 */       g.drawString("¶", nextX, y);
/*      */     } 
/*      */ 
/*      */     
/*  292 */     return nextX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private float getLineWidth(int lineNumber) {
/*  305 */     Token tokenList = ((RSyntaxDocument)getDocument()).getTokenListForLine(lineNumber);
/*  306 */     return RSyntaxUtilities.getTokenListWidth(tokenList, (RSyntaxTextArea)
/*  307 */         getContainer(), this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getNextVisualPositionFrom(int pos, Position.Bias b, Shape a, int direction, Position.Bias[] biasRet) throws BadLocationException {
/*  333 */     return RSyntaxUtilities.getNextVisualPositionFrom(pos, b, a, direction, biasRet, this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getPreferredSpan(int axis) {
/*      */     float span;
/*      */     int visibleLineCount;
/*  351 */     updateMetrics();
/*  352 */     switch (axis) {
/*      */       case 0:
/*  354 */         span = this.longLineWidth + getRhsCorrection();
/*  355 */         if (this.host.getEOLMarkersVisible()) {
/*  356 */           span += this.metrics.charWidth('¶');
/*      */         }
/*  358 */         return span;
/*      */ 
/*      */ 
/*      */       
/*      */       case 1:
/*  363 */         this.lineHeight = (this.host != null) ? this.host.getLineHeight() : this.lineHeight;
/*      */         
/*  365 */         visibleLineCount = getElement().getElementCount();
/*  366 */         if (this.host.isCodeFoldingEnabled()) {
/*  367 */           visibleLineCount -= this.host.getFoldManager().getHiddenLineCount();
/*      */         }
/*  369 */         return visibleLineCount * this.lineHeight;
/*      */     } 
/*  371 */     throw new IllegalArgumentException("Invalid axis: " + axis);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int getRhsCorrection() {
/*  383 */     int rhsCorrection = 10;
/*  384 */     if (this.host != null) {
/*  385 */       rhsCorrection = this.host.getRightHandSideCorrection();
/*      */     }
/*  387 */     return rhsCorrection;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int getTabSize() {
/*  397 */     Integer i = (Integer)getDocument().getProperty("tabSize");
/*      */     
/*  399 */     int size = (i != null) ? i.intValue() : 5;
/*  400 */     return size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Token getTokenListForPhysicalLineAbove(int offset) {
/*  418 */     RSyntaxDocument document = (RSyntaxDocument)getDocument();
/*  419 */     Element map = document.getDefaultRootElement();
/*  420 */     int line = map.getElementIndex(offset);
/*  421 */     FoldManager fm = this.host.getFoldManager();
/*  422 */     if (fm == null) {
/*  423 */       line--;
/*  424 */       if (line >= 0) {
/*  425 */         return document.getTokenListForLine(line);
/*      */       }
/*      */     } else {
/*      */       
/*  429 */       line = fm.getVisibleLineAbove(line);
/*  430 */       if (line >= 0) {
/*  431 */         return document.getTokenListForLine(line);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  437 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Token getTokenListForPhysicalLineBelow(int offset) {
/*  455 */     RSyntaxDocument document = (RSyntaxDocument)getDocument();
/*  456 */     Element map = document.getDefaultRootElement();
/*  457 */     int lineCount = map.getElementCount();
/*  458 */     int line = map.getElementIndex(offset);
/*  459 */     if (!this.host.isCodeFoldingEnabled()) {
/*  460 */       if (line < lineCount - 1) {
/*  461 */         return document.getTokenListForLine(line + 1);
/*      */       }
/*      */     } else {
/*      */       
/*  465 */       FoldManager fm = this.host.getFoldManager();
/*  466 */       line = fm.getVisibleLineBelow(line);
/*  467 */       if (line >= 0 && line < lineCount) {
/*  468 */         return document.getTokenListForLine(line);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  475 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void insertUpdate(DocumentEvent changes, Shape a, ViewFactory f) {
/*  489 */     updateDamage(changes, a, f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Rectangle lineToRect(Shape a, int line) {
/*  501 */     Rectangle r = null;
/*  502 */     updateMetrics();
/*  503 */     if (this.metrics != null) {
/*  504 */       Rectangle alloc = a.getBounds();
/*      */ 
/*      */ 
/*      */       
/*  508 */       this.lineHeight = (this.host != null) ? this.host.getLineHeight() : this.lineHeight;
/*  509 */       if (this.host != null && this.host.isCodeFoldingEnabled()) {
/*  510 */         FoldManager fm = this.host.getFoldManager();
/*  511 */         int hiddenCount = fm.getHiddenLineCountAbove(line);
/*  512 */         line -= hiddenCount;
/*      */       } 
/*  514 */       r = new Rectangle(alloc.x, alloc.y + line * this.lineHeight, alloc.width, this.lineHeight);
/*      */     } 
/*      */     
/*  517 */     return r;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException {
/*  537 */     Element map = getElement();
/*  538 */     RSyntaxDocument doc = (RSyntaxDocument)getDocument();
/*  539 */     int lineIndex = map.getElementIndex(pos);
/*  540 */     Token tokenList = doc.getTokenListForLine(lineIndex);
/*  541 */     Rectangle lineArea = lineToRect(a, lineIndex);
/*  542 */     this.tabBase = lineArea.x;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  549 */     lineArea = tokenList.listOffsetToView((RSyntaxTextArea)
/*  550 */         getContainer(), this, pos, this.tabBase, lineArea);
/*      */ 
/*      */     
/*  553 */     return lineArea;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Shape modelToView(int p0, Position.Bias b0, int p1, Position.Bias b1, Shape a) throws BadLocationException {
/*  598 */     Shape s1, s0 = modelToView(p0, a, b0);
/*      */     
/*  600 */     if (p1 == getEndOffset()) {
/*      */       try {
/*  602 */         s1 = modelToView(p1, a, b1);
/*  603 */       } catch (BadLocationException ble) {
/*  604 */         s1 = null;
/*      */       } 
/*  606 */       if (s1 == null)
/*      */       {
/*      */         
/*  609 */         Rectangle alloc = (a instanceof Rectangle) ? (Rectangle)a : a.getBounds();
/*  610 */         s1 = new Rectangle(alloc.x + alloc.width - 1, alloc.y, 1, alloc.height);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  615 */       s1 = modelToView(p1, a, b1);
/*      */     } 
/*  617 */     Rectangle r0 = (s0 instanceof Rectangle) ? (Rectangle)s0 : s0.getBounds();
/*  618 */     Rectangle r1 = (s1 instanceof Rectangle) ? (Rectangle)s1 : s1.getBounds();
/*  619 */     if (r0.y != r1.y) {
/*      */ 
/*      */       
/*  622 */       Rectangle alloc = (a instanceof Rectangle) ? (Rectangle)a : a.getBounds();
/*  623 */       r0.x = alloc.x;
/*  624 */       r0.width = alloc.width;
/*      */     } 
/*      */     
/*  627 */     r0.add(r1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  635 */     if (p1 > p0) {
/*  636 */       r0.width -= r1.width;
/*      */     }
/*      */     
/*  639 */     return r0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float nextTabStop(float x, int tabOffset) {
/*  656 */     if (this.tabSize == 0) {
/*  657 */       return x;
/*      */     }
/*  659 */     int ntabs = ((int)x - this.tabBase) / this.tabSize;
/*  660 */     return this.tabBase + (ntabs + 1.0F) * this.tabSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void paint(Graphics g, Shape a) {
/*  674 */     RSyntaxDocument document = (RSyntaxDocument)getDocument();
/*      */     
/*  676 */     Rectangle alloc = a.getBounds();
/*      */     
/*  678 */     this.tabBase = alloc.x;
/*  679 */     this.host = (RSyntaxTextArea)getContainer();
/*      */     
/*  681 */     Rectangle clip = g.getClipBounds();
/*      */ 
/*      */ 
/*      */     
/*  685 */     this.clipStart = clip.x;
/*  686 */     this.clipEnd = this.clipStart + clip.width;
/*      */     
/*  688 */     this.lineHeight = this.host.getLineHeight();
/*  689 */     this.ascent = this.host.getMaxAscent();
/*  690 */     int heightAbove = clip.y - alloc.y;
/*  691 */     int linesAbove = Math.max(0, heightAbove / this.lineHeight);
/*      */     
/*  693 */     FoldManager fm = this.host.getFoldManager();
/*  694 */     linesAbove += fm.getHiddenLineCountAbove(linesAbove, true);
/*  695 */     Rectangle lineArea = lineToRect(a, linesAbove);
/*  696 */     int y = lineArea.y + this.ascent;
/*  697 */     int x = lineArea.x;
/*  698 */     Element map = getElement();
/*  699 */     int lineCount = map.getElementCount();
/*      */ 
/*      */     
/*  702 */     int selStart = this.host.getSelectionStart();
/*  703 */     int selEnd = this.host.getSelectionEnd();
/*      */ 
/*      */     
/*  706 */     RSyntaxTextAreaHighlighter h = (RSyntaxTextAreaHighlighter)this.host.getHighlighter();
/*      */     
/*  708 */     Graphics2D g2d = (Graphics2D)g;
/*      */ 
/*      */ 
/*      */     
/*  712 */     TokenPainter painter = this.host.getTokenPainter();
/*  713 */     int line = linesAbove;
/*      */     
/*  715 */     while (y < clip.y + clip.height + this.ascent && line < lineCount) {
/*      */       
/*  717 */       Fold fold = fm.getFoldForLine(line);
/*  718 */       Element lineElement = map.getElement(line);
/*  719 */       int startOffset = lineElement.getStartOffset();
/*      */ 
/*      */       
/*  722 */       int endOffset = lineElement.getEndOffset() - 1;
/*  723 */       h.paintLayeredHighlights(g2d, startOffset, endOffset, a, (JTextComponent)this.host, this);
/*      */ 
/*      */ 
/*      */       
/*  727 */       Token token = document.getTokenListForLine(line);
/*  728 */       if (selStart == selEnd || startOffset >= selEnd || endOffset < selStart) {
/*      */         
/*  730 */         drawLine(painter, token, g2d, x, y, line);
/*      */       }
/*      */       else {
/*      */         
/*  734 */         drawLineWithSelection(painter, token, g2d, x, y, selStart, selEnd);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  739 */       h.paintParserHighlights(g2d, startOffset, endOffset, a, (JTextComponent)this.host, this);
/*      */ 
/*      */       
/*  742 */       if (fold != null && fold.isCollapsed()) {
/*      */ 
/*      */         
/*  745 */         Color c = RSyntaxUtilities.getFoldedLineBottomColor(this.host);
/*  746 */         if (c != null) {
/*  747 */           g.setColor(c);
/*  748 */           g.drawLine(x, y + this.lineHeight - this.ascent - 1, this.host
/*  749 */               .getWidth(), y + this.lineHeight - this.ascent - 1);
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*      */         do {
/*  755 */           int hiddenLineCount = fold.getLineCount();
/*  756 */           if (hiddenLineCount == 0) {
/*      */             break;
/*      */           }
/*      */ 
/*      */ 
/*      */           
/*  762 */           line += hiddenLineCount;
/*  763 */           fold = fm.getFoldForLine(line);
/*  764 */         } while (fold != null && fold.isCollapsed());
/*      */       } 
/*      */ 
/*      */       
/*  768 */       y += this.lineHeight;
/*  769 */       line++;
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
/*      */   private boolean possiblyUpdateLongLine(Element line, int lineNumber) {
/*  788 */     float w = getLineWidth(lineNumber);
/*  789 */     if (w > this.longLineWidth) {
/*  790 */       this.longLineWidth = w;
/*  791 */       this.longLine = line;
/*  792 */       return true;
/*      */     } 
/*  794 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeUpdate(DocumentEvent changes, Shape a, ViewFactory f) {
/*  808 */     updateDamage(changes, a, f);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSize(float width, float height) {
/*  814 */     super.setSize(width, height);
/*  815 */     updateMetrics();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void updateDamage(DocumentEvent changes, Shape a, ViewFactory f) {
/*  828 */     Component host = getContainer();
/*  829 */     updateMetrics();
/*  830 */     Element elem = getElement();
/*  831 */     DocumentEvent.ElementChange ec = changes.getChange(elem);
/*  832 */     Element[] added = (ec != null) ? ec.getChildrenAdded() : null;
/*  833 */     Element[] removed = (ec != null) ? ec.getChildrenRemoved() : null;
/*  834 */     if ((added != null && added.length > 0) || (removed != null && removed.length > 0)) {
/*      */ 
/*      */       
/*  837 */       if (added != null) {
/*  838 */         int addedAt = ec.getIndex();
/*  839 */         for (int i = 0; i < added.length; i++) {
/*  840 */           possiblyUpdateLongLine(added[i], addedAt + i);
/*      */         }
/*      */       } 
/*  843 */       if (removed != null) {
/*  844 */         for (Element element : removed) {
/*  845 */           if (element == this.longLine) {
/*  846 */             this.longLineWidth = -1.0F;
/*  847 */             calculateLongestLine();
/*      */             break;
/*      */           } 
/*      */         } 
/*      */       }
/*  852 */       preferenceChanged(null, true, true);
/*  853 */       host.repaint();
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*  858 */     else if (changes.getType() == DocumentEvent.EventType.CHANGE) {
/*      */       
/*  860 */       int startLine = changes.getOffset();
/*  861 */       int endLine = changes.getLength();
/*  862 */       damageLineRange(startLine, endLine, a, host);
/*      */     }
/*      */     else {
/*      */       
/*  866 */       Element map = getElement();
/*  867 */       int line = map.getElementIndex(changes.getOffset());
/*  868 */       damageLineRange(line, line, a, host);
/*  869 */       if (changes.getType() == DocumentEvent.EventType.INSERT) {
/*      */ 
/*      */         
/*  872 */         Element e = map.getElement(line);
/*  873 */         if (e == this.longLine) {
/*      */ 
/*      */           
/*  876 */           this.longLineWidth = getLineWidth(line);
/*  877 */           preferenceChanged(null, true, false);
/*      */ 
/*      */         
/*      */         }
/*  881 */         else if (possiblyUpdateLongLine(e, line)) {
/*  882 */           preferenceChanged(null, true, false);
/*      */         }
/*      */       
/*      */       }
/*  886 */       else if (changes.getType() == DocumentEvent.EventType.REMOVE && 
/*  887 */         map.getElement(line) == this.longLine) {
/*      */         
/*  889 */         this.longLineWidth = -1.0F;
/*  890 */         calculateLongestLine();
/*  891 */         preferenceChanged(null, true, false);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateMetrics() {
/*  902 */     this.host = (RSyntaxTextArea)getContainer();
/*  903 */     Font f = this.host.getFont();
/*  904 */     if (this.font != f)
/*      */     {
/*      */       
/*  907 */       calculateLongestLine();
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
/*      */   public int viewToModel(float fx, float fy, Shape a, Position.Bias[] bias) {
/*  925 */     bias[0] = Position.Bias.Forward;
/*      */     
/*  927 */     Rectangle alloc = a.getBounds();
/*  928 */     RSyntaxDocument doc = (RSyntaxDocument)getDocument();
/*  929 */     int x = (int)fx;
/*  930 */     int y = (int)fy;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  935 */     if (y < alloc.y) {
/*  936 */       return getStartOffset();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  941 */     if (y > alloc.y + alloc.height) {
/*  942 */       return this.host.getLastVisibleOffset();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  952 */     Element map = doc.getDefaultRootElement();
/*  953 */     this.lineHeight = this.host.getLineHeight();
/*  954 */     int lineIndex = Math.abs((y - alloc.y) / this.lineHeight);
/*  955 */     FoldManager fm = this.host.getFoldManager();
/*      */     
/*  957 */     lineIndex += fm.getHiddenLineCountAbove(lineIndex, true);
/*      */     
/*  959 */     if (lineIndex >= map.getElementCount()) {
/*  960 */       return this.host.getLastVisibleOffset();
/*      */     }
/*      */     
/*  963 */     Element line = map.getElement(lineIndex);
/*      */ 
/*      */     
/*  966 */     if (x < alloc.x) {
/*  967 */       return line.getStartOffset();
/*      */     }
/*  969 */     if (x > alloc.x + alloc.width) {
/*  970 */       return line.getEndOffset() - 1;
/*      */     }
/*      */ 
/*      */     
/*  974 */     int p0 = line.getStartOffset();
/*  975 */     Token tokenList = doc.getTokenListForLine(lineIndex);
/*  976 */     this.tabBase = alloc.x;
/*  977 */     int offs = tokenList.getListOffset((RSyntaxTextArea)
/*  978 */         getContainer(), this, this.tabBase, x);
/*      */     
/*  980 */     return (offs != -1) ? offs : p0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int yForLine(Rectangle alloc, int line) throws BadLocationException {
/*  992 */     updateMetrics();
/*  993 */     if (this.metrics != null) {
/*      */ 
/*      */ 
/*      */       
/*  997 */       this.lineHeight = (this.host != null) ? this.host.getLineHeight() : this.lineHeight;
/*  998 */       if (this.host != null) {
/*  999 */         FoldManager fm = this.host.getFoldManager();
/* 1000 */         if (!fm.isLineHidden(line)) {
/* 1001 */           line -= fm.getHiddenLineCountAbove(line);
/* 1002 */           return alloc.y + line * this.lineHeight;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1007 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int yForLineContaining(Rectangle alloc, int offs) throws BadLocationException {
/* 1015 */     Element map = getElement();
/* 1016 */     int line = map.getElementIndex(offs);
/* 1017 */     return yForLine(alloc, line);
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\SyntaxView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */