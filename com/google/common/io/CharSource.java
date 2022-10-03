/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Streams;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.MustBeClosed;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Stream;
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
/*     */ public abstract class CharSource
/*     */ {
/*     */   @Beta
/*     */   public ByteSource asByteSource(Charset charset) {
/*  90 */     return new AsByteSource(charset);
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
/*     */   public BufferedReader openBufferedStream() throws IOException {
/* 112 */     Reader reader = openStream();
/* 113 */     return (reader instanceof BufferedReader) ? (BufferedReader)reader : new BufferedReader(reader);
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
/*     */   @MustBeClosed
/*     */   public Stream<String> lines() throws IOException {
/* 147 */     BufferedReader reader = openBufferedStream();
/* 148 */     return reader
/* 149 */       .lines()
/* 150 */       .onClose(() -> {
/*     */           
/*     */           try {
/*     */             reader.close();
/* 154 */           } catch (IOException e) {
/*     */             throw new UncheckedIOException(e);
/*     */           } 
/*     */         });
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
/*     */   @Beta
/*     */   public Optional<Long> lengthIfKnown() {
/* 176 */     return Optional.absent();
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
/*     */   @Beta
/*     */   public long length() throws IOException {
/* 200 */     Optional<Long> lengthIfKnown = lengthIfKnown();
/* 201 */     if (lengthIfKnown.isPresent()) {
/* 202 */       return ((Long)lengthIfKnown.get()).longValue();
/*     */     }
/*     */     
/* 205 */     Closer closer = Closer.create();
/*     */     try {
/* 207 */       Reader reader = closer.<Reader>register(openStream());
/* 208 */       return countBySkipping(reader);
/* 209 */     } catch (Throwable e) {
/* 210 */       throw closer.rethrow(e);
/*     */     } finally {
/* 212 */       closer.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   private long countBySkipping(Reader reader) throws IOException {
/* 217 */     long count = 0L;
/*     */     long read;
/* 219 */     while ((read = reader.skip(Long.MAX_VALUE)) != 0L) {
/* 220 */       count += read;
/*     */     }
/* 222 */     return count;
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
/*     */   @CanIgnoreReturnValue
/*     */   public long copyTo(Appendable appendable) throws IOException {
/* 235 */     Preconditions.checkNotNull(appendable);
/*     */     
/* 237 */     Closer closer = Closer.create();
/*     */     try {
/* 239 */       Reader reader = closer.<Reader>register(openStream());
/* 240 */       return CharStreams.copy(reader, appendable);
/* 241 */     } catch (Throwable e) {
/* 242 */       throw closer.rethrow(e);
/*     */     } finally {
/* 244 */       closer.close();
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
/*     */   @CanIgnoreReturnValue
/*     */   public long copyTo(CharSink sink) throws IOException {
/* 257 */     Preconditions.checkNotNull(sink);
/*     */     
/* 259 */     Closer closer = Closer.create();
/*     */     try {
/* 261 */       Reader reader = closer.<Reader>register(openStream());
/* 262 */       Writer writer = closer.<Writer>register(sink.openStream());
/* 263 */       return CharStreams.copy(reader, writer);
/* 264 */     } catch (Throwable e) {
/* 265 */       throw closer.rethrow(e);
/*     */     } finally {
/* 267 */       closer.close();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String read() throws IOException {
/* 277 */     Closer closer = Closer.create();
/*     */     try {
/* 279 */       Reader reader = closer.<Reader>register(openStream());
/* 280 */       return CharStreams.toString(reader);
/* 281 */     } catch (Throwable e) {
/* 282 */       throw closer.rethrow(e);
/*     */     } finally {
/* 284 */       closer.close();
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
/*     */   public String readFirstLine() throws IOException {
/* 299 */     Closer closer = Closer.create();
/*     */     try {
/* 301 */       BufferedReader reader = closer.<BufferedReader>register(openBufferedStream());
/* 302 */       return reader.readLine();
/* 303 */     } catch (Throwable e) {
/* 304 */       throw closer.rethrow(e);
/*     */     } finally {
/* 306 */       closer.close();
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
/*     */   
/*     */   public ImmutableList<String> readLines() throws IOException {
/* 322 */     Closer closer = Closer.create();
/*     */     try {
/* 324 */       BufferedReader reader = closer.<BufferedReader>register(openBufferedStream());
/* 325 */       List<String> result = Lists.newArrayList();
/*     */       String line;
/* 327 */       while ((line = reader.readLine()) != null) {
/* 328 */         result.add(line);
/*     */       }
/* 330 */       return ImmutableList.copyOf(result);
/* 331 */     } catch (Throwable e) {
/* 332 */       throw closer.rethrow(e);
/*     */     } finally {
/* 334 */       closer.close();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   public <T> T readLines(LineProcessor<T> processor) throws IOException {
/* 355 */     Preconditions.checkNotNull(processor);
/*     */     
/* 357 */     Closer closer = Closer.create();
/*     */     try {
/* 359 */       Reader reader = closer.<Reader>register(openStream());
/* 360 */       return (T)CharStreams.readLines(reader, (LineProcessor)processor);
/* 361 */     } catch (Throwable e) {
/* 362 */       throw closer.rethrow(e);
/*     */     } finally {
/* 364 */       closer.close();
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public void forEachLine(Consumer<? super String> action) throws IOException {
/* 383 */     try (Stream<String> lines = lines()) {
/*     */       
/* 385 */       lines.forEachOrdered(action);
/* 386 */     } catch (UncheckedIOException e) {
/* 387 */       throw e.getCause();
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
/*     */ 
/*     */   
/*     */   public boolean isEmpty() throws IOException {
/* 404 */     Optional<Long> lengthIfKnown = lengthIfKnown();
/* 405 */     if (lengthIfKnown.isPresent()) {
/* 406 */       return (((Long)lengthIfKnown.get()).longValue() == 0L);
/*     */     }
/* 408 */     Closer closer = Closer.create();
/*     */     try {
/* 410 */       Reader reader = closer.<Reader>register(openStream());
/* 411 */       return (reader.read() == -1);
/* 412 */     } catch (Throwable e) {
/* 413 */       throw closer.rethrow(e);
/*     */     } finally {
/* 415 */       closer.close();
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
/*     */   
/*     */   public static CharSource concat(Iterable<? extends CharSource> sources) {
/* 431 */     return new ConcatenatedCharSource(sources);
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
/*     */   public static CharSource concat(Iterator<? extends CharSource> sources) {
/* 453 */     return concat((Iterable<? extends CharSource>)ImmutableList.copyOf(sources));
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
/*     */   public static CharSource concat(CharSource... sources) {
/* 469 */     return concat((Iterable<? extends CharSource>)ImmutableList.copyOf((Object[])sources));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSource wrap(CharSequence charSequence) {
/* 480 */     return (charSequence instanceof String) ? new StringCharSource((String)charSequence) : new CharSequenceCharSource(charSequence);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSource empty() {
/* 491 */     return EmptyCharSource.INSTANCE;
/*     */   }
/*     */   
/*     */   public abstract Reader openStream() throws IOException;
/*     */   
/*     */   private final class AsByteSource extends ByteSource {
/*     */     final Charset charset;
/*     */     
/*     */     AsByteSource(Charset charset) {
/* 500 */       this.charset = (Charset)Preconditions.checkNotNull(charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public CharSource asCharSource(Charset charset) {
/* 505 */       if (charset.equals(this.charset)) {
/* 506 */         return CharSource.this;
/*     */       }
/* 508 */       return super.asCharSource(charset);
/*     */     }
/*     */ 
/*     */     
/*     */     public InputStream openStream() throws IOException {
/* 513 */       return new ReaderInputStream(CharSource.this.openStream(), this.charset, 8192);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 518 */       return CharSource.this.toString() + ".asByteSource(" + this.charset + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CharSequenceCharSource
/*     */     extends CharSource {
/* 524 */     private static final Splitter LINE_SPLITTER = Splitter.onPattern("\r\n|\n|\r");
/*     */     
/*     */     protected final CharSequence seq;
/*     */     
/*     */     protected CharSequenceCharSource(CharSequence seq) {
/* 529 */       this.seq = (CharSequence)Preconditions.checkNotNull(seq);
/*     */     }
/*     */ 
/*     */     
/*     */     public Reader openStream() {
/* 534 */       return new CharSequenceReader(this.seq);
/*     */     }
/*     */ 
/*     */     
/*     */     public String read() {
/* 539 */       return this.seq.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 544 */       return (this.seq.length() == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public long length() {
/* 549 */       return this.seq.length();
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Long> lengthIfKnown() {
/* 554 */       return Optional.of(Long.valueOf(this.seq.length()));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Iterator<String> linesIterator() {
/* 562 */       return (Iterator<String>)new AbstractIterator<String>() {
/* 563 */           Iterator<String> lines = CharSource.CharSequenceCharSource.LINE_SPLITTER.split(CharSource.CharSequenceCharSource.this.seq).iterator();
/*     */ 
/*     */           
/*     */           protected String computeNext() {
/* 567 */             if (this.lines.hasNext()) {
/* 568 */               String next = this.lines.next();
/*     */               
/* 570 */               if (this.lines.hasNext() || !next.isEmpty()) {
/* 571 */                 return next;
/*     */               }
/*     */             } 
/* 574 */             return (String)endOfData();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public Stream<String> lines() {
/* 581 */       return Streams.stream(linesIterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public String readFirstLine() {
/* 586 */       Iterator<String> lines = linesIterator();
/* 587 */       return lines.hasNext() ? lines.next() : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableList<String> readLines() {
/* 592 */       return ImmutableList.copyOf(linesIterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T readLines(LineProcessor<T> processor) throws IOException {
/* 597 */       Iterator<String> lines = linesIterator(); do {  }
/* 598 */       while (lines.hasNext() && 
/* 599 */         processor.processLine(lines.next()));
/*     */ 
/*     */ 
/*     */       
/* 603 */       return processor.getResult();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 608 */       return "CharSource.wrap(" + Ascii.truncate(this.seq, 30, "...") + ")";
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
/*     */ 
/*     */ 
/*     */   
/*     */   private static class StringCharSource
/*     */     extends CharSequenceCharSource
/*     */   {
/*     */     protected StringCharSource(String seq) {
/* 629 */       super(seq);
/*     */     }
/*     */ 
/*     */     
/*     */     public Reader openStream() {
/* 634 */       return new StringReader((String)this.seq);
/*     */     }
/*     */ 
/*     */     
/*     */     public long copyTo(Appendable appendable) throws IOException {
/* 639 */       appendable.append(this.seq);
/* 640 */       return this.seq.length();
/*     */     }
/*     */ 
/*     */     
/*     */     public long copyTo(CharSink sink) throws IOException {
/* 645 */       Preconditions.checkNotNull(sink);
/* 646 */       Closer closer = Closer.create();
/*     */       try {
/* 648 */         Writer writer = closer.<Writer>register(sink.openStream());
/* 649 */         writer.write((String)this.seq);
/* 650 */         return this.seq.length();
/* 651 */       } catch (Throwable e) {
/* 652 */         throw closer.rethrow(e);
/*     */       } finally {
/* 654 */         closer.close();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class EmptyCharSource
/*     */     extends StringCharSource {
/* 661 */     private static final EmptyCharSource INSTANCE = new EmptyCharSource();
/*     */     
/*     */     private EmptyCharSource() {
/* 664 */       super("");
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 669 */       return "CharSource.empty()";
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ConcatenatedCharSource
/*     */     extends CharSource {
/*     */     private final Iterable<? extends CharSource> sources;
/*     */     
/*     */     ConcatenatedCharSource(Iterable<? extends CharSource> sources) {
/* 678 */       this.sources = (Iterable<? extends CharSource>)Preconditions.checkNotNull(sources);
/*     */     }
/*     */ 
/*     */     
/*     */     public Reader openStream() throws IOException {
/* 683 */       return new MultiReader(this.sources.iterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() throws IOException {
/* 688 */       for (CharSource source : this.sources) {
/* 689 */         if (!source.isEmpty()) {
/* 690 */           return false;
/*     */         }
/*     */       } 
/* 693 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Long> lengthIfKnown() {
/* 698 */       long result = 0L;
/* 699 */       for (CharSource source : this.sources) {
/* 700 */         Optional<Long> lengthIfKnown = source.lengthIfKnown();
/* 701 */         if (!lengthIfKnown.isPresent()) {
/* 702 */           return Optional.absent();
/*     */         }
/* 704 */         result += ((Long)lengthIfKnown.get()).longValue();
/*     */       } 
/* 706 */       return Optional.of(Long.valueOf(result));
/*     */     }
/*     */ 
/*     */     
/*     */     public long length() throws IOException {
/* 711 */       long result = 0L;
/* 712 */       for (CharSource source : this.sources) {
/* 713 */         result += source.length();
/*     */       }
/* 715 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 720 */       return "CharSource.concat(" + this.sources + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\google\common\io\CharSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */