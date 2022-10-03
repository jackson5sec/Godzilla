/*     */ package org.springframework.expression.spel.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.MethodExecutor;
/*     */ import org.springframework.expression.MethodFilter;
/*     */ import org.springframework.expression.MethodResolver;
/*     */ import org.springframework.expression.TypeConverter;
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
/*     */ public class ReflectiveMethodResolver
/*     */   implements MethodResolver
/*     */ {
/*     */   private final boolean useDistance;
/*     */   @Nullable
/*     */   private Map<Class<?>, MethodFilter> filters;
/*     */   
/*     */   public ReflectiveMethodResolver() {
/*  65 */     this.useDistance = true;
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
/*     */   public ReflectiveMethodResolver(boolean useDistance) {
/*  79 */     this.useDistance = useDistance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void registerMethodFilter(Class<?> type, @Nullable MethodFilter filter) {
/*  90 */     if (this.filters == null) {
/*  91 */       this.filters = new HashMap<>();
/*     */     }
/*  93 */     if (filter != null) {
/*  94 */       this.filters.put(type, filter);
/*     */     } else {
/*     */       
/*  97 */       this.filters.remove(type);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public MethodExecutor resolve(EvaluationContext context, Object targetObject, String name, List<TypeDescriptor> argumentTypes) throws AccessException {
/*     */     try {
/* 116 */       TypeConverter typeConverter = context.getTypeConverter();
/* 117 */       Class<?> type = (targetObject instanceof Class) ? (Class)targetObject : targetObject.getClass();
/* 118 */       ArrayList<Method> methods = new ArrayList<>(getMethods(type, targetObject));
/*     */ 
/*     */       
/* 121 */       MethodFilter filter = (this.filters != null) ? this.filters.get(type) : null;
/* 122 */       if (filter != null) {
/* 123 */         List<Method> filtered = filter.filter(methods);
/* 124 */         methods = (filtered instanceof ArrayList) ? (ArrayList<Method>)filtered : new ArrayList<>(filtered);
/*     */       } 
/*     */ 
/*     */       
/* 128 */       if (methods.size() > 1) {
/* 129 */         methods.sort((m1, m2) -> {
/*     */               int m1pl = m1.getParameterCount();
/*     */               
/*     */               int m2pl = m2.getParameterCount();
/*     */               
/* 134 */               return (m1pl == m2pl) ? ((!m1.isVarArgs() && m2.isVarArgs()) ? -1 : (
/*     */ 
/*     */                 
/* 137 */                 (m1.isVarArgs() && !m2.isVarArgs()) ? 1 : 0)) : Integer.compare(m1pl, m2pl);
/*     */             });
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 149 */       for (int i = 0; i < methods.size(); i++) {
/* 150 */         methods.set(i, BridgeMethodResolver.findBridgedMethod(methods.get(i)));
/*     */       }
/*     */ 
/*     */       
/* 154 */       Set<Method> methodsToIterate = new LinkedHashSet<>(methods);
/*     */       
/* 156 */       Method closeMatch = null;
/* 157 */       int closeMatchDistance = Integer.MAX_VALUE;
/* 158 */       Method matchRequiringConversion = null;
/* 159 */       boolean multipleOptions = false;
/*     */       
/* 161 */       for (Method method : methodsToIterate) {
/* 162 */         if (method.getName().equals(name)) {
/* 163 */           int paramCount = method.getParameterCount();
/* 164 */           List<TypeDescriptor> paramDescriptors = new ArrayList<>(paramCount);
/* 165 */           for (int j = 0; j < paramCount; j++) {
/* 166 */             paramDescriptors.add(new TypeDescriptor(new MethodParameter(method, j)));
/*     */           }
/* 168 */           ReflectionHelper.ArgumentsMatchInfo matchInfo = null;
/* 169 */           if (method.isVarArgs() && argumentTypes.size() >= paramCount - 1) {
/*     */             
/* 171 */             matchInfo = ReflectionHelper.compareArgumentsVarargs(paramDescriptors, argumentTypes, typeConverter);
/*     */           }
/* 173 */           else if (paramCount == argumentTypes.size()) {
/*     */             
/* 175 */             matchInfo = ReflectionHelper.compareArguments(paramDescriptors, argumentTypes, typeConverter);
/*     */           } 
/* 177 */           if (matchInfo != null) {
/* 178 */             if (matchInfo.isExactMatch()) {
/* 179 */               return new ReflectiveMethodExecutor(method);
/*     */             }
/* 181 */             if (matchInfo.isCloseMatch()) {
/* 182 */               if (this.useDistance) {
/* 183 */                 int matchDistance = ReflectionHelper.getTypeDifferenceWeight(paramDescriptors, argumentTypes);
/* 184 */                 if (closeMatch == null || matchDistance < closeMatchDistance) {
/*     */                   
/* 186 */                   closeMatch = method;
/* 187 */                   closeMatchDistance = matchDistance;
/*     */                 } 
/*     */                 
/*     */                 continue;
/*     */               } 
/* 192 */               if (closeMatch == null) {
/* 193 */                 closeMatch = method;
/*     */               }
/*     */               continue;
/*     */             } 
/* 197 */             if (matchInfo.isMatchRequiringConversion()) {
/* 198 */               if (matchRequiringConversion != null) {
/* 199 */                 multipleOptions = true;
/*     */               }
/* 201 */               matchRequiringConversion = method;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 206 */       if (closeMatch != null) {
/* 207 */         return new ReflectiveMethodExecutor(closeMatch);
/*     */       }
/* 209 */       if (matchRequiringConversion != null) {
/* 210 */         if (multipleOptions) {
/* 211 */           throw new SpelEvaluationException(SpelMessage.MULTIPLE_POSSIBLE_METHODS, new Object[] { name });
/*     */         }
/* 213 */         return new ReflectiveMethodExecutor(matchRequiringConversion);
/*     */       } 
/*     */       
/* 216 */       return null;
/*     */     
/*     */     }
/* 219 */     catch (EvaluationException ex) {
/* 220 */       throw new AccessException("Failed to resolve method", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private Set<Method> getMethods(Class<?> type, Object targetObject) {
/* 225 */     if (targetObject instanceof Class) {
/* 226 */       Set<Method> set = new LinkedHashSet<>();
/*     */       
/* 228 */       Method[] arrayOfMethod = getMethods(type);
/* 229 */       for (Method method : arrayOfMethod) {
/* 230 */         if (Modifier.isStatic(method.getModifiers())) {
/* 231 */           set.add(method);
/*     */         }
/*     */       } 
/*     */       
/* 235 */       Collections.addAll(set, getMethods(Class.class));
/* 236 */       return set;
/*     */     } 
/* 238 */     if (Proxy.isProxyClass(type)) {
/* 239 */       Set<Method> set = new LinkedHashSet<>();
/*     */       
/* 241 */       for (Class<?> ifc : type.getInterfaces()) {
/* 242 */         Method[] arrayOfMethod = getMethods(ifc);
/* 243 */         for (Method method : arrayOfMethod) {
/* 244 */           if (isCandidateForInvocation(method, type)) {
/* 245 */             set.add(method);
/*     */           }
/*     */         } 
/*     */       } 
/* 249 */       return set;
/*     */     } 
/*     */     
/* 252 */     Set<Method> result = new LinkedHashSet<>();
/* 253 */     Method[] methods = getMethods(type);
/* 254 */     for (Method method : methods) {
/* 255 */       if (isCandidateForInvocation(method, type)) {
/* 256 */         result.add(method);
/*     */       }
/*     */     } 
/* 259 */     return result;
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
/*     */   protected Method[] getMethods(Class<?> type) {
/* 272 */     return type.getMethods();
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
/*     */   protected boolean isCandidateForInvocation(Method method, Class<?> targetClass) {
/* 285 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\support\ReflectiveMethodResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */