/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
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
/*     */ @GwtIncompatible
/*     */ abstract class LineBuffer
/*     */ {
/*  35 */   private StringBuilder line = new StringBuilder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean sawReturn;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void add(char[] cbuf, int off, int len) throws IOException {
/*  50 */     int pos = off;
/*  51 */     if (this.sawReturn && len > 0)
/*     */     {
/*  53 */       if (finishLine((cbuf[pos] == '\n'))) {
/*  54 */         pos++;
/*     */       }
/*     */     }
/*     */     
/*  58 */     int start = pos;
/*  59 */     for (int end = off + len; pos < end; pos++) {
/*  60 */       switch (cbuf[pos]) {
/*     */         case '\r':
/*  62 */           this.line.append(cbuf, start, pos - start);
/*  63 */           this.sawReturn = true;
/*  64 */           if (pos + 1 < end && 
/*  65 */             finishLine((cbuf[pos + 1] == '\n'))) {
/*  66 */             pos++;
/*     */           }
/*     */           
/*  69 */           start = pos + 1;
/*     */           break;
/*     */         
/*     */         case '\n':
/*  73 */           this.line.append(cbuf, start, pos - start);
/*  74 */           finishLine(true);
/*  75 */           start = pos + 1;
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */     
/*     */     } 
/*  82 */     this.line.append(cbuf, start, off + len - start);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private boolean finishLine(boolean sawNewline) throws IOException {
/*  88 */     String separator = this.sawReturn ? (sawNewline ? "\r\n" : "\r") : (sawNewline ? "\n" : "");
/*  89 */     handleLine(this.line.toString(), separator);
/*  90 */     this.line = new StringBuilder();
/*  91 */     this.sawReturn = false;
/*  92 */     return sawNewline;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void finish() throws IOException {
/* 102 */     if (this.sawReturn || this.line.length() > 0)
/* 103 */       finishLine(false); 
/*     */   }
/*     */   
/*     */   protected abstract void handleLine(String paramString1, String paramString2) throws IOException;
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\io\LineBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */