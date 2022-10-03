package org.bouncycastle.openssl;

public interface PEMEncryptor {
  String getAlgorithm();
  
  byte[] getIV();
  
  byte[] encrypt(byte[] paramArrayOfbyte) throws PEMException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\openssl\PEMEncryptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */