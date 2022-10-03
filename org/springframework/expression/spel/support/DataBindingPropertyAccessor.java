/*    */ package org.springframework.expression.spel.support;
/*    */ 
/*    */ import java.lang.reflect.Method;
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
/*    */ public final class DataBindingPropertyAccessor
/*    */   extends ReflectivePropertyAccessor
/*    */ {
/*    */   private DataBindingPropertyAccessor(boolean allowWrite) {
/* 48 */     super(allowWrite);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isCandidateForProperty(Method method, Class<?> targetClass) {
/* 53 */     Class<?> clazz = method.getDeclaringClass();
/* 54 */     return (clazz != Object.class && clazz != Class.class && !ClassLoader.class.isAssignableFrom(targetClass));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static DataBindingPropertyAccessor forReadOnlyAccess() {
/* 62 */     return new DataBindingPropertyAccessor(false);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static DataBindingPropertyAccessor forReadWriteAccess() {
/* 69 */     return new DataBindingPropertyAccessor(true);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\expression\spel\support\DataBindingPropertyAccessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */