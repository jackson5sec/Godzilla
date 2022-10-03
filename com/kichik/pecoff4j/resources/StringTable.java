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
/*    */ public class StringTable
/*    */ {
/*    */   private int length;
/*    */   private int valueLength;
/*    */   private int type;
/*    */   private String key;
/*    */   private int padding;
/* 24 */   private List<StringPair> strings = new ArrayList<>();
/*    */   
/*    */   public void add(StringPair string) {
/* 27 */     this.strings.add(string);
/*    */   }
/*    */   
/*    */   public int getCount() {
/* 31 */     return this.strings.size();
/*    */   }
/*    */   
/*    */   public StringPair getString(int index) {
/* 35 */     return this.strings.get(index);
/*    */   }
/*    */   
/*    */   public int getLength() {
/* 39 */     return this.length;
/*    */   }
/*    */   
/*    */   public int getValueLength() {
/* 43 */     return this.valueLength;
/*    */   }
/*    */   
/*    */   public int getType() {
/* 47 */     return this.type;
/*    */   }
/*    */   
/*    */   public int getPadding() {
/* 51 */     return this.padding;
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 55 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 59 */     this.key = key;
/*    */   }
/*    */   
/*    */   public void setLength(int length) {
/* 63 */     this.length = length;
/*    */   }
/*    */   
/*    */   public void setValueLength(int valueLength) {
/* 67 */     this.valueLength = valueLength;
/*    */   }
/*    */   
/*    */   public void setType(int type) {
/* 71 */     this.type = type;
/*    */   }
/*    */   
/*    */   public void setPadding(int padding) {
/* 75 */     this.padding = padding;
/*    */   }
/*    */   
/*    */   public int sizeOf() {
/* 79 */     int actualLength = 6 + this.padding + Strings.getUtf16Length(this.key);
/* 80 */     for (StringPair s : this.strings)
/* 81 */       actualLength += s.sizeOf(); 
/* 82 */     return actualLength;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\resources\StringTable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */