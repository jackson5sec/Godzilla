/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableCollection;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.errorprone.annotations.ForOverride;
/*     */ import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ abstract class AggregateFuture<InputT, OutputT>
/*     */   extends AbstractFuture.TrustedFuture<OutputT>
/*     */ {
/*  41 */   private static final Logger logger = Logger.getLogger(AggregateFuture.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   private RunningState runningState;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void afterDone() {
/*  51 */     super.afterDone();
/*  52 */     RunningState localRunningState = this.runningState;
/*  53 */     if (localRunningState != null) {
/*     */       
/*  55 */       this.runningState = null;
/*     */       
/*  57 */       ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures = localRunningState.futures;
/*  58 */       boolean wasInterrupted = wasInterrupted();
/*     */       
/*  60 */       if (wasInterrupted) {
/*  61 */         localRunningState.interruptTask();
/*     */       }
/*     */       
/*  64 */       if ((isCancelled() & ((futures != null) ? 1 : 0)) != 0) {
/*  65 */         for (UnmodifiableIterator<ListenableFuture> unmodifiableIterator = futures.iterator(); unmodifiableIterator.hasNext(); ) { ListenableFuture<?> future = unmodifiableIterator.next();
/*  66 */           future.cancel(wasInterrupted); }
/*     */       
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected String pendingToString() {
/*  74 */     RunningState localRunningState = this.runningState;
/*  75 */     if (localRunningState == null) {
/*  76 */       return null;
/*     */     }
/*     */     
/*  79 */     ImmutableCollection<? extends ListenableFuture<? extends InputT>> localFutures = localRunningState.futures;
/*  80 */     if (localFutures != null) {
/*  81 */       return "futures=[" + localFutures + "]";
/*     */     }
/*  83 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   final void init(RunningState runningState) {
/*  88 */     this.runningState = runningState;
/*  89 */     runningState.init();
/*     */   }
/*     */   
/*     */   abstract class RunningState
/*     */     extends AggregateFutureState
/*     */     implements Runnable
/*     */   {
/*     */     private ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures;
/*     */     private final boolean allMustSucceed;
/*     */     private final boolean collectsValues;
/*     */     
/*     */     RunningState(ImmutableCollection<? extends ListenableFuture<? extends InputT>> futures, boolean allMustSucceed, boolean collectsValues) {
/* 101 */       super(futures.size());
/* 102 */       this.futures = (ImmutableCollection<? extends ListenableFuture<? extends InputT>>)Preconditions.checkNotNull(futures);
/* 103 */       this.allMustSucceed = allMustSucceed;
/* 104 */       this.collectsValues = collectsValues;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final void run() {
/* 110 */       decrementCountAndMaybeComplete();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void init() {
/* 121 */       if (this.futures.isEmpty()) {
/* 122 */         handleAllCompleted();
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */ 
/*     */       
/* 129 */       if (this.allMustSucceed) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 139 */         int i = 0;
/* 140 */         for (UnmodifiableIterator<ListenableFuture<? extends InputT>> unmodifiableIterator = this.futures.iterator(); unmodifiableIterator.hasNext(); ) { final ListenableFuture<? extends InputT> listenable = unmodifiableIterator.next();
/* 141 */           final int index = i++;
/* 142 */           listenable.addListener(new Runnable()
/*     */               {
/*     */                 public void run()
/*     */                 {
/*     */                   try {
/* 147 */                     AggregateFuture.RunningState.this.handleOneInputDone(index, listenable);
/*     */                   } finally {
/* 149 */                     AggregateFuture.RunningState.this.decrementCountAndMaybeComplete();
/*     */                   }
/*     */                 
/*     */                 }
/* 153 */               }MoreExecutors.directExecutor()); }
/*     */ 
/*     */       
/*     */       } else {
/*     */         
/* 158 */         for (UnmodifiableIterator<ListenableFuture<? extends InputT>> unmodifiableIterator = this.futures.iterator(); unmodifiableIterator.hasNext(); ) { final ListenableFuture<? extends InputT> listenable = unmodifiableIterator.next();
/* 159 */           listenable.addListener(this, MoreExecutors.directExecutor()); }
/*     */       
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void handleException(Throwable throwable) {
/* 171 */       Preconditions.checkNotNull(throwable);
/*     */       
/* 173 */       boolean completedWithFailure = false;
/* 174 */       boolean firstTimeSeeingThisException = true;
/* 175 */       if (this.allMustSucceed) {
/*     */ 
/*     */         
/* 178 */         completedWithFailure = AggregateFuture.this.setException(throwable);
/* 179 */         if (completedWithFailure) {
/* 180 */           releaseResourcesAfterFailure();
/*     */         }
/*     */         else {
/*     */           
/* 184 */           firstTimeSeeingThisException = AggregateFuture.addCausalChain(getOrInitSeenExceptions(), throwable);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 189 */       if ((throwable instanceof Error | this.allMustSucceed & (!completedWithFailure ? 1 : 0) & firstTimeSeeingThisException) != 0) {
/*     */         
/* 191 */         String message = (throwable instanceof Error) ? "Input Future failed with Error" : "Got more than one input Future failure. Logging failures after the first";
/*     */ 
/*     */ 
/*     */         
/* 195 */         AggregateFuture.logger.log(Level.SEVERE, message, throwable);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     final void addInitialException(Set<Throwable> seen) {
/* 201 */       if (!AggregateFuture.this.isCancelled())
/*     */       {
/* 203 */         boolean bool = AggregateFuture.addCausalChain(seen, AggregateFuture.this.tryInternalFastPathGetFailure());
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void handleOneInputDone(int index, Future<? extends InputT> future) {
/* 211 */       Preconditions.checkState((this.allMustSucceed || 
/* 212 */           !AggregateFuture.this.isDone() || AggregateFuture.this.isCancelled()), "Future was done before all dependencies completed");
/*     */ 
/*     */       
/*     */       try {
/* 216 */         Preconditions.checkState(future.isDone(), "Tried to set value from future which is not done");
/* 217 */         if (this.allMustSucceed) {
/* 218 */           if (future.isCancelled()) {
/*     */ 
/*     */             
/* 221 */             AggregateFuture.this.runningState = null;
/* 222 */             AggregateFuture.this.cancel(false);
/*     */           } else {
/*     */             
/* 225 */             InputT result = Futures.getDone((Future)future);
/* 226 */             if (this.collectsValues) {
/* 227 */               collectOneValue(this.allMustSucceed, index, result);
/*     */             }
/*     */           } 
/* 230 */         } else if (this.collectsValues && !future.isCancelled()) {
/* 231 */           collectOneValue(this.allMustSucceed, index, Futures.getDone((Future)future));
/*     */         } 
/* 233 */       } catch (ExecutionException e) {
/* 234 */         handleException(e.getCause());
/* 235 */       } catch (Throwable t) {
/* 236 */         handleException(t);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void decrementCountAndMaybeComplete() {
/* 241 */       int newRemaining = decrementRemainingAndGet();
/* 242 */       Preconditions.checkState((newRemaining >= 0), "Less than 0 remaining futures");
/* 243 */       if (newRemaining == 0) {
/* 244 */         processCompleted();
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void processCompleted() {
/* 251 */       if ((this.collectsValues & (!this.allMustSucceed ? 1 : 0)) != 0) {
/* 252 */         int i = 0;
/* 253 */         for (UnmodifiableIterator<ListenableFuture<? extends InputT>> unmodifiableIterator = this.futures.iterator(); unmodifiableIterator.hasNext(); ) { ListenableFuture<? extends InputT> listenable = unmodifiableIterator.next();
/* 254 */           handleOneInputDone(i++, listenable); }
/*     */       
/*     */       } 
/* 257 */       handleAllCompleted();
/*     */     }
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
/*     */     @ForOverride
/*     */     @OverridingMethodsMustInvokeSuper
/*     */     void releaseResourcesAfterFailure() {
/* 272 */       this.futures = null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void interruptTask() {}
/*     */ 
/*     */ 
/*     */     
/*     */     abstract void collectOneValue(boolean param1Boolean, int param1Int, InputT param1InputT);
/*     */ 
/*     */ 
/*     */     
/*     */     abstract void handleAllCompleted();
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean addCausalChain(Set<Throwable> seen, Throwable t) {
/* 290 */     for (; t != null; t = t.getCause()) {
/* 291 */       boolean firstTimeSeen = seen.add(t);
/* 292 */       if (!firstTimeSeen)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 299 */         return false;
/*     */       }
/*     */     } 
/* 302 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\AggregateFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */