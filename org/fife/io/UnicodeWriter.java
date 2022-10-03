/*     */ package org.fife.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
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
/*     */ public class UnicodeWriter
/*     */   extends Writer
/*     */ {
/*     */   public static final String PROPERTY_WRITE_UTF8_BOM = "UnicodeWriter.writeUtf8BOM";
/*     */   private OutputStreamWriter internalOut;
/*  53 */   private static final byte[] UTF8_BOM = new byte[] { -17, -69, -65 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   private static final byte[] UTF16LE_BOM = new byte[] { -1, -2 };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   private static final byte[] UTF16BE_BOM = new byte[] { -2, -1 };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   private static final byte[] UTF32LE_BOM = new byte[] { -1, -2, 0, 0 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   private static final byte[] UTF32BE_BOM = new byte[] { 0, 0, -2, -1 };
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
/*     */   public UnicodeWriter(String fileName, Charset charset) throws IOException {
/*  94 */     this(new FileOutputStream(fileName), charset.name());
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
/*     */   public UnicodeWriter(String fileName, String encoding) throws IOException {
/* 107 */     this(new FileOutputStream(fileName), encoding);
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
/*     */   public UnicodeWriter(File file, Charset charset) throws IOException {
/* 121 */     this(new FileOutputStream(file), charset.name());
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
/*     */   public UnicodeWriter(File file, String encoding) throws IOException {
/* 134 */     this(new FileOutputStream(file), encoding);
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
/*     */   public UnicodeWriter(OutputStream out, Charset charset) throws IOException {
/* 146 */     init(out, charset.name());
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
/*     */   public UnicodeWriter(OutputStream out, String encoding) throws IOException {
/* 158 */     init(out, encoding);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 169 */     this.internalOut.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 180 */     this.internalOut.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/* 191 */     return this.internalOut.getEncoding();
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
/*     */   public static boolean getWriteUtf8BOM() {
/* 203 */     return Boolean.getBoolean("UnicodeWriter.writeUtf8BOM");
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
/*     */   private void init(OutputStream out, String encoding) throws IOException {
/* 217 */     this.internalOut = new OutputStreamWriter(out, encoding);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 223 */     if ("UTF-8".equals(encoding)) {
/* 224 */       if (getWriteUtf8BOM()) {
/* 225 */         out.write(UTF8_BOM, 0, UTF8_BOM.length);
/*     */       }
/*     */     }
/* 228 */     else if ("UTF-16LE".equals(encoding)) {
/* 229 */       out.write(UTF16LE_BOM, 0, UTF16LE_BOM.length);
/*     */     }
/* 231 */     else if ("UTF-16BE".equals(encoding)) {
/* 232 */       out.write(UTF16BE_BOM, 0, UTF16BE_BOM.length);
/*     */     }
/* 234 */     else if ("UTF-32LE".equals(encoding)) {
/* 235 */       out.write(UTF32LE_BOM, 0, UTF32LE_BOM.length);
/*     */     }
/* 237 */     else if ("UTF-32".equals(encoding) || "UTF-32BE".equals(encoding)) {
/* 238 */       out.write(UTF32BE_BOM, 0, UTF32BE_BOM.length);
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
/*     */ 
/*     */   
/*     */   public static void setWriteUtf8BOM(boolean write) {
/* 252 */     System.setProperty("UnicodeWriter.writeUtf8BOM", 
/* 253 */         Boolean.toString(write));
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
/*     */   public void write(char[] cbuf, int off, int len) throws IOException {
/* 267 */     this.internalOut.write(cbuf, off, len);
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
/*     */   public void write(int c) throws IOException {
/* 279 */     this.internalOut.write(c);
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
/*     */   public void write(String str, int off, int len) throws IOException {
/* 293 */     this.internalOut.write(str, off, len);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\io\UnicodeWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */