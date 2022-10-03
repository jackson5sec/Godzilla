/*    */ package org.apache.log4j.helpers;
/*    */ 
/*    */ import java.text.DateFormatSymbols;
/*    */ import java.text.FieldPosition;
/*    */ import java.text.ParsePosition;
/*    */ import java.util.Calendar;
/*    */ import java.util.Date;
/*    */ import java.util.TimeZone;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DateTimeDateFormat
/*    */   extends AbsoluteTimeDateFormat
/*    */ {
/*    */   private static final long serialVersionUID = 5547637772208514971L;
/* 42 */   String[] shortMonths = (new DateFormatSymbols()).getShortMonths();
/*    */   
/*    */   public DateTimeDateFormat() {}
/*    */   
/*    */   public DateTimeDateFormat(TimeZone timeZone) {
/* 47 */     this();
/* 48 */     setCalendar(Calendar.getInstance(timeZone));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StringBuffer format(Date date, StringBuffer sbuf, FieldPosition fieldPosition) {
/* 61 */     this.calendar.setTime(date);
/*    */     
/* 63 */     int day = this.calendar.get(5);
/* 64 */     if (day < 10)
/* 65 */       sbuf.append('0'); 
/* 66 */     sbuf.append(day);
/* 67 */     sbuf.append(' ');
/* 68 */     sbuf.append(this.shortMonths[this.calendar.get(2)]);
/* 69 */     sbuf.append(' ');
/*    */     
/* 71 */     int year = this.calendar.get(1);
/* 72 */     sbuf.append(year);
/* 73 */     sbuf.append(' ');
/*    */     
/* 75 */     return super.format(date, sbuf, fieldPosition);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Date parse(String s, ParsePosition pos) {
/* 83 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\helpers\DateTimeDateFormat.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */