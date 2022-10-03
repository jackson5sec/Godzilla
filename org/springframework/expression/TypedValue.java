/*     */ package org.springframework.expression;
/*     */ 
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class TypedValue
/*     */ {
/*  37 */   public static final TypedValue NULL = new TypedValue(null);
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private final Object value;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private TypeDescriptor typeDescriptor;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue(@Nullable Object value) {
/*  53 */     this.value = value;
/*  54 */     this.typeDescriptor = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue(@Nullable Object value, @Nullable TypeDescriptor typeDescriptor) {
/*  64 */     this.value = value;
/*  65 */     this.typeDescriptor = typeDescriptor;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getValue() {
/*  71 */     return this.value;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public TypeDescriptor getTypeDescriptor() {
/*  76 */     if (this.typeDescriptor == null && this.value != null) {
/*  77 */       this.typeDescriptor = TypeDescriptor.forObject(this.value);
/*     */     }
/*  79 */     return this.typeDescriptor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/*  85 */     if (this == other) {
/*  86 */       return true;
/*     */     }
/*  88 */     if (!(other instanceof TypedValue)) {
/*  89 */       return false;
/*     */     }
/*  91 */     TypedValue otherTv = (TypedValue)other;
/*     */     
/*  93 */     return (ObjectUtils.nullSafeEquals(this.value, otherTv.value) && ((this.typeDescriptor == null && otherTv.typeDescriptor == null) || 
/*     */       
/*  95 */       ObjectUtils.nullSafeEquals(getTypeDescriptor(), otherTv.getTypeDescriptor())));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 100 */     return ObjectUtils.nullSafeHashCode(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 105 */     return "TypedValue: '" + this.value + "' of [" + getTypeDescriptor() + "]";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\TypedValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */