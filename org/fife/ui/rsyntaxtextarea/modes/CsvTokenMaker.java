/*     */ package org.fife.ui.rsyntaxtextarea.modes;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import javax.swing.text.Segment;
/*     */ import org.fife.ui.rsyntaxtextarea.AbstractJFlexCTokenMaker;
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
/*     */ public class CsvTokenMaker
/*     */   extends AbstractJFlexCTokenMaker
/*     */ {
/*     */   public static final int YYEOF = -1;
/*     */   public static final int STRING = 1;
/*     */   public static final int YYINITIAL = 0;
/*     */   private static final String ZZ_CMAP_PACKED = "\n\000\001\003\027\000\001\001\t\000\001\002ￓ\000";
/*  76 */   private static final char[] ZZ_CMAP = zzUnpackCMap("\n\000\001\003\027\000\001\001\t\000\001\002ￓ\000");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   private static final int[] ZZ_ACTION = zzUnpackAction();
/*     */ 
/*     */   
/*     */   private static final String ZZ_ACTION_PACKED_0 = "\002\000\001\001\001\002\001\003\001\004\001\005\001\006\001\007\001\005";
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackAction() {
/*  88 */     int[] result = new int[10];
/*  89 */     int offset = 0;
/*  90 */     offset = zzUnpackAction("\002\000\001\001\001\002\001\003\001\004\001\005\001\006\001\007\001\005", offset, result);
/*  91 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAction(String packed, int offset, int[] result) {
/*  95 */     int i = 0;
/*  96 */     int j = offset;
/*  97 */     int l = packed.length();
/*  98 */     label10: while (i < l) {
/*  99 */       int count = packed.charAt(i++);
/* 100 */       int value = packed.charAt(i++); while (true)
/* 101 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 103 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
/*     */ 
/*     */   
/*     */   private static final String ZZ_ROWMAP_PACKED_0 = "\000\000\000\004\000\b\000\f\000\f\000\f\000\020\000\024\000\f\000\f";
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackRowMap() {
/* 117 */     int[] result = new int[10];
/* 118 */     int offset = 0;
/* 119 */     offset = zzUnpackRowMap("\000\000\000\004\000\b\000\f\000\f\000\f\000\020\000\024\000\f\000\f", offset, result);
/* 120 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackRowMap(String packed, int offset, int[] result) {
/* 124 */     int i = 0;
/* 125 */     int j = offset;
/* 126 */     int l = packed.length();
/* 127 */     while (i < l) {
/* 128 */       int high = packed.charAt(i++) << 16;
/* 129 */       result[j++] = high | packed.charAt(i++);
/*     */     } 
/* 131 */     return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 137 */   private static final int[] ZZ_TRANS = zzUnpackTrans();
/*     */   private static final String ZZ_TRANS_PACKED_0 = "\001\003\001\004\001\005\001\006\001\007\001\b\001\007\001\t\001\003\007\000\001\007\001\000\001\007\002\000\001\n\002\000";
/*     */   private static final int ZZ_UNKNOWN_ERROR = 0;
/*     */   private static final int ZZ_NO_MATCH = 1;
/*     */   private static final int ZZ_PUSHBACK_2BIG = 2;
/*     */   
/*     */   private static int[] zzUnpackTrans() {
/* 144 */     int[] result = new int[24];
/* 145 */     int offset = 0;
/* 146 */     offset = zzUnpackTrans("\001\003\001\004\001\005\001\006\001\007\001\b\001\007\001\t\001\003\007\000\001\007\001\000\001\007\002\000\001\n\002\000", offset, result);
/* 147 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackTrans(String packed, int offset, int[] result) {
/* 151 */     int i = 0;
/* 152 */     int j = offset;
/* 153 */     int l = packed.length();
/* 154 */     label10: while (i < l) {
/* 155 */       int count = packed.charAt(i++);
/* 156 */       int value = packed.charAt(i++);
/* 157 */       value--; while (true)
/* 158 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 160 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 170 */   private static final String[] ZZ_ERROR_MSG = new String[] { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 179 */   private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
/*     */   private static final String ZZ_ATTRIBUTE_PACKED_0 = "\002\000\001\001\003\t\002\001\002\t";
/*     */   private Reader zzReader;
/*     */   private int zzState;
/*     */   
/*     */   private static int[] zzUnpackAttribute() {
/* 185 */     int[] result = new int[10];
/* 186 */     int offset = 0;
/* 187 */     offset = zzUnpackAttribute("\002\000\001\001\003\t\002\001\002\t", offset, result);
/* 188 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAttribute(String packed, int offset, int[] result) {
/* 192 */     int i = 0;
/* 193 */     int j = offset;
/* 194 */     int l = packed.length();
/* 195 */     label10: while (i < l) {
/* 196 */       int count = packed.charAt(i++);
/* 197 */       int value = packed.charAt(i++); while (true)
/* 198 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 200 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 210 */   private int zzLexicalState = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private char[] zzBuffer;
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
/*     */   public static final int INTERNAL_STRING = -2048;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int evenOdd;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CsvTokenMaker() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addEndToken(int tokenType) {
/* 262 */     addToken(this.zzMarkedPos, this.zzMarkedPos, tokenType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addEvenOrOddColumnToken() {
/* 270 */     addEvenOrOddColumnToken(this.zzStartRead, this.zzMarkedPos - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addEvenOrOddColumnToken(int start, int end) {
/* 278 */     addToken(start, end, (this.evenOdd == 0) ? 20 : 16);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToken(int tokenType) {
/* 288 */     addToken(this.zzStartRead, this.zzMarkedPos - 1, tokenType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToken(int start, int end, int tokenType) {
/* 298 */     int so = start + this.offsetShift;
/* 299 */     addToken(this.zzBuffer, start, end, tokenType, so);
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
/* 315 */     super.addToken(array, start, end, tokenType, startOffset);
/* 316 */     this.zzStartRead = this.zzMarkedPos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getClosestStandardTokenTypeForInternalType(int type) {
/* 326 */     return (type == -2048) ? 13 : type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getMarkOccurrencesOfTokenType(int type) {
/* 332 */     return (type == 20 || type == 16);
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
/*     */   public Token getTokenList(Segment text, int initialTokenType, int startOffset) {
/* 350 */     resetTokenList();
/* 351 */     this.offsetShift = -text.offset + startOffset;
/*     */ 
/*     */     
/* 354 */     int state = 0;
/* 355 */     this.evenOdd = 0;
/* 356 */     if (initialTokenType < -1024) {
/* 357 */       state = 1;
/* 358 */       this.evenOdd = initialTokenType & 0x1;
/* 359 */       this.start = text.offset;
/*     */     } 
/*     */     
/* 362 */     this.s = text;
/*     */     try {
/* 364 */       yyreset(this.zzReader);
/* 365 */       yybegin(state);
/* 366 */       return yylex();
/* 367 */     } catch (IOException ioe) {
/* 368 */       ioe.printStackTrace();
/* 369 */       return (Token)new TokenImpl();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isIdentifierChar(int languageIndex, char ch) {
/* 380 */     return (Character.isLetterOrDigit(ch) || ch == '-' || ch == '.' || ch == '_');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean zzRefill() {
/* 391 */     return (this.zzCurrentPos >= this.s.offset + this.s.count);
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
/* 407 */     this.zzBuffer = this.s.array;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 415 */     this.zzStartRead = this.s.offset;
/* 416 */     this.zzEndRead = this.zzStartRead + this.s.count - 1;
/* 417 */     this.zzCurrentPos = this.zzMarkedPos = this.s.offset;
/* 418 */     this.zzLexicalState = 0;
/* 419 */     this.zzReader = reader;
/* 420 */     this.zzAtEOF = false;
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
/*     */   public CsvTokenMaker(Reader in) {
/* 433 */     this.zzReader = in;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CsvTokenMaker(InputStream in) {
/* 443 */     this(new InputStreamReader(in));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static char[] zzUnpackCMap(String packed) {
/* 453 */     char[] map = new char[65536];
/* 454 */     int i = 0;
/* 455 */     int j = 0;
/* 456 */     label10: while (i < 14) {
/* 457 */       int count = packed.charAt(i++);
/* 458 */       char value = packed.charAt(i++); while (true)
/* 459 */       { map[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 461 */     }  return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void yyclose() throws IOException {
/* 469 */     this.zzAtEOF = true;
/* 470 */     this.zzEndRead = this.zzStartRead;
/*     */     
/* 472 */     if (this.zzReader != null) {
/* 473 */       this.zzReader.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int yystate() {
/* 481 */     return this.zzLexicalState;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void yybegin(int newState) {
/* 491 */     this.zzLexicalState = newState;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String yytext() {
/* 499 */     return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
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
/* 515 */     return this.zzBuffer[this.zzStartRead + pos];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int yylength() {
/* 523 */     return this.zzMarkedPos - this.zzStartRead;
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
/* 544 */       message = ZZ_ERROR_MSG[errorCode];
/*     */     }
/* 546 */     catch (ArrayIndexOutOfBoundsException e) {
/* 547 */       message = ZZ_ERROR_MSG[0];
/*     */     } 
/*     */     
/* 550 */     throw new Error(message);
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
/* 563 */     if (number > yylength()) {
/* 564 */       zzScanError(2);
/*     */     }
/* 566 */     this.zzMarkedPos -= number;
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
/* 584 */     int zzEndReadL = this.zzEndRead;
/* 585 */     char[] zzBufferL = this.zzBuffer;
/* 586 */     char[] zzCMapL = ZZ_CMAP;
/*     */     
/* 588 */     int[] zzTransL = ZZ_TRANS;
/* 589 */     int[] zzRowMapL = ZZ_ROWMAP;
/* 590 */     int[] zzAttrL = ZZ_ATTRIBUTE;
/*     */     
/*     */     while (true) {
/* 593 */       int zzInput, zzMarkedPosL = this.zzMarkedPos;
/*     */       
/* 595 */       int zzAction = -1;
/*     */       
/* 597 */       int zzCurrentPosL = this.zzCurrentPos = this.zzStartRead = zzMarkedPosL;
/*     */       
/* 599 */       this.zzState = this.zzLexicalState;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 605 */         if (zzCurrentPosL < zzEndReadL)
/* 606 */         { zzInput = zzBufferL[zzCurrentPosL++]; }
/* 607 */         else { if (this.zzAtEOF) {
/* 608 */             int i = -1;
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 613 */           this.zzCurrentPos = zzCurrentPosL;
/* 614 */           this.zzMarkedPos = zzMarkedPosL;
/* 615 */           boolean eof = zzRefill();
/*     */           
/* 617 */           zzCurrentPosL = this.zzCurrentPos;
/* 618 */           zzMarkedPosL = this.zzMarkedPos;
/* 619 */           zzBufferL = this.zzBuffer;
/* 620 */           zzEndReadL = this.zzEndRead;
/* 621 */           if (eof) {
/* 622 */             int i = -1;
/*     */             
/*     */             break;
/*     */           } 
/* 626 */           zzInput = zzBufferL[zzCurrentPosL++]; }
/*     */ 
/*     */         
/* 629 */         int zzNext = zzTransL[zzRowMapL[this.zzState] + zzCMapL[zzInput]];
/* 630 */         if (zzNext == -1)
/* 631 */           break;  this.zzState = zzNext;
/*     */         
/* 633 */         int zzAttributes = zzAttrL[this.zzState];
/* 634 */         if ((zzAttributes & 0x1) == 1) {
/* 635 */           zzAction = this.zzState;
/* 636 */           zzMarkedPosL = zzCurrentPosL;
/* 637 */           if ((zzAttributes & 0x8) == 8) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 644 */       this.zzMarkedPos = zzMarkedPosL;
/*     */       
/* 646 */       switch ((zzAction < 0) ? zzAction : ZZ_ACTION[zzAction]) {
/*     */         case 6:
/* 648 */           yybegin(0); addEvenOrOddColumnToken(this.start, this.zzStartRead); continue;
/*     */         case 8:
/*     */           continue;
/*     */         case 4:
/* 652 */           addNullToken(); return (Token)this.firstToken;
/*     */         case 9:
/*     */           continue;
/*     */         case 7:
/* 656 */           addEvenOrOddColumnToken(this.start, this.zzEndRead);
/* 657 */           addEndToken(0xFFFFF800 | this.evenOdd); return (Token)this.firstToken;
/*     */         case 10:
/*     */           continue;
/*     */         case 3:
/* 661 */           addToken(23);
/* 662 */           this.evenOdd = this.evenOdd + 1 & 0x1; continue;
/*     */         case 11:
/*     */           continue;
/*     */         case 1:
/* 666 */           addEvenOrOddColumnToken(); continue;
/*     */         case 12:
/*     */           continue;
/*     */         case 2:
/* 670 */           this.start = this.zzMarkedPos - 1; yybegin(1);
/*     */           continue;
/*     */         
/*     */         case 13:
/*     */         case 5:
/*     */         case 14:
/*     */           continue;
/*     */       } 
/* 678 */       if (zzInput == -1 && this.zzStartRead == this.zzCurrentPos) {
/* 679 */         this.zzAtEOF = true;
/* 680 */         switch (this.zzLexicalState) {
/*     */           case 1:
/* 682 */             addEvenOrOddColumnToken(this.start, this.zzEndRead);
/* 683 */             addEndToken(0xFFFFF800 | this.evenOdd); return (Token)this.firstToken;
/*     */           case 11:
/*     */             continue;
/*     */           case 0:
/* 687 */             addNullToken(); return (Token)this.firstToken;
/*     */           case 12:
/*     */             continue;
/*     */         } 
/* 691 */         return null;
/*     */       } 
/*     */ 
/*     */       
/* 695 */       zzScanError(1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\modes\CsvTokenMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */