/*     */ package com.jediterm.terminal.model;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.jediterm.terminal.RequestOrigin;
/*     */ import com.jediterm.terminal.StyledTextConsumer;
/*     */ import com.jediterm.terminal.StyledTextConsumerAdapter;
/*     */ import com.jediterm.terminal.TextStyle;
/*     */ import com.jediterm.terminal.model.hyperlinks.TextProcessing;
/*     */ import com.jediterm.terminal.util.Pair;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Point;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TerminalTextBuffer
/*     */ {
/*  33 */   private static final Logger LOG = Logger.getLogger(TerminalTextBuffer.class);
/*     */   
/*     */   @NotNull
/*     */   private final StyleState myStyleState;
/*     */   
/*     */   private LinesBuffer myHistoryBuffer;
/*     */   
/*     */   private LinesBuffer myScreenBuffer;
/*     */   
/*     */   private int myWidth;
/*     */   
/*     */   private int myHeight;
/*     */   
/*     */   private final int myHistoryLinesCount;
/*  47 */   private final Lock myLock = new ReentrantLock();
/*     */   
/*     */   private LinesBuffer myHistoryBufferBackup;
/*     */   
/*     */   private LinesBuffer myScreenBufferBackup;
/*     */   
/*     */   private boolean myAlternateBuffer = false;
/*     */   
/*     */   private boolean myUsingAlternateBuffer = false;
/*  56 */   private final List<TerminalModelListener> myListeners = new CopyOnWriteArrayList<>();
/*     */   
/*     */   @Nullable
/*     */   private final TextProcessing myTextProcessing;
/*     */   
/*     */   public TerminalTextBuffer(int width, int height, @NotNull StyleState styleState) {
/*  62 */     this(width, height, styleState, null);
/*     */   }
/*     */   
/*     */   public TerminalTextBuffer(int width, int height, @NotNull StyleState styleState, @Nullable TextProcessing textProcessing) {
/*  66 */     this(width, height, styleState, 5000, textProcessing);
/*     */   }
/*     */   
/*     */   public TerminalTextBuffer(int width, int height, @NotNull StyleState styleState, int historyLinesCount, @Nullable TextProcessing textProcessing) {
/*  70 */     this.myStyleState = styleState;
/*  71 */     this.myWidth = width;
/*  72 */     this.myHeight = height;
/*  73 */     this.myHistoryLinesCount = historyLinesCount;
/*  74 */     this.myTextProcessing = textProcessing;
/*     */     
/*  76 */     this.myScreenBuffer = createScreenBuffer();
/*  77 */     this.myHistoryBuffer = createHistoryBuffer();
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   private LinesBuffer createScreenBuffer() {
/*  82 */     return new LinesBuffer(-1, this.myTextProcessing);
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   private LinesBuffer createHistoryBuffer() {
/*  87 */     return new LinesBuffer(this.myHistoryLinesCount, this.myTextProcessing);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Dimension resize(@NotNull Dimension pendingResize, @NotNull RequestOrigin origin, int cursorX, int cursorY, @NotNull JediTerminal.ResizeHandler resizeHandler, @Nullable TerminalSelection mySelection) {
/*  96 */     if (pendingResize == null) $$$reportNull$$$0(3);  if (origin == null) $$$reportNull$$$0(4);  if (resizeHandler == null) $$$reportNull$$$0(5);  lock();
/*     */     try {
/*  98 */       return doResize(pendingResize, origin, cursorX, cursorY, resizeHandler, mySelection);
/*     */     } finally {
/* 100 */       unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Dimension doResize(@NotNull Dimension pendingResize, @NotNull RequestOrigin origin, int cursorX, int cursorY, @NotNull JediTerminal.ResizeHandler resizeHandler, @Nullable TerminalSelection mySelection) {
/* 110 */     if (pendingResize == null) $$$reportNull$$$0(6);  if (origin == null) $$$reportNull$$$0(7);  if (resizeHandler == null) $$$reportNull$$$0(8);  int newWidth = pendingResize.width;
/* 111 */     int newHeight = pendingResize.height;
/* 112 */     int newCursorX = cursorX;
/* 113 */     int newCursorY = cursorY;
/*     */     
/* 115 */     if (this.myWidth != newWidth) {
/* 116 */       ChangeWidthOperation changeWidthOperation = new ChangeWidthOperation(this, newWidth, newHeight);
/* 117 */       Point cursor = new Point(cursorX, cursorY - 1);
/* 118 */       changeWidthOperation.addPointToTrack(cursor);
/* 119 */       if (mySelection != null) {
/* 120 */         changeWidthOperation.addPointToTrack(mySelection.getStart());
/* 121 */         changeWidthOperation.addPointToTrack(mySelection.getEnd());
/*     */       } 
/* 123 */       changeWidthOperation.run();
/* 124 */       this.myWidth = newWidth;
/* 125 */       this.myHeight = newHeight;
/* 126 */       Point newCursor = changeWidthOperation.getTrackedPoint(cursor);
/* 127 */       newCursorX = newCursor.x;
/* 128 */       newCursorY = newCursor.y + 1;
/* 129 */       if (mySelection != null) {
/* 130 */         mySelection.getStart().setLocation(changeWidthOperation.getTrackedPoint(mySelection.getStart()));
/* 131 */         mySelection.getEnd().setLocation(changeWidthOperation.getTrackedPoint(mySelection.getEnd()));
/*     */       } 
/*     */     } 
/*     */     
/* 135 */     int oldHeight = this.myHeight;
/* 136 */     if (newHeight < oldHeight) {
/* 137 */       int count = oldHeight - newHeight;
/* 138 */       if (!this.myAlternateBuffer) {
/*     */ 
/*     */         
/* 141 */         int emptyLinesDeleted = this.myScreenBuffer.removeBottomEmptyLines(oldHeight - 1, count);
/* 142 */         this.myScreenBuffer.moveTopLinesTo(count - emptyLinesDeleted, this.myHistoryBuffer);
/* 143 */         newCursorY = cursorY - count - emptyLinesDeleted;
/*     */       } else {
/* 145 */         newCursorY = cursorY;
/*     */       } 
/* 147 */       if (mySelection != null) {
/* 148 */         mySelection.shiftY(-count);
/*     */       }
/* 150 */     } else if (newHeight > oldHeight) {
/* 151 */       if (!this.myAlternateBuffer) {
/*     */         
/* 153 */         int historyLinesCount = Math.min(newHeight - oldHeight, this.myHistoryBuffer.getLineCount());
/* 154 */         this.myHistoryBuffer.moveBottomLinesTo(historyLinesCount, this.myScreenBuffer);
/* 155 */         newCursorY = cursorY + historyLinesCount;
/*     */       } else {
/* 157 */         newCursorY = cursorY;
/*     */       } 
/* 159 */       if (mySelection != null) {
/* 160 */         mySelection.shiftY(newHeight - cursorY);
/*     */       }
/*     */     } 
/*     */     
/* 164 */     this.myWidth = newWidth;
/* 165 */     this.myHeight = newHeight;
/*     */ 
/*     */     
/* 168 */     resizeHandler.sizeUpdated(this.myWidth, this.myHeight, newCursorX, newCursorY);
/*     */ 
/*     */     
/* 171 */     fireModelChangeEvent();
/*     */     
/* 173 */     return pendingResize;
/*     */   }
/*     */   
/*     */   public void addModelListener(TerminalModelListener listener) {
/* 177 */     this.myListeners.add(listener);
/*     */   }
/*     */   
/*     */   public void removeModelListener(TerminalModelListener listener) {
/* 181 */     this.myListeners.remove(listener);
/*     */   }
/*     */   
/*     */   private void fireModelChangeEvent() {
/* 185 */     for (TerminalModelListener modelListener : this.myListeners) {
/* 186 */       modelListener.modelChanged();
/*     */     }
/*     */   }
/*     */   
/*     */   private TextStyle createEmptyStyleWithCurrentColor() {
/* 191 */     return this.myStyleState.getCurrent().createEmptyWithColors();
/*     */   }
/*     */   
/*     */   private TerminalLine.TextEntry createFillerEntry() {
/* 195 */     return new TerminalLine.TextEntry(createEmptyStyleWithCurrentColor(), new CharBuffer(false, this.myWidth));
/*     */   }
/*     */   
/*     */   public void deleteCharacters(int x, int y, int count) {
/* 199 */     if (y > this.myHeight - 1 || y < 0) {
/* 200 */       LOG.error("attempt to delete in line " + y + "\nargs were x:" + x + " count:" + count);
/*     */     }
/* 202 */     else if (count < 0) {
/* 203 */       LOG.error("Attempt to delete negative chars number: count:" + count);
/* 204 */     } else if (count > 0) {
/* 205 */       this.myScreenBuffer.deleteCharacters(x, y, count, createEmptyStyleWithCurrentColor());
/*     */       
/* 207 */       fireModelChangeEvent();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void insertBlankCharacters(int x, int y, int count) {
/* 212 */     if (y > this.myHeight - 1 || y < 0) {
/* 213 */       LOG.error("attempt to insert blank chars in line " + y + "\nargs were x:" + x + " count:" + count);
/*     */     }
/* 215 */     else if (count < 0) {
/* 216 */       LOG.error("Attempt to insert negative blank chars number: count:" + count);
/* 217 */     } else if (count > 0) {
/* 218 */       this.myScreenBuffer.insertBlankCharacters(x, y, count, this.myWidth, createEmptyStyleWithCurrentColor());
/*     */       
/* 220 */       fireModelChangeEvent();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void writeString(int x, int y, @NotNull CharBuffer str) {
/* 225 */     if (str == null) $$$reportNull$$$0(9);  writeString(x, y, str, this.myStyleState.getCurrent());
/*     */   }
/*     */   
/*     */   public void addLine(@NotNull TerminalLine line) {
/* 229 */     if (line == null) $$$reportNull$$$0(10);  this.myScreenBuffer.addLines(Lists.newArrayList((Object[])new TerminalLine[] { line }));
/*     */     
/* 231 */     fireModelChangeEvent();
/*     */   }
/*     */   
/*     */   private void writeString(int x, int y, @NotNull CharBuffer str, @NotNull TextStyle style) {
/* 235 */     if (str == null) $$$reportNull$$$0(11);  if (style == null) $$$reportNull$$$0(12);  this.myScreenBuffer.writeString(x, y - 1, str, style);
/*     */     
/* 237 */     fireModelChangeEvent();
/*     */   }
/*     */   
/*     */   public void scrollArea(int scrollRegionTop, int dy, int scrollRegionBottom) {
/* 241 */     if (dy == 0) {
/*     */       return;
/*     */     }
/* 244 */     if (dy > 0) {
/* 245 */       insertLines(scrollRegionTop - 1, dy, scrollRegionBottom);
/*     */     } else {
/* 247 */       LinesBuffer removed = deleteLines(scrollRegionTop - 1, -dy, scrollRegionBottom);
/* 248 */       if (scrollRegionTop == 1) {
/* 249 */         removed.moveTopLinesTo(removed.getLineCount(), this.myHistoryBuffer);
/*     */       }
/*     */       
/* 252 */       fireModelChangeEvent();
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getStyleLines() {
/* 257 */     final Map<Integer, Integer> hashMap = Maps.newHashMap();
/* 258 */     this.myLock.lock();
/*     */     try {
/* 260 */       final StringBuilder sb = new StringBuilder();
/* 261 */       this.myScreenBuffer.processLines(0, this.myHeight, (StyledTextConsumer)new StyledTextConsumerAdapter() {
/* 262 */             int count = 0;
/*     */ 
/*     */             
/*     */             public void consume(int x, int y, @NotNull TextStyle style, @NotNull CharBuffer characters, int startRow) {
/* 266 */               if (style == null) $$$reportNull$$$0(0);  if (characters == null) $$$reportNull$$$0(1);  if (x == 0) {
/* 267 */                 sb.append("\n");
/*     */               }
/* 269 */               int styleNum = style.getId();
/* 270 */               if (!hashMap.containsKey(Integer.valueOf(styleNum))) {
/* 271 */                 hashMap.put(Integer.valueOf(styleNum), Integer.valueOf(this.count++));
/*     */               }
/* 273 */               sb.append(String.format("%02d ", new Object[] { this.val$hashMap.get(Integer.valueOf(styleNum)) }));
/*     */             }
/*     */           });
/* 276 */       return sb.toString();
/*     */     } finally {
/* 278 */       this.myLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TerminalLine getLine(int index) {
/* 289 */     if (index >= 0) {
/* 290 */       if (index >= getHeight()) {
/* 291 */         LOG.error("Attempt to get line out of bounds: " + index + " >= " + getHeight());
/* 292 */         return TerminalLine.createEmpty();
/*     */       } 
/* 294 */       return this.myScreenBuffer.getLine(index);
/*     */     } 
/* 296 */     if (index < -getHistoryLinesCount()) {
/* 297 */       LOG.error("Attempt to get line out of bounds: " + index + " < " + -getHistoryLinesCount());
/* 298 */       return TerminalLine.createEmpty();
/*     */     } 
/* 300 */     return this.myHistoryBuffer.getLine(getHistoryLinesCount() + index);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getScreenLines() {
/* 305 */     this.myLock.lock();
/*     */     try {
/* 307 */       StringBuilder sb = new StringBuilder();
/* 308 */       for (int row = 0; row < this.myHeight; row++) {
/* 309 */         StringBuilder line = new StringBuilder(this.myScreenBuffer.getLine(row).getText());
/*     */         
/* 311 */         for (int i = line.length(); i < this.myWidth; i++) {
/* 312 */           line.append(' ');
/*     */         }
/* 314 */         if (line.length() > this.myWidth) {
/* 315 */           line.setLength(this.myWidth);
/*     */         }
/*     */         
/* 318 */         sb.append(line);
/* 319 */         sb.append('\n');
/*     */       } 
/* 321 */       return sb.toString();
/*     */     } finally {
/* 323 */       this.myLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void processScreenLines(int yStart, int yCount, @NotNull StyledTextConsumer consumer) {
/* 328 */     if (consumer == null) $$$reportNull$$$0(13);  this.myScreenBuffer.processLines(yStart, yCount, consumer);
/*     */   }
/*     */   
/*     */   public void lock() {
/* 332 */     this.myLock.lock();
/*     */   }
/*     */   
/*     */   public void unlock() {
/* 336 */     this.myLock.unlock();
/*     */   }
/*     */   
/*     */   public boolean tryLock() {
/* 340 */     return this.myLock.tryLock();
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 344 */     return this.myWidth;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 348 */     return this.myHeight;
/*     */   }
/*     */   
/*     */   public int getHistoryLinesCount() {
/* 352 */     return this.myHistoryBuffer.getLineCount();
/*     */   }
/*     */   
/*     */   public int getScreenLinesCount() {
/* 356 */     return this.myScreenBuffer.getLineCount();
/*     */   }
/*     */   
/*     */   public char getBuffersCharAt(int x, int y) {
/* 360 */     return getLine(y).charAt(x);
/*     */   }
/*     */   
/*     */   public TextStyle getStyleAt(int x, int y) {
/* 364 */     return getLine(y).getStyleAt(x);
/*     */   }
/*     */   
/*     */   public Pair<Character, TextStyle> getStyledCharAt(int x, int y) {
/* 368 */     synchronized (this.myScreenBuffer) {
/* 369 */       TerminalLine line = getLine(y);
/* 370 */       return new Pair(Character.valueOf(line.charAt(x)), line.getStyleAt(x));
/*     */     } 
/*     */   }
/*     */   
/*     */   public char getCharAt(int x, int y) {
/* 375 */     synchronized (this.myScreenBuffer) {
/* 376 */       TerminalLine line = getLine(y);
/* 377 */       return line.charAt(x);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isUsingAlternateBuffer() {
/* 382 */     return this.myUsingAlternateBuffer;
/*     */   }
/*     */   
/*     */   public void useAlternateBuffer(boolean enabled) {
/* 386 */     this.myAlternateBuffer = enabled;
/* 387 */     if (enabled) {
/* 388 */       if (!this.myUsingAlternateBuffer) {
/* 389 */         this.myScreenBufferBackup = this.myScreenBuffer;
/* 390 */         this.myHistoryBufferBackup = this.myHistoryBuffer;
/* 391 */         this.myScreenBuffer = createScreenBuffer();
/* 392 */         this.myHistoryBuffer = createHistoryBuffer();
/* 393 */         this.myUsingAlternateBuffer = true;
/*     */       }
/*     */     
/* 396 */     } else if (this.myUsingAlternateBuffer) {
/* 397 */       this.myScreenBuffer = this.myScreenBufferBackup;
/* 398 */       this.myHistoryBuffer = this.myHistoryBufferBackup;
/* 399 */       this.myScreenBufferBackup = createScreenBuffer();
/* 400 */       this.myHistoryBufferBackup = createHistoryBuffer();
/* 401 */       this.myUsingAlternateBuffer = false;
/*     */     } 
/*     */     
/* 404 */     fireModelChangeEvent();
/*     */   }
/*     */   
/*     */   public LinesBuffer getHistoryBuffer() {
/* 408 */     return this.myHistoryBuffer;
/*     */   }
/*     */   
/*     */   public void insertLines(int y, int count, int scrollRegionBottom) {
/* 412 */     this.myScreenBuffer.insertLines(y, count, scrollRegionBottom - 1, createFillerEntry());
/*     */     
/* 414 */     fireModelChangeEvent();
/*     */   }
/*     */ 
/*     */   
/*     */   public LinesBuffer deleteLines(int y, int count, int scrollRegionBottom) {
/* 419 */     LinesBuffer linesBuffer = this.myScreenBuffer.deleteLines(y, count, scrollRegionBottom - 1, createFillerEntry());
/* 420 */     fireModelChangeEvent();
/* 421 */     return linesBuffer;
/*     */   }
/*     */   
/*     */   public void clearLines(int startRow, int endRow) {
/* 425 */     this.myScreenBuffer.clearLines(startRow, endRow, createFillerEntry());
/* 426 */     fireModelChangeEvent();
/*     */   }
/*     */   
/*     */   public void eraseCharacters(int leftX, int rightX, int y) {
/* 430 */     TextStyle style = createEmptyStyleWithCurrentColor();
/* 431 */     if (y >= 0) {
/* 432 */       this.myScreenBuffer.clearArea(leftX, y, rightX, y + 1, style);
/* 433 */       fireModelChangeEvent();
/* 434 */       if (this.myTextProcessing != null && y < getHeight()) {
/* 435 */         this.myTextProcessing.processHyperlinks(this.myScreenBuffer, getLine(y));
/*     */       }
/*     */     } else {
/* 438 */       LOG.error("Attempt to erase characters in line: " + y);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void clearAll() {
/* 443 */     this.myScreenBuffer.clearAll();
/* 444 */     fireModelChangeEvent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void processHistoryAndScreenLines(int scrollOrigin, int maximalLinesToProcess, StyledTextConsumer consumer) {
/* 451 */     if (maximalLinesToProcess < 0)
/*     */     {
/*     */       
/* 454 */       maximalLinesToProcess = this.myHistoryBuffer.getLineCount() + this.myScreenBuffer.getLineCount();
/*     */     }
/*     */     
/* 457 */     int linesFromHistory = Math.min(-scrollOrigin, maximalLinesToProcess);
/*     */     
/* 459 */     int y = this.myHistoryBuffer.getLineCount() + scrollOrigin;
/* 460 */     if (y < 0) {
/* 461 */       y = 0;
/*     */     }
/* 463 */     this.myHistoryBuffer.processLines(y, linesFromHistory, consumer, y);
/*     */     
/* 465 */     if (linesFromHistory < maximalLinesToProcess)
/*     */     {
/* 467 */       this.myScreenBuffer.processLines(0, maximalLinesToProcess - linesFromHistory, consumer, -linesFromHistory);
/*     */     }
/*     */   }
/*     */   
/*     */   public void clearHistory() {
/* 472 */     this.myHistoryBuffer.clearAll();
/* 473 */     fireModelChangeEvent();
/*     */   }
/*     */   
/*     */   void moveScreenLinesToHistory() {
/* 477 */     this.myLock.lock();
/*     */     try {
/* 479 */       this.myScreenBuffer.removeBottomEmptyLines(this.myScreenBuffer.getLineCount() - 1, this.myScreenBuffer.getLineCount());
/* 480 */       this.myScreenBuffer.moveTopLinesTo(this.myScreenBuffer.getLineCount(), this.myHistoryBuffer);
/* 481 */       if (this.myHistoryBuffer.getLineCount() > 0) {
/* 482 */         this.myHistoryBuffer.getLine(this.myHistoryBuffer.getLineCount() - 1).setWrapped(false);
/*     */       }
/*     */     } finally {
/*     */       
/* 486 */       this.myLock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   LinesBuffer getHistoryBufferOrBackup() {
/* 492 */     if ((this.myUsingAlternateBuffer ? this.myHistoryBufferBackup : this.myHistoryBuffer) == null) $$$reportNull$$$0(14);  return this.myUsingAlternateBuffer ? this.myHistoryBufferBackup : this.myHistoryBuffer;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   LinesBuffer getScreenBufferOrBackup() {
/* 497 */     if ((this.myUsingAlternateBuffer ? this.myScreenBufferBackup : this.myScreenBuffer) == null) $$$reportNull$$$0(15);  return this.myUsingAlternateBuffer ? this.myScreenBufferBackup : this.myScreenBuffer;
/*     */   }
/*     */   
/*     */   public int findScreenLineIndex(@NotNull TerminalLine line) {
/* 501 */     if (line == null) $$$reportNull$$$0(16);  return this.myScreenBuffer.findLineIndex(line);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\model\TerminalTextBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */