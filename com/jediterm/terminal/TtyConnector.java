/*    */ package com.jediterm.terminal;
/*    */ 
/*    */ import java.awt.Dimension;
/*    */ import java.io.IOException;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface TtyConnector
/*    */ {
/*    */   boolean init(Questioner paramQuestioner);
/*    */   
/*    */   void close();
/*    */   
/*    */   default void resize(@NotNull Dimension termWinSize) {
/* 18 */     if (termWinSize == null) $$$reportNull$$$0(0);  resize(termWinSize, new Dimension(0, 0));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   default void resize(Dimension termWinSize, Dimension pixelSize) {
/* 29 */     resize(termWinSize);
/*    */   }
/*    */   
/*    */   String getName();
/*    */   
/*    */   int read(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws IOException;
/*    */   
/*    */   void write(byte[] paramArrayOfbyte) throws IOException;
/*    */   
/*    */   boolean isConnected();
/*    */   
/*    */   void write(String paramString) throws IOException;
/*    */   
/*    */   int waitFor() throws InterruptedException;
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\TtyConnector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */