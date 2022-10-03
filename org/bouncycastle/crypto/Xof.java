package org.bouncycastle.crypto;

public interface Xof extends ExtendedDigest {
  int doFinal(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  int doOutput(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\Xof.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */