/*    */ package core.c2profile.cryption;
/*    */ 
/*    */ import java.net.URLDecoder;
/*    */ import java.net.URLEncoder;
/*    */ import util.functions;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CryptionContext
/*    */ {
/*    */   public static String urlEncode(String str) {
/* 12 */     return URLEncoder.encode(str);
/*    */   }
/*    */   public static String urlDecode(String str) {
/* 15 */     return URLDecoder.decode(str);
/*    */   }
/*    */   public static String urlEncode(byte[] bytes) {
/* 18 */     return functions.byteArrayToHexPrefix(bytes, "%");
/*    */   }
/*    */   public static byte[] base64Encode(byte[] bytes) {
/* 21 */     return functions.base64Encode(bytes);
/*    */   }
/*    */   public static String base64EncodeToString(byte[] bytes) {
/* 24 */     return functions.base64EncodeToString(bytes);
/*    */   }
/*    */   public static String base64EncodeUrl(byte[] bytes) {
/* 27 */     return urlEncode(base64EncodeToString(bytes));
/*    */   }
/*    */   public static String md5(String str) {
/* 30 */     return functions.md5(str);
/*    */   }
/*    */   public static byte[] md5(byte[] bytes) {
/* 33 */     return functions.md5(bytes);
/*    */   }
/*    */   public static String hex(byte[] bytes) {
/* 36 */     return functions.byteArrayToHex(bytes);
/*    */   }
/*    */   public static byte[] unHex(String hexStr) {
/* 39 */     return functions.hexToByte(hexStr);
/*    */   }
/*    */   public static byte[] aes128(byte[] key, byte[] data) {
/* 42 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\core\c2profile\cryption\CryptionContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */