/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutput;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.ReadableByteChannel;
/*     */ import java.nio.channels.WritableByteChannel;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Arrays;
/*     */ import java.util.Deque;
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
/*     */ public final class ByteStreams
/*     */ {
/*     */   private static final int BUFFER_SIZE = 8192;
/*     */   private static final int ZERO_COPY_CHUNK_SIZE = 524288;
/*     */   private static final int MAX_ARRAY_LEN = 2147483639;
/*     */   private static final int TO_BYTE_ARRAY_DEQUE_SIZE = 20;
/*     */   
/*     */   static byte[] createBuffer() {
/*  58 */     return new byte[8192];
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
/*     */   @CanIgnoreReturnValue
/*     */   public static long copy(InputStream from, OutputStream to) throws IOException {
/* 103 */     Preconditions.checkNotNull(from);
/* 104 */     Preconditions.checkNotNull(to);
/* 105 */     byte[] buf = createBuffer();
/* 106 */     long total = 0L;
/*     */     while (true) {
/* 108 */       int r = from.read(buf);
/* 109 */       if (r == -1) {
/*     */         break;
/*     */       }
/* 112 */       to.write(buf, 0, r);
/* 113 */       total += r;
/*     */     } 
/* 115 */     return total;
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
/*     */   @CanIgnoreReturnValue
/*     */   public static long copy(ReadableByteChannel from, WritableByteChannel to) throws IOException {
/* 129 */     Preconditions.checkNotNull(from);
/* 130 */     Preconditions.checkNotNull(to);
/* 131 */     if (from instanceof FileChannel) {
/* 132 */       FileChannel sourceChannel = (FileChannel)from;
/* 133 */       long oldPosition = sourceChannel.position();
/* 134 */       long position = oldPosition;
/*     */       
/*     */       while (true) {
/* 137 */         long copied = sourceChannel.transferTo(position, 524288L, to);
/* 138 */         position += copied;
/* 139 */         sourceChannel.position(position);
/* 140 */         if (copied <= 0L && position >= sourceChannel.size())
/* 141 */           return position - oldPosition; 
/*     */       } 
/*     */     } 
/* 144 */     ByteBuffer buf = ByteBuffer.wrap(createBuffer());
/* 145 */     long total = 0L;
/* 146 */     while (from.read(buf) != -1) {
/* 147 */       buf.flip();
/* 148 */       while (buf.hasRemaining()) {
/* 149 */         total += to.write(buf);
/*     */       }
/* 151 */       buf.clear();
/*     */     } 
/* 153 */     return total;
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
/*     */ 
/*     */   
/*     */   private static byte[] toByteArrayInternal(InputStream in, Deque<byte[]> bufs, int totalLen) throws IOException {
/* 172 */     int bufSize = 8192;
/* 173 */     for (; totalLen < 2147483639; 
/* 174 */       bufSize = IntMath.saturatedMultiply(bufSize, 2)) {
/* 175 */       byte[] buf = new byte[Math.min(bufSize, 2147483639 - totalLen)];
/* 176 */       bufs.add(buf);
/* 177 */       int off = 0;
/* 178 */       while (off < buf.length) {
/*     */         
/* 180 */         int r = in.read(buf, off, buf.length - off);
/* 181 */         if (r == -1) {
/* 182 */           return combineBuffers(bufs, totalLen);
/*     */         }
/* 184 */         off += r;
/* 185 */         totalLen += r;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 190 */     if (in.read() == -1)
/*     */     {
/* 192 */       return combineBuffers(bufs, 2147483639);
/*     */     }
/* 194 */     throw new OutOfMemoryError("input is too large to fit in a byte array");
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] combineBuffers(Deque<byte[]> bufs, int totalLen) {
/* 199 */     byte[] result = new byte[totalLen];
/* 200 */     int remaining = totalLen;
/* 201 */     while (remaining > 0) {
/* 202 */       byte[] buf = bufs.removeFirst();
/* 203 */       int bytesToCopy = Math.min(remaining, buf.length);
/* 204 */       int resultOffset = totalLen - remaining;
/* 205 */       System.arraycopy(buf, 0, result, resultOffset, bytesToCopy);
/* 206 */       remaining -= bytesToCopy;
/*     */     } 
/* 208 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] toByteArray(InputStream in) throws IOException {
/* 219 */     Preconditions.checkNotNull(in);
/* 220 */     return toByteArrayInternal(in, (Deque)new ArrayDeque<>(20), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static byte[] toByteArray(InputStream in, long expectedSize) throws IOException {
/* 229 */     Preconditions.checkArgument((expectedSize >= 0L), "expectedSize (%s) must be non-negative", expectedSize);
/* 230 */     if (expectedSize > 2147483639L) {
/* 231 */       throw new OutOfMemoryError(expectedSize + " bytes is too large to fit in a byte array");
/*     */     }
/*     */     
/* 234 */     byte[] bytes = new byte[(int)expectedSize];
/* 235 */     int remaining = (int)expectedSize;
/*     */     
/* 237 */     while (remaining > 0) {
/* 238 */       int off = (int)expectedSize - remaining;
/* 239 */       int read = in.read(bytes, off, remaining);
/* 240 */       if (read == -1)
/*     */       {
/*     */         
/* 243 */         return Arrays.copyOf(bytes, off);
/*     */       }
/* 245 */       remaining -= read;
/*     */     } 
/*     */ 
/*     */     
/* 249 */     int b = in.read();
/* 250 */     if (b == -1) {
/* 251 */       return bytes;
/*     */     }
/*     */ 
/*     */     
/* 255 */     Deque<byte[]> bufs = (Deque)new ArrayDeque<>(22);
/* 256 */     bufs.add(bytes);
/* 257 */     bufs.add(new byte[] { (byte)b });
/* 258 */     return toByteArrayInternal(in, bufs, bytes.length + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   @Beta
/*     */   public static long exhaust(InputStream in) throws IOException {
/* 270 */     long total = 0L;
/*     */     
/* 272 */     byte[] buf = createBuffer(); long read;
/* 273 */     while ((read = in.read(buf)) != -1L) {
/* 274 */       total += read;
/*     */     }
/* 276 */     return total;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static ByteArrayDataInput newDataInput(byte[] bytes) {
/* 285 */     return newDataInput(new ByteArrayInputStream(bytes));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static ByteArrayDataInput newDataInput(byte[] bytes, int start) {
/* 297 */     Preconditions.checkPositionIndex(start, bytes.length);
/* 298 */     return newDataInput(new ByteArrayInputStream(bytes, start, bytes.length - start));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static ByteArrayDataInput newDataInput(ByteArrayInputStream byteArrayInputStream) {
/* 310 */     return new ByteArrayDataInputStream((ByteArrayInputStream)Preconditions.checkNotNull(byteArrayInputStream));
/*     */   }
/*     */   
/*     */   private static class ByteArrayDataInputStream implements ByteArrayDataInput {
/*     */     final DataInput input;
/*     */     
/*     */     ByteArrayDataInputStream(ByteArrayInputStream byteArrayInputStream) {
/* 317 */       this.input = new DataInputStream(byteArrayInputStream);
/*     */     }
/*     */ 
/*     */     
/*     */     public void readFully(byte[] b) {
/*     */       try {
/* 323 */         this.input.readFully(b);
/* 324 */       } catch (IOException e) {
/* 325 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void readFully(byte[] b, int off, int len) {
/*     */       try {
/* 332 */         this.input.readFully(b, off, len);
/* 333 */       } catch (IOException e) {
/* 334 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int skipBytes(int n) {
/*     */       try {
/* 341 */         return this.input.skipBytes(n);
/* 342 */       } catch (IOException e) {
/* 343 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean readBoolean() {
/*     */       try {
/* 350 */         return this.input.readBoolean();
/* 351 */       } catch (IOException e) {
/* 352 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public byte readByte() {
/*     */       try {
/* 359 */         return this.input.readByte();
/* 360 */       } catch (EOFException e) {
/* 361 */         throw new IllegalStateException(e);
/* 362 */       } catch (IOException impossible) {
/* 363 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int readUnsignedByte() {
/*     */       try {
/* 370 */         return this.input.readUnsignedByte();
/* 371 */       } catch (IOException e) {
/* 372 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public short readShort() {
/*     */       try {
/* 379 */         return this.input.readShort();
/* 380 */       } catch (IOException e) {
/* 381 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int readUnsignedShort() {
/*     */       try {
/* 388 */         return this.input.readUnsignedShort();
/* 389 */       } catch (IOException e) {
/* 390 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public char readChar() {
/*     */       try {
/* 397 */         return this.input.readChar();
/* 398 */       } catch (IOException e) {
/* 399 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public int readInt() {
/*     */       try {
/* 406 */         return this.input.readInt();
/* 407 */       } catch (IOException e) {
/* 408 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public long readLong() {
/*     */       try {
/* 415 */         return this.input.readLong();
/* 416 */       } catch (IOException e) {
/* 417 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public float readFloat() {
/*     */       try {
/* 424 */         return this.input.readFloat();
/* 425 */       } catch (IOException e) {
/* 426 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public double readDouble() {
/*     */       try {
/* 433 */         return this.input.readDouble();
/* 434 */       } catch (IOException e) {
/* 435 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String readLine() {
/*     */       try {
/* 442 */         return this.input.readLine();
/* 443 */       } catch (IOException e) {
/* 444 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String readUTF() {
/*     */       try {
/* 451 */         return this.input.readUTF();
/* 452 */       } catch (IOException e) {
/* 453 */         throw new IllegalStateException(e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static ByteArrayDataOutput newDataOutput() {
/* 461 */     return newDataOutput(new ByteArrayOutputStream());
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
/*     */   @Beta
/*     */   public static ByteArrayDataOutput newDataOutput(int size) {
/* 474 */     if (size < 0) {
/* 475 */       throw new IllegalArgumentException(String.format("Invalid size: %s", new Object[] { Integer.valueOf(size) }));
/*     */     }
/* 477 */     return newDataOutput(new ByteArrayOutputStream(size));
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
/*     */   @Beta
/*     */   public static ByteArrayDataOutput newDataOutput(ByteArrayOutputStream byteArrayOutputSteam) {
/* 494 */     return new ByteArrayDataOutputStream((ByteArrayOutputStream)Preconditions.checkNotNull(byteArrayOutputSteam));
/*     */   }
/*     */   
/*     */   private static class ByteArrayDataOutputStream
/*     */     implements ByteArrayDataOutput {
/*     */     final DataOutput output;
/*     */     final ByteArrayOutputStream byteArrayOutputSteam;
/*     */     
/*     */     ByteArrayDataOutputStream(ByteArrayOutputStream byteArrayOutputSteam) {
/* 503 */       this.byteArrayOutputSteam = byteArrayOutputSteam;
/* 504 */       this.output = new DataOutputStream(byteArrayOutputSteam);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(int b) {
/*     */       try {
/* 510 */         this.output.write(b);
/* 511 */       } catch (IOException impossible) {
/* 512 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b) {
/*     */       try {
/* 519 */         this.output.write(b);
/* 520 */       } catch (IOException impossible) {
/* 521 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(byte[] b, int off, int len) {
/*     */       try {
/* 528 */         this.output.write(b, off, len);
/* 529 */       } catch (IOException impossible) {
/* 530 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeBoolean(boolean v) {
/*     */       try {
/* 537 */         this.output.writeBoolean(v);
/* 538 */       } catch (IOException impossible) {
/* 539 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeByte(int v) {
/*     */       try {
/* 546 */         this.output.writeByte(v);
/* 547 */       } catch (IOException impossible) {
/* 548 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeBytes(String s) {
/*     */       try {
/* 555 */         this.output.writeBytes(s);
/* 556 */       } catch (IOException impossible) {
/* 557 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeChar(int v) {
/*     */       try {
/* 564 */         this.output.writeChar(v);
/* 565 */       } catch (IOException impossible) {
/* 566 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeChars(String s) {
/*     */       try {
/* 573 */         this.output.writeChars(s);
/* 574 */       } catch (IOException impossible) {
/* 575 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeDouble(double v) {
/*     */       try {
/* 582 */         this.output.writeDouble(v);
/* 583 */       } catch (IOException impossible) {
/* 584 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeFloat(float v) {
/*     */       try {
/* 591 */         this.output.writeFloat(v);
/* 592 */       } catch (IOException impossible) {
/* 593 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeInt(int v) {
/*     */       try {
/* 600 */         this.output.writeInt(v);
/* 601 */       } catch (IOException impossible) {
/* 602 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeLong(long v) {
/*     */       try {
/* 609 */         this.output.writeLong(v);
/* 610 */       } catch (IOException impossible) {
/* 611 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeShort(int v) {
/*     */       try {
/* 618 */         this.output.writeShort(v);
/* 619 */       } catch (IOException impossible) {
/* 620 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void writeUTF(String s) {
/*     */       try {
/* 627 */         this.output.writeUTF(s);
/* 628 */       } catch (IOException impossible) {
/* 629 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] toByteArray() {
/* 635 */       return this.byteArrayOutputSteam.toByteArray();
/*     */     }
/*     */   }
/*     */   
/* 639 */   private static final OutputStream NULL_OUTPUT_STREAM = new OutputStream()
/*     */     {
/*     */       public void write(int b) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void write(byte[] b) {
/* 648 */         Preconditions.checkNotNull(b);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void write(byte[] b, int off, int len) {
/* 654 */         Preconditions.checkNotNull(b);
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 659 */         return "ByteStreams.nullOutputStream()";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static OutputStream nullOutputStream() {
/* 670 */     return NULL_OUTPUT_STREAM;
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
/*     */   @Beta
/*     */   public static InputStream limit(InputStream in, long limit) {
/* 683 */     return new LimitedInputStream(in, limit);
/*     */   }
/*     */   
/*     */   private static final class LimitedInputStream
/*     */     extends FilterInputStream {
/*     */     private long left;
/* 689 */     private long mark = -1L;
/*     */     
/*     */     LimitedInputStream(InputStream in, long limit) {
/* 692 */       super(in);
/* 693 */       Preconditions.checkNotNull(in);
/* 694 */       Preconditions.checkArgument((limit >= 0L), "limit must be non-negative");
/* 695 */       this.left = limit;
/*     */     }
/*     */ 
/*     */     
/*     */     public int available() throws IOException {
/* 700 */       return (int)Math.min(this.in.available(), this.left);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public synchronized void mark(int readLimit) {
/* 706 */       this.in.mark(readLimit);
/* 707 */       this.mark = this.left;
/*     */     }
/*     */ 
/*     */     
/*     */     public int read() throws IOException {
/* 712 */       if (this.left == 0L) {
/* 713 */         return -1;
/*     */       }
/*     */       
/* 716 */       int result = this.in.read();
/* 717 */       if (result != -1) {
/* 718 */         this.left--;
/*     */       }
/* 720 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public int read(byte[] b, int off, int len) throws IOException {
/* 725 */       if (this.left == 0L) {
/* 726 */         return -1;
/*     */       }
/*     */       
/* 729 */       len = (int)Math.min(len, this.left);
/* 730 */       int result = this.in.read(b, off, len);
/* 731 */       if (result != -1) {
/* 732 */         this.left -= result;
/*     */       }
/* 734 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public synchronized void reset() throws IOException {
/* 739 */       if (!this.in.markSupported()) {
/* 740 */         throw new IOException("Mark not supported");
/*     */       }
/* 742 */       if (this.mark == -1L) {
/* 743 */         throw new IOException("Mark not set");
/*     */       }
/*     */       
/* 746 */       this.in.reset();
/* 747 */       this.left = this.mark;
/*     */     }
/*     */ 
/*     */     
/*     */     public long skip(long n) throws IOException {
/* 752 */       n = Math.min(n, this.left);
/* 753 */       long skipped = this.in.skip(n);
/* 754 */       this.left -= skipped;
/* 755 */       return skipped;
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
/*     */   
/*     */   @Beta
/*     */   public static void readFully(InputStream in, byte[] b) throws IOException {
/* 770 */     readFully(in, b, 0, b.length);
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
/*     */   @Beta
/*     */   public static void readFully(InputStream in, byte[] b, int off, int len) throws IOException {
/* 787 */     int read = read(in, b, off, len);
/* 788 */     if (read != len) {
/* 789 */       throw new EOFException("reached end of stream after reading " + read + " bytes; " + len + " bytes expected");
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
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static void skipFully(InputStream in, long n) throws IOException {
/* 805 */     long skipped = skipUpTo(in, n);
/* 806 */     if (skipped < n) {
/* 807 */       throw new EOFException("reached end of stream after skipping " + skipped + " bytes; " + n + " bytes expected");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static long skipUpTo(InputStream in, long n) throws IOException {
/* 818 */     long totalSkipped = 0L;
/* 819 */     byte[] buf = createBuffer();
/*     */     
/* 821 */     while (totalSkipped < n) {
/* 822 */       long remaining = n - totalSkipped;
/* 823 */       long skipped = skipSafely(in, remaining);
/*     */       
/* 825 */       if (skipped == 0L) {
/*     */ 
/*     */         
/* 828 */         int skip = (int)Math.min(remaining, buf.length);
/* 829 */         if ((skipped = in.read(buf, 0, skip)) == -1L) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 835 */       totalSkipped += skipped;
/*     */     } 
/*     */     
/* 838 */     return totalSkipped;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long skipSafely(InputStream in, long n) throws IOException {
/* 849 */     int available = in.available();
/* 850 */     return (available == 0) ? 0L : in.skip(Math.min(available, n));
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
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T readBytes(InputStream input, ByteProcessor<T> processor) throws IOException {
/*     */     int read;
/* 865 */     Preconditions.checkNotNull(input);
/* 866 */     Preconditions.checkNotNull(processor);
/*     */     
/* 868 */     byte[] buf = createBuffer();
/*     */     
/*     */     do {
/* 871 */       read = input.read(buf);
/* 872 */     } while (read != -1 && processor.processBytes(buf, 0, read));
/* 873 */     return processor.getResult();
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
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   public static int read(InputStream in, byte[] b, int off, int len) throws IOException {
/* 903 */     Preconditions.checkNotNull(in);
/* 904 */     Preconditions.checkNotNull(b);
/* 905 */     if (len < 0) {
/* 906 */       throw new IndexOutOfBoundsException("len is negative");
/*     */     }
/* 908 */     int total = 0;
/* 909 */     while (total < len) {
/* 910 */       int result = in.read(b, off + total, len - total);
/* 911 */       if (result == -1) {
/*     */         break;
/*     */       }
/* 914 */       total += result;
/*     */     } 
/* 916 */     return total;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\io\ByteStreams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */