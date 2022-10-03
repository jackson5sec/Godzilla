/*    */ package com.jediterm.terminal;
/*    */ 
/*    */ import com.jediterm.terminal.emulator.Emulator;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class DataStreamIteratingEmulator
/*    */   implements Emulator
/*    */ {
/*    */   protected final TerminalDataStream myDataStream;
/*    */   protected final Terminal myTerminal;
/*    */   private boolean myEof = false;
/*    */   
/*    */   public DataStreamIteratingEmulator(TerminalDataStream dataStream, Terminal terminal) {
/* 17 */     this.myDataStream = dataStream;
/* 18 */     this.myTerminal = terminal;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasNext() {
/* 23 */     return !this.myEof;
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetEof() {
/* 28 */     this.myEof = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void next() throws IOException {
/*    */     try {
/* 34 */       char b = this.myDataStream.getChar();
/*    */       
/* 36 */       processChar(b, this.myTerminal);
/*    */     }
/* 38 */     catch (EOF e) {
/* 39 */       this.myEof = true;
/*    */     } 
/*    */   }
/*    */   
/*    */   protected abstract void processChar(char paramChar, Terminal paramTerminal) throws IOException;
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\DataStreamIteratingEmulator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */