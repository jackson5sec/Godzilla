/*    */ package org.mozilla.javascript.tools.shell;
/*    */ 
/*    */ import java.io.OutputStream;
/*    */ import javax.swing.SwingUtilities;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ConsoleWriter
/*    */   extends OutputStream
/*    */ {
/*    */   private ConsoleTextArea textArea;
/*    */   private StringBuffer buffer;
/*    */   
/*    */   public ConsoleWriter(ConsoleTextArea textArea) {
/* 35 */     this.textArea = textArea;
/* 36 */     this.buffer = new StringBuffer();
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void write(int ch) {
/* 41 */     this.buffer.append((char)ch);
/* 42 */     if (ch == 10) {
/* 43 */       flushBuffer();
/*    */     }
/*    */   }
/*    */   
/*    */   public synchronized void write(char[] data, int off, int len) {
/* 48 */     for (int i = off; i < len; i++) {
/* 49 */       this.buffer.append(data[i]);
/* 50 */       if (data[i] == '\n') {
/* 51 */         flushBuffer();
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void flush() {
/* 58 */     if (this.buffer.length() > 0) {
/* 59 */       flushBuffer();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 65 */     flush();
/*    */   }
/*    */   
/*    */   private void flushBuffer() {
/* 69 */     String str = this.buffer.toString();
/* 70 */     this.buffer.setLength(0);
/* 71 */     SwingUtilities.invokeLater(new ConsoleWrite(this.textArea, str));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\shell\ConsoleWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */