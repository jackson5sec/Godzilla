/*    */ package org.fife.rsta.ac.common;
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
/*    */ public class VariableDeclaration
/*    */ {
/*    */   private String type;
/*    */   private String name;
/*    */   private int offset;
/*    */   
/*    */   public VariableDeclaration(String name, int offset) {
/* 33 */     this(null, name, offset);
/*    */   }
/*    */ 
/*    */   
/*    */   public VariableDeclaration(String type, String name, int offset) {
/* 38 */     this.type = type;
/* 39 */     this.name = name;
/* 40 */     this.offset = offset;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 45 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getOffset() {
/* 50 */     return this.offset;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getType() {
/* 60 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\common\VariableDeclaration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */