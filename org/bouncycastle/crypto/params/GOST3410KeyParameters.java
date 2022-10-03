package org.bouncycastle.crypto.params;

public class GOST3410KeyParameters extends AsymmetricKeyParameter {
  private GOST3410Parameters params;
  
  public GOST3410KeyParameters(boolean paramBoolean, GOST3410Parameters paramGOST3410Parameters) {
    super(paramBoolean);
    this.params = paramGOST3410Parameters;
  }
  
  public GOST3410Parameters getParameters() {
    return this.params;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\params\GOST3410KeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */