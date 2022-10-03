/*     */ package org.apache.log4j.chainsaw;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import org.apache.log4j.Logger;
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
/*     */ class LoggingReceiver
/*     */   extends Thread
/*     */ {
/*  36 */   private static final Logger LOG = Logger.getLogger(LoggingReceiver.class);
/*     */ 
/*     */   
/*     */   private MyTableModel mModel;
/*     */ 
/*     */   
/*     */   private ServerSocket mSvrSock;
/*     */ 
/*     */   
/*     */   private class Slurper
/*     */     implements Runnable
/*     */   {
/*     */     private final Socket mClient;
/*     */     
/*     */     private final LoggingReceiver this$0;
/*     */ 
/*     */     
/*     */     Slurper(Socket aClient) {
/*  54 */       this.mClient = aClient;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*  59 */       LoggingReceiver.LOG.debug("Starting to get data");
/*     */       try {
/*  61 */         ObjectInputStream ois = new ObjectInputStream(this.mClient.getInputStream());
/*     */         
/*     */         while (true) {
/*  64 */           LoggingEvent event = (LoggingEvent)ois.readObject();
/*  65 */           LoggingReceiver.this.mModel.addEvent(new EventDetails(event));
/*     */         } 
/*  67 */       } catch (EOFException e) {
/*  68 */         LoggingReceiver.LOG.info("Reached EOF, closing connection");
/*  69 */       } catch (SocketException e) {
/*  70 */         LoggingReceiver.LOG.info("Caught SocketException, closing connection");
/*  71 */       } catch (IOException e) {
/*  72 */         LoggingReceiver.LOG.warn("Got IOException, closing connection", e);
/*  73 */       } catch (ClassNotFoundException e) {
/*  74 */         LoggingReceiver.LOG.warn("Got ClassNotFoundException, closing connection", e);
/*     */       } 
/*     */       
/*     */       try {
/*  78 */         this.mClient.close();
/*  79 */       } catch (IOException e) {
/*  80 */         LoggingReceiver.LOG.warn("Error closing connection", e);
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
/*     */   
/*     */   LoggingReceiver(MyTableModel aModel, int aPort) throws IOException {
/*  99 */     setDaemon(true);
/* 100 */     this.mModel = aModel;
/* 101 */     this.mSvrSock = new ServerSocket(aPort);
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/* 106 */     LOG.info("Thread started");
/*     */     try {
/*     */       while (true) {
/* 109 */         LOG.debug("Waiting for a connection");
/* 110 */         Socket client = this.mSvrSock.accept();
/* 111 */         LOG.debug("Got a connection from " + client.getInetAddress().getHostName());
/*     */         
/* 113 */         Thread t = new Thread(new Slurper(client));
/* 114 */         t.setDaemon(true);
/* 115 */         t.start();
/*     */       } 
/* 117 */     } catch (IOException e) {
/* 118 */       LOG.error("Error in accepting connections, stopping.", e);
/*     */       return;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\chainsaw\LoggingReceiver.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */