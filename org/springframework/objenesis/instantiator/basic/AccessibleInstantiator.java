/*    */ package org.springframework.objenesis.instantiator.basic;
/*    */ 
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
/*    */ @Instantiator(Typology.NOT_COMPLIANT)
/*    */ public class AccessibleInstantiator<T>
/*    */   extends ConstructorInstantiator<T>
/*    */ {
/*    */   public AccessibleInstantiator(Class<T> type) {
/* 33 */     super(type);
/* 34 */     if (this.constructor != null)
/* 35 */       this.constructor.setAccessible(true); 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\instantiator\basic\AccessibleInstantiator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */