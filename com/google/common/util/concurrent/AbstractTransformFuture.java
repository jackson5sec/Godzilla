/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.ForOverride;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Future;
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
/*     */ abstract class AbstractTransformFuture<I, O, F, T>
/*     */   extends FluentFuture.TrustedFuture<O>
/*     */   implements Runnable
/*     */ {
/*     */   ListenableFuture<? extends I> inputFuture;
/*     */   F function;
/*     */   
/*     */   static <I, O> ListenableFuture<O> create(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor) {
/*  37 */     Preconditions.checkNotNull(executor);
/*  38 */     AsyncTransformFuture<I, O> output = new AsyncTransformFuture<>(input, function);
/*  39 */     input.addListener(output, MoreExecutors.rejectionPropagatingExecutor(executor, output));
/*  40 */     return output;
/*     */   }
/*     */ 
/*     */   
/*     */   static <I, O> ListenableFuture<O> create(ListenableFuture<I> input, Function<? super I, ? extends O> function, Executor executor) {
/*  45 */     Preconditions.checkNotNull(function);
/*  46 */     TransformFuture<I, O> output = new TransformFuture<>(input, function);
/*  47 */     input.addListener(output, MoreExecutors.rejectionPropagatingExecutor(executor, output));
/*  48 */     return output;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   AbstractTransformFuture(ListenableFuture<? extends I> inputFuture, F function) {
/*  59 */     this.inputFuture = (ListenableFuture<? extends I>)Preconditions.checkNotNull(inputFuture);
/*  60 */     this.function = (F)Preconditions.checkNotNull(function);
/*     */   }
/*     */   public final void run() {
/*     */     I sourceResult;
/*     */     T transformResult;
/*  65 */     ListenableFuture<? extends I> localInputFuture = this.inputFuture;
/*  66 */     F localFunction = this.function;
/*  67 */     if ((isCancelled() | ((localInputFuture == null) ? 1 : 0) | ((localFunction == null) ? 1 : 0)) != 0) {
/*     */       return;
/*     */     }
/*  70 */     this.inputFuture = null;
/*     */     
/*  72 */     if (localInputFuture.isCancelled()) {
/*     */ 
/*     */       
/*  75 */       boolean unused = setFuture(localInputFuture);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  89 */       sourceResult = Futures.getDone((Future)localInputFuture);
/*  90 */     } catch (CancellationException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  96 */       cancel(false);
/*     */       return;
/*  98 */     } catch (ExecutionException e) {
/*     */       
/* 100 */       setException(e.getCause());
/*     */       return;
/* 102 */     } catch (RuntimeException e) {
/*     */       
/* 104 */       setException(e);
/*     */       return;
/* 106 */     } catch (Error e) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 112 */       setException(e);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     try {
/* 118 */       transformResult = doTransform(localFunction, sourceResult);
/* 119 */     } catch (Throwable t) {
/*     */       
/* 121 */       setException(t);
/*     */       return;
/*     */     } finally {
/* 124 */       this.function = null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 163 */     setResult(transformResult);
/*     */   }
/*     */ 
/*     */   
/*     */   @ForOverride
/*     */   abstract T doTransform(F paramF, I paramI) throws Exception;
/*     */ 
/*     */   
/*     */   @ForOverride
/*     */   abstract void setResult(T paramT);
/*     */ 
/*     */   
/*     */   protected final void afterDone() {
/* 176 */     maybePropagateCancellationTo(this.inputFuture);
/* 177 */     this.inputFuture = null;
/* 178 */     this.function = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String pendingToString() {
/* 183 */     ListenableFuture<? extends I> localInputFuture = this.inputFuture;
/* 184 */     F localFunction = this.function;
/* 185 */     String superString = super.pendingToString();
/* 186 */     String resultString = "";
/* 187 */     if (localInputFuture != null) {
/* 188 */       resultString = "inputFuture=[" + localInputFuture + "], ";
/*     */     }
/* 190 */     if (localFunction != null)
/* 191 */       return resultString + "function=[" + localFunction + "]"; 
/* 192 */     if (superString != null) {
/* 193 */       return resultString + superString;
/*     */     }
/* 195 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class AsyncTransformFuture<I, O>
/*     */     extends AbstractTransformFuture<I, O, AsyncFunction<? super I, ? extends O>, ListenableFuture<? extends O>>
/*     */   {
/*     */     AsyncTransformFuture(ListenableFuture<? extends I> inputFuture, AsyncFunction<? super I, ? extends O> function) {
/* 207 */       super(inputFuture, function);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     ListenableFuture<? extends O> doTransform(AsyncFunction<? super I, ? extends O> function, I input) throws Exception {
/* 213 */       ListenableFuture<? extends O> outputFuture = function.apply(input);
/* 214 */       Preconditions.checkNotNull(outputFuture, "AsyncFunction.apply returned null instead of a Future. Did you mean to return immediateFuture(null)? %s", function);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 219 */       return outputFuture;
/*     */     }
/*     */ 
/*     */     
/*     */     void setResult(ListenableFuture<? extends O> result) {
/* 224 */       setFuture(result);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class TransformFuture<I, O>
/*     */     extends AbstractTransformFuture<I, O, Function<? super I, ? extends O>, O>
/*     */   {
/*     */     TransformFuture(ListenableFuture<? extends I> inputFuture, Function<? super I, ? extends O> function) {
/* 236 */       super(inputFuture, function);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     O doTransform(Function<? super I, ? extends O> function, I input) {
/* 242 */       return (O)function.apply(input);
/*     */     }
/*     */ 
/*     */     
/*     */     void setResult(O result) {
/* 247 */       set(result);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\AbstractTransformFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */