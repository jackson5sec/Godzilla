/*     */ package com.jediterm.terminal.util;
/*     */ 
/*     */ import com.jediterm.terminal.emulator.charset.CharacterSets;
/*     */ import com.jediterm.terminal.model.CharBuffer;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CharUtils
/*     */ {
/*     */   public static final int ESC = 27;
/*     */   public static final int DEL = 127;
/*     */   public static final char NUL_CHAR = '\000';
/*     */   public static final char EMPTY_CHAR = ' ';
/*     */   public static final char DWC = '';
/*  26 */   private static final String[] NONPRINTING_NAMES = new String[] { "NUL", "SOH", "STX", "ETX", "EOT", "ENQ", "ACK", "BEL", "BS", "TAB", "LF", "VT", "FF", "CR", "S0", "S1", "DLE", "DC1", "DC2", "DC3", "DC4", "NAK", "SYN", "ETB", "CAN", "EM", "SUB", "ESC", "FS", "GS", "RS", "US" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  31 */   public static byte[] VT102_RESPONSE = makeCode(new int[] { 27, 91, 63, 54, 99 });
/*     */   
/*     */   public static String getNonControlCharacters(int maxChars, char[] buf, int offset, int charsLength) {
/*  34 */     int len = Math.min(maxChars, charsLength);
/*     */     
/*  36 */     int origLen = len;
/*     */     
/*  38 */     while (len > 0) {
/*  39 */       char tmp = buf[offset++];
/*  40 */       if (' ' <= tmp) {
/*  41 */         len--;
/*     */         continue;
/*     */       } 
/*  44 */       offset--;
/*     */     } 
/*     */ 
/*     */     
/*  48 */     int length = origLen - len;
/*     */     
/*  50 */     return new String(buf, offset - length, length);
/*     */   }
/*     */   
/*     */   public static int countDoubleWidthCharacters(char[] buf, int start, int length, boolean ambiguousIsDWC) {
/*  54 */     int cnt = 0;
/*  55 */     for (int i = 0; i < length; i++) {
/*  56 */       int ucs = Character.codePointAt(buf, i + start);
/*  57 */       if (isDoubleWidthCharacter(ucs, ambiguousIsDWC)) {
/*  58 */         cnt++;
/*     */       }
/*     */     } 
/*     */     
/*  62 */     return cnt;
/*     */   }
/*     */   
/*     */   public enum CharacterType {
/*  66 */     NONPRINTING,
/*  67 */     PRINTING,
/*  68 */     NONASCII, NONE;
/*     */   }
/*     */   
/*     */   public static CharacterType appendChar(StringBuilder sb, CharacterType last, char c) {
/*  72 */     if (c <= '\037') {
/*  73 */       sb.append(' ');
/*  74 */       sb.append(NONPRINTING_NAMES[c]);
/*  75 */       return CharacterType.NONPRINTING;
/*  76 */     }  if (c == '') {
/*  77 */       sb.append(" DEL");
/*  78 */       return CharacterType.NONPRINTING;
/*  79 */     }  if (c > '\037' && c <= '~') {
/*  80 */       if (last != CharacterType.PRINTING) sb.append(' '); 
/*  81 */       sb.append(c);
/*  82 */       return CharacterType.PRINTING;
/*     */     } 
/*  84 */     sb.append(" 0x").append(Integer.toHexString(c));
/*  85 */     return CharacterType.NONASCII;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void appendBuf(StringBuilder sb, char[] bs, int begin, int length) {
/*  90 */     CharacterType last = CharacterType.NONPRINTING;
/*  91 */     int end = begin + length;
/*  92 */     for (int i = begin; i < end; i++) {
/*  93 */       char c = bs[i];
/*  94 */       last = appendChar(sb, last, c);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] makeCode(int... bytesAsInt) {
/* 100 */     byte[] bytes = new byte[bytesAsInt.length];
/* 101 */     int i = 0;
/* 102 */     for (int byteAsInt : bytesAsInt) {
/* 103 */       bytes[i] = (byte)byteAsInt;
/* 104 */       i++;
/*     */     } 
/* 106 */     return bytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getTextLengthDoubleWidthAware(char[] buffer, int start, int length, boolean ambiguousIsDWC) {
/* 114 */     int result = 0;
/* 115 */     for (int i = start; i < start + length; i++) {
/* 116 */       result += (buffer[i] != '' && isDoubleWidthCharacter(buffer[i], ambiguousIsDWC) && (i + 1 >= start + length || buffer[i + 1] != '')) ? 2 : 1;
/*     */     }
/* 118 */     return result;
/*     */   }
/*     */   
/*     */   public static boolean isDoubleWidthCharacter(int c, boolean ambiguousIsDWC) {
/* 122 */     if (c == 57344 || c <= 160 || (c > 1106 && c < 4352)) {
/* 123 */       return false;
/*     */     }
/*     */     
/* 126 */     return (mk_wcwidth(c, ambiguousIsDWC) == 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static CharBuffer heavyDecCompatibleBuffer(CharBuffer buf) {
/* 131 */     char[] c = Arrays.copyOfRange(buf.getBuf(), 0, (buf.getBuf()).length);
/* 132 */     for (int i = 0; i < c.length; i++) {
/* 133 */       c[i] = CharacterSets.getHeavyDecBoxChar(c[i]);
/*     */     }
/* 135 */     return new CharBuffer(c, buf.getStart(), buf.length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 143 */   private static final char[][] COMBINING = new char[][] { { '̀', 'ͯ' }, { '҃', '҆' }, { '҈', '҉' }, { '֑', 'ֽ' }, { 'ֿ', 'ֿ' }, { 'ׁ', 'ׂ' }, { 'ׄ', 'ׅ' }, { 'ׇ', 'ׇ' }, { '؀', '؃' }, { 'ؐ', 'ؕ' }, { 'ً', 'ٞ' }, { 'ٰ', 'ٰ' }, { 'ۖ', 'ۤ' }, { 'ۧ', 'ۨ' }, { '۪', 'ۭ' }, { '܏', '܏' }, { 'ܑ', 'ܑ' }, { 'ܰ', '݊' }, { 'ަ', 'ް' }, { '߫', '߳' }, { 'ँ', 'ं' }, { '़', '़' }, { 'ु', 'ै' }, { '्', '्' }, { '॑', '॔' }, { 'ॢ', 'ॣ' }, { 'ঁ', 'ঁ' }, { '়', '়' }, { 'ু', 'ৄ' }, { '্', '্' }, { 'ৢ', 'ৣ' }, { 'ਁ', 'ਂ' }, { '਼', '਼' }, { 'ੁ', 'ੂ' }, { 'ੇ', 'ੈ' }, { 'ੋ', '੍' }, { 'ੰ', 'ੱ' }, { 'ઁ', 'ં' }, { '઼', '઼' }, { 'ુ', 'ૅ' }, { 'ે', 'ૈ' }, { '્', '્' }, { 'ૢ', 'ૣ' }, { 'ଁ', 'ଁ' }, { '଼', '଼' }, { 'ି', 'ି' }, { 'ୁ', 'ୃ' }, { '୍', '୍' }, { 'ୖ', 'ୖ' }, { 'ஂ', 'ஂ' }, { 'ீ', 'ீ' }, { '்', '்' }, { 'ా', 'ీ' }, { 'ె', 'ై' }, { 'ొ', '్' }, { 'ౕ', 'ౖ' }, { '಼', '಼' }, { 'ಿ', 'ಿ' }, { 'ೆ', 'ೆ' }, { 'ೌ', '್' }, { 'ೢ', 'ೣ' }, { 'ു', 'ൃ' }, { '്', '്' }, { '්', '්' }, { 'ි', 'ු' }, { 'ූ', 'ූ' }, { 'ั', 'ั' }, { 'ิ', 'ฺ' }, { '็', '๎' }, { 'ັ', 'ັ' }, { 'ິ', 'ູ' }, { 'ົ', 'ຼ' }, { '່', 'ໍ' }, { '༘', '༙' }, { '༵', '༵' }, { '༷', '༷' }, { '༹', '༹' }, { 'ཱ', 'ཾ' }, { 'ྀ', '྄' }, { '྆', '྇' }, { 'ྐ', 'ྗ' }, { 'ྙ', 'ྼ' }, { '࿆', '࿆' }, { 'ိ', 'ူ' }, { 'ဲ', 'ဲ' }, { 'ံ', '့' }, { '္', '္' }, { 'ၘ', 'ၙ' }, { 'ᅠ', 'ᇿ' }, { '፟', '፟' }, { 'ᜒ', '᜔' }, { 'ᜲ', '᜴' }, { 'ᝒ', 'ᝓ' }, { 'ᝲ', 'ᝳ' }, { '឴', '឵' }, { 'ិ', 'ួ' }, { 'ំ', 'ំ' }, { '៉', '៓' }, { '៝', '៝' }, { '᠋', '᠍' }, { 'ᢩ', 'ᢩ' }, { 'ᤠ', 'ᤢ' }, { 'ᤧ', 'ᤨ' }, { 'ᤲ', 'ᤲ' }, { '᤹', '᤻' }, { 'ᨗ', 'ᨘ' }, { 'ᬀ', 'ᬃ' }, { '᬴', '᬴' }, { 'ᬶ', 'ᬺ' }, { 'ᬼ', 'ᬼ' }, { 'ᭂ', 'ᭂ' }, { '᭫', '᭳' }, { '᷀', '᷊' }, { '᷾', '᷿' }, { '​', '‏' }, { '‪', '‮' }, { '⁠', '⁣' }, { '⁪', '⁯' }, { '⃐', '⃯' }, { '〪', '〯' }, { '゙', '゚' }, { '꠆', '꠆' }, { 'ꠋ', 'ꠋ' }, { 'ꠥ', 'ꠦ' }, { 'ﬞ', 'ﬞ' }, { '︀', '️' }, { '︠', '︣' }, { '﻿', '﻿' }, { '￹', '￻' } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 189 */   private static final char[][] AMBIGUOUS = new char[][] { { '¡', '¡' }, { '¤', '¤' }, { '§', '¨' }, { 'ª', 'ª' }, { '®', '®' }, { '°', '´' }, { '¶', 'º' }, { '¼', '¿' }, { 'Æ', 'Æ' }, { 'Ð', 'Ð' }, { '×', 'Ø' }, { 'Þ', 'á' }, { 'æ', 'æ' }, { 'è', 'ê' }, { 'ì', 'í' }, { 'ð', 'ð' }, { 'ò', 'ó' }, { '÷', 'ú' }, { 'ü', 'ü' }, { 'þ', 'þ' }, { 'ā', 'ā' }, { 'đ', 'đ' }, { 'ē', 'ē' }, { 'ě', 'ě' }, { 'Ħ', 'ħ' }, { 'ī', 'ī' }, { 'ı', 'ĳ' }, { 'ĸ', 'ĸ' }, { 'Ŀ', 'ł' }, { 'ń', 'ń' }, { 'ň', 'ŋ' }, { 'ō', 'ō' }, { 'Œ', 'œ' }, { 'Ŧ', 'ŧ' }, { 'ū', 'ū' }, { 'ǎ', 'ǎ' }, { 'ǐ', 'ǐ' }, { 'ǒ', 'ǒ' }, { 'ǔ', 'ǔ' }, { 'ǖ', 'ǖ' }, { 'ǘ', 'ǘ' }, { 'ǚ', 'ǚ' }, { 'ǜ', 'ǜ' }, { 'ɑ', 'ɑ' }, { 'ɡ', 'ɡ' }, { '˄', '˄' }, { 'ˇ', 'ˇ' }, { 'ˉ', 'ˋ' }, { 'ˍ', 'ˍ' }, { 'ː', 'ː' }, { '˘', '˛' }, { '˝', '˝' }, { '˟', '˟' }, { 'Α', 'Ρ' }, { 'Σ', 'Ω' }, { 'α', 'ρ' }, { 'σ', 'ω' }, { 'Ё', 'Ё' }, { 'А', 'я' }, { 'ё', 'ё' }, { '‐', '‐' }, { '–', '‖' }, { '‘', '’' }, { '“', '”' }, { '†', '•' }, { '․', '‧' }, { '‰', '‰' }, { '′', '″' }, { '‵', '‵' }, { '※', '※' }, { '‾', '‾' }, { '⁴', '⁴' }, { 'ⁿ', 'ⁿ' }, { '₁', '₄' }, { '€', '€' }, { '℃', '℃' }, { '℅', '℅' }, { '℉', '℉' }, { 'ℓ', 'ℓ' }, { '№', '№' }, { '℡', '™' }, { 'Ω', 'Ω' }, { 'Å', 'Å' }, { '⅓', '⅔' }, { '⅛', '⅞' }, { 'Ⅰ', 'Ⅻ' }, { 'ⅰ', 'ⅹ' }, { '←', '↙' }, { '↸', '↹' }, { '⇒', '⇒' }, { '⇔', '⇔' }, { '⇧', '⇧' }, { '∀', '∀' }, { '∂', '∃' }, { '∇', '∈' }, { '∋', '∋' }, { '∏', '∏' }, { '∑', '∑' }, { '∕', '∕' }, { '√', '√' }, { '∝', '∠' }, { '∣', '∣' }, { '∥', '∥' }, { '∧', '∬' }, { '∮', '∮' }, { '∴', '∷' }, { '∼', '∽' }, { '≈', '≈' }, { '≌', '≌' }, { '≒', '≒' }, { '≠', '≡' }, { '≤', '≧' }, { '≪', '≫' }, { '≮', '≯' }, { '⊂', '⊃' }, { '⊆', '⊇' }, { '⊕', '⊕' }, { '⊙', '⊙' }, { '⊥', '⊥' }, { '⊿', '⊿' }, { '⌒', '⌒' }, { '①', 'ⓩ' }, { '⓫', '╋' }, { '═', '╳' }, { '▀', '▏' }, { '▒', '▕' }, { '■', '□' }, { '▣', '▩' }, { '▲', '△' }, { '▶', '▷' }, { '▼', '▽' }, { '◀', '◁' }, { '◆', '◈' }, { '○', '○' }, { '◎', '◑' }, { '◢', '◥' }, { '◯', '◯' }, { '★', '☆' }, { '☉', '☉' }, { '☎', '☏' }, { '☔', '☕' }, { '☜', '☜' }, { '☞', '☞' }, { '♀', '♀' }, { '♂', '♂' }, { '♠', '♡' }, { '♣', '♥' }, { '♧', '♪' }, { '♬', '♭' }, { '♯', '♯' }, { '✽', '✽' }, { '❶', '❿' }, { '', '' }, { '�', '�' } };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int bisearch(char ucs, char[][] table, int max) {
/* 246 */     int min = 0;
/*     */ 
/*     */     
/* 249 */     if (ucs < table[0][0] || ucs > table[max][1])
/* 250 */       return 0; 
/* 251 */     while (max >= min) {
/* 252 */       int mid = (min + max) / 2;
/* 253 */       if (ucs > table[mid][1]) {
/* 254 */         min = mid + 1; continue;
/* 255 */       }  if (ucs < table[mid][0]) {
/* 256 */         max = mid - 1; continue;
/*     */       } 
/* 258 */       return 1;
/*     */     } 
/*     */     
/* 261 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int mk_wcwidth(int ucs, boolean ambiguousIsDoubleWidth) {
/* 269 */     if (ucs == 0)
/* 270 */       return 0; 
/* 271 */     if (ucs < 32 || (ucs >= 127 && ucs < 160)) {
/* 272 */       return -1;
/*     */     }
/* 274 */     if (ambiguousIsDoubleWidth && 
/* 275 */       bisearch((char)ucs, AMBIGUOUS, AMBIGUOUS.length - 1) > 0) {
/* 276 */       return 2;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 282 */     if (bisearch((char)ucs, COMBINING, COMBINING.length - 1) > 0) {
/* 283 */       return 0;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 288 */     return 1 + ((ucs >= 4352 && (ucs <= 4447 || ucs == 9001 || ucs == 9002 || (ucs >= 11904 && ucs <= 42191 && ucs != 12351) || (ucs >= 44032 && ucs <= 55203) || (ucs >= 63744 && ucs <= 64255) || (ucs >= 65040 && ucs <= 65049) || (ucs >= 65072 && ucs <= 65135) || (ucs >= 65280 && ucs <= 65376) || (ucs >= 65504 && ucs <= 65510) || (ucs >= 131072 && ucs <= 196605) || (ucs >= 196608 && ucs <= 262141))) ? 1 : 0);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\termina\\util\CharUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */