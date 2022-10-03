/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
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
/*     */ final class MissingMergedAnnotation<A extends Annotation>
/*     */   extends AbstractMergedAnnotation<A>
/*     */ {
/*  41 */   private static final MissingMergedAnnotation<?> INSTANCE = new MissingMergedAnnotation();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<A> getType() {
/*  50 */     throw new NoSuchElementException("Unable to get type for missing annotation");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPresent() {
/*  55 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object getSource() {
/*  61 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MergedAnnotation<?> getMetaSource() {
/*  67 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public MergedAnnotation<?> getRoot() {
/*  72 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Class<? extends Annotation>> getMetaTypes() {
/*  77 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDistance() {
/*  82 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAggregateIndex() {
/*  87 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasNonDefaultValue(String attributeName) {
/*  92 */     throw new NoSuchElementException("Unable to check non-default value for missing annotation");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasDefaultValue(String attributeName) {
/*  98 */     throw new NoSuchElementException("Unable to check default value for missing annotation");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Optional<T> getValue(String attributeName, Class<T> type) {
/* 104 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Optional<T> getDefaultValue(@Nullable String attributeName, Class<T> type) {
/* 109 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public MergedAnnotation<A> filterAttributes(Predicate<String> predicate) {
/* 114 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public MergedAnnotation<A> withNonMergedAttributes() {
/* 119 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AnnotationAttributes asAnnotationAttributes(MergedAnnotation.Adapt... adaptations) {
/* 124 */     return new AnnotationAttributes();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> asMap(MergedAnnotation.Adapt... adaptations) {
/* 129 */     return Collections.emptyMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends Map<String, Object>> T asMap(Function<MergedAnnotation<?>, T> factory, MergedAnnotation.Adapt... adaptations) {
/* 134 */     return factory.apply(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 139 */     return "(missing)";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Annotation> MergedAnnotation<T> getAnnotation(String attributeName, Class<T> type) throws NoSuchElementException {
/* 146 */     throw new NoSuchElementException("Unable to get attribute value for missing annotation");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Annotation> MergedAnnotation<T>[] getAnnotationArray(String attributeName, Class<T> type) throws NoSuchElementException {
/* 154 */     throw new NoSuchElementException("Unable to get attribute value for missing annotation");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> T getAttributeValue(String attributeName, Class<T> type) {
/* 160 */     throw new NoSuchElementException("Unable to get attribute value for missing annotation");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected A createSynthesized() {
/* 166 */     throw new NoSuchElementException("Unable to synthesize missing annotation");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static <A extends Annotation> MergedAnnotation<A> getInstance() {
/* 172 */     return (MergedAnnotation)INSTANCE;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\MissingMergedAnnotation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */