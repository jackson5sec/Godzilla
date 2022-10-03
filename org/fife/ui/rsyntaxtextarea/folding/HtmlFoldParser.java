/*     */ package org.fife.ui.rsyntaxtextarea.folding;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
/*     */ import org.fife.ui.rsyntaxtextarea.Token;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HtmlFoldParser
/*     */   implements FoldParser
/*     */ {
/*     */   public static final int LANGUAGE_HTML = -1;
/*     */   public static final int LANGUAGE_PHP = 0;
/*     */   public static final int LANGUAGE_JSP = 1;
/*     */   private final int language;
/*     */   private static final Set<String> FOLDABLE_TAGS;
/*  61 */   private static final char[] MARKUP_CLOSING_TAG_START = "</".toCharArray();
/*     */   
/*  63 */   private static final char[] MLC_START = "<!--".toCharArray();
/*  64 */   private static final char[] MLC_END = "-->".toCharArray();
/*     */   
/*  66 */   private static final char[] PHP_START = "<?".toCharArray();
/*  67 */   private static final char[] PHP_END = "?>".toCharArray();
/*     */ 
/*     */   
/*  70 */   private static final char[] JSP_START = "<%".toCharArray();
/*  71 */   private static final char[] JSP_END = "%>".toCharArray();
/*     */   
/*  73 */   private static final char[][] LANG_START = new char[][] { PHP_START, JSP_START };
/*  74 */   private static final char[][] LANG_END = new char[][] { PHP_END, JSP_END };
/*     */   
/*  76 */   private static final char[] JSP_COMMENT_START = "<%--".toCharArray();
/*  77 */   private static final char[] JSP_COMMENT_END = "--%>".toCharArray();
/*     */   
/*     */   static {
/*  80 */     FOLDABLE_TAGS = new HashSet<>();
/*  81 */     FOLDABLE_TAGS.add("body");
/*  82 */     FOLDABLE_TAGS.add("canvas");
/*  83 */     FOLDABLE_TAGS.add("div");
/*  84 */     FOLDABLE_TAGS.add("form");
/*  85 */     FOLDABLE_TAGS.add("head");
/*  86 */     FOLDABLE_TAGS.add("html");
/*  87 */     FOLDABLE_TAGS.add("ol");
/*  88 */     FOLDABLE_TAGS.add("pre");
/*  89 */     FOLDABLE_TAGS.add("script");
/*  90 */     FOLDABLE_TAGS.add("span");
/*  91 */     FOLDABLE_TAGS.add("style");
/*  92 */     FOLDABLE_TAGS.add("table");
/*  93 */     FOLDABLE_TAGS.add("tfoot");
/*  94 */     FOLDABLE_TAGS.add("thead");
/*  95 */     FOLDABLE_TAGS.add("tr");
/*  96 */     FOLDABLE_TAGS.add("td");
/*  97 */     FOLDABLE_TAGS.add("ul");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HtmlFoldParser(int language) {
/* 107 */     if (language < -1 && language > 1) {
/* 108 */       throw new IllegalArgumentException("Invalid language: " + language);
/*     */     }
/* 110 */     this.language = language;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Fold> getFolds(RSyntaxTextArea textArea) {
/* 117 */     List<Fold> folds = new ArrayList<>();
/* 118 */     Stack<String> tagNameStack = new Stack<>();
/* 119 */     boolean inSublanguage = false;
/*     */     
/* 121 */     Fold currentFold = null;
/* 122 */     int lineCount = textArea.getLineCount();
/* 123 */     boolean inMLC = false;
/* 124 */     boolean inJSMLC = false;
/* 125 */     TagCloseInfo tci = new TagCloseInfo();
/*     */ 
/*     */     
/*     */     try {
/* 129 */       for (int line = 0; line < lineCount; line++)
/*     */       {
/* 131 */         Token t = textArea.getTokenListForLine(line);
/* 132 */         while (t != null && t.isPaintable())
/*     */         {
/*     */ 
/*     */           
/* 136 */           if (this.language >= 0 && t.getType() == 22)
/*     */           {
/*     */             
/* 139 */             if (t.startsWith(LANG_START[this.language])) {
/* 140 */               if (currentFold == null) {
/* 141 */                 currentFold = new Fold(0, textArea, t.getOffset());
/* 142 */                 folds.add(currentFold);
/*     */               } else {
/*     */                 
/* 145 */                 currentFold = currentFold.createChild(0, t.getOffset());
/*     */               } 
/* 147 */               inSublanguage = true;
/*     */ 
/*     */             
/*     */             }
/* 151 */             else if (t.startsWith(LANG_END[this.language]) && currentFold != null) {
/* 152 */               int phpEnd = t.getEndOffset() - 1;
/* 153 */               currentFold.setEndOffset(phpEnd);
/* 154 */               Fold parentFold = currentFold.getParent();
/*     */               
/* 156 */               if (currentFold.isOnSingleLine()) {
/* 157 */                 removeFold(currentFold, folds);
/*     */               }
/* 159 */               currentFold = parentFold;
/* 160 */               inSublanguage = false;
/* 161 */               t = t.getNextToken();
/*     */               
/*     */               continue;
/*     */             } 
/*     */           }
/*     */           
/* 167 */           if (!inSublanguage)
/*     */           {
/* 169 */             if (t.getType() == 2) {
/*     */ 
/*     */               
/* 172 */               if (inMLC) {
/*     */                 
/* 174 */                 if (t.endsWith(MLC_END)) {
/* 175 */                   int mlcEnd = t.getEndOffset() - 1;
/* 176 */                   currentFold.setEndOffset(mlcEnd);
/* 177 */                   Fold parentFold = currentFold.getParent();
/*     */                   
/* 179 */                   if (currentFold.isOnSingleLine()) {
/* 180 */                     removeFold(currentFold, folds);
/*     */                   }
/* 182 */                   currentFold = parentFold;
/* 183 */                   inMLC = false;
/*     */ 
/*     */                 
/*     */                 }
/*     */ 
/*     */               
/*     */               }
/* 190 */               else if (inJSMLC) {
/*     */                 
/* 192 */                 if (t.endsWith(JSP_COMMENT_END)) {
/* 193 */                   int mlcEnd = t.getEndOffset() - 1;
/* 194 */                   currentFold.setEndOffset(mlcEnd);
/* 195 */                   Fold parentFold = currentFold.getParent();
/*     */                   
/* 197 */                   if (currentFold.isOnSingleLine()) {
/* 198 */                     removeFold(currentFold, folds);
/*     */                   }
/* 200 */                   currentFold = parentFold;
/* 201 */                   inJSMLC = false;
/*     */ 
/*     */                 
/*     */                 }
/*     */ 
/*     */               
/*     */               }
/* 208 */               else if (t.startsWith(MLC_START) && !t.endsWith(MLC_END)) {
/* 209 */                 if (currentFold == null) {
/* 210 */                   currentFold = new Fold(1, textArea, t.getOffset());
/* 211 */                   folds.add(currentFold);
/*     */                 } else {
/*     */                   
/* 214 */                   currentFold = currentFold.createChild(1, t.getOffset());
/*     */                 } 
/* 216 */                 inMLC = true;
/*     */ 
/*     */               
/*     */               }
/* 220 */               else if (this.language == 1 && t
/* 221 */                 .startsWith(JSP_COMMENT_START) && 
/* 222 */                 !t.endsWith(JSP_COMMENT_END)) {
/* 223 */                 if (currentFold == null) {
/* 224 */                   currentFold = new Fold(1, textArea, t.getOffset());
/* 225 */                   folds.add(currentFold);
/*     */                 } else {
/*     */                   
/* 228 */                   currentFold = currentFold.createChild(1, t.getOffset());
/*     */                 } 
/* 230 */                 inJSMLC = true;
/*     */               
/*     */               }
/*     */ 
/*     */             
/*     */             }
/* 236 */             else if (t.isSingleChar(25, '<')) {
/* 237 */               Token tagStartToken = t;
/* 238 */               Token tagNameToken = t.getNextToken();
/* 239 */               if (isFoldableTag(tagNameToken)) {
/* 240 */                 int newLine = getTagCloseInfo(tagNameToken, textArea, line, tci);
/* 241 */                 if (tci.line == -1) {
/* 242 */                   return folds;
/*     */                 }
/*     */ 
/*     */                 
/* 246 */                 Token tagCloseToken = tci.closeToken;
/* 247 */                 if (tagCloseToken.isSingleChar(25, '>')) {
/* 248 */                   if (currentFold == null) {
/*     */                     
/* 250 */                     currentFold = new Fold(0, textArea, tagStartToken.getOffset());
/* 251 */                     folds.add(currentFold);
/*     */                   } else {
/*     */                     
/* 254 */                     currentFold = currentFold.createChild(0, tagStartToken
/* 255 */                         .getOffset());
/*     */                   } 
/* 257 */                   tagNameStack.push(tagNameToken.getLexeme());
/*     */                 } 
/* 259 */                 t = tagCloseToken;
/* 260 */                 line = newLine;
/*     */               
/*     */               }
/*     */             
/*     */             }
/* 265 */             else if (t.is(25, MARKUP_CLOSING_TAG_START) && 
/* 266 */               currentFold != null) {
/* 267 */               Token tagNameToken = t.getNextToken();
/* 268 */               if (isFoldableTag(tagNameToken) && 
/* 269 */                 isEndOfLastFold(tagNameStack, tagNameToken)) {
/* 270 */                 tagNameStack.pop();
/* 271 */                 currentFold.setEndOffset(t.getOffset());
/* 272 */                 Fold parentFold = currentFold.getParent();
/*     */                 
/* 274 */                 if (currentFold.isOnSingleLine()) {
/* 275 */                   removeFold(currentFold, folds);
/*     */                 }
/* 277 */                 currentFold = parentFold;
/* 278 */                 t = tagNameToken;
/*     */               } 
/*     */             } 
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 285 */           t = t.getNextToken();
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 291 */     catch (BadLocationException ble) {
/* 292 */       ble.printStackTrace();
/*     */     } 
/*     */     
/* 295 */     return folds;
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
/*     */   private int getTagCloseInfo(Token tagNameToken, RSyntaxTextArea textArea, int line, TagCloseInfo info) {
/* 316 */     info.reset();
/* 317 */     Token t = tagNameToken.getNextToken();
/*     */ 
/*     */     
/*     */     while (true) {
/* 321 */       if (t != null && t.getType() != 25) {
/* 322 */         t = t.getNextToken();
/*     */         continue;
/*     */       } 
/* 325 */       if (t != null) {
/* 326 */         info.closeToken = t;
/* 327 */         info.line = line;
/*     */         
/*     */         break;
/*     */       } 
/* 331 */       if (++line >= textArea.getLineCount() || (
/* 332 */         t = textArea.getTokenListForLine(line)) == null)
/*     */         break; 
/* 334 */     }  return line;
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
/*     */   private static boolean isEndOfLastFold(Stack<String> tagNameStack, Token tagNameToken) {
/* 350 */     if (tagNameToken != null && tagNameToken.getLexeme() != null && !tagNameStack.isEmpty()) {
/* 351 */       return tagNameToken.getLexeme().equalsIgnoreCase(tagNameStack.peek());
/*     */     }
/* 353 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isFoldableTag(Token tagNameToken) {
/* 364 */     return (tagNameToken != null && tagNameToken.getLexeme() != null && FOLDABLE_TAGS
/* 365 */       .contains(tagNameToken.getLexeme().toLowerCase()));
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
/*     */   private static void removeFold(Fold fold, List<Fold> folds) {
/* 378 */     if (!fold.removeFromParent()) {
/* 379 */       folds.remove(folds.size() - 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class TagCloseInfo
/*     */   {
/*     */     private Token closeToken;
/*     */     
/*     */     private int line;
/*     */ 
/*     */     
/*     */     private TagCloseInfo() {}
/*     */     
/*     */     public void reset() {
/* 394 */       this.closeToken = null;
/* 395 */       this.line = -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 400 */       return "[TagCloseInfo: closeToken=" + this.closeToken + ", line=" + this.line + "]";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\folding\HtmlFoldParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */