/*     */ package com.kichik.pecoff4j.io;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DataWriter
/*     */   implements IDataWriter
/*     */ {
/*     */   private BufferedOutputStream out;
/*     */   private int position;
/*     */   
/*     */   public DataWriter(File output) throws FileNotFoundException {
/*  24 */     this(new FileOutputStream(output));
/*     */   }
/*     */   
/*     */   public DataWriter(OutputStream out) {
/*  28 */     this.out = new BufferedOutputStream(out);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeByte(int b) throws IOException {
/*  33 */     this.out.write(b);
/*  34 */     this.position++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeByte(int b, int count) throws IOException {
/*  39 */     for (int i = 0; i < count; i++) {
/*  40 */       this.out.write(b);
/*     */     }
/*  42 */     this.position += count;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeBytes(byte[] b) throws IOException {
/*  47 */     this.out.write(b);
/*  48 */     this.position += b.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeDoubleWord(int dw) throws IOException {
/*  53 */     this.out.write(dw & 0xFF);
/*  54 */     this.out.write(dw >> 8 & 0xFF);
/*  55 */     this.out.write(dw >> 16 & 0xFF);
/*  56 */     this.out.write(dw >> 24 & 0xFF);
/*  57 */     this.position += 4;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeWord(int w) throws IOException {
/*  62 */     this.out.write(w & 0xFF);
/*  63 */     this.out.write(w >> 8 & 0xFF);
/*  64 */     this.position += 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeLong(long l) throws IOException {
/*  69 */     writeDoubleWord((int)l);
/*  70 */     writeDoubleWord((int)(l >> 32L));
/*     */   }
/*     */   
/*     */   public void flush() throws IOException {
/*  74 */     this.out.flush();
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/*  78 */     this.out.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPosition() {
/*  83 */     return this.position;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeUtf(String s, int len) throws IOException {
/*  88 */     byte[] b = s.getBytes();
/*  89 */     int i = 0;
/*  90 */     for (; i < b.length && i < len; i++) {
/*  91 */       this.out.write(b[i]);
/*     */     }
/*  93 */     for (; i < len; i++) {
/*  94 */       this.out.write(0);
/*     */     }
/*  96 */     this.position += len;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeUtf(String s) throws IOException {
/* 101 */     byte[] b = s.getBytes();
/* 102 */     this.out.write(b);
/* 103 */     this.out.write(0);
/* 104 */     this.position += b.length + 1;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kichik\pecoff4j\io\DataWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */