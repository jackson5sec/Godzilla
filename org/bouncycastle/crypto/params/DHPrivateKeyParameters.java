package org.bouncycastle.crypto.params;

import java.math.BigInteger;

public class DHPrivateKeyParameters extends DHKeyParameters {
  private BigInteger x;
  
  public DHPrivateKeyParameters(BigInteger paramBigInteger, DHParameters paramDHParameters) {
    super(true, paramDHParameters);
    this.x = paramBigInteger;
  }
  
  public BigInteger getX() {
    return this.x;
  }
  
  public int hashCode() {
    return this.x.hashCode() ^ super.hashCode();
  }
  
  public boolean equals(Object paramObject) {
    if (!(paramObject instanceof DHPrivateKeyParameters))
      return false; 
    DHPrivateKeyParameters dHPrivateKeyParameters = (DHPrivateKeyParameters)paramObject;
    return (dHPrivateKeyParameters.getX().equals(this.x) && super.equals(paramObject));
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\params\DHPrivateKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */