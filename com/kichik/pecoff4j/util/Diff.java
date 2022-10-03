/*    */ package com.kichik.pecoff4j.util;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.util.Arrays;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Diff
/*    */ {
/*    */   public static boolean equals(File f1, File f2) throws IOException {
/* 18 */     return equals(IO.toBytes(f1), IO.toBytes(f2));
/*    */   }
/*    */   
/*    */   public static boolean equals(byte[] b1, byte[] b2) {
/* 22 */     return Arrays.equals(b1, b2);
/*    */   }
/*    */   
/*    */   public static boolean equals(byte[] b1, byte[] b2, boolean ignoreLength) {
/* 26 */     if (ignoreLength) {
/* 27 */       for (int i = 0; i < b1.length && i < b2.length; i++) {
/* 28 */         if (b1[i] != b2[i]) {
/* 29 */           return false;
/*    */         }
/*    */       } 
/* 32 */       return true;
/*    */     } 
/* 34 */     return Arrays.equals(b1, b2);
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean findDiff(byte[] b1, byte[] b2, boolean ignoreLength) {
/* 39 */     boolean diff = false;
/* 40 */     if (b1.length != b2.length && !ignoreLength) {
/* 41 */       System.out.println("Different lengths: " + 
/* 42 */           Integer.toHexString(b1.length) + ", " + 
/* 43 */           Integer.toHexString(b2.length));
/* 44 */       diff = true;
/*    */     } 
/* 46 */     for (int i = 0; i < b1.length && i < b2.length; i++) {
/* 47 */       if (b1[i] != b2[i]) {
/* 48 */         int p = i;
/* 49 */         if (p < 0)
/* 50 */           p = 0; 
/* 51 */         System.out.println("Diff at " + Integer.toHexString(i));
/* 52 */         HexDump.dump(b1, p, 100);
/* 53 */         System.out.println("-----");
/* 54 */         HexDump.dump(b2, p, 100);
/* 55 */         return true;
/*    */       } 
/*    */     } 
/*    */     
/* 59 */     return diff;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4\\util\Diff.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */