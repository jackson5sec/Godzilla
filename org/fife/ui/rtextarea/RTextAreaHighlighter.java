/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.swing.plaf.TextUI;
/*     */ import javax.swing.plaf.basic.BasicTextUI;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Highlighter;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.LayeredHighlighter;
/*     */ import javax.swing.text.Position;
/*     */ import javax.swing.text.View;
/*     */ import org.fife.ui.rsyntaxtextarea.DocumentRange;
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
/*     */ public class RTextAreaHighlighter
/*     */   extends BasicTextUI.BasicHighlighter
/*     */ {
/*     */   protected RTextArea textArea;
/*  58 */   private List<HighlightInfo> markAllHighlights = new ArrayList<>();
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
/*     */   Object addMarkAllHighlight(int start, int end, Highlighter.HighlightPainter p) throws BadLocationException {
/*  74 */     Document doc = this.textArea.getDocument();
/*  75 */     TextUI mapper = this.textArea.getUI();
/*     */     
/*  77 */     HighlightInfoImpl i = new LayeredHighlightInfoImpl();
/*  78 */     i.setPainter(p);
/*  79 */     i.p0 = doc.createPosition(start);
/*     */ 
/*     */ 
/*     */     
/*  83 */     i.p1 = doc.createPosition(end - 1);
/*  84 */     this.markAllHighlights.add(i);
/*  85 */     mapper.damageRange(this.textArea, start, end);
/*  86 */     return i;
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
/*     */   void clearMarkAllHighlights() {
/*  98 */     for (HighlightInfo info : this.markAllHighlights) {
/*  99 */       repaintListHighlight(info);
/*     */     }
/* 101 */     this.markAllHighlights.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void deinstall(JTextComponent c) {
/* 107 */     this.textArea = null;
/* 108 */     this.markAllHighlights.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMarkAllHighlightCount() {
/* 119 */     return this.markAllHighlights.size();
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
/*     */   public List<DocumentRange> getMarkAllHighlightRanges() {
/* 131 */     List<DocumentRange> list = new ArrayList<>(this.markAllHighlights.size());
/* 132 */     for (HighlightInfo info : this.markAllHighlights) {
/* 133 */       int start = info.getStartOffset();
/* 134 */       int end = info.getEndOffset() + 1;
/* 135 */       DocumentRange range = new DocumentRange(start, end);
/* 136 */       list.add(range);
/*     */     } 
/* 138 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void install(JTextComponent c) {
/* 144 */     super.install(c);
/* 145 */     this.textArea = (RTextArea)c;
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
/* 164 */     paintListLayered(g, lineStart, lineEnd, viewBounds, editor, view, this.markAllHighlights);
/*     */     
/* 166 */     super.paintLayeredHighlights(g, lineStart, lineEnd, viewBounds, editor, view);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintListLayered(Graphics g, int lineStart, int lineEnd, Shape viewBounds, JTextComponent editor, View view, List<? extends HighlightInfo> highlights) {
/* 174 */     for (int i = highlights.size() - 1; i >= 0; i--) {
/* 175 */       HighlightInfo tag = highlights.get(i);
/* 176 */       if (tag instanceof LayeredHighlightInfo) {
/* 177 */         LayeredHighlightInfo lhi = (LayeredHighlightInfo)tag;
/* 178 */         int highlightStart = lhi.getStartOffset();
/* 179 */         int highlightEnd = lhi.getEndOffset() + 1;
/* 180 */         if ((lineStart < highlightStart && lineEnd > highlightStart) || (lineStart >= highlightStart && lineStart < highlightEnd))
/*     */         {
/* 182 */           lhi.paintLayeredHighlights(g, lineStart, lineEnd, viewBounds, editor, view);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void repaintListHighlight(HighlightInfo info) {
/* 192 */     if (info instanceof LayeredHighlightInfoImpl) {
/* 193 */       LayeredHighlightInfoImpl lhi = (LayeredHighlightInfoImpl)info;
/* 194 */       if (lhi.width > 0 && lhi.height > 0) {
/* 195 */         this.textArea.repaint(lhi.x, lhi.y, lhi.width, lhi.height);
/*     */       }
/*     */     } else {
/*     */       
/* 199 */       TextUI ui = this.textArea.getUI();
/* 200 */       ui.damageRange(this.textArea, info.getStartOffset(), info.getEndOffset());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface HighlightInfo
/*     */     extends Highlighter.Highlight {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface LayeredHighlightInfo
/*     */     extends HighlightInfo
/*     */   {
/*     */     void paintLayeredHighlights(Graphics param1Graphics, int param1Int1, int param1Int2, Shape param1Shape, JTextComponent param1JTextComponent, View param1View);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class HighlightInfoImpl
/*     */     implements HighlightInfo
/*     */   {
/*     */     private Position p0;
/*     */ 
/*     */ 
/*     */     
/*     */     private Position p1;
/*     */ 
/*     */ 
/*     */     
/*     */     private Highlighter.HighlightPainter painter;
/*     */ 
/*     */ 
/*     */     
/*     */     public Color getColor() {
/* 238 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getStartOffset() {
/* 243 */       return this.p0.getOffset();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getEndOffset() {
/* 248 */       return this.p1.getOffset();
/*     */     }
/*     */ 
/*     */     
/*     */     public Highlighter.HighlightPainter getPainter() {
/* 253 */       return this.painter;
/*     */     }
/*     */     
/*     */     public void setStartOffset(Position startOffset) {
/* 257 */       this.p0 = startOffset;
/*     */     }
/*     */     
/*     */     public void setEndOffset(Position endOffset) {
/* 261 */       this.p1 = endOffset;
/*     */     }
/*     */     
/*     */     public void setPainter(Highlighter.HighlightPainter painter) {
/* 265 */       this.painter = painter;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class LayeredHighlightInfoImpl
/*     */     extends HighlightInfoImpl
/*     */     implements LayeredHighlightInfo
/*     */   {
/*     */     public int x;
/*     */ 
/*     */ 
/*     */     
/*     */     public int y;
/*     */ 
/*     */ 
/*     */     
/*     */     public int width;
/*     */ 
/*     */ 
/*     */     
/*     */     public int height;
/*     */ 
/*     */ 
/*     */     
/*     */     void union(Shape bounds) {
/* 293 */       if (bounds == null) {
/*     */         return;
/*     */       }
/*     */       
/* 297 */       Rectangle alloc = (bounds instanceof Rectangle) ? (Rectangle)bounds : bounds.getBounds();
/* 298 */       if (this.width == 0 || this.height == 0) {
/* 299 */         this.x = alloc.x;
/* 300 */         this.y = alloc.y;
/* 301 */         this.width = alloc.width;
/* 302 */         this.height = alloc.height;
/*     */       } else {
/*     */         
/* 305 */         this.width = Math.max(this.x + this.width, alloc.x + alloc.width);
/* 306 */         this.height = Math.max(this.y + this.height, alloc.y + alloc.height);
/* 307 */         this.x = Math.min(this.x, alloc.x);
/* 308 */         this.width -= this.x;
/* 309 */         this.y = Math.min(this.y, alloc.y);
/* 310 */         this.height -= this.y;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void paintLayeredHighlights(Graphics g, int p0, int p1, Shape viewBounds, JTextComponent editor, View view) {
/* 318 */       int start = getStartOffset();
/* 319 */       int end = getEndOffset();
/* 320 */       end++;
/*     */       
/* 322 */       p0 = Math.max(start, p0);
/* 323 */       p1 = Math.min(end, p1);
/* 324 */       if (getColor() != null && 
/* 325 */         getPainter() instanceof ChangeableHighlightPainter) {
/* 326 */         ((ChangeableHighlightPainter)getPainter()).setPaint(getColor());
/*     */       }
/*     */ 
/*     */       
/* 330 */       union(((LayeredHighlighter.LayerPainter)getPainter()).paintLayer(g, p0, p1, viewBounds, editor, view));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\RTextAreaHighlighter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */