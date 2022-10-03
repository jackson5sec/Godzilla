package org.bouncycastle.jce.interfaces;

import java.math.BigInteger;
import java.security.PrivateKey;

public interface ECPrivateKey extends ECKey, PrivateKey {
  BigInteger getD();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jce\interfaces\ECPrivateKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */