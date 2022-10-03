package org.bouncycastle.crypto.tls;

import java.io.IOException;

interface DTLSHandshakeRetransmit {
  void receivedHandshakeRecord(int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3) throws IOException;
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\tls\DTLSHandshakeRetransmit.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */