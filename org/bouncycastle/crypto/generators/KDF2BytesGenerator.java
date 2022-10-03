package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.Digest;

public class KDF2BytesGenerator extends BaseKDFBytesGenerator {
  public KDF2BytesGenerator(Digest paramDigest) {
    super(1, paramDigest);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\generators\KDF2BytesGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */