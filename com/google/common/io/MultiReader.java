/*    */ package com.google.common.io;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import java.io.IOException;
/*    */ import java.io.Reader;
/*    */ import java.util.Iterator;
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
/*    */ @GwtIncompatible
/*    */ class MultiReader
/*    */   extends Reader
/*    */ {
/*    */   private final Iterator<? extends CharSource> it;
/*    */   private Reader current;
/*    */   
/*    */   MultiReader(Iterator<? extends CharSource> readers) throws IOException {
/* 36 */     this.it = readers;
/* 37 */     advance();
/*    */   }
/*    */ 
/*    */   
/*    */   private void advance() throws IOException {
/* 42 */     close();
/* 43 */     if (this.it.hasNext()) {
/* 44 */       this.current = ((CharSource)this.it.next()).openStream();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public int read(char[] cbuf, int off, int len) throws IOException {
/* 50 */     if (this.current == null) {
/* 51 */       return -1;
/*    */     }
/* 53 */     int result = this.current.read(cbuf, off, len);
/* 54 */     if (result == -1) {
/* 55 */       advance();
/* 56 */       return read(cbuf, off, len);
/*    */     } 
/* 58 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public long skip(long n) throws IOException {
/* 63 */     Preconditions.checkArgument((n >= 0L), "n is negative");
/* 64 */     if (n > 0L) {
/* 65 */       while (this.current != null) {
/* 66 */         long result = this.current.skip(n);
/* 67 */         if (result > 0L) {
/* 68 */           return result;
/*    */         }
/* 70 */         advance();
/*    */       } 
/*    */     }
/* 73 */     return 0L;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean ready() throws IOException {
/* 78 */     return (this.current != null && this.current.ready());
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 83 */     if (this.current != null)
/*    */       try {
/* 85 */         this.current.close();
/*    */       } finally {
/* 87 */         this.current = null;
/*    */       }  
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\io\MultiReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */