/*     */ package com.httpProxy.server;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.InputStream;
/*     */ import java.math.BigInteger;
/*     */ import java.net.URI;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Paths;
/*     */ import java.security.KeyFactory;
/*     */ import java.security.KeyPair;
/*     */ import java.security.KeyPairGenerator;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.security.Security;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.security.spec.EncodedKeySpec;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import java.security.spec.X509EncodedKeySpec;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import org.bouncycastle.asn1.ASN1Encodable;
/*     */ import org.bouncycastle.asn1.x500.X500Name;
/*     */ import org.bouncycastle.asn1.x509.Extension;
/*     */ import org.bouncycastle.asn1.x509.GeneralName;
/*     */ import org.bouncycastle.asn1.x509.GeneralNames;
/*     */ import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
/*     */ import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
/*     */ import org.bouncycastle.operator.ContentSigner;
/*     */ import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
/*     */ 
/*     */ public class CertUtil {
/*  37 */   private static KeyFactory keyFactory = null;
/*     */ 
/*     */   
/*     */   static {
/*  41 */     Security.addProvider((Provider)new BouncyCastleProvider());
/*     */   }
/*     */   
/*     */   private static KeyFactory getKeyFactory() throws NoSuchAlgorithmException {
/*  45 */     if (keyFactory == null) {
/*  46 */       keyFactory = KeyFactory.getInstance("RSA");
/*     */     }
/*  48 */     return keyFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static KeyPair genKeyPair() throws Exception {
/*  55 */     KeyPairGenerator caKeyPairGen = KeyPairGenerator.getInstance("RSA", "BC");
/*  56 */     caKeyPairGen.initialize(2048, new SecureRandom());
/*  57 */     return caKeyPairGen.genKeyPair();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PrivateKey loadPriKey(byte[] bts) throws NoSuchAlgorithmException, InvalidKeySpecException {
/*  66 */     EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bts);
/*  67 */     return getKeyFactory().generatePrivate(privateKeySpec);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PrivateKey loadPriKey(String path) throws Exception {
/*  75 */     return loadPriKey(Files.readAllBytes(Paths.get(path, new String[0])));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PrivateKey loadPriKey(URI uri) throws Exception {
/*  83 */     return loadPriKey(Paths.get(uri).toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PrivateKey loadPriKey(InputStream inputStream) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
/*  92 */     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
/*  93 */     byte[] bts = new byte[1024];
/*     */     int len;
/*  95 */     while ((len = inputStream.read(bts)) != -1) {
/*  96 */       outputStream.write(bts, 0, len);
/*     */     }
/*  98 */     inputStream.close();
/*  99 */     outputStream.close();
/* 100 */     return loadPriKey(outputStream.toByteArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PublicKey loadPubKey(byte[] bts) throws Exception {
/* 107 */     EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bts);
/* 108 */     return getKeyFactory().generatePublic(publicKeySpec);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PublicKey loadPubKey(String path) throws Exception {
/* 115 */     EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Files.readAllBytes(Paths.get(path, new String[0])));
/* 116 */     return getKeyFactory().generatePublic(publicKeySpec);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PublicKey loadPubKey(URI uri) throws Exception {
/* 123 */     return loadPubKey(Paths.get(uri).toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static PublicKey loadPubKey(InputStream inputStream) throws Exception {
/* 130 */     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
/* 131 */     byte[] bts = new byte[1024];
/*     */     int len;
/* 133 */     while ((len = inputStream.read(bts)) != -1) {
/* 134 */       outputStream.write(bts, 0, len);
/*     */     }
/* 136 */     inputStream.close();
/* 137 */     outputStream.close();
/* 138 */     return loadPubKey(outputStream.toByteArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static X509Certificate loadCert(InputStream inputStream) throws CertificateException {
/* 145 */     CertificateFactory cf = CertificateFactory.getInstance("X.509");
/* 146 */     return (X509Certificate)cf.generateCertificate(inputStream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static X509Certificate loadCert(String path) throws Exception {
/* 153 */     return loadCert(new FileInputStream(path));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static X509Certificate loadCert(URI uri) throws Exception {
/* 160 */     return loadCert(Paths.get(uri).toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getSubject(InputStream inputStream) throws Exception {
/* 167 */     X509Certificate certificate = loadCert(inputStream);
/*     */     
/* 169 */     List<String> tempList = Arrays.asList(certificate.getIssuerDN().toString().split(", "));
/* 170 */     return IntStream.rangeClosed(0, tempList.size() - 1)
/* 171 */       .<CharSequence>mapToObj(i -> (String)tempList.get(tempList.size() - i - 1)).collect(Collectors.joining(", "));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getSubject(X509Certificate certificate) throws Exception {
/* 179 */     List<String> tempList = Arrays.asList(certificate.getIssuerDN().toString().split(", "));
/* 180 */     return IntStream.rangeClosed(0, tempList.size() - 1)
/* 181 */       .<CharSequence>mapToObj(i -> (String)tempList.get(tempList.size() - i - 1)).collect(Collectors.joining(", "));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static X509Certificate genCert(String issuer, PrivateKey caPriKey, Date caNotBefore, Date caNotAfter, PublicKey serverPubKey, String... hosts) throws Exception {
/* 202 */     String subject = Stream.<String>of(issuer.split(", ")).map(item -> { String[] arr = item.split("="); return "CN".equals(arr[0]) ? ("CN=" + hosts[0]) : item; }).collect(Collectors.joining(", "));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 207 */     JcaX509v3CertificateBuilder jv3Builder = new JcaX509v3CertificateBuilder(new X500Name(issuer), BigInteger.valueOf(System.currentTimeMillis() + (long)(Math.random() * 10000.0D) + 1000L), caNotBefore, caNotAfter, new X500Name(subject), serverPubKey);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 213 */     GeneralName[] generalNames = new GeneralName[hosts.length];
/* 214 */     for (int i = 0; i < hosts.length; i++) {
/* 215 */       generalNames[i] = new GeneralName(2, hosts[i]);
/*     */     }
/* 217 */     GeneralNames subjectAltName = new GeneralNames(generalNames);
/* 218 */     jv3Builder.addExtension(Extension.subjectAlternativeName, false, (ASN1Encodable)subjectAltName);
/*     */     
/* 220 */     ContentSigner signer = (new JcaContentSignerBuilder("SHA256WithRSAEncryption")).build(caPriKey);
/* 221 */     return (new JcaX509CertificateConverter()).getCertificate(jv3Builder.build(signer));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static X509Certificate genCACert(String subject, Date caNotBefore, Date caNotAfter, KeyPair keyPair) throws Exception {
/* 234 */     JcaX509v3CertificateBuilder jv3Builder = new JcaX509v3CertificateBuilder(new X500Name(subject), BigInteger.valueOf(System.currentTimeMillis() + (long)(Math.random() * 10000.0D) + 1000L), caNotBefore, caNotAfter, new X500Name(subject), keyPair.getPublic());
/* 235 */     jv3Builder.addExtension(Extension.basicConstraints, true, (ASN1Encodable)new BasicConstraints(0));
/*     */     
/* 237 */     ContentSigner signer = (new JcaContentSignerBuilder("SHA256WithRSAEncryption")).build(keyPair.getPrivate());
/* 238 */     return (new JcaX509CertificateConverter()).getCertificate(jv3Builder.build(signer));
/*     */   }
/*     */ }


/* Location:              C:\User\\user\Downloads\godzilla.jar!\com\httpProxy\server\CertUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */