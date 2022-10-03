/*     */ package org.mozilla.javascript.tools.idswitch;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FileBody
/*     */ {
/*     */   private static class ReplaceItem
/*     */   {
/*     */     ReplaceItem next;
/*     */     int begin;
/*     */     int end;
/*     */     String replacement;
/*     */     
/*     */     ReplaceItem(int begin, int end, String text) {
/*  21 */       this.begin = begin;
/*  22 */       this.end = end;
/*  23 */       this.replacement = text;
/*     */     }
/*     */   }
/*     */   
/*  27 */   private char[] buffer = new char[16384];
/*     */   
/*     */   private int bufferEnd;
/*     */   
/*     */   private int lineBegin;
/*     */   private int lineEnd;
/*     */   private int nextLineStart;
/*     */   private int lineNumber;
/*     */   ReplaceItem firstReplace;
/*     */   ReplaceItem lastReplace;
/*     */   
/*     */   public char[] getBuffer() {
/*  39 */     return this.buffer;
/*     */   }
/*     */   public void readData(Reader r) throws IOException {
/*  42 */     int capacity = this.buffer.length;
/*  43 */     int offset = 0;
/*     */     while (true) {
/*  45 */       int n_read = r.read(this.buffer, offset, capacity - offset);
/*  46 */       if (n_read < 0)
/*  47 */         break;  offset += n_read;
/*  48 */       if (capacity == offset) {
/*  49 */         capacity *= 2;
/*  50 */         char[] tmp = new char[capacity];
/*  51 */         System.arraycopy(this.buffer, 0, tmp, 0, offset);
/*  52 */         this.buffer = tmp;
/*     */       } 
/*     */     } 
/*  55 */     this.bufferEnd = offset;
/*     */   }
/*     */   
/*     */   public void writeInitialData(Writer w) throws IOException {
/*  59 */     w.write(this.buffer, 0, this.bufferEnd);
/*     */   }
/*     */   
/*     */   public void writeData(Writer w) throws IOException {
/*  63 */     int offset = 0;
/*  64 */     for (ReplaceItem x = this.firstReplace; x != null; x = x.next) {
/*  65 */       int before_replace = x.begin - offset;
/*  66 */       if (before_replace > 0) {
/*  67 */         w.write(this.buffer, offset, before_replace);
/*     */       }
/*  69 */       w.write(x.replacement);
/*  70 */       offset = x.end;
/*     */     } 
/*  72 */     int tail = this.bufferEnd - offset;
/*  73 */     if (tail != 0)
/*  74 */       w.write(this.buffer, offset, tail); 
/*     */   }
/*     */   
/*     */   public boolean wasModified() {
/*  78 */     return (this.firstReplace != null);
/*     */   }
/*     */   public boolean setReplacement(int begin, int end, String text) {
/*  81 */     if (equals(text, this.buffer, begin, end)) return false;
/*     */     
/*  83 */     ReplaceItem item = new ReplaceItem(begin, end, text);
/*  84 */     if (this.firstReplace == null) {
/*  85 */       this.firstReplace = this.lastReplace = item;
/*     */     }
/*  87 */     else if (begin < this.firstReplace.begin) {
/*  88 */       item.next = this.firstReplace;
/*  89 */       this.firstReplace = item;
/*     */     } else {
/*     */       
/*  92 */       ReplaceItem cursor = this.firstReplace;
/*  93 */       ReplaceItem next = cursor.next;
/*  94 */       while (next != null) {
/*  95 */         if (begin < next.begin) {
/*  96 */           item.next = next;
/*  97 */           cursor.next = item;
/*     */           break;
/*     */         } 
/* 100 */         cursor = next;
/* 101 */         next = next.next;
/*     */       } 
/* 103 */       if (next == null) {
/* 104 */         this.lastReplace.next = item;
/*     */       }
/*     */     } 
/*     */     
/* 108 */     return true;
/*     */   }
/*     */   public int getLineNumber() {
/* 111 */     return this.lineNumber;
/*     */   } public int getLineBegin() {
/* 113 */     return this.lineBegin;
/*     */   } public int getLineEnd() {
/* 115 */     return this.lineEnd;
/*     */   }
/*     */   public void startLineLoop() {
/* 118 */     this.lineNumber = 0;
/* 119 */     this.lineBegin = this.lineEnd = this.nextLineStart = 0;
/*     */   }
/*     */   
/*     */   public boolean nextLine() {
/* 123 */     if (this.nextLineStart == this.bufferEnd) {
/* 124 */       this.lineNumber = 0; return false;
/*     */     } 
/* 126 */     int c = 0; int i;
/* 127 */     for (i = this.nextLineStart; i != this.bufferEnd; i++) {
/* 128 */       c = this.buffer[i];
/* 129 */       if (c == 10 || c == 13)
/*     */         break; 
/* 131 */     }  this.lineBegin = this.nextLineStart;
/* 132 */     this.lineEnd = i;
/* 133 */     if (i == this.bufferEnd) {
/* 134 */       this.nextLineStart = i;
/*     */     }
/* 136 */     else if (c == 13 && i + 1 != this.bufferEnd && this.buffer[i + 1] == '\n') {
/* 137 */       this.nextLineStart = i + 2;
/*     */     } else {
/*     */       
/* 140 */       this.nextLineStart = i + 1;
/*     */     } 
/* 142 */     this.lineNumber++;
/* 143 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean equals(String str, char[] array, int begin, int end) {
/* 148 */     if (str.length() == end - begin) {
/* 149 */       for (int i = begin, j = 0; i != end; i++, j++) {
/* 150 */         if (array[i] != str.charAt(j)) return false; 
/*     */       } 
/* 152 */       return true;
/*     */     } 
/* 154 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\mozilla\javascript\tools\idswitch\FileBody.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */