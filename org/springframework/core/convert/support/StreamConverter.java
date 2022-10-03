/*     */ package org.springframework.core.convert.support;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
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
/*     */ class StreamConverter
/*     */   implements ConditionalGenericConverter
/*     */ {
/*  41 */   private static final TypeDescriptor STREAM_TYPE = TypeDescriptor.valueOf(Stream.class);
/*     */   
/*  43 */   private static final Set<GenericConverter.ConvertiblePair> CONVERTIBLE_TYPES = createConvertibleTypes();
/*     */   
/*     */   private final ConversionService conversionService;
/*     */ 
/*     */   
/*     */   public StreamConverter(ConversionService conversionService) {
/*  49 */     this.conversionService = conversionService;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/*  55 */     return CONVERTIBLE_TYPES;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/*  60 */     if (sourceType.isAssignableTo(STREAM_TYPE)) {
/*  61 */       return matchesFromStream(sourceType.getElementTypeDescriptor(), targetType);
/*     */     }
/*  63 */     if (targetType.isAssignableTo(STREAM_TYPE)) {
/*  64 */       return matchesToStream(targetType.getElementTypeDescriptor(), sourceType);
/*     */     }
/*  66 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matchesFromStream(@Nullable TypeDescriptor elementType, TypeDescriptor targetType) {
/*  76 */     TypeDescriptor collectionOfElement = TypeDescriptor.collection(Collection.class, elementType);
/*  77 */     return this.conversionService.canConvert(collectionOfElement, targetType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matchesToStream(@Nullable TypeDescriptor elementType, TypeDescriptor sourceType) {
/*  87 */     TypeDescriptor collectionOfElement = TypeDescriptor.collection(Collection.class, elementType);
/*  88 */     return this.conversionService.canConvert(sourceType, collectionOfElement);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/*  94 */     if (sourceType.isAssignableTo(STREAM_TYPE)) {
/*  95 */       return convertFromStream((Stream)source, sourceType, targetType);
/*     */     }
/*  97 */     if (targetType.isAssignableTo(STREAM_TYPE)) {
/*  98 */       return convertToStream(source, sourceType, targetType);
/*     */     }
/*     */     
/* 101 */     throw new IllegalStateException("Unexpected source/target types");
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Object convertFromStream(@Nullable Stream<?> source, TypeDescriptor streamType, TypeDescriptor targetType) {
/* 106 */     List<Object> content = (source != null) ? source.collect((Collector)Collectors.toList()) : Collections.<Object>emptyList();
/* 107 */     TypeDescriptor listType = TypeDescriptor.collection(List.class, streamType.getElementTypeDescriptor());
/* 108 */     return this.conversionService.convert(content, listType, targetType);
/*     */   }
/*     */   
/*     */   private Object convertToStream(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor streamType) {
/* 112 */     TypeDescriptor targetCollection = TypeDescriptor.collection(List.class, streamType.getElementTypeDescriptor());
/* 113 */     List<?> target = (List)this.conversionService.convert(source, sourceType, targetCollection);
/* 114 */     if (target == null) {
/* 115 */       target = Collections.emptyList();
/*     */     }
/* 117 */     return target.stream();
/*     */   }
/*     */ 
/*     */   
/*     */   private static Set<GenericConverter.ConvertiblePair> createConvertibleTypes() {
/* 122 */     Set<GenericConverter.ConvertiblePair> convertiblePairs = new HashSet<>();
/* 123 */     convertiblePairs.add(new GenericConverter.ConvertiblePair(Stream.class, Collection.class));
/* 124 */     convertiblePairs.add(new GenericConverter.ConvertiblePair(Stream.class, Object[].class));
/* 125 */     convertiblePairs.add(new GenericConverter.ConvertiblePair(Collection.class, Stream.class));
/* 126 */     convertiblePairs.add(new GenericConverter.ConvertiblePair(Object[].class, Stream.class));
/* 127 */     return convertiblePairs;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\StreamConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */