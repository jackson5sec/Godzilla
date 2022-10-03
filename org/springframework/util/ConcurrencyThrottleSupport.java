/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ public abstract class ConcurrencyThrottleSupport
/*     */   implements Serializable
/*     */ {
/*     */   public static final int UNBOUNDED_CONCURRENCY = -1;
/*     */   public static final int NO_CONCURRENCY = 0;
/*  61 */   protected transient Log logger = LogFactory.getLog(getClass());
/*     */   
/*  63 */   private transient Object monitor = new Object();
/*     */   
/*  65 */   private int concurrencyLimit = -1;
/*     */   
/*  67 */   private int concurrencyCount = 0;
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
/*     */   public void setConcurrencyLimit(int concurrencyLimit) {
/*  80 */     this.concurrencyLimit = concurrencyLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getConcurrencyLimit() {
/*  87 */     return this.concurrencyLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isThrottleActive() {
/*  96 */     return (this.concurrencyLimit >= 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void beforeAccess() {
/* 106 */     if (this.concurrencyLimit == 0) {
/* 107 */       throw new IllegalStateException("Currently no invocations allowed - concurrency limit set to NO_CONCURRENCY");
/*     */     }
/*     */     
/* 110 */     if (this.concurrencyLimit > 0) {
/* 111 */       boolean debug = this.logger.isDebugEnabled();
/* 112 */       synchronized (this.monitor) {
/* 113 */         boolean interrupted = false;
/* 114 */         while (this.concurrencyCount >= this.concurrencyLimit) {
/* 115 */           if (interrupted) {
/* 116 */             throw new IllegalStateException("Thread was interrupted while waiting for invocation access, but concurrency limit still does not allow for entering");
/*     */           }
/*     */           
/* 119 */           if (debug) {
/* 120 */             this.logger.debug("Concurrency count " + this.concurrencyCount + " has reached limit " + this.concurrencyLimit + " - blocking");
/*     */           }
/*     */           
/*     */           try {
/* 124 */             this.monitor.wait();
/*     */           }
/* 126 */           catch (InterruptedException ex) {
/*     */             
/* 128 */             Thread.currentThread().interrupt();
/* 129 */             interrupted = true;
/*     */           } 
/*     */         } 
/* 132 */         if (debug) {
/* 133 */           this.logger.debug("Entering throttle at concurrency count " + this.concurrencyCount);
/*     */         }
/* 135 */         this.concurrencyCount++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void afterAccess() {
/* 145 */     if (this.concurrencyLimit >= 0) {
/* 146 */       synchronized (this.monitor) {
/* 147 */         this.concurrencyCount--;
/* 148 */         if (this.logger.isDebugEnabled()) {
/* 149 */           this.logger.debug("Returning from throttle at concurrency count " + this.concurrencyCount);
/*     */         }
/* 151 */         this.monitor.notify();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 163 */     ois.defaultReadObject();
/*     */ 
/*     */     
/* 166 */     this.logger = LogFactory.getLog(getClass());
/* 167 */     this.monitor = new Object();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\ConcurrencyThrottleSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */