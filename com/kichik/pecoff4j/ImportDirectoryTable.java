/*    */ package com.kichik.pecoff4j;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ImportDirectoryTable
/*    */ {
/* 15 */   private ArrayList imports = new ArrayList();
/*    */   
/*    */   public void add(ImportEntry entry) {
/* 18 */     this.imports.add(entry);
/*    */   }
/*    */   
/*    */   public int size() {
/* 22 */     return this.imports.size();
/*    */   }
/*    */   
/*    */   public ImportEntry getEntry(int index) {
/* 26 */     return this.imports.get(index);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\ImportDirectoryTable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */