/*    */ package com.httpProxy.server;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class ByteUtil {
/*    */   public static byte[] readNextLine(InputStream inputStream, boolean appendCRLF) {
/*  8 */     byte last = 0;
/*  9 */     ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
/*    */     
/* 11 */     byte current = 0;
/*    */     
/*    */     try {
/* 14 */       while ((current = (byte)inputStream.read()) != -1) {
/* 15 */         if (current == 13) {
/* 16 */           last = current;
/*    */           continue;
/*    */         } 
/* 19 */         if (last == 13 && current == 10) {
/* 20 */           if (appendCRLF) {
/* 21 */             outputStream.write(13);
/* 22 */             outputStream.write(10);
/*    */           } 
/*    */           break;
/*    */         } 
/* 26 */         if (last == 13) {
/* 27 */           outputStream.write(last);
/*    */         } else {
/* 29 */           outputStream.write(current);
/*    */         } 
/* 31 */         last = current;
/*    */       } 
/* 33 */     } catch (Exception e) {
/* 34 */       throw new RuntimeException(e);
/*    */     } 
/* 36 */     return outputStream.toByteArray();
/*    */   }
/*    */   public static byte[] readNextLine(InputStream inputStream) {
/* 39 */     return readNextLine(inputStream, false);
/*    */   }
/*    */   public static byte[] readInputStream(InputStream inputStream) {
/* 42 */     byte[] temp = new byte[5120];
/* 43 */     int readOneNum = 0;
/* 44 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*    */     try {
/* 46 */       while ((readOneNum = inputStream.read(temp)) != -1) {
/* 47 */         bos.write(temp, 0, readOneNum);
/*    */       }
/* 49 */     } catch (Exception e) {
/* 50 */       if (bos.size() == 0) {
/* 51 */         throw new RuntimeException(e);
/*    */       }
/*    */     } 
/* 54 */     return bos.toByteArray();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\httpProxy\server\ByteUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */