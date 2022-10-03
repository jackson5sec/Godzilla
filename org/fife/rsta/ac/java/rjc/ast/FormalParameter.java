/*    */ package org.fife.rsta.ac.java.rjc.ast;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.fife.rsta.ac.java.rjc.lang.Annotation;
/*    */ import org.fife.rsta.ac.java.rjc.lang.Type;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FormalParameter
/*    */   extends LocalVariable
/*    */ {
/*    */   private List<Annotation> annotations;
/*    */   
/*    */   public FormalParameter(Scanner s, boolean isFinal, Type type, int offs, String name, List<Annotation> annotations) {
/* 40 */     super(s, isFinal, type, offs, name);
/* 41 */     this.annotations = annotations;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getAnnotationCount() {
/* 46 */     return (this.annotations == null) ? 0 : this.annotations.size();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return getType() + " " + getName();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\ast\FormalParameter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */