/*    */ package org.springframework.cglib.core;
/*    */ 
/*    */ import java.lang.ref.WeakReference;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WeakCacheKey<T>
/*    */   extends WeakReference<T>
/*    */ {
/*    */   private final int hash;
/*    */   
/*    */   public WeakCacheKey(T referent) {
/* 18 */     super(referent);
/* 19 */     this.hash = referent.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 24 */     if (!(obj instanceof WeakCacheKey)) {
/* 25 */       return false;
/*    */     }
/* 27 */     Object ours = get();
/* 28 */     Object theirs = ((WeakCacheKey)obj).get();
/* 29 */     return (ours != null && theirs != null && ours.equals(theirs));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 34 */     return this.hash;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 39 */     T t = get();
/* 40 */     return (t == null) ? ("Clean WeakIdentityKey, hash: " + this.hash) : t.toString();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\core\WeakCacheKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */