/*    */ package org.springframework.objenesis.instantiator.basic;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @Instantiator(Typology.SERIALIZATION)
/*    */ public class ObjectStreamClassInstantiator<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/*    */   private static Method newInstanceMethod;
/*    */   private final ObjectStreamClass objStreamClass;
/*    */   
/*    */   private static void initialize() {
/* 42 */     if (newInstanceMethod == null) {
/*    */       try {
/* 44 */         newInstanceMethod = ObjectStreamClass.class.getDeclaredMethod("newInstance", new Class[0]);
/* 45 */         newInstanceMethod.setAccessible(true);
/*    */       }
/* 47 */       catch (RuntimeException|NoSuchMethodException e) {
/* 48 */         throw new ObjenesisException(e);
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ObjectStreamClassInstantiator(Class<T> type) {
/* 56 */     initialize();
/* 57 */     this.objStreamClass = ObjectStreamClass.lookup(type);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public T newInstance() {
/*    */     try {
/* 64 */       return (T)newInstanceMethod.invoke(this.objStreamClass, new Object[0]);
/*    */     }
/* 66 */     catch (Exception e) {
/* 67 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\instantiator\basic\ObjectStreamClassInstantiator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */