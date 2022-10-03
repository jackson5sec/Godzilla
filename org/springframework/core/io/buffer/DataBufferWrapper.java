/*     */ package org.springframework.core.io.buffer;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.function.IntPredicate;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class DataBufferWrapper
/*     */   implements DataBuffer
/*     */ {
/*     */   private final DataBuffer delegate;
/*     */   
/*     */   public DataBufferWrapper(DataBuffer delegate) {
/*  46 */     Assert.notNull(delegate, "Delegate must not be null");
/*  47 */     this.delegate = delegate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DataBuffer dataBuffer() {
/*  54 */     return this.delegate;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataBufferFactory factory() {
/*  59 */     return this.delegate.factory();
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(IntPredicate predicate, int fromIndex) {
/*  64 */     return this.delegate.indexOf(predicate, fromIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(IntPredicate predicate, int fromIndex) {
/*  69 */     return this.delegate.lastIndexOf(predicate, fromIndex);
/*     */   }
/*     */ 
/*     */   
/*     */   public int readableByteCount() {
/*  74 */     return this.delegate.readableByteCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public int writableByteCount() {
/*  79 */     return this.delegate.writableByteCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public int capacity() {
/*  84 */     return this.delegate.capacity();
/*     */   }
/*     */ 
/*     */   
/*     */   public DataBuffer capacity(int capacity) {
/*  89 */     return this.delegate.capacity(capacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataBuffer ensureCapacity(int capacity) {
/*  94 */     return this.delegate.ensureCapacity(capacity);
/*     */   }
/*     */ 
/*     */   
/*     */   public int readPosition() {
/*  99 */     return this.delegate.readPosition();
/*     */   }
/*     */ 
/*     */   
/*     */   public DataBuffer readPosition(int readPosition) {
/* 104 */     return this.delegate.readPosition(readPosition);
/*     */   }
/*     */ 
/*     */   
/*     */   public int writePosition() {
/* 109 */     return this.delegate.writePosition();
/*     */   }
/*     */ 
/*     */   
/*     */   public DataBuffer writePosition(int writePosition) {
/* 114 */     return this.delegate.writePosition(writePosition);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getByte(int index) {
/* 119 */     return this.delegate.getByte(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte read() {
/* 124 */     return this.delegate.read();
/*     */   }
/*     */ 
/*     */   
/*     */   public DataBuffer read(byte[] destination) {
/* 129 */     return this.delegate.read(destination);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataBuffer read(byte[] destination, int offset, int length) {
/* 134 */     return this.delegate.read(destination, offset, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataBuffer write(byte b) {
/* 139 */     return this.delegate.write(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataBuffer write(byte[] source) {
/* 144 */     return this.delegate.write(source);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataBuffer write(byte[] source, int offset, int length) {
/* 149 */     return this.delegate.write(source, offset, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataBuffer write(DataBuffer... buffers) {
/* 154 */     return this.delegate.write(buffers);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataBuffer write(ByteBuffer... buffers) {
/* 159 */     return this.delegate.write(buffers);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DataBuffer write(CharSequence charSequence, Charset charset) {
/* 165 */     return this.delegate.write(charSequence, charset);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataBuffer slice(int index, int length) {
/* 170 */     return this.delegate.slice(index, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataBuffer retainedSlice(int index, int length) {
/* 175 */     return this.delegate.retainedSlice(index, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer asByteBuffer() {
/* 180 */     return this.delegate.asByteBuffer();
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer asByteBuffer(int index, int length) {
/* 185 */     return this.delegate.asByteBuffer(index, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream asInputStream() {
/* 190 */     return this.delegate.asInputStream();
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream asInputStream(boolean releaseOnClose) {
/* 195 */     return this.delegate.asInputStream(releaseOnClose);
/*     */   }
/*     */ 
/*     */   
/*     */   public OutputStream asOutputStream() {
/* 200 */     return this.delegate.asOutputStream();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString(Charset charset) {
/* 205 */     return this.delegate.toString(charset);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString(int index, int length, Charset charset) {
/* 210 */     return this.delegate.toString(index, length, charset);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\buffer\DataBufferWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */