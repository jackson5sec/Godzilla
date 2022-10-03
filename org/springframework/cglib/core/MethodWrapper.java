/*    */ package org.springframework.cglib.core;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Collection;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
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
/*    */ public class MethodWrapper
/*    */ {
/* 23 */   private static final MethodWrapperKey KEY_FACTORY = (MethodWrapperKey)KeyFactory.create(MethodWrapperKey.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Object create(Method method) {
/* 34 */     return KEY_FACTORY.newInstance(method.getName(), 
/* 35 */         ReflectUtils.getNames(method.getParameterTypes()), method
/* 36 */         .getReturnType().getName());
/*    */   }
/*    */   
/*    */   public static Set createSet(Collection methods) {
/* 40 */     Set<Object> set = new HashSet();
/* 41 */     for (Iterator<Method> it = methods.iterator(); it.hasNext();) {
/* 42 */       set.add(create(it.next()));
/*    */     }
/* 44 */     return set;
/*    */   }
/*    */   
/*    */   public static interface MethodWrapperKey {
/*    */     Object newInstance(String param1String1, String[] param1ArrayOfString, String param1String2);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\core\MethodWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */