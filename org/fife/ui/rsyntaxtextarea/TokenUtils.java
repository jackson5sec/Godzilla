/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import javax.swing.text.TabExpander;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TokenUtils
/*     */ {
/*     */   public static TokenSubList getSubTokenList(Token tokenList, int pos, TabExpander e, RSyntaxTextArea textArea, float x0) {
/*  61 */     return getSubTokenList(tokenList, pos, e, textArea, x0, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TokenSubList getSubTokenList(Token tokenList, int pos, TabExpander e, RSyntaxTextArea textArea, float x0, TokenImpl tempToken) {
/* 110 */     if (tempToken == null) {
/* 111 */       tempToken = new TokenImpl();
/*     */     }
/* 113 */     Token t = tokenList;
/*     */ 
/*     */ 
/*     */     
/* 117 */     while (t != null && t.isPaintable() && !t.containsPosition(pos)) {
/* 118 */       x0 += t.getWidth(textArea, e, x0);
/* 119 */       t = t.getNextToken();
/*     */     } 
/*     */ 
/*     */     
/* 123 */     if (t != null && t.isPaintable()) {
/*     */       
/* 125 */       if (t.getOffset() != pos) {
/*     */         
/* 127 */         int difference = pos - t.getOffset();
/* 128 */         x0 += t.getWidthUpTo(t.length() - difference + 1, textArea, e, x0);
/* 129 */         tempToken.copyFrom(t);
/* 130 */         tempToken.makeStartAt(pos);
/*     */         
/* 132 */         return new TokenSubList(tempToken, x0);
/*     */       } 
/*     */ 
/*     */       
/* 136 */       return new TokenSubList(t, x0);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     return new TokenSubList(tokenList, x0);
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
/*     */   public static int getWhiteSpaceTokenLength(Token t, int tabSize, int curOffs) {
/* 159 */     int length = 0;
/*     */     
/* 161 */     for (int i = 0; i < t.length(); i++) {
/* 162 */       char ch = t.charAt(i);
/* 163 */       if (ch == '\t') {
/* 164 */         int newCurOffs = (curOffs + tabSize) / tabSize * tabSize;
/* 165 */         length += newCurOffs - curOffs;
/* 166 */         curOffs = newCurOffs;
/*     */       } else {
/*     */         
/* 169 */         length++;
/* 170 */         curOffs++;
/*     */       } 
/*     */     } 
/*     */     
/* 174 */     return length;
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
/*     */   public static boolean isBlankOrAllWhiteSpace(Token t) {
/* 189 */     while (t != null && t.isPaintable()) {
/* 190 */       if (!t.isCommentOrWhitespace()) {
/* 191 */         return false;
/*     */       }
/* 193 */       t = t.getNextToken();
/*     */     } 
/* 195 */     return true;
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
/*     */   public static boolean isBlankOrAllWhiteSpaceWithoutComments(Token t) {
/* 210 */     while (t != null && t.isPaintable()) {
/* 211 */       if (!t.isWhitespace()) {
/* 212 */         return false;
/*     */       }
/* 214 */       t = t.getNextToken();
/*     */     } 
/* 216 */     return true;
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
/*     */   public static String tokenToHtml(RSyntaxTextArea textArea, Token token) {
/* 231 */     StringBuilder style = new StringBuilder();
/*     */     
/* 233 */     Font font = textArea.getFontForTokenType(token.getType());
/* 234 */     if (font.isBold()) {
/* 235 */       style.append("font-weight: bold;");
/*     */     }
/* 237 */     if (font.isItalic()) {
/* 238 */       style.append("font-style: italic;");
/*     */     }
/*     */     
/* 241 */     Color c = textArea.getForegroundForToken(token);
/* 242 */     style.append("color: ").append(HtmlUtil.getHexString(c)).append(";");
/*     */     
/* 244 */     return "<span style=\"" + style + "\">" + 
/* 245 */       HtmlUtil.escapeForHtml(token.getLexeme(), "\n", true) + "</span>";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class TokenSubList
/*     */   {
/*     */     public Token tokenList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public float x;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public TokenSubList(Token tokenList, float x) {
/* 268 */       this.tokenList = tokenList;
/* 269 */       this.x = x;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\TokenUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */