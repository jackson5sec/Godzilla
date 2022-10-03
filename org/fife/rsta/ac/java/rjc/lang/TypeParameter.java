/*    */ package org.fife.rsta.ac.java.rjc.lang;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TypeParameter
/*    */ {
/*    */   private Token name;
/*    */   private List<Type> bounds;
/*    */   
/*    */   public TypeParameter(Token name) {
/* 40 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addBound(Type bound) {
/* 45 */     if (this.bounds == null) {
/* 46 */       this.bounds = new ArrayList<>(1);
/*    */     }
/* 48 */     this.bounds.add(bound);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 53 */     return this.name.getLexeme();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\lang\TypeParameter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */