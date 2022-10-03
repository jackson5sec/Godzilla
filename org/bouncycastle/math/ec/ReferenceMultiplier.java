package org.bouncycastle.math.ec;

import java.math.BigInteger;

public class ReferenceMultiplier extends AbstractECMultiplier {
  protected ECPoint multiplyPositive(ECPoint paramECPoint, BigInteger paramBigInteger) {
    return ECAlgorithms.referenceMultiply(paramECPoint, paramBigInteger);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\math\ec\ReferenceMultiplier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */