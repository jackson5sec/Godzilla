/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Strings
/*     */ {
/*     */   public static String nullToEmpty(String string) {
/*  43 */     return Platform.nullToEmpty(string);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String emptyToNull(String string) {
/*  53 */     return Platform.emptyToNull(string);
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
/*     */   public static boolean isNullOrEmpty(String string) {
/*  68 */     return Platform.stringIsNullOrEmpty(string);
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
/*     */   public static String padStart(String string, int minLength, char padChar) {
/*  90 */     Preconditions.checkNotNull(string);
/*  91 */     if (string.length() >= minLength) {
/*  92 */       return string;
/*     */     }
/*  94 */     StringBuilder sb = new StringBuilder(minLength);
/*  95 */     for (int i = string.length(); i < minLength; i++) {
/*  96 */       sb.append(padChar);
/*     */     }
/*  98 */     sb.append(string);
/*  99 */     return sb.toString();
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
/*     */   public static String padEnd(String string, int minLength, char padChar) {
/* 121 */     Preconditions.checkNotNull(string);
/* 122 */     if (string.length() >= minLength) {
/* 123 */       return string;
/*     */     }
/* 125 */     StringBuilder sb = new StringBuilder(minLength);
/* 126 */     sb.append(string);
/* 127 */     for (int i = string.length(); i < minLength; i++) {
/* 128 */       sb.append(padChar);
/*     */     }
/* 130 */     return sb.toString();
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
/*     */   public static String repeat(String string, int count) {
/* 144 */     Preconditions.checkNotNull(string);
/*     */     
/* 146 */     if (count <= 1) {
/* 147 */       Preconditions.checkArgument((count >= 0), "invalid count: %s", count);
/* 148 */       return (count == 0) ? "" : string;
/*     */     } 
/*     */ 
/*     */     
/* 152 */     int len = string.length();
/* 153 */     long longSize = len * count;
/* 154 */     int size = (int)longSize;
/* 155 */     if (size != longSize) {
/* 156 */       throw new ArrayIndexOutOfBoundsException("Required array size too large: " + longSize);
/*     */     }
/*     */     
/* 159 */     char[] array = new char[size];
/* 160 */     string.getChars(0, len, array, 0);
/*     */     int n;
/* 162 */     for (n = len; n < size - n; n <<= 1) {
/* 163 */       System.arraycopy(array, 0, array, n, n);
/*     */     }
/* 165 */     System.arraycopy(array, 0, array, n, size - n);
/* 166 */     return new String(array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String commonPrefix(CharSequence a, CharSequence b) {
/* 177 */     Preconditions.checkNotNull(a);
/* 178 */     Preconditions.checkNotNull(b);
/*     */     
/* 180 */     int maxPrefixLength = Math.min(a.length(), b.length());
/* 181 */     int p = 0;
/* 182 */     while (p < maxPrefixLength && a.charAt(p) == b.charAt(p)) {
/* 183 */       p++;
/*     */     }
/* 185 */     if (validSurrogatePairAt(a, p - 1) || validSurrogatePairAt(b, p - 1)) {
/* 186 */       p--;
/*     */     }
/* 188 */     return a.subSequence(0, p).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String commonSuffix(CharSequence a, CharSequence b) {
/* 199 */     Preconditions.checkNotNull(a);
/* 200 */     Preconditions.checkNotNull(b);
/*     */     
/* 202 */     int maxSuffixLength = Math.min(a.length(), b.length());
/* 203 */     int s = 0;
/* 204 */     while (s < maxSuffixLength && a.charAt(a.length() - s - 1) == b.charAt(b.length() - s - 1)) {
/* 205 */       s++;
/*     */     }
/* 207 */     if (validSurrogatePairAt(a, a.length() - s - 1) || 
/* 208 */       validSurrogatePairAt(b, b.length() - s - 1)) {
/* 209 */       s--;
/*     */     }
/* 211 */     return a.subSequence(a.length() - s, a.length()).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static boolean validSurrogatePairAt(CharSequence string, int index) {
/* 220 */     return (index >= 0 && index <= string
/* 221 */       .length() - 2 && 
/* 222 */       Character.isHighSurrogate(string.charAt(index)) && 
/* 223 */       Character.isLowSurrogate(string.charAt(index + 1)));
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
/*     */   
/*     */   public static String lenientFormat(String template, Object... args) {
/* 261 */     template = String.valueOf(template);
/*     */     
/* 263 */     if (args == null) {
/* 264 */       args = new Object[] { "(Object[])null" };
/*     */     } else {
/* 266 */       for (int j = 0; j < args.length; j++) {
/* 267 */         args[j] = lenientToString(args[j]);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 272 */     StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
/* 273 */     int templateStart = 0;
/* 274 */     int i = 0;
/* 275 */     while (i < args.length) {
/* 276 */       int placeholderStart = template.indexOf("%s", templateStart);
/* 277 */       if (placeholderStart == -1) {
/*     */         break;
/*     */       }
/* 280 */       builder.append(template, templateStart, placeholderStart);
/* 281 */       builder.append(args[i++]);
/* 282 */       templateStart = placeholderStart + 2;
/*     */     } 
/* 284 */     builder.append(template, templateStart, template.length());
/*     */ 
/*     */     
/* 287 */     if (i < args.length) {
/* 288 */       builder.append(" [");
/* 289 */       builder.append(args[i++]);
/* 290 */       while (i < args.length) {
/* 291 */         builder.append(", ");
/* 292 */         builder.append(args[i++]);
/*     */       } 
/* 294 */       builder.append(']');
/*     */     } 
/*     */     
/* 297 */     return builder.toString();
/*     */   }
/*     */   
/*     */   private static String lenientToString(Object o) {
/*     */     try {
/* 302 */       return String.valueOf(o);
/* 303 */     } catch (Exception e) {
/*     */ 
/*     */       
/* 306 */       String objectToString = o.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(o));
/*     */       
/* 308 */       Logger.getLogger("com.google.common.base.Strings")
/* 309 */         .log(Level.WARNING, "Exception during lenientFormat for " + objectToString, e);
/* 310 */       return "<" + objectToString + " threw " + e.getClass().getName() + ">";
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\Strings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */