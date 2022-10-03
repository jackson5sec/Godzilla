/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Rectangle;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TokenImpl
/*     */   implements Token
/*     */ {
/*     */   public char[] text;
/*     */   public int textOffset;
/*     */   public int textCount;
/*     */   private int offset;
/*     */   private int type;
/*     */   private boolean hyperlink;
/*     */   private Token nextToken;
/*     */   private int languageIndex;
/*     */   
/*     */   public TokenImpl() {
/*  77 */     this.text = null;
/*  78 */     this.textOffset = -1;
/*  79 */     this.textCount = -1;
/*  80 */     setType(0);
/*  81 */     setOffset(-1);
/*  82 */     this.hyperlink = false;
/*  83 */     this.nextToken = null;
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
/*     */   public TokenImpl(Segment line, int beg, int end, int startOffset, int type, int languageIndex) {
/* 100 */     this(line.array, beg, end, startOffset, type, languageIndex);
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
/*     */   public TokenImpl(char[] line, int beg, int end, int startOffset, int type, int languageIndex) {
/* 117 */     this();
/* 118 */     set(line, beg, end, startOffset, type);
/* 119 */     setLanguageIndex(languageIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenImpl(Token t2) {
/* 129 */     this();
/* 130 */     copyFrom(t2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringBuilder appendHTMLRepresentation(StringBuilder sb, RSyntaxTextArea textArea, boolean fontFamily) {
/* 138 */     return appendHTMLRepresentation(sb, textArea, fontFamily, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringBuilder appendHTMLRepresentation(StringBuilder sb, RSyntaxTextArea textArea, boolean fontFamily, boolean tabsToSpaces) {
/* 147 */     SyntaxScheme colorScheme = textArea.getSyntaxScheme();
/* 148 */     Style scheme = colorScheme.getStyle(getType());
/* 149 */     Font font = textArea.getFontForTokenType(getType());
/*     */     
/* 151 */     if (font.isBold()) {
/* 152 */       sb.append("<b>");
/*     */     }
/* 154 */     if (font.isItalic()) {
/* 155 */       sb.append("<em>");
/*     */     }
/* 157 */     if (scheme.underline || isHyperlink()) {
/* 158 */       sb.append("<u>");
/*     */     }
/*     */     
/* 161 */     boolean needsFontTag = (fontFamily || !isWhitespace());
/* 162 */     if (needsFontTag) {
/* 163 */       sb.append("<font");
/* 164 */       if (fontFamily) {
/* 165 */         sb.append(" face=\"").append(font.getFamily()).append('"');
/*     */       }
/* 167 */       if (!isWhitespace()) {
/* 168 */         sb.append(" color=\"").append(
/* 169 */             getHTMLFormatForColor(scheme.foreground)).append('"');
/*     */       }
/* 171 */       sb.append('>');
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 176 */     appendHtmlLexeme(textArea, sb, tabsToSpaces);
/*     */     
/* 178 */     if (needsFontTag) {
/* 179 */       sb.append("</font>");
/*     */     }
/* 181 */     if (scheme.underline || isHyperlink()) {
/* 182 */       sb.append("</u>");
/*     */     }
/* 184 */     if (font.isItalic()) {
/* 185 */       sb.append("</em>");
/*     */     }
/* 187 */     if (font.isBold()) {
/* 188 */       sb.append("</b>");
/*     */     }
/*     */     
/* 191 */     return sb;
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
/*     */   private StringBuilder appendHtmlLexeme(RSyntaxTextArea textArea, StringBuilder sb, boolean tabsToSpaces) {
/* 209 */     boolean lastWasSpace = false;
/* 210 */     int i = this.textOffset;
/* 211 */     int lastI = i;
/* 212 */     String tabStr = null;
/*     */     
/* 214 */     while (i < this.textOffset + this.textCount) {
/* 215 */       char ch = this.text[i];
/* 216 */       switch (ch) {
/*     */         case ' ':
/* 218 */           sb.append(this.text, lastI, i - lastI);
/* 219 */           lastI = i + 1;
/* 220 */           sb.append(lastWasSpace ? "&nbsp;" : " ");
/* 221 */           lastWasSpace = true;
/*     */           break;
/*     */         case '\t':
/* 224 */           sb.append(this.text, lastI, i - lastI);
/* 225 */           lastI = i + 1;
/* 226 */           if (tabsToSpaces && tabStr == null) {
/* 227 */             StringBuilder stringBuilder = new StringBuilder();
/* 228 */             for (int j = 0; j < textArea.getTabSize(); j++) {
/* 229 */               stringBuilder.append("&nbsp;");
/*     */             }
/* 231 */             tabStr = stringBuilder.toString();
/*     */           } 
/* 233 */           sb.append(tabsToSpaces ? tabStr : "&#09;");
/* 234 */           lastWasSpace = false;
/*     */           break;
/*     */         case '&':
/* 237 */           sb.append(this.text, lastI, i - lastI);
/* 238 */           lastI = i + 1;
/* 239 */           sb.append("&amp;");
/* 240 */           lastWasSpace = false;
/*     */           break;
/*     */         case '<':
/* 243 */           sb.append(this.text, lastI, i - lastI);
/* 244 */           lastI = i + 1;
/* 245 */           sb.append("&lt;");
/* 246 */           lastWasSpace = false;
/*     */           break;
/*     */         case '>':
/* 249 */           sb.append(this.text, lastI, i - lastI);
/* 250 */           lastI = i + 1;
/* 251 */           sb.append("&gt;");
/* 252 */           lastWasSpace = false;
/*     */           break;
/*     */         case '\'':
/* 255 */           sb.append(this.text, lastI, i - lastI);
/* 256 */           lastI = i + 1;
/* 257 */           sb.append("&#39;");
/* 258 */           lastWasSpace = false;
/*     */           break;
/*     */         case '"':
/* 261 */           sb.append(this.text, lastI, i - lastI);
/* 262 */           lastI = i + 1;
/* 263 */           sb.append("&#34;");
/* 264 */           lastWasSpace = false;
/*     */           break;
/*     */         case '/':
/* 267 */           sb.append(this.text, lastI, i - lastI);
/* 268 */           lastI = i + 1;
/* 269 */           sb.append("&#47;");
/* 270 */           lastWasSpace = false;
/*     */           break;
/*     */         default:
/* 273 */           lastWasSpace = false;
/*     */           break;
/*     */       } 
/* 276 */       i++;
/*     */     } 
/* 278 */     if (lastI < this.textOffset + this.textCount) {
/* 279 */       sb.append(this.text, lastI, this.textOffset + this.textCount - lastI);
/*     */     }
/* 281 */     return sb;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public char charAt(int index) {
/* 287 */     return this.text[this.textOffset + index];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsPosition(int pos) {
/* 293 */     return (pos >= getOffset() && pos < getOffset() + this.textCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void copyFrom(Token t2) {
/* 304 */     this.text = t2.getTextArray();
/* 305 */     this.textOffset = t2.getTextOffset();
/* 306 */     this.textCount = t2.length();
/* 307 */     setOffset(t2.getOffset());
/* 308 */     setType(t2.getType());
/* 309 */     this.hyperlink = t2.isHyperlink();
/* 310 */     this.languageIndex = t2.getLanguageIndex();
/* 311 */     this.nextToken = t2.getNextToken();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int documentToToken(int pos) {
/* 317 */     return pos + this.textOffset - getOffset();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean endsWith(char[] ch) {
/* 323 */     if (ch == null || ch.length > this.textCount) {
/* 324 */       return false;
/*     */     }
/* 326 */     int start = this.textOffset + this.textCount - ch.length;
/* 327 */     for (int i = 0; i < ch.length; i++) {
/* 328 */       if (this.text[start + i] != ch[i]) {
/* 329 */         return false;
/*     */       }
/*     */     } 
/* 332 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 339 */     if (obj == this) {
/* 340 */       return true;
/*     */     }
/* 342 */     if (!(obj instanceof Token)) {
/* 343 */       return false;
/*     */     }
/*     */     
/* 346 */     Token t2 = (Token)obj;
/* 347 */     return (this.offset == t2.getOffset() && this.type == t2
/* 348 */       .getType() && this.languageIndex == t2
/* 349 */       .getLanguageIndex() && this.hyperlink == t2
/* 350 */       .isHyperlink() && ((
/* 351 */       getLexeme() == null && t2.getLexeme() == null) || (
/* 352 */       getLexeme() != null && getLexeme().equals(t2.getLexeme()))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getEndOffset() {
/* 359 */     return this.offset + this.textCount;
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
/*     */   private static String getHTMLFormatForColor(Color color) {
/* 372 */     if (color == null) {
/* 373 */       return "black";
/*     */     }
/* 375 */     String hexRed = Integer.toHexString(color.getRed());
/* 376 */     if (hexRed.length() == 1) {
/* 377 */       hexRed = "0" + hexRed;
/*     */     }
/* 379 */     String hexGreen = Integer.toHexString(color.getGreen());
/* 380 */     if (hexGreen.length() == 1) {
/* 381 */       hexGreen = "0" + hexGreen;
/*     */     }
/* 383 */     String hexBlue = Integer.toHexString(color.getBlue());
/* 384 */     if (hexBlue.length() == 1) {
/* 385 */       hexBlue = "0" + hexBlue;
/*     */     }
/* 387 */     return "#" + hexRed + hexGreen + hexBlue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHTMLRepresentation(RSyntaxTextArea textArea) {
/* 393 */     StringBuilder buf = new StringBuilder();
/* 394 */     appendHTMLRepresentation(buf, textArea, true);
/* 395 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLanguageIndex() {
/* 401 */     return this.languageIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Token getLastNonCommentNonWhitespaceToken() {
/* 408 */     Token last = null;
/*     */     
/* 410 */     for (Token t = this; t != null && t.isPaintable(); t = t.getNextToken()) {
/* 411 */       switch (t.getType()) {
/*     */         case 1:
/*     */         case 2:
/*     */         case 3:
/*     */         case 4:
/*     */         case 5:
/*     */         case 21:
/*     */           break;
/*     */         default:
/* 420 */           last = t;
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 425 */     return last;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Token getLastPaintableToken() {
/* 432 */     Token t = this;
/* 433 */     while (t.isPaintable()) {
/* 434 */       Token next = t.getNextToken();
/* 435 */       if (next == null || !next.isPaintable()) {
/* 436 */         return t;
/*     */       }
/* 438 */       t = next;
/*     */     } 
/* 440 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLexeme() {
/* 446 */     if (this.text == null) {
/* 447 */       return null;
/*     */     }
/* 449 */     return isPaintable() ? new String(this.text, this.textOffset, this.textCount) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getListOffset(RSyntaxTextArea textArea, TabExpander e, float x0, float x) {
/* 458 */     if (x0 >= x) {
/* 459 */       return getOffset();
/*     */     }
/*     */     
/* 462 */     float currX = x0;
/* 463 */     float nextX = x0;
/* 464 */     float stableX = x0;
/* 465 */     TokenImpl token = this;
/* 466 */     int last = getOffset();
/* 467 */     FontMetrics fm = null;
/*     */     
/* 469 */     while (token != null && token.isPaintable()) {
/*     */       
/* 471 */       fm = textArea.getFontMetricsForTokenType(token.getType());
/* 472 */       char[] text = token.text;
/* 473 */       int start = token.textOffset;
/* 474 */       int end = start + token.textCount;
/*     */       
/* 476 */       for (int i = start; i < end; i++) {
/* 477 */         currX = nextX;
/* 478 */         if (text[i] == '\t') {
/* 479 */           nextX = e.nextTabStop(nextX, 0);
/* 480 */           stableX = nextX;
/* 481 */           start = i + 1;
/*     */         } else {
/*     */           
/* 484 */           nextX = stableX + fm.charsWidth(text, start, i - start + 1);
/*     */         } 
/* 486 */         if (x >= currX && x < nextX) {
/* 487 */           if (x - currX < nextX - x) {
/* 488 */             return last + i - token.textOffset;
/*     */           }
/* 490 */           return last + i + 1 - token.textOffset;
/*     */         } 
/*     */       } 
/*     */       
/* 494 */       stableX = nextX;
/* 495 */       last += token.textCount;
/* 496 */       token = (TokenImpl)token.getNextToken();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 501 */     return last;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Token getNextToken() {
/* 508 */     return this.nextToken;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOffset() {
/* 514 */     return this.offset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOffsetBeforeX(RSyntaxTextArea textArea, TabExpander e, float startX, float endBeforeX) {
/* 522 */     FontMetrics fm = textArea.getFontMetricsForTokenType(getType());
/* 523 */     int i = this.textOffset;
/* 524 */     int stop = i + this.textCount;
/* 525 */     float x = startX;
/*     */     
/* 527 */     while (i < stop) {
/* 528 */       if (this.text[i] == '\t') {
/* 529 */         x = e.nextTabStop(x, 0);
/*     */       } else {
/*     */         
/* 532 */         x += fm.charWidth(this.text[i]);
/*     */       } 
/* 534 */       if (x > endBeforeX) {
/*     */ 
/*     */ 
/*     */         
/* 538 */         int intoToken = Math.max(i - this.textOffset, 1);
/* 539 */         return getOffset() + intoToken;
/*     */       } 
/* 541 */       i++;
/*     */     } 
/*     */ 
/*     */     
/* 545 */     return getOffset() + this.textCount - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char[] getTextArray() {
/* 552 */     return this.text;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTextOffset() {
/* 558 */     return this.textOffset;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getType() {
/* 564 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getWidth(RSyntaxTextArea textArea, TabExpander e, float x0) {
/* 570 */     return getWidthUpTo(this.textCount, textArea, e, x0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getWidthUpTo(int numChars, RSyntaxTextArea textArea, TabExpander e, float x0) {
/* 577 */     float width = x0;
/* 578 */     FontMetrics fm = textArea.getFontMetricsForTokenType(getType());
/* 579 */     if (fm != null) {
/*     */       
/* 581 */       int currentStart = this.textOffset;
/* 582 */       int endBefore = this.textOffset + numChars;
/* 583 */       for (int i = currentStart; i < endBefore; i++) {
/* 584 */         if (this.text[i] == '\t') {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 589 */           int j = i - currentStart;
/* 590 */           if (j > 0) {
/* 591 */             width += fm.charsWidth(this.text, currentStart, j);
/*     */           }
/* 593 */           currentStart = i + 1;
/* 594 */           width = e.nextTabStop(width, 0);
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 600 */       int w = endBefore - currentStart;
/* 601 */       width += fm.charsWidth(this.text, currentStart, w);
/*     */     } 
/* 603 */     return width - x0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 609 */     return this.offset + ((getLexeme() == null) ? 0 : getLexeme().hashCode());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean is(char[] lexeme) {
/* 615 */     if (this.textCount == lexeme.length) {
/* 616 */       for (int i = 0; i < this.textCount; i++) {
/* 617 */         if (this.text[this.textOffset + i] != lexeme[i]) {
/* 618 */           return false;
/*     */         }
/*     */       } 
/* 621 */       return true;
/*     */     } 
/* 623 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean is(int type, char[] lexeme) {
/* 629 */     if (getType() == type && this.textCount == lexeme.length) {
/* 630 */       for (int i = 0; i < this.textCount; i++) {
/* 631 */         if (this.text[this.textOffset + i] != lexeme[i]) {
/* 632 */           return false;
/*     */         }
/*     */       } 
/* 635 */       return true;
/*     */     } 
/* 637 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean is(int type, String lexeme) {
/* 643 */     return (getType() == type && this.textCount == lexeme.length() && lexeme
/* 644 */       .equals(getLexeme()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isComment() {
/* 650 */     return (getType() >= 1 && getType() <= 5);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCommentOrWhitespace() {
/* 656 */     return (isComment() || isWhitespace());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isHyperlink() {
/* 662 */     return this.hyperlink;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIdentifier() {
/* 668 */     return (getType() == 20);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLeftCurly() {
/* 674 */     return (getType() == 22 && isSingleChar('{'));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRightCurly() {
/* 680 */     return (getType() == 22 && isSingleChar('}'));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPaintable() {
/* 686 */     return (getType() > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSingleChar(char ch) {
/* 692 */     return (this.textCount == 1 && this.text[this.textOffset] == ch);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSingleChar(int type, char ch) {
/* 698 */     return (getType() == type && isSingleChar(ch));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWhitespace() {
/* 704 */     return (getType() == 21);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 710 */     return this.textCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Rectangle listOffsetToView(RSyntaxTextArea textArea, TabExpander e, int pos, int x0, Rectangle rect) {
/* 718 */     int stableX = x0;
/* 719 */     TokenImpl token = this;
/* 720 */     FontMetrics fm = null;
/* 721 */     Segment s = new Segment();
/*     */     
/* 723 */     while (token != null && token.isPaintable()) {
/*     */       
/* 725 */       fm = textArea.getFontMetricsForTokenType(token.getType());
/* 726 */       if (fm == null) {
/* 727 */         return rect;
/*     */       }
/* 729 */       char[] text = token.text;
/* 730 */       int start = token.textOffset;
/* 731 */       int end = start + token.textCount;
/*     */ 
/*     */ 
/*     */       
/* 735 */       if (token.containsPosition(pos)) {
/*     */         
/* 737 */         s.array = token.text;
/* 738 */         s.offset = token.textOffset;
/* 739 */         s.count = pos - token.getOffset();
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 744 */         int w = Utilities.getTabbedTextWidth(s, fm, stableX, e, token
/* 745 */             .getOffset());
/* 746 */         rect.x = stableX + w;
/* 747 */         end = token.documentToToken(pos);
/*     */         
/* 749 */         if (text[end] == '\t') {
/* 750 */           rect.width = fm.charWidth(' ');
/*     */         } else {
/*     */           
/* 753 */           rect.width = fm.charWidth(text[end]);
/*     */         } 
/*     */         
/* 756 */         return rect;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 763 */       s.array = token.text;
/* 764 */       s.offset = token.textOffset;
/* 765 */       s.count = token.textCount;
/* 766 */       stableX += Utilities.getTabbedTextWidth(s, fm, stableX, e, token
/* 767 */           .getOffset());
/*     */ 
/*     */       
/* 770 */       token = (TokenImpl)token.getNextToken();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 778 */     rect.x = stableX;
/* 779 */     rect.width = 1;
/* 780 */     return rect;
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
/*     */   public void makeStartAt(int pos) {
/* 801 */     if (pos < getOffset() || pos >= getOffset() + this.textCount) {
/* 802 */       throw new IllegalArgumentException("pos " + pos + " is not in range " + 
/* 803 */           getOffset() + "-" + (getOffset() + this.textCount - 1));
/*     */     }
/* 805 */     int shift = pos - getOffset();
/* 806 */     setOffset(pos);
/* 807 */     this.textOffset += shift;
/* 808 */     this.textCount -= shift;
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
/*     */   public void moveOffset(int amt) {
/* 826 */     if (amt < 0 || amt > this.textCount) {
/* 827 */       throw new IllegalArgumentException("amt " + amt + " is not in range 0-" + this.textCount);
/*     */     }
/*     */     
/* 830 */     setOffset(getOffset() + amt);
/* 831 */     this.textOffset += amt;
/* 832 */     this.textCount -= amt;
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
/*     */   public void set(char[] line, int beg, int end, int offset, int type) {
/* 848 */     this.text = line;
/* 849 */     this.textOffset = beg;
/* 850 */     this.textCount = end - beg + 1;
/* 851 */     setType(type);
/* 852 */     setOffset(offset);
/* 853 */     this.nextToken = null;
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
/*     */   public void setHyperlink(boolean hyperlink) {
/* 865 */     this.hyperlink = hyperlink;
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
/*     */   public void setLanguageIndex(int languageIndex) {
/* 884 */     this.languageIndex = languageIndex;
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
/*     */   public void setNextToken(Token nextToken) {
/* 896 */     this.nextToken = nextToken;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOffset(int offset) {
/* 907 */     this.offset = offset;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(int type) {
/* 913 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean startsWith(char[] chars) {
/* 919 */     if (chars.length <= this.textCount) {
/* 920 */       for (int i = 0; i < chars.length; i++) {
/* 921 */         if (this.text[this.textOffset + i] != chars[i]) {
/* 922 */           return false;
/*     */         }
/*     */       } 
/* 925 */       return true;
/*     */     } 
/* 927 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int tokenToDocument(int pos) {
/* 933 */     return pos + getOffset() - this.textOffset;
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
/*     */   public String toString() {
/* 945 */     return "[Token: " + (
/* 946 */       (getType() == 0) ? "<null token>" : ("text: '" + ((this.text == null) ? "<null>" : (
/*     */       
/* 948 */       getLexeme() + "'; offset: " + 
/* 949 */       getOffset() + "; type: " + getType() + "; isPaintable: " + 
/* 950 */       isPaintable() + "; nextToken==null: " + ((this.nextToken == null) ? 1 : 0))))) + "]";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\TokenImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */