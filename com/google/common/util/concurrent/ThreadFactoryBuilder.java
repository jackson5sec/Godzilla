/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.CheckReturnValue;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.atomic.AtomicLong;
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
/*     */ @CanIgnoreReturnValue
/*     */ @GwtIncompatible
/*     */ public final class ThreadFactoryBuilder
/*     */ {
/*  49 */   private String nameFormat = null;
/*  50 */   private Boolean daemon = null;
/*  51 */   private Integer priority = null;
/*  52 */   private Thread.UncaughtExceptionHandler uncaughtExceptionHandler = null;
/*  53 */   private ThreadFactory backingThreadFactory = null;
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
/*     */   public ThreadFactoryBuilder setNameFormat(String nameFormat) {
/*  70 */     String unused = format(nameFormat, new Object[] { Integer.valueOf(0) });
/*  71 */     this.nameFormat = nameFormat;
/*  72 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ThreadFactoryBuilder setDaemon(boolean daemon) {
/*  82 */     this.daemon = Boolean.valueOf(daemon);
/*  83 */     return this;
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
/*     */   public ThreadFactoryBuilder setPriority(int priority) {
/*  95 */     Preconditions.checkArgument((priority >= 1), "Thread priority (%s) must be >= %s", priority, 1);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     Preconditions.checkArgument((priority <= 10), "Thread priority (%s) must be <= %s", priority, 10);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 105 */     this.priority = Integer.valueOf(priority);
/* 106 */     return this;
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
/*     */   public ThreadFactoryBuilder setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
/* 118 */     this.uncaughtExceptionHandler = (Thread.UncaughtExceptionHandler)Preconditions.checkNotNull(uncaughtExceptionHandler);
/* 119 */     return this;
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
/*     */   public ThreadFactoryBuilder setThreadFactory(ThreadFactory backingThreadFactory) {
/* 132 */     this.backingThreadFactory = (ThreadFactory)Preconditions.checkNotNull(backingThreadFactory);
/* 133 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CheckReturnValue
/*     */   public ThreadFactory build() {
/* 145 */     return doBuild(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static ThreadFactory doBuild(ThreadFactoryBuilder builder) {
/* 151 */     final String nameFormat = builder.nameFormat;
/* 152 */     final Boolean daemon = builder.daemon;
/* 153 */     final Integer priority = builder.priority;
/* 154 */     final Thread.UncaughtExceptionHandler uncaughtExceptionHandler = builder.uncaughtExceptionHandler;
/*     */ 
/*     */ 
/*     */     
/* 158 */     final ThreadFactory backingThreadFactory = (builder.backingThreadFactory != null) ? builder.backingThreadFactory : Executors.defaultThreadFactory();
/* 159 */     final AtomicLong count = (nameFormat != null) ? new AtomicLong(0L) : null;
/* 160 */     return new ThreadFactory()
/*     */       {
/*     */         public Thread newThread(Runnable runnable) {
/* 163 */           Thread thread = backingThreadFactory.newThread(runnable);
/* 164 */           if (nameFormat != null) {
/* 165 */             thread.setName(ThreadFactoryBuilder.format(nameFormat, new Object[] { Long.valueOf(this.val$count.getAndIncrement()) }));
/*     */           }
/* 167 */           if (daemon != null) {
/* 168 */             thread.setDaemon(daemon.booleanValue());
/*     */           }
/* 170 */           if (priority != null) {
/* 171 */             thread.setPriority(priority.intValue());
/*     */           }
/* 173 */           if (uncaughtExceptionHandler != null) {
/* 174 */             thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
/*     */           }
/* 176 */           return thread;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static String format(String format, Object... args) {
/* 182 */     return String.format(Locale.ROOT, format, args);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\ThreadFactoryBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */