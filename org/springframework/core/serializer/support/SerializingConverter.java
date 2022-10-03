/*    */ package org.springframework.core.serializer.support;
/*    */ 
/*    */ import org.springframework.core.convert.converter.Converter;
/*    */ import org.springframework.core.serializer.DefaultSerializer;
/*    */ import org.springframework.core.serializer.Serializer;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ 
/*    */ public class SerializingConverter
/*    */   implements Converter<Object, byte[]>
/*    */ {
/*    */   private final Serializer<Object> serializer;
/*    */   
/*    */   public SerializingConverter() {
/* 42 */     this.serializer = (Serializer<Object>)new DefaultSerializer();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SerializingConverter(Serializer<Object> serializer) {
/* 49 */     Assert.notNull(serializer, "Serializer must not be null");
/* 50 */     this.serializer = serializer;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] convert(Object source) {
/*    */     try {
/* 60 */       return this.serializer.serializeToByteArray(source);
/*    */     }
/* 62 */     catch (Throwable ex) {
/* 63 */       throw new SerializationFailedException("Failed to serialize object using " + this.serializer
/* 64 */           .getClass().getSimpleName(), ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\serializer\support\SerializingConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */