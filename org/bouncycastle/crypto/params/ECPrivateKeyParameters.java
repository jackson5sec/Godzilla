package org.bouncycastle.crypto.params;

import java.math.BigInteger;

public class ECPrivateKeyParameters extends ECKeyParameters {
  BigInteger d;
  
  public ECPrivateKeyParameters(BigInteger paramBigInteger, ECDomainParameters paramECDomainParameters) {
    super(true, paramECDomainParameters);
    this.d = paramBigInteger;
  }
  
  public BigInteger getD() {
    return this.d;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\params\ECPrivateKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */