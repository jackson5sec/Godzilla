package org.bouncycastle.pqc.crypto.sphincs;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.util.Arrays;

public class SPHINCSPrivateKeyParameters extends AsymmetricKeyParameter {
  private final byte[] keyData;
  
  public SPHINCSPrivateKeyParameters(byte[] paramArrayOfbyte) {
    super(true);
    this.keyData = Arrays.clone(paramArrayOfbyte);
  }
  
  public byte[] getKeyData() {
    return Arrays.clone(this.keyData);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\pqc\crypto\sphincs\SPHINCSPrivateKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */