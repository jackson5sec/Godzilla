package org.bouncycastle.jce.interfaces;

import java.math.BigInteger;
import javax.crypto.interfaces.DHPrivateKey;

public interface ElGamalPrivateKey extends ElGamalKey, DHPrivateKey {
  BigInteger getX();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jce\interfaces\ElGamalPrivateKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */