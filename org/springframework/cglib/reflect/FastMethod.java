/*    */ package org.springframework.cglib.reflect;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.asm.Type;
/*    */ import org.springframework.cglib.core.Signature;
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
/*    */ public class FastMethod
/*    */   extends FastMember
/*    */ {
/*    */   FastMethod(FastClass fc, Method method) {
/* 28 */     super(fc, method, helper(fc, method));
/*    */   }
/*    */   
/*    */   private static int helper(FastClass fc, Method method) {
/* 32 */     int index = fc.getIndex(new Signature(method.getName(), Type.getMethodDescriptor(method)));
/* 33 */     if (index < 0) {
/* 34 */       Class[] types = method.getParameterTypes();
/* 35 */       System.err.println("hash=" + method.getName().hashCode() + " size=" + types.length);
/* 36 */       for (int i = 0; i < types.length; i++) {
/* 37 */         System.err.println("  types[" + i + "]=" + types[i].getName());
/*    */       }
/* 39 */       throw new IllegalArgumentException("Cannot find method " + method);
/*    */     } 
/* 41 */     return index;
/*    */   }
/*    */   
/*    */   public Class getReturnType() {
/* 45 */     return ((Method)this.member).getReturnType();
/*    */   }
/*    */   
/*    */   public Class[] getParameterTypes() {
/* 49 */     return ((Method)this.member).getParameterTypes();
/*    */   }
/*    */   
/*    */   public Class[] getExceptionTypes() {
/* 53 */     return ((Method)this.member).getExceptionTypes();
/*    */   }
/*    */   
/*    */   public Object invoke(Object obj, Object[] args) throws InvocationTargetException {
/* 57 */     return this.fc.invoke(this.index, obj, args);
/*    */   }
/*    */   
/*    */   public Method getJavaMethod() {
/* 61 */     return (Method)this.member;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\reflect\FastMethod.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */