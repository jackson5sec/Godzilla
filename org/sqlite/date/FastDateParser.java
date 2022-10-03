/*     */ package org.sqlite.date;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.text.DateFormatSymbols;
/*     */ import java.text.ParseException;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TimeZone;
/*     */ import java.util.TreeMap;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FastDateParser
/*     */   implements DateParser, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2L;
/*  80 */   static final Locale JAPANESE_IMPERIAL = new Locale("ja", "JP", "JP");
/*     */ 
/*     */   
/*     */   private final String pattern;
/*     */ 
/*     */   
/*     */   private final TimeZone timeZone;
/*     */ 
/*     */   
/*     */   private final Locale locale;
/*     */ 
/*     */   
/*     */   private final int century;
/*     */ 
/*     */   
/*     */   private final int startYear;
/*     */ 
/*     */   
/*     */   private transient Pattern parsePattern;
/*     */ 
/*     */   
/*     */   private transient Strategy[] strategies;
/*     */   
/*     */   private transient String currentFormatField;
/*     */   
/*     */   private transient Strategy nextStrategy;
/*     */ 
/*     */   
/*     */   protected FastDateParser(String pattern, TimeZone timeZone, Locale locale) {
/* 109 */     this(pattern, timeZone, locale, null);
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
/*     */   protected FastDateParser(String pattern, TimeZone timeZone, Locale locale, Date centuryStart) {
/*     */     int centuryStartYear;
/* 124 */     this.pattern = pattern;
/* 125 */     this.timeZone = timeZone;
/* 126 */     this.locale = locale;
/*     */     
/* 128 */     Calendar definingCalendar = Calendar.getInstance(timeZone, locale);
/*     */     
/* 130 */     if (centuryStart != null) {
/* 131 */       definingCalendar.setTime(centuryStart);
/* 132 */       centuryStartYear = definingCalendar.get(1);
/*     */     }
/* 134 */     else if (locale.equals(JAPANESE_IMPERIAL)) {
/* 135 */       centuryStartYear = 0;
/*     */     }
/*     */     else {
/*     */       
/* 139 */       definingCalendar.setTime(new Date());
/* 140 */       centuryStartYear = definingCalendar.get(1) - 80;
/*     */     } 
/* 142 */     this.century = centuryStartYear / 100 * 100;
/* 143 */     this.startYear = centuryStartYear - this.century;
/*     */     
/* 145 */     init(definingCalendar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init(Calendar definingCalendar) {
/* 156 */     StringBuilder regex = new StringBuilder();
/* 157 */     List<Strategy> collector = new ArrayList<>();
/*     */     
/* 159 */     Matcher patternMatcher = formatPattern.matcher(this.pattern);
/* 160 */     if (!patternMatcher.lookingAt()) {
/* 161 */       throw new IllegalArgumentException("Illegal pattern character '" + this.pattern
/* 162 */           .charAt(patternMatcher.regionStart()) + "'");
/*     */     }
/*     */     
/* 165 */     this.currentFormatField = patternMatcher.group();
/* 166 */     Strategy currentStrategy = getStrategy(this.currentFormatField, definingCalendar);
/*     */     while (true) {
/* 168 */       patternMatcher.region(patternMatcher.end(), patternMatcher.regionEnd());
/* 169 */       if (!patternMatcher.lookingAt()) {
/* 170 */         this.nextStrategy = null;
/*     */         break;
/*     */       } 
/* 173 */       String nextFormatField = patternMatcher.group();
/* 174 */       this.nextStrategy = getStrategy(nextFormatField, definingCalendar);
/* 175 */       if (currentStrategy.addRegex(this, regex)) {
/* 176 */         collector.add(currentStrategy);
/*     */       }
/* 178 */       this.currentFormatField = nextFormatField;
/* 179 */       currentStrategy = this.nextStrategy;
/*     */     } 
/* 181 */     if (patternMatcher.regionStart() != patternMatcher.regionEnd()) {
/* 182 */       throw new IllegalArgumentException("Failed to parse \"" + this.pattern + "\" ; gave up at index " + patternMatcher.regionStart());
/*     */     }
/* 184 */     if (currentStrategy.addRegex(this, regex)) {
/* 185 */       collector.add(currentStrategy);
/*     */     }
/* 187 */     this.currentFormatField = null;
/* 188 */     this.strategies = collector.<Strategy>toArray(new Strategy[collector.size()]);
/* 189 */     this.parsePattern = Pattern.compile(regex.toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPattern() {
/* 198 */     return this.pattern;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeZone getTimeZone() {
/* 205 */     return this.timeZone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Locale getLocale() {
/* 212 */     return this.locale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Pattern getParsePattern() {
/* 221 */     return this.parsePattern;
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
/*     */   public boolean equals(Object obj) {
/* 234 */     if (!(obj instanceof FastDateParser)) {
/* 235 */       return false;
/*     */     }
/* 237 */     FastDateParser other = (FastDateParser)obj;
/* 238 */     return (this.pattern.equals(other.pattern) && this.timeZone
/* 239 */       .equals(other.timeZone) && this.locale
/* 240 */       .equals(other.locale));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 250 */     return this.pattern.hashCode() + 13 * (this.timeZone.hashCode() + 13 * this.locale.hashCode());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 260 */     return "FastDateParser[" + this.pattern + "," + this.locale + "," + this.timeZone.getID() + "]";
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
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 274 */     in.defaultReadObject();
/*     */     
/* 276 */     Calendar definingCalendar = Calendar.getInstance(this.timeZone, this.locale);
/* 277 */     init(definingCalendar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object parseObject(String source) throws ParseException {
/* 284 */     return parse(source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date parse(String source) throws ParseException {
/* 291 */     Date date = parse(source, new ParsePosition(0));
/* 292 */     if (date == null) {
/*     */       
/* 294 */       if (this.locale.equals(JAPANESE_IMPERIAL)) {
/* 295 */         throw new ParseException("(The " + this.locale + " locale does not support dates before 1868 AD)\nUnparseable date: \"" + source + "\" does not match " + this.parsePattern
/*     */             
/* 297 */             .pattern(), 0);
/*     */       }
/* 299 */       throw new ParseException("Unparseable date: \"" + source + "\" does not match " + this.parsePattern.pattern(), 0);
/*     */     } 
/* 301 */     return date;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object parseObject(String source, ParsePosition pos) {
/* 308 */     return parse(source, pos);
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
/*     */   public Date parse(String source, ParsePosition pos) {
/* 324 */     int offset = pos.getIndex();
/* 325 */     Matcher matcher = this.parsePattern.matcher(source.substring(offset));
/* 326 */     if (!matcher.lookingAt()) {
/* 327 */       return null;
/*     */     }
/*     */     
/* 330 */     Calendar cal = Calendar.getInstance(this.timeZone, this.locale);
/* 331 */     cal.clear();
/*     */     
/* 333 */     for (int i = 0; i < this.strategies.length; ) {
/* 334 */       Strategy strategy = this.strategies[i++];
/* 335 */       strategy.setCalendar(this, cal, matcher.group(i));
/*     */     } 
/* 337 */     pos.setIndex(offset + matcher.end());
/* 338 */     return cal.getTime();
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
/*     */   private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
/* 352 */     regex.append("\\Q");
/* 353 */     for (int i = 0; i < value.length(); i++) {
/* 354 */       char c = value.charAt(i);
/* 355 */       switch (c) {
/*     */         case '\'':
/* 357 */           if (unquote) {
/* 358 */             if (++i == value.length()) {
/* 359 */               return regex;
/*     */             }
/* 361 */             c = value.charAt(i);
/*     */           } 
/*     */           break;
/*     */         case '\\':
/* 365 */           if (++i == value.length()) {
/*     */             break;
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 375 */           regex.append(c);
/* 376 */           c = value.charAt(i);
/* 377 */           if (c == 'E') {
/* 378 */             regex.append("E\\\\E\\");
/* 379 */             c = 'Q';
/*     */           } 
/*     */           break;
/*     */       } 
/*     */ 
/*     */       
/* 385 */       regex.append(c);
/*     */     } 
/* 387 */     regex.append("\\E");
/* 388 */     return regex;
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
/*     */   private static Map<String, Integer> getDisplayNames(int field, Calendar definingCalendar, Locale locale) {
/* 400 */     return definingCalendar.getDisplayNames(field, 0, locale);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int adjustYear(int twoDigitYear) {
/* 409 */     int trial = this.century + twoDigitYear;
/* 410 */     return (twoDigitYear >= this.startYear) ? trial : (trial + 100);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isNextNumber() {
/* 418 */     return (this.nextStrategy != null && this.nextStrategy.isNumber());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getFieldWidth() {
/* 426 */     return this.currentFormatField.length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static abstract class Strategy
/*     */   {
/*     */     private Strategy() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isNumber() {
/* 441 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setCalendar(FastDateParser parser, Calendar cal, String value) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract boolean addRegex(FastDateParser param1FastDateParser, StringBuilder param1StringBuilder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 472 */   private static final Pattern formatPattern = Pattern.compile("D+|E+|F+|G+|H+|K+|M+|S+|W+|X+|Z+|a+|d+|h+|k+|m+|s+|w+|y+|z+|''|'[^']++(''[^']*+)*+'|[^'A-Za-z]++");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Strategy getStrategy(String formatField, Calendar definingCalendar) {
/* 482 */     switch (formatField.charAt(0)) {
/*     */       case '\'':
/* 484 */         if (formatField.length() > 2) {
/* 485 */           return new CopyQuotedStrategy(formatField.substring(1, formatField.length() - 1));
/*     */         }
/*     */       
/*     */       default:
/* 489 */         return new CopyQuotedStrategy(formatField);
/*     */       case 'D':
/* 491 */         return DAY_OF_YEAR_STRATEGY;
/*     */       case 'E':
/* 493 */         return getLocaleSpecificStrategy(7, definingCalendar);
/*     */       case 'F':
/* 495 */         return DAY_OF_WEEK_IN_MONTH_STRATEGY;
/*     */       case 'G':
/* 497 */         return getLocaleSpecificStrategy(0, definingCalendar);
/*     */       case 'H':
/* 499 */         return HOUR_OF_DAY_STRATEGY;
/*     */       case 'K':
/* 501 */         return HOUR_STRATEGY;
/*     */       case 'M':
/* 503 */         return (formatField.length() >= 3) ? getLocaleSpecificStrategy(2, definingCalendar) : NUMBER_MONTH_STRATEGY;
/*     */       case 'S':
/* 505 */         return MILLISECOND_STRATEGY;
/*     */       case 'W':
/* 507 */         return WEEK_OF_MONTH_STRATEGY;
/*     */       case 'a':
/* 509 */         return getLocaleSpecificStrategy(9, definingCalendar);
/*     */       case 'd':
/* 511 */         return DAY_OF_MONTH_STRATEGY;
/*     */       case 'h':
/* 513 */         return HOUR12_STRATEGY;
/*     */       case 'k':
/* 515 */         return HOUR24_OF_DAY_STRATEGY;
/*     */       case 'm':
/* 517 */         return MINUTE_STRATEGY;
/*     */       case 's':
/* 519 */         return SECOND_STRATEGY;
/*     */       case 'w':
/* 521 */         return WEEK_OF_YEAR_STRATEGY;
/*     */       case 'y':
/* 523 */         return (formatField.length() > 2) ? LITERAL_YEAR_STRATEGY : ABBREVIATED_YEAR_STRATEGY;
/*     */       case 'X':
/* 525 */         return ISO8601TimeZoneStrategy.getStrategy(formatField.length());
/*     */       case 'Z':
/* 527 */         if (formatField.equals("ZZ"))
/* 528 */           return ISO_8601_STRATEGY;  break;
/*     */       case 'z':
/*     */         break;
/*     */     } 
/* 532 */     return getLocaleSpecificStrategy(15, definingCalendar);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 537 */   private static final ConcurrentMap<Locale, Strategy>[] caches = (ConcurrentMap<Locale, Strategy>[])new ConcurrentMap[17];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ConcurrentMap<Locale, Strategy> getCache(int field) {
/* 545 */     synchronized (caches) {
/* 546 */       if (caches[field] == null) {
/* 547 */         caches[field] = new ConcurrentHashMap<>(3);
/*     */       }
/* 549 */       return caches[field];
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Strategy getLocaleSpecificStrategy(int field, Calendar definingCalendar) {
/* 560 */     ConcurrentMap<Locale, Strategy> cache = getCache(field);
/* 561 */     Strategy strategy = cache.get(this.locale);
/* 562 */     if (strategy == null) {
/* 563 */       strategy = (field == 15) ? new TimeZoneStrategy(this.locale) : new CaseInsensitiveTextStrategy(field, definingCalendar, this.locale);
/*     */ 
/*     */       
/* 566 */       Strategy inCache = cache.putIfAbsent(this.locale, strategy);
/* 567 */       if (inCache != null) {
/* 568 */         return inCache;
/*     */       }
/*     */     } 
/* 571 */     return strategy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CopyQuotedStrategy
/*     */     extends Strategy
/*     */   {
/*     */     private final String formatField;
/*     */ 
/*     */ 
/*     */     
/*     */     CopyQuotedStrategy(String formatField) {
/* 585 */       this.formatField = formatField;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isNumber() {
/* 593 */       char c = this.formatField.charAt(0);
/* 594 */       if (c == '\'') {
/* 595 */         c = this.formatField.charAt(1);
/*     */       }
/* 597 */       return Character.isDigit(c);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean addRegex(FastDateParser parser, StringBuilder regex) {
/* 605 */       FastDateParser.escapeRegex(regex, this.formatField, true);
/* 606 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class CaseInsensitiveTextStrategy
/*     */     extends Strategy
/*     */   {
/*     */     private final int field;
/*     */ 
/*     */     
/*     */     private final Locale locale;
/*     */ 
/*     */     
/*     */     private final Map<String, Integer> lKeyValues;
/*     */ 
/*     */     
/*     */     CaseInsensitiveTextStrategy(int field, Calendar definingCalendar, Locale locale) {
/* 625 */       this.field = field;
/* 626 */       this.locale = locale;
/* 627 */       Map<String, Integer> keyValues = FastDateParser.getDisplayNames(field, definingCalendar, locale);
/* 628 */       this.lKeyValues = new HashMap<>();
/*     */       
/* 630 */       for (Map.Entry<String, Integer> entry : keyValues.entrySet()) {
/* 631 */         this.lKeyValues.put(((String)entry.getKey()).toLowerCase(locale), entry.getValue());
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean addRegex(FastDateParser parser, StringBuilder regex) {
/* 640 */       regex.append("((?iu)");
/* 641 */       for (String textKeyValue : this.lKeyValues.keySet()) {
/* 642 */         FastDateParser.escapeRegex(regex, textKeyValue, false).append('|');
/*     */       }
/* 644 */       regex.setCharAt(regex.length() - 1, ')');
/* 645 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setCalendar(FastDateParser parser, Calendar cal, String value) {
/* 653 */       Integer iVal = this.lKeyValues.get(value.toLowerCase(this.locale));
/* 654 */       if (iVal == null) {
/* 655 */         StringBuilder sb = new StringBuilder(value);
/* 656 */         sb.append(" not in (");
/* 657 */         for (String textKeyValue : this.lKeyValues.keySet()) {
/* 658 */           sb.append(textKeyValue).append(' ');
/*     */         }
/* 660 */         sb.setCharAt(sb.length() - 1, ')');
/* 661 */         throw new IllegalArgumentException(sb.toString());
/*     */       } 
/* 663 */       cal.set(this.field, iVal.intValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class NumberStrategy
/*     */     extends Strategy
/*     */   {
/*     */     private final int field;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     NumberStrategy(int field) {
/* 679 */       this.field = field;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isNumber() {
/* 687 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean addRegex(FastDateParser parser, StringBuilder regex) {
/* 696 */       if (parser.isNextNumber()) {
/* 697 */         regex.append("(\\p{Nd}{").append(parser.getFieldWidth()).append("}+)");
/*     */       } else {
/*     */         
/* 700 */         regex.append("(\\p{Nd}++)");
/*     */       } 
/* 702 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setCalendar(FastDateParser parser, Calendar cal, String value) {
/* 710 */       cal.set(this.field, modify(Integer.parseInt(value)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int modify(int iValue) {
/* 719 */       return iValue;
/*     */     }
/*     */   }
/*     */   
/* 723 */   private static final Strategy ABBREVIATED_YEAR_STRATEGY = new NumberStrategy(1)
/*     */     {
/*     */ 
/*     */       
/*     */       void setCalendar(FastDateParser parser, Calendar cal, String value)
/*     */       {
/* 729 */         int iValue = Integer.parseInt(value);
/* 730 */         if (iValue < 100) {
/* 731 */           iValue = parser.adjustYear(iValue);
/*     */         }
/* 733 */         cal.set(1, iValue);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   private static class TimeZoneStrategy
/*     */     extends Strategy
/*     */   {
/*     */     private final String validTimeZoneChars;
/*     */     
/* 743 */     private final SortedMap<String, TimeZone> tzNames = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static final int ID = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static final int LONG_STD = 1;
/*     */ 
/*     */ 
/*     */     
/*     */     private static final int SHORT_STD = 2;
/*     */ 
/*     */ 
/*     */     
/*     */     private static final int LONG_DST = 3;
/*     */ 
/*     */ 
/*     */     
/*     */     private static final int SHORT_DST = 4;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     TimeZoneStrategy(Locale locale) {
/* 771 */       String[][] zones = DateFormatSymbols.getInstance(locale).getZoneStrings();
/* 772 */       for (String[] zone : zones) {
/* 773 */         if (!zone[0].startsWith("GMT")) {
/*     */ 
/*     */           
/* 776 */           TimeZone tz = TimeZone.getTimeZone(zone[0]);
/* 777 */           if (!this.tzNames.containsKey(zone[1])) {
/* 778 */             this.tzNames.put(zone[1], tz);
/*     */           }
/* 780 */           if (!this.tzNames.containsKey(zone[2])) {
/* 781 */             this.tzNames.put(zone[2], tz);
/*     */           }
/* 783 */           if (tz.useDaylightTime()) {
/* 784 */             if (!this.tzNames.containsKey(zone[3])) {
/* 785 */               this.tzNames.put(zone[3], tz);
/*     */             }
/* 787 */             if (!this.tzNames.containsKey(zone[4])) {
/* 788 */               this.tzNames.put(zone[4], tz);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/* 793 */       StringBuilder sb = new StringBuilder();
/* 794 */       sb.append("(GMT[+-]\\d{1,2}:\\d{2}").append('|');
/* 795 */       sb.append("[+-]\\d{4}").append('|');
/* 796 */       for (String id : this.tzNames.keySet()) {
/* 797 */         FastDateParser.escapeRegex(sb, id, false).append('|');
/*     */       }
/* 799 */       sb.setCharAt(sb.length() - 1, ')');
/* 800 */       this.validTimeZoneChars = sb.toString();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean addRegex(FastDateParser parser, StringBuilder regex) {
/* 808 */       regex.append(this.validTimeZoneChars);
/* 809 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setCalendar(FastDateParser parser, Calendar cal, String value) {
/*     */       TimeZone tz;
/* 818 */       if (value.charAt(0) == '+' || value.charAt(0) == '-') {
/* 819 */         tz = TimeZone.getTimeZone("GMT" + value);
/*     */       }
/* 821 */       else if (value.startsWith("GMT")) {
/* 822 */         tz = TimeZone.getTimeZone(value);
/*     */       } else {
/*     */         
/* 825 */         tz = this.tzNames.get(value);
/* 826 */         if (tz == null) {
/* 827 */           throw new IllegalArgumentException(value + " is not a supported timezone name");
/*     */         }
/*     */       } 
/* 830 */       cal.setTimeZone(tz);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class ISO8601TimeZoneStrategy
/*     */     extends Strategy
/*     */   {
/*     */     private final String pattern;
/*     */ 
/*     */     
/*     */     ISO8601TimeZoneStrategy(String pattern) {
/* 843 */       this.pattern = pattern;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean addRegex(FastDateParser parser, StringBuilder regex) {
/* 851 */       regex.append(this.pattern);
/* 852 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     void setCalendar(FastDateParser parser, Calendar cal, String value) {
/* 860 */       if (value.equals("Z")) {
/* 861 */         cal.setTimeZone(TimeZone.getTimeZone("UTC"));
/*     */       } else {
/* 863 */         cal.setTimeZone(TimeZone.getTimeZone("GMT" + value));
/*     */       } 
/*     */     }
/*     */     
/* 867 */     private static final FastDateParser.Strategy ISO_8601_1_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}))");
/* 868 */     private static final FastDateParser.Strategy ISO_8601_2_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}\\d{2}))");
/* 869 */     private static final FastDateParser.Strategy ISO_8601_3_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}(?::)\\d{2}))");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static FastDateParser.Strategy getStrategy(int tokenLen) {
/* 879 */       switch (tokenLen) {
/*     */         case 1:
/* 881 */           return ISO_8601_1_STRATEGY;
/*     */         case 2:
/* 883 */           return ISO_8601_2_STRATEGY;
/*     */         case 3:
/* 885 */           return ISO_8601_3_STRATEGY;
/*     */       } 
/* 887 */       throw new IllegalArgumentException("invalid number of X");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/* 892 */   private static final Strategy NUMBER_MONTH_STRATEGY = new NumberStrategy(2)
/*     */     {
/*     */       int modify(int iValue) {
/* 895 */         return iValue - 1;
/*     */       }
/*     */     };
/* 898 */   private static final Strategy LITERAL_YEAR_STRATEGY = new NumberStrategy(1);
/* 899 */   private static final Strategy WEEK_OF_YEAR_STRATEGY = new NumberStrategy(3);
/* 900 */   private static final Strategy WEEK_OF_MONTH_STRATEGY = new NumberStrategy(4);
/* 901 */   private static final Strategy DAY_OF_YEAR_STRATEGY = new NumberStrategy(6);
/* 902 */   private static final Strategy DAY_OF_MONTH_STRATEGY = new NumberStrategy(5);
/* 903 */   private static final Strategy DAY_OF_WEEK_IN_MONTH_STRATEGY = new NumberStrategy(8);
/* 904 */   private static final Strategy HOUR_OF_DAY_STRATEGY = new NumberStrategy(11);
/* 905 */   private static final Strategy HOUR24_OF_DAY_STRATEGY = new NumberStrategy(11)
/*     */     {
/*     */       int modify(int iValue) {
/* 908 */         return (iValue == 24) ? 0 : iValue;
/*     */       }
/*     */     };
/* 911 */   private static final Strategy HOUR12_STRATEGY = new NumberStrategy(10)
/*     */     {
/*     */       int modify(int iValue) {
/* 914 */         return (iValue == 12) ? 0 : iValue;
/*     */       }
/*     */     };
/* 917 */   private static final Strategy HOUR_STRATEGY = new NumberStrategy(10);
/* 918 */   private static final Strategy MINUTE_STRATEGY = new NumberStrategy(12);
/* 919 */   private static final Strategy SECOND_STRATEGY = new NumberStrategy(13);
/* 920 */   private static final Strategy MILLISECOND_STRATEGY = new NumberStrategy(14);
/* 921 */   private static final Strategy ISO_8601_STRATEGY = new ISO8601TimeZoneStrategy("(Z|(?:[+-]\\d{2}(?::?\\d{2})?))");
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\date\FastDateParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */