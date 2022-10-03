package org.bouncycastle.pqc.crypto.sphincs;

import java.security.SecureRandom;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.KeyGenerationParameters;

public class SPHINCS256KeyGenerationParameters extends KeyGenerationParameters {
  private final Digest treeDigest;
  
  public SPHINCS256KeyGenerationParameters(SecureRandom paramSecureRandom, Digest paramDigest) {
    super(paramSecureRandom, 8448);
    this.treeDigest = paramDigest;
  }
  
  public Digest getTreeDigest() {
    return this.treeDigest;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\pqc\crypto\sphincs\SPHINCS256KeyGenerationParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */