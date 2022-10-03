/*    */ package org.springframework.core.serializer;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.springframework.core.ConfigurableObjectInputStream;
/*    */ import org.springframework.core.NestedIOException;
/*    */ import org.springframework.lang.Nullable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultDeserializer
/*    */   implements Deserializer<Object>
/*    */ {
/*    */   @Nullable
/*    */   private final ClassLoader classLoader;
/*    */   
/*    */   public DefaultDeserializer() {
/* 48 */     this.classLoader = null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultDeserializer(@Nullable ClassLoader classLoader) {
/* 58 */     this.classLoader = classLoader;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object deserialize(InputStream inputStream) throws IOException {
/* 70 */     ConfigurableObjectInputStream configurableObjectInputStream = new ConfigurableObjectInputStream(inputStream, this.classLoader);
/*    */     try {
/* 72 */       return configurableObjectInputStream.readObject();
/*    */     }
/* 74 */     catch (ClassNotFoundException ex) {
/* 75 */       throw new NestedIOException("Failed to deserialize object type", ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\serializer\DefaultDeserializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */