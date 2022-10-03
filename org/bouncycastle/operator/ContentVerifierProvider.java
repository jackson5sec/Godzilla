package org.bouncycastle.operator;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.cert.X509CertificateHolder;

public interface ContentVerifierProvider {
  boolean hasAssociatedCertificate();
  
  X509CertificateHolder getAssociatedCertificate();
  
  ContentVerifier get(AlgorithmIdentifier paramAlgorithmIdentifier) throws OperatorCreationException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\operator\ContentVerifierProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */