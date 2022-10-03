/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Set;
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
/*    */ @GwtCompatible(serializable = true)
/*    */ final class JdkBackedImmutableSet<E>
/*    */   extends IndexedImmutableSet<E>
/*    */ {
/*    */   private final Set<?> delegate;
/*    */   private final ImmutableList<E> delegateList;
/*    */   
/*    */   JdkBackedImmutableSet(Set<?> delegate, ImmutableList<E> delegateList) {
/* 34 */     this.delegate = delegate;
/* 35 */     this.delegateList = delegateList;
/*    */   }
/*    */ 
/*    */   
/*    */   E get(int index) {
/* 40 */     return this.delegateList.get(index);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(Object object) {
/* 45 */     return this.delegate.contains(object);
/*    */   }
/*    */ 
/*    */   
/*    */   boolean isPartialView() {
/* 50 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 55 */     return this.delegateList.size();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\JdkBackedImmutableSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */