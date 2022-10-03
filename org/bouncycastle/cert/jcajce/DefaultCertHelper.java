package org.bouncycastle.cert.jcajce;

import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

class DefaultCertHelper extends CertHelper {
  protected CertificateFactory createCertificateFactory(String paramString) throws CertificateException {
    return CertificateFactory.getInstance(paramString);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cert\jcajce\DefaultCertHelper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */