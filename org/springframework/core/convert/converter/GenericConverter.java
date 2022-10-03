/*     */ package org.springframework.core.convert.converter;
/*     */ 
/*     */ import java.util.Set;
/*     */ import org.springframework.core.convert.TypeDescriptor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface GenericConverter
/*     */ {
/*     */   @Nullable
/*     */   Set<ConvertiblePair> getConvertibleTypes();
/*     */   
/*     */   @Nullable
/*     */   Object convert(@Nullable Object paramObject, TypeDescriptor paramTypeDescriptor1, TypeDescriptor paramTypeDescriptor2);
/*     */   
/*     */   public static final class ConvertiblePair
/*     */   {
/*     */     private final Class<?> sourceType;
/*     */     private final Class<?> targetType;
/*     */     
/*     */     public ConvertiblePair(Class<?> sourceType, Class<?> targetType) {
/*  85 */       Assert.notNull(sourceType, "Source type must not be null");
/*  86 */       Assert.notNull(targetType, "Target type must not be null");
/*  87 */       this.sourceType = sourceType;
/*  88 */       this.targetType = targetType;
/*     */     }
/*     */     
/*     */     public Class<?> getSourceType() {
/*  92 */       return this.sourceType;
/*     */     }
/*     */     
/*     */     public Class<?> getTargetType() {
/*  96 */       return this.targetType;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object other) {
/* 101 */       if (this == other) {
/* 102 */         return true;
/*     */       }
/* 104 */       if (other == null || other.getClass() != ConvertiblePair.class) {
/* 105 */         return false;
/*     */       }
/* 107 */       ConvertiblePair otherPair = (ConvertiblePair)other;
/* 108 */       return (this.sourceType == otherPair.sourceType && this.targetType == otherPair.targetType);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 113 */       return this.sourceType.hashCode() * 31 + this.targetType.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 118 */       return this.sourceType.getName() + " -> " + this.targetType.getName();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\converter\GenericConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */