/*    */ package com.kichik.pecoff4j.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HexDump
/*    */ {
/*    */   private static final int WIDTH = 20;
/*    */   
/*    */   public static void dump(byte[] data, int offset, int length) {
/* 16 */     int numRows = length / 20;
/* 17 */     for (int i = 0; i < numRows; i++) {
/* 18 */       dumpRow(data, offset + i * 20, 20);
/*    */     }
/* 20 */     int leftover = length % 20;
/* 21 */     if (leftover > 0) {
/* 22 */       dumpRow(data, offset + data.length - leftover, leftover);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void dump(byte[] data) {
/* 27 */     dump(data, 0, data.length);
/*    */   }
/*    */   
/*    */   private static void dumpRow(byte[] data, int start, int length) {
/* 31 */     StringBuilder sb = new StringBuilder(); int i;
/* 32 */     for (i = 0; i < length; i++) {
/* 33 */       String s = Integer.toHexString(data[start + i] & 0xFF);
/* 34 */       if (s.length() == 1) {
/* 35 */         sb.append("0");
/*    */       }
/* 37 */       sb.append(s);
/* 38 */       sb.append(" ");
/*    */     } 
/* 40 */     if (length < 20) {
/* 41 */       for (i = 0; i < 20 - length; i++) {
/* 42 */         sb.append("   ");
/*    */       }
/*    */     }
/* 45 */     for (i = 0; i < length; i++) {
/* 46 */       byte b = data[start + i];
/* 47 */       if (Character.isLetterOrDigit(b)) {
/* 48 */         sb.append(String.valueOf((char)b));
/*    */       } else {
/* 50 */         sb.append(".");
/*    */       } 
/*    */     } 
/* 53 */     System.out.println(sb.toString());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4\\util\HexDump.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */