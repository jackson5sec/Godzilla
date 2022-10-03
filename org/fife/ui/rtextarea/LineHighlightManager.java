/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Position;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class LineHighlightManager
/*     */ {
/*     */   private RTextArea textArea;
/*     */   private List<LineHighlightInfo> lineHighlights;
/*     */   private LineHighlightInfoComparator comparator;
/*     */   
/*     */   LineHighlightManager(RTextArea textArea) {
/*  38 */     this.textArea = textArea;
/*  39 */     this.comparator = new LineHighlightInfoComparator();
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
/*     */   public Object addLineHighlight(int line, Color color) throws BadLocationException {
/*  55 */     int offs = this.textArea.getLineStartOffset(line);
/*     */     
/*  57 */     LineHighlightInfo lhi = new LineHighlightInfo(this.textArea.getDocument().createPosition(offs), color);
/*  58 */     if (this.lineHighlights == null) {
/*  59 */       this.lineHighlights = new ArrayList<>(1);
/*     */     }
/*  61 */     int index = Collections.binarySearch(this.lineHighlights, lhi, this.comparator);
/*  62 */     if (index < 0) {
/*  63 */       index = -(index + 1);
/*     */     }
/*  65 */     this.lineHighlights.add(index, lhi);
/*  66 */     repaintLine(lhi);
/*  67 */     return lhi;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<Object> getCurrentLineHighlightTags() {
/*  78 */     return (this.lineHighlights == null) ? Collections.<Object>emptyList() : new ArrayList(this.lineHighlights);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getLineHighlightCount() {
/*  89 */     return (this.lineHighlights == null) ? 0 : this.lineHighlights.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void paintLineHighlights(Graphics g) {
/* 100 */     int count = (this.lineHighlights == null) ? 0 : this.lineHighlights.size();
/* 101 */     if (count > 0) {
/*     */       
/* 103 */       int docLen = this.textArea.getDocument().getLength();
/* 104 */       Rectangle vr = this.textArea.getVisibleRect();
/* 105 */       int lineHeight = this.textArea.getLineHeight();
/*     */ 
/*     */       
/*     */       try {
/* 109 */         for (int i = 0; i < count; i++) {
/* 110 */           LineHighlightInfo lhi = this.lineHighlights.get(i);
/* 111 */           int offs = lhi.getOffset();
/* 112 */           if (offs >= 0 && offs <= docLen) {
/* 113 */             int y = this.textArea.yForLineContaining(offs);
/* 114 */             if (y > vr.y - lineHeight) {
/* 115 */               if (y < vr.y + vr.height) {
/* 116 */                 g.setColor(lhi.getColor());
/* 117 */                 g.fillRect(0, y, this.textArea.getWidth(), lineHeight);
/*     */               }
/*     */               else {
/*     */                 
/*     */                 break;
/*     */               } 
/*     */             }
/*     */           } 
/*     */         } 
/* 126 */       } catch (BadLocationException ble) {
/* 127 */         ble.printStackTrace();
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
/*     */   public void removeAllLineHighlights() {
/* 140 */     if (this.lineHighlights != null) {
/* 141 */       this.lineHighlights.clear();
/* 142 */       this.textArea.repaint();
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
/*     */   public void removeLineHighlight(Object tag) {
/* 154 */     if (tag instanceof LineHighlightInfo) {
/* 155 */       this.lineHighlights.remove(tag);
/* 156 */       repaintLine((LineHighlightInfo)tag);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void repaintLine(LineHighlightInfo lhi) {
/* 167 */     int offs = lhi.getOffset();
/*     */     
/* 169 */     if (offs >= 0 && offs <= this.textArea.getDocument().getLength()) {
/*     */       try {
/* 171 */         int y = this.textArea.yForLineContaining(offs);
/* 172 */         if (y > -1) {
/* 173 */           this.textArea.repaint(0, y, this.textArea
/* 174 */               .getWidth(), this.textArea.getLineHeight());
/*     */         }
/* 176 */       } catch (BadLocationException ble) {
/* 177 */         ble.printStackTrace();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class LineHighlightInfo
/*     */   {
/*     */     private Position offs;
/*     */     
/*     */     private Color color;
/*     */ 
/*     */     
/*     */     LineHighlightInfo(Position offs, Color c) {
/* 192 */       this.offs = offs;
/* 193 */       this.color = c;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 198 */       if (other instanceof LineHighlightInfo) {
/* 199 */         LineHighlightInfo lhi2 = (LineHighlightInfo)other;
/* 200 */         return (getOffset() == lhi2.getOffset() && 
/* 201 */           Objects.equals(getColor(), lhi2.getColor()));
/*     */       } 
/* 203 */       return false;
/*     */     }
/*     */     
/*     */     public Color getColor() {
/* 207 */       return this.color;
/*     */     }
/*     */     
/*     */     public int getOffset() {
/* 211 */       return this.offs.getOffset();
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 216 */       return getOffset();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class LineHighlightInfoComparator
/*     */     implements Comparator<LineHighlightInfo>
/*     */   {
/*     */     private LineHighlightInfoComparator() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int compare(LineHighlightManager.LineHighlightInfo lhi1, LineHighlightManager.LineHighlightInfo lhi2) {
/* 235 */       if (lhi1.getOffset() < lhi2.getOffset()) {
/* 236 */         return -1;
/*     */       }
/* 238 */       return (lhi1.getOffset() == lhi2.getOffset()) ? 0 : 1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\LineHighlightManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */