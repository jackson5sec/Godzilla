/*    */ package org.springframework.cglib.core;
/*    */ 
/*    */ import org.springframework.asm.Type;
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
/*    */ public class Local
/*    */ {
/*    */   private Type type;
/*    */   private int index;
/*    */   
/*    */   public Local(int index, Type type) {
/* 26 */     this.type = type;
/* 27 */     this.index = index;
/*    */   }
/*    */   
/*    */   public int getIndex() {
/* 31 */     return this.index;
/*    */   }
/*    */   
/*    */   public Type getType() {
/* 35 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\cglib\core\Local.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */