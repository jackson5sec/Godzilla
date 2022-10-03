/*    */ package org.springframework.objenesis.instantiator.perc;
/*    */ 
/*    */ import java.io.ObjectInputStream;
/*    */ import java.lang.reflect.Method;
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
/*    */ public class PercInstantiator<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/*    */   private final Method newInstanceMethod;
/* 39 */   private final Object[] typeArgs = new Object[] { null, Boolean.FALSE };
/*    */ 
/*    */   
/*    */   public PercInstantiator(Class<T> type) {
/* 43 */     this.typeArgs[0] = type;
/*    */     
/*    */     try {
/* 46 */       this.newInstanceMethod = ObjectInputStream.class.getDeclaredMethod("newInstance", new Class[] { Class.class, boolean.class });
/*    */       
/* 48 */       this.newInstanceMethod.setAccessible(true);
/*    */     }
/* 50 */     catch (RuntimeException|NoSuchMethodException e) {
/* 51 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public T newInstance() {
/*    */     try {
/* 58 */       return (T)this.newInstanceMethod.invoke((Object)null, this.typeArgs);
/* 59 */     } catch (Exception e) {
/* 60 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\instantiator\perc\PercInstantiator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */