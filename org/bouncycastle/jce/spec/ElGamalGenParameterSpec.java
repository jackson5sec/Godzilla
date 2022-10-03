package org.bouncycastle.jce.spec;

import java.security.spec.AlgorithmParameterSpec;

public class ElGamalGenParameterSpec implements AlgorithmParameterSpec {
  private int primeSize;
  
  public ElGamalGenParameterSpec(int paramInt) {
    this.primeSize = paramInt;
  }
  
  public int getPrimeSize() {
    return this.primeSize;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jce\spec\ElGamalGenParameterSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */