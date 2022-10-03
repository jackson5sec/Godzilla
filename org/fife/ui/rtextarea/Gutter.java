/*      */ package org.fife.ui.rtextarea;
/*      */ 
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.ComponentOrientation;
/*      */ import java.awt.Font;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.event.ComponentAdapter;
/*      */ import java.awt.event.ComponentEvent;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.border.EmptyBorder;
/*      */ import javax.swing.event.DocumentEvent;
/*      */ import javax.swing.event.DocumentListener;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import org.fife.ui.rsyntaxtextarea.ActiveLineRangeEvent;
/*      */ import org.fife.ui.rsyntaxtextarea.ActiveLineRangeListener;
/*      */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Gutter
/*      */   extends JPanel
/*      */ {
/*   74 */   public static final Color DEFAULT_ACTIVE_LINE_RANGE_COLOR = new Color(51, 153, 255);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private RTextArea textArea;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private LineNumberList lineNumberList;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Color lineNumberColor;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int lineNumberingStartIndex;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Font lineNumberFont;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private IconRowHeader iconArea;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean iconRowHeaderInheritsGutterBackground;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int spacingBetweenLineNumbersAndFoldIndicator;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private FoldIndicator foldIndicator;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private transient TextAreaListener listener;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Gutter(RTextArea textArea) {
/*  137 */     this.listener = new TextAreaListener();
/*  138 */     this.lineNumberColor = Color.gray;
/*  139 */     this.lineNumberFont = RTextArea.getDefaultFont();
/*  140 */     this.lineNumberingStartIndex = 1;
/*  141 */     this.iconRowHeaderInheritsGutterBackground = false;
/*      */     
/*  143 */     setTextArea(textArea);
/*  144 */     setLayout(new BorderLayout());
/*  145 */     if (this.textArea != null) {
/*      */ 
/*      */       
/*  148 */       setLineNumbersEnabled(true);
/*  149 */       if (this.textArea instanceof RSyntaxTextArea) {
/*  150 */         RSyntaxTextArea rsta = (RSyntaxTextArea)this.textArea;
/*  151 */         setFoldIndicatorEnabled(rsta.isCodeFoldingEnabled());
/*      */       } 
/*      */     } 
/*      */     
/*  155 */     setBorder(new GutterBorder(0, 0, 0, 1));
/*      */     
/*  157 */     Color bg = null;
/*  158 */     if (textArea != null) {
/*  159 */       bg = textArea.getBackground();
/*      */     }
/*  161 */     setBackground((bg != null) ? bg : Color.WHITE);
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
/*      */   public GutterIconInfo addLineTrackingIcon(int line, Icon icon) throws BadLocationException {
/*  184 */     return addLineTrackingIcon(line, icon, (String)null);
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
/*      */   public GutterIconInfo addLineTrackingIcon(int line, Icon icon, String tip) throws BadLocationException {
/*  207 */     int offs = this.textArea.getLineStartOffset(line);
/*  208 */     return addOffsetTrackingIcon(offs, icon, tip);
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
/*      */   public GutterIconInfo addOffsetTrackingIcon(int offs, Icon icon) throws BadLocationException {
/*  228 */     return addOffsetTrackingIcon(offs, icon, (String)null);
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
/*      */   public GutterIconInfo addOffsetTrackingIcon(int offs, Icon icon, String tip) throws BadLocationException {
/*  249 */     return this.iconArea.addOffsetTrackingIcon(offs, icon, tip);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void clearActiveLineRange() {
/*  259 */     this.iconArea.clearActiveLineRange();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Color getActiveLineRangeColor() {
/*  270 */     return this.iconArea.getActiveLineRangeColor();
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
/*      */   public Color getArmedFoldBackground() {
/*  283 */     return this.foldIndicator.getFoldIconArmedBackground();
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
/*      */   public Icon getBookmarkIcon() {
/*  296 */     return this.iconArea.getBookmarkIcon();
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
/*      */   public GutterIconInfo[] getBookmarks() {
/*  308 */     return this.iconArea.getBookmarks();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Color getBorderColor() {
/*  319 */     return ((GutterBorder)getBorder()).getColor();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Color getFoldBackground() {
/*  330 */     return this.foldIndicator.getFoldIconBackground();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Color getFoldIndicatorForeground() {
/*  341 */     return this.foldIndicator.getForeground();
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
/*      */   public boolean getIconRowHeaderInheritsGutterBackground() {
/*  353 */     return this.iconRowHeaderInheritsGutterBackground;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Color getLineNumberColor() {
/*  364 */     return this.lineNumberColor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Font getLineNumberFont() {
/*  375 */     return this.lineNumberFont;
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
/*      */   public int getLineNumberingStartIndex() {
/*  387 */     return this.lineNumberingStartIndex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getLineNumbersEnabled() {
/*  397 */     for (int i = 0; i < getComponentCount(); i++) {
/*  398 */       if (getComponent(i) == this.lineNumberList) {
/*  399 */         return true;
/*      */       }
/*      */     } 
/*  402 */     return false;
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
/*      */   public boolean getShowCollapsedRegionToolTips() {
/*  414 */     return this.foldIndicator.getShowCollapsedRegionToolTips();
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
/*      */   public int getSpacingBetweenLineNumbersAndFoldIndicator() {
/*  428 */     return this.spacingBetweenLineNumbersAndFoldIndicator;
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
/*      */   public GutterIconInfo[] getTrackingIcons(Point p) throws BadLocationException {
/*  442 */     int offs = this.textArea.viewToModel(new Point(0, p.y));
/*  443 */     int line = this.textArea.getLineOfOffset(offs);
/*  444 */     return this.iconArea.getTrackingIcons(line);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFoldIndicatorEnabled() {
/*  455 */     for (int i = 0; i < getComponentCount(); i++) {
/*  456 */       if (getComponent(i) == this.foldIndicator) {
/*  457 */         return true;
/*      */       }
/*      */     } 
/*  460 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isBookmarkingEnabled() {
/*  471 */     return this.iconArea.isBookmarkingEnabled();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isIconRowHeaderEnabled() {
/*  481 */     for (int i = 0; i < getComponentCount(); i++) {
/*  482 */       if (getComponent(i) == this.iconArea) {
/*  483 */         return true;
/*      */       }
/*      */     } 
/*  486 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeAllTrackingIcons() {
/*  497 */     this.iconArea.removeAllTrackingIcons();
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
/*      */   public void removeTrackingIcon(GutterIconInfo tag) {
/*  512 */     this.iconArea.removeTrackingIcon(tag);
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
/*      */   public void setActiveLineRangeColor(Color color) {
/*  525 */     this.iconArea.setActiveLineRangeColor(color);
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
/*      */   private void setActiveLineRange(int startLine, int endLine) {
/*  538 */     this.iconArea.setActiveLineRange(startLine, endLine);
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
/*      */   public void setArmedFoldBackground(Color bg) {
/*  552 */     this.foldIndicator.setFoldIconArmedBackground(bg);
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
/*      */   public void setBookmarkIcon(Icon icon) {
/*  565 */     this.iconArea.setBookmarkIcon(icon);
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
/*      */   public void setBookmarkingEnabled(boolean enabled) {
/*  579 */     this.iconArea.setBookmarkingEnabled(enabled);
/*  580 */     if (enabled && !isIconRowHeaderEnabled()) {
/*  581 */       setIconRowHeaderEnabled(true);
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
/*      */   public void setBorderColor(Color color) {
/*  593 */     ((GutterBorder)getBorder()).setColor(color);
/*  594 */     repaint();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setComponentOrientation(ComponentOrientation o) {
/*  604 */     if (getBorder() instanceof GutterBorder)
/*      */     {
/*  606 */       if (o.isLeftToRight()) {
/*  607 */         ((GutterBorder)getBorder()).setEdges(0, 0, 0, 1);
/*      */       } else {
/*      */         
/*  610 */         ((GutterBorder)getBorder()).setEdges(0, 1, 0, 0);
/*      */       } 
/*      */     }
/*  613 */     super.setComponentOrientation(o);
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
/*      */   public void setFoldIcons(Icon collapsedIcon, Icon expandedIcon) {
/*  626 */     if (this.foldIndicator != null) {
/*  627 */       this.foldIndicator.setFoldIcons(collapsedIcon, expandedIcon);
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
/*      */   public void setFoldIndicatorEnabled(boolean enabled) {
/*  639 */     if (this.foldIndicator != null) {
/*  640 */       if (enabled) {
/*  641 */         add(this.foldIndicator, "After");
/*      */       } else {
/*      */         
/*  644 */         remove(this.foldIndicator);
/*      */       } 
/*  646 */       revalidate();
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
/*      */   public void setFoldBackground(Color bg) {
/*  659 */     if (bg == null) {
/*  660 */       bg = FoldIndicator.DEFAULT_FOLD_BACKGROUND;
/*      */     }
/*  662 */     this.foldIndicator.setFoldIconBackground(bg);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFoldIndicatorForeground(Color fg) {
/*  673 */     if (fg == null) {
/*  674 */       fg = FoldIndicator.DEFAULT_FOREGROUND;
/*      */     }
/*  676 */     this.foldIndicator.setForeground(fg);
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
/*      */   void setIconRowHeaderEnabled(boolean enabled) {
/*  688 */     if (this.iconArea != null) {
/*  689 */       if (enabled) {
/*  690 */         add(this.iconArea, "Before");
/*      */       } else {
/*      */         
/*  693 */         remove(this.iconArea);
/*      */       } 
/*  695 */       revalidate();
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
/*      */   public void setIconRowHeaderInheritsGutterBackground(boolean inherits) {
/*  710 */     if (inherits != this.iconRowHeaderInheritsGutterBackground) {
/*  711 */       this.iconRowHeaderInheritsGutterBackground = inherits;
/*  712 */       if (this.iconArea != null) {
/*  713 */         this.iconArea.setInheritsGutterBackground(inherits);
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
/*      */   public void setLineNumberColor(Color color) {
/*  726 */     if (color != null && !color.equals(this.lineNumberColor)) {
/*  727 */       this.lineNumberColor = color;
/*  728 */       if (this.lineNumberList != null) {
/*  729 */         this.lineNumberList.setForeground(color);
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
/*      */   public void setLineNumberFont(Font font) {
/*  742 */     if (font == null) {
/*  743 */       throw new IllegalArgumentException("font cannot be null");
/*      */     }
/*  745 */     if (!font.equals(this.lineNumberFont)) {
/*  746 */       this.lineNumberFont = font;
/*  747 */       if (this.lineNumberList != null) {
/*  748 */         this.lineNumberList.setFont(font);
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
/*      */   public void setLineNumberingStartIndex(int index) {
/*  763 */     if (index != this.lineNumberingStartIndex) {
/*  764 */       this.lineNumberingStartIndex = index;
/*  765 */       this.lineNumberList.setLineNumberingStartIndex(index);
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
/*      */   void setLineNumbersEnabled(boolean enabled) {
/*  777 */     if (this.lineNumberList != null) {
/*  778 */       if (enabled) {
/*  779 */         add(this.lineNumberList);
/*      */       } else {
/*      */         
/*  782 */         remove(this.lineNumberList);
/*      */       } 
/*  784 */       revalidate();
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
/*      */   public void setShowCollapsedRegionToolTips(boolean show) {
/*  797 */     if (this.foldIndicator != null) {
/*  798 */       this.foldIndicator.setShowCollapsedRegionToolTips(show);
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
/*      */   public void setSpacingBetweenLineNumbersAndFoldIndicator(int spacing) {
/*  813 */     if (spacing != this.spacingBetweenLineNumbersAndFoldIndicator) {
/*  814 */       this.spacingBetweenLineNumbersAndFoldIndicator = spacing;
/*  815 */       this.foldIndicator.setAdditionalLeftMargin(spacing);
/*  816 */       revalidate();
/*  817 */       repaint();
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
/*      */   void setTextArea(RTextArea textArea) {
/*  830 */     if (this.textArea != null) {
/*  831 */       this.listener.uninstall();
/*      */     }
/*      */     
/*  834 */     if (textArea != null) {
/*      */ 
/*      */       
/*  837 */       RTextAreaEditorKit kit = (RTextAreaEditorKit)textArea.getUI().getEditorKit(textArea);
/*      */       
/*  839 */       if (this.lineNumberList == null) {
/*  840 */         this.lineNumberList = kit.createLineNumberList(textArea);
/*  841 */         this.lineNumberList.setFont(getLineNumberFont());
/*  842 */         this.lineNumberList.setForeground(getLineNumberColor());
/*  843 */         this.lineNumberList.setLineNumberingStartIndex(
/*  844 */             getLineNumberingStartIndex());
/*      */       } else {
/*      */         
/*  847 */         this.lineNumberList.setTextArea(textArea);
/*      */       } 
/*  849 */       if (this.iconArea == null) {
/*  850 */         this.iconArea = kit.createIconRowHeader(textArea);
/*  851 */         this.iconArea.setInheritsGutterBackground(
/*  852 */             getIconRowHeaderInheritsGutterBackground());
/*      */       } else {
/*      */         
/*  855 */         this.iconArea.setTextArea(textArea);
/*      */       } 
/*  857 */       if (this.foldIndicator == null) {
/*  858 */         this.foldIndicator = new FoldIndicator(textArea);
/*      */       } else {
/*      */         
/*  861 */         this.foldIndicator.setTextArea(textArea);
/*      */       } 
/*      */       
/*  864 */       this.listener.install(textArea);
/*      */     } 
/*      */ 
/*      */     
/*  868 */     this.textArea = textArea;
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
/*      */   public boolean toggleBookmark(int line) throws BadLocationException {
/*  884 */     return this.iconArea.toggleBookmark(line);
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
/*      */   public void setBorder(Border border) {
/*  906 */     if (border instanceof GutterBorder) {
/*  907 */       super.setBorder(border);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static class GutterBorder
/*      */     extends EmptyBorder
/*      */   {
/*      */     private Color color;
/*      */     private Rectangle visibleRect;
/*      */     
/*      */     public GutterBorder(int top, int left, int bottom, int right) {
/*  919 */       super(top, left, bottom, right);
/*  920 */       this.color = new Color(221, 221, 221);
/*  921 */       this.visibleRect = new Rectangle();
/*      */     }
/*      */     
/*      */     public Color getColor() {
/*  925 */       return this.color;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
/*  932 */       this.visibleRect = g.getClipBounds(this.visibleRect);
/*  933 */       if (this.visibleRect == null) {
/*  934 */         this.visibleRect = ((JComponent)c).getVisibleRect();
/*      */       }
/*      */       
/*  937 */       g.setColor(this.color);
/*  938 */       if (this.left == 1) {
/*  939 */         g.drawLine(0, this.visibleRect.y, 0, this.visibleRect.y + this.visibleRect.height);
/*      */       }
/*      */       else {
/*      */         
/*  943 */         g.drawLine(width - 1, this.visibleRect.y, width - 1, this.visibleRect.y + this.visibleRect.height);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setColor(Color color) {
/*  950 */       this.color = color;
/*      */     }
/*      */     
/*      */     public void setEdges(int top, int left, int bottom, int right) {
/*  954 */       this.top = top;
/*  955 */       this.left = left;
/*  956 */       this.bottom = bottom;
/*  957 */       this.right = right;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class TextAreaListener
/*      */     extends ComponentAdapter
/*      */     implements DocumentListener, PropertyChangeListener, ActiveLineRangeListener
/*      */   {
/*      */     private boolean installed;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private TextAreaListener() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void activeLineRangeChanged(ActiveLineRangeEvent e) {
/*  987 */       if (e.getMin() == -1) {
/*  988 */         Gutter.this.clearActiveLineRange();
/*      */       } else {
/*      */         
/*  991 */         Gutter.this.setActiveLineRange(e.getMin(), e.getMax());
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void changedUpdate(DocumentEvent e) {}
/*      */ 
/*      */     
/*      */     public void componentResized(ComponentEvent e) {
/* 1001 */       Gutter.this.revalidate();
/*      */     }
/*      */     
/*      */     protected void handleDocumentEvent(DocumentEvent e) {
/* 1005 */       for (int i = 0; i < Gutter.this.getComponentCount(); i++) {
/*      */         
/* 1007 */         AbstractGutterComponent agc = (AbstractGutterComponent)Gutter.this.getComponent(i);
/* 1008 */         agc.handleDocumentEvent(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void insertUpdate(DocumentEvent e) {
/* 1014 */       handleDocumentEvent(e);
/*      */     }
/*      */     
/*      */     public void install(RTextArea textArea) {
/* 1018 */       if (this.installed) {
/* 1019 */         uninstall();
/*      */       }
/* 1021 */       textArea.addComponentListener(this);
/* 1022 */       textArea.getDocument().addDocumentListener(this);
/* 1023 */       textArea.addPropertyChangeListener(this);
/* 1024 */       if (textArea instanceof RSyntaxTextArea) {
/* 1025 */         RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
/* 1026 */         rsta.addActiveLineRangeListener(this);
/* 1027 */         rsta.getFoldManager().addPropertyChangeListener(this);
/*      */       } 
/* 1029 */       this.installed = true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void propertyChange(PropertyChangeEvent e) {
/* 1035 */       String name = e.getPropertyName();
/*      */ 
/*      */ 
/*      */       
/* 1039 */       if ("font".equals(name) || "RSTA.syntaxScheme"
/* 1040 */         .equals(name)) {
/* 1041 */         for (int i = 0; i < Gutter.this.getComponentCount(); i++)
/*      */         {
/* 1043 */           AbstractGutterComponent agc = (AbstractGutterComponent)Gutter.this.getComponent(i);
/* 1044 */           agc.lineHeightsChanged();
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 1049 */       else if ("RSTA.codeFolding".equals(name)) {
/* 1050 */         boolean foldingEnabled = ((Boolean)e.getNewValue()).booleanValue();
/* 1051 */         if (Gutter.this.lineNumberList != null)
/*      */         {
/* 1053 */           Gutter.this.lineNumberList.updateCellWidths();
/*      */         }
/* 1055 */         Gutter.this.setFoldIndicatorEnabled(foldingEnabled);
/*      */ 
/*      */       
/*      */       }
/* 1059 */       else if ("FoldsUpdated".equals(name)) {
/* 1060 */         Gutter.this.repaint();
/*      */       
/*      */       }
/* 1063 */       else if ("document".equals(name)) {
/*      */         
/* 1065 */         RDocument old = (RDocument)e.getOldValue();
/* 1066 */         if (old != null) {
/* 1067 */           old.removeDocumentListener(this);
/*      */         }
/* 1069 */         RDocument newDoc = (RDocument)e.getNewValue();
/* 1070 */         if (newDoc != null) {
/* 1071 */           newDoc.addDocumentListener(this);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void removeUpdate(DocumentEvent e) {
/* 1079 */       handleDocumentEvent(e);
/*      */     }
/*      */     
/*      */     public void uninstall() {
/* 1083 */       if (this.installed) {
/* 1084 */         Gutter.this.textArea.removeComponentListener(this);
/* 1085 */         Gutter.this.textArea.getDocument().removeDocumentListener(this);
/* 1086 */         Gutter.this.textArea.removePropertyChangeListener(this);
/* 1087 */         if (Gutter.this.textArea instanceof RSyntaxTextArea) {
/* 1088 */           RSyntaxTextArea rsta = (RSyntaxTextArea)Gutter.this.textArea;
/* 1089 */           rsta.removeActiveLineRangeListener(this);
/* 1090 */           rsta.getFoldManager().removePropertyChangeListener(this);
/*      */         } 
/* 1092 */         this.installed = false;
/*      */       } 
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\Gutter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */