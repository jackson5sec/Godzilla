/*     */ package com.kitfox.svg.xml.cpx;
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.zip.DataFormatException;
/*     */ import java.util.zip.Inflater;
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
/*     */ 
/*     */ public class CPXInputStream
/*     */   extends FilterInputStream
/*     */   implements CPXConsts
/*     */ {
/*  62 */   SecureRandom sec = new SecureRandom();
/*     */   
/*  64 */   Inflater inflater = new Inflater();
/*     */ 
/*     */   
/*     */   int xlateMode;
/*     */   
/*  69 */   byte[] head = new byte[4];
/*  70 */   int headSize = 0;
/*  71 */   int headPtr = 0;
/*     */   
/*     */   boolean reachedEOF = false;
/*  74 */   byte[] inBuffer = new byte[2048];
/*  75 */   byte[] decryptBuffer = new byte[2048];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CPXInputStream(InputStream in) throws IOException {
/*  83 */     super(in);
/*     */ 
/*     */     
/*  86 */     for (int i = 0; i < 4; i++) {
/*     */       
/*  88 */       int val = in.read();
/*  89 */       this.head[i] = (byte)val;
/*  90 */       if (val == -1 || this.head[i] != MAGIC_NUMBER[i]) {
/*     */         
/*  92 */         this.headSize = i + 1;
/*  93 */         this.xlateMode = 0;
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*  98 */     this.xlateMode = 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/* 105 */     return false;
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
/*     */   public void close() throws IOException {
/* 118 */     this.reachedEOF = true;
/* 119 */     this.in.close();
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
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 142 */     byte[] b = new byte[1];
/* 143 */     int retVal = read(b, 0, 1);
/* 144 */     if (retVal == -1) return -1; 
/* 145 */     return b[0];
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(byte[] b) throws IOException {
/* 171 */     return read(b, 0, b.length);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/* 194 */     if (this.reachedEOF) return -1;
/*     */     
/* 196 */     if (this.xlateMode == 0) {
/*     */       
/* 198 */       int count = 0;
/*     */       
/* 200 */       while (this.headPtr < this.headSize && len > 0) {
/*     */         
/* 202 */         b[off++] = this.head[this.headPtr++];
/* 203 */         count++;
/* 204 */         len--;
/*     */       } 
/*     */       
/* 207 */       return (len == 0) ? count : (count + this.in.read(b, off, len));
/*     */     } 
/*     */ 
/*     */     
/* 211 */     if (this.inflater.needsInput() && !decryptChunk()) {
/*     */       int numRead;
/* 213 */       this.reachedEOF = true;
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 218 */         numRead = this.inflater.inflate(b, off, len);
/*     */       }
/* 220 */       catch (Exception e) {
/*     */         
/* 222 */         Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, (String)null, e);
/* 223 */         return -1;
/*     */       } 
/*     */       
/* 226 */       if (!this.inflater.finished())
/*     */       {
/* 228 */         Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, "Inflation imncomplete");
/*     */       }
/*     */ 
/*     */       
/* 232 */       return (numRead == 0) ? -1 : numRead;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 237 */       return this.inflater.inflate(b, off, len);
/*     */     }
/* 239 */     catch (DataFormatException e) {
/*     */       DataFormatException dataFormatException1;
/* 241 */       Logger.getLogger("svgSalamandeLogger").log(Level.WARNING, (String)null, dataFormatException1);
/* 242 */       return -1;
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
/*     */   protected boolean decryptChunk() throws IOException {
/* 255 */     while (this.inflater.needsInput()) {
/*     */       
/* 257 */       int numInBytes = this.in.read(this.inBuffer);
/* 258 */       if (numInBytes == -1) return false;
/*     */ 
/*     */       
/* 261 */       this.inflater.setInput(this.inBuffer, 0, numInBytes);
/*     */     } 
/*     */     
/* 264 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int available() {
/* 273 */     return this.reachedEOF ? 0 : 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long skip(long n) throws IOException {
/* 281 */     int skipSize = (int)n;
/* 282 */     if (skipSize > this.inBuffer.length) skipSize = this.inBuffer.length; 
/* 283 */     return read(this.inBuffer, 0, skipSize);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\xml\cpx\CPXInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */