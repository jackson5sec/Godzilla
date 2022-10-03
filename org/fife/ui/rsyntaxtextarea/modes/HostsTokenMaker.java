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
/*     */ public class HostsTokenMaker
/*     */   extends AbstractJFlexTokenMaker
/*     */ {
/*     */   public static final int YYEOF = -1;
/*     */   public static final int EOL_COMMENT = 1;
/*     */   public static final int YYINITIAL = 0;
/*     */   private static final String ZZ_CMAP_PACKED = "\t\000\001\002\001\001\025\000\001\002\001\004\001\000\001\003\001\006\001\004\007\004\001\004\001\021\001\005\n\006\001\017\001\004\001\000\001\004\001\000\002\004\032\006\001\004\001\000\001\004\001\000\001\004\001\000\004\006\001\016\001\013\001\006\001\007\001\f\002\006\001\r\003\006\001\t\002\006\001\n\001\b\002\006\001\020\003\006\003\000\001\004ﾁ\000";
/*  78 */   private static final char[] ZZ_CMAP = zzUnpackCMap("\t\000\001\002\001\001\025\000\001\002\001\004\001\000\001\003\001\006\001\004\007\004\001\004\001\021\001\005\n\006\001\017\001\004\001\000\001\004\001\000\002\004\032\006\001\004\001\000\001\004\001\000\001\004\001\000\004\006\001\016\001\013\001\006\001\007\001\f\002\006\001\r\003\006\001\t\002\006\001\n\001\b\002\006\001\020\003\006\003\000\001\004ﾁ\000");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   private static final int[] ZZ_ACTION = zzUnpackAction();
/*     */ 
/*     */   
/*     */   private static final String ZZ_ACTION_PACKED_0 = "\002\000\001\001\001\002\001\003\001\004\001\005\001\006\003\005\n\000\001\007\002\000";
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackAction() {
/*  90 */     int[] result = new int[24];
/*  91 */     int offset = 0;
/*  92 */     offset = zzUnpackAction("\002\000\001\001\001\002\001\003\001\004\001\005\001\006\003\005\n\000\001\007\002\000", offset, result);
/*  93 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAction(String packed, int offset, int[] result) {
/*  97 */     int i = 0;
/*  98 */     int j = offset;
/*  99 */     int l = packed.length();
/* 100 */     label10: while (i < l) {
/* 101 */       int count = packed.charAt(i++);
/* 102 */       int value = packed.charAt(i++); while (true)
/* 103 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 105 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
/*     */ 
/*     */   
/*     */   private static final String ZZ_ROWMAP_PACKED_0 = "\000\000\000\022\000$\0006\000H\0006\000Z\0006\000l\000~\000\000¢\000´\000Æ\000Ø\000ê\000ü\000Ď\000Ġ\000Ĳ\000ń\000Ŗ\000Ũ\000Ŗ";
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackRowMap() {
/* 120 */     int[] result = new int[24];
/* 121 */     int offset = 0;
/* 122 */     offset = zzUnpackRowMap("\000\000\000\022\000$\0006\000H\0006\000Z\0006\000l\000~\000\000¢\000´\000Æ\000Ø\000ê\000ü\000Ď\000Ġ\000Ĳ\000ń\000Ŗ\000Ũ\000Ŗ", offset, result);
/* 123 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackRowMap(String packed, int offset, int[] result) {
/* 127 */     int i = 0;
/* 128 */     int j = offset;
/* 129 */     int l = packed.length();
/* 130 */     while (i < l) {
/* 131 */       int high = packed.charAt(i++) << 16;
/* 132 */       result[j++] = high | packed.charAt(i++);
/*     */     } 
/* 134 */     return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   private static final int[] ZZ_TRANS = zzUnpackTrans();
/*     */ 
/*     */   
/*     */   private static final String ZZ_TRANS_PACKED_0 = "\001\003\001\004\001\005\001\006\016\003\001\007\001\b\005\007\001\t\003\007\001\n\004\007\001\013\001\007\001\003\003\000\016\003\024\000\001\005\017\000\001\007\001\000\005\007\001\000\003\007\001\000\004\007\001\000\001\007\b\000\001\f\021\000\001\r\003\000\001\016\025\000\001\017\t\000\001\020\022\000\001\021\025\000\001\022\024\000\001\023\n\000\001\024\027\000\001\025\020\000\001\021\024\000\001\026\n\000\001\021\004\000\001\025\007\000\001\027\017\000\002\030\n\026\001\030\001\026\001\030\005\000\001\026\f\000";
/*     */ 
/*     */   
/*     */   private static final int ZZ_UNKNOWN_ERROR = 0;
/*     */   
/*     */   private static final int ZZ_NO_MATCH = 1;
/*     */   
/*     */   private static final int ZZ_PUSHBACK_2BIG = 2;
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackTrans() {
/* 154 */     int[] result = new int[378];
/* 155 */     int offset = 0;
/* 156 */     offset = zzUnpackTrans("\001\003\001\004\001\005\001\006\016\003\001\007\001\b\005\007\001\t\003\007\001\n\004\007\001\013\001\007\001\003\003\000\016\003\024\000\001\005\017\000\001\007\001\000\005\007\001\000\003\007\001\000\004\007\001\000\001\007\b\000\001\f\021\000\001\r\003\000\001\016\025\000\001\017\t\000\001\020\022\000\001\021\025\000\001\022\024\000\001\023\n\000\001\024\027\000\001\025\020\000\001\021\024\000\001\026\n\000\001\021\004\000\001\025\007\000\001\027\017\000\002\030\n\026\001\030\001\026\001\030\005\000\001\026\f\000", offset, result);
/* 157 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackTrans(String packed, int offset, int[] result) {
/* 161 */     int i = 0;
/* 162 */     int j = offset;
/* 163 */     int l = packed.length();
/* 164 */     label10: while (i < l) {
/* 165 */       int count = packed.charAt(i++);
/* 166 */       int value = packed.charAt(i++);
/* 167 */       value--; while (true)
/* 168 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 170 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 180 */   private static final String[] ZZ_ERROR_MSG = new String[] { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 189 */   private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
/*     */   
/*     */   private static final String ZZ_ATTRIBUTE_PACKED_0 = "\002\000\001\001\001\t\001\001\001\t\001\001\001\t\003\001\n\000\001\001\002\000";
/*     */   private Reader zzReader;
/*     */   private int zzState;
/*     */   
/*     */   private static int[] zzUnpackAttribute() {
/* 196 */     int[] result = new int[24];
/* 197 */     int offset = 0;
/* 198 */     offset = zzUnpackAttribute("\002\000\001\001\001\t\001\001\001\t\001\001\001\t\003\001\n\000\001\001\002\000", offset, result);
/* 199 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAttribute(String packed, int offset, int[] result) {
/* 203 */     int i = 0;
/* 204 */     int j = offset;
/* 205 */     int l = packed.length();
/* 206 */     label10: while (i < l) {
/* 207 */       int count = packed.charAt(i++);
/* 208 */       int value = packed.charAt(i++); while (true)
/* 209 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 211 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 221 */   private int zzLexicalState = 0;
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
/*     */   private boolean first;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HostsTokenMaker() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addHyperlinkToken(int start, int end, int tokenType) {
/* 266 */     int so = start + this.offsetShift;
/* 267 */     addToken(this.zzBuffer, start, end, tokenType, so, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToken(int tokenType) {
/* 277 */     addToken(this.zzStartRead, this.zzMarkedPos - 1, tokenType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToken(int start, int end, int tokenType) {
/* 287 */     int so = start + this.offsetShift;
/* 288 */     addToken(this.zzBuffer, start, end, tokenType, so);
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
/* 304 */     super.addToken(array, start, end, tokenType, startOffset);
/* 305 */     this.zzStartRead = this.zzMarkedPos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getLineCommentStartAndEnd(int languageIndex) {
/* 314 */     return new String[] { "#", null };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getMarkOccurrencesOfTokenType(int type) {
/* 323 */     return (type == 6);
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
/* 342 */     resetTokenList();
/* 343 */     this.offsetShift = -text.offset + startOffset;
/* 344 */     this.first = true;
/*     */ 
/*     */     
/* 347 */     int state = 0;
/*     */     
/* 349 */     this.s = text;
/*     */     try {
/* 351 */       yyreset(this.zzReader);
/* 352 */       yybegin(state);
/* 353 */       return yylex();
/* 354 */     } catch (IOException ioe) {
/* 355 */       ioe.printStackTrace();
/* 356 */       return (Token)new TokenImpl();
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
/* 369 */     return (this.zzCurrentPos >= this.s.offset + this.s.count);
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
/* 385 */     this.zzBuffer = this.s.array;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 393 */     this.zzStartRead = this.s.offset;
/* 394 */     this.zzEndRead = this.zzStartRead + this.s.count - 1;
/* 395 */     this.zzCurrentPos = this.zzMarkedPos = this.s.offset;
/* 396 */     this.zzLexicalState = 0;
/* 397 */     this.zzReader = reader;
/* 398 */     this.zzAtEOF = false;
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
/*     */   public HostsTokenMaker(Reader in) {
/* 411 */     this.zzReader = in;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HostsTokenMaker(InputStream in) {
/* 421 */     this(new InputStreamReader(in));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static char[] zzUnpackCMap(String packed) {
/* 431 */     char[] map = new char[65536];
/* 432 */     int i = 0;
/* 433 */     int j = 0;
/* 434 */     label10: while (i < 94) {
/* 435 */       int count = packed.charAt(i++);
/* 436 */       char value = packed.charAt(i++); while (true)
/* 437 */       { map[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 439 */     }  return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void yyclose() throws IOException {
/* 447 */     this.zzAtEOF = true;
/* 448 */     this.zzEndRead = this.zzStartRead;
/*     */     
/* 450 */     if (this.zzReader != null) {
/* 451 */       this.zzReader.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int yystate() {
/* 459 */     return this.zzLexicalState;
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
/* 470 */     this.zzLexicalState = newState;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String yytext() {
/* 478 */     return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
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
/* 494 */     return this.zzBuffer[this.zzStartRead + pos];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int yylength() {
/* 502 */     return this.zzMarkedPos - this.zzStartRead;
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
/* 523 */       message = ZZ_ERROR_MSG[errorCode];
/*     */     }
/* 525 */     catch (ArrayIndexOutOfBoundsException e) {
/* 526 */       message = ZZ_ERROR_MSG[0];
/*     */     } 
/*     */     
/* 529 */     throw new Error(message);
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
/* 542 */     if (number > yylength()) {
/* 543 */       zzScanError(2);
/*     */     }
/* 545 */     this.zzMarkedPos -= number;
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
/* 563 */     int zzEndReadL = this.zzEndRead;
/* 564 */     char[] zzBufferL = this.zzBuffer;
/* 565 */     char[] zzCMapL = ZZ_CMAP;
/*     */     
/* 567 */     int[] zzTransL = ZZ_TRANS;
/* 568 */     int[] zzRowMapL = ZZ_ROWMAP;
/* 569 */     int[] zzAttrL = ZZ_ATTRIBUTE;
/*     */     
/*     */     while (true) {
/* 572 */       int zzInput, temp, zzMarkedPosL = this.zzMarkedPos;
/*     */       
/* 574 */       int zzAction = -1;
/*     */       
/* 576 */       int zzCurrentPosL = this.zzCurrentPos = this.zzStartRead = zzMarkedPosL;
/*     */       
/* 578 */       this.zzState = this.zzLexicalState;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 584 */         if (zzCurrentPosL < zzEndReadL)
/* 585 */         { zzInput = zzBufferL[zzCurrentPosL++]; }
/* 586 */         else { if (this.zzAtEOF) {
/* 587 */             int i = -1;
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 592 */           this.zzCurrentPos = zzCurrentPosL;
/* 593 */           this.zzMarkedPos = zzMarkedPosL;
/* 594 */           boolean eof = zzRefill();
/*     */           
/* 596 */           zzCurrentPosL = this.zzCurrentPos;
/* 597 */           zzMarkedPosL = this.zzMarkedPos;
/* 598 */           zzBufferL = this.zzBuffer;
/* 599 */           zzEndReadL = this.zzEndRead;
/* 600 */           if (eof) {
/* 601 */             int i = -1;
/*     */             
/*     */             break;
/*     */           } 
/* 605 */           zzInput = zzBufferL[zzCurrentPosL++]; }
/*     */ 
/*     */         
/* 608 */         int zzNext = zzTransL[zzRowMapL[this.zzState] + zzCMapL[zzInput]];
/* 609 */         if (zzNext == -1)
/* 610 */           break;  this.zzState = zzNext;
/*     */         
/* 612 */         int zzAttributes = zzAttrL[this.zzState];
/* 613 */         if ((zzAttributes & 0x1) == 1) {
/* 614 */           zzAction = this.zzState;
/* 615 */           zzMarkedPosL = zzCurrentPosL;
/* 616 */           if ((zzAttributes & 0x8) == 8) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 623 */       this.zzMarkedPos = zzMarkedPosL;
/*     */       
/* 625 */       switch ((zzAction < 0) ? zzAction : ZZ_ACTION[zzAction]) {
/*     */         case 2:
/* 627 */           addNullToken(); return (Token)this.firstToken;
/*     */         case 8:
/*     */           continue;
/*     */         case 7:
/* 631 */           temp = this.zzStartRead; addToken(this.start, this.zzStartRead - 1, 1); addHyperlinkToken(temp, this.zzMarkedPos - 1, 1); this.start = this.zzMarkedPos; continue;
/*     */         case 9:
/*     */           continue;
/*     */         case 4:
/* 635 */           this.start = this.zzMarkedPos - 1; yybegin(1); continue;
/*     */         case 10:
/*     */           continue;
/*     */         case 3:
/* 639 */           addToken(21); continue;
/*     */         case 11:
/*     */           continue;
/*     */         case 6:
/* 643 */           addToken(this.start, this.zzStartRead - 1, 1); addNullToken(); return (Token)this.firstToken;
/*     */         case 12:
/*     */           continue;
/*     */         case 1:
/* 647 */           addToken(this.first ? 6 : 20);
/* 648 */           this.first = false;
/*     */           continue;
/*     */         
/*     */         case 13:
/*     */         case 5:
/*     */         case 14:
/*     */           continue;
/*     */       } 
/* 656 */       if (zzInput == -1 && this.zzStartRead == this.zzCurrentPos) {
/* 657 */         this.zzAtEOF = true;
/* 658 */         switch (this.zzLexicalState) {
/*     */           case 1:
/* 660 */             addToken(this.start, this.zzStartRead - 1, 1); addNullToken(); return (Token)this.firstToken;
/*     */           case 25:
/*     */             continue;
/*     */           case 0:
/* 664 */             addNullToken(); return (Token)this.firstToken;
/*     */           case 26:
/*     */             continue;
/*     */         } 
/* 668 */         return null;
/*     */       } 
/*     */ 
/*     */       
/* 672 */       zzScanError(1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\modes\HostsTokenMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */