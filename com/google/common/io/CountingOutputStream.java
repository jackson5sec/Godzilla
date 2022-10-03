/*    */ package com.google.common.io;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.FilterOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Beta
/*    */ @GwtIncompatible
/*    */ public final class CountingOutputStream
/*    */   extends FilterOutputStream
/*    */ {
/*    */   private long count;
/*    */   
/*    */   public CountingOutputStream(OutputStream out) {
/* 43 */     super((OutputStream)Preconditions.checkNotNull(out));
/*    */   }
/*    */ 
/*    */   
/*    */   public long getCount() {
/* 48 */     return this.count;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] b, int off, int len) throws IOException {
/* 53 */     this.out.write(b, off, len);
/* 54 */     this.count += len;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(int b) throws IOException {
/* 59 */     this.out.write(b);
/* 60 */     this.count++;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 68 */     this.out.close();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\io\CountingOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */