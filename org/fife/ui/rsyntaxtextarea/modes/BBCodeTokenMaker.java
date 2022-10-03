/*     */ package org.fife.ui.rsyntaxtextarea.modes;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import javax.swing.text.Segment;
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
/*     */ public class BBCodeTokenMaker
/*     */   extends AbstractMarkupTokenMaker
/*     */ {
/*     */   public static final int YYEOF = -1;
/*     */   public static final int INTAG = 1;
/*     */   public static final int YYINITIAL = 0;
/*     */   private static final String ZZ_CMAP_PACKED = "\t\000\001\001\001\002\001\000\001\001\023\000\001\001\016\000\001\027\r\000\001\030\035\000\001\003\001\000\001\004\004\000\001\005\001\013\001\026\001\n\001\000\001\023\001\000\001\006\002\000\001\r\001\022\001\017\001\f\001\000\001\021\001\016\001\b\001\020\001\007\001\025\002\000\001\024\001\tﾅ\000";
/*  79 */   private static final char[] ZZ_CMAP = zzUnpackCMap("\t\000\001\001\001\002\001\000\001\001\023\000\001\001\016\000\001\027\r\000\001\030\035\000\001\003\001\000\001\004\004\000\001\005\001\013\001\026\001\n\001\000\001\023\001\000\001\006\002\000\001\r\001\022\001\017\001\f\001\000\001\021\001\016\001\b\001\020\001\007\001\025\002\000\001\024\001\tﾅ\000");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  84 */   private static final int[] ZZ_ACTION = zzUnpackAction();
/*     */ 
/*     */   
/*     */   private static final String ZZ_ACTION_PACKED_0 = "\002\000\001\001\001\002\001\003\001\004\001\005\001\006\001\007\004\b\006\005\001\t\001\n\001\004\023\005";
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackAction() {
/*  91 */     int[] result = new int[41];
/*  92 */     int offset = 0;
/*  93 */     offset = zzUnpackAction("\002\000\001\001\001\002\001\003\001\004\001\005\001\006\001\007\004\b\006\005\001\t\001\n\001\004\023\005", offset, result);
/*  94 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAction(String packed, int offset, int[] result) {
/*  98 */     int i = 0;
/*  99 */     int j = offset;
/* 100 */     int l = packed.length();
/* 101 */     label10: while (i < l) {
/* 102 */       int count = packed.charAt(i++);
/* 103 */       int value = packed.charAt(i++); while (true)
/* 104 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 106 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZZ_ROWMAP_PACKED_0 = "\000\000\000\031\0002\000K\000d\000}\000\000d\000d\000\000¯\000È\000á\000ú\000ē\000Ĭ\000Ņ\000Ş\000ŷ\000Ɛ\000d\000d\000Ʃ\000ǂ\000Ǜ\000Ǵ\000ȍ\000Ȧ\000ȿ\000ɘ\000ɱ\000ʊ\000ʣ\000ʼ\000˕\000ˮ\000̇\000̠\000̹\000͒\000ͫ";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackRowMap() {
/* 124 */     int[] result = new int[41];
/* 125 */     int offset = 0;
/* 126 */     offset = zzUnpackRowMap("\000\000\000\031\0002\000K\000d\000}\000\000d\000d\000\000¯\000È\000á\000ú\000ē\000Ĭ\000Ņ\000Ş\000ŷ\000Ɛ\000d\000d\000Ʃ\000ǂ\000Ǜ\000Ǵ\000ȍ\000Ȧ\000ȿ\000ɘ\000ɱ\000ʊ\000ʣ\000ʼ\000˕\000ˮ\000̇\000̠\000̹\000͒\000ͫ", offset, result);
/* 127 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackRowMap(String packed, int offset, int[] result) {
/* 131 */     int i = 0;
/* 132 */     int j = offset;
/* 133 */     int l = packed.length();
/* 134 */     while (i < l) {
/* 135 */       int high = packed.charAt(i++) << 16;
/* 136 */       result[j++] = high | packed.charAt(i++);
/*     */     } 
/* 138 */     return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 144 */   private static final int[] ZZ_TRANS = zzUnpackTrans();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZZ_TRANS_PACKED_0 = "\001\003\001\004\001\005\001\006\025\003\001\007\001\004\001\000\001\b\001\t\001\n\001\013\001\f\001\r\002\007\001\016\001\017\001\020\003\007\001\021\001\007\001\022\001\023\002\007\001\024\001\025\001\003\003\000\025\003\001\000\001\004G\000\001\026\001\000\001\007\004\000\022\007\002\000\001\007\004\000\r\007\001\027\004\007\002\000\001\007\004\000\b\007\001\n\001\017\b\007\002\000\001\007\004\000\001\007\001\030\020\007\002\000\001\007\004\000\005\007\001\031\001\007\001\032\n\007\002\000\001\007\004\000\b\007\001\n\t\007\002\000\001\007\004\000\001\007\001\n\020\007\002\000\001\007\004\000\002\007\001\033\017\007\002\000\001\007\004\000\020\007\001\034\001\007\002\000\001\007\004\000\007\007\001\035\n\007\006\000\001\t\024\000\001\007\004\000\016\007\001\n\003\007\002\000\001\007\004\000\004\007\001\036\r\007\002\000\001\007\004\000\n\007\001\037\007\007\002\000\001\007\004\000\b\007\001 \t\007\002\000\001\007\004\000\007\007\001!\n\007\002\000\001\007\004\000\001\007\001\"\020\007\002\000\001\007\004\000\002\007\001#\017\007\002\000\001\007\004\000\005\007\001\n\f\007\002\000\001\007\004\000\013\007\001$\006\007\002\000\001\007\004\000\007\007\001%\n\007\002\000\001\007\004\000\013\007\001\036\006\007\002\000\001\007\004\000\021\007\001&\002\000\001\007\004\000\013\007\001'\006\007\002\000\001\007\004\000\005\007\001%\f\007\002\000\001\007\004\000\t\007\001\n\b\007\002\000\001\007\004\000\005\007\001(\f\007\002\000\001\007\004\000\002\007\001)\017\007\002\000\001\007\004\000\007\007\001\n\n\007\002\000\001\007\004\000\001\036\021\007\002\000";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int ZZ_UNKNOWN_ERROR = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int ZZ_NO_MATCH = 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int ZZ_PUSHBACK_2BIG = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackTrans() {
/* 176 */     int[] result = new int[900];
/* 177 */     int offset = 0;
/* 178 */     offset = zzUnpackTrans("\001\003\001\004\001\005\001\006\025\003\001\007\001\004\001\000\001\b\001\t\001\n\001\013\001\f\001\r\002\007\001\016\001\017\001\020\003\007\001\021\001\007\001\022\001\023\002\007\001\024\001\025\001\003\003\000\025\003\001\000\001\004G\000\001\026\001\000\001\007\004\000\022\007\002\000\001\007\004\000\r\007\001\027\004\007\002\000\001\007\004\000\b\007\001\n\001\017\b\007\002\000\001\007\004\000\001\007\001\030\020\007\002\000\001\007\004\000\005\007\001\031\001\007\001\032\n\007\002\000\001\007\004\000\b\007\001\n\t\007\002\000\001\007\004\000\001\007\001\n\020\007\002\000\001\007\004\000\002\007\001\033\017\007\002\000\001\007\004\000\020\007\001\034\001\007\002\000\001\007\004\000\007\007\001\035\n\007\006\000\001\t\024\000\001\007\004\000\016\007\001\n\003\007\002\000\001\007\004\000\004\007\001\036\r\007\002\000\001\007\004\000\n\007\001\037\007\007\002\000\001\007\004\000\b\007\001 \t\007\002\000\001\007\004\000\007\007\001!\n\007\002\000\001\007\004\000\001\007\001\"\020\007\002\000\001\007\004\000\002\007\001#\017\007\002\000\001\007\004\000\005\007\001\n\f\007\002\000\001\007\004\000\013\007\001$\006\007\002\000\001\007\004\000\007\007\001%\n\007\002\000\001\007\004\000\013\007\001\036\006\007\002\000\001\007\004\000\021\007\001&\002\000\001\007\004\000\013\007\001'\006\007\002\000\001\007\004\000\005\007\001%\f\007\002\000\001\007\004\000\t\007\001\n\b\007\002\000\001\007\004\000\005\007\001(\f\007\002\000\001\007\004\000\002\007\001)\017\007\002\000\001\007\004\000\007\007\001\n\n\007\002\000\001\007\004\000\001\036\021\007\002\000", offset, result);
/* 179 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackTrans(String packed, int offset, int[] result) {
/* 183 */     int i = 0;
/* 184 */     int j = offset;
/* 185 */     int l = packed.length();
/* 186 */     label10: while (i < l) {
/* 187 */       int count = packed.charAt(i++);
/* 188 */       int value = packed.charAt(i++);
/* 189 */       value--; while (true)
/* 190 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 192 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 202 */   private static final String[] ZZ_ERROR_MSG = new String[] { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 211 */   private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
/*     */   private static final String ZZ_ATTRIBUTE_PACKED_0 = "\002\000\002\001\001\t\002\001\002\t\013\001\002\t\023\001";
/*     */   private Reader zzReader;
/*     */   private int zzState;
/*     */   
/*     */   private static int[] zzUnpackAttribute() {
/* 217 */     int[] result = new int[41];
/* 218 */     int offset = 0;
/* 219 */     offset = zzUnpackAttribute("\002\000\002\001\001\t\002\001\002\t\013\001\002\t\023\001", offset, result);
/* 220 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAttribute(String packed, int offset, int[] result) {
/* 224 */     int i = 0;
/* 225 */     int j = offset;
/* 226 */     int l = packed.length();
/* 227 */     label10: while (i < l) {
/* 228 */       int count = packed.charAt(i++);
/* 229 */       int value = packed.charAt(i++); while (true)
/* 230 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 232 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 242 */   private int zzLexicalState = 0;
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
/*     */   public static final int INTERNAL_INTAG = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean completeCloseTags = true;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BBCodeTokenMaker() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToken(int tokenType) {
/* 293 */     addToken(this.zzStartRead, this.zzMarkedPos - 1, tokenType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToken(int start, int end, int tokenType) {
/* 303 */     int so = start + this.offsetShift;
/* 304 */     addToken(this.zzBuffer, start, end, tokenType, so);
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
/* 320 */     super.addToken(array, start, end, tokenType, startOffset);
/* 321 */     this.zzStartRead = this.zzMarkedPos;
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
/*     */   public boolean getCompleteCloseTags() {
/* 334 */     return completeCloseTags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getLineCommentStartAndEnd(int languageIndex) {
/* 345 */     return null;
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
/* 364 */     resetTokenList();
/* 365 */     this.offsetShift = -text.offset + startOffset;
/*     */ 
/*     */     
/* 368 */     int state = 0;
/* 369 */     switch (initialTokenType) {
/*     */       case -1:
/* 371 */         state = 1;
/* 372 */         this.start = text.offset;
/*     */         break;
/*     */       default:
/* 375 */         state = 0;
/*     */         break;
/*     */     } 
/* 378 */     this.s = text;
/*     */     try {
/* 380 */       yyreset(this.zzReader);
/* 381 */       yybegin(state);
/* 382 */       return yylex();
/* 383 */     } catch (IOException ioe) {
/* 384 */       ioe.printStackTrace();
/* 385 */       return (Token)new TokenImpl();
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
/*     */   public static void setCompleteCloseTags(boolean complete) {
/* 398 */     completeCloseTags = complete;
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
/* 409 */     return (this.zzCurrentPos >= this.s.offset + this.s.count);
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
/* 425 */     this.zzBuffer = this.s.array;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 433 */     this.zzStartRead = this.s.offset;
/* 434 */     this.zzEndRead = this.zzStartRead + this.s.count - 1;
/* 435 */     this.zzCurrentPos = this.zzMarkedPos = this.s.offset;
/* 436 */     this.zzLexicalState = 0;
/* 437 */     this.zzReader = reader;
/* 438 */     this.zzAtEOF = false;
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
/*     */   public BBCodeTokenMaker(Reader in) {
/* 451 */     this.zzReader = in;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BBCodeTokenMaker(InputStream in) {
/* 461 */     this(new InputStreamReader(in));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static char[] zzUnpackCMap(String packed) {
/* 471 */     char[] map = new char[65536];
/* 472 */     int i = 0;
/* 473 */     int j = 0;
/* 474 */     label10: while (i < 80) {
/* 475 */       int count = packed.charAt(i++);
/* 476 */       char value = packed.charAt(i++); while (true)
/* 477 */       { map[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 479 */     }  return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void yyclose() throws IOException {
/* 487 */     this.zzAtEOF = true;
/* 488 */     this.zzEndRead = this.zzStartRead;
/*     */     
/* 490 */     if (this.zzReader != null) {
/* 491 */       this.zzReader.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int yystate() {
/* 499 */     return this.zzLexicalState;
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
/* 510 */     this.zzLexicalState = newState;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String yytext() {
/* 518 */     return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
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
/* 534 */     return this.zzBuffer[this.zzStartRead + pos];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int yylength() {
/* 542 */     return this.zzMarkedPos - this.zzStartRead;
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
/* 563 */       message = ZZ_ERROR_MSG[errorCode];
/*     */     }
/* 565 */     catch (ArrayIndexOutOfBoundsException e) {
/* 566 */       message = ZZ_ERROR_MSG[0];
/*     */     } 
/*     */     
/* 569 */     throw new Error(message);
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
/* 582 */     if (number > yylength()) {
/* 583 */       zzScanError(2);
/*     */     }
/* 585 */     this.zzMarkedPos -= number;
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
/* 603 */     int zzEndReadL = this.zzEndRead;
/* 604 */     char[] zzBufferL = this.zzBuffer;
/* 605 */     char[] zzCMapL = ZZ_CMAP;
/*     */     
/* 607 */     int[] zzTransL = ZZ_TRANS;
/* 608 */     int[] zzRowMapL = ZZ_ROWMAP;
/* 609 */     int[] zzAttrL = ZZ_ATTRIBUTE;
/*     */     
/*     */     while (true) {
/* 612 */       int zzInput, zzMarkedPosL = this.zzMarkedPos;
/*     */       
/* 614 */       int zzAction = -1;
/*     */       
/* 616 */       int zzCurrentPosL = this.zzCurrentPos = this.zzStartRead = zzMarkedPosL;
/*     */       
/* 618 */       this.zzState = this.zzLexicalState;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 624 */         if (zzCurrentPosL < zzEndReadL)
/* 625 */         { zzInput = zzBufferL[zzCurrentPosL++]; }
/* 626 */         else { if (this.zzAtEOF) {
/* 627 */             int i = -1;
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 632 */           this.zzCurrentPos = zzCurrentPosL;
/* 633 */           this.zzMarkedPos = zzMarkedPosL;
/* 634 */           boolean eof = zzRefill();
/*     */           
/* 636 */           zzCurrentPosL = this.zzCurrentPos;
/* 637 */           zzMarkedPosL = this.zzMarkedPos;
/* 638 */           zzBufferL = this.zzBuffer;
/* 639 */           zzEndReadL = this.zzEndRead;
/* 640 */           if (eof) {
/* 641 */             int i = -1;
/*     */             
/*     */             break;
/*     */           } 
/* 645 */           zzInput = zzBufferL[zzCurrentPosL++]; }
/*     */ 
/*     */         
/* 648 */         int zzNext = zzTransL[zzRowMapL[this.zzState] + zzCMapL[zzInput]];
/* 649 */         if (zzNext == -1)
/* 650 */           break;  this.zzState = zzNext;
/*     */         
/* 652 */         int zzAttributes = zzAttrL[this.zzState];
/* 653 */         if ((zzAttributes & 0x1) == 1) {
/* 654 */           zzAction = this.zzState;
/* 655 */           zzMarkedPosL = zzCurrentPosL;
/* 656 */           if ((zzAttributes & 0x8) == 8) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 663 */       this.zzMarkedPos = zzMarkedPosL;
/*     */       
/* 665 */       switch ((zzAction < 0) ? zzAction : ZZ_ACTION[zzAction]) {
/*     */         case 1:
/* 667 */           addToken(20); continue;
/*     */         case 11:
/*     */           continue;
/*     */         case 9:
/* 671 */           addToken(25); continue;
/*     */         case 12:
/*     */           continue;
/*     */         case 2:
/* 675 */           addToken(21); continue;
/*     */         case 13:
/*     */           continue;
/*     */         case 10:
/* 679 */           addToken(23); continue;
/*     */         case 14:
/*     */           continue;
/*     */         case 8:
/* 683 */           addToken(26); continue;
/*     */         case 15:
/*     */           continue;
/*     */         case 4:
/* 687 */           addToken(25); yybegin(1); continue;
/*     */         case 16:
/*     */           continue;
/*     */         case 6:
/* 691 */           addToken(20); continue;
/*     */         case 17:
/*     */           continue;
/*     */         case 5:
/* 695 */           addToken(27); continue;
/*     */         case 18:
/*     */           continue;
/*     */         case 3:
/* 699 */           addNullToken(); return (Token)this.firstToken;
/*     */         case 19:
/*     */           continue;
/*     */         case 7:
/* 703 */           yybegin(0); addToken(25); continue;
/*     */         case 20:
/*     */           continue;
/*     */       } 
/* 707 */       if (zzInput == -1 && this.zzStartRead == this.zzCurrentPos) {
/* 708 */         this.zzAtEOF = true;
/* 709 */         switch (this.zzLexicalState) {
/*     */           case 1:
/* 711 */             addToken(this.zzMarkedPos, this.zzMarkedPos, -1); return (Token)this.firstToken;
/*     */           case 42:
/*     */             continue;
/*     */           case 0:
/* 715 */             addNullToken(); return (Token)this.firstToken;
/*     */           case 43:
/*     */             continue;
/*     */         } 
/* 719 */         return null;
/*     */       } 
/*     */ 
/*     */       
/* 723 */       zzScanError(1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\modes\BBCodeTokenMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */