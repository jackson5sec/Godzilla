package org.bouncycastle.crypto;

public interface DerivationFunction {
  void init(DerivationParameters paramDerivationParameters);
  
  int generateBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws DataLengthException, IllegalArgumentException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\DerivationFunction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */