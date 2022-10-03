/*     */ package com.jediterm.terminal.model;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.awt.Point;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ class ChangeWidthOperation {
/*  12 */   private static final Logger LOG = Logger.getLogger(TerminalTextBuffer.class);
/*     */   
/*     */   private final TerminalTextBuffer myTextBuffer;
/*     */   private final int myNewWidth;
/*     */   private final int myNewHeight;
/*  17 */   private final Map<Point, Point> myTrackingPoints = new HashMap<>();
/*  18 */   private final List<TerminalLine> myAllLines = new ArrayList<>();
/*     */   
/*     */   private TerminalLine myCurrentLine;
/*     */   private int myCurrentLineLength;
/*     */   
/*     */   ChangeWidthOperation(@NotNull TerminalTextBuffer textBuffer, int newWidth, int newHeight) {
/*  24 */     this.myTextBuffer = textBuffer;
/*  25 */     this.myNewWidth = newWidth;
/*  26 */     this.myNewHeight = newHeight;
/*     */   }
/*     */   
/*     */   void addPointToTrack(@NotNull Point original) {
/*  30 */     if (original == null) $$$reportNull$$$0(1);  this.myTrackingPoints.put(new Point(original), null);
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   Point getTrackedPoint(@NotNull Point original) {
/*  35 */     if (original == null) $$$reportNull$$$0(2);  Point result = this.myTrackingPoints.get(new Point(original));
/*  36 */     if (result == null) {
/*  37 */       LOG.warn("Not tracked point: " + original);
/*  38 */       if (original == null) $$$reportNull$$$0(3);  return original;
/*     */     } 
/*  40 */     if (result == null) $$$reportNull$$$0(4);  return result;
/*     */   }
/*     */   
/*     */   void run() {
/*  44 */     LinesBuffer historyBuffer = this.myTextBuffer.getHistoryBufferOrBackup();
/*  45 */     for (int i = 0; i < historyBuffer.getLineCount(); i++) {
/*  46 */       TerminalLine line = historyBuffer.getLine(i);
/*  47 */       addLine(line);
/*     */     } 
/*  49 */     int screenStartInd = this.myAllLines.size() - 1;
/*  50 */     if (this.myCurrentLine == null || this.myCurrentLineLength == this.myNewWidth) {
/*  51 */       screenStartInd++;
/*     */     }
/*  53 */     Preconditions.checkState((screenStartInd >= 0), "screenStartInd < 0: %d", screenStartInd);
/*  54 */     LinesBuffer screenBuffer = this.myTextBuffer.getScreenBufferOrBackup();
/*  55 */     if (screenBuffer.getLineCount() > this.myTextBuffer.getHeight()) {
/*  56 */       LOG.warn("Terminal height < screen buffer line count: " + this.myTextBuffer.getHeight() + " < " + screenBuffer.getLineCount());
/*     */     }
/*  58 */     int oldScreenLineCount = Math.min(screenBuffer.getLineCount(), this.myTextBuffer.getHeight()); int j;
/*  59 */     for (j = 0; j < oldScreenLineCount; j++) {
/*  60 */       List<Point> points = findPointsAtY(j);
/*  61 */       for (Point point : points) {
/*  62 */         int newX = (this.myCurrentLineLength + point.x) % this.myNewWidth;
/*  63 */         int newY = this.myAllLines.size() + (this.myCurrentLineLength + point.x) / this.myNewWidth;
/*  64 */         if (this.myCurrentLine != null) {
/*  65 */           newY--;
/*     */         }
/*  67 */         this.myTrackingPoints.put(point, new Point(newX, newY));
/*     */       } 
/*  69 */       addLine(screenBuffer.getLine(j));
/*     */     } 
/*  71 */     for (j = oldScreenLineCount; j < this.myTextBuffer.getHeight(); j++) {
/*  72 */       List<Point> points = findPointsAtY(j);
/*  73 */       for (Point point : points) {
/*  74 */         int newX = point.x % this.myNewWidth;
/*  75 */         int newY = j - oldScreenLineCount + this.myAllLines.size() + point.x / this.myNewWidth;
/*  76 */         this.myTrackingPoints.put(point, new Point(newX, newY));
/*     */       } 
/*     */     } 
/*     */     
/*  80 */     int emptyBottomLineCount = getEmptyBottomLineCount();
/*  81 */     screenStartInd = Math.max(screenStartInd, this.myAllLines.size() - Math.min(this.myAllLines.size(), this.myNewHeight) - emptyBottomLineCount);
/*  82 */     screenStartInd = Math.min(screenStartInd, this.myAllLines.size() - Math.min(this.myAllLines.size(), this.myNewHeight));
/*  83 */     historyBuffer.clearAll();
/*  84 */     historyBuffer.addLines(this.myAllLines.subList(0, screenStartInd));
/*  85 */     screenBuffer.clearAll();
/*  86 */     screenBuffer.addLines(this.myAllLines.subList(screenStartInd, Math.min(screenStartInd + this.myNewHeight, this.myAllLines.size())));
/*  87 */     for (Map.Entry<Point, Point> entry : this.myTrackingPoints.entrySet()) {
/*  88 */       Point p = entry.getValue();
/*  89 */       if (p != null) {
/*  90 */         p.y -= screenStartInd;
/*     */       } else {
/*  92 */         p = new Point(entry.getKey());
/*  93 */         entry.setValue(p);
/*     */       } 
/*  95 */       p.x = Math.min(this.myNewWidth, Math.max(0, p.x));
/*  96 */       p.y = Math.min(this.myNewHeight, Math.max(0, p.y));
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getEmptyBottomLineCount() {
/* 101 */     int result = 0;
/* 102 */     while (result < this.myAllLines.size() && ((TerminalLine)this.myAllLines.get(this.myAllLines.size() - result - 1)).isNul()) {
/* 103 */       result++;
/*     */     }
/* 105 */     return result;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   private List<Point> findPointsAtY(int y) {
/* 110 */     List<Point> result = Collections.emptyList();
/* 111 */     for (Point key : this.myTrackingPoints.keySet()) {
/* 112 */       if (key.y == y) {
/* 113 */         if (result.isEmpty()) {
/* 114 */           result = new ArrayList<>();
/*     */         }
/* 116 */         result.add(key);
/*     */       } 
/*     */     } 
/* 119 */     if (result == null) $$$reportNull$$$0(5);  return result;
/*     */   }
/*     */   
/*     */   private void addLine(@NotNull TerminalLine line) {
/* 123 */     if (line == null) $$$reportNull$$$0(6);  if (line.isNul()) {
/* 124 */       if (this.myCurrentLine != null) {
/* 125 */         this.myCurrentLine = null;
/* 126 */         this.myCurrentLineLength = 0;
/*     */       } 
/* 128 */       this.myAllLines.add(TerminalLine.createEmpty());
/*     */       return;
/*     */     } 
/* 131 */     line.forEachEntry(entry -> {
/*     */           if (entry.isNul()) {
/*     */             return;
/*     */           }
/*     */           
/*     */           for (int entryProcessedLength = 0; entryProcessedLength < entry.getLength(); entryProcessedLength += len) {
/*     */             if (this.myCurrentLine != null && this.myCurrentLineLength == this.myNewWidth) {
/*     */               this.myCurrentLine.setWrapped(true);
/*     */               
/*     */               this.myCurrentLine = null;
/*     */               this.myCurrentLineLength = 0;
/*     */             } 
/*     */             if (this.myCurrentLine == null) {
/*     */               this.myCurrentLine = new TerminalLine();
/*     */               this.myCurrentLineLength = 0;
/*     */               this.myAllLines.add(this.myCurrentLine);
/*     */             } 
/*     */             int len = Math.min(this.myNewWidth - this.myCurrentLineLength, entry.getLength() - entryProcessedLength);
/*     */             TerminalLine.TextEntry newEntry = subEntry(entry, entryProcessedLength, len);
/*     */             this.myCurrentLine.appendEntry(newEntry);
/*     */             this.myCurrentLineLength += len;
/*     */           } 
/*     */         });
/* 154 */     if (!line.isWrapped()) {
/* 155 */       this.myCurrentLine = null;
/* 156 */       this.myCurrentLineLength = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   private static TerminalLine.TextEntry subEntry(@NotNull TerminalLine.TextEntry entry, int startInd, int count) {
/* 162 */     if (entry == null) $$$reportNull$$$0(7);  if (startInd == 0 && count == entry.getLength()) {
/* 163 */       if (entry == null) $$$reportNull$$$0(8);  return entry;
/*     */     } 
/* 165 */     return new TerminalLine.TextEntry(entry.getStyle(), entry.getText().subBuffer(startInd, count));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\model\ChangeWidthOperation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */