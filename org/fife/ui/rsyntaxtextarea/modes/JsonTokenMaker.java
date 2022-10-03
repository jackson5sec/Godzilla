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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JsonTokenMaker
/*     */   extends AbstractJFlexCTokenMaker
/*     */ {
/*     */   public static final int YYEOF = -1;
/*     */   private static final int ZZ_BUFFERSIZE = 16384;
/*     */   public static final int EOL_COMMENT = 1;
/*     */   public static final int YYINITIAL = 0;
/*     */   private static final String ZZ_CMAP_PACKED = "\t\000\001\001\001\b\001\000\001\001\023\000\001\001\001\003\001\t\001\003\001\002\001\003\005\003\001\021\001\006\001\007\001\017\001\016\n\004\001\032\001\003\001\000\001\003\001\000\002\003\004\005\001\020\001\005\024\002\001\033\001\013\001\033\001\000\001\003\001\000\001\026\001\r\002\005\001\024\001\025\001\002\001\034\001\036\002\002\001\027\001\002\001\f\001\002\001\035\001\002\001\023\001\030\001\022\001\n\001\002\001\037\003\002\001\031\001\000\001\031\001\003ﾁ\000";
/*  85 */   private static final char[] ZZ_CMAP = zzUnpackCMap("\t\000\001\001\001\b\001\000\001\001\023\000\001\001\001\003\001\t\001\003\001\002\001\003\005\003\001\021\001\006\001\007\001\017\001\016\n\004\001\032\001\003\001\000\001\003\001\000\002\003\004\005\001\020\001\005\024\002\001\033\001\013\001\033\001\000\001\003\001\000\001\026\001\r\002\005\001\024\001\025\001\002\001\034\001\036\002\002\001\027\001\002\001\f\001\002\001\035\001\002\001\023\001\030\001\022\001\n\001\002\001\037\003\002\001\031\001\000\001\031\001\003ﾁ\000");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   private static final int[] ZZ_ACTION = zzUnpackAction();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZZ_ACTION_PACKED_0 = "\002\000\001\001\001\002\001\003\002\001\001\004\004\001\001\005\001\006\001\007\003\006\002\000\001\004\001\b\001\004\001\001\001\t\002\001\004\000\002\n\001\000\001\013\001\004\001\000\001\f\001\004\003\001\004\000\001\004\001\r\001\016\002\000\001\017\001\004\002\000\001\004";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackAction() {
/* 100 */     int[] result = new int[56];
/* 101 */     int offset = 0;
/* 102 */     offset = zzUnpackAction("\002\000\001\001\001\002\001\003\002\001\001\004\004\001\001\005\001\006\001\007\003\006\002\000\001\004\001\b\001\004\001\001\001\t\002\001\004\000\002\n\001\000\001\013\001\004\001\000\001\f\001\004\003\001\004\000\001\004\001\r\001\016\002\000\001\017\001\004\002\000\001\004", offset, result);
/* 103 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAction(String packed, int offset, int[] result) {
/* 107 */     int i = 0;
/* 108 */     int j = offset;
/* 109 */     int l = packed.length();
/* 110 */     label10: while (i < l) {
/* 111 */       int count = packed.charAt(i++);
/* 112 */       int value = packed.charAt(i++); while (true)
/* 113 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 115 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 122 */   private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZZ_ROWMAP_PACKED_0 = "\000\000\000 \000@\000`\000\000 \000À\000à\000Ā\000Ġ\000ŀ\000Š\000 \000ƀ\000 \000Ơ\000ǀ\000Ǡ\000Ȁ\000Ƞ\000ɀ\000ɠ\000ʀ\000ʠ\000 \000ˀ\000ˠ\000̀\000̠\000̀\000͠\000΀\000Π\000Π\000 \000π\000ɠ\000 \000Ϡ\000Ѐ\000Р\000р\000Ѡ\000Ҁ\000Ҡ\000Ӏ\000Ӡ\000@\000@\000Ԁ\000Ԡ\000Հ\000ՠ\000ր\000Հ\000֠";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackRowMap() {
/* 134 */     int[] result = new int[56];
/* 135 */     int offset = 0;
/* 136 */     offset = zzUnpackRowMap("\000\000\000 \000@\000`\000\000 \000À\000à\000Ā\000Ġ\000ŀ\000Š\000 \000ƀ\000 \000Ơ\000ǀ\000Ǡ\000Ȁ\000Ƞ\000ɀ\000ɠ\000ʀ\000ʠ\000 \000ˀ\000ˠ\000̀\000̠\000̀\000͠\000΀\000Π\000Π\000 \000π\000ɠ\000 \000Ϡ\000Ѐ\000Р\000р\000Ѡ\000Ҁ\000Ҡ\000Ӏ\000Ӡ\000@\000@\000Ԁ\000Ԡ\000Հ\000ՠ\000ր\000Հ\000֠", offset, result);
/* 137 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackRowMap(String packed, int offset, int[] result) {
/* 141 */     int i = 0;
/* 142 */     int j = offset;
/* 143 */     int l = packed.length();
/* 144 */     while (i < l) {
/* 145 */       int high = packed.charAt(i++) << 16;
/* 146 */       result[j++] = high | packed.charAt(i++);
/*     */     } 
/* 148 */     return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 154 */   private static final int[] ZZ_TRANS = zzUnpackTrans();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZZ_TRANS_PACKED_0 = "\001\003\001\004\002\003\001\005\001\003\001\006\001\007\001\003\001\b\002\003\001\t\001\003\001\n\003\003\001\013\002\003\001\f\003\003\001\r\001\006\001\r\004\003\b\016\001\017\f\016\001\020\006\016\001\021\002\016\001\022\001\003\001\000\004\003\001\000\002\003\001\000\004\003\001\000\n\003\003\000\004\003\001\000\001\004\"\000\001\005\n\000\001\023\001\024\003\000\001\024/\000\001\005\033\000\b\b\001\025\001\026\001\b\001\027\024\b\001\003\001\000\004\003\001\000\002\003\001\000\001\030\003\003\001\000\n\003\003\000\004\003\016\000\001\031\021\000\001\003\001\000\004\003\001\000\002\003\001\000\004\003\001\000\004\003\001\032\005\003\003\000\005\003\001\000\004\003\001\000\002\003\001\000\004\003\001\000\007\003\001\033\002\003\003\000\004\003\b\016\001\000\f\016\001\000\006\016\001\000\002\016\023\000\001\034\013\000\001\035\023\000\001\036,\000\001\037\004\000\001 \037\000\001!\002\000\001\"\t\000\001\"\016\000\t\025\001#\001\025\001$\024\025\001\000\001%\030\000\001&\005\000\b\025\001\000\001\b\001'\004\b\003\025\002\b\001\025\001\b\n\025\001\003\001\000\004\003\001\000\002\003\001\000\004\003\001\000\b\003\001(\001\003\003\000\005\003\001\000\004\003\001\000\002\003\001\000\001)\003\003\001\000\n\003\003\000\005\003\001\000\004\003\001\000\002\003\001\000\004\003\001\000\b\003\001*\001\003\003\000\004\003\035\000\001+\031\000\001,\032\000\001-,\000\001.\004\000\001 \013\000\001\024\003\000\001\024\017\000\001!\033\000\b\025\001\000\033\025\002/\003\025\001#\001\025\001$\001\025\001/\002\025\001/\003\025\003/\t\025\001\003\001\000\004\003\001\000\002\003\001\000\004\003\001\000\b\003\0010\001\003\003\000\005\003\001\000\004\003\001\000\002\003\001\000\004\003\001\000\005\003\0011\004\003\003\000\005\003\001\000\004\003\001\000\002\003\001\000\004\003\001\000\t\003\001)\003\000\004\003\032\000\0012\031\000\001+(\000\0013\021\000\0014\020\000\004\025\0025\003\025\001#\001\025\001$\001\025\0015\002\025\0015\003\025\0035\t\025\016\000\0016)\000\001+\001\000\0012\007\000\0014\0017\0024\0027\002\000\0014\001\000\0034\0017\0014\0017\0074\001\000\0027\0044\004\025\0028\003\025\001#\001\025\001$\001\025\0018\002\025\0018\003\025\0038\t\025\016\000\0014\021\000\004\025\002\b\003\025\001#\001\025\001$\001\025\001\b\002\025\001\b\003\025\003\b\t\025";
/*     */ 
/*     */ 
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
/*     */ 
/*     */   
/*     */   private static final int ZZ_NO_MATCH = 1;
/*     */ 
/*     */ 
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
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackTrans() {
/* 199 */     int[] result = new int[1472];
/* 200 */     int offset = 0;
/* 201 */     offset = zzUnpackTrans("\001\003\001\004\002\003\001\005\001\003\001\006\001\007\001\003\001\b\002\003\001\t\001\003\001\n\003\003\001\013\002\003\001\f\003\003\001\r\001\006\001\r\004\003\b\016\001\017\f\016\001\020\006\016\001\021\002\016\001\022\001\003\001\000\004\003\001\000\002\003\001\000\004\003\001\000\n\003\003\000\004\003\001\000\001\004\"\000\001\005\n\000\001\023\001\024\003\000\001\024/\000\001\005\033\000\b\b\001\025\001\026\001\b\001\027\024\b\001\003\001\000\004\003\001\000\002\003\001\000\001\030\003\003\001\000\n\003\003\000\004\003\016\000\001\031\021\000\001\003\001\000\004\003\001\000\002\003\001\000\004\003\001\000\004\003\001\032\005\003\003\000\005\003\001\000\004\003\001\000\002\003\001\000\004\003\001\000\007\003\001\033\002\003\003\000\004\003\b\016\001\000\f\016\001\000\006\016\001\000\002\016\023\000\001\034\013\000\001\035\023\000\001\036,\000\001\037\004\000\001 \037\000\001!\002\000\001\"\t\000\001\"\016\000\t\025\001#\001\025\001$\024\025\001\000\001%\030\000\001&\005\000\b\025\001\000\001\b\001'\004\b\003\025\002\b\001\025\001\b\n\025\001\003\001\000\004\003\001\000\002\003\001\000\004\003\001\000\b\003\001(\001\003\003\000\005\003\001\000\004\003\001\000\002\003\001\000\001)\003\003\001\000\n\003\003\000\005\003\001\000\004\003\001\000\002\003\001\000\004\003\001\000\b\003\001*\001\003\003\000\004\003\035\000\001+\031\000\001,\032\000\001-,\000\001.\004\000\001 \013\000\001\024\003\000\001\024\017\000\001!\033\000\b\025\001\000\033\025\002/\003\025\001#\001\025\001$\001\025\001/\002\025\001/\003\025\003/\t\025\001\003\001\000\004\003\001\000\002\003\001\000\004\003\001\000\b\003\0010\001\003\003\000\005\003\001\000\004\003\001\000\002\003\001\000\004\003\001\000\005\003\0011\004\003\003\000\005\003\001\000\004\003\001\000\002\003\001\000\004\003\001\000\t\003\001)\003\000\004\003\032\000\0012\031\000\001+(\000\0013\021\000\0014\020\000\004\025\0025\003\025\001#\001\025\001$\001\025\0015\002\025\0015\003\025\0035\t\025\016\000\0016)\000\001+\001\000\0012\007\000\0014\0017\0024\0027\002\000\0014\001\000\0034\0017\0014\0017\0074\001\000\0027\0044\004\025\0028\003\025\001#\001\025\001$\001\025\0018\002\025\0018\003\025\0038\t\025\016\000\0014\021\000\004\025\002\b\003\025\001#\001\025\001$\001\025\001\b\002\025\001\b\003\025\003\b\t\025", offset, result);
/* 202 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackTrans(String packed, int offset, int[] result) {
/* 206 */     int i = 0;
/* 207 */     int j = offset;
/* 208 */     int l = packed.length();
/* 209 */     label10: while (i < l) {
/* 210 */       int count = packed.charAt(i++);
/* 211 */       int value = packed.charAt(i++);
/* 212 */       value--; while (true)
/* 213 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 215 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 225 */   private static final String[] ZZ_ERROR_MSG = new String[] { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 234 */   private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
/*     */   
/*     */   private static final String ZZ_ATTRIBUTE_PACKED_0 = "\002\000\003\001\001\t\006\001\001\t\001\001\001\t\003\001\002\000\001\001\001\003\002\001\001\t\002\001\004\000\002\001\001\000\001\t\001\001\001\000\001\r\004\001\004\000\003\001\002\000\002\001\002\000\001\001";
/*     */   
/*     */   private Reader zzReader;
/*     */   
/*     */   private int zzState;
/*     */   
/*     */   private static int[] zzUnpackAttribute() {
/* 243 */     int[] result = new int[56];
/* 244 */     int offset = 0;
/* 245 */     offset = zzUnpackAttribute("\002\000\003\001\001\t\006\001\001\t\001\001\001\t\003\001\002\000\001\001\001\003\002\001\001\t\002\001\004\000\002\001\001\000\001\t\001\001\001\000\001\r\004\001\004\000\003\001\002\000\002\001\002\000\001\001", offset, result);
/* 246 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAttribute(String packed, int offset, int[] result) {
/* 250 */     int i = 0;
/* 251 */     int j = offset;
/* 252 */     int l = packed.length();
/* 253 */     label10: while (i < l) {
/* 254 */       int count = packed.charAt(i++);
/* 255 */       int value = packed.charAt(i++); while (true)
/* 256 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 258 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 268 */   private int zzLexicalState = 0;
/*     */ 
/*     */ 
/*     */   
/* 272 */   private char[] zzBuffer = new char[16384];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int zzMarkedPos;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int zzPushbackPos;
/*     */ 
/*     */ 
/*     */   
/*     */   private int zzCurrentPos;
/*     */ 
/*     */ 
/*     */   
/*     */   private int zzStartRead;
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
/*     */   private boolean highlightEolComments;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonTokenMaker() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addHyperlinkToken(int start, int end, int tokenType) {
/* 313 */     int so = start + this.offsetShift;
/* 314 */     addToken(this.zzBuffer, start, end, tokenType, so, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToken(int tokenType) {
/* 324 */     addToken(this.zzStartRead, this.zzMarkedPos - 1, tokenType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToken(int start, int end, int tokenType) {
/* 334 */     int so = start + this.offsetShift;
/* 335 */     addToken(this.zzBuffer, start, end, tokenType, so, false);
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
/* 353 */     super.addToken(array, start, end, tokenType, startOffset, hyperlink);
/* 354 */     this.zzStartRead = this.zzMarkedPos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getCurlyBracesDenoteCodeBlocks() {
/* 365 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getMarkOccurrencesOfTokenType(int type) {
/* 371 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getShouldIndentNextLineAfter(Token t) {
/* 377 */     if (t != null && t.length() == 1) {
/* 378 */       char ch = t.charAt(0);
/* 379 */       return (ch == '{' || ch == '[');
/*     */     } 
/* 381 */     return false;
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
/* 399 */     resetTokenList();
/* 400 */     this.offsetShift = -text.offset + startOffset;
/*     */ 
/*     */     
/* 403 */     int state = 0;
/* 404 */     this.start = text.offset;
/*     */     
/* 406 */     this.s = text;
/*     */     try {
/* 408 */       yyreset(this.zzReader);
/* 409 */       yybegin(state);
/* 410 */       return yylex();
/* 411 */     } catch (IOException ioe) {
/* 412 */       ioe.printStackTrace();
/* 413 */       return (Token)new TokenImpl();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setHighlightEolComments(boolean highlightEolComments) {
/* 420 */     this.highlightEolComments = highlightEolComments;
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
/* 431 */     return (this.zzCurrentPos >= this.s.offset + this.s.count);
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
/* 447 */     this.zzBuffer = this.s.array;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 455 */     this.zzStartRead = this.s.offset;
/* 456 */     this.zzEndRead = this.zzStartRead + this.s.count - 1;
/* 457 */     this.zzCurrentPos = this.zzMarkedPos = this.s.offset;
/* 458 */     this.zzLexicalState = 0;
/* 459 */     this.zzReader = reader;
/* 460 */     this.zzAtEOF = false;
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
/*     */   public JsonTokenMaker(Reader in) {
/* 473 */     this.zzReader = in;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonTokenMaker(InputStream in) {
/* 483 */     this(new InputStreamReader(in));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static char[] zzUnpackCMap(String packed) {
/* 493 */     char[] map = new char[65536];
/* 494 */     int i = 0;
/* 495 */     int j = 0;
/* 496 */     label10: while (i < 124) {
/* 497 */       int count = packed.charAt(i++);
/* 498 */       char value = packed.charAt(i++); while (true)
/* 499 */       { map[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 501 */     }  return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void yyclose() throws IOException {
/* 509 */     this.zzAtEOF = true;
/* 510 */     this.zzEndRead = this.zzStartRead;
/*     */     
/* 512 */     if (this.zzReader != null) {
/* 513 */       this.zzReader.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int yystate() {
/* 521 */     return this.zzLexicalState;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void yybegin(int newState) {
/* 531 */     this.zzLexicalState = newState;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String yytext() {
/* 539 */     return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
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
/* 555 */     return this.zzBuffer[this.zzStartRead + pos];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int yylength() {
/* 563 */     return this.zzMarkedPos - this.zzStartRead;
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
/* 584 */       message = ZZ_ERROR_MSG[errorCode];
/*     */     }
/* 586 */     catch (ArrayIndexOutOfBoundsException e) {
/* 587 */       message = ZZ_ERROR_MSG[0];
/*     */     } 
/*     */     
/* 590 */     throw new Error(message);
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
/* 603 */     if (number > yylength()) {
/* 604 */       zzScanError(2);
/*     */     }
/* 606 */     this.zzMarkedPos -= number;
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
/* 624 */     int zzEndReadL = this.zzEndRead;
/* 625 */     char[] zzBufferL = this.zzBuffer;
/* 626 */     char[] zzCMapL = ZZ_CMAP;
/*     */     
/* 628 */     int[] zzTransL = ZZ_TRANS;
/* 629 */     int[] zzRowMapL = ZZ_ROWMAP;
/* 630 */     int[] zzAttrL = ZZ_ATTRIBUTE;
/* 631 */     int zzPushbackPosL = this.zzPushbackPos = -1;
/*     */ 
/*     */     
/*     */     while (true) {
/* 635 */       int zzInput, temp, zzMarkedPosL = this.zzMarkedPos;
/*     */       
/* 637 */       int zzAction = -1;
/*     */       
/* 639 */       int zzCurrentPosL = this.zzCurrentPos = this.zzStartRead = zzMarkedPosL;
/*     */       
/* 641 */       this.zzState = this.zzLexicalState;
/*     */       
/* 643 */       boolean zzWasPushback = false;
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 648 */         if (zzCurrentPosL < zzEndReadL)
/* 649 */         { zzInput = zzBufferL[zzCurrentPosL++]; }
/* 650 */         else { if (this.zzAtEOF) {
/* 651 */             int i = -1;
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 656 */           this.zzCurrentPos = zzCurrentPosL;
/* 657 */           this.zzMarkedPos = zzMarkedPosL;
/* 658 */           this.zzPushbackPos = zzPushbackPosL;
/* 659 */           boolean eof = zzRefill();
/*     */           
/* 661 */           zzCurrentPosL = this.zzCurrentPos;
/* 662 */           zzMarkedPosL = this.zzMarkedPos;
/* 663 */           zzBufferL = this.zzBuffer;
/* 664 */           zzEndReadL = this.zzEndRead;
/* 665 */           zzPushbackPosL = this.zzPushbackPos;
/* 666 */           if (eof) {
/* 667 */             int i = -1;
/*     */             
/*     */             break;
/*     */           } 
/* 671 */           zzInput = zzBufferL[zzCurrentPosL++]; }
/*     */ 
/*     */         
/* 674 */         int zzNext = zzTransL[zzRowMapL[this.zzState] + zzCMapL[zzInput]];
/* 675 */         if (zzNext == -1)
/* 676 */           break;  this.zzState = zzNext;
/*     */         
/* 678 */         int zzAttributes = zzAttrL[this.zzState];
/* 679 */         if ((zzAttributes & 0x2) == 2) {
/* 680 */           zzPushbackPosL = zzCurrentPosL;
/*     */         }
/* 682 */         if ((zzAttributes & 0x1) == 1) {
/* 683 */           zzWasPushback = ((zzAttributes & 0x4) == 4);
/* 684 */           zzAction = this.zzState;
/* 685 */           zzMarkedPosL = zzCurrentPosL;
/* 686 */           if ((zzAttributes & 0x8) == 8) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 693 */       this.zzMarkedPos = zzMarkedPosL;
/* 694 */       if (zzWasPushback) {
/* 695 */         this.zzMarkedPos = zzPushbackPosL;
/*     */       }
/* 697 */       switch ((zzAction < 0) ? zzAction : ZZ_ACTION[zzAction]) {
/*     */         case 13:
/* 699 */           addToken(6); continue;
/*     */         case 16:
/*     */           continue;
/*     */         case 1:
/* 703 */           addToken(20); continue;
/*     */         case 17:
/*     */           continue;
/*     */         case 10:
/* 707 */           addToken(11); continue;
/*     */         case 18:
/*     */           continue;
/*     */         case 8:
/* 711 */           addToken(13); continue;
/*     */         case 19:
/*     */           continue;
/*     */         case 12:
/* 715 */           addToken(17); continue;
/*     */         case 20:
/*     */           continue;
/*     */         case 2:
/* 719 */           addToken(21); continue;
/*     */         case 21:
/*     */           continue;
/*     */         case 15:
/* 723 */           temp = this.zzStartRead; addToken(this.start, this.zzStartRead - 1, 1); addHyperlinkToken(temp, this.zzMarkedPos - 1, 1); this.start = this.zzMarkedPos; continue;
/*     */         case 22:
/*     */           continue;
/*     */         case 3:
/* 727 */           addToken(10); continue;
/*     */         case 23:
/*     */           continue;
/*     */         case 14:
/* 731 */           addToken(9); continue;
/*     */         case 24:
/*     */           continue;
/*     */         case 9:
/* 735 */           if (this.highlightEolComments) {
/* 736 */             this.start = this.zzMarkedPos - 2; yybegin(1);
/*     */             continue;
/*     */           } 
/* 739 */           addToken(20);
/*     */           continue;
/*     */         case 25:
/*     */           continue;
/*     */         case 4:
/* 744 */           addToken(37); addNullToken(); return (Token)this.firstToken;
/*     */         case 26:
/*     */           continue;
/*     */         case 7:
/* 748 */           addToken(this.start, this.zzStartRead - 1, 1); addNullToken(); return (Token)this.firstToken;
/*     */         case 27:
/*     */           continue;
/*     */         case 11:
/* 752 */           addToken(37);
/*     */           continue;
/*     */         
/*     */         case 28:
/*     */         case 6:
/*     */         case 29:
/*     */           continue;
/*     */         case 5:
/* 760 */           addToken(22); continue;
/*     */         case 30:
/*     */           continue;
/*     */       } 
/* 764 */       if (zzInput == -1 && this.zzStartRead == this.zzCurrentPos) {
/* 765 */         this.zzAtEOF = true;
/* 766 */         switch (this.zzLexicalState) {
/*     */           case 1:
/* 768 */             addToken(this.start, this.zzStartRead - 1, 1); addNullToken(); return (Token)this.firstToken;
/*     */           case 57:
/*     */             continue;
/*     */           case 0:
/* 772 */             addNullToken(); return (Token)this.firstToken;
/*     */           case 58:
/*     */             continue;
/*     */         } 
/* 776 */         return null;
/*     */       } 
/*     */ 
/*     */       
/* 780 */       zzScanError(1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\modes\JsonTokenMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */