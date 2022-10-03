package org.bouncycastle.crypto.params;

public class DSAKeyParameters extends AsymmetricKeyParameter {
  private DSAParameters params;
  
  public DSAKeyParameters(boolean paramBoolean, DSAParameters paramDSAParameters) {
    super(paramBoolean);
    this.params = paramDSAParameters;
  }
  
  public DSAParameters getParameters() {
    return this.params;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\params\DSAKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */