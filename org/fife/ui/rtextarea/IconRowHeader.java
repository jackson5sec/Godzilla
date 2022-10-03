/*     */ package org.fife.ui.rtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.ToolTipManager;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.Position;
/*     */ import javax.swing.text.View;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IconRowHeader
/*     */   extends AbstractGutterComponent
/*     */   implements MouseListener
/*     */ {
/*     */   protected List<GutterIconImpl> trackingIcons;
/*     */   protected int width;
/*     */   private boolean bookmarkingEnabled;
/*     */   private Icon bookmarkIcon;
/*     */   protected Rectangle visibleRect;
/*     */   protected Insets textAreaInsets;
/*     */   protected int activeLineRangeStart;
/*     */   protected int activeLineRangeEnd;
/*     */   private Color activeLineRangeColor;
/*     */   private boolean inheritsGutterBackground;
/*     */   
/*     */   public IconRowHeader(RTextArea textArea) {
/* 120 */     super(textArea);
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
/*     */   public GutterIconInfo addOffsetTrackingIcon(int offs, Icon icon) throws BadLocationException {
/* 138 */     return addOffsetTrackingIcon(offs, icon, (String)null);
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
/*     */   public GutterIconInfo addOffsetTrackingIcon(int offs, Icon icon, String tip) throws BadLocationException {
/* 160 */     if (offs < 0 || offs > this.textArea.getDocument().getLength()) {
/* 161 */       throw new BadLocationException("Offset " + offs + " not in required range of 0-" + this.textArea
/* 162 */           .getDocument().getLength(), offs);
/*     */     }
/*     */     
/* 165 */     Position pos = this.textArea.getDocument().createPosition(offs);
/* 166 */     GutterIconImpl ti = new GutterIconImpl(icon, pos, tip);
/* 167 */     if (this.trackingIcons == null) {
/* 168 */       this.trackingIcons = new ArrayList<>(1);
/*     */     }
/* 170 */     int index = Collections.binarySearch((List)this.trackingIcons, ti);
/* 171 */     if (index < 0) {
/* 172 */       index = -(index + 1);
/*     */     }
/* 174 */     this.trackingIcons.add(index, ti);
/* 175 */     repaint();
/* 176 */     return ti;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearActiveLineRange() {
/* 186 */     if (this.activeLineRangeStart != -1 || this.activeLineRangeEnd != -1) {
/* 187 */       this.activeLineRangeStart = this.activeLineRangeEnd = -1;
/* 188 */       repaint();
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
/*     */   public Color getActiveLineRangeColor() {
/* 200 */     return this.activeLineRangeColor;
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
/*     */   public Icon getBookmarkIcon() {
/* 213 */     return this.bookmarkIcon;
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
/*     */   public GutterIconInfo[] getBookmarks() {
/* 225 */     List<GutterIconInfo> retVal = new ArrayList<>(1);
/*     */     
/* 227 */     if (this.trackingIcons != null) {
/* 228 */       for (int i = 0; i < this.trackingIcons.size(); i++) {
/* 229 */         GutterIconImpl ti = getTrackingIcon(i);
/* 230 */         if (ti.getIcon() == this.bookmarkIcon) {
/* 231 */           retVal.add(ti);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 236 */     GutterIconInfo[] array = new GutterIconInfo[retVal.size()];
/* 237 */     return retVal.<GutterIconInfo>toArray(array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void handleDocumentEvent(DocumentEvent e) {
/* 244 */     int newLineCount = this.textArea.getLineCount();
/* 245 */     if (newLineCount != this.currentLineCount) {
/* 246 */       this.currentLineCount = newLineCount;
/* 247 */       repaint();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Dimension getPreferredSize() {
/* 254 */     int h = (this.textArea != null) ? this.textArea.getHeight() : 100;
/* 255 */     return new Dimension(this.width, h);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getToolTipText(MouseEvent e) {
/*     */     try {
/* 267 */       int line = viewToModelLine(e.getPoint());
/* 268 */       if (line > -1) {
/* 269 */         GutterIconInfo[] infos = getTrackingIcons(line);
/* 270 */         if (infos.length > 0)
/*     */         {
/* 272 */           return infos[infos.length - 1].getToolTip();
/*     */         }
/*     */       } 
/* 275 */     } catch (BadLocationException ble) {
/* 276 */       ble.printStackTrace();
/*     */     } 
/* 278 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected GutterIconImpl getTrackingIcon(int index) {
/* 283 */     return this.trackingIcons.get(index);
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
/*     */   public GutterIconInfo[] getTrackingIcons(int line) throws BadLocationException {
/* 298 */     List<GutterIconInfo> retVal = new ArrayList<>(1);
/*     */     
/* 300 */     if (this.trackingIcons != null) {
/* 301 */       int start = this.textArea.getLineStartOffset(line);
/* 302 */       int end = this.textArea.getLineEndOffset(line);
/* 303 */       if (line == this.textArea.getLineCount() - 1) {
/* 304 */         end++;
/*     */       }
/* 306 */       for (int i = 0; i < this.trackingIcons.size(); i++) {
/* 307 */         GutterIconImpl ti = getTrackingIcon(i);
/* 308 */         int offs = ti.getMarkedOffset();
/* 309 */         if (offs >= start && offs < end) {
/* 310 */           retVal.add(ti);
/*     */         }
/* 312 */         else if (offs >= end) {
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 318 */     GutterIconInfo[] array = new GutterIconInfo[retVal.size()];
/* 319 */     return retVal.<GutterIconInfo>toArray(array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init() {
/* 327 */     super.init();
/*     */     
/* 329 */     this.visibleRect = new Rectangle();
/* 330 */     this.width = 16;
/* 331 */     addMouseListener(this);
/* 332 */     this.activeLineRangeStart = this.activeLineRangeEnd = -1;
/* 333 */     setActiveLineRangeColor((Color)null);
/*     */ 
/*     */ 
/*     */     
/* 337 */     updateBackground();
/*     */     
/* 339 */     ToolTipManager.sharedInstance().registerComponent(this);
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
/*     */   public boolean isBookmarkingEnabled() {
/* 351 */     return this.bookmarkingEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void lineHeightsChanged() {
/* 357 */     repaint();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mouseClicked(MouseEvent e) {}
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
/*     */   public void mousePressed(MouseEvent e) {
/* 378 */     if (this.bookmarkingEnabled && this.bookmarkIcon != null) {
/*     */       try {
/* 380 */         int line = viewToModelLine(e.getPoint());
/* 381 */         if (line > -1) {
/* 382 */           toggleBookmark(line);
/*     */         }
/* 384 */       } catch (BadLocationException ble) {
/* 385 */         ble.printStackTrace();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mouseReleased(MouseEvent e) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintComponent(Graphics g) {
/* 399 */     if (this.textArea == null) {
/*     */       return;
/*     */     }
/*     */     
/* 403 */     this.visibleRect = g.getClipBounds(this.visibleRect);
/* 404 */     if (this.visibleRect == null) {
/* 405 */       this.visibleRect = getVisibleRect();
/*     */     }
/*     */     
/* 408 */     if (this.visibleRect == null) {
/*     */       return;
/*     */     }
/* 411 */     paintBackgroundImpl(g, this.visibleRect);
/*     */     
/* 413 */     if (this.textArea.getLineWrap()) {
/* 414 */       paintComponentWrapped(g);
/*     */       
/*     */       return;
/*     */     } 
/* 418 */     Document doc = this.textArea.getDocument();
/* 419 */     Element root = doc.getDefaultRootElement();
/* 420 */     this.textAreaInsets = this.textArea.getInsets(this.textAreaInsets);
/* 421 */     if (this.visibleRect.y < this.textAreaInsets.top) {
/* 422 */       this.visibleRect.height -= this.textAreaInsets.top - this.visibleRect.y;
/* 423 */       this.visibleRect.y = this.textAreaInsets.top;
/*     */     } 
/*     */ 
/*     */     
/* 427 */     int cellHeight = this.textArea.getLineHeight();
/* 428 */     int topLine = (this.visibleRect.y - this.textAreaInsets.top) / cellHeight;
/* 429 */     int bottomLine = Math.min(topLine + this.visibleRect.height / cellHeight + 1, root
/* 430 */         .getElementCount());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 435 */     int y = topLine * cellHeight + this.textAreaInsets.top;
/*     */     
/* 437 */     if ((this.activeLineRangeStart >= topLine && this.activeLineRangeStart <= bottomLine) || (this.activeLineRangeEnd >= topLine && this.activeLineRangeEnd <= bottomLine) || (this.activeLineRangeStart <= topLine && this.activeLineRangeEnd >= bottomLine)) {
/*     */ 
/*     */ 
/*     */       
/* 441 */       g.setColor(this.activeLineRangeColor);
/* 442 */       int firstLine = Math.max(this.activeLineRangeStart, topLine);
/* 443 */       int y1 = firstLine * cellHeight + this.textAreaInsets.top;
/* 444 */       int lastLine = Math.min(this.activeLineRangeEnd, bottomLine);
/* 445 */       int y2 = (lastLine + 1) * cellHeight + this.textAreaInsets.top - 1;
/*     */       
/* 447 */       int j = y1;
/* 448 */       while (j <= y2) {
/* 449 */         int yEnd = Math.min(y2, j + getWidth());
/* 450 */         int xEnd = yEnd - j;
/* 451 */         g.drawLine(0, j, xEnd, yEnd);
/* 452 */         j += 2;
/*     */       } 
/*     */       
/* 455 */       int i = 2;
/* 456 */       while (i < getWidth()) {
/* 457 */         int yEnd = y1 + getWidth() - i;
/* 458 */         g.drawLine(i, y1, getWidth(), yEnd);
/* 459 */         i += 2;
/*     */       } 
/*     */       
/* 462 */       if (firstLine == this.activeLineRangeStart) {
/* 463 */         g.drawLine(0, y1, getWidth(), y1);
/*     */       }
/* 465 */       if (lastLine == this.activeLineRangeEnd) {
/* 466 */         g.drawLine(0, y2, getWidth(), y2);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 471 */     if (this.trackingIcons != null) {
/* 472 */       int lastLine = bottomLine;
/* 473 */       for (int i = this.trackingIcons.size() - 1; i >= 0; i--) {
/* 474 */         GutterIconInfo ti = getTrackingIcon(i);
/* 475 */         int offs = ti.getMarkedOffset();
/* 476 */         if (offs >= 0 && offs <= doc.getLength()) {
/* 477 */           int line = root.getElementIndex(offs);
/* 478 */           if (line <= lastLine && line >= topLine) {
/* 479 */             Icon icon = ti.getIcon();
/* 480 */             if (icon != null) {
/* 481 */               int y2 = y + (line - topLine) * cellHeight;
/* 482 */               y2 += (cellHeight - icon.getIconHeight()) / 2;
/* 483 */               ti.getIcon().paintIcon(this, g, 0, y2);
/* 484 */               lastLine = line - 1;
/*     */             }
/*     */           
/* 487 */           } else if (line < topLine) {
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
/*     */   protected void paintBackgroundImpl(Graphics g, Rectangle visibleRect) {
/* 504 */     Color bg = getBackground();
/* 505 */     if (this.inheritsGutterBackground && getGutter() != null) {
/* 506 */       bg = getGutter().getBackground();
/*     */     }
/* 508 */     g.setColor(bg);
/* 509 */     g.fillRect(0, visibleRect.y, this.width, visibleRect.height);
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
/*     */   private void paintComponentWrapped(Graphics g) {
/* 549 */     RTextAreaUI ui = (RTextAreaUI)this.textArea.getUI();
/* 550 */     View v = ui.getRootView(this.textArea).getView(0);
/*     */     
/* 552 */     Document doc = this.textArea.getDocument();
/* 553 */     Element root = doc.getDefaultRootElement();
/* 554 */     int lineCount = root.getElementCount();
/* 555 */     int topPosition = this.textArea.viewToModel(new Point(this.visibleRect.x, this.visibleRect.y));
/*     */     
/* 557 */     int topLine = root.getElementIndex(topPosition);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 563 */     Rectangle visibleEditorRect = ui.getVisibleEditorRect();
/* 564 */     Rectangle r = getChildViewBounds(v, topLine, visibleEditorRect);
/*     */     
/* 566 */     int y = r.y;
/*     */     
/* 568 */     int visibleBottom = this.visibleRect.y + this.visibleRect.height;
/*     */ 
/*     */     
/* 571 */     int currentIcon = -1;
/* 572 */     if (this.trackingIcons != null) {
/* 573 */       for (int i = 0; i < this.trackingIcons.size(); i++) {
/* 574 */         GutterIconImpl icon = getTrackingIcon(i);
/* 575 */         int offs = icon.getMarkedOffset();
/* 576 */         if (offs >= 0 && offs <= doc.getLength()) {
/* 577 */           int line = root.getElementIndex(offs);
/* 578 */           if (line >= topLine) {
/* 579 */             currentIcon = i;
/*     */ 
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 588 */     g.setColor(getForeground());
/* 589 */     int cellHeight = this.textArea.getLineHeight();
/* 590 */     while (y < visibleBottom) {
/*     */       
/* 592 */       r = getChildViewBounds(v, topLine, visibleEditorRect);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 605 */       if (currentIcon > -1) {
/*     */         
/* 607 */         GutterIconImpl toPaint = null;
/* 608 */         while (currentIcon < this.trackingIcons.size()) {
/* 609 */           GutterIconImpl ti = getTrackingIcon(currentIcon);
/* 610 */           int offs = ti.getMarkedOffset();
/* 611 */           if (offs >= 0 && offs <= doc.getLength()) {
/* 612 */             int line = root.getElementIndex(offs);
/* 613 */             if (line == topLine) {
/* 614 */               toPaint = ti;
/*     */             }
/* 616 */             else if (line > topLine) {
/*     */               break;
/*     */             } 
/*     */           } 
/* 620 */           currentIcon++;
/*     */         } 
/* 622 */         if (toPaint != null) {
/* 623 */           Icon icon = toPaint.getIcon();
/* 624 */           if (icon != null) {
/* 625 */             int y2 = y + (cellHeight - icon.getIconHeight()) / 2;
/* 626 */             icon.paintIcon(this, g, 0, y2);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 633 */       y += r.height;
/*     */ 
/*     */ 
/*     */       
/* 637 */       topLine++;
/* 638 */       if (topLine >= lineCount) {
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeTrackingIcon(GutterIconInfo tag) {
/* 655 */     if (this.trackingIcons != null && this.trackingIcons.remove(tag)) {
/* 656 */       repaint();
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
/*     */   public void removeAllTrackingIcons() {
/* 668 */     if (this.trackingIcons != null && this.trackingIcons.size() > 0) {
/* 669 */       this.trackingIcons.clear();
/* 670 */       repaint();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeBookmarkTrackingIcons() {
/* 679 */     if (this.trackingIcons != null) {
/* 680 */       this.trackingIcons.removeIf(ti -> (ti.getIcon() == this.bookmarkIcon));
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
/*     */   public void setActiveLineRange(int startLine, int endLine) {
/* 693 */     if (startLine != this.activeLineRangeStart || endLine != this.activeLineRangeEnd) {
/*     */       
/* 695 */       this.activeLineRangeStart = startLine;
/* 696 */       this.activeLineRangeEnd = endLine;
/* 697 */       repaint();
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
/*     */   public void setActiveLineRangeColor(Color color) {
/* 711 */     if (color == null) {
/* 712 */       color = Gutter.DEFAULT_ACTIVE_LINE_RANGE_COLOR;
/*     */     }
/* 714 */     if (!color.equals(this.activeLineRangeColor)) {
/* 715 */       this.activeLineRangeColor = color;
/* 716 */       repaint();
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
/*     */   public void setBookmarkIcon(Icon icon) {
/* 731 */     removeBookmarkTrackingIcons();
/* 732 */     this.bookmarkIcon = icon;
/* 733 */     repaint();
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
/*     */   public void setBookmarkingEnabled(boolean enabled) {
/* 748 */     if (enabled != this.bookmarkingEnabled) {
/* 749 */       this.bookmarkingEnabled = enabled;
/* 750 */       if (!enabled) {
/* 751 */         removeBookmarkTrackingIcons();
/*     */       }
/* 753 */       repaint();
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
/*     */   public void setInheritsGutterBackground(boolean inherits) {
/* 767 */     if (inherits != this.inheritsGutterBackground) {
/* 768 */       this.inheritsGutterBackground = inherits;
/* 769 */       repaint();
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
/* 782 */     removeAllTrackingIcons();
/* 783 */     super.setTextArea(textArea);
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
/*     */   public boolean toggleBookmark(int line) throws BadLocationException {
/* 798 */     if (!isBookmarkingEnabled() || getBookmarkIcon() == null) {
/* 799 */       return false;
/*     */     }
/*     */     
/* 802 */     GutterIconInfo[] icons = getTrackingIcons(line);
/* 803 */     if (icons.length == 0) {
/* 804 */       int offs = this.textArea.getLineStartOffset(line);
/* 805 */       addOffsetTrackingIcon(offs, this.bookmarkIcon);
/* 806 */       return true;
/*     */     } 
/*     */     
/* 809 */     boolean found = false;
/* 810 */     for (GutterIconInfo icon : icons) {
/* 811 */       if (icon.getIcon() == this.bookmarkIcon) {
/* 812 */         removeTrackingIcon(icon);
/* 813 */         found = true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 822 */     if (!found) {
/* 823 */       int offs = this.textArea.getLineStartOffset(line);
/* 824 */       addOffsetTrackingIcon(offs, this.bookmarkIcon);
/*     */     } 
/*     */     
/* 827 */     return !found;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void updateBackground() {
/* 838 */     Color bg = UIManager.getColor("Panel.background");
/* 839 */     if (bg == null) {
/* 840 */       bg = (new JPanel()).getBackground();
/*     */     }
/* 842 */     setBackground(bg);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateUI() {
/* 848 */     super.updateUI();
/* 849 */     updateBackground();
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
/*     */   private int viewToModelLine(Point p) throws BadLocationException {
/* 861 */     int offs = this.textArea.viewToModel(p);
/* 862 */     return (offs > -1) ? this.textArea.getLineOfOffset(offs) : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class GutterIconImpl
/*     */     implements GutterIconInfo, Comparable<GutterIconInfo>
/*     */   {
/*     */     private Icon icon;
/*     */     
/*     */     private Position pos;
/*     */     
/*     */     private String toolTip;
/*     */ 
/*     */     
/*     */     GutterIconImpl(Icon icon, Position pos, String toolTip) {
/* 877 */       this.icon = icon;
/* 878 */       this.pos = pos;
/* 879 */       this.toolTip = toolTip;
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(GutterIconInfo other) {
/* 884 */       if (other != null) {
/* 885 */         return this.pos.getOffset() - other.getMarkedOffset();
/*     */       }
/* 887 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 892 */       return (o == this);
/*     */     }
/*     */ 
/*     */     
/*     */     public Icon getIcon() {
/* 897 */       return this.icon;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getMarkedOffset() {
/* 902 */       return this.pos.getOffset();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getToolTip() {
/* 907 */       return this.toolTip;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 912 */       return this.icon.hashCode();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\IconRowHeader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */