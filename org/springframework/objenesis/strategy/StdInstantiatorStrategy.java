/*    */ package org.springframework.objenesis.strategy;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.springframework.objenesis.instantiator.ObjectInstantiator;
/*    */ import org.springframework.objenesis.instantiator.android.Android10Instantiator;
/*    */ import org.springframework.objenesis.instantiator.android.Android17Instantiator;
/*    */ import org.springframework.objenesis.instantiator.android.Android18Instantiator;
/*    */ import org.springframework.objenesis.instantiator.basic.AccessibleInstantiator;
/*    */ import org.springframework.objenesis.instantiator.basic.ObjectInputStreamInstantiator;
/*    */ import org.springframework.objenesis.instantiator.gcj.GCJInstantiator;
/*    */ import org.springframework.objenesis.instantiator.perc.PercInstantiator;
/*    */ import org.springframework.objenesis.instantiator.sun.SunReflectionFactoryInstantiator;
/*    */ import org.springframework.objenesis.instantiator.sun.UnsafeFactoryInstantiator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StdInstantiatorStrategy
/*    */   extends BaseInstantiatorStrategy
/*    */ {
/*    */   public <T> ObjectInstantiator<T> newInstantiatorOf(Class<T> type) {
/* 58 */     if (PlatformDescription.isThisJVM("Java HotSpot") || PlatformDescription.isThisJVM("OpenJDK")) {
/*    */       
/* 60 */       if (PlatformDescription.isGoogleAppEngine() && PlatformDescription.SPECIFICATION_VERSION.equals("1.7")) {
/* 61 */         if (Serializable.class.isAssignableFrom(type)) {
/* 62 */           return (ObjectInstantiator<T>)new ObjectInputStreamInstantiator(type);
/*    */         }
/* 64 */         return (ObjectInstantiator<T>)new AccessibleInstantiator(type);
/*    */       } 
/*    */ 
/*    */       
/* 68 */       return (ObjectInstantiator<T>)new SunReflectionFactoryInstantiator(type);
/*    */     } 
/* 70 */     if (PlatformDescription.isThisJVM("Dalvik")) {
/* 71 */       if (PlatformDescription.isAndroidOpenJDK())
/*    */       {
/* 73 */         return (ObjectInstantiator<T>)new UnsafeFactoryInstantiator(type);
/*    */       }
/* 75 */       if (PlatformDescription.ANDROID_VERSION <= 10)
/*    */       {
/* 77 */         return (ObjectInstantiator<T>)new Android10Instantiator(type);
/*    */       }
/* 79 */       if (PlatformDescription.ANDROID_VERSION <= 17)
/*    */       {
/* 81 */         return (ObjectInstantiator<T>)new Android17Instantiator(type);
/*    */       }
/*    */       
/* 84 */       return (ObjectInstantiator<T>)new Android18Instantiator(type);
/*    */     } 
/* 86 */     if (PlatformDescription.isThisJVM("GNU libgcj")) {
/* 87 */       return (ObjectInstantiator<T>)new GCJInstantiator(type);
/*    */     }
/* 89 */     if (PlatformDescription.isThisJVM("PERC")) {
/* 90 */       return (ObjectInstantiator<T>)new PercInstantiator(type);
/*    */     }
/*    */ 
/*    */     
/* 94 */     return (ObjectInstantiator<T>)new UnsafeFactoryInstantiator(type);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\objenesis\strategy\StdInstantiatorStrategy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */