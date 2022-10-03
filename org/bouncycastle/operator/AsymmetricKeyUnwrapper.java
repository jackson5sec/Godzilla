package org.bouncycastle.operator;

import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public abstract class AsymmetricKeyUnwrapper implements KeyUnwrapper {
  private AlgorithmIdentifier algorithmId;
  
  protected AsymmetricKeyUnwrapper(AlgorithmIdentifier paramAlgorithmIdentifier) {
    this.algorithmId = paramAlgorithmIdentifier;
  }
  
  public AlgorithmIdentifier getAlgorithmIdentifier() {
    return this.algorithmId;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\operator\AsymmetricKeyUnwrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */