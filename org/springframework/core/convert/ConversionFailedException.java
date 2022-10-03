/*    */ package org.springframework.core.convert;
/*    */ 
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.ObjectUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConversionFailedException
/*    */   extends ConversionException
/*    */ {
/*    */   @Nullable
/*    */   private final TypeDescriptor sourceType;
/*    */   private final TypeDescriptor targetType;
/*    */   @Nullable
/*    */   private final Object value;
/*    */   
/*    */   public ConversionFailedException(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType, @Nullable Object value, Throwable cause) {
/* 51 */     super("Failed to convert from type [" + sourceType + "] to type [" + targetType + "] for value '" + 
/* 52 */         ObjectUtils.nullSafeToString(value) + "'", cause);
/* 53 */     this.sourceType = sourceType;
/* 54 */     this.targetType = targetType;
/* 55 */     this.value = value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public TypeDescriptor getSourceType() {
/* 64 */     return this.sourceType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeDescriptor getTargetType() {
/* 71 */     return this.targetType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object getValue() {
/* 79 */     return this.value;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\ConversionFailedException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */