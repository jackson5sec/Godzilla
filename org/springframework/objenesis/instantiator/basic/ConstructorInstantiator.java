/*    */ package org.springframework.objenesis.instantiator.basic;
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
/*    */ @Instantiator(Typology.NOT_COMPLIANT)
/*    */ public class ConstructorInstantiator<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/*    */   protected Constructor<T> constructor;
/*    */   
/*    */   public ConstructorInstantiator(Class<T> type) {
/*    */     try {
/* 40 */       this.constructor = type.getDeclaredConstructor((Class[])null);
/*    */     }
/* 42 */     catch (Exception e) {
/* 43 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public T newInstance() {
/*    */     try {
/* 49 */       return this.constructor.newInstance((Object[])null);
/*    */     }
/* 51 */     catch (Exception e) {
/* 52 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\instantiator\basic\ConstructorInstantiator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */