/*     */ package org.springframework.core.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ abstract class AnnotationsScanner
/*     */ {
/*  47 */   private static final Annotation[] NO_ANNOTATIONS = new Annotation[0];
/*     */   
/*  49 */   private static final Method[] NO_METHODS = new Method[0];
/*     */ 
/*     */   
/*  52 */   private static final Map<AnnotatedElement, Annotation[]> declaredAnnotationCache = (Map<AnnotatedElement, Annotation[]>)new ConcurrentReferenceHashMap(256);
/*     */ 
/*     */   
/*  55 */   private static final Map<Class<?>, Method[]> baseTypeMethodsCache = (Map<Class<?>, Method[]>)new ConcurrentReferenceHashMap(256);
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
/*     */   @Nullable
/*     */   static <C, R> R scan(C context, AnnotatedElement source, MergedAnnotations.SearchStrategy searchStrategy, AnnotationsProcessor<C, R> processor) {
/*  77 */     R result = process(context, source, searchStrategy, processor);
/*  78 */     return processor.finish(result);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static <C, R> R process(C context, AnnotatedElement source, MergedAnnotations.SearchStrategy searchStrategy, AnnotationsProcessor<C, R> processor) {
/*  85 */     if (source instanceof Class) {
/*  86 */       return processClass(context, (Class)source, searchStrategy, processor);
/*     */     }
/*  88 */     if (source instanceof Method) {
/*  89 */       return processMethod(context, (Method)source, searchStrategy, processor);
/*     */     }
/*  91 */     return processElement(context, source, processor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static <C, R> R processClass(C context, Class<?> source, MergedAnnotations.SearchStrategy searchStrategy, AnnotationsProcessor<C, R> processor) {
/*  98 */     switch (searchStrategy) {
/*     */       case DIRECT:
/* 100 */         return processElement(context, source, processor);
/*     */       case INHERITED_ANNOTATIONS:
/* 102 */         return processClassInheritedAnnotations(context, source, searchStrategy, processor);
/*     */       case SUPERCLASS:
/* 104 */         return processClassHierarchy(context, source, processor, false, false);
/*     */       case TYPE_HIERARCHY:
/* 106 */         return processClassHierarchy(context, source, processor, true, false);
/*     */       case TYPE_HIERARCHY_AND_ENCLOSING_CLASSES:
/* 108 */         return processClassHierarchy(context, source, processor, true, true);
/*     */     } 
/* 110 */     throw new IllegalStateException("Unsupported search strategy " + searchStrategy);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static <C, R> R processClassInheritedAnnotations(C context, Class<?> source, MergedAnnotations.SearchStrategy searchStrategy, AnnotationsProcessor<C, R> processor) {
/*     */     try {
/* 118 */       if (isWithoutHierarchy(source, searchStrategy)) {
/* 119 */         return processElement(context, source, processor);
/*     */       }
/* 121 */       Annotation[] relevant = null;
/* 122 */       int remaining = Integer.MAX_VALUE;
/* 123 */       int aggregateIndex = 0;
/* 124 */       Class<?> root = source;
/* 125 */       while (source != null && source != Object.class && remaining > 0 && 
/* 126 */         !hasPlainJavaAnnotationsOnly(source)) {
/* 127 */         R result = processor.doWithAggregate(context, aggregateIndex);
/* 128 */         if (result != null) {
/* 129 */           return result;
/*     */         }
/* 131 */         Annotation[] declaredAnnotations = getDeclaredAnnotations(source, true);
/* 132 */         if (relevant == null && declaredAnnotations.length > 0) {
/* 133 */           relevant = root.getAnnotations();
/* 134 */           remaining = relevant.length;
/*     */         } 
/* 136 */         for (int i = 0; i < declaredAnnotations.length; i++) {
/* 137 */           if (declaredAnnotations[i] != null) {
/* 138 */             boolean isRelevant = false;
/* 139 */             for (int relevantIndex = 0; relevantIndex < relevant.length; relevantIndex++) {
/* 140 */               if (relevant[relevantIndex] != null && declaredAnnotations[i]
/* 141 */                 .annotationType() == relevant[relevantIndex].annotationType()) {
/* 142 */                 isRelevant = true;
/* 143 */                 relevant[relevantIndex] = null;
/* 144 */                 remaining--;
/*     */                 break;
/*     */               } 
/*     */             } 
/* 148 */             if (!isRelevant) {
/* 149 */               declaredAnnotations[i] = null;
/*     */             }
/*     */           } 
/*     */         } 
/* 153 */         result = processor.doWithAnnotations(context, aggregateIndex, source, declaredAnnotations);
/* 154 */         if (result != null) {
/* 155 */           return result;
/*     */         }
/* 157 */         source = source.getSuperclass();
/* 158 */         aggregateIndex++;
/*     */       }
/*     */     
/* 161 */     } catch (Throwable ex) {
/* 162 */       AnnotationUtils.handleIntrospectionFailure(source, ex);
/*     */     } 
/* 164 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static <C, R> R processClassHierarchy(C context, Class<?> source, AnnotationsProcessor<C, R> processor, boolean includeInterfaces, boolean includeEnclosing) {
/* 171 */     return processClassHierarchy(context, new int[] { 0 }, source, processor, includeInterfaces, includeEnclosing);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static <C, R> R processClassHierarchy(C context, int[] aggregateIndex, Class<?> source, AnnotationsProcessor<C, R> processor, boolean includeInterfaces, boolean includeEnclosing) {
/*     */     try {
/* 180 */       R result = processor.doWithAggregate(context, aggregateIndex[0]);
/* 181 */       if (result != null) {
/* 182 */         return result;
/*     */       }
/* 184 */       if (hasPlainJavaAnnotationsOnly(source)) {
/* 185 */         return null;
/*     */       }
/* 187 */       Annotation[] annotations = getDeclaredAnnotations(source, false);
/* 188 */       result = processor.doWithAnnotations(context, aggregateIndex[0], source, annotations);
/* 189 */       if (result != null) {
/* 190 */         return result;
/*     */       }
/* 192 */       aggregateIndex[0] = aggregateIndex[0] + 1;
/* 193 */       if (includeInterfaces) {
/* 194 */         for (Class<?> interfaceType : source.getInterfaces()) {
/* 195 */           R interfacesResult = processClassHierarchy(context, aggregateIndex, interfaceType, processor, true, includeEnclosing);
/*     */           
/* 197 */           if (interfacesResult != null) {
/* 198 */             return interfacesResult;
/*     */           }
/*     */         } 
/*     */       }
/* 202 */       Class<?> superclass = source.getSuperclass();
/* 203 */       if (superclass != Object.class && superclass != null) {
/* 204 */         R superclassResult = processClassHierarchy(context, aggregateIndex, superclass, processor, includeInterfaces, includeEnclosing);
/*     */         
/* 206 */         if (superclassResult != null) {
/* 207 */           return superclassResult;
/*     */         }
/*     */       } 
/* 210 */       if (includeEnclosing) {
/*     */         
/*     */         try {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 217 */           Class<?> enclosingClass = source.getEnclosingClass();
/* 218 */           if (enclosingClass != null) {
/* 219 */             R enclosingResult = processClassHierarchy(context, aggregateIndex, enclosingClass, processor, includeInterfaces, true);
/*     */             
/* 221 */             if (enclosingResult != null) {
/* 222 */               return enclosingResult;
/*     */             }
/*     */           }
/*     */         
/* 226 */         } catch (Throwable ex) {
/* 227 */           AnnotationUtils.handleIntrospectionFailure(source, ex);
/*     */         }
/*     */       
/*     */       }
/* 231 */     } catch (Throwable ex) {
/* 232 */       AnnotationUtils.handleIntrospectionFailure(source, ex);
/*     */     } 
/* 234 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static <C, R> R processMethod(C context, Method source, MergedAnnotations.SearchStrategy searchStrategy, AnnotationsProcessor<C, R> processor) {
/* 241 */     switch (searchStrategy) {
/*     */       case DIRECT:
/*     */       case INHERITED_ANNOTATIONS:
/* 244 */         return processMethodInheritedAnnotations(context, source, processor);
/*     */       case SUPERCLASS:
/* 246 */         return processMethodHierarchy(context, new int[] { 0 }, source.getDeclaringClass(), processor, source, false);
/*     */       
/*     */       case TYPE_HIERARCHY:
/*     */       case TYPE_HIERARCHY_AND_ENCLOSING_CLASSES:
/* 250 */         return processMethodHierarchy(context, new int[] { 0 }, source.getDeclaringClass(), processor, source, true);
/*     */     } 
/*     */     
/* 253 */     throw new IllegalStateException("Unsupported search strategy " + searchStrategy);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static <C, R> R processMethodInheritedAnnotations(C context, Method source, AnnotationsProcessor<C, R> processor) {
/*     */     try {
/* 261 */       R result = processor.doWithAggregate(context, 0);
/* 262 */       return (result != null) ? result : 
/* 263 */         processMethodAnnotations(context, 0, source, processor);
/*     */     }
/* 265 */     catch (Throwable ex) {
/* 266 */       AnnotationUtils.handleIntrospectionFailure(source, ex);
/*     */       
/* 268 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static <C, R> R processMethodHierarchy(C context, int[] aggregateIndex, Class<?> sourceClass, AnnotationsProcessor<C, R> processor, Method rootMethod, boolean includeInterfaces) {
/*     */     try {
/* 277 */       R result = processor.doWithAggregate(context, aggregateIndex[0]);
/* 278 */       if (result != null) {
/* 279 */         return result;
/*     */       }
/* 281 */       if (hasPlainJavaAnnotationsOnly(sourceClass)) {
/* 282 */         return null;
/*     */       }
/* 284 */       boolean calledProcessor = false;
/* 285 */       if (sourceClass == rootMethod.getDeclaringClass()) {
/* 286 */         result = processMethodAnnotations(context, aggregateIndex[0], rootMethod, processor);
/*     */         
/* 288 */         calledProcessor = true;
/* 289 */         if (result != null) {
/* 290 */           return result;
/*     */         }
/*     */       } else {
/*     */         
/* 294 */         for (Method candidateMethod : getBaseTypeMethods(context, sourceClass)) {
/* 295 */           if (candidateMethod != null && isOverride(rootMethod, candidateMethod)) {
/* 296 */             result = processMethodAnnotations(context, aggregateIndex[0], candidateMethod, processor);
/*     */             
/* 298 */             calledProcessor = true;
/* 299 */             if (result != null) {
/* 300 */               return result;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/* 305 */       if (Modifier.isPrivate(rootMethod.getModifiers())) {
/* 306 */         return null;
/*     */       }
/* 308 */       if (calledProcessor) {
/* 309 */         aggregateIndex[0] = aggregateIndex[0] + 1;
/*     */       }
/* 311 */       if (includeInterfaces) {
/* 312 */         for (Class<?> interfaceType : sourceClass.getInterfaces()) {
/* 313 */           R interfacesResult = processMethodHierarchy(context, aggregateIndex, interfaceType, processor, rootMethod, true);
/*     */           
/* 315 */           if (interfacesResult != null) {
/* 316 */             return interfacesResult;
/*     */           }
/*     */         } 
/*     */       }
/* 320 */       Class<?> superclass = sourceClass.getSuperclass();
/* 321 */       if (superclass != Object.class && superclass != null) {
/* 322 */         R superclassResult = processMethodHierarchy(context, aggregateIndex, superclass, processor, rootMethod, includeInterfaces);
/*     */         
/* 324 */         if (superclassResult != null) {
/* 325 */           return superclassResult;
/*     */         }
/*     */       }
/*     */     
/* 329 */     } catch (Throwable ex) {
/* 330 */       AnnotationUtils.handleIntrospectionFailure(rootMethod, ex);
/*     */     } 
/* 332 */     return null;
/*     */   }
/*     */   
/*     */   private static <C> Method[] getBaseTypeMethods(C context, Class<?> baseType) {
/* 336 */     if (baseType == Object.class || hasPlainJavaAnnotationsOnly(baseType)) {
/* 337 */       return NO_METHODS;
/*     */     }
/*     */     
/* 340 */     Method[] methods = baseTypeMethodsCache.get(baseType);
/* 341 */     if (methods == null) {
/* 342 */       boolean isInterface = baseType.isInterface();
/* 343 */       methods = isInterface ? baseType.getMethods() : ReflectionUtils.getDeclaredMethods(baseType);
/* 344 */       int cleared = 0;
/* 345 */       for (int i = 0; i < methods.length; i++) {
/* 346 */         if ((!isInterface && Modifier.isPrivate(methods[i].getModifiers())) || 
/* 347 */           hasPlainJavaAnnotationsOnly(methods[i]) || (
/* 348 */           getDeclaredAnnotations(methods[i], false)).length == 0) {
/* 349 */           methods[i] = null;
/* 350 */           cleared++;
/*     */         } 
/*     */       } 
/* 353 */       if (cleared == methods.length) {
/* 354 */         methods = NO_METHODS;
/*     */       }
/* 356 */       baseTypeMethodsCache.put(baseType, methods);
/*     */     } 
/* 358 */     return methods;
/*     */   }
/*     */   
/*     */   private static boolean isOverride(Method rootMethod, Method candidateMethod) {
/* 362 */     return (!Modifier.isPrivate(candidateMethod.getModifiers()) && candidateMethod
/* 363 */       .getName().equals(rootMethod.getName()) && 
/* 364 */       hasSameParameterTypes(rootMethod, candidateMethod));
/*     */   }
/*     */   
/*     */   private static boolean hasSameParameterTypes(Method rootMethod, Method candidateMethod) {
/* 368 */     if (candidateMethod.getParameterCount() != rootMethod.getParameterCount()) {
/* 369 */       return false;
/*     */     }
/* 371 */     Class<?>[] rootParameterTypes = rootMethod.getParameterTypes();
/* 372 */     Class<?>[] candidateParameterTypes = candidateMethod.getParameterTypes();
/* 373 */     if (Arrays.equals((Object[])candidateParameterTypes, (Object[])rootParameterTypes)) {
/* 374 */       return true;
/*     */     }
/* 376 */     return hasSameGenericTypeParameters(rootMethod, candidateMethod, rootParameterTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean hasSameGenericTypeParameters(Method rootMethod, Method candidateMethod, Class<?>[] rootParameterTypes) {
/* 383 */     Class<?> sourceDeclaringClass = rootMethod.getDeclaringClass();
/* 384 */     Class<?> candidateDeclaringClass = candidateMethod.getDeclaringClass();
/* 385 */     if (!candidateDeclaringClass.isAssignableFrom(sourceDeclaringClass)) {
/* 386 */       return false;
/*     */     }
/* 388 */     for (int i = 0; i < rootParameterTypes.length; i++) {
/*     */       
/* 390 */       Class<?> resolvedParameterType = ResolvableType.forMethodParameter(candidateMethod, i, sourceDeclaringClass).resolve();
/* 391 */       if (rootParameterTypes[i] != resolvedParameterType) {
/* 392 */         return false;
/*     */       }
/*     */     } 
/* 395 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static <C, R> R processMethodAnnotations(C context, int aggregateIndex, Method source, AnnotationsProcessor<C, R> processor) {
/* 402 */     Annotation[] annotations = getDeclaredAnnotations(source, false);
/* 403 */     R result = processor.doWithAnnotations(context, aggregateIndex, source, annotations);
/* 404 */     if (result != null) {
/* 405 */       return result;
/*     */     }
/* 407 */     Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(source);
/* 408 */     if (bridgedMethod != source) {
/* 409 */       Annotation[] bridgedAnnotations = getDeclaredAnnotations(bridgedMethod, true);
/* 410 */       for (int i = 0; i < bridgedAnnotations.length; i++) {
/* 411 */         if (ObjectUtils.containsElement((Object[])annotations, bridgedAnnotations[i])) {
/* 412 */           bridgedAnnotations[i] = null;
/*     */         }
/*     */       } 
/* 415 */       return processor.doWithAnnotations(context, aggregateIndex, source, bridgedAnnotations);
/*     */     } 
/* 417 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static <C, R> R processElement(C context, AnnotatedElement source, AnnotationsProcessor<C, R> processor) {
/*     */     try {
/* 425 */       R result = processor.doWithAggregate(context, 0);
/* 426 */       return (result != null) ? result : processor.doWithAnnotations(context, 0, source, 
/* 427 */           getDeclaredAnnotations(source, false));
/*     */     }
/* 429 */     catch (Throwable ex) {
/* 430 */       AnnotationUtils.handleIntrospectionFailure(source, ex);
/*     */       
/* 432 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   static <A extends Annotation> A getDeclaredAnnotation(AnnotatedElement source, Class<A> annotationType) {
/* 438 */     Annotation[] annotations = getDeclaredAnnotations(source, false);
/* 439 */     for (Annotation annotation : annotations) {
/* 440 */       if (annotation != null && annotationType == annotation.annotationType()) {
/* 441 */         return (A)annotation;
/*     */       }
/*     */     } 
/* 444 */     return null;
/*     */   }
/*     */   
/*     */   static Annotation[] getDeclaredAnnotations(AnnotatedElement source, boolean defensive) {
/* 448 */     boolean cached = false;
/* 449 */     Annotation[] annotations = declaredAnnotationCache.get(source);
/* 450 */     if (annotations != null) {
/* 451 */       cached = true;
/*     */     } else {
/*     */       
/* 454 */       annotations = source.getDeclaredAnnotations();
/* 455 */       if (annotations.length != 0) {
/* 456 */         boolean allIgnored = true;
/* 457 */         for (int i = 0; i < annotations.length; i++) {
/* 458 */           Annotation annotation = annotations[i];
/* 459 */           if (isIgnorable(annotation.annotationType()) || 
/* 460 */             !AttributeMethods.forAnnotationType(annotation.annotationType()).isValid(annotation)) {
/* 461 */             annotations[i] = null;
/*     */           } else {
/*     */             
/* 464 */             allIgnored = false;
/*     */           } 
/*     */         } 
/* 467 */         annotations = allIgnored ? NO_ANNOTATIONS : annotations;
/* 468 */         if (source instanceof Class || source instanceof Member) {
/* 469 */           declaredAnnotationCache.put(source, annotations);
/* 470 */           cached = true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 474 */     if (!defensive || annotations.length == 0 || !cached) {
/* 475 */       return annotations;
/*     */     }
/* 477 */     return (Annotation[])annotations.clone();
/*     */   }
/*     */   
/*     */   private static boolean isIgnorable(Class<?> annotationType) {
/* 481 */     return AnnotationFilter.PLAIN.matches(annotationType);
/*     */   }
/*     */   
/*     */   static boolean isKnownEmpty(AnnotatedElement source, MergedAnnotations.SearchStrategy searchStrategy) {
/* 485 */     if (hasPlainJavaAnnotationsOnly(source)) {
/* 486 */       return true;
/*     */     }
/* 488 */     if (searchStrategy == MergedAnnotations.SearchStrategy.DIRECT || isWithoutHierarchy(source, searchStrategy)) {
/* 489 */       if (source instanceof Method && ((Method)source).isBridge()) {
/* 490 */         return false;
/*     */       }
/* 492 */       return ((getDeclaredAnnotations(source, false)).length == 0);
/*     */     } 
/* 494 */     return false;
/*     */   }
/*     */   
/*     */   static boolean hasPlainJavaAnnotationsOnly(@Nullable Object annotatedElement) {
/* 498 */     if (annotatedElement instanceof Class) {
/* 499 */       return hasPlainJavaAnnotationsOnly((Class)annotatedElement);
/*     */     }
/* 501 */     if (annotatedElement instanceof Member) {
/* 502 */       return hasPlainJavaAnnotationsOnly(((Member)annotatedElement).getDeclaringClass());
/*     */     }
/*     */     
/* 505 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean hasPlainJavaAnnotationsOnly(Class<?> type) {
/* 510 */     return (type.getName().startsWith("java.") || type == Ordered.class);
/*     */   }
/*     */   
/*     */   private static boolean isWithoutHierarchy(AnnotatedElement source, MergedAnnotations.SearchStrategy searchStrategy) {
/* 514 */     if (source == Object.class) {
/* 515 */       return true;
/*     */     }
/* 517 */     if (source instanceof Class) {
/* 518 */       Class<?> sourceClass = (Class)source;
/*     */       
/* 520 */       boolean noSuperTypes = (sourceClass.getSuperclass() == Object.class && (sourceClass.getInterfaces()).length == 0);
/* 521 */       return (searchStrategy == MergedAnnotations.SearchStrategy.TYPE_HIERARCHY_AND_ENCLOSING_CLASSES) ? ((noSuperTypes && sourceClass
/* 522 */         .getEnclosingClass() == null)) : noSuperTypes;
/*     */     } 
/* 524 */     if (source instanceof Method) {
/* 525 */       Method sourceMethod = (Method)source;
/* 526 */       return (Modifier.isPrivate(sourceMethod.getModifiers()) || 
/* 527 */         isWithoutHierarchy(sourceMethod.getDeclaringClass(), searchStrategy));
/*     */     } 
/* 529 */     return true;
/*     */   }
/*     */   
/*     */   static void clearCache() {
/* 533 */     declaredAnnotationCache.clear();
/* 534 */     baseTypeMethodsCache.clear();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\annotation\AnnotationsScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */