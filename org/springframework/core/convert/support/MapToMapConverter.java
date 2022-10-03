/*     */ package org.springframework.core.convert.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.CollectionFactory;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*     */ import org.springframework.core.convert.converter.GenericConverter;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ final class MapToMapConverter
/*     */   implements ConditionalGenericConverter
/*     */ {
/*     */   private final ConversionService conversionService;
/*     */   
/*     */   public MapToMapConverter(ConversionService conversionService) {
/*  49 */     this.conversionService = conversionService;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/*  55 */     return Collections.singleton(new GenericConverter.ConvertiblePair(Map.class, Map.class));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/*  60 */     return (canConvertKey(sourceType, targetType) && canConvertValue(sourceType, targetType));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/*  66 */     if (source == null) {
/*  67 */       return null;
/*     */     }
/*     */     
/*  70 */     Map<Object, Object> sourceMap = (Map<Object, Object>)source;
/*     */ 
/*     */     
/*  73 */     boolean copyRequired = !targetType.getType().isInstance(source);
/*  74 */     if (!copyRequired && sourceMap.isEmpty()) {
/*  75 */       return sourceMap;
/*     */     }
/*  77 */     TypeDescriptor keyDesc = targetType.getMapKeyTypeDescriptor();
/*  78 */     TypeDescriptor valueDesc = targetType.getMapValueTypeDescriptor();
/*     */     
/*  80 */     List<MapEntry> targetEntries = new ArrayList<>(sourceMap.size());
/*  81 */     for (Map.Entry<Object, Object> entry : sourceMap.entrySet()) {
/*  82 */       Object sourceKey = entry.getKey();
/*  83 */       Object sourceValue = entry.getValue();
/*  84 */       Object targetKey = convertKey(sourceKey, sourceType, keyDesc);
/*  85 */       Object targetValue = convertValue(sourceValue, sourceType, valueDesc);
/*  86 */       targetEntries.add(new MapEntry(targetKey, targetValue));
/*  87 */       if (sourceKey != targetKey || sourceValue != targetValue) {
/*  88 */         copyRequired = true;
/*     */       }
/*     */     } 
/*  91 */     if (!copyRequired) {
/*  92 */       return sourceMap;
/*     */     }
/*     */     
/*  95 */     Map<Object, Object> targetMap = CollectionFactory.createMap(targetType.getType(), (keyDesc != null) ? keyDesc
/*  96 */         .getType() : null, sourceMap.size());
/*     */     
/*  98 */     for (MapEntry entry : targetEntries) {
/*  99 */       entry.addToMap(targetMap);
/*     */     }
/* 101 */     return targetMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean canConvertKey(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 108 */     return ConversionUtils.canConvertElements(sourceType.getMapKeyTypeDescriptor(), targetType
/* 109 */         .getMapKeyTypeDescriptor(), this.conversionService);
/*     */   }
/*     */   
/*     */   private boolean canConvertValue(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 113 */     return ConversionUtils.canConvertElements(sourceType.getMapValueTypeDescriptor(), targetType
/* 114 */         .getMapValueTypeDescriptor(), this.conversionService);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Object convertKey(Object sourceKey, TypeDescriptor sourceType, @Nullable TypeDescriptor targetType) {
/* 119 */     if (targetType == null) {
/* 120 */       return sourceKey;
/*     */     }
/* 122 */     return this.conversionService.convert(sourceKey, sourceType.getMapKeyTypeDescriptor(sourceKey), targetType);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Object convertValue(Object sourceValue, TypeDescriptor sourceType, @Nullable TypeDescriptor targetType) {
/* 127 */     if (targetType == null) {
/* 128 */       return sourceValue;
/*     */     }
/* 130 */     return this.conversionService.convert(sourceValue, sourceType.getMapValueTypeDescriptor(sourceValue), targetType);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class MapEntry
/*     */   {
/*     */     @Nullable
/*     */     private final Object key;
/*     */     
/*     */     @Nullable
/*     */     private final Object value;
/*     */     
/*     */     public MapEntry(@Nullable Object key, @Nullable Object value) {
/* 143 */       this.key = key;
/* 144 */       this.value = value;
/*     */     }
/*     */     
/*     */     public void addToMap(Map<Object, Object> map) {
/* 148 */       map.put(this.key, this.value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\MapToMapConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */