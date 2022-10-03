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
/*     */ public class DtdTokenMaker
/*     */   extends AbstractJFlexTokenMaker
/*     */ {
/*     */   public static final int YYEOF = -1;
/*     */   public static final int INTAG_START = 2;
/*     */   public static final int INTAG_ELEMENT = 3;
/*     */   public static final int YYINITIAL = 0;
/*     */   public static final int INTAG_ATTLIST = 4;
/*     */   public static final int COMMENT = 1;
/*     */   private static final String ZZ_CMAP_PACKED = "\t\000\001\001\001\002\001\000\001\001\023\000\001\001\001\024\001\003\001!\001\007\001\005\001\005\001\004\005\005\001\025\001\022\001\006\n\007\001\020\001\005\001\023\001\005\001\026\002\005\001\034\001\007\001\037\001 \001\027\003\007\001\035\002\007\001\030\001\031\001\032\001\007\001\"\001$\001#\001\036\001\033\001%\005\007\001\005\001\000\001\005\001\000\001\005\001\000\004\007\001\017\001\f\001\007\001\b\001\r\002\007\001\016\003\007\001\n\002\007\001\013\001\t\002\007\001\021\003\007\003\000\001\005ﾁ\000";
/*  87 */   private static final char[] ZZ_CMAP = zzUnpackCMap("\t\000\001\001\001\002\001\000\001\001\023\000\001\001\001\024\001\003\001!\001\007\001\005\001\005\001\004\005\005\001\025\001\022\001\006\n\007\001\020\001\005\001\023\001\005\001\026\002\005\001\034\001\007\001\037\001 \001\027\003\007\001\035\002\007\001\030\001\031\001\032\001\007\001\"\001$\001#\001\036\001\033\001%\005\007\001\005\001\000\001\005\001\000\001\005\001\000\004\007\001\017\001\f\001\007\001\b\001\r\002\007\001\016\003\007\001\n\002\007\001\013\001\t\002\007\001\021\003\007\003\000\001\005ﾁ\000");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   private static final int[] ZZ_ACTION = zzUnpackAction();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZZ_ACTION_PACKED_0 = "\005\000\001\001\001\002\001\003\001\004\001\005\004\004\001\003\001\006\002\003\002\007\002\b\002\007\001\t\005\000\002\003\001\b\003\007\005\000\001\n\002\003\003\007\001\013\002\000\001\f\002\003\003\007\002\000\002\003\001\r\002\007\002\003\002\007\001\016\001\017\001\007";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackAction() {
/* 102 */     int[] result = new int[70];
/* 103 */     int offset = 0;
/* 104 */     offset = zzUnpackAction("\005\000\001\001\001\002\001\003\001\004\001\005\004\004\001\003\001\006\002\003\002\007\002\b\002\007\001\t\005\000\002\003\001\b\003\007\005\000\001\n\002\003\003\007\001\013\002\000\001\f\002\003\003\007\002\000\002\003\001\r\002\007\002\003\002\007\001\016\001\017\001\007", offset, result);
/* 105 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAction(String packed, int offset, int[] result) {
/* 109 */     int i = 0;
/* 110 */     int j = offset;
/* 111 */     int l = packed.length();
/* 112 */     label10: while (i < l) {
/* 113 */       int count = packed.charAt(i++);
/* 114 */       int value = packed.charAt(i++); while (true)
/* 115 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 117 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 124 */   private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZZ_ROWMAP_PACKED_0 = "\000\000\000&\000L\000r\000\000¾\000ä\000Ċ\000İ\000Ŗ\000ż\000Ƣ\000ǈ\000Ǯ\000Ȕ\000Ŗ\000Ⱥ\000ɠ\000ʆ\000ʬ\000˒\000˸\000̞\000̈́\000ͪ\000ΐ\000ζ\000Ϝ\000Ђ\000Ш\000ю\000Ѵ\000Ŗ\000Қ\000Ӏ\000Ӧ\000Ԍ\000Բ\000՘\000վ\000֤\000Ŗ\000׊\000װ\000ؖ\000ؼ\000٢\000Ŗ\000ڈ\000ڮ\000۔\000ۺ\000ܠ\000݆\000ݬ\000ޒ\000޸\000۔\000ߞ\000ࠄ\000ʬ\000ࠪ\000ࡐ\000ࡶ\000࢜\000ࣂ\000ࣨ\000Ȕ\000Ȕ\000ऎ";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackRowMap() {
/* 138 */     int[] result = new int[70];
/* 139 */     int offset = 0;
/* 140 */     offset = zzUnpackRowMap("\000\000\000&\000L\000r\000\000¾\000ä\000Ċ\000İ\000Ŗ\000ż\000Ƣ\000ǈ\000Ǯ\000Ȕ\000Ŗ\000Ⱥ\000ɠ\000ʆ\000ʬ\000˒\000˸\000̞\000̈́\000ͪ\000ΐ\000ζ\000Ϝ\000Ђ\000Ш\000ю\000Ѵ\000Ŗ\000Қ\000Ӏ\000Ӧ\000Ԍ\000Բ\000՘\000վ\000֤\000Ŗ\000׊\000װ\000ؖ\000ؼ\000٢\000Ŗ\000ڈ\000ڮ\000۔\000ۺ\000ܠ\000݆\000ݬ\000ޒ\000޸\000۔\000ߞ\000ࠄ\000ʬ\000ࠪ\000ࡐ\000ࡶ\000࢜\000ࣂ\000ࣨ\000Ȕ\000Ȕ\000ऎ", offset, result);
/* 141 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackRowMap(String packed, int offset, int[] result) {
/* 145 */     int i = 0;
/* 146 */     int j = offset;
/* 147 */     int l = packed.length();
/* 148 */     while (i < l) {
/* 149 */       int high = packed.charAt(i++) << 16;
/* 150 */       result[j++] = high | packed.charAt(i++);
/*     */     } 
/* 152 */     return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 158 */   private static final int[] ZZ_TRANS = zzUnpackTrans();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZZ_TRANS_PACKED_0 = "\001\006\001\007\021\006\001\b\022\006\002\t\001\n\005\t\001\013\003\t\001\f\004\t\001\r\003\t\001\016\020\t\001\017\001\007\024\017\001\020\001\021\004\017\001\022\t\017\001\023\001\007\024\023\001\020\017\023\001\024\001\007\001\024\001\025\001\026\021\024\001\020\b\024\001\027\001\024\001\030\004\024\001\006\001\000\021\006\001\000\022\006\001\000\001\0078\000\001\031\021\000\002\t\001\000\005\t\001\000\003\t\001\000\004\t\001\000\003\t\001\000\020\t/\000\001\032%\000\001\033\003\000\001\034)\000\001\035)\000\001\036\020\000\001\017\001\000\024\017\001\000\020\017\001\000\024\017\001\000\001\017\001\037\016\017\001\000\024\017\001\000\004\017\001 \n\017\001\023\001\000\024\023\001\000\017\023\001\024\001\000\001\024\002\000\021\024\001\000\017\024\003\025\001!\"\025\004\026\001!!\026\001\024\001\000\001\024\002\000\021\024\001\000\t\024\001\"\006\024\001\000\001\024\002\000\021\024\001\000\006\024\001#\005\024\001$\002\024\025\000\001%\031\000\001&&\000\001')\000\001((\000\001)*\000\001*\017\000\001\017\001\000\024\017\001\000\001+\017\017\001\000\024\017\001\000\004\017\001,\n\017\001\024\001\000\001\024\002\000\021\024\001\000\005\024\001-\n\024\001\000\001\024\002\000\021\024\001\000\002\024\001.\r\024\001\000\001\024\002\000\021\024\001\000\001/\016\024\025\000\0010\032\000\0011+\000\0012$\000\001'(\000\0013\023\000\001\017\001\000\024\017\001\000\002\017\0014\r\017\001\000\024\017\001\000\001\017\0015\r\017\001\024\001\000\001\024\002\000\021\024\001\000\004\024\0016\013\024\001\000\001\024\002\000\021\024\001\000\013\024\0017\004\024\001\000\001\024\002\000\021\024\001\000\r\024\0018\001\024\013\000\001'\004\000\0012\033\000\0019#\000\002:\n3\001:\0013\001:\001\000\002:\001\000\n3\001:\0043\001\017\001\000\024\017\001\000\001;\017\017\001\000\024\017\001\000\006\017\001<\b\017\001\024\001\000\001\024\002\000\021\024\001\000\005\024\001=\n\024\001\000\001\024\002\000\021\024\001\000\001\024\001>\016\024\001\000\001\024\002\000\021\024\001\000\016\024\001?\006\000\0013\037\000\001\017\001\000\024\017\001\000\003\017\001@\f\017\001\000\024\017\001\000\007\017\001A\007\017\001\024\001\000\001\024\002\000\021\024\001\000\006\024\001B\t\024\001\000\001\024\002\000\021\024\001\000\006\024\001C\b\024\001\017\001\000\024\017\001\000\004\017\001D\013\017\001\000\024\017\001\000\004\017\001E\n\017\001\024\001\000\001\024\002\000\021\024\001\000\001F\017\024\001\000\001\024\002\000\021\024\001\000\f\024\001B\003\024\001\000\001\024\002\000\021\024\001\000\t\024\001=\005\024";
/*     */ 
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
/*     */   
/*     */   private static int[] zzUnpackTrans() {
/* 207 */     int[] result = new int[2356];
/* 208 */     int offset = 0;
/* 209 */     offset = zzUnpackTrans("\001\006\001\007\021\006\001\b\022\006\002\t\001\n\005\t\001\013\003\t\001\f\004\t\001\r\003\t\001\016\020\t\001\017\001\007\024\017\001\020\001\021\004\017\001\022\t\017\001\023\001\007\024\023\001\020\017\023\001\024\001\007\001\024\001\025\001\026\021\024\001\020\b\024\001\027\001\024\001\030\004\024\001\006\001\000\021\006\001\000\022\006\001\000\001\0078\000\001\031\021\000\002\t\001\000\005\t\001\000\003\t\001\000\004\t\001\000\003\t\001\000\020\t/\000\001\032%\000\001\033\003\000\001\034)\000\001\035)\000\001\036\020\000\001\017\001\000\024\017\001\000\020\017\001\000\024\017\001\000\001\017\001\037\016\017\001\000\024\017\001\000\004\017\001 \n\017\001\023\001\000\024\023\001\000\017\023\001\024\001\000\001\024\002\000\021\024\001\000\017\024\003\025\001!\"\025\004\026\001!!\026\001\024\001\000\001\024\002\000\021\024\001\000\t\024\001\"\006\024\001\000\001\024\002\000\021\024\001\000\006\024\001#\005\024\001$\002\024\025\000\001%\031\000\001&&\000\001')\000\001((\000\001)*\000\001*\017\000\001\017\001\000\024\017\001\000\001+\017\017\001\000\024\017\001\000\004\017\001,\n\017\001\024\001\000\001\024\002\000\021\024\001\000\005\024\001-\n\024\001\000\001\024\002\000\021\024\001\000\002\024\001.\r\024\001\000\001\024\002\000\021\024\001\000\001/\016\024\025\000\0010\032\000\0011+\000\0012$\000\001'(\000\0013\023\000\001\017\001\000\024\017\001\000\002\017\0014\r\017\001\000\024\017\001\000\001\017\0015\r\017\001\024\001\000\001\024\002\000\021\024\001\000\004\024\0016\013\024\001\000\001\024\002\000\021\024\001\000\013\024\0017\004\024\001\000\001\024\002\000\021\024\001\000\r\024\0018\001\024\013\000\001'\004\000\0012\033\000\0019#\000\002:\n3\001:\0013\001:\001\000\002:\001\000\n3\001:\0043\001\017\001\000\024\017\001\000\001;\017\017\001\000\024\017\001\000\006\017\001<\b\017\001\024\001\000\001\024\002\000\021\024\001\000\005\024\001=\n\024\001\000\001\024\002\000\021\024\001\000\001\024\001>\016\024\001\000\001\024\002\000\021\024\001\000\016\024\001?\006\000\0013\037\000\001\017\001\000\024\017\001\000\003\017\001@\f\017\001\000\024\017\001\000\007\017\001A\007\017\001\024\001\000\001\024\002\000\021\024\001\000\006\024\001B\t\024\001\000\001\024\002\000\021\024\001\000\006\024\001C\b\024\001\017\001\000\024\017\001\000\004\017\001D\013\017\001\000\024\017\001\000\004\017\001E\n\017\001\024\001\000\001\024\002\000\021\024\001\000\001F\017\024\001\000\001\024\002\000\021\024\001\000\f\024\001B\003\024\001\000\001\024\002\000\021\024\001\000\t\024\001=\005\024", offset, result);
/* 210 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackTrans(String packed, int offset, int[] result) {
/* 214 */     int i = 0;
/* 215 */     int j = offset;
/* 216 */     int l = packed.length();
/* 217 */     label10: while (i < l) {
/* 218 */       int count = packed.charAt(i++);
/* 219 */       int value = packed.charAt(i++);
/* 220 */       value--; while (true)
/* 221 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 223 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 233 */   private static final String[] ZZ_ERROR_MSG = new String[] { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 242 */   private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
/*     */   
/*     */   private static final String ZZ_ATTRIBUTE_PACKED_0 = "\005\000\004\001\001\t\005\001\001\t\t\001\005\000\002\001\001\t\003\001\005\000\001\t\005\001\001\t\002\000\006\001\002\000\f\001";
/*     */   
/*     */   private Reader zzReader;
/*     */   private int zzState;
/*     */   
/*     */   private static int[] zzUnpackAttribute() {
/* 250 */     int[] result = new int[70];
/* 251 */     int offset = 0;
/* 252 */     offset = zzUnpackAttribute("\005\000\004\001\001\t\005\001\001\t\t\001\005\000\002\001\001\t\003\001\005\000\001\t\005\001\001\t\002\000\006\001\002\000\f\001", offset, result);
/* 253 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAttribute(String packed, int offset, int[] result) {
/* 257 */     int i = 0;
/* 258 */     int j = offset;
/* 259 */     int l = packed.length();
/* 260 */     label10: while (i < l) {
/* 261 */       int count = packed.charAt(i++);
/* 262 */       int value = packed.charAt(i++); while (true)
/* 263 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 265 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 275 */   private int zzLexicalState = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private char[] zzBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int zzMarkedPos;
/*     */ 
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
/*     */   public static final int INTERNAL_INTAG_START = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int INTERNAL_INTAG_ELEMENT = -2;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int INTERNAL_INTAG_ATTLIST = -3;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int INTERNAL_IN_COMMENT = -2048;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int prevState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DtdTokenMaker() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addEndToken(int tokenType) {
/* 346 */     addToken(this.zzMarkedPos, this.zzMarkedPos, tokenType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addHyperlinkToken(int start, int end, int tokenType) {
/* 357 */     int so = start + this.offsetShift;
/* 358 */     addToken(this.zzBuffer, start, end, tokenType, so, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToken(int tokenType) {
/* 368 */     addToken(this.zzStartRead, this.zzMarkedPos - 1, tokenType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToken(int start, int end, int tokenType) {
/* 378 */     int so = start + this.offsetShift;
/* 379 */     addToken(this.zzBuffer, start, end, tokenType, so);
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
/* 395 */     super.addToken(array, start, end, tokenType, startOffset);
/* 396 */     this.zzStartRead = this.zzMarkedPos;
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
/* 410 */     return false;
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
/* 429 */     resetTokenList();
/* 430 */     this.offsetShift = -text.offset + startOffset;
/* 431 */     this.prevState = 0;
/*     */ 
/*     */     
/* 434 */     int state = 0;
/* 435 */     switch (initialTokenType) {
/*     */       case -1:
/* 437 */         state = 2;
/*     */         break;
/*     */       case -2:
/* 440 */         state = 3;
/*     */         break;
/*     */       case -3:
/* 443 */         state = 4;
/*     */         break;
/*     */       default:
/* 446 */         if (initialTokenType < -1024) {
/* 447 */           int main = -(-initialTokenType & 0xFFFFFF00);
/* 448 */           switch (main) {
/*     */           
/*     */           } 
/* 451 */           state = 1;
/*     */ 
/*     */           
/* 454 */           this.prevState = -initialTokenType & 0xFF;
/*     */           break;
/*     */         } 
/* 457 */         state = 0;
/*     */         break;
/*     */     } 
/*     */     
/* 461 */     this.start = text.offset;
/* 462 */     this.s = text;
/*     */     try {
/* 464 */       yyreset(this.zzReader);
/* 465 */       yybegin(state);
/* 466 */       return yylex();
/* 467 */     } catch (IOException ioe) {
/* 468 */       ioe.printStackTrace();
/* 469 */       return (Token)new TokenImpl();
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
/* 482 */     return (this.zzCurrentPos >= this.s.offset + this.s.count);
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
/* 498 */     this.zzBuffer = this.s.array;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 506 */     this.zzStartRead = this.s.offset;
/* 507 */     this.zzEndRead = this.zzStartRead + this.s.count - 1;
/* 508 */     this.zzCurrentPos = this.zzMarkedPos = this.s.offset;
/* 509 */     this.zzLexicalState = 0;
/* 510 */     this.zzReader = reader;
/* 511 */     this.zzAtEOF = false;
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
/*     */   public DtdTokenMaker(Reader in) {
/* 524 */     this.zzReader = in;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DtdTokenMaker(InputStream in) {
/* 534 */     this(new InputStreamReader(in));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static char[] zzUnpackCMap(String packed) {
/* 544 */     char[] map = new char[65536];
/* 545 */     int i = 0;
/* 546 */     int j = 0;
/* 547 */     label10: while (i < 138) {
/* 548 */       int count = packed.charAt(i++);
/* 549 */       char value = packed.charAt(i++); while (true)
/* 550 */       { map[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 552 */     }  return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void yyclose() throws IOException {
/* 560 */     this.zzAtEOF = true;
/* 561 */     this.zzEndRead = this.zzStartRead;
/*     */     
/* 563 */     if (this.zzReader != null) {
/* 564 */       this.zzReader.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int yystate() {
/* 572 */     return this.zzLexicalState;
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
/* 583 */     this.zzLexicalState = newState;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String yytext() {
/* 591 */     return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
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
/* 607 */     return this.zzBuffer[this.zzStartRead + pos];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int yylength() {
/* 615 */     return this.zzMarkedPos - this.zzStartRead;
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
/* 636 */       message = ZZ_ERROR_MSG[errorCode];
/*     */     }
/* 638 */     catch (ArrayIndexOutOfBoundsException e) {
/* 639 */       message = ZZ_ERROR_MSG[0];
/*     */     } 
/*     */     
/* 642 */     throw new Error(message);
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
/* 655 */     if (number > yylength()) {
/* 656 */       zzScanError(2);
/*     */     }
/* 658 */     this.zzMarkedPos -= number;
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
/* 676 */     int zzEndReadL = this.zzEndRead;
/* 677 */     char[] zzBufferL = this.zzBuffer;
/* 678 */     char[] zzCMapL = ZZ_CMAP;
/*     */     
/* 680 */     int[] zzTransL = ZZ_TRANS;
/* 681 */     int[] zzRowMapL = ZZ_ROWMAP;
/* 682 */     int[] zzAttrL = ZZ_ATTRIBUTE;
/*     */     
/*     */     while (true) {
/* 685 */       int zzInput, temp, zzMarkedPosL = this.zzMarkedPos;
/*     */       
/* 687 */       int zzAction = -1;
/*     */       
/* 689 */       int zzCurrentPosL = this.zzCurrentPos = this.zzStartRead = zzMarkedPosL;
/*     */       
/* 691 */       this.zzState = this.zzLexicalState;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 697 */         if (zzCurrentPosL < zzEndReadL)
/* 698 */         { zzInput = zzBufferL[zzCurrentPosL++]; }
/* 699 */         else { if (this.zzAtEOF) {
/* 700 */             int i = -1;
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 705 */           this.zzCurrentPos = zzCurrentPosL;
/* 706 */           this.zzMarkedPos = zzMarkedPosL;
/* 707 */           boolean eof = zzRefill();
/*     */           
/* 709 */           zzCurrentPosL = this.zzCurrentPos;
/* 710 */           zzMarkedPosL = this.zzMarkedPos;
/* 711 */           zzBufferL = this.zzBuffer;
/* 712 */           zzEndReadL = this.zzEndRead;
/* 713 */           if (eof) {
/* 714 */             int i = -1;
/*     */             
/*     */             break;
/*     */           } 
/* 718 */           zzInput = zzBufferL[zzCurrentPosL++]; }
/*     */ 
/*     */         
/* 721 */         int zzNext = zzTransL[zzRowMapL[this.zzState] + zzCMapL[zzInput]];
/* 722 */         if (zzNext == -1)
/* 723 */           break;  this.zzState = zzNext;
/*     */         
/* 725 */         int zzAttributes = zzAttrL[this.zzState];
/* 726 */         if ((zzAttributes & 0x1) == 1) {
/* 727 */           zzAction = this.zzState;
/* 728 */           zzMarkedPosL = zzCurrentPosL;
/* 729 */           if ((zzAttributes & 0x8) == 8) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 736 */       this.zzMarkedPos = zzMarkedPosL;
/*     */       
/* 738 */       switch ((zzAction < 0) ? zzAction : ZZ_ACTION[zzAction]) {
/*     */         case 3:
/* 740 */           addToken(20); continue;
/*     */         case 16:
/*     */           continue;
/*     */         case 2:
/* 744 */           addToken(21); continue;
/*     */         case 17:
/*     */           continue;
/*     */         case 1:
/* 748 */           addToken(20); continue;
/*     */         case 18:
/*     */           continue;
/*     */         case 12:
/* 752 */           temp = this.zzStartRead; addToken(this.start, this.zzStartRead - 1, 29); addHyperlinkToken(temp, this.zzMarkedPos - 1, 29); this.start = this.zzMarkedPos; continue;
/*     */         case 19:
/*     */           continue;
/*     */         case 9:
/* 756 */           addToken(25); yybegin(2); continue;
/*     */         case 20:
/*     */           continue;
/*     */         case 6:
/* 760 */           addToken(25); yybegin(0); continue;
/*     */         case 21:
/*     */           continue;
/*     */         case 10:
/* 764 */           temp = this.zzMarkedPos; addToken(this.start, this.zzStartRead + 2, 29); this.start = temp; yybegin(this.prevState); continue;
/*     */         case 22:
/*     */           continue;
/*     */         case 11:
/* 768 */           this.start = this.zzStartRead; this.prevState = this.zzLexicalState; yybegin(1); continue;
/*     */         case 23:
/*     */           continue;
/*     */         case 7:
/* 772 */           addToken(27); continue;
/*     */         case 24:
/*     */           continue;
/*     */         case 15:
/* 776 */           addToken(26); yybegin(4); continue;
/*     */         case 25:
/*     */           continue;
/*     */         case 14:
/* 780 */           addToken(26); yybegin(3); continue;
/*     */         case 26:
/*     */           continue;
/*     */         case 13:
/* 784 */           addToken(31);
/*     */           continue;
/*     */         
/*     */         case 27:
/*     */         case 4:
/*     */         case 28:
/*     */           continue;
/*     */         case 5:
/* 792 */           addToken(this.start, this.zzStartRead - 1, 29); addEndToken(-2048 - this.prevState); return (Token)this.firstToken;
/*     */         case 29:
/*     */           continue;
/*     */         case 8:
/* 796 */           addToken(28); continue;
/*     */         case 30:
/*     */           continue;
/*     */       } 
/* 800 */       if (zzInput == -1 && this.zzStartRead == this.zzCurrentPos) {
/* 801 */         this.zzAtEOF = true;
/* 802 */         switch (this.zzLexicalState) {
/*     */           case 2:
/* 804 */             addEndToken(-1); return (Token)this.firstToken;
/*     */           case 71:
/*     */             continue;
/*     */           case 3:
/* 808 */             addEndToken(-2); return (Token)this.firstToken;
/*     */           case 72:
/*     */             continue;
/*     */           case 0:
/* 812 */             addNullToken(); return (Token)this.firstToken;
/*     */           case 73:
/*     */             continue;
/*     */           case 4:
/* 816 */             addEndToken(-3); return (Token)this.firstToken;
/*     */           case 74:
/*     */             continue;
/*     */           case 1:
/* 820 */             addToken(this.start, this.zzStartRead - 1, 29); addEndToken(-2048 - this.prevState); return (Token)this.firstToken;
/*     */           case 75:
/*     */             continue;
/*     */         } 
/* 824 */         return null;
/*     */       } 
/*     */ 
/*     */       
/* 828 */       zzScanError(1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\modes\DtdTokenMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */