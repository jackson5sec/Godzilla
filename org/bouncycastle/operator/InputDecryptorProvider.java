package org.bouncycastle.operator;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface InputDecryptorProvider {
  InputDecryptor get(AlgorithmIdentifier paramAlgorithmIdentifier) throws OperatorCreationException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\operator\InputDecryptorProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */