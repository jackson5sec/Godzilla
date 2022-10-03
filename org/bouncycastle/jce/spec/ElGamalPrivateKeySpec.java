package org.bouncycastle.jce.spec;

import java.math.BigInteger;

public class ElGamalPrivateKeySpec extends ElGamalKeySpec {
  private BigInteger x;
  
  public ElGamalPrivateKeySpec(BigInteger paramBigInteger, ElGamalParameterSpec paramElGamalParameterSpec) {
    super(paramElGamalParameterSpec);
    this.x = paramBigInteger;
  }
  
  public BigInteger getX() {
    return this.x;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jce\spec\ElGamalPrivateKeySpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */