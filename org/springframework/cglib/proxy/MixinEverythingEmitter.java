/*    */ package org.springframework.cglib.proxy;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import org.springframework.asm.ClassVisitor;
/*    */ import org.springframework.cglib.core.CollectionUtils;
/*    */ import org.springframework.cglib.core.Predicate;
/*    */ import org.springframework.cglib.core.ReflectUtils;
/*    */ import org.springframework.cglib.core.RejectModifierPredicate;
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
/*    */ class MixinEverythingEmitter
/*    */   extends MixinEmitter
/*    */ {
/*    */   public MixinEverythingEmitter(ClassVisitor v, String className, Class[] classes) {
/* 33 */     super(v, className, classes, null);
/*    */   }
/*    */   
/*    */   protected Class[] getInterfaces(Class[] classes) {
/* 37 */     List list = new ArrayList();
/* 38 */     for (int i = 0; i < classes.length; i++) {
/* 39 */       ReflectUtils.addAllInterfaces(classes[i], list);
/*    */     }
/* 41 */     return (Class[])list.toArray((Object[])new Class[list.size()]);
/*    */   }
/*    */   
/*    */   protected Method[] getMethods(Class type) {
/* 45 */     List methods = new ArrayList(Arrays.asList((Object[])type.getMethods()));
/* 46 */     CollectionUtils.filter(methods, (Predicate)new RejectModifierPredicate(24));
/* 47 */     return (Method[])methods.toArray((Object[])new Method[methods.size()]);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\proxy\MixinEverythingEmitter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */