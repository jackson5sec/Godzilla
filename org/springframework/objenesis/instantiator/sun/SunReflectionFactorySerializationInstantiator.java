/*    */ package org.springframework.objenesis.instantiator.sun;
/*    */ 
/*    */ import java.io.NotSerializableException;
/*    */ import java.lang.reflect.Constructor;
/*    */ import org.springframework.objenesis.ObjenesisException;
/*    */ import org.springframework.objenesis.instantiator.ObjectInstantiator;
/*    */ import org.springframework.objenesis.instantiator.SerializationInstantiatorHelper;
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
/*    */ 
/*    */ @Instantiator(Typology.SERIALIZATION)
/*    */ public class SunReflectionFactorySerializationInstantiator<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/*    */   private final Constructor<T> mungedConstructor;
/*    */   
/*    */   public SunReflectionFactorySerializationInstantiator(Class<T> type) {
/*    */     Constructor<? super T> nonSerializableAncestorConstructor;
/* 44 */     Class<? super T> nonSerializableAncestor = SerializationInstantiatorHelper.getNonSerializableSuperClass(type);
/*    */ 
/*    */ 
/*    */     
/*    */     try {
/* 49 */       nonSerializableAncestorConstructor = nonSerializableAncestor.getDeclaredConstructor((Class[])null);
/*    */     }
/* 51 */     catch (NoSuchMethodException e) {
/* 52 */       throw new ObjenesisException(new NotSerializableException(type + " has no suitable superclass constructor"));
/*    */     } 
/*    */     
/* 55 */     this.mungedConstructor = SunReflectionFactoryHelper.newConstructorForSerialization(type, nonSerializableAncestorConstructor);
/*    */     
/* 57 */     this.mungedConstructor.setAccessible(true);
/*    */   }
/*    */   
/*    */   public T newInstance() {
/*    */     try {
/* 62 */       return this.mungedConstructor.newInstance((Object[])null);
/*    */     }
/* 64 */     catch (Exception e) {
/* 65 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\instantiator\sun\SunReflectionFactorySerializationInstantiator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */