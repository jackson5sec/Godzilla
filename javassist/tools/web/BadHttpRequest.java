/*    */ package javassist.tools.web;
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
/*    */ public class BadHttpRequest
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private Exception e;
/*    */   
/*    */   public BadHttpRequest() {
/* 27 */     this.e = null;
/*    */   } public BadHttpRequest(Exception _e) {
/* 29 */     this.e = _e;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 33 */     if (this.e == null)
/* 34 */       return super.toString(); 
/* 35 */     return this.e.toString();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\javassist\tools\web\BadHttpRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */