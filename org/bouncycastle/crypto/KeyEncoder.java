package org.bouncycastle.crypto;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

public interface KeyEncoder {
  byte[] getEncoded(AsymmetricKeyParameter paramAsymmetricKeyParameter);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\KeyEncoder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */