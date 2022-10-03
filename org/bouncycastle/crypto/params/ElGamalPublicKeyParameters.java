package org.bouncycastle.crypto.params;

import java.math.BigInteger;

public class ElGamalPublicKeyParameters extends ElGamalKeyParameters {
  private BigInteger y;
  
  public ElGamalPublicKeyParameters(BigInteger paramBigInteger, ElGamalParameters paramElGamalParameters) {
    super(false, paramElGamalParameters);
    this.y = paramBigInteger;
  }
  
  public BigInteger getY() {
    return this.y;
  }
  
  public int hashCode() {
    return this.y.hashCode() ^ super.hashCode();
  }
  
  public boolean equals(Object paramObject) {
    if (!(paramObject instanceof ElGamalPublicKeyParameters))
      return false; 
    ElGamalPublicKeyParameters elGamalPublicKeyParameters = (ElGamalPublicKeyParameters)paramObject;
    return (elGamalPublicKeyParameters.getY().equals(this.y) && super.equals(paramObject));
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\params\ElGamalPublicKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */