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
/*     */ public class PlainTextTokenMaker
/*     */   extends AbstractJFlexTokenMaker
/*     */ {
/*     */   public static final int YYEOF = -1;
/*     */   public static final int YYINITIAL = 0;
/*     */   private static final String ZZ_CMAP_PACKED = "\t\000\001\003\001\002\025\000\001\003\001\004\001\000\001\004\001\006\001\004\007\004\001\004\001\021\001\005\n\001\001\017\001\004\001\000\001\004\001\000\002\004\032\001\001\004\001\000\001\004\001\000\001\004\001\000\004\001\001\016\001\013\001\001\001\007\001\f\002\001\001\r\003\001\001\t\002\001\001\n\001\b\002\001\001\020\003\001\003\000\001\004ﾁ\000";
/*  79 */   private static final char[] ZZ_CMAP = zzUnpackCMap("\t\000\001\003\001\002\025\000\001\003\001\004\001\000\001\004\001\006\001\004\007\004\001\004\001\021\001\005\n\001\001\017\001\004\001\000\001\004\001\000\002\004\032\001\001\004\001\000\001\004\001\000\001\004\001\000\004\001\001\016\001\013\001\001\001\007\001\f\002\001\001\r\003\001\001\t\002\001\001\n\001\b\002\001\001\020\003\001\003\000\001\004ﾁ\000");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   private static final int[] ZZ_ACTION = zzUnpackAction();
/*     */   
/*     */   private static final String ZZ_ACTION_PACKED_0 = "\001\000\002\001\001\002\001\003\f\001\001\000\001\004\002\000";
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackAction() {
/*  90 */     int[] result = new int[21];
/*  91 */     int offset = 0;
/*  92 */     offset = zzUnpackAction("\001\000\002\001\001\002\001\003\f\001\001\000\001\004\002\000", offset, result);
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
/*     */   private static final String ZZ_ROWMAP_PACKED_0 = "\000\000\000\022\000$\000\022\0006\000H\000Z\000l\000~\000\000¢\000´\000Æ\000Ø\000ê\000ü\000Ď\000Ġ\000Ĳ\000ń\000Ĳ";
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackRowMap() {
/* 120 */     int[] result = new int[21];
/* 121 */     int offset = 0;
/* 122 */     offset = zzUnpackRowMap("\000\000\000\022\000$\000\022\0006\000H\000Z\000l\000~\000\000¢\000´\000Æ\000Ø\000ê\000ü\000Ď\000Ġ\000Ĳ\000ń\000Ĳ", offset, result);
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
/*     */   
/*     */   private static final String ZZ_TRANS_PACKED_0 = "\001\002\001\003\001\004\001\005\003\002\001\006\003\003\001\007\003\003\001\002\001\b\001\002\023\000\001\003\005\000\b\003\001\000\001\003\004\000\001\005\017\000\001\003\005\000\001\003\001\t\006\003\001\000\001\003\002\000\001\003\005\000\001\003\001\n\003\003\001\013\002\003\001\000\001\003\002\000\001\003\005\000\b\003\001\000\001\f\002\000\001\003\005\000\001\003\001\r\006\003\001\000\001\003\002\000\001\003\005\000\002\003\001\016\005\003\001\000\001\003\002\000\001\003\005\000\006\003\001\017\001\003\001\000\001\003\002\000\001\003\005\000\b\003\001\000\001\020\002\000\001\003\005\000\002\003\001\021\005\003\001\000\001\003\002\000\001\003\005\000\b\003\001\022\001\003\002\000\001\003\005\000\007\003\001\016\001\000\001\003\002\000\001\003\005\000\b\003\001\000\001\003\001\023\001\000\001\003\005\000\003\003\001\016\004\003\001\022\001\003\006\000\001\024\r\000\001\023\002\000\001\025\n\023\001\025\001\023\001\025\005\000\001\023\f\000";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int ZZ_UNKNOWN_ERROR = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int ZZ_NO_MATCH = 1;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int ZZ_PUSHBACK_2BIG = 2;
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackTrans() {
/* 161 */     int[] result = new int[342];
/* 162 */     int offset = 0;
/* 163 */     offset = zzUnpackTrans("\001\002\001\003\001\004\001\005\003\002\001\006\003\003\001\007\003\003\001\002\001\b\001\002\023\000\001\003\005\000\b\003\001\000\001\003\004\000\001\005\017\000\001\003\005\000\001\003\001\t\006\003\001\000\001\003\002\000\001\003\005\000\001\003\001\n\003\003\001\013\002\003\001\000\001\003\002\000\001\003\005\000\b\003\001\000\001\f\002\000\001\003\005\000\001\003\001\r\006\003\001\000\001\003\002\000\001\003\005\000\002\003\001\016\005\003\001\000\001\003\002\000\001\003\005\000\006\003\001\017\001\003\001\000\001\003\002\000\001\003\005\000\b\003\001\000\001\020\002\000\001\003\005\000\002\003\001\021\005\003\001\000\001\003\002\000\001\003\005\000\b\003\001\022\001\003\002\000\001\003\005\000\007\003\001\016\001\000\001\003\002\000\001\003\005\000\b\003\001\000\001\003\001\023\001\000\001\003\005\000\003\003\001\016\004\003\001\022\001\003\006\000\001\024\r\000\001\023\002\000\001\025\n\023\001\025\001\023\001\025\005\000\001\023\f\000", offset, result);
/* 164 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackTrans(String packed, int offset, int[] result) {
/* 168 */     int i = 0;
/* 169 */     int j = offset;
/* 170 */     int l = packed.length();
/* 171 */     label10: while (i < l) {
/* 172 */       int count = packed.charAt(i++);
/* 173 */       int value = packed.charAt(i++);
/* 174 */       value--; while (true)
/* 175 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 177 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 187 */   private static final String[] ZZ_ERROR_MSG = new String[] { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 196 */   private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
/*     */   private static final String ZZ_ATTRIBUTE_PACKED_0 = "\001\000\001\t\001\001\001\t\r\001\001\000\001\001\002\000";
/*     */   private Reader zzReader;
/*     */   private int zzState;
/*     */   
/*     */   private static int[] zzUnpackAttribute() {
/* 202 */     int[] result = new int[21];
/* 203 */     int offset = 0;
/* 204 */     offset = zzUnpackAttribute("\001\000\001\t\001\001\001\t\r\001\001\000\001\001\002\000", offset, result);
/* 205 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAttribute(String packed, int offset, int[] result) {
/* 209 */     int i = 0;
/* 210 */     int j = offset;
/* 211 */     int l = packed.length();
/* 212 */     label10: while (i < l) {
/* 213 */       int count = packed.charAt(i++);
/* 214 */       int value = packed.charAt(i++); while (true)
/* 215 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 217 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 227 */   private int zzLexicalState = 0;
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
/*     */   public PlainTextTokenMaker() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToken(int tokenType, boolean link) {
/* 267 */     int so = this.zzStartRead + this.offsetShift;
/* 268 */     addToken(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - 1, tokenType, so, link);
/* 269 */     this.zzStartRead = this.zzMarkedPos;
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
/*     */   public int getLastTokenTypeOnLine(Segment text, int initialTokenType) {
/* 285 */     return 0;
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
/*     */   public String[] getLineCommentStartAndEnd(int languageIndex) {
/* 297 */     return null;
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
/*     */   public boolean getMarkOccurrencesOfTokenType(int type) {
/* 311 */     return false;
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
/* 330 */     resetTokenList();
/* 331 */     this.offsetShift = -text.offset + startOffset;
/*     */ 
/*     */     
/* 334 */     this.s = text;
/*     */     try {
/* 336 */       yyreset(this.zzReader);
/* 337 */       yybegin(0);
/* 338 */       return yylex();
/* 339 */     } catch (IOException ioe) {
/* 340 */       ioe.printStackTrace();
/* 341 */       return (Token)new TokenImpl();
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
/* 355 */     return (this.zzCurrentPos >= this.s.offset + this.s.count);
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
/* 371 */     this.zzBuffer = this.s.array;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 379 */     this.zzStartRead = this.s.offset;
/* 380 */     this.zzEndRead = this.zzStartRead + this.s.count - 1;
/* 381 */     this.zzCurrentPos = this.zzMarkedPos = this.s.offset;
/* 382 */     this.zzLexicalState = 0;
/* 383 */     this.zzReader = reader;
/* 384 */     this.zzAtEOF = false;
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
/*     */   public PlainTextTokenMaker(Reader in) {
/* 397 */     this.zzReader = in;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PlainTextTokenMaker(InputStream in) {
/* 407 */     this(new InputStreamReader(in));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static char[] zzUnpackCMap(String packed) {
/* 417 */     char[] map = new char[65536];
/* 418 */     int i = 0;
/* 419 */     int j = 0;
/* 420 */     label10: while (i < 94) {
/* 421 */       int count = packed.charAt(i++);
/* 422 */       char value = packed.charAt(i++); while (true)
/* 423 */       { map[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 425 */     }  return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void yyclose() throws IOException {
/* 433 */     this.zzAtEOF = true;
/* 434 */     this.zzEndRead = this.zzStartRead;
/*     */     
/* 436 */     if (this.zzReader != null) {
/* 437 */       this.zzReader.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int yystate() {
/* 445 */     return this.zzLexicalState;
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
/* 456 */     this.zzLexicalState = newState;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String yytext() {
/* 464 */     return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
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
/* 480 */     return this.zzBuffer[this.zzStartRead + pos];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int yylength() {
/* 488 */     return this.zzMarkedPos - this.zzStartRead;
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
/* 509 */       message = ZZ_ERROR_MSG[errorCode];
/*     */     }
/* 511 */     catch (ArrayIndexOutOfBoundsException e) {
/* 512 */       message = ZZ_ERROR_MSG[0];
/*     */     } 
/*     */     
/* 515 */     throw new Error(message);
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
/* 528 */     if (number > yylength()) {
/* 529 */       zzScanError(2);
/*     */     }
/* 531 */     this.zzMarkedPos -= number;
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
/* 549 */     int zzEndReadL = this.zzEndRead;
/* 550 */     char[] zzBufferL = this.zzBuffer;
/* 551 */     char[] zzCMapL = ZZ_CMAP;
/*     */     
/* 553 */     int[] zzTransL = ZZ_TRANS;
/* 554 */     int[] zzRowMapL = ZZ_ROWMAP;
/* 555 */     int[] zzAttrL = ZZ_ATTRIBUTE;
/*     */     
/*     */     while (true) {
/* 558 */       int zzInput, zzMarkedPosL = this.zzMarkedPos;
/*     */       
/* 560 */       int zzAction = -1;
/*     */       
/* 562 */       int zzCurrentPosL = this.zzCurrentPos = this.zzStartRead = zzMarkedPosL;
/*     */       
/* 564 */       this.zzState = this.zzLexicalState;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 570 */         if (zzCurrentPosL < zzEndReadL)
/* 571 */         { zzInput = zzBufferL[zzCurrentPosL++]; }
/* 572 */         else { if (this.zzAtEOF) {
/* 573 */             int i = -1;
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 578 */           this.zzCurrentPos = zzCurrentPosL;
/* 579 */           this.zzMarkedPos = zzMarkedPosL;
/* 580 */           boolean eof = zzRefill();
/*     */           
/* 582 */           zzCurrentPosL = this.zzCurrentPos;
/* 583 */           zzMarkedPosL = this.zzMarkedPos;
/* 584 */           zzBufferL = this.zzBuffer;
/* 585 */           zzEndReadL = this.zzEndRead;
/* 586 */           if (eof) {
/* 587 */             int i = -1;
/*     */             
/*     */             break;
/*     */           } 
/* 591 */           zzInput = zzBufferL[zzCurrentPosL++]; }
/*     */ 
/*     */         
/* 594 */         int zzNext = zzTransL[zzRowMapL[this.zzState] + zzCMapL[zzInput]];
/* 595 */         if (zzNext == -1)
/* 596 */           break;  this.zzState = zzNext;
/*     */         
/* 598 */         int zzAttributes = zzAttrL[this.zzState];
/* 599 */         if ((zzAttributes & 0x1) == 1) {
/* 600 */           zzAction = this.zzState;
/* 601 */           zzMarkedPosL = zzCurrentPosL;
/* 602 */           if ((zzAttributes & 0x8) == 8) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 609 */       this.zzMarkedPos = zzMarkedPosL;
/*     */       
/* 611 */       switch ((zzAction < 0) ? zzAction : ZZ_ACTION[zzAction]) {
/*     */         case 3:
/* 613 */           addToken(21, false); continue;
/*     */         case 5:
/*     */           continue;
/*     */         case 2:
/* 617 */           addNullToken(); return (Token)this.firstToken;
/*     */         case 6:
/*     */           continue;
/*     */         case 4:
/* 621 */           addToken(20, true); continue;
/*     */         case 7:
/*     */           continue;
/*     */         case 1:
/* 625 */           addToken(20, false); continue;
/*     */         case 8:
/*     */           continue;
/*     */       } 
/* 629 */       if (zzInput == -1 && this.zzStartRead == this.zzCurrentPos) {
/* 630 */         this.zzAtEOF = true;
/* 631 */         switch (this.zzLexicalState) {
/*     */           case 0:
/* 633 */             addNullToken(); return (Token)this.firstToken;
/*     */           case 22:
/*     */             continue;
/*     */         } 
/* 637 */         return null;
/*     */       } 
/*     */ 
/*     */       
/* 641 */       zzScanError(1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\modes\PlainTextTokenMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */