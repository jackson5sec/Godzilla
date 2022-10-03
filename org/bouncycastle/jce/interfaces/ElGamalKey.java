package org.bouncycastle.jce.interfaces;

import javax.crypto.interfaces.DHKey;
import org.bouncycastle.jce.spec.ElGamalParameterSpec;

public interface ElGamalKey extends DHKey {
  ElGamalParameterSpec getParameters();
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jce\interfaces\ElGamalKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */