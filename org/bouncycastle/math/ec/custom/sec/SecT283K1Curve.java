package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECMultiplier;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.math.ec.WTauNafMultiplier;

public class SecT283K1Curve extends ECCurve.AbstractF2m {
  private static final int SecT283K1_DEFAULT_COORDS = 6;
  
  protected SecT283K1Point infinity = new SecT283K1Point((ECCurve)this, null, null);
  
  public SecT283K1Curve() {
    super(283, 5, 7, 12);
  }
  
  protected ECCurve cloneCurve() {
    return (ECCurve)new SecT283K1Curve();
  }
  
  public boolean supportsCoordinateSystem(int paramInt) {
    switch (paramInt) {
      case 6:
        return true;
    } 
    return false;
  }
  
  protected ECMultiplier createDefaultMultiplier() {
    return (ECMultiplier)new WTauNafMultiplier();
  }
  
  public int getFieldSize() {
    return 283;
  }
  
  public ECFieldElement fromBigInteger(BigInteger paramBigInteger) {
    return new SecT283FieldElement(paramBigInteger);
  }
  
  protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, boolean paramBoolean) {
    return (ECPoint)new SecT283K1Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2, paramBoolean);
  }
  
  protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement, boolean paramBoolean) {
    return (ECPoint)new SecT283K1Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement, paramBoolean);
  }
  
  public ECPoint getInfinity() {
    return (ECPoint)this.infinity;
  }
  
  public boolean isKoblitz() {
    return true;
  }
  
  public int getM() {
    return 283;
  }
  
  public boolean isTrinomial() {
    return false;
  }
  
  public int getK1() {
    return 5;
  }
  
  public int getK2() {
    return 7;
  }
  
  public int getK3() {
    return 12;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\math\ec\custom\sec\SecT283K1Curve.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */