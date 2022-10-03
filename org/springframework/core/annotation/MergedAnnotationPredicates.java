/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ public abstract class MergedAnnotationPredicates
/*     */ {
/*     */   public static <A extends Annotation> Predicate<MergedAnnotation<? extends A>> typeIn(String... typeNames) {
/*  52 */     return annotation -> ObjectUtils.containsElement((Object[])typeNames, annotation.getType().getName());
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
/*     */   public static <A extends Annotation> Predicate<MergedAnnotation<? extends A>> typeIn(Class<?>... types) {
/*  64 */     return annotation -> ObjectUtils.containsElement((Object[])types, annotation.getType());
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
/*     */   public static <A extends Annotation> Predicate<MergedAnnotation<? extends A>> typeIn(Collection<?> types) {
/*  76 */     return annotation -> types.stream().map(()).anyMatch(());
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static <A extends Annotation> Predicate<MergedAnnotation<A>> firstRunOf(Function<? super MergedAnnotation<A>, ?> valueExtractor) {
/*  98 */     return new FirstRunOfPredicate<>(valueExtractor);
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
/*     */   public static <A extends Annotation, K> Predicate<MergedAnnotation<A>> unique(Function<? super MergedAnnotation<A>, K> keyExtractor) {
/* 114 */     return new UniquePredicate<>(keyExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FirstRunOfPredicate<A extends Annotation>
/*     */     implements Predicate<MergedAnnotation<A>>
/*     */   {
/*     */     private final Function<? super MergedAnnotation<A>, ?> valueExtractor;
/*     */ 
/*     */     
/*     */     private boolean hasLastValue;
/*     */     
/*     */     @Nullable
/*     */     private Object lastValue;
/*     */ 
/*     */     
/*     */     FirstRunOfPredicate(Function<? super MergedAnnotation<A>, ?> valueExtractor) {
/* 132 */       Assert.notNull(valueExtractor, "Value extractor must not be null");
/* 133 */       this.valueExtractor = valueExtractor;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(@Nullable MergedAnnotation<A> annotation) {
/* 138 */       if (!this.hasLastValue) {
/* 139 */         this.hasLastValue = true;
/* 140 */         this.lastValue = this.valueExtractor.apply(annotation);
/*     */       } 
/* 142 */       Object value = this.valueExtractor.apply(annotation);
/* 143 */       return ObjectUtils.nullSafeEquals(value, this.lastValue);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class UniquePredicate<A extends Annotation, K>
/*     */     implements Predicate<MergedAnnotation<A>>
/*     */   {
/*     */     private final Function<? super MergedAnnotation<A>, K> keyExtractor;
/*     */ 
/*     */ 
/*     */     
/* 157 */     private final Set<K> seen = new HashSet<>();
/*     */     
/*     */     UniquePredicate(Function<? super MergedAnnotation<A>, K> keyExtractor) {
/* 160 */       Assert.notNull(keyExtractor, "Key extractor must not be null");
/* 161 */       this.keyExtractor = keyExtractor;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(@Nullable MergedAnnotation<A> annotation) {
/* 166 */       K key = this.keyExtractor.apply(annotation);
/* 167 */       return this.seen.add(key);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\MergedAnnotationPredicates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */