/*    */ package com.jediterm.terminal.emulator;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.jediterm.terminal.TerminalDataStream;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SystemCommandSequence
/*    */ {
/* 14 */   private final List<Object> myArgs = Lists.newArrayList();
/*    */   
/* 16 */   private final StringBuilder mySequenceString = new StringBuilder();
/*    */   
/*    */   public SystemCommandSequence(TerminalDataStream dataStream) throws IOException {
/* 19 */     readSystemCommandSequence(dataStream);
/*    */   }
/*    */   
/*    */   private void readSystemCommandSequence(TerminalDataStream stream) throws IOException {
/* 23 */     boolean isNumber = true;
/* 24 */     int number = 0;
/* 25 */     StringBuilder string = new StringBuilder();
/*    */     
/*    */     while (true) {
/* 28 */       char b = stream.getChar();
/* 29 */       this.mySequenceString.append(b);
/*    */       
/* 31 */       if (b == ';' || isEnd(b)) {
/* 32 */         if (isTwoBytesEnd(b)) {
/* 33 */           string.delete(string.length() - 1, string.length());
/*    */         }
/* 35 */         if (isNumber) {
/* 36 */           this.myArgs.add(Integer.valueOf(number));
/*    */         } else {
/*    */           
/* 39 */           this.myArgs.add(string.toString());
/*    */         } 
/* 41 */         if (isEnd(b)) {
/*    */           break;
/*    */         }
/* 44 */         isNumber = true;
/* 45 */         number = 0;
/* 46 */         string = new StringBuilder(); continue;
/*    */       } 
/* 48 */       if (isNumber) {
/* 49 */         if ('0' <= b && b <= '9') {
/* 50 */           number = number * 10 + b - 48;
/*    */         } else {
/*    */           
/* 53 */           isNumber = false;
/*    */         } 
/* 55 */         string.append(b);
/*    */         continue;
/*    */       } 
/* 58 */       string.append(b);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private boolean isEnd(char b) {
/* 64 */     return (b == '\007' || b == '' || isTwoBytesEnd(b));
/*    */   }
/*    */   
/*    */   private boolean isTwoBytesEnd(char ch) {
/* 68 */     int len = this.mySequenceString.length();
/* 69 */     return (len >= 2 && this.mySequenceString.charAt(len - 2) == '\033' && ch == '\\');
/*    */   }
/*    */   
/*    */   public String getStringAt(int i) {
/* 73 */     if (i >= this.myArgs.size()) {
/* 74 */       return null;
/*    */     }
/* 76 */     Object val = this.myArgs.get(i);
/* 77 */     return (val instanceof String) ? (String)val : null;
/*    */   }
/*    */   
/*    */   public Integer getIntAt(int i) {
/* 81 */     if (i >= this.myArgs.size()) {
/* 82 */       return null;
/*    */     }
/* 84 */     Object val = this.myArgs.get(i);
/* 85 */     return (val instanceof Integer) ? (Integer)val : null;
/*    */   }
/*    */   
/*    */   public String getSequenceString() {
/* 89 */     return this.mySequenceString.toString();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\emulator\SystemCommandSequence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */