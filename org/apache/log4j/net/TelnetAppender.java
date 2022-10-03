/*     */ package org.apache.log4j.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.Vector;
/*     */ import org.apache.log4j.AppenderSkeleton;
/*     */ import org.apache.log4j.helpers.LogLog;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TelnetAppender
/*     */   extends AppenderSkeleton
/*     */ {
/*     */   private SocketHandler sh;
/*  64 */   private int port = 23;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean requiresLayout() {
/*  70 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateOptions() {
/*     */     try {
/*  77 */       this.sh = new SocketHandler(this.port);
/*  78 */       this.sh.start();
/*     */     }
/*  80 */     catch (InterruptedIOException e) {
/*  81 */       Thread.currentThread().interrupt();
/*  82 */       e.printStackTrace();
/*  83 */     } catch (IOException e) {
/*  84 */       e.printStackTrace();
/*  85 */     } catch (RuntimeException e) {
/*  86 */       e.printStackTrace();
/*     */     } 
/*  88 */     super.activateOptions();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPort() {
/*  93 */     return this.port;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPort(int port) {
/*  98 */     this.port = port;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 104 */     if (this.sh != null) {
/* 105 */       this.sh.close();
/*     */       try {
/* 107 */         this.sh.join();
/* 108 */       } catch (InterruptedException ex) {
/* 109 */         Thread.currentThread().interrupt();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void append(LoggingEvent event) {
/* 117 */     if (this.sh != null) {
/* 118 */       this.sh.send(this.layout.format(event));
/* 119 */       if (this.layout.ignoresThrowable()) {
/* 120 */         String[] s = event.getThrowableStrRep();
/* 121 */         if (s != null) {
/* 122 */           StringBuffer buf = new StringBuffer();
/* 123 */           for (int i = 0; i < s.length; i++) {
/* 124 */             buf.append(s[i]);
/* 125 */             buf.append("\r\n");
/*     */           } 
/* 127 */           this.sh.send(buf.toString());
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class SocketHandler
/*     */     extends Thread
/*     */   {
/* 140 */     private Vector writers = new Vector();
/* 141 */     private Vector connections = new Vector();
/*     */     private ServerSocket serverSocket;
/* 143 */     private int MAX_CONNECTIONS = 20; private final TelnetAppender this$0;
/*     */     
/*     */     public void finalize() {
/* 146 */       close();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void close() {
/* 154 */       synchronized (this) {
/* 155 */         for (Enumeration e = this.connections.elements(); e.hasMoreElements();) {
/*     */           
/* 157 */           try { ((Socket)e.nextElement()).close(); }
/* 158 */           catch (InterruptedIOException ex)
/* 159 */           { Thread.currentThread().interrupt(); }
/* 160 */           catch (IOException ex) {  }
/* 161 */           catch (RuntimeException ex) {}
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 167 */       try { this.serverSocket.close(); }
/* 168 */       catch (InterruptedIOException ex)
/* 169 */       { Thread.currentThread().interrupt(); }
/* 170 */       catch (IOException ex) {  }
/* 171 */       catch (RuntimeException ex) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized void send(String message) {
/* 177 */       Iterator ce = this.connections.iterator();
/* 178 */       for (Iterator e = this.writers.iterator(); e.hasNext(); ) {
/* 179 */         ce.next();
/* 180 */         PrintWriter writer = e.next();
/* 181 */         writer.print(message);
/* 182 */         if (writer.checkError()) {
/* 183 */           ce.remove();
/* 184 */           e.remove();
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       while (true)
/* 194 */       { if (!this.serverSocket.isClosed()) {
/*     */           try {
/* 196 */             Socket newClient = this.serverSocket.accept();
/* 197 */             PrintWriter pw = new PrintWriter(newClient.getOutputStream());
/* 198 */             if (this.connections.size() < this.MAX_CONNECTIONS) {
/* 199 */               synchronized (this) {
/* 200 */                 this.connections.addElement(newClient);
/* 201 */                 this.writers.addElement(pw);
/* 202 */                 pw.print("TelnetAppender v1.0 (" + this.connections.size() + " active connections)\r\n\r\n");
/*     */                 
/* 204 */                 pw.flush();
/*     */               }  continue;
/*     */             } 
/* 207 */             pw.print("Too many connections.\r\n");
/* 208 */             pw.flush();
/* 209 */             newClient.close();
/*     */             continue;
/* 211 */           } catch (Exception e) {
/* 212 */             if (e instanceof InterruptedIOException || e instanceof InterruptedException) {
/* 213 */               Thread.currentThread().interrupt();
/*     */             }
/* 215 */             if (!this.serverSocket.isClosed()) {
/* 216 */               LogLog.error("Encountered error while in SocketHandler loop.", e);
/*     */             }
/*     */           } 
/*     */         } else {
/*     */           break;
/*     */         } 
/*     */         try {
/* 223 */           this.serverSocket.close();
/* 224 */         } catch (InterruptedIOException ex) {
/* 225 */           Thread.currentThread().interrupt();
/* 226 */         } catch (IOException ex) {} return; }  try { this.serverSocket.close(); } catch (InterruptedIOException interruptedIOException) { Thread.currentThread().interrupt(); } catch (IOException iOException) {}
/*     */     }
/*     */ 
/*     */     
/*     */     public SocketHandler(int port) throws IOException {
/* 231 */       this.serverSocket = new ServerSocket(port);
/* 232 */       setName("TelnetAppender-" + getName() + "-" + port);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\net\TelnetAppender.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */