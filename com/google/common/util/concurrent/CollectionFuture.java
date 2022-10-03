/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableCollection;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ abstract class CollectionFuture<V, C>
/*     */   extends AggregateFuture<V, C>
/*     */ {
/*     */   abstract class CollectionFutureRunningState
/*     */     extends AggregateFuture<V, C>.RunningState
/*     */   {
/*     */     private List<Optional<V>> values;
/*     */     
/*     */     CollectionFutureRunningState(ImmutableCollection<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed) {
/*  39 */       super(futures, allMustSucceed, true);
/*     */       
/*  41 */       this
/*     */ 
/*     */         
/*  44 */         .values = futures.isEmpty() ? (List<Optional<V>>)ImmutableList.of() : Lists.newArrayListWithCapacity(futures.size());
/*     */ 
/*     */       
/*  47 */       for (int i = 0; i < futures.size(); i++) {
/*  48 */         this.values.add(null);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     final void collectOneValue(boolean allMustSucceed, int index, V returnValue) {
/*  54 */       List<Optional<V>> localValues = this.values;
/*     */       
/*  56 */       if (localValues != null) {
/*  57 */         localValues.set(index, Optional.fromNullable(returnValue));
/*     */       
/*     */       }
/*     */       else {
/*     */         
/*  62 */         Preconditions.checkState((allMustSucceed || CollectionFuture.this
/*  63 */             .isCancelled()), "Future was done before all dependencies completed");
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     final void handleAllCompleted() {
/*  69 */       List<Optional<V>> localValues = this.values;
/*  70 */       if (localValues != null) {
/*  71 */         CollectionFuture.this.set(combine(localValues));
/*     */       } else {
/*  73 */         Preconditions.checkState(CollectionFuture.this.isDone());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     void releaseResourcesAfterFailure() {
/*  79 */       super.releaseResourcesAfterFailure();
/*  80 */       this.values = null;
/*     */     }
/*     */ 
/*     */     
/*     */     abstract C combine(List<Optional<V>> param1List);
/*     */   }
/*     */   
/*     */   static final class ListFuture<V>
/*     */     extends CollectionFuture<V, List<V>>
/*     */   {
/*     */     ListFuture(ImmutableCollection<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed) {
/*  91 */       init(new ListFutureRunningState(futures, allMustSucceed));
/*     */     }
/*     */     
/*     */     private final class ListFutureRunningState
/*     */       extends CollectionFuture<V, List<V>>.CollectionFutureRunningState
/*     */     {
/*     */       ListFutureRunningState(ImmutableCollection<? extends ListenableFuture<? extends V>> futures, boolean allMustSucceed) {
/*  98 */         super(futures, allMustSucceed);
/*     */       }
/*     */ 
/*     */       
/*     */       public List<V> combine(List<Optional<V>> values) {
/* 103 */         List<V> result = Lists.newArrayListWithCapacity(values.size());
/* 104 */         for (Optional<V> element : values) {
/* 105 */           result.add((element != null) ? (V)element.orNull() : null);
/*     */         }
/* 107 */         return Collections.unmodifiableList(result);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\CollectionFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */