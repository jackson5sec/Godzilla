/*    */ package org.springframework.core.serializer;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
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
/*    */ @FunctionalInterface
/*    */ public interface Serializer<T>
/*    */ {
/*    */   void serialize(T paramT, OutputStream paramOutputStream) throws IOException;
/*    */   
/*    */   default byte[] serializeToByteArray(T object) throws IOException {
/* 55 */     ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
/* 56 */     serialize(object, out);
/* 57 */     return out.toByteArray();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframework\core\serializer\Serializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */