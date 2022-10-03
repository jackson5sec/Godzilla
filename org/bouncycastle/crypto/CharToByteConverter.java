package org.bouncycastle.crypto;

public interface CharToByteConverter {
  String getType();
  
  byte[] convert(char[] paramArrayOfchar);
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\CharToByteConverter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */