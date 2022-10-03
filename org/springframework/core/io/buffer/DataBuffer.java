/*     */ package org.springframework.core.io.buffer;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
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
/*     */ public interface DataBuffer
/*     */ {
/*     */   DataBufferFactory factory();
/*     */   
/*     */   int indexOf(IntPredicate paramIntPredicate, int paramInt);
/*     */   
/*     */   int lastIndexOf(IntPredicate paramIntPredicate, int paramInt);
/*     */   
/*     */   int readableByteCount();
/*     */   
/*     */   int writableByteCount();
/*     */   
/*     */   int capacity();
/*     */   
/*     */   DataBuffer capacity(int paramInt);
/*     */   
/*     */   default DataBuffer ensureCapacity(int capacity) {
/* 126 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int readPosition();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DataBuffer readPosition(int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int writePosition();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DataBuffer writePosition(int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   byte getByte(int paramInt);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   byte read();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DataBuffer read(byte[] paramArrayOfbyte);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DataBuffer read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DataBuffer write(byte paramByte);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DataBuffer write(byte[] paramArrayOfbyte);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DataBuffer write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DataBuffer write(DataBuffer... paramVarArgs);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   DataBuffer write(ByteBuffer... paramVarArgs);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default DataBuffer write(CharSequence charSequence, Charset charset) {
/* 247 */     Assert.notNull(charSequence, "CharSequence must not be null");
/* 248 */     Assert.notNull(charset, "Charset must not be null");
/* 249 */     if (charSequence.length() != 0) {
/*     */ 
/*     */       
/* 252 */       CharsetEncoder charsetEncoder = charset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
/* 253 */       CharBuffer inBuffer = CharBuffer.wrap(charSequence);
/* 254 */       int estimatedSize = (int)(inBuffer.remaining() * charsetEncoder.averageBytesPerChar());
/*     */       
/* 256 */       ByteBuffer outBuffer = ensureCapacity(estimatedSize).asByteBuffer(writePosition(), writableByteCount());
/*     */       
/*     */       while (true) {
/* 259 */         CoderResult cr = inBuffer.hasRemaining() ? charsetEncoder.encode(inBuffer, outBuffer, true) : CoderResult.UNDERFLOW;
/* 260 */         if (cr.isUnderflow()) {
/* 261 */           cr = charsetEncoder.flush(outBuffer);
/*     */         }
/* 263 */         if (cr.isUnderflow()) {
/*     */           break;
/*     */         }
/* 266 */         if (cr.isOverflow()) {
/* 267 */           writePosition(writePosition() + outBuffer.position());
/* 268 */           int maximumSize = (int)(inBuffer.remaining() * charsetEncoder.maxBytesPerChar());
/* 269 */           ensureCapacity(maximumSize);
/* 270 */           outBuffer = asByteBuffer(writePosition(), writableByteCount());
/*     */         } 
/*     */       } 
/* 273 */       writePosition(writePosition() + outBuffer.position());
/*     */     } 
/* 275 */     return this;
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
/*     */   DataBuffer slice(int paramInt1, int paramInt2);
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
/*     */   default DataBuffer retainedSlice(int index, int length) {
/* 306 */     return DataBufferUtils.retain(slice(index, length));
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
/*     */   ByteBuffer asByteBuffer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ByteBuffer asByteBuffer(int paramInt1, int paramInt2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   InputStream asInputStream();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   InputStream asInputStream(boolean paramBoolean);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   OutputStream asOutputStream();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default String toString(Charset charset) {
/* 366 */     Assert.notNull(charset, "Charset must not be null");
/* 367 */     return toString(readPosition(), readableByteCount(), charset);
/*     */   }
/*     */   
/*     */   String toString(int paramInt1, int paramInt2, Charset paramCharset);
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\buffer\DataBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */