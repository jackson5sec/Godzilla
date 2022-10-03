/*    */ package com.kichik.pecoff4j;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BoundImportDirectoryTable
/*    */ {
/* 16 */   private List<BoundImport> imports = new ArrayList<>();
/*    */   
/*    */   public void add(BoundImport bi) {
/* 19 */     this.imports.add(bi);
/*    */   }
/*    */   
/*    */   public int size() {
/* 23 */     return this.imports.size();
/*    */   }
/*    */   
/*    */   public BoundImport get(int index) {
/* 27 */     return this.imports.get(index);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\BoundImportDirectoryTable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */