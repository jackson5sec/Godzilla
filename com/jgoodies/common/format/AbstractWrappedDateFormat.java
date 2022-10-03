/*     */ package com.jgoodies.common.format;
/*     */ 
/*     */ import com.jgoodies.common.base.Preconditions;
/*     */ import java.text.AttributedCharacterIterator;
/*     */ import java.text.DateFormat;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.NumberFormat;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractWrappedDateFormat
/*     */   extends DateFormat
/*     */ {
/*     */   protected final DateFormat delegate;
/*     */   
/*     */   public AbstractWrappedDateFormat(DateFormat delegate) {
/*  80 */     this.delegate = (DateFormat)Preconditions.checkNotNull(delegate, "The %1$s must not be null.", new Object[] { "delegate format" });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract StringBuffer format(Date paramDate, StringBuffer paramStringBuffer, FieldPosition paramFieldPosition);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract Date parse(String paramString, ParsePosition paramParsePosition);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Calendar getCalendar() {
/*  99 */     return this.delegate.getCalendar();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCalendar(Calendar newCalendar) {
/* 105 */     this.delegate.setCalendar(newCalendar);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public NumberFormat getNumberFormat() {
/* 111 */     return this.delegate.getNumberFormat();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNumberFormat(NumberFormat newNumberFormat) {
/* 117 */     this.delegate.setNumberFormat(newNumberFormat);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TimeZone getTimeZone() {
/* 123 */     return this.delegate.getTimeZone();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeZone(TimeZone zone) {
/* 129 */     this.delegate.setTimeZone(zone);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLenient() {
/* 135 */     return this.delegate.isLenient();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLenient(boolean lenient) {
/* 141 */     this.delegate.setLenient(lenient);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
/* 147 */     return this.delegate.formatToCharacterIterator(obj);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\common\format\AbstractWrappedDateFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */