/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.net.URL;
/*     */ import java.security.AccessControlException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.Timer;
/*     */ import javax.swing.ToolTipManager;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
/*     */ import javax.swing.event.HyperlinkEvent;
/*     */ import javax.swing.event.HyperlinkListener;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.Highlighter;
/*     */ import javax.swing.text.Position;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.ParseResult;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.Parser;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.ParserNotice;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.ToolTipInfo;
/*     */ import org.fife.ui.rtextarea.RDocument;
/*     */ import org.fife.ui.rtextarea.RTextAreaHighlighter;
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
/*     */ class ParserManager
/*     */   implements DocumentListener, ActionListener, HyperlinkListener, PropertyChangeListener
/*     */ {
/*     */   private RSyntaxTextArea textArea;
/*     */   private List<Parser> parsers;
/*     */   private Timer timer;
/*     */   private boolean running;
/*     */   private Parser parserForTip;
/*     */   private Position firstOffsetModded;
/*     */   private Position lastOffsetModded;
/*     */   private List<NoticeHighlightPair> noticeHighlightPairs;
/*  75 */   private SquiggleUnderlineHighlightPainter parserErrorHighlightPainter = new SquiggleUnderlineHighlightPainter(Color.RED);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String PROPERTY_DEBUG_PARSING = "rsta.debugParsing";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final boolean DEBUG_PARSING;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int DEFAULT_DELAY_MS = 1250;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ParserManager(RSyntaxTextArea textArea) {
/* 103 */     this(1250, textArea);
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
/*     */   ParserManager(int delay, RSyntaxTextArea textArea) {
/* 116 */     this.textArea = textArea;
/* 117 */     textArea.getDocument().addDocumentListener(this);
/* 118 */     textArea.addPropertyChangeListener("document", this);
/* 119 */     this.parsers = new ArrayList<>(1);
/* 120 */     this.timer = new Timer(delay, this);
/* 121 */     this.timer.setRepeats(false);
/* 122 */     this.running = true;
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
/*     */   public void actionPerformed(ActionEvent e) {
/* 135 */     int parserCount = getParserCount();
/* 136 */     if (parserCount == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 140 */     long begin = 0L;
/* 141 */     if (DEBUG_PARSING) {
/* 142 */       begin = System.currentTimeMillis();
/*     */     }
/*     */     
/* 145 */     RSyntaxDocument doc = (RSyntaxDocument)this.textArea.getDocument();
/*     */     
/* 147 */     Element root = doc.getDefaultRootElement();
/*     */     
/* 149 */     int firstLine = (this.firstOffsetModded == null) ? 0 : root.getElementIndex(this.firstOffsetModded.getOffset());
/*     */     
/* 151 */     int lastLine = (this.lastOffsetModded == null) ? (root.getElementCount() - 1) : root.getElementIndex(this.lastOffsetModded.getOffset());
/* 152 */     this.firstOffsetModded = this.lastOffsetModded = null;
/* 153 */     if (DEBUG_PARSING) {
/* 154 */       System.out.println("[DEBUG]: Minimum lines to parse: " + firstLine + "-" + lastLine);
/*     */     }
/*     */     
/* 157 */     String style = this.textArea.getSyntaxEditingStyle();
/* 158 */     doc.readLock();
/*     */     try {
/* 160 */       for (int i = 0; i < parserCount; i++) {
/* 161 */         Parser parser = getParser(i);
/* 162 */         if (parser.isEnabled()) {
/* 163 */           ParseResult res = parser.parse(doc, style);
/* 164 */           addParserNoticeHighlights(res);
/*     */         } else {
/*     */           
/* 167 */           clearParserNoticeHighlights(parser);
/*     */         } 
/*     */       } 
/* 170 */       this.textArea.fireParserNoticesChange();
/*     */     } finally {
/* 172 */       doc.readUnlock();
/*     */     } 
/*     */     
/* 175 */     if (DEBUG_PARSING) {
/* 176 */       float time = (float)(System.currentTimeMillis() - begin) / 1000.0F;
/* 177 */       System.out.println("Total parsing time: " + time + " seconds");
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
/*     */   public void addParser(Parser parser) {
/* 192 */     if (parser != null && !this.parsers.contains(parser)) {
/* 193 */       if (this.running) {
/* 194 */         this.timer.stop();
/*     */       }
/* 196 */       this.parsers.add(parser);
/* 197 */       if (this.parsers.size() == 1)
/*     */       {
/* 199 */         ToolTipManager.sharedInstance().registerComponent((JComponent)this.textArea);
/*     */       }
/* 201 */       if (this.running) {
/* 202 */         this.timer.restart();
/*     */       }
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
/*     */   private void addParserNoticeHighlights(ParseResult res) {
/* 219 */     if (res == null) {
/*     */       return;
/*     */     }
/*     */     
/* 223 */     if (DEBUG_PARSING) {
/* 224 */       System.out.println("[DEBUG]: Adding parser notices from " + res
/* 225 */           .getParser());
/*     */     }
/*     */     
/* 228 */     if (this.noticeHighlightPairs == null) {
/* 229 */       this.noticeHighlightPairs = new ArrayList<>();
/*     */     }
/*     */     
/* 232 */     removeParserNotices(res);
/*     */     
/* 234 */     List<ParserNotice> notices = res.getNotices();
/* 235 */     if (notices.size() > 0) {
/*     */ 
/*     */       
/* 238 */       RSyntaxTextAreaHighlighter h = (RSyntaxTextAreaHighlighter)this.textArea.getHighlighter();
/*     */       
/* 240 */       for (ParserNotice notice : notices) {
/* 241 */         if (DEBUG_PARSING) {
/* 242 */           System.out.println("[DEBUG]: ... adding: " + notice);
/*     */         }
/*     */         try {
/* 245 */           RTextAreaHighlighter.HighlightInfo highlight = null;
/* 246 */           if (notice.getShowInEditor()) {
/* 247 */             highlight = h.addParserHighlight(notice, (Highlighter.HighlightPainter)this.parserErrorHighlightPainter);
/*     */           }
/*     */           
/* 250 */           this.noticeHighlightPairs.add(new NoticeHighlightPair(notice, highlight));
/* 251 */         } catch (BadLocationException ble) {
/* 252 */           ble.printStackTrace();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 258 */     if (DEBUG_PARSING) {
/* 259 */       System.out.println("[DEBUG]: Done adding parser notices from " + res
/* 260 */           .getParser());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void changedUpdate(DocumentEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void clearParserNoticeHighlights() {
/* 278 */     RSyntaxTextAreaHighlighter h = (RSyntaxTextAreaHighlighter)this.textArea.getHighlighter();
/* 279 */     if (h != null) {
/* 280 */       h.clearParserHighlights();
/*     */     }
/* 282 */     if (this.noticeHighlightPairs != null) {
/* 283 */       this.noticeHighlightPairs.clear();
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
/*     */   private void clearParserNoticeHighlights(Parser parser) {
/* 295 */     RSyntaxTextAreaHighlighter h = (RSyntaxTextAreaHighlighter)this.textArea.getHighlighter();
/* 296 */     if (h != null) {
/* 297 */       h.clearParserHighlights(parser);
/*     */     }
/* 299 */     if (this.noticeHighlightPairs != null) {
/* 300 */       this.noticeHighlightPairs.removeIf(pair -> (pair.notice.getParser() == parser));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearParsers() {
/* 311 */     this.timer.stop();
/* 312 */     clearParserNoticeHighlights();
/* 313 */     this.parsers.clear();
/* 314 */     this.textArea.fireParserNoticesChange();
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
/*     */   public void forceReparsing(int parser) {
/* 332 */     Parser p = getParser(parser);
/* 333 */     RSyntaxDocument doc = (RSyntaxDocument)this.textArea.getDocument();
/* 334 */     String style = this.textArea.getSyntaxEditingStyle();
/* 335 */     doc.readLock();
/*     */     try {
/* 337 */       if (p.isEnabled()) {
/* 338 */         ParseResult res = p.parse(doc, style);
/* 339 */         addParserNoticeHighlights(res);
/*     */       } else {
/*     */         
/* 342 */         clearParserNoticeHighlights(p);
/*     */       } 
/* 344 */       this.textArea.fireParserNoticesChange();
/*     */     } finally {
/* 346 */       doc.readUnlock();
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
/*     */   public int getDelay() {
/* 359 */     return this.timer.getDelay();
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
/*     */   public Parser getParser(int index) {
/* 373 */     return this.parsers.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getParserCount() {
/* 383 */     return this.parsers.size();
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
/*     */   public List<ParserNotice> getParserNotices() {
/* 396 */     List<ParserNotice> notices = new ArrayList<>();
/* 397 */     if (this.noticeHighlightPairs != null) {
/* 398 */       for (NoticeHighlightPair pair : this.noticeHighlightPairs) {
/* 399 */         notices.add(pair.notice);
/*     */       }
/*     */     }
/* 402 */     return notices;
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
/*     */   public ToolTipInfo getToolTipText(MouseEvent e) {
/* 417 */     String tip = null;
/* 418 */     HyperlinkListener listener = null;
/* 419 */     this.parserForTip = null;
/* 420 */     Point p = e.getPoint();
/*     */ 
/*     */     
/* 423 */     int pos = this.textArea.viewToModel(p);
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
/* 440 */     if (this.noticeHighlightPairs != null) {
/* 441 */       for (NoticeHighlightPair pair : this.noticeHighlightPairs) {
/* 442 */         ParserNotice notice = pair.notice;
/* 443 */         if (noticeContainsPosition(notice, pos) && 
/* 444 */           noticeContainsPointInView(notice, p)) {
/* 445 */           tip = notice.getToolTipText();
/* 446 */           this.parserForTip = notice.getParser();
/* 447 */           if (this.parserForTip instanceof HyperlinkListener) {
/* 448 */             listener = (HyperlinkListener)this.parserForTip;
/*     */           }
/*     */ 
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 458 */     URL imageBase = (this.parserForTip == null) ? null : this.parserForTip.getImageBase();
/* 459 */     return new ToolTipInfo(tip, listener, imageBase);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleDocumentEvent(DocumentEvent e) {
/* 470 */     if (this.running && this.parsers.size() > 0) {
/* 471 */       this.timer.restart();
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
/*     */   public void hyperlinkUpdate(HyperlinkEvent e) {
/* 484 */     if (this.parserForTip != null && this.parserForTip.getHyperlinkListener() != null) {
/* 485 */       this.parserForTip.getHyperlinkListener().linkClicked(this.textArea, e);
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
/*     */   public void insertUpdate(DocumentEvent e) {
/*     */     try {
/* 501 */       int offs = e.getOffset();
/* 502 */       if (this.firstOffsetModded == null || offs < this.firstOffsetModded.getOffset()) {
/* 503 */         this.firstOffsetModded = e.getDocument().createPosition(offs);
/*     */       }
/* 505 */       offs = e.getOffset() + e.getLength();
/* 506 */       if (this.lastOffsetModded == null || offs > this.lastOffsetModded.getOffset()) {
/* 507 */         this.lastOffsetModded = e.getDocument().createPosition(offs);
/*     */       }
/* 509 */     } catch (BadLocationException ble) {
/* 510 */       ble.printStackTrace();
/*     */     } 
/*     */     
/* 513 */     handleDocumentEvent(e);
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
/*     */   private boolean noticeContainsPosition(ParserNotice notice, int offs) {
/* 526 */     if (notice.getKnowsOffsetAndLength()) {
/* 527 */       return notice.containsPosition(offs);
/*     */     }
/* 529 */     Document doc = this.textArea.getDocument();
/* 530 */     Element root = doc.getDefaultRootElement();
/* 531 */     int line = notice.getLine();
/* 532 */     if (line < 0) {
/* 533 */       return false;
/*     */     }
/* 535 */     Element elem = root.getElement(line);
/* 536 */     return (elem != null && offs >= elem.getStartOffset() && offs < elem.getEndOffset());
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
/*     */   private boolean noticeContainsPointInView(ParserNotice notice, Point p) {
/*     */     try {
/*     */       int start, end;
/* 558 */       if (notice.getKnowsOffsetAndLength()) {
/* 559 */         start = notice.getOffset();
/* 560 */         end = start + notice.getLength() - 1;
/*     */       } else {
/*     */         
/* 563 */         Document doc = this.textArea.getDocument();
/* 564 */         Element root = doc.getDefaultRootElement();
/* 565 */         int line = notice.getLine();
/*     */         
/* 567 */         if (line < 0) {
/* 568 */           return false;
/*     */         }
/* 570 */         Element elem = root.getElement(line);
/* 571 */         start = elem.getStartOffset();
/* 572 */         end = elem.getEndOffset() - 1;
/*     */       } 
/*     */       
/* 575 */       Rectangle r1 = this.textArea.modelToView(start);
/* 576 */       Rectangle r2 = this.textArea.modelToView(end);
/* 577 */       if (r1.y != r2.y)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 582 */         return true;
/*     */       }
/*     */       
/* 585 */       r1.y--;
/* 586 */       r1.height += 2;
/* 587 */       return (p.x >= r1.x && p.x < r2.x + r2.width && p.y >= r1.y && p.y < r1.y + r1.height);
/*     */     
/*     */     }
/* 590 */     catch (BadLocationException ble) {
/*     */ 
/*     */       
/* 593 */       return true;
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
/*     */   public void propertyChange(PropertyChangeEvent e) {
/* 607 */     String name = e.getPropertyName();
/*     */     
/* 609 */     if ("document".equals(name)) {
/*     */       
/* 611 */       RDocument old = (RDocument)e.getOldValue();
/* 612 */       if (old != null) {
/* 613 */         old.removeDocumentListener(this);
/*     */       }
/* 615 */       RDocument newDoc = (RDocument)e.getNewValue();
/* 616 */       if (newDoc != null) {
/* 617 */         newDoc.addDocumentListener(this);
/*     */       }
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
/*     */   public boolean removeParser(Parser parser) {
/* 633 */     removeParserNotices(parser);
/* 634 */     boolean removed = this.parsers.remove(parser);
/* 635 */     if (removed) {
/* 636 */       this.textArea.fireParserNoticesChange();
/*     */     }
/* 638 */     return removed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeParserNotices(Parser parser) {
/* 649 */     if (this.noticeHighlightPairs != null) {
/*     */       
/* 651 */       RSyntaxTextAreaHighlighter h = (RSyntaxTextAreaHighlighter)this.textArea.getHighlighter();
/* 652 */       for (Iterator<NoticeHighlightPair> i = this.noticeHighlightPairs.iterator(); i.hasNext(); ) {
/* 653 */         NoticeHighlightPair pair = i.next();
/* 654 */         if (pair.notice.getParser() == parser && pair.highlight != null) {
/* 655 */           h.removeParserHighlight(pair.highlight);
/* 656 */           i.remove();
/*     */         } 
/*     */       } 
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
/*     */   private void removeParserNotices(ParseResult res) {
/* 671 */     if (this.noticeHighlightPairs != null) {
/*     */       
/* 673 */       RSyntaxTextAreaHighlighter h = (RSyntaxTextAreaHighlighter)this.textArea.getHighlighter();
/* 674 */       for (Iterator<NoticeHighlightPair> i = this.noticeHighlightPairs.iterator(); i.hasNext(); ) {
/* 675 */         NoticeHighlightPair pair = i.next();
/* 676 */         boolean removed = false;
/* 677 */         if (shouldRemoveNotice(pair.notice, res)) {
/* 678 */           if (pair.highlight != null) {
/* 679 */             h.removeParserHighlight(pair.highlight);
/*     */           }
/* 681 */           i.remove();
/* 682 */           removed = true;
/*     */         } 
/* 684 */         if (DEBUG_PARSING) {
/* 685 */           String text = removed ? "[DEBUG]: ... notice removed: " : "[DEBUG]: ... notice not removed: ";
/*     */           
/* 687 */           System.out.println(text + pair.notice);
/*     */         } 
/*     */       } 
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
/*     */   public void removeUpdate(DocumentEvent e) {
/*     */     try {
/* 709 */       int offs = e.getOffset();
/* 710 */       if (this.firstOffsetModded == null || offs < this.firstOffsetModded.getOffset()) {
/* 711 */         this.firstOffsetModded = e.getDocument().createPosition(offs);
/*     */       }
/* 713 */       if (this.lastOffsetModded == null || offs > this.lastOffsetModded.getOffset()) {
/* 714 */         this.lastOffsetModded = e.getDocument().createPosition(offs);
/*     */       }
/* 716 */     } catch (BadLocationException ble) {
/* 717 */       ble.printStackTrace();
/*     */     } 
/*     */     
/* 720 */     handleDocumentEvent(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void restartParsing() {
/* 731 */     this.timer.restart();
/* 732 */     this.running = true;
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
/*     */   public void setDelay(int millis) {
/* 745 */     if (this.running) {
/* 746 */       this.timer.stop();
/*     */     }
/* 748 */     this.timer.setInitialDelay(millis);
/* 749 */     this.timer.setDelay(millis);
/* 750 */     if (this.running) {
/* 751 */       this.timer.start();
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
/*     */   private boolean shouldRemoveNotice(ParserNotice notice, ParseResult res) {
/* 767 */     if (DEBUG_PARSING) {
/* 768 */       System.out.println("[DEBUG]: ... ... shouldRemoveNotice " + notice + ": " + (
/* 769 */           (notice.getParser() == res.getParser()) ? 1 : 0));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 779 */     return (notice.getParser() == res.getParser());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void stopParsing() {
/* 790 */     this.timer.stop();
/* 791 */     this.running = false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class NoticeHighlightPair
/*     */   {
/*     */     private ParserNotice notice;
/*     */     
/*     */     private RTextAreaHighlighter.HighlightInfo highlight;
/*     */ 
/*     */     
/*     */     NoticeHighlightPair(ParserNotice notice, RTextAreaHighlighter.HighlightInfo highlight) {
/* 804 */       this.notice = notice;
/* 805 */       this.highlight = highlight;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*     */     boolean debugParsing;
/*     */     try {
/* 814 */       debugParsing = Boolean.getBoolean("rsta.debugParsing");
/* 815 */     } catch (AccessControlException ace) {
/*     */       
/* 817 */       debugParsing = false;
/*     */     } 
/* 819 */     DEBUG_PARSING = debugParsing;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\ParserManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */