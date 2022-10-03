/*    */ package org.springframework.util;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
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
/*    */ public class ResizableByteArrayOutputStream
/*    */   extends ByteArrayOutputStream
/*    */ {
/*    */   private static final int DEFAULT_INITIAL_CAPACITY = 256;
/*    */   
/*    */   public ResizableByteArrayOutputStream() {
/* 51 */     super(256);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ResizableByteArrayOutputStream(int initialCapacity) {
/* 60 */     super(initialCapacity);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void resize(int targetCapacity) {
/* 72 */     Assert.isTrue((targetCapacity >= this.count), "New capacity must not be smaller than current size");
/* 73 */     byte[] resizedBuffer = new byte[targetCapacity];
/* 74 */     System.arraycopy(this.buf, 0, resizedBuffer, 0, this.count);
/* 75 */     this.buf = resizedBuffer;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void grow(int additionalCapacity) {
/* 84 */     Assert.isTrue((additionalCapacity >= 0), "Additional capacity must be 0 or higher");
/* 85 */     if (this.count + additionalCapacity > this.buf.length) {
/* 86 */       int newCapacity = Math.max(this.buf.length * 2, this.count + additionalCapacity);
/* 87 */       resize(newCapacity);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized int capacity() {
/* 95 */     return this.buf.length;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\ResizableByteArrayOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */