/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.GenericDeclaration;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ @Beta
/*     */ public final class TypeResolver
/*     */ {
/*     */   private final TypeTable typeTable;
/*     */   
/*     */   public TypeResolver() {
/*  60 */     this.typeTable = new TypeTable();
/*     */   }
/*     */   
/*     */   private TypeResolver(TypeTable typeTable) {
/*  64 */     this.typeTable = typeTable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static TypeResolver covariantly(Type contextType) {
/*  75 */     return (new TypeResolver()).where((Map<TypeVariableKey, ? extends Type>)TypeMappingIntrospector.getTypeMappings(contextType));
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
/*     */   static TypeResolver invariantly(Type contextType) {
/*  90 */     Type invariantContext = WildcardCapturer.INSTANCE.capture(contextType);
/*  91 */     return (new TypeResolver()).where((Map<TypeVariableKey, ? extends Type>)TypeMappingIntrospector.getTypeMappings(invariantContext));
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
/*     */   
/*     */   public TypeResolver where(Type formal, Type actual) {
/* 114 */     Map<TypeVariableKey, Type> mappings = Maps.newHashMap();
/* 115 */     populateTypeMappings(mappings, (Type)Preconditions.checkNotNull(formal), (Type)Preconditions.checkNotNull(actual));
/* 116 */     return where(mappings);
/*     */   }
/*     */ 
/*     */   
/*     */   TypeResolver where(Map<TypeVariableKey, ? extends Type> mappings) {
/* 121 */     return new TypeResolver(this.typeTable.where(mappings));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void populateTypeMappings(final Map<TypeVariableKey, Type> mappings, Type from, final Type to) {
/* 126 */     if (from.equals(to)) {
/*     */       return;
/*     */     }
/* 129 */     (new TypeVisitor()
/*     */       {
/*     */         void visitTypeVariable(TypeVariable<?> typeVariable) {
/* 132 */           mappings.put(new TypeResolver.TypeVariableKey(typeVariable), to);
/*     */         }
/*     */ 
/*     */         
/*     */         void visitWildcardType(WildcardType fromWildcardType) {
/* 137 */           if (!(to instanceof WildcardType)) {
/*     */             return;
/*     */           }
/* 140 */           WildcardType toWildcardType = (WildcardType)to;
/* 141 */           Type[] fromUpperBounds = fromWildcardType.getUpperBounds();
/* 142 */           Type[] toUpperBounds = toWildcardType.getUpperBounds();
/* 143 */           Type[] fromLowerBounds = fromWildcardType.getLowerBounds();
/* 144 */           Type[] toLowerBounds = toWildcardType.getLowerBounds();
/* 145 */           Preconditions.checkArgument((fromUpperBounds.length == toUpperBounds.length && fromLowerBounds.length == toLowerBounds.length), "Incompatible type: %s vs. %s", fromWildcardType, to);
/*     */ 
/*     */           
/*     */           int i;
/*     */ 
/*     */           
/* 151 */           for (i = 0; i < fromUpperBounds.length; i++) {
/* 152 */             TypeResolver.populateTypeMappings(mappings, fromUpperBounds[i], toUpperBounds[i]);
/*     */           }
/* 154 */           for (i = 0; i < fromLowerBounds.length; i++) {
/* 155 */             TypeResolver.populateTypeMappings(mappings, fromLowerBounds[i], toLowerBounds[i]);
/*     */           }
/*     */         }
/*     */ 
/*     */         
/*     */         void visitParameterizedType(ParameterizedType fromParameterizedType) {
/* 161 */           if (to instanceof WildcardType) {
/*     */             return;
/*     */           }
/* 164 */           ParameterizedType toParameterizedType = (ParameterizedType)TypeResolver.expectArgument((Class)ParameterizedType.class, to);
/* 165 */           if (fromParameterizedType.getOwnerType() != null && toParameterizedType
/* 166 */             .getOwnerType() != null) {
/* 167 */             TypeResolver.populateTypeMappings(mappings, fromParameterizedType
/* 168 */                 .getOwnerType(), toParameterizedType.getOwnerType());
/*     */           }
/* 170 */           Preconditions.checkArgument(fromParameterizedType
/* 171 */               .getRawType().equals(toParameterizedType.getRawType()), "Inconsistent raw type: %s vs. %s", fromParameterizedType, to);
/*     */ 
/*     */ 
/*     */           
/* 175 */           Type[] fromArgs = fromParameterizedType.getActualTypeArguments();
/* 176 */           Type[] toArgs = toParameterizedType.getActualTypeArguments();
/* 177 */           Preconditions.checkArgument((fromArgs.length == toArgs.length), "%s not compatible with %s", fromParameterizedType, toParameterizedType);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 182 */           for (int i = 0; i < fromArgs.length; i++) {
/* 183 */             TypeResolver.populateTypeMappings(mappings, fromArgs[i], toArgs[i]);
/*     */           }
/*     */         }
/*     */ 
/*     */         
/*     */         void visitGenericArrayType(GenericArrayType fromArrayType) {
/* 189 */           if (to instanceof WildcardType) {
/*     */             return;
/*     */           }
/* 192 */           Type componentType = Types.getComponentType(to);
/* 193 */           Preconditions.checkArgument((componentType != null), "%s is not an array type.", to);
/* 194 */           TypeResolver.populateTypeMappings(mappings, fromArrayType.getGenericComponentType(), componentType);
/*     */         }
/*     */ 
/*     */         
/*     */         void visitClass(Class<?> fromClass) {
/* 199 */           if (to instanceof WildcardType) {
/*     */             return;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 205 */           throw new IllegalArgumentException("No type mapping from " + fromClass + " to " + to);
/*     */         }
/* 207 */       }).visit(new Type[] { from });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type resolveType(Type type) {
/* 215 */     Preconditions.checkNotNull(type);
/* 216 */     if (type instanceof TypeVariable)
/* 217 */       return this.typeTable.resolve((TypeVariable)type); 
/* 218 */     if (type instanceof ParameterizedType)
/* 219 */       return resolveParameterizedType((ParameterizedType)type); 
/* 220 */     if (type instanceof GenericArrayType)
/* 221 */       return resolveGenericArrayType((GenericArrayType)type); 
/* 222 */     if (type instanceof WildcardType) {
/* 223 */       return resolveWildcardType((WildcardType)type);
/*     */     }
/*     */     
/* 226 */     return type;
/*     */   }
/*     */ 
/*     */   
/*     */   Type[] resolveTypesInPlace(Type[] types) {
/* 231 */     for (int i = 0; i < types.length; i++) {
/* 232 */       types[i] = resolveType(types[i]);
/*     */     }
/* 234 */     return types;
/*     */   }
/*     */   
/*     */   private Type[] resolveTypes(Type[] types) {
/* 238 */     Type[] result = new Type[types.length];
/* 239 */     for (int i = 0; i < types.length; i++) {
/* 240 */       result[i] = resolveType(types[i]);
/*     */     }
/* 242 */     return result;
/*     */   }
/*     */   
/*     */   private WildcardType resolveWildcardType(WildcardType type) {
/* 246 */     Type[] lowerBounds = type.getLowerBounds();
/* 247 */     Type[] upperBounds = type.getUpperBounds();
/* 248 */     return new Types.WildcardTypeImpl(resolveTypes(lowerBounds), resolveTypes(upperBounds));
/*     */   }
/*     */   
/*     */   private Type resolveGenericArrayType(GenericArrayType type) {
/* 252 */     Type componentType = type.getGenericComponentType();
/* 253 */     Type resolvedComponentType = resolveType(componentType);
/* 254 */     return Types.newArrayType(resolvedComponentType);
/*     */   }
/*     */   
/*     */   private ParameterizedType resolveParameterizedType(ParameterizedType type) {
/* 258 */     Type owner = type.getOwnerType();
/* 259 */     Type resolvedOwner = (owner == null) ? null : resolveType(owner);
/* 260 */     Type resolvedRawType = resolveType(type.getRawType());
/*     */     
/* 262 */     Type[] args = type.getActualTypeArguments();
/* 263 */     Type[] resolvedArgs = resolveTypes(args);
/* 264 */     return Types.newParameterizedTypeWithOwner(resolvedOwner, (Class)resolvedRawType, resolvedArgs);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> T expectArgument(Class<T> type, Object arg) {
/*     */     try {
/* 270 */       return type.cast(arg);
/* 271 */     } catch (ClassCastException e) {
/* 272 */       throw new IllegalArgumentException(arg + " is not a " + type.getSimpleName());
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class TypeTable
/*     */   {
/*     */     private final ImmutableMap<TypeResolver.TypeVariableKey, Type> map;
/*     */     
/*     */     TypeTable() {
/* 281 */       this.map = ImmutableMap.of();
/*     */     }
/*     */     
/*     */     private TypeTable(ImmutableMap<TypeResolver.TypeVariableKey, Type> map) {
/* 285 */       this.map = map;
/*     */     }
/*     */ 
/*     */     
/*     */     final TypeTable where(Map<TypeResolver.TypeVariableKey, ? extends Type> mappings) {
/* 290 */       ImmutableMap.Builder<TypeResolver.TypeVariableKey, Type> builder = ImmutableMap.builder();
/* 291 */       builder.putAll((Map)this.map);
/* 292 */       for (Map.Entry<TypeResolver.TypeVariableKey, ? extends Type> mapping : mappings.entrySet()) {
/* 293 */         TypeResolver.TypeVariableKey variable = mapping.getKey();
/* 294 */         Type type = mapping.getValue();
/* 295 */         Preconditions.checkArgument(!variable.equalsType(type), "Type variable %s bound to itself", variable);
/* 296 */         builder.put(variable, type);
/*     */       } 
/* 298 */       return new TypeTable(builder.build());
/*     */     }
/*     */     
/*     */     final Type resolve(final TypeVariable<?> var) {
/* 302 */       final TypeTable unguarded = this;
/* 303 */       TypeTable guarded = new TypeTable()
/*     */         {
/*     */           public Type resolveInternal(TypeVariable<?> intermediateVar, TypeResolver.TypeTable forDependent)
/*     */           {
/* 307 */             if (intermediateVar.getGenericDeclaration().equals(var.getGenericDeclaration())) {
/* 308 */               return intermediateVar;
/*     */             }
/* 310 */             return unguarded.resolveInternal(intermediateVar, forDependent);
/*     */           }
/*     */         };
/* 313 */       return resolveInternal(var, guarded);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Type resolveInternal(TypeVariable<?> var, TypeTable forDependants) {
/* 325 */       Type type = (Type)this.map.get(new TypeResolver.TypeVariableKey(var));
/* 326 */       if (type == null) {
/* 327 */         Type[] bounds = var.getBounds();
/* 328 */         if (bounds.length == 0) {
/* 329 */           return var;
/*     */         }
/* 331 */         Type[] resolvedBounds = (new TypeResolver(forDependants)).resolveTypes(bounds);
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
/* 360 */         if (Types.NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY && 
/* 361 */           Arrays.equals((Object[])bounds, (Object[])resolvedBounds)) {
/* 362 */           return var;
/*     */         }
/* 364 */         return Types.newArtificialTypeVariable((GenericDeclaration)var
/* 365 */             .getGenericDeclaration(), var.getName(), resolvedBounds);
/*     */       } 
/*     */       
/* 368 */       return (new TypeResolver(forDependants)).resolveType(type);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class TypeMappingIntrospector
/*     */     extends TypeVisitor {
/* 374 */     private final Map<TypeResolver.TypeVariableKey, Type> mappings = Maps.newHashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static ImmutableMap<TypeResolver.TypeVariableKey, Type> getTypeMappings(Type contextType) {
/* 381 */       Preconditions.checkNotNull(contextType);
/* 382 */       TypeMappingIntrospector introspector = new TypeMappingIntrospector();
/* 383 */       introspector.visit(new Type[] { contextType });
/* 384 */       return ImmutableMap.copyOf(introspector.mappings);
/*     */     }
/*     */ 
/*     */     
/*     */     void visitClass(Class<?> clazz) {
/* 389 */       visit(new Type[] { clazz.getGenericSuperclass() });
/* 390 */       visit(clazz.getGenericInterfaces());
/*     */     }
/*     */ 
/*     */     
/*     */     void visitParameterizedType(ParameterizedType parameterizedType) {
/* 395 */       Class<?> rawClass = (Class)parameterizedType.getRawType();
/* 396 */       TypeVariable[] arrayOfTypeVariable = (TypeVariable[])rawClass.getTypeParameters();
/* 397 */       Type[] typeArgs = parameterizedType.getActualTypeArguments();
/* 398 */       Preconditions.checkState((arrayOfTypeVariable.length == typeArgs.length));
/* 399 */       for (int i = 0; i < arrayOfTypeVariable.length; i++) {
/* 400 */         map(new TypeResolver.TypeVariableKey(arrayOfTypeVariable[i]), typeArgs[i]);
/*     */       }
/* 402 */       visit(new Type[] { rawClass });
/* 403 */       visit(new Type[] { parameterizedType.getOwnerType() });
/*     */     }
/*     */ 
/*     */     
/*     */     void visitTypeVariable(TypeVariable<?> t) {
/* 408 */       visit(t.getBounds());
/*     */     }
/*     */ 
/*     */     
/*     */     void visitWildcardType(WildcardType t) {
/* 413 */       visit(t.getUpperBounds());
/*     */     }
/*     */     
/*     */     private void map(TypeResolver.TypeVariableKey var, Type arg) {
/* 417 */       if (this.mappings.containsKey(var)) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 426 */       for (Type t = arg; t != null; t = this.mappings.get(TypeResolver.TypeVariableKey.forLookup(t))) {
/* 427 */         if (var.equalsType(t)) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 432 */           for (Type x = arg; x != null; x = this.mappings.remove(TypeResolver.TypeVariableKey.forLookup(x)));
/*     */           return;
/*     */         } 
/*     */       } 
/* 436 */       this.mappings.put(var, arg);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class WildcardCapturer
/*     */   {
/* 449 */     static final WildcardCapturer INSTANCE = new WildcardCapturer();
/*     */     
/*     */     private final AtomicInteger id;
/*     */     
/*     */     private WildcardCapturer() {
/* 454 */       this(new AtomicInteger());
/*     */     }
/*     */     
/*     */     private WildcardCapturer(AtomicInteger id) {
/* 458 */       this.id = id;
/*     */     }
/*     */     
/*     */     final Type capture(Type type) {
/* 462 */       Preconditions.checkNotNull(type);
/* 463 */       if (type instanceof Class) {
/* 464 */         return type;
/*     */       }
/* 466 */       if (type instanceof TypeVariable) {
/* 467 */         return type;
/*     */       }
/* 469 */       if (type instanceof GenericArrayType) {
/* 470 */         GenericArrayType arrayType = (GenericArrayType)type;
/* 471 */         return Types.newArrayType(
/* 472 */             notForTypeVariable().capture(arrayType.getGenericComponentType()));
/*     */       } 
/* 474 */       if (type instanceof ParameterizedType) {
/* 475 */         ParameterizedType parameterizedType = (ParameterizedType)type;
/* 476 */         Class<?> rawType = (Class)parameterizedType.getRawType();
/* 477 */         TypeVariable[] arrayOfTypeVariable = (TypeVariable[])rawType.getTypeParameters();
/* 478 */         Type[] typeArgs = parameterizedType.getActualTypeArguments();
/* 479 */         for (int i = 0; i < typeArgs.length; i++) {
/* 480 */           typeArgs[i] = forTypeVariable(arrayOfTypeVariable[i]).capture(typeArgs[i]);
/*     */         }
/* 482 */         return Types.newParameterizedTypeWithOwner(
/* 483 */             notForTypeVariable().captureNullable(parameterizedType.getOwnerType()), rawType, typeArgs);
/*     */       } 
/*     */ 
/*     */       
/* 487 */       if (type instanceof WildcardType) {
/* 488 */         WildcardType wildcardType = (WildcardType)type;
/* 489 */         Type[] lowerBounds = wildcardType.getLowerBounds();
/* 490 */         if (lowerBounds.length == 0) {
/* 491 */           return captureAsTypeVariable(wildcardType.getUpperBounds());
/*     */         }
/*     */         
/* 494 */         return type;
/*     */       } 
/*     */       
/* 497 */       throw new AssertionError("must have been one of the known types");
/*     */     }
/*     */ 
/*     */     
/*     */     TypeVariable<?> captureAsTypeVariable(Type[] upperBounds) {
/* 502 */       String name = "capture#" + this.id.incrementAndGet() + "-of ? extends " + Joiner.on('&').join((Object[])upperBounds);
/* 503 */       return Types.newArtificialTypeVariable(WildcardCapturer.class, name, upperBounds);
/*     */     }
/*     */     
/*     */     private WildcardCapturer forTypeVariable(final TypeVariable<?> typeParam) {
/* 507 */       return new WildcardCapturer(this.id)
/*     */         {
/*     */           TypeVariable<?> captureAsTypeVariable(Type[] upperBounds) {
/* 510 */             Set<Type> combined = new LinkedHashSet<>(Arrays.asList(upperBounds));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 517 */             combined.addAll(Arrays.asList(typeParam.getBounds()));
/* 518 */             if (combined.size() > 1) {
/* 519 */               combined.remove(Object.class);
/*     */             }
/* 521 */             return super.captureAsTypeVariable(combined.<Type>toArray(new Type[0]));
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     private WildcardCapturer notForTypeVariable() {
/* 527 */       return new WildcardCapturer(this.id);
/*     */     }
/*     */     
/*     */     private Type captureNullable(Type type) {
/* 531 */       if (type == null) {
/* 532 */         return null;
/*     */       }
/* 534 */       return capture(type);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class TypeVariableKey
/*     */   {
/*     */     private final TypeVariable<?> var;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     TypeVariableKey(TypeVariable<?> var) {
/* 555 */       this.var = (TypeVariable)Preconditions.checkNotNull(var);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 560 */       return Objects.hashCode(new Object[] { this.var.getGenericDeclaration(), this.var.getName() });
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 565 */       if (obj instanceof TypeVariableKey) {
/* 566 */         TypeVariableKey that = (TypeVariableKey)obj;
/* 567 */         return equalsTypeVariable(that.var);
/*     */       } 
/* 569 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 575 */       return this.var.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     static TypeVariableKey forLookup(Type t) {
/* 580 */       if (t instanceof TypeVariable) {
/* 581 */         return new TypeVariableKey((TypeVariable)t);
/*     */       }
/* 583 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean equalsType(Type type) {
/* 592 */       if (type instanceof TypeVariable) {
/* 593 */         return equalsTypeVariable((TypeVariable)type);
/*     */       }
/* 595 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean equalsTypeVariable(TypeVariable<?> that) {
/* 600 */       return (this.var.getGenericDeclaration().equals(that.getGenericDeclaration()) && this.var
/* 601 */         .getName().equals(that.getName()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\reflect\TypeResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */