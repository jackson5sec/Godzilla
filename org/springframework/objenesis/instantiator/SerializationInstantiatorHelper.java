/*    */ package org.springframework.objenesis.instantiator;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SerializationInstantiatorHelper
/*    */ {
/*    */   public static <T> Class<? super T> getNonSerializableSuperClass(Class<T> type) {
/* 39 */     Class<? super T> result = type;
/* 40 */     while (Serializable.class.isAssignableFrom(result)) {
/* 41 */       result = result.getSuperclass();
/* 42 */       if (result == null) {
/* 43 */         throw new Error("Bad class hierarchy: No non-serializable parents");
/*    */       }
/*    */     } 
/* 46 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\instantiator\SerializationInstantiatorHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */