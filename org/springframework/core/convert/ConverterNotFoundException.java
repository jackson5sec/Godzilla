/*    */ package org.springframework.core.convert;
/*    */ 
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public class ConverterNotFoundException
/*    */   extends ConversionException
/*    */ {
/*    */   @Nullable
/*    */   private final TypeDescriptor sourceType;
/*    */   private final TypeDescriptor targetType;
/*    */   
/*    */   public ConverterNotFoundException(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 44 */     super("No converter found capable of converting from type [" + sourceType + "] to type [" + targetType + "]");
/* 45 */     this.sourceType = sourceType;
/* 46 */     this.targetType = targetType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public TypeDescriptor getSourceType() {
/* 55 */     return this.sourceType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeDescriptor getTargetType() {
/* 62 */     return this.targetType;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\ConverterNotFoundException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */