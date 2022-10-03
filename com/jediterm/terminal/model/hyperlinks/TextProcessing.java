/*     */ package com.jediterm.terminal.model.hyperlinks;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.jediterm.terminal.HyperlinkStyle;
/*     */ import com.jediterm.terminal.TextStyle;
/*     */ import com.jediterm.terminal.model.CharBuffer;
/*     */ import com.jediterm.terminal.model.LinesBuffer;
/*     */ import com.jediterm.terminal.model.TerminalLine;
/*     */ import com.jediterm.terminal.model.TerminalTextBuffer;
/*     */ import java.util.List;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TextProcessing
/*     */ {
/*  22 */   private static final Logger LOG = Logger.getLogger(TextProcessing.class);
/*     */   
/*     */   private final List<HyperlinkFilter> myHyperlinkFilter;
/*     */   
/*     */   private TextStyle myHyperlinkColor;
/*     */   private HyperlinkStyle.HighlightMode myHighlightMode;
/*     */   private TerminalTextBuffer myTerminalTextBuffer;
/*     */   
/*     */   public TextProcessing(@NotNull TextStyle hyperlinkColor, @NotNull HyperlinkStyle.HighlightMode highlightMode) {
/*  31 */     this.myHyperlinkColor = hyperlinkColor;
/*  32 */     this.myHighlightMode = highlightMode;
/*  33 */     this.myHyperlinkFilter = Lists.newArrayList();
/*     */   }
/*     */   
/*     */   public void setTerminalTextBuffer(@NotNull TerminalTextBuffer terminalTextBuffer) {
/*  37 */     if (terminalTextBuffer == null) $$$reportNull$$$0(2);  this.myTerminalTextBuffer = terminalTextBuffer;
/*     */   }
/*     */   
/*     */   public void processHyperlinks(@NotNull LinesBuffer buffer, @NotNull TerminalLine updatedLine) {
/*  41 */     if (buffer == null) $$$reportNull$$$0(3);  if (updatedLine == null) $$$reportNull$$$0(4);  if (this.myHyperlinkFilter.isEmpty())
/*  42 */       return;  doProcessHyperlinks(buffer, updatedLine);
/*     */   }
/*     */   
/*     */   private void doProcessHyperlinks(@NotNull LinesBuffer buffer, @NotNull TerminalLine updatedLine) {
/*  46 */     if (buffer == null) $$$reportNull$$$0(5);  if (updatedLine == null) $$$reportNull$$$0(6);  this.myTerminalTextBuffer.lock();
/*     */     try {
/*  48 */       int updatedLineInd = findLineInd(buffer, updatedLine);
/*  49 */       if (updatedLineInd == -1) {
/*     */         
/*  51 */         updatedLineInd = findHistoryLineInd(this.myTerminalTextBuffer.getHistoryBuffer(), updatedLine);
/*  52 */         if (updatedLineInd == -1) {
/*  53 */           LOG.debug("Cannot find line for links processing");
/*     */           return;
/*     */         } 
/*  56 */         buffer = this.myTerminalTextBuffer.getHistoryBuffer();
/*     */       } 
/*  58 */       int startLineInd = updatedLineInd;
/*  59 */       while (startLineInd > 0 && buffer.getLine(startLineInd - 1).isWrapped()) {
/*  60 */         startLineInd--;
/*     */       }
/*  62 */       String lineStr = joinLines(buffer, startLineInd, updatedLineInd);
/*  63 */       for (HyperlinkFilter filter : this.myHyperlinkFilter) {
/*  64 */         LinkResult result = filter.apply(lineStr);
/*  65 */         if (result != null) {
/*  66 */           for (LinkResultItem item : result.getItems()) {
/*     */             
/*  68 */             HyperlinkStyle hyperlinkStyle = new HyperlinkStyle(this.myHyperlinkColor.getForeground(), this.myHyperlinkColor.getBackground(), item.getLinkInfo(), this.myHighlightMode, null);
/*  69 */             if (item.getStartOffset() < 0 || item.getEndOffset() > lineStr.length())
/*     */               continue; 
/*  71 */             int prevLinesLength = 0;
/*  72 */             for (int lineInd = startLineInd; lineInd <= updatedLineInd; lineInd++) {
/*  73 */               int startLineOffset = Math.max(prevLinesLength, item.getStartOffset());
/*  74 */               int endLineOffset = Math.min(prevLinesLength + this.myTerminalTextBuffer.getWidth(), item.getEndOffset());
/*  75 */               if (startLineOffset < endLineOffset) {
/*  76 */                 buffer.getLine(lineInd).writeString(startLineOffset - prevLinesLength, new CharBuffer(lineStr.substring(startLineOffset, endLineOffset)), (TextStyle)hyperlinkStyle);
/*     */               }
/*  78 */               prevLinesLength += this.myTerminalTextBuffer.getWidth();
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } finally {
/*     */       
/*  85 */       this.myTerminalTextBuffer.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   private int findHistoryLineInd(@NotNull LinesBuffer historyBuffer, @NotNull TerminalLine line) {
/*  90 */     if (historyBuffer == null) $$$reportNull$$$0(7);  if (line == null) $$$reportNull$$$0(8);  int lastLineInd = Math.max(0, historyBuffer.getLineCount() - 200);
/*  91 */     for (int i = historyBuffer.getLineCount() - 1; i >= lastLineInd; i--) {
/*  92 */       if (historyBuffer.getLine(i) == line) {
/*  93 */         return i;
/*     */       }
/*     */     } 
/*  96 */     return -1;
/*     */   }
/*     */   
/*     */   private static int findLineInd(@NotNull LinesBuffer buffer, @NotNull TerminalLine line) {
/* 100 */     if (buffer == null) $$$reportNull$$$0(9);  if (line == null) $$$reportNull$$$0(10);  for (int i = 0; i < buffer.getLineCount(); i++) {
/* 101 */       TerminalLine l = buffer.getLine(i);
/* 102 */       if (l == line) {
/* 103 */         return i;
/*     */       }
/*     */     } 
/* 106 */     return -1;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   private String joinLines(@NotNull LinesBuffer buffer, int startLineInd, int updatedLineInd) {
/* 111 */     if (buffer == null) $$$reportNull$$$0(11);  StringBuilder result = new StringBuilder();
/* 112 */     for (int i = startLineInd; i <= updatedLineInd; i++) {
/* 113 */       String text = buffer.getLine(i).getText();
/* 114 */       if (i < updatedLineInd && text.length() < this.myTerminalTextBuffer.getWidth()) {
/* 115 */         text = text + new CharBuffer(false, this.myTerminalTextBuffer.getWidth() - text.length());
/*     */       }
/* 117 */       result.append(text);
/*     */     } 
/* 119 */     if (result.toString() == null) $$$reportNull$$$0(12);  return result.toString();
/*     */   }
/*     */   
/*     */   public void addHyperlinkFilter(@NotNull HyperlinkFilter filter) {
/* 123 */     if (filter == null) $$$reportNull$$$0(13);  this.myHyperlinkFilter.add(filter);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\model\hyperlinks\TextProcessing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */