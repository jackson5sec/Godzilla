/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ObjectArrays;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class SimpleTimeLimiter
/*     */   implements TimeLimiter
/*     */ {
/*     */   private final ExecutorService executor;
/*     */   
/*     */   private SimpleTimeLimiter(ExecutorService executor) {
/*  53 */     this.executor = (ExecutorService)Preconditions.checkNotNull(executor);
/*     */   }
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
/*     */   public static SimpleTimeLimiter create(ExecutorService executor) {
/*  68 */     return new SimpleTimeLimiter(executor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T newProxy(final T target, Class<T> interfaceType, final long timeoutDuration, final TimeUnit timeoutUnit) {
/*  77 */     Preconditions.checkNotNull(target);
/*  78 */     Preconditions.checkNotNull(interfaceType);
/*  79 */     Preconditions.checkNotNull(timeoutUnit);
/*  80 */     checkPositiveTimeout(timeoutDuration);
/*  81 */     Preconditions.checkArgument(interfaceType.isInterface(), "interfaceType must be an interface type");
/*     */     
/*  83 */     final Set<Method> interruptibleMethods = findInterruptibleMethods(interfaceType);
/*     */     
/*  85 */     InvocationHandler handler = new InvocationHandler()
/*     */       {
/*     */         
/*     */         public Object invoke(Object obj, final Method method, final Object[] args) throws Throwable
/*     */         {
/*  90 */           Callable<Object> callable = new Callable()
/*     */             {
/*     */               public Object call() throws Exception
/*     */               {
/*     */                 try {
/*  95 */                   return method.invoke(target, args);
/*  96 */                 } catch (InvocationTargetException e) {
/*  97 */                   throw SimpleTimeLimiter.throwCause(e, false);
/*     */                 } 
/*     */               }
/*     */             };
/* 101 */           return SimpleTimeLimiter.this.callWithTimeout((Callable)callable, timeoutDuration, timeoutUnit, interruptibleMethods
/* 102 */               .contains(method));
/*     */         }
/*     */       };
/* 105 */     return newProxy(interfaceType, handler);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <T> T newProxy(Class<T> interfaceType, InvocationHandler handler) {
/* 111 */     Object object = Proxy.newProxyInstance(interfaceType
/* 112 */         .getClassLoader(), new Class[] { interfaceType }, handler);
/* 113 */     return interfaceType.cast(object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private <T> T callWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit, boolean amInterruptible) throws Exception {
/* 120 */     Preconditions.checkNotNull(callable);
/* 121 */     Preconditions.checkNotNull(timeoutUnit);
/* 122 */     checkPositiveTimeout(timeoutDuration);
/*     */     
/* 124 */     Future<T> future = this.executor.submit(callable);
/*     */     
/*     */     try {
/* 127 */       if (amInterruptible) {
/*     */         try {
/* 129 */           return future.get(timeoutDuration, timeoutUnit);
/* 130 */         } catch (InterruptedException e) {
/* 131 */           future.cancel(true);
/* 132 */           throw e;
/*     */         } 
/*     */       }
/* 135 */       return Uninterruptibles.getUninterruptibly(future, timeoutDuration, timeoutUnit);
/*     */     }
/* 137 */     catch (ExecutionException e) {
/* 138 */       throw throwCause(e, true);
/* 139 */     } catch (TimeoutException e) {
/* 140 */       future.cancel(true);
/* 141 */       throw new UncheckedTimeoutException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public <T> T callWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit) throws TimeoutException, InterruptedException, ExecutionException {
/* 149 */     Preconditions.checkNotNull(callable);
/* 150 */     Preconditions.checkNotNull(timeoutUnit);
/* 151 */     checkPositiveTimeout(timeoutDuration);
/*     */     
/* 153 */     Future<T> future = this.executor.submit(callable);
/*     */     
/*     */     try {
/* 156 */       return future.get(timeoutDuration, timeoutUnit);
/* 157 */     } catch (InterruptedException|TimeoutException e) {
/* 158 */       future.cancel(true);
/* 159 */       throw e;
/* 160 */     } catch (ExecutionException e) {
/* 161 */       wrapAndThrowExecutionExceptionOrError(e.getCause());
/* 162 */       throw new AssertionError();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public <T> T callUninterruptiblyWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit) throws TimeoutException, ExecutionException {
/* 171 */     Preconditions.checkNotNull(callable);
/* 172 */     Preconditions.checkNotNull(timeoutUnit);
/* 173 */     checkPositiveTimeout(timeoutDuration);
/*     */     
/* 175 */     Future<T> future = this.executor.submit(callable);
/*     */     
/*     */     try {
/* 178 */       return Uninterruptibles.getUninterruptibly(future, timeoutDuration, timeoutUnit);
/* 179 */     } catch (TimeoutException e) {
/* 180 */       future.cancel(true);
/* 181 */       throw e;
/* 182 */     } catch (ExecutionException e) {
/* 183 */       wrapAndThrowExecutionExceptionOrError(e.getCause());
/* 184 */       throw new AssertionError();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void runWithTimeout(Runnable runnable, long timeoutDuration, TimeUnit timeoutUnit) throws TimeoutException, InterruptedException {
/* 191 */     Preconditions.checkNotNull(runnable);
/* 192 */     Preconditions.checkNotNull(timeoutUnit);
/* 193 */     checkPositiveTimeout(timeoutDuration);
/*     */     
/* 195 */     Future<?> future = this.executor.submit(runnable);
/*     */     
/*     */     try {
/* 198 */       future.get(timeoutDuration, timeoutUnit);
/* 199 */     } catch (InterruptedException|TimeoutException e) {
/* 200 */       future.cancel(true);
/* 201 */       throw e;
/* 202 */     } catch (ExecutionException e) {
/* 203 */       wrapAndThrowRuntimeExecutionExceptionOrError(e.getCause());
/* 204 */       throw new AssertionError();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void runUninterruptiblyWithTimeout(Runnable runnable, long timeoutDuration, TimeUnit timeoutUnit) throws TimeoutException {
/* 211 */     Preconditions.checkNotNull(runnable);
/* 212 */     Preconditions.checkNotNull(timeoutUnit);
/* 213 */     checkPositiveTimeout(timeoutDuration);
/*     */     
/* 215 */     Future<?> future = this.executor.submit(runnable);
/*     */     
/*     */     try {
/* 218 */       Uninterruptibles.getUninterruptibly(future, timeoutDuration, timeoutUnit);
/* 219 */     } catch (TimeoutException e) {
/* 220 */       future.cancel(true);
/* 221 */       throw e;
/* 222 */     } catch (ExecutionException e) {
/* 223 */       wrapAndThrowRuntimeExecutionExceptionOrError(e.getCause());
/* 224 */       throw new AssertionError();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Exception throwCause(Exception e, boolean combineStackTraces) throws Exception {
/* 229 */     Throwable cause = e.getCause();
/* 230 */     if (cause == null) {
/* 231 */       throw e;
/*     */     }
/* 233 */     if (combineStackTraces) {
/*     */       
/* 235 */       StackTraceElement[] combined = (StackTraceElement[])ObjectArrays.concat((Object[])cause.getStackTrace(), (Object[])e.getStackTrace(), StackTraceElement.class);
/* 236 */       cause.setStackTrace(combined);
/*     */     } 
/* 238 */     if (cause instanceof Exception) {
/* 239 */       throw (Exception)cause;
/*     */     }
/* 241 */     if (cause instanceof Error) {
/* 242 */       throw (Error)cause;
/*     */     }
/*     */     
/* 245 */     throw e;
/*     */   }
/*     */   
/*     */   private static Set<Method> findInterruptibleMethods(Class<?> interfaceType) {
/* 249 */     Set<Method> set = Sets.newHashSet();
/* 250 */     for (Method m : interfaceType.getMethods()) {
/* 251 */       if (declaresInterruptedEx(m)) {
/* 252 */         set.add(m);
/*     */       }
/*     */     } 
/* 255 */     return set;
/*     */   }
/*     */   
/*     */   private static boolean declaresInterruptedEx(Method method) {
/* 259 */     for (Class<?> exType : method.getExceptionTypes()) {
/*     */       
/* 261 */       if (exType == InterruptedException.class) {
/* 262 */         return true;
/*     */       }
/*     */     } 
/* 265 */     return false;
/*     */   }
/*     */   
/*     */   private void wrapAndThrowExecutionExceptionOrError(Throwable cause) throws ExecutionException {
/* 269 */     if (cause instanceof Error)
/* 270 */       throw new ExecutionError((Error)cause); 
/* 271 */     if (cause instanceof RuntimeException) {
/* 272 */       throw new UncheckedExecutionException(cause);
/*     */     }
/* 274 */     throw new ExecutionException(cause);
/*     */   }
/*     */ 
/*     */   
/*     */   private void wrapAndThrowRuntimeExecutionExceptionOrError(Throwable cause) {
/* 279 */     if (cause instanceof Error) {
/* 280 */       throw new ExecutionError((Error)cause);
/*     */     }
/* 282 */     throw new UncheckedExecutionException(cause);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void checkPositiveTimeout(long timeoutDuration) {
/* 287 */     Preconditions.checkArgument((timeoutDuration > 0L), "timeout must be positive: %s", timeoutDuration);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\SimpleTimeLimiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */