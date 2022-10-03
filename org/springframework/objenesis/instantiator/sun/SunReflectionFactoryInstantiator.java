/*    */ package org.springframework.objenesis.instantiator.sun;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
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
/*    */ @Instantiator(Typology.STANDARD)
/*    */ public class SunReflectionFactoryInstantiator<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/*    */   private final Constructor<T> mungedConstructor;
/*    */   
/*    */   public SunReflectionFactoryInstantiator(Class<T> type) {
/* 40 */     Constructor<Object> javaLangObjectConstructor = getJavaLangObjectConstructor();
/* 41 */     this.mungedConstructor = SunReflectionFactoryHelper.newConstructorForSerialization(type, javaLangObjectConstructor);
/*    */     
/* 43 */     this.mungedConstructor.setAccessible(true);
/*    */   }
/*    */   
/*    */   public T newInstance() {
/*    */     try {
/* 48 */       return this.mungedConstructor.newInstance((Object[])null);
/*    */     }
/* 50 */     catch (Exception e) {
/* 51 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static Constructor<Object> getJavaLangObjectConstructor() {
/*    */     try {
/* 57 */       return Object.class.getConstructor((Class[])null);
/*    */     }
/* 59 */     catch (NoSuchMethodException e) {
/* 60 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\instantiator\sun\SunReflectionFactoryInstantiator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */