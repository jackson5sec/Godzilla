package org.bouncycastle.jcajce.provider.asymmetric;

import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.asn1.bsi.BSIObjectIdentifiers;
import org.bouncycastle.asn1.eac.EACObjectIdentifiers;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.sec.SECObjectIdentifiers;
import org.bouncycastle.asn1.teletrust.TeleTrusTObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;
import org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi;
import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.util.AsymmetricAlgorithmProvider;
import org.bouncycastle.jcajce.provider.util.AsymmetricKeyInfoConverter;
import org.bouncycastle.util.Properties;

public class EC {
  private static final String PREFIX = "org.bouncycastle.jcajce.provider.asymmetric.ec.";
  
  private static final Map<String, String> generalEcAttributes = new HashMap<String, String>();
  
  static {
    generalEcAttributes.put("SupportedKeyClasses", "java.security.interfaces.ECPublicKey|java.security.interfaces.ECPrivateKey");
    generalEcAttributes.put("SupportedKeyFormats", "PKCS#8|X.509");
  }
  
  public static class Mappings extends AsymmetricAlgorithmProvider {
    public void configure(ConfigurableProvider param1ConfigurableProvider) {
      param1ConfigurableProvider.addAlgorithm("AlgorithmParameters.EC", "org.bouncycastle.jcajce.provider.asymmetric.ec.AlgorithmParametersSpi");
      param1ConfigurableProvider.addAttributes("KeyAgreement.ECDH", EC.generalEcAttributes);
      param1ConfigurableProvider.addAlgorithm("KeyAgreement.ECDH", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$DH");
      param1ConfigurableProvider.addAttributes("KeyAgreement.ECDHC", EC.generalEcAttributes);
      param1ConfigurableProvider.addAlgorithm("KeyAgreement.ECDHC", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$DHC");
      param1ConfigurableProvider.addAttributes("KeyAgreement.ECCDH", EC.generalEcAttributes);
      param1ConfigurableProvider.addAlgorithm("KeyAgreement.ECCDH", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$DHC");
      param1ConfigurableProvider.addAlgorithm("KeyAgreement." + X9ObjectIdentifiers.dhSinglePass_stdDH_sha1kdf_scheme, "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$DHwithSHA1KDFAndSharedInfo");
      param1ConfigurableProvider.addAlgorithm("KeyAgreement." + X9ObjectIdentifiers.dhSinglePass_cofactorDH_sha1kdf_scheme, "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$CDHwithSHA1KDFAndSharedInfo");
      param1ConfigurableProvider.addAlgorithm("KeyAgreement." + SECObjectIdentifiers.dhSinglePass_stdDH_sha224kdf_scheme, "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$DHwithSHA224KDFAndSharedInfo");
      param1ConfigurableProvider.addAlgorithm("KeyAgreement." + SECObjectIdentifiers.dhSinglePass_cofactorDH_sha224kdf_scheme, "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$CDHwithSHA224KDFAndSharedInfo");
      param1ConfigurableProvider.addAlgorithm("KeyAgreement." + SECObjectIdentifiers.dhSinglePass_stdDH_sha256kdf_scheme, "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$DHwithSHA256KDFAndSharedInfo");
      param1ConfigurableProvider.addAlgorithm("KeyAgreement." + SECObjectIdentifiers.dhSinglePass_cofactorDH_sha256kdf_scheme, "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$CDHwithSHA256KDFAndSharedInfo");
      param1ConfigurableProvider.addAlgorithm("KeyAgreement." + SECObjectIdentifiers.dhSinglePass_stdDH_sha384kdf_scheme, "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$DHwithSHA384KDFAndSharedInfo");
      param1ConfigurableProvider.addAlgorithm("KeyAgreement." + SECObjectIdentifiers.dhSinglePass_cofactorDH_sha384kdf_scheme, "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$CDHwithSHA384KDFAndSharedInfo");
      param1ConfigurableProvider.addAlgorithm("KeyAgreement." + SECObjectIdentifiers.dhSinglePass_stdDH_sha512kdf_scheme, "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$DHwithSHA512KDFAndSharedInfo");
      param1ConfigurableProvider.addAlgorithm("KeyAgreement." + SECObjectIdentifiers.dhSinglePass_cofactorDH_sha512kdf_scheme, "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$CDHwithSHA512KDFAndSharedInfo");
      param1ConfigurableProvider.addAlgorithm("KeyAgreement.ECDHWITHSHA1KDF", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$DHwithSHA1KDF");
      param1ConfigurableProvider.addAlgorithm("KeyAgreement.ECCDHWITHSHA1CKDF", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$DHwithSHA1CKDF");
      param1ConfigurableProvider.addAlgorithm("KeyAgreement.ECCDHWITHSHA256CKDF", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$DHwithSHA256CKDF");
      param1ConfigurableProvider.addAlgorithm("KeyAgreement.ECCDHWITHSHA384CKDF", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$DHwithSHA384CKDF");
      param1ConfigurableProvider.addAlgorithm("KeyAgreement.ECCDHWITHSHA512CKDF", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$DHwithSHA512CKDF");
      registerOid(param1ConfigurableProvider, X9ObjectIdentifiers.id_ecPublicKey, "EC", (AsymmetricKeyInfoConverter)new KeyFactorySpi.EC());
      registerOid(param1ConfigurableProvider, X9ObjectIdentifiers.dhSinglePass_cofactorDH_sha1kdf_scheme, "EC", (AsymmetricKeyInfoConverter)new KeyFactorySpi.EC());
      registerOid(param1ConfigurableProvider, X9ObjectIdentifiers.mqvSinglePass_sha1kdf_scheme, "ECMQV", (AsymmetricKeyInfoConverter)new KeyFactorySpi.ECMQV());
      registerOid(param1ConfigurableProvider, SECObjectIdentifiers.dhSinglePass_stdDH_sha224kdf_scheme, "EC", (AsymmetricKeyInfoConverter)new KeyFactorySpi.EC());
      registerOid(param1ConfigurableProvider, SECObjectIdentifiers.dhSinglePass_cofactorDH_sha224kdf_scheme, "EC", (AsymmetricKeyInfoConverter)new KeyFactorySpi.EC());
      registerOid(param1ConfigurableProvider, SECObjectIdentifiers.dhSinglePass_stdDH_sha256kdf_scheme, "EC", (AsymmetricKeyInfoConverter)new KeyFactorySpi.EC());
      registerOid(param1ConfigurableProvider, SECObjectIdentifiers.dhSinglePass_cofactorDH_sha256kdf_scheme, "EC", (AsymmetricKeyInfoConverter)new KeyFactorySpi.EC());
      registerOid(param1ConfigurableProvider, SECObjectIdentifiers.dhSinglePass_stdDH_sha384kdf_scheme, "EC", (AsymmetricKeyInfoConverter)new KeyFactorySpi.EC());
      registerOid(param1ConfigurableProvider, SECObjectIdentifiers.dhSinglePass_cofactorDH_sha384kdf_scheme, "EC", (AsymmetricKeyInfoConverter)new KeyFactorySpi.EC());
      registerOid(param1ConfigurableProvider, SECObjectIdentifiers.dhSinglePass_stdDH_sha512kdf_scheme, "EC", (AsymmetricKeyInfoConverter)new KeyFactorySpi.EC());
      registerOid(param1ConfigurableProvider, SECObjectIdentifiers.dhSinglePass_cofactorDH_sha512kdf_scheme, "EC", (AsymmetricKeyInfoConverter)new KeyFactorySpi.EC());
      registerOidAlgorithmParameters(param1ConfigurableProvider, X9ObjectIdentifiers.id_ecPublicKey, "EC");
      registerOidAlgorithmParameters(param1ConfigurableProvider, X9ObjectIdentifiers.dhSinglePass_stdDH_sha1kdf_scheme, "EC");
      registerOidAlgorithmParameters(param1ConfigurableProvider, X9ObjectIdentifiers.dhSinglePass_cofactorDH_sha1kdf_scheme, "EC");
      registerOidAlgorithmParameters(param1ConfigurableProvider, SECObjectIdentifiers.dhSinglePass_stdDH_sha224kdf_scheme, "EC");
      registerOidAlgorithmParameters(param1ConfigurableProvider, SECObjectIdentifiers.dhSinglePass_cofactorDH_sha224kdf_scheme, "EC");
      registerOidAlgorithmParameters(param1ConfigurableProvider, SECObjectIdentifiers.dhSinglePass_stdDH_sha256kdf_scheme, "EC");
      registerOidAlgorithmParameters(param1ConfigurableProvider, SECObjectIdentifiers.dhSinglePass_cofactorDH_sha256kdf_scheme, "EC");
      registerOidAlgorithmParameters(param1ConfigurableProvider, SECObjectIdentifiers.dhSinglePass_stdDH_sha384kdf_scheme, "EC");
      registerOidAlgorithmParameters(param1ConfigurableProvider, SECObjectIdentifiers.dhSinglePass_cofactorDH_sha384kdf_scheme, "EC");
      registerOidAlgorithmParameters(param1ConfigurableProvider, SECObjectIdentifiers.dhSinglePass_stdDH_sha512kdf_scheme, "EC");
      registerOidAlgorithmParameters(param1ConfigurableProvider, SECObjectIdentifiers.dhSinglePass_cofactorDH_sha512kdf_scheme, "EC");
      if (!Properties.isOverrideSet("org.bouncycastle.ec.disable_mqv")) {
        param1ConfigurableProvider.addAlgorithm("KeyAgreement.ECMQV", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$MQV");
        param1ConfigurableProvider.addAlgorithm("KeyAgreement.ECMQVWITHSHA1CKDF", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$MQVwithSHA1CKDF");
        param1ConfigurableProvider.addAlgorithm("KeyAgreement.ECMQVWITHSHA224CKDF", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$MQVwithSHA224CKDF");
        param1ConfigurableProvider.addAlgorithm("KeyAgreement.ECMQVWITHSHA256CKDF", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$MQVwithSHA256CKDF");
        param1ConfigurableProvider.addAlgorithm("KeyAgreement.ECMQVWITHSHA384CKDF", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$MQVwithSHA384CKDF");
        param1ConfigurableProvider.addAlgorithm("KeyAgreement.ECMQVWITHSHA512CKDF", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$MQVwithSHA512CKDF");
        param1ConfigurableProvider.addAlgorithm("KeyAgreement." + X9ObjectIdentifiers.mqvSinglePass_sha1kdf_scheme, "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$MQVwithSHA1KDFAndSharedInfo");
        param1ConfigurableProvider.addAlgorithm("KeyAgreement." + SECObjectIdentifiers.mqvSinglePass_sha224kdf_scheme, "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$MQVwithSHA224KDFAndSharedInfo");
        param1ConfigurableProvider.addAlgorithm("KeyAgreement." + SECObjectIdentifiers.mqvSinglePass_sha256kdf_scheme, "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$MQVwithSHA256KDFAndSharedInfo");
        param1ConfigurableProvider.addAlgorithm("KeyAgreement." + SECObjectIdentifiers.mqvSinglePass_sha384kdf_scheme, "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$MQVwithSHA384KDFAndSharedInfo");
        param1ConfigurableProvider.addAlgorithm("KeyAgreement." + SECObjectIdentifiers.mqvSinglePass_sha512kdf_scheme, "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyAgreementSpi$MQVwithSHA512KDFAndSharedInfo");
        registerOid(param1ConfigurableProvider, X9ObjectIdentifiers.dhSinglePass_stdDH_sha1kdf_scheme, "EC", (AsymmetricKeyInfoConverter)new KeyFactorySpi.EC());
        registerOidAlgorithmParameters(param1ConfigurableProvider, X9ObjectIdentifiers.mqvSinglePass_sha1kdf_scheme, "EC");
        registerOid(param1ConfigurableProvider, SECObjectIdentifiers.mqvSinglePass_sha224kdf_scheme, "ECMQV", (AsymmetricKeyInfoConverter)new KeyFactorySpi.ECMQV());
        registerOidAlgorithmParameters(param1ConfigurableProvider, SECObjectIdentifiers.mqvSinglePass_sha256kdf_scheme, "EC");
        registerOid(param1ConfigurableProvider, SECObjectIdentifiers.mqvSinglePass_sha256kdf_scheme, "ECMQV", (AsymmetricKeyInfoConverter)new KeyFactorySpi.ECMQV());
        registerOidAlgorithmParameters(param1ConfigurableProvider, SECObjectIdentifiers.mqvSinglePass_sha224kdf_scheme, "EC");
        registerOid(param1ConfigurableProvider, SECObjectIdentifiers.mqvSinglePass_sha384kdf_scheme, "ECMQV", (AsymmetricKeyInfoConverter)new KeyFactorySpi.ECMQV());
        registerOidAlgorithmParameters(param1ConfigurableProvider, SECObjectIdentifiers.mqvSinglePass_sha384kdf_scheme, "EC");
        registerOid(param1ConfigurableProvider, SECObjectIdentifiers.mqvSinglePass_sha512kdf_scheme, "ECMQV", (AsymmetricKeyInfoConverter)new KeyFactorySpi.ECMQV());
        registerOidAlgorithmParameters(param1ConfigurableProvider, SECObjectIdentifiers.mqvSinglePass_sha512kdf_scheme, "EC");
        param1ConfigurableProvider.addAlgorithm("KeyFactory.ECMQV", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi$ECMQV");
        param1ConfigurableProvider.addAlgorithm("KeyPairGenerator.ECMQV", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyPairGeneratorSpi$ECMQV");
      } 
      param1ConfigurableProvider.addAlgorithm("KeyFactory.EC", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi$EC");
      param1ConfigurableProvider.addAlgorithm("KeyFactory.ECDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi$ECDSA");
      param1ConfigurableProvider.addAlgorithm("KeyFactory.ECDH", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi$ECDH");
      param1ConfigurableProvider.addAlgorithm("KeyFactory.ECDHC", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi$ECDHC");
      param1ConfigurableProvider.addAlgorithm("KeyPairGenerator.EC", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyPairGeneratorSpi$EC");
      param1ConfigurableProvider.addAlgorithm("KeyPairGenerator.ECDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyPairGeneratorSpi$ECDSA");
      param1ConfigurableProvider.addAlgorithm("KeyPairGenerator.ECDH", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyPairGeneratorSpi$ECDH");
      param1ConfigurableProvider.addAlgorithm("KeyPairGenerator.ECDHWITHSHA1KDF", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyPairGeneratorSpi$ECDH");
      param1ConfigurableProvider.addAlgorithm("KeyPairGenerator.ECDHC", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyPairGeneratorSpi$ECDHC");
      param1ConfigurableProvider.addAlgorithm("KeyPairGenerator.ECIES", "org.bouncycastle.jcajce.provider.asymmetric.ec.KeyPairGeneratorSpi$ECDH");
      param1ConfigurableProvider.addAlgorithm("Cipher.ECIES", "org.bouncycastle.jcajce.provider.asymmetric.ec.IESCipher$ECIES");
      param1ConfigurableProvider.addAlgorithm("Cipher.ECIESwithAES-CBC", "org.bouncycastle.jcajce.provider.asymmetric.ec.IESCipher$ECIESwithAESCBC");
      param1ConfigurableProvider.addAlgorithm("Cipher.ECIESWITHAES-CBC", "org.bouncycastle.jcajce.provider.asymmetric.ec.IESCipher$ECIESwithAESCBC");
      param1ConfigurableProvider.addAlgorithm("Cipher.ECIESwithDESEDE-CBC", "org.bouncycastle.jcajce.provider.asymmetric.ec.IESCipher$ECIESwithDESedeCBC");
      param1ConfigurableProvider.addAlgorithm("Cipher.ECIESWITHDESEDE-CBC", "org.bouncycastle.jcajce.provider.asymmetric.ec.IESCipher$ECIESwithDESedeCBC");
      param1ConfigurableProvider.addAlgorithm("Signature.ECDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecDSA");
      param1ConfigurableProvider.addAlgorithm("Signature.NONEwithECDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecDSAnone");
      param1ConfigurableProvider.addAlgorithm("Alg.Alias.Signature.SHA1withECDSA", "ECDSA");
      param1ConfigurableProvider.addAlgorithm("Alg.Alias.Signature.ECDSAwithSHA1", "ECDSA");
      param1ConfigurableProvider.addAlgorithm("Alg.Alias.Signature.SHA1WITHECDSA", "ECDSA");
      param1ConfigurableProvider.addAlgorithm("Alg.Alias.Signature.ECDSAWITHSHA1", "ECDSA");
      param1ConfigurableProvider.addAlgorithm("Alg.Alias.Signature.SHA1WithECDSA", "ECDSA");
      param1ConfigurableProvider.addAlgorithm("Alg.Alias.Signature.ECDSAWithSHA1", "ECDSA");
      param1ConfigurableProvider.addAlgorithm("Alg.Alias.Signature.1.2.840.10045.4.1", "ECDSA");
      param1ConfigurableProvider.addAlgorithm("Alg.Alias.Signature." + TeleTrusTObjectIdentifiers.ecSignWithSha1, "ECDSA");
      param1ConfigurableProvider.addAlgorithm("Signature.ECDDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecDetDSA");
      param1ConfigurableProvider.addAlgorithm("Signature.SHA1WITHECDDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecDetDSA");
      param1ConfigurableProvider.addAlgorithm("Signature.SHA224WITHECDDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecDetDSA224");
      param1ConfigurableProvider.addAlgorithm("Signature.SHA256WITHECDDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecDetDSA256");
      param1ConfigurableProvider.addAlgorithm("Signature.SHA384WITHECDDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecDetDSA384");
      param1ConfigurableProvider.addAlgorithm("Signature.SHA512WITHECDDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecDetDSA512");
      param1ConfigurableProvider.addAlgorithm("Signature.SHA3-224WITHECDDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecDetDSASha3_224");
      param1ConfigurableProvider.addAlgorithm("Signature.SHA3-256WITHECDDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecDetDSASha3_256");
      param1ConfigurableProvider.addAlgorithm("Signature.SHA3-384WITHECDDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecDetDSASha3_384");
      param1ConfigurableProvider.addAlgorithm("Signature.SHA3-512WITHECDDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecDetDSASha3_512");
      param1ConfigurableProvider.addAlgorithm("Alg.Alias.Signature.DETECDSA", "ECDDSA");
      param1ConfigurableProvider.addAlgorithm("Alg.Alias.Signature.SHA1WITHDETECDSA", "SHA1WITHECDDSA");
      param1ConfigurableProvider.addAlgorithm("Alg.Alias.Signature.SHA224WITHDETECDSA", "SHA224WITHECDDSA");
      param1ConfigurableProvider.addAlgorithm("Alg.Alias.Signature.SHA256WITHDETECDSA", "SHA256WITHECDDSA");
      param1ConfigurableProvider.addAlgorithm("Alg.Alias.Signature.SHA384WITHDETECDSA", "SHA384WITHECDDSA");
      param1ConfigurableProvider.addAlgorithm("Alg.Alias.Signature.SHA512WITHDETECDSA", "SHA512WITHECDDSA");
      addSignatureAlgorithm(param1ConfigurableProvider, "SHA224", "ECDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecDSA224", X9ObjectIdentifiers.ecdsa_with_SHA224);
      addSignatureAlgorithm(param1ConfigurableProvider, "SHA256", "ECDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecDSA256", X9ObjectIdentifiers.ecdsa_with_SHA256);
      addSignatureAlgorithm(param1ConfigurableProvider, "SHA384", "ECDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecDSA384", X9ObjectIdentifiers.ecdsa_with_SHA384);
      addSignatureAlgorithm(param1ConfigurableProvider, "SHA512", "ECDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecDSA512", X9ObjectIdentifiers.ecdsa_with_SHA512);
      addSignatureAlgorithm(param1ConfigurableProvider, "SHA3-224", "ECDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecDSASha3_224", NISTObjectIdentifiers.id_ecdsa_with_sha3_224);
      addSignatureAlgorithm(param1ConfigurableProvider, "SHA3-256", "ECDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecDSASha3_256", NISTObjectIdentifiers.id_ecdsa_with_sha3_256);
      addSignatureAlgorithm(param1ConfigurableProvider, "SHA3-384", "ECDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecDSASha3_384", NISTObjectIdentifiers.id_ecdsa_with_sha3_384);
      addSignatureAlgorithm(param1ConfigurableProvider, "SHA3-512", "ECDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecDSASha3_512", NISTObjectIdentifiers.id_ecdsa_with_sha3_512);
      addSignatureAlgorithm(param1ConfigurableProvider, "RIPEMD160", "ECDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecDSARipeMD160", TeleTrusTObjectIdentifiers.ecSignWithRipemd160);
      param1ConfigurableProvider.addAlgorithm("Signature.SHA1WITHECNR", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecNR");
      param1ConfigurableProvider.addAlgorithm("Signature.SHA224WITHECNR", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecNR224");
      param1ConfigurableProvider.addAlgorithm("Signature.SHA256WITHECNR", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecNR256");
      param1ConfigurableProvider.addAlgorithm("Signature.SHA384WITHECNR", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecNR384");
      param1ConfigurableProvider.addAlgorithm("Signature.SHA512WITHECNR", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecNR512");
      addSignatureAlgorithm(param1ConfigurableProvider, "SHA1", "CVC-ECDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecCVCDSA", EACObjectIdentifiers.id_TA_ECDSA_SHA_1);
      addSignatureAlgorithm(param1ConfigurableProvider, "SHA224", "CVC-ECDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecCVCDSA224", EACObjectIdentifiers.id_TA_ECDSA_SHA_224);
      addSignatureAlgorithm(param1ConfigurableProvider, "SHA256", "CVC-ECDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecCVCDSA256", EACObjectIdentifiers.id_TA_ECDSA_SHA_256);
      addSignatureAlgorithm(param1ConfigurableProvider, "SHA384", "CVC-ECDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecCVCDSA384", EACObjectIdentifiers.id_TA_ECDSA_SHA_384);
      addSignatureAlgorithm(param1ConfigurableProvider, "SHA512", "CVC-ECDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecCVCDSA512", EACObjectIdentifiers.id_TA_ECDSA_SHA_512);
      addSignatureAlgorithm(param1ConfigurableProvider, "SHA1", "PLAIN-ECDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecCVCDSA", BSIObjectIdentifiers.ecdsa_plain_SHA1);
      addSignatureAlgorithm(param1ConfigurableProvider, "SHA224", "PLAIN-ECDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecCVCDSA224", BSIObjectIdentifiers.ecdsa_plain_SHA224);
      addSignatureAlgorithm(param1ConfigurableProvider, "SHA256", "PLAIN-ECDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecCVCDSA256", BSIObjectIdentifiers.ecdsa_plain_SHA256);
      addSignatureAlgorithm(param1ConfigurableProvider, "SHA384", "PLAIN-ECDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecCVCDSA384", BSIObjectIdentifiers.ecdsa_plain_SHA384);
      addSignatureAlgorithm(param1ConfigurableProvider, "SHA512", "PLAIN-ECDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecCVCDSA512", BSIObjectIdentifiers.ecdsa_plain_SHA512);
      addSignatureAlgorithm(param1ConfigurableProvider, "RIPEMD160", "PLAIN-ECDSA", "org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi$ecPlainDSARP160", BSIObjectIdentifiers.ecdsa_plain_RIPEMD160);
    }
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jcajce\provider\asymmetric\EC.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */