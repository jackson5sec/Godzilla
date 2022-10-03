/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.Map;
/*     */ import javax.swing.event.CaretEvent;
/*     */ import javax.swing.event.CaretListener;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.MouseInputListener;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.View;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
/*     */ import org.fife.ui.rsyntaxtextarea.folding.Fold;
/*     */ import org.fife.ui.rsyntaxtextarea.folding.FoldManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LineNumberList
/*     */   extends AbstractGutterComponent
/*     */   implements MouseInputListener
/*     */ {
/*     */   private int currentLine;
/*  50 */   private int lastY = -1;
/*     */ 
/*     */ 
/*     */   
/*     */   private int lastVisibleLine;
/*     */ 
/*     */ 
/*     */   
/*     */   private int cellHeight;
/*     */ 
/*     */ 
/*     */   
/*     */   private int cellWidth;
/*     */ 
/*     */ 
/*     */   
/*     */   private int ascent;
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<?, ?> aaHints;
/*     */ 
/*     */ 
/*     */   
/*     */   private int mouseDragStartOffset;
/*     */ 
/*     */ 
/*     */   
/*     */   private Listener l;
/*     */ 
/*     */ 
/*     */   
/*     */   private Insets textAreaInsets;
/*     */ 
/*     */ 
/*     */   
/*     */   private Rectangle visibleRect;
/*     */ 
/*     */   
/*     */   private int lineNumberingStartIndex;
/*     */ 
/*     */ 
/*     */   
/*     */   public LineNumberList(RTextArea textArea) {
/*  94 */     this(textArea, (Color)null);
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
/*     */   public LineNumberList(RTextArea textArea, Color numberColor) {
/* 108 */     super(textArea);
/*     */     
/* 110 */     if (numberColor != null) {
/* 111 */       setForeground(numberColor);
/*     */     } else {
/*     */       
/* 114 */       setForeground(Color.GRAY);
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
/*     */   public void addNotify() {
/* 127 */     super.addNotify();
/* 128 */     if (this.textArea != null) {
/* 129 */       this.l.install(this.textArea);
/*     */     }
/* 131 */     updateCellWidths();
/* 132 */     updateCellHeights();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int calculateLastVisibleLineNumber() {
/* 142 */     int lastLine = 0;
/* 143 */     if (this.textArea != null) {
/* 144 */       lastLine = this.textArea.getLineCount() + getLineNumberingStartIndex() - 1;
/*     */     }
/* 146 */     return lastLine;
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
/*     */   public int getLineNumberingStartIndex() {
/* 158 */     return this.lineNumberingStartIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Dimension getPreferredSize() {
/* 164 */     int h = (this.textArea != null) ? this.textArea.getHeight() : 100;
/* 165 */     return new Dimension(this.cellWidth, h);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getRhsBorderWidth() {
/* 176 */     int w = 4;
/* 177 */     if (this.textArea instanceof RSyntaxTextArea && (
/* 178 */       (RSyntaxTextArea)this.textArea).isCodeFoldingEnabled()) {
/* 179 */       w = 0;
/*     */     }
/*     */     
/* 182 */     return w;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void handleDocumentEvent(DocumentEvent e) {
/* 188 */     int newLastLine = calculateLastVisibleLineNumber();
/* 189 */     if (newLastLine != this.lastVisibleLine) {
/*     */ 
/*     */       
/* 192 */       if (newLastLine / 10 != this.lastVisibleLine / 10) {
/* 193 */         updateCellWidths();
/*     */       }
/* 195 */       this.lastVisibleLine = newLastLine;
/* 196 */       repaint();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init() {
/* 204 */     super.init();
/*     */ 
/*     */ 
/*     */     
/* 208 */     this.currentLine = 0;
/* 209 */     setLineNumberingStartIndex(1);
/*     */     
/* 211 */     this.visibleRect = new Rectangle();
/*     */     
/* 213 */     addMouseListener(this);
/* 214 */     addMouseMotionListener(this);
/*     */     
/* 216 */     this.aaHints = RSyntaxUtilities.getDesktopAntiAliasHints();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void lineHeightsChanged() {
/* 223 */     updateCellHeights();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mouseClicked(MouseEvent e) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void mouseDragged(MouseEvent e) {
/* 234 */     if (this.mouseDragStartOffset > -1) {
/* 235 */       int pos = this.textArea.viewToModel(new Point(0, e.getY()));
/* 236 */       if (pos >= 0) {
/* 237 */         this.textArea.setCaretPosition(this.mouseDragStartOffset);
/* 238 */         this.textArea.moveCaretPosition(pos);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mouseEntered(MouseEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mouseExited(MouseEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mouseMoved(MouseEvent e) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void mousePressed(MouseEvent e) {
/* 261 */     if (this.textArea == null) {
/*     */       return;
/*     */     }
/* 264 */     if (e.getButton() == 1) {
/* 265 */       int pos = this.textArea.viewToModel(new Point(0, e.getY()));
/* 266 */       if (pos >= 0) {
/* 267 */         this.textArea.setCaretPosition(pos);
/*     */       }
/* 269 */       this.mouseDragStartOffset = pos;
/*     */     } else {
/*     */       
/* 272 */       this.mouseDragStartOffset = -1;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mouseReleased(MouseEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintComponent(Graphics g) {
/* 290 */     if (this.textArea == null) {
/*     */       return;
/*     */     }
/*     */     
/* 294 */     this.visibleRect = g.getClipBounds(this.visibleRect);
/* 295 */     if (this.visibleRect == null) {
/* 296 */       this.visibleRect = getVisibleRect();
/*     */     }
/*     */     
/* 299 */     if (this.visibleRect == null) {
/*     */       return;
/*     */     }
/*     */     
/* 303 */     Color bg = getBackground();
/* 304 */     if (getGutter() != null) {
/* 305 */       bg = getGutter().getBackground();
/*     */     }
/* 307 */     g.setColor(bg);
/* 308 */     g.fillRect(0, this.visibleRect.y, this.cellWidth, this.visibleRect.height);
/* 309 */     g.setFont(getFont());
/* 310 */     if (this.aaHints != null) {
/* 311 */       ((Graphics2D)g).addRenderingHints(this.aaHints);
/*     */     }
/*     */     
/* 314 */     if (this.textArea.getLineWrap()) {
/* 315 */       paintWrappedLineNumbers(g, this.visibleRect);
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 323 */     this.textAreaInsets = this.textArea.getInsets(this.textAreaInsets);
/* 324 */     if (this.visibleRect.y < this.textAreaInsets.top) {
/* 325 */       this.visibleRect.height -= this.textAreaInsets.top - this.visibleRect.y;
/* 326 */       this.visibleRect.y = this.textAreaInsets.top;
/*     */     } 
/* 328 */     int topLine = (this.visibleRect.y - this.textAreaInsets.top) / this.cellHeight;
/* 329 */     int actualTopY = topLine * this.cellHeight + this.textAreaInsets.top;
/* 330 */     int y = actualTopY + this.ascent;
/*     */ 
/*     */     
/* 333 */     FoldManager fm = null;
/* 334 */     if (this.textArea instanceof RSyntaxTextArea) {
/* 335 */       fm = ((RSyntaxTextArea)this.textArea).getFoldManager();
/* 336 */       topLine += fm.getHiddenLineCountAbove(topLine, true);
/*     */     } 
/* 338 */     int rhsBorderWidth = getRhsBorderWidth();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 351 */     g.setColor(getForeground());
/* 352 */     boolean ltr = getComponentOrientation().isLeftToRight();
/* 353 */     if (ltr) {
/* 354 */       FontMetrics metrics = g.getFontMetrics();
/* 355 */       int rhs = getWidth() - rhsBorderWidth;
/* 356 */       int line = topLine + 1;
/* 357 */       while (y < this.visibleRect.y + this.visibleRect.height + this.ascent && line <= this.textArea.getLineCount()) {
/* 358 */         String number = Integer.toString(line + getLineNumberingStartIndex() - 1);
/* 359 */         int width = metrics.stringWidth(number);
/* 360 */         g.drawString(number, rhs - width, y);
/* 361 */         y += this.cellHeight;
/* 362 */         if (fm != null) {
/* 363 */           Fold fold = fm.getFoldForLine(line - 1);
/*     */ 
/*     */           
/* 366 */           while (fold != null && fold.isCollapsed()) {
/* 367 */             int hiddenLineCount = fold.getLineCount();
/* 368 */             if (hiddenLineCount == 0) {
/*     */               break;
/*     */             }
/*     */ 
/*     */             
/* 373 */             line += hiddenLineCount;
/* 374 */             fold = fm.getFoldForLine(line - 1);
/*     */           } 
/*     */         } 
/* 377 */         line++;
/*     */       } 
/*     */     } else {
/*     */       
/* 381 */       int line = topLine + 1;
/* 382 */       while (y < this.visibleRect.y + this.visibleRect.height && line < this.textArea.getLineCount()) {
/* 383 */         String number = Integer.toString(line + getLineNumberingStartIndex() - 1);
/* 384 */         g.drawString(number, rhsBorderWidth, y);
/* 385 */         y += this.cellHeight;
/* 386 */         if (fm != null) {
/* 387 */           Fold fold = fm.getFoldForLine(line - 1);
/*     */ 
/*     */           
/* 390 */           while (fold != null && fold.isCollapsed()) {
/* 391 */             line += fold.getLineCount();
/* 392 */             fold = fm.getFoldForLine(line);
/*     */           } 
/*     */         } 
/* 395 */         line++;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void paintWrappedLineNumbers(Graphics g, Rectangle visibleRect) {
/* 440 */     int rhs, width = getWidth();
/*     */     
/* 442 */     RTextAreaUI ui = (RTextAreaUI)this.textArea.getUI();
/* 443 */     View v = ui.getRootView(this.textArea).getView(0);
/*     */     
/* 445 */     Document doc = this.textArea.getDocument();
/* 446 */     Element root = doc.getDefaultRootElement();
/* 447 */     int lineCount = root.getElementCount();
/* 448 */     int topPosition = this.textArea.viewToModel(new Point(visibleRect.x, visibleRect.y));
/*     */     
/* 450 */     int topLine = root.getElementIndex(topPosition);
/* 451 */     FoldManager fm = null;
/* 452 */     if (this.textArea instanceof RSyntaxTextArea) {
/* 453 */       fm = ((RSyntaxTextArea)this.textArea).getFoldManager();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 460 */     Rectangle visibleEditorRect = ui.getVisibleEditorRect();
/* 461 */     Rectangle r = getChildViewBounds(v, topLine, visibleEditorRect);
/*     */     
/* 463 */     int y = r.y;
/* 464 */     int rhsBorderWidth = getRhsBorderWidth();
/*     */     
/* 466 */     boolean ltr = getComponentOrientation().isLeftToRight();
/* 467 */     if (ltr) {
/* 468 */       rhs = width - rhsBorderWidth;
/*     */     } else {
/*     */       
/* 471 */       rhs = rhsBorderWidth;
/*     */     } 
/* 473 */     int visibleBottom = visibleRect.y + visibleRect.height;
/* 474 */     FontMetrics metrics = g.getFontMetrics();
/*     */ 
/*     */ 
/*     */     
/* 478 */     g.setColor(getForeground());
/*     */     
/* 480 */     while (y < visibleBottom) {
/*     */       
/* 482 */       r = getChildViewBounds(v, topLine, visibleEditorRect);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 494 */       int index = topLine + 1 + getLineNumberingStartIndex() - 1;
/* 495 */       String number = Integer.toString(index);
/* 496 */       if (ltr) {
/* 497 */         int strWidth = metrics.stringWidth(number);
/* 498 */         g.drawString(number, rhs - strWidth, y + this.ascent);
/*     */       } else {
/*     */         
/* 501 */         int x = rhsBorderWidth;
/* 502 */         g.drawString(number, x, y + this.ascent);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 507 */       y += r.height;
/*     */ 
/*     */ 
/*     */       
/* 511 */       if (fm != null) {
/* 512 */         Fold fold = fm.getFoldForLine(topLine);
/* 513 */         if (fold != null && fold.isCollapsed()) {
/* 514 */           topLine += fold.getCollapsedLineCount();
/*     */         }
/*     */       } 
/* 517 */       topLine++;
/* 518 */       if (topLine >= lineCount) {
/*     */         break;
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
/*     */   public void removeNotify() {
/* 532 */     super.removeNotify();
/* 533 */     if (this.textArea != null) {
/* 534 */       this.l.uninstall(this.textArea);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void repaintLine(int line) {
/* 545 */     int y = (this.textArea.getInsets()).top;
/* 546 */     y += line * this.cellHeight;
/* 547 */     repaint(0, y, this.cellWidth, this.cellHeight);
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
/*     */   public void setFont(Font font) {
/* 559 */     super.setFont(font);
/* 560 */     updateCellWidths();
/* 561 */     updateCellHeights();
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
/*     */   public void setLineNumberingStartIndex(int index) {
/* 574 */     if (index != this.lineNumberingStartIndex) {
/* 575 */       this.lineNumberingStartIndex = index;
/* 576 */       updateCellWidths();
/* 577 */       repaint();
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
/*     */   public void setTextArea(RTextArea textArea) {
/* 590 */     if (this.l == null) {
/* 591 */       this.l = new Listener();
/*     */     }
/*     */     
/* 594 */     if (this.textArea != null) {
/* 595 */       this.l.uninstall(textArea);
/*     */     }
/*     */     
/* 598 */     super.setTextArea(textArea);
/* 599 */     this.lastVisibleLine = calculateLastVisibleLineNumber();
/*     */     
/* 601 */     if (textArea != null) {
/* 602 */       this.l.install(textArea);
/* 603 */       updateCellHeights();
/* 604 */       updateCellWidths();
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
/*     */   private void updateCellHeights() {
/* 616 */     if (this.textArea != null) {
/* 617 */       this.cellHeight = this.textArea.getLineHeight();
/* 618 */       this.ascent = this.textArea.getMaxAscent();
/*     */     } else {
/*     */       
/* 621 */       this.cellHeight = 20;
/* 622 */       this.ascent = 5;
/*     */     } 
/* 624 */     repaint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void updateCellWidths() {
/* 634 */     int oldCellWidth = this.cellWidth;
/* 635 */     this.cellWidth = getRhsBorderWidth();
/*     */ 
/*     */     
/* 638 */     if (this.textArea != null) {
/* 639 */       Font font = getFont();
/* 640 */       if (font != null) {
/* 641 */         FontMetrics fontMetrics = getFontMetrics(font);
/* 642 */         int count = 0;
/*     */         
/* 644 */         int lineCount = this.textArea.getLineCount() + getLineNumberingStartIndex() - 1;
/*     */         while (true) {
/* 646 */           lineCount /= 10;
/* 647 */           count++;
/* 648 */           if (lineCount < 10) {
/* 649 */             this.cellWidth += fontMetrics.charWidth('9') * (count + 1) + 3; break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 653 */     }  if (this.cellWidth != oldCellWidth) {
/* 654 */       revalidate();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class Listener
/*     */     implements CaretListener, PropertyChangeListener
/*     */   {
/*     */     private boolean installed;
/*     */ 
/*     */     
/*     */     private Listener() {}
/*     */ 
/*     */     
/*     */     public void caretUpdate(CaretEvent e) {
/* 670 */       int dot = LineNumberList.this.textArea.getCaretPosition();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 680 */       if (!LineNumberList.this.textArea.getLineWrap()) {
/*     */         
/* 682 */         int line = LineNumberList.this.textArea.getDocument().getDefaultRootElement().getElementIndex(dot);
/* 683 */         if (LineNumberList.this.currentLine != line) {
/* 684 */           LineNumberList.this.repaintLine(line);
/* 685 */           LineNumberList.this.repaintLine(LineNumberList.this.currentLine);
/* 686 */           LineNumberList.this.currentLine = line;
/*     */         } 
/*     */       } else {
/*     */         
/*     */         try {
/* 691 */           int y = LineNumberList.this.textArea.yForLineContaining(dot);
/* 692 */           if (y != LineNumberList.this.lastY) {
/* 693 */             LineNumberList.this.lastY = y;
/* 694 */             LineNumberList.this.currentLine = LineNumberList.this.textArea.getDocument()
/* 695 */               .getDefaultRootElement().getElementIndex(dot);
/* 696 */             LineNumberList.this.repaint();
/*     */           } 
/* 698 */         } catch (BadLocationException ble) {
/* 699 */           ble.printStackTrace();
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void install(RTextArea textArea) {
/* 706 */       if (!this.installed) {
/*     */         
/* 708 */         textArea.addCaretListener(this);
/* 709 */         textArea.addPropertyChangeListener(this);
/* 710 */         caretUpdate(null);
/* 711 */         this.installed = true;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void propertyChange(PropertyChangeEvent e) {
/* 718 */       String name = e.getPropertyName();
/*     */ 
/*     */       
/* 721 */       if ("RTA.currentLineHighlight".equals(name) || "RTA.currentLineHighlightColor"
/* 722 */         .equals(name)) {
/* 723 */         LineNumberList.this.repaintLine(LineNumberList.this.currentLine);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void uninstall(RTextArea textArea) {
/* 729 */       if (this.installed) {
/*     */         
/* 731 */         textArea.removeCaretListener(this);
/* 732 */         textArea.removePropertyChangeListener(this);
/* 733 */         this.installed = false;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\LineNumberList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */