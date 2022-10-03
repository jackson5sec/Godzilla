/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ final class MultiInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private Iterator<? extends ByteSource> it;
/*     */   private InputStream in;
/*     */   
/*     */   public MultiInputStream(Iterator<? extends ByteSource> it) throws IOException {
/*  44 */     this.it = (Iterator<? extends ByteSource>)Preconditions.checkNotNull(it);
/*  45 */     advance();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  50 */     if (this.in != null) {
/*     */       try {
/*  52 */         this.in.close();
/*     */       } finally {
/*  54 */         this.in = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void advance() throws IOException {
/*  61 */     close();
/*  62 */     if (this.it.hasNext()) {
/*  63 */       this.in = ((ByteSource)this.it.next()).openStream();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int available() throws IOException {
/*  69 */     if (this.in == null) {
/*  70 */       return 0;
/*     */     }
/*  72 */     return this.in.available();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  82 */     while (this.in != null) {
/*  83 */       int result = this.in.read();
/*  84 */       if (result != -1) {
/*  85 */         return result;
/*     */       }
/*  87 */       advance();
/*     */     } 
/*  89 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/*  94 */     while (this.in != null) {
/*  95 */       int result = this.in.read(b, off, len);
/*  96 */       if (result != -1) {
/*  97 */         return result;
/*     */       }
/*  99 */       advance();
/*     */     } 
/* 101 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public long skip(long n) throws IOException {
/* 106 */     if (this.in == null || n <= 0L) {
/* 107 */       return 0L;
/*     */     }
/* 109 */     long result = this.in.skip(n);
/* 110 */     if (result != 0L) {
/* 111 */       return result;
/*     */     }
/* 113 */     if (read() == -1) {
/* 114 */       return 0L;
/*     */     }
/* 116 */     return 1L + this.in.skip(n - 1L);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\io\MultiInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */