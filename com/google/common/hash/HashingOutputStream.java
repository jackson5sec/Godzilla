/*    */ package com.google.common.hash;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Beta
/*    */ public final class HashingOutputStream
/*    */   extends FilterOutputStream
/*    */ {
/*    */   private final Hasher hasher;
/*    */   
/*    */   public HashingOutputStream(HashFunction hashFunction, OutputStream out) {
/* 45 */     super((OutputStream)Preconditions.checkNotNull(out));
/* 46 */     this.hasher = (Hasher)Preconditions.checkNotNull(hashFunction.newHasher());
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(int b) throws IOException {
/* 51 */     this.hasher.putByte((byte)b);
/* 52 */     this.out.write(b);
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(byte[] bytes, int off, int len) throws IOException {
/* 57 */     this.hasher.putBytes(bytes, off, len);
/* 58 */     this.out.write(bytes, off, len);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HashCode hash() {
/* 66 */     return this.hasher.hash();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() throws IOException {
/* 74 */     this.out.close();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\hash\HashingOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */