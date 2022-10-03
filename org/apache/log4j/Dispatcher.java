/*     */ package org.apache.log4j;
/*     */ 
/*     */ import org.apache.log4j.helpers.AppenderAttachableImpl;
/*     */ import org.apache.log4j.helpers.BoundedFIFO;
/*     */ import org.apache.log4j.spi.LoggingEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class Dispatcher
/*     */   extends Thread
/*     */ {
/*     */   private BoundedFIFO bf;
/*     */   private AppenderAttachableImpl aai;
/*     */   private boolean interrupted = false;
/*     */   AsyncAppender container;
/*     */   
/*     */   Dispatcher(BoundedFIFO bf, AsyncAppender container) {
/*  45 */     this.bf = bf;
/*  46 */     this.container = container;
/*  47 */     this.aai = container.aai;
/*     */ 
/*     */ 
/*     */     
/*  51 */     setDaemon(true);
/*     */ 
/*     */     
/*  54 */     setPriority(1);
/*  55 */     setName("Dispatcher-" + getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void close() {
/*  63 */     synchronized (this.bf) {
/*  64 */       this.interrupted = true;
/*     */ 
/*     */ 
/*     */       
/*  68 */       if (this.bf.length() == 0) {
/*  69 */         this.bf.notify();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     while (true) {
/*     */       LoggingEvent event;
/*  89 */       synchronized (this.bf) {
/*  90 */         if (this.bf.length() == 0) {
/*     */           
/*  92 */           if (this.interrupted) {
/*     */             break;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           try {
/*  99 */             this.bf.wait();
/* 100 */           } catch (InterruptedException e) {
/*     */             break;
/*     */           } 
/*     */         } 
/*     */         
/* 105 */         event = this.bf.get();
/*     */         
/* 107 */         if (this.bf.wasFull())
/*     */         {
/* 109 */           this.bf.notify();
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 114 */       synchronized (this.container.aai) {
/* 115 */         if (this.aai != null && event != null) {
/* 116 */           this.aai.appendLoopOnAppenders(event);
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 123 */     this.aai.removeAllAppenders();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\Dispatcher.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */