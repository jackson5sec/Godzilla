/*    */ package org.mozilla.javascript;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public class ConsString
/*    */   implements CharSequence, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -8432806714471372570L;
/*    */   private CharSequence s1;
/*    */   private CharSequence s2;
/*    */   private final int length;
/*    */   private int depth;
/*    */   
/*    */   public ConsString(CharSequence str1, CharSequence str2) {
/* 36 */     this.s1 = str1;
/* 37 */     this.s2 = str2;
/* 38 */     this.length = str1.length() + str2.length();
/* 39 */     this.depth = 1;
/* 40 */     if (str1 instanceof ConsString) {
/* 41 */       this.depth += ((ConsString)str1).depth;
/*    */     }
/* 43 */     if (str2 instanceof ConsString) {
/* 44 */       this.depth += ((ConsString)str2).depth;
/*    */     }
/*    */     
/* 47 */     if (this.depth > 2000) {
/* 48 */       flatten();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   private Object writeReplace() {
/* 54 */     return toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 59 */     return (this.depth == 0) ? (String)this.s1 : flatten();
/*    */   }
/*    */   
/*    */   private synchronized String flatten() {
/* 63 */     if (this.depth > 0) {
/* 64 */       StringBuilder b = new StringBuilder(this.length);
/* 65 */       appendTo(b);
/* 66 */       this.s1 = b.toString();
/* 67 */       this.s2 = "";
/* 68 */       this.depth = 0;
/*    */     } 
/* 70 */     return (String)this.s1;
/*    */   }
/*    */   
/*    */   private synchronized void appendTo(StringBuilder b) {
/* 74 */     appendFragment(this.s1, b);
/* 75 */     appendFragment(this.s2, b);
/*    */   }
/*    */   
/*    */   private static void appendFragment(CharSequence s, StringBuilder b) {
/* 79 */     if (s instanceof ConsString) {
/* 80 */       ((ConsString)s).appendTo(b);
/*    */     } else {
/* 82 */       b.append(s);
/*    */     } 
/*    */   }
/*    */   
/*    */   public int length() {
/* 87 */     return this.length;
/*    */   }
/*    */   
/*    */   public char charAt(int index) {
/* 91 */     String str = (this.depth == 0) ? (String)this.s1 : flatten();
/* 92 */     return str.charAt(index);
/*    */   }
/*    */   
/*    */   public CharSequence subSequence(int start, int end) {
/* 96 */     String str = (this.depth == 0) ? (String)this.s1 : flatten();
/* 97 */     return str.substring(start, end);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\ConsString.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */