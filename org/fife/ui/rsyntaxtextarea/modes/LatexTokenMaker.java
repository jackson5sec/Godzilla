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
/*     */ 
/*     */ 
/*     */ public class LatexTokenMaker
/*     */   extends AbstractJFlexTokenMaker
/*     */ {
/*     */   public static final int YYEOF = -1;
/*     */   public static final int EOL_COMMENT = 1;
/*     */   public static final int YYINITIAL = 0;
/*     */   private static final String ZZ_CMAP_PACKED = "\t\000\001\003\001\032\001\000\001\003\023\000\001\003\001\005\001\000\001\005\001\007\001\004\007\005\001\002\001\022\001\006\n\001\001\020\001\005\001\000\001\005\001\000\002\005\032\001\001\005\001\023\001\005\001\000\001\002\001\000\001\001\001\025\001\001\001\031\001\017\001\f\001\026\001\b\001\r\002\001\001\016\001\001\001\027\001\001\001\n\002\001\001\013\001\t\002\001\001\021\003\001\001\030\001\000\001\024\001\005ﾁ\000";
/*  84 */   private static final char[] ZZ_CMAP = zzUnpackCMap("\t\000\001\003\001\032\001\000\001\003\023\000\001\003\001\005\001\000\001\005\001\007\001\004\007\005\001\002\001\022\001\006\n\001\001\020\001\005\001\000\001\005\001\000\002\005\032\001\001\005\001\023\001\005\001\000\001\002\001\000\001\001\001\025\001\001\001\031\001\017\001\f\001\026\001\b\001\r\002\001\001\016\001\001\001\027\001\001\001\n\002\001\001\013\001\t\002\001\001\021\003\001\001\030\001\000\001\024\001\005ﾁ\000");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   private static final int[] ZZ_ACTION = zzUnpackAction();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZZ_ACTION_PACKED_0 = "\002\000\002\001\001\002\001\003\001\001\001\004\001\005\004\006\001\007\001\b\001\t\002\b\004\000\002\b\004\000\002\b\002\000\001\n\001\000\001\b\003\000\001\b\001\013\002\000\001\f";
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackAction() {
/*  98 */     int[] result = new int[43];
/*  99 */     int offset = 0;
/* 100 */     offset = zzUnpackAction("\002\000\002\001\001\002\001\003\001\001\001\004\001\005\004\006\001\007\001\b\001\t\002\b\004\000\002\b\004\000\002\b\002\000\001\n\001\000\001\b\003\000\001\b\001\013\002\000\001\f", offset, result);
/* 101 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAction(String packed, int offset, int[] result) {
/* 105 */     int i = 0;
/* 106 */     int j = offset;
/* 107 */     int l = packed.length();
/* 108 */     label10: while (i < l) {
/* 109 */       int count = packed.charAt(i++);
/* 110 */       int value = packed.charAt(i++); while (true)
/* 111 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 113 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZZ_ROWMAP_PACKED_0 = "\000\000\000\033\0006\000Q\0006\0006\000l\0006\0006\000\000¢\000½\000Ø\0006\000ó\0006\000Ď\000ĩ\000ń\000ş\000ź\000ƕ\000ư\000ǋ\000Ǧ\000ȁ\000Ȝ\000ȷ\000ɒ\000ɭ\000ʈ\000ʣ\000ʾ\000˙\000˴\000̏\000ʾ\000̪\000ͅ\0006\000͠\000ͻ\0006";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackRowMap() {
/* 131 */     int[] result = new int[43];
/* 132 */     int offset = 0;
/* 133 */     offset = zzUnpackRowMap("\000\000\000\033\0006\000Q\0006\0006\000l\0006\0006\000\000¢\000½\000Ø\0006\000ó\0006\000Ď\000ĩ\000ń\000ş\000ź\000ƕ\000ư\000ǋ\000Ǧ\000ȁ\000Ȝ\000ȷ\000ɒ\000ɭ\000ʈ\000ʣ\000ʾ\000˙\000˴\000̏\000ʾ\000̪\000ͅ\0006\000͠\000ͻ\0006", offset, result);
/* 134 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackRowMap(String packed, int offset, int[] result) {
/* 138 */     int i = 0;
/* 139 */     int j = offset;
/* 140 */     int l = packed.length();
/* 141 */     while (i < l) {
/* 142 */       int high = packed.charAt(i++) << 16;
/* 143 */       result[j++] = high | packed.charAt(i++);
/*     */     } 
/* 145 */     return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 151 */   private static final int[] ZZ_TRANS = zzUnpackTrans();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZZ_TRANS_PACKED_0 = "\001\003\002\004\001\005\001\006\003\003\b\004\001\003\001\004\001\003\001\007\001\b\003\004\001\b\001\004\001\t\b\n\001\013\003\n\001\f\004\n\001\r\b\n\001\016\034\000\002\004\005\000\b\004\001\000\001\004\003\000\003\004\001\000\001\004\002\000\002\017\001\000\001\020\003\000\007\017\001\021\001\000\001\017\003\000\001\022\002\017\001\000\001\017\001\000\b\n\001\000\003\n\001\000\004\n\001\000\b\n\n\000\001\023\032\000\001\024\003\000\001\025\036\000\001\026\n\000\002\017\005\000\b\017\001\000\001\017\003\000\003\017\001\000\001\017\002\000\002\017\005\000\b\017\001\000\001\017\003\000\002\017\001\027\001\000\001\017\002\000\002\017\005\000\007\017\001\030\001\000\001\017\003\000\003\017\001\000\001\017\n\000\001\031\033\000\001\032\036\000\001\033\035\000\001\034\n\000\002\017\005\000\b\017\001\000\001\017\003\000\003\017\001\000\001\035\002\000\002\017\005\000\b\017\001\000\001\017\003\000\001\017\001\036\001\017\001\000\001\017\013\000\001\037 \000\001 \031\000\001\032\035\000\001!\t\000\002\017\005\000\b\017\001\000\001\017\003\000\003\017\001\"\001\017\002\000\002\017\005\000\005\017\001#\002\017\001\000\001\017\003\000\003\017\001\000\001\017\f\000\001\032\004\000\001 \020\000\001$\025\000\001!\001%\001\000\002%\n!\001%\001!\001%\002\000\003!\001\000\001!\002\000\002&\005\000\b&\001\000\001&\003\000\003&\001\000\001&\002\000\002\017\005\000\b\017\001\000\001\017\003\000\002\017\001'\001\000\001\017\007\000\001!\025\000\002&\005\000\b&\001\000\001&\002\000\001(\003&\001\000\001&\002\000\002\017\005\000\b\017\001\000\001\017\003\000\003\017\001)\001\017\002\000\002*\005\000\b*\001\000\001*\003\000\003*\001\000\001*\002\000\002*\005\000\b*\001\000\001*\002\000\001+\003*\001\000\001*\001\000";
/*     */ 
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
/* 186 */     int[] result = new int[918];
/* 187 */     int offset = 0;
/* 188 */     offset = zzUnpackTrans("\001\003\002\004\001\005\001\006\003\003\b\004\001\003\001\004\001\003\001\007\001\b\003\004\001\b\001\004\001\t\b\n\001\013\003\n\001\f\004\n\001\r\b\n\001\016\034\000\002\004\005\000\b\004\001\000\001\004\003\000\003\004\001\000\001\004\002\000\002\017\001\000\001\020\003\000\007\017\001\021\001\000\001\017\003\000\001\022\002\017\001\000\001\017\001\000\b\n\001\000\003\n\001\000\004\n\001\000\b\n\n\000\001\023\032\000\001\024\003\000\001\025\036\000\001\026\n\000\002\017\005\000\b\017\001\000\001\017\003\000\003\017\001\000\001\017\002\000\002\017\005\000\b\017\001\000\001\017\003\000\002\017\001\027\001\000\001\017\002\000\002\017\005\000\007\017\001\030\001\000\001\017\003\000\003\017\001\000\001\017\n\000\001\031\033\000\001\032\036\000\001\033\035\000\001\034\n\000\002\017\005\000\b\017\001\000\001\017\003\000\003\017\001\000\001\035\002\000\002\017\005\000\b\017\001\000\001\017\003\000\001\017\001\036\001\017\001\000\001\017\013\000\001\037 \000\001 \031\000\001\032\035\000\001!\t\000\002\017\005\000\b\017\001\000\001\017\003\000\003\017\001\"\001\017\002\000\002\017\005\000\005\017\001#\002\017\001\000\001\017\003\000\003\017\001\000\001\017\f\000\001\032\004\000\001 \020\000\001$\025\000\001!\001%\001\000\002%\n!\001%\001!\001%\002\000\003!\001\000\001!\002\000\002&\005\000\b&\001\000\001&\003\000\003&\001\000\001&\002\000\002\017\005\000\b\017\001\000\001\017\003\000\002\017\001'\001\000\001\017\007\000\001!\025\000\002&\005\000\b&\001\000\001&\002\000\001(\003&\001\000\001&\002\000\002\017\005\000\b\017\001\000\001\017\003\000\003\017\001)\001\017\002\000\002*\005\000\b*\001\000\001*\003\000\003*\001\000\001*\002\000\002*\005\000\b*\001\000\001*\002\000\001+\003*\001\000\001*\001\000", offset, result);
/* 189 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackTrans(String packed, int offset, int[] result) {
/* 193 */     int i = 0;
/* 194 */     int j = offset;
/* 195 */     int l = packed.length();
/* 196 */     label10: while (i < l) {
/* 197 */       int count = packed.charAt(i++);
/* 198 */       int value = packed.charAt(i++);
/* 199 */       value--; while (true)
/* 200 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 202 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 212 */   private static final String[] ZZ_ERROR_MSG = new String[] { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 221 */   private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
/*     */   
/*     */   private static final String ZZ_ATTRIBUTE_PACKED_0 = "\002\000\001\t\001\001\002\t\001\001\002\t\004\001\001\t\001\001\001\t\002\001\004\000\002\001\004\000\002\001\002\000\001\001\001\000\001\001\003\000\001\001\001\t\002\000\001\t";
/*     */   
/*     */   private Reader zzReader;
/*     */   private int zzState;
/*     */   
/*     */   private static int[] zzUnpackAttribute() {
/* 229 */     int[] result = new int[43];
/* 230 */     int offset = 0;
/* 231 */     offset = zzUnpackAttribute("\002\000\001\t\001\001\002\t\001\001\002\t\004\001\001\t\001\001\001\t\002\001\004\000\002\001\004\000\002\001\002\000\001\001\001\000\001\001\003\000\001\001\001\t\002\000\001\t", offset, result);
/* 232 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAttribute(String packed, int offset, int[] result) {
/* 236 */     int i = 0;
/* 237 */     int j = offset;
/* 238 */     int l = packed.length();
/* 239 */     label10: while (i < l) {
/* 240 */       int count = packed.charAt(i++);
/* 241 */       int value = packed.charAt(i++); while (true)
/* 242 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 244 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 254 */   private int zzLexicalState = 0;
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
/*     */   public LatexTokenMaker() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addHyperlinkToken(int start, int end, int tokenType) {
/* 294 */     int so = start + this.offsetShift;
/* 295 */     addToken(this.zzBuffer, start, end, tokenType, so, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToken(int tokenType) {
/* 305 */     addToken(this.zzStartRead, this.zzMarkedPos - 1, tokenType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToken(int start, int end, int tokenType) {
/* 316 */     int so = start + this.offsetShift;
/* 317 */     addToken(this.zzBuffer, start, end, tokenType, so, false);
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
/*     */   public void addToken(char[] array, int start, int end, int tokenType, int startOffset, boolean hyperlink) {
/* 335 */     super.addToken(array, start, end, tokenType, startOffset, hyperlink);
/* 336 */     this.zzStartRead = this.zzMarkedPos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getLineCommentStartAndEnd(int languageIndex) {
/* 345 */     return new String[] { "%", null };
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
/*     */     
/* 370 */     this.s = text;
/*     */     try {
/* 372 */       yyreset(this.zzReader);
/* 373 */       yybegin(state);
/* 374 */       return yylex();
/* 375 */     } catch (IOException ioe) {
/* 376 */       ioe.printStackTrace();
/* 377 */       return (Token)new TokenImpl();
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
/*     */   public LatexTokenMaker(Reader in) {
/* 433 */     this.zzReader = in;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LatexTokenMaker(InputStream in) {
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
/* 456 */     label10: while (i < 112) {
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
/*     */   
/*     */   public final void yybegin(int newState) {
/* 492 */     this.zzLexicalState = newState;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String yytext() {
/* 500 */     return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
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
/* 516 */     return this.zzBuffer[this.zzStartRead + pos];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int yylength() {
/* 524 */     return this.zzMarkedPos - this.zzStartRead;
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
/* 545 */       message = ZZ_ERROR_MSG[errorCode];
/*     */     }
/* 547 */     catch (ArrayIndexOutOfBoundsException e) {
/* 548 */       message = ZZ_ERROR_MSG[0];
/*     */     } 
/*     */     
/* 551 */     throw new Error(message);
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
/* 564 */     if (number > yylength()) {
/* 565 */       zzScanError(2);
/*     */     }
/* 567 */     this.zzMarkedPos -= number;
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
/* 585 */     int zzEndReadL = this.zzEndRead;
/* 586 */     char[] zzBufferL = this.zzBuffer;
/* 587 */     char[] zzCMapL = ZZ_CMAP;
/*     */     
/* 589 */     int[] zzTransL = ZZ_TRANS;
/* 590 */     int[] zzRowMapL = ZZ_ROWMAP;
/* 591 */     int[] zzAttrL = ZZ_ATTRIBUTE;
/*     */     
/*     */     while (true) {
/* 594 */       int zzInput, temp, zzMarkedPosL = this.zzMarkedPos;
/*     */       
/* 596 */       int zzAction = -1;
/*     */       
/* 598 */       int zzCurrentPosL = this.zzCurrentPos = this.zzStartRead = zzMarkedPosL;
/*     */       
/* 600 */       this.zzState = this.zzLexicalState;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 606 */         if (zzCurrentPosL < zzEndReadL)
/* 607 */         { zzInput = zzBufferL[zzCurrentPosL++]; }
/* 608 */         else { if (this.zzAtEOF) {
/* 609 */             int i = -1;
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 614 */           this.zzCurrentPos = zzCurrentPosL;
/* 615 */           this.zzMarkedPos = zzMarkedPosL;
/* 616 */           boolean eof = zzRefill();
/*     */           
/* 618 */           zzCurrentPosL = this.zzCurrentPos;
/* 619 */           zzMarkedPosL = this.zzMarkedPos;
/* 620 */           zzBufferL = this.zzBuffer;
/* 621 */           zzEndReadL = this.zzEndRead;
/* 622 */           if (eof) {
/* 623 */             int i = -1;
/*     */             
/*     */             break;
/*     */           } 
/* 627 */           zzInput = zzBufferL[zzCurrentPosL++]; }
/*     */ 
/*     */         
/* 630 */         int zzNext = zzTransL[zzRowMapL[this.zzState] + zzCMapL[zzInput]];
/* 631 */         if (zzNext == -1)
/* 632 */           break;  this.zzState = zzNext;
/*     */         
/* 634 */         int zzAttributes = zzAttrL[this.zzState];
/* 635 */         if ((zzAttributes & 0x1) == 1) {
/* 636 */           zzAction = this.zzState;
/* 637 */           zzMarkedPosL = zzCurrentPosL;
/* 638 */           if ((zzAttributes & 0x8) == 8) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 645 */       this.zzMarkedPos = zzMarkedPosL;
/*     */       
/* 647 */       switch ((zzAction < 0) ? zzAction : ZZ_ACTION[zzAction]) {
/*     */         case 1:
/* 649 */           addToken(20); continue;
/*     */         case 13:
/*     */           continue;
/*     */         case 8:
/* 653 */           addToken(8); continue;
/*     */         case 14:
/*     */           continue;
/*     */         case 2:
/* 657 */           addToken(21); continue;
/*     */         case 15:
/*     */           continue;
/*     */         case 12:
/* 661 */           temp = this.zzStartRead;
/* 662 */           addToken(temp, temp + 5, 6);
/* 663 */           addToken(temp + 6, temp + 6, 22);
/* 664 */           addToken(temp + 7, this.zzMarkedPos - 2, 6);
/* 665 */           addToken(this.zzMarkedPos - 1, this.zzMarkedPos - 1, 22); continue;
/*     */         case 16:
/*     */           continue;
/*     */         case 10:
/* 669 */           temp = this.zzStartRead; addToken(this.start, this.zzStartRead - 1, 1); addHyperlinkToken(temp, this.zzMarkedPos - 1, 1); this.start = this.zzMarkedPos; continue;
/*     */         case 17:
/*     */           continue;
/*     */         case 3:
/* 673 */           this.start = this.zzMarkedPos - 1; yybegin(1); continue;
/*     */         case 18:
/*     */           continue;
/*     */         case 11:
/* 677 */           temp = this.zzStartRead;
/* 678 */           addToken(temp, temp + 3, 6);
/* 679 */           addToken(temp + 4, temp + 4, 22);
/* 680 */           addToken(temp + 5, this.zzMarkedPos - 2, 6);
/* 681 */           addToken(this.zzMarkedPos - 1, this.zzMarkedPos - 1, 22); continue;
/*     */         case 19:
/*     */           continue;
/*     */         case 5:
/* 685 */           addNullToken(); return (Token)this.firstToken;
/*     */         case 20:
/*     */           continue;
/*     */         case 7:
/* 689 */           addToken(this.start, this.zzStartRead - 1, 1); addNullToken(); return (Token)this.firstToken;
/*     */         case 21:
/*     */           continue;
/*     */         case 9:
/* 693 */           temp = this.zzStartRead;
/* 694 */           addToken(temp, temp, 22);
/* 695 */           addToken(temp + 1, temp + 1, 20);
/*     */           continue;
/*     */         
/*     */         case 22:
/*     */         case 6:
/*     */         case 23:
/*     */           continue;
/*     */         case 4:
/* 703 */           addToken(22); continue;
/*     */         case 24:
/*     */           continue;
/*     */       } 
/* 707 */       if (zzInput == -1 && this.zzStartRead == this.zzCurrentPos) {
/* 708 */         this.zzAtEOF = true;
/* 709 */         switch (this.zzLexicalState) {
/*     */           case 1:
/* 711 */             addToken(this.start, this.zzStartRead - 1, 1); addNullToken(); return (Token)this.firstToken;
/*     */           case 44:
/*     */             continue;
/*     */           case 0:
/* 715 */             addNullToken(); return (Token)this.firstToken;
/*     */           case 45:
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


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\modes\LatexTokenMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */