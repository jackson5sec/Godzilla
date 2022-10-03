/*     */ package com.kitfox.svg.animation.parser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.nio.charset.Charset;
/*     */ 
/*     */ 
/*     */ public class SimpleCharStream
/*     */   extends AbstractCharStream
/*     */ {
/*     */   protected Reader inputStream;
/*     */   
/*     */   protected int streamRead(char[] buffer, int offset, int len) throws IOException {
/*  16 */     return this.inputStream.read(buffer, offset, len);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void streamClose() throws IOException {
/*  21 */     this.inputStream.close();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void fillBuff() throws IOException {
/*  26 */     if (this.maxNextCharInd == this.available)
/*     */     {
/*  28 */       if (this.available == this.bufsize) {
/*     */         
/*  30 */         if (this.tokenBegin > 2048) {
/*     */           
/*  32 */           this.maxNextCharInd = 0;
/*  33 */           this.bufpos = 0;
/*  34 */           this.available = this.tokenBegin;
/*     */         
/*     */         }
/*  37 */         else if (this.tokenBegin < 0) {
/*  38 */           this.maxNextCharInd = 0;
/*  39 */           this.bufpos = 0;
/*     */         } else {
/*     */           
/*  42 */           expandBuff(false);
/*     */         }
/*     */       
/*  45 */       } else if (this.available > this.tokenBegin) {
/*  46 */         this.available = this.bufsize;
/*     */       }
/*  48 */       else if (this.tokenBegin - this.available < 2048) {
/*  49 */         expandBuff(true);
/*     */       } else {
/*  51 */         this.available = this.tokenBegin;
/*     */       } 
/*     */     }
/*     */     try {
/*  55 */       int i = this.inputStream.read(this.buffer, this.maxNextCharInd, this.available - this.maxNextCharInd);
/*  56 */       if (i == -1) {
/*     */         
/*  58 */         this.inputStream.close();
/*  59 */         throw new IOException();
/*     */       } 
/*  61 */       this.maxNextCharInd += i;
/*     */       
/*     */       return;
/*  64 */     } catch (IOException e) {
/*  65 */       this.bufpos--;
/*  66 */       backup(0);
/*  67 */       if (this.tokenBegin == -1)
/*  68 */         this.tokenBegin = this.bufpos; 
/*  69 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public char readChar() throws IOException {
/*  76 */     if (this.inBuf > 0) {
/*     */       
/*  78 */       this.inBuf--;
/*     */       
/*  80 */       if (++this.bufpos == this.bufsize) {
/*  81 */         this.bufpos = 0;
/*     */       }
/*  83 */       return this.buffer[this.bufpos];
/*     */     } 
/*     */     
/*  86 */     this.bufpos++;
/*  87 */     if (this.bufpos >= this.maxNextCharInd) {
/*  88 */       fillBuff();
/*     */     }
/*  90 */     char c = this.buffer[this.bufpos];
/*     */     
/*  92 */     if (isTrackLineColumn())
/*  93 */       updateLineColumn(c); 
/*  94 */     return c;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleCharStream(Reader dstream, int startline, int startcolumn, int buffersize) {
/* 103 */     super(startline, startcolumn, buffersize);
/* 104 */     this.inputStream = dstream;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleCharStream(Reader dstream, int startline, int startcolumn) {
/* 112 */     this(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleCharStream(Reader dstream) {
/* 118 */     this(dstream, 1, 1, 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void reInit(Reader dstream) {
/* 124 */     reInit(dstream, 1, 1, 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reInit(Reader dstream, int startline, int startcolumn) {
/* 132 */     reInit(dstream, startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reInit(Reader dstream, int startline, int startcolumn, int buffersize) {
/* 141 */     this.inputStream = dstream;
/* 142 */     reInit(startline, startcolumn, buffersize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleCharStream(InputStream dstream, Charset encoding, int startline, int startcolumn, int buffersize) {
/* 152 */     this(new InputStreamReader(dstream, encoding), startline, startcolumn, buffersize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleCharStream(InputStream dstream, Charset encoding, int startline, int startcolumn) {
/* 161 */     this(dstream, encoding, startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleCharStream(InputStream dstream, Charset encoding) {
/* 168 */     this(dstream, encoding, 1, 1, 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reInit(InputStream dstream, Charset encoding) {
/* 175 */     reInit(dstream, encoding, 1, 1, 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reInit(InputStream dstream, Charset encoding, int startline, int startcolumn) {
/* 184 */     reInit(dstream, encoding, startline, startcolumn, 4096);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reInit(InputStream dstream, Charset encoding, int startline, int startcolumn, int buffersize) {
/* 194 */     reInit(new InputStreamReader(dstream, encoding), startline, startcolumn, buffersize);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\parser\SimpleCharStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */