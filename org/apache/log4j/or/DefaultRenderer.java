/*    */ package org.apache.log4j.or;
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
/*    */ class DefaultRenderer
/*    */   implements ObjectRenderer
/*    */ {
/*    */   public String doRender(Object o) {
/*    */     try {
/* 37 */       return o.toString();
/* 38 */     } catch (Exception ex) {
/* 39 */       return ex.toString();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\or\DefaultRenderer.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */