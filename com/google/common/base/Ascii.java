/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public final class Ascii
/*     */ {
/*     */   public static final byte NUL = 0;
/*     */   public static final byte SOH = 1;
/*     */   public static final byte STX = 2;
/*     */   public static final byte ETX = 3;
/*     */   public static final byte EOT = 4;
/*     */   public static final byte ENQ = 5;
/*     */   public static final byte ACK = 6;
/*     */   public static final byte BEL = 7;
/*     */   public static final byte BS = 8;
/*     */   public static final byte HT = 9;
/*     */   public static final byte LF = 10;
/*     */   public static final byte NL = 10;
/*     */   public static final byte VT = 11;
/*     */   public static final byte FF = 12;
/*     */   public static final byte CR = 13;
/*     */   public static final byte SO = 14;
/*     */   public static final byte SI = 15;
/*     */   public static final byte DLE = 16;
/*     */   public static final byte DC1 = 17;
/*     */   public static final byte XON = 17;
/*     */   public static final byte DC2 = 18;
/*     */   public static final byte DC3 = 19;
/*     */   public static final byte XOFF = 19;
/*     */   public static final byte DC4 = 20;
/*     */   public static final byte NAK = 21;
/*     */   public static final byte SYN = 22;
/*     */   public static final byte ETB = 23;
/*     */   public static final byte CAN = 24;
/*     */   public static final byte EM = 25;
/*     */   public static final byte SUB = 26;
/*     */   public static final byte ESC = 27;
/*     */   public static final byte FS = 28;
/*     */   public static final byte GS = 29;
/*     */   public static final byte RS = 30;
/*     */   public static final byte US = 31;
/*     */   public static final byte SP = 32;
/*     */   public static final byte SPACE = 32;
/*     */   public static final byte DEL = 127;
/*     */   public static final char MIN = '\000';
/*     */   public static final char MAX = '';
/*     */   private static final char CASE_MASK = ' ';
/*     */   
/*     */   public static String toLowerCase(String string) {
/* 407 */     int length = string.length();
/* 408 */     for (int i = 0; i < length; i++) {
/* 409 */       if (isUpperCase(string.charAt(i))) {
/* 410 */         char[] chars = string.toCharArray();
/* 411 */         for (; i < length; i++) {
/* 412 */           char c = chars[i];
/* 413 */           if (isUpperCase(c)) {
/* 414 */             chars[i] = (char)(c ^ 0x20);
/*     */           }
/*     */         } 
/* 417 */         return String.valueOf(chars);
/*     */       } 
/*     */     } 
/* 420 */     return string;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toLowerCase(CharSequence chars) {
/* 431 */     if (chars instanceof String) {
/* 432 */       return toLowerCase((String)chars);
/*     */     }
/* 434 */     char[] newChars = new char[chars.length()];
/* 435 */     for (int i = 0; i < newChars.length; i++) {
/* 436 */       newChars[i] = toLowerCase(chars.charAt(i));
/*     */     }
/* 438 */     return String.valueOf(newChars);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char toLowerCase(char c) {
/* 446 */     return isUpperCase(c) ? (char)(c ^ 0x20) : c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toUpperCase(String string) {
/* 455 */     int length = string.length();
/* 456 */     for (int i = 0; i < length; i++) {
/* 457 */       if (isLowerCase(string.charAt(i))) {
/* 458 */         char[] chars = string.toCharArray();
/* 459 */         for (; i < length; i++) {
/* 460 */           char c = chars[i];
/* 461 */           if (isLowerCase(c)) {
/* 462 */             chars[i] = (char)(c ^ 0x20);
/*     */           }
/*     */         } 
/* 465 */         return String.valueOf(chars);
/*     */       } 
/*     */     } 
/* 468 */     return string;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toUpperCase(CharSequence chars) {
/* 479 */     if (chars instanceof String) {
/* 480 */       return toUpperCase((String)chars);
/*     */     }
/* 482 */     char[] newChars = new char[chars.length()];
/* 483 */     for (int i = 0; i < newChars.length; i++) {
/* 484 */       newChars[i] = toUpperCase(chars.charAt(i));
/*     */     }
/* 486 */     return String.valueOf(newChars);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char toUpperCase(char c) {
/* 494 */     return isLowerCase(c) ? (char)(c ^ 0x20) : c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isLowerCase(char c) {
/* 505 */     return (c >= 'a' && c <= 'z');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isUpperCase(char c) {
/* 514 */     return (c >= 'A' && c <= 'Z');
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String truncate(CharSequence seq, int maxLength, String truncationIndicator) {
/* 551 */     Preconditions.checkNotNull(seq);
/*     */ 
/*     */     
/* 554 */     int truncationLength = maxLength - truncationIndicator.length();
/*     */ 
/*     */ 
/*     */     
/* 558 */     Preconditions.checkArgument((truncationLength >= 0), "maxLength (%s) must be >= length of the truncation indicator (%s)", maxLength, truncationIndicator
/*     */ 
/*     */ 
/*     */         
/* 562 */         .length());
/*     */     
/* 564 */     if (seq.length() <= maxLength) {
/* 565 */       String string = seq.toString();
/* 566 */       if (string.length() <= maxLength) {
/* 567 */         return string;
/*     */       }
/*     */       
/* 570 */       seq = string;
/*     */     } 
/*     */     
/* 573 */     return (new StringBuilder(maxLength))
/* 574 */       .append(seq, 0, truncationLength)
/* 575 */       .append(truncationIndicator)
/* 576 */       .toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean equalsIgnoreCase(CharSequence s1, CharSequence s2) {
/* 603 */     int length = s1.length();
/* 604 */     if (s1 == s2) {
/* 605 */       return true;
/*     */     }
/* 607 */     if (length != s2.length()) {
/* 608 */       return false;
/*     */     }
/* 610 */     for (int i = 0; i < length; i++) {
/* 611 */       char c1 = s1.charAt(i);
/* 612 */       char c2 = s2.charAt(i);
/* 613 */       if (c1 != c2) {
/*     */ 
/*     */         
/* 616 */         int alphaIndex = getAlphaIndex(c1);
/*     */ 
/*     */         
/* 619 */         if (alphaIndex >= 26 || alphaIndex != getAlphaIndex(c2))
/*     */         {
/*     */           
/* 622 */           return false; } 
/*     */       } 
/* 624 */     }  return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getAlphaIndex(char c) {
/* 633 */     return (char)((c | 0x20) - 97);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\Ascii.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */