/*     */ package org.springframework.core.io.buffer;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Arrays;
/*     */ import java.util.function.IntPredicate;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public class DefaultDataBuffer
/*     */   implements DataBuffer
/*     */ {
/*     */   private static final int MAX_CAPACITY = 2147483647;
/*     */   private static final int CAPACITY_THRESHOLD = 4194304;
/*     */   private final DefaultDataBufferFactory dataBufferFactory;
/*     */   private ByteBuffer byteBuffer;
/*     */   private int capacity;
/*     */   private int readPosition;
/*     */   private int writePosition;
/*     */   
/*     */   private DefaultDataBuffer(DefaultDataBufferFactory dataBufferFactory, ByteBuffer byteBuffer) {
/*  65 */     Assert.notNull(dataBufferFactory, "DefaultDataBufferFactory must not be null");
/*  66 */     Assert.notNull(byteBuffer, "ByteBuffer must not be null");
/*  67 */     this.dataBufferFactory = dataBufferFactory;
/*  68 */     ByteBuffer slice = byteBuffer.slice();
/*  69 */     this.byteBuffer = slice;
/*  70 */     this.capacity = slice.remaining();
/*     */   }
/*     */   
/*     */   static DefaultDataBuffer fromFilledByteBuffer(DefaultDataBufferFactory dataBufferFactory, ByteBuffer byteBuffer) {
/*  74 */     DefaultDataBuffer dataBuffer = new DefaultDataBuffer(dataBufferFactory, byteBuffer);
/*  75 */     dataBuffer.writePosition(byteBuffer.remaining());
/*  76 */     return dataBuffer;
/*     */   }
/*     */   
/*     */   static DefaultDataBuffer fromEmptyByteBuffer(DefaultDataBufferFactory dataBufferFactory, ByteBuffer byteBuffer) {
/*  80 */     return new DefaultDataBuffer(dataBufferFactory, byteBuffer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteBuffer getNativeBuffer() {
/*  91 */     this.byteBuffer.position(this.readPosition);
/*  92 */     this.byteBuffer.limit(readableByteCount());
/*  93 */     return this.byteBuffer;
/*     */   }
/*     */   
/*     */   private void setNativeBuffer(ByteBuffer byteBuffer) {
/*  97 */     this.byteBuffer = byteBuffer;
/*  98 */     this.capacity = byteBuffer.remaining();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultDataBufferFactory factory() {
/* 104 */     return this.dataBufferFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(IntPredicate predicate, int fromIndex) {
/* 109 */     Assert.notNull(predicate, "IntPredicate must not be null");
/* 110 */     if (fromIndex < 0) {
/* 111 */       fromIndex = 0;
/*     */     }
/* 113 */     else if (fromIndex >= this.writePosition) {
/* 114 */       return -1;
/*     */     } 
/* 116 */     for (int i = fromIndex; i < this.writePosition; i++) {
/* 117 */       byte b = this.byteBuffer.get(i);
/* 118 */       if (predicate.test(b)) {
/* 119 */         return i;
/*     */       }
/*     */     } 
/* 122 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(IntPredicate predicate, int fromIndex) {
/* 127 */     Assert.notNull(predicate, "IntPredicate must not be null");
/* 128 */     int i = Math.min(fromIndex, this.writePosition - 1);
/* 129 */     for (; i >= 0; i--) {
/* 130 */       byte b = this.byteBuffer.get(i);
/* 131 */       if (predicate.test(b)) {
/* 132 */         return i;
/*     */       }
/*     */     } 
/* 135 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int readableByteCount() {
/* 140 */     return this.writePosition - this.readPosition;
/*     */   }
/*     */ 
/*     */   
/*     */   public int writableByteCount() {
/* 145 */     return this.capacity - this.writePosition;
/*     */   }
/*     */ 
/*     */   
/*     */   public int readPosition() {
/* 150 */     return this.readPosition;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer readPosition(int readPosition) {
/* 155 */     assertIndex((readPosition >= 0), "'readPosition' %d must be >= 0", new Object[] { Integer.valueOf(readPosition) });
/* 156 */     assertIndex((readPosition <= this.writePosition), "'readPosition' %d must be <= %d", new Object[] {
/* 157 */           Integer.valueOf(readPosition), Integer.valueOf(this.writePosition) });
/* 158 */     this.readPosition = readPosition;
/* 159 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int writePosition() {
/* 164 */     return this.writePosition;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer writePosition(int writePosition) {
/* 169 */     assertIndex((writePosition >= this.readPosition), "'writePosition' %d must be >= %d", new Object[] {
/* 170 */           Integer.valueOf(writePosition), Integer.valueOf(this.readPosition) });
/* 171 */     assertIndex((writePosition <= this.capacity), "'writePosition' %d must be <= %d", new Object[] {
/* 172 */           Integer.valueOf(writePosition), Integer.valueOf(this.capacity) });
/* 173 */     this.writePosition = writePosition;
/* 174 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 179 */     return this.capacity;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer capacity(int newCapacity) {
/* 184 */     if (newCapacity <= 0) {
/* 185 */       throw new IllegalArgumentException(String.format("'newCapacity' %d must be higher than 0", new Object[] { Integer.valueOf(newCapacity) }));
/*     */     }
/* 187 */     int readPosition = readPosition();
/* 188 */     int writePosition = writePosition();
/* 189 */     int oldCapacity = capacity();
/*     */     
/* 191 */     if (newCapacity > oldCapacity) {
/* 192 */       ByteBuffer oldBuffer = this.byteBuffer;
/* 193 */       ByteBuffer newBuffer = allocate(newCapacity, oldBuffer.isDirect());
/* 194 */       oldBuffer.position(0).limit(oldBuffer.capacity());
/* 195 */       newBuffer.position(0).limit(oldBuffer.capacity());
/* 196 */       newBuffer.put(oldBuffer);
/* 197 */       newBuffer.clear();
/* 198 */       setNativeBuffer(newBuffer);
/*     */     }
/* 200 */     else if (newCapacity < oldCapacity) {
/* 201 */       ByteBuffer oldBuffer = this.byteBuffer;
/* 202 */       ByteBuffer newBuffer = allocate(newCapacity, oldBuffer.isDirect());
/* 203 */       if (readPosition < newCapacity) {
/* 204 */         if (writePosition > newCapacity) {
/* 205 */           writePosition = newCapacity;
/* 206 */           writePosition(writePosition);
/*     */         } 
/* 208 */         oldBuffer.position(readPosition).limit(writePosition);
/* 209 */         newBuffer.position(readPosition).limit(writePosition);
/* 210 */         newBuffer.put(oldBuffer);
/* 211 */         newBuffer.clear();
/*     */       } else {
/*     */         
/* 214 */         readPosition(newCapacity);
/* 215 */         writePosition(newCapacity);
/*     */       } 
/* 217 */       setNativeBuffer(newBuffer);
/*     */     } 
/* 219 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataBuffer ensureCapacity(int length) {
/* 224 */     if (length > writableByteCount()) {
/* 225 */       int newCapacity = calculateCapacity(this.writePosition + length);
/* 226 */       capacity(newCapacity);
/*     */     } 
/* 228 */     return this;
/*     */   }
/*     */   
/*     */   private static ByteBuffer allocate(int capacity, boolean direct) {
/* 232 */     return direct ? ByteBuffer.allocateDirect(capacity) : ByteBuffer.allocate(capacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getByte(int index) {
/* 237 */     assertIndex((index >= 0), "index %d must be >= 0", new Object[] { Integer.valueOf(index) });
/* 238 */     assertIndex((index <= this.writePosition - 1), "index %d must be <= %d", new Object[] { Integer.valueOf(index), Integer.valueOf(this.writePosition - 1) });
/* 239 */     return this.byteBuffer.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte read() {
/* 244 */     assertIndex((this.readPosition <= this.writePosition - 1), "readPosition %d must be <= %d", new Object[] {
/* 245 */           Integer.valueOf(this.readPosition), Integer.valueOf(this.writePosition - 1) });
/* 246 */     int pos = this.readPosition;
/* 247 */     byte b = this.byteBuffer.get(pos);
/* 248 */     this.readPosition = pos + 1;
/* 249 */     return b;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer read(byte[] destination) {
/* 254 */     Assert.notNull(destination, "Byte array must not be null");
/* 255 */     read(destination, 0, destination.length);
/* 256 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer read(byte[] destination, int offset, int length) {
/* 261 */     Assert.notNull(destination, "Byte array must not be null");
/* 262 */     assertIndex((this.readPosition <= this.writePosition - length), "readPosition %d and length %d should be smaller than writePosition %d", new Object[] {
/*     */           
/* 264 */           Integer.valueOf(this.readPosition), Integer.valueOf(length), Integer.valueOf(this.writePosition)
/*     */         });
/* 266 */     ByteBuffer tmp = this.byteBuffer.duplicate();
/* 267 */     int limit = this.readPosition + length;
/* 268 */     tmp.clear().position(this.readPosition).limit(limit);
/* 269 */     tmp.get(destination, offset, length);
/*     */     
/* 271 */     this.readPosition += length;
/* 272 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer write(byte b) {
/* 277 */     ensureCapacity(1);
/* 278 */     int pos = this.writePosition;
/* 279 */     this.byteBuffer.put(pos, b);
/* 280 */     this.writePosition = pos + 1;
/* 281 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer write(byte[] source) {
/* 286 */     Assert.notNull(source, "Byte array must not be null");
/* 287 */     write(source, 0, source.length);
/* 288 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer write(byte[] source, int offset, int length) {
/* 293 */     Assert.notNull(source, "Byte array must not be null");
/* 294 */     ensureCapacity(length);
/*     */     
/* 296 */     ByteBuffer tmp = this.byteBuffer.duplicate();
/* 297 */     int limit = this.writePosition + length;
/* 298 */     tmp.clear().position(this.writePosition).limit(limit);
/* 299 */     tmp.put(source, offset, length);
/*     */     
/* 301 */     this.writePosition += length;
/* 302 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer write(DataBuffer... buffers) {
/* 307 */     if (!ObjectUtils.isEmpty((Object[])buffers)) {
/* 308 */       write((ByteBuffer[])Arrays.<DataBuffer>stream(buffers).map(DataBuffer::asByteBuffer).toArray(x$0 -> new ByteBuffer[x$0]));
/*     */     }
/* 310 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer write(ByteBuffer... buffers) {
/* 315 */     if (!ObjectUtils.isEmpty((Object[])buffers)) {
/* 316 */       int capacity = Arrays.<ByteBuffer>stream(buffers).mapToInt(Buffer::remaining).sum();
/* 317 */       ensureCapacity(capacity);
/* 318 */       Arrays.<ByteBuffer>stream(buffers).forEach(this::write);
/*     */     } 
/* 320 */     return this;
/*     */   }
/*     */   
/*     */   private void write(ByteBuffer source) {
/* 324 */     int length = source.remaining();
/* 325 */     ByteBuffer tmp = this.byteBuffer.duplicate();
/* 326 */     int limit = this.writePosition + source.remaining();
/* 327 */     tmp.clear().position(this.writePosition).limit(limit);
/* 328 */     tmp.put(source);
/* 329 */     this.writePosition += length;
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultDataBuffer slice(int index, int length) {
/* 334 */     checkIndex(index, length);
/* 335 */     int oldPosition = this.byteBuffer.position();
/*     */ 
/*     */     
/* 338 */     Buffer buffer = this.byteBuffer;
/*     */     try {
/* 340 */       buffer.position(index);
/* 341 */       ByteBuffer slice = this.byteBuffer.slice();
/*     */       
/* 343 */       slice.limit(length);
/* 344 */       return new SlicedDefaultDataBuffer(slice, this.dataBufferFactory, length);
/*     */     } finally {
/*     */       
/* 347 */       buffer.position(oldPosition);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer asByteBuffer() {
/* 353 */     return asByteBuffer(this.readPosition, readableByteCount());
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer asByteBuffer(int index, int length) {
/* 358 */     checkIndex(index, length);
/*     */     
/* 360 */     ByteBuffer duplicate = this.byteBuffer.duplicate();
/*     */ 
/*     */     
/* 363 */     Buffer buffer = duplicate;
/* 364 */     buffer.position(index);
/* 365 */     buffer.limit(index + length);
/* 366 */     return duplicate.slice();
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream asInputStream() {
/* 371 */     return new DefaultDataBufferInputStream();
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream asInputStream(boolean releaseOnClose) {
/* 376 */     return new DefaultDataBufferInputStream();
/*     */   }
/*     */ 
/*     */   
/*     */   public OutputStream asOutputStream() {
/* 381 */     return new DefaultDataBufferOutputStream();
/*     */   }
/*     */   
/*     */   public String toString(int index, int length, Charset charset) {
/*     */     byte[] bytes;
/*     */     int offset;
/* 387 */     checkIndex(index, length);
/* 388 */     Assert.notNull(charset, "Charset must not be null");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 393 */     if (this.byteBuffer.hasArray()) {
/* 394 */       bytes = this.byteBuffer.array();
/* 395 */       offset = this.byteBuffer.arrayOffset() + index;
/*     */     } else {
/*     */       
/* 398 */       bytes = new byte[length];
/* 399 */       offset = 0;
/* 400 */       ByteBuffer duplicate = this.byteBuffer.duplicate();
/* 401 */       duplicate.clear().position(index).limit(index + length);
/* 402 */       duplicate.get(bytes, 0, length);
/*     */     } 
/* 404 */     return new String(bytes, offset, length, charset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int calculateCapacity(int neededCapacity) {
/* 412 */     Assert.isTrue((neededCapacity >= 0), "'neededCapacity' must >= 0");
/*     */     
/* 414 */     if (neededCapacity == 4194304) {
/* 415 */       return 4194304;
/*     */     }
/* 417 */     if (neededCapacity > 4194304) {
/* 418 */       int i = neededCapacity / 4194304 * 4194304;
/* 419 */       if (i > 2143289343) {
/* 420 */         i = Integer.MAX_VALUE;
/*     */       } else {
/*     */         
/* 423 */         i += 4194304;
/*     */       } 
/* 425 */       return i;
/*     */     } 
/*     */     
/* 428 */     int newCapacity = 64;
/* 429 */     while (newCapacity < neededCapacity) {
/* 430 */       newCapacity <<= 1;
/*     */     }
/* 432 */     return Math.min(newCapacity, 2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 439 */     if (this == other) {
/* 440 */       return true;
/*     */     }
/* 442 */     if (!(other instanceof DefaultDataBuffer)) {
/* 443 */       return false;
/*     */     }
/* 445 */     DefaultDataBuffer otherBuffer = (DefaultDataBuffer)other;
/* 446 */     return (this.readPosition == otherBuffer.readPosition && this.writePosition == otherBuffer.writePosition && this.byteBuffer
/*     */       
/* 448 */       .equals(otherBuffer.byteBuffer));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 453 */     return this.byteBuffer.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 458 */     return String.format("DefaultDataBuffer (r: %d, w: %d, c: %d)", new Object[] {
/* 459 */           Integer.valueOf(this.readPosition), Integer.valueOf(this.writePosition), Integer.valueOf(this.capacity)
/*     */         });
/*     */   }
/*     */   
/*     */   private void checkIndex(int index, int length) {
/* 464 */     assertIndex((index >= 0), "index %d must be >= 0", new Object[] { Integer.valueOf(index) });
/* 465 */     assertIndex((length >= 0), "length %d must be >= 0", new Object[] { Integer.valueOf(index) });
/* 466 */     assertIndex((index <= this.capacity), "index %d must be <= %d", new Object[] { Integer.valueOf(index), Integer.valueOf(this.capacity) });
/* 467 */     assertIndex((length <= this.capacity), "length %d must be <= %d", new Object[] { Integer.valueOf(index), Integer.valueOf(this.capacity) });
/*     */   }
/*     */   
/*     */   private void assertIndex(boolean expression, String format, Object... args) {
/* 471 */     if (!expression) {
/* 472 */       String message = String.format(format, args);
/* 473 */       throw new IndexOutOfBoundsException(message);
/*     */     } 
/*     */   }
/*     */   
/*     */   private class DefaultDataBufferInputStream
/*     */     extends InputStream {
/*     */     private DefaultDataBufferInputStream() {}
/*     */     
/*     */     public int available() {
/* 482 */       return DefaultDataBuffer.this.readableByteCount();
/*     */     }
/*     */ 
/*     */     
/*     */     public int read() {
/* 487 */       return (available() > 0) ? (DefaultDataBuffer.this.read() & 0xFF) : -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int read(byte[] bytes, int off, int len) throws IOException {
/* 492 */       int available = available();
/* 493 */       if (available > 0) {
/* 494 */         len = Math.min(len, available);
/* 495 */         DefaultDataBuffer.this.read(bytes, off, len);
/* 496 */         return len;
/*     */       } 
/*     */       
/* 499 */       return -1;
/*     */     }
/*     */   }
/*     */   
/*     */   private class DefaultDataBufferOutputStream
/*     */     extends OutputStream
/*     */   {
/*     */     private DefaultDataBufferOutputStream() {}
/*     */     
/*     */     public void write(int b) throws IOException {
/* 509 */       DefaultDataBuffer.this.write((byte)b);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] bytes, int off, int len) throws IOException {
/* 514 */       DefaultDataBuffer.this.write(bytes, off, len);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SlicedDefaultDataBuffer
/*     */     extends DefaultDataBuffer
/*     */   {
/*     */     SlicedDefaultDataBuffer(ByteBuffer byteBuffer, DefaultDataBufferFactory dataBufferFactory, int length) {
/* 522 */       super(dataBufferFactory, byteBuffer);
/* 523 */       writePosition(length);
/*     */     }
/*     */ 
/*     */     
/*     */     public DefaultDataBuffer capacity(int newCapacity) {
/* 528 */       throw new UnsupportedOperationException("Changing the capacity of a sliced buffer is not supported");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\buffer\DefaultDataBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */