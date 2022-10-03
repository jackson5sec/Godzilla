/*    */ package org.mozilla.javascript;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public abstract class Ref
/*    */   implements Serializable
/*    */ {
/*    */   static final long serialVersionUID = 4044540354730911424L;
/*    */   
/*    */   public boolean has(Context cx) {
/* 22 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract Object get(Context paramContext);
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public abstract Object set(Context paramContext, Object paramObject);
/*    */ 
/*    */   
/*    */   public Object set(Context cx, Scriptable scope, Object value) {
/* 35 */     return set(cx, value);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean delete(Context cx) {
/* 40 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\Ref.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */