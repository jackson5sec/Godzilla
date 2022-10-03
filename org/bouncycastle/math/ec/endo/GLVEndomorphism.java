package org.bouncycastle.math.ec.endo;

import java.math.BigInteger;

public interface GLVEndomorphism extends ECEndomorphism {
  BigInteger[] decomposeScalar(BigInteger paramBigInteger);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\math\ec\endo\GLVEndomorphism.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */