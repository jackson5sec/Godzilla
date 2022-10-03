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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TokenMgrException
/*     */   extends RuntimeException
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public static final int LEXICAL_ERROR = 0;
/*     */   public static final int STATIC_LEXER_ERROR = 1;
/*     */   public static final int INVALID_LEXICAL_STATE = 2;
/*     */   public static final int LOOP_DETECTED = 3;
/*     */   int errorCode;
/*     */   
/*     */   protected static final String addEscapes(String str) {
/*  51 */     StringBuilder retval = new StringBuilder();
/*  52 */     for (int i = 0; i < str.length(); i++) {
/*  53 */       char ch = str.charAt(i);
/*  54 */       switch (ch) {
/*     */         
/*     */         case '\b':
/*  57 */           retval.append("\\b");
/*     */           break;
/*     */         case '\t':
/*  60 */           retval.append("\\t");
/*     */           break;
/*     */         case '\n':
/*  63 */           retval.append("\\n");
/*     */           break;
/*     */         case '\f':
/*  66 */           retval.append("\\f");
/*     */           break;
/*     */         case '\r':
/*  69 */           retval.append("\\r");
/*     */           break;
/*     */         case '"':
/*  72 */           retval.append("\\\"");
/*     */           break;
/*     */         case '\'':
/*  75 */           retval.append("\\'");
/*     */           break;
/*     */         case '\\':
/*  78 */           retval.append("\\\\");
/*     */           break;
/*     */         default:
/*  81 */           if (ch < ' ' || ch > '~') {
/*  82 */             String s = "0000" + Integer.toString(ch, 16);
/*  83 */             retval.append("\\u").append(s.substring(s.length() - 4, s.length())); break;
/*     */           } 
/*  85 */           retval.append(ch);
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/*  90 */     return retval.toString();
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
/*     */   protected static String LexicalErr(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, int curChar) {
/* 106 */     char curChar1 = (char)curChar;
/* 107 */     return "Lexical error at line " + errorLine + ", column " + errorColumn + ".  Encountered: " + (EOFSeen ? "<EOF> " : ("\"" + 
/*     */ 
/*     */       
/* 110 */       addEscapes(String.valueOf(curChar1)) + "\"" + " (" + curChar + "), ")) + "after : \"" + 
/* 111 */       addEscapes(errorAfter) + "\"";
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
/*     */   public String getMessage() {
/* 125 */     return super.getMessage();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenMgrException() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenMgrException(String message, int reason) {
/* 137 */     super(message);
/* 138 */     this.errorCode = reason;
/*     */   }
/*     */ 
/*     */   
/*     */   public TokenMgrException(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, int curChar, int reason) {
/* 143 */     this(LexicalErr(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar), reason);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\parser\TokenMgrException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */