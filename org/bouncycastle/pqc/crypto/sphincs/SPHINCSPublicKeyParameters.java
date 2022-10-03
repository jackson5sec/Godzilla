package org.bouncycastle.pqc.crypto.sphincs;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.util.Arrays;

public class SPHINCSPublicKeyParameters extends AsymmetricKeyParameter {
  private final byte[] keyData;
  
  public SPHINCSPublicKeyParameters(byte[] paramArrayOfbyte) {
    super(false);
    this.keyData = Arrays.clone(paramArrayOfbyte);
  }
  
  public byte[] getKeyData() {
    return Arrays.clone(this.keyData);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\pqc\crypto\sphincs\SPHINCSPublicKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */