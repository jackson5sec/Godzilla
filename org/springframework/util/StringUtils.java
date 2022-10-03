/*      */ package org.springframework.util;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.ArrayDeque;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Deque;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.StringJoiner;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.TimeZone;
/*      */ import org.springframework.lang.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class StringUtils
/*      */ {
/*   64 */   private static final String[] EMPTY_STRING_ARRAY = new String[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String FOLDER_SEPARATOR = "/";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String WINDOWS_FOLDER_SEPARATOR = "\\";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String TOP_PATH = "..";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final String CURRENT_PATH = ".";
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final char EXTENSION_SEPARATOR = '.';
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static boolean isEmpty(@Nullable Object str) {
/*   99 */     return (str == null || "".equals(str));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean hasLength(@Nullable CharSequence str) {
/*  119 */     return (str != null && str.length() > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean hasLength(@Nullable String str) {
/*  132 */     return (str != null && !str.isEmpty());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean hasText(@Nullable CharSequence str) {
/*  155 */     return (str != null && str.length() > 0 && containsText(str));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean hasText(@Nullable String str) {
/*  171 */     return (str != null && !str.isEmpty() && containsText(str));
/*      */   }
/*      */   
/*      */   private static boolean containsText(CharSequence str) {
/*  175 */     int strLen = str.length();
/*  176 */     for (int i = 0; i < strLen; i++) {
/*  177 */       if (!Character.isWhitespace(str.charAt(i))) {
/*  178 */         return true;
/*      */       }
/*      */     } 
/*  181 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean containsWhitespace(@Nullable CharSequence str) {
/*  192 */     if (!hasLength(str)) {
/*  193 */       return false;
/*      */     }
/*      */     
/*  196 */     int strLen = str.length();
/*  197 */     for (int i = 0; i < strLen; i++) {
/*  198 */       if (Character.isWhitespace(str.charAt(i))) {
/*  199 */         return true;
/*      */       }
/*      */     } 
/*  202 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean containsWhitespace(@Nullable String str) {
/*  213 */     return containsWhitespace(str);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String trimWhitespace(String str) {
/*  223 */     if (!hasLength(str)) {
/*  224 */       return str;
/*      */     }
/*      */     
/*  227 */     int beginIndex = 0;
/*  228 */     int endIndex = str.length() - 1;
/*      */     
/*  230 */     while (beginIndex <= endIndex && Character.isWhitespace(str.charAt(beginIndex))) {
/*  231 */       beginIndex++;
/*      */     }
/*      */     
/*  234 */     while (endIndex > beginIndex && Character.isWhitespace(str.charAt(endIndex))) {
/*  235 */       endIndex--;
/*      */     }
/*      */     
/*  238 */     return str.substring(beginIndex, endIndex + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String trimAllWhitespace(String str) {
/*  249 */     if (!hasLength(str)) {
/*  250 */       return str;
/*      */     }
/*      */     
/*  253 */     int len = str.length();
/*  254 */     StringBuilder sb = new StringBuilder(str.length());
/*  255 */     for (int i = 0; i < len; i++) {
/*  256 */       char c = str.charAt(i);
/*  257 */       if (!Character.isWhitespace(c)) {
/*  258 */         sb.append(c);
/*      */       }
/*      */     } 
/*  261 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String trimLeadingWhitespace(String str) {
/*  271 */     if (!hasLength(str)) {
/*  272 */       return str;
/*      */     }
/*      */     
/*  275 */     int beginIdx = 0;
/*  276 */     while (beginIdx < str.length() && Character.isWhitespace(str.charAt(beginIdx))) {
/*  277 */       beginIdx++;
/*      */     }
/*  279 */     return str.substring(beginIdx);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String trimTrailingWhitespace(String str) {
/*  289 */     if (!hasLength(str)) {
/*  290 */       return str;
/*      */     }
/*      */     
/*  293 */     int endIdx = str.length() - 1;
/*  294 */     while (endIdx >= 0 && Character.isWhitespace(str.charAt(endIdx))) {
/*  295 */       endIdx--;
/*      */     }
/*  297 */     return str.substring(0, endIdx + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String trimLeadingCharacter(String str, char leadingCharacter) {
/*  307 */     if (!hasLength(str)) {
/*  308 */       return str;
/*      */     }
/*      */     
/*  311 */     int beginIdx = 0;
/*  312 */     while (beginIdx < str.length() && leadingCharacter == str.charAt(beginIdx)) {
/*  313 */       beginIdx++;
/*      */     }
/*  315 */     return str.substring(beginIdx);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String trimTrailingCharacter(String str, char trailingCharacter) {
/*  325 */     if (!hasLength(str)) {
/*  326 */       return str;
/*      */     }
/*      */     
/*  329 */     int endIdx = str.length() - 1;
/*  330 */     while (endIdx >= 0 && trailingCharacter == str.charAt(endIdx)) {
/*  331 */       endIdx--;
/*      */     }
/*  333 */     return str.substring(0, endIdx + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean matchesCharacter(@Nullable String str, char singleCharacter) {
/*  343 */     return (str != null && str.length() == 1 && str.charAt(0) == singleCharacter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean startsWithIgnoreCase(@Nullable String str, @Nullable String prefix) {
/*  354 */     return (str != null && prefix != null && str.length() >= prefix.length() && str
/*  355 */       .regionMatches(true, 0, prefix, 0, prefix.length()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean endsWithIgnoreCase(@Nullable String str, @Nullable String suffix) {
/*  366 */     return (str != null && suffix != null && str.length() >= suffix.length() && str
/*  367 */       .regionMatches(true, str.length() - suffix.length(), suffix, 0, suffix.length()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
/*  378 */     if (index + substring.length() > str.length()) {
/*  379 */       return false;
/*      */     }
/*  381 */     for (int i = 0; i < substring.length(); i++) {
/*  382 */       if (str.charAt(index + i) != substring.charAt(i)) {
/*  383 */         return false;
/*      */       }
/*      */     } 
/*  386 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int countOccurrencesOf(String str, String sub) {
/*  395 */     if (!hasLength(str) || !hasLength(sub)) {
/*  396 */       return 0;
/*      */     }
/*      */     
/*  399 */     int count = 0;
/*  400 */     int pos = 0;
/*      */     int idx;
/*  402 */     while ((idx = str.indexOf(sub, pos)) != -1) {
/*  403 */       count++;
/*  404 */       pos = idx + sub.length();
/*      */     } 
/*  406 */     return count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String replace(String inString, String oldPattern, @Nullable String newPattern) {
/*  417 */     if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
/*  418 */       return inString;
/*      */     }
/*  420 */     int index = inString.indexOf(oldPattern);
/*  421 */     if (index == -1)
/*      */     {
/*  423 */       return inString;
/*      */     }
/*      */     
/*  426 */     int capacity = inString.length();
/*  427 */     if (newPattern.length() > oldPattern.length()) {
/*  428 */       capacity += 16;
/*      */     }
/*  430 */     StringBuilder sb = new StringBuilder(capacity);
/*      */     
/*  432 */     int pos = 0;
/*  433 */     int patLen = oldPattern.length();
/*  434 */     while (index >= 0) {
/*  435 */       sb.append(inString, pos, index);
/*  436 */       sb.append(newPattern);
/*  437 */       pos = index + patLen;
/*  438 */       index = inString.indexOf(oldPattern, pos);
/*      */     } 
/*      */ 
/*      */     
/*  442 */     sb.append(inString, pos, inString.length());
/*  443 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String delete(String inString, String pattern) {
/*  453 */     return replace(inString, pattern, "");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String deleteAny(String inString, @Nullable String charsToDelete) {
/*  464 */     if (!hasLength(inString) || !hasLength(charsToDelete)) {
/*  465 */       return inString;
/*      */     }
/*      */     
/*  468 */     int lastCharIndex = 0;
/*  469 */     char[] result = new char[inString.length()];
/*  470 */     for (int i = 0; i < inString.length(); i++) {
/*  471 */       char c = inString.charAt(i);
/*  472 */       if (charsToDelete.indexOf(c) == -1) {
/*  473 */         result[lastCharIndex++] = c;
/*      */       }
/*      */     } 
/*  476 */     if (lastCharIndex == inString.length()) {
/*  477 */       return inString;
/*      */     }
/*  479 */     return new String(result, 0, lastCharIndex);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static String quote(@Nullable String str) {
/*  494 */     return (str != null) ? ("'" + str + "'") : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Object quoteIfString(@Nullable Object obj) {
/*  506 */     return (obj instanceof String) ? quote((String)obj) : obj;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String unqualify(String qualifiedName) {
/*  515 */     return unqualify(qualifiedName, '.');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String unqualify(String qualifiedName, char separator) {
/*  525 */     return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String capitalize(String str) {
/*  536 */     return changeFirstCharacterCase(str, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String uncapitalize(String str) {
/*  547 */     return changeFirstCharacterCase(str, false);
/*      */   }
/*      */   private static String changeFirstCharacterCase(String str, boolean capitalize) {
/*      */     char updatedChar;
/*  551 */     if (!hasLength(str)) {
/*  552 */       return str;
/*      */     }
/*      */     
/*  555 */     char baseChar = str.charAt(0);
/*      */     
/*  557 */     if (capitalize) {
/*  558 */       updatedChar = Character.toUpperCase(baseChar);
/*      */     } else {
/*      */       
/*  561 */       updatedChar = Character.toLowerCase(baseChar);
/*      */     } 
/*  563 */     if (baseChar == updatedChar) {
/*  564 */       return str;
/*      */     }
/*      */     
/*  567 */     char[] chars = str.toCharArray();
/*  568 */     chars[0] = updatedChar;
/*  569 */     return new String(chars);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static String getFilename(@Nullable String path) {
/*  580 */     if (path == null) {
/*  581 */       return null;
/*      */     }
/*      */     
/*  584 */     int separatorIndex = path.lastIndexOf("/");
/*  585 */     return (separatorIndex != -1) ? path.substring(separatorIndex + 1) : path;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static String getFilenameExtension(@Nullable String path) {
/*  596 */     if (path == null) {
/*  597 */       return null;
/*      */     }
/*      */     
/*  600 */     int extIndex = path.lastIndexOf('.');
/*  601 */     if (extIndex == -1) {
/*  602 */       return null;
/*      */     }
/*      */     
/*  605 */     int folderIndex = path.lastIndexOf("/");
/*  606 */     if (folderIndex > extIndex) {
/*  607 */       return null;
/*      */     }
/*      */     
/*  610 */     return path.substring(extIndex + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String stripFilenameExtension(String path) {
/*  620 */     int extIndex = path.lastIndexOf('.');
/*  621 */     if (extIndex == -1) {
/*  622 */       return path;
/*      */     }
/*      */     
/*  625 */     int folderIndex = path.lastIndexOf("/");
/*  626 */     if (folderIndex > extIndex) {
/*  627 */       return path;
/*      */     }
/*      */     
/*  630 */     return path.substring(0, extIndex);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String applyRelativePath(String path, String relativePath) {
/*  642 */     int separatorIndex = path.lastIndexOf("/");
/*  643 */     if (separatorIndex != -1) {
/*  644 */       String newPath = path.substring(0, separatorIndex);
/*  645 */       if (!relativePath.startsWith("/")) {
/*  646 */         newPath = newPath + "/";
/*      */       }
/*  648 */       return newPath + relativePath;
/*      */     } 
/*      */     
/*  651 */     return relativePath;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String cleanPath(String path) {
/*  667 */     if (!hasLength(path)) {
/*  668 */       return path;
/*      */     }
/*      */     
/*  671 */     String normalizedPath = replace(path, "\\", "/");
/*  672 */     String pathToUse = normalizedPath;
/*      */ 
/*      */     
/*  675 */     if (pathToUse.indexOf('.') == -1) {
/*  676 */       return pathToUse;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  683 */     int prefixIndex = pathToUse.indexOf(':');
/*  684 */     String prefix = "";
/*  685 */     if (prefixIndex != -1) {
/*  686 */       prefix = pathToUse.substring(0, prefixIndex + 1);
/*  687 */       if (prefix.contains("/")) {
/*  688 */         prefix = "";
/*      */       } else {
/*      */         
/*  691 */         pathToUse = pathToUse.substring(prefixIndex + 1);
/*      */       } 
/*      */     } 
/*  694 */     if (pathToUse.startsWith("/")) {
/*  695 */       prefix = prefix + "/";
/*  696 */       pathToUse = pathToUse.substring(1);
/*      */     } 
/*      */     
/*  699 */     String[] pathArray = delimitedListToStringArray(pathToUse, "/");
/*      */     
/*  701 */     Deque<String> pathElements = new ArrayDeque<>(pathArray.length);
/*  702 */     int tops = 0;
/*      */     int i;
/*  704 */     for (i = pathArray.length - 1; i >= 0; i--) {
/*  705 */       String element = pathArray[i];
/*  706 */       if (!".".equals(element))
/*      */       {
/*      */         
/*  709 */         if ("..".equals(element)) {
/*      */           
/*  711 */           tops++;
/*      */         
/*      */         }
/*  714 */         else if (tops > 0) {
/*      */           
/*  716 */           tops--;
/*      */         }
/*      */         else {
/*      */           
/*  720 */           pathElements.addFirst(element);
/*      */         } 
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  726 */     if (pathArray.length == pathElements.size()) {
/*  727 */       return normalizedPath;
/*      */     }
/*      */     
/*  730 */     for (i = 0; i < tops; i++) {
/*  731 */       pathElements.addFirst("..");
/*      */     }
/*      */     
/*  734 */     if (pathElements.size() == 1 && ((String)pathElements.getLast()).isEmpty() && !prefix.endsWith("/")) {
/*  735 */       pathElements.addFirst(".");
/*      */     }
/*      */     
/*  738 */     String joined = collectionToDelimitedString(pathElements, "/");
/*      */     
/*  740 */     return prefix.isEmpty() ? joined : (prefix + joined);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean pathEquals(String path1, String path2) {
/*  750 */     return cleanPath(path1).equals(cleanPath(path2));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String uriDecode(String source, Charset charset) {
/*  769 */     int length = source.length();
/*  770 */     if (length == 0) {
/*  771 */       return source;
/*      */     }
/*  773 */     Assert.notNull(charset, "Charset must not be null");
/*      */     
/*  775 */     ByteArrayOutputStream baos = new ByteArrayOutputStream(length);
/*  776 */     boolean changed = false;
/*  777 */     for (int i = 0; i < length; i++) {
/*  778 */       int ch = source.charAt(i);
/*  779 */       if (ch == 37) {
/*  780 */         if (i + 2 < length) {
/*  781 */           char hex1 = source.charAt(i + 1);
/*  782 */           char hex2 = source.charAt(i + 2);
/*  783 */           int u = Character.digit(hex1, 16);
/*  784 */           int l = Character.digit(hex2, 16);
/*  785 */           if (u == -1 || l == -1) {
/*  786 */             throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
/*      */           }
/*  788 */           baos.write((char)((u << 4) + l));
/*  789 */           i += 2;
/*  790 */           changed = true;
/*      */         } else {
/*      */           
/*  793 */           throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
/*      */         } 
/*      */       } else {
/*      */         
/*  797 */         baos.write(ch);
/*      */       } 
/*      */     } 
/*  800 */     return changed ? StreamUtils.copyToString(baos, charset) : source;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Locale parseLocale(String localeValue) {
/*  818 */     String[] tokens = tokenizeLocaleSource(localeValue);
/*  819 */     if (tokens.length == 1) {
/*  820 */       validateLocalePart(localeValue);
/*  821 */       Locale resolved = Locale.forLanguageTag(localeValue);
/*  822 */       if (resolved.getLanguage().length() > 0) {
/*  823 */         return resolved;
/*      */       }
/*      */     } 
/*  826 */     return parseLocaleTokens(localeValue, tokens);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Locale parseLocaleString(String localeString) {
/*  845 */     return parseLocaleTokens(localeString, tokenizeLocaleSource(localeString));
/*      */   }
/*      */   
/*      */   private static String[] tokenizeLocaleSource(String localeSource) {
/*  849 */     return tokenizeToStringArray(localeSource, "_ ", false, false);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private static Locale parseLocaleTokens(String localeString, String[] tokens) {
/*  854 */     String language = (tokens.length > 0) ? tokens[0] : "";
/*  855 */     String country = (tokens.length > 1) ? tokens[1] : "";
/*  856 */     validateLocalePart(language);
/*  857 */     validateLocalePart(country);
/*      */     
/*  859 */     String variant = "";
/*  860 */     if (tokens.length > 2) {
/*      */ 
/*      */       
/*  863 */       int endIndexOfCountryCode = localeString.indexOf(country, language.length()) + country.length();
/*      */       
/*  865 */       variant = trimLeadingWhitespace(localeString.substring(endIndexOfCountryCode));
/*  866 */       if (variant.startsWith("_")) {
/*  867 */         variant = trimLeadingCharacter(variant, '_');
/*      */       }
/*      */     } 
/*      */     
/*  871 */     if (variant.isEmpty() && country.startsWith("#")) {
/*  872 */       variant = country;
/*  873 */       country = "";
/*      */     } 
/*      */     
/*  876 */     return (language.length() > 0) ? new Locale(language, country, variant) : null;
/*      */   }
/*      */   
/*      */   private static void validateLocalePart(String localePart) {
/*  880 */     for (int i = 0; i < localePart.length(); i++) {
/*  881 */       char ch = localePart.charAt(i);
/*  882 */       if (ch != ' ' && ch != '_' && ch != '-' && ch != '#' && !Character.isLetterOrDigit(ch)) {
/*  883 */         throw new IllegalArgumentException("Locale part \"" + localePart + "\" contains invalid characters");
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static String toLanguageTag(Locale locale) {
/*  898 */     return locale.getLanguage() + (hasText(locale.getCountry()) ? ("-" + locale.getCountry()) : "");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static TimeZone parseTimeZoneString(String timeZoneString) {
/*  909 */     TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
/*  910 */     if ("GMT".equals(timeZone.getID()) && !timeZoneString.startsWith("GMT"))
/*      */     {
/*  912 */       throw new IllegalArgumentException("Invalid time zone specification '" + timeZoneString + "'");
/*      */     }
/*  914 */     return timeZone;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] toStringArray(@Nullable Collection<String> collection) {
/*  930 */     return !CollectionUtils.isEmpty(collection) ? collection.<String>toArray(EMPTY_STRING_ARRAY) : EMPTY_STRING_ARRAY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] toStringArray(@Nullable Enumeration<String> enumeration) {
/*  941 */     return (enumeration != null) ? toStringArray(Collections.list(enumeration)) : EMPTY_STRING_ARRAY;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] addStringToArray(@Nullable String[] array, String str) {
/*  953 */     if (ObjectUtils.isEmpty((Object[])array)) {
/*  954 */       return new String[] { str };
/*      */     }
/*      */     
/*  957 */     String[] newArr = new String[array.length + 1];
/*  958 */     System.arraycopy(array, 0, newArr, 0, array.length);
/*  959 */     newArr[array.length] = str;
/*  960 */     return newArr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static String[] concatenateStringArrays(@Nullable String[] array1, @Nullable String[] array2) {
/*  973 */     if (ObjectUtils.isEmpty((Object[])array1)) {
/*  974 */       return array2;
/*      */     }
/*  976 */     if (ObjectUtils.isEmpty((Object[])array2)) {
/*  977 */       return array1;
/*      */     }
/*      */     
/*  980 */     String[] newArr = new String[array1.length + array2.length];
/*  981 */     System.arraycopy(array1, 0, newArr, 0, array1.length);
/*  982 */     System.arraycopy(array2, 0, newArr, array1.length, array2.length);
/*  983 */     return newArr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @Nullable
/*      */   public static String[] mergeStringArrays(@Nullable String[] array1, @Nullable String[] array2) {
/* 1001 */     if (ObjectUtils.isEmpty((Object[])array1)) {
/* 1002 */       return array2;
/*      */     }
/* 1004 */     if (ObjectUtils.isEmpty((Object[])array2)) {
/* 1005 */       return array1;
/*      */     }
/*      */     
/* 1008 */     List<String> result = new ArrayList<>(Arrays.asList(array1));
/* 1009 */     for (String str : array2) {
/* 1010 */       if (!result.contains(str)) {
/* 1011 */         result.add(str);
/*      */       }
/*      */     } 
/* 1014 */     return toStringArray(result);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] sortStringArray(String[] array) {
/* 1023 */     if (ObjectUtils.isEmpty((Object[])array)) {
/* 1024 */       return array;
/*      */     }
/*      */     
/* 1027 */     Arrays.sort((Object[])array);
/* 1028 */     return array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] trimArrayElements(String[] array) {
/* 1038 */     if (ObjectUtils.isEmpty((Object[])array)) {
/* 1039 */       return array;
/*      */     }
/*      */     
/* 1042 */     String[] result = new String[array.length];
/* 1043 */     for (int i = 0; i < array.length; i++) {
/* 1044 */       String element = array[i];
/* 1045 */       result[i] = (element != null) ? element.trim() : null;
/*      */     } 
/* 1047 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] removeDuplicateStrings(String[] array) {
/* 1057 */     if (ObjectUtils.isEmpty((Object[])array)) {
/* 1058 */       return array;
/*      */     }
/*      */     
/* 1061 */     Set<String> set = new LinkedHashSet<>(Arrays.asList(array));
/* 1062 */     return toStringArray(set);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static String[] split(@Nullable String toSplit, @Nullable String delimiter) {
/* 1076 */     if (!hasLength(toSplit) || !hasLength(delimiter)) {
/* 1077 */       return null;
/*      */     }
/* 1079 */     int offset = toSplit.indexOf(delimiter);
/* 1080 */     if (offset < 0) {
/* 1081 */       return null;
/*      */     }
/*      */     
/* 1084 */     String beforeDelimiter = toSplit.substring(0, offset);
/* 1085 */     String afterDelimiter = toSplit.substring(offset + delimiter.length());
/* 1086 */     return new String[] { beforeDelimiter, afterDelimiter };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
/* 1101 */     return splitArrayElementsIntoProperties(array, delimiter, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter, @Nullable String charsToDelete) {
/* 1122 */     if (ObjectUtils.isEmpty((Object[])array)) {
/* 1123 */       return null;
/*      */     }
/*      */     
/* 1126 */     Properties result = new Properties();
/* 1127 */     for (String element : array) {
/* 1128 */       if (charsToDelete != null) {
/* 1129 */         element = deleteAny(element, charsToDelete);
/*      */       }
/* 1131 */       String[] splittedElement = split(element, delimiter);
/* 1132 */       if (splittedElement != null)
/*      */       {
/*      */         
/* 1135 */         result.setProperty(splittedElement[0].trim(), splittedElement[1].trim()); } 
/*      */     } 
/* 1137 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] tokenizeToStringArray(@Nullable String str, String delimiters) {
/* 1157 */     return tokenizeToStringArray(str, delimiters, true, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] tokenizeToStringArray(@Nullable String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
/* 1182 */     if (str == null) {
/* 1183 */       return EMPTY_STRING_ARRAY;
/*      */     }
/*      */     
/* 1186 */     StringTokenizer st = new StringTokenizer(str, delimiters);
/* 1187 */     List<String> tokens = new ArrayList<>();
/* 1188 */     while (st.hasMoreTokens()) {
/* 1189 */       String token = st.nextToken();
/* 1190 */       if (trimTokens) {
/* 1191 */         token = token.trim();
/*      */       }
/* 1193 */       if (!ignoreEmptyTokens || token.length() > 0) {
/* 1194 */         tokens.add(token);
/*      */       }
/*      */     } 
/* 1197 */     return toStringArray(tokens);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] delimitedListToStringArray(@Nullable String str, @Nullable String delimiter) {
/* 1214 */     return delimitedListToStringArray(str, delimiter, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] delimitedListToStringArray(@Nullable String str, @Nullable String delimiter, @Nullable String charsToDelete) {
/* 1235 */     if (str == null) {
/* 1236 */       return EMPTY_STRING_ARRAY;
/*      */     }
/* 1238 */     if (delimiter == null) {
/* 1239 */       return new String[] { str };
/*      */     }
/*      */     
/* 1242 */     List<String> result = new ArrayList<>();
/* 1243 */     if (delimiter.isEmpty()) {
/* 1244 */       for (int i = 0; i < str.length(); i++) {
/* 1245 */         result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
/*      */       }
/*      */     } else {
/*      */       
/* 1249 */       int pos = 0;
/*      */       int delPos;
/* 1251 */       while ((delPos = str.indexOf(delimiter, pos)) != -1) {
/* 1252 */         result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
/* 1253 */         pos = delPos + delimiter.length();
/*      */       } 
/* 1255 */       if (str.length() > 0 && pos <= str.length())
/*      */       {
/* 1257 */         result.add(deleteAny(str.substring(pos), charsToDelete));
/*      */       }
/*      */     } 
/* 1260 */     return toStringArray(result);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String[] commaDelimitedListToStringArray(@Nullable String str) {
/* 1270 */     return delimitedListToStringArray(str, ",");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Set<String> commaDelimitedListToSet(@Nullable String str) {
/* 1282 */     String[] tokens = commaDelimitedListToStringArray(str);
/* 1283 */     return new LinkedHashSet<>(Arrays.asList(tokens));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String collectionToDelimitedString(@Nullable Collection<?> coll, String delim, String prefix, String suffix) {
/* 1298 */     if (CollectionUtils.isEmpty(coll)) {
/* 1299 */       return "";
/*      */     }
/*      */     
/* 1302 */     int totalLength = coll.size() * (prefix.length() + suffix.length()) + (coll.size() - 1) * delim.length();
/* 1303 */     for (Object element : coll) {
/* 1304 */       totalLength += String.valueOf(element).length();
/*      */     }
/*      */     
/* 1307 */     StringBuilder sb = new StringBuilder(totalLength);
/* 1308 */     Iterator<?> it = coll.iterator();
/* 1309 */     while (it.hasNext()) {
/* 1310 */       sb.append(prefix).append(it.next()).append(suffix);
/* 1311 */       if (it.hasNext()) {
/* 1312 */         sb.append(delim);
/*      */       }
/*      */     } 
/* 1315 */     return sb.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String collectionToDelimitedString(@Nullable Collection<?> coll, String delim) {
/* 1326 */     return collectionToDelimitedString(coll, delim, "", "");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String collectionToCommaDelimitedString(@Nullable Collection<?> coll) {
/* 1336 */     return collectionToDelimitedString(coll, ",");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String arrayToDelimitedString(@Nullable Object[] arr, String delim) {
/* 1347 */     if (ObjectUtils.isEmpty(arr)) {
/* 1348 */       return "";
/*      */     }
/* 1350 */     if (arr.length == 1) {
/* 1351 */       return ObjectUtils.nullSafeToString(arr[0]);
/*      */     }
/*      */     
/* 1354 */     StringJoiner sj = new StringJoiner(delim);
/* 1355 */     for (Object elem : arr) {
/* 1356 */       sj.add(String.valueOf(elem));
/*      */     }
/* 1358 */     return sj.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String arrayToCommaDelimitedString(@Nullable Object[] arr) {
/* 1369 */     return arrayToDelimitedString(arr, ",");
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\StringUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */