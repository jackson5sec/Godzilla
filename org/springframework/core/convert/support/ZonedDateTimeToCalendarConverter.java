/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.time.ZonedDateTime;
/*    */ import java.util.Calendar;
/*    */ import java.util.GregorianCalendar;
/*    */ import org.springframework.core.convert.converter.Converter;
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
/*    */ 
/*    */ 
/*    */ final class ZonedDateTimeToCalendarConverter
/*    */   implements Converter<ZonedDateTime, Calendar>
/*    */ {
/*    */   public Calendar convert(ZonedDateTime source) {
/* 42 */     return GregorianCalendar.from(source);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\ZonedDateTimeToCalendarConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */