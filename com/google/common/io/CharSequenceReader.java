/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.nio.CharBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class CharSequenceReader
/*     */   extends Reader
/*     */ {
/*     */   private CharSequence seq;
/*     */   private int pos;
/*     */   private int mark;
/*     */   
/*     */   public CharSequenceReader(CharSequence seq) {
/*  42 */     this.seq = (CharSequence)Preconditions.checkNotNull(seq);
/*     */   }
/*     */   
/*     */   private void checkOpen() throws IOException {
/*  46 */     if (this.seq == null) {
/*  47 */       throw new IOException("reader closed");
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean hasRemaining() {
/*  52 */     return (remaining() > 0);
/*     */   }
/*     */   
/*     */   private int remaining() {
/*  56 */     return this.seq.length() - this.pos;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int read(CharBuffer target) throws IOException {
/*  61 */     Preconditions.checkNotNull(target);
/*  62 */     checkOpen();
/*  63 */     if (!hasRemaining()) {
/*  64 */       return -1;
/*     */     }
/*  66 */     int charsToRead = Math.min(target.remaining(), remaining());
/*  67 */     for (int i = 0; i < charsToRead; i++) {
/*  68 */       target.put(this.seq.charAt(this.pos++));
/*     */     }
/*  70 */     return charsToRead;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int read() throws IOException {
/*  75 */     checkOpen();
/*  76 */     return hasRemaining() ? this.seq.charAt(this.pos++) : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int read(char[] cbuf, int off, int len) throws IOException {
/*  81 */     Preconditions.checkPositionIndexes(off, off + len, cbuf.length);
/*  82 */     checkOpen();
/*  83 */     if (!hasRemaining()) {
/*  84 */       return -1;
/*     */     }
/*  86 */     int charsToRead = Math.min(len, remaining());
/*  87 */     for (int i = 0; i < charsToRead; i++) {
/*  88 */       cbuf[off + i] = this.seq.charAt(this.pos++);
/*     */     }
/*  90 */     return charsToRead;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized long skip(long n) throws IOException {
/*  95 */     Preconditions.checkArgument((n >= 0L), "n (%s) may not be negative", n);
/*  96 */     checkOpen();
/*  97 */     int charsToSkip = (int)Math.min(remaining(), n);
/*  98 */     this.pos += charsToSkip;
/*  99 */     return charsToSkip;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean ready() throws IOException {
/* 104 */     checkOpen();
/* 105 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/* 110 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void mark(int readAheadLimit) throws IOException {
/* 115 */     Preconditions.checkArgument((readAheadLimit >= 0), "readAheadLimit (%s) may not be negative", readAheadLimit);
/* 116 */     checkOpen();
/* 117 */     this.mark = this.pos;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void reset() throws IOException {
/* 122 */     checkOpen();
/* 123 */     this.pos = this.mark;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void close() throws IOException {
/* 128 */     this.seq = null;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\io\CharSequenceReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */