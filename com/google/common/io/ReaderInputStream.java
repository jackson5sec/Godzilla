/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.UnsignedBytes;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CoderResult;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import java.util.Arrays;
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
/*     */ @GwtIncompatible
/*     */ final class ReaderInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private final Reader reader;
/*     */   private final CharsetEncoder encoder;
/*  50 */   private final byte[] singleByte = new byte[1];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CharBuffer charBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ByteBuffer byteBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean endOfInput;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean draining;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean doneFlushing;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ReaderInputStream(Reader reader, Charset charset, int bufferSize) {
/*  82 */     this(reader, charset
/*     */ 
/*     */         
/*  85 */         .newEncoder()
/*  86 */         .onMalformedInput(CodingErrorAction.REPLACE)
/*  87 */         .onUnmappableCharacter(CodingErrorAction.REPLACE), bufferSize);
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
/*     */   ReaderInputStream(Reader reader, CharsetEncoder encoder, int bufferSize) {
/* 101 */     this.reader = (Reader)Preconditions.checkNotNull(reader);
/* 102 */     this.encoder = (CharsetEncoder)Preconditions.checkNotNull(encoder);
/* 103 */     Preconditions.checkArgument((bufferSize > 0), "bufferSize must be positive: %s", bufferSize);
/* 104 */     encoder.reset();
/*     */     
/* 106 */     this.charBuffer = CharBuffer.allocate(bufferSize);
/* 107 */     this.charBuffer.flip();
/*     */     
/* 109 */     this.byteBuffer = ByteBuffer.allocate(bufferSize);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 114 */     this.reader.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/* 119 */     return (read(this.singleByte) == 1) ? UnsignedBytes.toInt(this.singleByte[0]) : -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/*     */     CoderResult result;
/* 127 */     Preconditions.checkPositionIndexes(off, off + len, b.length);
/* 128 */     if (len == 0) {
/* 129 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 133 */     int totalBytesRead = 0;
/* 134 */     boolean doneEncoding = this.endOfInput;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     label39: while (true) {
/* 140 */       if (this.draining) {
/* 141 */         totalBytesRead += drain(b, off + totalBytesRead, len - totalBytesRead);
/* 142 */         if (totalBytesRead == len || this.doneFlushing) {
/* 143 */           return (totalBytesRead > 0) ? totalBytesRead : -1;
/*     */         }
/* 145 */         this.draining = false;
/* 146 */         this.byteBuffer.clear();
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       while (true)
/* 153 */       { if (this.doneFlushing) {
/* 154 */           result = CoderResult.UNDERFLOW;
/* 155 */         } else if (doneEncoding) {
/* 156 */           result = this.encoder.flush(this.byteBuffer);
/*     */         } else {
/* 158 */           result = this.encoder.encode(this.charBuffer, this.byteBuffer, this.endOfInput);
/*     */         } 
/*     */         
/* 161 */         if (result.isOverflow()) {
/*     */           
/* 163 */           startDraining(true); continue label39;
/*     */         } 
/* 165 */         if (result.isUnderflow()) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 170 */           if (doneEncoding) {
/* 171 */             this.doneFlushing = true;
/* 172 */             startDraining(false); continue label39;
/*     */           } 
/* 174 */           if (this.endOfInput) {
/* 175 */             doneEncoding = true; continue;
/*     */           } 
/* 177 */           readMoreChars(); continue;
/*     */         } 
/* 179 */         if (result.isError())
/*     */           break;  }  break;
/* 181 */     }  result.throwException();
/* 182 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static CharBuffer grow(CharBuffer buf) {
/* 190 */     char[] copy = Arrays.copyOf(buf.array(), buf.capacity() * 2);
/* 191 */     CharBuffer bigger = CharBuffer.wrap(copy);
/* 192 */     bigger.position(buf.position());
/* 193 */     bigger.limit(buf.limit());
/* 194 */     return bigger;
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
/*     */   private void readMoreChars() throws IOException {
/* 207 */     if (availableCapacity(this.charBuffer) == 0) {
/* 208 */       if (this.charBuffer.position() > 0) {
/*     */         
/* 210 */         this.charBuffer.compact().flip();
/*     */       } else {
/*     */         
/* 213 */         this.charBuffer = grow(this.charBuffer);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 218 */     int limit = this.charBuffer.limit();
/* 219 */     int numChars = this.reader.read(this.charBuffer.array(), limit, availableCapacity(this.charBuffer));
/* 220 */     if (numChars == -1) {
/* 221 */       this.endOfInput = true;
/*     */     } else {
/* 223 */       this.charBuffer.limit(limit + numChars);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static int availableCapacity(Buffer buffer) {
/* 229 */     return buffer.capacity() - buffer.limit();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void startDraining(boolean overflow) {
/* 238 */     this.byteBuffer.flip();
/* 239 */     if (overflow && this.byteBuffer.remaining() == 0) {
/* 240 */       this.byteBuffer = ByteBuffer.allocate(this.byteBuffer.capacity() * 2);
/*     */     } else {
/* 242 */       this.draining = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int drain(byte[] b, int off, int len) {
/* 251 */     int remaining = Math.min(len, this.byteBuffer.remaining());
/* 252 */     this.byteBuffer.get(b, off, remaining);
/* 253 */     return remaining;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\io\ReaderInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */