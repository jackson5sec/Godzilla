/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
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
/*     */ public abstract class StreamUtils
/*     */ {
/*     */   public static final int BUFFER_SIZE = 4096;
/*  54 */   private static final byte[] EMPTY_CONTENT = new byte[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] copyToByteArray(@Nullable InputStream in) throws IOException {
/*  65 */     if (in == null) {
/*  66 */       return new byte[0];
/*     */     }
/*     */     
/*  69 */     ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
/*  70 */     copy(in, out);
/*  71 */     return out.toByteArray();
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
/*     */   public static String copyToString(@Nullable InputStream in, Charset charset) throws IOException {
/*  83 */     if (in == null) {
/*  84 */       return "";
/*     */     }
/*     */     
/*  87 */     StringBuilder out = new StringBuilder(4096);
/*  88 */     InputStreamReader reader = new InputStreamReader(in, charset);
/*  89 */     char[] buffer = new char[4096];
/*     */     int charsRead;
/*  91 */     while ((charsRead = reader.read(buffer)) != -1) {
/*  92 */       out.append(buffer, 0, charsRead);
/*     */     }
/*  94 */     return out.toString();
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
/*     */   public static String copyToString(ByteArrayOutputStream baos, Charset charset) {
/* 106 */     Assert.notNull(baos, "No ByteArrayOutputStream specified");
/* 107 */     Assert.notNull(charset, "No Charset specified");
/*     */     
/*     */     try {
/* 110 */       return baos.toString(charset.name());
/*     */     }
/* 112 */     catch (UnsupportedEncodingException ex) {
/*     */       
/* 114 */       throw new IllegalArgumentException("Invalid charset name: " + charset, ex);
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
/* 126 */     Assert.notNull(in, "No input byte array specified");
/* 127 */     Assert.notNull(out, "No OutputStream specified");
/*     */     
/* 129 */     out.write(in);
/* 130 */     out.flush();
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
/*     */   public static void copy(String in, Charset charset, OutputStream out) throws IOException {
/* 142 */     Assert.notNull(in, "No input String specified");
/* 143 */     Assert.notNull(charset, "No Charset specified");
/* 144 */     Assert.notNull(out, "No OutputStream specified");
/*     */     
/* 146 */     Writer writer = new OutputStreamWriter(out, charset);
/* 147 */     writer.write(in);
/* 148 */     writer.flush();
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
/*     */   public static int copy(InputStream in, OutputStream out) throws IOException {
/* 160 */     Assert.notNull(in, "No InputStream specified");
/* 161 */     Assert.notNull(out, "No OutputStream specified");
/*     */     
/* 163 */     int byteCount = 0;
/* 164 */     byte[] buffer = new byte[4096];
/*     */     int bytesRead;
/* 166 */     while ((bytesRead = in.read(buffer)) != -1) {
/* 167 */       out.write(buffer, 0, bytesRead);
/* 168 */       byteCount += bytesRead;
/*     */     } 
/* 170 */     out.flush();
/* 171 */     return byteCount;
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
/*     */   public static long copyRange(InputStream in, OutputStream out, long start, long end) throws IOException {
/* 188 */     Assert.notNull(in, "No InputStream specified");
/* 189 */     Assert.notNull(out, "No OutputStream specified");
/*     */     
/* 191 */     long skipped = in.skip(start);
/* 192 */     if (skipped < start) {
/* 193 */       throw new IOException("Skipped only " + skipped + " bytes out of " + start + " required");
/*     */     }
/*     */     
/* 196 */     long bytesToCopy = end - start + 1L;
/* 197 */     byte[] buffer = new byte[(int)Math.min(4096L, bytesToCopy)];
/* 198 */     while (bytesToCopy > 0L) {
/* 199 */       int bytesRead = in.read(buffer);
/* 200 */       if (bytesRead == -1) {
/*     */         break;
/*     */       }
/* 203 */       if (bytesRead <= bytesToCopy) {
/* 204 */         out.write(buffer, 0, bytesRead);
/* 205 */         bytesToCopy -= bytesRead;
/*     */         continue;
/*     */       } 
/* 208 */       out.write(buffer, 0, (int)bytesToCopy);
/* 209 */       bytesToCopy = 0L;
/*     */     } 
/*     */     
/* 212 */     return end - start + 1L - bytesToCopy;
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
/*     */   public static int drain(InputStream in) throws IOException {
/* 224 */     Assert.notNull(in, "No InputStream specified");
/* 225 */     byte[] buffer = new byte[4096];
/* 226 */     int bytesRead = -1;
/* 227 */     int byteCount = 0;
/* 228 */     while ((bytesRead = in.read(buffer)) != -1) {
/* 229 */       byteCount += bytesRead;
/*     */     }
/* 231 */     return byteCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InputStream emptyInput() {
/* 240 */     return new ByteArrayInputStream(EMPTY_CONTENT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InputStream nonClosing(InputStream in) {
/* 250 */     Assert.notNull(in, "No InputStream specified");
/* 251 */     return new NonClosingInputStream(in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static OutputStream nonClosing(OutputStream out) {
/* 261 */     Assert.notNull(out, "No OutputStream specified");
/* 262 */     return new NonClosingOutputStream(out);
/*     */   }
/*     */   
/*     */   private static class NonClosingInputStream
/*     */     extends FilterInputStream
/*     */   {
/*     */     public NonClosingInputStream(InputStream in) {
/* 269 */       super(in);
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {}
/*     */   }
/*     */ 
/*     */   
/*     */   private static class NonClosingOutputStream
/*     */     extends FilterOutputStream
/*     */   {
/*     */     public NonClosingOutputStream(OutputStream out) {
/* 281 */       super(out);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void write(byte[] b, int off, int let) throws IOException {
/* 287 */       this.out.write(b, off, let);
/*     */     }
/*     */     
/*     */     public void close() throws IOException {}
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\StreamUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */