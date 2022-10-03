package org.bouncycastle.crypto.tls;

import java.io.ByteArrayOutputStream;
import org.bouncycastle.crypto.Digest;

class DigestInputBuffer extends ByteArrayOutputStream {
  void updateDigest(Digest paramDigest) {
    paramDigest.update(this.buf, 0, this.count);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\DigestInputBuffer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */