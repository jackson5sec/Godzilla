package org.bouncycastle.crypto.ec;

import org.bouncycastle.crypto.CipherParameters;

public interface ECPairTransform {
  void init(CipherParameters paramCipherParameters);
  
  ECPair transform(ECPair paramECPair);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\ec\ECPairTransform.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */