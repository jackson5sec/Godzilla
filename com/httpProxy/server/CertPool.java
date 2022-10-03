/*    */ package com.httpProxy.server;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.security.KeyPair;
/*    */ import java.security.KeyStore;
/*    */ import java.security.PrivateKey;
/*    */ import java.security.cert.Certificate;
/*    */ import java.security.cert.X509Certificate;
/*    */ import java.util.Map;
/*    */ import java.util.WeakHashMap;
/*    */ import javax.net.ssl.KeyManagerFactory;
/*    */ import javax.net.ssl.SSLContext;
/*    */ 
/*    */ public class CertPool {
/*    */   private PrivateKey privateKey;
/*    */   private X509Certificate ca;
/*    */   private KeyPair keyPair;
/* 18 */   Map<String, X509Certificate> cacehep = new WeakHashMap<>();
/*    */   
/*    */   public CertPool(PrivateKey privateKey, X509Certificate ca) {
/* 21 */     this.privateKey = privateKey;
/* 22 */     this.ca = ca;
/*    */     try {
/* 24 */       this.keyPair = CertUtil.genKeyPair();
/* 25 */     } catch (Exception e) {
/* 26 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public SSLContext getSslContext(String host) {
/* 32 */     int index = -1;
/* 33 */     if ((index = host.indexOf(":")) != -1) {
/* 34 */       host = host.substring(0, index);
/*    */     }
/*    */     
/* 37 */     X509Certificate x509Certificate = null;
/*    */     try {
/* 39 */       x509Certificate = this.cacehep.get(host);
/* 40 */       if (x509Certificate == null) {
/* 41 */         x509Certificate = CertUtil.genCert(CertUtil.getSubject(this.ca), this.privateKey, this.ca
/* 42 */             .getNotBefore(), this.ca.getNotAfter(), this.keyPair
/* 43 */             .getPublic(), new String[] { host });
/* 44 */         this.cacehep.put(host, x509Certificate);
/*    */       } 
/* 46 */     } catch (Exception e) {
/* 47 */       e.printStackTrace();
/*    */     } 
/* 49 */     return getSslContext(this.keyPair.getPrivate(), new X509Certificate[] { x509Certificate });
/*    */   }
/*    */ 
/*    */   
/*    */   private SSLContext getSslContext(PrivateKey key, X509Certificate... keyCertChain) {
/*    */     try {
/* 55 */       KeyStore ks = KeyStore.getInstance("jks");
/* 56 */       ks.load((InputStream)null, (char[])null);
/*    */       
/* 58 */       ks.setKeyEntry("key", key, new char[0], (Certificate[])keyCertChain);
/*    */       
/* 60 */       KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
/* 61 */       kmf.init(ks, new char[0]);
/*    */       
/* 63 */       SSLContext ctx = SSLContext.getInstance("TLS");
/*    */       
/* 65 */       ctx.init(kmf.getKeyManagers(), null, null);
/*    */ 
/*    */       
/* 68 */       return ctx;
/* 69 */     } catch (Exception e) {
/* 70 */       e.printStackTrace();
/* 71 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\httpProxy\server\CertPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */