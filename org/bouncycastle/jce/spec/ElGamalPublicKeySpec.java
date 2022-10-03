package org.bouncycastle.jce.spec;

import java.math.BigInteger;

public class ElGamalPublicKeySpec extends ElGamalKeySpec {
  private BigInteger y;
  
  public ElGamalPublicKeySpec(BigInteger paramBigInteger, ElGamalParameterSpec paramElGamalParameterSpec) {
    super(paramElGamalParameterSpec);
    this.y = paramBigInteger;
  }
  
  public BigInteger getY() {
    return this.y;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jce\spec\ElGamalPublicKeySpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */