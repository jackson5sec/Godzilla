package org.bouncycastle.jce.interfaces;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface MQVPrivateKey extends PrivateKey {
  PrivateKey getStaticPrivateKey();
  
  PrivateKey getEphemeralPrivateKey();
  
  PublicKey getEphemeralPublicKey();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jce\interfaces\MQVPrivateKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */