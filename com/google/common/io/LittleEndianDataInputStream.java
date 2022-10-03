/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import com.google.common.primitives.Longs;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class LittleEndianDataInputStream
/*     */   extends FilterInputStream
/*     */   implements DataInput
/*     */ {
/*     */   public LittleEndianDataInputStream(InputStream in) {
/*  51 */     super((InputStream)Preconditions.checkNotNull(in));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public String readLine() {
/*  58 */     throw new UnsupportedOperationException("readLine is not supported");
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFully(byte[] b) throws IOException {
/*  63 */     ByteStreams.readFully(this, b);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFully(byte[] b, int off, int len) throws IOException {
/*  68 */     ByteStreams.readFully(this, b, off, len);
/*     */   }
/*     */ 
/*     */   
/*     */   public int skipBytes(int n) throws IOException {
/*  73 */     return (int)this.in.skip(n);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int readUnsignedByte() throws IOException {
/*  79 */     int b1 = this.in.read();
/*  80 */     if (0 > b1) {
/*  81 */       throw new EOFException();
/*     */     }
/*     */     
/*  84 */     return b1;
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
/*     */   public int readUnsignedShort() throws IOException {
/*  98 */     byte b1 = readAndCheckByte();
/*  99 */     byte b2 = readAndCheckByte();
/*     */     
/* 101 */     return Ints.fromBytes((byte)0, (byte)0, b2, b1);
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
/*     */   public int readInt() throws IOException {
/* 115 */     byte b1 = readAndCheckByte();
/* 116 */     byte b2 = readAndCheckByte();
/* 117 */     byte b3 = readAndCheckByte();
/* 118 */     byte b4 = readAndCheckByte();
/*     */     
/* 120 */     return Ints.fromBytes(b4, b3, b2, b1);
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
/*     */   public long readLong() throws IOException {
/* 134 */     byte b1 = readAndCheckByte();
/* 135 */     byte b2 = readAndCheckByte();
/* 136 */     byte b3 = readAndCheckByte();
/* 137 */     byte b4 = readAndCheckByte();
/* 138 */     byte b5 = readAndCheckByte();
/* 139 */     byte b6 = readAndCheckByte();
/* 140 */     byte b7 = readAndCheckByte();
/* 141 */     byte b8 = readAndCheckByte();
/*     */     
/* 143 */     return Longs.fromBytes(b8, b7, b6, b5, b4, b3, b2, b1);
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
/*     */   public float readFloat() throws IOException {
/* 157 */     return Float.intBitsToFloat(readInt());
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
/*     */   public double readDouble() throws IOException {
/* 171 */     return Double.longBitsToDouble(readLong());
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public String readUTF() throws IOException {
/* 177 */     return (new DataInputStream(this.in)).readUTF();
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
/*     */   public short readShort() throws IOException {
/* 191 */     return (short)readUnsignedShort();
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
/*     */   public char readChar() throws IOException {
/* 205 */     return (char)readUnsignedShort();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public byte readByte() throws IOException {
/* 211 */     return (byte)readUnsignedByte();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean readBoolean() throws IOException {
/* 217 */     return (readUnsignedByte() != 0);
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
/*     */   private byte readAndCheckByte() throws IOException, EOFException {
/* 229 */     int b1 = this.in.read();
/*     */     
/* 231 */     if (-1 == b1) {
/* 232 */       throw new EOFException();
/*     */     }
/*     */     
/* 235 */     return (byte)b1;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\io\LittleEndianDataInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */