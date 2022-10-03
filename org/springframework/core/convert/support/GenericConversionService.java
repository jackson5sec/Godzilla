/*     */ package org.springframework.core.convert.support;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Deque;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentLinkedDeque;
/*     */ import java.util.concurrent.CopyOnWriteArraySet;
/*     */ import org.springframework.core.DecoratingProxy;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.convert.ConversionFailedException;
/*     */ import org.springframework.core.convert.ConverterNotFoundException;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.core.convert.converter.ConditionalConverter;
/*     */ import org.springframework.core.convert.converter.ConditionalGenericConverter;
/*     */ import org.springframework.core.convert.converter.Converter;
/*     */ import org.springframework.core.convert.converter.ConverterFactory;
/*     */ import org.springframework.core.convert.converter.GenericConverter;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class GenericConversionService
/*     */   implements ConfigurableConversionService
/*     */ {
/*  69 */   private static final GenericConverter NO_OP_CONVERTER = new NoOpConverter("NO_OP");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   private static final GenericConverter NO_MATCH = new NoOpConverter("NO_MATCH");
/*     */ 
/*     */   
/*  78 */   private final Converters converters = new Converters();
/*     */   
/*  80 */   private final Map<ConverterCacheKey, GenericConverter> converterCache = (Map<ConverterCacheKey, GenericConverter>)new ConcurrentReferenceHashMap(64);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConverter(Converter<?, ?> converter) {
/*  87 */     ResolvableType[] typeInfo = getRequiredTypeInfo(converter.getClass(), Converter.class);
/*  88 */     if (typeInfo == null && converter instanceof DecoratingProxy) {
/*  89 */       typeInfo = getRequiredTypeInfo(((DecoratingProxy)converter).getDecoratedClass(), Converter.class);
/*     */     }
/*  91 */     if (typeInfo == null) {
/*  92 */       throw new IllegalArgumentException("Unable to determine source type <S> and target type <T> for your Converter [" + converter
/*  93 */           .getClass().getName() + "]; does the class parameterize those types?");
/*     */     }
/*  95 */     addConverter((GenericConverter)new ConverterAdapter(converter, typeInfo[0], typeInfo[1]));
/*     */   }
/*     */ 
/*     */   
/*     */   public <S, T> void addConverter(Class<S> sourceType, Class<T> targetType, Converter<? super S, ? extends T> converter) {
/* 100 */     addConverter((GenericConverter)new ConverterAdapter(converter, 
/* 101 */           ResolvableType.forClass(sourceType), ResolvableType.forClass(targetType)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addConverter(GenericConverter converter) {
/* 106 */     this.converters.add(converter);
/* 107 */     invalidateCache();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addConverterFactory(ConverterFactory<?, ?> factory) {
/* 112 */     ResolvableType[] typeInfo = getRequiredTypeInfo(factory.getClass(), ConverterFactory.class);
/* 113 */     if (typeInfo == null && factory instanceof DecoratingProxy) {
/* 114 */       typeInfo = getRequiredTypeInfo(((DecoratingProxy)factory).getDecoratedClass(), ConverterFactory.class);
/*     */     }
/* 116 */     if (typeInfo == null) {
/* 117 */       throw new IllegalArgumentException("Unable to determine source type <S> and target type <T> for your ConverterFactory [" + factory
/* 118 */           .getClass().getName() + "]; does the class parameterize those types?");
/*     */     }
/* 120 */     addConverter((GenericConverter)new ConverterFactoryAdapter(factory, new GenericConverter.ConvertiblePair(typeInfo[0]
/* 121 */             .toClass(), typeInfo[1].toClass())));
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeConvertible(Class<?> sourceType, Class<?> targetType) {
/* 126 */     this.converters.remove(sourceType, targetType);
/* 127 */     invalidateCache();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canConvert(@Nullable Class<?> sourceType, Class<?> targetType) {
/* 135 */     Assert.notNull(targetType, "Target type to convert to cannot be null");
/* 136 */     return canConvert((sourceType != null) ? TypeDescriptor.valueOf(sourceType) : null, 
/* 137 */         TypeDescriptor.valueOf(targetType));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canConvert(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 142 */     Assert.notNull(targetType, "Target type to convert to cannot be null");
/* 143 */     if (sourceType == null) {
/* 144 */       return true;
/*     */     }
/* 146 */     GenericConverter converter = getConverter(sourceType, targetType);
/* 147 */     return (converter != null);
/*     */   }
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
/*     */   public boolean canBypassConvert(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 162 */     Assert.notNull(targetType, "Target type to convert to cannot be null");
/* 163 */     if (sourceType == null) {
/* 164 */       return true;
/*     */     }
/* 166 */     GenericConverter converter = getConverter(sourceType, targetType);
/* 167 */     return (converter == NO_OP_CONVERTER);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public <T> T convert(@Nullable Object source, Class<T> targetType) {
/* 174 */     Assert.notNull(targetType, "Target type to convert to cannot be null");
/* 175 */     return (T)convert(source, TypeDescriptor.forObject(source), TypeDescriptor.valueOf(targetType));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object convert(@Nullable Object source, @Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 181 */     Assert.notNull(targetType, "Target type to convert to cannot be null");
/* 182 */     if (sourceType == null) {
/* 183 */       Assert.isTrue((source == null), "Source must be [null] if source type == [null]");
/* 184 */       return handleResult(null, targetType, convertNullSource(null, targetType));
/*     */     } 
/* 186 */     if (source != null && !sourceType.getObjectType().isInstance(source)) {
/* 187 */       throw new IllegalArgumentException("Source to convert from must be an instance of [" + sourceType + "]; instead it was a [" + source
/* 188 */           .getClass().getName() + "]");
/*     */     }
/* 190 */     GenericConverter converter = getConverter(sourceType, targetType);
/* 191 */     if (converter != null) {
/* 192 */       Object result = ConversionUtils.invokeConverter(converter, source, sourceType, targetType);
/* 193 */       return handleResult(sourceType, targetType, result);
/*     */     } 
/* 195 */     return handleConverterNotFound(source, sourceType, targetType);
/*     */   }
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
/*     */   @Nullable
/*     */   public Object convert(@Nullable Object source, TypeDescriptor targetType) {
/* 213 */     return convert(source, TypeDescriptor.forObject(source), targetType);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 218 */     return this.converters.toString();
/*     */   }
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
/*     */   @Nullable
/*     */   protected Object convertNullSource(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 236 */     if (targetType.getObjectType() == Optional.class) {
/* 237 */       return Optional.empty();
/*     */     }
/* 239 */     return null;
/*     */   }
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
/*     */   @Nullable
/*     */   protected GenericConverter getConverter(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 255 */     ConverterCacheKey key = new ConverterCacheKey(sourceType, targetType);
/* 256 */     GenericConverter converter = this.converterCache.get(key);
/* 257 */     if (converter != null) {
/* 258 */       return (converter != NO_MATCH) ? converter : null;
/*     */     }
/*     */     
/* 261 */     converter = this.converters.find(sourceType, targetType);
/* 262 */     if (converter == null) {
/* 263 */       converter = getDefaultConverter(sourceType, targetType);
/*     */     }
/*     */     
/* 266 */     if (converter != null) {
/* 267 */       this.converterCache.put(key, converter);
/* 268 */       return converter;
/*     */     } 
/*     */     
/* 271 */     this.converterCache.put(key, NO_MATCH);
/* 272 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected GenericConverter getDefaultConverter(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 285 */     return sourceType.isAssignableTo(targetType) ? NO_OP_CONVERTER : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private ResolvableType[] getRequiredTypeInfo(Class<?> converterClass, Class<?> genericIfc) {
/* 293 */     ResolvableType resolvableType = ResolvableType.forClass(converterClass).as(genericIfc);
/* 294 */     ResolvableType[] generics = resolvableType.getGenerics();
/* 295 */     if (generics.length < 2) {
/* 296 */       return null;
/*     */     }
/* 298 */     Class<?> sourceType = generics[0].resolve();
/* 299 */     Class<?> targetType = generics[1].resolve();
/* 300 */     if (sourceType == null || targetType == null) {
/* 301 */       return null;
/*     */     }
/* 303 */     return generics;
/*     */   }
/*     */   
/*     */   private void invalidateCache() {
/* 307 */     this.converterCache.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Object handleConverterNotFound(@Nullable Object source, @Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 314 */     if (source == null) {
/* 315 */       assertNotPrimitiveTargetType(sourceType, targetType);
/* 316 */       return null;
/*     */     } 
/* 318 */     if ((sourceType == null || sourceType.isAssignableTo(targetType)) && targetType
/* 319 */       .getObjectType().isInstance(source)) {
/* 320 */       return source;
/*     */     }
/* 322 */     throw new ConverterNotFoundException(sourceType, targetType);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Object handleResult(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType, @Nullable Object result) {
/* 327 */     if (result == null) {
/* 328 */       assertNotPrimitiveTargetType(sourceType, targetType);
/*     */     }
/* 330 */     return result;
/*     */   }
/*     */   
/*     */   private void assertNotPrimitiveTargetType(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 334 */     if (targetType.isPrimitive()) {
/* 335 */       throw new ConversionFailedException(sourceType, targetType, null, new IllegalArgumentException("A null value cannot be assigned to a primitive type"));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final class ConverterAdapter
/*     */     implements ConditionalGenericConverter
/*     */   {
/*     */     private final Converter<Object, Object> converter;
/*     */ 
/*     */     
/*     */     private final GenericConverter.ConvertiblePair typeInfo;
/*     */ 
/*     */     
/*     */     private final ResolvableType targetType;
/*     */ 
/*     */     
/*     */     public ConverterAdapter(Converter<?, ?> converter, ResolvableType sourceType, ResolvableType targetType) {
/* 354 */       this.converter = (Converter)converter;
/* 355 */       this.typeInfo = new GenericConverter.ConvertiblePair(sourceType.toClass(), targetType.toClass());
/* 356 */       this.targetType = targetType;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 361 */       return Collections.singleton(this.typeInfo);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 367 */       if (this.typeInfo.getTargetType() != targetType.getObjectType()) {
/* 368 */         return false;
/*     */       }
/*     */       
/* 371 */       ResolvableType rt = targetType.getResolvableType();
/* 372 */       if (!(rt.getType() instanceof Class) && !rt.isAssignableFrom(this.targetType) && 
/* 373 */         !this.targetType.hasUnresolvableGenerics()) {
/* 374 */         return false;
/*     */       }
/* 376 */       return (!(this.converter instanceof ConditionalConverter) || ((ConditionalConverter)this.converter)
/* 377 */         .matches(sourceType, targetType));
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 383 */       if (source == null) {
/* 384 */         return GenericConversionService.this.convertNullSource(sourceType, targetType);
/*     */       }
/* 386 */       return this.converter.convert(source);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 391 */       return this.typeInfo + " : " + this.converter;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final class ConverterFactoryAdapter
/*     */     implements ConditionalGenericConverter
/*     */   {
/*     */     private final ConverterFactory<Object, Object> converterFactory;
/*     */ 
/*     */     
/*     */     private final GenericConverter.ConvertiblePair typeInfo;
/*     */ 
/*     */     
/*     */     public ConverterFactoryAdapter(ConverterFactory<?, ?> converterFactory, GenericConverter.ConvertiblePair typeInfo) {
/* 407 */       this.converterFactory = (ConverterFactory)converterFactory;
/* 408 */       this.typeInfo = typeInfo;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 413 */       return Collections.singleton(this.typeInfo);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 418 */       boolean matches = true;
/* 419 */       if (this.converterFactory instanceof ConditionalConverter) {
/* 420 */         matches = ((ConditionalConverter)this.converterFactory).matches(sourceType, targetType);
/*     */       }
/* 422 */       if (matches) {
/* 423 */         Converter<?, ?> converter = this.converterFactory.getConverter(targetType.getType());
/* 424 */         if (converter instanceof ConditionalConverter) {
/* 425 */           matches = ((ConditionalConverter)converter).matches(sourceType, targetType);
/*     */         }
/*     */       } 
/* 428 */       return matches;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 434 */       if (source == null) {
/* 435 */         return GenericConversionService.this.convertNullSource(sourceType, targetType);
/*     */       }
/* 437 */       return this.converterFactory.getConverter(targetType.getObjectType()).convert(source);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 442 */       return this.typeInfo + " : " + this.converterFactory;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class ConverterCacheKey
/*     */     implements Comparable<ConverterCacheKey>
/*     */   {
/*     */     private final TypeDescriptor sourceType;
/*     */     
/*     */     private final TypeDescriptor targetType;
/*     */ 
/*     */     
/*     */     public ConverterCacheKey(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 457 */       this.sourceType = sourceType;
/* 458 */       this.targetType = targetType;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object other) {
/* 463 */       if (this == other) {
/* 464 */         return true;
/*     */       }
/* 466 */       if (!(other instanceof ConverterCacheKey)) {
/* 467 */         return false;
/*     */       }
/* 469 */       ConverterCacheKey otherKey = (ConverterCacheKey)other;
/* 470 */       return (this.sourceType.equals(otherKey.sourceType) && this.targetType
/* 471 */         .equals(otherKey.targetType));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 476 */       return this.sourceType.hashCode() * 29 + this.targetType.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 481 */       return "ConverterCacheKey [sourceType = " + this.sourceType + ", targetType = " + this.targetType + "]";
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int compareTo(ConverterCacheKey other) {
/* 487 */       int result = this.sourceType.getResolvableType().toString().compareTo(other.sourceType
/* 488 */           .getResolvableType().toString());
/* 489 */       if (result == 0) {
/* 490 */         result = this.targetType.getResolvableType().toString().compareTo(other.targetType
/* 491 */             .getResolvableType().toString());
/*     */       }
/* 493 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class Converters
/*     */   {
/* 503 */     private final Set<GenericConverter> globalConverters = new CopyOnWriteArraySet<>();
/*     */     
/* 505 */     private final Map<GenericConverter.ConvertiblePair, GenericConversionService.ConvertersForPair> converters = new ConcurrentHashMap<>(256);
/*     */     
/*     */     public void add(GenericConverter converter) {
/* 508 */       Set<GenericConverter.ConvertiblePair> convertibleTypes = converter.getConvertibleTypes();
/* 509 */       if (convertibleTypes == null) {
/* 510 */         Assert.state(converter instanceof ConditionalConverter, "Only conditional converters may return null convertible types");
/*     */         
/* 512 */         this.globalConverters.add(converter);
/*     */       } else {
/*     */         
/* 515 */         for (GenericConverter.ConvertiblePair convertiblePair : convertibleTypes) {
/* 516 */           getMatchableConverters(convertiblePair).add(converter);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     private GenericConversionService.ConvertersForPair getMatchableConverters(GenericConverter.ConvertiblePair convertiblePair) {
/* 522 */       return this.converters.computeIfAbsent(convertiblePair, k -> new GenericConversionService.ConvertersForPair());
/*     */     }
/*     */     
/*     */     public void remove(Class<?> sourceType, Class<?> targetType) {
/* 526 */       this.converters.remove(new GenericConverter.ConvertiblePair(sourceType, targetType));
/*     */     }
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
/*     */     @Nullable
/*     */     public GenericConverter find(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 540 */       List<Class<?>> sourceCandidates = getClassHierarchy(sourceType.getType());
/* 541 */       List<Class<?>> targetCandidates = getClassHierarchy(targetType.getType());
/* 542 */       for (Class<?> sourceCandidate : sourceCandidates) {
/* 543 */         for (Class<?> targetCandidate : targetCandidates) {
/* 544 */           GenericConverter.ConvertiblePair convertiblePair = new GenericConverter.ConvertiblePair(sourceCandidate, targetCandidate);
/* 545 */           GenericConverter converter = getRegisteredConverter(sourceType, targetType, convertiblePair);
/* 546 */           if (converter != null) {
/* 547 */             return converter;
/*     */           }
/*     */         } 
/*     */       } 
/* 551 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private GenericConverter getRegisteredConverter(TypeDescriptor sourceType, TypeDescriptor targetType, GenericConverter.ConvertiblePair convertiblePair) {
/* 559 */       GenericConversionService.ConvertersForPair convertersForPair = this.converters.get(convertiblePair);
/* 560 */       if (convertersForPair != null) {
/* 561 */         GenericConverter converter = convertersForPair.getConverter(sourceType, targetType);
/* 562 */         if (converter != null) {
/* 563 */           return converter;
/*     */         }
/*     */       } 
/*     */       
/* 567 */       for (GenericConverter globalConverter : this.globalConverters) {
/* 568 */         if (((ConditionalConverter)globalConverter).matches(sourceType, targetType)) {
/* 569 */           return globalConverter;
/*     */         }
/*     */       } 
/* 572 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private List<Class<?>> getClassHierarchy(Class<?> type) {
/* 581 */       List<Class<?>> hierarchy = new ArrayList<>(20);
/* 582 */       Set<Class<?>> visited = new HashSet<>(20);
/* 583 */       addToClassHierarchy(0, ClassUtils.resolvePrimitiveIfNecessary(type), false, hierarchy, visited);
/* 584 */       boolean array = type.isArray();
/*     */       
/* 586 */       int i = 0;
/* 587 */       while (i < hierarchy.size()) {
/* 588 */         Class<?> candidate = hierarchy.get(i);
/* 589 */         candidate = array ? candidate.getComponentType() : ClassUtils.resolvePrimitiveIfNecessary(candidate);
/* 590 */         Class<?> superclass = candidate.getSuperclass();
/* 591 */         if (superclass != null && superclass != Object.class && superclass != Enum.class) {
/* 592 */           addToClassHierarchy(i + 1, candidate.getSuperclass(), array, hierarchy, visited);
/*     */         }
/* 594 */         addInterfacesToClassHierarchy(candidate, array, hierarchy, visited);
/* 595 */         i++;
/*     */       } 
/*     */       
/* 598 */       if (Enum.class.isAssignableFrom(type)) {
/* 599 */         addToClassHierarchy(hierarchy.size(), Enum.class, array, hierarchy, visited);
/* 600 */         addToClassHierarchy(hierarchy.size(), Enum.class, false, hierarchy, visited);
/* 601 */         addInterfacesToClassHierarchy(Enum.class, array, hierarchy, visited);
/*     */       } 
/*     */       
/* 604 */       addToClassHierarchy(hierarchy.size(), Object.class, array, hierarchy, visited);
/* 605 */       addToClassHierarchy(hierarchy.size(), Object.class, false, hierarchy, visited);
/* 606 */       return hierarchy;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void addInterfacesToClassHierarchy(Class<?> type, boolean asArray, List<Class<?>> hierarchy, Set<Class<?>> visited) {
/* 612 */       for (Class<?> implementedInterface : type.getInterfaces()) {
/* 613 */         addToClassHierarchy(hierarchy.size(), implementedInterface, asArray, hierarchy, visited);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void addToClassHierarchy(int index, Class<?> type, boolean asArray, List<Class<?>> hierarchy, Set<Class<?>> visited) {
/* 620 */       if (asArray) {
/* 621 */         type = Array.newInstance(type, 0).getClass();
/*     */       }
/* 623 */       if (visited.add(type)) {
/* 624 */         hierarchy.add(index, type);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 630 */       StringBuilder builder = new StringBuilder();
/* 631 */       builder.append("ConversionService converters =\n");
/* 632 */       for (String converterString : getConverterStrings()) {
/* 633 */         builder.append('\t').append(converterString).append('\n');
/*     */       }
/* 635 */       return builder.toString();
/*     */     }
/*     */     
/*     */     private List<String> getConverterStrings() {
/* 639 */       List<String> converterStrings = new ArrayList<>();
/* 640 */       for (GenericConversionService.ConvertersForPair convertersForPair : this.converters.values()) {
/* 641 */         converterStrings.add(convertersForPair.toString());
/*     */       }
/* 643 */       Collections.sort(converterStrings);
/* 644 */       return converterStrings;
/*     */     }
/*     */ 
/*     */     
/*     */     private Converters() {}
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ConvertersForPair
/*     */   {
/* 654 */     private final Deque<GenericConverter> converters = new ConcurrentLinkedDeque<>();
/*     */     
/*     */     public void add(GenericConverter converter) {
/* 657 */       this.converters.addFirst(converter);
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public GenericConverter getConverter(TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 662 */       for (GenericConverter converter : this.converters) {
/* 663 */         if (!(converter instanceof ConditionalGenericConverter) || ((ConditionalGenericConverter)converter)
/* 664 */           .matches(sourceType, targetType)) {
/* 665 */           return converter;
/*     */         }
/*     */       } 
/* 668 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 673 */       return StringUtils.collectionToCommaDelimitedString(this.converters);
/*     */     }
/*     */ 
/*     */     
/*     */     private ConvertersForPair() {}
/*     */   }
/*     */   
/*     */   private static class NoOpConverter
/*     */     implements GenericConverter
/*     */   {
/*     */     private final String name;
/*     */     
/*     */     public NoOpConverter(String name) {
/* 686 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
/* 692 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
/* 698 */       return source;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 703 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\convert\support\GenericConversionService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */