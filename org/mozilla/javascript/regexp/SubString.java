/*    */ package org.mozilla.javascript.regexp;
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
/*    */ public class SubString
/*    */ {
/*    */   public SubString() {}
/*    */   
/*    */   public SubString(String str) {
/* 20 */     this.str = str;
/* 21 */     this.index = 0;
/* 22 */     this.length = str.length();
/*    */   }
/*    */ 
/*    */   
/*    */   public SubString(String source, int start, int len) {
/* 27 */     this.str = source;
/* 28 */     this.index = start;
/* 29 */     this.length = len;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 34 */     return (this.str == null) ? "" : this.str.substring(this.index, this.index + this.length);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public static final SubString emptySubString = new SubString();
/*    */   String str;
/*    */   int index;
/*    */   int length;
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\regexp\SubString.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */