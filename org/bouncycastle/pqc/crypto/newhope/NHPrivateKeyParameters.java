package org.bouncycastle.pqc.crypto.newhope;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.util.Arrays;

public class NHPrivateKeyParameters extends AsymmetricKeyParameter {
  final short[] secData;
  
  public NHPrivateKeyParameters(short[] paramArrayOfshort) {
    super(true);
    this.secData = Arrays.clone(paramArrayOfshort);
  }
  
  public short[] getSecData() {
    return Arrays.clone(this.secData);
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\pqc\crypto\newhope\NHPrivateKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */