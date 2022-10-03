/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Element;
/*     */ import org.fife.ui.rtextarea.SmartHighlightPainter;
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
/*     */ public class HtmlOccurrenceMarker
/*     */   implements OccurrenceMarker
/*     */ {
/*  34 */   private static final char[] CLOSE_TAG_START = new char[] { '<', '/' };
/*  35 */   private static final char[] TAG_SELF_CLOSE = new char[] { '/', '>' };
/*     */ 
/*     */   
/*  38 */   private static final Set<String> TAGS_REQUIRING_CLOSING = getRequiredClosingTags();
/*     */   
/*     */   public static final Set<String> getRequiredClosingTags() {
/*  41 */     String[] tags = { "html", "head", "title", "style", "script", "noscript", "body", "section", "nav", "article", "aside", "h1", "h2", "h3", "h4", "h5", "h6", "header", "footer", "address", "pre", "dialog", "blockquote", "ol", "ul", "dl", "a", "q", "cite", "em", "strong", "small", "mark", "dfn", "abbr", "time", "progress", "meter", "code", "var", "samp", "kbd", "sub", "sup", "span", "i", "b", "bdo", "ruby", "rt", "rp", "ins", "del", "figure", "iframe", "object", "video", "audio", "canvas", "map", "table", "caption", "form", "fieldset", "label", "button", "select", "datalist", "textarea", "output", "details", "bb", "menu", "legend", "div", "acronym", "applet", "big", "blink", "center", "dir", "font", "frame", "frameset", "isindex", "listing", "marquee", "nobr", "noembed", "noframes", "plaintext", "s", "spacer", "strike", "tt", "u", "xmp" };
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
/*     */     
/* 141 */     return new HashSet<>(Arrays.asList(tags));
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
/*     */   public static final Token getTagNameTokenForCaretOffset(RSyntaxTextArea textArea, OccurrenceMarker occurrenceMarker) {
/* 165 */     int dot = textArea.getCaretPosition();
/* 166 */     Token t = textArea.getTokenListForLine(textArea.getCaretLineNumber());
/* 167 */     Token toMark = null;
/*     */     
/* 169 */     while (t != null && t.isPaintable()) {
/* 170 */       if (t.getType() == 26) {
/* 171 */         toMark = t;
/*     */       }
/*     */ 
/*     */       
/* 175 */       if (t.getEndOffset() == dot || t.containsPosition(dot)) {
/*     */ 
/*     */         
/* 178 */         if (occurrenceMarker.isValidType(textArea, t) && t
/* 179 */           .getType() != 26) {
/* 180 */           return t;
/*     */         }
/* 182 */         if (t.containsPosition(dot)) {
/*     */           break;
/*     */         }
/*     */       } 
/* 186 */       if (t.getType() == 25 && (
/* 187 */         t.isSingleChar('>') || t.is(TAG_SELF_CLOSE))) {
/* 188 */         toMark = null;
/*     */       }
/*     */       
/* 191 */       t = t.getNextToken();
/*     */     } 
/*     */     
/* 194 */     return toMark;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Token getTokenToMark(RSyntaxTextArea textArea) {
/* 201 */     return getTagNameTokenForCaretOffset(textArea, this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValidType(RSyntaxTextArea textArea, Token t) {
/* 207 */     return textArea.getMarkOccurrencesOfTokenType(t.getType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void markOccurrences(RSyntaxDocument doc, Token t, RSyntaxTextAreaHighlighter h, SmartHighlightPainter p) {
/* 215 */     if (t.getType() != 26) {
/* 216 */       DefaultOccurrenceMarker.markOccurrencesOfToken(doc, t, h, p);
/*     */       
/*     */       return;
/*     */     } 
/* 220 */     String lexemeStr = t.getLexeme();
/* 221 */     char[] lexeme = lexemeStr.toCharArray();
/* 222 */     lexemeStr = lexemeStr.toLowerCase();
/* 223 */     int tokenOffs = t.getOffset();
/* 224 */     Element root = doc.getDefaultRootElement();
/* 225 */     int lineCount = root.getElementCount();
/* 226 */     int curLine = root.getElementIndex(t.getOffset());
/* 227 */     int depth = 0;
/*     */ 
/*     */ 
/*     */     
/* 231 */     boolean found = false;
/* 232 */     boolean forward = true;
/* 233 */     t = doc.getTokenListForLine(curLine);
/* 234 */     while (t != null && t.isPaintable()) {
/* 235 */       if (t.getType() == 25) {
/* 236 */         if (t.isSingleChar('<') && t.getOffset() + 1 == tokenOffs) {
/*     */ 
/*     */           
/* 239 */           if (TAGS_REQUIRING_CLOSING.contains(lexemeStr)) {
/* 240 */             found = true;
/*     */           }
/*     */           break;
/*     */         } 
/* 244 */         if (t.is(CLOSE_TAG_START) && t.getOffset() + 2 == tokenOffs) {
/*     */ 
/*     */           
/* 247 */           found = true;
/* 248 */           forward = false;
/*     */           break;
/*     */         } 
/*     */       } 
/* 252 */       t = t.getNextToken();
/*     */     } 
/*     */     
/* 255 */     if (!found) {
/*     */       return;
/*     */     }
/*     */     
/* 259 */     if (forward) {
/*     */       
/* 261 */       t = t.getNextToken().getNextToken();
/*     */ 
/*     */       
/*     */       while (true) {
/* 265 */         if (t != null && t.isPaintable()) {
/* 266 */           if (t.getType() == 25) {
/* 267 */             if (t.is(CLOSE_TAG_START)) {
/* 268 */               Token match = t.getNextToken();
/* 269 */               if (match != null && match.is(lexeme)) {
/* 270 */                 if (depth > 0) {
/* 271 */                   depth--;
/*     */                 } else {
/*     */                   
/*     */                   try {
/* 275 */                     int end = match.getOffset() + match.length();
/* 276 */                     h.addMarkedOccurrenceHighlight(match.getOffset(), end, p);
/* 277 */                     end = tokenOffs + match.length();
/* 278 */                     h.addMarkedOccurrenceHighlight(tokenOffs, end, p);
/* 279 */                   } catch (BadLocationException ble) {
/* 280 */                     ble.printStackTrace();
/*     */                   } 
/*     */                   
/*     */                   return;
/*     */                 } 
/*     */               }
/* 286 */             } else if (t.isSingleChar('<')) {
/* 287 */               t = t.getNextToken();
/* 288 */               if (t != null && t.is(lexeme)) {
/* 289 */                 depth++;
/*     */               }
/*     */             } 
/*     */           }
/* 293 */           t = (t == null) ? null : t.getNextToken();
/*     */           continue;
/*     */         } 
/* 296 */         if (++curLine < lineCount) {
/* 297 */           t = doc.getTokenListForLine(curLine);
/*     */         }
/*     */         
/* 300 */         if (curLine >= lineCount)
/*     */         {
/*     */           break;
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 312 */       List<Entry> openCloses = new ArrayList<>();
/* 313 */       boolean inPossibleMatch = false;
/* 314 */       t = doc.getTokenListForLine(curLine);
/* 315 */       int endBefore = tokenOffs - 2;
/*     */ 
/*     */       
/*     */       while (true) {
/* 319 */         if (t != null && t.getOffset() < endBefore && t.isPaintable()) {
/* 320 */           if (t.getType() == 25) {
/* 321 */             if (t.isSingleChar('<')) {
/* 322 */               Token next = t.getNextToken();
/* 323 */               if (next != null) {
/* 324 */                 if (next.is(lexeme)) {
/* 325 */                   openCloses.add(new Entry(true, next));
/* 326 */                   inPossibleMatch = true;
/*     */                 } else {
/*     */                   
/* 329 */                   inPossibleMatch = false;
/*     */                 } 
/* 331 */                 t = next;
/*     */               }
/*     */             
/* 334 */             } else if (t.isSingleChar('>')) {
/* 335 */               inPossibleMatch = false;
/*     */             }
/* 337 */             else if (inPossibleMatch && t.is(TAG_SELF_CLOSE)) {
/* 338 */               openCloses.remove(openCloses.size() - 1);
/* 339 */               inPossibleMatch = false;
/*     */             }
/* 341 */             else if (t.is(CLOSE_TAG_START)) {
/* 342 */               Token next = t.getNextToken();
/* 343 */               if (next != null) {
/*     */                 
/* 345 */                 if (next.is(lexeme)) {
/* 346 */                   openCloses.add(new Entry(false, next));
/*     */                 }
/* 348 */                 t = next;
/*     */               } 
/*     */             } 
/*     */           }
/* 352 */           t = t.getNextToken();
/*     */           continue;
/*     */         } 
/* 355 */         for (int i = openCloses.size() - 1; i >= 0; i--) {
/* 356 */           Entry entry = openCloses.get(i);
/* 357 */           depth += entry.open ? -1 : 1;
/* 358 */           if (depth == -1) {
/*     */             try {
/* 360 */               Token match = entry.t;
/* 361 */               int end = match.getOffset() + match.length();
/* 362 */               h.addMarkedOccurrenceHighlight(match.getOffset(), end, p);
/* 363 */               end = tokenOffs + match.length();
/* 364 */               h.addMarkedOccurrenceHighlight(tokenOffs, end, p);
/* 365 */             } catch (BadLocationException ble) {
/* 366 */               ble.printStackTrace();
/*     */             } 
/* 368 */             openCloses.clear();
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 373 */         openCloses.clear();
/* 374 */         if (--curLine >= 0) {
/* 375 */           t = doc.getTokenListForLine(curLine);
/*     */         }
/*     */         
/* 378 */         if (curLine < 0) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class Entry
/*     */   {
/*     */     private boolean open;
/*     */     
/*     */     private Token t;
/*     */ 
/*     */     
/*     */     Entry(boolean open, Token t) {
/* 395 */       this.open = open;
/* 396 */       this.t = t;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\HtmlOccurrenceMarker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */