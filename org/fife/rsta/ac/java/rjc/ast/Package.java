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
/*    */ public class Package
/*    */   extends AbstractASTNode
/*    */ {
/*    */   public Package(Scanner s, int offs, String pkg) {
/* 20 */     super(pkg, s.createOffset(offs), s.createOffset(offs + pkg.length()));
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\ast\Package.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */