/*    */ package com.kichik.pecoff4j;
/*    */ 
/*    */ import com.kichik.pecoff4j.util.DataObject;
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
/*    */ public class ImportDirectory
/*    */   extends DataObject
/*    */ {
/* 18 */   private List<ImportDirectoryEntry> entries = new ArrayList<>();
/* 19 */   private List<String> names = new ArrayList<>();
/* 20 */   private List<ImportDirectoryTable> nameTables = new ArrayList<>();
/* 21 */   private List<ImportDirectoryTable> addressTables = new ArrayList<>();
/*    */   
/*    */   public void add(ImportDirectoryEntry entry) {
/* 24 */     this.entries.add(entry);
/*    */   }
/*    */ 
/*    */   
/*    */   public void add(String name, ImportDirectoryTable names, ImportDirectoryTable addresses) {
/* 29 */     this.names.add(name);
/* 30 */     this.nameTables.add(names);
/* 31 */     this.addressTables.add(addresses);
/*    */   }
/*    */   
/*    */   public int size() {
/* 35 */     return this.entries.size();
/*    */   }
/*    */   
/*    */   public String getName(int index) {
/* 39 */     return this.names.get(index);
/*    */   }
/*    */   
/*    */   public ImportDirectoryTable getNameTable(int index) {
/* 43 */     return this.nameTables.get(index);
/*    */   }
/*    */   
/*    */   public ImportDirectoryTable getAddressTable(int index) {
/* 47 */     return this.addressTables.get(index);
/*    */   }
/*    */   
/*    */   public ImportDirectoryEntry getEntry(int index) {
/* 51 */     return this.entries.get(index);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\ImportDirectory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */