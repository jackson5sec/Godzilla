/*    */ package com.google.common.hash;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.errorprone.annotations.Immutable;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.charset.Charset;
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
/*    */ @Immutable
/*    */ abstract class AbstractHashFunction
/*    */   implements HashFunction
/*    */ {
/*    */   public <T> HashCode hashObject(T instance, Funnel<? super T> funnel) {
/* 33 */     return newHasher().<T>putObject(instance, funnel).hash();
/*    */   }
/*    */ 
/*    */   
/*    */   public HashCode hashUnencodedChars(CharSequence input) {
/* 38 */     int len = input.length();
/* 39 */     return newHasher(len * 2).putUnencodedChars(input).hash();
/*    */   }
/*    */ 
/*    */   
/*    */   public HashCode hashString(CharSequence input, Charset charset) {
/* 44 */     return newHasher().putString(input, charset).hash();
/*    */   }
/*    */ 
/*    */   
/*    */   public HashCode hashInt(int input) {
/* 49 */     return newHasher(4).putInt(input).hash();
/*    */   }
/*    */ 
/*    */   
/*    */   public HashCode hashLong(long input) {
/* 54 */     return newHasher(8).putLong(input).hash();
/*    */   }
/*    */ 
/*    */   
/*    */   public HashCode hashBytes(byte[] input) {
/* 59 */     return hashBytes(input, 0, input.length);
/*    */   }
/*    */ 
/*    */   
/*    */   public HashCode hashBytes(byte[] input, int off, int len) {
/* 64 */     Preconditions.checkPositionIndexes(off, off + len, input.length);
/* 65 */     return newHasher(len).putBytes(input, off, len).hash();
/*    */   }
/*    */ 
/*    */   
/*    */   public HashCode hashBytes(ByteBuffer input) {
/* 70 */     return newHasher(input.remaining()).putBytes(input).hash();
/*    */   }
/*    */ 
/*    */   
/*    */   public Hasher newHasher(int expectedInputSize) {
/* 75 */     Preconditions.checkArgument((expectedInputSize >= 0), "expectedInputSize must be >= 0 but was %s", expectedInputSize);
/*    */     
/* 77 */     return newHasher();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\hash\AbstractHashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */