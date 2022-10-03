package org.bouncycastle.cert.path;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.util.Memoable;

public interface CertPathValidation extends Memoable {
  void validate(CertPathValidationContext paramCertPathValidationContext, X509CertificateHolder paramX509CertificateHolder) throws CertPathValidationException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cert\path\CertPathValidation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */