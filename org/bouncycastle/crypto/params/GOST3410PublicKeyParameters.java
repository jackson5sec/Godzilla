package org.bouncycastle.crypto.params;

import java.math.BigInteger;

public class GOST3410PublicKeyParameters extends GOST3410KeyParameters {
  private BigInteger y;
  
  public GOST3410PublicKeyParameters(BigInteger paramBigInteger, GOST3410Parameters paramGOST3410Parameters) {
    super(false, paramGOST3410Parameters);
    this.y = paramBigInteger;
  }
  
  public BigInteger getY() {
    return this.y;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\params\GOST3410PublicKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */