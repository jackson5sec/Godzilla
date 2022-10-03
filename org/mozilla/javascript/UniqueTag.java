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
/*    */ public final class UniqueTag
/*    */   implements Serializable
/*    */ {
/*    */   static final long serialVersionUID = -4320556826714577259L;
/*    */   private static final int ID_NOT_FOUND = 1;
/*    */   private static final int ID_NULL_VALUE = 2;
/*    */   private static final int ID_DOUBLE_MARK = 3;
/* 32 */   public static final UniqueTag NOT_FOUND = new UniqueTag(1);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 38 */   public static final UniqueTag NULL_VALUE = new UniqueTag(2);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public static final UniqueTag DOUBLE_MARK = new UniqueTag(3);
/*    */   
/*    */   private final int tagId;
/*    */ 
/*    */   
/*    */   private UniqueTag(int tagId) {
/* 51 */     this.tagId = tagId;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object readResolve() {
/* 56 */     switch (this.tagId) {
/*    */       case 1:
/* 58 */         return NOT_FOUND;
/*    */       case 2:
/* 60 */         return NULL_VALUE;
/*    */       case 3:
/* 62 */         return DOUBLE_MARK;
/*    */     } 
/* 64 */     throw new IllegalStateException(String.valueOf(this.tagId));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/*    */     String name;
/* 72 */     switch (this.tagId) {
/*    */       case 1:
/* 74 */         name = "NOT_FOUND";
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
/* 85 */         return super.toString() + ": " + name;case 2: name = "NULL_VALUE"; return super.toString() + ": " + name;case 3: name = "DOUBLE_MARK"; return super.toString() + ": " + name;
/*    */     } 
/*    */     throw Kit.codeBug();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\UniqueTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */