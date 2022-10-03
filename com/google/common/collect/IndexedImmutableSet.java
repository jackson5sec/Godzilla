/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.util.Iterator;
/*    */ import java.util.Spliterator;
/*    */ import java.util.function.Consumer;
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
/*    */ @GwtCompatible(emulated = true)
/*    */ abstract class IndexedImmutableSet<E>
/*    */   extends ImmutableSet<E>
/*    */ {
/*    */   public UnmodifiableIterator<E> iterator() {
/* 32 */     return asList().iterator();
/*    */   }
/*    */ 
/*    */   
/*    */   public Spliterator<E> spliterator() {
/* 37 */     return CollectSpliterators.indexed(size(), 1297, this::get);
/*    */   }
/*    */ 
/*    */   
/*    */   public void forEach(Consumer<? super E> consumer) {
/* 42 */     Preconditions.checkNotNull(consumer);
/* 43 */     int n = size();
/* 44 */     for (int i = 0; i < n; i++) {
/* 45 */       consumer.accept(get(i));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   @GwtIncompatible
/*    */   int copyIntoArray(Object[] dst, int offset) {
/* 52 */     return asList().copyIntoArray(dst, offset);
/*    */   }
/*    */ 
/*    */   
/*    */   ImmutableList<E> createAsList() {
/* 57 */     return new ImmutableAsList<E>()
/*    */       {
/*    */         public E get(int index) {
/* 60 */           return IndexedImmutableSet.this.get(index);
/*    */         }
/*    */ 
/*    */         
/*    */         boolean isPartialView() {
/* 65 */           return IndexedImmutableSet.this.isPartialView();
/*    */         }
/*    */ 
/*    */         
/*    */         public int size() {
/* 70 */           return IndexedImmutableSet.this.size();
/*    */         }
/*    */ 
/*    */         
/*    */         ImmutableCollection<E> delegateCollection() {
/* 75 */           return IndexedImmutableSet.this;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   abstract E get(int paramInt);
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\collect\IndexedImmutableSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */