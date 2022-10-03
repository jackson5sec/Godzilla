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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Synchronizer
/*    */   extends Delegator
/*    */ {
/*    */   private Object syncObject;
/*    */   
/*    */   public Synchronizer(Scriptable obj) {
/* 39 */     super(obj);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Synchronizer(Scriptable obj, Object syncObject) {
/* 50 */     super(obj);
/* 51 */     this.syncObject = syncObject;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
/* 61 */     Object sync = (this.syncObject != null) ? this.syncObject : thisObj;
/* 62 */     synchronized ((sync instanceof Wrapper) ? ((Wrapper)sync).unwrap() : sync) {
/* 63 */       return ((Function)this.obj).call(cx, scope, thisObj, args);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\Synchronizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */