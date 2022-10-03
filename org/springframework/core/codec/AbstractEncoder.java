/*    */ package org.springframework.core.codec;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.core.ResolvableType;
/*    */ import org.springframework.lang.Nullable;
/*    */ import org.springframework.util.MimeType;
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
/*    */ public abstract class AbstractEncoder<T>
/*    */   implements Encoder<T>
/*    */ {
/*    */   private final List<MimeType> encodableMimeTypes;
/* 41 */   protected Log logger = LogFactory.getLog(getClass());
/*    */ 
/*    */   
/*    */   protected AbstractEncoder(MimeType... supportedMimeTypes) {
/* 45 */     this.encodableMimeTypes = Arrays.asList(supportedMimeTypes);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLogger(Log logger) {
/* 55 */     this.logger = logger;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Log getLogger() {
/* 63 */     return this.logger;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<MimeType> getEncodableMimeTypes() {
/* 69 */     return this.encodableMimeTypes;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canEncode(ResolvableType elementType, @Nullable MimeType mimeType) {
/* 74 */     if (mimeType == null) {
/* 75 */       return true;
/*    */     }
/* 77 */     for (MimeType candidate : this.encodableMimeTypes) {
/* 78 */       if (candidate.isCompatibleWith(mimeType)) {
/* 79 */         return true;
/*    */       }
/*    */     } 
/* 82 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\codec\AbstractEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */