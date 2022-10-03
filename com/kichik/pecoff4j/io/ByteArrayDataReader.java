/*     */ package com.kichik.pecoff4j.io;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ByteArrayDataReader
/*     */   implements IDataReader
/*     */ {
/*     */   private byte[] data;
/*     */   private int position;
/*     */   private int offset;
/*     */   private int length;
/*     */   
/*     */   public ByteArrayDataReader(byte[] data) {
/*  23 */     this.data = data;
/*  24 */     this.length = data.length;
/*     */   }
/*     */   
/*     */   public ByteArrayDataReader(byte[] data, int offset, int length) {
/*  28 */     this.data = data;
/*  29 */     this.offset = offset;
/*  30 */     this.length = length;
/*     */     
/*  32 */     if (this.length + this.offset > data.length) {
/*  33 */       throw new IndexOutOfBoundsException("length [" + length + "] + offset [" + offset + "] > data.length [" + data.length + "]");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPosition() {
/*  44 */     return this.position;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasMore() throws IOException {
/*  50 */     return (this.position < this.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public void jumpTo(int location) throws IOException {
/*  55 */     this.position = location;
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(byte[] b) throws IOException {
/*  60 */     for (int i = 0; i < b.length; i++) {
/*  61 */       b[i] = this.data[this.offset + this.position + i];
/*     */     }
/*  63 */     this.position += b.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public int readByte() throws IOException {
/*  68 */     if (!hasMore())
/*  69 */       throw new EOFException("End of stream"); 
/*  70 */     return (char)(this.data[this.offset + this.position++] & 0xFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public long readLong() throws IOException {
/*  75 */     return readDoubleWord() | readDoubleWord() << 32L;
/*     */   }
/*     */ 
/*     */   
/*     */   public int readDoubleWord() throws IOException {
/*  80 */     return readWord() | readWord() << 16;
/*     */   }
/*     */ 
/*     */   
/*     */   public String readUtf(int size) throws IOException {
/*  85 */     byte[] b = new byte[size];
/*  86 */     read(b);
/*  87 */     return new String(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public String readUtf() throws IOException {
/*  92 */     StringBuilder sb = new StringBuilder();
/*     */     while (true) {
/*  94 */       char c = (char)readByte();
/*  95 */       if (c == '\000') {
/*     */         break;
/*     */       }
/*  98 */       sb.append(c);
/*     */     } 
/* 100 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public int readWord() throws IOException {
/* 105 */     return readByte() | readByte() << 8;
/*     */   }
/*     */ 
/*     */   
/*     */   public void skipBytes(int numBytes) throws IOException {
/* 110 */     this.position += numBytes;
/*     */   }
/*     */ 
/*     */   
/*     */   public String readUnicode() throws IOException {
/* 115 */     StringBuilder sb = new StringBuilder();
/* 116 */     char c = Character.MIN_VALUE;
/* 117 */     while ((c = (char)readWord()) != '\000') {
/* 118 */       sb.append(c);
/*     */     }
/* 120 */     if (sb.length() == 0) {
/* 121 */       return null;
/*     */     }
/* 123 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String readUnicode(int size) throws IOException {
/* 128 */     StringBuilder sb = new StringBuilder();
/* 129 */     for (int i = 0; i < size; i++) {
/* 130 */       sb.append((char)readWord());
/*     */     }
/* 132 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] readAll() throws IOException {
/* 137 */     byte[] result = Arrays.copyOfRange(this.data, this.offset + this.position, this.offset + this.length);
/*     */     
/* 139 */     this.position = this.length;
/* 140 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\io\ByteArrayDataReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */