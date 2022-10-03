/*     */ package org.springframework.core.codec;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import org.reactivestreams.Publisher;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.io.buffer.DataBuffer;
/*     */ import org.springframework.lang.Nullable;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Decoder<T>
/*     */ {
/*     */   boolean canDecode(ResolvableType paramResolvableType, @Nullable MimeType paramMimeType);
/*     */   
/*     */   Flux<T> decode(Publisher<DataBuffer> paramPublisher, ResolvableType paramResolvableType, @Nullable MimeType paramMimeType, @Nullable Map<String, Object> paramMap);
/*     */   
/*     */   Mono<T> decodeToMono(Publisher<DataBuffer> paramPublisher, ResolvableType paramResolvableType, @Nullable MimeType paramMimeType, @Nullable Map<String, Object> paramMap);
/*     */   
/*     */   @Nullable
/*     */   default T decode(DataBuffer buffer, ResolvableType targetType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) throws DecodingException {
/*     */     Throwable failure;
/*  97 */     CompletableFuture<T> future = decodeToMono((Publisher<DataBuffer>)Mono.just(buffer), targetType, mimeType, hints).toFuture();
/*  98 */     Assert.state(future.isDone(), "DataBuffer decoding should have completed.");
/*     */ 
/*     */     
/*     */     try {
/* 102 */       return future.get();
/*     */     }
/* 104 */     catch (ExecutionException ex) {
/* 105 */       failure = ex.getCause();
/*     */     }
/* 107 */     catch (InterruptedException ex) {
/* 108 */       failure = ex;
/*     */     } 
/* 110 */     throw (failure instanceof CodecException) ? (CodecException)failure : new DecodingException("Failed to decode: " + failure
/* 111 */         .getMessage(), failure);
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
/*     */   List<MimeType> getDecodableMimeTypes();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default List<MimeType> getDecodableMimeTypes(ResolvableType targetType) {
/* 135 */     return canDecode(targetType, null) ? getDecodableMimeTypes() : Collections.<MimeType>emptyList();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\codec\Decoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */