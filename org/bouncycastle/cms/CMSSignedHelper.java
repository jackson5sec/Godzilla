package org.bouncycastle.cms;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.cms.OtherRevocationInfoFormat;
import org.bouncycastle.asn1.cryptopro.CryptoProObjectIdentifiers;
import org.bouncycastle.asn1.eac.EACObjectIdentifiers;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.rosstandart.RosstandartObjectIdentifiers;
import org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.cert.X509AttributeCertificateHolder;
import org.bouncycastle.cert.X509CRLHolder;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.util.CollectionStore;
import org.bouncycastle.util.Store;

class CMSSignedHelper {
  static final CMSSignedHelper INSTANCE = new CMSSignedHelper();
  
  private static final Map encryptionAlgs = new HashMap<Object, Object>();
  
  private static final Map digestAlgs = new HashMap<Object, Object>();
  
  private static final Map digestAliases = new HashMap<Object, Object>();
  
  private static void addEntries(ASN1ObjectIdentifier paramASN1ObjectIdentifier, String paramString1, String paramString2) {
    digestAlgs.put(paramASN1ObjectIdentifier.getId(), paramString1);
    encryptionAlgs.put(paramASN1ObjectIdentifier.getId(), paramString2);
  }
  
  String getEncryptionAlgName(String paramString) {
    String str = (String)encryptionAlgs.get(paramString);
    return (str != null) ? str : paramString;
  }
  
  AlgorithmIdentifier fixAlgID(AlgorithmIdentifier paramAlgorithmIdentifier) {
    return (paramAlgorithmIdentifier.getParameters() == null) ? new AlgorithmIdentifier(paramAlgorithmIdentifier.getAlgorithm(), (ASN1Encodable)DERNull.INSTANCE) : paramAlgorithmIdentifier;
  }
  
  void setSigningEncryptionAlgorithmMapping(ASN1ObjectIdentifier paramASN1ObjectIdentifier, String paramString) {
    encryptionAlgs.put(paramASN1ObjectIdentifier.getId(), paramString);
  }
  
  void setSigningDigestAlgorithmMapping(ASN1ObjectIdentifier paramASN1ObjectIdentifier, String paramString) {
    digestAlgs.put(paramASN1ObjectIdentifier.getId(), paramString);
  }
  
  Store getCertificates(ASN1Set paramASN1Set) {
    if (paramASN1Set != null) {
      ArrayList<X509CertificateHolder> arrayList = new ArrayList(paramASN1Set.size());
      Enumeration<ASN1Encodable> enumeration = paramASN1Set.getObjects();
      while (enumeration.hasMoreElements()) {
        ASN1Primitive aSN1Primitive = ((ASN1Encodable)enumeration.nextElement()).toASN1Primitive();
        if (aSN1Primitive instanceof org.bouncycastle.asn1.ASN1Sequence)
          arrayList.add(new X509CertificateHolder(Certificate.getInstance(aSN1Primitive))); 
      } 
      return (Store)new CollectionStore(arrayList);
    } 
    return (Store)new CollectionStore(new ArrayList());
  }
  
  Store getAttributeCertificates(ASN1Set paramASN1Set) {
    if (paramASN1Set != null) {
      ArrayList<X509AttributeCertificateHolder> arrayList = new ArrayList(paramASN1Set.size());
      Enumeration<ASN1Encodable> enumeration = paramASN1Set.getObjects();
      while (enumeration.hasMoreElements()) {
        ASN1Primitive aSN1Primitive = ((ASN1Encodable)enumeration.nextElement()).toASN1Primitive();
        if (aSN1Primitive instanceof ASN1TaggedObject)
          arrayList.add(new X509AttributeCertificateHolder(AttributeCertificate.getInstance(((ASN1TaggedObject)aSN1Primitive).getObject()))); 
      } 
      return (Store)new CollectionStore(arrayList);
    } 
    return (Store)new CollectionStore(new ArrayList());
  }
  
  Store getCRLs(ASN1Set paramASN1Set) {
    if (paramASN1Set != null) {
      ArrayList<X509CRLHolder> arrayList = new ArrayList(paramASN1Set.size());
      Enumeration<ASN1Encodable> enumeration = paramASN1Set.getObjects();
      while (enumeration.hasMoreElements()) {
        ASN1Primitive aSN1Primitive = ((ASN1Encodable)enumeration.nextElement()).toASN1Primitive();
        if (aSN1Primitive instanceof org.bouncycastle.asn1.ASN1Sequence)
          arrayList.add(new X509CRLHolder(CertificateList.getInstance(aSN1Primitive))); 
      } 
      return (Store)new CollectionStore(arrayList);
    } 
    return (Store)new CollectionStore(new ArrayList());
  }
  
  Store getOtherRevocationInfo(ASN1ObjectIdentifier paramASN1ObjectIdentifier, ASN1Set paramASN1Set) {
    if (paramASN1Set != null) {
      ArrayList<ASN1Encodable> arrayList = new ArrayList(paramASN1Set.size());
      Enumeration<ASN1Encodable> enumeration = paramASN1Set.getObjects();
      while (enumeration.hasMoreElements()) {
        ASN1Primitive aSN1Primitive = ((ASN1Encodable)enumeration.nextElement()).toASN1Primitive();
        if (aSN1Primitive instanceof ASN1TaggedObject) {
          ASN1TaggedObject aSN1TaggedObject = ASN1TaggedObject.getInstance(aSN1Primitive);
          if (aSN1TaggedObject.getTagNo() == 1) {
            OtherRevocationInfoFormat otherRevocationInfoFormat = OtherRevocationInfoFormat.getInstance(aSN1TaggedObject, false);
            if (paramASN1ObjectIdentifier.equals(otherRevocationInfoFormat.getInfoFormat()))
              arrayList.add(otherRevocationInfoFormat.getInfo()); 
          } 
        } 
      } 
      return (Store)new CollectionStore(arrayList);
    } 
    return (Store)new CollectionStore(new ArrayList());
  }
  
  static {
    addEntries(NISTObjectIdentifiers.dsa_with_sha224, "SHA224", "DSA");
    addEntries(NISTObjectIdentifiers.dsa_with_sha256, "SHA256", "DSA");
    addEntries(NISTObjectIdentifiers.dsa_with_sha384, "SHA384", "DSA");
    addEntries(NISTObjectIdentifiers.dsa_with_sha512, "SHA512", "DSA");
    addEntries(OIWObjectIdentifiers.dsaWithSHA1, "SHA1", "DSA");
    addEntries(OIWObjectIdentifiers.md4WithRSA, "MD4", "RSA");
    addEntries(OIWObjectIdentifiers.md4WithRSAEncryption, "MD4", "RSA");
    addEntries(OIWObjectIdentifiers.md5WithRSA, "MD5", "RSA");
    addEntries(OIWObjectIdentifiers.sha1WithRSA, "SHA1", "RSA");
    addEntries(PKCSObjectIdentifiers.md2WithRSAEncryption, "MD2", "RSA");
    addEntries(PKCSObjectIdentifiers.md4WithRSAEncryption, "MD4", "RSA");
    addEntries(PKCSObjectIdentifiers.md5WithRSAEncryption, "MD5", "RSA");
    addEntries(PKCSObjectIdentifiers.sha1WithRSAEncryption, "SHA1", "RSA");
    addEntries(PKCSObjectIdentifiers.sha224WithRSAEncryption, "SHA224", "RSA");
    addEntries(PKCSObjectIdentifiers.sha256WithRSAEncryption, "SHA256", "RSA");
    addEntries(PKCSObjectIdentifiers.sha384WithRSAEncryption, "SHA384", "RSA");
    addEntries(PKCSObjectIdentifiers.sha512WithRSAEncryption, "SHA512", "RSA");
    addEntries(X9ObjectIdentifiers.ecdsa_with_SHA1, "SHA1", "ECDSA");
    addEntries(X9ObjectIdentifiers.ecdsa_with_SHA224, "SHA224", "ECDSA");
    addEntries(X9ObjectIdentifiers.ecdsa_with_SHA256, "SHA256", "ECDSA");
    addEntries(X9ObjectIdentifiers.ecdsa_with_SHA384, "SHA384", "ECDSA");
    addEntries(X9ObjectIdentifiers.ecdsa_with_SHA512, "SHA512", "ECDSA");
    addEntries(X9ObjectIdentifiers.id_dsa_with_sha1, "SHA1", "DSA");
    addEntries(EACObjectIdentifiers.id_TA_ECDSA_SHA_1, "SHA1", "ECDSA");
    addEntries(EACObjectIdentifiers.id_TA_ECDSA_SHA_224, "SHA224", "ECDSA");
    addEntries(EACObjectIdentifiers.id_TA_ECDSA_SHA_256, "SHA256", "ECDSA");
    addEntries(EACObjectIdentifiers.id_TA_ECDSA_SHA_384, "SHA384", "ECDSA");
    addEntries(EACObjectIdentifiers.id_TA_ECDSA_SHA_512, "SHA512", "ECDSA");
    addEntries(EACObjectIdentifiers.id_TA_RSA_v1_5_SHA_1, "SHA1", "RSA");
    addEntries(EACObjectIdentifiers.id_TA_RSA_v1_5_SHA_256, "SHA256", "RSA");
    addEntries(EACObjectIdentifiers.id_TA_RSA_PSS_SHA_1, "SHA1", "RSAandMGF1");
    addEntries(EACObjectIdentifiers.id_TA_RSA_PSS_SHA_256, "SHA256", "RSAandMGF1");
    encryptionAlgs.put(X9ObjectIdentifiers.id_dsa.getId(), "DSA");
    encryptionAlgs.put(PKCSObjectIdentifiers.rsaEncryption.getId(), "RSA");
    encryptionAlgs.put(TeleTrusTObjectIdentifiers.teleTrusTRSAsignatureAlgorithm, "RSA");
    encryptionAlgs.put(X509ObjectIdentifiers.id_ea_rsa.getId(), "RSA");
    encryptionAlgs.put(CMSSignedDataGenerator.ENCRYPTION_RSA_PSS, "RSAandMGF1");
    encryptionAlgs.put(CryptoProObjectIdentifiers.gostR3410_94.getId(), "GOST3410");
    encryptionAlgs.put(CryptoProObjectIdentifiers.gostR3410_2001.getId(), "ECGOST3410");
    encryptionAlgs.put("1.3.6.1.4.1.5849.1.6.2", "ECGOST3410");
    encryptionAlgs.put("1.3.6.1.4.1.5849.1.1.5", "GOST3410");
    encryptionAlgs.put(RosstandartObjectIdentifiers.id_tc26_gost_3410_12_256, "ECGOST3410-2012-256");
    encryptionAlgs.put(RosstandartObjectIdentifiers.id_tc26_gost_3410_12_512, "ECGOST3410-2012-512");
    encryptionAlgs.put(CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_2001.getId(), "ECGOST3410");
    encryptionAlgs.put(CryptoProObjectIdentifiers.gostR3411_94_with_gostR3410_94.getId(), "GOST3410");
    encryptionAlgs.put(RosstandartObjectIdentifiers.id_tc26_signwithdigest_gost_3410_12_256, "ECGOST3410-2012-256");
    encryptionAlgs.put(RosstandartObjectIdentifiers.id_tc26_signwithdigest_gost_3410_12_512, "ECGOST3410-2012-512");
    digestAlgs.put(PKCSObjectIdentifiers.md2.getId(), "MD2");
    digestAlgs.put(PKCSObjectIdentifiers.md4.getId(), "MD4");
    digestAlgs.put(PKCSObjectIdentifiers.md5.getId(), "MD5");
    digestAlgs.put(OIWObjectIdentifiers.idSHA1.getId(), "SHA1");
    digestAlgs.put(NISTObjectIdentifiers.id_sha224.getId(), "SHA224");
    digestAlgs.put(NISTObjectIdentifiers.id_sha256.getId(), "SHA256");
    digestAlgs.put(NISTObjectIdentifiers.id_sha384.getId(), "SHA384");
    digestAlgs.put(NISTObjectIdentifiers.id_sha512.getId(), "SHA512");
    digestAlgs.put(TeleTrusTObjectIdentifiers.ripemd128.getId(), "RIPEMD128");
    digestAlgs.put(TeleTrusTObjectIdentifiers.ripemd160.getId(), "RIPEMD160");
    digestAlgs.put(TeleTrusTObjectIdentifiers.ripemd256.getId(), "RIPEMD256");
    digestAlgs.put(CryptoProObjectIdentifiers.gostR3411.getId(), "GOST3411");
    digestAlgs.put("1.3.6.1.4.1.5849.1.2.1", "GOST3411");
    digestAlgs.put(RosstandartObjectIdentifiers.id_tc26_gost_3411_12_256, "GOST3411-2012-256");
    digestAlgs.put(RosstandartObjectIdentifiers.id_tc26_gost_3411_12_512, "GOST3411-2012-512");
    digestAliases.put("SHA1", new String[] { "SHA-1" });
    digestAliases.put("SHA224", new String[] { "SHA-224" });
    digestAliases.put("SHA256", new String[] { "SHA-256" });
    digestAliases.put("SHA384", new String[] { "SHA-384" });
    digestAliases.put("SHA512", new String[] { "SHA-512" });
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cms\CMSSignedHelper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */