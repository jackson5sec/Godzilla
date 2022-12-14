/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collector;
/*     */ import org.springframework.util.MultiValueMap;
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
/*     */ public abstract class MergedAnnotationCollectors
/*     */ {
/*  43 */   private static final Collector.Characteristics[] NO_CHARACTERISTICS = new Collector.Characteristics[0];
/*     */   
/*  45 */   private static final Collector.Characteristics[] IDENTITY_FINISH_CHARACTERISTICS = new Collector.Characteristics[] { Collector.Characteristics.IDENTITY_FINISH };
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
/*     */   public static <A extends Annotation> Collector<MergedAnnotation<A>, ?, Set<A>> toAnnotationSet() {
/*  64 */     return (Collector)Collector.of(java.util.LinkedHashSet::new, (set, annotation) -> set.add(annotation.synthesize()), MergedAnnotationCollectors::combiner, new Collector.Characteristics[0]);
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
/*     */   public static <A extends Annotation> Collector<MergedAnnotation<A>, ?, Annotation[]> toAnnotationArray() {
/*  78 */     return (Collector)toAnnotationArray(x$0 -> new Annotation[x$0]);
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
/*     */   
/*     */   public static <R extends Annotation, A extends R> Collector<MergedAnnotation<A>, ?, R[]> toAnnotationArray(IntFunction<R[]> generator) {
/*  96 */     return Collector.of(ArrayList::new, (list, annotation) -> list.add(annotation.synthesize()), MergedAnnotationCollectors::combiner, list -> (Annotation[])list.toArray(generator.apply(list.size())), new Collector.Characteristics[0]);
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
/*     */   
/*     */   public static <A extends Annotation> Collector<MergedAnnotation<A>, ?, MultiValueMap<String, Object>> toMultiValueMap(MergedAnnotation.Adapt... adaptations) {
/* 114 */     return toMultiValueMap(Function.identity(), adaptations);
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
/*     */ 
/*     */   
/*     */   public static <A extends Annotation> Collector<MergedAnnotation<A>, ?, MultiValueMap<String, Object>> toMultiValueMap(Function<MultiValueMap<String, Object>, MultiValueMap<String, Object>> finisher, MergedAnnotation.Adapt... adaptations) {
/* 133 */     Collector.Characteristics[] characteristics = isSameInstance(finisher, Function.identity()) ? IDENTITY_FINISH_CHARACTERISTICS : NO_CHARACTERISTICS;
/*     */     
/* 135 */     return Collector.of(org.springframework.util.LinkedMultiValueMap::new, (map, annotation) -> annotation.asMap(adaptations).forEach(map::add), MergedAnnotationCollectors::combiner, finisher, characteristics);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isSameInstance(Object instance, Object candidate) {
/* 142 */     return (instance == candidate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <E, C extends Collection<E>> C combiner(C collection, C additions) {
/* 151 */     collection.addAll((Collection)additions);
/* 152 */     return collection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K, V> MultiValueMap<K, V> combiner(MultiValueMap<K, V> map, MultiValueMap<K, V> additions) {
/* 161 */     map.addAll(additions);
/* 162 */     return map;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\MergedAnnotationCollectors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */