/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
/*     */ import org.springframework.lang.Nullable;
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
/*     */ public class FastByteArrayOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private static final int DEFAULT_BLOCK_SIZE = 256;
/*  54 */   private final Deque<byte[]> buffers = (Deque)new ArrayDeque<>();
/*     */ 
/*     */   
/*     */   private final int initialBlockSize;
/*     */ 
/*     */   
/*  60 */   private int nextBlockSize = 0;
/*     */ 
/*     */ 
/*     */   
/*  64 */   private int alreadyBufferedSize = 0;
/*     */ 
/*     */   
/*  67 */   private int index = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean closed = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastByteArrayOutputStream() {
/*  78 */     this(256);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FastByteArrayOutputStream(int initialBlockSize) {
/*  87 */     Assert.isTrue((initialBlockSize > 0), "Initial block size must be greater than 0");
/*  88 */     this.initialBlockSize = initialBlockSize;
/*  89 */     this.nextBlockSize = initialBlockSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int datum) throws IOException {
/*  97 */     if (this.closed) {
/*  98 */       throw new IOException("Stream closed");
/*     */     }
/*     */     
/* 101 */     if (this.buffers.peekLast() == null || ((byte[])this.buffers.getLast()).length == this.index) {
/* 102 */       addBuffer(1);
/*     */     }
/*     */     
/* 105 */     ((byte[])this.buffers.getLast())[this.index++] = (byte)datum;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(byte[] data, int offset, int length) throws IOException {
/* 111 */     if (offset < 0 || offset + length > data.length || length < 0) {
/* 112 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 114 */     if (this.closed) {
/* 115 */       throw new IOException("Stream closed");
/*     */     }
/*     */     
/* 118 */     if (this.buffers.peekLast() == null || ((byte[])this.buffers.getLast()).length == this.index) {
/* 119 */       addBuffer(length);
/*     */     }
/* 121 */     if (this.index + length > ((byte[])this.buffers.getLast()).length) {
/* 122 */       int pos = offset;
/*     */       do {
/* 124 */         if (this.index == ((byte[])this.buffers.getLast()).length) {
/* 125 */           addBuffer(length);
/*     */         }
/* 127 */         int copyLength = ((byte[])this.buffers.getLast()).length - this.index;
/* 128 */         if (length < copyLength) {
/* 129 */           copyLength = length;
/*     */         }
/* 131 */         System.arraycopy(data, pos, this.buffers.getLast(), this.index, copyLength);
/* 132 */         pos += copyLength;
/* 133 */         this.index += copyLength;
/* 134 */         length -= copyLength;
/*     */       }
/* 136 */       while (length > 0);
/*     */     }
/*     */     else {
/*     */       
/* 140 */       System.arraycopy(data, offset, this.buffers.getLast(), this.index, length);
/* 141 */       this.index += length;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 148 */     this.closed = true;
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
/*     */   public String toString() {
/* 165 */     return new String(toByteArrayUnsafe());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 175 */     return this.alreadyBufferedSize + this.index;
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
/*     */   public byte[] toByteArrayUnsafe() {
/* 191 */     int totalSize = size();
/* 192 */     if (totalSize == 0) {
/* 193 */       return new byte[0];
/*     */     }
/* 195 */     resize(totalSize);
/* 196 */     return this.buffers.getFirst();
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
/*     */   public byte[] toByteArray() {
/* 209 */     byte[] bytesUnsafe = toByteArrayUnsafe();
/* 210 */     return (byte[])bytesUnsafe.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 219 */     this.buffers.clear();
/* 220 */     this.nextBlockSize = this.initialBlockSize;
/* 221 */     this.closed = false;
/* 222 */     this.index = 0;
/* 223 */     this.alreadyBufferedSize = 0;
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
/*     */   public InputStream getInputStream() {
/* 235 */     return new FastByteArrayInputStream(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeTo(OutputStream out) throws IOException {
/* 243 */     Iterator<byte[]> it = (Iterator)this.buffers.iterator();
/* 244 */     while (it.hasNext()) {
/* 245 */       byte[] bytes = it.next();
/* 246 */       if (it.hasNext()) {
/* 247 */         out.write(bytes, 0, bytes.length);
/*     */         continue;
/*     */       } 
/* 250 */       out.write(bytes, 0, this.index);
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
/*     */   public void resize(int targetCapacity) {
/* 263 */     Assert.isTrue((targetCapacity >= size()), "New capacity must not be smaller than current size");
/* 264 */     if (this.buffers.peekFirst() == null) {
/* 265 */       this.nextBlockSize = targetCapacity - size();
/*     */     }
/* 267 */     else if (size() != targetCapacity || ((byte[])this.buffers.getFirst()).length != targetCapacity) {
/*     */ 
/*     */ 
/*     */       
/* 271 */       int totalSize = size();
/* 272 */       byte[] data = new byte[targetCapacity];
/* 273 */       int pos = 0;
/* 274 */       Iterator<byte[]> it = (Iterator)this.buffers.iterator();
/* 275 */       while (it.hasNext()) {
/* 276 */         byte[] bytes = it.next();
/* 277 */         if (it.hasNext()) {
/* 278 */           System.arraycopy(bytes, 0, data, pos, bytes.length);
/* 279 */           pos += bytes.length;
/*     */           continue;
/*     */         } 
/* 282 */         System.arraycopy(bytes, 0, data, pos, this.index);
/*     */       } 
/*     */       
/* 285 */       this.buffers.clear();
/* 286 */       this.buffers.add(data);
/* 287 */       this.index = totalSize;
/* 288 */       this.alreadyBufferedSize = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addBuffer(int minCapacity) {
/* 297 */     if (this.buffers.peekLast() != null) {
/* 298 */       this.alreadyBufferedSize += this.index;
/* 299 */       this.index = 0;
/*     */     } 
/* 301 */     if (this.nextBlockSize < minCapacity) {
/* 302 */       this.nextBlockSize = nextPowerOf2(minCapacity);
/*     */     }
/* 304 */     this.buffers.add(new byte[this.nextBlockSize]);
/* 305 */     this.nextBlockSize *= 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int nextPowerOf2(int val) {
/* 312 */     val--;
/* 313 */     val = val >> 1 | val;
/* 314 */     val = val >> 2 | val;
/* 315 */     val = val >> 4 | val;
/* 316 */     val = val >> 8 | val;
/* 317 */     val = val >> 16 | val;
/* 318 */     val++;
/* 319 */     return val;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class FastByteArrayInputStream
/*     */     extends UpdateMessageDigestInputStream
/*     */   {
/*     */     private final FastByteArrayOutputStream fastByteArrayOutputStream;
/*     */ 
/*     */     
/*     */     private final Iterator<byte[]> buffersIterator;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     private byte[] currentBuffer;
/*     */     
/* 336 */     private int currentBufferLength = 0;
/*     */     
/* 338 */     private int nextIndexInCurrentBuffer = 0;
/*     */     
/* 340 */     private int totalBytesRead = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public FastByteArrayInputStream(FastByteArrayOutputStream fastByteArrayOutputStream) {
/* 347 */       this.fastByteArrayOutputStream = fastByteArrayOutputStream;
/* 348 */       this.buffersIterator = (Iterator)fastByteArrayOutputStream.buffers.iterator();
/* 349 */       if (this.buffersIterator.hasNext()) {
/* 350 */         this.currentBuffer = this.buffersIterator.next();
/* 351 */         if (this.currentBuffer == fastByteArrayOutputStream.buffers.getLast()) {
/* 352 */           this.currentBufferLength = fastByteArrayOutputStream.index;
/*     */         } else {
/*     */           
/* 355 */           this.currentBufferLength = (this.currentBuffer != null) ? this.currentBuffer.length : 0;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int read() {
/* 362 */       if (this.currentBuffer == null)
/*     */       {
/* 364 */         return -1;
/*     */       }
/*     */       
/* 367 */       if (this.nextIndexInCurrentBuffer < this.currentBufferLength) {
/* 368 */         this.totalBytesRead++;
/* 369 */         return this.currentBuffer[this.nextIndexInCurrentBuffer++] & 0xFF;
/*     */       } 
/*     */       
/* 372 */       if (this.buffersIterator.hasNext()) {
/* 373 */         this.currentBuffer = this.buffersIterator.next();
/* 374 */         updateCurrentBufferLength();
/* 375 */         this.nextIndexInCurrentBuffer = 0;
/*     */       } else {
/*     */         
/* 378 */         this.currentBuffer = null;
/*     */       } 
/* 380 */       return read();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int read(byte[] b) {
/* 387 */       return read(b, 0, b.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public int read(byte[] b, int off, int len) {
/* 392 */       if (off < 0 || len < 0 || len > b.length - off) {
/* 393 */         throw new IndexOutOfBoundsException();
/*     */       }
/* 395 */       if (len == 0) {
/* 396 */         return 0;
/*     */       }
/*     */       
/* 399 */       if (this.currentBuffer == null)
/*     */       {
/* 401 */         return -1;
/*     */       }
/*     */       
/* 404 */       if (this.nextIndexInCurrentBuffer < this.currentBufferLength) {
/* 405 */         int bytesToCopy = Math.min(len, this.currentBufferLength - this.nextIndexInCurrentBuffer);
/* 406 */         System.arraycopy(this.currentBuffer, this.nextIndexInCurrentBuffer, b, off, bytesToCopy);
/* 407 */         this.totalBytesRead += bytesToCopy;
/* 408 */         this.nextIndexInCurrentBuffer += bytesToCopy;
/* 409 */         int remaining = read(b, off + bytesToCopy, len - bytesToCopy);
/* 410 */         return bytesToCopy + Math.max(remaining, 0);
/*     */       } 
/*     */       
/* 413 */       if (this.buffersIterator.hasNext()) {
/* 414 */         this.currentBuffer = this.buffersIterator.next();
/* 415 */         updateCurrentBufferLength();
/* 416 */         this.nextIndexInCurrentBuffer = 0;
/*     */       } else {
/*     */         
/* 419 */         this.currentBuffer = null;
/*     */       } 
/* 421 */       return read(b, off, len);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long skip(long n) throws IOException {
/* 429 */       if (n > 2147483647L) {
/* 430 */         throw new IllegalArgumentException("n exceeds maximum (2147483647): " + n);
/*     */       }
/* 432 */       if (n == 0L) {
/* 433 */         return 0L;
/*     */       }
/* 435 */       if (n < 0L) {
/* 436 */         throw new IllegalArgumentException("n must be 0 or greater: " + n);
/*     */       }
/* 438 */       int len = (int)n;
/* 439 */       if (this.currentBuffer == null)
/*     */       {
/* 441 */         return 0L;
/*     */       }
/*     */       
/* 444 */       if (this.nextIndexInCurrentBuffer < this.currentBufferLength) {
/* 445 */         int bytesToSkip = Math.min(len, this.currentBufferLength - this.nextIndexInCurrentBuffer);
/* 446 */         this.totalBytesRead += bytesToSkip;
/* 447 */         this.nextIndexInCurrentBuffer += bytesToSkip;
/* 448 */         return bytesToSkip + skip((len - bytesToSkip));
/*     */       } 
/*     */       
/* 451 */       if (this.buffersIterator.hasNext()) {
/* 452 */         this.currentBuffer = this.buffersIterator.next();
/* 453 */         updateCurrentBufferLength();
/* 454 */         this.nextIndexInCurrentBuffer = 0;
/*     */       } else {
/*     */         
/* 457 */         this.currentBuffer = null;
/*     */       } 
/* 459 */       return skip(len);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int available() {
/* 466 */       return this.fastByteArrayOutputStream.size() - this.totalBytesRead;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void updateMessageDigest(MessageDigest messageDigest) {
/* 475 */       updateMessageDigest(messageDigest, available());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void updateMessageDigest(MessageDigest messageDigest, int len) {
/* 486 */       if (this.currentBuffer == null) {
/*     */         return;
/*     */       }
/*     */       
/* 490 */       if (len == 0) {
/*     */         return;
/*     */       }
/* 493 */       if (len < 0) {
/* 494 */         throw new IllegalArgumentException("len must be 0 or greater: " + len);
/*     */       }
/*     */       
/* 497 */       if (this.nextIndexInCurrentBuffer < this.currentBufferLength) {
/* 498 */         int bytesToCopy = Math.min(len, this.currentBufferLength - this.nextIndexInCurrentBuffer);
/* 499 */         messageDigest.update(this.currentBuffer, this.nextIndexInCurrentBuffer, bytesToCopy);
/* 500 */         this.nextIndexInCurrentBuffer += bytesToCopy;
/* 501 */         updateMessageDigest(messageDigest, len - bytesToCopy);
/*     */       } else {
/*     */         
/* 504 */         if (this.buffersIterator.hasNext()) {
/* 505 */           this.currentBuffer = this.buffersIterator.next();
/* 506 */           updateCurrentBufferLength();
/* 507 */           this.nextIndexInCurrentBuffer = 0;
/*     */         } else {
/*     */           
/* 510 */           this.currentBuffer = null;
/*     */         } 
/* 512 */         updateMessageDigest(messageDigest, len);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private void updateCurrentBufferLength() {
/* 518 */       if (this.currentBuffer == this.fastByteArrayOutputStream.buffers.getLast()) {
/* 519 */         this.currentBufferLength = this.fastByteArrayOutputStream.index;
/*     */       } else {
/*     */         
/* 522 */         this.currentBufferLength = (this.currentBuffer != null) ? this.currentBuffer.length : 0;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\FastByteArrayOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */