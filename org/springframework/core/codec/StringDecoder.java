/*     */ package org.springframework.core.codec;
/*     */ 
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferUtils;
/*     */ import org.springframework.core.io.buffer.LimitedDataBufferList;
/*     */ import org.springframework.core.io.buffer.PooledDataBuffer;
/*     */ import org.springframework.core.log.LogFormatUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.MimeType;
/*     */ import org.springframework.util.MimeTypeUtils;
/*     */ import reactor.core.publisher.Flux;
/*     */ import reactor.core.publisher.Mono;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class StringDecoder
/*     */   extends AbstractDataBufferDecoder<String>
/*     */ {
/*  64 */   public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
/*     */ 
/*     */   
/*  67 */   public static final List<String> DEFAULT_DELIMITERS = Arrays.asList(new String[] { "\r\n", "\n" });
/*     */ 
/*     */   
/*     */   private final List<String> delimiters;
/*     */   
/*     */   private final boolean stripDelimiter;
/*     */   
/*  74 */   private Charset defaultCharset = DEFAULT_CHARSET;
/*     */   
/*  76 */   private final ConcurrentMap<Charset, byte[][]> delimitersCache = (ConcurrentMap)new ConcurrentHashMap<>();
/*     */ 
/*     */   
/*     */   private StringDecoder(List<String> delimiters, boolean stripDelimiter, MimeType... mimeTypes) {
/*  80 */     super(mimeTypes);
/*  81 */     Assert.notEmpty(delimiters, "'delimiters' must not be empty");
/*  82 */     this.delimiters = new ArrayList<>(delimiters);
/*  83 */     this.stripDelimiter = stripDelimiter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultCharset(Charset defaultCharset) {
/*  94 */     this.defaultCharset = defaultCharset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Charset getDefaultCharset() {
/* 102 */     return this.defaultCharset;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canDecode(ResolvableType elementType, @Nullable MimeType mimeType) {
/* 108 */     return (elementType.resolve() == String.class && super.canDecode(elementType, mimeType));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Flux<String> decode(Publisher<DataBuffer> input, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 115 */     byte[][] delimiterBytes = getDelimiterBytes(mimeType);
/*     */     
/* 117 */     LimitedDataBufferList chunks = new LimitedDataBufferList(getMaxInMemorySize());
/* 118 */     DataBufferUtils.Matcher matcher = DataBufferUtils.matcher(delimiterBytes);
/*     */     
/* 120 */     return Flux.from(input)
/* 121 */       .concatMapIterable(buffer -> processDataBuffer(buffer, matcher, chunks))
/* 122 */       .concatWith((Publisher)Mono.defer(() -> {
/*     */             if (chunks.isEmpty()) {
/*     */               return Mono.empty();
/*     */             }
/*     */             
/*     */             DataBuffer lastBuffer = ((DataBuffer)chunks.get(0)).factory().join((List)chunks);
/*     */             chunks.clear();
/*     */             return Mono.just(lastBuffer);
/* 130 */           })).doOnTerminate(chunks::releaseAndClear)
/* 131 */       .doOnDiscard(PooledDataBuffer.class, PooledDataBuffer::release)
/* 132 */       .map(buffer -> decode(buffer, elementType, mimeType, hints));
/*     */   }
/*     */   
/*     */   private byte[][] getDelimiterBytes(@Nullable MimeType mimeType) {
/* 136 */     return this.delimitersCache.computeIfAbsent(getCharset(mimeType), charset -> {
/*     */           byte[][] result = new byte[this.delimiters.size()][];
/*     */           for (int i = 0; i < this.delimiters.size(); i++) {
/*     */             result[i] = ((String)this.delimiters.get(i)).getBytes(charset);
/*     */           }
/*     */           return result;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Collection<DataBuffer> processDataBuffer(DataBuffer buffer, DataBufferUtils.Matcher matcher, LimitedDataBufferList chunks) {
/*     */     try {
/* 149 */       List<DataBuffer> result = null;
/*     */       do {
/* 151 */         int endIndex = matcher.match(buffer);
/* 152 */         if (endIndex == -1) {
/* 153 */           chunks.add(buffer);
/* 154 */           DataBufferUtils.retain(buffer);
/*     */           break;
/*     */         } 
/* 157 */         int startIndex = buffer.readPosition();
/* 158 */         int length = endIndex - startIndex + 1;
/* 159 */         DataBuffer slice = buffer.retainedSlice(startIndex, length);
/* 160 */         result = (result != null) ? result : new ArrayList<>();
/* 161 */         if (chunks.isEmpty()) {
/* 162 */           if (this.stripDelimiter) {
/* 163 */             slice.writePosition(slice.writePosition() - (matcher.delimiter()).length);
/*     */           }
/* 165 */           result.add(slice);
/*     */         } else {
/*     */           
/* 168 */           chunks.add(slice);
/* 169 */           DataBuffer joined = buffer.factory().join((List)chunks);
/* 170 */           if (this.stripDelimiter) {
/* 171 */             joined.writePosition(joined.writePosition() - (matcher.delimiter()).length);
/*     */           }
/* 173 */           result.add(joined);
/* 174 */           chunks.clear();
/*     */         } 
/* 176 */         buffer.readPosition(endIndex + 1);
/*     */       }
/* 178 */       while (buffer.readableByteCount() > 0);
/* 179 */       return (Collection<DataBuffer>)((result != null) ? result : Collections.emptyList());
/*     */     } finally {
/*     */       
/* 182 */       DataBufferUtils.release(buffer);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String decode(DataBuffer dataBuffer, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 190 */     Charset charset = getCharset(mimeType);
/* 191 */     CharBuffer charBuffer = charset.decode(dataBuffer.asByteBuffer());
/* 192 */     DataBufferUtils.release(dataBuffer);
/* 193 */     String value = charBuffer.toString();
/* 194 */     LogFormatUtils.traceDebug(this.logger, traceOn -> {
/*     */           String formatted = LogFormatUtils.formatValue(value, !traceOn.booleanValue());
/*     */           return Hints.getLogPrefix(hints) + "Decoded " + formatted;
/*     */         });
/* 198 */     return value;
/*     */   }
/*     */   
/*     */   private Charset getCharset(@Nullable MimeType mimeType) {
/* 202 */     if (mimeType != null && mimeType.getCharset() != null) {
/* 203 */       return mimeType.getCharset();
/*     */     }
/*     */     
/* 206 */     return getDefaultCharset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static StringDecoder textPlainOnly(boolean stripDelimiter) {
/* 218 */     return textPlainOnly();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StringDecoder textPlainOnly() {
/* 225 */     return textPlainOnly(DEFAULT_DELIMITERS, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StringDecoder textPlainOnly(List<String> delimiters, boolean stripDelimiter) {
/* 235 */     return new StringDecoder(delimiters, stripDelimiter, new MimeType[] { new MimeType("text", "plain", DEFAULT_CHARSET) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static StringDecoder allMimeTypes(boolean stripDelimiter) {
/* 246 */     return allMimeTypes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StringDecoder allMimeTypes() {
/* 253 */     return allMimeTypes(DEFAULT_DELIMITERS, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static StringDecoder allMimeTypes(List<String> delimiters, boolean stripDelimiter) {
/* 263 */     return new StringDecoder(delimiters, stripDelimiter, new MimeType[] { new MimeType("text", "plain", DEFAULT_CHARSET), MimeTypeUtils.ALL });
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\codec\StringDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */