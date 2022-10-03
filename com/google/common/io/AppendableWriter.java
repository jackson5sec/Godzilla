/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Closeable;
/*     */ import java.io.Flushable;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ class AppendableWriter
/*     */   extends Writer
/*     */ {
/*     */   private final Appendable target;
/*     */   private boolean closed;
/*     */   
/*     */   AppendableWriter(Appendable target) {
/*  45 */     this.target = (Appendable)Preconditions.checkNotNull(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(char[] cbuf, int off, int len) throws IOException {
/*  54 */     checkNotClosed();
/*     */ 
/*     */     
/*  57 */     this.target.append(new String(cbuf, off, len));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int c) throws IOException {
/*  66 */     checkNotClosed();
/*  67 */     this.target.append((char)c);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(String str) throws IOException {
/*  72 */     checkNotClosed();
/*  73 */     this.target.append(str);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(String str, int off, int len) throws IOException {
/*  78 */     checkNotClosed();
/*     */     
/*  80 */     this.target.append(str, off, off + len);
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/*  85 */     checkNotClosed();
/*  86 */     if (this.target instanceof Flushable) {
/*  87 */       ((Flushable)this.target).flush();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  93 */     this.closed = true;
/*  94 */     if (this.target instanceof Closeable) {
/*  95 */       ((Closeable)this.target).close();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Writer append(char c) throws IOException {
/* 101 */     checkNotClosed();
/* 102 */     this.target.append(c);
/* 103 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Writer append(CharSequence charSeq) throws IOException {
/* 108 */     checkNotClosed();
/* 109 */     this.target.append(charSeq);
/* 110 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Writer append(CharSequence charSeq, int start, int end) throws IOException {
/* 115 */     checkNotClosed();
/* 116 */     this.target.append(charSeq, start, end);
/* 117 */     return this;
/*     */   }
/*     */   
/*     */   private void checkNotClosed() throws IOException {
/* 121 */     if (this.closed)
/* 122 */       throw new IOException("Cannot write to a closed writer."); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\io\AppendableWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */