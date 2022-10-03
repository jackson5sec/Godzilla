/*     */ package javassist.compiler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Lex
/*     */   implements TokenId
/*     */ {
/*     */   private int lastChar;
/*     */   private StringBuffer textBuffer;
/*     */   private Token currentToken;
/*     */   private Token lookAheadTokens;
/*     */   private String input;
/*     */   private int position;
/*     */   private int maxlen;
/*     */   private int lineNumber;
/*     */   
/*     */   public Lex(String s) {
/*  42 */     this.lastChar = -1;
/*  43 */     this.textBuffer = new StringBuffer();
/*  44 */     this.currentToken = new Token();
/*  45 */     this.lookAheadTokens = null;
/*     */     
/*  47 */     this.input = s;
/*  48 */     this.position = 0;
/*  49 */     this.maxlen = s.length();
/*  50 */     this.lineNumber = 0;
/*     */   }
/*     */   
/*     */   public int get() {
/*  54 */     if (this.lookAheadTokens == null) {
/*  55 */       return get(this.currentToken);
/*     */     }
/*  57 */     Token t = this.lookAheadTokens;
/*  58 */     this.lookAheadTokens = this.lookAheadTokens.next;
/*  59 */     return t.tokenId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int lookAhead() {
/*  66 */     return lookAhead(0);
/*     */   }
/*     */   
/*     */   public int lookAhead(int i) {
/*  70 */     Token tk = this.lookAheadTokens;
/*  71 */     if (tk == null) {
/*  72 */       this.lookAheadTokens = tk = this.currentToken;
/*  73 */       tk.next = null;
/*  74 */       get(tk);
/*     */     } 
/*     */     
/*  77 */     for (; i-- > 0; tk = tk.next) {
/*  78 */       if (tk.next == null) {
/*     */         
/*  80 */         Token tk2 = new Token();
/*  81 */         get(tk2);
/*     */       } 
/*     */     } 
/*  84 */     this.currentToken = tk;
/*  85 */     return tk.tokenId;
/*     */   }
/*     */   
/*     */   public String getString() {
/*  89 */     return this.currentToken.textValue;
/*     */   }
/*     */   
/*     */   public long getLong() {
/*  93 */     return this.currentToken.longValue;
/*     */   }
/*     */   
/*     */   public double getDouble() {
/*  97 */     return this.currentToken.doubleValue;
/*     */   }
/*     */ 
/*     */   
/*     */   private int get(Token token) {
/*     */     while (true) {
/* 103 */       int t = readLine(token);
/* 104 */       if (t != 10) {
/* 105 */         token.tokenId = t;
/* 106 */         return t;
/*     */       } 
/*     */     } 
/*     */   } private int readLine(Token token) {
/* 110 */     int c = getNextNonWhiteChar();
/* 111 */     if (c < 0)
/* 112 */       return c; 
/* 113 */     if (c == 10) {
/* 114 */       this.lineNumber++;
/* 115 */       return 10;
/*     */     } 
/* 117 */     if (c == 39)
/* 118 */       return readCharConst(token); 
/* 119 */     if (c == 34)
/* 120 */       return readStringL(token); 
/* 121 */     if (48 <= c && c <= 57)
/* 122 */       return readNumber(c, token); 
/* 123 */     if (c == 46) {
/* 124 */       c = getc();
/* 125 */       if (48 <= c && c <= 57) {
/* 126 */         StringBuffer tbuf = this.textBuffer;
/* 127 */         tbuf.setLength(0);
/* 128 */         tbuf.append('.');
/* 129 */         return readDouble(tbuf, c, token);
/*     */       } 
/* 131 */       ungetc(c);
/* 132 */       return readSeparator(46);
/*     */     } 
/* 134 */     if (Character.isJavaIdentifierStart((char)c))
/* 135 */       return readIdentifier(c, token); 
/* 136 */     return readSeparator(c);
/*     */   }
/*     */ 
/*     */   
/*     */   private int getNextNonWhiteChar() {
/*     */     while (true) {
/* 142 */       int c = getc();
/* 143 */       if (c == 47) {
/* 144 */         c = getc();
/* 145 */         if (c == 47)
/*     */         { do {
/* 147 */             c = getc();
/* 148 */           } while (c != 10 && c != 13 && c != -1); }
/* 149 */         else if (c == 42)
/*     */         { while (true) {
/* 151 */             c = getc();
/* 152 */             if (c == -1)
/*     */               break; 
/* 154 */             if (c == 42) {
/* 155 */               if ((c = getc()) == 47) {
/* 156 */                 c = 32;
/*     */                 
/*     */                 break;
/*     */               } 
/* 160 */               ungetc(c);
/*     */             } 
/*     */           }  }
/* 163 */         else { ungetc(c);
/* 164 */           c = 47; }
/*     */       
/*     */       } 
/* 167 */       if (!isBlank(c))
/* 168 */         return c; 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int readCharConst(Token token) {
/* 173 */     int value = 0; int c;
/* 174 */     while ((c = getc()) != 39) {
/* 175 */       if (c == 92) {
/* 176 */         value = readEscapeChar(); continue;
/* 177 */       }  if (c < 32) {
/* 178 */         if (c == 10) {
/* 179 */           this.lineNumber++;
/*     */         }
/* 181 */         return 500;
/*     */       } 
/*     */       
/* 184 */       value = c;
/*     */     } 
/* 186 */     token.longValue = value;
/* 187 */     return 401;
/*     */   }
/*     */   
/*     */   private int readEscapeChar() {
/* 191 */     int c = getc();
/* 192 */     if (c == 110) {
/* 193 */       c = 10;
/* 194 */     } else if (c == 116) {
/* 195 */       c = 9;
/* 196 */     } else if (c == 114) {
/* 197 */       c = 13;
/* 198 */     } else if (c == 102) {
/* 199 */       c = 12;
/* 200 */     } else if (c == 10) {
/* 201 */       this.lineNumber++;
/*     */     } 
/* 203 */     return c;
/*     */   }
/*     */ 
/*     */   
/*     */   private int readStringL(Token token) {
/* 208 */     StringBuffer tbuf = this.textBuffer;
/* 209 */     tbuf.setLength(0); while (true) {
/*     */       int c;
/* 211 */       while ((c = getc()) != 34) {
/* 212 */         if (c == 92) {
/* 213 */           c = readEscapeChar();
/* 214 */         } else if (c == 10 || c < 0) {
/* 215 */           this.lineNumber++;
/* 216 */           return 500;
/*     */         } 
/*     */         
/* 219 */         tbuf.append((char)c);
/*     */       } 
/*     */       
/*     */       while (true) {
/* 223 */         c = getc();
/* 224 */         if (c == 10) {
/* 225 */           this.lineNumber++; continue;
/* 226 */         }  if (!isBlank(c)) {
/*     */           break;
/*     */         }
/*     */       } 
/* 230 */       if (c != 34) {
/* 231 */         ungetc(c);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 236 */         token.textValue = tbuf.toString();
/* 237 */         return 406;
/*     */       } 
/*     */     } 
/*     */   } private int readNumber(int c, Token token) {
/* 241 */     long value = 0L;
/* 242 */     int c2 = getc();
/* 243 */     if (c == 48) {
/* 244 */       if (c2 == 88 || c2 == 120) {
/*     */         while (true) {
/* 246 */           c = getc();
/* 247 */           if (48 <= c && c <= 57) {
/* 248 */             value = value * 16L + (c - 48); continue;
/* 249 */           }  if (65 <= c && c <= 70) {
/* 250 */             value = value * 16L + (c - 65 + 10); continue;
/* 251 */           }  if (97 <= c && c <= 102) {
/* 252 */             value = value * 16L + (c - 97 + 10); continue;
/*     */           }  break;
/* 254 */         }  token.longValue = value;
/* 255 */         if (c == 76 || c == 108)
/* 256 */           return 403; 
/* 257 */         ungetc(c);
/* 258 */         return 402;
/*     */       } 
/*     */       
/* 261 */       if (48 <= c2 && c2 <= 55) {
/* 262 */         value = (c2 - 48);
/*     */         while (true) {
/* 264 */           c = getc();
/* 265 */           if (48 <= c && c <= 55) {
/* 266 */             value = value * 8L + (c - 48); continue;
/*     */           }  break;
/* 268 */         }  token.longValue = value;
/* 269 */         if (c == 76 || c == 108)
/* 270 */           return 403; 
/* 271 */         ungetc(c);
/* 272 */         return 402;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 277 */     value = (c - 48);
/* 278 */     while (48 <= c2 && c2 <= 57) {
/* 279 */       value = value * 10L + c2 - 48L;
/* 280 */       c2 = getc();
/*     */     } 
/*     */     
/* 283 */     token.longValue = value;
/* 284 */     if (c2 == 70 || c2 == 102) {
/* 285 */       token.doubleValue = value;
/* 286 */       return 404;
/*     */     } 
/* 288 */     if (c2 == 69 || c2 == 101 || c2 == 68 || c2 == 100 || c2 == 46) {
/*     */       
/* 290 */       StringBuffer tbuf = this.textBuffer;
/* 291 */       tbuf.setLength(0);
/* 292 */       tbuf.append(value);
/* 293 */       return readDouble(tbuf, c2, token);
/*     */     } 
/* 295 */     if (c2 == 76 || c2 == 108) {
/* 296 */       return 403;
/*     */     }
/* 298 */     ungetc(c2);
/* 299 */     return 402;
/*     */   }
/*     */ 
/*     */   
/*     */   private int readDouble(StringBuffer sbuf, int c, Token token) {
/* 304 */     if (c != 69 && c != 101 && c != 68 && c != 100) {
/* 305 */       sbuf.append((char)c);
/*     */       while (true) {
/* 307 */         c = getc();
/* 308 */         if (48 <= c && c <= 57) {
/* 309 */           sbuf.append((char)c);
/*     */           continue;
/*     */         } 
/*     */         break;
/*     */       } 
/*     */     } 
/* 315 */     if (c == 69 || c == 101) {
/* 316 */       sbuf.append((char)c);
/* 317 */       c = getc();
/* 318 */       if (c == 43 || c == 45) {
/* 319 */         sbuf.append((char)c);
/* 320 */         c = getc();
/*     */       } 
/*     */       
/* 323 */       while (48 <= c && c <= 57) {
/* 324 */         sbuf.append((char)c);
/* 325 */         c = getc();
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 330 */       token.doubleValue = Double.parseDouble(sbuf.toString());
/*     */     }
/* 332 */     catch (NumberFormatException e) {
/* 333 */       return 500;
/*     */     } 
/*     */     
/* 336 */     if (c == 70 || c == 102)
/* 337 */       return 404; 
/* 338 */     if (c != 68 && c != 100) {
/* 339 */       ungetc(c);
/*     */     }
/* 341 */     return 405;
/*     */   }
/*     */ 
/*     */   
/* 345 */   private static final int[] equalOps = new int[] { 350, 0, 0, 0, 351, 352, 0, 0, 0, 353, 354, 0, 355, 0, 356, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 357, 358, 359, 0 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int readSeparator(int c) {
/*     */     int c2;
/* 353 */     if (33 <= c && c <= 63) {
/* 354 */       int t = equalOps[c - 33];
/* 355 */       if (t == 0)
/* 356 */         return c; 
/* 357 */       c2 = getc();
/* 358 */       if (c == c2) {
/* 359 */         int c3; switch (c) {
/*     */           case 61:
/* 361 */             return 358;
/*     */           case 43:
/* 363 */             return 362;
/*     */           case 45:
/* 365 */             return 363;
/*     */           case 38:
/* 367 */             return 369;
/*     */           case 60:
/* 369 */             c3 = getc();
/* 370 */             if (c3 == 61)
/* 371 */               return 365; 
/* 372 */             ungetc(c3);
/* 373 */             return 364;
/*     */           case 62:
/* 375 */             c3 = getc();
/* 376 */             if (c3 == 61)
/* 377 */               return 367; 
/* 378 */             if (c3 == 62) {
/* 379 */               c3 = getc();
/* 380 */               if (c3 == 61)
/* 381 */                 return 371; 
/* 382 */               ungetc(c3);
/* 383 */               return 370;
/*     */             } 
/*     */             
/* 386 */             ungetc(c3);
/* 387 */             return 366;
/*     */         } 
/*     */ 
/*     */ 
/*     */       
/* 392 */       } else if (c2 == 61) {
/* 393 */         return t;
/*     */       } 
/* 395 */     } else if (c == 94) {
/* 396 */       c2 = getc();
/* 397 */       if (c2 == 61) {
/* 398 */         return 360;
/*     */       }
/* 400 */     } else if (c == 124) {
/* 401 */       c2 = getc();
/* 402 */       if (c2 == 61)
/* 403 */         return 361; 
/* 404 */       if (c2 == 124) {
/* 405 */         return 368;
/*     */       }
/*     */     } else {
/* 408 */       return c;
/*     */     } 
/* 410 */     ungetc(c2);
/* 411 */     return c;
/*     */   }
/*     */   
/*     */   private int readIdentifier(int c, Token token) {
/* 415 */     StringBuffer tbuf = this.textBuffer;
/* 416 */     tbuf.setLength(0);
/*     */     
/*     */     do {
/* 419 */       tbuf.append((char)c);
/* 420 */       c = getc();
/* 421 */     } while (Character.isJavaIdentifierPart((char)c));
/*     */     
/* 423 */     ungetc(c);
/*     */     
/* 425 */     String name = tbuf.toString();
/* 426 */     int t = ktable.lookup(name);
/* 427 */     if (t >= 0) {
/* 428 */       return t;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 436 */     token.textValue = name;
/* 437 */     return 400;
/*     */   }
/*     */   
/* 440 */   private static final KeywordTable ktable = new KeywordTable();
/*     */   
/*     */   static {
/* 443 */     ktable.append("abstract", 300);
/* 444 */     ktable.append("boolean", 301);
/* 445 */     ktable.append("break", 302);
/* 446 */     ktable.append("byte", 303);
/* 447 */     ktable.append("case", 304);
/* 448 */     ktable.append("catch", 305);
/* 449 */     ktable.append("char", 306);
/* 450 */     ktable.append("class", 307);
/* 451 */     ktable.append("const", 308);
/* 452 */     ktable.append("continue", 309);
/* 453 */     ktable.append("default", 310);
/* 454 */     ktable.append("do", 311);
/* 455 */     ktable.append("double", 312);
/* 456 */     ktable.append("else", 313);
/* 457 */     ktable.append("extends", 314);
/* 458 */     ktable.append("false", 411);
/* 459 */     ktable.append("final", 315);
/* 460 */     ktable.append("finally", 316);
/* 461 */     ktable.append("float", 317);
/* 462 */     ktable.append("for", 318);
/* 463 */     ktable.append("goto", 319);
/* 464 */     ktable.append("if", 320);
/* 465 */     ktable.append("implements", 321);
/* 466 */     ktable.append("import", 322);
/* 467 */     ktable.append("instanceof", 323);
/* 468 */     ktable.append("int", 324);
/* 469 */     ktable.append("interface", 325);
/* 470 */     ktable.append("long", 326);
/* 471 */     ktable.append("native", 327);
/* 472 */     ktable.append("new", 328);
/* 473 */     ktable.append("null", 412);
/* 474 */     ktable.append("package", 329);
/* 475 */     ktable.append("private", 330);
/* 476 */     ktable.append("protected", 331);
/* 477 */     ktable.append("public", 332);
/* 478 */     ktable.append("return", 333);
/* 479 */     ktable.append("short", 334);
/* 480 */     ktable.append("static", 335);
/* 481 */     ktable.append("strictfp", 347);
/* 482 */     ktable.append("super", 336);
/* 483 */     ktable.append("switch", 337);
/* 484 */     ktable.append("synchronized", 338);
/* 485 */     ktable.append("this", 339);
/* 486 */     ktable.append("throw", 340);
/* 487 */     ktable.append("throws", 341);
/* 488 */     ktable.append("transient", 342);
/* 489 */     ktable.append("true", 410);
/* 490 */     ktable.append("try", 343);
/* 491 */     ktable.append("void", 344);
/* 492 */     ktable.append("volatile", 345);
/* 493 */     ktable.append("while", 346);
/*     */   }
/*     */   
/*     */   private static boolean isBlank(int c) {
/* 497 */     return (c == 32 || c == 9 || c == 12 || c == 13 || c == 10);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isDigit(int c) {
/* 503 */     return (48 <= c && c <= 57);
/*     */   }
/*     */   
/*     */   private void ungetc(int c) {
/* 507 */     this.lastChar = c;
/*     */   }
/*     */   
/*     */   public String getTextAround() {
/* 511 */     int begin = this.position - 10;
/* 512 */     if (begin < 0) {
/* 513 */       begin = 0;
/*     */     }
/* 515 */     int end = this.position + 10;
/* 516 */     if (end > this.maxlen) {
/* 517 */       end = this.maxlen;
/*     */     }
/* 519 */     return this.input.substring(begin, end);
/*     */   }
/*     */   
/*     */   private int getc() {
/* 523 */     if (this.lastChar < 0) {
/* 524 */       if (this.position < this.maxlen) {
/* 525 */         return this.input.charAt(this.position++);
/*     */       }
/* 527 */       return -1;
/* 528 */     }  int c = this.lastChar;
/* 529 */     this.lastChar = -1;
/* 530 */     return c;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\compiler\Lex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */