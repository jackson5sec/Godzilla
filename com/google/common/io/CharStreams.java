/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ @GwtIncompatible
/*     */ public final class CharStreams
/*     */ {
/*     */   private static final int DEFAULT_BUF_SIZE = 2048;
/*     */   
/*     */   static CharBuffer createBuffer() {
/*  54 */     return CharBuffer.allocate(2048);
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
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public static long copy(Readable from, Appendable to) throws IOException {
/*  72 */     if (from instanceof Reader) {
/*     */       
/*  74 */       if (to instanceof StringBuilder) {
/*  75 */         return copyReaderToBuilder((Reader)from, (StringBuilder)to);
/*     */       }
/*  77 */       return copyReaderToWriter((Reader)from, asWriter(to));
/*     */     } 
/*     */     
/*  80 */     Preconditions.checkNotNull(from);
/*  81 */     Preconditions.checkNotNull(to);
/*  82 */     long total = 0L;
/*  83 */     CharBuffer buf = createBuffer();
/*  84 */     while (from.read(buf) != -1) {
/*  85 */       buf.flip();
/*  86 */       to.append(buf);
/*  87 */       total += buf.remaining();
/*  88 */       buf.clear();
/*     */     } 
/*  90 */     return total;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static long copyReaderToBuilder(Reader from, StringBuilder to) throws IOException {
/* 114 */     Preconditions.checkNotNull(from);
/* 115 */     Preconditions.checkNotNull(to);
/* 116 */     char[] buf = new char[2048];
/*     */     
/* 118 */     long total = 0L; int nRead;
/* 119 */     while ((nRead = from.read(buf)) != -1) {
/* 120 */       to.append(buf, 0, nRead);
/* 121 */       total += nRead;
/*     */     } 
/* 123 */     return total;
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
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static long copyReaderToWriter(Reader from, Writer to) throws IOException {
/* 142 */     Preconditions.checkNotNull(from);
/* 143 */     Preconditions.checkNotNull(to);
/* 144 */     char[] buf = new char[2048];
/*     */     
/* 146 */     long total = 0L; int nRead;
/* 147 */     while ((nRead = from.read(buf)) != -1) {
/* 148 */       to.write(buf, 0, nRead);
/* 149 */       total += nRead;
/*     */     } 
/* 151 */     return total;
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
/*     */   public static String toString(Readable r) throws IOException {
/* 163 */     return toStringBuilder(r).toString();
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
/*     */   private static StringBuilder toStringBuilder(Readable r) throws IOException {
/* 175 */     StringBuilder sb = new StringBuilder();
/* 176 */     if (r instanceof Reader) {
/* 177 */       copyReaderToBuilder((Reader)r, sb);
/*     */     } else {
/* 179 */       copy(r, sb);
/*     */     } 
/* 181 */     return sb;
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
/*     */   @Beta
/*     */   public static List<String> readLines(Readable r) throws IOException {
/* 197 */     List<String> result = new ArrayList<>();
/* 198 */     LineReader lineReader = new LineReader(r);
/*     */     String line;
/* 200 */     while ((line = lineReader.readLine()) != null) {
/* 201 */       result.add(line);
/*     */     }
/* 203 */     return result;
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
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T readLines(Readable readable, LineProcessor<T> processor) throws IOException {
/* 218 */     Preconditions.checkNotNull(readable);
/* 219 */     Preconditions.checkNotNull(processor);
/*     */     
/* 221 */     LineReader lineReader = new LineReader(readable); String line; do {
/*     */     
/* 223 */     } while ((line = lineReader.readLine()) != null && 
/* 224 */       processor.processLine(line));
/*     */ 
/*     */ 
/*     */     
/* 228 */     return processor.getResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   public static long exhaust(Readable readable) throws IOException {
/* 240 */     long total = 0L;
/*     */     
/* 242 */     CharBuffer buf = createBuffer(); long read;
/* 243 */     while ((read = readable.read(buf)) != -1L) {
/* 244 */       total += read;
/* 245 */       buf.clear();
/*     */     } 
/* 247 */     return total;
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
/*     */   @Beta
/*     */   public static void skipFully(Reader reader, long n) throws IOException {
/* 261 */     Preconditions.checkNotNull(reader);
/* 262 */     while (n > 0L) {
/* 263 */       long amt = reader.skip(n);
/* 264 */       if (amt == 0L) {
/* 265 */         throw new EOFException();
/*     */       }
/* 267 */       n -= amt;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static Writer nullWriter() {
/* 278 */     return NullWriter.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class NullWriter
/*     */     extends Writer {
/* 283 */     private static final NullWriter INSTANCE = new NullWriter();
/*     */ 
/*     */     
/*     */     public void write(int c) {}
/*     */ 
/*     */     
/*     */     public void write(char[] cbuf) {
/* 290 */       Preconditions.checkNotNull(cbuf);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(char[] cbuf, int off, int len) {
/* 295 */       Preconditions.checkPositionIndexes(off, off + len, cbuf.length);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(String str) {
/* 300 */       Preconditions.checkNotNull(str);
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(String str, int off, int len) {
/* 305 */       Preconditions.checkPositionIndexes(off, off + len, str.length());
/*     */     }
/*     */ 
/*     */     
/*     */     public Writer append(CharSequence csq) {
/* 310 */       Preconditions.checkNotNull(csq);
/* 311 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Writer append(CharSequence csq, int start, int end) {
/* 316 */       Preconditions.checkPositionIndexes(start, end, csq.length());
/* 317 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Writer append(char c) {
/* 322 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public void flush() {}
/*     */ 
/*     */     
/*     */     public void close() {}
/*     */ 
/*     */     
/*     */     public String toString() {
/* 333 */       return "CharStreams.nullWriter()";
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
/*     */   @Beta
/*     */   public static Writer asWriter(Appendable target) {
/* 347 */     if (target instanceof Writer) {
/* 348 */       return (Writer)target;
/*     */     }
/* 350 */     return new AppendableWriter(target);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\io\CharStreams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */