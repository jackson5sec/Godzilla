/*    */ package org.springframework.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.security.MessageDigest;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class UpdateMessageDigestInputStream
/*    */   extends InputStream
/*    */ {
/*    */   public void updateMessageDigest(MessageDigest messageDigest) throws IOException {
/*    */     int data;
/* 41 */     while ((data = read()) != -1) {
/* 42 */       messageDigest.update((byte)data);
/*    */     }
/*    */   }
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
/*    */   public void updateMessageDigest(MessageDigest messageDigest, int len) throws IOException {
/* 56 */     int bytesRead = 0; int data;
/* 57 */     while (bytesRead < len && (data = read()) != -1) {
/* 58 */       messageDigest.update((byte)data);
/* 59 */       bytesRead++;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\UpdateMessageDigestInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */