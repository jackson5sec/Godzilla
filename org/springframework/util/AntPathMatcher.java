/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.springframework.lang.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AntPathMatcher
/*     */   implements PathMatcher
/*     */ {
/*     */   public static final String DEFAULT_PATH_SEPARATOR = "/";
/*     */   private static final int CACHE_TURNOFF_THRESHOLD = 65536;
/*  81 */   private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{[^/]+?\\}");
/*     */   
/*  83 */   private static final char[] WILDCARD_CHARS = new char[] { '*', '?', '{' };
/*     */ 
/*     */   
/*     */   private String pathSeparator;
/*     */   
/*     */   private PathSeparatorPatternCache pathSeparatorPatternCache;
/*     */   
/*     */   private boolean caseSensitive = true;
/*     */   
/*     */   private boolean trimTokens = false;
/*     */   
/*     */   @Nullable
/*     */   private volatile Boolean cachePatterns;
/*     */   
/*  97 */   private final Map<String, String[]> tokenizedPatternCache = (Map)new ConcurrentHashMap<>(256);
/*     */   
/*  99 */   final Map<String, AntPathStringMatcher> stringMatcherCache = new ConcurrentHashMap<>(256);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AntPathMatcher() {
/* 106 */     this.pathSeparator = "/";
/* 107 */     this.pathSeparatorPatternCache = new PathSeparatorPatternCache("/");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AntPathMatcher(String pathSeparator) {
/* 116 */     Assert.notNull(pathSeparator, "'pathSeparator' is required");
/* 117 */     this.pathSeparator = pathSeparator;
/* 118 */     this.pathSeparatorPatternCache = new PathSeparatorPatternCache(pathSeparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPathSeparator(@Nullable String pathSeparator) {
/* 127 */     this.pathSeparator = (pathSeparator != null) ? pathSeparator : "/";
/* 128 */     this.pathSeparatorPatternCache = new PathSeparatorPatternCache(this.pathSeparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCaseSensitive(boolean caseSensitive) {
/* 137 */     this.caseSensitive = caseSensitive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTrimTokens(boolean trimTokens) {
/* 145 */     this.trimTokens = trimTokens;
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
/*     */   public void setCachePatterns(boolean cachePatterns) {
/* 161 */     this.cachePatterns = Boolean.valueOf(cachePatterns);
/*     */   }
/*     */   
/*     */   private void deactivatePatternCache() {
/* 165 */     this.cachePatterns = Boolean.valueOf(false);
/* 166 */     this.tokenizedPatternCache.clear();
/* 167 */     this.stringMatcherCache.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPattern(@Nullable String path) {
/* 173 */     if (path == null) {
/* 174 */       return false;
/*     */     }
/* 176 */     boolean uriVar = false;
/* 177 */     for (int i = 0; i < path.length(); i++) {
/* 178 */       char c = path.charAt(i);
/* 179 */       if (c == '*' || c == '?') {
/* 180 */         return true;
/*     */       }
/* 182 */       if (c == '{') {
/* 183 */         uriVar = true;
/*     */       
/*     */       }
/* 186 */       else if (c == '}' && uriVar) {
/* 187 */         return true;
/*     */       } 
/*     */     } 
/* 190 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean match(String pattern, String path) {
/* 195 */     return doMatch(pattern, path, true, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matchStart(String pattern, String path) {
/* 200 */     return doMatch(pattern, path, false, null);
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
/*     */   protected boolean doMatch(String pattern, @Nullable String path, boolean fullMatch, @Nullable Map<String, String> uriTemplateVariables) {
/* 214 */     if (path == null || path.startsWith(this.pathSeparator) != pattern.startsWith(this.pathSeparator)) {
/* 215 */       return false;
/*     */     }
/*     */     
/* 218 */     String[] pattDirs = tokenizePattern(pattern);
/* 219 */     if (fullMatch && this.caseSensitive && !isPotentialMatch(path, pattDirs)) {
/* 220 */       return false;
/*     */     }
/*     */     
/* 223 */     String[] pathDirs = tokenizePath(path);
/* 224 */     int pattIdxStart = 0;
/* 225 */     int pattIdxEnd = pattDirs.length - 1;
/* 226 */     int pathIdxStart = 0;
/* 227 */     int pathIdxEnd = pathDirs.length - 1;
/*     */ 
/*     */     
/* 230 */     while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
/* 231 */       String pattDir = pattDirs[pattIdxStart];
/* 232 */       if ("**".equals(pattDir)) {
/*     */         break;
/*     */       }
/* 235 */       if (!matchStrings(pattDir, pathDirs[pathIdxStart], uriTemplateVariables)) {
/* 236 */         return false;
/*     */       }
/* 238 */       pattIdxStart++;
/* 239 */       pathIdxStart++;
/*     */     } 
/*     */     
/* 242 */     if (pathIdxStart > pathIdxEnd) {
/*     */       
/* 244 */       if (pattIdxStart > pattIdxEnd) {
/* 245 */         return (pattern.endsWith(this.pathSeparator) == path.endsWith(this.pathSeparator));
/*     */       }
/* 247 */       if (!fullMatch) {
/* 248 */         return true;
/*     */       }
/* 250 */       if (pattIdxStart == pattIdxEnd && pattDirs[pattIdxStart].equals("*") && path.endsWith(this.pathSeparator)) {
/* 251 */         return true;
/*     */       }
/* 253 */       for (int j = pattIdxStart; j <= pattIdxEnd; j++) {
/* 254 */         if (!pattDirs[j].equals("**")) {
/* 255 */           return false;
/*     */         }
/*     */       } 
/* 258 */       return true;
/*     */     } 
/* 260 */     if (pattIdxStart > pattIdxEnd)
/*     */     {
/* 262 */       return false;
/*     */     }
/* 264 */     if (!fullMatch && "**".equals(pattDirs[pattIdxStart]))
/*     */     {
/* 266 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 270 */     while (pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd) {
/* 271 */       String pattDir = pattDirs[pattIdxEnd];
/* 272 */       if (pattDir.equals("**")) {
/*     */         break;
/*     */       }
/* 275 */       if (!matchStrings(pattDir, pathDirs[pathIdxEnd], uriTemplateVariables)) {
/* 276 */         return false;
/*     */       }
/* 278 */       pattIdxEnd--;
/* 279 */       pathIdxEnd--;
/*     */     } 
/* 281 */     if (pathIdxStart > pathIdxEnd) {
/*     */       
/* 283 */       for (int j = pattIdxStart; j <= pattIdxEnd; j++) {
/* 284 */         if (!pattDirs[j].equals("**")) {
/* 285 */           return false;
/*     */         }
/*     */       } 
/* 288 */       return true;
/*     */     } 
/*     */     
/* 291 */     while (pattIdxStart != pattIdxEnd && pathIdxStart <= pathIdxEnd) {
/* 292 */       int patIdxTmp = -1;
/* 293 */       for (int j = pattIdxStart + 1; j <= pattIdxEnd; j++) {
/* 294 */         if (pattDirs[j].equals("**")) {
/* 295 */           patIdxTmp = j;
/*     */           break;
/*     */         } 
/*     */       } 
/* 299 */       if (patIdxTmp == pattIdxStart + 1) {
/*     */         
/* 301 */         pattIdxStart++;
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 306 */       int patLength = patIdxTmp - pattIdxStart - 1;
/* 307 */       int strLength = pathIdxEnd - pathIdxStart + 1;
/* 308 */       int foundIdx = -1;
/*     */ 
/*     */       
/* 311 */       for (int k = 0; k <= strLength - patLength; ) {
/* 312 */         for (int m = 0; m < patLength; m++) {
/* 313 */           String subPat = pattDirs[pattIdxStart + m + 1];
/* 314 */           String subStr = pathDirs[pathIdxStart + k + m];
/* 315 */           if (!matchStrings(subPat, subStr, uriTemplateVariables)) {
/*     */             k++; continue;
/*     */           } 
/*     */         } 
/* 319 */         foundIdx = pathIdxStart + k;
/*     */       } 
/*     */ 
/*     */       
/* 323 */       if (foundIdx == -1) {
/* 324 */         return false;
/*     */       }
/*     */       
/* 327 */       pattIdxStart = patIdxTmp;
/* 328 */       pathIdxStart = foundIdx + patLength;
/*     */     } 
/*     */     
/* 331 */     for (int i = pattIdxStart; i <= pattIdxEnd; i++) {
/* 332 */       if (!pattDirs[i].equals("**")) {
/* 333 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 337 */     return true;
/*     */   }
/*     */   
/*     */   private boolean isPotentialMatch(String path, String[] pattDirs) {
/* 341 */     if (!this.trimTokens) {
/* 342 */       int pos = 0;
/* 343 */       for (String pattDir : pattDirs) {
/* 344 */         int skipped = skipSeparator(path, pos, this.pathSeparator);
/* 345 */         pos += skipped;
/* 346 */         skipped = skipSegment(path, pos, pattDir);
/* 347 */         if (skipped < pattDir.length()) {
/* 348 */           return (skipped > 0 || (pattDir.length() > 0 && isWildcardChar(pattDir.charAt(0))));
/*     */         }
/* 350 */         pos += skipped;
/*     */       } 
/*     */     } 
/* 353 */     return true;
/*     */   }
/*     */   
/*     */   private int skipSegment(String path, int pos, String prefix) {
/* 357 */     int skipped = 0;
/* 358 */     for (int i = 0; i < prefix.length(); i++) {
/* 359 */       char c = prefix.charAt(i);
/* 360 */       if (isWildcardChar(c)) {
/* 361 */         return skipped;
/*     */       }
/* 363 */       int currPos = pos + skipped;
/* 364 */       if (currPos >= path.length()) {
/* 365 */         return 0;
/*     */       }
/* 367 */       if (c == path.charAt(currPos)) {
/* 368 */         skipped++;
/*     */       }
/*     */     } 
/* 371 */     return skipped;
/*     */   }
/*     */   
/*     */   private int skipSeparator(String path, int pos, String separator) {
/* 375 */     int skipped = 0;
/* 376 */     while (path.startsWith(separator, pos + skipped)) {
/* 377 */       skipped += separator.length();
/*     */     }
/* 379 */     return skipped;
/*     */   }
/*     */   
/*     */   private boolean isWildcardChar(char c) {
/* 383 */     for (char candidate : WILDCARD_CHARS) {
/* 384 */       if (c == candidate) {
/* 385 */         return true;
/*     */       }
/*     */     } 
/* 388 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] tokenizePattern(String pattern) {
/* 399 */     String[] tokenized = null;
/* 400 */     Boolean cachePatterns = this.cachePatterns;
/* 401 */     if (cachePatterns == null || cachePatterns.booleanValue()) {
/* 402 */       tokenized = this.tokenizedPatternCache.get(pattern);
/*     */     }
/* 404 */     if (tokenized == null) {
/* 405 */       tokenized = tokenizePath(pattern);
/* 406 */       if (cachePatterns == null && this.tokenizedPatternCache.size() >= 65536) {
/*     */ 
/*     */ 
/*     */         
/* 410 */         deactivatePatternCache();
/* 411 */         return tokenized;
/*     */       } 
/* 413 */       if (cachePatterns == null || cachePatterns.booleanValue()) {
/* 414 */         this.tokenizedPatternCache.put(pattern, tokenized);
/*     */       }
/*     */     } 
/* 417 */     return tokenized;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] tokenizePath(String path) {
/* 426 */     return StringUtils.tokenizeToStringArray(path, this.pathSeparator, this.trimTokens, true);
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
/*     */   private boolean matchStrings(String pattern, String str, @Nullable Map<String, String> uriTemplateVariables) {
/* 438 */     return getStringMatcher(pattern).matchStrings(str, uriTemplateVariables);
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
/*     */   protected AntPathStringMatcher getStringMatcher(String pattern) {
/* 455 */     AntPathStringMatcher matcher = null;
/* 456 */     Boolean cachePatterns = this.cachePatterns;
/* 457 */     if (cachePatterns == null || cachePatterns.booleanValue()) {
/* 458 */       matcher = this.stringMatcherCache.get(pattern);
/*     */     }
/* 460 */     if (matcher == null) {
/* 461 */       matcher = new AntPathStringMatcher(pattern, this.caseSensitive);
/* 462 */       if (cachePatterns == null && this.stringMatcherCache.size() >= 65536) {
/*     */ 
/*     */ 
/*     */         
/* 466 */         deactivatePatternCache();
/* 467 */         return matcher;
/*     */       } 
/* 469 */       if (cachePatterns == null || cachePatterns.booleanValue()) {
/* 470 */         this.stringMatcherCache.put(pattern, matcher);
/*     */       }
/*     */     } 
/* 473 */     return matcher;
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
/*     */   public String extractPathWithinPattern(String pattern, String path) {
/* 491 */     String[] patternParts = StringUtils.tokenizeToStringArray(pattern, this.pathSeparator, this.trimTokens, true);
/* 492 */     String[] pathParts = StringUtils.tokenizeToStringArray(path, this.pathSeparator, this.trimTokens, true);
/* 493 */     StringBuilder builder = new StringBuilder();
/* 494 */     boolean pathStarted = false;
/*     */     
/* 496 */     for (int segment = 0; segment < patternParts.length; segment++) {
/* 497 */       String patternPart = patternParts[segment];
/* 498 */       if (patternPart.indexOf('*') > -1 || patternPart.indexOf('?') > -1) {
/* 499 */         for (; segment < pathParts.length; segment++) {
/* 500 */           if (pathStarted || (segment == 0 && !pattern.startsWith(this.pathSeparator))) {
/* 501 */             builder.append(this.pathSeparator);
/*     */           }
/* 503 */           builder.append(pathParts[segment]);
/* 504 */           pathStarted = true;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 509 */     return builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> extractUriTemplateVariables(String pattern, String path) {
/* 514 */     Map<String, String> variables = new LinkedHashMap<>();
/* 515 */     boolean result = doMatch(pattern, path, true, variables);
/* 516 */     if (!result) {
/* 517 */       throw new IllegalStateException("Pattern \"" + pattern + "\" is not a match for \"" + path + "\"");
/*     */     }
/* 519 */     return variables;
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
/*     */   public String combine(String pattern1, String pattern2) {
/* 552 */     if (!StringUtils.hasText(pattern1) && !StringUtils.hasText(pattern2)) {
/* 553 */       return "";
/*     */     }
/* 555 */     if (!StringUtils.hasText(pattern1)) {
/* 556 */       return pattern2;
/*     */     }
/* 558 */     if (!StringUtils.hasText(pattern2)) {
/* 559 */       return pattern1;
/*     */     }
/*     */     
/* 562 */     boolean pattern1ContainsUriVar = (pattern1.indexOf('{') != -1);
/* 563 */     if (!pattern1.equals(pattern2) && !pattern1ContainsUriVar && match(pattern1, pattern2))
/*     */     {
/*     */       
/* 566 */       return pattern2;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 571 */     if (pattern1.endsWith(this.pathSeparatorPatternCache.getEndsOnWildCard())) {
/* 572 */       return concat(pattern1.substring(0, pattern1.length() - 2), pattern2);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 577 */     if (pattern1.endsWith(this.pathSeparatorPatternCache.getEndsOnDoubleWildCard())) {
/* 578 */       return concat(pattern1, pattern2);
/*     */     }
/*     */     
/* 581 */     int starDotPos1 = pattern1.indexOf("*.");
/* 582 */     if (pattern1ContainsUriVar || starDotPos1 == -1 || this.pathSeparator.equals("."))
/*     */     {
/* 584 */       return concat(pattern1, pattern2);
/*     */     }
/*     */     
/* 587 */     String ext1 = pattern1.substring(starDotPos1 + 1);
/* 588 */     int dotPos2 = pattern2.indexOf('.');
/* 589 */     String file2 = (dotPos2 == -1) ? pattern2 : pattern2.substring(0, dotPos2);
/* 590 */     String ext2 = (dotPos2 == -1) ? "" : pattern2.substring(dotPos2);
/* 591 */     boolean ext1All = (ext1.equals(".*") || ext1.isEmpty());
/* 592 */     boolean ext2All = (ext2.equals(".*") || ext2.isEmpty());
/* 593 */     if (!ext1All && !ext2All) {
/* 594 */       throw new IllegalArgumentException("Cannot combine patterns: " + pattern1 + " vs " + pattern2);
/*     */     }
/* 596 */     String ext = ext1All ? ext2 : ext1;
/* 597 */     return file2 + ext;
/*     */   }
/*     */   
/*     */   private String concat(String path1, String path2) {
/* 601 */     boolean path1EndsWithSeparator = path1.endsWith(this.pathSeparator);
/* 602 */     boolean path2StartsWithSeparator = path2.startsWith(this.pathSeparator);
/*     */     
/* 604 */     if (path1EndsWithSeparator && path2StartsWithSeparator) {
/* 605 */       return path1 + path2.substring(1);
/*     */     }
/* 607 */     if (path1EndsWithSeparator || path2StartsWithSeparator) {
/* 608 */       return path1 + path2;
/*     */     }
/*     */     
/* 611 */     return path1 + this.pathSeparator + path2;
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
/*     */   public Comparator<String> getPatternComparator(String path) {
/* 634 */     return new AntPatternComparator(path);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class AntPathStringMatcher
/*     */   {
/* 645 */     private static final Pattern GLOB_PATTERN = Pattern.compile("\\?|\\*|\\{((?:\\{[^/]+?\\}|[^/{}]|\\\\[{}])+?)\\}");
/*     */     
/*     */     private static final String DEFAULT_VARIABLE_PATTERN = "((?s).*)";
/*     */     
/*     */     private final String rawPattern;
/*     */     
/*     */     private final boolean caseSensitive;
/*     */     
/*     */     private final boolean exactMatch;
/*     */     
/*     */     @Nullable
/*     */     private final Pattern pattern;
/*     */     
/* 658 */     private final List<String> variableNames = new ArrayList<>();
/*     */     
/*     */     public AntPathStringMatcher(String pattern) {
/* 661 */       this(pattern, true);
/*     */     }
/*     */     
/*     */     public AntPathStringMatcher(String pattern, boolean caseSensitive) {
/* 665 */       this.rawPattern = pattern;
/* 666 */       this.caseSensitive = caseSensitive;
/* 667 */       StringBuilder patternBuilder = new StringBuilder();
/* 668 */       Matcher matcher = GLOB_PATTERN.matcher(pattern);
/* 669 */       int end = 0;
/* 670 */       while (matcher.find()) {
/* 671 */         patternBuilder.append(quote(pattern, end, matcher.start()));
/* 672 */         String match = matcher.group();
/* 673 */         if ("?".equals(match)) {
/* 674 */           patternBuilder.append('.');
/*     */         }
/* 676 */         else if ("*".equals(match)) {
/* 677 */           patternBuilder.append(".*");
/*     */         }
/* 679 */         else if (match.startsWith("{") && match.endsWith("}")) {
/* 680 */           int colonIdx = match.indexOf(':');
/* 681 */           if (colonIdx == -1) {
/* 682 */             patternBuilder.append("((?s).*)");
/* 683 */             this.variableNames.add(matcher.group(1));
/*     */           } else {
/*     */             
/* 686 */             String variablePattern = match.substring(colonIdx + 1, match.length() - 1);
/* 687 */             patternBuilder.append('(');
/* 688 */             patternBuilder.append(variablePattern);
/* 689 */             patternBuilder.append(')');
/* 690 */             String variableName = match.substring(1, colonIdx);
/* 691 */             this.variableNames.add(variableName);
/*     */           } 
/*     */         } 
/* 694 */         end = matcher.end();
/*     */       } 
/*     */       
/* 697 */       if (end == 0) {
/* 698 */         this.exactMatch = true;
/* 699 */         this.pattern = null;
/*     */       } else {
/*     */         
/* 702 */         this.exactMatch = false;
/* 703 */         patternBuilder.append(quote(pattern, end, pattern.length()));
/* 704 */         this
/* 705 */           .pattern = this.caseSensitive ? Pattern.compile(patternBuilder.toString()) : Pattern.compile(patternBuilder.toString(), 2);
/*     */       } 
/*     */     }
/*     */     
/*     */     private String quote(String s, int start, int end) {
/* 710 */       if (start == end) {
/* 711 */         return "";
/*     */       }
/* 713 */       return Pattern.quote(s.substring(start, end));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean matchStrings(String str, @Nullable Map<String, String> uriTemplateVariables) {
/* 721 */       if (this.exactMatch) {
/* 722 */         return this.caseSensitive ? this.rawPattern.equals(str) : this.rawPattern.equalsIgnoreCase(str);
/*     */       }
/* 724 */       if (this.pattern != null) {
/* 725 */         Matcher matcher = this.pattern.matcher(str);
/* 726 */         if (matcher.matches()) {
/* 727 */           if (uriTemplateVariables != null) {
/* 728 */             if (this.variableNames.size() != matcher.groupCount()) {
/* 729 */               throw new IllegalArgumentException("The number of capturing groups in the pattern segment " + this.pattern + " does not match the number of URI template variables it defines, which can occur if capturing groups are used in a URI template regex. Use non-capturing groups instead.");
/*     */             }
/*     */ 
/*     */ 
/*     */             
/* 734 */             for (int i = 1; i <= matcher.groupCount(); i++) {
/* 735 */               String name = this.variableNames.get(i - 1);
/* 736 */               String value = matcher.group(i);
/* 737 */               uriTemplateVariables.put(name, value);
/*     */             } 
/*     */           } 
/* 740 */           return true;
/*     */         } 
/*     */       } 
/* 743 */       return false;
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
/*     */   protected static class AntPatternComparator
/*     */     implements Comparator<String>
/*     */   {
/*     */     private final String path;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public AntPatternComparator(String path) {
/* 767 */       this.path = path;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int compare(String pattern1, String pattern2) {
/* 778 */       PatternInfo info1 = new PatternInfo(pattern1);
/* 779 */       PatternInfo info2 = new PatternInfo(pattern2);
/*     */       
/* 781 */       if (info1.isLeastSpecific() && info2.isLeastSpecific()) {
/* 782 */         return 0;
/*     */       }
/* 784 */       if (info1.isLeastSpecific()) {
/* 785 */         return 1;
/*     */       }
/* 787 */       if (info2.isLeastSpecific()) {
/* 788 */         return -1;
/*     */       }
/*     */       
/* 791 */       boolean pattern1EqualsPath = pattern1.equals(this.path);
/* 792 */       boolean pattern2EqualsPath = pattern2.equals(this.path);
/* 793 */       if (pattern1EqualsPath && pattern2EqualsPath) {
/* 794 */         return 0;
/*     */       }
/* 796 */       if (pattern1EqualsPath) {
/* 797 */         return -1;
/*     */       }
/* 799 */       if (pattern2EqualsPath) {
/* 800 */         return 1;
/*     */       }
/*     */       
/* 803 */       if (info1.isPrefixPattern() && info2.isPrefixPattern()) {
/* 804 */         return info2.getLength() - info1.getLength();
/*     */       }
/* 806 */       if (info1.isPrefixPattern() && info2.getDoubleWildcards() == 0) {
/* 807 */         return 1;
/*     */       }
/* 809 */       if (info2.isPrefixPattern() && info1.getDoubleWildcards() == 0) {
/* 810 */         return -1;
/*     */       }
/*     */       
/* 813 */       if (info1.getTotalCount() != info2.getTotalCount()) {
/* 814 */         return info1.getTotalCount() - info2.getTotalCount();
/*     */       }
/*     */       
/* 817 */       if (info1.getLength() != info2.getLength()) {
/* 818 */         return info2.getLength() - info1.getLength();
/*     */       }
/*     */       
/* 821 */       if (info1.getSingleWildcards() < info2.getSingleWildcards()) {
/* 822 */         return -1;
/*     */       }
/* 824 */       if (info2.getSingleWildcards() < info1.getSingleWildcards()) {
/* 825 */         return 1;
/*     */       }
/*     */       
/* 828 */       if (info1.getUriVars() < info2.getUriVars()) {
/* 829 */         return -1;
/*     */       }
/* 831 */       if (info2.getUriVars() < info1.getUriVars()) {
/* 832 */         return 1;
/*     */       }
/*     */       
/* 835 */       return 0;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private static class PatternInfo
/*     */     {
/*     */       @Nullable
/*     */       private final String pattern;
/*     */ 
/*     */       
/*     */       private int uriVars;
/*     */ 
/*     */       
/*     */       private int singleWildcards;
/*     */       
/*     */       private int doubleWildcards;
/*     */       
/*     */       private boolean catchAllPattern;
/*     */       
/*     */       private boolean prefixPattern;
/*     */       
/*     */       @Nullable
/*     */       private Integer length;
/*     */ 
/*     */       
/*     */       public PatternInfo(@Nullable String pattern) {
/* 862 */         this.pattern = pattern;
/* 863 */         if (this.pattern != null) {
/* 864 */           initCounters();
/* 865 */           this.catchAllPattern = this.pattern.equals("/**");
/* 866 */           this.prefixPattern = (!this.catchAllPattern && this.pattern.endsWith("/**"));
/*     */         } 
/* 868 */         if (this.uriVars == 0) {
/* 869 */           this.length = Integer.valueOf((this.pattern != null) ? this.pattern.length() : 0);
/*     */         }
/*     */       }
/*     */       
/*     */       protected void initCounters() {
/* 874 */         int pos = 0;
/* 875 */         if (this.pattern != null) {
/* 876 */           while (pos < this.pattern.length()) {
/* 877 */             if (this.pattern.charAt(pos) == '{') {
/* 878 */               this.uriVars++;
/* 879 */               pos++; continue;
/*     */             } 
/* 881 */             if (this.pattern.charAt(pos) == '*') {
/* 882 */               if (pos + 1 < this.pattern.length() && this.pattern.charAt(pos + 1) == '*') {
/* 883 */                 this.doubleWildcards++;
/* 884 */                 pos += 2; continue;
/*     */               } 
/* 886 */               if (pos > 0 && !this.pattern.substring(pos - 1).equals(".*")) {
/* 887 */                 this.singleWildcards++;
/* 888 */                 pos++;
/*     */                 continue;
/*     */               } 
/* 891 */               pos++;
/*     */               
/*     */               continue;
/*     */             } 
/* 895 */             pos++;
/*     */           } 
/*     */         }
/*     */       }
/*     */ 
/*     */       
/*     */       public int getUriVars() {
/* 902 */         return this.uriVars;
/*     */       }
/*     */       
/*     */       public int getSingleWildcards() {
/* 906 */         return this.singleWildcards;
/*     */       }
/*     */       
/*     */       public int getDoubleWildcards() {
/* 910 */         return this.doubleWildcards;
/*     */       }
/*     */       
/*     */       public boolean isLeastSpecific() {
/* 914 */         return (this.pattern == null || this.catchAllPattern);
/*     */       }
/*     */       
/*     */       public boolean isPrefixPattern() {
/* 918 */         return this.prefixPattern;
/*     */       }
/*     */       
/*     */       public int getTotalCount() {
/* 922 */         return this.uriVars + this.singleWildcards + 2 * this.doubleWildcards;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public int getLength() {
/* 929 */         if (this.length == null) {
/* 930 */           this.length = Integer.valueOf((this.pattern != null) ? AntPathMatcher
/* 931 */               .VARIABLE_PATTERN.matcher(this.pattern).replaceAll("#").length() : 0);
/*     */         }
/* 933 */         return this.length.intValue();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class PathSeparatorPatternCache
/*     */   {
/*     */     private final String endsOnWildCard;
/*     */ 
/*     */     
/*     */     private final String endsOnDoubleWildCard;
/*     */ 
/*     */     
/*     */     public PathSeparatorPatternCache(String pathSeparator) {
/* 949 */       this.endsOnWildCard = pathSeparator + "*";
/* 950 */       this.endsOnDoubleWildCard = pathSeparator + "**";
/*     */     }
/*     */     
/*     */     public String getEndsOnWildCard() {
/* 954 */       return this.endsOnWildCard;
/*     */     }
/*     */     
/*     */     public String getEndsOnDoubleWildCard() {
/* 958 */       return this.endsOnDoubleWildCard;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\AntPathMatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */