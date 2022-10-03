/*    */ package org.springframework.core;
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
/*    */ public abstract class NativeDetector
/*    */ {
/* 31 */   private static final boolean imageCode = (System.getProperty("org.graalvm.nativeimage.imagecode") != null);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean inNativeImage() {
/* 37 */     return imageCode;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\NativeDetector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */