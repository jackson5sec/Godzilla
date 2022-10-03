/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.lang.ref.PhantomReference;
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
/*    */ @GwtIncompatible
/*    */ public abstract class FinalizablePhantomReference<T>
/*    */   extends PhantomReference<T>
/*    */   implements FinalizableReference
/*    */ {
/*    */   protected FinalizablePhantomReference(T referent, FinalizableReferenceQueue queue) {
/* 41 */     super(referent, queue.queue);
/* 42 */     queue.cleanUp();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\base\FinalizablePhantomReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */