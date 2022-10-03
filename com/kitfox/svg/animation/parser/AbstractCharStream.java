/*     */ package com.kitfox.svg.animation.parser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractCharStream
/*     */   implements CharStream
/*     */ {
/*     */   public static final int DEFAULT_BUF_SIZE = 4096;
/*     */   
/*     */   static final int hexval(char c) throws IOException {
/*  17 */     switch (c) {
/*     */       
/*     */       case '0':
/*  20 */         return 0;
/*     */       case '1':
/*  22 */         return 1;
/*     */       case '2':
/*  24 */         return 2;
/*     */       case '3':
/*  26 */         return 3;
/*     */       case '4':
/*  28 */         return 4;
/*     */       case '5':
/*  30 */         return 5;
/*     */       case '6':
/*  32 */         return 6;
/*     */       case '7':
/*  34 */         return 7;
/*     */       case '8':
/*  36 */         return 8;
/*     */       case '9':
/*  38 */         return 9;
/*     */       case 'A':
/*     */       case 'a':
/*  41 */         return 10;
/*     */       case 'B':
/*     */       case 'b':
/*  44 */         return 11;
/*     */       case 'C':
/*     */       case 'c':
/*  47 */         return 12;
/*     */       case 'D':
/*     */       case 'd':
/*  50 */         return 13;
/*     */       case 'E':
/*     */       case 'e':
/*  53 */         return 14;
/*     */       case 'F':
/*     */       case 'f':
/*  56 */         return 15;
/*     */     } 
/*     */ 
/*     */     
/*  60 */     throw new IOException("Invalid hex char '" + c + "' provided!");
/*     */   }
/*     */ 
/*     */   
/*  64 */   protected int bufpos = -1;
/*     */   
/*     */   protected int bufsize;
/*     */   protected int available;
/*     */   protected int tokenBegin;
/*     */   protected int[] bufline;
/*     */   protected int[] bufcolumn;
/*  71 */   protected int column = 0;
/*  72 */   protected int line = 1;
/*     */   
/*     */   protected boolean prevCharIsCR = false;
/*     */   
/*     */   protected boolean prevCharIsLF = false;
/*     */   protected char[] buffer;
/*  78 */   protected int maxNextCharInd = 0;
/*  79 */   protected int inBuf = 0;
/*  80 */   private int tabSize = 1;
/*     */   protected char[] nextCharBuf;
/*  82 */   protected int nextCharInd = -1;
/*     */   
/*     */   private boolean trackLineColumn = true;
/*     */   
/*     */   public void setTabSize(int i) {
/*  87 */     this.tabSize = i;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTabSize() {
/*  92 */     return this.tabSize;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void expandBuff(boolean wrapAround) {
/*  97 */     char[] newbuffer = new char[this.bufsize + 2048];
/*  98 */     int[] newbufline = new int[this.bufsize + 2048];
/*  99 */     int[] newbufcolumn = new int[this.bufsize + 2048];
/*     */ 
/*     */     
/*     */     try {
/* 103 */       if (wrapAround)
/*     */       {
/* 105 */         System.arraycopy(this.buffer, this.tokenBegin, newbuffer, 0, this.bufsize - this.tokenBegin);
/* 106 */         System.arraycopy(this.buffer, 0, newbuffer, this.bufsize - this.tokenBegin, this.bufpos);
/* 107 */         this.buffer = newbuffer;
/*     */         
/* 109 */         System.arraycopy(this.bufline, this.tokenBegin, newbufline, 0, this.bufsize - this.tokenBegin);
/* 110 */         System.arraycopy(this.bufline, 0, newbufline, this.bufsize - this.tokenBegin, this.bufpos);
/* 111 */         this.bufline = newbufline;
/*     */         
/* 113 */         System.arraycopy(this.bufcolumn, this.tokenBegin, newbufcolumn, 0, this.bufsize - this.tokenBegin);
/* 114 */         System.arraycopy(this.bufcolumn, 0, newbufcolumn, this.bufsize - this.tokenBegin, this.bufpos);
/* 115 */         this.bufcolumn = newbufcolumn;
/*     */         
/* 117 */         this.bufpos += this.bufsize - this.tokenBegin;
/* 118 */         this.maxNextCharInd = this.bufpos;
/*     */       }
/*     */       else
/*     */       {
/* 122 */         System.arraycopy(this.buffer, this.tokenBegin, newbuffer, 0, this.bufsize - this.tokenBegin);
/* 123 */         this.buffer = newbuffer;
/*     */         
/* 125 */         System.arraycopy(this.bufline, this.tokenBegin, newbufline, 0, this.bufsize - this.tokenBegin);
/* 126 */         this.bufline = newbufline;
/*     */         
/* 128 */         System.arraycopy(this.bufcolumn, this.tokenBegin, newbufcolumn, 0, this.bufsize - this.tokenBegin);
/* 129 */         this.bufcolumn = newbufcolumn;
/*     */         
/* 131 */         this.bufpos -= this.tokenBegin;
/* 132 */         this.maxNextCharInd = this.bufpos;
/*     */       }
/*     */     
/* 135 */     } catch (Exception ex) {
/*     */       
/* 137 */       throw new IllegalStateException(ex);
/*     */     } 
/*     */     
/* 140 */     this.bufsize += 2048;
/* 141 */     this.available = this.bufsize;
/* 142 */     this.tokenBegin = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract int streamRead(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws IOException;
/*     */   
/*     */   protected abstract void streamClose() throws IOException;
/*     */   
/*     */   protected void fillBuff() throws IOException {
/* 151 */     if (this.maxNextCharInd == this.available)
/*     */     {
/* 153 */       if (this.available == this.bufsize) {
/*     */         
/* 155 */         if (this.tokenBegin > 2048) {
/*     */           
/* 157 */           this.bufpos = this.maxNextCharInd = 0;
/* 158 */           this.available = this.tokenBegin;
/*     */         
/*     */         }
/* 161 */         else if (this.tokenBegin < 0) {
/* 162 */           this.bufpos = this.maxNextCharInd = 0;
/*     */         } else {
/* 164 */           expandBuff(false);
/*     */         }
/*     */       
/* 167 */       } else if (this.available > this.tokenBegin) {
/* 168 */         this.available = this.bufsize;
/*     */       }
/* 170 */       else if (this.tokenBegin - this.available < 2048) {
/* 171 */         expandBuff(true);
/*     */       } else {
/* 173 */         this.available = this.tokenBegin;
/*     */       } 
/*     */     }
/*     */     try {
/* 177 */       int i = streamRead(this.buffer, this.maxNextCharInd, this.available - this.maxNextCharInd);
/* 178 */       if (i == -1) {
/*     */         
/* 180 */         streamClose();
/* 181 */         throw new IOException();
/*     */       } 
/* 183 */       this.maxNextCharInd += i;
/*     */       
/*     */       return;
/* 186 */     } catch (IOException e) {
/* 187 */       this.bufpos--;
/* 188 */       backup(0);
/* 189 */       if (this.tokenBegin == -1)
/* 190 */         this.tokenBegin = this.bufpos; 
/* 191 */       throw e;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public char beginToken() throws IOException {
/* 197 */     this.tokenBegin = -1;
/* 198 */     char c = readChar();
/* 199 */     this.tokenBegin = this.bufpos;
/*     */     
/* 201 */     return c;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateLineColumn(char c) {
/* 206 */     this.column++;
/*     */     
/* 208 */     if (this.prevCharIsLF) {
/*     */       
/* 210 */       this.prevCharIsLF = false;
/* 211 */       this.column = 1;
/* 212 */       this.line++;
/*     */     
/*     */     }
/* 215 */     else if (this.prevCharIsCR) {
/*     */       
/* 217 */       this.prevCharIsCR = false;
/* 218 */       if (c == '\n') {
/* 219 */         this.prevCharIsLF = true;
/*     */       } else {
/*     */         
/* 222 */         this.column = 1;
/* 223 */         this.line++;
/*     */       } 
/*     */     } 
/*     */     
/* 227 */     switch (c) {
/*     */       
/*     */       case '\r':
/* 230 */         this.prevCharIsCR = true;
/*     */         break;
/*     */       case '\n':
/* 233 */         this.prevCharIsLF = true;
/*     */         break;
/*     */       case '\t':
/* 236 */         this.column--;
/* 237 */         this.column += this.tabSize - this.column % this.tabSize;
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 243 */     this.bufline[this.bufpos] = this.line;
/* 244 */     this.bufcolumn[this.bufpos] = this.column;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public char readChar() throws IOException {
/* 250 */     if (this.inBuf > 0) {
/*     */       
/* 252 */       this.inBuf--;
/*     */       
/* 254 */       if (++this.bufpos == this.bufsize) {
/* 255 */         this.bufpos = 0;
/*     */       }
/* 257 */       return this.buffer[this.bufpos];
/*     */     } 
/*     */     
/* 260 */     this.bufpos++;
/* 261 */     if (this.bufpos >= this.maxNextCharInd) {
/* 262 */       fillBuff();
/*     */     }
/* 264 */     char c = this.buffer[this.bufpos];
/*     */     
/* 266 */     if (this.trackLineColumn)
/* 267 */       updateLineColumn(c); 
/* 268 */     return c;
/*     */   }
/*     */   
/*     */   public int getBeginColumn() {
/* 272 */     return this.bufcolumn[this.tokenBegin];
/*     */   }
/*     */   
/*     */   public int getBeginLine() {
/* 276 */     return this.bufline[this.tokenBegin];
/*     */   }
/*     */   
/*     */   public int getEndColumn() {
/* 280 */     return this.bufcolumn[this.bufpos];
/*     */   }
/*     */   
/*     */   public int getEndLine() {
/* 284 */     return this.bufline[this.bufpos];
/*     */   }
/*     */   
/*     */   public void backup(int amount) {
/* 288 */     this.inBuf += amount;
/* 289 */     this.bufpos -= amount;
/* 290 */     if (this.bufpos < 0) {
/* 291 */       this.bufpos += this.bufsize;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractCharStream(int startline, int startcolumn, int buffersize) {
/* 299 */     this.line = startline;
/* 300 */     this.column = startcolumn - 1;
/*     */     
/* 302 */     this.bufsize = buffersize;
/* 303 */     this.available = buffersize;
/* 304 */     this.buffer = new char[buffersize];
/* 305 */     this.bufline = new int[buffersize];
/* 306 */     this.bufcolumn = new int[buffersize];
/* 307 */     this.nextCharBuf = new char[4096];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reInit(int startline, int startcolumn, int buffersize) {
/* 315 */     this.line = startline;
/* 316 */     this.column = startcolumn - 1;
/* 317 */     if (this.buffer == null || buffersize != this.buffer.length) {
/*     */       
/* 319 */       this.bufsize = buffersize;
/* 320 */       this.available = buffersize;
/* 321 */       this.buffer = new char[buffersize];
/* 322 */       this.bufline = new int[buffersize];
/* 323 */       this.bufcolumn = new int[buffersize];
/* 324 */       this.nextCharBuf = new char[4096];
/*     */     } 
/* 326 */     this.prevCharIsCR = false;
/* 327 */     this.prevCharIsLF = false;
/* 328 */     this.maxNextCharInd = 0;
/* 329 */     this.inBuf = 0;
/* 330 */     this.tokenBegin = 0;
/* 331 */     this.bufpos = -1;
/* 332 */     this.nextCharInd = -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getImage() {
/* 337 */     if (this.bufpos >= this.tokenBegin)
/*     */     {
/* 339 */       return new String(this.buffer, this.tokenBegin, this.bufpos - this.tokenBegin + 1);
/*     */     }
/*     */ 
/*     */     
/* 343 */     return new String(this.buffer, this.tokenBegin, this.bufsize - this.tokenBegin) + new String(this.buffer, 0, this.bufpos + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public char[] getSuffix(int len) {
/* 349 */     char[] ret = new char[len];
/* 350 */     if (this.bufpos + 1 >= len) {
/* 351 */       System.arraycopy(this.buffer, this.bufpos - len + 1, ret, 0, len);
/*     */     } else {
/*     */       
/* 354 */       System.arraycopy(this.buffer, this.bufsize - len - this.bufpos - 1, ret, 0, len - this.bufpos - 1);
/* 355 */       System.arraycopy(this.buffer, 0, ret, len - this.bufpos - 1, this.bufpos + 1);
/*     */     } 
/* 357 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void done() {
/* 362 */     this.nextCharBuf = null;
/* 363 */     this.buffer = null;
/* 364 */     this.bufline = null;
/* 365 */     this.bufcolumn = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void adjustBeginLineColumn(int nNewLine, int newCol) {
/* 373 */     int len, start = this.tokenBegin;
/* 374 */     int newLine = nNewLine;
/*     */ 
/*     */     
/* 377 */     if (this.bufpos >= this.tokenBegin) {
/*     */       
/* 379 */       len = this.bufpos - this.tokenBegin + this.inBuf + 1;
/*     */     }
/*     */     else {
/*     */       
/* 383 */       len = this.bufsize - this.tokenBegin + this.bufpos + 1 + this.inBuf;
/*     */     } 
/*     */     
/* 386 */     int i = 0;
/* 387 */     int j = 0;
/* 388 */     int k = 0;
/* 389 */     int nextColDiff = 0;
/* 390 */     int columnDiff = 0;
/*     */ 
/*     */     
/* 393 */     while (i < len && this.bufline[j = start % this.bufsize] == this.bufline[k = ++start % this.bufsize]) {
/*     */       
/* 395 */       this.bufline[j] = newLine;
/* 396 */       nextColDiff = columnDiff + this.bufcolumn[k] - this.bufcolumn[j];
/* 397 */       this.bufcolumn[j] = newCol + columnDiff;
/* 398 */       columnDiff = nextColDiff;
/* 399 */       i++;
/*     */     } 
/*     */     
/* 402 */     if (i < len) {
/*     */       
/* 404 */       this.bufline[j] = newLine++;
/* 405 */       this.bufcolumn[j] = newCol + columnDiff;
/*     */       
/* 407 */       while (i++ < len) {
/*     */ 
/*     */         
/* 410 */         if (this.bufline[j = start % this.bufsize] != this.bufline[++start % this.bufsize]) {
/* 411 */           this.bufline[j] = newLine++; continue;
/*     */         } 
/* 413 */         this.bufline[j] = newLine;
/*     */       } 
/*     */     } 
/*     */     
/* 417 */     this.line = this.bufline[j];
/* 418 */     this.column = this.bufcolumn[j];
/*     */   }
/*     */   
/*     */   public void setTrackLineColumn(boolean tlc) {
/* 422 */     this.trackLineColumn = tlc;
/*     */   }
/*     */   
/*     */   public boolean isTrackLineColumn() {
/* 426 */     return this.trackLineColumn;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\kitfox\svg\animation\parser\AbstractCharStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */