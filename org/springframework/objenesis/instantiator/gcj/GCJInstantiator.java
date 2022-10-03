/*    */ package org.springframework.objenesis.instantiator.gcj;
/*    */ 
/*    */ import org.springframework.objenesis.ObjenesisException;
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
/*    */ @Instantiator(Typology.STANDARD)
/*    */ public class GCJInstantiator<T>
/*    */   extends GCJInstantiatorBase<T>
/*    */ {
/*    */   public GCJInstantiator(Class<T> type) {
/* 34 */     super(type);
/*    */   }
/*    */ 
/*    */   
/*    */   public T newInstance() {
/*    */     try {
/* 40 */       return this.type.cast(newObjectMethod.invoke(dummyStream, new Object[] { this.type, Object.class }));
/*    */     }
/* 42 */     catch (RuntimeException|IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/* 43 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\instantiator\gcj\GCJInstantiator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */