/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
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
/*     */ final class MergedAnnotationsCollection
/*     */   implements MergedAnnotations
/*     */ {
/*     */   private final MergedAnnotation<?>[] annotations;
/*     */   private final AnnotationTypeMappings[] mappings;
/*     */   
/*     */   private MergedAnnotationsCollection(Collection<MergedAnnotation<?>> annotations) {
/*  48 */     Assert.notNull(annotations, "Annotations must not be null");
/*  49 */     this.annotations = (MergedAnnotation<?>[])annotations.<MergedAnnotation>toArray(new MergedAnnotation[0]);
/*  50 */     this.mappings = new AnnotationTypeMappings[this.annotations.length];
/*  51 */     for (int i = 0; i < this.annotations.length; i++) {
/*  52 */       MergedAnnotation<?> annotation = this.annotations[i];
/*  53 */       Assert.notNull(annotation, "Annotation must not be null");
/*  54 */       Assert.isTrue(annotation.isDirectlyPresent(), "Annotation must be directly present");
/*  55 */       Assert.isTrue((annotation.getAggregateIndex() == 0), "Annotation must have aggregate index of zero");
/*  56 */       this.mappings[i] = AnnotationTypeMappings.forAnnotationType((Class)annotation.getType());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<MergedAnnotation<Annotation>> iterator() {
/*  63 */     return Spliterators.iterator(spliterator());
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<MergedAnnotation<Annotation>> spliterator() {
/*  68 */     return spliterator(null);
/*     */   }
/*     */   
/*     */   private <A extends Annotation> Spliterator<MergedAnnotation<A>> spliterator(@Nullable Object annotationType) {
/*  72 */     return new AnnotationsSpliterator<>(annotationType);
/*     */   }
/*     */ 
/*     */   
/*     */   public <A extends Annotation> boolean isPresent(Class<A> annotationType) {
/*  77 */     return isPresent(annotationType, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPresent(String annotationType) {
/*  82 */     return isPresent(annotationType, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public <A extends Annotation> boolean isDirectlyPresent(Class<A> annotationType) {
/*  87 */     return isPresent(annotationType, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDirectlyPresent(String annotationType) {
/*  92 */     return isPresent(annotationType, true);
/*     */   }
/*     */   
/*     */   private boolean isPresent(Object requiredType, boolean directOnly) {
/*  96 */     for (MergedAnnotation<?> annotation : this.annotations) {
/*  97 */       Class<? extends Annotation> type = (Class)annotation.getType();
/*  98 */       if (type == requiredType || type.getName().equals(requiredType)) {
/*  99 */         return true;
/*     */       }
/*     */     } 
/* 102 */     if (!directOnly) {
/* 103 */       for (AnnotationTypeMappings mappings : this.mappings) {
/* 104 */         for (int i = 1; i < mappings.size(); i++) {
/* 105 */           AnnotationTypeMapping mapping = mappings.get(i);
/* 106 */           if (isMappingForType(mapping, requiredType)) {
/* 107 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/* 112 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public <A extends Annotation> MergedAnnotation<A> get(Class<A> annotationType) {
/* 117 */     return get(annotationType, (Predicate<? super MergedAnnotation<A>>)null, (MergedAnnotationSelector<A>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> MergedAnnotation<A> get(Class<A> annotationType, @Nullable Predicate<? super MergedAnnotation<A>> predicate) {
/* 124 */     return get(annotationType, predicate, (MergedAnnotationSelector<A>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> MergedAnnotation<A> get(Class<A> annotationType, @Nullable Predicate<? super MergedAnnotation<A>> predicate, @Nullable MergedAnnotationSelector<A> selector) {
/* 132 */     MergedAnnotation<A> result = find(annotationType, predicate, selector);
/* 133 */     return (result != null) ? result : MergedAnnotation.<A>missing();
/*     */   }
/*     */ 
/*     */   
/*     */   public <A extends Annotation> MergedAnnotation<A> get(String annotationType) {
/* 138 */     return get(annotationType, (Predicate<? super MergedAnnotation<A>>)null, (MergedAnnotationSelector<A>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> MergedAnnotation<A> get(String annotationType, @Nullable Predicate<? super MergedAnnotation<A>> predicate) {
/* 145 */     return get(annotationType, predicate, (MergedAnnotationSelector<A>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> MergedAnnotation<A> get(String annotationType, @Nullable Predicate<? super MergedAnnotation<A>> predicate, @Nullable MergedAnnotationSelector<A> selector) {
/* 153 */     MergedAnnotation<A> result = find(annotationType, predicate, selector);
/* 154 */     return (result != null) ? result : MergedAnnotation.<A>missing();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private <A extends Annotation> MergedAnnotation<A> find(Object requiredType, @Nullable Predicate<? super MergedAnnotation<A>> predicate, @Nullable MergedAnnotationSelector<A> selector) {
/* 163 */     if (selector == null) {
/* 164 */       selector = MergedAnnotationSelectors.nearest();
/*     */     }
/*     */     
/* 167 */     MergedAnnotation<A> result = null;
/* 168 */     for (int i = 0; i < this.annotations.length; i++) {
/* 169 */       MergedAnnotation<?> root = this.annotations[i];
/* 170 */       AnnotationTypeMappings mappings = this.mappings[i];
/* 171 */       for (int mappingIndex = 0; mappingIndex < mappings.size(); mappingIndex++) {
/* 172 */         AnnotationTypeMapping mapping = mappings.get(mappingIndex);
/* 173 */         if (isMappingForType(mapping, requiredType)) {
/*     */ 
/*     */ 
/*     */           
/* 177 */           MergedAnnotation<A> candidate = (mappingIndex == 0) ? (MergedAnnotation)root : TypeMappedAnnotation.<A>createIfPossible(mapping, root, IntrospectionFailureLogger.INFO);
/* 178 */           if (candidate != null && (predicate == null || predicate.test(candidate))) {
/* 179 */             if (selector.isBestCandidate(candidate)) {
/* 180 */               return candidate;
/*     */             }
/* 182 */             result = (result != null) ? selector.select(result, candidate) : candidate;
/*     */           } 
/*     */         } 
/*     */       } 
/* 186 */     }  return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public <A extends Annotation> Stream<MergedAnnotation<A>> stream(Class<A> annotationType) {
/* 191 */     return StreamSupport.stream(spliterator(annotationType), false);
/*     */   }
/*     */ 
/*     */   
/*     */   public <A extends Annotation> Stream<MergedAnnotation<A>> stream(String annotationType) {
/* 196 */     return StreamSupport.stream(spliterator(annotationType), false);
/*     */   }
/*     */ 
/*     */   
/*     */   public Stream<MergedAnnotation<Annotation>> stream() {
/* 201 */     return StreamSupport.stream(spliterator(), false);
/*     */   }
/*     */   
/*     */   private static boolean isMappingForType(AnnotationTypeMapping mapping, @Nullable Object requiredType) {
/* 205 */     if (requiredType == null) {
/* 206 */       return true;
/*     */     }
/* 208 */     Class<? extends Annotation> actualType = mapping.getAnnotationType();
/* 209 */     return (actualType == requiredType || actualType.getName().equals(requiredType));
/*     */   }
/*     */   
/*     */   static MergedAnnotations of(Collection<MergedAnnotation<?>> annotations) {
/* 213 */     Assert.notNull(annotations, "Annotations must not be null");
/* 214 */     if (annotations.isEmpty()) {
/* 215 */       return TypeMappedAnnotations.NONE;
/*     */     }
/* 217 */     return new MergedAnnotationsCollection(annotations);
/*     */   }
/*     */ 
/*     */   
/*     */   private class AnnotationsSpliterator<A extends Annotation>
/*     */     implements Spliterator<MergedAnnotation<A>>
/*     */   {
/*     */     @Nullable
/*     */     private Object requiredType;
/*     */     private final int[] mappingCursors;
/*     */     
/*     */     public AnnotationsSpliterator(Object requiredType) {
/* 229 */       this.mappingCursors = new int[MergedAnnotationsCollection.this.annotations.length];
/* 230 */       this.requiredType = requiredType;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean tryAdvance(Consumer<? super MergedAnnotation<A>> action) {
/* 235 */       int lowestDistance = Integer.MAX_VALUE;
/* 236 */       int annotationResult = -1;
/* 237 */       for (int annotationIndex = 0; annotationIndex < MergedAnnotationsCollection.this.annotations.length; annotationIndex++) {
/* 238 */         AnnotationTypeMapping mapping = getNextSuitableMapping(annotationIndex);
/* 239 */         if (mapping != null && mapping.getDistance() < lowestDistance) {
/* 240 */           annotationResult = annotationIndex;
/* 241 */           lowestDistance = mapping.getDistance();
/*     */         } 
/* 243 */         if (lowestDistance == 0) {
/*     */           break;
/*     */         }
/*     */       } 
/* 247 */       if (annotationResult != -1) {
/* 248 */         MergedAnnotation<A> mergedAnnotation = createMergedAnnotationIfPossible(annotationResult, this.mappingCursors[annotationResult]);
/*     */         
/* 250 */         this.mappingCursors[annotationResult] = this.mappingCursors[annotationResult] + 1;
/* 251 */         if (mergedAnnotation == null) {
/* 252 */           return tryAdvance(action);
/*     */         }
/* 254 */         action.accept(mergedAnnotation);
/* 255 */         return true;
/*     */       } 
/* 257 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private AnnotationTypeMapping getNextSuitableMapping(int annotationIndex) {
/*     */       while (true) {
/* 264 */         AnnotationTypeMapping mapping = getMapping(annotationIndex, this.mappingCursors[annotationIndex]);
/* 265 */         if (mapping != null && MergedAnnotationsCollection.isMappingForType(mapping, this.requiredType)) {
/* 266 */           return mapping;
/*     */         }
/* 268 */         this.mappingCursors[annotationIndex] = this.mappingCursors[annotationIndex] + 1;
/*     */         
/* 270 */         if (mapping == null)
/* 271 */           return null; 
/*     */       } 
/*     */     }
/*     */     @Nullable
/*     */     private AnnotationTypeMapping getMapping(int annotationIndex, int mappingIndex) {
/* 276 */       AnnotationTypeMappings mappings = MergedAnnotationsCollection.this.mappings[annotationIndex];
/* 277 */       return (mappingIndex < mappings.size()) ? mappings.get(mappingIndex) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private MergedAnnotation<A> createMergedAnnotationIfPossible(int annotationIndex, int mappingIndex) {
/* 283 */       MergedAnnotation<?> root = MergedAnnotationsCollection.this.annotations[annotationIndex];
/* 284 */       if (mappingIndex == 0) {
/* 285 */         return (MergedAnnotation)root;
/*     */       }
/* 287 */       IntrospectionFailureLogger logger = (this.requiredType != null) ? IntrospectionFailureLogger.INFO : IntrospectionFailureLogger.DEBUG;
/*     */       
/* 289 */       return TypeMappedAnnotation.createIfPossible(MergedAnnotationsCollection.this
/* 290 */           .mappings[annotationIndex].get(mappingIndex), root, logger);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Spliterator<MergedAnnotation<A>> trySplit() {
/* 296 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public long estimateSize() {
/* 301 */       int size = 0;
/* 302 */       for (int i = 0; i < MergedAnnotationsCollection.this.annotations.length; i++) {
/* 303 */         AnnotationTypeMappings mappings = MergedAnnotationsCollection.this.mappings[i];
/* 304 */         int numberOfMappings = mappings.size();
/* 305 */         numberOfMappings -= Math.min(this.mappingCursors[i], mappings.size());
/* 306 */         size += numberOfMappings;
/*     */       } 
/* 308 */       return size;
/*     */     }
/*     */ 
/*     */     
/*     */     public int characteristics() {
/* 313 */       return 1280;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\MergedAnnotationsCollection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */