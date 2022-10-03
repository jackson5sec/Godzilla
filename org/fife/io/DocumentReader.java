/*     */ package org.fife.io;
/*     */ 
/*     */ import java.io.Reader;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Segment;
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
/*     */ public class DocumentReader
/*     */   extends Reader
/*     */ {
/*     */   private long position;
/*     */   private long mark;
/*     */   private Document document;
/*     */   private Segment segment;
/*     */   
/*     */   public DocumentReader(Document document) {
/*  54 */     this.position = 0L;
/*  55 */     this.mark = -1L;
/*  56 */     this.document = document;
/*  57 */     this.segment = new Segment();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mark(int readAheadLimit) {
/*  77 */     this.mark = this.position;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean markSupported() {
/*  87 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int read() {
/*  96 */     if (this.position >= this.document.getLength()) {
/*  97 */       return -1;
/*     */     }
/*     */     try {
/* 100 */       this.document.getText((int)this.position, 1, this.segment);
/* 101 */       this.position++;
/* 102 */       return this.segment.array[this.segment.offset];
/* 103 */     } catch (BadLocationException ble) {
/*     */       
/* 105 */       ble.printStackTrace();
/* 106 */       return -1;
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
/*     */   public int read(char[] array) {
/* 120 */     return read(array, 0, array.length);
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
/*     */   public int read(char[] cbuf, int off, int len) {
/* 136 */     if (this.position >= this.document.getLength()) {
/* 137 */       return -1;
/*     */     }
/* 139 */     int k = len;
/* 140 */     if (this.position + k >= this.document.getLength()) {
/* 141 */       k = this.document.getLength() - (int)this.position;
/*     */     }
/* 143 */     if (off + k >= cbuf.length) {
/* 144 */       k = cbuf.length - off;
/*     */     }
/*     */     try {
/* 147 */       this.document.getText((int)this.position, k, this.segment);
/* 148 */       this.position += k;
/* 149 */       System.arraycopy(this.segment.array, this.segment.offset, cbuf, off, k);
/*     */ 
/*     */       
/* 152 */       return k;
/* 153 */     } catch (BadLocationException ble) {
/*     */       
/* 155 */       return -1;
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
/*     */   public boolean ready() {
/* 170 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 181 */     if (this.mark == -1L) {
/* 182 */       this.position = 0L;
/*     */     } else {
/*     */       
/* 185 */       this.position = this.mark;
/* 186 */       this.mark = -1L;
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
/*     */   public long skip(long n) {
/* 199 */     if (this.position + n <= this.document.getLength()) {
/* 200 */       this.position += n;
/* 201 */       return n;
/*     */     } 
/* 203 */     long temp = this.position;
/* 204 */     this.position = this.document.getLength();
/* 205 */     return this.document.getLength() - temp;
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
/*     */   public void seek(long pos) {
/* 217 */     this.position = Math.min(pos, this.document.getLength());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\fife\io\DocumentReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */