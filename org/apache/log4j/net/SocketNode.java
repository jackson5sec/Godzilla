/*     */ package org.apache.log4j.net;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.apache.log4j.Priority;
/*     */ import org.apache.log4j.spi.LoggerRepository;
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
/*     */ public class SocketNode
/*     */   implements Runnable
/*     */ {
/*     */   Socket socket;
/*     */   LoggerRepository hierarchy;
/*     */   ObjectInputStream ois;
/*  50 */   static Logger logger = Logger.getLogger(SocketNode.class);
/*     */   
/*     */   public SocketNode(Socket socket, LoggerRepository hierarchy) {
/*  53 */     this.socket = socket;
/*  54 */     this.hierarchy = hierarchy;
/*     */     try {
/*  56 */       this.ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
/*     */     }
/*  58 */     catch (InterruptedIOException e) {
/*  59 */       Thread.currentThread().interrupt();
/*  60 */       logger.error("Could not open ObjectInputStream to " + socket, e);
/*  61 */     } catch (IOException e) {
/*  62 */       logger.error("Could not open ObjectInputStream to " + socket, e);
/*  63 */     } catch (RuntimeException e) {
/*  64 */       logger.error("Could not open ObjectInputStream to " + socket, e);
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
/*     */   public void run() {
/*     */     try {
/*  79 */       if (this.ois != null) {
/*     */         while (true) {
/*     */           
/*  82 */           LoggingEvent event = (LoggingEvent)this.ois.readObject();
/*     */           
/*  84 */           Logger remoteLogger = this.hierarchy.getLogger(event.getLoggerName());
/*     */ 
/*     */           
/*  87 */           if (event.getLevel().isGreaterOrEqual((Priority)remoteLogger.getEffectiveLevel()))
/*     */           {
/*  89 */             remoteLogger.callAppenders(event);
/*     */           }
/*     */         } 
/*     */       }
/*  93 */     } catch (EOFException e) {
/*  94 */       logger.info("Caught java.io.EOFException closing conneciton.");
/*  95 */     } catch (SocketException e) {
/*  96 */       logger.info("Caught java.net.SocketException closing conneciton.");
/*  97 */     } catch (InterruptedIOException e) {
/*  98 */       Thread.currentThread().interrupt();
/*  99 */       logger.info("Caught java.io.InterruptedIOException: " + e);
/* 100 */       logger.info("Closing connection.");
/* 101 */     } catch (IOException e) {
/* 102 */       logger.info("Caught java.io.IOException: " + e);
/* 103 */       logger.info("Closing connection.");
/* 104 */     } catch (Exception e) {
/* 105 */       logger.error("Unexpected exception. Closing conneciton.", e);
/*     */     } finally {
/* 107 */       if (this.ois != null) {
/*     */         try {
/* 109 */           this.ois.close();
/* 110 */         } catch (Exception e) {
/* 111 */           logger.info("Could not close connection.", e);
/*     */         } 
/*     */       }
/* 114 */       if (this.socket != null)
/*     */         try {
/* 116 */           this.socket.close();
/* 117 */         } catch (InterruptedIOException e) {
/* 118 */           Thread.currentThread().interrupt();
/* 119 */         } catch (IOException ex) {} 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\net\SocketNode.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */