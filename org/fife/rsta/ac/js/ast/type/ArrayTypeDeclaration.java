/*    */ package org.fife.rsta.ac.js.ast.type;
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
/*    */ public class ArrayTypeDeclaration
/*    */   extends TypeDeclaration
/*    */ {
/*    */   private TypeDeclaration arrayType;
/*    */   
/*    */   public ArrayTypeDeclaration(String pkg, String apiName, String jsName, boolean staticsOnly) {
/* 23 */     super(pkg, apiName, jsName, staticsOnly);
/*    */   }
/*    */ 
/*    */   
/*    */   public ArrayTypeDeclaration(String pkg, String apiName, String jsName) {
/* 28 */     super(pkg, apiName, jsName);
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeDeclaration getArrayType() {
/* 33 */     return this.arrayType;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setArrayType(TypeDeclaration containerType) {
/* 38 */     this.arrayType = containerType;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 44 */     boolean equals = super.equals(obj);
/*    */     
/* 46 */     if (equals) {
/*    */       
/* 48 */       ArrayTypeDeclaration objArrayType = (ArrayTypeDeclaration)obj;
/*    */       
/* 50 */       if (getArrayType() == null && objArrayType.getArrayType() == null) {
/* 51 */         return false;
/*    */       }
/*    */       
/* 54 */       if (getArrayType() == null && objArrayType.getArrayType() != null) {
/* 55 */         return false;
/*    */       }
/*    */       
/* 58 */       if (getArrayType() != null && objArrayType.getArrayType() == null) {
/* 59 */         return false;
/*    */       }
/*    */       
/* 62 */       return getArrayType().equals(((ArrayTypeDeclaration)obj)
/* 63 */           .getArrayType());
/*    */     } 
/*    */     
/* 66 */     return equals;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\rsta\ac\js\ast\type\ArrayTypeDeclaration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */