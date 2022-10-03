/*    */ package org.springframework.core.convert.support;
/*    */ 
/*    */ import java.lang.reflect.Array;
/*    */ import java.util.Collection;
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import org.springframework.core.convert.ConversionService;
/*    */ import org.springframework.core.convert.TypeDescriptor;
/*    */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*    */ import org.springframework.core.convert.converter.GenericConverter;
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
/*    */ final class ObjectToOptionalConverter
/*    */   implements ConditionalGenericConverter
/*    */ {
/*    */   private final ConversionService conversionService;
/*    */   
/*    */   public ObjectToOptionalConverter(ConversionService conversionService) {
/* 45 */     this.conversionService = conversionService;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 51 */     Set<GenericConverter.ConvertiblePair> convertibleTypes = new LinkedHashSet<>(4);
/* 52 */     convertibleTypes.add(new GenericConverter.ConvertiblePair(Collection.class, Optional.class));
/* 53 */     convertibleTypes.add(new GenericConverter.ConvertiblePair(Object[].class, Optional.class));
/* 54 */     convertibleTypes.add(new GenericConverter.ConvertiblePair(Object.class, Optional.class));
/* 55 */     return convertibleTypes;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 60 */     if (targetType.getResolvableType().hasGenerics()) {
/* 61 */       return this.conversionService.canConvert(sourceType, new GenericTypeDescriptor(targetType));
/*    */     }
/*    */     
/* 64 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 70 */     if (source == null) {
/* 71 */       return Optional.empty();
/*    */     }
/* 73 */     if (source instanceof Optional) {
/* 74 */       return source;
/*    */     }
/* 76 */     if (targetType.getResolvableType().hasGenerics()) {
/* 77 */       Object target = this.conversionService.convert(source, sourceType, new GenericTypeDescriptor(targetType));
/* 78 */       if (target == null || (target.getClass().isArray() && Array.getLength(target) == 0) || (target instanceof Collection && ((Collection)target)
/* 79 */         .isEmpty())) {
/* 80 */         return Optional.empty();
/*    */       }
/* 82 */       return Optional.of(target);
/*    */     } 
/*    */     
/* 85 */     return Optional.of(source);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static class GenericTypeDescriptor
/*    */     extends TypeDescriptor
/*    */   {
/*    */     public GenericTypeDescriptor(TypeDescriptor typeDescriptor) {
/* 94 */       super(typeDescriptor.getResolvableType().getGeneric(new int[0]), null, typeDescriptor.getAnnotations());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\ObjectToOptionalConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */