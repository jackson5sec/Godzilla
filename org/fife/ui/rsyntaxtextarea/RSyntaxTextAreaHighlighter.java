/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Shape;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.swing.plaf.TextUI;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.Highlighter;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.View;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.Parser;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.ParserNotice;
/*     */ import org.fife.ui.rtextarea.RTextAreaHighlighter;
/*     */ import org.fife.ui.rtextarea.SmartHighlightPainter;
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
/*     */ public class RSyntaxTextAreaHighlighter
/*     */   extends RTextAreaHighlighter
/*     */ {
/*  59 */   private static final Color DEFAULT_PARSER_NOTICE_COLOR = Color.RED;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   private List<SyntaxLayeredHighlightInfoImpl> markedOccurrences = new ArrayList<>();
/*  67 */   private List<SyntaxLayeredHighlightInfoImpl> parserHighlights = new ArrayList<>(0);
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
/*     */   Object addMarkedOccurrenceHighlight(int start, int end, SmartHighlightPainter p) throws BadLocationException {
/*  83 */     Document doc = this.textArea.getDocument();
/*  84 */     TextUI mapper = this.textArea.getUI();
/*     */     
/*  86 */     SyntaxLayeredHighlightInfoImpl i = new SyntaxLayeredHighlightInfoImpl();
/*  87 */     i.setPainter((Highlighter.HighlightPainter)p);
/*  88 */     i.setStartOffset(doc.createPosition(start));
/*     */ 
/*     */ 
/*     */     
/*  92 */     i.setEndOffset(doc.createPosition(end - 1));
/*  93 */     this.markedOccurrences.add(i);
/*  94 */     mapper.damageRange((JTextComponent)this.textArea, start, end);
/*  95 */     return i;
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
/*     */   RTextAreaHighlighter.HighlightInfo addParserHighlight(ParserNotice notice, Highlighter.HighlightPainter p) throws BadLocationException {
/* 111 */     Document doc = this.textArea.getDocument();
/* 112 */     TextUI mapper = this.textArea.getUI();
/*     */     
/* 114 */     int start = notice.getOffset();
/* 115 */     int end = 0;
/* 116 */     if (start == -1) {
/* 117 */       int line = notice.getLine();
/* 118 */       Element root = doc.getDefaultRootElement();
/* 119 */       if (line >= 0 && line < root.getElementCount()) {
/* 120 */         Element elem = root.getElement(line);
/* 121 */         start = elem.getStartOffset();
/* 122 */         end = elem.getEndOffset();
/*     */       } 
/*     */     } else {
/*     */       
/* 126 */       end = start + notice.getLength();
/*     */     } 
/*     */ 
/*     */     
/* 130 */     SyntaxLayeredHighlightInfoImpl i = new SyntaxLayeredHighlightInfoImpl();
/* 131 */     i.setPainter(p);
/* 132 */     i.setStartOffset(doc.createPosition(start));
/*     */ 
/*     */ 
/*     */     
/* 136 */     i.setEndOffset(doc.createPosition(end - 1));
/* 137 */     i.notice = notice;
/*     */     
/* 139 */     this.parserHighlights.add(i);
/* 140 */     mapper.damageRange((JTextComponent)this.textArea, start, end);
/* 141 */     return (RTextAreaHighlighter.HighlightInfo)i;
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
/*     */   void clearMarkOccurrencesHighlights() {
/* 154 */     for (RTextAreaHighlighter.HighlightInfo info : this.markedOccurrences) {
/* 155 */       repaintListHighlight(info);
/*     */     }
/* 157 */     this.markedOccurrences.clear();
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
/*     */   void clearParserHighlights() {
/* 169 */     for (SyntaxLayeredHighlightInfoImpl parserHighlight : this.parserHighlights) {
/* 170 */       repaintListHighlight((RTextAreaHighlighter.HighlightInfo)parserHighlight);
/*     */     }
/* 172 */     this.parserHighlights.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearParserHighlights(Parser parser) {
/* 183 */     Iterator<SyntaxLayeredHighlightInfoImpl> i = this.parserHighlights.iterator();
/* 184 */     while (i.hasNext()) {
/*     */       
/* 186 */       SyntaxLayeredHighlightInfoImpl info = i.next();
/*     */       
/* 188 */       if (info.notice.getParser() == parser) {
/* 189 */         if (info.width > 0 && info.height > 0) {
/* 190 */           this.textArea.repaint(info.x, info.y, info.width, info.height);
/*     */         }
/* 192 */         i.remove();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void deinstall(JTextComponent c) {
/* 202 */     super.deinstall(c);
/* 203 */     this.markedOccurrences.clear();
/* 204 */     this.parserHighlights.clear();
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
/*     */   public List<DocumentRange> getMarkedOccurrences() {
/* 216 */     List<DocumentRange> list = new ArrayList<>(this.markedOccurrences.size());
/* 217 */     for (RTextAreaHighlighter.HighlightInfo info : this.markedOccurrences) {
/* 218 */       int start = info.getStartOffset();
/* 219 */       int end = info.getEndOffset() + 1;
/* 220 */       if (start <= end) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 225 */         DocumentRange range = new DocumentRange(start, end);
/* 226 */         list.add(range);
/*     */       } 
/*     */     } 
/* 229 */     return list;
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
/*     */   public void paintLayeredHighlights(Graphics g, int lineStart, int lineEnd, Shape viewBounds, JTextComponent editor, View view) {
/* 248 */     paintListLayered(g, lineStart, lineEnd, viewBounds, editor, view, this.markedOccurrences);
/* 249 */     super.paintLayeredHighlights(g, lineStart, lineEnd, viewBounds, editor, view);
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
/*     */   public void paintParserHighlights(Graphics g, int lineStart, int lineEnd, Shape viewBounds, JTextComponent editor, View view) {
/* 266 */     paintListLayered(g, lineStart, lineEnd, viewBounds, editor, view, this.parserHighlights);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void removeParserHighlight(RTextAreaHighlighter.HighlightInfo tag) {
/* 277 */     repaintListHighlight(tag);
/* 278 */     this.parserHighlights.remove(tag);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SyntaxLayeredHighlightInfoImpl
/*     */     extends RTextAreaHighlighter.LayeredHighlightInfoImpl
/*     */   {
/*     */     private ParserNotice notice;
/*     */ 
/*     */     
/*     */     private SyntaxLayeredHighlightInfoImpl() {}
/*     */ 
/*     */     
/*     */     public Color getColor() {
/* 293 */       Color color = null;
/* 294 */       if (this.notice != null) {
/* 295 */         color = this.notice.getColor();
/* 296 */         if (color == null) {
/* 297 */           color = RSyntaxTextAreaHighlighter.DEFAULT_PARSER_NOTICE_COLOR;
/*     */         }
/*     */       } 
/* 300 */       return color;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 305 */       return "[SyntaxLayeredHighlightInfoImpl: startOffs=" + 
/* 306 */         getStartOffset() + ", endOffs=" + 
/* 307 */         getEndOffset() + ", color=" + 
/* 308 */         getColor() + "]";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\RSyntaxTextAreaHighlighter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */