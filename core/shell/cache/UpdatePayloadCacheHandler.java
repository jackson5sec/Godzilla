/*    */ package core.shell.cache;
/*    */ 
/*    */ import core.imp.Payload;
/*    */ import core.shell.ShellEntity;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
/*    */ import java.util.Arrays;
/*    */ import util.functions;
/*    */ import util.http.ReqParameter;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UpdatePayloadCacheHandler
/*    */   extends PayloadCacheHandler
/*    */ {
/*    */   public UpdatePayloadCacheHandler(ShellEntity entity, Payload payload) {
/* 18 */     super(entity, payload);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] evalFunc(byte[] realResult, String className, String funcName, ReqParameter parameter) {
/* 24 */     if (className == null && funcName != null && realResult != null && realResult.length > 0) {
/*    */       try {
/* 26 */         StackTraceElement[] stack = Thread.currentThread().getStackTrace();
/* 27 */         String methodName = stack[3].getMethodName();
/* 28 */         if (Arrays.binarySearch((Object[])blackMethod, methodName) < 0) {
/* 29 */           if ("downloadFile".equals(methodName)) {
/* 30 */             synchronized (this.rc4) {
/* 31 */               File file = new File(this.currentDirectory + functions.byteArrayToHex(functions.md5(parameter.getParameterByteArray("fileName"))));
/* 32 */               try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
/* 33 */                 fileOutputStream.write(this.rc4.encryptMessage(functions.gzipE(realResult), this.shellId));
/*    */               } 
/*    */             } 
/*    */           } else {
/* 37 */             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/* 38 */             byteArrayOutputStream.write(funcName.getBytes());
/* 39 */             byteArrayOutputStream.write(parameter.formatEx());
/* 40 */             this.cacheDb.updateSetingKV(functions.byteArrayToHex(functions.md5(byteArrayOutputStream.toByteArray())), functions.gzipE(realResult));
/*    */           } 
/*    */         }
/* 43 */       } catch (Exception e) {
/* 44 */         e.printStackTrace();
/*    */       } 
/*    */     }
/*    */     
/* 48 */     return realResult;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\shell\cache\UpdatePayloadCacheHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */