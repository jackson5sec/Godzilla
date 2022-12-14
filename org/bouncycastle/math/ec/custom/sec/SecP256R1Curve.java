package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;

public class SecP256R1Curve extends ECCurve.AbstractFp {
  public static final BigInteger q = new BigInteger(1, Hex.decode("FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF"));
  
  private static final int SecP256R1_DEFAULT_COORDS = 2;
  
  protected SecP256R1Point infinity = new SecP256R1Point((ECCurve)this, null, null);
  
  public SecP256R1Curve() {
    super(q);
  }
  
  protected ECCurve cloneCurve() {
    return (ECCurve)new SecP256R1Curve();
  }
  
  public boolean supportsCoordinateSystem(int paramInt) {
    switch (paramInt) {
      case 2:
        return true;
    } 
    return false;
  }
  
  public BigInteger getQ() {
    return q;
  }
  
  public int getFieldSize() {
    return q.bitLength();
  }
  
  public ECFieldElement fromBigInteger(BigInteger paramBigInteger) {
    return new SecP256R1FieldElement(paramBigInteger);
  }
  
  protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, boolean paramBoolean) {
    return (ECPoint)new SecP256R1Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2, paramBoolean);
  }
  
  protected ECPoint createRawPoint(ECFieldElement paramECFieldElement1, ECFieldElement paramECFieldElement2, ECFieldElement[] paramArrayOfECFieldElement, boolean paramBoolean) {
    return (ECPoint)new SecP256R1Point((ECCurve)this, paramECFieldElement1, paramECFieldElement2, paramArrayOfECFieldElement, paramBoolean);
  }
  
  public ECPoint getInfinity() {
    return (ECPoint)this.infinity;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\math\ec\custom\sec\SecP256R1Curve.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */