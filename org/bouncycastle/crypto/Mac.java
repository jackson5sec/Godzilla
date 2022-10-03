package org.bouncycastle.crypto;

public interface Mac {
  void init(CipherParameters paramCipherParameters) throws IllegalArgumentException;
  
  String getAlgorithmName();
  
  int getMacSize();
  
  void update(byte paramByte) throws IllegalStateException;
  
  void update(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws DataLengthException, IllegalStateException;
  
  int doFinal(byte[] paramArrayOfbyte, int paramInt) throws DataLengthException, IllegalStateException;
  
  void reset();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\Mac.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */