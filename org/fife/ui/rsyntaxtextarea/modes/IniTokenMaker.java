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
/*     */ 
/*     */ public class IniTokenMaker
/*     */   extends AbstractJFlexTokenMaker
/*     */ {
/*     */   public static final int YYEOF = -1;
/*     */   public static final int YYINITIAL = 0;
/*     */   public static final int VALUE = 1;
/*     */   private static final String ZZ_CMAP_PACKED = "\t\000\001\003\001\005\025\000\001\003\002\000\001\004\027\000\001\004\001\000\001\001\035\000\001\002\001\000\001\006ﾢ\000";
/*  81 */   private static final char[] ZZ_CMAP = zzUnpackCMap("\t\000\001\003\001\005\025\000\001\003\002\000\001\004\027\000\001\004\001\000\001\001\035\000\001\002\001\000\001\006ﾢ\000");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   private static final int[] ZZ_ACTION = zzUnpackAction();
/*     */ 
/*     */   
/*     */   private static final String ZZ_ACTION_PACKED_0 = "\001\001\001\002\001\001\001\003\001\004\001\005\001\006\001\002\001\007\001\004";
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackAction() {
/*  93 */     int[] result = new int[10];
/*  94 */     int offset = 0;
/*  95 */     offset = zzUnpackAction("\001\001\001\002\001\001\001\003\001\004\001\005\001\006\001\002\001\007\001\004", offset, result);
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
/*     */   private static final String ZZ_ROWMAP_PACKED_0 = "\000\000\000\007\000\016\000\025\000\034\000#\000*\0001\000\025\000\025";
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackRowMap() {
/* 122 */     int[] result = new int[10];
/* 123 */     int offset = 0;
/* 124 */     offset = zzUnpackRowMap("\000\000\000\007\000\016\000\025\000\034\000#\000*\0001\000\025\000\025", offset, result);
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
/*     */   private static final String ZZ_TRANS_PACKED_0 = "\001\003\001\004\001\005\001\006\001\007\001\000\001\003\001\b\001\t\001\005\001\006\001\007\001\000\001\b\001\003\005\000\001\003\007\000\006\005\001\n\003\000\001\006\003\000\005\007\001\000\001\007\001\b\005\000\001\b";
/*     */   
/*     */   private static final int ZZ_UNKNOWN_ERROR = 0;
/*     */   private static final int ZZ_NO_MATCH = 1;
/*     */   private static final int ZZ_PUSHBACK_2BIG = 2;
/*     */   
/*     */   private static int[] zzUnpackTrans() {
/* 151 */     int[] result = new int[56];
/* 152 */     int offset = 0;
/* 153 */     offset = zzUnpackTrans("\001\003\001\004\001\005\001\006\001\007\001\000\001\003\001\b\001\t\001\005\001\006\001\007\001\000\001\b\001\003\005\000\001\003\007\000\006\005\001\n\003\000\001\006\003\000\005\007\001\000\001\007\001\b\005\000\001\b", offset, result);
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
/*     */   private static final String ZZ_ATTRIBUTE_PACKED_0 = "\003\001\001\t\004\001\002\t";
/*     */   private Reader zzReader;
/*     */   private int zzState;
/*     */   
/*     */   private static int[] zzUnpackAttribute() {
/* 192 */     int[] result = new int[10];
/* 193 */     int offset = 0;
/* 194 */     offset = zzUnpackAttribute("\003\001\001\t\004\001\002\t", offset, result);
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
/*     */   private boolean zzAtEOF;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IniTokenMaker() {}
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
/* 294 */     return new String[] { ";", null };
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
/* 318 */     this.s = text;
/*     */     try {
/* 320 */       yyreset(this.zzReader);
/* 321 */       yybegin(state);
/* 322 */       return yylex();
/* 323 */     } catch (IOException ioe) {
/* 324 */       ioe.printStackTrace();
/* 325 */       return (Token)new TokenImpl();
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
/*     */   private boolean zzRefill() {
/* 338 */     return (this.zzCurrentPos >= this.s.offset + this.s.count);
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
/* 354 */     this.zzBuffer = this.s.array;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 362 */     this.zzStartRead = this.s.offset;
/* 363 */     this.zzEndRead = this.zzStartRead + this.s.count - 1;
/* 364 */     this.zzCurrentPos = this.zzMarkedPos = this.s.offset;
/* 365 */     this.zzLexicalState = 0;
/* 366 */     this.zzReader = reader;
/* 367 */     this.zzAtEOF = false;
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
/*     */   public IniTokenMaker(Reader in) {
/* 380 */     this.zzReader = in;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IniTokenMaker(InputStream in) {
/* 390 */     this(new InputStreamReader(in));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static char[] zzUnpackCMap(String packed) {
/* 400 */     char[] map = new char[65536];
/* 401 */     int i = 0;
/* 402 */     int j = 0;
/* 403 */     label10: while (i < 32) {
/* 404 */       int count = packed.charAt(i++);
/* 405 */       char value = packed.charAt(i++); while (true)
/* 406 */       { map[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 408 */     }  return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void yyclose() throws IOException {
/* 415 */     this.zzAtEOF = true;
/* 416 */     this.zzEndRead = this.zzStartRead;
/*     */     
/* 418 */     if (this.zzReader != null) {
/* 419 */       this.zzReader.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int yystate() {
/* 427 */     return this.zzLexicalState;
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
/* 438 */     this.zzLexicalState = newState;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String yytext() {
/* 446 */     return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
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
/* 462 */     return this.zzBuffer[this.zzStartRead + pos];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int yylength() {
/* 470 */     return this.zzMarkedPos - this.zzStartRead;
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
/* 491 */       message = ZZ_ERROR_MSG[errorCode];
/*     */     }
/* 493 */     catch (ArrayIndexOutOfBoundsException e) {
/* 494 */       message = ZZ_ERROR_MSG[0];
/*     */     } 
/*     */     
/* 497 */     throw new Error(message);
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
/* 510 */     if (number > yylength()) {
/* 511 */       zzScanError(2);
/*     */     }
/* 513 */     this.zzMarkedPos -= number;
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
/* 531 */     int zzEndReadL = this.zzEndRead;
/* 532 */     char[] zzBufferL = this.zzBuffer;
/* 533 */     char[] zzCMapL = ZZ_CMAP;
/*     */     
/* 535 */     int[] zzTransL = ZZ_TRANS;
/* 536 */     int[] zzRowMapL = ZZ_ROWMAP;
/* 537 */     int[] zzAttrL = ZZ_ATTRIBUTE;
/*     */     
/*     */     while (true) {
/* 540 */       int zzInput, zzMarkedPosL = this.zzMarkedPos;
/*     */       
/* 542 */       int zzAction = -1;
/*     */       
/* 544 */       int zzCurrentPosL = this.zzCurrentPos = this.zzStartRead = zzMarkedPosL;
/*     */       
/* 546 */       this.zzState = this.zzLexicalState;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 552 */         if (zzCurrentPosL < zzEndReadL)
/* 553 */         { zzInput = zzBufferL[zzCurrentPosL++]; }
/* 554 */         else { if (this.zzAtEOF) {
/* 555 */             int i = -1;
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 560 */           this.zzCurrentPos = zzCurrentPosL;
/* 561 */           this.zzMarkedPos = zzMarkedPosL;
/* 562 */           boolean eof = zzRefill();
/*     */           
/* 564 */           zzCurrentPosL = this.zzCurrentPos;
/* 565 */           zzMarkedPosL = this.zzMarkedPos;
/* 566 */           zzBufferL = this.zzBuffer;
/* 567 */           zzEndReadL = this.zzEndRead;
/* 568 */           if (eof) {
/* 569 */             int i = -1;
/*     */             
/*     */             break;
/*     */           } 
/* 573 */           zzInput = zzBufferL[zzCurrentPosL++]; }
/*     */ 
/*     */         
/* 576 */         int zzNext = zzTransL[zzRowMapL[this.zzState] + zzCMapL[zzInput]];
/* 577 */         if (zzNext == -1)
/* 578 */           break;  this.zzState = zzNext;
/*     */         
/* 580 */         int zzAttributes = zzAttrL[this.zzState];
/* 581 */         if ((zzAttributes & 0x1) == 1) {
/* 582 */           zzAction = this.zzState;
/* 583 */           zzMarkedPosL = zzCurrentPosL;
/* 584 */           if ((zzAttributes & 0x8) == 8) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 591 */       this.zzMarkedPos = zzMarkedPosL;
/*     */       
/* 593 */       switch ((zzAction < 0) ? zzAction : ZZ_ACTION[zzAction]) {
/*     */         case 3:
/* 595 */           this.start = this.zzMarkedPos; addToken(23); yybegin(1); continue;
/*     */         case 8:
/*     */           continue;
/*     */         case 2:
/* 599 */           addToken(20); continue;
/*     */         case 9:
/*     */           continue;
/*     */         case 5:
/* 603 */           addToken(21); continue;
/*     */         case 10:
/*     */           continue;
/*     */         case 7:
/* 607 */           this.start = this.zzMarkedPos; addToken(23); continue;
/*     */         case 11:
/*     */           continue;
/*     */         case 6:
/* 611 */           addToken(1); continue;
/*     */         case 12:
/*     */           continue;
/*     */         case 4:
/* 615 */           addToken(24); continue;
/*     */         case 13:
/*     */           continue;
/*     */         case 1:
/* 619 */           addToken(16); continue;
/*     */         case 14:
/*     */           continue;
/*     */       } 
/* 623 */       if (zzInput == -1 && this.zzStartRead == this.zzCurrentPos) {
/* 624 */         this.zzAtEOF = true;
/* 625 */         switch (this.zzLexicalState) {
/*     */           case 0:
/* 627 */             addNullToken(); return (Token)this.firstToken;
/*     */           case 11:
/*     */             continue;
/*     */           case 1:
/* 631 */             addNullToken(); return (Token)this.firstToken;
/*     */           case 12:
/*     */             continue;
/*     */         } 
/* 635 */         return null;
/*     */       } 
/*     */ 
/*     */       
/* 639 */       zzScanError(1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\modes\IniTokenMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */