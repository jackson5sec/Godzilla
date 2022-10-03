package org.bouncycastle.pqc.crypto;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

public interface StateAwareMessageSigner extends MessageSigner {
  AsymmetricKeyParameter getUpdatedPrivateKey();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\pqc\crypto\StateAwareMessageSigner.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */