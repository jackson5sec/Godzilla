/*     */ package org.springframework.expression.spel.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.BeanResolver;
/*     */ import org.springframework.expression.ConstructorResolver;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.MethodFilter;
/*     */ import org.springframework.expression.MethodResolver;
/*     */ import org.springframework.expression.OperatorOverloader;
/*     */ import org.springframework.expression.PropertyAccessor;
/*     */ import org.springframework.expression.TypeComparator;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypeLocator;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StandardEvaluationContext
/*     */   implements EvaluationContext
/*     */ {
/*     */   private TypedValue rootObject;
/*     */   @Nullable
/*     */   private volatile List<PropertyAccessor> propertyAccessors;
/*     */   @Nullable
/*     */   private volatile List<ConstructorResolver> constructorResolvers;
/*     */   @Nullable
/*     */   private volatile List<MethodResolver> methodResolvers;
/*     */   @Nullable
/*     */   private volatile ReflectiveMethodResolver reflectiveMethodResolver;
/*     */   @Nullable
/*     */   private BeanResolver beanResolver;
/*     */   @Nullable
/*     */   private TypeLocator typeLocator;
/*     */   @Nullable
/*     */   private TypeConverter typeConverter;
/*  87 */   private TypeComparator typeComparator = new StandardTypeComparator();
/*     */   
/*  89 */   private OperatorOverloader operatorOverloader = new StandardOperatorOverloader();
/*     */   
/*  91 */   private final Map<String, Object> variables = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardEvaluationContext() {
/*  98 */     this.rootObject = TypedValue.NULL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public StandardEvaluationContext(@Nullable Object rootObject) {
/* 107 */     this.rootObject = new TypedValue(rootObject);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRootObject(@Nullable Object rootObject, TypeDescriptor typeDescriptor) {
/* 112 */     this.rootObject = new TypedValue(rootObject, typeDescriptor);
/*     */   }
/*     */   
/*     */   public void setRootObject(@Nullable Object rootObject) {
/* 116 */     this.rootObject = (rootObject != null) ? new TypedValue(rootObject) : TypedValue.NULL;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypedValue getRootObject() {
/* 121 */     return this.rootObject;
/*     */   }
/*     */   
/*     */   public void setPropertyAccessors(List<PropertyAccessor> propertyAccessors) {
/* 125 */     this.propertyAccessors = propertyAccessors;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<PropertyAccessor> getPropertyAccessors() {
/* 130 */     return initPropertyAccessors();
/*     */   }
/*     */   
/*     */   public void addPropertyAccessor(PropertyAccessor accessor) {
/* 134 */     addBeforeDefault(initPropertyAccessors(), accessor);
/*     */   }
/*     */   
/*     */   public boolean removePropertyAccessor(PropertyAccessor accessor) {
/* 138 */     return initPropertyAccessors().remove(accessor);
/*     */   }
/*     */   
/*     */   public void setConstructorResolvers(List<ConstructorResolver> constructorResolvers) {
/* 142 */     this.constructorResolvers = constructorResolvers;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<ConstructorResolver> getConstructorResolvers() {
/* 147 */     return initConstructorResolvers();
/*     */   }
/*     */   
/*     */   public void addConstructorResolver(ConstructorResolver resolver) {
/* 151 */     addBeforeDefault(initConstructorResolvers(), resolver);
/*     */   }
/*     */   
/*     */   public boolean removeConstructorResolver(ConstructorResolver resolver) {
/* 155 */     return initConstructorResolvers().remove(resolver);
/*     */   }
/*     */   
/*     */   public void setMethodResolvers(List<MethodResolver> methodResolvers) {
/* 159 */     this.methodResolvers = methodResolvers;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MethodResolver> getMethodResolvers() {
/* 164 */     return initMethodResolvers();
/*     */   }
/*     */   
/*     */   public void addMethodResolver(MethodResolver resolver) {
/* 168 */     addBeforeDefault(initMethodResolvers(), resolver);
/*     */   }
/*     */   
/*     */   public boolean removeMethodResolver(MethodResolver methodResolver) {
/* 172 */     return initMethodResolvers().remove(methodResolver);
/*     */   }
/*     */   
/*     */   public void setBeanResolver(BeanResolver beanResolver) {
/* 176 */     this.beanResolver = beanResolver;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BeanResolver getBeanResolver() {
/* 182 */     return this.beanResolver;
/*     */   }
/*     */   
/*     */   public void setTypeLocator(TypeLocator typeLocator) {
/* 186 */     Assert.notNull(typeLocator, "TypeLocator must not be null");
/* 187 */     this.typeLocator = typeLocator;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeLocator getTypeLocator() {
/* 192 */     if (this.typeLocator == null) {
/* 193 */       this.typeLocator = new StandardTypeLocator();
/*     */     }
/* 195 */     return this.typeLocator;
/*     */   }
/*     */   
/*     */   public void setTypeConverter(TypeConverter typeConverter) {
/* 199 */     Assert.notNull(typeConverter, "TypeConverter must not be null");
/* 200 */     this.typeConverter = typeConverter;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeConverter getTypeConverter() {
/* 205 */     if (this.typeConverter == null) {
/* 206 */       this.typeConverter = new StandardTypeConverter();
/*     */     }
/* 208 */     return this.typeConverter;
/*     */   }
/*     */   
/*     */   public void setTypeComparator(TypeComparator typeComparator) {
/* 212 */     Assert.notNull(typeComparator, "TypeComparator must not be null");
/* 213 */     this.typeComparator = typeComparator;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeComparator getTypeComparator() {
/* 218 */     return this.typeComparator;
/*     */   }
/*     */   
/*     */   public void setOperatorOverloader(OperatorOverloader operatorOverloader) {
/* 222 */     Assert.notNull(operatorOverloader, "OperatorOverloader must not be null");
/* 223 */     this.operatorOverloader = operatorOverloader;
/*     */   }
/*     */ 
/*     */   
/*     */   public OperatorOverloader getOperatorOverloader() {
/* 228 */     return this.operatorOverloader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVariable(@Nullable String name, @Nullable Object value) {
/* 236 */     if (name != null) {
/* 237 */       if (value != null) {
/* 238 */         this.variables.put(name, value);
/*     */       } else {
/*     */         
/* 241 */         this.variables.remove(name);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public void setVariables(Map<String, Object> variables) {
/* 247 */     variables.forEach(this::setVariable);
/*     */   }
/*     */   
/*     */   public void registerFunction(String name, Method method) {
/* 251 */     this.variables.put(name, method);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object lookupVariable(String name) {
/* 257 */     return this.variables.get(name);
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
/*     */   public void registerMethodFilter(Class<?> type, MethodFilter filter) throws IllegalStateException {
/* 270 */     initMethodResolvers();
/* 271 */     ReflectiveMethodResolver resolver = this.reflectiveMethodResolver;
/* 272 */     if (resolver == null) {
/* 273 */       throw new IllegalStateException("Method filter cannot be set as the reflective method resolver is not in use");
/*     */     }
/*     */     
/* 276 */     resolver.registerMethodFilter(type, filter);
/*     */   }
/*     */ 
/*     */   
/*     */   private List<PropertyAccessor> initPropertyAccessors() {
/* 281 */     List<PropertyAccessor> accessors = this.propertyAccessors;
/* 282 */     if (accessors == null) {
/* 283 */       accessors = new ArrayList<>(5);
/* 284 */       accessors.add(new ReflectivePropertyAccessor());
/* 285 */       this.propertyAccessors = accessors;
/*     */     } 
/* 287 */     return accessors;
/*     */   }
/*     */   
/*     */   private List<ConstructorResolver> initConstructorResolvers() {
/* 291 */     List<ConstructorResolver> resolvers = this.constructorResolvers;
/* 292 */     if (resolvers == null) {
/* 293 */       resolvers = new ArrayList<>(1);
/* 294 */       resolvers.add(new ReflectiveConstructorResolver());
/* 295 */       this.constructorResolvers = resolvers;
/*     */     } 
/* 297 */     return resolvers;
/*     */   }
/*     */   
/*     */   private List<MethodResolver> initMethodResolvers() {
/* 301 */     List<MethodResolver> resolvers = this.methodResolvers;
/* 302 */     if (resolvers == null) {
/* 303 */       resolvers = new ArrayList<>(1);
/* 304 */       this.reflectiveMethodResolver = new ReflectiveMethodResolver();
/* 305 */       resolvers.add(this.reflectiveMethodResolver);
/* 306 */       this.methodResolvers = resolvers;
/*     */     } 
/* 308 */     return resolvers;
/*     */   }
/*     */   
/*     */   private static <T> void addBeforeDefault(List<T> resolvers, T resolver) {
/* 312 */     resolvers.add(resolvers.size() - 1, resolver);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\support\StandardEvaluationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */