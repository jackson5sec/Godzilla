/*    */ package org.fife.rsta.ac.java.rjc.ast;
/*    */ 
/*    */ import org.fife.rsta.ac.java.rjc.lexer.Offset;
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
/*    */ abstract class AbstractASTNode
/*    */   implements ASTNode
/*    */ {
/*    */   private String name;
/*    */   private Offset startOffs;
/*    */   private Offset endOffs;
/*    */   
/*    */   protected AbstractASTNode(String name, Offset start) {
/* 30 */     this(name, start, null);
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractASTNode(String name, Offset start, Offset end) {
/* 35 */     this.name = name;
/* 36 */     this.startOffs = start;
/* 37 */     this.endOffs = end;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 46 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNameEndOffset() {
/* 55 */     return (this.endOffs != null) ? this.endOffs.getOffset() : Integer.MAX_VALUE;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNameStartOffset() {
/* 64 */     return (this.startOffs != null) ? this.startOffs.getOffset() : 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDeclarationEndOffset(Offset end) {
/* 69 */     this.endOffs = end;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void setDeclarationOffsets(Offset start, Offset end) {
/* 80 */     this.startOffs = start;
/* 81 */     this.endOffs = end;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 93 */     return getName();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\ast\AbstractASTNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */