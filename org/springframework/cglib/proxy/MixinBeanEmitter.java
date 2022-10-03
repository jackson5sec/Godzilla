/*    */ package org.springframework.cglib.proxy;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.asm.ClassVisitor;
/*    */ import org.springframework.cglib.core.ReflectUtils;
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
/*    */ class MixinBeanEmitter
/*    */   extends MixinEmitter
/*    */ {
/*    */   public MixinBeanEmitter(ClassVisitor v, String className, Class[] classes) {
/* 28 */     super(v, className, classes, null);
/*    */   }
/*    */   
/*    */   protected Class[] getInterfaces(Class[] classes) {
/* 32 */     return null;
/*    */   }
/*    */   
/*    */   protected Method[] getMethods(Class type) {
/* 36 */     return ReflectUtils.getPropertyMethods(ReflectUtils.getBeanProperties(type), true, true);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\proxy\MixinBeanEmitter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */