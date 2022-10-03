/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Point;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import org.fife.ui.rsyntaxtextarea.folding.FoldManager;
/*     */ import org.fife.ui.rtextarea.IconRowHeader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FoldingAwareIconRowHeader
/*     */   extends IconRowHeader
/*     */ {
/*     */   public FoldingAwareIconRowHeader(RSyntaxTextArea textArea) {
/*  41 */     super(textArea);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintComponent(Graphics g) {
/*  49 */     if (this.textArea == null) {
/*     */       return;
/*     */     }
/*  52 */     RSyntaxTextArea rsta = (RSyntaxTextArea)this.textArea;
/*  53 */     FoldManager fm = rsta.getFoldManager();
/*  54 */     if (!fm.isCodeFoldingSupportedAndEnabled()) {
/*  55 */       super.paintComponent(g);
/*     */       
/*     */       return;
/*     */     } 
/*  59 */     this.visibleRect = g.getClipBounds(this.visibleRect);
/*  60 */     if (this.visibleRect == null) {
/*  61 */       this.visibleRect = getVisibleRect();
/*     */     }
/*     */     
/*  64 */     if (this.visibleRect == null) {
/*     */       return;
/*     */     }
/*  67 */     paintBackgroundImpl(g, this.visibleRect);
/*     */     
/*  69 */     if (this.textArea.getLineWrap()) {
/*  70 */       paintComponentWrapped(g);
/*     */       
/*     */       return;
/*     */     } 
/*  74 */     Document doc = this.textArea.getDocument();
/*  75 */     Element root = doc.getDefaultRootElement();
/*  76 */     this.textAreaInsets = this.textArea.getInsets(this.textAreaInsets);
/*  77 */     if (this.visibleRect.y < this.textAreaInsets.top) {
/*  78 */       this.visibleRect.height -= this.textAreaInsets.top - this.visibleRect.y;
/*  79 */       this.visibleRect.y = this.textAreaInsets.top;
/*     */     } 
/*     */ 
/*     */     
/*  83 */     int cellHeight = this.textArea.getLineHeight();
/*  84 */     int topLine = (this.visibleRect.y - this.textAreaInsets.top) / cellHeight;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     int y = topLine * cellHeight + this.textAreaInsets.top;
/*     */ 
/*     */     
/*  92 */     topLine += fm.getHiddenLineCountAbove(topLine, true);
/*     */ 
/*     */     
/*  95 */     if (this.activeLineRangeStart > -1 && this.activeLineRangeEnd > -1) {
/*  96 */       Color activeLineRangeColor = getActiveLineRangeColor();
/*  97 */       g.setColor(activeLineRangeColor);
/*     */       
/*     */       try {
/* 100 */         int realY1 = rsta.yForLine(this.activeLineRangeStart);
/* 101 */         if (realY1 > -1)
/*     */         {
/* 103 */           int y1 = realY1;
/*     */           
/* 105 */           int y2 = rsta.yForLine(this.activeLineRangeEnd);
/* 106 */           if (y2 == -1) {
/* 107 */             y2 = y1;
/*     */           }
/* 109 */           y2 += cellHeight - 1;
/*     */           
/* 111 */           if (y2 < this.visibleRect.y || y1 > this.visibleRect.y + this.visibleRect.height) {
/*     */             return;
/*     */           }
/*     */           
/* 115 */           y1 = Math.max(y, realY1);
/* 116 */           y2 = Math.min(y2, this.visibleRect.y + this.visibleRect.height);
/*     */ 
/*     */           
/* 119 */           int j = y1;
/* 120 */           while (j <= y2) {
/* 121 */             int yEnd = Math.min(y2, j + getWidth());
/* 122 */             int xEnd = yEnd - j;
/* 123 */             g.drawLine(0, j, xEnd, yEnd);
/* 124 */             j += 2;
/*     */           } 
/*     */           
/* 127 */           int i = 2;
/* 128 */           while (i < getWidth()) {
/* 129 */             int yEnd = y1 + getWidth() - i;
/* 130 */             g.drawLine(i, y1, getWidth(), yEnd);
/* 131 */             i += 2;
/*     */           } 
/*     */           
/* 134 */           if (realY1 >= y && realY1 < this.visibleRect.y + this.visibleRect.height) {
/* 135 */             g.drawLine(0, realY1, getWidth(), realY1);
/*     */           }
/* 137 */           if (y2 >= y && y2 < this.visibleRect.y + this.visibleRect.height) {
/* 138 */             g.drawLine(0, y2, getWidth(), y2);
/*     */           }
/*     */         }
/*     */       
/*     */       }
/* 143 */       catch (BadLocationException ble) {
/* 144 */         ble.printStackTrace();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 149 */     if (this.trackingIcons != null) {
/* 150 */       int lastLine = this.textArea.getLineCount() - 1;
/* 151 */       for (int i = this.trackingIcons.size() - 1; i >= 0; i--) {
/* 152 */         IconRowHeader.GutterIconImpl gutterIconImpl = getTrackingIcon(i);
/* 153 */         int offs = gutterIconImpl.getMarkedOffset();
/* 154 */         if (offs >= 0 && offs <= doc.getLength()) {
/* 155 */           int line = root.getElementIndex(offs);
/* 156 */           if (line <= lastLine && line >= topLine) {
/*     */             try {
/* 158 */               Icon icon = gutterIconImpl.getIcon();
/* 159 */               if (icon != null) {
/* 160 */                 int lineY = rsta.yForLine(line);
/* 161 */                 if (lineY >= y && lineY <= this.visibleRect.y + this.visibleRect.height) {
/* 162 */                   int y2 = lineY + (cellHeight - icon.getIconHeight()) / 2;
/* 163 */                   icon.paintIcon((Component)this, g, 0, y2);
/* 164 */                   lastLine = line - 1;
/*     */                 } 
/*     */               } 
/* 167 */             } catch (BadLocationException ble) {
/* 168 */               ble.printStackTrace();
/*     */             }
/*     */           
/* 171 */           } else if (line < topLine) {
/*     */             break;
/*     */           } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void paintComponentWrapped(Graphics g) {
/* 202 */     RSyntaxTextArea rsta = (RSyntaxTextArea)this.textArea;
/*     */     
/* 204 */     Document doc = this.textArea.getDocument();
/* 205 */     Element root = doc.getDefaultRootElement();
/* 206 */     int topPosition = this.textArea.viewToModel(new Point(this.visibleRect.x, this.visibleRect.y));
/*     */     
/* 208 */     int topLine = root.getElementIndex(topPosition);
/*     */     
/* 210 */     int topY = this.visibleRect.y;
/* 211 */     int bottomY = this.visibleRect.y + this.visibleRect.height;
/* 212 */     int cellHeight = this.textArea.getLineHeight();
/*     */ 
/*     */     
/* 215 */     if (this.trackingIcons != null) {
/* 216 */       int lastLine = this.textArea.getLineCount() - 1;
/* 217 */       for (int i = this.trackingIcons.size() - 1; i >= 0; i--) {
/* 218 */         IconRowHeader.GutterIconImpl gutterIconImpl = getTrackingIcon(i);
/* 219 */         Icon icon = gutterIconImpl.getIcon();
/* 220 */         if (icon != null) {
/* 221 */           int iconH = icon.getIconHeight();
/* 222 */           int offs = gutterIconImpl.getMarkedOffset();
/* 223 */           if (offs >= 0 && offs <= doc.getLength()) {
/* 224 */             int line = root.getElementIndex(offs);
/* 225 */             if (line <= lastLine && line >= topLine) {
/*     */               try {
/* 227 */                 int lineY = rsta.yForLine(line);
/* 228 */                 if (lineY <= bottomY && lineY + iconH >= topY) {
/* 229 */                   int y2 = lineY + (cellHeight - iconH) / 2;
/* 230 */                   gutterIconImpl.getIcon().paintIcon((Component)this, g, 0, y2);
/* 231 */                   lastLine = line - 1;
/*     */                 } 
/* 233 */               } catch (BadLocationException ble) {
/* 234 */                 ble.printStackTrace();
/*     */               }
/*     */             
/* 237 */             } else if (line < topLine) {
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\FoldingAwareIconRowHeader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */