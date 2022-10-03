package org.bouncycastle.math.ec;

public class WNafPreCompInfo implements PreCompInfo {
  protected ECPoint[] preComp = null;
  
  protected ECPoint[] preCompNeg = null;
  
  protected ECPoint twice = null;
  
  public ECPoint[] getPreComp() {
    return this.preComp;
  }
  
  public void setPreComp(ECPoint[] paramArrayOfECPoint) {
    this.preComp = paramArrayOfECPoint;
  }
  
  public ECPoint[] getPreCompNeg() {
    return this.preCompNeg;
  }
  
  public void setPreCompNeg(ECPoint[] paramArrayOfECPoint) {
    this.preCompNeg = paramArrayOfECPoint;
  }
  
  public ECPoint getTwice() {
    return this.twice;
  }
  
  public void setTwice(ECPoint paramECPoint) {
    this.twice = paramECPoint;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\math\ec\WNafPreCompInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */