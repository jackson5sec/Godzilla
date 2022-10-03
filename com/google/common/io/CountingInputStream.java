/*    */ package com.google.common.io;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.FilterInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Beta
/*    */ @GwtIncompatible
/*    */ public final class CountingInputStream
/*    */   extends FilterInputStream
/*    */ {
/*    */   private long count;
/* 36 */   private long mark = -1L;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CountingInputStream(InputStream in) {
/* 44 */     super((InputStream)Preconditions.checkNotNull(in));
/*    */   }
/*    */ 
/*    */   
/*    */   public long getCount() {
/* 49 */     return this.count;
/*    */   }
/*    */ 
/*    */   
/*    */   public int read() throws IOException {
/* 54 */     int result = this.in.read();
/* 55 */     if (result != -1) {
/* 56 */       this.count++;
/*    */     }
/* 58 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public int read(byte[] b, int off, int len) throws IOException {
/* 63 */     int result = this.in.read(b, off, len);
/* 64 */     if (result != -1) {
/* 65 */       this.count += result;
/*    */     }
/* 67 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public long skip(long n) throws IOException {
/* 72 */     long result = this.in.skip(n);
/* 73 */     this.count += result;
/* 74 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized void mark(int readlimit) {
/* 79 */     this.in.mark(readlimit);
/* 80 */     this.mark = this.count;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void reset() throws IOException {
/* 86 */     if (!this.in.markSupported()) {
/* 87 */       throw new IOException("Mark not supported");
/*    */     }
/* 89 */     if (this.mark == -1L) {
/* 90 */       throw new IOException("Mark not set");
/*    */     }
/*    */     
/* 93 */     this.in.reset();
/* 94 */     this.count = this.mark;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\io\CountingInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */