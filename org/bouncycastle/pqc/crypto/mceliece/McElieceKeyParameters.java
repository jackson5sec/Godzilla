package org.bouncycastle.pqc.crypto.mceliece;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

public class McElieceKeyParameters extends AsymmetricKeyParameter {
  private McElieceParameters params;
  
  public McElieceKeyParameters(boolean paramBoolean, McElieceParameters paramMcElieceParameters) {
    super(paramBoolean);
    this.params = paramMcElieceParameters;
  }
  
  public McElieceParameters getParameters() {
    return this.params;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\pqc\crypto\mceliece\McElieceKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */