/*      */ package com.google.common.util.concurrent;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.collect.ImmutableCollection;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.ScheduledExecutorService;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.TimeoutException;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Futures
/*      */   extends GwtFuturesCatchingSpecialization
/*      */ {
/*      */   @Deprecated
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> CheckedFuture<V, X> makeChecked(ListenableFuture<V> future, Function<? super Exception, X> mapper) {
/*  154 */     return new MappingCheckedFuture<>((ListenableFuture<V>)Preconditions.checkNotNull(future), mapper);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> ListenableFuture<V> immediateFuture(V value) {
/*  163 */     if (value == null) {
/*      */ 
/*      */       
/*  166 */       ListenableFuture<V> typedNull = ImmediateFuture.ImmediateSuccessfulFuture.NULL;
/*  167 */       return typedNull;
/*      */     } 
/*  169 */     return new ImmediateFuture.ImmediateSuccessfulFuture<>(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> CheckedFuture<V, X> immediateCheckedFuture(V value) {
/*  194 */     return new ImmediateFuture.ImmediateSuccessfulCheckedFuture<>(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> ListenableFuture<V> immediateFailedFuture(Throwable throwable) {
/*  205 */     Preconditions.checkNotNull(throwable);
/*  206 */     return new ImmediateFuture.ImmediateFailedFuture<>(throwable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> ListenableFuture<V> immediateCancelledFuture() {
/*  216 */     return new ImmediateFuture.ImmediateCancelledFuture<>();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> CheckedFuture<V, X> immediateFailedCheckedFuture(X exception) {
/*  242 */     Preconditions.checkNotNull(exception);
/*  243 */     return new ImmediateFuture.ImmediateFailedCheckedFuture<>(exception);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <O> ListenableFuture<O> submitAsync(AsyncCallable<O> callable, Executor executor) {
/*  254 */     TrustedListenableFutureTask<O> task = TrustedListenableFutureTask.create(callable);
/*  255 */     executor.execute(task);
/*  256 */     return task;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static <O> ListenableFuture<O> scheduleAsync(AsyncCallable<O> callable, long delay, TimeUnit timeUnit, ScheduledExecutorService executorService) {
/*  273 */     TrustedListenableFutureTask<O> task = TrustedListenableFutureTask.create(callable);
/*  274 */     final Future<?> scheduled = executorService.schedule(task, delay, timeUnit);
/*  275 */     task.addListener(new Runnable()
/*      */         {
/*      */           
/*      */           public void run()
/*      */           {
/*  280 */             scheduled.cancel(false);
/*      */           }
/*      */         }, 
/*  283 */         MoreExecutors.directExecutor());
/*  284 */     return task;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   @GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
/*      */   public static <V, X extends Throwable> ListenableFuture<V> catching(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback, Executor executor) {
/*  332 */     return AbstractCatchingFuture.create(input, exceptionType, fallback, executor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   @GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
/*      */   public static <V, X extends Throwable> ListenableFuture<V> catchingAsync(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback, Executor executor) {
/*  401 */     return AbstractCatchingFuture.create(input, exceptionType, fallback, executor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static <V> ListenableFuture<V> withTimeout(ListenableFuture<V> delegate, long time, TimeUnit unit, ScheduledExecutorService scheduledExecutor) {
/*  424 */     if (delegate.isDone()) {
/*  425 */       return delegate;
/*      */     }
/*  427 */     return TimeoutFuture.create(delegate, time, unit, scheduledExecutor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <I, O> ListenableFuture<O> transformAsync(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor) {
/*  471 */     return AbstractTransformFuture.create(input, function, executor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, Function<? super I, ? extends O> function, Executor executor) {
/*  508 */     return AbstractTransformFuture.create(input, function, executor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static <I, O> Future<O> lazyTransform(final Future<I> input, final Function<? super I, ? extends O> function) {
/*  535 */     Preconditions.checkNotNull(input);
/*  536 */     Preconditions.checkNotNull(function);
/*  537 */     return new Future<O>()
/*      */       {
/*      */         public boolean cancel(boolean mayInterruptIfRunning)
/*      */         {
/*  541 */           return input.cancel(mayInterruptIfRunning);
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isCancelled() {
/*  546 */           return input.isCancelled();
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isDone() {
/*  551 */           return input.isDone();
/*      */         }
/*      */ 
/*      */         
/*      */         public O get() throws InterruptedException, ExecutionException {
/*  556 */           return applyTransformation(input.get());
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         public O get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*  562 */           return applyTransformation(input.get(timeout, unit));
/*      */         }
/*      */         
/*      */         private O applyTransformation(I input) throws ExecutionException {
/*      */           try {
/*  567 */             return (O)function.apply(input);
/*  568 */           } catch (Throwable t) {
/*  569 */             throw new ExecutionException(t);
/*      */           } 
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @SafeVarargs
/*      */   @Beta
/*      */   public static <V> ListenableFuture<List<V>> allAsList(ListenableFuture<? extends V>... futures) {
/*  591 */     return new CollectionFuture.ListFuture<>((ImmutableCollection<? extends ListenableFuture<? extends V>>)ImmutableList.copyOf((Object[])futures), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <V> ListenableFuture<List<V>> allAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
/*  610 */     return new CollectionFuture.ListFuture<>((ImmutableCollection<? extends ListenableFuture<? extends V>>)ImmutableList.copyOf(futures), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @SafeVarargs
/*      */   @Beta
/*      */   public static <V> FutureCombiner<V> whenAllComplete(ListenableFuture<? extends V>... futures) {
/*  622 */     return new FutureCombiner<>(false, ImmutableList.copyOf((Object[])futures));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <V> FutureCombiner<V> whenAllComplete(Iterable<? extends ListenableFuture<? extends V>> futures) {
/*  634 */     return new FutureCombiner<>(false, ImmutableList.copyOf(futures));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @SafeVarargs
/*      */   @Beta
/*      */   public static <V> FutureCombiner<V> whenAllSucceed(ListenableFuture<? extends V>... futures) {
/*  647 */     return new FutureCombiner<>(true, ImmutableList.copyOf((Object[])futures));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <V> FutureCombiner<V> whenAllSucceed(Iterable<? extends ListenableFuture<? extends V>> futures) {
/*  660 */     return new FutureCombiner<>(true, ImmutableList.copyOf(futures));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   @CanIgnoreReturnValue
/*      */   @GwtCompatible
/*      */   public static final class FutureCombiner<V>
/*      */   {
/*      */     private final boolean allMustSucceed;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ImmutableList<ListenableFuture<? extends V>> futures;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private FutureCombiner(boolean allMustSucceed, ImmutableList<ListenableFuture<? extends V>> futures) {
/*  698 */       this.allMustSucceed = allMustSucceed;
/*  699 */       this.futures = futures;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <C> ListenableFuture<C> callAsync(AsyncCallable<C> combiner, Executor executor) {
/*  717 */       return new CombinedFuture<>((ImmutableCollection<? extends ListenableFuture<?>>)this.futures, this.allMustSucceed, executor, combiner);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public <C> ListenableFuture<C> call(Callable<C> combiner, Executor executor) {
/*  736 */       return new CombinedFuture<>((ImmutableCollection<? extends ListenableFuture<?>>)this.futures, this.allMustSucceed, executor, combiner);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ListenableFuture<?> run(final Runnable combiner, Executor executor) {
/*  751 */       return call(new Callable<Void>()
/*      */           {
/*      */             public Void call() throws Exception
/*      */             {
/*  755 */               combiner.run();
/*  756 */               return null;
/*      */             }
/*      */           },  executor);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <V> ListenableFuture<V> nonCancellationPropagating(ListenableFuture<V> future) {
/*  772 */     if (future.isDone()) {
/*  773 */       return future;
/*      */     }
/*  775 */     NonCancellationPropagatingFuture<V> output = new NonCancellationPropagatingFuture<>(future);
/*  776 */     future.addListener(output, MoreExecutors.directExecutor());
/*  777 */     return output;
/*      */   }
/*      */   
/*      */   private static final class NonCancellationPropagatingFuture<V>
/*      */     extends AbstractFuture.TrustedFuture<V>
/*      */     implements Runnable {
/*      */     private ListenableFuture<V> delegate;
/*      */     
/*      */     NonCancellationPropagatingFuture(ListenableFuture<V> delegate) {
/*  786 */       this.delegate = delegate;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void run() {
/*  793 */       ListenableFuture<V> localDelegate = this.delegate;
/*  794 */       if (localDelegate != null) {
/*  795 */         setFuture(localDelegate);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     protected String pendingToString() {
/*  801 */       ListenableFuture<V> localDelegate = this.delegate;
/*  802 */       if (localDelegate != null) {
/*  803 */         return "delegate=[" + localDelegate + "]";
/*      */       }
/*  805 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void afterDone() {
/*  810 */       this.delegate = null;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @SafeVarargs
/*      */   @Beta
/*      */   public static <V> ListenableFuture<List<V>> successfulAsList(ListenableFuture<? extends V>... futures) {
/*  831 */     return new CollectionFuture.ListFuture<>((ImmutableCollection<? extends ListenableFuture<? extends V>>)ImmutableList.copyOf((Object[])futures), false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <V> ListenableFuture<List<V>> successfulAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
/*  850 */     return new CollectionFuture.ListFuture<>((ImmutableCollection<? extends ListenableFuture<? extends V>>)ImmutableList.copyOf(futures), false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <T> ImmutableList<ListenableFuture<T>> inCompletionOrder(Iterable<? extends ListenableFuture<? extends T>> futures) {
/*      */     ImmutableList immutableList;
/*  879 */     if (futures instanceof Collection) {
/*  880 */       Collection<ListenableFuture<? extends T>> collection = (Collection)futures;
/*      */     } else {
/*  882 */       immutableList = ImmutableList.copyOf(futures);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  887 */     ListenableFuture[] arrayOfListenableFuture = (ListenableFuture[])immutableList.toArray((Object[])new ListenableFuture[immutableList.size()]);
/*  888 */     final InCompletionOrderState<T> state = new InCompletionOrderState<>(arrayOfListenableFuture);
/*  889 */     ImmutableList.Builder<AbstractFuture<T>> delegatesBuilder = ImmutableList.builder();
/*  890 */     for (int i = 0; i < arrayOfListenableFuture.length; i++) {
/*  891 */       delegatesBuilder.add(new InCompletionOrderFuture(state));
/*      */     }
/*      */     
/*  894 */     final ImmutableList<AbstractFuture<T>> delegates = delegatesBuilder.build();
/*  895 */     for (int j = 0; j < arrayOfListenableFuture.length; j++) {
/*  896 */       final int localI = j;
/*  897 */       arrayOfListenableFuture[j].addListener(new Runnable()
/*      */           {
/*      */             public void run()
/*      */             {
/*  901 */               state.recordInputCompletion(delegates, localI);
/*      */             }
/*  904 */           }MoreExecutors.directExecutor());
/*      */     } 
/*      */ 
/*      */     
/*  908 */     return (ImmutableList)delegates;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class InCompletionOrderFuture<T>
/*      */     extends AbstractFuture<T>
/*      */   {
/*      */     private Futures.InCompletionOrderState<T> state;
/*      */ 
/*      */     
/*      */     private InCompletionOrderFuture(Futures.InCompletionOrderState<T> state) {
/*  919 */       this.state = state;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean cancel(boolean interruptIfRunning) {
/*  924 */       Futures.InCompletionOrderState<T> localState = this.state;
/*  925 */       if (super.cancel(interruptIfRunning)) {
/*  926 */         localState.recordOutputCancellation(interruptIfRunning);
/*  927 */         return true;
/*      */       } 
/*  929 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void afterDone() {
/*  934 */       this.state = null;
/*      */     }
/*      */ 
/*      */     
/*      */     protected String pendingToString() {
/*  939 */       Futures.InCompletionOrderState<T> localState = this.state;
/*  940 */       if (localState != null)
/*      */       {
/*      */         
/*  943 */         return "inputCount=[" + localState
/*  944 */           .inputFutures.length + "], remaining=[" + localState
/*      */           
/*  946 */           .incompleteOutputCount.get() + "]";
/*      */       }
/*      */       
/*  949 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class InCompletionOrderState<T>
/*      */   {
/*      */     private boolean wasCancelled = false;
/*      */     
/*      */     private boolean shouldInterrupt = true;
/*      */     private final AtomicInteger incompleteOutputCount;
/*      */     private final ListenableFuture<? extends T>[] inputFutures;
/*  961 */     private volatile int delegateIndex = 0;
/*      */     
/*      */     private InCompletionOrderState(ListenableFuture<? extends T>[] inputFutures) {
/*  964 */       this.inputFutures = inputFutures;
/*  965 */       this.incompleteOutputCount = new AtomicInteger(inputFutures.length);
/*      */     }
/*      */     
/*      */     private void recordOutputCancellation(boolean interruptIfRunning) {
/*  969 */       this.wasCancelled = true;
/*      */ 
/*      */       
/*  972 */       if (!interruptIfRunning) {
/*  973 */         this.shouldInterrupt = false;
/*      */       }
/*  975 */       recordCompletion();
/*      */     }
/*      */ 
/*      */     
/*      */     private void recordInputCompletion(ImmutableList<AbstractFuture<T>> delegates, int inputFutureIndex) {
/*  980 */       ListenableFuture<? extends T> inputFuture = this.inputFutures[inputFutureIndex];
/*      */       
/*  982 */       this.inputFutures[inputFutureIndex] = null;
/*  983 */       for (int i = this.delegateIndex; i < delegates.size(); i++) {
/*  984 */         if (((AbstractFuture<T>)delegates.get(i)).setFuture(inputFuture)) {
/*  985 */           recordCompletion();
/*      */           
/*  987 */           this.delegateIndex = i + 1;
/*      */ 
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*      */       
/*  994 */       this.delegateIndex = delegates.size();
/*      */     }
/*      */     
/*      */     private void recordCompletion() {
/*  998 */       if (this.incompleteOutputCount.decrementAndGet() == 0 && this.wasCancelled) {
/*  999 */         for (ListenableFuture<?> toCancel : this.inputFutures) {
/* 1000 */           if (toCancel != null) {
/* 1001 */             toCancel.cancel(this.shouldInterrupt);
/*      */           }
/*      */         } 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> void addCallback(ListenableFuture<V> future, FutureCallback<? super V> callback, Executor executor) {
/* 1050 */     Preconditions.checkNotNull(callback);
/* 1051 */     future.addListener(new CallbackListener<>(future, callback), executor);
/*      */   }
/*      */   
/*      */   private static final class CallbackListener<V>
/*      */     implements Runnable {
/*      */     final Future<V> future;
/*      */     final FutureCallback<? super V> callback;
/*      */     
/*      */     CallbackListener(Future<V> future, FutureCallback<? super V> callback) {
/* 1060 */       this.future = future;
/* 1061 */       this.callback = callback;
/*      */     }
/*      */ 
/*      */     
/*      */     public void run() {
/*      */       V value;
/*      */       try {
/* 1068 */         value = Futures.getDone(this.future);
/* 1069 */       } catch (ExecutionException e) {
/* 1070 */         this.callback.onFailure(e.getCause());
/*      */         return;
/* 1072 */       } catch (RuntimeException|Error e) {
/* 1073 */         this.callback.onFailure(e);
/*      */         return;
/*      */       } 
/* 1076 */       this.callback.onSuccess(value);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1081 */       return MoreObjects.toStringHelper(this).addValue(this.callback).toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <V> V getDone(Future<V> future) throws ExecutionException {
/* 1118 */     Preconditions.checkState(future.isDone(), "Future was expected to be done: %s", future);
/* 1119 */     return Uninterruptibles.getUninterruptibly(future);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   @CanIgnoreReturnValue
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass) throws X {
/* 1169 */     return FuturesGetChecked.getChecked(future, exceptionClass);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   @CanIgnoreReturnValue
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass, long timeout, TimeUnit unit) throws X {
/* 1221 */     return FuturesGetChecked.getChecked(future, exceptionClass, timeout, unit);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <V> V getUnchecked(Future<V> future) {
/* 1260 */     Preconditions.checkNotNull(future);
/*      */     try {
/* 1262 */       return Uninterruptibles.getUninterruptibly(future);
/* 1263 */     } catch (ExecutionException e) {
/* 1264 */       wrapAndThrowUnchecked(e.getCause());
/* 1265 */       throw new AssertionError();
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void wrapAndThrowUnchecked(Throwable cause) {
/* 1270 */     if (cause instanceof Error) {
/* 1271 */       throw new ExecutionError((Error)cause);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1278 */     throw new UncheckedExecutionException(cause);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   private static class MappingCheckedFuture<V, X extends Exception>
/*      */     extends AbstractCheckedFuture<V, X>
/*      */   {
/*      */     final Function<? super Exception, X> mapper;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     MappingCheckedFuture(ListenableFuture<V> delegate, Function<? super Exception, X> mapper) {
/* 1303 */       super(delegate);
/*      */       
/* 1305 */       this.mapper = (Function<? super Exception, X>)Preconditions.checkNotNull(mapper);
/*      */     }
/*      */ 
/*      */     
/*      */     protected X mapException(Exception e) {
/* 1310 */       return (X)this.mapper.apply(e);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\Futures.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */