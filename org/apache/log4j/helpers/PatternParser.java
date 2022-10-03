/*     */ package org.apache.log4j.helpers;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.Map;
/*     */ import org.apache.log4j.Layout;
/*     */ import org.apache.log4j.spi.LocationInfo;
/*     */ import org.apache.log4j.spi.LoggingEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PatternParser
/*     */ {
/*     */   private static final char ESCAPE_CHAR = '%';
/*     */   private static final int LITERAL_STATE = 0;
/*     */   private static final int CONVERTER_STATE = 1;
/*     */   private static final int DOT_STATE = 3;
/*     */   private static final int MIN_STATE = 4;
/*     */   private static final int MAX_STATE = 5;
/*     */   static final int FULL_LOCATION_CONVERTER = 1000;
/*     */   static final int METHOD_LOCATION_CONVERTER = 1001;
/*     */   static final int CLASS_LOCATION_CONVERTER = 1002;
/*     */   static final int LINE_LOCATION_CONVERTER = 1003;
/*     */   static final int FILE_LOCATION_CONVERTER = 1004;
/*     */   static final int RELATIVE_TIME_CONVERTER = 2000;
/*     */   static final int THREAD_CONVERTER = 2001;
/*     */   static final int LEVEL_CONVERTER = 2002;
/*     */   static final int NDC_CONVERTER = 2003;
/*     */   static final int MESSAGE_CONVERTER = 2004;
/*     */   int state;
/*  68 */   protected StringBuffer currentLiteral = new StringBuffer(32);
/*     */   protected int patternLength;
/*     */   protected int i;
/*     */   PatternConverter head;
/*     */   PatternConverter tail;
/*  73 */   protected FormattingInfo formattingInfo = new FormattingInfo();
/*     */   
/*     */   protected String pattern;
/*     */   
/*     */   public PatternParser(String pattern) {
/*  78 */     this.pattern = pattern;
/*  79 */     this.patternLength = pattern.length();
/*  80 */     this.state = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private void addToList(PatternConverter pc) {
/*  85 */     if (this.head == null) {
/*  86 */       this.head = this.tail = pc;
/*     */     } else {
/*  88 */       this.tail.next = pc;
/*  89 */       this.tail = pc;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected String extractOption() {
/*  95 */     if (this.i < this.patternLength && this.pattern.charAt(this.i) == '{') {
/*  96 */       int end = this.pattern.indexOf('}', this.i);
/*  97 */       if (end > this.i) {
/*  98 */         String r = this.pattern.substring(this.i + 1, end);
/*  99 */         this.i = end + 1;
/* 100 */         return r;
/*     */       } 
/*     */     } 
/* 103 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int extractPrecisionOption() {
/* 112 */     String opt = extractOption();
/* 113 */     int r = 0;
/* 114 */     if (opt != null) {
/*     */       try {
/* 116 */         r = Integer.parseInt(opt);
/* 117 */         if (r <= 0) {
/* 118 */           LogLog.error("Precision option (" + opt + ") isn't a positive integer.");
/*     */           
/* 120 */           r = 0;
/*     */         }
/*     */       
/* 123 */       } catch (NumberFormatException e) {
/* 124 */         LogLog.error("Category option \"" + opt + "\" not a decimal integer.", e);
/*     */       } 
/*     */     }
/* 127 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PatternConverter parse() {
/* 133 */     this.i = 0;
/* 134 */     while (this.i < this.patternLength) {
/* 135 */       char c = this.pattern.charAt(this.i++);
/* 136 */       switch (this.state) {
/*     */         
/*     */         case 0:
/* 139 */           if (this.i == this.patternLength) {
/* 140 */             this.currentLiteral.append(c);
/*     */             continue;
/*     */           } 
/* 143 */           if (c == '%') {
/*     */             
/* 145 */             switch (this.pattern.charAt(this.i)) {
/*     */               case '%':
/* 147 */                 this.currentLiteral.append(c);
/* 148 */                 this.i++;
/*     */                 continue;
/*     */               case 'n':
/* 151 */                 this.currentLiteral.append(Layout.LINE_SEP);
/* 152 */                 this.i++;
/*     */                 continue;
/*     */             } 
/* 155 */             if (this.currentLiteral.length() != 0) {
/* 156 */               addToList(new LiteralPatternConverter(this.currentLiteral.toString()));
/*     */             }
/*     */ 
/*     */ 
/*     */             
/* 161 */             this.currentLiteral.setLength(0);
/* 162 */             this.currentLiteral.append(c);
/* 163 */             this.state = 1;
/* 164 */             this.formattingInfo.reset();
/*     */             
/*     */             continue;
/*     */           } 
/* 168 */           this.currentLiteral.append(c);
/*     */ 
/*     */         
/*     */         case 1:
/* 172 */           this.currentLiteral.append(c);
/* 173 */           switch (c) {
/*     */             case '-':
/* 175 */               this.formattingInfo.leftAlign = true;
/*     */               continue;
/*     */             case '.':
/* 178 */               this.state = 3;
/*     */               continue;
/*     */           } 
/* 181 */           if (c >= '0' && c <= '9') {
/* 182 */             this.formattingInfo.min = c - 48;
/* 183 */             this.state = 4;
/*     */             continue;
/*     */           } 
/* 186 */           finalizeConverter(c);
/*     */ 
/*     */         
/*     */         case 4:
/* 190 */           this.currentLiteral.append(c);
/* 191 */           if (c >= '0' && c <= '9') {
/* 192 */             this.formattingInfo.min = this.formattingInfo.min * 10 + c - 48; continue;
/* 193 */           }  if (c == '.') {
/* 194 */             this.state = 3; continue;
/*     */           } 
/* 196 */           finalizeConverter(c);
/*     */ 
/*     */         
/*     */         case 3:
/* 200 */           this.currentLiteral.append(c);
/* 201 */           if (c >= '0' && c <= '9') {
/* 202 */             this.formattingInfo.max = c - 48;
/* 203 */             this.state = 5;
/*     */             continue;
/*     */           } 
/* 206 */           LogLog.error("Error occured in position " + this.i + ".\n Was expecting digit, instead got char \"" + c + "\".");
/*     */           
/* 208 */           this.state = 0;
/*     */ 
/*     */         
/*     */         case 5:
/* 212 */           this.currentLiteral.append(c);
/* 213 */           if (c >= '0' && c <= '9') {
/* 214 */             this.formattingInfo.max = this.formattingInfo.max * 10 + c - 48; continue;
/*     */           } 
/* 216 */           finalizeConverter(c);
/* 217 */           this.state = 0;
/*     */       } 
/*     */ 
/*     */     
/*     */     } 
/* 222 */     if (this.currentLiteral.length() != 0) {
/* 223 */       addToList(new LiteralPatternConverter(this.currentLiteral.toString()));
/*     */     }
/*     */     
/* 226 */     return this.head;
/*     */   } protected void finalizeConverter(char c) {
/*     */     String dateFormatStr;
/*     */     DateFormat dateFormat;
/*     */     String dOpt, xOpt;
/* 231 */     PatternConverter pc = null;
/* 232 */     switch (c) {
/*     */       case 'c':
/* 234 */         pc = new CategoryPatternConverter(this.formattingInfo, extractPrecisionOption());
/*     */ 
/*     */ 
/*     */         
/* 238 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */       case 'C':
/* 241 */         pc = new ClassNamePatternConverter(this.formattingInfo, extractPrecisionOption());
/*     */ 
/*     */ 
/*     */         
/* 245 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */       case 'd':
/* 248 */         dateFormatStr = "ISO8601";
/*     */         
/* 250 */         dOpt = extractOption();
/* 251 */         if (dOpt != null) {
/* 252 */           dateFormatStr = dOpt;
/*     */         }
/* 254 */         if (dateFormatStr.equalsIgnoreCase("ISO8601")) {
/*     */           
/* 256 */           dateFormat = new ISO8601DateFormat();
/* 257 */         } else if (dateFormatStr.equalsIgnoreCase("ABSOLUTE")) {
/*     */           
/* 259 */           dateFormat = new AbsoluteTimeDateFormat();
/* 260 */         } else if (dateFormatStr.equalsIgnoreCase("DATE")) {
/*     */           
/* 262 */           dateFormat = new DateTimeDateFormat();
/*     */         } else {
/*     */           try {
/* 265 */             dateFormat = new SimpleDateFormat(dateFormatStr);
/*     */           }
/* 267 */           catch (IllegalArgumentException e) {
/* 268 */             LogLog.error("Could not instantiate SimpleDateFormat with " + dateFormatStr, e);
/*     */             
/* 270 */             dateFormat = (DateFormat)OptionConverter.instantiateByClassName("org.apache.log4j.helpers.ISO8601DateFormat", DateFormat.class, null);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 275 */         pc = new DatePatternConverter(this.formattingInfo, dateFormat);
/*     */ 
/*     */         
/* 278 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */       case 'F':
/* 281 */         pc = new LocationPatternConverter(this.formattingInfo, 1004);
/*     */ 
/*     */ 
/*     */         
/* 285 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */       case 'l':
/* 288 */         pc = new LocationPatternConverter(this.formattingInfo, 1000);
/*     */ 
/*     */ 
/*     */         
/* 292 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */       case 'L':
/* 295 */         pc = new LocationPatternConverter(this.formattingInfo, 1003);
/*     */ 
/*     */ 
/*     */         
/* 299 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */       case 'm':
/* 302 */         pc = new BasicPatternConverter(this.formattingInfo, 2004);
/*     */ 
/*     */         
/* 305 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */       case 'M':
/* 308 */         pc = new LocationPatternConverter(this.formattingInfo, 1001);
/*     */ 
/*     */ 
/*     */         
/* 312 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */       case 'p':
/* 315 */         pc = new BasicPatternConverter(this.formattingInfo, 2002);
/*     */ 
/*     */         
/* 318 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */       case 'r':
/* 321 */         pc = new BasicPatternConverter(this.formattingInfo, 2000);
/*     */ 
/*     */ 
/*     */         
/* 325 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */       case 't':
/* 328 */         pc = new BasicPatternConverter(this.formattingInfo, 2001);
/*     */ 
/*     */         
/* 331 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 'x':
/* 348 */         pc = new BasicPatternConverter(this.formattingInfo, 2003);
/*     */         
/* 350 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */       case 'X':
/* 353 */         xOpt = extractOption();
/* 354 */         pc = new MDCPatternConverter(this.formattingInfo, xOpt);
/* 355 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */       default:
/* 358 */         LogLog.error("Unexpected char [" + c + "] at position " + this.i + " in conversion patterrn.");
/*     */         
/* 360 */         pc = new LiteralPatternConverter(this.currentLiteral.toString());
/* 361 */         this.currentLiteral.setLength(0);
/*     */         break;
/*     */     } 
/* 364 */     addConverter(pc);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addConverter(PatternConverter pc) {
/* 369 */     this.currentLiteral.setLength(0);
/*     */     
/* 371 */     addToList(pc);
/*     */     
/* 373 */     this.state = 0;
/*     */     
/* 375 */     this.formattingInfo.reset();
/*     */   }
/*     */ 
/*     */   
/*     */   private static class BasicPatternConverter
/*     */     extends PatternConverter
/*     */   {
/*     */     int type;
/*     */ 
/*     */     
/*     */     BasicPatternConverter(FormattingInfo formattingInfo, int type) {
/* 386 */       super(formattingInfo);
/* 387 */       this.type = type;
/*     */     }
/*     */ 
/*     */     
/*     */     public String convert(LoggingEvent event) {
/* 392 */       switch (this.type) {
/*     */         case 2000:
/* 394 */           return Long.toString(event.timeStamp - LoggingEvent.getStartTime());
/*     */         case 2001:
/* 396 */           return event.getThreadName();
/*     */         case 2002:
/* 398 */           return event.getLevel().toString();
/*     */         case 2003:
/* 400 */           return event.getNDC();
/*     */         case 2004:
/* 402 */           return event.getRenderedMessage();
/*     */       } 
/* 404 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LiteralPatternConverter
/*     */     extends PatternConverter {
/*     */     private String literal;
/*     */     
/*     */     LiteralPatternConverter(String value) {
/* 413 */       this.literal = value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final void format(StringBuffer sbuf, LoggingEvent event) {
/* 419 */       sbuf.append(this.literal);
/*     */     }
/*     */ 
/*     */     
/*     */     public String convert(LoggingEvent event) {
/* 424 */       return this.literal;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DatePatternConverter extends PatternConverter {
/*     */     private DateFormat df;
/*     */     private Date date;
/*     */     
/*     */     DatePatternConverter(FormattingInfo formattingInfo, DateFormat df) {
/* 433 */       super(formattingInfo);
/* 434 */       this.date = new Date();
/* 435 */       this.df = df;
/*     */     }
/*     */ 
/*     */     
/*     */     public String convert(LoggingEvent event) {
/* 440 */       this.date.setTime(event.timeStamp);
/* 441 */       String converted = null;
/*     */       try {
/* 443 */         converted = this.df.format(this.date);
/*     */       }
/* 445 */       catch (Exception ex) {
/* 446 */         LogLog.error("Error occured while converting date.", ex);
/*     */       } 
/* 448 */       return converted;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class MDCPatternConverter extends PatternConverter {
/*     */     private String key;
/*     */     
/*     */     MDCPatternConverter(FormattingInfo formattingInfo, String key) {
/* 456 */       super(formattingInfo);
/* 457 */       this.key = key;
/*     */     }
/*     */ 
/*     */     
/*     */     public String convert(LoggingEvent event) {
/* 462 */       if (this.key == null) {
/* 463 */         StringBuffer buf = new StringBuffer("{");
/* 464 */         Map properties = event.getProperties();
/* 465 */         if (properties.size() > 0) {
/* 466 */           Object[] keys = properties.keySet().toArray();
/* 467 */           Arrays.sort(keys);
/* 468 */           for (int i = 0; i < keys.length; i++) {
/* 469 */             buf.append('{');
/* 470 */             buf.append(keys[i]);
/* 471 */             buf.append(',');
/* 472 */             buf.append(properties.get(keys[i]));
/* 473 */             buf.append('}');
/*     */           } 
/*     */         } 
/* 476 */         buf.append('}');
/* 477 */         return buf.toString();
/*     */       } 
/* 479 */       Object val = event.getMDC(this.key);
/* 480 */       if (val == null) {
/* 481 */         return null;
/*     */       }
/* 483 */       return val.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private class LocationPatternConverter
/*     */     extends PatternConverter
/*     */   {
/*     */     int type;
/*     */     private final PatternParser this$0;
/*     */     
/*     */     LocationPatternConverter(FormattingInfo formattingInfo, int type) {
/* 494 */       super(formattingInfo);
/* 495 */       this.type = type;
/*     */     }
/*     */ 
/*     */     
/*     */     public String convert(LoggingEvent event) {
/* 500 */       LocationInfo locationInfo = event.getLocationInformation();
/* 501 */       switch (this.type) {
/*     */         case 1000:
/* 503 */           return locationInfo.fullInfo;
/*     */         case 1001:
/* 505 */           return locationInfo.getMethodName();
/*     */         case 1003:
/* 507 */           return locationInfo.getLineNumber();
/*     */         case 1004:
/* 509 */           return locationInfo.getFileName();
/* 510 */       }  return null;
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class NamedPatternConverter
/*     */     extends PatternConverter {
/*     */     int precision;
/*     */     
/*     */     NamedPatternConverter(FormattingInfo formattingInfo, int precision) {
/* 519 */       super(formattingInfo);
/* 520 */       this.precision = precision;
/*     */     }
/*     */ 
/*     */     
/*     */     abstract String getFullyQualifiedName(LoggingEvent param1LoggingEvent);
/*     */ 
/*     */     
/*     */     public String convert(LoggingEvent event) {
/* 528 */       String n = getFullyQualifiedName(event);
/* 529 */       if (this.precision <= 0) {
/* 530 */         return n;
/*     */       }
/* 532 */       int len = n.length();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 537 */       int end = len - 1;
/* 538 */       for (int i = this.precision; i > 0; i--) {
/* 539 */         end = n.lastIndexOf('.', end - 1);
/* 540 */         if (end == -1)
/* 541 */           return n; 
/*     */       } 
/* 543 */       return n.substring(end + 1, len);
/*     */     }
/*     */   }
/*     */   
/*     */   private class ClassNamePatternConverter extends NamedPatternConverter {
/*     */     private final PatternParser this$0;
/*     */     
/*     */     ClassNamePatternConverter(FormattingInfo formattingInfo, int precision) {
/* 551 */       super(formattingInfo, precision);
/*     */     }
/*     */     
/*     */     String getFullyQualifiedName(LoggingEvent event) {
/* 555 */       return event.getLocationInformation().getClassName();
/*     */     } }
/*     */   
/*     */   private class CategoryPatternConverter extends NamedPatternConverter {
/*     */     private final PatternParser this$0;
/*     */     
/*     */     CategoryPatternConverter(FormattingInfo formattingInfo, int precision) {
/* 562 */       super(formattingInfo, precision);
/*     */     }
/*     */     
/*     */     String getFullyQualifiedName(LoggingEvent event) {
/* 566 */       return event.getLoggerName();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\helpers\PatternParser.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */