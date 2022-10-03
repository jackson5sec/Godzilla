package org.bouncycastle.crypto;

import java.math.BigInteger;

public interface BasicAgreement {
  void init(CipherParameters paramCipherParameters);
  
  int getFieldSize();
  
  BigInteger calculateAgreement(CipherParameters paramCipherParameters);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\BasicAgreement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */