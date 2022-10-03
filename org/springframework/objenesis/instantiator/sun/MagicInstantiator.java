/*    */ package org.springframework.objenesis.instantiator.sun;
/*    */ 
/*    */ import org.springframework.objenesis.instantiator.annotations.Instantiator;
/*    */ import org.springframework.objenesis.instantiator.annotations.Typology;
/*    */ import org.springframework.objenesis.instantiator.basic.DelegatingToExoticInstantiator;
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
/*    */ public class MagicInstantiator<T>
/*    */   extends DelegatingToExoticInstantiator<T>
/*    */ {
/*    */   public MagicInstantiator(Class<T> type) {
/* 31 */     super("org.springframework.objenesis.instantiator.exotic.MagicInstantiator", type);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\instantiator\sun\MagicInstantiator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */