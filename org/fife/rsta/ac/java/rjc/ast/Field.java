/*    */ package org.fife.rsta.ac.java.rjc.ast;
/*    */ 
/*    */ import org.fife.rsta.ac.java.rjc.lang.Modifiers;
/*    */ import org.fife.rsta.ac.java.rjc.lang.Type;
/*    */ import org.fife.rsta.ac.java.rjc.lexer.Offset;
/*    */ import org.fife.rsta.ac.java.rjc.lexer.Scanner;
/*    */ import org.fife.rsta.ac.java.rjc.lexer.Token;
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
/*    */ public class Field
/*    */   extends AbstractMember
/*    */ {
/*    */   private Modifiers modifiers;
/*    */   private Type type;
/*    */   private boolean deprecated;
/*    */   private String docComment;
/*    */   
/*    */   public Field(Scanner s, Modifiers modifiers, Type type, Token t) {
/* 28 */     super(t.getLexeme(), s.createOffset(t.getOffset()));
/* 29 */     setDeclarationEndOffset(s.createOffset(t.getOffset() + t.getLength()));
/* 30 */     if (modifiers == null) {
/* 31 */       modifiers = new Modifiers();
/*    */     }
/* 33 */     this.modifiers = modifiers;
/* 34 */     this.type = type;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDocComment() {
/* 40 */     return this.docComment;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Modifiers getModifiers() {
/* 46 */     return this.modifiers;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Type getType() {
/* 52 */     return this.type;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isDeprecated() {
/* 58 */     return this.deprecated;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDeprecated(boolean deprecated) {
/* 63 */     this.deprecated = deprecated;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDocComment(String comment) {
/* 68 */     this.docComment = comment;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\ast\Field.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */