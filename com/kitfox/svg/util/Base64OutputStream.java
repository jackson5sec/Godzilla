/*     */ package com.kitfox.svg.util;
/*     */ 
/*     */ import java.io.FilterOutputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Base64OutputStream
/*     */   extends FilterOutputStream
/*     */   implements Base64Consts
/*     */ {
/*     */   int buf;
/*     */   int bitsUsed;
/*     */   int charsPrinted;
/*     */   
/*     */   public Base64OutputStream(OutputStream out) {
/*  56 */     super(out);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  62 */     writeBits();
/*  63 */     super.close();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/*  69 */     this.buf = this.buf << 8 | b & 0xFF;
/*  70 */     this.bitsUsed += 8;
/*  71 */     if (this.bitsUsed == 24)
/*     */     {
/*  73 */       writeBits();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeBits() throws IOException {
/*     */     int padSize;
/*  81 */     switch (this.bitsUsed) {
/*     */ 
/*     */       
/*     */       case 8:
/*  85 */         this.bitsUsed = 12;
/*  86 */         this.buf <<= 4;
/*  87 */         padSize = 2;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 16:
/*  92 */         this.bitsUsed = 18;
/*  93 */         this.buf <<= 2;
/*  94 */         padSize = 1;
/*     */         break;
/*     */ 
/*     */       
/*     */       default:
/*  99 */         padSize = 0;
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 104 */     if (this.charsPrinted == 76) {
/*     */       
/* 106 */       this.out.write(13);
/* 107 */       this.out.write(10);
/* 108 */       this.charsPrinted = 0;
/*     */     } 
/*     */     
/* 111 */     for (; this.bitsUsed > 0; this.bitsUsed -= 6) {
/*     */       
/* 113 */       int b = this.buf >> this.bitsUsed - 6 & 0x3F;
/* 114 */       this.out.write("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".charAt(b));
/*     */     } 
/*     */     
/* 117 */     for (int i = 0; i < padSize; i++)
/*     */     {
/* 119 */       this.out.write(61);
/*     */     }
/*     */     
/* 122 */     this.charsPrinted += 4;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\sv\\util\Base64OutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */