/*     */ package com.jgoodies.common.format;
/*     */ 
/*     */ import com.jgoodies.common.base.Objects;
/*     */ import com.jgoodies.common.base.Strings;
/*     */ import java.text.DateFormat;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.Date;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class EmptyDateFormat
/*     */   extends AbstractWrappedDateFormat
/*     */ {
/*     */   private final Date emptyValue;
/*     */   
/*     */   public EmptyDateFormat(DateFormat delegate) {
/*  83 */     this(delegate, null);
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
/*     */   public EmptyDateFormat(DateFormat delegate, Date emptyValue) {
/*  98 */     super(delegate);
/*  99 */     this.emptyValue = emptyValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
/* 108 */     return Objects.equals(date, this.emptyValue) ? toAppendTo : this.delegate.format(date, toAppendTo, pos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Date parse(String source, ParsePosition pos) {
/* 116 */     if (Strings.isBlank(source)) {
/*     */ 
/*     */       
/* 119 */       pos.setIndex(1);
/* 120 */       return this.emptyValue;
/*     */     } 
/* 122 */     return this.delegate.parse(source, pos);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\common\format\EmptyDateFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */