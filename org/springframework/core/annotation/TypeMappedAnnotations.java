/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
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
/*     */ final class TypeMappedAnnotations
/*     */   implements MergedAnnotations
/*     */ {
/*  46 */   static final MergedAnnotations NONE = new TypeMappedAnnotations(null, new Annotation[0], 
/*  47 */       RepeatableContainers.none(), AnnotationFilter.ALL);
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private final Object source;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private final AnnotatedElement element;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private final MergedAnnotations.SearchStrategy searchStrategy;
/*     */   
/*     */   @Nullable
/*     */   private final Annotation[] annotations;
/*     */   
/*     */   private final RepeatableContainers repeatableContainers;
/*     */   
/*     */   private final AnnotationFilter annotationFilter;
/*     */   
/*     */   @Nullable
/*     */   private volatile List<Aggregate> aggregates;
/*     */ 
/*     */   
/*     */   private TypeMappedAnnotations(AnnotatedElement element, MergedAnnotations.SearchStrategy searchStrategy, RepeatableContainers repeatableContainers, AnnotationFilter annotationFilter) {
/*  73 */     this.source = element;
/*  74 */     this.element = element;
/*  75 */     this.searchStrategy = searchStrategy;
/*  76 */     this.annotations = null;
/*  77 */     this.repeatableContainers = repeatableContainers;
/*  78 */     this.annotationFilter = annotationFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private TypeMappedAnnotations(@Nullable Object source, Annotation[] annotations, RepeatableContainers repeatableContainers, AnnotationFilter annotationFilter) {
/*  84 */     this.source = source;
/*  85 */     this.element = null;
/*  86 */     this.searchStrategy = null;
/*  87 */     this.annotations = annotations;
/*  88 */     this.repeatableContainers = repeatableContainers;
/*  89 */     this.annotationFilter = annotationFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> boolean isPresent(Class<A> annotationType) {
/*  95 */     if (this.annotationFilter.matches(annotationType)) {
/*  96 */       return false;
/*     */     }
/*  98 */     return Boolean.TRUE.equals(scan(annotationType, 
/*  99 */           IsPresent.get(this.repeatableContainers, this.annotationFilter, false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPresent(String annotationType) {
/* 104 */     if (this.annotationFilter.matches(annotationType)) {
/* 105 */       return false;
/*     */     }
/* 107 */     return Boolean.TRUE.equals(scan(annotationType, 
/* 108 */           IsPresent.get(this.repeatableContainers, this.annotationFilter, false)));
/*     */   }
/*     */ 
/*     */   
/*     */   public <A extends Annotation> boolean isDirectlyPresent(Class<A> annotationType) {
/* 113 */     if (this.annotationFilter.matches(annotationType)) {
/* 114 */       return false;
/*     */     }
/* 116 */     return Boolean.TRUE.equals(scan(annotationType, 
/* 117 */           IsPresent.get(this.repeatableContainers, this.annotationFilter, true)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDirectlyPresent(String annotationType) {
/* 122 */     if (this.annotationFilter.matches(annotationType)) {
/* 123 */       return false;
/*     */     }
/* 125 */     return Boolean.TRUE.equals(scan(annotationType, 
/* 126 */           IsPresent.get(this.repeatableContainers, this.annotationFilter, true)));
/*     */   }
/*     */ 
/*     */   
/*     */   public <A extends Annotation> MergedAnnotation<A> get(Class<A> annotationType) {
/* 131 */     return get(annotationType, (Predicate<? super MergedAnnotation<A>>)null, (MergedAnnotationSelector<A>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> MergedAnnotation<A> get(Class<A> annotationType, @Nullable Predicate<? super MergedAnnotation<A>> predicate) {
/* 138 */     return get(annotationType, predicate, (MergedAnnotationSelector<A>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> MergedAnnotation<A> get(Class<A> annotationType, @Nullable Predicate<? super MergedAnnotation<A>> predicate, @Nullable MergedAnnotationSelector<A> selector) {
/* 146 */     if (this.annotationFilter.matches(annotationType)) {
/* 147 */       return MergedAnnotation.missing();
/*     */     }
/* 149 */     MergedAnnotation<A> result = scan(annotationType, (AnnotationsProcessor)new MergedAnnotationFinder<>(annotationType, predicate, selector));
/*     */     
/* 151 */     return (result != null) ? result : MergedAnnotation.<A>missing();
/*     */   }
/*     */ 
/*     */   
/*     */   public <A extends Annotation> MergedAnnotation<A> get(String annotationType) {
/* 156 */     return get(annotationType, (Predicate<? super MergedAnnotation<A>>)null, (MergedAnnotationSelector<A>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> MergedAnnotation<A> get(String annotationType, @Nullable Predicate<? super MergedAnnotation<A>> predicate) {
/* 163 */     return get(annotationType, predicate, (MergedAnnotationSelector<A>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> MergedAnnotation<A> get(String annotationType, @Nullable Predicate<? super MergedAnnotation<A>> predicate, @Nullable MergedAnnotationSelector<A> selector) {
/* 171 */     if (this.annotationFilter.matches(annotationType)) {
/* 172 */       return MergedAnnotation.missing();
/*     */     }
/* 174 */     MergedAnnotation<A> result = scan(annotationType, (AnnotationsProcessor)new MergedAnnotationFinder<>(annotationType, predicate, selector));
/*     */     
/* 176 */     return (result != null) ? result : MergedAnnotation.<A>missing();
/*     */   }
/*     */ 
/*     */   
/*     */   public <A extends Annotation> Stream<MergedAnnotation<A>> stream(Class<A> annotationType) {
/* 181 */     if (this.annotationFilter == AnnotationFilter.ALL) {
/* 182 */       return Stream.empty();
/*     */     }
/* 184 */     return StreamSupport.stream(spliterator(annotationType), false);
/*     */   }
/*     */ 
/*     */   
/*     */   public <A extends Annotation> Stream<MergedAnnotation<A>> stream(String annotationType) {
/* 189 */     if (this.annotationFilter == AnnotationFilter.ALL) {
/* 190 */       return Stream.empty();
/*     */     }
/* 192 */     return StreamSupport.stream(spliterator(annotationType), false);
/*     */   }
/*     */ 
/*     */   
/*     */   public Stream<MergedAnnotation<Annotation>> stream() {
/* 197 */     if (this.annotationFilter == AnnotationFilter.ALL) {
/* 198 */       return Stream.empty();
/*     */     }
/* 200 */     return StreamSupport.stream(spliterator(), false);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<MergedAnnotation<Annotation>> iterator() {
/* 205 */     if (this.annotationFilter == AnnotationFilter.ALL) {
/* 206 */       return Collections.emptyIterator();
/*     */     }
/* 208 */     return Spliterators.iterator(spliterator());
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<MergedAnnotation<Annotation>> spliterator() {
/* 213 */     if (this.annotationFilter == AnnotationFilter.ALL) {
/* 214 */       return Spliterators.emptySpliterator();
/*     */     }
/* 216 */     return spliterator(null);
/*     */   }
/*     */   
/*     */   private <A extends Annotation> Spliterator<MergedAnnotation<A>> spliterator(@Nullable Object annotationType) {
/* 220 */     return new AggregatesSpliterator<>(annotationType, getAggregates());
/*     */   }
/*     */   
/*     */   private List<Aggregate> getAggregates() {
/* 224 */     List<Aggregate> aggregates = this.aggregates;
/* 225 */     if (aggregates == null) {
/* 226 */       aggregates = scan(this, new AggregatesCollector());
/* 227 */       if (aggregates == null || aggregates.isEmpty()) {
/* 228 */         aggregates = Collections.emptyList();
/*     */       }
/* 230 */       this.aggregates = aggregates;
/*     */     } 
/* 232 */     return aggregates;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private <C, R> R scan(C criteria, AnnotationsProcessor<C, R> processor) {
/* 237 */     if (this.annotations != null) {
/* 238 */       R result = processor.doWithAnnotations(criteria, 0, this.source, this.annotations);
/* 239 */       return processor.finish(result);
/*     */     } 
/* 241 */     if (this.element != null && this.searchStrategy != null) {
/* 242 */       return AnnotationsScanner.scan(criteria, this.element, this.searchStrategy, processor);
/*     */     }
/* 244 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static MergedAnnotations from(AnnotatedElement element, MergedAnnotations.SearchStrategy searchStrategy, RepeatableContainers repeatableContainers, AnnotationFilter annotationFilter) {
/* 251 */     if (AnnotationsScanner.isKnownEmpty(element, searchStrategy)) {
/* 252 */       return NONE;
/*     */     }
/* 254 */     return new TypeMappedAnnotations(element, searchStrategy, repeatableContainers, annotationFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static MergedAnnotations from(@Nullable Object source, Annotation[] annotations, RepeatableContainers repeatableContainers, AnnotationFilter annotationFilter) {
/* 260 */     if (annotations.length == 0) {
/* 261 */       return NONE;
/*     */     }
/* 263 */     return new TypeMappedAnnotations(source, annotations, repeatableContainers, annotationFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isMappingForType(AnnotationTypeMapping mapping, AnnotationFilter annotationFilter, @Nullable Object requiredType) {
/* 269 */     Class<? extends Annotation> actualType = mapping.getAnnotationType();
/* 270 */     return (!annotationFilter.matches(actualType) && (requiredType == null || actualType == requiredType || actualType
/* 271 */       .getName().equals(requiredType)));
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
/*     */   private static final class IsPresent
/*     */     implements AnnotationsProcessor<Object, Boolean>
/*     */   {
/* 287 */     private static final IsPresent[] SHARED = new IsPresent[4]; static {
/* 288 */       SHARED[0] = new IsPresent(RepeatableContainers.none(), AnnotationFilter.PLAIN, true);
/* 289 */       SHARED[1] = new IsPresent(RepeatableContainers.none(), AnnotationFilter.PLAIN, false);
/* 290 */       SHARED[2] = new IsPresent(RepeatableContainers.standardRepeatables(), AnnotationFilter.PLAIN, true);
/* 291 */       SHARED[3] = new IsPresent(RepeatableContainers.standardRepeatables(), AnnotationFilter.PLAIN, false);
/*     */     }
/*     */ 
/*     */     
/*     */     private final RepeatableContainers repeatableContainers;
/*     */     
/*     */     private final AnnotationFilter annotationFilter;
/*     */     
/*     */     private final boolean directOnly;
/*     */ 
/*     */     
/*     */     private IsPresent(RepeatableContainers repeatableContainers, AnnotationFilter annotationFilter, boolean directOnly) {
/* 303 */       this.repeatableContainers = repeatableContainers;
/* 304 */       this.annotationFilter = annotationFilter;
/* 305 */       this.directOnly = directOnly;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Boolean doWithAnnotations(Object requiredType, int aggregateIndex, @Nullable Object source, Annotation[] annotations) {
/* 313 */       for (Annotation annotation : annotations) {
/* 314 */         if (annotation != null) {
/* 315 */           Class<? extends Annotation> type = annotation.annotationType();
/* 316 */           if (type != null && !this.annotationFilter.matches(type)) {
/* 317 */             if (type == requiredType || type.getName().equals(requiredType)) {
/* 318 */               return Boolean.TRUE;
/*     */             }
/*     */             
/* 321 */             Annotation[] repeatedAnnotations = this.repeatableContainers.findRepeatedAnnotations(annotation);
/* 322 */             if (repeatedAnnotations != null) {
/* 323 */               Boolean result = doWithAnnotations(requiredType, aggregateIndex, source, repeatedAnnotations);
/*     */               
/* 325 */               if (result != null) {
/* 326 */                 return result;
/*     */               }
/*     */             } 
/* 329 */             if (!this.directOnly) {
/* 330 */               AnnotationTypeMappings mappings = AnnotationTypeMappings.forAnnotationType(type);
/* 331 */               for (int i = 0; i < mappings.size(); i++) {
/* 332 */                 AnnotationTypeMapping mapping = mappings.get(i);
/* 333 */                 if (TypeMappedAnnotations.isMappingForType(mapping, this.annotationFilter, requiredType)) {
/* 334 */                   return Boolean.TRUE;
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 341 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static IsPresent get(RepeatableContainers repeatableContainers, AnnotationFilter annotationFilter, boolean directOnly) {
/* 348 */       if (annotationFilter == AnnotationFilter.PLAIN) {
/* 349 */         if (repeatableContainers == RepeatableContainers.none()) {
/* 350 */           return SHARED[directOnly ? 0 : 1];
/*     */         }
/* 352 */         if (repeatableContainers == RepeatableContainers.standardRepeatables()) {
/* 353 */           return SHARED[directOnly ? 2 : 3];
/*     */         }
/*     */       } 
/* 356 */       return new IsPresent(repeatableContainers, annotationFilter, directOnly);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class MergedAnnotationFinder<A extends Annotation>
/*     */     implements AnnotationsProcessor<Object, MergedAnnotation<A>>
/*     */   {
/*     */     private final Object requiredType;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private final Predicate<? super MergedAnnotation<A>> predicate;
/*     */ 
/*     */     
/*     */     private final MergedAnnotationSelector<A> selector;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private MergedAnnotation<A> result;
/*     */ 
/*     */     
/*     */     MergedAnnotationFinder(@Nullable Object requiredType, @Nullable Predicate<? super MergedAnnotation<A>> predicate, MergedAnnotationSelector<A> selector) {
/* 380 */       this.requiredType = requiredType;
/* 381 */       this.predicate = predicate;
/* 382 */       this.selector = (selector != null) ? selector : MergedAnnotationSelectors.<A>nearest();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public MergedAnnotation<A> doWithAggregate(Object context, int aggregateIndex) {
/* 388 */       return this.result;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public MergedAnnotation<A> doWithAnnotations(Object type, int aggregateIndex, @Nullable Object source, Annotation[] annotations) {
/* 396 */       for (Annotation annotation : annotations) {
/* 397 */         if (annotation != null && !TypeMappedAnnotations.this.annotationFilter.matches(annotation)) {
/* 398 */           MergedAnnotation<A> result = process(type, aggregateIndex, source, annotation);
/* 399 */           if (result != null) {
/* 400 */             return result;
/*     */           }
/*     */         } 
/*     */       } 
/* 404 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private MergedAnnotation<A> process(Object type, int aggregateIndex, @Nullable Object source, Annotation annotation) {
/* 411 */       Annotation[] repeatedAnnotations = TypeMappedAnnotations.this.repeatableContainers.findRepeatedAnnotations(annotation);
/* 412 */       if (repeatedAnnotations != null) {
/* 413 */         return doWithAnnotations(type, aggregateIndex, source, repeatedAnnotations);
/*     */       }
/* 415 */       AnnotationTypeMappings mappings = AnnotationTypeMappings.forAnnotationType(annotation
/* 416 */           .annotationType(), TypeMappedAnnotations.this.repeatableContainers, TypeMappedAnnotations.this.annotationFilter);
/* 417 */       for (int i = 0; i < mappings.size(); i++) {
/* 418 */         AnnotationTypeMapping mapping = mappings.get(i);
/* 419 */         if (TypeMappedAnnotations.isMappingForType(mapping, TypeMappedAnnotations.this.annotationFilter, this.requiredType)) {
/* 420 */           MergedAnnotation<A> candidate = TypeMappedAnnotation.createIfPossible(mapping, source, annotation, aggregateIndex, IntrospectionFailureLogger.INFO);
/*     */           
/* 422 */           if (candidate != null && (this.predicate == null || this.predicate.test(candidate))) {
/* 423 */             if (this.selector.isBestCandidate(candidate)) {
/* 424 */               return candidate;
/*     */             }
/* 426 */             updateLastResult(candidate);
/*     */           } 
/*     */         } 
/*     */       } 
/* 430 */       return null;
/*     */     }
/*     */     
/*     */     private void updateLastResult(MergedAnnotation<A> candidate) {
/* 434 */       MergedAnnotation<A> lastResult = this.result;
/* 435 */       this.result = (lastResult != null) ? this.selector.select(lastResult, candidate) : candidate;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public MergedAnnotation<A> finish(@Nullable MergedAnnotation<A> result) {
/* 441 */       return (result != null) ? result : this.result;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class AggregatesCollector
/*     */     implements AnnotationsProcessor<Object, List<Aggregate>>
/*     */   {
/* 451 */     private final List<TypeMappedAnnotations.Aggregate> aggregates = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public List<TypeMappedAnnotations.Aggregate> doWithAnnotations(Object criteria, int aggregateIndex, @Nullable Object source, Annotation[] annotations) {
/* 458 */       this.aggregates.add(createAggregate(aggregateIndex, source, annotations));
/* 459 */       return null;
/*     */     }
/*     */     
/*     */     private TypeMappedAnnotations.Aggregate createAggregate(int aggregateIndex, @Nullable Object source, Annotation[] annotations) {
/* 463 */       List<Annotation> aggregateAnnotations = getAggregateAnnotations(annotations);
/* 464 */       return new TypeMappedAnnotations.Aggregate(aggregateIndex, source, aggregateAnnotations);
/*     */     }
/*     */     
/*     */     private List<Annotation> getAggregateAnnotations(Annotation[] annotations) {
/* 468 */       List<Annotation> result = new ArrayList<>(annotations.length);
/* 469 */       addAggregateAnnotations(result, annotations);
/* 470 */       return result;
/*     */     }
/*     */     
/*     */     private void addAggregateAnnotations(List<Annotation> aggregateAnnotations, Annotation[] annotations) {
/* 474 */       for (Annotation annotation : annotations) {
/* 475 */         if (annotation != null && !TypeMappedAnnotations.this.annotationFilter.matches(annotation)) {
/* 476 */           Annotation[] repeatedAnnotations = TypeMappedAnnotations.this.repeatableContainers.findRepeatedAnnotations(annotation);
/* 477 */           if (repeatedAnnotations != null) {
/* 478 */             addAggregateAnnotations(aggregateAnnotations, repeatedAnnotations);
/*     */           } else {
/*     */             
/* 481 */             aggregateAnnotations.add(annotation);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public List<TypeMappedAnnotations.Aggregate> finish(@Nullable List<TypeMappedAnnotations.Aggregate> processResult) {
/* 489 */       return this.aggregates;
/*     */     }
/*     */ 
/*     */     
/*     */     private AggregatesCollector() {}
/*     */   }
/*     */ 
/*     */   
/*     */   private static class Aggregate
/*     */   {
/*     */     private final int aggregateIndex;
/*     */     @Nullable
/*     */     private final Object source;
/*     */     private final List<Annotation> annotations;
/*     */     private final AnnotationTypeMappings[] mappings;
/*     */     
/*     */     Aggregate(int aggregateIndex, @Nullable Object source, List<Annotation> annotations) {
/* 506 */       this.aggregateIndex = aggregateIndex;
/* 507 */       this.source = source;
/* 508 */       this.annotations = annotations;
/* 509 */       this.mappings = new AnnotationTypeMappings[annotations.size()];
/* 510 */       for (int i = 0; i < annotations.size(); i++) {
/* 511 */         this.mappings[i] = AnnotationTypeMappings.forAnnotationType(((Annotation)annotations.get(i)).annotationType());
/*     */       }
/*     */     }
/*     */     
/*     */     int size() {
/* 516 */       return this.annotations.size();
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     AnnotationTypeMapping getMapping(int annotationIndex, int mappingIndex) {
/* 521 */       AnnotationTypeMappings mappings = getMappings(annotationIndex);
/* 522 */       return (mappingIndex < mappings.size()) ? mappings.get(mappingIndex) : null;
/*     */     }
/*     */     
/*     */     AnnotationTypeMappings getMappings(int annotationIndex) {
/* 526 */       return this.mappings[annotationIndex];
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     <A extends Annotation> MergedAnnotation<A> createMergedAnnotationIfPossible(int annotationIndex, int mappingIndex, IntrospectionFailureLogger logger) {
/* 533 */       return TypeMappedAnnotation.createIfPossible(this.mappings[annotationIndex]
/* 534 */           .get(mappingIndex), this.source, this.annotations
/* 535 */           .get(annotationIndex), this.aggregateIndex, logger);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class AggregatesSpliterator<A extends Annotation>
/*     */     implements Spliterator<MergedAnnotation<A>>
/*     */   {
/*     */     @Nullable
/*     */     private final Object requiredType;
/*     */ 
/*     */     
/*     */     private final List<TypeMappedAnnotations.Aggregate> aggregates;
/*     */     
/*     */     private int aggregateCursor;
/*     */     
/*     */     @Nullable
/*     */     private int[] mappingCursors;
/*     */ 
/*     */     
/*     */     AggregatesSpliterator(Object requiredType, List<TypeMappedAnnotations.Aggregate> aggregates) {
/* 557 */       this.requiredType = requiredType;
/* 558 */       this.aggregates = aggregates;
/* 559 */       this.aggregateCursor = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryAdvance(Consumer<? super MergedAnnotation<A>> action) {
/* 564 */       while (this.aggregateCursor < this.aggregates.size()) {
/* 565 */         TypeMappedAnnotations.Aggregate aggregate = this.aggregates.get(this.aggregateCursor);
/* 566 */         if (tryAdvance(aggregate, action)) {
/* 567 */           return true;
/*     */         }
/* 569 */         this.aggregateCursor++;
/* 570 */         this.mappingCursors = null;
/*     */       } 
/* 572 */       return false;
/*     */     }
/*     */     
/*     */     private boolean tryAdvance(TypeMappedAnnotations.Aggregate aggregate, Consumer<? super MergedAnnotation<A>> action) {
/* 576 */       if (this.mappingCursors == null) {
/* 577 */         this.mappingCursors = new int[aggregate.size()];
/*     */       }
/* 579 */       int lowestDistance = Integer.MAX_VALUE;
/* 580 */       int annotationResult = -1;
/* 581 */       for (int annotationIndex = 0; annotationIndex < aggregate.size(); annotationIndex++) {
/* 582 */         AnnotationTypeMapping mapping = getNextSuitableMapping(aggregate, annotationIndex);
/* 583 */         if (mapping != null && mapping.getDistance() < lowestDistance) {
/* 584 */           annotationResult = annotationIndex;
/* 585 */           lowestDistance = mapping.getDistance();
/*     */         } 
/* 587 */         if (lowestDistance == 0) {
/*     */           break;
/*     */         }
/*     */       } 
/* 591 */       if (annotationResult != -1) {
/* 592 */         MergedAnnotation<A> mergedAnnotation = aggregate.createMergedAnnotationIfPossible(annotationResult, this.mappingCursors[annotationResult], (this.requiredType != null) ? IntrospectionFailureLogger.INFO : IntrospectionFailureLogger.DEBUG);
/*     */ 
/*     */         
/* 595 */         this.mappingCursors[annotationResult] = this.mappingCursors[annotationResult] + 1;
/* 596 */         if (mergedAnnotation == null) {
/* 597 */           return tryAdvance(aggregate, action);
/*     */         }
/* 599 */         action.accept(mergedAnnotation);
/* 600 */         return true;
/*     */       } 
/* 602 */       return false;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     private AnnotationTypeMapping getNextSuitableMapping(TypeMappedAnnotations.Aggregate aggregate, int annotationIndex) {
/* 607 */       int[] cursors = this.mappingCursors;
/* 608 */       if (cursors != null) {
/*     */         AnnotationTypeMapping mapping;
/*     */         do {
/* 611 */           mapping = aggregate.getMapping(annotationIndex, cursors[annotationIndex]);
/* 612 */           if (mapping != null && TypeMappedAnnotations.isMappingForType(mapping, TypeMappedAnnotations.this.annotationFilter, this.requiredType)) {
/* 613 */             return mapping;
/*     */           }
/* 615 */           cursors[annotationIndex] = cursors[annotationIndex] + 1;
/*     */         }
/* 617 */         while (mapping != null);
/*     */       } 
/* 619 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Spliterator<MergedAnnotation<A>> trySplit() {
/* 625 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public long estimateSize() {
/* 630 */       int size = 0;
/* 631 */       int aggregateIndex = this.aggregateCursor;
/* 632 */       for (; aggregateIndex < this.aggregates.size(); aggregateIndex++) {
/* 633 */         TypeMappedAnnotations.Aggregate aggregate = this.aggregates.get(aggregateIndex);
/* 634 */         for (int annotationIndex = 0; annotationIndex < aggregate.size(); annotationIndex++) {
/* 635 */           AnnotationTypeMappings mappings = aggregate.getMappings(annotationIndex);
/* 636 */           int numberOfMappings = mappings.size();
/* 637 */           if (aggregateIndex == this.aggregateCursor && this.mappingCursors != null) {
/* 638 */             numberOfMappings -= Math.min(this.mappingCursors[annotationIndex], mappings.size());
/*     */           }
/* 640 */           size += numberOfMappings;
/*     */         } 
/*     */       } 
/* 643 */       return size;
/*     */     }
/*     */ 
/*     */     
/*     */     public int characteristics() {
/* 648 */       return 1280;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\TypeMappedAnnotations.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */