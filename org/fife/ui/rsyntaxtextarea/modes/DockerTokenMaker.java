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
/*     */ public class DockerTokenMaker
/*     */   extends AbstractJFlexTokenMaker
/*     */ {
/*     */   public static final int YYEOF = -1;
/*     */   public static final int STRING = 1;
/*     */   public static final int CHAR_LITERAL = 2;
/*     */   public static final int YYINITIAL = 0;
/*     */   private static final String ZZ_CMAP_PACKED = "\t\000\001\002\001\037\001\000\001\002\023\000\001\002\001\000\001\034\001\036\003\000\001\035\005\000\002\001\001\000\n\001\004\000\001\033\002\000\001\004\001\f\001\024\001\017\001\b\001\n\001\030\001\001\001\005\001\001\001\027\001\016\001\003\001\006\001\013\001\021\001\001\001\t\001\022\001\007\001\r\001\023\001\026\001\020\001\025\001\001\001\031\001 \001\031\001\000\001\001\001\000\001\004\001\f\001\024\001\017\001\b\001\n\001\030\001\001\001\005\001\001\001\027\001\016\001\003\001\006\001\013\001\021\001\001\001\t\001\022\001\007\001\r\001\023\001\026\001\020\001\025\001\001\001\000\001\032ﾃ\000";
/*  82 */   private static final char[] ZZ_CMAP = zzUnpackCMap("\t\000\001\002\001\037\001\000\001\002\023\000\001\002\001\000\001\034\001\036\003\000\001\035\005\000\002\001\001\000\n\001\004\000\001\033\002\000\001\004\001\f\001\024\001\017\001\b\001\n\001\030\001\001\001\005\001\001\001\027\001\016\001\003\001\006\001\013\001\021\001\001\001\t\001\022\001\007\001\r\001\023\001\026\001\020\001\025\001\001\001\031\001 \001\031\001\000\001\001\001\000\001\004\001\f\001\024\001\017\001\b\001\n\001\030\001\001\001\005\001\001\001\027\001\016\001\003\001\006\001\013\001\021\001\001\001\t\001\022\001\007\001\r\001\023\001\026\001\020\001\025\001\001\001\000\001\032ﾃ\000");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   private static final int[] ZZ_ACTION = zzUnpackAction();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZZ_ACTION_PACKED_0 = "\001\001\002\000\002\001\001\002\f\001\001\003\002\004\001\005\001\006\001\007\001\b\001\t\001\n\001\013\001\f\001\t\001\r\001\016\001\017\016\001\001\f\001\017\001\001\001\020!\001";
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackAction() {
/*  96 */     int[] result = new int[84];
/*  97 */     int offset = 0;
/*  98 */     offset = zzUnpackAction("\001\001\002\000\002\001\001\002\f\001\001\003\002\004\001\005\001\006\001\007\001\b\001\t\001\n\001\013\001\f\001\t\001\r\001\016\001\017\016\001\001\f\001\017\001\001\001\020!\001", offset, result);
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
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZZ_ROWMAP_PACKED_0 = "\000\000\000!\000B\000c\000\000¥\000Æ\000ç\000Ĉ\000ĩ\000Ŋ\000ū\000ƌ\000ƭ\000ǎ\000ǯ\000Ȑ\000ȱ\000c\000c\000ɒ\000c\000c\000ɳ\000c\000ʔ\000c\000c\000ʵ\000˖\000c\000c\000˷\000̘\000̹\000͚\000ͻ\000Μ\000ν\000Ϟ\000Ͽ\000Р\000с\000Ѣ\000҃\000Ҥ\000Ӆ\000c\000c\000Ӧ\000\000ԇ\000Ԩ\000Չ\000ժ\000֋\000֬\000׍\000׮\000؏\000ذ\000ّ\000ٲ\000ړ\000ڴ\000ە\000۶\000ܗ\000ܸ\000ݙ\000ݺ\000ޛ\000޼\000ߝ\000߾\000ࠟ\000ࡀ\000ࡡ\000ࢂ\000ࢣ\000ࣄ\000ࣥ\000आ\000ध";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackRowMap() {
/* 134 */     int[] result = new int[84];
/* 135 */     int offset = 0;
/* 136 */     offset = zzUnpackRowMap("\000\000\000!\000B\000c\000\000¥\000Æ\000ç\000Ĉ\000ĩ\000Ŋ\000ū\000ƌ\000ƭ\000ǎ\000ǯ\000Ȑ\000ȱ\000c\000c\000ɒ\000c\000c\000ɳ\000c\000ʔ\000c\000c\000ʵ\000˖\000c\000c\000˷\000̘\000̹\000͚\000ͻ\000Μ\000ν\000Ϟ\000Ͽ\000Р\000с\000Ѣ\000҃\000Ҥ\000Ӆ\000c\000c\000Ӧ\000\000ԇ\000Ԩ\000Չ\000ժ\000֋\000֬\000׍\000׮\000؏\000ذ\000ّ\000ٲ\000ړ\000ڴ\000ە\000۶\000ܗ\000ܸ\000ݙ\000ݺ\000ޛ\000޼\000ߝ\000߾\000ࠟ\000ࡀ\000ࡡ\000ࢂ\000ࢣ\000ࣄ\000ࣥ\000आ\000ध", offset, result);
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
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZZ_TRANS_PACKED_0 = "\001\004\001\005\001\006\001\007\001\b\003\005\001\t\001\n\001\013\001\f\001\005\001\r\001\016\003\005\001\017\001\020\001\021\001\005\001\022\002\005\001\023\001\024\001\025\001\026\001\027\001\030\001\031\001\004\034\032\001\033\002\032\001\034\001\035\035\036\001\037\001\036\001 \001!\"\000\001\005\001\000\026\005\n\000\001\006\037\000\001\005\001\000\001\005\001\"\024\005\t\000\001\005\001\000\006\005\001#\005\005\001$\t\005\t\000\001\005\001\000\003\005\001%\t\005\001&\b\005\t\000\001\005\001\000\n\005\001'\013\005\t\000\001\005\001\000\006\005\001(\017\005\t\000\001\005\001\000\003\005\001)\022\005\t\000\001\005\001\000\017\005\001*\006\005\t\000\001\005\001\000\001\005\001+\024\005\t\000\001\005\001\000\004\005\001,\021\005\t\000\001\005\001\000\b\005\001-\r\005\t\000\001\005\001\000\001$\007\005\001.\r\005\t\000\001\005\001\000\b\005\001/\r\005#\000\001\024\005\000\037\030\001\000\001\030\034\032\001\000\002\032\002\000\0370\001\000\0010\035\036\001\000\001\036\002\000\0371\001\000\0011\001\000\001\005\001\000\002\005\0012\023\005\t\000\001\005\001\000\025\005\0013\t\000\001\005\001\000\f\005\0013\t\005\t\000\001\005\001\000\004\005\0014\013\005\0013\005\005\t\000\001\005\001\000\016\005\0015\007\005\t\000\001\005\001\000\003\005\0013\022\005\t\000\001\005\001\000\b\005\0016\r\005\t\000\001\005\001\000\t\005\0017\f\005\t\000\001\005\001\000\005\005\0018\020\005\t\000\001\005\001\000\t\005\0019\f\005\t\000\001\005\001\000\b\005\001:\r\005\t\000\001\005\001\000\013\005\001;\n\005\t\000\001\005\001\000\016\005\001<\007\005\t\000\001\005\001\000\006\005\001=\017\005\t\000\001\005\001\000\003\005\001>\022\005\t\000\001\005\001\000\006\005\001?\017\005\t\000\001\005\001\000\b\005\001@\r\005\t\000\001\005\001\000\0013\025\005\t\000\001\005\001\000\n\005\001A\013\005\t\000\001\005\001\000\006\005\0013\017\005\t\000\001\005\001\000\005\005\001B\020\005\t\000\001\005\001\000\016\005\001C\007\005\t\000\001\005\001\000\n\005\001D\013\005\t\000\001\005\001\000\022\005\0013\003\005\t\000\001\005\001\000\024\005\001E\001\005\t\000\001\005\001\000\004\005\001F\021\005\t\000\001\005\001\000\022\005\001G\003\005\t\000\001\005\001\000\017\005\001H\006\005\t\000\001\005\001\000\002\005\001I\023\005\t\000\001\005\001\000\013\005\0013\n\005\t\000\001\005\001\000\017\005\001J\006\005\t\000\001\005\001\000\001H\025\005\t\000\001\005\001\000\f\005\001K\t\005\t\000\001\005\001\000\001\005\001L\024\005\t\000\001\005\001\000\016\005\001M\007\005\t\000\001\005\001\000\005\005\0013\020\005\t\000\001\005\001\000\013\005\001$\n\005\t\000\001\005\001\000\002\005\001N\023\005\t\000\001\005\001\000\002\005\0018\023\005\t\000\001\005\001\000\002\005\001O\023\005\t\000\001\005\001\000\b\005\001P\r\005\t\000\001\005\001\000\025\005\001Q\t\000\001\005\001\000\003\005\001*\022\005\t\000\001\005\001\000\002\005\001R\023\005\t\000\001\005\001\000\003\005\001S\022\005\t\000\001\005\001\000\003\005\001T\022\005\t\000\001\005\001\000\001\005\001B\024\005\t\000\001\005\001\000\004\005\0013\021\005\b\000";
/*     */ 
/*     */ 
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
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackTrans() {
/* 213 */     int[] result = new int[2376];
/* 214 */     int offset = 0;
/* 215 */     offset = zzUnpackTrans("\001\004\001\005\001\006\001\007\001\b\003\005\001\t\001\n\001\013\001\f\001\005\001\r\001\016\003\005\001\017\001\020\001\021\001\005\001\022\002\005\001\023\001\024\001\025\001\026\001\027\001\030\001\031\001\004\034\032\001\033\002\032\001\034\001\035\035\036\001\037\001\036\001 \001!\"\000\001\005\001\000\026\005\n\000\001\006\037\000\001\005\001\000\001\005\001\"\024\005\t\000\001\005\001\000\006\005\001#\005\005\001$\t\005\t\000\001\005\001\000\003\005\001%\t\005\001&\b\005\t\000\001\005\001\000\n\005\001'\013\005\t\000\001\005\001\000\006\005\001(\017\005\t\000\001\005\001\000\003\005\001)\022\005\t\000\001\005\001\000\017\005\001*\006\005\t\000\001\005\001\000\001\005\001+\024\005\t\000\001\005\001\000\004\005\001,\021\005\t\000\001\005\001\000\b\005\001-\r\005\t\000\001\005\001\000\001$\007\005\001.\r\005\t\000\001\005\001\000\b\005\001/\r\005#\000\001\024\005\000\037\030\001\000\001\030\034\032\001\000\002\032\002\000\0370\001\000\0010\035\036\001\000\001\036\002\000\0371\001\000\0011\001\000\001\005\001\000\002\005\0012\023\005\t\000\001\005\001\000\025\005\0013\t\000\001\005\001\000\f\005\0013\t\005\t\000\001\005\001\000\004\005\0014\013\005\0013\005\005\t\000\001\005\001\000\016\005\0015\007\005\t\000\001\005\001\000\003\005\0013\022\005\t\000\001\005\001\000\b\005\0016\r\005\t\000\001\005\001\000\t\005\0017\f\005\t\000\001\005\001\000\005\005\0018\020\005\t\000\001\005\001\000\t\005\0019\f\005\t\000\001\005\001\000\b\005\001:\r\005\t\000\001\005\001\000\013\005\001;\n\005\t\000\001\005\001\000\016\005\001<\007\005\t\000\001\005\001\000\006\005\001=\017\005\t\000\001\005\001\000\003\005\001>\022\005\t\000\001\005\001\000\006\005\001?\017\005\t\000\001\005\001\000\b\005\001@\r\005\t\000\001\005\001\000\0013\025\005\t\000\001\005\001\000\n\005\001A\013\005\t\000\001\005\001\000\006\005\0013\017\005\t\000\001\005\001\000\005\005\001B\020\005\t\000\001\005\001\000\016\005\001C\007\005\t\000\001\005\001\000\n\005\001D\013\005\t\000\001\005\001\000\022\005\0013\003\005\t\000\001\005\001\000\024\005\001E\001\005\t\000\001\005\001\000\004\005\001F\021\005\t\000\001\005\001\000\022\005\001G\003\005\t\000\001\005\001\000\017\005\001H\006\005\t\000\001\005\001\000\002\005\001I\023\005\t\000\001\005\001\000\013\005\0013\n\005\t\000\001\005\001\000\017\005\001J\006\005\t\000\001\005\001\000\001H\025\005\t\000\001\005\001\000\f\005\001K\t\005\t\000\001\005\001\000\001\005\001L\024\005\t\000\001\005\001\000\016\005\001M\007\005\t\000\001\005\001\000\005\005\0013\020\005\t\000\001\005\001\000\013\005\001$\n\005\t\000\001\005\001\000\002\005\001N\023\005\t\000\001\005\001\000\002\005\0018\023\005\t\000\001\005\001\000\002\005\001O\023\005\t\000\001\005\001\000\b\005\001P\r\005\t\000\001\005\001\000\025\005\001Q\t\000\001\005\001\000\003\005\001*\022\005\t\000\001\005\001\000\002\005\001R\023\005\t\000\001\005\001\000\003\005\001S\022\005\t\000\001\005\001\000\003\005\001T\022\005\t\000\001\005\001\000\001\005\001B\024\005\t\000\001\005\001\000\004\005\0013\021\005\b\000", offset, result);
/* 216 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackTrans(String packed, int offset, int[] result) {
/* 220 */     int i = 0;
/* 221 */     int j = offset;
/* 222 */     int l = packed.length();
/* 223 */     label10: while (i < l) {
/* 224 */       int count = packed.charAt(i++);
/* 225 */       int value = packed.charAt(i++);
/* 226 */       value--; while (true)
/* 227 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 229 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 239 */   private static final String[] ZZ_ERROR_MSG = new String[] { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 248 */   private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
/*     */   
/*     */   private static final String ZZ_ATTRIBUTE_PACKED_0 = "\001\001\002\000\001\t\016\001\002\t\001\001\002\t\001\001\001\t\001\001\002\t\002\001\002\t\017\001\002\t#\001";
/*     */   private Reader zzReader;
/*     */   private int zzState;
/*     */   
/*     */   private static int[] zzUnpackAttribute() {
/* 255 */     int[] result = new int[84];
/* 256 */     int offset = 0;
/* 257 */     offset = zzUnpackAttribute("\001\001\002\000\001\t\016\001\002\t\001\001\002\t\001\001\001\t\001\001\002\t\002\001\002\t\017\001\002\t#\001", offset, result);
/* 258 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAttribute(String packed, int offset, int[] result) {
/* 262 */     int i = 0;
/* 263 */     int j = offset;
/* 264 */     int l = packed.length();
/* 265 */     label10: while (i < l) {
/* 266 */       int count = packed.charAt(i++);
/* 267 */       int value = packed.charAt(i++); while (true)
/* 268 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 270 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 280 */   private int zzLexicalState = 0;
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
/*     */   private int zzEndRead;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean zzAtEOF;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DockerTokenMaker() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToken(int tokenType) {
/* 319 */     addToken(this.zzStartRead, this.zzMarkedPos - 1, tokenType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToken(int start, int end, int tokenType) {
/* 329 */     int so = start + this.offsetShift;
/* 330 */     addToken(this.zzBuffer, start, end, tokenType, so);
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
/* 346 */     super.addToken(array, start, end, tokenType, startOffset);
/* 347 */     this.zzStartRead = this.zzMarkedPos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getLineCommentStartAndEnd(int languageIndex) {
/* 356 */     return new String[] { "#", null };
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
/* 370 */     return (type == 20 || type == 6);
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
/* 389 */     resetTokenList();
/* 390 */     this.offsetShift = -text.offset + startOffset;
/*     */ 
/*     */     
/* 393 */     int state = 0;
/*     */     
/* 395 */     this.s = text;
/*     */     try {
/* 397 */       yyreset(this.zzReader);
/* 398 */       yybegin(state);
/* 399 */       return yylex();
/* 400 */     } catch (IOException ioe) {
/* 401 */       ioe.printStackTrace();
/* 402 */       return (Token)new TokenImpl();
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
/* 415 */     return (this.zzCurrentPos >= this.s.offset + this.s.count);
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
/* 431 */     this.zzBuffer = this.s.array;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 439 */     this.zzStartRead = this.s.offset;
/* 440 */     this.zzEndRead = this.zzStartRead + this.s.count - 1;
/* 441 */     this.zzCurrentPos = this.zzMarkedPos = this.s.offset;
/* 442 */     this.zzLexicalState = 0;
/* 443 */     this.zzReader = reader;
/* 444 */     this.zzAtEOF = false;
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
/*     */   public DockerTokenMaker(Reader in) {
/* 457 */     this.zzReader = in;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DockerTokenMaker(InputStream in) {
/* 467 */     this(new InputStreamReader(in));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static char[] zzUnpackCMap(String packed) {
/* 477 */     char[] map = new char[65536];
/* 478 */     int i = 0;
/* 479 */     int j = 0;
/* 480 */     label10: while (i < 160) {
/* 481 */       int count = packed.charAt(i++);
/* 482 */       char value = packed.charAt(i++); while (true)
/* 483 */       { map[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 485 */     }  return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void yyclose() throws IOException {
/* 493 */     this.zzAtEOF = true;
/* 494 */     this.zzEndRead = this.zzStartRead;
/*     */     
/* 496 */     if (this.zzReader != null) {
/* 497 */       this.zzReader.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int yystate() {
/* 505 */     return this.zzLexicalState;
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
/* 516 */     this.zzLexicalState = newState;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String yytext() {
/* 524 */     return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
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
/* 540 */     return this.zzBuffer[this.zzStartRead + pos];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int yylength() {
/* 548 */     return this.zzMarkedPos - this.zzStartRead;
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
/* 569 */       message = ZZ_ERROR_MSG[errorCode];
/*     */     }
/* 571 */     catch (ArrayIndexOutOfBoundsException e) {
/* 572 */       message = ZZ_ERROR_MSG[0];
/*     */     } 
/*     */     
/* 575 */     throw new Error(message);
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
/* 588 */     if (number > yylength()) {
/* 589 */       zzScanError(2);
/*     */     }
/* 591 */     this.zzMarkedPos -= number;
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
/* 609 */     int zzEndReadL = this.zzEndRead;
/* 610 */     char[] zzBufferL = this.zzBuffer;
/* 611 */     char[] zzCMapL = ZZ_CMAP;
/*     */     
/* 613 */     int[] zzTransL = ZZ_TRANS;
/* 614 */     int[] zzRowMapL = ZZ_ROWMAP;
/* 615 */     int[] zzAttrL = ZZ_ATTRIBUTE;
/*     */     
/*     */     while (true) {
/* 618 */       int zzInput, zzMarkedPosL = this.zzMarkedPos;
/*     */       
/* 620 */       int zzAction = -1;
/*     */       
/* 622 */       int zzCurrentPosL = this.zzCurrentPos = this.zzStartRead = zzMarkedPosL;
/*     */       
/* 624 */       this.zzState = this.zzLexicalState;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 630 */         if (zzCurrentPosL < zzEndReadL)
/* 631 */         { zzInput = zzBufferL[zzCurrentPosL++]; }
/* 632 */         else { if (this.zzAtEOF) {
/* 633 */             int i = -1;
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 638 */           this.zzCurrentPos = zzCurrentPosL;
/* 639 */           this.zzMarkedPos = zzMarkedPosL;
/* 640 */           boolean eof = zzRefill();
/*     */           
/* 642 */           zzCurrentPosL = this.zzCurrentPos;
/* 643 */           zzMarkedPosL = this.zzMarkedPos;
/* 644 */           zzBufferL = this.zzBuffer;
/* 645 */           zzEndReadL = this.zzEndRead;
/* 646 */           if (eof) {
/* 647 */             int i = -1;
/*     */             
/*     */             break;
/*     */           } 
/* 651 */           zzInput = zzBufferL[zzCurrentPosL++]; }
/*     */ 
/*     */         
/* 654 */         int zzNext = zzTransL[zzRowMapL[this.zzState] + zzCMapL[zzInput]];
/* 655 */         if (zzNext == -1)
/* 656 */           break;  this.zzState = zzNext;
/*     */         
/* 658 */         int zzAttributes = zzAttrL[this.zzState];
/* 659 */         if ((zzAttributes & 0x1) == 1) {
/* 660 */           zzAction = this.zzState;
/* 661 */           zzMarkedPosL = zzCurrentPosL;
/* 662 */           if ((zzAttributes & 0x8) == 8) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 669 */       this.zzMarkedPos = zzMarkedPosL;
/*     */       
/* 671 */       switch ((zzAction < 0) ? zzAction : ZZ_ACTION[zzAction]) {
/*     */         case 16:
/* 673 */           addToken(6); continue;
/*     */         case 17:
/*     */           continue;
/*     */         case 1:
/* 677 */           addToken(20); continue;
/*     */         case 18:
/*     */           continue;
/*     */         case 7:
/* 681 */           addToken(1); addNullToken(); return (Token)this.firstToken;
/*     */         case 19:
/*     */           continue;
/*     */         case 2:
/* 685 */           addToken(21); continue;
/*     */         case 20:
/*     */           continue;
/*     */         case 11:
/* 689 */           addToken(this.start, this.zzStartRead - 1, 13); return (Token)this.firstToken;
/*     */         case 21:
/*     */           continue;
/*     */         case 14:
/* 693 */           addToken(this.start, this.zzStartRead - 1, 14); return (Token)this.firstToken;
/*     */         
/*     */         case 22:
/*     */         case 12:
/*     */         case 23:
/*     */           continue;
/*     */         
/*     */         case 4:
/* 701 */           addToken(23);
/*     */           continue;
/*     */         
/*     */         case 24:
/*     */         case 15:
/*     */         case 25:
/*     */           continue;
/*     */         case 5:
/* 709 */           this.start = this.zzMarkedPos - 1; yybegin(1); continue;
/*     */         case 26:
/*     */           continue;
/*     */         case 13:
/* 713 */           yybegin(0); addToken(this.start, this.zzStartRead, 14); continue;
/*     */         case 27:
/*     */           continue;
/*     */         case 10:
/* 717 */           yybegin(0); addToken(this.start, this.zzStartRead, 13); continue;
/*     */         case 28:
/*     */           continue;
/*     */         case 8:
/* 721 */           addNullToken(); return (Token)this.firstToken;
/*     */         
/*     */         case 29:
/*     */         case 9:
/*     */         case 30:
/*     */           continue;
/*     */         
/*     */         case 3:
/* 729 */           addToken(22); continue;
/*     */         case 31:
/*     */           continue;
/*     */         case 6:
/* 733 */           this.start = this.zzMarkedPos - 1; yybegin(2); continue;
/*     */         case 32:
/*     */           continue;
/*     */       } 
/* 737 */       if (zzInput == -1 && this.zzStartRead == this.zzCurrentPos) {
/* 738 */         this.zzAtEOF = true;
/* 739 */         switch (this.zzLexicalState) {
/*     */           case 1:
/* 741 */             addToken(this.start, this.zzStartRead - 1, 13); return (Token)this.firstToken;
/*     */           case 85:
/*     */             continue;
/*     */           case 2:
/* 745 */             addToken(this.start, this.zzStartRead - 1, 14); return (Token)this.firstToken;
/*     */           case 86:
/*     */             continue;
/*     */           case 0:
/* 749 */             addNullToken(); return (Token)this.firstToken;
/*     */           case 87:
/*     */             continue;
/*     */         } 
/* 753 */         return null;
/*     */       } 
/*     */ 
/*     */       
/* 757 */       zzScanError(1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\modes\DockerTokenMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */