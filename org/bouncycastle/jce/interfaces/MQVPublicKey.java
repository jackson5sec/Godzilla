package org.bouncycastle.jce.interfaces;

import java.security.PublicKey;

public interface MQVPublicKey extends PublicKey {
  PublicKey getStaticKey();
  
  PublicKey getEphemeralKey();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jce\interfaces\MQVPublicKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */