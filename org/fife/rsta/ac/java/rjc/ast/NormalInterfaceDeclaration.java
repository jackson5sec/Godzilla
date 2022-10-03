/*    */ package org.fife.rsta.ac.java.rjc.ast;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
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
/*    */ public class NormalInterfaceDeclaration
/*    */   extends AbstractTypeDeclarationNode
/*    */ {
/*    */   private List<Type> extendedList;
/*    */   
/*    */   public NormalInterfaceDeclaration(Scanner s, int offs, String name) {
/* 38 */     super(name, s.createOffset(offs), s.createOffset(offs + name.length()));
/* 39 */     this.extendedList = new ArrayList<>(1);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addExtended(Type extended) {
/* 44 */     this.extendedList.add(extended);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getExtendedCount() {
/* 49 */     return this.extendedList.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<Type> getExtendedIterator() {
/* 54 */     return this.extendedList.iterator();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTypeString() {
/* 60 */     return "interface";
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\java\rjc\ast\NormalInterfaceDeclaration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */