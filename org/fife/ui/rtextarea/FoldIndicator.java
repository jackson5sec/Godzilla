/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JToolTip;
/*     */ import javax.swing.ToolTipManager;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.MouseInputAdapter;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.View;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rsyntaxtextarea.Token;
/*     */ import org.fife.ui.rsyntaxtextarea.focusabletip.TipUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FoldIndicator
/*     */   extends AbstractGutterComponent
/*     */ {
/*     */   private Insets textAreaInsets;
/*     */   private Rectangle visibleRect;
/*     */   private Fold foldWithOutlineShowing;
/*     */   private Color foldIconBackground;
/*     */   private Color foldIconArmedBackground;
/*     */   private Icon collapsedFoldIcon;
/*     */   private Icon expandedFoldIcon;
/*     */   private boolean mouseOverFoldIcon;
/*     */   private boolean paintFoldArmed;
/*     */   private boolean showFoldRegionTips;
/*     */   private int additionalLeftMargin;
/* 114 */   public static final Color DEFAULT_FOREGROUND = Color.GRAY;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   public static final Color DEFAULT_FOLD_BACKGROUND = Color.WHITE;
/*     */ 
/*     */ 
/*     */   
/*     */   private Listener listener;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int WIDTH = 12;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FoldIndicator(RTextArea textArea) {
/* 133 */     super(textArea);
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
/*     */   public JToolTip createToolTip() {
/* 146 */     JToolTip tip = super.createToolTip();
/* 147 */     tip.setBackground(TipUtil.getToolTipBackground(this.textArea));
/* 148 */     tip.setBorder(TipUtil.getToolTipBorder(this.textArea));
/* 149 */     return tip;
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
/*     */   public int getAdditionalLeftMargin() {
/* 162 */     return this.additionalLeftMargin;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Fold findOpenFoldClosestTo(Point p) {
/* 168 */     Fold fold = null;
/* 169 */     this.mouseOverFoldIcon = false;
/*     */     
/* 171 */     RSyntaxTextArea rsta = (RSyntaxTextArea)this.textArea;
/* 172 */     if (rsta.isCodeFoldingEnabled()) {
/* 173 */       int offs = rsta.viewToModel(p);
/* 174 */       if (offs > -1) {
/*     */         try {
/* 176 */           int line = rsta.getLineOfOffset(offs);
/* 177 */           FoldManager fm = rsta.getFoldManager();
/* 178 */           fold = fm.getFoldForLine(line);
/* 179 */           if (fold != null) {
/*     */             
/* 181 */             this.mouseOverFoldIcon = true;
/*     */           } else {
/*     */             
/* 184 */             fold = fm.getDeepestOpenFoldContaining(offs);
/*     */           } 
/* 186 */         } catch (BadLocationException ble) {
/* 187 */           ble.printStackTrace();
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 192 */     return fold;
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
/*     */   public Color getFoldIconArmedBackground() {
/* 207 */     return this.foldIconArmedBackground;
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
/*     */   public Color getFoldIconBackground() {
/* 220 */     return this.foldIconBackground;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Dimension getPreferredSize() {
/* 226 */     int h = (this.textArea != null) ? this.textArea.getHeight() : 100;
/* 227 */     return new Dimension(12 + this.additionalLeftMargin, h);
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
/*     */   public boolean getShowCollapsedRegionToolTips() {
/* 239 */     return this.showFoldRegionTips;
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
/*     */   public Point getToolTipLocation(MouseEvent e) {
/* 257 */     String text = getToolTipText(e);
/* 258 */     if (text == null) {
/* 259 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 263 */     Point p = e.getPoint();
/* 264 */     p.y = p.y / this.textArea.getLineHeight() * this.textArea.getLineHeight();
/* 265 */     p.x = getWidth() + (this.textArea.getMargin()).left;
/* 266 */     Gutter gutter = getGutter();
/* 267 */     int gutterMargin = (gutter.getInsets()).right;
/* 268 */     p.x += gutterMargin;
/* 269 */     JToolTip tempTip = createToolTip();
/* 270 */     p.x -= (tempTip.getInsets()).left;
/* 271 */     p.y += 16;
/* 272 */     return p;
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
/*     */   public String getToolTipText(MouseEvent e) {
/* 284 */     String text = null;
/*     */     
/* 286 */     RSyntaxTextArea rsta = (RSyntaxTextArea)this.textArea;
/* 287 */     if (rsta.isCodeFoldingEnabled()) {
/* 288 */       FoldManager fm = rsta.getFoldManager();
/* 289 */       int pos = rsta.viewToModel(new Point(0, e.getY()));
/* 290 */       if (pos >= 0) {
/* 291 */         int line = 0;
/*     */         try {
/* 293 */           line = rsta.getLineOfOffset(pos);
/* 294 */         } catch (BadLocationException ble) {
/* 295 */           ble.printStackTrace();
/* 296 */           return null;
/*     */         } 
/* 298 */         Fold fold = fm.getFoldForLine(line);
/* 299 */         if (fold != null && fold.isCollapsed()) {
/*     */           
/* 301 */           int endLine = fold.getEndLine();
/* 302 */           if (fold.getLineCount() > 25) {
/* 303 */             endLine = fold.getStartLine() + 25;
/*     */           }
/*     */           
/* 306 */           StringBuilder sb = new StringBuilder("<html><nobr>");
/* 307 */           while (line <= endLine && line < rsta.getLineCount()) {
/* 308 */             Token t = rsta.getTokenListForLine(line);
/* 309 */             while (t != null && t.isPaintable()) {
/* 310 */               t.appendHTMLRepresentation(sb, rsta, true, true);
/* 311 */               t = t.getNextToken();
/*     */             } 
/* 313 */             sb.append("<br>");
/* 314 */             line++;
/*     */           } 
/*     */           
/* 317 */           text = sb.toString();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 323 */     return text;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void handleDocumentEvent(DocumentEvent e) {
/* 330 */     int newLineCount = this.textArea.getLineCount();
/* 331 */     if (newLineCount != this.currentLineCount) {
/* 332 */       this.currentLineCount = newLineCount;
/* 333 */       repaint();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init() {
/* 340 */     super.init();
/* 341 */     setForeground(DEFAULT_FOREGROUND);
/* 342 */     setFoldIconBackground(DEFAULT_FOLD_BACKGROUND);
/* 343 */     this.collapsedFoldIcon = new FoldIcon(true);
/* 344 */     this.expandedFoldIcon = new FoldIcon(false);
/* 345 */     this.listener = new Listener(this);
/* 346 */     this.visibleRect = new Rectangle();
/* 347 */     setShowCollapsedRegionToolTips(true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void lineHeightsChanged() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintComponent(Graphics g) {
/* 360 */     if (this.textArea == null) {
/*     */       return;
/*     */     }
/*     */     
/* 364 */     this.visibleRect = g.getClipBounds(this.visibleRect);
/* 365 */     if (this.visibleRect == null) {
/* 366 */       this.visibleRect = getVisibleRect();
/*     */     }
/*     */     
/* 369 */     if (this.visibleRect == null) {
/*     */       return;
/*     */     }
/*     */     
/* 373 */     Color bg = getBackground();
/* 374 */     if (getGutter() != null) {
/* 375 */       bg = getGutter().getBackground();
/*     */     }
/* 377 */     g.setColor(bg);
/* 378 */     g.fillRect(0, this.visibleRect.y, getWidth(), this.visibleRect.height);
/*     */     
/* 380 */     RSyntaxTextArea rsta = (RSyntaxTextArea)this.textArea;
/* 381 */     if (!rsta.isCodeFoldingEnabled()) {
/*     */       return;
/*     */     }
/*     */     
/* 385 */     if (this.textArea.getLineWrap()) {
/* 386 */       paintComponentWrapped(g);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 393 */     this.textAreaInsets = this.textArea.getInsets(this.textAreaInsets);
/* 394 */     if (this.visibleRect.y < this.textAreaInsets.top) {
/* 395 */       this.visibleRect.height -= this.textAreaInsets.top - this.visibleRect.y;
/* 396 */       this.visibleRect.y = this.textAreaInsets.top;
/*     */     } 
/* 398 */     int cellHeight = this.textArea.getLineHeight();
/* 399 */     int topLine = (this.visibleRect.y - this.textAreaInsets.top) / cellHeight;
/*     */     
/* 401 */     int y = topLine * cellHeight + (cellHeight - this.collapsedFoldIcon.getIconHeight()) / 2;
/* 402 */     y += this.textAreaInsets.top;
/*     */ 
/*     */     
/* 405 */     FoldManager fm = rsta.getFoldManager();
/* 406 */     topLine += fm.getHiddenLineCountAbove(topLine, true);
/*     */     
/* 408 */     int width = getWidth();
/* 409 */     int x = width - 10;
/* 410 */     int line = topLine;
/*     */     
/* 412 */     boolean paintingOutlineLine = (this.foldWithOutlineShowing != null && this.foldWithOutlineShowing.containsLine(line));
/*     */     
/* 414 */     while (y < this.visibleRect.y + this.visibleRect.height) {
/* 415 */       if (paintingOutlineLine) {
/* 416 */         g.setColor(getForeground());
/* 417 */         int w2 = width - 6;
/* 418 */         if (line == this.foldWithOutlineShowing.getEndLine()) {
/* 419 */           int y2 = y + cellHeight / 2;
/* 420 */           g.drawLine(w2, y, w2, y2);
/* 421 */           g.drawLine(w2, y2, width - 2, y2);
/* 422 */           paintingOutlineLine = false;
/*     */         } else {
/*     */           
/* 425 */           g.drawLine(w2, y, w2, y + cellHeight);
/*     */         } 
/*     */       } 
/* 428 */       Fold fold = fm.getFoldForLine(line);
/* 429 */       if (fold != null) {
/* 430 */         if (fold == this.foldWithOutlineShowing) {
/* 431 */           if (!fold.isCollapsed()) {
/* 432 */             g.setColor(getForeground());
/* 433 */             int w2 = width - 6;
/* 434 */             g.drawLine(w2, y + cellHeight / 2, w2, y + cellHeight);
/* 435 */             paintingOutlineLine = true;
/*     */           } 
/* 437 */           if (this.mouseOverFoldIcon) {
/* 438 */             this.paintFoldArmed = true;
/*     */           }
/*     */         } 
/* 441 */         if (fold.isCollapsed()) {
/* 442 */           this.collapsedFoldIcon.paintIcon(this, g, x, y);
/*     */ 
/*     */           
/*     */           do {
/* 446 */             int hiddenLineCount = fold.getLineCount();
/* 447 */             if (hiddenLineCount == 0) {
/*     */               break;
/*     */             }
/*     */ 
/*     */ 
/*     */             
/* 453 */             line += hiddenLineCount;
/* 454 */             fold = fm.getFoldForLine(line);
/* 455 */           } while (fold != null && fold.isCollapsed());
/*     */         } else {
/*     */           
/* 458 */           this.expandedFoldIcon.paintIcon(this, g, x, y);
/*     */         } 
/* 460 */         this.paintFoldArmed = false;
/*     */       } 
/* 462 */       line++;
/* 463 */       y += cellHeight;
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
/*     */   private void paintComponentWrapped(Graphics g) {
/* 506 */     int width = getWidth();
/*     */     
/* 508 */     RTextAreaUI ui = (RTextAreaUI)this.textArea.getUI();
/* 509 */     View v = ui.getRootView(this.textArea).getView(0);
/* 510 */     Document doc = this.textArea.getDocument();
/* 511 */     Element root = doc.getDefaultRootElement();
/* 512 */     int topPosition = this.textArea.viewToModel(new Point(this.visibleRect.x, this.visibleRect.y));
/*     */     
/* 514 */     int topLine = root.getElementIndex(topPosition);
/* 515 */     int cellHeight = this.textArea.getLineHeight();
/* 516 */     FoldManager fm = ((RSyntaxTextArea)this.textArea).getFoldManager();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 522 */     Rectangle visibleEditorRect = ui.getVisibleEditorRect();
/* 523 */     Rectangle r = LineNumberList.getChildViewBounds(v, topLine, visibleEditorRect);
/*     */     
/* 525 */     int y = r.y;
/* 526 */     y += (cellHeight - this.collapsedFoldIcon.getIconHeight()) / 2;
/*     */     
/* 528 */     int visibleBottom = this.visibleRect.y + this.visibleRect.height;
/* 529 */     int x = width - 10;
/* 530 */     int line = topLine;
/*     */     
/* 532 */     boolean paintingOutlineLine = (this.foldWithOutlineShowing != null && this.foldWithOutlineShowing.containsLine(line));
/* 533 */     int lineCount = root.getElementCount();
/*     */     
/* 535 */     while (y < visibleBottom && line < lineCount) {
/*     */       
/* 537 */       int curLineH = (LineNumberList.getChildViewBounds(v, line, visibleEditorRect)).height;
/*     */ 
/*     */       
/* 540 */       if (paintingOutlineLine) {
/* 541 */         g.setColor(getForeground());
/* 542 */         int w2 = width - 6;
/* 543 */         if (line == this.foldWithOutlineShowing.getEndLine()) {
/* 544 */           int y2 = y + curLineH - cellHeight / 2;
/* 545 */           g.drawLine(w2, y, w2, y2);
/* 546 */           g.drawLine(w2, y2, width - 2, y2);
/* 547 */           paintingOutlineLine = false;
/*     */         } else {
/*     */           
/* 550 */           g.drawLine(w2, y, w2, y + curLineH);
/*     */         } 
/*     */       } 
/* 553 */       Fold fold = fm.getFoldForLine(line);
/* 554 */       if (fold != null) {
/* 555 */         if (fold == this.foldWithOutlineShowing) {
/* 556 */           if (!fold.isCollapsed()) {
/* 557 */             g.setColor(getForeground());
/* 558 */             int w2 = width - 6;
/* 559 */             g.drawLine(w2, y + cellHeight / 2, w2, y + curLineH);
/* 560 */             paintingOutlineLine = true;
/*     */           } 
/* 562 */           if (this.mouseOverFoldIcon) {
/* 563 */             this.paintFoldArmed = true;
/*     */           }
/*     */         } 
/* 566 */         if (fold.isCollapsed()) {
/* 567 */           this.collapsedFoldIcon.paintIcon(this, g, x, y);
/* 568 */           y += (LineNumberList.getChildViewBounds(v, line, visibleEditorRect)).height;
/*     */           
/* 570 */           line += fold.getLineCount() + 1;
/*     */         } else {
/*     */           
/* 573 */           this.expandedFoldIcon.paintIcon(this, g, x, y);
/* 574 */           y += curLineH;
/* 575 */           line++;
/*     */         } 
/* 577 */         this.paintFoldArmed = false;
/*     */         continue;
/*     */       } 
/* 580 */       y += curLineH;
/* 581 */       line++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int rowAtPoint(Point p) {
/* 590 */     int line = 0;
/*     */     
/*     */     try {
/* 593 */       int offs = this.textArea.viewToModel(p);
/* 594 */       if (offs > -1) {
/* 595 */         line = this.textArea.getLineOfOffset(offs);
/*     */       }
/* 597 */     } catch (BadLocationException ble) {
/* 598 */       ble.printStackTrace();
/*     */     } 
/*     */     
/* 601 */     return line;
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
/*     */   public void setAdditionalLeftMargin(int leftMargin) {
/* 617 */     if (leftMargin < 0) {
/* 618 */       throw new IllegalArgumentException("leftMargin must be >= 0");
/*     */     }
/*     */     
/* 621 */     this.additionalLeftMargin = leftMargin;
/* 622 */     revalidate();
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
/*     */   public void setFoldIconArmedBackground(Color bg) {
/* 636 */     this.foldIconArmedBackground = bg;
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
/*     */   public void setFoldIconBackground(Color bg) {
/* 649 */     this.foldIconBackground = bg;
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
/*     */   public void setFoldIcons(Icon collapsedIcon, Icon expandedIcon) {
/* 662 */     this.collapsedFoldIcon = collapsedIcon;
/* 663 */     this.expandedFoldIcon = expandedIcon;
/* 664 */     revalidate();
/* 665 */     repaint();
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
/*     */   public void setShowCollapsedRegionToolTips(boolean show) {
/* 677 */     if (show != this.showFoldRegionTips) {
/* 678 */       if (show) {
/* 679 */         ToolTipManager.sharedInstance().registerComponent(this);
/*     */       } else {
/*     */         
/* 682 */         ToolTipManager.sharedInstance().unregisterComponent(this);
/*     */       } 
/* 684 */       this.showFoldRegionTips = show;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTextArea(RTextArea textArea) {
/* 694 */     if (this.textArea != null) {
/* 695 */       this.textArea.removePropertyChangeListener("RSTA.codeFolding", this.listener);
/*     */     }
/*     */     
/* 698 */     super.setTextArea(textArea);
/* 699 */     if (this.textArea != null) {
/* 700 */       this.textArea.addPropertyChangeListener("RSTA.codeFolding", this.listener);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class FoldIcon
/*     */     implements Icon
/*     */   {
/*     */     private boolean collapsed;
/*     */ 
/*     */ 
/*     */     
/*     */     FoldIcon(boolean collapsed) {
/* 714 */       this.collapsed = collapsed;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getIconHeight() {
/* 719 */       return 8;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getIconWidth() {
/* 724 */       return 8;
/*     */     }
/*     */ 
/*     */     
/*     */     public void paintIcon(Component c, Graphics g, int x, int y) {
/* 729 */       Color bg = FoldIndicator.this.foldIconBackground;
/* 730 */       if (FoldIndicator.this.paintFoldArmed && FoldIndicator.this.foldIconArmedBackground != null) {
/* 731 */         bg = FoldIndicator.this.foldIconArmedBackground;
/*     */       }
/* 733 */       g.setColor(bg);
/* 734 */       g.fillRect(x, y, 8, 8);
/* 735 */       g.setColor(FoldIndicator.this.getForeground());
/* 736 */       g.drawRect(x, y, 8, 8);
/* 737 */       g.drawLine(x + 2, y + 4, x + 2 + 4, y + 4);
/* 738 */       if (this.collapsed) {
/* 739 */         g.drawLine(x + 4, y + 2, x + 4, y + 6);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class Listener
/*     */     extends MouseInputAdapter
/*     */     implements PropertyChangeListener
/*     */   {
/*     */     Listener(FoldIndicator fgc) {
/* 753 */       fgc.addMouseListener(this);
/* 754 */       fgc.addMouseMotionListener(this);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void mouseClicked(MouseEvent e) {
/* 760 */       Point p = e.getPoint();
/* 761 */       int line = FoldIndicator.this.rowAtPoint(p);
/*     */       
/* 763 */       RSyntaxTextArea rsta = (RSyntaxTextArea)FoldIndicator.this.textArea;
/* 764 */       FoldManager fm = rsta.getFoldManager();
/*     */       
/* 766 */       Fold fold = fm.getFoldForLine(line);
/* 767 */       if (fold != null) {
/* 768 */         fold.toggleCollapsedState();
/* 769 */         FoldIndicator.this.getGutter().repaint();
/* 770 */         FoldIndicator.this.textArea.repaint();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void mouseExited(MouseEvent e) {
/* 777 */       if (FoldIndicator.this.foldWithOutlineShowing != null) {
/* 778 */         FoldIndicator.this.foldWithOutlineShowing = null;
/* 779 */         FoldIndicator.this.mouseOverFoldIcon = false;
/* 780 */         FoldIndicator.this.repaint();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void mouseMoved(MouseEvent e) {
/* 786 */       boolean oldMouseOverFoldIcon = FoldIndicator.this.mouseOverFoldIcon;
/* 787 */       Fold newSelectedFold = FoldIndicator.this.findOpenFoldClosestTo(e.getPoint());
/* 788 */       if (newSelectedFold != FoldIndicator.this.foldWithOutlineShowing && newSelectedFold != null && 
/* 789 */         !newSelectedFold.isOnSingleLine()) {
/* 790 */         FoldIndicator.this.foldWithOutlineShowing = newSelectedFold;
/* 791 */         FoldIndicator.this.repaint();
/*     */       }
/* 793 */       else if (FoldIndicator.this.mouseOverFoldIcon != oldMouseOverFoldIcon) {
/* 794 */         FoldIndicator.this.repaint();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void propertyChange(PropertyChangeEvent e) {
/* 801 */       FoldIndicator.this.repaint();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\FoldIndicator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */