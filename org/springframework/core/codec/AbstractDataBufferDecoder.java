/*     */ package org.springframework.core.codec;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferUtils;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.MimeType;
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
/*     */ 
/*     */ 
/*     */ public abstract class AbstractDataBufferDecoder<T>
/*     */   extends AbstractDecoder<T>
/*     */ {
/*  51 */   private int maxInMemorySize = 262144;
/*     */ 
/*     */   
/*     */   protected AbstractDataBufferDecoder(MimeType... supportedMimeTypes) {
/*  55 */     super(supportedMimeTypes);
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
/*     */   public void setMaxInMemorySize(int byteCount) {
/*  72 */     this.maxInMemorySize = byteCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxInMemorySize() {
/*  80 */     return this.maxInMemorySize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Flux<T> decode(Publisher<DataBuffer> input, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/*  88 */     return Flux.from(input).map(buffer -> decodeDataBuffer(buffer, elementType, mimeType, hints));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Mono<T> decodeToMono(Publisher<DataBuffer> input, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/*  95 */     return DataBufferUtils.join(input, this.maxInMemorySize)
/*  96 */       .map(buffer -> decodeDataBuffer(buffer, elementType, mimeType, hints));
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
/*     */   @Nullable
/*     */   protected T decodeDataBuffer(DataBuffer buffer, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 109 */     return decode(buffer, elementType, mimeType, hints);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\codec\AbstractDataBufferDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */