package org.bouncycastle.crypto.tls;

import java.io.IOException;

public interface TlsCipher {
  int getPlaintextLimit(int paramInt);
  
  byte[] encodePlaintext(long paramLong, short paramShort, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;
  
  byte[] decodeCiphertext(long paramLong, short paramShort, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\TlsCipher.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */