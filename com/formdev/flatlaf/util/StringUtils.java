/*    */ package com.formdev.flatlaf.util;
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
/*    */ public class StringUtils
/*    */ {
/*    */   public static boolean isEmpty(String string) {
/* 33 */     return (string == null || string.isEmpty());
/*    */   }
/*    */   
/*    */   public static String removeLeading(String string, String leading) {
/* 37 */     return string.startsWith(leading) ? string
/* 38 */       .substring(leading.length()) : string;
/*    */   }
/*    */ 
/*    */   
/*    */   public static String removeTrailing(String string, String trailing) {
/* 43 */     return string.endsWith(trailing) ? string
/* 44 */       .substring(0, string.length() - trailing.length()) : string;
/*    */   }
/*    */ 
/*    */   
/*    */   public static List<String> split(String str, char delim) {
/* 49 */     ArrayList<String> strs = new ArrayList<>();
/* 50 */     int delimIndex = str.indexOf(delim);
/* 51 */     int index = 0;
/* 52 */     while (delimIndex >= 0) {
/* 53 */       strs.add(str.substring(index, delimIndex));
/* 54 */       index = delimIndex + 1;
/* 55 */       delimIndex = str.indexOf(delim, index);
/*    */     } 
/* 57 */     strs.add(str.substring(index));
/*    */     
/* 59 */     return strs;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\formdev\flatla\\util\StringUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */