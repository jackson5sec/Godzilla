package org.bouncycastle.crypto.tls;

import java.io.ByteArrayOutputStream;
import org.bouncycastle.crypto.Signer;

class SignerInputBuffer extends ByteArrayOutputStream {
  void updateSigner(Signer paramSigner) {
    paramSigner.update(this.buf, 0, this.count);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\SignerInputBuffer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */