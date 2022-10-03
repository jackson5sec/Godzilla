/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.file.Files;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class FileCopyUtils
/*     */ {
/*     */   public static final int BUFFER_SIZE = 4096;
/*     */   
/*     */   public static int copy(File in, File out) throws IOException {
/*  66 */     Assert.notNull(in, "No input File specified");
/*  67 */     Assert.notNull(out, "No output File specified");
/*  68 */     return copy(Files.newInputStream(in.toPath(), new java.nio.file.OpenOption[0]), Files.newOutputStream(out.toPath(), new java.nio.file.OpenOption[0]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copy(byte[] in, File out) throws IOException {
/*  78 */     Assert.notNull(in, "No input byte array specified");
/*  79 */     Assert.notNull(out, "No output File specified");
/*  80 */     copy(new ByteArrayInputStream(in), Files.newOutputStream(out.toPath(), new java.nio.file.OpenOption[0]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] copyToByteArray(File in) throws IOException {
/*  90 */     Assert.notNull(in, "No input File specified");
/*  91 */     return copyToByteArray(Files.newInputStream(in.toPath(), new java.nio.file.OpenOption[0]));
/*     */   }
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
/*     */   public static int copy(InputStream in, OutputStream out) throws IOException {
/* 108 */     Assert.notNull(in, "No InputStream specified");
/* 109 */     Assert.notNull(out, "No OutputStream specified");
/*     */     
/*     */     try {
/* 112 */       return StreamUtils.copy(in, out);
/*     */     } finally {
/*     */       
/* 115 */       close(in);
/* 116 */       close(out);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copy(byte[] in, OutputStream out) throws IOException {
/* 128 */     Assert.notNull(in, "No input byte array specified");
/* 129 */     Assert.notNull(out, "No OutputStream specified");
/*     */     
/*     */     try {
/* 132 */       out.write(in);
/*     */     } finally {
/*     */       
/* 135 */       close(out);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] copyToByteArray(@Nullable InputStream in) throws IOException {
/* 147 */     if (in == null) {
/* 148 */       return new byte[0];
/*     */     }
/*     */     
/* 151 */     ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
/* 152 */     copy(in, out);
/* 153 */     return out.toByteArray();
/*     */   }
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
/*     */   public static int copy(Reader in, Writer out) throws IOException {
/* 170 */     Assert.notNull(in, "No Reader specified");
/* 171 */     Assert.notNull(out, "No Writer specified");
/*     */     
/*     */     try {
/* 174 */       int charCount = 0;
/* 175 */       char[] buffer = new char[4096];
/*     */       int charsRead;
/* 177 */       while ((charsRead = in.read(buffer)) != -1) {
/* 178 */         out.write(buffer, 0, charsRead);
/* 179 */         charCount += charsRead;
/*     */       } 
/* 181 */       out.flush();
/* 182 */       return charCount;
/*     */     } finally {
/*     */       
/* 185 */       close(in);
/* 186 */       close(out);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copy(String in, Writer out) throws IOException {
/* 198 */     Assert.notNull(in, "No input String specified");
/* 199 */     Assert.notNull(out, "No Writer specified");
/*     */     
/*     */     try {
/* 202 */       out.write(in);
/*     */     } finally {
/*     */       
/* 205 */       close(out);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String copyToString(@Nullable Reader in) throws IOException {
/* 217 */     if (in == null) {
/* 218 */       return "";
/*     */     }
/*     */     
/* 221 */     StringWriter out = new StringWriter(4096);
/* 222 */     copy(in, out);
/* 223 */     return out.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void close(Closeable closeable) {
/*     */     try {
/* 233 */       closeable.close();
/*     */     }
/* 235 */     catch (IOException iOException) {}
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\FileCopyUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */