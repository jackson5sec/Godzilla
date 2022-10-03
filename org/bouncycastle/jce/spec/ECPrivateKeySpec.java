package org.bouncycastle.jce.spec;

import java.math.BigInteger;

public class ECPrivateKeySpec extends ECKeySpec {
  private BigInteger d;
  
  public ECPrivateKeySpec(BigInteger paramBigInteger, ECParameterSpec paramECParameterSpec) {
    super(paramECParameterSpec);
    this.d = paramBigInteger;
  }
  
  public BigInteger getD() {
    return this.d;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jce\spec\ECPrivateKeySpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */