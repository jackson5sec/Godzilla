/*      */ package org.fife.ui.rtextarea;
/*      */ 
/*      */ import java.awt.Point;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import java.util.regex.PatternSyntaxException;
/*      */ import javax.swing.JTextArea;
/*      */ import javax.swing.text.BadLocationException;
/*      */ import javax.swing.text.Caret;
/*      */ import org.fife.ui.rsyntaxtextarea.DocumentRange;
/*      */ import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class SearchEngine
/*      */ {
/*      */   public static SearchResult find(JTextArea textArea, SearchContext context) {
/*   69 */     if (textArea instanceof RTextArea || context.getMarkAll()) {
/*   70 */       ((RTextArea)textArea).clearMarkAllHighlights();
/*      */     }
/*   72 */     boolean doMarkAll = (textArea instanceof RTextArea && context.getMarkAll());
/*      */     
/*   74 */     String text = context.getSearchFor();
/*   75 */     if (text == null || text.length() == 0) {
/*   76 */       if (doMarkAll) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*   81 */         List<DocumentRange> emptyRangeList = Collections.emptyList();
/*   82 */         ((RTextArea)textArea).markAll(emptyRangeList);
/*      */       } 
/*   84 */       return new SearchResult();
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   91 */     Caret c = textArea.getCaret();
/*   92 */     boolean forward = context.getSearchForward();
/*      */     
/*   94 */     int start = forward ? Math.max(c.getDot(), c.getMark()) : Math.min(c.getDot(), c.getMark());
/*      */     
/*   96 */     String findIn = getFindInText(textArea, start, forward);
/*   97 */     if (!context.getSearchWrap() && (findIn == null || findIn.length() == 0)) {
/*   98 */       return new SearchResult();
/*      */     }
/*      */     
/*  101 */     int markAllCount = 0;
/*  102 */     if (doMarkAll)
/*      */     {
/*  104 */       markAllCount = markAllImpl((RTextArea)textArea, context).getMarkedCount();
/*      */     }
/*      */     
/*  107 */     SearchResult result = findImpl((findIn == null) ? "" : findIn, context);
/*  108 */     if (result.wasFound() && !result.getMatchRange().isZeroLength()) {
/*      */ 
/*      */       
/*  111 */       textArea.getCaret().setSelectionVisible(true);
/*  112 */       if (forward && start > -1) {
/*  113 */         result.getMatchRange().translate(start);
/*      */       }
/*  115 */       RSyntaxUtilities.selectAndPossiblyCenter(textArea, result
/*  116 */           .getMatchRange(), true);
/*  117 */     } else if (context.getSearchWrap() && !result.wasFound()) {
/*  118 */       if (forward) {
/*  119 */         start = 0;
/*      */       } else {
/*  121 */         start = textArea.getDocument().getLength() - 1;
/*      */       } 
/*      */       
/*  124 */       findIn = getFindInText(textArea, start, forward);
/*      */       
/*  126 */       if (findIn == null || findIn.length() == 0) {
/*  127 */         SearchResult emptyResult = new SearchResult();
/*  128 */         emptyResult.setWrapped(true);
/*  129 */         return emptyResult;
/*      */       } 
/*      */       
/*  132 */       if (doMarkAll)
/*      */       {
/*  134 */         markAllCount = markAllImpl((RTextArea)textArea, context).getMarkedCount();
/*      */       }
/*      */       
/*  137 */       result = findImpl(findIn, context);
/*  138 */       result.setWrapped(true);
/*  139 */       if (result.wasFound() && !result.getMatchRange().isZeroLength()) {
/*      */ 
/*      */         
/*  142 */         textArea.getCaret().setSelectionVisible(true);
/*  143 */         if (forward) {
/*  144 */           result.getMatchRange().translate(start);
/*      */         }
/*  146 */         RSyntaxUtilities.selectAndPossiblyCenter(textArea, result
/*  147 */             .getMatchRange(), true);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  152 */     result.setMarkedCount(markAllCount);
/*  153 */     return result;
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
/*      */   private static SearchResult findImpl(String findIn, SearchContext context) {
/*  172 */     String text = context.getSearchFor();
/*  173 */     boolean forward = context.getSearchForward();
/*      */ 
/*      */     
/*  176 */     DocumentRange range = null;
/*  177 */     if (!context.isRegularExpression()) {
/*  178 */       int pos = getNextMatchPos(text, findIn, forward, context
/*  179 */           .getMatchCase(), context.getWholeWord());
/*  180 */       findIn = null;
/*  181 */       if (pos != -1) {
/*  182 */         range = new DocumentRange(pos, pos + text.length());
/*      */       
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/*  190 */       Point regExPos = null;
/*  191 */       int start = 0;
/*      */       do {
/*  193 */         regExPos = getNextMatchPosRegEx(text, findIn.substring(start), forward, context
/*  194 */             .getMatchCase(), context.getWholeWord());
/*  195 */         if (regExPos == null)
/*  196 */           continue;  if (regExPos.x != regExPos.y) {
/*  197 */           regExPos.translate(start, start);
/*  198 */           range = new DocumentRange(regExPos.x, regExPos.y);
/*      */         } else {
/*      */           
/*  201 */           start += regExPos.x + 1;
/*      */         }
/*      */       
/*  204 */       } while (start < findIn.length() && regExPos != null && range == null);
/*      */     } 
/*      */     
/*  207 */     if (range != null) {
/*  208 */       return new SearchResult(range, 1, 0);
/*      */     }
/*  210 */     return new SearchResult();
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
/*      */   private static CharSequence getFindInCharSequence(RTextArea textArea, int start, boolean forward) {
/*  229 */     RDocument doc = (RDocument)textArea.getDocument();
/*  230 */     int csStart = 0;
/*  231 */     int csEnd = 0;
/*  232 */     if (forward) {
/*  233 */       csStart = start;
/*  234 */       csEnd = doc.getLength();
/*      */     } else {
/*      */       
/*  237 */       csStart = 0;
/*  238 */       csEnd = start;
/*      */     } 
/*  240 */     return new RDocumentCharSequence(doc, csStart, csEnd);
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
/*      */   private static String getFindInText(JTextArea textArea, int start, boolean forward) {
/*  254 */     String findIn = null;
/*      */     try {
/*  256 */       if (forward) {
/*  257 */         findIn = textArea.getText(start, textArea
/*  258 */             .getDocument().getLength() - start);
/*      */       } else {
/*      */         
/*  261 */         findIn = textArea.getText(0, start);
/*      */       } 
/*  263 */     } catch (BadLocationException ble) {
/*      */       
/*  265 */       ble.printStackTrace();
/*      */     } 
/*      */     
/*  268 */     return findIn;
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
/*      */   private static List getMatches(Matcher m, String replaceStr) {
/*  303 */     ArrayList<Point> matches = new ArrayList();
/*  304 */     while (m.find()) {
/*  305 */       Point loc = new Point(m.start(), m.end());
/*  306 */       if (replaceStr == null) {
/*  307 */         matches.add(loc);
/*      */         continue;
/*      */       } 
/*  310 */       matches.add(new RegExReplaceInfo(m.group(0), loc.x, loc.y, 
/*  311 */             getReplacementText(m, replaceStr)));
/*      */     } 
/*      */     
/*  314 */     return matches;
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
/*      */   public static int getNextMatchPos(String searchFor, String searchIn, boolean forward, boolean matchCase, boolean wholeWord) {
/*  342 */     if (!matchCase) {
/*  343 */       return getNextMatchPosImpl(searchFor.toLowerCase(), searchIn
/*  344 */           .toLowerCase(), forward, matchCase, wholeWord);
/*      */     }
/*      */ 
/*      */     
/*  348 */     return getNextMatchPosImpl(searchFor, searchIn, forward, matchCase, wholeWord);
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
/*      */   private static int getNextMatchPosImpl(String searchFor, String searchIn, boolean goForward, boolean matchCase, boolean wholeWord) {
/*  375 */     if (wholeWord) {
/*  376 */       int len = searchFor.length();
/*  377 */       int temp = goForward ? 0 : searchIn.length();
/*  378 */       int tempChange = goForward ? 1 : -1;
/*      */       while (true) {
/*  380 */         if (goForward) {
/*  381 */           temp = searchIn.indexOf(searchFor, temp);
/*      */         } else {
/*      */           
/*  384 */           temp = searchIn.lastIndexOf(searchFor, temp);
/*      */         } 
/*  386 */         if (temp != -1) {
/*  387 */           if (isWholeWord(searchIn, temp, len)) {
/*  388 */             return temp;
/*      */           }
/*      */           
/*  391 */           temp += tempChange; continue;
/*      */         } 
/*      */         break;
/*      */       } 
/*  395 */       return temp;
/*      */     } 
/*      */ 
/*      */     
/*  399 */     return goForward ? searchIn.indexOf(searchFor) : searchIn
/*  400 */       .lastIndexOf(searchFor);
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
/*      */   private static Point getNextMatchPosRegEx(String regEx, CharSequence searchIn, boolean goForward, boolean matchCase, boolean wholeWord) {
/*  429 */     return (Point)getNextMatchPosRegExImpl(regEx, searchIn, goForward, matchCase, wholeWord, null);
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
/*      */ 
/*      */ 
/*      */   
/*      */   private static Object getNextMatchPosRegExImpl(String regEx, CharSequence searchIn, boolean goForward, boolean matchCase, boolean wholeWord, String replaceStr) {
/*  469 */     if (wholeWord) {
/*  470 */       regEx = "\\b" + regEx + "\\b";
/*      */     }
/*      */ 
/*      */     
/*  474 */     int flags = 8;
/*  475 */     flags = RSyntaxUtilities.getPatternFlags(matchCase, flags);
/*  476 */     Pattern pattern = null;
/*      */     try {
/*  478 */       pattern = Pattern.compile(regEx, flags);
/*  479 */     } catch (PatternSyntaxException pse) {
/*  480 */       return null;
/*      */     } 
/*      */ 
/*      */     
/*  484 */     Matcher m = pattern.matcher(searchIn);
/*      */ 
/*      */     
/*  487 */     if (goForward) {
/*  488 */       if (m.find()) {
/*  489 */         if (replaceStr == null) {
/*  490 */           return new Point(m.start(), m.end());
/*      */         }
/*      */         
/*  493 */         return new RegExReplaceInfo(m.group(0), m
/*  494 */             .start(), m.end(), 
/*  495 */             getReplacementText(m, replaceStr));
/*      */       }
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  501 */       List<?> matches = getMatches(m, replaceStr);
/*  502 */       if (!matches.isEmpty()) {
/*  503 */         return matches.get(matches.size() - 1);
/*      */       }
/*      */     } 
/*      */     
/*  507 */     return null;
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
/*      */   private static RegExReplaceInfo getRegExReplaceInfo(CharSequence searchIn, SearchContext context) {
/*  532 */     String replacement = context.getReplaceWith();
/*  533 */     if (replacement == null) {
/*  534 */       replacement = "";
/*      */     }
/*  536 */     String regex = context.getSearchFor();
/*  537 */     boolean goForward = context.getSearchForward();
/*  538 */     boolean matchCase = context.getMatchCase();
/*  539 */     boolean wholeWord = context.getWholeWord();
/*  540 */     return (RegExReplaceInfo)getNextMatchPosRegExImpl(regex, searchIn, goForward, matchCase, wholeWord, replacement);
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
/*      */   public static String getReplacementText(Matcher m, CharSequence template) {
/*  575 */     int cursor = 0;
/*  576 */     StringBuilder result = new StringBuilder();
/*      */     
/*  578 */     while (cursor < template.length()) {
/*      */       
/*  580 */       char nextChar = template.charAt(cursor);
/*      */       
/*  582 */       if (nextChar == '\\') {
/*  583 */         nextChar = template.charAt(++cursor);
/*  584 */         switch (nextChar) {
/*      */           case 'n':
/*  586 */             nextChar = '\n';
/*      */             break;
/*      */           case 't':
/*  589 */             nextChar = '\t';
/*      */             break;
/*      */         } 
/*  592 */         result.append(nextChar);
/*  593 */         cursor++; continue;
/*      */       } 
/*  595 */       if (nextChar == '$') {
/*      */         
/*  597 */         cursor++;
/*      */ 
/*      */         
/*  600 */         int refNum = template.charAt(cursor) - 48;
/*  601 */         if (refNum < 0 || refNum > 9)
/*      */         {
/*      */ 
/*      */           
/*  605 */           throw new IndexOutOfBoundsException("No group " + template
/*  606 */               .charAt(cursor));
/*      */         }
/*  608 */         cursor++;
/*      */ 
/*      */         
/*  611 */         boolean done = false;
/*  612 */         while (!done && 
/*  613 */           cursor < template.length()) {
/*      */ 
/*      */           
/*  616 */           int nextDigit = template.charAt(cursor) - 48;
/*  617 */           if (nextDigit < 0 || nextDigit > 9) {
/*      */             break;
/*      */           }
/*  620 */           int newRefNum = refNum * 10 + nextDigit;
/*  621 */           if (m.groupCount() < newRefNum) {
/*  622 */             done = true;
/*      */             continue;
/*      */           } 
/*  625 */           refNum = newRefNum;
/*  626 */           cursor++;
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  631 */         if (m.group(refNum) != null) {
/*  632 */           result.append(m.group(refNum));
/*      */         }
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/*  638 */       result.append(nextChar);
/*  639 */       cursor++;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  644 */     return result.toString();
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
/*      */   private static boolean isWholeWord(CharSequence searchIn, int offset, int len) {
/*      */     boolean wsBefore;
/*      */     boolean wsAfter;
/*      */     try {
/*  660 */       wsBefore = !Character.isLetterOrDigit(searchIn.charAt(offset - 1));
/*  661 */     } catch (IndexOutOfBoundsException e) {
/*  662 */       wsBefore = true;
/*      */     } 
/*      */     
/*      */     try {
/*  666 */       wsAfter = !Character.isLetterOrDigit(searchIn.charAt(offset + len));
/*  667 */     } catch (IndexOutOfBoundsException e) {
/*  668 */       wsAfter = true;
/*      */     } 
/*      */     
/*  671 */     return (wsBefore && wsAfter);
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
/*      */   private static int makeMarkAndDotEqual(JTextArea textArea, boolean forward) {
/*  688 */     Caret c = textArea.getCaret();
/*      */     
/*  690 */     int val = forward ? Math.min(c.getDot(), c.getMark()) : Math.max(c.getDot(), c.getMark());
/*  691 */     c.setDot(val);
/*  692 */     return val;
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
/*      */   public static SearchResult markAll(RTextArea textArea, SearchContext context) {
/*  711 */     textArea.clearMarkAllHighlights();
/*      */     
/*  713 */     return markAllImpl(textArea, context);
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
/*      */   private static SearchResult markAllImpl(RTextArea textArea, SearchContext context) {
/*  735 */     String toMark = context.getSearchFor();
/*  736 */     int markAllCount = 0;
/*      */ 
/*      */     
/*  739 */     if (context.getMarkAll() && toMark != null && toMark.length() > 0) {
/*      */ 
/*      */       
/*  742 */       List<DocumentRange> highlights = new ArrayList<>();
/*  743 */       context = context.clone();
/*  744 */       context.setSearchForward(true);
/*  745 */       context.setMarkAll(false);
/*      */       
/*  747 */       String findIn = textArea.getText();
/*  748 */       int start = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  754 */       if (!context.getMatchCase()) {
/*  755 */         context.setMatchCase(true);
/*  756 */         context.setSearchFor(toMark.toLowerCase());
/*  757 */         findIn = findIn.toLowerCase();
/*      */       } 
/*      */       
/*  760 */       SearchResult res = findImpl(findIn, context);
/*  761 */       while (res.wasFound()) {
/*  762 */         DocumentRange match = res.getMatchRange().translate(start);
/*  763 */         if (match.isZeroLength()) {
/*      */ 
/*      */ 
/*      */           
/*  767 */           start = match.getEndOffset() + 1;
/*  768 */           if (start > findIn.length()) {
/*      */             break;
/*      */           }
/*      */         } else {
/*      */           
/*  773 */           highlights.add(match);
/*  774 */           start = match.getEndOffset();
/*      */         } 
/*  776 */         res = findImpl(findIn.substring(start), context);
/*      */       } 
/*  778 */       textArea.markAll(highlights);
/*  779 */       markAllCount = highlights.size();
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/*  785 */       List<DocumentRange> empty = Collections.emptyList();
/*  786 */       textArea.markAll(empty);
/*      */     } 
/*      */     
/*  789 */     return new SearchResult(null, 0, markAllCount);
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
/*      */   private static SearchResult regexReplace(RTextArea textArea, SearchContext context) {
/*  819 */     Caret c = textArea.getCaret();
/*  820 */     boolean forward = context.getSearchForward();
/*  821 */     int start = makeMarkAndDotEqual(textArea, forward);
/*      */     
/*  823 */     CharSequence findIn = getFindInCharSequence(textArea, start, forward);
/*  824 */     if (findIn == null) {
/*  825 */       return new SearchResult();
/*      */     }
/*      */     
/*  828 */     int markAllCount = 0;
/*  829 */     if (context.getMarkAll()) {
/*  830 */       markAllCount = markAllImpl(textArea, context).getMarkedCount();
/*      */     }
/*      */ 
/*      */     
/*  834 */     RegExReplaceInfo info = getRegExReplaceInfo(findIn, context);
/*      */ 
/*      */     
/*  837 */     DocumentRange range = null;
/*  838 */     if (info != null) {
/*      */ 
/*      */ 
/*      */       
/*  842 */       c.setSelectionVisible(true);
/*      */       
/*  844 */       int matchStart = info.getStartIndex();
/*  845 */       int matchEnd = info.getEndIndex();
/*  846 */       if (forward) {
/*  847 */         matchStart += start;
/*  848 */         matchEnd += start;
/*      */       } 
/*  850 */       textArea.setSelectionStart(matchStart);
/*  851 */       textArea.setSelectionEnd(matchEnd);
/*  852 */       String replacement = info.getReplacement();
/*  853 */       textArea.replaceSelection(replacement);
/*      */ 
/*      */       
/*  856 */       int dot = matchStart + replacement.length();
/*  857 */       findIn = getFindInCharSequence(textArea, dot, forward);
/*  858 */       info = getRegExReplaceInfo(findIn, context);
/*  859 */       if (info != null) {
/*  860 */         matchStart = info.getStartIndex();
/*  861 */         matchEnd = info.getEndIndex();
/*  862 */         if (forward) {
/*  863 */           matchStart += dot;
/*  864 */           matchEnd += dot;
/*      */         } 
/*  866 */         range = new DocumentRange(matchStart, matchEnd);
/*      */       } else {
/*      */         
/*  869 */         range = new DocumentRange(dot, dot);
/*      */       } 
/*  871 */       RSyntaxUtilities.selectAndPossiblyCenter(textArea, range, true);
/*      */     } 
/*      */ 
/*      */     
/*  875 */     int count = (range != null) ? 1 : 0;
/*  876 */     return new SearchResult(range, count, markAllCount);
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
/*      */   public static SearchResult replace(RTextArea textArea, SearchContext context) {
/*  901 */     if (context.getMarkAll()) {
/*  902 */       textArea.clearMarkAllHighlights();
/*      */     }
/*      */     
/*  905 */     String toFind = context.getSearchFor();
/*  906 */     if (toFind == null || toFind.length() == 0) {
/*  907 */       return new SearchResult();
/*      */     }
/*      */     
/*  910 */     textArea.beginAtomicEdit();
/*      */ 
/*      */     
/*      */     try {
/*  914 */       if (context.isRegularExpression()) {
/*  915 */         return regexReplace(textArea, context);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  924 */       makeMarkAndDotEqual(textArea, context.getSearchForward());
/*  925 */       SearchResult res = find(textArea, context);
/*      */       
/*  927 */       if (res.wasFound() && !res.getMatchRange().isZeroLength()) {
/*      */         DocumentRange range;
/*      */         
/*  930 */         String replacement = context.getReplaceWith();
/*  931 */         textArea.replaceSelection(replacement);
/*      */ 
/*      */         
/*  934 */         int dot = res.getMatchRange().getStartOffset();
/*  935 */         if (context.getSearchForward()) {
/*  936 */           int length = (replacement == null) ? 0 : replacement.length();
/*  937 */           dot += length;
/*      */         } 
/*  939 */         textArea.setCaretPosition(dot);
/*      */         
/*  941 */         SearchResult next = find(textArea, context);
/*      */         
/*  943 */         if (next.wasFound()) {
/*  944 */           range = next.getMatchRange();
/*      */         } else {
/*      */           
/*  947 */           range = new DocumentRange(dot, dot);
/*      */         } 
/*  949 */         res.setMatchRange(range);
/*  950 */         RSyntaxUtilities.selectAndPossiblyCenter(textArea, range, true);
/*      */       } 
/*      */ 
/*      */       
/*  954 */       return res;
/*      */     } finally {
/*      */       
/*  957 */       textArea.endAtomicEdit();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static SearchResult replaceAll(RTextArea textArea, SearchContext context) {
/*  982 */     if (context.getMarkAll()) {
/*  983 */       textArea.clearMarkAllHighlights();
/*      */     }
/*      */     
/*  986 */     context.setSearchForward(true);
/*  987 */     String toFind = context.getSearchFor();
/*  988 */     if (toFind == null || toFind.length() == 0) {
/*  989 */       return new SearchResult();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  994 */     if (context.getMarkAll()) {
/*  995 */       context = context.clone();
/*  996 */       context.setMarkAll(false);
/*      */     } 
/*      */     
/*  999 */     SearchResult lastFound = null;
/* 1000 */     int count = 0;
/* 1001 */     textArea.beginAtomicEdit();
/*      */     
/*      */     try {
/* 1004 */       int oldOffs = textArea.getCaretPosition();
/* 1005 */       textArea.setCaretPosition(0);
/* 1006 */       SearchResult res = replace(textArea, context);
/*      */       
/* 1008 */       while (res.wasFound()) {
/*      */         
/* 1010 */         lastFound = res;
/* 1011 */         count++;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1016 */         if (res.getMatchRange().isZeroLength()) {
/* 1017 */           if (res.getMatchRange().getStartOffset() == textArea
/* 1018 */             .getDocument().getLength()) {
/*      */             break;
/*      */           }
/*      */           
/* 1022 */           textArea.setCaretPosition(textArea.getCaretPosition() + 1);
/*      */         } 
/*      */ 
/*      */         
/* 1026 */         res = replace(textArea, context);
/*      */       } 
/*      */ 
/*      */       
/* 1030 */       if (lastFound == null) {
/* 1031 */         textArea.setCaretPosition(oldOffs);
/* 1032 */         lastFound = new SearchResult();
/*      */       } 
/*      */     } finally {
/*      */       
/* 1036 */       textArea.endAtomicEdit();
/*      */     } 
/*      */     
/* 1039 */     lastFound.setCount(count);
/* 1040 */     return lastFound;
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rtextarea\SearchEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */