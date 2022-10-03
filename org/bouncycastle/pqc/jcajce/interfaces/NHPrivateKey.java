package org.bouncycastle.pqc.jcajce.interfaces;

import java.security.PrivateKey;

public interface NHPrivateKey extends NHKey, PrivateKey {
  short[] getSecretData();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\pqc\jcajce\interfaces\NHPrivateKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */