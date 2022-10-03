/*     */ package com.kitfox.svg.animation.parser;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParseException
/*     */   extends Exception
/*     */ {
/*  18 */   protected static final String EOL = System.getProperty("line.separator", "\n");
/*     */ 
/*     */   
/*     */   public Token currentToken;
/*     */ 
/*     */   
/*     */   public int[][] expectedTokenSequences;
/*     */   
/*     */   public String[] tokenImage;
/*     */ 
/*     */   
/*     */   public ParseException(Token currentTokenVal, int[][] expectedTokenSequencesVal, String[] tokenImageVal) {
/*  30 */     super(_initialise(currentTokenVal, expectedTokenSequencesVal, tokenImageVal));
/*  31 */     this.currentToken = currentTokenVal;
/*  32 */     this.expectedTokenSequences = expectedTokenSequencesVal;
/*  33 */     this.tokenImage = tokenImageVal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParseException() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParseException(String message) {
/*  52 */     super(message);
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
/*     */   private static String _initialise(Token currentToken, int[][] expectedTokenSequences, String[] tokenImage) {
/*  88 */     StringBuilder expected = new StringBuilder();
/*  89 */     int maxSize = 0;
/*  90 */     for (int i = 0; i < expectedTokenSequences.length; i++) {
/*  91 */       if (maxSize < (expectedTokenSequences[i]).length)
/*  92 */         maxSize = (expectedTokenSequences[i]).length; 
/*  93 */       for (int k = 0; k < (expectedTokenSequences[i]).length; k++) {
/*  94 */         expected.append(tokenImage[expectedTokenSequences[i][k]]).append(' ');
/*     */       }
/*  96 */       if (expectedTokenSequences[i][(expectedTokenSequences[i]).length - 1] != 0)
/*  97 */         expected.append("..."); 
/*  98 */       expected.append(EOL).append("    ");
/*     */     } 
/*     */     
/* 101 */     StringBuilder sb = new StringBuilder();
/* 102 */     sb.append("Encountered \"");
/*     */     
/* 104 */     Token tok = currentToken.next;
/* 105 */     for (int j = 0; j < maxSize; j++) {
/* 106 */       String tokenText = tok.image;
/* 107 */       String escapedTokenText = add_escapes(tokenText);
/* 108 */       if (j != 0)
/* 109 */         sb.append(' '); 
/* 110 */       if (tok.kind == 0) {
/* 111 */         sb.append(tokenImage[0]);
/*     */         break;
/*     */       } 
/* 114 */       sb.append(" " + tokenImage[tok.kind]);
/* 115 */       sb.append(" \"");
/* 116 */       sb.append(escapedTokenText);
/* 117 */       sb.append("\"");
/* 118 */       tok = tok.next;
/*     */     } 
/* 120 */     sb.append("\" at line ")
/* 121 */       .append(currentToken.next.beginLine)
/* 122 */       .append(", column ")
/* 123 */       .append(currentToken.next.beginColumn);
/* 124 */     sb.append(".").append(EOL);
/*     */     
/* 126 */     if (expectedTokenSequences.length != 0)
/*     */     {
/*     */       
/* 129 */       sb.append(EOL)
/* 130 */         .append("Was expecting")
/* 131 */         .append((expectedTokenSequences.length == 1) ? ":" : " one of:")
/* 132 */         .append(EOL)
/* 133 */         .append(EOL)
/* 134 */         .append(expected);
/*     */     }
/*     */     
/* 137 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String add_escapes(String str) {
/* 147 */     StringBuilder retval = new StringBuilder();
/* 148 */     for (int i = 0; i < str.length(); i++) {
/* 149 */       char ch = str.charAt(i);
/* 150 */       switch (ch) {
/*     */         
/*     */         case '\b':
/* 153 */           retval.append("\\b");
/*     */           break;
/*     */         case '\t':
/* 156 */           retval.append("\\t");
/*     */           break;
/*     */         case '\n':
/* 159 */           retval.append("\\n");
/*     */           break;
/*     */         case '\f':
/* 162 */           retval.append("\\f");
/*     */           break;
/*     */         case '\r':
/* 165 */           retval.append("\\r");
/*     */           break;
/*     */         case '"':
/* 168 */           retval.append("\\\"");
/*     */           break;
/*     */         case '\'':
/* 171 */           retval.append("\\'");
/*     */           break;
/*     */         case '\\':
/* 174 */           retval.append("\\\\");
/*     */           break;
/*     */         default:
/* 177 */           if (ch < ' ' || ch > '~') {
/* 178 */             String s = "0000" + Integer.toString(ch, 16);
/* 179 */             retval.append("\\u").append(s.substring(s.length() - 4, s.length())); break;
/*     */           } 
/* 181 */           retval.append(ch);
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 186 */     return retval.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\parser\ParseException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */