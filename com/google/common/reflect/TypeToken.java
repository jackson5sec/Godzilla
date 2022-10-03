/*      */ package com.google.common.reflect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Joiner;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.collect.FluentIterable;
/*      */ import com.google.common.collect.ForwardingSet;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Ordering;
/*      */ import com.google.common.collect.UnmodifiableIterator;
/*      */ import com.google.common.primitives.Primitives;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.GenericArrayType;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.lang.reflect.ParameterizedType;
/*      */ import java.lang.reflect.Type;
/*      */ import java.lang.reflect.TypeVariable;
/*      */ import java.lang.reflect.WildcardType;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
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
/*      */ @Beta
/*      */ public abstract class TypeToken<T>
/*      */   extends TypeCapture<T>
/*      */   implements Serializable
/*      */ {
/*      */   private final Type runtimeType;
/*      */   private transient TypeResolver invariantTypeResolver;
/*      */   private transient TypeResolver covariantTypeResolver;
/*      */   private static final long serialVersionUID = 3637540370352322684L;
/*      */   
/*      */   protected TypeToken() {
/*  125 */     this.runtimeType = capture();
/*  126 */     Preconditions.checkState(!(this.runtimeType instanceof TypeVariable), "Cannot construct a TypeToken for a type variable.\nYou probably meant to call new TypeToken<%s>(getClass()) that can resolve the type variable for you.\nIf you do need to create a TypeToken of a type variable, please use TypeToken.of() instead.", this.runtimeType);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected TypeToken(Class<?> declaringClass) {
/*  156 */     Type captured = capture();
/*  157 */     if (captured instanceof Class) {
/*  158 */       this.runtimeType = captured;
/*      */     } else {
/*  160 */       this.runtimeType = TypeResolver.covariantly(declaringClass).resolveType(captured);
/*      */     } 
/*      */   }
/*      */   
/*      */   private TypeToken(Type type) {
/*  165 */     this.runtimeType = (Type)Preconditions.checkNotNull(type);
/*      */   }
/*      */ 
/*      */   
/*      */   public static <T> TypeToken<T> of(Class<T> type) {
/*  170 */     return new SimpleTypeToken<>(type);
/*      */   }
/*      */ 
/*      */   
/*      */   public static TypeToken<?> of(Type type) {
/*  175 */     return new SimpleTypeToken(type);
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
/*      */   public final Class<? super T> getRawType() {
/*  195 */     Class<?> rawType = (Class)getRawTypes().iterator().next();
/*      */     
/*  197 */     Class<? super T> result = (Class)rawType;
/*  198 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Type getType() {
/*  203 */     return this.runtimeType;
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
/*      */   public final <X> TypeToken<T> where(TypeParameter<X> typeParam, TypeToken<X> typeArg) {
/*  227 */     TypeResolver resolver = (new TypeResolver()).where(
/*  228 */         (Map<TypeResolver.TypeVariableKey, ? extends Type>)ImmutableMap.of(new TypeResolver.TypeVariableKey(typeParam.typeVariable), typeArg.runtimeType));
/*      */ 
/*      */     
/*  231 */     return new SimpleTypeToken<>(resolver.resolveType(this.runtimeType));
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
/*      */   public final <X> TypeToken<T> where(TypeParameter<X> typeParam, Class<X> typeArg) {
/*  253 */     return where(typeParam, of(typeArg));
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
/*      */   public final TypeToken<?> resolveType(Type type) {
/*  266 */     Preconditions.checkNotNull(type);
/*      */ 
/*      */     
/*  269 */     return of(getInvariantTypeResolver().resolveType(type));
/*      */   }
/*      */   
/*      */   private TypeToken<?> resolveSupertype(Type type) {
/*  273 */     TypeToken<?> supertype = of(getCovariantTypeResolver().resolveType(type));
/*      */     
/*  275 */     supertype.covariantTypeResolver = this.covariantTypeResolver;
/*  276 */     supertype.invariantTypeResolver = this.invariantTypeResolver;
/*  277 */     return supertype;
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
/*      */   final TypeToken<? super T> getGenericSuperclass() {
/*  293 */     if (this.runtimeType instanceof TypeVariable)
/*      */     {
/*  295 */       return boundAsSuperclass(((TypeVariable)this.runtimeType).getBounds()[0]);
/*      */     }
/*  297 */     if (this.runtimeType instanceof WildcardType)
/*      */     {
/*  299 */       return boundAsSuperclass(((WildcardType)this.runtimeType).getUpperBounds()[0]);
/*      */     }
/*  301 */     Type superclass = getRawType().getGenericSuperclass();
/*  302 */     if (superclass == null) {
/*  303 */       return null;
/*      */     }
/*      */     
/*  306 */     TypeToken<? super T> superToken = (TypeToken)resolveSupertype(superclass);
/*  307 */     return superToken;
/*      */   }
/*      */   
/*      */   private TypeToken<? super T> boundAsSuperclass(Type bound) {
/*  311 */     TypeToken<?> token = of(bound);
/*  312 */     if (token.getRawType().isInterface()) {
/*  313 */       return null;
/*      */     }
/*      */     
/*  316 */     TypeToken<? super T> superclass = (TypeToken)token;
/*  317 */     return superclass;
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
/*      */   final ImmutableList<TypeToken<? super T>> getGenericInterfaces() {
/*  333 */     if (this.runtimeType instanceof TypeVariable) {
/*  334 */       return boundsAsInterfaces(((TypeVariable)this.runtimeType).getBounds());
/*      */     }
/*  336 */     if (this.runtimeType instanceof WildcardType) {
/*  337 */       return boundsAsInterfaces(((WildcardType)this.runtimeType).getUpperBounds());
/*      */     }
/*  339 */     ImmutableList.Builder<TypeToken<? super T>> builder = ImmutableList.builder();
/*  340 */     for (Type interfaceType : getRawType().getGenericInterfaces()) {
/*      */ 
/*      */       
/*  343 */       TypeToken<? super T> resolvedInterface = (TypeToken)resolveSupertype(interfaceType);
/*  344 */       builder.add(resolvedInterface);
/*      */     } 
/*  346 */     return builder.build();
/*      */   }
/*      */   
/*      */   private ImmutableList<TypeToken<? super T>> boundsAsInterfaces(Type[] bounds) {
/*  350 */     ImmutableList.Builder<TypeToken<? super T>> builder = ImmutableList.builder();
/*  351 */     for (Type bound : bounds) {
/*      */       
/*  353 */       TypeToken<? super T> boundType = (TypeToken)of(bound);
/*  354 */       if (boundType.getRawType().isInterface()) {
/*  355 */         builder.add(boundType);
/*      */       }
/*      */     } 
/*  358 */     return builder.build();
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
/*      */   public final TypeSet getTypes() {
/*  373 */     return new TypeSet();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<? super T> getSupertype(Class<? super T> superclass) {
/*  382 */     Preconditions.checkArgument(
/*  383 */         someRawTypeIsSubclassOf(superclass), "%s is not a super class of %s", superclass, this);
/*      */ 
/*      */ 
/*      */     
/*  387 */     if (this.runtimeType instanceof TypeVariable) {
/*  388 */       return getSupertypeFromUpperBounds(superclass, ((TypeVariable)this.runtimeType).getBounds());
/*      */     }
/*  390 */     if (this.runtimeType instanceof WildcardType) {
/*  391 */       return getSupertypeFromUpperBounds(superclass, ((WildcardType)this.runtimeType).getUpperBounds());
/*      */     }
/*  393 */     if (superclass.isArray()) {
/*  394 */       return getArraySupertype(superclass);
/*      */     }
/*      */ 
/*      */     
/*  398 */     TypeToken<? super T> supertype = (TypeToken)resolveSupertype((toGenericType((Class)superclass)).runtimeType);
/*  399 */     return supertype;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<? extends T> getSubtype(Class<?> subclass) {
/*  408 */     Preconditions.checkArgument(!(this.runtimeType instanceof TypeVariable), "Cannot get subtype of type variable <%s>", this);
/*      */     
/*  410 */     if (this.runtimeType instanceof WildcardType) {
/*  411 */       return getSubtypeFromLowerBounds(subclass, ((WildcardType)this.runtimeType).getLowerBounds());
/*      */     }
/*      */     
/*  414 */     if (isArray()) {
/*  415 */       return getArraySubtype(subclass);
/*      */     }
/*      */     
/*  418 */     Preconditions.checkArgument(
/*  419 */         getRawType().isAssignableFrom(subclass), "%s isn't a subclass of %s", subclass, this);
/*  420 */     Type resolvedTypeArgs = resolveTypeArgsForSubclass(subclass);
/*      */     
/*  422 */     TypeToken<? extends T> subtype = (TypeToken)of(resolvedTypeArgs);
/*  423 */     Preconditions.checkArgument(subtype
/*  424 */         .isSubtypeOf(this), "%s does not appear to be a subtype of %s", subtype, this);
/*  425 */     return subtype;
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
/*      */   public final boolean isSupertypeOf(TypeToken<?> type) {
/*  437 */     return type.isSubtypeOf(getType());
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
/*      */   public final boolean isSupertypeOf(Type type) {
/*  449 */     return of(type).isSubtypeOf(getType());
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
/*      */   public final boolean isSubtypeOf(TypeToken<?> type) {
/*  461 */     return isSubtypeOf(type.getType());
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
/*      */   public final boolean isSubtypeOf(Type supertype) {
/*  473 */     Preconditions.checkNotNull(supertype);
/*  474 */     if (supertype instanceof WildcardType)
/*      */     {
/*      */ 
/*      */       
/*  478 */       return any(((WildcardType)supertype).getLowerBounds()).isSupertypeOf(this.runtimeType);
/*      */     }
/*      */ 
/*      */     
/*  482 */     if (this.runtimeType instanceof WildcardType)
/*      */     {
/*  484 */       return any(((WildcardType)this.runtimeType).getUpperBounds()).isSubtypeOf(supertype);
/*      */     }
/*      */ 
/*      */     
/*  488 */     if (this.runtimeType instanceof TypeVariable) {
/*  489 */       return (this.runtimeType.equals(supertype) || 
/*  490 */         any(((TypeVariable)this.runtimeType).getBounds()).isSubtypeOf(supertype));
/*      */     }
/*  492 */     if (this.runtimeType instanceof GenericArrayType) {
/*  493 */       return of(supertype).isSupertypeOfArray((GenericArrayType)this.runtimeType);
/*      */     }
/*      */     
/*  496 */     if (supertype instanceof Class)
/*  497 */       return someRawTypeIsSubclassOf((Class)supertype); 
/*  498 */     if (supertype instanceof ParameterizedType)
/*  499 */       return isSubtypeOfParameterizedType((ParameterizedType)supertype); 
/*  500 */     if (supertype instanceof GenericArrayType) {
/*  501 */       return isSubtypeOfArrayType((GenericArrayType)supertype);
/*      */     }
/*  503 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isArray() {
/*  512 */     return (getComponentType() != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isPrimitive() {
/*  521 */     return (this.runtimeType instanceof Class && ((Class)this.runtimeType).isPrimitive());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<T> wrap() {
/*  531 */     if (isPrimitive()) {
/*      */       
/*  533 */       Class<T> type = (Class<T>)this.runtimeType;
/*  534 */       return of(Primitives.wrap(type));
/*      */     } 
/*  536 */     return this;
/*      */   }
/*      */   
/*      */   private boolean isWrapper() {
/*  540 */     return Primitives.allWrapperTypes().contains(this.runtimeType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<T> unwrap() {
/*  550 */     if (isWrapper()) {
/*      */       
/*  552 */       Class<T> type = (Class<T>)this.runtimeType;
/*  553 */       return of(Primitives.unwrap(type));
/*      */     } 
/*  555 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final TypeToken<?> getComponentType() {
/*  563 */     Type componentType = Types.getComponentType(this.runtimeType);
/*  564 */     if (componentType == null) {
/*  565 */       return null;
/*      */     }
/*  567 */     return of(componentType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Invokable<T, Object> method(Method method) {
/*  576 */     Preconditions.checkArgument(
/*  577 */         someRawTypeIsSubclassOf(method.getDeclaringClass()), "%s not declared by %s", method, this);
/*      */ 
/*      */ 
/*      */     
/*  581 */     return new Invokable.MethodInvokable<T>(method)
/*      */       {
/*      */         Type getGenericReturnType() {
/*  584 */           return TypeToken.this.getCovariantTypeResolver().resolveType(super.getGenericReturnType());
/*      */         }
/*      */ 
/*      */         
/*      */         Type[] getGenericParameterTypes() {
/*  589 */           return TypeToken.this.getInvariantTypeResolver().resolveTypesInPlace(super.getGenericParameterTypes());
/*      */         }
/*      */ 
/*      */         
/*      */         Type[] getGenericExceptionTypes() {
/*  594 */           return TypeToken.this.getCovariantTypeResolver().resolveTypesInPlace(super.getGenericExceptionTypes());
/*      */         }
/*      */ 
/*      */         
/*      */         public TypeToken<T> getOwnerType() {
/*  599 */           return TypeToken.this;
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/*  604 */           return getOwnerType() + "." + super.toString();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Invokable<T, T> constructor(Constructor<?> constructor) {
/*  615 */     Preconditions.checkArgument(
/*  616 */         (constructor.getDeclaringClass() == getRawType()), "%s not declared by %s", constructor, 
/*      */ 
/*      */         
/*  619 */         getRawType());
/*  620 */     return new Invokable.ConstructorInvokable<T>(constructor)
/*      */       {
/*      */         Type getGenericReturnType() {
/*  623 */           return TypeToken.this.getCovariantTypeResolver().resolveType(super.getGenericReturnType());
/*      */         }
/*      */ 
/*      */         
/*      */         Type[] getGenericParameterTypes() {
/*  628 */           return TypeToken.this.getInvariantTypeResolver().resolveTypesInPlace(super.getGenericParameterTypes());
/*      */         }
/*      */ 
/*      */         
/*      */         Type[] getGenericExceptionTypes() {
/*  633 */           return TypeToken.this.getCovariantTypeResolver().resolveTypesInPlace(super.getGenericExceptionTypes());
/*      */         }
/*      */ 
/*      */         
/*      */         public TypeToken<T> getOwnerType() {
/*  638 */           return TypeToken.this;
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/*  643 */           return getOwnerType() + "(" + Joiner.on(", ").join((Object[])getGenericParameterTypes()) + ")";
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public class TypeSet
/*      */     extends ForwardingSet<TypeToken<? super T>>
/*      */     implements Serializable
/*      */   {
/*      */     private transient ImmutableSet<TypeToken<? super T>> types;
/*      */ 
/*      */     
/*      */     private static final long serialVersionUID = 0L;
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeSet interfaces() {
/*  662 */       return new TypeToken.InterfaceSet(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public TypeSet classes() {
/*  667 */       return new TypeToken.ClassSet();
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<TypeToken<? super T>> delegate() {
/*  672 */       ImmutableSet<TypeToken<? super T>> filteredTypes = this.types;
/*  673 */       if (filteredTypes == null) {
/*      */ 
/*      */ 
/*      */         
/*  677 */         ImmutableList<TypeToken<? super T>> collectedTypes = (ImmutableList)TypeToken.TypeCollector.FOR_GENERIC_TYPE.collectTypes(TypeToken.this);
/*  678 */         return 
/*      */ 
/*      */           
/*  681 */           (Set<TypeToken<? super T>>)(this.types = FluentIterable.from((Iterable)collectedTypes).filter(TypeToken.TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet());
/*      */       } 
/*  683 */       return (Set<TypeToken<? super T>>)filteredTypes;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Class<? super T>> rawTypes() {
/*  692 */       ImmutableList<Class<? super T>> collectedTypes = (ImmutableList)TypeToken.TypeCollector.FOR_RAW_TYPE.collectTypes((Iterable<? extends Class<?>>)TypeToken.this.getRawTypes());
/*  693 */       return (Set<Class<? super T>>)ImmutableSet.copyOf((Collection)collectedTypes);
/*      */     }
/*      */   }
/*      */   
/*      */   private final class InterfaceSet
/*      */     extends TypeSet
/*      */   {
/*      */     private final transient TypeToken<T>.TypeSet allTypes;
/*      */     private transient ImmutableSet<TypeToken<? super T>> interfaces;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     InterfaceSet(TypeToken<T>.TypeSet allTypes) {
/*  705 */       this.allTypes = allTypes;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<TypeToken<? super T>> delegate() {
/*  710 */       ImmutableSet<TypeToken<? super T>> result = this.interfaces;
/*  711 */       if (result == null) {
/*  712 */         return 
/*  713 */           (Set<TypeToken<? super T>>)(this.interfaces = FluentIterable.from((Iterable)this.allTypes).filter(TypeToken.TypeFilter.INTERFACE_ONLY).toSet());
/*      */       }
/*  715 */       return (Set<TypeToken<? super T>>)result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeToken<T>.TypeSet interfaces() {
/*  721 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Class<? super T>> rawTypes() {
/*  729 */       ImmutableList<Class<? super T>> collectedTypes = (ImmutableList)TypeToken.TypeCollector.FOR_RAW_TYPE.collectTypes((Iterable<? extends Class<?>>)TypeToken.this.getRawTypes());
/*  730 */       return (Set<Class<? super T>>)FluentIterable.from((Iterable)collectedTypes)
/*  731 */         .filter(new Predicate<Class<?>>()
/*      */           {
/*      */             public boolean apply(Class<?> type)
/*      */             {
/*  735 */               return type.isInterface();
/*      */             }
/*  738 */           }).toSet();
/*      */     }
/*      */ 
/*      */     
/*      */     public TypeToken<T>.TypeSet classes() {
/*  743 */       throw new UnsupportedOperationException("interfaces().classes() not supported.");
/*      */     }
/*      */     
/*      */     private Object readResolve() {
/*  747 */       return TypeToken.this.getTypes().interfaces();
/*      */     }
/*      */   }
/*      */   
/*      */   private final class ClassSet
/*      */     extends TypeSet {
/*      */     private transient ImmutableSet<TypeToken<? super T>> classes;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     private ClassSet() {}
/*      */     
/*      */     protected Set<TypeToken<? super T>> delegate() {
/*  759 */       ImmutableSet<TypeToken<? super T>> result = this.classes;
/*  760 */       if (result == null) {
/*      */ 
/*      */ 
/*      */         
/*  764 */         ImmutableList<TypeToken<? super T>> collectedTypes = TypeToken.TypeCollector.FOR_GENERIC_TYPE.classesOnly().collectTypes(TypeToken.this);
/*  765 */         return 
/*      */ 
/*      */           
/*  768 */           (Set<TypeToken<? super T>>)(this.classes = FluentIterable.from((Iterable)collectedTypes).filter(TypeToken.TypeFilter.IGNORE_TYPE_VARIABLE_OR_WILDCARD).toSet());
/*      */       } 
/*  770 */       return (Set<TypeToken<? super T>>)result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public TypeToken<T>.TypeSet classes() {
/*  776 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Class<? super T>> rawTypes() {
/*  784 */       ImmutableList<Class<? super T>> collectedTypes = TypeToken.TypeCollector.FOR_RAW_TYPE.classesOnly().collectTypes((Iterable<? extends Class<? super T>>)TypeToken.this.getRawTypes());
/*  785 */       return (Set<Class<? super T>>)ImmutableSet.copyOf((Collection)collectedTypes);
/*      */     }
/*      */ 
/*      */     
/*      */     public TypeToken<T>.TypeSet interfaces() {
/*  790 */       throw new UnsupportedOperationException("classes().interfaces() not supported.");
/*      */     }
/*      */     
/*      */     private Object readResolve() {
/*  794 */       return TypeToken.this.getTypes().classes();
/*      */     }
/*      */   }
/*      */   
/*      */   private enum TypeFilter
/*      */     implements Predicate<TypeToken<?>>
/*      */   {
/*  801 */     IGNORE_TYPE_VARIABLE_OR_WILDCARD
/*      */     {
/*      */       public boolean apply(TypeToken<?> type) {
/*  804 */         return (!(type.runtimeType instanceof TypeVariable) && 
/*  805 */           !(type.runtimeType instanceof WildcardType));
/*      */       }
/*      */     },
/*  808 */     INTERFACE_ONLY
/*      */     {
/*      */       public boolean apply(TypeToken<?> type) {
/*  811 */         return type.getRawType().isInterface();
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object o) {
/*  821 */     if (o instanceof TypeToken) {
/*  822 */       TypeToken<?> that = (TypeToken)o;
/*  823 */       return this.runtimeType.equals(that.runtimeType);
/*      */     } 
/*  825 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  830 */     return this.runtimeType.hashCode();
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/*  835 */     return Types.toString(this.runtimeType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object writeReplace() {
/*  842 */     return of((new TypeResolver()).resolveType(this.runtimeType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   final TypeToken<T> rejectTypeVariables() {
/*  851 */     (new TypeVisitor()
/*      */       {
/*      */         void visitTypeVariable(TypeVariable<?> type) {
/*  854 */           throw new IllegalArgumentException(TypeToken.this
/*  855 */               .runtimeType + "contains a type variable and is not safe for the operation");
/*      */         }
/*      */ 
/*      */         
/*      */         void visitWildcardType(WildcardType type) {
/*  860 */           visit(type.getLowerBounds());
/*  861 */           visit(type.getUpperBounds());
/*      */         }
/*      */ 
/*      */         
/*      */         void visitParameterizedType(ParameterizedType type) {
/*  866 */           visit(type.getActualTypeArguments());
/*  867 */           visit(new Type[] { type.getOwnerType() });
/*      */         }
/*      */ 
/*      */         
/*      */         void visitGenericArrayType(GenericArrayType type) {
/*  872 */           visit(new Type[] { type.getGenericComponentType() });
/*      */         }
/*  874 */       }).visit(new Type[] { this.runtimeType });
/*  875 */     return this;
/*      */   }
/*      */   
/*      */   private boolean someRawTypeIsSubclassOf(Class<?> superclass) {
/*  879 */     for (UnmodifiableIterator<Class<?>> unmodifiableIterator = getRawTypes().iterator(); unmodifiableIterator.hasNext(); ) { Class<?> rawType = unmodifiableIterator.next();
/*  880 */       if (superclass.isAssignableFrom(rawType)) {
/*  881 */         return true;
/*      */       } }
/*      */     
/*  884 */     return false;
/*      */   }
/*      */   
/*      */   private boolean isSubtypeOfParameterizedType(ParameterizedType supertype) {
/*  888 */     Class<?> matchedClass = of(supertype).getRawType();
/*  889 */     if (!someRawTypeIsSubclassOf(matchedClass)) {
/*  890 */       return false;
/*      */     }
/*  892 */     TypeVariable[] arrayOfTypeVariable = (TypeVariable[])matchedClass.getTypeParameters();
/*  893 */     Type[] supertypeArgs = supertype.getActualTypeArguments();
/*  894 */     for (int i = 0; i < arrayOfTypeVariable.length; i++) {
/*  895 */       Type subtypeParam = getCovariantTypeResolver().resolveType(arrayOfTypeVariable[i]);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  900 */       if (!of(subtypeParam).is(supertypeArgs[i], arrayOfTypeVariable[i])) {
/*  901 */         return false;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  907 */     return (Modifier.isStatic(((Class)supertype.getRawType()).getModifiers()) || supertype
/*  908 */       .getOwnerType() == null || 
/*  909 */       isOwnedBySubtypeOf(supertype.getOwnerType()));
/*      */   }
/*      */   
/*      */   private boolean isSubtypeOfArrayType(GenericArrayType supertype) {
/*  913 */     if (this.runtimeType instanceof Class) {
/*  914 */       Class<?> fromClass = (Class)this.runtimeType;
/*  915 */       if (!fromClass.isArray()) {
/*  916 */         return false;
/*      */       }
/*  918 */       return of(fromClass.getComponentType()).isSubtypeOf(supertype.getGenericComponentType());
/*  919 */     }  if (this.runtimeType instanceof GenericArrayType) {
/*  920 */       GenericArrayType fromArrayType = (GenericArrayType)this.runtimeType;
/*  921 */       return of(fromArrayType.getGenericComponentType())
/*  922 */         .isSubtypeOf(supertype.getGenericComponentType());
/*      */     } 
/*  924 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isSupertypeOfArray(GenericArrayType subtype) {
/*  929 */     if (this.runtimeType instanceof Class) {
/*  930 */       Class<?> thisClass = (Class)this.runtimeType;
/*  931 */       if (!thisClass.isArray()) {
/*  932 */         return thisClass.isAssignableFrom(Object[].class);
/*      */       }
/*  934 */       return of(subtype.getGenericComponentType()).isSubtypeOf(thisClass.getComponentType());
/*  935 */     }  if (this.runtimeType instanceof GenericArrayType) {
/*  936 */       return of(subtype.getGenericComponentType())
/*  937 */         .isSubtypeOf(((GenericArrayType)this.runtimeType).getGenericComponentType());
/*      */     }
/*  939 */     return false;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean is(Type formalType, TypeVariable<?> declaration) {
/*  970 */     if (this.runtimeType.equals(formalType)) {
/*  971 */       return true;
/*      */     }
/*  973 */     if (formalType instanceof WildcardType) {
/*  974 */       WildcardType your = canonicalizeWildcardType(declaration, (WildcardType)formalType);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  980 */       return (every(your.getUpperBounds()).isSupertypeOf(this.runtimeType) && 
/*  981 */         every(your.getLowerBounds()).isSubtypeOf(this.runtimeType));
/*      */     } 
/*  983 */     return canonicalizeWildcardsInType(this.runtimeType).equals(canonicalizeWildcardsInType(formalType));
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
/*      */   private static Type canonicalizeTypeArg(TypeVariable<?> declaration, Type typeArg) {
/* 1005 */     return (typeArg instanceof WildcardType) ? 
/* 1006 */       canonicalizeWildcardType(declaration, (WildcardType)typeArg) : 
/* 1007 */       canonicalizeWildcardsInType(typeArg);
/*      */   }
/*      */   
/*      */   private static Type canonicalizeWildcardsInType(Type type) {
/* 1011 */     if (type instanceof ParameterizedType) {
/* 1012 */       return canonicalizeWildcardsInParameterizedType((ParameterizedType)type);
/*      */     }
/* 1014 */     if (type instanceof GenericArrayType) {
/* 1015 */       return Types.newArrayType(
/* 1016 */           canonicalizeWildcardsInType(((GenericArrayType)type).getGenericComponentType()));
/*      */     }
/* 1018 */     return type;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static WildcardType canonicalizeWildcardType(TypeVariable<?> declaration, WildcardType type) {
/* 1026 */     Type[] declared = declaration.getBounds();
/* 1027 */     List<Type> upperBounds = new ArrayList<>();
/* 1028 */     for (Type bound : type.getUpperBounds()) {
/* 1029 */       if (!any(declared).isSubtypeOf(bound)) {
/* 1030 */         upperBounds.add(canonicalizeWildcardsInType(bound));
/*      */       }
/*      */     } 
/* 1033 */     return new Types.WildcardTypeImpl(type.getLowerBounds(), upperBounds.<Type>toArray(new Type[0]));
/*      */   }
/*      */ 
/*      */   
/*      */   private static ParameterizedType canonicalizeWildcardsInParameterizedType(ParameterizedType type) {
/* 1038 */     Class<?> rawType = (Class)type.getRawType();
/* 1039 */     TypeVariable[] arrayOfTypeVariable = (TypeVariable[])rawType.getTypeParameters();
/* 1040 */     Type[] typeArgs = type.getActualTypeArguments();
/* 1041 */     for (int i = 0; i < typeArgs.length; i++) {
/* 1042 */       typeArgs[i] = canonicalizeTypeArg(arrayOfTypeVariable[i], typeArgs[i]);
/*      */     }
/* 1044 */     return Types.newParameterizedTypeWithOwner(type.getOwnerType(), rawType, typeArgs);
/*      */   }
/*      */ 
/*      */   
/*      */   private static Bounds every(Type[] bounds) {
/* 1049 */     return new Bounds(bounds, false);
/*      */   }
/*      */ 
/*      */   
/*      */   private static Bounds any(Type[] bounds) {
/* 1054 */     return new Bounds(bounds, true);
/*      */   }
/*      */   
/*      */   private static class Bounds {
/*      */     private final Type[] bounds;
/*      */     private final boolean target;
/*      */     
/*      */     Bounds(Type[] bounds, boolean target) {
/* 1062 */       this.bounds = bounds;
/* 1063 */       this.target = target;
/*      */     }
/*      */     
/*      */     boolean isSubtypeOf(Type supertype) {
/* 1067 */       for (Type bound : this.bounds) {
/* 1068 */         if (TypeToken.of(bound).isSubtypeOf(supertype) == this.target) {
/* 1069 */           return this.target;
/*      */         }
/*      */       } 
/* 1072 */       return !this.target;
/*      */     }
/*      */     
/*      */     boolean isSupertypeOf(Type subtype) {
/* 1076 */       TypeToken<?> type = TypeToken.of(subtype);
/* 1077 */       for (Type bound : this.bounds) {
/* 1078 */         if (type.isSubtypeOf(bound) == this.target) {
/* 1079 */           return this.target;
/*      */         }
/*      */       } 
/* 1082 */       return !this.target;
/*      */     }
/*      */   }
/*      */   
/*      */   private ImmutableSet<Class<? super T>> getRawTypes() {
/* 1087 */     final ImmutableSet.Builder<Class<?>> builder = ImmutableSet.builder();
/* 1088 */     (new TypeVisitor()
/*      */       {
/*      */         void visitTypeVariable(TypeVariable<?> t) {
/* 1091 */           visit(t.getBounds());
/*      */         }
/*      */ 
/*      */         
/*      */         void visitWildcardType(WildcardType t) {
/* 1096 */           visit(t.getUpperBounds());
/*      */         }
/*      */ 
/*      */         
/*      */         void visitParameterizedType(ParameterizedType t) {
/* 1101 */           builder.add(t.getRawType());
/*      */         }
/*      */ 
/*      */         
/*      */         void visitClass(Class<?> t) {
/* 1106 */           builder.add(t);
/*      */         }
/*      */ 
/*      */         
/*      */         void visitGenericArrayType(GenericArrayType t) {
/* 1111 */           builder.add(Types.getArrayClass(TypeToken.of(t.getGenericComponentType()).getRawType()));
/*      */         }
/* 1113 */       }).visit(new Type[] { this.runtimeType });
/*      */ 
/*      */     
/* 1116 */     ImmutableSet<Class<? super T>> result = builder.build();
/* 1117 */     return result;
/*      */   }
/*      */   
/*      */   private boolean isOwnedBySubtypeOf(Type supertype) {
/* 1121 */     for (TypeToken<?> type : (Iterable<TypeToken<?>>)getTypes()) {
/* 1122 */       Type ownerType = type.getOwnerTypeIfPresent();
/* 1123 */       if (ownerType != null && of(ownerType).isSubtypeOf(supertype)) {
/* 1124 */         return true;
/*      */       }
/*      */     } 
/* 1127 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Type getOwnerTypeIfPresent() {
/* 1135 */     if (this.runtimeType instanceof ParameterizedType)
/* 1136 */       return ((ParameterizedType)this.runtimeType).getOwnerType(); 
/* 1137 */     if (this.runtimeType instanceof Class) {
/* 1138 */       return ((Class)this.runtimeType).getEnclosingClass();
/*      */     }
/* 1140 */     return null;
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
/*      */   @VisibleForTesting
/*      */   static <T> TypeToken<? extends T> toGenericType(Class<T> cls) {
/* 1153 */     if (cls.isArray()) {
/*      */       
/* 1155 */       Type arrayOfGenericType = Types.newArrayType(
/*      */           
/* 1157 */           (toGenericType((Class)cls.getComponentType())).runtimeType);
/*      */       
/* 1159 */       TypeToken<? extends T> result = (TypeToken)of(arrayOfGenericType);
/* 1160 */       return result;
/*      */     } 
/* 1162 */     TypeVariable[] arrayOfTypeVariable = (TypeVariable[])cls.getTypeParameters();
/*      */ 
/*      */     
/* 1165 */     Type ownerType = (cls.isMemberClass() && !Modifier.isStatic(cls.getModifiers())) ? (toGenericType((Class)cls.getEnclosingClass())).runtimeType : null;
/*      */ 
/*      */     
/* 1168 */     if (arrayOfTypeVariable.length > 0 || (ownerType != null && ownerType != cls.getEnclosingClass())) {
/*      */ 
/*      */ 
/*      */       
/* 1172 */       TypeToken<? extends T> type = (TypeToken)of(Types.newParameterizedTypeWithOwner(ownerType, cls, (Type[])arrayOfTypeVariable));
/* 1173 */       return type;
/*      */     } 
/* 1175 */     return of(cls);
/*      */   }
/*      */ 
/*      */   
/*      */   private TypeResolver getCovariantTypeResolver() {
/* 1180 */     TypeResolver resolver = this.covariantTypeResolver;
/* 1181 */     if (resolver == null) {
/* 1182 */       resolver = this.covariantTypeResolver = TypeResolver.covariantly(this.runtimeType);
/*      */     }
/* 1184 */     return resolver;
/*      */   }
/*      */   
/*      */   private TypeResolver getInvariantTypeResolver() {
/* 1188 */     TypeResolver resolver = this.invariantTypeResolver;
/* 1189 */     if (resolver == null) {
/* 1190 */       resolver = this.invariantTypeResolver = TypeResolver.invariantly(this.runtimeType);
/*      */     }
/* 1192 */     return resolver;
/*      */   }
/*      */ 
/*      */   
/*      */   private TypeToken<? super T> getSupertypeFromUpperBounds(Class<? super T> supertype, Type[] upperBounds) {
/* 1197 */     for (Type upperBound : upperBounds) {
/*      */       
/* 1199 */       TypeToken<? super T> bound = (TypeToken)of(upperBound);
/* 1200 */       if (bound.isSubtypeOf(supertype)) {
/*      */         
/* 1202 */         TypeToken<? super T> result = bound.getSupertype(supertype);
/* 1203 */         return result;
/*      */       } 
/*      */     } 
/* 1206 */     throw new IllegalArgumentException(supertype + " isn't a super type of " + this);
/*      */   }
/*      */   
/*      */   private TypeToken<? extends T> getSubtypeFromLowerBounds(Class<?> subclass, Type[] lowerBounds) {
/* 1210 */     Type[] arrayOfType = lowerBounds; int i = arrayOfType.length; byte b = 0; if (b < i) { Type lowerBound = arrayOfType[b];
/*      */       
/* 1212 */       TypeToken<? extends T> bound = (TypeToken)of(lowerBound);
/*      */       
/* 1214 */       return bound.getSubtype(subclass); }
/*      */     
/* 1216 */     throw new IllegalArgumentException(subclass + " isn't a subclass of " + this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private TypeToken<? super T> getArraySupertype(Class<? super T> supertype) {
/* 1224 */     TypeToken<?> componentType = (TypeToken)Preconditions.checkNotNull(getComponentType(), "%s isn't a super type of %s", supertype, this);
/*      */ 
/*      */     
/* 1227 */     TypeToken<?> componentSupertype = componentType.getSupertype(supertype.getComponentType());
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1232 */     TypeToken<? super T> result = (TypeToken)of(newArrayClassOrGenericArrayType(componentSupertype.runtimeType));
/* 1233 */     return result;
/*      */   }
/*      */ 
/*      */   
/*      */   private TypeToken<? extends T> getArraySubtype(Class<?> subclass) {
/* 1238 */     TypeToken<?> componentSubtype = getComponentType().getSubtype(subclass.getComponentType());
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1243 */     TypeToken<? extends T> result = (TypeToken)of(newArrayClassOrGenericArrayType(componentSubtype.runtimeType));
/* 1244 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Type resolveTypeArgsForSubclass(Class<?> subclass) {
/* 1252 */     if (this.runtimeType instanceof Class && ((subclass
/* 1253 */       .getTypeParameters()).length == 0 || (
/* 1254 */       getRawType().getTypeParameters()).length != 0))
/*      */     {
/* 1256 */       return subclass;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1265 */     TypeToken<?> genericSubtype = toGenericType(subclass);
/*      */ 
/*      */     
/* 1268 */     Type supertypeWithArgsFromSubtype = (genericSubtype.getSupertype(getRawType())).runtimeType;
/* 1269 */     return (new TypeResolver())
/* 1270 */       .where(supertypeWithArgsFromSubtype, this.runtimeType)
/* 1271 */       .resolveType(genericSubtype.runtimeType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Type newArrayClassOrGenericArrayType(Type componentType) {
/* 1279 */     return Types.JavaVersion.JAVA7.newArrayType(componentType);
/*      */   }
/*      */   
/*      */   private static final class SimpleTypeToken<T> extends TypeToken<T> { private static final long serialVersionUID = 0L;
/*      */     
/*      */     SimpleTypeToken(Type type) {
/* 1285 */       super(type);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static abstract class TypeCollector<K>
/*      */   {
/*      */     private TypeCollector() {}
/*      */ 
/*      */ 
/*      */     
/* 1298 */     static final TypeCollector<TypeToken<?>> FOR_GENERIC_TYPE = new TypeCollector<TypeToken<?>>()
/*      */       {
/*      */         Class<?> getRawType(TypeToken<?> type)
/*      */         {
/* 1302 */           return type.getRawType();
/*      */         }
/*      */ 
/*      */         
/*      */         Iterable<? extends TypeToken<?>> getInterfaces(TypeToken<?> type) {
/* 1307 */           return (Iterable<? extends TypeToken<?>>)type.getGenericInterfaces();
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         TypeToken<?> getSuperclass(TypeToken<?> type) {
/* 1313 */           return type.getGenericSuperclass();
/*      */         }
/*      */       };
/*      */     
/* 1317 */     static final TypeCollector<Class<?>> FOR_RAW_TYPE = new TypeCollector<Class<?>>()
/*      */       {
/*      */         Class<?> getRawType(Class<?> type)
/*      */         {
/* 1321 */           return type;
/*      */         }
/*      */ 
/*      */         
/*      */         Iterable<? extends Class<?>> getInterfaces(Class<?> type) {
/* 1326 */           return Arrays.asList(type.getInterfaces());
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         Class<?> getSuperclass(Class<?> type) {
/* 1332 */           return type.getSuperclass();
/*      */         }
/*      */       };
/*      */ 
/*      */     
/*      */     final TypeCollector<K> classesOnly() {
/* 1338 */       return new ForwardingTypeCollector<K>(this)
/*      */         {
/*      */           Iterable<? extends K> getInterfaces(K type) {
/* 1341 */             return (Iterable<? extends K>)ImmutableSet.of();
/*      */           }
/*      */ 
/*      */           
/*      */           ImmutableList<K> collectTypes(Iterable<? extends K> types) {
/* 1346 */             ImmutableList.Builder<K> builder = ImmutableList.builder();
/* 1347 */             for (K type : types) {
/* 1348 */               if (!getRawType(type).isInterface()) {
/* 1349 */                 builder.add(type);
/*      */               }
/*      */             } 
/* 1352 */             return super.collectTypes((Iterable<? extends K>)builder.build());
/*      */           }
/*      */         };
/*      */     }
/*      */     
/*      */     final ImmutableList<K> collectTypes(K type) {
/* 1358 */       return collectTypes((Iterable<? extends K>)ImmutableList.of(type));
/*      */     }
/*      */ 
/*      */     
/*      */     ImmutableList<K> collectTypes(Iterable<? extends K> types) {
/* 1363 */       Map<K, Integer> map = Maps.newHashMap();
/* 1364 */       for (K type : types) {
/* 1365 */         collectTypes(type, map);
/*      */       }
/* 1367 */       return sortKeysByValue(map, (Comparator<? super Integer>)Ordering.natural().reverse());
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     private int collectTypes(K type, Map<? super K, Integer> map) {
/* 1373 */       Integer existing = map.get(type);
/* 1374 */       if (existing != null)
/*      */       {
/* 1376 */         return existing.intValue();
/*      */       }
/*      */       
/* 1379 */       int aboveMe = getRawType(type).isInterface() ? 1 : 0;
/* 1380 */       for (K interfaceType : getInterfaces(type)) {
/* 1381 */         aboveMe = Math.max(aboveMe, collectTypes(interfaceType, map));
/*      */       }
/* 1383 */       K superclass = getSuperclass(type);
/* 1384 */       if (superclass != null) {
/* 1385 */         aboveMe = Math.max(aboveMe, collectTypes(superclass, map));
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1392 */       map.put(type, Integer.valueOf(aboveMe + 1));
/* 1393 */       return aboveMe + 1;
/*      */     }
/*      */ 
/*      */     
/*      */     private static <K, V> ImmutableList<K> sortKeysByValue(final Map<K, V> map, final Comparator<? super V> valueComparator) {
/* 1398 */       Ordering<K> keyOrdering = new Ordering<K>()
/*      */         {
/*      */           public int compare(K left, K right)
/*      */           {
/* 1402 */             return valueComparator.compare(map.get(left), map.get(right));
/*      */           }
/*      */         };
/* 1405 */       return keyOrdering.immutableSortedCopy(map.keySet());
/*      */     }
/*      */     
/*      */     abstract Class<?> getRawType(K param1K);
/*      */     
/*      */     abstract Iterable<? extends K> getInterfaces(K param1K);
/*      */     
/*      */     abstract K getSuperclass(K param1K);
/*      */     
/*      */     private static class ForwardingTypeCollector<K>
/*      */       extends TypeCollector<K> {
/*      */       private final TypeToken.TypeCollector<K> delegate;
/*      */       
/*      */       ForwardingTypeCollector(TypeToken.TypeCollector<K> delegate) {
/* 1419 */         this.delegate = delegate;
/*      */       }
/*      */ 
/*      */       
/*      */       Class<?> getRawType(K type) {
/* 1424 */         return this.delegate.getRawType(type);
/*      */       }
/*      */ 
/*      */       
/*      */       Iterable<? extends K> getInterfaces(K type) {
/* 1429 */         return this.delegate.getInterfaces(type);
/*      */       }
/*      */ 
/*      */       
/*      */       K getSuperclass(K type) {
/* 1434 */         return this.delegate.getSuperclass(type);
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\reflect\TypeToken.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */