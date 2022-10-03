/*     */ package org.fife.ui.rsyntaxtextarea.modes;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import javax.swing.text.Segment;
/*     */ import org.fife.ui.rsyntaxtextarea.OccurrenceMarker;
/*     */ import org.fife.ui.rsyntaxtextarea.Token;
/*     */ import org.fife.ui.rsyntaxtextarea.TokenImpl;
/*     */ import org.fife.ui.rsyntaxtextarea.XmlOccurrenceMarker;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XMLTokenMaker
/*     */   extends AbstractMarkupTokenMaker
/*     */ {
/*     */   public static final int YYEOF = -1;
/*     */   public static final int INTAG = 4;
/*     */   public static final int DTD = 3;
/*     */   public static final int INATTR_DOUBLE = 5;
/*     */   public static final int YYINITIAL = 0;
/*     */   public static final int COMMENT = 1;
/*     */   public static final int CDATA = 7;
/*     */   public static final int INATTR_SINGLE = 6;
/*     */   public static final int PI = 2;
/*     */   private static final String ZZ_CMAP_PACKED = "\t\000\001\006\001\004\001\000\001\003\023\000\001\006\001\n\001\t\001\022\001\030\001\022\001\007\001\025\005\022\001\002\001\"\001\024\n\027\001\023\001\b\001\005\001$\001\021\001#\001\022\001\016\001\026\001\f\001\r\017\026\001\017\006\026\001\013\001\000\001\020\001\000\001\001\001\000\004\026\001 \001\035\001\026\001\031\001\036\002\026\001\037\003\026\001\033\002\026\001\034\001\032\002\026\001!\003\026\003\000\001\022ﾁ\000";
/*  87 */   private static final char[] ZZ_CMAP = zzUnpackCMap("\t\000\001\006\001\004\001\000\001\003\023\000\001\006\001\n\001\t\001\022\001\030\001\022\001\007\001\025\005\022\001\002\001\"\001\024\n\027\001\023\001\b\001\005\001$\001\021\001#\001\022\001\016\001\026\001\f\001\r\017\026\001\017\006\026\001\013\001\000\001\020\001\000\001\001\001\000\004\026\001 \001\035\001\026\001\031\001\036\002\026\001\037\003\026\001\033\002\026\001\034\001\032\002\026\001!\003\026\003\000\001\022ﾁ\000");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   private static final int[] ZZ_ACTION = zzUnpackAction();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZZ_ACTION_PACKED_0 = "\005\000\002\001\001\000\002\002\001\003\001\004\001\005\001\006\002\001\001\007\004\001\001\b\002\001\001\t\001\001\001\n\001\013\001\f\002\r\001\016\001\017\001\020\001\021\001\022\001\001\001\023\003\001\001\024\001\025\001\004\001\026\001\006\005\000\001\027\004\000\001\030\001\031\005\000\001\032\001\033\003\000\001\034\001\035\006\000\001\036";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackAction() {
/* 103 */     int[] result = new int[77];
/* 104 */     int offset = 0;
/* 105 */     offset = zzUnpackAction("\005\000\002\001\001\000\002\002\001\003\001\004\001\005\001\006\002\001\001\007\004\001\001\b\002\001\001\t\001\001\001\n\001\013\001\f\002\r\001\016\001\017\001\020\001\021\001\022\001\001\001\023\003\001\001\024\001\025\001\004\001\026\001\006\005\000\001\027\004\000\001\030\001\031\005\000\001\032\001\033\003\000\001\034\001\035\006\000\001\036", offset, result);
/* 106 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackAction(String packed, int offset, int[] result) {
/* 110 */     int i = 0;
/* 111 */     int j = offset;
/* 112 */     int l = packed.length();
/* 113 */     label10: while (i < l) {
/* 114 */       int count = packed.charAt(i++);
/* 115 */       int value = packed.charAt(i++); while (true)
/* 116 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 118 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 125 */   private static final int[] ZZ_ROWMAP = zzUnpackRowMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZZ_ROWMAP_PACKED_0 = "\000\000\000%\000J\000o\000\000¹\000Þ\000ă\000Ĩ\000ō\000Ų\000Ɨ\000Ƽ\000ǡ\000Ȇ\000ȫ\000Ų\000ɐ\000ɵ\000ʚ\000ʿ\000Ų\000ˤ\000̉\000Ų\000̮\000Ų\000Ų\000Ų\000͓\000͸\000Ų\000Ų\000Ν\000Ų\000Ų\000ς\000Ų\000ϧ\000Ќ\000б\000і\000ѻ\000Ҡ\000Ų\000Ų\000Ӆ\000Ӫ\000ԏ\000Դ\000ՙ\000Ų\000վ\000֣\000׈\000׭\000ؒ\000Ų\000ط\000ٜ\000ځ\000ڦ\000ۋ\000Ų\000Ų\000۰\000ܕ\000ܺ\000ݟ\000Ų\000ބ\000ީ\000ݟ\000ߎ\000߳\000࠘\000Ų";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackRowMap() {
/* 140 */     int[] result = new int[77];
/* 141 */     int offset = 0;
/* 142 */     offset = zzUnpackRowMap("\000\000\000%\000J\000o\000\000¹\000Þ\000ă\000Ĩ\000ō\000Ų\000Ɨ\000Ƽ\000ǡ\000Ȇ\000ȫ\000Ų\000ɐ\000ɵ\000ʚ\000ʿ\000Ų\000ˤ\000̉\000Ų\000̮\000Ų\000Ų\000Ų\000͓\000͸\000Ų\000Ų\000Ν\000Ų\000Ų\000ς\000Ų\000ϧ\000Ќ\000б\000і\000ѻ\000Ҡ\000Ų\000Ų\000Ӆ\000Ӫ\000ԏ\000Դ\000ՙ\000Ų\000վ\000֣\000׈\000׭\000ؒ\000Ų\000ط\000ٜ\000ځ\000ڦ\000ۋ\000Ų\000Ų\000۰\000ܕ\000ܺ\000ݟ\000Ų\000ބ\000ީ\000ݟ\000ߎ\000߳\000࠘\000Ų", offset, result);
/* 143 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackRowMap(String packed, int offset, int[] result) {
/* 147 */     int i = 0;
/* 148 */     int j = offset;
/* 149 */     int l = packed.length();
/* 150 */     while (i < l) {
/* 151 */       int high = packed.charAt(i++) << 16;
/* 152 */       result[j++] = high | packed.charAt(i++);
/*     */     } 
/* 154 */     return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 160 */   private static final int[] ZZ_TRANS = zzUnpackTrans();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String ZZ_TRANS_PACKED_0 = "\003\t\001\n\001\013\001\f\001\r\001\016\035\t\002\017\001\020\001\017\001\021\024\017\001\022\003\017\001\023\003\017\001\024\003\017\004\025\001\026\036\025\001\027\001\025\004\030\001\031\001\032\005\030\001\033\004\030\001\034\001\035\023\030\003\036\001\037\001\000\001\036\001\r\002\036\001 \007\036\001!\002\036\001\"\001#\016\036\001$\t%\001&\033%\025'\001&\017'\020(\001)\024(\004\t\004\000 \t\001\n\002\000\001\r\001\000\035\t&\000\001*\b\000\001+\001\000\004*\003\000\001*\001,\001\000\001*\002\000\t*\001\000\001-\004\000\001\r\002\000\001\r\036\000\006\016\001\000\001\016\001.\034\016\002\017\001\000\001\017\001\000\024\017\001\000\003\017\001\000\003\017\001\000\003\017\002\000\001/<\000\0010$\000\0011\003\000\0012'\000\0013\003\000\004\025\001\000\036\025\001\000\001\025\021\000\0014\023\000\004\030\002\000\005\030\001\000\004\030\002\000\023\030\n\000\0015\032\000\004\036\001\000\001\036\001\000\002\036\001\000\007\036\001\000\002\036\002\000\016\036\001\000\003\036\001\037\001\000\001\036\001\r\002\036\001\000\007\036\001\000\002\036\002\000\016\036\022\000\001!\023\000\t%\001\000\033%\025'\001\000\017'\020(\001\000\024(\020\000\0016\025\000\002*\t\000\004*\003\000\001*\002\000\002*\001\000\n*\004\000\0017\b\000\0018\032\000\0019\n\000\0049\003\000\0019\002\000\0019\002\000\t9\024\000\001:-\000\001;%\000\001<(\000\001=&\000\001>\005\000\001?3\000\001@\025\000\001A.\000\001B\031\000\0029\t\000\0049\003\000\0019\002\000\0029\001\000\n9\035\000\001C\034\000\001D1\000\001<&\000\001E\004\000\001F/\000\001G*\000\001D\b\000\001<\034\000\001H\021\000\002I\004\000\002I\001\000\002I\004E\001I\001\000\002I\001E\001I\fE\003I\016\000\001J*\000\001E\037\000\001K#\000\001L!\000\001M\031\000";
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
/*     */   
/*     */   private static final int ZZ_PUSHBACK_2BIG = 2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackTrans() {
/* 198 */     int[] result = new int[2109];
/* 199 */     int offset = 0;
/* 200 */     offset = zzUnpackTrans("\003\t\001\n\001\013\001\f\001\r\001\016\035\t\002\017\001\020\001\017\001\021\024\017\001\022\003\017\001\023\003\017\001\024\003\017\004\025\001\026\036\025\001\027\001\025\004\030\001\031\001\032\005\030\001\033\004\030\001\034\001\035\023\030\003\036\001\037\001\000\001\036\001\r\002\036\001 \007\036\001!\002\036\001\"\001#\016\036\001$\t%\001&\033%\025'\001&\017'\020(\001)\024(\004\t\004\000 \t\001\n\002\000\001\r\001\000\035\t&\000\001*\b\000\001+\001\000\004*\003\000\001*\001,\001\000\001*\002\000\t*\001\000\001-\004\000\001\r\002\000\001\r\036\000\006\016\001\000\001\016\001.\034\016\002\017\001\000\001\017\001\000\024\017\001\000\003\017\001\000\003\017\001\000\003\017\002\000\001/<\000\0010$\000\0011\003\000\0012'\000\0013\003\000\004\025\001\000\036\025\001\000\001\025\021\000\0014\023\000\004\030\002\000\005\030\001\000\004\030\002\000\023\030\n\000\0015\032\000\004\036\001\000\001\036\001\000\002\036\001\000\007\036\001\000\002\036\002\000\016\036\001\000\003\036\001\037\001\000\001\036\001\r\002\036\001\000\007\036\001\000\002\036\002\000\016\036\022\000\001!\023\000\t%\001\000\033%\025'\001\000\017'\020(\001\000\024(\020\000\0016\025\000\002*\t\000\004*\003\000\001*\002\000\002*\001\000\n*\004\000\0017\b\000\0018\032\000\0019\n\000\0049\003\000\0019\002\000\0019\002\000\t9\024\000\001:-\000\001;%\000\001<(\000\001=&\000\001>\005\000\001?3\000\001@\025\000\001A.\000\001B\031\000\0029\t\000\0049\003\000\0019\002\000\0029\001\000\n9\035\000\001C\034\000\001D1\000\001<&\000\001E\004\000\001F/\000\001G*\000\001D\b\000\001<\034\000\001H\021\000\002I\004\000\002I\001\000\002I\004E\001I\001\000\002I\001E\001I\fE\003I\016\000\001J*\000\001E\037\000\001K#\000\001L!\000\001M\031\000", offset, result);
/* 201 */     return result;
/*     */   }
/*     */   
/*     */   private static int zzUnpackTrans(String packed, int offset, int[] result) {
/* 205 */     int i = 0;
/* 206 */     int j = offset;
/* 207 */     int l = packed.length();
/* 208 */     label10: while (i < l) {
/* 209 */       int count = packed.charAt(i++);
/* 210 */       int value = packed.charAt(i++);
/* 211 */       value--; while (true)
/* 212 */       { result[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 214 */     }  return j;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 224 */   private static final String[] ZZ_ERROR_MSG = new String[] { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 233 */   private static final int[] ZZ_ATTRIBUTE = zzUnpackAttribute();
/*     */   
/*     */   private static final String ZZ_ATTRIBUTE_PACKED_0 = "\005\000\002\001\001\000\002\001\001\t\005\001\001\t\004\001\001\t\002\001\001\t\001\001\003\t\002\001\002\t\001\001\002\t\001\001\001\t\006\001\002\t\005\000\001\t\004\000\001\001\001\t\005\000\002\t\003\000\001\001\001\t\006\000\001\t";
/*     */   
/*     */   private Reader zzReader;
/*     */   
/*     */   private int zzState;
/*     */ 
/*     */   
/*     */   private static int[] zzUnpackAttribute() {
/* 243 */     int[] result = new int[77];
/* 244 */     int offset = 0;
/* 245 */     offset = zzUnpackAttribute("\005\000\002\001\001\000\002\001\001\t\005\001\001\t\004\001\001\t\002\001\001\t\001\001\003\t\002\001\002\t\001\001\002\t\001\001\001\t\006\001\002\t\005\000\001\t\004\000\001\001\001\t\005\000\002\t\003\000\001\001\001\t\006\000\001\t", offset, result);
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
/*     */   
/*     */   private int zzStartRead;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int zzEndRead;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean zzAtEOF;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int INTERNAL_ATTR_DOUBLE = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int INTERNAL_ATTR_SINGLE = -2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int INTERNAL_INTAG = -3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int INTERNAL_DTD = -4;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int INTERNAL_DTD_INTERNAL = -5;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int INTERNAL_IN_XML_COMMENT = -2048;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean completeCloseTags = true;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean inInternalDtd;
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
/*     */   public XMLTokenMaker() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addEndToken(int tokenType) {
/* 369 */     addToken(this.zzMarkedPos, this.zzMarkedPos, tokenType);
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
/* 380 */     int so = start + this.offsetShift;
/* 381 */     addToken(this.zzBuffer, start, end, tokenType, so, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToken(int tokenType) {
/* 391 */     addToken(this.zzStartRead, this.zzMarkedPos - 1, tokenType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addToken(int start, int end, int tokenType) {
/* 401 */     int so = start + this.offsetShift;
/* 402 */     addToken(this.zzBuffer, start, end, tokenType, so);
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
/* 418 */     super.addToken(array, start, end, tokenType, startOffset);
/* 419 */     this.zzStartRead = this.zzMarkedPos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected OccurrenceMarker createOccurrenceMarker() {
/* 428 */     return (OccurrenceMarker)new XmlOccurrenceMarker();
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
/* 441 */     return completeCloseTags;
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
/*     */   public static boolean getCompleteCloseMarkupTags() {
/* 454 */     return completeCloseTags;
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
/*     */   public boolean getMarkOccurrencesOfTokenType(int type) {
/* 467 */     return (type == 26);
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
/* 486 */     resetTokenList();
/* 487 */     this.offsetShift = -text.offset + startOffset;
/* 488 */     this.prevState = 0;
/* 489 */     this.inInternalDtd = false;
/*     */ 
/*     */     
/* 492 */     int state = 0;
/* 493 */     switch (initialTokenType) {
/*     */       case 29:
/* 495 */         state = 1;
/*     */         break;
/*     */       case -4:
/* 498 */         state = 3;
/*     */         break;
/*     */       case -5:
/* 501 */         state = 3;
/* 502 */         this.inInternalDtd = true;
/*     */         break;
/*     */       case -1:
/* 505 */         state = 5;
/*     */         break;
/*     */       case -2:
/* 508 */         state = 6;
/*     */         break;
/*     */       case 31:
/* 511 */         state = 2;
/*     */         break;
/*     */       case -3:
/* 514 */         state = 4;
/*     */         break;
/*     */       case 33:
/* 517 */         state = 7;
/*     */         break;
/*     */       default:
/* 520 */         if (initialTokenType < -1024) {
/* 521 */           int main = -(-initialTokenType & 0xFFFFFF00);
/* 522 */           switch (main) {
/*     */           
/*     */           } 
/* 525 */           state = 1;
/*     */ 
/*     */           
/* 528 */           this.prevState = -initialTokenType & 0xFF;
/*     */           break;
/*     */         } 
/* 531 */         state = 0;
/*     */         break;
/*     */     } 
/*     */     
/* 535 */     this.start = text.offset;
/* 536 */     this.s = text;
/*     */     try {
/* 538 */       yyreset(this.zzReader);
/* 539 */       yybegin(state);
/* 540 */       return yylex();
/* 541 */     } catch (IOException ioe) {
/* 542 */       ioe.printStackTrace();
/* 543 */       return (Token)new TokenImpl();
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
/* 556 */     completeCloseTags = complete;
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
/* 567 */     return (this.zzCurrentPos >= this.s.offset + this.s.count);
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
/* 583 */     this.zzBuffer = this.s.array;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 591 */     this.zzStartRead = this.s.offset;
/* 592 */     this.zzEndRead = this.zzStartRead + this.s.count - 1;
/* 593 */     this.zzCurrentPos = this.zzMarkedPos = this.s.offset;
/* 594 */     this.zzLexicalState = 0;
/* 595 */     this.zzReader = reader;
/* 596 */     this.zzAtEOF = false;
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
/*     */   public XMLTokenMaker(Reader in) {
/* 609 */     this.zzReader = in;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLTokenMaker(InputStream in) {
/* 619 */     this(new InputStreamReader(in));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static char[] zzUnpackCMap(String packed) {
/* 629 */     char[] map = new char[65536];
/* 630 */     int i = 0;
/* 631 */     int j = 0;
/* 632 */     label10: while (i < 116) {
/* 633 */       int count = packed.charAt(i++);
/* 634 */       char value = packed.charAt(i++); while (true)
/* 635 */       { map[j++] = value; if (--count <= 0)
/*     */           continue label10;  } 
/* 637 */     }  return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void yyclose() throws IOException {
/* 645 */     this.zzAtEOF = true;
/* 646 */     this.zzEndRead = this.zzStartRead;
/*     */     
/* 648 */     if (this.zzReader != null) {
/* 649 */       this.zzReader.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int yystate() {
/* 657 */     return this.zzLexicalState;
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
/* 668 */     this.zzLexicalState = newState;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String yytext() {
/* 676 */     return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
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
/* 692 */     return this.zzBuffer[this.zzStartRead + pos];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int yylength() {
/* 700 */     return this.zzMarkedPos - this.zzStartRead;
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
/* 721 */       message = ZZ_ERROR_MSG[errorCode];
/*     */     }
/* 723 */     catch (ArrayIndexOutOfBoundsException e) {
/* 724 */       message = ZZ_ERROR_MSG[0];
/*     */     } 
/*     */     
/* 727 */     throw new Error(message);
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
/* 740 */     if (number > yylength()) {
/* 741 */       zzScanError(2);
/*     */     }
/* 743 */     this.zzMarkedPos -= number;
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
/* 761 */     int zzEndReadL = this.zzEndRead;
/* 762 */     char[] zzBufferL = this.zzBuffer;
/* 763 */     char[] zzCMapL = ZZ_CMAP;
/*     */     
/* 765 */     int[] zzTransL = ZZ_TRANS;
/* 766 */     int[] zzRowMapL = ZZ_ROWMAP;
/* 767 */     int[] zzAttrL = ZZ_ATTRIBUTE;
/*     */     
/*     */     while (true) {
/* 770 */       int zzInput, k, j, i, count, temp, zzMarkedPosL = this.zzMarkedPos;
/*     */       
/* 772 */       int zzAction = -1;
/*     */       
/* 774 */       int zzCurrentPosL = this.zzCurrentPos = this.zzStartRead = zzMarkedPosL;
/*     */       
/* 776 */       this.zzState = this.zzLexicalState;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/* 782 */         if (zzCurrentPosL < zzEndReadL)
/* 783 */         { zzInput = zzBufferL[zzCurrentPosL++]; }
/* 784 */         else { if (this.zzAtEOF) {
/* 785 */             int m = -1;
/*     */             
/*     */             break;
/*     */           } 
/*     */           
/* 790 */           this.zzCurrentPos = zzCurrentPosL;
/* 791 */           this.zzMarkedPos = zzMarkedPosL;
/* 792 */           boolean eof = zzRefill();
/*     */           
/* 794 */           zzCurrentPosL = this.zzCurrentPos;
/* 795 */           zzMarkedPosL = this.zzMarkedPos;
/* 796 */           zzBufferL = this.zzBuffer;
/* 797 */           zzEndReadL = this.zzEndRead;
/* 798 */           if (eof) {
/* 799 */             int m = -1;
/*     */             
/*     */             break;
/*     */           } 
/* 803 */           zzInput = zzBufferL[zzCurrentPosL++]; }
/*     */ 
/*     */         
/* 806 */         int zzNext = zzTransL[zzRowMapL[this.zzState] + zzCMapL[zzInput]];
/* 807 */         if (zzNext == -1)
/* 808 */           break;  this.zzState = zzNext;
/*     */         
/* 810 */         int zzAttributes = zzAttrL[this.zzState];
/* 811 */         if ((zzAttributes & 0x1) == 1) {
/* 812 */           zzAction = this.zzState;
/* 813 */           zzMarkedPosL = zzCurrentPosL;
/* 814 */           if ((zzAttributes & 0x8) == 8) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 821 */       this.zzMarkedPos = zzMarkedPosL;
/*     */       
/* 823 */       switch ((zzAction < 0) ? zzAction : ZZ_ACTION[zzAction]) {
/*     */         case 25:
/* 825 */           k = this.zzMarkedPos; addToken(this.start, this.zzStartRead + 2, 29); this.start = k; yybegin(this.prevState); continue;
/*     */         case 31:
/*     */           continue;
/*     */         case 19:
/* 829 */           yybegin(4); addToken(this.start, this.zzStartRead, 28); continue;
/*     */         case 32:
/*     */           continue;
/*     */         case 3:
/* 833 */           addNullToken(); return (Token)this.firstToken;
/*     */         case 33:
/*     */           continue;
/*     */         case 29:
/* 837 */           k = this.zzStartRead; addToken(this.start, this.zzStartRead - 1, 30); this.start = k; this.prevState = this.zzLexicalState; yybegin(1); continue;
/*     */         case 34:
/*     */           continue;
/*     */         case 11:
/* 841 */           this.inInternalDtd = false; continue;
/*     */         case 35:
/*     */           continue;
/*     */         case 4:
/* 845 */           addToken(25); yybegin(4); continue;
/*     */         case 36:
/*     */           continue;
/*     */         case 24:
/* 849 */           j = yylength();
/* 850 */           addToken(this.zzStartRead, this.zzStartRead + 1, 25);
/* 851 */           addToken(this.zzMarkedPos - j - 2, this.zzMarkedPos - 1, 26);
/* 852 */           yybegin(4); continue;
/*     */         case 37:
/*     */           continue;
/*     */         case 9:
/* 856 */           addToken(this.start, this.zzStartRead - 1, 30); addEndToken(this.inInternalDtd ? -5 : -4); return (Token)this.firstToken;
/*     */         case 38:
/*     */           continue;
/*     */         case 16:
/* 860 */           addToken(25); continue;
/*     */         case 39:
/*     */           continue;
/*     */         case 7:
/* 864 */           addToken(this.start, this.zzStartRead - 1, 29); addEndToken(-2048 - this.prevState); return (Token)this.firstToken;
/*     */         case 40:
/*     */           continue;
/*     */         case 5:
/* 868 */           addToken(21); continue;
/*     */         case 41:
/*     */           continue;
/*     */         case 27:
/* 872 */           this.start = this.zzStartRead; this.prevState = this.zzLexicalState; yybegin(1); continue;
/*     */         case 42:
/*     */           continue;
/*     */         case 26:
/* 876 */           i = this.zzStartRead; yybegin(0); addToken(this.start, this.zzStartRead - 1, 33); addToken(i, this.zzMarkedPos - 1, 32); continue;
/*     */         case 43:
/*     */           continue;
/*     */         case 6:
/* 880 */           addToken(34); continue;
/*     */         case 44:
/*     */           continue;
/*     */         case 12:
/* 884 */           if (!this.inInternalDtd) { yybegin(0); addToken(this.start, this.zzStartRead, 30); }  continue;
/*     */         case 45:
/*     */           continue;
/*     */         case 2:
/* 888 */           addToken(20); continue;
/*     */         case 46:
/*     */           continue;
/*     */         case 10:
/* 892 */           this.inInternalDtd = true; continue;
/*     */         case 47:
/*     */           continue;
/*     */         case 23:
/* 896 */           yybegin(0); addToken(this.start, this.zzStartRead + 1, 31); continue;
/*     */         case 48:
/*     */           continue;
/*     */         case 21:
/* 900 */           this.start = this.zzMarkedPos - 2; this.inInternalDtd = false; yybegin(3); continue;
/*     */         case 49:
/*     */           continue;
/*     */         case 20:
/* 904 */           count = yylength();
/* 905 */           addToken(this.zzStartRead, this.zzStartRead, 25);
/* 906 */           addToken(this.zzMarkedPos - count - 1, this.zzMarkedPos - 1, 26);
/* 907 */           yybegin(4); continue;
/*     */         case 50:
/*     */           continue;
/*     */         case 22:
/* 911 */           this.start = this.zzMarkedPos - 2; yybegin(2); continue;
/*     */         case 51:
/*     */           continue;
/*     */         case 8:
/* 915 */           addToken(this.start, this.zzStartRead - 1, 31); return (Token)this.firstToken;
/*     */         case 52:
/*     */           continue;
/*     */         case 14:
/* 919 */           this.start = this.zzMarkedPos - 1; yybegin(5); continue;
/*     */         case 53:
/*     */           continue;
/*     */         case 28:
/* 923 */           temp = this.zzStartRead; addToken(this.start, this.zzStartRead - 1, 29); addHyperlinkToken(temp, this.zzMarkedPos - 1, 29); this.start = this.zzMarkedPos; continue;
/*     */         case 54:
/*     */           continue;
/*     */         case 15:
/* 927 */           yybegin(0); addToken(25); continue;
/*     */         case 55:
/*     */           continue;
/*     */         case 17:
/* 931 */           this.start = this.zzMarkedPos - 1; yybegin(6); continue;
/*     */         case 56:
/*     */           continue;
/*     */         case 18:
/* 935 */           addToken(23); continue;
/*     */         case 57:
/*     */           continue;
/*     */         case 30:
/* 939 */           addToken(32); this.start = this.zzMarkedPos; yybegin(7); continue;
/*     */         case 58:
/*     */           continue;
/*     */         case 13:
/* 943 */           addToken(27);
/*     */           continue;
/*     */         
/*     */         case 59:
/*     */         case 1:
/*     */         case 60:
/*     */           continue;
/*     */       } 
/* 951 */       if (zzInput == -1 && this.zzStartRead == this.zzCurrentPos) {
/* 952 */         this.zzAtEOF = true;
/* 953 */         switch (this.zzLexicalState) {
/*     */           case 4:
/* 955 */             addToken(this.start, this.zzStartRead - 1, -3); return (Token)this.firstToken;
/*     */           case 78:
/*     */             continue;
/*     */           case 3:
/* 959 */             addToken(this.start, this.zzStartRead - 1, 30); addEndToken(this.inInternalDtd ? -5 : -4); return (Token)this.firstToken;
/*     */           case 79:
/*     */             continue;
/*     */           case 5:
/* 963 */             addToken(this.start, this.zzStartRead - 1, 28); addEndToken(-1); return (Token)this.firstToken;
/*     */           case 80:
/*     */             continue;
/*     */           case 0:
/* 967 */             addNullToken(); return (Token)this.firstToken;
/*     */           case 81:
/*     */             continue;
/*     */           case 1:
/* 971 */             addToken(this.start, this.zzStartRead - 1, 29); addEndToken(-2048 - this.prevState); return (Token)this.firstToken;
/*     */           case 82:
/*     */             continue;
/*     */           case 7:
/* 975 */             addToken(this.start, this.zzStartRead - 1, 33); return (Token)this.firstToken;
/*     */           case 83:
/*     */             continue;
/*     */           case 6:
/* 979 */             addToken(this.start, this.zzStartRead - 1, 28); addEndToken(-2); return (Token)this.firstToken;
/*     */           case 84:
/*     */             continue;
/*     */           case 2:
/* 983 */             addToken(this.start, this.zzStartRead - 1, 31); return (Token)this.firstToken;
/*     */           case 85:
/*     */             continue;
/*     */         } 
/* 987 */         return null;
/*     */       } 
/*     */ 
/*     */       
/* 991 */       zzScanError(1);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fif\\ui\rsyntaxtextarea\modes\XMLTokenMaker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */