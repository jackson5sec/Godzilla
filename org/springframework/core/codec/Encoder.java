/*     */ package org.springframework.core.codec;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.core.io.buffer.DataBufferFactory;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.MimeType;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Encoder<T>
/*     */ {
/*     */   boolean canEncode(ResolvableType paramResolvableType, @Nullable MimeType paramMimeType);
/*     */   
/*     */   Flux<DataBuffer> encode(Publisher<? extends T> paramPublisher, DataBufferFactory paramDataBufferFactory, ResolvableType paramResolvableType, @Nullable MimeType paramMimeType, @Nullable Map<String, Object> paramMap);
/*     */   
/*     */   default DataBuffer encodeValue(T value, DataBufferFactory bufferFactory, ResolvableType valueType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/*  90 */     throw new UnsupportedOperationException();
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
/*     */   List<MimeType> getEncodableMimeTypes();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default List<MimeType> getEncodableMimeTypes(ResolvableType elementType) {
/* 114 */     return canEncode(elementType, null) ? getEncodableMimeTypes() : Collections.<MimeType>emptyList();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\codec\Encoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */