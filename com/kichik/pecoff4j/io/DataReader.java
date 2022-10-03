/*     */ package com.kichik.pecoff4j.io;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.EOFException;
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
/*     */ public class DataReader
/*     */   implements IDataReader
/*     */ {
/*     */   private InputStream dis;
/*  20 */   private int position = 0;
/*     */   
/*     */   public DataReader(byte[] buffer) {
/*  23 */     this.dis = new BufferedInputStream(new ByteArrayInputStream(buffer));
/*     */   }
/*     */   
/*     */   public DataReader(byte[] buffer, int offset, int length) {
/*  27 */     this.dis = new BufferedInputStream(new ByteArrayInputStream(buffer, offset, length));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataReader(InputStream is) {
/*  32 */     if (is instanceof BufferedInputStream) {
/*  33 */       this.dis = is;
/*     */     } else {
/*  35 */       this.dis = new BufferedInputStream(is);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int readByte() throws IOException {
/*  41 */     this.position++;
/*  42 */     return safeRead();
/*     */   }
/*     */ 
/*     */   
/*     */   public int readWord() throws IOException {
/*  47 */     this.position += 2;
/*  48 */     return safeRead() | safeRead() << 8;
/*     */   }
/*     */ 
/*     */   
/*     */   public long readLong() throws IOException {
/*  53 */     return readDoubleWord() & 0xFFFFFFFFL | 
/*  54 */       readDoubleWord() << 32L;
/*     */   }
/*     */ 
/*     */   
/*     */   public int readDoubleWord() throws IOException {
/*  59 */     this.position += 4;
/*  60 */     return safeRead() | safeRead() << 8 | safeRead() << 16 | 
/*  61 */       safeRead() << 24;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPosition() {
/*  66 */     return this.position;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasMore() throws IOException {
/*  72 */     return (this.dis.available() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void jumpTo(int location) throws IOException {
/*  77 */     if (location < this.position) {
/*  78 */       throw new IOException("DataReader does not support scanning backwards (" + location + ")");
/*     */     }
/*     */     
/*  81 */     if (location > this.position) {
/*  82 */       skipBytes(location - this.position);
/*     */     }
/*     */   }
/*     */   
/*     */   public void skipBytes(int numBytes) throws IOException {
/*  87 */     this.position += numBytes;
/*  88 */     for (int i = 0; i < numBytes; i++) {
/*  89 */       safeRead();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  95 */     this.dis.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(byte[] b) throws IOException {
/* 100 */     this.position += b.length;
/* 101 */     safeRead(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public String readUtf(int size) throws IOException {
/* 106 */     this.position += size;
/* 107 */     byte[] b = new byte[size];
/* 108 */     safeRead(b);
/* 109 */     int i = 0;
/* 110 */     for (; i < b.length && 
/* 111 */       b[i] != 0; i++);
/*     */ 
/*     */     
/* 114 */     return new String(b, 0, i);
/*     */   }
/*     */ 
/*     */   
/*     */   public String readUtf() throws IOException {
/* 119 */     StringBuilder sb = new StringBuilder();
/* 120 */     int c = 0;
/* 121 */     while ((c = readByte()) != 0) {
/* 122 */       if (c == -1)
/* 123 */         throw new IOException("Unexpected end of stream"); 
/* 124 */       sb.append((char)c);
/*     */     } 
/* 126 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String readUnicode() throws IOException {
/* 131 */     StringBuilder sb = new StringBuilder();
/* 132 */     char c = Character.MIN_VALUE;
/* 133 */     while ((c = (char)readWord()) != '\000') {
/* 134 */       sb.append(c);
/*     */     }
/* 136 */     if (sb.length() == 0) {
/* 137 */       return null;
/*     */     }
/* 139 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String readUnicode(int size) throws IOException {
/* 144 */     StringBuilder sb = new StringBuilder();
/* 145 */     for (int i = 0; i < size; i++) {
/* 146 */       sb.append((char)readWord());
/*     */     }
/* 148 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] readAll() throws IOException {
/* 153 */     byte[] all = new byte[this.dis.available()];
/* 154 */     read(all);
/* 155 */     return all;
/*     */   }
/*     */   
/*     */   private int safeRead() throws IOException {
/* 159 */     int b = this.dis.read();
/* 160 */     if (b == -1)
/* 161 */       throw new EOFException("Expected to read bytes from the stream"); 
/* 162 */     return b;
/*     */   }
/*     */   
/*     */   private void safeRead(byte[] b) throws IOException {
/* 166 */     int read = this.dis.read(b);
/* 167 */     if (read != b.length)
/* 168 */       throw new EOFException("Expected to read bytes from the stream"); 
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\io\DataReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */