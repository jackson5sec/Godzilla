/*     */ package org.fife.ui.rsyntaxtextarea;
/*     */ 
/*     */ import java.awt.Color;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class HtmlUtil
/*     */ {
/*     */   public static String escapeForHtml(String s, String newlineReplacement, boolean inPreBlock) {
/*  31 */     if (newlineReplacement == null) {
/*  32 */       newlineReplacement = "";
/*     */     }
/*  34 */     String tabString = inPreBlock ? "    " : "&nbsp;&nbsp;&nbsp;&nbsp;";
/*     */     
/*  36 */     StringBuilder sb = new StringBuilder();
/*     */     
/*  38 */     for (int i = 0; i < s.length(); i++) {
/*  39 */       char ch = s.charAt(i);
/*  40 */       switch (ch) {
/*     */         case ' ':
/*  42 */           if (inPreBlock) {
/*  43 */             sb.append(' ');
/*     */             break;
/*     */           } 
/*  46 */           sb.append("&nbsp;");
/*     */           break;
/*     */         
/*     */         case '\n':
/*  50 */           sb.append(newlineReplacement);
/*     */           break;
/*     */         case '&':
/*  53 */           sb.append("&amp;");
/*     */           break;
/*     */         case '\t':
/*  56 */           sb.append(tabString);
/*     */           break;
/*     */         case '<':
/*  59 */           sb.append("&lt;");
/*     */           break;
/*     */         case '>':
/*  62 */           sb.append("&gt;");
/*     */           break;
/*     */         default:
/*  65 */           sb.append(ch);
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/*  70 */     return sb.toString();
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
/*     */   public static String getHexString(Color c) {
/*  83 */     if (c == null) {
/*  84 */       return null;
/*     */     }
/*     */     
/*  87 */     StringBuilder sb = new StringBuilder("#");
/*     */     
/*  89 */     int r = c.getRed();
/*  90 */     if (r < 16) {
/*  91 */       sb.append('0');
/*     */     }
/*  93 */     sb.append(Integer.toHexString(r));
/*  94 */     int g = c.getGreen();
/*  95 */     if (g < 16) {
/*  96 */       sb.append('0');
/*     */     }
/*  98 */     sb.append(Integer.toHexString(g));
/*  99 */     int b = c.getBlue();
/* 100 */     if (b < 16) {
/* 101 */       sb.append('0');
/*     */     }
/* 103 */     sb.append(Integer.toHexString(b));
/*     */     
/* 105 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getTextAsHtml(RSyntaxTextArea textArea, int start, int end) {
/* 112 */     StringBuilder sb = (new StringBuilder("<pre style='")).append("font-family: \"").append(textArea.getFont().getFamily()).append("\", courier;");
/* 113 */     if (textArea.getBackground() != null) {
/* 114 */       sb.append(" background: ")
/* 115 */         .append(getHexString(textArea.getBackground()))
/* 116 */         .append("'>");
/*     */     }
/*     */     
/* 119 */     Token token = textArea.getTokenListFor(start, end);
/* 120 */     for (Token t = token; t != null; t = t.getNextToken()) {
/*     */       
/* 122 */       if (t.isPaintable())
/*     */       {
/* 124 */         if (t.isSingleChar('\n')) {
/* 125 */           sb.append("<br>");
/*     */         } else {
/*     */           
/* 128 */           sb.append(TokenUtils.tokenToHtml(textArea, t));
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 133 */     sb.append("</pre>");
/* 134 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\HtmlUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */