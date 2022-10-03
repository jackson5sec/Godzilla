/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.concurrent.Executor;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public abstract class AbstractIdleService
/*     */   implements Service
/*     */ {
/*  39 */   private final Supplier<String> threadNameSupplier = new ThreadNameSupplier();
/*     */   
/*     */   private final class ThreadNameSupplier implements Supplier<String> {
/*     */     private ThreadNameSupplier() {}
/*     */     
/*     */     public String get() {
/*  45 */       return AbstractIdleService.this.serviceName() + " " + AbstractIdleService.this.state();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  50 */   private final Service delegate = new DelegateService();
/*     */   
/*     */   private final class DelegateService extends AbstractService {
/*     */     private DelegateService() {}
/*     */     
/*     */     protected final void doStart() {
/*  56 */       MoreExecutors.renamingDecorator(AbstractIdleService.this.executor(), AbstractIdleService.this.threadNameSupplier)
/*  57 */         .execute(new Runnable()
/*     */           {
/*     */             public void run()
/*     */             {
/*     */               try {
/*  62 */                 AbstractIdleService.this.startUp();
/*  63 */                 AbstractIdleService.DelegateService.this.notifyStarted();
/*  64 */               } catch (Throwable t) {
/*  65 */                 AbstractIdleService.DelegateService.this.notifyFailed(t);
/*     */               } 
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*     */     protected final void doStop() {
/*  73 */       MoreExecutors.renamingDecorator(AbstractIdleService.this.executor(), AbstractIdleService.this.threadNameSupplier)
/*  74 */         .execute(new Runnable()
/*     */           {
/*     */             public void run()
/*     */             {
/*     */               try {
/*  79 */                 AbstractIdleService.this.shutDown();
/*  80 */                 AbstractIdleService.DelegateService.this.notifyStopped();
/*  81 */               } catch (Throwable t) {
/*  82 */                 AbstractIdleService.DelegateService.this.notifyFailed(t);
/*     */               } 
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  90 */       return AbstractIdleService.this.toString();
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Executor executor() {
/* 111 */     return new Executor()
/*     */       {
/*     */         public void execute(Runnable command) {
/* 114 */           MoreExecutors.newThread((String)AbstractIdleService.this.threadNameSupplier.get(), command).start();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 121 */     return serviceName() + " [" + state() + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isRunning() {
/* 126 */     return this.delegate.isRunning();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Service.State state() {
/* 131 */     return this.delegate.state();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void addListener(Service.Listener listener, Executor executor) {
/* 137 */     this.delegate.addListener(listener, executor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Throwable failureCause() {
/* 143 */     return this.delegate.failureCause();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Service startAsync() {
/* 150 */     this.delegate.startAsync();
/* 151 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Service stopAsync() {
/* 158 */     this.delegate.stopAsync();
/* 159 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void awaitRunning() {
/* 165 */     this.delegate.awaitRunning();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException {
/* 171 */     this.delegate.awaitRunning(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void awaitTerminated() {
/* 177 */     this.delegate.awaitTerminated();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException {
/* 183 */     this.delegate.awaitTerminated(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String serviceName() {
/* 193 */     return getClass().getSimpleName();
/*     */   }
/*     */   
/*     */   protected abstract void startUp() throws Exception;
/*     */   
/*     */   protected abstract void shutDown() throws Exception;
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\AbstractIdleService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */