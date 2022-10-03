/*      */ package org.springframework.core.io.buffer;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.channels.AsynchronousFileChannel;
/*      */ import java.nio.channels.Channel;
/*      */ import java.nio.channels.Channels;
/*      */ import java.nio.channels.CompletionHandler;
/*      */ import java.nio.channels.ReadableByteChannel;
/*      */ import java.nio.channels.WritableByteChannel;
/*      */ import java.nio.file.OpenOption;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.StandardOpenOption;
/*      */ import java.nio.file.attribute.FileAttribute;
/*      */ import java.util.HashSet;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ import java.util.concurrent.atomic.AtomicReference;
/*      */ import java.util.function.Consumer;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.reactivestreams.Publisher;
/*      */ import org.reactivestreams.Subscription;
/*      */ import org.springframework.core.io.Resource;
/*      */ import org.springframework.lang.Nullable;
/*      */ import org.springframework.util.Assert;
/*      */ import reactor.core.CoreSubscriber;
/*      */ import reactor.core.Disposable;
/*      */ import reactor.core.publisher.BaseSubscriber;
/*      */ import reactor.core.publisher.Flux;
/*      */ import reactor.core.publisher.FluxSink;
/*      */ import reactor.core.publisher.Mono;
/*      */ import reactor.core.publisher.MonoSink;
/*      */ import reactor.core.publisher.SynchronousSink;
/*      */ import reactor.util.context.Context;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class DataBufferUtils
/*      */ {
/*   65 */   private static final Log logger = LogFactory.getLog(DataBufferUtils.class);
/*      */   
/*   67 */   private static final Consumer<DataBuffer> RELEASE_CONSUMER = DataBufferUtils::release;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Flux<DataBuffer> readInputStream(Callable<InputStream> inputStreamSupplier, DataBufferFactory bufferFactory, int bufferSize) {
/*   86 */     Assert.notNull(inputStreamSupplier, "'inputStreamSupplier' must not be null");
/*   87 */     return readByteChannel(() -> Channels.newChannel(inputStreamSupplier.call()), bufferFactory, bufferSize);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Flux<DataBuffer> readByteChannel(Callable<ReadableByteChannel> channelSupplier, DataBufferFactory bufferFactory, int bufferSize) {
/*  102 */     Assert.notNull(channelSupplier, "'channelSupplier' must not be null");
/*  103 */     Assert.notNull(bufferFactory, "'dataBufferFactory' must not be null");
/*  104 */     Assert.isTrue((bufferSize > 0), "'bufferSize' must be > 0");
/*      */     
/*  106 */     return Flux.using(channelSupplier, channel -> Flux.generate(new ReadableByteChannelGenerator(channel, bufferFactory, bufferSize)), DataBufferUtils::closeChannel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Flux<DataBuffer> readAsynchronousFileChannel(Callable<AsynchronousFileChannel> channelSupplier, DataBufferFactory bufferFactory, int bufferSize) {
/*  125 */     return readAsynchronousFileChannel(channelSupplier, 0L, bufferFactory, bufferSize);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Flux<DataBuffer> readAsynchronousFileChannel(Callable<AsynchronousFileChannel> channelSupplier, long position, DataBufferFactory bufferFactory, int bufferSize) {
/*  142 */     Assert.notNull(channelSupplier, "'channelSupplier' must not be null");
/*  143 */     Assert.notNull(bufferFactory, "'dataBufferFactory' must not be null");
/*  144 */     Assert.isTrue((position >= 0L), "'position' must be >= 0");
/*  145 */     Assert.isTrue((bufferSize > 0), "'bufferSize' must be > 0");
/*      */     
/*  147 */     Flux<DataBuffer> flux = Flux.using(channelSupplier, channel -> Flux.create(()), channel -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  159 */     return flux.doOnDiscard(PooledDataBuffer.class, DataBufferUtils::release);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Flux<DataBuffer> read(Path path, DataBufferFactory bufferFactory, int bufferSize, OpenOption... options) {
/*  174 */     Assert.notNull(path, "Path must not be null");
/*  175 */     Assert.notNull(bufferFactory, "BufferFactory must not be null");
/*  176 */     Assert.isTrue((bufferSize > 0), "'bufferSize' must be > 0");
/*  177 */     if (options.length > 0) {
/*  178 */       for (OpenOption option : options) {
/*  179 */         Assert.isTrue((option != StandardOpenOption.APPEND && option != StandardOpenOption.WRITE), "'" + option + "' not allowed");
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*  184 */     return readAsynchronousFileChannel(() -> AsynchronousFileChannel.open(path, options), bufferFactory, bufferSize);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Flux<DataBuffer> read(Resource resource, DataBufferFactory bufferFactory, int bufferSize) {
/*  201 */     return read(resource, 0L, bufferFactory, bufferSize);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Flux<DataBuffer> read(Resource resource, long position, DataBufferFactory bufferFactory, int bufferSize) {
/*      */     try {
/*  222 */       if (resource.isFile()) {
/*  223 */         File file = resource.getFile();
/*  224 */         return readAsynchronousFileChannel(() -> AsynchronousFileChannel.open(file.toPath(), new OpenOption[] { StandardOpenOption.READ }), position, bufferFactory, bufferSize);
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  229 */     catch (IOException iOException) {}
/*      */ 
/*      */     
/*  232 */     Flux<DataBuffer> result = readByteChannel(resource::readableChannel, bufferFactory, bufferSize);
/*  233 */     return (position == 0L) ? result : skipUntilByteCount((Publisher<? extends DataBuffer>)result, position);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Flux<DataBuffer> write(Publisher<DataBuffer> source, OutputStream outputStream) {
/*  257 */     Assert.notNull(source, "'source' must not be null");
/*  258 */     Assert.notNull(outputStream, "'outputStream' must not be null");
/*      */     
/*  260 */     WritableByteChannel channel = Channels.newChannel(outputStream);
/*  261 */     return write(source, channel);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Flux<DataBuffer> write(Publisher<DataBuffer> source, WritableByteChannel channel) {
/*  280 */     Assert.notNull(source, "'source' must not be null");
/*  281 */     Assert.notNull(channel, "'channel' must not be null");
/*      */     
/*  283 */     Flux<DataBuffer> flux = Flux.from(source);
/*  284 */     return Flux.create(sink -> {
/*      */           WritableByteChannelSubscriber subscriber = new WritableByteChannelSubscriber(sink, channel);
/*      */           sink.onDispose((Disposable)subscriber);
/*      */           flux.subscribe((CoreSubscriber)subscriber);
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Flux<DataBuffer> write(Publisher<DataBuffer> source, AsynchronousFileChannel channel) {
/*  308 */     return write(source, channel, 0L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Flux<DataBuffer> write(Publisher<? extends DataBuffer> source, AsynchronousFileChannel channel, long position) {
/*  330 */     Assert.notNull(source, "'source' must not be null");
/*  331 */     Assert.notNull(channel, "'channel' must not be null");
/*  332 */     Assert.isTrue((position >= 0L), "'position' must be >= 0");
/*      */     
/*  334 */     Flux<DataBuffer> flux = Flux.from(source);
/*  335 */     return Flux.create(sink -> {
/*      */           WriteCompletionHandler handler = new WriteCompletionHandler(sink, channel, position);
/*      */           sink.onDispose((Disposable)handler);
/*      */           flux.subscribe((CoreSubscriber)handler);
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Mono<Void> write(Publisher<DataBuffer> source, Path destination, OpenOption... options) {
/*  358 */     Assert.notNull(source, "Source must not be null");
/*  359 */     Assert.notNull(destination, "Destination must not be null");
/*      */     
/*  361 */     Set<OpenOption> optionSet = checkWriteOptions(options);
/*      */     
/*  363 */     return Mono.create(sink -> {
/*      */           try {
/*      */             AsynchronousFileChannel channel = AsynchronousFileChannel.open(destination, optionSet, null, (FileAttribute<?>[])new FileAttribute[0]);
/*      */ 
/*      */             
/*      */             sink.onDispose(());
/*      */             
/*      */             write(source, channel).subscribe(DataBufferUtils::release, sink::error, sink::success);
/*  371 */           } catch (IOException ex) {
/*      */             sink.error(ex);
/*      */           } 
/*      */         });
/*      */   }
/*      */   
/*      */   private static Set<OpenOption> checkWriteOptions(OpenOption[] options) {
/*  378 */     int length = options.length;
/*  379 */     Set<OpenOption> result = new HashSet<>(length + 3);
/*  380 */     if (length == 0) {
/*  381 */       result.add(StandardOpenOption.CREATE);
/*  382 */       result.add(StandardOpenOption.TRUNCATE_EXISTING);
/*      */     } else {
/*      */       
/*  385 */       for (OpenOption opt : options) {
/*  386 */         if (opt == StandardOpenOption.READ) {
/*  387 */           throw new IllegalArgumentException("READ not allowed");
/*      */         }
/*  389 */         result.add(opt);
/*      */       } 
/*      */     } 
/*  392 */     result.add(StandardOpenOption.WRITE);
/*  393 */     return result;
/*      */   }
/*      */   
/*      */   static void closeChannel(@Nullable Channel channel) {
/*  397 */     if (channel != null && channel.isOpen()) {
/*      */       try {
/*  399 */         channel.close();
/*      */       }
/*  401 */       catch (IOException iOException) {}
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Flux<DataBuffer> takeUntilByteCount(Publisher<? extends DataBuffer> publisher, long maxByteCount) {
/*  420 */     Assert.notNull(publisher, "Publisher must not be null");
/*  421 */     Assert.isTrue((maxByteCount >= 0L), "'maxByteCount' must be a positive number");
/*      */     
/*  423 */     return Flux.defer(() -> {
/*      */           AtomicLong countDown = new AtomicLong(maxByteCount);
/*      */           return (Publisher)Flux.from(publisher).map(()).takeUntil(());
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Flux<DataBuffer> skipUntilByteCount(Publisher<? extends DataBuffer> publisher, long maxByteCount) {
/*  451 */     Assert.notNull(publisher, "Publisher must not be null");
/*  452 */     Assert.isTrue((maxByteCount >= 0L), "'maxByteCount' must be a positive number");
/*      */     
/*  454 */     return Flux.defer(() -> {
/*      */           AtomicLong countDown = new AtomicLong(maxByteCount);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           return (Publisher)Flux.from(publisher).skipUntil(()).map(());
/*  473 */         }).doOnDiscard(PooledDataBuffer.class, DataBufferUtils::release);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends DataBuffer> T retain(T dataBuffer) {
/*  483 */     if (dataBuffer instanceof PooledDataBuffer) {
/*  484 */       return (T)((PooledDataBuffer)dataBuffer).retain();
/*      */     }
/*      */     
/*  487 */     return dataBuffer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends DataBuffer> T touch(T dataBuffer, Object hint) {
/*  501 */     if (dataBuffer instanceof PooledDataBuffer) {
/*  502 */       return (T)((PooledDataBuffer)dataBuffer).touch(hint);
/*      */     }
/*      */     
/*  505 */     return dataBuffer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean release(@Nullable DataBuffer dataBuffer) {
/*  516 */     if (dataBuffer instanceof PooledDataBuffer) {
/*  517 */       PooledDataBuffer pooledDataBuffer = (PooledDataBuffer)dataBuffer;
/*  518 */       if (pooledDataBuffer.isAllocated()) {
/*      */         try {
/*  520 */           return pooledDataBuffer.release();
/*      */         }
/*  522 */         catch (IllegalStateException ex) {
/*      */           
/*  524 */           if (logger.isDebugEnabled()) {
/*  525 */             logger.debug("Failed to release PooledDataBuffer: " + dataBuffer, ex);
/*      */           }
/*  527 */           return false;
/*      */         } 
/*      */       }
/*      */     } 
/*  531 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Consumer<DataBuffer> releaseConsumer() {
/*  539 */     return RELEASE_CONSUMER;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Mono<DataBuffer> join(Publisher<? extends DataBuffer> dataBuffers) {
/*  558 */     return join(dataBuffers, -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Mono<DataBuffer> join(Publisher<? extends DataBuffer> buffers, int maxByteCount) {
/*  574 */     Assert.notNull(buffers, "'dataBuffers' must not be null");
/*      */     
/*  576 */     if (buffers instanceof Mono) {
/*  577 */       return (Mono)buffers;
/*      */     }
/*      */     
/*  580 */     return Flux.from(buffers)
/*  581 */       .collect(() -> new LimitedDataBufferList(maxByteCount), LimitedDataBufferList::add)
/*  582 */       .filter(list -> !list.isEmpty())
/*  583 */       .map(list -> list.get(0).factory().join(list))
/*  584 */       .doOnDiscard(PooledDataBuffer.class, DataBufferUtils::release);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Matcher matcher(byte[] delimiter) {
/*  595 */     return createMatcher(delimiter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Matcher matcher(byte[]... delimiters) {
/*  606 */     Assert.isTrue((delimiters.length > 0), "Delimiters must not be empty");
/*  607 */     return (delimiters.length == 1) ? createMatcher(delimiters[0]) : new CompositeMatcher(delimiters);
/*      */   }
/*      */   
/*      */   private static NestedMatcher createMatcher(byte[] delimiter) {
/*  611 */     Assert.isTrue((delimiter.length > 0), "Delimiter must not be empty");
/*  612 */     switch (delimiter.length) {
/*      */       case 1:
/*  614 */         return (delimiter[0] == 10) ? SingleByteMatcher.NEWLINE_MATCHER : new SingleByteMatcher(delimiter);
/*      */       case 2:
/*  616 */         return new TwoByteMatcher(delimiter);
/*      */     } 
/*  618 */     return new KnuthMorrisPrattMatcher(delimiter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static interface Matcher
/*      */   {
/*      */     int match(DataBuffer param1DataBuffer);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     byte[] delimiter();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void reset();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class CompositeMatcher
/*      */     implements Matcher
/*      */   {
/*  655 */     private static final byte[] NO_DELIMITER = new byte[0];
/*      */ 
/*      */     
/*      */     private final DataBufferUtils.NestedMatcher[] matchers;
/*      */     
/*  660 */     byte[] longestDelimiter = NO_DELIMITER;
/*      */     
/*      */     CompositeMatcher(byte[][] delimiters) {
/*  663 */       this.matchers = initMatchers(delimiters);
/*      */     }
/*      */     
/*      */     private static DataBufferUtils.NestedMatcher[] initMatchers(byte[][] delimiters) {
/*  667 */       DataBufferUtils.NestedMatcher[] matchers = new DataBufferUtils.NestedMatcher[delimiters.length];
/*  668 */       for (int i = 0; i < delimiters.length; i++) {
/*  669 */         matchers[i] = DataBufferUtils.createMatcher(delimiters[i]);
/*      */       }
/*  671 */       return matchers;
/*      */     }
/*      */ 
/*      */     
/*      */     public int match(DataBuffer dataBuffer) {
/*  676 */       this.longestDelimiter = NO_DELIMITER;
/*      */       
/*  678 */       for (int pos = dataBuffer.readPosition(); pos < dataBuffer.writePosition(); pos++) {
/*  679 */         byte b = dataBuffer.getByte(pos);
/*      */         
/*  681 */         for (DataBufferUtils.NestedMatcher matcher : this.matchers) {
/*  682 */           if (matcher.match(b) && (matcher.delimiter()).length > this.longestDelimiter.length) {
/*  683 */             this.longestDelimiter = matcher.delimiter();
/*      */           }
/*      */         } 
/*      */         
/*  687 */         if (this.longestDelimiter != NO_DELIMITER) {
/*  688 */           reset();
/*  689 */           return pos;
/*      */         } 
/*      */       } 
/*  692 */       return -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public byte[] delimiter() {
/*  697 */       Assert.state((this.longestDelimiter != NO_DELIMITER), "Illegal state!");
/*  698 */       return this.longestDelimiter;
/*      */     }
/*      */ 
/*      */     
/*      */     public void reset() {
/*  703 */       for (DataBufferUtils.NestedMatcher matcher : this.matchers) {
/*  704 */         matcher.reset();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static interface NestedMatcher
/*      */     extends Matcher
/*      */   {
/*      */     boolean match(byte param1Byte);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class SingleByteMatcher
/*      */     implements NestedMatcher
/*      */   {
/*  730 */     static SingleByteMatcher NEWLINE_MATCHER = new SingleByteMatcher(new byte[] { 10 });
/*      */     
/*      */     private final byte[] delimiter;
/*      */     
/*      */     SingleByteMatcher(byte[] delimiter) {
/*  735 */       Assert.isTrue((delimiter.length == 1), "Expected a 1 byte delimiter");
/*  736 */       this.delimiter = delimiter;
/*      */     }
/*      */ 
/*      */     
/*      */     public int match(DataBuffer dataBuffer) {
/*  741 */       for (int pos = dataBuffer.readPosition(); pos < dataBuffer.writePosition(); pos++) {
/*  742 */         byte b = dataBuffer.getByte(pos);
/*  743 */         if (match(b)) {
/*  744 */           return pos;
/*      */         }
/*      */       } 
/*  747 */       return -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean match(byte b) {
/*  752 */       return (this.delimiter[0] == b);
/*      */     }
/*      */ 
/*      */     
/*      */     public byte[] delimiter() {
/*  757 */       return this.delimiter;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void reset() {}
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static abstract class AbstractNestedMatcher
/*      */     implements NestedMatcher
/*      */   {
/*      */     private final byte[] delimiter;
/*      */ 
/*      */     
/*  773 */     private int matches = 0;
/*      */ 
/*      */     
/*      */     protected AbstractNestedMatcher(byte[] delimiter) {
/*  777 */       this.delimiter = delimiter;
/*      */     }
/*      */     
/*      */     protected void setMatches(int index) {
/*  781 */       this.matches = index;
/*      */     }
/*      */     
/*      */     protected int getMatches() {
/*  785 */       return this.matches;
/*      */     }
/*      */ 
/*      */     
/*      */     public int match(DataBuffer dataBuffer) {
/*  790 */       for (int pos = dataBuffer.readPosition(); pos < dataBuffer.writePosition(); pos++) {
/*  791 */         byte b = dataBuffer.getByte(pos);
/*  792 */         if (match(b)) {
/*  793 */           reset();
/*  794 */           return pos;
/*      */         } 
/*      */       } 
/*  797 */       return -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean match(byte b) {
/*  802 */       if (b == this.delimiter[this.matches]) {
/*  803 */         this.matches++;
/*  804 */         return (this.matches == (delimiter()).length);
/*      */       } 
/*  806 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public byte[] delimiter() {
/*  811 */       return this.delimiter;
/*      */     }
/*      */ 
/*      */     
/*      */     public void reset() {
/*  816 */       this.matches = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TwoByteMatcher
/*      */     extends AbstractNestedMatcher
/*      */   {
/*      */     protected TwoByteMatcher(byte[] delimiter) {
/*  828 */       super(delimiter);
/*  829 */       Assert.isTrue((delimiter.length == 2), "Expected a 2-byte delimiter");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class KnuthMorrisPrattMatcher
/*      */     extends AbstractNestedMatcher
/*      */   {
/*      */     private final int[] table;
/*      */ 
/*      */ 
/*      */     
/*      */     public KnuthMorrisPrattMatcher(byte[] delimiter) {
/*  843 */       super(delimiter);
/*  844 */       this.table = longestSuffixPrefixTable(delimiter);
/*      */     }
/*      */     
/*      */     private static int[] longestSuffixPrefixTable(byte[] delimiter) {
/*  848 */       int[] result = new int[delimiter.length];
/*  849 */       result[0] = 0;
/*  850 */       for (int i = 1; i < delimiter.length; i++) {
/*  851 */         int j = result[i - 1];
/*  852 */         while (j > 0 && delimiter[i] != delimiter[j]) {
/*  853 */           j = result[j - 1];
/*      */         }
/*  855 */         if (delimiter[i] == delimiter[j]) {
/*  856 */           j++;
/*      */         }
/*  858 */         result[i] = j;
/*      */       } 
/*  860 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean match(byte b) {
/*  865 */       while (getMatches() > 0 && b != delimiter()[getMatches()]) {
/*  866 */         setMatches(this.table[getMatches() - 1]);
/*      */       }
/*  868 */       return super.match(b);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class ReadableByteChannelGenerator
/*      */     implements Consumer<SynchronousSink<DataBuffer>>
/*      */   {
/*      */     private final ReadableByteChannel channel;
/*      */     
/*      */     private final DataBufferFactory dataBufferFactory;
/*      */     
/*      */     private final int bufferSize;
/*      */ 
/*      */     
/*      */     public ReadableByteChannelGenerator(ReadableByteChannel channel, DataBufferFactory dataBufferFactory, int bufferSize) {
/*  884 */       this.channel = channel;
/*  885 */       this.dataBufferFactory = dataBufferFactory;
/*  886 */       this.bufferSize = bufferSize;
/*      */     }
/*      */ 
/*      */     
/*      */     public void accept(SynchronousSink<DataBuffer> sink) {
/*  891 */       boolean release = true;
/*  892 */       DataBuffer dataBuffer = this.dataBufferFactory.allocateBuffer(this.bufferSize);
/*      */       
/*      */       try {
/*  895 */         ByteBuffer byteBuffer = dataBuffer.asByteBuffer(0, dataBuffer.capacity()); int read;
/*  896 */         if ((read = this.channel.read(byteBuffer)) >= 0) {
/*  897 */           dataBuffer.writePosition(read);
/*  898 */           release = false;
/*  899 */           sink.next(dataBuffer);
/*      */         } else {
/*      */           
/*  902 */           sink.complete();
/*      */         }
/*      */       
/*  905 */       } catch (IOException ex) {
/*  906 */         sink.error(ex);
/*      */       } finally {
/*      */         
/*  909 */         if (release) {
/*  910 */           DataBufferUtils.release(dataBuffer);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class ReadCompletionHandler
/*      */     implements CompletionHandler<Integer, DataBuffer>
/*      */   {
/*      */     private final AsynchronousFileChannel channel;
/*      */     
/*      */     private final FluxSink<DataBuffer> sink;
/*      */     
/*      */     private final DataBufferFactory dataBufferFactory;
/*      */     
/*      */     private final int bufferSize;
/*      */     
/*      */     private final AtomicLong position;
/*  929 */     private final AtomicReference<State> state = new AtomicReference<>(State.IDLE);
/*      */ 
/*      */ 
/*      */     
/*      */     public ReadCompletionHandler(AsynchronousFileChannel channel, FluxSink<DataBuffer> sink, long position, DataBufferFactory dataBufferFactory, int bufferSize) {
/*  934 */       this.channel = channel;
/*  935 */       this.sink = sink;
/*  936 */       this.position = new AtomicLong(position);
/*  937 */       this.dataBufferFactory = dataBufferFactory;
/*  938 */       this.bufferSize = bufferSize;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void request(long n) {
/*  945 */       tryRead();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void cancel() {
/*  952 */       this.state.getAndSet(State.DISPOSED);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  959 */       DataBufferUtils.closeChannel(this.channel);
/*      */     }
/*      */     
/*      */     private void tryRead() {
/*  963 */       if (this.sink.requestedFromDownstream() > 0L && this.state.compareAndSet(State.IDLE, State.READING)) {
/*  964 */         read();
/*      */       }
/*      */     }
/*      */     
/*      */     private void read() {
/*  969 */       DataBuffer dataBuffer = this.dataBufferFactory.allocateBuffer(this.bufferSize);
/*  970 */       ByteBuffer byteBuffer = dataBuffer.asByteBuffer(0, this.bufferSize);
/*  971 */       this.channel.read(byteBuffer, this.position.get(), dataBuffer, this);
/*      */     }
/*      */ 
/*      */     
/*      */     public void completed(Integer read, DataBuffer dataBuffer) {
/*  976 */       if (((State)this.state.get()).equals(State.DISPOSED)) {
/*  977 */         DataBufferUtils.release(dataBuffer);
/*  978 */         DataBufferUtils.closeChannel(this.channel);
/*      */         
/*      */         return;
/*      */       } 
/*  982 */       if (read.intValue() == -1) {
/*  983 */         DataBufferUtils.release(dataBuffer);
/*  984 */         DataBufferUtils.closeChannel(this.channel);
/*  985 */         this.state.set(State.DISPOSED);
/*  986 */         this.sink.complete();
/*      */         
/*      */         return;
/*      */       } 
/*  990 */       this.position.addAndGet(read.intValue());
/*  991 */       dataBuffer.writePosition(read.intValue());
/*  992 */       this.sink.next(dataBuffer);
/*      */ 
/*      */       
/*  995 */       if (this.sink.requestedFromDownstream() > 0L) {
/*  996 */         read();
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/* 1001 */       if (this.state.compareAndSet(State.READING, State.IDLE)) {
/* 1002 */         tryRead();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public void failed(Throwable exc, DataBuffer dataBuffer) {
/* 1008 */       DataBufferUtils.release(dataBuffer);
/* 1009 */       DataBufferUtils.closeChannel(this.channel);
/* 1010 */       this.state.set(State.DISPOSED);
/* 1011 */       this.sink.error(exc);
/*      */     }
/*      */     
/*      */     private enum State {
/* 1015 */       IDLE, READING, DISPOSED;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class WritableByteChannelSubscriber
/*      */     extends BaseSubscriber<DataBuffer>
/*      */   {
/*      */     private final FluxSink<DataBuffer> sink;
/*      */     private final WritableByteChannel channel;
/*      */     
/*      */     public WritableByteChannelSubscriber(FluxSink<DataBuffer> sink, WritableByteChannel channel) {
/* 1027 */       this.sink = sink;
/* 1028 */       this.channel = channel;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void hookOnSubscribe(Subscription subscription) {
/* 1033 */       request(1L);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void hookOnNext(DataBuffer dataBuffer) {
/*      */       try {
/* 1039 */         ByteBuffer byteBuffer = dataBuffer.asByteBuffer();
/* 1040 */         while (byteBuffer.hasRemaining()) {
/* 1041 */           this.channel.write(byteBuffer);
/*      */         }
/* 1043 */         this.sink.next(dataBuffer);
/* 1044 */         request(1L);
/*      */       }
/* 1046 */       catch (IOException ex) {
/* 1047 */         this.sink.next(dataBuffer);
/* 1048 */         this.sink.error(ex);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     protected void hookOnError(Throwable throwable) {
/* 1054 */       this.sink.error(throwable);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void hookOnComplete() {
/* 1059 */       this.sink.complete();
/*      */     }
/*      */ 
/*      */     
/*      */     public Context currentContext() {
/* 1064 */       return this.sink.currentContext();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class WriteCompletionHandler
/*      */     extends BaseSubscriber<DataBuffer>
/*      */     implements CompletionHandler<Integer, ByteBuffer>
/*      */   {
/*      */     private final FluxSink<DataBuffer> sink;
/*      */     
/*      */     private final AsynchronousFileChannel channel;
/*      */     
/* 1077 */     private final AtomicBoolean completed = new AtomicBoolean();
/*      */     
/* 1079 */     private final AtomicReference<Throwable> error = new AtomicReference<>();
/*      */     
/*      */     private final AtomicLong position;
/*      */     
/* 1083 */     private final AtomicReference<DataBuffer> dataBuffer = new AtomicReference<>();
/*      */ 
/*      */ 
/*      */     
/*      */     public WriteCompletionHandler(FluxSink<DataBuffer> sink, AsynchronousFileChannel channel, long position) {
/* 1088 */       this.sink = sink;
/* 1089 */       this.channel = channel;
/* 1090 */       this.position = new AtomicLong(position);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void hookOnSubscribe(Subscription subscription) {
/* 1095 */       request(1L);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void hookOnNext(DataBuffer value) {
/* 1100 */       if (!this.dataBuffer.compareAndSet(null, value)) {
/* 1101 */         throw new IllegalStateException();
/*      */       }
/* 1103 */       ByteBuffer byteBuffer = value.asByteBuffer();
/* 1104 */       this.channel.write(byteBuffer, this.position.get(), byteBuffer, this);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void hookOnError(Throwable throwable) {
/* 1109 */       this.error.set(throwable);
/*      */       
/* 1111 */       if (this.dataBuffer.get() == null) {
/* 1112 */         this.sink.error(throwable);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     protected void hookOnComplete() {
/* 1118 */       this.completed.set(true);
/*      */       
/* 1120 */       if (this.dataBuffer.get() == null) {
/* 1121 */         this.sink.complete();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public void completed(Integer written, ByteBuffer byteBuffer) {
/* 1127 */       long pos = this.position.addAndGet(written.intValue());
/* 1128 */       if (byteBuffer.hasRemaining()) {
/* 1129 */         this.channel.write(byteBuffer, pos, byteBuffer, this);
/*      */         return;
/*      */       } 
/* 1132 */       sinkDataBuffer();
/*      */       
/* 1134 */       Throwable throwable = this.error.get();
/* 1135 */       if (throwable != null) {
/* 1136 */         this.sink.error(throwable);
/*      */       }
/* 1138 */       else if (this.completed.get()) {
/* 1139 */         this.sink.complete();
/*      */       } else {
/*      */         
/* 1142 */         request(1L);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void failed(Throwable exc, ByteBuffer byteBuffer) {
/* 1148 */       sinkDataBuffer();
/* 1149 */       this.sink.error(exc);
/*      */     }
/*      */     
/*      */     private void sinkDataBuffer() {
/* 1153 */       DataBuffer dataBuffer = this.dataBuffer.get();
/* 1154 */       Assert.state((dataBuffer != null), "DataBuffer should not be null");
/* 1155 */       this.sink.next(dataBuffer);
/* 1156 */       this.dataBuffer.set(null);
/*      */     }
/*      */ 
/*      */     
/*      */     public Context currentContext() {
/* 1161 */       return this.sink.currentContext();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\io\buffer\DataBufferUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */