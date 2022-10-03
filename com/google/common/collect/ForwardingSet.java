/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Collection;
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
/*    */ @GwtCompatible
/*    */ public abstract class ForwardingSet<E>
/*    */   extends ForwardingCollection<E>
/*    */   implements Set<E>
/*    */ {
/*    */   public boolean equals(Object object) {
/* 60 */     return (object == this || delegate().equals(object));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 65 */     return delegate().hashCode();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean standardRemoveAll(Collection<?> collection) {
/* 77 */     return Sets.removeAllImpl(this, (Collection)Preconditions.checkNotNull(collection));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean standardEquals(Object object) {
/* 88 */     return Sets.equalsImpl(this, object);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int standardHashCode() {
/* 98 */     return Sets.hashCodeImpl(this);
/*    */   }
/*    */   
/*    */   protected abstract Set<E> delegate();
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\ForwardingSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */