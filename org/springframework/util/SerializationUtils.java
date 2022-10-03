/*    */ package org.springframework.util;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
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
/*    */ public abstract class SerializationUtils
/*    */ {
/*    */   @Nullable
/*    */   public static byte[] serialize(@Nullable Object object) {
/* 42 */     if (object == null) {
/* 43 */       return null;
/*    */     }
/* 45 */     ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
/* 46 */     try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
/* 47 */       oos.writeObject(object);
/* 48 */       oos.flush();
/*    */     }
/* 50 */     catch (IOException ex) {
/* 51 */       throw new IllegalArgumentException("Failed to serialize object of type: " + object.getClass(), ex);
/*    */     } 
/* 53 */     return baos.toByteArray();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public static Object deserialize(@Nullable byte[] bytes) {
/* 63 */     if (bytes == null) {
/* 64 */       return null;
/*    */     }
/* 66 */     try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
/* 67 */       return ois.readObject();
/*    */     }
/* 69 */     catch (IOException ex) {
/* 70 */       throw new IllegalArgumentException("Failed to deserialize object", ex);
/*    */     }
/* 72 */     catch (ClassNotFoundException ex) {
/* 73 */       throw new IllegalStateException("Failed to deserialize object type", ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\springframewor\\util\SerializationUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */