package org.bouncycastle.cert.path;

class CertPathValidationResultBuilder {
  public CertPathValidationResult build() {
    return new CertPathValidationResult(null, 0, 0, null);
  }
  
  public void addException(CertPathValidationException paramCertPathValidationException) {}
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cert\path\CertPathValidationResultBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */