/*     */ package com.jgoodies.common.format;
/*     */ 
/*     */ import com.jgoodies.common.base.Objects;
/*     */ import com.jgoodies.common.base.Preconditions;
/*     */ import com.jgoodies.common.base.Strings;
/*     */ import java.text.AttributedCharacterIterator;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.Format;
/*     */ import java.text.ParseException;
/*     */ import java.text.ParsePosition;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EmptyFormat
/*     */   extends Format
/*     */ {
/*     */   private final Format delegate;
/*     */   private final Object emptyValue;
/*     */   
/*     */   public EmptyFormat(Format delegate) {
/*  90 */     this(delegate, null);
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
/*     */   public EmptyFormat(Format delegate, Object emptyValue) {
/* 105 */     this.delegate = (Format)Preconditions.checkNotNull(delegate, "The %1$s must not be null.", new Object[] { "delegate format" });
/* 106 */     this.emptyValue = emptyValue;
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
/*     */   public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
/* 122 */     return Objects.equals(obj, this.emptyValue) ? toAppendTo : this.delegate.format(obj, toAppendTo, pos);
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
/*     */   public Object parseObject(String source) throws ParseException {
/* 136 */     return Strings.isBlank(source) ? this.emptyValue : super.parseObject(source);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Object parseObject(String source, ParsePosition pos) {
/* 144 */     return this.delegate.parseObject(source, pos);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final AttributedCharacterIterator formatToCharacterIterator(Object obj) {
/* 150 */     return this.delegate.formatToCharacterIterator(obj);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jgoodies\common\format\EmptyFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */