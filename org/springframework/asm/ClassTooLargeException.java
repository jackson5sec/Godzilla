/*    */ package org.springframework.asm;
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
/*    */ public final class ClassTooLargeException
/*    */   extends IndexOutOfBoundsException
/*    */ {
/*    */   private static final long serialVersionUID = 160715609518896765L;
/*    */   private final String className;
/*    */   private final int constantPoolCount;
/*    */   
/*    */   public ClassTooLargeException(String className, int constantPoolCount) {
/* 49 */     super("Class too large: " + className);
/* 50 */     this.className = className;
/* 51 */     this.constantPoolCount = constantPoolCount;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getClassName() {
/* 60 */     return this.className;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getConstantPoolCount() {
/* 69 */     return this.constantPoolCount;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\asm\ClassTooLargeException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */