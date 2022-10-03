/*    */ package com.kichik.pecoff4j.util;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FilenameFilter;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.io.Reader;
/*    */ import java.io.Writer;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IO
/*    */ {
/*    */   public static byte[] toBytes(File f) throws IOException {
/* 26 */     byte[] b = new byte[(int)f.length()];
/* 27 */     FileInputStream fis = new FileInputStream(f);
/* 28 */     fis.read(b);
/* 29 */     return b;
/*    */   }
/*    */   
/*    */   public static byte[] toBytes(InputStream is) throws IOException {
/* 33 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 34 */     copy(is, bos, true);
/* 35 */     return bos.toByteArray();
/*    */   }
/*    */ 
/*    */   
/*    */   public static void findFiles(File dir, FilenameFilter filter, FindFilesCallback callback) {
/* 40 */     File[] f = dir.listFiles();
/* 41 */     for (File fs : f) {
/* 42 */       if (fs.isDirectory()) {
/* 43 */         findFiles(fs, filter, callback);
/* 44 */       } else if (filter == null || filter.accept(dir, fs.getName())) {
/* 45 */         callback.fileFound(fs);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public static File[] findFiles(File dir, FilenameFilter filter) {
/* 51 */     Set<File> files = new HashSet<>();
/* 52 */     findFiles(dir, filter, files);
/* 53 */     return files.<File>toArray(new File[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   private static void findFiles(File dir, FilenameFilter filter, Set<File> files) {
/* 58 */     File[] f = dir.listFiles();
/* 59 */     if (f == null) {
/*    */       return;
/*    */     }
/*    */     
/* 63 */     for (File ff : f) {
/* 64 */       if (ff.isDirectory()) {
/* 65 */         findFiles(ff, filter, files);
/*    */       }
/* 67 */       else if (filter.accept(ff.getParentFile(), ff.getName())) {
/* 68 */         files.add(ff);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static void copy(Reader r, Writer w, boolean close) throws IOException {
/* 75 */     char[] buf = new char[4096];
/* 76 */     int len = 0;
/* 77 */     while ((len = r.read(buf)) > 0) {
/* 78 */       w.write(buf, 0, len);
/*    */     }
/* 80 */     if (close) {
/* 81 */       r.close();
/* 82 */       w.close();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static void copy(InputStream r, OutputStream w, boolean close) throws IOException {
/* 88 */     byte[] buf = new byte[4096];
/* 89 */     int len = 0;
/* 90 */     while ((len = r.read(buf)) > 0) {
/* 91 */       w.write(buf, 0, len);
/*    */     }
/* 93 */     if (close) {
/* 94 */       r.close();
/* 95 */       w.close();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4\\util\IO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */