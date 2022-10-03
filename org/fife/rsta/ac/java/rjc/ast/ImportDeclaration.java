/*    */ package org.fife.rsta.ac.java.rjc.ast;
/*    */ 
/*    */ import org.fife.rsta.ac.java.rjc.lexer.Offset;
/*    */ import org.fife.rsta.ac.java.rjc.lexer.Scanner;
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
/*    */ public class ImportDeclaration
/*    */   extends AbstractASTNode
/*    */ {
/*    */   private boolean isStatic;
/*    */   
/*    */   public ImportDeclaration(Scanner s, int offs, String info, boolean isStatic) {
/* 28 */     super(info, s.createOffset(offs), s.createOffset(offs + info.length()));
/* 29 */     setStatic(isStatic);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isStatic() {
/* 34 */     return this.isStatic;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isWildcard() {
/* 39 */     return getName().endsWith(".*");
/*    */   }
/*    */ 
/*    */   
/*    */   public void setStatic(boolean isStatic) {
/* 44 */     this.isStatic = isStatic;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\ast\ImportDeclaration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */