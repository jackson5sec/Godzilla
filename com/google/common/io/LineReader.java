/*    */ package com.google.common.io;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.io.IOException;
/*    */ import java.io.Reader;
/*    */ import java.nio.CharBuffer;
/*    */ import java.util.LinkedList;
/*    */ import java.util.Queue;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Beta
/*    */ @GwtIncompatible
/*    */ public final class LineReader
/*    */ {
/*    */   private final Readable readable;
/*    */   private final Reader reader;
/* 43 */   private final CharBuffer cbuf = CharStreams.createBuffer();
/* 44 */   private final char[] buf = this.cbuf.array();
/*    */   
/* 46 */   private final Queue<String> lines = new LinkedList<>();
/* 47 */   private final LineBuffer lineBuf = new LineBuffer()
/*    */     {
/*    */       protected void handleLine(String line, String end)
/*    */       {
/* 51 */         LineReader.this.lines.add(line);
/*    */       }
/*    */     };
/*    */ 
/*    */   
/*    */   public LineReader(Readable readable) {
/* 57 */     this.readable = (Readable)Preconditions.checkNotNull(readable);
/* 58 */     this.reader = (readable instanceof Reader) ? (Reader)readable : null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public String readLine() throws IOException {
/* 72 */     while (this.lines.peek() == null) {
/* 73 */       this.cbuf.clear();
/*    */ 
/*    */       
/* 76 */       int read = (this.reader != null) ? this.reader.read(this.buf, 0, this.buf.length) : this.readable.read(this.cbuf);
/* 77 */       if (read == -1) {
/* 78 */         this.lineBuf.finish();
/*    */         break;
/*    */       } 
/* 81 */       this.lineBuf.add(this.buf, 0, read);
/*    */     } 
/* 83 */     return this.lines.poll();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\io\LineReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */