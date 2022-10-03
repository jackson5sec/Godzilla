package org.bouncycastle.crypto.params;

public class ElGamalKeyParameters extends AsymmetricKeyParameter {
  private ElGamalParameters params;
  
  protected ElGamalKeyParameters(boolean paramBoolean, ElGamalParameters paramElGamalParameters) {
    super(paramBoolean);
    this.params = paramElGamalParameters;
  }
  
  public ElGamalParameters getParameters() {
    return this.params;
  }
  
  public int hashCode() {
    return (this.params != null) ? this.params.hashCode() : 0;
  }
  
  public boolean equals(Object paramObject) {
    if (!(paramObject instanceof ElGamalKeyParameters))
      return false; 
    ElGamalKeyParameters elGamalKeyParameters = (ElGamalKeyParameters)paramObject;
    return (this.params == null) ? ((elGamalKeyParameters.getParameters() == null)) : this.params.equals(elGamalKeyParameters.getParameters());
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\params\ElGamalKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */