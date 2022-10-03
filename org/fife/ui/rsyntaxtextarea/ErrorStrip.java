/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.ResourceBundle;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.ToolTipManager;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.event.CaretEvent;
/*     */ import javax.swing.event.CaretListener;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.Parser;
/*     */ import org.fife.ui.rsyntaxtextarea.parser.ParserNotice;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ErrorStrip
/*     */   extends JPanel
/*     */ {
/*     */   private RSyntaxTextArea textArea;
/*     */   private transient Listener listener;
/*     */   private boolean showMarkedOccurrences;
/*     */   private boolean showMarkAll;
/*     */   private Map<Color, Color> brighterColors;
/*     */   private ParserNotice.Level levelThreshold;
/*     */   private boolean followCaret;
/*     */   private Color caretMarkerColor;
/*     */   private int caretLineY;
/*     */   private int lastLineY;
/*     */   private transient ErrorStripMarkerToolTipProvider markerToolTipProvider;
/*     */   private static final int PREFERRED_WIDTH = 14;
/* 145 */   private static final ResourceBundle MSG = ResourceBundle.getBundle("org.fife.ui.rsyntaxtextarea.ErrorStrip");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ErrorStrip(RSyntaxTextArea textArea) {
/* 154 */     this.textArea = textArea;
/* 155 */     this.listener = new Listener();
/* 156 */     ToolTipManager.sharedInstance().registerComponent(this);
/* 157 */     setLayout((LayoutManager)null);
/* 158 */     addMouseListener(this.listener);
/* 159 */     setShowMarkedOccurrences(true);
/* 160 */     setShowMarkAll(true);
/* 161 */     setLevelThreshold(ParserNotice.Level.WARNING);
/* 162 */     setFollowCaret(true);
/* 163 */     setCaretMarkerColor(getDefaultCaretMarkerColor());
/* 164 */     setMarkerToolTipProvider((ErrorStripMarkerToolTipProvider)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addNotify() {
/* 174 */     super.addNotify();
/* 175 */     this.textArea.addCaretListener(this.listener);
/* 176 */     this.textArea.addPropertyChangeListener("RSTA.parserNotices", this.listener);
/*     */     
/* 178 */     this.textArea.addPropertyChangeListener("RSTA.markOccurrences", this.listener);
/*     */     
/* 180 */     this.textArea.addPropertyChangeListener("RSTA.markedOccurrencesChanged", this.listener);
/*     */     
/* 182 */     this.textArea.addPropertyChangeListener("RTA.markAllOccurrencesChanged", this.listener);
/*     */     
/* 184 */     refreshMarkers();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doLayout() {
/* 193 */     for (int i = 0; i < getComponentCount(); i++) {
/* 194 */       Marker m = (Marker)getComponent(i);
/* 195 */       m.updateLocation();
/*     */     } 
/* 197 */     this.listener.caretUpdate(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Color getBrighterColor(Color c) {
/* 208 */     if (this.brighterColors == null) {
/* 209 */       this.brighterColors = new HashMap<>(5);
/*     */     }
/* 211 */     Color brighter = this.brighterColors.get(c);
/* 212 */     if (brighter == null) {
/*     */ 
/*     */       
/* 215 */       int r = possiblyBrighter(c.getRed());
/* 216 */       int g = possiblyBrighter(c.getGreen());
/* 217 */       int b = possiblyBrighter(c.getBlue());
/* 218 */       brighter = new Color(r, g, b);
/* 219 */       this.brighterColors.put(c, brighter);
/*     */     } 
/* 221 */     return brighter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Color getCaretMarkerColor() {
/* 232 */     return this.caretMarkerColor;
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
/*     */   private ColorUIResource getDefaultCaretMarkerColor() {
/* 245 */     if (RSyntaxUtilities.isLightForeground(getForeground())) {
/* 246 */       return new ColorUIResource(this.textArea.getCaretColor());
/*     */     }
/*     */     
/* 249 */     return new ColorUIResource(Color.BLACK);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getFollowCaret() {
/* 259 */     return this.followCaret;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Dimension getPreferredSize() {
/* 265 */     int height = (this.textArea.getPreferredScrollableViewportSize()).height;
/* 266 */     return new Dimension(14, height);
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
/*     */   public ParserNotice.Level getLevelThreshold() {
/* 279 */     return this.levelThreshold;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getShowMarkAll() {
/* 290 */     return this.showMarkAll;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getShowMarkedOccurrences() {
/* 301 */     return this.showMarkedOccurrences;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getToolTipText(MouseEvent e) {
/* 307 */     String text = null;
/* 308 */     int line = yToLine(e.getY());
/* 309 */     if (line > -1) {
/* 310 */       text = MSG.getString("Line");
/* 311 */       text = MessageFormat.format(text, new Object[] { Integer.valueOf(line + 1) });
/*     */     } 
/* 313 */     return text;
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
/*     */   private int lineToY(int line) {
/* 326 */     int h = (this.textArea.getVisibleRect()).height;
/* 327 */     float lineCount = this.textArea.getLineCount();
/* 328 */     return (int)((line - 1) / (lineCount - 1.0F) * (h - 2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintComponent(Graphics g) {
/* 339 */     super.paintComponent(g);
/* 340 */     if (this.caretLineY > -1) {
/* 341 */       g.setColor(getCaretMarkerColor());
/* 342 */       g.fillRect(0, this.caretLineY, getWidth(), 2);
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
/*     */   private static int possiblyBrighter(int i) {
/* 354 */     if (i < 255) {
/* 355 */       i += (int)((255 - i) * 0.8F);
/*     */     }
/* 357 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void refreshMarkers() {
/* 366 */     removeAll();
/* 367 */     Map<Integer, Marker> markerMap = new HashMap<>();
/*     */     
/* 369 */     List<ParserNotice> notices = this.textArea.getParserNotices();
/* 370 */     for (ParserNotice notice : notices) {
/* 371 */       if (notice.getLevel().isEqualToOrWorseThan(this.levelThreshold) || notice instanceof org.fife.ui.rsyntaxtextarea.parser.TaskTagParser.TaskNotice) {
/*     */         
/* 373 */         Integer key = Integer.valueOf(notice.getLine());
/* 374 */         Marker m = markerMap.get(key);
/* 375 */         if (m == null) {
/* 376 */           m = new Marker(notice);
/* 377 */           m.addMouseListener(this.listener);
/* 378 */           markerMap.put(key, m);
/* 379 */           add(m);
/*     */           continue;
/*     */         } 
/* 382 */         m.addNotice(notice);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 387 */     if (getShowMarkedOccurrences() && this.textArea.getMarkOccurrences()) {
/* 388 */       List<DocumentRange> occurrences = this.textArea.getMarkedOccurrences();
/* 389 */       addMarkersForRanges(occurrences, markerMap, this.textArea.getMarkOccurrencesColor());
/*     */     } 
/*     */     
/* 392 */     if (getShowMarkAll()) {
/* 393 */       Color markAllColor = this.textArea.getMarkAllHighlightColor();
/* 394 */       List<DocumentRange> ranges = this.textArea.getMarkAllHighlightRanges();
/* 395 */       addMarkersForRanges(ranges, markerMap, markAllColor);
/*     */     } 
/*     */     
/* 398 */     revalidate();
/* 399 */     repaint();
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
/*     */   private void addMarkersForRanges(List<DocumentRange> ranges, Map<Integer, Marker> markerMap, Color color) {
/* 413 */     for (DocumentRange range : ranges) {
/* 414 */       int line = 0;
/*     */       try {
/* 416 */         line = this.textArea.getLineOfOffset(range.getStartOffset());
/* 417 */       } catch (BadLocationException ble) {
/*     */         continue;
/*     */       } 
/* 420 */       ParserNotice notice = new MarkedOccurrenceNotice(range, color);
/* 421 */       Integer key = Integer.valueOf(line);
/* 422 */       Marker m = markerMap.get(key);
/* 423 */       if (m == null) {
/* 424 */         m = new Marker(notice);
/* 425 */         m.addMouseListener(this.listener);
/* 426 */         markerMap.put(key, m);
/* 427 */         add(m);
/*     */         continue;
/*     */       } 
/* 430 */       if (!m.containsMarkedOccurence()) {
/* 431 */         m.addNotice(notice);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeNotify() {
/* 440 */     super.removeNotify();
/* 441 */     this.textArea.removeCaretListener(this.listener);
/* 442 */     this.textArea.removePropertyChangeListener("RSTA.parserNotices", this.listener);
/*     */     
/* 444 */     this.textArea.removePropertyChangeListener("RSTA.markOccurrences", this.listener);
/*     */     
/* 446 */     this.textArea.removePropertyChangeListener("RSTA.markedOccurrencesChanged", this.listener);
/*     */     
/* 448 */     this.textArea.removePropertyChangeListener("RTA.markAllOccurrencesChanged", this.listener);
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
/*     */   public void setCaretMarkerColor(Color color) {
/* 460 */     if (color != null) {
/* 461 */       this.caretMarkerColor = color;
/* 462 */       this.listener.caretUpdate(null);
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
/*     */   public void setFollowCaret(boolean follow) {
/* 474 */     if (this.followCaret != follow) {
/* 475 */       if (this.followCaret) {
/* 476 */         repaint(0, this.caretLineY, getWidth(), 2);
/*     */       }
/* 478 */       this.caretLineY = -1;
/* 479 */       this.lastLineY = -1;
/* 480 */       this.followCaret = follow;
/* 481 */       this.listener.caretUpdate(null);
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
/*     */   public void setLevelThreshold(ParserNotice.Level level) {
/* 497 */     this.levelThreshold = level;
/* 498 */     if (isDisplayable()) {
/* 499 */       refreshMarkers();
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
/*     */   public void setMarkerToolTipProvider(ErrorStripMarkerToolTipProvider provider) {
/* 513 */     this.markerToolTipProvider = (provider != null) ? provider : new DefaultErrorStripMarkerToolTipProvider();
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
/*     */   public void setShowMarkAll(boolean show) {
/* 525 */     if (show != this.showMarkAll) {
/* 526 */       this.showMarkAll = show;
/* 527 */       if (isDisplayable()) {
/* 528 */         refreshMarkers();
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
/*     */   public void setShowMarkedOccurrences(boolean show) {
/* 541 */     if (show != this.showMarkedOccurrences) {
/* 542 */       this.showMarkedOccurrences = show;
/* 543 */       if (isDisplayable()) {
/* 544 */         refreshMarkers();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateUI() {
/* 552 */     super.updateUI();
/*     */     
/* 554 */     if (this.caretMarkerColor instanceof ColorUIResource) {
/* 555 */       setCaretMarkerColor(getDefaultCaretMarkerColor());
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
/*     */   private int yToLine(int y) {
/* 568 */     int line = -1;
/* 569 */     int h = (this.textArea.getVisibleRect()).height;
/* 570 */     if (y < h) {
/* 571 */       float at = y / h;
/* 572 */       line = Math.round((this.textArea.getLineCount() - 1) * at);
/*     */     } 
/* 574 */     return line;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class DefaultErrorStripMarkerToolTipProvider
/*     */     implements ErrorStripMarkerToolTipProvider
/*     */   {
/*     */     private DefaultErrorStripMarkerToolTipProvider() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getToolTipText(List<ParserNotice> notices) {
/*     */       String text;
/* 592 */       if (notices.size() == 1) {
/* 593 */         text = ((ParserNotice)notices.get(0)).getMessage();
/*     */       } else {
/*     */         
/* 596 */         StringBuilder sb = new StringBuilder("<html>");
/* 597 */         sb.append(ErrorStrip.MSG.getString("MultipleMarkers"));
/* 598 */         sb.append("<br>");
/* 599 */         for (ParserNotice pn : notices) {
/* 600 */           sb.append("&nbsp;&nbsp;&nbsp;- ");
/* 601 */           sb.append(pn.getMessage());
/* 602 */           sb.append("<br>");
/*     */         } 
/* 604 */         text = sb.toString();
/*     */       } 
/*     */       
/* 607 */       return text;
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
/*     */   public static interface ErrorStripMarkerToolTipProvider
/*     */   {
/*     */     String getToolTipText(List<ParserNotice> param1List);
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
/*     */   private class Listener
/*     */     extends MouseAdapter
/*     */     implements PropertyChangeListener, CaretListener
/*     */   {
/* 641 */     private Rectangle visibleRect = new Rectangle();
/*     */ 
/*     */     
/*     */     public void caretUpdate(CaretEvent e) {
/* 645 */       if (ErrorStrip.this.getFollowCaret()) {
/* 646 */         int line = ErrorStrip.this.textArea.getCaretLineNumber();
/* 647 */         float percent = line / (ErrorStrip.this.textArea.getLineCount() - 1);
/* 648 */         ErrorStrip.this.textArea.computeVisibleRect(this.visibleRect);
/* 649 */         ErrorStrip.this.caretLineY = (int)(this.visibleRect.height * percent);
/* 650 */         if (ErrorStrip.this.caretLineY != ErrorStrip.this.lastLineY) {
/* 651 */           ErrorStrip.this.repaint(0, ErrorStrip.this.lastLineY, ErrorStrip.this.getWidth(), 2);
/* 652 */           ErrorStrip.this.repaint(0, ErrorStrip.this.caretLineY, ErrorStrip.this.getWidth(), 2);
/* 653 */           ErrorStrip.this.lastLineY = ErrorStrip.this.caretLineY;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void mouseClicked(MouseEvent e) {
/* 661 */       Component source = (Component)e.getSource();
/* 662 */       if (source instanceof ErrorStrip.Marker) {
/* 663 */         ((ErrorStrip.Marker)source).mouseClicked(e);
/*     */         
/*     */         return;
/*     */       } 
/* 667 */       int line = ErrorStrip.this.yToLine(e.getY());
/* 668 */       if (line > -1) {
/*     */         try {
/* 670 */           int offs = ErrorStrip.this.textArea.getLineStartOffset(line);
/* 671 */           ErrorStrip.this.textArea.setCaretPosition(offs);
/* 672 */         } catch (BadLocationException ble) {
/* 673 */           UIManager.getLookAndFeel().provideErrorFeedback((Component)ErrorStrip.this.textArea);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void propertyChange(PropertyChangeEvent e) {
/* 682 */       String propName = e.getPropertyName();
/*     */ 
/*     */       
/* 685 */       if ("RSTA.markOccurrences".equals(propName)) {
/* 686 */         if (ErrorStrip.this.getShowMarkedOccurrences()) {
/* 687 */           ErrorStrip.this.refreshMarkers();
/*     */ 
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 693 */       else if ("RSTA.parserNotices".equals(propName)) {
/* 694 */         ErrorStrip.this.refreshMarkers();
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 699 */       else if ("RSTA.markedOccurrencesChanged"
/* 700 */         .equals(propName)) {
/* 701 */         if (ErrorStrip.this.getShowMarkedOccurrences()) {
/* 702 */           ErrorStrip.this.refreshMarkers();
/*     */ 
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 708 */       else if ("RTA.markAllOccurrencesChanged"
/* 709 */         .equals(propName) && 
/* 710 */         ErrorStrip.this.getShowMarkAll()) {
/* 711 */         ErrorStrip.this.refreshMarkers();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private Listener() {}
/*     */   }
/*     */ 
/*     */   
/*     */   private class MarkedOccurrenceNotice
/*     */     implements ParserNotice
/*     */   {
/*     */     private DocumentRange range;
/*     */     
/*     */     private Color color;
/*     */ 
/*     */     
/*     */     MarkedOccurrenceNotice(DocumentRange range, Color color) {
/* 729 */       this.range = range;
/* 730 */       this.color = color;
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(ParserNotice other) {
/* 735 */       return 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsPosition(int pos) {
/* 740 */       return (pos >= this.range.getStartOffset() && pos < this.range.getEndOffset());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 746 */       if (!(o instanceof ParserNotice)) {
/* 747 */         return false;
/*     */       }
/* 749 */       return (compareTo((ParserNotice)o) == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public Color getColor() {
/* 754 */       return this.color;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean getKnowsOffsetAndLength() {
/* 759 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getLength() {
/* 764 */       return this.range.getEndOffset() - this.range.getStartOffset();
/*     */     }
/*     */ 
/*     */     
/*     */     public ParserNotice.Level getLevel() {
/* 769 */       return ParserNotice.Level.INFO;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getLine() {
/*     */       try {
/* 775 */         return ErrorStrip.this.textArea.getLineOfOffset(this.range.getStartOffset()) + 1;
/* 776 */       } catch (BadLocationException ble) {
/* 777 */         return 0;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String getMessage() {
/* 783 */       String text = null;
/*     */       try {
/* 785 */         String word = ErrorStrip.this.textArea.getText(this.range.getStartOffset(), 
/* 786 */             getLength());
/* 787 */         text = ErrorStrip.MSG.getString("OccurrenceOf");
/* 788 */         text = MessageFormat.format(text, new Object[] { word });
/* 789 */       } catch (BadLocationException ble) {
/* 790 */         UIManager.getLookAndFeel().provideErrorFeedback((Component)ErrorStrip.this.textArea);
/*     */       } 
/* 792 */       return text;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getOffset() {
/* 797 */       return this.range.getStartOffset();
/*     */     }
/*     */ 
/*     */     
/*     */     public Parser getParser() {
/* 802 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean getShowInEditor() {
/* 807 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getToolTipText() {
/* 812 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 817 */       return 0;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class Marker
/*     */     extends JComponent
/*     */   {
/*     */     private List<ParserNotice> notices;
/*     */ 
/*     */ 
/*     */     
/*     */     Marker(ParserNotice notice) {
/* 831 */       this.notices = new ArrayList<>(1);
/* 832 */       addNotice(notice);
/* 833 */       setCursor(Cursor.getPredefinedCursor(12));
/* 834 */       setSize(getPreferredSize());
/* 835 */       ToolTipManager.sharedInstance().registerComponent(this);
/*     */     }
/*     */     
/*     */     public void addNotice(ParserNotice notice) {
/* 839 */       this.notices.add(notice);
/*     */     }
/*     */     
/*     */     public boolean containsMarkedOccurence() {
/* 843 */       boolean result = false;
/* 844 */       for (ParserNotice notice : this.notices) {
/* 845 */         if (notice instanceof ErrorStrip.MarkedOccurrenceNotice) {
/* 846 */           result = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 850 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public Color getColor() {
/* 855 */       Color c = null;
/* 856 */       int lowestLevel = Integer.MAX_VALUE;
/* 857 */       for (ParserNotice notice : this.notices) {
/* 858 */         if (notice.getLevel().getNumericValue() < lowestLevel) {
/* 859 */           lowestLevel = notice.getLevel().getNumericValue();
/* 860 */           c = notice.getColor();
/*     */         } 
/*     */       } 
/* 863 */       return c;
/*     */     }
/*     */ 
/*     */     
/*     */     public Dimension getPreferredSize() {
/* 868 */       int w = 10;
/* 869 */       return new Dimension(w, 5);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getToolTipText() {
/* 874 */       return ErrorStrip.this.markerToolTipProvider.getToolTipText(
/* 875 */           Collections.unmodifiableList(this.notices));
/*     */     }
/*     */     
/*     */     protected void mouseClicked(MouseEvent e) {
/* 879 */       ParserNotice pn = this.notices.get(0);
/* 880 */       int offs = pn.getOffset();
/* 881 */       int len = pn.getLength();
/* 882 */       if (offs > -1 && len > -1) {
/* 883 */         DocumentRange range = new DocumentRange(offs, offs + len);
/* 884 */         RSyntaxUtilities.selectAndPossiblyCenter((JTextArea)ErrorStrip.this.textArea, range, true);
/*     */       } else {
/*     */         
/* 887 */         int line = pn.getLine();
/*     */         try {
/* 889 */           offs = ErrorStrip.this.textArea.getLineStartOffset(line);
/* 890 */           ErrorStrip.this.textArea.getFoldManager().ensureOffsetNotInClosedFold(offs);
/* 891 */           ErrorStrip.this.textArea.setCaretPosition(offs);
/* 892 */         } catch (BadLocationException ble) {
/* 893 */           UIManager.getLookAndFeel().provideErrorFeedback((Component)ErrorStrip.this.textArea);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void paintComponent(Graphics g) {
/* 904 */       Color borderColor = getColor();
/* 905 */       if (borderColor == null) {
/* 906 */         borderColor = Color.DARK_GRAY;
/*     */       }
/* 908 */       Color fillColor = ErrorStrip.this.getBrighterColor(borderColor);
/*     */       
/* 910 */       int w = getWidth();
/* 911 */       int h = getHeight();
/*     */       
/* 913 */       g.setColor(fillColor);
/* 914 */       g.fillRect(0, 0, w, h);
/*     */       
/* 916 */       g.setColor(borderColor);
/* 917 */       g.drawRect(0, 0, w - 1, h - 1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void removeNotify() {
/* 923 */       super.removeNotify();
/* 924 */       ToolTipManager.sharedInstance().unregisterComponent(this);
/* 925 */       removeMouseListener(ErrorStrip.this.listener);
/*     */     }
/*     */     
/*     */     public void updateLocation() {
/* 929 */       int line = ((ParserNotice)this.notices.get(0)).getLine();
/* 930 */       int y = ErrorStrip.this.lineToY(line);
/* 931 */       setLocation(2, y);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\ErrorStrip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */