package org.bouncycastle.pqc.crypto.newhope;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.util.Arrays;

public class NHPublicKeyParameters extends AsymmetricKeyParameter {
  final byte[] pubData;
  
  public NHPublicKeyParameters(byte[] paramArrayOfbyte) {
    super(false);
    this.pubData = Arrays.clone(paramArrayOfbyte);
  }
  
  public byte[] getPubData() {
    return Arrays.clone(this.pubData);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\pqc\crypto\newhope\NHPublicKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */