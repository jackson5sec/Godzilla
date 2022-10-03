/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.GenericDeclaration;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.security.AccessControlException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicReference;
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
/*     */ final class Types
/*     */ {
/*  57 */   private static final Function<Type, String> TYPE_NAME = new Function<Type, String>()
/*     */     {
/*     */       public String apply(Type from)
/*     */       {
/*  61 */         return Types.JavaVersion.CURRENT.typeName(from);
/*     */       }
/*     */     };
/*     */   
/*  65 */   private static final Joiner COMMA_JOINER = Joiner.on(", ").useForNull("null");
/*     */ 
/*     */   
/*     */   static Type newArrayType(Type componentType) {
/*  69 */     if (componentType instanceof WildcardType) {
/*  70 */       WildcardType wildcard = (WildcardType)componentType;
/*  71 */       Type[] lowerBounds = wildcard.getLowerBounds();
/*  72 */       Preconditions.checkArgument((lowerBounds.length <= 1), "Wildcard cannot have more than one lower bounds.");
/*  73 */       if (lowerBounds.length == 1) {
/*  74 */         return supertypeOf(newArrayType(lowerBounds[0]));
/*     */       }
/*  76 */       Type[] upperBounds = wildcard.getUpperBounds();
/*  77 */       Preconditions.checkArgument((upperBounds.length == 1), "Wildcard should have only one upper bound.");
/*  78 */       return subtypeOf(newArrayType(upperBounds[0]));
/*     */     } 
/*     */     
/*  81 */     return JavaVersion.CURRENT.newArrayType(componentType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ParameterizedType newParameterizedTypeWithOwner(Type ownerType, Class<?> rawType, Type... arguments) {
/*  90 */     if (ownerType == null) {
/*  91 */       return newParameterizedType(rawType, arguments);
/*     */     }
/*     */     
/*  94 */     Preconditions.checkNotNull(arguments);
/*  95 */     Preconditions.checkArgument((rawType.getEnclosingClass() != null), "Owner type for unenclosed %s", rawType);
/*  96 */     return new ParameterizedTypeImpl(ownerType, rawType, arguments);
/*     */   }
/*     */ 
/*     */   
/*     */   static ParameterizedType newParameterizedType(Class<?> rawType, Type... arguments) {
/* 101 */     return new ParameterizedTypeImpl(ClassOwnership.JVM_BEHAVIOR
/* 102 */         .getOwnerType(rawType), rawType, arguments);
/*     */   }
/*     */   
/*     */   private enum ClassOwnership
/*     */   {
/* 107 */     OWNED_BY_ENCLOSING_CLASS
/*     */     {
/*     */       Class<?> getOwnerType(Class<?> rawType)
/*     */       {
/* 111 */         return rawType.getEnclosingClass();
/*     */       }
/*     */     },
/* 114 */     LOCAL_CLASS_HAS_NO_OWNER
/*     */     {
/*     */       Class<?> getOwnerType(Class<?> rawType)
/*     */       {
/* 118 */         if (rawType.isLocalClass()) {
/* 119 */           return null;
/*     */         }
/* 121 */         return rawType.getEnclosingClass();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 128 */     static final ClassOwnership JVM_BEHAVIOR = detectJvmBehavior();
/*     */ 
/*     */     
/*     */     private static ClassOwnership detectJvmBehavior() {
/* 132 */       Class<?> subclass = (new LocalClass<String>() {  }).getClass();
/* 133 */       ParameterizedType parameterizedType = (ParameterizedType)subclass.getGenericSuperclass();
/* 134 */       for (ClassOwnership behavior : values()) {
/* 135 */         if (behavior.getOwnerType(LocalClass.class) == parameterizedType.getOwnerType()) {
/* 136 */           return behavior;
/*     */         }
/*     */       } 
/* 139 */       throw new AssertionError();
/*     */     }
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     abstract Class<?> getOwnerType(Class<?> param1Class); }
/*     */   
/*     */   static <D extends GenericDeclaration> TypeVariable<D> newArtificialTypeVariable(D declaration, String name, Type... bounds) {
/* 149 */     (new Type[1])[0] = Object.class; return newTypeVariableImpl(declaration, name, (bounds.length == 0) ? new Type[1] : bounds);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static WildcardType subtypeOf(Type upperBound) {
/* 156 */     return new WildcardTypeImpl(new Type[0], new Type[] { upperBound });
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static WildcardType supertypeOf(Type lowerBound) {
/* 162 */     return new WildcardTypeImpl(new Type[] { lowerBound }, new Type[] { Object.class });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String toString(Type type) {
/* 171 */     return (type instanceof Class) ? ((Class)type).getName() : type.toString();
/*     */   }
/*     */   
/*     */   static Type getComponentType(Type type) {
/* 175 */     Preconditions.checkNotNull(type);
/* 176 */     final AtomicReference<Type> result = new AtomicReference<>();
/* 177 */     (new TypeVisitor()
/*     */       {
/*     */         void visitTypeVariable(TypeVariable<?> t) {
/* 180 */           result.set(Types.subtypeOfComponentType(t.getBounds()));
/*     */         }
/*     */ 
/*     */         
/*     */         void visitWildcardType(WildcardType t) {
/* 185 */           result.set(Types.subtypeOfComponentType(t.getUpperBounds()));
/*     */         }
/*     */ 
/*     */         
/*     */         void visitGenericArrayType(GenericArrayType t) {
/* 190 */           result.set(t.getGenericComponentType());
/*     */         }
/*     */ 
/*     */         
/*     */         void visitClass(Class<?> t) {
/* 195 */           result.set(t.getComponentType());
/*     */         }
/* 197 */       }).visit(new Type[] { type });
/* 198 */     return result.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Type subtypeOfComponentType(Type[] bounds) {
/* 206 */     for (Type bound : bounds) {
/* 207 */       Type componentType = getComponentType(bound);
/* 208 */       if (componentType != null) {
/*     */ 
/*     */         
/* 211 */         if (componentType instanceof Class) {
/* 212 */           Class<?> componentClass = (Class)componentType;
/* 213 */           if (componentClass.isPrimitive()) {
/* 214 */             return componentClass;
/*     */           }
/*     */         } 
/* 217 */         return subtypeOf(componentType);
/*     */       } 
/*     */     } 
/* 220 */     return null;
/*     */   }
/*     */   
/*     */   private static final class GenericArrayTypeImpl implements GenericArrayType, Serializable {
/*     */     private final Type componentType;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     GenericArrayTypeImpl(Type componentType) {
/* 228 */       this.componentType = Types.JavaVersion.CURRENT.usedInGenericType(componentType);
/*     */     }
/*     */ 
/*     */     
/*     */     public Type getGenericComponentType() {
/* 233 */       return this.componentType;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 238 */       return Types.toString(this.componentType) + "[]";
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 243 */       return this.componentType.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 248 */       if (obj instanceof GenericArrayType) {
/* 249 */         GenericArrayType that = (GenericArrayType)obj;
/* 250 */         return Objects.equal(getGenericComponentType(), that.getGenericComponentType());
/*     */       } 
/* 252 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ParameterizedTypeImpl
/*     */     implements ParameterizedType, Serializable
/*     */   {
/*     */     private final Type ownerType;
/*     */     private final ImmutableList<Type> argumentsList;
/*     */     private final Class<?> rawType;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ParameterizedTypeImpl(Type ownerType, Class<?> rawType, Type[] typeArguments) {
/* 265 */       Preconditions.checkNotNull(rawType);
/* 266 */       Preconditions.checkArgument((typeArguments.length == (rawType.getTypeParameters()).length));
/* 267 */       Types.disallowPrimitiveType(typeArguments, "type parameter");
/* 268 */       this.ownerType = ownerType;
/* 269 */       this.rawType = rawType;
/* 270 */       this.argumentsList = Types.JavaVersion.CURRENT.usedInGenericType(typeArguments);
/*     */     }
/*     */ 
/*     */     
/*     */     public Type[] getActualTypeArguments() {
/* 275 */       return Types.toArray((Collection<Type>)this.argumentsList);
/*     */     }
/*     */ 
/*     */     
/*     */     public Type getRawType() {
/* 280 */       return this.rawType;
/*     */     }
/*     */ 
/*     */     
/*     */     public Type getOwnerType() {
/* 285 */       return this.ownerType;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 290 */       StringBuilder builder = new StringBuilder();
/* 291 */       if (this.ownerType != null && Types.JavaVersion.CURRENT.jdkTypeDuplicatesOwnerName()) {
/* 292 */         builder.append(Types.JavaVersion.CURRENT.typeName(this.ownerType)).append('.');
/*     */       }
/* 294 */       return builder
/* 295 */         .append(this.rawType.getName())
/* 296 */         .append('<')
/* 297 */         .append(Types.COMMA_JOINER.join(Iterables.transform((Iterable)this.argumentsList, Types.TYPE_NAME)))
/* 298 */         .append('>')
/* 299 */         .toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 304 */       return ((this.ownerType == null) ? 0 : this.ownerType.hashCode()) ^ this.argumentsList
/* 305 */         .hashCode() ^ this.rawType
/* 306 */         .hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 311 */       if (!(other instanceof ParameterizedType)) {
/* 312 */         return false;
/*     */       }
/* 314 */       ParameterizedType that = (ParameterizedType)other;
/* 315 */       return (getRawType().equals(that.getRawType()) && 
/* 316 */         Objects.equal(getOwnerType(), that.getOwnerType()) && 
/* 317 */         Arrays.equals((Object[])getActualTypeArguments(), (Object[])that.getActualTypeArguments()));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <D extends GenericDeclaration> TypeVariable<D> newTypeVariableImpl(D genericDeclaration, String name, Type[] bounds) {
/* 325 */     TypeVariableImpl<D> typeVariableImpl = new TypeVariableImpl<>(genericDeclaration, name, bounds);
/*     */ 
/*     */ 
/*     */     
/* 329 */     TypeVariable<D> typeVariable = Reflection.<TypeVariable<D>>newProxy((Class)TypeVariable.class, new TypeVariableInvocationHandler(typeVariableImpl));
/*     */     
/* 331 */     return typeVariable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class TypeVariableInvocationHandler
/*     */     implements InvocationHandler
/*     */   {
/*     */     private static final ImmutableMap<String, Method> typeVariableMethods;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Types.TypeVariableImpl<?> typeVariableImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/* 358 */       ImmutableMap.Builder<String, Method> builder = ImmutableMap.builder();
/* 359 */       for (Method method : Types.TypeVariableImpl.class.getMethods()) {
/* 360 */         if (method.getDeclaringClass().equals(Types.TypeVariableImpl.class)) {
/*     */           try {
/* 362 */             method.setAccessible(true);
/* 363 */           } catch (AccessControlException accessControlException) {}
/*     */ 
/*     */ 
/*     */           
/* 367 */           builder.put(method.getName(), method);
/*     */         } 
/*     */       } 
/* 370 */       typeVariableMethods = builder.build();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     TypeVariableInvocationHandler(Types.TypeVariableImpl<?> typeVariableImpl) {
/* 376 */       this.typeVariableImpl = typeVariableImpl;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 381 */       String methodName = method.getName();
/* 382 */       Method typeVariableMethod = (Method)typeVariableMethods.get(methodName);
/* 383 */       if (typeVariableMethod == null) {
/* 384 */         throw new UnsupportedOperationException(methodName);
/*     */       }
/*     */       try {
/* 387 */         return typeVariableMethod.invoke(this.typeVariableImpl, args);
/* 388 */       } catch (InvocationTargetException e) {
/* 389 */         throw e.getCause();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class TypeVariableImpl<D extends GenericDeclaration>
/*     */   {
/*     */     private final D genericDeclaration;
/*     */     private final String name;
/*     */     private final ImmutableList<Type> bounds;
/*     */     
/*     */     TypeVariableImpl(D genericDeclaration, String name, Type[] bounds) {
/* 402 */       Types.disallowPrimitiveType(bounds, "bound for type variable");
/* 403 */       this.genericDeclaration = (D)Preconditions.checkNotNull(genericDeclaration);
/* 404 */       this.name = (String)Preconditions.checkNotNull(name);
/* 405 */       this.bounds = ImmutableList.copyOf((Object[])bounds);
/*     */     }
/*     */     
/*     */     public Type[] getBounds() {
/* 409 */       return Types.toArray((Collection<Type>)this.bounds);
/*     */     }
/*     */     
/*     */     public D getGenericDeclaration() {
/* 413 */       return this.genericDeclaration;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 417 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getTypeName() {
/* 421 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 426 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 431 */       return this.genericDeclaration.hashCode() ^ this.name.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 436 */       if (Types.NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY) {
/*     */         
/* 438 */         if (obj != null && 
/* 439 */           Proxy.isProxyClass(obj.getClass()) && 
/* 440 */           Proxy.getInvocationHandler(obj) instanceof Types.TypeVariableInvocationHandler) {
/*     */           
/* 442 */           Types.TypeVariableInvocationHandler typeVariableInvocationHandler = (Types.TypeVariableInvocationHandler)Proxy.getInvocationHandler(obj);
/* 443 */           TypeVariableImpl<?> that = typeVariableInvocationHandler.typeVariableImpl;
/* 444 */           return (this.name.equals(that.getName()) && this.genericDeclaration
/* 445 */             .equals(that.getGenericDeclaration()) && this.bounds
/* 446 */             .equals(that.bounds));
/*     */         } 
/* 448 */         return false;
/*     */       } 
/*     */       
/* 451 */       if (obj instanceof TypeVariable) {
/* 452 */         TypeVariable<?> that = (TypeVariable)obj;
/* 453 */         return (this.name.equals(that.getName()) && this.genericDeclaration
/* 454 */           .equals(that.getGenericDeclaration()));
/*     */       } 
/* 456 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class WildcardTypeImpl
/*     */     implements WildcardType, Serializable {
/*     */     private final ImmutableList<Type> lowerBounds;
/*     */     private final ImmutableList<Type> upperBounds;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     WildcardTypeImpl(Type[] lowerBounds, Type[] upperBounds) {
/* 467 */       Types.disallowPrimitiveType(lowerBounds, "lower bound for wildcard");
/* 468 */       Types.disallowPrimitiveType(upperBounds, "upper bound for wildcard");
/* 469 */       this.lowerBounds = Types.JavaVersion.CURRENT.usedInGenericType(lowerBounds);
/* 470 */       this.upperBounds = Types.JavaVersion.CURRENT.usedInGenericType(upperBounds);
/*     */     }
/*     */ 
/*     */     
/*     */     public Type[] getLowerBounds() {
/* 475 */       return Types.toArray((Collection<Type>)this.lowerBounds);
/*     */     }
/*     */ 
/*     */     
/*     */     public Type[] getUpperBounds() {
/* 480 */       return Types.toArray((Collection<Type>)this.upperBounds);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 485 */       if (obj instanceof WildcardType) {
/* 486 */         WildcardType that = (WildcardType)obj;
/* 487 */         return (this.lowerBounds.equals(Arrays.asList(that.getLowerBounds())) && this.upperBounds
/* 488 */           .equals(Arrays.asList(that.getUpperBounds())));
/*     */       } 
/* 490 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 495 */       return this.lowerBounds.hashCode() ^ this.upperBounds.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 500 */       StringBuilder builder = new StringBuilder("?");
/* 501 */       for (UnmodifiableIterator<Type> unmodifiableIterator = this.lowerBounds.iterator(); unmodifiableIterator.hasNext(); ) { Type lowerBound = unmodifiableIterator.next();
/* 502 */         builder.append(" super ").append(Types.JavaVersion.CURRENT.typeName(lowerBound)); }
/*     */       
/* 504 */       for (Type upperBound : Types.filterUpperBounds((Iterable<Type>)this.upperBounds)) {
/* 505 */         builder.append(" extends ").append(Types.JavaVersion.CURRENT.typeName(upperBound));
/*     */       }
/* 507 */       return builder.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Type[] toArray(Collection<Type> types) {
/* 514 */     return types.<Type>toArray(new Type[types.size()]);
/*     */   }
/*     */   
/*     */   private static Iterable<Type> filterUpperBounds(Iterable<Type> bounds) {
/* 518 */     return Iterables.filter(bounds, Predicates.not(Predicates.equalTo(Object.class)));
/*     */   }
/*     */   
/*     */   private static void disallowPrimitiveType(Type[] types, String usedAs) {
/* 522 */     for (Type type : types) {
/* 523 */       if (type instanceof Class) {
/* 524 */         Class<?> cls = (Class)type;
/* 525 */         Preconditions.checkArgument(!cls.isPrimitive(), "Primitive type '%s' used as %s", cls, usedAs);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Class<?> getArrayClass(Class<?> componentType) {
/* 535 */     return Array.newInstance(componentType, 0).getClass();
/*     */   }
/*     */   
/*     */   enum JavaVersion
/*     */   {
/* 540 */     JAVA6
/*     */     {
/*     */       GenericArrayType newArrayType(Type componentType) {
/* 543 */         return new Types.GenericArrayTypeImpl(componentType);
/*     */       }
/*     */ 
/*     */       
/*     */       Type usedInGenericType(Type type) {
/* 548 */         Preconditions.checkNotNull(type);
/* 549 */         if (type instanceof Class) {
/* 550 */           Class<?> cls = (Class)type;
/* 551 */           if (cls.isArray()) {
/* 552 */             return new Types.GenericArrayTypeImpl(cls.getComponentType());
/*     */           }
/*     */         } 
/* 555 */         return type;
/*     */       }
/*     */     },
/* 558 */     JAVA7
/*     */     {
/*     */       Type newArrayType(Type componentType) {
/* 561 */         if (componentType instanceof Class) {
/* 562 */           return Types.getArrayClass((Class)componentType);
/*     */         }
/* 564 */         return new Types.GenericArrayTypeImpl(componentType);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       Type usedInGenericType(Type type) {
/* 570 */         return (Type)Preconditions.checkNotNull(type);
/*     */       }
/*     */     },
/* 573 */     JAVA8
/*     */     {
/*     */       Type newArrayType(Type componentType) {
/* 576 */         return JAVA7.newArrayType(componentType);
/*     */       }
/*     */ 
/*     */       
/*     */       Type usedInGenericType(Type type) {
/* 581 */         return JAVA7.usedInGenericType(type);
/*     */       }
/*     */ 
/*     */       
/*     */       String typeName(Type type) {
/*     */         try {
/* 587 */           Method getTypeName = Type.class.getMethod("getTypeName", new Class[0]);
/* 588 */           return (String)getTypeName.invoke(type, new Object[0]);
/* 589 */         } catch (NoSuchMethodException e) {
/* 590 */           throw new AssertionError("Type.getTypeName should be available in Java 8");
/* 591 */         } catch (InvocationTargetException|IllegalAccessException e) {
/* 592 */           throw new RuntimeException(e);
/*     */         } 
/*     */       }
/*     */     },
/* 596 */     JAVA9
/*     */     {
/*     */       Type newArrayType(Type componentType) {
/* 599 */         return JAVA8.newArrayType(componentType);
/*     */       }
/*     */ 
/*     */       
/*     */       Type usedInGenericType(Type type) {
/* 604 */         return JAVA8.usedInGenericType(type);
/*     */       }
/*     */ 
/*     */       
/*     */       String typeName(Type type) {
/* 609 */         return JAVA8.typeName(type);
/*     */       }
/*     */ 
/*     */       
/*     */       boolean jdkTypeDuplicatesOwnerName() {
/* 614 */         return false;
/*     */       }
/*     */     };
/*     */     
/*     */     static final JavaVersion CURRENT;
/*     */     
/*     */     static {
/* 621 */       if (AnnotatedElement.class.isAssignableFrom(TypeVariable.class)) {
/* 622 */         if ((new TypeCapture<Map.Entry<String, int[][]>>() {  }).capture()
/* 623 */           .toString()
/* 624 */           .contains("java.util.Map.java.util.Map")) {
/* 625 */           CURRENT = JAVA8;
/*     */         } else {
/* 627 */           CURRENT = JAVA9;
/*     */         } 
/* 629 */       } else if ((new TypeCapture<int[]>() {  }).capture() instanceof Class) {
/* 630 */         CURRENT = JAVA7;
/*     */       } else {
/* 632 */         CURRENT = JAVA6;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final ImmutableList<Type> usedInGenericType(Type[] types) {
/* 641 */       ImmutableList.Builder<Type> builder = ImmutableList.builder();
/* 642 */       for (Type type : types) {
/* 643 */         builder.add(usedInGenericType(type));
/*     */       }
/* 645 */       return builder.build();
/*     */     }
/*     */     
/*     */     String typeName(Type type) {
/* 649 */       return Types.toString(type);
/*     */     }
/*     */     
/*     */     boolean jdkTypeDuplicatesOwnerName() {
/* 653 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     abstract Type newArrayType(Type param1Type);
/*     */ 
/*     */ 
/*     */     
/*     */     abstract Type usedInGenericType(Type param1Type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class NativeTypeVariableEquals<X>
/*     */   {
/* 669 */     static final boolean NATIVE_TYPE_VARIABLE_ONLY = !NativeTypeVariableEquals.class.getTypeParameters()[0].equals(
/* 670 */         Types.newArtificialTypeVariable(NativeTypeVariableEquals.class, "X", new Type[0]));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\reflect\Types.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */