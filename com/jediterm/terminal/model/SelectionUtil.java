/*     */ package com.jediterm.terminal.model;
/*     */ 
/*     */ import com.jediterm.terminal.util.Pair;
/*     */ import java.awt.Point;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SelectionUtil
/*     */ {
/*  16 */   private static final Logger LOG = Logger.getLogger(SelectionUtil.class);
/*     */   
/*  18 */   private static final List<Character> SEPARATORS = new ArrayList<>();
/*     */   static {
/*  20 */     SEPARATORS.add(Character.valueOf(' '));
/*  21 */     SEPARATORS.add(Character.valueOf(' '));
/*  22 */     SEPARATORS.add(Character.valueOf('\t'));
/*  23 */     SEPARATORS.add(Character.valueOf('\''));
/*  24 */     SEPARATORS.add(Character.valueOf('"'));
/*  25 */     SEPARATORS.add(Character.valueOf('$'));
/*  26 */     SEPARATORS.add(Character.valueOf('('));
/*  27 */     SEPARATORS.add(Character.valueOf(')'));
/*  28 */     SEPARATORS.add(Character.valueOf('['));
/*  29 */     SEPARATORS.add(Character.valueOf(']'));
/*  30 */     SEPARATORS.add(Character.valueOf('{'));
/*  31 */     SEPARATORS.add(Character.valueOf('}'));
/*  32 */     SEPARATORS.add(Character.valueOf('<'));
/*  33 */     SEPARATORS.add(Character.valueOf('>'));
/*     */   }
/*     */   
/*     */   public static List<Character> getDefaultSeparators() {
/*  37 */     return new ArrayList<>(SEPARATORS);
/*     */   }
/*     */   
/*     */   public static Pair<Point, Point> sortPoints(Point a, Point b) {
/*  41 */     if (a.y == b.y) {
/*  42 */       return Pair.create((a.x <= b.x) ? a : b, (a.x > b.x) ? a : b);
/*     */     }
/*     */     
/*  45 */     return Pair.create((a.y < b.y) ? a : b, (a.y > b.y) ? a : b);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getSelectionText(TerminalSelection selection, TerminalTextBuffer terminalTextBuffer) {
/*  50 */     return getSelectionText(selection.getStart(), selection.getEnd(), terminalTextBuffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static String getSelectionText(@NotNull Point selectionStart, @NotNull Point selectionEnd, @NotNull TerminalTextBuffer terminalTextBuffer) {
/*  58 */     if (selectionStart == null) $$$reportNull$$$0(0);  if (selectionEnd == null) $$$reportNull$$$0(1);  if (terminalTextBuffer == null) $$$reportNull$$$0(2);  Pair<Point, Point> pair = sortPoints(selectionStart, selectionEnd);
/*  59 */     ((Point)pair.first).y = Math.max(((Point)pair.first).y, -terminalTextBuffer.getHistoryLinesCount());
/*  60 */     pair = sortPoints((Point)pair.first, (Point)pair.second);
/*     */     
/*  62 */     Point top = (Point)pair.first;
/*  63 */     Point bottom = (Point)pair.second;
/*     */     
/*  65 */     StringBuilder selectionText = new StringBuilder();
/*     */     
/*  67 */     for (int i = top.y; i <= bottom.y; i++) {
/*  68 */       TerminalLine line = terminalTextBuffer.getLine(i);
/*  69 */       String text = line.getText();
/*  70 */       if (i == top.y) {
/*  71 */         if (i == bottom.y) {
/*  72 */           selectionText.append(processForSelection(text.substring(Math.min(text.length(), top.x), Math.min(text.length(), bottom.x))));
/*     */         } else {
/*  74 */           selectionText.append(processForSelection(text.substring(Math.min(text.length(), top.x))));
/*     */         }
/*     */       
/*  77 */       } else if (i == bottom.y) {
/*  78 */         selectionText.append(processForSelection(text.substring(0, Math.min(text.length(), bottom.x))));
/*     */       } else {
/*     */         
/*  81 */         selectionText.append(processForSelection(line.getText()));
/*     */       } 
/*  83 */       if ((!line.isWrapped() && i < bottom.y) || bottom.x > text.length()) {
/*  84 */         selectionText.append("\n");
/*     */       }
/*     */     } 
/*     */     
/*  88 */     if (selectionText.toString() == null) $$$reportNull$$$0(3);  return selectionText.toString();
/*     */   }
/*     */   
/*     */   private static String processForSelection(String text) {
/*  92 */     if (text.indexOf('') != 0) {
/*     */       
/*  94 */       StringBuilder sb = new StringBuilder();
/*  95 */       for (char c : text.toCharArray()) {
/*  96 */         if (c != '') {
/*  97 */           sb.append(c);
/*     */         }
/*     */       } 
/* 100 */       return sb.toString();
/*     */     } 
/* 102 */     return text;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Point getPreviousSeparator(Point charCoords, TerminalTextBuffer terminalTextBuffer) {
/* 107 */     return getPreviousSeparator(charCoords, terminalTextBuffer, SEPARATORS);
/*     */   }
/*     */   
/*     */   public static Point getPreviousSeparator(Point charCoords, TerminalTextBuffer terminalTextBuffer, @NotNull List<Character> separators) {
/* 111 */     if (separators == null) $$$reportNull$$$0(4);  int x = charCoords.x;
/* 112 */     int y = charCoords.y;
/* 113 */     int terminalWidth = terminalTextBuffer.getWidth();
/*     */     
/* 115 */     if (separators.contains(Character.valueOf(terminalTextBuffer.getBuffersCharAt(x, y)))) {
/* 116 */       return new Point(x, y);
/*     */     }
/*     */     
/* 119 */     String line = terminalTextBuffer.getLine(y).getText();
/* 120 */     while (x < line.length() && !separators.contains(Character.valueOf(line.charAt(x)))) {
/* 121 */       x--;
/* 122 */       if (x < 0) {
/* 123 */         if (y <= -terminalTextBuffer.getHistoryLinesCount()) {
/* 124 */           return new Point(0, y);
/*     */         }
/* 126 */         y--;
/* 127 */         x = terminalWidth - 1;
/*     */         
/* 129 */         line = terminalTextBuffer.getLine(y).getText();
/*     */       } 
/*     */     } 
/*     */     
/* 133 */     x++;
/* 134 */     if (x >= terminalWidth) {
/* 135 */       y++;
/* 136 */       x = 0;
/*     */     } 
/*     */     
/* 139 */     return new Point(x, y);
/*     */   }
/*     */   
/*     */   public static Point getNextSeparator(Point charCoords, TerminalTextBuffer terminalTextBuffer) {
/* 143 */     return getNextSeparator(charCoords, terminalTextBuffer, SEPARATORS);
/*     */   }
/*     */   
/*     */   public static Point getNextSeparator(Point charCoords, TerminalTextBuffer terminalTextBuffer, @NotNull List<Character> separators) {
/* 147 */     if (separators == null) $$$reportNull$$$0(5);  int x = charCoords.x;
/* 148 */     int y = charCoords.y;
/* 149 */     int terminalWidth = terminalTextBuffer.getWidth();
/* 150 */     int terminalHeight = terminalTextBuffer.getHeight();
/*     */     
/* 152 */     if (separators.contains(Character.valueOf(terminalTextBuffer.getBuffersCharAt(x, y)))) {
/* 153 */       return new Point(x, y);
/*     */     }
/*     */     
/* 156 */     String line = terminalTextBuffer.getLine(y).getText();
/* 157 */     while (x < line.length() && !separators.contains(Character.valueOf(line.charAt(x)))) {
/* 158 */       x++;
/* 159 */       if (x >= terminalWidth) {
/* 160 */         if (y >= terminalHeight - 1) {
/* 161 */           return new Point(terminalWidth - 1, terminalHeight - 1);
/*     */         }
/* 163 */         y++;
/* 164 */         x = 0;
/*     */         
/* 166 */         line = terminalTextBuffer.getLine(y).getText();
/*     */       } 
/*     */     } 
/*     */     
/* 170 */     x--;
/* 171 */     if (x < 0) {
/* 172 */       y--;
/* 173 */       x = terminalWidth - 1;
/*     */     } 
/*     */     
/* 176 */     return new Point(x, y);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\model\SelectionUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */