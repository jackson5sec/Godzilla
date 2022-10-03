/*     */ package com.kitfox.svg.xml.cpx;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CPXTest
/*     */ {
/*     */   public CPXTest() {
/*  54 */     writeTest();
/*  55 */     readTest();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeTest() {
/*     */     try {
/*  62 */       InputStream is = CPXTest.class.getResourceAsStream("/data/readme.txt");
/*     */ 
/*     */       
/*  65 */       FileOutputStream fout = new FileOutputStream("C:\\tmp\\cpxFile.cpx");
/*  66 */       CPXOutputStream cout = new CPXOutputStream(fout);
/*     */       
/*  68 */       byte[] buffer = new byte[1024];
/*     */       int numBytes;
/*  70 */       while ((numBytes = is.read(buffer)) != -1)
/*     */       {
/*  72 */         cout.write(buffer, 0, numBytes);
/*     */       }
/*  74 */       cout.close();
/*     */     }
/*  76 */     catch (Exception e) {
/*     */       
/*  78 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, (String)null, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void readTest() {
/*     */     try {
/*  88 */       FileInputStream is = new FileInputStream("C:\\tmp\\cpxFile.cpx");
/*  89 */       CPXInputStream cin = new CPXInputStream(is);
/*     */       
/*  91 */       BufferedReader br = new BufferedReader(new InputStreamReader(cin));
/*     */       String line;
/*  93 */       while ((line = br.readLine()) != null)
/*     */       {
/*  95 */         System.err.println(line);
/*     */       }
/*     */     }
/*  98 */     catch (Exception e) {
/*     */       
/* 100 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, (String)null, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/* 109 */     new CPXTest();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\xml\cpx\CPXTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */