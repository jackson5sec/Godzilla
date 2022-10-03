package org.bouncycastle.operator;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface KeyUnwrapper {
  AlgorithmIdentifier getAlgorithmIdentifier();
  
  GenericKey generateUnwrappedKey(AlgorithmIdentifier paramAlgorithmIdentifier, byte[] paramArrayOfbyte) throws OperatorException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\operator\KeyUnwrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */