/*     */ package org.apache.log4j.varia;
/*     */ 
/*     */ import org.apache.log4j.RollingFileAppender;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExternallyRolledFileAppender
/*     */   extends RollingFileAppender
/*     */ {
/*     */   public static final String ROLL_OVER = "RollOver";
/*     */   public static final String OK = "OK";
/*  64 */   int port = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   HUP hup;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPort(int port) {
/*  80 */     this.port = port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPort() {
/*  88 */     return this.port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateOptions() {
/*  96 */     super.activateOptions();
/*  97 */     if (this.port != 0) {
/*  98 */       if (this.hup != null) {
/*  99 */         this.hup.interrupt();
/*     */       }
/* 101 */       this.hup = new HUP(this, this.port);
/* 102 */       this.hup.setDaemon(true);
/* 103 */       this.hup.start();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\varia\ExternallyRolledFileAppender.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */