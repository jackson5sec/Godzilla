package org.bouncycastle.math.ec;

public class WTauNafPreCompInfo implements PreCompInfo {
  protected ECPoint.AbstractF2m[] preComp = null;
  
  public ECPoint.AbstractF2m[] getPreComp() {
    return this.preComp;
  }
  
  public void setPreComp(ECPoint.AbstractF2m[] paramArrayOfAbstractF2m) {
    this.preComp = paramArrayOfAbstractF2m;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\math\ec\WTauNafPreCompInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */