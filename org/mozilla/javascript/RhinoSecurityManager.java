/*    */ package org.mozilla.javascript;
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
/*    */ public class RhinoSecurityManager
/*    */   extends SecurityManager
/*    */ {
/*    */   protected Class<?> getCurrentScriptClass() {
/* 23 */     Class<?>[] context = getClassContext();
/* 24 */     for (Class<?> c : context) {
/* 25 */       if ((c != InterpretedFunction.class && NativeFunction.class.isAssignableFrom(c)) || PolicySecurityController.SecureCaller.class.isAssignableFrom(c))
/*    */       {
/* 27 */         return c;
/*    */       }
/*    */     } 
/* 30 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\RhinoSecurityManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */