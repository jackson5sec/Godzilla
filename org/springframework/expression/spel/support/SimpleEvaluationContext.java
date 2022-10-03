/*     */ package org.springframework.expression.spel.support;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.core.convert.ConversionService;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.BeanResolver;
/*     */ import org.springframework.expression.ConstructorResolver;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.MethodResolver;
/*     */ import org.springframework.expression.OperatorOverloader;
/*     */ import org.springframework.expression.PropertyAccessor;
/*     */ import org.springframework.expression.TypeComparator;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypeLocator;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
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
/*     */ public final class SimpleEvaluationContext
/*     */   implements EvaluationContext
/*     */ {
/*     */   private static final TypeLocator typeNotFoundTypeLocator;
/*     */   private final TypedValue rootObject;
/*     */   private final List<PropertyAccessor> propertyAccessors;
/*     */   private final List<MethodResolver> methodResolvers;
/*     */   private final TypeConverter typeConverter;
/*     */   
/*     */   static {
/*  91 */     typeNotFoundTypeLocator = (typeName -> {
/*     */         throw new SpelEvaluationException(SpelMessage.TYPE_NOT_FOUND, new Object[] { typeName });
/*     */       });
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
/* 104 */   private final TypeComparator typeComparator = new StandardTypeComparator();
/*     */   
/* 106 */   private final OperatorOverloader operatorOverloader = new StandardOperatorOverloader();
/*     */   
/* 108 */   private final Map<String, Object> variables = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SimpleEvaluationContext(List<PropertyAccessor> accessors, List<MethodResolver> resolvers, @Nullable TypeConverter converter, @Nullable TypedValue rootObject) {
/* 114 */     this.propertyAccessors = accessors;
/* 115 */     this.methodResolvers = resolvers;
/* 116 */     this.typeConverter = (converter != null) ? converter : new StandardTypeConverter();
/* 117 */     this.rootObject = (rootObject != null) ? rootObject : TypedValue.NULL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypedValue getRootObject() {
/* 126 */     return this.rootObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<PropertyAccessor> getPropertyAccessors() {
/* 135 */     return this.propertyAccessors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ConstructorResolver> getConstructorResolvers() {
/* 144 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<MethodResolver> getMethodResolvers() {
/* 153 */     return this.methodResolvers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BeanResolver getBeanResolver() {
/* 163 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeLocator getTypeLocator() {
/* 173 */     return typeNotFoundTypeLocator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeConverter getTypeConverter() {
/* 184 */     return this.typeConverter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeComparator getTypeComparator() {
/* 192 */     return this.typeComparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OperatorOverloader getOperatorOverloader() {
/* 200 */     return this.operatorOverloader;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVariable(String name, @Nullable Object value) {
/* 205 */     this.variables.put(name, value);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Object lookupVariable(String name) {
/* 211 */     return this.variables.get(name);
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
/*     */   public static Builder forPropertyAccessors(PropertyAccessor... accessors) {
/* 225 */     for (PropertyAccessor accessor : accessors) {
/* 226 */       if (accessor.getClass() == ReflectivePropertyAccessor.class) {
/* 227 */         throw new IllegalArgumentException("SimpleEvaluationContext is not designed for use with a plain ReflectivePropertyAccessor. Consider using DataBindingPropertyAccessor or a custom subclass.");
/*     */       }
/*     */     } 
/*     */     
/* 231 */     return new Builder(accessors);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder forReadOnlyDataBinding() {
/* 241 */     return new Builder(new PropertyAccessor[] { DataBindingPropertyAccessor.forReadOnlyAccess() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Builder forReadWriteDataBinding() {
/* 251 */     return new Builder(new PropertyAccessor[] { DataBindingPropertyAccessor.forReadWriteAccess() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private final List<PropertyAccessor> accessors;
/*     */ 
/*     */     
/* 262 */     private List<MethodResolver> resolvers = Collections.emptyList();
/*     */     
/*     */     @Nullable
/*     */     private TypeConverter typeConverter;
/*     */     
/*     */     @Nullable
/*     */     private TypedValue rootObject;
/*     */     
/*     */     public Builder(PropertyAccessor... accessors) {
/* 271 */       this.accessors = Arrays.asList(accessors);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withMethodResolvers(MethodResolver... resolvers) {
/* 282 */       for (MethodResolver resolver : resolvers) {
/* 283 */         if (resolver.getClass() == ReflectiveMethodResolver.class) {
/* 284 */           throw new IllegalArgumentException("SimpleEvaluationContext is not designed for use with a plain ReflectiveMethodResolver. Consider using DataBindingMethodResolver or a custom subclass.");
/*     */         }
/*     */       } 
/*     */       
/* 288 */       this.resolvers = Arrays.asList(resolvers);
/* 289 */       return this;
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
/*     */     public Builder withInstanceMethods() {
/* 301 */       this.resolvers = Collections.singletonList(DataBindingMethodResolver.forInstanceMethodInvocation());
/* 302 */       return this;
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
/*     */     public Builder withConversionService(ConversionService conversionService) {
/* 314 */       this.typeConverter = new StandardTypeConverter(conversionService);
/* 315 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withTypeConverter(TypeConverter converter) {
/* 325 */       this.typeConverter = converter;
/* 326 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withRootObject(Object rootObject) {
/* 336 */       this.rootObject = new TypedValue(rootObject);
/* 337 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder withTypedRootObject(Object rootObject, TypeDescriptor typeDescriptor) {
/* 347 */       this.rootObject = new TypedValue(rootObject, typeDescriptor);
/* 348 */       return this;
/*     */     }
/*     */     
/*     */     public SimpleEvaluationContext build() {
/* 352 */       return new SimpleEvaluationContext(this.accessors, this.resolvers, this.typeConverter, this.rootObject);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\support\SimpleEvaluationContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */