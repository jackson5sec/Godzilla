/*    */ package org.springframework.expression.spel.support;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import org.springframework.core.convert.ConversionException;
/*    */ import org.springframework.core.convert.ConversionService;
/*    */ import org.springframework.core.convert.TypeDescriptor;
/*    */ import org.springframework.core.convert.support.DefaultConversionService;
/*    */ import org.springframework.expression.TypeConverter;
/*    */ import org.springframework.expression.spel.SpelEvaluationException;
/*    */ import org.springframework.expression.spel.SpelMessage;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.Assert;
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
/*    */ public class StandardTypeConverter
/*    */   implements TypeConverter
/*    */ {
/*    */   private final Supplier<ConversionService> conversionService;
/*    */   
/*    */   public StandardTypeConverter() {
/* 50 */     this.conversionService = DefaultConversionService::getSharedInstance;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StandardTypeConverter(ConversionService conversionService) {
/* 58 */     Assert.notNull(conversionService, "ConversionService must not be null");
/* 59 */     this.conversionService = (() -> conversionService);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StandardTypeConverter(Supplier<ConversionService> conversionService) {
/* 68 */     Assert.notNull(conversionService, "Supplier must not be null");
/* 69 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canConvert(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 75 */     return ((ConversionService)this.conversionService.get()).canConvert(sourceType, targetType);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Object convertValue(@Nullable Object value, @Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
/*    */     try {
/* 82 */       return ((ConversionService)this.conversionService.get()).convert(value, sourceType, targetType);
/*    */     }
/* 84 */     catch (ConversionException ex) {
/* 85 */       throw new SpelEvaluationException(ex, SpelMessage.TYPE_CONVERSION_ERROR, new Object[] { (sourceType != null) ? sourceType
/* 86 */             .toString() : ((value != null) ? value.getClass().getName() : "null"), targetType
/* 87 */             .toString() });
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\support\StandardTypeConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */