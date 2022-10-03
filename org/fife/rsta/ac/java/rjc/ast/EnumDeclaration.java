/*    */ package org.fife.rsta.ac.java.rjc.ast;
/*    */ 
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
/*    */ public class EnumDeclaration
/*    */   extends AbstractTypeDeclarationNode
/*    */ {
/*    */   public EnumDeclaration(Scanner s, int offs, String name) {
/* 22 */     super(name, s.createOffset(offs), s.createOffset(offs + name.length()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTypeString() {
/* 28 */     return "enum";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\ast\EnumDeclaration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */