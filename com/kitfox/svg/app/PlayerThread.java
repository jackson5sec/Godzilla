/*     */ package com.kitfox.svg.app;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerThread
/*     */   implements Runnable
/*     */ {
/*  48 */   HashSet<PlayerThreadListener> listeners = new HashSet<PlayerThreadListener>();
/*     */   
/*  50 */   double curTime = 0.0D;
/*  51 */   double timeStep = 0.2D;
/*     */   
/*     */   public static final int PS_STOP = 0;
/*     */   
/*     */   public static final int PS_PLAY_FWD = 1;
/*     */   public static final int PS_PLAY_BACK = 2;
/*  57 */   int playState = 0;
/*     */ 
/*     */   
/*     */   Thread thread;
/*     */ 
/*     */   
/*     */   public PlayerThread() {
/*  64 */     this.thread = new Thread(this);
/*  65 */     this.thread.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  70 */     while (this.thread != null) {
/*     */       
/*  72 */       synchronized (this) {
/*     */         
/*  74 */         switch (this.playState) {
/*     */           
/*     */           case 1:
/*  77 */             this.curTime += this.timeStep;
/*     */             break;
/*     */           case 2:
/*  80 */             this.curTime -= this.timeStep;
/*  81 */             if (this.curTime < 0.0D) this.curTime = 0.0D;
/*     */             
/*     */             break;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/*  88 */         fireTimeUpdateEvent();
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/*  93 */         Thread.sleep((long)(this.timeStep * 1000.0D));
/*     */       }
/*  95 */       catch (Exception e) {
/*     */         
/*  97 */         throw new RuntimeException(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void exit() {
/* 102 */     this.thread = null;
/*     */   }
/*     */   public synchronized void addListener(PlayerThreadListener listener) {
/* 105 */     this.listeners.add(listener);
/*     */   }
/*     */   public synchronized double getCurTime() {
/* 108 */     return this.curTime;
/*     */   }
/*     */   
/*     */   public synchronized void setCurTime(double time) {
/* 112 */     this.curTime = time;
/*     */   }
/*     */   public synchronized double getTimeStep() {
/* 115 */     return this.timeStep;
/*     */   }
/*     */   
/*     */   public synchronized void setTimeStep(double time) {
/* 119 */     this.timeStep = time;
/* 120 */     if (this.timeStep < 0.01D) this.timeStep = 0.01D; 
/*     */   }
/*     */   public synchronized int getPlayState() {
/* 123 */     return this.playState;
/*     */   }
/*     */   
/*     */   public synchronized void setPlayState(int playState) {
/* 127 */     this.playState = playState;
/*     */   }
/*     */ 
/*     */   
/*     */   private void fireTimeUpdateEvent() {
/* 132 */     for (PlayerThreadListener listener : this.listeners)
/* 133 */       listener.updateTime(this.curTime, this.timeStep, this.playState); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\app\PlayerThread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */