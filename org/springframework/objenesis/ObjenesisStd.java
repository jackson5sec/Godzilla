/*    */ package org.springframework.objenesis;
/*    */ 
/*    */ import org.springframework.objenesis.strategy.InstantiatorStrategy;
/*    */ import org.springframework.objenesis.strategy.StdInstantiatorStrategy;
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
/*    */ public class ObjenesisStd
/*    */   extends ObjenesisBase
/*    */ {
/*    */   public ObjenesisStd() {
/* 31 */     super((InstantiatorStrategy)new StdInstantiatorStrategy());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ObjenesisStd(boolean useCache) {
/* 41 */     super((InstantiatorStrategy)new StdInstantiatorStrategy(), useCache);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\ObjenesisStd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */