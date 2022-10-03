package org.bouncycastle.operator;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public abstract class SymmetricKeyUnwrapper implements KeyUnwrapper {
  private AlgorithmIdentifier algorithmId;
  
  protected SymmetricKeyUnwrapper(AlgorithmIdentifier paramAlgorithmIdentifier) {
    this.algorithmId = paramAlgorithmIdentifier;
  }
  
  public AlgorithmIdentifier getAlgorithmIdentifier() {
    return this.algorithmId;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\operator\SymmetricKeyUnwrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */