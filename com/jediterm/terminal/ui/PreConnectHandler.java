/*    */ package com.jediterm.terminal.ui;
/*    */ 
/*    */ import com.jediterm.terminal.Questioner;
/*    */ import com.jediterm.terminal.Terminal;
/*    */ import java.awt.event.KeyEvent;
/*    */ import java.awt.event.KeyListener;
/*    */ 
/*    */ public class PreConnectHandler
/*    */   implements Questioner, KeyListener {
/* 10 */   private Object mySync = new Object();
/*    */   private Terminal myTerminal;
/*    */   private StringBuffer myAnswer;
/*    */   private boolean myVisible;
/*    */   
/*    */   public PreConnectHandler(Terminal terminal) {
/* 16 */     this.myTerminal = terminal;
/* 17 */     this.myVisible = true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String questionHidden(String question) {
/* 23 */     this.myVisible = false;
/* 24 */     String answer = questionVisible(question, null);
/* 25 */     this.myVisible = true;
/* 26 */     return answer;
/*    */   }
/*    */   
/*    */   public String questionVisible(String question, String defValue) {
/* 30 */     synchronized (this.mySync) {
/* 31 */       this.myTerminal.writeUnwrappedString(question);
/* 32 */       this.myAnswer = new StringBuffer();
/* 33 */       if (defValue != null) {
/* 34 */         this.myAnswer.append(defValue);
/* 35 */         this.myTerminal.writeUnwrappedString(defValue);
/*    */       } 
/*    */       try {
/* 38 */         this.mySync.wait();
/* 39 */       } catch (InterruptedException e) {
/* 40 */         e.printStackTrace();
/*    */       } 
/* 42 */       String answerStr = this.myAnswer.toString();
/* 43 */       this.myAnswer = null;
/* 44 */       return answerStr;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void showMessage(String message) {
/* 49 */     this.myTerminal.writeUnwrappedString(message);
/* 50 */     this.myTerminal.nextLine();
/*    */   }
/*    */   
/*    */   public void keyPressed(KeyEvent e) {
/* 54 */     if (this.myAnswer == null)
/* 55 */       return;  synchronized (this.mySync) {
/* 56 */       boolean release = false;
/*    */       
/* 58 */       switch (e.getKeyCode()) {
/*    */         case 8:
/* 60 */           if (this.myAnswer.length() > 0) {
/* 61 */             this.myTerminal.backspace();
/* 62 */             this.myTerminal.eraseInLine(0);
/* 63 */             this.myAnswer.deleteCharAt(this.myAnswer.length() - 1);
/*    */           } 
/*    */           break;
/*    */         case 10:
/* 67 */           this.myTerminal.nextLine();
/* 68 */           release = true;
/*    */           break;
/*    */       } 
/*    */       
/* 72 */       if (release) this.mySync.notifyAll();
/*    */     
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void keyReleased(KeyEvent e) {}
/*    */ 
/*    */   
/*    */   public void keyTyped(KeyEvent e) {
/* 82 */     if (this.myAnswer == null)
/* 83 */       return;  char c = e.getKeyChar();
/* 84 */     if (Character.getType(c) != 15) {
/* 85 */       if (this.myVisible) this.myTerminal.writeCharacters(Character.toString(c)); 
/* 86 */       this.myAnswer.append(c);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\termina\\ui\PreConnectHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */