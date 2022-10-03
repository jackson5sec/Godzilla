/*     */ package com.jediterm.terminal;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.charset.Charset;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ProcessTtyConnector
/*     */   implements TtyConnector
/*     */ {
/*     */   protected final InputStream myInputStream;
/*     */   protected final OutputStream myOutputStream;
/*     */   protected final InputStreamReader myReader;
/*     */   protected final Charset myCharset;
/*     */   private Dimension myPendingTermSize;
/*     */   private final Process myProcess;
/*     */   
/*     */   public ProcessTtyConnector(@NotNull Process process, @NotNull Charset charset) {
/*  25 */     this.myOutputStream = process.getOutputStream();
/*  26 */     this.myCharset = charset;
/*  27 */     this.myInputStream = process.getInputStream();
/*  28 */     this.myReader = new InputStreamReader(this.myInputStream, charset);
/*  29 */     this.myProcess = process;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public Process getProcess() {
/*  34 */     if (this.myProcess == null) $$$reportNull$$$0(2);  return this.myProcess;
/*     */   }
/*     */ 
/*     */   
/*     */   public void resize(@NotNull Dimension termWinSize) {
/*  39 */     if (termWinSize == null) $$$reportNull$$$0(3);  setPendingTermSize(termWinSize);
/*  40 */     if (isConnected()) {
/*  41 */       resizeImmediately();
/*  42 */       setPendingTermSize(null);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected void resizeImmediately() {}
/*     */ 
/*     */   
/*     */   public abstract String getName();
/*     */ 
/*     */   
/*     */   public int read(char[] buf, int offset, int length) throws IOException {
/*  56 */     return this.myReader.read(buf, offset, length);
/*     */   }
/*     */   
/*     */   public void write(byte[] bytes) throws IOException {
/*  60 */     this.myOutputStream.write(bytes);
/*  61 */     this.myOutputStream.flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract boolean isConnected();
/*     */ 
/*     */   
/*     */   public void write(String string) throws IOException {
/*  69 */     write(string.getBytes(this.myCharset));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected void setPendingTermSize(@Nullable Dimension pendingTermSize) {
/*  77 */     this.myPendingTermSize = pendingTermSize;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @Nullable
/*     */   protected Dimension getPendingTermSize() {
/*  85 */     return this.myPendingTermSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected Dimension getPendingPixelSize() {
/*  93 */     return new Dimension(0, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean init(Questioner q) {
/*  98 */     return isConnected();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 103 */     this.myProcess.destroy();
/*     */     try {
/* 105 */       this.myOutputStream.close();
/*     */     }
/* 107 */     catch (IOException iOException) {}
/*     */     try {
/* 109 */       this.myInputStream.close();
/*     */     }
/* 111 */     catch (IOException iOException) {}
/*     */   }
/*     */ 
/*     */   
/*     */   public int waitFor() throws InterruptedException {
/* 116 */     return this.myProcess.waitFor();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\ProcessTtyConnector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */