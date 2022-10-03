/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class CustomizableThreadCreator
/*     */   implements Serializable
/*     */ {
/*     */   private String threadNamePrefix;
/*  40 */   private int threadPriority = 5;
/*     */   
/*     */   private boolean daemon = false;
/*     */   
/*     */   @Nullable
/*     */   private ThreadGroup threadGroup;
/*     */   
/*  47 */   private final AtomicInteger threadCount = new AtomicInteger();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CustomizableThreadCreator() {
/*  54 */     this.threadNamePrefix = getDefaultThreadNamePrefix();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CustomizableThreadCreator(@Nullable String threadNamePrefix) {
/*  62 */     this.threadNamePrefix = (threadNamePrefix != null) ? threadNamePrefix : getDefaultThreadNamePrefix();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThreadNamePrefix(@Nullable String threadNamePrefix) {
/*  71 */     this.threadNamePrefix = (threadNamePrefix != null) ? threadNamePrefix : getDefaultThreadNamePrefix();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getThreadNamePrefix() {
/*  79 */     return this.threadNamePrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThreadPriority(int threadPriority) {
/*  88 */     this.threadPriority = threadPriority;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getThreadPriority() {
/*  95 */     return this.threadPriority;
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
/*     */   public void setDaemon(boolean daemon) {
/* 109 */     this.daemon = daemon;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDaemon() {
/* 116 */     return this.daemon;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThreadGroupName(String name) {
/* 124 */     this.threadGroup = new ThreadGroup(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setThreadGroup(@Nullable ThreadGroup threadGroup) {
/* 132 */     this.threadGroup = threadGroup;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ThreadGroup getThreadGroup() {
/* 141 */     return this.threadGroup;
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
/*     */   public Thread createThread(Runnable runnable) {
/* 153 */     Thread thread = new Thread(getThreadGroup(), runnable, nextThreadName());
/* 154 */     thread.setPriority(getThreadPriority());
/* 155 */     thread.setDaemon(isDaemon());
/* 156 */     return thread;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String nextThreadName() {
/* 166 */     return getThreadNamePrefix() + this.threadCount.incrementAndGet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getDefaultThreadNamePrefix() {
/* 174 */     return ClassUtils.getShortName(getClass()) + "-";
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\CustomizableThreadCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */