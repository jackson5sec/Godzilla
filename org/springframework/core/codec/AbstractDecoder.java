/*    */ package org.springframework.core.codec;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.reactivestreams.Publisher;
/*    */ import org.springframework.core.ResolvableType;
/*    */ import org.springframework.core.io.buffer.DataBuffer;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.MimeType;
/*    */ import reactor.core.publisher.Mono;
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
/*    */ 
/*    */ 
/*    */ public abstract class AbstractDecoder<T>
/*    */   implements Decoder<T>
/*    */ {
/*    */   private final List<MimeType> decodableMimeTypes;
/* 45 */   protected Log logger = LogFactory.getLog(getClass());
/*    */ 
/*    */   
/*    */   protected AbstractDecoder(MimeType... supportedMimeTypes) {
/* 49 */     this.decodableMimeTypes = Arrays.asList(supportedMimeTypes);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLogger(Log logger) {
/* 59 */     this.logger = logger;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Log getLogger() {
/* 67 */     return this.logger;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<MimeType> getDecodableMimeTypes() {
/* 73 */     return this.decodableMimeTypes;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canDecode(ResolvableType elementType, @Nullable MimeType mimeType) {
/* 78 */     if (mimeType == null) {
/* 79 */       return true;
/*    */     }
/* 81 */     for (MimeType candidate : this.decodableMimeTypes) {
/* 82 */       if (candidate.isCompatibleWith(mimeType)) {
/* 83 */         return true;
/*    */       }
/*    */     } 
/* 86 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Mono<T> decodeToMono(Publisher<DataBuffer> inputStream, ResolvableType elementType, @Nullable MimeType mimeType, @Nullable Map<String, Object> hints) {
/* 93 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\codec\AbstractDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */