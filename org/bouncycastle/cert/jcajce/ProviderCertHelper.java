package org.bouncycastle.cert.jcajce;

import java.security.Provider;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

class ProviderCertHelper extends CertHelper {
  private final Provider provider;
  
  ProviderCertHelper(Provider paramProvider) {
    this.provider = paramProvider;
  }
  
  protected CertificateFactory createCertificateFactory(String paramString) throws CertificateException {
    return CertificateFactory.getInstance(paramString, this.provider);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cert\jcajce\ProviderCertHelper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */