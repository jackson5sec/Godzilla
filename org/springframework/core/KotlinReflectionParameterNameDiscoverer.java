/*    */ package org.springframework.core;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import kotlin.reflect.KFunction;
/*    */ import kotlin.reflect.KParameter;
/*    */ import kotlin.reflect.jvm.ReflectJvmMapping;
/*    */ import org.springframework.lang.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class KotlinReflectionParameterNameDiscoverer
/*    */   implements ParameterNameDiscoverer
/*    */ {
/*    */   @Nullable
/*    */   public String[] getParameterNames(Method method) {
/* 45 */     if (!KotlinDetector.isKotlinType(method.getDeclaringClass())) {
/* 46 */       return null;
/*    */     }
/*    */     
/*    */     try {
/* 50 */       KFunction<?> function = ReflectJvmMapping.getKotlinFunction(method);
/* 51 */       return (function != null) ? getParameterNames(function.getParameters()) : null;
/*    */     }
/* 53 */     catch (UnsupportedOperationException ex) {
/* 54 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String[] getParameterNames(Constructor<?> ctor) {
/* 61 */     if (ctor.getDeclaringClass().isEnum() || !KotlinDetector.isKotlinType(ctor.getDeclaringClass())) {
/* 62 */       return null;
/*    */     }
/*    */     
/*    */     try {
/* 66 */       KFunction<?> function = ReflectJvmMapping.getKotlinFunction(ctor);
/* 67 */       return (function != null) ? getParameterNames(function.getParameters()) : null;
/*    */     }
/* 69 */     catch (UnsupportedOperationException ex) {
/* 70 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   private String[] getParameterNames(List<KParameter> parameters) {
/* 80 */     List<KParameter> filteredParameters = (List<KParameter>)parameters.stream().filter(p -> (KParameter.Kind.VALUE.equals(p.getKind()) || KParameter.Kind.EXTENSION_RECEIVER.equals(p.getKind()))).collect(Collectors.toList());
/* 81 */     String[] parameterNames = new String[filteredParameters.size()];
/* 82 */     for (int i = 0; i < filteredParameters.size(); i++) {
/* 83 */       KParameter parameter = filteredParameters.get(i);
/*    */ 
/*    */       
/* 86 */       String name = KParameter.Kind.EXTENSION_RECEIVER.equals(parameter.getKind()) ? "$receiver" : parameter.getName();
/* 87 */       if (name == null) {
/* 88 */         return null;
/*    */       }
/* 90 */       parameterNames[i] = name;
/*    */     } 
/* 92 */     return parameterNames;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\KotlinReflectionParameterNameDiscoverer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */