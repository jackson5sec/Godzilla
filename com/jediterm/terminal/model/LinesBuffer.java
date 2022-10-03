/*     */ package com.jediterm.terminal.model;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.jediterm.terminal.StyledTextConsumer;
/*     */ import com.jediterm.terminal.TextStyle;
/*     */ import com.jediterm.terminal.model.hyperlinks.TextProcessing;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LinesBuffer
/*     */ {
/*  19 */   private static final Logger LOG = Logger.getLogger(LinesBuffer.class);
/*     */ 
/*     */   
/*     */   public static final int DEFAULT_MAX_LINES_COUNT = 5000;
/*     */   
/*  24 */   private int myBufferMaxLinesCount = 5000;
/*     */   
/*  26 */   private ArrayList<TerminalLine> myLines = Lists.newArrayList();
/*     */   
/*     */   @Nullable
/*     */   private final TextProcessing myTextProcessing;
/*     */   
/*     */   public LinesBuffer(@Nullable TextProcessing textProcessing) {
/*  32 */     this.myTextProcessing = textProcessing;
/*     */   }
/*     */   
/*     */   public LinesBuffer(int bufferMaxLinesCount, @Nullable TextProcessing textProcessing) {
/*  36 */     this.myBufferMaxLinesCount = bufferMaxLinesCount;
/*  37 */     this.myTextProcessing = textProcessing;
/*     */   }
/*     */   
/*     */   public synchronized String getLines() {
/*  41 */     StringBuilder sb = new StringBuilder();
/*     */     
/*  43 */     boolean first = true;
/*     */     
/*  45 */     for (TerminalLine line : this.myLines) {
/*  46 */       if (!first) {
/*  47 */         sb.append("\n");
/*     */       }
/*     */       
/*  50 */       sb.append(line.getText());
/*  51 */       first = false;
/*     */     } 
/*     */     
/*  54 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void addNewLine(@NotNull TextStyle style, @NotNull CharBuffer characters) {
/*  59 */     if (style == null) $$$reportNull$$$0(0);  if (characters == null) $$$reportNull$$$0(1);  addNewLine(new TerminalLine.TextEntry(style, characters));
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized void addNewLine(@NotNull TerminalLine.TextEntry entry) {
/*  64 */     if (entry == null) $$$reportNull$$$0(2);  addLine(new TerminalLine(entry));
/*     */   }
/*     */   
/*     */   private synchronized void addLine(@NotNull TerminalLine line) {
/*  68 */     if (line == null) $$$reportNull$$$0(3);  if (this.myBufferMaxLinesCount > 0 && this.myLines.size() >= this.myBufferMaxLinesCount) {
/*  69 */       removeTopLines(1);
/*     */     }
/*     */     
/*  72 */     this.myLines.add(line);
/*     */   }
/*     */   
/*     */   public synchronized int getLineCount() {
/*  76 */     return this.myLines.size();
/*     */   }
/*     */   
/*     */   public synchronized void removeTopLines(int count) {
/*  80 */     if (count >= this.myLines.size()) {
/*  81 */       this.myLines = Lists.newArrayList();
/*     */     } else {
/*  83 */       this.myLines = Lists.newArrayList(this.myLines.subList(count, this.myLines.size()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getLineText(int row) {
/*  88 */     TerminalLine line = getLine(row);
/*     */     
/*  90 */     return line.getText();
/*     */   }
/*     */   
/*     */   public synchronized void insertLines(int y, int count, int lastLine, @NotNull TerminalLine.TextEntry filler) {
/*  94 */     if (filler == null) $$$reportNull$$$0(4);  LinesBuffer tail = new LinesBuffer(this.myTextProcessing);
/*     */     
/*  96 */     if (lastLine < getLineCount() - 1) {
/*  97 */       moveBottomLinesTo(getLineCount() - lastLine - 1, tail);
/*     */     }
/*     */     
/* 100 */     LinesBuffer head = new LinesBuffer(this.myTextProcessing);
/* 101 */     if (y > 0) {
/* 102 */       moveTopLinesTo(y, head);
/*     */     }
/*     */     
/* 105 */     for (int i = 0; i < count; i++) {
/* 106 */       head.addNewLine(filler);
/*     */     }
/*     */     
/* 109 */     head.moveBottomLinesTo(head.getLineCount(), this);
/*     */     
/* 111 */     removeBottomLines(count);
/*     */     
/* 113 */     tail.moveTopLinesTo(tail.getLineCount(), this);
/*     */   }
/*     */   
/*     */   public synchronized LinesBuffer deleteLines(int y, int count, int lastLine, @NotNull TerminalLine.TextEntry filler) {
/* 117 */     if (filler == null) $$$reportNull$$$0(5);  LinesBuffer tail = new LinesBuffer(this.myTextProcessing);
/*     */     
/* 119 */     if (lastLine < getLineCount() - 1) {
/* 120 */       moveBottomLinesTo(getLineCount() - lastLine - 1, tail);
/*     */     }
/*     */     
/* 123 */     LinesBuffer head = new LinesBuffer(this.myTextProcessing);
/* 124 */     if (y > 0) {
/* 125 */       moveTopLinesTo(y, head);
/*     */     }
/*     */     
/* 128 */     int toRemove = Math.min(count, getLineCount());
/*     */     
/* 130 */     LinesBuffer removed = new LinesBuffer(this.myTextProcessing);
/* 131 */     moveTopLinesTo(toRemove, removed);
/*     */     
/* 133 */     head.moveBottomLinesTo(head.getLineCount(), this);
/*     */     
/* 135 */     for (int i = 0; i < toRemove; i++) {
/* 136 */       addNewLine(filler);
/*     */     }
/*     */     
/* 139 */     tail.moveTopLinesTo(tail.getLineCount(), this);
/*     */     
/* 141 */     return removed;
/*     */   }
/*     */   
/*     */   public synchronized void writeString(int x, int y, CharBuffer str, @NotNull TextStyle style) {
/* 145 */     if (style == null) $$$reportNull$$$0(6);  TerminalLine line = getLine(y);
/*     */     
/* 147 */     line.writeString(x, str, style);
/*     */     
/* 149 */     if (this.myTextProcessing != null) {
/* 150 */       this.myTextProcessing.processHyperlinks(this, line);
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void clearLines(int startRow, int endRow, @NotNull TerminalLine.TextEntry filler) {
/* 155 */     if (filler == null) $$$reportNull$$$0(7);  for (int i = startRow; i <= endRow; i++) {
/* 156 */       getLine(i).clear(filler);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void clearAll() {
/* 162 */     this.myLines.clear();
/*     */   }
/*     */   
/*     */   public synchronized void deleteCharacters(int x, int y, int count, @NotNull TextStyle style) {
/* 166 */     if (style == null) $$$reportNull$$$0(8);  TerminalLine line = getLine(y);
/* 167 */     line.deleteCharacters(x, count, style);
/*     */   }
/*     */   
/*     */   public synchronized void insertBlankCharacters(int x, int y, int count, int maxLen, @NotNull TextStyle style) {
/* 171 */     if (style == null) $$$reportNull$$$0(9);  TerminalLine line = getLine(y);
/* 172 */     line.insertBlankCharacters(x, count, maxLen, style);
/*     */   }
/*     */   
/*     */   public synchronized void clearArea(int leftX, int topY, int rightX, int bottomY, @NotNull TextStyle style) {
/* 176 */     if (style == null) $$$reportNull$$$0(10);  for (int y = topY; y < bottomY; y++) {
/* 177 */       TerminalLine line = getLine(y);
/* 178 */       line.clearArea(leftX, rightX, style);
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void processLines(int yStart, int yCount, @NotNull StyledTextConsumer consumer) {
/* 183 */     if (consumer == null) $$$reportNull$$$0(11);  processLines(yStart, yCount, consumer, -getLineCount());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void processLines(int firstLine, int count, @NotNull StyledTextConsumer consumer, int startRow) {
/* 190 */     if (consumer == null) $$$reportNull$$$0(12);  if (firstLine < 0) {
/* 191 */       throw new IllegalArgumentException("firstLine=" + firstLine + ", should be >0");
/*     */     }
/* 193 */     for (int y = firstLine; y < Math.min(firstLine + count, this.myLines.size()); y++) {
/* 194 */       ((TerminalLine)this.myLines.get(y)).process(y, consumer, startRow);
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized void moveTopLinesTo(int count, @NotNull LinesBuffer buffer) {
/* 199 */     if (buffer == null) $$$reportNull$$$0(13);  count = Math.min(count, getLineCount());
/* 200 */     buffer.addLines(this.myLines.subList(0, count));
/* 201 */     removeTopLines(count);
/*     */   }
/*     */   
/*     */   public synchronized void addLines(@NotNull List<TerminalLine> lines) {
/* 205 */     if (lines == null) $$$reportNull$$$0(14);  if (this.myBufferMaxLinesCount > 0) {
/*     */       
/* 207 */       if (lines.size() >= this.myBufferMaxLinesCount) {
/* 208 */         int index = lines.size() - this.myBufferMaxLinesCount;
/* 209 */         this.myLines = Lists.newArrayList(lines.subList(index, lines.size()));
/*     */         
/*     */         return;
/*     */       } 
/* 213 */       int count = this.myLines.size() + lines.size();
/* 214 */       if (count >= this.myBufferMaxLinesCount) {
/* 215 */         removeTopLines(count - this.myBufferMaxLinesCount);
/*     */       }
/*     */     } 
/*     */     
/* 219 */     this.myLines.addAll(lines);
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public synchronized TerminalLine getLine(int row) {
/* 224 */     if (row < 0) {
/* 225 */       LOG.error("Negative line number: " + row);
/* 226 */       if (TerminalLine.createEmpty() == null) $$$reportNull$$$0(15);  return TerminalLine.createEmpty();
/*     */     } 
/*     */     
/* 229 */     for (int i = getLineCount(); i <= row; i++) {
/* 230 */       addLine(TerminalLine.createEmpty());
/*     */     }
/*     */     
/* 233 */     if ((TerminalLine)this.myLines.get(row) == null) $$$reportNull$$$0(16);  return this.myLines.get(row);
/*     */   }
/*     */   
/*     */   public synchronized void moveBottomLinesTo(int count, @NotNull LinesBuffer buffer) {
/* 237 */     if (buffer == null) $$$reportNull$$$0(17);  count = Math.min(count, getLineCount());
/* 238 */     buffer.addLinesFirst(this.myLines.subList(getLineCount() - count, getLineCount()));
/*     */     
/* 240 */     removeBottomLines(count);
/*     */   }
/*     */   
/*     */   private synchronized void addLinesFirst(@NotNull List<TerminalLine> lines) {
/* 244 */     if (lines == null) $$$reportNull$$$0(18);  List<TerminalLine> list = Lists.newArrayList(lines);
/* 245 */     list.addAll(this.myLines);
/* 246 */     this.myLines = Lists.newArrayList(list);
/*     */   }
/*     */   
/*     */   private synchronized void removeBottomLines(int count) {
/* 250 */     this.myLines = Lists.newArrayList(this.myLines.subList(0, getLineCount() - count));
/*     */   }
/*     */   
/*     */   public int removeBottomEmptyLines(int ind, int maxCount) {
/* 254 */     int i = 0;
/* 255 */     while (maxCount - i > 0 && (ind >= this.myLines.size() || ((TerminalLine)this.myLines.get(ind)).isNul())) {
/* 256 */       if (ind < this.myLines.size()) {
/* 257 */         this.myLines.remove(ind);
/*     */       }
/* 259 */       ind--;
/* 260 */       i++;
/*     */     } 
/*     */     
/* 263 */     return i;
/*     */   }
/*     */   
/*     */   synchronized int findLineIndex(@NotNull TerminalLine line) {
/* 267 */     if (line == null) $$$reportNull$$$0(19);  return this.myLines.indexOf(line);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\model\LinesBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */