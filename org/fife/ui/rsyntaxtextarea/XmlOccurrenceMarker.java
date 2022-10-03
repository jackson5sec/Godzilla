/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class XmlOccurrenceMarker
/*     */   implements OccurrenceMarker
/*     */ {
/*  28 */   private static final char[] CLOSE_TAG_START = new char[] { '<', '/' };
/*  29 */   private static final char[] TAG_SELF_CLOSE = new char[] { '/', '>' };
/*     */ 
/*     */ 
/*     */   
/*     */   public Token getTokenToMark(RSyntaxTextArea textArea) {
/*  34 */     return HtmlOccurrenceMarker.getTagNameTokenForCaretOffset(textArea, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValidType(RSyntaxTextArea textArea, Token t) {
/*  41 */     return textArea.getMarkOccurrencesOfTokenType(t.getType());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void markOccurrences(RSyntaxDocument doc, Token t, RSyntaxTextAreaHighlighter h, SmartHighlightPainter p) {
/*  49 */     char[] lexeme = t.getLexeme().toCharArray();
/*  50 */     int tokenOffs = t.getOffset();
/*  51 */     Element root = doc.getDefaultRootElement();
/*  52 */     int lineCount = root.getElementCount();
/*  53 */     int curLine = root.getElementIndex(t.getOffset());
/*  54 */     int depth = 0;
/*     */ 
/*     */ 
/*     */     
/*  58 */     boolean found = false;
/*  59 */     boolean forward = true;
/*  60 */     t = doc.getTokenListForLine(curLine);
/*  61 */     while (t != null && t.isPaintable()) {
/*  62 */       if (t.getType() == 25) {
/*  63 */         if (t.isSingleChar('<') && t.getOffset() + 1 == tokenOffs) {
/*  64 */           found = true;
/*     */           break;
/*     */         } 
/*  67 */         if (t.is(CLOSE_TAG_START) && t.getOffset() + 2 == tokenOffs) {
/*  68 */           found = true;
/*  69 */           forward = false;
/*     */           break;
/*     */         } 
/*     */       } 
/*  73 */       t = t.getNextToken();
/*     */     } 
/*     */     
/*  76 */     if (!found) {
/*     */       return;
/*     */     }
/*     */     
/*  80 */     if (forward) {
/*     */       
/*  82 */       t = t.getNextToken().getNextToken();
/*     */ 
/*     */       
/*     */       while (true) {
/*  86 */         if (t != null && t.isPaintable()) {
/*  87 */           if (t.getType() == 25) {
/*  88 */             if (t.is(CLOSE_TAG_START)) {
/*  89 */               Token match = t.getNextToken();
/*  90 */               if (match != null && match.is(lexeme)) {
/*  91 */                 if (depth > 0) {
/*  92 */                   depth--;
/*     */                 } else {
/*     */                   
/*     */                   try {
/*  96 */                     int end = match.getOffset() + match.length();
/*  97 */                     h.addMarkedOccurrenceHighlight(match.getOffset(), end, p);
/*  98 */                     end = tokenOffs + match.length();
/*  99 */                     h.addMarkedOccurrenceHighlight(tokenOffs, end, p);
/* 100 */                   } catch (BadLocationException ble) {
/* 101 */                     ble.printStackTrace();
/*     */                   } 
/*     */                   
/*     */                   return;
/*     */                 } 
/*     */               }
/* 107 */             } else if (t.isSingleChar('<')) {
/* 108 */               t = t.getNextToken();
/* 109 */               if (t != null && t.is(lexeme)) {
/* 110 */                 depth++;
/*     */               }
/*     */             } 
/*     */           }
/* 114 */           t = (t == null) ? null : t.getNextToken();
/*     */           continue;
/*     */         } 
/* 117 */         if (++curLine < lineCount) {
/* 118 */           t = doc.getTokenListForLine(curLine);
/*     */         }
/*     */         
/* 121 */         if (curLine >= lineCount)
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
/* 133 */       List<Entry> openCloses = new ArrayList<>();
/* 134 */       boolean inPossibleMatch = false;
/* 135 */       t = doc.getTokenListForLine(curLine);
/* 136 */       int endBefore = tokenOffs - 2;
/*     */ 
/*     */       
/*     */       while (true) {
/* 140 */         if (t != null && t.getOffset() < endBefore && t.isPaintable()) {
/* 141 */           if (t.getType() == 25) {
/* 142 */             if (t.isSingleChar('<')) {
/* 143 */               Token next = t.getNextToken();
/* 144 */               if (next != null) {
/* 145 */                 if (next.is(lexeme)) {
/* 146 */                   openCloses.add(new Entry(true, next));
/* 147 */                   inPossibleMatch = true;
/*     */                 } else {
/*     */                   
/* 150 */                   inPossibleMatch = false;
/*     */                 } 
/* 152 */                 t = next;
/*     */               }
/*     */             
/* 155 */             } else if (t.isSingleChar('>')) {
/* 156 */               inPossibleMatch = false;
/*     */             }
/* 158 */             else if (inPossibleMatch && t.is(TAG_SELF_CLOSE)) {
/* 159 */               openCloses.remove(openCloses.size() - 1);
/* 160 */               inPossibleMatch = false;
/*     */             }
/* 162 */             else if (t.is(CLOSE_TAG_START)) {
/* 163 */               Token next = t.getNextToken();
/* 164 */               if (next != null) {
/*     */                 
/* 166 */                 if (next.is(lexeme)) {
/* 167 */                   openCloses.add(new Entry(false, next));
/*     */                 }
/* 169 */                 t = next;
/*     */               } 
/*     */             } 
/*     */           }
/* 173 */           t = t.getNextToken();
/*     */           continue;
/*     */         } 
/* 176 */         for (int i = openCloses.size() - 1; i >= 0; i--) {
/* 177 */           Entry entry = openCloses.get(i);
/* 178 */           depth += entry.open ? -1 : 1;
/* 179 */           if (depth == -1) {
/*     */             try {
/* 181 */               Token match = entry.t;
/* 182 */               int end = match.getOffset() + match.length();
/* 183 */               h.addMarkedOccurrenceHighlight(match.getOffset(), end, p);
/* 184 */               end = tokenOffs + match.length();
/* 185 */               h.addMarkedOccurrenceHighlight(tokenOffs, end, p);
/* 186 */             } catch (BadLocationException ble) {
/* 187 */               ble.printStackTrace();
/*     */             } 
/* 189 */             openCloses.clear();
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/* 194 */         openCloses.clear();
/* 195 */         if (--curLine >= 0) {
/* 196 */           t = doc.getTokenListForLine(curLine);
/*     */         }
/*     */         
/* 199 */         if (curLine < 0) {
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
/* 216 */       this.open = open;
/* 217 */       this.t = t;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\XmlOccurrenceMarker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */