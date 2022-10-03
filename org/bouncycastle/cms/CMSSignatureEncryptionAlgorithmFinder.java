package org.bouncycastle.cms;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface CMSSignatureEncryptionAlgorithmFinder {
  AlgorithmIdentifier findEncryptionAlgorithm(AlgorithmIdentifier paramAlgorithmIdentifier);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\cms\CMSSignatureEncryptionAlgorithmFinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */