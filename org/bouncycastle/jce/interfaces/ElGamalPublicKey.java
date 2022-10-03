package org.bouncycastle.jce.interfaces;

import java.math.BigInteger;
import javax.crypto.interfaces.DHPublicKey;

public interface ElGamalPublicKey extends ElGamalKey, DHPublicKey {
  BigInteger getY();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jce\interfaces\ElGamalPublicKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */