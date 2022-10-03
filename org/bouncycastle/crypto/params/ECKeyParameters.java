package org.bouncycastle.crypto.params;

public class ECKeyParameters extends AsymmetricKeyParameter {
  ECDomainParameters params;
  
  protected ECKeyParameters(boolean paramBoolean, ECDomainParameters paramECDomainParameters) {
    super(paramBoolean);
    this.params = paramECDomainParameters;
  }
  
  public ECDomainParameters getParameters() {
    return this.params;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\params\ECKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */