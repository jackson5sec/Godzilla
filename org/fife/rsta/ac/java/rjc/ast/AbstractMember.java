/*    */ package org.fife.rsta.ac.java.rjc.ast;
/*    */ 
/*    */ import org.fife.rsta.ac.java.rjc.lang.Modifiers;
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
/*    */ abstract class AbstractMember
/*    */   extends AbstractASTNode
/*    */   implements Member
/*    */ {
/*    */   private TypeDeclaration parentTypeDec;
/*    */   
/*    */   protected AbstractMember(String name, Offset start) {
/* 29 */     super(name, start);
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractMember(String name, Offset start, Offset end) {
/* 34 */     super(name, start, end);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TypeDeclaration getParentTypeDeclaration() {
/* 40 */     return this.parentTypeDec;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isStatic() {
/* 49 */     Modifiers modifiers = getModifiers();
/* 50 */     return (modifiers != null && modifiers.isStatic());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setParentTypeDeclaration(TypeDeclaration dec) {
/* 59 */     if (dec == null) {
/* 60 */       throw new InternalError("Parent TypeDeclaration cannot be null");
/*    */     }
/* 62 */     this.parentTypeDec = dec;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\ast\AbstractMember.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */