/*      */ package org.springframework.core;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.GenericArrayType;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.ParameterizedType;
/*      */ import java.lang.reflect.Type;
/*      */ import java.lang.reflect.TypeVariable;
/*      */ import java.lang.reflect.WildcardType;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.StringJoiner;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.ConcurrentReferenceHashMap;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ResolvableType
/*      */   implements Serializable
/*      */ {
/*   90 */   public static final ResolvableType NONE = new ResolvableType(EmptyType.INSTANCE, null, null, Integer.valueOf(0));
/*      */   
/*   92 */   private static final ResolvableType[] EMPTY_TYPES_ARRAY = new ResolvableType[0];
/*      */   
/*   94 */   private static final ConcurrentReferenceHashMap<ResolvableType, ResolvableType> cache = new ConcurrentReferenceHashMap(256);
/*      */ 
/*      */ 
/*      */   
/*      */   private final Type type;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private final SerializableTypeWrapper.TypeProvider typeProvider;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private final VariableResolver variableResolver;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private final ResolvableType componentType;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private final Integer hash;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private Class<?> resolved;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private volatile ResolvableType superType;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private volatile ResolvableType[] interfaces;
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private volatile ResolvableType[] generics;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ResolvableType(Type type, @Nullable SerializableTypeWrapper.TypeProvider typeProvider, @Nullable VariableResolver variableResolver) {
/*  144 */     this.type = type;
/*  145 */     this.typeProvider = typeProvider;
/*  146 */     this.variableResolver = variableResolver;
/*  147 */     this.componentType = null;
/*  148 */     this.hash = Integer.valueOf(calculateHashCode());
/*  149 */     this.resolved = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ResolvableType(Type type, @Nullable SerializableTypeWrapper.TypeProvider typeProvider, @Nullable VariableResolver variableResolver, @Nullable Integer hash) {
/*  160 */     this.type = type;
/*  161 */     this.typeProvider = typeProvider;
/*  162 */     this.variableResolver = variableResolver;
/*  163 */     this.componentType = null;
/*  164 */     this.hash = hash;
/*  165 */     this.resolved = resolveClass();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ResolvableType(Type type, @Nullable SerializableTypeWrapper.TypeProvider typeProvider, @Nullable VariableResolver variableResolver, @Nullable ResolvableType componentType) {
/*  175 */     this.type = type;
/*  176 */     this.typeProvider = typeProvider;
/*  177 */     this.variableResolver = variableResolver;
/*  178 */     this.componentType = componentType;
/*  179 */     this.hash = null;
/*  180 */     this.resolved = resolveClass();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ResolvableType(@Nullable Class<?> clazz) {
/*  189 */     this.resolved = (clazz != null) ? clazz : Object.class;
/*  190 */     this.type = this.resolved;
/*  191 */     this.typeProvider = null;
/*  192 */     this.variableResolver = null;
/*  193 */     this.componentType = null;
/*  194 */     this.hash = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Type getType() {
/*  202 */     return SerializableTypeWrapper.unwrap(this.type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Class<?> getRawClass() {
/*  211 */     if (this.type == this.resolved) {
/*  212 */       return this.resolved;
/*      */     }
/*  214 */     Type rawType = this.type;
/*  215 */     if (rawType instanceof ParameterizedType) {
/*  216 */       rawType = ((ParameterizedType)rawType).getRawType();
/*      */     }
/*  218 */     return (rawType instanceof Class) ? (Class)rawType : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getSource() {
/*  229 */     Object source = (this.typeProvider != null) ? this.typeProvider.getSource() : null;
/*  230 */     return (source != null) ? source : this.type;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class<?> toClass() {
/*  242 */     return resolve(Object.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInstance(@Nullable Object obj) {
/*  252 */     return (obj != null && isAssignableFrom(obj.getClass()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAssignableFrom(Class<?> other) {
/*  263 */     return isAssignableFrom(forClass(other), null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAssignableFrom(ResolvableType other) {
/*  278 */     return isAssignableFrom(other, null);
/*      */   }
/*      */   
/*      */   private boolean isAssignableFrom(ResolvableType other, @Nullable Map<Type, Type> matchedBefore) {
/*  282 */     Assert.notNull(other, "ResolvableType must not be null");
/*      */ 
/*      */     
/*  285 */     if (this == NONE || other == NONE) {
/*  286 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  290 */     if (isArray()) {
/*  291 */       return (other.isArray() && getComponentType().isAssignableFrom(other.getComponentType()));
/*      */     }
/*      */     
/*  294 */     if (matchedBefore != null && matchedBefore.get(this.type) == other.type) {
/*  295 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  299 */     WildcardBounds ourBounds = WildcardBounds.get(this);
/*  300 */     WildcardBounds typeBounds = WildcardBounds.get(other);
/*      */ 
/*      */     
/*  303 */     if (typeBounds != null) {
/*  304 */       return (ourBounds != null && ourBounds.isSameKind(typeBounds) && ourBounds
/*  305 */         .isAssignableFrom(typeBounds.getBounds()));
/*      */     }
/*      */ 
/*      */     
/*  309 */     if (ourBounds != null) {
/*  310 */       return ourBounds.isAssignableFrom(new ResolvableType[] { other });
/*      */     }
/*      */ 
/*      */     
/*  314 */     boolean exactMatch = (matchedBefore != null);
/*  315 */     boolean checkGenerics = true;
/*  316 */     Class<?> ourResolved = null;
/*  317 */     if (this.type instanceof TypeVariable) {
/*  318 */       TypeVariable<?> variable = (TypeVariable)this.type;
/*      */       
/*  320 */       if (this.variableResolver != null) {
/*  321 */         ResolvableType resolved = this.variableResolver.resolveVariable(variable);
/*  322 */         if (resolved != null) {
/*  323 */           ourResolved = resolved.resolve();
/*      */         }
/*      */       } 
/*  326 */       if (ourResolved == null)
/*      */       {
/*  328 */         if (other.variableResolver != null) {
/*  329 */           ResolvableType resolved = other.variableResolver.resolveVariable(variable);
/*  330 */           if (resolved != null) {
/*  331 */             ourResolved = resolved.resolve();
/*  332 */             checkGenerics = false;
/*      */           } 
/*      */         } 
/*      */       }
/*  336 */       if (ourResolved == null)
/*      */       {
/*  338 */         exactMatch = false;
/*      */       }
/*      */     } 
/*  341 */     if (ourResolved == null) {
/*  342 */       ourResolved = resolve(Object.class);
/*      */     }
/*  344 */     Class<?> otherResolved = other.toClass();
/*      */ 
/*      */ 
/*      */     
/*  348 */     if (exactMatch ? !ourResolved.equals(otherResolved) : !ClassUtils.isAssignable(ourResolved, otherResolved)) {
/*  349 */       return false;
/*      */     }
/*      */     
/*  352 */     if (checkGenerics) {
/*      */       
/*  354 */       ResolvableType[] ourGenerics = getGenerics();
/*  355 */       ResolvableType[] typeGenerics = other.as(ourResolved).getGenerics();
/*  356 */       if (ourGenerics.length != typeGenerics.length) {
/*  357 */         return false;
/*      */       }
/*  359 */       if (matchedBefore == null) {
/*  360 */         matchedBefore = new IdentityHashMap<>(1);
/*      */       }
/*  362 */       matchedBefore.put(this.type, other.type);
/*  363 */       for (int i = 0; i < ourGenerics.length; i++) {
/*  364 */         if (!ourGenerics[i].isAssignableFrom(typeGenerics[i], matchedBefore)) {
/*  365 */           return false;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  370 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isArray() {
/*  378 */     if (this == NONE) {
/*  379 */       return false;
/*      */     }
/*  381 */     return ((this.type instanceof Class && ((Class)this.type).isArray()) || this.type instanceof GenericArrayType || 
/*  382 */       resolveType().isArray());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType getComponentType() {
/*  391 */     if (this == NONE) {
/*  392 */       return NONE;
/*      */     }
/*  394 */     if (this.componentType != null) {
/*  395 */       return this.componentType;
/*      */     }
/*  397 */     if (this.type instanceof Class) {
/*  398 */       Class<?> componentType = ((Class)this.type).getComponentType();
/*  399 */       return forType(componentType, this.variableResolver);
/*      */     } 
/*  401 */     if (this.type instanceof GenericArrayType) {
/*  402 */       return forType(((GenericArrayType)this.type).getGenericComponentType(), this.variableResolver);
/*      */     }
/*  404 */     return resolveType().getComponentType();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType asCollection() {
/*  415 */     return as(Collection.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType asMap() {
/*  426 */     return as(Map.class);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType as(Class<?> type) {
/*  443 */     if (this == NONE) {
/*  444 */       return NONE;
/*      */     }
/*  446 */     Class<?> resolved = resolve();
/*  447 */     if (resolved == null || resolved == type) {
/*  448 */       return this;
/*      */     }
/*  450 */     for (ResolvableType interfaceType : getInterfaces()) {
/*  451 */       ResolvableType interfaceAsType = interfaceType.as(type);
/*  452 */       if (interfaceAsType != NONE) {
/*  453 */         return interfaceAsType;
/*      */       }
/*      */     } 
/*  456 */     return getSuperType().as(type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType getSuperType() {
/*  466 */     Class<?> resolved = resolve();
/*  467 */     if (resolved == null) {
/*  468 */       return NONE;
/*      */     }
/*      */     try {
/*  471 */       Type superclass = resolved.getGenericSuperclass();
/*  472 */       if (superclass == null) {
/*  473 */         return NONE;
/*      */       }
/*  475 */       ResolvableType superType = this.superType;
/*  476 */       if (superType == null) {
/*  477 */         superType = forType(superclass, this);
/*  478 */         this.superType = superType;
/*      */       } 
/*  480 */       return superType;
/*      */     }
/*  482 */     catch (TypeNotPresentException ex) {
/*      */       
/*  484 */       return NONE;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType[] getInterfaces() {
/*  496 */     Class<?> resolved = resolve();
/*  497 */     if (resolved == null) {
/*  498 */       return EMPTY_TYPES_ARRAY;
/*      */     }
/*  500 */     ResolvableType[] interfaces = this.interfaces;
/*  501 */     if (interfaces == null) {
/*  502 */       Type[] genericIfcs = resolved.getGenericInterfaces();
/*  503 */       interfaces = new ResolvableType[genericIfcs.length];
/*  504 */       for (int i = 0; i < genericIfcs.length; i++) {
/*  505 */         interfaces[i] = forType(genericIfcs[i], this);
/*      */       }
/*  507 */       this.interfaces = interfaces;
/*      */     } 
/*  509 */     return interfaces;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasGenerics() {
/*  518 */     return ((getGenerics()).length > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isEntirelyUnresolvable() {
/*  526 */     if (this == NONE) {
/*  527 */       return false;
/*      */     }
/*  529 */     ResolvableType[] generics = getGenerics();
/*  530 */     for (ResolvableType generic : generics) {
/*  531 */       if (!generic.isUnresolvableTypeVariable() && !generic.isWildcardWithoutBounds()) {
/*  532 */         return false;
/*      */       }
/*      */     } 
/*  535 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasUnresolvableGenerics() {
/*  546 */     if (this == NONE) {
/*  547 */       return false;
/*      */     }
/*  549 */     ResolvableType[] generics = getGenerics();
/*  550 */     for (ResolvableType generic : generics) {
/*  551 */       if (generic.isUnresolvableTypeVariable() || generic.isWildcardWithoutBounds()) {
/*  552 */         return true;
/*      */       }
/*      */     } 
/*  555 */     Class<?> resolved = resolve();
/*  556 */     if (resolved != null) {
/*      */       try {
/*  558 */         for (Type genericInterface : resolved.getGenericInterfaces()) {
/*  559 */           if (genericInterface instanceof Class && 
/*  560 */             forClass((Class)genericInterface).hasGenerics()) {
/*  561 */             return true;
/*      */           }
/*      */         }
/*      */       
/*      */       }
/*  566 */       catch (TypeNotPresentException typeNotPresentException) {}
/*      */ 
/*      */       
/*  569 */       return getSuperType().hasUnresolvableGenerics();
/*      */     } 
/*  571 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isUnresolvableTypeVariable() {
/*  579 */     if (this.type instanceof TypeVariable) {
/*  580 */       if (this.variableResolver == null) {
/*  581 */         return true;
/*      */       }
/*  583 */       TypeVariable<?> variable = (TypeVariable)this.type;
/*  584 */       ResolvableType resolved = this.variableResolver.resolveVariable(variable);
/*  585 */       if (resolved == null || resolved.isUnresolvableTypeVariable()) {
/*  586 */         return true;
/*      */       }
/*      */     } 
/*  589 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isWildcardWithoutBounds() {
/*  597 */     if (this.type instanceof WildcardType) {
/*  598 */       WildcardType wt = (WildcardType)this.type;
/*  599 */       if ((wt.getLowerBounds()).length == 0) {
/*  600 */         Type[] upperBounds = wt.getUpperBounds();
/*  601 */         if (upperBounds.length == 0 || (upperBounds.length == 1 && Object.class == upperBounds[0])) {
/*  602 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*  606 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType getNested(int nestingLevel) {
/*  616 */     return getNested(nestingLevel, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType getNested(int nestingLevel, @Nullable Map<Integer, Integer> typeIndexesPerLevel) {
/*  640 */     ResolvableType result = this;
/*  641 */     for (int i = 2; i <= nestingLevel; i++) {
/*  642 */       if (result.isArray()) {
/*  643 */         result = result.getComponentType();
/*      */       }
/*      */       else {
/*      */         
/*  647 */         while (result != NONE && !result.hasGenerics()) {
/*  648 */           result = result.getSuperType();
/*      */         }
/*  650 */         Integer index = (typeIndexesPerLevel != null) ? typeIndexesPerLevel.get(Integer.valueOf(i)) : null;
/*  651 */         index = Integer.valueOf((index == null) ? ((result.getGenerics()).length - 1) : index.intValue());
/*  652 */         result = result.getGeneric(new int[] { index.intValue() });
/*      */       } 
/*      */     } 
/*  655 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType getGeneric(@Nullable int... indexes) {
/*  676 */     ResolvableType[] generics = getGenerics();
/*  677 */     if (indexes == null || indexes.length == 0) {
/*  678 */       return (generics.length == 0) ? NONE : generics[0];
/*      */     }
/*  680 */     ResolvableType generic = this;
/*  681 */     for (int index : indexes) {
/*  682 */       generics = generic.getGenerics();
/*  683 */       if (index < 0 || index >= generics.length) {
/*  684 */         return NONE;
/*      */       }
/*  686 */       generic = generics[index];
/*      */     } 
/*  688 */     return generic;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResolvableType[] getGenerics() {
/*  705 */     if (this == NONE) {
/*  706 */       return EMPTY_TYPES_ARRAY;
/*      */     }
/*  708 */     ResolvableType[] generics = this.generics;
/*  709 */     if (generics == null) {
/*  710 */       if (this.type instanceof Class) {
/*  711 */         TypeVariable[] arrayOfTypeVariable = ((Class)this.type).getTypeParameters();
/*  712 */         generics = new ResolvableType[arrayOfTypeVariable.length];
/*  713 */         for (int i = 0; i < generics.length; i++) {
/*  714 */           generics[i] = forType(arrayOfTypeVariable[i], this);
/*      */         }
/*      */       }
/*  717 */       else if (this.type instanceof ParameterizedType) {
/*  718 */         Type[] actualTypeArguments = ((ParameterizedType)this.type).getActualTypeArguments();
/*  719 */         generics = new ResolvableType[actualTypeArguments.length];
/*  720 */         for (int i = 0; i < actualTypeArguments.length; i++) {
/*  721 */           generics[i] = forType(actualTypeArguments[i], this.variableResolver);
/*      */         }
/*      */       } else {
/*      */         
/*  725 */         generics = resolveType().getGenerics();
/*      */       } 
/*  727 */       this.generics = generics;
/*      */     } 
/*  729 */     return generics;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class<?>[] resolveGenerics() {
/*  741 */     ResolvableType[] generics = getGenerics();
/*  742 */     Class<?>[] resolvedGenerics = new Class[generics.length];
/*  743 */     for (int i = 0; i < generics.length; i++) {
/*  744 */       resolvedGenerics[i] = generics[i].resolve();
/*      */     }
/*  746 */     return resolvedGenerics;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class<?>[] resolveGenerics(Class<?> fallback) {
/*  759 */     ResolvableType[] generics = getGenerics();
/*  760 */     Class<?>[] resolvedGenerics = new Class[generics.length];
/*  761 */     for (int i = 0; i < generics.length; i++) {
/*  762 */       resolvedGenerics[i] = generics[i].resolve(fallback);
/*      */     }
/*  764 */     return resolvedGenerics;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Class<?> resolveGeneric(int... indexes) {
/*  778 */     return getGeneric(indexes).resolve();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Class<?> resolve() {
/*  796 */     return this.resolved;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class<?> resolve(Class<?> fallback) {
/*  811 */     return (this.resolved != null) ? this.resolved : fallback;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private Class<?> resolveClass() {
/*  816 */     if (this.type == EmptyType.INSTANCE) {
/*  817 */       return null;
/*      */     }
/*  819 */     if (this.type instanceof Class) {
/*  820 */       return (Class)this.type;
/*      */     }
/*  822 */     if (this.type instanceof GenericArrayType) {
/*  823 */       Class<?> resolvedComponent = getComponentType().resolve();
/*  824 */       return (resolvedComponent != null) ? Array.newInstance(resolvedComponent, 0).getClass() : null;
/*      */     } 
/*  826 */     return resolveType().resolve();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ResolvableType resolveType() {
/*  835 */     if (this.type instanceof ParameterizedType) {
/*  836 */       return forType(((ParameterizedType)this.type).getRawType(), this.variableResolver);
/*      */     }
/*  838 */     if (this.type instanceof WildcardType) {
/*  839 */       Type resolved = resolveBounds(((WildcardType)this.type).getUpperBounds());
/*  840 */       if (resolved == null) {
/*  841 */         resolved = resolveBounds(((WildcardType)this.type).getLowerBounds());
/*      */       }
/*  843 */       return forType(resolved, this.variableResolver);
/*      */     } 
/*  845 */     if (this.type instanceof TypeVariable) {
/*  846 */       TypeVariable<?> variable = (TypeVariable)this.type;
/*      */       
/*  848 */       if (this.variableResolver != null) {
/*  849 */         ResolvableType resolved = this.variableResolver.resolveVariable(variable);
/*  850 */         if (resolved != null) {
/*  851 */           return resolved;
/*      */         }
/*      */       } 
/*      */       
/*  855 */       return forType(resolveBounds(variable.getBounds()), this.variableResolver);
/*      */     } 
/*  857 */     return NONE;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private Type resolveBounds(Type[] bounds) {
/*  862 */     if (bounds.length == 0 || bounds[0] == Object.class) {
/*  863 */       return null;
/*      */     }
/*  865 */     return bounds[0];
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private ResolvableType resolveVariable(TypeVariable<?> variable) {
/*  870 */     if (this.type instanceof TypeVariable) {
/*  871 */       return resolveType().resolveVariable(variable);
/*      */     }
/*  873 */     if (this.type instanceof ParameterizedType) {
/*  874 */       ParameterizedType parameterizedType = (ParameterizedType)this.type;
/*  875 */       Class<?> resolved = resolve();
/*  876 */       if (resolved == null) {
/*  877 */         return null;
/*      */       }
/*  879 */       TypeVariable[] arrayOfTypeVariable = (TypeVariable[])resolved.getTypeParameters();
/*  880 */       for (int i = 0; i < arrayOfTypeVariable.length; i++) {
/*  881 */         if (ObjectUtils.nullSafeEquals(arrayOfTypeVariable[i].getName(), variable.getName())) {
/*  882 */           Type actualType = parameterizedType.getActualTypeArguments()[i];
/*  883 */           return forType(actualType, this.variableResolver);
/*      */         } 
/*      */       } 
/*  886 */       Type ownerType = parameterizedType.getOwnerType();
/*  887 */       if (ownerType != null) {
/*  888 */         return forType(ownerType, this.variableResolver).resolveVariable(variable);
/*      */       }
/*      */     } 
/*  891 */     if (this.type instanceof WildcardType) {
/*  892 */       ResolvableType resolved = resolveType().resolveVariable(variable);
/*  893 */       if (resolved != null) {
/*  894 */         return resolved;
/*      */       }
/*      */     } 
/*  897 */     if (this.variableResolver != null) {
/*  898 */       return this.variableResolver.resolveVariable(variable);
/*      */     }
/*  900 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(@Nullable Object other) {
/*  906 */     if (this == other) {
/*  907 */       return true;
/*      */     }
/*  909 */     if (!(other instanceof ResolvableType)) {
/*  910 */       return false;
/*      */     }
/*      */     
/*  913 */     ResolvableType otherType = (ResolvableType)other;
/*  914 */     if (!ObjectUtils.nullSafeEquals(this.type, otherType.type)) {
/*  915 */       return false;
/*      */     }
/*  917 */     if (this.typeProvider != otherType.typeProvider && (this.typeProvider == null || otherType.typeProvider == null || 
/*      */       
/*  919 */       !ObjectUtils.nullSafeEquals(this.typeProvider.getType(), otherType.typeProvider.getType()))) {
/*  920 */       return false;
/*      */     }
/*  922 */     if (this.variableResolver != otherType.variableResolver && (this.variableResolver == null || otherType.variableResolver == null || 
/*      */       
/*  924 */       !ObjectUtils.nullSafeEquals(this.variableResolver.getSource(), otherType.variableResolver.getSource()))) {
/*  925 */       return false;
/*      */     }
/*  927 */     if (!ObjectUtils.nullSafeEquals(this.componentType, otherType.componentType)) {
/*  928 */       return false;
/*      */     }
/*  930 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  935 */     return (this.hash != null) ? this.hash.intValue() : calculateHashCode();
/*      */   }
/*      */   
/*      */   private int calculateHashCode() {
/*  939 */     int hashCode = ObjectUtils.nullSafeHashCode(this.type);
/*  940 */     if (this.typeProvider != null) {
/*  941 */       hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode(this.typeProvider.getType());
/*      */     }
/*  943 */     if (this.variableResolver != null) {
/*  944 */       hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode(this.variableResolver.getSource());
/*      */     }
/*  946 */     if (this.componentType != null) {
/*  947 */       hashCode = 31 * hashCode + ObjectUtils.nullSafeHashCode(this.componentType);
/*      */     }
/*  949 */     return hashCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   VariableResolver asVariableResolver() {
/*  957 */     if (this == NONE) {
/*  958 */       return null;
/*      */     }
/*  960 */     return new DefaultVariableResolver(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object readResolve() {
/*  967 */     return (this.type == EmptyType.INSTANCE) ? NONE : this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  976 */     if (isArray()) {
/*  977 */       return getComponentType() + "[]";
/*      */     }
/*  979 */     if (this.resolved == null) {
/*  980 */       return "?";
/*      */     }
/*  982 */     if (this.type instanceof TypeVariable) {
/*  983 */       TypeVariable<?> variable = (TypeVariable)this.type;
/*  984 */       if (this.variableResolver == null || this.variableResolver.resolveVariable(variable) == null)
/*      */       {
/*      */         
/*  987 */         return "?";
/*      */       }
/*      */     } 
/*  990 */     if (hasGenerics()) {
/*  991 */       return this.resolved.getName() + '<' + StringUtils.arrayToDelimitedString((Object[])getGenerics(), ", ") + '>';
/*      */     }
/*  993 */     return this.resolved.getName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forClass(@Nullable Class<?> clazz) {
/* 1010 */     return new ResolvableType(clazz);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forRawClass(@Nullable final Class<?> clazz) {
/* 1026 */     return new ResolvableType(clazz)
/*      */       {
/*      */         public ResolvableType[] getGenerics() {
/* 1029 */           return ResolvableType.EMPTY_TYPES_ARRAY;
/*      */         }
/*      */         
/*      */         public boolean isAssignableFrom(Class<?> other) {
/* 1033 */           return (clazz == null || ClassUtils.isAssignable(clazz, other));
/*      */         }
/*      */         
/*      */         public boolean isAssignableFrom(ResolvableType other) {
/* 1037 */           Class<?> otherClass = other.resolve();
/* 1038 */           return (otherClass != null && (clazz == null || ClassUtils.isAssignable(clazz, otherClass)));
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forClass(Class<?> baseType, Class<?> implementationClass) {
/* 1055 */     Assert.notNull(baseType, "Base type must not be null");
/* 1056 */     ResolvableType asType = forType(implementationClass).as(baseType);
/* 1057 */     return (asType == NONE) ? forType(baseType) : asType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forClassWithGenerics(Class<?> clazz, Class<?>... generics) {
/* 1068 */     Assert.notNull(clazz, "Class must not be null");
/* 1069 */     Assert.notNull(generics, "Generics array must not be null");
/* 1070 */     ResolvableType[] resolvableGenerics = new ResolvableType[generics.length];
/* 1071 */     for (int i = 0; i < generics.length; i++) {
/* 1072 */       resolvableGenerics[i] = forClass(generics[i]);
/*      */     }
/* 1074 */     return forClassWithGenerics(clazz, resolvableGenerics);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forClassWithGenerics(Class<?> clazz, ResolvableType... generics) {
/* 1085 */     Assert.notNull(clazz, "Class must not be null");
/* 1086 */     Assert.notNull(generics, "Generics array must not be null");
/* 1087 */     TypeVariable[] arrayOfTypeVariable = (TypeVariable[])clazz.getTypeParameters();
/* 1088 */     Assert.isTrue((arrayOfTypeVariable.length == generics.length), "Mismatched number of generics specified");
/*      */     
/* 1090 */     Type[] arguments = new Type[generics.length];
/* 1091 */     for (int i = 0; i < generics.length; i++) {
/* 1092 */       ResolvableType generic = generics[i];
/* 1093 */       Type argument = (generic != null) ? generic.getType() : null;
/* 1094 */       arguments[i] = (argument != null && !(argument instanceof TypeVariable)) ? argument : arrayOfTypeVariable[i];
/*      */     } 
/*      */     
/* 1097 */     ParameterizedType syntheticType = new SyntheticParameterizedType(clazz, arguments);
/* 1098 */     return forType(syntheticType, new TypeVariablesVariableResolver((TypeVariable<?>[])arrayOfTypeVariable, generics));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forInstance(Object instance) {
/* 1112 */     Assert.notNull(instance, "Instance must not be null");
/* 1113 */     if (instance instanceof ResolvableTypeProvider) {
/* 1114 */       ResolvableType type = ((ResolvableTypeProvider)instance).getResolvableType();
/* 1115 */       if (type != null) {
/* 1116 */         return type;
/*      */       }
/*      */     } 
/* 1119 */     return forClass(instance.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forField(Field field) {
/* 1129 */     Assert.notNull(field, "Field must not be null");
/* 1130 */     return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forField(Field field, Class<?> implementationClass) {
/* 1144 */     Assert.notNull(field, "Field must not be null");
/* 1145 */     ResolvableType owner = forType(implementationClass).as(field.getDeclaringClass());
/* 1146 */     return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), owner.asVariableResolver());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forField(Field field, @Nullable ResolvableType implementationType) {
/* 1160 */     Assert.notNull(field, "Field must not be null");
/* 1161 */     ResolvableType owner = (implementationType != null) ? implementationType : NONE;
/* 1162 */     owner = owner.as(field.getDeclaringClass());
/* 1163 */     return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), owner.asVariableResolver());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forField(Field field, int nestingLevel) {
/* 1175 */     Assert.notNull(field, "Field must not be null");
/* 1176 */     return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), null).getNested(nestingLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forField(Field field, int nestingLevel, @Nullable Class<?> implementationClass) {
/* 1192 */     Assert.notNull(field, "Field must not be null");
/* 1193 */     ResolvableType owner = forType(implementationClass).as(field.getDeclaringClass());
/* 1194 */     return forType(null, new SerializableTypeWrapper.FieldTypeProvider(field), owner.asVariableResolver()).getNested(nestingLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forConstructorParameter(Constructor<?> constructor, int parameterIndex) {
/* 1205 */     Assert.notNull(constructor, "Constructor must not be null");
/* 1206 */     return forMethodParameter(new MethodParameter(constructor, parameterIndex));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forConstructorParameter(Constructor<?> constructor, int parameterIndex, Class<?> implementationClass) {
/* 1223 */     Assert.notNull(constructor, "Constructor must not be null");
/* 1224 */     MethodParameter methodParameter = new MethodParameter(constructor, parameterIndex, implementationClass);
/* 1225 */     return forMethodParameter(methodParameter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forMethodReturnType(Method method) {
/* 1235 */     Assert.notNull(method, "Method must not be null");
/* 1236 */     return forMethodParameter(new MethodParameter(method, -1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forMethodReturnType(Method method, Class<?> implementationClass) {
/* 1249 */     Assert.notNull(method, "Method must not be null");
/* 1250 */     MethodParameter methodParameter = new MethodParameter(method, -1, implementationClass);
/* 1251 */     return forMethodParameter(methodParameter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forMethodParameter(Method method, int parameterIndex) {
/* 1263 */     Assert.notNull(method, "Method must not be null");
/* 1264 */     return forMethodParameter(new MethodParameter(method, parameterIndex));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forMethodParameter(Method method, int parameterIndex, Class<?> implementationClass) {
/* 1279 */     Assert.notNull(method, "Method must not be null");
/* 1280 */     MethodParameter methodParameter = new MethodParameter(method, parameterIndex, implementationClass);
/* 1281 */     return forMethodParameter(methodParameter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forMethodParameter(MethodParameter methodParameter) {
/* 1291 */     return forMethodParameter(methodParameter, (Type)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forMethodParameter(MethodParameter methodParameter, @Nullable ResolvableType implementationType) {
/* 1306 */     Assert.notNull(methodParameter, "MethodParameter must not be null");
/*      */     
/* 1308 */     implementationType = (implementationType != null) ? implementationType : forType(methodParameter.getContainingClass());
/* 1309 */     ResolvableType owner = implementationType.as(methodParameter.getDeclaringClass());
/* 1310 */     return forType(null, new SerializableTypeWrapper.MethodParameterTypeProvider(methodParameter), owner.asVariableResolver())
/* 1311 */       .getNested(methodParameter.getNestingLevel(), methodParameter.typeIndexesPerLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forMethodParameter(MethodParameter methodParameter, @Nullable Type targetType) {
/* 1323 */     Assert.notNull(methodParameter, "MethodParameter must not be null");
/* 1324 */     return forMethodParameter(methodParameter, targetType, methodParameter.getNestingLevel());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static ResolvableType forMethodParameter(MethodParameter methodParameter, @Nullable Type targetType, int nestingLevel) {
/* 1341 */     ResolvableType owner = forType(methodParameter.getContainingClass()).as(methodParameter.getDeclaringClass());
/* 1342 */     return forType(targetType, new SerializableTypeWrapper.MethodParameterTypeProvider(methodParameter), owner.asVariableResolver())
/* 1343 */       .getNested(nestingLevel, methodParameter.typeIndexesPerLevel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forArrayComponent(ResolvableType componentType) {
/* 1352 */     Assert.notNull(componentType, "Component type must not be null");
/* 1353 */     Class<?> arrayClass = Array.newInstance(componentType.resolve(), 0).getClass();
/* 1354 */     return new ResolvableType(arrayClass, null, null, componentType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forType(@Nullable Type type) {
/* 1365 */     return forType(type, null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forType(@Nullable Type type, @Nullable ResolvableType owner) {
/* 1378 */     VariableResolver variableResolver = null;
/* 1379 */     if (owner != null) {
/* 1380 */       variableResolver = owner.asVariableResolver();
/*      */     }
/* 1382 */     return forType(type, variableResolver);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ResolvableType forType(ParameterizedTypeReference<?> typeReference) {
/* 1395 */     return forType(typeReference.getType(), null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static ResolvableType forType(@Nullable Type type, @Nullable VariableResolver variableResolver) {
/* 1406 */     return forType(type, null, variableResolver);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static ResolvableType forType(@Nullable Type type, @Nullable SerializableTypeWrapper.TypeProvider typeProvider, @Nullable VariableResolver variableResolver) {
/* 1420 */     if (type == null && typeProvider != null) {
/* 1421 */       type = SerializableTypeWrapper.forTypeProvider(typeProvider);
/*      */     }
/* 1423 */     if (type == null) {
/* 1424 */       return NONE;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1429 */     if (type instanceof Class) {
/* 1430 */       return new ResolvableType(type, typeProvider, variableResolver, (ResolvableType)null);
/*      */     }
/*      */ 
/*      */     
/* 1434 */     cache.purgeUnreferencedEntries();
/*      */ 
/*      */     
/* 1437 */     ResolvableType resultType = new ResolvableType(type, typeProvider, variableResolver);
/* 1438 */     ResolvableType cachedType = (ResolvableType)cache.get(resultType);
/* 1439 */     if (cachedType == null) {
/* 1440 */       cachedType = new ResolvableType(type, typeProvider, variableResolver, resultType.hash);
/* 1441 */       cache.put(cachedType, cachedType);
/*      */     } 
/* 1443 */     resultType.resolved = cachedType.resolved;
/* 1444 */     return resultType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void clearCache() {
/* 1452 */     cache.clear();
/* 1453 */     SerializableTypeWrapper.cache.clear();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static interface VariableResolver
/*      */     extends Serializable
/*      */   {
/*      */     Object getSource();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     ResolvableType resolveVariable(TypeVariable<?> param1TypeVariable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class DefaultVariableResolver
/*      */     implements VariableResolver
/*      */   {
/*      */     private final ResolvableType source;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     DefaultVariableResolver(ResolvableType resolvableType) {
/* 1483 */       this.source = resolvableType;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public ResolvableType resolveVariable(TypeVariable<?> variable) {
/* 1489 */       return this.source.resolveVariable(variable);
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getSource() {
/* 1494 */       return this.source;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class TypeVariablesVariableResolver
/*      */     implements VariableResolver
/*      */   {
/*      */     private final TypeVariable<?>[] variables;
/*      */     
/*      */     private final ResolvableType[] generics;
/*      */     
/*      */     public TypeVariablesVariableResolver(TypeVariable<?>[] variables, ResolvableType[] generics) {
/* 1507 */       this.variables = variables;
/* 1508 */       this.generics = generics;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public ResolvableType resolveVariable(TypeVariable<?> variable) {
/* 1514 */       TypeVariable<?> variableToCompare = SerializableTypeWrapper.<TypeVariable>unwrap(variable);
/* 1515 */       for (int i = 0; i < this.variables.length; i++) {
/* 1516 */         TypeVariable<?> resolvedVariable = SerializableTypeWrapper.<TypeVariable>unwrap(this.variables[i]);
/* 1517 */         if (ObjectUtils.nullSafeEquals(resolvedVariable, variableToCompare)) {
/* 1518 */           return this.generics[i];
/*      */         }
/*      */       } 
/* 1521 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getSource() {
/* 1526 */       return this.generics;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class SyntheticParameterizedType
/*      */     implements ParameterizedType, Serializable
/*      */   {
/*      */     private final Type rawType;
/*      */     private final Type[] typeArguments;
/*      */     
/*      */     public SyntheticParameterizedType(Type rawType, Type[] typeArguments) {
/* 1538 */       this.rawType = rawType;
/* 1539 */       this.typeArguments = typeArguments;
/*      */     }
/*      */ 
/*      */     
/*      */     public String getTypeName() {
/* 1544 */       String typeName = this.rawType.getTypeName();
/* 1545 */       if (this.typeArguments.length > 0) {
/* 1546 */         StringJoiner stringJoiner = new StringJoiner(", ", "<", ">");
/* 1547 */         for (Type argument : this.typeArguments) {
/* 1548 */           stringJoiner.add(argument.getTypeName());
/*      */         }
/* 1550 */         return typeName + stringJoiner;
/*      */       } 
/* 1552 */       return typeName;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public Type getOwnerType() {
/* 1558 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public Type getRawType() {
/* 1563 */       return this.rawType;
/*      */     }
/*      */ 
/*      */     
/*      */     public Type[] getActualTypeArguments() {
/* 1568 */       return this.typeArguments;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@Nullable Object other) {
/* 1573 */       if (this == other) {
/* 1574 */         return true;
/*      */       }
/* 1576 */       if (!(other instanceof ParameterizedType)) {
/* 1577 */         return false;
/*      */       }
/* 1579 */       ParameterizedType otherType = (ParameterizedType)other;
/* 1580 */       return (otherType.getOwnerType() == null && this.rawType.equals(otherType.getRawType()) && 
/* 1581 */         Arrays.equals((Object[])this.typeArguments, (Object[])otherType.getActualTypeArguments()));
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1586 */       return this.rawType.hashCode() * 31 + Arrays.hashCode((Object[])this.typeArguments);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1591 */       return getTypeName();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class WildcardBounds
/*      */   {
/*      */     private final Kind kind;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ResolvableType[] bounds;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public WildcardBounds(Kind kind, ResolvableType[] bounds) {
/* 1612 */       this.kind = kind;
/* 1613 */       this.bounds = bounds;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isSameKind(WildcardBounds bounds) {
/* 1620 */       return (this.kind == bounds.kind);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isAssignableFrom(ResolvableType... types) {
/* 1629 */       for (ResolvableType bound : this.bounds) {
/* 1630 */         for (ResolvableType type : types) {
/* 1631 */           if (!isAssignable(bound, type)) {
/* 1632 */             return false;
/*      */           }
/*      */         } 
/*      */       } 
/* 1636 */       return true;
/*      */     }
/*      */     
/*      */     private boolean isAssignable(ResolvableType source, ResolvableType from) {
/* 1640 */       return (this.kind == Kind.UPPER) ? source.isAssignableFrom(from) : from.isAssignableFrom(source);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ResolvableType[] getBounds() {
/* 1647 */       return this.bounds;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public static WildcardBounds get(ResolvableType type) {
/* 1658 */       ResolvableType resolveToWildcard = type;
/* 1659 */       while (!(resolveToWildcard.getType() instanceof WildcardType)) {
/* 1660 */         if (resolveToWildcard == ResolvableType.NONE) {
/* 1661 */           return null;
/*      */         }
/* 1663 */         resolveToWildcard = resolveToWildcard.resolveType();
/*      */       } 
/* 1665 */       WildcardType wildcardType = (WildcardType)resolveToWildcard.type;
/* 1666 */       Kind boundsType = ((wildcardType.getLowerBounds()).length > 0) ? Kind.LOWER : Kind.UPPER;
/* 1667 */       Type[] bounds = (boundsType == Kind.UPPER) ? wildcardType.getUpperBounds() : wildcardType.getLowerBounds();
/* 1668 */       ResolvableType[] resolvableBounds = new ResolvableType[bounds.length];
/* 1669 */       for (int i = 0; i < bounds.length; i++) {
/* 1670 */         resolvableBounds[i] = ResolvableType.forType(bounds[i], type.variableResolver);
/*      */       }
/* 1672 */       return new WildcardBounds(boundsType, resolvableBounds);
/*      */     }
/*      */ 
/*      */     
/*      */     enum Kind
/*      */     {
/* 1678 */       UPPER, LOWER;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class EmptyType
/*      */     implements Type, Serializable
/*      */   {
/* 1688 */     static final Type INSTANCE = new EmptyType();
/*      */     
/*      */     Object readResolve() {
/* 1691 */       return INSTANCE;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\ResolvableType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */