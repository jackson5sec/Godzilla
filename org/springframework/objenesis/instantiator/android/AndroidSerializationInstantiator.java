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
/*    */ @Instantiator(Typology.SERIALIZATION)
/*    */ public class AndroidSerializationInstantiator<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/*    */   private final Class<T> type;
/*    */   private final ObjectStreamClass objectStreamClass;
/*    */   private final Method newInstanceMethod;
/*    */   
/*    */   public AndroidSerializationInstantiator(Class<T> type) {
/*    */     Method m;
/* 41 */     this.type = type;
/* 42 */     this.newInstanceMethod = getNewInstanceMethod();
/*    */     
/*    */     try {
/* 45 */       m = ObjectStreamClass.class.getMethod("lookupAny", new Class[] { Class.class });
/* 46 */     } catch (NoSuchMethodException e) {
/* 47 */       throw new ObjenesisException(e);
/*    */     } 
/*    */     try {
/* 50 */       this.objectStreamClass = (ObjectStreamClass)m.invoke((Object)null, new Object[] { type });
/* 51 */     } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/* 52 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public T newInstance() {
/*    */     try {
/* 58 */       return this.type.cast(this.newInstanceMethod.invoke(this.objectStreamClass, new Object[] { this.type }));
/*    */     }
/* 60 */     catch (IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException e) {
/* 61 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static Method getNewInstanceMethod() {
/*    */     try {
/* 67 */       Method newInstanceMethod = ObjectStreamClass.class.getDeclaredMethod("newInstance", new Class[] { Class.class });
/*    */       
/* 69 */       newInstanceMethod.setAccessible(true);
/* 70 */       return newInstanceMethod;
/*    */     }
/* 72 */     catch (RuntimeException|NoSuchMethodException e) {
/* 73 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\instantiator\android\AndroidSerializationInstantiator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */