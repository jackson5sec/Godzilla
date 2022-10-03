package org.bouncycastle.jcajce.provider.asymmetric;

import org.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.bouncycastle.jcajce.provider.util.AsymmetricAlgorithmProvider;

public class IES {
  private static final String PREFIX = "org.bouncycastle.jcajce.provider.asymmetric.ies.";
  
  public static class Mappings extends AsymmetricAlgorithmProvider {
    public void configure(ConfigurableProvider param1ConfigurableProvider) {
      param1ConfigurableProvider.addAlgorithm("AlgorithmParameters.IES", "org.bouncycastle.jcajce.provider.asymmetric.ies.AlgorithmParametersSpi");
      param1ConfigurableProvider.addAlgorithm("AlgorithmParameters.ECIES", "org.bouncycastle.jcajce.provider.asymmetric.ies.AlgorithmParametersSpi");
    }
  }
}


/* Location:              C:\User\\user\Downloads\godzilla.jar!\org\bouncycastle\jcajce\provider\asymmetric\IES.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */