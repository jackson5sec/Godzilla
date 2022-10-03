package org.bouncycastle.crypto.ec;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.math.ec.ECPoint;

public interface ECEncryptor {
  void init(CipherParameters paramCipherParameters);
  
  ECPair encrypt(ECPoint paramECPoint);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\ec\ECEncryptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */