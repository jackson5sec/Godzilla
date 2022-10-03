/*     */ package org.springframework.expression.spel.standard;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.springframework.expression.spel.InternalParseException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.SpelParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Tokenizer
/*     */ {
/*  38 */   private static final String[] ALTERNATIVE_OPERATOR_NAMES = new String[] { "DIV", "EQ", "GE", "GT", "LE", "LT", "MOD", "NE", "NOT" };
/*     */ 
/*     */   
/*  41 */   private static final byte[] FLAGS = new byte[256]; private static final byte IS_DIGIT = 1; private static final byte IS_HEXDIGIT = 2;
/*     */   private static final byte IS_ALPHA = 4;
/*     */   private String expressionString;
/*     */   private char[] charsToProcess;
/*     */   private int pos;
/*     */   private int max;
/*     */   
/*     */   static {
/*     */     int ch;
/*  50 */     for (ch = 48; ch <= 57; ch++) {
/*  51 */       FLAGS[ch] = (byte)(FLAGS[ch] | 0x3);
/*     */     }
/*  53 */     for (ch = 65; ch <= 70; ch++) {
/*  54 */       FLAGS[ch] = (byte)(FLAGS[ch] | 0x2);
/*     */     }
/*  56 */     for (ch = 97; ch <= 102; ch++) {
/*  57 */       FLAGS[ch] = (byte)(FLAGS[ch] | 0x2);
/*     */     }
/*  59 */     for (ch = 65; ch <= 90; ch++) {
/*  60 */       FLAGS[ch] = (byte)(FLAGS[ch] | 0x4);
/*     */     }
/*  62 */     for (ch = 97; ch <= 122; ch++) {
/*  63 */       FLAGS[ch] = (byte)(FLAGS[ch] | 0x4);
/*     */     }
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
/*  76 */   private List<Token> tokens = new ArrayList<>();
/*     */ 
/*     */   
/*     */   public Tokenizer(String inputData) {
/*  80 */     this.expressionString = inputData;
/*  81 */     this.charsToProcess = (inputData + "\000").toCharArray();
/*  82 */     this.max = this.charsToProcess.length;
/*  83 */     this.pos = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Token> process() {
/*  88 */     while (this.pos < this.max) {
/*  89 */       char ch = this.charsToProcess[this.pos];
/*  90 */       if (isAlphabetic(ch)) {
/*  91 */         lexIdentifier();
/*     */         continue;
/*     */       } 
/*  94 */       switch (ch) {
/*     */         case '+':
/*  96 */           if (isTwoCharToken(TokenKind.INC)) {
/*  97 */             pushPairToken(TokenKind.INC);
/*     */             continue;
/*     */           } 
/* 100 */           pushCharToken(TokenKind.PLUS);
/*     */           continue;
/*     */         
/*     */         case '_':
/* 104 */           lexIdentifier();
/*     */           continue;
/*     */         case '-':
/* 107 */           if (isTwoCharToken(TokenKind.DEC)) {
/* 108 */             pushPairToken(TokenKind.DEC);
/*     */             continue;
/*     */           } 
/* 111 */           pushCharToken(TokenKind.MINUS);
/*     */           continue;
/*     */         
/*     */         case ':':
/* 115 */           pushCharToken(TokenKind.COLON);
/*     */           continue;
/*     */         case '.':
/* 118 */           pushCharToken(TokenKind.DOT);
/*     */           continue;
/*     */         case ',':
/* 121 */           pushCharToken(TokenKind.COMMA);
/*     */           continue;
/*     */         case '*':
/* 124 */           pushCharToken(TokenKind.STAR);
/*     */           continue;
/*     */         case '/':
/* 127 */           pushCharToken(TokenKind.DIV);
/*     */           continue;
/*     */         case '%':
/* 130 */           pushCharToken(TokenKind.MOD);
/*     */           continue;
/*     */         case '(':
/* 133 */           pushCharToken(TokenKind.LPAREN);
/*     */           continue;
/*     */         case ')':
/* 136 */           pushCharToken(TokenKind.RPAREN);
/*     */           continue;
/*     */         case '[':
/* 139 */           pushCharToken(TokenKind.LSQUARE);
/*     */           continue;
/*     */         case '#':
/* 142 */           pushCharToken(TokenKind.HASH);
/*     */           continue;
/*     */         case ']':
/* 145 */           pushCharToken(TokenKind.RSQUARE);
/*     */           continue;
/*     */         case '{':
/* 148 */           pushCharToken(TokenKind.LCURLY);
/*     */           continue;
/*     */         case '}':
/* 151 */           pushCharToken(TokenKind.RCURLY);
/*     */           continue;
/*     */         case '@':
/* 154 */           pushCharToken(TokenKind.BEAN_REF);
/*     */           continue;
/*     */         case '^':
/* 157 */           if (isTwoCharToken(TokenKind.SELECT_FIRST)) {
/* 158 */             pushPairToken(TokenKind.SELECT_FIRST);
/*     */             continue;
/*     */           } 
/* 161 */           pushCharToken(TokenKind.POWER);
/*     */           continue;
/*     */         
/*     */         case '!':
/* 165 */           if (isTwoCharToken(TokenKind.NE)) {
/* 166 */             pushPairToken(TokenKind.NE); continue;
/*     */           } 
/* 168 */           if (isTwoCharToken(TokenKind.PROJECT)) {
/* 169 */             pushPairToken(TokenKind.PROJECT);
/*     */             continue;
/*     */           } 
/* 172 */           pushCharToken(TokenKind.NOT);
/*     */           continue;
/*     */         
/*     */         case '=':
/* 176 */           if (isTwoCharToken(TokenKind.EQ)) {
/* 177 */             pushPairToken(TokenKind.EQ);
/*     */             continue;
/*     */           } 
/* 180 */           pushCharToken(TokenKind.ASSIGN);
/*     */           continue;
/*     */         
/*     */         case '&':
/* 184 */           if (isTwoCharToken(TokenKind.SYMBOLIC_AND)) {
/* 185 */             pushPairToken(TokenKind.SYMBOLIC_AND);
/*     */             continue;
/*     */           } 
/* 188 */           pushCharToken(TokenKind.FACTORY_BEAN_REF);
/*     */           continue;
/*     */         
/*     */         case '|':
/* 192 */           if (!isTwoCharToken(TokenKind.SYMBOLIC_OR)) {
/* 193 */             raiseParseException(this.pos, SpelMessage.MISSING_CHARACTER, new Object[] { "|" });
/*     */           }
/* 195 */           pushPairToken(TokenKind.SYMBOLIC_OR);
/*     */           continue;
/*     */         case '?':
/* 198 */           if (isTwoCharToken(TokenKind.SELECT)) {
/* 199 */             pushPairToken(TokenKind.SELECT); continue;
/*     */           } 
/* 201 */           if (isTwoCharToken(TokenKind.ELVIS)) {
/* 202 */             pushPairToken(TokenKind.ELVIS); continue;
/*     */           } 
/* 204 */           if (isTwoCharToken(TokenKind.SAFE_NAVI)) {
/* 205 */             pushPairToken(TokenKind.SAFE_NAVI);
/*     */             continue;
/*     */           } 
/* 208 */           pushCharToken(TokenKind.QMARK);
/*     */           continue;
/*     */         
/*     */         case '$':
/* 212 */           if (isTwoCharToken(TokenKind.SELECT_LAST)) {
/* 213 */             pushPairToken(TokenKind.SELECT_LAST);
/*     */             continue;
/*     */           } 
/* 216 */           lexIdentifier();
/*     */           continue;
/*     */         
/*     */         case '>':
/* 220 */           if (isTwoCharToken(TokenKind.GE)) {
/* 221 */             pushPairToken(TokenKind.GE);
/*     */             continue;
/*     */           } 
/* 224 */           pushCharToken(TokenKind.GT);
/*     */           continue;
/*     */         
/*     */         case '<':
/* 228 */           if (isTwoCharToken(TokenKind.LE)) {
/* 229 */             pushPairToken(TokenKind.LE);
/*     */             continue;
/*     */           } 
/* 232 */           pushCharToken(TokenKind.LT);
/*     */           continue;
/*     */         
/*     */         case '0':
/*     */         case '1':
/*     */         case '2':
/*     */         case '3':
/*     */         case '4':
/*     */         case '5':
/*     */         case '6':
/*     */         case '7':
/*     */         case '8':
/*     */         case '9':
/* 245 */           lexNumericLiteral((ch == '0'));
/*     */           continue;
/*     */         
/*     */         case '\t':
/*     */         case '\n':
/*     */         case '\r':
/*     */         case ' ':
/* 252 */           this.pos++;
/*     */           continue;
/*     */         case '\'':
/* 255 */           lexQuotedStringLiteral();
/*     */           continue;
/*     */         case '"':
/* 258 */           lexDoubleQuotedStringLiteral();
/*     */           continue;
/*     */         
/*     */         case '\000':
/* 262 */           this.pos++;
/*     */           continue;
/*     */         case '\\':
/* 265 */           raiseParseException(this.pos, SpelMessage.UNEXPECTED_ESCAPE_CHAR, new Object[0]);
/*     */           continue;
/*     */       } 
/* 268 */       throw new IllegalStateException("Cannot handle (" + ch + ") '" + ch + "'");
/*     */     } 
/*     */ 
/*     */     
/* 272 */     return this.tokens;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void lexQuotedStringLiteral() {
/* 278 */     int start = this.pos;
/* 279 */     boolean terminated = false;
/* 280 */     while (!terminated) {
/* 281 */       this.pos++;
/* 282 */       char ch = this.charsToProcess[this.pos];
/* 283 */       if (ch == '\'')
/*     */       {
/* 285 */         if (this.charsToProcess[this.pos + 1] == '\'') {
/* 286 */           this.pos++;
/*     */         } else {
/*     */           
/* 289 */           terminated = true;
/*     */         } 
/*     */       }
/* 292 */       if (isExhausted()) {
/* 293 */         raiseParseException(start, SpelMessage.NON_TERMINATING_QUOTED_STRING, new Object[0]);
/*     */       }
/*     */     } 
/* 296 */     this.pos++;
/* 297 */     this.tokens.add(new Token(TokenKind.LITERAL_STRING, subarray(start, this.pos), start, this.pos));
/*     */   }
/*     */ 
/*     */   
/*     */   private void lexDoubleQuotedStringLiteral() {
/* 302 */     int start = this.pos;
/* 303 */     boolean terminated = false;
/* 304 */     while (!terminated) {
/* 305 */       this.pos++;
/* 306 */       char ch = this.charsToProcess[this.pos];
/* 307 */       if (ch == '"')
/*     */       {
/* 309 */         if (this.charsToProcess[this.pos + 1] == '"') {
/* 310 */           this.pos++;
/*     */         } else {
/*     */           
/* 313 */           terminated = true;
/*     */         } 
/*     */       }
/* 316 */       if (isExhausted()) {
/* 317 */         raiseParseException(start, SpelMessage.NON_TERMINATING_DOUBLE_QUOTED_STRING, new Object[0]);
/*     */       }
/*     */     } 
/* 320 */     this.pos++;
/* 321 */     this.tokens.add(new Token(TokenKind.LITERAL_STRING, subarray(start, this.pos), start, this.pos));
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
/*     */   private void lexNumericLiteral(boolean firstCharIsZero) {
/* 341 */     boolean isReal = false;
/* 342 */     int start = this.pos;
/* 343 */     char ch = this.charsToProcess[this.pos + 1];
/* 344 */     boolean isHex = (ch == 'x' || ch == 'X');
/*     */ 
/*     */     
/* 347 */     if (firstCharIsZero && isHex) {
/* 348 */       this.pos++;
/*     */       while (true) {
/* 350 */         this.pos++;
/*     */         
/* 352 */         if (!isHexadecimalDigit(this.charsToProcess[this.pos])) {
/* 353 */           if (isChar('L', 'l')) {
/* 354 */             pushHexIntToken(subarray(start + 2, this.pos), true, start, this.pos);
/* 355 */             this.pos++;
/*     */           } else {
/*     */             
/* 358 */             pushHexIntToken(subarray(start + 2, this.pos), false, start, this.pos);
/*     */           } 
/*     */           
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*     */     do {
/* 367 */       this.pos++;
/*     */     }
/* 369 */     while (isDigit(this.charsToProcess[this.pos]));
/*     */ 
/*     */     
/* 372 */     ch = this.charsToProcess[this.pos];
/* 373 */     if (ch == '.') {
/* 374 */       isReal = true;
/* 375 */       int dotpos = this.pos;
/*     */       
/*     */       while (true) {
/* 378 */         this.pos++;
/*     */         
/* 380 */         if (!isDigit(this.charsToProcess[this.pos])) {
/* 381 */           if (this.pos == dotpos + 1) {
/*     */ 
/*     */ 
/*     */             
/* 385 */             this.pos = dotpos;
/* 386 */             pushIntToken(subarray(start, this.pos), false, start, this.pos); return;
/*     */           }  break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 391 */     int endOfNumber = this.pos;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 396 */     if (isChar('L', 'l')) {
/* 397 */       if (isReal) {
/* 398 */         raiseParseException(start, SpelMessage.REAL_CANNOT_BE_LONG, new Object[0]);
/*     */       }
/* 400 */       pushIntToken(subarray(start, endOfNumber), true, start, endOfNumber);
/* 401 */       this.pos++;
/*     */     } else {
/* 403 */       if (isExponentChar(this.charsToProcess[this.pos])) {
/* 404 */         isReal = true;
/* 405 */         this.pos++;
/* 406 */         char possibleSign = this.charsToProcess[this.pos];
/* 407 */         if (isSign(possibleSign)) {
/* 408 */           this.pos++;
/*     */         }
/*     */ 
/*     */         
/*     */         while (true) {
/* 413 */           this.pos++;
/*     */           
/* 415 */           if (!isDigit(this.charsToProcess[this.pos])) {
/* 416 */             boolean bool = false;
/* 417 */             if (isFloatSuffix(this.charsToProcess[this.pos])) {
/* 418 */               bool = true;
/* 419 */               endOfNumber = ++this.pos;
/*     */             }
/* 421 */             else if (isDoubleSuffix(this.charsToProcess[this.pos])) {
/* 422 */               endOfNumber = ++this.pos;
/*     */             } 
/* 424 */             pushRealToken(subarray(start, this.pos), bool, start, this.pos); return;
/*     */           } 
/*     */         } 
/* 427 */       }  ch = this.charsToProcess[this.pos];
/* 428 */       boolean isFloat = false;
/* 429 */       if (isFloatSuffix(ch)) {
/* 430 */         isReal = true;
/* 431 */         isFloat = true;
/* 432 */         endOfNumber = ++this.pos;
/*     */       }
/* 434 */       else if (isDoubleSuffix(ch)) {
/* 435 */         isReal = true;
/* 436 */         endOfNumber = ++this.pos;
/*     */       } 
/* 438 */       if (isReal) {
/* 439 */         pushRealToken(subarray(start, endOfNumber), isFloat, start, endOfNumber);
/*     */       } else {
/*     */         
/* 442 */         pushIntToken(subarray(start, endOfNumber), false, start, endOfNumber);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void lexIdentifier() {
/* 448 */     int start = this.pos;
/*     */     while (true) {
/* 450 */       this.pos++;
/*     */       
/* 452 */       if (!isIdentifier(this.charsToProcess[this.pos])) {
/* 453 */         char[] subarray = subarray(start, this.pos);
/*     */ 
/*     */ 
/*     */         
/* 457 */         if (this.pos - start == 2 || this.pos - start == 3) {
/* 458 */           String asString = (new String(subarray)).toUpperCase();
/* 459 */           int idx = Arrays.binarySearch((Object[])ALTERNATIVE_OPERATOR_NAMES, asString);
/* 460 */           if (idx >= 0) {
/* 461 */             pushOneCharOrTwoCharToken(TokenKind.valueOf(asString), start, subarray);
/*     */             return;
/*     */           } 
/*     */         } 
/* 465 */         this.tokens.add(new Token(TokenKind.IDENTIFIER, subarray, start, this.pos));
/*     */         return;
/*     */       } 
/*     */     }  } private void pushIntToken(char[] data, boolean isLong, int start, int end) {
/* 469 */     if (isLong) {
/* 470 */       this.tokens.add(new Token(TokenKind.LITERAL_LONG, data, start, end));
/*     */     } else {
/*     */       
/* 473 */       this.tokens.add(new Token(TokenKind.LITERAL_INT, data, start, end));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void pushHexIntToken(char[] data, boolean isLong, int start, int end) {
/* 478 */     if (data.length == 0) {
/* 479 */       if (isLong) {
/* 480 */         raiseParseException(start, SpelMessage.NOT_A_LONG, new Object[] { this.expressionString.substring(start, end + 1) });
/*     */       } else {
/*     */         
/* 483 */         raiseParseException(start, SpelMessage.NOT_AN_INTEGER, new Object[] { this.expressionString.substring(start, end) });
/*     */       } 
/*     */     }
/* 486 */     if (isLong) {
/* 487 */       this.tokens.add(new Token(TokenKind.LITERAL_HEXLONG, data, start, end));
/*     */     } else {
/*     */       
/* 490 */       this.tokens.add(new Token(TokenKind.LITERAL_HEXINT, data, start, end));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void pushRealToken(char[] data, boolean isFloat, int start, int end) {
/* 495 */     if (isFloat) {
/* 496 */       this.tokens.add(new Token(TokenKind.LITERAL_REAL_FLOAT, data, start, end));
/*     */     } else {
/*     */       
/* 499 */       this.tokens.add(new Token(TokenKind.LITERAL_REAL, data, start, end));
/*     */     } 
/*     */   }
/*     */   
/*     */   private char[] subarray(int start, int end) {
/* 504 */     return Arrays.copyOfRange(this.charsToProcess, start, end);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isTwoCharToken(TokenKind kind) {
/* 511 */     return (kind.tokenChars.length == 2 && this.charsToProcess[this.pos] == kind.tokenChars[0] && this.charsToProcess[this.pos + 1] == kind.tokenChars[1]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void pushCharToken(TokenKind kind) {
/* 520 */     this.tokens.add(new Token(kind, this.pos, this.pos + 1));
/* 521 */     this.pos++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void pushPairToken(TokenKind kind) {
/* 528 */     this.tokens.add(new Token(kind, this.pos, this.pos + 2));
/* 529 */     this.pos += 2;
/*     */   }
/*     */   
/*     */   private void pushOneCharOrTwoCharToken(TokenKind kind, int pos, char[] data) {
/* 533 */     this.tokens.add(new Token(kind, data, pos, pos + kind.getLength()));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isIdentifier(char ch) {
/* 538 */     return (isAlphabetic(ch) || isDigit(ch) || ch == '_' || ch == '$');
/*     */   }
/*     */   
/*     */   private boolean isChar(char a, char b) {
/* 542 */     char ch = this.charsToProcess[this.pos];
/* 543 */     return (ch == a || ch == b);
/*     */   }
/*     */   
/*     */   private boolean isExponentChar(char ch) {
/* 547 */     return (ch == 'e' || ch == 'E');
/*     */   }
/*     */   
/*     */   private boolean isFloatSuffix(char ch) {
/* 551 */     return (ch == 'f' || ch == 'F');
/*     */   }
/*     */   
/*     */   private boolean isDoubleSuffix(char ch) {
/* 555 */     return (ch == 'd' || ch == 'D');
/*     */   }
/*     */   
/*     */   private boolean isSign(char ch) {
/* 559 */     return (ch == '+' || ch == '-');
/*     */   }
/*     */   
/*     */   private boolean isDigit(char ch) {
/* 563 */     if (ch > 'ÿ') {
/* 564 */       return false;
/*     */     }
/* 566 */     return ((FLAGS[ch] & 0x1) != 0);
/*     */   }
/*     */   
/*     */   private boolean isAlphabetic(char ch) {
/* 570 */     if (ch > 'ÿ') {
/* 571 */       return false;
/*     */     }
/* 573 */     return ((FLAGS[ch] & 0x4) != 0);
/*     */   }
/*     */   
/*     */   private boolean isHexadecimalDigit(char ch) {
/* 577 */     if (ch > 'ÿ') {
/* 578 */       return false;
/*     */     }
/* 580 */     return ((FLAGS[ch] & 0x2) != 0);
/*     */   }
/*     */   
/*     */   private boolean isExhausted() {
/* 584 */     return (this.pos == this.max - 1);
/*     */   }
/*     */   
/*     */   private void raiseParseException(int start, SpelMessage msg, Object... inserts) {
/* 588 */     throw new InternalParseException(new SpelParseException(this.expressionString, start, msg, inserts));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\standard\Tokenizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */