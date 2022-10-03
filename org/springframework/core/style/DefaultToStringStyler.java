/*     */ package org.springframework.core.style;
/*     */ 
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultToStringStyler
/*     */   implements ToStringStyler
/*     */ {
/*     */   private final ValueStyler valueStyler;
/*     */   
/*     */   public DefaultToStringStyler(ValueStyler valueStyler) {
/*  44 */     Assert.notNull(valueStyler, "ValueStyler must not be null");
/*  45 */     this.valueStyler = valueStyler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ValueStyler getValueStyler() {
/*  52 */     return this.valueStyler;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void styleStart(StringBuilder buffer, Object obj) {
/*  58 */     if (!obj.getClass().isArray()) {
/*  59 */       buffer.append('[').append(ClassUtils.getShortName(obj.getClass()));
/*  60 */       styleIdentityHashCode(buffer, obj);
/*     */     } else {
/*     */       
/*  63 */       buffer.append('[');
/*  64 */       styleIdentityHashCode(buffer, obj);
/*  65 */       buffer.append(' ');
/*  66 */       styleValue(buffer, obj);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void styleIdentityHashCode(StringBuilder buffer, Object obj) {
/*  71 */     buffer.append('@');
/*  72 */     buffer.append(ObjectUtils.getIdentityHexString(obj));
/*     */   }
/*     */ 
/*     */   
/*     */   public void styleEnd(StringBuilder buffer, Object o) {
/*  77 */     buffer.append(']');
/*     */   }
/*     */ 
/*     */   
/*     */   public void styleField(StringBuilder buffer, String fieldName, @Nullable Object value) {
/*  82 */     styleFieldStart(buffer, fieldName);
/*  83 */     styleValue(buffer, value);
/*  84 */     styleFieldEnd(buffer, fieldName);
/*     */   }
/*     */   
/*     */   protected void styleFieldStart(StringBuilder buffer, String fieldName) {
/*  88 */     buffer.append(' ').append(fieldName).append(" = ");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void styleFieldEnd(StringBuilder buffer, String fieldName) {}
/*     */ 
/*     */   
/*     */   public void styleValue(StringBuilder buffer, @Nullable Object value) {
/*  96 */     buffer.append(this.valueStyler.style(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void styleFieldSeparator(StringBuilder buffer) {
/* 101 */     buffer.append(',');
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\style\DefaultToStringStyler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */