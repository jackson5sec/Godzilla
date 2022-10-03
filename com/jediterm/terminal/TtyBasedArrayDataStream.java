/*    */ package com.jediterm.terminal;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TtyBasedArrayDataStream
/*    */   extends ArrayTerminalDataStream
/*    */ {
/*    */   private final TtyConnector myTtyConnector;
/*    */   
/*    */   public TtyBasedArrayDataStream(TtyConnector ttyConnector) {
/* 14 */     super(new char[1024], 0, 0);
/* 15 */     this.myTtyConnector = ttyConnector;
/*    */   }
/*    */   
/*    */   private void fillBuf() throws IOException {
/* 19 */     this.myOffset = 0;
/* 20 */     this.myLength = this.myTtyConnector.read(this.myBuf, this.myOffset, this.myBuf.length);
/*    */     
/* 22 */     if (this.myLength <= 0) {
/* 23 */       this.myLength = 0;
/* 24 */       throw new TerminalDataStream.EOF();
/*    */     } 
/*    */   }
/*    */   
/*    */   public char getChar() throws IOException {
/* 29 */     if (this.myLength == 0) {
/* 30 */       fillBuf();
/*    */     }
/* 32 */     return super.getChar();
/*    */   }
/*    */   
/*    */   public String readNonControlCharacters(int maxChars) throws IOException {
/* 36 */     if (this.myLength == 0) {
/* 37 */       fillBuf();
/*    */     }
/*    */     
/* 40 */     return super.readNonControlCharacters(maxChars);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 45 */     return getDebugText();
/*    */   }
/*    */   @NotNull
/*    */   private String getDebugText() {
/* 49 */     String s = new String(this.myBuf, this.myOffset, this.myLength);
/* 50 */     if (s.replace("\033", "ESC").replace("\n", "\\n").replace("\r", "\\r").replace("\007", "BEL").replace(" ", "<S>") == null) $$$reportNull$$$0(0);  return s.replace("\033", "ESC").replace("\n", "\\n").replace("\r", "\\r").replace("\007", "BEL").replace(" ", "<S>");
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\TtyBasedArrayDataStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */