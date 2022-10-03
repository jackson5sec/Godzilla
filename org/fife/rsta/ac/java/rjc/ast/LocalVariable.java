/*    */ package org.fife.rsta.ac.java.rjc.ast;
/*    */ 
/*    */ import org.fife.rsta.ac.java.rjc.lang.Type;
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
/*    */ 
/*    */ public class LocalVariable
/*    */   extends AbstractASTNode
/*    */ {
/*    */   private boolean isFinal;
/*    */   private Type type;
/*    */   
/*    */   public LocalVariable(Scanner s, boolean isFinal, Type type, int offs, String name) {
/* 31 */     super(name, s.createOffset(offs), s.createOffset(offs + name.length()));
/* 32 */     this.isFinal = isFinal;
/* 33 */     this.type = type;
/*    */   }
/*    */ 
/*    */   
/*    */   public Type getType() {
/* 38 */     return this.type;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFinal() {
/* 43 */     return this.isFinal;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\ast\LocalVariable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */