/*    */ package org.apache.log4j.spi;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Vector;
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
/*    */ 
/*    */ 
/*    */ class VectorWriter
/*    */   extends PrintWriter
/*    */ {
/*    */   private Vector v;
/*    */   
/*    */   VectorWriter() {
/* 36 */     super(new NullWriter());
/* 37 */     this.v = new Vector();
/*    */   }
/*    */   
/*    */   public void print(Object o) {
/* 41 */     this.v.addElement(String.valueOf(o));
/*    */   }
/*    */   
/*    */   public void print(char[] chars) {
/* 45 */     this.v.addElement(new String(chars));
/*    */   }
/*    */   
/*    */   public void print(String s) {
/* 49 */     this.v.addElement(s);
/*    */   }
/*    */   
/*    */   public void println(Object o) {
/* 53 */     this.v.addElement(String.valueOf(o));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void println(char[] chars) {
/* 60 */     this.v.addElement(new String(chars));
/*    */   }
/*    */ 
/*    */   
/*    */   public void println(String s) {
/* 65 */     this.v.addElement(s);
/*    */   }
/*    */   
/*    */   public void write(char[] chars) {
/* 69 */     this.v.addElement(new String(chars));
/*    */   }
/*    */   
/*    */   public void write(char[] chars, int off, int len) {
/* 73 */     this.v.addElement(new String(chars, off, len));
/*    */   }
/*    */   
/*    */   public void write(String s, int off, int len) {
/* 77 */     this.v.addElement(s.substring(off, off + len));
/*    */   }
/*    */   
/*    */   public void write(String s) {
/* 81 */     this.v.addElement(s);
/*    */   }
/*    */   
/*    */   public String[] toStringArray() {
/* 85 */     int len = this.v.size();
/* 86 */     String[] sa = new String[len];
/* 87 */     for (int i = 0; i < len; i++) {
/* 88 */       sa[i] = this.v.elementAt(i);
/*    */     }
/* 90 */     return sa;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\apache\log4j\spi\VectorWriter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */