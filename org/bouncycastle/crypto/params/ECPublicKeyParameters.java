package org.bouncycastle.crypto.params;

import org.bouncycastle.math.ec.ECPoint;

public class ECPublicKeyParameters extends ECKeyParameters {
  private final ECPoint Q;
  
  public ECPublicKeyParameters(ECPoint paramECPoint, ECDomainParameters paramECDomainParameters) {
    super(false, paramECDomainParameters);
    this.Q = validate(paramECPoint);
  }
  
  private ECPoint validate(ECPoint paramECPoint) {
    if (paramECPoint == null)
      throw new IllegalArgumentException("point has null value"); 
    if (paramECPoint.isInfinity())
      throw new IllegalArgumentException("point at infinity"); 
    paramECPoint = paramECPoint.normalize();
    if (!paramECPoint.isValid())
      throw new IllegalArgumentException("point not on curve"); 
    return paramECPoint;
  }
  
  public ECPoint getQ() {
    return this.Q;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\params\ECPublicKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */