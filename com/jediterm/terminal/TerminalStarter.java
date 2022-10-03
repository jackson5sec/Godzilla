/*     */ package com.jediterm.terminal;
/*     */ 
/*     */ import com.jediterm.terminal.emulator.Emulator;
/*     */ import com.jediterm.terminal.emulator.JediEmulator;
/*     */ import java.awt.Dimension;
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BiConsumer;
/*     */ import org.apache.log4j.Logger;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TerminalStarter
/*     */   implements TerminalOutputStream
/*     */ {
/*  24 */   private static final Logger LOG = Logger.getLogger(TerminalStarter.class);
/*     */   
/*     */   private final Emulator myEmulator;
/*     */   
/*     */   private final Terminal myTerminal;
/*     */   
/*     */   private final TtyConnector myTtyConnector;
/*     */   
/*  32 */   private final ScheduledExecutorService myEmulatorExecutor = Executors.newSingleThreadScheduledExecutor();
/*     */   
/*     */   public TerminalStarter(Terminal terminal, TtyConnector ttyConnector, TerminalDataStream dataStream) {
/*  35 */     this.myTtyConnector = ttyConnector;
/*  36 */     this.myTerminal = terminal;
/*  37 */     this.myTerminal.setTerminalOutput(this);
/*  38 */     this.myEmulator = (Emulator)createEmulator(dataStream, terminal);
/*     */   }
/*     */   
/*     */   protected JediEmulator createEmulator(TerminalDataStream dataStream, Terminal terminal) {
/*  42 */     return new JediEmulator(dataStream, terminal);
/*     */   }
/*     */   
/*     */   private void execute(Runnable runnable) {
/*  46 */     if (!this.myEmulatorExecutor.isShutdown()) {
/*  47 */       this.myEmulatorExecutor.execute(runnable);
/*     */     }
/*     */   }
/*     */   
/*     */   public void start() {
/*     */     try {
/*  53 */       while (!Thread.currentThread().isInterrupted() && this.myEmulator.hasNext()) {
/*  54 */         this.myEmulator.next();
/*     */       }
/*     */     }
/*  57 */     catch (InterruptedIOException e) {
/*  58 */       LOG.info("Terminal exiting");
/*     */     }
/*  60 */     catch (Exception e) {
/*  61 */       if (!this.myTtyConnector.isConnected()) {
/*  62 */         this.myTerminal.disconnected();
/*     */         return;
/*     */       } 
/*  65 */       LOG.error("Caught exception in terminal thread", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public byte[] getCode(int key, int modifiers) {
/*  70 */     return this.myTerminal.getCodeForKey(key, modifiers);
/*     */   }
/*     */   
/*     */   public void postResize(@NotNull Dimension dimension, @NotNull RequestOrigin origin) {
/*  74 */     if (dimension == null) $$$reportNull$$$0(0);  if (origin == null) $$$reportNull$$$0(1);  execute(() -> resize(this.myEmulator, this.myTerminal, this.myTtyConnector, dimension, origin, ()));
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
/*     */   public static void resize(@NotNull Emulator emulator, @NotNull Terminal terminal, @NotNull TtyConnector ttyConnector, @NotNull Dimension newTermSize, @NotNull RequestOrigin origin, @NotNull BiConsumer<Long, Runnable> taskScheduler) {
/*  90 */     if (emulator == null) $$$reportNull$$$0(2);  if (terminal == null) $$$reportNull$$$0(3);  if (ttyConnector == null) $$$reportNull$$$0(4);  if (newTermSize == null) $$$reportNull$$$0(5);  if (origin == null) $$$reportNull$$$0(6);  if (taskScheduler == null) $$$reportNull$$$0(7);  CompletableFuture<?> promptUpdated = ((JediEmulator)emulator).getPromptUpdatedAfterResizeFuture(taskScheduler);
/*  91 */     terminal.resize(newTermSize, origin, promptUpdated);
/*  92 */     ttyConnector.resize(newTermSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendBytes(byte[] bytes) {
/*  97 */     execute(() -> {
/*     */           
/*     */           try {
/*     */             this.myTtyConnector.write(bytes);
/* 101 */           } catch (IOException e) {
/*     */             throw new RuntimeException(e);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendString(String string) {
/* 109 */     execute(() -> {
/*     */           
/*     */           try {
/*     */             this.myTtyConnector.write(string);
/* 113 */           } catch (IOException e) {
/*     */             throw new RuntimeException(e);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   public void close() {
/* 120 */     execute(() -> {
/*     */           
/*     */           try {
/*     */             this.myTtyConnector.close();
/* 124 */           } catch (Exception e) {
/*     */             LOG.error("Error closing terminal", e);
/*     */           } finally {
/*     */             this.myEmulatorExecutor.shutdown();
/*     */           } 
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\TerminalStarter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */