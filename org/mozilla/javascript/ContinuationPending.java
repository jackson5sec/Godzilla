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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ContinuationPending
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 4956008116771118856L;
/*    */   private NativeContinuation continuationState;
/*    */   private Object applicationState;
/*    */   
/*    */   ContinuationPending(NativeContinuation continuationState) {
/* 32 */     this.continuationState = continuationState;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getContinuation() {
/* 42 */     return this.continuationState;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   NativeContinuation getContinuationState() {
/* 49 */     return this.continuationState;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setApplicationState(Object applicationState) {
/* 58 */     this.applicationState = applicationState;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getApplicationState() {
/* 65 */     return this.applicationState;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ContinuationPending.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */