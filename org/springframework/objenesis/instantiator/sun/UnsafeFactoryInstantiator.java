/*    */ package org.springframework.objenesis.instantiator.sun;
/*    */ 
/*    */ import org.springframework.objenesis.ObjenesisException;
/*    */ import org.springframework.objenesis.instantiator.ObjectInstantiator;
/*    */ import org.springframework.objenesis.instantiator.annotations.Instantiator;
/*    */ import org.springframework.objenesis.instantiator.annotations.Typology;
/*    */ import org.springframework.objenesis.instantiator.util.UnsafeUtils;
/*    */ import sun.misc.Unsafe;
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
/*    */ public class UnsafeFactoryInstantiator<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/*    */   private final Unsafe unsafe;
/*    */   private final Class<T> type;
/*    */   
/*    */   public UnsafeFactoryInstantiator(Class<T> type) {
/* 41 */     this.unsafe = UnsafeUtils.getUnsafe();
/* 42 */     this.type = type;
/*    */   }
/*    */   
/*    */   public T newInstance() {
/*    */     try {
/* 47 */       return this.type.cast(this.unsafe.allocateInstance(this.type));
/* 48 */     } catch (InstantiationException e) {
/* 49 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\instantiator\sun\UnsafeFactoryInstantiator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */