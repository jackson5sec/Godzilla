/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ final class AnnotationTypeMapping
/*     */ {
/*  49 */   private static final MirrorSets.MirrorSet[] EMPTY_MIRROR_SETS = new MirrorSets.MirrorSet[0];
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private final AnnotationTypeMapping source;
/*     */   
/*     */   private final AnnotationTypeMapping root;
/*     */   
/*     */   private final int distance;
/*     */   
/*     */   private final Class<? extends Annotation> annotationType;
/*     */   
/*     */   private final List<Class<? extends Annotation>> metaTypes;
/*     */   
/*     */   @Nullable
/*     */   private final Annotation annotation;
/*     */   
/*     */   private final AttributeMethods attributes;
/*     */   
/*     */   private final MirrorSets mirrorSets;
/*     */   
/*     */   private final int[] aliasMappings;
/*     */   
/*     */   private final int[] conventionMappings;
/*     */   
/*     */   private final int[] annotationValueMappings;
/*     */   
/*     */   private final AnnotationTypeMapping[] annotationValueSource;
/*     */   
/*     */   private final Map<Method, List<Method>> aliasedBy;
/*     */   
/*     */   private final boolean synthesizable;
/*     */   
/*  82 */   private final Set<Method> claimedAliases = new HashSet<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AnnotationTypeMapping(@Nullable AnnotationTypeMapping source, Class<? extends Annotation> annotationType, @Nullable Annotation annotation) {
/*  88 */     this.source = source;
/*  89 */     this.root = (source != null) ? source.getRoot() : this;
/*  90 */     this.distance = (source == null) ? 0 : (source.getDistance() + 1);
/*  91 */     this.annotationType = annotationType;
/*  92 */     this.metaTypes = merge((source != null) ? source
/*  93 */         .getMetaTypes() : null, annotationType);
/*     */     
/*  95 */     this.annotation = annotation;
/*  96 */     this.attributes = AttributeMethods.forAnnotationType(annotationType);
/*  97 */     this.mirrorSets = new MirrorSets();
/*  98 */     this.aliasMappings = filledIntArray(this.attributes.size());
/*  99 */     this.conventionMappings = filledIntArray(this.attributes.size());
/* 100 */     this.annotationValueMappings = filledIntArray(this.attributes.size());
/* 101 */     this.annotationValueSource = new AnnotationTypeMapping[this.attributes.size()];
/* 102 */     this.aliasedBy = resolveAliasedForTargets();
/* 103 */     processAliases();
/* 104 */     addConventionMappings();
/* 105 */     addConventionAnnotationValues();
/* 106 */     this.synthesizable = computeSynthesizableFlag();
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> List<T> merge(@Nullable List<T> existing, T element) {
/* 111 */     if (existing == null) {
/* 112 */       return Collections.singletonList(element);
/*     */     }
/* 114 */     List<T> merged = new ArrayList<>(existing.size() + 1);
/* 115 */     merged.addAll(existing);
/* 116 */     merged.add(element);
/* 117 */     return Collections.unmodifiableList(merged);
/*     */   }
/*     */   
/*     */   private Map<Method, List<Method>> resolveAliasedForTargets() {
/* 121 */     Map<Method, List<Method>> aliasedBy = new HashMap<>();
/* 122 */     for (int i = 0; i < this.attributes.size(); i++) {
/* 123 */       Method attribute = this.attributes.get(i);
/* 124 */       AliasFor aliasFor = AnnotationsScanner.<AliasFor>getDeclaredAnnotation(attribute, AliasFor.class);
/* 125 */       if (aliasFor != null) {
/* 126 */         Method target = resolveAliasTarget(attribute, aliasFor);
/* 127 */         ((List<Method>)aliasedBy.computeIfAbsent(target, key -> new ArrayList())).add(attribute);
/*     */       } 
/*     */     } 
/* 130 */     return Collections.unmodifiableMap(aliasedBy);
/*     */   }
/*     */   
/*     */   private Method resolveAliasTarget(Method attribute, AliasFor aliasFor) {
/* 134 */     return resolveAliasTarget(attribute, aliasFor, true);
/*     */   }
/*     */   
/*     */   private Method resolveAliasTarget(Method attribute, AliasFor aliasFor, boolean checkAliasPair) {
/* 138 */     if (StringUtils.hasText(aliasFor.value()) && StringUtils.hasText(aliasFor.attribute()))
/* 139 */       throw new AnnotationConfigurationException(String.format("In @AliasFor declared on %s, attribute 'attribute' and its alias 'value' are present with values of '%s' and '%s', but only one is permitted.", new Object[] {
/*     */ 
/*     */               
/* 142 */               AttributeMethods.describe(attribute), aliasFor.attribute(), aliasFor
/* 143 */               .value()
/*     */             })); 
/* 145 */     Class<? extends Annotation> targetAnnotation = aliasFor.annotation();
/* 146 */     if (targetAnnotation == Annotation.class) {
/* 147 */       targetAnnotation = this.annotationType;
/*     */     }
/* 149 */     String targetAttributeName = aliasFor.attribute();
/* 150 */     if (!StringUtils.hasLength(targetAttributeName)) {
/* 151 */       targetAttributeName = aliasFor.value();
/*     */     }
/* 153 */     if (!StringUtils.hasLength(targetAttributeName)) {
/* 154 */       targetAttributeName = attribute.getName();
/*     */     }
/* 156 */     Method target = AttributeMethods.forAnnotationType(targetAnnotation).get(targetAttributeName);
/* 157 */     if (target == null) {
/* 158 */       if (targetAnnotation == this.annotationType)
/* 159 */         throw new AnnotationConfigurationException(String.format("@AliasFor declaration on %s declares an alias for '%s' which is not present.", new Object[] {
/*     */                 
/* 161 */                 AttributeMethods.describe(attribute), targetAttributeName
/*     */               })); 
/* 163 */       throw new AnnotationConfigurationException(String.format("%s is declared as an @AliasFor nonexistent %s.", new Object[] {
/*     */               
/* 165 */               StringUtils.capitalize(AttributeMethods.describe(attribute)), 
/* 166 */               AttributeMethods.describe(targetAnnotation, targetAttributeName) }));
/*     */     } 
/* 168 */     if (target.equals(attribute))
/* 169 */       throw new AnnotationConfigurationException(String.format("@AliasFor declaration on %s points to itself. Specify 'annotation' to point to a same-named attribute on a meta-annotation.", new Object[] {
/*     */ 
/*     */               
/* 172 */               AttributeMethods.describe(attribute)
/*     */             })); 
/* 174 */     if (!isCompatibleReturnType(attribute.getReturnType(), target.getReturnType()))
/* 175 */       throw new AnnotationConfigurationException(String.format("Misconfigured aliases: %s and %s must declare the same return type.", new Object[] {
/*     */               
/* 177 */               AttributeMethods.describe(attribute), 
/* 178 */               AttributeMethods.describe(target)
/*     */             })); 
/* 180 */     if (isAliasPair(target) && checkAliasPair) {
/* 181 */       AliasFor targetAliasFor = target.<AliasFor>getAnnotation(AliasFor.class);
/* 182 */       if (targetAliasFor != null) {
/* 183 */         Method mirror = resolveAliasTarget(target, targetAliasFor, false);
/* 184 */         if (!mirror.equals(attribute))
/* 185 */           throw new AnnotationConfigurationException(String.format("%s must be declared as an @AliasFor %s, not %s.", new Object[] {
/*     */                   
/* 187 */                   StringUtils.capitalize(AttributeMethods.describe(target)), 
/* 188 */                   AttributeMethods.describe(attribute), AttributeMethods.describe(mirror)
/*     */                 })); 
/*     */       } 
/*     */     } 
/* 192 */     return target;
/*     */   }
/*     */   
/*     */   private boolean isAliasPair(Method target) {
/* 196 */     return (this.annotationType == target.getDeclaringClass());
/*     */   }
/*     */   
/*     */   private boolean isCompatibleReturnType(Class<?> attributeType, Class<?> targetType) {
/* 200 */     return (attributeType == targetType || attributeType == targetType.getComponentType());
/*     */   }
/*     */   
/*     */   private void processAliases() {
/* 204 */     List<Method> aliases = new ArrayList<>();
/* 205 */     for (int i = 0; i < this.attributes.size(); i++) {
/* 206 */       aliases.clear();
/* 207 */       aliases.add(this.attributes.get(i));
/* 208 */       collectAliases(aliases);
/* 209 */       if (aliases.size() > 1) {
/* 210 */         processAliases(i, aliases);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void collectAliases(List<Method> aliases) {
/* 216 */     AnnotationTypeMapping mapping = this;
/* 217 */     while (mapping != null) {
/* 218 */       int size = aliases.size();
/* 219 */       for (int j = 0; j < size; j++) {
/* 220 */         List<Method> additional = mapping.aliasedBy.get(aliases.get(j));
/* 221 */         if (additional != null) {
/* 222 */           aliases.addAll(additional);
/*     */         }
/*     */       } 
/* 225 */       mapping = mapping.source;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processAliases(int attributeIndex, List<Method> aliases) {
/* 230 */     int rootAttributeIndex = getFirstRootAttributeIndex(aliases);
/* 231 */     AnnotationTypeMapping mapping = this;
/* 232 */     while (mapping != null) {
/* 233 */       if (rootAttributeIndex != -1 && mapping != this.root) {
/* 234 */         for (int i = 0; i < mapping.attributes.size(); i++) {
/* 235 */           if (aliases.contains(mapping.attributes.get(i))) {
/* 236 */             mapping.aliasMappings[i] = rootAttributeIndex;
/*     */           }
/*     */         } 
/*     */       }
/* 240 */       mapping.mirrorSets.updateFrom(aliases);
/* 241 */       mapping.claimedAliases.addAll(aliases);
/* 242 */       if (mapping.annotation != null) {
/* 243 */         int[] resolvedMirrors = mapping.mirrorSets.resolve(null, mapping.annotation, ReflectionUtils::invokeMethod);
/*     */         
/* 245 */         for (int i = 0; i < mapping.attributes.size(); i++) {
/* 246 */           if (aliases.contains(mapping.attributes.get(i))) {
/* 247 */             this.annotationValueMappings[attributeIndex] = resolvedMirrors[i];
/* 248 */             this.annotationValueSource[attributeIndex] = mapping;
/*     */           } 
/*     */         } 
/*     */       } 
/* 252 */       mapping = mapping.source;
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getFirstRootAttributeIndex(Collection<Method> aliases) {
/* 257 */     AttributeMethods rootAttributes = this.root.getAttributes();
/* 258 */     for (int i = 0; i < rootAttributes.size(); i++) {
/* 259 */       if (aliases.contains(rootAttributes.get(i))) {
/* 260 */         return i;
/*     */       }
/*     */     } 
/* 263 */     return -1;
/*     */   }
/*     */   
/*     */   private void addConventionMappings() {
/* 267 */     if (this.distance == 0) {
/*     */       return;
/*     */     }
/* 270 */     AttributeMethods rootAttributes = this.root.getAttributes();
/* 271 */     int[] mappings = this.conventionMappings;
/* 272 */     for (int i = 0; i < mappings.length; i++) {
/* 273 */       String name = this.attributes.get(i).getName();
/* 274 */       MirrorSets.MirrorSet mirrors = getMirrorSets().getAssigned(i);
/* 275 */       int mapped = rootAttributes.indexOf(name);
/* 276 */       if (!"value".equals(name) && mapped != -1) {
/* 277 */         mappings[i] = mapped;
/* 278 */         if (mirrors != null) {
/* 279 */           for (int j = 0; j < mirrors.size(); j++) {
/* 280 */             mappings[mirrors.getAttributeIndex(j)] = mapped;
/*     */           }
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addConventionAnnotationValues() {
/* 288 */     for (int i = 0; i < this.attributes.size(); i++) {
/* 289 */       Method attribute = this.attributes.get(i);
/* 290 */       boolean isValueAttribute = "value".equals(attribute.getName());
/* 291 */       AnnotationTypeMapping mapping = this;
/* 292 */       while (mapping != null && mapping.distance > 0) {
/* 293 */         int mapped = mapping.getAttributes().indexOf(attribute.getName());
/* 294 */         if (mapped != -1 && isBetterConventionAnnotationValue(i, isValueAttribute, mapping)) {
/* 295 */           this.annotationValueMappings[i] = mapped;
/* 296 */           this.annotationValueSource[i] = mapping;
/*     */         } 
/* 298 */         mapping = mapping.source;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isBetterConventionAnnotationValue(int index, boolean isValueAttribute, AnnotationTypeMapping mapping) {
/* 306 */     if (this.annotationValueMappings[index] == -1) {
/* 307 */       return true;
/*     */     }
/* 309 */     int existingDistance = (this.annotationValueSource[index]).distance;
/* 310 */     return (!isValueAttribute && existingDistance > mapping.distance);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean computeSynthesizableFlag() {
/* 316 */     for (int index : this.aliasMappings) {
/* 317 */       if (index != -1) {
/* 318 */         return true;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 323 */     if (!this.aliasedBy.isEmpty()) {
/* 324 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 328 */     for (int index : this.conventionMappings) {
/* 329 */       if (index != -1) {
/* 330 */         return true;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 335 */     if (getAttributes().hasNestedAnnotation()) {
/* 336 */       AttributeMethods attributeMethods = getAttributes();
/* 337 */       for (int i = 0; i < attributeMethods.size(); i++) {
/* 338 */         Method method = attributeMethods.get(i);
/* 339 */         Class<?> type = method.getReturnType();
/* 340 */         if (type.isAnnotation() || (type.isArray() && type.getComponentType().isAnnotation())) {
/*     */           
/* 342 */           Class<? extends Annotation> annotationType = type.isAnnotation() ? (Class)type : (Class)type.getComponentType();
/* 343 */           AnnotationTypeMapping mapping = AnnotationTypeMappings.forAnnotationType(annotationType).get(0);
/* 344 */           if (mapping.isSynthesizable()) {
/* 345 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 351 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void afterAllMappingsSet() {
/* 359 */     validateAllAliasesClaimed();
/* 360 */     for (int i = 0; i < this.mirrorSets.size(); i++) {
/* 361 */       validateMirrorSet(this.mirrorSets.get(i));
/*     */     }
/* 363 */     this.claimedAliases.clear();
/*     */   }
/*     */   
/*     */   private void validateAllAliasesClaimed() {
/* 367 */     for (int i = 0; i < this.attributes.size(); i++) {
/* 368 */       Method attribute = this.attributes.get(i);
/* 369 */       AliasFor aliasFor = AnnotationsScanner.<AliasFor>getDeclaredAnnotation(attribute, AliasFor.class);
/* 370 */       if (aliasFor != null && !this.claimedAliases.contains(attribute)) {
/* 371 */         Method target = resolveAliasTarget(attribute, aliasFor);
/* 372 */         throw new AnnotationConfigurationException(String.format("@AliasFor declaration on %s declares an alias for %s which is not meta-present.", new Object[] {
/*     */                 
/* 374 */                 AttributeMethods.describe(attribute), AttributeMethods.describe(target) }));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void validateMirrorSet(MirrorSets.MirrorSet mirrorSet) {
/* 380 */     Method firstAttribute = mirrorSet.get(0);
/* 381 */     Object firstDefaultValue = firstAttribute.getDefaultValue();
/* 382 */     for (int i = 1; i <= mirrorSet.size() - 1; i++) {
/* 383 */       Method mirrorAttribute = mirrorSet.get(i);
/* 384 */       Object mirrorDefaultValue = mirrorAttribute.getDefaultValue();
/* 385 */       if (firstDefaultValue == null || mirrorDefaultValue == null)
/* 386 */         throw new AnnotationConfigurationException(String.format("Misconfigured aliases: %s and %s must declare default values.", new Object[] {
/*     */                 
/* 388 */                 AttributeMethods.describe(firstAttribute), AttributeMethods.describe(mirrorAttribute)
/*     */               })); 
/* 390 */       if (!ObjectUtils.nullSafeEquals(firstDefaultValue, mirrorDefaultValue)) {
/* 391 */         throw new AnnotationConfigurationException(String.format("Misconfigured aliases: %s and %s must declare the same default value.", new Object[] {
/*     */                 
/* 393 */                 AttributeMethods.describe(firstAttribute), AttributeMethods.describe(mirrorAttribute)
/*     */               }));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AnnotationTypeMapping getRoot() {
/* 403 */     return this.root;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   AnnotationTypeMapping getSource() {
/* 412 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getDistance() {
/* 420 */     return this.distance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Class<? extends Annotation> getAnnotationType() {
/* 428 */     return this.annotationType;
/*     */   }
/*     */   
/*     */   List<Class<? extends Annotation>> getMetaTypes() {
/* 432 */     return this.metaTypes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   Annotation getAnnotation() {
/* 442 */     return this.annotation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AttributeMethods getAttributes() {
/* 450 */     return this.attributes;
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
/*     */   int getAliasMapping(int attributeIndex) {
/* 462 */     return this.aliasMappings[attributeIndex];
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
/*     */   int getConventionMapping(int attributeIndex) {
/* 474 */     return this.conventionMappings[attributeIndex];
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
/*     */   @Nullable
/*     */   Object getMappedAnnotationValue(int attributeIndex, boolean metaAnnotationsOnly) {
/* 491 */     int mappedIndex = this.annotationValueMappings[attributeIndex];
/* 492 */     if (mappedIndex == -1) {
/* 493 */       return null;
/*     */     }
/* 495 */     AnnotationTypeMapping source = this.annotationValueSource[attributeIndex];
/* 496 */     if (source == this && metaAnnotationsOnly) {
/* 497 */       return null;
/*     */     }
/* 499 */     return ReflectionUtils.invokeMethod(source.attributes.get(mappedIndex), source.annotation);
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
/*     */   boolean isEquivalentToDefaultValue(int attributeIndex, Object value, ValueExtractor valueExtractor) {
/* 513 */     Method attribute = this.attributes.get(attributeIndex);
/* 514 */     return isEquivalentToDefaultValue(attribute, value, valueExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   MirrorSets getMirrorSets() {
/* 522 */     return this.mirrorSets;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isSynthesizable() {
/* 533 */     return this.synthesizable;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int[] filledIntArray(int size) {
/* 538 */     int[] array = new int[size];
/* 539 */     Arrays.fill(array, -1);
/* 540 */     return array;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isEquivalentToDefaultValue(Method attribute, Object value, ValueExtractor valueExtractor) {
/* 546 */     return areEquivalent(attribute.getDefaultValue(), value, valueExtractor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean areEquivalent(@Nullable Object value, @Nullable Object extractedValue, ValueExtractor valueExtractor) {
/* 552 */     if (ObjectUtils.nullSafeEquals(value, extractedValue)) {
/* 553 */       return true;
/*     */     }
/* 555 */     if (value instanceof Class && extractedValue instanceof String) {
/* 556 */       return areEquivalent((Class)value, (String)extractedValue);
/*     */     }
/* 558 */     if (value instanceof Class[] && extractedValue instanceof String[]) {
/* 559 */       return areEquivalent((Class[])value, (String[])extractedValue);
/*     */     }
/* 561 */     if (value instanceof Annotation) {
/* 562 */       return areEquivalent((Annotation)value, extractedValue, valueExtractor);
/*     */     }
/* 564 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean areEquivalent(Class<?>[] value, String[] extractedValue) {
/* 568 */     if (value.length != extractedValue.length) {
/* 569 */       return false;
/*     */     }
/* 571 */     for (int i = 0; i < value.length; i++) {
/* 572 */       if (!areEquivalent(value[i], extractedValue[i])) {
/* 573 */         return false;
/*     */       }
/*     */     } 
/* 576 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean areEquivalent(Class<?> value, String extractedValue) {
/* 580 */     return value.getName().equals(extractedValue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean areEquivalent(Annotation annotation, @Nullable Object extractedValue, ValueExtractor valueExtractor) {
/* 586 */     AttributeMethods attributes = AttributeMethods.forAnnotationType(annotation.annotationType());
/* 587 */     for (int i = 0; i < attributes.size(); i++) {
/* 588 */       Object value2; Method attribute = attributes.get(i);
/* 589 */       Object value1 = ReflectionUtils.invokeMethod(attribute, annotation);
/*     */       
/* 591 */       if (extractedValue instanceof TypeMappedAnnotation) {
/* 592 */         value2 = ((TypeMappedAnnotation)extractedValue).getValue(attribute.getName()).orElse(null);
/*     */       } else {
/*     */         
/* 595 */         value2 = valueExtractor.extract(attribute, extractedValue);
/*     */       } 
/* 597 */       if (!areEquivalent(value1, value2, valueExtractor)) {
/* 598 */         return false;
/*     */       }
/*     */     } 
/* 601 */     return true;
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
/*     */   class MirrorSets
/*     */   {
/* 616 */     private final MirrorSet[] assigned = new MirrorSet[AnnotationTypeMapping.this.attributes.size()];
/* 617 */     private MirrorSet[] mirrorSets = AnnotationTypeMapping.EMPTY_MIRROR_SETS;
/*     */ 
/*     */     
/*     */     void updateFrom(Collection<Method> aliases) {
/* 621 */       MirrorSet mirrorSet = null;
/* 622 */       int size = 0;
/* 623 */       int last = -1;
/* 624 */       for (int i = 0; i < AnnotationTypeMapping.this.attributes.size(); i++) {
/* 625 */         Method attribute = AnnotationTypeMapping.this.attributes.get(i);
/* 626 */         if (aliases.contains(attribute)) {
/* 627 */           size++;
/* 628 */           if (size > 1) {
/* 629 */             if (mirrorSet == null) {
/* 630 */               mirrorSet = new MirrorSet();
/* 631 */               this.assigned[last] = mirrorSet;
/*     */             } 
/* 633 */             this.assigned[i] = mirrorSet;
/*     */           } 
/* 635 */           last = i;
/*     */         } 
/*     */       } 
/* 638 */       if (mirrorSet != null) {
/* 639 */         mirrorSet.update();
/* 640 */         Set<MirrorSet> unique = new LinkedHashSet<>(Arrays.asList(this.assigned));
/* 641 */         unique.remove(null);
/* 642 */         this.mirrorSets = unique.<MirrorSet>toArray(AnnotationTypeMapping.EMPTY_MIRROR_SETS);
/*     */       } 
/*     */     }
/*     */     
/*     */     int size() {
/* 647 */       return this.mirrorSets.length;
/*     */     }
/*     */     
/*     */     MirrorSet get(int index) {
/* 651 */       return this.mirrorSets[index];
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     MirrorSet getAssigned(int attributeIndex) {
/* 656 */       return this.assigned[attributeIndex];
/*     */     }
/*     */     
/*     */     int[] resolve(@Nullable Object source, @Nullable Object annotation, ValueExtractor valueExtractor) {
/* 660 */       int[] result = new int[AnnotationTypeMapping.this.attributes.size()]; int i;
/* 661 */       for (i = 0; i < result.length; i++) {
/* 662 */         result[i] = i;
/*     */       }
/* 664 */       for (i = 0; i < size(); i++) {
/* 665 */         MirrorSet mirrorSet = get(i);
/* 666 */         int resolved = mirrorSet.resolve(source, annotation, valueExtractor);
/* 667 */         for (int j = 0; j < mirrorSet.size; j++) {
/* 668 */           result[mirrorSet.indexes[j]] = resolved;
/*     */         }
/*     */       } 
/* 671 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     class MirrorSet
/*     */     {
/*     */       private int size;
/*     */ 
/*     */       
/* 682 */       private final int[] indexes = new int[AnnotationTypeMapping.this.attributes.size()];
/*     */       
/*     */       void update() {
/* 685 */         this.size = 0;
/* 686 */         Arrays.fill(this.indexes, -1);
/* 687 */         for (int i = 0; i < AnnotationTypeMapping.MirrorSets.this.assigned.length; i++) {
/* 688 */           if (AnnotationTypeMapping.MirrorSets.this.assigned[i] == this) {
/* 689 */             this.indexes[this.size] = i;
/* 690 */             this.size++;
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/*     */       <A> int resolve(@Nullable Object source, @Nullable A annotation, ValueExtractor valueExtractor) {
/* 696 */         int result = -1;
/* 697 */         Object lastValue = null;
/* 698 */         for (int i = 0; i < this.size; i++) {
/* 699 */           Method attribute = AnnotationTypeMapping.this.attributes.get(this.indexes[i]);
/* 700 */           Object value = valueExtractor.extract(attribute, annotation);
/*     */           
/* 702 */           boolean isDefaultValue = (value == null || AnnotationTypeMapping.isEquivalentToDefaultValue(attribute, value, valueExtractor));
/* 703 */           if (isDefaultValue || ObjectUtils.nullSafeEquals(lastValue, value)) {
/* 704 */             if (result == -1) {
/* 705 */               result = this.indexes[i];
/*     */             }
/*     */           } else {
/*     */             
/* 709 */             if (lastValue != null && !ObjectUtils.nullSafeEquals(lastValue, value)) {
/* 710 */               String on = (source != null) ? (" declared on " + source) : "";
/* 711 */               throw new AnnotationConfigurationException(String.format("Different @AliasFor mirror values for annotation [%s]%s; attribute '%s' and its alias '%s' are declared with values of [%s] and [%s].", new Object[] { this.this$1.this$0
/*     */ 
/*     */                       
/* 714 */                       .getAnnotationType().getName(), on, 
/* 715 */                       AnnotationTypeMapping.access$000(this.this$1.this$0).get(result).getName(), attribute
/* 716 */                       .getName(), 
/* 717 */                       ObjectUtils.nullSafeToString(lastValue), 
/* 718 */                       ObjectUtils.nullSafeToString(value) }));
/*     */             } 
/* 720 */             result = this.indexes[i];
/* 721 */             lastValue = value;
/*     */           } 
/* 723 */         }  return result;
/*     */       }
/*     */       
/*     */       int size() {
/* 727 */         return this.size;
/*     */       }
/*     */       
/*     */       Method get(int index) {
/* 731 */         int attributeIndex = this.indexes[index];
/* 732 */         return AnnotationTypeMapping.this.attributes.get(attributeIndex);
/*     */       }
/*     */       
/*     */       int getAttributeIndex(int index) {
/* 736 */         return this.indexes[index]; } } } class MirrorSet { int getAttributeIndex(int index) { return this.indexes[index]; }
/*     */ 
/*     */     
/*     */     private int size;
/*     */     private final int[] indexes;
/*     */     
/*     */     MirrorSet() {
/*     */       this.indexes = new int[AnnotationTypeMapping.this.attributes.size()];
/*     */     }
/*     */     
/*     */     void update() {
/*     */       this.size = 0;
/*     */       Arrays.fill(this.indexes, -1);
/*     */       for (int i = 0; i < AnnotationTypeMapping.MirrorSets.this.assigned.length; i++) {
/*     */         if (AnnotationTypeMapping.MirrorSets.this.assigned[i] == this) {
/*     */           this.indexes[this.size] = i;
/*     */           this.size++;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     <A> int resolve(@Nullable Object source, @Nullable A annotation, ValueExtractor valueExtractor) {
/*     */       int result = -1;
/*     */       Object lastValue = null;
/*     */       for (int i = 0; i < this.size; i++) {
/*     */         Method attribute = AnnotationTypeMapping.this.attributes.get(this.indexes[i]);
/*     */         Object value = valueExtractor.extract(attribute, annotation);
/*     */         boolean isDefaultValue = (value == null || AnnotationTypeMapping.isEquivalentToDefaultValue(attribute, value, valueExtractor));
/*     */         if (isDefaultValue || ObjectUtils.nullSafeEquals(lastValue, value)) {
/*     */           if (result == -1)
/*     */             result = this.indexes[i]; 
/*     */         } else {
/*     */           if (lastValue != null && !ObjectUtils.nullSafeEquals(lastValue, value)) {
/*     */             String on = (source != null) ? (" declared on " + source) : "";
/*     */             throw new AnnotationConfigurationException(String.format("Different @AliasFor mirror values for annotation [%s]%s; attribute '%s' and its alias '%s' are declared with values of [%s] and [%s].", new Object[] { this.this$1.this$0.getAnnotationType().getName(), on, AnnotationTypeMapping.access$000(this.this$1.this$0).get(result).getName(), attribute.getName(), ObjectUtils.nullSafeToString(lastValue), ObjectUtils.nullSafeToString(value) }));
/*     */           } 
/*     */           result = this.indexes[i];
/*     */           lastValue = value;
/*     */         } 
/*     */       } 
/*     */       return result;
/*     */     }
/*     */     
/*     */     int size() {
/*     */       return this.size;
/*     */     }
/*     */     
/*     */     Method get(int index) {
/*     */       int attributeIndex = this.indexes[index];
/*     */       return AnnotationTypeMapping.this.attributes.get(attributeIndex);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\AnnotationTypeMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */