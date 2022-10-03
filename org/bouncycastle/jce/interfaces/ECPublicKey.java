package org.bouncycastle.jce.interfaces;

import java.security.PublicKey;
import org.bouncycastle.math.ec.ECPoint;

public interface ECPublicKey extends ECKey, PublicKey {
  ECPoint getQ();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jce\interfaces\ECPublicKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */