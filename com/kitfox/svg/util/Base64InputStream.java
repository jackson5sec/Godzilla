/*     */ package com.kitfox.svg.util;
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Base64InputStream
/*     */   extends FilterInputStream
/*     */   implements Base64Consts
/*     */ {
/*  50 */   static final HashMap<Byte, Integer> lookup64 = new HashMap<Byte, Integer>();
/*     */   static {
/*  52 */     byte[] ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes();
/*  53 */     for (int i = 0; i < ch.length; i++)
/*     */     {
/*  55 */       lookup64.put(new Byte(ch[i]), new Integer(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   int buf;
/*     */   
/*     */   int charsInBuf;
/*     */   
/*     */   public Base64InputStream(InputStream in) {
/*  65 */     super(in);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException {
/*  71 */     for (int i = 0; i < len; i++) {
/*     */       
/*  73 */       int val = read();
/*  74 */       if (val == -1)
/*     */       {
/*  76 */         return (i == 0) ? -1 : i;
/*     */       }
/*  78 */       b[off + i] = (byte)val;
/*     */     } 
/*  80 */     return len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  87 */     if (this.charsInBuf == 0) {
/*     */       
/*  89 */       fillBuffer();
/*  90 */       if (this.charsInBuf == 0)
/*     */       {
/*  92 */         return -1;
/*     */       }
/*     */     } 
/*     */     
/*  96 */     return this.buf >> --this.charsInBuf * 8 & 0xFF;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void fillBuffer() throws IOException {
/* 102 */     int bitsRead = 0;
/* 103 */     while (bitsRead < 24) {
/*     */       
/* 105 */       int val = this.in.read();
/* 106 */       if (val == -1 || val == 61)
/*     */         break; 
/* 108 */       Integer lval = lookup64.get(new Byte((byte)val));
/* 109 */       if (lval == null)
/*     */         continue; 
/* 111 */       this.buf = this.buf << 6 | lval.byteValue();
/* 112 */       bitsRead += 6;
/*     */     } 
/*     */     
/* 115 */     switch (bitsRead) {
/*     */ 
/*     */       
/*     */       case 6:
/* 119 */         throw new RuntimeException("Invalid termination of base64 encoding.");
/*     */ 
/*     */       
/*     */       case 12:
/* 123 */         this.buf >>= 4;
/* 124 */         bitsRead = 8;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 18:
/* 129 */         this.buf >>= 2;
/* 130 */         bitsRead = 16;
/*     */         break;
/*     */ 
/*     */       
/*     */       case 0:
/*     */       case 24:
/*     */         break;
/*     */ 
/*     */       
/*     */       default:
/* 140 */         assert false : "Should never encounter other bit counts";
/*     */         break;
/*     */     } 
/*     */     
/* 144 */     this.charsInBuf = bitsRead / 8;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\sv\\util\Base64InputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */