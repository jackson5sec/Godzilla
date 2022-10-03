/*     */ package org.springframework.core.codec;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CoderMalfunctionError;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.core.io.buffer.DataBufferUtils;
/*     */ import org.springframework.core.log.LogFormatUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.MimeType;
/*     */ import org.springframework.util.MimeTypeUtils;
/*     */ import reactor.core.publisher.Flux;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CharSequenceEncoder
/*     */   extends AbstractEncoder<CharSequence>
/*     */ {
/*  52 */   public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
/*     */   
/*  54 */   private final ConcurrentMap<Charset, Float> charsetToMaxBytesPerChar = new ConcurrentHashMap<>(3);
/*     */ 
/*     */ 
/*     */   
/*     */   private CharSequenceEncoder(MimeType... mimeTypes) {
/*  59 */     super(mimeTypes);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canEncode(ResolvableType elementType, @Nullable MimeType mimeType) {
/*  65 */     Class<?> clazz = elementType.toClass();
/*  66 */     return (super.canEncode(elementType, mimeType) && CharSequence.class.isAssignableFrom(clazz));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Flux<DataBuffer> encode(Publisher<? extends CharSequence> inputStream, DataBufferFactory bufferFactory, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/*  74 */     return Flux.from(inputStream).map(charSequence -> encodeValue(charSequence, bufferFactory, elementType, mimeType, hints));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DataBuffer encodeValue(CharSequence charSequence, DataBufferFactory bufferFactory, ResolvableType valueType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/*  82 */     if (!Hints.isLoggingSuppressed(hints)) {
/*  83 */       LogFormatUtils.traceDebug(this.logger, traceOn -> {
/*     */             String formatted = LogFormatUtils.formatValue(charSequence, !traceOn.booleanValue());
/*     */             return Hints.getLogPrefix(hints) + "Writing " + formatted;
/*     */           });
/*     */     }
/*  88 */     boolean release = true;
/*  89 */     Charset charset = getCharset(mimeType);
/*  90 */     int capacity = calculateCapacity(charSequence, charset);
/*  91 */     DataBuffer dataBuffer = bufferFactory.allocateBuffer(capacity);
/*     */     try {
/*  93 */       dataBuffer.write(charSequence, charset);
/*  94 */       release = false;
/*     */     }
/*  96 */     catch (CoderMalfunctionError ex) {
/*  97 */       throw new EncodingException("String encoding error: " + ex.getMessage(), ex);
/*     */     } finally {
/*     */       
/* 100 */       if (release) {
/* 101 */         DataBufferUtils.release(dataBuffer);
/*     */       }
/*     */     } 
/* 104 */     return dataBuffer;
/*     */   }
/*     */ 
/*     */   
/*     */   int calculateCapacity(CharSequence sequence, Charset charset) {
/* 109 */     float maxBytesPerChar = ((Float)this.charsetToMaxBytesPerChar.computeIfAbsent(charset, cs -> Float.valueOf(cs.newEncoder().maxBytesPerChar()))).floatValue();
/* 110 */     float maxBytesForSequence = sequence.length() * maxBytesPerChar;
/* 111 */     return (int)Math.ceil(maxBytesForSequence);
/*     */   }
/*     */   
/*     */   private Charset getCharset(@Nullable MimeType mimeType) {
/* 115 */     if (mimeType != null && mimeType.getCharset() != null) {
/* 116 */       return mimeType.getCharset();
/*     */     }
/*     */     
/* 119 */     return DEFAULT_CHARSET;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSequenceEncoder textPlainOnly() {
/* 128 */     return new CharSequenceEncoder(new MimeType[] { new MimeType("text", "plain", DEFAULT_CHARSET) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharSequenceEncoder allMimeTypes() {
/* 135 */     return new CharSequenceEncoder(new MimeType[] { new MimeType("text", "plain", DEFAULT_CHARSET), MimeTypeUtils.ALL });
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\codec\CharSequenceEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */