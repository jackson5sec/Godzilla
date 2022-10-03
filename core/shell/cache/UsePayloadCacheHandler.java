/*    */ package core.shell.cache;
/*    */ 
/*    */ import core.imp.Payload;
/*    */ import core.shell.ShellEntity;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.util.Arrays;
/*    */ import util.Log;
/*    */ import util.functions;
/*    */ import util.http.ReqParameter;
/*    */ 
/*    */ public class UsePayloadCacheHandler
/*    */   extends PayloadCacheHandler
/*    */ {
/*    */   public UsePayloadCacheHandler(ShellEntity entity, Payload payload) {
/* 17 */     super(entity, payload);
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] evalFunc(byte[] realResult, String className, String funcName, ReqParameter parameter) {
/* 22 */     if (className == null && funcName != null) {
/*    */       try {
/* 24 */         StackTraceElement[] stack = Thread.currentThread().getStackTrace();
/* 25 */         String methodName = stack[3].getMethodName();
/* 26 */         if (Arrays.binarySearch((Object[])blackMethod, methodName) < 0) {
/* 27 */           if ("downloadFile".equals(methodName)) {
/* 28 */             synchronized (this.rc4) {
/* 29 */               byte[] arrayOfByte; File file = new File(this.currentDirectory + functions.byteArrayToHex(functions.md5(parameter.getParameterByteArray("fileName"))));
/*    */               
/* 31 */               try (FileInputStream fileInputStream = new FileInputStream(file)) {
/* 32 */                 arrayOfByte = functions.gzipD(this.rc4.decryptMessage(functions.readInputStream(fileInputStream), this.shellId));
/* 33 */               } catch (Throwable e) {
/* 34 */                 return "The cache file does not exist".getBytes();
/*    */               } 
/* 36 */               return (arrayOfByte == null) ? new byte[0] : arrayOfByte;
/*    */             } 
/*    */           }
/* 39 */           this.payload.fillParameter(className, funcName, parameter);
/* 40 */           ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/* 41 */           byteArrayOutputStream.write(funcName.getBytes());
/* 42 */           byteArrayOutputStream.write(parameter.formatEx());
/* 43 */           byte[] ret = this.cacheDb.getSetingValue(functions.byteArrayToHex(functions.md5(byteArrayOutputStream.toByteArray())));
/* 44 */           return (ret == null) ? "The operation has no cache".getBytes() : functions.gzipD(ret);
/*    */         }
/*    */       
/* 47 */       } catch (Exception e) {
/* 48 */         Log.error(e);
/*    */       } 
/*    */     }
/*    */     
/* 52 */     return "Payload does not cache the plugin return".getBytes();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\shell\cache\UsePayloadCacheHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */