package org.bouncycastle.cert.path.validations;

import org.bouncycastle.cert.X509CertificateHolder;

class ValidationUtils {
  static boolean isSelfIssued(X509CertificateHolder paramX509CertificateHolder) {
    return paramX509CertificateHolder.getSubject().equals(paramX509CertificateHolder.getIssuer());
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cert\path\validations\ValidationUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */