/*     */ package org.fife.print;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.print.PageFormat;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.Segment;
/*     */ import javax.swing.text.TabExpander;
/*     */ import javax.swing.text.Utilities;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class RPrintUtilities
/*     */ {
/*     */   private static int currentDocLineNumber;
/*     */   private static int numDocLines;
/*     */   private static Element rootElement;
/*  42 */   private static final char[] BREAK_CHARS = new char[] { ' ', '\t', ',', '.', ';', '?', '!' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int xOffset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int tabSizeInSpaces;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static FontMetrics fm;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getLineBreakPoint(String line, int maxCharsPerLine) {
/*  78 */     int breakPoint = -1;
/*  79 */     for (char breakChar : BREAK_CHARS) {
/*  80 */       int breakCharPos = line.lastIndexOf(breakChar, maxCharsPerLine - 1);
/*  81 */       if (breakCharPos > breakPoint) {
/*  82 */         breakPoint = breakCharPos;
/*     */       }
/*     */     } 
/*     */     
/*  86 */     return (breakPoint == -1) ? (maxCharsPerLine - 1) : breakPoint;
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
/*     */   public static int printDocumentMonospaced(Graphics g, Document doc, int fontSize, int pageIndex, PageFormat pageFormat, int tabSize) {
/* 108 */     g.setColor(Color.BLACK);
/* 109 */     g.setFont(new Font("Monospaced", 0, fontSize));
/*     */ 
/*     */     
/* 112 */     tabSizeInSpaces = tabSize;
/* 113 */     fm = g.getFontMetrics();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 119 */     int fontWidth = fm.charWidth('w');
/* 120 */     int fontHeight = fm.getHeight();
/*     */     
/* 122 */     int maxCharsPerLine = (int)pageFormat.getImageableWidth() / fontWidth;
/* 123 */     int maxLinesPerPage = (int)pageFormat.getImageableHeight() / fontHeight;
/*     */     
/* 125 */     int startingLineNumber = maxLinesPerPage * pageIndex;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 130 */     xOffset = (int)pageFormat.getImageableX();
/* 131 */     int y = (int)pageFormat.getImageableY() + fm.getAscent() + 1;
/*     */ 
/*     */ 
/*     */     
/* 135 */     int numPrintedLines = 0;
/*     */ 
/*     */     
/* 138 */     currentDocLineNumber = 0;
/* 139 */     rootElement = doc.getDefaultRootElement();
/* 140 */     numDocLines = rootElement.getElementCount();
/* 141 */     while (currentDocLineNumber < numDocLines) {
/*     */ 
/*     */ 
/*     */       
/* 145 */       Element currentLine = rootElement.getElement(currentDocLineNumber);
/* 146 */       int startOffs = currentLine.getStartOffset();
/*     */       try {
/* 148 */         curLineString = doc.getText(startOffs, currentLine.getEndOffset() - startOffs);
/* 149 */       } catch (BadLocationException ble) {
/* 150 */         ble.printStackTrace();
/* 151 */         return 1;
/*     */       } 
/*     */ 
/*     */       
/* 155 */       String curLineString = curLineString.replaceAll("\n", "");
/*     */ 
/*     */       
/* 158 */       if (tabSizeInSpaces == 0) {
/* 159 */         curLineString = curLineString.replaceAll("\t", "");
/*     */       } else {
/*     */         
/* 162 */         int tabIndex = curLineString.indexOf('\t');
/* 163 */         while (tabIndex > -1) {
/* 164 */           int spacesNeeded = tabSizeInSpaces - tabIndex % tabSizeInSpaces;
/* 165 */           StringBuilder stringBuilder = new StringBuilder();
/* 166 */           for (int i = 0; i < spacesNeeded; i++) {
/* 167 */             stringBuilder.append(" ");
/*     */           }
/*     */           
/* 170 */           curLineString = curLineString.replaceFirst("\t", stringBuilder.toString());
/* 171 */           tabIndex = curLineString.indexOf('\t');
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 177 */       while (curLineString.length() > maxCharsPerLine) {
/*     */         
/* 179 */         numPrintedLines++;
/* 180 */         if (numPrintedLines > startingLineNumber) {
/* 181 */           g.drawString(curLineString.substring(0, maxCharsPerLine), xOffset, y);
/* 182 */           y += fontHeight;
/* 183 */           if (numPrintedLines == startingLineNumber + maxLinesPerPage) {
/* 184 */             return 0;
/*     */           }
/*     */         } 
/*     */         
/* 188 */         curLineString = curLineString.substring(maxCharsPerLine, curLineString.length());
/*     */       } 
/*     */ 
/*     */       
/* 192 */       currentDocLineNumber++;
/*     */       
/* 194 */       numPrintedLines++;
/* 195 */       if (numPrintedLines > startingLineNumber) {
/* 196 */         g.drawString(curLineString, xOffset, y);
/* 197 */         y += fontHeight;
/* 198 */         if (numPrintedLines == startingLineNumber + maxLinesPerPage) {
/* 199 */           return 0;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 207 */     if (numPrintedLines > startingLineNumber) {
/* 208 */       return 0;
/*     */     }
/* 210 */     return 1;
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
/*     */   public static int printDocumentMonospacedWordWrap(Graphics g, Document doc, int fontSize, int pageIndex, PageFormat pageFormat, int tabSize) {
/* 233 */     g.setColor(Color.BLACK);
/* 234 */     g.setFont(new Font("Monospaced", 0, fontSize));
/*     */ 
/*     */     
/* 237 */     tabSizeInSpaces = tabSize;
/* 238 */     fm = g.getFontMetrics();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 244 */     int fontWidth = fm.charWidth('w');
/* 245 */     int fontHeight = fm.getHeight();
/*     */     
/* 247 */     int maxCharsPerLine = (int)pageFormat.getImageableWidth() / fontWidth;
/* 248 */     int maxLinesPerPage = (int)pageFormat.getImageableHeight() / fontHeight;
/*     */     
/* 250 */     int startingLineNumber = maxLinesPerPage * pageIndex;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 255 */     xOffset = (int)pageFormat.getImageableX();
/* 256 */     int y = (int)pageFormat.getImageableY() + fm.getAscent() + 1;
/*     */ 
/*     */ 
/*     */     
/* 260 */     int numPrintedLines = 0;
/*     */ 
/*     */     
/* 263 */     currentDocLineNumber = 0;
/* 264 */     rootElement = doc.getDefaultRootElement();
/* 265 */     numDocLines = rootElement.getElementCount();
/* 266 */     while (currentDocLineNumber < numDocLines) {
/*     */ 
/*     */ 
/*     */       
/* 270 */       Element currentLine = rootElement.getElement(currentDocLineNumber);
/* 271 */       int startOffs = currentLine.getStartOffset();
/*     */       try {
/* 273 */         curLineString = doc.getText(startOffs, currentLine.getEndOffset() - startOffs);
/* 274 */       } catch (BadLocationException ble) {
/* 275 */         ble.printStackTrace();
/* 276 */         return 1;
/*     */       } 
/*     */ 
/*     */       
/* 280 */       String curLineString = curLineString.replaceAll("\n", "");
/*     */ 
/*     */       
/* 283 */       if (tabSizeInSpaces == 0) {
/* 284 */         curLineString = curLineString.replaceAll("\t", "");
/*     */       } else {
/*     */         
/* 287 */         int tabIndex = curLineString.indexOf('\t');
/* 288 */         while (tabIndex > -1) {
/* 289 */           int spacesNeeded = tabSizeInSpaces - tabIndex % tabSizeInSpaces;
/* 290 */           StringBuilder stringBuilder = new StringBuilder();
/* 291 */           for (int i = 0; i < spacesNeeded; i++) {
/* 292 */             stringBuilder.append(" ");
/*     */           }
/*     */           
/* 295 */           curLineString = curLineString.replaceFirst("\t", stringBuilder.toString());
/* 296 */           tabIndex = curLineString.indexOf('\t');
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 302 */       while (curLineString.length() > maxCharsPerLine) {
/*     */         
/* 304 */         int breakPoint = getLineBreakPoint(curLineString, maxCharsPerLine) + 1;
/*     */         
/* 306 */         numPrintedLines++;
/* 307 */         if (numPrintedLines > startingLineNumber) {
/* 308 */           g.drawString(curLineString.substring(0, breakPoint), xOffset, y);
/* 309 */           y += fontHeight;
/* 310 */           if (numPrintedLines == startingLineNumber + maxLinesPerPage) {
/* 311 */             return 0;
/*     */           }
/*     */         } 
/*     */         
/* 315 */         curLineString = curLineString.substring(breakPoint, curLineString.length());
/*     */       } 
/*     */ 
/*     */       
/* 319 */       currentDocLineNumber++;
/*     */       
/* 321 */       numPrintedLines++;
/* 322 */       if (numPrintedLines > startingLineNumber) {
/* 323 */         g.drawString(curLineString, xOffset, y);
/* 324 */         y += fontHeight;
/* 325 */         if (numPrintedLines == startingLineNumber + maxLinesPerPage) {
/* 326 */           return 0;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 334 */     if (numPrintedLines > startingLineNumber) {
/* 335 */       return 0;
/*     */     }
/* 337 */     return 1;
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
/*     */   public static int printDocumentWordWrap(Graphics g, JTextComponent textComponent, Font font, int pageIndex, PageFormat pageFormat, int tabSize) {
/* 363 */     g.setColor(Color.BLACK);
/* 364 */     g.setFont((font != null) ? font : textComponent.getFont());
/*     */ 
/*     */     
/* 367 */     tabSizeInSpaces = tabSize;
/* 368 */     fm = g.getFontMetrics();
/* 369 */     int fontHeight = fm.getHeight();
/*     */     
/* 371 */     int lineLengthInPixels = (int)pageFormat.getImageableWidth();
/* 372 */     int maxLinesPerPage = (int)pageFormat.getImageableHeight() / fontHeight;
/*     */     
/* 374 */     int startingLineNumber = maxLinesPerPage * pageIndex;
/*     */ 
/*     */     
/* 377 */     RPrintTabExpander tabExpander = new RPrintTabExpander();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 382 */     xOffset = (int)pageFormat.getImageableX();
/* 383 */     int y = (int)pageFormat.getImageableY() + fm.getAscent() + 1;
/*     */ 
/*     */ 
/*     */     
/* 387 */     int numPrintedLines = 0;
/*     */ 
/*     */     
/* 390 */     Document doc = textComponent.getDocument();
/* 391 */     rootElement = doc.getDefaultRootElement();
/* 392 */     numDocLines = rootElement.getElementCount();
/* 393 */     currentDocLineNumber = 0;
/* 394 */     int startingOffset = 0;
/* 395 */     while (currentDocLineNumber < numDocLines) {
/*     */       
/* 397 */       Segment currentLineSeg = new Segment();
/*     */ 
/*     */       
/* 400 */       Element currentLine = rootElement.getElement(currentDocLineNumber);
/* 401 */       int currentLineStart = currentLine.getStartOffset();
/* 402 */       int currentLineEnd = currentLine.getEndOffset();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 408 */         doc.getText(currentLineStart + startingOffset, currentLineEnd - currentLineStart + startingOffset, currentLineSeg);
/*     */       }
/* 410 */       catch (BadLocationException ble) {
/* 411 */         System.err.println("BadLocationException in print (where there shouldn't be one!): " + ble);
/* 412 */         return 1;
/*     */       } 
/*     */ 
/*     */       
/* 416 */       currentLineSeg = removeEndingWhitespace(currentLineSeg);
/*     */ 
/*     */       
/* 419 */       int currentLineLengthInPixels = Utilities.getTabbedTextWidth(currentLineSeg, fm, 0, tabExpander, 0);
/*     */ 
/*     */ 
/*     */       
/* 423 */       if (currentLineLengthInPixels <= lineLengthInPixels) {
/* 424 */         currentDocLineNumber++;
/* 425 */         startingOffset = 0;
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 432 */         int currentPos = -1;
/* 433 */         while (currentLineLengthInPixels > lineLengthInPixels) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 438 */           currentLineSeg = removeEndingWhitespace(currentLineSeg);
/*     */ 
/*     */           
/* 441 */           currentPos = -1;
/* 442 */           String currentLineString = currentLineSeg.toString();
/* 443 */           for (char breakChar : BREAK_CHARS) {
/*     */             
/* 445 */             int pos = currentLineString.lastIndexOf(breakChar) + 1;
/*     */ 
/*     */             
/* 448 */             if (pos > 0 && pos > currentPos && pos != currentLineString.length()) {
/* 449 */               currentPos = pos;
/*     */             }
/*     */           } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 457 */           if (currentPos == -1) {
/*     */ 
/*     */ 
/*     */             
/* 461 */             currentPos = 0;
/*     */             while (true) {
/* 463 */               currentPos++;
/*     */               try {
/* 465 */                 doc.getText(currentLineStart + startingOffset, currentPos, currentLineSeg);
/* 466 */               } catch (BadLocationException ble) {
/* 467 */                 System.err.println(ble);
/* 468 */                 return 1;
/*     */               } 
/*     */               
/* 471 */               currentLineLengthInPixels = Utilities.getTabbedTextWidth(currentLineSeg, fm, 0, tabExpander, 0);
/* 472 */               if (currentLineLengthInPixels > lineLengthInPixels) {
/* 473 */                 currentPos--; break;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */           try {
/* 478 */             doc.getText(currentLineStart + startingOffset, currentPos, currentLineSeg);
/* 479 */           } catch (BadLocationException ble) {
/* 480 */             System.err.println("BadLocationException in print (a):");
/* 481 */             System.err.println("==> currentLineStart: " + currentLineStart + "; startingOffset: " + startingOffset + "; currentPos: " + currentPos);
/*     */             
/* 483 */             System.err.println("==> Range: " + (currentLineStart + startingOffset) + " - " + (currentLineStart + startingOffset + currentPos));
/*     */             
/* 485 */             ble.printStackTrace();
/* 486 */             return 1;
/*     */           } 
/*     */           
/* 489 */           currentLineLengthInPixels = Utilities.getTabbedTextWidth(currentLineSeg, fm, 0, tabExpander, 0);
/*     */         } 
/*     */         
/* 492 */         startingOffset += currentPos;
/*     */       } 
/*     */ 
/*     */       
/* 496 */       numPrintedLines++;
/* 497 */       if (numPrintedLines > startingLineNumber) {
/*     */         
/* 499 */         Utilities.drawTabbedText(currentLineSeg, xOffset, y, g, tabExpander, 0);
/* 500 */         y += fontHeight;
/* 501 */         if (numPrintedLines == startingLineNumber + maxLinesPerPage) {
/* 502 */           return 0;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 509 */     if (numPrintedLines > startingLineNumber) {
/* 510 */       return 0;
/*     */     }
/* 512 */     return 1;
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
/*     */   private static Segment removeEndingWhitespace(Segment segment) {
/* 524 */     int toTrim = 0;
/* 525 */     char currentChar = segment.setIndex(segment.getEndIndex() - 1);
/* 526 */     while ((currentChar == ' ' || currentChar == '\t') && currentChar != Character.MAX_VALUE) {
/* 527 */       toTrim++;
/* 528 */       currentChar = segment.previous();
/*     */     } 
/* 530 */     String stringVal = segment.toString();
/* 531 */     String newStringVal = stringVal.substring(0, stringVal.length() - toTrim);
/* 532 */     return new Segment(newStringVal.toCharArray(), 0, newStringVal.length());
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
/*     */   private static class RPrintTabExpander
/*     */     implements TabExpander
/*     */   {
/*     */     public float nextTabStop(float x, int tabOffset) {
/* 547 */       if (RPrintUtilities.tabSizeInSpaces == 0) {
/* 548 */         return x;
/*     */       }
/* 550 */       int tabSizeInPixels = RPrintUtilities.tabSizeInSpaces * RPrintUtilities.fm.charWidth(' ');
/* 551 */       int ntabs = ((int)x - RPrintUtilities.xOffset) / tabSizeInPixels;
/* 552 */       return RPrintUtilities.xOffset + (ntabs + 1.0F) * tabSizeInPixels;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\print\RPrintUtilities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */