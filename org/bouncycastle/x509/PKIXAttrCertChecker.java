package org.bouncycastle.x509;

import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.util.Collection;
import java.util.Set;

public abstract class PKIXAttrCertChecker implements Cloneable {
  public abstract Set getSupportedExtensions();
  
  public abstract void check(X509AttributeCertificate paramX509AttributeCertificate, CertPath paramCertPath1, CertPath paramCertPath2, Collection paramCollection) throws CertPathValidatorException;
  
  public abstract Object clone();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\x509\PKIXAttrCertChecker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */