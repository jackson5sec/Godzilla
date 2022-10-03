/*     */ package org.fife.io;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PushbackInputStream;
/*     */ import java.io.Reader;
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
/*     */ public class UnicodeReader
/*     */   extends Reader
/*     */ {
/*  51 */   private InputStreamReader internalIn = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String encoding;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int BOM_SIZE = 4;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnicodeReader(String file) throws IOException {
/*  79 */     this(new File(file));
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
/*     */   public UnicodeReader(File file) throws IOException {
/*  96 */     this(new FileInputStream(file));
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
/*     */ 
/*     */ 
/*     */   
/*     */   public UnicodeReader(File file, String defaultEncoding) throws IOException {
/* 116 */     this(new FileInputStream(file), defaultEncoding);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public UnicodeReader(File file, Charset defaultCharset) throws IOException {
/* 136 */     this(new FileInputStream(file), (defaultCharset != null) ? defaultCharset.name() : null);
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
/*     */   public UnicodeReader(InputStream in) throws IOException {
/* 149 */     this(in, (String)null);
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
/*     */   
/*     */   public UnicodeReader(InputStream in, String defaultEncoding) throws IOException {
/* 167 */     init(in, defaultEncoding);
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
/*     */   public UnicodeReader(InputStream in, Charset defaultCharset) throws IOException {
/* 184 */     init(in, defaultCharset.name());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 193 */     this.internalIn.close();
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
/*     */   public String getEncoding() {
/* 206 */     return this.encoding;
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
/*     */   protected void init(InputStream in, String defaultEncoding) throws IOException {
/*     */     int unread;
/* 221 */     PushbackInputStream tempIn = new PushbackInputStream(in, 4);
/*     */     
/* 223 */     byte[] bom = new byte[4];
/*     */     
/* 225 */     int n = tempIn.read(bom, 0, bom.length);
/*     */     
/* 227 */     if (bom[0] == 0 && bom[1] == 0 && bom[2] == -2 && bom[3] == -1) {
/*     */       
/* 229 */       this.encoding = "UTF-32BE";
/* 230 */       unread = n - 4;
/*     */     
/*     */     }
/* 233 */     else if (n == 4 && bom[0] == -1 && bom[1] == -2 && bom[2] == 0 && bom[3] == 0) {
/*     */ 
/*     */       
/* 236 */       this.encoding = "UTF-32LE";
/* 237 */       unread = n - 4;
/*     */     
/*     */     }
/* 240 */     else if (bom[0] == -17 && bom[1] == -69 && bom[2] == -65) {
/*     */ 
/*     */       
/* 243 */       this.encoding = "UTF-8";
/* 244 */       unread = n - 3;
/*     */     
/*     */     }
/* 247 */     else if (bom[0] == -2 && bom[1] == -1) {
/* 248 */       this.encoding = "UTF-16BE";
/* 249 */       unread = n - 2;
/*     */     
/*     */     }
/* 252 */     else if (bom[0] == -1 && bom[1] == -2) {
/* 253 */       this.encoding = "UTF-16LE";
/* 254 */       unread = n - 2;
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 259 */       this.encoding = defaultEncoding;
/* 260 */       unread = n;
/*     */     } 
/*     */     
/* 263 */     if (unread > 0) {
/* 264 */       tempIn.unread(bom, n - unread, unread);
/*     */     }
/* 266 */     else if (unread < -1) {
/* 267 */       tempIn.unread(bom, 0, 0);
/*     */     } 
/*     */ 
/*     */     
/* 271 */     if (this.encoding == null) {
/* 272 */       this.internalIn = new InputStreamReader(tempIn);
/* 273 */       this.encoding = this.internalIn.getEncoding();
/*     */     } else {
/*     */       
/* 276 */       this.internalIn = new InputStreamReader(tempIn, this.encoding);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(char[] cbuf, int off, int len) throws IOException {
/* 296 */     return this.internalIn.read(cbuf, off, len);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\io\UnicodeReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */