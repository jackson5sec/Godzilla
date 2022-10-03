/*     */ package org.fife.ui.rsyntaxtextarea.modes;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import javax.swing.text.Segment;
/*     */ import org.fife.ui.rsyntaxtextarea.AbstractJFlexTokenMaker;
/*     */ import org.fife.ui.rsyntaxtextarea.Token;
/*     */ import org.fife.ui.rsyntaxtextarea.TokenImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertiesFileTokenMaker
/*     */   extends AbstractJFlexTokenMaker
/*     */ {
/*     */   public static final int YYEOF = -1;
/*     */   private static final int ZZ_BUFFERSIZE = 16384;
/*     */   public static final int YYINITIAL = 0;
/*     */   public static final int VALUE = 1;
/*     */   private static final String ZZ_CMAP_PACKED = "\t\000\001\003\001\002\025\000\001\003\001\004\001\000\001\004\003\000\001\005\022\000\001\001\002\000\001\001\036\000\001\006\036\000\001\007\001\000\001\bﾂ\000";
/*  81 */   private static final char[] ZZ_CMAP = zzUnpackCMap("\t\000\001\003\001\002\025\000\001\003\001\004\001\000\001\004\003\000\001\005\022\000\001\001\002\000\001\001\036\000\001\006\036\000\001\007\001\000\001\bﾂ\000");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   private static final int[] ZZ_ACTION = zzUnpackAction();
/*     */ 
/*     */   
/*     */   private static final String ZZ_ACTION_PACKED_0 = "\001\001\001\000\001\001\001\002\001\003\001\004\001\005\001\006\001\007\001\b\001\006\001\005\001\b";
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackAction() {
/*  93 */     int[] result = new int[13];
/*  94 */     int offset = 0;
/*  95 */     offset = zzUnpackAction("\001\001\001\000\001\001\001\002\001\003\001\004\001\005\001\006\001\007\001\b\001\006\001\005\001\b", offset, result);
/*  96 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAction(String packed, int offset, int[] result) {
/* 100 */     int i = 0;
/* 101 */     int j = offset;
/* 102 */     int l = packed.length();
/* 103 */     label10: while (i < l) {
/* 104 */       int count = packed.charAt(i++);
/* 105 */       int value = packed.charAt(i++); while (true)
/* 106 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 108 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
/*     */ 
/*     */   
/*     */   private static final String ZZ_ROWMAP_PACKED_0 = "\000\000\000\t\000\022\000\033\000$\000-\0006\000?\000H\000Q\000\033\000\033\000\033";
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackRowMap() {
/* 122 */     int[] result = new int[13];
/* 123 */     int offset = 0;
/* 124 */     offset = zzUnpackRowMap("\000\000\000\t\000\022\000\033\000$\000-\0006\000?\000H\000Q\000\033\000\033\000\033", offset, result);
/* 125 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackRowMap(String packed, int offset, int[] result) {
/* 129 */     int i = 0;
/* 130 */     int j = offset;
/* 131 */     int l = packed.length();
/* 132 */     while (i < l) {
/* 133 */       int high = packed.charAt(i++) << 16;
/* 134 */       result[j++] = high | packed.charAt(i++);
/*     */     } 
/* 136 */     return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 142 */   private static final int[] ZZ_TRANS = zzUnpackTrans();
/*     */   
/*     */   private static final String ZZ_TRANS_PACKED_0 = "\001\003\001\004\001\000\001\005\001\006\004\003\005\007\001\b\001\t\001\n\001\007\001\003\004\000\004\003\f\000\001\005\005\000\002\006\001\000\006\006\005\007\003\000\001\007\005\b\001\013\003\b\002\f\001\000\006\f\b\n\001\r";
/*     */   
/*     */   private static final int ZZ_UNKNOWN_ERROR = 0;
/*     */   private static final int ZZ_NO_MATCH = 1;
/*     */   private static final int ZZ_PUSHBACK_2BIG = 2;
/*     */   
/*     */   private static int[] zzUnpackTrans() {
/* 151 */     int[] result = new int[90];
/* 152 */     int offset = 0;
/* 153 */     offset = zzUnpackTrans("\001\003\001\004\001\000\001\005\001\006\004\003\005\007\001\b\001\t\001\n\001\007\001\003\004\000\004\003\f\000\001\005\005\000\002\006\001\000\006\006\005\007\003\000\001\007\005\b\001\013\003\b\002\f\001\000\006\f\b\n\001\r", offset, result);
/* 154 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackTrans(String packed, int offset, int[] result) {
/* 158 */     int i = 0;
/* 159 */     int j = offset;
/* 160 */     int l = packed.length();
/* 161 */     label10: while (i < l) {
/* 162 */       int count = packed.charAt(i++);
/* 163 */       int value = packed.charAt(i++);
/* 164 */       value--; while (true)
/* 165 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 167 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 177 */   private static final String[] ZZ_ERROR_MSG = new String[] { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 186 */   private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
/*     */   private static final String ZZ_ATTRIBUTE_PACKED_0 = "\001\001\001\000\001\001\001\t\006\001\003\t";
/*     */   private Reader zzReader;
/*     */   private int zzState;
/*     */   
/*     */   private static int[] zzUnpackAttribute() {
/* 192 */     int[] result = new int[13];
/* 193 */     int offset = 0;
/* 194 */     offset = zzUnpackAttribute("\001\001\001\000\001\001\001\t\006\001\003\t", offset, result);
/* 195 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAttribute(String packed, int offset, int[] result) {
/* 199 */     int i = 0;
/* 200 */     int j = offset;
/* 201 */     int l = packed.length();
/* 202 */     label10: while (i < l) {
/* 203 */       int count = packed.charAt(i++);
/* 204 */       int value = packed.charAt(i++); while (true)
/* 205 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 207 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 217 */   private int zzLexicalState = 0;
/*     */ 
/*     */ 
/*     */   
/* 221 */   private char[] zzBuffer = new char[16384];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int zzMarkedPos;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int zzCurrentPos;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int zzStartRead;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int zzEndRead;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean zzAtEOF;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertiesFileTokenMaker() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToken(int tokenType) {
/* 257 */     addToken(this.zzStartRead, this.zzMarkedPos - 1, tokenType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToken(int start, int end, int tokenType) {
/* 267 */     int so = start + this.offsetShift;
/* 268 */     addToken(this.zzBuffer, start, end, tokenType, so);
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
/*     */   public void addToken(char[] array, int start, int end, int tokenType, int startOffset) {
/* 284 */     super.addToken(array, start, end, tokenType, startOffset);
/* 285 */     this.zzStartRead = this.zzMarkedPos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getLineCommentStartAndEnd(int languageIndex) {
/* 294 */     return new String[] { "#", null };
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
/*     */   public Token getTokenList(Segment text, int initialTokenType, int startOffset) {
/* 313 */     resetTokenList();
/* 314 */     this.offsetShift = -text.offset + startOffset;
/*     */ 
/*     */     
/* 317 */     int state = 0;
/* 318 */     switch (initialTokenType) {
/*     */       case 13:
/* 320 */         state = 1;
/* 321 */         this.start = text.offset;
/*     */         break;
/*     */       default:
/* 324 */         state = 0;
/*     */         break;
/*     */     } 
/* 327 */     this.s = text;
/*     */     try {
/* 329 */       yyreset(this.zzReader);
/* 330 */       yybegin(state);
/* 331 */       return yylex();
/* 332 */     } catch (IOException ioe) {
/* 333 */       ioe.printStackTrace();
/* 334 */       return (Token)new TokenImpl();
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
/*     */   private boolean zzRefill() {
/* 348 */     return (this.zzCurrentPos >= this.s.offset + this.s.count);
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
/*     */   public final void yyreset(Reader reader) {
/* 364 */     this.zzBuffer = this.s.array;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 372 */     this.zzStartRead = this.s.offset;
/* 373 */     this.zzEndRead = this.zzStartRead + this.s.count - 1;
/* 374 */     this.zzCurrentPos = this.zzMarkedPos = this.s.offset;
/* 375 */     this.zzLexicalState = 0;
/* 376 */     this.zzReader = reader;
/* 377 */     this.zzAtEOF = false;
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
/*     */   public PropertiesFileTokenMaker(Reader in) {
/* 390 */     this.zzReader = in;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertiesFileTokenMaker(InputStream in) {
/* 400 */     this(new InputStreamReader(in));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static char[] zzUnpackCMap(String packed) {
/* 410 */     char[] map = new char[65536];
/* 411 */     int i = 0;
/* 412 */     int j = 0;
/* 413 */     label10: while (i < 42) {
/* 414 */       int count = packed.charAt(i++);
/* 415 */       char value = packed.charAt(i++); while (true)
/* 416 */       { map[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 418 */     }  return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void yyclose() throws IOException {
/* 426 */     this.zzAtEOF = true;
/* 427 */     this.zzEndRead = this.zzStartRead;
/*     */     
/* 429 */     if (this.zzReader != null) {
/* 430 */       this.zzReader.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int yystate() {
/* 438 */     return this.zzLexicalState;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void yybegin(int newState) {
/* 449 */     this.zzLexicalState = newState;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String yytext() {
/* 457 */     return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
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
/*     */   public final char yycharat(int pos) {
/* 473 */     return this.zzBuffer[this.zzStartRead + pos];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int yylength() {
/* 481 */     return this.zzMarkedPos - this.zzStartRead;
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
/*     */   private void zzScanError(int errorCode) {
/*     */     String message;
/*     */     try {
/* 502 */       message = ZZ_ERROR_MSG[errorCode];
/*     */     }
/* 504 */     catch (ArrayIndexOutOfBoundsException e) {
/* 505 */       message = ZZ_ERROR_MSG[0];
/*     */     } 
/*     */     
/* 508 */     throw new Error(message);
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
/*     */   public void yypushback(int number) {
/* 521 */     if (number > yylength()) {
/* 522 */       zzScanError(2);
/*     */     }
/* 524 */     this.zzMarkedPos -= number;
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
/*     */   public Token yylex() throws IOException {
/* 542 */     int zzEndReadL = this.zzEndRead;
/* 543 */     char[] zzBufferL = this.zzBuffer;
/* 544 */     char[] zzCMapL = ZZ_CMAP;
/*     */     
/* 546 */     int[] zzTransL = ZZ_TRANS;
/* 547 */     int[] zzRowMapL = ZZ_ROWMAP;
/* 548 */     int[] zzAttrL = ZZ_ATTRIBUTE;
/*     */     
/*     */     while (true) {
/* 551 */       int zzInput, temp, zzMarkedPosL = this.zzMarkedPos;
/*     */       
/* 553 */       int zzAction = -1;
/*     */       
/* 555 */       int zzCurrentPosL = this.zzCurrentPos = this.zzStartRead = zzMarkedPosL;
/*     */       
/* 557 */       this.zzState = this.zzLexicalState;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 563 */         if (zzCurrentPosL < zzEndReadL)
/* 564 */         { zzInput = zzBufferL[zzCurrentPosL++]; }
/* 565 */         else { if (this.zzAtEOF) {
/* 566 */             int i = -1;
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 571 */           this.zzCurrentPos = zzCurrentPosL;
/* 572 */           this.zzMarkedPos = zzMarkedPosL;
/* 573 */           boolean eof = zzRefill();
/*     */           
/* 575 */           zzCurrentPosL = this.zzCurrentPos;
/* 576 */           zzMarkedPosL = this.zzMarkedPos;
/* 577 */           zzBufferL = this.zzBuffer;
/* 578 */           zzEndReadL = this.zzEndRead;
/* 579 */           if (eof) {
/* 580 */             int i = -1;
/*     */             
/*     */             break;
/*     */           } 
/* 584 */           zzInput = zzBufferL[zzCurrentPosL++]; }
/*     */ 
/*     */         
/* 587 */         int zzNext = zzTransL[zzRowMapL[this.zzState] + zzCMapL[zzInput]];
/* 588 */         if (zzNext == -1)
/* 589 */           break;  this.zzState = zzNext;
/*     */         
/* 591 */         int zzAttributes = zzAttrL[this.zzState];
/* 592 */         if ((zzAttributes & 0x1) == 1) {
/* 593 */           zzAction = this.zzState;
/* 594 */           zzMarkedPosL = zzCurrentPosL;
/* 595 */           if ((zzAttributes & 0x8) == 8) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 602 */       this.zzMarkedPos = zzMarkedPosL;
/*     */       
/* 604 */       switch ((zzAction < 0) ? zzAction : ZZ_ACTION[zzAction]) {
/*     */         case 7:
/* 606 */           addToken(this.start, this.zzEndRead, 13); return (Token)this.firstToken;
/*     */         case 9:
/*     */           continue;
/*     */         case 2:
/* 610 */           this.start = this.zzMarkedPos; addToken(23); yybegin(1); continue;
/*     */         case 10:
/*     */           continue;
/*     */         case 8:
/* 614 */           temp = this.zzStartRead; addToken(this.start, this.zzStartRead - 1, 13); addToken(temp, this.zzMarkedPos - 1, 17); this.start = this.zzMarkedPos; continue;
/*     */         case 11:
/*     */           continue;
/*     */         case 3:
/* 618 */           addToken(21); continue;
/*     */         case 12:
/*     */           continue;
/*     */         case 6:
/* 622 */           addToken(this.start, this.zzMarkedPos - 1, 13); this.start = this.zzMarkedPos; continue;
/*     */         case 13:
/*     */           continue;
/*     */         case 1:
/* 626 */           addToken(6);
/*     */           continue;
/*     */         
/*     */         case 14:
/*     */         case 5:
/*     */         case 15:
/*     */           continue;
/*     */         case 4:
/* 634 */           addToken(1); continue;
/*     */         case 16:
/*     */           continue;
/*     */       } 
/* 638 */       if (zzInput == -1 && this.zzStartRead == this.zzCurrentPos) {
/* 639 */         this.zzAtEOF = true;
/* 640 */         switch (this.zzLexicalState) {
/*     */           case 0:
/* 642 */             addNullToken(); return (Token)this.firstToken;
/*     */           case 14:
/*     */             continue;
/*     */           case 1:
/* 646 */             addToken(this.start, this.zzStartRead - 1, 13); addNullToken(); return (Token)this.firstToken;
/*     */           case 15:
/*     */             continue;
/*     */         } 
/* 650 */         return null;
/*     */       } 
/*     */ 
/*     */       
/* 654 */       zzScanError(1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\modes\PropertiesFileTokenMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */