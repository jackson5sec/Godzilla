/*    */ package com.kichik.pecoff4j.resources;
/*    */ 
/*    */ import com.kichik.pecoff4j.util.Strings;
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
/*    */ 
/*    */ 
/*    */ public class StringFileInfo
/*    */ {
/*    */   private int length;
/*    */   private int valueLength;
/*    */   private int type;
/*    */   private String key;
/*    */   private int padding;
/* 24 */   private List<StringTable> tables = new ArrayList<>();
/*    */   
/*    */   public void add(StringTable table) {
/* 27 */     this.tables.add(table);
/*    */   }
/*    */   
/*    */   public int getCount() {
/* 31 */     return this.tables.size();
/*    */   }
/*    */   
/*    */   public StringTable getTable(int index) {
/* 35 */     return this.tables.get(index);
/*    */   }
/*    */   
/*    */   public int getLength() {
/* 39 */     return this.length;
/*    */   }
/*    */   
/*    */   public void setLength(int length) {
/* 43 */     this.length = length;
/*    */   }
/*    */   
/*    */   public int getValueLength() {
/* 47 */     return this.valueLength;
/*    */   }
/*    */   
/*    */   public void setValueLength(int valueLength) {
/* 51 */     this.valueLength = valueLength;
/*    */   }
/*    */   
/*    */   public int getType() {
/* 55 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(int type) {
/* 59 */     this.type = type;
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 63 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 67 */     this.key = key;
/*    */   }
/*    */   
/*    */   public int getPadding() {
/* 71 */     return this.padding;
/*    */   }
/*    */   
/*    */   public void setPadding(int padding) {
/* 75 */     this.padding = padding;
/*    */   }
/*    */   
/*    */   public int sizeOf() {
/* 79 */     int actualLength = 6 + this.padding + Strings.getUtf16Length(this.key);
/* 80 */     for (StringTable t : this.tables)
/* 81 */       actualLength += t.sizeOf(); 
/* 82 */     return actualLength;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\resources\StringFileInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */