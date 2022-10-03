/*      */ package org.fife.ui.rsyntaxtextarea;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import javax.swing.text.BoxView;
/*      */ import javax.swing.text.Document;
/*      */ import javax.swing.text.Element;
/*      */ import javax.swing.text.JTextComponent;
/*      */ import javax.swing.text.Position;
/*      */ import javax.swing.text.Segment;
/*      */ import javax.swing.text.TabExpander;
/*      */ import javax.swing.text.View;
/*      */ import javax.swing.text.ViewFactory;
/*      */ import org.fife.ui.rsyntaxtextarea.folding.Fold;
/*      */ import org.fife.ui.rsyntaxtextarea.folding.FoldManager;
/*      */ import org.fife.ui.rtextarea.Gutter;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class WrappedSyntaxView
/*      */   extends BoxView
/*      */   implements TabExpander, RSTAView
/*      */ {
/*      */   private int tabBase;
/*      */   private int tabSize;
/*      */   private Segment s;
/*      */   private Segment drawSeg;
/*      */   private Rectangle tempRect;
/*      */   private RSyntaxTextArea host;
/*      */   private FontMetrics metrics;
/*      */   private TokenImpl tempToken;
/*      */   private TokenImpl lineCountTempToken;
/*      */   private static final int MIN_WIDTH = 20;
/*      */   
/*      */   public WrappedSyntaxView(Element elem) {
/*   90 */     super(elem, 1);
/*   91 */     this.tempToken = new TokenImpl();
/*   92 */     this.s = new Segment();
/*   93 */     this.drawSeg = new Segment();
/*   94 */     this.tempRect = new Rectangle();
/*   95 */     this.lineCountTempToken = new TokenImpl();
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
/*      */   protected int calculateBreakPosition(int p0, Token tokenList, float x0) {
/*  110 */     int p = p0;
/*  111 */     RSyntaxTextArea textArea = (RSyntaxTextArea)getContainer();
/*  112 */     float currentWidth = getWidth();
/*  113 */     if (currentWidth == 2.14748365E9F) {
/*  114 */       currentWidth = getPreferredSpan(0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  121 */     currentWidth = Math.max(currentWidth, 20.0F);
/*  122 */     Token t = tokenList;
/*  123 */     while (t != null && t.isPaintable()) {
/*      */ 
/*      */ 
/*      */       
/*  127 */       float tokenWidth = t.getWidth(textArea, this, x0);
/*  128 */       if (tokenWidth > currentWidth) {
/*      */ 
/*      */         
/*  131 */         if (p == p0) {
/*  132 */           return t.getOffsetBeforeX(textArea, this, 0.0F, currentWidth);
/*      */         }
/*      */ 
/*      */         
/*  136 */         return t.isWhitespace() ? (p + t.length()) : p;
/*      */       } 
/*      */       
/*  139 */       currentWidth -= tokenWidth;
/*  140 */       x0 += tokenWidth;
/*  141 */       p += t.length();
/*      */       
/*  143 */       t = t.getNextToken();
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  149 */     return p + 1;
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
/*      */   public void changedUpdate(DocumentEvent e, Shape a, ViewFactory f) {
/*  172 */     updateChildren(e, a);
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
/*      */   private void childAllocation2(int line, int y, Rectangle alloc) {
/*  187 */     alloc.x += getOffset(0, line);
/*  188 */     alloc.y += y;
/*  189 */     alloc.width = getSpan(0, line);
/*  190 */     alloc.height = getSpan(1, line);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  195 */     Insets margin = this.host.getMargin();
/*  196 */     if (margin != null) {
/*  197 */       alloc.y -= margin.top;
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
/*      */   protected void drawView(TokenPainter painter, Graphics2D g, Rectangle r, View view, int fontHeight, int y, int line) {
/*  217 */     float x = r.x;
/*      */ 
/*      */     
/*  220 */     RSyntaxTextAreaHighlighter h = (RSyntaxTextAreaHighlighter)this.host.getHighlighter();
/*      */     
/*  222 */     RSyntaxDocument document = (RSyntaxDocument)getDocument();
/*  223 */     Element map = getElement();
/*      */     
/*  225 */     int p0 = view.getStartOffset();
/*  226 */     int lineNumber = map.getElementIndex(p0);
/*  227 */     int p1 = view.getEndOffset();
/*      */     
/*  229 */     setSegment(p0, p1 - 1, (Document)document, this.drawSeg);
/*      */     
/*  231 */     int start = p0 - this.drawSeg.offset;
/*  232 */     Token token = document.getTokenListForLine(lineNumber);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  237 */     if (token != null && token.getType() == 0) {
/*  238 */       h.paintLayeredHighlights(g, p0, p1, r, (JTextComponent)this.host, this);
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  243 */     while (token != null && token.isPaintable()) {
/*      */       
/*  245 */       int p = calculateBreakPosition(p0, token, x);
/*  246 */       x = r.x;
/*      */       
/*  248 */       h.paintLayeredHighlights(g, p0, p, r, (JTextComponent)this.host, this);
/*      */       
/*  250 */       while (token != null && token.isPaintable() && token.getEndOffset() - 1 < p) {
/*  251 */         boolean paintBG = this.host.getPaintTokenBackgrounds(line, y);
/*  252 */         x = painter.paint(token, g, x, y, this.host, this, 0.0F, paintBG);
/*  253 */         token = token.getNextToken();
/*      */       } 
/*      */       
/*  256 */       if (token != null && token.isPaintable() && token.getOffset() < p) {
/*  257 */         int tokenOffset = token.getOffset();
/*  258 */         this.tempToken.set(this.drawSeg.array, tokenOffset - start, p - 1 - start, tokenOffset, token
/*  259 */             .getType());
/*  260 */         this.tempToken.setLanguageIndex(token.getLanguageIndex());
/*  261 */         boolean paintBG = this.host.getPaintTokenBackgrounds(line, y);
/*  262 */         painter.paint(this.tempToken, g, x, y, this.host, this, 0.0F, paintBG);
/*  263 */         this.tempToken.copyFrom(token);
/*  264 */         this.tempToken.makeStartAt(p);
/*  265 */         token = new TokenImpl(this.tempToken);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  270 */       h.paintParserHighlights(g, p0, p, r, (JTextComponent)this.host, this);
/*      */       
/*  272 */       p0 = (p == p0) ? p1 : p;
/*  273 */       y += fontHeight;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  279 */     if (this.host.getEOLMarkersVisible()) {
/*  280 */       g.setColor(this.host.getForegroundForTokenType(21));
/*  281 */       g.setFont(this.host.getFontForTokenType(21));
/*  282 */       g.drawString("¶", x, y - fontHeight);
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
/*      */   protected void drawViewWithSelection(TokenPainter painter, Graphics2D g, Rectangle r, View view, int fontHeight, int y, int selStart, int selEnd) {
/*  306 */     float x = r.x;
/*  307 */     boolean useSTC = this.host.getUseSelectedTextColor();
/*      */ 
/*      */     
/*  310 */     RSyntaxTextAreaHighlighter h = (RSyntaxTextAreaHighlighter)this.host.getHighlighter();
/*      */     
/*  312 */     RSyntaxDocument document = (RSyntaxDocument)getDocument();
/*  313 */     Element map = getElement();
/*      */     
/*  315 */     int p0 = view.getStartOffset();
/*  316 */     int lineNumber = map.getElementIndex(p0);
/*  317 */     int p1 = view.getEndOffset();
/*      */     
/*  319 */     setSegment(p0, p1 - 1, (Document)document, this.drawSeg);
/*      */     
/*  321 */     int start = p0 - this.drawSeg.offset;
/*  322 */     Token token = document.getTokenListForLine(lineNumber);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  327 */     if (token != null && token.getType() == 0) {
/*  328 */       h.paintLayeredHighlights(g, p0, p1, r, (JTextComponent)this.host, this);
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  333 */     while (token != null && token.isPaintable()) {
/*      */       
/*  335 */       int p = calculateBreakPosition(p0, token, x);
/*  336 */       x = r.x;
/*      */       
/*  338 */       h.paintLayeredHighlights(g, p0, p, r, (JTextComponent)this.host, this);
/*      */       
/*  340 */       while (token != null && token.isPaintable() && token.getEndOffset() - 1 < p) {
/*      */ 
/*      */         
/*  343 */         if (token.containsPosition(selStart)) {
/*      */ 
/*      */           
/*  346 */           if (selStart > token.getOffset()) {
/*  347 */             this.tempToken.copyFrom(token);
/*  348 */             this.tempToken.textCount = selStart - this.tempToken.getOffset();
/*  349 */             x = painter.paint(this.tempToken, g, x, y, this.host, this);
/*  350 */             this.tempToken.textCount = token.length();
/*  351 */             this.tempToken.makeStartAt(selStart);
/*      */ 
/*      */             
/*  354 */             token = new TokenImpl(this.tempToken);
/*      */           } 
/*      */           
/*  357 */           int selCount = Math.min(token.length(), selEnd - token.getOffset());
/*  358 */           if (selCount == token.length()) {
/*  359 */             x = painter.paintSelected(token, g, x, y, this.host, this, useSTC);
/*      */           }
/*      */           else {
/*      */             
/*  363 */             this.tempToken.copyFrom(token);
/*  364 */             this.tempToken.textCount = selCount;
/*  365 */             x = painter.paintSelected(this.tempToken, g, x, y, this.host, this, useSTC);
/*      */             
/*  367 */             this.tempToken.textCount = token.length();
/*  368 */             this.tempToken.makeStartAt(token.getOffset() + selCount);
/*  369 */             token = this.tempToken;
/*  370 */             x = painter.paint(token, g, x, y, this.host, this);
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/*  376 */         else if (token.containsPosition(selEnd)) {
/*  377 */           this.tempToken.copyFrom(token);
/*  378 */           this.tempToken.textCount = selEnd - this.tempToken.getOffset();
/*  379 */           x = painter.paintSelected(this.tempToken, g, x, y, this.host, this, useSTC);
/*      */           
/*  381 */           this.tempToken.textCount = token.length();
/*  382 */           this.tempToken.makeStartAt(selEnd);
/*  383 */           token = this.tempToken;
/*  384 */           x = painter.paint(token, g, x, y, this.host, this);
/*      */ 
/*      */         
/*      */         }
/*  388 */         else if (token.getOffset() >= selStart && token
/*  389 */           .getEndOffset() <= selEnd) {
/*  390 */           x = painter.paintSelected(token, g, x, y, this.host, this, useSTC);
/*      */         
/*      */         }
/*      */         else {
/*      */           
/*  395 */           x = painter.paint(token, g, x, y, this.host, this);
/*      */         } 
/*      */         
/*  398 */         token = token.getNextToken();
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  403 */       if (token != null && token.isPaintable() && token.getOffset() < p) {
/*      */         
/*  405 */         int tokenOffset = token.getOffset();
/*  406 */         Token orig = token;
/*      */         
/*  408 */         token = new TokenImpl(this.drawSeg, tokenOffset - start, p - 1 - start, tokenOffset, token.getType(), token.getLanguageIndex());
/*  409 */         token.setLanguageIndex(token.getLanguageIndex());
/*      */ 
/*      */         
/*  412 */         if (token.containsPosition(selStart)) {
/*      */           
/*  414 */           if (selStart > token.getOffset()) {
/*  415 */             this.tempToken.copyFrom(token);
/*  416 */             this.tempToken.textCount = selStart - this.tempToken.getOffset();
/*  417 */             x = painter.paint(this.tempToken, g, x, y, this.host, this);
/*  418 */             this.tempToken.textCount = token.length();
/*  419 */             this.tempToken.makeStartAt(selStart);
/*      */ 
/*      */             
/*  422 */             token = new TokenImpl(this.tempToken);
/*      */           } 
/*      */           
/*  425 */           int selCount = Math.min(token.length(), selEnd - token.getOffset());
/*  426 */           if (selCount == token.length()) {
/*  427 */             x = painter.paintSelected(token, g, x, y, this.host, this, useSTC);
/*      */           }
/*      */           else {
/*      */             
/*  431 */             this.tempToken.copyFrom(token);
/*  432 */             this.tempToken.textCount = selCount;
/*  433 */             x = painter.paintSelected(this.tempToken, g, x, y, this.host, this, useSTC);
/*      */             
/*  435 */             this.tempToken.textCount = token.length();
/*  436 */             this.tempToken.makeStartAt(token.getOffset() + selCount);
/*  437 */             token = this.tempToken;
/*  438 */             x = painter.paint(token, g, x, y, this.host, this);
/*      */           
/*      */           }
/*      */ 
/*      */         
/*      */         }
/*  444 */         else if (token.containsPosition(selEnd)) {
/*  445 */           this.tempToken.copyFrom(token);
/*  446 */           this.tempToken.textCount = selEnd - this.tempToken.getOffset();
/*  447 */           x = painter.paintSelected(this.tempToken, g, x, y, this.host, this, useSTC);
/*      */           
/*  449 */           this.tempToken.textCount = token.length();
/*  450 */           this.tempToken.makeStartAt(selEnd);
/*  451 */           token = this.tempToken;
/*  452 */           x = painter.paint(token, g, x, y, this.host, this);
/*      */ 
/*      */         
/*      */         }
/*  456 */         else if (token.getOffset() >= selStart && token
/*  457 */           .getEndOffset() <= selEnd) {
/*  458 */           x = painter.paintSelected(token, g, x, y, this.host, this, useSTC);
/*      */         
/*      */         }
/*      */         else {
/*      */           
/*  463 */           x = painter.paint(token, g, x, y, this.host, this);
/*      */         } 
/*      */         
/*  466 */         token = new TokenImpl(orig);
/*  467 */         ((TokenImpl)token).makeStartAt(p);
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  473 */       h.paintParserHighlights(g, p0, p, r, (JTextComponent)this.host, this);
/*      */       
/*  475 */       p0 = (p == p0) ? p1 : p;
/*  476 */       y += fontHeight;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  482 */     if (this.host.getEOLMarkersVisible()) {
/*  483 */       g.setColor(this.host.getForegroundForTokenType(21));
/*  484 */       g.setFont(this.host.getFontForTokenType(21));
/*  485 */       g.drawString("¶", x, y - fontHeight);
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
/*      */   public Shape getChildAllocation(int index, Shape a) {
/*  503 */     if (a != null) {
/*  504 */       Shape ca = getChildAllocationImpl(index, a);
/*  505 */       if (ca != null && !isAllocationValid()) {
/*      */ 
/*      */         
/*  508 */         Rectangle r = (ca instanceof Rectangle) ? (Rectangle)ca : ca.getBounds();
/*  509 */         if (r.width == 0 && r.height == 0) {
/*  510 */           return null;
/*      */         }
/*      */       } 
/*  513 */       return ca;
/*      */     } 
/*  515 */     return null;
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
/*      */   public Shape getChildAllocationImpl(int line, Shape a) {
/*  528 */     Rectangle alloc = getInsideAllocation(a);
/*  529 */     this.host = (RSyntaxTextArea)getContainer();
/*  530 */     FoldManager fm = this.host.getFoldManager();
/*  531 */     int y = alloc.y;
/*      */ 
/*      */ 
/*      */     
/*  535 */     for (int i = 0; i < line; i++) {
/*  536 */       y += getSpan(1, i);
/*  537 */       Fold fold = fm.getFoldForLine(i);
/*  538 */       if (fold != null && fold.isCollapsed()) {
/*  539 */         i += fold.getCollapsedLineCount();
/*      */       }
/*      */     } 
/*      */     
/*  543 */     childAllocation2(line, y, alloc);
/*  544 */     return alloc;
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
/*      */   public float getMaximumSpan(int axis) {
/*  566 */     updateMetrics();
/*  567 */     float span = super.getPreferredSpan(axis);
/*  568 */     if (axis == 0) {
/*  569 */       span += this.metrics.charWidth('¶');
/*      */     }
/*  571 */     return span;
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
/*      */   public float getMinimumSpan(int axis) {
/*  592 */     updateMetrics();
/*  593 */     float span = super.getPreferredSpan(axis);
/*  594 */     if (axis == 0) {
/*  595 */       span += this.metrics.charWidth('¶');
/*      */     }
/*  597 */     return span;
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
/*      */   public float getPreferredSpan(int axis) {
/*  618 */     updateMetrics();
/*  619 */     float span = 0.0F;
/*  620 */     if (axis == 0) {
/*  621 */       span = super.getPreferredSpan(axis);
/*  622 */       span += this.metrics.charWidth('¶');
/*      */     } else {
/*      */       
/*  625 */       span = super.getPreferredSpan(axis);
/*  626 */       this.host = (RSyntaxTextArea)getContainer();
/*  627 */       if (this.host.isCodeFoldingEnabled()) {
/*      */ 
/*      */         
/*  630 */         int lineCount = this.host.getLineCount();
/*  631 */         FoldManager fm = this.host.getFoldManager();
/*  632 */         for (int i = 0; i < lineCount; i++) {
/*  633 */           if (fm.isLineHidden(i)) {
/*  634 */             span -= getSpan(1, i);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/*  639 */     return span;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int getTabSize() {
/*  650 */     Integer i = (Integer)getDocument().getProperty("tabSize");
/*  651 */     int size = (i != null) ? i.intValue() : 5;
/*  652 */     return size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected View getViewAtPoint(int x, int y, Rectangle alloc) {
/*  662 */     int lineCount = getViewCount();
/*  663 */     int curY = alloc.y + getOffset(1, 0);
/*  664 */     this.host = (RSyntaxTextArea)getContainer();
/*  665 */     FoldManager fm = this.host.getFoldManager();
/*      */     
/*  667 */     for (int line = 1; line < lineCount; line++) {
/*  668 */       int span = getSpan(1, line - 1);
/*  669 */       if (y < curY + span) {
/*  670 */         childAllocation2(line - 1, curY, alloc);
/*  671 */         return getView(line - 1);
/*      */       } 
/*  673 */       curY += span;
/*  674 */       Fold fold = fm.getFoldForLine(line - 1);
/*  675 */       if (fold != null && fold.isCollapsed()) {
/*  676 */         line += fold.getCollapsedLineCount();
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  681 */     childAllocation2(lineCount - 1, curY, alloc);
/*  682 */     return getView(lineCount - 1);
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
/*      */   public void insertUpdate(DocumentEvent changes, Shape a, ViewFactory f) {
/*  699 */     updateChildren(changes, a);
/*      */     
/*  701 */     Rectangle alloc = (a != null && isAllocationValid()) ? getInsideAllocation(a) : null;
/*  702 */     int pos = changes.getOffset();
/*  703 */     View v = getViewAtPosition(pos, alloc);
/*  704 */     if (v != null) {
/*  705 */       v.insertUpdate(changes, alloc, f);
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
/*      */   protected void loadChildren(ViewFactory f) {
/*  722 */     Element e = getElement();
/*  723 */     int n = e.getElementCount();
/*  724 */     if (n > 0) {
/*  725 */       View[] added = new View[n];
/*  726 */       for (int i = 0; i < n; i++) {
/*  727 */         added[i] = new WrappedLine(e.getElement(i));
/*      */       }
/*  729 */       replace(0, 0, added);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException {
/*  738 */     if (!isAllocationValid()) {
/*  739 */       Rectangle alloc = a.getBounds();
/*  740 */       setSize(alloc.width, alloc.height);
/*      */     } 
/*      */     
/*  743 */     boolean isBackward = (b == Position.Bias.Backward);
/*  744 */     int testPos = isBackward ? Math.max(0, pos - 1) : pos;
/*  745 */     if (isBackward && testPos < getStartOffset()) {
/*  746 */       return null;
/*      */     }
/*      */     
/*  749 */     int vIndex = getViewIndexAtPosition(testPos);
/*  750 */     if (vIndex != -1 && vIndex < getViewCount()) {
/*  751 */       View v = getView(vIndex);
/*  752 */       if (v != null && testPos >= v.getStartOffset() && testPos < v
/*  753 */         .getEndOffset()) {
/*  754 */         Shape childShape = getChildAllocation(vIndex, a);
/*  755 */         if (childShape == null)
/*      */         {
/*  757 */           return null;
/*      */         }
/*  759 */         Shape retShape = v.modelToView(pos, childShape, b);
/*  760 */         if (retShape == null && v.getEndOffset() == pos && 
/*  761 */           ++vIndex < getViewCount()) {
/*  762 */           v = getView(vIndex);
/*  763 */           retShape = v.modelToView(pos, getChildAllocation(vIndex, a), b);
/*      */         } 
/*      */         
/*  766 */         return retShape;
/*      */       } 
/*      */     } 
/*      */     
/*  770 */     throw new BadLocationException("Position not represented by view", pos);
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
/*  815 */     Shape s1, s0 = modelToView(p0, a, b0);
/*      */     
/*  817 */     if (p1 == getEndOffset()) {
/*      */       try {
/*  819 */         s1 = modelToView(p1, a, b1);
/*  820 */       } catch (BadLocationException ble) {
/*  821 */         s1 = null;
/*      */       } 
/*  823 */       if (s1 == null)
/*      */       {
/*      */         
/*  826 */         Rectangle alloc = (a instanceof Rectangle) ? (Rectangle)a : a.getBounds();
/*  827 */         s1 = new Rectangle(alloc.x + alloc.width - 1, alloc.y, 1, alloc.height);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  832 */       s1 = modelToView(p1, a, b1);
/*      */     } 
/*  834 */     Rectangle r0 = s0.getBounds();
/*      */     
/*  836 */     Rectangle r1 = (s1 instanceof Rectangle) ? (Rectangle)s1 : s1.getBounds();
/*  837 */     if (r0.y != r1.y) {
/*      */ 
/*      */       
/*  840 */       Rectangle alloc = (a instanceof Rectangle) ? (Rectangle)a : a.getBounds();
/*  841 */       r0.x = alloc.x;
/*  842 */       r0.width = alloc.width;
/*      */     } 
/*      */     
/*  845 */     r0.add(r1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  853 */     if (p1 > p0) {
/*  854 */       r0.width -= r1.width;
/*      */     }
/*  856 */     return r0;
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
/*  873 */     if (this.tabSize == 0) {
/*  874 */       return x;
/*      */     }
/*  876 */     int ntabs = ((int)x - this.tabBase) / this.tabSize;
/*  877 */     return this.tabBase + (ntabs + 1.0F) * this.tabSize;
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
/*  891 */     Rectangle alloc = (a instanceof Rectangle) ? (Rectangle)a : a.getBounds();
/*  892 */     this.tabBase = alloc.x;
/*      */     
/*  894 */     Graphics2D g2d = (Graphics2D)g;
/*  895 */     this.host = (RSyntaxTextArea)getContainer();
/*  896 */     int ascent = this.host.getMaxAscent();
/*  897 */     int fontHeight = this.host.getLineHeight();
/*  898 */     FoldManager fm = this.host.getFoldManager();
/*  899 */     TokenPainter painter = this.host.getTokenPainter();
/*  900 */     Element root = getElement();
/*      */ 
/*      */     
/*  903 */     int selStart = this.host.getSelectionStart();
/*  904 */     int selEnd = this.host.getSelectionEnd();
/*      */     
/*  906 */     int n = getViewCount();
/*  907 */     int x = alloc.x + getLeftInset();
/*  908 */     alloc.y += getTopInset();
/*  909 */     Rectangle clip = g.getClipBounds();
/*  910 */     for (int i = 0; i < n; i++) {
/*      */       
/*  912 */       this.tempRect.x = x + getOffset(0, i);
/*      */       
/*  914 */       this.tempRect.width = getSpan(0, i);
/*  915 */       this.tempRect.height = getSpan(1, i);
/*      */ 
/*      */       
/*  918 */       if (this.tempRect.intersects(clip)) {
/*  919 */         Element lineElement = root.getElement(i);
/*  920 */         int startOffset = lineElement.getStartOffset();
/*  921 */         int endOffset = lineElement.getEndOffset() - 1;
/*  922 */         View view = getView(i);
/*  923 */         if (selStart == selEnd || startOffset >= selEnd || endOffset < selStart) {
/*      */           
/*  925 */           drawView(painter, g2d, alloc, view, fontHeight, this.tempRect.y + ascent, i);
/*      */         
/*      */         }
/*      */         else {
/*      */           
/*  930 */           drawViewWithSelection(painter, g2d, alloc, view, fontHeight, this.tempRect.y + ascent, selStart, selEnd);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  935 */       this.tempRect.y += this.tempRect.height;
/*      */       
/*  937 */       Fold possibleFold = fm.getFoldForLine(i);
/*  938 */       if (possibleFold != null && possibleFold.isCollapsed()) {
/*  939 */         i += possibleFold.getCollapsedLineCount();
/*      */         
/*  941 */         Color c = RSyntaxUtilities.getFoldedLineBottomColor(this.host);
/*  942 */         if (c != null) {
/*  943 */           g.setColor(c);
/*  944 */           g.drawLine(x, this.tempRect.y - 1, this.host.getWidth(), this.tempRect.y - 1);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeUpdate(DocumentEvent changes, Shape a, ViewFactory f) {
/*  966 */     updateChildren(changes, a);
/*      */ 
/*      */     
/*  969 */     Rectangle alloc = (a != null && isAllocationValid()) ? getInsideAllocation(a) : null;
/*  970 */     int pos = changes.getOffset();
/*  971 */     View v = getViewAtPosition(pos, alloc);
/*  972 */     if (v != null) {
/*  973 */       v.removeUpdate(changes, alloc, f);
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
/*      */   private void setSegment(int p0, int p1, Document document, Segment seg) {
/*      */     try {
/*  993 */       document.getText(p0, p1 - p0, seg);
/*      */     }
/*  995 */     catch (BadLocationException ble) {
/*  996 */       ble.printStackTrace();
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
/*      */   public void setSize(float width, float height) {
/* 1010 */     updateMetrics();
/* 1011 */     if ((int)width != getWidth()) {
/*      */ 
/*      */       
/* 1014 */       preferenceChanged((View)null, true, true);
/* 1015 */       setWidthChangePending(true);
/*      */     } 
/* 1017 */     super.setSize(width, height);
/* 1018 */     setWidthChangePending(false);
/*      */   }
/*      */ 
/*      */   
/*      */   private void setWidthChangePending(boolean widthChangePending) {
/* 1023 */     int count = getViewCount();
/* 1024 */     for (int i = 0; i < count; i++) {
/* 1025 */       View v = getView(i);
/* 1026 */       if (v instanceof WrappedLine) {
/* 1027 */         ((WrappedLine)v).widthChangePending = widthChangePending;
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
/*      */   void updateChildren(DocumentEvent e, Shape a) {
/* 1039 */     Element elem = getElement();
/* 1040 */     DocumentEvent.ElementChange ec = e.getChange(elem);
/*      */ 
/*      */ 
/*      */     
/* 1044 */     if (e.getType() == DocumentEvent.EventType.CHANGE) {
/*      */ 
/*      */       
/* 1047 */       getContainer().repaint();
/*      */ 
/*      */     
/*      */     }
/* 1051 */     else if (ec != null) {
/*      */ 
/*      */       
/* 1054 */       Element[] removedElems = ec.getChildrenRemoved();
/* 1055 */       Element[] addedElems = ec.getChildrenAdded();
/* 1056 */       View[] added = new View[addedElems.length];
/*      */       
/* 1058 */       for (int i = 0; i < addedElems.length; i++) {
/* 1059 */         added[i] = new WrappedLine(addedElems[i]);
/*      */       }
/*      */ 
/*      */       
/* 1063 */       replace(ec.getIndex(), removedElems.length, added);
/*      */ 
/*      */       
/* 1066 */       if (a != null) {
/* 1067 */         preferenceChanged((View)null, true, true);
/* 1068 */         getContainer().repaint();
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1074 */     updateMetrics();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final void updateMetrics() {
/* 1080 */     Component host = getContainer();
/* 1081 */     Font f = host.getFont();
/* 1082 */     this.metrics = host.getFontMetrics(f);
/* 1083 */     this.tabSize = getTabSize() * this.metrics.charWidth('m');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int viewToModel(float x, float y, Shape a, Position.Bias[] bias) {
/* 1090 */     int offs = -1;
/*      */     
/* 1092 */     if (!isAllocationValid()) {
/* 1093 */       Rectangle rectangle = a.getBounds();
/* 1094 */       setSize(rectangle.width, rectangle.height);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1099 */     Rectangle alloc = getInsideAllocation(a);
/* 1100 */     View v = getViewAtPoint((int)x, (int)y, alloc);
/* 1101 */     if (v != null) {
/* 1102 */       offs = v.viewToModel(x, y, alloc, bias);
/*      */ 
/*      */ 
/*      */       
/* 1106 */       if (this.host.isCodeFoldingEnabled() && v == getView(getViewCount() - 1) && offs == v
/* 1107 */         .getEndOffset() - 1) {
/* 1108 */         offs = this.host.getLastVisibleOffset();
/*      */       }
/*      */     } 
/* 1111 */     return offs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int yForLine(Rectangle alloc, int line) throws BadLocationException {
/* 1118 */     return yForLineContaining(alloc, 
/* 1119 */         getElement().getElement(line).getStartOffset());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int yForLineContaining(Rectangle alloc, int offs) throws BadLocationException {
/* 1127 */     if (isAllocationValid()) {
/*      */ 
/*      */       
/* 1130 */       Rectangle r = (Rectangle)modelToView(offs, alloc, Position.Bias.Forward);
/* 1131 */       if (r != null) {
/* 1132 */         if (this.host.isCodeFoldingEnabled()) {
/* 1133 */           int line = this.host.getLineOfOffset(offs);
/* 1134 */           FoldManager fm = this.host.getFoldManager();
/* 1135 */           if (fm.isLineHidden(line)) {
/* 1136 */             return -1;
/*      */           }
/*      */         } 
/* 1139 */         return r.y;
/*      */       } 
/*      */     } 
/* 1142 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class WrappedLine
/*      */     extends View
/*      */   {
/*      */     private int nlines;
/*      */ 
/*      */     
/*      */     private boolean widthChangePending;
/*      */ 
/*      */ 
/*      */     
/*      */     WrappedLine(Element elem) {
/* 1159 */       super(elem);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final int calculateLineCount() {
/* 1168 */       int nlines = 0;
/* 1169 */       int startOffset = getStartOffset();
/* 1170 */       int p1 = getEndOffset();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1175 */       RSyntaxTextArea textArea = (RSyntaxTextArea)getContainer();
/* 1176 */       RSyntaxDocument doc = (RSyntaxDocument)getDocument();
/* 1177 */       Element map = doc.getDefaultRootElement();
/* 1178 */       int line = map.getElementIndex(startOffset);
/* 1179 */       Token tokenList = doc.getTokenListForLine(line);
/* 1180 */       float x0 = 0.0F;
/*      */       
/*      */       int p0;
/*      */       
/* 1184 */       for (p0 = startOffset; p0 < p1; ) {
/*      */         
/* 1186 */         nlines++;
/* 1187 */         TokenUtils.TokenSubList subList = TokenUtils.getSubTokenList(tokenList, p0, WrappedSyntaxView.this, textArea, x0, WrappedSyntaxView.this
/* 1188 */             .lineCountTempToken);
/* 1189 */         x0 = (subList != null) ? subList.x : x0;
/* 1190 */         tokenList = (subList != null) ? subList.tokenList : null;
/* 1191 */         int p = WrappedSyntaxView.this.calculateBreakPosition(p0, tokenList, x0);
/*      */ 
/*      */         
/* 1194 */         p0 = (p == p0) ? ++p : p;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1210 */       return nlines;
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
/*      */     public float getPreferredSpan(int axis) {
/*      */       float width;
/* 1226 */       switch (axis) {
/*      */         case 0:
/* 1228 */           width = WrappedSyntaxView.this.getWidth();
/* 1229 */           if (width == 2.14748365E9F)
/*      */           {
/*      */             
/* 1232 */             return 100.0F;
/*      */           }
/* 1234 */           return width;
/*      */         case 1:
/* 1236 */           if (this.nlines == 0 || this.widthChangePending) {
/* 1237 */             this.nlines = calculateLineCount();
/* 1238 */             this.widthChangePending = false;
/*      */           } 
/* 1240 */           return (this.nlines * ((RSyntaxTextArea)getContainer()).getLineHeight());
/*      */       } 
/* 1242 */       throw new IllegalArgumentException("Invalid axis: " + axis);
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
/*      */     public void paint(Graphics g, Shape a) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Shape modelToView(int pos, Shape a, Position.Bias b) throws BadLocationException {
/* 1275 */       Rectangle alloc = a.getBounds();
/* 1276 */       RSyntaxTextArea textArea = (RSyntaxTextArea)getContainer();
/* 1277 */       alloc.height = textArea.getLineHeight();
/* 1278 */       alloc.width = 1;
/* 1279 */       int p0 = getStartOffset();
/* 1280 */       int p1 = getEndOffset();
/*      */       
/* 1282 */       int testP = (b == Position.Bias.Forward) ? pos : Math.max(p0, pos - 1);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1287 */       RSyntaxDocument doc = (RSyntaxDocument)getDocument();
/* 1288 */       Element map = doc.getDefaultRootElement();
/* 1289 */       int line = map.getElementIndex(p0);
/* 1290 */       Token tokenList = doc.getTokenListForLine(line);
/* 1291 */       float x0 = alloc.x;
/*      */       
/* 1293 */       while (p0 < p1) {
/* 1294 */         TokenUtils.TokenSubList subList = TokenUtils.getSubTokenList(tokenList, p0, WrappedSyntaxView.this, textArea, x0, WrappedSyntaxView.this
/* 1295 */             .lineCountTempToken);
/* 1296 */         x0 = (subList != null) ? subList.x : x0;
/* 1297 */         tokenList = (subList != null) ? subList.tokenList : null;
/* 1298 */         int p = WrappedSyntaxView.this.calculateBreakPosition(p0, tokenList, x0);
/* 1299 */         if (pos >= p0 && testP < p) {
/*      */           
/* 1301 */           alloc = RSyntaxUtilities.getLineWidthUpTo(textArea, WrappedSyntaxView.this
/* 1302 */               .s, p0, pos, WrappedSyntaxView.this, alloc, alloc.x);
/*      */ 
/*      */ 
/*      */           
/* 1306 */           return alloc;
/*      */         } 
/*      */         
/* 1309 */         if (p == p1 - 1 && pos == p1 - 1) {
/*      */           
/* 1311 */           if (pos > p0) {
/* 1312 */             alloc = RSyntaxUtilities.getLineWidthUpTo(textArea, WrappedSyntaxView.this
/* 1313 */                 .s, p0, pos, WrappedSyntaxView.this, alloc, alloc.x);
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 1318 */           return alloc;
/*      */         } 
/*      */         
/* 1321 */         p0 = (p == p0) ? p1 : p;
/*      */         
/* 1323 */         alloc.y += alloc.height;
/*      */       } 
/*      */ 
/*      */       
/* 1327 */       throw new BadLocationException(null, pos);
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
/*      */     public int viewToModel(float fx, float fy, Shape a, Position.Bias[] bias) {
/* 1346 */       bias[0] = Position.Bias.Forward;
/*      */       
/* 1348 */       Rectangle alloc = (Rectangle)a;
/* 1349 */       RSyntaxDocument doc = (RSyntaxDocument)getDocument();
/* 1350 */       int x = (int)fx;
/* 1351 */       int y = (int)fy;
/* 1352 */       if (y < alloc.y)
/*      */       {
/*      */         
/* 1355 */         return getStartOffset();
/*      */       }
/* 1357 */       if (y > alloc.y + alloc.height)
/*      */       {
/*      */         
/* 1360 */         return getEndOffset() - 1;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1370 */       RSyntaxTextArea textArea = (RSyntaxTextArea)getContainer();
/* 1371 */       alloc.height = textArea.getLineHeight();
/* 1372 */       int p1 = getEndOffset();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1377 */       Element map = doc.getDefaultRootElement();
/* 1378 */       int p0 = getStartOffset();
/* 1379 */       int line = map.getElementIndex(p0);
/* 1380 */       Token tlist = doc.getTokenListForLine(line);
/*      */ 
/*      */       
/* 1383 */       while (p0 < p1) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1388 */         TokenUtils.TokenSubList subList = TokenUtils.getSubTokenList(tlist, p0, WrappedSyntaxView.this, textArea, alloc.x, WrappedSyntaxView.this
/* 1389 */             .lineCountTempToken);
/* 1390 */         tlist = (subList != null) ? subList.tokenList : null;
/* 1391 */         int p = WrappedSyntaxView.this.calculateBreakPosition(p0, tlist, alloc.x);
/*      */ 
/*      */         
/* 1394 */         if (y >= alloc.y && y < alloc.y + alloc.height) {
/*      */ 
/*      */           
/* 1397 */           if (x < alloc.x) {
/* 1398 */             return p0;
/*      */           }
/*      */ 
/*      */           
/* 1402 */           if (x > alloc.x + alloc.width) {
/* 1403 */             return p - 1;
/*      */           }
/*      */ 
/*      */           
/* 1407 */           if (tlist != null) {
/*      */ 
/*      */ 
/*      */             
/* 1411 */             int n = tlist.getListOffset(textArea, WrappedSyntaxView.this, alloc.x, x);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1421 */             return Math.max(Math.min(n, p - 1), p0);
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1427 */         p0 = (p == p0) ? p1 : p;
/* 1428 */         alloc.y += alloc.height;
/*      */       } 
/*      */ 
/*      */       
/* 1432 */       return getEndOffset() - 1;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void handleDocumentEvent(DocumentEvent e, Shape a, ViewFactory f) {
/* 1440 */       int n = calculateLineCount();
/* 1441 */       if (this.nlines != n) {
/* 1442 */         this.nlines = n;
/* 1443 */         WrappedSyntaxView.this.preferenceChanged(this, false, true);
/*      */         
/* 1445 */         RSyntaxTextArea textArea = (RSyntaxTextArea)getContainer();
/* 1446 */         textArea.repaint();
/*      */ 
/*      */         
/* 1449 */         Gutter gutter = RSyntaxUtilities.getGutter(textArea);
/* 1450 */         if (gutter != null) {
/* 1451 */           gutter.revalidate();
/* 1452 */           gutter.repaint();
/*      */         }
/*      */       
/* 1455 */       } else if (a != null) {
/* 1456 */         Component c = getContainer();
/* 1457 */         Rectangle alloc = (Rectangle)a;
/* 1458 */         c.repaint(alloc.x, alloc.y, alloc.width, alloc.height);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void insertUpdate(DocumentEvent e, Shape a, ViewFactory f) {
/* 1464 */       handleDocumentEvent(e, a, f);
/*      */     }
/*      */ 
/*      */     
/*      */     public void removeUpdate(DocumentEvent e, Shape a, ViewFactory f) {
/* 1469 */       handleDocumentEvent(e, a, f);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\WrappedSyntaxView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */