package org.bouncycastle.jce.spec;

import java.security.spec.AlgorithmParameterSpec;

public class ECNamedCurveGenParameterSpec implements AlgorithmParameterSpec {
  private String name;
  
  public ECNamedCurveGenParameterSpec(String paramString) {
    this.name = paramString;
  }
  
  public String getName() {
    return this.name;
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jce\spec\ECNamedCurveGenParameterSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */