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
/*    */ public class ResourceDirectory
/*    */   extends DataObject
/*    */ {
/*    */   private ResourceDirectoryTable table;
/* 19 */   private List<ResourceEntry> entries = new ArrayList<>();
/*    */   
/*    */   public ResourceDirectoryTable getTable() {
/* 22 */     return this.table;
/*    */   }
/*    */   
/*    */   public void setTable(ResourceDirectoryTable table) {
/* 26 */     this.table = table;
/*    */   }
/*    */   
/*    */   public void add(ResourceEntry entry) {
/* 30 */     this.entries.add(entry);
/*    */   }
/*    */   
/*    */   public ResourceEntry get(int index) {
/* 34 */     return this.entries.get(index);
/*    */   }
/*    */   
/*    */   public int size() {
/* 38 */     return this.entries.size();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\ResourceDirectory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */