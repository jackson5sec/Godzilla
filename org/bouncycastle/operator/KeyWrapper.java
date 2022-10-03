package org.bouncycastle.operator;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public interface KeyWrapper {
  AlgorithmIdentifier getAlgorithmIdentifier();
  
  byte[] generateWrappedKey(GenericKey paramGenericKey) throws OperatorException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\operator\KeyWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */