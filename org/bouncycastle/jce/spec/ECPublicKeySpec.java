package org.bouncycastle.jce.spec;

import org.bouncycastle.math.ec.ECPoint;

public class ECPublicKeySpec extends ECKeySpec {
  private ECPoint q;
  
  public ECPublicKeySpec(ECPoint paramECPoint, ECParameterSpec paramECParameterSpec) {
    super(paramECParameterSpec);
    if (paramECPoint.getCurve() != null) {
      this.q = paramECPoint.normalize();
    } else {
      this.q = paramECPoint;
    } 
  }
  
  public ECPoint getQ() {
    return this.q;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jce\spec\ECPublicKeySpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */