/*     */ package org.apache.log4j.helpers;
/*     */ 
/*     */ import java.io.File;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class FileWatchdog
/*     */   extends Thread
/*     */ {
/*     */   public static final long DEFAULT_DELAY = 60000L;
/*     */   protected String filename;
/*  45 */   protected long delay = 60000L;
/*     */   
/*     */   File file;
/*  48 */   long lastModif = 0L;
/*     */   
/*     */   boolean warnedAlready = false;
/*     */   boolean interrupted = false;
/*     */   
/*     */   protected FileWatchdog(String filename) {
/*  54 */     super("FileWatchdog");
/*  55 */     this.filename = filename;
/*  56 */     this.file = new File(filename);
/*  57 */     setDaemon(true);
/*  58 */     checkAndConfigure();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDelay(long delay) {
/*  66 */     this.delay = delay;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void doOnChange();
/*     */ 
/*     */   
/*     */   protected void checkAndConfigure() {
/*     */     boolean fileExists;
/*     */     try {
/*  77 */       fileExists = this.file.exists();
/*  78 */     } catch (SecurityException e) {
/*  79 */       LogLog.warn("Was not allowed to read check file existance, file:[" + this.filename + "].");
/*     */       
/*  81 */       this.interrupted = true;
/*     */       
/*     */       return;
/*     */     } 
/*  85 */     if (fileExists) {
/*  86 */       long l = this.file.lastModified();
/*  87 */       if (l > this.lastModif) {
/*  88 */         this.lastModif = l;
/*  89 */         doOnChange();
/*  90 */         this.warnedAlready = false;
/*     */       }
/*     */     
/*  93 */     } else if (!this.warnedAlready) {
/*  94 */       LogLog.debug("[" + this.filename + "] does not exist.");
/*  95 */       this.warnedAlready = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 102 */     while (!this.interrupted) {
/*     */       try {
/* 104 */         Thread.sleep(this.delay);
/* 105 */       } catch (InterruptedException e) {}
/*     */ 
/*     */       
/* 108 */       checkAndConfigure();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\helpers\FileWatchdog.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */