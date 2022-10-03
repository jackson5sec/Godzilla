/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
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
/*     */ public interface Service
/*     */ {
/*     */   @CanIgnoreReturnValue
/*     */   Service startAsync();
/*     */   
/*     */   boolean isRunning();
/*     */   
/*     */   State state();
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   Service stopAsync();
/*     */   
/*     */   void awaitRunning();
/*     */   
/*     */   void awaitRunning(long paramLong, TimeUnit paramTimeUnit) throws TimeoutException;
/*     */   
/*     */   void awaitTerminated();
/*     */   
/*     */   void awaitTerminated(long paramLong, TimeUnit paramTimeUnit) throws TimeoutException;
/*     */   
/*     */   Throwable failureCause();
/*     */   
/*     */   void addListener(Listener paramListener, Executor paramExecutor);
/*     */   
/*     */   @Beta
/*     */   public enum State
/*     */   {
/* 179 */     NEW
/*     */     {
/*     */       boolean isTerminal() {
/* 182 */         return false;
/*     */       }
/*     */     },
/*     */ 
/*     */     
/* 187 */     STARTING
/*     */     {
/*     */       boolean isTerminal() {
/* 190 */         return false;
/*     */       }
/*     */     },
/*     */ 
/*     */     
/* 195 */     RUNNING
/*     */     {
/*     */       boolean isTerminal() {
/* 198 */         return false;
/*     */       }
/*     */     },
/*     */ 
/*     */     
/* 203 */     STOPPING
/*     */     {
/*     */       boolean isTerminal() {
/* 206 */         return false;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 214 */     TERMINATED
/*     */     {
/*     */       boolean isTerminal() {
/* 217 */         return true;
/*     */       }
/*     */     },
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 225 */     FAILED
/*     */     {
/*     */       boolean isTerminal() {
/* 228 */         return true;
/*     */       }
/*     */     };
/*     */     
/*     */     abstract boolean isTerminal();
/*     */   }
/*     */   
/*     */   @Beta
/*     */   public static abstract class Listener {
/*     */     public void starting() {}
/*     */     
/*     */     public void running() {}
/*     */     
/*     */     public void stopping(Service.State from) {}
/*     */     
/*     */     public void terminated(Service.State from) {}
/*     */     
/*     */     public void failed(Service.State from, Throwable failure) {}
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\commo\\util\concurrent\Service.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */