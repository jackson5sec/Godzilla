package org.bouncycastle.crypto.params;

public class CramerShoupKeyParameters extends AsymmetricKeyParameter {
  private CramerShoupParameters params;
  
  protected CramerShoupKeyParameters(boolean paramBoolean, CramerShoupParameters paramCramerShoupParameters) {
    super(paramBoolean);
    this.params = paramCramerShoupParameters;
  }
  
  public CramerShoupParameters getParameters() {
    return this.params;
  }
  
  public boolean equals(Object paramObject) {
    if (!(paramObject instanceof CramerShoupKeyParameters))
      return false; 
    CramerShoupKeyParameters cramerShoupKeyParameters = (CramerShoupKeyParameters)paramObject;
    return (this.params == null) ? ((cramerShoupKeyParameters.getParameters() == null)) : this.params.equals(cramerShoupKeyParameters.getParameters());
  }
  
  public int hashCode() {
    int i = isPrivate() ? 0 : 1;
    if (this.params != null)
      i ^= this.params.hashCode(); 
    return i;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\crypto\params\CramerShoupKeyParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */