/*      */ package org.fife.ui.rsyntaxtextarea;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Container;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.Toolkit;
/*      */ import java.util.Map;
/*      */ import java.util.regex.Pattern;
/*      */ import java.util.regex.PatternSyntaxException;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.JTextArea;
/*      */ import javax.swing.SwingConstants;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import javax.swing.text.Caret;
/*      */ import javax.swing.text.Document;
/*      */ import javax.swing.text.Element;
/*      */ import javax.swing.text.Position;
/*      */ import javax.swing.text.Segment;
/*      */ import javax.swing.text.TabExpander;
/*      */ import javax.swing.text.View;
/*      */ import org.fife.ui.rsyntaxtextarea.folding.FoldManager;
/*      */ import org.fife.ui.rtextarea.Gutter;
/*      */ import org.fife.ui.rtextarea.RTextArea;
/*      */ import org.fife.ui.rtextarea.RTextScrollPane;
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
/*      */ public final class RSyntaxUtilities
/*      */   implements SwingConstants
/*      */ {
/*      */   public static final int OS_WINDOWS = 1;
/*      */   public static final int OS_MAC_OSX = 2;
/*      */   public static final int OS_LINUX = 4;
/*      */   public static final int OS_OTHER = 8;
/*   78 */   private static final Color LIGHT_HYPERLINK_FG = new Color(14221311);
/*      */   
/*   80 */   private static final int OS = getOSImpl();
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int LETTER_MASK = 2;
/*      */ 
/*      */   
/*      */   private static final int HEX_CHARACTER_MASK = 16;
/*      */ 
/*      */   
/*      */   private static final int LETTER_OR_DIGIT_MASK = 32;
/*      */ 
/*      */   
/*      */   private static final int BRACKET_MASK = 64;
/*      */ 
/*      */   
/*      */   private static final int JAVA_OPERATOR_MASK = 128;
/*      */ 
/*      */   
/*   99 */   private static final int[] DATA_TABLE = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 128, 0, 0, 0, 128, 128, 0, 64, 64, 128, 128, 0, 128, 0, 128, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 128, 0, 128, 128, 128, 128, 0, 58, 58, 58, 58, 58, 58, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 64, 0, 64, 128, 0, 0, 50, 50, 50, 50, 50, 50, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 34, 64, 128, 64, 128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
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
/*  121 */   private static Segment charSegment = new Segment();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  126 */   private static final TokenImpl TEMP_TOKEN = new TokenImpl();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  131 */   private static final char[] JS_KEYWORD_RETURN = new char[] { 'r', 'e', 't', 'u', 'r', 'n' };
/*  132 */   private static final char[] JS_AND = new char[] { '&', '&' };
/*  133 */   private static final char[] JS_OR = new char[] { '|', '|' };
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
/*      */   private static final String BRACKETS = "{([})]";
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
/*      */   public static String escapeForHtml(String s, String newlineReplacement, boolean inPreBlock) {
/*  166 */     if (s == null) {
/*  167 */       return null;
/*      */     }
/*  169 */     if (newlineReplacement == null) {
/*  170 */       newlineReplacement = "";
/*      */     }
/*  172 */     String tabString = "   ";
/*  173 */     boolean lastWasSpace = false;
/*      */     
/*  175 */     StringBuilder sb = new StringBuilder();
/*      */     
/*  177 */     for (int i = 0; i < s.length(); i++) {
/*  178 */       char ch = s.charAt(i);
/*  179 */       switch (ch) {
/*      */         case ' ':
/*  181 */           if (inPreBlock || !lastWasSpace) {
/*  182 */             sb.append(' ');
/*      */           } else {
/*      */             
/*  185 */             sb.append("&nbsp;");
/*      */           } 
/*  187 */           lastWasSpace = true;
/*      */           break;
/*      */         case '\n':
/*  190 */           sb.append(newlineReplacement);
/*  191 */           lastWasSpace = false;
/*      */           break;
/*      */         case '&':
/*  194 */           sb.append("&amp;");
/*  195 */           lastWasSpace = false;
/*      */           break;
/*      */         case '\t':
/*  198 */           sb.append("   ");
/*  199 */           lastWasSpace = false;
/*      */           break;
/*      */         case '<':
/*  202 */           sb.append("&lt;");
/*  203 */           lastWasSpace = false;
/*      */           break;
/*      */         case '>':
/*  206 */           sb.append("&gt;");
/*  207 */           lastWasSpace = false;
/*      */           break;
/*      */         case '\'':
/*  210 */           sb.append("&#39;");
/*  211 */           lastWasSpace = false;
/*      */           break;
/*      */         case '"':
/*  214 */           sb.append("&#34;");
/*  215 */           lastWasSpace = false;
/*      */           break;
/*      */         case '/':
/*  218 */           sb.append("&#47;");
/*  219 */           lastWasSpace = false;
/*      */           break;
/*      */         default:
/*  222 */           sb.append(ch);
/*  223 */           lastWasSpace = false;
/*      */           break;
/*      */       } 
/*      */     
/*      */     } 
/*  228 */     return sb.toString();
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
/*      */   public static Map<?, ?> getDesktopAntiAliasHints() {
/*  241 */     return (Map<?, ?>)Toolkit.getDefaultToolkit()
/*  242 */       .getDesktopProperty("awt.font.desktophints");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Color getFoldedLineBottomColor(RSyntaxTextArea textArea) {
/*  253 */     Color color = Color.GRAY;
/*  254 */     Gutter gutter = getGutter(textArea);
/*  255 */     if (gutter != null) {
/*  256 */       color = gutter.getFoldIndicatorForeground();
/*      */     }
/*  258 */     return color;
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
/*      */   public static Gutter getGutter(RTextArea textArea) {
/*  272 */     Gutter gutter = null;
/*  273 */     Container parent = textArea.getParent();
/*  274 */     if (parent instanceof javax.swing.JViewport) {
/*  275 */       parent = parent.getParent();
/*  276 */       if (parent instanceof RTextScrollPane) {
/*  277 */         RTextScrollPane sp = (RTextScrollPane)parent;
/*  278 */         gutter = sp.getGutter();
/*      */       } 
/*      */     } 
/*  281 */     return gutter;
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
/*      */   public static Color getHyperlinkForeground() {
/*  298 */     Color fg = UIManager.getColor("Label.foreground");
/*  299 */     if (fg == null) {
/*  300 */       fg = (new JLabel()).getForeground();
/*      */     }
/*      */     
/*  303 */     return isLightForeground(fg) ? LIGHT_HYPERLINK_FG : Color.blue;
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
/*      */   public static String getLeadingWhitespace(String text) {
/*  316 */     int count = 0;
/*  317 */     int len = text.length();
/*  318 */     while (count < len && isWhitespace(text.charAt(count))) {
/*  319 */       count++;
/*      */     }
/*  321 */     return text.substring(0, count);
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
/*      */   public static String getLeadingWhitespace(Document doc, int offs) throws BadLocationException {
/*  337 */     Element root = doc.getDefaultRootElement();
/*  338 */     int line = root.getElementIndex(offs);
/*  339 */     Element elem = root.getElement(line);
/*  340 */     int startOffs = elem.getStartOffset();
/*  341 */     int endOffs = elem.getEndOffset() - 1;
/*  342 */     String text = doc.getText(startOffs, endOffs - startOffs);
/*  343 */     return getLeadingWhitespace(text);
/*      */   }
/*      */ 
/*      */   
/*      */   private static Element getLineElem(Document doc, int offs) {
/*  348 */     Element root = doc.getDefaultRootElement();
/*  349 */     int line = root.getElementIndex(offs);
/*  350 */     Element elem = root.getElement(line);
/*  351 */     if (offs >= elem.getStartOffset() && offs < elem.getEndOffset()) {
/*  352 */       return elem;
/*      */     }
/*  354 */     return null;
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
/*      */   public static Rectangle getLineWidthUpTo(RSyntaxTextArea textArea, Segment s, int p0, int p1, TabExpander e, Rectangle rect, int x0) throws BadLocationException {
/*  390 */     RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
/*      */ 
/*      */     
/*  393 */     if (p0 < 0) {
/*  394 */       throw new BadLocationException("Invalid document position", p0);
/*      */     }
/*  396 */     if (p1 > doc.getLength()) {
/*  397 */       throw new BadLocationException("Invalid document position", p1);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  402 */     Element map = doc.getDefaultRootElement();
/*  403 */     int lineNum = map.getElementIndex(p0);
/*      */ 
/*      */ 
/*      */     
/*  407 */     if (Math.abs(lineNum - map.getElementIndex(p1)) > 1) {
/*  408 */       throw new IllegalArgumentException("p0 and p1 are not on the same line (" + p0 + ", " + p1 + ").");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  413 */     Token t = doc.getTokenListForLine(lineNum);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  418 */     TokenUtils.TokenSubList subList = TokenUtils.getSubTokenList(t, p0, e, textArea, 0.0F, TEMP_TOKEN);
/*      */     
/*  420 */     t = subList.tokenList;
/*      */     
/*  422 */     rect = t.listOffsetToView(textArea, e, p1, x0, rect);
/*  423 */     return rect;
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
/*      */   public static Point getMatchingBracketPosition(RSyntaxTextArea textArea, Point input) {
/*  445 */     if (input == null) {
/*  446 */       input = new Point();
/*      */     }
/*  448 */     input.setLocation(-1, -1);
/*      */     
/*      */     try {
/*      */       char bracketMatch;
/*      */       boolean goForward;
/*  453 */       int caretPosition = textArea.getCaretPosition() - 1;
/*  454 */       RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
/*  455 */       char bracket = Character.MIN_VALUE;
/*      */ 
/*      */       
/*  458 */       if (caretPosition >= 0) {
/*  459 */         bracket = doc.charAt(caretPosition);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  464 */       int index = "{([})]".indexOf(bracket);
/*  465 */       if (index == -1 && caretPosition < doc.getLength() - 1) {
/*  466 */         bracket = doc.charAt(++caretPosition);
/*      */       }
/*      */ 
/*      */       
/*  470 */       if (index == -1) {
/*  471 */         index = "{([})]".indexOf(bracket);
/*  472 */         if (index == -1) {
/*  473 */           return input;
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  482 */       Element map = doc.getDefaultRootElement();
/*  483 */       int curLine = map.getElementIndex(caretPosition);
/*  484 */       Element line = map.getElement(curLine);
/*  485 */       int start = line.getStartOffset();
/*  486 */       int end = line.getEndOffset();
/*  487 */       Token token = doc.getTokenListForLine(curLine);
/*  488 */       token = getTokenAtOffset(token, caretPosition);
/*      */       
/*  490 */       if (token.getType() != 22) {
/*  491 */         return input;
/*      */       }
/*  493 */       int languageIndex = token.getLanguageIndex();
/*  494 */       if (index < 3) {
/*  495 */         goForward = true;
/*  496 */         bracketMatch = "{([})]".charAt(index + 3);
/*      */       } else {
/*      */         
/*  499 */         goForward = false;
/*  500 */         bracketMatch = "{([})]".charAt(index - 3);
/*      */       } 
/*      */       
/*  503 */       if (goForward) {
/*      */         
/*  505 */         int lastLine = map.getElementCount();
/*      */ 
/*      */ 
/*      */         
/*  509 */         start = caretPosition + 1;
/*  510 */         int i = 0;
/*  511 */         boolean bool = false;
/*      */ 
/*      */         
/*      */         while (true) {
/*  515 */           doc.getText(start, end - start, charSegment);
/*  516 */           int segOffset = charSegment.offset;
/*      */           
/*  518 */           for (int j = segOffset; j < segOffset + charSegment.count; j++) {
/*      */             
/*  520 */             char ch = charSegment.array[j];
/*      */             
/*  522 */             if (ch == bracket) {
/*  523 */               if (!bool) {
/*  524 */                 token = doc.getTokenListForLine(curLine);
/*  525 */                 bool = true;
/*      */               } 
/*  527 */               int offset = start + j - segOffset;
/*  528 */               token = getTokenAtOffset(token, offset);
/*  529 */               if (token.getType() == 22 && token
/*  530 */                 .getLanguageIndex() == languageIndex) {
/*  531 */                 i++;
/*      */               
/*      */               }
/*      */             }
/*  535 */             else if (ch == bracketMatch) {
/*  536 */               if (!bool) {
/*  537 */                 token = doc.getTokenListForLine(curLine);
/*  538 */                 bool = true;
/*      */               } 
/*  540 */               int offset = start + j - segOffset;
/*  541 */               token = getTokenAtOffset(token, offset);
/*  542 */               if (token.getType() == 22 && token
/*  543 */                 .getLanguageIndex() == languageIndex) {
/*  544 */                 if (i == 0) {
/*  545 */                   if (textArea.isCodeFoldingEnabled() && textArea
/*  546 */                     .getFoldManager().isLineHidden(curLine)) {
/*  547 */                     return input;
/*      */                   }
/*  549 */                   input.setLocation(caretPosition, offset);
/*  550 */                   return input;
/*      */                 } 
/*  552 */                 i--;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  560 */           if (++curLine == lastLine) {
/*  561 */             return input;
/*      */           }
/*      */ 
/*      */           
/*  565 */           bool = false;
/*  566 */           line = map.getElement(curLine);
/*  567 */           start = line.getStartOffset();
/*  568 */           end = line.getEndOffset();
/*      */         } 
/*      */       } 
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
/*  581 */       end = caretPosition;
/*  582 */       int numEmbedded = 0;
/*  583 */       boolean haveTokenList = false;
/*      */ 
/*      */ 
/*      */       
/*      */       while (true) {
/*  588 */         doc.getText(start, end - start, charSegment);
/*  589 */         int segOffset = charSegment.offset;
/*  590 */         int iStart = segOffset + charSegment.count - 1;
/*      */         
/*  592 */         for (int i = iStart; i >= segOffset; i--) {
/*      */           
/*  594 */           char ch = charSegment.array[i];
/*      */           
/*  596 */           if (ch == bracket) {
/*  597 */             if (!haveTokenList) {
/*  598 */               token = doc.getTokenListForLine(curLine);
/*  599 */               haveTokenList = true;
/*      */             } 
/*  601 */             int offset = start + i - segOffset;
/*  602 */             Token t2 = getTokenAtOffset(token, offset);
/*  603 */             if (t2.getType() == 22 && token
/*  604 */               .getLanguageIndex() == languageIndex) {
/*  605 */               numEmbedded++;
/*      */             
/*      */             }
/*      */           }
/*  609 */           else if (ch == bracketMatch) {
/*  610 */             if (!haveTokenList) {
/*  611 */               token = doc.getTokenListForLine(curLine);
/*  612 */               haveTokenList = true;
/*      */             } 
/*  614 */             int offset = start + i - segOffset;
/*  615 */             Token t2 = getTokenAtOffset(token, offset);
/*  616 */             if (t2.getType() == 22 && token
/*  617 */               .getLanguageIndex() == languageIndex) {
/*  618 */               if (numEmbedded == 0) {
/*  619 */                 input.setLocation(caretPosition, offset);
/*  620 */                 return input;
/*      */               } 
/*  622 */               numEmbedded--;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  630 */         if (--curLine == -1) {
/*  631 */           return input;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  636 */         haveTokenList = false;
/*  637 */         line = map.getElement(curLine);
/*  638 */         start = line.getStartOffset();
/*  639 */         end = line.getEndOffset();
/*      */       
/*      */       }
/*      */ 
/*      */     
/*      */     }
/*  645 */     catch (BadLocationException ble) {
/*      */       
/*  647 */       ble.printStackTrace();
/*      */ 
/*      */ 
/*      */       
/*  651 */       return input;
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
/*      */ 
/*      */ 
/*      */   
/*      */   public static Token getNextImportantToken(Token t, RSyntaxTextArea textArea, int line) {
/*  669 */     while (t != null && t.isPaintable() && t.isCommentOrWhitespace()) {
/*  670 */       t = t.getNextToken();
/*      */     }
/*  672 */     if ((t == null || !t.isPaintable()) && line < textArea.getLineCount() - 1) {
/*  673 */       t = textArea.getTokenListForLine(++line);
/*  674 */       return getNextImportantToken(t, textArea, line);
/*      */     } 
/*  676 */     return t;
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
/*      */   public static int getNextVisualPositionFrom(int pos, Position.Bias b, Shape a, int direction, Position.Bias[] biasRet, View view) throws BadLocationException {
/*      */     int endOffs;
/*  714 */     RSyntaxTextArea target = (RSyntaxTextArea)view.getContainer();
/*  715 */     biasRet[0] = Position.Bias.Forward;
/*      */ 
/*      */     
/*  718 */     switch (direction) {
/*      */       
/*      */       case 1:
/*      */       case 5:
/*  722 */         if (pos == -1) {
/*      */ 
/*      */           
/*  725 */           pos = (direction == 1) ? Math.max(0, view.getEndOffset() - 1) : view.getStartOffset();
/*      */         } else {
/*      */           Point mcp; int x;
/*  728 */           Caret c = (target != null) ? target.getCaret() : null;
/*      */ 
/*      */ 
/*      */           
/*  732 */           if (c != null) {
/*  733 */             mcp = c.getMagicCaretPosition();
/*      */           } else {
/*      */             
/*  736 */             mcp = null;
/*      */           } 
/*      */           
/*  739 */           if (mcp == null) {
/*  740 */             Rectangle loc = target.modelToView(pos);
/*  741 */             x = (loc == null) ? 0 : loc.x;
/*      */           } else {
/*      */             
/*  744 */             x = mcp.x;
/*      */           } 
/*  746 */           if (direction == 1) {
/*  747 */             pos = getPositionAbove(target, pos, x, (TabExpander)view);
/*      */           } else {
/*      */             
/*  750 */             pos = getPositionBelow(target, pos, x, (TabExpander)view);
/*      */           } 
/*      */         } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  804 */         return pos;case 7: endOffs = view.getEndOffset(); if (pos == -1) { pos = Math.max(0, endOffs - 1); } else { pos = Math.max(0, pos - 1); if (target.isCodeFoldingEnabled()) { int last = (pos == endOffs - 1) ? (target.getLineCount() - 1) : target.getLineOfOffset(pos + 1); int current = target.getLineOfOffset(pos); if (last != current) { FoldManager fm = target.getFoldManager(); if (fm.isLineHidden(current)) { while (--current > 0 && fm.isLineHidden(current)); pos = target.getLineEndOffset(current) - 1; }  }  }  }  return pos;case 3: if (pos == -1) { pos = view.getStartOffset(); } else { pos = Math.min(pos + 1, view.getDocument().getLength()); if (target.isCodeFoldingEnabled()) { int last = (pos == 0) ? 0 : target.getLineOfOffset(pos - 1); int current = target.getLineOfOffset(pos); if (last != current) { FoldManager fm = target.getFoldManager(); if (fm.isLineHidden(current)) { int lineCount = target.getLineCount(); while (++current < lineCount && fm.isLineHidden(current)); pos = (current == lineCount) ? (target.getLineEndOffset(last) - 1) : target.getLineStartOffset(current); }  }  }  }  return pos;
/*      */     } 
/*      */     throw new IllegalArgumentException("Bad direction: " + direction);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getOS() {
/*  817 */     return OS;
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
/*      */   private static int getOSImpl() {
/*  829 */     int os = 8;
/*  830 */     String osName = System.getProperty("os.name");
/*  831 */     if (osName != null) {
/*  832 */       osName = osName.toLowerCase();
/*  833 */       if (osName.contains("windows")) {
/*  834 */         os = 1;
/*      */       }
/*  836 */       else if (osName.contains("mac os x")) {
/*  837 */         os = 2;
/*      */       }
/*  839 */       else if (osName.contains("linux")) {
/*  840 */         os = 4;
/*      */       } else {
/*      */         
/*  843 */         os = 8;
/*      */       } 
/*      */     } 
/*  846 */     return os;
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
/*      */   public static int getPatternFlags(boolean matchCase, int others) {
/*  858 */     if (!matchCase) {
/*  859 */       others |= 0x42;
/*      */     }
/*  861 */     return others;
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
/*      */   public static int getPositionAbove(RSyntaxTextArea c, int offs, float x, TabExpander e) throws BadLocationException {
/*  881 */     TokenOrientedView tov = (TokenOrientedView)e;
/*  882 */     Token token = tov.getTokenListForPhysicalLineAbove(offs);
/*  883 */     if (token == null) {
/*  884 */       return -1;
/*      */     }
/*  886 */     if (token.getType() == 0) {
/*  887 */       int line = c.getLineOfOffset(offs);
/*  888 */       return c.getLineStartOffset(line - 1);
/*      */     } 
/*      */ 
/*      */     
/*  892 */     return token.getListOffset(c, e, (c.getMargin()).left, x);
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
/*      */   public static int getPositionBelow(RSyntaxTextArea c, int offs, float x, TabExpander e) throws BadLocationException {
/*  914 */     TokenOrientedView tov = (TokenOrientedView)e;
/*  915 */     Token token = tov.getTokenListForPhysicalLineBelow(offs);
/*  916 */     if (token == null) {
/*  917 */       return -1;
/*      */     }
/*  919 */     if (token.getType() == 0) {
/*  920 */       int line = c.getLineOfOffset(offs);
/*      */       
/*  922 */       FoldManager fm = c.getFoldManager();
/*  923 */       line = fm.getVisibleLineBelow(line);
/*  924 */       return c.getLineStartOffset(line);
/*      */     } 
/*      */ 
/*      */     
/*  928 */     return token.getListOffset(c, e, (c.getMargin()).left, x);
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
/*      */   public static Token getPreviousImportantToken(RSyntaxDocument doc, int line) {
/*  947 */     if (line < 0) {
/*  948 */       return null;
/*      */     }
/*  950 */     Token t = doc.getTokenListForLine(line);
/*  951 */     if (t != null) {
/*  952 */       t = t.getLastNonCommentNonWhitespaceToken();
/*  953 */       if (t != null) {
/*  954 */         return t;
/*      */       }
/*      */     } 
/*  957 */     return getPreviousImportantToken(doc, line - 1);
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
/*      */   public static Token getPreviousImportantTokenFromOffs(RSyntaxDocument doc, int offs) {
/*  975 */     Element root = doc.getDefaultRootElement();
/*  976 */     int line = root.getElementIndex(offs);
/*  977 */     Token t = doc.getTokenListForLine(line);
/*      */ 
/*      */     
/*  980 */     Token target = null;
/*  981 */     while (t != null && t.isPaintable() && !t.containsPosition(offs)) {
/*  982 */       if (!t.isCommentOrWhitespace()) {
/*  983 */         target = t;
/*      */       }
/*  985 */       t = t.getNextToken();
/*      */     } 
/*      */ 
/*      */     
/*  989 */     if (target == null) {
/*  990 */       target = getPreviousImportantToken(doc, line - 1);
/*      */     }
/*      */     
/*  993 */     return target;
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
/*      */   public static Token getTokenAtOffset(RSyntaxTextArea textArea, int offset) {
/* 1009 */     RSyntaxDocument doc = (RSyntaxDocument)textArea.getDocument();
/* 1010 */     return getTokenAtOffset(doc, offset);
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
/*      */   public static Token getTokenAtOffset(RSyntaxDocument doc, int offset) {
/* 1025 */     Element root = doc.getDefaultRootElement();
/* 1026 */     int lineIndex = root.getElementIndex(offset);
/* 1027 */     Token t = doc.getTokenListForLine(lineIndex);
/* 1028 */     return getTokenAtOffset(t, offset);
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
/*      */   public static Token getTokenAtOffset(Token tokenList, int offset) {
/* 1046 */     for (Token t = tokenList; t != null && t.isPaintable(); t = t.getNextToken()) {
/* 1047 */       if (t.containsPosition(offset)) {
/* 1048 */         return t;
/*      */       }
/*      */     } 
/* 1051 */     return null;
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
/*      */   public static int getWordEnd(RSyntaxTextArea textArea, int offs) throws BadLocationException {
/* 1067 */     Document doc = textArea.getDocument();
/* 1068 */     int endOffs = textArea.getLineEndOffsetOfCurrentLine();
/* 1069 */     int lineEnd = Math.min(endOffs, doc.getLength());
/* 1070 */     if (offs == lineEnd) {
/* 1071 */       return offs;
/*      */     }
/*      */     
/* 1074 */     String s = doc.getText(offs, lineEnd - offs - 1);
/* 1075 */     if (s != null && s.length() > 0) {
/* 1076 */       int i = 0;
/* 1077 */       int count = s.length();
/* 1078 */       char ch = s.charAt(i);
/* 1079 */       if (Character.isWhitespace(ch)) {
/* 1080 */         while (i < count && Character.isWhitespace(s.charAt(i++)));
/*      */       }
/* 1082 */       else if (Character.isLetterOrDigit(ch)) {
/* 1083 */         while (i < count && Character.isLetterOrDigit(s.charAt(i++)));
/*      */       } else {
/*      */         
/* 1086 */         i = 2;
/*      */       } 
/* 1088 */       offs += i - 1;
/*      */     } 
/*      */     
/* 1091 */     return offs;
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
/*      */   public static int getWordStart(RSyntaxTextArea textArea, int offs) throws BadLocationException {
/* 1107 */     Document doc = textArea.getDocument();
/* 1108 */     Element line = getLineElem(doc, offs);
/* 1109 */     if (line == null) {
/* 1110 */       throw new BadLocationException("No word at " + offs, offs);
/*      */     }
/*      */     
/* 1113 */     int lineStart = line.getStartOffset();
/* 1114 */     if (offs == lineStart) {
/* 1115 */       return offs;
/*      */     }
/*      */     
/* 1118 */     int endOffs = Math.min(offs + 1, doc.getLength());
/* 1119 */     String s = doc.getText(lineStart, endOffs - lineStart);
/* 1120 */     if (s != null && s.length() > 0) {
/* 1121 */       int i = s.length() - 1;
/* 1122 */       char ch = s.charAt(i);
/* 1123 */       if (Character.isWhitespace(ch)) {
/* 1124 */         while (i > 0 && Character.isWhitespace(s.charAt(i - 1))) {
/* 1125 */           i--;
/*      */         }
/* 1127 */         offs = lineStart + i;
/*      */       }
/* 1129 */       else if (Character.isLetterOrDigit(ch)) {
/* 1130 */         while (i > 0 && Character.isLetterOrDigit(s.charAt(i - 1))) {
/* 1131 */           i--;
/*      */         }
/* 1133 */         offs = lineStart + i;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1138 */     return offs;
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
/*      */   public static float getTokenListWidth(Token tokenList, RSyntaxTextArea textArea, TabExpander e) {
/* 1159 */     return getTokenListWidth(tokenList, textArea, e, 0.0F);
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
/*      */   public static float getTokenListWidth(Token tokenList, RSyntaxTextArea textArea, TabExpander e, float x0) {
/* 1178 */     float width = x0;
/* 1179 */     for (Token t = tokenList; t != null && t.isPaintable(); t = t.getNextToken()) {
/* 1180 */       width += t.getWidth(textArea, e, width);
/*      */     }
/* 1182 */     return width - x0;
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
/*      */ 
/*      */   
/*      */   public static float getTokenListWidthUpTo(Token tokenList, RSyntaxTextArea textArea, TabExpander e, float x0, int upTo) {
/* 1207 */     float width = 0.0F;
/* 1208 */     for (Token t = tokenList; t != null && t.isPaintable(); t = t.getNextToken()) {
/* 1209 */       if (t.containsPosition(upTo)) {
/* 1210 */         return width + t.getWidthUpTo(upTo - t.getOffset(), textArea, e, x0 + width);
/*      */       }
/*      */       
/* 1213 */       width += t.getWidth(textArea, e, x0 + width);
/*      */     } 
/* 1215 */     return width;
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
/*      */   public static boolean isBracket(char ch) {
/* 1231 */     return (ch <= '}' && (DATA_TABLE[ch] & 0x40) > 0);
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
/*      */   public static boolean isDigit(char ch) {
/* 1245 */     return (ch >= '0' && ch <= '9');
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
/*      */   public static boolean isHexCharacter(char ch) {
/* 1261 */     return (ch <= 'f' && (DATA_TABLE[ch] & 0x10) > 0);
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
/*      */   public static boolean isJavaOperator(char ch) {
/* 1276 */     return (ch <= '~' && (DATA_TABLE[ch] & 0x80) > 0);
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
/*      */   public static boolean isLetter(char ch) {
/* 1289 */     return (ch <= 'z' && (DATA_TABLE[ch] & 0x2) > 0);
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
/*      */   public static boolean isLetterOrDigit(char ch) {
/* 1302 */     return (ch <= 'z' && (DATA_TABLE[ch] & 0x20) > 0);
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
/*      */   public static boolean isLightForeground(Color fg) {
/* 1316 */     return (fg.getRed() > 160 && fg.getGreen() > 160 && fg.getBlue() > 160);
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
/*      */   public static boolean isNonWordChar(Token t) {
/* 1331 */     return (t.length() == 1 && !isLetter(t.charAt(0)));
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
/*      */   public static boolean isWhitespace(char ch) {
/* 1347 */     return (ch == ' ' || ch == '\t');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void possiblyRepaintGutter(RTextArea textArea) {
/* 1357 */     Gutter gutter = getGutter(textArea);
/* 1358 */     if (gutter != null) {
/* 1359 */       gutter.repaint();
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
/*      */   public static boolean regexCanFollowInJavaScript(Token t) {
/*      */     char ch;
/* 1375 */     return (t == null || (t
/*      */       
/* 1377 */       .length() == 1 && ((
/* 1378 */       ch = t.charAt(0)) == '=' || ch == '(' || ch == ',' || ch == '?' || ch == ':' || ch == '[' || ch == '!' || ch == '&')) || (t
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1388 */       .getType() == 23 && (t
/* 1389 */       .charAt(t.length() - 1) == '=' || t
/* 1390 */       .is(JS_AND) || t.is(JS_OR))) || t
/* 1391 */       .is(7, JS_KEYWORD_RETURN));
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
/*      */   public static void selectAndPossiblyCenter(JTextArea textArea, DocumentRange range, boolean select) {
/*      */     Rectangle r;
/* 1406 */     int start = range.getStartOffset();
/* 1407 */     int end = range.getEndOffset();
/*      */     
/* 1409 */     boolean foldsExpanded = false;
/* 1410 */     if (textArea instanceof RSyntaxTextArea) {
/* 1411 */       RSyntaxTextArea rsta = (RSyntaxTextArea)textArea;
/* 1412 */       FoldManager fm = rsta.getFoldManager();
/* 1413 */       if (fm.isCodeFoldingSupportedAndEnabled()) {
/* 1414 */         foldsExpanded = fm.ensureOffsetNotInClosedFold(start);
/* 1415 */         foldsExpanded |= fm.ensureOffsetNotInClosedFold(end);
/*      */       } 
/*      */     } 
/*      */     
/* 1419 */     if (select) {
/* 1420 */       textArea.setSelectionStart(start);
/* 1421 */       textArea.setSelectionEnd(end);
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 1426 */       r = textArea.modelToView(start);
/* 1427 */       if (r == null) {
/*      */         return;
/*      */       }
/* 1430 */       if (end != start) {
/* 1431 */         r = r.union(textArea.modelToView(end));
/*      */       }
/* 1433 */     } catch (BadLocationException ble) {
/* 1434 */       ble.printStackTrace();
/* 1435 */       if (select) {
/* 1436 */         textArea.setSelectionStart(start);
/* 1437 */         textArea.setSelectionEnd(end);
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/* 1442 */     Rectangle visible = textArea.getVisibleRect();
/*      */ 
/*      */ 
/*      */     
/* 1446 */     if (!foldsExpanded && visible.contains(r)) {
/* 1447 */       if (select) {
/* 1448 */         textArea.setSelectionStart(start);
/* 1449 */         textArea.setSelectionEnd(end);
/*      */       } 
/*      */       
/*      */       return;
/*      */     } 
/* 1454 */     r.x -= (visible.width - r.width) / 2;
/* 1455 */     r.y -= (visible.height - r.height) / 2;
/*      */     
/* 1457 */     Rectangle bounds = textArea.getBounds();
/* 1458 */     Insets i = textArea.getInsets();
/* 1459 */     bounds.x = i.left;
/* 1460 */     bounds.y = i.top;
/* 1461 */     bounds.width -= i.left + i.right;
/* 1462 */     bounds.height -= i.top + i.bottom;
/*      */     
/* 1464 */     if (visible.x < bounds.x) {
/* 1465 */       visible.x = bounds.x;
/*      */     }
/*      */     
/* 1468 */     if (visible.x + visible.width > bounds.x + bounds.width) {
/* 1469 */       visible.x = bounds.x + bounds.width - visible.width;
/*      */     }
/*      */     
/* 1472 */     if (visible.y < bounds.y) {
/* 1473 */       visible.y = bounds.y;
/*      */     }
/*      */     
/* 1476 */     if (visible.y + visible.height > bounds.y + bounds.height) {
/* 1477 */       visible.y = bounds.y + bounds.height - visible.height;
/*      */     }
/*      */     
/* 1480 */     textArea.scrollRectToVisible(visible);
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
/*      */   public static char toLowerCase(char ch) {
/* 1501 */     if (ch >= 'A' && ch <= 'Z') {
/* 1502 */       return (char)(ch | 0x20);
/*      */     }
/* 1504 */     return ch;
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
/*      */   public static Pattern wildcardToPattern(String wildcard, boolean matchCase, boolean escapeStartChar) {
/*      */     Pattern p;
/* 1520 */     int flags = getPatternFlags(matchCase, 0);
/*      */     
/* 1522 */     StringBuilder sb = new StringBuilder();
/* 1523 */     for (int i = 0; i < wildcard.length(); i++) {
/* 1524 */       char ch = wildcard.charAt(i);
/* 1525 */       switch (ch) {
/*      */         case '*':
/* 1527 */           sb.append(".*");
/*      */           break;
/*      */         case '?':
/* 1530 */           sb.append('.');
/*      */           break;
/*      */         case '^':
/* 1533 */           if (i > 0 || escapeStartChar) {
/* 1534 */             sb.append('\\');
/*      */           }
/* 1536 */           sb.append('^'); break;
/*      */         case '$': case '(': case ')': case '+': case '-':
/*      */         case '.':
/*      */         case '[':
/*      */         case '\\':
/*      */         case ']':
/*      */         case '{':
/*      */         case '|':
/*      */         case '}':
/* 1545 */           sb.append('\\').append(ch);
/*      */           break;
/*      */         default:
/* 1548 */           sb.append(ch);
/*      */           break;
/*      */       } 
/*      */ 
/*      */     
/*      */     } 
/*      */     try {
/* 1555 */       p = Pattern.compile(sb.toString(), flags);
/* 1556 */     } catch (PatternSyntaxException pse) {
/* 1557 */       pse.printStackTrace();
/* 1558 */       p = Pattern.compile(".+");
/*      */     } 
/*      */     
/* 1561 */     return p;
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\RSyntaxUtilities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */