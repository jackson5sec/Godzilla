/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public abstract class AbstractExecutionThreadService
/*     */   implements Service
/*     */ {
/*  39 */   private static final Logger logger = Logger.getLogger(AbstractExecutionThreadService.class.getName());
/*     */ 
/*     */   
/*  42 */   private final Service delegate = new AbstractService()
/*     */     {
/*     */       
/*     */       protected final void doStart()
/*     */       {
/*  47 */         Executor executor = MoreExecutors.renamingDecorator(AbstractExecutionThreadService.this
/*  48 */             .executor(), new Supplier<String>()
/*     */             {
/*     */               public String get()
/*     */               {
/*  52 */                 return AbstractExecutionThreadService.this.serviceName();
/*     */               }
/*     */             });
/*  55 */         executor.execute(new Runnable()
/*     */             {
/*     */               public void run()
/*     */               {
/*     */                 try {
/*  60 */                   AbstractExecutionThreadService.this.startUp();
/*  61 */                   AbstractExecutionThreadService.null.this.notifyStarted();
/*     */ 
/*     */                   
/*  64 */                   if (AbstractExecutionThreadService.null.this.isRunning()) {
/*     */                     try {
/*  66 */                       AbstractExecutionThreadService.this.run();
/*  67 */                     } catch (Throwable t) {
/*     */                       try {
/*  69 */                         AbstractExecutionThreadService.this.shutDown();
/*  70 */                       } catch (Exception ignored) {
/*     */ 
/*     */ 
/*     */                         
/*  74 */                         AbstractExecutionThreadService.logger.log(Level.WARNING, "Error while attempting to shut down the service after failure.", ignored);
/*     */                       } 
/*     */ 
/*     */ 
/*     */                       
/*  79 */                       AbstractExecutionThreadService.null.this.notifyFailed(t);
/*     */                       
/*     */                       return;
/*     */                     } 
/*     */                   }
/*  84 */                   AbstractExecutionThreadService.this.shutDown();
/*  85 */                   AbstractExecutionThreadService.null.this.notifyStopped();
/*  86 */                 } catch (Throwable t) {
/*  87 */                   AbstractExecutionThreadService.null.this.notifyFailed(t);
/*     */                 } 
/*     */               }
/*     */             });
/*     */       }
/*     */ 
/*     */       
/*     */       protected void doStop() {
/*  95 */         AbstractExecutionThreadService.this.triggerShutdown();
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 100 */         return AbstractExecutionThreadService.this.toString();
/*     */       }
/*     */     };
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
/*     */   protected void startUp() throws Exception {}
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
/*     */   protected void shutDown() throws Exception {}
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
/*     */   protected void triggerShutdown() {}
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
/* 157 */     return new Executor()
/*     */       {
/*     */         public void execute(Runnable command) {
/* 160 */           MoreExecutors.newThread(AbstractExecutionThreadService.this.serviceName(), command).start();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 167 */     return serviceName() + " [" + state() + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isRunning() {
/* 172 */     return this.delegate.isRunning();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Service.State state() {
/* 177 */     return this.delegate.state();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void addListener(Service.Listener listener, Executor executor) {
/* 183 */     this.delegate.addListener(listener, executor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Throwable failureCause() {
/* 189 */     return this.delegate.failureCause();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Service startAsync() {
/* 196 */     this.delegate.startAsync();
/* 197 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Service stopAsync() {
/* 204 */     this.delegate.stopAsync();
/* 205 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void awaitRunning() {
/* 211 */     this.delegate.awaitRunning();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException {
/* 217 */     this.delegate.awaitRunning(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void awaitTerminated() {
/* 223 */     this.delegate.awaitTerminated();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException {
/* 229 */     this.delegate.awaitTerminated(timeout, unit);
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
/*     */   protected String serviceName() {
/* 241 */     return getClass().getSimpleName();
/*     */   }
/*     */   
/*     */   protected abstract void run() throws Exception;
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\AbstractExecutionThreadService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */