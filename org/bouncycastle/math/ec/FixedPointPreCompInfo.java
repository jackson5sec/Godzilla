package org.bouncycastle.math.ec;

public class FixedPointPreCompInfo implements PreCompInfo {
  protected ECPoint offset = null;
  
  protected ECPoint[] preComp = null;
  
  protected int width = -1;
  
  public ECPoint getOffset() {
    return this.offset;
  }
  
  public void setOffset(ECPoint paramECPoint) {
    this.offset = paramECPoint;
  }
  
  public ECPoint[] getPreComp() {
    return this.preComp;
  }
  
  public void setPreComp(ECPoint[] paramArrayOfECPoint) {
    this.preComp = paramArrayOfECPoint;
  }
  
  public int getWidth() {
    return this.width;
  }
  
  public void setWidth(int paramInt) {
    this.width = paramInt;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\math\ec\FixedPointPreCompInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */