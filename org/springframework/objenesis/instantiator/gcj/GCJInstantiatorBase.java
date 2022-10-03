/*    */ package org.springframework.objenesis.instantiator.gcj;
/*    */ 
/*    */ import java.io.ObjectInputStream;
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.objenesis.ObjenesisException;
/*    */ import org.springframework.objenesis.instantiator.ObjectInstantiator;
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
/*    */ public abstract class GCJInstantiatorBase<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/* 33 */   static Method newObjectMethod = null;
/*    */   
/*    */   static ObjectInputStream dummyStream;
/*    */   protected final Class<T> type;
/*    */   
/*    */   private static class DummyStream
/*    */     extends ObjectInputStream {}
/*    */   
/*    */   private static void initialize() {
/* 42 */     if (newObjectMethod == null) {
/*    */       try {
/* 44 */         newObjectMethod = ObjectInputStream.class.getDeclaredMethod("newObject", new Class[] { Class.class, Class.class });
/* 45 */         newObjectMethod.setAccessible(true);
/* 46 */         dummyStream = new DummyStream();
/*    */       }
/* 48 */       catch (RuntimeException|NoSuchMethodException|java.io.IOException e) {
/* 49 */         throw new ObjenesisException(e);
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public GCJInstantiatorBase(Class<T> type) {
/* 57 */     this.type = type;
/* 58 */     initialize();
/*    */   }
/*    */   
/*    */   public abstract T newInstance();
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\instantiator\gcj\GCJInstantiatorBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */