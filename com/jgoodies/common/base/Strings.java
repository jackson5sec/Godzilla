/*     */ package com.jgoodies.common.base;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Strings
/*     */ {
/*     */   public static final String NO_ELLIPSIS_STRING = "...";
/*     */   public static final String ELLIPSIS_STRING = "…";
/*     */   
/*     */   public static boolean isBlank(String str) {
/*     */     int length;
/*  85 */     if (str == null || (length = str.length()) == 0) {
/*  86 */       return true;
/*     */     }
/*  88 */     for (int i = length - 1; i >= 0; i--) {
/*  89 */       if (!Character.isWhitespace(str.charAt(i))) {
/*  90 */         return false;
/*     */       }
/*     */     } 
/*  93 */     return true;
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
/*     */   public static boolean isNotBlank(String str) {
/*     */     int length;
/* 118 */     if (str == null || (length = str.length()) == 0) {
/* 119 */       return false;
/*     */     }
/* 121 */     for (int i = length - 1; i >= 0; i--) {
/* 122 */       if (!Character.isWhitespace(str.charAt(i))) {
/* 123 */         return true;
/*     */       }
/*     */     } 
/* 126 */     return false;
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
/*     */   public static boolean isEmpty(String str) {
/* 146 */     return (str == null || str.length() == 0);
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
/*     */   public static boolean isNotEmpty(String str) {
/* 168 */     return (str != null && str.length() > 0);
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
/*     */   public static boolean isTrimmed(String str) {
/*     */     int length;
/* 193 */     if (str == null || (length = str.length()) == 0) {
/* 194 */       return true;
/*     */     }
/* 196 */     return (!Character.isWhitespace(str.charAt(0)) && !Character.isWhitespace(str.charAt(length - 1)));
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
/*     */   public static boolean startsWithIgnoreCase(String str, String prefix) {
/* 231 */     if (str == null) {
/* 232 */       return (prefix == null);
/*     */     }
/* 234 */     if (prefix == null) {
/* 235 */       return false;
/*     */     }
/* 237 */     return str.regionMatches(true, 0, prefix, 0, prefix.length());
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
/*     */   public static String abbreviateCenter(String str, int maxLength) {
/* 268 */     if (str == null) {
/* 269 */       return null;
/*     */     }
/* 271 */     int length = str.length();
/* 272 */     if (length <= maxLength) {
/* 273 */       return str;
/*     */     }
/* 275 */     int headLength = maxLength / 2;
/* 276 */     int tailLength = maxLength - headLength - 1;
/* 277 */     String head = str.substring(0, headLength);
/* 278 */     String tail = str.substring(length - tailLength, length);
/* 279 */     return head + "…" + tail;
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
/*     */   public static String get(String str, Object... args) {
/* 305 */     return (args == null || args.length == 0) ? str : String.format(str, args);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\common\base\Strings.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */