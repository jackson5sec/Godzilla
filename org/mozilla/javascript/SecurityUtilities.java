/*    */ package org.mozilla.javascript;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import java.security.ProtectionDomain;
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
/*    */ public class SecurityUtilities
/*    */ {
/*    */   public static String getSystemProperty(final String name) {
/* 26 */     return AccessController.<String>doPrivileged(new PrivilegedAction<String>()
/*    */         {
/*    */           
/*    */           public String run()
/*    */           {
/* 31 */             return System.getProperty(name);
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public static ProtectionDomain getProtectionDomain(final Class<?> clazz) {
/* 38 */     return AccessController.<ProtectionDomain>doPrivileged(new PrivilegedAction<ProtectionDomain>()
/*    */         {
/*    */           
/*    */           public ProtectionDomain run()
/*    */           {
/* 43 */             return clazz.getProtectionDomain();
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ProtectionDomain getScriptProtectionDomain() {
/* 56 */     final SecurityManager securityManager = System.getSecurityManager();
/* 57 */     if (securityManager instanceof RhinoSecurityManager) {
/* 58 */       return AccessController.<ProtectionDomain>doPrivileged(new PrivilegedAction<ProtectionDomain>()
/*    */           {
/*    */             public ProtectionDomain run() {
/* 61 */               Class<?> c = ((RhinoSecurityManager)securityManager).getCurrentScriptClass();
/*    */               
/* 63 */               return (c == null) ? null : c.getProtectionDomain();
/*    */             }
/*    */           });
/*    */     }
/*    */     
/* 68 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\SecurityUtilities.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */