/*     */ package com.kitfox.svg.animation.parser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnimTimeParserTokenManager
/*     */   implements AnimTimeParserConstants
/*     */ {
/*     */   private final int jjStopStringLiteralDfa_0(int pos, long active0) {
/*  20 */     switch (pos) {
/*     */       
/*     */       case 0:
/*  23 */         if ((active0 & 0x30000L) != 0L)
/*  24 */           return 6; 
/*  25 */         if ((active0 & 0x800L) != 0L) {
/*     */           
/*  27 */           this.jjmatchedKind = 14;
/*  28 */           return 13;
/*     */         } 
/*  30 */         if ((active0 & 0x1400L) != 0L) {
/*     */           
/*  32 */           this.jjmatchedKind = 14;
/*  33 */           return 11;
/*     */         } 
/*  35 */         if ((active0 & 0x80000L) != 0L)
/*  36 */           return 2; 
/*  37 */         return -1;
/*     */       case 1:
/*  39 */         if ((active0 & 0x1C00L) != 0L) {
/*     */           
/*  41 */           this.jjmatchedKind = 14;
/*  42 */           this.jjmatchedPos = 1;
/*  43 */           return 11;
/*     */         } 
/*  45 */         return -1;
/*     */       case 2:
/*  47 */         if ((active0 & 0x1C00L) != 0L) {
/*     */           
/*  49 */           this.jjmatchedKind = 14;
/*  50 */           this.jjmatchedPos = 2;
/*  51 */           return 11;
/*     */         } 
/*  53 */         return -1;
/*     */       case 3:
/*  55 */         if ((active0 & 0x1C00L) != 0L) {
/*     */           
/*  57 */           this.jjmatchedKind = 14;
/*  58 */           this.jjmatchedPos = 3;
/*  59 */           return 11;
/*     */         } 
/*  61 */         return -1;
/*     */       case 4:
/*  63 */         if ((active0 & 0x1C00L) != 0L) {
/*     */           
/*  65 */           this.jjmatchedKind = 14;
/*  66 */           this.jjmatchedPos = 4;
/*  67 */           return 11;
/*     */         } 
/*  69 */         return -1;
/*     */       case 5:
/*  71 */         if ((active0 & 0x1C00L) != 0L) {
/*     */           
/*  73 */           this.jjmatchedKind = 14;
/*  74 */           this.jjmatchedPos = 5;
/*  75 */           return 11;
/*     */         } 
/*  77 */         return -1;
/*     */       case 6:
/*  79 */         if ((active0 & 0x1C00L) != 0L) {
/*     */           
/*  81 */           this.jjmatchedKind = 14;
/*  82 */           this.jjmatchedPos = 6;
/*  83 */           return 11;
/*     */         } 
/*  85 */         return -1;
/*     */       case 7:
/*  87 */         if ((active0 & 0x1C00L) != 0L) {
/*     */           
/*  89 */           this.jjmatchedKind = 14;
/*  90 */           this.jjmatchedPos = 7;
/*  91 */           return 11;
/*     */         } 
/*  93 */         return -1;
/*     */       case 8:
/*  95 */         if ((active0 & 0x800L) != 0L)
/*  96 */           return 11; 
/*  97 */         if ((active0 & 0x1400L) != 0L) {
/*     */           
/*  99 */           this.jjmatchedKind = 14;
/* 100 */           this.jjmatchedPos = 8;
/* 101 */           return 11;
/*     */         } 
/* 103 */         return -1;
/*     */       case 9:
/* 105 */         if ((active0 & 0x1000L) != 0L) {
/*     */           
/* 107 */           this.jjmatchedKind = 14;
/* 108 */           this.jjmatchedPos = 9;
/* 109 */           return 11;
/*     */         } 
/* 111 */         if ((active0 & 0x400L) != 0L)
/* 112 */           return 11; 
/* 113 */         return -1;
/*     */       case 10:
/* 115 */         if ((active0 & 0x1000L) != 0L) {
/*     */           
/* 117 */           this.jjmatchedKind = 14;
/* 118 */           this.jjmatchedPos = 10;
/* 119 */           return 11;
/*     */         } 
/* 121 */         return -1;
/*     */       case 11:
/* 123 */         if ((active0 & 0x1000L) != 0L) {
/*     */           
/* 125 */           this.jjmatchedKind = 14;
/* 126 */           this.jjmatchedPos = 11;
/* 127 */           return 11;
/*     */         } 
/* 129 */         return -1;
/*     */     } 
/* 131 */     return -1;
/*     */   }
/*     */   
/*     */   private final int jjStartNfa_0(int pos, long active0) {
/* 135 */     return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
/*     */   }
/*     */   
/*     */   private int jjStopAtPos(int pos, int kind) {
/* 139 */     this.jjmatchedKind = kind;
/* 140 */     this.jjmatchedPos = pos;
/* 141 */     return pos + 1;
/*     */   }
/*     */   private int jjMoveStringLiteralDfa0_0() {
/* 144 */     switch (this.curChar) {
/*     */       
/*     */       case 40:
/* 147 */         return jjStopAtPos(0, 20);
/*     */       case 41:
/* 149 */         return jjStopAtPos(0, 21);
/*     */       case 43:
/* 151 */         return jjStartNfaWithStates_0(0, 16, 6);
/*     */       case 45:
/* 153 */         return jjStartNfaWithStates_0(0, 17, 6);
/*     */       case 46:
/* 155 */         return jjStartNfaWithStates_0(0, 19, 2);
/*     */       case 58:
/* 157 */         return jjStopAtPos(0, 18);
/*     */       case 59:
/* 159 */         return jjStopAtPos(0, 15);
/*     */       case 105:
/* 161 */         return jjMoveStringLiteralDfa1_0(1024L);
/*     */       case 109:
/* 163 */         return jjMoveStringLiteralDfa1_0(2048L);
/*     */       case 119:
/* 165 */         return jjMoveStringLiteralDfa1_0(4096L);
/*     */     } 
/* 167 */     return jjMoveNfa_0(0, 0);
/*     */   }
/*     */   private int jjMoveStringLiteralDfa1_0(long active0) {
/*     */     try {
/* 171 */       this.curChar = this.input_stream.readChar();
/* 172 */     } catch (IOException e) {
/* 173 */       jjStopStringLiteralDfa_0(0, active0);
/* 174 */       return 1;
/*     */     } 
/* 176 */     switch (this.curChar) {
/*     */       
/*     */       case 104:
/* 179 */         return jjMoveStringLiteralDfa2_0(active0, 4096L);
/*     */       case 110:
/* 181 */         return jjMoveStringLiteralDfa2_0(active0, 1024L);
/*     */       case 111:
/* 183 */         return jjMoveStringLiteralDfa2_0(active0, 2048L);
/*     */     } 
/*     */ 
/*     */     
/* 187 */     return jjStartNfa_0(0, active0);
/*     */   }
/*     */   private int jjMoveStringLiteralDfa2_0(long old0, long active0) {
/* 190 */     if ((active0 &= old0) == 0L)
/* 191 */       return jjStartNfa_0(0, old0);  try {
/* 192 */       this.curChar = this.input_stream.readChar();
/* 193 */     } catch (IOException e) {
/* 194 */       jjStopStringLiteralDfa_0(1, active0);
/* 195 */       return 2;
/*     */     } 
/* 197 */     switch (this.curChar) {
/*     */       
/*     */       case 100:
/* 200 */         return jjMoveStringLiteralDfa3_0(active0, 1024L);
/*     */       case 101:
/* 202 */         return jjMoveStringLiteralDfa3_0(active0, 4096L);
/*     */       case 117:
/* 204 */         return jjMoveStringLiteralDfa3_0(active0, 2048L);
/*     */     } 
/*     */ 
/*     */     
/* 208 */     return jjStartNfa_0(1, active0);
/*     */   }
/*     */   private int jjMoveStringLiteralDfa3_0(long old0, long active0) {
/* 211 */     if ((active0 &= old0) == 0L)
/* 212 */       return jjStartNfa_0(1, old0);  try {
/* 213 */       this.curChar = this.input_stream.readChar();
/* 214 */     } catch (IOException e) {
/* 215 */       jjStopStringLiteralDfa_0(2, active0);
/* 216 */       return 3;
/*     */     } 
/* 218 */     switch (this.curChar) {
/*     */       
/*     */       case 101:
/* 221 */         return jjMoveStringLiteralDfa4_0(active0, 1024L);
/*     */       case 110:
/* 223 */         return jjMoveStringLiteralDfa4_0(active0, 4096L);
/*     */       case 115:
/* 225 */         return jjMoveStringLiteralDfa4_0(active0, 2048L);
/*     */     } 
/*     */ 
/*     */     
/* 229 */     return jjStartNfa_0(2, active0);
/*     */   }
/*     */   private int jjMoveStringLiteralDfa4_0(long old0, long active0) {
/* 232 */     if ((active0 &= old0) == 0L)
/* 233 */       return jjStartNfa_0(2, old0);  try {
/* 234 */       this.curChar = this.input_stream.readChar();
/* 235 */     } catch (IOException e) {
/* 236 */       jjStopStringLiteralDfa_0(3, active0);
/* 237 */       return 4;
/*     */     } 
/* 239 */     switch (this.curChar) {
/*     */       
/*     */       case 78:
/* 242 */         return jjMoveStringLiteralDfa5_0(active0, 4096L);
/*     */       case 101:
/* 244 */         return jjMoveStringLiteralDfa5_0(active0, 2048L);
/*     */       case 102:
/* 246 */         return jjMoveStringLiteralDfa5_0(active0, 1024L);
/*     */     } 
/*     */ 
/*     */     
/* 250 */     return jjStartNfa_0(3, active0);
/*     */   }
/*     */   private int jjMoveStringLiteralDfa5_0(long old0, long active0) {
/* 253 */     if ((active0 &= old0) == 0L)
/* 254 */       return jjStartNfa_0(3, old0);  try {
/* 255 */       this.curChar = this.input_stream.readChar();
/* 256 */     } catch (IOException e) {
/* 257 */       jjStopStringLiteralDfa_0(4, active0);
/* 258 */       return 5;
/*     */     } 
/* 260 */     switch (this.curChar) {
/*     */       
/*     */       case 105:
/* 263 */         return jjMoveStringLiteralDfa6_0(active0, 1024L);
/*     */       case 111:
/* 265 */         return jjMoveStringLiteralDfa6_0(active0, 6144L);
/*     */     } 
/*     */ 
/*     */     
/* 269 */     return jjStartNfa_0(4, active0);
/*     */   }
/*     */   private int jjMoveStringLiteralDfa6_0(long old0, long active0) {
/* 272 */     if ((active0 &= old0) == 0L)
/* 273 */       return jjStartNfa_0(4, old0);  try {
/* 274 */       this.curChar = this.input_stream.readChar();
/* 275 */     } catch (IOException e) {
/* 276 */       jjStopStringLiteralDfa_0(5, active0);
/* 277 */       return 6;
/*     */     } 
/* 279 */     switch (this.curChar) {
/*     */       
/*     */       case 110:
/* 282 */         return jjMoveStringLiteralDfa7_0(active0, 1024L);
/*     */       case 116:
/* 284 */         return jjMoveStringLiteralDfa7_0(active0, 4096L);
/*     */       case 118:
/* 286 */         return jjMoveStringLiteralDfa7_0(active0, 2048L);
/*     */     } 
/*     */ 
/*     */     
/* 290 */     return jjStartNfa_0(5, active0);
/*     */   }
/*     */   private int jjMoveStringLiteralDfa7_0(long old0, long active0) {
/* 293 */     if ((active0 &= old0) == 0L)
/* 294 */       return jjStartNfa_0(5, old0);  try {
/* 295 */       this.curChar = this.input_stream.readChar();
/* 296 */     } catch (IOException e) {
/* 297 */       jjStopStringLiteralDfa_0(6, active0);
/* 298 */       return 7;
/*     */     } 
/* 300 */     switch (this.curChar) {
/*     */       
/*     */       case 65:
/* 303 */         return jjMoveStringLiteralDfa8_0(active0, 4096L);
/*     */       case 101:
/* 305 */         return jjMoveStringLiteralDfa8_0(active0, 2048L);
/*     */       case 105:
/* 307 */         return jjMoveStringLiteralDfa8_0(active0, 1024L);
/*     */     } 
/*     */ 
/*     */     
/* 311 */     return jjStartNfa_0(6, active0);
/*     */   }
/*     */   private int jjMoveStringLiteralDfa8_0(long old0, long active0) {
/* 314 */     if ((active0 &= old0) == 0L)
/* 315 */       return jjStartNfa_0(6, old0);  try {
/* 316 */       this.curChar = this.input_stream.readChar();
/* 317 */     } catch (IOException e) {
/* 318 */       jjStopStringLiteralDfa_0(7, active0);
/* 319 */       return 8;
/*     */     } 
/* 321 */     switch (this.curChar) {
/*     */       
/*     */       case 99:
/* 324 */         return jjMoveStringLiteralDfa9_0(active0, 4096L);
/*     */       case 114:
/* 326 */         if ((active0 & 0x800L) != 0L)
/* 327 */           return jjStartNfaWithStates_0(8, 11, 11); 
/*     */         break;
/*     */       case 116:
/* 330 */         return jjMoveStringLiteralDfa9_0(active0, 1024L);
/*     */     } 
/*     */ 
/*     */     
/* 334 */     return jjStartNfa_0(7, active0);
/*     */   }
/*     */   private int jjMoveStringLiteralDfa9_0(long old0, long active0) {
/* 337 */     if ((active0 &= old0) == 0L)
/* 338 */       return jjStartNfa_0(7, old0);  try {
/* 339 */       this.curChar = this.input_stream.readChar();
/* 340 */     } catch (IOException e) {
/* 341 */       jjStopStringLiteralDfa_0(8, active0);
/* 342 */       return 9;
/*     */     } 
/* 344 */     switch (this.curChar) {
/*     */       
/*     */       case 101:
/* 347 */         if ((active0 & 0x400L) != 0L)
/* 348 */           return jjStartNfaWithStates_0(9, 10, 11); 
/*     */         break;
/*     */       case 116:
/* 351 */         return jjMoveStringLiteralDfa10_0(active0, 4096L);
/*     */     } 
/*     */ 
/*     */     
/* 355 */     return jjStartNfa_0(8, active0);
/*     */   }
/*     */   private int jjMoveStringLiteralDfa10_0(long old0, long active0) {
/* 358 */     if ((active0 &= old0) == 0L)
/* 359 */       return jjStartNfa_0(8, old0);  try {
/* 360 */       this.curChar = this.input_stream.readChar();
/* 361 */     } catch (IOException e) {
/* 362 */       jjStopStringLiteralDfa_0(9, active0);
/* 363 */       return 10;
/*     */     } 
/* 365 */     switch (this.curChar) {
/*     */       
/*     */       case 105:
/* 368 */         return jjMoveStringLiteralDfa11_0(active0, 4096L);
/*     */     } 
/*     */ 
/*     */     
/* 372 */     return jjStartNfa_0(9, active0);
/*     */   }
/*     */   private int jjMoveStringLiteralDfa11_0(long old0, long active0) {
/* 375 */     if ((active0 &= old0) == 0L)
/* 376 */       return jjStartNfa_0(9, old0);  try {
/* 377 */       this.curChar = this.input_stream.readChar();
/* 378 */     } catch (IOException e) {
/* 379 */       jjStopStringLiteralDfa_0(10, active0);
/* 380 */       return 11;
/*     */     } 
/* 382 */     switch (this.curChar) {
/*     */       
/*     */       case 118:
/* 385 */         return jjMoveStringLiteralDfa12_0(active0, 4096L);
/*     */     } 
/*     */ 
/*     */     
/* 389 */     return jjStartNfa_0(10, active0);
/*     */   }
/*     */   private int jjMoveStringLiteralDfa12_0(long old0, long active0) {
/* 392 */     if ((active0 &= old0) == 0L)
/* 393 */       return jjStartNfa_0(10, old0);  try {
/* 394 */       this.curChar = this.input_stream.readChar();
/* 395 */     } catch (IOException e) {
/* 396 */       jjStopStringLiteralDfa_0(11, active0);
/* 397 */       return 12;
/*     */     } 
/* 399 */     switch (this.curChar) {
/*     */       
/*     */       case 101:
/* 402 */         if ((active0 & 0x1000L) != 0L) {
/* 403 */           return jjStartNfaWithStates_0(12, 12, 11);
/*     */         }
/*     */         break;
/*     */     } 
/*     */     
/* 408 */     return jjStartNfa_0(11, active0);
/*     */   }
/*     */   
/*     */   private int jjStartNfaWithStates_0(int pos, int kind, int state) {
/* 412 */     this.jjmatchedKind = kind;
/* 413 */     this.jjmatchedPos = pos; 
/* 414 */     try { this.curChar = this.input_stream.readChar(); }
/* 415 */     catch (IOException e) { return pos + 1; }
/* 416 */      return jjMoveNfa_0(state, pos + 1);
/*     */   }
/*     */   
/*     */   private int jjMoveNfa_0(int startState, int curPos) {
/* 420 */     int startsAt = 0;
/* 421 */     this.jjnewStateCnt = 18;
/* 422 */     int i = 1;
/* 423 */     this.jjstateSet[0] = startState;
/* 424 */     int kind = Integer.MAX_VALUE;
/*     */     
/*     */     while (true) {
/* 427 */       if (++this.jjround == Integer.MAX_VALUE)
/* 428 */         ReInitRounds(); 
/* 429 */       if (this.curChar < 64) {
/*     */         
/* 431 */         long l = 1L << this.curChar;
/*     */         
/*     */         do {
/* 434 */           switch (this.jjstateSet[--i]) {
/*     */             
/*     */             case 0:
/* 437 */               if ((0x3FF000000000000L & l) != 0L) {
/*     */                 
/* 439 */                 if (kind > 8)
/* 440 */                   kind = 8; 
/* 441 */                 jjCheckNAddStates(0, 4); break;
/*     */               } 
/* 443 */               if ((0x280000000000L & l) != 0L) {
/* 444 */                 jjCheckNAddTwoStates(1, 6); break;
/* 445 */               }  if (this.curChar == 46)
/* 446 */                 jjCheckNAdd(2); 
/*     */               break;
/*     */             case 6:
/* 449 */               if ((0x3FF000000000000L & l) != 0L) {
/*     */                 
/* 451 */                 if (kind > 9)
/* 452 */                   kind = 9; 
/* 453 */                 jjCheckNAddStates(5, 8); break;
/*     */               } 
/* 455 */               if (this.curChar == 46)
/* 456 */                 jjCheckNAdd(2); 
/*     */               break;
/*     */             case 11:
/*     */             case 13:
/* 460 */               if ((0x3FF200000000000L & l) == 0L)
/*     */                 break; 
/* 462 */               if (kind > 14)
/* 463 */                 kind = 14; 
/* 464 */               jjCheckNAdd(11);
/*     */               break;
/*     */             case 1:
/* 467 */               if (this.curChar == 46)
/* 468 */                 jjCheckNAdd(2); 
/*     */               break;
/*     */             case 2:
/* 471 */               if ((0x3FF000000000000L & l) == 0L)
/*     */                 break; 
/* 473 */               if (kind > 9)
/* 474 */                 kind = 9; 
/* 475 */               jjCheckNAddTwoStates(2, 3);
/*     */               break;
/*     */             case 4:
/* 478 */               if ((0x280000000000L & l) != 0L)
/* 479 */                 jjCheckNAdd(5); 
/*     */               break;
/*     */             case 5:
/* 482 */               if ((0x3FF000000000000L & l) == 0L)
/*     */                 break; 
/* 484 */               if (kind > 9)
/* 485 */                 kind = 9; 
/* 486 */               jjCheckNAdd(5);
/*     */               break;
/*     */             case 7:
/* 489 */               if ((0x3FF000000000000L & l) != 0L)
/* 490 */                 jjCheckNAddTwoStates(7, 1); 
/*     */               break;
/*     */             case 8:
/* 493 */               if ((0x3FF000000000000L & l) == 0L)
/*     */                 break; 
/* 495 */               if (kind > 9)
/* 496 */                 kind = 9; 
/* 497 */               jjCheckNAddTwoStates(8, 3);
/*     */               break;
/*     */             case 16:
/* 500 */               if ((0x3FF000000000000L & l) == 0L)
/*     */                 break; 
/* 502 */               if (kind > 8)
/* 503 */                 kind = 8; 
/* 504 */               jjCheckNAddStates(0, 4);
/*     */               break;
/*     */             case 17:
/* 507 */               if ((0x3FF000000000000L & l) == 0L)
/*     */                 break; 
/* 509 */               if (kind > 8)
/* 510 */                 kind = 8; 
/* 511 */               jjCheckNAdd(17);
/*     */               break;
/*     */           } 
/*     */         
/* 515 */         } while (i != startsAt);
/*     */       }
/* 517 */       else if (this.curChar < 128) {
/*     */         
/* 519 */         long l = 1L << (this.curChar & 0x3F);
/*     */         
/*     */         do {
/* 522 */           switch (this.jjstateSet[--i]) {
/*     */             
/*     */             case 0:
/* 525 */               if ((0x7FFFFFE07FFFFFEL & l) != 0L) {
/*     */                 
/* 527 */                 if (kind > 14)
/* 528 */                   kind = 14; 
/* 529 */                 jjCheckNAdd(11);
/*     */               } 
/* 531 */               if ((0x8010000000000L & l) != 0L) {
/*     */                 
/* 533 */                 if (kind > 13)
/* 534 */                   kind = 13;  break;
/*     */               } 
/* 536 */               if (this.curChar == 109)
/* 537 */                 jjAddStates(9, 10); 
/*     */               break;
/*     */             case 13:
/* 540 */               if ((0x7FFFFFE87FFFFFEL & l) != 0L) {
/*     */                 
/* 542 */                 if (kind > 14)
/* 543 */                   kind = 14; 
/* 544 */                 jjCheckNAdd(11);
/*     */               } 
/* 546 */               if (this.curChar == 105) {
/* 547 */                 this.jjstateSet[this.jjnewStateCnt++] = 14; break;
/* 548 */               }  if (this.curChar == 115)
/*     */               {
/* 550 */                 if (kind > 13)
/* 551 */                   kind = 13; 
/*     */               }
/*     */               break;
/*     */             case 3:
/* 555 */               if ((0x2000000020L & l) != 0L)
/* 556 */                 jjAddStates(11, 12); 
/*     */               break;
/*     */             case 9:
/* 559 */               if ((0x8010000000000L & l) != 0L && kind > 13)
/* 560 */                 kind = 13; 
/*     */               break;
/*     */             case 10:
/* 563 */               if ((0x7FFFFFE07FFFFFEL & l) == 0L)
/*     */                 break; 
/* 565 */               if (kind > 14)
/* 566 */                 kind = 14; 
/* 567 */               jjCheckNAdd(11);
/*     */               break;
/*     */             case 11:
/* 570 */               if ((0x7FFFFFE87FFFFFEL & l) == 0L)
/*     */                 break; 
/* 572 */               if (kind > 14)
/* 573 */                 kind = 14; 
/* 574 */               jjCheckNAdd(11);
/*     */               break;
/*     */             case 12:
/* 577 */               if (this.curChar == 109)
/* 578 */                 jjAddStates(9, 10); 
/*     */               break;
/*     */             case 14:
/* 581 */               if (this.curChar == 110 && kind > 13)
/* 582 */                 kind = 13; 
/*     */               break;
/*     */             case 15:
/* 585 */               if (this.curChar == 105) {
/* 586 */                 this.jjstateSet[this.jjnewStateCnt++] = 14;
/*     */               }
/*     */               break;
/*     */           } 
/* 590 */         } while (i != startsAt);
/*     */       }
/*     */       else {
/*     */         
/* 594 */         int i2 = (this.curChar & 0xFF) >> 6;
/* 595 */         long l2 = 1L << (this.curChar & 0x3F);
/*     */         
/*     */         do {
/* 598 */           switch (this.jjstateSet[--i]) {
/*     */           
/*     */           } 
/*     */         
/* 602 */         } while (i != startsAt);
/*     */       } 
/* 604 */       if (kind != Integer.MAX_VALUE) {
/*     */         
/* 606 */         this.jjmatchedKind = kind;
/* 607 */         this.jjmatchedPos = curPos;
/* 608 */         kind = Integer.MAX_VALUE;
/*     */       } 
/* 610 */       curPos++;
/* 611 */       i = this.jjnewStateCnt;
/* 612 */       this.jjnewStateCnt = startsAt;
/* 613 */       startsAt = 18 - this.jjnewStateCnt;
/* 614 */       if (i == startsAt)
/* 615 */         return curPos;  
/* 616 */       try { this.curChar = this.input_stream.readChar(); }
/* 617 */       catch (IOException e) { return curPos; }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/* 622 */   public static final String[] jjstrLiteralImages = new String[] { "", null, null, null, null, null, null, null, null, null, "indefinite", "mouseover", "whenNotActive", null, null, ";", "+", "-", ":", ".", "(", ")" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Token jjFillToken() {
/* 634 */     String im = jjstrLiteralImages[this.jjmatchedKind];
/* 635 */     String curTokenImage = (im == null) ? this.input_stream.getImage() : im;
/* 636 */     int beginLine = this.input_stream.getBeginLine();
/* 637 */     int beginColumn = this.input_stream.getBeginColumn();
/* 638 */     int endLine = this.input_stream.getEndLine();
/* 639 */     int endColumn = this.input_stream.getEndColumn();
/* 640 */     Token t = Token.newToken(this.jjmatchedKind);
/* 641 */     t.kind = this.jjmatchedKind;
/* 642 */     t.image = curTokenImage;
/*     */     
/* 644 */     t.beginLine = beginLine;
/* 645 */     t.endLine = endLine;
/* 646 */     t.beginColumn = beginColumn;
/* 647 */     t.endColumn = endColumn;
/*     */     
/* 649 */     return t;
/*     */   }
/* 651 */   static final int[] jjnextStates = new int[] { 17, 7, 1, 8, 3, 7, 1, 8, 3, 13, 15, 4, 5 };
/*     */ 
/*     */ 
/*     */   
/* 655 */   int curLexState = 0;
/* 656 */   int defaultLexState = 0;
/*     */   
/*     */   int jjnewStateCnt;
/*     */   
/*     */   int jjround;
/*     */   
/*     */   int jjmatchedPos;
/*     */   int jjmatchedKind;
/*     */   
/*     */   public Token getNextToken() {
/* 666 */     int curPos = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/*     */       try {
/* 673 */         this.curChar = this.input_stream.beginToken();
/*     */       }
/* 675 */       catch (Exception e) {
/*     */         
/* 677 */         this.jjmatchedKind = 0;
/* 678 */         this.jjmatchedPos = -1;
/* 679 */         Token matchedToken = jjFillToken();
/* 680 */         return matchedToken;
/*     */       } 
/*     */       
/*     */       try {
/* 684 */         this.input_stream.backup(0);
/* 685 */         while (this.curChar <= 32 && (0x100003600L & 1L << this.curChar) != 0L) {
/* 686 */           this.curChar = this.input_stream.beginToken();
/*     */         }
/* 688 */       } catch (IOException e1) {
/*     */         continue;
/*     */       } 
/* 691 */       this.jjmatchedKind = Integer.MAX_VALUE;
/* 692 */       this.jjmatchedPos = 0;
/* 693 */       curPos = jjMoveStringLiteralDfa0_0();
/* 694 */       if (this.jjmatchedKind != Integer.MAX_VALUE) {
/*     */         
/* 696 */         if (this.jjmatchedPos + 1 < curPos)
/* 697 */           this.input_stream.backup(curPos - this.jjmatchedPos - 1); 
/* 698 */         if ((jjtoToken[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 0x3F)) != 0L) {
/*     */           
/* 700 */           Token matchedToken = jjFillToken();
/* 701 */           return matchedToken;
/*     */         } 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       break;
/*     */     } 
/* 708 */     int error_line = this.input_stream.getEndLine();
/* 709 */     int error_column = this.input_stream.getEndColumn();
/* 710 */     String error_after = null;
/* 711 */     boolean EOFSeen = false;
/*     */     try {
/* 713 */       this.input_stream.readChar();
/* 714 */       this.input_stream.backup(1);
/*     */     }
/* 716 */     catch (IOException e1) {
/* 717 */       EOFSeen = true;
/* 718 */       error_after = (curPos <= 1) ? "" : this.input_stream.getImage();
/* 719 */       if (this.curChar == 10 || this.curChar == 13) {
/* 720 */         error_line++;
/* 721 */         error_column = 0;
/*     */       } else {
/*     */         
/* 724 */         error_column++;
/*     */       } 
/* 726 */     }  if (!EOFSeen) {
/* 727 */       this.input_stream.backup(1);
/* 728 */       error_after = (curPos <= 1) ? "" : this.input_stream.getImage();
/*     */     } 
/* 730 */     throw new TokenMgrException(EOFSeen, this.curLexState, error_line, error_column, error_after, this.curChar, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void SkipLexicalActions(Token matchedToken) {
/* 736 */     switch (this.jjmatchedKind) {
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void MoreLexicalActions() {
/* 744 */     this.jjimageLen += this.lengthOfMatch = this.jjmatchedPos + 1;
/* 745 */     switch (this.jjmatchedKind) {
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void TokenLexicalActions(Token matchedToken) {
/* 753 */     switch (this.jjmatchedKind) {
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void jjCheckNAdd(int state) {
/* 761 */     if (this.jjrounds[state] != this.jjround) {
/*     */       
/* 763 */       this.jjstateSet[this.jjnewStateCnt++] = state;
/* 764 */       this.jjrounds[state] = this.jjround;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void jjAddStates(int start, int end) {
/*     */     do {
/* 770 */       this.jjstateSet[this.jjnewStateCnt++] = jjnextStates[start];
/* 771 */     } while (start++ != end);
/*     */   }
/*     */   
/*     */   private void jjCheckNAddTwoStates(int state1, int state2) {
/* 775 */     jjCheckNAdd(state1);
/* 776 */     jjCheckNAdd(state2);
/*     */   }
/*     */ 
/*     */   
/*     */   private void jjCheckNAddStates(int start, int end) {
/*     */     do {
/* 782 */       jjCheckNAdd(jjnextStates[start]);
/* 783 */     } while (start++ != end);
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
/*     */   public void ReInit(SimpleCharStream stream) {
/* 803 */     this.jjmatchedPos = this.jjnewStateCnt = 0;
/*     */ 
/*     */     
/* 806 */     this.curLexState = this.defaultLexState;
/* 807 */     this.input_stream = stream;
/* 808 */     ReInitRounds();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void ReInitRounds() {
/* 814 */     this.jjround = -2147483647;
/* 815 */     for (int i = 18; i-- > 0;) {
/* 816 */       this.jjrounds[i] = Integer.MIN_VALUE;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void ReInit(SimpleCharStream stream, int lexState) {
/* 822 */     ReInit(stream);
/* 823 */     SwitchTo(lexState);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void SwitchTo(int lexState) {
/* 829 */     if (lexState >= 1 || lexState < 0) {
/* 830 */       throw new TokenMgrException("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
/*     */     }
/* 832 */     this.curLexState = lexState;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 837 */   public static final String[] lexStateNames = new String[] { "DEFAULT" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 842 */   public static final int[] jjnewLexState = new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
/*     */ 
/*     */   
/* 845 */   static final long[] jjtoToken = new long[] { 4194049L };
/*     */ 
/*     */   
/* 848 */   static final long[] jjtoSkip = new long[] { 62L };
/*     */ 
/*     */   
/* 851 */   static final long[] jjtoSpecial = new long[] { 0L };
/*     */ 
/*     */   
/* 854 */   static final long[] jjtoMore = new long[] { 0L };
/*     */   protected SimpleCharStream input_stream; private final int[] jjrounds; private final int[] jjstateSet; private final StringBuilder jjimage; private StringBuilder image; private int jjimageLen;
/*     */   private int lengthOfMatch;
/*     */   protected int curChar;
/*     */   
/* 859 */   public AnimTimeParserTokenManager(SimpleCharStream stream) { this.jjrounds = new int[18];
/* 860 */     this.jjstateSet = new int[36];
/* 861 */     this.jjimage = new StringBuilder();
/* 862 */     this.image = this.jjimage; this.input_stream = stream; } public AnimTimeParserTokenManager(SimpleCharStream stream, int lexState) { this.jjrounds = new int[18]; this.jjstateSet = new int[36]; this.jjimage = new StringBuilder(); this.image = this.jjimage;
/*     */     ReInit(stream);
/*     */     SwitchTo(lexState); }
/*     */ 
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\parser\AnimTimeParserTokenManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */