/*    */ package org.springframework.objenesis.instantiator.android;
/*    */ 
/*    */ import java.io.ObjectStreamClass;
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.objenesis.ObjenesisException;
/*    */ import org.springframework.objenesis.instantiator.ObjectInstantiator;
/*    */ import org.springframework.objenesis.instantiator.annotations.Instantiator;
/*    */ import org.springframework.objenesis.instantiator.annotations.Typology;
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
/*    */ @Instantiator(Typology.STANDARD)
/*    */ public class Android17Instantiator<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/*    */   private final Class<T> type;
/*    */   private final Method newInstanceMethod;
/*    */   private final Integer objectConstructorId;
/*    */   
/*    */   public Android17Instantiator(Class<T> type) {
/* 40 */     this.type = type;
/* 41 */     this.newInstanceMethod = getNewInstanceMethod();
/* 42 */     this.objectConstructorId = findConstructorIdForJavaLangObjectConstructor();
/*    */   }
/*    */   
/*    */   public T newInstance() {
/*    */     try {
/* 47 */       return this.type.cast(this.newInstanceMethod.invoke((Object)null, new Object[] { this.type, this.objectConstructorId }));
/*    */     }
/* 49 */     catch (Exception e) {
/* 50 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static Method getNewInstanceMethod() {
/*    */     try {
/* 56 */       Method newInstanceMethod = ObjectStreamClass.class.getDeclaredMethod("newInstance", new Class[] { Class.class, int.class });
/*    */       
/* 58 */       newInstanceMethod.setAccessible(true);
/* 59 */       return newInstanceMethod;
/*    */     }
/* 61 */     catch (RuntimeException|NoSuchMethodException e) {
/* 62 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static Integer findConstructorIdForJavaLangObjectConstructor() {
/*    */     try {
/* 68 */       Method newInstanceMethod = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", new Class[] { Class.class });
/*    */       
/* 70 */       newInstanceMethod.setAccessible(true);
/*    */       
/* 72 */       return (Integer)newInstanceMethod.invoke((Object)null, new Object[] { Object.class });
/*    */     }
/* 74 */     catch (RuntimeException|NoSuchMethodException|java.lang.reflect.InvocationTargetException|IllegalAccessException e) {
/* 75 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\instantiator\android\Android17Instantiator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */