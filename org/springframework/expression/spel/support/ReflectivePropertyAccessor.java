/*     */ package org.springframework.expression.spel.support;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.Property;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.PropertyAccessor;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.CompilablePropertyAccessor;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReflectivePropertyAccessor
/*     */   implements PropertyAccessor
/*     */ {
/*  66 */   private static final Set<Class<?>> ANY_TYPES = Collections.emptySet();
/*     */   private static final Set<Class<?>> BOOLEAN_TYPES;
/*     */   private final boolean allowWrite;
/*     */   
/*     */   static {
/*  71 */     Set<Class<?>> booleanTypes = new HashSet<>(4);
/*  72 */     booleanTypes.add(Boolean.class);
/*  73 */     booleanTypes.add(boolean.class);
/*  74 */     BOOLEAN_TYPES = Collections.unmodifiableSet(booleanTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   private final Map<PropertyCacheKey, InvokerPair> readerCache = new ConcurrentHashMap<>(64);
/*     */   
/*  82 */   private final Map<PropertyCacheKey, Member> writerCache = new ConcurrentHashMap<>(64);
/*     */   
/*  84 */   private final Map<PropertyCacheKey, TypeDescriptor> typeDescriptorCache = new ConcurrentHashMap<>(64);
/*     */   
/*  86 */   private final Map<Class<?>, Method[]> sortedMethodsCache = (Map)new ConcurrentHashMap<>(64);
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private volatile InvokerPair lastReadInvokerPair;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReflectivePropertyAccessor() {
/*  97 */     this.allowWrite = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReflectivePropertyAccessor(boolean allowWrite) {
/* 107 */     this.allowWrite = allowWrite;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Class<?>[] getSpecificTargetClasses() {
/* 117 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canRead(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
/* 122 */     if (target == null) {
/* 123 */       return false;
/*     */     }
/*     */     
/* 126 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/* 127 */     if (type.isArray() && name.equals("length")) {
/* 128 */       return true;
/*     */     }
/*     */     
/* 131 */     PropertyCacheKey cacheKey = new PropertyCacheKey(type, name, target instanceof Class);
/* 132 */     if (this.readerCache.containsKey(cacheKey)) {
/* 133 */       return true;
/*     */     }
/*     */     
/* 136 */     Method method = findGetterForProperty(name, type, target);
/* 137 */     if (method != null) {
/*     */ 
/*     */       
/* 140 */       Property property = new Property(type, method, null);
/* 141 */       TypeDescriptor typeDescriptor = new TypeDescriptor(property);
/* 142 */       method = ClassUtils.getInterfaceMethodIfPossible(method);
/* 143 */       this.readerCache.put(cacheKey, new InvokerPair(method, typeDescriptor));
/* 144 */       this.typeDescriptorCache.put(cacheKey, typeDescriptor);
/* 145 */       return true;
/*     */     } 
/*     */     
/* 148 */     Field field = findField(name, type, target);
/* 149 */     if (field != null) {
/* 150 */       TypeDescriptor typeDescriptor = new TypeDescriptor(field);
/* 151 */       this.readerCache.put(cacheKey, new InvokerPair(field, typeDescriptor));
/* 152 */       this.typeDescriptorCache.put(cacheKey, typeDescriptor);
/* 153 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 157 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypedValue read(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
/* 162 */     Assert.state((target != null), "Target must not be null");
/* 163 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/*     */     
/* 165 */     if (type.isArray() && name.equals("length")) {
/* 166 */       if (target instanceof Class) {
/* 167 */         throw new AccessException("Cannot access length on array class itself");
/*     */       }
/* 169 */       return new TypedValue(Integer.valueOf(Array.getLength(target)));
/*     */     } 
/*     */     
/* 172 */     PropertyCacheKey cacheKey = new PropertyCacheKey(type, name, target instanceof Class);
/* 173 */     InvokerPair invoker = this.readerCache.get(cacheKey);
/* 174 */     this.lastReadInvokerPair = invoker;
/*     */     
/* 176 */     if (invoker == null || invoker.member instanceof Method) {
/* 177 */       Method method = (invoker != null) ? (Method)invoker.member : null;
/* 178 */       if (method == null) {
/* 179 */         method = findGetterForProperty(name, type, target);
/* 180 */         if (method != null) {
/*     */ 
/*     */           
/* 183 */           Property property = new Property(type, method, null);
/* 184 */           TypeDescriptor typeDescriptor = new TypeDescriptor(property);
/* 185 */           method = ClassUtils.getInterfaceMethodIfPossible(method);
/* 186 */           invoker = new InvokerPair(method, typeDescriptor);
/* 187 */           this.lastReadInvokerPair = invoker;
/* 188 */           this.readerCache.put(cacheKey, invoker);
/*     */         } 
/*     */       } 
/* 191 */       if (method != null) {
/*     */         try {
/* 193 */           ReflectionUtils.makeAccessible(method);
/* 194 */           Object value = method.invoke(target, new Object[0]);
/* 195 */           return new TypedValue(value, invoker.typeDescriptor.narrow(value));
/*     */         }
/* 197 */         catch (Exception ex) {
/* 198 */           throw new AccessException("Unable to access property '" + name + "' through getter method", ex);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 203 */     if (invoker == null || invoker.member instanceof Field) {
/* 204 */       Field field = (invoker == null) ? null : (Field)invoker.member;
/* 205 */       if (field == null) {
/* 206 */         field = findField(name, type, target);
/* 207 */         if (field != null) {
/* 208 */           invoker = new InvokerPair(field, new TypeDescriptor(field));
/* 209 */           this.lastReadInvokerPair = invoker;
/* 210 */           this.readerCache.put(cacheKey, invoker);
/*     */         } 
/*     */       } 
/* 213 */       if (field != null) {
/*     */         try {
/* 215 */           ReflectionUtils.makeAccessible(field);
/* 216 */           Object value = field.get(target);
/* 217 */           return new TypedValue(value, invoker.typeDescriptor.narrow(value));
/*     */         }
/* 219 */         catch (Exception ex) {
/* 220 */           throw new AccessException("Unable to access field '" + name + "'", ex);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 225 */     throw new AccessException("Neither getter method nor field found for property '" + name + "'");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canWrite(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
/* 230 */     if (!this.allowWrite || target == null) {
/* 231 */       return false;
/*     */     }
/*     */     
/* 234 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/* 235 */     PropertyCacheKey cacheKey = new PropertyCacheKey(type, name, target instanceof Class);
/* 236 */     if (this.writerCache.containsKey(cacheKey)) {
/* 237 */       return true;
/*     */     }
/*     */     
/* 240 */     Method method = findSetterForProperty(name, type, target);
/* 241 */     if (method != null) {
/*     */       
/* 243 */       Property property = new Property(type, null, method);
/* 244 */       TypeDescriptor typeDescriptor = new TypeDescriptor(property);
/* 245 */       method = ClassUtils.getInterfaceMethodIfPossible(method);
/* 246 */       this.writerCache.put(cacheKey, method);
/* 247 */       this.typeDescriptorCache.put(cacheKey, typeDescriptor);
/* 248 */       return true;
/*     */     } 
/*     */     
/* 251 */     Field field = findField(name, type, target);
/* 252 */     if (field != null) {
/* 253 */       this.writerCache.put(cacheKey, field);
/* 254 */       this.typeDescriptorCache.put(cacheKey, new TypeDescriptor(field));
/* 255 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 259 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(EvaluationContext context, @Nullable Object target, String name, @Nullable Object newValue) throws AccessException {
/* 266 */     if (!this.allowWrite) {
/* 267 */       throw new AccessException("PropertyAccessor for property '" + name + "' on target [" + target + "] does not allow write operations");
/*     */     }
/*     */ 
/*     */     
/* 271 */     Assert.state((target != null), "Target must not be null");
/* 272 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/*     */     
/* 274 */     Object possiblyConvertedNewValue = newValue;
/* 275 */     TypeDescriptor typeDescriptor = getTypeDescriptor(context, target, name);
/* 276 */     if (typeDescriptor != null) {
/*     */       try {
/* 278 */         possiblyConvertedNewValue = context.getTypeConverter().convertValue(newValue, 
/* 279 */             TypeDescriptor.forObject(newValue), typeDescriptor);
/*     */       }
/* 281 */       catch (EvaluationException evaluationException) {
/* 282 */         throw new AccessException("Type conversion failure", evaluationException);
/*     */       } 
/*     */     }
/*     */     
/* 286 */     PropertyCacheKey cacheKey = new PropertyCacheKey(type, name, target instanceof Class);
/* 287 */     Member cachedMember = this.writerCache.get(cacheKey);
/*     */     
/* 289 */     if (cachedMember == null || cachedMember instanceof Method) {
/* 290 */       Method method = (Method)cachedMember;
/* 291 */       if (method == null) {
/* 292 */         method = findSetterForProperty(name, type, target);
/* 293 */         if (method != null) {
/* 294 */           method = ClassUtils.getInterfaceMethodIfPossible(method);
/* 295 */           cachedMember = method;
/* 296 */           this.writerCache.put(cacheKey, cachedMember);
/*     */         } 
/*     */       } 
/* 299 */       if (method != null) {
/*     */         try {
/* 301 */           ReflectionUtils.makeAccessible(method);
/* 302 */           method.invoke(target, new Object[] { possiblyConvertedNewValue });
/*     */           
/*     */           return;
/* 305 */         } catch (Exception ex) {
/* 306 */           throw new AccessException("Unable to access property '" + name + "' through setter method", ex);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 311 */     if (cachedMember == null || cachedMember instanceof Field) {
/* 312 */       Field field = (Field)cachedMember;
/* 313 */       if (field == null) {
/* 314 */         field = findField(name, type, target);
/* 315 */         if (field != null) {
/* 316 */           cachedMember = field;
/* 317 */           this.writerCache.put(cacheKey, cachedMember);
/*     */         } 
/*     */       } 
/* 320 */       if (field != null) {
/*     */         try {
/* 322 */           ReflectionUtils.makeAccessible(field);
/* 323 */           field.set(target, possiblyConvertedNewValue);
/*     */           
/*     */           return;
/* 326 */         } catch (Exception ex) {
/* 327 */           throw new AccessException("Unable to access field '" + name + "'", ex);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 332 */     throw new AccessException("Neither setter method nor field found for property '" + name + "'");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @Nullable
/*     */   public Member getLastReadInvokerPair() {
/* 342 */     InvokerPair lastReadInvoker = this.lastReadInvokerPair;
/* 343 */     return (lastReadInvoker != null) ? lastReadInvoker.member : null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private TypeDescriptor getTypeDescriptor(EvaluationContext context, Object target, String name) {
/* 349 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/*     */     
/* 351 */     if (type.isArray() && name.equals("length")) {
/* 352 */       return TypeDescriptor.valueOf(int.class);
/*     */     }
/* 354 */     PropertyCacheKey cacheKey = new PropertyCacheKey(type, name, target instanceof Class);
/* 355 */     TypeDescriptor typeDescriptor = this.typeDescriptorCache.get(cacheKey);
/* 356 */     if (typeDescriptor == null) {
/*     */       
/*     */       try {
/* 359 */         if (canRead(context, target, name) || canWrite(context, target, name)) {
/* 360 */           typeDescriptor = this.typeDescriptorCache.get(cacheKey);
/*     */         }
/*     */       }
/* 363 */       catch (AccessException accessException) {}
/*     */     }
/*     */ 
/*     */     
/* 367 */     return typeDescriptor;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Method findGetterForProperty(String propertyName, Class<?> clazz, Object target) {
/* 372 */     Method method = findGetterForProperty(propertyName, clazz, target instanceof Class);
/* 373 */     if (method == null && target instanceof Class) {
/* 374 */       method = findGetterForProperty(propertyName, target.getClass(), false);
/*     */     }
/* 376 */     return method;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Method findSetterForProperty(String propertyName, Class<?> clazz, Object target) {
/* 381 */     Method method = findSetterForProperty(propertyName, clazz, target instanceof Class);
/* 382 */     if (method == null && target instanceof Class) {
/* 383 */       method = findSetterForProperty(propertyName, target.getClass(), false);
/*     */     }
/* 385 */     return method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Method findGetterForProperty(String propertyName, Class<?> clazz, boolean mustBeStatic) {
/* 393 */     Method method = findMethodForProperty(getPropertyMethodSuffixes(propertyName), "get", clazz, mustBeStatic, 0, ANY_TYPES);
/*     */     
/* 395 */     if (method == null) {
/* 396 */       method = findMethodForProperty(getPropertyMethodSuffixes(propertyName), "is", clazz, mustBeStatic, 0, BOOLEAN_TYPES);
/*     */       
/* 398 */       if (method == null)
/*     */       {
/* 400 */         method = findMethodForProperty(new String[] { propertyName }, "", clazz, mustBeStatic, 0, ANY_TYPES);
/*     */       }
/*     */     } 
/*     */     
/* 404 */     return method;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Method findSetterForProperty(String propertyName, Class<?> clazz, boolean mustBeStatic) {
/* 412 */     return findMethodForProperty(getPropertyMethodSuffixes(propertyName), "set", clazz, mustBeStatic, 1, ANY_TYPES);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Method findMethodForProperty(String[] methodSuffixes, String prefix, Class<?> clazz, boolean mustBeStatic, int numberOfParams, Set<Class<?>> requiredReturnTypes) {
/* 420 */     Method[] methods = getSortedMethods(clazz);
/* 421 */     for (String methodSuffix : methodSuffixes) {
/* 422 */       for (Method method : methods) {
/* 423 */         if (isCandidateForProperty(method, clazz) && method.getName().equals(prefix + methodSuffix) && method
/* 424 */           .getParameterCount() == numberOfParams && (!mustBeStatic || 
/* 425 */           Modifier.isStatic(method.getModifiers())) && (requiredReturnTypes
/* 426 */           .isEmpty() || requiredReturnTypes.contains(method.getReturnType()))) {
/* 427 */           return method;
/*     */         }
/*     */       } 
/*     */     } 
/* 431 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Method[] getSortedMethods(Class<?> clazz) {
/* 438 */     return this.sortedMethodsCache.computeIfAbsent(clazz, key -> {
/*     */           Method[] methods = key.getMethods();
/*     */           Arrays.sort(methods, ());
/*     */           return methods;
/*     */         });
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
/*     */   protected boolean isCandidateForProperty(Method method, Class<?> targetClass) {
/* 455 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String[] getPropertyMethodSuffixes(String propertyName) {
/* 465 */     String suffix = getPropertyMethodSuffix(propertyName);
/* 466 */     if (suffix.length() > 0 && Character.isUpperCase(suffix.charAt(0))) {
/* 467 */       return new String[] { suffix };
/*     */     }
/* 469 */     return new String[] { suffix, StringUtils.capitalize(suffix) };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getPropertyMethodSuffix(String propertyName) {
/* 477 */     if (propertyName.length() > 1 && Character.isUpperCase(propertyName.charAt(1))) {
/* 478 */       return propertyName;
/*     */     }
/* 480 */     return StringUtils.capitalize(propertyName);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Field findField(String name, Class<?> clazz, Object target) {
/* 485 */     Field field = findField(name, clazz, target instanceof Class);
/* 486 */     if (field == null && target instanceof Class) {
/* 487 */       field = findField(name, target.getClass(), false);
/*     */     }
/* 489 */     return field;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected Field findField(String name, Class<?> clazz, boolean mustBeStatic) {
/* 497 */     Field[] fields = clazz.getFields();
/* 498 */     for (Field field : fields) {
/* 499 */       if (field.getName().equals(name) && (!mustBeStatic || Modifier.isStatic(field.getModifiers()))) {
/* 500 */         return field;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 505 */     if (clazz.getSuperclass() != null) {
/* 506 */       Field field = findField(name, clazz.getSuperclass(), mustBeStatic);
/* 507 */       if (field != null) {
/* 508 */         return field;
/*     */       }
/*     */     } 
/* 511 */     for (Class<?> implementedInterface : clazz.getInterfaces()) {
/* 512 */       Field field = findField(name, implementedInterface, mustBeStatic);
/* 513 */       if (field != null) {
/* 514 */         return field;
/*     */       }
/*     */     } 
/* 517 */     return null;
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
/*     */   public PropertyAccessor createOptimalAccessor(EvaluationContext context, @Nullable Object target, String name) {
/* 533 */     if (target == null) {
/* 534 */       return this;
/*     */     }
/* 536 */     Class<?> clazz = (target instanceof Class) ? (Class)target : target.getClass();
/* 537 */     if (clazz.isArray()) {
/* 538 */       return this;
/*     */     }
/*     */     
/* 541 */     PropertyCacheKey cacheKey = new PropertyCacheKey(clazz, name, target instanceof Class);
/* 542 */     InvokerPair invocationTarget = this.readerCache.get(cacheKey);
/*     */     
/* 544 */     if (invocationTarget == null || invocationTarget.member instanceof Method) {
/* 545 */       Method method = (invocationTarget != null) ? (Method)invocationTarget.member : null;
/* 546 */       if (method == null) {
/* 547 */         method = findGetterForProperty(name, clazz, target);
/* 548 */         if (method != null) {
/* 549 */           TypeDescriptor typeDescriptor = new TypeDescriptor(new MethodParameter(method, -1));
/* 550 */           method = ClassUtils.getInterfaceMethodIfPossible(method);
/* 551 */           invocationTarget = new InvokerPair(method, typeDescriptor);
/* 552 */           ReflectionUtils.makeAccessible(method);
/* 553 */           this.readerCache.put(cacheKey, invocationTarget);
/*     */         } 
/*     */       } 
/* 556 */       if (method != null) {
/* 557 */         return (PropertyAccessor)new OptimalPropertyAccessor(invocationTarget);
/*     */       }
/*     */     } 
/*     */     
/* 561 */     if (invocationTarget == null || invocationTarget.member instanceof Field) {
/* 562 */       Field field = (invocationTarget != null) ? (Field)invocationTarget.member : null;
/* 563 */       if (field == null) {
/* 564 */         field = findField(name, clazz, target instanceof Class);
/* 565 */         if (field != null) {
/* 566 */           invocationTarget = new InvokerPair(field, new TypeDescriptor(field));
/* 567 */           ReflectionUtils.makeAccessible(field);
/* 568 */           this.readerCache.put(cacheKey, invocationTarget);
/*     */         } 
/*     */       } 
/* 571 */       if (field != null) {
/* 572 */         return (PropertyAccessor)new OptimalPropertyAccessor(invocationTarget);
/*     */       }
/*     */     } 
/*     */     
/* 576 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class InvokerPair
/*     */   {
/*     */     final Member member;
/*     */ 
/*     */     
/*     */     final TypeDescriptor typeDescriptor;
/*     */ 
/*     */ 
/*     */     
/*     */     public InvokerPair(Member member, TypeDescriptor typeDescriptor) {
/* 591 */       this.member = member;
/* 592 */       this.typeDescriptor = typeDescriptor;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class PropertyCacheKey
/*     */     implements Comparable<PropertyCacheKey>
/*     */   {
/*     */     private final Class<?> clazz;
/*     */     
/*     */     private final String property;
/*     */     private boolean targetIsClass;
/*     */     
/*     */     public PropertyCacheKey(Class<?> clazz, String name, boolean targetIsClass) {
/* 606 */       this.clazz = clazz;
/* 607 */       this.property = name;
/* 608 */       this.targetIsClass = targetIsClass;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object other) {
/* 613 */       if (this == other) {
/* 614 */         return true;
/*     */       }
/* 616 */       if (!(other instanceof PropertyCacheKey)) {
/* 617 */         return false;
/*     */       }
/* 619 */       PropertyCacheKey otherKey = (PropertyCacheKey)other;
/* 620 */       return (this.clazz == otherKey.clazz && this.property.equals(otherKey.property) && this.targetIsClass == otherKey.targetIsClass);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 626 */       return this.clazz.hashCode() * 29 + this.property.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 631 */       return "PropertyCacheKey [clazz=" + this.clazz.getName() + ", property=" + this.property + ", targetIsClass=" + this.targetIsClass + "]";
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int compareTo(PropertyCacheKey other) {
/* 637 */       int result = this.clazz.getName().compareTo(other.clazz.getName());
/* 638 */       if (result == 0) {
/* 639 */         result = this.property.compareTo(other.property);
/*     */       }
/* 641 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class OptimalPropertyAccessor
/*     */     implements CompilablePropertyAccessor
/*     */   {
/*     */     public final Member member;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final TypeDescriptor typeDescriptor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     OptimalPropertyAccessor(ReflectivePropertyAccessor.InvokerPair target) {
/* 664 */       this.member = target.member;
/* 665 */       this.typeDescriptor = target.typeDescriptor;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Class<?>[] getSpecificTargetClasses() {
/* 671 */       throw new UnsupportedOperationException("Should not be called on an OptimalPropertyAccessor");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canRead(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
/* 676 */       if (target == null) {
/* 677 */         return false;
/*     */       }
/* 679 */       Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/* 680 */       if (type.isArray()) {
/* 681 */         return false;
/*     */       }
/*     */       
/* 684 */       if (this.member instanceof Method) {
/* 685 */         Method method = (Method)this.member;
/* 686 */         String getterName = "get" + StringUtils.capitalize(name);
/* 687 */         if (getterName.equals(method.getName())) {
/* 688 */           return true;
/*     */         }
/* 690 */         getterName = "is" + StringUtils.capitalize(name);
/* 691 */         if (getterName.equals(method.getName())) {
/* 692 */           return true;
/*     */         }
/*     */       } 
/* 695 */       return this.member.getName().equals(name);
/*     */     }
/*     */ 
/*     */     
/*     */     public TypedValue read(EvaluationContext context, @Nullable Object target, String name) throws AccessException {
/* 700 */       if (this.member instanceof Method) {
/* 701 */         Method method = (Method)this.member;
/*     */         try {
/* 703 */           ReflectionUtils.makeAccessible(method);
/* 704 */           Object value = method.invoke(target, new Object[0]);
/* 705 */           return new TypedValue(value, this.typeDescriptor.narrow(value));
/*     */         }
/* 707 */         catch (Exception ex) {
/* 708 */           throw new AccessException("Unable to access property '" + name + "' through getter method", ex);
/*     */         } 
/*     */       } 
/*     */       
/* 712 */       Field field = (Field)this.member;
/*     */       try {
/* 714 */         ReflectionUtils.makeAccessible(field);
/* 715 */         Object value = field.get(target);
/* 716 */         return new TypedValue(value, this.typeDescriptor.narrow(value));
/*     */       }
/* 718 */       catch (Exception ex) {
/* 719 */         throw new AccessException("Unable to access field '" + name + "'", ex);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean canWrite(EvaluationContext context, @Nullable Object target, String name) {
/* 726 */       throw new UnsupportedOperationException("Should not be called on an OptimalPropertyAccessor");
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(EvaluationContext context, @Nullable Object target, String name, @Nullable Object newValue) {
/* 731 */       throw new UnsupportedOperationException("Should not be called on an OptimalPropertyAccessor");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isCompilable() {
/* 736 */       return (Modifier.isPublic(this.member.getModifiers()) && 
/* 737 */         Modifier.isPublic(this.member.getDeclaringClass().getModifiers()));
/*     */     }
/*     */ 
/*     */     
/*     */     public Class<?> getPropertyType() {
/* 742 */       if (this.member instanceof Method) {
/* 743 */         return ((Method)this.member).getReturnType();
/*     */       }
/*     */       
/* 746 */       return ((Field)this.member).getType();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void generateCode(String propertyName, MethodVisitor mv, CodeFlow cf) {
/* 752 */       boolean isStatic = Modifier.isStatic(this.member.getModifiers());
/* 753 */       String descriptor = cf.lastDescriptor();
/* 754 */       String classDesc = this.member.getDeclaringClass().getName().replace('.', '/');
/*     */       
/* 756 */       if (!isStatic) {
/* 757 */         if (descriptor == null) {
/* 758 */           cf.loadTarget(mv);
/*     */         }
/* 760 */         if (descriptor == null || !classDesc.equals(descriptor.substring(1))) {
/* 761 */           mv.visitTypeInsn(192, classDesc);
/*     */         
/*     */         }
/*     */       }
/* 765 */       else if (descriptor != null) {
/*     */ 
/*     */         
/* 768 */         mv.visitInsn(87);
/*     */       } 
/*     */ 
/*     */       
/* 772 */       if (this.member instanceof Method) {
/* 773 */         Method method = (Method)this.member;
/* 774 */         boolean isInterface = method.getDeclaringClass().isInterface();
/* 775 */         int opcode = isStatic ? 184 : (isInterface ? 185 : 182);
/* 776 */         mv.visitMethodInsn(opcode, classDesc, method.getName(), 
/* 777 */             CodeFlow.createSignatureDescriptor(method), isInterface);
/*     */       } else {
/*     */         
/* 780 */         mv.visitFieldInsn(isStatic ? 178 : 180, classDesc, this.member.getName(), 
/* 781 */             CodeFlow.toJvmDescriptor(((Field)this.member).getType()));
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\support\ReflectivePropertyAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */