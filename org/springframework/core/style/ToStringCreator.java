/*     */ package org.springframework.core.style;
/*     */ 
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ToStringCreator
/*     */ {
/*  36 */   private static final ToStringStyler DEFAULT_TO_STRING_STYLER = new DefaultToStringStyler(StylerUtils.DEFAULT_VALUE_STYLER);
/*     */ 
/*     */ 
/*     */   
/*  40 */   private final StringBuilder buffer = new StringBuilder(256);
/*     */ 
/*     */   
/*     */   private final ToStringStyler styler;
/*     */ 
/*     */   
/*     */   private final Object object;
/*     */ 
/*     */   
/*     */   private boolean styledFirstField;
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringCreator(Object obj) {
/*  54 */     this(obj, (ToStringStyler)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringCreator(Object obj, @Nullable ValueStyler styler) {
/*  63 */     this(obj, new DefaultToStringStyler((styler != null) ? styler : StylerUtils.DEFAULT_VALUE_STYLER));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringCreator(Object obj, @Nullable ToStringStyler styler) {
/*  72 */     Assert.notNull(obj, "The object to be styled must not be null");
/*  73 */     this.object = obj;
/*  74 */     this.styler = (styler != null) ? styler : DEFAULT_TO_STRING_STYLER;
/*  75 */     this.styler.styleStart(this.buffer, this.object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringCreator append(String fieldName, byte value) {
/*  86 */     return append(fieldName, Byte.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringCreator append(String fieldName, short value) {
/*  96 */     return append(fieldName, Short.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringCreator append(String fieldName, int value) {
/* 106 */     return append(fieldName, Integer.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringCreator append(String fieldName, long value) {
/* 116 */     return append(fieldName, Long.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringCreator append(String fieldName, float value) {
/* 126 */     return append(fieldName, Float.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringCreator append(String fieldName, double value) {
/* 136 */     return append(fieldName, Double.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringCreator append(String fieldName, boolean value) {
/* 146 */     return append(fieldName, Boolean.valueOf(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringCreator append(String fieldName, @Nullable Object value) {
/* 156 */     printFieldSeparatorIfNecessary();
/* 157 */     this.styler.styleField(this.buffer, fieldName, value);
/* 158 */     return this;
/*     */   }
/*     */   
/*     */   private void printFieldSeparatorIfNecessary() {
/* 162 */     if (this.styledFirstField) {
/* 163 */       this.styler.styleFieldSeparator(this.buffer);
/*     */     } else {
/*     */       
/* 166 */       this.styledFirstField = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ToStringCreator append(Object value) {
/* 176 */     this.styler.styleValue(this.buffer, value);
/* 177 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 186 */     this.styler.styleEnd(this.buffer, this.object);
/* 187 */     return this.buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\style\ToStringCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */