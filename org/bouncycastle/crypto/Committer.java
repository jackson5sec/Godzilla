package org.bouncycastle.crypto;

public interface Committer {
  Commitment commit(byte[] paramArrayOfbyte);
  
  boolean isRevealed(Commitment paramCommitment, byte[] paramArrayOfbyte);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\Committer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */