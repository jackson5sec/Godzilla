/*    */ package com.kichik.pecoff4j.resources;
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
/*    */ public class VarFileInfo
/*    */ {
/*    */   private String key;
/* 17 */   private List<String> names = new ArrayList<>();
/* 18 */   private List<String> values = new ArrayList<>();
/*    */   
/*    */   public String getKey() {
/* 21 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 25 */     this.key = key;
/*    */   }
/*    */   
/*    */   public int size() {
/* 29 */     return this.names.size();
/*    */   }
/*    */   
/*    */   public String getName(int index) {
/* 33 */     return this.names.get(index);
/*    */   }
/*    */   
/*    */   public String getValue(int index) {
/* 37 */     return this.values.get(index);
/*    */   }
/*    */   
/*    */   public void add(String name, String value) {
/* 41 */     this.names.add(name);
/* 42 */     this.values.add(value);
/*    */   }
/*    */   
/*    */   public void clear() {
/* 46 */     this.names.clear();
/* 47 */     this.values.clear();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\resources\VarFileInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */