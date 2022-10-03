/*    */ package org.mozilla.javascript.tools.shell;
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
/*    */ class ConsoleWrite
/*    */   implements Runnable
/*    */ {
/*    */   private ConsoleTextArea textArea;
/*    */   private String str;
/*    */   
/*    */   public ConsoleWrite(ConsoleTextArea textArea, String str) {
/* 20 */     this.textArea = textArea;
/* 21 */     this.str = str;
/*    */   }
/*    */   
/*    */   public void run() {
/* 25 */     this.textArea.write(this.str);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\shell\ConsoleWrite.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */