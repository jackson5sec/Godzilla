package org.bouncycastle.cms.bc;

import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.KeyTransRecipientInfoGenerator;
import org.bouncycastle.operator.AsymmetricKeyWrapper;
import org.bouncycastle.operator.bc.BcAsymmetricKeyWrapper;

public abstract class BcKeyTransRecipientInfoGenerator extends KeyTransRecipientInfoGenerator {
  public BcKeyTransRecipientInfoGenerator(X509CertificateHolder paramX509CertificateHolder, BcAsymmetricKeyWrapper paramBcAsymmetricKeyWrapper) {
    super(new IssuerAndSerialNumber(paramX509CertificateHolder.toASN1Structure()), (AsymmetricKeyWrapper)paramBcAsymmetricKeyWrapper);
  }
  
  public BcKeyTransRecipientInfoGenerator(byte[] paramArrayOfbyte, BcAsymmetricKeyWrapper paramBcAsymmetricKeyWrapper) {
    super(paramArrayOfbyte, (AsymmetricKeyWrapper)paramBcAsymmetricKeyWrapper);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cms\bc\BcKeyTransRecipientInfoGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */