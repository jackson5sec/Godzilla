/*    */ package org.springframework.objenesis.instantiator.android;
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
/*    */ @Instantiator(Typology.STANDARD)
/*    */ public class Android10Instantiator<T>
/*    */   implements ObjectInstantiator<T>
/*    */ {
/*    */   private final Class<T> type;
/*    */   private final Method newStaticMethod;
/*    */   
/*    */   public Android10Instantiator(Class<T> type) {
/* 39 */     this.type = type;
/* 40 */     this.newStaticMethod = getNewStaticMethod();
/*    */   }
/*    */   
/*    */   public T newInstance() {
/*    */     try {
/* 45 */       return this.type.cast(this.newStaticMethod.invoke((Object)null, new Object[] { this.type, Object.class }));
/*    */     }
/* 47 */     catch (Exception e) {
/* 48 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static Method getNewStaticMethod() {
/*    */     try {
/* 54 */       Method newStaticMethod = ObjectInputStream.class.getDeclaredMethod("newInstance", new Class[] { Class.class, Class.class });
/*    */       
/* 56 */       newStaticMethod.setAccessible(true);
/* 57 */       return newStaticMethod;
/*    */     }
/* 59 */     catch (RuntimeException|NoSuchMethodException e) {
/* 60 */       throw new ObjenesisException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\instantiator\android\Android10Instantiator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */