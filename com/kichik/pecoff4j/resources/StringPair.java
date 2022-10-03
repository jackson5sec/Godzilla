/*    */ package com.kichik.pecoff4j.resources;
/*    */ 
/*    */ import com.kichik.pecoff4j.util.Reflection;
/*    */ import com.kichik.pecoff4j.util.Strings;
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
/*    */ public class StringPair
/*    */ {
/*    */   private int length;
/*    */   private int valueLength;
/*    */   private int type;
/*    */   private String key;
/*    */   private String value;
/*    */   private int padding;
/*    */   
/*    */   public int getLength() {
/* 25 */     return this.length;
/*    */   }
/*    */   
/*    */   public void setLength(int length) {
/* 29 */     this.length = length;
/*    */   }
/*    */   
/*    */   public int getValueLength() {
/* 33 */     return this.valueLength;
/*    */   }
/*    */   
/*    */   public void setValueLength(int valueLength) {
/* 37 */     this.valueLength = valueLength;
/*    */   }
/*    */   
/*    */   public int getType() {
/* 41 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(int type) {
/* 45 */     this.type = type;
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 49 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 53 */     this.key = key;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 57 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(String value) {
/* 61 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 66 */     return Reflection.toString(this);
/*    */   }
/*    */   
/*    */   public int sizeOf() {
/* 70 */     return 6 + this.padding + Strings.getUtf16Length(this.key) + 
/* 71 */       Strings.getUtf16Length(this.value);
/*    */   }
/*    */   
/*    */   public int getPadding() {
/* 75 */     return this.padding;
/*    */   }
/*    */   
/*    */   public void setPadding(int padding) {
/* 79 */     this.padding = padding;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\resources\StringPair.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */