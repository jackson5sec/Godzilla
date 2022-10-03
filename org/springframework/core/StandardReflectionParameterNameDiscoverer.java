/*    */ package org.springframework.core;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Parameter;
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
/*    */ public class StandardReflectionParameterNameDiscoverer
/*    */   implements ParameterNameDiscoverer
/*    */ {
/*    */   @Nullable
/*    */   public String[] getParameterNames(Method method) {
/* 39 */     return getParameterNames(method.getParameters());
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String[] getParameterNames(Constructor<?> ctor) {
/* 45 */     return getParameterNames(ctor.getParameters());
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   private String[] getParameterNames(Parameter[] parameters) {
/* 50 */     String[] parameterNames = new String[parameters.length];
/* 51 */     for (int i = 0; i < parameters.length; i++) {
/* 52 */       Parameter param = parameters[i];
/* 53 */       if (!param.isNamePresent()) {
/* 54 */         return null;
/*    */       }
/* 56 */       parameterNames[i] = param.getName();
/*    */     } 
/* 58 */     return parameterNames;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\StandardReflectionParameterNameDiscoverer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */