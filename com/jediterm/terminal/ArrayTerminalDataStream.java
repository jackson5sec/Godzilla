/*    */ package com.jediterm.terminal;
/*    */ 
/*    */ import com.jediterm.terminal.util.CharUtils;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ArrayTerminalDataStream
/*    */   implements TerminalDataStream
/*    */ {
/*    */   protected char[] myBuf;
/*    */   protected int myOffset;
/*    */   protected int myLength;
/*    */   
/*    */   public ArrayTerminalDataStream(char[] buf, int offset, int length) {
/* 18 */     this.myBuf = buf;
/* 19 */     this.myOffset = offset;
/* 20 */     this.myLength = length;
/*    */   }
/*    */   
/*    */   public ArrayTerminalDataStream(char[] buf) {
/* 24 */     this(buf, 0, buf.length);
/*    */   }
/*    */   
/*    */   public char getChar() throws IOException {
/* 28 */     if (this.myLength == 0) {
/* 29 */       throw new TerminalDataStream.EOF();
/*    */     }
/*    */     
/* 32 */     this.myLength--;
/*    */     
/* 34 */     return this.myBuf[this.myOffset++];
/*    */   }
/*    */   
/*    */   public void pushChar(char c) throws TerminalDataStream.EOF {
/* 38 */     if (this.myOffset == 0) {
/*    */       char[] newBuf;
/*    */ 
/*    */       
/* 42 */       if (this.myBuf.length - this.myLength == 0) {
/* 43 */         newBuf = new char[this.myBuf.length + 1];
/*    */       } else {
/*    */         
/* 46 */         newBuf = this.myBuf;
/*    */       } 
/* 48 */       this.myOffset = newBuf.length - this.myLength;
/* 49 */       System.arraycopy(this.myBuf, 0, newBuf, this.myOffset, this.myLength);
/* 50 */       this.myBuf = newBuf;
/*    */     } 
/*    */     
/* 53 */     this.myLength++;
/* 54 */     this.myBuf[--this.myOffset] = c;
/*    */   }
/*    */   
/*    */   public String readNonControlCharacters(int maxChars) throws IOException {
/* 58 */     String nonControlCharacters = CharUtils.getNonControlCharacters(maxChars, this.myBuf, this.myOffset, this.myLength);
/*    */     
/* 60 */     this.myOffset += nonControlCharacters.length();
/* 61 */     this.myLength -= nonControlCharacters.length();
/*    */     
/* 63 */     return nonControlCharacters;
/*    */   }
/*    */   
/*    */   public void pushBackBuffer(char[] bytes, int length) throws TerminalDataStream.EOF {
/* 67 */     for (int i = length - 1; i >= 0; i--) {
/* 68 */       pushChar(bytes[i]);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 74 */     return (this.myLength == 0);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\jediterm\terminal\ArrayTerminalDataStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */