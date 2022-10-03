package org.bouncycastle.operator;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public abstract class SymmetricKeyWrapper implements KeyWrapper {
  private AlgorithmIdentifier algorithmId;
  
  protected SymmetricKeyWrapper(AlgorithmIdentifier paramAlgorithmIdentifier) {
    this.algorithmId = paramAlgorithmIdentifier;
  }
  
  public AlgorithmIdentifier getAlgorithmIdentifier() {
    return this.algorithmId;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\operator\SymmetricKeyWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */