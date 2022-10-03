/*    */ package org.springframework.core;
/*    */ 
/*    */ import org.springframework.lang.Nullable;
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
/*    */ public final class SpringVersion
/*    */ {
/*    */   @Nullable
/*    */   public static String getVersion() {
/* 47 */     Package pkg = SpringVersion.class.getPackage();
/* 48 */     return (pkg != null) ? pkg.getImplementationVersion() : null;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\SpringVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */