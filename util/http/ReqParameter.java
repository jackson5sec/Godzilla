/*    */ package util.http;
/*    */ 
/*    */ import core.ui.component.dialog.ShellSuperRequest;
/*    */ import java.util.Iterator;
/*    */ import util.functions;
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
/*    */ public class ReqParameter
/*    */   extends Parameter
/*    */ {
/*    */   public String format() {
/* 27 */     String randomRP = ShellSuperRequest.randomReqParameter();
/* 28 */     if (randomRP != null && randomRP.length() > 1) {
/* 29 */       add(functions.getRandomString(5), randomRP);
/*    */     }
/*    */ 
/*    */     
/* 33 */     Iterator<String> keys = this.hashMap.keySet().iterator();
/* 34 */     StringBuffer buffer = new StringBuffer();
/*    */ 
/*    */ 
/*    */     
/* 38 */     while (keys.hasNext()) {
/* 39 */       String key = keys.next();
/* 40 */       buffer.append(key);
/* 41 */       buffer.append("=");
/* 42 */       Object valueObject = this.hashMap.get(key);
/* 43 */       if (valueObject.getClass().isAssignableFrom(byte[].class)) {
/* 44 */         buffer.append(functions.base64EncodeToString((byte[])valueObject));
/*    */       } else {
/* 46 */         buffer.append(functions.base64EncodeToString(((String)valueObject).getBytes()));
/*    */       } 
/* 48 */       buffer.append("&");
/*    */     } 
/* 50 */     String temString = buffer.delete(buffer.length() - 1, buffer.length()).toString();
/* 51 */     return temString;
/*    */   }
/*    */   
/*    */   public byte[] formatEx() {
/* 55 */     return serialize();
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar\\util\http\ReqParameter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */