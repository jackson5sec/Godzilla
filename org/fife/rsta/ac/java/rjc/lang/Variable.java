/*    */ package org.fife.rsta.ac.java.rjc.lang;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Variable
/*    */ {
/*    */   private boolean isFinal;
/*    */   private Type type;
/*    */   private Token name;
/*    */   
/*    */   public Variable(boolean isFinal, Type type, Token name) {
/* 30 */     this.isFinal = isFinal;
/* 31 */     this.type = type;
/* 32 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 37 */     return this.name.getLexeme();
/*    */   }
/*    */ 
/*    */   
/*    */   public Type getType() {
/* 42 */     return this.type;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFinal() {
/* 47 */     return this.isFinal;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\lang\Variable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */