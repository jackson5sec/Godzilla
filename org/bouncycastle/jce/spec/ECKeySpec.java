package org.bouncycastle.jce.spec;

import java.security.spec.KeySpec;

public class ECKeySpec implements KeySpec {
  private ECParameterSpec spec;
  
  protected ECKeySpec(ECParameterSpec paramECParameterSpec) {
    this.spec = paramECParameterSpec;
  }
  
  public ECParameterSpec getParams() {
    return this.spec;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jce\spec\ECKeySpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */