/*    */ package org.fife.rsta.ac.java.rjc.lang;
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
/*    */ public class TypeArgument
/*    */ {
/*    */   public static final int NOTHING = 0;
/*    */   public static final int EXTENDS = 1;
/*    */   public static final int SUPER = 2;
/*    */   private Type type;
/*    */   private int doesExtend;
/*    */   private Type otherType;
/*    */   
/*    */   public TypeArgument(Type type) {
/* 26 */     this.type = type;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeArgument(Type type, int doesExtend, Type otherType) {
/* 31 */     if (doesExtend < 0 || doesExtend > 2) {
/* 32 */       throw new IllegalArgumentException("Illegal doesExtend: " + doesExtend);
/*    */     }
/* 34 */     this.type = type;
/* 35 */     this.doesExtend = doesExtend;
/* 36 */     this.otherType = otherType;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 42 */     StringBuilder sb = new StringBuilder();
/* 43 */     if (this.type == null) {
/* 44 */       sb.append('?');
/*    */     } else {
/*    */       
/* 47 */       sb.append(this.type.toString());
/*    */     } 
/* 49 */     if (this.doesExtend == 1) {
/* 50 */       sb.append(" extends ");
/* 51 */       sb.append(this.otherType.toString());
/*    */     }
/* 53 */     else if (this.doesExtend == 2) {
/* 54 */       sb.append(" super ");
/* 55 */       sb.append(this.otherType.toString());
/*    */     } 
/* 57 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\lang\TypeArgument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */