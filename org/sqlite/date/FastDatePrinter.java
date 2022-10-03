/*      */ package org.sqlite.date;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Serializable;
/*      */ import java.text.DateFormatSymbols;
/*      */ import java.text.FieldPosition;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.TimeZone;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
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
/*      */ public class FastDatePrinter
/*      */   implements DatePrinter, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*      */   public static final int FULL = 0;
/*      */   public static final int LONG = 1;
/*      */   public static final int MEDIUM = 2;
/*      */   public static final int SHORT = 3;
/*      */   private final String mPattern;
/*      */   private final TimeZone mTimeZone;
/*      */   private final Locale mLocale;
/*      */   private transient Rule[] mRules;
/*      */   private transient int mMaxLengthEstimate;
/*      */   
/*      */   protected FastDatePrinter(String pattern, TimeZone timeZone, Locale locale) {
/*  151 */     this.mPattern = pattern;
/*  152 */     this.mTimeZone = timeZone;
/*  153 */     this.mLocale = locale;
/*      */     
/*  155 */     init();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void init() {
/*  162 */     List<Rule> rulesList = parsePattern();
/*  163 */     this.mRules = rulesList.<Rule>toArray(new Rule[rulesList.size()]);
/*      */     
/*  165 */     int len = 0;
/*  166 */     for (int i = this.mRules.length; --i >= 0;) {
/*  167 */       len += this.mRules[i].estimateLength();
/*      */     }
/*      */     
/*  170 */     this.mMaxLengthEstimate = len;
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
/*      */   protected List<Rule> parsePattern() {
/*  182 */     DateFormatSymbols symbols = new DateFormatSymbols(this.mLocale);
/*  183 */     List<Rule> rules = new ArrayList<>();
/*      */     
/*  185 */     String[] ERAs = symbols.getEras();
/*  186 */     String[] months = symbols.getMonths();
/*  187 */     String[] shortMonths = symbols.getShortMonths();
/*  188 */     String[] weekdays = symbols.getWeekdays();
/*  189 */     String[] shortWeekdays = symbols.getShortWeekdays();
/*  190 */     String[] AmPmStrings = symbols.getAmPmStrings();
/*      */     
/*  192 */     int length = this.mPattern.length();
/*  193 */     int[] indexRef = new int[1];
/*      */     
/*  195 */     for (int i = 0; i < length; i++) {
/*  196 */       Rule rule; String sub; indexRef[0] = i;
/*  197 */       String token = parseToken(this.mPattern, indexRef);
/*  198 */       i = indexRef[0];
/*      */       
/*  200 */       int tokenLen = token.length();
/*  201 */       if (tokenLen == 0) {
/*      */         break;
/*      */       }
/*      */ 
/*      */       
/*  206 */       char c = token.charAt(0);
/*      */       
/*  208 */       switch (c) {
/*      */         case 'G':
/*  210 */           rule = new TextField(0, ERAs);
/*      */           break;
/*      */         case 'y':
/*  213 */           if (tokenLen == 2) {
/*  214 */             rule = TwoDigitYearField.INSTANCE; break;
/*      */           } 
/*  216 */           rule = selectNumberRule(1, (tokenLen < 4) ? 4 : tokenLen);
/*      */           break;
/*      */         
/*      */         case 'M':
/*  220 */           if (tokenLen >= 4) {
/*  221 */             rule = new TextField(2, months); break;
/*  222 */           }  if (tokenLen == 3) {
/*  223 */             rule = new TextField(2, shortMonths); break;
/*  224 */           }  if (tokenLen == 2) {
/*  225 */             rule = TwoDigitMonthField.INSTANCE; break;
/*      */           } 
/*  227 */           rule = UnpaddedMonthField.INSTANCE;
/*      */           break;
/*      */         
/*      */         case 'd':
/*  231 */           rule = selectNumberRule(5, tokenLen);
/*      */           break;
/*      */         case 'h':
/*  234 */           rule = new TwelveHourField(selectNumberRule(10, tokenLen));
/*      */           break;
/*      */         case 'H':
/*  237 */           rule = selectNumberRule(11, tokenLen);
/*      */           break;
/*      */         case 'm':
/*  240 */           rule = selectNumberRule(12, tokenLen);
/*      */           break;
/*      */         case 's':
/*  243 */           rule = selectNumberRule(13, tokenLen);
/*      */           break;
/*      */         case 'S':
/*  246 */           rule = selectNumberRule(14, tokenLen);
/*      */           break;
/*      */         case 'E':
/*  249 */           rule = new TextField(7, (tokenLen < 4) ? shortWeekdays : weekdays);
/*      */           break;
/*      */         case 'D':
/*  252 */           rule = selectNumberRule(6, tokenLen);
/*      */           break;
/*      */         case 'F':
/*  255 */           rule = selectNumberRule(8, tokenLen);
/*      */           break;
/*      */         case 'w':
/*  258 */           rule = selectNumberRule(3, tokenLen);
/*      */           break;
/*      */         case 'W':
/*  261 */           rule = selectNumberRule(4, tokenLen);
/*      */           break;
/*      */         case 'a':
/*  264 */           rule = new TextField(9, AmPmStrings);
/*      */           break;
/*      */         case 'k':
/*  267 */           rule = new TwentyFourHourField(selectNumberRule(11, tokenLen));
/*      */           break;
/*      */         case 'K':
/*  270 */           rule = selectNumberRule(10, tokenLen);
/*      */           break;
/*      */         case 'X':
/*  273 */           rule = Iso8601_Rule.getRule(tokenLen);
/*      */           break;
/*      */         case 'z':
/*  276 */           if (tokenLen >= 4) {
/*  277 */             rule = new TimeZoneNameRule(this.mTimeZone, this.mLocale, 1); break;
/*      */           } 
/*  279 */           rule = new TimeZoneNameRule(this.mTimeZone, this.mLocale, 0);
/*      */           break;
/*      */         
/*      */         case 'Z':
/*  283 */           if (tokenLen == 1) {
/*  284 */             rule = TimeZoneNumberRule.INSTANCE_NO_COLON; break;
/*  285 */           }  if (tokenLen == 2) {
/*  286 */             rule = TimeZoneNumberRule.INSTANCE_ISO_8601; break;
/*      */           } 
/*  288 */           rule = TimeZoneNumberRule.INSTANCE_COLON;
/*      */           break;
/*      */         
/*      */         case '\'':
/*  292 */           sub = token.substring(1);
/*  293 */           if (sub.length() == 1) {
/*  294 */             rule = new CharacterLiteral(sub.charAt(0)); break;
/*      */           } 
/*  296 */           rule = new StringLiteral(sub);
/*      */           break;
/*      */         
/*      */         default:
/*  300 */           throw new IllegalArgumentException("Illegal pattern component: " + token);
/*      */       } 
/*      */       
/*  303 */       rules.add(rule);
/*      */     } 
/*      */     
/*  306 */     return rules;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String parseToken(String pattern, int[] indexRef) {
/*  317 */     StringBuilder buf = new StringBuilder();
/*      */     
/*  319 */     int i = indexRef[0];
/*  320 */     int length = pattern.length();
/*      */     
/*  322 */     char c = pattern.charAt(i);
/*  323 */     if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
/*      */ 
/*      */       
/*  326 */       buf.append(c);
/*      */       
/*  328 */       while (i + 1 < length) {
/*  329 */         char peek = pattern.charAt(i + 1);
/*  330 */         if (peek == c) {
/*  331 */           buf.append(c);
/*  332 */           i++;
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/*  339 */       buf.append('\'');
/*      */       
/*  341 */       boolean inLiteral = false;
/*      */       
/*  343 */       for (; i < length; i++) {
/*  344 */         c = pattern.charAt(i);
/*      */         
/*  346 */         if (c == '\'')
/*  347 */         { if (i + 1 < length && pattern.charAt(i + 1) == '\'') {
/*      */             
/*  349 */             i++;
/*  350 */             buf.append(c);
/*      */           } else {
/*  352 */             inLiteral = !inLiteral;
/*      */           }  }
/*  354 */         else { if (!inLiteral && ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))) {
/*      */             
/*  356 */             i--;
/*      */             break;
/*      */           } 
/*  359 */           buf.append(c); }
/*      */       
/*      */       } 
/*      */     } 
/*      */     
/*  364 */     indexRef[0] = i;
/*  365 */     return buf.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected NumberRule selectNumberRule(int field, int padding) {
/*  376 */     switch (padding) {
/*      */       case 1:
/*  378 */         return new UnpaddedNumberField(field);
/*      */       case 2:
/*  380 */         return new TwoDigitNumberField(field);
/*      */     } 
/*  382 */     return new PaddedNumberField(field, padding);
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
/*      */   public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
/*  398 */     if (obj instanceof Date)
/*  399 */       return format((Date)obj, toAppendTo); 
/*  400 */     if (obj instanceof Calendar)
/*  401 */       return format((Calendar)obj, toAppendTo); 
/*  402 */     if (obj instanceof Long) {
/*  403 */       return format(((Long)obj).longValue(), toAppendTo);
/*      */     }
/*  405 */     throw new IllegalArgumentException("Unknown class: " + ((obj == null) ? "<null>" : obj
/*  406 */         .getClass().getName()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String format(long millis) {
/*  414 */     Calendar c = newCalendar();
/*  415 */     c.setTimeInMillis(millis);
/*  416 */     return applyRulesToString(c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String applyRulesToString(Calendar c) {
/*  425 */     return applyRules(c, new StringBuffer(this.mMaxLengthEstimate)).toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private GregorianCalendar newCalendar() {
/*  434 */     return new GregorianCalendar(this.mTimeZone, this.mLocale);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String format(Date date) {
/*  441 */     Calendar c = newCalendar();
/*  442 */     c.setTime(date);
/*  443 */     return applyRulesToString(c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String format(Calendar calendar) {
/*  450 */     return format(calendar, new StringBuffer(this.mMaxLengthEstimate)).toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuffer format(long millis, StringBuffer buf) {
/*  457 */     return format(new Date(millis), buf);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuffer format(Date date, StringBuffer buf) {
/*  464 */     Calendar c = newCalendar();
/*  465 */     c.setTime(date);
/*  466 */     return applyRules(c, buf);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StringBuffer format(Calendar calendar, StringBuffer buf) {
/*  473 */     return applyRules(calendar, buf);
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
/*      */   protected StringBuffer applyRules(Calendar calendar, StringBuffer buf) {
/*  485 */     for (Rule rule : this.mRules) {
/*  486 */       rule.appendTo(buf, calendar);
/*      */     }
/*  488 */     return buf;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getPattern() {
/*  497 */     return this.mPattern;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TimeZone getTimeZone() {
/*  504 */     return this.mTimeZone;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Locale getLocale() {
/*  511 */     return this.mLocale;
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
/*      */   public int getMaxLengthEstimate() {
/*  524 */     return this.mMaxLengthEstimate;
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
/*      */   public boolean equals(Object obj) {
/*  537 */     if (!(obj instanceof FastDatePrinter)) {
/*  538 */       return false;
/*      */     }
/*  540 */     FastDatePrinter other = (FastDatePrinter)obj;
/*  541 */     return (this.mPattern.equals(other.mPattern) && this.mTimeZone
/*  542 */       .equals(other.mTimeZone) && this.mLocale
/*  543 */       .equals(other.mLocale));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  553 */     return this.mPattern.hashCode() + 13 * (this.mTimeZone.hashCode() + 13 * this.mLocale.hashCode());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  563 */     return "FastDatePrinter[" + this.mPattern + "," + this.mLocale + "," + this.mTimeZone.getID() + "]";
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
/*      */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/*  577 */     in.defaultReadObject();
/*  578 */     init();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void appendDigits(StringBuffer buffer, int value) {
/*  588 */     buffer.append((char)(value / 10 + 48));
/*  589 */     buffer.append((char)(value % 10 + 48));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static interface Rule
/*      */   {
/*      */     int estimateLength();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void appendTo(StringBuffer param1StringBuffer, Calendar param1Calendar);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static interface NumberRule
/*      */     extends Rule
/*      */   {
/*      */     void appendTo(StringBuffer param1StringBuffer, int param1Int);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class CharacterLiteral
/*      */     implements Rule
/*      */   {
/*      */     private final char mValue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     CharacterLiteral(char value) {
/*  640 */       this.mValue = value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  647 */       return 1;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  654 */       buffer.append(this.mValue);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class StringLiteral
/*      */     implements Rule
/*      */   {
/*      */     private final String mValue;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     StringLiteral(String value) {
/*  671 */       this.mValue = value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  678 */       return this.mValue.length();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  685 */       buffer.append(this.mValue);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TextField
/*      */     implements Rule
/*      */   {
/*      */     private final int mField;
/*      */ 
/*      */ 
/*      */     
/*      */     private final String[] mValues;
/*      */ 
/*      */ 
/*      */     
/*      */     TextField(int field, String[] values) {
/*  704 */       this.mField = field;
/*  705 */       this.mValues = values;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  712 */       int max = 0;
/*  713 */       for (int i = this.mValues.length; --i >= 0; ) {
/*  714 */         int len = this.mValues[i].length();
/*  715 */         if (len > max) {
/*  716 */           max = len;
/*      */         }
/*      */       } 
/*  719 */       return max;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  726 */       buffer.append(this.mValues[calendar.get(this.mField)]);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class UnpaddedNumberField
/*      */     implements NumberRule
/*      */   {
/*      */     private final int mField;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     UnpaddedNumberField(int field) {
/*  742 */       this.mField = field;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  749 */       return 4;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  756 */       appendTo(buffer, calendar.get(this.mField));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(StringBuffer buffer, int value) {
/*  763 */       if (value < 10) {
/*  764 */         buffer.append((char)(value + 48));
/*  765 */       } else if (value < 100) {
/*  766 */         FastDatePrinter.appendDigits(buffer, value);
/*      */       } else {
/*  768 */         buffer.append(value);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class UnpaddedMonthField
/*      */     implements NumberRule
/*      */   {
/*  777 */     static final UnpaddedMonthField INSTANCE = new UnpaddedMonthField();
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
/*      */     public int estimateLength() {
/*  791 */       return 2;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  798 */       appendTo(buffer, calendar.get(2) + 1);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(StringBuffer buffer, int value) {
/*  805 */       if (value < 10) {
/*  806 */         buffer.append((char)(value + 48));
/*      */       } else {
/*  808 */         FastDatePrinter.appendDigits(buffer, value);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class PaddedNumberField
/*      */     implements NumberRule
/*      */   {
/*      */     private final int mField;
/*      */ 
/*      */     
/*      */     private final int mSize;
/*      */ 
/*      */ 
/*      */     
/*      */     PaddedNumberField(int field, int size) {
/*  827 */       if (size < 3)
/*      */       {
/*  829 */         throw new IllegalArgumentException();
/*      */       }
/*  831 */       this.mField = field;
/*  832 */       this.mSize = size;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  839 */       return this.mSize;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  846 */       appendTo(buffer, calendar.get(this.mField));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(StringBuffer buffer, int value) {
/*  854 */       for (int digit = 0; digit < this.mSize; digit++) {
/*  855 */         buffer.append('0');
/*      */       }
/*      */       
/*  858 */       int index = buffer.length();
/*  859 */       for (; value > 0; value /= 10) {
/*  860 */         buffer.setCharAt(--index, (char)(48 + value % 10));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TwoDigitNumberField
/*      */     implements NumberRule
/*      */   {
/*      */     private final int mField;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     TwoDigitNumberField(int field) {
/*  877 */       this.mField = field;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  884 */       return 2;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  891 */       appendTo(buffer, calendar.get(this.mField));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(StringBuffer buffer, int value) {
/*  898 */       if (value < 100) {
/*  899 */         FastDatePrinter.appendDigits(buffer, value);
/*      */       } else {
/*  901 */         buffer.append(value);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class TwoDigitYearField
/*      */     implements NumberRule
/*      */   {
/*  910 */     static final TwoDigitYearField INSTANCE = new TwoDigitYearField();
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
/*      */     public int estimateLength() {
/*  923 */       return 2;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  930 */       appendTo(buffer, calendar.get(1) % 100);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(StringBuffer buffer, int value) {
/*  937 */       FastDatePrinter.appendDigits(buffer, value);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class TwoDigitMonthField
/*      */     implements NumberRule
/*      */   {
/*  945 */     static final TwoDigitMonthField INSTANCE = new TwoDigitMonthField();
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
/*      */     public int estimateLength() {
/*  958 */       return 2;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/*  965 */       appendTo(buffer, calendar.get(2) + 1);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final void appendTo(StringBuffer buffer, int value) {
/*  972 */       FastDatePrinter.appendDigits(buffer, value);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TwelveHourField
/*      */     implements NumberRule
/*      */   {
/*      */     private final FastDatePrinter.NumberRule mRule;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     TwelveHourField(FastDatePrinter.NumberRule rule) {
/*  989 */       this.mRule = rule;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/*  996 */       return this.mRule.estimateLength();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/* 1003 */       int value = calendar.get(10);
/* 1004 */       if (value == 0) {
/* 1005 */         value = calendar.getLeastMaximum(10) + 1;
/*      */       }
/* 1007 */       this.mRule.appendTo(buffer, value);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, int value) {
/* 1014 */       this.mRule.appendTo(buffer, value);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TwentyFourHourField
/*      */     implements NumberRule
/*      */   {
/*      */     private final FastDatePrinter.NumberRule mRule;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     TwentyFourHourField(FastDatePrinter.NumberRule rule) {
/* 1031 */       this.mRule = rule;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1038 */       return this.mRule.estimateLength();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/* 1045 */       int value = calendar.get(11);
/* 1046 */       if (value == 0) {
/* 1047 */         value = calendar.getMaximum(11) + 1;
/*      */       }
/* 1049 */       this.mRule.appendTo(buffer, value);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, int value) {
/* 1056 */       this.mRule.appendTo(buffer, value);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1062 */   private static final ConcurrentMap<TimeZoneDisplayKey, String> cTimeZoneDisplayCache = new ConcurrentHashMap<>(7);
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
/*      */   static String getTimeZoneDisplay(TimeZone tz, boolean daylight, int style, Locale locale) {
/* 1074 */     TimeZoneDisplayKey key = new TimeZoneDisplayKey(tz, daylight, style, locale);
/* 1075 */     String value = cTimeZoneDisplayCache.get(key);
/* 1076 */     if (value == null) {
/*      */       
/* 1078 */       value = tz.getDisplayName(daylight, style, locale);
/* 1079 */       String prior = cTimeZoneDisplayCache.putIfAbsent(key, value);
/* 1080 */       if (prior != null) {
/* 1081 */         value = prior;
/*      */       }
/*      */     } 
/* 1084 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TimeZoneNameRule
/*      */     implements Rule
/*      */   {
/*      */     private final Locale mLocale;
/*      */ 
/*      */     
/*      */     private final int mStyle;
/*      */ 
/*      */     
/*      */     private final String mStandard;
/*      */     
/*      */     private final String mDaylight;
/*      */ 
/*      */     
/*      */     TimeZoneNameRule(TimeZone timeZone, Locale locale, int style) {
/* 1104 */       this.mLocale = locale;
/* 1105 */       this.mStyle = style;
/*      */       
/* 1107 */       this.mStandard = FastDatePrinter.getTimeZoneDisplay(timeZone, false, style, locale);
/* 1108 */       this.mDaylight = FastDatePrinter.getTimeZoneDisplay(timeZone, true, style, locale);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1118 */       return Math.max(this.mStandard.length(), this.mDaylight.length());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/* 1125 */       TimeZone zone = calendar.getTimeZone();
/* 1126 */       if (calendar.get(16) != 0) {
/* 1127 */         buffer.append(FastDatePrinter.getTimeZoneDisplay(zone, true, this.mStyle, this.mLocale));
/*      */       } else {
/* 1129 */         buffer.append(FastDatePrinter.getTimeZoneDisplay(zone, false, this.mStyle, this.mLocale));
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TimeZoneNumberRule
/*      */     implements Rule
/*      */   {
/* 1139 */     static final TimeZoneNumberRule INSTANCE_COLON = new TimeZoneNumberRule(true, false);
/* 1140 */     static final TimeZoneNumberRule INSTANCE_NO_COLON = new TimeZoneNumberRule(false, false);
/* 1141 */     static final TimeZoneNumberRule INSTANCE_ISO_8601 = new TimeZoneNumberRule(true, true);
/*      */ 
/*      */ 
/*      */     
/*      */     final boolean mColon;
/*      */ 
/*      */     
/*      */     final boolean mISO8601;
/*      */ 
/*      */ 
/*      */     
/*      */     TimeZoneNumberRule(boolean colon, boolean iso8601) {
/* 1153 */       this.mColon = colon;
/* 1154 */       this.mISO8601 = iso8601;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1161 */       return 5;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/* 1168 */       if (this.mISO8601 && calendar.getTimeZone().getID().equals("UTC")) {
/* 1169 */         buffer.append("Z");
/*      */         
/*      */         return;
/*      */       } 
/* 1173 */       int offset = calendar.get(15) + calendar.get(16);
/*      */       
/* 1175 */       if (offset < 0) {
/* 1176 */         buffer.append('-');
/* 1177 */         offset = -offset;
/*      */       } else {
/* 1179 */         buffer.append('+');
/*      */       } 
/*      */       
/* 1182 */       int hours = offset / 3600000;
/* 1183 */       FastDatePrinter.appendDigits(buffer, hours);
/*      */       
/* 1185 */       if (this.mColon) {
/* 1186 */         buffer.append(':');
/*      */       }
/*      */       
/* 1189 */       int minutes = offset / 60000 - 60 * hours;
/* 1190 */       FastDatePrinter.appendDigits(buffer, minutes);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class Iso8601_Rule
/*      */     implements Rule
/*      */   {
/* 1201 */     static final Iso8601_Rule ISO8601_HOURS = new Iso8601_Rule(3);
/*      */     
/* 1203 */     static final Iso8601_Rule ISO8601_HOURS_MINUTES = new Iso8601_Rule(5);
/*      */     
/* 1205 */     static final Iso8601_Rule ISO8601_HOURS_COLON_MINUTES = new Iso8601_Rule(6);
/*      */ 
/*      */ 
/*      */     
/*      */     final int length;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static Iso8601_Rule getRule(int tokenLen) {
/* 1215 */       switch (tokenLen) {
/*      */         case 1:
/* 1217 */           return ISO8601_HOURS;
/*      */         case 2:
/* 1219 */           return ISO8601_HOURS_MINUTES;
/*      */         case 3:
/* 1221 */           return ISO8601_HOURS_COLON_MINUTES;
/*      */       } 
/* 1223 */       throw new IllegalArgumentException("invalid number of X");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Iso8601_Rule(int length) {
/* 1235 */       this.length = length;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int estimateLength() {
/* 1242 */       return this.length;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void appendTo(StringBuffer buffer, Calendar calendar) {
/* 1249 */       int zoneOffset = calendar.get(15);
/* 1250 */       if (zoneOffset == 0) {
/* 1251 */         buffer.append("Z");
/*      */         
/*      */         return;
/*      */       } 
/* 1255 */       int offset = zoneOffset + calendar.get(16);
/*      */       
/* 1257 */       if (offset < 0) {
/* 1258 */         buffer.append('-');
/* 1259 */         offset = -offset;
/*      */       } else {
/* 1261 */         buffer.append('+');
/*      */       } 
/*      */       
/* 1264 */       int hours = offset / 3600000;
/* 1265 */       FastDatePrinter.appendDigits(buffer, hours);
/*      */       
/* 1267 */       if (this.length < 5) {
/*      */         return;
/*      */       }
/*      */       
/* 1271 */       if (this.length == 6) {
/* 1272 */         buffer.append(':');
/*      */       }
/*      */       
/* 1275 */       int minutes = offset / 60000 - 60 * hours;
/* 1276 */       FastDatePrinter.appendDigits(buffer, minutes);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TimeZoneDisplayKey
/*      */   {
/*      */     private final TimeZone mTimeZone;
/*      */ 
/*      */ 
/*      */     
/*      */     private final int mStyle;
/*      */ 
/*      */ 
/*      */     
/*      */     private final Locale mLocale;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     TimeZoneDisplayKey(TimeZone timeZone, boolean daylight, int style, Locale locale) {
/* 1299 */       this.mTimeZone = timeZone;
/* 1300 */       if (daylight) {
/* 1301 */         this.mStyle = style | Integer.MIN_VALUE;
/*      */       } else {
/* 1303 */         this.mStyle = style;
/*      */       } 
/* 1305 */       this.mLocale = locale;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1313 */       return (this.mStyle * 31 + this.mLocale.hashCode()) * 31 + this.mTimeZone.hashCode();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/* 1321 */       if (this == obj) {
/* 1322 */         return true;
/*      */       }
/* 1324 */       if (obj instanceof TimeZoneDisplayKey) {
/* 1325 */         TimeZoneDisplayKey other = (TimeZoneDisplayKey)obj;
/* 1326 */         return (this.mTimeZone
/* 1327 */           .equals(other.mTimeZone) && this.mStyle == other.mStyle && this.mLocale
/*      */           
/* 1329 */           .equals(other.mLocale));
/*      */       } 
/* 1331 */       return false;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\sqlite\date\FastDatePrinter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */