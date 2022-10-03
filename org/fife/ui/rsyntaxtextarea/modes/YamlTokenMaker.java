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
/*     */ 
/*     */ public class YamlTokenMaker
/*     */   extends AbstractJFlexTokenMaker
/*     */ {
/*     */   public static final int YYEOF = -1;
/*     */   public static final int YYINITIAL = 0;
/*     */   private static final String ZZ_CMAP_PACKED = "\t\000\001\r\001\004\025\000\001\r\001\t\001\b\001\f\001\000\002\t\001\003\002\n\001\t\001\000\001\013\001\t\001\013\001\000\001\002\003\002\004\002\002\001\001\t\001\013\003\000\002\t\006\001\024\000\001\n\001\005\001\n\002\000\001\t\001\001\001\002\003\001\001\002\007\000\001\007\003\000\001\007\001\000\001\007\001\006\005\000\001\n\001\000\001\nﾂ\000";
/*  81 */   private static final char[] ZZ_CMAP = zzUnpackCMap("\t\000\001\r\001\004\025\000\001\r\001\t\001\b\001\f\001\000\002\t\001\003\002\n\001\t\001\000\001\013\001\t\001\013\001\000\001\002\003\002\004\002\002\001\001\t\001\013\003\000\002\t\006\001\024\000\001\n\001\005\001\n\002\000\001\t\001\001\001\002\003\001\001\002\007\000\001\007\003\000\001\007\001\000\001\007\001\006\005\000\001\n\001\000\001\nﾂ\000");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   private static final int[] ZZ_ACTION = zzUnpackAction();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZZ_ACTION_PACKED_0 = "\001\000\001\001\001\002\001\003\001\004\001\005\001\006\001\001\001\007\001\b\001\t\001\000\001\002\002\004\001\n\001\000\001\002\001\013\001\002\001\004\001\f\001\004\001\000\001\013\001\002\001\004\001\000\001\002\001\004\001\000\001\002\001\004\001\000";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackAction() {
/*  96 */     int[] result = new int[34];
/*  97 */     int offset = 0;
/*  98 */     offset = zzUnpackAction("\001\000\001\001\001\002\001\003\001\004\001\005\001\006\001\001\001\007\001\b\001\t\001\000\001\002\002\004\001\n\001\000\001\002\001\013\001\002\001\004\001\f\001\004\001\000\001\013\001\002\001\004\001\000\001\002\001\004\001\000\001\002\001\004\001\000", offset, result);
/*  99 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAction(String packed, int offset, int[] result) {
/* 103 */     int i = 0;
/* 104 */     int j = offset;
/* 105 */     int l = packed.length();
/* 106 */     label10: while (i < l) {
/* 107 */       int count = packed.charAt(i++);
/* 108 */       int value = packed.charAt(i++); while (true)
/* 109 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 111 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZZ_ROWMAP_PACKED_0 = "\000\000\000\016\000\034\000*\0008\000*\000*\000*\000F\000T\000*\000b\000p\000~\000\000*\000\000¨\000b\000¶\000Ä\000*\000Ò\000à\000*\000î\000ü\000Ċ\000Ę\000Ħ\000Ĵ\000ł\000Ő\000Ş";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackRowMap() {
/* 128 */     int[] result = new int[34];
/* 129 */     int offset = 0;
/* 130 */     offset = zzUnpackRowMap("\000\000\000\016\000\034\000*\0008\000*\000*\000*\000F\000T\000*\000b\000p\000~\000\000*\000\000¨\000b\000¶\000Ä\000*\000Ò\000à\000*\000î\000ü\000Ċ\000Ę\000Ħ\000Ĵ\000ł\000Ő\000Ş", offset, result);
/* 131 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackRowMap(String packed, int offset, int[] result) {
/* 135 */     int i = 0;
/* 136 */     int j = offset;
/* 137 */     int l = packed.length();
/* 138 */     while (i < l) {
/* 139 */       int high = packed.charAt(i++) << 16;
/* 140 */       result[j++] = high | packed.charAt(i++);
/*     */     } 
/* 142 */     return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 148 */   private static final int[] ZZ_TRANS = zzUnpackTrans();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZZ_TRANS_PACKED_0 = "\003\002\001\003\001\004\003\002\001\005\001\006\001\007\001\b\001\t\001\n\003\002\002\000\003\002\006\000\003\003\001\013\001\f\001\r\b\003\016\000\004\005\001\016\001\017\002\005\001\020\005\005\004\t\001\000\t\t\r\000\001\n\003\f\001\013\001\f\001\021\b\f\002\022\001\003\001\023\001\000\001\003\001\024\002\003\005\022\005\016\001\025\002\016\001\026\007\016\002\005\001\000\001\005\001\027\002\005\005\016\002\000\002\f\001\000\001\f\001\030\002\f\005\000\003\022\001\031\001\000\n\022\002\032\001\031\001\000\t\022\004\016\001\000\n\016\002\033\002\016\001\025\002\016\001\026\005\016\001\000\002\034\013\000\001\022\002\035\001\031\001\000\t\022\001\016\002\036\002\016\001\025\002\016\001\026\005\016\001\000\002\037\013\000\001\022\002 \001\031\001\000\t\022\001\016\002!\002\016\001\025\002\016\001\026\005\016\001\000\002\"\013\000\001\022\002\003\001\031\001\000\t\022\001\016\002\005\002\016\001\025\002\016\001\026\005\016\001\000\002\f\013\000";
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
/* 169 */     int[] result = new int[364];
/* 170 */     int offset = 0;
/* 171 */     offset = zzUnpackTrans("\003\002\001\003\001\004\003\002\001\005\001\006\001\007\001\b\001\t\001\n\003\002\002\000\003\002\006\000\003\003\001\013\001\f\001\r\b\003\016\000\004\005\001\016\001\017\002\005\001\020\005\005\004\t\001\000\t\t\r\000\001\n\003\f\001\013\001\f\001\021\b\f\002\022\001\003\001\023\001\000\001\003\001\024\002\003\005\022\005\016\001\025\002\016\001\026\007\016\002\005\001\000\001\005\001\027\002\005\005\016\002\000\002\f\001\000\001\f\001\030\002\f\005\000\003\022\001\031\001\000\n\022\002\032\001\031\001\000\t\022\004\016\001\000\n\016\002\033\002\016\001\025\002\016\001\026\005\016\001\000\002\034\013\000\001\022\002\035\001\031\001\000\t\022\001\016\002\036\002\016\001\025\002\016\001\026\005\016\001\000\002\037\013\000\001\022\002 \001\031\001\000\t\022\001\016\002!\002\016\001\025\002\016\001\026\005\016\001\000\002\"\013\000\001\022\002\003\001\031\001\000\t\022\001\016\002\005\002\016\001\025\002\016\001\026\005\016\001\000\002\f\013\000", offset, result);
/* 172 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackTrans(String packed, int offset, int[] result) {
/* 176 */     int i = 0;
/* 177 */     int j = offset;
/* 178 */     int l = packed.length();
/* 179 */     label10: while (i < l) {
/* 180 */       int count = packed.charAt(i++);
/* 181 */       int value = packed.charAt(i++);
/* 182 */       value--; while (true)
/* 183 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 185 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 195 */   private static final String[] ZZ_ERROR_MSG = new String[] { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 204 */   private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
/*     */   
/*     */   private static final String ZZ_ATTRIBUTE_PACKED_0 = "\001\000\002\001\001\t\001\001\003\t\002\001\001\t\001\000\003\001\001\t\001\000\004\001\001\t\001\001\001\000\001\t\002\001\001\000\002\001\001\000\002\001\001\000";
/*     */   
/*     */   private Reader zzReader;
/*     */   private int zzState;
/*     */   
/*     */   private static int[] zzUnpackAttribute() {
/* 212 */     int[] result = new int[34];
/* 213 */     int offset = 0;
/* 214 */     offset = zzUnpackAttribute("\001\000\002\001\001\t\001\001\003\t\002\001\001\t\001\000\003\001\001\t\001\000\004\001\001\t\001\001\001\000\001\t\002\001\001\000\002\001\001\000\002\001\001\000", offset, result);
/* 215 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAttribute(String packed, int offset, int[] result) {
/* 219 */     int i = 0;
/* 220 */     int j = offset;
/* 221 */     int l = packed.length();
/* 222 */     label10: while (i < l) {
/* 223 */       int count = packed.charAt(i++);
/* 224 */       int value = packed.charAt(i++); while (true)
/* 225 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 227 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 237 */   private int zzLexicalState = 0;
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
/*     */   public YamlTokenMaker() {}
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
/*     */   public String[] getLineCommentStartAndEnd(int languageIndex) {
/* 311 */     return new String[] { "#", null };
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
/* 334 */     int state = 0;
/* 335 */     this.s = text;
/*     */     try {
/* 337 */       yyreset(this.zzReader);
/* 338 */       yybegin(state);
/* 339 */       return yylex();
/* 340 */     } catch (IOException ioe) {
/* 341 */       ioe.printStackTrace();
/* 342 */       return (Token)new TokenImpl();
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
/*     */   public YamlTokenMaker(Reader in) {
/* 397 */     this.zzReader = in;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public YamlTokenMaker(InputStream in) {
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
/* 420 */     label10: while (i < 98) {
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
/*     */         case 1:
/* 613 */           addToken(20); continue;
/*     */         case 13:
/*     */           continue;
/*     */         case 10:
/* 617 */           addToken(13); continue;
/*     */         case 14:
/*     */           continue;
/*     */         case 2:
/* 621 */           addToken(38); addNullToken(); return (Token)this.firstToken;
/*     */         case 15:
/*     */           continue;
/*     */         case 8:
/* 625 */           addToken(21); continue;
/*     */         case 16:
/*     */           continue;
/*     */         case 11:
/* 629 */           addToken(38); continue;
/*     */         case 17:
/*     */           continue;
/*     */         case 7:
/* 633 */           addToken(1); continue;
/*     */         case 18:
/*     */           continue;
/*     */         case 5:
/* 637 */           addToken(23); continue;
/*     */         case 19:
/*     */           continue;
/*     */         case 9:
/* 641 */           addToken(14); continue;
/*     */         case 20:
/*     */           continue;
/*     */         case 4:
/* 645 */           addToken(37); addNullToken(); return (Token)this.firstToken;
/*     */         case 21:
/*     */           continue;
/*     */         case 3:
/* 649 */           addNullToken(); return (Token)this.firstToken;
/*     */         case 22:
/*     */           continue;
/*     */         case 12:
/* 653 */           addToken(37); continue;
/*     */         case 23:
/*     */           continue;
/*     */         case 6:
/* 657 */           addToken(22); continue;
/*     */         case 24:
/*     */           continue;
/*     */       } 
/* 661 */       if (zzInput == -1 && this.zzStartRead == this.zzCurrentPos) {
/* 662 */         this.zzAtEOF = true;
/* 663 */         switch (this.zzLexicalState) {
/*     */           case 0:
/* 665 */             addNullToken(); return (Token)this.firstToken;
/*     */           case 35:
/*     */             continue;
/*     */         } 
/* 669 */         return null;
/*     */       } 
/*     */ 
/*     */       
/* 673 */       zzScanError(1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\modes\YamlTokenMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */